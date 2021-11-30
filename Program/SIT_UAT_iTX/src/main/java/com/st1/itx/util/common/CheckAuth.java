package com.st1.itx.util.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.data.CheckAuthVo;
import com.st1.itx.db.service.TxAuthorityService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.springjpa.cm.CheckAuthServiceImpl;

import com.st1.itx.db.domain.TxAgent;
import com.st1.itx.db.domain.TxAgentId;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.domain.TxTellerAuth;
import com.st1.itx.db.domain.TxTellerAuthId;
import com.st1.itx.db.domain.TxAuthority;
import com.st1.itx.db.domain.TxAuthorityId;
import com.st1.itx.db.service.TxAgentService;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.service.TxTellerAuthService;

@Component("checkAuth")
@Scope("prototype")

public class CheckAuth extends CommBuffer {

	/* DB服務注入 */
	@Autowired
	public CheckAuthServiceImpl checkAuthServiceImpl;

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public TxTellerAuthService txTellerAuthService;

	@Autowired
	public TxAuthGroupService txAuthGroupService;

	@Autowired
	public TxAuthorityService txAuthorityService;

	@Autowired
	public TxAgentService txAgentService;

	/**
	 * 獲取櫃員交易權限
	 * 
	 * @param titaVo  titaVo
	 * @param tlrNo   櫃員
	 * @param agentNo 被代理人
	 * @param authNo  權限群組
	 * @param tranNo  交易代號
	 * @param actFg   流程步驟(同titaVo.actfg)
	 * @return CheckAuthVo
	 * @throws LogicException LogicException
	 */

	private CheckAuthVo getAuth(TitaVo titaVo, String tlrNo, String agentNo, String authNo, String tranNo, int actFg) throws LogicException {
		this.info("CheckAuth.getAuth(new) tlrNo=" + tlrNo + ",agentNo=" + agentNo + ",authNo=" + authNo + ",tranNo=" + tranNo + ",actFg=" + actFg);

		if (actFg < 0 || actFg > 4) {
			throw new LogicException(titaVo, "EC008", "(CheckAuth.getAuth) 流程步驟 =" + actFg);
		}

		tlrNo = tlrNo.trim();
		agentNo = agentNo.trim();
		authNo = authNo.trim();
		tranNo = tranNo.trim();

		CheckAuthVo checkAuthVo = new CheckAuthVo();

		checkAuthVo.setTlrNo(tlrNo);
		checkAuthVo.setAgentNo(agentNo);
		checkAuthVo.setTlrNo(tranNo);
		checkAuthVo.setCanInquiry(false);
		checkAuthVo.setCanUpdate(false);

		// 檢查經辦
		TxTeller txTeller = txTellerService.findById(tlrNo);

		if (txTeller == null) {
//			throw new LogicException(titaVo, "EC001", "(CheckAuth.getAuth) 經辦 =" + tlrNo);
			return checkAuthVo;
		}

		TxTellerAuthId txTellerAuthId = new TxTellerAuthId();

		// 確認是否有代理權限
		if (!"".equals(agentNo) && agentNo != null) {
			TxAgentId txAgentId = new TxAgentId();
			txAgentId.setTlrNo(tlrNo);
			txAgentId.setAgentTlrNo(agentNo);
			TxAgent txAgent = txAgentService.findById(txAgentId, titaVo);
			if (txAgent == null) {
//				throw new LogicException(titaVo, "EC008", "(CheckAuth.getAuth) 無" + agentNo + "代理權限");
				return checkAuthVo;
			}

			if (!isAgent(titaVo, txAgent)) {
//				throw new LogicException(titaVo, "EC008", "(CheckAuth.getAuth) 代理權限" + agentNo + "未生效或逾期");
				return checkAuthVo;
			}

			txTellerAuthId.setTlrNo(agentNo);
		} else {
			txTellerAuthId.setTlrNo(tlrNo);
		}

		// 確認是否有權限權限
		txTellerAuthId.setAuthNo(authNo);

		TxTellerAuth txTellerAuth = txTellerAuthService.findById(txTellerAuthId, titaVo);

		if (txTellerAuth == null) {
			return checkAuthVo;
		}

		// 確認是否有交易權限

		TxAuthorityId txAuthorityId = new TxAuthorityId();
		txAuthorityId.setAuthNo(authNo);
		txAuthorityId.setTranNo(tranNo);
		TxAuthority txAuthority = txAuthorityService.findById(txAuthorityId, titaVo);
		if (txAuthority == null) {
//			throw new LogicException(titaVo, "EC008", "(CheckAuth.getAuth) 經辦" + txTellerAuthId.getTlrNo() + "無" + authNo + "權限群組權限");
			return checkAuthVo;
		}

		this.info("@@@CheckAuth.TxAuthority AuthFg=" + txAuthority.getAuthFg() + ",actFg=" + actFg + ",txTeller.getLevelFg=" + txTeller.getLevelFg());

		if (actFg == 0) {
			// 全部權限
			if (txAuthority.getAuthFg() == 2) {
				checkAuthVo.setCanUpdate(true);
				checkAuthVo.setCanInquiry(true);
			} else if (txAuthority.getAuthFg() == 1) {
				checkAuthVo.setCanInquiry(true);
			}
		} else if ((actFg == 1 || actFg == 3) && txTeller.getLevelFg() == 3) {
			if (txAuthority.getAuthFg() == 2) {
				checkAuthVo.setCanUpdate(true);
			}
		} else if ((actFg == 2 || actFg == 4) && txTeller.getLevelFg() == 1) {
			if (txAuthority.getAuthFg() == 2) {
				checkAuthVo.setCanUpdate(true);
			}
		}

		return checkAuthVo;
	}

	/**
	 * 檢查代理權限是否在有效期間
	 * 
	 * @param txAgent 代理人資料
	 * @return 是否有權限 true/false
	 */
	public boolean isAgent(TitaVo titaVo, TxAgent txAgent) throws LogicException {
		this.info("CheckAuth.isAgeng CALDY = " + titaVo.getParam("CALDY"));
		
		int caldy = Integer.valueOf(titaVo.getParam("CALDY"));

		// 現在時間
		SimpleDateFormat sdFormat = new SimpleDateFormat("HHmm");
		Date date = new Date();
		int nowTime = Integer.valueOf(sdFormat.format(date));

		// 未生效
		if (txAgent.getStatus() != 1) {
			return false;
		}
		// 未在有效期間
		if (txAgent.getBeginDate() > caldy) {
			return false;
		}

		if (txAgent.getBeginDate() == caldy && txAgent.getBeginTime() > nowTime) {
			return false;
		}

		if (txAgent.getEndDate() < caldy) {
			return false;
		}

		if (txAgent.getEndDate() == caldy && txAgent.getEndTime() < nowTime) {
			return false;
		}

		this.info("CheckAuth.isAgeng tlrno = " + txAgent.getTlrNo());

		// 代理人姓名
		TxTeller txTeller = txTellerService.findById(txAgent.getTlrNo());

		if (txTeller == null) {
			return false;
		}

		return true;
	}

	/**
	 * 檢查櫃員交易權限
	 * 
	 * @param titaVo  TitaVo
	 * @param tlrNo   櫃員
	 * @param agentNo 被代理人
	 * @param authNo  權限群組
	 * @param tranNo  交易代號
	 * @param actFg   流程步驟(同titaVo.actfg)
	 * @param funCode 0.依交易代號判斷 1.新增 2.修改 3.複製 4.刪除 5.查詢
	 * @return true有權限,/false無權限
	 * @throws LogicException LogicException
	 */
	public boolean isCan(TitaVo titaVo, String tlrNo, String agentNo, String authNo, String tranNo, int actFg, int funCode) throws LogicException {

		this.info("CheckAuth.isCan (new)");

		if (actFg < 0 || actFg > 4) {
			throw new LogicException(titaVo, "EC008", "(CheckAuth.isCan) 流程步驟=" + actFg);
		}

		if (funCode < 0 || funCode > 5) {
			throw new LogicException(titaVo, "EC008", "(CheckAuth.isCan) 功能=" + funCode);
		}

		CheckAuthVo checkAuthVo = this.getAuth(titaVo, tlrNo, agentNo, authNo, tranNo, actFg);

		this.info("CheckAuth.isCan tlrno=" + tlrNo + ",agentNo=" + agentNo + ",authNo=" + authNo + ",tranNo=" + tranNo + ",actFg=" + actFg);
		this.info("CheckAuth.isCan Update=" + checkAuthVo.isCanUpdate() + ",Inquiry=" + checkAuthVo.isCanInquiry());

		if (funCode == 0) {
			String t = tranNo.substring(2, 3);
			if ("0".equals(t) || "9".equals(t)) {
				funCode = 5;
			} else {
				funCode = 1;
			}
		}

		if (funCode == 5 && checkAuthVo.isCanInquiry()) {
			return true;
		} else if (funCode >= 1 && funCode <= 4 && checkAuthVo.isCanUpdate()) {
			return true;
		}

		return false;
	}

	/**
	 * 回覆交易可執行清單
	 * @param txcd 交易代號
	 * @return 有權限櫃員清單
	 */
	public List<Map<String, String>> canDoList(String txcd) throws LogicException {
		
		List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
		
		List<Map<String, String>> rList = null;

		try {
			rList = checkAuthServiceImpl.findCanDoList(txcd);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("CheckAuth ErrorForDB=" + e);
			throw new LogicException(titaVo, "E0005", "");
		}
		
//		if (rList != null && rList.size() > 0) {
//			for (Map<String, String> d : rList) {
//				this.info("CheckAuth.canDoList TlrNo = " + d.get("TlrNo") + " , AuthFg = " + d.get("AuthFg"));
//			}			
//		}
		return rList;
	}
	
	// TitaVo:ACTFG,FUNCIND

	/**
	 * 獲取櫃員交易權限 (舊版)
	 * 
	 * @param titaVo titaVo
	 * @param tlrno  櫃員
	 * @param tranno 交易代號
	 * @param actfg  流程步驟(同titaVo.actfg)
	 * @return CheckAuthVo
	 * @throws LogicException LogicException
	 */
	public CheckAuthVo getAuth(TitaVo titaVo, String tlrno, String tranno, int actfg) throws LogicException {

		this.info("CheckAuth.getAuth(old) tlrno=" + tlrno + ",tranno=" + tranno + ",actfg=" + actfg);

		if (actfg < 0 || actfg > 4) {
			throw new LogicException(titaVo, "EC010", "(CheckAuth.getAuth) actfg=" + actfg);
		}

		CheckAuthVo checkAuthVo = new CheckAuthVo();

		checkAuthVo.setTlrNo(tlrno);

		checkAuthVo.setTranNo(tranno);

		checkAuthVo.setAgenUpdtTlrNo("");
		checkAuthVo.setCanUpdate(false);

		checkAuthVo.setAgenInqtTlrNo("");
		checkAuthVo.setCanInquiry(false);

		try {
			List<Map<String, String>> lCheckAuthVo = checkAuthServiceImpl.findAll(tlrno, tranno);
			for (Map<String, String> tVo : lCheckAuthVo) {
//				F0:"TlrNo\",F1:\"LevelFg\",F2:\"AuthNo\",F3:\"AuthFg\",F4:\"BeginDate\",F5:\"BeginTime\",f6:\"EndDate\",F7:\"EndTime\"
				this.info("Vo = " + tVo.get("F0") + "/" + tVo.get("F1") + "/" + tVo.get("F2") + "/" + tVo.get("F3") + "/" + tVo.get("F4") + "/" + tVo.get("F5"));
				if (actfg == 0) {
					if ("2".equals(tVo.get("F3"))) {
						checkAuthVo = setUpd(checkAuthVo, tlrno, tVo.get("F0"));
						checkAuthVo = setInq(checkAuthVo, tlrno, tVo.get("F0"));
					} else if ("1".equals(tVo.get("F3"))) {
						checkAuthVo = setInq(checkAuthVo, tlrno, tVo.get("F0"));
					}
				} else if ((actfg == 1 || actfg == 3) && "3".equals(tVo.get("F1"))) {
					if ("2".equals(tVo.get("F3"))) {
						checkAuthVo = setUpd(checkAuthVo, tlrno, tVo.get("F0"));
					}
				} else if ((actfg == 2 || actfg == 4) && "1".equals(tVo.get("F1"))) {
					if ("2".equals(tVo.get("F3"))) {
						checkAuthVo = setUpd(checkAuthVo, tlrno, tVo.get("F0"));
					}
				}
			}
		} catch (Throwable e) {
			throw new LogicException(titaVo, "EC004", e.getMessage());
		}

		this.info("CheckAuth.getAuth inquiry=" + checkAuthVo.isCanInquiry() + ",update=" + checkAuthVo.isCanUpdate() + ",agenttlrno=" + checkAuthVo.getAgenInqtTlrNo() + "/"
				+ checkAuthVo.getAgenUpdtTlrNo());

		return checkAuthVo;
	}

	private CheckAuthVo setUpd(CheckAuthVo checkAuthVo, String tlrno, String authtlrno) {

		if (!checkAuthVo.isCanUpdate()) {
			checkAuthVo.setCanUpdate(true);
			if (!tlrno.equals(authtlrno)) {
				checkAuthVo.setAgenUpdtTlrNo(authtlrno);
			}
		}

		return checkAuthVo;
	}

	private CheckAuthVo setInq(CheckAuthVo checkAuthVo, String tlrno, String authtlrno) {

		if (!checkAuthVo.isCanInquiry()) {
			checkAuthVo.setCanInquiry(true);
			if (!tlrno.equals(authtlrno)) {
				checkAuthVo.setAgenInqtTlrNo(authtlrno);
			}
		}

		return checkAuthVo;
	}

	/**
	 * 檢查櫃員交易權限 (舊版)
	 * 
	 * @param titaVo  TitaVo
	 * @param tlrno   櫃員
	 * @param tranno  交易代號
	 * @param actfg   流程步驟(同titaVo.actfg)
	 * @param funcode 0.依交易代號判斷 1.新增 2.修改 3.複製 4.刪除 5.查詢
	 * @return true有權限,/false無權限
	 * @throws LogicException LogicException
	 */
	public boolean isCan(TitaVo titaVo, String tlrno, String tranno, int actfg, int funcode) throws LogicException {

		this.info("CheckAuth.isCan(old) tlrno=" + tlrno + ",tranno=" + tranno + ",actfg=" + actfg + "funcode=" + funcode);

		if (actfg < 0 || actfg > 4) {
			throw new LogicException(titaVo, "EC010", "(CheckAuth.isCan) actfg=" + actfg);
		}

		if (funcode < 0 || funcode > 5) {
			throw new LogicException(titaVo, "EC010", "(CheckAuth.isCan) funcode=" + funcode);
		}

		CheckAuthVo checkAuthVo = this.getAuth(titaVo, tlrno, tranno, actfg);

		if (funcode == 0) {
			String t = tranno.substring(2, 3);
			if ("0".equals(t) || "9".equals(t)) {
				funcode = 5;
			} else {
				funcode = 1;
			}
		}

		if (funcode == 5 && checkAuthVo.isCanInquiry()) {
			return true;
		} else if (funcode >= 1 && funcode <= 4 && checkAuthVo.isCanUpdate()) {
			return true;
		}

		return false;
	}

//	public int getLevel(String TlrNo, String TranNo, int Actfg) throws LogicException {
//
//		return 0;
//	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}
}
