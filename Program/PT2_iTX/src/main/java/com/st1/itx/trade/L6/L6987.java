package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6987")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6987 extends TradeBuffer {
	
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public BaTxCom baTxCom;

	private int selectCode = 0;
	private int custNo = 0;
	private int trasCollDate = 0;
	private int cnt = 0;
	private TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6987 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 400; // 126 * 400 = 50400

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
		custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		trasCollDate = parse.stringToInteger(titaVo.getParam("TransCollDate"));
		ForeclosureFee tForeclosureFee = new ForeclosureFee();

		this.info("selectCode = " + selectCode);
		this.info("trasCollDate = " + trasCollDate);
		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();
		Slice<TxToDoDetail> slTxToDoDetail = null;
		Slice<ForeclosureFee> slForeclosureFee = null;
//		BDLW00 呆帳戶法務費墊付
		if (custNo > 0) {
			this.info("custno>0");
			slForeclosureFee = sForeclosureFeeService.custNoEq(custNo, this.index, this.limit, titaVo);
			lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
			if (lForeclosureFee == null) {
				throw new LogicException("E0001", "法拍費用檔");
			}
			ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
			try {
				baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), custNo, 0, 0, 0, BigDecimal.ZERO, titaVo);
			} catch (LogicException e) {
				throw new LogicException(titaVo, "E0015", "查詢費用 " + e.getMessage()); // 檢查錯誤
			}

			Boolean isAllBadLoan = false;
			isAllBadLoan = checkStatus(custNo, titaVo);
			// 非呆帳戶、呆帳結案戶或債權轉讓戶
			if (!isAllBadLoan) {
				throw new LogicException("E0015", "全戶非呆帳戶、呆帳結案戶或債權轉讓戶");
			}

			for (ForeclosureFee tmpForeclosureFee : lForeclosureFee) {

				// 取未銷 銷帳日期=0,轉催收日=0
				if (tmpForeclosureFee.getCloseDate() == 0) {
					int recordNo = tmpForeclosureFee.getRecordNo();
					String wkAcctCode = "";

					OccursList occursList = new OccursList();

					CustMain tCustMain = new CustMain();

					wkAcctCode = getAcctCode(tmpForeclosureFee, baTxList,
							parse.IntegerToString(tmpForeclosureFee.getRecordNo(), 7), titaVo);
					// 取戶名
					tCustMain = sCustMainService.custNoFirst(tmpForeclosureFee.getCustNo(),
							tmpForeclosureFee.getCustNo(), titaVo);

					occursList.putParam("OOProcStatus", 0);
					occursList.putParam("OOCustNo", tmpForeclosureFee.getCustNo());
					occursList.putParam("OOFacmNo", tmpForeclosureFee.getFacmNo());
					if (tCustMain != null) {
						occursList.putParam("OOCustName", tCustMain.getCustName());
					} else {
						occursList.putParam("OOCustName", "");
					}
					occursList.putParam("OOEntryDate", tmpForeclosureFee.getDocDate()); // 入帳日期
					occursList.putParam("OOFee", tmpForeclosureFee.getFee()); // 法拍費用
					occursList.putParam("OOFeeCode", tmpForeclosureFee.getFeeCode()); // 科目名稱代號
					occursList.putParam("OOTitaCrDb", 1); // 借貸
					occursList.putParam("OOAcDate", tmpForeclosureFee.getReceiveDate()); // 收件日
					occursList.putParam("OORelNo", "");
					occursList.putParam("OOItemCode", "BDLW00");
					occursList.putParam("OOBormNo", "");
					occursList.putParam("OODtlValue", parse.IntegerToString(tmpForeclosureFee.getRecordNo(), 7));// 銷帳編號

					occursList.putParam("OOAcctCode", wkAcctCode);
					cnt++;

					this.totaVo.addOccursList(occursList);

				}
			}

		} else {
			// 上一營業日
			int lbsDy = this.txBuffer.getTxCom().getLbsdy() + 19110000;
			// 本營業日
			int tbsDy = this.txBuffer.getTxCom().getTbsdy() + 19110000;
			String itemCode = "BDLW00"; // 呆帳戶法務費墊付
//			! 1:昨日留存  0-上一營業日
//			! 2:本日新增  本營業日-本營業日
//			! 3:全部     0-99991231
//			! 4:本日處理   0-99991231
//			! 5:本日刪除   0-99991231
//			! 6:保留     0-99991231
//			! 7:未處理 0-99991231
//			! 9:未處理 (按鈕處理) 0-99991231
	//   0.未處理
	//   1.已保留
	//   2.已處理
	//   3.已刪除

			switch (selectCode) {
			case 1:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0, lbsDy, this.index, this.limit,
						titaVo);
				break;
			case 2:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, tbsDy, tbsDy, this.index, this.limit,
						titaVo);
				break;
			case 3:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 4:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 2, 2, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 5:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 3, 3, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 6:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 1, 1, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 7:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 9:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			default:
				break;
			}

			lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
			this.info("lTxToDoDetail = " + lTxToDoDetail);

			if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
				for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
					if (custNo > 0 && tTxToDoDetail.getCustNo() != custNo) {
						continue;
					}
					OccursList occursList = new OccursList();
					tTempVo = new TempVo();
					CustMain tCustMain = new CustMain();

					// 取戶名
					tCustMain = sCustMainService.custNoFirst(tTxToDoDetail.getCustNo(), tTxToDoDetail.getCustNo(),
							titaVo);

					tForeclosureFee = sForeclosureFeeService
							.findById(parse.stringToInteger(tTxToDoDetail.getDtlValue()), titaVo);

					tTempVo = tTempVo.getVo(tTxToDoDetail.getProcessNote());

					occursList.putParam("OOProcStatus", tTxToDoDetail.getStatus());
					occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
					occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
					occursList.putParam("OOCustName", tCustMain.getCustName());
					occursList.putParam("OOEntryDate", tForeclosureFee.getDocDate()); // 入帳日期
					occursList.putParam("OOFee", tForeclosureFee.getFee()); // 法拍費用
					occursList.putParam("OOFeeCode", tForeclosureFee.getFeeCode()); // 科目名稱代號
					occursList.putParam("OOTitaCrDb", 1); // 借貸
					occursList.putParam("OOAcDate", tForeclosureFee.getReceiveDate()); // 收件日
					occursList.putParam("OORelNo", tTxToDoDetail.getTitaEntdy() + tTxToDoDetail.getTitaKinbr()
							+ tTxToDoDetail.getTitaTlrNo() + parse.IntegerToString(tTxToDoDetail.getTitaTxtNo(), 8));
					occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
					occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
					occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());
					occursList.putParam("OOAcctCode", tTempVo.get("AcctCode"));

					cnt++;

					this.totaVo.addOccursList(occursList);
				}

			}
		}
		if (cnt == 0) {
//			throw new LogicException(titaVo, "E0001", "法拍費用檔"); // 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "無可墊付資料"); // 查詢資料不存在
		}

		if (custNo > 0) {
			// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
			if (slForeclosureFee != null && slForeclosureFee.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
			if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getAcctCode(ForeclosureFee f, ArrayList<BaTxVo> baTxList, String rvNo, TitaVo titaVo)
			throws LogicException {
		String wkAcctCode = "";
		if (baTxList == null || baTxList.size() == 0) {
			return wkAcctCode;
		}
		// 找銷帳檔acctCode
		for (BaTxVo ba : baTxList) {
			if (ba.getRepayType() == 7 && ba.getFacmNo() == f.getFacmNo()
					&& ba.getRvNo().equals(parse.IntegerToString(f.getRecordNo(), 7))) {
				wkAcctCode = ba.getAcctCode();
				break;
			}
		}
		return wkAcctCode;
	}

	// 檢查全戶為呆帳戶、債權轉讓戶、呆帳結案戶
	private Boolean checkStatus(int custNo , TitaVo titaVo) throws LogicException {
		boolean isAllBadLoan = true;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, 0, 999, 0, 900, 0,
				Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain t : slLoanBorMain.getContent()) {
				if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 7) {
					isAllBadLoan = false;					
				}
			}
		}
		return isAllBadLoan;

	}
	
}