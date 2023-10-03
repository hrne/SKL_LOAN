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
public class LM002ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> doQuery(TitaVo titaVo) throws Exception {
		this.info("lM002.doQuery");

		// 資料前兩年一月起至當月
		String startYM = (parse.stringToInteger(titaVo.get("ENTDY").substring(0, 4)) + 1909) + "01";
		String endYM = parse.IntegerToString(parse.stringToInteger(titaVo.get("ENTDY").substring(0, 6)) + 191100, 1);

		this.info("startYM = " + startYM);
		this.info("endYM = " + endYM);
		String sql = "";

		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'1' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER(JSON_VALUE(\"JsonFields\",'$.\"921LoanBal\"')) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '02'";
		sql += "   AND TRUNC(\"EffectDate\" / 100 ) BETWEEN :startYM AND :endYM";

		sql += " UNION ALL";

		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'2' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER( ";
		sql += "           		JSON_VALUE(\"JsonFields\",'$.\"IALoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IBLoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"ICLoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IDLoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IELoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IFLoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IGLoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IHLoanBal\"') ";
		sql += "           	  + JSON_VALUE(\"JsonFields\",'$.\"IILoanBal\"') ) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '02'";
		sql += "   AND TRUNC(\"EffectDate\" / 100 ) BETWEEN :startYM AND :endYM";

		sql += " UNION ALL";

		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'3' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER(JSON_VALUE(\"JsonFields\",'$.\"340LoanBal\"')) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '02'";
		sql += "   AND TRUNC(\"EffectDate\" / 100 ) BETWEEN :startYM AND :endYM";

		sql += " UNION ALL";

		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'4' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER(JSON_VALUE(\"JsonFields\",'$.\"990LoanBal\"')) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '02'";
		sql += "   AND TRUNC(\"EffectDate\" / 100 ) BETWEEN :startYM AND :endYM";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startYM", startYM);
		query.setParameter("endYM", endYM);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> tmpProjectLoan(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("lM002.tmpProjectLoan");

		String sql = "";
		sql += " WITH \"tmpA\" AS (";
		sql += " SELECT R.\"Type\"";
		sql += "	   ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += "	   ,SUM(\"LoanBal2\") AS \"LoanBal2\"";
		sql += " FROM (";
		sql += " SELECT CASE ";
		sql += "       	  WHEN M.\"AcctCode\" = '990' AND (FP.\"GovOfferFlag\" <> 'N' OR M.\"FacAcctCode\" = '340' )";
		sql += "		  THEN '990' ";
		sql += "       	  WHEN FP.\"GovOfferFlag\" <> 'N' ";
		sql += "		  THEN M.\"ProdNo\" ";
		sql += "       	  WHEN M.\"AcctCode\" = '340' ";
		sql += "		  THEN '340' ";
		sql += "       	  WHEN M.\"ProdNo\" IN ('88')";
		sql += "		  THEN '88' ";
		sql += "       	  WHEN M.\"ProdNo\" BETWEEN '81' AND '83'";
		sql += "		  THEN '921' ";
		sql += "		ELSE '0' END AS \"Type\"";
		// 990--轉催收金額
		sql += "       ,  DECODE(M.\"AcctCode\",'990',M.\"OvduPrinAmt\",M.\"LoanBalance\") AS \"LoanBal\" ";
		sql += "       ,  M.\"LoanBalance\" AS \"LoanBal2\" ";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"FacMain\" FA  ON FA.\"CustNo\" = M.\"CustNo\"";
		sql += "                          AND FA.\"FacmNo\" = M.\"FacmNo\"";
		sql += " LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = M.\"ProdNo\"";
		sql += " WHERE M.\"YearMonth\" = :yearMonth ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += "   AND M.\"ProdNo\" BETWEEN 'IA' AND 'II' ";
		// sql += " AND ( ( (TRUNC(:yearMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" /
		// 10000) ) * 12 ";
		// sql += " + MOD(:yearMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" /
		// 100),100) ";
		// sql += " ) < 240 ) ";//-- 排除本月超過屆滿20年
		sql += " ) R";
		sql += " WHERE R.\"Type\" <> '0'";
		sql += " GROUP BY R.\"Type\"";
		sql += " ), \"tmpB\" AS (";
		sql += " SELECT R.\"Type\"";
		sql += "	   ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += "	   ,SUM(\"LoanBal2\") AS \"LoanBal2\"";
		sql += " FROM (";
		sql += " SELECT CASE ";
		sql += "       	  WHEN M.\"AcctCode\" = '990' AND (FP.\"GovOfferFlag\" <> 'N' OR M.\"FacAcctCode\" = '340' )";
		sql += "		  THEN '990' ";
		sql += "       	  WHEN FP.\"GovOfferFlag\" <> 'N' ";
		sql += "		  THEN M.\"ProdNo\" ";
		sql += "       	  WHEN M.\"AcctCode\" = '340' ";
		sql += "		  THEN '340' ";
		sql += "       	  WHEN M.\"ProdNo\" IN ('88')";
		sql += "		  THEN '88' ";
		sql += "       	  WHEN M.\"ProdNo\" BETWEEN '81' AND '83'";
		sql += "		  THEN '921' ";
		sql += "		ELSE '0' END AS \"Type\"";
		sql += "       ,  DECODE(M.\"AcctCode\",'990',M.\"OvduPrinAmt\",M.\"LoanBalance\") AS \"LoanBal\" ";
		sql += "       ,  M.\"LoanBalance\" AS \"LoanBal2\" ";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"FacMain\" FA  ON FA.\"CustNo\" = M.\"CustNo\"";
		sql += "                          AND FA.\"FacmNo\" = M.\"FacmNo\"";
		sql += " LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = M.\"ProdNo\"";
		sql += " WHERE M.\"YearMonth\" = :yearMonth ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += "   AND NOT M.\"ProdNo\" BETWEEN 'IA' AND 'II' ";
		sql += " ) R";
		sql += " WHERE R.\"Type\" <> '0'";
		sql += " GROUP BY R.\"Type\"";
		sql += " )";
		sql += " SELECT \"Type\" ";
		sql += "       ,\"LoanBal\"";
		sql += " FROM \"tmpA\"";
		sql += " UNION ";
		sql += " SELECT \"Type\" ";
		sql += "       ,\"LoanBal\"";
		sql += " FROM \"tmpB\"";
		sql += " UNION ";
		sql += " SELECT '' AS \"Type\"";
		sql += "       ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += " FROM (";
		sql += " 	SELECT * FROM \"tmpA\"";
		sql += " 	UNION ";
		sql += " 	SELECT * FROM \"tmpB\"";
		sql += " ) ";
		sql += " WHERE \"Type\" <> '88' ";
		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

}