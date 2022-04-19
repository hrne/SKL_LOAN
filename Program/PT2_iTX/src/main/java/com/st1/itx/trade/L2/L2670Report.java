package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2670Report")
@Scope("prototype")
public class L2670Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public CdEmpService sCdEmpService;

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
	private String reportCode = "L2670";
	private String reportItem = "貸後契約內容變更手續費收據";
	private String security = "";
	private String pageSize = "A4";
	private String pageOrientation = "P";
	private String reprint = "******重印******";
	private int sPrintCode = 0;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2670Report.printHeader");

		this.print(-2, 55, "新光人壽保險股份有限公司", "C");
		if (sPrintCode != 0) {
			this.print(-2, 110, reprint, "R");
		}
		this.print(-3, 55, this.reportItem, "C");
		this.print(-4, 6, "茲收到", "L");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(5);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

	}

	// 自訂表尾
	@Override
	public void printFooter() {

		CdEmp tCdEmp = new CdEmp();
		String EmpName = "";
		tCdEmp = sCdEmpService.findById(this.titaVo.getTlrNo(), titaVo);
		if (tCdEmp != null) {
			EmpName = tCdEmp.getFullname(); // 建檔人員姓名
		}

		this.print(-15, 25, "放款部部章：　　　　　　　　　　　　　　　　　　　經辦：" + this.titaVo.getTlrNo() + "  " + EmpName);
	}

	public void exec(TitaVo titaVo, int PrintCode) throws LogicException {
		this.info("L2670Report exec ...");
		sPrintCode = PrintCode;
		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getEntDy());
		this.brno = titaVo.getBrno();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		// 戶號
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		// 額度
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		// 契變日期
		int iContractChgDate = Integer.valueOf(titaVo.getParam("ContractChgDate")) + 19110000;
		Integer.valueOf(titaVo.getParam("ContractChgNo"));

		String iRvNo = parse.IntegerToString(iContractChgDate, 8) + titaVo.getParam("ContractChgNo");

		Point a = new Point(30, 60);
		Point b = new Point(550, 60);
		Point c = new Point(30, 150);
		Point d = new Point(550, 150);

		Point e = new Point(120, 60);
		Point f = new Point(210, 60);
		Point g = new Point(270, 60);
		Point h = new Point(350, 60);
		Point i = new Point(400, 60);

		Point j = new Point(30, 90);

		Point k = new Point(120, 90);
		Point m = new Point(270, 90);
		Point n = new Point(350, 90);
		Point o = new Point(400, 90);
		Point p = new Point(550, 90);

		Point q = new Point(210, 150);

		Line ab = new Line(a, b);
		Line ac = new Line(a, c);
		Line bd = new Line(b, d);
		Line cd = new Line(c, d);
		Line ek = new Line(e, k);
		Line fq = new Line(f, q);
		Line gm = new Line(g, m);
		Line hn = new Line(h, n);
		Line io = new Line(i, o);
		Line jp = new Line(j, p);

		ArrayList<Line> lineList = new ArrayList<Line>();

		lineList.add(ab);
		lineList.add(ac);
		lineList.add(bd);
		lineList.add(cd);
		lineList.add(ek);
		lineList.add(fq);
		lineList.add(gm);
		lineList.add(hn);
		lineList.add(io);
		lineList.add(jp);

		this.drawLineList(lineList);

		String custName = "";
		CustMain tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
		if (tCustMain != null) {
			custName = tCustMain.getCustName();
		}

		AcReceivable acReceivable = acReceivableService.findById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo));

		if (acReceivable == null) {
			acReceivable = new AcReceivable();
		}
		this.info("acReceivable L2670REPORT = " + acReceivable);
		String acDate = showRocDate(acReceivable.getOpenAcDate(), 0);

		String feeAmt = formatAmt(acReceivable.getRvAmt(), 0);
		this.print(1, 1, "　　");
		this.print(1, 8, "　戶號　　　　　　　　　　　　　　　借款人　　　　　　　　　　　日期");
		String custNoX = FormatUtil.pad9(String.valueOf(iCustNo), 7) + "-" + FormatUtil.pad9(String.valueOf(iFacmNo), 3);
		this.print(0, 27, custNoX);
		this.print(0, 55, custName);
		this.print(0, 85, acDate);
		this.print(1, 1, "　　");
		this.print(1, 1, "　　");
		this.print(1, 1, "　　");
		this.print(1, 8, "　　　　金額：　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　元");
		this.print(0, 85, feeAmt, "R");
	}

	private void drawLineList(ArrayList<Line> lineList) {
		if (lineList != null && lineList.size() > 0) {
			for (Line line : lineList) {
				drawLine(line);
			}
		}
	}

	private void drawLine(Line line) {
		this.drawLine(line.getA().getX(), line.getA().getY(), line.getB().getX(), line.getB().getY());
	}

	private class Point {
		int x = 0;
		int y = 0;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;

		}

		public int getY() {
			return y;
		}
	}

	private class Line {
		Point A;
		Point B;

		/**
		 * Set a line from A to B
		 * 
		 * @param A starting point.
		 * @param B end.
		 */
		public Line(Point A, Point B) {
			this.A = A;
			this.B = B;
		}

		public Point getA() {
			return A;
		}

		public Point getB() {
			return B;
		}
	}
}
