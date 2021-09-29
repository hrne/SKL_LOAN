package com.st1.itx.db.service.springjpa.cm;

import java.time.LocalDate;
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

	public enum ReportType {
		Acct990("催收"), AcctOthers("逾期");
		
		private String desc = "";
		
		ReportType(String _desc)
		{
			this.desc = _desc;
		}
		
		public String getDesc()
		{
			return this.desc;
		}
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, ReportType reportType) throws Exception {
		this.info("l9718.findAll ");

		String sql = "";

		switch (reportType) {
		case Acct990:
			sql += " SELECT ";
			sql += "   EMP.\"Fullname\" AS \"EmpName\" ";
			sql += "  ,TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(:inputYearMonth, 'YYYYMM'), -1), 'YYYYMM'), 5, 2))+1 AS \"RocMonth\" ";
			sql += "  ,:inputEntryDateMin - 19110000 AS \"RocEntryDateMin\" ";
			sql += "  ,:inputEntryDateMax - 19110000 AS \"RocEntryDateMax\" ";
			sql += "  ,M.\"OvduTerm\" AS \"OvduTerm\" ";
			sql += "  ,M.\"CustNo\" AS \"CustNo\" ";
			sql += "  ,M.\"FacmNo\" AS \"FacmNo\" ";
			sql += "  ,CUS.\"CustName\" AS \"CustName\" ";
			sql += "  ,CASE WHEN M.\"EntCode\" = '1' ";
			sql += "        THEN '企金' ";
			sql += "        ELSE ' ' ";
			sql += "   END AS \"IsEnt\" ";
			sql += "  ,M.\"FireFee\" AS \"FireFee\" ";
			sql += "  ,M.\"LawFee\" AS \"LawFee\" ";
			sql += "  ,0 AS \"OvduBal\" "; // 催收餘額 樣張為空白 ??
			sql += "  ,M.\"OvduBal\" AS \"YetRetrievedBal\" ";
			sql += "  ,NVL(TO_CHAR(GREATEST(MDate.\"PrevIntDate\" - 19110000, 0)), ' ') AS \"rocPrevIntDate\" ";
            sql += " ,NVL(TX.\"LnTxAmt\",0) AS \"TxAmt\" ";
            sql += " ,NVL(GREATEST((CASE WHEN M.\"AcctCode\" = '990' AND NVL(COL.\"PrinBalance\", 1) = 0  ";
            sql += "                      THEN TX.\"OvEntryDate\"   ";
            sql += "                 ELSE 0 ";
            sql += "                 END) - 19110000, 0), 0) AS \"EntryDate\" ";
			sql += "  ,0 AS \"RetrievedRatio\" "; // done in report
			sql += "  ,CASE WHEN LBM.\"Status\" = 7 ";
			sql += "        THEN '部份轉呆' ";
			sql += "        WHEN LBM.\"Status\" = 0 ";
			sql += "        THEN '催收轉正常' ";
			sql += "        WHEN LBM.\"Status\" = 6 ";
			sql += "        THEN '轉呆帳' ";
			sql += "        WHEN LBM.\"Status\" = 5 ";
			sql += "        THEN '催收結案' ";
			sql += "        ELSE ' ' ";
			sql += "   END AS \"IsClosed\" ";
			sql += "  ,LAW.\"LegalProg\" AS \"LegalProgCode\" ";
			sql += "  ,CDL.\"Item\" AS \"LegalProgItem\" ";
			sql += "  ,GREATEST(LAW.\"RecordDate\" - 19110000, 0) AS \"rocRecordDate\" ";
			sql += "  ,CITY.\"CityItem\" AS \"CityItem\" ";
			sql += "  FROM \"MonthlyFacBal\" M ";
			sql += "  LEFT JOIN (SELECT \"CustNo\" ";
			sql += "             ,\"FacmNo\" ";
			sql += "             ,MAX(\"PrevIntDate\") \"PrevIntDate\" ";
			sql += "       FROM \"MonthlyFacBal\" ";
			sql += "       GROUP BY \"CustNo\" ";
			sql += "               ,\"FacmNo\") MDate ON MDate.\"CustNo\" = M.\"CustNo\" ";
			sql += "                               AND MDate.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "  LEFT JOIN \"CustMain\" CUS ON CUS.\"CustNo\" = M.\"CustNo\" ";
			sql += "  LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\" ";
			sql += "                   AND FAC.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "  LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = M.\"CustNo\" ";
			sql += "                             AND LBM.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "                             AND LBM.\"BormNo\" = FAC.\"LastBormNo\" ";
			sql += "  LEFT JOIN \"CollList\" COL ON COL.\"CustNo\" = M.\"CustNo\" ";
			sql += "                    AND COL.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "  LEFT JOIN \"CdEmp\" EMP ON EMP.\"EmployeeNo\" = COL.\"AccCollPsn\" ";
			sql += "  LEFT JOIN ( SELECT \"CustNo\" ";
			sql += "              ,\"FacmNo\" ";
			sql += "              ,\"LegalProg\" ";
			sql += "              ,\"RecordDate\" ";
			sql += "              ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\" ";
			sql += "                                  ORDER BY \"RecordDate\" ASC ";
			sql += "                                          ,\"CreateDate\" ASC ";
			sql += "      ) AS \"LawRowNo\" ";
			sql += "  FROM \"CollLaw\" ";
			sql += "      ) LAW ON LAW.\"CustNo\"  = M.\"CustNo\" ";
			sql += "   AND LAW.\"FacmNo\"  = M.\"FacmNo\" ";
			sql += "   AND LAW.\"LawRowNo\" = 1 ";
			sql += "  LEFT JOIN \"CdCode\" CDL ON CDL.\"DefCode\" = 'LegalProg' ";
			sql += "    AND CDL.\"Code\" = LAW.\"LegalProg\" ";
			sql += "  LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = M.\"CityCode\" ";
			sql += "  LEFT JOIN (SELECT lbtx.\"CustNo\" ";
			sql += "       ,DECODE(lbtx.\"FacmNo\", 0, MGroup.\"FacmNo\", lbtx.\"FacmNo\") \"FacmNo\" ";
			sql += "       ,MAX(lbtx.\"IntEndDate\")  AS \"IntEndDate\" ";
			sql += "       ,SUM(lbtx.\"TxAmt\") AS \"OvTxAmt\" ";
			sql += "       ,MAX(lbtx.\"EntryDate\") AS \"OvEntryDate\" ";
			 // 不計額度為0的暫收款
			sql += "       ,SUM(lbtx.\"Principal\" + lbtx.\"Interest\" + lbtx.\"DelayInt\" + lbtx.\"BreachAmt\" + DECODE(lbtx.\"FacmNo\", 0, 0, lbtx.\"TempAmt\")) AS \"LnTxAmt\" ";
			sql += "       ,MAX(CASE WHEN (lbtx.\"Principal\" + lbtx.\"Interest\" +  lbtx.\"DelayInt\" + lbtx.\"BreachAmt\" + DECODE(lbtx.\"FacmNo\", 0, 0, lbtx.\"TempAmt\")) > 0 ";
			sql += "                 THEN lbtx.\"EntryDate\" ";
			sql += "                 ELSE 0 ";
			sql += "            END) AS \"LnEntryDate\" ";
			sql += "       ,SUM(CASE WHEN lbtx.\"TitaTxCd\" IN ('L3210', 'L3220', 'L3230') AND lbtx.\"FacmNo\" = 0 "; // 僅蒐集暫收款交易且額度為0者
			sql += "                 THEN lbtx.\"TempAmt\" ";
			sql += "            ELSE 0 END) \"TempTotal\" ";
			sql += " FROM \"LoanBorTx\"  lbtx ";
			sql += "  LEFT JOIN (SELECT \"YearMonth\" ";
			sql += "                   ,\"CustNo\" ";
			sql += "                   ,MIN(\"FacmNo\") \"FacmNo\" ";
			sql += "             FROM \"MonthlyFacBal\" ";
			sql += "             WHERE \"YearMonth\" = TO_CHAR(ADD_MONTHS(TO_DATE(:inputYearMonth, 'YYYYMM'), -1), 'YYYYMM') ";
			sql += "             GROUP BY \"YearMonth\" ";
			sql += "                     ,\"CustNo\" ) MGroup ON SUBSTR(lbtx.\"EntryDate\", 1, 6) - MGroup.\"YearMonth\" IN (1,89) "; // 入帳日期在YearMonth後一個月
			sql += "                                       AND MGroup.\"CustNo\" = lbtx.\"CustNo\" ";
			sql += " WHERE lbtx.\"TitaHCode\" = '0' ";
			sql += "   AND lbtx.\"EntryDate\" >= :inputEntryDateMin ";
			sql += "   AND lbtx.\"EntryDate\" <= :inputEntryDateMax ";
			sql += " GROUP BY lbtx.\"CustNo\", DECODE(lbtx.\"FacmNo\", 0, MGroup.\"FacmNo\", lbtx.\"FacmNo\") ";
			sql += "      ) TX ON TX.\"CustNo\"  = M.\"CustNo\" ";
			sql += "    AND (TX.\"FacmNo\" = 0 OR TX.\"FacmNo\" = M.\"FacmNo\") ";
			sql += "  WHERE M.\"YearMonth\" = TO_CHAR(ADD_MONTHS(TO_DATE(:inputYearMonth, 'YYYYMM'), -1), 'YYYYMM') ";
			sql += "    AND (NVL(:inputCollector, ' ') = ' ' OR NVL(M.\"AccCollPsn\",'X') = :inputCollector) ";
			sql += "    AND FAC.\"FirstDrawdownDate\" >= :inputDrawdownDate ";
			sql += "    AND :findOvdu = 'Y' ";
			sql += "    AND M.\"AcctCode\" = '990' ";
			sql += "    AND M.\"PrinBalance\" > 0 ";
			sql += "  ORDER BY M.\"CustNo\" ";
			sql += "          ,M.\"FacmNo\" ";

			break;
		case AcctOthers:
			sql += " SELECT ";
			sql += "   EMP.\"Fullname\" AS \"EmpName\" ";
			sql += "  ,TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(:inputYearMonth, 'YYYYMM'), -1), 'YYYYMM'), 5 ,2)) AS \"RocMonth\" ";
			sql += "  ,:inputEntryDateMin - 19110000 AS \"RocEntryDateMin\" ";
			sql += "  ,:inputEntryDateMax - 19110000 AS \"RocEntryDateMax\" ";
			sql += "  ,M.\"OvduTerm\" AS \"OvduTerm\" ";
			sql += "  ,M.\"CustNo\" AS \"CustNo\" ";
			sql += "  ,M.\"FacmNo\" AS \"FacmNo\" ";
			sql += "  ,CUS.\"CustName\" AS \"CustName\" ";
			sql += "  ,CASE WHEN M.\"EntCode\" = '1' ";
			sql += "        THEN '企金' ";
			sql += "        ELSE ' ' ";
			sql += "   END AS \"IsEnt\" ";
			sql += "  ,CASE WHEN M.\"AcctCode\" = '990' ";
			sql += "        THEN M.\"OvduBal\" ";
			sql += "        ELSE M.\"UnpaidPrincipal\" ";
			sql += "           + M.\"UnpaidInterest\" ";
			sql += "           + M.\"UnpaidBreachAmt\" ";
			sql += "           + M.\"UnpaidDelayInt\" ";
			sql += "           - M.\"TempAmt\" ";
			sql += "   END AS \"OvduBal\" "; // 非990(逾期)時, 用額度月報的欄位算
			sql += "  ,GREATEST(MDate.\"PrevIntDate\" - 19110000, 0) AS \"rocPrevIntDate\" ";
			sql += "  ,CASE WHEN M.\"AcctCode\" = '990' ";
			sql += "        THEN NVL(TO_CHAR(TX.\"OvTxAmt\"), ' ') ";
			sql += "   ELSE NVL(TO_CHAR(TX.\"LnTxAmt\"), ' ') END  AS \"TxAmt\" ";
			sql += "  ,NVL(TO_CHAR(GREATEST((CASE WHEN M.\"AcctCode\" = '990' ";
			sql += "                              THEN TX.\"OvEntryDate\" ";
			sql += "                         ELSE      TX.\"LnEntryDate\" ";
			sql += "                         END) - 19110000, 0)), ' ') AS \"EntryDate\" ";
			sql += "  ,0 AS \"IntEndDate\" "; // 計息迄日, 樣張為0 
			sql += "  ,0 AS \"RetrievedRatio\" "; // done in report
			sql += "  ,CASE WHEN NVL(COL.\"PrinBalance\",1) = 0 ";
			sql += "        THEN '結案' ";
			sql += "        WHEN LBM.\"Status\" = 7 AND M.\"AcctCode\" = '990' ";
			sql += "        THEN '部份轉呆' ";
			sql += "        WHEN LBM.\"Status\" = 0 AND M.\"AcctCode\" = '990' ";
			sql += "        THEN '催收轉正常' ";
			sql += "        WHEN LBM.\"Status\" = 6 AND M.\"AcctCode\" = '990' ";
			sql += "        THEN '轉呆帳' ";
			sql += "        WHEN LBM.\"Status\" = 5 AND M.\"AcctCode\" = '990' ";
			sql += "        THEN '催收結案' ";
			sql += "        ELSE ' ' ";
			sql += "   END AS \"IsClosed\" ";
			sql += "  ,NVL(LAW.\"LegalProg\", 0) AS \"LegalProgCode\" ";
			sql += "  ,NVL(CDL.\"Item\", ' ') AS \"LegalProgItem\" ";
			sql += "  ,GREATEST(NVL(LAW.\"RecordDate\", 0) - 19110000, 0) AS \"rocRecordDate\" ";
			sql += "  ,NVL(CITY.\"CityItem\", ' ') AS \"CityItem\" ";
			sql += "  FROM \"MonthlyFacBal\" M ";
			sql += "  LEFT JOIN (SELECT \"CustNo\" ";
			sql += "             ,\"FacmNo\" ";
			sql += "             ,MAX(\"PrevIntDate\") \"PrevIntDate\" ";
			sql += "       FROM \"MonthlyFacBal\" ";
			sql += "       GROUP BY \"CustNo\" ";
			sql += "               ,\"FacmNo\") MDate ON MDate.\"CustNo\" = M.\"CustNo\" ";
			sql += "                               AND MDate.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "  LEFT JOIN \"CustMain\" CUS ON CUS.\"CustNo\" = M.\"CustNo\" ";
			sql += "  LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\" ";
			sql += "                   AND FAC.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "  LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = M.\"CustNo\" ";
			sql += "                             AND LBM.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "                             AND LBM.\"BormNo\" = FAC.\"LastBormNo\" ";
			sql += "  LEFT JOIN \"CdEmp\" EMP ON EMP.\"EmployeeNo\" = M.\"AccCollPsn\" ";
			sql += "  LEFT JOIN \"CollList\" COL ON COL.\"CustNo\" = M.\"CustNo\" ";
			sql += "                    AND COL.\"FacmNo\" = M.\"FacmNo\" ";
			sql += "  LEFT JOIN ( SELECT \"CustNo\" ";
			sql += "              ,\"FacmNo\" ";
			sql += "              ,\"LegalProg\" ";
			sql += "              ,\"RecordDate\" ";
			sql += "              ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\" ";
			sql += "                                  ORDER BY \"RecordDate\" ASC ";
			sql += "                                          ,\"CreateDate\" ASC ";
			sql += "      ) AS \"LawRowNo\" ";
			sql += "  FROM \"CollLaw\" ";
			sql += "      ) LAW ON LAW.\"CustNo\"  = M.\"CustNo\" ";
			sql += "   AND LAW.\"FacmNo\"  = M.\"FacmNo\" ";
			sql += "   AND LAW.\"LawRowNo\" = 1 ";
			sql += "  LEFT JOIN \"CdCode\" CDL ON CDL.\"DefCode\" = 'LegalProg' ";
			sql += "    AND CDL.\"Code\" = LAW.\"LegalProg\" ";
			sql += "  LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = M.\"CityCode\" ";
			sql += "  LEFT JOIN (SELECT lbtx.\"CustNo\" ";
			sql += "       ,DECODE(lbtx.\"FacmNo\", 0, MGroup.\"FacmNo\", lbtx.\"FacmNo\") \"FacmNo\" ";
			sql += "       ,MAX(lbtx.\"IntEndDate\")  AS \"IntEndDate\" ";
			sql += "       ,SUM(lbtx.\"TxAmt\") AS \"OvTxAmt\" ";
			sql += "       ,MAX(lbtx.\"EntryDate\") AS \"OvEntryDate\" ";
			sql += "       ,SUM(lbtx.\"Principal\" + lbtx.\"Interest\" + lbtx.\"DelayInt\" + lbtx.\"BreachAmt\" + DECODE(lbtx.\"FacmNo\", 0, 0, lbtx.\"TempAmt\")) AS \"LnTxAmt\" "; // 額度為0的暫收款在這裡不計
			sql += "       ,MAX(CASE WHEN (lbtx.\"Principal\" + lbtx.\"Interest\" +  lbtx.\"DelayInt\" + lbtx.\"BreachAmt\" + DECODE(lbtx.\"FacmNo\", 0, 0, lbtx.\"TempAmt\")) > 0 ";
			sql += "                 THEN lbtx.\"EntryDate\" ";
			sql += "                 ELSE 0 ";
			sql += "            END) AS \"LnEntryDate\" ";
			sql += "       ,SUM(CASE WHEN lbtx.\"TitaTxCd\" IN ('L3210', 'L3220', 'L3230') AND lbtx.\"FacmNo\" = 0 "; // 僅蒐集暫收款交易且額度為0者
			sql += "                 THEN lbtx.\"TempAmt\" ";
			sql += "            ELSE 0 END) \"TempTotal\" ";
			sql += " FROM \"LoanBorTx\"  lbtx ";
			sql += "  LEFT JOIN (SELECT \"YearMonth\" ";
			sql += "                   ,\"CustNo\" ";
			sql += "                   ,MIN(\"FacmNo\") \"FacmNo\" ";
			sql += "             FROM \"MonthlyFacBal\" ";
			sql += "             WHERE \"YearMonth\" = TO_CHAR(ADD_MONTHS(TO_DATE(:inputYearMonth, 'YYYYMM'), -1), 'YYYYMM') ";
			sql += "               AND \"PrinBalance\" > 0 ";
			sql += "               AND \"PrevIntDate\" > 0 ";
			sql += "               AND \"NextIntDate\" > 0 "; // 額度月報轉換時, 如從未繳款, 這兩個日期會為0; 因為這裡是找出繳納暫收款可能歸屬的額度, 在這裡排除從未繳款的額度
			sql += "             GROUP BY \"YearMonth\" ";
			sql += "                     ,\"CustNo\" ) MGroup ON SUBSTR(lbtx.\"EntryDate\", 1, 6) - MGroup.\"YearMonth\" IN (1,89) "; // 入帳日期在YearMonth後一個月
			sql += "                                       AND MGroup.\"CustNo\" = lbtx.\"CustNo\" ";
			sql += " WHERE lbtx.\"TitaHCode\" = '0' ";
			sql += "   AND lbtx.\"EntryDate\" >= :inputEntryDateMin ";
			sql += "   AND lbtx.\"EntryDate\" <= :inputEntryDateMax ";
			sql += " GROUP BY lbtx.\"CustNo\", DECODE(lbtx.\"FacmNo\", 0, MGroup.\"FacmNo\", lbtx.\"FacmNo\") ";
			sql += "      ) TX ON TX.\"CustNo\"  = M.\"CustNo\" ";
			sql += "    AND (TX.\"FacmNo\" = 0 OR TX.\"FacmNo\" = M.\"FacmNo\") ";
			sql += "  WHERE M.\"YearMonth\" = TO_CHAR(ADD_MONTHS(TO_DATE(:inputYearMonth, 'YYYYMM'), -1), 'YYYYMM') ";
			sql += "    AND (NVL(:inputCollector, ' ') = ' ' OR NVL(M.\"AccCollPsn\",'X') = :inputCollector) ";
			sql += "    AND FAC.\"FirstDrawdownDate\" >= :inputDrawdownDate ";
			sql += "    AND :findOvdu = 'N' ";
			sql += "    AND M.\"OvduDays\" > 0 ";
			sql += "    AND M.\"PrinBalance\" > 0 ";
			sql += "  ORDER BY M.\"CustNo\" ";
			sql += "    ,M.\"FacmNo\" ";

			break;
		default:
			return null;
		}

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		LocalDate lastYearMonth = LocalDate.of(
				Integer.parseInt(titaVo.getParam("inputYearMonth").substring(0, 3)) + 1911,
				Integer.parseInt(titaVo.getParam("inputYearMonth").substring(3)), 1);
		lastYearMonth = lastYearMonth.minusMonths(1);

		this.info("inputEntryDateMin="
				+ Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMin")) + 19110000));
		this.info("inputEntryDateMax="
				+ Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMax")) + 19110000));
		this.info("inputYearMonth=" + Integer.toString(Integer.parseInt(titaVo.getParam("inputYearMonth")) + 191100));
		this.info("inputCollectorShow=" + titaVo.getParam("inputCollector"));
		this.info("inputDrawdownDate="
				+ Integer.toString(Integer.parseInt(titaVo.getParam("inputDrawdownDate")) + 19110000));

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputEntryDateMin",
				Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMin")) + 19110000));
		query.setParameter("inputEntryDateMax",
				Integer.toString(Integer.parseInt(titaVo.getParam("inputEntryDateMax")) + 19110000));
		query.setParameter("inputYearMonth",
				Integer.toString(Integer.parseInt(titaVo.getParam("inputYearMonth")) + 191100)); // 實際出表資料應為前一個月 月份推移在Query中做
		query.setParameter("inputCollector", titaVo.getParam("inputCollector"));
		query.setParameter("inputDrawdownDate",
				Integer.toString(Integer.parseInt(titaVo.getParam("inputDrawdownDate")) + 19110000));
		query.setParameter("findOvdu", reportType == ReportType.Acct990 ? "Y" : "N");

		return this.convertToMap(query);
	}

}