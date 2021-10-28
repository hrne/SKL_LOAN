package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
/* 逾期放款明細 */
public class LM009ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM009.findAll ");

		int iYRMO = (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000) / 100;

		String sql = "";
		sql += "      SELECT C.\"AcctItem\" ";
		sql += "            ,CdC.\"Item\" AS \"AcSubBookItem\" ";
		sql += "            ,SUM(S1.\"Count\") AS \"Count\" ";
		sql += "            ,SUM(\"Interest\") AS \"Interest\" ";
		sql += "            ,S1.\"AcctCode\" F4 ";
		sql += "            ,S1.\"AcSubBookCode\" F5 ";
		sql += "      FROM (SELECT A.\"AcctCode\" AS \"AcctCode\" ";
		sql += "                  ,A.\"AcSubBookCode\" AS \"AcSubBookCode\" ";
		sql += "                  ,1 AS \"Count\" ";
		sql += "                  ,A.\"CustNo\" ";
		sql += "                  ,SUM(A.\"Interest\") AS \"Interest\" ";
		sql += "            FROM \"AcLoanInt\" A ";
		sql += "            WHERE A.\"YearMonth\" = :iyemo ";
		sql += "              AND A.\"AcctCode\" IN ('IC1', 'IC2', 'IC3', 'IC4') ";
		sql += "              AND A.\"Interest\" > 0 ";
		sql += "            GROUP BY A.\"AcctCode\", A.\"AcSubBookCode\", A.\"CustNo\") S1 ";
		sql += "      LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = DECODE(S1.\"AcctCode\", 'IC1', '310', 'IC2', '320', 'IC3', '330', 'IC4', '340') ";
		sql += "      LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcSubBookCode' AND CdC.\"Code\" = S1.\"AcSubBookCode\" ";
		sql += "      GROUP BY C.\"AcctItem\" ";
		sql += "              ,S1.\"AcctCode\" ";
		sql += "              ,CdC.\"Item\" ";
		sql += "              ,S1.\"AcSubBookCode\" ";
		sql += "      ORDER BY S1.\"AcctCode\" ";
		sql += "              ,S1.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("iyemo", iYRMO);

		return this.convertToMap(query);
	}

}