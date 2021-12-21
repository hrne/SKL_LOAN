package com.st1.itx.trade.L6;

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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.domain.TxTellerAuth;
import com.st1.itx.db.domain.TxTellerAuthId;
import com.st1.itx.db.service.TxTellerAuthService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6401")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6401 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTellerService txTellerService;
	@Autowired
	public TxTellerAuthService sTxTellerAuthService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6401 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iTlrNo = titaVo.get("TlrNo").trim();

		TxTeller tTxTeller = txTellerService.holdById(iTlrNo);

		if (tTxTeller == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "使用者:" + iTlrNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "使用者:" + iTlrNo);
			}

			tTxTeller = new TxTeller();
			tTxTeller = MoveToDb(iTlrNo, tTxTeller, titaVo);
			tTxTeller.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tTxTeller.setCreateEmpNo(titaVo.getTlrNo());
			try {
				txTellerService.insert(tTxTeller);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
			moveGroup(iTlrNo, iFunCode, titaVo);
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "使用者:" + iTlrNo);
			}
			try {
				if ("2".equals(iFunCode)) {
					tTxTeller = MoveToDb(iTlrNo, tTxTeller, titaVo);
					tTxTeller.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tTxTeller.setLastUpdateEmpNo(titaVo.getTlrNo());
					txTellerService.update(tTxTeller);
				} else if ("4".equals(iFunCode)) {
					txTellerService.delete(tTxTeller);
				}
			} catch (DBException e) {
				if ("2".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				} else if ("4".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
			moveGroup(iTlrNo, iFunCode, titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();

	}

	private TxTeller MoveToDb(String iTlrNo, TxTeller tTxTeller, TitaVo titaVo) throws LogicException {

		tTxTeller.setTlrNo(iTlrNo);
		tTxTeller.setTlrItem(titaVo.getParam("TlrItem"));
		tTxTeller.setBrNo(titaVo.getParam("BrNo"));
//		tTxTeller.setAdFg(Integer.valueOf(titaVo.get("AdFg")));
		tTxTeller.setLevelFg(Integer.valueOf(titaVo.get("LevelFg")));
		tTxTeller.setStatus(Integer.valueOf(titaVo.get("Status")));
		tTxTeller.setGroupNo(titaVo.getParam("GroupNo"));
		tTxTeller.setDesc(titaVo.getParam("Desc"));
		tTxTeller.setAmlHighFg(titaVo.getParam("AmlHighFg"));
		tTxTeller.setAllowFg(Integer.parseInt(titaVo.getParam("AllowFg")));

		return tTxTeller;

	}

	private void moveGroup(String mTlrNo, String mFuncCode, TitaVo titaVo) throws LogicException {

		this.info("into moveGroup");

		if (("2").equals(mFuncCode) || ("4").equals(mFuncCode)) {

			Slice<TxTellerAuth> mTxTellerAuth = sTxTellerAuthService.findByTlrNo(mTlrNo, this.index, this.limit, titaVo);
			List<TxTellerAuth> iTxTellerAuth = mTxTellerAuth == null ? null : mTxTellerAuth.getContent();
			if (iTxTellerAuth != null) {
				try {
					sTxTellerAuthService.deleteAll(iTxTellerAuth);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			}
		}

		if (("1").equals(mFuncCode) || ("2").equals(mFuncCode)) {
			for (int i = 1; i <= 40; i++) {

				if (titaVo.getParam("AuthNo" + i) != null && titaVo.getParam("AuthNo" + i).length() > 0) {
					TxTellerAuth tTxTellerAuth = new TxTellerAuth();
					TxTellerAuthId tTxTellerAuthId = new TxTellerAuthId();

					tTxTellerAuthId.setTlrNo(mTlrNo);
					tTxTellerAuthId.setAuthNo(titaVo.getParam("AuthNo" + i));

					tTxTellerAuth.setTxTellerAuthId(tTxTellerAuthId);

					try {
						sTxTellerAuthService.insert(tTxTellerAuth, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());

					}
				}
			}
		}

	}
}