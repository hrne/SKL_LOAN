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
public class LM014ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	// 有四種表:
	// A 放款科目別 (會計科目) - AcctCode, AcSubBookCode
	// B 放款種類別 (擔保品/商品代碼, kinda) - ClCode1/ProdNo, AcSubBookCode
	// C 關係人別 - IsRels, EntCode
	// D 放款種類別 - ClCode1/ProdNo, IsRels, EntCode

	// 每次產表有兩種條件:
	// EntCodeCondition 房貸/企金
	// DepartmentCodeCondition 非企金通路/企金通路

	public enum EntCodeCondition {
		Enterprise("1"), Natural("0"), All("%");

		private String value;

		EntCodeCondition(String value) {
			this.value = value;
		}
	}

	public enum DepartmentCodeCondition {
		Yes("1"), No("0"), All("%");

		private String value;

		DepartmentCodeCondition(String value) {
			this.value = value;
		}
	}

	public enum QueryType {
		A("放款科目別"), B("放款種類別"), C("關係人別"), D("放款種類別");

		public String value = "";

		QueryType(String value) {
			this.value = value;
		}
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, EntCodeCondition entCodeCondition, DepartmentCodeCondition departmentCodeCondition, QueryType queryType) throws Exception {

		this.info("lM014.findAll ");
		this.info("entCond: " + entCodeCondition.value);
		this.info("dptCond: " + departmentCodeCondition.value);
		this.info("queryType: " + queryType.value);

		String sql = GetSQL(queryType);

		if (sql.length() > 0) {
			this.info("sql=" + sql);

			Query query;
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

			query = em.createNativeQuery(sql);

			// DepartmentCodeCondition 企金通路條件 0,1,%
			// inputYear 當日西元年 4
			// EntCodeCondition 企金別條件 0,1,%
			// inputYearMonth 當日西元年月 6

			query.setParameter("DepartmentCodeCondition", departmentCodeCondition.value);
			query.setParameter("EntCodeCondition", entCodeCondition.value);
			query.setParameter("inputYear", (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000) / 10000);
			query.setParameter("inputYearMonth", (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000) / 100);

			return this.convertToMap(query);
		} else {
			return null;
		}
	}

	private String GetSQL(QueryType qt) {
		StringBuilder result = new StringBuilder();
		switch (qt) {
		case A:
			result.append(" WITH TotalData AS ( ");
			result.append("     SELECT mlb.\"YearMonth\" \"YearMonth\" ");
			result.append("           ,mlb.\"AcctCode\" \"AcctCode\" ");
			result.append("           ,CdAC.\"AcctItem\" \"AcctItem\" ");
			result.append("           ,NVL(mlb.\"AcSubBookCode\", ' ') \"AcSubBookCode\" ");
			result.append("           ,CdC.\"Item\" \"AcSubBookItem\" ");
			result.append("           ,mlb.\"LoanBalance\" \"LoanBal\" ");
			result.append("           ,ROUND(mlb.\"LoanBalance\" * mlb.\"StoreRate\" / 1200, 0) \"IntRcv\" ");
			result.append("           ,mlb.\"LoanBalance\" * mlb.\"StoreRate\" \"LoanBalWeighted\" ");
			result.append("           ,1 \"Count\" ");
			result.append("     FROM \"MonthlyLoanBal\" mlb ");
			result.append("     LEFT JOIN \"CdAcCode\" CdAC ON CdAC.\"AcctCode\" = mlb.\"AcctCode\" ");
			result.append("     LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcSubBookCode' ");
			result.append("                           AND CdC.\"Code\" = mlb.\"AcSubBookCode\" ");
			result.append("     WHERE mlb.\"YearMonth\" BETWEEN :inputYear * 100 + 1 AND :inputYearMonth ");
			result.append("       AND DECODE(mlb.\"EntCode\", 1, 1, 0) LIKE :EntCodeCondition ");
			result.append("       AND NVL(mlb.\"DepartmentCode\", 0) LIKE :DepartmentCodeCondition ");
			result.append("       AND mlb.\"LoanBalance\" > 0 ");
			result.append("       AND mlb.\"AcctCode\" BETWEEN 300 AND 390 ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataGrouped AS ( ");
			result.append("     SELECT MAX(\"YearMonth\") \"YearMonth\" ");
			result.append("           ,\"AcctCode\" \"AcctCode\" ");
			result.append("           ,\"AcctItem\" \"AcctItem\" ");
			result.append("           ,\"AcSubBookCode\" \"AcSubBookCode\" ");
			result.append("           ,\"AcSubBookItem\" \"AcSubBookItem\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("     GROUP BY \"AcctCode\" ");
			result.append("             ,\"AcctItem\" ");
			result.append("             ,\"AcSubBookCode\" ");
			result.append("             ,\"AcSubBookItem\" ");
			result.append("  ) ");
			result.append(" ,ThisYearDataGrouped AS ( ");
			result.append("     SELECT \"AcctCode\" \"AcctCode\" ");
			result.append("           ,\"AcctItem\" \"AcctItem\" ");
			result.append("           ,\"AcSubBookCode\" \"AcSubBookCode\" ");
			result.append("           ,\"AcSubBookItem\" \"AcSubBookItem\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumYearly\" ");
			result.append("     FROM TotalData ");
			result.append("     GROUP BY \"AcctCode\" ");
			result.append("             ,\"AcctItem\" ");
			result.append("             ,\"AcSubBookCode\" ");
			result.append("             ,\"AcSubBookItem\" ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("  ) ");
			result.append(" ,ThisYearDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalYearly\" ");
			result.append("     FROM TotalData ");
			result.append("  ) ");
			result.append(" ");
			result.append(" ");
			result.append(" SELECT CASE WHEN \"Seq\" = 1 ");
			result.append("             THEN \"DescriptionA\" ");
			result.append(" 	   ELSE u' ' END F0 ");
			result.append(" 	  ,\"DescriptionB\" F1 ");
			result.append(" 	  ,\"MonthlyIntRcv\" F2 ");
			result.append(" 	  ,\"MonthlyIntRcvRatio\" F3 ");
			result.append(" 	  ,\"MonthlyLoanBal\" F4 ");
			result.append(" 	  ,\"MonthlyLoanBalRatio\" F5 ");
			result.append(" 	  ,\"MonthlyAvgStoreRate\" F6 ");
			result.append(" 	  ,\"YearlyIntRcv\" F7 ");
			result.append(" 	  ,\"YearlyIntRcvRatio\" F8 ");
			result.append(" 	  ,\"YearlyAvgLoanBal\" F9 ");
			result.append(" 	  ,\"YearlyAvgLoanBalRatio\" F10 ");
			result.append(" 	  ,\"YearlyAvgStoreRate\" F11 ");
			result.append(" FROM (SELECT monthGroup.\"AcctItem\" \"DescriptionA\" ");
			result.append("             ,monthGroup.\"AcSubBookItem\" \"DescriptionB\" ");
			result.append("             ,monthGroup.\"IntRcvSumMonthly\" \"MonthlyIntRcv\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"IntRcvTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"IntRcvSumMonthly\" / monthWhole.\"IntRcvTotalMonthly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"MonthlyIntRcvRatio\" ");
			result.append("             ,monthGroup.\"LoanBalSumMonthly\" \"MonthlyLoanBal\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"LoanBalSumMonthly\" / monthWhole.\"LoanBalTotalMonthly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"MonthlyLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthGroup.\"LoanBalSumMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"LoanBalWeightedSumMonthly\" / monthGroup.\"LoanBalSumMonthly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') \"MonthlyAvgStoreRate\" ");
			result.append("             ,yearGroup.\"IntRcvSumYearly\" \"YearlyIntRcv\" ");
			result.append("             ,TO_CHAR(CASE WHEN yearWhole.\"IntRcvTotalYearly\" > 0 ");
			result.append("                           THEN ROUND(yearGroup.\"IntRcvSumYearly\" / yearWhole.\"IntRcvTotalYearly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"YearlyIntRcvRatio\" ");
			result.append("             ,ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) \"YearlyAvgLoanBal\" ");
			result.append(
					"             ,TO_CHAR(ROUND(ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) / ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) * 100, 1), 'FM999.0') \"YearlyAvgLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN yearGroup.\"LoanBalSumYearly\" > 0 ");
			result.append("                           THEN ROUND(yearGroup.\"LoanBalWeightedSumYearly\" / yearGroup.\"LoanBalSumYearly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') \"YearlyAvgStoreRate\" ");
			result.append("             ,monthGroup.\"AcctCode\" \"AcctCode\" ");
			result.append("             ,monthGroup.\"AcSubBookCode\" \"AcSubBookCode\" ");
			result.append("             ,ROW_NUMBER() OVER (PARTITION BY monthGroup.\"AcctCode\" ");
			result.append("                                     ORDER BY monthGroup.\"AcSubBookCode\") \"Seq\" ");
			result.append("       FROM ThisMonthDataGrouped monthGroup ");
			result.append("       LEFT JOIN ThisYearDataGrouped yearGroup ON yearGroup.\"AcctCode\" = monthGroup.\"AcctCode\" ");
			result.append("                                              AND NVL(yearGroup.\"AcSubBookCode\", ' ') = NVL(monthGroup.\"AcSubBookCode\", ' ') ");
			result.append("       LEFT JOIN ThisMonthDataWhole monthWhole ON 1 = 1 ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append(" ");
			result.append("       UNION ALL ");
			result.append(" ");
			result.append("       SELECT u'合計' ");
			result.append("             ,u' ' ");
			result.append("             ,monthWhole.\"IntRcvTotalMonthly\" ");
			result.append("             ,'***' ");
			result.append("             ,monthWhole.\"LoanBalTotalMonthly\" ");
			result.append("             ,'***' ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthWhole.\"LoanBalWeightedTotalMonthly\" / monthWhole.\"LoanBalTotalMonthly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,yearWhole.\"IntRcvTotalYearly\" ");
			result.append("             ,'***' ");
			result.append("             ,ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) ");
			result.append("             ,'***' ");
			result.append("             ,TO_CHAR(CASE WHEN yearWhole.\"LoanBalTotalYearly\" > 0 ");
			result.append("                           THEN ROUND(yearWhole.\"LoanBalWeightedTotalYearly\" / yearWhole.\"LoanBalTotalYearly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,'999' ");
			result.append(" 	        ,'ZZZ' ");
			result.append(" 	        ,1 ");
			result.append("       FROM ThisMonthDataWhole monthWhole ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append(" ");
			result.append("       UNION ALL ");
			result.append(" ");
			result.append("       SELECT u' ' ");
			result.append("             ,u'小計' ");
			result.append("             ,SUM(monthGroup.\"IntRcvSumMonthly\") ");
			result.append("             ,TO_CHAR(SUM(CASE WHEN monthWhole.\"IntRcvTotalMonthly\" > 0 ");
			result.append("                               THEN ROUND(monthGroup.\"IntRcvSumMonthly\" / monthWhole.\"IntRcvTotalMonthly\" * 100, 1) ");
			result.append(" 	      			   ELSE 0 END), 'FM990.0') ");
			result.append("             ,SUM(monthGroup.\"LoanBalSumMonthly\") ");
			result.append("             ,TO_CHAR(SUM(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                               THEN ROUND(monthGroup.\"LoanBalSumMonthly\" / monthWhole.\"LoanBalTotalMonthly\" * 100, 1) ");
			result.append("                          ELSE 0 END), 'FM990.0') ");
			result.append("             ,TO_CHAR(CASE WHEN SUM(monthGroup.\"LoanBalSumMonthly\") > 0 ");
			result.append("                           THEN ROUND(SUM(monthGroup.\"LoanBalWeightedSumMonthly\") / SUM(monthGroup.\"LoanBalSumMonthly\"), 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,SUM(yearGroup.\"IntRcvSumYearly\") ");
			result.append("             ,TO_CHAR(SUM(CASE WHEN yearWhole.\"IntRcvTotalYearly\" > 0 ");
			result.append("                               THEN ROUND(yearGroup.\"IntRcvSumYearly\" / yearWhole.\"IntRcvTotalYearly\" * 100, 1) ");
			result.append("                          ELSE 0 END), 'FM990.0') ");
			result.append("             ,ROUND(SUM(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100))) \"YearlyAvgLoanBal\" ");
			result.append(
					"             ,TO_CHAR(SUM(ROUND(ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) / ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) * 100, 1)), 'FM999.0') \"YearlyAvgLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN SUM(yearGroup.\"LoanBalSumYearly\") > 0 ");
			result.append("                           THEN ROUND(SUM(yearGroup.\"LoanBalWeightedSumYearly\") / SUM(yearGroup.\"LoanBalSumYearly\"), 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,monthGroup.\"AcctCode\" ");
			result.append(" 	        ,'ZZZ' ");
			result.append(" 	        ,1 ");
			result.append("       FROM ThisMonthDataGrouped monthGroup ");
			result.append("       LEFT JOIN ThisYearDataGrouped yearGroup ON yearGroup.\"AcctCode\" = monthGroup.\"AcctCode\" ");
			result.append("                                              AND NVL(yearGroup.\"AcSubBookCode\", ' ') = NVL(monthGroup.\"AcSubBookCode\", ' ') ");
			result.append("       LEFT JOIN ThisMonthDataWhole monthWhole ON 1 = 1 ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("       GROUP BY monthGroup.\"AcctCode\" ");
			result.append("      ) ");
			result.append(" ORDER BY \"AcctCode\" ");
			result.append("         ,\"AcSubBookCode\" ");
			break;
		case B:
			result.append(" WITH TotalData AS ( ");
			result.append("     SELECT mlb.\"YearMonth\" \"YearMonth\" ");
			result.append("           ,NVL(mlb.\"AcSubBookCode\", ' ') \"AcSubBookCode\" ");
			result.append("           ,CdC.\"Item\" \"AcSubBookItem\" ");
			result.append("           ,CASE WHEN mlb.\"ProdNo\" IN ('1','1A','38','EM','EN','EO') ");
			result.append("                 THEN '4' ");
			result.append("                 WHEN mlb.\"ClCode1\" IN (1,2) ");
			result.append("                 THEN '3' ");
			result.append("                 WHEN mlb.\"ClCode1\" IN (3,4) ");
			result.append("                 THEN '2' ");
			result.append("                 WHEN mlb.\"ClCode1\" = 5 ");
			result.append("                 THEN '1' ");
			result.append("                 WHEN mlb.\"ClCode1\" = 9 ");
			result.append("                 THEN '5' ");
			result.append("            ELSE '3' END \"TypeCode\" ");
			result.append("           ,DECODE(mlb.\"EntCode\", 1, '1', '0') \"EntType\" ");
			result.append("           ,mlb.\"LoanBalance\" \"LoanBal\" ");
			result.append("           ,ROUND(mlb.\"LoanBalance\" * mlb.\"StoreRate\" / 1200, 0) \"IntRcv\" ");
			result.append("           ,mlb.\"LoanBalance\" * mlb.\"StoreRate\" \"LoanBalWeighted\" ");
			result.append("           ,1 \"Count\" ");
			result.append("     FROM \"MonthlyLoanBal\" mlb ");
			result.append("     LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcSubBookCode' ");
			result.append("                           AND CdC.\"Code\" = mlb.\"AcSubBookCode\" ");
			result.append("     WHERE mlb.\"YearMonth\" BETWEEN :inputYear * 100 + 1 AND :inputYearMonth ");
			result.append("       AND mlb.\"LoanBalance\" > 0 ");
			result.append("       AND mlb.\"AcctCode\" BETWEEN 300 AND 390 ");
			result.append("       AND DECODE(mlb.\"EntCode\", 1, 1, 0) LIKE :EntCodeCondition ");
			result.append("       AND NVL(mlb.\"DepartmentCode\", 0) LIKE :DepartmentCodeCondition ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataGrouped AS ( ");
			result.append("     SELECT MAX(\"YearMonth\") \"YearMonth\" ");
			result.append("           ,\"AcSubBookCode\" \"AcSubBookCode\" ");
			result.append("           ,\"AcSubBookItem\" \"AcSubBookItem\" ");
			result.append("           ,\"TypeCode\" ");
			result.append("           ,\"EntType\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("     GROUP BY \"AcSubBookCode\" ");
			result.append("             ,\"AcSubBookItem\" ");
			result.append("             ,\"TypeCode\" ");
			result.append("             ,\"EntType\" ");
			result.append("  ) ");
			result.append(" ,ThisYearDataGrouped AS ( ");
			result.append("     SELECT \"AcSubBookCode\" \"AcSubBookCode\" ");
			result.append("           ,\"AcSubBookItem\" \"AcSubBookItem\" ");
			result.append("           ,\"TypeCode\" ");
			result.append("           ,\"EntType\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumYearly\" ");
			result.append("     FROM TotalData ");
			result.append("     GROUP BY \"AcSubBookCode\" ");
			result.append("             ,\"AcSubBookItem\" ");
			result.append("             ,\"TypeCode\" ");
			result.append("             ,\"EntType\" ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("  ) ");
			result.append(" ,ThisYearDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalYearly\" ");
			result.append("     FROM TotalData ");
			result.append("  ) ");
			result.append("  ");
			result.append("  ");
			result.append(" SELECT CASE WHEN \"Seq\" = 1 ");
			result.append("             THEN \"DescriptionA\" ");
			result.append(" 	   ELSE u' ' END F0 ");
			result.append(" 	  ,\"DescriptionB\" F1 ");
			result.append(" 	  ,\"MonthlyIntRcv\" F2 ");
			result.append(" 	  ,\"MonthlyIntRcvRatio\" F3 ");
			result.append(" 	  ,\"MonthlyLoanBal\" F4 ");
			result.append(" 	  ,\"MonthlyLoanBalRatio\" F5 ");
			result.append(" 	  ,\"MonthlyAvgStoreRate\" F6 ");
			result.append(" 	  ,\"YearlyIntRcv\" F7 ");
			result.append(" 	  ,\"YearlyIntRcvRatio\" F8 ");
			result.append(" 	  ,\"YearlyAvgLoanBal\" F9 ");
			result.append(" 	  ,\"YearlyAvgLoanBalRatio\" F10 ");
			result.append(" 	  ,\"YearlyAvgStoreRate\" F11 ");
			result.append(" FROM (SELECT DECODE(monthGroup.\"TypeCode\", 1, u'銀行保證' ");
			result.append("                                          , 2, u'有價證券' ");
			result.append("                                          , 3, u'不動產' ");
			result.append("                                          , 4, u'員工貸款' ");
			result.append("                                          , 5, u'動產')  ");
			result.append("              || CASE WHEN monthGroup.\"TypeCode\" = 2 ");
			result.append("                      THEN DECODE(monthGroup.\"EntType\", 0, u'（個人）', u'（公司）') ");
			result.append("                      WHEN monthGroup.\"TypeCode\" = 4 ");
			result.append("                      THEN u' ' ");
			result.append("                 ELSE DECODE(monthGroup.\"EntType\", 0, u'－個人', u'－公司') END \"DescriptionA\" ");
			result.append("             ,CASE WHEN monthGroup.\"TypeCode\" = 2 ");
			result.append("                   THEN u' ' ");
			result.append("              ELSE monthGroup.\"AcSubBookItem\" END \"DescriptionB\" ");
			result.append("             ,monthGroup.\"IntRcvSumMonthly\" \"MonthlyIntRcv\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"IntRcvTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"IntRcvSumMonthly\" / monthWhole.\"IntRcvTotalMonthly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"MonthlyIntRcvRatio\" ");
			result.append("             ,monthGroup.\"LoanBalSumMonthly\" \"MonthlyLoanBal\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"LoanBalSumMonthly\" / monthWhole.\"LoanBalTotalMonthly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"MonthlyLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthGroup.\"LoanBalSumMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"LoanBalWeightedSumMonthly\" / monthGroup.\"LoanBalSumMonthly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') \"MonthlyAvgStoreRate\" ");
			result.append("             ,yearGroup.\"IntRcvSumYearly\" \"YearlyIntRcv\" ");
			result.append("             ,TO_CHAR(CASE WHEN yearWhole.\"IntRcvTotalYearly\" > 0 ");
			result.append("                           THEN ROUND(yearGroup.\"IntRcvSumYearly\" / yearWhole.\"IntRcvTotalYearly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"YearlyIntRcvRatio\" ");
			result.append("             ,ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) \"YearlyAvgLoanBal\" ");
			result.append(
					"             ,TO_CHAR(ROUND(ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) / ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) * 100, 1), 'FM999.0') \"YearlyAvgLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN yearGroup.\"LoanBalSumYearly\" > 0 ");
			result.append("                           THEN ROUND(yearGroup.\"LoanBalWeightedSumYearly\" / yearGroup.\"LoanBalSumYearly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') \"YearlyAvgStoreRate\" ");
			result.append("             ,monthGroup.\"TypeCode\" \"TypeCode\" ");
			result.append("             ,monthGroup.\"EntType\" \"EntType\" ");
			result.append("             ,monthGroup.\"AcSubBookCode\" \"AcSubBookCode\" ");
			result.append("             ,ROW_NUMBER() OVER (PARTITION BY monthGroup.\"TypeCode\" ");
			result.append("                                             ,monthGroup.\"EntType\" ");
			result.append("                                     ORDER BY monthGroup.\"AcSubBookCode\") \"Seq\" ");
			result.append("       FROM ThisMonthDataGrouped monthGroup ");
			result.append("       LEFT JOIN ThisYearDataGrouped yearGroup ON yearGroup.\"TypeCode\" = monthGroup.\"TypeCode\" ");
			result.append("                                              AND yearGroup.\"EntType\" = monthGroup.\"EntType\" ");
			result.append("                                              AND NVL(yearGroup.\"AcSubBookCode\", ' ') = NVL(monthGroup.\"AcSubBookCode\", ' ') ");
			result.append("       LEFT JOIN ThisMonthDataWhole monthWhole ON 1 = 1 ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("  ");
			result.append("       UNION ALL ");
			result.append("  ");
			result.append("       SELECT u'合計' ");
			result.append("             ,u' ' ");
			result.append("             ,monthWhole.\"IntRcvTotalMonthly\" ");
			result.append("             ,'***' ");
			result.append("             ,monthWhole.\"LoanBalTotalMonthly\" ");
			result.append("             ,'***' ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthWhole.\"LoanBalWeightedTotalMonthly\" / monthWhole.\"LoanBalTotalMonthly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,yearWhole.\"IntRcvTotalYearly\" ");
			result.append("             ,'***' ");
			result.append("             ,ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) ");
			result.append("             ,'***' ");
			result.append("             ,TO_CHAR(CASE WHEN yearWhole.\"LoanBalTotalYearly\" > 0 ");
			result.append("                           THEN ROUND(yearWhole.\"LoanBalWeightedTotalYearly\" / yearWhole.\"LoanBalTotalYearly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,'999' ");
			result.append("             ,'9' ");
			result.append(" 	        ,'ZZZ' ");
			result.append(" 	        ,1 ");
			result.append("       FROM ThisMonthDataWhole monthWhole ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("  ");
			result.append("       UNION ALL ");
			result.append("  ");
			result.append("       SELECT u' ' ");
			result.append("             ,u'小計' ");
			result.append("             ,SUM(monthGroup.\"IntRcvSumMonthly\") ");
			result.append("             ,TO_CHAR(SUM(CASE WHEN monthWhole.\"IntRcvTotalMonthly\" > 0 ");
			result.append("                               THEN ROUND(monthGroup.\"IntRcvSumMonthly\" / monthWhole.\"IntRcvTotalMonthly\" * 100, 1) ");
			result.append(" 	      			   ELSE 0 END), 'FM990.0') ");
			result.append("             ,SUM(monthGroup.\"LoanBalSumMonthly\") ");
			result.append("             ,TO_CHAR(SUM(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                               THEN ROUND(monthGroup.\"LoanBalSumMonthly\" / monthWhole.\"LoanBalTotalMonthly\" * 100, 1) ");
			result.append("                          ELSE 0 END), 'FM990.0') ");
			result.append("             ,TO_CHAR(CASE WHEN SUM(monthGroup.\"LoanBalSumMonthly\") > 0 ");
			result.append("                           THEN ROUND(SUM(monthGroup.\"LoanBalWeightedSumMonthly\") / SUM(monthGroup.\"LoanBalSumMonthly\"), 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,SUM(yearGroup.\"IntRcvSumYearly\") ");
			result.append("             ,TO_CHAR(SUM(CASE WHEN yearWhole.\"IntRcvTotalYearly\" > 0 ");
			result.append("                               THEN ROUND(yearGroup.\"IntRcvSumYearly\" / yearWhole.\"IntRcvTotalYearly\" * 100, 1) ");
			result.append("                          ELSE 0 END), 'FM990.0') ");
			result.append("             ,ROUND(SUM(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100))) \"YearlyAvgLoanBal\" ");
			result.append(
					"             ,TO_CHAR(SUM(ROUND(ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) / ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) * 100, 1)), 'FM999.0') \"YearlyAvgLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN SUM(yearGroup.\"LoanBalSumYearly\") > 0 ");
			result.append("                           THEN ROUND(SUM(yearGroup.\"LoanBalWeightedSumYearly\") / SUM(yearGroup.\"LoanBalSumYearly\"), 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,monthGroup.\"TypeCode\" ");
			result.append("             ,monthGroup.\"EntType\" ");
			result.append(" 	        ,'ZZZ' ");
			result.append(" 	        ,1 ");
			result.append("       FROM ThisMonthDataGrouped monthGroup ");
			result.append("       LEFT JOIN ThisYearDataGrouped yearGroup ON yearGroup.\"TypeCode\" = monthGroup.\"TypeCode\" ");
			result.append("                                              AND yearGroup.\"EntType\" = monthGroup.\"EntType\" ");
			result.append("                                              AND NVL(yearGroup.\"AcSubBookCode\", ' ') = NVL(monthGroup.\"AcSubBookCode\", ' ') ");
			result.append("       LEFT JOIN ThisMonthDataWhole monthWhole ON 1 = 1 ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("       WHERE monthGroup.\"TypeCode\" != 2 ");
			result.append("       GROUP BY monthGroup.\"TypeCode\" ");
			result.append("               ,monthGroup.\"EntType\" ");
			result.append("      ) ");
			result.append(" ORDER BY \"TypeCode\" ");
			result.append("         ,\"EntType\" ");
			result.append("         ,\"AcSubBookCode\" ");
			break;
		case C:
			result.append(" WITH TotalData AS ( ");
			result.append("     SELECT mlb.\"YearMonth\" \"YearMonth\" ");
			result.append("           ,DECODE(mlb.\"EntCode\", 1, 1, 0) \"EntCode\" ");
			result.append("           ,rel.\"IsRels\" \"IsRels\" ");
			result.append("           ,mlb.\"LoanBalance\" \"LoanBal\" ");
			result.append("           ,ROUND(mlb.\"LoanBalance\" * mlb.\"StoreRate\" / 1200, 0) \"IntRcv\" ");
			result.append("           ,mlb.\"LoanBalance\" * mlb.\"StoreRate\" \"LoanBalWeighted\" ");
			result.append("           ,1 \"Count\" ");
			result.append("     FROM \"MonthlyLoanBal\" mlb ");
			result.append("     LEFT JOIN ( SELECT cm.\"CustNo\" ");
//			result.append("                       ,DECODE(COUNT(brs.\"CustId\") + COUNT(brf.\"CustId\") + COUNT(brc.\"CustId\"), 0, 'N', 'Y') \"IsRels\" ");
			result.append("                       ,DECODE(COUNT(s.\"StaffId\"), 0, 'N', 'Y') \"IsRels\" ");
			result.append("                 FROM \"CustMain\" cm ");
			result.append("                 LEFT JOIN \"StakeholdersStaff\" s ON s.\"StaffId\" = cm.\"CustId\" ");
//			result.append("                 LEFT JOIN \"BankRelationSelf\" brs ON brs.\"CustId\" = cm.\"CustId\" ");
//			result.append("                 LEFT JOIN \"BankRelationFamily\" brf ON brf.\"RelationId\" = cm.\"CustId\" ");
//			result.append("                 LEFT JOIN \"BankRelationCompany\" brc ON brc.\"CompanyId\" = cm.\"CustId\" ");
			result.append("                 GROUP BY cm.\"CustNo\" ");
			result.append("               ) rel ON rel.\"CustNo\" = mlb.\"CustNo\" ");
			result.append("     WHERE mlb.\"YearMonth\" BETWEEN :inputYear * 100 + 1 AND :inputYearMonth ");
			result.append("       AND DECODE(mlb.\"EntCode\", 1, 1, 0) LIKE :EntCodeCondition ");
			result.append("       AND NVL(mlb.\"DepartmentCode\", 0) LIKE :DepartmentCodeCondition ");
			result.append("       AND mlb.\"LoanBalance\" > 0 ");
			result.append("       AND mlb.\"AcctCode\" BETWEEN 300 AND 390");
			result.append("  ) ");
			result.append(" ,ThisMonthDataGrouped AS ( ");
			result.append("     SELECT MAX(\"YearMonth\") \"YearMonth\" ");
			result.append("           ,\"IsRels\" \"IsRels\" ");
			result.append("           ,\"EntCode\" \"EntCode\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("     GROUP BY \"IsRels\" ");
			result.append("             ,\"EntCode\" ");
			result.append("  ) ");
			result.append(" ,ThisYearDataGrouped AS ( ");
			result.append("     SELECT \"IsRels\" \"IsRels\" ");
			result.append("           ,\"EntCode\" \"EntCode\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumYearly\" ");
			result.append("     FROM TotalData ");
			result.append("     GROUP BY \"IsRels\" ");
			result.append("             ,\"EntCode\" ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("  ) ");
			result.append(" ,ThisYearDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalYearly\" ");
			result.append("     FROM TotalData ");
			result.append("  ) ");
			result.append("  ");
			result.append(" SELECT DECODE(monthGroup.\"IsRels\", 'Y', u'關係人', u'非關係人') \"DescriptionA\" ");
			result.append("       ,DECODE(monthGroup.\"EntCode\", 1, u'（公司）', u'（個人）') \"DescriptionB\" ");
			result.append("       ,monthGroup.\"IntRcvSumMonthly\" \"MonthlyIntRcv\" ");
			result.append("       ,TO_CHAR(CASE WHEN monthWhole.\"IntRcvTotalMonthly\" > 0 ");
			result.append("                     THEN ROUND(monthGroup.\"IntRcvSumMonthly\" / monthWhole.\"IntRcvTotalMonthly\" * 100, 1) ");
			result.append("                ELSE 0 END, 'FM990.0') \"MonthlyIntRcvRatio\" ");
			result.append("       ,monthGroup.\"LoanBalSumMonthly\" \"MonthlyLoanBal\" ");
			result.append("       ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                     THEN ROUND(monthGroup.\"LoanBalSumMonthly\" / monthWhole.\"LoanBalTotalMonthly\" * 100, 1) ");
			result.append("                ELSE 0 END, 'FM990.0') \"MonthlyLoanBalRatio\" ");
			result.append("       ,TO_CHAR(CASE WHEN monthGroup.\"LoanBalSumMonthly\" > 0 ");
			result.append("                     THEN ROUND(monthGroup.\"LoanBalWeightedSumMonthly\" / monthGroup.\"LoanBalSumMonthly\", 3) ");
			result.append(" 			   ELSE 0 END, 'FM990.000') \"MonthlyAvgStoreRate\" ");
			result.append("       ,yearGroup.\"IntRcvSumYearly\" \"YearlyIntRcv\" ");
			result.append("       ,TO_CHAR(CASE WHEN yearWhole.\"IntRcvTotalYearly\" > 0 ");
			result.append("                     THEN ROUND(yearGroup.\"IntRcvSumYearly\" / yearWhole.\"IntRcvTotalYearly\" * 100, 1) ");
			result.append("                ELSE 0 END, 'FM990.0') \"YearlyIntRcvRatio\" ");
			result.append("       ,ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) \"YearlyAvgLoanBal\" ");
			result.append(
					"       ,TO_CHAR(ROUND(ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) / ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) * 100, 1), 'FM999.0') \"YearlyAvgLoanBalRatio\" ");
			result.append("       ,TO_CHAR(CASE WHEN yearGroup.\"LoanBalSumYearly\" > 0 ");
			result.append("                     THEN ROUND(yearGroup.\"LoanBalWeightedSumYearly\" / yearGroup.\"LoanBalSumYearly\", 3) ");
			result.append("                ELSE 0 END, 'FM990.000') \"YearlyAvgStoreRate\" ");
			result.append(" 	  ,monthGroup.\"IsRels\" \"IsRels\" ");
			result.append("       ,monthGroup.\"EntCode\" \"EntCode\" ");
			result.append(" FROM ThisMonthDataGrouped monthGroup ");
			result.append(" LEFT JOIN ThisYearDataGrouped yearGroup ON yearGroup.\"IsRels\" = monthGroup.\"IsRels\" ");
			result.append("                                        AND yearGroup.\"EntCode\" = monthGroup.\"EntCode\" ");
			result.append(" LEFT JOIN ThisMonthDataWhole monthWhole ON 1 = 1 ");
			result.append(" LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("  ");
			result.append(" UNION ALL ");
			result.append("  ");
			result.append(" SELECT u'合計' \"DescriptionA\" ");
			result.append("       ,u' ' \"DescriptionB\" ");
			result.append("       ,monthWhole.\"IntRcvTotalMonthly\" ");
			result.append("       ,'***' ");
			result.append("       ,monthWhole.\"LoanBalTotalMonthly\" ");
			result.append("       ,'***' ");
			result.append("       ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                     THEN ROUND(monthWhole.\"LoanBalWeightedTotalMonthly\" / monthWhole.\"LoanBalTotalMonthly\", 3) ");
			result.append("                ELSE 0 END, 'FM990.000') ");
			result.append("       ,yearWhole.\"IntRcvTotalYearly\" ");
			result.append("       ,'***' ");
			result.append("       ,ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) ");
			result.append("       ,'***' ");
			result.append("       ,TO_CHAR(CASE WHEN yearWhole.\"LoanBalTotalYearly\" > 0 ");
			result.append("                     THEN ROUND(yearWhole.\"LoanBalWeightedTotalYearly\" / yearWhole.\"LoanBalTotalYearly\", 3) ");
			result.append("  				ELSE 0 END, 'FM990.000') \"YearlyAvgStoreRate\" ");
			result.append(" 	  ,'Z' ");
			result.append("       ,9 ");
			result.append(" FROM ThisMonthDataWhole monthWhole ");
			result.append(" LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("  ");
			result.append(" ORDER BY \"IsRels\", \"EntCode\" ");
			break;
		case D:
			result.append(" WITH TotalData AS ( ");
			result.append("     SELECT mlb.\"YearMonth\" \"YearMonth\" ");
			result.append("           ,rel.\"IsRels\" \"IsRels\" ");
			result.append("           ,CASE WHEN mlb.\"ProdNo\" IN ('1','1A','38','EM','EN','EO') ");
			result.append("                 THEN '4' ");
			result.append("                 WHEN mlb.\"ClCode1\" IN (1,2) ");
			result.append("                 THEN '3' ");
			result.append("                 WHEN mlb.\"ClCode1\" IN (3,4) ");
			result.append("                 THEN '2' ");
			result.append("                 WHEN mlb.\"ClCode1\" = 5 ");
			result.append("                 THEN '1' ");
			result.append("                 WHEN mlb.\"ClCode1\" = 9 ");
			result.append("                 THEN '5' ");
			result.append("            ELSE '3' END \"TypeCode\" ");
			result.append("           ,DECODE(mlb.\"EntCode\", 1, '1', '0') \"EntType\" ");
			result.append("           ,mlb.\"LoanBalance\" \"LoanBal\" ");
			result.append("           ,ROUND(mlb.\"LoanBalance\" * mlb.\"StoreRate\" / 1200, 0) \"IntRcv\" ");
			result.append("           ,mlb.\"LoanBalance\" * mlb.\"StoreRate\" \"LoanBalWeighted\" ");
			result.append("           ,1 \"Count\" ");
			result.append("     FROM \"MonthlyLoanBal\" mlb ");
			result.append("     LEFT JOIN ( SELECT cm.\"CustNo\" ");
//			result.append("                       ,DECODE(COUNT(brs.\"CustId\") + COUNT(brf.\"CustId\") + COUNT(brc.\"CustId\"), 0, 'N', 'Y') \"IsRels\" ");
			result.append("                       ,DECODE(COUNT(s.\"StaffId\"), 0, 'N', 'Y') \"IsRels\" ");
			result.append("                 FROM \"CustMain\" cm ");
			result.append("                 LEFT JOIN \"StakeholdersStaff\" s ON s.\"StaffId\" = cm.\"CustId\" ");
//			result.append("                 LEFT JOIN \"BankRelationSelf\" brs ON brs.\"CustId\" = cm.\"CustId\" ");
//			result.append("                 LEFT JOIN \"BankRelationFamily\" brf ON brf.\"RelationId\" = cm.\"CustId\" ");
//			result.append("                 LEFT JOIN \"BankRelationCompany\" brc ON brc.\"CompanyId\" = cm.\"CustId\" ");
			result.append("                 GROUP BY cm.\"CustNo\" ");
			result.append("               ) rel ON rel.\"CustNo\" = mlb.\"CustNo\" ");
			result.append("     WHERE mlb.\"YearMonth\" BETWEEN :inputYear * 100 + 1 AND :inputYearMonth ");
			result.append("       AND mlb.\"LoanBalance\" > 0 ");
			result.append("       AND mlb.\"AcctCode\" BETWEEN 300 AND 390 ");
			result.append("       AND DECODE(mlb.\"EntCode\", 1, 1, 0) LIKE :EntCodeCondition ");
			result.append("       AND NVL(mlb.\"DepartmentCode\", 0) LIKE :DepartmentCodeCondition ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataGrouped AS ( ");
			result.append("     SELECT MAX(\"YearMonth\") \"YearMonth\" ");
			result.append("           ,\"IsRels\" ");
			result.append("           ,\"TypeCode\" ");
			result.append("           ,\"EntType\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("     GROUP BY \"IsRels\" ");
			result.append("             ,\"TypeCode\" ");
			result.append("             ,\"EntType\" ");
			result.append("  ) ");
			result.append(" ,ThisYearDataGrouped AS ( ");
			result.append("     SELECT \"IsRels\" ");
			result.append("           ,\"TypeCode\" ");
			result.append("           ,\"EntType\" ");
			result.append("           ,SUM(\"LoanBal\") \"LoanBalSumYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvSumYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedSumYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountSumYearly\" ");
			result.append("     FROM TotalData ");
			result.append("     GROUP BY \"IsRels\" ");
			result.append("             ,\"TypeCode\" ");
			result.append("             ,\"EntType\" ");
			result.append("  ) ");
			result.append(" ,ThisMonthDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalMonthly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalMonthly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalMonthly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalMonthly\" ");
			result.append("     FROM TotalData ");
			result.append("     WHERE \"YearMonth\" = :inputYearMonth ");
			result.append("  ) ");
			result.append(" ,ThisYearDataWhole AS ( ");
			result.append("     SELECT SUM(\"LoanBal\") \"LoanBalTotalYearly\" ");
			result.append("           ,SUM(\"IntRcv\") \"IntRcvTotalYearly\" ");
			result.append("           ,SUM(\"LoanBalWeighted\") \"LoanBalWeightedTotalYearly\" ");
			result.append("           ,SUM(\"Count\") \"CountTotalYearly\" ");
			result.append("     FROM TotalData ");
			result.append("  ) ");
			result.append("  ");
			result.append("  ");
			result.append(" SELECT \"DescriptionA\" F0 ");
			result.append(" 	  ,\"DescriptionB\" F1 ");
			result.append(" 	  ,\"MonthlyIntRcv\" F2 ");
			result.append(" 	  ,\"MonthlyIntRcvRatio\" F3 ");
			result.append(" 	  ,\"MonthlyLoanBal\" F4 ");
			result.append(" 	  ,\"MonthlyLoanBalRatio\" F5 ");
			result.append(" 	  ,\"MonthlyAvgStoreRate\" F6 ");
			result.append(" 	  ,\"YearlyIntRcv\" F7 ");
			result.append(" 	  ,\"YearlyIntRcvRatio\" F8 ");
			result.append(" 	  ,\"YearlyAvgLoanBal\" F9 ");
			result.append(" 	  ,\"YearlyAvgLoanBalRatio\" F10 ");
			result.append(" 	  ,\"YearlyAvgStoreRate\" F11 ");
			result.append(" FROM (SELECT DECODE(monthGroup.\"TypeCode\", 1, u'銀行保證' ");
			result.append("                                          , 2, u'有價證券' ");
			result.append("                                          , 3, u'不動產' ");
			result.append("                                          , 4, u'員工貸款' ");
			result.append("                                          , 5, u'動產')  ");
			result.append("              || CASE WHEN monthGroup.\"TypeCode\" = 4 ");
			result.append("                      THEN u' ' ");
			result.append("                 ELSE DECODE(monthGroup.\"EntType\", 0, u'（個人）', u'（公司）') END \"DescriptionA\" ");
			result.append("             ,DECODE(monthGroup.\"IsRels\", 'Y', u'關係人', u'非關係人') \"DescriptionB\" ");
			result.append("             ,monthGroup.\"IntRcvSumMonthly\" \"MonthlyIntRcv\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"IntRcvTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"IntRcvSumMonthly\" / monthWhole.\"IntRcvTotalMonthly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"MonthlyIntRcvRatio\" ");
			result.append("             ,monthGroup.\"LoanBalSumMonthly\" \"MonthlyLoanBal\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"LoanBalSumMonthly\" / monthWhole.\"LoanBalTotalMonthly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"MonthlyLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN monthGroup.\"LoanBalSumMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthGroup.\"LoanBalWeightedSumMonthly\" / monthGroup.\"LoanBalSumMonthly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') \"MonthlyAvgStoreRate\" ");
			result.append("             ,yearGroup.\"IntRcvSumYearly\" \"YearlyIntRcv\" ");
			result.append("             ,TO_CHAR(CASE WHEN yearWhole.\"IntRcvTotalYearly\" > 0 ");
			result.append("                           THEN ROUND(yearGroup.\"IntRcvSumYearly\" / yearWhole.\"IntRcvTotalYearly\" * 100, 1) ");
			result.append("                      ELSE 0 END, 'FM990.0') \"YearlyIntRcvRatio\" ");
			result.append("             ,ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) \"YearlyAvgLoanBal\" ");
			result.append(
					"             ,TO_CHAR(ROUND(ROUND(yearGroup.\"LoanBalSumYearly\" / MOD(:inputYearMonth, 100)) / ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) * 100, 1), 'FM999.0') \"YearlyAvgLoanBalRatio\" ");
			result.append("             ,TO_CHAR(CASE WHEN yearGroup.\"LoanBalSumYearly\" > 0 ");
			result.append("                           THEN ROUND(yearGroup.\"LoanBalWeightedSumYearly\" / yearGroup.\"LoanBalSumYearly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') \"YearlyAvgStoreRate\" ");
			result.append("             ,monthGroup.\"TypeCode\" \"TypeCode\" ");
			result.append("             ,monthGroup.\"EntType\" \"EntType\" ");
			result.append("             ,monthGroup.\"IsRels\" \"IsRels\" ");
			result.append("             ,ROW_NUMBER() OVER (PARTITION BY monthGroup.\"TypeCode\" ");
			result.append("                                             ,monthGroup.\"EntType\" ");
			result.append("                                     ORDER BY monthGroup.\"IsRels\" ASC) \"Seq\" ");
			result.append("       FROM ThisMonthDataGrouped monthGroup ");
			result.append("       LEFT JOIN ThisYearDataGrouped yearGroup ON yearGroup.\"TypeCode\" = monthGroup.\"TypeCode\" ");
			result.append("                                              AND yearGroup.\"EntType\" = monthGroup.\"EntType\" ");
			result.append("                                              AND yearGroup.\"IsRels\" = monthGroup.\"IsRels\" ");
			result.append("       LEFT JOIN ThisMonthDataWhole monthWhole ON 1 = 1 ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("  ");
			result.append("       UNION ALL ");
			result.append("  ");
			result.append("       SELECT u'合計' ");
			result.append("             ,u' ' ");
			result.append("             ,monthWhole.\"IntRcvTotalMonthly\" ");
			result.append("             ,'***' ");
			result.append("             ,monthWhole.\"LoanBalTotalMonthly\" ");
			result.append("             ,'***' ");
			result.append("             ,TO_CHAR(CASE WHEN monthWhole.\"LoanBalTotalMonthly\" > 0 ");
			result.append("                           THEN ROUND(monthWhole.\"LoanBalWeightedTotalMonthly\" / monthWhole.\"LoanBalTotalMonthly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,yearWhole.\"IntRcvTotalYearly\" ");
			result.append("             ,'***' ");
			result.append("             ,ROUND(yearWhole.\"LoanBalTotalYearly\" / MOD(:inputYearMonth, 100)) ");
			result.append("             ,'***' ");
			result.append("             ,TO_CHAR(CASE WHEN yearWhole.\"LoanBalTotalYearly\" > 0 ");
			result.append("                           THEN ROUND(yearWhole.\"LoanBalWeightedTotalYearly\" / yearWhole.\"LoanBalTotalYearly\", 3) ");
			result.append("                      ELSE 0 END, 'FM990.000') ");
			result.append("             ,'999' ");
			result.append("             ,'Z' ");
			result.append(" 	        ,'ZZZ' ");
			result.append(" 	        ,1 ");
			result.append("       FROM ThisMonthDataWhole monthWhole ");
			result.append("       LEFT JOIN ThisYearDataWhole yearWhole ON 1 = 1 ");
			result.append("      ) ");
			result.append(" ORDER BY \"TypeCode\" ");
			result.append("         ,\"EntType\" ");
			result.append("         ,\"IsRels\" ");
			break;
		default:
			return "";
		}

		return result.toString();
	}

}
