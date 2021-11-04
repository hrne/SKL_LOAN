package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6202")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6202 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6202.class);

	@Autowired
	public CdSyndFeeService cdSyndFeeService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6202 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCd = this.parse.stringToInteger(titaVo.getParam("FunCd"));
		String iSyndFeeCode = titaVo.getParam("SyndFeeCode");

		// 檢查輸入資料
		if (!(iFunCd >= 1 && iFunCd <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6607"); // 功能選擇錯誤
		}

		// 更新保證人關係代碼檔
		CdSyndFee tCdSyndFee = new CdSyndFee();
		switch (iFunCd) {
		case 1: // 新增
			moveCdSyndFee(tCdSyndFee, iFunCd, titaVo);
			try {
				cdSyndFeeService.insert(tCdSyndFee, titaVo);

			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdSyndFee = cdSyndFeeService.holdById(iSyndFeeCode);
			if (tCdSyndFee == null) {
				throw new LogicException(titaVo, "E0003", iSyndFeeCode); // 修改資料不存在
			}
			CdSyndFee tCdSyndFee2 = (CdSyndFee) dataLog.clone(tCdSyndFee);
			try {
				moveCdSyndFee(tCdSyndFee, iFunCd, titaVo);
				tCdSyndFee = cdSyndFeeService.update2(tCdSyndFee, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdSyndFee2, tCdSyndFee);
			dataLog.exec();
			break;

		case 4: // 刪除
			tCdSyndFee = cdSyndFeeService.holdById(iSyndFeeCode);
			this.info("L6202 del : " + iFunCd + iSyndFeeCode);
			if (tCdSyndFee != null) {

				// 刪除須刷主管卡
				if (titaVo.getEmpNos().trim().isEmpty()) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
				}
				try {
					cdSyndFeeService.delete(tCdSyndFee);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iSyndFeeCode); // 刪除資料不存在
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdSyndFee(CdSyndFee mCdSyndFee, int mFuncCode, TitaVo titaVo) throws LogicException {

		mCdSyndFee.setSyndFeeCode(titaVo.getParam("SyndFeeCode"));
		mCdSyndFee.setSyndFeeItem(titaVo.getParam("SyndFeeItem"));
		mCdSyndFee.setAcctCode(titaVo.getParam("AcctCode"));

	}
}