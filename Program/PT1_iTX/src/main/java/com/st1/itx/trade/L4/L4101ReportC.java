package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L4101ReportC extends MakeReport {

	/* DB服務注入 */
	@Autowired
	SlipMediaService slipMediaService;
	@Autowired
	AcDetailService acDetailService;
	@Autowired
	AcCloseService acCloseService;
	@Autowired
	CdAcCodeService cdAcCodeService;
	@Autowired
	CdBankService cdBankService;
	@Autowired
	CustMainService custMainService;

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
	private String reportItem = "撥款傳票明細表";
	private String drawdownDate = "";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int acDate = 0;
	String batchNo = "";
	List<String> tempList = null;
	int cnt = 0; // 總筆數
	BigDecimal sumDbAmt = BigDecimal.ZERO;// 借方總金額
	BigDecimal sumCrAmt = BigDecimal.ZERO;// 貸方總金額
	private HashMap<CdAcCodeId, BigDecimal> acCodeTotalAmt = new HashMap<>();
	private HashMap<CdAcCodeId, Integer> acCodeTotalSize = new HashMap<>();
	private Boolean isRT = false;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L4101ReportC.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		// 退款名稱
		if ("BCK".equals(batchNo)) {
			reportItem = "抽退票傳票明細表";
			drawdownDate = this.showRocDate(acDate, 0);
		} else if ("RT".equals(batchNo.substring(0, 2))) {
			reportItem = "退款傳票明細表";
			drawdownDate = this.showRocDate(acDate, 0);
		} else {
			drawdownDate = "撥款日期：" + this.showRocDate(acDate, 1);
		}
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "批號：" + titaVo.getBacthNo());
		this.print(-4, 85, drawdownDate, "C");
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

		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "傳票號碼　　日期　　科子細目名稱　　　　　　　　　　　　　　　　戶號　戶名　　　　　　　　借方金額　　　　貸方金額　　　銀行別　　　　　　　　　　　帳號　　　　　");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		print(1, 1, " ");
	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.print(-41, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		this.print(-42, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		if ("RT".equals(batchNo.substring(0, 2))) {
			this.print(-43, 1, "　　　　　　　　　　放款部　協理：　　　　　　　　經理：　　　　　　　　襄理：　　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
			this.print(-44, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
			this.print(-45, 1, "　　　　　　　　　　財務部　協理：　　　　　　　　經理：　　　　　　　　襄理：　　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
		} else {
			this.print(-43, 1, "　　　　　　　　　　　　　　協理：　　　　　　　　經理：　　　　　　　　襄理：　　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
			this.print(-44, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");

		}
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4101ReportC exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = titaVo.getBacthNo();
		reportCode = titaVo.getTxcd();
		reportCode = reportCode + "-C";
		// 退款名稱
		if ("BCK".equals(batchNo)) {
			reportItem = "抽退票傳票明細表";
		} else if ("RT".equals(batchNo.substring(0, 2))) {
			reportItem = "退款傳票明細表";
			isRT = true;
		}
		String wkName = "";
		String wkBankCode = "";
		String wkBranchCode = "";
		String wkBankItem = "";
		String wkBranchItem = "";
		String wkAcctNo = "";
		// 分錄
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

		Slice<AcDetail> slAcDetail = acDetailService.acdtlTitaBatchNoSlipNo(titaVo.getAcbrNo(), titaVo.getCurName(),
				acDate, batchNo, 0, Integer.MAX_VALUE, titaVo);
		lAcDetail = slAcDetail == null ? null : new ArrayList<AcDetail>(slAcDetail.getContent());

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem + "-" + batchNo).setSecurity(security).setRptSize(pageSize)
				.setPageOrientation(pageOrientation).build();
		this.open(titaVo, reportVo);

		if (lAcDetail == null || lAcDetail.isEmpty()) {
			// 出空表
			this.setCharSpaces(0);
			print(1, 1, "本日無資料");
			return;
		}

		// 統一大小
		this.setFont(1, 10);

		this.setCharSpaces(0);
		String wkRelTxSeq = "";

		for (AcDetail tAcDetail : lAcDetail) {

			this.info("tAcDetail EntAc = " + tAcDetail.getEntAc());
			if (tAcDetail.getEntAc() == 0) {
				continue;
			}
			if (!wkRelTxSeq.equals(tAcDetail.getRelTxseq())) {
				wkRelTxSeq = tAcDetail.getRelTxseq();
				cnt++;
			}
//			print(1, 1, "　　　　　　           ");

			String acNoCode = tAcDetail.getAcNoCode();
			String acSubCode = tAcDetail.getAcSubCode();
			String acDtlCode = "  ";
			CdAcCodeId tCdAcCodeId = new CdAcCodeId();
			tCdAcCodeId.setAcNoCode(acNoCode);
			tCdAcCodeId.setAcSubCode(acSubCode);
			tCdAcCodeId.setAcDtlCode(acDtlCode);
			CdAcCode tCdAcCode = cdAcCodeService.findById(tCdAcCodeId, titaVo);
			if (isRT) {
				if (acCodeTotalAmt.containsKey(tCdAcCodeId)) {
					acCodeTotalAmt.put(tCdAcCodeId, acCodeTotalAmt.get(tCdAcCodeId).add(tAcDetail.getTxAmt()));
					acCodeTotalSize.put(tCdAcCodeId, acCodeTotalSize.get(tCdAcCodeId) + 1);
				} else {
					acCodeTotalAmt.put(tCdAcCodeId, tAcDetail.getTxAmt());
					acCodeTotalSize.put(tCdAcCodeId, 1);
				}
			}
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tAcDetail.getJsonFields());

			this.info("tTempVo = " + tTempVo);
			wkName = tTempVo.getParam("CustName");
			wkBankCode = tTempVo.getParam("RemitBank");
			wkBranchCode = tTempVo.getParam("RemitBranch").isEmpty() ? "    " : tTempVo.getParam("RemitBranch");
			wkAcctNo = tTempVo.getParam("RemitAcctNo");
			CustMain c = custMainService.custNoFirst(tAcDetail.getCustNo(), tAcDetail.getCustNo(), titaVo);
			CdBank t = cdBankService.findById(new CdBankId(wkBankCode, wkBranchCode), titaVo);
			if (t != null) {
				wkBankItem = t.getBankItem();
				wkBranchItem = t.getBranchItem();
			} else {
				wkBankItem = wkBankCode;
				wkBranchItem = wkBranchCode;
			}

			// 明細資料第一行
			print(1, 2, parse.IntegerToString(tAcDetail.getSlipNo(), 6)); // 傳票號碼
			print(0, 9, this.showRocDate(acDate, 1)); // 會計日期
			print(0, 19, acNoCode); // 科子細目
			print(0, 64, "" + FormatUtil.pad9("" + tAcDetail.getCustNo(), 7));// 戶號
			if (c != null) {
				print(0, 73, FormatUtil.padX("" + c.getCustName(), 20));// 戶名
			}
			if ("D".equals(tAcDetail.getDbCr())) {
				print(0, 100, formatAmt(tAcDetail.getTxAmt(), 0), "R");// 借方金額
				sumDbAmt = sumDbAmt.add(tAcDetail.getTxAmt());
			}
			if ("C".equals(tAcDetail.getDbCr())) {

				print(0, 116, formatAmt(tAcDetail.getTxAmt(), 0), "R");// 貸方金額
				sumCrAmt = sumCrAmt.add(tAcDetail.getTxAmt());
			}
			print(0, 118, wkBankItem); // 銀行別
			print(0, 146, wkAcctNo);// 帳號
			// 明細資料第二行
			print(1, 1, "");
			print(0, 19, tCdAcCode.getAcNoItem());// 科子細目名稱
			print(0, 118, wkBranchItem);// 分行別

			// 如是第三人則第二行顯示第三人名稱
			if (c != null) {
				this.info("wkName = " + wkName);
				this.info("c Name = " + c.getCustName());
				if (!"".equals(wkName) && !wkName.equals(c.getCustName())) {
					print(0, 1, "　　");
					print(0, 63, "※非借款人帳號　 帳戶戶名：　" + FormatUtil.padX("" + wkName, 20)); // 第三人姓名
				}
			}
		}

		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		print(1, 1, "　　　　　　       共　" + cnt + "　筆數");
		print(0, 100, formatAmt(sumDbAmt, 0), "R");
		print(0, 116, formatAmt(sumCrAmt, 0), "R");
		if (isRT) {
			print(2, 1, "");
			print(1, 10, "註：");
			for (CdAcCodeId t : acCodeTotalAmt.keySet()) {

				CdAcCode tCdAcCode = cdAcCodeService.findById(t, titaVo);
				if (tCdAcCode != null) {
					if ("P02".equals(tCdAcCode.getAcctCode())) {
						print(1, 15, tCdAcCode.getAcNoItem()); // 科子目名稱
						print(0, 92, "計　" + parse.IntegerToString(acCodeTotalSize.get(t), 3) + "　筆　　$"); // 筆數
						print(0, 130, formatAmt(acCodeTotalAmt.get(t), 0), "R"); // 科目總金額
					}
				}
			}
		}
	}

}
