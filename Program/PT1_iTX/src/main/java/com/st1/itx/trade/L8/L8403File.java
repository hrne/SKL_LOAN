package com.st1.itx.trade.L8;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ045LogService;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.db.service.JcicZ046LogService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047LogService;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ048LogService;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ049LogService;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.db.service.JcicZ050LogService;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.db.service.JcicZ051LogService;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.db.service.JcicZ052LogService;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.service.JcicZ053LogService;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.db.service.JcicZ054LogService;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.service.JcicZ055LogService;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.db.service.JcicZ056LogService;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.db.service.JcicZ060LogService;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ061LogService;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.db.service.JcicZ062LogService;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.JcicZ063LogService;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.db.service.JcicZ440LogService;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446LogService;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448LogService;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ450LogService;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.db.service.JcicZ451LogService;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.db.service.JcicZ454LogService;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;

@Component("L8403File")
@Scope("prototype")

public class L8403File extends MakeFile {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ040LogService sJcicZ040LogService;
	@Autowired
	public JcicZ041Service sJcicZ041Service;
	@Autowired
	public JcicZ041LogService sJcicZ041LogService;
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ042LogService sJcicZ042LogService;
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ043LogService sJcicZ043LogService;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ044LogService sJcicZ044LogService;
	@Autowired
	public JcicZ045Service sJcicZ045Service;
	@Autowired
	public JcicZ045LogService sJcicZ045LogService;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ046LogService sJcicZ046LogService;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ047LogService sJcicZ047LogService;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ048LogService sJcicZ048LogService;
	@Autowired
	public JcicZ049Service sJcicZ049Service;
	@Autowired
	public JcicZ049LogService sJcicZ049LogService;
	@Autowired
	public JcicZ050Service sJcicZ050Service;
	@Autowired
	public JcicZ050LogService sJcicZ050LogService;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ051LogService sJcicZ051LogService;
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ052LogService sJcicZ052LogService;
	@Autowired
	public JcicZ053Service sJcicZ053Service;
	@Autowired
	public JcicZ053LogService sJcicZ053LogService;
	@Autowired
	public JcicZ054Service sJcicZ054Service;
	@Autowired
	public JcicZ054LogService sJcicZ054LogService;
	@Autowired
	public JcicZ055Service sJcicZ055Service;
	@Autowired
	public JcicZ055LogService sJcicZ055LogService;
	@Autowired
	public JcicZ056Service sJcicZ056Service;
	@Autowired
	public JcicZ056LogService sJcicZ056LogService;
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ060LogService sJcicZ060LogService;
	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ061LogService sJcicZ061LogService;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ062LogService sJcicZ062LogService;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	public JcicZ063LogService sJcicZ063LogService;
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ440LogService sJcicZ440LogService;
	@Autowired
	public JcicZ442Service sJcicZ442Service;
	@Autowired
	public JcicZ442LogService sJcicZ442LogService;
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicZ443LogService sJcicZ443LogService;
	@Autowired
	public JcicZ444Service sJcicZ444Service;
	@Autowired
	public JcicZ444LogService sJcicZ444LogService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ446LogService sJcicZ446LogService;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ447LogService sJcicZ447LogService;
	@Autowired
	public JcicZ448Service sJcicZ448Service;
	@Autowired
	public JcicZ448LogService sJcicZ448LogService;
	@Autowired
	public JcicZ450Service sJcicZ450Service;
	@Autowired
	public JcicZ450LogService sJcicZ450LogService;
	@Autowired
	public JcicZ451Service sJcicZ451Service;
	@Autowired
	public JcicZ451LogService sJcicZ451LogService;
	@Autowired
	public JcicZ454Service sJcicZ454Service;
	@Autowired
	public JcicZ454LogService sJcicZ454LogService;
	@Autowired
	public JcicZ570Service sJcicZ570Service;
	@Autowired
	public JcicZ570LogService sJcicZ570LogService;
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ571LogService sJcicZ571LogService;
	@Autowired
	public JcicZ572Service sJcicZ572Service;
	@Autowired
	public JcicZ572LogService sJcicZ572LogService;
	@Autowired
	public JcicZ573Service sJcicZ573Service;
	@Autowired
	public JcicZ573LogService sJcicZ573LogService;
	@Autowired
	public JcicZ574Service sJcicZ574Service;
	@Autowired
	public JcicZ574LogService sJcicZ574LogService;
	@Autowired
	public JcicZ575Service sJcicZ575Service;
	@Autowired
	public JcicZ575LogService sJcicZ575LogService;

	@Autowired
	DateUtil dDateUtil;
	String brno;
	String fileCode;
	String fileItem;
	String fileName;
	int date;
	String headText;
	String tranCode;
	int iCount;
	String iCustId;
	String iSubmitKey;
	int iRcDate;
	String iMaxMainCode;
	String iAccount;
	int iCloseDate;
	int iPayDate;
	String iCaseStatus;
	int iClaimDate;
	String iCourtCode;
	int iChangePayDate;
	int iApplyDate;
	int iDelayYM;
	String iBankId;
	String itranCode;
	String ixCustId;
	String ixSubmitKey;
	int ixRcDate;
	int iActualFilingDate;
	String iActualFilingMark;

	public long exec(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		tranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');
		this.info("tranCode    = " + tranCode);
		setFileCode();
		openAndPut();
		totalCount();
		putDetailData();
		finalData();
		// 末筆資料

		return this.close();
		// return sno;

	}

	private void finalData() throws LogicException {
		if (iCount == 0) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}
		this.info("finalDate Count = " + iCount);
		String sCount = String.valueOf(iCount);
		String footText = "TRLR" + StringUtils.leftPad(sCount, 8, '0') + StringUtils.rightPad("", 129);
		this.put(footText);
	}

	private void putDetailData() throws LogicException {
		switch (tranCode) {
		case "040":
			doZ040File(titaVo);
			break;
		case "041":
			doZ041File(titaVo);
			break;
		case "042":
			doZ042File(titaVo);
			break;
		case "043":
			doZ043File(titaVo);
			break;
		case "044":
			doZ044File(titaVo);
			break;
		case "045":
			doZ045File(titaVo);
			break;
		case "046":
			doZ046File(titaVo);
			break;
		case "047":
			doZ047File(titaVo);
			break;
		case "048":
			doZ048File(titaVo);
			break;
		case "049":
			doZ049File(titaVo);
			break;
		case "050":
			doZ050File(titaVo);
			break;
		case "051":
			doZ051File(titaVo);
			break;
		case "052":
			doZ052File(titaVo);
			break;
		case "053":
			doZ053File(titaVo);
			break;
		case "054":
			doZ054File(titaVo);
			break;
		case "055":
			doZ055File(titaVo);
			break;
		case "056":
			doZ056File(titaVo);
			break;
		case "060":
			doZ060File(titaVo);
			break;
		case "061":
			doZ061File(titaVo);
			break;
		case "062":
			doZ062File(titaVo);
			break;
		case "063":
			doZ063File(titaVo);
			break;
		case "440":
			doZ440File(titaVo);
			break;
		case "442":
			doZ442File(titaVo);
			break;
		case "443":
			doZ443File(titaVo);
			break;
		case "444":
			doZ444File(titaVo);
			break;
		case "446":
			doZ446File(titaVo);
			break;
		case "447":
			doZ447File(titaVo);
			break;
		case "448":
			doZ448File(titaVo);
			break;
		case "450":
			doZ450File(titaVo);
			break;
		case "451":
			doZ451File(titaVo);
			break;
		case "454":
			doZ454File(titaVo);
			break;
		case "570":
			doZ570File(titaVo);
			break;
		case "571":
			doZ571File(titaVo);
			break;
		case "572":
			doZ572File(titaVo);
			break;
		case "573":
			doZ573File(titaVo);
			break;
		case "574":
			doZ574File(titaVo);
			break;
		case "575":
			doZ575File(titaVo);
			break;
		}
	}

	private void setFileCode() {
		switch (tranCode) {
		case "040":
			fileCode = "L8403-040";
			fileItem = "前置協商受理申請暨請求回報債權通知資料";
			break;
		case "041":
			fileCode = "L8403-041";
			fileItem = "協商開始暨停催通知資料";
			break;
		case "042":
			fileCode = "L8403-042";
			fileItem = "回報無擔保債權金額資料";
			break;
		case "043":
			fileCode = "L8403-043";
			fileItem = "回報有擔保債權金額資料";
			break;
		case "044":
			fileCode = "L8403-044";
			fileItem = "請求同意債務清償方案通知資料";
			break;
		case "045":
			fileCode = "L8403-045";
			fileItem = "回報是否同意債務清償方案資料";
			break;
		case "046":
			fileCode = "L8403-046";
			fileItem = "結案通知資料";
			break;
		case "047":
			fileCode = "L8403-047";
			fileItem = "金融機構無擔保債務協議資料";
			break;
		case "048":
			fileCode = "L8403-048";
			fileItem = "債務人基本資料";
			break;
		case "049":
			fileCode = "L8403-049";
			fileItem = "債務清償方案法院認可資料";
			break;
		case "050":
			fileCode = "L8403-050";
			fileItem = "債務人繳款資料";
			break;
		case "051":
			fileCode = "L8403-051";
			fileItem = "延期繳款（喘息期）資料";
			break;
		case "052":
			fileCode = "L8403-052";
			fileItem = "前置協商相關資料報送例外處理";
			break;
		case "053":
			fileCode = "L8403-053";
			fileItem = "同意報送例外處裡檔案";
			break;
		case "054":
			fileCode = "L8403-054";
			fileItem = "單獨全數受清償資料";
			break;
		case "055":
			fileCode = "L8403-055";
			fileItem = "更生案件通報資料";
			break;
		case "056":
			fileCode = "L8403-056";
			fileItem = "清算案件通報資料";
			break;
		case "060":
			fileCode = "L8403-060";
			fileItem = "前置協商受理變更還款條件申請暨請求回報剩餘債權通知資料";
			break;
		case "061":
			fileCode = "L8403-061";
			fileItem = "回報協商剩餘債權金額資料";
			break;
		case "062":
			fileCode = "L8403-062";
			fileItem = "金融機構無擔保債務變更還款條件協議資料";
			break;
		case "063":
			fileCode = "L8403-063";
			fileItem = "變更還款方案結案通知資料";
			break;
		case "440":
			fileCode = "L8403-440";
			fileItem = "前置調解受理申請暨請求回報債權通知資料";
			break;
		case "442":
			fileCode = "L8403-442";
			fileItem = "前置調解回報無擔保債權金額資料";
			break;
		case "443":
			fileCode = "L8403-443";
			fileItem = "前置調解回報有擔保債權金額資料";
			break;
		case "444":
			fileCode = "L8403-444";
			fileItem = "前置調解債務人基本資料";
			break;
		case "446":
			fileCode = "L8403-446";
			fileItem = "前置調解結案通知資料";
			break;
		case "447":
			fileCode = "L8403-447";
			fileItem = "前置調解金融機構無擔保債務協議資料";
			break;
		case "448":
			fileCode = "L8403-448";
			fileItem = "前置調解無擔保債務分配資料";
			break;
		case "450":
			fileCode = "L8403-450";
			fileItem = "前置調解債務人繳款資料";
			break;
		case "451":
			fileCode = "L8403-451";
			fileItem = "前置調解延期繳款資料";
			break;
		case "454":
			fileCode = "L8403-454";
			fileItem = "前置調解單獨全數受清償資料";
			break;
		case "570":
			fileCode = "L8403-570";
			fileItem = "受理更生款項統一收付通知資料";
			break;
		case "571":
			fileCode = "L8403-571";
			fileItem = "更生款項統一收付回報債權資料";
			break;
		case "572":
			fileCode = "L8403-572";
			fileItem = "更生款項統一收款及撥付款項分配表資料";
			break;
		case "573":
			fileCode = "L8403-573";
			fileItem = "更生債務人繳款資料";
			break;
		case "574":
			fileCode = "L8403-574";
			fileItem = "更生款項統一收付結案通知資料";
			break;
		case "575":
			fileCode = "L8403-575";
			fileItem = "債權金額異動通知資料";
			break;
		}
	}

	private void openAndPut() throws LogicException {
		tranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');
		date = Integer.valueOf(titaVo.getEntDy());
		brno = titaVo.getBrno();
		String iReportDate = titaVo.getParam("ReportDate");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iReportTime = titaVo.getParam("ReportTime");
		// 頭筆資料
		// JCIC-DAT-Z040-V01
		String iContactX = FormatUtil.padX("放款部聯絡人-邱怡婷", 80);
		headText = "JCIC-DAT-Z" + tranCode + "-V01-458     " + iReportDate + "01          02-23895858#7076" + iContactX;
		// 檔名
		// 金融機構總行代號+月份+日期+次數.檔案類別
		fileName = iSubmitKey + iReportDate.substring(3) + iReportTime + "." + tranCode;

		this.open(titaVo, date, brno, fileCode, fileItem, fileName, 2);
		this.put(headText);
	}

	private void totalCount() throws LogicException {
		int jcicdate = Integer.valueOf(titaVo.getParam("ReportDate"));
		switch (tranCode) {
		case "040":
			Slice<JcicZ040> xJcicZ040 = sJcicZ040Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ040.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ040 iJcicZ040 : xJcicZ040) {
				this.info("iJcicZ040 Count = " + iCount);
//				if (iJcicZ040.getOutJcicTxtDate() == jcicdate || iJcicZ040.getActualFilingDate() == jcicdate) {
				if (iJcicZ040.getOutJcicTxtDate() == jcicdate ) {
					iCount += 1;
				} 
			}

			break;
		case "041":
			Slice<JcicZ041> xJcicZ041 = sJcicZ041Service.findAll(0, Integer.MAX_VALUE, titaVo);
			this.info(" z041yu = " + xJcicZ041.getContent());
			if (xJcicZ041.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ041 iJcicZ041 : xJcicZ041) {
				if (iJcicZ041.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				}
//				else if (iJcicZ041.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "042":
			Slice<JcicZ042> xJcicZ042 = sJcicZ042Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ042.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ042 iJcicZ042 : xJcicZ042) {
				if (iJcicZ042.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ042.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "043":
			Slice<JcicZ043> xJcicZ043 = sJcicZ043Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ043.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ043 iJcicZ043 : xJcicZ043) {
				if (iJcicZ043.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ043.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "044":
			Slice<JcicZ044> xJcicZ044 = sJcicZ044Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ044.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ044 iJcicZ044 : xJcicZ044) {
				if (iJcicZ044.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ044.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "045":
			Slice<JcicZ045> xJcicZ045 = sJcicZ045Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ045.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ045 iJcicZ045 : xJcicZ045) {
				if (iJcicZ045.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ045.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "046":
			Slice<JcicZ046> xJcicZ046 = sJcicZ046Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ046.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ046 iJcicZ046 : xJcicZ046) {
				if (iJcicZ046.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ046.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "047":
			Slice<JcicZ047> xJcicZ047 = sJcicZ047Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ047.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ047 iJcicZ047 : xJcicZ047) {
				if (iJcicZ047.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ047.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "048":
			Slice<JcicZ048> xJcicZ048 = sJcicZ048Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ048.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ048 iJcicZ048 : xJcicZ048) {
				if (iJcicZ048.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ048.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "049":
			Slice<JcicZ049> xJcicZ049 = sJcicZ049Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ049.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ049 iJcicZ049 : xJcicZ049) {
				if (iJcicZ049.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ049.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "050":
			Slice<JcicZ050> xJcicZ050 = sJcicZ050Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ050.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ050 iJcicZ050 : xJcicZ050) {
				if (iJcicZ050.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ050.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "051":
			Slice<JcicZ051> xJcicZ051 = sJcicZ051Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ051.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ051 iJcicZ051 : xJcicZ051) {
				if (iJcicZ051.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ051.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "052":
			Slice<JcicZ052> xJcicZ052 = sJcicZ052Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ052.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ052 iJcicZ052 : xJcicZ052) {
				if (iJcicZ052.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ052.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "053":
			Slice<JcicZ053> xJcicZ053 = sJcicZ053Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ053.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ053 iJcicZ053 : xJcicZ053) {
				if (iJcicZ053.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ053.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "054":
			Slice<JcicZ054> xJcicZ054 = sJcicZ054Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ054.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ054 iJcicZ054 : xJcicZ054) {
				if (iJcicZ054.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ054.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "055":
			Slice<JcicZ055> xJcicZ055 = sJcicZ055Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ055.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ055 iJcicZ055 : xJcicZ055) {
				if (iJcicZ055.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ055.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "056":
			Slice<JcicZ056> xJcicZ056 = sJcicZ056Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ056.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ056 iJcicZ056 : xJcicZ056) {
				if (iJcicZ056.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ056.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "060":
			Slice<JcicZ060> xJcicZ060 = sJcicZ060Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ060.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ060 iJcicZ060 : xJcicZ060) {
				if (iJcicZ060.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ060.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "061":
			Slice<JcicZ061> xJcicZ061 = sJcicZ061Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ061.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ061 iJcicZ061 : xJcicZ061) {
				if (iJcicZ061.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ061.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "062":
			Slice<JcicZ062> xJcicZ062 = sJcicZ062Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ062.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ062 iJcicZ062 : xJcicZ062) {
				if (iJcicZ062.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ062.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "063":
			Slice<JcicZ063> xJcicZ063 = sJcicZ063Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ063.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ063 iJcicZ063 : xJcicZ063) {
				if (iJcicZ063.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ063.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "440":
			Slice<JcicZ440> xJcicZ440 = sJcicZ440Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ440.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ440 iJcicZ440 : xJcicZ440) {
				if (iJcicZ440.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				}
//				else if (iJcicZ440.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "442":
			Slice<JcicZ442> xJcicZ442 = sJcicZ442Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ442.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ442 iJcicZ442 : xJcicZ442) {
				if (iJcicZ442.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ442.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "443":
			Slice<JcicZ443> xJcicZ443 = sJcicZ443Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ443.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ443 iJcicZ443 : xJcicZ443) {
				if (iJcicZ443.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ443.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "444":
			Slice<JcicZ444> xJcicZ444 = sJcicZ444Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ444.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ444 iJcicZ444 : xJcicZ444) {
				if (iJcicZ444.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ444.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "446":
			Slice<JcicZ446> xJcicZ446 = sJcicZ446Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ446.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ446 iJcicZ446 : xJcicZ446) {
				if (iJcicZ446.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ446.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "447":
			Slice<JcicZ447> xJcicZ447 = sJcicZ447Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ447.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ447 iJcicZ447 : xJcicZ447) {
				if (iJcicZ447.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ447.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "448":
			Slice<JcicZ448> xJcicZ448 = sJcicZ448Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ448.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ448 iJcicZ448 : xJcicZ448) {
				if (iJcicZ448.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				}
//				else if (iJcicZ448.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "450":
			Slice<JcicZ450> xJcicZ450 = sJcicZ450Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ450.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ450 iJcicZ450 : xJcicZ450) {
				if (iJcicZ450.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ450.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "451":
			Slice<JcicZ451> xJcicZ451 = sJcicZ451Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ451.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ451 iJcicZ451 : xJcicZ451) {
				if (iJcicZ451.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ451.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "454":
			Slice<JcicZ454> xJcicZ454 = sJcicZ454Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ454.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ454 iJcicZ454 : xJcicZ454) {
				if (iJcicZ454.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ454.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "570":
			Slice<JcicZ570> xJcicZ570 = sJcicZ570Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ570.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ570 iJcicZ570 : xJcicZ570) {
				if (iJcicZ570.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ570.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "571":
			Slice<JcicZ571> xJcicZ571 = sJcicZ571Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ571.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ571 iJcicZ571 : xJcicZ571) {
				if (iJcicZ571.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ571.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "572":
			Slice<JcicZ572> xJcicZ572 = sJcicZ572Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ572.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ572 iJcicZ572 : xJcicZ572) {
				if (iJcicZ572.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ572.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "573":
			Slice<JcicZ573> xJcicZ573 = sJcicZ573Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ573.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ573 iJcicZ573 : xJcicZ573) {
				if (iJcicZ573.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ573.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "574":
			Slice<JcicZ574> xJcicZ574 = sJcicZ574Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ574.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ574 iJcicZ574 : xJcicZ574) {
				if (iJcicZ574.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ574.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		case "575":
			Slice<JcicZ575> xJcicZ575 = sJcicZ575Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xJcicZ575.getContent() == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for (JcicZ575 iJcicZ575 : xJcicZ575) {
				if (iJcicZ575.getOutJcicTxtDate() == jcicdate) {
					iCount += 1;
				} 
//				else if (iJcicZ575.getActualFilingDate() == jcicdate) {
//					iCount += 1;
//				}
			}
			break;
		}
	}

//jcic-040
	public void doZ040File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ040> rJcicZ040 = null;
		List<JcicZ040> zJcicZ040 = new ArrayList<JcicZ040>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		ixCustId = titaVo.getParam("CustId");
		ixSubmitKey = titaVo.get("SubmitKey");
		ixRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ040Id jcicZ040Id = new JcicZ040Id();
		jcicZ040Id.setCustId(iCustId);
		jcicZ040Id.setRcDate(iRcDate);
		jcicZ040Id.setSubmitKey(iSubmitKey);

		this.info("iCount   = " + iCount);
		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+ 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ040 = sJcicZ040Service.findkeyFilingDate(iActualFilingDate , iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ040 = rJcicZ040 == null ? null : rJcicZ040.getContent();
		if (rJcicZ040 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			// TODO:每個File都要改
		}

		this.info("zJcicZ040   = " + zJcicZ040);
		for (JcicZ040 sJcicZ040 : zJcicZ040) {
			if (sJcicZ040.getOutJcicTxtDate() == iJcicDate || sJcicZ040.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ040.getTranKey();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				String iSubmitKey = sJcicZ040.getSubmitKey();
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				String iCustId = sJcicZ040.getCustId();
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ040.getRcDate());
				String iRbDate = String.valueOf(sJcicZ040.getRbDate());
				int ixRbDate = Integer.valueOf(sJcicZ040.getRbDate());
				String iApplyType = sJcicZ040.getApplyType();
				iApplyType = FormatUtil.padX(iApplyType, 1);
				String iRefBankId = sJcicZ040.getRefBankId();
				iRefBankId = FormatUtil.padX(iRefBankId, 3);
				String iNotBankId1 = sJcicZ040.getNotBankId1();
				iNotBankId1 = FormatUtil.padX(iNotBankId1, 3);
				String iNotBankId2 = sJcicZ040.getNotBankId2();
				iNotBankId2 = FormatUtil.padX(iNotBankId2, 3);
				String iNotBankId3 = sJcicZ040.getNotBankId3();
				iNotBankId3 = FormatUtil.padX(iNotBankId3, 3);
				String iNotBankId4 = sJcicZ040.getNotBankId4();
				iNotBankId4 = FormatUtil.padX(iNotBankId4, 3);
				String iNotBankId5 = sJcicZ040.getNotBankId5();
				iNotBankId5 = FormatUtil.padX(iNotBankId5, 3);
				String iNotBankId6 = sJcicZ040.getNotBankId6();
				iNotBankId6 = FormatUtil.padX(iNotBankId6, 3);
				String iUkey = sJcicZ040.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "40" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iRbDate, 7, '0') + iApplyType + iRefBankId
						+ iNotBankId1 + iNotBankId2 + iNotBankId3 + iNotBankId4 + iNotBankId5 + iNotBankId6
						+ StringUtils.rightPad("", 23);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ040.setOutJcicTxtDate(sJcicZ040.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ040.setActualFilingDate(iDate);
					sJcicZ040.setActualFilingMark("Y");
				} else {
					sJcicZ040.setActualFilingDate(sJcicZ040.getActualFilingDate());
					sJcicZ040.setActualFilingMark("N");
				}
				try {
					sJcicZ040Service.update(sJcicZ040, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
				// 回填JcicDate後寫入Log檔
				JcicZ040Log iJcicZ040Log = new JcicZ040Log();
				JcicZ040LogId iJcicZ040LogId = new JcicZ040LogId();
				iJcicZ040LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ040LogId.setUkey(iUkey);
				iJcicZ040Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ040Log.setUkey(iUkey);
				iJcicZ040Log.setJcicZ040LogId(iJcicZ040LogId);
				iJcicZ040Log.setRbDate(ixRbDate);
				iJcicZ040Log.setApplyType(iApplyType);
				iJcicZ040Log.setRefBankId(iRefBankId);
				iJcicZ040Log.setNotBankId1(iNotBankId1);
				iJcicZ040Log.setNotBankId2(iNotBankId2);
				iJcicZ040Log.setNotBankId3(iNotBankId3);
				iJcicZ040Log.setNotBankId4(iNotBankId4);
				iJcicZ040Log.setNotBankId5(iNotBankId5);
				iJcicZ040Log.setNotBankId6(iNotBankId6);
				iJcicZ040Log.setTranKey(iTranKey);
				iJcicZ040Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ040LogService.insert(iJcicZ040Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z040)");
				}
				this.info("準備寫入L6932 ===== L8301(040)");
				JcicZ040Log uJcicZ040log = sJcicZ040LogService.ukeyFirst(iJcicZ040LogId.getUkey(), titaVo);
				this.info("uJcicZ040log      = " + uJcicZ040log);
				JcicZ040Log iJcicZ040Log2 = new JcicZ040Log();
				iJcicZ040Log2 = (JcicZ040Log) iDataLog.clone(iJcicZ040Log);
				this.info("iJcicZ040Log2     = " + iJcicZ040Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ040.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8403");
				iDataLog.setEnv(titaVo, iJcicZ040Log2, uJcicZ040log);
//				iDataLog.exec("L8403報送", iJcicZ040Log2.getUkey() + iJcicZ040Log2.getTxSeq());
				iDataLog.exec("L8403報送", iJcicZ040Log2.getUkey());
				// TODO:每個File都要加
//				iCount++;
			}
		}
	}

//jcic-041
	public void doZ041File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ041> rJcicZ041 = null;
		List<JcicZ041> zJcicZ041 = new ArrayList<JcicZ041>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		JcicZ041Id jcicZ041Id = new JcicZ041Id();
		jcicZ041Id.setCustId(iCustId);
		jcicZ041Id.setRcDate(iRcDate);
		jcicZ041Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+ 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ041 = sJcicZ041Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ041 = rJcicZ041 == null ? null : rJcicZ041.getContent();
		if (rJcicZ041 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ041   = " + zJcicZ041);
		for (JcicZ041 sJcicZ041 : zJcicZ041) {
			if (sJcicZ041.getOutJcicTxtDate() == iJcicDate || sJcicZ041.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ041.getTranKey();
				String iSubmitKey = sJcicZ041.getSubmitKey();
				String iCustId = sJcicZ041.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ041.getRcDate());
				String iScDate = String.valueOf(sJcicZ041.getScDate());
				String iNonFinClaimAmt = String.valueOf(sJcicZ041.getNonFinClaimAmt());
				String iNegoStartDate = String.valueOf(sJcicZ041.getNegoStartDate());
				int ixScDate = Integer.valueOf(sJcicZ041.getScDate());
				int ixNegoStartDate = Integer.valueOf(sJcicZ041.getNegoStartDate());
				int ixNonFinClaimAmt = Integer.valueOf(sJcicZ041.getNonFinClaimAmt());
				String iUkey = sJcicZ041.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "41" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iNegoStartDate, 7, '0')
						+ StringUtils.leftPad(iNonFinClaimAmt, 9, '0') + StringUtils.leftPad(iScDate, 7, '0')
						+ StringUtils.rightPad("", 29);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ041.setOutJcicTxtDate(sJcicZ041.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ041.setActualFilingDate(iDate);
					sJcicZ041.setActualFilingMark("Y");
				} else {
					sJcicZ041.setActualFilingDate(sJcicZ041.getActualFilingDate());
					sJcicZ041.setActualFilingMark("N");
				}
				try {
					sJcicZ041Service.update(sJcicZ041, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ041Log iJcicZ041Log = new JcicZ041Log();
				JcicZ041LogId iJcicZ041LogId = new JcicZ041LogId();
				iJcicZ041LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ041LogId.setUkey(iUkey);
				iJcicZ041Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ041Log.setUkey(iUkey);
				iJcicZ041Log.setJcicZ041LogId(iJcicZ041LogId);
				iJcicZ041Log.setScDate(ixScDate);
				iJcicZ041Log.setNegoStartDate(ixNegoStartDate);
				iJcicZ041Log.setNonFinClaimAmt(ixNonFinClaimAmt);
				iJcicZ041Log.setTranKey(iTranKey);
				iJcicZ041Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ041LogService.insert(iJcicZ041Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z041)");
				}
				this.info("準備寫入L6932 ===== L8302(041)");
				JcicZ041Log uJcicZ041log = sJcicZ041LogService.ukeyFirst(iJcicZ041LogId.getUkey(), titaVo);
				this.info("uJcicZ041log      = " + uJcicZ041log);
				JcicZ041Log iJcicZ041Log2 = new JcicZ041Log();
				iJcicZ041Log2 = (JcicZ041Log) iDataLog.clone(iJcicZ041Log);
				this.info("iJcicZ041Log2     = " + iJcicZ041Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ041.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				this.info("iCustNo   = " + iCustNo);
				titaVo.putParam("TXCODE", "L8404");
				titaVo.putParam("CustNo", iCustNo);
				iDataLog.setEnv(titaVo, iJcicZ041Log2, uJcicZ041log);
//				iDataLog.exec("L8404報送", iJcicZ041Log2.getUkey() + iJcicZ041Log2.getTxSeq());
				iDataLog.exec("L8404報送", iJcicZ041Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-042
	public void doZ042File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ042> rJcicZ042 = null;
		List<JcicZ042> zJcicZ042 = new ArrayList<JcicZ042>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		ixCustId = titaVo.getParam("CustId");
		ixSubmitKey = titaVo.get("SubmitKey");
		ixRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ042Id jcicZ042Id = new JcicZ042Id();
		jcicZ042Id.setCustId(iCustId);
		jcicZ042Id.setRcDate(iRcDate);
		jcicZ042Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ042 = sJcicZ042Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ042 = rJcicZ042 == null ? null : rJcicZ042.getContent();
		if (rJcicZ042 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ042   = " + zJcicZ042);
		for (JcicZ042 sJcicZ042 : zJcicZ042) {
			if (sJcicZ042.getOutJcicTxtDate() == iJcicDate || sJcicZ042.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ042.getTranKey();
				String iSubmitKey = sJcicZ042.getSubmitKey();
				String iCustId = sJcicZ042.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ042.getRcDate());
				String iMaxMainCode = sJcicZ042.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iIsClaims = sJcicZ042.getIsClaims();
				iIsClaims = FormatUtil.padX(iIsClaims, 1);
				String iGuarLoanCnt = String.valueOf(sJcicZ042.getGuarLoanCnt());
				int ixGuarLoanCnt = Integer.valueOf(sJcicZ042.getGuarLoanCnt());
				String iExpLoanAmt = String.valueOf(sJcicZ042.getExpLoanAmt());
				int ixExpLoanAmt = Integer.valueOf(sJcicZ042.getExpLoanAmt());
				String iCivil323ExpAmt = String.valueOf(sJcicZ042.getCivil323ExpAmt());
				int ixCivil323ExpAmt = Integer.valueOf(sJcicZ042.getCivil323ExpAmt());
				String iReceExpAmt = String.valueOf(sJcicZ042.getReceExpAmt());
				int ixReceExpAmt = Integer.valueOf(sJcicZ042.getReceExpAmt());
				String iCashCardAmt = String.valueOf(sJcicZ042.getCashCardAmt());
				int ixCashCardAmt = Integer.valueOf(sJcicZ042.getCashCardAmt());
				String iCivil323CashAmt = String.valueOf(sJcicZ042.getCivil323CashAmt());
				int ixCivil323CashAmt = Integer.valueOf(sJcicZ042.getCivil323CashAmt());
				String iReceCashAmt = String.valueOf(sJcicZ042.getReceCashAmt());
				int ixReceCashAmt = Integer.valueOf(sJcicZ042.getReceCashAmt());
				String iCreditCardAmt = String.valueOf(sJcicZ042.getCreditCardAmt());
				int ixCreditCardAmt = Integer.valueOf(sJcicZ042.getCreditCardAmt());
				String iCivil323CreditAmt = String.valueOf(sJcicZ042.getCivil323CreditAmt());
				int ixCivil323CreditAmt = Integer.valueOf(sJcicZ042.getCivil323CreditAmt());
				String iReceCreditAmt = String.valueOf(sJcicZ042.getReceCreditAmt());
				int ixReceCreditAmt = Integer.valueOf(sJcicZ042.getReceCreditAmt());
				String iReceExpPrin = String.valueOf(sJcicZ042.getReceExpPrin());
				int ixReceExpPrin = Integer.valueOf(sJcicZ042.getReceExpPrin());
				String iReceExpInte = String.valueOf(sJcicZ042.getReceExpInte());
				int ixReceExpInte = Integer.valueOf(sJcicZ042.getReceExpInte());
				String iReceExpPena = String.valueOf(sJcicZ042.getReceExpPena());
				int ixReceExpPena = Integer.valueOf(sJcicZ042.getReceExpPena());
				String iReceExpOther = String.valueOf(sJcicZ042.getReceExpOther());
				int ixReceExpOther = Integer.valueOf(sJcicZ042.getReceExpOther());
				String iCashCardPrin = String.valueOf(sJcicZ042.getCashCardPrin());
				int ixCashCardPrin = Integer.valueOf(sJcicZ042.getCashCardPrin());
				String iCashCardInte = String.valueOf(sJcicZ042.getCashCardInte());
				int ixCashCardInte = Integer.valueOf(sJcicZ042.getCashCardInte());
				String iCashCardPena = String.valueOf(sJcicZ042.getCashCardPena());
				int ixCashCardPena = Integer.valueOf(sJcicZ042.getCashCardPena());
				String iCashCardOther = String.valueOf(sJcicZ042.getCashCardOther());
				int ixCashCardOther = Integer.valueOf(sJcicZ042.getCashCardOther());
				String iCreditCardPrin = String.valueOf(sJcicZ042.getCreditCardPrin());
				int ixCreditCardPrin = Integer.valueOf(sJcicZ042.getCreditCardPrin());
				String iCreditCardInte = String.valueOf(sJcicZ042.getCreditCardInte());
				int ixCreditCardInte = Integer.valueOf(sJcicZ042.getCreditCardInte());
				String iCreditCardPena = String.valueOf(sJcicZ042.getCreditCardPena());
				int ixCreditCardPena = Integer.valueOf(sJcicZ042.getCreditCardPena());
				String iCreditCardOther = String.valueOf(sJcicZ042.getCreditCardOther());
				int ixCreditCardOther = Integer.valueOf(sJcicZ042.getCreditCardOther());
				String iUkey = sJcicZ042.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "42" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iMaxMainCode + iIsClaims
						+ StringUtils.leftPad(iGuarLoanCnt, 2, '0') + StringUtils.leftPad(iExpLoanAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323ExpAmt, 9, '0') + StringUtils.leftPad(iReceExpAmt, 9, '0')
						+ StringUtils.leftPad(iCashCardAmt, 9, '0') + StringUtils.leftPad(iCivil323CashAmt, 9, '0')
						+ StringUtils.leftPad(iReceCashAmt, 9, '0') + StringUtils.leftPad(iCreditCardAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323CreditAmt, 9, '0') + StringUtils.leftPad(iReceCreditAmt, 9, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iReceExpPrin, 9, '0')
						+ StringUtils.leftPad(iReceExpInte, 9, '0') + StringUtils.leftPad(iReceExpPena, 9, '0')
						+ StringUtils.leftPad(iReceExpOther, 9, '0') + StringUtils.leftPad(iCashCardPrin, 9, '0')
						+ StringUtils.leftPad(iCashCardInte, 9, '0') + StringUtils.leftPad(iCashCardPena, 9, '0')
						+ StringUtils.leftPad(iCashCardOther, 9, '0') + StringUtils.leftPad(iCreditCardPrin, 9, '0')
						+ StringUtils.leftPad(iCreditCardInte, 9, '0') + StringUtils.leftPad(iCreditCardPena, 9, '0')
						+ StringUtils.leftPad(iCreditCardOther, 9, '0') + StringUtils.rightPad("", 72);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ042.setOutJcicTxtDate(sJcicZ042.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ042.setActualFilingDate(iDate);
					sJcicZ042.setActualFilingMark("Y");
				} else {
					sJcicZ042.setActualFilingDate(sJcicZ042.getActualFilingDate());
					sJcicZ042.setActualFilingMark("N");
				}
				try {
					sJcicZ042Service.update(sJcicZ042, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ042Log iJcicZ042Log = new JcicZ042Log();
				JcicZ042LogId iJcicZ042LogId = new JcicZ042LogId();
				iJcicZ042LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ042LogId.setUkey(iUkey);
				iJcicZ042Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ042Log.setUkey(iUkey);
				iJcicZ042Log.setJcicZ042LogId(iJcicZ042LogId);
				iJcicZ042Log.setIsClaims(iIsClaims);
				iJcicZ042Log.setGuarLoanCnt(ixGuarLoanCnt);
				iJcicZ042Log.setExpLoanAmt(ixExpLoanAmt);
				iJcicZ042Log.setCivil323ExpAmt(ixCivil323ExpAmt);
				iJcicZ042Log.setReceExpAmt(ixReceExpAmt);
				iJcicZ042Log.setCashCardAmt(ixCashCardAmt);
				iJcicZ042Log.setCivil323CashAmt(ixCivil323CashAmt);
				iJcicZ042Log.setReceCashAmt(ixReceCashAmt);
				iJcicZ042Log.setCreditCardAmt(ixCreditCardAmt);
				iJcicZ042Log.setCivil323CreditAmt(ixCivil323CreditAmt);
				iJcicZ042Log.setReceCreditAmt(ixReceCreditAmt);
				iJcicZ042Log.setReceExpPrin(ixReceExpPrin);
				iJcicZ042Log.setReceExpInte(ixReceExpInte);
				iJcicZ042Log.setReceExpPena(ixReceExpPena);
				iJcicZ042Log.setReceExpOther(ixReceExpOther);
				iJcicZ042Log.setCashCardPrin(ixCashCardPrin);
				iJcicZ042Log.setCashCardInte(ixCashCardInte);
				iJcicZ042Log.setCashCardPena(ixCashCardPena);
				iJcicZ042Log.setCashCardOther(ixCashCardOther);
				iJcicZ042Log.setCreditCardPrin(ixCreditCardPrin);
				iJcicZ042Log.setCreditCardInte(ixCreditCardInte);
				iJcicZ042Log.setCreditCardPena(ixCreditCardPena);
				iJcicZ042Log.setCreditCardOther(ixCreditCardOther);
				iJcicZ042Log.setTranKey(iTranKey);
				iJcicZ042Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ042LogService.insert(iJcicZ042Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z042)");
				}
				this.info("準備寫入L6932 ===== L8303(042)");
				JcicZ042Log uJcicZ042log = sJcicZ042LogService.ukeyFirst(iJcicZ042LogId.getUkey(), titaVo);
				this.info("uJcicZ042log      = " + uJcicZ042log);
				JcicZ042Log iJcicZ042Log2 = new JcicZ042Log();
				iJcicZ042Log2 = (JcicZ042Log) iDataLog.clone(iJcicZ042Log);
				this.info("iJcicZ042Log2     = " + iJcicZ042Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ042.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8405");
				iDataLog.setEnv(titaVo, iJcicZ042Log2, uJcicZ042log);
//				iDataLog.exec("L8405報送", iJcicZ042Log2.getUkey() + iJcicZ042Log2.getTxSeq());
				iDataLog.exec("L8405報送", iJcicZ042Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-043
	public void doZ043File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ043> rJcicZ043 = null;
		List<JcicZ043> zJcicZ043 = new ArrayList<JcicZ043>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		ixCustId = titaVo.getParam("CustId");
		ixSubmitKey = titaVo.get("SubmitKey");
		ixRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ043Id jcicZ043Id = new JcicZ043Id();
		jcicZ043Id.setCustId(iCustId);
		jcicZ043Id.setRcDate(iRcDate);
		jcicZ043Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ043 = sJcicZ043Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ043 = rJcicZ043 == null ? null : rJcicZ043.getContent();
		if (rJcicZ043 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ043   = " + zJcicZ043);
		for (JcicZ043 sJcicZ043 : zJcicZ043) {
			if (sJcicZ043.getOutJcicTxtDate() == iJcicDate || sJcicZ043.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ043.getTranKey();
				String iSubmitKey = sJcicZ043.getSubmitKey();
				String iCustId = sJcicZ043.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ043.getRcDate());
				String iMaxMainCode = sJcicZ043.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iAccount = sJcicZ043.getAccount();
				iAccount = FormatUtil.padX(iAccount, 50);
				String iCollateralType = sJcicZ043.getCollateralType(); // 擔保品類別
				iCollateralType = FormatUtil.padX(iCollateralType, 2);
				String iOriginLoanAmt = String.valueOf(sJcicZ043.getOriginLoanAmt()); // 原借款金額
				String iCreditBalance = String.valueOf(sJcicZ043.getCreditBalance()); // 授信餘額
				String iPerPeriordAmt = String.valueOf(sJcicZ043.getPerPeriordAmt()); // 每期應付金額
				String iLastPayDate = String.valueOf(sJcicZ043.getLastPayDate()); // 最後繳息日
				String iLastPayAmt = String.valueOf(sJcicZ043.getLastPayAmt()); // 最後繳款金額
				String iOutstandAmt = String.valueOf(sJcicZ043.getOutstandAmt()); // 已到期尚未償還金額
				String iRepayPerMonday = String.valueOf(sJcicZ043.getRepayPerMonDay()); // 每月應還款日
//					String iContractStartYM = FormateYM(sJcicZ043.getContractStartYM()); // 契約起始年月									
//					String iContractEndYM = FormateYM(sJcicZ043.getContractEndYM()) ;// 契約截止年月
				// 2022/2/23檢查發現不需要用formateYM()此方法 Mata
				String iContractStartYM = String.valueOf(sJcicZ043.getContractStartYM()); // 契約起始年月
				String iContractEndYM = String.valueOf(sJcicZ043.getContractEndYM());// 契約截止年月
				BigDecimal ixOriginLoanAmt = new BigDecimal(iOriginLoanAmt);
				BigDecimal ixCreditBalance = new BigDecimal(iCreditBalance);
				BigDecimal ixPerPeriordAmt = new BigDecimal(iPerPeriordAmt);
				BigDecimal ixLastPayAmt = new BigDecimal(iLastPayAmt);
				int ixLastPayDate = Integer.valueOf(sJcicZ043.getLastPayDate());
				BigDecimal ixOutstandAmt = new BigDecimal(iOutstandAmt);
				int ixRepayPerMonDay = Integer.valueOf(sJcicZ043.getRepayPerMonDay());
				int ixContractStartYM = Integer.valueOf(sJcicZ043.getContractStartYM());
				int ixContractEndYM = Integer.valueOf(sJcicZ043.getContractEndYM());
				String iUkey = sJcicZ043.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "43" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iMaxMainCode + iAccount + iCollateralType
						+ StringUtils.leftPad(iOriginLoanAmt, 12, '0') + StringUtils.leftPad(iCreditBalance, 12, '0')
						+ StringUtils.leftPad(iPerPeriordAmt, 10, '0') + StringUtils.leftPad(iLastPayAmt, 10, '0')
						+ StringUtils.leftPad(iLastPayDate, 7, '0') + StringUtils.leftPad(iOutstandAmt, 10, '0')
						+ StringUtils.leftPad(iRepayPerMonday, 2, '0') + StringUtils.leftPad(iContractStartYM, 5, '0')
						+ StringUtils.leftPad(iContractEndYM, 5, '0') + StringUtils.rightPad("", 44);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ043.setOutJcicTxtDate(sJcicZ043.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ043.setActualFilingDate(iDate);
					sJcicZ043.setActualFilingMark("Y");
				} else {
					sJcicZ043.setActualFilingDate(sJcicZ043.getActualFilingDate());
					sJcicZ043.setActualFilingMark("N");
				}
				try {
					sJcicZ043Service.update(sJcicZ043, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ043Log iJcicZ043Log = new JcicZ043Log();
				JcicZ043LogId iJcicZ043LogId = new JcicZ043LogId();
				iJcicZ043LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ043LogId.setUkey(iUkey);
				iJcicZ043Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ043Log.setUkey(iUkey);
				iJcicZ043Log.setJcicZ043LogId(iJcicZ043LogId);
				iJcicZ043Log.setCollateralType(iCollateralType);
				iJcicZ043Log.setOriginLoanAmt(ixOriginLoanAmt);
				iJcicZ043Log.setCreditBalance(ixCreditBalance);
				iJcicZ043Log.setPerPeriordAmt(ixPerPeriordAmt);
				iJcicZ043Log.setLastPayAmt(ixLastPayAmt);
				iJcicZ043Log.setLastPayDate(ixLastPayDate);
				iJcicZ043Log.setOutstandAmt(ixOutstandAmt);
				iJcicZ043Log.setRepayPerMonDay(ixRepayPerMonDay);
				iJcicZ043Log.setContractStartYM(ixContractStartYM);
				iJcicZ043Log.setContractEndYM(ixContractEndYM);
				iJcicZ043Log.setTranKey(iTranKey);
				iJcicZ043Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ043LogService.insert(iJcicZ043Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z043)");
				}
				this.info("準備寫入L6932 ===== L8304(043)");
				JcicZ043Log uJcicZ043log = sJcicZ043LogService.ukeyFirst(iJcicZ043LogId.getUkey(), titaVo);
				this.info("uJcicZ043log      = " + uJcicZ043log);
				JcicZ043Log iJcicZ043Log2 = new JcicZ043Log();
				iJcicZ043Log2 = (JcicZ043Log) iDataLog.clone(iJcicZ043Log);
				this.info("iJcicZ043Log2     = " + iJcicZ043Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ043.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8406");
				iDataLog.setEnv(titaVo, iJcicZ043Log2, uJcicZ043log);
//				iDataLog.exec("L8406報送", iJcicZ043Log2.getUkey() + iJcicZ043Log2.getTxSeq());
				iDataLog.exec("L8406報送", iJcicZ043Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-044
	public void doZ044File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ044> rJcicZ044 = null;
		List<JcicZ044> zJcicZ044 = new ArrayList<JcicZ044>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		ixCustId = titaVo.getParam("CustId");
		ixSubmitKey = titaVo.get("SubmitKey");
		ixRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ044Id jcicZ044Id = new JcicZ044Id();
		jcicZ044Id.setCustId(iCustId);
		jcicZ044Id.setRcDate(iRcDate);
		jcicZ044Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+ 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ044 = sJcicZ044Service.findkeyFilingDate(iActualFilingDate , iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ044 = rJcicZ044 == null ? null : rJcicZ044.getContent();
		if (rJcicZ044 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ044   = " + zJcicZ044);
		for (JcicZ044 sJcicZ044 : zJcicZ044) {
			if (sJcicZ044.getOutJcicTxtDate() == iJcicDate || sJcicZ044.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ044.getTranKey();
				String iSubmitKey = sJcicZ044.getSubmitKey();
				String iCustId = sJcicZ044.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ044.getRcDate());
				String iDebtCode = sJcicZ044.getDebtCode();
				iDebtCode = FormatUtil.padX(iDebtCode, 2);
				String iNonGageAmt = String.valueOf(sJcicZ044.getNonGageAmt());
				int ixNonGageAmt = Integer.valueOf(sJcicZ044.getNonGageAmt());
				String iPeriod = String.valueOf(sJcicZ044.getPeriod());
				int ixPeriod = Integer.valueOf(sJcicZ044.getPeriod());
				String iRate = String.valueOf(sJcicZ044.getRate());
				BigDecimal ixRate = sJcicZ044.getRate();
				String iMonthPayAmt = String.valueOf(sJcicZ044.getMonthPayAmt());
				int ixMonthPayAmt = Integer.valueOf(sJcicZ044.getMonthPayAmt());
				String iReceYearIncome = String.valueOf(sJcicZ044.getReceYearIncome());
				int ixReceYearIncome = Integer.valueOf(sJcicZ044.getReceYearIncome());
				String iReceYear = FormateYear(sJcicZ044.getReceYear());
				int ixReceYear = Integer.valueOf(sJcicZ044.getReceYear());
				String iReceYear2Income = String.valueOf(sJcicZ044.getReceYear2Income());
				int ixReceYear2Income = Integer.valueOf(sJcicZ044.getReceYear2Income());
				String iReceYear2 = FormateYear(sJcicZ044.getReceYear2());
				int ixReceYear2 = Integer.valueOf(sJcicZ044.getReceYear2());
				String iCurrentMonthIncome = String.valueOf(sJcicZ044.getCurrentMonthIncome());
				int ixCurrentMonthIncome = Integer.valueOf(sJcicZ044.getCurrentMonthIncome());
				String iLivingCost = String.valueOf(sJcicZ044.getLivingCost());
				int ixLivingCost = Integer.valueOf(sJcicZ044.getLivingCost());
				String iCompName = sJcicZ044.getCompName();
				iCompName = FormatUtil.padX(iCompName, 40);
				String iCompId = sJcicZ044.getCompId();
				iCompId = FormatUtil.padX(iCompId, 8);
				String iCarCnt = String.valueOf(sJcicZ044.getCarCnt());
				int ixCarCnt = Integer.valueOf(sJcicZ044.getCarCnt());
				String iHouseCnt = String.valueOf(sJcicZ044.getHouseCnt());
				int ixHouseCnt = Integer.valueOf(sJcicZ044.getHouseCnt());
				String iLandCnt = String.valueOf(sJcicZ044.getLandCnt());
				int ixLandCnt = Integer.valueOf(sJcicZ044.getLandCnt());
				String iChildCnt = String.valueOf(sJcicZ044.getChildCnt());
				int ixChildCnt = Integer.valueOf(sJcicZ044.getChildCnt());
				String iChildRate = String.valueOf(sJcicZ044.getChildRate());
				BigDecimal ixChildRate = sJcicZ044.getChildRate();
				String iParentCnt = String.valueOf(sJcicZ044.getParentCnt());
				int ixParentCnt = Integer.valueOf(sJcicZ044.getParentCnt());
				String iParentRate = String.valueOf(sJcicZ044.getParentRate());
				BigDecimal ixParentRate = sJcicZ044.getParentRate();
				String iMouthCnt = String.valueOf(sJcicZ044.getMouthCnt());
				int ixMouthCnt = Integer.valueOf(sJcicZ044.getMouthCnt());
				String iMouthRate = String.valueOf(sJcicZ044.getMouthRate());
				BigDecimal ixMouthRate = sJcicZ044.getMouthRate();
				String iGradeType = sJcicZ044.getGradeType();
				String iPayLastAmt = String.valueOf(sJcicZ044.getPayLastAmt());
				int ixPayLastAmt = Integer.valueOf(sJcicZ044.getPayLastAmt());
				String iPeriod2 = String.valueOf(sJcicZ044.getPeriod2());
				String outPeriod2 = "";
				if (iPeriod2.equals("0")) {
					outPeriod2 = StringUtils.leftPad(outPeriod2, 3, " ");
				} else {
					outPeriod2 = StringUtils.leftPad(iPeriod2, 3, '0');
				}

				int ixPeriod2 = Integer.valueOf(sJcicZ044.getMouthCnt());
				String iRate2 = String.valueOf(sJcicZ044.getRate2());
				String outRate2 = "";
				if (iRate2.equals("0")) {
					outRate2 = StringUtils.leftPad(outRate2, 5, " ");
				} else {
					outRate2 = StringUtils.leftPad(FormatRate(iRate2, 2), 5, '0');
				}
				BigDecimal ixRate2 = sJcicZ044.getRate2();
				String iMonthPayAmt2 = String.valueOf(sJcicZ044.getMonthPayAmt2());
				int ixMonthPayAmt2 = Integer.valueOf(sJcicZ044.getMonthPayAmt2());
				String iPayLastAmt2 = String.valueOf(sJcicZ044.getPayLastAmt2());
				int ixPayLastAmt2 = Integer.valueOf(sJcicZ044.getPayLastAmt2());
				String iUkey = sJcicZ044.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "44" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iDebtCode + StringUtils.leftPad(iNonGageAmt, 9, '0')
						+ StringUtils.leftPad(iPeriod, 3, "0") + StringUtils.leftPad(FormatRate(iRate, 2), 5, '0')
						+ StringUtils.leftPad(iMonthPayAmt, 9, '0') + StringUtils.rightPad("", 10)
						+ StringUtils.leftPad(iReceYearIncome, 9, '0') + StringUtils.leftPad(iReceYear, 3, '0')
						+ StringUtils.leftPad(iReceYear2Income, 9, '0') + StringUtils.leftPad(iReceYear2, 3, '0')
						+ StringUtils.leftPad(iCurrentMonthIncome, 9, '0') + StringUtils.leftPad(iLivingCost, 9, '0')
						+ iCompName + iCompId + StringUtils.rightPad("", 10) + StringUtils.leftPad(iCarCnt, 2, '0')
						+ StringUtils.leftPad(iHouseCnt, 2, '0') + StringUtils.leftPad(iLandCnt, 2, '0')
						+ StringUtils.leftPad(iChildCnt, 2, '0')
						+ StringUtils.leftPad(FormatRate2(iChildRate, 1), 5, '0')
						+ StringUtils.leftPad(iParentCnt, 2, '0')
						+ StringUtils.leftPad(FormatRate2(iParentRate, 1), 5, '0')
						+ StringUtils.leftPad(iMouthCnt, 2, '0')
						+ StringUtils.leftPad(FormatRate2(iMouthRate, 1), 5, '0')
						+ StringUtils.rightPad(iGradeType, 1, "") + StringUtils.leftPad(iPayLastAmt, 9, '0')
						+ outPeriod2 + outRate2 + StringUtils.leftPad(iMonthPayAmt2, 9, '0')
						+ StringUtils.leftPad(iPayLastAmt2, 9, '0') + StringUtils.rightPad("", 21);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ044.setOutJcicTxtDate(sJcicZ044.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ044.setActualFilingDate(iDate);
					sJcicZ044.setActualFilingMark("Y");
				} else {
					sJcicZ044.setActualFilingDate(sJcicZ044.getActualFilingDate());
					sJcicZ044.setActualFilingMark("N");
				}
				try {
					sJcicZ044Service.update(sJcicZ044, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ044Log iJcicZ044Log = new JcicZ044Log();
				JcicZ044LogId iJcicZ044LogId = new JcicZ044LogId();
				iJcicZ044LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ044LogId.setUkey(iUkey);
				iJcicZ044Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ044Log.setUkey(iUkey);
				iJcicZ044Log.setJcicZ044LogId(iJcicZ044LogId);
				iJcicZ044Log.setDebtCode(iDebtCode);
				iJcicZ044Log.setNonGageAmt(ixNonGageAmt);// int
				iJcicZ044Log.setPeriod(ixPeriod);// int
				iJcicZ044Log.setRate(ixRate);// BigDecimal
				iJcicZ044Log.setMonthPayAmt(ixMonthPayAmt);// int
				iJcicZ044Log.setReceYearIncome(ixReceYearIncome);// int
				iJcicZ044Log.setReceYear(ixReceYear);// int
				iJcicZ044Log.setReceYear2Income(ixReceYear2Income);// int
				iJcicZ044Log.setReceYear2(ixReceYear2);// int
				iJcicZ044Log.setCurrentMonthIncome(ixCurrentMonthIncome);// int
				iJcicZ044Log.setLivingCost(ixLivingCost);// int
				iJcicZ044Log.setCompName(iCompName);
				iJcicZ044Log.setCompId(iCompId);
				iJcicZ044Log.setCarCnt(ixCarCnt);// int
				iJcicZ044Log.setHouseCnt(ixHouseCnt);// int
				iJcicZ044Log.setLandCnt(ixLandCnt);// int
				iJcicZ044Log.setChildCnt(ixChildCnt);// int
				iJcicZ044Log.setChildRate(ixChildRate);// int
				iJcicZ044Log.setParentCnt(ixParentCnt);// int
				iJcicZ044Log.setParentRate(ixParentRate);// BigDecimal
				iJcicZ044Log.setMouthCnt(ixMouthCnt);// int
				iJcicZ044Log.setMouthRate(ixMouthRate);// BigDecimal
				iJcicZ044Log.setGradeType(iGradeType);
				iJcicZ044Log.setPayLastAmt(ixPayLastAmt);// int
				iJcicZ044Log.setPeriod2(ixPeriod2);// int
				iJcicZ044Log.setRate2(ixRate2);// BigDecimal
				iJcicZ044Log.setMonthPayAmt2(ixMonthPayAmt2);// int
				iJcicZ044Log.setPayLastAmt2(ixPayLastAmt2);// int
				iJcicZ044Log.setTranKey(iTranKey);
				iJcicZ044Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ044LogService.insert(iJcicZ044Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z044)");
				}
				this.info("準備寫入L6932 ===== L8305(044)");
				JcicZ044Log uJcicZ044log = sJcicZ044LogService.ukeyFirst(iJcicZ044LogId.getUkey(), titaVo);
				this.info("uJcicZ044log      = " + uJcicZ044log);
				JcicZ044Log iJcicZ044Log2 = new JcicZ044Log();
				iJcicZ044Log2 = (JcicZ044Log) iDataLog.clone(iJcicZ044Log);
				this.info("iJcicZ044Log2     = " + iJcicZ044Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ044.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8407");
				iDataLog.setEnv(titaVo, iJcicZ044Log2, uJcicZ044log);
//				iDataLog.exec("L8407報送", iJcicZ044Log2.getUkey() + iJcicZ044Log2.getTxSeq());
				iDataLog.exec("L8407報送", iJcicZ044Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-045
	public void doZ045File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ045> rJcicZ045 = null;
		List<JcicZ045> zJcicZ045 = new ArrayList<JcicZ045>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ045Id jcicZ045Id = new JcicZ045Id();
		jcicZ045Id.setCustId(iCustId);
		jcicZ045Id.setRcDate(iRcDate);
		jcicZ045Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ045 = sJcicZ045Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ045 = rJcicZ045 == null ? null : rJcicZ045.getContent();
		if (rJcicZ045 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ045   = " + zJcicZ045);
		for (JcicZ045 sJcicZ045 : zJcicZ045) {
			if (sJcicZ045.getOutJcicTxtDate() == iJcicDate || sJcicZ045.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ045.getTranKey();
				String iSubmitKey = sJcicZ045.getSubmitKey();
				String iCustId = sJcicZ045.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ045.getRcDate());
				String iMaxMainCode = sJcicZ045.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iAgreeCode = sJcicZ045.getAgreeCode();
				iAgreeCode = FormatUtil.padX(iAgreeCode, 1);
				String iUkey = sJcicZ045.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "45" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iMaxMainCode + iAgreeCode + StringUtils.rightPad("", 48);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ045.setOutJcicTxtDate(sJcicZ045.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ045.setActualFilingDate(iDate);
					sJcicZ045.setActualFilingMark("Y");
				} else {
					sJcicZ045.setActualFilingDate(sJcicZ045.getActualFilingDate());
					sJcicZ045.setActualFilingMark("N");
				}
				try {
					sJcicZ045Service.update(sJcicZ045, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ045Log iJcicZ045Log = new JcicZ045Log();
				JcicZ045LogId iJcicZ045LogId = new JcicZ045LogId();
				iJcicZ045LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ045LogId.setUkey(iUkey);
				iJcicZ045Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ045Log.setUkey(iUkey);
				iJcicZ045Log.setJcicZ045LogId(iJcicZ045LogId);
				iJcicZ045Log.setAgreeCode(iAgreeCode);
				iJcicZ045Log.setTranKey(iTranKey);
				iJcicZ045Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ045LogService.insert(iJcicZ045Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z045)");
				}
				this.info("準備寫入L6932 ===== L8306(045)");
				JcicZ045Log uJcicZ045log = sJcicZ045LogService.ukeyFirst(iJcicZ045LogId.getUkey(), titaVo);
				this.info("uJcicZ045log      = " + uJcicZ045log);
				JcicZ045Log iJcicZ045Log2 = new JcicZ045Log();
				iJcicZ045Log2 = (JcicZ045Log) iDataLog.clone(iJcicZ045Log);
				this.info("iJcicZ045Log2     = " + iJcicZ045Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ045.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8408");
				iDataLog.setEnv(titaVo, iJcicZ045Log2, uJcicZ045log);
//				iDataLog.exec("L8408報送", iJcicZ045Log2.getUkey() + iJcicZ045Log2.getTxSeq());
				iDataLog.exec("L8408報送", iJcicZ045Log2.getUkey());
//				iCount++;
			}
		}
	}

//Jcic-046
	public void doZ046File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ046> rJcicZ046 = null;
		List<JcicZ046> zJcicZ046 = new ArrayList<JcicZ046>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ046Id jcicZ046Id = new JcicZ046Id();
		jcicZ046Id.setCustId(iCustId);
		jcicZ046Id.setRcDate(iRcDate);
		jcicZ046Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ046 = sJcicZ046Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ046 = rJcicZ046 == null ? null : rJcicZ046.getContent();
		if (rJcicZ046 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ046   = " + zJcicZ046);
		for (JcicZ046 sJcicZ046 : zJcicZ046) {
			if (sJcicZ046.getOutJcicTxtDate() == iJcicDate || sJcicZ046.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ046.getTranKey();
				String iSubmitKey = sJcicZ046.getSubmitKey();
				String iCustId = sJcicZ046.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ046.getRcDate());
				String iCloseCode = sJcicZ046.getCloseCode();
				iCloseCode = FormatUtil.padX(iCloseCode, 2);
				String ixCloseCode = sJcicZ046.getCloseCode();
				String iBreakCode = sJcicZ046.getBreakCode();

				String iCloseDate = String.valueOf(sJcicZ046.getCloseDate());
				String iUkey = sJcicZ046.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "46" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.leftPad(iBreakCode, 2, "") + StringUtils.rightPad("", 3) + iCloseCode
						+ StringUtils.leftPad(iCloseDate, 7, '0') + StringUtils.rightPad("", 43);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ046.setOutJcicTxtDate(sJcicZ046.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ046.setActualFilingDate(iDate);
					sJcicZ046.setActualFilingMark("Y");
				} else {
					sJcicZ046.setActualFilingDate(sJcicZ046.getActualFilingDate());
					sJcicZ046.setActualFilingMark("N");
				}
				try {
					sJcicZ046Service.update(sJcicZ046, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ046Log iJcicZ046Log = new JcicZ046Log();
				JcicZ046LogId iJcicZ046LogId = new JcicZ046LogId();
				iJcicZ046LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ046LogId.setUkey(iUkey);
				iJcicZ046Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ046Log.setUkey(iUkey);
				iJcicZ046Log.setJcicZ046LogId(iJcicZ046LogId);
				iJcicZ046Log.setCloseCode(ixCloseCode);
				iJcicZ046Log.setBreakCode(iBreakCode);
				iJcicZ046Log.setTranKey(iTranKey);
				iJcicZ046Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ046LogService.insert(iJcicZ046Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z046)");
				}
				this.info("準備寫入L6932 ===== L8307(046)");
				JcicZ046Log uJcicZ046log = sJcicZ046LogService.ukeyFirst(iJcicZ046LogId.getUkey(), titaVo);
				this.info("uJcicZ046log      = " + uJcicZ046log);
				JcicZ046Log iJcicZ046Log2 = new JcicZ046Log();
				iJcicZ046Log2 = (JcicZ046Log) iDataLog.clone(iJcicZ046Log);
				this.info("iJcicZ046Log2     = " + iJcicZ046Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ046.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8409");
				iDataLog.setEnv(titaVo, iJcicZ046Log2, uJcicZ046log);
//				iDataLog.exec("L8409報送", iJcicZ046Log2.getUkey() + iJcicZ046Log2.getTxSeq());
				iDataLog.exec("L8409報送", iJcicZ046Log2.getUkey());
//				iCount++;
			}
		}
	}

//Jcic-047
	public void doZ047File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ047> rJcicZ047 = null;
		List<JcicZ047> zJcicZ047 = new ArrayList<JcicZ047>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ047Id jcicZ047Id = new JcicZ047Id();
		jcicZ047Id.setCustId(iCustId);
		jcicZ047Id.setRcDate(iRcDate);
		jcicZ047Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ047 = sJcicZ047Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ047 = rJcicZ047 == null ? null : rJcicZ047.getContent();
		if (rJcicZ047 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ047   = " + zJcicZ047);
		for (JcicZ047 sJcicZ047 : zJcicZ047) {
			if (sJcicZ047.getOutJcicTxtDate() == iJcicDate || sJcicZ047.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ047.getTranKey();
				String iSubmitKey = sJcicZ047.getSubmitKey();
				String iCustId = sJcicZ047.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ047.getRcDate());
				String iPeriod = String.valueOf(sJcicZ047.getPeriod());
				String iRate = String.valueOf(sJcicZ047.getRate());
				String iCivil323ExpAmt = String.valueOf(sJcicZ047.getCivil323ExpAmt());
				int ixCivil323ExpAmt = Integer.valueOf(sJcicZ047.getCivil323ExpAmt());
				String iExpLoanAmt = String.valueOf(sJcicZ047.getExpLoanAmt());
				int ixExpLoanAmt = Integer.valueOf(sJcicZ047.getExpLoanAmt());
				String iCivil323CashAmt = String.valueOf(sJcicZ047.getCivil323CashAmt());
				int ixCivil323CashAmt = Integer.valueOf(sJcicZ047.getCivil323CashAmt());
				String iCashCardAmt = String.valueOf(sJcicZ047.getCashCardAmt());
				int ixCashCardAmt = Integer.valueOf(sJcicZ047.getCashCardAmt());
				String iCivil323CreditAmt = String.valueOf(sJcicZ047.getCivil323CreditAmt());
				int ixCivil323CreditAmt = Integer.valueOf(sJcicZ047.getCivil323CreditAmt());
				String iCreditCardAmt = String.valueOf(sJcicZ047.getCreditCardAmt());
				int ixCreditCardAmt = Integer.valueOf(sJcicZ047.getCreditCardAmt());
				String iCivil323Amt = String.valueOf(sJcicZ047.getCivil323Amt());
				BigDecimal ixCivil323Amt = sJcicZ047.getCivil323Amt();
				String iTotalAmt = String.valueOf(sJcicZ047.getTotalAmt());
				BigDecimal ixTotalAmt = sJcicZ047.getTotalAmt();
				String iPassDate = String.valueOf(sJcicZ047.getPassDate());
				int ixPassDate = Integer.valueOf(sJcicZ047.getPassDate());
				String iInterviewDate = String.valueOf(sJcicZ047.getInterviewDate());
				int ixInterviewDate = Integer.valueOf(sJcicZ047.getInterviewDate());
				String iSignDate = String.valueOf(sJcicZ047.getSignDate());
				int ixSignDate = Integer.valueOf(sJcicZ047.getSignDate());
				String iLimitDate = String.valueOf(sJcicZ047.getLimitDate());
				int ixLimitDate = Integer.valueOf(sJcicZ047.getLimitDate());
				String iFirstPayDate = String.valueOf(sJcicZ047.getFirstPayDate());
				int ixFirstPayDate = Integer.valueOf(sJcicZ047.getFirstPayDate());
				String iMonthPayAmt = String.valueOf(sJcicZ047.getMonthPayAmt());
				int ixMonthPayAmt = Integer.valueOf(sJcicZ047.getMonthPayAmt());
				String iPayAccount = String.valueOf(sJcicZ047.getPayAccount());

				String iPostAddr = String.valueOf(sJcicZ047.getPostAddr());
				String nPostAddr = FormatUtil.padX(iPostAddr, 76);
				String iGradeType = String.valueOf(sJcicZ047.getGradeType());
				String iPayLastAmt = String.valueOf(sJcicZ047.getPayLastAmt());
				int ixPayLastAmt = Integer.valueOf(sJcicZ047.getPayLastAmt());
				String iPeriod2 = String.valueOf(sJcicZ047.getPeriod2());
				String outPeriod2 = "";
				if (iPeriod2.equals("0")) {
					outPeriod2 = StringUtils.leftPad(outPeriod2, 3, " ");
				} else {
					outPeriod2 = StringUtils.leftPad(iPeriod2, 3, '0');
				}
				int ixPeriod2 = Integer.valueOf(sJcicZ047.getPayLastAmt());
				String iRate2 = String.valueOf(sJcicZ047.getRate2());
				String outRate2 = "";
				if (iRate2.equals("0")) {
					outRate2 = StringUtils.leftPad(outRate2, 5, " ");
				} else {
					outRate2 = StringUtils.leftPad(FormatRate(iRate2, 2), 5, '0');
				}
				BigDecimal ixRate2 = sJcicZ047.getRate2();
				String iMonthPayAmt2 = String.valueOf(sJcicZ047.getMonthPayAmt2());
				int ixMonthPayAmt2 = Integer.valueOf(sJcicZ047.getMonthPayAmt2());
				String iPayLastAmt2 = String.valueOf(sJcicZ047.getPayLastAmt2());
				int ixPayLastAmt2 = Integer.valueOf(sJcicZ047.getPayLastAmt2());
				/** 新增如果面試日期沒有輸入會帶0近來，但是在印出時應該顯示為空白 */
				String iInterviewDate2 = "";
				if (iInterviewDate.equals("0")) {
					iInterviewDate2 = " ";
				} else {
					iInterviewDate2 = iInterviewDate;
				}
				/** 新增如果簽約日期沒有輸入會帶0近來，但是在印出時應該顯示為空白 */
				String iSignDate2 = "";
				if (iSignDate.equals("0")) {
					iSignDate2 = " ";
				} else {
					iSignDate2 = iSignDate;
				}

				String iUkey = sJcicZ047.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "47" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iPeriod, 3, '0')
						+ StringUtils.leftPad(FormatRate(iRate, 2), 5, '0')
						+ StringUtils.leftPad(iCivil323ExpAmt, 9, '0') + StringUtils.leftPad(iExpLoanAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323CashAmt, 9, '0') + StringUtils.leftPad(iCashCardAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323CreditAmt, 9, '0') + StringUtils.leftPad(iCreditCardAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323Amt, 10, '0') + StringUtils.leftPad(iTotalAmt, 10, '0')
						+ StringUtils.leftPad(iPassDate, 7, '0') + StringUtils.leftPad(iInterviewDate2, 7, "")
						+ StringUtils.leftPad(iSignDate2, 7, "") + StringUtils.leftPad(iLimitDate, 7, '0')
						+ StringUtils.leftPad(iFirstPayDate, 7, '0') + StringUtils.leftPad(iMonthPayAmt, 9, '0')
						+ StringUtils.rightPad(iPayAccount, 20, "") + nPostAddr
						+ StringUtils.rightPad(iGradeType, 1, "") + StringUtils.leftPad(iPayLastAmt, 9, '0')
						+ outPeriod2 + outRate2 + StringUtils.leftPad(iMonthPayAmt2, 9, '0')
						+ StringUtils.leftPad(iPayLastAmt2, 9, '0') + StringUtils.rightPad("", 14);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ047.setOutJcicTxtDate(sJcicZ047.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ047.setActualFilingDate(iDate);
					sJcicZ047.setActualFilingMark("Y");
				} else {
					sJcicZ047.setActualFilingDate(sJcicZ047.getActualFilingDate());
					sJcicZ047.setActualFilingMark("N");
				}
				try {
					sJcicZ047Service.update(sJcicZ047, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ047Log iJcicZ047Log = new JcicZ047Log();
				JcicZ047LogId iJcicZ047LogId = new JcicZ047LogId();
				iJcicZ047LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ047LogId.setUkey(iUkey);
				iJcicZ047Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ047Log.setUkey(iUkey);
				iJcicZ047Log.setJcicZ047LogId(iJcicZ047LogId);
				iJcicZ047Log.setCivil323ExpAmt(ixCivil323ExpAmt);
				iJcicZ047Log.setExpLoanAmt(ixExpLoanAmt);
				iJcicZ047Log.setCivil323CashAmt(ixCivil323CashAmt);
				iJcicZ047Log.setCashCardAmt(ixCashCardAmt);
				iJcicZ047Log.setCivil323CreditAmt(ixCivil323CreditAmt);
				iJcicZ047Log.setCreditCardAmt(ixCreditCardAmt);
				iJcicZ047Log.setCivil323Amt(ixCivil323Amt);
				iJcicZ047Log.setTotalAmt(ixTotalAmt);
				iJcicZ047Log.setPassDate(ixPassDate);
				iJcicZ047Log.setInterviewDate(ixInterviewDate);
				iJcicZ047Log.setSignDate(ixSignDate);
				iJcicZ047Log.setLimitDate(ixLimitDate);
				iJcicZ047Log.setFirstPayDate(ixFirstPayDate);
				iJcicZ047Log.setMonthPayAmt(ixMonthPayAmt);
				iJcicZ047Log.setPayAccount(iPayAccount);
				iJcicZ047Log.setPostAddr(iPostAddr);
				iJcicZ047Log.setGradeType(iGradeType);
				iJcicZ047Log.setPayLastAmt(ixPayLastAmt);
				iJcicZ047Log.setPeriod2(ixPeriod2);
				iJcicZ047Log.setRate2(ixRate2);
				iJcicZ047Log.setMonthPayAmt2(ixMonthPayAmt2);
				iJcicZ047Log.setPayLastAmt2(ixPayLastAmt2);
				iJcicZ047Log.setTranKey(iTranKey);
				iJcicZ047Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ047LogService.insert(iJcicZ047Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z047)");
				}
				this.info("準備寫入L6932 ===== L8308(047)");
				JcicZ047Log uJcicZ047log = sJcicZ047LogService.ukeyFirst(iJcicZ047LogId.getUkey(), titaVo);
				this.info("uJcicZ047log      = " + uJcicZ047log);
				JcicZ047Log iJcicZ047Log2 = new JcicZ047Log();
				iJcicZ047Log2 = (JcicZ047Log) iDataLog.clone(iJcicZ047Log);
				this.info("iJcicZ047Log2     = " + iJcicZ047Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ047.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8410");
				iDataLog.setEnv(titaVo, iJcicZ047Log2, uJcicZ047log);
//				iDataLog.exec("L8410報送", iJcicZ047Log2.getUkey() + iJcicZ047Log2.getTxSeq());
				iDataLog.exec("L8410報送", iJcicZ047Log2.getUkey());
//				iCount++;
			}
		}
	}

//Jcic-048
	public void doZ048File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ048> rJcicZ048 = null;
		List<JcicZ048> zJcicZ048 = new ArrayList<JcicZ048>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ048Id jcicZ048Id = new JcicZ048Id();
		jcicZ048Id.setCustId(iCustId);
		jcicZ048Id.setRcDate(iRcDate);
		jcicZ048Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ048 = sJcicZ048Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ048 = rJcicZ048 == null ? null : rJcicZ048.getContent();
		if (rJcicZ048 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ048   = " + zJcicZ048);
		for (JcicZ048 sJcicZ048 : zJcicZ048) {
			if (sJcicZ048.getOutJcicTxtDate() == iJcicDate || sJcicZ048.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ048.getTranKey();
				String iSubmitKey = sJcicZ048.getSubmitKey();
				String iCustId = sJcicZ048.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ048.getRcDate());
				String iCustRegAddr = sJcicZ048.getCustRegAddr();
				iCustRegAddr = FormatUtil.padX(iCustRegAddr, 76);
				String iCustComAddr = sJcicZ048.getCustComAddr();
				iCustComAddr = FormatUtil.padX(iCustComAddr, 76);
				String iCustRegTelNo = sJcicZ048.getCustRegTelNo();
				String iCustComTelNo = sJcicZ048.getCustComTelNo();
				String iCustMobilNo = sJcicZ048.getCustMobilNo();
				String iUkey = sJcicZ048.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "48" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iCustRegAddr + iCustComAddr
						+ StringUtils.rightPad(iCustRegTelNo, 16, "") + StringUtils.rightPad(iCustComTelNo, 16, "")
						+ StringUtils.rightPad(iCustMobilNo, 16, "") + StringUtils.rightPad("", 32);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ048.setOutJcicTxtDate(sJcicZ048.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ048.setActualFilingDate(iDate);
					sJcicZ048.setActualFilingMark("Y");
				} else {
					sJcicZ048.setActualFilingDate(sJcicZ048.getActualFilingDate());
					sJcicZ048.setActualFilingMark("N");
				}
				try {
					sJcicZ048Service.update(sJcicZ048, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ048Log iJcicZ048Log = new JcicZ048Log();
				JcicZ048LogId iJcicZ048LogId = new JcicZ048LogId();
				iJcicZ048LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ048LogId.setUkey(iUkey);
				iJcicZ048Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ048Log.setUkey(iUkey);
				iJcicZ048Log.setJcicZ048LogId(iJcicZ048LogId);
				iJcicZ048Log.setCustRegAddr(iCustRegAddr);
				iJcicZ048Log.setCustComAddr(iCustComAddr);
				iJcicZ048Log.setCustRegTelNo(iCustRegTelNo);
				iJcicZ048Log.setCustComTelNo(iCustComTelNo);
				iJcicZ048Log.setCustMobilNo(iCustMobilNo);
				iJcicZ048Log.setTranKey(iTranKey);
				iJcicZ048Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ048LogService.insert(iJcicZ048Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z048)");
				}
				this.info("準備寫入L6932 ===== L8309(048)");
				JcicZ048Log uJcicZ048log = sJcicZ048LogService.ukeyFirst(iJcicZ048LogId.getUkey(), titaVo);
				this.info("uJcicZ048log      = " + uJcicZ048log);
				JcicZ048Log iJcicZ048Log2 = new JcicZ048Log();
				iJcicZ048Log2 = (JcicZ048Log) iDataLog.clone(iJcicZ048Log);
				this.info("iJcicZ048Log2     = " + iJcicZ048Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ048.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8411");
				iDataLog.setEnv(titaVo, iJcicZ048Log2, uJcicZ048log);
//				iDataLog.exec("L8411報送", iJcicZ048Log2.getUkey() + iJcicZ048Log2.getTxSeq());
				iDataLog.exec("L8411報送", iJcicZ048Log2.getUkey());
//				iCount++;
			}
		}
	}

//Jcic-049
	public void doZ049File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ049> rJcicZ049 = null;
		List<JcicZ049> zJcicZ049 = new ArrayList<JcicZ049>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ049Id jcicZ049Id = new JcicZ049Id();
		jcicZ049Id.setCustId(iCustId);
		jcicZ049Id.setRcDate(iRcDate);
		jcicZ049Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ049 = sJcicZ049Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ049 = rJcicZ049 == null ? null : rJcicZ049.getContent();
		if (rJcicZ049 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ049   = " + zJcicZ049);
		for (JcicZ049 sJcicZ049 : zJcicZ049) {
			if (sJcicZ049.getOutJcicTxtDate() == iJcicDate || sJcicZ049.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ049.getTranKey();
				String iSubmitKey = sJcicZ049.getSubmitKey();
				String iCustId = sJcicZ049.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ049.getRcDate());
				String iClaimStatus = String.valueOf(sJcicZ049.getClaimStatus());
				int ixClaimStatus = Integer.valueOf(sJcicZ049.getClaimStatus());
				String iApplyDate = String.valueOf(sJcicZ049.getApplyDate());
				int ixApplyDate = Integer.valueOf(sJcicZ049.getApplyDate());
				String iCourtCode = sJcicZ049.getCourtCode();
				iCourtCode = FormatUtil.padX(iCourtCode, 3);
				String iYear = String.valueOf(sJcicZ049.getYear() - 1911);
				int ixYear = Integer.valueOf(sJcicZ049.getYear());
				String iCourtDiv = sJcicZ049.getCourtDiv();
				iCourtDiv = FormatUtil.padX(iCourtDiv, 4);
				String iCourtCaseNo = sJcicZ049.getCourtCaseNo();
				iCourtCaseNo = FormatUtil.padX(iCourtCaseNo, 20);
				String iApprove = sJcicZ049.getApprove();
				String iClaimDate = String.valueOf(sJcicZ049.getClaimDate());
				int ixClaimDate = Integer.valueOf(sJcicZ049.getClaimDate());

				this.info("年度別資料" + iYear);
				this.info("年度別資料2" + ixYear);

				/** 新增如果年度別沒有輸入會帶0近來，但是在印出時應該顯示為空白 */
				String iYear2 = "";
				if (iYear.equals("0")) {
					iYear2 = " ";
				} else {
					iYear2 = iYear;
				}
				this.info("年度別資料3" + iYear2);

				String iUkey = sJcicZ049.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "49" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.rightPad(iClaimStatus, 1, "")
						+ StringUtils.leftPad(iApplyDate, 7, '0') + iCourtCode + StringUtils.leftPad(iYear2, 3, "")
						+ iCourtDiv + iCourtCaseNo + StringUtils.rightPad(iApprove, 1, "")
						+ StringUtils.leftPad(iClaimDate, 7, '0') + StringUtils.rightPad("", 46);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ049.setOutJcicTxtDate(sJcicZ049.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ049.setActualFilingDate(iDate);
					sJcicZ049.setActualFilingMark("Y");
				} else {
					sJcicZ049.setActualFilingDate(sJcicZ049.getActualFilingDate());
					sJcicZ049.setActualFilingMark("N");
				}
				try {
					sJcicZ049Service.update(sJcicZ049, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ049Log iJcicZ049Log = new JcicZ049Log();
				JcicZ049LogId iJcicZ049LogId = new JcicZ049LogId();
				iJcicZ049LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ049LogId.setUkey(iUkey);
				iJcicZ049Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ049Log.setUkey(iUkey);
				iJcicZ049Log.setJcicZ049LogId(iJcicZ049LogId);
				iJcicZ049Log.setClaimStatus(ixClaimStatus);
				iJcicZ049Log.setApplyDate(ixApplyDate);
				iJcicZ049Log.setCourtCode(iCourtCode);
				iJcicZ049Log.setYear(ixYear);
				iJcicZ049Log.setCourtDiv(iCourtDiv);
				iJcicZ049Log.setCourtCaseNo(iCourtCaseNo);
				iJcicZ049Log.setApprove(iApprove);
				iJcicZ049Log.setClaimDate(ixClaimDate);
				iJcicZ049Log.setTranKey(iTranKey);
				iJcicZ049Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ049LogService.insert(iJcicZ049Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z049)");
				}
				this.info("準備寫入L6932 ===== L8310(049)");
				JcicZ049Log uJcicZ049log = sJcicZ049LogService.ukeyFirst(iJcicZ049LogId.getUkey(), titaVo);
				this.info("uJcicZ049log      = " + uJcicZ049log);
				JcicZ049Log iJcicZ049Log2 = new JcicZ049Log();
				iJcicZ049Log2 = (JcicZ049Log) iDataLog.clone(iJcicZ049Log);
				this.info("iJcicZ049Log2     = " + iJcicZ049Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ049.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8412");
				iDataLog.setEnv(titaVo, iJcicZ049Log2, uJcicZ049log);
//				iDataLog.exec("L8412報送", iJcicZ049Log2.getUkey() + iJcicZ049Log2.getTxSeq());
				iDataLog.exec("L8412報送", iJcicZ049Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-050
	public void doZ050File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ050> rJcicZ050 = null;
		List<JcicZ050> zJcicZ050 = new ArrayList<JcicZ050>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ050Id jcicZ050Id = new JcicZ050Id();
		jcicZ050Id.setCustId(iCustId);
		jcicZ050Id.setRcDate(iRcDate);
		jcicZ050Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ050 = sJcicZ050Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ050 = rJcicZ050 == null ? null : rJcicZ050.getContent();
		if (rJcicZ050 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ050   = " + zJcicZ050);
		for (JcicZ050 sJcicZ050 : zJcicZ050) {
			if (sJcicZ050.getOutJcicTxtDate() == iJcicDate || sJcicZ050.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ050.getTranKey();
				String iSubmitKey = sJcicZ050.getSubmitKey();
				String iCustId = sJcicZ050.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ050.getRcDate());
				String iPayDate = String.valueOf(sJcicZ050.getPayDate());
				String iPayAmt = String.valueOf(sJcicZ050.getPayAmt());
				String iSumRepayActualAmt = String.valueOf(sJcicZ050.getSumRepayActualAmt());
				String iSumRepayShouldAmt = String.valueOf(sJcicZ050.getSumRepayShouldAmt());
				String iStatus = sJcicZ050.getStatus();
//					String iSecondRepayYM = FormateYM(sJcicZ050.getSecondRepayYM());
				int ixSecondRepayYM = Integer.valueOf(sJcicZ050.getSecondRepayYM());
				int ixPayAmt = Integer.valueOf(sJcicZ050.getPayAmt());
				int ixSumRepayActualAmt = Integer.valueOf(sJcicZ050.getSumRepayActualAmt());
				int ixSumRepayShouldAmt = Integer.valueOf(sJcicZ050.getSumRepayShouldAmt());
				String iUkey = sJcicZ050.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 有包含SecondRepayYM
//					String text = "50" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0') + StringUtils.rightPad("", 5)
//							+ StringUtils.leftPad(iPayDate, 7, '0') + StringUtils.leftPad(iPayAmt, 9, '0') + StringUtils.leftPad(iSumRepayActualAmt, 9, '0')
//							+ StringUtils.leftPad(iSumRepayShouldAmt, 9, '0') + StringUtils.rightPad(iStatus, 1, "") + StringUtils.leftPad(iSecondRepayYM, 5, '0') + StringUtils.rightPad("", 32);
				// 無包含SecondRepayYM與as400一致
				String text = "50" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iPayDate, 7, '0')
						+ StringUtils.leftPad(iPayAmt, 9, '0') + StringUtils.leftPad(iSumRepayActualAmt, 9, '0')
						+ StringUtils.leftPad(iSumRepayShouldAmt, 9, '0') + StringUtils.rightPad(iStatus, 1, "")
						+ StringUtils.rightPad("0", 4, '0') + StringUtils.rightPad("", 33);

				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ050.setOutJcicTxtDate(sJcicZ050.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ050.setActualFilingDate(iDate);
					sJcicZ050.setActualFilingMark("Y");
				} else {
					sJcicZ050.setActualFilingDate(sJcicZ050.getActualFilingDate());
					sJcicZ050.setActualFilingMark("N");
				}
				try {
					sJcicZ050Service.update(sJcicZ050, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ050Log iJcicZ050Log = new JcicZ050Log();
				JcicZ050LogId iJcicZ050LogId = new JcicZ050LogId();
				iJcicZ050LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ050LogId.setUkey(iUkey);
				iJcicZ050Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ050Log.setUkey(iUkey);
				iJcicZ050Log.setJcicZ050LogId(iJcicZ050LogId);
				iJcicZ050Log.setPayAmt(ixPayAmt);
				iJcicZ050Log.setSumRepayActualAmt(ixSumRepayActualAmt);
				iJcicZ050Log.setSumRepayShouldAmt(ixSumRepayShouldAmt);
				iJcicZ050Log.setStatus(iStatus);
				iJcicZ050Log.setSecondRepayYM(ixSecondRepayYM);
				iJcicZ050Log.setTranKey(iTranKey);
				iJcicZ050Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ050LogService.insert(iJcicZ050Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z050)");
				}
				this.info("準備寫入L6932 ===== L8311(050)");
				JcicZ050Log uJcicZ050log = sJcicZ050LogService.ukeyFirst(iJcicZ050LogId.getUkey(), titaVo);
				this.info("uJcicZ050log      = " + uJcicZ050log);
				JcicZ050Log iJcicZ050Log2 = new JcicZ050Log();
				iJcicZ050Log2 = (JcicZ050Log) iDataLog.clone(iJcicZ050Log);
				this.info("iJcicZ050Log2     = " + iJcicZ050Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ050.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8413");
				iDataLog.setEnv(titaVo, iJcicZ050Log2, uJcicZ050log);
//				iDataLog.exec("L8413報送", iJcicZ050Log2.getUkey() + iJcicZ050Log2.getTxSeq());
				iDataLog.exec("L8413報送", iJcicZ050Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-051
	public void doZ051File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ051> rJcicZ051 = null;
		List<JcicZ051> zJcicZ051 = new ArrayList<JcicZ051>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ051Id jcicZ051Id = new JcicZ051Id();
		jcicZ051Id.setCustId(iCustId);
		jcicZ051Id.setRcDate(iRcDate);
		jcicZ051Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ051 = sJcicZ051Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ051 = rJcicZ051 == null ? null : rJcicZ051.getContent();
		if (rJcicZ051 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ051   = " + zJcicZ051);
		for (JcicZ051 sJcicZ051 : zJcicZ051) {
			if (sJcicZ051.getOutJcicTxtDate() == iJcicDate || sJcicZ051.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ051.getTranKey();
				String iSubmitKey = sJcicZ051.getSubmitKey();
				String iCustId = sJcicZ051.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ051.getRcDate());
				String iDelayCode = sJcicZ051.getDelayCode();
				String iDelayYM = FormateYM(sJcicZ051.getDelayYM());
				String iDelayDesc = sJcicZ051.getDelayDesc();
				String nDelayDesc = FormatUtil.padX(iDelayDesc, 40);
				String iUkey = sJcicZ051.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "51" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.rightPad(iDelayCode, 1, "")
						+ StringUtils.leftPad(iDelayYM, 5, '0') + nDelayDesc + StringUtils.rightPad("", 46);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ051.setOutJcicTxtDate(sJcicZ051.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ051.setActualFilingDate(iDate);
					sJcicZ051.setActualFilingMark("Y");
				} else {
					sJcicZ051.setActualFilingDate(sJcicZ051.getActualFilingDate());
					sJcicZ051.setActualFilingMark("N");
				}
				try {
					sJcicZ051Service.update(sJcicZ051, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ051Log iJcicZ051Log = new JcicZ051Log();
				JcicZ051LogId iJcicZ051LogId = new JcicZ051LogId();
				iJcicZ051LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ051LogId.setUkey(iUkey);
				iJcicZ051Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ051Log.setUkey(iUkey);
				iJcicZ051Log.setJcicZ051LogId(iJcicZ051LogId);
				iJcicZ051Log.setDelayCode(iDelayCode);
				iJcicZ051Log.setDelayDesc(iDelayDesc);
				iJcicZ051Log.setTranKey(iTranKey);
				iJcicZ051Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ051LogService.insert(iJcicZ051Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z051)");
				}
				this.info("準備寫入L6932 ===== L8312(051)");
				JcicZ051Log uJcicZ051log = sJcicZ051LogService.ukeyFirst(iJcicZ051LogId.getUkey(), titaVo);
				this.info("uJcicZ051log      = " + uJcicZ051log);
				JcicZ051Log iJcicZ051Log2 = new JcicZ051Log();
				iJcicZ051Log2 = (JcicZ051Log) iDataLog.clone(iJcicZ051Log);
				this.info("iJcicZ051Log2     = " + iJcicZ051Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ051.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8414");
				iDataLog.setEnv(titaVo, iJcicZ051Log2, uJcicZ051log);
//				iDataLog.exec("L8414報送", iJcicZ051Log2.getUkey() + iJcicZ051Log2.getTxSeq());
				iDataLog.exec("L8414報送", iJcicZ051Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-052
	public void doZ052File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ052> rJcicZ052 = null;
		List<JcicZ052> zJcicZ052 = new ArrayList<JcicZ052>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ052Id jcicZ052Id = new JcicZ052Id();
		jcicZ052Id.setCustId(iCustId);
		jcicZ052Id.setRcDate(iRcDate);
		jcicZ052Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ052 = sJcicZ052Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ052 = rJcicZ052 == null ? null : rJcicZ052.getContent();
		if (rJcicZ052 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ052   = " + zJcicZ052);
		for (JcicZ052 sJcicZ052 : zJcicZ052) {
			if (sJcicZ052.getOutJcicTxtDate() == iJcicDate || sJcicZ052.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ052.getTranKey();
				String iSubmitKey = sJcicZ052.getSubmitKey();
				String iCustId = sJcicZ052.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ052.getRcDate());
				String iBankCode1 = sJcicZ052.getBankCode1();
				String iDataCode1 = sJcicZ052.getDataCode1();
				String iBankCode2 = sJcicZ052.getBankCode2();
				String iDataCode2 = sJcicZ052.getDataCode2();
				String iBankCode3 = sJcicZ052.getBankCode3();
				String iDataCode3 = sJcicZ052.getDataCode3();
				String iBankCode4 = sJcicZ052.getBankCode4();
				String iDataCode4 = sJcicZ052.getDataCode4();
				String iBankCode5 = sJcicZ052.getBankCode5();
				String iDataCode5 = sJcicZ052.getDataCode5();
				iBankCode1 = FormatUtil.padX(iBankCode1, 3);
				iDataCode1 = FormatUtil.padX(iDataCode1, 2);
				iBankCode2 = FormatUtil.padX(iBankCode2, 3);
				iDataCode2 = FormatUtil.padX(iDataCode2, 2);
				iBankCode3 = FormatUtil.padX(iBankCode3, 3);
				iDataCode3 = FormatUtil.padX(iDataCode3, 2);
				iBankCode4 = FormatUtil.padX(iBankCode4, 3);
				iDataCode4 = FormatUtil.padX(iDataCode4, 2);
				iBankCode5 = FormatUtil.padX(iBankCode5, 3);
				iDataCode5 = FormatUtil.padX(iDataCode5, 2);
				String iChangePayDate = String.valueOf(sJcicZ052.getChangePayDate());
				int ixChangePayDate = Integer.valueOf(sJcicZ052.getChangePayDate());
				String iUkey = sJcicZ052.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "52" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iBankCode1 + iDataCode1 + iBankCode2 + iDataCode2 + iBankCode3
						+ iDataCode3 + iBankCode4 + iDataCode4 + iBankCode5 + iDataCode5
						+ StringUtils.leftPad(iChangePayDate, 7, '0') + StringUtils.rightPad("", 20);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ052.setOutJcicTxtDate(sJcicZ052.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ052.setActualFilingDate(iDate);
					sJcicZ052.setActualFilingMark("Y");
				} else {
					sJcicZ052.setActualFilingDate(sJcicZ052.getActualFilingDate());
					sJcicZ052.setActualFilingMark("N");
				}
				try {
					sJcicZ052Service.update(sJcicZ052, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ052Log iJcicZ052Log = new JcicZ052Log();
				JcicZ052LogId iJcicZ052LogId = new JcicZ052LogId();
				iJcicZ052LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ052LogId.setUkey(iUkey);
				iJcicZ052Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ052Log.setUkey(iUkey);
				iJcicZ052Log.setJcicZ052LogId(iJcicZ052LogId);
				iJcicZ052Log.setBankCode1(iBankCode1);
				iJcicZ052Log.setDataCode1(iDataCode1);
				iJcicZ052Log.setBankCode2(iBankCode2);
				iJcicZ052Log.setDataCode2(iDataCode2);
				iJcicZ052Log.setBankCode3(iBankCode3);
				iJcicZ052Log.setDataCode3(iDataCode3);
				iJcicZ052Log.setBankCode4(iBankCode4);
				iJcicZ052Log.setDataCode4(iDataCode4);
				iJcicZ052Log.setBankCode5(iBankCode5);
				iJcicZ052Log.setDataCode5(iDataCode5);
				iJcicZ052Log.setChangePayDate(ixChangePayDate);
				iJcicZ052Log.setTranKey(iTranKey);
				iJcicZ052Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ052LogService.insert(iJcicZ052Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z052)");
				}
				this.info("準備寫入L6932 ===== L8313(052)");
				JcicZ052Log uJcicZ052log = sJcicZ052LogService.ukeyFirst(iJcicZ052LogId.getUkey(), titaVo);
				this.info("uJcicZ052log      = " + uJcicZ052log);
				JcicZ052Log iJcicZ052Log2 = new JcicZ052Log();
				iJcicZ052Log2 = (JcicZ052Log) iDataLog.clone(iJcicZ052Log);
				this.info("iJcicZ052Log2     = " + iJcicZ052Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ052.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8415");
				iDataLog.setEnv(titaVo, iJcicZ052Log2, uJcicZ052log);
//				iDataLog.exec("L8415報送", iJcicZ052Log2.getUkey() + iJcicZ052Log2.getTxSeq());
				iDataLog.exec("L8415報送", iJcicZ052Log2.getUkey());
//				iCount++;
			}
		}
	}

//Jcic-053
	public void doZ053File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ053> rJcicZ053 = null;
		List<JcicZ053> zJcicZ053 = new ArrayList<JcicZ053>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ053Id jcicZ053Id = new JcicZ053Id();
		jcicZ053Id.setCustId(iCustId);
		jcicZ053Id.setRcDate(iRcDate);
		jcicZ053Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ053 = sJcicZ053Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ053 = rJcicZ053 == null ? null : rJcicZ053.getContent();
		if (rJcicZ053 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ053   = " + zJcicZ053);
		for (JcicZ053 sJcicZ053 : zJcicZ053) {
			if (sJcicZ053.getOutJcicTxtDate() == iJcicDate || sJcicZ053.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ053.getTranKey();
				String iSubmitKey = sJcicZ053.getSubmitKey();
				String iCustId = sJcicZ053.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ053.getRcDate());
				String iMaxMainCode = sJcicZ053.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iAgreeSend = sJcicZ053.getAgreeSend();
				iAgreeSend = FormatUtil.padX(iAgreeSend, 1);
				String iAgreeSendData1 = sJcicZ053.getAgreeSendData1();
				iAgreeSendData1 = FormatUtil.padX(iAgreeSendData1, 2);
				String iAgreeSendData2 = sJcicZ053.getAgreeSendData2();
				iAgreeSendData2 = FormatUtil.padX(iAgreeSendData2, 2);
				String iChangePayDate = String.valueOf(sJcicZ053.getChangePayDate());
				int ixChangePayDate = Integer.valueOf(sJcicZ053.getChangePayDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ053.getUkey();
				String text = "53" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iMaxMainCode + iAgreeSend + iAgreeSendData1 + iAgreeSendData2
						+ StringUtils.leftPad(iChangePayDate, 7, '0') + StringUtils.rightPad("", 37);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ053.setOutJcicTxtDate(sJcicZ053.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ053.setActualFilingDate(iDate);
					sJcicZ053.setActualFilingMark("Y");
				} else {
					sJcicZ053.setActualFilingDate(sJcicZ053.getActualFilingDate());
					sJcicZ053.setActualFilingMark("N");
				}
				try {
					sJcicZ053Service.update(sJcicZ053, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
				// 回填JcicDate後寫入Log檔
				JcicZ053Log iJcicZ053Log = new JcicZ053Log();
				JcicZ053LogId iJcicZ053LogId = new JcicZ053LogId();
				iJcicZ053LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ053LogId.setUkey(iUkey);
				iJcicZ053Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ053Log.setUkey(iUkey);
				iJcicZ053Log.setJcicZ053LogId(iJcicZ053LogId);
				iJcicZ053Log.setAgreeSend(iAgreeSend);
				iJcicZ053Log.setAgreeSendData1(iAgreeSendData1);
				iJcicZ053Log.setAgreeSendData2(iAgreeSendData2);
				iJcicZ053Log.setChangePayDate(ixChangePayDate);
				iJcicZ053Log.setTranKey(iTranKey);
				iJcicZ053Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ053LogService.insert(iJcicZ053Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z053)");
				}
				this.info("準備寫入L6932 ===== L8314(053)");
				JcicZ053Log uJcicZ053log = sJcicZ053LogService.ukeyFirst(iJcicZ053LogId.getUkey(), titaVo);
				this.info("uJcicZ053log      = " + uJcicZ053log);
				JcicZ053Log iJcicZ053Log2 = new JcicZ053Log();
				iJcicZ053Log2 = (JcicZ053Log) iDataLog.clone(iJcicZ053Log);
				this.info("iJcicZ053Log2     = " + iJcicZ053Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ053.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8416");
				iDataLog.setEnv(titaVo, iJcicZ053Log2, uJcicZ053log);
//				iDataLog.exec("L8416報送", iJcicZ053Log2.getUkey() + iJcicZ053Log2.getTxSeq());
				iDataLog.exec("L8416報送", iJcicZ053Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-054
	public void doZ054File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ054> rJcicZ054 = null;
		List<JcicZ054> zJcicZ054 = new ArrayList<JcicZ054>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ054Id jcicZ054Id = new JcicZ054Id();
		jcicZ054Id.setCustId(iCustId);
		jcicZ054Id.setRcDate(iRcDate);
		jcicZ054Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+ 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ054 = sJcicZ054Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0, Integer.MAX_VALUE,
				titaVo);
		zJcicZ054 = rJcicZ054 == null ? null : rJcicZ054.getContent();
		if (rJcicZ054 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ054   = " + zJcicZ054);
		for (JcicZ054 sJcicZ054 : zJcicZ054) {
			if (sJcicZ054.getOutJcicTxtDate() == iJcicDate || sJcicZ054.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ054.getTranKey();
				String iSubmitKey = sJcicZ054.getSubmitKey();
				String iCustId = sJcicZ054.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ054.getRcDate());
				String iMaxMainCode = sJcicZ054.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iPayOffResult = sJcicZ054.getPayOffResult();
				iPayOffResult = FormatUtil.padX(iPayOffResult, 1);
				String iPayOffDate = String.valueOf(sJcicZ054.getPayOffDate());
				String iUkey = sJcicZ054.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "54" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + iMaxMainCode + iPayOffResult
						+ StringUtils.leftPad(iPayOffDate, 7, '0') + StringUtils.rightPad("", 41);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ054.setOutJcicTxtDate(sJcicZ054.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ054.setActualFilingDate(iDate);
					sJcicZ054.setActualFilingMark("Y");
				} else {
					sJcicZ054.setActualFilingDate(sJcicZ054.getActualFilingDate());
					sJcicZ054.setActualFilingMark("N");
				}
				try {
					sJcicZ054Service.update(sJcicZ054, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ054Log iJcicZ054Log = new JcicZ054Log();
				JcicZ054LogId iJcicZ054LogId = new JcicZ054LogId();
				iJcicZ054LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ054LogId.setUkey(iUkey);
				iJcicZ054Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ054Log.setUkey(iUkey);
				iJcicZ054Log.setJcicZ054LogId(iJcicZ054LogId);
				iJcicZ054Log.setPayOffResult(iPayOffResult);
				iJcicZ054Log.setTranKey(iTranKey);
				iJcicZ054Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ054LogService.insert(iJcicZ054Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z054)");
				}
				this.info("準備寫入L6932 ===== L8315(054)");
				JcicZ054Log uJcicZ054log = sJcicZ054LogService.ukeyFirst(iJcicZ054LogId.getUkey(), titaVo);
				this.info("uJcicZ054log      = " + uJcicZ054log);
				JcicZ054Log iJcicZ054Log2 = new JcicZ054Log();
				iJcicZ054Log2 = (JcicZ054Log) iDataLog.clone(iJcicZ054Log);
				this.info("iJcicZ054Log2     = " + iJcicZ054Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ054.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8417");
				iDataLog.setEnv(titaVo, iJcicZ054Log2, uJcicZ054log);
//				iDataLog.exec("L8417報送", iJcicZ054Log2.getUkey() + iJcicZ054Log2.getTxSeq());
				iDataLog.exec("L8417報送", iJcicZ054Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-055
	public void doZ055File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ055> rJcicZ055 = null;
		List<JcicZ055> zJcicZ055 = new ArrayList<JcicZ055>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iClaimDate = Integer.valueOf(titaVo.get("ClaimDate"));

		JcicZ055Id jcicZ055Id = new JcicZ055Id();
		jcicZ055Id.setCustId(iCustId);
		jcicZ055Id.setClaimDate(iRcDate);
		jcicZ055Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ055 = sJcicZ055Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ055 = rJcicZ055 == null ? null : rJcicZ055.getContent();
		if (rJcicZ055 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ055   = " + zJcicZ055);
		for (JcicZ055 sJcicZ055 : zJcicZ055) {
			if (sJcicZ055.getOutJcicTxtDate() == iJcicDate || sJcicZ055.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ055.getTranKey();
				String iSubmitKey = sJcicZ055.getSubmitKey();
				String iCustId = sJcicZ055.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iCaseStatus = sJcicZ055.getCaseStatus();
				String iClaimDate = String.valueOf(sJcicZ055.getClaimDate());
				String iCourtCode = sJcicZ055.getCourtCode();
				iCourtCode = FormatUtil.padX(iCourtCode, 3);
				int iYear = sJcicZ055.getYear() - 1911;
				String iCourtDiv = sJcicZ055.getCourtDiv();
				iCourtDiv = FormatUtil.padX(iCourtDiv, 4);
				String iCourtCaseNo = sJcicZ055.getCourtCaseNo();
				iCourtCaseNo = FormatUtil.padX(iCourtCaseNo, 40);
				String iPayDate = String.valueOf(sJcicZ055.getPayDate());
				String iPayEndDate = String.valueOf(sJcicZ055.getPayEndDate());
				String iPeriod = String.valueOf(sJcicZ055.getPeriod());
				String iRate = String.valueOf(sJcicZ055.getRate());
				String iOutstandAmt = String.valueOf(sJcicZ055.getOutstandAmt());
				String iSubAmt = String.valueOf(sJcicZ055.getSubAmt());
				String iClaimStatus1 = sJcicZ055.getClaimStatus1();
				String iSaveDate = String.valueOf(sJcicZ055.getSaveDate());
				String outSaceDate = "";
				if (iSaveDate.equals("0")) {
					outSaceDate = StringUtils.rightPad("", 7, "");
				} else {
					outSaceDate = StringUtils.leftPad(iSaveDate, 7, '0');
				}
				String iClaimStatus2 = sJcicZ055.getClaimStatus2();
				String iSaveEndDate = String.valueOf(sJcicZ055.getSaveEndDate());
				String outSaveEndDate = "";
				if (iSaveEndDate.equals("0")) {
					outSaveEndDate = StringUtils.rightPad("", 7, "");
				} else {
					outSaveEndDate = StringUtils.leftPad(iSaveEndDate, 7, '0');
				}

				String iIsImplement = sJcicZ055.getIsImplement();
				String iInspectName = sJcicZ055.getInspectName();
				int ixPayDate = Integer.valueOf(sJcicZ055.getPayDate());
				int ixPayEndDate = Integer.valueOf(sJcicZ055.getPayEndDate());
				int ixPeriod = Integer.valueOf(sJcicZ055.getPeriod());
				int ixOutstandAmt = Integer.valueOf(sJcicZ055.getOutstandAmt());
				int ixSubAmt = Integer.valueOf(sJcicZ055.getSubAmt());
				int ixSaveDate = Integer.valueOf(sJcicZ055.getSaveDate());
				int ixSaveEndDate = Integer.valueOf(sJcicZ055.getSaveEndDate());
				String iUkey = sJcicZ055.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "55" + iTranKey + iSubmitKey + iCustId + StringUtils.rightPad(iCaseStatus, 1, "")
						+ StringUtils.leftPad(iClaimDate, 7, '0') + StringUtils.rightPad("", 5) + iCourtCode
						+ StringUtils.leftPad(String.valueOf(iYear), 3, '0') + iCourtDiv + iCourtCaseNo
						+ StringUtils.leftPad(iPayDate, 7, '0') + StringUtils.leftPad(iPayEndDate, 7, '0')
						+ StringUtils.leftPad(iPeriod, 3, '0') + StringUtils.leftPad(FormatRate(iRate, 2), 5, '0')
						+ StringUtils.leftPad(iOutstandAmt, 9, '0') + StringUtils.leftPad(iSubAmt, 9, '0')
						+ StringUtils.rightPad(iClaimStatus1, 1, "") + outSaceDate
						+ StringUtils.rightPad(iClaimStatus2, 1, "") + outSaveEndDate
						+ StringUtils.rightPad(iIsImplement, 1, "") + StringUtils.rightPad(iInspectName, 10, "")
						+ StringUtils.rightPad("", 54);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ055.setOutJcicTxtDate(sJcicZ055.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ055.setActualFilingDate(iDate);
					sJcicZ055.setActualFilingMark("Y");
				} else {
					sJcicZ055.setActualFilingDate(sJcicZ055.getActualFilingDate());
					sJcicZ055.setActualFilingMark("N");
				}
				try {
					sJcicZ055Service.update(sJcicZ055, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
				// 回填JcicDate後寫入Log檔
				JcicZ055Log iJcicZ055Log = new JcicZ055Log();
				JcicZ055LogId iJcicZ055LogId = new JcicZ055LogId();
				iJcicZ055LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ055LogId.setUkey(iUkey);
				iJcicZ055Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ055Log.setUkey(iUkey);
				iJcicZ055Log.setJcicZ055LogId(iJcicZ055LogId);
				iJcicZ055Log.setYear(iYear);
				iJcicZ055Log.setCourtDiv(iCourtDiv);
				iJcicZ055Log.setCourtCaseNo(iCourtCaseNo);
				iJcicZ055Log.setPayDate(ixPayDate);
				iJcicZ055Log.setPayEndDate(ixPayEndDate);
				iJcicZ055Log.setPeriod(ixPeriod);
				iJcicZ055Log.setOutstandAmt(ixOutstandAmt);
				iJcicZ055Log.setSubAmt(ixSubAmt);
				iJcicZ055Log.setClaimStatus1(iClaimStatus1);
				iJcicZ055Log.setSaveDate(ixSaveDate);
				iJcicZ055Log.setClaimStatus2(iClaimStatus2);
				iJcicZ055Log.setSaveEndDate(ixSaveEndDate);
				iJcicZ055Log.setIsImplement(iIsImplement);
				iJcicZ055Log.setInspectName(iInspectName);
				iJcicZ055Log.setTranKey(iTranKey);
				iJcicZ055Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ055LogService.insert(iJcicZ055Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z055)");
				}
				this.info("準備寫入L6932 ===== L8316(055)");
				JcicZ055Log uJcicZ055log = sJcicZ055LogService.ukeyFirst(iJcicZ055LogId.getUkey(), titaVo);
				this.info("uJcicZ055log      = " + uJcicZ055log);
				JcicZ055Log iJcicZ055Log2 = new JcicZ055Log();
				iJcicZ055Log2 = (JcicZ055Log) iDataLog.clone(iJcicZ055Log);
				this.info("iJcicZ055Log2     = " + iJcicZ055Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ055.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8418");
				iDataLog.setEnv(titaVo, iJcicZ055Log2, uJcicZ055log);
//				iDataLog.exec("L8418報送", iJcicZ055Log2.getUkey() + iJcicZ055Log2.getTxSeq());
				iDataLog.exec("L8418報送", iJcicZ055Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-056
	public void doZ056File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ056> rJcicZ056 = null;
		List<JcicZ056> zJcicZ056 = new ArrayList<JcicZ056>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iClaimDate = Integer.valueOf(titaVo.get("ClaimDate"));

		JcicZ056Id jcicZ056Id = new JcicZ056Id();
		jcicZ056Id.setCustId(iCustId);
		jcicZ056Id.setClaimDate(iClaimDate);
		jcicZ056Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ056 = sJcicZ056Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ056 = rJcicZ056 == null ? null : rJcicZ056.getContent();
		if (rJcicZ056 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ056   = " + zJcicZ056);
		for (JcicZ056 sJcicZ056 : zJcicZ056) {
			if (sJcicZ056.getOutJcicTxtDate() == iJcicDate || sJcicZ056.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ056.getTranKey();
				String iSubmitKey = sJcicZ056.getSubmitKey();
				String iCustId = sJcicZ056.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iCaseStatus = sJcicZ056.getCaseStatus();
				String iClaimDate = String.valueOf(sJcicZ056.getClaimDate());
				String iCourtCode = sJcicZ056.getCourtCode();
				iCourtCode = FormatUtil.padX(iCourtCode, 3);
				String iYear = String.valueOf(sJcicZ056.getYear() - 1911);
				int ixYear = Integer.valueOf(sJcicZ056.getYear());
				String iCourtDiv = sJcicZ056.getCourtDiv();
				iCourtDiv = FormatUtil.padX(iCourtDiv, 4);
				String iCourtCaseNo = sJcicZ056.getCourtCaseNo();
				iCourtCaseNo = FormatUtil.padX(iCourtCaseNo, 40);
				String iApprove = sJcicZ056.getApprove();
				String iOutStandAmt = String.valueOf(sJcicZ056.getOutstandAmt());
				String iClaimStatus1 = sJcicZ056.getClaimStatus1();
				String iSaveDate = String.valueOf(sJcicZ056.getSaveDate());
				String iClaimStatus2 = sJcicZ056.getClaimStatus2();
				String iSaveEndDate = String.valueOf(sJcicZ056.getSaveEndDate());
				String iSubAmt = String.valueOf(sJcicZ056.getSubAmt());
				String iAdminName = sJcicZ056.getAdminName();
				iAdminName = FormatUtil.padX(iAdminName, 10);
				String iUkey = sJcicZ056.getUkey();
				int ixOutStandAmt = Integer.valueOf(sJcicZ056.getOutstandAmt());
				int ixSubAmt = Integer.valueOf(sJcicZ056.getSubAmt());
				int ixSaveDate = Integer.valueOf(sJcicZ056.getSaveDate());
				int ixSaveEndDate = Integer.valueOf(sJcicZ056.getSaveEndDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "56" + iTranKey + iSubmitKey + iCustId + StringUtils.rightPad(iCaseStatus, 1, "")
						+ StringUtils.leftPad(iClaimDate, 7, '0') + StringUtils.rightPad("", 5) + iCourtCode
						+ StringUtils.leftPad(iYear, 3, '0') + iCourtDiv + iCourtCaseNo
						+ StringUtils.rightPad(iApprove, 1, "") + StringUtils.leftPad(iOutStandAmt, 9, '0')
						+ StringUtils.leftPad(iSubAmt, 9, '0') + StringUtils.rightPad(iClaimStatus1, 1, "")
						+ StringUtils.leftPad(iSaveDate, 7, '0') + StringUtils.rightPad(iClaimStatus2, 1, "")
						+ StringUtils.leftPad(iSaveEndDate, 7, '0') + iAdminName + StringUtils.rightPad("", 76);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ056.setOutJcicTxtDate(sJcicZ056.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ056.setActualFilingDate(iDate);
					sJcicZ056.setActualFilingMark("Y");
				} else {
					sJcicZ056.setActualFilingDate(sJcicZ056.getActualFilingDate());
					sJcicZ056.setActualFilingMark("N");
				}
				try {
					sJcicZ056Service.update(sJcicZ056, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ056Log iJcicZ056Log = new JcicZ056Log();
				JcicZ056LogId iJcicZ056LogId = new JcicZ056LogId();
				iJcicZ056LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ056LogId.setUkey(iUkey);
				iJcicZ056Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ056Log.setUkey(iUkey);
				iJcicZ056Log.setJcicZ056LogId(iJcicZ056LogId);
				iJcicZ056Log.setYear(ixYear);
				iJcicZ056Log.setCourtDiv(iCourtDiv);
				iJcicZ056Log.setCourtCaseNo(iCourtCaseNo);
				iJcicZ056Log.setApprove(iApprove);
				iJcicZ056Log.setOutstandAmt(ixOutStandAmt);
				iJcicZ056Log.setSubAmt(ixSubAmt);
				iJcicZ056Log.setClaimStatus1(iClaimStatus1);
				iJcicZ056Log.setSaveDate(ixSaveDate);
				iJcicZ056Log.setClaimStatus2(iClaimStatus2);
				iJcicZ056Log.setSaveEndDate(ixSaveEndDate);
				iJcicZ056Log.setAdminName(iAdminName);
				iJcicZ056Log.setTranKey(iTranKey);
				iJcicZ056Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ056LogService.insert(iJcicZ056Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z056)");
				}
				this.info("準備寫入L6932 ===== L8317(056)");
				JcicZ056Log uJcicZ056log = sJcicZ056LogService.ukeyFirst(iJcicZ056LogId.getUkey(), titaVo);
				this.info("uJcicZ056log      = " + uJcicZ056log);
				JcicZ056Log iJcicZ056Log2 = new JcicZ056Log();
				iJcicZ056Log2 = (JcicZ056Log) iDataLog.clone(iJcicZ056Log);
				this.info("iJcicZ056Log2     = " + iJcicZ056Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ056.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8419");
				iDataLog.setEnv(titaVo, iJcicZ056Log2, uJcicZ056log);
//				iDataLog.exec("L8419報送", iJcicZ056Log2.getUkey() + iJcicZ056Log2.getTxSeq());
				iDataLog.exec("L8419報送", iJcicZ056Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-060
	public void doZ060File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ060> rJcicZ060 = null;
		List<JcicZ060> zJcicZ060 = new ArrayList<JcicZ060>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ060Id jcicZ060Id = new JcicZ060Id();
		jcicZ060Id.setCustId(iCustId);
		jcicZ060Id.setRcDate(iRcDate);
		jcicZ060Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+ 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ060 = sJcicZ060Service.findkeyFilingDate(iActualFilingDate , iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ060 = rJcicZ060 == null ? null : rJcicZ060.getContent();
		if (rJcicZ060 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ060   = " + zJcicZ060);
		for (JcicZ060 sJcicZ060 : zJcicZ060) {
			if (sJcicZ060.getOutJcicTxtDate() == iJcicDate || sJcicZ060.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ060.getTranKey();
				String iSubmitKey = sJcicZ060.getSubmitKey();
				String iCustId = sJcicZ060.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ060.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ060.getChangePayDate());
				String iYM = FormateYM(sJcicZ060.getYM());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ060.getUkey();
				String text = "60" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iChangePayDate, 7, '0')
						+ StringUtils.leftPad(iYM, 5, '0') + StringUtils.rightPad("", 30);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ060.setOutJcicTxtDate(sJcicZ060.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ060.setActualFilingDate(iDate);
					sJcicZ060.setActualFilingMark("Y");
				} else {
					sJcicZ060.setActualFilingDate(sJcicZ060.getActualFilingDate());
					sJcicZ060.setActualFilingMark("N");
				}
				try {
					sJcicZ060Service.update(sJcicZ060, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ060Log iJcicZ060Log = new JcicZ060Log();
				JcicZ060LogId iJcicZ060LogId = new JcicZ060LogId();
				iJcicZ060LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ060LogId.setUkey(iUkey);
				iJcicZ060Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ060Log.setUkey(iUkey);
				iJcicZ060Log.setJcicZ060LogId(iJcicZ060LogId);
				iJcicZ060Log.setYM(Integer.valueOf(iYM) + 191100);
				iJcicZ060Log.setTranKey(iTranKey);
				iJcicZ060Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ060LogService.insert(iJcicZ060Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z060)");
				}
				this.info("準備寫入L6932 ===== L8318(060)");
				JcicZ060Log uJcicZ060log = sJcicZ060LogService.ukeyFirst(iJcicZ060LogId.getUkey(), titaVo);
				this.info("uJcicZ060log      = " + uJcicZ060log);
				JcicZ060Log iJcicZ060Log2 = new JcicZ060Log();
				iJcicZ060Log2 = (JcicZ060Log) iDataLog.clone(iJcicZ060Log);
				this.info("iJcicZ060Log2     = " + iJcicZ060Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ060.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8420");
				iDataLog.setEnv(titaVo, iJcicZ060Log2, uJcicZ060log);
//				iDataLog.exec("L8420報送", iJcicZ060Log2.getUkey() + iJcicZ060Log2.getTxSeq());
				iDataLog.exec("L8420報送", iJcicZ060Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-061
	public void doZ061File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ061> rJcicZ061 = null;
		List<JcicZ061> zJcicZ061 = new ArrayList<JcicZ061>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ061Id jcicZ061Id = new JcicZ061Id();
		jcicZ061Id.setCustId(iCustId);
		jcicZ061Id.setRcDate(iRcDate);
		jcicZ061Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ061 = sJcicZ061Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ061 = rJcicZ061 == null ? null : rJcicZ061.getContent();
		if (rJcicZ061 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ061   = " + zJcicZ061);
		for (JcicZ061 sJcicZ061 : zJcicZ061) {
			if (sJcicZ061.getOutJcicTxtDate() == iJcicDate || sJcicZ061.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ061.getTranKey();
				String iSubmitKey = sJcicZ061.getSubmitKey();
				String iCustId = sJcicZ061.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ061.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ061.getChangePayDate());
				String iMaxMainCode = sJcicZ061.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iExpBalanceAmt = String.valueOf(sJcicZ061.getExpBalanceAmt());
				String iCashBalanceAmt = String.valueOf(sJcicZ061.getCashBalanceAmt());
				String iCreditBalanceAmt = String.valueOf(sJcicZ061.getCreditBalanceAmt());
				String iMaxMainNote = sJcicZ061.getMaxMainNote();
				String iIsGuarantor = sJcicZ061.getIsGuarantor();
				String iIsChangePayment = sJcicZ061.getIsChangePayment();
				int ixExpBalanceAmt = Integer.valueOf(sJcicZ061.getExpBalanceAmt());
				int ixCashBalanceAmt = Integer.valueOf(sJcicZ061.getCashBalanceAmt());
				int ixCreditBalanceAmt = Integer.valueOf(sJcicZ061.getCreditBalanceAmt());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ061.getUkey();
				String text = "61" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iChangePayDate, 7, '0') + iMaxMainCode
						+ StringUtils.leftPad(iExpBalanceAmt, 9, '0') + StringUtils.leftPad(iCashBalanceAmt, 9, '0')
						+ StringUtils.leftPad(iCreditBalanceAmt, 9, '0') + StringUtils.rightPad(iMaxMainNote, 1, "")
						+ StringUtils.rightPad(iIsGuarantor, 1, "") + StringUtils.rightPad(iIsChangePayment, 1, "")
						+ StringUtils.rightPad("", 42);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ061.setOutJcicTxtDate(sJcicZ061.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ061.setActualFilingDate(iDate);
					sJcicZ061.setActualFilingMark("Y");
				} else {
					sJcicZ061.setActualFilingDate(sJcicZ061.getActualFilingDate());
					sJcicZ061.setActualFilingMark("N");
				}
				try {
					sJcicZ061Service.update(sJcicZ061, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ061Log iJcicZ061Log = new JcicZ061Log();
				JcicZ061LogId iJcicZ061LogId = new JcicZ061LogId();
				iJcicZ061LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ061LogId.setUkey(iUkey);
				iJcicZ061Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ061Log.setUkey(iUkey);
				iJcicZ061Log.setJcicZ061LogId(iJcicZ061LogId);
				iJcicZ061Log.setExpBalanceAmt(ixExpBalanceAmt);
				iJcicZ061Log.setCashBalanceAmt(ixCashBalanceAmt);
				iJcicZ061Log.setCreditBalanceAmt(ixCreditBalanceAmt);
				iJcicZ061Log.setMaxMainNote(iMaxMainNote);
				iJcicZ061Log.setIsGuarantor(iIsGuarantor);
				iJcicZ061Log.setIsChangePayment(iIsChangePayment);
				iJcicZ061Log.setTranKey(iTranKey);
				iJcicZ061Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ061LogService.insert(iJcicZ061Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z061)");
				}
				this.info("準備寫入L6932 ===== L8319(061)");
				JcicZ061Log uJcicZ061log = sJcicZ061LogService.ukeyFirst(iJcicZ061LogId.getUkey(), titaVo);
				this.info("uJcicZ061log      = " + uJcicZ061log);
				JcicZ061Log iJcicZ061Log2 = new JcicZ061Log();
				iJcicZ061Log2 = (JcicZ061Log) iDataLog.clone(iJcicZ061Log);
				this.info("iJcicZ061Log2     = " + iJcicZ061Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ061.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8421");
				iDataLog.setEnv(titaVo, iJcicZ061Log2, uJcicZ061log);
//				iDataLog.exec("L8421報送", iJcicZ061Log2.getUkey() + iJcicZ061Log2.getTxSeq());
				iDataLog.exec("L8421報送", iJcicZ061Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-062
	public void doZ062File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ062> rJcicZ062 = null;
		List<JcicZ062> zJcicZ062 = new ArrayList<JcicZ062>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ062Id jcicZ062Id = new JcicZ062Id();
		jcicZ062Id.setCustId(iCustId);
		jcicZ062Id.setRcDate(iRcDate);
		jcicZ062Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ062 = sJcicZ062Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ062 = rJcicZ062 == null ? null : rJcicZ062.getContent();
		if (rJcicZ062 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}
		this.info("zJcicZ062   = " + zJcicZ062);
		for (JcicZ062 sJcicZ062 : zJcicZ062) {
			if (sJcicZ062.getOutJcicTxtDate() == iJcicDate || sJcicZ062.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ062.getTranKey();
				String iSubmitKey = sJcicZ062.getSubmitKey();
				String iCustId = sJcicZ062.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ062.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ062.getChangePayDate());
				String iCompletePeriod = String.valueOf(sJcicZ062.getCompletePeriod());
				int ixCompletePeriod = Integer.valueOf(sJcicZ062.getCompletePeriod());
				String iPeriod = String.valueOf(sJcicZ062.getPeriod());
				int ixPeriod = Integer.valueOf(sJcicZ062.getPeriod());
				String iRate = String.valueOf(sJcicZ062.getRate());
				BigDecimal ixRate = sJcicZ062.getRate();
				String iExpBalanceAmt = String.valueOf(sJcicZ062.getExpBalanceAmt());
				int ixExpBalanceAmt = Integer.valueOf(sJcicZ062.getExpBalanceAmt());
				String iCashBalanceAmt = String.valueOf(sJcicZ062.getCashBalanceAmt());
				int ixCashBalanceAmt = Integer.valueOf(sJcicZ062.getCashBalanceAmt());
				String iCreditBalanceAmt = String.valueOf(sJcicZ062.getCreditBalanceAmt());
				int ixCreditBalanceAmt = Integer.valueOf(sJcicZ062.getCreditBalanceAmt());
				String iChaRepayAmt = String.valueOf(sJcicZ062.getChaRepayAmt());
				BigDecimal ixChaRepayAmt = sJcicZ062.getChaRepayAmt();
				String iChaRepayAgreeDate = String.valueOf(sJcicZ062.getChaRepayAgreeDate());
				int ixChaRepayAgreeDate = Integer.valueOf(sJcicZ062.getChaRepayAgreeDate());
				String iChaRepayViewDate = String.valueOf(sJcicZ062.getChaRepayViewDate());
				int ixChaRepayViewDate = Integer.valueOf(sJcicZ062.getChaRepayViewDate());
				String iChaRepayEndDate = String.valueOf(sJcicZ062.getChaRepayEndDate());
				int ixChaRepayEndDate = Integer.valueOf(sJcicZ062.getChaRepayEndDate());
				String iChaRepayFirstDate = String.valueOf(sJcicZ062.getChaRepayFirstDate());
				int ixChaRepayFirstDate = Integer.valueOf(sJcicZ062.getChaRepayFirstDate());
				String iPayAccount = String.valueOf(sJcicZ062.getPayAccount());
				String iPostAddr = String.valueOf(sJcicZ062.getPostAddr());
				iPostAddr = FormatUtil.padX(iPostAddr, 76);
				String iMonthPayAmt = String.valueOf(sJcicZ062.getMonthPayAmt());
				int ixMonthPayAmt = Integer.valueOf(sJcicZ062.getMonthPayAmt());
				String iGradeType = String.valueOf(sJcicZ062.getGradeType());
				String iPeriod2 = String.valueOf(sJcicZ062.getPeriod2());
				String outPeriod2 = "";
				if (iPeriod2.equals("0")) {
					outPeriod2 = StringUtils.leftPad("0", 3, "0");
				} else {
					outPeriod2 = StringUtils.leftPad(iPeriod2, 3, '0');
				}
				int ixPeriod2 = Integer.valueOf(sJcicZ062.getPeriod2());
				String iRate2 = String.valueOf(sJcicZ062.getRate2());
				String outRate2 = "";
				if (iRate2.equals("0")) {
					outRate2 = StringUtils.leftPad("0", 5, "0");
				} else {
					outRate2 = StringUtils.leftPad(FormatRate(iRate2, 2), 5, '0');
				}
				BigDecimal ixRate2 = sJcicZ062.getRate2();
				String iMonthPayAmt2 = String.valueOf(sJcicZ062.getMonthPayAmt2());
				int ixMonthPayAmt2 = Integer.valueOf(sJcicZ062.getMonthPayAmt2());
				String iUkey = sJcicZ062.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "62" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iChangePayDate, 7, '0')
						+ StringUtils.leftPad(iCompletePeriod, 3, '0') + StringUtils.leftPad(iPeriod, 3, '0')
						+ StringUtils.leftPad(FormatRate(iRate, 2), 5, '0')
						+ StringUtils.leftPad(iExpBalanceAmt, 9, '0') + StringUtils.leftPad(iCashBalanceAmt, 9, '0')
						+ StringUtils.leftPad(iCreditBalanceAmt, 9, '0') + StringUtils.leftPad(iChaRepayAmt, 10, '0')
						+ StringUtils.leftPad(iChaRepayAgreeDate, 7, '0')
						+ StringUtils.leftPad(iChaRepayViewDate, 7, '0') + StringUtils.leftPad(iChaRepayEndDate, 7, '0')
						+ StringUtils.leftPad(iChaRepayFirstDate, 7, '0') + StringUtils.rightPad(iPayAccount, 20, "")
						+ iPostAddr + StringUtils.leftPad(iMonthPayAmt, 9, '0')
						+ StringUtils.rightPad(iGradeType, 1, "") + outPeriod2 + outRate2
						+ StringUtils.leftPad(iMonthPayAmt2, 9, '0') + StringUtils.rightPad("", 66);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ062.setOutJcicTxtDate(sJcicZ062.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ062.setActualFilingDate(iDate);
					sJcicZ062.setActualFilingMark("Y");
				} else {
					sJcicZ062.setActualFilingDate(sJcicZ062.getActualFilingDate());
					sJcicZ062.setActualFilingMark("N");
				}
				try {
					sJcicZ062Service.update(sJcicZ062, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ062Log iJcicZ062Log = new JcicZ062Log();
				JcicZ062LogId iJcicZ062LogId = new JcicZ062LogId();
				iJcicZ062LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ062LogId.setUkey(iUkey);
				iJcicZ062Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ062Log.setUkey(iUkey);
				iJcicZ062Log.setJcicZ062LogId(iJcicZ062LogId);
				iJcicZ062Log.setCompletePeriod(ixCompletePeriod);
				iJcicZ062Log.setPeriod(ixPeriod);
				iJcicZ062Log.setRate(ixRate);
				iJcicZ062Log.setExpBalanceAmt(ixExpBalanceAmt);
				iJcicZ062Log.setCashBalanceAmt(ixCashBalanceAmt);
				iJcicZ062Log.setCreditBalanceAmt(ixCreditBalanceAmt);
				iJcicZ062Log.setChaRepayAmt(ixChaRepayAmt);
				iJcicZ062Log.setChaRepayAgreeDate(ixChaRepayAgreeDate);
				iJcicZ062Log.setChaRepayViewDate(ixChaRepayViewDate);
				iJcicZ062Log.setChaRepayEndDate(ixChaRepayEndDate);
				iJcicZ062Log.setChaRepayFirstDate(ixChaRepayFirstDate);
				iJcicZ062Log.setPayAccount(iPayAccount);
				iJcicZ062Log.setPostAddr(iPostAddr);
				iJcicZ062Log.setMonthPayAmt(ixMonthPayAmt);
				iJcicZ062Log.setGradeType(iGradeType);
				iJcicZ062Log.setPeriod2(ixPeriod2);
				iJcicZ062Log.setRate2(ixRate2);
				iJcicZ062Log.setMonthPayAmt2(ixMonthPayAmt2);
				iJcicZ062Log.setTranKey(iTranKey);
				iJcicZ062Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ062LogService.insert(iJcicZ062Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z062)");
				}
				this.info("準備寫入L6932 ===== L8320(062)");
				JcicZ062Log uJcicZ062log = sJcicZ062LogService.ukeyFirst(iJcicZ062LogId.getUkey(), titaVo);
				this.info("uJcicZ062log      = " + uJcicZ062log);
				JcicZ062Log iJcicZ062Log2 = new JcicZ062Log();
				iJcicZ062Log2 = (JcicZ062Log) iDataLog.clone(iJcicZ062Log);
				this.info("iJcicZ062Log2     = " + iJcicZ062Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ062.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8422");
				iDataLog.setEnv(titaVo, iJcicZ062Log2, uJcicZ062log);
//				iDataLog.exec("L8422報送", iJcicZ062Log2.getUkey() + iJcicZ062Log2.getTxSeq());
				iDataLog.exec("L8422報送", iJcicZ062Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-063
	public void doZ063File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ063> rJcicZ063 = null;
		List<JcicZ063> zJcicZ063 = new ArrayList<JcicZ063>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iRcDate = Integer.valueOf(titaVo.get("RcDate"));

		JcicZ063Id jcicZ063Id = new JcicZ063Id();
		jcicZ063Id.setCustId(iCustId);
		jcicZ063Id.setRcDate(iRcDate);
		jcicZ063Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ063 = sJcicZ063Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ063 = rJcicZ063 == null ? null : rJcicZ063.getContent();
		if (rJcicZ063 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ063   = " + zJcicZ063);
		for (JcicZ063 sJcicZ063 : zJcicZ063) {
			if (sJcicZ063.getOutJcicTxtDate() == iJcicDate || sJcicZ063.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ063.getTranKey();
				String iSubmitKey = sJcicZ063.getSubmitKey();
				String iCustId = sJcicZ063.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iRcDate = String.valueOf(sJcicZ063.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ063.getChangePayDate());
				String iCloseDate = String.valueOf(sJcicZ063.getClosedDate());
				int ixCloseDate = Integer.valueOf(sJcicZ063.getClosedDate());
				String iClosedResult = sJcicZ063.getClosedResult();
				String iUkey = sJcicZ063.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "63" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iRcDate, 7, '0')
						+ StringUtils.rightPad("", 5) + StringUtils.leftPad(iChangePayDate, 7, '0')
						+ StringUtils.leftPad(iCloseDate, 7, '0') + StringUtils.rightPad(iClosedResult, 1, "")
						+ StringUtils.rightPad("", 42);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ063.setOutJcicTxtDate(sJcicZ063.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ063.setActualFilingDate(iDate);
					sJcicZ063.setActualFilingMark("Y");
				} else {
					sJcicZ063.setActualFilingDate(sJcicZ063.getActualFilingDate());
					sJcicZ063.setActualFilingMark("N");
				}
				try {
					sJcicZ063Service.update(sJcicZ063, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ063Log iJcicZ063Log = new JcicZ063Log();
				JcicZ063LogId iJcicZ063LogId = new JcicZ063LogId();
				iJcicZ063LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ063LogId.setUkey(iUkey);
				iJcicZ063Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ063Log.setUkey(iUkey);
				iJcicZ063Log.setJcicZ063LogId(iJcicZ063LogId);
				iJcicZ063Log.setClosedDate(ixCloseDate);
				iJcicZ063Log.setClosedResult(iClosedResult);
				iJcicZ063Log.setTranKey(iTranKey);
				iJcicZ063Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ063LogService.insert(iJcicZ063Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z063)");
				}
				this.info("準備寫入L6932 ===== L8321(063)");
				JcicZ063Log uJcicZ063log = sJcicZ063LogService.ukeyFirst(iJcicZ063LogId.getUkey(), titaVo);
				this.info("uJcicZ063log      = " + uJcicZ063log);
				JcicZ063Log iJcicZ063Log2 = new JcicZ063Log();
				iJcicZ063Log2 = (JcicZ063Log) iDataLog.clone(iJcicZ063Log);
				this.info("iJcicZ063Log2     = " + iJcicZ063Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ063.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8423");
				iDataLog.setEnv(titaVo, iJcicZ063Log2, uJcicZ063log);
//				iDataLog.exec("L8423報送", iJcicZ063Log2.getUkey() + iJcicZ063Log2.getTxSeq());
				iDataLog.exec("L8423報送", iJcicZ063Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-440
	public void doZ440File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ440> rJcicZ440 = null;
		List<JcicZ440> zJcicZ440 = new ArrayList<JcicZ440>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ440Id jcicZ440Id = new JcicZ440Id();
		jcicZ440Id.setCustId(iCustId);
		jcicZ440Id.setApplyDate(iApplyDate);
		jcicZ440Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ440 = sJcicZ440Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ440 = rJcicZ440 == null ? null : rJcicZ440.getContent();
		if (rJcicZ440 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ440   = " + zJcicZ440);
		for (JcicZ440 sJcicZ440 : zJcicZ440) {
			if (sJcicZ440.getOutJcicTxtDate() == iJcicDate || sJcicZ440.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ440.getTranKey();
				String iSubmitKey = sJcicZ440.getSubmitKey();
				String iCustId = sJcicZ440.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ440.getApplyDate());
				String iCourtCode = sJcicZ440.getCourtCode();
				String iAgreeDate = String.valueOf(sJcicZ440.getAgreeDate());
				int ixAgreeDate = Integer.valueOf(sJcicZ440.getAgreeDate());
				String iStartDate = String.valueOf(sJcicZ440.getStartDate());
				int ixStartDate = Integer.valueOf(sJcicZ440.getStartDate());
				String iRemindDate = String.valueOf(sJcicZ440.getRemindDate());
				int ixRemindDate = Integer.valueOf(sJcicZ440.getRemindDate());
				String iApplyType = sJcicZ440.getApplyType();
				String iReportYn = sJcicZ440.getReportYn();
				String iNotBankId1 = sJcicZ440.getNotBankId1();
				String iNotBankId2 = sJcicZ440.getNotBankId2();
				String iNotBankId3 = sJcicZ440.getNotBankId3();
				String iNotBankId4 = sJcicZ440.getNotBankId4();
				String iNotBankId5 = sJcicZ440.getNotBankId5();
				String iNotBankId6 = sJcicZ440.getNotBankId6();
				iNotBankId1 = FormatUtil.padX(iNotBankId1, 3);
				iNotBankId2 = FormatUtil.padX(iNotBankId2, 3);
				iNotBankId3 = FormatUtil.padX(iNotBankId3, 3);
				iNotBankId4 = FormatUtil.padX(iNotBankId4, 3);
				iNotBankId5 = FormatUtil.padX(iNotBankId5, 3);
				iNotBankId6 = FormatUtil.padX(iNotBankId6, 3);

				String iUkey = sJcicZ440.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "440" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5)
						+ StringUtils.leftPad(iAgreeDate, 7, '0') + StringUtils.leftPad(iStartDate, 7, '0')
						+ StringUtils.leftPad(iRemindDate, 7, '0') + StringUtils.rightPad(iApplyType, 1, "")
						+ StringUtils.rightPad(iReportYn, 1, "") + iNotBankId1 + iNotBankId2 + iNotBankId3 + iNotBankId4
						+ iNotBankId5 + iNotBankId6 + StringUtils.rightPad("", 17);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ440.setOutJcicTxtDate(sJcicZ440.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ440.setActualFilingDate(iDate);
					sJcicZ440.setActualFilingMark("Y");
				} else {
					sJcicZ440.setActualFilingDate(sJcicZ440.getActualFilingDate());
					sJcicZ440.setActualFilingMark("N");
				}
				try {
					sJcicZ440Service.update(sJcicZ440, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ440Log iJcicZ440Log = new JcicZ440Log();
				JcicZ440LogId iJcicZ440LogId = new JcicZ440LogId();
				iJcicZ440LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ440LogId.setUkey(iUkey);
				iJcicZ440Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ440Log.setUkey(iUkey);
				iJcicZ440Log.setJcicZ440LogId(iJcicZ440LogId);
				iJcicZ440Log.setAgreeDate(ixAgreeDate);
				iJcicZ440Log.setStartDate(ixStartDate);
				iJcicZ440Log.setRemindDate(ixRemindDate);
				iJcicZ440Log.setApplyType(iApplyType);
				iJcicZ440Log.setReportYn(iReportYn);
				iJcicZ440Log.setNotBankId1(iNotBankId1);
				iJcicZ440Log.setNotBankId2(iNotBankId2);
				iJcicZ440Log.setNotBankId3(iNotBankId3);
				iJcicZ440Log.setNotBankId4(iNotBankId4);
				iJcicZ440Log.setNotBankId5(iNotBankId5);
				iJcicZ440Log.setNotBankId6(iNotBankId6);
				iJcicZ440Log.setTranKey(iTranKey);
				iJcicZ440Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ440LogService.insert(iJcicZ440Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z440)");
				}
				this.info("準備寫入L6932 ===== L8322(440)");
				JcicZ440Log uJcicZ440log = sJcicZ440LogService.ukeyFirst(iJcicZ440LogId.getUkey(), titaVo);
				this.info("uJcicZ440log      = " + uJcicZ440log);
				JcicZ440Log iJcicZ440Log2 = new JcicZ440Log();
				iJcicZ440Log2 = (JcicZ440Log) iDataLog.clone(iJcicZ440Log);
				this.info("iJcicZ440Log2     = " + iJcicZ440Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ440.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8424");
				iDataLog.setEnv(titaVo, iJcicZ440Log2, uJcicZ440log);
//				iDataLog.exec("L8424報送", iJcicZ440Log2.getUkey() + iJcicZ440Log2.getTxSeq());
				iDataLog.exec("L8424報送", iJcicZ440Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-442
	public void doZ442File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ442> rJcicZ442 = null;
		List<JcicZ442> zJcicZ442 = new ArrayList<JcicZ442>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ442Id jcicZ442Id = new JcicZ442Id();
		jcicZ442Id.setCustId(iCustId);
		jcicZ442Id.setApplyDate(iApplyDate);
		jcicZ442Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ442 = sJcicZ442Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ442 = rJcicZ442 == null ? null : rJcicZ442.getContent();
		if (rJcicZ442 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ442   = " + zJcicZ442);
		for (JcicZ442 sJcicZ442 : zJcicZ442) {
			if (sJcicZ442.getOutJcicTxtDate() == iJcicDate || sJcicZ442.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ442.getTranKey();
				String iSubmitKey = sJcicZ442.getSubmitKey();
				String iCustId = sJcicZ442.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ442.getApplyDate());
				String iCourtCode = sJcicZ442.getCourtCode();
				iCourtCode = FormatUtil.padX(iCourtCode, 3);
				String iMaxMainCode = sJcicZ442.getMaxMainCode();
				iMaxMainCode = FormatUtil.padX(iMaxMainCode, 3);
				String iIsMaxMain = sJcicZ442.getIsMaxMain();
				String iIsClaims = sJcicZ442.getIsClaims();
				String iGuarLoanCnt = String.valueOf(sJcicZ442.getGuarLoanCnt());
				int ixGuarLoanCnt = Integer.valueOf(sJcicZ442.getGuarLoanCnt());
				String iCivil323ExpAmt = String.valueOf(sJcicZ442.getCivil323ExpAmt());
				int ixCivil323ExpAmt = Integer.valueOf(sJcicZ442.getCivil323ExpAmt());
				String iCivil323CashAmt = String.valueOf(sJcicZ442.getCivil323CashAmt());
				int ixCivil323CashAmt = Integer.valueOf(sJcicZ442.getCivil323CashAmt());
				String iCivil323CreditAmt = String.valueOf(sJcicZ442.getCivil323CreditAmt());
				int ixCivil323CreditAmt = Integer.valueOf(sJcicZ442.getCivil323CreditAmt());
				String iCivil323GuarAmt = String.valueOf(sJcicZ442.getCivil323GuarAmt());
				int ixCivil323GuarAmt = Integer.valueOf(sJcicZ442.getCivil323GuarAmt());
				String iReceExpPrin = String.valueOf(sJcicZ442.getReceExpPrin());
				int ixReceExpPrin = Integer.valueOf(sJcicZ442.getReceExpPrin());
				String iReceExpInte = String.valueOf(sJcicZ442.getReceExpInte());
				int ixReceExpInte = Integer.valueOf(sJcicZ442.getReceExpInte());
				String iReceExpPena = String.valueOf(sJcicZ442.getReceExpPena());
				int ixReceExpPena = Integer.valueOf(sJcicZ442.getReceExpPena());
				String iReceExpOther = String.valueOf(sJcicZ442.getReceExpOther());
				int ixReceExpOther = Integer.valueOf(sJcicZ442.getReceExpOther());
				String iCashCardPrin = String.valueOf(sJcicZ442.getCashCardPrin());
				int ixCashCardPrin = Integer.valueOf(sJcicZ442.getCashCardPrin());
				String iCashCardInte = String.valueOf(sJcicZ442.getCashCardInte());
				int ixCashCardInte = Integer.valueOf(sJcicZ442.getCashCardInte());
				String iCashCardPena = String.valueOf(sJcicZ442.getCashCardPena());
				int ixCashCardPena = Integer.valueOf(sJcicZ442.getCashCardPena());
				String iCashCardOther = String.valueOf(sJcicZ442.getCashCardOther());
				int ixCashCardOther = Integer.valueOf(sJcicZ442.getCashCardOther());
				String iCreditCardPrin = String.valueOf(sJcicZ442.getCreditCardPrin());
				int ixCreditCardPrin = Integer.valueOf(sJcicZ442.getCreditCardPrin());
				String iCreditCardInte = String.valueOf(sJcicZ442.getCreditCardInte());
				int ixCreditCardInte = Integer.valueOf(sJcicZ442.getCreditCardInte());
				String iCreditCardPena = String.valueOf(sJcicZ442.getCreditCardPena());
				int ixCreditCardPena = Integer.valueOf(sJcicZ442.getCreditCardPena());
				String iCreditCardOther = String.valueOf(sJcicZ442.getCreditCardOther());
				int ixCreditCardOther = Integer.valueOf(sJcicZ442.getCreditCardOther());
				String iGuarObliPrin = String.valueOf(sJcicZ442.getGuarObliPrin());
				int ixGuarObliPrin = Integer.valueOf(sJcicZ442.getGuarObliPrin());
				String iGuarObliInte = String.valueOf(sJcicZ442.getGuarObliInte());
				int ixGuarObliInte = Integer.valueOf(sJcicZ442.getGuarObliInte());
				String iGuarObliPena = String.valueOf(sJcicZ442.getGuarObliPena());
				int ixGuarObliPena = Integer.valueOf(sJcicZ442.getGuarObliPena());
				String iGuarObliOther = String.valueOf(sJcicZ442.getGuarObliOther());
				int ixGuarObliOther = Integer.valueOf(sJcicZ442.getGuarObliOther());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ442.getUkey();
				String text = "442" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ iCourtCode + StringUtils.rightPad("", 5) + iMaxMainCode
						+ StringUtils.rightPad(iIsMaxMain, 1, "") + StringUtils.rightPad(iIsClaims, 1, "")
						+ StringUtils.leftPad(iGuarLoanCnt, 2, '0') + StringUtils.leftPad(iCivil323ExpAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323CashAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323CreditAmt, 9, '0')
						+ StringUtils.leftPad(iCivil323GuarAmt, 9, '0') + StringUtils.rightPad("", 5)
						+ StringUtils.leftPad(iReceExpPrin, 9, '0') + StringUtils.leftPad(iReceExpInte, 9, '0')
						+ StringUtils.leftPad(iReceExpPena, 9, '0') + StringUtils.leftPad(iReceExpOther, 9, '0')
						+ StringUtils.leftPad(iCashCardPrin, 9, '0') + StringUtils.leftPad(iCashCardInte, 9, '0')
						+ StringUtils.leftPad(iCashCardPena, 9, '0') + StringUtils.leftPad(iCashCardOther, 9, '0')
						+ StringUtils.leftPad(iCreditCardPrin, 9, '0') + StringUtils.leftPad(iCreditCardInte, 9, '0')
						+ StringUtils.leftPad(iCreditCardPena, 9, '0') + StringUtils.leftPad(iCreditCardOther, 9, '0')
						+ StringUtils.leftPad(iGuarObliPrin, 9, '0') + StringUtils.leftPad(iGuarObliInte, 9, '0')
						+ StringUtils.leftPad(iGuarObliPena, 9, '0') + StringUtils.leftPad(iGuarObliOther, 9, '0')
						+ StringUtils.rightPad("", 56);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ442.setOutJcicTxtDate(sJcicZ442.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ442.setActualFilingDate(iDate);
					sJcicZ442.setActualFilingMark("Y");
				} else {
					sJcicZ442.setActualFilingDate(sJcicZ442.getActualFilingDate());
					sJcicZ442.setActualFilingMark("N");
				}
				try {
					sJcicZ442Service.update(sJcicZ442, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ442Log iJcicZ442Log = new JcicZ442Log();
				JcicZ442LogId iJcicZ442LogId = new JcicZ442LogId();
				this.info("TxSeq===" + titaVo.getTxSeq());
				iJcicZ442LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ442LogId.setUkey(iUkey);
				iJcicZ442Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ442Log.setUkey(iUkey);
				iJcicZ442Log.setJcicZ442LogId(iJcicZ442LogId);
				iJcicZ442Log.setIsMaxMain(iIsMaxMain);
				iJcicZ442Log.setIsClaims(iIsClaims);
				iJcicZ442Log.setGuarLoanCnt(ixGuarLoanCnt);
				iJcicZ442Log.setCivil323ExpAmt(ixCivil323ExpAmt);
				iJcicZ442Log.setCivil323CashAmt(ixCivil323CashAmt);
				iJcicZ442Log.setCivil323CreditAmt(ixCivil323CreditAmt);
				iJcicZ442Log.setCivil323GuarAmt(ixCivil323GuarAmt);
				iJcicZ442Log.setReceExpPrin(ixReceExpPrin);
				iJcicZ442Log.setReceExpInte(ixReceExpInte);
				iJcicZ442Log.setReceExpPena(ixReceExpPena);
				iJcicZ442Log.setReceExpOther(ixReceExpOther);
				iJcicZ442Log.setCashCardPrin(ixCashCardPrin);
				iJcicZ442Log.setCashCardInte(ixCashCardInte);
				iJcicZ442Log.setCashCardPena(ixCashCardPena);
				iJcicZ442Log.setCashCardOther(ixCashCardOther);
				iJcicZ442Log.setCreditCardPrin(ixCreditCardPrin);
				iJcicZ442Log.setCreditCardInte(ixCreditCardInte);
				iJcicZ442Log.setCreditCardPena(ixCreditCardPena);
				iJcicZ442Log.setCreditCardOther(ixCreditCardOther);
				iJcicZ442Log.setGuarObliPrin(ixGuarObliPrin);
				iJcicZ442Log.setGuarObliInte(ixGuarObliInte);
				iJcicZ442Log.setGuarObliPena(ixGuarObliPena);
				iJcicZ442Log.setGuarObliOther(ixGuarObliOther);
				iJcicZ442Log.setTranKey(iTranKey);
				iJcicZ442Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ442LogService.insert(iJcicZ442Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z443)");
				}
				this.info("準備寫入L6932 ===== L8323(442)");
				JcicZ442Log uJcicZ442log = sJcicZ442LogService.ukeyFirst(iJcicZ442LogId.getUkey(), titaVo);
				this.info("uJcicZ442log      = " + uJcicZ442log);
				JcicZ442Log iJcicZ442Log2 = new JcicZ442Log();
				iJcicZ442Log2 = (JcicZ442Log) iDataLog.clone(iJcicZ442Log);
				this.info("iJcicZ442Log2     = " + iJcicZ442Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ442.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8425");
				iDataLog.setEnv(titaVo, iJcicZ442Log2, uJcicZ442log);
//				iDataLog.exec("L8425報送", iJcicZ442Log2.getUkey() + iJcicZ442Log2.getTxSeq());
				iDataLog.exec("L8425報送", iJcicZ442Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-443
	public void doZ443File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ443> rJcicZ443 = null;
		List<JcicZ443> zJcicZ443 = new ArrayList<JcicZ443>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ443Id jcicZ443Id = new JcicZ443Id();
		jcicZ443Id.setCustId(iCustId);
		jcicZ443Id.setApplyDate(iApplyDate);
		jcicZ443Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ443 = sJcicZ443Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ443 = rJcicZ443 == null ? null : rJcicZ443.getContent();
		if (rJcicZ443 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ443   = " + zJcicZ443);
		for (JcicZ443 sJcicZ443 : zJcicZ443) {
			if (sJcicZ443.getOutJcicTxtDate() == iJcicDate || sJcicZ443.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ443.getTranKey();
				String iSubmitKey = sJcicZ443.getSubmitKey();
				String iCustId = sJcicZ443.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ443.getApplyDate());
				String iCourtCode = sJcicZ443.getCourtCode();
				iCourtCode = FormatUtil.padX(iCourtCode, 3);
				String iMaxMainCode = sJcicZ443.getMaxMainCode();
				String iIsMaxMain = sJcicZ443.getIsMaxMain();
				String iAccount = sJcicZ443.getAccount();
				iAccount = FormatUtil.padX(iAccount, 50);
				String iGuarantyType = sJcicZ443.getGuarantyType();
				BigDecimal iLoanAmt = sJcicZ443.getLoanAmt();
				String ixLoanAmt = String.valueOf(sJcicZ443.getLoanAmt());
				BigDecimal iCreditAmt = sJcicZ443.getCreditAmt();
				String ixCreditAmt = String.valueOf(sJcicZ443.getCreditAmt());
				BigDecimal iPrincipal = sJcicZ443.getPrincipal();
				String ixPrincipal = String.valueOf(sJcicZ443.getPrincipal());
				BigDecimal iInterest = sJcicZ443.getInterest();
				String ixInterest = String.valueOf(sJcicZ443.getInterest());
				BigDecimal iPenalty = sJcicZ443.getPenalty();
				String ixPenalty = String.valueOf(sJcicZ443.getPenalty());
				BigDecimal iOther = sJcicZ443.getOther();
				String ixOther = String.valueOf(sJcicZ443.getOther());
				BigDecimal iTerminalPayAmt = sJcicZ443.getTerminalPayAmt();
				String ixTerminalPayAmt = String.valueOf(sJcicZ443.getTerminalPayAmt());
				BigDecimal iLatestPayAmt = sJcicZ443.getLatestPayAmt();
				String ixLatestPayAmt = String.valueOf(sJcicZ443.getLatestPayAmt());
				int iFinalPayDay = Integer.valueOf(sJcicZ443.getFinalPayDay());
				String ixFinalPayDay = String.valueOf(sJcicZ443.getFinalPayDay());
				BigDecimal iNotyetacQuit = sJcicZ443.getNotyetacQuit();
				String ixNotyetacQuit = String.valueOf(sJcicZ443.getNotyetacQuit());
				int iMothPayDay = Integer.valueOf(sJcicZ443.getMothPayDay());
				String ixMothPayDay = String.valueOf(sJcicZ443.getMothPayDay());
				int iBeginDate = Integer.valueOf(sJcicZ443.getBeginDate()) - 191100;
				String ixBeginDate = FormateYM(sJcicZ443.getBeginDate());
				int iEndDate = Integer.valueOf(sJcicZ443.getEndDate()) - 191100;
				String ixEndDate = FormateYM(sJcicZ443.getEndDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ443.getUkey();
				String text = "443" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5) + iMaxMainCode
						+ StringUtils.rightPad(iIsMaxMain, 1, "") + iAccount
						+ StringUtils.rightPad(iGuarantyType, 2, "") + StringUtils.leftPad(ixLoanAmt, 12, '0')
						+ StringUtils.leftPad(ixCreditAmt, 12, '0') + StringUtils.leftPad(ixPrincipal, 10, '0')
						+ StringUtils.leftPad(ixInterest, 10, '0') + StringUtils.leftPad(ixPenalty, 10, '0')
						+ StringUtils.leftPad(ixOther, 10, '0') + StringUtils.leftPad(ixTerminalPayAmt, 10, '0')
						+ StringUtils.leftPad(ixLatestPayAmt, 10, '0') + StringUtils.leftPad(ixFinalPayDay, 7, '0')
						+ StringUtils.leftPad(ixNotyetacQuit, 10, '0') + StringUtils.leftPad(ixMothPayDay, 2, '0')
						+ StringUtils.leftPad(ixBeginDate, 5, '0') + StringUtils.leftPad(ixEndDate, 5, '0')
						+ StringUtils.rightPad("", 49);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ443.setOutJcicTxtDate(sJcicZ443.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ443.setActualFilingDate(iDate);
					sJcicZ443.setActualFilingMark("Y");
				} else {
					sJcicZ443.setActualFilingDate(sJcicZ443.getActualFilingDate());
					sJcicZ443.setActualFilingMark("N");
				}
				try {
					sJcicZ443Service.update(sJcicZ443, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ443Log iJcicZ443Log = new JcicZ443Log();
				JcicZ443LogId iJcicZ443LogId = new JcicZ443LogId();
				this.info("TxSeq===" + titaVo.getTxSeq());
				iJcicZ443LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ443LogId.setUkey(iUkey);
				iJcicZ443Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ443Log.setUkey(iUkey);
				iJcicZ443Log.setJcicZ443LogId(iJcicZ443LogId);
				iJcicZ443Log.setIsMaxMain(iIsMaxMain);
				iJcicZ443Log.setGuarantyType(iGuarantyType);
				iJcicZ443Log.setLoanAmt(iLoanAmt);
				iJcicZ443Log.setCreditAmt(iCreditAmt);
				iJcicZ443Log.setPrincipal(iPrincipal);
				iJcicZ443Log.setInterest(iInterest);
				iJcicZ443Log.setPenalty(iPenalty);
				iJcicZ443Log.setOther(iOther);
				iJcicZ443Log.setTerminalPayAmt(iTerminalPayAmt);
				iJcicZ443Log.setLatestPayAmt(iLatestPayAmt);
				iJcicZ443Log.setFinalPayDay(iFinalPayDay);
				iJcicZ443Log.setNotyetacQuit(iNotyetacQuit);
				iJcicZ443Log.setMothPayDay(iMothPayDay);
				iJcicZ443Log.setBeginDate(iBeginDate + 191100);
				iJcicZ443Log.setEndDate(iEndDate + 191100);
				iJcicZ443Log.setTranKey(iTranKey);
				iJcicZ443Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ443LogService.insert(iJcicZ443Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z443)");
				}
				this.info("準備寫入L6932 ===== L8324(443)");
				JcicZ443Log uJcicZ443log = sJcicZ443LogService.ukeyFirst(iJcicZ443LogId.getUkey(), titaVo);
				this.info("uJcicZ443log      = " + uJcicZ443log);
				JcicZ443Log iJcicZ443Log2 = new JcicZ443Log();
				iJcicZ443Log2 = (JcicZ443Log) iDataLog.clone(iJcicZ443Log);
				this.info("iJcicZ443Log2     = " + iJcicZ443Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ443.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8426");
				iDataLog.setEnv(titaVo, iJcicZ443Log2, uJcicZ443log);
//				iDataLog.exec("L8426報送", iJcicZ443Log2.getUkey() + iJcicZ443Log2.getTxSeq());
				iDataLog.exec("L8426報送", iJcicZ443Log2.getUkey());
//				iCount++;
			}
		}
	}

	public void doZ444File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ444> rJcicZ444 = null;
		List<JcicZ444> zJcicZ444 = new ArrayList<JcicZ444>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ444Id jcicZ444Id = new JcicZ444Id();
		jcicZ444Id.setCustId(iCustId);
		jcicZ444Id.setApplyDate(iApplyDate);
		jcicZ444Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ444 = sJcicZ444Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ444 = rJcicZ444 == null ? null : rJcicZ444.getContent();
		if (rJcicZ444 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ444   = " + zJcicZ444);
		for (JcicZ444 sJcicZ444 : zJcicZ444) {
			if (sJcicZ444.getOutJcicTxtDate() == iJcicDate || sJcicZ444.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ444.getTranKey();
				String iSubmitKey = sJcicZ444.getSubmitKey();
				String iCustId = sJcicZ444.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ444.getApplyDate());
				String iCourtCode = sJcicZ444.getCourtCode();
				iCourtCode = FormatUtil.padX(iCourtCode, 3);
				String iCustRegAddr = sJcicZ444.getCustRegAddr();
				/** 2022/2/23 使用padLeft會導致地址往右邊靠，且位元組會有錯，已修改成padx Mata */
//					iCustRegAddr = FormatUtil.padLeft(iCustRegAddr, 76);
				iCustRegAddr = FormatUtil.padX(iCustRegAddr, 76);
				String iCustComAddr = sJcicZ444.getCustComAddr();
//					iCustComAddr = FormatUtil.padLeft(iCustComAddr, 76);
				iCustComAddr = FormatUtil.padX(iCustComAddr, 76);
				String iCustRegTelNo = sJcicZ444.getCustRegTelNo();
				String iCustComTelNo = sJcicZ444.getCustComTelNo();
				String iCustMobilNo = sJcicZ444.getCustMobilNo();
				String iUkey = sJcicZ444.getUkey();
				this.info("iCustRegAddr ===== " + iCustRegAddr);
				this.info("iCustComAddr ===== " + iCustComAddr);
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "444" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ iCourtCode + StringUtils.rightPad("", 5) + iCustRegAddr + iCustComAddr
						+ StringUtils.rightPad(iCustRegTelNo, 16, "") + StringUtils.rightPad(iCustComTelNo, 16, "")
						+ StringUtils.rightPad(iCustMobilNo, 16, "") + StringUtils.rightPad("", 18);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ444.setOutJcicTxtDate(sJcicZ444.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ444.setActualFilingDate(iDate);
					sJcicZ444.setActualFilingMark("Y");
				} else {
					sJcicZ444.setActualFilingDate(sJcicZ444.getActualFilingDate());
					sJcicZ444.setActualFilingMark("N");
				}
				try {
					sJcicZ444Service.update(sJcicZ444, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
				// 回填JcicDate後寫入Log檔
				JcicZ444Log iJcicZ444Log = new JcicZ444Log();
				JcicZ444LogId iJcicZ444LogId = new JcicZ444LogId();
				this.info("TxSeq===" + titaVo.getTxSeq());
				iJcicZ444LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ444LogId.setUkey(iUkey);
				iJcicZ444Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ444Log.setUkey(iUkey);
				iJcicZ444Log.setJcicZ444LogId(iJcicZ444LogId);
				iJcicZ444Log.setCustRegAddr(iCustRegAddr);
				iJcicZ444Log.setCustComAddr(iCustComAddr);
				iJcicZ444Log.setCustRegTelNo(iCustRegTelNo);
				iJcicZ444Log.setCustComTelNo(iCustComTelNo);
				iJcicZ444Log.setCustMobilNo(iCustMobilNo);
				iJcicZ444Log.setTranKey(iTranKey);
				iJcicZ444Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ444LogService.insert(iJcicZ444Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z444)");
				}
				this.info("準備寫入L6932 ===== L8325(444)");
				JcicZ444Log uJcicZ444log = sJcicZ444LogService.ukeyFirst(iJcicZ444LogId.getUkey(), titaVo);
				this.info("uJcicZ444log      = " + uJcicZ444log);
				JcicZ444Log iJcicZ444Log2 = new JcicZ444Log();
				iJcicZ444Log2 = (JcicZ444Log) iDataLog.clone(iJcicZ444Log);
				this.info("iJcicZ444Log2     = " + iJcicZ444Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ444.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8427");
				iDataLog.setEnv(titaVo, iJcicZ444Log2, uJcicZ444log);
//				iDataLog.exec("L8427報送", iJcicZ444Log2.getUkey() + iJcicZ444Log2.getTxSeq());
				iDataLog.exec("L8427報送", iJcicZ444Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-446
	public void doZ446File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ446> rJcicZ446 = null;
		List<JcicZ446> zJcicZ446 = new ArrayList<JcicZ446>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ446Id jcicZ446Id = new JcicZ446Id();
		jcicZ446Id.setCustId(iCustId);
		jcicZ446Id.setApplyDate(iApplyDate);
		jcicZ446Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ446 = sJcicZ446Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ446 = rJcicZ446 == null ? null : rJcicZ446.getContent();
		if (rJcicZ446 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ446   = " + zJcicZ446);
		for (JcicZ446 sJcicZ446 : zJcicZ446) {
			if (sJcicZ446.getOutJcicTxtDate() == iJcicDate || sJcicZ446.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ446.getTranKey();
				String iSubmitKey = sJcicZ446.getSubmitKey();
				String iCustId = sJcicZ446.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ446.getApplyDate());
				String iCourtCode = sJcicZ446.getCourtCode();
				String iCloseCode = sJcicZ446.getCloseCode();
				String iCloseDate = String.valueOf(sJcicZ446.getCloseDate());
				int ixCloseDate = Integer.valueOf(sJcicZ446.getCloseDate());
				String iUkey = sJcicZ446.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "446" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5)
						+ StringUtils.rightPad(iCloseCode, 2, "") + StringUtils.leftPad(iCloseDate, 7, '0')
						+ StringUtils.rightPad("", 39);
				this.put(text);
				// 檔案產生後，回填JcicDate
				sJcicZ446.setOutJcicTxtDate(sJcicZ446.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ446.setActualFilingDate(iDate);
					sJcicZ446.setActualFilingMark("Y");
				} else {
					sJcicZ446.setActualFilingDate(sJcicZ446.getActualFilingDate());
					sJcicZ446.setActualFilingMark("N");
				}
				try {
					sJcicZ446Service.update(sJcicZ446, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
				// 回填JcicDate後寫入Log檔
				JcicZ446Log iJcicZ446Log = new JcicZ446Log();
				JcicZ446LogId iJcicZ446LogId = new JcicZ446LogId();
				iJcicZ446LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ446LogId.setUkey(iUkey);
				iJcicZ446Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ446Log.setUkey(iUkey);
				iJcicZ446Log.setJcicZ446LogId(iJcicZ446LogId);
				iJcicZ446Log.setCloseCode(iCloseCode);
				iJcicZ446Log.setCloseDate(ixCloseDate);
				iJcicZ446Log.setTranKey(iTranKey);
				iJcicZ446Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ446LogService.insert(iJcicZ446Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z447)");
				}
				this.info("準備寫入L6932 ===== L8326(446)");
				JcicZ446Log uJcicZ446log = sJcicZ446LogService.ukeyFirst(iJcicZ446LogId.getUkey(), titaVo);
				this.info("uJcicZ446log      = " + uJcicZ446log);
				JcicZ446Log iJcicZ446Log2 = new JcicZ446Log();
				iJcicZ446Log2 = (JcicZ446Log) iDataLog.clone(iJcicZ446Log);
				this.info("iJcicZ446Log2     = " + iJcicZ446Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ446.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8428");
				iDataLog.setEnv(titaVo, iJcicZ446Log2, uJcicZ446log);
//				iDataLog.exec("L8428報送", iJcicZ446Log2.getUkey() + iJcicZ446Log2.getTxSeq());
				iDataLog.exec("L8428報送", iJcicZ446Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-447
	public void doZ447File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ447> rJcicZ447 = null;
		List<JcicZ447> zJcicZ447 = new ArrayList<JcicZ447>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ447Id jcicZ447Id = new JcicZ447Id();
		jcicZ447Id.setCustId(iCustId);
		jcicZ447Id.setApplyDate(iApplyDate);
		jcicZ447Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ447 = sJcicZ447Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ447 = rJcicZ447 == null ? null : rJcicZ447.getContent();
		if (rJcicZ447 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ447   = " + zJcicZ447);
		for (JcicZ447 sJcicZ447 : zJcicZ447) {
			if (sJcicZ447.getOutJcicTxtDate() == iJcicDate || sJcicZ447.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ447.getTranKey();
				String iSubmitKey = sJcicZ447.getSubmitKey();
				String iCustId = sJcicZ447.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ447.getApplyDate());
				String iCourtCode = sJcicZ447.getCourtCode();
				BigDecimal iCivil323Amt = sJcicZ447.getCivil323Amt();
				String ixCivil323Amt = String.valueOf(sJcicZ447.getCivil323Amt());
				BigDecimal iTotalAmt = sJcicZ447.getTotalAmt();
				String ixTotalAmt = String.valueOf(sJcicZ447.getTotalAmt());
				int iSignDate = Integer.valueOf(sJcicZ447.getSignDate());
				String ixSignDate = String.valueOf(sJcicZ447.getSignDate());
				int iFirstPayDate = Integer.valueOf(sJcicZ447.getFirstPayDate());
				String ixFirstPayDate = String.valueOf(sJcicZ447.getFirstPayDate());
				int iPeriod = Integer.valueOf(sJcicZ447.getPeriod());
				String ixPeriod = String.valueOf(sJcicZ447.getPeriod());
				BigDecimal iRate = sJcicZ447.getRate();
				String ixRate = String.valueOf(sJcicZ447.getRate());
				int iMonthPayAmt = Integer.valueOf(sJcicZ447.getMonthPayAmt());
				String ixMonthPayAmt = String.valueOf(sJcicZ447.getMonthPayAmt());
				String iPayAccount = sJcicZ447.getPayAccount();
				String iUkey = sJcicZ447.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "447" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5)
						+ StringUtils.leftPad(ixCivil323Amt, 10, '0') + StringUtils.leftPad(ixTotalAmt, 10, '0')
						+ StringUtils.leftPad(ixSignDate, 7, "") + StringUtils.leftPad(ixFirstPayDate, 7, '0')
						+ StringUtils.leftPad(ixPeriod, 3, '0') + StringUtils.leftPad(FormatRate(ixRate, 2), 5, '0')
						+ StringUtils.leftPad(ixMonthPayAmt, 9, '0') + StringUtils.rightPad(iPayAccount, 20, "")
						+ StringUtils.rightPad("", 27);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ447.setOutJcicTxtDate(sJcicZ447.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ447.setActualFilingDate(iDate);
					sJcicZ447.setActualFilingMark("Y");
				} else {
					sJcicZ447.setActualFilingDate(sJcicZ447.getActualFilingDate());
					sJcicZ447.setActualFilingMark("N");
				}
				try {
					sJcicZ447Service.update(sJcicZ447, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ447Log iJcicZ447Log = new JcicZ447Log();
				JcicZ447LogId iJcicZ447LogId = new JcicZ447LogId();
				this.info("TxSeq===" + titaVo.getTxSeq());
				iJcicZ447LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ447LogId.setUkey(iUkey);
				iJcicZ447Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ447Log.setUkey(iUkey);
				iJcicZ447Log.setJcicZ447LogId(iJcicZ447LogId);
				iJcicZ447Log.setCivil323Amt(iCivil323Amt);
				iJcicZ447Log.setTotalAmt(iTotalAmt);
				iJcicZ447Log.setSignDate(iSignDate);
				iJcicZ447Log.setFirstPayDate(iFirstPayDate);
				iJcicZ447Log.setPeriod(iPeriod);
				iJcicZ447Log.setRate(iRate);
				iJcicZ447Log.setMonthPayAmt(iMonthPayAmt);
				iJcicZ447Log.setPayAccount(iPayAccount);
				iJcicZ447Log.setTranKey(iTranKey);
				iJcicZ447Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ447LogService.insert(iJcicZ447Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z447)");
				}
				this.info("準備寫入L6932 ===== L8327(447)");
				JcicZ447Log uJcicZ447log = sJcicZ447LogService.ukeyFirst(iJcicZ447LogId.getUkey(), titaVo);
				this.info("uJcicZ447log      = " + uJcicZ447log);
				JcicZ447Log iJcicZ447Log2 = new JcicZ447Log();
				iJcicZ447Log2 = (JcicZ447Log) iDataLog.clone(iJcicZ447Log);
				this.info("iJcicZ447Log2     = " + iJcicZ447Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ447.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8429");
				iDataLog.setEnv(titaVo, iJcicZ447Log2, uJcicZ447log);
//				iDataLog.exec("L8429報送", iJcicZ447Log2.getUkey() + iJcicZ447Log2.getTxSeq());
				iDataLog.exec("L8429報送", iJcicZ447Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-448
	public void doZ448File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ448> rJcicZ448 = null;
		List<JcicZ448> zJcicZ448 = new ArrayList<JcicZ448>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ448Id jcicZ448Id = new JcicZ448Id();
		jcicZ448Id.setCustId(iCustId);
		jcicZ448Id.setApplyDate(iApplyDate);
		jcicZ448Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ448 = sJcicZ448Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ448 = rJcicZ448 == null ? null : rJcicZ448.getContent();
		if (rJcicZ448 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ448   = " + zJcicZ448);
		for (JcicZ448 sJcicZ448 : zJcicZ448) {
			if (sJcicZ448.getOutJcicTxtDate() == iJcicDate || sJcicZ448.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ448.getTranKey();
				String iSubmitKey = sJcicZ448.getSubmitKey();
				String iCustId = sJcicZ448.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ448.getApplyDate());
				String iCourtCode = sJcicZ448.getCourtCode();
				String iMaxMainCode = sJcicZ448.getMaxMainCode();
				String iSignPrin = String.valueOf(sJcicZ448.getSignPrin());
				int ixSignPrin = Integer.valueOf(sJcicZ448.getSignPrin());
				String iSignOther = String.valueOf(sJcicZ448.getSignOther());
				int ixSignOther = Integer.valueOf(sJcicZ448.getSignOther());
				String iOwnPercentage = String.valueOf(sJcicZ448.getOwnPercentage());
				BigDecimal ixOwnPercentage = sJcicZ448.getOwnPercentage();
				String iAcQuitAmt = String.valueOf(sJcicZ448.getAcQuitAmt());
				int ixAcQuitAmt = Integer.valueOf(sJcicZ448.getAcQuitAmt());
				String iUkey = sJcicZ448.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "448" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5) + iMaxMainCode
						+ StringUtils.leftPad(iSignPrin, 9, '0') + StringUtils.leftPad(iSignOther, 9, '0')
						+ StringUtils.leftPad(FormatRate(iOwnPercentage, 2), 6, '0')
						+ StringUtils.leftPad(iAcQuitAmt, 9, '0') + StringUtils.rightPad("", 22);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ448.setOutJcicTxtDate(sJcicZ448.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ448.setActualFilingDate(iDate);
					sJcicZ448.setActualFilingMark("Y");
				} else {
					sJcicZ448.setActualFilingDate(sJcicZ448.getActualFilingDate());
					sJcicZ448.setActualFilingMark("N");
				}
				try {
					sJcicZ448Service.update(sJcicZ448, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ448Log iJcicZ448Log = new JcicZ448Log();
				JcicZ448LogId iJcicZ448LogId = new JcicZ448LogId();
				this.info("TxSeq===" + titaVo.getTxSeq());
				iJcicZ448LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ448LogId.setUkey(iUkey);
				iJcicZ448Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ448Log.setUkey(iUkey);
				iJcicZ448Log.setJcicZ448LogId(iJcicZ448LogId);
				iJcicZ448Log.setSignPrin(ixSignPrin);
				iJcicZ448Log.setSignOther(ixSignOther);
				iJcicZ448Log.setOwnPercentage(ixOwnPercentage);
				iJcicZ448Log.setAcQuitAmt(ixAcQuitAmt);
				iJcicZ448Log.setTranKey(iTranKey);
				iJcicZ448Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ448LogService.insert(iJcicZ448Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z448)");
				}
				this.info("準備寫入L6932 ===== L8328(448)");
				JcicZ448Log uJcicZ448log = sJcicZ448LogService.ukeyFirst(iJcicZ448LogId.getUkey(), titaVo);
				this.info("uJcicZ448log      = " + uJcicZ448log);
				JcicZ448Log iJcicZ448Log2 = new JcicZ448Log();
				iJcicZ448Log2 = (JcicZ448Log) iDataLog.clone(iJcicZ448Log);
				this.info("iJcicZ448Log2     = " + iJcicZ448Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ448.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8430");
				iDataLog.setEnv(titaVo, iJcicZ448Log2, uJcicZ448log);
//				iDataLog.exec("L8430報送", iJcicZ448Log2.getUkey() + iJcicZ448Log2.getTxSeq());
				iDataLog.exec("L8430報送", iJcicZ448Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-450
	public void doZ450File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ450> rJcicZ450 = null;
		List<JcicZ450> zJcicZ450 = new ArrayList<JcicZ450>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ450Id jcicZ450Id = new JcicZ450Id();
		jcicZ450Id.setCustId(iCustId);
		jcicZ450Id.setApplyDate(iApplyDate);
		jcicZ450Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ450 = sJcicZ450Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ450 = rJcicZ450 == null ? null : rJcicZ450.getContent();
		if (rJcicZ450 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ450   = " + zJcicZ450);
		for (JcicZ450 sJcicZ450 : zJcicZ450) {
			if (sJcicZ450.getOutJcicTxtDate() == iJcicDate || sJcicZ450.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ450.getTranKey();
				String iSubmitKey = sJcicZ450.getSubmitKey();
				String iCustId = sJcicZ450.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ450.getApplyDate());
				String iCourtCode = sJcicZ450.getCourtCode();
				String iPayDate = String.valueOf(sJcicZ450.getPayDate());
				String iPayAmt = String.valueOf(sJcicZ450.getPayAmt());
				String iSumRepayActualAmt = String.valueOf(sJcicZ450.getSumRepayActualAmt());
				String iSumRepayShouldAmt = String.valueOf(sJcicZ450.getSumRepayShouldAmt());
				String iPayStatus = sJcicZ450.getPayStatus();
				int ixPayAmt = Integer.valueOf(sJcicZ450.getPayAmt());
				int ixSumRepayActualAmt = Integer.valueOf(sJcicZ450.getSumRepayActualAmt());
				int ixSumRepayShouldAmt = Integer.valueOf(sJcicZ450.getSumRepayShouldAmt());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ450.getUkey();
				String text = "450" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5)
						+ StringUtils.leftPad(iPayDate, 7, '0') + StringUtils.leftPad(iPayAmt, 9, '0')
						+ StringUtils.leftPad(iSumRepayActualAmt, 9, '0')
						+ StringUtils.leftPad(iSumRepayShouldAmt, 9, '0') + StringUtils.rightPad(iPayStatus, 1, "")
						+ StringUtils.rightPad("", 23);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ450.setOutJcicTxtDate(sJcicZ450.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ450.setActualFilingDate(iDate);
					sJcicZ450.setActualFilingMark("Y");
				} else {
					sJcicZ450.setActualFilingDate(sJcicZ450.getActualFilingDate());
					sJcicZ450.setActualFilingMark("N");
				}
				try {
					sJcicZ450Service.update(sJcicZ450, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ450Log iJcicZ450Log = new JcicZ450Log();
				JcicZ450LogId iJcicZ450LogId = new JcicZ450LogId();
				iJcicZ450LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ450LogId.setUkey(iUkey);
				iJcicZ450Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ450Log.setUkey(iUkey);
				iJcicZ450Log.setJcicZ450LogId(iJcicZ450LogId);
				iJcicZ450Log.setPayAmt(ixPayAmt);
				iJcicZ450Log.setSumRepayActualAmt(ixSumRepayActualAmt);
				iJcicZ450Log.setSumRepayShouldAmt(ixSumRepayShouldAmt);
				iJcicZ450Log.setPayStatus(iPayStatus);
				iJcicZ450Log.setTranKey(iTranKey);
				iJcicZ450Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ450LogService.insert(iJcicZ450Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z451)");
				}
				this.info("準備寫入L6932 ===== L8329(450)");
				JcicZ450Log uJcicZ450log = sJcicZ450LogService.ukeyFirst(iJcicZ450LogId.getUkey(), titaVo);
				this.info("uJcicZ450log      = " + uJcicZ450log);
				JcicZ450Log iJcicZ450Log2 = new JcicZ450Log();
				iJcicZ450Log2 = (JcicZ450Log) iDataLog.clone(iJcicZ450Log);
				this.info("iJcicZ450Log2     = " + iJcicZ450Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ450.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8431");
				iDataLog.setEnv(titaVo, iJcicZ450Log2, uJcicZ450log);
//				iDataLog.exec("L8431報送", iJcicZ450Log2.getUkey() + iJcicZ450Log2.getTxSeq());
				iDataLog.exec("L8431報送", iJcicZ450Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-451
	public void doZ451File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ451> rJcicZ451 = null;
		List<JcicZ451> zJcicZ451 = new ArrayList<JcicZ451>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ451Id jcicZ451Id = new JcicZ451Id();
		jcicZ451Id.setCustId(iCustId);
		jcicZ451Id.setApplyDate(iApplyDate);
		jcicZ451Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ451 = sJcicZ451Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ451 = rJcicZ451 == null ? null : rJcicZ451.getContent();
		if (rJcicZ451 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ451   = " + zJcicZ451);
		for (JcicZ451 sJcicZ451 : zJcicZ451) {
			if (sJcicZ451.getOutJcicTxtDate() == iJcicDate || sJcicZ451.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ451.getTranKey();
				String iSubmitKey = sJcicZ451.getSubmitKey();
				String iCustId = sJcicZ451.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ451.getApplyDate());
				String iCourtCode = sJcicZ451.getCourtCode();
				String iDelayYM = FormateYM(sJcicZ451.getDelayYM());
				String iDelayCode = sJcicZ451.getDelayCode();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ451.getUkey();
				String text = "451" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5)
						+ StringUtils.rightPad(iDelayCode, 1, "") + StringUtils.leftPad(iDelayYM, 5, '0')
						+ StringUtils.rightPad("", 12);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ451.setOutJcicTxtDate(sJcicZ451.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ451.setActualFilingDate(iDate);
					sJcicZ451.setActualFilingMark("Y");
				} else {
					sJcicZ451.setActualFilingDate(sJcicZ451.getActualFilingDate());
					sJcicZ451.setActualFilingMark("N");
				}
				try {
					sJcicZ451Service.update(sJcicZ451, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ451Log iJcicZ451Log = new JcicZ451Log();
				JcicZ451LogId iJcicZ451LogId = new JcicZ451LogId();
				iJcicZ451LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ451LogId.setUkey(iUkey);
				iJcicZ451Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ451Log.setUkey(iUkey);
				iJcicZ451Log.setJcicZ451LogId(iJcicZ451LogId);
				iJcicZ451Log.setDelayCode(iDelayCode);
				iJcicZ451Log.setTranKey(iTranKey);
				iJcicZ451Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ451LogService.insert(iJcicZ451Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z451)");
				}
				this.info("準備寫入L6932 ===== L8330(451)");
				JcicZ451Log uJcicZ451log = sJcicZ451LogService.ukeyFirst(iJcicZ451LogId.getUkey(), titaVo);
				this.info("uJcicZ451log      = " + uJcicZ451log);
				JcicZ451Log iJcicZ451Log2 = new JcicZ451Log();
				iJcicZ451Log2 = (JcicZ451Log) iDataLog.clone(iJcicZ451Log);
				this.info("iJcicZ451Log2     = " + iJcicZ451Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ451.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8432");
				iDataLog.setEnv(titaVo, iJcicZ451Log2, uJcicZ451log);
//				iDataLog.exec("L8432報送", iJcicZ451Log2.getUkey() + iJcicZ451Log2.getTxSeq());
				iDataLog.exec("L8432報送", iJcicZ451Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-454
	public void doZ454File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ454> rJcicZ454 = null;
		List<JcicZ454> zJcicZ454 = new ArrayList<JcicZ454>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ454Id jcicZ454Id = new JcicZ454Id();
		jcicZ454Id.setCustId(iCustId);
		jcicZ454Id.setApplyDate(iApplyDate);
		jcicZ454Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ454 = sJcicZ454Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ454 = rJcicZ454 == null ? null : rJcicZ454.getContent();
		if (rJcicZ454 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ454   = " + zJcicZ454);
		for (JcicZ454 sJcicZ454 : zJcicZ454) {
			if (sJcicZ454.getOutJcicTxtDate() == iJcicDate || sJcicZ454.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ454.getTranKey();
				String iSubmitKey = sJcicZ454.getSubmitKey();
				String iCustId = sJcicZ454.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ454.getApplyDate());
				String iCourtCode = sJcicZ454.getCourtCode();
				String iMaxMainCode = sJcicZ454.getMaxMainCode();
				String iPayOffResult = sJcicZ454.getPayOffResult();
				String iPayOffDate = String.valueOf(sJcicZ454.getPayOffDate());
				int ixPayOffDate = Integer.valueOf(sJcicZ454.getPayOffDate());
				String iUkey = sJcicZ454.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "454" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iCourtCode, 3, "") + StringUtils.rightPad("", 5) + iMaxMainCode
						+ StringUtils.rightPad(iPayOffResult, 1, "") + StringUtils.leftPad(iPayOffDate, 7, '0')
						+ StringUtils.rightPad("", 37);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ454.setOutJcicTxtDate(sJcicZ454.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ454.setActualFilingDate(iDate);
					sJcicZ454.setActualFilingMark("Y");
				} else {
					sJcicZ454.setActualFilingDate(sJcicZ454.getActualFilingDate());
					sJcicZ454.setActualFilingMark("N");
				}
				try {
					sJcicZ454Service.update(sJcicZ454, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ454Log iJcicZ454Log = new JcicZ454Log();
				JcicZ454LogId iJcicZ454LogId = new JcicZ454LogId();
				iJcicZ454LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ454LogId.setUkey(iUkey);
				iJcicZ454Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ454Log.setUkey(iUkey);
				iJcicZ454Log.setJcicZ454LogId(iJcicZ454LogId);
				iJcicZ454Log.setPayOffResult(iPayOffResult);
				iJcicZ454Log.setPayOffDate(ixPayOffDate);
				iJcicZ454Log.setTranKey(iTranKey);
				iJcicZ454Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ454LogService.insert(iJcicZ454Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z454)");
				}
				this.info("準備寫入L6932 ===== L8331(454)");
				JcicZ454Log uJcicZ454log = sJcicZ454LogService.ukeyFirst(iJcicZ454LogId.getUkey(), titaVo);
				this.info("uJcicZ454log      = " + uJcicZ454log);
				JcicZ454Log iJcicZ454Log2 = new JcicZ454Log();
				iJcicZ454Log2 = (JcicZ454Log) iDataLog.clone(iJcicZ454Log);
				this.info("iJcicZ454Log2     = " + iJcicZ454Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ454.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8433");
				iDataLog.setEnv(titaVo, iJcicZ454Log2, uJcicZ454log);
//				iDataLog.exec("L8433報送", iJcicZ454Log2.getUkey() + iJcicZ454Log2.getTxSeq());
				iDataLog.exec("L8433報送", iJcicZ454Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-570
	public void doZ570File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ570> rJcicZ570 = null;
		List<JcicZ570> zJcicZ570 = new ArrayList<JcicZ570>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ570Id jcicZ570Id = new JcicZ570Id();
		jcicZ570Id.setCustId(iCustId);
		jcicZ570Id.setApplyDate(iApplyDate);
		jcicZ570Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ570 = sJcicZ570Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ570 = rJcicZ570 == null ? null : rJcicZ570.getContent();
		if (rJcicZ570 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ570   = " + zJcicZ570);
		for (JcicZ570 sJcicZ570 : zJcicZ570) {
			if (sJcicZ570.getOutJcicTxtDate() == iJcicDate || sJcicZ570.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ570.getTranKey();
				String iSubmitKey = sJcicZ570.getSubmitKey();
				String iCustId = sJcicZ570.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ570.getApplyDate());
				String iAdjudicateDate = String.valueOf(sJcicZ570.getAdjudicateDate());
				String iBankCount = String.valueOf(sJcicZ570.getBankCount());
				int ixAdjudicateDate = Integer.valueOf(sJcicZ570.getAdjudicateDate());
				int ixBankCount = Integer.valueOf(sJcicZ570.getBankCount());
				String iBank1 = sJcicZ570.getBank1();
				String iBank2 = sJcicZ570.getBank2();
				String iBank3 = sJcicZ570.getBank3();
				String iBank4 = sJcicZ570.getBank4();
				String iBank5 = sJcicZ570.getBank5();
				String iBank6 = sJcicZ570.getBank6();
				String iBank7 = sJcicZ570.getBank7();
				String iBank8 = sJcicZ570.getBank8();
				String iBank9 = sJcicZ570.getBank9();
				String iBank10 = sJcicZ570.getBank10();
				String iBank11 = sJcicZ570.getBank11();
				String iBank12 = sJcicZ570.getBank12();
				String iBank13 = sJcicZ570.getBank13();
				String iBank14 = sJcicZ570.getBank14();
				String iBank15 = sJcicZ570.getBank15();
				String iBank16 = sJcicZ570.getBank16();
				String iBank17 = sJcicZ570.getBank17();
				String iBank18 = sJcicZ570.getBank18();
				String iBank19 = sJcicZ570.getBank19();
				String iBank20 = sJcicZ570.getBank20();
				String iBank21 = sJcicZ570.getBank21();
				String iBank22 = sJcicZ570.getBank22();
				String iBank23 = sJcicZ570.getBank23();
				String iBank24 = sJcicZ570.getBank24();
				String iBank25 = sJcicZ570.getBank25();
				String iBank26 = sJcicZ570.getBank26();
				String iBank27 = sJcicZ570.getBank27();
				String iBank28 = sJcicZ570.getBank28();
				String iBank29 = sJcicZ570.getBank29();
				String iBank30 = sJcicZ570.getBank30();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iUkey = sJcicZ570.getUkey();
				String text = "570" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.leftPad(iAdjudicateDate, 7, '0') + StringUtils.leftPad(iBankCount, 2, '0')
						+ StringUtils.rightPad(iBank1, 3, "") + StringUtils.rightPad(iBank2, 3, "")
						+ StringUtils.rightPad(iBank3, 3, "") + StringUtils.rightPad(iBank4, 3, "")
						+ StringUtils.rightPad(iBank5, 3, "") + StringUtils.rightPad(iBank6, 3, "")
						+ StringUtils.rightPad(iBank7, 3, "") + StringUtils.rightPad(iBank8, 3, "")
						+ StringUtils.rightPad(iBank9, 3, "") + StringUtils.rightPad(iBank10, 3, "")
						+ StringUtils.rightPad(iBank11, 3, "") + StringUtils.rightPad(iBank12, 3, "")
						+ StringUtils.rightPad(iBank13, 3, "") + StringUtils.rightPad(iBank14, 3, "")
						+ StringUtils.rightPad(iBank15, 3, "") + StringUtils.rightPad(iBank16, 3, "")
						+ StringUtils.rightPad(iBank17, 3, "") + StringUtils.rightPad(iBank18, 3, "")
						+ StringUtils.rightPad(iBank19, 3, "") + StringUtils.rightPad(iBank20, 3, "")
						+ StringUtils.rightPad(iBank21, 3, "") + StringUtils.rightPad(iBank22, 3, "")
						+ StringUtils.rightPad(iBank23, 3, "") + StringUtils.rightPad(iBank24, 3, "")
						+ StringUtils.rightPad(iBank25, 3, "") + StringUtils.rightPad(iBank26, 3, "")
						+ StringUtils.rightPad(iBank27, 3, "") + StringUtils.rightPad(iBank28, 3, "")
						+ StringUtils.rightPad(iBank29, 3, "") + StringUtils.rightPad(iBank30, 3, "")
						+ StringUtils.rightPad("", 27);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ570.setOutJcicTxtDate(sJcicZ570.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ570.setActualFilingDate(iDate);
					sJcicZ570.setActualFilingMark("Y");
				} else {
					sJcicZ570.setActualFilingDate(sJcicZ570.getActualFilingDate());
					sJcicZ570.setActualFilingMark("N");
				}
				try {
					sJcicZ570Service.update(sJcicZ570, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ570Log iJcicZ570Log = new JcicZ570Log();
				JcicZ570LogId iJcicZ570LogId = new JcicZ570LogId();
				iJcicZ570LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ570LogId.setUkey(iUkey);
				iJcicZ570Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ570Log.setUkey(iUkey);
				iJcicZ570Log.setJcicZ570LogId(iJcicZ570LogId);
				iJcicZ570Log.setAdjudicateDate(ixAdjudicateDate);
				iJcicZ570Log.setBankCount(ixBankCount);
				iJcicZ570Log.setBank1(iBank1);
				iJcicZ570Log.setBank2(iBank2);
				iJcicZ570Log.setBank3(iBank3);
				iJcicZ570Log.setBank4(iBank4);
				iJcicZ570Log.setBank5(iBank5);
				iJcicZ570Log.setBank6(iBank6);
				iJcicZ570Log.setBank7(iBank7);
				iJcicZ570Log.setBank8(iBank8);
				iJcicZ570Log.setBank9(iBank9);
				iJcicZ570Log.setBank10(iBank10);
				iJcicZ570Log.setBank11(iBank11);
				iJcicZ570Log.setBank12(iBank12);
				iJcicZ570Log.setBank13(iBank13);
				iJcicZ570Log.setBank14(iBank14);
				iJcicZ570Log.setBank15(iBank15);
				iJcicZ570Log.setBank16(iBank16);
				iJcicZ570Log.setBank17(iBank17);
				iJcicZ570Log.setBank18(iBank18);
				iJcicZ570Log.setBank19(iBank19);
				iJcicZ570Log.setBank20(iBank20);
				iJcicZ570Log.setBank21(iBank21);
				iJcicZ570Log.setBank22(iBank22);
				iJcicZ570Log.setBank23(iBank23);
				iJcicZ570Log.setBank24(iBank24);
				iJcicZ570Log.setBank25(iBank25);
				iJcicZ570Log.setBank26(iBank26);
				iJcicZ570Log.setBank27(iBank27);
				iJcicZ570Log.setBank28(iBank28);
				iJcicZ570Log.setBank29(iBank29);
				iJcicZ570Log.setBank30(iBank30);
				iJcicZ570Log.setTranKey(iTranKey);
				iJcicZ570Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ570LogService.insert(iJcicZ570Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z570)");
				}
				this.info("準備寫入L6932 ===== L8332(570)");
				JcicZ570Log uJcicZ570log = sJcicZ570LogService.ukeyFirst(iJcicZ570LogId.getUkey(), titaVo);
				this.info("uJcicZ570log      = " + uJcicZ570log);
				JcicZ570Log iJcicZ570Log2 = new JcicZ570Log();
				iJcicZ570Log2 = (JcicZ570Log) iDataLog.clone(iJcicZ570Log);
				this.info("iJcicZ570Log2     = " + iJcicZ570Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ570.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8434");
				iDataLog.setEnv(titaVo, iJcicZ570Log2, uJcicZ570log);
//				iDataLog.exec("L8434報送", iJcicZ570Log2.getUkey() + iJcicZ570Log2.getTxSeq());
				iDataLog.exec("L8434報送", iJcicZ570Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-571
	public void doZ571File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ571> rJcicZ571 = null;
		List<JcicZ571> zJcicZ571 = new ArrayList<JcicZ571>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ571Id jcicZ571Id = new JcicZ571Id();
		jcicZ571Id.setCustId(iCustId);
		jcicZ571Id.setApplyDate(iApplyDate);
		jcicZ571Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ571 = sJcicZ571Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ571 = rJcicZ571 == null ? null : rJcicZ571.getContent();
		if (rJcicZ571 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ571   = " + zJcicZ571);
		for (JcicZ571 sJcicZ571 : zJcicZ571) {
			if (sJcicZ571.getOutJcicTxtDate() == iJcicDate || sJcicZ571.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ571.getTranKey();
				String iSubmitKey = sJcicZ571.getSubmitKey();
				String iCustId = sJcicZ571.getCustId();
				String iBankId = sJcicZ571.getBankId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ571.getApplyDate());
				String iXOwnerAmt = String.valueOf(sJcicZ571.getOwnerAmt());
				String iXAllotAmt = String.valueOf(sJcicZ571.getAllotAmt());
				String iXUnallotAmt = String.valueOf(sJcicZ571.getUnallotAmt());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String iOwnerYn = String.valueOf(sJcicZ571.getOwnerYn());
				String iPayYn = String.valueOf(sJcicZ571.getPayYn());
				int iOwnerAmt = Integer.valueOf(sJcicZ571.getOwnerAmt());
				int iAllotAmt = Integer.valueOf(sJcicZ571.getAllotAmt());
				int iUnallotAmt = Integer.valueOf(sJcicZ571.getUnallotAmt());
				String iUkey = sJcicZ571.getUkey();
				// 組成檔案內容，並產生檔案
				String text = "571" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iBankId, 3, "") + StringUtils.rightPad(iOwnerYn, 1, "")
						+ StringUtils.rightPad(iPayYn, 1, "") + StringUtils.leftPad(iXOwnerAmt, 9, '0')
						+ StringUtils.leftPad(iXAllotAmt, 9, '0') + StringUtils.leftPad(iXUnallotAmt, 9, '0')
						+ StringUtils.rightPad("", 44);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ571.setOutJcicTxtDate(sJcicZ571.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ571.setActualFilingDate(iDate);
					sJcicZ571.setActualFilingMark("Y");
				} else {
					sJcicZ571.setActualFilingDate(sJcicZ571.getActualFilingDate());
					sJcicZ571.setActualFilingMark("N");
				}
				try {
					sJcicZ571Service.update(sJcicZ571, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ571Log iJcicZ571Log = new JcicZ571Log();
				JcicZ571LogId iJcicZ571LogId = new JcicZ571LogId();
				iJcicZ571LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ571LogId.setUkey(iUkey);
				iJcicZ571Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ571Log.setUkey(iUkey);
				iJcicZ571Log.setJcicZ571LogId(iJcicZ571LogId);
				iJcicZ571Log.setOwnerYn(iOwnerYn);
				iJcicZ571Log.setPayYn(iPayYn);
				iJcicZ571Log.setOwnerAmt(iOwnerAmt);
				iJcicZ571Log.setAllotAmt(iAllotAmt);
				iJcicZ571Log.setUnallotAmt(iUnallotAmt);
				iJcicZ571Log.setTranKey(iTranKey);
				iJcicZ571Log.setOutJcicTxtDate(iDate);
				try {
					sJcicZ571LogService.insert(iJcicZ571Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z571)");
				}
				this.info("準備寫入L6932 ===== L8333(571)");
				JcicZ571Log uJcicZ571log = sJcicZ571LogService.ukeyFirst(iJcicZ571LogId.getUkey(), titaVo);
				this.info("uJcicZ571log      = " + uJcicZ571log);
				JcicZ571Log iJcicZ571Log2 = new JcicZ571Log();
				iJcicZ571Log2 = (JcicZ571Log) iDataLog.clone(iJcicZ571Log);
				this.info("iJcicZ571Log2     = " + iJcicZ571Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ571.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8435");
				iDataLog.setEnv(titaVo, iJcicZ571Log2, uJcicZ571log);
//				iDataLog.exec("L8435報送", iJcicZ571Log2.getUkey() + iJcicZ571Log2.getTxSeq());
				iDataLog.exec("L8435報送", iJcicZ571Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-572
	public void doZ572File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ572> rJcicZ572 = null;
		List<JcicZ572> zJcicZ572 = new ArrayList<JcicZ572>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ572Id jcicZ572Id = new JcicZ572Id();
		jcicZ572Id.setCustId(iCustId);
		jcicZ572Id.setApplyDate(iApplyDate);
		jcicZ572Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ572 = sJcicZ572Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ572 = rJcicZ572 == null ? null : rJcicZ572.getContent();
		if (rJcicZ572 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ572   = " + zJcicZ572);
		for (JcicZ572 sJcicZ572 : zJcicZ572) {
			if (sJcicZ572.getOutJcicTxtDate() == iJcicDate || sJcicZ572.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ572.getTranKey();
				String iSubmitKey = sJcicZ572.getSubmitKey();
				String iCustId = sJcicZ572.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ572.getApplyDate());
				String iStartDate = String.valueOf(sJcicZ572.getStartDate());
				int ixStartDate = Integer.valueOf(sJcicZ572.getStartDate());
				String iPayDate = String.valueOf(sJcicZ572.getPayDate());
				String iBankId = sJcicZ572.getBankId();
				String iAllotAmt = String.valueOf(sJcicZ572.getAllotAmt());
				int ixAllotAmt = Integer.valueOf(sJcicZ572.getAllotAmt());
				String iOwnPercentage = String.valueOf(sJcicZ572.getOwnPercentage());
				BigDecimal ixOwnPercentage = sJcicZ572.getOwnPercentage();
				String iUkey = sJcicZ572.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "572" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.leftPad(iStartDate, 7, '0') + StringUtils.leftPad(iPayDate, 7, '0')
						+ StringUtils.rightPad(iBankId, 3, "") + StringUtils.leftPad(iAllotAmt, 9, '0')
						+ StringUtils.leftPad(FormatRate(iOwnPercentage, 2), 6, '0') + StringUtils.rightPad("", 44);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ572.setOutJcicTxtDate(sJcicZ572.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ572.setActualFilingDate(iDate);
					sJcicZ572.setActualFilingMark("Y");
				} else {
					sJcicZ572.setActualFilingDate(sJcicZ572.getActualFilingDate());
					sJcicZ572.setActualFilingMark("N");
				}
				try {
					sJcicZ572Service.update(sJcicZ572, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ572Log iJcicZ572Log = new JcicZ572Log();
				JcicZ572LogId iJcicZ572LogId = new JcicZ572LogId();
				iJcicZ572LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ572LogId.setUkey(iUkey);
				iJcicZ572Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ572Log.setUkey(iUkey);
				iJcicZ572Log.setJcicZ572LogId(iJcicZ572LogId);
				iJcicZ572Log.setStartDate(ixStartDate);
				iJcicZ572Log.setAllotAmt(ixAllotAmt);
				iJcicZ572Log.setOwnPercentage(ixOwnPercentage);
				iJcicZ572Log.setTranKey(iTranKey);
				iJcicZ572Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ572LogService.insert(iJcicZ572Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z572)");
				}
				this.info("準備寫入L6932 ===== L8334(572)");
				JcicZ572Log uJcicZ572log = sJcicZ572LogService.ukeyFirst(iJcicZ572LogId.getUkey(), titaVo);
				this.info("uJcicZ572log      = " + uJcicZ572log);
				JcicZ572Log iJcicZ572Log2 = new JcicZ572Log();
				iJcicZ572Log2 = (JcicZ572Log) iDataLog.clone(iJcicZ572Log);
				this.info("iJcicZ572Log2     = " + iJcicZ572Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ572.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8436");
				iDataLog.setEnv(titaVo, iJcicZ572Log2, uJcicZ572log);
//				iDataLog.exec("L8436報送", iJcicZ572Log2.getUkey() + iJcicZ572Log2.getTxSeq());
				iDataLog.exec("L8436報送", iJcicZ572Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-573
	public void doZ573File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ573> rJcicZ573 = null;
		List<JcicZ573> zJcicZ573 = new ArrayList<JcicZ573>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ573Id jcicZ573Id = new JcicZ573Id();
		jcicZ573Id.setCustId(iCustId);
		jcicZ573Id.setApplyDate(iApplyDate);
		jcicZ573Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
			
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
			rJcicZ573 = sJcicZ573Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0, Integer.MAX_VALUE,
					titaVo);
			zJcicZ573 = rJcicZ573 == null ? null : rJcicZ573.getContent();
			if (rJcicZ573 == null) {
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
		this.info("zJcicZ573   = " + zJcicZ573);
		for (JcicZ573 sJcicZ573 : zJcicZ573) {
			if (sJcicZ573.getOutJcicTxtDate() == iJcicDate || sJcicZ573.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ573.getTranKey();
				String iSubmitKey = sJcicZ573.getSubmitKey();
				String iCustId = sJcicZ573.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ573.getApplyDate());
				String iPayDate = String.valueOf(sJcicZ573.getPayDate());
				String iPayAmt = String.valueOf(sJcicZ573.getPayAmt());
				int ixPayAmt = Integer.valueOf(sJcicZ573.getPayAmt());
				String iTotalPayAmt = String.valueOf(sJcicZ573.getTotalPayAmt());
				int ixTotalPayAmt = Integer.valueOf(sJcicZ573.getTotalPayAmt());
				String iUkey = sJcicZ573.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "573" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.leftPad(iPayDate, 7, '0') + StringUtils.leftPad(iPayAmt, 9, '0')
						+ StringUtils.leftPad(iTotalPayAmt, 9, '0') + StringUtils.rightPad("", 51);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ573.setOutJcicTxtDate(sJcicZ573.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ573.setActualFilingDate(iDate);
					sJcicZ573.setActualFilingMark("Y");
				} else {
					sJcicZ573.setActualFilingDate(sJcicZ573.getActualFilingDate());
					sJcicZ573.setActualFilingMark("N");
				}
				try {
					sJcicZ573Service.update(sJcicZ573, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ573Log iJcicZ573Log = new JcicZ573Log();
				JcicZ573LogId iJcicZ573LogId = new JcicZ573LogId();
				iJcicZ573LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ573LogId.setUkey(iUkey);
				iJcicZ573Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ573Log.setUkey(iUkey);
				iJcicZ573Log.setJcicZ573LogId(iJcicZ573LogId);
				iJcicZ573Log.setPayAmt(ixPayAmt);
				iJcicZ573Log.setTotalPayAmt(ixTotalPayAmt);
				iJcicZ573Log.setTranKey(iTranKey);
				iJcicZ573Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ573LogService.insert(iJcicZ573Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z573)");
				}
				this.info("準備寫入L6932 ===== L8335(573)");
				JcicZ573Log uJcicZ573log = sJcicZ573LogService.ukeyFirst(iJcicZ573LogId.getUkey(), titaVo);
				this.info("uJcicZ573log      = " + uJcicZ573log);
				JcicZ573Log iJcicZ573Log2 = new JcicZ573Log();
				iJcicZ573Log2 = (JcicZ573Log) iDataLog.clone(iJcicZ573Log);
				this.info("iJcicZ573Log2     = " + iJcicZ573Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ573.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8437");
				iDataLog.setEnv(titaVo, iJcicZ573Log2, uJcicZ573log);
//				iDataLog.exec("L8437報送", iJcicZ573Log2.getUkey() + iJcicZ573Log2.getTxSeq());
				iDataLog.exec("L8437報送", iJcicZ573Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-574
	public void doZ574File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ574> rJcicZ574 = null;
		List<JcicZ574> zJcicZ574 = new ArrayList<JcicZ574>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ574Id jcicZ574Id = new JcicZ574Id();
		jcicZ574Id.setCustId(iCustId);
		jcicZ574Id.setApplyDate(iApplyDate);
		jcicZ574Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate+ 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ574 = sJcicZ574Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0, Integer.MAX_VALUE,
				titaVo);
		zJcicZ574 = rJcicZ574 == null ? null : rJcicZ574.getContent();
		if (rJcicZ574 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}
		this.info("zJcicZ574   = " + zJcicZ574);
		for (JcicZ574 sJcicZ574 : zJcicZ574) {
			if (sJcicZ574.getOutJcicTxtDate() == iJcicDate || sJcicZ574.getActualFilingDate() == iJcicDate) {
				String iTranKey = sJcicZ574.getTranKey();
				String iSubmitKey = sJcicZ574.getSubmitKey();
				String iCustId = sJcicZ574.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ574.getApplyDate());
				String iCloseDate = String.valueOf(sJcicZ574.getCloseDate());
				int ixCloseDate = Integer.valueOf(sJcicZ574.getCloseDate());
				String iCloseMark = sJcicZ574.getCloseMark();
				String iPhoneNo = sJcicZ574.getPhoneNo();
				String iUkey = sJcicZ574.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "574" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.leftPad(iCloseDate, 7, '0') + StringUtils.rightPad(iCloseMark, 2, "")
						+ StringUtils.rightPad(iPhoneNo, 16, "") + StringUtils.rightPad("", 31);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ574.setOutJcicTxtDate(sJcicZ574.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ574.setActualFilingDate(iDate);
					sJcicZ574.setActualFilingMark("Y");
				} else {
					sJcicZ574.setActualFilingDate(sJcicZ574.getActualFilingDate());
					sJcicZ574.setActualFilingMark("N");
				}
				try {
					sJcicZ574Service.update(sJcicZ574, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ574Log iJcicZ574Log = new JcicZ574Log();
				JcicZ574LogId iJcicZ574LogId = new JcicZ574LogId();
				iJcicZ574LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ574LogId.setUkey(iUkey);
				iJcicZ574Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ574Log.setUkey(iUkey);
				iJcicZ574Log.setJcicZ574LogId(iJcicZ574LogId);
				iJcicZ574Log.setCloseDate(ixCloseDate);
				iJcicZ574Log.setCloseMark(iCloseMark);
				iJcicZ574Log.setPhoneNo(iPhoneNo);
				iJcicZ574Log.setTranKey(iTranKey);
				iJcicZ574Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ574LogService.insert(iJcicZ574Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z574)");
				}
				this.info("準備寫入L6932 ===== L8336(574)");
				JcicZ574Log uJcicZ574log = sJcicZ574LogService.ukeyFirst(iJcicZ574LogId.getUkey(), titaVo);
				this.info("uJcicZ574log      = " + uJcicZ574log);
				JcicZ574Log iJcicZ574Log2 = new JcicZ574Log();
				iJcicZ574Log2 = (JcicZ574Log) iDataLog.clone(iJcicZ574Log);
				this.info("iJcicZ574Log2     = " + iJcicZ574Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ574.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8438");
				iDataLog.setEnv(titaVo, iJcicZ574Log2, uJcicZ574log);
//				iDataLog.exec("L8438報送", iJcicZ574Log2.getUkey() + iJcicZ574Log2.getTxSeq());
				iDataLog.exec("L8438報送", iJcicZ574Log2.getUkey());
//				iCount++;
			}
		}
	}

//jcic-575
	public void doZ575File(TitaVo titaVo) throws LogicException {
		Slice<JcicZ575> rJcicZ575 = null;
		List<JcicZ575> zJcicZ575 = new ArrayList<JcicZ575>();

		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		iCustId = titaVo.getParam("CustId");
		iSubmitKey = titaVo.get("SubmitKey");
		iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));

		JcicZ575Id jcicZ575Id = new JcicZ575Id();
		jcicZ575Id.setCustId(iCustId);
		jcicZ575Id.setApplyDate(iApplyDate);
		jcicZ575Id.setSubmitKey(iSubmitKey);

		if (iSubmitType != 3) {
			iActualFilingDate = 0;
			iActualFilingMark = "N";
		} else {
			iActualFilingDate = iJcicDate + 19110000;
			iActualFilingMark = "Y";
		}
		rJcicZ575 = sJcicZ575Service.findkeyFilingDate(iActualFilingDate, iActualFilingMark, 0,
				Integer.MAX_VALUE, titaVo);
		zJcicZ575 = rJcicZ575 == null ? null : rJcicZ575.getContent();
		if (rJcicZ575 == null) {
			throw new LogicException(titaVo, "E2003", "查無可轉出資料");
		}

		this.info("zJcicZ575   = " + zJcicZ575);
		for (JcicZ575 sJcicZ575 : zJcicZ575) {
			if (sJcicZ575.getOutJcicTxtDate() == iJcicDate || sJcicZ575.getActualFilingDate() == iJcicDate) {

				String iTranKey = sJcicZ575.getTranKey();
				String iSubmitKey = sJcicZ575.getSubmitKey();
				String iCustId = sJcicZ575.getCustId();
				iTranKey = FormatUtil.padX(iTranKey, 1);
				iSubmitKey = FormatUtil.padX(iSubmitKey, 3);
				iCustId = FormatUtil.padX(iCustId, 10);
				String iApplyDate = String.valueOf(sJcicZ575.getApplyDate());
				String iBankId = sJcicZ575.getBankId();
				String iModifyType = sJcicZ575.getModifyType();
				String iUkey = sJcicZ575.getUkey();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				// 組成檔案內容，並產生檔案
				String text = "575" + iTranKey + iSubmitKey + iCustId + StringUtils.leftPad(iApplyDate, 7, '0')
						+ StringUtils.rightPad(iModifyType, 1, "") + StringUtils.rightPad(iBankId, 3, "")
						+ StringUtils.rightPad("", 52);
				this.put(text);

				// 檔案產生後，回填JcicDate
				sJcicZ575.setOutJcicTxtDate(sJcicZ575.getOutJcicTxtDate());
				if (iSubmitType == 3) {
					sJcicZ575.setActualFilingDate(iDate);
					sJcicZ575.setActualFilingMark("Y");
				} else {
					sJcicZ575.setActualFilingDate(sJcicZ575.getActualFilingDate());
					sJcicZ575.setActualFilingMark("N");
				}
				try {
					sJcicZ575Service.update(sJcicZ575, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}

				// 回填JcicDate後寫入Log檔
				JcicZ575Log iJcicZ575Log = new JcicZ575Log();
				JcicZ575LogId iJcicZ575LogId = new JcicZ575LogId();
				iJcicZ575LogId.setTxSeq(titaVo.getTxSeq());
				iJcicZ575LogId.setUkey(iUkey);
				iJcicZ575Log.setTxSeq(titaVo.getTxSeq());
				iJcicZ575Log.setUkey(iUkey);
				iJcicZ575Log.setJcicZ575LogId(iJcicZ575LogId);
				iJcicZ575Log.setModifyType(iModifyType);
				iJcicZ575Log.setTranKey(iTranKey);
				iJcicZ575Log.setOutJcicTxtDate(iDate);

				try {
					sJcicZ575LogService.insert(iJcicZ575Log, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z575)");
				}
				this.info("準備寫入L6932 ===== L8337(575)");
				JcicZ575Log uJcicZ575log = sJcicZ575LogService.ukeyFirst(iJcicZ575LogId.getUkey(), titaVo);
				this.info("uJcicZ575log      = " + uJcicZ575log);
				JcicZ575Log iJcicZ575Log2 = new JcicZ575Log();
				iJcicZ575Log2 = (JcicZ575Log) iDataLog.clone(iJcicZ575Log);
				this.info("iJcicZ575Log2     = " + iJcicZ575Log2);
				CustMain tCustMain = sCustMainService.custIdFirst(sJcicZ575.getCustId(), titaVo);
				int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
				titaVo.putParam("CustNo", iCustNo);
				titaVo.putParam("TXCODE", "L8439");
				iDataLog.setEnv(titaVo, iJcicZ575Log2, uJcicZ575log);
//				iDataLog.exec("L8439報送", iJcicZ575Log2.getUkey() + iJcicZ575Log2.getTxSeq());
				iDataLog.exec("L8439報送", iJcicZ575Log2.getUkey());
//				iCount++;
			}
		}
	}

	private String FormatRate(String iRate, int decimal) {
		String formatRate = "";
		int indexPoint = iRate.indexOf(".");
		if (indexPoint == -1) {
			formatRate = iRate + ".00";
		} else {
			formatRate = StringUtils.substringBefore(iRate, ".") + "."
					+ StringUtils.rightPad(StringUtils.substringAfter(iRate, "."), decimal, "0");
		}
		return formatRate;
	}

	private String FormatRate2(String iRate, int decimal) {
		String formatRate = "";
		int indexPoint = iRate.indexOf(".");
		if (indexPoint == -1) {
			formatRate = iRate + ".0";
		} else {
			formatRate = StringUtils.substringBefore(iRate, ".") + "."
					+ StringUtils.rightPad(StringUtils.substringAfter(iRate, "."), decimal, "0");
		}
		return formatRate;
	}

	private String FormateYM(int YM) {

		String reYm = "";
		if (YM == 0) {
			reYm = String.valueOf(YM);
		} else {
			reYm = String.valueOf(YM - 191100);
		}
		return reYm;
	}

	private String FormateYear(int Year) {

		String reYear = "";
		if (Year == 0) {
			reYear = String.valueOf(Year);
		} else {
			reYear = String.valueOf(Year - 1911);
		}
		return reYear;
	}
}
