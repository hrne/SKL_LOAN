package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.math.BigDecimal;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.domain.NegMain;
/* DB容器 */
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
/*DB服務*/
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegMainService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5712")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5712 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegTransService sNegTransService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public NegAppr02Service sNegAppr02Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public DataLog dataLog;
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5712 ");
		this.totaVo.init(titaVo);

		int bringUpDate = Integer.parseInt(titaVo.getParam("OOEntryDate")) + 19110000;
		String finCode = titaVo.getParam("FinCode");
		String txSeq = titaVo.getParam("TxSeq");

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;// 查全部 Integer.MAX_VALUE

		NegAppr02Id tNegAppr02Id = new NegAppr02Id();
		NegAppr02 tNegAppr02 = new NegAppr02();

		tNegAppr02Id.setBringUpDate(bringUpDate);
		tNegAppr02Id.setFinCode(finCode);
		tNegAppr02Id.setTxSeq(txSeq);
		tNegAppr02 = sNegAppr02Service.holdById(tNegAppr02Id);
		if (tNegAppr02 == null) {
			throw new LogicException(titaVo, "E0001", "一般債權撥付資料檔 ");
		}
		NegAppr02 bNegAppr02 = (NegAppr02) dataLog.clone(tNegAppr02); 
		
		BigDecimal OOPayAmt = tNegAppr02.getTxAmt();
		titaVo.setTxAmt(OOPayAmt);
		NegTransId tNegTransId = new NegTransId();
		NegTrans tNegTrans = new NegTrans();

		// 正常交易新增、訂正交易要刪除
		if (titaVo.isHcodeNormal()) {

			if (tNegAppr02.getAcDate() == 0 || tNegAppr02.getTxStatus() != 0) {
				throw new LogicException(titaVo, "E0015", "一般債權撥付資料檔條件不符 " + tNegAppr02Id);
			}

			NegMain tNegMain = new NegMain();
			tNegMain = sNegMainService.statusFirst("0", tNegAppr02.getCustNo(), titaVo); // 0-正常
			if (tNegMain == null) {
				throw new LogicException(titaVo, "E0001", "債務協商案件主檔戶號: " + tNegAppr02.getCustNo());
			}

			tNegTransId.setAcDate(titaVo.getOrgEntdyI() + 19110000);
			tNegTransId.setTitaTlrNo(titaVo.getParam("TLRNO"));
			tNegTransId.setTitaTxtNo(Integer.parseInt(titaVo.getParam("TXTNO")));
			tNegTrans.setNegTransId(tNegTransId);

			tNegTrans.setCustNo(tNegAppr02.getCustNo()); // 戶號
			tNegTrans.setCaseSeq(tNegMain.getCaseSeq()); // 案件序號
			tNegTrans.setEntryDate(bringUpDate); // 入帳日期
			tNegTrans.setTxStatus(0); // 交易狀態 0:未入帳
			tNegTrans.setTxKind("9"); // 交易別 9:未處理
			tNegTrans.setTxAmt(tNegAppr02.getTxAmt()); // 交易金額

			try {
				sNegTransService.insert(tNegTrans, titaVo); // insert
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "債務協商交易檔 " + tNegTransId + e.getErrorMsg());
			}
			tNegAppr02.setTxStatus(1);// 已入客戶暫收
			tNegAppr02.setNegTransAcDate(titaVo.getOrgEntdyI() + 19110000);
			tNegAppr02.setNegTransTlrNo(titaVo.getParam("TLRNO"));
			tNegAppr02.setNegTransTxtNo(Integer.parseInt(titaVo.getParam("TXTNO")));
			try {
				sNegAppr02Service.update(tNegAppr02, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "一般債權撥付資料檔 " + tNegAppr02Id + e.getErrorMsg());
			}
			dataLog.setEnv(titaVo, bNegAppr02, tNegAppr02); ////
			dataLog.exec("修改一般債權撥付資料檔"); ////

		} else {

			tNegTransId.setAcDate(tNegAppr02.getNegTransAcDate() + 19110000);
			tNegTransId.setTitaTlrNo(tNegAppr02.getNegTransTlrNo());
			tNegTransId.setTitaTxtNo(tNegAppr02.getNegTransTxtNo());
			tNegTrans.setNegTransId(tNegTransId);

			tNegTrans = sNegTransService.holdById(tNegTransId, titaVo); // hold by id
			if (tNegTrans == null) {
				throw new LogicException(titaVo, "E0001", "債務協商交易檔 " + tNegTransId);
			}
			if (tNegTrans.getTxStatus() == 2) { // 2:已入帳
				throw new LogicException(titaVo, "E0015", "債務協商交易檔已入帳不可訂正 " + tNegTransId);
			}
			if (tNegAppr02.getAcDate() == 0 || tNegAppr02.getTxStatus() != 1) {
				throw new LogicException(titaVo, "E0015", "一般債權撥付資料檔條件不符 " + tNegAppr02Id);
			}

			try {
				sNegTransService.delete(tNegTrans, titaVo); // delete
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "債務協商交易檔" + tNegTransId + e.getErrorMsg());
			}

			tNegAppr02.setTxStatus(0);// 未入專戶
			tNegAppr02.setNegTransAcDate(0);
			tNegAppr02.setNegTransTlrNo("");
			tNegAppr02.setNegTransTxtNo(0);

			try {
				sNegAppr02Service.update(tNegAppr02, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "一般債權撥付資料檔 " + tNegAppr02Id + e.getErrorMsg());
			}
			dataLog.setEnv(titaVo, bNegAppr02, tNegAppr02); ////
			dataLog.exec("訂正一般債權撥付資料檔"); ////
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}