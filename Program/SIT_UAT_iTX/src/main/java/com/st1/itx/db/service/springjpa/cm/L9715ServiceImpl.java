package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class L9715ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L9715ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String appro_dy = Integer.toString(Integer.parseInt(titaVo.getParam("APPRO_DAY").toString()) + 19110000);
		String fund_dy = Integer.toString(Integer.parseInt(titaVo.getParam("FUND_DAY").toString()) + 19110000);
		String day_st = "";
		String day_ed = "";

		if ("00".equals(titaVo.getParam("UNPAY_TERM_ST"))) {

			day_st = dateconvert(fund_dy, 2, Integer.parseInt(titaVo.getParam("UNPAY_DAY_ED").toString()));
			day_ed = dateconvert(fund_dy, 2, Integer.parseInt(titaVo.getParam("UNPAY_DAY_ST").toString()));
		} else {
			day_st = dateconvert(fund_dy, 1, Integer.parseInt(titaVo.getParam("UNPAY_TERM_ED").toString()));
			day_ed = dateconvert(fund_dy, 1, Integer.parseInt(titaVo.getParam("UNPAY_TERM_ST").toString()));
		}
		logger.info("day_st：" + day_st);
		logger.info("day_ed：" + day_ed);
		logger.info("l9715.findAll");

		String sql = " ";
		sql += "	SELECT E.\"EmployeeNo\" AS F0"; 
		sql += "		  ,E.\"Fullname\" AS F1";
		sql += "		  ,CD.\"CityItem\" AS F2";
		sql += "		  ,CC.\"Item\" AS F3";
		sql += "		  ,LPAD(TO_CHAR(D.\"CustNo\"),7,'0') || '-' || LPAD(TO_CHAR(D.\"FacmNo\"),3,'0') AS F4";
		sql += "		  ,D.\"CustName\" AS F5";
		sql += "		  ,D.\"FirstDrawdownDate\" AS F6";
		sql += "		  ,D.\"PrinBalance\" AS F7";
		sql += "		  ,D.\"StoreRate\" AS F8";
		sql += "		  ,D.\"PrevIntDate\" AS F9";
		sql += "		  ,D.\"NextIntDate\" AS F10";
		sql += "		  ,D.\"OvduDays\" AS F11";
		sql += "		  ,D.\"UnpaidLoan\" AS F12";
		sql += "		  ,D.\"UnpaidBreachAmt\" AS F13";
		sql += "		  ,D.\"OverShortAmt\" AS F14";
		sql += "          ,CASE";
		sql += "			 WHEN T1.\"Enable\" IS NOT NULL THEN T1.\"LiaisonName\"";
		sql += "             ELSE T2.\"LiaisonName\"";
		sql += "		   END AS F15";
		sql += "	      ,CASE";
		sql += "			 WHEN T1.\"Enable\" IS NOT NULL THEN DECODE(T1.\"TelArea\",NULL,' ',T1.\"TelArea\" || T1.\"TelNo\" || T1.\"TelExt\")";
		sql += "             ELSE DECODE(T2.\"TelArea\",NULL,' ',T2.\"TelArea\" || T2.\"TelNo\" || T2.\"TelExt\")";
		sql += "           END AS F16";
		sql += "          ,CASE";
		sql += "  		     WHEN T1.\"Enable\" IS NOT NULL THEN DECODE(T1.\"TelArea\",NULL,T1.\"TelNo\",' ')";
		sql += "             ELSE DECODE(T2.\"TelArea\",NULL,T2.\"TelNo\",' ')";
		sql += "   		   END AS F17";
		sql += "	FROM(SELECT F.\"BusinessOfficer\"";
		sql += "  			   ,L.\"CustNo\"";
		sql += "  			   ,L.\"FacmNo\"";
		sql += "  			   ,C.\"CustName\"";
		sql += "   			   ,F.\"FirstDrawdownDate\"";
		sql += "  			   ,L.\"PrinBalance\"";
		sql += "			   ,L.\"PrevIntDate\"";
		sql += "  			   ,L.\"NextIntDate\"";
		sql += "  			   ,L.\"OvduDays\"";
		sql += "  			   ,MF.\"StoreRate\"";
		sql += "  			   ,MF.\"UnpaidPrincipal\" + MF.\"UnpaidInterest\" AS \"UnpaidLoan\"";
		sql += "  			   ,MF.\"UnpaidBreachAmt\" + MF.\"UnpaidDelayInt\" AS \"UnpaidBreachAmt\"";
		sql += "  			   ,MF.\"ShortfallPrin\" + MF.\"ShortfallInt\" - MF.\"TempAmt\" AS \"OverShortAmt\"";
		sql += "  			   ,F.\"RepayCode\"";
		sql += "  			   ,C.\"CustUKey\"";
		sql += "  	     FROM \"CollList\" L";
		sql += "  		 LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "  		 LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "  								AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "  		 LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"CustNo\" = L.\"CustNo\"";
		sql += "  								       AND MF.\"FacmNo\" = L.\"FacmNo\"";
		sql += "  							    	   AND MF.\"YearMonth\" = TRUNC( :appro_dy / 100 )";
		sql += "  		 WHERE L.\"CaseCode\" = 1";
		sql += "  		   AND L.\"Status\" IN (0, 2, 4, 6)";
		sql += "   		   AND L.\"NextIntDate\" <= :day_ed ";
		sql += "   		   AND L.\"NextIntDate\" >= :day_st ";
		sql += "  		   AND F. \"FirstDrawdownDate\" >= :appro_dy ) D";
		sql += "	LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = D.\"BusinessOfficer\"";
		sql += "  	LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = D.\"CustNo\"";
		sql += "  						 AND F.\"FacmNo\" = D.\"FacmNo\"";
		sql += "  						 AND F.\"MainFlag\" = 'Y'";
		sql += "	LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = F.\"ClCode1\"";
		sql += "  						   AND Cl.\"ClCode2\" = F.\"ClCode2\"";
		sql += "  						   AND Cl.\"ClNo\" = F.\"ClNo\"";
		sql += "	LEFT JOIN \"CustTelNo\" T1 ON T1.\"CustUKey\" = D.\"CustUKey\"";
		sql += "  							  AND T1.\"TelTypeCode\" = '06'";
		sql += "  							  AND T1.\"Enable\" = 'Y'";
		sql += "	LEFT JOIN \"CustTelNo\" T2 ON T2.\"CustUKey\" = D.\"CustUKey\"";
		sql += "  							  AND T2.\"TelTypeCode\" = '01'";
		sql += "  							  AND T2.\"Enable\" = 'Y'";
		sql += "	LEFT JOIN \"CdCity\" CD ON CD.\"CityCode\" = CL.\"CityCode\"";
		sql += "	LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'RepayCode'";
		sql += "  						   AND CC.\"Code\" = D.\"RepayCode\"";
		sql += "	ORDER BY F0";
		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("appro_dy", appro_dy);
		query.setParameter("day_st", day_st);
		query.setParameter("day_ed", day_ed);
		return this.convertToMap(query.getResultList());
	}

	public String dateconvert(String str, int type, int day_or_term) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dt = sdf.parse(str);

		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);

		if (type == 1) {
			rightNow.add(Calendar.MONTH, 0 - day_or_term);// 日期加3個月
		} else if (type == 2) {
			rightNow.add(Calendar.DAY_OF_YEAR, 0 - day_or_term);// 日期加10天
		}

		Date dt1 = rightNow.getTime();

		String reStr = sdf.format(dt1);

		return reStr;
	}



	// sql += "	SELECT Cl.\"CityCode\" F0";
	// sql += "		  ,E.\"Fullname\" F1";
	// sql += "		  ,D.\"CustNo\" F2";
	// sql += "          ,D.\"FacmNo\" F3";
	// sql += "		  ,D.\"CustName\" F4";
	// sql += "		  ,D.\"FirstDrawdownDate\" F5";
	// sql += "		  ,D.\"PrinBalance\" F6";
	// sql += "		  ,D.\"PrevIntDate\" F7";
	// sql += "		  ,D.\"NextIntDate\" F8";
	// sql += "		  ,D.\"OvduDays\" F9";
	// sql += "          ,CASE";
	// sql += "			 WHEN T1.\"Enable\" IS NOT NULL THEN T1.\"LiaisonName\"";
	// sql += "             ELSE T2.\"LiaisonName\"";
	// sql += "		   END F10 ";
	// sql += "	      ,CASE";
	// sql += "			 WHEN T1.\"Enable\" IS NOT NULL THEN DECODE(T1.\"TelArea\",NULL,' ',T1.\"TelArea\" || T1.\"TelNo\" || T1.\"TelExt\")";
	// sql += "             ELSE DECODE(T2.\"TelArea\",NULL,' ',T2.\"TelArea\" || T2.\"TelNo\" || T2.\"TelExt\")";
	// sql += "           END F11";
	// sql += "          ,CASE";
	// sql += "  		     WHEN T1.\"Enable\" IS NOT NULL THEN DECODE(T1.\"TelArea\",NULL,T1.\"TelNo\",' ')";
	// sql += "             ELSE DECODE(T2.\"TelArea\",NULL,T2.\"TelNo\",' ')";
	// sql += "   		   END F12";
	// sql += "	FROM(SELECT F.\"BusinessOfficer\"";
	// sql += "  			   ,L.\"CustNo\"";
	// sql += "  			   ,L.\"FacmNo\"";
	// sql += "  			   ,C.\"CustName\"";
	// sql += "   			   ,F.\"FirstDrawdownDate\"";
	// sql += "  			   ,L.\"PrinBalance\"";
	// sql += "			   ,L.\"PrevIntDate\"";
	// sql += "  			   ,L.\"NextIntDate\"";
	// sql += "  			   ,L.\"OvduDays\"";
	// sql += "  			   ,F.\"RepayCode\"";
	// sql += "  			   ,C.\"CustUKey\"";
	// sql += "  	     FROM \"CollList\" L";
	// sql += "  		 LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
	// sql += "  		 LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
	// sql += "  								AND F.\"FacmNo\" = L.\"FacmNo\"";
	// sql += "  		 WHERE L.\"CaseCode\" = 1";
	// sql += "  		   AND L.\"Status\" IN (0, 2, 4, 6)";
	// sql += "   		   AND L.\"NextIntDate\" <= :day_ed ";
	// sql += "   		   AND L.\"NextIntDate\" >= :day_st ";
	// sql += "  		   AND F. \"FirstDrawdownDate\" >= :appro_dy ) D";
	// sql += "	LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = D.\"BusinessOfficer\"";
	// sql += "  	LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = D.\"CustNo\"";
	// sql += "  						 AND F.\"FacmNo\" = D.\"FacmNo\"";
	// sql += "  						 AND F.\"MainFlag\" = 'Y'";
	// sql += "	LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = F.\"ClCode1\"";
	// sql += "  						   AND Cl.\"ClCode2\" = F.\"ClCode2\"";
	// sql += "  						   AND Cl.\"ClNo\" = F.\"ClNo\"";
	// sql += "	LEFT JOIN \"CustTelNo\" T1 ON T1.\"CustUKey\" = D.\"CustUKey\"";
	// sql += "  							  AND T1.\"TelTypeCode\" = '06'";
	// sql += "  							  AND T1.\"Enable\" = 'Y'";
	// sql += "	LEFT JOIN \"CustTelNo\" T2 ON T2.\"CustUKey\" = D.\"CustUKey\"";
	// sql += "  							  AND T2.\"TelTypeCode\" = '01'";
	// sql += "  							  AND T2.\"Enable\" = 'Y'";
}