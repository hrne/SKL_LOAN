package com.st1.itx.db.service.springjpa.cm;

import java.time.LocalDate;
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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class L9716ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L9716ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("l9716.findAll ");
		
		LocalDate inputYearMonth = LocalDate.of(Integer.parseInt(titaVo.getParam("inputYear")) + 1911, Integer.parseInt(titaVo.getParam("inputMonth")), 1);
		inputYearMonth = inputYearMonth.minusMonths(1);
				
		String sql = "SELECT M.\"OvduTerm\" F0";
		sql += "            ,EMP.\"Fullname\" F1";
		sql += "            ,:inputYearMonth - 191100 AS F2";
		sql += "            ,CITY.\"CityItem\" F3";
		sql += "            ,M.\"CustNo\" F4";
		sql += "            ,M.\"FacmNo\" F5";
		sql += "            ,CUS.\"CustName\" F6";
		sql += "            ,FAC.\"FirstDrawdownDate\" F7";
		sql += "            ,M.\"PrinBalance\" F8";
		sql += "            ,M.\"StoreRate\" F9";
		sql += "            ,M.\"PrevIntDate\" F10";
		sql += "            ,M.\"OvduDays\" F11";
		sql += "            ,M.\"UnpaidPrincipal\" + M.\"UnpaidInterest\" F12";
		sql += "            ,M.\"UnpaidBreachAmt\" + M.\"UnpaidDelayInt\" F13";
		sql += "            ,M.\"ShortfallPrin\" + M.\"ShortfallInt\" - M.\"TempAmt\" AS F14";
		sql += "            ,M.\"FireFee\" F15";
		sql += "            ,M.\"LawFee\" F16";
		sql += "            ,M.\"AcctFee\" F17";
		sql += "            ,M.\"ModifyFee\" F18";
		sql += "            ,M.\"UnpaidPrincipal\"";
		sql += "             + M.\"UnpaidInterest\"";
		sql += "             + M.\"UnpaidBreachAmt\"";
		sql += "             + M.\"UnpaidDelayInt\" F19";
		sql += "            ,CUS.\"LiaisonName\" F20";
		sql += "            ,CUS.\"TelNo\" F21";
		sql += "            ,LAW.\"LegalProg\" F22";
		sql += "            ,CDL.\"Item\" F23";
		sql += "            ,LAW.\"RecordDate\" F24";
		sql += "      FROM \"MonthlyFacBal\" M";
		sql += "      LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\"";
		sql += "                               AND FAC.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      LEFT JOIN (SELECT C.\"CustNo\"";
		sql += "                       ,C.\"CustName\"";
		sql += "                       ,NVL(T.\"LiaisonName\",C.\"CustName\") AS \"LiaisonName\"";
		sql += "                       ,NVL(T.\"TelArea\",'')";
		sql += "                        || NVL(T.\"TelNo\",'')";
		sql += "                        || CASE WHEN NVL(T.\"TelExt\",' ') <> ' ' THEN ' ' || NVL(T.\"TelExt\",'') ELSE '' END";
		sql += "                        AS \"TelNo\"";
		sql += "                       ,ROW_NUMBER() OVER (PARTITION BY C.\"CustNo\"";
		sql += "                                           ORDER BY CASE WHEN T.\"TelTypeCode\" = '06'";
		sql += "                                                         THEN '0'";
		sql += "                                                         ELSE T.\"TelNoUKey\"";
		sql += "                                                    END) AS \"TelRowNo\"";
		sql += "                 FROM \"CustMain\" C";
		sql += "                 LEFT JOIN \"CustTelNo\" T ON T.\"CustUKey\" = C.\"CustUKey\"";
		sql += "                                          AND T.\"Enable\" = 'Y'";
		sql += "                ) CUS ON CUS.\"CustNo\" = M.\"CustNo\"";
		sql += "                     AND CUS.\"TelRowNo\" = 1";
		sql += "      LEFT JOIN \"CdEmp\" EMP ON EMP.\"EmployeeNo\" = M.\"AccCollPsn\"";
		sql += "      LEFT JOIN (SELECT \"CustNo\"";
		sql += "                       ,\"FacmNo\"";
		sql += "                       ,\"LegalProg\"";
		sql += "                       ,\"RecordDate\"";
		sql += "                       ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\"";
		sql += "                                           ORDER BY \"RecordDate\" ASC";
		sql += "                                                   ,\"CreateDate\" ASC";
		sql += "                                          ) AS \"LawRowNo\"";
		sql += "                 FROM \"CollLaw\"";
		sql += "                ) LAW ON LAW.\"CustNo\" = M.\"CustNo\"";
		sql += "                     AND LAW.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                     AND LAW.\"LawRowNo\" = 1";
		sql += "      LEFT JOIN \"CdCode\" CDL ON CDL.\"DefCode\" = 'LegalProg'";
		sql += "                              AND CDL.\"Code\" = LAW.\"LegalProg\"";
		sql += "      LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = M.\"CityCode\"";
		sql += "      WHERE M.\"YearMonth\" = :inputYearMonth";
		sql += "        AND M.\"Status\" = 0";
		sql += "        AND :inputCollPsn IN ('999999', NVL(M.\"AccCollPsn\",' '))";
		sql += "        AND M.\"OvduDays\" > 0";
		sql += "        AND M.\"OvduTerm\" >= :inputOvduTermMin";
		sql += "        AND M.\"OvduTerm\" <= :inputOvduTermMax";

		logger.info("sql=" + sql);
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		Query query;
		query = em.createNativeQuery(sql);
		logger.info("L9716 inputYearMonth: " + Integer.toString(inputYearMonth.getYear()) + String.format("%02d",inputYearMonth.getMonthValue()) + " peko");
		query.setParameter("inputYearMonth", Integer.toString(inputYearMonth.getYear()) + String.format("%02d",inputYearMonth.getMonthValue()));
		query.setParameter("inputCollPsn", titaVo.getParam("inputCollPsn"));
		query.setParameter("inputOvduTermMin", titaVo.getParam("inputOvduTermMin"));
		query.setParameter("inputOvduTermMax", titaVo.getParam("inputOvduTermMax"));
		
		return this.convertToMap(query.getResultList());
	}

}