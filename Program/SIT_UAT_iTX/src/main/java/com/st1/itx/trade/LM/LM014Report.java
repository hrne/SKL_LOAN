package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM014ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LM014ServiceImpl.DepartmentCodeCondition;
import com.st1.itx.db.service.springjpa.cm.LM014ServiceImpl.EntCodeCondition;
import com.st1.itx.db.service.springjpa.cm.LM014ServiceImpl.QueryType;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM014Report extends MakeReport {

	@Autowired
	LM014ServiceImpl lM014ServiceImpl;
	
	// 表別
	private enum ReportType
	{
		All("總表", EntCodeCondition.All, DepartmentCodeCondition.All),
		Enterprise("企金", EntCodeCondition.Enterprise, DepartmentCodeCondition.All),
		Natural("房貸", EntCodeCondition.Natural, DepartmentCodeCondition.All),
		IsDepartment("企金通路", EntCodeCondition.All, DepartmentCodeCondition.Yes),
		NotDepartment("非企金通路", EntCodeCondition.All, DepartmentCodeCondition.No);
		
		protected String value = "";
		protected EntCodeCondition entCond = null;
		protected DepartmentCodeCondition dptCond = null;
		
		ReportType(String value, EntCodeCondition entCond, DepartmentCodeCondition dptCond)
		{
			this.value = value;
			this.entCond = entCond;
			this.dptCond = dptCond;
		}
	}
	
	private class Query
	{
		public ReportType reportType = null;
		public QueryType queryType = null;
		
		Query(ReportType rptType, QueryType qType)
		{
			this.reportType = rptType;
			this.queryType = qType;
		}
	}

	private ReportType currentReportType = null;
	
	@Override
	public void printHeader() {

		// 設定字體大小
		this.setFontSize(8);

		this.print(-1, 146, "機密等級：密");

		this.print(-2, 3, "程式ＩＤ：" + this.getParentTranCode());
		this.print(-2, 86, "新光人壽保險股份有限公司", "C");
		this.print(-2, 146, "日　　期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));

		this.print(-3, 3, "報　　表：" + this.getRptCode());
		this.print(-3, 86, "平均利率月報表《" + currentReportType.value + "》", "C");
		this.print(-3, 146, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));

		this.print(-4, 146, "頁　　數：" + this.getNowPage());

		this.print(-5, 86, getshowRocDate(this.getReportDate()), "C");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);
	}

	public Boolean exec(TitaVo titaVo) throws LogicException {
		
		this.setCharSpaces(0);
		
		//					A	B	C	D
		// 0 全部			1	1	1	1 = 共 20 張
		// 1 科目別			1	0	0	0 = 共 5  張
		// 2 種類別			0	1	0	0 = 共 5  張
		// 3 關係人			0	0	1	0 = 共 5  張
		// 4 種類別+關係人	0	0	0	1 = 共 5  張
		
		// 					總表
		// EntCode Y 		企金
		//         N 		房貸
		// DepartmentCode Y 企金通路
		//                N 非企金通路
		
		this.info("LM014Report.exec");
		
		int inputType = Integer.valueOf(titaVo.getParam("inputType"));
		
		// 依據輸入種類, 製作需要產的表的 List
		
		ArrayList<Query> queryList = new ArrayList<Query>(inputType == 0 ? 20 : 5);
		
		this.info("inputType: " + inputType);
		
		if (inputType >= 1 && inputType <= 4) {
		
			// 輸入為 1/2/3/4
			
			// QueryType A <--> 1
			//			 B <--> 2
			//			 C <--> 3
			//			 D <--> 4
			//	    無對應      0
			
			for (int i = 0; i < ReportType.values().length; i++)
			{
				queryList.add(new Query(ReportType.values()[i], QueryType.values()[inputType-1]));
			}		
			
		} else if (inputType == 0) {
			
			// 輸入為 0: 全出
				
			for (int i = 0; i < ReportType.values().length; i++)
			{
				for (int j = 0; j < QueryType.values().length; j++)
				{			
					queryList.add(new Query(ReportType.values()[i], QueryType.values()[j]));
				}
			}
		}
		
		// Queue完成, 依序查表產表
		// 每次/第一次處理表時才開新頁/開pdf
		// 以便處理報表首顯示reportType
		
		this.info("Start query and output");
		
		Boolean isFirstOutput = true;
		
		for (Query qo : queryList)
		{
			// 取得此循環應做的報表，並且開pdf/開新頁			
			currentReportType = qo.reportType;
			
			if (isFirstOutput)
			{
				this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM014", "平均利率月報表", "LM014", "A4", "L");
				isFirstOutput = false;
			} else
			{
				this.newPage();
			}
			
			// 執行對應的Query
			List<Map<String, String>> listLM014 = null;
			try {
				listLM014 = lM014ServiceImpl.findAll(titaVo, qo.reportType.entCond, qo.reportType.dptCond, qo.queryType);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("lM014ServiceImpl.findAll error = " + errors.toString());
			}
			
			// 開始輸出
			if (listLM014 != null && !listLM014.isEmpty())
			{
				
				// 表頭
				
				print(1, 0, "┌─────────────────┬───────────────────────────────────┬───────────────────────────────────┐");
				print(1, 0, "｜　　　　　　　　　　　　　　　　　｜　　　　　　　　　　　　　　本　　　月　　　份　　　　　　　　　　　　｜　　　　　　　　　　年　　初　　到　　本　　月　　份　　　　　　　　　｜");
				print(1, 0, "｜　　　　　　　　　　　　　　　　　├────────┬────┬────────┬────┬───────┼────────┬────┬────────┬────┬───────┤");
				print(0, 19, qo.queryType.value, "C");
				print(1, 0, "｜　　　　　　　　　　　　　　　　　｜　本月利息收入　｜　佔率　｜　本月月底餘額　｜　佔率　｜　平均利率％　｜　累計利息收入　｜　佔率　｜每月平均放款餘額｜　佔率　｜　平均利率％　｜");
				
				for (Map<String, String> tLDVo : listLM014)
				{
		   /**
		   * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7---------8
		   * ----------------12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
  		   */	
				print(1, 0, "├─────────────────┼────────┼────┼────────┼────┼───────┼────────┼────┼────────┼────┼───────┤");
				print(1, 0, "｜　　　　　　　　　　　　　　　　　｜　　　　　　　　｜　　　　｜　　　　　　　　｜　　　　｜　　　　　　　｜　　　　　　　　｜　　　　｜　　　　　　　　｜　　　　｜　　　　　　　｜");
				print(0, 4, tLDVo.get("F0"), "L");
				print(0, 24, tLDVo.get("F1"), "L");
				print(0, 53, formatAmt(tLDVo.get("F2"), 0), "R");
				print(0, 62, tLDVo.get("F3"), "R");
				print(0, 81, formatAmt(tLDVo.get("F4"), 0), "R");
				print(0, 90, tLDVo.get("F5"), "R");
				print(0, 97, tLDVo.get("F6"), "L");
				print(0, 125, formatAmt(tLDVo.get("F7"), 0), "R");
				print(0, 134, tLDVo.get("F8"), "R");
				print(0, 153, formatAmt(tLDVo.get("F9"), 0), "R");
				print(0, 162, tLDVo.get("F10"), "R");
				print(0, 168, tLDVo.get("F11"), "L");
				}
				
				print(1, 0, "└─────────────────┴────────┴────┴────────┴────┴───────┴────────┴────┴────────┴────┴───────┘");
			} else {
				// query結果為空白
				this.print(1, 0, "本月無資料!!");
			}
		}
	
		long sno = this.close();
		this.toPdf(sno);
		
		return true;
	}
}
