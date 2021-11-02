package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
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
	public CustMainService custMainService;

	private int header = 0;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		if (header == 0) {
			printHeaderP();
		} else {
			printHeaderP1();
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 70, "更新成功明細表", "C");
		this.print(-2, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
//		this.print(-4, 1, "業績年月：" + perfMonth);
//		this.print(-4, 30, "流程別：" + procCode);
//		this.print(-4, 60, "入帳日期：" + formatDate(entryDate));
		this.print(-5, 1, " 戶號    員工姓名              回傳訊息                       應扣金額           實扣金額   員工代號   身份證字號 入帳日期  作業結果");
		this.print(-6, 0, "-------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void printHeaderP1() {
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 70, "更新失敗明細表", "C");
		this.print(-2, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
//		this.print(-4, 1, "業績年月：" + perfMonth);
//		this.print(-4, 30, "流程別：" + procCode);
//		this.print(-4, 60, "入帳日期：" + formatDate(entryDate));
		this.print(-5, 1, " 戶號    員工姓名              失敗原因                       應扣金額           實扣金額   員工代號   身份證字號 入帳日期  作業結果");
		this.print(-6, 0, "-------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo1) throws LogicException {

		titaVo = titaVo1;
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值	
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		this.info("L4520Report exec");

		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int iPerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		String iProcCode = titaVo.getParam("ProcCode");

		List<EmpDeductMedia> lEmpDeductMedia = new ArrayList<EmpDeductMedia>();
		List<EmpDeductMedia> lEmpDeductMediaA = new ArrayList<EmpDeductMedia>();
		List<EmpDeductMedia> lEmpDeductMediaB = new ArrayList<EmpDeductMedia>();
		Slice<EmpDeductMedia> sEmpDeductMedia = null;

		sEmpDeductMedia = empDeductMediaService.findL4520A(iAcDate, iPerfMonth, iProcCode, this.index, this.limit);

		lEmpDeductMedia = sEmpDeductMedia == null ? null : sEmpDeductMedia.getContent();

		if (lEmpDeductMedia != null && lEmpDeductMedia.size() != 0) {
			for (EmpDeductMedia tEmpDeductMedia : lEmpDeductMedia) {
				if (tEmpDeductMedia.getErrorCode() == null) {
					continue;
				} else if ("01".equals(tEmpDeductMedia.getErrorCode())) {
					lEmpDeductMediaA.add(tEmpDeductMedia);
				} else {
					lEmpDeductMediaB.add(tEmpDeductMedia);
				} // else
			} // for

			int Asize = lEmpDeductMediaA.size();
			int Bsize = lEmpDeductMediaB.size();

			if (Asize != 0) {
				setReportA(lEmpDeductMediaA, titaVo1);
			}

			if (Bsize != 0) {
				header = 1;
				setReportB(lEmpDeductMediaB, titaVo1);
			}

//			if (Asize + Bsize == 0) {
//				throw new LogicException("E0001", "查無資料");
//			}
		} else {
//			throw new LogicException("E0001", "查無資料");
		}
	}

	private void setReportA(List<EmpDeductMedia> tEmpDeductMedia, TitaVo titaVo) throws LogicException {

		long sno = 0;

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "更新成功明細表", "", "A4", "L");

		DecimalFormat df1 = new DecimalFormat("#,##0");

		BigDecimal RepayAmt = new BigDecimal("0");
		BigDecimal TxAmt = new BigDecimal("0");

		BigDecimal totalRepayAmt = new BigDecimal("0");
		BigDecimal totalTxAmt = new BigDecimal("0");

		int total = 0, i = 0, pageCnt = 0;
		for (int j = 1; j <= tEmpDeductMedia.size(); j++) {

			i = j - 1;

//			1.每筆先印出明細
			this.print(1, 1,
					"                                                                                                                                                                               ");
			this.print(0, 2, FormatUtil.pad9(parse.IntegerToString(tEmpDeductMedia.get(i).getCustNo(), 7), 7)); // 戶號

			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tEmpDeductMedia.get(i).getCustNo(), tEmpDeductMedia.get(i).getCustNo());

			if (tCustMain != null) {

				int nameLength = 10;
				if (tCustMain.getCustName().length() < 10) {
					nameLength = tCustMain.getCustName().length();
				}
				this.print(0, 10, tCustMain.getCustName().substring(0, nameLength));// 員工姓名
			} else {
				this.print(0, 10, "");// 員工姓名
			}

			String Msg = "";
			switch (tEmpDeductMedia.get(i).getErrorCode()) {
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

			this.print(0, 31, Msg);// 回傳訊息

			this.print(0, 69, df1.format(tEmpDeductMedia.get(i).getRepayAmt()), "R");// 應扣金額
			this.print(0, 87, df1.format(tEmpDeductMedia.get(i).getTxAmt()), "R");// 實扣金額

			if (tCustMain != null) {
				this.print(0, 90, tCustMain.getEmpNo());// 員工代號
				this.print(0, 100, tCustMain.getCustId());// 身分證字號
			} else {
				this.print(0, 90, "");// 員工代號
				this.print(0, 100, "");// 身分證字號
			}

			this.print(0, 111, parse.IntegerToString(tEmpDeductMedia.get(i).getAcDate(), 7));// 入帳日期
			this.print(0, 128, "  ");// 作業結果
//			每頁應扣金額，實扣金額總和
			RepayAmt = RepayAmt.add(tEmpDeductMedia.get(i).getRepayAmt());
			TxAmt = TxAmt.add(tEmpDeductMedia.get(i).getTxAmt());

//			全部應扣金額，實扣金額總和			
			totalRepayAmt = totalRepayAmt.add(RepayAmt);
			totalTxAmt = totalTxAmt.add(TxAmt);

//			全部筆數統計
			total++;

//			每頁筆數相加
			pageCnt++;

//			2.再與下一筆比較，決定是否換行或換頁
			if (j != tEmpDeductMedia.size()) {

//				每頁第41筆 跳頁 
				if (pageCnt == 41) {
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "         失敗筆數：           筆                                                                                                  ");
					this.print(0, 27, String.format("%,d", pageCnt), "R");
					this.print(0, 69, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 87, df1.format(TxAmt), "R");// 實扣金額
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
					this.print(1, 1, "         成功筆數：           筆                                                                                                  ");
					this.print(0, 27, String.format("%,d", pageCnt), "R");
					this.print(0, 69, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 87, df1.format(TxAmt), "R");// 實扣金額

					this.print(1, 1, "         總計筆數：           筆                                                                                                  ");
					this.print(0, 69, df1.format(totalRepayAmt), "R");// 應扣金額
					this.print(0, 87, df1.format(totalTxAmt), "R");// 實扣金額
					this.print(0, 27, String.format("%,d", total), "R");

				}
			}
		}
		sno = this.close();
		this.toPdf(sno);

	}

	private void setReportB(List<EmpDeductMedia> tEmpDeductMedia, TitaVo titaVo) throws LogicException {

		long sno = 0;

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "更新失敗明細表", "", "A4", "L");

		DecimalFormat df1 = new DecimalFormat("#,##0");

		BigDecimal RepayAmt = new BigDecimal("0");
		BigDecimal TxAmt = new BigDecimal("0");

		BigDecimal totalRepayAmt = new BigDecimal("0");
		BigDecimal totalTxAmt = new BigDecimal("0");

		int total = 0, i = 0, pageCnt = 0;

		for (int j = 1; j <= tEmpDeductMedia.size(); j++) {

			i = j - 1;

//			1.每筆先印出明細
			this.print(1, 1,
					"                                                                                                                                                                               ");
			this.print(0, 2, FormatUtil.pad9(parse.IntegerToString(tEmpDeductMedia.get(i).getCustNo(), 7), 7)); // 戶號

			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tEmpDeductMedia.get(i).getCustNo(), tEmpDeductMedia.get(i).getCustNo());

			if (tCustMain != null) {

				int nameLength = 10;
				if (tCustMain.getCustName().length() < 10) {
					nameLength = tCustMain.getCustName().length();
				}
				this.print(0, 10, tCustMain.getCustName().substring(0, nameLength));// 員工姓名
			} else {
				this.print(0, 10, "");// 員工姓名
			}

			String Msg = "";
			switch (tEmpDeductMedia.get(i).getErrorCode()) {
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

			this.print(0, 31, Msg);// 回傳訊息

			this.print(0, 69, df1.format(tEmpDeductMedia.get(i).getRepayAmt()), "R");// 應扣金額
			this.print(0, 87, df1.format(tEmpDeductMedia.get(i).getTxAmt()), "R");// 實扣金額

			if (tCustMain != null) {
				this.print(0, 90, tCustMain.getEmpNo());// 員工代號
				this.print(0, 100, tCustMain.getCustId());// 身分證字號
			} else {
				this.print(0, 90, "");// 員工代號
				this.print(0, 100, "");// 身分證字號
			}

			this.print(0, 111, parse.IntegerToString(tEmpDeductMedia.get(i).getAcDate(), 7));// 入帳日期
			this.print(0, 128, "  ");// 作業結果
//			每頁應扣金額，實扣金額總和
			RepayAmt = RepayAmt.add(tEmpDeductMedia.get(i).getRepayAmt());
			TxAmt = TxAmt.add(tEmpDeductMedia.get(i).getTxAmt());

//			全部應扣金額，實扣金額總和			
			totalRepayAmt = totalRepayAmt.add(tEmpDeductMedia.get(i).getRepayAmt());
			totalTxAmt = totalTxAmt.add(tEmpDeductMedia.get(i).getTxAmt());

//			全部筆數統計
			total++;

//			每頁筆數相加
			pageCnt++;

//			2.再與下一筆比較，決定是否換行或換頁
			if (j != tEmpDeductMedia.size()) {

//				每頁第41筆 跳頁 
				if (pageCnt == 41) {
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "         失敗筆數：           筆                                                                                                  ");
					this.print(0, 27, String.format("%,d", pageCnt), "R");
					this.print(0, 69, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 87, df1.format(TxAmt), "R");// 實扣金額
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
					this.print(1, 1, "         失敗筆數：           筆                                                                                                  ");
					this.print(0, 27, String.format("%,d", pageCnt), "R");
					this.print(0, 69, df1.format(RepayAmt), "R");// 應扣金額
					this.print(0, 87, df1.format(TxAmt), "R");// 實扣金額

					this.print(1, 1, "         總計筆數：           筆                                                                                                  ");
					this.print(0, 69, df1.format(totalRepayAmt), "R");// 應扣金額
					this.print(0, 87, df1.format(totalTxAmt), "R");// 實扣金額
					this.print(0, 27, String.format("%,d", total), "R");

				}
			}
		}
		sno = this.close();
		this.toPdf(sno);

	}

}
