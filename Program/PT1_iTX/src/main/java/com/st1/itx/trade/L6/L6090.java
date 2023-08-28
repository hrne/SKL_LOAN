package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdRuleCode;
import com.st1.itx.db.domain.CdRuleCodeId;
import com.st1.itx.db.service.CdRuleCodeService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.menu.MenuBuilder;
import com.st1.itx.util.parse.Parse;

@Service("L6090")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L6090 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	CdRuleCodeService sCdRuleCodeService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public DataLog dataLog;

	@Autowired
	MenuBuilder menuBuilder;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6090 ");

		this.totaVo.init(titaVo);
		String iRuleCode = titaVo.getParam("RuleCode");
//		String iRuleCodeItem = titaVo.getParam("RuleCodeItem");
//		String iRmkItem = titaVo.getParam("RmkItem");
//		int iRuleStDate = parse.stringToInteger(titaVo.getParam("RuleStDate")) + 19110000;
//		String iRuleEdDate = titaVo.getParam("RuleEdDate");
//		String iEnableMark = titaVo.getParam("EnableMark");

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.info("日期    =" + titaVo.getCalDy() );
		
		int iCalDy = parse.stringToInteger(titaVo.getCalDy())+19110000;
		this.info("iCalDy   = " + iCalDy);
		Slice<CdRuleCode> sCdRuleCode;
		
		if (iRuleCode.equals("")) {
			sCdRuleCode = sCdRuleCodeService.findAll(this.index, this.limit, titaVo);
			List<CdRuleCode> lCdRuleCode = sCdRuleCode == null ? null : sCdRuleCode.getContent();
			if (sCdRuleCode == null) {
				throw new LogicException("E0005", "管制代碼檔");
			}
			for (CdRuleCode s : lCdRuleCode) {
				OccursList occursList = new OccursList();

//				1.管制生效日、管制取消日 皆可預先設定，功能判斷是否顯示該管制項目是用「是否啟用=Y」才顯示			
//				2.若案件建檔時，若該管制項目尚未生效，該管制項目仍需顯示	
				if ((s.getEnableMark().equals("Y")) || (s.getRuleStDate() >= iCalDy)) {

					occursList.putParam("OORuleCode", s.getRuleCode());
					occursList.putParam("OORuleCodeItem", s.getRuleCodeItem());
					occursList.putParam("OORmkItem", s.getRmkItem());
					occursList.putParam("OORuleStDate", s.getRuleStDate());
					occursList.putParam("OORuleEdDate", s.getRuleEdDate());
					occursList.putParam("OOEnableMark", s.getEnableMark());

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				}

			}
		} else { // 有代碼
			CdRuleCode lCdRuleCode = new CdRuleCode();
			lCdRuleCode = sCdRuleCodeService.findById(iRuleCode, titaVo);
			if (lCdRuleCode == null) {
				throw new LogicException("E0005", "管制代碼檔");
			} else {
				OccursList occursList = new OccursList();

				occursList.putParam("OORuleCode", lCdRuleCode.getRuleCode());
				occursList.putParam("OORuleCodeItem", lCdRuleCode.getRuleCodeItem());
				occursList.putParam("OORmkItem", lCdRuleCode.getRmkItem());
				occursList.putParam("OORuleStDate", lCdRuleCode.getRuleStDate());
				occursList.putParam("OORuleEdDate", lCdRuleCode.getRuleEdDate());
				occursList.putParam("OOEnableMark", lCdRuleCode.getEnableMark());
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
