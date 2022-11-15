package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4520ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4520Report")
@Scope("prototype")

public class L4520Report extends MakeReport {

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public L4520ServiceImpl l4520ServiceImpl;
	@Autowired
	public CustMainService custMainService;

	private int header = 0;
	private String PerfMonth = "";
	private String MediaKind = "";
	private String BatchNo= "";
	
	
	private int tcount = 0;
	private BigDecimal tRepayAmt = new BigDecimal("0");
	private BigDecimal tTxAmt = new BigDecimal("0");

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		if (header == 0) {
			printHeaderP();
		} else {
			printHeaderP1();
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-2, 1, "程式ID：" + "L4520Report");
		this.print(-2, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-2, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 1, "報　表：" + "L4520Report");
		if("4".equals(MediaKind)) {
		  this.print(-3, 70, "15日薪扣薪媒體回傳作業", "C");
		} else {
		  this.print(-3, 70, "非15日薪扣薪媒體回傳作業", "C");
		}
		this.print(-3, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 70, "(更新成功明細表)", "C");
		this.print(-4, 123, "頁　　次：" + this.getNowPage(), "R");
		this.print(-5, 1, "申請年月：   " + PerfMonth.substring(0,3) + "/" + PerfMonth.substring(3,5));
		this.print(-5, 30, "申請批號：" + BatchNo);
//		this.print(-4, 60, "入帳日期：" + formatDate(entryDate));
		this.print(-7, 1, "  戶號       員工姓名              回傳訊息               應扣金額           實扣金額     員工代號     身份證字號   入帳日期    作業結果");
		this.print(-8, 0, "-------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void printHeaderP1() {
		this.print(-2, 1, "程式ID：" + "L4520Report");
		this.print(-2, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-2, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 1, "報　表：" + "L4520Report");
		if("4".equals(MediaKind)) {
			  this.print(-3, 70, "15日薪扣薪媒體回傳作業", "C");
			} else {
			  this.print(-3, 70, "非15日薪扣薪媒體回傳作業", "C");
			}
		this.print(-3, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 70, "(失敗明細表)", "C");
		this.print(-4, 123, "頁　　次：" + this.getNowPage(), "R");
		this.print(-5, 1, "申請年月：   " + PerfMonth.substring(0,3) + "/" + PerfMonth.substring(3,5));
		this.print(-5, 30, "申請批號：" + BatchNo);
//		this.print(-4, 60, "入帳日期：" + formatDate(entryDate));
		this.print(-7, 1, "  戶號       員工姓名              失敗原因               應扣金額           實扣金額     員工代號     身份證字號   入帳日期    作業結果");
		this.print(-8, 0, "-------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo1, List<Map<String, String>> fnAllList) throws LogicException {

		titaVo = titaVo1;
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值	
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		this.info("L4520Report exec");
		
		PerfMonth = titaVo.getCalDy();

		List<Map<String, String>> lEmpDeductMediaA = new ArrayList<Map<String, String>>();
		List<Map<String, String>> lEmpDeductMediaB = new ArrayList<Map<String, String>>();
		List<Map<String, String>> lEmpDeductMediaC = new ArrayList<Map<String, String>>();
		List<Map<String, String>> lEmpDeductMediaD = new ArrayList<Map<String, String>>();
		
//		List<Map<String, String>> fnAllList = new ArrayList<>();
//		
//		try {
//			fnAllList = l4520ServiceImpl.findSF(titaVo1);
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("L4520ServiceImpl.fs error = " + errors.toString());
//		}

		
		if (fnAllList != null && fnAllList.size() != 0) {
			
			for (Map<String, String> tEmpDeductMedia : fnAllList) {
				if (tEmpDeductMedia.get("ErrorCode") == null) {
					continue;
				} else if ("01".equals(tEmpDeductMedia.get("ErrorCode")) && "4".equals(tEmpDeductMedia.get("MediaKind")) 
						 || "17".equals(tEmpDeductMedia.get("ErrorCode")) && "4".equals(tEmpDeductMedia.get("MediaKind"))) {
					lEmpDeductMediaA.add(tEmpDeductMedia);
				} else if ("01".equals(tEmpDeductMedia.get("ErrorCode")) && "5".equals(tEmpDeductMedia.get("MediaKind"))
						|| "17".equals(tEmpDeductMedia.get("ErrorCode")) && "5".equals(tEmpDeductMedia.get("MediaKind"))) {	
					lEmpDeductMediaC.add(tEmpDeductMedia);
				} else if(!"01".equals(tEmpDeductMedia.get("ErrorCode")) && !"17".equals(tEmpDeductMedia.get("ErrorCode")) 
						 && "4".equals(tEmpDeductMedia.get("MediaKind"))) {
					lEmpDeductMediaB.add(tEmpDeductMedia);
				} else if(!"01".equals(tEmpDeductMedia.get("ErrorCode")) && !"17".equals(tEmpDeductMedia.get("ErrorCode")) 
						 && "5".equals(tEmpDeductMedia.get("MediaKind"))) {
					lEmpDeductMediaD.add(tEmpDeductMedia);
				}
				
				tcount++; // 總和
				tRepayAmt = tRepayAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get("RepayAmt"))); // 總和
				tTxAmt = tTxAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get("TxAmt"))); // 總和
			} // for

			int Asize = lEmpDeductMediaA.size();
			int Bsize = lEmpDeductMediaB.size();
			int Csize = lEmpDeductMediaC.size();
			int Dsize = lEmpDeductMediaD.size();
			
			if (Asize != 0) {
				setReportA(lEmpDeductMediaA, titaVo1,true); // 15 成功
				this.info("Asize =" + Asize);
			}

			if (Csize != 0) {
				setReportA(lEmpDeductMediaC, titaVo1,true); // 非15 成功
				this.info("Csize =" + Csize);
			}
			
			if (Bsize != 0) {
				header = 1;
				setReportB(lEmpDeductMediaB, titaVo1,false); // 15 失敗
				this.info("Bsize =" + Bsize);
			}
			
			if (Dsize != 0) {
				header = 1;
				setReportB(lEmpDeductMediaD, titaVo1,false); // 非15 失敗	
				this.info("Dsize =" + Dsize);
			}
			
		} 
	}

	private void setReportA(List<Map<String, String>> tEmpDeductMedia, TitaVo titaVo,boolean isSuccess) throws LogicException {

		long sno = 0;

//		MediaKind = tEmpDeductMedia.get(0).get("MediaKind");
		BatchNo = tEmpDeductMedia.get(0).get("BatchNo");
		if(isSuccess) {
		  this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "15日薪扣薪媒體回傳成功明細表", "", "A4", "L");
		} else {
		  this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "非15日薪扣薪媒體回傳成功明細表", "", "A4", "L");
		}
		
		DecimalFormat df1 = new DecimalFormat("#,##0");

		BigDecimal RepayAmt = new BigDecimal("0");
		BigDecimal TxAmt = new BigDecimal("0");

//		BigDecimal totalRepayAmt = new BigDecimal("0");
//		BigDecimal totalTxAmt = new BigDecimal("0");

		int total = 0, i = 0, pageCnt = 0;
		for (int j = 1; j <= tEmpDeductMedia.size(); j++) {

			i = j - 1;

//			1.每筆先印出明細
			this.print(1, 1,
					"                                                                                                                                                                               ");
			this.print(0, 2, FormatUtil.pad9(tEmpDeductMedia.get(i).get("CustNo"), 7)); // 戶號

			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(parse.stringToInteger(tEmpDeductMedia.get(i).get("CustNo")), parse.stringToInteger(tEmpDeductMedia.get(i).get("CustNo")));

			if (tCustMain != null) {

				int nameLength = 10;
				if (tCustMain.getCustName().length() < 10) {
					nameLength = tCustMain.getCustName().length();
				}
				this.print(0, 14, tCustMain.getCustName().substring(0, nameLength));// 員工姓名
			} else {
				this.print(0, 14, "");// 員工姓名
			}

			String Msg = "";
			switch (tEmpDeductMedia.get(i).get("ErrorCode")) {
			case "01":
				Msg = "成功";
				break;
			case "16":
				Msg = "扣款失敗";
				break;
			case "17":
				Msg = "扣款不足";
				break;
			default:
				Msg = "無此錯誤代碼";
				break;
			}

			this.print(0, 38, tEmpDeductMedia.get(i).get("ErrorCode"));// 回傳訊息

			this.print(0, 64, df1.format(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("RepayAmt"))), "R");// 應扣金額
			this.print(0, 83, df1.format(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("TxAmt"))), "R");// 實扣金額

			if (tCustMain != null) {
				this.print(0, 89, tCustMain.getEmpNo());// 員工代號
				this.print(0, 100, tCustMain.getCustId());// 身分證字號
			} else {
				this.print(0, 89, "");// 員工代號
				this.print(0, 100, "");// 身分證字號
			}

			String EntryDate = String.valueOf(parse.stringToInteger(tEmpDeductMedia.get(i).get("EntryDate")) -19110000);
			if(EntryDate.length() == 7) {
				this.print(0, 113, EntryDate.substring(0,3) + "/" + EntryDate.substring(3,5) + "/" + EntryDate.substring(5,7));// 入帳日期
			} else {
				this.print(0, 113, EntryDate.substring(0,2) + "/" + EntryDate.substring(2,4) + "/" + EntryDate.substring(4,6));// 入帳日期
			}
			
			this.print(0, 124, Msg);// 作業結果
//			每頁應扣金額，實扣金額總和
			RepayAmt = RepayAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("RepayAmt")));
			TxAmt = TxAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("TxAmt")));

//			全部應扣金額，實扣金額總和			
//			totalRepayAmt = totalRepayAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("RepayAmt")));
//			totalTxAmt = totalTxAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("TxAmt")));

//			全部筆數統計
			total++;

//			每頁筆數相加
			pageCnt++;

//			2.再與下一筆比較，決定是否換行或換頁
			if (j != tEmpDeductMedia.size()) {

				MediaKind = tEmpDeductMedia.get(j).get("MediaKind");
				BatchNo = tEmpDeductMedia.get(j).get("BatchNo");
//				每頁第40筆 跳頁 
				if (pageCnt == 40) {
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "         失敗筆數：                                                                                                             ");
					this.print(0, 27, String.format("%,d", pageCnt), "R");
					this.print(0, 64, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 83, df1.format(TxAmt), "R");// 實扣金額
					this.print(1, 70, "=====續下頁=====", "C");

					RepayAmt = new BigDecimal("0");
					TxAmt = new BigDecimal("0");

					pageCnt = 0;
					this.newPage();
					continue;
				}
			} else {
//			3.若為最後一筆，則固定產出小計、總計、報表合計
				if (total == tEmpDeductMedia.size()) {
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "         成功筆數：                                                                                                             ");
					this.print(0, 27, String.format("%,d", total), "R");
					this.print(0, 64, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 83, df1.format(TxAmt), "R");// 實扣金額

					this.print(1, 1, "         總計筆數：                                                                                                             ");
					this.print(0, 64, df1.format(tRepayAmt), "R");// 應扣金額
					this.print(0, 83, df1.format(tTxAmt), "R");// 實扣金額
					this.print(0, 27, String.format("%,d", tcount), "R");

				}
			}
		}
		sno = this.close();
//		this.toPdf(sno);
		this.toPdf(sno, "成功明細表");

	}

	private void setReportB(List<Map<String, String>> tEmpDeductMedia, TitaVo titaVom,boolean isSuccess) throws LogicException {

		long sno = 0;

//		MediaKind = tEmpDeductMedia.get(0).get("MediaKind");
		BatchNo = tEmpDeductMedia.get(0).get("BatchNo");
		if(isSuccess) {
		  this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "15日薪扣薪媒體回傳失敗明細表", "", "A4", "L");
		} else {
		  this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "非15日薪扣薪媒體回傳失敗明細表", "", "A4", "L");
		}
		

		DecimalFormat df1 = new DecimalFormat("#,##0");

		BigDecimal RepayAmt = new BigDecimal("0");
		BigDecimal TxAmt = new BigDecimal("0");

//		BigDecimal totalRepayAmt = new BigDecimal("0");
//		BigDecimal totalTxAmt = new BigDecimal("0");

		int total = 0, i = 0, pageCnt = 0;

		for (int j = 1; j <= tEmpDeductMedia.size(); j++) {

			i = j - 1;

//			1.每筆先印出明細
			this.print(1, 1,
					"                                                                                                                                                                               ");
			this.print(0, 2, FormatUtil.pad9(tEmpDeductMedia.get(i).get("CustNo"), 7)); // 戶號

			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(parse.stringToInteger(tEmpDeductMedia.get(i).get("CustNo")), parse.stringToInteger(tEmpDeductMedia.get(i).get("CustNo")));

			if (tCustMain != null) {

				int nameLength = 10;
				if (tCustMain.getCustName().length() < 10) {
					nameLength = tCustMain.getCustName().length();
				}
				this.print(0, 14, tCustMain.getCustName().substring(0, nameLength));// 員工姓名
			} else {
				this.print(0, 14, "");// 員工姓名
			}

			String Msg = "";
			switch (tEmpDeductMedia.get(i).get("ErrorCode")) {
			case "01":
				Msg = "成功";
				break;
			case "16":
				Msg = "扣款失敗";
				break;
			case "17":
				Msg = "扣款不足";
				break;
			default:
				Msg = "無此錯誤代碼";
				break;
			}

			this.print(0, 38, tEmpDeductMedia.get(i).get("ErrorCode"));// 回傳訊息

			this.print(0, 64, df1.format(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("RepayAmt"))), "R");// 應扣金額
			this.print(0, 83, df1.format(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("TxAmt"))), "R");// 實扣金額
			
			if (tCustMain != null) {
				this.print(0, 89, tCustMain.getEmpNo());// 員工代號
				this.print(0, 100, tCustMain.getCustId());// 身分證字號
			} else {
				this.print(0, 89, "");// 員工代號
				this.print(0, 100, "");// 身分證字號
			}

			String AcDate = String.valueOf(parse.stringToInteger(tEmpDeductMedia.get(i).get("AcDate")) -19110000);
			if(AcDate.length() == 7) {
				this.print(0, 113, AcDate.substring(0,3) + "/" + AcDate.substring(3,5) + "/" + AcDate.substring(5,7));// 入帳日期
			} else {
				this.print(0, 113, AcDate.substring(0,2) + "/" + AcDate.substring(2,4) + "/" + AcDate.substring(4,6));// 入帳日期
			}
			
			this.print(0, 124, Msg);// 作業結果
//			每頁應扣金額，實扣金額總和
			RepayAmt = RepayAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("RepayAmt")));
			TxAmt = TxAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("TxAmt")));

//			全部應扣金額，實扣金額總和			
//			totalRepayAmt = totalRepayAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("RepayAmt")));
//			totalTxAmt = totalTxAmt.add(parse.stringToBigDecimal(tEmpDeductMedia.get(i).get("TxAmt")));

//			全部筆數統計
			total++;

//			每頁筆數相加
			pageCnt++;

//			2.再與下一筆比較，決定是否換行或換頁
			if (j != tEmpDeductMedia.size()) {
				MediaKind = tEmpDeductMedia.get(j).get("MediaKind");
				BatchNo = tEmpDeductMedia.get(j).get("BatchNo");
				
//				每頁第40筆 跳頁 
				if (pageCnt == 40) {
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "         失敗筆數：                                                                                                             ");
					this.print(0, 27, String.format("%,d", pageCnt), "R");
					this.print(0, 64, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 83, df1.format(TxAmt), "R");// 實扣金額
					this.print(1, 70, "=====續下頁=====", "C");

					RepayAmt = new BigDecimal("0");
					TxAmt = new BigDecimal("0");

					pageCnt = 0;
					this.newPage();
					continue;
				}
			} else {
//			3.若為最後一筆，則固定產出小計、總計、報表合計
				if (total == tEmpDeductMedia.size()) {
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "         失敗筆數：                                                                                                             ");
					this.print(0, 27, String.format("%,d", total), "R");
					this.print(0, 64, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 83, df1.format(TxAmt), "R");// 實扣金額

					this.print(1, 1, "         總計筆數：                                                                                                             ");
					this.print(0, 64, df1.format(tRepayAmt), "R");// 應扣金額
					this.print(0, 83, df1.format(tTxAmt), "R");// 實扣金額
					this.print(0, 27, String.format("%,d", tcount), "R");

				}
			}
		}
		sno = this.close();
//		this.toPdf(sno);
		this.toPdf(sno, "失敗明細表");
	}

}
