package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9720ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.format.StringCut;

@Component("L9720Report2")
@Scope("prototype")
public class L9720Report2 extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9720Report2.class);

	@Autowired
	L9720ServiceImpl l9720ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9720";
	private String reportItem = "理財型商品續約檢核報表";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private int validTime = 1;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9720Report2.printHeader");

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);

		this.print(-2, 1, "報　表：" + this.getRptCode());
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));

		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));

		this.print(-4, 1, "檢核時間：" + (validTime == 1 ? "撥款後第10個月" : "撥款後第22個月"));
		this.print(-4, 85, showRocDate(this.reportDate), "C");

		this.print(-4, 145, "頁　　數：" + this.getNowPage());

		// 明細表頭
		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		this.print(-6, 1,
				" 戶號    額度     姓名                                    核准額度    首次撥款日    到期日           餘額   商品代碼   火險保額   火險迄日   房貸專員");
		this.print(-7, 1,
				"====================================================================================================================================================================");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(39);

	}

	// 自訂表尾
	private void printCloseFooter() {
		this.print(-48, 1,
				"                                                                         ===== 報　表　結　束 =====");

	}

	private void fillData(TitaVo titaVo, int vt) {
		this.newPage();

		List<Map<String, String>> lL9720 = null;

		try {
			lL9720 = l9720ServiceImpl.findAll(titaVo, vt);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9720ServiceImpl.findAll error = " + errors.toString());
		}

		if (lL9720 != null && lL9720.size() != 0) {

			for (Map<String, String> tLDVo : lL9720) {
				// 明細資料新的一行
				print(1, 1, "　　");

				// 明細資料格式處理
				// 戶號-左補零7碼
				String detailCustNo = FormatUtil.pad9(tLDVo.get("F0"), 7);
				// 額度-左補零3碼
				String detailFacmNo = FormatUtil.pad9(tLDVo.get("F1"), 3);
				// 姓名-取總長最多40字
				String detailCustName = StringCut.stringCut(tLDVo.get("F2"), 0, 40);
				// 核准額度-加撇節,顯示到整數
				String detailLineAmt = formatAmt(tLDVo.get("F3"), 0);
				// 首次撥款日-民國年,以/區隔年月日
				String detailFirstDrawdownDate = showRocDate(tLDVo.get("F4"), 1);
				// 到期日-民國年,以/區隔年月日
				String detailMaturityDate = showRocDate(tLDVo.get("F5"), 1);
				// 餘額-加撇節,顯示到整數
				String detailUtilAmt = formatAmt(tLDVo.get("F6"), 0);
				// 商品代碼
				String detailProdNo = tLDVo.get("F7");
				// 火險保額-加撇節,顯示到整數
				String detailInsuAmt = formatAmt(tLDVo.get("F8"), 0);
				// 火險迄日-民國年,以/區隔年月日
				String detailInsuEndDate = showRocDate(tLDVo.get("F9"), 1);
				// 房貸專員(員編)
				String detailEmpNo = tLDVo.get("F10");
				// 房貸專員(姓名)
				String detailEmpName = tLDVo.get("F11");

				// 明細資料輸出位置處理(預設靠左)
				print(0, 1, detailCustNo); // 戶號
				print(0, 9, detailFacmNo); // 額度
				print(0, 16, detailCustName); // 姓名
				print(0, 70, detailLineAmt, "R"); // 核准額度-靠右
				print(0, 72, detailFirstDrawdownDate); // 首次撥款日
				print(0, 84, detailMaturityDate); // 到期日
				print(0, 108, detailUtilAmt, "R"); // 餘額-靠右
				print(0, 110, detailProdNo); // 利率代碼
				print(0, 128, detailInsuAmt, "R"); // 火險保額-靠右
				print(0, 130, detailInsuEndDate); // 火險迄日
				print(0, 141, detailEmpNo); // 房貸專員(員編)
				print(0, 149, detailEmpName); // 房貸專員(姓名)
			}

		} else {
			print(1, 1, "本月無資料");
		}

	}

	public void makePdf(TitaVo titaVo) throws LogicException {
		String EntDy = Integer.toString(titaVo.getEntDyI() + 19110000);
		LocalDate validDatePivot = LocalDate.of(Integer.parseInt(EntDy.substring(0, 4)),
				Integer.parseInt(EntDy.substring(4, 6)), Integer.parseInt(EntDy.substring(6)));
		LocalDate validDateFirst = validDatePivot.minusMonths(10);
		LocalDate validDateSecond = validDatePivot.minusMonths(22);

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		for (validTime = 1; validTime <= 2; validTime++) {
			fillData(titaVo, validTime);
		}

		printCloseFooter();

		this.toPdf(this.close(),
				"L9720_" + Integer.toString(validDatePivot.getYear() - 1911)
						+ String.format("%02d", validDatePivot.getMonthValue()) + "續約檢核結果("
						+ Integer.toString(validDateFirst.getYear() - 1911)
						+ String.format("%02d", validDateFirst.getMonthValue()) + "及"
						+ Integer.toString(validDateSecond.getYear() - 1911)
						+ String.format("%02d", validDateSecond.getMonthValue()) + "月)");

	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9720Report2 exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = titaVo.getEntDyI() + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		makePdf(titaVo);

		return true;
	}
}
