package com.st1.itx.trade.L6;

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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.L6069ServiceImpl;

@Service("L6069")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L6069 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;
	
	
	@Autowired
	private L6069ServiceImpl l6069ServiceImpl;

	@Autowired
	Parse parse;

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6069 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 20; // 64 * 200 = 12,800

		String iDefType = titaVo.getParam("DefType");
		String iCode = titaVo.getParam("Code");
		String iItem = titaVo.getParam("Item");
		this.info("iDefType     = " + iDefType);  //09
		this.info("iCode        = " + iCode);    // DateType
		this.info("iItem        = " + iItem);   // 日期類別

		if(iDefType==null) {
			this.info("這是空值");
		}else if(iDefType=="0"){
			this.info("這是文字0");
		}else if(iDefType.length() == 0) {
			this.info("傳入文字長度為0");
		}
		
				
		List<Map<String, String>> L6069List = null;
		
		try {
			L6069List = l6069ServiceImpl.FindAll(titaVo, this.index, this.limit);
		}catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		if (L6069List == null || L6069List.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");
		}

		
		for(Map<String, String> c : L6069List) {
			OccursList occursList = new OccursList();

			occursList.putParam("OOCustNo", c.get("ACODE")); //代號
			
			occursList.putParam("OOCode",c.get("ADEFCODE"));//代碼檔代號
			
			occursList.putParam("OOItem",c.get("AITEM"));//代碼類別說明
			
			occursList.putParam("OODefItem",c.get("BITEM"));//代碼檔類別名稱
			
			occursList.putParam("OOType",c.get("ADEFTYPE"));//業務類別
			
			occursList.putParam("OOTypeX",c.get("BCODE"));//業務類別說明
			
			this.totaVo.addOccursList(occursList);
		}
		
		if (L6069List != null && L6069List.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}