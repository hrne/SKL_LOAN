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
public class LD009ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("ld009.findAll ");

		String sql = " ";
		sql += "	SELECT R.\"AcctItem\"";
		sql += "		  ,R.\"AcSubBookCode\"";
		sql += "		  ,NVL(AAC.\"TdCnt\",0) - NVL(A.\"netWorth\",0) AS \"yCount\"";
		sql += "		  ,NVL(A.\"newCnt\",0) AS \"newCnt\"";
		sql += "		  ,NVL(A.\"endCnt\",0) AS \"endCnt\"";
		sql += "		  ,NVL(A.\"inExtCnt\",0) AS \"inExtCnt\"";
		sql += "		  ,NVL(A.\"outExtCnt\",0) AS \"outExtCnt\"";
		sql += "		  ,NVL(A.\"netWorth\",0) AS \"netWorth\"";
		sql += "		  ,NVL(AAC.\"TdCnt\",0) AS \"tCount\"";
		sql += "		  ,NVL(M.\"YdBal\",0) AS \"YdBal\"";
		sql += "		  ,NVL(AC.\"DbAmt\",0) AS \"DbAmt\"";
		sql += "		  ,NVL(AC.\"CrAmt\",0) AS \"CrAmt\"";
		sql += "		  ,NVL(AC.\"DbAmt\",0) - NVL(AC.\"CrAmt\",0) AS \"netWorthAmt\"";
		sql += "		  ,NVL(M.\"TdBal\",0) AS \"TdBal\"";
		sql += "		  ,NVL(A.\"extAmount\",0) AS \"extAmount\"";
		sql += "	FROM (";
		sql += "		SELECT A.\"AcctCode\"";
		sql += "			  ,A.\"AcctItem\"";
		sql += "			  ,B.\"Code\"";
		sql += "			  ,B.\"Item\" AS \"AcSubBookCode\"";
		sql += "		FROM \"CdAcCode\" A";
		sql += "		CROSS JOIN (";
		sql += "			SELECT \"Code\"";
		sql += "				  ,\"Item\"";
		sql += "			FROM \"CdCode\"";
		sql += "			WHERE \"DefCode\" = 'AcSubBookCode'";
		sql += "			  AND \"Code\" IN ('00A','201')";
		sql += "		) B";
		sql += "		WHERE A.\"AcctCode\" IN ('310','320','330','340')";
		sql += "	) R";
		sql += "	LEFT JOIN (";
		sql += "		SELECT R.\"AcctCode\"";
		sql += "			  ,R.\"AcSubBookCode\"";
		sql += "			  ,SUM(R.\"newCnt\") AS \"newCnt\"";
		sql += "			  ,SUM(R.\"endCnt\") AS \"endCnt\"";
		sql += "			  ,SUM(R.\"inExtCnt\") AS \"inExtCnt\"";
		sql += "			  ,SUM(R.\"outExtCnt\") AS \"outExtCnt\"";
		sql += "			  ,SUM(R.\"newCnt\" - R.\"endCnt\" + R.\"inExtCnt\" - R.\"outExtCnt\") AS \"netWorth\"";
		sql += "			  ,SUM(R.\"extAmount\") AS \"extAmount\"";
		sql += "		FROM (";
		sql += "			SELECT A.\"TitaTxtNo\"";
		sql += "				  ,A.\"CustNo\"";
		sql += "				  ,1 AS \"Count\"";
		sql += "				  ,A.\"FacmNo\"";
		sql += "				  ,A.\"BormNo\"";
		sql += "				  ,A.\"AcctCode\"";
		sql += "				  ,A.\"AcSubBookCode\"";
		sql += "				  ,CASE ";
		sql += "					 WHEN M.\"RenewFlag\" = 0 ";
		sql += "					  AND A.\"TitaTxCd\" = 'L3100' ";
		sql += "					 THEN CASE WHEN A.\"DbCr\" = 'D' THEN 1 ELSE -1 END ";
		sql += "					 ELSE 0";
		sql += "				   END AS \"newCnt\"";// --新撥款
		sql += "				  ,CASE ";
		sql += "					 WHEN A.\"TitaTxCd\" IN ('L3420','L3410') ";
		sql += "					  AND JSON_VALUE(A.\"JsonFields\",'$.CaseCloseCode') IN ('0') ";
		sql += "					 THEN CASE WHEN A.\"DbCr\" = 'C' THEN 1 ELSE -1 END ";
		sql += "					 ELSE 0";
		sql += "				   END AS \"endCnt\""; // --結案
		sql += "				  ,CASE ";
		sql += "					 WHEN M.\"RenewFlag\" = 1 ";
		sql += "					  AND A.\"TitaTxCd\" = 'L3100' ";
		sql += "					 THEN CASE WHEN A.\"DbCr\" = 'D' THEN 1 ELSE -1 END ";
		sql += "					 ELSE 0";
		sql += "				   END AS \"inExtCnt\""; //-- 展入
		sql += "				  ,CASE ";
		sql += "					 WHEN A.\"TitaTxCd\" IN ('L3420','L3410') ";
		sql += "					  AND JSON_VALUE(A.\"JsonFields\",'$.CaseCloseCode') IN ('1') ";
		sql += "					 THEN CASE WHEN A.\"DbCr\" = 'C' THEN 1 ELSE -1 END ";
		sql += "					 ELSE 0";
		sql += "				   END AS \"outExtCnt\"";// --展出
		sql += "				  ,CASE ";
		sql += "					 WHEN M.\"RenewFlag\" = 1 ";
		sql += "					  AND A.\"TitaTxCd\" = 'L3100' ";
		sql += "					 THEN CASE WHEN A.\"DbCr\" = 'D' THEN A.\"TxAmt\" ELSE -A.\"TxAmt\" END ";
		sql += "					 ELSE 0";
		sql += "				   END AS \"extAmount\""; // --展期金額
		sql += "				  ,A.\"EntAc\"";
		sql += "			FROM \"AcDetail\" A";
		sql += "			LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = A.\"CustNo\"";
		sql += "									   AND M.\"FacmNo\" = A.\"FacmNo\"";
		sql += "									   AND M.\"BormNo\" = A.\"BormNo\"";
		sql += "			WHERE A.\"AcDate\" = :today ";
		sql += "			  AND A.\"AcctCode\" IN ('310','320','330','340')";
		sql += "			  AND A.\"EntAc\" > 0 ";
		sql += "		) R";
		sql += "		GROUP BY R.\"AcctCode\"";
		sql += "				,R.\"AcSubBookCode\"";
		sql += "	) A ON A.\"AcctCode\" = R.\"AcctCode\"";
		sql += "	   AND A.\"AcSubBookCode\" = R.\"Code\"";
		sql += "	LEFT JOIN (";
		sql += "		SELECT AC.\"AcctCode\"";
		sql += "			  ,AC.\"AcSubBookCode\"";
		sql += "			  ,SUM(";
		sql += "				CASE";
		sql += "				  WHEN AC.\"DbCr\" = 'D' ";
		sql += "				  THEN AC.\"TxAmt\"";
		sql += "				ELSE 0 END ";
		sql += "			   ) AS \"DbAmt\"";
		sql += "			  ,SUM(";
		sql += "				CASE";
		sql += "				  WHEN AC.\"DbCr\" = 'C' ";
		sql += "				  THEN AC.\"TxAmt\"";
		sql += "				ELSE 0 END ";
		sql += "			   ) AS \"CrAmt\"";
		sql += "		FROM \"AcDetail\" AC";
		sql += "		WHERE AC.\"AcDate\" = :today ";
		sql += "		  AND AC.\"EntAc\" > 0";
		sql += "		  AND AC.\"AcctCode\" IN ('310','320','330','340')";
		sql += "		GROUP BY AC.\"AcctCode\"";
		sql += "				,AC.\"AcSubBookCode\"";
		sql += "	) AC ON AC.\"AcctCode\" = R.\"AcctCode\"";
		sql += "		AND AC.\"AcSubBookCode\" = R.\"Code\"";
		sql += "	LEFT JOIN \"AcMain\" M ON M.\"AcctCode\" = R.\"AcctCode\"";
		sql += "						  AND M.\"AcSubBookCode\" = R.\"Code\"";
		sql += "						  AND M.\"AcDate\" = :today";
		sql += "	LEFT JOIN \"AcAcctCheck\" AAC ON AAC.\"AcctCode\" = R.\"AcctCode\"";
		sql += "						  		 AND AAC.\"AcSubBookCode\" = R.\"Code\"";
		sql += "						  		 AND AAC.\"AcDate\" = :today";
		sql += "	ORDER BY R.\"AcctCode\" ASC";
		sql += "			,R.\"AcSubBookCode\" ASC";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("today", titaVo.getEntDyI() + 19110000);

		return this.convertToMap(query);
	}

}