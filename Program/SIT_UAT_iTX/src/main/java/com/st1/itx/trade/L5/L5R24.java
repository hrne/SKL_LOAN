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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5R24ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5R24")
@Scope("prototype")
/**
 * 房貸專員績效津貼計算
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R24 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5R24.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public L5R24ServiceImpl iL5R24ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R24 ");
		this.totaVo.init(titaVo);
		String iCustUKey = titaVo.getParam("RimCustUKey");
		String iRimAddressCode = titaVo.getParam("RimAddressCode");
		String iClNo = titaVo.getParam("RimClNo");
		String iRimClCode1 = titaVo.getParam("RimClCode1");
		String iRimClCode2 = titaVo.getParam("RimClCode2");
		String reAddress = ""; //回傳地址
		List<Map<String, String>> i5R24SqlReturn = new ArrayList<Map<String,String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		try {
			i5R24SqlReturn = iL5R24ServiceImpl.FindData(iRimAddressCode,iCustUKey,iClNo,iRimClCode1,iRimClCode2,titaVo);
		}catch (Exception e) {
			//E5004 讀取DB語法發生問題
			this.info("L5R24 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		this.info("db return = "+i5R24SqlReturn.toString());
		if(i5R24SqlReturn.size() == 0) {
			totaVo.putParam("L5R24FullAddress", "");
		}else {
			for (Map<String, String> r5R24SqlReturn:i5R24SqlReturn) {
				for (int i = 0 ; i < r5R24SqlReturn.size();i++) {
					reAddress += r5R24SqlReturn.get("F"+i);
				}
				totaVo.putParam("L5R24FullAddress", reAddress);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}