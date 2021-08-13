package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * L2016 核准號碼明細資料查詢
 * a.此功能提供以核准號碼查詢額度之明細資料
 */
/*
 * Tita
 * ApplNo1=9,7
 * ApplNo2=9,7
 * ColSetCode=9,1 0:查詢全部;1:查詢未設定;2:查詢已設定
 */
/**
 * L2016 核准號碼明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2016")
@Scope("prototype")
public class L2016 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2016.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2016 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo1 = this.parse.stringToInteger(titaVo.getParam("ApplNo1"));
		int iApplNo2 = this.parse.stringToInteger(titaVo.getParam("ApplNo2"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		String iCustName = titaVo.getParam("CustName");
		int iColSetCode = this.parse.stringToInteger(titaVo.getParam("ColSetCode"));
		String wkColSetFlag;
		switch (iColSetCode) {
		case 0:
			wkColSetFlag = "%";
			break;
		case 1:
			wkColSetFlag = "N%";
			break;
		case 2:
			wkColSetFlag = "Y%";
			break;
		default:
			throw new LogicException(titaVo, "E2004", "設定擔保品記號"); // 功能選擇錯誤
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 159 * 300 = 47700
		
		Slice<FacMain> lFacMain = null;
		Slice<CustMain> iCustMain = null;
		CustMain tCustMain;
		int Case = 0;
		
		if (iApplNo1 > 0) { // 當輸入核准號碼時
			Case = 1;
			
			lFacMain = facMainService.facmApplNoRange(iApplNo1, iApplNo2, 1, 999, wkColSetFlag, this.index, this.limit,
					titaVo);
		} else if(iCustNo > 0){ // 輸入戶號時
			Case = 2;
			
			lFacMain = facMainService.fildCustNoRange(iCustNo, iCustNo, wkColSetFlag, this.index, this.limit, titaVo);
		} else {  // 輸入姓名
			Case = 3;
		}
		
		switch(Case) {
		  case 1:
		  case 2:
			  
			  if (lFacMain == null || lFacMain.isEmpty()) {
					throw new LogicException(titaVo, "E2003", "額度主檔"); // 查無資料
			  }
			  
			  for (FacMain tFacMain : lFacMain.getContent()) {
					OccursList occursList = new OccursList();
					
					String dShow = "Y";
					// 該額度有撥款或預約撥款,不顯示刪除按鈕
					if (tFacMain.getLastBormNo() > 0 || tFacMain.getLastBormRvNo() > 900) {
						// hide
						dShow = "N";
					}
					
					occursList.putParam("OOApplNo", tFacMain.getApplNo());
					occursList.putParam("OOCustNo", tFacMain.getCustNo());
					occursList.putParam("OOFacmNo", tFacMain.getFacmNo());
					occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
					occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
					occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
					occursList.putParam("OOColSetFlag", tFacMain.getColSetFlag());
					occursList.putParam("OOLoanFg", dShow);
					
					tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);

					if (tCustMain != null) {
						occursList.putParam("OOCustName", tCustMain.getCustName());
					} else {
						// occursList.putParam("OOCustNoX", "");
						throw new LogicException(titaVo, "E2003", "客戶資料主檔 戶號=" + tFacMain.getCustNo()); // 查無資料
					}
					// 將每筆資料放入Tota的OcList
					this.totaVo.addOccursList(occursList);
				}

				// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
				if (lFacMain != null && lFacMain.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}
			  break;
		  case 3:			  
			  iCustMain = custMainService.custNameLike("%" + iCustName + "%", this.index, this.limit, titaVo);
			  
			  if (iCustMain == null) {
				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
			  }
			  
			  List<CustMain> lCustMain = new ArrayList<CustMain>();			
			  lCustMain = iCustMain == null ? null : iCustMain.getContent();
				
			  for( CustMain tlCustMain: lCustMain) {
				iCustNo = tlCustMain.getCustNo();		
				lFacMain = facMainService.fildCustNoRange(iCustNo, iCustNo, wkColSetFlag, this.index, this.limit, titaVo);
				
				if (lFacMain != null) {
				
				  for (FacMain tFacMain : lFacMain.getContent()) {
					OccursList occursList = new OccursList();
					
					String dShow = "Y";
					// 該額度有撥款或預約撥款,不顯示刪除按鈕
					if (tFacMain.getLastBormNo() > 0 || tFacMain.getLastBormRvNo() > 900) {
						// hide
						dShow = "N";
					}
					
					occursList.putParam("OOApplNo", tFacMain.getApplNo());
					occursList.putParam("OOCustNo", tFacMain.getCustNo());
					occursList.putParam("OOFacmNo", tFacMain.getFacmNo());
					occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
					occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
					occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
					occursList.putParam("OOColSetFlag", tFacMain.getColSetFlag());
					occursList.putParam("OOLoanFg", dShow);
					
					tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);

					if (tCustMain != null) {
						occursList.putParam("OOCustName", tCustMain.getCustName());
					} else {
						// occursList.putParam("OOCustNoX", "");
						throw new LogicException(titaVo, "E2003", "客戶資料主檔 戶號=" + tFacMain.getCustNo()); // 查無資料
					}
					// 將每筆資料放入Tota的OcList
					this.totaVo.addOccursList(occursList);
				  }
			    
				  // 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
				  if (lFacMain != null && lFacMain.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				  }
			    } // if
			  } // for
			  
			  if (this.totaVo.getOccursList() == null || this.totaVo.getOccursList().isEmpty()) {
					throw new LogicException(titaVo, "E2003", "額度主檔"); // 查無資料
			  }
		  default:
			  break;
		}
		
		
		
//		if (iApplNo1 > 0) { // 當輸入核准號碼時
//			lFacMain = facMainService.facmApplNoRange(iApplNo1, iApplNo2, 1, 999, wkColSetFlag, this.index, this.limit,
//					titaVo);
//		} else if(iCustNo > 0){ // 輸入戶號時
//			lFacMain = facMainService.fildCustNoRange(iCustNo, iCustNo, wkColSetFlag, this.index, this.limit, titaVo);
//		} else { 
//			Slice<CustMain> iCustMain = null;
//			iCustMain = custMainService.custNameLike("%" + iCustName + "%", this.index, this.limit, titaVo);
//			if (iCustMain == null) {
//				throw new LogicException("E0001", "客戶檔查無資料"); // 查無資料
//			}
//			List<CustMain> lCustMain = new ArrayList<CustMain>();			
//			lCustMain = iCustMain == null ? null : iCustMain.getContent();
//			
//			for( CustMain tCustMain: lCustMain) {
//				iCustNo = tCustMain.getCustNo();		
//				sFacMain = facMainService.fildCustNoRange(iCustNo, iCustNo, wkColSetFlag, this.index, this.limit, titaVo);
//	
//				List<FacMain> tFacMain = new ArrayList<FacMain>();
//				tFacMain = sFacMain == null ? null : sFacMain.getContent();
//				
//				
//			}
//		}
//
//		if (lFacMain == null || lFacMain.isEmpty()) {
//			throw new LogicException(titaVo, "E2003", "額度主檔"); // 查無資料
//		}
//
//		// 如有有找到資料
//		CustMain tCustMain;
//		for (FacMain tFacMain : lFacMain.getContent()) {
//			OccursList occursList = new OccursList();
//			
//			String dShow = "Y";
//			// 該額度有撥款或預約撥款,不顯示刪除按鈕
//			if (tFacMain.getLastBormNo() > 0 || tFacMain.getLastBormRvNo() > 900) {
//				// hide
//				dShow = "N";
//			}
//			
//			occursList.putParam("OOApplNo", tFacMain.getApplNo());
//			occursList.putParam("OOCustNo", tFacMain.getCustNo());
//			occursList.putParam("OOFacmNo", tFacMain.getFacmNo());
//			occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
//			occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
//			occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
//			occursList.putParam("OOColSetFlag", tFacMain.getColSetFlag());
//			occursList.putParam("OOLoanFg", dShow);
//			
//			tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
//
//			if (tCustMain != null) {
//				occursList.putParam("OOCustName", tCustMain.getCustName());
//			} else {
//				// occursList.putParam("OOCustNoX", "");
//				throw new LogicException(titaVo, "E2003", "客戶資料主檔 戶號=" + tFacMain.getCustNo()); // 查無資料
//			}
//			// 將每筆資料放入Tota的OcList
//			this.totaVo.addOccursList(occursList);
//		}
//
//		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
//		if (lFacMain != null && lFacMain.hasNext()) {
//			titaVo.setReturnIndex(this.setIndexNext());
//			/* 手動折返 */
//			this.totaVo.setMsgEndToEnter();
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}