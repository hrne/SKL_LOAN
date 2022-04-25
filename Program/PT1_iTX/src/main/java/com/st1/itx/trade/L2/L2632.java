package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2632")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2632 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	AcReceivableCom acReceivableCom;

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
		// 額度編號
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 清償金額
		BigDecimal iCloseAmt = parse.stringToBigDecimal(titaVo.getParam("TimCloseAmt"));
		// 作業項目
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode"));

		// new PK
		FacCloseId FacCloseId = new FacCloseId();
		FacCloseId.setCustNo(iCustNo);
		FacCloseId.setCloseNo(iCloseNo);
		// new table
		FacClose tFacClose = new FacClose();

		// 修改
		if (iFunCd == 2) {
			tFacClose = sFacCloseService.holdById(FacCloseId);
			if (tFacClose == null) {
				throw new LogicException(titaVo, "E0003", "戶號= " + iCustNo + " 清償序號 =" + iCloseNo); // 修改資料不存在
			}
			// 變更前
			FacClose beforeFacClose = (FacClose) dataLog.clone(tFacClose);

			tFacClose.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tFacClose.setEntryDate(parse.stringToInteger(titaVo.getParam("EntryDate")));
			tFacClose.setCloseInd(titaVo.getParam("CloseInd"));
			tFacClose.setCollectFlag(titaVo.getParam("CollectFlag"));
			tFacClose.setCloseReasonCode(titaVo.getParam("CloseReasonCode"));
			tFacClose.setCloseAmt(parse.stringToBigDecimal(titaVo.getParam("TimCloseAmt")));
			tFacClose.setCollectWayCode(titaVo.getParam("CollectWayCode"));
			tFacClose.setAgreeNo(titaVo.getParam("AgreeNo"));
			tFacClose.setDocNo(parse.stringToInteger(titaVo.getParam("DocNo")));
			tFacClose.setClsNo(titaVo.getParam("ClsNo"));
			tFacClose.setTelNo1(titaVo.getParam("TelNo1"));
			tFacClose.setTelNo2(titaVo.getParam("TelNo2"));
			tFacClose.setTelNo3(titaVo.getParam("TelNo3"));
			tFacClose.setRmk(titaVo.getParam("Rmk"));

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

			FacClose tFacClose4 = sFacCloseService.holdById(FacCloseId);
			this.info(" L2632 tFacClose4" + tFacClose4);
			if (tFacClose4 == null) {
				throw new LogicException(titaVo, "E0004", "戶號= " + iCustNo + " 清償序號 =" + iCloseNo); // 刪除資料不存在
			}
			try {
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, tFacClose4, tFacClose4);
				dataLog.exec("刪除清償作業");
				sFacCloseService.delete(tFacClose4);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			if (iItemCode == 2) {

				// 銷帳
				AcReceivable acReceivable = new AcReceivable();
				List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
				acReceivable.setReceivableFlag(4); // 短繳期金
				acReceivable.setAcctCode("YOP");
				acReceivable.setCustNo(iCustNo);
				acReceivable.setFacmNo(iFacmNo);
				acReceivable.setRvNo("000");
				acReceivable.setRvAmt(iCloseAmt);
				acReceivableList.add(acReceivable);
				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除

			}

		} else if (iFunCd == 5) {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}