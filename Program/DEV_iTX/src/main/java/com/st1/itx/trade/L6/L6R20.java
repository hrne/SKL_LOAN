package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
//import com.st1.itx.db.service.CustMainService;
//import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.domain.TxTellerAuth;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxTellerAuthService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R20")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 * @description for L6401 rim
 */
public class L6R20 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	public TxTellerAuthService sTxTellerAuthService;

	@Autowired
	public CdEmpService cdEmpService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R20 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();

		TxTeller tTxTeller = new TxTeller();

		// FunCode = 9 for L6041
		if (!"9".equals(iFunCode)) {
			String iTlrNo = titaVo.get("TlrNo").trim();

			/*
			 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
			 */
			this.index = 0;

			/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
			this.limit = Integer.MAX_VALUE;

			tTxTeller = sTxTellerService.findById(iTlrNo, titaVo);

			for (int i = 1; i <= 40; i++) {
				this.totaVo.putParam("AuthNo" + i, "");
			}

			if (tTxTeller == null) {
				if ("2".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0003", "使用者:" + iTlrNo);
				} else if ("4".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0004", "使用者:" + iTlrNo);
				}
				MoveTota(iTlrNo, new TxTeller(), titaVo);
			} else {
				if ("1".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0002", "使用者:" + iTlrNo);
				}
				MoveTota(iTlrNo, tTxTeller, titaVo);
				if (!"8".equals(iFunCode)) {
					MoveGroup(iTlrNo, titaVo);
				}
			}
		}

		//
		TxTeller tTxTeller2 = sTxTellerService.findById(titaVo.getTlrNo(), titaVo);
		this.totaVo.putParam("IsAdmin", tTxTeller2.getAdminFg());

		if ("8".equals(iFunCode) && !titaVo.get("BrNo").trim().isEmpty()
				&& !tTxTeller.getBrNo().equals(titaVo.get("BrNo").trim())) {
			throw new LogicException(titaVo, "E0015",
					"使用者 " + tTxTeller.getTlrNo() + " 非隸屬單位 " + titaVo.get("BrNo").trim());
		}

		/*
		 * if ("8".equals(iFunCode) && tTxTeller2.getAdminFg() == 0 &&
		 * !tTxTeller2.getBrNo().equals(tTxTeller.getBrNo())) { throw new
		 * LogicException(titaVo, "E0015", "無使用者 " + tTxTeller.getTlrNo() + " 查詢/異動權限");
		 * }
		 */

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void MoveTota(String iTlrNo, TxTeller tTxTeller, TitaVo titaVo) {
		this.info("L6R20 MoveTota");

		this.totaVo.putParam("BrNo", tTxTeller.getBrNo());
		this.totaVo.putParam("AdFg", tTxTeller.getAdFg());
		this.totaVo.putParam("LevelFg", tTxTeller.getLevelFg());
		this.totaVo.putParam("AllowFg", tTxTeller.getAllowFg());
		this.totaVo.putParam("Status", tTxTeller.getStatus());
		this.totaVo.putParam("GroupNo", tTxTeller.getGroupNo());
		this.totaVo.putParam("Entdy", tTxTeller.getEntdy());
		this.totaVo.putParam("LogonFg", tTxTeller.getLogonFg());
		this.totaVo.putParam("TxtNo", tTxTeller.getTxtNo());
		this.totaVo.putParam("LtxDate", tTxTeller.getLtxDate());
		this.totaVo.putParam("LtxTime", tTxTeller.getLtxTime());
		this.totaVo.putParam("Desc", tTxTeller.getDesc());
		CdEmp tCdEmp = cdEmpService.findById(iTlrNo, titaVo);
		if (tCdEmp != null) {
			this.totaVo.putParam("TlrItem", tCdEmp.getFullname());
		} else {
			this.totaVo.putParam("TlrItem", "");
		}

		this.totaVo.putParam("AmlHighFg", tTxTeller.getAmlHighFg());

		this.totaVo.putParam("Station", tTxTeller.getStation());
		this.totaVo.putParam("AdminFg", tTxTeller.getAdminFg());

		this.totaVo.putParam("LastDate", tTxTeller.getLastDate());
		this.totaVo.putParam("LastTime", tTxTeller.getLastTime());

		this.totaVo.putParam("LastUpdate", parse.timeStampToString(tTxTeller.getLastUpdate()));
		String LastUpdateEmpNo = tTxTeller.getLastUpdateEmpNo();
		this.totaVo.putParam("LastUpdateEmpNo", LastUpdateEmpNo);

		String name = "";
		TxTeller sTxTeller = sTxTellerService.findById(LastUpdateEmpNo, titaVo);
		if (sTxTeller != null) {
			name = sTxTeller.getTlrItem();
		}

		this.totaVo.putParam("LastUpdateEmpNoX", name);

	}

	private void MoveGroup(String iTlrNo, TitaVo titaVo) {
		this.info("L6R20 MoveGroup");
		int i = 1;
		Slice<TxTellerAuth> tTxTellerAuth = sTxTellerAuthService.findByTlrNo(iTlrNo, this.index, this.limit, titaVo);
		List<TxTellerAuth> lTxTellerAuth = tTxTellerAuth == null ? null : tTxTellerAuth.getContent();

		if (lTxTellerAuth != null) {
			for (TxTellerAuth mTxTellerAuth : lTxTellerAuth) {
				this.totaVo.putParam("AuthNo" + i, mTxTellerAuth.getAuthNo());
				i++;
			}
		}
	}
}