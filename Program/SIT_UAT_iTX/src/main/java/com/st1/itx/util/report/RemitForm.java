package com.st1.itx.util.report;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.util.common.MakeReport;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.util.common.data.RemitFormVo;
import com.st1.itx.dataVO.TitaVo;

@Component("remitForm")
@Scope("prototype")

public class RemitForm extends MakeReport {
	// 自訂表頭
	@Override
	public void printHeader() {

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);
	}

	// 自訂明細標題
	@Override
	public void printTitle() {

	}

	/**
	 * 輸出單筆匯款申請書
	 * 
	 * @param titaVo      titaVo
	 * @param remitFormVo remitFormVo
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, RemitFormVo remitFormVo) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), remitFormVo.getReportCode(), remitFormVo.getReportItem(), "", "新版匯款申請書107年10月版.pdf");

	}

	public void addpage(TitaVo titaVo, RemitFormVo remitFormVo) throws LogicException {
		this.info("RemitForm.addpage=" + this.getNowPage());

		if (remitFormVo.isNewPage())
			this.newPage();

		// 微 軟正黑體
		this.setFont(3);

		// 申請日期(民國年)
		int applyday = remitFormVo.getApplyDay();
		if (applyday > 19110000) {
			applyday -= 19110000;
		}
		String x = String.format("%07d", applyday);
		this.setField("Text16", x.substring(0, 3));
		this.setField("Text17", x.substring(3, 5));
		this.setField("Text19", x.substring(5, 7));

		// 取款金額記號:1.同匯款金額 2.同匯款金額及手續費
		switch (remitFormVo.getAmtFg()) {
		case 1:
			this.setField("Radio Button30", "0");
			break;
		case 2:
			this.setField("Radio Button30", "1");
			break;
		}

		// 取款帳號
		this.setField("Text3", remitFormVo.getWithdrawAccount());

		// 銀行記號:1.跨行 2.聯行 3.國庫 4.同業 5.證券 6.票券
		switch (remitFormVo.getBankFg()) {
		case 1:
			this.setField("Radio Button5", "01");
			break;
		case 2:
			this.setField("Radio Button5", "02");
			break;
		case 3:
			this.setField("Radio Button5", "03");
			break;
		case 4:
			this.setField("Radio Button5", "04");
			break;
		case 5:
			this.setField("Radio Button5", "05");
			break;
		}

		// 收款行-銀行
		this.setField("Text1", remitFormVo.getReceiveBank());

		// 收款行-分行
		this.setField("Text2", remitFormVo.getReceiveBranch());

		// 財金費
		this.setField("Text21", String.valueOf(remitFormVo.getFiscFeeAmt()));

		// 手續費
		this.setField("Text22", String.valueOf(remitFormVo.getNormalFeeAmt()));

		// 收款人-帳號
		this.setField("Text5", remitFormVo.getReceiveAccount());

		// 收款人-戶名
		this.setField("Text6", remitFormVo.getReceiveName());

		// 匯款代理人
		this.setField("Text11", remitFormVo.getAgentName());

		// 匯款代理人身份證號碼
		this.setField("Text12", remitFormVo.getAgentId());

		// 匯款人代理人電話
		this.setField("Text13", remitFormVo.getAgentTel());

		// 匯款金額
		this.setField("Text7", "$" + String.valueOf(remitFormVo.getRemitAmt()));

		// 匯款人名稱
		this.setField("Text8", remitFormVo.getRemitName());

		// 匯款人統一編號
		this.setField("Text14", remitFormVo.getRemitId());

		// 匯款人電話
		this.setField("Text9", remitFormVo.getRemitTel());

		// 附言
		this.setField("Text15", remitFormVo.getNote());

	}

}
