package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5022ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Component("L5022")
@Scope("prototype")

/**
 * 放款專員業績統計作業－協辦人員等級明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5022 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5022.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public L5022ServiceImpl iL5022ServiceImpl;
	
	//輸入的員工編號必須存在於員工檔

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iEmpNo = titaVo.getParam("EmpNo");
		int cDate = Integer.valueOf(titaVo.getEntDy())+19110000;
		this.info("cDate="+cDate);
		List<Map<String, String>> iL5022SqlReturn = new ArrayList<Map<String,String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;
		
		if (iEmpNo.equals("")) {
			//user無輸入則查PfCoOfficer全部，且只顯示最近的一筆
			try {
				iL5022SqlReturn = iL5022ServiceImpl.officerNoBlank(cDate,this.index,this.limit,titaVo);
			}catch (Exception e) {
				//E5004 讀取DB語法發生問題
				this.info("L5024 ErrorForSql="+e);
				throw new LogicException(titaVo, "E5004","");
			}
		}else {
			//user有輸入則只顯示該員工在PfCoOfficer裡全部資料，由近到遠
			try {
				iL5022SqlReturn = iL5022ServiceImpl.officerNo(iEmpNo,titaVo);
			}catch (Exception e) {
				//E5004 讀取DB語法發生問題
				this.info("L5024 ErrorForSql="+e);
				throw new LogicException(titaVo, "E5004","");
			}
		}
		
		if(iL5022SqlReturn.size()==0) {
			throw new LogicException(titaVo, "E0001","協辦人員等級檔查無 "+iEmpNo+" 資料");
		}
		
		if(iL5022SqlReturn!=null && iL5022SqlReturn.size()>=this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			//this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		int returnFg = 0 ;
		for (Map<String, String> r5022SqlReturn:iL5022SqlReturn) {
			
			OccursList occursList = new OccursList();
			occursList.putParam("OOEmpNo", r5022SqlReturn.get("F0"));
			if(r5022SqlReturn.get("F1").equals("")||r5022SqlReturn.get("F1").equals("0")) {
				occursList.putParam("OOEffectiveDate", "");
			}else {
				occursList.putParam("OOEffectiveDate", Integer.valueOf(r5022SqlReturn.get("F1"))-19110000);
			}
			occursList.putParam("OOEmpClass", r5022SqlReturn.get("F2"));
			occursList.putParam("OOClassPass", r5022SqlReturn.get("F3"));
			occursList.putParam("OOFullname", r5022SqlReturn.get("F4"));
			occursList.putParam("OOUnitCode", r5022SqlReturn.get("F6"));
			occursList.putParam("OODistCode", r5022SqlReturn.get("F7"));
			occursList.putParam("OODeptCode", r5022SqlReturn.get("F9"));
			occursList.putParam("OOUnitCodeX", r5022SqlReturn.get("F5"));
			occursList.putParam("OODistCodeX", r5022SqlReturn.get("F8"));
			occursList.putParam("OODeptCodeX", r5022SqlReturn.get("F10"));
			if(r5022SqlReturn.get("F11").equals("")||r5022SqlReturn.get("F11").equals("0")) {
				occursList.putParam("OOIneffectiveDate", "");
			}else {
				occursList.putParam("OOIneffectiveDate", Integer.valueOf(r5022SqlReturn.get("F11"))-19110000);
			}
			occursList.putParam("OOReturnFg", returnFg);
			// 0:已生效 1:已停效 2:未生效
			int a = Integer.valueOf(cDate); //會計日
			int b = Integer.valueOf(r5022SqlReturn.get("F1")); //生效日
			int c = Integer.valueOf(r5022SqlReturn.get("F11")); //停效日
//			if(DateC < b) {
//				occursList.putParam("OOStatusFg", 0);
//			}else {
//				if(Integer.valueOf(r5022SqlReturn.get("F11")) == 0 && DateC > a) {
//					occursList.putParam("OOStatusFg", 0);
//				}else {
//					if(DateC < a) {
//						occursList.putParam("OOStatusFg", 2);	
//					}else {
//						occursList.putParam("OOStatusFg", 1);
//					}
//				}
//			}
			this.info("TEST==1"+a);
			this.info("TEST==2"+b);
			this.info("TEST==3"+c);
			if (c == 0) {
				if (a<b) {
					occursList.putParam("OOStatusFg", 2);
				}else {
					occursList.putParam("OOStatusFg", 0);
				}
			}else {
				if (a<b) {
					occursList.putParam("OOStatusFg", 2);
				}else {
					if (a>c) {
						occursList.putParam("OOStatusFg", 1);
					}else {
						occursList.putParam("OOStatusFg", 0);
					}
					
				}
			}
			this.totaVo.addOccursList(occursList);
			if (!iEmpNo.equals("")) {
				returnFg ++ ;
			  }
	     	}
			
		this.addList(this.totaVo);
		return this.sendList();
	}
}
