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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4455RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4455ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int funcd) throws Exception {

		int RepayBank = parse.stringToInteger(titaVo.getParam("RepayBank"));
		String BatchNo = titaVo.getParam("BatchNo");
		int AcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int EntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;

		String sql = "  WITH TX1 AS (";
		sql += "    SELECT \"CustNo\"";
		sql += "         , \"FacmNo\"";
		sql += "         , \"BormNo\"";
		sql += "         , \"IntStartDate\"";
		sql += "         , \"IntEndDate\"";
		sql += "         , \"AcDate\"";
		sql += "         , \"TitaTlrNo\"";
		sql += "         , \"TitaTxtNo\"";
		sql += "    FROM \"LoanBorTx\"";
		sql += "    WHERE \"AcDate\" = :inputAcDate";
		sql += "      AND \"EntryDate\" = :inputEntryDate";
		sql += "      AND \"TitaHCode\" = 0";
		sql += "    GROUP BY \"CustNo\"";
		sql += "           , \"FacmNo\"";
		sql += "           , \"BormNo\"";
		sql += "           , \"IntStartDate\"";
		sql += "           , \"IntEndDate\"";
		sql += "           , \"AcDate\"";
		sql += "           , \"TitaTlrNo\"";
		sql += "           , \"TitaTxtNo\"";
		sql += "  )";
		sql += "  , TX2 AS (";
		sql += "    SELECT \"CustNo\"";
		sql += "         , \"FacmNo\"";
		sql += "         , \"BormNo\"";
		sql += "         , \"IntStartDate\"";
		sql += "         , \"IntEndDate\"";
		sql += "         , \"AcDate\"";
		sql += "         , \"TitaTxCd\"";
		sql += "         , \"TitaTlrNo\"";
		sql += "         , \"TitaTxtNo\"";
		sql += "         , SUM(\"TxAmt\")           AS \"TxAmt\"";// 交易金額
		sql += "         , SUM(\"Principal\")       AS \"Principal\"";// 本金
		sql += "         , SUM(\"Interest\")        AS \"Interest\"";// 利息
		sql += "         , SUM(\"DelayInt\")        AS \"DelayInt\"";// 延遲息
		sql += "         , SUM(\"BreachAmt\")       AS \"BreachAmt\"";// 違約金
		sql += "         , SUM(\"CloseBreachAmt\")  AS \"CloseBreachAmt\"";// 清償違約金
		sql += "         , SUM(\"FeeAmt\")  AS \"FeeAmt\"";// 費用
		sql += "         , SUM( \"TempAmt\" )         AS \"TempAmt\"";// 暫收借
		sql += "         , SUM(\"Overflow\")         AS \"Overflow\"";// 暫收貸
		sql += "    	 , SUM(\"UnpaidPrincipal\" + \"UnpaidInterest\" + \"UnpaidCloseBreach\") AS \"Shortfall\" "; // 短繳(G)
		sql += "         , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.AcctFee'),0))";
		sql += "                                  AS \"AcctFee\"";
		sql += "         , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.ModifyFee'),0))";
		sql += "                                  AS \"ModifyFee\"";
		sql += "         , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.FireFee'),0))";
		sql += "                                  AS \"FireFee\"";
		sql += "         , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.LawFee'),0))";
		sql += "                                  AS \"LawFee\"";
		sql += "    FROM \"LoanBorTx\"";
		sql += "    WHERE \"AcDate\" = :inputAcDate";
		sql += "      AND \"EntryDate\" = :inputEntryDate";
		sql += "      AND \"TitaHCode\" = 0";
		sql += "    GROUP BY \"CustNo\"";
		sql += "           , \"FacmNo\"";
		sql += "           , \"BormNo\"";
		sql += "           , \"IntStartDate\"";
		sql += "           , \"IntEndDate\"";
		sql += "           , \"AcDate\"";
		sql += "           , \"TitaTxCd\"";
		sql += "           , \"TitaTlrNo\"";
		sql += "           , \"TitaTxtNo\"";
		sql += "  )";
		sql += "  SELECT BD.\"BatchNo\" AS \"BatchNo\"";
		sql += "     , BKD.\"EntryDate\"";
		sql += "     , BKD.\"RepayBank\"";
		sql += "     , BKD.\"AcctCode\"";
		sql += "     , LPAD(BKD.\"CustNo\",7,'0')";
		sql += "       || '-' || LPAD(TX1.\"FacmNo\",3,'0')";
		sql += "       || '-' || LPAD(TX1.\"BormNo\",3,'0') AS \"CustNo\"";
		sql += "     , CM.\"CustName\"";
		sql += "     , BKD.\"RepayAmt\"";

		if (funcd == 1) { // 全部
			sql += "     , TX2.\"Principal\"";
			sql += "       + TX2.\"Interest\"";
			sql += "       + TX2.\"DelayInt\"";
			sql += "       + TX2.\"BreachAmt\"";
			sql += "       + TX2.\"CloseBreachAmt\"";
			sql += "       + TX2.\"Overflow\"";
			sql += "       - TX2.\"TempAmt\"";
			sql += "       + TX2.\"FeeAmt\" AS \"AcctAmt\"";
		} else if (funcd == 2) { // 帳管 + 法拍
			sql += "     , TX2.\"Principal\"";
			sql += "       + TX2.\"Interest\"";
			sql += "       + TX2.\"DelayInt\"";
			sql += "       + TX2.\"BreachAmt\"";
			sql += "       + TX2.\"CloseBreachAmt\"";
			sql += "       + TX2.\"AcctFee\"";
			sql += "       + TX2.\"LawFee\" AS \"AcctAmt\" ";
		} else if (funcd == 3) { // 契變
			sql += "     , TX2.\"Principal\"";
			sql += "       + TX2.\"Interest\"";
			sql += "       + TX2.\"DelayInt\"";
			sql += "       + TX2.\"BreachAmt\"";
			sql += "       + TX2.\"CloseBreachAmt\"";
			sql += "       + TX2.\"ModifyFee\" AS \"AcctAmt\" ";
		} else { // 火險
			sql += "     , TX2.\"Principal\"";
			sql += "       + TX2.\"Interest\"";
			sql += "       + TX2.\"DelayInt\"";
			sql += "       + TX2.\"BreachAmt\"";
			sql += "       + TX2.\"CloseBreachAmt\"";
			sql += "       + TX2.\"FireFee\" AS \"AcctAmt\" ";
		}

		sql += "     , TX1.\"IntStartDate\"";
		sql += "     , TX1.\"IntEndDate\"";
		sql += "     , TX2.\"Principal\"";
		sql += "     , TX2.\"Interest\"";
		sql += "     , 0 AS \"TempPayAmt\" ";
		sql += "     , TX2.\"DelayInt\"";
		sql += "       + TX2.\"BreachAmt\"";
		sql += "       + TX2.\"CloseBreachAmt\" AS \"BreachAmt\" ";
		sql += "    , TX2.\"TempAmt\"   AS \"TempDr\""; // 暫收借(E)
		sql += " 	, TX2.\"Overflow\"  AS \"TempCr\" ";// 暫收貸(F)
		sql += "     , TX2.\"Shortfall\" ";
		sql += "     , TX2.\"AcDate\" ";
		sql += "     , TX2.\"TitaTlrNo\" ";
		sql += "     , TX2.\"TitaTxtNo\" ";
		sql += "     , ROW_NUMBER() OVER (PARTITION BY BD.\"BatchNo\"";
		sql += "                                     , BKD.\"EntryDate\"";
		sql += "                                     , BKD.\"RepayBank\"";
		sql += "                                     , BKD.\"CustNo\"";
		sql += "                                     , TX2.\"TitaTxtNo\"";
		sql += "                          ORDER BY BD.\"BatchNo\"";
		sql += "                                , BKD.\"EntryDate\"";
		sql += "                                , BKD.\"RepayBank\"";
		sql += "                                , BKD.\"CustNo\"";
		sql += "                                , TX1.\"FacmNo\"";
		sql += "     							, TX1.\"IntStartDate\"";
		sql += "     							, TX1.\"IntEndDate\"";
		sql += "                                , TX1.\"BormNo\"";
		sql += "                                , BKD.\"AcctCode\"";
		sql += "                                , TX2.\"TitaTxtNo\"";
		sql += "                         ) AS \"TxSeq\" ";
		sql += "  FROM \"BankDeductDtl\" BKD";
		sql += "  LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BKD.\"CustNo\"";
		sql += "  LEFT JOIN \"BatxDetail\" BD ON BD.\"AcDate\" = BKD.\"AcDate\"";
		sql += "                              AND BD.\"TitaTlrNo\" = BKD.\"TitaTlrNo\"";
		sql += "                              AND BD.\"TitaTxtNo\" = BKD.\"TitaTxtNo\"";
		sql += "  LEFT JOIN TX1 ON TX1.\"CustNo\" = BKD.\"CustNo\"";
		sql += "             AND TX1.\"AcDate\" = BKD.\"AcDate\"";
		sql += "             AND TX1.\"TitaTlrNo\" = BKD.\"TitaTlrNo\"";
		sql += "             ANd TX1.\"TitaTxtNo\" = BKD.\"TitaTxtNo\"";
		sql += "  LEFT JOIN TX2 ON TX2.\"CustNo\" = TX1.\"CustNo\"";
		sql += "             AND TX2.\"FacmNo\" = TX1.\"FacmNo\"";
		sql += "             AND TX2.\"BormNo\" = TX1.\"BormNo\"";
		sql += "             AND TX2.\"IntStartDate\" = TX1.\"IntStartDate\"";
		sql += "             AND TX2.\"IntEndDate\" = TX1.\"IntEndDate\"";
		sql += "             AND TX2.\"AcDate\" = TX1.\"AcDate\"";
		sql += "             AND TX2.\"TitaTlrNo\" = TX1.\"TitaTlrNo\"";
		sql += "             AND TX2.\"TitaTxtNo\" = TX1.\"TitaTxtNo\"";
		sql += "  WHERE BKD.\"ReturnCode\" = '00'";
		sql += "  AND CASE ";
		sql += "        WHEN LPAD(:inputBatchNo,2,'0') != '00' ";
		sql += "        THEN 'BATX'||LPAD(:inputBatchNo,2,'0')";
		sql += "      ELSE BD.\"BatchNo\"";
		sql += "      END = BD.\"BatchNo\"";

		switch (funcd) {
		case 1:
			sql += " AND bkd.\"RepayType\" NOT IN (4,5,6,7) ";
			break;
		case 2:
			sql += " AND ( bkd.\"RepayType\" = 4 or bkd.\"RepayType\" = 7 )";
			break;
		case 3:
			sql += " AND bkd.\"RepayType\" = 6 ";
			break;
		case 4:
			sql += " AND bkd.\"RepayType\" = 5 ";
			break;
		default:
			break;

		}

		if (RepayBank != 998 && RepayBank != 999) {
			sql += "       AND BKD.\"RepayBank\" = :inputRepayBank";
		} else if (RepayBank == 998) {
			sql += "       AND BKD.\"RepayBank\" <> 700";
		}

		sql += "  AND BKD.\"AcDate\" = :inputAcDate";
		sql += "  AND BKD.\"EntryDate\" = :inputEntryDate";
		sql += "  ORDER BY BD.\"BatchNo\"";
		sql += "       , BKD.\"EntryDate\"";
		sql += "       , BKD.\"RepayBank\"";
		sql += "       , BKD.\"AcctCode\"";
		sql += "       , BKD.\"CustNo\"";
		sql += "       , TX1.\"FacmNo\"";
		sql += "       , TX1.\"IntStartDate\"";
		sql += "       , TX1.\"IntEndDate\"";
		sql += "       , TX1.\"BormNo\"";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		if (RepayBank != 998 && RepayBank != 999) {
			query.setParameter("inputRepayBank", RepayBank);
		}
		query.setParameter("inputBatchNo", BatchNo);
		query.setParameter("inputAcDate", AcDate);
		query.setParameter("inputEntryDate", EntryDate);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll2(TitaVo titaVo) throws Exception {

		int EntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		int RepayBank = parse.stringToInteger(titaVo.getParam("RepayBank"));

		String sql = "SELECT BKD.\"RepayBank\"   ";
		sql += "     , CASE WHEN BKD.\"RepayType\" IN (4,5,6) THEN '456' ELSE BKD.\"AcctCode\" END AS \"AcctCode\"";
		sql += "     , BKD.\"RepayAcctNo\"  ";
		sql += "     , BKD.\"CustNo\"       ";
		sql += "     , CM.\"CustName\"      ";
		sql += "     , BKD.\"IntStartDate\" ";
		sql += "     , BKD.\"IntEndDate\"   ";
		sql += "     , BKD.\"RepayAmt\"     ";
		sql += "     , \"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1)";
		sql += "          AS \"CustTel\"    ";
		sql += "     , CASE";
		sql += "         WHEN NVL(CTN.\"RelationCode\",' ') = '00'";
		sql += "         THEN CM.\"CustName\"";
		sql += "         WHEN NVL(CTN.\"RelationCode\",' ') != ' '";
		sql += "         THEN CTN.\"LiaisonName\"";
		sql += "       ELSE N' ' ";
		sql += "       END AS \"LiaisonName\"";
		sql += "                          ";
		sql += "     , BKD.\"ReturnCode\"   ";
		sql += "     , CASE";
		sql += "         WHEN BKD.\"MediaKind\" = 3 ";
		sql += "         THEN \"Fn_GetCdCode\"('ProcCode', '003' || BKD.\"ReturnCode\")";
		sql += "       ELSE \"Fn_GetCdCode\"('ProcCode', '002' || BKD.\"ReturnCode\")";
		sql += "       END AS \"Remark\"    ";
		sql += "     , BKD.\"RelationCode\" ";
		sql += "     , CASE";
		sql += "         WHEN NVL(BKD.\"RelationCode\",'00') != '00'";
		sql += "         THEN NVL(BKD.\"RelCustId\",' ')";
		sql += "       ELSE ' ' END";
		sql += "        AS \"RelCustId\"    ";
		sql += "     , CASE";
		sql += "         WHEN NVL(BKD.\"RelationCode\",'00') != '00'";
		sql += "         THEN NVL(BKD.\"RelCustName\",N' ')";
		sql += "       ELSE N' ' END";
		sql += "        AS \"RelCustName\"  ";
		sql += "     , \"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1)";
		sql += "          AS \"CustCell\"    ";
		sql += "     , CM.\"Email\" AS \"Email\" ";
		sql += "  FROM \"BankDeductDtl\" BKD";
		sql += "  LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BKD.\"CustNo\"";
		sql += "  LEFT JOIN (SELECT TMP.\"CustUKey\"";
		sql += "                , TMP.\"TelNo\" AS \"TelNo\"";
		sql += "                , TMP.\"RelationCode\"";
		sql += "                , TMP.\"LiaisonName\"";
		sql += "                , TMP.\"Seq\"";
		sql += "           FROM (SELECT CASE";
		sql += "                          WHEN \"TelArea\" IS NOT NULL THEN \"TelArea\" || '-' ";
		sql += "                        ELSE '' END";
		sql += "                        || CASE";
		sql += "                             WHEN \"TelNo\" IS NOT NULL THEN \"TelNo\" ";
		sql += "                           ELSE '' END";
		sql += "                        || CASE";
		sql += "                             WHEN \"TelExt\" IS NOT NULL THEN '-' || \"TelExt\"  ";
		sql += "                           ELSE '' END AS \"TelNo\"";
		sql += "                      , \"CustUKey\"";
		sql += "                      , \"RelationCode\"";
		sql += "                      , \"LiaisonName\"";
		sql += "                      , ROW_NUMBER() OVER (PARTITION BY \"CustUKey\"";
		sql += "                                                      , \"TelTypeCode\"";
		sql += "                                           ORDER BY \"RelationCode\") AS \"Seq\"";
		sql += "                 FROM \"CustTelNo\"";
		sql += "                 WHERE \"TelTypeCode\" = '01'";
		sql += "                   AND \"Enable\" = 'Y'";
		sql += "                ) TMP";
		sql += "           WHERE TMP.\"Seq\" <= 1";
		sql += "          ) CTN ON CTN.\"CustUKey\" = CM.\"CustUKey\"";
		sql += "  WHERE NVL(BKD.\"ReturnCode\",'00') != '00'";
		if (RepayBank != 998 && RepayBank != 999) {
			sql += "       AND BKD.\"RepayBank\" = :inputRepayBank";
		} else if (RepayBank == 998) {
			sql += "       AND BKD.\"RepayBank\" <> 700";
		}
		sql += "  AND BKD.\"EntryDate\" = :inputEntryDate";
		sql += "  ORDER BY BKD.\"RepayBank\"    ";
		sql += "       , BKD.\"AcctCode\"     ";
		sql += "       , BKD.\"CustNo\"       ";
		sql += "       , BKD.\"IntStartDate\" ";
		sql += "       , BKD.\"IntEndDate\"   ";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("inputEntryDate", EntryDate);
		if (RepayBank != 998 && RepayBank != 999) {
			query.setParameter("inputRepayBank", RepayBank);
		}
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findSum(TitaVo titaVo) throws Exception {

		int EntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		int RepayBank = parse.stringToInteger(titaVo.getParam("RepayBank"));

		String sql = "WITH FailedData AS (";
		sql += "     SELECT COUNT(1) AS \"Counts\"";
		sql += "          , SUM(BKD.\"RepayAmt\") AS \"Total\"";
		sql += "     FROM \"BankDeductDtl\" BKD";
		sql += "     WHERE NVL(BKD.\"ReturnCode\",'00') != '00'";
		sql += "       AND BKD.\"EntryDate\" = :inputEntryDate";

		if (RepayBank != 998 && RepayBank != 999) {
			sql += "       AND BKD.\"RepayBank\" = :inputRepayBank";
		} else if (RepayBank == 998) {
			sql += "       AND BKD.\"RepayBank\" <> 700";
		}
		sql += "  )";
		sql += "  , BKDCustNo AS (";
		sql += "     SELECT COUNT(1) AS \"Counts\"";
		sql += "     FROM (";
		sql += "          SELECT \"CustNo\"";
		sql += "          		,\"RepayBank\"";
		sql += "          FROM \"BankDeductDtl\" BKD";
		sql += "          WHERE BKD.\"EntryDate\" = :inputEntryDate";
		sql += "          AND BKD.\"MediaCode\" = 'Y' ";
		if (RepayBank != 998 && RepayBank != 999) {
			sql += "       AND BKD.\"RepayBank\" = :inputRepayBank";
		} else if (RepayBank == 998) {
			sql += "       AND BKD.\"RepayBank\" <> 700";
		}

		sql += "          GROUP BY \"CustNo\"";
		sql += "          		,\"RepayBank\"";
		sql += "     )";
		sql += "  )";
		sql += "  , FailedBKDCustNo AS (";
		sql += "     SELECT COUNT(1) AS \"Counts\"";
		sql += "     FROM (";
		sql += "          SELECT \"CustNo\"";
		sql += "          FROM \"BankDeductDtl\" BKD";
		sql += "          WHERE NVL(BKD.\"ReturnCode\",'00') != '00'";
		sql += "          AND BKD.\"EntryDate\" = :inputEntryDate";
		if (RepayBank != 998 && RepayBank != 999) {
			sql += "       AND BKD.\"RepayBank\" = :inputRepayBank";
		} else if (RepayBank == 998) {
			sql += "       AND BKD.\"RepayBank\" <> 700";
		}
		sql += "          GROUP BY \"CustNo\"";
		sql += "     )";
		sql += "  )";
		sql += "  SELECT NVL(FailedData.\"Counts\",0)      AS F0 ";
		sql += "     , NVL(FailedData.\"Total\",0)       AS F1 ";
		sql += "     , NVL(BKDCustNo.\"Counts\",0)       AS F2 ";
		sql += "     , NVL(FailedBKDCustNo.\"Counts\",0) AS F3 ";
		sql += "  FROM FailedData, BKDCustNo, FailedBKDCustNo";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("inputEntryDate", EntryDate);
		if (RepayBank != 998 && RepayBank != 999) {
			query.setParameter("inputRepayBank", RepayBank);
		}
		return this.convertToMap(query);
	}

}