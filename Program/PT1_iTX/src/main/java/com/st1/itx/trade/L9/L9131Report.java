package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.SlipMedia2022Service;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")

public class L9131Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	SlipMedia2022Service sSlipMedia2022Service;

	@Autowired
	CdCodeService sCdCodeService;

	@Autowired
	CdAcCodeService sCdAcCodeService;

	private ReportVo reportVo;

	// 帳冊別
	private String nowAcBookCode = "000";
	private String nowAcBookItem = "全帳冊";
	private String slipNo = "";

	// 區隔帳冊
	private String nowAcSubBookCode = "00A";
	private String nowAcSubBookItem = "傳統帳冊";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9131Report.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + reportVo.getSecurity());
		this.print(-2, 2, "報　表：" + reportVo.getRptCode());
		this.print(-2, 85, reportVo.getRptItem(), "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "帳冊別：");
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, 85, "傳 票 日 期：" + showRocDate(reportVo.getRptDate()), "C");
		this.print(-5, 2, "區隔帳冊：");
		// 頁首帳冊別判斷
		print(-4, 10, this.nowAcBookCode);
		print(-4, 14, this.nowAcBookItem);

		// 頁首區隔帳冊
		print(-5, 12, this.nowAcSubBookCode);
		print(-5, 16, this.nowAcSubBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * -------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ----------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(1, 1, "傳票　會計科目代號　　　子目代號　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　銷帳碼／　　　　　　　　單位代號／");
		print(1, 1, "序號　科目名稱　　　　　子目名稱　　　　　　　　　　　　　　幣別　　　　借方金額　　　　　　　貸方金額　　　　　　　　　摘要說明　　　　　　　　成本單位　　　通路");
		print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－　－－－－");

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.print(-41, 1, "總　　　　　　　　　副　　　　　　　　協　　　　　　　　經　　　　　　覆　　　　　　　　　　　　　　　　　　　　　　　　　　傳票號碼：" + slipNo);
		this.print(-42, 1, "經　　　　　　　　　總　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　經辦單位");
		this.print(-43, 1, "理　　　　　　　　　經　　　　　　　　理　　　　　　　　理　　　　　　核　　　　　　　　　　　　　　　　　－－－－－－－－－－－－－－－－－－－－－");
		this.print(-44, 1, "　　　　　　　　　　理　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　協理　　　　經理　　　　經辦");
	}

	private void setReportVo() throws LogicException {
		this.reportVo = ReportVo.builder().setRptDate(Integer.valueOf(titaVo.getParam("AcDate")) + 19110000)
				.setBrno(titaVo.getBrno()).setRptCode("L9131").setRptItem("總帳日結單代傳票列印").setSecurity(this.getSecurity())
				.setRptSize("A4").setPageOrientation("L").build();
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9131Report exec ...");

		this.titaVo = titaVo;

		setReportVo();

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		int iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		Slice<SlipMedia2022> sSlipMedia2022 = sSlipMedia2022Service.findMediaSeq(reportVo.getRptDate(), iBatchNo,
				iMediaSeq, "Y", 0, Integer.MAX_VALUE, titaVo);
		List<SlipMedia2022> lSlipMedia2022 = sSlipMedia2022 == null ? null : sSlipMedia2022.getContent();

		if (lSlipMedia2022 == null || lSlipMedia2022.isEmpty()) {
			// 出空表
			this.open(titaVo, reportVo);
			this.setCharSpaces(0);
			print(1, 1, "本日無資料");
			return;
		}

		SlipMedia2022 tFirstSlipMedia2022 = lSlipMedia2022.get(0);

		// 合計容器(以借貸方區分)
		BigDecimal dbAmt = BigDecimal.ZERO;
		BigDecimal crAmt = BigDecimal.ZERO;
		String currencyCode = tFirstSlipMedia2022.getCurrencyCode();

		this.nowAcBookCode = tFirstSlipMedia2022.getAcBookCode();
		CdCode acBookCdCodeFirst = sCdCodeService.getItemFirst(6, "AcBookCode", this.nowAcBookCode, titaVo);
		this.nowAcBookItem = acBookCdCodeFirst == null ? "" : acBookCdCodeFirst.getItem();

		this.nowAcSubBookCode = tFirstSlipMedia2022.getAcSubBookCode();
		CdCode acSubBookCdCodeFirst = sCdCodeService.getItemFirst(6, "AcSubBookCode", this.nowAcSubBookCode, titaVo);
		this.nowAcSubBookItem = acSubBookCdCodeFirst == null ? "" : acSubBookCdCodeFirst.getItem();

		this.slipNo = tFirstSlipMedia2022.getSlipMedia2022Id().getMediaSlipNo();

		int thisMediaSeq = tFirstSlipMedia2022.getMediaSeq();

		reportVo.setRptItem(reportVo.getRptItem() + "_" + FormatUtil.pad9("" + thisMediaSeq, 3));

		this.open(titaVo, reportVo);

		this.setCharSpaces(0);

		for (SlipMedia2022 tSlipMedia2022 : lSlipMedia2022) {

			if (!this.nowAcBookCode.equals(tSlipMedia2022.getAcBookCode())
					|| !this.nowAcSubBookCode.equals(tSlipMedia2022.getAcSubBookCode())) {

				print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－－－－－－");
				print(1, 1, "　　　合計　TOTAL ：");

				print(0, 61, currencyCode);
				print(0, 86, formatAmt(dbAmt, 2), "R");
				print(0, 108, formatAmt(crAmt, 2), "R");
				dbAmt = BigDecimal.ZERO;
				crAmt = BigDecimal.ZERO;

				// 修改表頭的帳冊別欄位
				this.nowAcBookCode = tSlipMedia2022.getAcBookCode();
				CdCode acBookCdCode = sCdCodeService.getItemFirst(6, "AcBookCode", this.nowAcBookCode, titaVo);
				this.nowAcBookItem = acBookCdCode == null ? "" : acBookCdCode.getItem();

				// 修改表頭的區隔帳冊欄位
				this.nowAcSubBookCode = tSlipMedia2022.getAcSubBookCode();
				CdCode acSubBookCdCode = sCdCodeService.getItemFirst(6, "AcSubBookCode", this.nowAcSubBookCode, titaVo);
				this.nowAcSubBookItem = acSubBookCdCode == null ? "" : acSubBookCdCode.getItem();

				this.newPage();

				// 修改頁尾的傳票號碼欄位
				this.slipNo = tSlipMedia2022.getSlipMedia2022Id().getMediaSlipNo();
			}

			BigDecimal txAmt = tSlipMedia2022.getTxAmt();

			// 明細資料第一行
			print(1, 1, "　　");
			print(0, 1, FormatUtil.pad9(String.valueOf(tSlipMedia2022.getSeq()), 3));
			print(0, 7, tSlipMedia2022.getAcNoCode());
			print(0, 19, tSlipMedia2022.getAcSubCode());
			print(0, 61, tSlipMedia2022.getCurrencyCode());

			String receiveCode = tSlipMedia2022.getReceiveCode();

			if (receiveCode != null && !receiveCode.isEmpty()) {
				print(0, 111, tSlipMedia2022.getReceiveCode());
			}

			print(0, 141, tSlipMedia2022.getDeptCode());

			if (tSlipMedia2022.getDbCr().equals("D")) {
				print(0, 86, formatAmt(txAmt, 2), "R");
				dbAmt = dbAmt.add(txAmt);
			} else if (tSlipMedia2022.getDbCr().equals("C")) {
				print(0, 108, formatAmt(txAmt, 2), "R");
				crAmt = crAmt.add(txAmt);
			}

			String acNoItem = tSlipMedia2022.getSlipRmk();
			CdAcCodeId cdAcCodeId = new CdAcCodeId();
			cdAcCodeId.setAcNoCode(tSlipMedia2022.getAcNoCode());
			cdAcCodeId.setAcSubCode(tSlipMedia2022.getAcSubCode());
			cdAcCodeId.setAcDtlCode("  ");
			CdAcCode sCdAcCode= sCdAcCodeService.findById(cdAcCodeId, titaVo);
			// 明細資料第二行
			print(1, 1, "　　");
			print(0, 7, sCdAcCode.getAcNoItem());
			print(0, 111, acNoItem);
		}

		print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－－－－－－");
		print(1, 1, "　　　合計　TOTAL ：");

		print(0, 61, currencyCode);
		print(0, 86, formatAmt(dbAmt, 2), "R");
		print(0, 108, formatAmt(crAmt, 2), "R");

	}

}
