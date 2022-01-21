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
public class LP003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> wkSsn(TitaVo titaVo) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
		this.info("LP003ServiceImpl.wkSsn ENTDY=" + iENTDY);

		// 總表 f0:本日之工作年,f1:本日之工作月
		String sql = " ";
		sql += " SELECT W.\"Year\"  AS F0 ";
		sql += "      , W.\"Month\" AS F1 ";
		sql += " FROM \"CdWorkMonth\" W ";
		sql += " WHERE W.\"StartDate\" <= :iday ";
		sql += "   AND W.\"EndDate\" >= :iday ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", iENTDY);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findDept(TitaVo titaVo, Map<String, String> wkSsnVo) throws Exception {

		int iYEAR = 0;
		int iMM = 0;
		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0"));
			iMM = Integer.parseInt(wkSsnVo.get("F1"));
		}

		int inputYearMonth = (iYEAR * 100) + iMM;

		this.info("LP003ServiceImpl.findDept iYEAR =" + iYEAR);
		this.info("LP003ServiceImpl.findDept iMM =" + iMM);
		this.info("LP003ServiceImpl.findDept inputYearMonth =" + inputYearMonth);

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
		sql += "      , SUBSTR(B.\"UnitItem\",1,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",3,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",5,1) AS \"UnitItem\" "; // F1
		sql += "      , DE.\"Fullname\" "; // F2
		sql += "      , DT.\"WorkMonth\" "; // F3
		sql += "      , DT.\"GoalAmt\" "; // F4
		sql += "      , NVL(DPMS.\"PerfCntMonthlyTotal\",0) AS \"PerfCntMonthlyTotal\" "; // F5
		sql += "      , NVL(DPMS.\"PerfAmtMonthlyTotal\",0) AS \"PerfAmtMonthlyTotal\" "; // F6
		sql += "      , DT.\"DeptCode\" "; // F7
		sql += " FROM \"DeptTarget\" DT ";
		sql += " LEFT JOIN \"DeptEmpName\" DE ON DE.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptTargetSummary\" DTS ON DTS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptPfSummary\" DPS ON DPS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptPfMonthlySummary\" DPMS ON DPMS.\"DeptCode\" = Dt.\"DeptCode\" ";
		sql += "                                      AND DPMS.\"WorkMonth\" = DT.\"WorkMonth\" ";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = DT.\"DeptCode\" ";
		sql += " ORDER BY \"SEQ\" ";
		sql += "        , DT.\"WorkMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYear", iYEAR);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findEmp(TitaVo titaVo, Map<String, String> wkSsnVo) throws Exception {

		int iYEAR = 0;
		int iMM = 0;
		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0"));
			iMM = Integer.parseInt(wkSsnVo.get("F1"));
		}

		int inputYearMonth = (iYEAR * 100) + iMM;

		this.info("LP003ServiceImpl.findEmp iYEAR =" + iYEAR);
		this.info("LP003ServiceImpl.findEmp iMM =" + iMM);
		this.info("LP003ServiceImpl.findEmp inputYearMonth =" + inputYearMonth);

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
		sql += "                          ) AS \"SEQ\" ";
		sql += "      , SUBSTR(B.\"UnitItem\",1,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",3,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",5,1) AS \"UnitItem\" ";
		sql += "      , PBO.\"DistItem\" ";
		sql += "      , PBO.\"Fullname\" ";
		sql += "      , DT.\"WorkMonth\" ";
		sql += "      , DT.\"GoalAmt\" ";
		sql += "      , NVL(DP.\"PerfCntMonthlyTotal\",0) AS \"PerfCntMonthlyTotal\" ";
		sql += "      , NVL(DP.\"PerfAmtMonthlyTotal\",0) AS \"PerfAmtMonthlyTotal\" ";
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
		sql += " ORDER BY \"SEQ\" ";
		sql += "        , DT.\"WorkMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYear", iYEAR);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findDeptThisWKM(TitaVo titaVo, Map<String, String> wkSsnVo) {

		int iYEAR = 0;
		int iMM = 0;
		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0"));
			iMM = Integer.parseInt(wkSsnVo.get("F1"));
		}

		int inputYearMonth = (iYEAR * 100) + iMM;

		this.info("LP003ServiceImpl.findEmp iYEAR =" + iYEAR);
		this.info("LP003ServiceImpl.findEmp iMM =" + iMM);

		this.info("LP003ServiceImpl.findDeptThisWKM inputYearMonth =" + inputYearMonth);

		String sql = " ";
		sql += " WITH \"DeptTarget\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"WorkMonth\" ";
		sql += "          , SUM(\"GoalAmt\") AS \"GoalAmt\" ";
		sql += "     FROM \"PfBsOfficer\" ";
		sql += "     WHERE \"WorkMonth\" = :inputYearMonth ";
		sql += "       AND \"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000') ";
		sql += "     GROUP BY \"DeptCode\" ";
		sql += "            , \"WorkMonth\" ";
		sql += " ) ";
		sql += " , \"DeptEmpName\" AS ( ";
		sql += "      SELECT \"DeptCode\" ";
		sql += "           , \"EmpNo\" ";
		sql += "           , \"Fullname\" ";
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
		sql += "     WHERE O.\"WorkMonth\" = :inputYearMonth ";
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
		sql += "                          ) AS \"SEQ\" ";
		sql += "      , CASE DT.\"DeptCode\" ";
		sql += "          WHEN 'A0F000' ";
		sql += "          THEN '台北' ";
		sql += "          WHEN 'A0E000' ";
		sql += "          THEN '台中' ";
		sql += "          WHEN 'A0B000' ";
		sql += "          THEN '台北' ";
		sql += "          WHEN 'A0M000' ";
		sql += "          THEN '高雄' ";
		sql += "        ELSE ' ' END AS \"Resident\" ";
		sql += "      , DE.\"Fullname\" ";
		sql += "      , DE.\"EmpNo\" ";
		sql += "      , DT.\"GoalAmt\" ";
		sql += "      , NVL(DPMS.\"PerfAmtMonthlyTotal\",0) AS \"PerfAmtMonthlyTotal\" ";
		sql += "      , NVL(DPMS.\"PerfCntMonthlyTotal\",0) AS \"PerfCntMonthlyTotal\" ";
		sql += "      , SUBSTR(B.\"UnitItem\",1,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",3,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",5,1) AS \"DeptItem\" ";
		sql += " FROM \"DeptTarget\" DT ";
		sql += " LEFT JOIN \"DeptEmpName\" DE ON DE.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptTargetSummary\" DTS ON DTS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptPfSummary\" DPS ON DPS.\"DeptCode\" = DT.\"DeptCode\" ";
		sql += " LEFT JOIN \"DeptPfMonthlySummary\" DPMS ON DPMS.\"DeptCode\" = Dt.\"DeptCode\" ";
		sql += "                                      AND DPMS.\"WorkMonth\" = DT.\"WorkMonth\" ";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = DT.\"DeptCode\" ";
		sql += " ORDER BY \"SEQ\" ";
		sql += "        , DT.\"WorkMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findEmpThisWKM(TitaVo titaVo, Map<String, String> wkSsnVo) {

		int iYEAR = 0;
		int iMM = 0;
		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkSsnVo.get("F0"));
			iMM = Integer.parseInt(wkSsnVo.get("F1"));
		}

		int inputYearMonth = (iYEAR * 100) + iMM;

		this.info("LP003ServiceImpl.findEmp iYEAR =" + iYEAR);
		this.info("LP003ServiceImpl.findEmp iMM =" + iMM);

		this.info("LP003ServiceImpl.findEmpThisWKM inputYearMonth =" + inputYearMonth);

		String sql = " ";
		sql += " WITH \"DistTarget\" AS ( ";
		sql += "     SELECT \"DeptCode\" ";
		sql += "          , \"EmpNo\" ";
		sql += "          , \"WorkMonth\" ";
		sql += "          , SUM(\"GoalAmt\") AS \"GoalAmt\" ";
		sql += "     FROM \"PfBsOfficer\" ";
		sql += "     WHERE \"WorkMonth\" = :inputYearMonth ";
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
		sql += "     WHERE O.\"WorkMonth\" = :inputYearMonth ";
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
		sql += "                          ) AS \"SEQ\" ";
		sql += "      , PBO.\"Fullname\" ";
		sql += "      , PBO.\"EmpNo\" ";
		sql += "      , DT.\"GoalAmt\" ";
		sql += "      , NVL(DP.\"PerfAmtMonthlyTotal\",0) AS \"PerfAmtMonthlyTotal\" ";
		sql += "      , NVL(DP.\"PerfCntMonthlyTotal\",0) AS \"PerfCntMonthlyTotal\" ";
		sql += "      , SUBSTR(B.\"UnitItem\",1,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",3,1) ";
		sql += "        || SUBSTR(B.\"UnitItem\",5,1) AS \"UnitItem\" ";
		sql += "      , PBO.\"DistItem\" ";
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
		sql += " ORDER BY \"SEQ\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}
}
