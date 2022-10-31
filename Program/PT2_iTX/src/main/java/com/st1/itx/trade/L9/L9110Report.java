package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.springjpa.cm.L9110ServiceImpl;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.EmployeeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L9110Report")
@Scope("prototype")
public class L9110Report extends MakeReport {

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9110";
	private String reportItem = "首次撥款審核資料表";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// number with commas
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	String rocYear;
	String rocMonth;

	@Autowired
	FacMainService sFacMainService;
	@Autowired
	CdEmpService sCdEmpService;
	@Autowired
	CdBaseRateService cdBaseRateService;

	@Autowired
	FacCaseApplService sFacCaseApplService;

	@Autowired
	CustMainService sCustMainService;

	@Autowired
	L9110ServiceImpl l9110ServiceImpl;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	AuthLogCom authLogCom;
	@Autowired
	EmployeeCom employeeCom;

	// length: 164
	// usually we do print() with x at 1 instead of 0 so the last symbol output is
	// actually at 165;
	// hence the extra space to avoid print()s using newBorder.length() to pin the
	// output on right side and gets wrong position
	private String newBorder = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------"
			+ " ";

	private String custEntCode = "0";

	private String thisApplNo;

	private int thisBeginRow = 7;
	private int thisMaxRow = 39;
	private boolean isLastPage = false;

	/**
	 * Make sure there's space for <i>line</i> lines of output on current page.
	 *
	 * @param line amount of lines
	 */
	private void checkSpace(int line) {
		if (this.NowRow + line > thisBeginRow + thisMaxRow - 1) {
			this.info("thisBeginRow = " + thisBeginRow);
			this.info("thisMaxRow = " + thisMaxRow);
			this.info("NowRow = " + this.NowRow);
			this.newPage();
		}
	}

	public boolean exec(TitaVo titaVo, TotaVo totaVo) throws LogicException {

		this.info("L9110Report exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = titaVo.getEntDyI() + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		makePdf(titaVo, totaVo);

		return true;
	}

	public void fillData(TitaVo titaVo, boolean isLegalPerson) throws LogicException {
		if (isLegalPerson) {
			fillDataLegalPerson(titaVo);
		} else {
			fillDataNaturalPerson(titaVo);
		}
	}

	/**
	 * 法人資料
	 *
	 * @param titaVo TitaVo
	 */
	private void fillDataLegalPerson(TitaVo titaVo) {

		List<Map<String, String>> listL9110 = null;

		List<Map<String, String>> listCrossUseQuery = null;
		List<Map<String, String>> listCustTelNoQuery = null;

		List<Map<String, String>> listGuaQuery = null;

		List<Map<String, String>> listClQuery = null;
		List<Map<String, String>> listLandQuery = null;
		List<Map<String, String>> listBuildingQuery = null;
		List<Map<String, String>> listInsuQuery = null;
		List<Map<String, String>> listStockQuery = null;

		List<Map<String, String>> listCoborrowerQuery = null;
		List<Map<String, String>> listShareQuotaQuery = null;

		try {
			listL9110 = l9110ServiceImpl.queryPerson(titaVo, thisApplNo);

			listCrossUseQuery = l9110ServiceImpl.queryCrossUse(titaVo, thisApplNo);
			listCustTelNoQuery = l9110ServiceImpl.queryCustTelNo(titaVo, thisApplNo);

			listGuaQuery = l9110ServiceImpl.queryGua(titaVo, thisApplNo);

			listClQuery = l9110ServiceImpl.queryCl(titaVo, thisApplNo);
			listLandQuery = l9110ServiceImpl.queryLand(titaVo, thisApplNo);
			listBuildingQuery = l9110ServiceImpl.queryBuilding(titaVo, thisApplNo);
			listInsuQuery = l9110ServiceImpl.queryInsu(titaVo, thisApplNo);
			listStockQuery = l9110ServiceImpl.queryStock(titaVo, thisApplNo);

			listCoborrowerQuery = l9110ServiceImpl.queryCoborrower(titaVo, thisApplNo);
			listShareQuotaQuery = l9110ServiceImpl.queryShareQuota(titaVo, thisApplNo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9110ServiceImpl.findAll error = " + errors.toString());
		}

		Map<String, String> tL9110 = null;

		if (listL9110 != null && !listL9110.isEmpty()) {
			tL9110 = listL9110.get(0);
		}

		Map<String, String> tL9110Cl = null;

		if (listClQuery != null && !listClQuery.isEmpty()) {
			tL9110Cl = listClQuery.get(0);
		}

		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ----------------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */

		this.print(-6, 1,
				"戶號 .........            　戶名 ..........　　　　　　　　　　　　　　　　　　　　　　　　　　　 統一編號 ..... 　　　　　　　　　　　　核准號碼 ..... ");

		if (tL9110 != null && tL9110.size() != 0) {
			// header fill-in
			this.print(-6, 16, tL9110.get("F0")); // 戶號 custNo - facmNo

			String[] tmpCustNo = tL9110.get("F0").split("-");

			int custNo = Integer.parseInt(tmpCustNo[0]);
			int facmNo = Integer.parseInt(tmpCustNo[1]);
			String achRelId = "";
			String achRelName = "";
			try {
				TempVo tempVo = authLogCom.exec(custNo, facmNo, titaVo);

				achRelId = tempVo.get("RelationId");
				achRelName = tempVo.get("RelationName");

			} catch (LogicException e) {
				this.error("L9110Report" + e.getMessage());
			}
			this.print(-6, 45, tL9110.get("F1")); // 戶名
			this.print(-6, 114, tL9110.get("F2")); // 統編
			this.print(-6, 153, tL9110.get("F3")); // 核准號碼

			String custId = tL9110.get("F2");

			if (achRelId != null && achRelId.equals(custId)) {
				achRelId = "";
				achRelName = "本人";
			} else if (achRelId == null) {
				achRelId = "";
				achRelName = "";
			}
			// 1
			this.print(1, 1, "一、基本資料：");
			this.print(1, 5, "負責人姓名 ... " + tL9110.get("F5"));
			this.print(0, 50, "負責人身分證 . " + tL9110.get("F6"));
			this.print(0, 99, "行業別 ....... " + tL9110.get("F7"));
			this.print(1, 5, "設立日期 ..... " + this.showRocDate(tL9110.get("F11"), 1));
			this.print(0, 50, "客戶別 ....... " + tL9110.get("F8"));
			this.print(1, 5, "公司地址 ..... " + tL9110.get("F12"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F13"));
			this.print(1, 5, "通訊地址 ..... " + tL9110.get("F14"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F15"));

			// 印交互運用
			printCrossUse(listCrossUseQuery);

			// 印電話
			printCustTel(listCustTelNoQuery);

			// 2
			this.print(1, 1, " ");

			String relateCustName = "資料未建立";

			// TODO:關聯戶???

			this.print(1, 1, "二、關聯戶戶名： " + relateCustName);

			// 3
			this.print(1, 1, " ");
			if (listGuaQuery == null || listGuaQuery.isEmpty()) {
				this.print(1, 1, "三、保證人資料： 資料未建立");
			} else {
				this.print(1, 1, "三、保證人資料：");
				printGuaDetail(listGuaQuery);
			}

			// 4
			this.print(1, 1, " ");
			this.print(1, 1, "四、核准資料：");
			this.print(1, 5, "鍵檔日期 ..... " + this.showRocDate(tL9110.get("F16"), 1));
			this.print(0, 35, "核准額度 ..... "); // amount is R-pined at 49
			this.print(0, 65, formatAmt(tL9110.get("F17"), 0), "R");
			this.print(0, 69, "核准科目 ..... " + tL9110.get("F18"));
			this.print(0, 105,
					"貸款期間 ..... " + tL9110.get("F19") + " 年 " + tL9110.get("F20") + " 月 " + tL9110.get("F21") + " 日");

			this.print(1, 5, "商品代碼 ..... " + tL9110.get("F22"));
			this.print(0, 35, "核准利率 ..... ");
			this.print(0, 65, formatAmt(tL9110.get("F23"), 4), "R");
			this.print(0, 69, "利率調整週期 . " + tL9110.get("F24") + "月");
			this.print(0, 105, "利率調整不變攤還額 . " + tL9110.get("F25"));
			this.print(0, 135, "信用評分　.... " + tL9110.get("F26"));

			this.print(1, 5, "動支期限 ..... " + showRocDate(tL9110.get("F27"), 1));
			this.print(0, 35, "利率加減碼 ... ");
			this.print(0, 65, formatAmt(tL9110.get("F28"), 3), "R");
			this.print(0, 69, "用途別 ....... " + tL9110.get("F29"));
			this.print(0, 105, "循環動用期限 . " + showRocDate(tL9110.get("F30"), 1));
			this.print(0, 135, "介紹人姓名 ... " + tL9110.get("F31"));

			this.print(1, 5, "代繳所得稅 ... " + tL9110.get("F32"));
			this.print(0, 35, "代償碼 ....... " + tL9110.get("F33"));
			this.print(0, 69, "攤還方式 ..... " + tL9110.get("F34"));
			this.print(0, 105, "寬限總月數 ... " + tL9110.get("F35") + "月");
			this.print(0, 135, "首次調整週期 . " + tL9110.get("F36") + "月");

			this.print(1, 5, "繳款方式 ..... " + tL9110.get("F37"));
			this.print(0, 35, "繳息週期 ..... " + tL9110.get("F40"));
			this.print(0, 69, "利率區分 ..... " + tL9110.get("F41"));
			this.print(0, 105, "客戶別 ....... " + tL9110.get("F51"));
			this.print(0, 135, "規定管制代碼 . " + tL9110.get("F56"));

			this.print(1, 5, "扣款銀行 ..... " + tL9110.get("F38"));
			this.print(0, 35, "扣款帳號 ..... " + tL9110.get("F39"));
			this.print(0, 69, "第三人帳戶統編 " + achRelId);
			this.print(0, 105, "第三人帳戶姓名 " + achRelName);
			this.print(0, 135, "團體戶名 ..... " + tL9110.get("F43"));

			this.print(1, 5, "計件代碼 ..... " + tL9110.get("F44"));
			this.print(0, 69, "火險服務姓名 . " + tL9110.get("F45"));
			this.print(0, 105, "放款專員 ..... " + tL9110.get("F46"));
			this.print(0, 135, "核決主管 ..... " + tL9110.get("F47"));

			this.print(1, 5, "帳管費 ....... ");
			this.print(0, 30, formatAmt(tL9110.get("F49"), 0), "R");
			this.print(0, 35, "協辦姓名 ..... " + tL9110.get("F54"));
			this.print(0, 69, "估價覆核姓名 . " + tL9110.get("F50"));
			this.print(0, 105, "徵信姓名 ..... " + tL9110.get("F52"));
			this.print(0, 135, "授信姓名 ..... " + tL9110.get("F53"));

			// 違約適用方式
			if (tL9110.get("F58").isEmpty()) {
				this.print(1, 5, "違約適用方式 . " + "無");
			} else if ("999".equals(tL9110.get("F58"))) {
				this.print(1, 5, "違約適用方式 . " + "無");
			} else {
				this.print(1, 5, "違約適用方式 . " + tL9110.get("F58") + " " + tL9110.get("F42"));
			}
			if (!tL9110.get("F58").isEmpty()) {
				this.print(1, 5, "綁約期限 ..... " + tL9110.get("F48") + " 個月");
				this.print(0, 35, "違約金收取方式 " + tL9110.get("F57"));
			}
			this.info(" 違約金收取方式 = " + tL9110.get("F57"));

			if (tL9110Cl != null) {
				this.print(1, 1, " ");
				this.print(1, 1, "五、擔保品資料： " + tL9110Cl.get("F0"));

				String clCode1 = "";
				// 擔保品不同時格式不同
				clCode1 = tL9110Cl.get("F11") == null ? "" : tL9110Cl.get("F11");
				switch (clCode1) {
				case "1": // 房地
				case "2": // 土地
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "鑑價日期 ..... " + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 105, "他項存續期限 . " + this.showRocDate(tL9110Cl.get("F4"), 1));
					this.print(0, 135, "鑑估總值 .....");
					this.print(0, 165, formatAmt(tL9110Cl.get("F17"), 0), "R");
					this.print(1, 5, "順位 ......... " + tL9110Cl.get("F5"));
					this.print(0, 35, "前順位金額 ...");
					this.print(0, 65, formatAmt(tL9110Cl.get("F6"), 0), "R");
					this.print(0, 135, "評估淨值 .....");
					this.print(0, 165, formatAmt(tL9110Cl.get("F21"), 0), "R");
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					this.print(0, 105, "鑑定公司 ..... " + tL9110Cl.get("F8"));
					this.print(1, 5, "建物標示備註 . " + tL9110Cl.get("F9"));
					this.print(0, 105, "設定日期 ..... " + this.showRocDate(tL9110Cl.get("F10"), 1));
					this.print(0, 135, "設定金額 .....");
					this.print(0, 165, formatAmt(tL9110Cl.get("F22"), 0), "R");

					if (listLandQuery != null && !listLandQuery.isEmpty()) {
						// 列印土地明細
						printLandDetail(listLandQuery, clCode1.equals("1") ? "0" : tL9110Cl.get("F19"));
					}

					if (listBuildingQuery != null && !listBuildingQuery.isEmpty()) {
						// 列印建物明細
						printBuildingDetail(listBuildingQuery, clCode1.equals("2") ? "0" : tL9110Cl.get("F19"));
					}

					break;
				case "3": // 股票
				case "4": // 有價證券
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					this.print(0, 105, "有價證券代號 . " + tL9110Cl.get("F12")); // F12 股票代號及股票名稱

					this.print(1, 5, "質權設定書號 . " + tL9110Cl.get("F13")); // F13 質權設定書號
					this.print(0, 35, "鑑價日期 ..... " + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 69, "三個月平均價 .. ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F14"), 2), "R"); // F14 三個月平均價
					this.print(0, 105, "前日收盤價 ... ");
					this.print(0, 130, formatAmt(tL9110Cl.get("F15"), 2), "R"); // F15 前日收盤價

					this.print(1, 5, "鑑定單價 ..... ");
					this.print(0, 30, formatAmt(tL9110Cl.get("F16"), 2), "R"); // F16 鑑定單價
					this.print(0, 35, "鑑定總價 ..... ");
					this.print(0, 64, formatAmt(tL9110Cl.get("F17"), 0), "R"); // F17 鑑定總價
					this.print(0, 69, "貸放成數 ..... ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F18"), 2) + "%", "R"); // F18 貸放成數
					this.print(0, 105, "核准額度 ..... ");
					this.print(0, 130, formatAmt(tL9110Cl.get("F19"), 0), "R"); // F19 核准額度

					this.print(1, 5, "保管條號碼 ... " + tL9110Cl.get("F20")); // F20 保管條號碼

					printStockDetail(listStockQuery);

					break;
				case "5": // 其他
					this.info("其他擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					break;
				case "9": // 動產
					this.info("動產擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					break;
				default:
					this.error("clCode1 is out of cases.");
					break;
				}
			} else {
				this.print(1, 1, " ");
				this.print(1, 1, "五、擔保品資料： 資料未建立");
			}

			// 6
			this.print(1, 1, " ");
			if (listInsuQuery != null && !listInsuQuery.isEmpty()) {
				this.print(1, 1, "六、保險資料：");
				// 列印保險資料
				printInsu(listInsuQuery);
			} else {
				this.print(1, 1, "六、保險資料： 資料未建立");
			}

			// 7
			this.print(1, 1, " ");
			this.print(1, 1, "七、本戶號目前總餘額： " + formatAmt(tL9110.get("F55"), 0) + "　元");

			// 8
			this.print(1, 1, " ");
			if (listCoborrowerQuery == null || listCoborrowerQuery.isEmpty()) {
				this.print(1, 1, "八、共同借款人資料： 資料未建立");
			} else {
				this.print(1, 1, "八、共同借款人資料：");
				printCoborrower(listCoborrowerQuery);
			}

			// 9
			this.print(1, 1, " ");
			if (listShareQuotaQuery == null || listShareQuotaQuery.isEmpty()) {
				this.print(1, 1, "九、合併額度控管資料： 資料未建立");
			} else {
				this.print(1, 1, "九、合併額度控管資料：");
				printShareQuota(listShareQuotaQuery);
			}

		} else {
			print(1, 1, "核准號碼" + thisApplNo + "無資料");
		}
	}

	/**
	 * 自然人資料
	 *
	 * @param titaVo TitaVo
	 * @throws LogicException
	 */
	private void fillDataNaturalPerson(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listL9110 = null;

		List<Map<String, String>> listCrossUseQuery = null;
		List<Map<String, String>> listCustTelNoQuery = null;

		List<Map<String, String>> listGuaQuery = null;

		List<Map<String, String>> listClQuery = null;
		List<Map<String, String>> listLandQuery = null;
		List<Map<String, String>> listBuildingQuery = null;
		List<Map<String, String>> listInsuQuery = null;
		List<Map<String, String>> listStockQuery = null;

		List<Map<String, String>> listCoborrowerQuery = null;
		List<Map<String, String>> listShareQuotaQuery = null;

		try {
			listL9110 = l9110ServiceImpl.queryPerson(titaVo, thisApplNo);

			listCrossUseQuery = l9110ServiceImpl.queryCrossUse(titaVo, thisApplNo);
			listCustTelNoQuery = l9110ServiceImpl.queryCustTelNo(titaVo, thisApplNo);

			listGuaQuery = l9110ServiceImpl.queryGua(titaVo, thisApplNo);

			listClQuery = l9110ServiceImpl.queryCl(titaVo, thisApplNo);
			listLandQuery = l9110ServiceImpl.queryLand(titaVo, thisApplNo);
			listBuildingQuery = l9110ServiceImpl.queryBuilding(titaVo, thisApplNo);
			listInsuQuery = l9110ServiceImpl.queryInsu(titaVo, thisApplNo);
			listStockQuery = l9110ServiceImpl.queryStock(titaVo, thisApplNo);

			listCoborrowerQuery = l9110ServiceImpl.queryCoborrower(titaVo, thisApplNo);
			listShareQuotaQuery = l9110ServiceImpl.queryShareQuota(titaVo, thisApplNo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9110ServiceImpl.findAll error = " + errors.toString());
		}

		Map<String, String> tL9110 = listL9110.size() > 0 ? listL9110.get(0) : null;
		Map<String, String> tL9110Cl = listClQuery.size() > 0 ? listClQuery.get(0) : null;

		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ----------------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */

		this.print(-6, 1,
				"戶號 .........            　戶名 ..........　　　　　　　　　　　　　　　　　　　　　　　　　　　 統一編號 ..... 　　　　　　　　　　　　核准號碼 ..... ");

		if (tL9110 != null && tL9110.size() != 0) {
			// header fill-in
			this.print(-6, 16, tL9110.get("F0"));// 戶號 CustNo - FacmNo

			String custNoFacmNo = tL9110.get("F0");
			String[] tmpString = custNoFacmNo.split("-");

			String achRelId = "";
			String achRelName = "";
			if (tmpString != null && tmpString.length >= 2) {
				int custNo = Integer.parseInt(tmpString[0]);
				int facmNo = Integer.parseInt(tmpString[1]);

				try {
					TempVo tempVo = authLogCom.exec(custNo, facmNo, titaVo);

					achRelId = tempVo.get("RelationId");
					achRelName = tempVo.get("RelationName");
				} catch (LogicException e) {
					this.error("L9110Report" + e.getMessage());
				}
			}

			this.print(-6, 45, tL9110.get("F1")); // 戶名
			this.print(-6, 114, tL9110.get("F2")); // 統編

			String custId = tL9110.get("F2");

			if (achRelId != null && achRelId.equals(custId)) {
				achRelId = "";
				achRelName = "本人";
			} else if (achRelId == null) {
				achRelId = "";
				achRelName = "";
			}

			this.print(-6, 153, tL9110.get("F3")); // 核准編號

			// 1
			this.print(1, 1, "一、基本資料：");
			this.print(1, 5, "性別 ......... " + tL9110.get("F4"));
			this.print(0, 50, "出生年月日 ... " + this.showRocDate(tL9110.get("F11"), 1));
			this.print(0, 99, "客戶別 ....... " + tL9110.get("F8"));
			this.print(1, 5, "員工代號 ..... " + tL9110.get("F9"));
			// TODO:15日
			CdEmp tCdEmp = sCdEmpService.findById(tL9110.get("F9"), titaVo);
			if (tCdEmp != null) {
				if (employeeCom.isDay15Salary(tCdEmp, titaVo)) {
					this.print(0, 50, "15 日薪 ...... " + "十五日薪");
				} else {
					this.print(0, 50, "15 日薪 ...... " + "非十五日薪");
				}
			} else {
				this.print(0, 50, "15 日薪 ...... " + tL9110.get("F10"));
			}
			this.print(0, 99, "配偶姓名 ..... " + tL9110.get("F5"));
			this.print(0, 138, "配偶統一編號 . " + tL9110.get("F6"));
			this.print(1, 5, "戶籍地址 ..... " + tL9110.get("F12"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F13"));
			this.print(1, 5, "通訊地址 ..... " + tL9110.get("F14"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F15"));

			// 印交互運用
			printCrossUse(listCrossUseQuery);

			// 印電話
			printCustTel(listCustTelNoQuery);

			// 2
			this.print(1, 1, " ");
			if (listGuaQuery == null || listGuaQuery.isEmpty()) {
				this.print(1, 1, "二、保證人資料： 資料未建立");
			} else {
				this.print(1, 1, "二、保證人資料：");
				printGuaDetail(listGuaQuery);
			}

			// 3
			this.print(1, 1, " ");
			this.print(1, 1, "三、核准資料：");
			this.print(1, 5, "鍵檔日期 ..... " + this.showRocDate(tL9110.get("F16"), 1));
			this.print(0, 35, "核准額度 ..... "); // amount is R-pined at 49
			this.print(0, 65, formatAmt(tL9110.get("F17"), 0), "R");
			this.print(0, 69, "核准科目 ..... " + tL9110.get("F18"));
			this.print(0, 105,
					"貸款期間 ..... " + tL9110.get("F19") + " 年 " + tL9110.get("F20") + " 月 " + tL9110.get("F21") + " 日");

			this.print(1, 5, "商品代碼 ..... " + FormatUtil.padX("" + tL9110.get("F22") + " " + tL9110.get("F59"), 14));
			this.print(0, 35, "核准利率 ..... ");
			this.print(0, 65, formatAmt(parse.stringToBigDecimal(tL9110.get("F23")), 4), "R");
			this.print(0, 69, "利率調整週期 . " + tL9110.get("F24") + "月");
			this.print(0, 105, "利率調整不變攤還額 . " + tL9110.get("F25"));
			this.print(0, 135, "信用評分　.... " + tL9110.get("F26"));

			this.print(1, 5, "動支期限 ..... " + this.showRocDate(tL9110.get("F27"), 1));
			this.print(0, 35, "利率加減碼 ... ");
			this.print(0, 65, formatAmt(tL9110.get("F28"), 3), "R");
			this.print(0, 69, "用途別 ....... " + tL9110.get("F29"));
			this.print(0, 105, "循環動用期限 . " + this.showRocDate(tL9110.get("F30"), 1));
			this.print(0, 135, "介紹人姓名 ... " + tL9110.get("F31"));

			this.print(1, 5, "代繳所得稅 ... " + tL9110.get("F32"));
			this.print(0, 35, "代償碼 ....... " + tL9110.get("F33"));
			this.print(0, 69, "攤還方式 ..... " + tL9110.get("F34"));
			this.print(0, 105, "寬限總月數 ... " + tL9110.get("F35") + "月");
			this.print(0, 135, "首次調整週期 . " + tL9110.get("F36") + "月");

			this.print(1, 5, "繳款方式 ..... " + tL9110.get("F37"));
			this.print(0, 35, "繳息週期 ..... " + tL9110.get("F40"));
			this.print(0, 69, "利率區分 ..... " + tL9110.get("F41"));
			this.print(0, 105, "客戶別 ....... " + tL9110.get("F51"));
			this.print(0, 135, "規定管制代碼 . " + tL9110.get("F56"));

			this.print(1, 5, "扣款銀行 ..... " + tL9110.get("F38"));
			this.print(0, 35, "扣款帳號 ..... " + tL9110.get("F39"));
			this.print(0, 69, "第三人帳戶統編 " + achRelId);
			this.print(0, 105, "第三人帳戶姓名 " + achRelName);
			this.print(0, 135, "團體戶名 ..... " + tL9110.get("F43"));

			this.print(1, 5, "計件代碼 ..... " + tL9110.get("F44"));
			this.print(0, 69, "火險服務姓名 . " + tL9110.get("F45"));
			this.print(0, 105, "放款專員 ..... " + tL9110.get("F46"));
			this.print(0, 135, "核決主管 ..... " + tL9110.get("F47"));

			this.print(1, 5, "帳管費 ....... ");
			this.print(0, 30, formatAmt(tL9110.get("F49"), 0), "R");
			this.print(0, 35, "協辦姓名 ..... " + tL9110.get("F54"));
			this.print(0, 69, "估價覆核姓名 . " + tL9110.get("F50"));
			this.print(0, 105, "徵信姓名 ..... " + tL9110.get("F52"));
			this.print(0, 135, "授信姓名 ..... " + tL9110.get("F53"));
			// 違約適用方式
			if (tL9110.get("F58").isEmpty()) {
				this.print(1, 5, "違約適用方式 . " + "無");
			} else if ("999".equals(tL9110.get("F58"))) {
				this.print(1, 5, "違約適用方式 . " + "無");
			} else {
				this.print(1, 5, "違約適用方式 . " + tL9110.get("F58") + " " + tL9110.get("F42"));
			}
			this.print(1, 5, "綁約期限 ..... " + tL9110.get("F48") + " 個月");
			this.print(0, 35, "違約金收取方式 " + tL9110.get("F57"));

			this.info(" 違約金收取方式 = " + tL9110.get("F57"));

			// 4
			if (tL9110Cl != null) {
				this.print(1, 1, " ");
				this.print(1, 1, "四、擔保品資料： " + tL9110Cl.get("F0"));

				String clCode1 = "";

				// 擔保品不同時格式不同
				clCode1 = tL9110Cl.get("F11") == null ? "" : tL9110Cl.get("F11");
				switch (clCode1) {
				case "1": // 房地
				case "2": // 土地
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "鑑價日期 ..... " + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 105, "他項存續期限 . " + this.showRocDate(tL9110Cl.get("F4"), 1));
					this.print(0, 135, "鑑估總值 .....");
					this.print(0, 165, formatAmt(tL9110Cl.get("F17"), 0), "R");
					this.print(1, 5, "順位 ......... " + tL9110Cl.get("F5"));
					this.print(0, 35, "前順位金額 ...");
					this.print(0, 65, formatAmt(tL9110Cl.get("F6"), 0), "R");
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					this.print(0, 105, "鑑定公司 ..... " + tL9110Cl.get("F8"));
					this.print(0, 135, "評估淨值 .....");
					this.print(0, 165, formatAmt(tL9110Cl.get("F21"), 0), "R");
					this.print(1, 5, "建物標示備註 . " + tL9110Cl.get("F9"));
					this.print(0, 105, "設定日期 ..... " + this.showRocDate(tL9110Cl.get("F10"), 1));
					this.print(0, 135, "設定金額 ...");
					this.print(0, 165, formatAmt(tL9110Cl.get("F22"), 0), "R");

					if (listLandQuery != null && !listLandQuery.isEmpty()) {
						// 列印土地明細
						printLandDetail(listLandQuery, clCode1.equals("1") ? "0" : tL9110Cl.get("F19"));
					}

					if (listBuildingQuery != null && !listBuildingQuery.isEmpty()) {
						// 列印建物明細
						printBuildingDetail(listBuildingQuery, clCode1.equals("2") ? "0" : tL9110Cl.get("F19"));
					}
					break;
				case "3": // 股票
				case "4": // 有價證券
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					this.print(0, 105, "有價證券代號 . " + tL9110Cl.get("F12")); // F12 股票代號及股票名稱

					this.print(1, 5, "質權設定書號 . " + tL9110Cl.get("F13")); // F13 質權設定書號
					this.print(0, 35, "鑑價日期 ..... " + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 69, "三個月平均價 .. ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F14"), 2), "R"); // F14 三個月平均價
					this.print(0, 105, "前日收盤價 ... ");
					this.print(0, 130, formatAmt(tL9110Cl.get("F15"), 2), "R"); // F15 前日收盤價

					this.print(1, 5, "鑑定單價 ..... ");
					this.print(0, 30, formatAmt(tL9110Cl.get("F16"), 2), "R"); // F16 鑑定單價
					this.print(0, 35, "鑑定總價 ..... ");
					this.print(0, 60, formatAmt(tL9110Cl.get("F17"), 0), "R"); // F17 鑑定總價
					this.print(0, 69, "貸放成數 ..... ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F18"), 2) + "%", "R"); // F18 貸放成數
					this.print(0, 105, "核准額度 ..... ");
					this.print(0, 130, formatAmt(tL9110Cl.get("F19"), 0), "R"); // F19 核准額度

					this.print(1, 5, "保管條號碼 ... " + tL9110Cl.get("F20")); // F20 保管條號碼

					printStockDetail(listStockQuery);

					break;
				case "5": // 其他
					this.info("其他擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					break;
				case "9": // 動產
					this.info("動產擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 ..... " + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 ....... " + tL9110Cl.get("F7"));
					break;
				default:
					this.error("clCode1 is out of cases.");
					break;
				}
			} else {
				this.print(1, 1, " ");
				this.print(1, 1, "四、擔保品資料：　資料未建立");
			}

			// 5
			if (listInsuQuery == null || listInsuQuery.isEmpty()) {
				this.print(1, 1, "五、保險資料： 資料未建立");
			} else {
				this.print(1, 1, "五、保險資料：");
				// 列印保險資料
				printInsu(listInsuQuery);
			}

			// 6
			this.print(1, 1, " ");
			this.print(1, 1, "六、本戶號目前總餘額： " + formatAmt(tL9110.get("F55"), 0) + "　元");

			// 7
			this.print(1, 1, " ");
			if (listCoborrowerQuery == null || listCoborrowerQuery.isEmpty()) {
				this.print(1, 1, "七、共同借款人資料： 資料未建立");
			} else {
				this.print(1, 1, "七、共同借款人資料：");
				printCoborrower(listCoborrowerQuery);
			}

			// 8
			this.print(1, 1, " ");
			if (listShareQuotaQuery == null || listShareQuotaQuery.isEmpty()) {
				this.print(1, 1, "八、合併額度控管資料： 資料未建立");
			} else {
				this.print(1, 1, "八、合併額度控管資料：");
				printShareQuota(listShareQuotaQuery);
			}
		} else {
			print(1, 1, "核准號碼" + thisApplNo + "無資料");
		}
	}

	public void makePdf(TitaVo titaVo, TotaVo totaVo) throws LogicException {
		// 名稱固定為查詢的第一筆額度編號
		String fileReportItem = reportItem.concat(titaVo.getParam("APPLNO1"));

		this.open(titaVo, reportDate, brno, reportCode, fileReportItem, security, pageSize, pageOrientation);
		this.setCharSpaces(0);

		// 2022-06-17 Wei:應珮琪要求照AS400畫面增加欄位"列印條件選擇"
		// 1:未撥款;2:已撥款;3:全部
		int choice = Integer.parseInt(titaVo.getParam("Choice"));

		for (int currentApplNoItem = 1; currentApplNoItem <= 50; currentApplNoItem++) {
			thisApplNo = titaVo.getParam("APPLNO" + currentApplNoItem);

			BigDecimal loanBal = BigDecimal.ZERO;

			if (thisApplNo != null && !(thisApplNo.isEmpty()) && Integer.parseInt(thisApplNo) > 0) {

				this.info("thisApplNo = " + thisApplNo);
				// 每次查詢都須確定企金別以決定輸出方式
				FacMain tFacMain = sFacMainService.facmApplNoFirst(Integer.parseInt(thisApplNo), titaVo);

				CustMain tCustMain = null;

				if (tFacMain == null) {
					this.warn("L9110Report thisApplNo(" + thisApplNo + ")  is not found in FacMain. ");
					FacCaseAppl tFacCaseAppl = sFacCaseApplService.findById(Integer.parseInt(thisApplNo), titaVo);

					if (tFacCaseAppl != null) {
						tCustMain = sCustMainService.findById(tFacCaseAppl.getCustUKey(), titaVo);
					} else {
						this.warn("L9110Report thisApplNo(" + thisApplNo + ")  is not found in FacCaseAppl. ");
					}
				} else {
					loanBal = tFacMain.getUtilBal();
					tCustMain = sCustMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
				}

				if (tCustMain != null) {
					custEntCode = tCustMain.getEntCode();
				} else {
					this.warn("L9110Report thisApplNo(" + thisApplNo + ")'s CustUKey is not found in CustMain. ");
				}

				// 企金別無值時，預設為0:個金
				if (custEntCode == null || custEntCode.isEmpty()) {
					custEntCode = "0";
				}

				this.info("L9110Report : " + thisApplNo + ", This applNo is..." + (custEntCode.equals("1") ? "" : "not")
						+ " a legal person");

				if (currentApplNoItem > 1) {
					this.newPage();
				}

				// 2022-06-17 Wei:應珮琪要求照AS400畫面增加欄位"列印條件選擇"
				// 1:未撥款;2:已撥款;3:全部
				if (choice == 1 && loanBal.compareTo(BigDecimal.ZERO) != 0) {
					// 要篩選未撥款,但此筆撥款餘額!=0,跳過
					continue;
				} else if (choice == 2 && loanBal.compareTo(BigDecimal.ZERO) == 0) {
					// 要篩選已撥款,但此筆撥款餘額=0,跳過
					continue;
				}

				fillData(titaVo, custEntCode.equals("1"));

			}
		}

		isLastPage = true;
		printFooter();

		long rptNo = this.close();
		totaVo.put("PdfSnoF", Long.toString(rptNo));
		this.toPdf(rptNo, this.reportCode + "_" + fileReportItem);
	}

	/**
	 * 列印建物明細(自然人、法人共用)
	 *
	 * @param listBuildingQuery 建物明細查詢結果
	 */
	private void printBuildingDetail(List<Map<String, String>> listBuildingQuery, String lineAmt) {

		/**
		 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		checkSpace(5);
		print(1, 5, "　　　　　　　　　　　　　　　　　　　　　　　　　　主建物　　　公設　　　（坪）　　 （坪） 　(坪)　　(坪)　　(仟／坪)　(仟)　　(仟)");
		print(1, 5, "序號　提供人／門牌號碼　　　　　　　　　　　　　　　建　號　　　建號　　　主建物　　附屬建物　公設　　車位　　鑑定單價　核貸　　設定　　　賣方姓名　　　賣方 ID");
		print(1, 5, "－－　－－－－－－－－－－－－－－－－－－－－－　－－－－－　－－－－－　－－－－　－－－－　－－－　－－－　－－－－　－－－　－－－　－－－－－－　－－－－－");

		BigDecimal totalFloorArea = BigDecimal.ZERO;
		BigDecimal totalBdSubArea = BigDecimal.ZERO;
		BigDecimal totalPublicArea = BigDecimal.ZERO;
		BigDecimal totalCarArea = BigDecimal.ZERO;
//		BigDecimal totalSettingAmt = BigDecimal.ZERO;

		BigDecimal thousand = getBigDecimal("1000");

		for (Map<String, String> mBuilding : listBuildingQuery) {

			checkSpace(2);
			this.print(1, 9, mBuilding.get("F0"), "R"); // 序號
			this.print(0, 11, mBuilding.get("F1")); // 提供人
			this.print(0, 55, mBuilding.get("F2")); // 主建物建號
			this.print(0, 67, mBuilding.get("F3")); // 公設建號

			this.print(0, 87, formatAmt(mBuilding.get("F4"), 2), "R"); // 主建物面積
			totalFloorArea = totalFloorArea.add(getBigDecimal(mBuilding.get("F4")));

			// 2022-04-22 智偉新增:附屬建物面積
			this.print(0, 97, formatAmt(mBuilding.get("F12"), 2), "R"); // 附屬建物面積
			totalBdSubArea = totalBdSubArea.add(getBigDecimal(mBuilding.get("F12")));

			this.print(0, 105, formatAmt(mBuilding.get("F5"), 2), "R"); // 公設面積
			totalPublicArea = totalPublicArea.add(getBigDecimal(mBuilding.get("F5")));

			this.print(0, 113, formatAmt(mBuilding.get("F6"), 2), "R"); // 車位面積
			totalCarArea = totalCarArea.add(getBigDecimal(mBuilding.get("F6")));

			this.print(0, 123, formatAmt(computeDivide(getBigDecimal(mBuilding.get("F7")), thousand, 0), 0), "R"); // 鑑定單價

			this.print(0, 131, formatAmt(computeDivide(getBigDecimal(lineAmt), thousand, 0), 0), "R"); // 核貸

			this.print(0, 139, formatAmt(computeDivide(getBigDecimal(mBuilding.get("F8")), thousand, 0), 0), "R");// 設定金額
//			totalSettingAmt = totalSettingAmt.add(getBigDecimal(mBuilding.get("F8")));

			this.print(0, 141, mBuilding.get("F10")); // 賣方姓名
			this.print(0, 155, mBuilding.get("F11")); // 賣方ID

			this.print(1, 11, mBuilding.get("F9")); // 門牌號碼
			this.print(0, 55,
					"擔保品編號：　" + mBuilding.get("F13") + "-" + mBuilding.get("F14") + "-" + mBuilding.get("F15")); // 擔保品編號
		}

		checkSpace(2);
		print(1, 5, "－－　－－－－－－－－－－－－－－－－－－－－－　－－－－－　－－－－－　－－－－　－－－－　－－－　－－－　－－－－　－－－　－－－　－－－－－－　－－－－－");
		print(1, 5, " 建物合計：");
		print(0, 87, formatAmt(totalFloorArea, 2), "R");
		print(0, 97, formatAmt(totalBdSubArea, 2), "R");
		print(0, 105, formatAmt(totalPublicArea, 2), "R");
		print(0, 113, formatAmt(totalCarArea, 2), "R");
		// 2022-04-25 智偉:改為不顯示加總的設定金額
//		print(0, 141, formatAmt(computeDivide(totalSettingAmt, thousand, 0), 0), "R");
	}

	/**
	 * 列印共同借款人資料
	 *
	 * @param listCoborrowerQuery 共同借款人查詢結果
	 */
	private void printCoborrower(List<Map<String, String>> listCoborrowerQuery) {
		this.info("printCoborrower");

		this.checkSpace(4);
		/**
		 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		print(1, 5, "　　　　　　　　　　　　　　　　　　　循環動用　　　　　　　　　　　　　　　　　　　　　　　　　　　是否");
		print(1, 5, "戶號　　　　　戶名　　　　　　　　　　動支期限　　幣別　核准額度　　　　已動用額度餘額　循環動用　合併申報");
		print(1, 5, "－－－－－－　－－－－－－－－－－－　－－－－－　－－　－－－－－－－　－－－－－－－　－－－－　－－－－");

		for (Map<String, String> queryCoborrower : listCoborrowerQuery) {
			print(1, 5, queryCoborrower.get("F0")); // 戶號
			print(0, 19, queryCoborrower.get("F1")); // 戶名
			print(0, 43, showRocDate(queryCoborrower.get("F2"), 1)); // 循環動用動支期限
			print(0, 55, queryCoborrower.get("F3")); // 幣別
			print(0, 75, formatAmt(queryCoborrower.get("F4"), 0), "R"); // 核准額度
			print(0, 91, formatAmt(queryCoborrower.get("F5"), 0), "R"); // 已動用額度餘額
			print(0, 93, queryCoborrower.get("F6")); // 循環動用
			print(0, 103, queryCoborrower.get("F7")); // 是否合併申報
		}
	}

	/**
	 * 列印交互運用資料
	 *
	 * @param listCrossUseQuery 交互運用查詢結果
	 */
	private void printCrossUse(List<Map<String, String>> listCrossUseQuery) {
		if (listCrossUseQuery == null || listCrossUseQuery.isEmpty()) {
			this.print(1, 5, "交互運用 ..... 無");
		} else {
			this.print(1, 5, "交互運用 ..... ");

			int i = 0;
			for (Map<String, String> queryCrossUse : listCrossUseQuery) {
				String subCompany = queryCrossUse.get("F0");

				this.print(i, 20, subCompany);
				i = 1; // 第一次為0, 第二次以後印新的一行
			}
		}
	}

	/**
	 * 列印客戶聯絡電話
	 *
	 * @param listCustTelNoQuery 客戶聯絡電話查詢結果
	 */
	private void printCustTel(List<Map<String, String>> listCustTelNoQuery) {
		this.info("printCustTel");

		checkSpace(3);
		this.print(1, 3, "　電話種類　區碼　　　電話號碼　　　分機　與借款人關係　聯絡人姓名");
		this.print(1, 3, "　－－－－　－－　－－－－－－－－　－－　－－－－－－　－－－－－");

		if (listCustTelNoQuery == null || listCustTelNoQuery.isEmpty()) {
			this.print(1, 5, "資料未建立");
		} else {
			for (Map<String, String> queryCustTel : listCustTelNoQuery) {
				this.print(1, 5, queryCustTel.get("F0")); // 電話種類
				this.print(0, 15, queryCustTel.get("F1")); // 電話區碼
				this.print(0, 21, queryCustTel.get("F2")); // 電話號碼
				this.print(0, 39, queryCustTel.get("F3")); // 分機號碼
				this.print(0, 45, queryCustTel.get("F4")); // 與借款人關係
				this.print(0, 59, queryCustTel.get("F5")); // 聯絡人姓名
			}
		}
	}

	/**
	 * 列印保證人明細(自然人、法人共用)
	 *
	 * @param listGuaQuery 保證人明細查詢結果
	 */
	private void printGuaDetail(List<Map<String, String>> listGuaQuery) {

		for (Map<String, String> tGua : listGuaQuery) {
			checkSpace(2);
			this.print(1, 5, "統一編號 ..... " + tGua.get("F0"));
			this.print(0, 35, "姓名 ......... " + tGua.get("F1"));
			this.print(0, 69, "關係 ......... " + tGua.get("F2"));
			this.print(0, 105, "類別 ......... " + tGua.get("F6")); // 保證類別最長有十個中文字,把保證金額移到第二行

			this.print(1, 5, "通訊地址 ..... " + tGua.get("F4"));
			this.print(0, 105, "郵遞區號 ..... " + tGua.get("F5"));
			this.print(0, 135, "保証金額 ..... " + formatAmt(tGua.get("F3"), 0));
		}
	}

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9110Report.printHeader");

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-2, 1, "報　表：" + this.getRptCode());

		int rpad = 18;
		this.print(-1, newBorder.length() - rpad, "機密等級：" + this.security);
		this.print(-2, newBorder.length() - rpad, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, newBorder.length() - rpad, "時　　間：" + showTime(this.nowTime));
		this.print(-4, newBorder.length() - rpad, "頁　　數：" + this.getNowPage());

		this.print(-2, (newBorder.length() + 2) / 2, "新光人壽保險股份有限公司", "C");
		this.print(-3, (newBorder.length() + 2) / 2, this.reportItem.concat(custEntCode.equals("1") ? "（法人）" : "（自然人）"),
				"C");

		this.setBeginRow(thisBeginRow);
		this.setMaxRows(thisMaxRow);
	}

	// 自訂表頭
	@Override
	public void printFooter() {
		if (isLastPage) {
			print(-47, 1, "副總　　　　　　　　資協　　　　　　　　協理　　　　　　　　經理　　　　　　　　撥款覆核　　　　　　　　放款專員　　　　　　　　製表人", "L");
		} else {
			print(-47, (newBorder.length() / 2) + 2, "=====　續下頁　=====", "C");
		}
	}

	/**
	 * 列印保險明細(自然人、法人共用)
	 *
	 * @param listInsuQuery 保險明細查詢結果
	 */
	private void printInsu(List<Map<String, String>> listInsuQuery) {
		checkSpace(3);
		/**
		 * --------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * -----------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		this.print(1, 5, " 序號　保單號碼　　　　　　　 火險金額        地震險金額      保險起日　 保險迄日　 保險公司");
		this.print(1, 5,
				"------------------------------------------------------------------------------------------------------------");
		for (Map<String, String> mInsu : listInsuQuery) {
			this.print(1, 8, mInsu.get("F0"), "R");
			this.print(0, 12, mInsu.get("F1"));
			this.print(0, 43, formatAmt(mInsu.get("FireInsuAmt"), 0), "R");
			this.print(0, 61, formatAmt(mInsu.get("EarthInsuAmt"), 0), "R");
			this.print(0, 67, this.showRocDate(mInsu.get("F3"), 1));
			this.print(0, 78, this.showRocDate(mInsu.get("F4"), 1));
			this.print(0, 89, mInsu.get("F5"));
		}
	}

	/**
	 * 列印土地明細(自然人、法人共用)
	 *
	 * @param listLandQuery 土地明細查詢結果
	 */
	private void printLandDetail(List<Map<String, String>> listLandQuery, String lineAmt) {

		// land
		BigDecimal totalArea = new BigDecimal(0);
		BigDecimal totalLastTransferred = new BigDecimal(0);
//		BigDecimal totalSettingAmt = new BigDecimal(0);

		BigDecimal thousand = getBigDecimal("1000");

		/**
		 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		checkSpace(4);
		print(1, 5, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　（坪）　　　　　　 （元） 　　　（仟／坪）　　 （仟）　　　 （仟）");
		print(1, 5, "序號　提供人／擔保品編號　　　　　　　　縣市　　鄉鎮區　　　　段小段　　　　地號　　　　 面積 　　年度　　前次移轉　　　 鑑定單價 　　  核貸　　　　 設定");
		print(1, 5, "－－　－－－－－－－－－－－－－－－　－－－－　－－－－　－－－－－－－　－－－－－　－－－－－　－－　－－－－－－－　－－－－－　－－－－－－　－－－－－－");
		for (Map<String, String> mLand : listLandQuery) {
			BigDecimal ownerPart = new BigDecimal(mLand.get("F11"));
			BigDecimal ownerTotal = new BigDecimal(mLand.get("F12"));
			BigDecimal ownerPartTotal = BigDecimal.ZERO;
			if (ownerPart.compareTo(BigDecimal.ZERO) == 0 && ownerTotal.compareTo(BigDecimal.ZERO) == 0) {
				ownerPartTotal = BigDecimal.ZERO;
			} else {
				ownerPartTotal = ownerPart.divide(ownerTotal);
			}
			this.info("F11 =" + mLand.get("F11") + ownerPart);
			this.info("F12 =" + mLand.get("F12") + ownerTotal);
			this.info("F6 =" + mLand.get("F6"));

			this.print(1, 9, mLand.get("F0"), "R"); // 序號
			this.print(0, 11, mLand.get("F1")); // 提供人
			this.print(0, 43, mLand.get("F2")); // 縣市
			this.print(0, 53, mLand.get("F3")); // 鄉鎮區
			this.print(0, 63, mLand.get("F4")); // 段小段
			this.print(0, 79, mLand.get("F5")); // 地號
//			this.print(0, 101, formatAmt(mLand.get("F6"), 2), "R"); // 面積
			BigDecimal Area = new BigDecimal(mLand.get("F6")).multiply(ownerPartTotal);
			this.print(0, 101, formatAmt(Area, 2), "R"); // 面積

			String f7 = "";

			try {
				f7 = Integer.toString(Integer.parseInt(mLand.get("F7")) - 1911);
			} catch (Exception e) {
				this.warn(thisApplNo + "'s land " + mLand.get("F0") + " got a wrong F7 input, " + e);
			}

			this.print(0, 107, f7, "R"); // 年度
			this.print(0, 123, formatAmt(mLand.get("F8"), 0), "R"); // 前次移轉
			this.print(0, 135, formatAmt(computeDivide(getBigDecimal(mLand.get("F9")), thousand, 0), 0), "R"); // 鑑定單價
			this.print(0, 149, formatAmt(computeDivide(getBigDecimal(lineAmt), thousand, 0), 0), "R"); // 核貸
			this.print(0, 163, formatAmt(computeDivide(getBigDecimal(mLand.get("F10")), thousand, 0), 0), "R"); // 設定
			this.print(1, 11, mLand.get("F13") + "-" + mLand.get("F14") + "-" + mLand.get("F15")); // 擔保品編號

			totalArea = totalArea.add(Area);
			totalLastTransferred = totalLastTransferred.add(getBigDecimal(mLand.get("F8")));
//			totalSettingAmt = totalSettingAmt.add(getBigDecimal(mLand.get("F10")));

		}
		checkSpace(2);
		print(1, 5, "－－　－－－－－－－－－－－－－－－　－－－－　－－－－　－－－－－－－　－－－－－　－－－－－　－－　－－－－－－－　－－－－－　－－－－－－　－－－－－－");
		print(1, 5, "土地合計：");
		print(0, 101, formatAmt(totalArea, 2), "R"); // 合計-面積
		print(0, 123, formatAmt(totalLastTransferred, 0), "R"); // 合計-前次移轉
		// 2022-04-25 智偉:改為不顯示加總的設定金額
//		print(0, 149, formatAmt(computeDivide(totalSettingAmt, thousand, 0), 0), "R"); // 合計-設定
	}

	/**
	 * 列印合併額度控管資料
	 *
	 * @param listShareQuotaQuery 合併額度控管查詢結果
	 */
	private void printShareQuota(List<Map<String, String>> listShareQuotaQuery) {

		this.info("printShareQuota");

		List<String> listMainApplNoCCY = new ArrayList<>();

		for (Map<String, String> queryShareQuota : listShareQuotaQuery) {

			String mainApplNo = queryShareQuota.get("F0");
			String ccy = queryShareQuota.get("F1");

			if (!listMainApplNoCCY.contains(mainApplNo + ccy)) {
				listMainApplNoCCY.add(mainApplNo + ccy);
			}
		}

		this.info("listMainApplNo size = " + listMainApplNoCCY.size());

		for (String mainApplNoCCY : listMainApplNoCCY) {

			int i = 0;

			for (Map<String, String> queryShareQuota : listShareQuotaQuery) {

				String tmpMainApplNo = queryShareQuota.get("F0"); // F0 主要核准號碼
				String tmpCCY = queryShareQuota.get("F1"); // F1 主要核准號碼之幣別

				if (mainApplNoCCY.equals(tmpMainApplNo + tmpCCY)) {

					if (i == 0) {
						// 列印合併額度控管之加總資料
						print(1, 5, "幣別 ........... " + tmpCCY);

						print(1, 5, "總額度 ......... ");
						print(0, 40, formatAmt(queryShareQuota.get("F2"), 0), "R"); // F2 核准額度加總

						print(1, 5, "已動用額度餘額 . ");
						print(0, 40, formatAmt(queryShareQuota.get("F3"), 0), "R"); // F3 已動用額度餘額加總

						print(1, 5, "目前餘額 ....... ");
						print(0, 40, formatAmt(queryShareQuota.get("F4"), 0), "R"); // F4 貸出金額(放款餘額、目前餘額)加總

						// 可用額度 = 總額度 - 已動用額度餘額

						BigDecimal availableAmt = getBigDecimal(queryShareQuota.get("F2"))
								.subtract(getBigDecimal(queryShareQuota.get("F3")));

						// 負數時擺0

						if (availableAmt.compareTo(BigDecimal.ZERO) < 0) {
							availableAmt = BigDecimal.ZERO;
						}

						print(1, 5, "可用額度 ....... ");
						print(0, 40, formatAmt(availableAmt, 0), "R");

						checkSpace(4);
						/**
						 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
						 * ------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
						 */
						print(1, 5, "　　　　　　　　　　　　　　　　　　　循環動用");
						print(1, 5, "戶號　　　　　戶名　　　　　　　　　　動支期限　　幣別　核准額度　　　　已動用額度餘額　目前餘額　　　　循環動用");
						print(1, 5, "－－－－－－　－－－－－－－－－－－　－－－－－　－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－");

						// 列印過增加i,使得加總資料只印一次
						i++;
					}

					// 列印合併額度控管之明細資料
					print(1, 5, queryShareQuota.get("F5")); // 戶號
					print(0, 19, queryShareQuota.get("F6")); // 戶名
					print(0, 43, showRocDate(queryShareQuota.get("F7"), 1)); // 循環動用動支期限
					print(0, 55, queryShareQuota.get("F8")); // 幣別
					print(0, 75, formatAmt(queryShareQuota.get("F9"), 0), "R"); // 核准額度
					print(0, 91, formatAmt(queryShareQuota.get("F10"), 0), "R"); // 已動用額度餘額
					print(0, 107, formatAmt(queryShareQuota.get("F11"), 0), "R"); // 貸出金額(放款餘額、目前餘額)
					print(0, 109, queryShareQuota.get("F12")); // 循環動用
				}
			}
		}
	}

	/**
	 * 列印股票明細(自然人、法人共用)
	 *
	 * @param listStockQuery 股票明細查詢結果
	 */
	private void printStockDetail(List<Map<String, String>> listStockQuery) {
		/**
		 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		checkSpace(3);
		print(1, 5, "擔保品提供人　　　　　　　　　　數量(股)　　面額合計");
		print(1, 5, "－－－－－－－－－－－－－－－　－－－－－　－－－－－－－");

		BigDecimal totalShares = BigDecimal.ZERO;
		BigDecimal totalValues = BigDecimal.ZERO;

		for (Map<String, String> detailStock : listStockQuery) {
			String owner = detailStock.get("F0");
			BigDecimal shares = detailStock.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(detailStock.get("F1"));
			BigDecimal values = detailStock.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(detailStock.get("F2"));

			this.print(1, 5, owner); // 擔保品提供人
			this.print(0, 47, formatAmt(shares, 0), "R"); // 數量(股)
			this.print(0, 63, formatAmt(values, 0), "R"); // 面額合計

			totalShares = totalShares.add(shares);
			totalValues = totalValues.add(values);
		}
		// 印合計
		checkSpace(2);
		print(1, 5, "－－－－－－－－－－－－－－－　－－－－－　－－－－－－－");
		print(1, 6, "合計 : ");
		print(0, 47, formatAmt(totalShares, 0), "R"); // 數量(股)
		print(0, 63, formatAmt(totalValues, 0), "R"); // 面額合計
	}

}
