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
public class LM009ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM009.findAll ");

		String iYRMO = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);

//		String sql = "SELECT F1";
//		sql += "             , F2";
//		sql += "             , F4";
//		sql += "             , SUM(F5)";
//		sql += "             , SUM(F6)";
//		sql += "        FROM ( SELECT DECODE( A.\"RvNo\",'IC1A0', '310', 'IC2A0', '320', 'IC3A0', '330', 'IC4A0', '340') AS F1";
//		sql += "                     , E.\"AcctItem\" AS F2";
//		sql += "                     , NVL(D.\"Item\",'一般帳戶') AS F4";
//		sql += "                     , 1 AS F5";
//		sql += "                     , A.\"TxAmt\" F6";
//		sql += "                      FROM \"AcDetail\" A";
//		sql += "               LEFT JOIN \"CdAcBook\" B ON B.\"AcBookCode\" =  A.\"AcBookCode\"";
//		sql += "               LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcBookCode'AND D.\"Code\" = B.\"AcBookCode\" ";
//		sql += "               LEFT JOIN \"CdAcCode\" E ON E.\"AcctCode\" = DECODE(A.\"RvNo\",'IC1A0', '310', 'IC2A0','320'";
//		sql += "                                                                             , 'IC3A0', '330', 'IC4A0', '340')";
//		sql += "               WHERE TRUNC(A.\"AcDate\"/100) = :iyemo";
//		sql += "                        AND A.\"AcctCode\" = 'ICR'";
//		sql += "                        AND A.\"DbCr\" = 'D' ";
//		sql += "                        AND A.\"RvNo\" IN ('IC1A0', 'IC2A0', 'IC3A0', 'IC4A0')";
//		sql += "               UNION ALL";
//		sql += "               SELECT A.\"AcctCode\" AS F1";
//		sql += "                    , A.\"AcctItem\" AS F2";
//		sql += "                    , DECODE(D.\"Item\", '利變年金', D.\"Item\", '一般帳戶') AS F4";
//		sql += "                    , 0 AS F5";
//		sql += "                    , 0 AS F6";
//		sql += "               FROM \"CdAcCode\" A ";
//		sql += "               CROSS JOIN \"CdAcBook\" B";
//		sql += "               LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcBookCode'";
//		sql += "                                     AND D.\"Code\" = B.\"AcBookCode\"";
//		sql += "               WHERE A.\"AcctCode\" > '300' AND A.\"AcctCode\" < '390' ";
//		sql += "               UNION ALL";
//		sql += "               SELECT A.\"AcctCode\" AS F1";
//		sql += "                    , A.\"AcctItem\" AS F2";
//		sql += "                    , CAST('一般帳戶' AS NVARCHAR2(50)) AS F4";
//		sql += "                    , 0 AS F5";
//		sql += "                    , 0 AS F6";
//		sql += "               FROM \"CdAcCode\" A ";
//		sql += "               WHERE A.\"AcctCode\" > '300' AND A.\"AcctCode\" < '390')";
//		sql += "        GROUP BY \"F1\", \"F2\", \"F4\"";
//		sql += "        ORDER BY \"F1\"";

		String sql = "SELECT C.\"AcctItem\"";
		sql += "            ,CdC.\"Item\" AS \"AcSubBookItem\"";
		sql += "            ,SUM(S1.\"Count\") AS \"Count\"";
		sql += "            ,SUM(\"Interest\") AS \"Interest\"";
		sql += "            ,S1.\"AcctCode\" F4";
		sql += "            ,S1.\"AcSubBookCode\" F5";
		sql += "      FROM (SELECT A.\"AcctCode\" AS \"AcctCode\"";
		sql += "                  ,A.\"AcSubBookCode\" AS \"AcSubBookCode\"";
		sql += "                  ,1 AS \"Count\"";
		sql += "                  ,A.\"CustNo\"";
		sql += "                  ,SUM(A.\"Interest\") AS \"Interest\"";
		sql += "            FROM \"AcLoanInt\" A";
		sql += "            WHERE A.\"YearMonth\" = :iyemo";
		sql += "              AND A.\"AcctCode\" IN ('IC1', 'IC2', 'IC3', 'IC4')";
		sql += "              AND A.\"Interest\" > 0 ";
		sql += "            GROUP BY A.\"AcctCode\", A.\"AcSubBookCode\", A.\"CustNo\") S1";
		sql += "      LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = DECODE(S1.\"AcctCode\", 'IC1', '310', 'IC2', '320', 'IC3', '330', 'IC4', '340')";
		sql += "      LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcSubBookCode' AND CdC.\"Code\" = S1.\"AcSubBookCode\"";
		sql += "      GROUP BY C.\"AcctItem\"";
		sql += "              ,S1.\"AcctCode\"";
		sql += "              ,CdC.\"Item\"";
		sql += "              ,S1.\"AcSubBookCode\"";
		sql += "      ORDER BY S1.\"AcctCode\"";
		sql += "              ,S1.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyemo", iYRMO);
		return this.convertToMap(query);
	}

}