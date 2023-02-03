package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2703")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2703 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustDataCtrlService sCustDataCtrlService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;
	@Autowired
	public SendRsp sendRsp;

	@Autowired
	GSeqCom gSeqCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	DataLog dataLog;

	private CustMain updateCustMain(CustMain tCustMainBefore, CustMain tCustMainAfter, String reason, TitaVo titaVo)
			throws LogicException {
		this.info("L2703 updateCustMain");
		try {
			tCustMainAfter = sCustMainService.update2(tCustMainAfter, titaVo);
		} catch (DBException e) {
			this.error(e.toString());
			throw new LogicException(titaVo, "E0007", "寫回客戶主檔時錯誤");
		}

		// 紀錄變更前變更後
//		dataLog.setEnv(titaVo, tCustMainBefore, tCustMainAfter);
//		dataLog.exec(reason, "CustUKey:" + tCustMainAfter.getCustUKey());
		return tCustMainAfter;
	}

	private CustDataCtrl updateCustDataCtrl(Boolean isNew, CustDataCtrl tCustDataCtrlAfter, TitaVo titaVo)
			throws LogicException {
		this.info("L2703 updateCustDataCtrl");

		if (isNew) {
			try {
				sCustDataCtrlService.insert(tCustDataCtrlAfter, titaVo);
			} catch (DBException e) {
				this.error(e.toString());
				throw new LogicException(titaVo, "E0007", "寫回顧客控管檔時錯誤");
			}
		} else {
			try {
				tCustDataCtrlAfter = sCustDataCtrlService.update2(tCustDataCtrlAfter, titaVo);
			} catch (DBException e) {
				this.error(e.toString());
				throw new LogicException(titaVo, "E0007", "寫回顧客控管檔時錯誤");
			}
		}

		return tCustDataCtrlAfter;
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2703 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 客戶統編
		String iCustId = titaVo.getParam("CustId");

		String iReason = titaVo.getParam("Reason");

		// new table
		CustMain tCustMain = new CustMain();
		CustDataCtrl tCustDataCtrl = new CustDataCtrl();

		// 客戶統編找客戶主檔戶號
		tCustMain = sCustMainService.custIdFirst(iCustId);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "客戶主檔");
		}
		int custNo = tCustMain.getCustNo();
		// 如無戶號則拋錯
		if (custNo == 0) {
			throw new LogicException(titaVo, "E0002", "無戶號");
		}
		String custUKet = tCustMain.getCustUKey();
		Boolean isNew = false;
		if (iFunCd == 1) {

			tCustDataCtrl = sCustDataCtrlService.holdById(custNo);

			if (tCustDataCtrl == null) {
				tCustDataCtrl = new CustDataCtrl();
				tCustDataCtrl.setCustNo(custNo);
				tCustDataCtrl.setCustUKey(custUKet);
				tCustDataCtrl.setReason("");
				tCustDataCtrl.setCustId(tCustMain.getCustId());
				tCustDataCtrl.setCustName(tCustMain.getCustName());
				isNew = true;
			} else if (tCustDataCtrl.getApplMark() == 2) {
				tCustDataCtrl.setCustNo(custNo);
				tCustDataCtrl.setCustUKey(custUKet);
				tCustDataCtrl.setReason("");
				tCustDataCtrl.setCustId(tCustMain.getCustId());
				tCustDataCtrl.setCustName(tCustMain.getCustName());
			} else if (tCustDataCtrl.getApplMark() == 1) {
				throw new LogicException(titaVo, "E0012", "該戶已設定為控管狀態");
			}

			CustDataCtrl tCustDataCtrlBefore = (CustDataCtrl) dataLog.clone(tCustDataCtrl);

			// 檢查是否結清
			Slice<LoanBorMain> slLoanBorMain = sLoanBorMainService.bormCustNoEq(custNo, 0, 999, 0, 999, 0,
					Integer.MAX_VALUE, titaVo);
			List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
			if (lLoanBorMain != null) {
				for (LoanBorMain tLoanBorMain : lLoanBorMain) {
					if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 4
							|| tLoanBorMain.getStatus() == 7 || tLoanBorMain.getStatus() == 99) {
						throw new LogicException(titaVo, "E2003", "該戶號非結清戶");
					}
				}
			}

			tCustDataCtrl.setApplMark(1);
			tCustDataCtrl.setSetEmpNo(titaVo.getTlrNo());
			tCustDataCtrl
					.setSetDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));

			String custName = tCustMain.getCustName();
			String custId = tCustMain.getCustId();

			CustMain tCustMainBefore = (CustMain) dataLog.clone(tCustMain);

			// 遮罩戶名
			StringBuilder sb = new StringBuilder(custName);
			for (int i = 1; i < sb.length(); i += 2)
				sb.setCharAt(i, 'Ｏ');
			tCustMain.setCustName(sb.toString());

			if (tCustDataCtrl.getCustId().startsWith("XX")) {
				// 為 XX 開頭時，表示這筆是設定後又解除過
				// 這時將 CustMain 和 CustDataCtrl 互相交換，不取新編號

				tCustMain.setCustId(tCustDataCtrl.getCustId());
				tCustDataCtrl.setCustId(custId); // custId 是原本的 CustMain.CustId

				updateCustMain(tCustMainBefore, tCustMain, "重新設定顧客控管", titaVo);

			} else if (custName != null && !custName.isEmpty() && custId != null && !custId.isEmpty()) {

				// Id
				String newSeq = parse.IntegerToString(gSeqCom.getSeqNo(0, 0, "L2", "2703", 99999999, titaVo), 8);
				String newId = "XX" + newSeq;
				tCustMain.setCustId(newId);

				// 寫回
				tCustMain = updateCustMain(tCustMainBefore, tCustMain, "設定顧客控管", titaVo);
			}

			/* 存入DB */
			if (tCustDataCtrl.getReSetEmpNo() == null || tCustDataCtrl.getReSetEmpNo().isEmpty()) {
				// 為新資料
				updateCustDataCtrl(isNew, tCustDataCtrl, titaVo);
			} else {
				// 為重新設定
				updateCustDataCtrl(isNew, tCustDataCtrl, titaVo);
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, tCustMainBefore, tCustMain);
			dataLog.exec("設定更新客戶主檔", "CustUKey:" + tCustMain.getCustUKey());

			this.totaVo.putParam("OSetDate", parse.timeStampToString(tCustDataCtrl.getSetDate()));
			this.totaVo.putParam("OSetEmpNo", tCustDataCtrl.getSetEmpNo());
		} else if (iFunCd == 2) { // 解除
			tCustDataCtrl = sCustDataCtrlService.holdById(custNo);
			if (tCustDataCtrl == null) {
				throw new LogicException(titaVo, "E0003", "L2703 該戶號" + custNo + "不存在於結清戶個資控管檔。");
			} else if (tCustDataCtrl.getApplMark() == 3) {
				throw new LogicException(titaVo, "E0012", "該戶已解除控管");
			}

			CustDataCtrl tCustDataCtrlBefore = (CustDataCtrl) dataLog.clone(tCustDataCtrl);

			// 開頭為XX時表示已設定過，不再重寫
			if (!iCustId.startsWith("XX")) {
				tCustDataCtrl.setCustId(tCustMain.getCustId());
				tCustDataCtrl.setCustName(tCustMain.getCustName());
			}
			tCustDataCtrl.setApplMark(3);
			tCustDataCtrl.setReason(iReason);
			tCustDataCtrl.setLastUpdateEmpNo(titaVo.getTlrNo());
			tCustDataCtrl.setReSetEmpNo(titaVo.getTlrNo());
			tCustDataCtrl.setReSetDate(parse.StringToSqlDateO(dateUtil.getNowStringBc(), dateUtil.getNowStringTime()));

			String custName = tCustDataCtrl.getCustName();
			String custId = tCustDataCtrl.getCustId();

			CustMain tCustMainBefore = (CustMain) dataLog.clone(tCustMain);

			// 測試舊資料防
			if (custName != null && !custName.isEmpty()) {
				tCustMain.setCustName(custName);
			}

			if (custId != null && !custId.isEmpty()) {
				tCustMain.setCustId(custId);
			}

			tCustMain = updateCustMain(tCustMainBefore, tCustMain, "解除顧客控管", titaVo);

			/* UpdateDB */
			tCustDataCtrl = updateCustDataCtrl(false, tCustDataCtrl, titaVo);

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, tCustMainBefore, tCustMain);
			dataLog.exec("解除更新客戶主檔", "CustUKey:" + tCustMain.getCustUKey());
			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, tCustDataCtrlBefore, tCustDataCtrl);
			dataLog.exec("解除顧客控管檔", "CustUKey:" + tCustMain.getCustUKey());

			// 宣告
			this.totaVo.putParam("OReSetDate", parse.timeStampToString(tCustDataCtrl.getReSetDate()));
			this.totaVo.putParam("OReSetEmpNo", tCustDataCtrl.getReSetEmpNo());
			this.totaVo.putParam("OSetDate", parse.timeStampToString(tCustDataCtrl.getSetDate()));
			this.totaVo.putParam("OSetEmpNo", tCustDataCtrl.getSetEmpNo());
		} else if (iFunCd == 5) {
			tCustDataCtrl = sCustDataCtrlService.findById(custNo);
		}

		// 刷主管卡
		if (titaVo.getEmpNos().trim().isEmpty()) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}