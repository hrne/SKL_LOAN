package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdEmpService;
/*DB服務*/
import com.st1.itx.db.service.TbJcicMu01Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8950")
@Scope("prototype")
/**
 * 聯徵產品查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8950 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public TbJcicMu01Service sTbJcicMu01Service;
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public CdBankService iCdBankService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8349 ");
		this.totaVo.init(titaVo);

		String iEmpId = titaVo.getParam("EmpId");
		int iDataDate = Integer.valueOf(titaVo.getParam("DataDate"))+19110000;
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TbJcicMu01> iTbJcicMu01 = null;
		if (iEmpId.equals("") && iDataDate == 19110000) {
			iTbJcicMu01 = sTbJcicMu01Service.findAll(this.index, this.limit, titaVo);
		} else if (!iEmpId.equals("") && iDataDate == 19110000) {
			iTbJcicMu01 = sTbJcicMu01Service.empIdEq(iEmpId, this.index, this.limit, titaVo);
		} else if (iEmpId.equals("") && iDataDate != 19110000) {
			iTbJcicMu01 = sTbJcicMu01Service.dataDateEq(iDataDate, this.index, this.limit, titaVo);
		} else {
			iTbJcicMu01 = sTbJcicMu01Service.empIdRcEq(iEmpId, iDataDate, this.index, this.limit, titaVo);
		}
		if (iTbJcicMu01 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 主檔無資料錯誤訊息
		} else {
			for (TbJcicMu01 aTbJcicMu01 : iTbJcicMu01) {
				OccursList occursList = new OccursList();
				Slice<CdBank> iCdBank = null;
				occursList.putParam("OOHeadOfficeCode", aTbJcicMu01.getHeadOfficeCode());
				iCdBank = iCdBankService.bankCodeLike(aTbJcicMu01.getHeadOfficeCode(), 0, Integer.MAX_VALUE, titaVo);
				if (iCdBank == null) {
					occursList.putParam("OOHeadOfficeCodeX", "");
				}else {
					occursList.putParam("OOHeadOfficeCodeX", iCdBank.getContent().get(0).getBankItem());
				}
				occursList.putParam("OOBranchCode", aTbJcicMu01.getBranchCode());
				iCdBank = iCdBankService.branchCodeLike(aTbJcicMu01.getHeadOfficeCode(),aTbJcicMu01.getBranchCode(), 0, Integer.MAX_VALUE, titaVo);
				if (iCdBank == null) {
					occursList.putParam("OOBranchCodeX", "");
				}else {
					occursList.putParam("OOBranchCodeX", iCdBank.getContent().get(0).getBranchItem());
				}
				occursList.putParam("OODataDate", Integer.valueOf(aTbJcicMu01.getDataDate()));
				occursList.putParam("OOEmpId", aTbJcicMu01.getEmpId());
				CdEmp iCdEmp = new CdEmp();
				iCdEmp = iCdEmpService.findById(aTbJcicMu01.getEmpId(), titaVo);
				if (iCdEmp == null) {
					occursList.putParam("OOEmpName", "");
				} else {
					occursList.putParam("OOEmpName", iCdEmp.getFullname());
				}
				occursList.putParam("OOJcicDate", aTbJcicMu01.getOutJcictxtDate());
				String tU = "";
				if (aTbJcicMu01.getLastUpdate().toString().trim().isEmpty()) {
					tU = aTbJcicMu01.getCreateDate().toString();
				}else {
					tU = aTbJcicMu01.getLastUpdate().toString();
				}
				String uDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(tU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
				uDate = uDate.substring(0, 3) + "/" + uDate.substring(3, 5) + "/" + uDate.substring(5);
				String uTime = tU.substring(11, 19);
				occursList.putParam("OOLastUpdate", uDate + " " + uTime);
				occursList.putParam("OOLastUpdateEmpNo", aTbJcicMu01.getLastUpdateEmpNo());
				if (!aTbJcicMu01.getLastUpdateEmpNo().trim().isEmpty()) {
					CdEmp aCdEmp = iCdEmpService.findById(aTbJcicMu01.getLastUpdateEmpNo(), titaVo);
					if (aCdEmp == null) {
						occursList.putParam("OOLastUpdateEmpName", "");
					}else {
						occursList.putParam("OOLastUpdateEmpName", aCdEmp.getFullname());
					}
				}else {
					occursList.putParam("OOLastUpdateEmpName", "");
				}
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}