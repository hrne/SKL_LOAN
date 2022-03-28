package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L4455ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4455Report2")
@Scope("prototype")

public class L4455Report2 extends MakeReport {


	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	CustNoticeCom custNoticeCom;
	
	@Autowired
	public L4455ServiceImpl sL4455ServiceImpl;
	
	@Autowired
	public CustMainService custMainService;
	
	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;
	
	@Autowired
	public Parse parse;

	@Autowired
	public TxBuffer txBuffer;

	private int entrydate = 0;
	private int pageIndex = 38;
	
	
	private String year = "";
	private String month = "";
	private String date = "";
	private String repaybank = "";
	private String bank = "";
	private String acctcodex = "";
	private String relationcodex = "";
	
	private List<CdCode> lCdCode = null;
	private List<CdCode> lCdCode2 = null;
	private List<CdCode> lCdCode3 = null;
	
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		printHeaderP();
		
		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public void printHeaderP() {
		this.setFont(1,9);
		this.print(-1, 185, "機密等級：密");
		this.print(-2, 1, "程式ID：" + "L4455Report2");
		this.print(-2, 95, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-2, 203, "日　期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 1, "報　表：" + "L4455Report2");
		
		for(CdCode tCdCode :lCdCode) {
			if(repaybank.equals(tCdCode.getCode())) {
				bank = tCdCode.getItem();
			}
		}
		this.print(-3, 95, "ACH 扣款失敗報表 -- " + bank, "C");
		this.print(-3, 203, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 185, "頁　數：");
		this.print(-4, 200, ""+this.getNowPage(),"R");
		this.print(-5, 95, "入帳日期 ：    年    月    日", "C");
		
		if(String.valueOf(entrydate).length() == 7) {
			year = String.valueOf(entrydate).substring(0, 3);
			month = String.valueOf(entrydate).substring(3, 5);
			date = String.valueOf(entrydate).substring(5, 7);
		} else if(String.valueOf(entrydate).length() == 6){
			year = String.valueOf(entrydate).substring(0, 2);
			month = String.valueOf(entrydate).substring(2, 4);
			date = String.valueOf(entrydate).substring(4, 6);
		} 
		
		this.print(-5, 91, year);
		this.print(-5, 99, month);
		this.print(-5, 106, date);
		
		this.print(-5, 185, "單　位：元");
		this.print(-7, 1, "業務科目        扣款帳號         戶號         戶名                     計息起迄日        應扣款金額合計       連絡電話          聯絡人              原因              備註");
		this.print(-8, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L4455Report2 exec");
		entrydate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		
		List<Map<String, String>> L4455List = new ArrayList<Map<String, String>>();
		
		try {
			L4455List = sL4455ServiceImpl.findAll2(titaVo);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		
		List<Map<String, String>> L4455ListSum = new ArrayList<Map<String, String>>();
		
		try {
			L4455ListSum = sL4455ServiceImpl.findSum(titaVo);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4455", "銀行扣款失敗報表", "", "A4", "L");

		Slice<CdCode> slCdCode = sCdCodeDefService.defItemEq("BankDeductCd", "%", this.index, this.limit, titaVo);
		
		lCdCode = slCdCode == null ? null : slCdCode.getContent();
		
		Slice<CdCode> slCdCode2 = sCdCodeDefService.defItemEq("AcctCode", "%", this.index, this.limit, titaVo);
		
		lCdCode2 = slCdCode2 == null ? null : slCdCode2.getContent();
		
		Slice<CdCode> slCdCode3 = sCdCodeDefService.defItemEq("RelationCode", "%", this.index, this.limit, titaVo);
		
		lCdCode3 = slCdCode3 == null ? null : slCdCode3.getContent();
		
		
		if (L4455List.size() > 0 && !L4455List.isEmpty()) {
			int i = 0, pageCnt = 0;
			int pagetime = 0 ; 
			BigDecimal Amt = new BigDecimal("0");
			repaybank = L4455List.get(0).get("RepayBank");
			
			for (int j = 1; j <= L4455List.size(); j++) {
				i = j - 1;
				
//				每頁筆數相加
				pageCnt++;

				DecimalFormat df1 = new DecimalFormat("#,##0");

//				1.每筆先印出明細
				this.print(1, 1,"                                                                                                                                                                               ");
				
				for(CdCode tCdCode :lCdCode2) {
					if(L4455List.get(i).get("AcctCode").equals(tCdCode.getCode())) {
						acctcodex = tCdCode.getItem();
					}
				}
				
				this.print(0, 1, acctcodex);// 業務科目
				this.print(0, 19, L4455List.get(i).get("RepayAcctNo"));// 扣款帳號
				this.print(0, 38, FormatUtil.pad9(L4455List.get(i).get("CustNo"), 7));// 戶號
				this.print(0, 48, limitLength(L4455List.get(i).get("CustName"), 20));// 戶名
				
				if(parse.stringToInteger(L4455List.get(i).get("IntStartDate")) != 0) {
					String IntStartDate = String.valueOf(parse.stringToInteger(L4455List.get(i).get("IntStartDate")) -19110000);
					
					if(String.valueOf(IntStartDate).length() == 7) {
						this.print(0, 70, IntStartDate.substring(0, 3) + "/" + IntStartDate.substring(3, 5) + "/" + IntStartDate.substring(5, 7));// 計息起訖日					
					} else {
						this.print(0, 70, IntStartDate.substring(0, 2) + "/" + IntStartDate.substring(2, 4) + "/" + IntStartDate.substring(4, 6));// 計息起訖日					
					}
					
				} 
				
				this.print(0, 80, "-");// 計息起訖日
				
				if(parse.stringToInteger(L4455List.get(i).get("IntEndDate")) != 0) {
					
					String IntEndDate = String.valueOf(parse.stringToInteger(L4455List.get(i).get("IntEndDate")) -19110000);
					
					if(String.valueOf(IntEndDate).length() == 7) {
						this.print(0, 83, IntEndDate.substring(0, 3) + "/" + IntEndDate.substring(3, 5) + "/" + IntEndDate.substring(5, 7));// 計息起訖日					
					} else {
						this.print(0, 83, IntEndDate.substring(0, 2) + "/" + IntEndDate.substring(2, 4) + "/" + IntEndDate.substring(4, 6));// 計息起訖日					
					}
					
				}
				
				if(parse.stringToBigDecimal(L4455List.get(i).get("RepayAmt")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 117, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("RepayAmt"))), "R");// 應扣款金額合計
				}
				
				Amt = Amt.add(parse.stringToBigDecimal(L4455List.get(i).get("RepayAmt")));
				
				if(!"".equals(L4455List.get(i).get("CustTel"))) { // 0不顯示
					this.print(0, 125, L4455List.get(i).get("CustTel"));// 連絡電話
				}
				
				if(!"".equals(L4455List.get(i).get("LiaisonName"))) { // 0不顯示
					this.print(0, 145, limitLength(L4455List.get(i).get("LiaisonName"),20));// 連絡人

				}
				
				this.print(0, 168, L4455List.get(i).get("ReturnCode"));// 原因
				this.print(0, 172, L4455List.get(i).get("Remark"));// 備註
				
				pagetime++;
				
				if(!"00".equals(L4455List.get(i).get("RelationCode"))) {
					this.print(1, 1,"                                                                                                                                                                               ");
					
					for(CdCode tCdCode :lCdCode3) {
						if(L4455List.get(i).get("RelationCode").equals(tCdCode.getCode())) {
							relationcodex = tCdCode.getItem();
						}
					}
					
					this.print(0, 19, L4455List.get(i).get("RelationCode") + "  " + relationcodex); // 與借款人關係
					this.print(0, 52, L4455List.get(i).get("RelCustId")); // 第三人身分證字號
					this.print(0, 70, L4455List.get(i).get("RelCustName")); // 第三人身分證字號
					pageCnt++;
					relationcodex = ""; // 清空關係
				}
				
				
				if (j != L4455List.size()) {
//					批次號碼/扣款銀行 不同則跳頁，並且累計歸零
					repaybank = L4455List.get(j).get("RepayBank");
					if (!L4455List.get(i).get("RepayBank").equals(repaybank)) {
						this.info(" Not Match...");
						
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 1,"                                                                                                                                                                               ");
						this.print(0, 70, "小    計             件 ");
						
						this.print(0, 90, String.valueOf(pagetime),"R");
						
						if(Amt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
							this.print(0, 117, df1.format(Amt), "R");// 應扣款金額合計
						}
						
						Amt = new BigDecimal("0");
						
						pageCnt = pageCnt + 2;
						this.print(pageIndex - pageCnt - 2, 95, "=====續下頁=====", "C");
						pagetime = 0;
						pageCnt = 0;
						this.newPage();
						continue;
					}
					
					
//					每頁第38筆 跳頁 
					if (pageCnt >= 34) {
						this.print(pageIndex - pageCnt - 2, 95, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
//					扣除總計合計的行數 +1 
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1,"                                                                                                                                                                               ");
					this.print(0, 70, "小    計             件 ");
					
					this.print(0, 90, String.valueOf(pagetime),"R");
					
					if(Amt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
						this.print(0, 117, df1.format(Amt), "R");// 應扣款金額合計
					}
					
					Amt = new BigDecimal("0");
					
					pageCnt = pageCnt + 2;
					
					// 每頁第38筆 跳頁 
					if (pageCnt >= 34) {
						this.print(pageIndex - pageCnt - 2, 95, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
						
					}
					
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1,"                                                                                                                                                                               ");
					this.print(0, 70, "合    計             件                                       總戶數 ： 　　　 　　失敗戶數 ：");
					
					this.print(0, 90, df1.format(parse.stringToBigDecimal(L4455ListSum.get(0).get("F0"))),"R");
					this.print(0, 117, df1.format(parse.stringToBigDecimal(L4455ListSum.get(0).get("F1"))),"R");
					this.print(0, 160, df1.format(parse.stringToBigDecimal(L4455ListSum.get(0).get("F2"))),"R");
					this.print(0, 172, df1.format(parse.stringToBigDecimal(L4455ListSum.get(0).get("F3"))),"R");
					pageCnt = pageCnt + 2 ;
					this.print(pageIndex - pageCnt - 2, 95, "=====報表結束=====", "C");
					this.print(2, 95, "協理：　　　　　　　　　　　　　　　襄理：　　　　　　　　　　　　　　　製表人：","C");
				}
				
			
			} // for
			
		} else {
			this.print(1, 20, "*******    查無資料   ******");
		}

		long sno = this.close();

		this.toPdf(sno);
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