package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacRelation;
import com.st1.itx.db.domain.FacRelationId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacRelationService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2221")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2221 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public FacRelationService sFacRelationService;

	/* 日期工具 */
	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2221 ");
		this.totaVo.init(titaVo);

		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));
		String iCustId = titaVo.getParam("CustId");
		String Ukey = "";
		CustMain lCustMain = new CustMain();

		// new table PK
		FacRelationId tfacRelationId = new FacRelationId();

		if (iFunCd == 1) {

			lCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

			if (lCustMain == null) { // 沒在客戶檔 同步新增資料到客戶檔
				Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				// PK
				tfacRelationId.setCreditSysNo(iCaseNo);
				tfacRelationId.setCustUKey(Ukey);

				// new table
				FacRelation tFacRelation = new FacRelation();

				tFacRelation.setFacRelationId(tfacRelationId);
				tFacRelation.setCreditSysNo(iCaseNo);
				tFacRelation.setCustUKey(Ukey);
				tFacRelation.setFacRelationCode(titaVo.getParam("FacRelationCode"));


				try {
					sFacRelationService.insert(tFacRelation, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "交易關係人檔");
				}

				// new table
				CustMain tCustMain = new CustMain();
				tCustMain.setCustUKey(Ukey);
				tCustMain.setCustId(iCustId);
				tCustMain.setCustName(titaVo.getParam("CustName"));
				tCustMain.setDataStatus(1);
				tCustMain.setTypeCode(3);
				if (iCustId.length() == 8) {
					tCustMain.setCuscCd("2");
				} else {
					tCustMain.setCuscCd("1");
				}
				try {
					sCustMainService.insert(tCustMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "客戶資料主檔");
				}

				/* 需加丟訊息 要至客戶資料主檔補件資料 */
				this.totaVo.putParam("OWarningMsg", "需至顧客明細資料補件資料");

			} else { // 有在客戶檔
				Ukey = lCustMain.getCustUKey();

				tfacRelationId.setCreditSysNo(iCaseNo);
				tfacRelationId.setCustUKey(Ukey);

				FacRelation FacRelation = sFacRelationService.findById(tfacRelationId, titaVo);

				if (FacRelation != null) {
					throw new LogicException(titaVo, "E0002", "交易關係人主檔");
				}

				// new table
				FacRelation tFacRelation = new FacRelation();

				tFacRelation.setFacRelationId(tfacRelationId);
				tFacRelation.setCreditSysNo(parse.stringToInteger(titaVo.getParam("CaseNo")));
				tFacRelation.setCustUKey(Ukey);
				tFacRelation.setFacRelationCode(titaVo.getParam("FacRelationCode"));

				try {
					sFacRelationService.insert(tFacRelation, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "交易關係人檔");
				}

				this.totaVo.putParam("OWarningMsg", "");
			} // else
		} else if (iFunCd == 2) {

			lCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

			if (lCustMain == null) {
				throw new LogicException("E0001", "客戶資料主檔");
			}

			Ukey = lCustMain.getCustUKey();

			tfacRelationId.setCreditSysNo(iCaseNo);
			tfacRelationId.setCustUKey(Ukey);

			FacRelation FacRelation = sFacRelationService.findById(tfacRelationId, titaVo);

			if (FacRelation == null) {
				throw new LogicException(titaVo, "E0003", "交易關係人主檔");
			}

			// 變更前
			FacRelation beforeFacRelation = (FacRelation) dataLog.clone(FacRelation);

			// new table
			FacRelation tFacRelation = new FacRelation();

			tFacRelation = sFacRelationService.holdById(tfacRelationId);
			tFacRelation.setFacRelationId(tfacRelationId);
			tFacRelation.setCreditSysNo(parse.stringToInteger(titaVo.getParam("CaseNo")));
			tFacRelation.setCustUKey(Ukey);
			tFacRelation.setFacRelationCode(titaVo.getParam("FacRelationCode"));


			try {
				sFacRelationService.update(tFacRelation, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "交易關係人檔");
			}

			if (!lCustMain.getCustName().equals(titaVo.getParam("CustName"))) { // 姓名不同 更新客戶主檔戶名
				lCustMain.setCustName(titaVo.getParam("CustName"));

				try {
					sCustMainService.update(lCustMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "客戶資料主檔");
				}
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeFacRelation, tFacRelation);
			dataLog.exec("修改掃描類別");

			this.totaVo.putParam("OWarningMsg", "");
		} else if (iFunCd == 4) {

			lCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

			if (lCustMain == null) {
				throw new LogicException("E0001", "客戶資料主檔");
			}

			Ukey = lCustMain.getCustUKey();

			tfacRelationId.setCreditSysNo(iCaseNo);
			tfacRelationId.setCustUKey(Ukey);

			FacRelation dFacRelation = sFacRelationService.findById(tfacRelationId, titaVo);

			if (dFacRelation == null) {
				throw new LogicException(titaVo, "E0004", "交易關係人主檔");
			}

			// new table
			FacRelation tFacRelation = new FacRelation();

			tFacRelation = sFacRelationService.holdById(tfacRelationId);

			try {
				sFacRelationService.delete(tFacRelation, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "交易關係人檔");
			}
			dataLog.setEnv(titaVo, tFacRelation, tFacRelation);
			dataLog.exec("刪除交易關係人檔");
			this.totaVo.putParam("OWarningMsg", "");
		} // else

		this.addList(this.totaVo);
		return this.sendList();

	}
}