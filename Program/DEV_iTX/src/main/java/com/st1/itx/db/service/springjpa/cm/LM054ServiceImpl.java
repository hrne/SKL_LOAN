package com.st1.itx.db.service.springjpa.cm;

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
/* 逾期放款明細 */
public class LM054ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM054ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> ias34Ap(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYearMonth = iENTDY.substring(0, 6);

		logger.info("lM054.ias34Ap ENTDY=" + iENTDY + ",iYearMonth=" + iYearMonth);

		String sql = "SELECT I.\"CustNo\"            F0";
		sql += "            ,I.\"CustId\"            F1";
		sql += "            ,I.\"FacmNo\"            F2";
		sql += "            ,I.\"ApplNo\"            F3";
		sql += "            ,I.\"BormNo\"            F4";
		sql += "            ,I.\"AcCode\"            F5";
		sql += "            ,I.\"Status\"            F6";
		sql += "            ,I.\"FirstDrawdownDate\" F7";
		sql += "            ,I.\"DrawdownDate\"      F8";
		sql += "            ,I.\"FacLineDate\"       F9";
		sql += "            ,I.\"MaturityDate\"      F10";
		sql += "            ,I.\"LineAmt\"           F11";
		sql += "            ,I.\"DrawdownAmt\"       F12";
		sql += "            ,I.\"AcctFee\"           F13";
		sql += "            ,I.\"LoanBal\"           F14";
		sql += "            ,I.\"IntAmt\"            F15";
		sql += "            ,I.\"Fee\"               F16";
		sql += "            ,I.\"Rate\"              F17";
		sql += "            ,I.\"OvduDays\"          F18";
		sql += "            ,I.\"OvduDate\"          F19";
		sql += "            ,I.\"BadDebtDate\"       F20";
		sql += "            ,I.\"BadDebtAmt\"        F21";
		sql += "            ,I.\"DerCode\"           F22";
		sql += "            ,I.\"GracePeriod\"       F23";
		sql += "            ,I.\"ApproveRate\"       F24";
		sql += "            ,I.\"AmortizedCode\"     F25";
		sql += "            ,I.\"RateCode\"          F26";
		sql += "            ,I.\"RepayFreq\"         F27";
		sql += "            ,I.\"PayIntFreq\"        F28";
		sql += "            ,I.\"IndustryCode\"      F29";
		sql += "            ,I.\"ClTypeJCIC\"        F30";
		sql += "            ,I.\"Zip3\"          F31";
		sql += "            ,I.\"RateCode\"      F32";
		sql += "            ,I.\"CustKind\"          F33";
		sql += "            ,I.\"AssetKind\"         F34";
		sql += "            ,I.\"ProdNo\"            F35";
		sql += "            ,I.\"EvaAmt\"            F36";
		sql += "            ,I.\"FirstDueDate\"      F37";
		sql += "            ,I.\"TotalPeriod\"       F38";
		sql += "            ,I.\"AgreeBefFacmNo\"    F39";
		sql += "            ,I.\"AgreeBefBormNo\"    F40";
		sql += "            ,'F41'                   F41";//欄位聯貸項目不知道輸出條件
		sql += "            ,C.\"CustName\"          F42";
		sql += "            ,CASE";
		sql += "               WHEN P.\"GovOfferFlag\" = 'Y' THEN '*'";
		sql += "             ELSE ' ' END            F43";
		sql += "      FROM \"Ias34Ap\" I";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		sql += "      LEFT JOIN \"FacProd\" P ON P.\"ProdNo\" = I.\"ProdNo\"";
		sql += "      WHERE I.\"DataYM\" = :yearMonth ";
		sql += "      ORDER BY I.\"CustNo\"";
		sql += "              ,I.\"FacmNo\"";
		sql += "              ,I.\"BormNo\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", iYearMonth);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYearMonth = iENTDY.substring(0, 6);

		logger.info("lM054.findAll ENTDY=" + iENTDY + ",iYearMonth=" + iYearMonth);

		String sql = "SELECT I.\"CustNo\"      F0";
		sql += "            ,I.\"CustName\"    F1";
		sql += "            ,I.\"CustId\"      F2";
		sql += "            ,I.\"LoanBal\"     F3";
		sql += "            ,CASE";
		sql += "               WHEN I.\"Type\" = 1 THEN '聯貸'";
		sql += "               WHEN I.\"Type\" = 2 THEN '**'";
		sql += "               WHEN I.\"Type\" = 3 THEN '*'";
		sql += "             ELSE ' ' END      F4";
		sql += "            ,CASE";
		sql += "               WHEN I.\"RelsCode\" = '99' THEN N'#N/A'";
		sql += "             ELSE I.\"Item\" END F5";
		sql += "      FROM (SELECT I.\"CustNo\"";
		sql += "                  ,C.\"CustName\"";
		sql += "                  ,C.\"CustId\"";
		sql += "                  ,I.\"LoanBal\"";
		sql += "                  ,CASE WHEN I.\"SyndNo\"  > 0         THEN 1";
		sql += "                        WHEN I.\"LoanBal\" > 100000000 THEN 2";
		sql += "                        WHEN I.\"RelsCode\" <> '99'    THEN 3";
		sql += "                   ELSE 9 END \"Type\", D.\"Item\", I.\"RelsCode\"";
		sql += "            FROM (SELECT I.\"CustNo\"";
		sql += "                        ,MAX(NVL(M.\"RelsCode\", '99')) \"RelsCode\"";
		sql += "                        ,SUM(I.\"LoanBal\") \"LoanBal\"";
		sql += "                        ,MAX(L.\"SyndNo\") \"SyndNo\"";
		sql += "                  FROM  \"Ias34Ap\" I";
		sql += "                  LEFT JOIN \"MonthlyLoanBal\" M ON M.\"YearMonth\" = :yearMonth";
		sql += "                                                AND M.\"CustNo\"    = I.\"CustNo\"";
		sql += "                                                AND M.\"FacmNo\"    = I.\"FacmNo\"";
		sql += "                                                AND M.\"BormNo\"    = I.\"BormNo\"";
		sql += "                  LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = I.\"CustNo\"";
		sql += "                                             AND L.\"FacmNo\" = I.\"FacmNo\"";
		sql += "                                             AND L.\"BormNo\" = I.\"BormNo\"";
		sql += "                  WHERE  I.\"DataYM\" = :yearMonth";
		sql += "                  GROUP BY I.\"CustNo\") I";
		sql += "            LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		sql += "            LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'RelsCode'";
		sql += "                                  AND D.\"DefType\" = '2'";
		sql += "                                  AND D.\"Code\"    = I.\"RelsCode\") I";
		sql += "      ORDER BY I.\"Type\", I.\"LoanBal\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", iYearMonth);
		return this.convertToMap(query.getResultList());
	}

}