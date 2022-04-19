package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsId;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2076Report")
@Scope("prototype")
public class L2076Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L2076Report.class);

	@Autowired
	public ClOtherRightsService sClOtherRightsService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public CdLandOfficeService cdLandOfficeService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public FacCloseService sFacCloseService;

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
	private String reportCode = "L2631";
	private String reportItem = "抵押權塗銷同意書";
	private String security = "";
//	private String pageSize ="A5";
	private String pageOrientation = "P";
	private int iCustNo = 0;
	private int iCloseNo = 0;

	// 自訂表頭
	@Override
	public void printHeader() {

		logger.info("L2076Report.printHeader");

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

	}

	public void exec(TitaVo titaVo, FacClose tFacClose) throws LogicException {
		logger.info("L2076Report exec ...");

		// 設定字體1:標楷體 字體大小36
		this.setFont(1, 36);
		this.reportDate = Integer.valueOf(titaVo.getEntDy());
		this.brno = titaVo.getBrno();
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		iCloseNo = parse.stringToInteger(titaVo.getParam("CloseNo"));
		reportItem = "抵押權塗銷同意書,戶號 :" + titaVo.getParam("CustNo");

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, "A4", pageOrientation);

		this.setCharSpaces(0);

		// 擔保品代號1
		int iClCode1 = Integer.valueOf(titaVo.getParam("OOClCode1"));
		// 擔保品代號2
		int iClCode2 = Integer.valueOf(titaVo.getParam("OOClCode2"));
		// 擔保品編號
		int iClNo = Integer.valueOf(titaVo.getParam("OOClNo"));
		// 他項權利序號
		String iSeq = titaVo.getParam("OOSeq");
		// 取戶名
		String CustName = "";
		CustName = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo).getCustName();
		FacCloseId FacCloseId = new FacCloseId();
		FacCloseId.setCustNo(iCustNo);
		FacCloseId.setCloseNo(iCloseNo);
		ClOtherRights tClOtherRights = sClOtherRightsService
				.findById(new ClOtherRightsId(iClCode1, iClCode2, iClNo, iSeq), titaVo);
		if (tClOtherRights == null) {
			throw new LogicException(titaVo, "E2003", "擔保品他項權利檔"); // 查無資料
		}
		int DocNo = tFacClose.getDocNo();
		String DocNoyy = parse.IntegerToString(DocNo, 7).substring(0, 3);

		String DocNoseQ = parse.IntegerToString(DocNo, 7).substring(3, 7);
		// 功能
		String funCdString = tFacClose.getFunCode();
		// 縣市
		String wkCity = "";
		if ("".equals(tClOtherRights.getOtherCity())) {
			CdCity tCdCity = cdCityService.findById(tClOtherRights.getCity(), titaVo);
			if (tCdCity != null) {
				wkCity = tCdCity.getCityItem();
			}
		} else {
			wkCity = tClOtherRights.getOtherCity();
		}
		// 地政
		String wkLandAdm = "";
		if ("".equals(tClOtherRights.getOtherLandAdm())) {
			CdCode tCdCode = cdCodeService.findById(new CdCodeId("LandOfficeCode", tClOtherRights.getLandAdm()),
					titaVo);
			if (tCdCode != null) {
				wkLandAdm = tCdCode.getItem();
			}
		} else {
			wkLandAdm = tClOtherRights.getOtherLandAdm();
		}
		// 收件年
		String wkRecYear = "";
		wkRecYear = "" + tClOtherRights.getRecYear();
		// 收件字
		String wkRecWord = "";
		if ("".equals(tClOtherRights.getOtherRecWord())) {
			CdLandOffice tCdLandOffice = cdLandOfficeService
					.findById(new CdLandOfficeId(tClOtherRights.getLandAdm(), tClOtherRights.getRecWord()), titaVo);
			if (tCdLandOffice != null) {
				wkRecWord = tCdLandOffice.getRecWordItem();
			}
		} else {
			wkRecWord = tClOtherRights.getOtherRecWord();
		}
		// 收件號
		String wkRecNumber = "";
		wkRecNumber = tClOtherRights.getRecNumber();
		// 權利價值說明
		String wkRightsNote = "";
		CdCode tCdCode = cdCodeService.findById(new CdCodeId("ClRightsNote", tClOtherRights.getRightsNote()), titaVo);
		if (tCdCode != null) {
			wkRightsNote = tCdCode.getItem();
		}
		// 擔保債權總金額
		BigDecimal wkSecuredTotal = BigDecimal.ZERO;
		wkSecuredTotal = tClOtherRights.getSecuredTotal();
		// 金額轉中文大寫
		String amtChinese = this.convertAmtToChinese(wkSecuredTotal) + "元整";

		logger.info("Doc = " + DocNo);
		logger.info("DocNoyy = " + DocNoyy);
		logger.info("DocNoseQ = " + DocNoseQ);
		logger.info("wkCity = " + wkCity);
		logger.info("wkLandAdm = " + wkLandAdm);
		logger.info("RecYear = " + wkRecYear);
		logger.info("RecWord = " + wkRecWord);
		logger.info("RecNumber = " + wkRecNumber);
		logger.info("RightsNote = " + wkRightsNote);
		logger.info("SecuredTotal = " + wkSecuredTotal);
		logger.info("amtChinese = " + amtChinese);
		if ("3".equals(funCdString)) {

			Point a = new Point(85, 200);
			Point b = new Point(120, 200);
			Point c = new Point(85, 220);
			Point d = new Point(120, 220);

			Line ab = new Line(a, b);
			Line ac = new Line(a, c);
			Line bd = new Line(b, d);
			Line cd = new Line(c, d);

			ArrayList<Line> lineList = new ArrayList<Line>();

			lineList.add(ab);
			lineList.add(ac);
			lineList.add(bd);
			lineList.add(cd);

			this.drawLineList(lineList);
			this.setFont(1, 17);
			this.print(7, 11, "補發", "L");
			this.print(-9, 1, " ");
		}
		this.print(1, 1, "　　");


		// 設定字體1:標楷體 字體大小14
		this.setFont(1, 14);
		this.print(1, 5, "                                        新壽放款   　　           　 ");
		this.print(0, 40, "(" + DocNoyy + ")");
		this.print(0, 62, DocNoseQ);
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");

		this.print(1, 10, "茲因                債務清償，同意       縣(市)      地政事務所");
		this.print(0, 14, CustName);
		if (wkCity.length() > 3) {
			this.print(0, 44, wkCity.substring(0, 3));
		} else {
			this.print(0, 44, wkCity);
		}
		if (wkLandAdm.length() > 3) {
			this.print(0, 57, wkLandAdm.substring(0, 3));
		} else {
			this.print(0, 57, wkLandAdm);
		}
		this.print(1, 1, " ");
		this.print(1, 1, " ");

		this.print(1, 10, "中華民國     年    月    日收件             字第             號");
		this.print(0, 18, wkRecYear);
		this.print(0, 25, "  ");
		this.print(0, 30, "  ");
		if (wkRecWord.length() > 6) {
			this.print(0, 41, wkRecWord.substring(0, 6));
		} else {
			this.print(0, 41, wkRecWord);
		}
		this.print(0, 60, wkRecNumber);
		this.print(1, 1, " ");
		this.print(1, 1, " ");

		this.print(1, 10, "權利價值                                                     ");
		this.print(0, 18, wkRightsNote + " " + amtChinese);
		this.print(1, 1, " ");
		this.print(1, 1, " ");

		this.print(1, 10, "抵押權登記全部塗銷。");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 10, "");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 23, "代表人  吳  東  進");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 1, " ");
		this.print(1, 10, "地　  　址 : 台北市忠孝西路1段66號31～43樓");
		this.print(1, 1, " ");
		if ("3".equals(funCdString)) {
			this.print(1, 17, "※他項權利證明書與抵押權設定契約書已由客戶領取無誤。");
		} else {
			this.print(1, 1, " ");
		}
		this.print(1, 17, " ");
		this.print(1, 1, " ");
		this.print(1, 10, "");
		String date = titaVo.getCalDy();
		this.print(0, 40, date.substring(0, 3));
		this.print(0, 52, date.substring(3, 5));
		this.print(0, 68, date.substring(5, 7));
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
