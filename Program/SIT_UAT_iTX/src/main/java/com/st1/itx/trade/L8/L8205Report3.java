package com.st1.itx.trade.L8;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L8205ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class L8205Report3 extends MakeReport {

	@Autowired
	public L8205ServiceImpl l8205ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

//	自訂表頭
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);
	}

	public void printHeaderL() {
		this.setFontSize(10);
		
		this.print(-3, 5, "");
		this.print(-3, 50, "");
		this.print(-4, 5, "");
		this.print(-4, 60, "疑似洗錢交易登記表");
		this.print(-3, 80, "");
		this.print(-4, 113, "機密等級 : 機密");
		this.print(-5, 113, "文件持有人請嚴加管控本項文件" );
	}

	// 自訂表頭
		@Override
		public void printFooter() {
			String bcDate = dDateUtil.getNowStringBc().substring(0, 4) + "/" + dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6, 8);
		print(-47, 1, "　　經辦:　　　　　　　　　　　　　　　　　　　　　　　　　　　　　課主管:　　　　　　　　　　　　　　                    "+bcDate+"製作:放款服務課", "L");
		
		}
	public boolean exec(TitaVo titaVo) throws LogicException {


		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L8205", "疑似洗錢交易登記表", "", "A4", "L");

		List<Map<String, String>> L8205List = null;

		try {
			L8205List = l8205ServiceImpl.L8205Rpt3(titaVo);
		} catch (Exception e) {
			this.info("l8205ServiceImpl.L8205Rpt3 error = " + e.toString());
		}

		if (L8205List != null && L8205List.size() > 0) {
			DecimalFormat df1 = new DecimalFormat("#,##0");

			this.print(-6, 3, "　　　　預計還款 實際還款　　　　　　　　　　　　　　　　　　　　　　　 年收入");
			this.print(-7, 3, "訪談日期　日期　　 日期 　　戶號　　　戶名　　　還款金額　　　　職業別 　(萬) 　　　還款來源　　代償銀行　　其他說明　　　　　　　　　　　　　經辦	");
			this.print(-9, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

			for (Map<String, String> tL8205Vo : L8205List) {
				
				//訪談日期
				int recorddate = Integer.parseInt(tL8205Vo.get("F0"))-19110000;
				print(1,3,String.valueOf(recorddate));
				
				//預計還款日期
				int repaydate = Integer.parseInt(tL8205Vo.get("F1"));
				if(repaydate!=0) {
					repaydate = repaydate-19110000;
				}
				print(0,11,String.valueOf(repaydate));
				
				//實際還款日期
				int acrepaydate = Integer.parseInt(tL8205Vo.get("F2"));
				if(acrepaydate==0) {
					print(0,19,"");
				} else if(acrepaydate>0){
					acrepaydate = acrepaydate-19110000;
					print(0,19,String.valueOf(acrepaydate));
				}
				
				
				// 戶號
				print(0, 28, padStart(tL8205Vo.get("F3"), 7, "0"));

				// 戶名
				print(0, 36, tL8205Vo.get("F4"));


				
				// 還款金額
				BigDecimal f5 = tL8205Vo.get("F5") == "0" || tL8205Vo.get("F5") == null || tL8205Vo.get("F5").length() == 0 || tL8205Vo.get("F5").equals(" ") ? BigDecimal.ZERO
						: new BigDecimal(tL8205Vo.get("F5"));

				print(0, 59, f5.equals(BigDecimal.ZERO) ? " " : df1.format(f5), "R");

				
				// 職業別
				print(0, 60, tL8205Vo.get("F6"));
				
				// 年收入
				if(tL8205Vo.get("F7").isEmpty()) {
					print(0, 71, "0");
				} else {
					print(0, 71, tL8205Vo.get("F7"));
				}
				
				
				// 還款來源
				String sourse = tL8205Vo.get("F8");
				if(sourse.length()<2) {
					sourse = "0"+sourse;
				}
				print(0, 79, sourse);
				//還款來源中文
				print(0, 81, tL8205Vo.get("F12"));
				
				
				// 代償銀行
				print(0, 90, tL8205Vo.get("F9"));
				
				// 其他說明
				String description = "";
				description = tL8205Vo.get("F10").replace("$n", "");
				print(0, 100, description);
				
				
				//經辦
				print(0, 132, tL8205Vo.get("F13"));
				
				// 檢查列數
				checkRow();
			}

		} else {

			this.print(1, 3, "無資料");
			this.print(-6, 3, "　　　　預計還款 實際還款　　　　　　　　　　　　　　　　　　　　　　　 年收入");
			this.print(-7, 3, "訪談日期　日期　　 日期 　　戶號　　　戶名　　　還款金額　　　　職業別 　(萬) 　還款來源　　代償銀行　　其他說明　　　　　　　　　　　　　　經辦	");
			this.print(-9, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");


		}

		long sno = this.close();
		this.toPdf(sno);

		if (L8205List != null && L8205List.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

	/**
	 * 檢查列數
	 * 
	 * @param cnMin 借款人戶號 區間-起
	 * @param cnMax 借款人戶號 區間-止	
	 */
	private void checkRow() {
		if (this.NowRow >= 40) {
//
			newPage();
			this.print(-6, 3, "　　　　預計還款 實際還款　　　　　　　　　　　　　　　　　　　　　　　 年收入");
			this.print(-7, 3, "訪談日期　日期　　 日期 　　戶號　　　戶名　　　還款金額　　　　職業別 　(萬) 　還款來源　　代償銀行　　其他說明　　　　　　　　　　　　　　　經辦	");
			this.print(-9, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		}
//
	}

//	private String showDate(String date, int iType) {
////		this.info("MakeReport.toPdf showRocDate1 = " + date);
//		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
//			return " ";
//		}
//		int rocdate = Integer.valueOf(date);
//		if (rocdate > 19110000) {
//			rocdate -= 19110000;
//		}
//		String rocdatex = String.valueOf(rocdate);
////		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
//
//		if (rocdatex.length() == 7) {
//			return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
//		} else {
//			return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);
//
//		}
//
//	}

}
