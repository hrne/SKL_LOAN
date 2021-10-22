package com.st1.itx.trade.L5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.MakeReport;

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
		if (iCustMain != null) {
			iCustName = iCustMain.getCustName();
		}
		switch(adjFlag) {
		case "0": //列印存證信函
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060"+iCustNo, "存證信函", "Normal","A4","P");
			break;
		case "1": //列印延遲繳款通知函
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060"+iCustNo, "延遲繳款通知函", "Normal","A4","P");
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
			printRectCm(1, 14,70,20, "帳　　號：9510200-1280076");
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
			
			break;
		case "2": //列印繳款通知函
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060"+iCustNo, "繳款通知函", "Normal","A4","P");
			break;
		}
		
			
		long sno = close();
		
		//test only
		toPdf(sno);
		
		return sno;
	}
}