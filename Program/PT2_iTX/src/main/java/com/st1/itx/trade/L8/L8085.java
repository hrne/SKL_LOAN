package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.springjpa.cm.L6064ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

/**
 * Tita FuncCode=9,1 BusinessType=X,2 JcicEmpName=x,8 JcicEmpTel=x,16 END=X,1
 */

@Service("L8085")
@Scope("prototype")
/**
 *
 *
 * @author St1
 * @version 1.0.0
 */
public class L8085 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public SystemParasService sSystemParasService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	public L6064ServiceImpl l6064ServiceImpls;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8085 ");
		this.totaVo.init(titaVo);
		// 取得輸入資料
		String iDefType = "08";
		String iDefCode = "HMLCode";
		String iCode = titaVo.getParam("AmlHMLFg");
		String iCodeItem = titaVo.getParam("AmlEmp");
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();;

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 217 * 200 = 43400
		List<Map<String, String>> L6064DateList = null;
		try {
			L6064DateList = l6064ServiceImpls.findAll(iDefType, iDefCode, iCode, iCodeItem, this.index, this.limit,
					titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}	
		int idx = 0;
		int cnt = 0;
		if (L6064DateList != null) {
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
				occursList.putParam("OODDefItem", t.get("CI"));
				occursList.putParam("OODefCode", t.get("DefCode"));
				String ixCode = t.get("Code").substring(0, 1);
				if(ixCode.equals("L")) {
					occursList.putParam("OOCode", "低風險");					
				}
				if(ixCode.equals("M")) {
					occursList.putParam("OOCode", "中風險");					
				}
				if(ixCode.equals("H")) {
					occursList.putParam("OOCode", "高風險");					
				}
		
				occursList.putParam("OOItem", t.get("Item"));
				occursList.putParam("OOType", t.get("DefType"));
				occursList.putParam("OOEnable", t.get("Enable"));
				occursList.putParam("OOLastUpdate", t.get("LastUpdate"));
				occursList.putParam("OOLastEmp", t.get("LastUpdateEmpNo") + " " + t.get("Fullname"));
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

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
