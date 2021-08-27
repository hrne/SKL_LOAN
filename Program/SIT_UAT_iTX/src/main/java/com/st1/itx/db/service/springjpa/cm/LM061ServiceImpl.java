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

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {


		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}
		
		//月底日
		int iDay = thisMonthEndDate % 100;	
		
		// 一年前：月份扣13。 1月為0,2月為1 以此類推。 
		calMonthDate.set(iYear, iMonth-13, iDay);

		int lastYearDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		String iYYMM = iYear + String.format("%02d", iMonth);

		String iOneYearAgo = String.valueOf(lastYearDate);

		this.info("iOneYearAgo:" + iOneYearAgo + ",iYYMM:" + iYYMM);

		String sql = "";
		sql += "      SELECT M.\"CustNo\"";
		sql += "            ,M.\"FacmNo\"";
		sql += "            ,C.\"CustName\"";
		sql += "            ,F.\"LineAmt\"";
		sql += "            ,M.\"UnpaidPrincipal\"";
		sql += "             + M.\"UnpaidInterest\" AS \"OvduPay\"";
		sql += "            ,M.\"PrinBalance\"";
		sql += "            ,M.\"PrevIntDate\"";
		sql += "            ,M.\"StoreRate\"";
		sql += "            ,F.\"MaturityDate\"";
		sql += "            ,LO.\"OvduDate\"";
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
		sql += "        AND M.\"AssetClass\" IN ('21','22','23','3','4','5')";
		sql += "        AND M.\"OvduTerm\" > 0 ";
		sql += "     	AND M.\"PrevIntDate\" <= :iOneYearAgo";
		sql += "     ORDER BY M.\"CustNo\"";
		sql += "             ,M.\"FacmNo\"";

		// String sql = "SELECT F.\"CustNo\" AS F0"; // 戶號
		// sql += " ,F.\"FacmNo\" AS F1"; // 額度
		// sql += " ,C.\"CustName\" AS F2"; // 戶名
		// sql += " ,F.\"LineAmt\" AS F3"; // 核貸金額
		// sql += " ,NVL(LO.\"OvduAmt\",0) AS F4"; // 轉催收本息
		// sql += " ,M.\"OvduBal\" AS F5"; // 催收款餘額
		// sql += " ,M.\"PrevIntDate\" AS F6"; // 繳息迄日
		// sql += " ,M.\"StoreRate\" AS F7"; // 利率
		// sql += " ,F.\"MaturityDate\" AS F8"; // 到期日
		// sql += " ,CASE";
		// sql += " WHEN NVL(LO.\"OvduDate\",99991231) < 99991231 THEN LO.\"OvduDate\"";
		// sql += " ELSE 0 END AS F9"; // 轉催收日
		// sql += " ,F.\"Amount1\" AS F10"; // 鑑價(拍底)金額
		// sql += " ,'LTV' AS F11"; // LTV
		// sql += " ,NVL(F.\"Memo\",'')";
		// sql += " || NVL(D.\"Item\",'') AS F12"; // 執行程序
		// sql += " ,F.\"Amount2\" AS F13"; // 拍定不足金額
		// sql += " ,M.\"BadDebtBal\" AS F14"; // 轉呆金額
		// sql += " ,DECODE(M.\"RenewCode\",'2','V',' ')";
		// sql += " AS F15"; // 協議分期
		// sql += " ,F.\"Amount3\" AS F16"; // 協議金額
		// sql += " ,NVL(B.\"BdLocation\",L.\"LandLocation\")";
		// sql += " AS F17"; // 擔保品座落
		// sql += " ,'F18' AS F18"; // 符合規範
		// sql += " ,E.\"Fullname\" AS F19"; // 催收人員
		// sql += " ,'F20' AS F20"; // 備註
		// sql += " FROM(SELECT F.\"CustNo\"";
		// sql += " ,F.\"FacmNo\"";
		// sql += " ,F.\"LineAmt\"";
		// sql += " ,F.\"MaturityDate\"";
		// sql += " ,SUM(CASE";
		// sql += " WHEN F.\"LegalProg\" = '048' AND F.\"SEQ\" = 1 THEN F.\"Amount\"";
		// sql += " ELSE 0 END) \"Amount1\"";
		// sql += " ,MAX(CASE";
		// sql += " WHEN F.\"SEQ2\" = 1 THEN F.\"LegalProg\"";
		// sql += " ELSE NULL END) \"LegalProg\"";
		// sql += " ,MAX(CASE";
		// sql += " WHEN F.\"SEQ2\" = 1 THEN F.\"Memo\"";
		// sql += " ELSE NULL END) \"Memo\"";
		// sql += " ,SUM(CASE";
		// sql += " WHEN F.\"LegalProg\" = '901' AND F.\"SEQ\" = 1 THEN F.\"Amount\"";
		// sql += " ELSE 0 END) \"Amount2\"";
		// sql += " ,SUM(CASE";
		// sql += " WHEN F.\"LegalProg\" = '077' AND F.\"SEQ\" = 1 THEN F.\"Amount\"";
		// sql += " ELSE 0 END) \"Amount3\"";
		// sql += " FROM(SELECT F.\"CustNo\"";
		// sql += " ,F.\"FacmNo\"";
		// sql += " ,F.\"LineAmt\"";
		// sql += " ,F.\"MaturityDate\"";
		// sql += " ,L.\"LegalProg\"";
		// sql += " ,L.\"Amount\"";
		// sql += " ,L.\"Memo\"";
		// sql += " ,ROW_NUMBER() OVER (PARTITION BY F.\"CustNo\", F.\"FacmNo\",
		// L.\"LegalProg\" ORDER BY L.\"RecordDate\" DESC) AS SEQ";
		// sql += " ,ROW_NUMBER() OVER (PARTITION BY F.\"CustNo\", F.\"FacmNo\" ORDER BY
		// L.\"RecordDate\" DESC) AS SEQ2";
		// sql += " FROM(SELECT L.\"CustNo\"";
		// sql += " ,L.\"FacmNo\"";
		// sql += " ,F.\"LineAmt\"";
		// sql += " ,F.\"MaturityDate\"";
		// sql += " FROM \"CollList\" L";
		// sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		// sql += " AND F.\"FacmNo\" = L.\"FacmNo\"";
		// sql += " WHERE L.\"FacmNo\" > 0";
		// sql += " AND L.\"OvduDays\" > 0";
		// sql += " AND L.\"PrevIntDate\" > 0";
		// sql += " AND F.\"MaturityDate\" > 0";
		// sql += " AND CASE";
		// // 條件1:逾期兩年以上
		// sql += " WHEN L.\"PrevIntDate\" <= :iTwoYearsAgo";
		// sql += " THEN 1";
		// // 條件2:逾期一年以上,即將屆清償期兩年
		// sql += " WHEN L.\"PrevIntDate\" <= :iOneYearAgo";
		// sql += " AND
		// MONTHS_BETWEEN(TO_DATE(F.\"MaturityDate\",'YYYYMMDD'),TO_DATE(L.\"PrevIntDate\",'YYYYMMDD'))
		// >= 12";
		// sql += " THEN 2";
		// sql += " ELSE 0 END > 0";
		// sql += " ) F";
		// sql += " LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" = '1'";
		// sql += " AND L.\"CustNo\" = F.\"CustNo\"";
		// sql += " AND L.\"FacmNo\" = F.\"FacmNo\"";
		// sql += " ) F";
		// sql += " WHERE F.\"SEQ\" = 1";
		// sql += " GROUP BY
		// F.\"CustNo\",F.\"FacmNo\",F.\"LineAmt\",F.\"MaturityDate\"";
		// sql += " ) F";
		// sql += " LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
		// sql += " AND M.\"CustNo\" = F.\"CustNo\"";
		// sql += " AND M.\"FacmNo\" = F.\"FacmNo\"";
		// sql += " LEFT JOIN (SELECT LBM.\"CustNo\"";
		// sql += " ,LBM.\"FacmNo\"";
		// sql += " ,SUM(NVL(LO.\"OvduAmt\",0)) AS \"OvduAmt\""; // 轉催收本息
		// sql += " ,MIN(NVL(LO.\"OvduDate\",99991231)) AS \"OvduDate\""; // 轉催收本息
		// sql += " FROM \"LoanBorMain\" LBM";
		// sql += " LEFT JOIN \"LoanOverdue\" LO ON LO.\"CustNo\" = LBM.\"CustNo\"";
		// sql += " AND LO.\"FacmNo\" = LBM.\"FacmNo\"";
		// sql += " AND LO.\"BormNo\" = LBM.\"BormNo\"";
		// sql += " AND LO.\"OvduNo\" = LBM.\"LastOvduNo\"";
		// sql += " WHERE LBM.\"Status\" IN (2,7)";
		// sql += " GROUP BY LBM.\"CustNo\"";
		// sql += " ,LBM.\"FacmNo\"";
		// sql += " ) LO ON LO.\"CustNo\" = M.\"CustNo\"";
		// sql += " AND LO.\"FacmNo\" = M.\"FacmNo\"";
		// sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\"";
		// sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = M.\"AccCollPsn\"";
		// sql += " LEFT JOIN \"ClBuilding\" B ON B.\"ClCode1\" = M.\"ClCode1\"";
		// sql += " AND B.\"ClCode2\" = M.\"ClCode2\"";
		// sql += " AND B.\"ClNo\" = M.\"ClNo\"";
		// sql += " LEFT JOIN \"ClLand\" L ON L.\"ClCode1\" = M.\"ClCode1\"";
		// sql += " AND L.\"ClCode2\" = M.\"ClCode2\"";
		// sql += " AND L.\"ClNo\" = M.\"ClNo\"";
		// sql += " LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'LegalProg'";
		// sql += " AND D.\"DefType\" = 2";
		// sql += " AND D.\"Code\" = F.\"LegalProg\"";
		// sql += " ORDER BY F.\"CustNo\", F.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYYMM);
		// query.setParameter("iTwoYearsAgo", iTwoYearsAgo);
		query.setParameter("iOneYearAgo", iOneYearAgo);

		return this.convertToMap(query.getResultList());
	}

}