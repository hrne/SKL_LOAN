package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4922")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L4922 extends TradeBuffer {

	@Autowired
	Parse parse;
	@Autowired
	BankAuthActService bankAuthActService;
	@Autowired
	CustMainService custMainService;
	@Autowired
	FacMainService facMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4922 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 3 + 75 * 600 = 45003

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		String iCustId = titaVo.getParam("CustId");

		// wk
		int wkCustNo = 0;
		Slice<BankAuthAct> slBankAuthAct;
		List<BankAuthAct> lBankAuthAct;
		Slice<FacMain> slFacMain;
		List<FacMain> lFacMain;

		// 依統編尋找戶號
		if (!"".equals(iCustId)) {
			CustMain tCustMain = custMainService.custIdFirst(iCustId, titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶主檔 統編 = " + iCustId); // 查詢資料不存在
			}
			wkCustNo = tCustMain.getCustNo();
		}
		if (iCustNo > 0) {
			wkCustNo = iCustNo;
		}

		// 戶號查所有額度資料
		slFacMain = facMainService.facmCustNoRange(wkCustNo, wkCustNo, 0, 999, this.index, this.limit, titaVo);
		lFacMain = slFacMain == null ? null : slFacMain.getContent();
		// 如沒有找到資料
		if (lFacMain == null || lFacMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "額度主檔"); // 查詢資料不存在
		}
		this.totaVo.putParam("OCustNo", lFacMain.get(0).getCustNo());
		// 如有有找到資料
		for (FacMain tFacMain : lFacMain) {
			slBankAuthAct = bankAuthActService.facmNoEq(tFacMain.getCustNo(), tFacMain.getFacmNo(), 0,
					Integer.MAX_VALUE, titaVo);

			lBankAuthAct = slBankAuthAct == null ? null : slBankAuthAct.getContent();
			OccursList occursList = new OccursList();
			occursList.putParam("OOFacmNo", tFacMain.getFacmNo());
			occursList.putParam("OORepayCode", tFacMain.getRepayCode());
			occursList.putParam("OORepayBank", "");
			occursList.putParam("OORepayAcct", "");
			occursList.putParam("OOStatus", "");
			if (lBankAuthAct != null) {
				occursList.putParam("OORepayBank", lBankAuthAct.get(0).getRepayBank());
				// 存款別處理
				if ("".equals(lBankAuthAct.get(0).getPostDepCode())) {
					occursList.putParam("OORepayAcct", lBankAuthAct.get(0).getRepayAcct());
				} else {
					occursList.putParam("OORepayAcct",
							lBankAuthAct.get(0).getPostDepCode() + lBankAuthAct.get(0).getRepayAcct());
				}

				occursList.putParam("OOStatus", lBankAuthAct.get(0).getStatus());
			}

			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slFacMain != null && slFacMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}