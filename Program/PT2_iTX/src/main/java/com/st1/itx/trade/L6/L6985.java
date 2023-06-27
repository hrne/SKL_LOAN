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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6985")
@Scope("prototype")
/**
 * 各項提存作業
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6985 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	private int selectCode = 0;
	private String iItemCode = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6985 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
		iItemCode = titaVo.getParam("cItemCode");

		this.info("selectCode = " + selectCode);

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();

		Slice<TxToDoDetail> slTxToDoDetail = null;
//		ACCL00 各項提存作業
		// 上一營業日
		int lbsDy = this.txBuffer.getTxCom().getLbsdy() + 19110000;
		// 本營業日
		int tbsDy = this.txBuffer.getTxCom().getTbsdy() + 19110000;
//		! 1:昨日留存  0-上一營業日
//		! 2:本日新增  本營業日-本營業日
//		! 3:全部     0-99991231
//		! 4:本日處理   0-99991231
//		! 5:本日刪除   0-99991231
//		! 6:保留     0-99991231
//		! 7:未處理 0-99991231
//		! 9:未處理 (按鈕處理) 0-99991231
//   0.未處理
//   1.已保留
//   2.已處理
//   3.已刪除

		switch (selectCode) {
		case 1:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 0, 3, 0, lbsDy, this.index, this.limit,
					titaVo);
			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 0, 3, tbsDy, tbsDy, this.index, this.limit,
					titaVo);
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 0, 3, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 2, 2, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 5:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 3, 3, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 6:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 1, 1, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 0, 0, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.DataDateRange(iItemCode, 0, 0, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		default:
			break;
		}

		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
		this.info("lTxToDoDetail = " + lTxToDoDetail);

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				OccursList occursList = new OccursList();

				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tTxToDoDetail.getProcessNote());

				this.info("tTempVo = " + tTempVo);

				String AcclType = tTempVo.getParam("AcclType");
				String SlipNote = tTempVo.getParam("SlipNote");
				BigDecimal TxAmt = parse.stringToBigDecimal(tTempVo.getParam("DbTxAmt1"));
				if ("ACCL04".equals(iItemCode)) {// 折溢價
					TxAmt = parse.stringToBigDecimal(tTempVo.getParam("DbTxAmt1"))
							.subtract(parse.stringToBigDecimal(tTempVo.getParam("CrTxAmt1")));
				}
				String AcctCode = tTempVo.getParam("AcctCode");
				String AcBookCode = tTempVo.getParam("AcBookCode");
				String AcSubBookCode = tTempVo.getParam("AcSubBookCode");
				String AcDate = tTempVo.getParam("AcDate");
				String SlipBatNo = tTempVo.getParam("SlipBatNo");
				String AcctItem = "";
				CdAcCode tCdAcCode = cdAcCodeService.acCodeAcctFirst(AcctCode, titaVo);
				if (tCdAcCode != null) {
					AcctItem = tCdAcCode.getAcNoCode() + " " + tCdAcCode.getAcctItem();
				}
				this.info("AcclType = " + AcclType);
				this.info("SlipNote = " + SlipNote);
				this.info("TxAmt = " + TxAmt);
				this.info("AcctCode = " + AcctCode);
				this.info("AcBookCode = " + AcBookCode);
				this.info("AcSubBookCode = " + AcSubBookCode);
				this.info("AcctItem = " + AcctItem);

				occursList.putParam("OOProcStatus", tTxToDoDetail.getStatus());
				occursList.putParam("OOAccruedType", AcclType); // 提存種類
				occursList.putParam("OOAcctCode", AcctCode); // 科目
				occursList.putParam("OOAcctItem", AcctItem); // 科目名稱
				occursList.putParam("OOAcBookCode", AcBookCode + "/" + AcSubBookCode); // 帳冊別
				occursList.putParam("OORmk", SlipNote); // 摘要
				occursList.putParam("OORelNo", tTxToDoDetail.getTitaEntdy() + tTxToDoDetail.getTitaKinbr()
						+ tTxToDoDetail.getTitaTlrNo() + parse.IntegerToString(tTxToDoDetail.getTitaTxtNo(), 8));
				occursList.putParam("OOAcDate", AcDate); // 會計日期
				occursList.putParam("OOSlipBatNo", SlipBatNo); // 會計日期
				occursList.putParam("OODbAmt", TxAmt); // 金額
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());

				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}