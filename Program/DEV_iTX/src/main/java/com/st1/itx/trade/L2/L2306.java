package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.domain.ReltMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2306")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2306 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ReltMainService sReltMainService;

	@Autowired
	public FacCaseApplService sFacCaseApplService;

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
		this.info("active L2306 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
		}

		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));

		String iRelId = titaVo.getParam("RelId");
		String Ukey = "";
		CustMain lCustMain = new CustMain();

		ReltMain tReltMain = new ReltMain();
		List<ReltMain> tmplReltMain = new ArrayList<ReltMain>();

//		// eloan resend
//		if (isEloan && iFunCd == 1) {
//			ReltMainId ReltMainIdVo = new ReltMainId();
//			ReltMainIdVo.setCaseNo(iCaseNo);
//			ReltMainIdVo.setCustNo(iCustNo);
//			ReltMainIdVo.setReltId(iRelId);
//			tReltMain.setReltMainId(ReltMainIdVo);
//			tReltMain = sReltMainService.findById(ReltMainIdVo);
//			if (tReltMain != null) {
//				iFunCd = 2;
//			}
//		}

		// 新增
		if (iFunCd == 1) {

			lCustMain = sCustMainService.custIdFirst(iRelId, titaVo);

			if (lCustMain == null) { // 沒在客戶檔 同步新增資料到客戶檔
				Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				// PK

				tReltMain = new ReltMain();

				ReltMainId ReltMainIdVo = new ReltMainId();
				ReltMainIdVo.setCaseNo(iCaseNo);
				ReltMainIdVo.setCustNo(iCustNo);
				ReltMainIdVo.setReltUKey(Ukey);
				tReltMain.setReltMainId(ReltMainIdVo);

				tReltMain.setReltCode(titaVo.getParam("PosInd"));
				tReltMain.setRemarkType(titaVo.getParam("RemarkType"));
				tReltMain.setReltmark(titaVo.getParam("Remark"));
				tReltMain.setApplDate(parse.stringToInteger(titaVo.getEntDy()));
				/* 存入DB */

				try {
					sReltMainService.insert(tReltMain);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}

				// new table
				CustMain tCustMain = new CustMain();
				tCustMain.setCustUKey(Ukey);
				tCustMain.setCustId(iRelId);
				tCustMain.setCustName(titaVo.getParam("RelName"));
				tCustMain.setDataStatus(1);
				tCustMain.setTypeCode(4);
				if (iRelId.length() == 8) {
					tCustMain.setCuscCd("2");
				} else {
					tCustMain.setCuscCd("1");
				}
				try {
					sCustMainService.insert(tCustMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "客戶資料主檔");
				}

//	    		/*需加丟訊息  要至客戶資料主檔補件資料*/
//	    		this.totaVo.putParam("OWarningMsg", "需至顧客明細資料補件資料");

			} else { // 有在客戶檔
				Ukey = lCustMain.getCustUKey();
				tReltMain = new ReltMain();

				ReltMainId ReltMainIdVo = new ReltMainId();
				ReltMainIdVo.setCaseNo(iCaseNo);
				ReltMainIdVo.setCustNo(iCustNo);
				ReltMainIdVo.setReltUKey(Ukey);
				tReltMain.setReltMainId(ReltMainIdVo);
				tReltMain.setCaseNo(iCaseNo);
				tReltMain.setCustNo(iCustNo);
				tReltMain.setReltUKey(Ukey);
				tReltMain.setReltCode(titaVo.getParam("PosInd"));
				tReltMain.setRemarkType(titaVo.getParam("RemarkType"));
				tReltMain.setReltmark(titaVo.getParam("Remark"));
				tReltMain.setApplDate(parse.stringToInteger(titaVo.getEntDy()));
				/* 存入DB */

				try {
					sReltMainService.insert(tReltMain);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}

			} // else

			// 修改
		} else if (iFunCd == 2) {

			lCustMain = sCustMainService.custIdFirst(iRelId, titaVo);

			if (lCustMain == null) {
				throw new LogicException("E0001", "客戶資料主檔");
			}

			Ukey = lCustMain.getCustUKey();

			ReltMainId ReltMainIdVo = new ReltMainId();
			ReltMainIdVo.setCaseNo(iCaseNo);
			ReltMainIdVo.setCustNo(iCustNo);
			ReltMainIdVo.setReltUKey(Ukey);
			tReltMain.setReltMainId(ReltMainIdVo);
			tReltMain = sReltMainService.holdById(ReltMainIdVo);

			if (tReltMain == null) {
				throw new LogicException(titaVo, "E0003", "關係人主檔");
			}

			// 變更前
			ReltMain beforeReltMain = (ReltMain) dataLog.clone(tReltMain);
			tReltMain.setCaseNo(iCaseNo);
			tReltMain.setCustNo(iCustNo);
			tReltMain.setReltUKey(Ukey);
			tReltMain.setReltCode(titaVo.getParam("PosInd"));
			tReltMain.setRemarkType(titaVo.getParam("RemarkType"));
			tReltMain.setReltmark(titaVo.getParam("Remark"));

			try {
				// 修改
				tReltMain = sReltMainService.update2(tReltMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeReltMain, tReltMain);
			dataLog.exec();

			// 刪除
		} else if (iFunCd == 4) {

			lCustMain = sCustMainService.custIdFirst(iRelId, titaVo);

			if (lCustMain == null) {
				throw new LogicException("E0001", "客戶資料主檔");
			}

			Ukey = lCustMain.getCustUKey();

			ReltMainId ReltMainIdVo = new ReltMainId();
			ReltMainIdVo.setCaseNo(iCaseNo);
			ReltMainIdVo.setCustNo(iCustNo);
			ReltMainIdVo.setReltUKey(Ukey);
			tReltMain.setReltMainId(ReltMainIdVo);
			tReltMain = sReltMainService.holdById(ReltMainIdVo);

			if (tReltMain == null) {
				throw new LogicException(titaVo, "E0004", "關係人主檔");
			}

			try {
				sReltMainService.delete(tReltMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}

		}
		// 抓該戶號所有資料更新Finalfg

		int CreatDate = 0;
		int tempcase = 0;
		FacCaseAppl tFacCaseAppl = new FacCaseAppl();
		Slice<ReltMain> slReltMain = sReltMainService.custNoEq(iCustNo, index, limit, titaVo);
		tmplReltMain = slReltMain == null ? null : slReltMain.getContent();
		if (tmplReltMain != null) {
			for (ReltMain ttReltMain : tmplReltMain) {
				tFacCaseAppl = sFacCaseApplService.CreditSysNoFirst(ttReltMain.getCaseNo(), titaVo);
				if (tFacCaseAppl != null) {
					if (CreatDate < tFacCaseAppl.getApplDate()) {
						CreatDate = tFacCaseAppl.getApplDate();
						tempcase = ttReltMain.getCaseNo();
					}
				}
				tFacCaseAppl = new FacCaseAppl();
			}
			// 先全部清空再把最新的案件編號上"Y"
			for (ReltMain ttReltMain : tmplReltMain) {

				ReltMainId ReltMainIdVo = new ReltMainId();
				ReltMainIdVo = ttReltMain.getReltMainId();
				tReltMain = sReltMainService.holdById(ReltMainIdVo);

				if (tReltMain == null) {
					throw new LogicException(titaVo, "E0003", "關係人主檔案件記號錯誤");
				}

				tReltMain.setFinalFg("");
				if (tempcase == tReltMain.getCaseNo()) {
					tReltMain.setFinalFg("Y");
				}

				try {
					// 修改
					sReltMainService.update(tReltMain);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}

			} // for
		} // if

		this.addList(this.totaVo);
		return this.sendList();
	}
}