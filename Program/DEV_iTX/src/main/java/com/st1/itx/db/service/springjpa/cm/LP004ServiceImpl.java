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

@Service
@Repository
public class LP004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}
	//工作月
	public List<Map<String, String>> wkSsn(TitaVo titaVo) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
		this.info("lW001.wkSsn ENTDY=" + iENTDY);

		// 總表 f0:本日之工作年,f1:本日之工作月

		String sql = " ";
		sql += " SELECT W.\"Year\"  AS F0 ";
		sql += "      , W.\"Month\" AS F1 ";
		sql += " FROM \"CdWorkMonth\" W ";
		sql += " WHERE W.\"StartDate\" <= :iday ";
		sql += "   AND W.\"EndDate\" >= :iday ";
		this.info("sql=" + sql);
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", iENTDY);
		return this.convertToMap(query);
	}
	//放款審查
	public List<Map<String, String>> findArea(TitaVo titaVo, Map<String, String> wkVo) throws Exception {


		int inputYear = Integer.parseInt(wkVo.get("F0"));

		int iMM = Integer.parseInt(wkVo.get("F1"));

		int inputYearMonth = (inputYear * 100) + iMM;

		if (iMM == 1) {
			inputYear = inputYear - 1;
			iMM = 13;
		} else {
			iMM = iMM - 1;
		}

		int lastYearMonth = inputYear * 100 + iMM;

		this.info("LP004ServiceImpl.findArea thisYearMonth = " + inputYearMonth + " ,lastYearMonth = " + lastYearMonth);

		String sql = " ";
		sql += " SELECT CASE ";
		sql += " 		  WHEN tab1.\"UnitCode\" = '10HC00' THEN '放款審查課-北區' ";
		sql += " 		  WHEN tab1.\"UnitCode\" = '10HJ00' THEN '放款審查課-中區' ";
		sql += " 		  WHEN tab1.\"UnitCode\" = '10HL00' THEN '放款審查課-南部' ";
		sql += "		END AS \"UnitItem\"";
		sql += "      , tab1.\"Fullname\" ";
		sql += "      , CASE WHEN tab2.\"PerfCnt\" >= 0 THEN tab2.\"PerfCnt\" ELSE 0 END AS \"lastPerfCnt\" ";
		sql += "      , CASE WHEN tab2.\"PerfAmt\" >= 0 THEN tab2.\"PerfAmt\" ELSE 0 END AS \"lastPerfAmt\" ";
		sql += "      , CASE WHEN tab3.\"PerfCnt\" >= 0 THEN tab3.\"PerfCnt\" ELSE 0 END AS \"thisPerfCnt\" ";
		sql += "      , CASE WHEN tab3.\"PerfAmt\" >= 0 THEN tab3.\"PerfAmt\" ELSE 0 END AS \"thisPerfAmt\" ";
		sql += " FROM ( SELECT CB.\"UnitCode\" ";
		sql += "             , CB.\"UnitItem\" ";
		sql += "             , CB.\"UnitManager\" ";
		sql += "             , CE.\"Fullname\" ";
		sql += "        FROM \"CdBcm\" CB";
		sql += "        LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\"=CB.\"UnitManager\" ";
		sql += "        WHERE \"UnitCode\" IN('10HC00','10HJ00','10HL00') ";
		sql += "      ) tab1 ";
		sql += " LEFT JOIN ( SELECT \"unitArea\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "             FROM ( SELECT \"PerfCnt\" ";
		sql += "                         , \"PerfAmt\" ";
		sql += "                         , PO.\"AreaCode\" AS \"unitArea\" ";
		sql += "                    FROM \"PfBsDetail\" PD";
		sql += "                    LEFT JOIN \"PfBsOfficer\" PO ON PO.\"EmpNo\" = PD.\"BsOfficer\" ";
		sql += "                                                AND PO.\"WorkMonth\" = PD.\"WorkMonth\" ";
		sql += "                    WHERE NVL(PO.\"AreaCode\",' ') IN ('10HC00','10HJ00','10HL00') ";
		sql += "                      AND PD.\"WorkMonth\"= :liyymm";
		sql += "                  ) ";
		sql += "             GROUP BY \"unitArea\" ";
		sql += "           ) tab2 ON tab2.\"unitArea\" = tab1.\"UnitCode\" ";
		sql += " LEFT JOIN ( SELECT \"unitArea\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "             FROM ( SELECT \"PerfCnt\" ";
		sql += "                         , \"PerfAmt\" ";
		sql += "                         , PO.\"AreaCode\" AS \"unitArea\" ";
		sql += "                    FROM \"PfBsDetail\" PD";
		sql += "                    LEFT JOIN \"PfBsOfficer\" PO ON PO.\"EmpNo\" = PD.\"BsOfficer\" ";
		sql += "                                                AND PO.\"WorkMonth\" = PD.\"WorkMonth\" ";
		sql += "                    WHERE NVL(PO.\"AreaCode\",' ') IN ('10HC00','10HJ00','10HL00') ";
		sql += "                      AND PD.\"WorkMonth\"= :iyymm ";
		sql += "                  ) ";
		sql += "             GROUP BY \"unitArea\" ";
		sql += "           ) tab3 ON tab3.\"unitArea\"=tab1.\"UnitCode\" ";
		sql += " ORDER BY tab1.\"UnitCode\" ASC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", inputYearMonth);
		query.setParameter("liyymm", lastYearMonth);

		return this.convertToMap(query);
	}

	//房貸部專
	public List<Map<String, String>> setDeptSpecialist(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		int inputYear = Integer.parseInt(wkVo.get("F0"));

		int iMM = Integer.parseInt(wkVo.get("F1"));

		int inputYearMonth = (inputYear * 100) + iMM;

		this.info("LP004ServiceImpl.findAllDept inputYear =" + inputYear);
		this.info("LP004ServiceImpl.findAllDept inputYearMonth =" + inputYearMonth);

		String sql = " ";
		sql += " WITH \"DeptTarget\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"WorkMonth\" ";
		sql += "          , SUM(\"GoalAmt\") AS \"GoalAmt\" ";
		sql += "     FROM \"PfBsOfficer\" ";
		sql += "     WHERE TRUNC(\"WorkMonth\" / 100) = :inputYear ";
		sql += "       AND \"WorkMonth\" <= :inputYearMonth ";
		sql += "       AND \"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000') ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"WorkMonth\" ";
		sql += " ) ";
		sql += " , \"DeptEmpName\" AS ( ";
		sql += "      SELECT \"DeptCode\" ";
		sql += "           , \"Fullname\" ";
		sql += "           , \"EmpNo\" ";
		sql += "      FROM \"PfBsOfficer\" ";
		sql += "      WHERE \"WorkMonth\" = :inputYearMonth ";
		sql += "        AND \"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000') ";
		sql += "        AND \"DistItem\" = '房貸部專' ";
		sql += " ) ";
		sql += " , \"DeptTargetSummary\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , SUM(\"GoalAmt\") AS \"GoalAmtTotal\" ";
		sql += "     FROM \"DeptTarget\" ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += " ) ";
		sql += " , \"DeptPf\" AS ( ";
		sql += "     SELECT O.\"DeptCode\" ";
		sql += "          , O.\"WorkMonth\" ";
		sql += "          , D.\"CustNo\" ";
		sql += "          , D.\"FacmNo\" ";
		sql += "          , D.\"BormNo\" ";
		sql += "          , D.\"PerfDate\" ";
		sql += "          , D.\"PerfCnt\" ";
		sql += "          , D.\"PerfAmt\" ";
		sql += "     FROM \"PfBsOfficer\" O ";
		sql += "     LEFT JOIN \"PfBsDetail\" D ON D.\"BsOfficer\" = O.\"EmpNo\" ";
		sql += "                             AND D.\"WorkMonth\" = O.\"WorkMonth\" ";
		sql += "     WHERE TRUNC(O.\"WorkMonth\" / 100) = :inputYear ";
		sql += "       AND O.\"WorkMonth\" <= :inputYearMonth ";
		sql += "       AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000') ";
		sql += "       AND D.\"PerfAmt\" >= 0 ";
		sql += " ) ";
		sql += " , \"DeptPfSummary\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , SUM(\"PerfAmt\") AS \"PerfAmtTotal\" ";
		sql += "     FROM \"DeptPf\" ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += " ) ";
		sql += " , \"DeptPfMonthlySummary\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"WorkMonth\" ";
		sql += "          , SUM(\"PerfCnt\") AS \"PerfCntMonthlyTotal\" ";
		sql += "          , SUM(\"PerfAmt\") AS \"PerfAmtMonthlyTotal\" ";
		sql += "     FROM \"DeptPf\" ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"WorkMonth\" ";
		sql += " ) ";
		sql += " SELECT DENSE_RANK() OVER (ORDER BY ROUND(NVL(DPS.\"PerfAmtTotal\",0) / DTS.\"GoalAmtTotal\",4) * 100 DESC ";
		sql += "                                  , DT.\"DeptCode\" ";
		sql += "                          ) AS \"SEQ\" ";// F0
		sql += "	  , CASE ";
		sql += "          WHEN DPMS.\"DeptCode\"='A0B000' THEN '台北'";
		sql += "          WHEN DPMS.\"DeptCode\"='A0F000' THEN '台北'";
		sql += "          WHEN DPMS.\"DeptCode\"='A0E000' THEN '台中'";
		sql += "          WHEN DPMS.\"DeptCode\"='A0M000' THEN '高雄'";
		sql += "        END     AS \"DeptCity\" ";// F1
		sql += "      , DE.\"Fullname\" "; // F2
		sql += "      , DE.\"EmpNo\" "; // F3
		sql += "      , DT.\"GoalAmt\" "; // F4
		sql += "      , NVL(DPMS.\"PerfAmtMonthlyTotal\",0) AS \"PerfAmtMonthlyTotal\" "; // F5
		sql += "      , NVL(DPMS.\"PerfCntMonthlyTotal\",0) AS \"PerfCntMonthlyTotal\" "; // F6
		sql += "      , SUBSTR(B.\"UnitItem\",1,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",3,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",5,1) AS \"UnitItem\" "; // F7
		sql += " FROM \"DeptTarget\" DT ";
		sql += " LEFT JOIN \"DeptEmpName\" DE ON DE.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptTargetSummary\" DTS ON DTS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptPfSummary\" DPS ON DPS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptPfMonthlySummary\" DPMS ON DPMS.\"DeptCode\" = Dt.\"DeptCode\" ";
		sql += "                                      AND DPMS.\"WorkMonth\" = DT.\"WorkMonth\" ";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = DT.\"DeptCode\" ";
		sql += " WHERE DT.\"WorkMonth\" = :inputYearMonth";
		sql += " ORDER BY \"SEQ\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYear", inputYear);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);

	}

	//房貸專員
	public List<Map<String, String>> findPfBsOfficer(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		int inputYear = Integer.parseInt(wkVo.get("F0"));

		int iMM = Integer.parseInt(wkVo.get("F1"));

		int inputYearMonth = (inputYear * 100) + iMM;

		this.info("LP004ServiceImpl.findDist inputYear =" + inputYear);
		this.info("LP004ServiceImpl.findDist inputYearMonth =" + inputYearMonth);

		String sql = " ";
		sql += " WITH \"DistTarget\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"EmpNo\" ";
		sql += "          , \"WorkMonth\" ";
		sql += "          , SUM(\"GoalAmt\") AS \"GoalAmt\" ";
		sql += "     FROM \"PfBsOfficer\" ";
		sql += "     WHERE TRUNC(\"WorkMonth\" / 100) = :inputYear ";
		sql += "       AND \"WorkMonth\" <= :inputYearMonth ";
		sql += "       AND \"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000') ";
		sql += "       AND NVL(\"DistItem\",' ') != '房貸部專'  ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"EmpNo\" ";
		sql += "            , \"WorkMonth\" ";
		sql += " ) ";
		sql += " , \"DistTargetSummary\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"EmpNo\" ";
		sql += "          , SUM(\"GoalAmt\") AS \"GoalAmtTotal\" ";
		sql += "     FROM \"DistTarget\" ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"EmpNo\" ";
		sql += " ) ";
		sql += " , \"DistPf\" AS ( ";
		sql += "     SELECT O.\"DeptCode\" ";
		sql += "          , O.\"EmpNo\" ";
		sql += "          , O.\"WorkMonth\" ";
		sql += "          , D.\"CustNo\" ";
		sql += "          , D.\"FacmNo\" ";
		sql += "          , D.\"BormNo\" ";
		sql += "          , D.\"PerfDate\" ";
		sql += "          , D.\"PerfCnt\" ";
		sql += "          , D.\"PerfAmt\" ";
		sql += "     FROM \"PfBsOfficer\" O ";
		sql += "     LEFT JOIN \"PfBsDetail\" D ON D.\"BsOfficer\" = O.\"EmpNo\" ";
		sql += "                             AND D.\"WorkMonth\" = O.\"WorkMonth\" ";
		sql += "     WHERE TRUNC(O.\"WorkMonth\" / 100) = :inputYear ";
		sql += "       AND O.\"WorkMonth\" <= :inputYearMonth ";
		sql += "       AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000') ";
		sql += "       AND NVL(O.\"DistItem\",' ') != '房貸部專'  ";
		sql += "       AND D.\"PerfAmt\" >= 0 ";
		sql += " ) ";
		sql += " , \"DistPfSummary\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"EmpNo\" ";
		sql += "          , SUM(\"PerfAmt\") AS \"PerfAmtTotal\" ";
		sql += "     FROM \"DistPf\" ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"EmpNo\" ";
		sql += " ) ";
		sql += " , \"DistPfMonthlySummary\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"EmpNo\" ";
		sql += "          , \"WorkMonth\" ";
		sql += "          , SUM(\"PerfCnt\") AS \"PerfCntMonthlyTotal\" ";
		sql += "          , SUM(\"PerfAmt\") AS \"PerfAmtMonthlyTotal\" ";
		sql += "     FROM \"DistPf\" ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"EmpNo\" ";
		sql += "            , \"WorkMonth\" ";
		sql += " ) ";
		sql += " SELECT DENSE_RANK() OVER (ORDER BY ROUND(NVL(DPS.\"PerfAmtTotal\",0) / DTS.\"GoalAmtTotal\",4) * 100 DESC ";
		sql += "                                  , DT.\"DeptCode\" ";
		sql += "                                  , PBO.\"DistItem\" ";
		sql += "                          ) AS \"SEQ\" ";// F0
		sql += "      , PBO.\"Fullname\" ";// F1
		sql += "      , DT.\"EmpNo\" ";// F2
		sql += "      , DT.\"GoalAmt\" ";// F3
		sql += "      , NVL(DP.\"PerfAmtMonthlyTotal\",0) AS \"PerfAmtMonthlyTotal\" ";// F4
		sql += "      , NVL(DP.\"PerfCntMonthlyTotal\",0) AS \"PerfCntMonthlyTotal\" ";// F5
		sql += "      , SUBSTR(B.\"UnitItem\",1,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",3,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",5,1) AS \"UnitItem\" ";// F6
		sql += "      , PBO.\"DistItem\" ";// F7
		sql += " FROM \"DistTarget\" DT ";
		sql += " LEFT JOIN \"DistTargetSummary\" DTS ON DTS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += "                                  AND DTS.\"EmpNo\" = DT.\"EmpNo\" ";
		sql += " LEFT JOIN \"DistPfSummary\" DPS ON DPS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += "                              AND DPS.\"EmpNo\" = Dt.\"EmpNo\" ";
		sql += " LEFT JOIN \"DistPfMonthlySummary\" DP ON DP.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += "                                    AND DP.\"EmpNo\" = DT.\"EmpNo\" ";
		sql += "                                    AND DP.\"WorkMonth\" = DT.\"WorkMonth\" ";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"PfBsOfficer\" PBO ON PBO.\"WorkMonth\" = DT.\"WorkMonth\" ";
		sql += "                            AND PBO.\"EmpNo\" = DT.\"EmpNo\" ";
		sql += " WHERE DT.\"WorkMonth\" = :inputYearMonth";
		sql += " ORDER BY \"SEQ\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYear", inputYear);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

	// 部室
	public List<Map<String, String>> findDept(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		int inputYear = Integer.parseInt(wkVo.get("F0"));

		int iMM = Integer.parseInt(wkVo.get("F1"));

		int inputYearMonth = (inputYear * 100) + iMM;

		// 季初的工作月
		String iWKs = "";
		// 季末的工作月
		String iWKe = "";
		if (iMM < 4) {
			iWKs = inputYear + "01";
			iWKe = inputYear + String.format("%02d", iMM);
		} else if (iMM < 7) {
			iWKs = inputYear + "04";
			iWKe = inputYear + String.format("%02d", iMM);
		} else if (iMM < 10) {
			iWKs = inputYear + "07";
			iWKe = inputYear + String.format("%02d", iMM);
		} else {
			iWKs = inputYear + "10";
			iWKe = inputYear + String.format("%02d", iMM);
		}

		this.info("LP004.findDept iYYMM=" + inputYearMonth + ",iWKSSN=" + iWKs + "~" + iWKe);

		String sql = " ";
		sql += " SELECT DENSE_RANK() OVER (ORDER BY tab2.\"sumPerfAmt\" DESC ) AS \"SEQ\" ";// F0
		sql += "      , tab1.\"UnitItem\" ";//F1
		sql += "      , tab1.\"DeptName\" ";//F2
		sql += "      , tab1.\"BsName\" ";//F3
		sql += "      , tab2.\"t2sumPerfCnt\" ";//F4
		sql += "      , tab2.\"t2sumPerfAmt\" ";//F5
		sql += "      , tab3.\"t3sumPerfCnt\" ";//F6
		sql += "      , tab3.\"t3sumPerfAmt\" ";//F7
		sql += " FROM ( SELECT DISTINCT ";
		sql += "               CB.\"DeptManager\" ";
		sql += "             , CE.\"Fullname\" AS \"DeptName\" ";
		sql += "             , PB.\"Fullname\" AS \"BsName\" ";
		sql += "      		 , SUBSTR(CB.\"DeptItem\",1,1) ";
		sql += "            || SUBSTR(CB.\"DeptItem\",3,1) ";
		sql += "        	|| SUBSTR(CB.\"DeptItem\",5,1) AS \"UnitItem\" ";
		sql += "             , CB.\"DeptCode\" ";
		sql += "        FROM \"PfBsOfficer\" PB ";
		sql += "        LEFT JOIN \"CdBcm\" CB ON CB.\"DeptCode\" = PB.\"DeptCode\" ";
		sql += "        LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = CB.\"DeptManager\" ";
		sql += "        WHERE PB.\"WorkMonth\" = :iyymm ";
		sql += "          AND NVL(PB.\"DistItem\",' ') = '房貸部專' ";
		sql += "      ) tab1 ";
		sql += " LEFT JOIN ( SELECT \"DeptCode\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"t2sumPerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"t2sumPerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             WHERE \"WorkMonth\"= :iyymm ";
		sql += "               AND \"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "             GROUP BY \"DeptCode\" ";
		sql += "           ) tab2 ON tab1.\"DeptCode\" = tab2.\"DeptCode\" ";
		sql += " LEFT JOIN ( SELECT \"DeptCode\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"t3sumPerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"t3sumPerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             WHERE \"WorkMonth\">= :iWKs ";
		sql += "               AND \"WorkMonth\"<= :iWKe ";
		sql += "               AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000') ";
		sql += "             GROUP BY \"DeptCode\" ";
		sql += "           ) tab3 ON tab1.\"DeptCode\" = tab3.\"DeptCode\" ";
		sql += " ORDER BY \"SEQ\" ASC ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", inputYearMonth);
		query.setParameter("iWKs", iWKs);
		query.setParameter("iWKe", iWKe);

		return this.convertToMap(query);

	}

	// 區部
	public List<Map<String, String>> findDist(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		int inputYear = Integer.parseInt(wkVo.get("F0"));

		int iMM = Integer.parseInt(wkVo.get("F1"));

		int inputYearMonth = (inputYear * 100) + iMM;

		// 季初的工作月
		String iWKs = "";
		// 季末的工作月
		String iWKe = "";
		if (iMM < 4) {
			iWKs = inputYear + "01";
			iWKe = inputYear + String.format("%02d", iMM);
		} else if (iMM < 7) {
			iWKs = inputYear + "04";
			iWKe = inputYear + String.format("%02d", iMM);
		} else if (iMM < 10) {
			iWKs = inputYear + "07";
			iWKe = inputYear + String.format("%02d", iMM);
		} else {
			iWKs = inputYear + "10";
			iWKe = inputYear + String.format("%02d", iMM);
		}

		this.info("LP004.findAsDept iYYMM=" + inputYearMonth + ",iWKSSN=" + iWKs + "~" + iWKe);

		String sql = " ";
		sql += " SELECT CASE ";
		sql += "          WHEN tab1.\"DeptCode\" = 'A0B000' ";
		sql += "          THEN '營管部' ";
		sql += "          WHEN tab1.\"DeptCode\" = 'A0F000' ";
		sql += "          THEN '營推部' ";
		sql += "          WHEN tab1.\"DeptCode\" = 'A0E000' ";
		sql += "          THEN '業推部' ";
		sql += "          WHEN tab1.\"DeptCode\" = 'A0M000' ";
		sql += "          THEN '業開部' ";
		sql += "          WHEN tab1.\"DeptCode\" = 'A0X000' ";
		sql += "          THEN '專業行銷課' ";
		sql += "        ELSE null END            AS \"DeptName\" ";
		sql += "      , tab1.\"DistItem\" ";
		sql += "      , tab1.\"Fullname\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab2.\"sumPerfCnt\" > 0 ";
		sql += "          THEN tab2.\"sumPerfCnt\" ";
		sql += "        ELSE 0 END               AS \"wmCnt\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab2.\"sumPerfAmt\" > 0 ";
		sql += "          THEN tab2.\"sumPerfAmt\" ";
		sql += "        ELSE 0 END               AS \"wmAmt\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab3.\"sumPerfCnt\" > 0 ";
		sql += "          THEN tab3.\"sumPerfCnt\" ";
		sql += "        ELSE 0 END               AS \"QCnt\" ";
		sql += "      , CASE";
		sql += "          WHEN tab3.\"sumPerfAmt\" > 0 ";
		sql += "          THEN tab3.\"sumPerfAmt\" ";
		sql += "        ELSE 0 END               AS \"QAmt\" ";
		sql += " FROM ( SELECT DISTINCT ";
		sql += "               CB.\"DistManager\" ";
		sql += "             , CB.\"DistCode\" ";
		sql += "             , CB.\"DistItem\" ";
		sql += "             , CB.\"DeptCode\" ";
		sql += "             , CB.\"DeptItem\" ";
		sql += "             , CE.\"Fullname\" ";
		sql += "        FROM \"CdBcm\" CB ";
		sql += "        LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = CB.\"DistManager\" ";
		sql += "        WHERE CB.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "          AND CB.\"DistManager\" IS NOT NULL ";
		sql += "          AND CB.\"DistCode\" IS NOT NULL ";
		sql += "      ) tab1 ";
		sql += " LEFT JOIN ( SELECT \"DistManager\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"sumPerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"sumPerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             WHERE \"WorkMonth\"= :iyymm ";
		sql += "               AND \"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "               AND \"DistManager\" is not null ";
		sql += "             GROUP BY \"DistManager\" ";
		sql += "           ) tab2 ON tab1.\"DistManager\" = tab2.\"DistManager\" ";
		sql += " LEFT JOIN ( SELECT \"DistManager\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"sumPerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"sumPerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             WHERE \"WorkMonth\">= :iWKs ";
		sql += "               AND \"WorkMonth\"<= :iWKe ";
		sql += "               AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000') ";
		sql += "               AND \"DistManager\" IS NOT NULL ";
		sql += "             GROUP BY \"DistManager\" ";
		sql += "           ) tab3 ON tab1.\"DistManager\" = tab3.\"DistManager\" ";
		sql += " ORDER BY \"wmAmt\" DESC ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", inputYearMonth);
		query.setParameter("iWKs", iWKs);
		query.setParameter("iWKe", iWKe);

		return this.convertToMap(query);

	}

	// 營管、營推、業推、業開、單位
	public List<Map<String, String>> findAllDept(TitaVo titaVo, Map<String, String> wkVo, String deptCode)
			throws Exception {
		String sdeptCode = "";

		int inputYear = Integer.parseInt(wkVo.get("F0"));

		int iMM = Integer.parseInt(wkVo.get("F1"));

		int inputYearMonth = (inputYear * 100) + iMM;

		// 季初的工作月
		String iWKs = "";
		// 季末的工作月
		String iWKe = "";
		if (iMM < 4) {
			iWKs = inputYear + "01";
			iWKe = inputYear + String.format("%02d", iMM);
		} else if (iMM < 7) {
			iWKs = inputYear + "04";
			iWKe = inputYear + String.format("%02d", iMM);
		} else if (iMM < 10) {
			iWKs = inputYear + "07";
			iWKe = inputYear + String.format("%02d", iMM);
		} else {
			iWKs = inputYear + "10";
			iWKe = inputYear + String.format("%02d", iMM);
		}

		if (deptCode == "A0B000" || deptCode == "A0F000" || deptCode == "A0E000" || deptCode == "A0M000"
				|| deptCode == "A0X000") {
			sdeptCode = "'" + deptCode + "'";
		} else {
			sdeptCode = "'A0B000','A0F000','A0E000','A0M000'";
		}

		this.info("LP004.findAll iYYMM=" + inputYearMonth + ",iWKSSN=" + iWKs + "~" + iWKe);

		String sql = " ";
		sql += " SELECT CASE ";
		sql += "          WHEN tab1.\"DeptCode\"='A0B000' ";
		sql += "          THEN '營管部' ";
		sql += "          WHEN tab1.\"DeptCode\"='A0F000' ";
		sql += "          THEN '營推部' ";
		sql += "          WHEN tab1.\"DeptCode\"='A0E000' ";
		sql += "          THEN '業推部' ";
		sql += "          WHEN tab1.\"DeptCode\"='A0M000' ";
		sql += "          THEN '業開部' ";
		sql += "          WHEN tab1.\"DeptCode\"='A0X000' ";
		sql += "          THEN '專業行銷課' ";
		sql += "        ELSE null END               AS \"DeptName\" ";
		sql += "      , tab1.\"DistItem\" ";
		sql += "      , tab1.\"UnitItem\" ";
		sql += "      , tab1.\"Fullname\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab2.\"sumPerfCnt\" > 0 ";
		sql += "          THEN tab2.\"sumPerfCnt\" ";
		sql += "        ELSE 0 END                  AS \"wmCnt\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab2.\"sumPerfAmt\" > 0 ";
		sql += "          THEN tab2.\"sumPerfAmt\" ";
		sql += "        ELSE 0 END                  AS \"wmAmt\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab3.\"sumPerfCnt\" > 0 ";
		sql += "          THEN tab3.\"sumPerfCnt\" ";
		sql += "        ELSE 0 END                  AS \"QCnt\" ";
		sql += "      , CASE ";
		sql += "          WHEN tab3.\"sumPerfAmt\" > 0 ";
		sql += "          THEN tab3.\"sumPerfAmt\" ";
		sql += "        ELSE 0 END                  AS \"QAmt\" ";
		sql += " FROM ( SELECT CB.\"DeptCode\" ";
		sql += "             , CB.\"DeptItem\" ";
		sql += "             , CB.\"DistCode\" ";
		sql += "             , CB.\"DistItem\" ";
		sql += "             , CB.\"UnitCode\" ";
		sql += "             , CB.\"UnitItem\" ";
		sql += "             , CB.\"UnitManager\" ";
		sql += "             , CE.\"Fullname\" ";
		sql += "        FROM \"CdBcm\" CB ";
		sql += "        LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = CB.\"UnitManager\" ";
		sql += "        WHERE CB.\"DeptCode\" IN (" + sdeptCode + ") ";
		sql += "          AND CB.\"UnitManager\" is not null ";
		sql += "          AND CB.\"DistCode\" is not null ";
		sql += "          AND CB.\"DistCode\" <> CB.\"UnitCode\" ";
		sql += "      ) tab1 ";
		sql += " LEFT JOIN ( SELECT \"UnitCode\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"sumPerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"sumPerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             WHERE \"WorkMonth\"= :iyymm ";
		sql += "               AND \"DeptCode\" IN(" + sdeptCode + ") ";
		sql += "             GROUP BY \"UnitCode\" ";
		sql += "           ) tab2 ON tab1.\"UnitCode\" = tab2.\"UnitCode\" ";
		sql += " LEFT JOIN ( SELECT \"UnitCode\" ";
		sql += "                  , SUM(\"PerfCnt\") AS \"sumPerfCnt\" ";
		sql += "                  , SUM(\"PerfAmt\") AS \"sumPerfAmt\" ";
		sql += "             FROM \"PfItDetail\" ";
		sql += "             WHERE \"WorkMonth\">= :iWKs ";
		sql += "               AND \"WorkMonth\"<= :iWKe ";
		sql += "               AND \"DeptCode\" IN(" + sdeptCode + ") ";
		sql += "            GROUP BY \"UnitCode\" ";
		sql += "           ) tab3 ON tab1.\"UnitCode\" = tab3.\"UnitCode\" ";
		sql += " ORDER BY \"wmAmt\" DESC ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", inputYearMonth);
		query.setParameter("iWKs", iWKs);
		query.setParameter("iWKe", iWKe);

		return this.convertToMap(query);

	}
	
}
