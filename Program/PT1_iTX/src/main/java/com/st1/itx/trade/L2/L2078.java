package com.st1.itx.trade.L2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.springjpa.cm.L2078ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2078")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */

public class L2078 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	@Autowired
	public L2078ServiceImpl l2078ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	WebClient webClient;
	@Autowired
	LoanCom loanCom;
	@Autowired
	private MakeExcel makeExcel;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private BigDecimal ovduFeeTotal = BigDecimal.ZERO;
	private BigDecimal feeTotal = BigDecimal.ZERO;
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2078 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 143 * 400 = 57200

		// tita
		int iReceiveDateStart = parse.stringToInteger(titaVo.getParam("ReceiveDateStart")) + 19110000;
		int iReceiveDateEnd = parse.stringToInteger(titaVo.getParam("ReceiveDateEnd")) + 19110000;
		int iCustNoStart = parse.stringToInteger(titaVo.getParam("CustNoStart"));
		int iCustNoEnd = parse.stringToInteger(titaVo.getParam("CustNoEnd"));
		int iSearc = parse.stringToInteger(titaVo.getParam("Searc"));

		if (iSearc == 2) {

			List<Map<String, String>> dList = new ArrayList<Map<String, String>>();
			try {
				dList = l2078ServiceImpl.findAll(titaVo, index, limit);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l2078ServiceImpl.findAll error = " + e.getMessage());
				// E5004 讀取DB時發生問題
				throw new LogicException(titaVo, "E5004", "");
			}
			if (this.index == 0 && (dList == null || dList.size() == 0)) {
				throw new LogicException(titaVo, "E2003", "查無資料,請至L2601新增"); // 查無資料
			}

			for (Map<String, String> t : dList) {
				// new occurs
				OccursList occursList = new OccursList();

				int docDate = parse.stringToInteger(t.get("DocDate"));
				if (docDate > 19110000) {
					docDate = docDate - 19110000;
				}
				int receiveDate = parse.stringToInteger(t.get("ReceiveDate"));
				if (receiveDate > 19110000) {
					receiveDate = receiveDate - 19110000;
				}
				int closeDate = parse.stringToInteger(t.get("CloseDate"));
				if (closeDate > 19110000) {
					closeDate = closeDate - 19110000;
				}
				int overdueDate = parse.stringToInteger(t.get("OverdueDate"));
				if (overdueDate > 19110000) {
					overdueDate = overdueDate - 19110000;
				}
				BigDecimal fee = parse.stringToBigDecimal(t.get("Fee"));

				// 有銷號日期的為已還完費用
				// 有催收無銷帳日期 - > 催收法務費
				if (overdueDate != 0 && closeDate == 0) {
					ovduFeeTotal = ovduFeeTotal.add(fee);
				}
				// 無催收無銷帳日期 - > 法拍費用
				if (overdueDate == 0 && closeDate == 0) {
					feeTotal = feeTotal.add(fee);
				}
				occursList.putParam("OORecordNo", t.get("RecordNo"));
				occursList.putParam("OOReceiveDate", receiveDate);
				occursList.putParam("OOCustNo", t.get("CustNo"));
				occursList.putParam("OOFacmNo", t.get("FacmNo"));
				occursList.putParam("OOFee", t.get("Fee"));
				occursList.putParam("OOFeeCode", t.get("FeeCode"));
				occursList.putParam("OOCaseCode", t.get("CaseCode"));
				// 單位
				occursList.putParam("OOUnit", t.get("RemitBranch"));
				occursList.putParam("OOCaseNo", t.get("CaseNo"));
				// 入帳日期
				occursList.putParam("OOEntryDate", docDate);
				occursList.putParam("OODocDate", docDate); // 單據日期
				occursList.putParam("OOCloseDate", closeDate);
				// 催收記號
				occursList.putParam("OOOverdue", t.get("OverdueFg"));
				occursList.putParam("OOCloseNo", t.get("CloseNo"));
				occursList.putParam("OOLegalStaff", t.get("LegalStaff")); // 法務人員
				occursList.putParam("OORmk", t.get("Rmk")); // 備註

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

			if (l2078ServiceImpl.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

		} else {
			getExcel(titaVo);
		}
		this.totaVo.putParam("FeeTotal", feeTotal);
		this.totaVo.putParam("OvduFeeTotal", ovduFeeTotal);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 產生excel
	private void getExcel(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> resultListAll = new ArrayList<Map<String, String>>();
		try {
			resultListAll = l2078ServiceImpl.findAll(titaVo, 0, Integer.MAX_VALUE);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l2078ServiceImpl.findAll error = " + e.getMessage());
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		if (resultListAll != null && resultListAll.size() > 0) {
			int R = 3;

			ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
					.setRptCode("L2078").setRptItem("法拍費用明細資料").build();
			// open
			makeExcel.open(titaVo, reportVo, "法拍費用明細資料");
			// 設定excel欄位寬度
			makeExcel.setWidth(1, 12);// 收件日
			makeExcel.setWidth(2, 15);// 戶號
			makeExcel.setWidth(3, 13);// 法拍費用
			makeExcel.setWidth(4, 20);// 科目
			makeExcel.setWidth(5, 12);// 單據日期
			makeExcel.setWidth(6, 12);// 銷號日期
			makeExcel.setWidth(7, 8);// 催收
			makeExcel.setWidth(8, 10);// 銷帳編號
			makeExcel.setValue(1, 1, "法拍費用");
			makeExcel.setValue(2, 1, "催收法務費");
			makeExcel.setValue(R, 1, "收件日");
			makeExcel.setValue(R, 2, "戶號");
			makeExcel.setValue(R, 3, "法拍費用");
			makeExcel.setValue(R, 4, "科目");
			makeExcel.setValue(R, 5, "單據日期");
			makeExcel.setValue(R, 6, "銷號日期");
			makeExcel.setValue(R, 7, "催收");
			makeExcel.setValue(R, 8, "銷帳編號");
			for (Map<String, String> result : resultListAll) {
				R++;

				int receiveDate = parse.stringToInteger(result.get("ReceiveDate"));
				if (receiveDate > 19110000) {
					receiveDate = receiveDate - 19110000;
				}

				String custNo = String.format("%07d", parse.stringToInteger(result.get("CustNo")));
				String facmNo = String.format("%03d", parse.stringToInteger(result.get("FacmNo")));
				BigDecimal fee = parse.stringToBigDecimal(result.get("Fee"));

				int docDate = parse.stringToInteger(result.get("DocDate"));
				if (docDate > 19110000) {
					docDate = docDate - 19110000;
				}
				int closeDate = parse.stringToInteger(result.get("CloseDate"));
				if (closeDate > 19110000) {
					closeDate = closeDate - 19110000;
				}
				int overdueDate = parse.stringToInteger(result.get("OverdueDate"));
				if (overdueDate > 19110000) {
					overdueDate = overdueDate - 19110000;
				}
				if (overdueDate != 0 && closeDate == 0) {
					ovduFeeTotal = ovduFeeTotal.add(fee);
				}
				// 無催收無銷帳日期 - > 法拍費用
				if (overdueDate == 0 && closeDate == 0) {
					feeTotal = feeTotal.add(fee);
				}

				String feeCode = result.get("FeeCode");
				String caseCode = result.get("CaseCode");
				String remitBranch = result.get("RemitBranch");
				String caseNo = result.get("CaseNo");

				String overdueFg = result.get("OverdueFg");
				String closeNo = result.get("CloseNo");
				String legalStaff = result.get("LegalStaff");
				String rmk = result.get("Rmk");

				makeExcel.setValue(R, 1, receiveDate);// 收件日
				makeExcel.setValue(R, 2, custNo + "-" + facmNo);// 戶號
				this.info("fee = " + fee);
				makeExcel.setValue(R, 3, "" + df.format(fee));// 法拍費用
				makeExcel.setValue(R, 4, feeCode + "-" + loanCom.getCdCodeX("FeeCode", result.get("FeeCode"), titaVo));// 科目
				makeExcel.setValue(R, 5, docDate);// 單據日期
				makeExcel.setValue(R, 6, closeDate);// 銷號日期
				makeExcel.setValue(R, 7, overdueFg);// 催收
				makeExcel.setValue(R, 8, closeNo);// 銷帳編號
			}
			makeExcel.setValue(1, 2, "" + df.format(feeTotal));// 法拍費用
			makeExcel.setValue(2, 2, "" + df.format(ovduFeeTotal));// 法拍費用
		}
		makeExcel.toExcel(makeExcel.close());

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", "", "L5903已完成", titaVo);

	}
}