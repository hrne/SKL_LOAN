package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L618A")
@Scope("prototype")
/**
 * 應處理明細更新
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L618A extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L618A.class);

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoMainService txToDoMainService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L618A ");
		this.totaVo.init(titaVo);
		txToDoCom.setTxBuffer(this.getTxBuffer());

//		此交易為 L6001 處理清單 連結交易所使用之勾選傳送功能
//		T(2,#SelectCode+#ItemCode+#OOCustNo+#OOFacmNo+#OOBormNo+#DtlValue)
		int iSelectCode = parse.stringToInteger(titaVo.getParam("TxSelectCode"));
		String iItemCode = titaVo.getParam("TxItemCode");
		int iCustNo = parse.stringToInteger(titaVo.getParam("TxCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("TxFacmNo"));
		int iBormNo = parse.stringToInteger(titaVo.getParam("TxBormNo"));
		String iDtlValue = titaVo.getParam("TxDtlValue");
		String iExcuteTxcd = titaVo.get("TxExcuteTxcd");

		TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
		tTxToDoDetailId.setItemCode(iItemCode);
		tTxToDoDetailId.setCustNo(iCustNo);
		tTxToDoDetailId.setFacmNo(iFacmNo);
		tTxToDoDetailId.setBormNo(iBormNo);
		tTxToDoDetailId.setDtlValue(iDtlValue);

		TxToDoMain tTxToDoMain = txToDoMainService.findById(iItemCode);
		if (tTxToDoMain == null)
			throw new LogicException(titaVo, "E0013", "txtodoMain notfound.." + iItemCode);

//      iSelectCode 	     BtnIndex		status
//		! 1:昨日留存 
//		! 2:本日新增 
//		! 3:全部     
//		! 4:本日處理                取消保留               -> 0.未處理
//		! 5:本日刪除                取消刪除               -> 0.未處理
//		! 6:保留                       取消保留               -> 0.未處理
//		! 7:未處理                    0.保留                   -> 1.已保留
//		!                   1.刪除                   ->  3.已刪除
//		! 9:未處理 (按鈕處理)   處理                     ->  2.已處理

		this.info("iDtlValue : " + iDtlValue);
		this.info("iSelectCode=" + iSelectCode + ", getBtnIndex:" + titaVo.getBtnIndex());
		switch (iSelectCode) {
		case 4:
			this.info(tTxToDoMain.getExcuteTxcd() + ", " + tTxToDoMain.getEraseFg());
			if ("L618A".equals(tTxToDoMain.getExcuteTxcd()) && "Y".equals(tTxToDoMain.getEraseFg()))
				txToDoCom.updDetailStatus(0, tTxToDoDetailId, titaVo);
			else
				throw new LogicException(titaVo, "E0010", "此項目不可取消處理");
			break;
		case 5:
			txToDoCom.updDetailStatus(0, tTxToDoDetailId, titaVo);
			break;
		case 6:
			txToDoCom.updDetailStatus(0, tTxToDoDetailId, titaVo);
			break;
		case 7:
			if ("0".equals(titaVo.getBtnIndex())) {
				if ("Y".equals(tTxToDoMain.getReserveFg()))
					txToDoCom.updDetailStatus(1, tTxToDoDetailId, titaVo);
				else
					throw new LogicException(titaVo, "E0010", "此項目不可保留");

			} else if ("1".equals(titaVo.getBtnIndex())) {
				if ("Y".equals(tTxToDoMain.getDeleteFg()))
					txToDoCom.updDetailStatus(3, tTxToDoDetailId, titaVo);
				else
					throw new LogicException(titaVo, "E0010", "此項目不可刪除");
			}
			break;

		case 9:
			txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo); // 2.已處理

			if (iExcuteTxcd != null && iExcuteTxcd.length() >= 5 && !iExcuteTxcd.equals("618A"))
				MySpring.newTask(iExcuteTxcd, this.txBuffer, titaVo);
			break;

		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}