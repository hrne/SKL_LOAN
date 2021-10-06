package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L4101ReportB extends MakeReport {

	/* DB服務注入 */
	@Autowired
	SlipMediaService slipMediaService;
	@Autowired
	AcDetailService acDetailService;
	@Autowired
	BankRemitService bankRemitService;
	@Autowired
	CustMainService custMainService;
	@Autowired
	CdBcmService cdBcmService;
	@Autowired
	FacMainService facMainService;

	@Autowired
	AcCloseService acCloseService;

	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L4101";
	private String reportItem = "整批匯款單";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 帳冊別 TODO:待修改
	private String nowAcBookCode = "000";
	private String nowAcBookItem = "全帳冊";
	private String slipNo = "";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int acDate = 0;
	String batchNo = "";

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L4101ReportB.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "批號：" + titaVo.getBacthNo());
		this.print(-4, 145, "頁　　次：" + this.getNowPage());

		// 頁首帳冊別判斷
//		print(-4, 10, this.nowAcBookCode);
//		print(-4, 14, this.nowAcBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "編號　戶號　　　　　　　　　　　　　　　　　　　銀行別　　分行別　　　　匯款帳號　　　　　　　收款人　　　　　　　匯款金額　　　　　　核貸金額　　　　專辦　　沖轉　　");
		print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－　－－－－　－－－－－　－－－－－－－－　－－－－－－－－－－　－－－－－－－－－　－－－－－－－－－　－－－－　－－－　");
//		print(1, 1, "編號　戶號　　　　　　　　　　　　　　　　　　　銀行別　　分行別　　　匯款帳號　　　　　收款人　　　　　　　　匯款金額　　核貸金額　　　　　站別　　　　專辦　　　沖轉　　　　　　");

	}

//	// 自訂表尾
//	@Override
//	public void printFooter() {
//		this.print(-41, 1, "總　　　　　　　　　副　　　　　　　　協　　　　　　　　經　　　　　　覆　　　　　　　　　　　　　　　　　　　　　　　　　　傳票號碼：" + slipNo);
//		this.print(-42, 1, "經　　　　　　　　　總　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　經辦單位");
//		this.print(-43, 1, "理　　　　　　　　　經　　　　　　　　理　　　　　　　　理　　　　　　核　　　　　　　　　　　　　　　　　－－－－－－－－－－－－－－－－－－－－－");
//		this.print(-44, 1, "　　　　　　　　　　理　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　協理　　　　經理　　　　經辦");
//	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4101ReportB exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = titaVo.getBacthNo();
		reportCode = reportCode + "-" + batchNo;
		// 批號查全部
		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
		Slice<BankRemit> slBankRemit = bankRemitService.findL4901B(acDate, batchNo, 00, 99, 0, 0, 0, Integer.MAX_VALUE,
				titaVo);
		if (slBankRemit == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		lBankRemit = slBankRemit == null ? null : new ArrayList<BankRemit>(slBankRemit.getContent());

		if (lBankRemit == null || lBankRemit.isEmpty()) {
			// 出空表
			this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);
			this.setCharSpaces(0);
			print(1, 1, "本日無資料");
			return;
		}

		// 合計容器(以借貸方區分)
		BigDecimal dbAmt = BigDecimal.ZERO;
		BigDecimal crAmt = BigDecimal.ZERO;
		String currencyCode = lBankRemit.get(0).getCurrencyCode();

//		this.nowAcBookCode = lAcDetail.get(0).getSlipMediaId().getAcBookCode();
//		this.nowAcBookItem = lAcDetail.get(0).getAcBookItem();
//		this.slipNo = lAcDetail.get(0).getSlipMediaId().getMediaSlipNo();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		int rounds = 1;
		for (BankRemit tBankRemit : lBankRemit) {

//			if (!this.nowAcBookCode.equals(tAcDetail.getAcBookCode())) {
//				// 修改表頭的帳冊別欄位
//				this.nowAcBookCode = tSlipMedia.getAcBookCode();
//				this.nowAcBookItem = tSlipMedia.getAcBookItem();
//				this.slipNo = tSlipMedia.getSlipMediaId().getMediaSlipNo();
//
//				print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－－－－－－－－　－－　－－－－－－－－－－　－－－－－－－－－－　－－－－－－－－－－－－－－　－－－－－－－－－－－－");
//				print(1, 1, "　　　　　　           ：");
//
////				print(0, 61, currencyCode);
//				print(0, 86, formatAmt(dbAmt, 2), "R");
//				print(0, 108, formatAmt(crAmt, 2), "R");
//				dbAmt = BigDecimal.ZERO;
//				crAmt = BigDecimal.ZERO;
//
//				this.newPage();
//			}
			Slice<AcDetail> slAcDetail = acDetailService.acdtlRelTxseqEq(acDate,
					titaVo.getKinbr() + tBankRemit.getTitaTlrNo() + tBankRemit.getTitaTxtNo(), acDate, 0,
					Integer.MAX_VALUE, titaVo);
			FacMain tFacMain = facMainService.findById(new FacMainId(tBankRemit.getCustNo(), tBankRemit.getFacmNo()),
					titaVo);
			CdBcm tCdBcm = new CdBcm();
			if (tFacMain != null) {
				tCdBcm = cdBcmService.distCodeFirst(tFacMain.getDistrict(), titaVo);
			}
//			沖轉 1.是 0.否
			int corFlag = 1;

			if (slAcDetail != null) {
				corFlag = 0;
			}
			String distItem = "";
			BigDecimal lineAmt = BigDecimal.ZERO;
			String businessOfficer = "";
			if (tFacMain != null) {
				lineAmt = tFacMain.getLineAmt();
				if (tCdBcm != null)
					distItem = tCdBcm.getDistItem();
				businessOfficer = tFacMain.getBusinessOfficer();
			}

//			BigDecimal txAmt = tBankRemit.get.getTxAmt();
			String custNo = FormatUtil.pad9("" + tBankRemit.getCustNo(), 7); // 戶號
			String facmNo = FormatUtil.pad9("" + tBankRemit.getFacmNo(), 3); // 戶號額度
			String bormNo = FormatUtil.pad9("" + tBankRemit.getBormNo(), 3); // 戶號撥款序號
			String custName = ""; // 戶名
			CustMain tCustMain = custMainService.custNoFirst(tBankRemit.getCustNo(), tBankRemit.getCustNo(), titaVo);
			if (tCustMain != null) {
				custName = tCustMain.getCustName();
			} else {
				custName = "";
			}
			String remitBank = tBankRemit.getRemitBank(); // 銀行別
			String remitBranch = tBankRemit.getRemitBranch(); // 分行別
			String remitAcctNo = tBankRemit.getRemitAcctNo(); // 匯款帳號

			String wkCustName = tBankRemit.getCustName(); // 收款戶名
			BigDecimal remitAmt = tBankRemit.getRemitAmt(); // 匯款金額
			BigDecimal wkLineAmt = lineAmt;// 核貸金額
			String wkBusinessOfficer = businessOfficer;// 專辦
			int wkCorFlag = corFlag; // 沖轉

			// 明細資料第一行
			print(1, 1, "　　");
			print(0, 1, FormatUtil.pad9("" + rounds, 3));// 序號
			print(0, 7, "" + custNo + "-" + facmNo + "-" + bormNo);// 戶號
			print(0, 26, FormatUtil.padX("" + custName, 20));// 戶名
			print(0, 50, remitBank);// 銀行別
			print(0, 60, remitBranch);// 分行別
			print(0, 70, remitAcctNo);// 匯款帳號
			print(0, 87, FormatUtil.padX("" + wkCustName, 20));// 收款戶名
			print(0, 126, formatAmt(remitAmt, 0), "R");// 匯款金額
			print(0, 146, formatAmt(wkLineAmt, 0), "R");// 核貸金額
			print(0, 150, wkBusinessOfficer);// 專辦
			print(0, 161, String.valueOf(wkCorFlag));// 沖轉

			dbAmt = dbAmt.add(remitAmt);
			crAmt = crAmt.add(wkLineAmt);

			// 明細資料第二行
//			print(1, 1, "　　");
//			print(0, 7, tSlipMedia.getAcNoItem());
//			print(0, 111, tSlipMedia.getSlipRmk());
			rounds++;
		}

		print(1, 1, "－－　－－－－－－－－－－－－－－－－－－－　－－－－　－－－－－　－－－－－－－－　－－－－－－－－－－　－－－－－－－－－　－－－－－－－－－　－－－－　－－－　");
		print(1, 1, "　　　　　　           ");

//		print(0, 61, currencyCode);
		print(0, 126, formatAmt(dbAmt, 2), "R");
		print(0, 146, formatAmt(crAmt, 2), "R");

	}

	private String getBatchNo(TitaVo titaVo) throws LogicException {
		String batchNo = "";
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無帳務資料"); // 查詢資料不存在
		}
		batchNo = "LN" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";
		return batchNo;
	}

}
