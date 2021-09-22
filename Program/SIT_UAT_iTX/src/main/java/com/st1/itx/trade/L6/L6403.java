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
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.domain.TxAuthority;
import com.st1.itx.db.domain.TxAuthorityId;
import com.st1.itx.db.service.TxAuthorityService;

@Service("L6403")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6403 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public TxAuthGroupService sTxAuthGroupService;

	@Autowired
	public TxAuthorityService sTxAuthorityService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6403 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L6403Batch", this.txBuffer, titaVo);

		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

//		String iFunCode = titaVo.get("FunCode").trim();
//		String iAuthNo = titaVo.get("AuthNo").trim();
//
//		TxAuthGroup tTxAuthGroup = sTxAuthGroupService.holdById(iAuthNo);
//
//		if (tTxAuthGroup == null) {
//			if ("2".equals(iFunCode)) {
//				throw new LogicException(titaVo, "E0003", "權限群組:" + iAuthNo);
//			} else if ("4".equals(iFunCode)) {
//				throw new LogicException(titaVo, "E0004", "權限群組:" + iAuthNo);
//			}
//
//			tTxAuthGroup = new TxAuthGroup();
//			tTxAuthGroup = MoveToAuthGroup(iAuthNo, tTxAuthGroup, titaVo);
//			tTxAuthGroup.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
//			tTxAuthGroup.setCreateEmpNo(titaVo.getTlrNo());
//			try {
//				this.info("TxAuthGroup Authno = " + tTxAuthGroup.getAuthNo());
//				this.info("TxAuthGroup AuthItem = " + tTxAuthGroup.getAuthItem());
//				sTxAuthGroupService.insert(tTxAuthGroup);
//				InsertAllAuthority(iAuthNo, titaVo);
//			} catch (DBException e) {
//				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
//			}
//		} else {
//			if ("1".equals(iFunCode)) {
//				throw new LogicException(titaVo, "E0002", "權限群組:" + iAuthNo);
//			}
//			try {
//				if ("2".equals(iFunCode)) {
//					tTxAuthGroup = MoveToAuthGroup(iAuthNo, tTxAuthGroup, titaVo);
//					tTxAuthGroup.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
//					tTxAuthGroup.setLastUpdateEmpNo(titaVo.getTlrNo());
//					sTxAuthGroupService.update(tTxAuthGroup);
//					DeleteAllAuthority(iAuthNo, titaVo);
//					InsertAllAuthority(iAuthNo, titaVo);
//				} else if ("4".equals(iFunCode)) {
//					sTxAuthGroupService.delete(tTxAuthGroup);
//					DeleteAllAuthority(iAuthNo, titaVo);
//				}
//			} catch (DBException e) {
//				if ("2".equals(iFunCode)) {
//					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
//				} else if ("4".equals(iFunCode)) {
//					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
//				}
//			}
//		}

		this.addList(this.totaVo);
		return this.sendList();
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
			String iTranNo = titaVo.get("ApaCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApaBtn" + i).trim();
				String iInqFg = titaVo.get("OiaBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L2
		for (int i = 1; i <= 190; i++) {
			String iTranNo = titaVo.get("ApbCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApbBtn" + i).trim();
				String iInqFg = titaVo.get("OibBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L3
		for (int i = 1; i <= 50; i++) {
			String iTranNo = titaVo.get("ApcCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApcBtn" + i).trim();
				String iInqFg = titaVo.get("OicBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L4
		for (int i = 1; i <= 100; i++) {
			String iTranNo = titaVo.get("ApdCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApdBtn" + i).trim();
				String iInqFg = titaVo.get("OidBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L5
		for (int i = 1; i <= 100; i++) {
			String iTranNo = titaVo.get("ApeCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApeBtn" + i).trim();
				String iInqFg = titaVo.get("OieBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L6
		for (int i = 1; i <= 110; i++) {
			String iTranNo = titaVo.get("ApfCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApfBtn" + i).trim();
				String iInqFg = titaVo.get("OifBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L7
		for (int i = 1; i <= 20; i++) {
			String iTranNo = titaVo.get("ApgCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApgBtn" + i).trim();
				String iInqFg = titaVo.get("OigBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L8
		for (int i = 1; i <= 150; i++) {
			String iTranNo = titaVo.get("AphCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("AphBtn" + i).trim();
				String iInqFg = titaVo.get("OihBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}
		// L9
		for (int i = 1; i <= 50; i++) {
			String iTranNo = titaVo.get("ApiCode" + i).trim();
//			this.info("L6403 > InsertAllAuthority TranNo = " + iTranNo);
			if (iTranNo.length() == 5) {
				String iCanFg = titaVo.get("ApiBtn" + i).trim();
				String iInqFg = titaVo.get("OiiBtn" + i).trim();
				InserOnetAuthority(AuthNo, iTranNo, iCanFg, iInqFg, titaVo);
			}
		}

	}

	private void InserOnetAuthority(String AuthNo, String TranNo, String CanFg, String InqFg, TitaVo titaVo) throws LogicException {
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

		if (AuthFg != 0) {
			try {
				TxAuthorityId tTxAuthorityId = new TxAuthorityId(AuthNo, TranNo);
				tTxAuthority.setTxAuthorityId(tTxAuthorityId);
				tTxAuthority.setAuthFg(AuthFg);
				tTxAuthority.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tTxAuthority.setCreateEmpNo(titaVo.getTlrNo());
//				this.info("L6403 > Txauthority = " + tTxAuthority.getAuthNo() + "/" + tTxAuthority.getTranNo() + "/" + tTxAuthority.getAuthFg());
				sTxAuthorityService.insert(tTxAuthority);
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

//		Slice<TxAuthority> slTxauthority = sTxAuthorityService.findByAuthNo(AuthNo, this.index, this.limit);
		Slice<TxAuthority> slTxauthority = sTxAuthorityService.findByAuthNo2(AuthNo, "LC%", this.index, this.limit);
		List<TxAuthority> lTxauthority = slTxauthority == null ? null : slTxauthority.getContent();

		if (lTxauthority != null) {
			try {
				sTxAuthorityService.deleteAll(lTxauthority);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
	}
}