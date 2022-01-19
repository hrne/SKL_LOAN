package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimApplNo=9,7
 */
/**
 * L2R11 以核准編號尋找擔保品
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R11")
@Scope("prototype")
public class L2R11 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService clMainService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public ClBuildingService sClBuildingService;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R11 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));

		for (int i = 1; i <= 20; i++) {
			this.totaVo.putParam("L2r11ClKey" + i, "");
			this.totaVo.putParam("L2r11ApplNo" + i, 0); // 核准號碼
			this.totaVo.putParam("L2r11ClTypeCode" + i, ""); // 擔保品類別
			this.totaVo.putParam("L2r11MainFlag" + i, ""); // 主要擔保品記號
			this.totaVo.putParam("L2r11EvaAmt" + i, 0);
			this.totaVo.putParam("L2r11BdLocation" + i, ""); // 門牌坐落
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 20;

		Slice<ClFac> lClFac = clFacService.approveNoEq(iApplNo, this.index, this.limit, titaVo);
		if (!(lClFac == null || lClFac.isEmpty())) {
			int i = 1;
			for (ClFac cl : lClFac.getContent()) {
				ClMain tClMain = clMainService.findById(new ClMainId(cl.getClCode1(), cl.getClCode2(), cl.getClNo()), titaVo);
				if (tClMain != null) {
					this.totaVo.putParam("L2r11ClKey" + i, cl.getClCode1() + "-" + this.parse.IntegerToString(cl.getClCode2(), 2) + "-" + this.parse.IntegerToString(cl.getClNo(), 7));
					this.totaVo.putParam("L2r11ApplNo" + i, cl.getApproveNo()); // 核准號碼
					this.totaVo.putParam("L2r11ClTypeCode" + i,tClMain.getClTypeCode() ); // 擔保品類別
					this.totaVo.putParam("L2r11MainFlag" + i, cl.getMainFlag()); // 主要擔保品記號
					this.totaVo.putParam("L2r11EvaAmt" + i, tClMain.getEvaAmt()); // 鑑價總值
					
					ClBuildingId clBuildingId = new ClBuildingId();
					clBuildingId.setClCode1(cl.getClCode1());
					clBuildingId.setClCode2(cl.getClCode2());
					clBuildingId.setClNo(cl.getClNo());
					ClBuilding tClBuilding = new ClBuilding();
					tClBuilding = sClBuildingService.findById(clBuildingId, titaVo);

					if (tClBuilding != null) { // 建物門牌
					  this.totaVo.putParam("L2r11BdLocation" + i, tClBuilding.getBdLocation() + "，建號" + tClBuilding.getBdNo1() + "-" + tClBuilding.getBdNo2()); // 門牌坐落
					} else {
					  this.totaVo.putParam("L2r11BdLocation","");
					}
					
					i++;
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}