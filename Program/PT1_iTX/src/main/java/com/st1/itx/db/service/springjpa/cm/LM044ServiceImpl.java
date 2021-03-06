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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LM044ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int iENTDY = parse.stringToInteger(titaVo.get("ENTDY")) + 19110000;
		int iYEAR = iENTDY / 100000;
		int iEYYMM = iENTDY / 100; // n月
		int iMM = iEYYMM % 100;

		int iLYYMM = 0; // n-1月
		int iSYYMM = 0; // n-2月
		if (iMM == 1) {
			iLYYMM = (iYEAR - 1) * 100 + 12;
			iSYYMM = (iYEAR - 1) * 100 + 11;
		} else if (iMM == 2) {
			iLYYMM = iYEAR * 100 + 1;
			iSYYMM = (iYEAR - 1) * 100 + 12;
		} else {
			iLYYMM = iEYYMM - 1;
			iSYYMM = iEYYMM - 2;
		}

		this.info("lM044.findAll SYYMM=" + iSYYMM + ",EYYMM=" + iEYYMM + ",LYYMM=" + iLYYMM);

		String sql = "SELECT DECODE(M.\"CityCode\", 'A3', '0', 'AZ', '0', M.\"CityCode\") \"CityCode\"";
		sql += "            ,DECODE(M.\"CityCode\", 'A3', '股票', 'AZ', '企金', C.\"CityItem\") \"CityItem\"";
		sql += "            ,SUM(M.\"LoanBal\") + SUM(M.\"ColBal\") \"LoanBal\"";
		sql += "            ,SUM(M.\"OvduBal\") \"OvduBal\"";
		sql += "            ,SUM(M.\"ColBal\") \"ColBal\"";
		sql += "            ,CASE WHEN (SUM(M.\"sLoanBal\") + SUM(M.\"sColBal\") + SUM(M.\"sOvduBal\")) > 0 ";
		sql += "                  THEN ROUND((SUM(M.\"sColBal\") + SUM(M.\"sOvduBal\")) / (SUM(M.\"sLoanBal\") + SUM(M.\"sColBal\") + SUM(M.\"sOvduBal\")), 4) ";
		sql += "             ELSE 0 END \"sRatio\"";
		sql += "            ,CASE WHEN (SUM(M.\"lLoanBal\") + SUM(M.\"lColBal\") + SUM(M.\"lOvduBal\")) > 0";
		sql += "                  THEN ROUND((SUM(M.\"lColBal\") + SUM(M.\"lOvduBal\")) / (SUM(M.\"lLoanBal\") + SUM(M.\"lColBal\") + SUM(M.\"lOvduBal\")), 4) ";
		sql += "             ELSE 0 END \"lRatio\"";
		sql += "            ,CASE WHEN (SUM(M.\"LoanBal\") + SUM(M.\"ColBal\") + SUM(M.\"OvduBal\")) > 0";
		sql += "                  THEN ROUND((SUM(M.\"ColBal\") + SUM(M.\"OvduBal\")) / (SUM(M.\"LoanBal\") + SUM(M.\"ColBal\") + SUM(M.\"OvduBal\")), 4) ";
		sql += "             ELSE 0 END \"Ratio\"";
		sql += "            ,SUM(M.\"sLoanBal\") + SUM(M.\"sColBal\") + SUM(M.\"sOvduBal\") \"sLoanBal\"";
		sql += "            ,SUM(M.\"lLoanBal\") + SUM(M.\"lColBal\") + SUM(M.\"lOvduBal\") \"lLoanBal\"";
		sql += "            ,SUM(M.\"NorthOvduBal\") \"NorthOvduBal\"";
		sql += "            ,SUM(M.\"CenterOvduBal\") \"CenterOvduBal\"";
		sql += "            ,SUM(M.\"SouthOvduBal\") \"SouthOvduBal\"";
		sql += "            ,SUM(M.\"NorthColBal\") \"NorthColBal\"";
		sql += "            ,SUM(M.\"CenterColBal\") \"CenterColBal\"";
		sql += "            ,SUM(M.\"SouthColBal\") \"SouthColBal\"";
		sql += "            ,SUM(M.\"sNorthOvduBal\") \"sNorthOvduBal\"";
		sql += "            ,SUM(M.\"sCenterOvduBal\") \"sCenterOvduBal\"";
		sql += "            ,SUM(M.\"sSouthOvduBal\") \"sSouthOvduBal\"";
		sql += "            ,SUM(M.\"lNorthOvduBal\") \"lNorthOvduBal\"";
		sql += "            ,SUM(M.\"lCenterOvduBal\") \"lCenterOvduBal\"";
		sql += "            ,SUM(M.\"lSouthOvduBal\") \"lSouthOvduBal\"";
		// 企推
		sql += "            ,SUM(M.\"CompanyOvduBal\") \"CompanyOvduBal\" ";
		sql += "            ,SUM(M.\"CompanyColBal\") \"CompanyColBal\" ";
		sql += "            ,SUM(M.\"sCompanyOvduBal\") \"sCompanyOvduBal\" ";
		sql += "            ,SUM(M.\"lCompanyOvduBal\") \"lCompanyOvduBal\" ";
		// 企推
		sql += "      FROM( SELECT CASE WHEN DECODE(M.\"DepartmentCode\", '1', 1, 0) = 0";
		sql += "                         AND M.\"CityCode\" IS NOT NULL THEN M.\"CityCode\"";
		sql += "                        WHEN M.\"ClCode1\" = 3 THEN 'A3'";
		sql += "                   ELSE 'AZ' END \"CityCode\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND M.\"OvduTerm\" < 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"sLoanBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"sColBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                        THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"sOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND M.\"OvduTerm\" < 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"lLoanBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"lColBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                        THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"lOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND M.\"OvduTerm\" < 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"LoanBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"ColBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                        THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"OvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HC00'";
		sql += "                        THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"NorthOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HJ00'";
		sql += "                        THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"CenterOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HL00'";
		sql += "                        THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"SouthOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HC00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"NorthColBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HJ00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"CenterColBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HL00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"SouthColBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HC00' THEN M.\"PrinBalance\"";
		sql += "                        WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HC00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"sNorthOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HJ00' THEN M.\"PrinBalance\"";
		sql += "                        WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HJ00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"sCenterOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HL00' THEN M.\"PrinBalance\"";
		sql += "                        WHEN M.\"YearMonth\" = :syymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HL00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"sSouthOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HC00' THEN M.\"PrinBalance\"";
		sql += "                        WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HC00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"lNorthOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HJ00' THEN M.\"PrinBalance\"";
		sql += "                        WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HJ00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"lCenterOvduBal\"";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" = '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HL00' THEN M.\"PrinBalance\"";
		sql += "                        WHEN M.\"YearMonth\" = :lyymm";
		sql += "                         AND M.\"AcctCode\" <> '990'";
		sql += "                         AND C.\"CenterCodeAcc\" = '10HL00'";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\"";
		sql += "                   ELSE 0 END \"lSouthOvduBal\"";
		// 企推
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm ";
		sql += "                         AND M.\"ClCode1\" = 3 ";
		sql += "                         AND M.\"AcctCode\" <> '990' ";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\" ";
		sql += "                   ELSE 0 END \"CompanyColBal\" ";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :eyymm ";
		sql += "                         AND M.\"ClCode1\" = 3 ";
		sql += "                         AND M.\"AcctCode\" = '990' THEN M.\"PrinBalance\" ";
		sql += "                   ELSE 0 END \"CompanyOvduBal\" ";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :lyymm ";
		sql += "                         AND M.\"ClCode1\" = 3 ";
		sql += "                         AND M.\"AcctCode\" = '990' THEN M.\"PrinBalance\" ";
		sql += "                        WHEN M.\"YearMonth\" = :lyymm ";
		sql += "                         AND M.\"ClCode1\" = 3 ";
		sql += "                         AND M.\"AcctCode\" <> '990' ";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\" ";
		sql += "                   ELSE 0 END \"lCompanyOvduBal\" ";
		sql += "                  ,CASE WHEN M.\"YearMonth\" = :syymm ";
		sql += "                         AND M.\"ClCode1\" = 3 ";
		sql += "                         AND M.\"AcctCode\" = '990' THEN M.\"PrinBalance\" ";
		sql += "                        WHEN M.\"YearMonth\" = :syymm ";
		sql += "                         AND M.\"ClCode1\" = 3 ";
		sql += "                         AND M.\"AcctCode\" <> '990' ";
		sql += "                         AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\" ";
		sql += "                   ELSE 0 END \"sCompanyOvduBal\" ";
		// 企推
		sql += "            FROM \"MonthlyFacBal\" M";
		sql += "            LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\"";
		sql += "                                     AND FAC.\"FacmNo\" = M.\"FacmNo\"";
		sql += "            LEFT JOIN \"CdEmp\" C ON C.\"EmployeeNo\" = FAC.\"Supervisor\"";
		sql += "            WHERE M.\"YearMonth\" >= :syymm";
		sql += "              AND M.\"YearMonth\" <= :eyymm";
		sql += "              AND M.\"PrinBalance\" > 0";
		sql += "            UNION ALL";
		sql += "            SELECT C.\"CityCode\" \"CityCode\"";
		sql += "                  ,0 \"sLoanBal\"";
		sql += "                  ,0 \"sColBal\"";
		sql += "                  ,0 \"sOvduBal\"";
		sql += "                  ,0 \"lLoanBal\"";
		sql += "                  ,0 \"lColBal\"";
		sql += "                  ,0 \"lOvduBal\"";
		sql += "                  ,0 \"LoanBal\"";
		sql += "                  ,0 \"ColBal\"";
		sql += "                  ,0 \"OvduBal\"";
		sql += "                  ,0 \"NorthOvduBal\"";
		sql += "                  ,0 \"CenterOvduBal\"";
		sql += "                  ,0 \"SouthOvduBal\"";
		sql += "                  ,0 \"NorthColBal\"";
		sql += "                  ,0 \"CenterColBal\"";
		sql += "                  ,0 \"SouthColBal\"";
		sql += "                  ,0 \"sNorthOvduBal\"";
		sql += "                  ,0 \"sCenterOvduBal\"";
		sql += "                  ,0 \"sSouthOvduBal\"";
		sql += "                  ,0 \"lNorthOvduBal\"";
		sql += "                  ,0 \"lCenterOvduBal\"";
		sql += "                  ,0 \"lSouthOvduBal\"";
		sql += "                  ,0 \"CompanyOvduBal\"";
		sql += "                  ,0 \"CompanyColBal\"";
		sql += "                  ,0 \"sCompanyOvduBal\"";
		sql += "                  ,0 \"lCompanyOvduBal\"";
		sql += "            FROM \"CdCity\" C";
		sql += "            WHERE C.\"CityCode\" NOT IN ('96', '98')";
		sql += "            UNION ALL";
		sql += "            SELECT 'A3' \"CityCode\"";
		sql += "                  ,0 \"sLoanBal\"";
		sql += "                  ,0 \"sColBal\"";
		sql += "                  ,0 \"sOvduBal\"";
		sql += "                  ,0 \"lLoanBal\"";
		sql += "                  ,0 \"lColBal\"";
		sql += "                  ,0 \"lOvduBal\"";
		sql += "                  ,0 \"LoanBal\"";
		sql += "                  ,0 \"ColBal\"";
		sql += "                  ,0 \"OvduBal\"";
		sql += "                  ,0 \"NorthOvduBal\"";
		sql += "                  ,0 \"CenterOvduBal\"";
		sql += "                  ,0 \"SouthOvduBal\"";
		sql += "                  ,0 \"NorthColBal\"";
		sql += "                  ,0 \"CenterColBal\"";
		sql += "                  ,0 \"SouthColBal\"";
		sql += "                  ,0 \"sNorthOvduBal\"";
		sql += "                  ,0 \"sCenterOvduBal\"";
		sql += "                  ,0 \"sSouthOvduBal\"";
		sql += "                  ,0 \"lNorthOvduBal\"";
		sql += "                  ,0 \"lCenterOvduBal\"";
		sql += "                  ,0 \"lSouthOvduBal\"";
		sql += "                  ,0 \"CompanyOvduBal\"";
		sql += "                  ,0 \"CompanyColBal\"";
		sql += "                  ,0 \"sCompanyOvduBal\"";
		sql += "                  ,0 \"lCompanyOvduBal\"";
		sql += "            FROM DUAL";
		sql += "            UNION ALL";
		sql += "            SELECT 'AZ' \"CityCode\"";
		sql += "                  ,0 \"sLoanBal\"";
		sql += "                  ,0 \"sColBal\"";
		sql += "                  ,0 \"sOvduBal\"";
		sql += "                  ,0 \"lLoanBal\"";
		sql += "                  ,0 \"lColBal\"";
		sql += "                  ,0 \"lOvduBal\"";
		sql += "                  ,0 \"LoanBal\"";
		sql += "                  ,0 \"ColBal\"";
		sql += "                  ,0 \"OvduBal\"";
		sql += "                  ,0 \"NorthOvduBal\"";
		sql += "                  ,0 \"CenterOvduBal\"";
		sql += "                  ,0 \"SouthOvduBal\"";
		sql += "                  ,0 \"NorthColBal\"";
		sql += "                  ,0 \"CenterColBal\"";
		sql += "                  ,0 \"SouthColBal\"";
		sql += "                  ,0 \"sNorthOvduBal\"";
		sql += "                  ,0 \"sCenterOvduBal\"";
		sql += "                  ,0 \"sSouthOvduBal\"";
		sql += "                  ,0 \"lNorthOvduBal\"";
		sql += "                  ,0 \"lCenterOvduBal\"";
		sql += "                  ,0 \"lSouthOvduBal\"";
		sql += "                  ,0 \"CompanyOvduBal\"";
		sql += "                  ,0 \"CompanyColBal\"";
		sql += "                  ,0 \"sCompanyOvduBal\"";
		sql += "                  ,0 \"lCompanyOvduBal\"";
		sql += "            FROM DUAL) M";
		sql += "      LEFT JOIN \"CdCity\" C ON C.\"CityCode\" = M.\"CityCode\"";
		sql += "      GROUP BY M.\"CityCode\", \"CityItem\"";
		sql += "      ORDER BY CASE WHEN M.\"CityCode\" = '85' THEN '96'";
		sql += "                    WHEN M.\"CityCode\" = '85' THEN '98'";
		sql += "                    WHEN M.\"CityCode\" = '85' THEN '99'";
		sql += "               ELSE M.\"CityCode\" END";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("syymm", iSYYMM);
		query.setParameter("lyymm", iLYYMM);
		query.setParameter("eyymm", iEYYMM);

		return this.convertToMap(query);
	}

}