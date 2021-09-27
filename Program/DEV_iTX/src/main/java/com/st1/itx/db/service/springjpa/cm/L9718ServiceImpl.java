package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class L9718ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, boolean findOvdu) throws Exception {
		this.info("l9718.findAll ");

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthLastDate = Calendar.getInstance();
		// 設當年月底日
		calMonthLastDate.set(iYear, iMonth, 0);

		int monthLastDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < monthLastDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		
		
		
		String sql = "";
		sql += " SELECT ";
		sql += "        EMP.\"Fullname\" AS \"EmpName\" ";
		sql += "       ,SUBSTR(:inputYearMonth, 1, 3) AS \"RocYear\" ";
		sql += "       ,:inputEntryDateMin - 19110000 AS \"RocEntryDateMin\" ";
		sql += "       ,:inputEntryDateMax - 19110000 AS \"RocEntryDateMax\" ";
		sql += "       ,M.\"OvduTerm\" AS \"OvduTerm\" ";
		sql += "       ,M.\"CustNo\" AS \"CustNo\" ";
		sql += "       ,M.\"FacmNo\" AS \"FacmNo\" ";
		sql += "       ,CUS.\"CustName\" AS \"CustName\" ";
		sql += "       ,CASE WHEN M.\"EntCode\" = '1' ";
		sql += "             THEN '企金' ";
		sql += "             ELSE ' ' ";
		sql += "        END AS \"IsEnt\" ";
		sql += "       ,M.\"FireFee\" AS \"FireFee\" ";
		sql += "       ,M.\"LawFee\" AS \"LawFee\" ";
		sql += "       ,CASE WHEN M.\"AcctCode\" = '990' ";
		sql += "             THEN M.\"OvduBal\" ";
		sql += "             ELSE M.\"UnpaidPrincipal\" ";
		sql += "                + M.\"UnpaidInterest\" ";
		sql += "                + M.\"UnpaidBreachAmt\" ";
		sql += "                + M.\"UnpaidDelayInt\" ";
		sql += "                + M.\"TempAmt\" ";
		sql += "        END AS \"OvduBal\" ";
		sql += "       ,M.\"OvduBal\" AS \"OvduBalAgain\" ";
		sql += "       ,GREATEST(MDate.\"PrevIntDate\" - 19110000, 0) AS \"rocPrevIntDate\" ";
		sql += "       ,CASE WHEN M.\"AcctCode\" = '990' ";
		sql += "             THEN NVL(TX.\"OvTxAmt\",0) ";
		sql += "        ELSE NVL(TX.\"LnTxAmt\",0) ";
		sql += "        END AS \"TxAmt\" ";
		sql += "       ,GREATEST((CASE WHEN M.\"AcctCode\" = '990' ";
		sql += "                       THEN TX.\"OvEntryDate\" ";
		sql += "                  ELSE      TX.\"LnEntryDate\" ";
		sql += "                  END) - 19110000, 0) AS \"EntryDate\" ";
		sql += "       ,0 AS \"RetrievedRatio\" "; // done in report
		sql += "       ,CASE WHEN NVL(COL.\"PrinBalance\",1) = 0 ";
		sql += "             THEN '結案' ";
		sql += "             ELSE ' ' ";
		sql += "        END AS \"IsClosed\" ";
		sql += "       ,LAW.\"LegalProg\" AS \"LegalProgCode\" ";
		sql += "       ,CDL.\"Item\" AS \"LegalProgItem\" ";
		sql += "       ,GREATEST(LAW.\"RecordDate\" - 19110000, 0) AS \"rocRecordDate\" ";
		sql += "       ,CITY.\"CityItem\" AS \"CityItem\" ";
		sql += " FROM \"MonthlyFacBal\" M ";
		sql += " LEFT JOIN (SELECT \"CustNo\" ";
		sql += "                  ,\"FacmNo\" ";
		sql += "                  ,MAX(\"PrevIntDate\") \"PrevIntDate\" ";
		sql += "            FROM \"MonthlyFacBal\" ";
		sql += "            GROUP BY \"CustNo\" ";
		sql += "                    ,\"FacmNo\") MDate ON MDate.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                    AND MDate.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" CUS ON CUS.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdEmp\" EMP ON EMP.\"EmployeeNo\" = M.\"AccCollPsn\" ";
		sql += " LEFT JOIN \"CollList\" COL ON COL.\"CustNo\" = M.\"CustNo\" ";
		sql += "                         AND COL.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,\"FacmNo\" ";
		sql += "                   ,\"LegalProg\" ";
		sql += "                   ,\"RecordDate\" ";
		sql += "                   ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\" ";
		sql += "                                       ORDER BY \"RecordDate\" ASC ";
		sql += "                                               ,\"CreateDate\" ASC ";
		sql += "           ) AS \"LawRowNo\" ";
		sql += " FROM \"CollLaw\" ";
		sql += "     ) LAW ON LAW.\"CustNo\"  = M.\"CustNo\" ";
		sql += "  AND LAW.\"FacmNo\"  = M.\"FacmNo\" ";
		sql += "  AND LAW.\"LawRowNo\" = 1 ";
		sql += " LEFT JOIN \"CdCode\" CDL ON CDL.\"DefCode\" = 'LegalProg' ";
		sql += "         AND CDL.\"Code\" = LAW.\"LegalProg\" ";
		sql += " LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = M.\"CityCode\" ";
		sql += " LEFT JOIN (SELECT \"CustNo\" ";
		sql += "            ,\"FacmNo\" ";
		sql += "            ,MAX(\"IntEndDate\")  AS \"IntEndDate\" ";
		sql += "            ,SUM(CASE WHEN NVL(JSON_VALUE(\"OtherFields\", '$.CaseCloseCode'),'0') = '4' ";
		sql += "                      THEN \"TxAmt\" ";
		sql += "                      ELSE 0 ";
		sql += "                 END) AS \"OvTxAmt\" ";
		sql += "            ,MAX(CASE WHEN NVL(JSON_VALUE(\"OtherFields\", '$.CaseCloseCode'),'0') = '4' ";
		sql += "                      THEN \"EntryDate\" ";
		sql += "                     ELSE 0 ";
		sql += "                 END) AS \"OvEntryDate\" ";
		sql += "            ,SUM(\"Principal\" + \"Interest\" +  \"DelayInt\" + \"BreachAmt\" + \"TempAmt\") AS \"LnTxAmt\" ";
		sql += "            ,MAX(CASE WHEN (\"Principal\" + \"Interest\" +  \"DelayInt\" + \"BreachAmt\" + \"TempAmt\") > 0 ";
		sql += "                      THEN \"EntryDate\" ";
		sql += "                      ELSE 0 ";
		sql += "                 END) AS \"LnEntryDate\" ";
		sql += "      FROM \"LoanBorTx\" ";
		sql += "      WHERE \"TitaHCode\" = '0' ";
		sql += "        AND \"EntryDate\" >= :inputEntryDateMin ";
		sql += "        AND \"EntryDate\" <= :inputEntryDateMax ";
		sql += "      GROUP BY \"CustNo\", \"FacmNo\" ";
		sql += "     ) TX ON TX.\"CustNo\"  = M.\"CustNo\" ";
		sql += "         AND TX.\"FacmNo\"  = M.\"FacmNo\" ";
		sql += " WHERE M.\"YearMonth\" = :inputYearMonth ";
		sql += "   AND (NVL(:inputCollector, ' ') = ' ' OR NVL(M.\"AccCollPsn\",'X') = :inputCollector) ";
		sql += "   AND FAC.\"FirstDrawdownDate\" >= :inputDrawdownDate ";
		sql += "   AND ((:findOvdu = 'Y' AND M.\"AcctCode\" = '990') ";
		sql += "        OR ";
		sql += "        (:findOvdu = 'N' AND M.\"OvduDays\" > 0) ";
		sql += "       ) ";
		sql += "   AND M.\"PrinBalance\" > 0 ";
		sql += " ORDER BY M.\"CustNo\" ";
		sql += "         ,M.\"FacmNo\" ";
		
 
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		LocalDate lastYearMonth = LocalDate.of(Integer.parseInt(titaVo.getParam("inputYearMonth").substring(0, 3)) + 1911, Integer.parseInt(titaVo.getParam("inputYearMonth").substring(3)), 1);
		lastYearMonth = lastYearMonth.minusMonths(1);
		
		this.info("inputEntryDateMin=" + Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMin")) + 19110000));
		this.info("inputEntryDateMax=" + Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMax")) + 19110000));
		this.info("inputYearMonth=" + iYear + String.format("%02d", iMonth));
		this.info("inputCollectorShow=" + titaVo.getParam("inputCollector"));
		this.info("inputDrawdownDate="	 +Integer.toString(Integer.parseInt(titaVo.getParam("inputDrawdownDate")) + 19110000));

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputEntryDateMin", Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMin")) + 19110000));
		query.setParameter("inputEntryDateMax", Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMax")) + 19110000));
		query.setParameter("inputYearMonth", iYear + String.format("%02d", iMonth));
		query.setParameter("inputCollector", titaVo.getParam("inputCollector"));
		query.setParameter("inputDrawdownDate",
				Integer.toString(Integer.parseInt(titaVo.getParam("inputDrawdownDate")) + 19110000));
		query.setParameter("findOvdu", findOvdu ? "Y" : "N");

		return this.convertToMap(query);
	}

}