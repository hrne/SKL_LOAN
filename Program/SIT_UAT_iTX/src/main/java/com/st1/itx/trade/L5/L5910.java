package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5910ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5910")
@Scope("prototype")
/**
 * 撥款件貸款成數統計資料產生  
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5910 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5910.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public L5910ServiceImpl iL5910ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;
		
		this.info("active L5910 ");
		this.totaVo.init(titaVo);
		String iDateFm = titaVo.getParam("DateFm");
		String iDateTo = titaVo.getParam("DateTo");
		int xDateFm = Integer.valueOf(iDateFm)+19110000;
		int xDateTo = Integer.valueOf(iDateTo)+19110000;
//		工作月 pfbsdetail.workmonth
//		撥款日期  loanbormain.drawdowndate
//		戶號 pfbsdetail.custno
//		額度 pfbsdetail.facmno
//		撥款序號 pfbsdetail.bormno
//		撥款金額 loanbormain.drwadownamt
//		放款餘額 loanbormain.loanbal
//		現有利率 loanbormain.StoreRate
//		下一利率 loanbormain.ApproveRate
//		核准利率 loanbormain.ApproveRate
//		基本利率代碼 facmain.prodno
//		利率名稱(商品代碼) facprod.prodname
//		利率加減碼 facmain.rateincr
//		寄件代碼 facmain.piececode
//		地區別 custmain.RegCityCode
//		地區別名稱 CdCity.CityItem
//		放款專員 facmain.BusinessOfficer
//		專員姓名 cdemp.fullname
		
		List<Map<String, String>> iL5910SqlReturn = new ArrayList<Map<String,String>>();
		try {
			iL5910SqlReturn = iL5910ServiceImpl.FindData(this.index,this.limit,xDateFm,xDateTo,titaVo);
		}catch (Exception e) {
			//E5004 讀取DB語法發生問題
			this.info("L5910 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		if(iL5910SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001",iDateFm+"到"+iDateTo+"期間內查無資料");
		}else {
			
			if(iL5910SqlReturn!=null && iL5910SqlReturn.size()>=this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				//this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			
			
			for (Map<String, String> r5910SqlReturn:iL5910SqlReturn) {
				OccursList occursList = new OccursList();
				if(r5910SqlReturn.get("F0").equals("0") || r5910SqlReturn.get("F0").equals("")) {
					occursList.putParam("OOWorkMonth", "");
				}else {
					occursList.putParam("OOWorkMonth", Integer.valueOf(r5910SqlReturn.get("F0"))-191100);
				}	
				if(r5910SqlReturn.get("F1").equals("0") || r5910SqlReturn.get("F1").equals("")) {
					occursList.putParam("OODrawdownDate","");
				}else {
					occursList.putParam("OODrawdownDate", Integer.valueOf(r5910SqlReturn.get("F1"))-19110000);
				}
				occursList.putParam("OOCustNo", r5910SqlReturn.get("F2"));
				occursList.putParam("OOFacmNo", r5910SqlReturn.get("F3"));
				occursList.putParam("OOBormNo", r5910SqlReturn.get("F4"));
				occursList.putParam("OODrawdownAmt", r5910SqlReturn.get("F5"));
				occursList.putParam("OOLoanBal", r5910SqlReturn.get("F6"));
				occursList.putParam("OOStoreRate", r5910SqlReturn.get("F7"));
				occursList.putParam("OOApproveRate", r5910SqlReturn.get("F8"));
				occursList.putParam("OOApproveRate1", r5910SqlReturn.get("F8"));
				occursList.putParam("OOProdNo", r5910SqlReturn.get("F9"));
				occursList.putParam("OOProdName", r5910SqlReturn.get("F10"));
				occursList.putParam("OORateIncr", r5910SqlReturn.get("F11"));
				occursList.putParam("OOPieceCode", r5910SqlReturn.get("F12"));
				occursList.putParam("OORegityCode", r5910SqlReturn.get("F13"));
				occursList.putParam("OOCityItem", r5910SqlReturn.get("F14"));
				occursList.putParam("OOBusinessOfficer", r5910SqlReturn.get("F15"));
				occursList.putParam("OOBusinessOfficerX", r5910SqlReturn.get("F16"));
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}