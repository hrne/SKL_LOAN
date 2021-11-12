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
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;

@Component("L4450Report")
@Scope("prototype")

public class L4450Report extends MakeReport {

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private CdCodeService cdCodeService;

	@Autowired
	private CdBankService cdBankService;

	@Autowired
	public InsuRenewService insuRenewService;
	
	private String repayBank = "";
	private int RepayType = 0;
	// 明細資料容器
	private List<BankDeductDtl> lBankDeductDtl = new ArrayList<>();

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 1, "程式ID：" + "L4450");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 1, "報　表：" + "L4450");
		this.print(-2, 70, "銀行扣款明細表", "C");
		this.print(-2, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 1, "扣款銀行：" + repayBank + " " + bankCodeX(repayBank));
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
		if(RepayType == 5) {
		  this.print(-4, 1, "入帳日期   戶號   額度         到期年月   保險迄日 還款類別           應扣金額        暫收抵繳金額           扣款金額   檢核結果");	
		} else {
		  this.print(-4, 1, "入帳日期   戶號   額度         繳息迄日   應繳日   還款類別           應扣金額        暫收抵繳金額           扣款金額   檢核結果");
		}
		this.print(-5, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void doReport(List<BankDeductDtl> lBankDeductDtl, TitaVo titaVo) throws LogicException {
		this.info("doReport ");
		this.info("lBankDeductDtl------->" + lBankDeductDtl);
		// RepayBank ,RepayType ,CustNo ,FacmNo ,BormNo
		lBankDeductDtl.sort((c1,c2) -> { // 字串ASCII 排序 CHROME有問題
			int result = 0;
			if (Integer.valueOf(c1.getRepayBank()) - Integer.valueOf(c2.getRepayBank()) !=0) {
				result = Integer.valueOf(c1.getRepayBank().compareTo(c2.getRepayBank()));
				
			} else if (c1.getRepayType() - c2.getRepayType() != 0) {
				result = c1.getRepayType() - c2.getRepayType();
				
			} else if (c1.getCustNo() - c2.getCustNo() != 0) {
				result = c1.getCustNo() - c2.getCustNo();
				
			} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
				result = c1.getFacmNo() - c2.getFacmNo();
				
			} else if (c1.getBormNo() - c2.getBormNo() != 0) {
				result = c1.getBormNo() - c2.getBormNo();
				
			} else if (c1.getPayIntDate() - c2.getPayIntDate() != 0) {
				result = c1.getPayIntDate() - c2.getPayIntDate();
				
			} else {
				result = 0;
			}

			return result;
		});

		this.lBankDeductDtl = lBankDeductDtl;
		this.info("this.lBankDeductDtl------->" + this.lBankDeductDtl);
		this.exec(titaVo);
	}

	public void exec(TitaVo titaVo1) throws LogicException {

		titaVo = titaVo1;
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 1;

		this.info("L4450Report exec");

//		設定工作表名稱
//		String iENTDY = titaVo.get("ENTDY");
//		txExcel.setSheet("108.04", iENTDY.substring(1, 4) + "." + iENTDY.substring(4, 6));

		if (lBankDeductDtl.size() > 0) {

			repayBank = lBankDeductDtl.get(0).getRepayBank();
			RepayType = lBankDeductDtl.get(0).getRepayType();
			this.info("repayBank : " + repayBank);
			this.info("bankCodeX : " + bankCodeX(repayBank));

			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4450", "銀行扣款合計報表", "", "A4", "L");

			DecimalFormat df1 = new DecimalFormat("#,##0");

//			by 扣款類別合計
			BigDecimal sumA1 = BigDecimal.ZERO;
			BigDecimal sumA2 = BigDecimal.ZERO;
			BigDecimal sumA3 = BigDecimal.ZERO;
//			by 扣款銀行合計
			BigDecimal sumB1 = BigDecimal.ZERO;
			BigDecimal sumB2 = BigDecimal.ZERO;
			BigDecimal sumB3 = BigDecimal.ZERO;
//			by 報表總計
			BigDecimal sumC1 = BigDecimal.ZERO;
			BigDecimal sumC2 = BigDecimal.ZERO;
			BigDecimal sumC3 = BigDecimal.ZERO;

			int timeAs = 0, timeBs = 0, total = 0, i = 0, pageCnt = 0;
			for (int j = 1; j <= lBankDeductDtl.size(); j++) {

				i = j - 1;

				String repayTypei = "";
				String repayTypej = "";
				repayTypei = "" + lBankDeductDtl.get(i).getRepayType();

//				1.每筆先印出明細
				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 1, "" + lBankDeductDtl.get(i).getEntryDate()); // 入帳日
				this.print(0, 10, FormatUtil.pad9("" + lBankDeductDtl.get(i).getCustNo(), 7));// 戶號
				this.print(0, 17, "-");
				this.print(0, 18, FormatUtil.pad9("" + lBankDeductDtl.get(i).getFacmNo(), 3));// 額度
//				this.print(0, 21, "-");
//				this.print(0, 22, FormatUtil.pad9("" + lBankDeductDtl.get(i).getBormNo(), 3));// 撥款
				
				if(RepayType == 5 ) {
				  
				  TempVo tTempVo = new TempVo();
				  tTempVo = tTempVo.getVo(lBankDeductDtl.get(i).getJsonFields());
				
				  String InsuNo = tTempVo.getParam("InsuNo");
				  this.info("tTempVo---->" + tTempVo);
				  if(!"".equals(InsuNo)) {	
					int InsuYearMonth = 0,InsuEndDate = 0;
					
					String tInsuNo[] = InsuNo.split(",");
					Boolean firstflg = false;
					for(int k = 0 ; k < tInsuNo.length; k++) {
						InsuRenew tInsuRenew = new InsuRenew();
						tInsuRenew = insuRenewService.prevInsuNoFirst(lBankDeductDtl.get(i).getCustNo(), lBankDeductDtl.get(i).getFacmNo(), tInsuNo[k], titaVo);
					    
						if(tInsuRenew != null) {
							
							if(InsuYearMonth == 0 && !firstflg) { // 第一次
								InsuYearMonth = tInsuRenew.getInsuYearMonth()-191100;
								InsuEndDate = tInsuRenew.getInsuEndDate();
								firstflg = true;
							} // if
							
							if( InsuYearMonth > tInsuRenew.getInsuYearMonth()) {
								InsuYearMonth = tInsuRenew.getInsuYearMonth()-191100;
								InsuEndDate = tInsuRenew.getInsuEndDate();
							} 
							
						}
					}
					
			
					if(InsuYearMonth == 0) {
						this.print(0, 30, "0");// 到期年月
						this.print(0, 40, "0");// 保險迄日
					} else {
					    this.print(0, 30, "" + InsuYearMonth );// 到期年月
					    this.print(0, 40, "" + InsuEndDate );// 保險迄日
					} // else 
					
				  } else {
				    this.print(0, 30, "0" );// 到期年月
				    this.print(0, 40, "0" );// 保險迄日
				  }
				    
				  
				} else {
				  this.print(0, 30, "" + lBankDeductDtl.get(i).getPrevIntDate());// 繳息迄日
				  this.print(0, 40, "" + lBankDeductDtl.get(i).getPayIntDate());// 應繳日
				}
				this.print(0, 49, repayTypeX(repayTypei));// 還款類別
				this.print(0, 75, df1.format(lBankDeductDtl.get(i).getUnpaidAmt()), "R");// 應扣金額
				this.print(0, 94, df1.format(lBankDeductDtl.get(i).getTempAmt()), "R");// 暫收抵繳金額
				this.print(0, 112, df1.format(lBankDeductDtl.get(i).getRepayAmt()), "R");// 扣款金額

				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(lBankDeductDtl.get(i).getJsonFields());

				String procNote = "";
//				欄位不夠長，順序為帳號、AML、扣帳金額為零
				if (tempVo.get("Auth") != null && tempVo.get("Auth").length() > 0) {
					procNote = procNote + "帳號授權檢核:" + authX(tempVo.get("Auth")) + "。";

				} else if (tempVo.get("Aml") != null && tempVo.get("Aml").length() > 0) {
					procNote = "Aml檢核:" + amlRspX(tempVo.get("Aml")) + "。";

				} else if (tempVo.get("Deduct") != null && tempVo.get("Deduct").length() > 0) {
					procNote = procNote + "扣款檢核：" + tempVo.get("Deduct") + "。";
				}

				this.print(0, 115, procNote);// AML檢核

//				by 扣款銀行合計
				sumC1 = sumC1.add(lBankDeductDtl.get(i).getUnpaidAmt());
				sumC2 = sumC2.add(lBankDeductDtl.get(i).getTempAmt());
				sumC3 = sumC3.add(lBankDeductDtl.get(i).getRepayAmt());

//				銀行筆數統計
				timeBs++;
//				by 扣款銀行合計
				sumB1 = sumB1.add(lBankDeductDtl.get(i).getUnpaidAmt());
				sumB2 = sumB2.add(lBankDeductDtl.get(i).getTempAmt());
				sumB3 = sumB3.add(lBankDeductDtl.get(i).getRepayAmt());
//				還款類別筆數統計
				timeAs++;
//				by 還款類別合計
				sumA1 = sumA1.add(lBankDeductDtl.get(i).getUnpaidAmt());
				sumA2 = sumA2.add(lBankDeductDtl.get(i).getTempAmt());
				sumA3 = sumA3.add(lBankDeductDtl.get(i).getRepayAmt());
				;

//				全部筆數統計
				total++;

//				每頁筆數相加
				pageCnt++;

//				2.再與下一筆比較，決定是否換行或換頁

				if (j != lBankDeductDtl.size()) {
					repayTypej = "" + lBankDeductDtl.get(j).getRepayType();
					RepayType = lBankDeductDtl.get(j).getRepayType();
//					扣款銀行不同則跳頁，並且累計歸零
					if (!lBankDeductDtl.get(i).getRepayBank().equals(lBankDeductDtl.get(j).getRepayBank())) {
						this.info("RepayBank Not Match...");

						this.print(1, 1, "         小　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeAs), "R");
						this.print(0, 75, df1.format(sumA1), "R");
						this.print(0, 94, df1.format(sumA2), "R");
						this.print(0, 112, df1.format(sumA3), "R");
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");

						this.print(1, 1, "         總　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeBs), "R");
						this.print(0, 75, df1.format(sumB1), "R");
						this.print(0, 94, df1.format(sumB2), "R");
						this.print(0, 112, df1.format(sumB3), "R");
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 70, "=====續下頁=====", "C");

						timeAs = 0;
						sumA1 = BigDecimal.ZERO;
						sumA2 = BigDecimal.ZERO;
						sumA3 = BigDecimal.ZERO;

						timeBs = 0;
						sumB1 = BigDecimal.ZERO;
						sumB2 = BigDecimal.ZERO;
						sumB3 = BigDecimal.ZERO;

						repayBank = lBankDeductDtl.get(j).getRepayBank();

						pageCnt = 0;
						this.newPage();
						continue;
					}
//					a.若同還款類別則累計金額
//					b.不同則跳頁，並且累計歸零
					if (!repayTypei.equals(repayTypej)) {
						this.info("RepayType Not Match...");

						this.print(1, 1, "         小　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeAs), "R");
						this.print(0, 75, df1.format(sumA1), "R");
						this.print(0, 94, df1.format(sumA2), "R");
						this.print(0, 112, df1.format(sumA3), "R");
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 70, "=====續下頁=====", "C");

						timeAs = 0;
						sumA1 = BigDecimal.ZERO;
						sumA2 = BigDecimal.ZERO;
						sumA3 = BigDecimal.ZERO;

						pageCnt = 0;
						this.newPage();
						continue;
					}

//					每頁第42筆 跳頁 
					if (pageCnt == 42) {
						this.print(1, 70, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
					if (total == lBankDeductDtl.size()) {
						this.print(1, 1, "         小　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeAs), "R");
						this.print(0, 75, df1.format(sumA1), "R");
						this.print(0, 94, df1.format(sumA2), "R");
						this.print(0, 112, df1.format(sumA3), "R");
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");

						this.print(1, 1, "         總　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", timeBs), "R");
						this.print(0, 75, df1.format(sumB1), "R");
						this.print(0, 94, df1.format(sumB2), "R");
						this.print(0, 112, df1.format(sumB3), "R");
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");

						this.print(1, 1, " 報　表　合　計：           筆                                                                                                  ");
						this.print(0, 27, String.format("%,d", total), "R");
						this.print(0, 75, df1.format(sumC1), "R");
						this.print(0, 94, df1.format(sumC2), "R");
						this.print(0, 112, df1.format(sumC3), "R");
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
					}
				}
			}
			long sno = this.close();
			this.toPdf(sno);
		}
	}

	private String repayTypeX(String repayType) {
		String result = "";

		String srt = FormatUtil.pad9(repayType.trim(), 2);

		CdCode cdCode = cdCodeService.getItemFirst(4, "RepayType", srt, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

	private String authX(String auth) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "AuthStatusCode", auth, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

	private String amlRspX(String amlRsp) {
		String result = "";

		String srt = FormatUtil.pad9(amlRsp.trim(), 1);

		CdCode cdCode = cdCodeService.getItemFirst(4, "AmlCheckItem", srt, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

	private String bankCodeX(String bankCode) {
		String result = "";
		Slice<CdBank> sCdBank = null;
		List<CdBank> lCdBank = new ArrayList<CdBank>();

		this.info("bankCodeX Start...");
		this.info("bankCode :" + bankCode);

		sCdBank = cdBankService.bankCodeLike(bankCode, this.index, this.limit, titaVo);

		lCdBank = sCdBank == null ? null : sCdBank.getContent();

		if (lCdBank != null && lCdBank.size() != 0) {
			result = lCdBank.get(0).getBankItem();
		}

		return result;
	}
}
