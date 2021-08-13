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
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L5908ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5908")
@Scope("prototype")
/**
 * 房貸專員撥款筆數統計表 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5908 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5908.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public CdEmpService iCdEmpService;
	
	@Autowired
	public CdBcmService iCdBcmService;
	
	@Autowired
	public L5908ServiceImpl iL5908ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5908 ");
		this.totaVo.init(titaVo);
		//處理tita日期為西元並加上日期
		String iYyyMmFm = titaVo.getParam("YyyMmFm");
		int xYyyMmFm = Integer.valueOf(iYyyMmFm)+191100;
		String iYyyMmTo = titaVo.getParam("YyyMmTo");
		int xYyyMmTo = Integer.valueOf(iYyyMmTo)+191100;
		List<Map<String, String>> iL5908SqlReturn = new ArrayList<Map<String,String>>();

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;
		
		try {
			iL5908SqlReturn = iL5908ServiceImpl.FindData(this.index,this.limit,xYyyMmFm,xYyyMmTo,titaVo);
		}catch (Exception e) {
			//E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		if(iL5908SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001",iYyyMmFm+"到"+iYyyMmTo+"期間內查無資料");
		}else {
			
			if(iL5908SqlReturn!=null && iL5908SqlReturn.size()>=this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				//this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			
			for (Map<String, String> r5908SqlReturn:iL5908SqlReturn) {
				OccursList occursList = new OccursList();
//				if (r5908SqlReturn.get("F1").equals("")) {
//					continue;
//				}
				
				occursList.putParam("OOPerfDate",Integer.valueOf(r5908SqlReturn.get("F0"))-191100);
				occursList.putParam("OODeptCode",r5908SqlReturn.get("F2"));
				occursList.putParam("OODeptCodeX",r5908SqlReturn.get("F3"));
				occursList.putParam("OOBsOfficer",r5908SqlReturn.get("F1"));
				occursList.putParam("OOBsOfficerX",r5908SqlReturn.get("F4"));
				occursList.putParam("OOTotal",r5908SqlReturn.get("F5"));
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);{
		return this.sendList();
		}
	}
}

