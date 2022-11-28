package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5904ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
/**
 * 列印債協通知單
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L5104Report extends MakeReport {

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
	private String reportCode = "L5104A";
	private String reportItem = "未歸還月報表";
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
		this.print(-5, 1, "　　　戶號　　　　         戶名　　　　　　　　　　　　         　　用途　　　借閱人　　　　　　　　　　   　管理人　　　　　    　　借閱日期");
		this.print(-6, 7,
				"-------------------------------------------------------------------------------------------------------------------------------------------");

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
		this.info("L5104Report exec ...");
		this.info("startDate    = " + startDate);
		this.info("endDate      = " + endDate);

		headerStartDate = this.showRocDate(startDate, 1);
		headerEndDate = this.showRocDate(endDate, 1);

		this.setFont(1, 12);
		this.reportDate = endDate;
		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem).setSecurity("security").setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l5904ServiceImpl.findAll(startDate, endDate, "00", 1, this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5904ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		if (resultList == null || resultList.isEmpty()) {
			// 本日無資料
			print(1, 1, "本日無資料");
		} else {
			int i = 0;
			String ipUsCode = "";
			for (Map<String, String> result : resultList) {
				i++;
				if (!ipUsCode.equals(result.get("F9"))) {
					if (i != 1) {
						print(1, 7,
								"-------------------------------------------------------------------------------------------------------------------------------------------");
					}
				}
				int iFacmNo = parse.stringToInteger(result.get("F1"));
				String ixFacmNo = "";
				if (iFacmNo < 10) {
					ixFacmNo = "00" + result.get("F1");
				} else if (iFacmNo < 100) {
					ixFacmNo = "0" + result.get("F1");
				}
				String iCustNo = result.get("F0");
				String ixCustNo = "";
				for (int y = 7; iCustNo.length() != y; y--) {
						ixCustNo += "0";
				}
				this.info("ixCustNo    = " + ixCustNo);
				print(1, 7, ixCustNo += result.get("F0") + '-' + ixFacmNo);
				print(0, 28, result.get("F3"));
				String iUsCode = result.get("F9");
				String iUsCodeName = "";
				if ("01".equals(iUsCode)) {
					iUsCodeName = "清償";
				}
				if ("02".equals(iUsCode)) {
					iUsCodeName = "法拍";
				}
				if ("03".equals(iUsCode)) {
					iUsCodeName = "增貸";
				}
				if ("04".equals(iUsCode)) {
					iUsCodeName = "展期";
				}
				if ("05".equals(iUsCode)) {
					iUsCodeName = "撥款";
				}
				if ("06".equals(iUsCode)) {
					iUsCodeName = "查閱";
				}
				if ("07".equals(iUsCode)) {
					iUsCodeName = "重估";
				}
				if ("08".equals(iUsCode)) {
					iUsCodeName = "其他";
				}

				print(0, 69, iUsCodeName);

				print(0, 79, result.get("F5") + ' ' + result.get("LTlrItem"));
				print(0, 110, result.get("F4") + ' ' + result.get("F13"));
				String iYYY = parse.IntegerToString(parse.stringToInteger(result.get("F6").substring(0, 4)) - 1911, 3);
				String iMM = result.get("F6").substring(4, 6);
				String iDD = result.get("F6").substring(6, 8);
				this.info("iYYY  = " + iYYY);
				this.info("iMM   = " + iMM);
				this.info("iDD   = " + iDD);
				print(0, 134, iYYY + '/' + iMM + '/' + iDD);
				this.info("iUsCode      = " + iUsCode);
				this.info("ipUsCode     = " + ipUsCode);

				ipUsCode = iUsCode;
				if (i == resultList.size()) {
					print(1, 7,
							"-------------------------------------------------------------------------------------------------------------------------------------------");
					print(1, 7, "總　　計:" + "         " + i + "筆　　未歸還");
				}

			}

			// 此表不用簽核但需要印報表結束
			this.print(1, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");

			this.close();
			this.info("L5104Report finished.");
		}
	}
}