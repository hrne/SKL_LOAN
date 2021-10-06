package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FacStatusCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ReceiveDate=9,7<br>
 */

@Service("L2605")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2605 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public LoanBorTxService sLoanBorTxService;

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService sLoanBorMainService;

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public CollListService sCollListService;

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	/* DB服務注入 */
	@Autowired
	public FacStatusCom facStatusCom;

	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdAreaService cdAreaService;
	@Autowired
	public CdLandSectionService cdLandSectionService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	int consiz = 0;
	// 法拍費總計
	BigDecimal fee = BigDecimal.ZERO;
	// 催收法拍費總計
	BigDecimal overdueFee = BigDecimal.ZERO;
	// 同額度法拍費
	BigDecimal facmFee = BigDecimal.ZERO;
	// 同額度催收法拍費
	BigDecimal facmOverdueFee = BigDecimal.ZERO;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2605 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		// tita
		// 收件迄日
		int iReceiveDateStart = 19110000;
		int iReceiveDate = parse.stringToInteger(titaVo.getParam("ReceiveDate")) + 19110000;

		// new Slice
		Slice<LoanBorMain> sLoanBorMain;
		Slice<LoanBorTx> slLoanBorTx;

		// new ArrayList
		List<LoanBorMain> lLoanBorMain;
		List<LoanBorTx> lLoanBorTx;

		// new table
		ClMain tClMain;
		CollList tCollList;
		CustMain tCustMain;
		CdCity tCdCity;

				
		Slice<ForeclosureFee> slForeclosureFee = sForeclosureFeeService.receiveDatecloseZero(iReceiveDateStart, iReceiveDate, 0,this.index, this.limit);
		List<ForeclosureFee> lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
		
		
		if (lForeclosureFee == null || lForeclosureFee.isEmpty()) {
			throw new LogicException("E0001", "L2605該收件迄日在法拍費用檔無資料");
		}

		
		int listsize = lForeclosureFee.size();

		this.info("listsize = " + listsize);
		for (int i = 0; i < listsize; i++) {
			// 本筆
			ForeclosureFee tmpFF = lForeclosureFee.get(i);

//			if (tmpFF.getCloseDate() > 0) {
//				continue;
//			}
		
			// 與下一筆比較戶號(比到額度層)及收件日
			int nextCustNo;
			int nextFacmNo;
			int nextReceiveDate;

			// 最後一筆處理
			if (i == listsize - 1) {
				nextCustNo = 0;
				nextFacmNo = 0;
				nextReceiveDate = 0;
			} else {
				ForeclosureFee nextFF = lForeclosureFee.get(i + 1);
				nextCustNo = nextFF.getCustNo();
				nextFacmNo = nextFF.getFacmNo();
				nextReceiveDate = nextFF.getReceiveDate();
			}

			// 計算法拍費總計 & 催收法拍費總計 & 同額度法拍費 & 同額度催收法拍費
			// (根據是否有催收日期)
			if (tmpFF.getOverdueDate() == 0) {
				fee = fee.add(tmpFF.getFee());
				facmFee = facmFee.add(tmpFF.getFee());
			} else {
				overdueFee = overdueFee.add(tmpFF.getFee());
				facmOverdueFee = facmOverdueFee.add(tmpFF.getFee());
			}

			// 若此筆與下筆的戶號(比到額度層)或收件日不同,放入tota occurs
			if (tmpFF.getCustNo() != nextCustNo || tmpFF.getFacmNo() != nextFacmNo || tmpFF.getReceiveDate() != nextReceiveDate) {

				tCustMain = sCustMainService.custNoFirst(tmpFF.getCustNo(), tmpFF.getCustNo());

				if (tCustMain == null) {
					throw new LogicException("E0001", "L2605該戶號在客戶主檔無資料" + tmpFF.getCustNo());
				}

				int[] test = { 0, 0 };

				// 取本戶累溢短收
				BigDecimal overflowShortfall = BigDecimal.ZERO;

				slLoanBorTx = sLoanBorTxService.borxFacmNoEq(tmpFF.getCustNo(), tmpFF.getFacmNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
				lLoanBorTx = slLoanBorTx == null ? null : slLoanBorTx.getContent();

				if (lLoanBorTx != null && !lLoanBorTx.isEmpty()) {
					// 累溢短收
					for (LoanBorTx tmpLoanBorTx : lLoanBorTx) {

						overflowShortfall = overflowShortfall.add(tmpLoanBorTx.getOverflow()).subtract(tmpLoanBorTx.getShortfall());
						this.info("原+溢收-短收 = " + overflowShortfall + "+" + tmpLoanBorTx.getOverflow() + "-" + tmpLoanBorTx.getShortfall());
					}
				}
				this.info("累溢短收 = " + overflowShortfall);

				sLoanBorMain = sLoanBorMainService.bormCustNoEq(tmpFF.getCustNo(), tmpFF.getFacmNo(), tmpFF.getFacmNo(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
				lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

				if (lLoanBorMain != null && !lLoanBorMain.isEmpty()) {
					test = judgeStatus(lLoanBorMain);
				}

				// 取地區別
				int clcode1 = 0;
				int clcode2 = 0;
				int clno = 0;
				String cityCode = "";
				String cityItem = "";

				ClFac tClFac = sClFacService.mainClNoFirst(tmpFF.getCustNo(), tmpFF.getFacmNo(), "Y", titaVo);

				if (tClFac != null) {
					clcode1 = tClFac.getClCode1();
					clcode2 = tClFac.getClCode2();
					clno = tClFac.getClNo();
					if (clcode1 != 0 && clcode2 != 0 && clno != 0) {
						tClMain = sClMainService.findById(new ClMainId(clcode1, clcode2, clno));
						if (tClMain != null) {
							cityCode = parse.IntegerToString(parse.stringToInteger(tClMain.getCityCode()), 2);
							this.info("地區別代號 = " + cityCode);
							/* 取縣市名稱 */
							tCdCity = cdCityService.findById(cityCode, titaVo);

							if (tCdCity != null) {
								cityItem = tCdCity.getCityItem();
								this.info("縣市 = " + cityItem);
							}
						}
					}
				}

				// 催收人員
				String accCollPsn = "";
				tCollList = sCollListService.findById(new CollListId(tmpFF.getCustNo(), tmpFF.getFacmNo()));
				if (tCollList != null) {
					accCollPsn = tCollList.getAccCollPsn();
				}
				this.info("test01" + "=" + test[0] + ";" + test[1]);
				// new occurs
				OccursList occurslist = new OccursList();
				occurslist.putParam("OOCustNo", tmpFF.getCustNo());
				occurslist.putParam("OOFacmNo", tmpFF.getFacmNo());
				occurslist.putParam("OOCustName", tCustMain.getCustName());
				occurslist.putParam("OOReceiveDate", tmpFF.getReceiveDate());
				occurslist.putParam("OOFee", facmFee);
				occurslist.putParam("OOFee1", facmOverdueFee);
				occurslist.putParam("OOFeeAmt", facmOverdueFee.add(facmFee));
				// 本戶累溢短收
				occurslist.putParam("OOShortfallOverflow", overflowShortfall);
				occurslist.putParam("OOCityCode", cityCode);
				occurslist.putParam("OOCityCodeX", cityItem);
				occurslist.putParam("OOStatus", parse.IntegerToString(test[0], 2)); // 戶況
				occurslist.putParam("OOPrevPayIntDate", test[1]); // 繳息迄日
				occurslist.putParam("OOCollPsn", accCollPsn); // 催收人員
				occurslist.putParam("OOOverdueCode", tmpFF.getOverdueDate() != 0 ? "Y" : " ");

				consiz++;
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);

				// 因為下筆資料不同,將同額度法拍費用及同額度催收法拍費用歸零
				facmFee = BigDecimal.ZERO;
				facmOverdueFee = BigDecimal.ZERO;
			} // if
		} // for		
		
//		// Header資料
		if(this.index == 0 ) {
			this.totaVo.putParam("OCnt", consiz);
			this.totaVo.putParam("OFee", fee);
			this.totaVo.putParam("OOverFee", overdueFee);
			this.totaVo.putParam("OFeeAmt", fee.add(overdueFee));
			titaVo.putParam("OCnt", consiz);
			titaVo.putParam("OFee", fee);
			titaVo.putParam("OOverFee", overdueFee);
			titaVo.putParam("OFeeAmt", fee.add(overdueFee));
		} else {
			this.totaVo.putParam("OCnt", consiz + parse.stringToInteger(titaVo.getParam("OCnt")));
			this.totaVo.putParam("OFee", fee.add(parse.stringToBigDecimal(titaVo.getParam("OFee"))));
			this.totaVo.putParam("OOverFee", overdueFee.add(parse.stringToBigDecimal(titaVo.getParam("OOverFee"))));
			this.totaVo.putParam("OFeeAmt", (fee.add(overdueFee).add(parse.stringToBigDecimal(titaVo.getParam("OFeeAmt")))));
			
			titaVo.putParam("OCnt", consiz + parse.stringToInteger(titaVo.getParam("OCnt")));
			titaVo.putParam("OFee", fee.add(parse.stringToBigDecimal(titaVo.getParam("OFee"))));
			titaVo.putParam("OOverFee", overdueFee.add(parse.stringToBigDecimal(titaVo.getParam("OOverFee"))));
			titaVo.putParam("OFeeAmt", (fee.add(overdueFee).add(parse.stringToBigDecimal(titaVo.getParam("OFeeAmt")))));
		}
		

		if (lForeclosureFee.size() == this.limit && slForeclosureFee.hasNext()) {
			 titaVo.setReturnIndex(this.setIndexNext());
			 
				/* 手動折返 */
			 this.totaVo.setMsgEndToEnter();
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
			if (tmpLoanBorMain.getStatus() > 1 && tmpLoanBorMain.getStatus() < 90) {
				int thisStatus = tmpLoanBorMain.getStatus();
				int thisPriorty;
				loandate = tmpLoanBorMain.getDrawdownDate();
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
				this.info("thisPriorty  = " + thisPriorty);
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
			}
		}

		result[0] = status;
		result[1] = loandate;

		return result;
	}
}