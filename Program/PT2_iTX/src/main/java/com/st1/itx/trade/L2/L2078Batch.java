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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L2078ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2078Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2078Batch extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public MakeExcel makeExcel;
	@Autowired
	public L2078ServiceImpl l2078ServiceImpl;
	@Autowired
	public LoanCom loanCom;
	@Autowired
	public WebClient webClient;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	private BigDecimal ovduFeeTotal = BigDecimal.ZERO;
	private BigDecimal feeTotal = BigDecimal.ZERO;
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2078Batch ");
		this.totaVo.init(titaVo);

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

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", "", "L2078已完成", titaVo);

		return this.sendList();
	}
}