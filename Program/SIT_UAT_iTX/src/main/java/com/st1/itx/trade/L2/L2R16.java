package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacRelation;
import com.st1.itx.db.domain.FacRelationId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacRelationService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R16")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R16 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	
	@Autowired
	public FacRelationService sFacRelationService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R16 ");
		this.totaVo.init(titaVo);

		// tita取值
		// 統編
		int iCaseNo = parse.stringToInteger(titaVo.getParam("RimCaseNo"));
		String iCustId = titaVo.getParam("RimCustId");

		CustMain lCustMain = new CustMain();
		lCustMain  = sCustMainService.custIdFirst(iCustId, titaVo);

		if( lCustMain == null ) {
			throw new LogicException("E0001", "客戶資料主檔");
		} 
			
		String Ukey = lCustMain.getCustUKey();
		
		// new table PK
		FacRelationId tfacRelationId = new FacRelationId();
		
		tfacRelationId.setCreditSysNo(iCaseNo);		
		tfacRelationId.setCustUKey(Ukey);
		
		FacRelation tFacRelation = new FacRelation();
		tFacRelation = sFacRelationService.holdById(tfacRelationId);
		
		if( tFacRelation == null ) {
			throw new LogicException("E0001", "交易關係人檔");
		} 
		
		this.totaVo.putParam("L2r16CustName", lCustMain.getCustName());
		this.totaVo.putParam("L2r16DataStatus", lCustMain.getDataStatus());
		this.totaVo.putParam("L2r16FacRelationCode", tFacRelation.getFacRelationCode());

		this.addList(this.totaVo);
		return this.sendList();

	}
}