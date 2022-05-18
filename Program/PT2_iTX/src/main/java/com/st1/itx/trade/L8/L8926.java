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
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryChkDtlService;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcDateStart=9,7 AcDateEnd=9,7 END=X,1
 */

@Service("L8926")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L8926 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryChkDtlService sMlaundryChkDtlService;
	@Autowired
	MlaundryDetailService sMlaundryDetailService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	CdCodeService sCdCodeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8926 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFactor = this.parse.stringToInteger(titaVo.getParam("Factor"));
		this.info("L8926 iAcDate=" + iAcDate);
		this.info("L8926 iCustNo=" + iCustNo);
		this.info("L8926 iFactor=" + iFactor);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 255 * 200 = 51,000

		// 查詢疑似洗錢樣態檢核明細檔檔
		Slice<MlaundryChkDtl> slMlaundryChkDtl;
		slMlaundryChkDtl = sMlaundryChkDtlService.findEntryDateRangeFactorCustNo(iAcDate, iAcDate, iFactor, iCustNo, this.index, this.limit, titaVo);

		List<MlaundryChkDtl> lMlaundryChkDtl = slMlaundryChkDtl == null ? new ArrayList<MlaundryChkDtl>() : slMlaundryChkDtl.getContent();

		MlaundryDetail tMlaundryDetail = sMlaundryDetailService.findEntryDateRangeFactorCustNoFirst(iAcDate, iAcDate, iFactor, iCustNo, titaVo);
		
		if (tMlaundryDetail == null) {
			throw new LogicException(titaVo, "E0001", "疑似洗錢交易合理性明細檔"); // 查無資料
		}
		
		totaVo.putParam("OEntryDate", tMlaundryDetail.getEntryDate());
		totaVo.putParam("OCustNo", tMlaundryDetail.getCustNo());
		
		CustMain tCustMainOuter = sCustMainService.custNoFirst(tMlaundryDetail.getCustNo(), tMlaundryDetail.getCustNo(), titaVo);
		
		totaVo.putParam("OCustName", tCustMainOuter == null ? "" : tCustMainOuter.getCustName());
		totaVo.putParam("OFactor", tMlaundryDetail.getFactor());
		totaVo.putParam("OTotalAmt", tMlaundryDetail.getTotalAmt());
		totaVo.putParam("OTotalCnt", tMlaundryDetail.getTotalCnt());
		totaVo.putParam("ORational", tMlaundryDetail.getRational());
		totaVo.putParam("OEmpNoDesc", tMlaundryDetail.getEmpNoDesc() != null ? tMlaundryDetail.getEmpNoDesc().replace("$n", "\n") : "");
		totaVo.putParam("OManagerCheck", tMlaundryDetail.getManagerCheck());
		totaVo.putParam("OManagerCheckDate", tMlaundryDetail.getManagerCheckDate());
		totaVo.putParam("OManagerDate", tMlaundryDetail.getManagerDate());
		totaVo.putParam("OManagerDesc", tMlaundryDetail.getManagerDesc() != null ? tMlaundryDetail.getManagerDesc().replace("$n", "\n") : "");
		
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
			
			// 匯款摘要
			CdCode tCdCode = sCdCodeService.getItemFirst(4, "BankRmftCode", tMlaundryChkDtl.getDscptCode(), titaVo);
			occursList.putParam("OODscptCode", tCdCode != null ? tCdCode.getItem() : "");
			
			occursList.putParam("OOTxAmt", tMlaundryChkDtl.getTxAmt()); // 交易金額
			occursList.putParam("OOTotalAmt", tMlaundryChkDtl.getTotalAmt()); // 累計金額
			occursList.putParam("OOTotalCnt", tMlaundryChkDtl.getTotalCnt()); // 累計筆數
			occursList.putParam("OOStartEntryDate", tMlaundryChkDtl.getStartEntryDate()); // 統計期間起日

			DateTime = this.parse.timeStampToString(tMlaundryChkDtl.getCreateDate()); // 產製日期
			this.info("L8926 DateTime : " + DateTime);
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