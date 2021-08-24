package com.st1.itx.trade.L1;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R05")
@Scope("prototype")
public class L1R05 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustTelNoService sCustTelNoService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("L1R05 Started");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// tita取值
		String iTelNoUKey = titaVo.getParam("TelNoUKey");

		CustTelNo iCustTelNo = new CustTelNo();

		iCustTelNo = sCustTelNoService.findById(iTelNoUKey, titaVo);

		if (iCustTelNo == null) {
			throw new LogicException(titaVo, "E0001", "客戶電話主檔查無資料");
		} else {
			this.totaVo.putParam("L1r05TelTypeCode", iCustTelNo.getTelTypeCode());
			this.totaVo.putParam("L1r05TelArea", iCustTelNo.getTelArea());
			this.totaVo.putParam("L1r05TelNo", iCustTelNo.getTelNo());
			this.totaVo.putParam("L1r05TelExt", iCustTelNo.getTelExt());
			if (iCustTelNo.getTelTypeCode().equals("09")) {
				String iTelOther = "";
				iTelOther = iCustTelNo.getTelArea() + iCustTelNo.getTelNo() + iCustTelNo.getTelExt();
				this.totaVo.putParam("L1r05TelOther", iTelOther);
			} else {
				this.totaVo.putParam("L1r05TelOther", "");
			}
			this.totaVo.putParam("L1r05TelChgRsnCode", iCustTelNo.getTelChgRsnCode());
			this.totaVo.putParam("L1r05RelationCode", iCustTelNo.getRelationCode());
			this.totaVo.putParam("L1r05LiaisonName", iCustTelNo.getLiaisonName());
			this.totaVo.putParam("L1r05Rmk", iCustTelNo.getRmk());
			this.totaVo.putParam("L1r05StopReason", iCustTelNo.getStopReason());
			this.totaVo.putParam("L1r05Enable", iCustTelNo.getEnable());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}