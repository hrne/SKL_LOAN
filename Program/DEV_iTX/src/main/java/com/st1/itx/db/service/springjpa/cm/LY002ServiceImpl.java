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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("LY002ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LY002ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo,int endOfYearMonth) throws Exception {
		this.info("lY002.findAll ");

		this.info("YYMM:" + endOfYearMonth);

		String sql = "";
		sql += "	SELECT MLB.\"CustNo\" AS F0";
		sql += "		  ,C.\"CustId\" AS F1";
		sql += "		  ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F2";
		sql += "		  ,(CASE";
		sql += "			  WHEN R.\"RptId\" IS NULL AND MLB.\"EntCode\" = 0 THEN 'D'";
		sql += "			  WHEN R.\"RptId\" IS NULL AND MLB.\"EntCode\" <> 0 THEN 'C'";
		sql += "			  WHEN R.\"RptId\" IS NOT NULL AND MLB.\"EntCode\" = 0 THEN 'B'";
		sql += "			  WHEN R.\"RptId\" IS NOT NULL AND MLB.\"EntCode\" <> 0 THEN 'A'";
		sql += "			ELSE ' ' END ) AS F3";
		sql += "		  ,' ' AS F4";
		sql += "		  ,(CASE";
		sql += "			  WHEN MLB.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			  WHEN MLB.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			  WHEN MLB.\"ClCode1\" = 5 THEN 'A'";
		sql += "			  WHEN MLB.\"ClCode1\" = 9  THEN 'B'";
		sql += "			ELSE 'Z' END ) AS F5";
		sql += "		  ,MLB.\"AcctCode\" AS F6";
		sql += "		  ,L.\"DrawdownDate\" AS F7";
		sql += "		  ,L.\"MaturityDate\" AS F8";
		sql += "		  ,L.\"StoreRate\" / 100 AS F9";
		sql += "		  ,DECODE(L.\"FreqBase\",2,'E','G') AS F10";
		sql += "		  ,L.\"PrevPayIntDate\" AS F11";
		sql += "		  ,C.\"CustId\" AS F12";
		sql += "		  ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F13";
		sql += "		  ,'1' AS F14";
		sql += "		  ,NVL(CM.\"EvaAmt\",0) AS F15";
		sql += "		  ,NVL(F.\"LineAmt\",0) AS F16";
		sql += "		  ,'NTD' AS F17";
		sql += "		  ,MLB.\"LoanBalance\" AS F18";
		sql += "		  ,MLB.\"IntAmtAcc\" AS F19";
		sql += "		  ,MFB.\"AssetClass\" AS F20";
		sql += "		  ,MLB.\"FacmNo\" AS F21";
		sql += "		  ,MLB.\"ClNo\" AS F22";
		sql += "		  ,MLB.\"BormNo\" AS F23";
		sql += "	FROM \"MonthlyLoanBal\" MLB";
		sql += "	LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"CustNo\" = MLB.\"CustNo\"";
		sql += "								   AND MFB.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "								   AND MFB.\"YearMonth\" = MLB.\"YearMonth\"";
		sql += "	LEFT JOIN ( SELECT S.\"CustNo\" ";
		sql += "					  ,S.\"FacmNo\" ";
		sql += "					  ,S.\"LineAmt\" ";
		sql += "				FROM ( SELECT \"CustNo\"";
		sql += "					   		 ,SUM(\"LineAmt\") AS \"LineAmt\"";
		sql += "					   FROM \"FacMain\"";
		sql += "					   WHERE \"UtilAmt\" > 0";
		sql += "					   GROUP BY \"CustNo\" )T ";
		sql += "				LEFT JOIN ( SELECT \"CustNo\"";
		sql += "								  ,\"FacmNo\"";
		sql += "					   		 	  ,SUM(\"LineAmt\") AS \"LineAmt\"";
		sql += "					   FROM \"FacMain\"";
		sql += "					   WHERE \"UtilAmt\" > 0";
		sql += "					   GROUP BY \"CustNo\"";
		sql += "					   		   ,\"FacmNo\" ) S ";
		sql += "				ON S.\"CustNo\" = T.\"CustNo\"";
		sql += "				WHERE T.\"LineAmt\" >= 100000000 ) MLB2";
		sql += "	 ON MLB2.\"CustNo\" = MFB.\"CustNo\"";
		sql += "	AND MLB2.\"FacmNo\" = MFB.\"FacmNo\"";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = MLB.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = MLB.\"CustNo\"";
		sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = MLB.\"CustNo\"";
		sql += "							   AND L.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "							   AND L.\"BormNo\" = MLB.\"BormNo\"";
		sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = MLB.\"ClCode1\"";
		sql += "						   AND CM.\"ClCode2\" = MLB.\"ClCode2\"";
		sql += "						   AND CM.\"ClNo\" = MLB.\"ClNo\"";
		sql += "	LEFT JOIN ( SELECT * ";
		sql += "				FROM \"CustMain\" CM";
		sql += " 				LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "             			   FROM \"RptRelationSelf\" ";
		sql += "             			   WHERE \"LAW005\" = '1' ";
		sql += "             			   UNION ";
		sql += "             			   SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "             			   FROM \"RptRelationFamily\" ";
		sql += "             			   WHERE \"LAW005\" = '1' ";
		sql += "             			   UNION ";
		sql += "             			   SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "             			   FROM \"RptRelationCompany\" ";
		sql += "             			   WHERE \"LAW005\" = '1' ";
		sql += "           			    ) S1 ON S1.\"RptId\" = CM.\"CustId\" ";
		sql += "				WHERE NVL(S1.\"RptId\",' ') != ' ' ) R";
		sql += "	ON R.\"RptId\" = C.\"CustId\"";
		sql += "	WHERE MLB.\"YearMonth\" = :yymm";
		sql += "	  AND MLB.\"LoanBalance\" > 0 ";
		sql += "	  AND MLB2.\"CustNo\" IS NOT NULL";
		sql += "	ORDER BY MLB.\"AcctCode\"";
		sql += "			,C.\"CustId\"";
		sql += "			,MLB.\"CustNo\"";
		sql += "			,MLB.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", endOfYearMonth);
		return this.convertToMap(query);
	}

}