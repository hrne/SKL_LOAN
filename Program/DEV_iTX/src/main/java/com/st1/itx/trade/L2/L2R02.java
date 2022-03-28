package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimFKey=9,1
 * RimApplNo=9,7
 * RimCustId=X,10
 */
/**
 * L2R02 尋找案件申請檔資料
 * 
 * @author iza
 * @version 1.0.0
 */

@Service("L2R02")
@Scope("prototype")
public class L2R02 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R02 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		int iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		int iRimApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));
		String iRimCustId = titaVo.getParam("RimCustId").trim();

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", ""); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 7)) {
			throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
		}

		// 查詢客戶檔
		String wkCustUkey = "";
		CustMain tCustMain = new CustMain();
		if (iRimTxCode.equals("L2111") || iRimTxCode.equals("L2153")) {
			tCustMain = custMainService.custIdFirst(iRimCustId, titaVo);
			if (tCustMain != null) {
				wkCustUkey = tCustMain.getCustUKey().trim();
			} else {
				throw new LogicException(titaVo, "E0001", " 客戶資料主檔" + iRimCustId); // 查無資料
			}
		}
		// 查詢案件申請檔
		FacCaseAppl tFacCaseAppl = facCaseApplService.findById(iRimApplNo, titaVo);
		/* 如有有找到資料 */
		if (tFacCaseAppl != null) {
			// 檢查案件申請檔
			switch (iRimTxCode) {
			case "L2111": // 案件申請登錄
				if (iRimFuncCode != 3 && !tFacCaseAppl.getCustUKey().trim().equals(wkCustUkey)) {
					this.info("L2R02 CustUkey=" + wkCustUkey);
					this.info("L2R02 ApplUkey=" + tFacCaseAppl.getCustUKey().trim());
					throw new LogicException(titaVo, "E2061", ""); // 統一編號與申請案件不符
				}
				if (iRimFuncCode == 1) {
					throw new LogicException(titaVo, "E2062", ""); // 申請案件檔，資料已存在
				}
				if (iRimFuncCode == 4 && !tFacCaseAppl.getProcessCode().equals("0")) {
					throw new LogicException(titaVo, "E2063", ""); // 已作過准駁處理之案件不可刪除
				}
				break;
			case "L2151": // 駁回額度登錄
				if (iRimFuncCode == 6 || iRimFuncCode == 7) {
					if (tFacCaseAppl.getProcessCode().equals("1")) {
						throw new LogicException(titaVo, "E2064", ""); // 申請案件已核准
					}
					if (tFacCaseAppl.getProcessCode().equals("2")) {
						throw new LogicException(titaVo, "E2065", ""); // 申請案件已駁回
					}
				}
				break;
			case "L2153": // 核准額度登錄
				if (!tFacCaseAppl.getCustUKey().trim().equals(wkCustUkey)) {
					this.info("L2R02 CustUkey=" + wkCustUkey);
					this.info("L2R02 ApplUkey=" + tFacCaseAppl.getCustUKey().trim());
					throw new LogicException(titaVo, "E2061", ""); // 統一編號與申請案件不符
				}
				if (iRimFuncCode == 6 || iRimFuncCode == 7) {
					if (tFacCaseAppl.getProcessCode().equals("1") && iFKey == 0) {
						throw new LogicException(titaVo, "E2064", ""); // 申請案件已核准
					}
					if (tFacCaseAppl.getProcessCode().equals("2")) {
						throw new LogicException(titaVo, "E2065", ""); // 申請案件已駁回
					}
				}
				break;
			}
			// 將每筆資料放入Tota
			this.totaVo.putParam("L2r02ApplNo", tFacCaseAppl.getApplNo());
			tCustMain = custMainService.findById(tFacCaseAppl.getCustUKey(), titaVo);
			if (tCustMain != null) {
				this.totaVo.putParam("L2r02CustId", tCustMain.getCustId());
			} else {
				throw new LogicException(titaVo, "E0001", " 客戶資料主檔" + tFacCaseAppl.getCustUKey()); // 查無資料
			}
			this.totaVo.putParam("L2r02CreditSysNo", tFacCaseAppl.getCreditSysNo());
			this.totaVo.putParam("L2r02SyndNo", tFacCaseAppl.getSyndNo());
			this.totaVo.putParam("L2r02ApplDate", tFacCaseAppl.getApplDate());
			this.totaVo.putParam("L2r02DepartmentCode", tFacCaseAppl.getDepartmentCode());
			this.totaVo.putParam("L2r02ProdNo", tFacCaseAppl.getProdNo());
			this.totaVo.putParam("L2r02AcctCode", "");
			this.totaVo.putParam("L2r02CurrencyCode", tFacCaseAppl.getCurrencyCode());
			this.totaVo.putParam("L2r02IsLimit", tFacCaseAppl.getIsLimit());
			this.totaVo.putParam("L2r02IsRelated", tFacCaseAppl.getIsRelated());
			this.totaVo.putParam("L2r02IsLnrelNear", tFacCaseAppl.getIsLnrelNear());
			// 2022.3.28 新增 By昱衡
			this.totaVo.putParam("L2r02IsSuspected", tFacCaseAppl.getIsSuspected());
			this.totaVo.putParam("L2r02IsSuspectedCheck", tFacCaseAppl.getIsSuspectedCheck());
			this.totaVo.putParam("L2r02IsSuspectedCheckType", tFacCaseAppl.getIsSuspectedCheckType());
			this.totaVo.putParam("L2r02IsDate", tFacCaseAppl.getIsDate());			
			
			this.totaVo.putParam("L2r02ApplAmt", tFacCaseAppl.getApplAmt());
			this.totaVo.putParam("L2r02Estimate", tFacCaseAppl.getEstimate());
			this.totaVo.putParam("L2r02PieceCode", tFacCaseAppl.getPieceCode());
			this.totaVo.putParam("L2r02CreditOfficer", tFacCaseAppl.getCreditOfficer());
			this.totaVo.putParam("L2r02LoanOfficer", tFacCaseAppl.getLoanOfficer());
			this.totaVo.putParam("L2r02Introducer", tFacCaseAppl.getIntroducer());
			this.totaVo.putParam("L2r02Supervisor", tFacCaseAppl.getSupervisor());
			this.totaVo.putParam("L2r02Coorgnizer", tFacCaseAppl.getCoorgnizer());
			this.totaVo.putParam("L2r02ProcessCode", tFacCaseAppl.getProcessCode());
			this.totaVo.putParam("L2r02ApproveDate", tFacCaseAppl.getApproveDate());
			if (tFacCaseAppl.getGroupUKey().isEmpty()) {
				this.totaVo.putParam("L2r02GroupId", "");
			} else {
				tCustMain = custMainService.findById(tFacCaseAppl.getGroupUKey(), titaVo);
				if (tCustMain != null) {
					this.totaVo.putParam("L2r02GroupId", tCustMain.getCustId());
				} else {
					throw new LogicException(titaVo, "E0001", " 客戶資料主檔" + tFacCaseAppl.getGroupUKey()); // 查無資料
				}
			}
		} else {
			if (!(iRimTxCode.equals("L2111") && iRimFuncCode == 1)) {
				throw new LogicException(titaVo, "E0001", " 案件申請檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}