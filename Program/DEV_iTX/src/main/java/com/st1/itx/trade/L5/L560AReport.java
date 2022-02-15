package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.WarningLetterForm;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.db.service.springjpa.cm.L560AServiceImpl;

@Service("L560AReport")
@Scope("prototype")
/**
 * 列印催收通知單
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L560AReport extends MakeReport {
	
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public FacMainService iFacMainService;
	@Autowired
	public CdAreaService iCdAreaService;
	@Autowired
	public CdCityService iCdCityService;
	@Autowired
	public CollListService iCollListService;
	@Autowired
	public ClFacService iClFacService;
	@Autowired
	public GuarantorService iGuarantorService;
	@Autowired
	public CdEmpService iCdEmpService;
	
	@Autowired
	WarningLetterForm iWarningLetterForm;
	@Autowired
	BaTxCom baTxCom;

	@Autowired
	public MakeFile makeFile;
	
	@Autowired
	public Parse parse;
	
	@Autowired
	L560AServiceImpl l560AServiceImpl;

	@Override
	public void printTitle() {
//		this.print(-8, 1, "┌────────────────────┬──────┬─────────┐");
//		this.print(-9, 1, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　│　貸　款　金　額　│");
	}

	@Override
	public void printHeader() {
	}
	
	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		String adjFlag = titaVo.getBtnIndex(); // 0-存證信函;1-延遲繳款通知函
		String iCustNo = titaVo.getParam("OOCustNo");
		int rCustNo = Integer.valueOf(iCustNo);
		String iFacmNo = titaVo.getParam("OOFacmNo");
		int rFacmNo = Integer.valueOf(iFacmNo);
		String iCalDy = titaVo.getCalDy(); // 日曆日
		String iCalyy = iCalDy.substring(0, 3);
		String iCalMm = iCalDy.substring(3, 5);
		String iCalDd = iCalDy.substring(5, 7);
		String iEntDy = titaVo.getEntDy(); // 會計日
		String iEntyy = iEntDy.substring(1, 4);
		String iEntMm = iEntDy.substring(4, 6);
		String iEntDd = iEntDy.substring(6, 8);
		String iaddress = "";

		CustMain iCustMain = iCustMainService.custNoFirst(rCustNo, rCustNo, titaVo);
		String iCustName = "";
//		long sno = 0;
		if (iCustMain != null) {
			iCustName = iCustMain.getCustName();// 借款人戶名
		} else {
			throw new LogicException(titaVo, "E0001", "客戶主檔無此戶號:" + iCustNo);
		}
		iaddress = findaddr(iCustMain, titaVo);// 借款人通訊地
		
		FacMain aFacMain = new FacMain();
		FacMainId aFacMainId = new FacMainId();
		aFacMainId.setCustNo(rCustNo);
		aFacMainId.setFacmNo(rFacmNo);
		aFacMain = iFacMainService.findById(aFacMainId, titaVo);
		if (aFacMain == null) {
			throw new LogicException(titaVo, "E0001", "查無額度，戶號:" + iCustNo + "額度:" + iFacmNo);
		}
		String aLineAmt = String.valueOf(aFacMain.getLineAmt());// 核准額度

		int iPayIntDate = 0;
		int iIntStartDate = 0;
		int terms = 0;
		BigDecimal unpaidAmt = BigDecimal.ZERO; // 未收本息
		
		ArrayList<BaTxVo> listBaTxVo = new ArrayList<BaTxVo>();
		baTxCom.setTxBuffer(txbuffer);
		try {
			listBaTxVo = baTxCom.settingUnPaid(txbuffer.getTxBizDate().getTbsDy(), rCustNo, rFacmNo, 0, 1,
					BigDecimal.ZERO, titaVo);// 日期為會計日
		} catch (LogicException e) {
			this.error("baTxCom settingUnPaid ErrorMsg :" + e.getMessage());
		}
		for (BaTxVo baTxVo : listBaTxVo) {
			if (baTxVo.getDataKind() == 2) {
				if (baTxVo.getPayIntDate() > iPayIntDate) {// 取日期最大的for繳息止日
					iPayIntDate = baTxVo.getPayIntDate();
				}
				if (baTxVo.getPaidTerms() > terms) {// 期數最大
					terms = baTxVo.getPaidTerms();
				}
				if (baTxVo.getIntStartDate() > 0) {
					if (iIntStartDate == 0) {
						iIntStartDate = baTxVo.getIntStartDate();
					}
					if (baTxVo.getIntStartDate() < iIntStartDate) {// 取日期最小的for繳息起日
						iIntStartDate = baTxVo.getIntStartDate();
					}
				}
				unpaidAmt = unpaidAmt.add(baTxVo.getPrincipal()); // 未收本
				unpaidAmt = unpaidAmt.add(baTxVo.getInterest()); // 未收息
				unpaidAmt = unpaidAmt.add(baTxVo.getBreachAmt()); // 違約金
				unpaidAmt = unpaidAmt.add(baTxVo.getDelayInt()); // 延遲息
			}
			if (baTxVo.getDataKind() == 3) {
				unpaidAmt = unpaidAmt.subtract(baTxVo.getUnPaidAmt()); // 暫收抵繳(累溢收)
			}
			if (baTxVo.getDataKind() == 1 && baTxVo.getRepayType() == 1) {
				unpaidAmt = unpaidAmt.add(baTxVo.getUnPaidAmt()); // 累短收
			}
		}
		if (terms == 0) {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}
		String iPrevIntDate0 = StringUtils.leftPad(String.valueOf(iIntStartDate), 7, "0");// 計息起日
		String iPrevIntDate1 = StringUtils.leftPad(String.valueOf(iPayIntDate), 7, "0");// 計息止日

		String iPrYyy = iPrevIntDate0.substring(0, 3);
		String iPrMm = iPrevIntDate0.substring(3, 5);
		String iPrDd = iPrevIntDate0.substring(5);
		String iPrYyy1 = iPrevIntDate1.substring(0, 3);
		String iPrMm1 = iPrevIntDate1.substring(3, 5);
		String iPrDd1 = iPrevIntDate1.substring(5);

		String sterms = String.valueOf(terms);
		String iPrinBalance = String.valueOf(unpaidAmt);
		String strContent = "";
		String strFileName = "";

		CollListId iCollListId = new CollListId();
		CollList iCollList = new CollList();
		CdEmp iCdEmp = new CdEmp();
		String iAccTel = "";// 法催人員電話
		String iAccCollPsn = "";
		String iAccCollPsnX = "";// 法催人員姓名
		String iCityCode = "";
		if (adjFlag.equals("1")) {// 通知函-法催人員與電話
			iCollListId.setCustNo(rCustNo);
			iCollListId.setFacmNo(rFacmNo);
			iCollList = iCollListService.findById(iCollListId, titaVo);
			if (iCollList == null) {
				throw new LogicException(titaVo, "E0001", "法催紀錄清單檔無資料，戶號:\" + iCustNo + \"額度:\" + iFacmNo");
			}
			iAccCollPsn = iCollList.getAccCollPsn();// 催收人員
			if (!iAccCollPsn.trim().isEmpty() || !iAccCollPsn.equals("")) {
				iCdEmp = iCdEmpService.findById(iAccCollPsn, titaVo);
				if (iCdEmp != null) {
					iAccCollPsnX = iCdEmp.getFullname();
				}
			}
			if (iCollList.getIsSpecify().equals("Y")) {//個案人員指派則使用CollList電話資料
				if (!iCollList.getAccTelArea().trim().isEmpty() || !iCollList.getAccTelArea().equals("")) {
					iAccTel = iCollList.getAccTelArea().trim() + "-";
				}
				if (!iCollList.getAccTelNo().trim().isEmpty() || !iCollList.getAccTelNo().equals("")) {
					iAccTel = iAccTel + iCollList.getAccTelNo().trim();
				}
				if (!iCollList.getAccTelExt().trim().isEmpty() || !iCollList.getAccTelExt().equals("")) {
					iAccTel = iAccTel + "-" + iCollList.getAccTelExt().trim();
				}
				
			} else {

				iCityCode = iCollList.getCityCode();// 法催地區
				if (!iCityCode.trim().isEmpty() || !iCityCode.equals("")) {
					CdCity iCdCity = null;
					iCdCity = iCdCityService.findById(iCityCode, titaVo);
					if (iCdCity == null) {
						throw new LogicException(titaVo, "E0001", "地區別代碼檔"); // 查無資料
					}
					if (!iCdCity.getAccTelArea().trim().isEmpty() || !iCdCity.getAccTelArea().equals("")) {
						iAccTel = iCdCity.getAccTelArea().trim() + "-";
					}
					if (!iCdCity.getAccTelNo().trim().isEmpty() || !iCdCity.getAccTelNo().equals("")) {
						iAccTel = iAccTel + iCdCity.getAccTelNo().trim();
					}
					if (!iCdCity.getAccTelExt().trim().isEmpty() || !iCdCity.getAccTelExt().equals("")) {
						iAccTel = iAccTel + "-" + iCdCity.getAccTelExt().trim();
					}

				}
			}
		}
		
		switch (adjFlag) {
		case "0": // 列印存證信函
			strFileName = "催收存證信函" + iCustNo + "-" + iFacmNo;
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", strFileName, strFileName + ".TXT", 2);

			makeFile.put("寄件人姓名：新光人壽保險股份有限公司");
			makeFile.put("　　　　　　法定代理人：潘  柏  錚");
			makeFile.put("寄件人詳細地址：台北市松山區南京東路五段125號13樓");
			strContent = "收件人姓名：" + iCustName + "   戶號：" + iCustNo;
			makeFile.put(strContent);
			strContent = "收件人詳細地址：" + iaddress;
			makeFile.put(strContent);
			strContent = "副本收件人姓名：";
			makeFile.put(strContent);
			strContent = "副本收件人詳細地址：";
			makeFile.put(strContent);
			strContent = "副本收件人姓名：";
			makeFile.put(strContent);
			strContent = "副本收件人詳細地址：";
			makeFile.put(strContent);
			strContent = "";
			makeFile.put(strContent);

			strContent = "一、台端前向本公司辦理房屋抵押貸款新台幣" + aLineAmt + "元整，約定於每月" + iPrDd + "日繳交應攤還之本息；惟　台端僅繳至" + iPrYyy + "年"
					+ iPrMm + "月" + iPrDd + "日，共計積欠" + sterms + "期未繳付。";
			makeFile.put(strContent);
			strContent = "二、依約定借款人如有一期未繳付應攤還本金或利息時，全部借款視為到期，借款人應即償還全部借款餘額，為此特通知　台端三日內繳清所積欠之本金、利息、違約金，否則將聲請法院查封拍賣抵押物追償，事涉　台端權益，請速處理，祈勿自誤為禱。";
			makeFile.put(strContent);

			makeFile.close();

			// HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("SenderName", "新光人壽保險股份有限公司");
			// map.put("AgentName", "法定代理人：潘 柏 錚");
			// map.put("SenderAddress", "台北市中正區忠孝西路一段66號18樓");
			// map.put("RcvName1", iCustName + " 戶號 :" + StringUtils.leftPad(iCustNo, 7,
			// "0"));
			// map.put("RcvAddress1", iaddress);
			// map.put("p1", "一、台端前向本公司辦理房屋抵押貸款新台幣" + aLineAmt + "元整，約定於每月" + iPrDd +
			// "日繳交應攤還之本息；惟台端僅繳至" + iPrYyy + "年"
			// + iPrMm + "月" + iPrDd + "日，共計積欠" + sterms + "期未繳付。");
			// map.put("p2",
			// "二、依約定借款人如有一期未繳付應攤還本金或利息時，全部借款視為到期，借款人應即償還全部借款餘額，為此特通知台端三日內繳清所積欠之本金、利息、違約金，否則將聲請法院查封拍賣抵押物追償，事涉台端權益，請速處理，祈勿自誤為禱。");
			// iWarningLetterForm.run(titaVo, map);
			break;
		case "1": // 列印延遲繳款通知函
			strFileName = "延遲繳款通知函" + iCustNo + "-" + iFacmNo;
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", strFileName, strFileName + ".TXT", 2);

			strContent = "新光人壽保險股份有限公司　　通知(戶號 " + iCustNo + "-" + iFacmNo + ")";
			makeFile.put(strContent);
			strContent = "聯絡電話：" + iAccTel + "  " + iAccCollPsnX;
			makeFile.put(strContent);
			strContent = "受文者：" + iCustName + "君     " + iaddress;
			makeFile.put(strContent);
			strContent = "受文者：";
			makeFile.put(strContent);
			strContent = "";
			makeFile.put(strContent);
			strContent = "一、台端前向本公司辦理房屋抵押貸款，約定於每月" + iPrDd + "日繳交應攤還之本息；惟　台端僅繳至" + iPrYyy + "年" + iPrMm + "月" + iPrDd
					+ "日止，積欠計" + sterms + "期。為此特此函知　台端，請於五天內繳清積欠逾期之本息及違約金。";
			makeFile.put(strContent);
			strContent = "    額度" + iFacmNo + "：計息期間 " + iPrYyy + "/" + iPrMm + "/" + iPrDd + " - " + iPrYyy1 + "/"
					+ iPrMm1 + "/" + iPrDd1 + " 總計" + iPrinBalance + "元";
			makeFile.put(strContent);
			strContent = "";
			makeFile.put(strContent);
			strContent = "二、※提醒您！如逾期放款資料依規報送聯合徵信中心，將嚴重影響　台端及保證人信用（將無法辦理增貸，信用卡，其他銀行貸款…），事涉　台端權益，請速處理。";
			makeFile.put(strContent);
			strContent = "    ※自行匯款期款專戶：";
			makeFile.put(strContent);
			strContent = "    解款銀行--- 新光銀行城內分行  ( ATM銀行代號 103 )";
			makeFile.put(strContent);
			strContent = "    收款戶名--- 新光人壽保險股份有限公司";
			makeFile.put(strContent);
			strContent = "    帳號：9510200-" + StringUtils.leftPad(iCustNo, 7, "0");
			makeFile.put(strContent);
			strContent = "";
			makeFile.put(strContent);
			// 列出最近6筆繳款明細   ,2022/1/17取消下列明細內容,USER自行列印L9701報表:客戶往來本息明細表
//			List<Map<String, String>> listL560A = null;
//			try {
//				listL560A = l560AServiceImpl.findLoanBorTx(titaVo, this.index, this.limit);
//			} catch (Exception e) {
//				StringWriter errors = new StringWriter();
//				e.printStackTrace(new PrintWriter(errors));
//				this.info("l560AServiceImpl.findLoanBorTx error = " + errors.toString());
//			}
//			if (listL560A == null || listL560A.size() == 0) {
//				this.info("L560A LoanBorTx DETAIL NO DATA");
//			} else {
//				strContent = " 撥款 入帳日期       放款餘額       計息期間         利率  交易內容    交易金額    作帳金額"
//						+ "       本金       利息     違約金     暫收借     暫收貸       短繳";
//				makeFile.put(strContent);
//				strContent = "-------------------------------------------------------------------------------------------"
//						+ "------------------------------------------------------------------";
//				makeFile.put(strContent);
//
//				String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14";
//				String txt1[] = txt.split(";");
//
//				int count = 0;
//				int loanBal = 0;// 當日餘額
//				int sumPrincipal = 0;// 本金小計
//				int sumInterest = 0;// 利息小計
//				String strContent1 = "";
//				String strContent2 = "";
//				String strContent3 = "";
//				String strContent4 = "";
//				String strContent5 = "";
//				String strContent6 = "";
//				String tempDate = "";
//				int tempLoanBal= 0;
//				int tempTxAmt= 0;
//				int tempTxAmt2= 0;
//				int tempPrincipal = 0;
//				int tempInterest = 0;
//				int tempBreachAmt = 0;
//				int tempTempAmt1 = 0;
//				int tempTempAmt2 = 0;
//				int tempUnpaidAmt = 0;
//				boolean samEntdate = false;
//
//				DecimalFormat df1 = new DecimalFormat("#,##0");
//				
//				for (Map<String, String> t560A : listL560A) {
//					count = count + 1;
//					strContent = "";
//					int tDate = 0;
//
//					if (tempDate.equals(t560A.get(txt1[1]).trim())) {//入帳日期相同時,不同撥款序號資料合併
//						count = count - 1;
//						samEntdate = true;
//					}else {
//						samEntdate = false;
//					}
//					if (count == 7) {
//						break;
//					}
//					
//					for (int j = 1; j <= 15; j++) {
//						String strField = "";
//						if (t560A.get(txt1[j - 1]) == null) {
//							strField = "";
//						} else {
//							strField = t560A.get(txt1[j - 1]).trim();
//							
//						}
//						// 格式處理
//						switch (j) {
//						case 1://撥款序號
//							if (samEntdate == true) {
//								strField = " " + makeFile.fillStringL("000", 3, '0');
//							} else {
//								strField = " " + makeFile.fillStringL(strField, 3, '0');
//							}
//							break;
//						case 2://入帳日期
//							if ("0".equals(strField)) {
//								strField = makeFile.fillStringL("000/00/00",10, ' ');
//							} else {
//								tDate = Integer.valueOf(strField) - 19110000;
//								String sDate = String.valueOf(tDate);
//								sDate = sDate.substring(0, 3) + "/" + sDate.substring(3, 5) + "/"
//										+ sDate.substring(5, 7);
//								strField = makeFile.fillStringL(sDate,10, ' ');
//								tempDate = t560A.get(txt1[1]).trim();
//							}
//							break;
//						case 3://放款餘額
//							if (samEntdate == true) {
//								tempLoanBal = tempLoanBal + Integer.valueOf(t560A.get("F2"));
//								strField =  makeFile.fillStringL(df1.format(parse.stringToBigDecimal(String.valueOf(tempLoanBal))), 15,
//										' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 15,
//										' ');
//								tempLoanBal = Integer.valueOf(t560A.get("F2"));
//							}
//							break;
//						case 4://計息期間-起
//							if ("0".equals(strField) || "".equals(strField)) {
//								strField = makeFile.fillStringL("000/00/00",10, ' ');
//							} else {
//								tDate = Integer.valueOf(strField) - 19110000;
//								String sDate = String.valueOf(tDate);
//								sDate = sDate.substring(0, 3) + "/" + sDate.substring(3, 5) + "/"
//										+ sDate.substring(5, 7);
//								strField = makeFile.fillStringL(sDate,10, ' ');
//							}
//							break;
//						case 5://計息期間-迄
//							if ("0".equals(strField) || "".equals(strField)) {
//								strField = makeFile.fillStringL("-000/00/00",10, ' ');
//							} else {
//								tDate = Integer.valueOf(strField) - 19110000;
//								String sDate = String.valueOf(tDate);
//								sDate = "-" + sDate.substring(0, 3) + "/" + sDate.substring(3, 5) + "/"
//										+ sDate.substring(5, 7);
//								strField = makeFile.fillStringL(sDate,10, ' ');
//							}
//							break;
//						case 6://利率
//							strField = makeFile.fillStringL(strField, 8, ' ');
//							break;
//						case 7://交易內容
//							strField = makeFile.fillStringL(strField,10, ' ');
//							break;
//						case 8://交易金額
//							if (samEntdate == true) {
//								tempTxAmt = tempTxAmt + Integer.valueOf(t560A.get("F7"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempTxAmt))), 12, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 12,
//										' ');
//								tempTxAmt = Integer.valueOf(t560A.get("F7"));
//							}
//							break;
//						case 9://作帳金額
//							if (samEntdate == true) {
//								tempTxAmt2 = tempTxAmt2 + Integer.valueOf(t560A.get("F8"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempTxAmt2))), 12, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 12,
//										' ');
//								tempTxAmt2 = Integer.valueOf(t560A.get("F8"));
//							}
//							break;
//						case 10://本金
//							if (samEntdate == true) {
//								tempPrincipal = tempPrincipal + Integer.valueOf(t560A.get("F9"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempPrincipal))), 11, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 11,
//										' ');
//								tempPrincipal = Integer.valueOf(t560A.get("F9"));
//							}
//							break;
//						case 11://利息
//							if (samEntdate == true) {
//								tempInterest = tempInterest + Integer.valueOf(t560A.get("F10"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempInterest))), 11, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 11,
//										' ');
//								tempInterest = Integer.valueOf(t560A.get("F10"));
//							}
//							break;
//						case 12://違約金
//							if (samEntdate == true) {
//								tempBreachAmt = tempBreachAmt + Integer.valueOf(t560A.get("F11"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempBreachAmt))), 11, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 11,
//										' ');
//								tempBreachAmt = Integer.valueOf(t560A.get("F11"));
//							}
//							break;
//						case 13://暫收借
//							if (samEntdate == true) {
//								tempTempAmt1 = tempTempAmt1 + Integer.valueOf(t560A.get("F12"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempTempAmt1))), 11, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 11,
//										' ');
//								tempTempAmt1 = Integer.valueOf(t560A.get("F12"));
//							}
//							break;
//						case 14://暫收貸
//							if (samEntdate == true) {
//								tempTempAmt2 = tempTempAmt2 + Integer.valueOf(t560A.get("F13"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempTempAmt2))), 11, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 11,
//										' ');
//								tempTempAmt2 = Integer.valueOf(t560A.get("F13"));
//							}
//							break;
//						case 15://短繳
//							if (samEntdate == true) {
//								tempUnpaidAmt = tempUnpaidAmt + Integer.valueOf(t560A.get("F14"));
//								strField = makeFile.fillStringL(
//										df1.format(parse.stringToBigDecimal(String.valueOf(tempUnpaidAmt))), 11, ' ');
//							} else {
//								strField = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(strField)), 11,
//										' ');
//								tempUnpaidAmt = Integer.valueOf(t560A.get("F14"));
//							}
//							break;
//						default:
//							strField = "";
//							break;
//						}
//						strContent = strContent + strField;
//					}
//					
//					//資料為入帳日期遞減排序,只抓日期最近的6筆資料,要列印的順序相反,以入帳日期遞增顯示
//					if (count == 1) {
//						loanBal = tempLoanBal;
//						strContent6 = strContent;
//					}
//					if (count == 2) {
//						strContent5 = strContent;
//					}
//					if (count == 3) {
//						strContent4 = strContent;
//					}
//					if (count == 4) {
//						strContent3 = strContent;
//					}
//					if (count == 5) {
//						strContent2 = strContent;
//					}
//					if (count == 6) {
//						strContent1 = strContent;
//					}
//					sumPrincipal = sumPrincipal + Integer.valueOf(t560A.get("F9"));
//					sumInterest = sumInterest + Integer.valueOf(t560A.get("F10"));
//
//				} // for
//				if (!"".equals(strContent1)){
//					makeFile.put(strContent1);
//				}
//				if (!"".equals(strContent2)){
//					makeFile.put(strContent2);
//				}
//				if (!"".equals(strContent3)){
//					makeFile.put(strContent3);
//				}
//				if (!"".equals(strContent4)){
//					makeFile.put(strContent4);
//				}
//				if (!"".equals(strContent5)){
//					makeFile.put(strContent5);
//				}
//				if (!"".equals(strContent6)){
//					makeFile.put(strContent6);
//				}
//				
//				strContent = "-------------------------------------------------------------------------------------------"
//						+ "------------------------------------------------------------------";
//				makeFile.put(strContent);
//				
//				String sloanBal = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(String.valueOf(loanBal))),15, ' ');
//				String ssumPrincipal = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(String.valueOf(sumPrincipal))),15, ' ');
//				String ssumInterest = makeFile.fillStringL(df1.format(parse.stringToBigDecimal(String.valueOf(sumInterest))),15, ' ');
//
//				strContent = " " + iEntyy + "年" + iEntMm + "月" + iEntDd + "日當日餘額:" + sloanBal + "        本金小計:"
//						+ ssumPrincipal + "        利息小計:" + ssumInterest;
//				makeFile.put(strContent);
//
//			} // else

			strContent = "";
			makeFile.put(strContent);
			strContent = "";
			makeFile.put(strContent);

			strContent = "中　　華　　民　　國　" + iCalyy + "　年　" + iCalMm + "　月　" + iCalDd + "　日";
			makeFile.put(strContent);

			makeFile.close();

			//產pdf寫法,已改為txt故點掉
			// open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", "延遲繳款通知函" +
			// iCustNo + "-" + iFacmNo, "Normal",
			// "A4", "P");
			// printImageCm(1, 1, 35, "SklLogo.jpg");

			// setFont(1, 14);

			// printCm(10,2,"倘如您已繳款，則無須理會此通知函！","L");

			// setFont(1, 20);
			// printCm(10,3,"延遲繳款通知函","C");

			// setFont(1, 18);
			// printCm(1, 5, iCustName+" 生生/小姐 台照：");
			// setFont(1, 14);
			// printRectCm(1, 6, 70, 20 , "
			// 台端前向本公司辦理房屋抵押貸款，至民國"+iPrYyy+"年"+iPrMm+"月"+iPrDd+"日止，尚積欠"+sterms+"期期款未繳納，應納金額共"+iPrinBalance+"元，特函提醒台端儘速處理，如您仍未按時依約繳款，將會使您與保證人的信用出現不良紀錄，亦可能影響您們未來與各銀行間之往來甚鉅（諸如持用支票、信用卡、信用貸款等）。");
			// printRectCm(1, 9, 70, 20, "
			// 為維護您的個人信用，請儘速繳清所應支付之本金、利息及違約金，或電洽本公司承辦人員洽商還款事宜。");
			// printRectCm(1, 11,70,20, "【繳期款專戶】");
			// printRectCm(1, 12,70,20, "解款銀行：新光銀行 城內分行（ATM銀行代號 103）");
			// printRectCm(1, 13,70,20, "戶 名：新光人壽保險股份有限公司");
			// printRectCm(1, 14,70,20, "帳 號：9510200-"+StringUtils.leftPad(iCustNo, 7,"0"));
			// setFont(1, 13);
			// printRectCm(1, 16,70,20,
			// "※如您的行動電話、住家、公司電話及連絡地址已有變更，請儘速與我們聯絡更正，讓我們持續為您服務，並感謝您對本公司的支持與愛護。");
			// setFont(1, 15);
			// printCm(1, 18, "敬 祝");
			// printCm(3, 19, "順 頌");
			// setFont(1, 13);
			// printCm(7, 20, "新光人壽保險股份有限公司 放款部");
			// printCm(7, 21, "承辦人員："+iAccCollPsnX);
			// printCm(7, 22, "電 話："+iAccTel);
			// setFont(1, 15);
			// printCm(10,27,"中 華 民 國 "+iCalyy+" 年 "+iCalMm+" 月 "+iCalDd+" 日","C");
			// sno = close();

			// toPdf(sno);
			break;
		}
	}
	
	public String findaddr(CustMain tCustMain, TitaVo titaVo) throws LogicException {
		String iCityCode = tCustMain.getCurrCityCode();
		String iAreaCode = tCustMain.getCurrAreaCode();
		String iRoad = tCustMain.getCurrRoad();
		String iSection = tCustMain.getCurrSection();
		String iAlley = tCustMain.getCurrAlley();
		String iLane = tCustMain.getCurrLane();
		String iCurrNum = tCustMain.getCurrNum();
		String iCurrNumDash = tCustMain.getCurrNumDash();
		String iCurrFloor = tCustMain.getCurrFloor();
		String iCurrFloorDash = tCustMain.getCurrFloorDash();
		String iCity = "";
		String iArea = "";
		String iAddress = ""; // 收件人地址
		CdCity iCdCity = new CdCity();
		CdArea iCdArea = new CdArea();
		CdAreaId iCdAreaId = new CdAreaId();
		iCdAreaId.setAreaCode(iAreaCode);
		iCdAreaId.setCityCode(iCityCode);
		iCdCity = iCdCityService.findById(iCityCode, titaVo);
		iCdArea = iCdAreaService.findById(iCdAreaId, titaVo);
		if (iCdCity != null) {
			iCity = iCdCity.getCityItem();
		}
		if (iCdArea != null) {
			iArea = iCdArea.getAreaItem();
		}
		if (!iSection.trim().isEmpty()) {
			iSection = iSection + "段";
		}
		if (!iAlley.trim().isEmpty()) {
			iAlley = iAlley + "巷";
		}
		if (!iLane.trim().isEmpty()) {
			iLane = iLane + "弄";
		}

		if (!iCurrNum.trim().isEmpty()) {
			iCurrNum = iCurrNum + "號";
		}
		if (!iCurrNumDash.trim().isEmpty()) {
			iCurrNumDash = "之" + iCurrNumDash;
		}
		if (!iCurrFloor.trim().isEmpty()) {
			iCurrFloor = iCurrFloor + "樓";
		}
		if (!iCurrFloorDash.trim().isEmpty()) {
			iCurrFloorDash = "之" + iCurrFloorDash;
		}
		iAddress = iCity + iArea + iRoad + iSection + iAlley + iLane + iCurrNum + iCurrNumDash + iCurrFloor
				+ iCurrFloorDash;

		return iAddress;
	}
		
}