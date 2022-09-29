package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4606ServiceImpl;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component("L4606Report5")
@Scope("prototype")

public class L4606Report5 extends MakeReport {

	@Autowired
	private L4606ServiceImpl l4606ServiceImpl;

	/* 轉換工具 */
	@Autowired
	private Parse parse;

	@Autowired
	private MakeFile makeFile;

//	每頁筆數
	private int pageIndex = 40;
	private long sno = 0;

	public void exec(TitaVo titaVo) throws LogicException {
		
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),titaVo.getTxCode() + "-火險佣金發放明細表", "火險佣金發放明細表.txt", 2);

		List<Map<String, String>> listL4606 = null;
		try {
			listL4606 = l4606ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4606ServiceImpl.findAll error = " + errors.toString());
		}
		this.titaVo = titaVo;
		// 設定資料庫(必須的)

		List<String> tfire = new ArrayList<String>();

		String officer = "";
		int i = 0;
		int times = 0;
		int total = 0;
		BigDecimal amt = new BigDecimal("0");
		BigDecimal totamt = new BigDecimal("0");
		int page = 0;
		int pageCnt = 0;
		for (Map<String, String> tL4606Vo : listL4606) {
			
			// 寫入TXT前面部分靠又欄位以及需要判斷欄位先切割
			//日期
			String F3="";
			if(tL4606Vo.get("F3") == ""){
				F3="         ";
			}else {
				F3=this.showRocDate(tL4606Vo.get("F3"),1);
			}
			String F4="";
			if(tL4606Vo.get("F4") == ""){
				F4="         ";
			}else {
				F4=this.showRocDate(tL4606Vo.get("F4"),1);
			}
			// 地址欄位判斷
			String F5 = "";
			if (tL4606Vo.get("F5").length() > 27) {
				F5 = tL4606Vo.get("F5").substring(0, 27);

			} else {
				F5 = tL4606Vo.get("F5");

			}
			//戶名
			String F8 = "";
			if ((tL4606Vo.get("F8") == "")) {
				F8 = "        ";
			} else {
				F8 = fillUpWord(tL4606Vo.get("F8"), 8, " ", "R");
			}

			//火險服務ID
			String F9 = "";
			if ((tL4606Vo.get("F9") == "")) {
				F9 = "          ";
			} else {
				F9 = fillUpWord(tL4606Vo.get("F9"), 10, " ", "R");
			}

			//火險服務人
			String F10 = "";
			if ((tL4606Vo.get("F10") == "")) {
				F10 = "          ";
			} else {
				F10 = fillUpWord(tL4606Vo.get("F10"), 10, " ", "R");
			}

			if (i == 0) {
				officer = tL4606Vo.get("F9");

				if (officer != null && !officer.isEmpty()) {
					tfire.add(officer);
				}
				page++;
				makeFile.put(
						"程式ID: L4606                                                                 新光人壽保險股份有限公司                                                                     日  期:"
								+ dDateUtil.getNowStringBc().substring(6, 8) + "/"
								+ dDateUtil.getNowStringBc().substring(4, 6) + "/"
								+ dDateUtil.getNowStringBc().substring(2, 4));

				makeFile.put(
						"報　表：L4606                                                                    火險佣金發放明細表                                                                        時  間:"
								+ dDateUtil.getNowStringTime().substring(0, 2) + ":"
								+ dDateUtil.getNowStringTime().substring(2, 4) + ":"
								+ dDateUtil.getNowStringTime().substring(4, 6));
				makeFile.put("                                                                                          "
						+ titaVo.get("InsuEndMonth").substring(0, 3) + "/" + titaVo.get("InsuEndMonth").substring(3, 5)
						+ "                                                                                " + "頁  數:    " + page);
				makeFile.put(
						"保單號碼         險種           保費   起保日期   到期日期   被保險人地址                                           戶號額度        戶名        火險服務ＩＤ     火險服務人         應領金額");
				makeFile.put(
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				makeFile.put(
						  fillUpWord(tL4606Vo.get("F0"), 15, " ", "R") 
						+ fillUpWord(tL4606Vo.get("F1"), 6, " ", "L")+" "
						+ fillUpWord(String.format("%,d", Integer.parseInt(tL4606Vo.get("F2"))), 14, " ", "L") + "   "
						+ fillUpWord(F3, 9, " ", "R")+"  " 
						+ fillUpWord(F4, 9, " ", "R")+"  "
						+ fillUpWord(F5, 54, " ", "R")+" " 
						+ fillUpWord((padStart(7, tL4606Vo.get("F6"))), 7, " ", "R")
						+ "  " 
						+ fillUpWord((padStart(3, tL4606Vo.get("F7"))), 4, " ", "R")+"   " 
						+ F8+"    "+ F9+"       "+ F10+" "
						+ fillUpWord(String.format("%,d", Integer.parseInt(tL4606Vo.get("F11"))), 16, " ", "L"));

				amt = amt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
				totamt = totamt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
				pageCnt++;
				
				times++;
			} else {
				if (tfire.contains(tL4606Vo.get("F9")) || "".equals(tL4606Vo.get("F9"))) {
					times++;
				} else {
					
//					reset比較值
					officer = tL4606Vo.get("F9");
					if (officer != null && !officer.isEmpty()) {
						tfire.add(officer);
					}
					makeFile.put("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					makeFile.put("                                                                                            小　計："
									+ String.format("%,d", times) +  " 筆				                                                 " + String.format("%,d", amt.intValue()));
					amt = new BigDecimal("0");
					total = total + times;
					times = 1;
					pageCnt = pageCnt + 2;

//					每頁第38筆 跳頁 
					if (pageCnt >= 35) {
						page++;
						makeFile.put("　　　　　　　　　　　　　　　　    　　       　 ===== 續下頁=====");
						makeFile.put(
								"程式ID: L4606                                                                 新光人壽保險股份有限公司                                                                     日  期:"
										+ dDateUtil.getNowStringBc().substring(6, 8) + "/"
										+ dDateUtil.getNowStringBc().substring(4, 6) + "/"
										+ dDateUtil.getNowStringBc().substring(2, 4));

						makeFile.put(
								"報　表：L4606                                                                    火險佣金發放明細表                                                                        時  間:"
										+ dDateUtil.getNowStringTime().substring(0, 2) + ":"
										+ dDateUtil.getNowStringTime().substring(2, 4) + ":"
										+ dDateUtil.getNowStringTime().substring(4, 6));
						makeFile.put("                                                                                          "
								+ titaVo.get("InsuEndMonth").substring(0, 3) + "/" + titaVo.get("InsuEndMonth").substring(3, 5)
								+ "                                                                                " + "頁  數:    " + page);
						makeFile.put(
								"保單號碼         險種           保費   起保日期   到期日期   被保險人地址                                           戶號額度        戶名        火險服務ＩＤ     火險服務人         應領金額");
						makeFile.put(
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

						makeFile.put(
								  fillUpWord(tL4606Vo.get("F0"), 15, " ", "R") 
								+ fillUpWord(tL4606Vo.get("F1"), 6, " ", "L")+" "
								+ fillUpWord(String.format("%,d", Integer.parseInt(tL4606Vo.get("F2"))), 14, " ", "L") + "   "
								+ fillUpWord(F3, 9, " ", "R")+"  " 
								+ fillUpWord(F4, 9, " ", "R")+"  "
								+ fillUpWord(F5, 54, " ", "R")+" " 
								+ fillUpWord((padStart(7, tL4606Vo.get("F6"))), 7, " ", "R")
								+ "  " 
								+ fillUpWord((padStart(3, tL4606Vo.get("F7"))), 4, " ", "R")+"   " 
								+ F8+"    "+ F9+"       "+ F10+" "
								+ fillUpWord(String.format("%,d", Integer.parseInt(tL4606Vo.get("F11"))), 16, " ", "L"));
						pageCnt = 0;

					}

				}

				makeFile.put(
						  fillUpWord(tL4606Vo.get("F0"), 15, " ", "R") 
						+ fillUpWord(tL4606Vo.get("F1"), 6, " ", "L")+" "
						+ fillUpWord(String.format("%,d", Integer.parseInt(tL4606Vo.get("F2"))), 14, " ", "L") + "   "
						+ fillUpWord(F3, 9, " ", "R")+"  " 
						+ fillUpWord(F4, 9, " ", "R")+"  "
						+ fillUpWord(F5, 54, " ", "R")+" " 
						+ fillUpWord((padStart(7, tL4606Vo.get("F6"))), 7, " ", "R")
						+ "  " 
						+ fillUpWord((padStart(3, tL4606Vo.get("F7"))), 4, " ", "R")+"   " 
						+ F8+"    "+ F9+"       "+ F10+" "
						+ fillUpWord(String.format("%,d", Integer.parseInt(tL4606Vo.get("F11"))), 16, " ", "L"));
				pageCnt++;
				
				amt = amt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
				totamt = totamt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
			} // else
			
//			每頁第38筆 跳頁 
			if (pageCnt >= 35) {
				page++;
				makeFile.put("　　　　　　　　　　　　　　　　    　　　        ===== 續下頁=====");
				makeFile.put(
						"程式ID: L4606                                                                 新光人壽保險股份有限公司                                                                     日  期:"
								+ dDateUtil.getNowStringBc().substring(6, 8) + "/"
								+ dDateUtil.getNowStringBc().substring(4, 6) + "/"
								+ dDateUtil.getNowStringBc().substring(2, 4));

				makeFile.put(
						"報　表：L4606                                                                    火險佣金發放明細表                                                                        時  間:"
								+ dDateUtil.getNowStringTime().substring(0, 2) + ":"
								+ dDateUtil.getNowStringTime().substring(2, 4) + ":"
								+ dDateUtil.getNowStringTime().substring(4, 6));
				makeFile.put("                                                                                          "
						+ titaVo.get("InsuEndMonth").substring(0, 3) + "/" + titaVo.get("InsuEndMonth").substring(3, 5)
						+ "                                                                                " + "頁  數:    " + page);
				makeFile.put(
						"保單號碼         險種           保費   起保日期   到期日期   被保險人地址                                           戶號額度        戶名        火險服務ＩＤ     火險服務人         應領金額");
				makeFile.put(
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				pageCnt = 0;
			}
			
			i++;
			if (i == listL4606.size()) {
				makeFile.put("                                                                                            小　計："
						+ String.format("%,d", times) + " 筆				                                                   "+ String.format("%,d", amt.intValue()));
				makeFile.put(
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				total = total + times;
				times = 0;
				pageCnt = pageCnt + 2;
				
			}
		}
		if(listL4606.size() == 0) {
			makeFile.put(
					"程式ID: L4606                                                                 新光人壽保險股份有限公司                                                                     日  期:"
							+ dDateUtil.getNowStringBc().substring(6, 8) + "/"
							+ dDateUtil.getNowStringBc().substring(4, 6) + "/"
							+ dDateUtil.getNowStringBc().substring(2, 4));

			makeFile.put(
					"報　表：L4606                                                                    火險佣金發放明細表                                                                        時  間:"
							+ dDateUtil.getNowStringTime().substring(0, 2) + ":"
							+ dDateUtil.getNowStringTime().substring(2, 4) + ":"
							+ dDateUtil.getNowStringTime().substring(4, 6));
			makeFile.put("                                                                                          "
					+ titaVo.get("InsuEndMonth").substring(0, 3) + "/" + titaVo.get("InsuEndMonth").substring(3, 5)
					+ "                                                                                " + "頁  數:    " + page);
			makeFile.put(
					"保單號碼         險種           保費   起保日期   到期日期   被保險人地址                                           戶號額度        戶名        火險服務ＩＤ     火險服務人         應領金額");
			makeFile.put(
					"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		}
		makeFile.put("                                                                                            總　計："
				+ String.format("%,d", total) + "筆 " + "             火險服務人：    " + String.format("%,d", tfire.size())
				+ "           人                              " + String.format("%,d", totamt.intValue()));
		makeFile.put(
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		makeFile.put("　　　　　　　　　　　　　　　　　　　             =====報  表  結  束=====");
		makeFile.put("                                                                    協理:                   經理:                    製作人:  ");

		makeFile.info("sno : " + sno);
		makeFile.close();
	}

	private String padStart(int size, String string) {
		for (int i = 0; i < size; i++) {
			if (string.length() < size) {
				string = "0" + string;
			}
		}
		return string;
	}

	/**
	 * 文字內容處理
	 * 
	 * @param text 文字
	 * @param all  上限位數
	 * @param word 要補滿的符號或文字(單字佳)
	 * @param pos  向左補齊(L)/向右補齊(R) 如果文字字數大於上限位數 自動截斷
	 *
	 */

	private String fillUpWord(String text, int allCount, String word, String pos) {

		int[] num = new int[text.length()];

		String tmpText = "";

		for (int i = 0; i < num.length; i++) {

			tmpText = text.substring(i, i + 1);
			// 中文字數為2，非中文為1
//			if (tmpText.matches("[\\u4E00-\\u9FA5]+")) {
			// 獲取字串的長度，對雙字元（包括漢字）按兩位計數
			if (tmpText.matches("[\\u0391-\\uFFE5]+")) {
				num[i] = 2;
			} else {
				num[i] = 1;
			}

		}

		// 計算用
		int tmpAllCount = 0;
		// 截斷位置用
		int tmpI = 0;
		// 最終文字數
		int tmpHaveCount = 0;

		for (int i = 0; i < num.length; i++) {
			tmpAllCount = tmpAllCount + num[i];
			if (allCount >= tmpAllCount) {
				tmpI = i;
				tmpHaveCount = tmpAllCount;
			}
		}

		String str = text.substring(0, tmpI + 1);
		String lstr = "";

		// 總字數 減去 目前有的字數 = 空的字數
		int emptyCount = 0;
		emptyCount = allCount - tmpHaveCount;

		// 重新組合
		for (int i = 0; i < emptyCount; i++) {
			if (pos == "L") {
				lstr += word;
			} else {
				str += word;
			}
		}
		if (pos == "L") {
			str = lstr + text;
		}

		return str;
	}

}
