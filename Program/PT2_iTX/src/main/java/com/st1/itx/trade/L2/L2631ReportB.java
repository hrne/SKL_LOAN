package com.st1.itx.trade.L2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2631ReportB")
@Scope("prototype")
public class L2631ReportB extends MakeReport {

	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	CustMainService custMainService;
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
	private String reportCode = "L2631-B";
	private String reportItem = "用印申請書";
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

		this.info("L2631ReportB.printHeader");

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
		this.print(-15, 25, " ");

//		this.print(-15, 25, "放款部部章：　　　　　　　　　　　　　　　　　　　經辦：" + this.titaVo.getTlrNo());
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L2631ReportB exec");

		int iCustNo = parse.stringToInteger(titaVo.getParam("TimCustNo"));

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, reportItem, "", pageSize, pageOrientation);
		this.setFont(1);

		this.setFontSize(12);

		this.setCharSpaces(0);

		CdEmp iCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);
		String tlrNoX = titaVo.getTlrNo();
		if (iCdEmp != null) {
			tlrNoX = iCdEmp.getFullname();
		}
		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		String custId = "";
		if (tCustMain != null) {
			custId = tCustMain.getCustId();
		}
		this.print(1, 8, "用　印　申　請　書");
		this.print(0, 37, "年　　月　　日");
		this.print(0, 76, "監　印　人", "R");
		this.print(1, 8, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, "公司名稱：　新光人壽保險股份有限公司");
		this.print(1, 1, "");
		this.print(1, 8, "申請單位：　放款部　放款服務課");
		this.print(1, 1, "");
		this.print(1, 8, "印鑑內容：　抵押權利變更及塗銷專用章　放款部主管專用章");
		this.print(1, 1, "");
		this.print(1, 8, "用印文件說明：　抵押權塗銷同意書　　份");
		this.print(1, 1, "");
		this.print(1, 8, "用印申請理由：　業已全部清償　呈請公司用印");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 17, "借戶戶號：　" + parse.IntegerToString(iCustNo, 7));
		this.print(1, 1, "");
		this.print(1, 17, "借戶名稱：　" + FormatUtil.padX(loanCom.getCustNameByNo(iCustNo), 20));
		this.print(1, 1, "");
		this.print(1, 15, "身分證號碼：　" + custId);
		this.print(1, 1, "");
		this.print(1, 17, "設定金額：　　　　　　　　　　　萬");
		this.print(1, 1, "");
		this.print(1, 17, "結清日期：　" + this.showRocDate(parse.stringToInteger(titaVo.getParam("TranDate")), 1));
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, "部主管：　　　　　　　課主管：　　　　　　　經辦：");

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
