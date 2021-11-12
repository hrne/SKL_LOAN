package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
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
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.ClFac;

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
import com.st1.itx.util.report.WarningLetterForm;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.util.common.BaTxCom;


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

	@Override
	public void printTitle() {
//		this.print(-8, 1, "┌────────────────────┬──────┬─────────┐");
//		this.print(-9, 1, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　│　貸　款　金　額　│");
	}

	@Override
	public void printHeader() {
	}
	
	public long exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		String adjFlag = titaVo.getBtnIndex(); // 0-存證信函;1-延遲繳款通知函
		String iCustNo = titaVo.getParam("OOCustNo");
		int rCustNo = Integer.valueOf(iCustNo);
		String iFacmNo = titaVo.getParam("OOFacmNo");
		int rFacmNo = Integer.valueOf(iFacmNo);
		String iCalDy = titaVo.getCalDy(); // 日曆日
		String iCalyy = iCalDy.substring(0, 3);
		String iCalMm = iCalDy.substring(3, 5);
		String iCalDd = iCalDy.substring(5, 7);
		String iaddress = "";
		String guaaddress1 = "";// 保證人通訊地址
		String guaaddress2 = "";
		String guaname1 = "";// 保證人戶名
		String guaname2 = "";
		int approveNo1 = 0;// 擔保品核准號碼
		int approveNo2 = 0;

		CustMain iCustMain = iCustMainService.custNoFirst(rCustNo, rCustNo, titaVo);
		String iCustName = "";
		long sno = 0;
		if (iCustMain != null) {
			iCustName = iCustMain.getCustName();// 借款人戶名
		} else {
			throw new LogicException(titaVo, "E0001", "客戶主檔無此戶號:" + iCustNo);
		}
		iaddress = findaddr(iCustMain, titaVo);// 借款人通訊地
		// 由戶號+額度找前二個擔保品核准號碼
		Slice<ClFac> iClFac = null;
		iClFac = iClFacService.facmNoEq(rCustNo, rFacmNo, 0, Integer.MAX_VALUE, titaVo);
		if (iClFac != null) {
			for (ClFac rClFac : iClFac) {
				if (approveNo1 == 0 || approveNo2 == 0) {
					if (approveNo1 == 0) {
						approveNo1 = rClFac.getApproveNo();
					} else {
						approveNo2 = rClFac.getApproveNo();
					}
				}
			}
		}
		if (approveNo1 == approveNo2) {// 若保證人相同只需印1個
			approveNo2 = 0;
		}
		int gCustNo1 = findCustNo(approveNo1, titaVo);// 保證人1
		if (gCustNo1 > 0) {
			CustMain gCustMain1 = iCustMainService.custNoFirst(gCustNo1, gCustNo1, titaVo);
			if (gCustMain1 != null) {
				guaname1 = gCustMain1.getCustName();
				guaaddress1 = findaddr(iCustMain, titaVo);
			}
		}
		int gCustNo2 = findCustNo(approveNo2, titaVo);// 保證人2
		if (gCustNo2 > 0) {
			CustMain gCustMain2 = iCustMainService.custNoFirst(gCustNo2, gCustNo2, titaVo);
			if (gCustMain2 != null) {
				guaname2 = gCustMain2.getCustName();
				guaaddress2 = findaddr(iCustMain, titaVo);
			}
		}
		
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
			listBaTxVo = baTxCom.settingUnPaid(txbuffer.getTxBizDate().getLbsDy(), rCustNo, rFacmNo, 0, 1,
					BigDecimal.ZERO, titaVo);// 日期為上一個會計日
		} catch (LogicException e) {
			this.error("baTxCom settingUnPaid ErrorMsg :" + e.getMessage());
		}
		for (BaTxVo baTxVo : listBaTxVo) {
			if (baTxVo.getDataKind() == 2) {
				if (baTxVo.getPayIntDate() > iPayIntDate) {// 取日期最大的for通知函
					iPayIntDate = baTxVo.getPayIntDate();
				}
				if (baTxVo.getPaidTerms() > terms) {// 期數最大
					terms = baTxVo.getPaidTerms();
				}
				if (baTxVo.getIntStartDate() > 0) {
					if (iIntStartDate == 0) {
						iIntStartDate = baTxVo.getIntStartDate();
					}
					if (baTxVo.getIntStartDate() < iIntStartDate) {// 取日期最小的for存證信函
						iIntStartDate = baTxVo.getIntStartDate();
					}
				}
				unpaidAmt = unpaidAmt.add(baTxVo.getPrincipal()); // 未收本
				unpaidAmt = unpaidAmt.add(baTxVo.getInterest()); // 未收息
				unpaidAmt = unpaidAmt.add(baTxVo.getBreachAmt()); // 違約金
				unpaidAmt = unpaidAmt.add(baTxVo.getDelayInt()); // 延遲息
			}
		}
		
		if (terms == 0) {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}

		String iPrevIntDate = StringUtils.leftPad(String.valueOf(iPayIntDate), 7, "0");
		if (adjFlag.equals("0")) {
			iPrevIntDate = StringUtils.leftPad(String.valueOf(iIntStartDate), 7, "0");// 存證信函使用
		}

		String iPrYyy = iPrevIntDate.substring(0, 3);
		String iPrMm = iPrevIntDate.substring(3, 5);
		String iPrDd = iPrevIntDate.substring(5);
		String sterms = String.valueOf(terms);
		String iPrinBalance = String.valueOf(unpaidAmt);

		CollListId iCollListId = new CollListId();
		CollList iCollList = new CollList();
		CdEmp iCdEmp = new CdEmp();
		String iAccTel = "";// 通知函需用到-未定版
		String iAccCollPsn = "";
		String iAccCollPsnX = "";// 通知函需用到-未定版
		String iCityCode = "";
		if (adjFlag.equals("1")) {// 通知函-法催人員與電話
			iCollListId.setCustNo(rCustNo);
			iCollListId.setFacmNo(rFacmNo);
			iCollList = iCollListService.findById(iCollListId, titaVo);
			if (iCollList == null) {
				throw new LogicException(titaVo, "E0001", "法催紀錄清單檔無資料，戶號:\" + iCustNo + \"額度:\" + iFacmNo");
			}
			iCityCode = iCollList.getCityCode();// 法催地區
			iAccCollPsn = iCollList.getAccCollPsn();// 催收人員
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

				iCdEmp = iCdEmpService.findById(iAccCollPsn, titaVo);
				if (iCdEmp != null) {
					iAccCollPsnX = iCdEmp.getFullname();
				}
			}
		}
		
		switch (adjFlag) {
		case "0": // 列印存證信函
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("SenderName", "新光人壽保險股份有限公司");
			map.put("AgentName", "法定代理人：潘　柏　錚");
			map.put("SenderAddress", "台北市中正區忠孝西路一段66號18樓");
			map.put("RcvName1", iCustName + "    戶號 :" + StringUtils.leftPad(iCustNo, 7, "0"));
			map.put("RcvAddress1", iaddress);
			map.put("RcvName2",guaname1);
			map.put("RcvAddress2",guaaddress1);
			map.put("RcvName3",guaname2);
			map.put("RcvAddress3",guaaddress2);
			map.put("p1", "一、台端前向本公司辦理房屋抵押貸款新台幣" + aLineAmt + "元整，約定於每月" + iPrDd + "日繳交應攤還之本息；惟台端僅繳至" + iPrYyy + "年"
					+ iPrMm + "月" + iPrDd + "日，共計積欠" + sterms + "期未繳付。");
			map.put("p2",
					"二、依約定借款人如有一期未繳付應攤還本金或利息時，全部借款視為到期，借款人應即償還全部借款餘額，為此特通知台端三日內繳清所積欠之本金、利息、違約金，否則將聲請法院查封拍賣抵押物追償，事涉台端權益，請速處理，祈勿自誤為禱。");
			iWarningLetterForm.run(titaVo, map);
			break;
		case "1": // 列印延遲繳款通知函
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", "延遲繳款通知函" + iCustNo + "-" + iFacmNo, "Normal",
					"A4", "P");
			printImageCm(1, 1, 35, "SklLogo.jpg");
			
			setFont(1, 14);
			
			printCm(10,2,"倘如您已繳款，則無須理會此通知函！","L");
			
			setFont(1, 20);
			printCm(10,3,"延遲繳款通知函","C");
			
			setFont(1, 18);
			printCm(1, 5, iCustName+"　 生生/小姐　台照：");
			setFont(1, 14);
			printRectCm(1, 6, 70, 20 , "　　台端前向本公司辦理房屋抵押貸款，至民國"+iPrYyy+"年"+iPrMm+"月"+iPrDd+"日止，尚積欠"+sterms+"期期款未繳納，應納金額共"+iPrinBalance+"元，特函提醒台端儘速處理，如您仍未按時依約繳款，將會使您與保證人的信用出現不良紀錄，亦可能影響您們未來與各銀行間之往來甚鉅（諸如持用支票、信用卡、信用貸款等）。");
			printRectCm(1, 9, 70, 20, "　　為維護您的個人信用，請儘速繳清所應支付之本金、利息及違約金，或電洽本公司承辦人員洽商還款事宜。");
			printRectCm(1, 11,70,20, "【繳期款專戶】");
			printRectCm(1, 12,70,20, "解款銀行：新光銀行　城內分行（ATM銀行代號  103）");
			printRectCm(1, 13,70,20, "戶　　名：新光人壽保險股份有限公司");
			printRectCm(1, 14,70,20, "帳　　號：9510200-"+StringUtils.leftPad(iCustNo, 7,"0"));
			setFont(1, 13);
			printRectCm(1, 16,70,20, "※如您的行動電話、住家、公司電話及連絡地址已有變更，請儘速與我們聯絡更正，讓我們持續為您服務，並感謝您對本公司的支持與愛護。");
			setFont(1, 15);
			printCm(1, 18, "敬　祝");
			printCm(3, 19, "順　頌");
			setFont(1, 13);
			printCm(7, 20, "新光人壽保險股份有限公司 放款部");
			printCm(7, 21, "承辦人員：邱怡婷");
			printCm(7, 22, "電　　話：(02) 2389-5858 分機 7076");
			setFont(1, 15);
			printCm(10,27,"中　　華　　民　　國　"+iCalyy+"　年　"+iCalMm+"　月　"+iCalDd+"　日","C");
			sno = close();

			toPdf(sno);
			break;
		}
		return sno;
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
	
	public int findCustNo(int iapproveNo, TitaVo titaVo) throws LogicException {
		String iGuaUKey = "";
		int tCustNo = 0;
		if (iapproveNo > 0) {// 找保證人檔
			Slice<Guarantor> iGuarantor = null;
			iGuarantor = iGuarantorService.approveNoEq(iapproveNo, 0, Integer.MAX_VALUE, titaVo);
			if (iGuarantor != null) {
				for (Guarantor rGuarantor : iGuarantor) {
					if (rGuarantor.getGuaStatCode().equals("1")) {
						iGuaUKey = rGuarantor.getGuaUKey();
						break;
					}
				}
			}
		}
		if (!iGuaUKey.trim().isEmpty()) {// 找客戶檔
			CustMain tCustMain = iCustMainService.findById(iGuaUKey, titaVo);
			if (tCustMain != null) {
				tCustNo = tCustMain.getCustNo();
			}
		}
		return tCustNo;
	}
	
}