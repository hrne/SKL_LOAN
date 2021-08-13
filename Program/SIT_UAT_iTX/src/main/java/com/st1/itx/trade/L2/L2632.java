package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2632")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2632 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2632.class);

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2632 ");
		this.totaVo.init(titaVo);

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 清償序號
		int iCloseNo = parse.stringToInteger(titaVo.getParam("CloseNo"));
		// 功能
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCode"));

		// new PK
		FacCloseId FacCloseId = new FacCloseId();
		FacCloseId.setCustNo(iCustNo);
		FacCloseId.setCloseNo(iCloseNo);
		// new table
		FacClose tFacClose = new FacClose();

		// 修改
		if (iFunCd == 2) {
			tFacClose = sFacCloseService.holdById(FacCloseId);
			// 變更前
			FacClose beforeFacClose = (FacClose) dataLog.clone(tFacClose);

			tFacClose.setActFlag(parse.stringToInteger(titaVo.getParam("ACTFG")));
			tFacClose.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tFacClose.setCloseReasonCode(titaVo.getParam("CloseReasonCode"));
			tFacClose.setCloseAmt(parse.stringToBigDecimal(titaVo.getParam("TIM_PAYOFF_AMT")));
			tFacClose.setCollectWayCode(titaVo.getParam("CollectWayCode"));
			tFacClose.setReceiveDate(parse.stringToInteger(titaVo.getParam("ReceiveDate")));
			tFacClose.setAgreeNo(titaVo.getParam("AgreeNo"));
			tFacClose.setDocNo(parse.stringToInteger(titaVo.getParam("DocNo")));
			tFacClose.setClsNo(titaVo.getParam("ClsNo"));
			tFacClose.setTelNo1(titaVo.getParam("TelNo1"));
			tFacClose.setTelNo2(titaVo.getParam("TelNo2"));

			try {
				// 修改
				tFacClose = sFacCloseService.update2(tFacClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeFacClose, tFacClose);
			dataLog.exec();
			// 刪除
		} else if (iFunCd == 4) {

			try {
				// PK找關係人主檔HOLD資料

				FacClose tFacClose4 = sFacCloseService.holdById(FacCloseId);
				this.info(" L2632 tFacClose4" + tFacClose4);

				if (tFacClose4 != null) {
					sFacCloseService.delete(tFacClose4);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		} else if (iFunCd == 5) {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}