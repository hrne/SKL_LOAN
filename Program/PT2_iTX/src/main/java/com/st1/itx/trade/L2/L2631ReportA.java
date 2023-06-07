package com.st1.itx.trade.L2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2631ReportA")
@Scope("prototype")
public class L2631ReportA extends MakeReport {

	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	LoanCom loanCom;

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
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
	private String reportCode = "L2631-A";
	private String reportItem = "簽收回條";
	private String defaultPdf = "";
	private TempVo t2TempVo = new TempVo();

	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "P";

//	// 製表日期
//	private String NowDate;
//	// 製表時間
//	private String NowTime;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2631ReportA.printHeader");

		this.print(-1, 76, "機密等級：" + this.security, "R");
		this.print(-4, 6, "", "L");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(5);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.setFontSize(8);
		this.print(-83, 2, "SKL-B#DBB94!5");

//		this.print(-15, 25, "放款部部章：　　　　　　　　　　　　　　　　　　　經辦：" + this.titaVo.getTlrNo());
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L2631ReportA exec");

		int iCustNo = parse.stringToInteger(titaVo.getParam("TimCustNo"));

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(reportCode).setRptItem(reportItem).setSecurity(security).setRptSize(pageSize)
				.setPageOrientation(pageOrientation).build();
		this.open(titaVo, reportVo);
//		.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, reportItem, "", pageSize, pageOrientation);
		this.setFont(1);

		this.setFontSize(12);

		this.setCharSpaces(0);

		CdEmp iCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);

		String tlrNoX = titaVo.getTlrNo();
		if (iCdEmp != null) {
			tlrNoX = iCdEmp.getFullname();
		}
		this.print(1, 8, "戶名：" + FormatUtil.padX(loanCom.getCustNameByNo(iCustNo), 20)); // 戶名
		this.print(0, 37, "戶號：" + parse.IntegerToString(iCustNo, 7)); // 戶號
		this.print(0, 76, "結清日期：" + this.showRocDate(parse.stringToInteger(titaVo.getParam("TranDate")), 1), "R"); // 結清日期
		this.print(1, 8, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		this.print(1, 1, "");
		this.print(1, 44, "茲　　收　　到", "C");
		this.print(1, 1, "");
		this.print(1, 8, "一、他項權利證明書：　　　　　 份。");
		this.print(1, 1, "");
		this.print(1, 8, "二、抵押權設定契約書：　　　　 份。");
		this.print(1, 1, "");
		this.print(1, 8, "三、抵押權塗銷同意書：　　　　 份。");
		this.print(1, 1, "");
		this.print(1, 8, "四、火險單正本：　　　　　　　 份。");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, "●上列文件領取後，貴貸戶須至地政事務所辦理塗銷，解除抵押設定。");
		this.print(1, 8, "　如以原押品向本公司再申請撥款，須重新辦理設定手續。");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 44, "無　　誤　　此　　致", "C");
		this.print(1, 1, "");
		this.print(1, 8, "新光人壽保險股份有限公司　　　　台照");
		this.print(1, 1, "");
		this.print(1, 24, "領收人：");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, "中　華　民　國　　　　　　　　年　　　　　　　　月　　　　　　　　日");
		this.print(1, 1, "");
		this.print(1, 8, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		this.print(1, 1, "");
//		this.print(1, 8, "※請將此張簽收回條簽章後寄回，謝謝！");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, "地 址：台北市松山區南京東路五段１２５號１３樓　　　　　　 放款服務課");
		this.print(1, 1, "");
		this.print(1, 8, "備 註：　　　　　　　　　　　　　　　　　　　　製表人：　" + tlrNoX);


		this.setFontSize(18);
		this.print(-25, 6, "※請將此張簽收回條簽章後寄回，謝謝！");
		this.setFontSize(12);
//		for (int i = 1; i <= 400; i++) {
//			if ((i % 10) == 0) {
//				this.print(-2, i, "" + (i / 10));
//			}
//			this.print(-1, i, "" + (i % 10));
//			this.print(-i, 1, "" + (i % 10));
//		}

		this.info("A 結束");
		this.close();

	}

}
