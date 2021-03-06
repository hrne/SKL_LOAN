package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class LM053ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM053.findAll");
		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
		int nowDate = Integer.valueOf(iEntdy);

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		// calendar.set(iYear, iMonth, 0);
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("1.thisMonthEndDate=" + thisMonthEndDate);

		// 確認是否為1月
		boolean isMonthZero = iMonth - 1 == 0;

		// 當前日期 比 當月底日期 前面 就取上個月底日
		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("yymm:" + iYearMonth);

		String sql = "SELECT CL.\"RecordDate\" F0";
		sql += "            ,CI.\"CityItem\"     F1";
		sql += "            ,L.\"CustNo\"        F2";
		sql += "            ,L.\"FacmNo\"        F3";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0)      F4";
		sql += "            ,L.\"Amount056\"     F5";
		sql += "            ,DECODE(L.\"Amount060\",0,L.\"Amount058\" ";
		sql += "			 + L.\"Amount092\",L.\"Amount060\" + L.\"Amount092\")  F6";
		sql += "            ,CASE WHEN M.\"PrinBalance\" < L.\"Amount901\" THEN L.\"Amount901\" - M.\"PrinBalance\"";
		sql += "             ELSE 0 END          F7";
		sql += "            ,CL.\"AcDate\"     F8";
		sql += "           ,'F9'                 F9";
		sql += "            ,E.\"Fullname\"      F10";
		sql += "            ,CASE WHEN L.\"Amount058\" <> L.\"Amount060\" THEN '金額不合'";
		sql += "             ELSE ' ' END        F11";
		sql += "      FROM (SELECT CLM.\"CustNo\"";
		sql += "                  ,CLM.\"FacmNo\"";
		sql += "                  ,SUM(DECODE(CLM.\"LegalProg\",'056',CLM.\"Amount\",0)) \"Amount056\"";
		sql += "                  ,SUM(DECODE(CLM.\"LegalProg\",'058',CLM.\"Amount\",0)) \"Amount058\"";
		sql += "                  ,SUM(DECODE(CLM.\"LegalProg\",'092',CLM.\"Amount\",0)) \"Amount092\"";
		sql += "                  ,SUM(DECODE(CLM.\"LegalProg\",'060',CLM.\"Amount\",0)) \"Amount060\"";
		sql += "                  ,SUM(DECODE(CLM.\"LegalProg\",'901',CLM.\"Amount\",0)) \"Amount901\"";
		sql += "            FROM (SELECT \"CustNo\"";
		sql += "                        ,\"FacmNo\"";
		sql += "                        ,L.\"LegalProg\"";
		sql += "                        ,MAX(L.\"TitaTxtNo\") \"TitaTxtNo\"";
		sql += "                        ,SUM(L.\"Amount\") \"Amount\"";
		sql += "                  FROM \"CollLaw\" L";
		sql += "                  WHERE L.\"LegalProg\" IN ('056','060','092','901','058')";
		sql += "				  GROUP BY \"CustNo\"";
		sql += "                          ,\"FacmNo\"";
		sql += "                          ,\"LegalProg\") CLM";
		sql += "            WHERE CLM.\"Amount\" > 0";
		sql += "		    GROUP BY CLM.\"CustNo\"";
		sql += "                    ,CLM.\"FacmNo\") L";
		sql += "	  LEFT JOIN ( SELECT \"CustNo\"";
		sql += "						,\"FacmNo\"";
		sql += "						,MAX(\"TitaTxtNo\") \"TitaTxtNo\"";
		sql += "				  FROM \"CollLaw\"";
		sql += "				  GROUP BY \"CustNo\"";
		sql += "						  ,\"FacmNo\") CLL";
		sql += " 	  ON CLL.\"CustNo\" = L.\"CustNo\" AND CLL.\"FacmNo\" = L.\"FacmNo\"";
		sql += "      LEFT JOIN \"CollLaw\" CL ON CL.\"CustNo\" = CLL.\"CustNo\"";
		sql += "                              AND CL.\"FacmNo\" = CLL.\"FacmNo\"";
		sql += "                              AND CL.\"TitaTxtNo\" = CLL.\"TitaTxtNo\"";
		sql += "      LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
		sql += "                                   AND M.\"CustNo\"    =  L.\"CustNo\"";
		sql += "                                   AND M.\"FacmNo\"    =  L.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "      LEFT JOIN \"CdCity\" CI ON Ci.\"CityCode\" = M.\"CityCode\"";
		sql += "      LEFT JOIN \"CdEmp\"  E ON E.\"EmployeeNo\" = M.\"LegalPsn\"";
		sql += " 	  ORDER BY L.\"CustNo\"";
		sql += "  			  ,L.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);

		return this.convertToMap(query);
	}

}

//String sql = "SELECT L.\"RecordDate058\" F0";
//sql += "            ,Ci.\"CityItem\"     F1";
//sql += "            ,L.\"CustNo\"        F2";
//sql += "            ,L.\"FacmNo\"        F3";
//sql += "            ,\"Fn_ParseEOL\"(CM.\"CustName\",0)      F4";
//sql += "            ,L.\"Amount056\"     F5";
//sql += "            ,DECODE(L.\"Amount058\",0,L.\"Amount058\",L.\"Amount060\")     F6";
//sql += "            ,CASE WHEN M.\"PrinBalance\" < L.\"Amount901\" THEN L.\"Amount901\" - M.\"PrinBalance\"";
//sql += "             ELSE 0 END          F7";
//sql += "            ,L.\"AcDate060\"     F8";
//sql += "           ,'F9'                 F9";
//sql += "            ,E.\"Fullname\"      F10";
//sql += "            ,CASE WHEN L.\"Amount058\" <> L.\"Amount060\" THEN '金額不合'";
//sql += "             ELSE ' ' END        F11";
//sql += "      FROM (SELECT L.\"RecordDate058\"";
//sql += "                  ,L.\"CustNo\"";
//sql += "                  ,L.\"FacmNo\"";
//sql += "                  ,L.\"Amount058\"";
//sql += "                  ,L.\"AcDate\"";
//sql += "                  ,L.\"TitaTlrNo\"";
//sql += "                  ,L.\"TitaTxtNo\"";
//sql += "                  ,SUM(L.\"Amount056\") \"Amount056\"";
//sql += "                  ,SUM(L.\"Amount060\") \"Amount060\"";
//sql += "                  ,SUM(L.\"Amount901\") \"Amount901\"";
//sql += "                  ,MAX(L.\"AcDate060\") \"AcDate060\"";
//sql += "            FROM (SELECT L.\"RecordDate058\"";
//sql += "                        ,L.\"CustNo\"";
//sql += "                        ,L.\"FacmNo\"";
//sql += "                        ,L.\"Amount058\"";
//sql += "                        ,L.\"AcDate\"";
//sql += "                        ,L.\"TitaTlrNo\"";
//sql += "                        ,L.\"TitaTxtNo\"";
//sql += "                        ,DECODE(T.\"LegalProg\"";
//sql += "                              ,'056'";
//sql += "                               ,T.\"Amount\"";
//sql += "                               ,0) \"Amount056\"";
//sql += "                        ,DECODE(T.\"LegalProg\"";
//sql += "                              ,'060'";
//sql += "                               ,T.\"Amount\"";
//sql += "                               ,0) \"Amount060\"";
//sql += "                        ,DECODE(T.\"LegalProg\"";
//sql += "                              ,'901'";
//sql += "                               ,T.\"Amount\"";
//sql += "                               ,0) \"Amount901\"";
//sql += "                        ,DECODE(T.\"LegalProg\"";
//sql += "                              ,'060'";
//sql += "                               ,T.\"AcDate\"";
//sql += "                               ,0) \"AcDate060\"";
//sql += "                        ,ROW_NUMBER() OVER (PARTITION BY L.\"RecordDate058\"";
//sql += "                                                        ,L.\"CustNo\"";
//sql += "                                                        ,L.\"FacmNo\"";
//sql += "                                                         ORDER BY T.\"RecordDate\" DESC) AS SEQ";
//sql += "                  FROM (SELECT L.\"RecordDate\" \"RecordDate058\"";
//sql += "                              ,L.\"CustNo\", L.\"FacmNo\"";
//sql += "                              ,L.\"Amount\" \"Amount058\"";
//sql += "                              ,L.\"AcDate\"";
//sql += "                              ,L.\"TitaTlrNo\"";
//sql += "                              ,L.\"TitaTxtNo\"";
//sql += "                        FROM \"CollLaw\" L";
//sql += "                        WHERE L.\"LegalProg\" = '058') L";
//sql += "                  LEFT JOIN \"CollLaw\" T ON T.\"CustNo\" = L.\"CustNo\"";
//sql += "                                         AND T.\"FacmNo\" = L.\"FacmNo\"";
//sql += "                                         AND T.\"LegalProg\" IN ('056', '060', '901')";
//sql += "				   WHERE T.\"Amount\" > 0 ) L";
//sql += "            WHERE L.\"SEQ\" = 1";
//sql += "            GROUP BY L.\"RecordDate058\"";
//sql += "                    ,L.\"CustNo\"";
//sql += "                    ,L.\"FacmNo\"";
//sql += "                    ,L.\"Amount058\"";
//sql += "                    ,L.\"AcDate\"";
//sql += "                    ,L.\"TitaTlrNo\"";
//sql += "                    ,L.\"TitaTxtNo\") L";
//sql += "      LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
//sql += "                                   AND M.\"CustNo\"    =  L.\"CustNo\"";
//sql += "                                   AND M.\"FacmNo\"    =  L.\"FacmNo\"";
//sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
//sql += "      LEFT JOIN \"CdCity\" Ci ON Ci.\"CityCode\" = M.\"CityCode\"";
//sql += "      LEFT JOIN \"CdEmp\"  E ON E.\"EmployeeNo\" = M.\"LegalPsn\"";