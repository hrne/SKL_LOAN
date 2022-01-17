package com.st1.itx.trade.L7;

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
import com.st1.itx.db.domain.Ias39Loss;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.Ias39LossService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

/**
 * Tita CustNo=9,7 FacmNo=9,3 END=X,1
 */

@Service("L7904") // 特殊客觀減損狀況查詢
@Scope("prototype")
/**
 *
 *
 * @author CSChen
 * @version 1.0.0
 */

public class L7904 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public Ias39LossService sIas39LossService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7904 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		this.info("*** L7904 tita=" + iCustNo + ", " + iFacmNo);

		int iCustNoStart;
		int iCustNoEnd;
		int iFacmNoStart;
		int iFacmNoEnd;

		if (iCustNo == 0) {
			iCustNoStart = 0;
			iCustNoEnd = 9999999;
		} else {
			iCustNoStart = iCustNo;
			iCustNoEnd = iCustNo;
		}

		if (iFacmNo == 0) {
			iFacmNoStart = 0;
			iFacmNoEnd = 999;
		} else {
			iFacmNoStart = iFacmNo;
			iFacmNoEnd = iFacmNo;
		}
		this.info("*** L7904 iCustNo=" + iCustNoStart + "~" + iCustNoEnd);
		this.info("*** L7904 iFacmNo=" + iFacmNoStart + "~" + iFacmNoEnd);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		// 查詢特殊客觀減損狀況檔
		Slice<Ias39Loss> slIas39Loss;
		slIas39Loss = sIas39LossService.findCustNo(iCustNoStart, iCustNoEnd, iFacmNoStart, iFacmNoEnd, this.index, this.limit, titaVo);
		List<Ias39Loss> lIas39Loss = slIas39Loss == null ? null : slIas39Loss.getContent();

		if (lIas39Loss == null || lIas39Loss.size() == 0) {
			throw new LogicException(titaVo, "E0001", "特殊客觀減損狀況檔"); // 查無資料
		}

		// 如有找到資料
		for (Ias39Loss tIas39Loss : lIas39Loss) {
			OccursList occursList = new OccursList();
			CustMain tCustMain = new CustMain();

			occursList.putParam("OOCustNo", tIas39Loss.getCustNo());
			occursList.putParam("OOFacmNo", tIas39Loss.getFacmNo());

			tCustMain = sCustMainService.custNoFirst(tIas39Loss.getCustNo(), tIas39Loss.getCustNo(), titaVo);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}
			occursList.putParam("OOCustName", StringCut.replaceLineUp(tCustMain.getCustName()));

			occursList.putParam("OOMarkDate", tIas39Loss.getMarkDate());
			occursList.putParam("OOMarkCode", tIas39Loss.getMarkCode());
			occursList.putParam("OOStartDate", tIas39Loss.getStartDate());
			occursList.putParam("OOEndDate", tIas39Loss.getEndDate());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slIas39Loss != null && slIas39Loss.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}