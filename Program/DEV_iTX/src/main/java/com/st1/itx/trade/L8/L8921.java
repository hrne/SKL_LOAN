package com.st1.itx.trade.L8;

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
import com.st1.itx.db.domain.MlaundryChkDtl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryChkDtlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcDateStart=9,7 AcDateEnd=9,7 END=X,1
 */

@Service("L8921") // 疑似洗錢樣態檢核明細檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L8921 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryChkDtlService sMlaundryChkDtlService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8921 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";
		int iAcDateStart = this.parse.stringToInteger(titaVo.getParam("AcDateStart"));
		int iAcDateEnd = this.parse.stringToInteger(titaVo.getParam("AcDateEnd"));
		int iFAcDateStart = iAcDateStart + 19110000;
		int iFAcDateEnd = iAcDateEnd + 19110000;
		int iFactor = this.parse.stringToInteger(titaVo.getParam("Factor"));
		this.info("L8921 iFAcDate : " + iFAcDateStart + "~" + iFAcDateEnd);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 255 * 200 = 51,000

		// 查詢疑似洗錢樣態檢核明細檔檔
		Slice<MlaundryChkDtl> slMlaundryChkDtl;
		if(iFactor==0) {
			slMlaundryChkDtl = sMlaundryChkDtlService.findEntryDateRange(iFAcDateStart, iFAcDateEnd, this.index, this.limit, titaVo);
		} else {
			slMlaundryChkDtl = sMlaundryChkDtlService.findFactor(iFAcDateStart, iFAcDateEnd, iFactor, this.index, this.limit, titaVo);
		}
		
		List<MlaundryChkDtl> lMlaundryChkDtl = slMlaundryChkDtl == null ? null : slMlaundryChkDtl.getContent();

		if (lMlaundryChkDtl == null || lMlaundryChkDtl.size() == 0) {
			throw new LogicException(titaVo, "E0001", "疑似洗錢樣態檢核明細檔"); // 查無資料
		}
		// 如有找到資料
		for (MlaundryChkDtl tMlaundryChkDtl : lMlaundryChkDtl) {
			OccursList occursList = new OccursList();

			// 查詢客戶資料主檔
			CustMain tCustMain = new CustMain();
			tCustMain = sCustMainService.custNoFirst(tMlaundryChkDtl.getCustNo(), tMlaundryChkDtl.getCustNo(), titaVo);
			if (tCustMain == null) {
				occursList.putParam("OOCustName", ""); // 戶名
			} else {
				occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名
			}
			

			occursList.putParam("OOFactor", tMlaundryChkDtl.getFactor()); // 交易樣態
			occursList.putParam("OOEntryDate", tMlaundryChkDtl.getEntryDate()); // 入帳日期
			occursList.putParam("OOCustNo", tMlaundryChkDtl.getCustNo()); // 戶號
			occursList.putParam("OORepayItem", tMlaundryChkDtl.getRepayItem()); // 來源
			occursList.putParam("OODscptCode", tMlaundryChkDtl.getDscptCode()); // 匯款摘要
			occursList.putParam("OOTxAmt", tMlaundryChkDtl.getTxAmt()); // 交易金額
			occursList.putParam("OOTotalAmt", tMlaundryChkDtl.getTotalAmt()); // 累計金額
			occursList.putParam("OOTotalCnt", tMlaundryChkDtl.getTotalCnt()); // 累計筆數
			occursList.putParam("OOStartEntryDate", tMlaundryChkDtl.getStartEntryDate()); // 統計期間起日

			DateTime = this.parse.timeStampToString(tMlaundryChkDtl.getCreateDate()); // 產製日期
			this.info("L8921 DateTime : " + DateTime);
			Date = FormatUtil.left(DateTime, 9);
			occursList.putParam("OOCreateDate", Date);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slMlaundryChkDtl != null && slMlaundryChkDtl.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}