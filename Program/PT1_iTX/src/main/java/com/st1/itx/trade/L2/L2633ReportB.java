package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.springjpa.cm.L2633ServiceImpl;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2633ReportB")
@Scope("prototype")
public class L2633ReportB extends MakeReport {

	@Autowired
	public FacCloseService sFacCloseService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	CdCodeService cdCodeService;
	@Autowired
	L2633ServiceImpl l2633ServiceImpl;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L2633B";
	private String reportItem = "清償日報表-限補領補發";
	private String security = "機密";
	private String defaultPdf = "";
	private String pageSize = "A4";
	private TempVo t2TempVo = new TempVo();

//	private String pageSize ="A5";
	private String pageOrientation = "L";
	private int iCustNo = 0;
	private int iCloseNo = 0;
	private BigDecimal msAmt = BigDecimal.ZERO;
	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

//	// 製表日期
//	private String NowDate;
//	// 製表時間
//	private String NowTime;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2633Report.printHeader");

		this.setFontSize(10);
//		this.print(-2, 55, "新光人壽保險股份有限公司", "C");
//		this.print(-3, 55, "抵押權塗銷同意書", "C");
		this.print(-4, 6, "", "L");
		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
//		this.print(-4, 2, "批號：" + titaVo.getBacthNo());
		this.print(-4, 145, "頁　　次：" + this.getNowPage());

		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

		this.setFontSize(8);
		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(1, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "　戶號　　　　　戶名　　　　　　　　　　　清償原因　　　　　　　　結清金額　　　　領取方式　　　　　備註　　　　　　　　聯絡電話１　　　　公文編號　　　　塗銷編號　　　　　　　銷號欄　　　　");
		print(1, 1, "－－－－－－　－－－－－－－－－－　－－－－－－－－－－－　－－－－－－－－－　－－－－－－　－－－－－－－－－－　－－－－－－－－－－　－－－－　－－－－－－－－－－　－－－－－－－－－－");

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.setFont(1, 10);
		this.print(-41, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		this.print(-42, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		this.print(-43, 1, "　　　　　　　　　　　　　　　　經理：　　　　　　　　　　　　　製表人：　　　　　　　　　　　　　　　　　");
		this.print(-44, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");

		this.setFont(1, 8);
	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L2633ReportB exec");

		exportPdf(titaVo);

		return true;
	}

	private void exportPdf(TitaVo titaVo) throws LogicException {
		this.info("exportExcel ... ");

		this.reportDate = Integer.valueOf(titaVo.getParam("ApplDate")) + 19110000;
		this.brno = titaVo.getBrno();
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.setFont(1);

		this.setFontSize(8);
		this.setCharSpaces(0);
		// 入帳日期
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l2633ServiceImpl.findAllB(0, Integer.MAX_VALUE, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (resultList != null && resultList.size() != 0) {

			this.info("Size =" + resultList.size());

			this.open(titaVo, titaVo.getEntDyI(), brno, reportCode, reportItem, security, pageSize, pageOrientation);
			this.setCharSpaces(0);
			this.setFont(1, 10);
			print(-4, 85, this.showRocDate(iApplDate, 0), "C"); // 日期
			this.setFont(1, 8);
			print(1, 1, " ");
			int i = 1;
			int cnt = 0;
			BigDecimal totAmt = BigDecimal.ZERO;
			for (Map<String, String> result : resultList) {
				int custNo = parse.stringToInteger(result.get("CustNo"));
				int facmNo = parse.stringToInteger(result.get("FacmNo"));
				int closeDate = parse.stringToInteger(result.get("CloseDate"));
				if (closeDate > 0) {
					closeDate = closeDate - 19110000;
				}
				String closeReasonCode = result.get("CloseReasonCode");
				String collectWayCode = result.get("CollectWayCode");
				BigDecimal closeAmt = parse.stringToBigDecimal(result.get("CloseAmt"));
				String rmk = result.get("Rmk");
				String telNo1 = result.get("TelNo1");
				String docNo = result.get("DocNo");
				String docNoE = result.get("DocNoE");
				String agreeNo = result.get("AgreeNo");
				String clsNo = result.get("ClsNo");

				// 全部結案
				Slice<ClFac> slClFac = null; // 擔保品與額度關聯檔
				if (facmNo == 0) {
					slClFac = clFacService.custNoEq(custNo, 0, Integer.MAX_VALUE, titaVo);
				} else {
					slClFac = clFacService.facmNoEq(custNo, facmNo, 0, Integer.MAX_VALUE, titaVo);
				}
				boolean isAllClose = true;
				if (slClFac != null) {
					for (ClFac t : slClFac.getContent()) {
						Slice<ClFac> slClFac2 = null;
						slClFac2 = clFacService.clNoEq(t.getClCode1(), t.getClCode2(), t.getClNo(), 0,
								Integer.MAX_VALUE, titaVo);
						if (slClFac2 != null) {
							for (ClFac t2 : slClFac2.getContent()) {
								if ((t2.getCustNo() == custNo && facmNo == 0)
										|| (t2.getCustNo() == custNo && t2.getFacmNo() == facmNo)) {
									// 撥款主檔
									Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(t2.getCustNo(),
											t2.getFacmNo(), t2.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
									if (slLoanBorMain != null) {
										for (LoanBorMain tlbm : slLoanBorMain.getContent()) {

											// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
											if (tlbm.getStatus() == 0 || tlbm.getStatus() == 2 || tlbm.getStatus() == 4
													|| tlbm.getStatus() == 6 || tlbm.getStatus() == 8) {
												isAllClose = false;
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				if (!isAllClose) {
					continue;
				}

				i++;
				cnt++;

				print(1, 1, " ");
				// new occurs
				OccursList occursList = new OccursList();
				print(0, 2, parse.IntegerToString(custNo, 7) + "-" + parse.IntegerToString(facmNo, 3)); // 戶號
				print(0, 15, FormatUtil.padX(loanCom.getCustNameByNo(custNo), 20)); // 戶名
				String CloseReasonItem = "";
				CdCode tCdCode = cdCodeService.getItemFirst(3, "AdvanceCloseCode", closeReasonCode, titaVo);
				if (tCdCode != null) {
					CloseReasonItem = tCdCode.getItem();
				}
				if (closeReasonCode.isEmpty()) {
					print(0, 37, "無"); // 清償原因
				} else {
					print(0, 37, closeReasonCode + "-" + CloseReasonItem); // 清償原因
				}
				print(0, 79, formatAmt(closeAmt, 0), "R"); // 還清金額
				totAmt = totAmt.add(closeAmt);
				String CollectWayItem = "";
				CdCode t2CdCode = cdCodeService.getItemFirst(2, "CollectWayCode", collectWayCode, titaVo);
				if (t2CdCode != null) {
					CollectWayItem = t2CdCode.getItem();
					print(0, 81, CollectWayItem); // 領取方式
				} else {
					print(0, 81, collectWayCode); // 領取方式
				}

				print(0, 95, FormatUtil.padX("" + rmk, 20)); // 備註欄
				print(0, 117, FormatUtil.padX("" + telNo1, 20)); // 連絡電話1
				this.info("docNoE = " + docNoE);
				if (!"0".equals(docNoE)) {
					print(0, 139, FormatUtil.padX("" + docNo, 7) + "-" + FormatUtil.padX("" + docNoE, 4)); // 公文編號
				} else {
					print(0, 139, FormatUtil.padX("" + docNo, 7)); // 公文編號
				}
				print(0, 149, FormatUtil.padX(agreeNo, 20)); // 塗銷編號
				print(0, 171, FormatUtil.padX(clsNo, 20)); // 銷號欄
				print(1, 1, "　　　　　　           ");
			}
			print(1, 1,
					"－－－－－－　－－－－－－－－－－　－－－－－－－－－－－　－－－－－－－－－　－－－－－－　－－－－－－－－－－　－－－－－－－－－－　－－－－　－－－－－－－－－－　－－－－－－－－－－");

		} else {
			// 出空表
			this.open(titaVo, titaVo.getEntDyI(), brno, reportCode, reportItem, security, pageSize, pageOrientation);
			this.setCharSpaces(0);
			this.setRptItem("清償日報表-限補領補發(無符合資料)");
			print(2, 1, "本日無資料");
			return;
		}
	}

}
