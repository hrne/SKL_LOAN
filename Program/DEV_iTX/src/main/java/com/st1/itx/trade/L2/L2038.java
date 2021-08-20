package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L2038ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * END=X,1<br>
 */

@Service("L2038")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2038 extends TradeBuffer {
	@Autowired
	public L2038ServiceImpl sL2038ServiceImpl;

	@Autowired
	public ClBuildingService sClBuildingService;
	
	@Autowired
	public ClLandService sClLandService;
	
	@Autowired
	public ClStockService sClStockService;
	
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2038 ");
		this.totaVo.init(titaVo);

		/*
		 * *** 折返控制相關 *** 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		// *** 折返控制相關 ***
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 10; // 129 * 400 = 51600

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = sL2038ServiceImpl.findByCondition(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2038ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L2038");

		}
		
		if (resultList != null && resultList.size() > 0) {
			
			int TempClCode1 = 0;
			int TempClCode2 = 0;
			int TempClNo = 0;
			
			for (Map<String, String> result : resultList) {
				this.info("L2038 result = " + result.toString());
				// new occurs
				OccursList occurslist = new OccursList();
				
			
					occurslist.putParam("OOApproveNo", result.get("F0"));
					occurslist.putParam("OOFacmNo", result.get("F1"));
					occurslist.putParam("OOCustId", result.get("F2"));				
					occurslist.putParam("OOCustNo", result.get("F3"));
					occurslist.putParam("OOClCode1", result.get("F4"));
					occurslist.putParam("OOClCode2", result.get("F5"));
					occurslist.putParam("OOClNo", result.get("F6"));
					
					TempClCode1 = parse.stringToInteger(result.get("F4"));
					TempClCode2 = parse.stringToInteger(result.get("F5"));
					TempClNo = parse.stringToInteger(result.get("F6"));
					
					occurslist.putParam("OONewNote", result.get("F7"));
					occurslist.putParam("OOClTypeCode", result.get("F8"));

					
					occurslist.putParam("OOOwnerId", "");
					occurslist.putParam("OOOwnerName", "");
					
					CustMain tCustMain = new CustMain();
					tCustMain = sCustMainService.findById(result.get("F9"), titaVo);				
					if(tCustMain != null) {						
						occurslist.putParam("OOOwnerId", tCustMain.getCustId());
						occurslist.putParam("OOOwnerName", tCustMain.getCustName());
					}
					
					occurslist.putParam("OOOwnerFlag", result.get("F10"));
					occurslist.putParam("OOSettingStat", result.get("F11"));
					occurslist.putParam("OOSettingAmt", result.get("F12"));
					occurslist.putParam("OOClStat", result.get("F13"));
					occurslist.putParam("OOShareTotal", result.get("F14"));
					occurslist.putParam("OOOther", " ");
					if(TempClCode1 == 1) {
						ClBuildingId clBuildingId = new ClBuildingId();
						clBuildingId.setClCode1(TempClCode1);
						clBuildingId.setClCode2(TempClCode2);
						clBuildingId.setClNo(TempClNo);
						ClBuilding tClBuilding = new ClBuilding();
						tClBuilding = sClBuildingService.findById(clBuildingId, titaVo);
						
						if(tClBuilding != null) {
							occurslist.putParam("OOOther", tClBuilding.getBdLocation());
						} 
						
					} else if( TempClCode1 == 2) {
						ClLandId clLandId = new ClLandId();
						clLandId.setClCode1(TempClCode1);
						clLandId.setClCode2(TempClCode2);
						clLandId.setClNo(TempClNo);
						ClLand tClLand = new ClLand();
						tClLand = sClLandService.findById(clLandId, titaVo);
						
						if(tClLand != null) {
							occurslist.putParam("OOOther", tClLand.getLandLocation());
						} 
						
					} else if( TempClCode1 == 3 || TempClCode1 ==4) {
						ClStockId tClStockId = new ClStockId();
						tClStockId.setClCode1(TempClCode1);
						tClStockId.setClCode2(TempClCode2);
						tClStockId.setClNo(TempClNo);
						ClStock tClStock = new ClStock();
						tClStock = sClStockService.findById(tClStockId, titaVo);
						
						if(tClStock != null) {
							occurslist.putParam("OOOther", tClStock.getStockCode());
						} 
					} else {
					  occurslist.putParam("OOOther", " ");
					}
					
					
					if( parse.stringToInteger(result.get("F1")) != 0) {
						occurslist.putParam("OOFlag", "Y");
					} else {
						occurslist.putParam("OOFlag", " ");
					}	
					
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occurslist);
					

				}
			}


		List<LinkedHashMap<String, String>> chkOccursList = this.totaVo.getOccursList();

		if (chkOccursList == null || chkOccursList.size() == 0) {
			throw new LogicException("E2003", "擔保品主檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}