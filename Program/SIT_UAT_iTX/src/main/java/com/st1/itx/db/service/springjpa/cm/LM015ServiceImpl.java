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

@Service
@Repository
/* 逾期放款明細 */
public class LM015ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM015.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);
		this.info("entdy == >" + entdy);
		String sql = "SELECT \"F1\"";
		sql += "              , \"F2\"";
		sql += "              , ROUND(SUM(F3),0)";
		sql += "        FROM ( SELECT DECODE(M.\"AcctCode\", '990', 1, 0) AS F1";
		sql += "                    , NVL(A.\"CityGroup\",'A') AS F2";
		sql += "                    , NVL(MBV.\"BookValue\",M.\"LoanBalance\") AS F3";
		sql += "               FROM \"MonthlyLoanBal\" M";
		sql += "               LEFT JOIN(SELECT \"CityGroup\"";
		sql += "               		           ,\"CityCode\"";
		sql += "                         FROM \"CdArea\"";
		sql += "               	   		 GROUP BY \"CityGroup\"";
		sql += "               					 ,\"CityCode\") A";
		sql += "               ON A.\"CityCode\" = M.\"CityCode\"";
		// BookValue
		sql += "               LEFT JOIN \"Ias39IntMethod\" MBV ON MBV.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                                    			 AND MBV.\"CustNo\" = M.\"CustNo\"";
		sql += "                                     			 AND MBV.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                     			 AND MBV.\"BormNo\" = M.\"BormNo\"";
		sql += "               WHERE M.\"YearMonth\" = :entdy)";
		sql += "        GROUP BY \"F1\", \"F2\" ";
		sql += "        ORDER BY \"F1\", \"F2\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}

// String sql = "SELECT \"F1\"";
// sql += "              , \"F2\"";
// sql += "              , SUM(F3)";
// sql += "        FROM ( SELECT DECODE(M.\"AcctCode\", '990', 1, 0) AS F1";
// sql += "                    , A.\"CityGroup\" AS F2";
// sql += "                    ,  DECODE(M.\"AcctCode\", '990',M.\"LoanBalance\",MBV.\"BookValue\") AS F3";
// sql += "               FROM \"MonthlyLoanBal\" M";
// sql += "               LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = M.\"CustNo\"";
// sql += "                                    AND F.\"FacmNo\" = M.\"FacmNo\"";
// sql += "                                    AND F.\"MainFlag\" = 'Y'";
// sql += "               LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = F.\"ClCode1\"";
// sql += "                                      AND Cl.\"ClCode2\" = F.\"ClCode2\"";
// sql += "                                      AND Cl.\"ClNo\"    = F.\"ClNo\"";
// sql += "               LEFT JOIN \"CdArea\" A ON A.\"CityCode\" = Cl.\"CityCode\"";
// sql += "                                     AND A.\"AreaCode\" = Cl.\"AreaCode\"";
// sql += "               LEFT JOIN \"MonthlyBookValue\" MBV ON MBV.\"YearMonth\" = M.\"YearMonth\"";
// sql += "                                    			 AND MBV.\"CustNo\" = M.\"CustNo\"";
// sql += "                                     			 AND MBV.\"FacmNo\" = M.\"FacmNo\"";
// sql += "                                     			 AND MBV.\"BormNo\" = M.\"BormNo\"";
// sql += "               WHERE M.\"YearMonth\" = :entdy)";
// sql += "        GROUP BY \"F1\", \"F2\" ";
// sql += "        ORDER BY \"F1\", \"F2\"";