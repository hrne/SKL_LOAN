package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.TxAuthorize;
import com.st1.itx.db.service.TxAuthorizeService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita Entdy=D,7 SupNo=X,6 END=X,1
 */

@Service("L6044") // 主管授權紀錄查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6044 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6044.class);

	/* DB服務注入 */
	@Autowired
	public TxAuthorizeService sTxAuthorizeService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6044 ");
		this.totaVo.init(titaVo);

		int iEntdy = this.parse.stringToInteger(titaVo.getParam("Entdy"));
		int iFEntdy = iEntdy + 19110000;
		String iSupNo = titaVo.getParam("SupNo");
		String iTlrItem = "";
		String iTranItem = "";
		String DateTime; // YYY/MM/DD hh:mm:ss

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 395 * 100 = 39,500

		// 查詢主管授權紀錄
		Slice<TxAuthorize> slTxAuthorize;
		if (iSupNo.isEmpty()) {
			slTxAuthorize = sTxAuthorizeService.findEntdy(iFEntdy, iFEntdy, this.index, this.limit, titaVo);
		} else {
			slTxAuthorize = sTxAuthorizeService.findSupNo(iFEntdy, iSupNo, this.index, this.limit, titaVo);
		}
		List<TxAuthorize> lTxAuthorize = slTxAuthorize == null ? null : slTxAuthorize.getContent();

		if (lTxAuthorize == null || lTxAuthorize.size() == 0) {
			throw new LogicException(titaVo, "E0001", "主管授權紀錄"); // 查無資料
		}
		// 如有找到資料
		for (TxAuthorize tTxAuthorize : lTxAuthorize) {

			OccursList occursList = new OccursList();
			occursList.putParam("OOEntdy", tTxAuthorize.getEntdy());
			occursList.putParam("OOSupNo", tTxAuthorize.getSupNo());
			occursList.putParam("OOTlrNo", tTxAuthorize.getTlrNo());
			occursList.putParam("OOReasonCode", tTxAuthorize.getReasonCode());
			occursList.putParam("OOReason", tTxAuthorize.getReason());
			occursList.putParam("OOTxcd", tTxAuthorize.getTxcd());
			occursList.putParam("OOTxSeq", tTxAuthorize.getTxSeq());

			iTranItem = "";
			iTranItem = inqTxTranCode(tTxAuthorize.getTxcd(), iTranItem, titaVo);
			occursList.putParam("OOTranItem", iTranItem);

			iTlrItem = "";
			iTlrItem = inqTxTeller(tTxAuthorize.getTlrNo(), iTlrItem, titaVo);
			occursList.putParam("OOTlrItem", iTlrItem);
			iTlrItem = "";
			iTlrItem = inqTxTeller(tTxAuthorize.getSupNo(), iTlrItem, titaVo);
			occursList.putParam("OOSupItem", iTlrItem);

			DateTime = this.parse.timeStampToString(tTxAuthorize.getLastUpdate());
			occursList.putParam("OODateTime", DateTime);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAuthorize != null && slTxAuthorize.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, String uTranItem, TitaVo titaVo) throws LogicException {

		TxTranCode tTxTranCode = new TxTranCode();

		tTxTranCode = sTxTranCodeService.findById(uTranNo);

		if (tTxTranCode == null) {
			uTranItem = "";
		} else {
			uTranItem = tTxTranCode.getTranItem();
		}

		return uTranItem;

	}

	// 查詢使用者設定檔
	private String inqTxTeller(String uTlrNo, String uTlrItem, TitaVo titaVo) throws LogicException {

		TxTeller tTxTeller = new TxTeller();

		tTxTeller = sTxTellerService.findById(uTlrNo);

		if (tTxTeller == null) {
			uTlrItem = uTlrNo;
		} else {
			uTlrItem = tTxTeller.getTlrItem();
		}

		return uTlrItem;

	}

}