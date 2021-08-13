package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * AcDate=9,7<br>
 * PerfMonth=9,5<br>
 * BatchNoFrom=X,6<br>
 * BatchNoTo=X,6<br>
 */

@Service("L4520")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4520 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4520.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	int succCnt = 0;
	BigDecimal succRpAmt = BigDecimal.ZERO;
	BigDecimal succTxAmt = BigDecimal.ZERO;
	int failCnt = 0;
	BigDecimal failRpAmt = BigDecimal.ZERO;
	BigDecimal failTxAmt = BigDecimal.ZERO;
	int totlCnt = 0;
	BigDecimal totlRpAmt = BigDecimal.ZERO;
	BigDecimal totlTxAmt = BigDecimal.ZERO;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4520 ");
		this.totaVo.init(titaVo);
//		回傳後產出

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int iPerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		String iProcCode = titaVo.getParam("ProcCode");

		List<EmpDeductMedia> lEmpDeductMedia = new ArrayList<EmpDeductMedia>();

		Slice<EmpDeductMedia> sEmpDeductMedia = null;

		sEmpDeductMedia = empDeductMediaService.findL4520A(iAcDate, iPerfMonth, iProcCode, this.index, this.limit);

		lEmpDeductMedia = sEmpDeductMedia == null ? null : sEmpDeductMedia.getContent();

		if (lEmpDeductMedia != null && lEmpDeductMedia.size() != 0) {
			for (EmpDeductMedia tEmpDeductMedia : lEmpDeductMedia) {
				if (tEmpDeductMedia.getErrorCode() == null) {
					continue;
				} else if ("01".equals(tEmpDeductMedia.getErrorCode())) {
					setReport(tEmpDeductMedia, 1);
					succCnt = succCnt + 1;
					succRpAmt = succRpAmt.add(tEmpDeductMedia.getRepayAmt());
					succTxAmt = succTxAmt.add(tEmpDeductMedia.getTxAmt());
				} else {
					setReport(tEmpDeductMedia, 2);
					failCnt = failCnt + 1;
					failRpAmt = failRpAmt.add(tEmpDeductMedia.getRepayAmt());
					failTxAmt = failTxAmt.add(tEmpDeductMedia.getTxAmt());
				}
			}

			if (succCnt >= 1) {
				totaA.putParam("OA_SuccCnt", succCnt);
				totaA.putParam("OA_SuccRpAmt", succRpAmt);
				totaA.putParam("OA_SuccTxAmt", succTxAmt);
				totaA.putParam("OA_TotlCnt", succCnt + failCnt);
				totaA.putParam("OA_TotlRpAmt", succRpAmt.add(failRpAmt));
				totaA.putParam("OA_TotlTxAmt", succTxAmt.add(failTxAmt));
				totaA.putParam("MSGID", "L452A");
				this.addList(totaA);
			}
			if (failCnt >= 1) {
				totaB.putParam("OB_FailCnt", failCnt);
				totaB.putParam("OB_FailRpAmt", failRpAmt);
				totaB.putParam("OB_FailTxAmt", failTxAmt);
				totaB.putParam("OB_TotlCnt", succCnt + failCnt);
				totaB.putParam("OB_TotlRpAmt", succRpAmt.add(failRpAmt));
				totaB.putParam("OB_TotlTxAmt", succTxAmt.add(failTxAmt));
				totaB.putParam("MSGID", "L452B");
				this.addList(totaB);
			}
			if (succCnt + failCnt == 0) {
				throw new LogicException("E0001", "查無資料");
			}
		} else {
			throw new LogicException("E0001", "查無資料");
		}
		return this.sendList();
	}

//	flag : 1= success ; 2=fail
	private void setReport(EmpDeductMedia tEmpDeductMedia, int flag) {
		OccursList occursList = new OccursList();

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tEmpDeductMedia.getCustNo(), tEmpDeductMedia.getCustNo());

		int nameLength = 10;
		if (tCustMain.getCustName().length() < 10) {
			nameLength = tCustMain.getCustName().length();
		}

		if (flag == 1) {
			occursList.putParam("OOA_CustNo", tEmpDeductMedia.getCustNo());
			occursList.putParam("OOA_EmpName", tCustMain.getCustName().substring(0, nameLength));
			switch (tEmpDeductMedia.getErrorCode()) {
			case "01":
				occursList.putParam("OOA_FailMsg", "成功");
				break;
			case "16":
				occursList.putParam("OOA_FailMsg", "扣款失敗");
				break;
			case "17":
				occursList.putParam("OOA_FailMsg", "扣款不足");
				break;
			default:
				occursList.putParam("OOA_FailMsg", "無此錯誤代碼");
				break;
			}
			occursList.putParam("OOA_RepayAmt", tEmpDeductMedia.getRepayAmt());
			occursList.putParam("OOA_TxAmt", tEmpDeductMedia.getTxAmt());
			occursList.putParam("OOA_EmpNo", tCustMain.getEmpNo());
			occursList.putParam("OOA_EmpId", tCustMain.getCustId());
			occursList.putParam("OOA_AcDate", tEmpDeductMedia.getAcDate());
			occursList.putParam("OOA_Remark", "  ");

			totaA.addOccursList(occursList);
		} else if (flag == 2) {
			occursList.putParam("OOB_CustNo", tEmpDeductMedia.getCustNo());
			occursList.putParam("OOB_EmpName", tCustMain.getCustName().substring(0, nameLength));
			switch (tEmpDeductMedia.getErrorCode()) {
			case "01":
				occursList.putParam("OOB_FailMsg", "成功");
				break;
			case "16":
				occursList.putParam("OOB_FailMsg", "扣款失敗");
				break;
			case "17":
				occursList.putParam("OOB_FailMsg", "扣款不足");
				break;
			default:
				occursList.putParam("OOB_FailMsg", "無此錯誤代碼");
				break;
			}
			occursList.putParam("OOB_RepayAmt", tEmpDeductMedia.getRepayAmt());
			occursList.putParam("OOB_TxAmt", tEmpDeductMedia.getTxAmt());
			occursList.putParam("OOB_EmpNo", tCustMain.getEmpNo());
			occursList.putParam("OOB_EmpId", tCustMain.getCustId());
			occursList.putParam("OOB_AcDate", tEmpDeductMedia.getAcDate());
			occursList.putParam("OOB_Remark", "  ");

			totaB.addOccursList(occursList);
		}
	}
}