package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM013ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM013Report extends MakeReport {

	@Autowired
	public LM013ServiceImpl lM013ServiceImpl;

	@Autowired
	DateUtil dDateUtil;
	
	private BigDecimal marginAmount;
	private String validDate;

	private String newBorder = "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	
	@Override
	public void printHeader() {
		
		printHeaderL();
		
	}
	
	private void printHeaderL() {
		
		print(-3, 3, "程式ID："+ this.getParentTranCode());
		print(-4, 3, "報  表："+ this.getRptCode());
		
		print(-1, 192, "機密等級：密");
		print(-2, 192, "日  期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		print(-3, 192, "時  間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		print(-4, 192, "頁  數：" + this.getNowPage());
		
		print(-3, newBorder.length()/2, "新光人壽保險股份有限公司", "C");
		print(-4, newBorder.length()/2, "金檢報表(放款種類表)", "C");
		
		print(-6, 1, "核貸總值分界. " + formatAmt(marginAmount, 0));
		print(-7, 1, "金檢日期..... " + validDate);
		
		print(-9, 0, "          放款對象                                                核貸總值                                                                                  帳面總值");
		print(-10, 1, "戶號   額度  名稱    統編");
		print(-11, 0, "                          不動產抵押       動產抵押       有價證券       銀行保證       專案放款           合計      不動產抵押       動產抵押       有價證券       銀行保證       專案放款           合計");
		print(-12, 0, newBorder);

		
	}

	// private static final Logger logger = LoggerFactory.getLogger(LM013Report.class);

	public Boolean exec(TitaVo titaVo) throws LogicException {
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM013", "金檢報表(放款種類表)", "密", "A4", "L");
		
		// 明細起始列(自訂亦必須)
		this.setBeginRow(13);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(44);
		
		this.setCharSpaces(0);
		
		this.setFont(1, 8);
		
		marginAmount = new BigDecimal(titaVo.getParam("inputAmount"));
		validDate = showRocDate(Integer.valueOf(titaVo.getParam("inputDate"))+19110000, 1);

		List<Map<String, String>> LM013List = null;
		try {
			LM013List = lM013ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lM013ServiceImpl.findAll error = " + e.toString());
		}

		
		// 以上/以下合計用array:
		// 核貸 不動產 0 動產 1 有價 2 銀行 3 專案 4 合計 5
		// 帳面 不動產 6 動產 7 有價 8 銀行 9 專案 10 合計 11
		// 以下合計 +0
		// 以上合計 +12
		
		BigDecimal[] totalArray = new BigDecimal[24];
		Arrays.fill(totalArray, BigDecimal.ZERO);
		
		if (LM013List != null &&LM013List.size() != 0) {
			
			// these are for totalArray
			int arrayPosShift = 0;
			int arrayPos = 0;

			String CustNo = "";
			BigDecimal[] sum = new BigDecimal[2];
			for(int i = 0 ; i < 2 ; i++) {
				sum[i] = BigDecimal.ZERO;
			}
			for (int i = 0; i < LM013List.size(); i++) {

				if (i == 0) { // 第一筆
					CustNo = PadStart(7, LM013List.get(i).get("F2").toString());
					print(1, 1, PadStart(7, LM013List.get(i).get("F2").toString()) + LM013List.get(i).get("F3"));

/**          1         2         3         4         5         6         7         8         9         0         1         2         3         4         5         6         7         8         9         0         1         2
/** 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890 */
/**	                          不動產抵押       動產抵押       有價證券       銀行保證       專案放款           合計      不動產抵押      動產抵押       有價證券       銀行保證       專案放款           合計*/
/** 0000007AA催收戶AAAAB123456789A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A這裡十四個半形A */
					BigDecimal f7 = LM013List.get(i).get("F7") == null ? BigDecimal.ZERO : new BigDecimal(LM013List.get(i).get("F7").toString());
					BigDecimal f8 = LM013List.get(i).get("F7") == null ? BigDecimal.ZERO : new BigDecimal(LM013List.get(i).get("F8").toString());

					switch (LM013List.get(i).get("F6").toString()) {
					case "0": // 專案
						print(0, 96, formatAmt(f7,0), "R");
						print(0, 187, formatAmt(f8,0), "R");
						
						arrayPos = 4;
						
						break;
					case "1":
					case "2": // 不動產
						print(0, 36, formatAmt(f7,0), "R");
						print(0, 127, formatAmt(f8,0), "R");
						
						arrayPos = 0;
						break;
					case "3":
					case "4": // 有價證券
						print(0, 66, formatAmt(f7,0), "R");
						print(0, 157, formatAmt(f8,0), "R");
						
						arrayPos = 2;
						break;
					case "5": // 銀行保證
						print(0, 81, formatAmt(f7,0), "R");
						print(0, 172, formatAmt(f8,0), "R");
						
						arrayPos = 3;
						break;
					case "9": // 動產
						print(0, 51, formatAmt(f7,0), "R");
						print(0, 142, formatAmt(f8,0), "R");
						
						arrayPos = 1;
						
						break;
					default:
						f7 = BigDecimal.ZERO;
						f8 = BigDecimal.ZERO;
						break;
					} // switch
					
					arrayPosShift = (f7.compareTo(marginAmount) < 0 ? 0 : 12);
					totalArray[arrayPos+arrayPosShift] = totalArray[arrayPos+arrayPosShift].add(f7);
					totalArray[5+arrayPosShift] = totalArray[5+arrayPosShift].add(f7); // 小計用

					arrayPosShift = (f8.compareTo(marginAmount) < 0 ? 0 : 12);
					totalArray[arrayPos+6+arrayPosShift] = totalArray[arrayPos+6+arrayPosShift].add(f8);
					totalArray[11+arrayPosShift] = totalArray[11+arrayPosShift].add(f8); // 小計用
					
					sum[0] = sum[0].add(f7);
					sum[1] = sum[1].add(f8);

				} else { // 後面筆數都要判斷戶號是否不同

					if (!CustNo.equals(PadStart(7, LM013List.get(i).get("F2").toString()))) {
						CustNo = PadStart(7, LM013List.get(i).get("F2").toString());

						print(1, 1, PadStart(7, LM013List.get(i - 1).get("F2").toString()));
						String custName = LM013List.get(i - 1).get("F5").toString();
						if(custName.length() > 5) {
							print(0, 10, custName.substring(0, 5));
						}else {
							print(0, 10, custName);
						}
						print(0, 20, LM013List.get(i - 1).get("F4").toString());

						print(0, 111, formatAmt(sum[0],0), "R");
						print(0, 202, formatAmt(sum[1],0), "R");

						sum[0] = new BigDecimal("0");
						sum[1] = new BigDecimal("0");

						print(1, 0, newBorder);

					} // if 先印前一筆所有 後印當下這筆

					print(1, 1, PadStart(7, LM013List.get(i).get("F2").toString()) + LM013List.get(i).get("F3"));
					// 1 4 0 9 5

					BigDecimal f7 = new BigDecimal(LM013List.get(i).get("F7").toString());
					BigDecimal f8 = new BigDecimal(LM013List.get(i).get("F8").toString());

					switch (LM013List.get(i).get("F6").toString()) {
					case "0": // 專案
						print(0, 96, formatAmt(f7,0), "R");
						print(0, 187, formatAmt(f8,0), "R");
						
						arrayPos = 4;
						
						break;
					case "1":
					case "2": // 不動產
						print(0, 36, formatAmt(f7,0), "R");
						print(0, 127, formatAmt(f8,0), "R");
						
						arrayPos = 0;
						break;
					case "3":
					case "4": // 有價證券
						print(0, 66, formatAmt(f7,0), "R");
						print(0, 157, formatAmt(f8,0), "R");
						
						arrayPos = 2;
						break;
					case "5": // 銀行保證
						print(0, 81, formatAmt(f7,0), "R");
						print(0, 172, formatAmt(f8,0), "R");
						
						arrayPos = 3;
						break;
					case "9": // 動產
						print(0, 51, formatAmt(f7,0), "R");
						print(0, 142, formatAmt(f8,0), "R");
						
						arrayPos = 1;
						
						break;
					default:
						f7 = BigDecimal.ZERO;
						f8 = BigDecimal.ZERO;
						break;
					} // switch
					
					arrayPosShift = (f7.compareTo(marginAmount) < 0 ? 0 : 12);
					totalArray[arrayPos+arrayPosShift] = totalArray[arrayPos+arrayPosShift].add(f7);
					totalArray[5+arrayPosShift] = totalArray[5+arrayPosShift].add(f7); // 小計用

					arrayPosShift = (f8.compareTo(marginAmount) < 0 ? 0 : 12);
					totalArray[arrayPos+6+arrayPosShift] = totalArray[arrayPos+6+arrayPosShift].add(f8);
					totalArray[11+arrayPosShift] = totalArray[11+arrayPosShift].add(f8); // 小計用

					sum[0] = sum[0].add(f7);
					sum[1] = sum[1].add(f8);

				} // else

				if (i == LM013List.size() - 1) { // 當筆為最後一筆

					print(1, 1, PadStart(7, LM013List.get(i).get("F2").toString()));
					String custName = LM013List.get(i - 1).get("F5").toString();
					if(custName.length() > 5) {
						print(0, 10, custName.substring(0, 5));
					}else {
						print(0, 10, custName);
					}
					print(0, 20, LM013List.get(i).get("F4").toString());

					print(0, 111, formatAmt(sum[0],0), "R");
					print(0, 202, formatAmt(sum[1],0), "R");

					sum[0] = new BigDecimal("0");
					sum[1] = new BigDecimal("0");

					print(1, 0, newBorder);
				}

			} // for

			print(1, 1, "以上合計");
			
			for (int i = 12; i < 18; i++)
			{
				print(0, 36 + (i-12)*15, formatAmt(totalArray[i], 0), "R");
				print(0, 127 + (i-12)*15, formatAmt(totalArray[i + 6], 0), "R");
			}
			
			print(1, 1, "以下合計");
			
			for (int i = 0; i < 6; i++)
			{
				print(0, 36 + i*15, formatAmt(totalArray[i], 0), "R");
				print(0, 127 + i*15, formatAmt(totalArray[i + 6], 0), "R");
			}
			
			print(1, 0, newBorder);
			print(1, 1, "總計");
			
			for (int i = 0; i < 6; i++)
			{
				print(0, 36 + i*15, formatAmt(totalArray[i].add(totalArray[i+12]), 0), "R");
				print(0, 127 + i*15, formatAmt(totalArray[i + 6].add(totalArray[i+6+12]), 0), "R");
			}
			
			print(1, 0, newBorder);

		}else{
			print(1, 1, "本日無資料");
			print(1, 0, newBorder);
			print(1, 0, newBorder);
			print(1, 1, "以上合計");
			print(1, 1, "以下合計");
			print(1, 0, newBorder);
			print(1, 1, "總計");
			print(1, 0, newBorder);
		}
		
		
		long sno = this.close();
		this.toPdf(sno);
		
		return true;
	}

	private String PadStart(int size, String intfor) {
		for (int i = 0; i < size; i++) {
			if (intfor.length() < size) {
				intfor = "0" + intfor;
			}
		}
		return intfor;
	}
}
