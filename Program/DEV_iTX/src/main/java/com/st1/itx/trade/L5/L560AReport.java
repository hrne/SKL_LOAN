package com.st1.itx.trade.L5;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.report.WarningLetterForm;

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
	WarningLetterForm iWarningLetterForm;
	@Override
	public void printTitle() {
//		this.print(-8, 1, "┌────────────────────┬──────┬─────────┐");
//		this.print(-9, 1, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　│　貸　款　金　額　│");
	}

	@Override
	public void printHeader() {
	}
	
	public long run(TitaVo titaVo) throws LogicException {
		String adjFlag = titaVo.getBtnIndex(); //0-存證信函;1-延遲繳款通知函;2-繳款通知函
		String iCustNo = titaVo.getParam("OOCustNo");
		String iBusDate = titaVo.getEntDy(); //會計日
		String iYyy = iBusDate.substring(1,4);
		String iMm = iBusDate.substring(4,6);
		String iDd = iBusDate.substring(6);
		String iPrinBalance = titaVo.getParam("OOPrinBalance");
		String iOverDueterm = titaVo.getParam("OOOverDueterm");
		CustMain iCustMain = iCustMainService.custNoFirst(Integer.valueOf(iCustNo), Integer.valueOf(iCustNo), titaVo);
		String iCustName = "";
		long sno = 0;
		if (iCustMain != null) {
			iCustName = iCustMain.getCustName();
		}else {
			throw new LogicException(titaVo, "E0001", "客戶主檔無此戶號:"+titaVo.getParam("OOCustNo"));
		}
		
		
		String aFacmNo = titaVo.getParam("OOFacmNo");
		FacMain aFacMain = new FacMain();
		FacMainId aFacMainId = new FacMainId();
		aFacMainId.setCustNo(Integer.valueOf(iCustNo));
		aFacMainId.setFacmNo(Integer.valueOf(aFacmNo));
		aFacMain = iFacMainService.findById(aFacMainId, titaVo);
		if (aFacMain == null) {
			throw new LogicException(titaVo, "E0001", "查無額度，戶號:"+iCustNo+"額度:"+aFacmNo);
		}

		String aLineAmt = String.valueOf(aFacMain.getLineAmt());	
		switch(adjFlag) {
		case "0": //列印存證信函
			String iCityCode = iCustMain.getCurrCityCode();
			String iCity = "";
			String iAreaCode = iCustMain.getCurrAreaCode();
			String iArea = "";
			String iRoad = iCustMain.getCurrRoad();
			String iSection = iCustMain.getCurrSection();
			String iAlley = iCustMain.getCurrAlley();
			String iLane = iCustMain.getCurrLane();
			String iCurrNum = iCustMain.getCurrNum();
			String iCurrNumDash = iCustMain.getCurrNumDash();
			String iCurrFloor = iCustMain.getCurrFloor();
			String iCurrFloorDash = iCustMain.getCurrFloorDash();
			String iAddress = ""; //收件人地址
			CdCity iCdCity = new CdCity();
			CdArea iCdArea = new CdArea();
			CdAreaId iCdAreaId = new CdAreaId();
			iCdAreaId.setAreaCode(iAreaCode);
			iCdAreaId.setCityCode(iCityCode);
			iCdCity = iCdCityService.findById(iCityCode, titaVo);
			iCdArea = iCdAreaService.findById(iCdAreaId, titaVo);
			if (iCdCity!=null) {
				iCity = iCdCity.getCityItem();
			}
			if (iCdArea!=null) {
				iArea = iCdArea.getAreaItem();
			}
			if (!iSection.trim().isEmpty()) {
				iSection = iSection+"段";
			}
			if (!iAlley.trim().isEmpty()) {
				iAlley = iAlley+"巷";
			}
			if (!iLane.trim().isEmpty()) {
				iLane = iLane+"弄";
			}
			
			if (!iCurrNum.trim().isEmpty()) {
				iCurrNum = iCurrNum+"號";
			}
			if (!iCurrNumDash.trim().isEmpty()) {
				iCurrNumDash = "之"+iCurrNumDash;
			}
			if (!iCurrFloor.trim().isEmpty()) {
				iCurrFloor = iCurrFloor+"樓";
			}
			if (!iCurrFloorDash.trim().isEmpty()) {
				iCurrFloorDash = "之"+iCurrFloorDash;
			}
			iAddress = iCity+iArea+iRoad+iSection+iAlley+iLane+iCurrNum+iCurrNumDash+iCurrFloor+iCurrFloorDash;
			
			CollListId iCollListId = new CollListId();
			iCollListId.setCustNo(Integer.valueOf(iCustNo));
			iCollListId.setFacmNo(Integer.valueOf(aFacmNo));
			CollList iCollList = new CollList();
			iCollList = iCollListService.findById(iCollListId, titaVo);
			String iPrevIntDate = String.valueOf(iCollList.getPrevIntDate());
			String iPrYyy = iPrevIntDate.substring(0,3);
			String iPrMm = iPrevIntDate.substring(3,5);
			String iPrDd = iPrevIntDate.substring(5);
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("RcvName1",iCustName+"    戶號 :" + String.format("%07d", 123));
			map.put("RcvAddress1",iAddress);
			map.put("p1", "一、台端前向本公司辦理房屋抵押貸款新台幣"+aLineAmt+"元整，約定於每月二十日繳交應攤還之本息；惟台端僅繳至"+iPrYyy+"年"+iPrMm+"月"+iPrDd+"日，共計積欠"+iOverDueterm+"期未繳付。");
			map.put("p2", "二、依約定借款人如有一期未繳付應攤還本金或利息時，全部借款視為到期，借款人應即償還全部借款餘額，為此特通知台端三日內繳清所積欠之本金、利息、違約金，否則將聲請法院查封拍賣抵押物追償，事涉台端權益，請速處理，祈勿自誤為禱。");
			iWarningLetterForm.run(titaVo,map);
			break;
		case "1": //列印延遲繳款通知函
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060"+iCustNo, "延遲繳款通知函"+iCustNo, "Normal","A4","P");
			printImageCm(1, 1, 35, "SklLogo.jpg");
			
			setFont(1, 14);
			
			printCm(10,2,"倘如您已繳款，則無須理會此通知函！","L");
			
			setFont(1, 20);
			printCm(10,3,"延遲繳款通知函","C");
			
			setFont(1, 18);
			printCm(1, 5, iCustName+"　 生生/小姐　台照：");
			setFont(1, 14);
			printRectCm(1, 6, 70, 20 , "　　台端前向本公司辦理房屋抵押貸款，至民國"+iYyy+"年"+iMm+"月"+iDd+"日止，尚積欠"+iOverDueterm+"期期款未繳納，應納金額共"+iPrinBalance+"元，特函提醒台端儘速處理，如您仍未按時依約繳款，將會使您與保證人的信用出現不良紀錄，亦可能影響您們未來與各銀行間之往來甚鉅（諸如持用支票、信用卡、信用貸款等）。");
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
			printCm(10,27,"中　　華　　民　　國　"+iYyy+"　年　"+iMm+"　月　"+iDd+"　日","C");
			sno = close();
			//test only
			toPdf(sno);
			break;
		case "2": //列印繳款通知函
			String iFacmNo = titaVo.getParam("OOFacmNo");
			FacMain iFacMain = new FacMain();
			FacMainId iFacMainId = new FacMainId();
			iFacMainId.setCustNo(Integer.valueOf(iCustNo));
			iFacMainId.setFacmNo(Integer.valueOf(iFacmNo));
			iFacMain = iFacMainService.findById(iFacMainId, titaVo);
			if (iFacMain == null) {
				throw new LogicException(titaVo, "E0001", "查無額度，戶號:"+iCustNo+"額度:"+iFacmNo);
			}
			if (iFacMain.getMaturityDate() == 0) {
				throw new LogicException(titaVo, "E0001", "查無到期日，戶號:"+iCustNo+"額度:"+iFacmNo);
			}
			String iMaturityDate = String.valueOf(iFacMain.getMaturityDate());
			String iMatYyy = iMaturityDate.substring(0,3);
			String iMatMm = iMaturityDate.substring(3, 5);
			String iMatDd = iMaturityDate.substring(5);
			String iLineAmt = String.valueOf(iFacMain.getLineAmt());			
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060"+iCustNo, "繳款通知函"+iCustNo, "Normal","A4","P");
			printImageCm(1, 1, 35, "SklLogo.jpg");
			
			setFont(1, 14);
			
			printCm(10,2,"倘如您已繳款，則無須理會此通知函！","L");
			
			setFont(1, 20);
			printCm(10,3,"繳款通知函","C");
			setFont(1, 18);
			printCm(1, 5, iCustName+"　 生生/小姐　台照：");
			setFont(1, 14);
			printRectCm(1, 6, 70, 20 , "　　台端前向本公司辦理房屋抵押貸款共新台幣（以下同）"+iLineAmt+"元整，其中500元之借款至民國"+iMatYyy+"年"+iMatMm+"月"+iMatDd+"日已到期，尚有應納本金金額50元及計至清償日止之利息，特函提醒台端來電處理結清事宜。");
			printRectCm(1, 9, 70, 20, "　　為維護您的個人信用，請儘速繳清所應支付之本額，或電洽本公司承辦人員洽商相關事宜。");
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
			printCm(10,27,"中　　華　　民　　國　"+iYyy+"　年　"+iMm+"　月　"+iDd+"　日","C");
			sno = close();
			//test only
			toPdf(sno);
			break;
		}
		return sno;
	}
}