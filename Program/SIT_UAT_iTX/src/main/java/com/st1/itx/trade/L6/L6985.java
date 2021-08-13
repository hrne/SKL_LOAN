package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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
	private static final Logger logger = LoggerFactory.getLogger(L6985.class);

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
	private int custNo = 0;
	private int trasCollDate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6985 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;// 389*100 = 38900

		trasCollDate = parse.stringToInteger(titaVo.getParam("AcDate"));
		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));

		this.info("selectCode = " + selectCode);
		this.info("trasCollDate = " + trasCollDate);
		// 0.未處理
		// 1.已保留
		// 2.已處理
		// 3.已刪除

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();

		Slice<TxToDoDetail> slTxToDoDetail = null;
		String fallMessage = "";
//		ACCL00 各項提存作業

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
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 0, 3, this.index, this.limit, titaVo);
			fallMessage = "昨日留存";
			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 0, 3, this.index, this.limit, titaVo);
			fallMessage = "本日新增";
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 0, 3, this.index, this.limit, titaVo);
			fallMessage = "全部";
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 2, 2, this.index, this.limit, titaVo);
			fallMessage = "本日處理";
			break;
		case 5:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 3, 3, this.index, this.limit, titaVo);
			fallMessage = "本日刪除";
			break;
		case 6:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 1, 1, this.index, this.limit, titaVo);
			fallMessage = "保留";
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 0, 0, this.index, this.limit, titaVo);
			fallMessage = "未處理";
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL00", 0, 0, this.index, this.limit, titaVo);
			fallMessage = "未處理 (按鈕處理)";
			break;
		default:
			break;
		}

		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
		this.info("lTxToDoDetail = " + lTxToDoDetail);

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				OccursList occursList = new OccursList();

				if (selectCodeIsNotQualify(tTxToDoDetail)) {
					continue;
				}

				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tTxToDoDetail.getProcessNote());

				this.info("tTempVo = " + tTempVo);

				String AcclType = tTempVo.getParam("AcclType");
				String SlipNote = tTempVo.getParam("SlipNote");
				BigDecimal TxAmt = parse.stringToBigDecimal(tTempVo.getParam("DbTxAmt1"));
				String AcctCode = tTempVo.getParam("AcctCode") == null ? "" : tTempVo.getParam("AcctCode");
				String AcBookCode = tTempVo.getParam("AcBookCode");
				String AcSubBookCode = tTempVo.getParam("AcSubBookCode");
				String AcctItem = "";
				CdAcCode tCdAcCode = cdAcCodeService.acCodeAcctFirst(AcctCode, titaVo);
				if (tCdAcCode != null) {
					AcctItem = tCdAcCode.getAcctItem();
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

				occursList.putParam("OODbAmt", TxAmt); // 金額
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());

				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查詢範圍: " + fallMessage + " 查無資料");
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
		this.info("result = " + result + " " + tTxToDoDetail);
		return result;
	}

}