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
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.common.data.ReportVo;

@Component("L9133Report")
@Scope("prototype")
public class L9133Report extends CommBuffer {

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

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(reportCode).setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, reportItem, "L9133_底稿_會計與主檔餘額檢核表.xlsx", "檢核表");

		// 查會計業務檢核檔
		Slice<AcAcctCheck> slAcAcctCheck = sAcAcctCheckService.findAcDate(reportDate, 0, Integer.MAX_VALUE, titaVo);
		List<AcAcctCheck> lAcAcctCheck = slAcAcctCheck == null ? null : slAcAcctCheck.getContent();

		if (lAcAcctCheck == null || lAcAcctCheck.size() == 0) {
			throw new LogicException("E0001", "會計業務檢核檔");
		}

		// 是否有差額
		boolean isDiff = false;

		int rowCursor = 0;
		int rowCursorMain = 1;
		int rowCursorUnpaid = 1;
		int rowCursorReceivable = 1;

		BigDecimal totalAcMainBal = BigDecimal.ZERO;// 會計帳餘額小計
		BigDecimal totalReceivableBal = BigDecimal.ZERO;// 銷帳檔餘額小計
		BigDecimal totalYdBal = BigDecimal.ZERO;// 前日餘額小計
		BigDecimal totalAddAmt = BigDecimal.ZERO;// 加 小計
		BigDecimal totalSubAmt = BigDecimal.ZERO;// 減 小計
		BigDecimal totalNetAmt = BigDecimal.ZERO;// 淨增減小計
		BigDecimal totalMasterBal = BigDecimal.ZERO;// 主檔餘額小計
		BigDecimal totalDiffAcAmt = BigDecimal.ZERO;// 會計檔與主檔差額
		BigDecimal totalDiffReceivableAmt = BigDecimal.ZERO;// 銷帳檔與主黨差額小計
		String tmpAcctCode = "";

		
		
		// 紀錄筆數
		int tmpCount = 0;

		// 紀錄sheet1(檢核表)科目筆數
		int tmpSheet1Count = 0;

		

		// 為sheet1(檢核表)的最後一筆列印小計 先跑一遍做筆數紀錄
		for (AcAcctCheck tAcAcctCheck : lAcAcctCheck) {
			
			
			BigDecimal total = tAcAcctCheck.getTdBal().add(tAcAcctCheck.getReceivableBal())
					.add(tAcAcctCheck.getAcctMasterBal());

			if (total.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			switch (tAcAcctCheck.getAcctCode()) {
			case "310":
			case "320":
			case "330":
			case "340":
			case "990":
				tmpSheet1Count++;

				break;
			default:
				break;
			}

		}


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
//			this.info(" rAcSubBookCode = " + rAcSubBookCode);
//			this.info(" acctCode = " + acctCode);
			if (lAcMain != null && lAcMain.size() > 0) {
				this.info(" lAcMain.size() = " + lAcMain.size());
				for (AcMain r : lAcMain) {

					this.info("inside rAcSubBookCode = " + rAcSubBookCode.toString());
					this.info("inside getAcSubBookCode = " + r.getAcSubBookCode().toString());
					this.info("result = " + rAcSubBookCode.equals(r.getAcSubBookCode()));
					if (acSubBookCode.equals(r.getAcSubBookCode())) {

						tmpFirstAcNoCode = r.getAcNoCode().substring(0, 1);
//						this.info("tmpFirstAcNoCode = " + tmpFirstAcNoCode.toString());
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

//			this.info("addAmt = " + addAmt);
//			this.info("subAmt = " + subAmt);
//			this.info("AcctCode = " + acctCode);

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
					rowCursorMain++;
					rowCursor = rowCursorMain;
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
					rowCursor = rowCursorReceivable;
					sheet = 2;
					break;

				// 銷帳科目記號ReceivableFlag = 3-未收款
				// F29 契變手續費
				// F10 帳管費/手續費
				// F12 聯貸件
				// F27 聯貸管理費
				case "F29":
				case "F10":
				case "F12":
				case "F27":
				default:
					makeExcel.setSheet("未銷帳");
					rowCursorUnpaid++;
					rowCursor = rowCursorUnpaid;
					sheet = 3;
					break;
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
			// 工作表為第三張(未銷帳)
			if (sheet == 3) {

				makeExcel.setValue(rowCursor, 1, acSubBookCode); // 區隔帳冊
				makeExcel.setValue(rowCursor, 2, acctCode + " " + acctItem); // 科目
				makeExcel.setValue(rowCursor, 3, receivableBal, "#,##0"); // 銷帳檔餘額
				makeExcel.setValue(rowCursor, 4, masterBal, "#,##0"); // 主檔餘額
				makeExcel.setValue(rowCursor, 5, diffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額
			} else {

				// 只會有shee1(檢核表)使用到小計
				if (sheet == 1) {

					// 紀錄筆數
					tmpCount++;

					// 當會計科目不一樣的時候歸零
					if (!tmpAcctCode.equals(acctCode)) {

						// 排除不是一開始就要換行
						if (tmpAcctCode != "") {

							makeExcel.setValue(rowCursor, 2, "小計");
							makeExcel.setValue(rowCursor, 3, totalAcMainBal, "#,##0"); // 業務科目或銷帳科目才顯示會計帳餘額小計
							makeExcel.setValue(rowCursor, 4, totalYdBal, "#,##0"); // 前日餘額小計
							makeExcel.setValue(rowCursor, 5, totalAddAmt, "#,##0");// 加 小計
							makeExcel.setValue(rowCursor, 6, totalSubAmt, "#,##0");// 減 小計
							makeExcel.setValue(rowCursor, 7, totalNetAmt, "#,##0");// 淨增減小計
							makeExcel.setValue(rowCursor, 8, totalReceivableBal, "#,##0"); // 銷帳檔餘額小計
							makeExcel.setValue(rowCursor, 9, totalMasterBal, "#,##0"); // 主檔餘額小計
							makeExcel.setValue(rowCursor, 10, totalDiffAcAmt, "#,##0"); // 會計檔與主檔差額小計
							makeExcel.setValue(rowCursor, 11, totalDiffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額小計

							totalAcMainBal = BigDecimal.ZERO;// 會計帳餘額小計
							totalYdBal = BigDecimal.ZERO;// 前日餘額小計
							totalAddAmt = BigDecimal.ZERO;// 加 小計
							totalSubAmt = BigDecimal.ZERO;// 減 小計
							totalNetAmt = BigDecimal.ZERO;// 淨增減小計
							totalReceivableBal = BigDecimal.ZERO;// 銷帳檔餘額小計
							totalMasterBal = BigDecimal.ZERO;// 主檔餘額小計
							totalDiffAcAmt = BigDecimal.ZERO;// 會計檔與主檔差額
							totalDiffReceivableAmt = BigDecimal.ZERO;// 銷帳檔與主黨差額小計

							// 僅為第一次的金額需小計
							totalAcMainBal = totalAcMainBal.add(acMainBal);// 會計帳餘額小計
							totalYdBal = totalYdBal.add(ydBal);// 前日餘額小計
							totalAddAmt = totalAddAmt.add(addAmt);// 加 小計
							totalSubAmt = totalSubAmt.add(subAmt);// 減 小計
							totalNetAmt = totalNetAmt.add(netAmt);// 淨增減小計
							totalReceivableBal = totalReceivableBal.add(receivableBal);// 銷帳檔餘額小計
							totalMasterBal = totalMasterBal.add(masterBal);// 主檔餘額小計
							totalDiffAcAmt = totalDiffAcAmt.add(diffAcAmt);// 會計檔與主檔差額小計
							totalDiffReceivableAmt = totalDiffReceivableAmt.add(diffReceivableAmt);// 銷帳檔與主黨差額小計

							// 有小計的時候就要多加一列
							rowCursorMain++;

							// 因應小計後下方 繼續makeExcel.setValue
							rowCursor++;
						} else {

							totalAcMainBal = totalAcMainBal.add(acMainBal);// 會計帳餘額小計
							totalYdBal = totalYdBal.add(ydBal);// 前日餘額小計
							totalAddAmt = totalAddAmt.add(addAmt);// 加 小計
							totalSubAmt = totalSubAmt.add(subAmt);// 減 小計
							totalNetAmt = totalNetAmt.add(netAmt);// 淨增減小計
							totalReceivableBal = totalReceivableBal.add(receivableBal);// 銷帳檔餘額小計
							totalMasterBal = totalMasterBal.add(masterBal);// 主檔餘額小計
							totalDiffAcAmt = totalDiffAcAmt.add(diffAcAmt);// 會計檔與主檔差額小計
							totalDiffReceivableAmt = totalDiffReceivableAmt.add(diffReceivableAmt);// 銷帳檔與主黨差額小計

						}

						tmpAcctCode = acctCode;

					} else {

						totalAcMainBal = totalAcMainBal.add(acMainBal);// 會計帳餘額小計
						totalYdBal = totalYdBal.add(ydBal);// 前日餘額小計
						totalAddAmt = totalAddAmt.add(addAmt);// 加 小計
						totalSubAmt = totalSubAmt.add(subAmt);// 減 小計
						totalNetAmt = totalNetAmt.add(netAmt);// 淨增減小計
						totalReceivableBal = totalReceivableBal.add(receivableBal);// 銷帳檔餘額小計
						totalMasterBal = totalMasterBal.add(masterBal);// 主檔餘額小計
						totalDiffAcAmt = totalDiffAcAmt.add(diffAcAmt);// 會計檔與主檔差額小計
						totalDiffReceivableAmt = totalDiffReceivableAmt.add(diffReceivableAmt);// 銷帳檔與主黨差額小計

					}
				}

				// 工作表為第一、二張(檢核表、應收應付)
				makeExcel.setValue(rowCursor, 1, acSubBookCode); // 區隔帳冊
				makeExcel.setValue(rowCursor, 2, acctCode + " " + acctItem); // 科目
				makeExcel.setValue(rowCursor, 3, acMainBal, "#,##0"); // 業務科目或銷帳科目才顯示會計帳餘額
				makeExcel.setValue(rowCursor, 4, ydBal, "#,##0"); // 前日餘額
				makeExcel.setValue(rowCursor, 5, addAmt, "#,##0"); // 加
				makeExcel.setValue(rowCursor, 6, subAmt, "#,##0"); // 減
				makeExcel.setValue(rowCursor, 7, netAmt, "#,##0"); // 淨增減
				makeExcel.setValue(rowCursor, 8, receivableBal, "#,##0"); // 銷帳檔餘額
				makeExcel.setValue(rowCursor, 9, masterBal, "#,##0"); // 主檔餘額
				makeExcel.setValue(rowCursor, 10, diffAcAmt, "#,##0"); // 會計檔與主檔差額
				makeExcel.setValue(rowCursor, 11, diffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額

				// 最後一筆的時候輸出
				// 只會有shee1(檢核表)使用到小計
				this.info("AcctCode = " + acctCode + ",tmpCount = " + tmpCount + " ==" + tmpSheet1Count);
				if (sheet == 1 && tmpCount == tmpSheet1Count) {
					rowCursor++;
					makeExcel.setValue(rowCursor, 2, "小計");
					makeExcel.setValue(rowCursor, 3, totalAcMainBal, "#,##0"); // 業務科目或銷帳科目才顯示會計帳餘額小計
					makeExcel.setValue(rowCursor, 4, totalYdBal, "#,##0"); // 前日餘額小計
					makeExcel.setValue(rowCursor, 5, totalAddAmt, "#,##0");// 加 小計
					makeExcel.setValue(rowCursor, 6, totalSubAmt, "#,##0");// 減 小計
					makeExcel.setValue(rowCursor, 7, totalNetAmt, "#,##0");// 淨增減小計
					makeExcel.setValue(rowCursor, 8, totalReceivableBal, "#,##0"); // 銷帳檔餘額小計
					makeExcel.setValue(rowCursor, 9, totalMasterBal, "#,##0"); // 主檔餘額小計
					makeExcel.setValue(rowCursor, 10, totalDiffAcAmt, "#,##0"); // 會計檔與主檔差額小計
					makeExcel.setValue(rowCursor, 11, totalDiffReceivableAmt, "#,##0"); // 銷帳檔與主檔差額小計

				}

			}
		}
		long excelNo = makeExcel.close();

		makeExcel.toExcel(excelNo);

		// "是否有差額"參數傳回交易主程式
		return isDiff;
	}

	@Override
	public void exec() throws LogicException {
	}
}
