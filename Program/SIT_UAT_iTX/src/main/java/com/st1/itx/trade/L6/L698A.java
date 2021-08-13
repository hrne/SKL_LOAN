package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L698A")
@Scope("prototype")
/**
 * 應處理明細查詢
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L698A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L698A.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* DB服務注入 */
	@Autowired
	public TxToDoMainService txToDoMainService;

	/* DB服務注入 */
	@Autowired
	public TxToDoDetailService txToDoDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private int selectCode = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L698A ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 120;

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));

		String itemCode = titaVo.getParam("ItemCode");

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		Slice<TxToDoDetail> slTxToDoDetail = null;
//		! 1:昨日留存  0-上一營業日
//		! 2:本日新增  本營業日-本營業日
//		! 3:全部     0-9991231
//		! 4:本日處理  本營業日-本營業日
//		! 5:本日刪除  本營業日-本營業日
//		! 6:保留     0-9991231
//		! 7:未處理 0-9991231
//		! 9:未處理 (按鈕處理) 0-9991231
//   0.未處理
//   1.已保留
//   2.已處理
//   3.已刪除

		// 上一營業日
		int lbsDy = this.txBuffer.getTxCom().getLbsdy();
		// 本營業日
		int tbsDy = this.txBuffer.getTxCom().getTbsdy();

		switch (selectCode) {
		case 1:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0 + 19110000, lbsDy + 19110000,
					this.index, this.limit, titaVo);
			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, tbsDy + 19110000, tbsDy + 19110000,
					this.index, this.limit, titaVo);
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0 + 19110000, 9991231 + 19110000,
					this.index, this.limit, titaVo);
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 2, 2, tbsDy + 19110000, tbsDy + 19110000,
					this.index, this.limit, titaVo);
		case 5:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 3, 3, tbsDy + 19110000, tbsDy + 19110000,
					this.index, this.limit, titaVo);
		case 6:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 1, 1, 0 + 19110000, 9991231 + 19110000,
					this.index, this.limit, titaVo);
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0 + 19110000, 9991231 + 19110000,
					this.index, this.limit, titaVo);
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0 + 19110000, 9991231 + 19110000,
					this.index, this.limit, titaVo);
			break;
		default:
			break;
		}
		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {

			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
//				if (selectCodeIsNotQualify(tTxToDoDetail)) {
//					continue;
//				}

				int custno = tTxToDoDetail.getCustNo();
				String csutNm = "";

				if (custno != 0) {
					CustMain tCustMain = custMainService.custNoFirst(custno, custno);
					if (tCustMain != null) {
						csutNm = tCustMain.getCustName();
					}
				}

				OccursList occursList = new OccursList();

				occursList.putParam("OOAcDate", tTxToDoDetail.getDataDate());
				occursList.putParam("OOStatus", tTxToDoDetail.getStatus());
				occursList.putParam("OOCustNo", custno);
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
				occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
				occursList.putParam("OOCustName", csutNm);
				occursList.putParam("OOProcessNote", tTxToDoDetail.getProcessNote());
				occursList.putParam("OOTxSn", tTxToDoDetail.getTitaEntdy() + titaVo.getKinbr()
						+ tTxToDoDetail.getTitaTlrNo() + tTxToDoDetail.getTitaTxtNo()); // 登放序號
				occursList.putParam("OOExcuteTxcd", tTxToDoDetail.getExcuteTxcd());
				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);

			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean selectCodeIsNotQualify(TxToDoDetail tTxToDoDetail) throws LogicException {
		Boolean result = false;
		int today = this.getTxBuffer().getTxCom().getTbsdy();
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