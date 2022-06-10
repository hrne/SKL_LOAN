package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5915ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5915")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5915 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	private L5915ServiceImpl l5915ServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5915 ");
		this.totaVo.init(titaVo);

		List<Map<String, String>> dList = null;

		try {
		    dList = l5915ServiceImpl.FindData(titaVo);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5915 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		
		if (dList == null || dList.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");
		}
		
		for (Map<String, String> d : dList) {
			OccursList occursList = new OccursList();
			
 			occursList.putParam("OEmpNo", d.get("Coorgnizer"));
 			occursList.putParam("OEmpName", d.get("Fullname"));
 			occursList.putParam("OCustNo", d.get("CustNo"));
 			occursList.putParam("OFacmNo", d.get("FacmNO"));
 			occursList.putParam("OBormNo", d.get("BormNo"));
 			occursList.putParam("OAmt", d.get("DrawdownAmt"));
 			
 			BigDecimal bonus = new BigDecimal(d.get("CoorgnizerBonus"));
 			
 			int cnt = 0;
 			if (bonus.compareTo(BigDecimal.ZERO) != 0) {
 				cnt = 1;
 			}
 			occursList.putParam("OCnt", cnt);
			
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
			
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}