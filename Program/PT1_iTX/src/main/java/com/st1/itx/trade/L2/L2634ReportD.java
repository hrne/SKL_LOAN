package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2634ReportD")
@Scope("prototype")
public class L2634ReportD extends MakeReport {

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
	public LoanCom loanCom;
	@Autowired
	public CustNoticeCom custNoticeCom;

//	private static DecimalFormat df = new DecimalFormat("#########################0.0#");

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
	private String reportCode = "L2634";
	private String reportItem = "雙掛號信封-整批列印";
	private String defaultPdf = "";

	private String security = "";
//	private String pageSize ="A5";
	private String pageOrientation = "P";
	private int iCustNo = 0;
	private int iCloseNo = 0;
	boolean isLast = false;

//	// 製表日期
//	private String NowDate;
//	// 製表時間
//	private String NowTime;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2634ReportD.printHeader");

//		this.print(-2, 55, "新光人壽保險股份有限公司", "C");
//		this.print(-3, 55, "抵押權塗銷同意書", "C");
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

	public Boolean exec(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {

		this.info("L2634ReportD exec");

		exportPdf(lClOtherRights, titaVo);

		return true;
	}

	private void exportPdf(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("exportExcel ... ");

		// 設定字體1:標楷體 字體大小36
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2634D", "雙掛號信封", "", "L2631D_雙掛號信封.pdf");
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2634D", "雙掛號信封-整批列印", "", "A4", "L");

		String custId = "";

		int custNo = 0;
		int closeNo = 0;
		for (ClOtherRights t : lClOtherRights) {

			if (custNo != t.getReceiveCustNo() || closeNo != t.getCloseNo()) {
				custNo = t.getReceiveCustNo();
				closeNo = t.getCloseNo();
				FacClose tFacClose = sFacCloseService.findById(new FacCloseId(custNo, closeNo), titaVo);
				if (tFacClose == null) {
					this.info("tFacClose = null continue 1");
					continue;
				}
				this.info("CollectWayCode =" + tFacClose.getCollectWayCode());
				if ("21".equals(tFacClose.getCollectWayCode()) || "26".equals(tFacClose.getCollectWayCode()) || "27".equals(tFacClose.getCollectWayCode())) {
					if (!isLast) {
						this.info("C newPage");
						this.newPage();
					}
				} else {
					this.info("continue 2");
					continue;
				}
				getSelecTotal(custNo, closeNo, lClOtherRights, titaVo);
				CustMain tCustMain = sCustMainService.custNoFirst(tFacClose.getCustNo(), tFacClose.getCustNo(), titaVo);
				if (tCustMain != null) {
					custId = tCustMain.getCustId();
				}
				String custName = loanCom.getCustNameByNo(tFacClose.getCustNo());
				if (custName.length() > 10) {
					custName = loanCom.getCustNameByNo(tFacClose.getCustNo()).substring(0, 10);
				}
				String telNo = "";
				if (tFacClose.getTelNo1().isEmpty()) {

					if (tFacClose.getTelNo2().isEmpty()) {
						telNo = tFacClose.getTelNo3();
					} else {
						telNo = tFacClose.getTelNo2();
					}
				} else {
					telNo = tFacClose.getTelNo1();
				}
				String WkRegAddres = "";
				if (tFacClose.getPostAddress().isEmpty()) {
					WkRegAddres = custNoticeCom.getCurrAddress(tCustMain, titaVo);
					if (tCustMain.getCurrZip3() != null || !tCustMain.getCurrZip3().isEmpty()) {
						if (tCustMain.getCurrZip2() != null || !tCustMain.getCurrZip2().isEmpty()) {
							WkRegAddres = tCustMain.getCurrZip3() + " " + tCustMain.getCurrZip2() + " " + WkRegAddres;
						} else {
							WkRegAddres = tCustMain.getCurrZip3() + " " + WkRegAddres;
						}
					}
				} else {
					WkRegAddres = tFacClose.getPostAddress();
				}
				this.setLineSpaces(5);
				this.setCharSpaces(0);
				this.setFont(1);
				this.setFontSize(15);
//				this.printImageCm(500, 50, 100, "雙掛號.jpg");
				this.print(-2, 21, "放款部　放款服務課");
				this.print(-4, 18, "105　　台北市松山區南京東路五段125號13樓");
				this.info("1 test 2634 ");

				Point a = new Point(550, 155);
				Point b = new Point(610, 155);
				Point c = new Point(550, 185);
				Point d = new Point(610, 185);

				Point a4 = new Point(546, 151);
				Point b4 = new Point(614, 151);
				Point c4 = new Point(546, 189);
				Point d4 = new Point(614, 189);

				Point a41 = new Point(544, 151);
				Point b41 = new Point(616, 151);
				Point c41 = new Point(544, 189);
				Point d41 = new Point(616, 189);

				Line ab = new Line(a, b);
				Line ac = new Line(a, c);
				Line bd = new Line(b, d);
				Line cd = new Line(c, d);

				Line ab4 = new Line(a41, b41);
				Line ac4 = new Line(a4, c4);
				Line bd4 = new Line(b4, d4);
				Line cd4 = new Line(c41, d41);

				ArrayList<Line> lineList = new ArrayList<Line>();
				ArrayList<Line> lineList4 = new ArrayList<Line>();

				lineList.add(ab);
				lineList.add(ac);
				lineList.add(bd);
				lineList.add(cd);
				lineList4.add(ab4);
				lineList4.add(ac4);
				lineList4.add(bd4);
				lineList4.add(cd4);

				this.drawLineList(lineList);
				this.drawLineList4(lineList4);

				this.print(-8, 80, "雙掛號");
				this.print(-11, 32, "【限定本人拆閱，若無此人，請寄回本公司】");
				this.print(-13, 32, WkRegAddres); // 地址

				this.print(-15, 41, "#" + StringUtils.leftPad(String.valueOf(tFacClose.getCustNo()), 7, "0") + "  " + custName + "  啟"); // 戶號戶名
				this.print(-17, 39, "電話:" + telNo); // 電話

			}

			if (isLast) {

				break;
			}
		}
		this.info("D 結束");

		this.close();
	}

	public void drawLineList(ArrayList<Line> lineList) {
		if (lineList != null && lineList.size() > 0) {
			for (Line line : lineList) {
				drawLine(line);
			}
		}
	}

	public void drawLineList4(ArrayList<Line> lineList) {
		if (lineList != null && lineList.size() > 0) {
			for (Line line : lineList) {
				drawLine4(line);
			}
		}
	}

	public void drawLine(Line line) {
		this.info("drawLine ");
		this.drawLine(line.getA().getX(), line.getA().getY(), line.getB().getX(), line.getB().getY());
	}

	public void drawLine4(Line line) {
		this.drawLine(line.getA().getX(), line.getA().getY(), line.getB().getX(), line.getB().getY(), 4);
	}

	class Point {
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

	public class Line {
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

	public int getSelecTotal(int custNo, int closeNo, List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		int selecTotal = 0;
		int cnt = 0;
		for (ClOtherRights t : lClOtherRights) {
			cnt++;
			if (custNo == t.getReceiveCustNo() && closeNo == t.getCloseNo()) {
				selecTotal++;
				if (cnt == lClOtherRights.size()) {
					isLast = true;
				}
			}
		}
		return selecTotal;
	}
}
