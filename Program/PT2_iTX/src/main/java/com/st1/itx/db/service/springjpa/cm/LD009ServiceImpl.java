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
		sql += "		  ,NVL(A.\"yCount\",0) AS \"yCount\"";
		sql += "		  ,NVL(A.\"newCnt\",0) AS \"newCnt\"";
		sql += "		  ,NVL(A.\"endCnt\",0) AS \"endCnt\"";
		sql += "		  ,NVL(A.\"inExtCnt\",0) AS \"inExtCnt\"";
		sql += "		  ,NVL(A.\"outExtCnt\",0) AS \"outExtCnt\"";
		sql += "		  ,NVL(A.\"netWorth\",0) AS \"netWorth\"";
		sql += "		  ,NVL(A.\"tCount\",0) AS \"tCount\"";
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
		sql += "			  ,SUM(R.\"Count\" + R.\"newCnt\" - R.\"endCnt\" + R.\"inExtCnt\" - R.\"outExtCnt\") AS \"yCount\"";
		sql += "			  ,SUM(R.\"newCnt\") AS \"newCnt\"";
		sql += "			  ,SUM(R.\"endCnt\") AS \"endCnt\"";
		sql += "			  ,SUM(R.\"inExtCnt\") AS \"inExtCnt\"";
		sql += "			  ,SUM(R.\"outExtCnt\") AS \"outExtCnt\"";
		sql += "			  ,SUM(R.\"newCnt\" - R.\"endCnt\" + R.\"inExtCnt\" - R.\"outExtCnt\") AS \"netWorth\"";
		sql += "			  ,SUM(R.\"Count\") AS \"tCount\"";
		sql += "			  ,SUM(R.\"extAmount\") AS \"extAmount\"";
		sql += "		FROM (";
		sql += "			SELECT A.\"TitaTxtNo\"";
		sql += "				  ,L.\"CustNo\"";
		sql += "				  ,1 AS \"Count\"";
		sql += "				  ,L.\"FacmNo\"";
		sql += "				  ,L.\"BormNo\"";
		sql += "				  ,L.\"BorxNo\"";
		sql += "				  ,L.\"AcctCode\"";
		sql += "				  ,A.\"AcSubBookCode\"";
		sql += "				  ,CASE ";
		sql += "					 WHEN M.\"RenewFlag\" = 0 AND L.\"TitaHCode\" = 0";
		sql += "					 THEN 1 ELSE 0";
		sql += "				   END AS \"newCnt\"";// --新撥款
		sql += "				  ,CASE ";
		sql += "					 WHEN L.\"TitaTxCd\" IN ('L3420','L3410') ";
		sql += "					  AND JSON_VALUE(L.\"OtherFields\",'$.CaseCloseCode') = '0' ";
		sql += "					  AND  L.\"TitaHCode\" = 0 ";
		sql += "					 THEN 1 ELSE 0";
		sql += "				   END AS \"endCnt\""; // --結案
		sql += "				  ,CASE ";
		sql += "					 WHEN M.\"RenewFlag\" = 1 AND L.\"TitaHCode\" = 0";
		sql += "					 THEN 1 ELSE 0";
		sql += "				   END AS \"inExtCnt\""; //-- 展入
		sql += "				  ,CASE ";
		sql += "					 WHEN L.\"TitaTxCd\" IN ('L3420','L3410') ";
		sql += "					  AND JSON_VALUE(L.\"OtherFields\",'$.CaseCloseCode') = '1' ";
		sql += "					  AND  L.\"TitaHCode\" = 0 ";
		sql += "					 THEN 1 ELSE 0";
		sql += "				   END AS \"outExtCnt\"";// --展出
		sql += "				  ,CASE ";
		sql += "					 WHEN M.\"RenewFlag\" = 1 AND L.\"TitaHCode\" = 0";
		sql += "					 THEN M.\"DrawdownAmt\" ELSE 0";
		sql += "				   END AS \"extAmount\""; // --展期金額
		sql += "				  ,A.\"EntAc\"";
		sql += "				  ,L.\"LoanBal\"";
		sql += "			FROM \"LoanBorTx\" L";
		sql += "			LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = L.\"CustNo\"";
		sql += "									   AND M.\"FacmNo\" = L.\"FacmNo\"";
		sql += "									   AND M.\"BormNo\" = L.\"BormNo\"";
		sql += "			LEFT JOIN (";
		sql += "				SELECT DISTINCT \"CustNo\"";
		sql += "					  ,\"TitaTlrNo\"";
		sql += "					  ,\"TitaTxtNo\"";
		sql += "					  ,\"AcSubBookCode\"";
		sql += "					  ,\"AcctCode\"";
		sql += "					  ,\"EntAc\"";
		sql += "				FROM \"AcDetail\"";
		sql += "				WHERE \"AcDate\" = :today ";
		sql += "				  AND \"EntAc\" > 0 ";
		sql += "			) A ON A.\"TitaTlrNo\" = L.\"TitaTlrNo\"";
		sql += "			   AND A.\"TitaTxtNo\" = LPAD(L.\"TitaTxtNo\",8,0)";
		sql += "			   AND A.\"AcctCode\" = L.\"AcctCode\"";
		sql += "			WHERE L.\"AcDate\" = :today ";
		sql += "			  AND L.\"AcctCode\" IN ('310','320','330','340')";
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
		sql += "	ORDER BY R.\"AcctCode\" ASC";
		sql += "			,R.\"AcSubBookCode\" ASC";
	
//		sql += " SELECT am.\"AcctCode\" AS F0 ";
//		sql += "       ,CdC.\"Item\" AS F1 ";
//		sql += "       ,am.\"AcSubBookCode\" AS F2 ";
//		sql += "       ,CdCb.\"Item\" AS F3 ";
//		sql += "       ,  aac.\"TdCnt\" ";
//		sql += "        - aac.\"TdNewCnt\" ";
//		sql += "        + aac.\"TdClsCnt\" ";
//		sql += "        - aac.\"TdExtCnt\" AS F4 "; // 展出展入未完全
//		sql += "       ,aac.\"TdNewCnt\" AS F5 ";
//		sql += "       ,aac.\"TdClsCnt\" AS F6 ";
//		sql += "       ,aac.\"TdExtCnt\" AS F7 "; // 展入件數
//		sql += "       ,0 AS F8 "; // 展出件數; 展出的定義是什麼? LA$LDGP有兩個展期件數, LDGETC and LDGEIC, 哪一個是展出?
//		sql += "       ,aac.\"TdNewCnt\" + aac.\"TdExtCnt\" - aac.\"TdClsCnt\" AS F9 "; // 展出展入未完全
//		sql += "       ,aac.\"TdCnt\" AS F10 ";
//		sql += "       ,am.\"YdBal\" AS F11 ";
//		sql += "       ,am.\"DbAmt\" AS F12 ";
//		sql += "       ,am.\"CrAmt\" AS F13 ";
//		sql += "       ,am.\"DbAmt\" - am.\"CrAmt\" AS F14 ";
//		sql += "       ,am.\"TdBal\" AS F15 ";
//		sql += "       ,aac.\"TdExtAmt\" AS F16 ";
//		sql += " FROM \"AcMain\" am ";
//		sql += " LEFT JOIN \"AcAcctCheck\" aac ON aac.\"AcDate\" = am.\"AcDate\" ";
//		sql += "                            AND NVL(aac.\"AcctCode\", ' ') = am.\"AcctCode\" ";
//		sql += "                            AND NVL(aac.\"AcSubBookCode\", ' ') = am.\"AcSubBookCode\" ";
//		sql += "                            AND aac.\"BranchNo\" = am.\"BranchNo\" ";
//		sql += "                            AND aac.\"CurrencyCode\" = am.\"CurrencyCode\" ";
//		sql += " LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcctCode' ";
//		sql += "                       AND CdC.\"Code\" = am.\"AcctCode\" ";
//		sql += " LEFT JOIN \"CdCode\" CdCb ON CdCb.\"DefCode\" = 'AcSubBookCode' ";
//		sql += "                        AND CdCb.\"Code\" = am.\"AcSubBookCode\" ";
//		sql += " WHERE am.\"AcctCode\" BETWEEN '310' AND '390' ";
//		sql += "   AND am.\"AcDate\" = :today ";
//		sql += " ORDER BY am.\"AcctCode\" ";
//		sql += "         ,am.\"AcSubBookCode\" ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("today", titaVo.getEntDyI() + 19110000);

		return this.convertToMap(query);
	}

}