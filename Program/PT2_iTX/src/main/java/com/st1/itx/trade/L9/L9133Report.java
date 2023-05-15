package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcAcctCheck;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.service.AcAcctCheckService;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.common.data.ReportVo;

@Component("L9133Report")
@Scope("prototype")
public class L9133Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	private AcAcctCheckService sAcAcctCheckService;

	@Autowired
	private AcMainService sAcMainService;

	@Autowired
	private MakeExcel makeExcel;

	@Autowired
	SortMapListCom sortMapListCom;

	private String reportCode = "L9133";
	private String reportItem = "會計與主檔餘額檢核表";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9133Report exec ...");

		int reportDate = Integer.parseInt(titaVo.getParam("AcDate")) + 19110000;
		String nowDate = dDateUtil.getNowStringRoc();
		String nowTime = dDateUtil.getNowStringTime();

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(reportCode).setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, reportItem, "L9133_底稿_會計與主檔餘額檢核表.xlsx", "檢核表");
		makeExcel.setFontType(5);

		makeExcel.setValue(1, 1, "程式ID：" + this.getParentTranCode());
		makeExcel.setValue(4, 6, this.showRocDate(reportDate, 0));
		makeExcel.setValue(2, 13, "日　　期：" + showBcDate(nowDate, 1));
		makeExcel.setValue(3, 13, "時　　間：" + showTime(nowTime));

		makeExcel.setSheet("應收應付");
		makeExcel.setValue(1, 1, "程式ID：" + this.getParentTranCode());
		makeExcel.setValue(4, 6, this.showRocDate(reportDate, 0));
		makeExcel.setValue(2, 13, "日　　期：" + showBcDate(nowDate, 1));
		makeExcel.setValue(3, 13, "時　　間：" + showTime(nowTime));

//		makeExcel.setSheet("未銷帳");
//		makeExcel.setValue(1, 1, "程式ID：" + this.getParentTranCode());
//		makeExcel.setValue(4, 6, this.showRocDate(reportDate, 0));
//		makeExcel.setValue(2, 13, "日　　期：" + showBcDate(nowDate, 1));
//		makeExcel.setValue(3, 13, "時　　間：" + showTime(nowTime));

		// 查會計業務檢核檔
		Slice<AcAcctCheck> slAcAcctCheck = sAcAcctCheckService.findAcDate(reportDate, 0, Integer.MAX_VALUE, titaVo);
		List<AcAcctCheck> lAcAcctCheck = slAcAcctCheck == null ? null : slAcAcctCheck.getContent();

		if (lAcAcctCheck == null || lAcAcctCheck.size() == 0) {
			throw new LogicException("E0001", "會計業務檢核檔");
		}

		// 是否有差額
		boolean isDiff = false;

//		int rowCursorMain = 1;
//		int rowCursorUnpaid = 5;
		int rowCursorReceivable = 5;

		for (AcAcctCheck tAcAcctCheck : lAcAcctCheck) {

			// 2022-03-16 智偉新增判斷:會計帳&銷帳檔&主檔 皆為0者不顯示
			BigDecimal total = tAcAcctCheck.getTdBal().add(tAcAcctCheck.getReceivableBal())
					.add(tAcAcctCheck.getAcctMasterBal());
			if (total.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			String acSubBookCode = tAcAcctCheck.getAcSubBookCode();// 區隔帳冊
			String acctCode = tAcAcctCheck.getAcctCode();// 科目
			String acctItem = tAcAcctCheck.getAcctItem();// 科目中文
			BigDecimal acMainBal = tAcAcctCheck.getTdBal();// 會計帳餘額
			BigDecimal receivableBal = tAcAcctCheck.getReceivableBal();// 銷帳檔餘額
			BigDecimal masterBal = tAcAcctCheck.getAcctMasterBal();// 主檔餘額

			BigDecimal ydBal = BigDecimal.ZERO;// 前日餘額
			BigDecimal addAmt = BigDecimal.ZERO;// 加
			BigDecimal subAmt = BigDecimal.ZERO;// 減
			BigDecimal netAmt = BigDecimal.ZERO;// 淨增減

			Slice<AcMain> sAcMain = sAcMainService.acctCodeEq(reportDate, acctCode, 0, Integer.MAX_VALUE, titaVo);
			List<AcMain> lAcMain = sAcMain == null ? null : sAcMain.getContent();

			String tmpFirstAcNoCode = "";
			String rAcSubBookCode = "";

			if (lAcMain != null && lAcMain.size() > 0) {
				this.info(" lAcMain.size() = " + lAcMain.size());
				for (AcMain r : lAcMain) {

					this.info("inside rAcSubBookCode = " + rAcSubBookCode.toString());
					this.info("inside getAcSubBookCode = " + r.getAcSubBookCode().toString());
					this.info("result = " + rAcSubBookCode.equals(r.getAcSubBookCode()));
					if (acSubBookCode.equals(r.getAcSubBookCode())) {
						tmpFirstAcNoCode = r.getAcNoCode().substring(0, 1);

						ydBal = r.getYdBal();

						// 科目開頭為1 5 6 9 借為正，貸為負
						if ("1".equals(tmpFirstAcNoCode) || "5".equals(tmpFirstAcNoCode) || "6".equals(tmpFirstAcNoCode)
								|| "9".equals(tmpFirstAcNoCode)) {
							addAmt = r.getDbAmt();
							subAmt = r.getCrAmt();
							netAmt = addAmt.subtract(subAmt);// 淨增減 (借方為正，貸方為負)
						} else {
							addAmt = r.getCrAmt();
							subAmt = r.getDbAmt();
							netAmt = addAmt.subtract(subAmt);// 淨增減 (借方為負，貸方為正)
						}

					}

				}
			}

			int sheet = 0;
			// 明細資料新的一行
			if (acctCode != null && !acctCode.isEmpty()) {
				switch (acctCode) {
				case "310":
				case "320":
				case "330":
				case "340":
				case "990":
					makeExcel.setSheet("檢核表");

//					rowCursorMain++;
					sheet = 1;
					break;
				case "TAV":
				case "TCK":
				case "TAM":
				case "TRO":
				case "TLD":
				case "TSL":
				case "T10":
				case "T11":
				case "T12":
				case "T13":
					// 銷帳科目記號ReceivableFlag = 1,2
					// F09 暫付款－火險保費
					// F25 催收款項－火險費用
					// F07 暫付法務費
					// F24 催收款項－法務費用
				case "F09":
				case "F25":
				case "F07":
				case "F24":
				case "TMI": // TMI 火險保費(已繳未解付新產)
					makeExcel.setSheet("應收應付");
					rowCursorReceivable++;
					sheet = 2;
					break;

				// 銷帳科目記號ReceivableFlag = 3-未收款
				// F29 契變手續費
				// F10 帳管費/手續費
				// F12 聯貸件
				// F27 聯貸管理費
//				case "F29":
//				case "F10":
//				case "F12":
//				case "F27":
//				default:
//					makeExcel.setSheet("未銷帳");
//					rowCursorUnpaid++;
//					sheet = 3;
//					break;
				}
			}
			BigDecimal diffAcAmt = BigDecimal.ZERO;
			BigDecimal diffReceivableAmt = BigDecimal.ZERO;

			// 有差額就把記號改為true
			if (sheet == 1 || sheet == 2) {
				if (acMainBal.subtract(masterBal).compareTo(BigDecimal.ZERO) != 0) {
					diffAcAmt = acMainBal.subtract(masterBal); // 差額 (會計檔餘額-主檔餘額)
					if (sheet == 1) {
						isDiff = true;
					}
				}
			}

			if (!"TMI".equals(acctCode) && receivableBal.subtract(masterBal).compareTo(BigDecimal.ZERO) != 0) {
				diffReceivableAmt = receivableBal.subtract(masterBal); // 銷帳檔與主檔差額
				if (sheet == 1) {
					isDiff = true;
				}
			}

			// 工作表:檢核表
			if (sheet == 1) {
				// 310 短期擔保放款
				// 320中期擔保放款
				// 330長期擔保放款
				// 340三十年房屋貸款
				// 990催收款項
				int row = 0;

				switch (acctCode) {
				case "310":
					row = 6;
					break;
				case "320":
					row = 10;
					break;
				case "330":
					row = 14;
					break;
				case "340":
					row = 18;
					break;
				case "990":
					row = 22;
					break;
				default:
					break;
				}

				if ("201".equals(acSubBookCode)) {
					acSubBookCode = acSubBookCode + " 利變帳冊";
					row++;
				} else {
					acSubBookCode = acSubBookCode + " 一般帳冊";
				}

				makeExcel.setValue(row, 1, acctCode + " " + acctItem); // 科目
				makeExcel.setValue(row, 2, acSubBookCode); // 區隔帳冊
				makeExcel.setValue(row, 3, acMainBal, "#,##0"); // 業務科目或銷帳科目才顯示會計帳餘額
				makeExcel.setValue(row, 5, ydBal, "#,##0"); // 前日餘額
				makeExcel.setValue(row, 6, addAmt, "#,##0"); // 加
				makeExcel.setValue(row, 7, subAmt, "#,##0"); // 減
				makeExcel.setValue(row, 8, netAmt, "#,##0"); // 淨增減
				makeExcel.setValue(row, 9, receivableBal, "#,##0"); // 銷帳檔餘額
				makeExcel.setValue(row, 11, masterBal, "#,##0"); // 主檔餘額
				makeExcel.setValue(row, 12, diffAcAmt, "#,##0"); // 會計檔與主檔差額
				makeExcel.setValue(row, 13, diffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額
			} // if

			// 工作表:應收應付
			if (sheet == 2) {

				if ("201".equals(acSubBookCode)) {
					acSubBookCode = acSubBookCode + " 利變帳冊";

				} else {
					acSubBookCode = acSubBookCode + " 一般帳冊";
				}

				makeExcel.setValue(rowCursorReceivable, 1, acctCode + " " + acctItem); // 科目
				makeExcel.setValue(rowCursorReceivable, 2, acSubBookCode); // 區隔帳冊
				makeExcel.setValue(rowCursorReceivable, 3, acMainBal, "#,##0"); // 業務科目或銷帳科目才顯示會計帳餘額
				makeExcel.setValue(rowCursorReceivable, 5, ydBal, "#,##0"); // 前日餘額
				makeExcel.setValue(rowCursorReceivable, 6, addAmt, "#,##0"); // 加
				makeExcel.setValue(rowCursorReceivable, 7, subAmt, "#,##0"); // 減
				makeExcel.setValue(rowCursorReceivable, 8, netAmt, "#,##0"); // 淨增減
				makeExcel.setValue(rowCursorReceivable, 9, receivableBal, "#,##0"); // 銷帳檔餘額
				makeExcel.setValue(rowCursorReceivable, 11, masterBal, "#,##0"); // 主檔餘額
				makeExcel.setValue(rowCursorReceivable, 12, diffAcAmt, "#,##0"); // 會計檔與主檔差額
				makeExcel.setValue(rowCursorReceivable, 13, diffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額
			} // if

			// 工作表:未銷帳
//			if (sheet == 3) {
//
//				if ("201".equals(acSubBookCode)) {
//					acSubBookCode = acSubBookCode + " 利變帳冊";
//
//				} else {
//					acSubBookCode = acSubBookCode + " 一般帳冊";
//				}
//
//				makeExcel.setValue(rowCursorUnpaid, 1, acctCode + " " + acctItem); // 科目
//				makeExcel.setValue(rowCursorUnpaid, 2, acSubBookCode); // 區隔帳冊
//				makeExcel.setValue(rowCursorUnpaid, 3, acMainBal, "#,##0"); // 業務科目或銷帳科目才顯示會計帳餘額
//				makeExcel.setValue(rowCursorUnpaid, 5, ydBal, "#,##0"); // 前日餘額
//				makeExcel.setValue(rowCursorUnpaid, 6, addAmt, "#,##0"); // 加
//				makeExcel.setValue(rowCursorUnpaid, 7, subAmt, "#,##0"); // 減
//				makeExcel.setValue(rowCursorUnpaid, 8, netAmt, "#,##0"); // 淨增減
//				makeExcel.setValue(rowCursorUnpaid, 9, receivableBal, "#,##0"); // 銷帳檔餘額
//				makeExcel.setValue(rowCursorUnpaid, 11, masterBal, "#,##0"); // 主檔餘額
//				makeExcel.setValue(rowCursorUnpaid, 12, diffAcAmt, "#,##0"); // 會計檔與主檔差額
//				makeExcel.setValue(rowCursorUnpaid, 13, diffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額
//			} // if

		} // for

		makeExcel.setSheet("檢核表");
		for (int col = 3; col <= 13; col++) {
			makeExcel.formulaCaculate(8, col);
			makeExcel.formulaCaculate(12, col);
			makeExcel.formulaCaculate(16, col);
			makeExcel.formulaCaculate(20, col);
			makeExcel.formulaCaculate(24, col);
		}

		long excelNo = makeExcel.close();

		makeExcel.toExcel(excelNo);

		// "是否有差額"參數傳回交易主程式
		return isDiff;
	}

}
