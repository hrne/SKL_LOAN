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

	String inputReconCode;
	int inputAcDate;

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		inputReconCode = String.valueOf(titaVo.get("ReconCode")).trim();
		if (inputReconCode.equals("A7")) {
			inputReconCode = "P03";
		}
		inputAcDate = (Integer.valueOf(titaVo.get("AcDate")) + 19110000);
		this.info("ReconCode     = " + inputReconCode);
		this.info("DATEENTDY     = " + inputAcDate);
		// 匯款總傳票明細表
		String sql = " WITH TX1 AS (";
		sql += "   SELECT \"CustNo\"";
		sql += "        , \"FacmNo\"";
		sql += "        , \"BormNo\"";
		sql += "        , \"IntStartDate\"";
		sql += "        , \"IntEndDate\"";
		sql += "        , \"AcDate\"";
		sql += "        , \"TitaTlrNo\"";
		sql += "        , \"TitaTxtNo\"";
		sql += "        , \"PaidTerms\" ";
		sql += "        , \"OtherFields\" ";
		sql += "        , \"TitaTxCd\" ";
		sql += "   FROM \"LoanBorTx\"";
		sql += "   WHERE \"AcDate\" = :inputAcDate";
		sql += "     AND \"TitaHCode\" = 0";
		sql += "   GROUP BY \"CustNo\"";
		sql += "          , \"FacmNo\"";
		sql += "          , \"BormNo\"";
		sql += "          , \"IntStartDate\"";
		sql += "          , \"IntEndDate\"";
		sql += "          , \"AcDate\"";
		sql += "          , \"TitaTlrNo\"";
		sql += "          , \"TitaTxtNo\"";
		sql += "          , \"PaidTerms\"";
		sql += "          , \"OtherFields\" ";
		sql += "          , \"TitaTxCd\" ";
		sql += " )";
		sql += ", TX2 AS (";
		// 將戶號下相同計息起迄日的明細金額加總
		sql += "   SELECT \"CustNo\"";
		sql += "        , \"FacmNo\"";
		sql += "        , \"BormNo\"";
		sql += "        , \"IntStartDate\"";
		sql += "        , \"IntEndDate\"";
		sql += "        , \"AcDate\"";
		sql += "        , \"TitaTlrNo\"";
		sql += "        , \"TitaTxtNo\"";
		sql += "        , \"PaidTerms\" ";
		sql += "        , \"OtherFields\" ";
		sql += "        , \"TitaTxCd\" ";
		sql += "        , SUM(\"TxAmt\")           AS \"TxAmt\"";
		sql += "        , SUM(\"Principal\")       AS \"Principal\"";
		sql += "        , SUM(\"Interest\")        AS \"Interest\"";
		sql += "        , SUM(\"DelayInt\")        AS \"DelayInt\"";
		sql += "        , SUM(\"BreachAmt\")       AS \"BreachAmt\"";
		sql += "        , SUM(\"CloseBreachAmt\")  AS \"CloseBreachAmt\"";
		sql += "        , SUM(\"TempAmt\")         AS \"TempAmt\"";
		sql += "        , SUM(\"UnpaidInterest\"+\"UnpaidPrincipal\"+\"UnpaidCloseBreach\")       AS \"Shortfall\"";
		sql += "        , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.AcctFee'),0))  AS \"AcctFee\"";
		sql += "      , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.ModifyFee'),0)) AS \"ModifyFee\"";
		sql += "        , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.FireFee'),0)) AS \"FireFee\"";
		sql += "        , SUM(NVL(JSON_VALUE(\"OtherFields\", '$.LawFee'),0))";
		sql += "                                 AS \"LawFee\"";
		sql += "   FROM \"LoanBorTx\"";
		sql += "   WHERE \"AcDate\" = :inputAcDate";
		sql += "     AND \"TitaHCode\" = 0";
		sql += "   GROUP BY \"CustNo\"";
		sql += "          , \"FacmNo\"";
		sql += "          , \"BormNo\"";
		sql += "          , \"IntStartDate\"";
		sql += "          , \"IntEndDate\"";
		sql += "          , \"AcDate\"";
		sql += "          , \"TitaTlrNo\"";
		sql += "          , \"TitaTxtNo\"";
		sql += "          , \"PaidTerms\" ";
		sql += "          , \"OtherFields\" ";
		sql += "          , \"TitaTxCd\" ";
		sql += " )";
		sql += " SELECT BATX.\"ReconCode\" ";// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
		sql += "    , BATX.\"BatchNo\""; // 批次號碼(表頭)
		sql += "    , BATX.\"EntryDate\" ";// 匯款日
		sql += "    , BATX.\"DetailSeq\""; // 匯款序號
		sql += "    , BATX.\"RepayAmt\" "; // 匯款金額（排序用）
		sql += "    , TX2.\"TxAmt\""; // 匯款金額
		sql += "    , TX2.\"Principal\"";
		sql += "      + TX2.\"Interest\"";
		sql += "      + TX2.\"DelayInt\"";
		sql += "      + TX2.\"BreachAmt\"";
		sql += "      + TX2.\"CloseBreachAmt\"";
		sql += "      + TX2.\"AcctFee\"";
		sql += "      + TX2.\"ModifyFee\"";
		sql += "      + TX2.\"FireFee\"";
		sql += "      + TX2.\"LawFee\" ";
		sql += "      + TX2.\"TempAmt\" AS \"AcctAmt\""; // 作帳金額(A+B+D+G+H+暫收款)
		sql += "    , LPAD(BATX.\"CustNo\",7,'0')";
		sql += "      || '-'";
		sql += "      || LPAD(TX2.\"FacmNo\",3,'0')";
		sql += "      || '-'";
		sql += "      || LPAD(TX2.\"BormNo\",3,'0') AS \"CustNo\"";// 戶號
		sql += "    , CM.\"CustName\""; // 戶名
		sql += "    , TX1.\"IntStartDate\""; // 計息起日
		sql += "    , TX1.\"IntEndDate\""; // 計息迄日
		sql += "    , TX2.\"Principal\""; // 本金(A) 需扣短繳(JSON)
		sql += "    , TX2.\"Interest\""; // 利息(B) 需扣短繳(JSON)
		sql += "    , 0 AS \"TempPayAmt\" ";// 暫付款(C) TODO:待確認來源
		sql += "    , TX2.\"DelayInt\"";
		sql += "      + TX2.\"BreachAmt\"";
		sql += "      + TX2.\"CloseBreachAmt\" AS \"BreachAmt\""; // 違約金(D)
		sql += "    , CASE";
		sql += "        WHEN TX2.\"TempAmt\" < 0";
		sql += "        THEN ABS(TX2.\"TempAmt\")";
		sql += "      ELSE 0 END AS \"TempDr\""; // 暫收借(E) LoanBorTx.TempAmt值為負時放這欄(輸出時顯示正值)
		sql += "    , CASE";
		sql += "        WHEN TX2.\"TempAmt\" > 0";
		sql += "        THEN TX2.\"TempAmt\"";
		sql += "      ELSE 0 END AS \"TempCr\" ";// 暫收貸(F) LoanBorTx.TempAmt值為正時放這欄
		sql += "    , TX2.\"Shortfall\" "; // 短繳(G)\
		sql += "    , TX2.\"AcctFee\"";
		sql += "      + TX2.\"ModifyFee\"";
		sql += "      + TX2.\"FireFee\"";
		sql += "      + TX2.\"LawFee\" AS \"Fee\""; // 帳管費及其他(H) 新增欄位 (JSON)費用類放[帳管費及其他]欄位
		sql += "    , TX2.\"AcDate\" ";// 除錯時查資料用欄位
		sql += "    , TX2.\"TitaTlrNo\" ";// 除錯時查資料用欄位
		sql += "    , TX2.\"TitaTxtNo\""; // 除錯時查資料用欄位
		sql += "    , DECODE(TX1.\"TitaTxCd\", 'L3210', '999', NVL(FAC.\"AcctCode\",'999')) AS \"SortingForSubTotal\""; // 配合小計產生的排序
		sql += " 	, \"Fn_GetCdCode\"('AcctCode',FAC.\"AcctCode\") AS \"AcctItem\"";
		sql += " 	, \"Fn_GetCdCode\"('RepayType',BATX.\"RepayType\") AS \"RepayItem\"";
		sql += "    , NVL(TX1.\"PaidTerms\", 0) AS \"PaidTerms\" ";
		sql += "    , NVL(JSON_VALUE(TX1.\"OtherFields\", '$.AdvanceCloseCode'), '  ') AS \"CloseReasonCode\" ";
		sql += " FROM \"BatxDetail\" BATX";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BATX.\"CustNo\"";
		sql += " LEFT JOIN TX1 ON TX1.\"CustNo\" = BATX.\"CustNo\"";
		sql += "            AND TX1.\"AcDate\" = BATX.\"AcDate\"";
		sql += "            AND TX1.\"TitaTlrNo\" = BATX.\"TitaTlrNo\"";
		sql += "            AND TX1.\"TitaTxtNo\" = BATX.\"TitaTxtNo\"";
		sql += " LEFT JOIN TX2 ON TX2.\"CustNo\" = TX1.\"CustNo\"";
		sql += "            AND TX2.\"FacmNo\" = TX1.\"FacmNo\"";
		sql += "            AND TX2.\"BormNo\" = TX1.\"BormNo\"";
		sql += "            AND TX2.\"IntStartDate\" = TX1.\"IntStartDate\"";
		sql += "            AND TX2.\"IntEndDate\" = TX1.\"IntEndDate\"";
		sql += "            AND TX2.\"AcDate\" = TX1.\"AcDate\"";
		sql += "            AND TX2.\"TitaTlrNo\" = TX1.\"TitaTlrNo\"";
		sql += "            AND TX2.\"TitaTxtNo\" = TX1.\"TitaTxtNo\"";
		sql += "            AND TX2.\"PaidTerms\" = TX1.\"PaidTerms\" ";
		sql += "            AND TX2.\"OtherFields\" = TX1.\"OtherFields\" ";
		sql += "            AND TX2.\"TitaTxCd\" = TX1.\"TitaTxCd\" ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = TX2.\"CustNo\"";
		sql += "                      AND FAC.\"FacmNo\" = TX2.\"FacmNo\"";
		sql += " WHERE BATX.\"AcDate\" = :inputAcDate";
		sql += " AND CASE";
		sql += "       WHEN NVL(TRIM( :inputReconCode ),' ') != ' ' ";// 輸入空白時查全部
		sql += "       THEN :inputReconCode";
		sql += "     ELSE BATX.\"ReconCode\" ";
		sql += "     END = BATX.\"ReconCode\"";
		sql += " AND BATX.\"RepayCode\" = '01'"; // 匯款轉帳
		sql += " AND BATX.\"ProcStsCode\" IN ( '5'  ";// 單筆入帳
		sql += "                           , '6' "; // 批次入帳
		sql += "                           , '7') ";// 轉暫收
		

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputAcDate", inputAcDate);
		query.setParameter("inputReconCode", inputReconCode);
		return this.convertToMap(query);
	}
}
