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
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;

@Component
@Scope("prototype")

public class L8205Report5 extends MakeReport {

	@Autowired
	public L8205ServiceImpl l8205ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	MakeExcel makeExcel;

	@Autowired
	CdEmpService cdEmpService;
	
	private List<Map<String, String>> L8205List = null;
	private DecimalFormat df1 = new DecimalFormat("#,##0");
	
	private String description1 ="";
	private String description2 ="";
	private String description3 ="";
	private String description4 ="";
	private String cdEmpFullname = "";
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
		this.print(-4, this.getMidXAxis(), "疑似洗錢交易登記表", "C");
		this.print(-3, 80, "");
		this.print(-4, 113, "機密等級 : 機密");
		this.print(-5, 113, "文件持有人請嚴加管控本項文件" );
	}

	// 自訂表頭
		@Override
		public void printFooter() {

		String bcDate = dDateUtil.getNowStringBc().substring(0, 4) + "/" + dDateUtil.getNowStringBc().substring(4, 6)
				+ "/" + dDateUtil.getNowStringBc().substring(6, 8);
		print(-47, 1, "　　經辦:　　　　　　　　　　　　　　　　　　　　經理:　　　　　　　　　　　　　　　　　　　　" + bcDate + "製作:　" + cdEmpFullname, "L");		

		}
	public boolean exec(TitaVo titaVo) throws LogicException {

		try {
			L8205List = l8205ServiceImpl.L8205Rpt5(titaVo);
		} catch (Exception e) {
			this.info("l8205ServiceImpl.L8205Rpt5 error = " + e.toString());
		}

		makeReport(titaVo);

		makeExcel(titaVo);
		
		if (L8205List != null && L8205List.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public void makeReport(TitaVo titaVo) throws LogicException{
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L8205", "疑似洗錢交易登記表", "", "A4", "L");
		
		if (L8205List != null && L8205List.size() > 0) {
			

			this.print(-6, 3, "　　　　預計還款 實際還款　　　　　　　　　　　　　　　　　　　　　　　 年收入");
			this.print(-7, 3, "訪談日期　日期　　 日期 　　戶號　　　戶名　　　還款金額　　　　職業別 　(萬) 　　　還款來源　　代償銀行　　其他說明　　　　　　　　　　　　　經辦	");
			this.print(-9, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

			for (Map<String, String> tL8205Vo : L8205List) {
				
				// 檢查列數
				checkRow();
				
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
				String CustName = tL8205Vo.get("F4").replace("$n","");
				if(CustName.length()>=6) {
					CustName = CustName.substring(0, 6);
				}
				
				print(0, 36, CustName);


				
				// 還款金額
				BigDecimal f5 = tL8205Vo.get("F5") == "0" || tL8205Vo.get("F5") == null || tL8205Vo.get("F5").length() == 0 || tL8205Vo.get("F5").equals(" ") ? BigDecimal.ZERO
						: new BigDecimal(tL8205Vo.get("F5"));

				print(0, 59, f5.equals(BigDecimal.ZERO) ? " " : df1.format(f5), "R");

				
				// 職業別
				print(0, 60, tL8205Vo.get("F6"));
				
				// 年收入
				if(tL8205Vo.get("F7").isEmpty()) {
					print(0, 77,"0", "R");
				} else {
					print(0, 77, tL8205Vo.get("F7"),"R");
				}
				
				
				// 還款來源
//				String sourse = tL8205Vo.get("F8");
//				if(sourse.length()<2) {
//					sourse = "0"+sourse;
//				}
//				print(0, 79, sourse);
				//還款來源中文
				print(0, 81, tL8205Vo.get("F12"));
				
				
				// 代償銀行
				print(0, 90, tL8205Vo.get("F9"));
				
				//經辦
				print(0, 132, tL8205Vo.get("F13"));
				
				// 其他說明
				String desc = tL8205Vo.get("F10").replace("$n", "");
				cutString(desc);

				
				print(0, 101, description1);
	
				if(!description2.isEmpty()) {
					print(1, 101, description2);
				}
				
				if(!description3.isEmpty()) {
					print(1, 101, description3);
				}
				if(!description4.isEmpty()) {
					print(1, 101, description4);
				}
				
			}

		} else {

			this.print(1, 3, "無資料");
			this.print(-6, 3, "　　　　預計還款 實際還款　　　　　　　　　　　　　　　　　　　　　　　 年收入");
			this.print(-7, 3, "訪談日期　日期　　 日期 　　戶號　　　戶名　　　還款金額　　　　職業別 　(萬) 　還款來源　　代償銀行　　其他說明　　　　　　　　　　　　　　經辦	");
			this.print(-9, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");


		}
		// 表尾:製表人經辦名稱
		CdEmp tCdEmp = new CdEmp();
		tCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);
		if (tCdEmp != null) {
			cdEmpFullname = tCdEmp.getFullname();
		}
		long sno = this.close();
		this.toPdf(sno);

	}
	
	public void makeExcel(TitaVo titaVo) throws LogicException{
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L8205", "疑似洗錢交易登記表", "L8205" + "_" + "疑似洗錢交易登記表");
		printExcelHeader();
		
		int rowCursor = 2;
		
		if (L8205List != null && L8205List.size() > 0) {
			
			for (Map<String, String> tL8205Vo : L8205List) {
				
				int recorddate = Integer.parseInt(tL8205Vo.get("F0"))-19110000;
				makeExcel.setValue(rowCursor, 1, recorddate);
				
				//預計還款日期
				int repaydate = Integer.parseInt(tL8205Vo.get("F1"));
				if(repaydate!=0) {
					repaydate = repaydate-19110000;
				}
				makeExcel.setValue(rowCursor, 2, repaydate);
				
				//實際還款日期
				int acrepaydate = Integer.parseInt(tL8205Vo.get("F2"));
				if(acrepaydate==0) {
					makeExcel.setValue(rowCursor, 3, "");
				} else if(acrepaydate>0){
					acrepaydate = acrepaydate-19110000;
					makeExcel.setValue(rowCursor, 3, acrepaydate);
				}
			
				
				makeExcel.setValue(rowCursor, 4, padStart(tL8205Vo.get("F3"), 7, "0"));
				
				makeExcel.setValue(rowCursor, 5, tL8205Vo.get("F4"));
				
				BigDecimal f5 = tL8205Vo.get("F5") == "0" || tL8205Vo.get("F5") == null || tL8205Vo.get("F5").length() == 0 || tL8205Vo.get("F5").equals(" ") ? BigDecimal.ZERO
						: new BigDecimal(tL8205Vo.get("F5"));
				
				makeExcel.setValue(rowCursor, 6, f5.equals(BigDecimal.ZERO) ? " " : df1.format(f5));
				
				makeExcel.setValue(rowCursor, 7, tL8205Vo.get("F6"));
				
				// 年收入
				if(tL8205Vo.get("F7").isEmpty()) {
					makeExcel.setValue(rowCursor, 8, "0");
				} else {
					makeExcel.setValue(rowCursor, 8, tL8205Vo.get("F7"));
				}
			
				//還款來源
				String sourse = tL8205Vo.get("F8")+" "+tL8205Vo.get("F12");
				if(sourse.length()<2) {
					sourse = "0"+sourse;
				}
				
				makeExcel.setValue(rowCursor, 9, sourse);
				
				makeExcel.setValue(rowCursor, 10, tL8205Vo.get("F9"));
				
				String description = "";
				description = tL8205Vo.get("F10").replace("$n", "");
				makeExcel.setValue(rowCursor, 11, description);
				
				makeExcel.setValue(rowCursor, 12, tL8205Vo.get("F13"));
				
				
				
				
				rowCursor++;
			}
			
			
			
		}
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
		
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

			newPage();
			this.print(-6, 3, "　　　　預計還款 實際還款　　　　　　　　　　　　　　　　　　　　　　　 年收入");
			this.print(-7, 3, "訪談日期　日期　　 日期 　　戶號　　　戶名　　　還款金額　　　　職業別 　(萬) 　還款來源　　代償銀行　　其他說明　　　　　　　　　　　　　　　經辦	");
			this.print(-9, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		}

	}

	private void printExcelHeader() throws LogicException {
		makeExcel.setValue(1, 1, "訪談日期");
		makeExcel.setWidth(1, 14);
		
		makeExcel.setValue(1, 2, "預計還款日期");
		makeExcel.setWidth(2, 18);
		
		makeExcel.setValue(1, 3, "實際還款日期");
		makeExcel.setWidth(3, 18);
		
		
		makeExcel.setValue(1, 4, "戶號");
		makeExcel.setWidth(4, 16);
		
		makeExcel.setValue(1, 5, "戶名");
		makeExcel.setWidth(5, 20);
		
		makeExcel.setValue(1, 6, "還款金額");
		makeExcel.setWidth(6, 20);
		
		makeExcel.setValue(1, 7, "職業別");
		
		makeExcel.setValue(1, 8, "年收入(萬)");
		makeExcel.setWidth(8, 16);
		
		makeExcel.setValue(1, 9, "還款來源");
		makeExcel.setWidth(9, 18);
		
		makeExcel.setValue(1, 10, "代償銀行");
		makeExcel.setWidth(10, 20);
		
		makeExcel.setValue(1, 11, "其他說明");
		makeExcel.setWidth(11, 40);
		
		makeExcel.setValue(1, 12, "經辦");
		makeExcel.setWidth(12, 20);
		
		
	}

	public void  cutString(String desc) throws LogicException{
		
		description1 ="";
		description2 ="";
		description3 ="";
		description4 ="";
		
		int size = desc.length();
		if(size>=64) {
			size = 64;
			desc = desc.substring(0, 64);
		} 
		
		if(size>48) {
			description1 = desc.substring(0, 16);
			description2 = desc.substring(16, 32);
			description3 = desc.substring(32, 48);
			description4 = desc.substring(48, size);
		}else if(size>32) {
			description1 = desc.substring(0, 16);
			description2 = desc.substring(16, 32);
			description3 = desc.substring(32, size);
		}else if(size>16) {
			description1 = desc.substring(0, 16);
			description2 = desc.substring(16, size);
		} else {
			description1 = desc;
		}

		
	}
	
}
