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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6989")
@Scope("prototype")
/**
 * 攤提到期作業
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6989 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanCom loanCom;

	private int selectCode = 0;
	private int trasCollDate = 0;
	private int cnt = 0;
	private TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6989 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 400; // 126 * 400 = 50400

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
		trasCollDate = parse.stringToInteger(titaVo.getParam("TransCollDate"));

		this.info("selectCode = " + selectCode);
		this.info("trasCollDate = " + trasCollDate);
		// 0.未處理
		// 1.已保留
		// 2.已處理
		// 3.已刪除

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		Slice<TxToDoDetail> slTxToDoDetail = null;
		Slice<AcReceivable> slAcReceivable = null;

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
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 0, 3, this.index, this.limit, titaVo);
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 2, 2, this.index, this.limit, titaVo);
			break;
		case 5:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 3, 3, this.index, this.limit, titaVo);
			break;
		case 6:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 1, 1, this.index, this.limit, titaVo);
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 0, 0, this.index, this.limit, titaVo);
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("SLCL00", 0, 0, this.index, this.limit, titaVo);
			break;
		default:
			break;
		}
		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
		this.info("lTxToDoDetail = " + lTxToDoDetail);

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				OccursList occursList = new OccursList();
				AcReceivable tAcReceivable = new AcReceivable();
				String wkAcctCode = "";
				String wkTempItemCode = "";
				int wkSyndFeeYYMM = 0;
				int wkOpenAcDate = 0;
				BigDecimal wkSyndFee = BigDecimal.ZERO;
				if (selectCodeIsNotQualify(tTxToDoDetail)) {
					continue;
				}

				tTempVo = tTempVo.getVo(tTxToDoDetail.getProcessNote());
				wkAcctCode = tTempVo.get("AcctCode");
				wkTempItemCode = wkAcctCode.substring(1, 3);
				tAcReceivable = acReceivableService.findById(new AcReceivableId(wkAcctCode, tTxToDoDetail.getCustNo(),
						tTxToDoDetail.getFacmNo(), tTxToDoDetail.getDtlValue()), titaVo);
				if (tAcReceivable != null) {
					wkSyndFeeYYMM = parse.stringToInteger(tAcReceivable.getRvNo().substring(10, 15));// SL-XX-XXX-YYYMM
					wkSyndFee = tAcReceivable.getRvAmt();
					wkOpenAcDate = tAcReceivable.getOpenAcDate();
				}

				occursList.putParam("OOProcStatus", tTxToDoDetail.getStatus()); // 狀態
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());// 戶號
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());// 額度
				occursList.putParam("OOCustName", loanCom.getCustNameByNo(tTxToDoDetail.getCustNo()));// 戶名
				occursList.putParam("OOSyndFeeYYYMM", wkSyndFeeYYMM); // 年月
				occursList.putParam("OOSyndFee", wkSyndFee); // 金額
				occursList.putParam("OOFeeCode", wkAcctCode); // 科目名稱代號
				occursList.putParam("OOTitaCrDb", 1); // 借貸
				occursList.putParam("OOAcDate", wkOpenAcDate); // 起帳日
				occursList.putParam("OORelNo", tTxToDoDetail.getTitaEntdy() + tTxToDoDetail.getTitaKinbr()
						+ tTxToDoDetail.getTitaTlrNo() + parse.IntegerToString(tTxToDoDetail.getTitaTxtNo(), 8));
				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());
				occursList.putParam("OOTempItemCode", wkTempItemCode);

				cnt++;

				this.totaVo.addOccursList(occursList);
			}

		}

		if (cnt == 0) {
			throw new LogicException(titaVo, "E0001", "應處理明細檔"); // 查詢資料不存在
		}
		{
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
//	! 1:昨日留存 
//	! 2:本日新增 
//	! 3:全部     
//	! 4:本日處理 
//	! 5:本日刪除 
//	! 6:保留    
//	! 7:未處理

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