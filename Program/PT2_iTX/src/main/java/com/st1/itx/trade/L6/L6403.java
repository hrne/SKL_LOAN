package com.st1.itx.trade.L6;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
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

		String iFunCode = titaVo.get("FunCode").trim();
		String iAuthNo = titaVo.get("AuthNo").trim();

		// 檢查使用複製功能，新增的權限群組(AuthNo)沒有重複
		TxAuthGroup tTxAuthGroup = sTxAuthGroupService.findById(iAuthNo, titaVo);
		if (tTxAuthGroup != null && "3".equals(iFunCode)) {
			throw new LogicException(titaVo, "E0002", "權限群組:" + iAuthNo);
		}

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
}