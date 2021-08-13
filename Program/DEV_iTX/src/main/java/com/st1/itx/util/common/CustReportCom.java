package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
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
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("CustReportCom")
@Scope("prototype")
public class CustReportCom extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(CustReportCom.class);

	private TitaVo titaVo;

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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		this.totaVo.init(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

//	回傳
//	1.int  最優先者 
//		1 = <Decu> 
//		2 = <text> 
//		3 = <eMail>
//		9 = 皆為最優先
//		若無則回傳      0 = <NA>
//	2.String 回傳值
//		1 = <Decu> 
//		2 = <text> = telno
//		3 = <eMail> = address
//		9 = 皆為最優先 
//		若無則回傳      0 = <NA>
	/**
	 * 
	 * @param formNo 程式ID or 報表ID
	 * @param custNo 戶號
	 * @param facmNo 額度
	 * @param rank   第幾順位
	 * @return ReportCode = 1:書信 2:簡訊 3:電郵 9:皆為第一 0:皆無 <br>
	 *         ReportPhoneNo = 電話 <br>
	 *         ReportEmailAd = 電郵地址 <br>
	 *         ReportAddress = 地址 <br>
	 * @throws LogicException ..
	 */
	public TempVo getReportCode(String formNo, int custNo, int facmNo, int rank) throws LogicException {
		TempVo tempVo = new TempVo();

		int reportCode = 9;
		String reportPhoneNo = "";
		String reportEmailAd = "";
		String reportAddress = "";

		CdReport tCdReport = new CdReport();

		tCdReport = cdReportService.findById(formNo);

		tmpFacm tmp = new tmpFacm(custNo, facmNo);
//		1.找CdReport 判斷該寄送規則 & 各通知優先序列

//		SendCode = 0:不送  1:依利率調整通知方式  2:依設定優先序
		switch (tCdReport.getSendCode()) {
		case 0:
			reportCode = 0;
			break;
		case 1:
//			共用代碼檔
//			1:電子郵件 
//			2:書面通知 
//			3:簡訊通知

			FacMain tFacMain = new FacMain();
			FacMainId tFacMainId = new FacMainId();

			tFacMainId.setCustNo(custNo);
			tFacMainId.setFacmNo(facmNo);

			tFacMain = facMainService.findById(tFacMainId);

			reportCode = parse.stringToInteger(tFacMain.getRateAdjNoticeCode());

			break;
		case 2:
			for (int i = 1; i <= 3; i++) {
				if (tCdReport.getLetter() == i) {
					findNotice(formNo, custNo, facmNo, 1, i);
				}
				if (tCdReport.getMessage() == i) {
					findNotice(formNo, custNo, facmNo, 2, i);
				}
				if (tCdReport.getMessage() == i) {
					findNotice(formNo, custNo, facmNo, 3, i);
				}
			}

			if (decuNoticeCheck.get(tmp) == rank) {
				reportCode = 1;
			} else if (textNoticeCheck.get(tmp) == rank) {
				reportCode = 2;
			} else if (mailNoticeCheck.get(tmp) == rank) {
				reportCode = 3;
			} else if (decuNoticeCheck.get(tmp) == textNoticeCheck.get(tmp) && textNoticeCheck.get(tmp) == mailNoticeCheck.get(tmp) && decuNoticeCheck.get(tmp) == 1) {
				reportCode = 9;
			} else {
				reportCode = 0;
			}

			break;
		default:
			reportCode = 0;
			break;
		}

//		Address
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(custNo, facmNo);

		CdArea tCdArea = new CdArea();
		CdAreaId tCdAreaId = new CdAreaId();
		tCdAreaId.setCityCode(tCustMain.getCurrCityCode());
		tCdAreaId.setAreaCode(tCustMain.getCurrAreaCode());
		tCdArea = cdAreaService.findById(tCdAreaId);

		CdCity tCdCity = new CdCity();
		tCdCity = cdCityService.findById(tCustMain.getCurrCityCode());

		if (tCdCity != null && tCdArea != null && tCustMain != null) {
			reportAddress = tCdCity.getCityItem().trim() + tCdArea.getAreaShort().trim() + tCustMain.getCurrRoad().trim() + tCustMain.getCurrSection().trim() + tCustMain.getCurrAlley().trim()
					+ tCustMain.getCurrLane().trim() + tCustMain.getCurrNum().trim() + tCustMain.getCurrNumDash().trim() + tCustMain.getCurrFloor().trim() + tCustMain.getCurrFloorDash().trim();
		}
//		TelePhone
		List<CustTelNo> lCustTelNo = new ArrayList<CustTelNo>();

		Slice<CustTelNo> slCustTelNo = custTelNoService.findCustUKey(tCustMain.getCustUKey(), this.index, Integer.MAX_VALUE);
		lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();

		if (lCustTelNo != null && lCustTelNo.size() != 0) {
			for (CustTelNo tCustTelNo : lCustTelNo) {
				if ("Y".equals(tCustTelNo.getEnable()) && "05".equals(tCustTelNo.getTelTypeCode())) {
					reportPhoneNo = tCustTelNo.getTelNo();
				}
			}
		}

//		Email
		reportEmailAd = tCustMain.getEmail();

		tempVo.putParam("ReportCode", reportCode);
		tempVo.putParam("ReportPhoneNo", reportPhoneNo);
		tempVo.putParam("ReportEmailAd", reportEmailAd);
		tempVo.putParam("ReportAddress", reportAddress);

		return tempVo;
	}

	private void findNotice(String formNo, int custNo, int facmNo, int reportType, int reportSeq) {

		List<CustNotice> lCustNotice = new ArrayList<CustNotice>();

		Slice<CustNotice> slCustNotice = custNoticeService.facmNoEq(custNo, 0, facmNo, this.index, Integer.MAX_VALUE);
		lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();
		tmpFacm tmp = new tmpFacm(custNo, facmNo);

		logger.info("ReportType : " + reportType);
		logger.info("ReportSeq : " + reportSeq);

//		2.notice檔找 
//		a.戶號額度 
//		b.戶號 : 若a以a為主
//		c.未建檔 : 擺入report檔值

		if (lCustNotice != null && lCustNotice.size() != 0) {
			for (CustNotice tCustNotice : lCustNotice) {
				if (tCustNotice.getFacmNo() == facmNo) {
					switch (reportType) {
					case 1:
						if ("Y".equals(tCustNotice.getPaperNotice())) {
							decuNoticeCheck.put(tmp, reportSeq);
						} else {
							decuNoticeCheck.put(tmp, 0);
						}
						break;
					case 2:
						if ("Y".equals(tCustNotice.getMsgNotice())) {
							textNoticeCheck.put(tmp, reportSeq);
						} else {
							textNoticeCheck.put(tmp, 0);
						}
						break;
					case 3:
						if ("Y".equals(tCustNotice.getEmailNotice())) {
							mailNoticeCheck.put(tmp, reportSeq);
						} else {
							mailNoticeCheck.put(tmp, 0);
						}
						break;
					}
				} else if (tCustNotice.getFacmNo() == 0) {
					switch (reportType) {
					case 1:
						if (decuNoticeCheck.containsKey(tmp)) {
							break;
						} else {
							if ("Y".equals(tCustNotice.getPaperNotice())) {
								decuNoticeCheck.put(tmp, reportSeq);
							} else {
								decuNoticeCheck.put(tmp, 0);
							}
						}
						break;
					case 2:
						if (textNoticeCheck.containsKey(tmp)) {
							break;
						} else {
							if ("Y".equals(tCustNotice.getMsgNotice())) {
								textNoticeCheck.put(tmp, reportSeq);
							} else {
								textNoticeCheck.put(tmp, 0);
							}
						}
						break;
					case 3:
						if (mailNoticeCheck.containsKey(tmp)) {
							break;
						} else {
							if ("Y".equals(tCustNotice.getEmailNotice())) {
								mailNoticeCheck.put(tmp, reportSeq);
							} else {
								mailNoticeCheck.put(tmp, 0);
							}
						}
						break;
					}
				}

			}
		} else {
			switch (reportType) {
			case 1:
				decuNoticeCheck.put(tmp, reportSeq);
				break;
			case 2:
				textNoticeCheck.put(tmp, reportSeq);
				break;
			case 3:
				mailNoticeCheck.put(tmp, reportSeq);
				break;
			}
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

		public int getFacmNo() {
			return facmNo;
		}

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

		private CustReportCom getEnclosingInstance() {
			return CustReportCom.this;
		}
	}
}
