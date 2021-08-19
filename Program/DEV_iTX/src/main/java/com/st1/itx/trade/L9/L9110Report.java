package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.springjpa.cm.L9110ServiceImpl;
import com.st1.itx.util.common.MakeReport;

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
	FacCaseApplService sFacCaseApplService;

	@Autowired
	CustMainService sCustMainService;

	@Autowired
	L9110ServiceImpl l9110ServiceImpl;

	// length: 164
	// usually we do print() with x at 1 instead of 0 so the last symbol output is
	// actually at 165;
	// hence the extra space to avoid print()s using newBorder.length() to pin the
	// output on right side and gets wrong position
	private String newBorder = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------"
			+ " ";

	private String custEntCode = "0";

	private String thisApplNo;

	private int thisMaxRow = 39;

	/**
	 * Make sure there's space for <i>line</i> lines of output on current page.
	 * 
	 * @param line amount of lines
	 */
	private void checkSpace(int line) {
		if (thisMaxRow - this.NowRow < line) {
			newPageWithFooter();
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

	public void fillData(TitaVo titaVo, boolean isLegalPerson) {
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

		List<Map<String, String>> listGuaQuery = null;

		List<Map<String, String>> listClQuery = null;
		List<Map<String, String>> listLandQuery = null;
		List<Map<String, String>> listBuildingQuery = null;
		List<Map<String, String>> listInsuQuery = null;
		List<Map<String, String>> listStockQuery = null;

		try {

			listL9110 = l9110ServiceImpl.queryLegalPerson(titaVo, thisApplNo);

			listGuaQuery = l9110ServiceImpl.queryGua(titaVo, thisApplNo);

			listClQuery = l9110ServiceImpl.queryCl(titaVo, thisApplNo);
			listLandQuery = l9110ServiceImpl.queryLand(titaVo, thisApplNo);
			listBuildingQuery = l9110ServiceImpl.queryBuilding(titaVo, thisApplNo);
			listInsuQuery = l9110ServiceImpl.queryInsu(titaVo, thisApplNo);
			listStockQuery = l9110ServiceImpl.queryStock(titaVo, thisApplNo);

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
				"戶號 .........            　戶名 ..........　　　　　　　　　　　　　　　　　　　　　　　　　　　 統一編號 ..... 　　　　　　　　　　　　核准號碼");

		if (tL9110 != null && tL9110.size() != 0) {
			// header fill-in
			this.print(-6, 16, tL9110.get("F0") + "-" + tL9110.get("F1"));
			this.print(-6, 45, tL9110.get("F2"));
			this.print(-6, 114, tL9110.get("F3"));
			this.print(-6, 147, tL9110.get("F4"));

			// 1
			this.print(1, 1, "    ");
			this.print(1, 1, "一、基本資料：");
			this.print(1, 5, "負責人姓名 ...　" + tL9110.get("F5"));
			this.print(0, 50, "負責人身分證 . " + tL9110.get("F6"));
			this.print(0, 99, "行業別 .......　" + tL9110.get("F7"));
			this.print(1, 5, "客戶別 .......　" + tL9110.get("F8"));
			this.print(0, 50, "設立日期 .....　　" + this.showRocDate(tL9110.get("F9"), 1));
			this.print(1, 5, "公司地址 .....　" + tL9110.get("F10"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F11"));
			this.print(0, 138, "公司電話 ..... " + tL9110.get("F12"));
			this.print(1, 5, "通訊地址 .....　" + tL9110.get("F13"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F14"));
			this.print(0, 138, "聯絡人姓名 ... " + tL9110.get("F15"));
			this.print(1, 5, "聯絡電話 ..... " + tL9110.get("F16") + " " + tL9110.get("F17"));
			this.print(0, 99, "傳真 ......... " + tL9110.get("F18"));
			this.print(1, 5, "站別 ......... " + tL9110.get("F19"));
			this.print(0, 99, "交互運用 ..... " + tL9110.get("F20"));

			// 2
			this.print(1, 1, "    ");
			this.print(1, 1, "二、關聯戶戶名：" + tL9110.get("F21"));

			// 3
			this.print(1, 1, "    ");
			if (listGuaQuery != null && !listGuaQuery.isEmpty()) {
				this.print(1, 1, "三、保証人資料：");

				printGuaDetail(listGuaQuery);
			} else {
				this.print(1, 1, "三、保証人資料：  資料未建立");
			}

			// 4
			this.print(1, 1, "    ");
			this.print(1, 1, "四、核准資料：");
			this.print(1, 5, "鍵檔日期 .....　" + this.showRocDate(tL9110.get("F28"), 1));
			this.print(0, 35, "核准額度 ....."); // amount is R-pined at 49
			this.print(0, 50, formatAmtSafely(tL9110.get("F29"), 0));
			this.print(0, 69, "核准科目 .....　" + tL9110.get("F30"));
			this.print(0, 105,
					"貸款期間 ..... " + tL9110.get("F31") + " 年 " + tL9110.get("F32") + " 月 " + tL9110.get("F33") + " 日");
			this.print(1, 5, "基本利率代碼 . " + tL9110.get("F34"));
			this.print(0, 35, "核准利率 .....　" + formatDecimal(tL9110.get("F35"), 4));
			this.print(0, 69, "利率調整週期 . " + tL9110.get("F36"));
			this.print(0, 105, "利率調整不變攤還額 .　" + tL9110.get("F37"));
			this.print(0, 141, "信用評分　.... " + tL9110.get("F38"));
			this.print(1, 5, "動支期限 .....　" + this.showRocDate(tL9110.get("F39"), 1));
			this.print(0, 35, "利率加減碼 ... " + formatDecimal(tL9110.get("F40"), 3));
			this.print(0, 69, "用途別 .......　" + tL9110.get("F41"));
			this.print(0, 105, "循環動用期限 .　" + this.showRocDate(tL9110.get("F42"), 1));
			this.print(0, 135, "介紹人姓名 ...　" + tL9110.get("F43"));
			this.print(1, 5, "代繳所得稅 ...　" + tL9110.get("F44"));
			this.print(0, 35, "代償碼 ........　" + tL9110.get("F45"));
			this.print(0, 69, "攤還方式 .....　" + tL9110.get("F46"));
			this.print(0, 105, "寬限總月數 ...　" + tL9110.get("F47"));
			this.print(0, 135, "首次調整週期 . " + tL9110.get("F48"));
			this.print(1, 5, "繳款方式 .....　" + tL9110.get("F49"));
			this.print(0, 35, "扣款銀行 .....　" + tL9110.get("F50"));
			this.print(0, 69, "扣款帳號 ..... " + tL9110.get("F51"));
			this.print(0, 105, "繳息週期 ..... " + tL9110.get("F52"));
			this.print(0, 135, "利率區分 .....　" + tL9110.get("F53"));
			this.print(1, 5, "違約適用方式 .　" + tL9110.get("F54"));
			this.print(0, 35, "違約率－金額 .　" + formatDecimal(tL9110.get("F55"), 3));
			this.print(0, 69, "違約率－月數 .　" + formatDecimal(tL9110.get("F56"), 3));
			this.print(0, 105, "違約還款月數 . " + tL9110.get("F57"));
			this.print(0, 135, "前段月數 ..... " + tL9110.get("F58"));
			this.print(1, 35, "計件代碼 ..... " + tL9110.get("F59"));
			this.print(0, 69, "火險服務姓名 .　" + tL9110.get("F60"));
			this.print(0, 105, "放款專員 .....　" + tL9110.get("F61"));
			this.print(0, 135, "督辦姓名 .....　" + tL9110.get("F62"));
			this.print(1, 5, "限制.清償年限 .　" + tL9110.get("F63"));
			this.print(0, 35, "帳管費 ....... 　" + formatAmtSafely(tL9110.get("F64"), 0));
			this.print(0, 69, "估價覆核姓名 .　" + tL9110.get("F65"));
			this.print(0, 105, "客戶別 ....... " + tL9110.get("F66"));
			this.print(1, 5, "徵信姓名 .....　" + tL9110.get("F67"));
			this.print(0, 35, "授信姓名 .....　" + tL9110.get("F68"));
			this.print(0, 69, "協辦姓名 .....　" + tL9110.get("F69"));

			if (tL9110Cl != null) {
				this.print(1, 1, "五、擔保品資料：　" + tL9110Cl.get("F0"));

				String clCode1 = "";
				// 擔保品不同時格式不同
				clCode1 = tL9110Cl.get("F11") == null ? "" : tL9110Cl.get("F11");
				switch (clCode1) {
				case "1": // 房地
				case "2": // 土地
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "鑑價日期 .....　" + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 105, "他項存續期限 .　" + this.showRocDate(tL9110Cl.get("F4"), 1));
					this.print(1, 5, "順位 ......... " + tL9110Cl.get("F5"));
					this.print(0, 35, "前順位金額 ...");
					this.print(0, 50, formatAmtSafely(tL9110Cl.get("F6"), 0), "R");
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					this.print(0, 105, "鑑定公司 .....　" + tL9110Cl.get("F8"));
					this.print(1, 5, "建物標示備註 . " + tL9110Cl.get("F9"));
					this.print(0, 105, "設定日期 .....　" + this.showRocDate(tL9110Cl.get("F10"), 1));

					this.print(1, 0, "    ");

					if (listLandQuery != null && !listLandQuery.isEmpty()) {
						// 列印土地明細
						printLandDetail(listLandQuery);
					}

					if (listBuildingQuery != null && !listBuildingQuery.isEmpty()) {
						// 列印建物明細
						printBuildingDetail(listBuildingQuery);
					}

					if (listInsuQuery != null && !listInsuQuery.isEmpty()) {

						this.print(1, 1, "六、保險資料：");

						// 列印保險資料
						printInsu(listInsuQuery);
					} else {
						this.print(1, 1, "六、保險資料：　　資料未建立");
					}

					break;
				case "3": // 股票
				case "4": // 有價證券
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					this.print(0, 105, "有價證券代號 .　" + tL9110Cl.get("F12")); // F12 股票代號及股票名稱

					this.print(1, 5, "質權設定書號 . " + tL9110Cl.get("F13")); // F13 質權設定書號
					this.print(0, 35, "鑑價日期 ..... " + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 69, "三個月平均價 .. ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F14"), 2), "R"); // F14 三個月平均價
					this.print(0, 105, "前日收盤價 ...　");
					this.print(0, 130, formatAmt(tL9110Cl.get("F15"), 2), "R"); // F15 前日收盤價

					this.print(1, 5, "鑑定單價 ..... ");
					this.print(0, 30, formatAmt(tL9110Cl.get("F16"), 2), "R"); // F16 鑑定單價
					this.print(0, 35, "鑑定總價 ..... ");
					this.print(0, 64, formatAmt(tL9110Cl.get("F17"), 0), "R"); // F17 鑑定總價
					this.print(0, 69, "貸放成數 ..... ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F18"), 2) + "%", "R"); // F18 貸放成數
					this.print(0, 105, "核准額度 .....　");
					this.print(0, 130, formatAmt(tL9110Cl.get("F19"), 0), "R"); // F19 核准額度

					this.print(1, 5, "保管條號碼 ... " + tL9110Cl.get("F20")); // F20 保管條號碼

					this.print(1, 0, "    ");

					printStockDetail(listStockQuery);

					break;
				case "5": // 其他
					this.info("其他擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					break;
				case "9": // 動產
					this.info("動產擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					break;
				default:
					this.error("clCode1 is out of cases.");
					break;
				}
			} else {
				this.print(1, 1, "五、擔保品資料：　資料未建立");
			}

			this.print(1, 0, "    ");

			// 7
			this.print(1, 1, "    ");
			this.print(1, 1, "七、本戶號目前總餘額：　" + formatAmtSafely(tL9110.get("F70"), 0) + "　元");
		} else {
			print(1, 1, "核准號碼" + thisApplNo + "無資料");
		}
	}

	/**
	 * 自然人資料
	 * 
	 * @param titaVo TitaVo
	 */
	private void fillDataNaturalPerson(TitaVo titaVo) {
		List<Map<String, String>> listL9110 = null;

		List<Map<String, String>> listGuaQuery = null;

		List<Map<String, String>> listClQuery = null;
		List<Map<String, String>> listLandQuery = null;
		List<Map<String, String>> listBuildingQuery = null;
		List<Map<String, String>> listInsuQuery = null;
		List<Map<String, String>> listStockQuery = null;

		try {

			listL9110 = l9110ServiceImpl.queryNaturalPerson(titaVo, thisApplNo);

			listGuaQuery = l9110ServiceImpl.queryGua(titaVo, thisApplNo);

			listClQuery = l9110ServiceImpl.queryCl(titaVo, thisApplNo);
			listLandQuery = l9110ServiceImpl.queryLand(titaVo, thisApplNo);
			listBuildingQuery = l9110ServiceImpl.queryBuilding(titaVo, thisApplNo);
			listInsuQuery = l9110ServiceImpl.queryInsu(titaVo, thisApplNo);
			listStockQuery = l9110ServiceImpl.queryStock(titaVo, thisApplNo);

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
			this.print(-6, 16, tL9110.get("F0"));
			this.print(-6, 45, tL9110.get("F1"));
			this.print(-6, 114, tL9110.get("F2"));
			this.print(-6, 153, tL9110.get("F3"));

			// 1
			this.print(1, 1, "    ");
			this.print(1, 1, "一、基本資料：");
			this.print(1, 5, "性別 .........　" + tL9110.get("F4"));
			this.print(0, 50, "出生年月日 ... " + this.showRocDate(tL9110.get("F5"), 1));
			this.print(0, 99, "客戶別 .......　" + tL9110.get("F6"));
			this.print(0, 138, "員工代號 ..... " + tL9110.get("F7"));
			this.print(1, 5, "15 日薪 ......　" + tL9110.get("F8"));
			this.print(0, 50, "配偶姓名 .....　　" + tL9110.get("F9"));
			this.print(0, 99, "配偶統一編號 .　" + tL9110.get("F10"));
			this.print(0, 138, "(BBC)......... " + tL9110.get("F11"));
			this.print(1, 5, "戶籍地址 ..... " + tL9110.get("F12"));
			this.print(0, 99, "郵遞區號 .....　" + tL9110.get("F13"));
			this.print(0, 138, "戶籍電話 ..... " + tL9110.get("F14"));
			this.print(1, 5, "通訊地址 ..... " + tL9110.get("F15"));
			this.print(0, 99, "郵遞區號 ..... " + tL9110.get("F16"));
			this.print(0, 138, "聯絡人姓名 ...　" + tL9110.get("F17"));
			this.print(1, 5, "聯絡電話 ..... " + tL9110.get("F18"));
			this.print(0, 99, "交互運用 ..... " + tL9110.get("F19"));
			this.print(0, 138, "傳真 ......... " + tL9110.get("F20"));

			// 2
			this.print(1, 1, "    ");
			if (listGuaQuery != null && !listGuaQuery.isEmpty()) {
				this.print(1, 1, "二、保証人資料：");

				printGuaDetail(listGuaQuery);
			} else {
				this.print(1, 1, "二、保証人資料：  資料未建立");
			}

			// 3
			this.print(1, 1, "    ");
			this.print(1, 1, "三、核准資料：");
			this.print(1, 5, "鍵檔日期 .....　" + this.showRocDate(tL9110.get("F27"), 1));
			this.print(0, 35, "核准額度 ....."); // amount is R-pined at 49
			this.print(0, 50, formatAmtSafely(tL9110.get("F28"), 0));
			this.print(0, 69, "核准科目 .....　" + tL9110.get("F29"));
			this.print(0, 105,
					"貸款期間 ..... " + tL9110.get("F30") + " 年 " + tL9110.get("F31") + " 月 " + tL9110.get("F32") + " 日");
			this.print(1, 5, "基本利率代碼 . " + tL9110.get("F33"));
			this.print(0, 35, "核准利率 .....　" + formatDecimal(tL9110.get("F34"), 4));
			this.print(0, 69, "利率調整週期 . " + tL9110.get("F35"));
			this.print(0, 105, "利率調整不變攤還額 .　" + tL9110.get("F36"));
			this.print(0, 141, "信用評分　.... " + tL9110.get("F37"));
			this.print(1, 5, "動支期限 .....　" + this.showRocDate(tL9110.get("F38"), 1));
			this.print(0, 35, "利率加減碼 ... " + formatDecimal(tL9110.get("F39"), 3));
			this.print(0, 69, "用途別 .......　" + tL9110.get("F40"));
			this.print(0, 105, "循環動用期限 .　" + this.showRocDate(tL9110.get("F41"), 1));
			this.print(0, 135, "介紹人姓名 ...　" + tL9110.get("F42"));
			this.print(1, 5, "代繳所得稅 ...　" + tL9110.get("F43"));
			this.print(0, 35, "代償碼 ........　" + tL9110.get("F44"));
			this.print(0, 69, "攤還方式 .....　" + tL9110.get("F45"));
			this.print(0, 105, "寬限總月數 ...　" + tL9110.get("F46"));
			this.print(0, 135, "首次調整週期 . " + tL9110.get("F47"));
			this.print(1, 5, "繳款方式 .....　" + tL9110.get("F48"));
			this.print(0, 35, "扣款銀行 .....　" + tL9110.get("F49"));
			this.print(0, 69, "扣款帳號 ..... " + tL9110.get("F50"));
			this.print(0, 105, "繳息週期 ..... " + tL9110.get("F51"));
			this.print(0, 135, "利率區分 .....　" + tL9110.get("F52"));
			this.print(1, 5, "違約適用方式 .　" + tL9110.get("F53"));
			this.print(0, 35, "違約率－金額 .　" + formatDecimal(tL9110.get("F54"), 3));
			this.print(0, 69, "違約率－月數 .　" + formatDecimal(tL9110.get("F55"), 3));
			this.print(0, 105, "違約還款月數 . " + tL9110.get("F56"));
			this.print(0, 135, "前段月數 ..... " + tL9110.get("F57"));
			this.print(1, 5, "團體戶名 ..... " + tL9110.get("F58"));
			this.print(0, 35, "計件代碼 ..... " + tL9110.get("F59"));
			this.print(0, 69, "火險服務姓名 .　" + tL9110.get("F60"));
			this.print(0, 105, "放款專員 .....　" + tL9110.get("F61"));
			this.print(0, 135, "督辦姓名 .....　" + tL9110.get("F62"));
			this.print(1, 5, "限制清償年限 .　" + tL9110.get("F63"));
			this.print(0, 35, "帳管費 ....... 　" + formatAmtSafely(tL9110.get("F64"), 0));
			this.print(0, 69, "估價覆核姓名 .　" + tL9110.get("F65"));
			this.print(0, 105, "客戶別 ....... " + tL9110.get("F66"));
			this.print(1, 5, "徵信姓名 .....　" + tL9110.get("F67"));
			this.print(0, 35, "授信姓名 .....　" + tL9110.get("F68"));
			this.print(0, 69, "協辦姓名 .....　" + tL9110.get("F69"));

			// 4
			this.print(1, 1, "    ");

			if (tL9110Cl != null) {
				this.print(1, 1, "四、擔保品資料：　" + tL9110Cl.get("F0"));

				String clCode1 = "";

				// 擔保品不同時格式不同
				clCode1 = tL9110Cl.get("F11") == null ? "" : tL9110Cl.get("F11");
				switch (clCode1) {
				case "1": // 房地
				case "2": // 土地
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "鑑價日期 .....　" + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 105, "他項存續期限 .　" + this.showRocDate(tL9110Cl.get("F4"), 1));
					this.print(1, 5, "順位 ......... " + tL9110Cl.get("F5"));
					this.print(0, 35, "前順位金額 ...");
					this.print(0, 50, formatAmtSafely(tL9110Cl.get("F6"), 0), "R");
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					this.print(0, 105, "鑑定公司 .....　" + tL9110Cl.get("F8"));
					this.print(1, 5, "建物標示備註 . " + tL9110Cl.get("F9"));
					this.print(0, 105, "設定日期 .....　" + this.showRocDate(tL9110Cl.get("F10"), 1));

					this.print(1, 0, "    ");

					if (listLandQuery != null && !listLandQuery.isEmpty()) {
						// 列印土地明細
						printLandDetail(listLandQuery);
					}

					if (listBuildingQuery != null && !listBuildingQuery.isEmpty()) {
						// 列印建物明細
						printBuildingDetail(listBuildingQuery);
					}

					if (listInsuQuery != null) {

						this.print(1, 1, "五、保險資料：");

						// 列印保險資料
						printInsu(listInsuQuery);
					} else {
						this.print(1, 1, "五、保險資料：　　資料未建立");
					}

					break;
				case "3": // 股票
				case "4": // 有價證券
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					this.print(0, 105, "有價證券代號 .　" + tL9110Cl.get("F12")); // F12 股票代號及股票名稱

					this.print(1, 5, "質權設定書號 . " + tL9110Cl.get("F13")); // F13 質權設定書號
					this.print(0, 35, "鑑價日期 ..... " + this.showRocDate(tL9110Cl.get("F3"), 1));
					this.print(0, 69, "三個月平均價 .. ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F14"), 2), "R"); // F14 三個月平均價
					this.print(0, 105, "前日收盤價 ...　");
					this.print(0, 130, formatAmt(tL9110Cl.get("F15"), 2), "R"); // F15 前日收盤價

					this.print(1, 5, "鑑定單價 ..... ");
					this.print(0, 30, formatAmt(tL9110Cl.get("F16"), 2), "R"); // F16 鑑定單價
					this.print(0, 35, "鑑定總價 ..... ");
					this.print(0, 60, formatAmt(tL9110Cl.get("F17"), 0), "R"); // F17 鑑定總價
					this.print(0, 69, "貸放成數 ..... ");
					this.print(0, 100, formatAmt(tL9110Cl.get("F18"), 2) + "%", "R"); // F18 貸放成數
					this.print(0, 105, "核准額度 .....　");
					this.print(0, 130, formatAmt(tL9110Cl.get("F19"), 0), "R"); // F19 核准額度

					this.print(1, 5, "保管條號碼 ... " + tL9110Cl.get("F20")); // F20 保管條號碼

					this.print(1, 0, "    ");

					printStockDetail(listStockQuery);

					break;
				case "5": // 其他
					this.info("其他擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					break;
				case "9": // 動產
					this.info("動產擔保品");
					this.print(1, 5, "擔保品號碼 ... " + tL9110Cl.get("F1"));
					this.print(0, 35, "擔保品別 .....　" + tL9110Cl.get("F2"));
					this.print(0, 69, "地區別 .......　" + tL9110Cl.get("F7"));
					break;
				default:
					this.error("clCode1 is out of cases.");
					break;
				}
			} else {
				this.print(1, 1, "四、擔保品資料：　資料未建立");
			}

			// 5
			// interface has no sample for reference. using legalperson's for now.

			// 7
			this.print(1, 1, "    ");
			this.print(1, 1, "六、本戶號目前總餘額：　" + formatAmtSafely(tL9110.get("F70"), 0) + "　元");
		} else {
			print(1, 1, "核准號碼" + thisApplNo + "無資料");
		}
	}

	/**
	 * 列印保證人明細(自然人、法人共用)
	 * 
	 * @param listGuaQuery 保證人明細查詢結果
	 */
	private void printGuaDetail(List<Map<String, String>> listGuaQuery) {

		for (Map<String, String> tGua : listGuaQuery) {

			this.print(1, 5, "統一編號 ..... " + tGua.get("F0"));
			this.print(0, 33, "姓名 .........　" + tGua.get("F1"));
			this.print(0, 99, "關係 .........　" + tGua.get("F2"));

			this.print(1, 99, "保証金額 .....　　　" + formatAmtSafely(tGua.get("F3"), 0));

			this.print(1, 5, "通訊地址 .....　" + tGua.get("F4"));
			this.print(0, 99, "郵遞區號 ..... " + tGua.get("F5"));
		}

	}

	/**
	 * formatAmt() doesn't have null protection so i made this</br>
	 * it's basically same as formatAmt() but with try catch
	 * 
	 * @param amt   金額
	 * @param digit 小數後位數
	 */
	private String formatAmtSafely(BigDecimal amt, int digit) {
		try {

			return formatAmt(amt, digit);
		} catch (Exception e) {
			this.error("L9110Report.formatAmtSafely warning = " + e + "( " + amt.toString() + "," + digit + " )");
			return amt.toString();
		}
	}

	/**
	 * formatAmt() doesn't have null protection so i made this</br>
	 * it's basically same as formatAmt() but with try catch
	 * 
	 * @param totalArea 金額
	 * @param digit     小數後位數
	 */
	private String formatAmtSafely(String amt, int digit) {
		try {
			return formatAmt(new BigDecimal(amt), digit);
		} catch (Exception e) {
			this.error("L9110Report.formatAmtSafely warning = " + e + "( " + amt + "," + digit + " )");
			return amt;
		}
	}

	/**
	 * 單純處理數字後小數點
	 * 
	 * @param decimal       數字
	 * @param digitAfterDot 小數點後保留幾位
	 */
	private String formatDecimal(BigDecimal decimal, int digitAfterDot) {
		try {
			return String.format("%.".concat(Integer.toString(digitAfterDot)).concat("f"), decimal);
		} catch (Exception e) {
			this.error("L9110Report.formatDecimal warning = " + e + "( " + decimal.toString() + "," + digitAfterDot
					+ " )");
			return decimal.toString();
		}
	}

	/**
	 * 單純處理數字後小數點
	 * 
	 * @param decimal       數字
	 * @param digitAfterDot 小數點後保留幾位
	 */
	private String formatDecimal(String decimal, int digitAfterDot) {
		try {
			return String.format("%.".concat(Integer.toString(digitAfterDot)).concat("f"), new BigDecimal(decimal));
		} catch (Exception e) {
			this.warn("L9110Report.formatDecimal warning = " + e + "( " + decimal + "," + digitAfterDot + " )");
			return decimal;
		}
	}

	/**
	 * 單位為如仟元、百元時使用
	 * 
	 * @param amt 金額
	 * @param per 單位
	 */
	private String formatNum(BigDecimal amt, int per) {
		try {
			return amt.divide(new BigDecimal(per)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			this.error("L9110Report.formatNum warning = " + e + "( " + amt.toString() + " )");
			return amt.toString();
		}
	}

	/**
	 * 單位為如仟元、百元時使用
	 * 
	 * @param amt 金額
	 * @param per 單位
	 */
	private String formatNum(String amt, int per) {
		try {
			BigDecimal b = new BigDecimal(amt);
			return b.divide(new BigDecimal(per)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			this.error("L9110Report.formatNum warning = " + e + "( " + amt + " )");
			return amt;
		}
	}

	public void makePdf(TitaVo titaVo, TotaVo totaVo) throws LogicException {
		// 名稱固定為查詢的第一筆額度編號
		String fileReportItem = reportItem.concat(titaVo.getParam("APPLNO1"));

		this.open(titaVo, reportDate, brno, reportCode, fileReportItem, security, pageSize, pageOrientation);
		this.setCharSpaces(0);

		for (int currentApplNoItem = 1; currentApplNoItem <= 50; currentApplNoItem++) {
			thisApplNo = titaVo.getParam("APPLNO" + currentApplNoItem);

			this.info("thisApplNo = " + thisApplNo);

			if (thisApplNo != null && !(thisApplNo.isEmpty()) && Integer.parseInt(thisApplNo) > 0) {

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
					tCustMain = sCustMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
				}

				if (tCustMain != null) {
					custEntCode = tCustMain.getEntCode();
				} else {
					this.warn("L9110Report thisApplNo(" + thisApplNo + ")'s CustUKey is not found in CustMain. ");
					continue; // 跳下一圈
				}

				// 企金別無值時，預設為0:個金
				if (custEntCode == null || custEntCode.isEmpty()) {
					custEntCode = "0";
				}

				this.info("L9110Report : " + thisApplNo + ", This applNo is..." + (custEntCode.equals("1") ? "" : "not")
						+ " a legal person");

				// 第一筆不做續下頁
				if (currentApplNoItem == 1) {
					this.newPage();
				} else {
					newPageWithFooter();
				}

				fillData(titaVo, custEntCode.equals("1"));

				if (tFacMain != null) {
					// 更新原DB
					tFacMain = sFacMainService.holdById(tFacMain, titaVo);
					tFacMain.setL9110Flag("Y");
					try {
						sFacMainService.update(tFacMain, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "額度主檔" + thisApplNo);
					}
				}
			}
		}

		printCloseFooter();

		long rptNo = this.close();
		totaVo.put("PdfSnoF", Long.toString(rptNo));
		this.toPdf(rptNo, this.reportCode + "_" + fileReportItem);
	}

	/**
	 * 確保每頁下面有續下頁字樣
	 */
	private void newPageWithFooter() {
		print(-47, (newBorder.length() / 2) + 2, "=====　續下頁　=====", "C");
		this.newPage();
	}

	/**
	 * 列印建物明細(自然人、法人共用)
	 * 
	 * @param listBuildingQuery 建物明細查詢結果
	 */
	private void printBuildingDetail(List<Map<String, String>> listBuildingQuery) {

		/**
		 * --------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * -----------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		checkSpace(3);
		this.print(1, 5, "　　　　　　　　　　　　　　　　　　　　　　　　　　 主建物　公設　　 （坪）　　 （坪）  （坪）　 （仟／坪）　　（仟）　　 （仟）");
		this.print(1, 5, " 序號　提供人／門牌號碼　　　　　　　　　　　　　　　建　號　建號　　 主建物　　　公設　　車位　　 鑑定單價　　　核貸　　　 設定　　賣方姓名　　　 賣方 ID");
		this.print(1, 5,
				"-----------------------------------------------------------------------------------------------------------------------------------------------------------------");

		BigDecimal totalFloorArea = BigDecimal.ZERO;
		BigDecimal totalPublicArea = BigDecimal.ZERO;
		BigDecimal totalCarArea = BigDecimal.ZERO;
		BigDecimal totalApplyAmt = BigDecimal.ZERO;
		BigDecimal totalSettingAmt = BigDecimal.ZERO;

		for (Map<String, String> mBuilding : listBuildingQuery) {
			this.print(1, 0, "    ");
			this.print(0, 8, mBuilding.get("F0"), "R");
			this.print(0, 12, mBuilding.get("F1"));
			this.print(0, 58, mBuilding.get("F2") + " " + mBuilding.get("F3"));

			this.print(0, 81, formatDecimal(mBuilding.get("F4"), 2), "R");
			totalFloorArea = totalFloorArea.add(toDecimal(mBuilding.get("F4")));

			this.print(0, 92, formatDecimal(mBuilding.get("F5"), 2), "R");
			totalPublicArea = totalPublicArea.add(toDecimal(mBuilding.get("F5")));

			this.print(0, 101, formatDecimal(mBuilding.get("F6"), 2), "R");
			totalCarArea = totalCarArea.add(toDecimal(mBuilding.get("F6")));

			this.print(0, 113, formatNum(mBuilding.get("F7"), 1000), "R");

			this.print(0, 118, formatNum(mBuilding.get("F8"), 1000));
			totalApplyAmt = totalApplyAmt.add(toDecimal(mBuilding.get("F8")));

			this.print(0, 129, formatNum(mBuilding.get("F9"), 1000));
			totalSettingAmt = totalSettingAmt.add(toDecimal(mBuilding.get("F9")));

			this.print(1, 122, mBuilding.get("F10"));

		}

		checkSpace(2);
		this.print(1, 5,
				"-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 5, " 建物合計：");
		this.print(0, 81, formatDecimal(totalFloorArea, 2), "R");
		this.print(0, 92, formatDecimal(totalPublicArea, 2), "R");
		this.print(0, 101, formatDecimal(totalCarArea, 2), "R");
		this.print(0, 118, formatNum(totalApplyAmt, 1000));
		this.print(0, 129, formatNum(totalSettingAmt, 1000));

		this.print(1, 0, "    ");
	}

	// 自訂表尾
	public void printCloseFooter() {
		this.print(-47, 1, "副總　　　　　　　　資協　　　　　　　　協理　　　　　　　　經理　　　　　　　　撥款覆核　　　　　　　　放款專員　　　　　　　　製表人", "L");
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

		this.setMaxRows(thisMaxRow);
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
		this.print(1, 5, " 序號　保單號碼　　　　　　　 保險金額　 保險起日　 保險迄日　 保險公司");
		this.print(1, 5, "---------------------------------------------------------------------------------------");
		for (Map<String, String> mInsu : listInsuQuery) {
			this.print(1, 0, "   ");
			this.print(0, 8, mInsu.get("F0"), "R");
			this.print(0, 12, mInsu.get("F1"));
			this.print(0, 43, formatAmtSafely(mInsu.get("F2"), 0), "R");
			this.print(0, 46, this.showRocDate(mInsu.get("F3"), 1));
			this.print(0, 57, this.showRocDate(mInsu.get("F4"), 1));
			this.print(0, 69, mInsu.get("F5"));
		}

	}

	/**
	 * 列印土地明細(自然人、法人共用)
	 * 
	 * @param listLandQuery 土地明細查詢結果
	 */
	private void printLandDetail(List<Map<String, String>> listLandQuery) {

		// land
		BigDecimal totalArea = new BigDecimal(0);
		BigDecimal totalLastTransferred = new BigDecimal(0);
		BigDecimal totalApplyAmt = new BigDecimal(0);
		BigDecimal totalSettingAmt = new BigDecimal(0);

		/**
		 * --------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * -----------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		checkSpace(3);
		this.print(1, 5, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　  （坪）　　　　　　  （元） 　（仟／坪）     （仟）　 　（仟）");
		this.print(1, 5, " 序號　提供人　　　縣市　　鄉鎮區　　段　　　　　 小段　　　　　地號　　　　　　 面積　年度　　　　前次移轉　 鑑定單價　　　 核貸　　　 設定");
		this.print(1, 5,
				"----------------------------------------------------------------------------------------------------------------------------------------------");
		for (Map<String, String> mLand : listLandQuery) {
			this.print(1, 1, "   ");
			this.print(0, 8, mLand.get("F0"), "R");
			this.print(0, 12, mLand.get("F1"));
			this.print(0, 24, mLand.get("F2"));
			this.print(0, 32, mLand.get("F3"));
			this.print(0, 42, mLand.get("F4"));
			this.print(0, 67, mLand.get("F5"));
			this.print(0, 88, formatDecimal(mLand.get("F6"), 2), "C");

			String f7 = "";

			try {
				f7 = Integer.toString(Integer.parseInt(mLand.get("F7")) - 1911);
			} catch (Exception e) {
				this.warn(thisApplNo + "'s land " + mLand.get("F0") + " got a wrong F7 input, " + e);
			}

			this.print(0, 95, f7, "R");
			this.print(0, 111, formatAmtSafely(mLand.get("F8"), 0), "R");
			this.print(0, 122, formatNum(mLand.get("F9"), 1000), "R");
			this.print(0, 133, formatNum(mLand.get("F10"), 1000), "R");
			this.print(0, 144, formatNum(mLand.get("F11"), 1000), "R");

			totalArea = totalArea.add(toDecimal(mLand.get("F6")));
			totalLastTransferred = totalLastTransferred.add(toDecimal(mLand.get("F8")));
			totalApplyAmt = totalApplyAmt.add(toDecimal(mLand.get("F10")));
			totalSettingAmt = totalSettingAmt.add(toDecimal(mLand.get("F11")));

		}
		checkSpace(2);
		this.print(1, 5,
				"----------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 5, " 土地合計：");
		this.print(0, 88, formatAmtSafely(totalArea, 2));
		this.print(0, 111, formatNum(totalLastTransferred, 1000), "R");
		this.print(0, 133, formatNum(totalApplyAmt, 1000), "R");
		this.print(0, 144, formatNum(totalSettingAmt, 1000), "R");

		this.print(1, 0, "    ");
	}

	/**
	 * 列印股票明細(自然人、法人共用)
	 * 
	 * @param listStockQuery 股票明細查詢結果
	 */
	private void printStockDetail(List<Map<String, String>> listStockQuery) {
		/**
		 * --------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * -----------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		this.print(1, 5, " 擔保品提供人          數量(股)                 面額合計");
		this.print(1, 5, "------------------------------------------------------------");

		BigDecimal totalShares = BigDecimal.ZERO;
		BigDecimal totalValues = BigDecimal.ZERO;

		for (Map<String, String> detailStock : listStockQuery) {
			String ownerId = detailStock.get("F0");
			BigDecimal shares = detailStock.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(detailStock.get("F1"));
			BigDecimal values = detailStock.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(detailStock.get("F2"));

			this.print(1, 6, ownerId); // 擔保品提供人
			this.print(0, 38, formatAmt(shares, 0), "R"); // 數量(股)
			this.print(0, 64, formatAmt(values, 0), "R"); // 面額合計

			totalShares = totalShares.add(shares);
			totalValues = totalValues.add(values);
		}
		// 印合計
		this.print(1, 5, "------------------------------------------------------------");
		this.print(1, 6, "合計 : ");
		this.print(0, 38, formatAmt(totalShares, 0), "R"); // 數量(股)
		this.print(0, 64, formatAmt(totalValues, 0), "R"); // 面額合計
	}

	/**
	 * breaks when input is empty</br>
	 * hence this
	 * 
	 * @param decimal num
	 */
	private BigDecimal toDecimal(String decimal) {
		try {
			return new BigDecimal(decimal);
		} catch (Exception e) {
			this.warn("L9110Report.formatDecimal warning = " + e + "( " + decimal + " )");
			return BigDecimal.ZERO;
		}
	}
}
