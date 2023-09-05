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
public class LM061ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param ymEnd 月底日(依yearMonth的月份)
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth, int ymEnd) throws Exception {


		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar calendar = Calendar.getInstance();

		String iYearMonth = String.valueOf(yearMonth);
		
		// 月底日
		int iDay = ymEnd % 100;

		// 一年前：月份扣13。 1月為0,2月為1 以此類推。
		calendar.set(iYear, iMonth - 13, iDay);

		int lastYearDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		String iOneYearAgo = String.valueOf(lastYearDate);

		this.info("iOneYearAgo:" + iOneYearAgo + ",iYYMM:" + iYearMonth);

		String sql = "";
		sql += "      SELECT M.\"CustNo\"";
		sql += "            ,M.\"FacmNo\"";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "            ,F.\"LineAmt\"";
		sql += "            ,M.\"UnpaidPrincipal\"";
		sql += "             + M.\"UnpaidInterest\" AS \"OvduPay\"";
		sql += "            ,M.\"PrinBalance\"";
		sql += "            ,M.\"PrevIntDate\"";
		sql += "            ,M.\"StoreRate\"";
		sql += "            ,F.\"MaturityDate\"";
		sql += "            ,M.\"OvduDate\"";
		sql += "            ,CD.\"Item\"";
		sql += "            ,COL.\"Amount\"";
		sql += "            ,COL.\"LegalProg\"";
		sql += "            ,LO.\"BadDebtAmt\"";
		sql += "            ,CB.\"BdLocation\"";
		sql += "            ,'V'";
//		sql += "            ,M.\"AccCollPsn\"";
		sql += "            ,CE.\"Fullname\"";
		sql += "      FROM \"MonthlyFacBal\" M";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "                            AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                            AND CF.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                             AND CL.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                             AND CL.\"ClNo\" = CF.\"ClNo\"";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = CL.\"ClCode1\"";
		sql += "                             	 AND CB.\"ClCode2\" = CL.\"ClCode2\"";
		sql += "                             	 AND CB.\"ClNo\" = CL.\"ClNo\"";
		sql += "      LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = M.\"AccCollPsn\"";
		sql += "      LEFT JOIN ( SELECT \"CustNo\"";
		sql += "                        ,\"FacmNo\"";
		sql += "                        ,\"LegalProg\"";
		sql += "                        ,\"RecordDate\"";
		sql += "                        ,\"Amount\"";
		sql += "                        ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\"";
		sql += "                                            ORDER BY \"RecordDate\" DESC";
		sql += "                                            		,\"TitaTxtNo\" DESC";
		sql += "                ) AS \"LawRowNo\"";
		sql += "      FROM \"CollLaw\"";
		sql += "    ) COL ON COL.\"CustNo\"  = M.\"CustNo\"";
		sql += "         AND COL.\"FacmNo\"  = M.\"FacmNo\"";
		sql += "         AND COL.\"LawRowNo\" = 1 ";
		sql += "	  LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'LegalProg'";
		sql += "             				  AND CD.\"Code\" = COL.\"LegalProg\"";
		sql += "      LEFT JOIN (SELECT \"CustNo\"";
		sql += "                       ,\"FacmNo\"";
		sql += "                       ,SUM(NVL(\"BadDebtAmt\",0)) AS \"BadDebtAmt\"";
		sql += "                       ,SUM(NVL(\"OvduAmt\",0)) AS \"OvduAmt\""; // 轉催收本息
		sql += "                       ,MIN(NVL(\"OvduDate\",99991231)) AS \"OvduDate\""; // 轉催收本息
		sql += "                 FROM \"LoanOverdue\"";
		sql += "                 GROUP BY \"CustNo\"";
		sql += "                 		 ,\"FacmNo\"";
		sql += "    ) LO ON LO.\"CustNo\"  = M.\"CustNo\"";
		sql += "         AND LO.\"FacmNo\"  = M.\"FacmNo\"";
		sql += "      WHERE M.\"YearMonth\" = :yymm";
		sql += "        AND M.\"Status\" IN (2,6,7)";
		sql += "        AND M.\"AssetClass\" || ";
		sql += "		   CASE";
		sql += "			 WHEN M.\"AcctCode\" = '990' ";
		sql += "		 	  AND M.\"ProdNo\" IN ('60','61','62')";
		sql += "		 	  AND M.\"AssetClass\" = 2 ";
		sql += "			 THEN '3'";
		sql += "			 WHEN M.\"OvduTerm\" >= 7";
		sql += "			  AND M.\"OvduTerm\" <= 12";
		sql += "		 	  AND M.\"AssetClass\" = 2 ";
		sql += "			 THEN '3'";
		sql += "			 WHEN M.\"AcctCode\" = '990'";
		sql += "			  AND M.\"OvduTerm\" <= 12";
		sql += "		 	  AND M.\"AssetClass\" = 2 ";
		sql += "			 THEN '3'";
		sql += "   		     WHEN M.\"AcctCode\" <> '990'";
		sql += "			  AND M.\"ProdNo\" IN ('60','61','62')";
		sql += "			  AND M.\"OvduTerm\" = 0";
		sql += "		 	  AND M.\"AssetClass\" = 2 ";
		sql += "			 THEN '1'";
		sql += "			 WHEN M.\"AcctCode\" <> '990'";
		sql += "			  AND M.\"OvduTerm\" >= 1";
		sql += "			  AND M.\"OvduTerm\" <= 6";
		sql += "		 	  AND M.\"AssetClass\" = 2 ";
		sql += "			 THEN '2'";
		sql += "			 ELSE '' END ";
		sql += "	   IN ('21','22','23','3','4','5')";
		sql += "        AND M.\"PrinBalance\" > 0 ";
		sql += "        AND M.\"OvduTerm\" > 0 ";
		sql += "     	AND M.\"PrevIntDate\" <= :iOneYearAgo";
		sql += "     ORDER BY M.\"CustNo\"";
		sql += "             ,M.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);
		query.setParameter("iOneYearAgo", iOneYearAgo);

		return this.convertToMap(query);
	}

}