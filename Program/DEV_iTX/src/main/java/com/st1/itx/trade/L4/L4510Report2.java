package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.L4510R2ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4510Report2")
@Scope("prototype")

public class L4510Report2 extends MakeReport {

	@Autowired
	public L4510R2ServiceImpl l4510R2ServiceImpl;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private CdCodeService cdCodeService;

	private int perfMonth = 0;
	private String procCode = "";
	private int entryDate = 0;
	private int flag = 0;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 1, "程式ID：" + "L4510Report2");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 1, "報　表：" + "L4510Report2");
		this.print(-2, 70, "帳管費扣薪明細表", "C");
		this.print(-2, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
		this.print(-4, 1, "業績年月：" + perfMonth);
		this.print(-4, 30, "流程別：" + procCode);
		this.print(-4, 60, "入帳日期：" + formatDate(entryDate));
		this.print(-5, 1, "員工代號  身分證字號   戶號    戶名             額度編號    撥款序號                     應扣金額");
		this.print(-6, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public long exec(TitaVo titaVo1) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		long sno = 0;

		titaVo = titaVo1;
		this.info("L4510Report exec");

		try {
			fnAllList = l4510R2ServiceImpl.findAll(entryDate, procCode, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4510ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {

			perfMonth = parse.stringToInteger(fnAllList.get(0).get("F0")) - 191100;
			procCode = fnAllList.get(0).get("F1");
			entryDate = parse.stringToInteger(fnAllList.get(0).get("F2")) - 19110000;

			if (flag == 1) {
				this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4510", "15日薪-帳管費扣薪明細表", "", "A4", "L");
			} else {
				this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4510", "非15日薪-帳管費扣薪明細表", "", "A4", "L");
			}

			DecimalFormat df1 = new DecimalFormat("#,##0");

//			by 流程別合計
			BigDecimal sumA1 = BigDecimal.ZERO;

			int timeAs = 0, total = 0, i = 0, pageCnt = 0;
			for (int j = 1; j <= fnAllList.size(); j++) {

				i = j - 1;

				this.info("fnAllList.get(i)-------->" + fnAllList.get(i).toString());

				int lengthF6 = 8;
				if (fnAllList.get(i).get("F6").length() < 8) {
					lengthF6 = fnAllList.get(i).get("F6").length();
				}

//				1.每筆先印出明細
				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 2, fnAllList.get(i).get("F3")); // 員工代號
				this.print(0, 10, fnAllList.get(i).get("F4"));// 身分證字號
				this.print(0, 22, FormatUtil.pad9(fnAllList.get(i).get("F5"), 7));// 戶號
				this.print(0, 30, fnAllList.get(i).get("F6").substring(0, lengthF6));// 戶名
				this.print(0, 50, FormatUtil.pad9(fnAllList.get(i).get("F7"), 3));// 額度
				this.print(0, 60, FormatUtil.pad9(fnAllList.get(i).get("F8"), 3));// 撥款
				this.print(0, 94, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F9"))), "R");// 應扣金額

//				流程別筆數統計
				timeAs++;
//				by 流程別合計
				sumA1 = sumA1.add(parse.stringToBigDecimal(fnAllList.get(i).get("F9")));

//				全部筆數統計
				total++;

//				每頁筆數相加
				pageCnt++;

//				2.再與下一筆比較，決定是否換行或換頁

				if (j != fnAllList.size()) {
//					年月不同則跳頁，並且累計歸零
					if (!fnAllList.get(i).get("F1").equals(fnAllList.get(j).get("F1"))) {
						this.info("RepayBank Not Match...");

						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 1, "         總　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeAs), "R");
						this.print(0, 94, df1.format(sumA1), "R");
						this.print(1, 70, "=====續下頁=====", "C");

						timeAs = 0;
						sumA1 = BigDecimal.ZERO;

						perfMonth = parse.stringToInteger(fnAllList.get(j).get("F0")) - 191100;
						procCode = fnAllList.get(j).get("F1");
						entryDate = parse.stringToInteger(fnAllList.get(j).get("F2")) - 19110000;

						pageCnt = 0;
						this.newPage();
						continue;
					}
//					每頁第41筆 跳頁 
					if (pageCnt == 41) {
						this.print(1, 70, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
					if (total == fnAllList.size()) {
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 1, "         總　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeAs), "R");
						this.print(0, 94, df1.format(sumA1), "R");
					}
				}
			}
			sno = this.close();
			this.toPdf(sno);
		}
		return sno;
	}

	public long exec(int iEntryDate, List<String> iProcCode, int flags, TitaVo titaVo) throws LogicException {
		entryDate = iEntryDate;
		flag = flags;

		for (int i = 0; i < iProcCode.size(); i++) {
			if (i == 0) {
				procCode = iProcCode.get(0);
			} else {
				procCode += "," + iProcCode.get(i);
			}
		}

		return exec(titaVo);
	}

	private String formatDate(int date) {
		String result = "";

		if (date > 19110000) {
			date = date - 19110000;
		}
		result = FormatUtil.pad9("" + date, 7);

		result = result.substring(0, 3) + "/" + result.substring(3, 5) + "/" + result.substring(5);

		return result;
	}

	private String limitLength(String str, int pos) {
		byte[] input = str.getBytes();

		int inputLength = input.length;

		this.info("str ..." + str);
		this.info("inputLength ..." + inputLength);

		int resultLength = inputLength;

		if (inputLength > pos) {
			resultLength = pos;
		}

		String result = "";

		if (resultLength > 0) {
			byte[] resultBytes = new byte[resultLength];
			System.arraycopy(input, 0, resultBytes, 0, resultLength);
			result = new String(resultBytes);
		}

		return result;
	}
}
