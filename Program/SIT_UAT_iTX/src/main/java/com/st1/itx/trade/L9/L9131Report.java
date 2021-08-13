package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SlipMedia;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")

public class L9131Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L9131Report.class);

	/* DB服務注入 */
	@Autowired
	SlipMediaService sSlipMediaService;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9131";
	private String reportItem = "核心日結單代傳票列印";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 帳冊別
	private String nowAcBookCode = "000";
	private String nowAcBookItem = "全帳冊";
	private String slipNo = "";

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
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "帳冊別：");
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, 85, "傳 票 日 期：" + showRocDate(this.reportDate), "C");

		// 頁首帳冊別判斷
		print(-4, 10, this.nowAcBookCode);
		print(-4, 14, this.nowAcBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(2, 1, "傳票　會計科目代號　　　子目代號　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　銷帳碼／　　　　　　　　　　單位代號／");
		print(1, 1, "序號　科目名稱　　　　　子目名稱　　　　　　　　　　　　　　幣別　　　　借方金額　　　　　　　貸方金額　　　　　　　　　摘要說明　　　　　　　　　　成本月份");
		print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－－－－－－");

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.print(-41, 1, "總　　　　　　　　　副　　　　　　　　協　　　　　　　　經　　　　　　覆　　　　　　　　　　　　　　　　　　　　　　　　　　傳票號碼：" + slipNo);
		this.print(-42, 1, "經　　　　　　　　　總　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　經辦單位");
		this.print(-43, 1, "理　　　　　　　　　經　　　　　　　　理　　　　　　　　理　　　　　　核　　　　　　　　　　　　　　　　　－－－－－－－－－－－－－－－－－－－－－");
		this.print(-44, 1, "　　　　　　　　　　理　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　協理　　　　經理　　　　經辦");
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9131Report exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));
		
		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		int iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		Slice<SlipMedia> sSlipMedia = sSlipMediaService.findMediaSeq(this.reportDate, iBatchNo, iMediaSeq, 0,
				Integer.MAX_VALUE, titaVo);
		List<SlipMedia> lSlipMedia = sSlipMedia == null ? null : sSlipMedia.getContent();

		if (lSlipMedia == null || lSlipMedia.isEmpty()) {
			// 出空表
			this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);
			this.setCharSpaces(0);
			print(1, 1, "本日無資料");
			return;
		}

		// 合計容器(以借貸方區分)
		BigDecimal dbAmt = BigDecimal.ZERO;
		BigDecimal crAmt = BigDecimal.ZERO;
		String currencyCode = lSlipMedia.get(0).getCurrencyCode();

		this.nowAcBookCode = lSlipMedia.get(0).getSlipMediaId().getAcBookCode();
		this.nowAcBookItem = lSlipMedia.get(0).getAcBookItem();
		this.slipNo = lSlipMedia.get(0).getSlipMediaId().getMediaSlipNo();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		for (SlipMedia tSlipMedia : lSlipMedia) {

			if (!this.nowAcBookCode.equals(tSlipMedia.getAcBookCode())) {
				// 修改表頭的帳冊別欄位
				this.nowAcBookCode = tSlipMedia.getAcBookCode();
				this.nowAcBookItem = tSlipMedia.getAcBookItem();
				this.slipNo = tSlipMedia.getSlipMediaId().getMediaSlipNo();

				print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－－－－－－");
				print(1, 1, "　　　合計　TOTAL ：");

				print(0, 61, currencyCode);
				print(0, 86, formatAmt(dbAmt, 2), "R");
				print(0, 108, formatAmt(crAmt, 2), "R");
				dbAmt = BigDecimal.ZERO;
				crAmt = BigDecimal.ZERO;

				this.newPage();
			}

			BigDecimal txAmt = tSlipMedia.getTxAmt();

			// 明細資料第一行
			print(1, 1, "　　");
			print(0, 1, FormatUtil.pad9(String.valueOf(tSlipMedia.getSeq()), 3));
			print(0, 7, tSlipMedia.getAcNoCode());
			print(0, 16, tSlipMedia.getAcSubCode());
			print(0, 61, tSlipMedia.getCurrencyCode());

			if (tSlipMedia.getDbCr().equals("D")) {
				print(0, 86, formatAmt(txAmt, 2), "R");
				dbAmt = dbAmt.add(txAmt);
			} else if (tSlipMedia.getDbCr().equals("C")) {
				print(0, 108, formatAmt(txAmt, 2), "R");
				crAmt = crAmt.add(txAmt);
			}

			// 明細資料第二行
			print(1, 1, "　　");
			print(0, 7, tSlipMedia.getAcNoItem());
			print(0, 111, tSlipMedia.getSlipRmk());
		}

		print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－－－－－－");
		print(1, 1, "　　　合計　TOTAL ：");

		print(0, 61, currencyCode);
		print(0, 86, formatAmt(dbAmt, 2), "R");
		print(0, 108, formatAmt(crAmt, 2), "R");

	}

}
