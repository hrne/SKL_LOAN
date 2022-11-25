package com.st1.itx.trade.L8;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.DailyLoanBal;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.DailyLoanBalService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 產製公務人員報送資料 執行時機：call by L8701
 * 
 * @author Lai
 * @version 1.0.0
 */

@Service("L8701Batch")
@Scope("prototype")
public class L8701Batch extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	@Autowired
	public CustMainService custMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public DailyLoanBalService dailyLoanBalService;
	@Autowired
	public AcDetailService acDetailService;
	@Autowired
	public WebClient webClient;

	@Autowired
	public MakeExcel makeExcel;
	@Autowired
	public MakeFile makeFile;
	@Value("${iTXInFolder}")
	private String inFolder = "";

	private CustMain tCustMain = new CustMain();
	private FacMain tFacMain = new FacMain();
	private DailyLoanBal tDailyLoanBal = new DailyLoanBal();
	private String iDataDatef;
	private int iDataDate;
	private int iTbsdyf;
	private int iTbsdy;
	private int iBday;
	private String iCustId;
	private String acctCode;
	private BigDecimal loanBal;
	private int bCount;
	private String sign = "+";
	private String memo;
	private ArrayList<String> Data = new ArrayList<String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS996 ......");
		iTbsdy = titaVo.getEntDyI();
		iTbsdyf = iTbsdy + 19110000;
		String iCode = "";
		boolean Zero = true;
		
		if ("".equals(titaVo.getParam("FILENA").trim())) {
			throw new LogicException(titaVo, "E0015", "檔案不存在,請查驗路徑");
		}
		String FilePath = inFolder + dDateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();

		// 讀取CSV資料
		makeExcel.openCsv(FilePath, ",");
		

		int iExcelline = 1;

		for (Map<String, Object> map : makeExcel.getListMap()) {
			this.info("Map Data = " + map);

			if (iExcelline == 1) {// 第一行 資料交換序號
				Data.add((String) map.get("f1"));
				iCode = (String) map.get("f1");
			}

			if (iExcelline == 2) {// 第二行 空白
				Data.add("");
			}

			if (("@").equals(map.get("f1"))) {// 結束記號
				break;

			}
			if (iExcelline >= 4 && map.get("f2") != null) {
				iCustId = (String) map.get("f2");// 統編 A123456789
				iDataDatef = (String) map.get("f5");// 資料基準日 => 上傳csv資料E欄<索取日迄日>
				iDataDate = Integer.parseInt(iDataDatef) - 19110000;
				iBday = Integer.parseInt((String) map.get("f3"));//=> 上傳csv資料C欄<生日>
				memo = "";
				this.info("iDataDatef==" + iDataDatef);
				getDataRoutine(titaVo);
			}

			iExcelline++;

		}
		// 查詢機管編碼2+公司編碼3+資料產生日8+類別代碼固定B1+序號從01編碼
	
		if(iCode.length()>=10) {
			if(("01").equals(iCode.substring(8, 10)) || ("02").equals(iCode.substring(8, 10))) {
				iCode = iCode.substring(8, 10);
			} else {
				iCode = "01";
			}
			
		} else {
			iCode = "01";
		}
		String filename = iCode + "458" + iTbsdyf + "B1" + "01" + ".txt"; // 輸出檔TXT名
				
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "公務人員報送", filename, 2);
		
		for (String iData : Data) {
			this.info("iData==" + iData);

			if (Zero) {
				String aCount = makeFile.fillStringL(String.valueOf(bCount), 9, '0');
				makeFile.put(iData + aCount);// 資料交換序號+筆數
				Zero = false;
			} else {
				makeFile.put(iData);
			}

		}

		makeFile.put("@");// 結束記號

		long sno = makeFile.close();
		makeFile.toFile(sno);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo()+"L8701", "L8701公務人員報送資料完成", titaVo);

		this.info(" BS996 END");
		// end
		return null;
	}

	// Get Data
	private void getDataRoutine(TitaVo titaVo) throws LogicException {
		this.info("getDataRoutine ...");
		tCustMain = custMainService.custIdFirst(iCustId, titaVo);
		if (tCustMain == null || tCustMain.getCustNo() == 0) {
			return;
		}
		int icustno = tCustMain.getCustNo();
		int iday = tCustMain.getBirthday() + 19110000;
//		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findAll(0, Integer.MAX_VALUE, titaVo);
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(icustno, 0, 999, 0, 999, 0,
				Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain == null) {
			return;
		}
		// 0: 正常戶 1:展期 2: 催收戶 3: 結案戶 4: 逾期戶 5: 催收結案戶 6: 呆帳戶 7: 部分轉呆戶 8: 債權轉讓戶 9: 呆帳結案戶
		List<Integer> statuslist = Arrays.asList(new Integer[] { 1, 3, 5, 6, 8, 9 });
		for (LoanBorMain ln : slLoanBorMain.getContent()) {
			if (ln.getBormNo() > 900) {
				continue;
			}
			// 基準日前結案
			if (statuslist.contains(ln.getStatus()) && ln.getAcDate() <= iDataDate) {
				continue;
			}

			// 基準日後撥款
			if (ln.getDrawdownDate() > iDataDate) {
				continue;
			}

			if(ln.getCustNo()!=tCustMain.getCustNo()) {
				continue;
			}
			
			acctCode = "";
			loanBal = BigDecimal.ZERO;

			// 找基準日前的每日餘額檔，找不到則依會計明細檔回朔餘額(資料轉換未轉每日餘額檔)
			tDailyLoanBal = dailyLoanBalService.dataDateFirst(ln.getCustNo(), ln.getFacmNo(), ln.getBormNo(), Integer.parseInt(iDataDatef), titaVo);
			if (tDailyLoanBal == null) {
				acDetailRoutine(ln, titaVo);
			} else {
				acctCode = tDailyLoanBal.getAcctCode();
				loanBal = tDailyLoanBal.getLoanBalance();
			}
			if (loanBal.compareTo(BigDecimal.ZERO) > 0) {
				if(iBday != iday) {
					memo = "生日不符";
				}				
				outputRoutine(ln, titaVo);
			}
		}

	}

	// 依AcDetail回朔餘額
	private void acDetailRoutine(LoanBorMain ln, TitaVo titaVo) throws LogicException {
		this.info("acDetailRoutine ..." + ln.getCustNo() + "-" + ln.getFacmNo() + "-" + ln.getBormNo());
		BigDecimal wkLnBal = BigDecimal.ZERO;
		BigDecimal wkOvBal = BigDecimal.ZERO;
		int endDatef = 0;
		// 最近的一筆餘額
		tDailyLoanBal = dailyLoanBalService.dataDateFirst(ln.getCustNo(), ln.getFacmNo(), ln.getBormNo(), iTbsdyf, titaVo);
		if (tDailyLoanBal == null) {
			endDatef = iTbsdyf - 1; // 本日不算
		}

		if (tDailyLoanBal != null) {
			if ("990".equals(tDailyLoanBal.getAcctCode())) {
				wkOvBal = tDailyLoanBal.getLoanBalance();
			} else {
				wkLnBal = tDailyLoanBal.getLoanBalance();
				acctCode = tDailyLoanBal.getAcctCode();
			}
			endDatef = tDailyLoanBal.getDataDate() + 19110000;
			this.info("DailyLoanBal=" + tDailyLoanBal.getAcctCode() + ", OvBal=" + wkOvBal + ", LnBal=" + wkLnBal);
		}

		// 資料基準日 < AcDate <= 最近的一筆餘額檔日期(沒有則小於本日)
		Slice<AcDetail> slAcDetail = acDetailService.bormNoAcDateRange(ln.getCustNo(), ln.getFacmNo(), ln.getBormNo(), 1, Integer.parseInt(iDataDatef) + 1, endDatef, 0, Integer.MAX_VALUE, titaVo);
		if (slAcDetail == null) {
			return;
		}
		for (AcDetail ac : slAcDetail.getContent()) {
			if ("990".equals(ac.getAcctCode())) {
				if ("D".equals(ac.getDbCr())) {
					wkOvBal = wkOvBal.subtract(ac.getTxAmt());
				} else {
					wkOvBal = wkOvBal.add(ac.getTxAmt());
				}
			} else {
				if ("D".equals(ac.getDbCr())) {
					wkLnBal = wkLnBal.subtract(ac.getTxAmt());
				} else {
					wkLnBal = wkLnBal.add(ac.getTxAmt());
				}
				acctCode = tDailyLoanBal.getAcctCode();
			}
			this.info("ac=" + ac.getAcctCode() + "-" + ac.getDbCr() + "-" + ac.getTxAmt() + ", OvBal=" + wkOvBal + ", LnBal=" + wkLnBal);
		}
		if (wkOvBal.compareTo(BigDecimal.ZERO) > 0) {
			acctCode = "990";
			loanBal = wkOvBal;
		} else {
			loanBal = wkLnBal;
		}
	}

	private void outputRoutine(LoanBorMain ln, TitaVo titaVo) throws LogicException {
		this.info("outputRoutine ...");
		String strField = "";
		bCount++; // 計算筆數
		tFacMain = facMainService.findById(new FacMainId(ln.getCustNo(), ln.getFacmNo()), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E3011", "額度主檔  " + ln.getCustNo() + "- " + ln.getCustNo()); // 鎖定資料時，發生錯誤
		}

		String oAcctItem;
		if ("990".equals(acctCode))
			oAcctItem = "催收";
		else if ("310".equals(acctCode))
			oAcctItem = "短擔(放)";
		else if ("320".equals(acctCode))
			oAcctItem = "中擔(放)";
		else
			oAcctItem = "長擔(放)";

		String oUsageItem;
		// 1:購置不動產 ==> 2: 購置不動產 3: 營業用資產 4: 固定資產
		// 2:購置動產 ==> 6: 購置動產
		// 3:企業投資 ==> 5: 企業投資
		// 4:週轉金 ==> 1: 週轉金
		if ("06".equals(ln.getUsageCode()))
			oUsageItem = "購置動產";
		else if ("05".equals(ln.getUsageCode()))
			oUsageItem = "企業投資";
		else if ("01".equals(ln.getUsageCode()))
			oUsageItem = "週轉金";
		else
			oUsageItem = "購置不動產";
		// 正負號
		if(loanBal.compareTo(BigDecimal.ZERO)<0) {
			sign = "-";
		} 
		
		strField += makeFile.fillStringR(iCustId, 10, ' ');// 債務人統編 10碼
		strField += makeFile.fillStringR(tCustMain.getCustName(), 60);// 債務人名稱 60
		strField += "4580000";// 機構代碼 7碼
		strField += "B";// 放款種類 1碼
		// 授信帳號 50
		strField += makeFile.fillStringR(parse.IntegerToString(ln.getCustNo(), 7) + parse.IntegerToString(ln.getFacmNo(), 3) + parse.IntegerToString(ln.getBormNo(), 3), 50);
		strField += makeFile.fillStringR(oAcctItem, 20);// 放款科目20碼
		strField += makeFile.fillStringR(iDataDatef, 8);// 資料基準日8碼
		int FirstDrawdownDate = 0; 
		if(tFacMain.getFirstDrawdownDate()!=0) {
			FirstDrawdownDate = tFacMain.getFirstDrawdownDate() + 19110000;
		}
		strField += parse.IntegerToString(FirstDrawdownDate, 8);// 初貸日期 8碼
		
		int DrawdownDate = 0; 
		if(ln.getDrawdownDate()!=0) {
			DrawdownDate = ln.getDrawdownDate() + 19110000;
		}
		strField += parse.IntegerToString(DrawdownDate, 8);// 契約起始日期 8碼
		
		int MaturityDate = 0; 
		if(ln.getMaturityDate()!=0) {
			MaturityDate = ln.getMaturityDate() + 19110000;
		}
		strField += parse.IntegerToString(MaturityDate, 8);// 契約終止日期 8碼
		strField += "TWD"; // 幣別
		strField += "0000000001000000"; // 匯率
		strField += sign;// 基準日放款餘額 1碼 ( + OR - )
		strField += makeFile.fillStringL(String.valueOf(loanBal), 13, '0') + "00";// 基準日放款餘額 15碼
		strField += makeFile.fillStringR(oUsageItem, 60, ' ');// 借款用途 60碼
		strField += makeFile.fillStringR(memo, 120, ' ');// 備註 120碼
		Data.add(strField);

		this.info("債務人統編 :" + iCustId);
		this.info("債務人名稱 :" + tCustMain.getCustName());
		this.info("金融機構代碼 :" + "4580000");
		this.info("放款種類 :" + "B");
		this.info("授信帳號=" + parse.IntegerToString(ln.getCustNo(), 7) + parse.IntegerToString(ln.getFacmNo(), 3) + parse.IntegerToString(ln.getBormNo(), 3));
		this.info("放款科目 :" + oAcctItem);
		this.info("資料基準日 :" + iDataDatef);
		this.info("初貸日期 :" + (tFacMain.getFirstDrawdownDate() + 19110000));
		this.info("契約起始日期 :" + (ln.getDrawdownDate() + 19110000));
		this.info("契約終止日期 :" + (ln.getMaturityDate() + 19110000));
		this.info("基準日放款餘 :" + loanBal);
		this.info("借款用途 :" + oUsageItem);
		this.info("備註 :" + "");

	}
}