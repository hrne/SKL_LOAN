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
public class LM055ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM055ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYYMM = iENTDY.substring(0, 6);

		logger.info("lM055.findAll ENTDY=" + iENTDY + ",YYMM=" + iYYMM);

		// 工作表

		String sql = "SELECT I.\"CustNo\"                             F0";
		sql += "            ,I.\"CustId\"                             F1";
		sql += "            ,I.\"FacmNo\"                             F2";
		sql += "            ,I.\"ApplNo\"                             F3";
		sql += "            ,I.\"BormNo\"                             F4";
		sql += "            ,I.\"AcCode\"                             F5";
		sql += "            ,I.\"Status\"                             F6";
		sql += "            ,I.\"FirstDrawdownDate\"                  F7";
		sql += "            ,I.\"DrawdownDate\"                       F8";
		sql += "            ,I.\"FacLineDate\"                        F9";
		sql += "            ,I.\"MaturityDate\"                       F10";
		sql += "            ,I.\"LineAmt\"                            F11";
		sql += "            ,I.\"LoanBal\"                            F12";
		sql += "            ,I.\"IntAmt\"                             F13";
		sql += "            ,I.\"Fee\"                                F14";
		sql += "            ,I.\"Rate\"                               F15";
		sql += "            ,I.\"OvduDays\"                           F16";
		sql += "            ,I.\"OvduDate\"                           F17";
		sql += "            ,I.\"IndustryCode\"                       F18";
		sql += "            ,I.\"ClTypeJCIC\"                         F19";
		sql += "            ,I.\"CityCode\"                           F20";
		sql += "            ,I.\"RateCode\"                       F21";
		sql += "            ,I.\"CustKind\"                           F22";
		sql += "            ,CASE WHEN I.\"AssetKind\" = NULL THEN '#N/A'";
		sql += "                  WHEN I.\"AssetKind\" = 0    THEN '#N/A'";
		sql += "             ELSE CAST(I.\"AssetKind\" AS CHAR) END   F23";
		sql += "            ,I.\"ProdNo\"                             F24";
		sql += "            ,CASE WHEN I.\"AssetKind\" = NULL THEN '#N/A'";
		sql += "                  WHEN I.\"AssetKind\" = 0    THEN '#N/A'";
		sql += "             ELSE CAST(I.\"AssetKind\" AS CHAR) END   F25";
		sql += "            ,CASE WHEN I.\"OvduDate\" > 0 THEN '催'";
		sql += "                  WHEN I.\"OvduDays\" = 0 THEN '#N/A'";
		sql += "             ELSE CAST(I.\"OvduTerm\" AS CHAR) END    F26";
		sql += "            ,I.\"AcctCode\"                           F27";
		sql += "            ,I.\"Project\"                            F28";
		sql += "            ,DECODE(I.\"Project\"";
		sql += "                  ,'*'";
		sql += "                  ,'Z'";
		sql += "                   ,I.\"LoanType\")                   F29";
		sql += "      FROM (SELECT I.\"CustNo\"";
		sql += "                  ,I.\"CustId\"";
		sql += "                  ,I.\"FacmNo\"";
		sql += "                  ,I.\"BormNo\"";
		sql += "                  ,I.\"AcCode\"";
		sql += "                  ,I.\"Status\"";
		sql += "                  ,I.\"FirstDrawdownDate\"";
		sql += "                  ,I.\"DrawdownDate\"";
		sql += "                  ,I.\"FacLineDate\"";
		sql += "                  ,I.\"MaturityDate\"";
		sql += "                  ,I.\"LineAmt\"";
		sql += "                  ,I.\"LoanBal\"";
		sql += "                  ,I.\"IntAmt\"";
		sql += "                  ,I.\"Fee\"";
		sql += "                  ,I.\"Rate\"";
		sql += "                  ,I.\"OvduDays\"";
		sql += "                  ,I.\"OvduDate\"";
		sql += "                  ,I.\"IndustryCode\"";
		sql += "                  ,I.\"ClTypeJCIC\"";
		sql += "                  ,I.\"CityCode\"";
		sql += "                  ,I.\"RateCode\"";
		sql += "                  ,I.\"CustKind\"";
		sql += "                  ,I.\"ProdNo\"";
		sql += "                  ,I.\"AssetKind\"";
		sql += "                  ,M.\"OvduTerm\"";
		sql += "                  ,M.\"AcctCode\"";
		sql += "                  ,I.\"ApplNo\"";
		sql += "                  ,CASE WHEN M.\"AcctCode\" = '340' THEN '*'";
		sql += "                        WHEN I.\"ProdNo\" LIKE 'I%' THEN '*'";
		sql += "                        WHEN I.\"ProdNo\" LIKE '8%' THEN '*'";
		sql += "                   ELSE ' ' END \"Project\"";
		sql += "                  ,CASE WHEN I.\"ClTypeJCIC\" = '1H' THEN 'E'";
		sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '1%' THEN 'D'";
		sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '2%' THEN 'C'";
		sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '3%' THEN 'B'";
		sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '4%' THEN 'B'";
		sql += "                   ELSE 'A' END \"LoanType\"";
		sql += "            FROM \"Ias34Ap\" I";
		sql += "            LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
		sql += "                                         AND M.\"CustNo\"    =  I.\"CustNo\"";
		sql += "                                         AND M.\"FacmNo\"    =  I.\"FacmNo\"";
		sql += "            WHERE I.\"DataYM\" = :yymm) I";
		sql += "      ORDER BY I.\"CustNo\"";
		sql += "              ,I.\"FacmNo\"";
		sql += "              ,I.\"BormNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYYMM);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll_1(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYYMM = iENTDY.substring(0, 6);

		logger.info("lM055.findAll_1 ENTDY=" + iENTDY + ",YYMM=" + iYYMM);

		// 手搞

		String sql = "SELECT I.\"CustNo\"                             F0";
		sql += "            ,I.\"CustId\"                             F1";
		sql += "            ,I.\"FacmNo\"                             F2";
		sql += "            ,I.\"BormNo\"                             F3";
		sql += "            ,I.\"LoanBal\"                            F4";
		sql += "            ,I.\"OvduDays\"                           F5";
		sql += "            ,I.\"OvduDate\"                           F6";
		sql += "            ,I.\"ClTypeJCIC\"                         F7";
		sql += "            ,I.\"RateCode\"                       F8";
		sql += "            ,CASE WHEN I.\"AssetKind\" = NULL THEN '#N/A'";
		sql += "                  WHEN I.\"AssetKind\" = 0    THEN '#N/A'";
		sql += "             ELSE CAST(I.\"AssetKind\" AS CHAR) END   F9";
		sql += "            ,CASE WHEN I.\"OvduDate\" > 0 THEN '催'";
		sql += "                  WHEN I.\"OvduDays\" = 0 THEN '#N/A'";
		sql += "             ELSE CAST(I.\"OvduTerm\" AS CHAR) END    F10";
		sql += "            ,I.\"AcctCode\"                           F11";
		sql += "            ,I.\"Project\"                            F12";
		sql += "            ,DECODE(I.\"Project\"";
		sql += "                  ,'*'";
		sql += "                  ,'Z'";
		sql += "                  ,I.\"LoanType\")                    F13";
		sql += "            ,I.\"Project\"                            F14";
		sql += "      FROM (SELECT  I.\"CustNo\"";
		sql += "                   ,I.\"CustId\"";
		sql += "                   ,I.\"FacmNo\"";
		sql += "                   ,I.\"BormNo\"";
		sql += "                   ,I.\"AcCode\"";
		sql += "                   ,I.\"RateCode\"";
		sql += "                   ,I.\"LoanBal\"";
		sql += "                   ,I.\"OvduDays\"";
		sql += "                   ,I.\"OvduDate\"";
		sql += "                   ,I.\"ClTypeJCIC\"";
		sql += "                   ,I.\"ProdNo\"";
		sql += "                   ,I.\"AssetKind\"";
		sql += "                   ,M.\"OvduTerm\"";
		sql += "                   ,M.\"AcctCode\"";
		sql += "                   ,CASE WHEN M.\"AcctCode\" = '340' THEN '*'";
		sql += "                         WHEN I.\"RateCode\" IN ('IA', 'IB', 'IC', 'ID', 'IE', 'IF', 'IG', 'IH') THEN '*'";
		sql += "                         WHEN I.\"RateCode\" IN ('81', '82', '83', '84', '85', '86', '87', '88') THEN '*'";
		sql += "                    ELSE '' END \"Project\"";
		sql += "                   ,CASE WHEN I.\"ClTypeJCIC\" =    '1H' THEN 'E'";
		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '1%' THEN 'D'";
		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '2%' THEN 'C'";
		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '3%' THEN 'B'";
		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '4%' THEN 'B'";
		sql += "                    ELSE 'A' END \"LoanType\"";
		sql += "            FROM \"Ias34Ap\" I";
		sql += "            LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
		sql += "                                         AND M.\"CustNo\"    =  I.\"CustNo\"";
		sql += "                                         AND M.\"FacmNo\"    =  I.\"FacmNo\"";
		sql += "                                             WHERE I.\"DataYM\" = :yymm) I";
		sql += "      ORDER BY I.\"CustNo\"";
		sql += "              ,I.\"FacmNo\"";
		sql += "              ,I.\"BormNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYYMM);
		return this.convertToMap(query.getResultList());
	}

}