package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l597AServiceImpl")
@Repository
public class L597AServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	L5051ServiceImpl l5051ServiceImpl;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
//注意異動此邊欄位,或SQL語法 請檢查L5074,L597A,L5708
	private String slectDataHeaderName[] = { "使用表格", "合計資料", "身分證號", "案件序號", "戶號", "戶名", "交易別", "會計日", "入帳日", "入帳還款日", "暫收金額", "溢繳款", "繳期數", "還款金額", "應還期數", "應還金額", "累溢短收", "新壽攤分", "撥付金額",
			"退還金額", "經辦", "交易序號", "案件種類", "借款人戶號", "借款人戶名" };
	private String slectSummaryDataHeaderName[] = { "筆數", "合計" };
	// private String StatusCodeValue[][] = { { "4001", "入/扣帳成功", "0" }, { "4505",
	// "存款不足", "0" }, { "4508", "非委託或已終止帳戶", "0" }, { "4806", "存戶查核資料錯誤", "0" }, {
	// "4808", "無此帳戶或問題帳戶", "0" },{ "4405", "未開卡或額度不足", "0" }, { "4705", "剃除不轉帳",
	// "0" }, { "2999", "其他錯誤", "0" } };

	public String FindL5074(String sqlL597A) throws Exception {
		String sql = "SELECT COUNT(*) AS \"Count\",SUM(NVL(\"合計資料\",0)) AS \"Sum\" FROM (" + sqlL597A + ") T";
		return sql;
	}

	public String FindL597A(TitaVo titaVo, int AcDate, int IsMainFin, int State, int Detail, int ExportDateYN, String KindCode) throws LogicException {
		String sqlSelect = "";
		String sqlFrom = "";
		String sqlLeftJoin = "";
		String sqlWhere = "";
		String sqlOrder = "";
		// AcDate 會計日期(西元年月日)
		// IsMainFin 是否為最大債權,1是0否
		// State 選項 State 01:前日匯入 02:未入帳 03:待處理 04:已入帳 05:本月入帳 06:放款攤分 07:保單攤分 08:結清退還
		// 09:本月放款 10:本月保單 11:累計未退還 12:本日匯入 13:撥入筆數 14:檢核成功 15:檢核失敗 16:放款暫收
		// 17:本月放款(非最大債權機構)
		// Detail 細項 0:無,1:債協,2:調解,3:更生,4:清算
		// ExportDateYN 製檔與否 0:無,1:已製檔,2:未製檔,3:撥付製檔,4:撥付傳票,5:撥付提兌

		this.info("pKindCode  3==" + KindCode);
		String sqlCaseKindCode = "AND (";
		String sqlCaseKindCode1 = "";
		String sqlCaseKindCode3 = "";
		String sqlCaseKindCode4 = "";
		boolean havekincode = false;

		if (ExportDateYN == 3 && State == 4) {

			this.info("KindCode.length  4==" + KindCode.length());

			for (int j = 0; j < KindCode.length(); j++) {
				if (("1").equals(KindCode.substring(j, j + 1))) {
					if (sqlCaseKindCode1.isEmpty()) {
						sqlCaseKindCode1 = "\"CaseKindCode\"= '1'";
					}
				}
				if (("2").equals(KindCode.substring(j, j + 1))) {
					sqlCaseKindCode1 = "\"CaseKindCode\"= '1' OR \"CaseKindCode\" ='2'";
				}
				if (("3").equals(KindCode.substring(j, j + 1))) {
					sqlCaseKindCode3 = "\"CaseKindCode\" ='3'";
				}
				if (("4").equals(KindCode.substring(j, j + 1))) {
					sqlCaseKindCode4 = "\"CaseKindCode\" ='4'";
				}
			}
			if (!sqlCaseKindCode1.isEmpty()) {// CaseKindCode=1或2有值
				havekincode = true;
				sqlCaseKindCode += sqlCaseKindCode1;
			}
			if (havekincode && (!sqlCaseKindCode3.isEmpty() || !sqlCaseKindCode4.isEmpty())) {
				sqlCaseKindCode += " OR ";
			}
			if (!sqlCaseKindCode3.isEmpty()) {
				sqlCaseKindCode += sqlCaseKindCode3;
			}
			if (!sqlCaseKindCode4.isEmpty()) {
				if (sqlCaseKindCode3.isEmpty()) {
					sqlCaseKindCode += sqlCaseKindCode4;
				} else {
					sqlCaseKindCode += " OR " + sqlCaseKindCode4;
				}
			}
		}
		this.info("sqlCaseKindCode==" + sqlCaseKindCode);

		// 需要的欄位
		// "合計資料","身分證號","案件序號","戶號","戶名","交易別","備註","會計日","入帳日","入帳還款日","暫收金額","溢繳款","繳期數","還款金額","應還期數","應還金額","累溢短收","新壽攤分","撥付金額","退還金額","經辦","交易序號"

		// 特殊情況
		if (State == 11) {
			// 累計未退還
			// TB:銷帳檔 AcReceivable
			// AcReceivable.AcctCode 業務科目代號
			// In(
			// T21 債協暫收款－退還款
			// T22 前調暫收款－退還款
			// T23 更生暫收款－退還款
			// )
			// AcReceivable.ClsFlag=0:未銷
			// Sum(AcReceivable.RvBal 未銷餘額)

//			List<String> lAcctCode = new ArrayList<String>();
//			lAcctCode.add("T21");// T21 債協暫收款－退還款
//			lAcctCode.add("T22");// T22 前調暫收款－退還款
//			lAcctCode.add("T23");// T23 更生暫收款－退還款
//			Slice<AcReceivable> slAcReceivable = sAcReceivableService.UseL5074(0, lAcctCode, this.index, this.limit, titaVo);
//			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
//			if (lAcReceivable != null) {
//				for (AcReceivable AcReceivableVO : lAcReceivable) {
//					BigDecimal RvBal = AcReceivableVO.getRvBal();// RvBal 未銷餘額
//				}
//			}
			sqlSelect += "SELECT ";
			sqlSelect += "'AcReceivable' AS \"使用表格\",";
			sqlSelect += "acRec.\"RvBal\" AS \"合計資料\",";
			sqlSelect += " case when  nvl(m.\"CustNo\" , 0) > 9990000 then   m.\"NegCustId\" ";
			sqlSelect += "      else  c.\"CustId\" end   AS \"身分證號\",";
//			sqlSelect += "acRec.\"FacmNo\" AS \"案件序號\",";
			sqlSelect += " m.\"CaseSeq\" AS \"案件序號\",";
			sqlSelect += " m.\"CustNo\"  AS \"戶號\",";
			sqlSelect += " case when  nvl(m.\"CustNo\" , 0) > 9990000 then   m.\"NegCustName\" ";
			sqlSelect += "      else  c.\"CustName\" end AS \"戶名\",";
			sqlSelect += "' ' AS \"交易別\",";
			sqlSelect += "'' AS \"會計日\",";
			sqlSelect += "acRec.\"OpenAcDate\" AS \"入帳日\",";
			sqlSelect += "'' AS \"入帳還款日\",";
			sqlSelect += "'' AS \"暫收金額\",";
			sqlSelect += "'' AS \"溢繳款\",";
			sqlSelect += "'' AS \"繳期數\",";
			sqlSelect += "'' AS \"還款金額\",";
			sqlSelect += "'' AS \"應還期數\",";
			sqlSelect += "'' AS \"應還金額\",";
			sqlSelect += "'' AS \"累溢短收\",";
			sqlSelect += "acRec.\"RvBal\" AS \"新壽攤分\",";
			sqlSelect += "'' AS \"撥付金額\",";
			sqlSelect += "'' AS \"退還金額\",";
			sqlSelect += "acRec.\"TitaTlrNo\" AS \"經辦\",";
			sqlSelect += "acRec.\"TitaTxtNo\" AS \"交易序號\", ";
			sqlSelect += " m.\"CaseKindCode\" AS \"案件種類\",";
			sqlSelect += " case when  nvl(m.\"CustLoanKind\", ' ') = '3'  then   to_char(c.\"CustNo\") ";//保證人的借款戶
			sqlSelect += "      else ' '  end AS \"借款人戶號\",";
			sqlSelect += " case when  nvl(m.\"CustLoanKind\", ' ') = '3'  then   to_char(c.\"CustName\") ";//保證人的借款戶
			sqlSelect += "      else ' '  end AS \"借款人戶名\"";

			sqlFrom += " FROM \"AcReceivable\" acRec ";

			sqlLeftJoin += "LEFT JOIN \"CustMain\" c ON acRec.\"CustNo\"= c.\"CustNo\" ";
			sqlLeftJoin += "LEFT JOIN \"NegMain\" m ON m.\"CustNo\"=  ";
			sqlLeftJoin += "                           case when  NVL(JSON_VALUE(acRec.\"JsonFields\", '$.NegCustNo'), 0)  > 0   ";
			sqlLeftJoin += "                                      then to_number(NVL(JSON_VALUE(acRec.\"JsonFields\", '$.NegCustNo'), 0) )  ";
			sqlLeftJoin += "                                else  acRec.\"CustNo\" end  ";
			sqlLeftJoin += "                       AND m.\"CaseSeq\" = (SELECT MAX(\"CaseSeq\") FROM \"NegMain\" ";
			sqlLeftJoin += "                                             WHERE \"CustNo\" =  ";
			sqlLeftJoin += "                           case when  NVL(JSON_VALUE(acRec.\"JsonFields\", '$.NegCustNo'), 0)  > 0   ";
			sqlLeftJoin += "                                      then to_number(NVL(JSON_VALUE(acRec.\"JsonFields\", '$.NegCustNo'), 0))   ";
			sqlLeftJoin += "                                else  acRec.\"CustNo\" end   )";
			sqlLeftJoin += "                       AND m.\"Status\" = '3' ";//已結案
			sqlLeftJoin += "                       AND m.\"ThisAcDate\" = acRec.\"OpenAcDate\" ";

			sqlWhere += "WHERE 1=1 ";
			sqlWhere += "AND acRec.\"ClsFlag\"='0' ";
//			sqlWhere += "AND acRec.\"AcctCode\" IN ('T21','T22','T23') ";
			sqlWhere += "AND acRec.\"AcctCode\" IN ('TAV') ";
			sqlWhere += "AND acRec.\"RvBal\">0 ";
			sqlWhere += "AND acRec.\"RvNo\" = ' ' ";
			sqlWhere += "AND nvl(m.\"CustNo\" , 0)  > 0   ";

			sqlOrder += "";
		} else if (State == 13 || State == 14 || State == 15 ) {
			// 撥入筆數 13
			// 檢核成功 14
			// 檢核失敗 15
			sqlSelect += "SELECT ";
			sqlSelect += "'NegAppr02' AS \"使用表格\",";
			sqlSelect += "NegAp02.\"TxAmt\" AS \"合計資料\",";
			sqlSelect += "NegAp02.\"CustId\" AS \"身分證號\",";
			sqlSelect += "m.\"CaseSeq\" AS \"案件序號\",";
			sqlSelect += "NegAp02.\"CustNo\" AS \"戶號\",";
			sqlSelect += " case when NegAp02.\"CustNo\" > 9990000 then  m.\"NegCustName\" ";
			sqlSelect += "      else  c.\"CustName\" end AS \"戶名\",";
			sqlSelect += "' ' AS \"交易別\",";
			sqlSelect += "NegAp02.\"AcDate\" AS \"會計日\",";
			sqlSelect += "NegAp02.\"BringUpDate\" AS \"入帳日\",";
			sqlSelect += "'' AS \"入帳還款日\",";
			sqlSelect += "'' AS \"暫收金額\",";
			sqlSelect += "'' AS \"溢繳款\",";
			sqlSelect += "'' AS \"繳期數\",";
			sqlSelect += "NegAp02.\"TxAmt\" AS \"還款金額\",";
			sqlSelect += "'' AS \"應還期數\",";
			sqlSelect += "'' AS \"應還金額\",";
			sqlSelect += "'' AS \"累溢短收\",";
			sqlSelect += "'' AS \"新壽攤分\",";
			sqlSelect += "'' AS \"撥付金額\",";
			sqlSelect += "'' AS \"退還金額\",";
			sqlSelect += "'' AS \"經辦\",";
			sqlSelect += "'' AS \"交易序號\",";
			sqlSelect += " m.\"CaseKindCode\" AS \"案件種類\",";
			sqlSelect += " case when  nvl(m.\"CustLoanKind\", ' ') = '3'  then   to_char(m.\"PayerCustNo\") ";//保證人的借款戶
			sqlSelect += "      else ' '  end AS \"借款人戶號\",";
			sqlSelect += " case when  nvl(m.\"CustLoanKind\", ' ') = '3'  then   to_char(nvl(c2.\"CustName\",'')) ";//保證人的借款戶
			sqlSelect += "      else ' '  end AS \"借款人戶名\"";

			sqlFrom += "FROM \"NegAppr02\" NegAp02 ";

			sqlLeftJoin += "LEFT JOIN \"CustMain\" c ON NegAp02.\"CustId\"= c.\"CustId\" ";
			sqlLeftJoin += "LEFT JOIN \"NegMain\" m ON m.\"CustNo\"= NegAp02.\"CustNo\" ";
			sqlLeftJoin += "                       AND m.\"CaseSeq\" = (SELECT MAX(\"CaseSeq\") FROM \"NegMain\" ";
			sqlLeftJoin += "                                             WHERE \"CustNo\" =  NegAp02.\"CustNo\"    )";
			sqlLeftJoin += "LEFT JOIN \"CustMain\" c2 ON c2.\"CustNo\"= nvl(m.\"PayerCustNo\",'0') ";
			sqlLeftJoin += "                         and m.\"CustLoanKind\" = '3' ";

			sqlWhere += "WHERE 1=1 ";

			if (State == 18) {// 暫收解入:已做L4002整批入帳入一筆總金額到專戶,18已取消
//				sqlWhere += "AND NegAp02.\"AcDate\" > 0 ";
//				sqlWhere += "AND NegAp02.\"StatusCode\" IN ('4001') ";
//				sqlWhere += "AND NegAp02.\"TxStatus\" = 0 ";
			} else {
				sqlWhere += "AND NegAp02.\"BringUpDate\" = (SELECT MAX(\"BringUpDate\") FROM \"NegAppr02\" WHERE \"BringUpDate\" > 0  )";
			}
			
			if (State == 13) {
				// 撥入筆數 13
			} else if (State == 14) {
				// 檢核成功 14
				sqlWhere += "AND NegAp02.\"StatusCode\" IN ('4001') ";
			} else if (State == 15) {
				// 檢核失敗 15
				sqlWhere += "AND NVL(NegAp02.\"StatusCode\",'*')!='*' AND NegAp02.\"StatusCode\" NOT IN ('4001') ";
			}

			sqlOrder += "ORDER BY NegAp02.\"BringUpDate\" , NegAp02.\"CustId\"";
		} else {
			// 債協
			sqlSelect += "SELECT ";
			sqlSelect += "'NegTrans' AS \"使用表格\",";

			String SumSql = "";
			if (State == 6 || State == 7 || State == 8 || State == 9 || State == 10) {
				if (State == 8) {
					SumSql = "NegTran.\"ReturnAmt\" AS \"合計資料\",";// 退還金額
				} else {
					// (State==6 || State==7 || State == 9 || State == 10)
					SumSql = "NegTran.\"SklShareAmt\" AS \"合計資料\",";// 新壽攤分
				}
			} else {
				if (Detail == 0) {
					SumSql = "NegTran.\"TxAmt\" AS \"合計資料\",";
				} else {
					SumSql = "NegTran.\"ApprAmt\" AS \"合計資料\",";
				}
			}

			sqlSelect += SumSql;
			sqlSelect += "case when  m.\"CustNo\" > 9990000 then  m.\"NegCustId\" ";
			sqlSelect += "     else c.\"CustId\" end AS \"身分證號\",";
			sqlSelect += "NegTran.\"CaseSeq\" AS \"案件序號\",";
			sqlSelect += "NegTran.\"CustNo\" AS \"戶號\",";
			sqlSelect += " case when  m.\"CustNo\" > 9990000 then   m.\"NegCustName\" ";
			sqlSelect += "      else  c.\"CustName\" end AS \"戶名\",";
			sqlSelect += "NegTran.\"TxKind\" AS \"交易別\",";
			sqlSelect += "NegTran.\"AcDate\" AS \"會計日\",";
			sqlSelect += "NegTran.\"EntryDate\" AS \"入帳日\",";
			sqlSelect += "NegTran.\"RepayDate\" AS \"入帳還款日\",";
			sqlSelect += "NegTran.\"TxAmt\" AS \"暫收金額\",";
			sqlSelect += "(NVL(NegTran.\"OverAmt\",0)-NVL(NegTran.\"OverRepayAmt\",0)) AS \"溢繳款\","; // 轉入溢收金額 - 溢收抵繳金
			sqlSelect += "NegTran.\"RepayPeriod\" AS \"繳期數\",";
			sqlSelect += "(NVL(NegTran.\"PrincipalAmt\",0)+NVL(NegTran.\"InterestAmt\",0)) AS \"還款金額\",";
			sqlSelect += "NegTran.\"ShouldPayPeriod\" AS \"應還期數\",";
			sqlSelect += "(NegTran.\"DueAmt\" * NegTran.\"ShouldPayPeriod\") AS \"應還金額\",";
			sqlSelect += "NegTran.\"AccuOverAmt\" AS \"累溢短收\",";
			sqlSelect += "NegTran.\"SklShareAmt\" AS \"新壽攤分\",";
			sqlSelect += "NegTran.\"ApprAmt\" AS \"撥付金額\",";
			sqlSelect += "NegTran.\"ReturnAmt\" AS \"退還金額\",";
			sqlSelect += "NegTran.\"TitaTlrNo\" AS \"經辦\",";
			sqlSelect += "NegTran.\"TitaTxtNo\" AS \"交易序號\",";
			sqlSelect += " m.\"CaseKindCode\" AS \"案件種類\",";
			sqlSelect += " case when  nvl(m.\"CustLoanKind\", ' ') = '3'  then   to_char(m.\"PayerCustNo\") ";//保證人的借款戶
			sqlSelect += "      else ' '  end AS \"借款人戶號\",";
			sqlSelect += " case when  nvl(m.\"CustLoanKind\", ' ') = '3'  then   to_char(nvl(c2.\"CustName\",'')) ";//保證人的借款戶
			sqlSelect += "      else ' '  end AS \"借款人戶名\"";

			sqlFrom += "FROM \"NegTrans\" NegTran ";

			sqlLeftJoin += "LEFT JOIN \"CustMain\" c ON NegTran.\"CustNo\"= c.\"CustNo\" ";
			sqlLeftJoin += "LEFT JOIN \"NegMain\" m ON m.\"CustNo\"=NegTran.\"CustNo\" AND m.\"CaseSeq\"=NegTran.\"CaseSeq\"";
			sqlLeftJoin += "LEFT JOIN \"CustMain\" c2 ON c2.\"CustNo\"= nvl(m.\"PayerCustNo\",'0') ";
			sqlLeftJoin += "                         and m.\"CustLoanKind\" = '3' ";

			sqlWhere += "WHERE 1=1 ";
			// IsMainFin 是否為最大債權,1是0否
			if (IsMainFin == 1) {
				sqlWhere += "AND m.\"IsMainFin\"='Y' ";
			} else if (IsMainFin == 0) {
				sqlWhere += "AND m.\"IsMainFin\"='N' ";
			}
			if (State == 1) {
				// 01:前日匯入 =>不要顯示在L5074上,故暫把日期改為反方向就不會有資料
				// 未入帳+待處理
				sqlWhere += "AND NegTran.\"TxStatus\"!='2' ";// TxStatus 0:未入帳 1:待處理 2:已入帳
				sqlWhere += "AND NegTran.\"EntryDate\" > :AcDate "; // 入帳日期 < 會計日
			} else if (State == 2) {
				// 02:未入帳
				sqlWhere += "AND NegTran.\"TxStatus\"='0' ";// TxStatus 0:未入帳 1:待處理 2:已入帳
				// 未入帳
				// 日期小於今天才要進來
				if (IsMainFin == 1) { // 新增: 最大債權有區分前日及本日
					sqlWhere += "AND NegTran.\"AcDate\" < :AcDate ";
				}
			} else if (State == 3) {
				// 03:待處理
				sqlWhere += "AND NegTran.\"TxStatus\" = '1' ";// TxStatus 0:未入帳 1:待處理 2:已入帳
			} else if (State == 4) {
				// 04:已入帳
				// NegTrans->TxStatus交易狀態=2
				// 根據入帳還款日期 RepayDate為Tita會計日期
				// Sum(TxAmt 交易金額)
				if (Detail == 0 && ExportDateYN == 0) { // 本日入帳,非已製檔未製檔
					sqlWhere += "AND NegTran.\"RepayDate\" = :AcDate ";
				} else {
					if (ExportDateYN == 1 && Detail == 1) { // 已製檔須找Negappr製檔日日期最大的那筆顯示資料,其中債權含一分二分
						sqlWhere += "AND NegTran.\"ExportDate\" = (SELECT MAX(\"ExportDate\") FROM \"NegAppr\" WHERE \"ExportMark\" = 1 AND (\"KindCode\" = 1 OR  \"KindCode\" = 2) )";
					} else if (ExportDateYN == 1 && (Detail == 2 || Detail == 3 || Detail == 4)) {
						sqlWhere += "AND NegTran.\"ExportDate\" = (SELECT MAX(\"ExportDate\") FROM \"NegAppr\" WHERE \"ExportMark\" = 1 AND \"KindCode\" = " + Integer.toString(Detail) + " )";
					}
				}

				sqlWhere += "AND NegTran.\"TxStatus\"='2' ";// TxStatus 0:未入帳 1:待處理 2:已入帳
			} else if (State == 5 || State == 9 || State == 10 || State == 6 || State == 7 || State == 8) {
				if (State == 5 || State == 9 || State == 10) {
					// 05:本月入帳
					// 根據入帳還款日期 RepayDate為 Tita會計日期同月資料
					sqlWhere += "AND NegTran.\"RepayDate\" Between :ThisMothStart AND :ThisMothEnd ";
					// 09:本月放款
					// 入帳還款日期 RepayDate為 Tita會計日期(本月)
					// CustMain:CustTypeCode 客戶別不為[10 保貸戶]
					// Sum(SklShareAmt 新壽攤分)

					// 10:本月保單:
					// 入帳還款日期 RepayDate為 Tita會計日期(本月)
					// CustMain:CustTypeCode 客戶別為[10 保貸戶]
					// Sum(SklShareAmt 新壽攤分)
				} else if (State == 6 || State == 7 || State == 8) {
					// 放款攤分
					// 入帳還款日期 RepayDate等於 Tita會計日期
					// CustMain:CustTypeCode 客戶別不為[10 保貸戶]
					// Sum(SklShareAmt 新壽攤分)

					// 保單攤分:
					// 入帳還款日期 RepayDate等於 Tita會計日期
					// CustMain:CustTypeCode 客戶別為[10 保貸戶]
					// Sum(SklShareAmt 新壽攤分)

					// 結清退還
					// 入帳還款日期 RepayDate等於 Tita會計日期
					// Sum(ReturnAmt 退還金額)
					// 交易狀態 TxStatus=2:已入帳
					// TxKind=4 || 5 結清 提前清償 交易別
					sqlWhere += "AND NegTran.\"RepayDate\" = :AcDate ";
				}
				// 20201127 發現已經沒有CustMain.CustTypeCode[10 保貸戶] 用[05 保戶]取代-Jacky
				String CustTypeCode = "XX"; // 2023/1/17改為全部都劃分在放款攤分,沒有保單攤分,以XX沒有的代碼代替
				if (State == 6 || State == 9) {
					// 06:放款攤分
					// 09:本月放款
					sqlWhere += "AND c.\"CustTypeCode\" != '" + CustTypeCode + "' ";
					sqlWhere += "AND NegTran.\"SklShareAmt\" > 0 ";
				} else if (State == 7 || State == 10) {
					// 07:保單攤分
					// 10:本月保單
					sqlWhere += "AND c.\"CustTypeCode\" = '" + CustTypeCode + "' ";
				} else if (State == 8) {
					sqlWhere += "AND NegTran.\"TxStatus\" = '2' ";// 已入帳
					sqlWhere += "AND NegTran.\"TxKind\" IN ('4','5') ";// 4:結清 ,5:提前清償
					sqlWhere += "AND NegTran.\"ReturnAmt\" > 0 ";
				}
			} else if (State == 12) {
				// 12:本日匯入
				// 未入帳
				sqlWhere += "AND NegTran.\"TxStatus\" = '0' ";// TxStatus 0:未入帳 1:待處理 2:已入帳
				sqlWhere += "AND NegTran.\"AcDate\" = :AcDate ";
			} else if (State == 16) {
				// 16:放款暫收
				// TxStatus:未入帳
				// 日期小於今天才要進來
				// sqlWhere+="AND NegTran.\"TxStatus\"= '1' OR (NegTran.\"TxStatus\"= '0' AND
				// NegTran.\"AcDate\" < :AcDate) ";//TxStatus 0:未入帳 1:待處理 2:已入帳
				// 修改條件為一般債權時抓當日已入帳資料
				sqlWhere += "AND NegTran.\"RepayDate\" = :AcDate ";
				sqlWhere += "AND NegTran.\"TxStatus\" = '2' ";// TxStatus 0:未入帳 1:待處理 2:已入帳

			} else if (State == 17) {
				// 本月放款(非最大債權機構)
				// NegTrans->TxStatus交易狀態=2
				// 根據入帳還款日期 RepayDate為Tita會計日期
				// Sum(TxAmt 交易金額)
				sqlWhere += "AND NegTran.\"TxStatus\"= '2' ";
				sqlWhere += "AND NegTran.\"RepayDate\" Between :ThisMothStart AND :ThisMothEnd ";
			}

			// Detail-0:無 01:債協 02:調解 03:更生 04:清算
			if (Detail == 0) {

			} else {
				sqlWhere += "AND m.\"CaseKindCode\"='" + Detail + "' ";
//				if(Detail==1) {
//					//01:債協
//				}else if(Detail==2) {
//					//02:調解
//				}else if(Detail==3) {
//					//03:更生
//				}else if(Detail==4) {
//					//04:清算
//				}
			}
			if (ExportDateYN == 0) {
				// 無(左側)
			} else if (ExportDateYN == 1) {
				// 已製檔
				sqlWhere += "AND NVL(NegTran.\"ExportDate\",0)!=0 ";//
			} else if (ExportDateYN == 2) {
				// 未製檔
				sqlWhere += "AND NVL(NegTran.\"ExportDate\",0)=0 ";//
			} else {
				if (ExportDateYN == 3) {
					// 撥付製檔-L5707
					sqlWhere += "AND NVL(NegTran.\"RepayDate\",0)!=0 ";
					if (titaVo.isHcodeNormal()) {
						// 正向交易
						// 未製檔
						sqlWhere += "AND NVL(NegTran.\"ExportDate\",0)=0 ";

						sqlWhere += sqlCaseKindCode + ")";
					} else {
						// 逆向
						// 未製檔
						sqlWhere += "AND NVL(NegTran.\"ExportDate\",0)= :AcDate ";
					}
				} else if (ExportDateYN == 4) {
					// 撥付傳票-L5708
					if (titaVo.isHcodeNormal()) {
						// 正向交易
						// 已製檔
						sqlWhere += "AND NVL(NegTran.\"ExportDate\",0)!=0 ";
						sqlWhere += "AND NVL(NegTran.\"ExportAcDate\",0)=0 ";
					} else {
						// 逆向
						// 已製檔
						sqlWhere += "AND NVL(NegTran.\"ExportAcDate\",0)= :AcDate ";
					}
				} else if (ExportDateYN == 5) {
					// 撥付提兌
				}
			}
			//
			sqlWhere += "AND NegTran.\"TxKind\" NOT IN ('8') ";// 2023/7/5新增:不顯示在撈取資料中 =>TxKind 交易別 8:註銷

			sqlOrder += " ORDER BY case when nvl(c.\"CustId\",' ') not in (' ') then c.\"CustId\" else m.\"NegCustId\" end ,  ";
			sqlOrder += "         NegTran.\"EntryDate\" ";
		}

		String sql = sqlSelect + sqlFrom + sqlLeftJoin + sqlWhere + sqlOrder + sqlRow;
		this.info("sql==" + sql);
		return sql;
	}

	public List<Map<String, String>> FindData(int index, int limit, String sql, TitaVo titaVo, int AcDate, int IsMainFin, int State, int Detail, int ExportDateYN) throws LogicException {
		this.info("FindData");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		this.info("L597AServiceImpl sql=[" + sql + "]");
		this.info("L597AServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");

		Query query;
//		query = em.createNativeQuery(sql,L597AVo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		String strAcDate = Integer.toString(AcDate);
		if (AcDate > 0 && AcDate < 19110000) {
			strAcDate = String.valueOf(Integer.toString(AcDate + 19110000));
		}

		this.info("L597AServiceImpl strAcDate=[" + strAcDate + "]");
//		if(State==1 || State==2 || State==6 || State==7 || State==8 || State==12 || State==16 || (!titaVo.isHcodeNormal() && (ExportDateYN==3 || ExportDateYN==4))) {
		if (State == 1 || (State == 2 && (IsMainFin == 1)) || (State == 4 && Detail == 0 && ExportDateYN == 0) || State == 6 || State == 7 || State == 8 || State == 12 || State == 16
				|| (!titaVo.isHcodeNormal() && (ExportDateYN == 3 || ExportDateYN == 4))) {

			if (strAcDate != null && strAcDate.length() != 0) {
				if (Integer.parseInt(strAcDate) == 0) {
					// E5003 組建SQL語法發生問題
					throw new LogicException(titaVo, "E5003", "會計日期不應為0");
				}
			}
			query.setParameter("AcDate", strAcDate);
			this.info("L597A query AcDate=[" + strAcDate + "]");
		} else if (State == 5 || State == 9 || State == 10 || State == 17) {
			String ThisMothStart = strAcDate.substring(0, 6) + "01";
			String ThisMothEnd = strAcDate.substring(0, 6) + "31";
			query.setParameter("ThisMothStart", ThisMothStart);
			query.setParameter("ThisMothEnd", ThisMothEnd);
			this.info("L597A query ThisMothStart=[" + ThisMothStart + "]");
			this.info("L597A query ThisMothEnd=[" + ThisMothEnd + "]");
		}
		// this.info("L597AService FindData=" + query.getParameter);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}

	public List<String[]> FindL597A(List<Map<String, String>> lObject, String UseSerch) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		int slectDataHeaderNameL = 0;
		if ("L597A".equals(UseSerch)) {
			slectDataHeaderNameL = slectDataHeaderName.length;
		} else if ("L5074".equals(UseSerch)) {
			slectDataHeaderNameL = slectSummaryDataHeaderName.length;
		}
		for (Map<String, String> MapObj : lObject) {
			String row[] = new String[slectDataHeaderNameL];
			for (int i = 0; i < slectDataHeaderNameL; i++) {
				row[i] = MapObj.get("F" + String.valueOf(i));
			}
			data.add(row);
		}

		return data;
	}
}