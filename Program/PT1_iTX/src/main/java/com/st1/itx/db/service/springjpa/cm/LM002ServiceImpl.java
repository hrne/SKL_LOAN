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
//		// 為了排除掉不是出表範圍的資料，實際Query包進subquery，以便篩選掉DataType=0者
//		sql += " SELECT \"Year\" ";
//		sql += "       ,\"DataType\" ";
//		sql += "       ,\"Month\" ";
//		sql += "       ,SUM(\"LoanBalance\") \"LoanSum\" ";
//
//		sql += " FROM ( SELECT TRUNC(MLB.\"YearMonth\" / 100) AS \"Year\" ";
//		sql += "              ,CASE WHEN MLB.\"AcctCode\" != '990' ";
//		sql += "                    THEN CASE WHEN NVL(FP.\"GovOfferFlag\", 'N') <> 'N' ";
//		sql += "                              THEN 2 "; // 政府優惠: 非990; 政府優惠記號為Y
//		sql += "                              WHEN MLB.\"AcctCode\" = '340' ";
//		sql += "                              THEN 3 "; // 首購: 非990; 交易代號為340
//		sql += "                              WHEN MLB.\"ProdNo\" IN ('81','82','83') ";
//		sql += "                              THEN 1 "; // 921: 非990; 商品代號為81, 82, 83
//		sql += "                         ELSE 0 END "; // 非資料範圍者標記為0，在外層去除掉
//		sql += "                    WHEN NVL(FP.\"GovOfferFlag\", 'N') <> 'N' ";
//		sql += "                      OR MLB.\"FacAcctCode\" = '340' ";
//		sql += "                    THEN 4 "; // 催收款項: 是990; GovOfferFlag為Y 或 原會計科目為340者
//		sql += "               ELSE 0 END AS \"DataType\" "; // 非資料範圍者標記為0，在外層去除掉
//		// 非催收三種的子條件做成巢狀稍微難讀，但會使速度較快
//
//		sql += "              ,MOD(MLB.\"YearMonth\", 100) AS \"Month\" ";
//		sql += "              ,MLB.\"LoanBalance\" AS \"LoanBalance\" ";
//		sql += "        FROM \"MonthlyLoanBal\" MLB ";
//		sql += "        LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = MLB.\"ProdNo\" ";
//		sql += "        WHERE MLB.\"YearMonth\" BETWEEN :startYM AND :endYM ";
//		sql += "          AND MLB.\"LoanBalance\" > 0 ";
//		sql += "          AND NVL(FP.\"ProdNo\", 'XXX') != 'XXX' ";
//		sql += " ) ";
//		sql += " WHERE \"DataType\" != 0 ";
//		sql += " GROUP BY \"Year\" "; // 為了DRY，把GROUP BY拉出來外層做，否則DataType條件會需要寫兩次
//		sql += "         ,\"DataType\" "; // 對於效能並無顯著影響
//		sql += "         ,\"Month\" ";

		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'1' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER(JSON_VALUE(\"JsonFields\",'$.\"921LoanBal\"')) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '01'";
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
		sql += "   AND \"CdItem\" = '01'";
		sql += "   AND TRUNC(\"EffectDate\" / 100 ) BETWEEN :startYM AND :endYM";
		
		sql += " UNION ALL";
		
		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'3' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER(JSON_VALUE(\"JsonFields\",'$.\"340LoanBal\"')) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '01'";
		sql += "   AND TRUNC(\"EffectDate\" / 100 ) BETWEEN :startYM AND :endYM";
		
		sql += " UNION ALL";
		
		sql += " SELECT SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),1,4) AS \"Year\"";
		sql += "       ,'4' AS \"DataType\" ";
		sql += "       , SUBSTR(JSON_VALUE(\"JsonFields\",'$.\"YearMonth\"'),5,6) AS \"Month\"";
		sql += "       , TO_NUMBER(JSON_VALUE(\"JsonFields\",'$.\"990LoanBal\"')) AS \"LoanSum\"";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" = '02' ";
		sql += "   AND \"CdItem\" = '01'";
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
		sql += " FROM (";
		sql += " SELECT CASE ";
		sql += "       	  WHEN M.\"AcctCode\" = '990' AND (FP.\"GovOfferFlag\" <> 'N' OR M.\"FacAcctCode\" = '340' )";
		sql += "		  THEN '990' ";
		sql += "       	  WHEN FP.\"GovOfferFlag\" <> 'N' ";
		sql += "		  THEN M.\"ProdNo\" ";
		sql += "       	  WHEN M.\"AcctCode\" = '340' ";
		sql += "		  THEN '340' ";
		sql += "       	  WHEN M.\"ProdNo\" BETWEEN '81' AND '83'";
		sql += "		  THEN '921' ";
		sql += "		ELSE '0' END AS \"Type\"";
		sql += "       ,\"LoanBalance\" AS \"LoanBal\" ";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"FacMain\" FA  ON FA.\"CustNo\" = M.\"CustNo\"";
		sql += "                          AND FA.\"FacmNo\" = M.\"FacmNo\"";
		sql += " LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = M.\"ProdNo\"";
		sql += " WHERE M.\"YearMonth\" = :yearMonth ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += "   AND M.\"ProdNo\" BETWEEN 'IA' AND 'II' ";
		sql += "   AND (  ( (TRUNC(:yearMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 ";
		sql += "           + MOD(:yearMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "        )  < 240 )    ";//-- 排除本月超過屆滿20年
		sql += " ) R";
		sql += " WHERE R.\"Type\" <> '0'";
		sql += " GROUP BY R.\"Type\"";
		sql += " ), \"tmpB\" AS (";
		sql += " SELECT R.\"Type\"";
		sql += "	   ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += " FROM (";
		sql += " SELECT CASE ";
		sql += "       	  WHEN M.\"AcctCode\" = '990' AND (FP.\"GovOfferFlag\" <> 'N' OR M.\"FacAcctCode\" = '340' )";
		sql += "		  THEN '990' ";
		sql += "       	  WHEN FP.\"GovOfferFlag\" <> 'N' ";
		sql += "		  THEN M.\"ProdNo\" ";
		sql += "       	  WHEN M.\"AcctCode\" = '340' ";
		sql += "		  THEN '340' ";
		sql += "       	  WHEN M.\"ProdNo\" BETWEEN '81' AND '83'";
		sql += "		  THEN '921' ";
		sql += "		ELSE '0' END AS \"Type\"";
		sql += "       ,\"LoanBalance\" AS \"LoanBal\" ";
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
		sql += " SELECT * FROM \"tmpA\"";
		sql += " UNION ";
		sql += " SELECT * FROM \"tmpB\"";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

}