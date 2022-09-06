package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.springjpa.cm.L9706ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.ConvertUpMoney;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L9706Report")
@Scope("prototype")

public class L9706Report extends MakeReport {

	@Autowired
	L9706ServiceImpl l9706ServiceImpl;

	@Autowired
	public FacShareApplService facShareApplService;

	@Autowired
	public GuarantorService guarantorService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public ClFacService clFacService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	Parse parse;

	String iENTDAY = "";

	int standardFontSize = 13;

	boolean footerSayContinue = false;

	@Override
	public void printHeader() {

		this.info("L9706 exec" + this.titaVo.get("ENTDY"));

		this.setFontSize(standardFontSize - 3);
		// TODO: header 內容改成跟其他報表統一化
		// 找 LM012 之類的報表複製一下
		// 右邊 Align R, x 座標 90
		// 左邊 Align L, x 座標待測, 可能為 4 ~ 5
		// 中間 Align C 請用 this.getMidAxis()

		this.print(-1, 90, "機密等級：□極機密 ■機密 □密 □普通", "R");
		this.print(-2, 90, "文件持有人請嚴加控管本項文件", "R");

		this.setFontSize(standardFontSize + 3);
		this.print(-4, this.getMidXAxis(), "貸　款　餘　額　證　明　書", "C");
		this.setFontSize(standardFontSize);

		if (footerSayContinue) // 表示是同額度第二頁
		{
			this.print(-6, this.getMidXAxis(), "====　承　上　頁　====", "C");
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(43);
	}

	@Override
	public void printFooter() {

		if (footerSayContinue)
			print(-51, this.getMidXAxis(), "====　續　下　頁　====", "C");
	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L9706Report exec");

		int cnt = 0;

		iENTDAY = tranDate(titaVo.getCalDy());

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9706", "貸款餘額證明書", "普通", "A4", "P");
		this.setFontSize(standardFontSize);

		List<Map<String, String>> loanBTXList = null;

		try {
			loanBTXList = l9706ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("L9706ServiceImpl.LoanBorTx error = " + e.toString());
		}

		boolean hasData = loanBTXList != null && !loanBTXList.isEmpty();

		if (hasData) {
			this.info("L9706Report LoanBTXList =" + loanBTXList.toString());
			for (Map<String, String> tL9706Vo : loanBTXList) {
				if (cnt == 1) {
					this.newPage();
					cnt = 0;
				}
				report1(tL9706Vo);
				cnt += 1;
			}
		} else {
			this.print(1, 1, "無資料");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
		}
		this.close();

		// 測試用
		// this.toPdf(sno);
		return hasData;
	}

	private void report1(Map<String, String> tL9706Vo) throws LogicException {

		this.print(1, 0, "");

		footerSayContinue = true; // 沒看 MakeReport，但從測試過程來看，我猜第一頁的 header 在第一次 print 時發動，所以如果這行放在 print
									// 上面會讓第一頁也有承上頁

		// 每行起始 3
		// 每行有效 78

		// 每行長度 76

		// 輸出第一段：基本資料

		// 年月日 期 的部分比較麻煩
		// 先輸出好
		int loanTermYy = parse.stringToInteger(tL9706Vo.get("LoanTermYy"));
		int loanTermMm = parse.stringToInteger(tL9706Vo.get("LoanTermMm"));
		int loanTermDd = parse.stringToInteger(tL9706Vo.get("LoanTermDd"));

		String loanTermString = (loanTermYy > 0 ? loanTermYy + " 年 " : "") + (loanTermMm > 0 ? loanTermMm + " 月 " : "") + (loanTermDd > 0 ? loanTermDd + " 日" : "");

		loanTermString = loanTermString.trim();

		// 這邊因為新需求，要把每行做成固定長度，只是可以往下延伸，像 word 排版那樣
		// 所以用的是先把字串組好，然後依照內容長度拆成多個固定長度的行，達到類似效果
		String cuscCdX = "";
		if ("2".equals(tL9706Vo.get("CuscCd"))) {
			cuscCdX = "統一編號";
		} else {
			cuscCdX = "身分證字號";
		}
		String basicInfo = String.format("　　查　%s　君（%s%s），於 %s 向本公司辦理　%s期　購置貸款，借款金額　%s整　，戶號 %s － %s 截至民國 %s止　　貸款餘額為 %s整。", tL9706Vo.get("CustName") // 戶名
				, cuscCdX, tL9706Vo.get("CustId") // 身分證字號
				, this.showRocDate(tL9706Vo.get("FirstDrawdownDate"), 0) // 首撥日
				, loanTermString // 辦理 %s期
				, ConvertUpMoney.toChinese(tL9706Vo.get("LineAmt")) // 借款金額
				, FormatUtil.pad9(tL9706Vo.get("CustNo"), 7) // 戶號
				, FormatUtil.pad9(tL9706Vo.get("FacmNo"), 3) // 額度
				, this.showRocDate(this.titaVo.getCalDy(), 0) // 截至民國 %s 日
				, ConvertUpMoney.toChinese(tL9706Vo.get("LoanBal"))); // 貸款餘額

		// 切成當中所有 strings 長度 <= 74 的 string[]

		List<String> split = splitChinese(basicInfo, 74);

		for (String s : split) {
			this.print(1, 3, s);
		}

		// 輸出第二段：共同借款人
		// 共同借款人資料檔
		int applNo = parse.stringToInteger(tL9706Vo.get("ApplNo"));
		List<CustMain> shareList = new ArrayList<CustMain>();
		FacShareAppl tfacShareAppl = facShareApplService.findById(applNo, titaVo);
		if (tfacShareAppl != null) {
			Slice<FacShareAppl> slFacShareApp = facShareApplService.findMainApplNo(tfacShareAppl.getMainApplNo(), 0, Integer.MAX_VALUE, titaVo);
			for (FacShareAppl t : slFacShareApp.getContent()) {
				CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (tCustMain != null && !tCustMain.getCustId().equals(tL9706Vo.get("CustId"))) {
					shareList.add(tCustMain);
				}
			}
		}
		// 保證人檔的共同借款人，重複剃除
		Slice<Guarantor> slGuarantor = guarantorService.approveNoEq(applNo, 0, Integer.MAX_VALUE, titaVo);
		if (slGuarantor != null) {
			for (Guarantor t : slGuarantor.getContent()) {
				if ("06".equals(t.getGuaTypeCode())) {
					boolean isNew = true;
					for (CustMain c : shareList) {
						if (c.getCustUKey().equals(t.getGuaUKey()) || c.getCustId().equals(tL9706Vo.get("CustId"))) {
							isNew = false;
						}
					}
					if (isNew) {
						CustMain tCustMain = custMainService.findById(t.getGuaUKey(), titaVo);
						if (tCustMain != null) {
							shareList.add(tCustMain);
						}

					}
				}
			}
		}
		if (shareList.size() > 0) {
			this.print(1, 0, "");
			this.print(1, 3, "共同借款人：");
			for (CustMain tCustMain : shareList) {
				cuscCdX = "身分證字號";
				if ("2".equals(tCustMain.getCuscCd())) {
					cuscCdX = "統一編號";
				} else {
					cuscCdX = "身分證字號";
				}
				this.print(0, 13, tCustMain.getCustName() + " (" + cuscCdX + tCustMain.getCustId() + ")");
				this.print(1, 0, "");
			}
		}
		int facmNo = parse.stringToInteger(tL9706Vo.get("FacmNo"));
		// 輸出第三段：所有地址
		this.info("applNo=" + applNo);
		this.info("facmNo=" + facmNo);
		Slice<ClFac> slClFac = clFacService.approveNoEq(applNo, 0, Integer.MAX_VALUE, titaVo);

		List<ClBuilding> addressList = new ArrayList<ClBuilding>();
		if (slClFac != null) {

			for (ClFac f : slClFac.getContent()) {

				ClBuilding tClBuilding = clBuildingService.findById(new ClBuildingId(f.getClCode1(), f.getClCode2(), f.getClNo()), titaVo);
				if (tClBuilding != null) {
					addressList.add(tClBuilding);
				}

			}

		}
		if (addressList.size() > 0) {
			this.print(1, 0, "");
			this.print(1, 3, "貸款抵押標的物地址：");

			// 處理重複地址問題
			List<String> result = new ArrayList<String>();
			for (ClBuilding str : addressList) {
				String tempBdLocation = str.getBdLocation().toString();
				if (!result.contains(tempBdLocation)) {
					result.add(tempBdLocation);
				}
			}

			for (String bdLocation : result) {
				this.print(1, 8, bdLocation); // 每個地址的輸出位置：8
			}
		}

		// 輸出第四段：如果為政府優惠房屋貸款時，要多輸出
		String loanKind = "";

		switch (tL9706Vo.get("ProdNo").trim().toUpperCase()) {
		case "IA":
			loanKind = "青年優專房屋貸款暨信用保證專案";
			break;
		case "IB":
			loanKind = "四千億元優惠購物專案貸款";
			break;
		case "IC":
			loanKind = "續辦二千億元優惠購屋專案貸款";
			break;
		case "ID":
		case "IE":
			loanKind = "續辦四千八百億元優惠購屋專案貸款";
			break;
		case "IF":
		case "IG":
			loanKind = "續辦六千億元優惠購屋專案貸款";
			break;
		case "IH":
		case "II":
			loanKind = "增撥新台幣四千億元優惠購屋專案貸款";
			break;
		default:
			break;
		}

		if (!loanKind.isEmpty()) {
			this.print(1, 0, "");
			this.print(1, 3, "本案為政府優惠房屋貸款 ： " + loanKind);
		}

		this.print(1, 0, "");

		// footer

		this.print(1, 1, "");
		this.print(1, 18, "此  致");
		this.print(1, 1, "");
		this.print(1, 9, "台照");
		this.print(1, 1, "");
		this.print(1, 76, "新光人壽保險股份有限公司敬啟", "R");
		this.print(1, 1, "");
		this.print(1, 4, "中 　 　　華 　 　　民 　 　　國  " + iENTDAY);
//		this.print(0, 66, iENTDAY, "R");

		footerSayContinue = false;
	}

	private String tranDate(String date) {
		this.info("tranDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}

		String rocdatex = String.valueOf(rocdate);
		this.info("tranDate2 = " + rocdatex);
		this.info("tranDate2 LEN = " + rocdatex.length());

		String rocdatexx = fullForm(rocdatex);
		rocdatex = "";
		int i = 0;
		if (rocdatexx.length() == 6) {
			rocdatex = rocdatexx.substring(0, 2) + "   年   ";
			i = 2;
		} else {
			rocdatex = rocdatexx.substring(0, 3) + "   年   ";
			i = 3;
		}
		if (rocdatexx.substring(i, i + 1).equals("０")) {
			rocdatex = rocdatex + " " + rocdatexx.substring(i + 1, i + 2) + "    月   ";
		} else {
			rocdatex = rocdatex + rocdatexx.substring(i, i + 2) + "   月   ";
		}
		i += 2;

		if (rocdatexx.substring(i, i + 1).equals("０")) {
			rocdatex = rocdatex + " " + rocdatexx.substring(i + 1, i + 2) + "    日";
		} else {
			rocdatex = rocdatex + rocdatexx.substring(i, i + 2) + "   日";
		}
		return rocdatex;
	}

	// 數字半型轉全型
	private String fullForm(String idata) {
		int tranTemp = 0;
		char tmp;
		String odata = "";

		for (int i = 0; i < idata.length(); i++) {

//			this.info("idata i = " + i);
//			this.info("idata X = " + idata.substring(i, i + 1));
			tmp = idata.charAt(i);

			tranTemp = (int) tmp;

			tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差

//			this.info("tranDate XX= " + (char) tranTemp);
			odata += (char) tranTemp;
		}
		return odata;
	}

	// copied from StringCut
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	private int getThisNumberLength(String data, int leftIndex) {

		int len = 0;

		for (int i = leftIndex; i < data.length(); i++) {
			if (Character.isDigit(data.charAt(i)))
				len++;
			else
				break;
		}

		return len;
	}

	private List<String> splitChinese(String data, int lineLength) {
		List<String> resultList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int currentLength = 0;

		for (int i = 0; i < data.length(); i++) {
			char thisChar = data.charAt(i);
			int thisCharLength = isChinese(thisChar) ? 2 : 1;

			// 如果這個字元是數字，檢查從這一位數開始後面還有沒有數字
			// 如果有，盡量加在一起
			// 有一些 edge case 沒有處理：

			// 1. 如果數字長度大於 lineLength，會多很多空白的行（TODO: 檢查 thisNumberLength > lineLength
			// 時擲錯，或者做其他的特殊處理）
			// 2. 假如數字是 5 位數，同樣的檢查會檢查五次，5,4,3,2,1，會影響效能，有時間可作改善

			int thisNumberLength = 0;

			if (Character.isDigit(thisChar))
				thisNumberLength = getThisNumberLength(data, i);

			if ((currentLength + Math.max(thisCharLength, thisNumberLength) > lineLength)) {
				resultList.add(sb.toString());
				sb.setLength(0);
				currentLength = 0;
			}

			currentLength += thisCharLength;
			sb.append(thisChar);
		}

		// 最後剩下的所有文字
		if (sb.length() > 0)
			resultList.add(sb.toString());

		return resultList;
	}

}
