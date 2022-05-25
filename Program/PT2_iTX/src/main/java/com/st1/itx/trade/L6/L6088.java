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
import com.st1.itx.db.service.springjpa.cm.L6088ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita EmployeeNo=X,6 AgCurInd=X,1 END=X,1
 */
@Service("L6088") // 員工資料檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6088 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L6088ServiceImpl sL6088ServiceImpl;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		
		this.info("active L6088 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();
		
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 20; // 232 * 40 = 9280

		List<Map<String, String>> lL6088 = null;
		lL6088 = sL6088ServiceImpl.findAll(this.index, this.limit, titaVo);
		
		if(lL6088 == null || lL6088.isEmpty())
			throw new LogicException(titaVo, "E0001", "員工資料檔");

		// 查詢員工資料檔檔
		this.info("lL6088 ==== " + lL6088);
				
		for(Map<String, String> m : lL6088) {
			
			String agLevel;
			OccursList occursList = new OccursList();
			occursList.putParam("OOEmployeeNo", m.get("EmployeeNo"));
			occursList.putParam("OOAgentId", m.get("AgentId"));
			occursList.putParam("OOFullname", m.get("Fullname"));
			
			// 檢查上層長官職等是否符合
			agLevel = m.get("FirstSuperiorAgLevel");
			if(!agLevel.trim().isEmpty() && agLevel.toUpperCase().startsWith("K"))
				occursList.putParam("OOChief", m.get("FirstSuperiorEmpNo") + " " + m.get("FirstSuperiorName"));
			else
				occursList.putParam("OOChief", "");
			
			// 檢查上上層長官職等是否符合
			agLevel = m.get("SecondSuperiorAgLevel");
			if(!agLevel.trim().isEmpty() && agLevel.toUpperCase().startsWith("H"))
				occursList.putParam("OODirector", m.get("SecondSuperiorEmpNo") + " " + m.get("SecondSuperiorName"));
			else
				occursList.putParam("OODirector", "");
			
			// 檢查上上上層長官職等是否符合
			agLevel = m.get("ThirdSuperiorAgLevel");
			if(!agLevel.trim().isEmpty() && agLevel.toUpperCase().startsWith("E"))
				occursList.putParam("OOManager", m.get("ThirdSuperiorEmpNo") + " " + m.get("ThirdSuperiorName"));
			else
				occursList.putParam("OOManager", "");
			
			occursList.putParam("OOCenterCode", m.get("UnitCode"));
			occursList.putParam("OOCenterCodeName", m.get("UnitItem"));
			occursList.putParam("OOCenterCode1", m.get("DistCode"));
			occursList.putParam("OOCenterCode1Name", m.get("DistItem"));
			occursList.putParam("OOCenterCode2", m.get("DeptCode"));
			occursList.putParam("OOCenterCode2Name", m.get("DeptItem"));
			occursList.putParam("OOAgStatus", m.get("AgStatus"));
			occursList.putParam("OOAgCurInd", m.get("AgCurInd"));
			occursList.putParam("OODataDate", this.parse.stringToStringDate(m.get("LastUpdate")));
			
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (lL6088 != null && lL6088.size() >= this.limit) {
			
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter(); // 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}