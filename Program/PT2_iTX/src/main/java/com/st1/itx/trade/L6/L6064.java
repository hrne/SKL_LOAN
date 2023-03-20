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
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L6064ServiceImpl;

/**
 * Tita<br>
 * DefNo=9,4<br>
 * DefType=9,2<br>
 * Code=X,10<br>
 * END=X,1<br>
 */

@Service("L6064") // 各類代碼檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6064 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public L6064ServiceImpl l6064ServiceImpls;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6064 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iDefType = titaVo.getParam("DefType");
		String iDefCode = titaVo.getParam("DefCode");
		String iCode = titaVo.getParam("Code");
		String iCodeItem = titaVo.getParam("CodeItem");
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit =Integer.MAX_VALUE ; // 217 * 200 = 43400
		List<Map<String, String>> L6064DateList = null;
		try {
			L6064DateList = l6064ServiceImpls.findAll(iDefType,iDefCode,iCode,iCodeItem,this.index,this.limit,titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		int idx = 0;
		int cnt = 0;
		if(L6064DateList != null ) {
			for (Map<String, String> t : L6064DateList) {
				idx++;
				if (this.index > 0 && this.index > idx) {
					continue;
				}
				cnt++;
				if (cnt > this.limit) {
					break;
				}
				OccursList occursList = new OccursList();
				occursList.putParam("OODefCode",t.get("DefCode"));
				occursList.putParam("OOCode",t.get("Code"));
				occursList.putParam("OOItem",t.get("Item"));
				occursList.putParam("OOType",t.get("DefType"));
				occursList.putParam("OOEnable",t.get("Enable"));
				occursList.putParam("OOLastUpdate",t.get("LastUpdate"));
				occursList.putParam("OOLastEmp",t.get("LastUpdateEmpNo")+" "+t.get("Fullname"));
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}
		this.info("L6064DateList.size = " + L6064DateList.size() + "/" + idx);
		
		if (L6064DateList != null && idx < L6064DateList.size()) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(idx);
			if ("".equals(titaVo.getParam("TlrNo")) && "".equals(titaVo.getParam("TranNo"))) {
				this.totaVo.setMsgEndToEnter();// 手動折返
			} else {
				this.totaVo.setMsgEndToAuto();// 自動折返
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}