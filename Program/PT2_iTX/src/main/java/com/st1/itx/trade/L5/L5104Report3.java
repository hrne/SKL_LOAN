package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5904ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Service
@Scope("prototype")
/**
 * 列印債協通知單
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L5104Report3 extends MakeReport {

	@Autowired
	L5904ServiceImpl l5904ServiceImpl;
	@Autowired
	public Parse parse;
	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L5104C";
	private String reportItem = "件數統計表";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private String headerStartDate = "";
	private String headerEndDate = "";

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L5104Report.printHeader");

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 1, "報　表：" + this.reportCode);
		this.print(-2, this.getMidXAxis(), this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 1, "借閱日期：　" + headerStartDate + "　~　" + headerEndDate);
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, this.getMidXAxis(), showRocDate(this.reportDate), "C");
		// 明細表頭
		// -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		// ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		this.print(-5, 1, "用途　　　　筆數　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		this.print(-6, 1,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	
		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

	}

	@Override
	public void printContinueNext() {
		this.print(1, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

	public void exec(int startDate, int endDate, TitaVo titaVo) throws LogicException {
		this.info("L5104Report3 exec ...");
		this.info("startDate    = " + startDate);
		this.info("endDate      = " + endDate);

		headerStartDate = this.showRocDate(startDate, 1);
		headerEndDate = this.showRocDate(endDate, 1);

		this.setFont(1, 12);
		this.reportDate = endDate;
		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem).setSecurity("security").setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		try {
			// *** 折返控制相關 ***
			resultList = l5904ServiceImpl.findAll(startDate, endDate, "00", 3, this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5904ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		if (resultList == null || resultList.isEmpty()) {
			// 本日無資料
			print(1, 1, "本日無資料");
		} else {
			int cnt1 = 0;
			int cnt2 = 0;
			int cnt3 = 0;
			int cnt4 = 0;
			int cnt5 = 0;
			int cnt6 = 0;
			int cnt7 = 0;
			int cnt8 = 0;
			int i = 0;
			for (Map<String, String> result : resultList) {
				i++;
				if ("01".equals(result.get("F9"))) {
					cnt1 = cnt1 + 1;
				}
				if ("02".equals(result.get("F9"))) {
					cnt2 = cnt2 + 1;
				}
				if ("03".equals(result.get("F9"))) {
					cnt3 = cnt3 + 1;

				}
				if ("04".equals(result.get("F9"))) {
					cnt4 = cnt4 + 1;

				}
				if ("05".equals(result.get("F9"))) {
					cnt5 = cnt5 + 1;

				}
				if ("06".equals(result.get("F9"))) {
					cnt6 = cnt6 + 1;

				}
				if ("07".equals(result.get("F9"))) {
					cnt7 = cnt7 + 1;

				}
				if ("08".equals(result.get("F9"))) {
					cnt8 = cnt8 + 1;

				}

				this.info("cnt1   = " + cnt1);
				this.info("cnt2   = " + cnt2);
				this.info("cnt3   = " + cnt3);
				this.info("cnt4   = " + cnt4);
				this.info("cnt5   = " + cnt5);
				this.info("cnt6   = " + cnt6);
				this.info("cnt7   = " + cnt7);
				this.info("cnt8   = " + cnt8);

					if (i == resultList.size()) {
						if(cnt1 != 0 ) {
						print(1,1,"清償" + "         " + cnt1);
						}
						if(cnt2 != 0 ) {
						print(1,1,"法拍" + "         " + cnt2);
						}
						if(cnt3 != 0 ) {
						print(1,1,"增貸" + "         " + cnt3);
						}
						if(cnt4 != 0 ) {
						print(1,1,"展期" + "         " + cnt4);
						}
						if(cnt5 != 0 ) {
						print(1,1,"撥款" + "         " + cnt5);
						}
						if(cnt6 != 0 ) {
						print(1,1,"查閱" + "         " + cnt6);
						}
						if(cnt7 != 0 ) {
						print(1,1,"重估" + "         " + cnt7);
						}
						if(cnt8 != 0 ) {
						print(1,1,"其他" + "         " + cnt8);
						}
						int total = cnt1+cnt2+cnt3+cnt4+cnt5+cnt6+cnt7+cnt8;
						this.print(1, 1,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						print(1,1,"總　　計:" + "         " + total+"筆");
				}
			}

			// 此表不用簽核但需要印報表結束
			this.print(1, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");

		}
		this.close();
		this.info("L5104Report finished.");
	}
}