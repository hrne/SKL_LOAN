package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R23")
@Scope("prototype")
/**
 * 電催抓客戶電話
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R23 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustTelNoService sCustTelNoService;

	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.totaVo.init(titaVo);

		String iRimCustUKey = titaVo.getParam("RimCustUKey");

		Slice<CustTelNo> iCustTelNo = null;

		CustMain iCustMain = null;
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

//		iCustTelNo = sCustTelNoService.findCustUKey(iRimCustUKey, this.index, this.limit, titaVo);
		iCustTelNo = sCustTelNoService.findAll(0, Integer.MAX_VALUE, titaVo);
		iCustMain = iCustMainService.findById(iRimCustUKey, titaVo);
		String iTelArea = "";
		String iTelNo = "";
		String iTelExt = "";
		String fTel = "";
		if (iCustTelNo != null) {
			for (CustTelNo sCustTelNo : iCustTelNo) {
				if (sCustTelNo.getCustUKey().equals(iRimCustUKey) && sCustTelNo.getTelTypeCode().equals("06")) {
					// if (sCustTelNo.getLiaisonName().equals("")) {
					// //本人不用打
					// totaVo.putParam("L5R23LiaisonName", "");
					// }else {
					// totaVo.putParam("L5R23LiaisonName", sCustTelNo.getLiaisonName());
					// }
					totaVo.putParam("L5R23LiaisonName", iCustMain.getCustName());
					iTelArea = sCustTelNo.getTelArea();
					iTelNo = sCustTelNo.getTelNo();
					iTelExt = sCustTelNo.getTelExt();
					fTel = iTelArea + iTelNo + iTelExt;
					totaVo.putParam("L5R23TelPhone", fTel);
					totaVo.putParam("L5R23CustId", iCustMain.getCustId());
				} else {
					totaVo.putParam("L5R23LiaisonName", "");
					totaVo.putParam("L5R23TelPhone", "");
					totaVo.putParam("L5R23CustId", iCustMain.getCustId());
				}
			}
		} else {
			totaVo.putParam("L5R23LiaisonName", "");
			totaVo.putParam("L5R23TelPhone", "");
			totaVo.putParam("L5R23CustId", iCustMain.getCustId());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}