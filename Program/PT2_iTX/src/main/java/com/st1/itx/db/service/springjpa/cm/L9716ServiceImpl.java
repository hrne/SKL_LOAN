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
public class L9716ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		// LNW0581E
		this.info("l9716.findAll ");

//		LocalDate inputYearMonth = LocalDate.of(Integer.parseInt(titaVo.getParam("inputYear")) + 1911, Integer.parseInt(titaVo.getParam("inputMonth")), 1);
//		inputYearMonth = inputYearMonth.minusMonths(1);

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = Integer.valueOf(titaVo.getParam("inputYear")) + 1911;
		int iMonth = Integer.parseInt(titaVo.getParam("inputMonth"));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}
		
		this.info("inputYearMonth=" + iYear + String.format("%02d", iMonth));
		this.info("inputCollPsn=" + titaVo.getParam("inputCollPsn"));
		this.info("inputOvduTermMin=" + titaVo.getParam("inputOvduTermMin"));
		this.info("inputOvduTermMax=" + titaVo.getParam("inputOvduTermMax"));
		

		String sql = "SELECT M.\"OvduTerm\" F0";
		sql += "            ,EMP.\"Fullname\" F1";
		sql += "            ,:inputYearMonth - 191100 AS F2";
		sql += "            ,CITY.\"CityItem\" F3";
		sql += "            ,M.\"CustNo\" F4";
		sql += "            ,M.\"FacmNo\" F5";
		sql += "            ,NVL(\"Fn_ParseEOL\"(C.\"CustName\",0),'') F6";
		sql += "            ,FAC.\"FirstDrawdownDate\" - 19110000 AS  F7";
		sql += "            ,M.\"PrinBalance\" F8";
		sql += "            ,M2.\"StoreRate\" F9";
		sql += "            ,M.\"PrevIntDate\" - 19110000 AS F10";
		sql += "            ,M.\"OvduDays\" F11";
		sql += "            ,M.\"UnpaidPrincipal\" + M.\"UnpaidInterest\" F12";
		sql += "            ,M.\"UnpaidBreachAmt\" + M.\"UnpaidDelayInt\" F13";
		sql += "            ,M.\"TempAmt\" - M.\"ShortfallPrin\" - M.\"ShortfallInt\"  AS F14";
		sql += "            ,M.\"FireFee\" F15";
		sql += "            ,M.\"LawFee\" F16";
		sql += "            ,M.\"AcctFee\" F17";
		sql += "            ,M.\"ModifyFee\" F18";
		sql += "            ,(M.\"UnpaidPrincipal\"";
		sql += "             + M.\"UnpaidInterest\"";
		sql += "             + M.\"UnpaidBreachAmt\"";
		sql += "             + M.\"UnpaidDelayInt\")";
		sql += "             -(M.\"TempAmt\"";
		sql += "             - M.\"ShortfallPrin\"";
		sql += "             - M.\"ShortfallInt\")";
		sql += "             + M.\"FireFee\"";
		sql += "             + M.\"LawFee\" AS F19";
		sql += "            ,NVL(CUS.\"LiaisonName\",CUS2.\"LiaisonName\") F20";
		sql += "            ,NVL(\"Fn_GetTelNo\"(C.\"CustUKey\",'01',1),\"Fn_GetTelNo\"(C.\"CustUKey\",'03',1)) F21";
		sql += "            ,LAW.\"LegalProg\" F22";
		sql += "            ,CDL.\"Item\" F23";
		sql += "            ,LAW.\"RecordDate\" - 19110000 AS F24";
		sql += "      FROM \"MonthlyFacBal\" M";
		sql += "      LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\"";
		sql += "                               AND FAC.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      LEFT JOIN (SELECT \"CustNo\"";
		sql += "                       ,\"FacmNo\"";
		sql += "                       ,\"StoreRate\"";
		sql += "                       ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\"";
		sql += "                                           ORDER BY \"BormNo\" DESC) AS \"SEQ\"";
		sql += "                 FROM \"MonthlyLoanBal\"";
		sql += "                 WHERE \"YearMonth\" = :inputYearMonth ";
		sql += "                   AND \"LoanBalance\" > 0 ";
		sql += "                ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                    AND M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                    AND M2.\"SEQ\" = 1";
		sql += "      LEFT JOIN (SELECT C.\"CustNo\"";
		sql += "                       ,\"Fn_ParseEOL\"(C.\"CustName\",0)";
		sql += "                       ,NVL(T.\"LiaisonName\",\"Fn_ParseEOL\"(C.\"CustName\",0)) AS \"LiaisonName\"";
		sql += "					   ,T.\"TelArea\" AS \"TelArea\"";
		sql += "					   ,T.\"TelNo\" AS \"TelNo\"";
		sql += "					   ,T.\"TelTypeCode\" AS \"TelTypeCode\"";
		sql += "                       ,ROW_NUMBER() OVER (PARTITION BY C.\"CustNo\",T.\"TelTypeCode\"";
		sql += "                                           ORDER BY CASE WHEN T.\"TelTypeCode\" = '06'";
		sql += "                                                         THEN '0'";
		sql += "                                                         ELSE T.\"TelNoUKey\"";
		sql += "                                                    END) AS \"TelRowNo\"";
		sql += "                 FROM \"CustMain\" C";
		sql += "                 LEFT JOIN \"CustTelNo\" T ON T.\"CustUKey\" = C.\"CustUKey\"";
		sql += "                                          AND T.\"Enable\" = 'Y'";
		sql += "                 WHERE T.\"TelTypeCode\" = '01'";
		sql += "                ) CUS ON CUS.\"CustNo\" = M.\"CustNo\"";
		sql += "                     AND CUS.\"TelRowNo\" = 1";
		sql += "      LEFT JOIN (SELECT C.\"CustNo\"";
		sql += "                       ,\"Fn_ParseEOL\"(C.\"CustName\",0)";
		sql += "                       ,NVL(T.\"LiaisonName\",\"Fn_ParseEOL\"(C.\"CustName\",0)) AS \"LiaisonName\"";
		sql += "					   ,T.\"TelArea\" AS \"TelArea\"";
		sql += "					   ,T.\"TelNo\" AS \"TelNo\"";
		sql += "					   ,T.\"TelTypeCode\" AS \"TelTypeCode\"";
		sql += "                       ,ROW_NUMBER() OVER (PARTITION BY C.\"CustNo\",T.\"TelTypeCode\"";
		sql += "                                           ORDER BY CASE WHEN T.\"TelTypeCode\" = '06'";
		sql += "                                                         THEN '0'";
		sql += "                                                         ELSE T.\"TelNoUKey\"";
		sql += "                                                    END) AS \"TelRowNo\"";
		sql += "                 FROM \"CustMain\" C";
		sql += "                 LEFT JOIN \"CustTelNo\" T ON T.\"CustUKey\" = C.\"CustUKey\"";
		sql += "                                          AND T.\"Enable\" = 'Y'";
		sql += "                 WHERE T.\"TelTypeCode\" = '03'";
		sql += "                ) CUS2 ON CUS2.\"CustNo\" = M.\"CustNo\"";
		sql += "                     AND CUS2.\"TelRowNo\" = 1";
		sql += "      LEFT JOIN \"CdEmp\" EMP ON EMP.\"EmployeeNo\" = M.\"AccCollPsn\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
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
//		sql += "        AND M.\"OvduDays\" > 0";
		sql += "        AND M.\"OvduTerm\" >= :inputOvduTermMin";
		sql += "        AND M.\"OvduTerm\" <= :inputOvduTermMax";
		sql += "   		AND TRUNC(M.\"NextIntDate\" / 100) <= :inputYearMonth ";
		sql += "   		AND TRUNC(M.\"PrevIntDate\" / 100) <> :inputYearMonth ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", iYear + String.format("%02d", iMonth));
		query.setParameter("inputCollPsn", titaVo.getParam("inputCollPsn"));
		query.setParameter("inputOvduTermMin", titaVo.getParam("inputOvduTermMin"));
		query.setParameter("inputOvduTermMax", titaVo.getParam("inputOvduTermMax"));

		return this.convertToMap(query);
	}

	public List<Map<String, String>> ovduFindAll(TitaVo titaVo) throws Exception {
		this.info("l9716.ovduFindAll");

//		LocalDate inputYearMonth = LocalDate.of(Integer.parseInt(titaVo.getParam("inputYear")) + 1911, Integer.parseInt(titaVo.getParam("inputMonth")), 1);
//		inputYearMonth = inputYearMonth.minusMonths(1);

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = Integer.valueOf(titaVo.getParam("inputYear")) + 1911;
		int iMonth = Integer.parseInt(titaVo.getParam("inputMonth"));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}
		
	
		String sql = " ";
		sql += "      SELECT DECODE(M.\"AcSubBookCode\",'00A',' ','201','A') AS F0";
		sql += "            ,L.\"CustNo\" F1";
		sql += "            ,L.\"FacmNo\" AS F2";
		sql += "            ,L.\"BormNo\" F3";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) F4";
		sql += "            ,F.\"FirstDrawdownDate\" F5";
		sql += "            ,L.\"PrevPayIntDate\" F6";
		sql += "            ,NVL(LO.\"OvduBal\",MB.\"OvduBal\") F7";
		sql += "            ,LO.\"OvduDate\" F8";
		sql += "            ,NVL(LO.\"OvduPrinAmt\",MB.\"OvduPrinBal\") F9";
		sql += "            ,NVL(LO.\"OvduIntAmt\",MB.\"OvduIntBal\") F10";
		sql += "            ,NVL(LO.\"OvduPrinAmt\",MB.\"OvduPrinBal\")";
		sql += "             + NVL(LO.\"OvduIntAmt\",MB.\"OvduIntBal\") ";
		sql += "             - NVL(LO.\"OvduBal\",MB.\"OvduBal\")";
		sql += "             - NVL(LO.\"BadDebtAmt\",0) F11";
		sql += "            ,\"Fn_GetTelNo\"(C.\"CustUKey\",'01',1) AS F12";
		sql += "            ,\"Fn_GetTelNo\"(C.\"CustUKey\",'03',1) AS F13";
		sql += "            ,C1.\"CityItem\"";
		sql += "            || C2.\"AreaItem\"";
		sql += "            || C.\"CurrRoad\"";
		sql += "            || DECODE(C.\"CurrSection\",NULL,'',C.\"CurrSection\" || '段')";
		sql += "            || DECODE(C.\"CurrAlley\",NULL,'',C.\"CurrAlley\" || '巷')";
		sql += "            || DECODE(C.\"CurrLane\",NULL,'',C.\"CurrLane\" || '弄')";
		sql += "            || DECODE(C.\"CurrNumDash\",NULL";
		sql += "										,DECODE(C.\"CurrNum\",NULL,'',C.\"CurrNum\" || '號')";
		sql += "										,DECODE(C.\"CurrNum\",NULL,'',C.\"CurrNum\" || '之') || C.\"CurrNumDash\" || '號')";
		sql += "            || DECODE(C.\"CurrFloor\",NULL,'',C.\"CurrFloor\" || '樓')";
		sql += "            || DECODE(C.\"CurrFloorDash\",NULL,'','之' || C.\"CurrFloorDash\") AS F14";
		sql += "            ,\"Fn_GetTelNo\"(C.\"CustUKey\",'01',1) AS F15";
		sql += "            ,C3.\"CityItem\"";
		sql += "            || C4.\"AreaItem\"";
		sql += "            || C.\"RegRoad\"";
		sql += "            || DECODE(C.\"RegSection\",NULL,'',C.\"RegSection\" || '段')";
		sql += "            || DECODE(C.\"RegAlley\",NULL,'',C.\"RegAlley\" || '巷')";
		sql += "            || DECODE(C.\"RegLane\",NULL,'',C.\"RegLane\" || '弄')";
		sql += "            || DECODE(C.\"RegNumDash\",NULL";
		sql += "										,DECODE(C.\"RegNum\",NULL,'',C.\"RegNum\" || '號')";
		sql += "										,DECODE(C.\"RegNum\",NULL,'',C.\"RegNum\" || '之') || C.\"RegNumDash\" || '號')";
		sql += "            || DECODE(C.\"RegFloor\",NULL,'',C.\"RegFloor\" || '樓')";
		sql += "            || DECODE(C.\"RegFloorDash\",NULL,'','之' || C.\"RegFloorDash\") AS F16";
		sql += "            ,:inputYearMonth AS F17";
		sql += "            ,CL.\"CityCode\" F18";
		sql += "            ,Cl.\"ClCode1\" F19";
		sql += "            ,CL.\"ClCode2\" F20";
		sql += "            ,CB.\"BdLocation\" F21";
		sql += "            ,M.\"AcctCode\" F22";
		sql += "            ,M.\"EntCode\" F23";
		sql += "            ,F.\"CustTypeCode\" F24";
		sql += "            ,M.\"FacAcctCode\" F25";
		sql += "            ,F.\"MaturityDate\" F26";
		sql += "            ,M.\"StoreRate\" F27";
//		sql += "            ,MB.\"UnpaidPrincipal\" F28";
		sql += "            ,F.\"UtilBal\" F28";
		sql += "            ,F.\"UtilAmt\" F29";
		sql += "            ,F.\"LineAmt\" F30";
		sql += "            ,CB.\"FloorArea\" F31";
		sql += "            ,NVL(CBP.\"Area\",0) AS F32";
		sql += "            ,NVL(CB.\"ParkingArea\",0) AS F33";
		sql += "            ,NVL(CB.\"EvaUnitPrice\",0) AS F34";
		sql += "            ,DECODE(CB.\"BdDate\",0,0,TRUNC(CB.\"BdDate\" / 10000)) AS F35";
		sql += "            ,F.\"CreditOfficer\" F36";
		sql += "            ,F.\"BusinessOfficer\" F37";
//		sql += "            ,L.\"DrawdownAmt\" F38";
		sql += "            ,MB.\"AccCollPsn\" F38";
		sql += "            ,F.\"UsageCode\" F39";
		sql += "            ,F.\"ProdNo\" F40";
		sql += "            ,CE.\"Fullname\" F41";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"MonthlyFacBal\" MB ON MB.\"CustNo\" = M.\"CustNo\"";
		sql += "                                	AND MB.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 	AND MB.\"YearMonth\" = M.\"YearMonth\"";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = M.\"BormNo\"";
		sql += "      LEFT JOIN \"LoanOverdue\" LO ON LO.\"CustNo\" = L.\"CustNo\"";
		sql += "                                 AND LO.\"FacmNo\" = L.\"FacmNo\"";
		sql += "                                 AND LO.\"BormNo\" = L.\"BormNo\"";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "                            AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                            AND CF.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                             AND CL.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                             AND CL.\"ClNo\" = CF.\"ClNo\"";
		sql += "      LEFT JOIN \"CdCity\" C1 ON C1.\"CityCode\" =  C.\"CurrCityCode\"";
		sql += "      LEFT JOIN \"CdArea\" C2 ON C2.\"CityCode\" =  C.\"CurrCityCode\"";
		sql += "                             AND C2.\"AreaCode\" =  C.\"CurrAreaCode\"";
		sql += "      LEFT JOIN \"CdCity\" C3 ON C3.\"CityCode\" =  C.\"CurrCityCode\"";
		sql += "      LEFT JOIN \"CdArea\" C4 ON C4.\"CityCode\" =  C.\"CurrCityCode\"";
		sql += "                             AND C4.\"AreaCode\" =  C.\"CurrAreaCode\"";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = CL.\"ClCode1\"";
		sql += "                             	 AND CB.\"ClCode2\" = CL.\"ClCode2\"";
		sql += "                             	 AND CB.\"ClNo\" = CL.\"ClNo\"";
		sql += "      LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                   	    ,\"ClCode2\"";
		sql += "                        ,\"ClNo\"";
		sql += "                        ,\"Area\"";
		sql += "            		    ,ROW_NUMBER()OVER(PARTITION BY \"ClCode1\"";
		sql += "            							  		      ,\"ClCode2\"";
		sql += "            							  		      ,\"ClNo\"";
		sql += "            				 		      ORDER BY \"PublicBdNo1\" ) AS \"SEQ\"";
		sql += "                   FROM \"ClBuildingPublic\") CBP";
		sql += "       ON CBP.\"ClCode1\" = CB.\"ClCode1\"";
		sql += "      AND CBP.\"ClCode2\" = CB.\"ClCode2\"";
		sql += "      AND CBP.\"ClNo\" = CB.\"ClNo\"";
		sql += "      AND CBP.\"SEQ\" = 1 ";
		sql += "      LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = MB.\"AccCollPsn\"";
		sql += "      WHERE M.\"YearMonth\" = :inputYearMonth";
		sql += "        AND M.\"AcctCode\" = 990 ";
		sql += "        AND M.\"LoanBalance\" > 0 ";
		sql += "      ORDER BY L.\"CustNo\" ASC";
		sql += "              ,L.\"FacmNo\" ASC";
		sql += "     		  ,L.\"BormNo\" ASC";

		// String sql = "SELECT * FROM (";
		// sql += "      SELECT DECODE(M.\"AcSubBookCode\",'00A',' ','201','A') AS F0";
		// sql += "            ,L.\"CustNo\" F1";
		// sql += "            ,L.\"FacmNo\" AS F2";
		// sql += "            ,L.\"BormNo\" F3";
		// sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) F4";
		// sql += "            ,F.\"FirstDrawdownDate\" F5";
		// sql += "            ,M.\"PrevIntDate\" F6";
		// sql += "            ,LO.\"OvduBal\" F7";
		// sql += "            ,LO.\"OvduDate\" F8";
		// sql += "            ,LO.\"OvduPrinAmt\" F9";
		// sql += "            ,LO.\"OvduIntAmt\" F10";
		// sql += "            ,LO.\"OvduPrinAmt\"";
		// sql += "             + LO.\"OvduIntAmt\"";
		// sql += "             - LO.\"OvduBal\"";
		// sql += "             - LO.\"BadDebtAmt\" F11";
		// sql += "            ,\"Fn_GetTelNo\"(C.\"CustUKey\",'01',1) AS F12";
		// sql += "            ,\"Fn_GetTelNo\"(C.\"CustUKey\",'03',1) AS F13";
		// sql += "            ,C1.\"CityItem\"";
		// sql += "            || C2.\"AreaItem\"";
		// sql += "            || C.\"CurrRoad\"";
		// sql += "            || DECODE(C.\"CurrSection\",NULL,'',C.\"CurrSection\" || '段')";
		// sql += "            || DECODE(C.\"CurrAlley\",NULL,'',C.\"CurrAlley\" || '巷')";
		// sql += "            || DECODE(C.\"CurrLane\",NULL,'',C.\"CurrLane\" || '弄')";
		// sql += "            || DECODE(C.\"CurrNumDash\",NULL";
		// sql += "										,DECODE(C.\"CurrNum\",NULL,'',C.\"CurrNum\" || '號')";
		// sql += "										,DECODE(C.\"CurrNum\",NULL,'',C.\"CurrNum\" || '之') || C.\"CurrNumDash\" || '號')";
		// sql += "            || DECODE(C.\"CurrFloor\",NULL,'',C.\"CurrFloor\" || '樓')";
		// sql += "            || DECODE(C.\"CurrFloorDash\",NULL,'','之' || C.\"CurrFloorDash\") AS F14";
		// sql += "            ,\"Fn_GetTelNo\"(C.\"CustUKey\",'01',1) AS F15";
		// sql += "            ,C3.\"CityItem\"";
		// sql += "            || C4.\"AreaItem\"";
		// sql += "            || C.\"RegRoad\"";
		// sql += "            || DECODE(C.\"RegSection\",NULL,'',C.\"RegSection\" || '段')";
		// sql += "            || DECODE(C.\"RegAlley\",NULL,'',C.\"RegAlley\" || '巷')";
		// sql += "            || DECODE(C.\"RegLane\",NULL,'',C.\"RegLane\" || '弄')";
		// sql += "            || DECODE(C.\"RegNumDash\",NULL";
		// sql += "										,DECODE(C.\"RegNum\",NULL,'',C.\"RegNum\" || '號')";
		// sql += "										,DECODE(C.\"RegNum\",NULL,'',C.\"RegNum\" || '之') || C.\"RegNumDash\" || '號')";
		// sql += "            || DECODE(C.\"RegFloor\",NULL,'',C.\"RegFloor\" || '樓')";
		// sql += "            || DECODE(C.\"RegFloorDash\",NULL,'','之' || C.\"RegFloorDash\") AS F16";
		// sql += "            ,:inputYearMonth AS F17";
		// sql += "            ,CL.\"CityCode\" F18";
		// sql += "            ,Cl.\"ClCode1\" F19";
		// sql += "            ,CL.\"ClCode2\" F20";
		// sql += "            ,CB.\"BdLocation\" F21";
		// sql += "            ,M.\"AcctCode\" F22";
		// sql += "            ,M.\"EntCode\" F23";
		// sql += "            ,F.\"CustTypeCode\" F24";
		// sql += "            ,M.\"FacAcctCode\" F25";
		// sql += "            ,L.\"MaturityDate\" F26";
		// sql += "            ,M.\"StoreRate\" F27";
		// sql += "            ,M.\"UnpaidPrincipal\" F28";
		// sql += "            ,F.\"LineAmt\" F29";
		// sql += "            ,CB.\"FloorArea\" F30";
		// sql += "            ,NVL(CBP.\"Area\",0) AS F31";
		// sql += "            ,' ' AS F32";
		// sql += "            ,' ' AS F33";
		// sql += "            ,F.\"CreditOfficer\" F34";
		// sql += "            ,F.\"BusinessOfficer\" F35";
		// sql += "            ,F.\"UtilBal\" F36";
		// sql += "            ,0 AS F37";
		// sql += "            ,M.\"AccCollPsn\" F38";
		// sql += "            ,F.\"UsageCode\" F39";
		// sql += "            ,F.\"ProdNo\" F40";
		// sql += "            ,CE.\"Fullname\" F41";
		// sql += "            ,ROW_NUMBER()OVER(PARTITION BY L.\"CustNo\"";
		// sql += "            							  ,L.\"FacmNo\"";
		// sql += "            							  ,L.\"BormNo\"";
		// sql += "            				  ORDER BY T1.\"TelTypeCode\" ) AS \"SEQ\"";
		// sql += "      FROM \"MonthlyFacBal\" M";
		// sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		// sql += "                                 AND L.\"FacmNo\" = M.\"FacmNo\"";
		// sql += "      LEFT JOIN \"LoanOverdue\" LO ON LO.\"CustNo\" = L.\"CustNo\"";
		// sql += "                                 AND LO.\"FacmNo\" = L.\"FacmNo\"";
		// sql += "                                 AND LO.\"BormNo\" = L.\"BormNo\"";
		// sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		// sql += "                             AND F.\"FacmNo\" = M.\"FacmNo\"";
		// sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		// sql += "      LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		// sql += "                            AND CF.\"FacmNo\" = M.\"FacmNo\"";
		// sql += "                            AND CF.\"MainFlag\" = 'Y'";
		// sql += "      LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\"";
		// sql += "                             AND CL.\"ClCode2\" = CF.\"ClCode2\"";
		// sql += "                             AND CL.\"ClNo\" = CF.\"ClNo\"";
		// sql += "      LEFT JOIN \"CustTelNo\" T1 ON T1.\"CustUKey\" = C.\"CustUKey\"";
		// sql += "                             	AND T1.\"Enable\" = 'Y'";
		// sql += "      LEFT JOIN \"CdCity\" C1 ON C1.\"CityCode\" =  C.\"CurrCityCode\"";
		// sql += "      LEFT JOIN \"CdArea\" C2 ON C2.\"CityCode\" =  C.\"CurrCityCode\"";
		// sql += "                             AND C2.\"AreaCode\" =  C.\"CurrAreaCode\"";
		// sql += "      LEFT JOIN \"CdCity\" C3 ON C3.\"CityCode\" =  C.\"CurrCityCode\"";
		// sql += "      LEFT JOIN \"CdArea\" C4 ON C4.\"CityCode\" =  C.\"CurrCityCode\"";
		// sql += "                             AND C4.\"AreaCode\" =  C.\"CurrAreaCode\"";
		// sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = CL.\"ClCode1\"";
		// sql += "                             	 AND CB.\"ClCode2\" = CL.\"ClCode2\"";
		// sql += "                             	 AND CB.\"ClNo\" = CL.\"ClNo\"";
		// sql += "      LEFT JOIN ( SELECT \"ClCode1\"";
		// sql += "                   	    ,\"ClCode2\"";
		// sql += "                        ,\"ClNo\"";
		// sql += "                        ,\"Area\"";
		// sql += "            		    ,ROW_NUMBER()OVER(PARTITION BY \"ClCode1\"";
		// sql += "            							  		      ,\"ClCode2\"";
		// sql += "            							  		      ,\"ClNo\"";
		// sql += "            				 		      ORDER BY \"PublicBdNo1\" ) AS \"SEQ\"";
		// sql += "                   FROM \"ClBuildingPublic\") CBP";
		// sql += "       ON CBP.\"ClCode1\" = CB.\"ClCode1\"";
		// sql += "      AND CBP.\"ClCode2\" = CB.\"ClCode2\"";
		// sql += "      AND CBP.\"ClNo\" = CB.\"ClNo\"";
		// sql += "      AND CBP.\"SEQ\" = 1 ";
		// sql += "      LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = M.\"AccCollPsn\"";
		// sql += "      WHERE M.\"YearMonth\" = :inputYearMonth";
		// sql += "        AND L.\"Status\" IN (2,6,7)";
		// sql += "        AND M.\"AssetClass\" IN ('21','22','23','3','4','5')";
		// sql += "        AND M.\"OvduTerm\" >= 0 ";
		// sql += "        AND M.\"PrinBalance\" > 0 ";
		// sql += "     ORDER BY L.\"CustNo\"";
		// sql += "             ,L.\"FacmNo\"";
		// sql += "     		 ,L.\"BormNo\") RES";
		// sql += "     WHERE RES.\"SEQ\" = 1 ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		this.info("L9716 inputYearMonth: " + iYear + String.format("%02d", iMonth));
		query.setParameter("inputYearMonth", iYear + String.format("%02d", iMonth));

		return this.convertToMap(query);
	}

}