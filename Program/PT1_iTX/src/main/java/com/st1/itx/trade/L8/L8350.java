package com.st1.itx.trade.L8;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.domain.TbJcicMu01Id;
/*DB服務*/
import com.st1.itx.db.service.TbJcicMu01Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8350")
@Scope("prototype")
/**
 * 查詢人員名冊報送
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8350 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public TbJcicMu01Service sTbJcicMu01Service;

	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8350 ");
		this.totaVo.init(titaVo);
		String iFunCd = titaVo.getParam("FunCd");
		String iHeadOfficeCode = titaVo.getParam("HeadOfficeCode");
		String iBranchCode = titaVo.getParam("BranchCode");
		int iDataDate = Integer.valueOf(titaVo.getParam("DataDate"));
//		int iOldDataDate = Integer.valueOf(titaVo.getParam("OldDataDate"));
		String iEmpId = titaVo.getParam("EmpId");
		String iEmailAccount = titaVo.getParam("EmailAccount");
		String iTitle = titaVo.getParam("Title");
		String iQryUserId = titaVo.getParam("QryUserId");
		String iAuthQryType = titaVo.getParam("AuthQryType");
		String iAuthItemQuery = titaVo.getParam("AuthItemQuery");
		String iAuthItemReview = titaVo.getParam("AuthItemReview");
		String iAuthItemOther = titaVo.getParam("AuthItemOther");
		int iAuthStartDay = Integer.valueOf(titaVo.getParam("AuthStartDay"));
		int iAuthEndDay = Integer.valueOf(titaVo.getParam("AuthEndDay"));

		String iAuthMgrIdS = titaVo.getParam("AuthMgrIdS");
		String iAuthMgrIdE = titaVo.getParam("AuthMgrIdE");
//		int iOutJcicTxtDate = Integer.valueOf(titaVo.getParam("OutJcicTxtDate"));

		TbJcicMu01 iTbJcicMu01 = new TbJcicMu01();
		TbJcicMu01Id iTbJcicMu01Id = new TbJcicMu01Id();

		iTbJcicMu01Id.setHeadOfficeCode(iHeadOfficeCode);
		iTbJcicMu01Id.setBranchCode(iBranchCode);
//		if (iFunCd.equals("2")) {
//			iTbJcicMu01.setDataDate(iOldDataDate);
//		}else {
//			iTbJcicMu01.setDataDate(iDataDate);
//		}	
		iTbJcicMu01Id.setEmpId(iEmpId);
		iTbJcicMu01.setTbJcicMu01Id(iTbJcicMu01Id);
		switch (iFunCd) {
		case "1":
			iTbJcicMu01.setEmailAccount(iEmailAccount);
			iTbJcicMu01.setTitle(iTitle);
			iTbJcicMu01.setQryUserId(iQryUserId);
			iTbJcicMu01.setAuthQryType(iAuthQryType);
			iTbJcicMu01.setAuthItemQuery(iAuthItemQuery);
			iTbJcicMu01.setAuthItemReview(iAuthItemReview);
			iTbJcicMu01.setAuthItemOther(iAuthItemOther);
			iTbJcicMu01.setAuthStartDay(iAuthStartDay);
			iTbJcicMu01.setAuthEndDay(iAuthEndDay);
			iTbJcicMu01.setAuthMgrIdS(iAuthMgrIdS);
			iTbJcicMu01.setAuthMgrIdE(iAuthMgrIdE);
			
			TbJcicMu01 aTbJcicMu01 = sTbJcicMu01Service.findById(iTbJcicMu01Id, titaVo);
			this.info("前置=" + aTbJcicMu01);
			if (aTbJcicMu01 == null) {
				try {
					sTbJcicMu01Service.insert(iTbJcicMu01, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}
			}
			break;
		case "2":
			//修改進入時不會再刪除資料，並記錄到L6932，只顯示最新的那一筆
			this.info("iTbJcicMu01Id    = " + iTbJcicMu01Id);
			TbJcicMu01Id dTbJcicMu01Id = new TbJcicMu01Id();
			TbJcicMu01 nTbJcicMu01 = new TbJcicMu01();
			nTbJcicMu01 = sTbJcicMu01Service.holdById(iTbJcicMu01Id, titaVo);
			this.info("L8350資料2   = " + nTbJcicMu01);
			if(nTbJcicMu01 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			TbJcicMu01 oTbJcicMu01Id = (TbJcicMu01) iDataLog.clone(nTbJcicMu01);
			dTbJcicMu01Id.setHeadOfficeCode(iHeadOfficeCode);
			dTbJcicMu01Id.setBranchCode(iBranchCode);
			dTbJcicMu01Id.setEmpId(iEmpId);
			nTbJcicMu01.setDataDate(iDataDate);	
			nTbJcicMu01.setTbJcicMu01Id(dTbJcicMu01Id);
			nTbJcicMu01.setEmailAccount(iEmailAccount);
			nTbJcicMu01.setTitle(iTitle);
			nTbJcicMu01.setQryUserId(iQryUserId);
			nTbJcicMu01.setAuthQryType(iAuthQryType);
			nTbJcicMu01.setAuthItemQuery(iAuthItemQuery);
			nTbJcicMu01.setAuthItemReview(iAuthItemReview);
			nTbJcicMu01.setAuthItemOther(iAuthItemOther);
			nTbJcicMu01.setAuthStartDay(iAuthStartDay);
			nTbJcicMu01.setAuthEndDay(iAuthEndDay);
			nTbJcicMu01.setAuthMgrIdS(iAuthMgrIdS);
			nTbJcicMu01.setAuthMgrIdE(iAuthMgrIdE);
			nTbJcicMu01.setOutJcictxtDate(0);

			try {
				sTbJcicMu01Service.update(nTbJcicMu01, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}

			iDataLog.setEnv(titaVo, oTbJcicMu01Id, nTbJcicMu01);
			iDataLog.exec("L8350修改");
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}