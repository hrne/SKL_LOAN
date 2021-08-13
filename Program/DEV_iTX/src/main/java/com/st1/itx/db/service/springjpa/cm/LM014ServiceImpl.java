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
/* 逾期放款明細 */
public class LM014ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail1(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail1 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"AcctCode\"               AS \"AcctCode\"                "; // F0
		sql += "             ,C.\"AcctItem\"               AS \"AcctItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"AcctCode\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")   AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT M.\"AcctCode\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"AcctCode\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = M.\"AcctCode\"";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"AcctCode\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail10(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail10 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1' ";
		sql += "                THEN '不動產-個人' ";
		sql += "                WHEN M.\"LoanType\" = '2' ";
		sql += "                THEN '不動產-公司' ";
		sql += "                WHEN M.\"LoanType\" = '9' ";
		sql += "                THEN '動產' ";
		sql += "                WHEN M.\"LoanType\" = 'A' ";
		sql += "                THEN '員工貸款' ";
		sql += "              ELSE '有價證券' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail11(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail11 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"GroupType\"              AS \"GroupType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"GroupType\" = '1' ";
		sql += "                THEN '非關係人(個人)' ";
		sql += "                WHEN M.\"GroupType\" = '2' ";
		sql += "                THEN '非關係人(公司)' ";
		sql += "                WHEN M.\"GroupType\" = '3' ";
		sql += "                THEN '關係人  (個人)' ";
		sql += "                WHEN M.\"GroupType\" = '4' ";
		sql += "                THEN '關係人  (公司)' ";
		sql += "              ELSE ' ' END                 AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"GroupType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN BR.\"IsRelated\" = 'N' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'N'";
		sql += "                                THEN '1' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '4' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y'";
		sql += "                                THEN '3' ";
		sql += "                              ELSE '9' END AS \"GroupType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"GroupType\"";
		sql += "              ) M";
		sql += "         WHERE M.\"GroupType\" <> '9'";
		sql += "         ORDER BY M.\"GroupType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail12(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail12 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1Y' ";
		sql += "                THEN '不動產 (個人) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '1N' ";
		sql += "                THEN '不動產 (個人) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2Y' ";
		sql += "                THEN '不動產 (公司) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2N' ";
		sql += "                THEN '不動產 (公司) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9Y' ";
		sql += "                THEN '動產 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9N' ";
		sql += "                THEN '動產 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 非關係人' ";
		sql += "              ELSE '有價證券 (個人) 非關係人' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END ";
		sql += "                              || BR.\"IsRelated\" AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail13(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail13 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"AcctCode\"               AS \"AcctCode\"                "; // F0
		sql += "             ,C.\"AcctItem\"               AS \"AcctItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"AcctCode\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT M.\"AcctCode\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '1'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"AcctCode\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = M.\"AcctCode\"";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"AcctCode\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail14(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail14 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1' ";
		sql += "                THEN '不動產-個人' ";
		sql += "                WHEN M.\"LoanType\" = '2' ";
		sql += "                THEN '不動產-公司' ";
		sql += "                WHEN M.\"LoanType\" = '9' ";
		sql += "                THEN '動產' ";
		sql += "                WHEN M.\"LoanType\" = 'A' ";
		sql += "                THEN '員工貸款' ";
		sql += "              ELSE '有價證券' END          AS \"LoanItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END AS \"LoanType\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '1'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"LoanType\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail15(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail15 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"GroupType\"              AS \"GroupType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"GroupType\" = '1' ";
		sql += "                THEN '非關係人(個人)' ";
		sql += "                WHEN M.\"GroupType\" = '2' ";
		sql += "                THEN '非關係人(公司)' ";
		sql += "                WHEN M.\"GroupType\" = '3' ";
		sql += "                THEN '關係人  (個人)' ";
		sql += "                WHEN M.\"GroupType\" = '4' ";
		sql += "                THEN '關係人  (公司)' ";
		sql += "              ELSE ' ' END                 AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"GroupType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN BR.\"IsRelated\" = 'N' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'N'";
		sql += "                                THEN '1' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '4' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y'";
		sql += "                                THEN '3' ";
		sql += "                              ELSE '9' END AS \"GroupType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '1'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"GroupType\"";
		sql += "              ) M";
		sql += "         WHERE M.\"GroupType\" <> '9'";
		sql += "         ORDER BY M.\"GroupType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail16(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail16 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1Y' ";
		sql += "                THEN '不動產 (個人) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '1N' ";
		sql += "                THEN '不動產 (個人) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2Y' ";
		sql += "                THEN '不動產 (公司) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2N' ";
		sql += "                THEN '不動產 (公司) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9Y' ";
		sql += "                THEN '動產 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9N' ";
		sql += "                THEN '動產 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 非關係人' ";
		sql += "              ELSE '有價證券 (個人) 非關係人' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END ";
		sql += "                              || BR.\"IsRelated\" AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '1'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail17(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail17 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"AcctCode\"               AS \"AcctCode\"                "; // F0
		sql += "             ,C.\"AcctItem\"               AS \"AcctItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"AcctCode\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT M.\"AcctCode\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"AcctCode\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = M.\"AcctCode\"";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"AcctCode\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail18(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail18 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1' ";
		sql += "                THEN '不動產-個人' ";
		sql += "                WHEN M.\"LoanType\" = '2' ";
		sql += "                THEN '不動產-公司' ";
		sql += "                WHEN M.\"LoanType\" = '9' ";
		sql += "                THEN '動產' ";
		sql += "                WHEN M.\"LoanType\" = 'A' ";
		sql += "                THEN '員工貸款' ";
		sql += "              ELSE '有價證券' END          AS \"LoanItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END AS \"LoanType\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"LoanType\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail19(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail19 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"GroupType\"              AS \"GroupType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"GroupType\" = '1' ";
		sql += "                THEN '非關係人(個人)' ";
		sql += "                WHEN M.\"GroupType\" = '2' ";
		sql += "                THEN '非關係人(公司)' ";
		sql += "                WHEN M.\"GroupType\" = '3' ";
		sql += "                THEN '關係人  (個人)' ";
		sql += "                WHEN M.\"GroupType\" = '4' ";
		sql += "                THEN '關係人  (公司)' ";
		sql += "              ELSE ' ' END                 AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"GroupType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN BR.\"IsRelated\" = 'N' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'N'";
		sql += "                                THEN '1' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '4' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y'";
		sql += "                                THEN '3' ";
		sql += "                              ELSE '9' END AS \"GroupType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"GroupType\"";
		sql += "              ) M";
		sql += "         WHERE M.\"GroupType\" <> '9'";
		sql += "         ORDER BY M.\"GroupType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail2(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail2 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1' ";
		sql += "                THEN '不動產-個人' ";
		sql += "                WHEN M.\"LoanType\" = '2' ";
		sql += "                THEN '不動產-公司' ";
		sql += "                WHEN M.\"LoanType\" = '9' ";
		sql += "                THEN '動產' ";
		sql += "                WHEN M.\"LoanType\" = 'A' ";
		sql += "                THEN '員工貸款' ";
		sql += "              ELSE '有價證券' END          AS \"LoanItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END AS \"LoanType\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"LoanType\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail20(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail20 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1Y' ";
		sql += "                THEN '不動產 (個人) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '1N' ";
		sql += "                THEN '不動產 (個人) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2Y' ";
		sql += "                THEN '不動產 (公司) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2N' ";
		sql += "                THEN '不動產 (公司) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9Y' ";
		sql += "                THEN '動產 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9N' ";
		sql += "                THEN '動產 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 非關係人' ";
		sql += "              ELSE '有價證券 (個人) 非關係人' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END ";
		sql += "                              || BR.\"IsRelated\" AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND NVL(M.\"DepartmentCode\",'0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail3(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail3 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"GroupType\"              AS \"GroupType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"GroupType\" = '1' ";
		sql += "                THEN '非關係人(個人)' ";
		sql += "                WHEN M.\"GroupType\" = '2' ";
		sql += "                THEN '非關係人(公司)' ";
		sql += "                WHEN M.\"GroupType\" = '3' ";
		sql += "                THEN '關係人  (個人)' ";
		sql += "                WHEN M.\"GroupType\" = '4' ";
		sql += "                THEN '關係人  (公司)' ";
		sql += "              ELSE ' ' END                 AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"GroupType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN BR.\"IsRelated\" = 'N' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'N'";
		sql += "                                THEN '1' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '4' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y'";
		sql += "                                THEN '3' ";
		sql += "                              ELSE '9' END AS \"GroupType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"GroupType\"";
		sql += "              ) M";
		sql += "         WHERE M.\"GroupType\" <> '9'";
		sql += "         ORDER BY M.\"GroupType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail4(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail4 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1Y' ";
		sql += "                THEN '不動產 (個人) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '1N' ";
		sql += "                THEN '不動產 (個人) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2Y' ";
		sql += "                THEN '不動產 (公司) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2N' ";
		sql += "                THEN '不動產 (公司) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9Y' ";
		sql += "                THEN '動產 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9N' ";
		sql += "                THEN '動產 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 非關係人' ";
		sql += "              ELSE '有價證券 (個人) 非關係人' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END ";
		sql += "                              || BR.\"IsRelated\" AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail5(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail5 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"AcctCode\"               AS \"AcctCode\"                "; // F0
		sql += "             ,C.\"AcctItem\"               AS \"AcctItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"AcctCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT M.\"AcctCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') > '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"AcctCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = M.\"AcctCode\"";
		sql += "         ORDER BY M.\"AcctCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail6(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail6 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1' ";
		sql += "                THEN '不動產-個人' ";
		sql += "                WHEN M.\"LoanType\" = '2' ";
		sql += "                THEN '不動產-公司' ";
		sql += "                WHEN M.\"LoanType\" = '9' ";
		sql += "                THEN '動產' ";
		sql += "                WHEN M.\"LoanType\" = 'A' ";
		sql += "                THEN '員工貸款' ";
		sql += "              ELSE '有價證券' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') > '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail7(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail7 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"GroupType\"              AS \"GroupType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"GroupType\" = '1' ";
		sql += "                THEN '非關係人(個人)' ";
		sql += "                WHEN M.\"GroupType\" = '2' ";
		sql += "                THEN '非關係人(公司)' ";
		sql += "                WHEN M.\"GroupType\" = '3' ";
		sql += "                THEN '關係人  (個人)' ";
		sql += "                WHEN M.\"GroupType\" = '4' ";
		sql += "                THEN '關係人  (公司)' ";
		sql += "              ELSE ' ' END                 AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"GroupType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN BR.\"IsRelated\" = 'N' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'N'";
		sql += "                                THEN '1' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y' AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '4' ";
		sql += "                                WHEN BR.\"IsRelated\" = 'Y'";
		sql += "                                THEN '3' ";
		sql += "                              ELSE '9' END AS \"GroupType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') > '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"GroupType\"";
		sql += "              ) M";
		sql += "         WHERE M.\"GroupType\" <> '9'";
		sql += "         ORDER BY M.\"GroupType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail8(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail8 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"LoanType\"               AS \"LoanType\"                "; // F0
		sql += "             ,CASE ";
		sql += "                WHEN M.\"LoanType\" = '1Y' ";
		sql += "                THEN '不動產 (個人) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '1N' ";
		sql += "                THEN '不動產 (個人) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2Y' ";
		sql += "                THEN '不動產 (公司) 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '2N' ";
		sql += "                THEN '不動產 (公司) 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9Y' ";
		sql += "                THEN '動產 關係人' ";
		sql += "                WHEN M.\"LoanType\" = '9N' ";
		sql += "                THEN '動產 非關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 關係人' ";
		sql += "                WHEN M.\"LoanType\" = 'AY' ";
		sql += "                THEN '員工貸款 非關係人' ";
		sql += "              ELSE '有價證券 (個人) 非關係人' END          AS \"LoanItem\"                "; // F1
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"LoanType\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT CASE";
		sql += "                                WHEN SUBSTR(M.\"ProdNo\",1,1) = '1'";
		sql += "                                THEN 'A' ";
		sql += "                                WHEN M.\"ClCode1\" = 2 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                                WHEN M.\"ClCode1\" = 1 AND DECODE(M.\"EntCode\", '1', '1', '0') = '1'";
		sql += "                                THEN '2' ";
		sql += "                              ELSE TO_CHAR(M.\"ClCode1\") END ";
		sql += "                              || BR.\"IsRelated\" AS \"LoanType\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       LEFT JOIN ( SELECT CM.\"CustNo\"";
		sql += "                                         ,CASE";
		sql += "                                            WHEN NVL(BRS.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRS.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRC.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW001\",' ') = '1' THEN 'Y'";
		sql += "                                            WHEN NVL(BRF.\"LAW005\",' ') = '1' THEN 'Y'";
		sql += "                                         ELSE 'N' END AS \"IsRelated\"";
		sql += "                                   FROM \"CustMain\" CM";
		sql += "                                   LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
		sql += "                                   LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                                 ) BR ON BR.\"CustNo\" = M.\"CustNo\"";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') > '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"LoanType\"";
		sql += "              ) M";
		sql += "         ORDER BY M.\"LoanType\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDetail9(TitaVo titaVo) throws Exception {
		this.info("LM014ServiceImpl getDetail9 ");

		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT M.\"AcctCode\"               AS \"AcctCode\"                "; // F0
		sql += "             ,C.\"AcctItem\"               AS \"AcctItem\"                "; // F1
		sql += "             ,NVL(D.\"Code\",0)            AS \"AcSubBookCode\"              "; // F2
		sql += "             ,NVL(D.\"Item\",'一般帳戶')   AS \"AcBookItem\"              "; // F3
		sql += "             ,M.\"thisMonthIntAmt\"        AS \"thisMonthIntAmt\"         "; // F4
		sql += "             ,M.\"thisMonthLoanBal\"       AS \"thisMonthLoanBal\"        "; // F5
		sql += "             ,M.\"thisMonthWeighted\"      AS \"thisMonthWeighted\"       "; // F6
		sql += "             ,M.\"thisYearIntAmt\"         AS \"thisYearIntAmt\"          "; // F7
		sql += "             ,M.\"thisYearLoanBal\"        AS \"thisYearLoanBal\"         "; // F8
		sql += "             ,M.\"thisYearWeighted\"       AS \"thisYearWeighted\"        "; // F9
		sql += "         FROM ( SELECT S.\"AcctCode\"";
		sql += "                      ,S.\"AcSubBookCode\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthIntAmt\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthLoanBal\"";
		sql += "                      ,SUM(CASE";
		sql += "                             WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                           ELSE 0 END)        AS \"thisMonthWeighted\"";
		sql += "                      ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmt\"";
		sql += "                      ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBal\"   ";
		sql += "                      ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeighted\"  ";
		sql += "                FROM ( SELECT M.\"AcctCode\"";
		sql += "                            , M.\"AcSubBookCode\"";
		sql += "                            , CASE";
		sql += "                                WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                              ELSE 1 END      AS \"SumFlag\"";
		sql += "                            , ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                            , M.\"LoanBalance\"";
		sql += "                            , M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                            , M.\"YearMonth\"";
		sql += "                       FROM \"MonthlyLoanBal\" M";
		sql += "                       WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                         AND M.\"AcctCode\" > '300'";
		sql += "                         AND M.\"AcctCode\" < '390'";
		sql += "                         AND DECODE(M.\"EntCode\", '1', '1', '0') = '0'";
		sql += "                     ) S";
		sql += "                GROUP BY S.\"AcctCode\", S.\"AcSubBookCode\"";
		sql += "              ) M";
		sql += "         LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = M.\"AcctCode\"";
		sql += "         LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                               AND D.\"Code\"    = M.\"AcSubBookCode\" ";
		sql += "         ORDER BY M.\"AcctCode\", M.\"AcSubBookCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTotalAll(TitaVo titaVo) throws Exception {

		this.info("LM014ServiceImpl getTotalAll");
		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthIntAmtTotal\"   "; // F0
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthLoanBalTotal\"  "; // F1
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthWeightedTotal\" "; // F2
		sql += "             ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmtTotal\"    "; // F3
		sql += "             ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBalTotal\"   "; // F4
		sql += "             ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeightedTotal\"  "; // F5
		sql += "       FROM ( SELECT CASE";
		sql += "                       WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                     ELSE 1 END      AS \"SumFlag\"";
		sql += "                    ,ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                    ,M.\"LoanBalance\"";
		sql += "                    ,M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                    ,M.\"YearMonth\"";
		sql += "              FROM \"MonthlyLoanBal\" M";
		sql += "              WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                AND M.\"AcctCode\" > '300'";
		sql += "                AND M.\"AcctCode\" < '390'";
		sql += "            ) S";
		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTotalEnt(TitaVo titaVo) throws Exception {

		this.info("LM014ServiceImpl getTotalEnt");
		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthIntAmtTotal\"   "; // F0
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthLoanBalTotal\"  "; // F1
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthWeightedTotal\" "; // F2
		sql += "             ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmtTotal\"    "; // F3
		sql += "             ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBalTotal\"   "; // F4
		sql += "             ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeightedTotal\"  "; // F5
		sql += "       FROM ( SELECT CASE";
		sql += "                       WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                     ELSE 1 END      AS \"SumFlag\"";
		sql += "                    ,ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                    ,M.\"LoanBalance\"";
		sql += "                    ,M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                    ,M.\"YearMonth\"";
		sql += "              FROM \"MonthlyLoanBal\" M";
		sql += "              WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                AND M.\"AcctCode\" > '300'";
		sql += "                AND M.\"AcctCode\" < '390'";
		sql += "                AND DECODE(M.\"EntCode\", '1', '1', '0') > '0'";
		sql += "            ) S";
		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTotalEntChannel(TitaVo titaVo) throws Exception {

		this.info("LM014ServiceImpl getTotal13");
		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthIntAmtTotal\"   "; // F0
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthLoanBalTotal\"  "; // F1
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthWeightedTotal\" "; // F2
		sql += "             ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmtTotal\"    "; // F3
		sql += "             ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBalTotal\"   "; // F4
		sql += "             ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeightedTotal\"  "; // F5
		sql += "       FROM ( SELECT CASE";
		sql += "                       WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                     ELSE 1 END      AS \"SumFlag\"";
		sql += "                    ,ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                    ,M.\"LoanBalance\"";
		sql += "                    ,M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                    ,M.\"YearMonth\"";
		sql += "              FROM \"MonthlyLoanBal\" M";
		sql += "              WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                AND M.\"AcctCode\" > '300'";
		sql += "                AND M.\"AcctCode\" < '390'";
		sql += "                AND NVL(M.\"DepartmentCode\",'0') = '1'";
		sql += "            ) S";
		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTotalHouse(TitaVo titaVo) throws Exception {

		this.info("LM014ServiceImpl getTotal9");
		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthIntAmtTotal\"   "; // F0
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthLoanBalTotal\"  "; // F1
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthWeightedTotal\" "; // F2
		sql += "             ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmtTotal\"    "; // F3
		sql += "             ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBalTotal\"   "; // F4
		sql += "             ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeightedTotal\"  "; // F5
		sql += "       FROM ( SELECT CASE";
		sql += "                       WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                     ELSE 1 END      AS \"SumFlag\"";
		sql += "                    ,ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                    ,M.\"LoanBalance\"";
		sql += "                    ,M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                    ,M.\"YearMonth\"";
		sql += "              FROM \"MonthlyLoanBal\" M";
		sql += "              WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                AND M.\"AcctCode\" > '300'";
		sql += "                AND M.\"AcctCode\" < '390'";
		sql += "                AND DECODE(M.\"EntCode\", '1', '1', '0') = '0'";
		sql += "            ) S";
		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getTotalNotEntChannel(TitaVo titaVo) throws Exception {

		this.info("LM014ServiceImpl getTotalNotEntChannel");
		String month = String.valueOf((titaVo.getEntDyI() + 19110000) / 100);
		String year = String.valueOf((Integer.valueOf(month)) / 100);
		String monthCounts = month.substring(4, 6);

		String sql = " SELECT SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"IntAmtRcv\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthIntAmtTotal\"   "; // F0
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"LoanBalance\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthLoanBalTotal\"  "; // F1
		sql += "             ,SUM(CASE";
		sql += "                    WHEN S.\"SumFlag\" = 0 THEN S.\"Weighted\"";
		sql += "                  ELSE 0 END)        AS \"thisMonthWeightedTotal\" "; // F2
		sql += "             ,SUM(S.\"IntAmtRcv\")      AS \"thisYearIntAmtTotal\"    "; // F3
		sql += "             ,SUM(S.\"LoanBalance\") / :monthCounts AS \"thisYearLoanBalTotal\"   "; // F4
		sql += "             ,SUM(S.\"Weighted\")  / :monthCounts   AS \"thisYearWeightedTotal\"  "; // F5
		sql += "       FROM ( SELECT CASE";
		sql += "                       WHEN M.\"YearMonth\" = :month THEN 0";
		sql += "                     ELSE 1 END      AS \"SumFlag\"";
		sql += "                    ,ROUND(M.\"LoanBalance\" * M.\"StoreRate\" / 1200 , 0)  AS \"IntAmtRcv\"";
		sql += "                    ,M.\"LoanBalance\"";
		sql += "                    ,M.\"LoanBalance\" * M.\"StoreRate\"   AS \"Weighted\"";
		sql += "                    ,M.\"YearMonth\"";
		sql += "              FROM \"MonthlyLoanBal\" M";
		sql += "              WHERE TRUNC(M.\"YearMonth\" / 100) = :year";
		sql += "                AND M.\"AcctCode\" > '300'";
		sql += "                AND M.\"AcctCode\" < '390'";
		sql += "                AND NVL(M.\"DepartmentCode\",'0') = '0'";
		sql += "            ) S";
		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("monthCounts", monthCounts);
		return this.convertToMap(query.getResultList());
	}
}
