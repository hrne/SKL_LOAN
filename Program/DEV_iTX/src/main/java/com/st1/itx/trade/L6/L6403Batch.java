package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.domain.TxAuthority;
import com.st1.itx.db.domain.TxAuthorityId;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxAuthorityService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.http.WebClient;

@Service("L6403Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6403Batch extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public TxAuthGroupService sTxAuthGroupService;

	@Autowired
	public TxAuthorityService sTxAuthorityService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;
	
	@Autowired
	public DataLog dataLog;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public WebClient webClient;

	private Map<String, Integer> authMap = new TreeMap<String, Integer>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6403Batch ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iAuthNo = titaVo.get("AuthNo").trim();

		TxAuthGroup tTxAuthGroup = sTxAuthGroupService.holdById(iAuthNo);

		if (tTxAuthGroup == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "權限群組:" + iAuthNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "權限群組:" + iAuthNo);
			}

			tTxAuthGroup = new TxAuthGroup();
			tTxAuthGroup = MoveToAuthGroup(iAuthNo, tTxAuthGroup, titaVo);
			tTxAuthGroup.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tTxAuthGroup.setCreateEmpNo(titaVo.getTlrNo());
			try {
				this.info("TxAuthGroup Authno = " + tTxAuthGroup.getAuthNo());
				this.info("TxAuthGroup AuthItem = " + tTxAuthGroup.getAuthItem());
				sTxAuthGroupService.insert(tTxAuthGroup);
				InsertAllAuthority(iAuthNo, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "權限群組:" + iAuthNo);
			}
			try {
				if ("2".equals(iFunCode)) {
					TxAuthGroup tTxAuthGroup2 = (TxAuthGroup) dataLog.clone(tTxAuthGroup);

					tTxAuthGroup = MoveToAuthGroup(iAuthNo, tTxAuthGroup, titaVo);
					tTxAuthGroup = sTxAuthGroupService.update2(tTxAuthGroup);

					DeleteAllAuthority(iAuthNo, titaVo);
					InsertAllAuthority(iAuthNo, titaVo);

					dataLog.compareOldNew("無權限");
					
					dataLog.setEnv(titaVo, tTxAuthGroup2, tTxAuthGroup);
					dataLog.exec("修改權限群組 " + tTxAuthGroup.getAuthNo());

					
				} else if ("4".equals(iFunCode)) {
					sTxAuthGroupService.delete(tTxAuthGroup);
					DeleteAllAuthority(iAuthNo, titaVo);
				}
			} catch (DBException e) {
				if ("2".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				} else if ("4".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "",
				"已成功更新權限群組" + iAuthNo + "交易權限", titaVo);

		return null;
	}

	private TxAuthGroup MoveToAuthGroup(String AuthNo, TxAuthGroup tTxAuthGroup, TitaVo titaVo) throws LogicException {
		this.info("L6403 > MoveToAuthGroup");

		tTxAuthGroup.setAuthNo(AuthNo);
		tTxAuthGroup.setAuthItem(titaVo.get("AuthItem").trim());
		tTxAuthGroup.setDesc(titaVo.get("Desc").trim());
		tTxAuthGroup.setStatus(Integer.valueOf(titaVo.get("Status")));
		tTxAuthGroup.setBranchNo(titaVo.get("BranchNo").trim());
		tTxAuthGroup.setLevelFg(Integer.valueOf(titaVo.get("LevelFg")));

		return tTxAuthGroup;
	}

	private void InsertAllAuthority(String AuthNo, TitaVo titaVo) throws LogicException {
		this.info("L6403 > InsertAllAuthority AuthNo = " + AuthNo);
		// L1
		for (int i = 1; i <= 30; i++) {
			String iTranNo = titaVo.get("ApaCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApaBtn" + i).trim();
					String iInqFg = titaVo.get("OiaBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L2
		for (int i = 1; i <= 110; i++) {
			String iTranNo = titaVo.get("ApbCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApbBtn" + i).trim();
					String iInqFg = titaVo.get("OibBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L3
		for (int i = 1; i <= 50; i++) {
			String iTranNo = titaVo.get("ApcCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApcBtn" + i).trim();
					String iInqFg = titaVo.get("OicBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L4
		for (int i = 1; i <= 100; i++) {
			String iTranNo = titaVo.get("ApdCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApdBtn" + i).trim();
					String iInqFg = titaVo.get("OidBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L5
		for (int i = 1; i <= 120; i++) {
			String iTranNo = titaVo.get("ApeCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApeBtn" + i).trim();
					String iInqFg = titaVo.get("OieBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L6
		for (int i = 1; i <= 130; i++) {
			String iTranNo = titaVo.get("ApfCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApfBtn" + i).trim();
					String iInqFg = titaVo.get("OifBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L7
		for (int i = 1; i <= 20; i++) {
			String iTranNo = titaVo.get("ApgCode" + i);
			if (iTranNo != null) {

				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApgBtn" + i).trim();
					String iInqFg = titaVo.get("OigBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L8
		for (int i = 1; i <= 150; i++) {
			String iTranNo = titaVo.get("AphCode" + i);

			if (iTranNo != null) {
				this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("AphBtn" + i).trim();
					String iInqFg = titaVo.get("OihBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}
		// L9
		for (int i = 1; i <= 50; i++) {
			String iTranNo = titaVo.get("ApiCode" + i);

			if (iTranNo != null) {
				if (iTranNo.trim().length() == 5) {
					String iCanFg = titaVo.get("ApiBtn" + i).trim();
					String iInqFg = titaVo.get("OiiBtn" + i).trim();
					InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
				}
			}

		}

	}

	private void InserOnetAuthority(String AuthNo, String TranNo, String CanFg, String InqFg, TitaVo titaVo)
			throws LogicException {
//		this.info("L6403 > InserOnetAuthority = " + AuthNo + "/" + TranNo + "/" + CanFg + "/" + InqFg);
		TxAuthority tTxAuthority = new TxAuthority();
		int AuthFg = 0;
		if ("V".equals(InqFg)) {
			AuthFg = 1;
		} else if ("V".equals(CanFg)) {
			AuthFg = 2;
		} else {
			AuthFg = 0;
		}

		TxTranCode txTranCode = sTxTranCodeService.findById(TranNo, titaVo);
		if (txTranCode != null) {
			dataLog.putNew(txTranCode.getTranNo() + " " + txTranCode.getTranItem(), authFgX(AuthFg));
		}

		if (AuthFg != 0) {
			try {
				TxAuthorityId tTxAuthorityId = new TxAuthorityId(AuthNo, TranNo);
				tTxAuthority.setTxAuthorityId(tTxAuthorityId);
				tTxAuthority.setAuthFg(AuthFg);
				tTxAuthority.setCreateDate(
						parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tTxAuthority.setCreateEmpNo(titaVo.getTlrNo());
				sTxAuthorityService.insert(tTxAuthority, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
	}

	private void DeleteAllAuthority(String AuthNo, TitaVo titaVo) throws LogicException {
		this.info("L6403 > DeleteAllAuthority");

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<TxAuthority> slTxauthority = sTxAuthorityService.findByAuthNo2(AuthNo, "LC%", this.index, this.limit);
		List<TxAuthority> lTxauthority = slTxauthority == null ? null : slTxauthority.getContent();

		if (lTxauthority != null) {
			try {
				sTxAuthorityService.deleteAll(lTxauthority);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}

			for (TxAuthority txAuthority : lTxauthority) {
				TxTranCode txTranCode = sTxTranCodeService.findById(txAuthority.getTranNo(), titaVo);
				if (txTranCode != null) {
					dataLog.putOld(txTranCode.getTranNo() + " " + txTranCode.getTranItem(), authFgX(txAuthority.getAuthFg()));
				}
			}
		}
	}

	private String authFgX(int authfg) {
		String r = "";
		if (authfg == 0) {
			r = "無權限";
		} else if (authfg == 1) {
			r = "僅查詢權限";
		} else if (authfg == 2) {
			r = "全部權限";
		}
		return r;
	}
}