package com.st1.itx.trade.L4;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.db.service.springjpa.cm.L9708ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4101ReportE")
@Scope("prototype")

public class L4101ReportE extends MakeReport {

	/* DB服務注入 */
	@Autowired
	public LoanNotYetService sLoanNotYetService;

	@Autowired
	public L9708ServiceImpl l9708ServiceImpl;
	
	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)

	@Autowired
	MakeReport makeReport;

	@Autowired
	DateUtil dateUtil;
	
	private String reportCode = "L9708";
	private String reportItem = "貸款自動轉帳申請書明細表";
	private String security = "機密";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int cnt = 0;
	int tcnt = 0;
	int endfg = 0;

	@Override
	public void printHeader() {
		
		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, 68, "新光人壽保險股份有限公司", "C");
		this.print(-1, 123, "機密等級：" + this.security);
		this.print(-2, 1, "報　表：" + this.reportCode);
		this.print(-2, 68, this.reportItem, "C");
		this.print(-2, 123, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 1, "來源別：放款服務課");
		this.print(-3, 123, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 123, "頁　　次：" + this.getNowPage());
		
		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */

		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "扣款銀行　　　　              撥款日期　　　　　　　　戶 號　　　　　　額度　       　首次應繳日　　　　　　　扣款帳號 　　　　　　公 司 名 稱      　　          ");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		   
		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
		
	}

	@Override
	public void printFooter() {
		if (endfg == 9) {
			return;
		}
	}

	public void exec(TitaVo titaVo,int acDate) throws LogicException {

		this.info("L9708Report exec");
		String batchNo = "";
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();
		batchNo = titaVo.getBacthNo();
		reportCode = titaVo.getTxcd();
		reportCode = reportCode + "-" + batchNo + "-E";

//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9708", "貸款自動轉帳申請書明細表", "", "A4", "L");
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, "貸款自動轉帳申請書明細表", "", "A4", "L");
		
		List<Map<String, String>> l9708List = null;
		try {
			l9708List = l9708ServiceImpl.findAll(titaVo, acDate);
			this.info("L9708Report findAll =" + l9708List.toString());
		} catch (Exception e) {
			this.info("L9708ServiceImpl.LoanBorTx error = " + e.toString());
		}

		makeReport(titaVo, l9708List);

	}

	public void makeReport(TitaVo titaVo, List<Map<String, String>> l9708List) throws LogicException {
		if (l9708List != null && l9708List.size() != 0) {
			int bankGroup = 0;
			int countGroup = 0;
			int countAll = 0;
			String backname = "";
			for (Map<String, String> l9708Vo : l9708List) {

				if (bankGroup != Integer.valueOf(l9708Vo.get("F0"))) {
					bankGroup = Integer.valueOf(l9708Vo.get("F0"));
					backname = String.valueOf(l9708Vo.get("F1"));
					if (l9708List.size() > 0 && countAll != 0) {
						this.print(1, 1, "");
						this.print(1, 118, "筆數：");
						this.print(0, 128, Integer.toString(countGroup), "R");
					}

					countGroup = 0;
				}

				countGroup++;

				if (countGroup == 1) {
					this.print(1, 1, backname + "");
				} else {
					this.print(1, 7, " ");
				}

				this.print(0, 30, showRocDate(Integer.valueOf(l9708Vo.get("F2")), 1));
				this.print(0, 52, String.format("%07d",Integer.valueOf(l9708Vo.get("F3"))));
				this.print(0, 68, String.format("%03d", Integer.valueOf(l9708Vo.get("F4"))));
				this.print(0, 82, showRocDate(Integer.valueOf(l9708Vo.get("F5")), 1));
				this.print(0, 100, l9708Vo.get("F6"));
				this.print(0, 125, l9708Vo.get("F7"));

				countAll++;

				if (countAll == l9708List.size()) {
					this.print(1, 1, "");
					this.print(1, 118, "筆數：");
					this.print(0, 128, Integer.toString(countGroup), "R");

					this.print(1, 1, "");
					this.print(1, 118, "總 筆 數：");
					this.print(0, 128, Integer.toString(countAll), "R");
				}
			}

		} else {
			this.print(1, 1, "無資料");
			print(1, 1, "　＊＊＊ＥＮＤＯＦＲＥＰＯＲＴ＊＊＊");
		}
	}


}