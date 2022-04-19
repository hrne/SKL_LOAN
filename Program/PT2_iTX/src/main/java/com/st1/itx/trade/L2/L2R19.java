package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R19")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R19 extends TradeBuffer {

	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;
	@Autowired
	public CustMainService sCustMainService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R19 ");
		this.totaVo.init(titaVo);

		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));

		String s = "";

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		List<ClBuildingOwner> lClBuildingOwner = new ArrayList<ClBuildingOwner>();

		Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);

		lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();

		if (lClBuildingOwner != null) {
			for (ClBuildingOwner o : lClBuildingOwner) {
				if (!"".equals(s)) {
					s += ";";
				}
				CustMain custMain = sCustMainService.findById(o.getOwnerCustUKey(), titaVo);
				if (custMain != null) {
					s += custMain.getCustId().trim() + ":" + custMain.getCustName().trim();
				}
			}
		}
		this.totaVo.putParam("L2r19PublicBdOwnerId", s);

		this.addList(this.totaVo);
		return this.sendList();
	}
}