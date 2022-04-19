package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6981")
@Scope("prototype")
/**
 * 放款轉列催收作業
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6981 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public BaTxCom baTxCom;

	@Autowired
	public CustMainService sCustMainService;

	private int selectCode = 0;
	private int trasCollDate = 0;

	int ovduMonth = 0; // 逾期日期-月
	int ovduDay = 0; // 逾期日期-日
	private TempVo tTempVo = new TempVo();
	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6981 ");
		this.totaVo.init(titaVo);
		baTxCom.setTxBuffer(this.txBuffer);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 300; // 172 * 300 = 51600

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
		trasCollDate = parse.stringToInteger(titaVo.getParam("TransCollDate"));

// 0.未處理
// 1.已保留
// 2.已處理
// 3.已刪除

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		Slice<TxToDoDetail> slTxToDoDetail = null;
//		TRLN00 放款轉列催收

//		! 1:昨日留存 
//		! 2:本日新增 
//		! 3:全部     
//		! 4:本日處理 
//		! 5:本日刪除 
//		! 6:保留    
//		! 7:未處理
//		! 9:未處理 (按鈕處理)

		switch (selectCode) {
		case 1:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 2, 2, this.index, this.limit, titaVo);
			break;
		case 5:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 3, 3, this.index, this.limit, titaVo);
			break;
		case 6:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 1, 1, this.index, this.limit, titaVo);
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 0, 0, this.index, this.limit, titaVo);
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("TRLN00", 0, 0, this.index, this.limit, titaVo);
			break;
		default:
			break;
		}

		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				OccursList occursList = new OccursList();

				if (selectCodeIsNotQualify(tTxToDoDetail)) {
					continue;
				}

				CustMain tCustMain = new CustMain();
				tCustMain = sCustMainService.custNoFirst(tTxToDoDetail.getCustNo(), tTxToDoDetail.getCustNo(), titaVo);

				occursList.putParam("OOProcStatus", tTxToDoDetail.getStatus());
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
				if (tCustMain != null) {
					occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名
				} else {
					occursList.putParam("OOCustName", ""); // 戶名
				}
				tTempVo = tTempVo.getVo(tTxToDoDetail.getProcessNote());
				occursList.putParam("OOOvduMonth", tTempVo.get("OvduMonth")); // 逾期日期-月
				occursList.putParam("OOOvduDay", tTempVo.get("OvduDay")); // 逾期日期-日
				occursList.putParam("OOOvduPrinAmt", tTempVo.get("OvduPrinAmt")); // 轉催收本金
				occursList.putParam("OOOvduIntAmt", tTempVo.get("OvduIntAmt")); // 轉催收利息
				occursList.putParam("OOOvduAmt", tTempVo.get("OvduAmt")); // 轉催收金額
				occursList.putParam("OORelNo", tTxToDoDetail.getTitaEntdy() + tTxToDoDetail.getTitaKinbr() + tTxToDoDetail.getTitaTlrNo() + parse.IntegerToString(tTxToDoDetail.getTitaTxtNo(), 8));

				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());

				cnt++;
				this.totaVo.addOccursList(occursList);
			}
		}

		if (cnt == 0) {
			throw new LogicException(titaVo, "E0001", "放款轉列催收作業"); // 查詢資料不存在
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean selectCodeIsNotQualify(TxToDoDetail tTxToDoDetail) throws LogicException {
		Boolean result = false;
		int today = this.getTxBuffer().getTxCom().getTbsdy();

		switch (selectCode) {
		case 1:
			if (tTxToDoDetail.getDataDate() >= today) {
				result = true;
			}
			break;
		case 2:
			if (tTxToDoDetail.getDataDate() != today) {
				result = true;
			}
			break;
		default:
			break;
		}

		return result;
	}
}
