package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FacStatusCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10<br>
 * Borrower=X,1<br>
 * END=X,1<br>
 */

@Service("L2902")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2902 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public GuarantorService sGuarantorService;

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService sFacCaseApplService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public FacStatusCom facStatusCom;

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService sLoanBorMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	/**
	 *
	 */
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2902 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 159 * 300 = 47700

		String iCustId = titaVo.getParam("CustId");
		BigDecimal GuaAmt = BigDecimal.ZERO;
		// new ArrayList
		List<Guarantor> lGuarantor = new ArrayList<Guarantor>();
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

		CustMain tCustMain = new CustMain();
		FacMain tFacMain = new FacMain();
		
		// 測試該統編是否存在保證人檔
		
		tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		if (tCustMain == null) {
			throw new LogicException("E2003", "該統編不存在客戶主檔 L2902(CustMain)");
		}
		
		List<LinkedHashMap<String, String>> chkOccursList = null;
		
		Slice<Guarantor> slGuarantor = sGuarantorService.guaUKeyEq(tCustMain.getCustUKey(), this.index, this.limit, titaVo);
		lGuarantor = slGuarantor == null ? null : slGuarantor.getContent();

		if (lGuarantor != null) {
			
		  for (Guarantor tGuarantor : lGuarantor) {
			// new occurs
			OccursList occurslist = new OccursList();
			// new Table
			tCustMain = new CustMain();
			tFacMain = new FacMain();

			lLoanBorMain = new ArrayList<LoanBorMain>();
			GuaAmt = BigDecimal.ZERO;
			int[] test = { 0, 0 };

			// 取額度號碼
			tFacMain = sFacMainService.facmApplNoFirst(tGuarantor.getApproveNo(), titaVo);
			if (tFacMain == null) {
				tFacMain = new FacMain();
			}
			int custNo = tFacMain.getCustNo();
			// 取戶號,戶名
			tCustMain = sCustMainService.custNoFirst(custNo, custNo, titaVo);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}
			// 取戶況,繳息迄日
			Slice<LoanBorMain> slLoanBorMain = sLoanBorMainService.bormCustNoEq(tFacMain.getCustNo(), tFacMain.getFacmNo(), tFacMain.getFacmNo(), 0, 900, 0, Integer.MAX_VALUE, titaVo);

			lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

			if (lLoanBorMain != null && !lLoanBorMain.isEmpty()) {
				test = judgeStatus(lLoanBorMain);
				occurslist.putParam("OOStat", 1);
			} else {
				occurslist.putParam("OOStat", 0);
			}

			if (lLoanBorMain != null) {
				for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
					GuaAmt = GuaAmt.add(tmpLoanBorMain.getLoanBal());
				}
			} else {
				GuaAmt = BigDecimal.ZERO;
			}

			occurslist.putParam("OCustNo", tFacMain.getCustNo());
			occurslist.putParam("OFacmNo", tFacMain.getFacmNo());
			occurslist.putParam("OOCustName", tCustMain.getCustName());
			occurslist.putParam("OOCustStat", test[0]);
			// 最高保證金額
			occurslist.putParam("OOHighGuaAmt", tGuarantor.getGuaAmt());
			occurslist.putParam("OOGuaAmt", GuaAmt);
			occurslist.putParam("OOPayIntDate", test[1]);
			occurslist.putParam("OOGuaStatCode",tGuarantor.getGuaStatCode());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		  } // for
		  
		  chkOccursList = this.totaVo.getOccursList();
			
		  /* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		  if (slGuarantor != null && slGuarantor.hasNext()) {
			  titaVo.setReturnIndex(this.setIndexNext());
			  /* 手動折返 */
			  this.totaVo.setMsgEndToEnter();
		  }
		} // if
		
		
		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException("E2003", ""); // 查無資料
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 根據該額度底下所有撥款戶戶況對應的優先度判斷tota應放的戶況及繳息迄日<BR>
	 * 優先度 : 戶況 : 中文<BR>
	 * 1 : 6 : 呆帳戶<BR>
	 * 2 : 7 : 部分轉呆戶<BR>
	 * 3 : 2 : 催收戶<BR>
	 * 4 : 4 : 逾期戶<BR>
	 * 5 : 0 : 正常戶<BR>
	 * 6 : 5 : 催收結案戶<BR>
	 * 7 : 8 : 債權轉讓戶<BR>
	 * 8 : 9 : 呆帳結案戶<BR>
	 * 9 : 3 : 結案戶<BR>
	 * 10 : 97 : 預約撥款已刪除<BR>
	 * 10 : 98 : 預約已撥款<BR>
	 * 10 : 99 : 預約撥款<BR>
	 * 
	 * @param lLoanBorMain 撥款資料
	 * @return [0]=戶況<BR>
	 *         [1]=繳息迄日
	 * @throws LogicException LogicException
	 */
	public int[] judgeStatus(List<LoanBorMain> lLoanBorMain) throws LogicException {
		int priorty = 10;
		int status = 0;
		int tbsdy = this.getTxBuffer().getTxCom().getTbsdy();
		int loandate = 0;
		int[] result = new int[2];

		for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
			if (tmpLoanBorMain.getStatus() < 90) {
//				if (tmpLoanBorMain.getStatus() > 1 && tmpLoanBorMain.getStatus() < 90) {
				int thisStatus = tmpLoanBorMain.getStatus();
				int thisPriorty;
				switch (thisStatus) {
				case 6:
					thisPriorty = 1;
					break;
				case 7:
					thisPriorty = 2;
					break;
				case 2:
					thisPriorty = 3;
					break;
				case 4:
					thisPriorty = 4;
					break;
				case 0:
					thisPriorty = 5;
					break;
				case 5:
					thisPriorty = 6;
					break;
				case 8:
					thisPriorty = 7;
					break;
				case 9:
					thisPriorty = 8;
					break;
				case 3:
					thisPriorty = 9;
					break;
				default:
					thisPriorty = 10;
					break;
				}

				if (thisPriorty < priorty) {
					status = thisStatus;
					if (tmpLoanBorMain.getPrevPayIntDate() == 0) {
						loandate = tmpLoanBorMain.getDrawdownDate();
					} else {
						loandate = tmpLoanBorMain.getPrevPayIntDate();
					}
					priorty = thisPriorty;
					// 判斷是否為逾期戶
					if (status == 0 && tmpLoanBorMain.getNextPayIntDate() < tbsdy) {
						dateUtil.init();
						dateUtil.setDate_1(tbsdy);
						dateUtil.setMons(-1);
						int payDate = dateUtil.getCalenderDay();
						if (tmpLoanBorMain.getNextPayIntDate() < payDate) { // 逾期超過一個月
							status = 4;
							priorty = 4;
							if (tmpLoanBorMain.getPrevPayIntDate() == 0) {
								loandate = tmpLoanBorMain.getDrawdownDate();
							} else {
								loandate = tmpLoanBorMain.getPrevPayIntDate();
							}
						}
					}
				}
			} // if
		} // for

		result[0] = status;
		result[1] = loandate;

		return result;
	}
}