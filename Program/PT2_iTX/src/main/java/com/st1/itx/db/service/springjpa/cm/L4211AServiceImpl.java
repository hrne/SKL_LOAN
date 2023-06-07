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

@Service("L4211AServiceImpl")
@Repository
public class L4211AServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	private String inputReconCode;
	private int inputAcDate;
//	private int reportNo = 0;
//	private int functionCode = 0;

	public List<Map<String, String>> findAll(TitaVo titaVo , int printNo) throws Exception {

		
//		printNo
//		1 匯款總傳票明細表
//		2入帳後檢核明細表
//		3 人工處理明細表
		
//		reportNo = Integer.valueOf(titaVo.get("ReportNo"));
		
		inputReconCode = String.valueOf(titaVo.get("ReconCode")).trim();

		inputAcDate = (Integer.valueOf(titaVo.get("AcDate")) + 19110000);

		this.info("L4211AServiceImpl.printNo = " + printNo);
		this.info("L4211AServiceImpl.ReconCode     = " + inputReconCode);
		this.info("L4211AServiceImpl.DATEENTDY     = " + inputAcDate);
		// 匯款總傳票明細表
		String sql = " ";

		// 總傳票
		if (printNo == 1) {

			sql += " WITH TX1 AS (";
			sql += "   SELECT \"AcDate\"";
			sql += "        , \"TitaTlrNo\"";
			sql += "        , \"TitaTxtNo\"";
			sql += "        , \"TitaTxCd\" ";
			sql += "        , \"CustNo\" ";
			sql += "        , Max(\"FacmNo\")           AS \"FacmNo\"";
			sql += "   FROM \"LoanBorTx\"";
			sql += "   WHERE \"AcDate\" = :inputAcDate";
			sql += "    AND \"RepayCode\" = '01'"; // 匯款轉帳
//			sql += "    AND TO_NUMBER(SUBSTR(\"TitaTxtNo\",1,2)) > 0"; // 整批入帳(交易序號前兩碼為批號後兩碼)
			sql += "   GROUP BY \"CustNo\"";
			sql += "          , \"AcDate\"";
			sql += "          , \"TitaTlrNo\"";
			sql += "          , \"TitaTxtNo\"";
			sql += "          , \"TitaTxCd\" ";
			sql += " )";
			sql += " SELECT BATX.\"ReconCode\" ";// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
			sql += "    , BATX.\"BatchNo\""; // 批次號碼(表頭)
			sql += "    , BATX.\"EntryDate\" ";// 匯款日
			sql += "    , BATX.\"DetailSeq\""; // 匯款序號
			sql += "    , BATX.\"RepayAmt\" "; // 匯款金額（排序用）
			sql += "    , TX2.\"AcSeq\" AS \"AcSeq\" ";
			sql += "    , TX2.\"TxAmt\""; // 匯款金額
			sql += "    , TX2.\"Principal\"";
			sql += "      + TX2.\"Interest\"";
			sql += "      + TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\"";
			sql += "      + (TX2.\"Overflow\"";
			sql += "      - TX2.\"TempAmt\")";
			sql += "      + TX2.\"FeeAmt\" AS \"AcctAmt\""; // 作帳金額(A+B+D+G+H)
			sql += "    , LPAD(NVL(TX2.\"CustNo\",BATX.\"CustNo\"),7,'0')";
			sql += "      || '-'";
			sql += "      || LPAD(NVL(TX2.\"FacmNo\",0),3,'0')";
			sql += "      || '-'";
			sql += "      || LPAD(NVL(TX2.\"BormNo\",0),3,'0') AS \"CustNo\"";// 戶號
			sql += "    , CM.\"CustName\""; // 戶名
			sql += "    , TX2.\"IntStartDate\""; // 計息起日
			sql += "    , TX2.\"IntEndDate\""; // 計息迄日
			sql += "    , TX2.\"Principal\""; // 本金(A) 需扣短繳(JSON)
			sql += "    , TX2.\"Interest\""; // 利息(B) 需扣短繳(JSON)
			sql += "    , 0 AS \"TempPayAmt\" ";// 暫付款(C) TODO:待確認來源
			sql += "    , TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\" AS \"BreachAmt\""; // 違約金(D)
			sql += "    , TX2.\"TempAmt\"   AS \"TempDr\""; // 暫收借(E)
			sql += " 	, TX2.\"Overflow\" AS \"TempCr\" ";// 暫收貸(F)
			sql += "    , TX2.\"UnpaidPrincipal\" + TX2.\"UnpaidInterest\" AS \"Shortfall\" "; // 短繳(G)
			sql += "    , TX2.\"FeeAmt\" AS \"Fee\""; // 帳管費及其他(H)
			sql += "    , TX2.\"AcDate\" ";// 除錯時查資料用欄位
			sql += "    , TX2.\"TitaTlrNo\" ";// 除錯時查資料用欄位
			sql += "    , TX2.\"TitaTxtNo\""; // 除錯時查資料用欄位
			sql += "    , CASE WHEN NVL(TX1.\"AcDate\",0) = 0 THEN '9999' ";
			sql += "    	   WHEN NVL(JSON_VALUE(BATX.\"ProcNote\", '$.FacStatus'), ' ') <> ' ' THEN '999' ";
			sql += "           WHEN TX1.\"FacmNo\" = 0 THEN '999' ";
			sql += "           ELSE NVL(FAC.\"AcctCode\",'999') END AS \"SortingForSubTotal\""; // 配合小計產生的排序
			sql += "    , CASE WHEN NVL(TX1.\"AcDate\",0) = 0 THEN '人工入帳' ";
			sql += "    	   WHEN NVL(JSON_VALUE(BATX.\"ProcNote\", '$.FacStatus'), ' ') <> ' ' THEN '暫收款' ";
			sql += "           WHEN TX1.\"FacmNo\" = 0 THEN '暫收款' ";
			sql += "           ELSE \"Fn_GetCdCode\"('AcctCode',FAC.\"AcctCode\") END AS \"AcctItem\"";
			sql += " 	, \"Fn_GetCdCode\"('RepayType',BATX.\"RepayType\") AS \"RepayItem\"";
			sql += "    , NVL(TX2.\"PaidTerms\", 0) AS \"PaidTerms\" ";
			sql += "    , LPAD(NVL(JSON_VALUE(TX2.\"OtherFields\", '$.AdvanceCloseCode'),0),2,0) AS \"CloseReasonCode\" ";
			sql += " FROM \"BatxDetail\" BATX";
			sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BATX.\"CustNo\"";
			sql += " LEFT JOIN TX1 ON TX1.\"AcDate\" = BATX.\"AcDate\"";
			sql += "            AND SUBSTR(TX1.\"TitaTxtNo\",1,2) = SUBSTR(BATX.\"BatchNo\",5,2)";
			sql += "            AND TO_NUMBER(SUBSTR(TX1.\"TitaTxtNo\",3,6)) = BATX.\"DetailSeq\"";
			sql += " LEFT JOIN \"LoanBorTx\" TX2 ";
			sql += "             ON TX2.\"AcDate\" = CASE WHEN NVL(TX1.\"AcDate\",0) = 0 ";
			sql += "									  THEN BATX.\"AcDate\"";
			sql += "				   				      ELSE TX1.\"AcDate\" END ";
			sql += "            AND TX2.\"TitaTlrNo\" = CASE WHEN NVL(TX1.\"AcDate\",0) = 0 ";
			sql += "									  THEN BATX.\"TitaTlrNo\"";
			sql += "				   				      ELSE TX1.\"TitaTlrNo\" END ";
			sql += "            AND TX2.\"TitaTxtNo\" = CASE WHEN NVL(TX1.\"AcDate\",0) = 0 ";
			sql += "									  THEN BATX.\"TitaTxtNo\"";
			sql += "				   				      ELSE TX1.\"TitaTxtNo\" END ";
			sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = TX2.\"CustNo\"";
			sql += "                      AND FAC.\"FacmNo\" = TX2.\"FacmNo\"";
			sql += "                      AND TX2.\"FacmNo\" > 0";
			sql += " WHERE BATX.\"AcDate\" = :inputAcDate";
			sql += " AND SUBSTR(BATX.\"BatchNo\",1,4) = 'BATX'     ";
			sql += " AND CASE";
			sql += "       WHEN NVL(TRIM( :inputReconCode ),' ') != ' ' ";// 輸入空白時查全部
			sql += "       THEN :inputReconCode";
			sql += "     ELSE BATX.\"ReconCode\" ";
			sql += "     END = BATX.\"ReconCode\"";
			sql += " AND BATX.\"RepayCode\" = '01'"; // 匯款轉帳
//			sql += " AND BATX.\"ProcStsCode\" <> 'D' ";// 刪除
			

		} else if (printNo == 2) {
			// 入帳後檢核明細表
			sql += " WITH TX1 AS (";
			sql += "   SELECT \"AcDate\"";
			sql += "        , \"TitaTlrNo\"";
			sql += "        , \"TitaTxtNo\"";
			sql += "        , \"TitaTxCd\" ";
			sql += "        , \"CustNo\" ";
			sql += "        , Max(\"FacmNo\")           AS \"FacmNo\"";
			sql += "   FROM \"LoanBorTx\"";
			sql += "   WHERE \"AcDate\" = :inputAcDate";
			sql += "    AND \"RepayCode\" = '01'"; // 匯款轉帳
			sql += "   GROUP BY \"CustNo\"";
			sql += "          , \"AcDate\"";
			sql += "          , \"TitaTlrNo\"";
			sql += "          , \"TitaTxtNo\"";
			sql += "          , \"TitaTxCd\" ";
			sql += " )";
			sql += " SELECT BATX.\"ReconCode\" ";// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
			sql += "    , BATX.\"BatchNo\""; // 批次號碼(表頭)
			sql += "    , BATX.\"EntryDate\" ";// 匯款日
			sql += "    , BATX.\"DetailSeq\""; // 匯款序號
			sql += "    , BATX.\"RepayAmt\" "; // 匯款金額（排序用）
			sql += "    , TX2.\"AcSeq\" AS \"AcSeq\" ";
			sql += "    , TX2.\"TxAmt\""; // 匯款金額
			sql += "    , TX2.\"Principal\"";
			sql += "      + TX2.\"Interest\"";
			sql += "      + TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\"";
			sql += "      + (TX2.\"Overflow\"";
			sql += "      - TX2.\"TempAmt\")";
			sql += "      + TX2.\"FeeAmt\" AS \"AcctAmt\""; // 作帳金額(A+B+D+G+H)
			sql += "    , LPAD(NVL(TX2.\"CustNo\",BATX.\"CustNo\"),7,'0')";
			sql += "      || '-'";
			sql += "      || LPAD(NVL(TX2.\"FacmNo\",0),3,'0')";
			sql += "      || '-'";
			sql += "      || LPAD(NVL(TX2.\"BormNo\",0),3,'0') AS \"CustNo\"";// 戶號
			sql += "    , CM.\"CustName\""; // 戶名
			sql += "    , TX2.\"IntStartDate\""; // 計息起日
			sql += "    , TX2.\"IntEndDate\""; // 計息迄日
			sql += "    , TX2.\"Principal\""; // 本金(A) 需扣短繳(JSON)
			sql += "    , TX2.\"Interest\""; // 利息(B) 需扣短繳(JSON)
			sql += "    , 0 AS \"TempPayAmt\" ";// 暫付款(C) TODO:待確認來源
			sql += "    , TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\" AS \"BreachAmt\""; // 違約金(D)
			sql += "    , TX2.\"TempAmt\"   AS \"TempDr\""; // 暫收借(E)
			sql += " 	, TX2.\"Overflow\" AS \"TempCr\" ";// 暫收貸(F)
			sql += "    , TX2.\"UnpaidPrincipal\" + TX2.\"UnpaidInterest\" AS \"Shortfall\" "; // 短繳(G)
			sql += "    , TX2.\"FeeAmt\" AS \"Fee\""; // 帳管費及其他(H)
			sql += "    , TX2.\"AcDate\" ";// 除錯時查資料用欄位
			sql += "    , TX2.\"TitaTlrNo\" ";// 除錯時查資料用欄位
			sql += "    , TX2.\"TitaTxtNo\""; // 除錯時查資料用欄位
			sql += "    , CASE WHEN NVL(JSON_VALUE(BATX.\"ProcNote\", '$.FacStatus'), ' ') <> ' ' THEN '999' ";
			sql += "           WHEN TX1.\"FacmNo\" = 0 THEN '999' ";
			sql += "           ELSE NVL(FAC.\"AcctCode\",'999') END AS \"SortingForSubTotal\""; // 配合小計產生的排序
			sql += "    , CASE WHEN NVL(JSON_VALUE(BATX.\"ProcNote\", '$.FacStatus'), ' ') <> ' ' THEN '暫收款' ";
			sql += "           WHEN TX1.\"FacmNo\" = 0 THEN '暫收款' ";
			sql += "           ELSE \"Fn_GetCdCode\"('AcctCode',FAC.\"AcctCode\") END AS \"AcctItem\"";
//			sql += " 	, \"Fn_GetCdCode\"('AcctCode',FAC.\"AcctCode\") AS \"AcctItem\"";
			sql += " 	, \"Fn_GetCdCode\"('RepayType',BATX.\"RepayType\") AS \"RepayItem\"";
			sql += "    , NVL(TX2.\"PaidTerms\", 0) AS \"PaidTerms\" ";
			sql += "    , LPAD(NVL(JSON_VALUE(TX2.\"OtherFields\", '$.AdvanceCloseCode'),0),2,0) AS \"CloseReasonCode\" ";
			sql += " FROM \"BatxDetail\" BATX";
			sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BATX.\"CustNo\"";
			sql += " LEFT JOIN TX1 ON TX1.\"AcDate\" = BATX.\"AcDate\"";
			sql += "            AND TX1.\"TitaTlrNo\" = BATX.\"TitaTlrNo\"";
			sql += "            AND TO_NUMBER(TX1.\"TitaTxtNo\") = BATX.\"TitaTxtNo\"";
			sql += " LEFT JOIN \"LoanBorTx\" TX2 ";
			sql += "             ON TX2.\"AcDate\" = TX1.\"AcDate\"";
			sql += "            AND TX2.\"TitaTlrNo\" = TX1.\"TitaTlrNo\"";
			sql += "            AND TX2.\"TitaTxtNo\" = TX1.\"TitaTxtNo\"";
			sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = TX2.\"CustNo\"";
			sql += "                      AND FAC.\"FacmNo\" = TX2.\"FacmNo\"";
			sql += "                      AND TX2.\"FacmNo\" > 0";
			sql += " WHERE BATX.\"AcDate\" = :inputAcDate";
			sql += " AND SUBSTR(BATX.\"BatchNo\",1,4) = 'BATX'     ";
			sql += " AND CASE";
			sql += "       WHEN NVL(TRIM( :inputReconCode ),' ') != ' ' ";// 輸入空白時查全部
			sql += "       THEN :inputReconCode";
			sql += "     ELSE BATX.\"ReconCode\" ";
			sql += "     END = BATX.\"ReconCode\"";
			sql += " AND BATX.\"RepayCode\" = '01'"; // 匯款轉帳
			sql += " AND BATX.\"ProcStsCode\" IN ( '5'  ";// 單筆入帳
			sql += "                           , '6' "; // 批次入帳
			sql += "                           , '7') ";// 批次人工

		} else if (printNo == 3) {

			// 人工處理明細表
			sql += " WITH TX1 AS (";
			sql += "   SELECT \"AcDate\"";
			sql += "        , \"TitaTlrNo\"";
			sql += "        , \"TitaTxtNo\"";
			sql += "        , \"TitaTxCd\" ";
			sql += "        , \"CustNo\" ";
			sql += "        , Max(\"FacmNo\")           AS \"FacmNo\"";
			sql += "   FROM \"LoanBorTx\"";
			sql += "   WHERE \"AcDate\" = :inputAcDate";
			sql += "    AND \"RepayCode\" = '01'"; // 匯款轉帳
			sql += "   GROUP BY \"CustNo\"";
			sql += "          , \"AcDate\"";
			sql += "          , \"TitaTlrNo\"";
			sql += "          , \"TitaTxtNo\"";
			sql += "          , \"TitaTxCd\" ";
			sql += " )";
			sql += " SELECT BATX.\"ReconCode\" ";// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
			sql += "    , BATX.\"BatchNo\""; // 批次號碼(表頭)
			sql += "    , BATX.\"EntryDate\" ";// 匯款日
			sql += "    , BATX.\"DetailSeq\""; // 匯款序號
			sql += "    , BATX.\"RepayAmt\" "; // 匯款金額（排序用）
			sql += "    , TX2.\"AcSeq\" AS \"AcSeq\" ";
			sql += "    , TX2.\"TxAmt\""; // 匯款金額
			sql += "    , TX2.\"Principal\"";
			sql += "      + TX2.\"Interest\"";
			sql += "      + TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\"";
			sql += "      + (TX2.\"Overflow\"";
			sql += "      - TX2.\"TempAmt\")";
			sql += "      + TX2.\"FeeAmt\" AS \"AcctAmt\""; // 作帳金額(A+B+D+G+H)
			sql += "    , LPAD(TX2.\"CustNo\",7,'0')";
			sql += "      || '-'";
			sql += "      || LPAD(TX2.\"FacmNo\",3,'0')";
			sql += "      || '-'";
			sql += "      || LPAD(TX2.\"BormNo\",3,'0') AS \"CustNo\"";// 戶號
			sql += "    , CM.\"CustName\""; // 戶名
			sql += "    , TX2.\"IntStartDate\""; // 計息起日
			sql += "    , TX2.\"IntEndDate\""; // 計息迄日
			sql += "    , TX2.\"Principal\""; // 本金(A) 需扣短繳(JSON)
			sql += "    , TX2.\"Interest\""; // 利息(B) 需扣短繳(JSON)
			sql += "    , 0 AS \"TempPayAmt\" ";// 暫付款(C) TODO:待確認來源
			sql += "    , TX2.\"DelayInt\"";
			sql += "      + TX2.\"BreachAmt\"";
			sql += "      + TX2.\"CloseBreachAmt\" AS \"BreachAmt\""; // 違約金(D)
			sql += "    , TX2.\"TempAmt\"   AS \"TempDr\""; // 暫收借(E)
			sql += " 	, TX2.\"Overflow\" AS \"TempCr\" ";// 暫收貸(F)
			sql += "    , TX2.\"UnpaidPrincipal\" + TX2.\"UnpaidInterest\" AS \"Shortfall\" "; // 短繳(G)
			sql += "    , TX2.\"FeeAmt\" AS \"Fee\""; // 帳管費及其他(H)
			sql += "    , TX2.\"AcDate\" ";// 除錯時查資料用欄位
			sql += "    , TX2.\"TitaTlrNo\" ";// 除錯時查資料用欄位
			sql += "    , TX2.\"TitaTxtNo\""; // 除錯時查資料用欄位
			sql += "    , CASE WHEN NVL(JSON_VALUE(BATX.\"ProcNote\", '$.FacStatus'), ' ') <> ' ' THEN '999' ";
			sql += "           WHEN TX1.\"FacmNo\" = 0 THEN '999' ";
			sql += "           ELSE NVL(FAC.\"AcctCode\",'999') END AS \"SortingForSubTotal\""; // 配合小計產生的排序
			sql += "    , CASE WHEN NVL(JSON_VALUE(BATX.\"ProcNote\", '$.FacStatus'), ' ') <> ' ' THEN '暫收款' ";
			sql += "           WHEN TX1.\"FacmNo\" = 0 THEN '暫收款' ";
			sql += "           ELSE \"Fn_GetCdCode\"('AcctCode',FAC.\"AcctCode\") END AS \"AcctItem\"";
			sql += " 	, \"Fn_GetCdCode\"('RepayType',BATX.\"RepayType\") AS \"RepayItem\"";
			sql += "    , NVL(TX2.\"PaidTerms\", 0) AS \"PaidTerms\" ";
			sql += "    , LPAD(NVL(JSON_VALUE(TX2.\"OtherFields\", '$.AdvanceCloseCode'),0),2,0) AS \"CloseReasonCode\" ";
			sql += " FROM \"BatxDetail\" BATX";
			sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BATX.\"CustNo\"";
			sql += " LEFT JOIN TX1 ON TX1.\"AcDate\" = BATX.\"AcDate\"";
			sql += "            AND TX1.\"TitaTlrNo\" = BATX.\"TitaTlrNo\"";
			sql += "            AND TO_NUMBER(TX1.\"TitaTxtNo\") = BATX.\"TitaTxtNo\"";		
//			TX1.\"CustNo\" = BATX.\"CustNo\"";
//			sql += "            AND 
			sql += " LEFT JOIN \"LoanBorTx\" TX2 ";
			sql += "             ON TX2.\"AcDate\" = TX1.\"AcDate\"";
			sql += "            AND TX2.\"TitaTlrNo\" = TX1.\"TitaTlrNo\"";
			sql += "            AND TX2.\"TitaTxtNo\" = TX1.\"TitaTxtNo\"";
			sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = TX2.\"CustNo\"";
			sql += "                      AND FAC.\"FacmNo\" = TX2.\"FacmNo\"";
			sql += "                      AND TX2.\"FacmNo\" > 0";
			sql += " WHERE BATX.\"AcDate\" = :inputAcDate";
			sql += " AND SUBSTR(BATX.\"BatchNo\",1,4) = 'BATX'     ";
			sql += " AND CASE";
			sql += "       WHEN NVL(TRIM( :inputReconCode ),' ') != ' ' ";// 輸入空白時查全部
			sql += "       THEN :inputReconCode";
			sql += "     ELSE BATX.\"ReconCode\" ";
			sql += "     END = BATX.\"ReconCode\"";
			sql += " AND BATX.\"RepayCode\" = '01'"; // 匯款轉帳
			sql += " AND BATX.\"ProcStsCode\" IN ( '5'  ";// 單筆入帳
			sql += "                           , '7') ";// 批次人工
			sql += " AND ( ";
			sql += " 	CASE ";
			sql += " 	  WHEN SUBSTR(TX1.\"TitaTxtNo\",1,2) = SUBSTR(BATX.\"BatchNo\",5,2)";
			sql += "       AND TO_NUMBER(SUBSTR(TX1.\"TitaTxtNo\",3,6)) = BATX.\"DetailSeq\"";
			sql += "	  THEN 1 ELSE 0 END";
			sql += "	) = 0 ";

		}
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputAcDate", inputAcDate);
		query.setParameter("inputReconCode", inputReconCode);
		return this.convertToMap(query);
	}
}
