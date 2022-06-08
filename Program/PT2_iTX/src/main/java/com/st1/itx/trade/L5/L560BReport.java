package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.common.NegCom;

@Service("L560BReport")
@Scope("prototype")
/**
 * 列印債協通知單
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L560BReport extends MakeReport {

	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public NegMainService iNegMainService;
	@Autowired
	public JcicZ046Service iJcicZ046Service;
	@Autowired
	public JcicZ047Service iJcicZ047Service;
	@Autowired
	public DateUtil iDateUtil;
	@Autowired
	public NegCom sNegCom;

	@Override
	public void printTitle() {
//		this.print(-8, 1, "┌────────────────────┬──────┬─────────┐");
//		this.print(-9, 1, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　│　貸　款　金　額　│");
	}

	@Override
	public void printHeader() {
	}

	public long run(TitaVo titaVo) throws LogicException {
		String adjFlag = titaVo.getBtnIndex(); // 0-前置協商毀諾通知函;1-前置協商逾期繳款通知函
		String iCustNo = titaVo.getParam("OOCustNo");
		String iBusDate = titaVo.getEntDy(); // 會計日
		String iYyy = iBusDate.substring(1, 4);
		String iMm = iBusDate.substring(4, 6);
		String iCalDy = titaVo.getCalDy(); // 日曆日
		String iCalyy = iCalDy.substring(0, 3);
		String iCalMm = iCalDy.substring(3, 5);
		String iCalDd = iCalDy.substring(5, 7);
		// 檢查客戶檔戶號
		String iCustId = "";
		String iCustName = "";
		CustMain iCustMain = iCustMainService.custNoFirst(Integer.valueOf(iCustNo), Integer.valueOf(iCustNo), titaVo);
		if (iCustMain != null) {
			iCustName = iCustMain.getCustName();
			iCustId = iCustMain.getCustId();
		} else {
			throw new LogicException(titaVo, "E0001", "客戶主檔無此戶號:" + iCustNo);
		}
		// 檢查債協主檔
//		Slice<NegMain> iNegMain = null;
		NegMain rNegMain = new NegMain();
		rNegMain = iNegMainService.custNoFirst(Integer.valueOf(iCustNo), titaVo);
//		if (iNegMain == null) {
//			throw new LogicException(titaVo, "E0001", "債協案件主檔無相符戶號:" + titaVo.getParam("OOCustNo"));
//		}
//		rNegMain = iNegMain.getContent().get(0);

		if (rNegMain == null) {
			throw new LogicException(titaVo, "E0001", "債協案件主檔無相符戶號:" + titaVo.getParam("OOCustNo"));
		}
		switch (adjFlag) {
		case "0": // 前置協商毀諾通知函
			if (!rNegMain.getStatus().equals("0") && !rNegMain.getStatus().equals("2")) {// 債協戶況需為正常或毀諾
				throw new LogicException(titaVo, "E0001", "債協案件主檔無相符戶況:" + iCustNo);
			}

			String iNegDate = "";
			String iNegYyy = "";
			String iNegMm = "";
			String iNegDd = "";
			Slice<JcicZ046> iJcicZ046 = null;
			JcicZ046 rJcicZ046 = new JcicZ046();
			String iCloseDate = ""; // JcicZ046--結案日期
			iJcicZ046 = iJcicZ046Service.custIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
			if (iJcicZ046 == null) {
				throw new LogicException(titaVo, "E0001", "結案通知檔案無此戶號資料:" + iCustNo);
			}
			rJcicZ046 = iJcicZ046.getContent().get(0);
			if (rJcicZ046.getTranKey().equals("D")) {
				throw new LogicException(titaVo, "E0001", "結案通知檔案無相符資料(已報送刪除)");
			}
			if (!rJcicZ046.getCloseCode().equals("00")) {
				throw new LogicException(titaVo, "E0001", "結案通知檔案無相符資料(結案原因不為毀諾)");
			}
			iCloseDate = String.valueOf(rJcicZ046.getCloseDate());
			if (iCloseDate.equals("0") || iCloseDate.trim().isEmpty()) {
				throw new LogicException(titaVo, "E0001", "結案通知檔案無相符資料(結案日期)");
			}
			String iCloseYyy = iCloseDate.substring(0, 3);
			String iCloseMm = iCloseDate.substring(3, 5);
			String iCloseDd = iCloseDate.substring(5);
			// 金融機構無擔保債務協議資料檔案Jcicz047
			JcicZ047 iJcicZ047 = new JcicZ047();
			JcicZ047Id iJcicZ047Id = new JcicZ047Id();
			iJcicZ047Id.setCustId(iCustId);
			iJcicZ047Id.setRcDate(rNegMain.getApplDate());// 協商申請日
			iJcicZ047Id.setSubmitKey("458");
			iJcicZ047 = iJcicZ047Service.findById(iJcicZ047Id, titaVo);
			if (iJcicZ047 == null) {
				throw new LogicException(titaVo, "E0001", "金融機構無擔保債務協議資料檔案無此戶號資料:" + iCustNo);
			}
			iNegDate = StringUtils.leftPad(String.valueOf(iJcicZ047.getSignDate()), 7, "0");// 簽約完成日
			iNegYyy = iNegDate.substring(0, 3);
			iNegMm = iNegDate.substring(3, 5);
			iNegDd = iNegDate.substring(5);

			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", "前置協商毀諾通知函" + iCustNo, "Normal", "A4", "P");
			setFont(1, 20);
			printCm(10, 1, "前置協商毀諾(未依約履行)通知函", "C");
			setFont(1, 14);
			printCm(1, 3, "致　" + iCustName + " 君：");
			printRectCm(2, 4, 64, 20, "台端所辦理之「消費者債務清理條例前置協商」業已毀諾，惠請 查照。");
			printRectCm(2, 5, 63, 20, "一、依「消費者債務清理條例前置協商」相關規定辦理。");
			printRectCm(2, 6, 63, 4, 20,
					"二、台端於" + iNegYyy + "年" + iNegMm + "月" + iNegDd
							+ "日簽訂前置協商機制協議書後並未依約繳還各債權金融機構之欠款，屢經通知，仍未獲繳款，已違反前置協商協議書之約定，本行於" + iCloseYyy + "年" + iCloseMm
							+ "月" + iCloseDd + "日依規定向財團法人金融聯合徵信中心及各債權金融機構通知台端前置協商已毀諾。");
			printRectCm(2, 9, 63, 4, 20, "三、再按「消費者債務清理條例前置協商」規定，毀諾後各金融機構即日起恢復催收作業。");
			printRectCm(2, 11, 63, 20, "四、台端後續可依下列方式清理債務:");
			printRectCm(2.5, 12, 63, 20, "(一)個別協商(向各金融機構申請)。");
			printRectCm(2.5, 13, 63, 4, 20, "(二)個別協商一致性方案(向無擔保最大債權金融機構申請，相關申請資訊請參閱銀行公會網站https://www.twidrp.org.tw/)。");
			printRectCm(2.5, 15, 63, 20, "(三)前置調解(向住、居所地之法院或鄉、鎮、市、區調解委員會聲請)。");
			printRectCm(2.5, 16, 63, 4, 20,
					"(四)更生或清算程序(向住、居所地之法院聲請，相關資訊請查閱司法院網站https://www.judicial.gov.tw/index.asp →業務綜覽→消費者債務清理)。");
			printCm(2.5, 19, "謹 此");
			printCm(20, 23, "新光人壽保險股份有限公司   敬上", "R");
			printCm(20, 24, "（聯絡電話：(02)2389-5858#7076 承辦人員：邱小姐）", "R");
			printCm(10, 27, "中　　華　　民　　國　" + iCalyy + "　年　" + iCalMm + "　月　" + iCalDd + "　日", "C");
			break;
		case "1": // 前置協商逾期繳款通知函
			if (!rNegMain.getStatus().equals("0")) {// 債協戶況須為正常
				throw new LogicException(titaVo, "E0001", "債協案件主檔無相符戶況:" + iCustNo);
			}
			if (!rNegMain.getIsMainFin().equals("Y")) {
				throw new LogicException(titaVo, "E0001", "非最大債權:" + iCustNo);
			}
			int aNextPayDate = rNegMain.getNextPayDate() + 19110000;// 下次應繳日
			int rBusDate = Integer.valueOf(iBusDate) + 19110000; // 會計日
			String rBusDatedd = String.valueOf(rBusDate).substring(6, 8);// 會計日是否為10號

			if (aNextPayDate >= rBusDate) {// 未逾期
				throw new LogicException(titaVo, "E0001", "下次應繳日未大於會計日");
			}

			// 用下次繳款日~會計日計算期數(債協有喘息期)
			int gapMonth = sNegCom.DiffMonth(1, aNextPayDate, rBusDate);
			if (!rBusDatedd.equals("10")) {// 計算月份非10號需加1,10號算未到期
				gapMonth = gapMonth + 1;
			}

			// 計算剩餘期數
			int remainPeriod = sNegCom.nper(rNegMain.getPrincipalBal(), rNegMain.getDueAmt(), rNegMain.getIntRate());
			// 計算應繳金額:最多扣到本金=0為止
			BigDecimal iCount = sNegCom.calAccuDueAmt(rNegMain.getDueAmt(), rNegMain.getPrincipalBal(),
					rNegMain.getIntRate(), gapMonth, 0);// 最後一位參數傳0代表計算本利和

			if (gapMonth > remainPeriod) {// 應繳期數不可大於剩餘期數(不超過到期日)
				gapMonth = remainPeriod;
			}

			if (gapMonth > 7) {
				throw new LogicException(titaVo, "E0001", "逾期資料過多:" + iCustNo);
			}

			int lastPayIntDate = sNegCom.getRepayDate(aNextPayDate, -1, titaVo);// 上次繳息迄日為下次繳款日往前推一個月
			String iDateString = "";
			for (int i = 1; i <= gapMonth; i++) {
				iDateUtil.init();
				iDateUtil.setDate_1(lastPayIntDate);
				iDateUtil.setMons(i);
				String sPayIntDate = String.valueOf(iDateUtil.getCalenderDay() - 19110000);
				String rPayIntDate = StringUtils.leftPad(sPayIntDate, 7, "0");
				String sYyy = rPayIntDate.substring(0, 3);
				String sMm = rPayIntDate.substring(3, 5);
				String sDd = rPayIntDate.substring(5);
				if (i != gapMonth) {
					iDateString = iDateString + sYyy + "年" + sMm + "月" + sDd + "日、";
				} else {
					iDateString = iDateString + sYyy + "年" + sMm + "月" + sDd + "日";
				}
			}

			String reCount = String.valueOf(iCount);
			open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", "前置協商逾期繳款通知函" + iCustNo, "Normal", "A4", "P");
			setFont(1, 20);
			printCm(10, 1, "前置協商逾期繳款通知函", "C");
			setFont(1, 14);
			printCm(1, 3, "敬啟者："+ iCustName);
			printRectCm(1, 4, 70, 20, "台端業依消費者債務清理條例成立前置調解清償方案，依約應於" + iDateString + "繳付協商期付金新臺幣" + reCount
					+ "元，惟迄今尚未繳付，請  台端務必於" + iYyy + "年" + iMm + "月25日前繳納，為維護台端權益，特以此函通知下列事項：");
			printRectCm(1.5, 7, 65, 4, 20,
					"一、依貴我雙方簽訂之前置協商機制協議書（金融機構無擔保債權）約定，倘  台端未依協議書約定條件清償者，除協議書自違約日起失去效力，未到期部分之債務視為全部到期，債權金融機構並得回復依各債權金融機構之原契約條件繼續訴追。");
			printRectCm(1.5, 10, 65, 4, 20, "二、台端倘未能於" + iYyy + "年" + iMm + "月25日前繳足應繳款項，即視為毀諾，請  台端確實依約履行。");
			printRectCm(1.5, 12, 65, 20, "三、台端應繳款項請存入本行協商專戶如下：");
			printRectCm(2.2, 13, 63, 20, "（一）收款銀行：新光銀行城內分行");
			printRectCm(2.2, 14, 63, 20, "（二）收款帳號：	9510500-" + StringUtils.leftPad(iCustNo, 7, "0"));
			printRectCm(2.2, 15, 63, 20, "（三）收款人戶名：新光人壽保險股份有限公司");
			printRectCm(2, 16, 63, 20, "四、台端倘於收到本通知函時已完成繳款，上述事項請免予處理。");
			printCm(2.5, 18, "謹 此");
			printCm(20, 23, "新光人壽保險股份有限公司   敬上", "R");
			printCm(20, 24, "（聯絡電話：(02)2389-5858#7076 承辦人員：邱小姐）", "R");
			break;
		}

		long sno = close();

		toPdf(sno);

		return sno;
	}
}