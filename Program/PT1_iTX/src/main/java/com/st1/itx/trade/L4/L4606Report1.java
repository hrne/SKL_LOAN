package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4606ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4606Report1")
@Scope("prototype")

public class L4606Report1 extends MakeReport {

	@Autowired
	public L4606ServiceImpl L4606ServiceImpl;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dDateUtil;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);

	}

//	@Override
//	public void printHeaderP() {
//
//	}

//	每頁筆數
	private int pageIndex = 40;
	
	public void printHeaderL() {
		this.print(-1, 1, "程式ID：" + "L4606");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");

		this.print(-1, 130, "日　　期：" + dDateUtil.getNowStringBc().substring(6, 8) + "/" + dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(2, 4), "R");
		this.print(-2, 1, "報　表：" + "L4606");
		this.print(-2, 70, "火險佣金發放明細表", "C");
		this.print(-2, 130, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 70, titaVo.get("InsuEndMonth").toString().substring(0, 3) + "/" + titaVo.get("InsuEndMonth").toString().substring(3, 5), "C");
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
		this.print(-4, 1, "保單號碼         險種   保費 起保日期  到期日期 被保險人地址                          戶號額度    戶名          火險服務ＩＤ 火險服務人 應領金額"); // fix
		this.print(-5, 1, "-------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 讀取VAR參數
		this.info("titaVo ======" + titaVo);
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4606", "火險佣金發放明細表", "", "A4", "L");
		// 設定資料庫(必須的)

		List<Map<String, String>> L4606List = null;
		try {
			L4606List = L4606ServiceImpl.findAll(titaVo);
			this.info("------->list = " + L4606List);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4606ServiceImpl.findAll error = " + errors.toString());
		}
		
		ArrayList<String> tfire = new ArrayList<String>();
		
		String officer = "";
		int i = 0;
		int times = 0, total = 0;
		BigDecimal amt = new BigDecimal("0");
		BigDecimal totamt = new BigDecimal("0");
		
		int pageCnt = 0;
		for (Map<String, String> tL4606Vo : L4606List) {

			if (i == 0) {
				officer = tL4606Vo.get("F9").toString();
				
				if(officer != "") {
				  tfire.add(officer);
				}
				
				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 1, tL4606Vo.get("F0"));
				this.print(0, 18, tL4606Vo.get("F1"));
				this.print(0, 27, String.format("%,d", Integer.parseInt(tL4606Vo.get("F2").toString())), "R");
				this.print(0, 36, tL4606Vo.get("F3"), "R");
				this.print(0, 45, tL4606Vo.get("F4"), "R");
				this.print(0, 46, limitLength(tL4606Vo.get("F5"), 40));
				this.print(0, 83, PadStart(7, tL4606Vo.get("F6").toString()));
				this.print(0, 90, "-");
				this.print(0, 94, PadStart(3, tL4606Vo.get("F7").toString()), "R");
				this.print(0, 95, limitLength(tL4606Vo.get("F8"),14));
				this.print(0, 109, tL4606Vo.get("F9"));
				this.print(0, 120, limitLength(tL4606Vo.get("F10"),12));
				this.print(0, 138, String.format("%,d", Integer.parseInt(tL4606Vo.get("F11").toString())), "R");
				
				amt = amt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
				totamt = totamt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
				pageCnt++;
				times++;
			} else {
				if(tfire.contains(tL4606Vo.get("F9").toString()) || "".equals(tL4606Vo.get("F9").toString())) {
					times++;
				} else {
//					reset比較值
					officer = tL4606Vo.get("F9").toString();
					if(officer != "") {
						  tfire.add(officer);
					}
					this.print(1, 1, "                                                                小　計：           筆");
					this.print(0, 81, String.format("%,d", times), "R");
					this.print(0, 138, String.format("%,d", amt.intValue()), "R");
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					amt = new BigDecimal("0");
					total = total + times;
					times = 1;
					pageCnt = pageCnt + 2;
					
//					每頁第38筆 跳頁 
					if (pageCnt >= 35) {
						this.print(pageIndex - pageCnt - 2, 70, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
					}
					
				}

				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 1, tL4606Vo.get("F0"));
				this.print(0, 18, tL4606Vo.get("F1"));
				this.print(0, 27, tL4606Vo.get("F2"), "R");
				this.print(0, 36, tL4606Vo.get("F3"), "R");
				this.print(0, 45, tL4606Vo.get("F4"), "R");
				this.print(0, 46, limitLength(tL4606Vo.get("F5"), 40));
				this.print(0, 83, PadStart(7, tL4606Vo.get("F6").toString()));
				this.print(0, 90, "-");
				this.print(0, 94, PadStart(3, tL4606Vo.get("F7").toString()), "R");
				this.print(0, 95, limitLength(tL4606Vo.get("F8"),14));
				this.print(0, 109, tL4606Vo.get("F9"));
				this.print(0, 120, limitLength(tL4606Vo.get("F10"),12));
				this.print(0, 138, String.format("%,d", Integer.parseInt(tL4606Vo.get("F11").toString())), "R");
				pageCnt++;
				amt = amt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
				totamt = totamt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
			} // else

//			每頁第38筆 跳頁 
			if (pageCnt >= 35) {
				this.print(pageIndex - pageCnt - 2, 70, "=====續下頁=====", "C");

				pageCnt = 0;
				this.newPage();
			}
			
			i++;
			if (i == L4606List.size()) {
				this.print(1, 1, "                                                                小　計：           筆");
				this.print(0, 81, String.format("%,d", times), "R");
				this.print(0, 138, String.format("%,d", amt.intValue()), "R");
				this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
				total = total + times;
				times = 0;
				pageCnt = pageCnt + 2;
			}
			
//			每頁第35筆 跳頁 
			if (pageCnt >= 35) {
				this.print(pageIndex - pageCnt - 2, 70, "=====續下頁=====", "C");

				pageCnt = 0;
				this.newPage();
			}
		}

		this.print(1, 1, "                                                                總　計：           筆              火險服務人：           人 ");
		this.print(0, 81, String.format("%,d", total), "R");
		this.print(0, 119, String.format("%,d", tfire.size()), "R");
		this.print(0, 138, String.format("%,d", totamt.intValue()), "R");
		long sno = this.close();
		this.toPdf(sno);
	}

	private String PadStart(int size, String intfor) {
		for (int i = 0; i < size; i++) {
			if (intfor.length() < size) {
				intfor = "0" + intfor;
			}
		}
		return intfor;
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
