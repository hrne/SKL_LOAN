package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.domain.TbJcicMu01Id;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TbJcicMu01Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R56")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R56 extends TradeBuffer {

	@Autowired
	public TbJcicMu01Service iTbJcicMu01Service;
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R56 ");
		this.totaVo.init(titaVo);
		String iRimHeadOfficeCode = titaVo.getParam("RimHeadOfficeCode");
		String iRimBranchCode = titaVo.getParam("RimBranchCode");
	//	int iRimDataDate = Integer.valueOf(titaVo.getParam("RimDataDate")) + 19110000;
		String iRimEmpId = titaVo.getParam("RimEmpId");

		
		TbJcicMu01 iTbJcicMu01 = new TbJcicMu01();
		TbJcicMu01Id iTbJcicMu01Id = new TbJcicMu01Id();
		iTbJcicMu01Id.setBranchCode(iRimBranchCode);
	//	iTbJcicMu01Id.setDataDate(iRimDataDate);
		iTbJcicMu01Id.setEmpId(iRimEmpId);
		iTbJcicMu01Id.setHeadOfficeCode(iRimHeadOfficeCode);
		iTbJcicMu01 = iTbJcicMu01Service.findById(iTbJcicMu01Id, titaVo);
		if (iTbJcicMu01 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 主檔無資料錯誤訊息
		}

		CdEmp iCdEmp = new CdEmp();
		iCdEmp = iCdEmpService.findById(iRimEmpId, titaVo);
		if (iCdEmp == null) {
			throw new LogicException(titaVo, "E0001", "員工檔");
		} else {
			this.totaVo.putParam("L8R56EmpName", iCdEmp.getFullname());
		}
		this.totaVo.putParam("L8R56Title", iTbJcicMu01.getTitle());
		this.totaVo.putParam("L8R56AuthQryType", iTbJcicMu01.getAuthQryType());
		this.totaVo.putParam("L8R56QryUserId", iTbJcicMu01.getQryUserId());
		this.totaVo.putParam("L8R56AuthItemQuery", iTbJcicMu01.getAuthItemQuery());
		this.totaVo.putParam("L8R56AuthItemReview", iTbJcicMu01.getAuthItemReview());
		this.totaVo.putParam("L8R56AuthItemOther", iTbJcicMu01.getAuthItemOther());
		this.totaVo.putParam("L8R56AuthStartDay", iTbJcicMu01.getAuthStartDay());
		//日期傳送var有問題之後要改
		this.totaVo.putParam("L8R56DataDate", iTbJcicMu01.getDataDate());

		String iAuthMgrIdS = iTbJcicMu01.getAuthMgrIdS();
		if (iAuthMgrIdS.trim().isEmpty()) {
			this.totaVo.putParam("L8R56AuthMgrIdS", "");
			this.totaVo.putParam("L8R56AuthMgrNameS", "");
		} else {
			this.totaVo.putParam("L8R56AuthMgrIdS", iAuthMgrIdS);
			iCdEmp = iCdEmpService.findById(iAuthMgrIdS, titaVo);
			if (iCdEmp == null) {
				throw new LogicException(titaVo, "E0001", "員工檔");
			} else {
				this.totaVo.putParam("L8R56AuthMgrNameS", iCdEmp.getFullname());
			}
		}
		this.totaVo.putParam("L8R56AuthEndDay", iTbJcicMu01.getAuthEndDay());

		String iAuthMgrIdE = iTbJcicMu01.getAuthMgrIdE();
		if (iAuthMgrIdE.trim().isEmpty()) {
			this.totaVo.putParam("L8R56AuthMgrIdE", "");
			this.totaVo.putParam("L8R56AuthMgrNameE", "");
		} else {
			this.totaVo.putParam("L8R56AuthMgrIdE", iAuthMgrIdE);
			iCdEmp = iCdEmpService.findById(iAuthMgrIdE, titaVo);
			if (iCdEmp == null) {
				throw new LogicException(titaVo, "E0001", "員工檔");
			} else {
				this.totaVo.putParam("L8R56AuthMgrNameE", iCdEmp.getFullname());
			}
		}
		this.totaVo.putParam("L8R56EmailAccount", iTbJcicMu01.getEmailAccount());
		this.totaVo.putParam("L8R56OutJcictxtDate", iTbJcicMu01.getOutJcictxtDate());
		this.addList(this.totaVo);
		return this.sendList();
	}
}