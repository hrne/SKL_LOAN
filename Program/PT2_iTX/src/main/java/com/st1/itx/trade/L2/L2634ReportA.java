package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2634ReportA")
@Scope("prototype")
public class L2634ReportA extends MakeReport {
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
	GSeqCom gGSeqCom;

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
	private String reportItem = "抵押權塗銷同意書-整批列印";
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

		this.info("L2634ReportA.printHeader");

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

	public void exec(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		this.info("L2634ReportA exec ...");

		// 設定字體1:標楷體 字體大小36
		this.setFont(1, 36);
		this.reportDate = Integer.valueOf(titaVo.getEntDy());
		this.brno = titaVo.getBrno();
		int iAgreeNo = parse.stringToInteger(titaVo.getParam("AgreeNo"));
//		this.NowDate = dDateUtil.getNowStringRoc();
//		this.pageSize = "A5";
//		this.NowTime = dDateUtil.getNowStringTime();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, "A4", pageOrientation);
		int agreecnt = 0;
		for (ClOtherRights t : lClOtherRights) {

			iCustNo = t.getReceiveCustNo();
			iCloseNo = t.getCloseNo();
			int wkClCode1 = t.getClCode1();
			int wkClCode2 = t.getClCode2();
			int wkClNo = t.getClNo();
			String wkClOtherSeq = t.getSeq();
			reportItem = "抵押權塗銷同意書,戶號 :" + iCustNo;

			// 自動取公文編號
			int wkDocNo = 0;

			wkDocNo = gGSeqCom.getSeqNo(titaVo.getEntDyI() / 10000, 1, "L2", "2631", 9999, titaVo);
			String finalDocNo = StringUtils.leftPad(String.valueOf(wkDocNo), 4, "0");

			String docNo = titaVo.getCalDy().substring(0, 3) + finalDocNo;

			this.setCharSpaces(0);

			getSelecTotal(iCustNo, iCloseNo, wkClCode1, wkClCode2, wkClNo, wkClOtherSeq, lClOtherRights, titaVo);
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
//			更新公文編號
			FacClose updFacClose = sFacCloseService.holdById(FacCloseId, titaVo);
			if (updFacClose != null) {
				if (updFacClose.getDocNo() == 0) {
					updFacClose.setDocNo(parse.stringToInteger(docNo));
				} else {
					updFacClose.setDocNoE(parse.stringToInteger(finalDocNo));
				}
				updFacClose.setAgreeNo(parse.IntegerToString((iAgreeNo + agreecnt), 10));
				this.info("AgreeNo =" + updFacClose.getAgreeNo());
				try {
					sFacCloseService.update(updFacClose, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "清償作業檔");
				}
			}
			FacClose tFacClose = sFacCloseService.findById(FacCloseId, titaVo);
			int DocNo = tFacClose.getDocNo();
			String DocNoyy = parse.IntegerToString(DocNo, 7).substring(0, 3);

			String DocNoseQ = parse.IntegerToString(DocNo, 7).substring(3, 7);

			// 縣市
			String wkCity = "";
			if (!"AAAA".equals(t.getCity())) {
				if ("".equals(t.getOtherCity())) {
					CdCity tCdCity = cdCityService.findById(t.getCity(), titaVo);
					if (tCdCity != null) {
						wkCity = tCdCity.getCityItem();
					} else {
						CdCode tCdCode = cdCodeService.findById(new CdCodeId("ClOtherRightsCityCd", t.getCity()),
								titaVo);
						wkCity = tCdCode.getItem();
					}
				} else {
					wkCity = t.getOtherCity();
				}
			}
			// 地政
			String wkLandAdm = "";
			if ("".equals(t.getOtherLandAdm())) {
				CdCode tCdCode = cdCodeService.findById(new CdCodeId("LandOfficeCode", t.getLandAdm()), titaVo);
				if (tCdCode != null) {
					wkLandAdm = tCdCode.getItem();
				}
			} else {
				wkLandAdm = t.getOtherLandAdm();
			}
			// 收件年
			String wkRecYear = "";
			wkRecYear = "" + t.getRecYear();
			// 收件字
			String wkRecWord = "";
			if ("".equals(t.getOtherRecWord())) {
				CdLandOffice tCdLandOffice = cdLandOfficeService
						.findById(new CdLandOfficeId(t.getCity(), t.getLandAdm(), t.getRecWord()), titaVo);
				if (tCdLandOffice != null) {
					wkRecWord = tCdLandOffice.getRecWordItem();
				}
			} else {
				wkRecWord = t.getOtherRecWord();
			}
			// 收件號
			String wkRecNumber = "";
			wkRecNumber = t.getRecNumber();
			// 權利價值說明 空白(即無代入字樣)"
			String wkRightsNote = "";
			if (!t.getRightsNote().isEmpty()) {
				CdCode tCdCode = cdCodeService.findById(new CdCodeId("ClRightsNote", t.getRightsNote()), titaVo);
				if (tCdCode != null) {
					wkRightsNote = tCdCode.getItem();
				}
			}

			// 擔保債權總金額
			BigDecimal wkSecuredTotal = BigDecimal.ZERO;
			wkSecuredTotal = t.getSecuredTotal();
			// 金額轉中文大寫
			String amtChinese = this.convertAmtToChinese(wkSecuredTotal) + "萬元整";

			this.info("Doc = " + DocNo);
			this.info("DocNoyy = " + DocNoyy);
			this.info("DocNoseQ = " + DocNoseQ);
			this.info("wkCity = " + wkCity);
			this.info("wkLandAdm = " + wkLandAdm);
			this.info("RecYear = " + wkRecYear);
			this.info("RecWord = " + wkRecWord);
			this.info("RecNumber = " + wkRecNumber);
			this.info("RightsNote = " + wkRightsNote);
			this.info("SecuredTotal = " + wkSecuredTotal);
			this.info("amtChinese = " + amtChinese);
//		ClCode1擔保品代號1
//		ClCode2擔保品代號2
//		ClNo擔保品編號
//		Seq他項權利序號
//		City縣市
//		LandAdm地政
//		RecYear收件年
//		RecWord收件字
//		RecNumber收件號
//		RightsNote權利價值說明
//		SecuredTotal擔保債權總金額
			this.print(1, 1, "　　");

			/**
			 * -----------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * --------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
//		this.print(1, 15, " ");

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
			if (wkCity.length() > 3 && wkCity.length() <= 10) {
				this.setFont(1, 10);
				this.print(0, 54, wkCity.substring(0, wkCity.length()));
			} else {
				if (wkCity.length() > 10) {
					this.setFont(1, 8);
					this.print(0, 44, wkCity.substring(0, 10));
				} else {
					this.print(0, 44, wkCity);
				}
			}
			this.setFont(1, 14);
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
			this.print(0, 18, wkRightsNote + "最高限額新台幣 " + amtChinese);
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
			this.print(1, 23, "代　表　人　董事長  潘  柏  錚");
			this.print(1, 1, " ");
			this.print(1, 1, " ");
			this.print(1, 1, " ");
			this.print(1, 1, " ");
			this.print(1, 10, "地　  　址 : 台北市忠孝西路1段66號31～43樓");
			this.print(1, 1, " ");
			if (t.getReceiveFg() == 1) {
				this.print(1, 17, "※他項權利證明書與抵押權設定契約書已由客戶領取無誤。");
			} else {
				this.print(1, 1, " ");
			}
			this.print(1, 17, " ");
			this.print(1, 1, " ");
			this.print(1, 10, "");
//		this.print(1, 10, "");
			String date = titaVo.getCalDy();
			this.print(0, 40, date.substring(0, 3));
			this.print(0, 52, date.substring(3, 5));
			this.print(0, 68, date.substring(5, 7));
			if (t.getReceiveFg() == 1) {

				Point a = new Point(85, 119);
				Point b = new Point(120, 119);
				Point c = new Point(85, 139);
				Point d = new Point(120, 139);

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
				this.print(-7, 11, "補發", "L");
				this.setFont(1, 14);
			}
////			更新他項權利領取記號 1已領取
//			ClOtherRights updClOtherRights = sClOtherRightsService.holdById(t, titaVo);
//			updClOtherRights.setReceiveFg(1);
//			try {
//				sClOtherRightsService.update(updClOtherRights, titaVo);
//			} catch (DBException e) {
//				throw new LogicException("E0007", "L2634A ClOtherRights update " + e.getErrorMsg());
//			}
			if (!isLast) {
				agreecnt++;
				this.newPage();
			}
		}
		this.info("A 結束");
		this.close();

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

	public int getSelecTotal(int custNo, int closeNo, int clCode1, int clCode2, int clNo, String seq,
			List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {
		int selecTotal = 0;
		int cnt = 0;
		for (ClOtherRights t : lClOtherRights) {
			cnt++;
			if (custNo == t.getReceiveCustNo() && closeNo == t.getCloseNo()) {
				selecTotal++;
				if (clCode1 == t.getClCode1() && clCode2 == t.getClCode2() && clNo == t.getClNo()
						&& seq.equals(t.getSeq())) {
					this.info("size =   " + cnt + "," + lClOtherRights.size());
					if (cnt == lClOtherRights.size()) {
						isLast = true;
					}
				}
			}
		}
		return selecTotal;
	}
}
