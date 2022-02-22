package com.st1.itx.trade.L6;

import java.sql.Timestamp;
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
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.domain.TxTellerAuth;
import com.st1.itx.db.domain.TxTellerAuthId;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.service.TxTellerAuthService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.util.data.DataLog;
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
	public TxAuthGroupService sTxAuthGroupService;
	
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	
	@Autowired
	public DataLog dataLog;
	

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
					TxTeller tTxTeller2 = (TxTeller) dataLog.clone(tTxTeller);
					
					tTxTeller = MoveToDb(iTlrNo, tTxTeller, titaVo);
					tTxTeller = txTellerService.update2(tTxTeller,titaVo);
					
					moveGroup(iTlrNo, iFunCode, titaVo);
					
					dataLog.compareOldNew("無權限");
					
					tTxTeller2.setMntDate(tTxTeller.getMntDate());
					tTxTeller2.setMntEmpNo(tTxTeller.getMntEmpNo());
					
					dataLog.setEnv(titaVo, tTxTeller2, tTxTeller);
					dataLog.exec("修改使用者 " + tTxTeller.getTlrNo() + " 資料");
					
					
				} else if ("4".equals(iFunCode)) {
					txTellerService.delete(tTxTeller);
					moveGroup(iTlrNo, iFunCode, titaVo);
				}
			} catch (DBException e) {
				if ("2".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				} else if ("4".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
			
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
		
		tTxTeller.setStation(titaVo.getParam("Station"));
		tTxTeller.setAdminFg(Integer.parseInt(titaVo.getParam("AdminFg")));
		
		tTxTeller.setMntEmpNo(titaVo.getTlrNo());       
		Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        tTxTeller.setMntDate(timestamp);
        
		return tTxTeller;

	}

	private void moveGroup(String mTlrNo, String mFuncCode, TitaVo titaVo) throws LogicException {

		this.info("into moveGroup");

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;
		
		if (("2").equals(mFuncCode) || ("4").equals(mFuncCode)) {

			Slice<TxTellerAuth> mTxTellerAuth = sTxTellerAuthService.findByTlrNo(mTlrNo, this.index, this.limit, titaVo);
			List<TxTellerAuth> iTxTellerAuth = mTxTellerAuth == null ? null : mTxTellerAuth.getContent();
			if (iTxTellerAuth != null) {
				try {
					sTxTellerAuthService.deleteAll(iTxTellerAuth);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
				
				for (TxTellerAuth txTellerAuth : iTxTellerAuth) {
					TxAuthGroup txAuthGroup = sTxAuthGroupService.findById(txTellerAuth.getAuthNo(), titaVo);
					if(txAuthGroup!=null) {
						//刪除時將原本的記入"有權限"
						dataLog.putOld(txTellerAuth.getAuthNo() + " " + txAuthGroup.getAuthItem(),"有權限");
					}
						
				}
				
			}
		}

		if (("1").equals(mFuncCode) || ("2").equals(mFuncCode)) {
			
			if(("2").equals(mFuncCode)) {
				for (int j = 1; j <= 40; j++) {
				
					if (titaVo.getParam("AuthNo" + j) != null && titaVo.getParam("AuthNo" + j).length() > 0) {	
						TxAuthGroup txAuthGroup2 = sTxAuthGroupService.findById(titaVo.getParam("AuthNo" + j), titaVo);
						if(txAuthGroup2!=null) {//修改時將此次TITA記入"有權限"
							dataLog.putNew(txAuthGroup2.getAuthNo() + " " + txAuthGroup2.getAuthItem(),"有權限");
							this.info("insert "+txAuthGroup2.getAuthNo()+"有權限");
						}
					}
				}
					
					
					Slice<TxAuthGroup> txAuthGroup1 = sTxAuthGroupService.findAll(this.index, this.limit, titaVo);
					List<TxAuthGroup> AuthGroup1 = txAuthGroup1 == null ? null : txAuthGroup1.getContent();
					
					for(TxAuthGroup AuthGroup : AuthGroup1) {//修改時將除TITA之外記入"無權限"
						int type = 0;
						for (int k = 1; k <= 40; k++) {
							if((AuthGroup.getAuthNo()).equals(titaVo.getParam("AuthNo" + k))) {
								type=1;
								break;
							}
						}
						if(type==0) {
							TxAuthGroup txAuthGroup2 = sTxAuthGroupService.findById(AuthGroup.getAuthNo(), titaVo);
							dataLog.putNew(txAuthGroup2.getAuthNo() + " " + txAuthGroup2.getAuthItem(),"無權限");
							this.info("insert "+txAuthGroup2.getAuthNo()+"無權限");
						}
						
					}
						
				}
					
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