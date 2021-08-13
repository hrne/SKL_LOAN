package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
//import com.st1.itx.db.domain.TxCtrl;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacMainService;
//import com.st1.itx.db.service.TxCtrlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 報表產製順序查詢 <BR>
 * 1.順序為L6068報表代號對照檔查詢、L1908申請不列印書面通知書查詢、客戶檔設定 <BR>
 * 2. call by LXXXX<BR>
 * 
 * @author st1
 * @version 1.0.0
 */
@Component("CustNoticeCom")
@Scope("prototype")
public class CustNoticeCom extends TradeBuffer {

//	private TitaVo titaVo;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public CdReportService cdReportService;

	@Autowired
	public CustNoticeService custNoticeService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CdAreaService cdAreaService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	public CustTelNoService custTelNoService;

	@Autowired
	public FacMainService facMainService;

	private HashMap<tmpFacm, Integer> decuNoticeCheck = new HashMap<>();
	private HashMap<tmpFacm, Integer> textNoticeCheck = new HashMap<>();
	private HashMap<tmpFacm, Integer> mailNoticeCheck = new HashMap<>();

	private int sendCode = 1;

	private int decuCode = 0;
	private int textCode = 0;
	private int mailCode = 0;

	private String reportPhoneNo = "";
	private String reportEmailAd = "";
	private String reportAddress = "";

	private int flag = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.titaVo = titaVo;
		this.totaVo.init(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 
	 * @param formNo 程式ID or 報表ID
	 * @param custNo 戶號
	 * @param facmNo 額度
	 * @return ReportCode = 1:書信 2:簡訊 3:電郵 9:皆為第一 0:皆無 <br>
	 *         ReportPhoneNo = 電話 <br>
	 *         ReportEmailAd = 電郵地址 <br>
	 *         ReportAddress = 地址 <br>
	 * @throws LogicException ..
	 */
	public TempVo getReportCode(String formNo, int custNo, int facmNo) throws LogicException {
		TempVo tempVo = new TempVo();
		this.info("custNoticeCom.getReportCode Start ...");

		int reportCode = 9;

		getNoticeCode(formNo);

//		1.找CdReport 判斷該寄送規則 & 各通知優先序列

		this.info(formNo + "'s SendCode is : " + sendCode);

//		SendCode = 0:不送  1:依利率調整通知方式  2:依設定優先序
//		預設優先順序   手機 > MAIL > 書信
		switch (sendCode) {
		case 0:
			reportCode = 0;
			break;
		case 1:
			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(custNo, custNo);

			FacMain tFacMain = new FacMain();
			FacMainId tFacMainId = new FacMainId();

			tFacMainId.setCustNo(custNo);
			tFacMainId.setFacmNo(facmNo);

			tFacMain = facMainService.findById(tFacMainId);

			reportCode = parse.stringToInteger(tFacMain.getRateAdjNoticeCode());

//			reportCode 共用代碼檔
//			1:電子郵件 
//			2:書面通知 
//			3:簡訊通知

//			flag 1:書信 2:簡訊 3:電郵
			switch (reportCode) {
			case 1:
				getEmail(tCustMain);
				if (!"".equals(reportEmailAd)) {
					flag = 3;
				}
				break;
			case 2:
				getAdress(tCustMain);
				if (!"".equals(reportAddress)) {
					flag = 1;
				}
				break;
			case 3:
				getPhone(tCustMain);
				if (!"".equals(reportPhoneNo)) {
					flag = 2;
				}
				break;
			}

			break;
		case 2:

//			改為+一個Method先By交易將Code找出來(1,2,3,9)
//			後面再依Code將對應通知方式放入(書信:地址、簡訊:手機、電郵:Mail)
			findNotice(formNo, custNo, facmNo);

			tmpFacm tmp = new tmpFacm(custNo, facmNo);
			tmpFacm tmp2 = new tmpFacm(custNo, 0);

			getResult(tmp);

			if (flag == 0) {
				getResult(tmp2);
			}

			break;
		default:
			reportCode = 0;
			break;
		}

		if (!"".equals(reportPhoneNo) && !"".equals(reportEmailAd) && !"".equals(reportAddress)) {
			flag = 9;
		}

		tempVo.putParam("ReportCode", flag);
		tempVo.putParam("ReportPhoneNo", reportPhoneNo);
		tempVo.putParam("ReportEmailAd", reportEmailAd);
		tempVo.putParam("ReportAddress", reportAddress);

		return tempVo;
	}

	private void getResult(tmpFacm tmp) {
		flag = 0;
		reportAddress = "";
		reportPhoneNo = "";
		reportEmailAd = "";

		this.info("getResult ...");
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tmp.getCustNo(), tmp.getCustNo());

		this.info("decuNoticeCheck ..." + decuNoticeCheck.get(tmp));
		this.info("textNoticeCheck ..." + textNoticeCheck.get(tmp));
		this.info("mailNoticeCheck ..." + mailNoticeCheck.get(tmp));

		if (decuNoticeCheck.get(tmp) == null || textNoticeCheck.get(tmp) == null || mailNoticeCheck.get(tmp) == null) {
			return;
		}

		if (decuNoticeCheck.get(tmp) == 1 || textNoticeCheck.get(tmp) == 1 || mailNoticeCheck.get(tmp) == 1) {
			if (decuNoticeCheck.get(tmp) == 1) {

				getAdress(tCustMain);

				if (!"".equals(reportAddress)) {
					flag = 1;
				}
			}
			if (textNoticeCheck.get(tmp) == 1) {

				getPhone(tCustMain);

				if (!"".equals(reportPhoneNo)) {
					flag = 2;
				}
			}
			if (mailNoticeCheck.get(tmp) == 1) {

				getEmail(tCustMain);

				if (!"".equals(reportEmailAd)) {
					flag = 3;
				}
			}

			if (flag >= 1) {
				return;
			}
		}

		if (decuNoticeCheck.get(tmp) == 2 || textNoticeCheck.get(tmp) == 2 || mailNoticeCheck.get(tmp) == 2) {
			if (decuNoticeCheck.get(tmp) == 2) {

				getAdress(tCustMain);

				if (!"".equals(reportAddress)) {
					flag = 1;
				}
			}
			if (textNoticeCheck.get(tmp) == 2) {

				getPhone(tCustMain);

				if (!"".equals(reportPhoneNo)) {
					flag = 2;
				}

			}
			if (mailNoticeCheck.get(tmp) == 2) {

				getEmail(tCustMain);

				if (!"".equals(reportEmailAd)) {
					flag = 3;
				}

			}
			if (flag >= 1) {
				return;
			}
		}

		if (decuNoticeCheck.get(tmp) == 3) {

			getAdress(tCustMain);

			if (!"".equals(reportAddress)) {
				flag = 1;
			}
		}
		if (textNoticeCheck.get(tmp) == 3) {

			getPhone(tCustMain);

			if (!"".equals(reportPhoneNo)) {
				flag = 2;
			}

		}
		if (mailNoticeCheck.get(tmp) == 3) {

			getEmail(tCustMain);

			if (!"".equals(reportEmailAd)) {
				flag = 3;
			}
		}
	}

	private void getAdress(CustMain tCustMain) {
		if (tCustMain != null) {
			reportAddress = getCurrAddress(tCustMain);
		}
	}

	/**
	 * 
	 * @param custMain 客戶主檔
	 * @return regAddress = 通訊地址 <br>
	 */
	public String getCurrAddress(CustMain custMain) {
		String currAddress = "";

		if (!"".equals(custMain.getCurrCityCode())) {
			CdCity cdCity = cdCityService.findById(custMain.getCurrCityCode());
			if (cdCity != null) {
				currAddress += cdCity.getCityItem();

				if (!"".equals(custMain.getCurrAreaCode())) {
					CdAreaId cdAreaId = new CdAreaId();
					cdAreaId.setCityCode(custMain.getCurrCityCode());
					cdAreaId.setAreaCode(custMain.getCurrAreaCode());
					CdArea cdArea = cdAreaService.findById(cdAreaId);
					if (cdArea != null) {
						currAddress += cdArea.getAreaItem();
					}
				}
			}
		}

		currAddress += custMain.getCurrRoad();
		if (!"".equals(custMain.getCurrSection())) {
			currAddress += custMain.getCurrSection() + "段";
		}
		if (!"".equals(custMain.getCurrAlley())) {
			currAddress += custMain.getCurrAlley() + "巷";
		}
		if (!"".equals(custMain.getCurrLane())) {
			currAddress += custMain.getCurrLane() + "弄";
		}
		if (!"".equals(custMain.getCurrNum())) {
			currAddress += custMain.getCurrNum() + "號";
		}
		String numDash = "";
		if (!"".equals(custMain.getCurrNumDash())) {
			currAddress += "-" + custMain.getCurrNumDash();
			numDash = ",";
		}
		if (!"".equals(custMain.getCurrFloor())) {
			currAddress += numDash + custMain.getCurrFloor() + "樓";
		}
		if (!"".equals(custMain.getCurrFloorDash())) {
			currAddress += "-" + custMain.getCurrFloorDash();
		}

		this.info("currAddress ..." + currAddress);
		return currAddress;
	}

	/**
	 * 
	 * @param custMain 客戶主檔
	 * @return regAddress = 戶籍地址 <br>
	 */
	public String getRegAddress(CustMain custMain) {
		String regAddress = "";

		if (!"".equals(custMain.getRegCityCode())) {
			CdCity cdCity = cdCityService.findById(custMain.getRegCityCode());
			if (cdCity != null) {
				regAddress += cdCity.getCityItem();

				if (!"".equals(custMain.getRegAreaCode())) {
					CdAreaId cdAreaId = new CdAreaId();
					cdAreaId.setCityCode(custMain.getRegCityCode());
					cdAreaId.setAreaCode(custMain.getRegAreaCode());
					CdArea cdArea = cdAreaService.findById(cdAreaId);
					if (cdArea != null) {
						regAddress += cdArea.getAreaItem();
					}
				}
			}
		}

		regAddress += custMain.getRegRoad();
		if (!"".equals(custMain.getRegSection())) {
			regAddress += custMain.getRegSection() + "段";
		}
		if (!"".equals(custMain.getRegAlley())) {
			regAddress += custMain.getRegAlley() + "巷";
		}
		if (!"".equals(custMain.getRegLane())) {
			regAddress += custMain.getRegLane() + "弄";
		}
		if (!"".equals(custMain.getRegNum())) {
			regAddress += custMain.getRegNum() + "號";
		}
		String numDash = "";
		if (!"".equals(custMain.getRegNumDash())) {
			regAddress += "-" + custMain.getRegNumDash();
			numDash = ",";
		}
		if (!"".equals(custMain.getRegFloor())) {
			regAddress += numDash + custMain.getRegFloor() + "樓";
		}
		if (!"".equals(custMain.getRegFloorDash())) {
			regAddress += "-" + custMain.getRegFloorDash();
		}

		this.info("regAddress ..." + regAddress);
		return regAddress;
	}

	private void getPhone(CustMain tCustMain) {
		if (tCustMain != null) {
			List<CustTelNo> lCustTelNo = new ArrayList<CustTelNo>();

			Slice<CustTelNo> slCustTelNo = custTelNoService.findCustUKey(tCustMain.getCustUKey(), this.index, Integer.MAX_VALUE);
			lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();

			if (lCustTelNo != null && lCustTelNo.size() != 0) {
				for (CustTelNo tCustTelNo : lCustTelNo) {
//					03:手機  05:簡訊
					if ("05".equals(tCustTelNo.getTelTypeCode()) || "03".equals(tCustTelNo.getTelTypeCode())) {
						if ("Y".equals(tCustTelNo.getEnable())) {
							reportPhoneNo = tCustTelNo.getTelNo();
							this.info("reportPhoneNo ..." + reportPhoneNo);
						}
					}
				}
			}
		}
	}

	private void getEmail(CustMain tCustMain) {
		if (tCustMain != null) {
			reportEmailAd = tCustMain.getEmail();
			this.info("reportEmailAd ..." + reportEmailAd);
		}
	}

	private void findNotice(String formNo, int custNo, int facmNo) {
		this.info("findNotice ... ");

		List<CustNotice> lCustNotice0 = new ArrayList<CustNotice>();
		List<CustNotice> lCustNoticeX = new ArrayList<CustNotice>();

		Slice<CustNotice> slCustNotice0 = custNoticeService.facmNoEq(custNo, 0, 0, this.index, Integer.MAX_VALUE);
		lCustNotice0 = slCustNotice0 == null ? null : slCustNotice0.getContent();
		if (facmNo != 0) {
			Slice<CustNotice> slCustNoticeX = custNoticeService.facmNoEq(custNo, facmNo, facmNo, this.index, Integer.MAX_VALUE);
			lCustNoticeX = slCustNoticeX == null ? null : slCustNoticeX.getContent();
		}

		tmpFacm tmp = new tmpFacm(custNo, facmNo);

//		2.notice檔找 
//		a.戶號額度 
//		b.戶號 : 若a以a為主
//		c.未建檔 : 擺入report檔值

		if (lCustNoticeX != null && lCustNoticeX.size() != 0) {
			for (CustNotice tCustNotice : lCustNoticeX) {
				if (formNo.equals(tCustNotice.getFormNo())) {

					this.info(parse.IntegerToString(tCustNotice.getCustNo(), 7) + "-" + parse.IntegerToString(tCustNotice.getFacmNo(), 3) + "'s PaperNotice : " + tCustNotice.getPaperNotice());
					this.info(parse.IntegerToString(tCustNotice.getCustNo(), 7) + "-" + parse.IntegerToString(tCustNotice.getFacmNo(), 3) + "'s MsgNotice : " + tCustNotice.getMsgNotice());
					this.info(parse.IntegerToString(tCustNotice.getCustNo(), 7) + "-" + parse.IntegerToString(tCustNotice.getFacmNo(), 3) + "'s EmailNotice : " + tCustNotice.getEmailNotice());

					if (tCustNotice.getFacmNo() == facmNo) {
						if ("N".equals(tCustNotice.getPaperNotice())) {
							decuNoticeCheck.put(tmp, 0);
						} else {
							decuNoticeCheck.put(tmp, decuCode);
						}
						if ("N".equals(tCustNotice.getMsgNotice())) {
							textNoticeCheck.put(tmp, 0);
						} else {
							textNoticeCheck.put(tmp, textCode);
						}
						if ("N".equals(tCustNotice.getEmailNotice())) {
							mailNoticeCheck.put(tmp, 0);
						} else {
							mailNoticeCheck.put(tmp, mailCode);
						}
					}
				}
			}
		} else if (lCustNotice0 != null && lCustNotice0.size() != 0) {
			for (CustNotice tCustNotice : lCustNotice0) {

				this.info(parse.IntegerToString(tCustNotice.getCustNo(), 7) + "-" + parse.IntegerToString(tCustNotice.getFacmNo(), 3) + "'s PaperNotice : " + tCustNotice.getPaperNotice());
				this.info(parse.IntegerToString(tCustNotice.getCustNo(), 7) + "-" + parse.IntegerToString(tCustNotice.getFacmNo(), 3) + "'s MsgNotice : " + tCustNotice.getMsgNotice());
				this.info(parse.IntegerToString(tCustNotice.getCustNo(), 7) + "-" + parse.IntegerToString(tCustNotice.getFacmNo(), 3) + "'s EmailNotice : " + tCustNotice.getEmailNotice());

				if ("N".equals(tCustNotice.getPaperNotice())) {
					decuNoticeCheck.put(tmp, 0);
				} else {
					decuNoticeCheck.put(tmp, decuCode);
				}
				if ("N".equals(tCustNotice.getMsgNotice())) {
					textNoticeCheck.put(tmp, 0);
				} else {
					textNoticeCheck.put(tmp, textCode);
				}
				if ("N".equals(tCustNotice.getEmailNotice())) {
					mailNoticeCheck.put(tmp, 0);
				} else {
					mailNoticeCheck.put(tmp, mailCode);
				}
			}
		} else {
//			預設都沒設定L1109，就是要寄送
			decuNoticeCheck.put(tmp, decuCode);
			textNoticeCheck.put(tmp, textCode);
			mailNoticeCheck.put(tmp, mailCode);
		}

		this.info("decuNoticeCheck : " + decuNoticeCheck.get(tmp));
		this.info("textNoticeCheck : " + textNoticeCheck.get(tmp));
		this.info("mailNoticeCheck : " + mailNoticeCheck.get(tmp));
	}

	public void getNoticeCode(String txcd) {
		CdReport tCdReport = new CdReport();

		tCdReport = cdReportService.findById(txcd);

		if (tCdReport != null) {

			sendCode = tCdReport.getSendCode();

			if (tCdReport.getSendCode() == 2) {
				decuCode = tCdReport.getLetter();
				textCode = tCdReport.getMessage();
				mailCode = tCdReport.getEmail();
			}
		} else {
			this.info("tCdReport is null... ");
		}
	}

	private class tmpFacm {

		private int custNo = 0;
		private int facmNo = 0;

		public tmpFacm(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

//		public int getFacmNo() {
//			return facmNo;
//		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			return true;
		}

		private CustNoticeCom getEnclosingInstance() {
			return CustNoticeCom.this;
		}
	}
}
