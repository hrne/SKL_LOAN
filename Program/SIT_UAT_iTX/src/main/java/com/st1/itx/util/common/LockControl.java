package com.st1.itx.util.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxLock;
import com.st1.itx.db.service.TxLockService;
import com.st1.itx.db.domain.TxUnLock;
import com.st1.itx.db.service.TxUnLockService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;

/**
 * 鎖住戶號<BR>
 * 1. ToLock 鎖定資料 call by LCR97, ApControl<BR>
 * 1.1 經辦進入L3更新類畫面，輸入戶號後，送 LCR97 RIM 鎖定戶號<BR>
 * 1.1 ApControl 一段式、二段式交易流程開始，鎖定戶號<BR>
 * 1.2 訂正交易 ApControl 鎖定戶號<BR>
 * 2. ToUnLock 解除鎖定資料 call by ApControl，LC110<BR>
 * 2.1 ApControl 一段式、二段式交易流程結束，解除鎖定<BR>
 * 2.2 經辦執行LC110 解除鎖定，寫入TxUnLock 人工解除鎖定控制檔<BR>
 * 
 * @author st1
 *
 */

@Component("lockControl")
@Scope("prototype")

public class LockControl extends CommBuffer {
	/* DB服務注入 */
	@Autowired
	public TxLockService sTxLockService;

	@Autowired
	public TxUnLockService sTxUnLockService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	/**
	 * 鎖定資料
	 * 
	 * @param CustNo   客戶戶號
	 * @param TranNo   交易代號
	 * @param unLockNo 原 LockNo
	 * @return lockno 新 LockNo
	 * @throws LogicException LogicException
	 */
	public long ToLock(int CustNo, String TranNo, Long unLockNo) throws LogicException {
		long lockno = 0;
		//
		Slice<TxLock> t = sTxLockService.findByCustNo(CustNo, this.index, this.limit);
		List<TxLock> lTxLock = t == null ? null : t.getContent();

		this.info("LockControl ToLock = " + CustNo + "/" + TranNo);
		// 戶號未被鎖定，則新增該戶號鎖定控制檔，回應新 LockNo
		// 戶號被相同的LockNo鎖定，回應原 LockNo
		// 戶號被不同的LockNo鎖定，回應錯誤訊息
		if (lTxLock == null) {
			if (unLockNo > 0) {
				this.ToUnLock(unLockNo, 0, false, false);
			}
			try {
				TxLock tTxLock = new TxLock();
				tTxLock.setCustNo(CustNo);
				tTxLock.setTranNo(TranNo);
				tTxLock.setBrNo(titaVo.getKinbr());
				tTxLock.setTlrNo(titaVo.getTlrNo());
//				tTxLock.setCreateDate(
//						parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
//				tTxLock.setCreateEmpNo(this.titaVo.getTlrNo());
				this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);
				tTxLock = sTxLockService.insert(tTxLock, this.titaVo);
				lockno = tTxLock.getLockNo();
			} catch (DBException e) {
				throw new LogicException(titaVo, "EC002", "鎖定戶號" + CustNo + ":" + e.getErrorMsg());
			}
		} else {
			TxLock tTxLock = lTxLock.get(0);
			if (tTxLock.getLockNo() != unLockNo) {
				throw new LogicException(titaVo, "E0006", "戶號 " + CustNo + " 已被單位(" + tTxLock.getBrNo() + ") 櫃員(" + tTxLock.getTlrNo() + ") 操作 " + tTxLock.getTranNo() + " 交易,，鎖定中");
			}
			lockno = unLockNo;
		}

		return lockno;
	}

	/**
	 * 解除鎖定資料
	 * 
	 * @param LockNo 銷定序號
	 * @param CustNo 銷定戶號
	 * @throws LogicException LogicException
	 */
	public void ToUnLock(long LockNo, int CustNo) throws LogicException {

		ToUnLock2(LockNo, CustNo, true, true);

	}

	/**
	 * 解除鎖定資料
	 * 
	 * @param LockNo 銷定序號
	 * @param CustNo 銷定戶號
	 * @param MustFg 鎖定資料必需存在
	 * @param LogFg  寫TxUnlock紀錄
	 * @throws LogicException LogicException
	 */
	public void ToUnLock(long LockNo, int CustNo, boolean MustFg, boolean LogFg) throws LogicException {

		ToUnLock2(LockNo, CustNo, MustFg, LogFg);
	}

	private void ToUnLock2(long LockNo, int CustNo, boolean MustFg, boolean LogFg) throws LogicException {

		this.info("LockControl ToUnLock = " + LockNo + "/" + CustNo + "/" + MustFg + "/" + LogFg);
		// ApControl -> MustFg 正常交易=true、訂正交易=false，LogFg = flase
		// LC110 => MustFg=true，LogFg = true
		TxLock tTxLock = sTxLockService.holdById(LockNo);
		if (tTxLock == null) {
			if (MustFg) {
//				throw new LogicException(titaVo, "EC001", "鎖定戶號序號 " + LockNo);
				TxUnLock tTxUnLock = sTxUnLockService.findById(LockNo);
				if (tTxUnLock == null) {
					throw new LogicException(titaVo, "EC001", "鎖定戶號 (" + CustNo + ") 序號 (" + LockNo + ") 已被解除");
				} else {
					throw new LogicException(titaVo, "EC001", "戶號 " + CustNo + " 已被單位(" + tTxUnLock.getBrNo2() + ") 櫃員(" + tTxUnLock.getTlrNo2() + ") 解除鎖定");
				}
			}
		} else {
			if (LogFg) {
				try {
					TxUnLock tTxUnLock = new TxUnLock();
					tTxUnLock.setCustNo(tTxLock.getCustNo());
					tTxUnLock.setLockNo(LockNo);
					tTxUnLock.setTranNo(tTxLock.getTranNo());
					tTxUnLock.setEntdy(Integer.valueOf(titaVo.getEntDy()));
					tTxUnLock.setBrNo(tTxLock.getBrNo());
					tTxUnLock.setTlrNo(tTxLock.getTlrNo());
					tTxUnLock.setBrNo2(titaVo.getKinbr());
					tTxUnLock.setTlrNo2(titaVo.getTlrNo());
//					tTxUnLock.setCreateDate(tTxLock.getCreateDate());
//					tTxUnLock.setCreateEmpNo(tTxLock.getCreateEmpNo());
					tTxUnLock.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tTxUnLock.setLastUpdateEmpNo(titaVo.getTlrNo());
					this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);
					sTxUnLockService.insert(tTxUnLock, this.titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0002", "戶號解除鎖定紀錄檔:" + e.getErrorMsg());
				}
			}
			try {
				sTxLockService.delete(tTxLock);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0006", "鎖定戶號(" + CustNo + ")檔:" + e.getErrorMsg());
			}

		}

	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}
}
