package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.CdCodeService;
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
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446LogService;
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
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L840A")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(040)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L840A extends TradeBuffer {

	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Autowired
	public WebClient webClient;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;

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
	int iPayOffDate;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L840A");
		this.info("titaVo    = " + titaVo);
		this.totaVo.init(titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		itranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');
		long sno1 = 0;
		if (iSubmitType == 1 || iSubmitType == 3) {
//			更新篩選資料日期
			setJcicDate(titaVo);

			if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {

				sno1 = iL8403File.exec(titaVo);

				String checkMsg = "Jcic債協報送檔案已產出。";
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
						titaVo.getTlrNo() + "L8403-" + itranCode, checkMsg, titaVo);
			}
		} else if (iSubmitType == 2) {

			this.info("進入到這裡");
			doRemoveJcicDate(titaVo);

		}
		totaVo.put("ExcelSnoM", "" + sno1);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setJcicDate(TitaVo titaVo) throws LogicException {
		this.info("setJcicDate");
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int count = 0;
		String tranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');
		this.info("tranCode    = " + tranCode);
		switch (tranCode) {
		case "040":
			Slice<JcicZ040> iJcicZ040 = null;
			JcicZ040 uJcicZ040 = new JcicZ040();
			iJcicZ040 = sJcicZ040Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ040Id jcicZ040Id = new JcicZ040Id();
			jcicZ040Id.setCustId(iCustId);
			jcicZ040Id.setRcDate(iRcDate);
			jcicZ040Id.setSubmitKey(iSubmitKey);
			JcicZ040 sJcicZ040 = new JcicZ040();
			sJcicZ040 = sJcicZ040Service.findById(jcicZ040Id, titaVo);
			for (JcicZ040 iiJcicZ040 : iJcicZ040) {
				if ((iiJcicZ040.getOutJcicTxtDate() == 0 || iiJcicZ040.getActualFilingDate() == 0
						|| iiJcicZ040.getActualFilingMark() == null)
						&& iiJcicZ040.getCustId().equals(sJcicZ040.getCustId())
						&& iiJcicZ040.getSubmitKey().equals(sJcicZ040.getSubmitKey())
						&& iiJcicZ040.getRcDate() == sJcicZ040.getRcDate()) {
					count++;
					uJcicZ040 = sJcicZ040Service.holdById(sJcicZ040.getJcicZ040Id(), titaVo);
					uJcicZ040.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ040.setActualFilingDate(iJcicDate);
						uJcicZ040.setActualFilingMark("Y");
					} else {
						uJcicZ040.setActualFilingDate(0);
						uJcicZ040.setActualFilingMark("");
					}
					try {
						sJcicZ040Service.update(uJcicZ040, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "041":
			Slice<JcicZ041> iJcicZ041 = null;
			JcicZ041 uJcicZ041 = new JcicZ041();
			iJcicZ041 = sJcicZ041Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ041Id jcicZ041Id = new JcicZ041Id();
			jcicZ041Id.setCustId(iCustId);
			jcicZ041Id.setRcDate(iRcDate);
			jcicZ041Id.setSubmitKey(iSubmitKey);
			JcicZ041 sJcicZ041 = new JcicZ041();
			sJcicZ041 = sJcicZ041Service.findById(jcicZ041Id, titaVo);
			for (JcicZ041 iiJcicZ041 : iJcicZ041) {
				if ((iiJcicZ041.getOutJcicTxtDate() == 0 || iiJcicZ041.getActualFilingDate() == 0
						|| iiJcicZ041.getActualFilingMark() == null)
						&& iiJcicZ041.getCustId().equals(sJcicZ041.getCustId())
						&& iiJcicZ041.getSubmitKey().equals(sJcicZ041.getSubmitKey())
						&& iiJcicZ041.getRcDate() == sJcicZ041.getRcDate()) {
					count++;
					uJcicZ041 = sJcicZ041Service.holdById(sJcicZ041.getJcicZ041Id(), titaVo);
					this.info("041iJcicDate     = " + iJcicDate);
					uJcicZ041.setOutJcicTxtDate(iJcicDate);
					this.info("041iJcicDate  upd   = " + uJcicZ041.getOutJcicTxtDate());
					if (iSubmitType == 3) {
						uJcicZ041.setActualFilingDate(iJcicDate);
						uJcicZ041.setActualFilingMark("Y");
					} else {
						uJcicZ041.setActualFilingDate(0);
						uJcicZ041.setActualFilingMark("");
					}
					try {
						sJcicZ041Service.update(uJcicZ041, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "042":
			Slice<JcicZ042> iJcicZ042 = null;
			JcicZ042 uJcicZ042 = new JcicZ042();
			iJcicZ042 = sJcicZ042Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ042Id jcicZ042Id = new JcicZ042Id();
			jcicZ042Id.setCustId(iCustId);
			jcicZ042Id.setRcDate(iRcDate);
			jcicZ042Id.setSubmitKey(iSubmitKey);
			jcicZ042Id.setMaxMainCode(iMaxMainCode);
			JcicZ042 sJcicZ042 = new JcicZ042();
			sJcicZ042 = sJcicZ042Service.findById(jcicZ042Id, titaVo);
			for (JcicZ042 iiJcicZ042 : iJcicZ042) {
				if ((iiJcicZ042.getOutJcicTxtDate() == 0 || iiJcicZ042.getActualFilingDate() == 0
						|| iiJcicZ042.getActualFilingMark() == null)
						&& iiJcicZ042.getCustId().equals(sJcicZ042.getCustId())
						&& iiJcicZ042.getSubmitKey().equals(sJcicZ042.getSubmitKey())
						&& iiJcicZ042.getMaxMainCode().equals(sJcicZ042.getMaxMainCode())
						&& iiJcicZ042.getRcDate() == sJcicZ042.getRcDate()) {
					count++;
					uJcicZ042 = sJcicZ042Service.holdById(sJcicZ042.getJcicZ042Id(), titaVo);
					uJcicZ042.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ042.setActualFilingDate(iJcicDate);
						uJcicZ042.setActualFilingMark("Y");
					} else {
						uJcicZ042.setActualFilingDate(0);
						uJcicZ042.setActualFilingMark("");
					}
					try {
						sJcicZ042Service.update(uJcicZ042, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "043":
			Slice<JcicZ043> iJcicZ043 = null;
			JcicZ043 uJcicZ043 = new JcicZ043();
			iJcicZ043 = sJcicZ043Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			iAccount = titaVo.getParam("Account");
			JcicZ043Id jcicZ043Id = new JcicZ043Id();
			jcicZ043Id.setCustId(iCustId);
			jcicZ043Id.setRcDate(iRcDate);
			jcicZ043Id.setSubmitKey(iSubmitKey);
			jcicZ043Id.setMaxMainCode(iMaxMainCode);
			jcicZ043Id.setAccount(iAccount);
			JcicZ043 sJcicZ043 = new JcicZ043();
			sJcicZ043 = sJcicZ043Service.findById(jcicZ043Id, titaVo);
			for (JcicZ043 iiJcicZ043 : iJcicZ043) {
				if ((iiJcicZ043.getOutJcicTxtDate() == 0 || iiJcicZ043.getActualFilingDate() == 0
						|| iiJcicZ043.getActualFilingMark() == null)
						&& iiJcicZ043.getCustId().equals(sJcicZ043.getCustId())
						&& iiJcicZ043.getSubmitKey().equals(sJcicZ043.getSubmitKey())
						&& iiJcicZ043.getRcDate() == sJcicZ043.getRcDate()
						&& iiJcicZ043.getMaxMainCode().equals(sJcicZ043.getMaxMainCode())
						&& iiJcicZ043.getAccount().equals(sJcicZ043.getAccount())) {
					count++;
					uJcicZ043 = sJcicZ043Service.holdById(sJcicZ043.getJcicZ043Id(), titaVo);
					uJcicZ043.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ043.setActualFilingDate(iJcicDate);
						uJcicZ043.setActualFilingMark("Y");
					} else {
						uJcicZ043.setActualFilingDate(0);
						uJcicZ043.setActualFilingMark("");
					}
					try {
						sJcicZ043Service.update(uJcicZ043, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "044":
			Slice<JcicZ044> iJcicZ044 = null;
			JcicZ044 uJcicZ044 = new JcicZ044();
			iJcicZ044 = sJcicZ044Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ044Id jcicZ044Id = new JcicZ044Id();
			jcicZ044Id.setCustId(iCustId);
			jcicZ044Id.setRcDate(iRcDate);
			jcicZ044Id.setSubmitKey(iSubmitKey);
			JcicZ044 sJcicZ044 = new JcicZ044();
			sJcicZ044 = sJcicZ044Service.findById(jcicZ044Id, titaVo);
			for (JcicZ044 iiJcicZ044 : iJcicZ044) {
				if ((iiJcicZ044.getOutJcicTxtDate() == 0 || iiJcicZ044.getActualFilingDate() == 0
						|| iiJcicZ044.getActualFilingMark() == null)
						&& iiJcicZ044.getCustId().equals(sJcicZ044.getCustId())
						&& iiJcicZ044.getSubmitKey().equals(sJcicZ044.getSubmitKey())
						&& iiJcicZ044.getRcDate() == sJcicZ044.getRcDate()) {
					count++;
					uJcicZ044 = sJcicZ044Service.holdById(sJcicZ044.getJcicZ044Id(), titaVo);
					uJcicZ044.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ044.setActualFilingDate(iJcicDate);
						uJcicZ044.setActualFilingMark("Y");
					} else {
						uJcicZ044.setActualFilingDate(0);
						uJcicZ044.setActualFilingMark("");
					}
					try {
						sJcicZ044Service.update(uJcicZ044, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "045":
			Slice<JcicZ045> iJcicZ045 = null;
			JcicZ045 uJcicZ045 = new JcicZ045();
			iJcicZ045 = sJcicZ045Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ045Id jcicZ045Id = new JcicZ045Id();
			jcicZ045Id.setCustId(iCustId);
			jcicZ045Id.setRcDate(iRcDate);
			jcicZ045Id.setSubmitKey(iSubmitKey);
			jcicZ045Id.setMaxMainCode(iMaxMainCode);
			JcicZ045 sJcicZ045 = new JcicZ045();
			sJcicZ045 = sJcicZ045Service.findById(jcicZ045Id, titaVo);
			for (JcicZ045 iiJcicZ045 : iJcicZ045) {
				if ((iiJcicZ045.getOutJcicTxtDate() == 0 || iiJcicZ045.getActualFilingDate() == 0
						|| iiJcicZ045.getActualFilingMark() == null)
						&& iiJcicZ045.getCustId().equals(sJcicZ045.getCustId())
						&& iiJcicZ045.getSubmitKey().equals(sJcicZ045.getSubmitKey())
						&& iiJcicZ045.getRcDate() == sJcicZ045.getRcDate()
						&& iiJcicZ045.getMaxMainCode().equals(sJcicZ045.getMaxMainCode())) {
					count++;
					uJcicZ045 = sJcicZ045Service.holdById(sJcicZ045.getJcicZ045Id(), titaVo);
					uJcicZ045.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ045.setActualFilingDate(iJcicDate);
						uJcicZ045.setActualFilingMark("Y");
					} else {
						uJcicZ045.setActualFilingDate(0);
						uJcicZ045.setActualFilingMark("");
					}
					try {
						sJcicZ045Service.update(uJcicZ045, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "046":
			Slice<JcicZ046> iJcicZ046 = null;
			JcicZ046 uJcicZ046 = new JcicZ046();
			iJcicZ046 = sJcicZ046Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate"));
			JcicZ046Id jcicZ046Id = new JcicZ046Id();
			jcicZ046Id.setCustId(iCustId);
			jcicZ046Id.setRcDate(iRcDate);
			jcicZ046Id.setSubmitKey(iSubmitKey);
			jcicZ046Id.setCloseDate(iCloseDate);
			JcicZ046 sJcicZ046 = new JcicZ046();
			sJcicZ046 = sJcicZ046Service.findById(jcicZ046Id, titaVo);
			for (JcicZ046 iiJcicZ046 : iJcicZ046) {
				if ((iiJcicZ046.getOutJcicTxtDate() == 0 || iiJcicZ046.getActualFilingDate() == 0
						|| iiJcicZ046.getActualFilingMark() == null)
						&& iiJcicZ046.getCustId().equals(sJcicZ046.getCustId())
						&& iiJcicZ046.getSubmitKey().equals(sJcicZ046.getSubmitKey())
						&& iiJcicZ046.getRcDate() == sJcicZ046.getRcDate()
						&& iiJcicZ046.getCloseDate() == sJcicZ046.getCloseDate()) {
					count++;
					uJcicZ046 = sJcicZ046Service.holdById(sJcicZ046.getJcicZ046Id(), titaVo);
					uJcicZ046.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ046.setActualFilingDate(iJcicDate);
						uJcicZ046.setActualFilingMark("Y");
					} else {
						uJcicZ046.setActualFilingDate(0);
						uJcicZ046.setActualFilingMark("");
					}
					try {
						sJcicZ046Service.update(uJcicZ046, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "047":
			Slice<JcicZ047> iJcicZ047 = null;
			JcicZ047 uJcicZ047 = new JcicZ047();
			iJcicZ047 = sJcicZ047Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ047Id jcicZ047Id = new JcicZ047Id();
			jcicZ047Id.setCustId(iCustId);
			jcicZ047Id.setRcDate(iRcDate);
			jcicZ047Id.setSubmitKey(iSubmitKey);
			JcicZ047 sJcicZ047 = new JcicZ047();
			sJcicZ047 = sJcicZ047Service.findById(jcicZ047Id, titaVo);
			for (JcicZ047 iiJcicZ047 : iJcicZ047) {
				if ((iiJcicZ047.getOutJcicTxtDate() == 0 || iiJcicZ047.getActualFilingDate() == 0
						|| iiJcicZ047.getActualFilingMark() == null)
						&& iiJcicZ047.getCustId().equals(sJcicZ047.getCustId())
						&& iiJcicZ047.getSubmitKey().equals(sJcicZ047.getSubmitKey())
						&& iiJcicZ047.getRcDate() == sJcicZ047.getRcDate()) {
					count++;
					uJcicZ047 = sJcicZ047Service.holdById(sJcicZ047.getJcicZ047Id(), titaVo);
					uJcicZ047.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ047.setActualFilingDate(iJcicDate);
						uJcicZ047.setActualFilingMark("Y");
					} else {
						uJcicZ047.setActualFilingDate(0);
						uJcicZ047.setActualFilingMark("");
					}
					try {
						sJcicZ047Service.update(uJcicZ047, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "048":
			Slice<JcicZ048> iJcicZ048 = null;
			JcicZ048 uJcicZ048 = new JcicZ048();
			iJcicZ048 = sJcicZ048Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ048Id jcicZ048Id = new JcicZ048Id();
			jcicZ048Id.setCustId(iCustId);
			jcicZ048Id.setRcDate(iRcDate);
			jcicZ048Id.setSubmitKey(iSubmitKey);
			JcicZ048 sJcicZ048 = new JcicZ048();
			sJcicZ048 = sJcicZ048Service.findById(jcicZ048Id, titaVo);
			for (JcicZ048 iiJcicZ048 : iJcicZ048) {
				if ((iiJcicZ048.getOutJcicTxtDate() == 0 || iiJcicZ048.getActualFilingDate() == 0
						|| iiJcicZ048.getActualFilingMark() == null)
						&& iiJcicZ048.getCustId().equals(sJcicZ048.getCustId())
						&& iiJcicZ048.getSubmitKey().equals(sJcicZ048.getSubmitKey())
						&& iiJcicZ048.getRcDate() == sJcicZ048.getRcDate()) {
					count++;
					uJcicZ048 = sJcicZ048Service.holdById(sJcicZ048.getJcicZ048Id(), titaVo);
					uJcicZ048.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ048.setActualFilingDate(iJcicDate);
						uJcicZ048.setActualFilingMark("Y");
					} else {
						uJcicZ048.setActualFilingDate(0);
						uJcicZ048.setActualFilingMark("");
					}
					try {
						sJcicZ048Service.update(uJcicZ048, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "049":
			Slice<JcicZ049> iJcicZ049 = null;
			JcicZ049 uJcicZ049 = new JcicZ049();
			iJcicZ049 = sJcicZ049Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ049Id jcicZ049Id = new JcicZ049Id();
			jcicZ049Id.setCustId(iCustId);
			jcicZ049Id.setRcDate(iRcDate);
			jcicZ049Id.setSubmitKey(iSubmitKey);
			JcicZ049 sJcicZ049 = new JcicZ049();
			sJcicZ049 = sJcicZ049Service.findById(jcicZ049Id, titaVo);
			for (JcicZ049 iiJcicZ049 : iJcicZ049) {
				if ((iiJcicZ049.getOutJcicTxtDate() == 0 || iiJcicZ049.getActualFilingDate() == 0
						|| iiJcicZ049.getActualFilingMark() == null)
						&& iiJcicZ049.getCustId().equals(sJcicZ049.getCustId())
						&& iiJcicZ049.getSubmitKey().equals(sJcicZ049.getSubmitKey())
						&& iiJcicZ049.getRcDate() == sJcicZ049.getRcDate()) {
					count++;
					uJcicZ049 = sJcicZ049Service.holdById(sJcicZ049.getJcicZ049Id(), titaVo);
					uJcicZ049.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ049.setActualFilingDate(iJcicDate);
						uJcicZ049.setActualFilingMark("Y");
					} else {
						uJcicZ049.setActualFilingDate(0);
						uJcicZ049.setActualFilingMark("");
					}
					try {
						sJcicZ049Service.update(uJcicZ049, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "050":
			Slice<JcicZ050> iJcicZ050 = null;
			JcicZ050 uJcicZ050 = new JcicZ050();
			iJcicZ050 = sJcicZ050Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iPayDate = Integer.valueOf(titaVo.getParam("PayDate"));
			JcicZ050Id jcicZ050Id = new JcicZ050Id();
			jcicZ050Id.setCustId(iCustId);
			jcicZ050Id.setRcDate(iRcDate);
			jcicZ050Id.setSubmitKey(iSubmitKey);
			jcicZ050Id.setPayDate(iPayDate);
			JcicZ050 sJcicZ050 = new JcicZ050();
			sJcicZ050 = sJcicZ050Service.findById(jcicZ050Id, titaVo);
			for (JcicZ050 iiJcicZ050 : iJcicZ050) {
				if ((iiJcicZ050.getOutJcicTxtDate() == 0 || iiJcicZ050.getActualFilingDate() == 0
						|| iiJcicZ050.getActualFilingMark() == null)
						&& iiJcicZ050.getCustId().equals(sJcicZ050.getCustId())
						&& iiJcicZ050.getSubmitKey().equals(sJcicZ050.getSubmitKey())
						&& iiJcicZ050.getRcDate() == sJcicZ050.getRcDate()
						&& iiJcicZ050.getPayDate() == sJcicZ050.getPayDate()) {
					count++;
					uJcicZ050 = sJcicZ050Service.holdById(sJcicZ050.getJcicZ050Id(), titaVo);
					uJcicZ050.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ050.setActualFilingDate(iJcicDate);
						uJcicZ050.setActualFilingMark("Y");
					} else {
						uJcicZ050.setActualFilingDate(0);
						uJcicZ050.setActualFilingMark("");
					}
					try {
						sJcicZ050Service.update(uJcicZ050, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "051":
			Slice<JcicZ051> iJcicZ051 = null;
			JcicZ051 uJcicZ051 = new JcicZ051();
			iJcicZ051 = sJcicZ051Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iDelayYM = Integer.valueOf(titaVo.get("DelayYM"));
			JcicZ051Id jcicZ051Id = new JcicZ051Id();
			jcicZ051Id.setCustId(iCustId);
			jcicZ051Id.setRcDate(iRcDate);
			jcicZ051Id.setSubmitKey(iSubmitKey);
			jcicZ051Id.setDelayYM(iDelayYM);
			JcicZ051 sJcicZ051 = new JcicZ051();
			sJcicZ051 = sJcicZ051Service.findById(jcicZ051Id, titaVo);
			for (JcicZ051 iiJcicZ051 : iJcicZ051) {
				if ((iiJcicZ051.getOutJcicTxtDate() == 0 || iiJcicZ051.getActualFilingDate() == 0
						|| iiJcicZ051.getActualFilingMark() == null)
						&& iiJcicZ051.getCustId().equals(sJcicZ051.getCustId())
						&& iiJcicZ051.getSubmitKey().equals(sJcicZ051.getSubmitKey())
						&& iiJcicZ051.getDelayYM() == sJcicZ051.getDelayYM()
						&& iiJcicZ051.getRcDate() == sJcicZ051.getRcDate()) {
					count++;
					uJcicZ051 = sJcicZ051Service.holdById(sJcicZ051.getJcicZ051Id(), titaVo);
					uJcicZ051.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ051.setActualFilingDate(iJcicDate);
						uJcicZ051.setActualFilingMark("Y");
					} else {
						uJcicZ051.setActualFilingDate(0);
						uJcicZ051.setActualFilingMark("");
					}
					try {
						sJcicZ051Service.update(uJcicZ051, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "052":
			Slice<JcicZ052> iJcicZ052 = null;
			JcicZ052 uJcicZ052 = new JcicZ052();
			iJcicZ052 = sJcicZ052Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ052Id jcicZ052Id = new JcicZ052Id();
			jcicZ052Id.setCustId(iCustId);
			jcicZ052Id.setRcDate(iRcDate);
			jcicZ052Id.setSubmitKey(iSubmitKey);
			JcicZ052 sJcicZ052 = new JcicZ052();
			sJcicZ052 = sJcicZ052Service.findById(jcicZ052Id, titaVo);
			for (JcicZ052 iiJcicZ052 : iJcicZ052) {
				if ((iiJcicZ052.getOutJcicTxtDate() == 0 || iiJcicZ052.getActualFilingDate() == 0
						|| iiJcicZ052.getActualFilingMark() == null)
						&& iiJcicZ052.getCustId().equals(sJcicZ052.getCustId())
						&& iiJcicZ052.getSubmitKey().equals(sJcicZ052.getSubmitKey())
						&& iiJcicZ052.getRcDate() == sJcicZ052.getRcDate()) {
					count++;
					uJcicZ052 = sJcicZ052Service.holdById(sJcicZ052.getJcicZ052Id(), titaVo);
					uJcicZ052.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ052.setActualFilingDate(iJcicDate);
						uJcicZ052.setActualFilingMark("Y");
					} else {
						uJcicZ052.setActualFilingDate(0);
						uJcicZ052.setActualFilingMark("");
					}
					try {
						sJcicZ052Service.update(uJcicZ052, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "053":
			Slice<JcicZ053> iJcicZ053 = null;
			JcicZ053 uJcicZ053 = new JcicZ053();
			iJcicZ053 = sJcicZ053Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ053Id jcicZ053Id = new JcicZ053Id();
			jcicZ053Id.setCustId(iCustId);
			jcicZ053Id.setRcDate(iRcDate);
			jcicZ053Id.setSubmitKey(iSubmitKey);
			jcicZ053Id.setMaxMainCode(iMaxMainCode);
			JcicZ053 sJcicZ053 = new JcicZ053();
			sJcicZ053 = sJcicZ053Service.findById(jcicZ053Id, titaVo);
			for (JcicZ053 iiJcicZ053 : iJcicZ053) {
				if ((iiJcicZ053.getOutJcicTxtDate() == 0 || iiJcicZ053.getActualFilingDate() == 0
						|| iiJcicZ053.getActualFilingMark() == null)
						&& iiJcicZ053.getCustId().equals(sJcicZ053.getCustId())
						&& iiJcicZ053.getSubmitKey().equals(sJcicZ053.getSubmitKey())
						&& iiJcicZ053.getRcDate() == sJcicZ053.getRcDate()
						&& iiJcicZ053.getMaxMainCode().equals(sJcicZ053.getMaxMainCode())) {
					count++;
					uJcicZ053 = sJcicZ053Service.holdById(sJcicZ053.getJcicZ053Id(), titaVo);
					uJcicZ053.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ053.setActualFilingDate(iJcicDate);
						uJcicZ053.setActualFilingMark("Y");
					} else {
						uJcicZ053.setActualFilingDate(0);
						uJcicZ053.setActualFilingMark("");
					}
					try {
						sJcicZ053Service.update(uJcicZ053, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "054":
			Slice<JcicZ054> iJcicZ054 = null;
			JcicZ054 uJcicZ054 = new JcicZ054();
			iJcicZ054 = sJcicZ054Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			iPayOffDate = Integer.valueOf(titaVo.getParam("PayOffDate"));
			JcicZ054Id jcicZ054Id = new JcicZ054Id();
			jcicZ054Id.setCustId(iCustId);
			jcicZ054Id.setRcDate(iRcDate);
			jcicZ054Id.setSubmitKey(iSubmitKey);
			jcicZ054Id.setMaxMainCode(iMaxMainCode);
			jcicZ054Id.setPayOffDate(iPayOffDate);
			JcicZ054 sJcicZ054 = new JcicZ054();
			sJcicZ054 = sJcicZ054Service.findById(jcicZ054Id, titaVo);
			for (JcicZ054 iiJcicZ054 : iJcicZ054) {
				if ((iiJcicZ054.getOutJcicTxtDate() == 0 || iiJcicZ054.getActualFilingDate() == 0
						|| iiJcicZ054.getActualFilingMark() == null)
						&& iiJcicZ054.getCustId().equals(sJcicZ054.getCustId())
						&& iiJcicZ054.getSubmitKey().equals(sJcicZ054.getSubmitKey())
						&& iiJcicZ054.getRcDate() == sJcicZ054.getRcDate()
						&& iiJcicZ054.getMaxMainCode().equals(sJcicZ054.getMaxMainCode())) {
					count++;
					uJcicZ054 = sJcicZ054Service.holdById(sJcicZ054.getJcicZ054Id(), titaVo);
					uJcicZ054.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ054.setActualFilingDate(iJcicDate);
						uJcicZ054.setActualFilingMark("Y");
					} else {
						uJcicZ054.setActualFilingDate(0);
						uJcicZ054.setActualFilingMark("");
					}
					try {
						sJcicZ054Service.update(uJcicZ054, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "055":
			Slice<JcicZ055> iJcicZ055 = null;
			JcicZ055 uJcicZ055 = new JcicZ055();
			iJcicZ055 = sJcicZ055Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iCaseStatus = titaVo.getParam("CaseStatus");
			iClaimDate = Integer.valueOf(titaVo.get("ClaimDate"));
			iCourtCode = titaVo.getParam("CourtCode");
			JcicZ055Id jcicZ055Id = new JcicZ055Id();
			jcicZ055Id.setCustId(iCustId);
			jcicZ055Id.setSubmitKey(iSubmitKey);
			jcicZ055Id.setCaseStatus(iCaseStatus);
			jcicZ055Id.setClaimDate(iClaimDate);
			jcicZ055Id.setCourtCode(iCourtCode);
			JcicZ055 sJcicZ055 = new JcicZ055();
			sJcicZ055 = sJcicZ055Service.findById(jcicZ055Id, titaVo);
			for (JcicZ055 iiJcicZ055 : iJcicZ055) {
				if ((iiJcicZ055.getOutJcicTxtDate() == 0 || iiJcicZ055.getActualFilingDate() == 0
						|| iiJcicZ055.getActualFilingMark() == null)
						&& iiJcicZ055.getCustId().equals(sJcicZ055.getCustId())
						&& iiJcicZ055.getSubmitKey().equals(sJcicZ055.getSubmitKey())
						&& iiJcicZ055.getCaseStatus().equals(sJcicZ055.getCaseStatus())
						&& iiJcicZ055.getCourtCode().equals(sJcicZ055.getCourtCode())
						&& iiJcicZ055.getClaimDate() == sJcicZ055.getClaimDate()) {
					count++;
					uJcicZ055 = sJcicZ055Service.holdById(sJcicZ055.getJcicZ055Id(), titaVo);
					uJcicZ055.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ055.setActualFilingDate(iJcicDate);
						uJcicZ055.setActualFilingMark("Y");
					} else {
						uJcicZ055.setActualFilingDate(0);
						uJcicZ055.setActualFilingMark("");
					}
					try {
						sJcicZ055Service.update(uJcicZ055, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "056":
			Slice<JcicZ056> iJcicZ056 = null;
			JcicZ056 uJcicZ056 = new JcicZ056();
			iJcicZ056 = sJcicZ056Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iCaseStatus = titaVo.getParam("CaseStatus");
			iClaimDate = Integer.valueOf(titaVo.get("ClaimDate"));
			iCourtCode = titaVo.getParam("CourtCode");
			JcicZ056Id jcicZ056Id = new JcicZ056Id();
			jcicZ056Id.setCustId(iCustId);
			jcicZ056Id.setSubmitKey(iSubmitKey);
			jcicZ056Id.setCaseStatus(iCaseStatus);
			jcicZ056Id.setClaimDate(iClaimDate);
			jcicZ056Id.setCourtCode(iCourtCode);
			JcicZ056 sJcicZ056 = new JcicZ056();
			sJcicZ056 = sJcicZ056Service.findById(jcicZ056Id, titaVo);
			for (JcicZ056 iiJcicZ056 : iJcicZ056) {
				if ((iiJcicZ056.getOutJcicTxtDate() == 0 || iiJcicZ056.getActualFilingDate() == 0
						|| iiJcicZ056.getActualFilingMark() == null)
						&& iiJcicZ056.getCustId().equals(sJcicZ056.getCustId())
						&& iiJcicZ056.getSubmitKey().equals(sJcicZ056.getSubmitKey())
						&& iiJcicZ056.getCaseStatus().equals(sJcicZ056.getCaseStatus())
						&& iiJcicZ056.getCourtCode().equals(sJcicZ056.getCourtCode())
						&& iiJcicZ056.getClaimDate() == sJcicZ056.getClaimDate()) {
					count++;
					uJcicZ056 = sJcicZ056Service.holdById(sJcicZ056.getJcicZ056Id(), titaVo);
					uJcicZ056.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ056.setActualFilingDate(iJcicDate);
						uJcicZ056.setActualFilingMark("Y");
					} else {
						uJcicZ056.setActualFilingDate(0);
						uJcicZ056.setActualFilingMark("");
					}
					try {
						sJcicZ056Service.update(uJcicZ056, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "060":
			Slice<JcicZ060> iJcicZ060 = null;
			JcicZ060 uJcicZ060 = new JcicZ060();
			iJcicZ060 = sJcicZ060Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			JcicZ060Id jcicZ060Id = new JcicZ060Id();
			jcicZ060Id.setCustId(iCustId);
			jcicZ060Id.setSubmitKey(iSubmitKey);
			jcicZ060Id.setRcDate(iRcDate);
			jcicZ060Id.setChangePayDate(iChangePayDate);
			JcicZ060 sJcicZ060 = new JcicZ060();
			sJcicZ060 = sJcicZ060Service.findById(jcicZ060Id, titaVo);
			for (JcicZ060 iiJcicZ060 : iJcicZ060) {
				if ((iiJcicZ060.getOutJcicTxtDate() == 0 || iiJcicZ060.getActualFilingDate() == 0
						|| iiJcicZ060.getActualFilingMark() == null)
						&& iiJcicZ060.getCustId().equals(sJcicZ060.getCustId())
						&& iiJcicZ060.getSubmitKey().equals(sJcicZ060.getSubmitKey())
						&& iiJcicZ060.getRcDate() == sJcicZ060.getRcDate()
						&& iiJcicZ060.getChangePayDate() == sJcicZ060.getChangePayDate()) {
					count++;
					uJcicZ060 = sJcicZ060Service.holdById(sJcicZ060.getJcicZ060Id(), titaVo);
					uJcicZ060.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ060.setActualFilingDate(iJcicDate);
						uJcicZ060.setActualFilingMark("Y");
					} else {
						uJcicZ060.setActualFilingDate(0);
						uJcicZ060.setActualFilingMark("");
					}
					try {
						sJcicZ060Service.update(uJcicZ060, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "061":
			Slice<JcicZ061> iJcicZ061 = null;
			JcicZ061 uJcicZ061 = new JcicZ061();
			iJcicZ061 = sJcicZ061Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ061Id jcicZ061Id = new JcicZ061Id();
			jcicZ061Id.setCustId(iCustId);
			jcicZ061Id.setSubmitKey(iSubmitKey);
			jcicZ061Id.setRcDate(iRcDate);
			jcicZ061Id.setChangePayDate(iChangePayDate);
			jcicZ061Id.setMaxMainCode(iMaxMainCode);
			JcicZ061 sJcicZ061 = new JcicZ061();
			sJcicZ061 = sJcicZ061Service.findById(jcicZ061Id, titaVo);
			for (JcicZ061 iiJcicZ061 : iJcicZ061) {
				if ((iiJcicZ061.getOutJcicTxtDate() == 0 || iiJcicZ061.getActualFilingDate() == 0
						|| iiJcicZ061.getActualFilingMark() == null)
						&& iiJcicZ061.getCustId().equals(sJcicZ061.getCustId())
						&& iiJcicZ061.getSubmitKey().equals(sJcicZ061.getSubmitKey())
						&& iiJcicZ061.getRcDate() == sJcicZ061.getRcDate()
						&& iiJcicZ061.getChangePayDate() == sJcicZ061.getChangePayDate()) {
					count++;
					uJcicZ061 = sJcicZ061Service.holdById(sJcicZ061.getJcicZ061Id(), titaVo);
					uJcicZ061.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ061.setActualFilingDate(iJcicDate);
						uJcicZ061.setActualFilingMark("Y");
					} else {
						uJcicZ061.setActualFilingDate(0);
						uJcicZ061.setActualFilingMark("");
					}
					try {
						sJcicZ061Service.update(uJcicZ061, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "062":
			Slice<JcicZ062> iJcicZ062 = null;
			JcicZ062 uJcicZ062 = new JcicZ062();
			iJcicZ062 = sJcicZ062Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			JcicZ062Id jcicZ062Id = new JcicZ062Id();
			jcicZ062Id.setCustId(iCustId);
			jcicZ062Id.setSubmitKey(iSubmitKey);
			jcicZ062Id.setRcDate(iRcDate);
			jcicZ062Id.setChangePayDate(iChangePayDate);
			JcicZ062 sJcicZ062 = new JcicZ062();
			sJcicZ062 = sJcicZ062Service.findById(jcicZ062Id, titaVo);
			for (JcicZ062 iiJcicZ062 : iJcicZ062) {
				if ((iiJcicZ062.getOutJcicTxtDate() == 0 || iiJcicZ062.getActualFilingDate() == 0
						|| iiJcicZ062.getActualFilingMark() == null)
						&& iiJcicZ062.getCustId().equals(sJcicZ062.getCustId())
						&& iiJcicZ062.getSubmitKey().equals(sJcicZ062.getSubmitKey())
						&& iiJcicZ062.getRcDate() == sJcicZ062.getRcDate()
						&& iiJcicZ062.getChangePayDate() == sJcicZ062.getChangePayDate()) {
					count++;
					uJcicZ062 = sJcicZ062Service.holdById(sJcicZ062.getJcicZ062Id(), titaVo);
					uJcicZ062.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ062.setActualFilingDate(iJcicDate);
						uJcicZ062.setActualFilingMark("Y");
					} else {
						uJcicZ062.setActualFilingDate(0);
						uJcicZ062.setActualFilingMark("");
					}
					try {
						sJcicZ062Service.update(uJcicZ062, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "063":
			Slice<JcicZ063> iJcicZ063 = null;
			JcicZ063 uJcicZ063 = new JcicZ063();
			iJcicZ063 = sJcicZ063Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			JcicZ063Id jcicZ063Id = new JcicZ063Id();
			jcicZ063Id.setCustId(iCustId);
			jcicZ063Id.setSubmitKey(iSubmitKey);
			jcicZ063Id.setRcDate(iRcDate);
			jcicZ063Id.setChangePayDate(iChangePayDate);
			JcicZ063 sJcicZ063 = new JcicZ063();
			sJcicZ063 = sJcicZ063Service.findById(jcicZ063Id, titaVo);
			for (JcicZ063 iiJcicZ063 : iJcicZ063) {
				if ((iiJcicZ063.getOutJcicTxtDate() == 0 || iiJcicZ063.getActualFilingDate() == 0
						|| iiJcicZ063.getActualFilingMark() == null)
						&& iiJcicZ063.getCustId().equals(sJcicZ063.getCustId())
						&& iiJcicZ063.getSubmitKey().equals(sJcicZ063.getSubmitKey())
						&& iiJcicZ063.getRcDate() == sJcicZ063.getRcDate()
						&& iiJcicZ063.getChangePayDate() == sJcicZ063.getChangePayDate()) {
					count++;
					uJcicZ063 = sJcicZ063Service.holdById(sJcicZ063.getJcicZ063Id(), titaVo);
					uJcicZ063.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ063.setActualFilingDate(iJcicDate);
						uJcicZ063.setActualFilingMark("Y");
					} else {
						uJcicZ063.setActualFilingDate(0);
						uJcicZ063.setActualFilingMark("");
					}
					try {
						sJcicZ063Service.update(uJcicZ063, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "440":
			Slice<JcicZ440> iJcicZ440 = null;
			JcicZ440 uJcicZ440 = new JcicZ440();
			iJcicZ440 = sJcicZ440Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ440Id jcicZ440Id = new JcicZ440Id();
			jcicZ440Id.setCustId(iCustId);
			jcicZ440Id.setSubmitKey(iSubmitKey);
			jcicZ440Id.setApplyDate(iApplyDate);
			jcicZ440Id.setCourtCode(iCourtCode);
			JcicZ440 sJcicZ440 = new JcicZ440();
			sJcicZ440 = sJcicZ440Service.findById(jcicZ440Id, titaVo);
			for (JcicZ440 iiJcicZ440 : iJcicZ440) {
				if ((iiJcicZ440.getOutJcicTxtDate() == 0 || iiJcicZ440.getActualFilingDate() == 0
						|| iiJcicZ440.getActualFilingMark() == null)
						&& iiJcicZ440.getCustId().equals(sJcicZ440.getCustId())
						&& iiJcicZ440.getSubmitKey().equals(sJcicZ440.getSubmitKey())
						&& iiJcicZ440.getApplyDate() == sJcicZ440.getApplyDate()
						&& iiJcicZ440.getCourtCode().equals(sJcicZ440.getCourtCode())
						) {
					count++;
					uJcicZ440 = sJcicZ440Service.holdById(sJcicZ440.getJcicZ440Id(), titaVo);
					uJcicZ440.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ440.setActualFilingDate(iJcicDate);
						uJcicZ440.setActualFilingMark("Y");
					} else {
						uJcicZ440.setActualFilingDate(0);
						uJcicZ440.setActualFilingMark("");
					}
					try {
						sJcicZ440Service.update(uJcicZ440, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "442":
			Slice<JcicZ442> iJcicZ442 = null;
			JcicZ442 uJcicZ442 = new JcicZ442();
			iJcicZ442 = sJcicZ442Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			JcicZ442Id jcicZ442Id = new JcicZ442Id();
			jcicZ442Id.setCustId(iCustId);
			jcicZ442Id.setSubmitKey(iSubmitKey);
			jcicZ442Id.setApplyDate(iApplyDate);
			jcicZ442Id.setCourtCode(iCourtCode);
			jcicZ442Id.setMaxMainCode(iMaxMainCode);
			JcicZ442 sJcicZ442 = new JcicZ442();
			sJcicZ442 = sJcicZ442Service.findById(jcicZ442Id, titaVo);
			for (JcicZ442 iiJcicZ442 : iJcicZ442) {
				if ((iiJcicZ442.getOutJcicTxtDate() == 0 || iiJcicZ442.getActualFilingDate() == 0
						|| iiJcicZ442.getActualFilingMark() == null)
						&& iiJcicZ442.getCustId().equals(sJcicZ442.getCustId())
						&& iiJcicZ442.getSubmitKey().equals(sJcicZ442.getSubmitKey())
						&& iiJcicZ442.getApplyDate() == sJcicZ442.getApplyDate()
						&& iiJcicZ442.getCourtCode().equals(sJcicZ442.getCourtCode())
						&& iiJcicZ442.getMaxMainCode().equals(sJcicZ442.getMaxMainCode())) {
					count++;
					uJcicZ442 = sJcicZ442Service.holdById(sJcicZ442.getJcicZ442Id(), titaVo);
					uJcicZ442.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ442.setActualFilingDate(iJcicDate);
						uJcicZ442.setActualFilingMark("Y");
					} else {
						uJcicZ442.setActualFilingDate(0);
						uJcicZ442.setActualFilingMark("");
					}
					try {
						sJcicZ442Service.update(uJcicZ442, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "443":
			Slice<JcicZ443> iJcicZ443 = null;
			JcicZ443 uJcicZ443 = new JcicZ443();
			iJcicZ443 = sJcicZ443Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			iAccount = titaVo.get("Account");
			JcicZ443Id jcicZ443Id = new JcicZ443Id();
			jcicZ443Id.setCustId(iCustId);
			jcicZ443Id.setSubmitKey(iSubmitKey);
			jcicZ443Id.setApplyDate(iApplyDate);
			jcicZ443Id.setCourtCode(iCourtCode);
			jcicZ443Id.setMaxMainCode(iMaxMainCode);
			jcicZ443Id.setAccount(iAccount);
			JcicZ443 sJcicZ443 = new JcicZ443();
			sJcicZ443 = sJcicZ443Service.findById(jcicZ443Id, titaVo);
			for (JcicZ443 iiJcicZ443 : iJcicZ443) {
				if ((iiJcicZ443.getOutJcicTxtDate() == 0 || iiJcicZ443.getActualFilingDate() == 0
						|| iiJcicZ443.getActualFilingMark() == null)
						&& iiJcicZ443.getCustId().equals(sJcicZ443.getCustId())
						&& iiJcicZ443.getSubmitKey().equals(sJcicZ443.getSubmitKey())
						&& iiJcicZ443.getApplyDate() == sJcicZ443.getApplyDate()
						&& iiJcicZ443.getCourtCode().equals(sJcicZ443.getCourtCode())
						&& iiJcicZ443.getMaxMainCode().equals(sJcicZ443.getMaxMainCode())
						) {
					count++;
					uJcicZ443 = sJcicZ443Service.holdById(sJcicZ443.getJcicZ443Id(), titaVo);
					uJcicZ443.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ443.setActualFilingDate(iJcicDate);
						uJcicZ443.setActualFilingMark("Y");
					} else {
						uJcicZ443.setActualFilingDate(0);
						uJcicZ443.setActualFilingMark("");
					}
					try {
						sJcicZ443Service.update(uJcicZ443, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "444":
			Slice<JcicZ444> iJcicZ444 = null;
			JcicZ444 uJcicZ444 = new JcicZ444();
			iJcicZ444 = sJcicZ444Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ444Id jcicZ444Id = new JcicZ444Id();
			jcicZ444Id.setCustId(iCustId);
			jcicZ444Id.setSubmitKey(iSubmitKey);
			jcicZ444Id.setApplyDate(iApplyDate);
			jcicZ444Id.setCourtCode(iCourtCode);
			JcicZ444 sJcicZ444 = new JcicZ444();
			sJcicZ444 = sJcicZ444Service.findById(jcicZ444Id, titaVo);
			for (JcicZ444 iiJcicZ444 : iJcicZ444) {
				if ((iiJcicZ444.getOutJcicTxtDate() == 0 || iiJcicZ444.getActualFilingDate() == 0
						|| iiJcicZ444.getActualFilingMark() == null)
						&& iiJcicZ444.getCustId().equals(sJcicZ444.getCustId())
						&& iiJcicZ444.getSubmitKey().equals(sJcicZ444.getSubmitKey())
						&& iiJcicZ444.getApplyDate() == sJcicZ444.getApplyDate()
						&& iiJcicZ444.getCourtCode().equals(sJcicZ444.getCourtCode())) {
					count++;
					uJcicZ444 = sJcicZ444Service.holdById(sJcicZ444.getJcicZ444Id(), titaVo);
					uJcicZ444.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ444.setActualFilingDate(iJcicDate);
						uJcicZ444.setActualFilingMark("Y");
					} else {
						uJcicZ444.setActualFilingDate(0);
						uJcicZ444.setActualFilingMark("");
					}
					try {
						sJcicZ444Service.update(uJcicZ444, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "446":
			Slice<JcicZ446> iJcicZ446 = null;
			JcicZ446 uJcicZ446 = new JcicZ446();
			iJcicZ446 = sJcicZ446Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ446Id jcicZ446Id = new JcicZ446Id();
			jcicZ446Id.setCustId(iCustId);
			jcicZ446Id.setSubmitKey(iSubmitKey);
			jcicZ446Id.setApplyDate(iApplyDate);
			jcicZ446Id.setCourtCode(iCourtCode);
			JcicZ446 sJcicZ446 = new JcicZ446();
			sJcicZ446 = sJcicZ446Service.findById(jcicZ446Id, titaVo);
			for (JcicZ446 iiJcicZ446 : iJcicZ446) {
				if ((iiJcicZ446.getOutJcicTxtDate() == 0 || iiJcicZ446.getActualFilingDate() == 0
						|| iiJcicZ446.getActualFilingMark() == null)
						&& iiJcicZ446.getCustId().equals(sJcicZ446.getCustId())
						&& iiJcicZ446.getSubmitKey().equals(sJcicZ446.getSubmitKey())
						&& iiJcicZ446.getApplyDate() == sJcicZ446.getApplyDate()
						&& iiJcicZ446.getCourtCode().equals(sJcicZ446.getCourtCode())) {
					count++;
					uJcicZ446 = sJcicZ446Service.holdById(sJcicZ446.getJcicZ446Id(), titaVo);
					uJcicZ446.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ446.setActualFilingDate(iJcicDate);
						uJcicZ446.setActualFilingMark("Y");
					} else {
						uJcicZ446.setActualFilingDate(0);
						uJcicZ446.setActualFilingMark("");
					}
					try {
						sJcicZ446Service.update(uJcicZ446, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "447":
			Slice<JcicZ447> iJcicZ447 = null;
			JcicZ447 uJcicZ447 = new JcicZ447();
			iJcicZ447 = sJcicZ447Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ447Id jcicZ447Id = new JcicZ447Id();
			jcicZ447Id.setCustId(iCustId);
			jcicZ447Id.setSubmitKey(iSubmitKey);
			jcicZ447Id.setApplyDate(iApplyDate);
			jcicZ447Id.setCourtCode(iCourtCode);
			JcicZ447 sJcicZ447 = new JcicZ447();
			sJcicZ447 = sJcicZ447Service.findById(jcicZ447Id, titaVo);
			for (JcicZ447 iiJcicZ447 : iJcicZ447) {
				if ((iiJcicZ447.getOutJcicTxtDate() == 0 || iiJcicZ447.getActualFilingDate() == 0
						|| iiJcicZ447.getActualFilingMark() == null)
						&& iiJcicZ447.getCustId().equals(sJcicZ447.getCustId())
						&& iiJcicZ447.getSubmitKey().equals(sJcicZ447.getSubmitKey())
						&& iiJcicZ447.getApplyDate() == sJcicZ447.getApplyDate()
						&& iiJcicZ447.getCourtCode().equals(sJcicZ447.getCourtCode())) {
					count++;
					uJcicZ447 = sJcicZ447Service.holdById(sJcicZ447.getJcicZ447Id(), titaVo);
					uJcicZ447.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ447.setActualFilingDate(iJcicDate);
						uJcicZ447.setActualFilingMark("Y");
					} else {
						uJcicZ447.setActualFilingDate(0);
						uJcicZ447.setActualFilingMark("");
					}
					try {
						sJcicZ447Service.update(uJcicZ447, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "448":
			Slice<JcicZ448> iJcicZ448 = null;
			JcicZ448 uJcicZ448 = new JcicZ448();
			iJcicZ448 = sJcicZ448Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			JcicZ448Id jcicZ448Id = new JcicZ448Id();
			jcicZ448Id.setCustId(iCustId);
			jcicZ448Id.setSubmitKey(iSubmitKey);
			jcicZ448Id.setApplyDate(iApplyDate);
			jcicZ448Id.setCourtCode(iCourtCode);
			jcicZ448Id.setMaxMainCode(iMaxMainCode);
			JcicZ448 sJcicZ448 = new JcicZ448();
			sJcicZ448 = sJcicZ448Service.findById(jcicZ448Id, titaVo);
			for (JcicZ448 iiJcicZ448 : iJcicZ448) {
				if ((iiJcicZ448.getOutJcicTxtDate() == 0 || iiJcicZ448.getActualFilingDate() == 0
						|| iiJcicZ448.getActualFilingMark() == null)
						&& iiJcicZ448.getCustId().equals(sJcicZ448.getCustId())
						&& iiJcicZ448.getSubmitKey().equals(sJcicZ448.getSubmitKey())
						&& iiJcicZ448.getApplyDate() == sJcicZ448.getApplyDate()
						&& iiJcicZ448.getCourtCode().equals(sJcicZ448.getCourtCode())
						&& iiJcicZ448.getMaxMainCode().equals(sJcicZ448.getMaxMainCode())) {
					count++;
					uJcicZ448 = sJcicZ448Service.holdById(sJcicZ448.getJcicZ448Id(), titaVo);
					uJcicZ448.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ448.setActualFilingDate(iJcicDate);
						uJcicZ448.setActualFilingMark("Y");
					} else {
						uJcicZ448.setActualFilingDate(0);
						uJcicZ448.setActualFilingMark("");
					}
					try {
						sJcicZ448Service.update(uJcicZ448, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "450":
			Slice<JcicZ450> iJcicZ450 = null;
			JcicZ450 uJcicZ450 = new JcicZ450();
			iJcicZ450 = sJcicZ450Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iPayDate = Integer.valueOf(titaVo.get("PayDate"));
			JcicZ450Id jcicZ450Id = new JcicZ450Id();
			jcicZ450Id.setCustId(iCustId);
			jcicZ450Id.setSubmitKey(iSubmitKey);
			jcicZ450Id.setApplyDate(iApplyDate);
			jcicZ450Id.setCourtCode(iCourtCode);
			jcicZ450Id.setPayDate(iPayDate);
			JcicZ450 sJcicZ450 = new JcicZ450();
			sJcicZ450 = sJcicZ450Service.findById(jcicZ450Id, titaVo);
			for (JcicZ450 iiJcicZ450 : iJcicZ450) {
				if ((iiJcicZ450.getOutJcicTxtDate() == 0 || iiJcicZ450.getActualFilingDate() == 0
						|| iiJcicZ450.getActualFilingMark() == null)
						&& iiJcicZ450.getCustId().equals(sJcicZ450.getCustId())
						&& iiJcicZ450.getSubmitKey().equals(sJcicZ450.getSubmitKey())
						&& iiJcicZ450.getApplyDate() == sJcicZ450.getApplyDate()
						&& iiJcicZ450.getCourtCode().equals(sJcicZ450.getCourtCode())
						&& iiJcicZ450.getPayDate() == sJcicZ450.getPayDate()) {
					count++;
					uJcicZ450 = sJcicZ450Service.holdById(sJcicZ450.getJcicZ450Id(), titaVo);
					uJcicZ450.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ450.setActualFilingDate(iJcicDate);
						uJcicZ450.setActualFilingMark("Y");
					} else {
						uJcicZ450.setActualFilingDate(0);
						uJcicZ450.setActualFilingMark("");
					}
					try {
						sJcicZ450Service.update(uJcicZ450, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "451":
			Slice<JcicZ451> iJcicZ451 = null;
			JcicZ451 uJcicZ451 = new JcicZ451();
			iJcicZ451 = sJcicZ451Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iDelayYM = Integer.valueOf(titaVo.get("DelayYM"));
			JcicZ451Id jcicZ451Id = new JcicZ451Id();
			jcicZ451Id.setCustId(iCustId);
			jcicZ451Id.setSubmitKey(iSubmitKey);
			jcicZ451Id.setApplyDate(iApplyDate);
			jcicZ451Id.setCourtCode(iCourtCode);
			jcicZ451Id.setDelayYM(iDelayYM);
			JcicZ451 sJcicZ451 = new JcicZ451();
			sJcicZ451 = sJcicZ451Service.findById(jcicZ451Id, titaVo);
			for (JcicZ451 iiJcicZ451 : iJcicZ451) {
				if ((iiJcicZ451.getOutJcicTxtDate() == 0 || iiJcicZ451.getActualFilingDate() == 0
						|| iiJcicZ451.getActualFilingMark() == null)
						&& iiJcicZ451.getCustId().equals(sJcicZ451.getCustId())
						&& iiJcicZ451.getSubmitKey().equals(sJcicZ451.getSubmitKey())
						&& iiJcicZ451.getApplyDate() == sJcicZ451.getApplyDate()
						&& iiJcicZ451.getCourtCode().equals(sJcicZ451.getCourtCode())
						&& iiJcicZ451.getDelayYM() == sJcicZ451.getDelayYM()) {
					count++;
					uJcicZ451 = sJcicZ451Service.holdById(sJcicZ451.getJcicZ451Id(), titaVo);
					uJcicZ451.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ451.setActualFilingDate(iJcicDate);
						uJcicZ451.setActualFilingMark("Y");
					} else {
						uJcicZ451.setActualFilingDate(0);
						uJcicZ451.setActualFilingMark("");
					}
					try {
						sJcicZ451Service.update(uJcicZ451, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "454":
			Slice<JcicZ454> iJcicZ454 = null;
			JcicZ454 uJcicZ454 = new JcicZ454();
			iJcicZ454 = sJcicZ454Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			JcicZ454Id jcicZ454Id = new JcicZ454Id();
			jcicZ454Id.setCustId(iCustId);
			jcicZ454Id.setSubmitKey(iSubmitKey);
			jcicZ454Id.setApplyDate(iApplyDate);
			jcicZ454Id.setCourtCode(iCourtCode);
			jcicZ454Id.setMaxMainCode(iMaxMainCode);
			JcicZ454 sJcicZ454 = new JcicZ454();
			sJcicZ454 = sJcicZ454Service.findById(jcicZ454Id, titaVo);
			for (JcicZ454 iiJcicZ454 : iJcicZ454) {
				if ((iiJcicZ454.getOutJcicTxtDate() == 0 || iiJcicZ454.getActualFilingDate() == 0
						|| iiJcicZ454.getActualFilingMark() == null)
						&& iiJcicZ454.getCustId().equals(sJcicZ454.getCustId())
						&& iiJcicZ454.getSubmitKey().equals(sJcicZ454.getSubmitKey())
						&& iiJcicZ454.getApplyDate() == sJcicZ454.getApplyDate()
						&& iiJcicZ454.getCourtCode().equals(sJcicZ454.getCourtCode())
						&& iiJcicZ454.getMaxMainCode().equals(sJcicZ454.getMaxMainCode())) {
					count++;
					uJcicZ454 = sJcicZ454Service.holdById(sJcicZ454.getJcicZ454Id(), titaVo);
					uJcicZ454.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ454.setActualFilingDate(iJcicDate);
						uJcicZ454.setActualFilingMark("Y");
					} else {
						uJcicZ454.setActualFilingDate(0);
						uJcicZ454.setActualFilingMark("");
					}
					try {
						sJcicZ454Service.update(uJcicZ454, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "570":
			Slice<JcicZ570> iJcicZ570 = null;
			JcicZ570 uJcicZ570 = new JcicZ570();
			iJcicZ570 = sJcicZ570Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			JcicZ570Id jcicZ570Id = new JcicZ570Id();
			jcicZ570Id.setCustId(iCustId);
			jcicZ570Id.setSubmitKey(iSubmitKey);
			jcicZ570Id.setApplyDate(iApplyDate);
			JcicZ570 sJcicZ570 = new JcicZ570();
			sJcicZ570 = sJcicZ570Service.findById(jcicZ570Id, titaVo);
			for (JcicZ570 iiJcicZ570 : iJcicZ570) {
				if ((iiJcicZ570.getOutJcicTxtDate() == 0 || iiJcicZ570.getActualFilingDate() == 0
						|| iiJcicZ570.getActualFilingMark() == null)
						&& iiJcicZ570.getCustId().equals(sJcicZ570.getCustId())
						&& iiJcicZ570.getSubmitKey().equals(sJcicZ570.getSubmitKey())
						&& iiJcicZ570.getApplyDate() == sJcicZ570.getApplyDate()) {
					count++;
					uJcicZ570 = sJcicZ570Service.holdById(sJcicZ570.getJcicZ570Id(), titaVo);
					uJcicZ570.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ570.setActualFilingDate(iJcicDate);
						uJcicZ570.setActualFilingMark("Y");
					} else {
						uJcicZ570.setActualFilingDate(0);
						uJcicZ570.setActualFilingMark("");
					}
					try {
						sJcicZ570Service.update(uJcicZ570, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "571":
			Slice<JcicZ571> iJcicZ571 = null;
			JcicZ571 uJcicZ571 = new JcicZ571();
			iJcicZ571 = sJcicZ571Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iBankId = titaVo.get("BankId");
			JcicZ571Id jcicZ571Id = new JcicZ571Id();
			jcicZ571Id.setCustId(iCustId);
			jcicZ571Id.setSubmitKey(iSubmitKey);
			jcicZ571Id.setApplyDate(iApplyDate);
			jcicZ571Id.setBankId(iBankId);
			JcicZ571 sJcicZ571 = new JcicZ571();
			sJcicZ571 = sJcicZ571Service.findById(jcicZ571Id, titaVo);
			for (JcicZ571 iiJcicZ571 : iJcicZ571) {
				if ((iiJcicZ571.getOutJcicTxtDate() == 0 || iiJcicZ571.getActualFilingDate() == 0
						|| iiJcicZ571.getActualFilingMark() == null)
						&& iiJcicZ571.getCustId().equals(sJcicZ571.getCustId())
						&& iiJcicZ571.getSubmitKey().equals(sJcicZ571.getSubmitKey())
						&& iiJcicZ571.getApplyDate() == sJcicZ571.getApplyDate()
						&& iiJcicZ571.getBankId().equals(sJcicZ571.getBankId())) {
					count++;
					uJcicZ571 = sJcicZ571Service.holdById(sJcicZ571.getJcicZ571Id(), titaVo);
					uJcicZ571.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ571.setActualFilingDate(iJcicDate);
						uJcicZ571.setActualFilingMark("Y");
					} else {
						uJcicZ571.setActualFilingDate(0);
						uJcicZ571.setActualFilingMark("");
					}
					try {
						sJcicZ571Service.update(uJcicZ571, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "572":
			Slice<JcicZ572> iJcicZ572 = null;
			JcicZ572 uJcicZ572 = new JcicZ572();
			iJcicZ572 = sJcicZ572Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iBankId = titaVo.get("BankId");
			iPayDate = Integer.valueOf(titaVo.get("PayDate"));
			JcicZ572Id jcicZ572Id = new JcicZ572Id();
			jcicZ572Id.setCustId(iCustId);
			jcicZ572Id.setSubmitKey(iSubmitKey);
			jcicZ572Id.setApplyDate(iApplyDate);
			jcicZ572Id.setBankId(iBankId);
			jcicZ572Id.setPayDate(iPayDate);
			JcicZ572 sJcicZ572 = new JcicZ572();
			sJcicZ572 = sJcicZ572Service.findById(jcicZ572Id, titaVo);
			for (JcicZ572 iiJcicZ572 : iJcicZ572) {
				if ((iiJcicZ572.getOutJcicTxtDate() == 0 || iiJcicZ572.getActualFilingDate() == 0
						|| iiJcicZ572.getActualFilingMark() == null)
						&& iiJcicZ572.getCustId().equals(sJcicZ572.getCustId())
						&& iiJcicZ572.getSubmitKey().equals(sJcicZ572.getSubmitKey())
						&& iiJcicZ572.getApplyDate() == sJcicZ572.getApplyDate()
						&& iiJcicZ572.getBankId().equals(sJcicZ572.getBankId())
						&& iiJcicZ572.getPayDate() == sJcicZ572.getPayDate()) {
					count++;
					uJcicZ572 = sJcicZ572Service.holdById(sJcicZ572.getJcicZ572Id(), titaVo);
					uJcicZ572.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ572.setActualFilingDate(iJcicDate);
						uJcicZ572.setActualFilingMark("Y");
					} else {
						uJcicZ572.setActualFilingDate(0);
						uJcicZ572.setActualFilingMark("");
					}
					try {
						sJcicZ572Service.update(uJcicZ572, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "573":
			Slice<JcicZ573> iJcicZ573 = null;
			JcicZ573 uJcicZ573 = new JcicZ573();
			iJcicZ573 = sJcicZ573Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iPayDate = Integer.valueOf(titaVo.get("PayDate"));
			JcicZ573Id jcicZ573Id = new JcicZ573Id();
			jcicZ573Id.setCustId(iCustId);
			jcicZ573Id.setSubmitKey(iSubmitKey);
			jcicZ573Id.setApplyDate(iApplyDate);
			jcicZ573Id.setPayDate(iPayDate);
			JcicZ573 sJcicZ573 = new JcicZ573();
			sJcicZ573 = sJcicZ573Service.findById(jcicZ573Id, titaVo);
			for (JcicZ573 iiJcicZ573 : iJcicZ573) {
				if ((iiJcicZ573.getOutJcicTxtDate() == 0 || iiJcicZ573.getActualFilingDate() == 0
						|| iiJcicZ573.getActualFilingMark() == null)
						&& iiJcicZ573.getCustId().equals(sJcicZ573.getCustId())
						&& iiJcicZ573.getSubmitKey().equals(sJcicZ573.getSubmitKey())
						&& iiJcicZ573.getApplyDate() == sJcicZ573.getApplyDate()
						&& iiJcicZ573.getPayDate() == sJcicZ573.getPayDate()) {
					count++;
					uJcicZ573 = sJcicZ573Service.holdById(sJcicZ573.getJcicZ573Id(), titaVo);
					uJcicZ573.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ573.setActualFilingDate(iJcicDate);
						uJcicZ573.setActualFilingMark("Y");
					} else {
						uJcicZ573.setActualFilingDate(0);
						uJcicZ573.setActualFilingMark("");
					}
					try {
						sJcicZ573Service.update(uJcicZ573, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "574":
			Slice<JcicZ574> iJcicZ574 = null;
			JcicZ574 uJcicZ574 = new JcicZ574();
			iJcicZ574 = sJcicZ574Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			JcicZ574Id jcicZ574Id = new JcicZ574Id();
			jcicZ574Id.setCustId(iCustId);
			jcicZ574Id.setSubmitKey(iSubmitKey);
			jcicZ574Id.setApplyDate(iApplyDate);
			JcicZ574 sJcicZ574 = new JcicZ574();
			sJcicZ574 = sJcicZ574Service.findById(jcicZ574Id, titaVo);
			for (JcicZ574 iiJcicZ574 : iJcicZ574) {
				if ((iiJcicZ574.getOutJcicTxtDate() == 0 || iiJcicZ574.getActualFilingDate() == 0
						|| iiJcicZ574.getActualFilingMark() == null)
						&& iiJcicZ574.getCustId().equals(sJcicZ574.getCustId())
						&& iiJcicZ574.getSubmitKey().equals(sJcicZ574.getSubmitKey())
						&& iiJcicZ574.getApplyDate() == sJcicZ574.getApplyDate()) {
					count++;
					uJcicZ574 = sJcicZ574Service.holdById(sJcicZ574.getJcicZ574Id(), titaVo);
					uJcicZ574.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ574.setActualFilingDate(iJcicDate);
						uJcicZ574.setActualFilingMark("Y");
					} else {
						uJcicZ574.setActualFilingDate(0);
						uJcicZ574.setActualFilingMark("");
					}
					try {
						sJcicZ574Service.update(uJcicZ574, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "575":
			Slice<JcicZ575> iJcicZ575 = null;
			JcicZ575 uJcicZ575 = new JcicZ575();
			iJcicZ575 = sJcicZ575Service.findAll(0, Integer.MAX_VALUE, titaVo);
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
			iBankId = titaVo.get("BankId");
			JcicZ575Id jcicZ575Id = new JcicZ575Id();
			jcicZ575Id.setCustId(iCustId);
			jcicZ575Id.setSubmitKey(iSubmitKey);
			jcicZ575Id.setApplyDate(iApplyDate);
			jcicZ575Id.setBankId(iBankId);
			JcicZ575 sJcicZ575 = new JcicZ575();
			sJcicZ575 = sJcicZ575Service.findById(jcicZ575Id, titaVo);
			for (JcicZ575 iiJcicZ575 : iJcicZ575) {
				if ((iiJcicZ575.getOutJcicTxtDate() == 0 || iiJcicZ575.getActualFilingDate() == 0
						|| iiJcicZ575.getActualFilingMark() == null)
						&& iiJcicZ575.getCustId().equals(sJcicZ575.getCustId())
						&& iiJcicZ575.getSubmitKey().equals(sJcicZ575.getSubmitKey())
						&& iiJcicZ575.getApplyDate() == sJcicZ575.getApplyDate()
						&& iiJcicZ575.getBankId().equals(sJcicZ575.getBankId())) {
					count++;
					uJcicZ575 = sJcicZ575Service.holdById(sJcicZ575.getJcicZ575Id(), titaVo);
					uJcicZ575.setOutJcicTxtDate(iJcicDate);
					if (iSubmitType == 3) {
						uJcicZ575.setActualFilingDate(iJcicDate);
						uJcicZ575.setActualFilingMark("Y");
					} else {
						uJcicZ575.setActualFilingDate(0);
						uJcicZ575.setActualFilingMark("");
					}
					try {
						sJcicZ575Service.update(uJcicZ575, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		}
	}

	public void doRemoveJcicDate(TitaVo titaVo) throws LogicException {
		this.info("doRemoveJcicDate");
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		int count = 0;
		String tranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');
		this.info("tranCode    = " + tranCode);
		switch (tranCode) {
		case "040":
			JcicZ040 uJcicZ040 = new JcicZ040();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ040Id jcicZ040Id = new JcicZ040Id();
			jcicZ040Id.setCustId(iCustId);
			jcicZ040Id.setRcDate(iRcDate);
			jcicZ040Id.setSubmitKey(iSubmitKey);
			JcicZ040 sJcicZ040 = new JcicZ040();
			sJcicZ040 = sJcicZ040Service.findById(jcicZ040Id, titaVo);
			Slice<JcicZ040> JcicZ040S = sJcicZ040Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ040 oldJcicZ040 = new JcicZ040();
			List<JcicZ040> lJcicZ040 = JcicZ040S.getContent();
			this.info("iJcicZ040SIZE     =" + lJcicZ040.size());
			for (JcicZ040 iiJcicZ040 : lJcicZ040) {
				count++;
				if (iiJcicZ040.getOutJcicTxtDate() == iJcicDate && iiJcicZ040.getCustId().equals(sJcicZ040.getCustId())
						&& iiJcicZ040.getSubmitKey().equals(sJcicZ040.getSubmitKey())
						&& iiJcicZ040.getRcDate() == sJcicZ040.getRcDate() && iiJcicZ040.getActualFilingDate() == 0) {

					uJcicZ040 = sJcicZ040Service.holdById(iiJcicZ040.getJcicZ040Id(), titaVo);
					oldJcicZ040 = (JcicZ040) iDataLog.clone(uJcicZ040);
					uJcicZ040.setOutJcicTxtDate(0);
					try {
						sJcicZ040Service.update(uJcicZ040, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ040Log iJcicZ040Log = sJcicZ040LogService.ukeyFirst(uJcicZ040.getUkey(), titaVo);
					JcicZ040 cJcicZ040 = sJcicZ040Service.ukeyFirst(uJcicZ040.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ040.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ040, uJcicZ040);
					iDataLog.exec("L8403取消報送", iJcicZ040Log.getUkey() + iJcicZ040Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "041":
			JcicZ041 uJcicZ041 = new JcicZ041();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ041Id jcicZ041Id = new JcicZ041Id();
			jcicZ041Id.setCustId(iCustId);
			jcicZ041Id.setRcDate(iRcDate);
			jcicZ041Id.setSubmitKey(iSubmitKey);
			JcicZ041 sJcicZ041 = new JcicZ041();
			sJcicZ041 = sJcicZ041Service.findById(jcicZ041Id, titaVo);
			Slice<JcicZ041> JcicZ041S = sJcicZ041Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ041 oldJcicZ041 = new JcicZ041();
			List<JcicZ041> lJcicZ041 = JcicZ041S.getContent();
			this.info("iJcicZ041SIZE     =" + lJcicZ041.size());
			for (JcicZ041 iiJcicZ041 : lJcicZ041) {
				count++;
				if (iiJcicZ041.getOutJcicTxtDate() == iJcicDate && iiJcicZ041.getCustId().equals(sJcicZ041.getCustId())
						&& iiJcicZ041.getSubmitKey().equals(sJcicZ041.getSubmitKey())
						&& iiJcicZ041.getRcDate() == sJcicZ041.getRcDate() && iiJcicZ041.getActualFilingDate() == 0) {

					uJcicZ041 = sJcicZ041Service.holdById(iiJcicZ041.getJcicZ041Id(), titaVo);
					oldJcicZ041 = (JcicZ041) iDataLog.clone(uJcicZ041);
					uJcicZ041.setOutJcicTxtDate(0);
					try {
						sJcicZ041Service.update(uJcicZ041, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ041Log iJcicZ041Log = sJcicZ041LogService.ukeyFirst(uJcicZ041.getUkey(), titaVo);
					JcicZ041 cJcicZ041 = sJcicZ041Service.ukeyFirst(uJcicZ041.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ041.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ041, uJcicZ041);
					iDataLog.exec("L8403取消報送", iJcicZ041Log.getUkey() + iJcicZ041Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "042":
			JcicZ042 uJcicZ042 = new JcicZ042();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ042Id jcicZ042Id = new JcicZ042Id();
			jcicZ042Id.setCustId(iCustId);
			jcicZ042Id.setRcDate(iRcDate);
			jcicZ042Id.setSubmitKey(iSubmitKey);
			jcicZ042Id.setMaxMainCode(iMaxMainCode);
			JcicZ042 sJcicZ042 = new JcicZ042();
			sJcicZ042 = sJcicZ042Service.findById(jcicZ042Id, titaVo);
			Slice<JcicZ042> JcicZ042S = sJcicZ042Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ042 oldJcicZ042 = new JcicZ042();
			List<JcicZ042> lJcicZ042 = JcicZ042S.getContent();
			this.info("iJcicZ042SIZE     =" + lJcicZ042.size());
			for (JcicZ042 iiJcicZ042 : lJcicZ042) {
				count++;
				if (iiJcicZ042.getOutJcicTxtDate() == iJcicDate && iiJcicZ042.getCustId().equals(sJcicZ042.getCustId())
						&& iiJcicZ042.getSubmitKey().equals(sJcicZ042.getSubmitKey())
						&& iiJcicZ042.getRcDate() == sJcicZ042.getRcDate() && iiJcicZ042.getActualFilingDate() == 0) {

					uJcicZ042 = sJcicZ042Service.holdById(iiJcicZ042.getJcicZ042Id(), titaVo);
					oldJcicZ042 = (JcicZ042) iDataLog.clone(uJcicZ042);
					uJcicZ042.setOutJcicTxtDate(0);
					try {
						sJcicZ042Service.update(uJcicZ042, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ042Log iJcicZ042Log = sJcicZ042LogService.ukeyFirst(uJcicZ042.getUkey(), titaVo);
					JcicZ042 cJcicZ042 = sJcicZ042Service.ukeyFirst(uJcicZ042.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ042.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ042, uJcicZ042);
					iDataLog.exec("L8403取消報送", iJcicZ042Log.getUkey() + iJcicZ042Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "043":
			JcicZ043 uJcicZ043 = new JcicZ043();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			iAccount = titaVo.getParam("Account");
			JcicZ043Id jcicZ043Id = new JcicZ043Id();
			jcicZ043Id.setCustId(iCustId);
			jcicZ043Id.setRcDate(iRcDate);
			jcicZ043Id.setSubmitKey(iSubmitKey);
			jcicZ043Id.setMaxMainCode(iMaxMainCode);
			jcicZ043Id.setAccount(iAccount);
			JcicZ043 sJcicZ043 = new JcicZ043();
			sJcicZ043 = sJcicZ043Service.findById(jcicZ043Id, titaVo);
			Slice<JcicZ043> JcicZ043S = sJcicZ043Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ043 oldJcicZ043 = new JcicZ043();
			List<JcicZ043> lJcicZ043 = JcicZ043S.getContent();
			this.info("iJcicZ043SIZE     =" + lJcicZ043.size());
			for (JcicZ043 iiJcicZ043 : lJcicZ043) {
				count++;
				if (iiJcicZ043.getOutJcicTxtDate() == iJcicDate && iiJcicZ043.getCustId().equals(sJcicZ043.getCustId())
						&& iiJcicZ043.getSubmitKey().equals(sJcicZ043.getSubmitKey())
						&& iiJcicZ043.getRcDate() == sJcicZ043.getRcDate()
						&& iiJcicZ043.getMaxMainCode().equals(sJcicZ043.getMaxMainCode())
						&& iiJcicZ043.getAccount().equals(sJcicZ043.getAccount())
						&& iiJcicZ043.getActualFilingDate() == 0) {

					uJcicZ043 = sJcicZ043Service.holdById(iiJcicZ043.getJcicZ043Id(), titaVo);
					oldJcicZ043 = (JcicZ043) iDataLog.clone(uJcicZ043);
					uJcicZ043.setOutJcicTxtDate(0);
					try {
						sJcicZ043Service.update(uJcicZ043, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ043Log iJcicZ043Log = sJcicZ043LogService.ukeyFirst(uJcicZ043.getUkey(), titaVo);
					JcicZ043 cJcicZ043 = sJcicZ043Service.ukeyFirst(uJcicZ043.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ043.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ043, uJcicZ043);
					iDataLog.exec("L8403取消報送", iJcicZ043Log.getUkey() + iJcicZ043Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "044":
			JcicZ044 uJcicZ044 = new JcicZ044();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ044Id jcicZ044Id = new JcicZ044Id();
			jcicZ044Id.setCustId(iCustId);
			jcicZ044Id.setRcDate(iRcDate);
			jcicZ044Id.setSubmitKey(iSubmitKey);
			JcicZ044 sJcicZ044 = new JcicZ044();
			sJcicZ044 = sJcicZ044Service.findById(jcicZ044Id, titaVo);
			Slice<JcicZ044> JcicZ044S = sJcicZ044Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ044 oldJcicZ044 = new JcicZ044();
			List<JcicZ044> lJcicZ044 = JcicZ044S.getContent();
			this.info("iJcicZ044SIZE     =" + lJcicZ044.size());
			for (JcicZ044 iiJcicZ044 : lJcicZ044) {
				count++;
				if (iiJcicZ044.getOutJcicTxtDate() == iJcicDate && iiJcicZ044.getCustId().equals(sJcicZ044.getCustId())
						&& iiJcicZ044.getSubmitKey().equals(sJcicZ044.getSubmitKey())
						&& iiJcicZ044.getRcDate() == sJcicZ044.getRcDate() && iiJcicZ044.getActualFilingDate() == 0) {

					uJcicZ044 = sJcicZ044Service.holdById(iiJcicZ044.getJcicZ044Id(), titaVo);
					oldJcicZ044 = (JcicZ044) iDataLog.clone(uJcicZ044);
					uJcicZ044.setOutJcicTxtDate(0);
					try {
						sJcicZ044Service.update(uJcicZ044, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ044Log iJcicZ044Log = sJcicZ044LogService.ukeyFirst(uJcicZ044.getUkey(), titaVo);
					JcicZ044 cJcicZ044 = sJcicZ044Service.ukeyFirst(uJcicZ044.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ044.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ044, uJcicZ044);
					iDataLog.exec("L8403取消報送", iJcicZ044Log.getUkey() + iJcicZ044Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "045":
			JcicZ045 uJcicZ045 = new JcicZ045();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ045Id jcicZ045Id = new JcicZ045Id();
			jcicZ045Id.setCustId(iCustId);
			jcicZ045Id.setRcDate(iRcDate);
			jcicZ045Id.setSubmitKey(iSubmitKey);
			jcicZ045Id.setMaxMainCode(iMaxMainCode);
			JcicZ045 sJcicZ045 = new JcicZ045();
			sJcicZ045 = sJcicZ045Service.findById(jcicZ045Id, titaVo);
			Slice<JcicZ045> JcicZ045S = sJcicZ045Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ045 oldJcicZ045 = new JcicZ045();
			List<JcicZ045> lJcicZ045 = JcicZ045S.getContent();
			this.info("iJcicZ045SIZE     =" + lJcicZ045.size());
			for (JcicZ045 iiJcicZ045 : lJcicZ045) {
				count++;
				if (iiJcicZ045.getOutJcicTxtDate() == iJcicDate && iiJcicZ045.getCustId().equals(sJcicZ045.getCustId())
						&& iiJcicZ045.getSubmitKey().equals(sJcicZ045.getSubmitKey())
						&& iiJcicZ045.getRcDate() == sJcicZ045.getRcDate()
						&& iiJcicZ045.getMaxMainCode().equals(sJcicZ045.getMaxMainCode())
						&& iiJcicZ045.getActualFilingDate() == 0) {

					uJcicZ045 = sJcicZ045Service.holdById(iiJcicZ045.getJcicZ045Id(), titaVo);
					oldJcicZ045 = (JcicZ045) iDataLog.clone(uJcicZ045);
					uJcicZ045.setOutJcicTxtDate(0);
					try {
						sJcicZ045Service.update(uJcicZ045, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ045Log iJcicZ045Log = sJcicZ045LogService.ukeyFirst(uJcicZ045.getUkey(), titaVo);
					JcicZ045 cJcicZ045 = sJcicZ045Service.ukeyFirst(uJcicZ045.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ045.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ045, uJcicZ045);
					iDataLog.exec("L8403取消報送", iJcicZ045Log.getUkey() + iJcicZ045Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "046":
			JcicZ046 uJcicZ046 = new JcicZ046();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate"));
			JcicZ046Id jcicZ046Id = new JcicZ046Id();
			jcicZ046Id.setCustId(iCustId);
			jcicZ046Id.setRcDate(iRcDate);
			jcicZ046Id.setSubmitKey(iSubmitKey);
			jcicZ046Id.setCloseDate(iCloseDate);
			JcicZ046 sJcicZ046 = new JcicZ046();
			sJcicZ046 = sJcicZ046Service.findById(jcicZ046Id, titaVo);
			Slice<JcicZ046> JcicZ046S = sJcicZ046Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ046 oldJcicZ046 = new JcicZ046();
			List<JcicZ046> lJcicZ046 = JcicZ046S.getContent();

			for (JcicZ046 iiJcicZ046 : lJcicZ046) {
				count++;
				if (iiJcicZ046.getOutJcicTxtDate() == iJcicDate && iiJcicZ046.getCustId().equals(sJcicZ046.getCustId())
						&& iiJcicZ046.getSubmitKey().equals(sJcicZ046.getSubmitKey())
						&& iiJcicZ046.getRcDate() == sJcicZ046.getRcDate()
						&& iiJcicZ046.getCloseDate() == sJcicZ046.getCloseDate()
						&& iiJcicZ046.getActualFilingDate() == 0) {

					uJcicZ046 = sJcicZ046Service.holdById(iiJcicZ046.getJcicZ046Id(), titaVo);
					oldJcicZ046 = (JcicZ046) iDataLog.clone(uJcicZ046);
					uJcicZ046.setOutJcicTxtDate(0);
					try {
						sJcicZ046Service.update(uJcicZ046, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ046Log iJcicZ046Log = sJcicZ046LogService.ukeyFirst(uJcicZ046.getUkey(), titaVo);
					JcicZ046 cJcicZ046 = sJcicZ046Service.ukeyFirst(uJcicZ046.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ046.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ046, uJcicZ046);
					iDataLog.exec("L8403取消報送", iJcicZ046Log.getUkey() + iJcicZ046Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "047":
			JcicZ047 uJcicZ047 = new JcicZ047();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ047Id jcicZ047Id = new JcicZ047Id();
			jcicZ047Id.setCustId(iCustId);
			jcicZ047Id.setRcDate(iRcDate);
			jcicZ047Id.setSubmitKey(iSubmitKey);
			JcicZ047 sJcicZ047 = new JcicZ047();
			sJcicZ047 = sJcicZ047Service.findById(jcicZ047Id, titaVo);
			Slice<JcicZ047> JcicZ047S = sJcicZ047Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ047 oldJcicZ047 = new JcicZ047();
			List<JcicZ047> lJcicZ047 = JcicZ047S.getContent();
			this.info("iJcicZ047SIZE     =" + lJcicZ047.size());
			for (JcicZ047 iiJcicZ047 : lJcicZ047) {
				count++;
				if (iiJcicZ047.getOutJcicTxtDate() == iJcicDate && iiJcicZ047.getCustId().equals(sJcicZ047.getCustId())
						&& iiJcicZ047.getSubmitKey().equals(sJcicZ047.getSubmitKey())
						&& iiJcicZ047.getRcDate() == sJcicZ047.getRcDate() && iiJcicZ047.getActualFilingDate() == 0) {

					uJcicZ047 = sJcicZ047Service.holdById(iiJcicZ047.getJcicZ047Id(), titaVo);
					oldJcicZ047 = (JcicZ047) iDataLog.clone(uJcicZ047);
					uJcicZ047.setOutJcicTxtDate(0);
					try {
						sJcicZ047Service.update(uJcicZ047, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ047Log iJcicZ047Log = sJcicZ047LogService.ukeyFirst(uJcicZ047.getUkey(), titaVo);
					JcicZ047 cJcicZ047 = sJcicZ047Service.ukeyFirst(uJcicZ047.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ047.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ047, uJcicZ047);
					iDataLog.exec("L8403取消報送", iJcicZ047Log.getUkey() + iJcicZ047Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "048":
			JcicZ048 uJcicZ048 = new JcicZ048();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ048Id jcicZ048Id = new JcicZ048Id();
			jcicZ048Id.setCustId(iCustId);
			jcicZ048Id.setRcDate(iRcDate);
			jcicZ048Id.setSubmitKey(iSubmitKey);
			JcicZ048 sJcicZ048 = new JcicZ048();
			sJcicZ048 = sJcicZ048Service.findById(jcicZ048Id, titaVo);
			Slice<JcicZ048> JcicZ048S = sJcicZ048Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ048 oldJcicZ048 = new JcicZ048();
			List<JcicZ048> lJcicZ048 = JcicZ048S.getContent();
			this.info("iJcicZ048SIZE     =" + lJcicZ048.size());
			for (JcicZ048 iiJcicZ048 : lJcicZ048) {
				count++;
				if (iiJcicZ048.getOutJcicTxtDate() == iJcicDate && iiJcicZ048.getCustId().equals(sJcicZ048.getCustId())
						&& iiJcicZ048.getSubmitKey().equals(sJcicZ048.getSubmitKey())
						&& iiJcicZ048.getRcDate() == sJcicZ048.getRcDate() && iiJcicZ048.getActualFilingDate() == 0) {

					uJcicZ048 = sJcicZ048Service.holdById(iiJcicZ048.getJcicZ048Id(), titaVo);
					oldJcicZ048 = (JcicZ048) iDataLog.clone(uJcicZ048);
					uJcicZ048.setOutJcicTxtDate(0);
					try {
						sJcicZ048Service.update(uJcicZ048, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ048Log iJcicZ048Log = sJcicZ048LogService.ukeyFirst(uJcicZ048.getUkey(), titaVo);
					JcicZ048 cJcicZ048 = sJcicZ048Service.ukeyFirst(uJcicZ048.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ048.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ048, uJcicZ048);
					iDataLog.exec("L8403取消報送", iJcicZ048Log.getUkey() + iJcicZ048Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "049":
			JcicZ049 uJcicZ049 = new JcicZ049();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ049Id jcicZ049Id = new JcicZ049Id();
			jcicZ049Id.setCustId(iCustId);
			jcicZ049Id.setRcDate(iRcDate);
			jcicZ049Id.setSubmitKey(iSubmitKey);
			JcicZ049 sJcicZ049 = new JcicZ049();
			sJcicZ049 = sJcicZ049Service.findById(jcicZ049Id, titaVo);
			Slice<JcicZ049> JcicZ049S = sJcicZ049Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ049 oldJcicZ049 = new JcicZ049();
			List<JcicZ049> lJcicZ049 = JcicZ049S.getContent();
			this.info("iJcicZ049SIZE     =" + lJcicZ049.size());
			for (JcicZ049 iiJcicZ049 : lJcicZ049) {
				count++;
				if (iiJcicZ049.getOutJcicTxtDate() == iJcicDate && iiJcicZ049.getCustId().equals(sJcicZ049.getCustId())
						&& iiJcicZ049.getSubmitKey().equals(sJcicZ049.getSubmitKey())
						&& iiJcicZ049.getRcDate() == sJcicZ049.getRcDate() && iiJcicZ049.getActualFilingDate() == 0) {

					uJcicZ049 = sJcicZ049Service.holdById(iiJcicZ049.getJcicZ049Id(), titaVo);
					oldJcicZ049 = (JcicZ049) iDataLog.clone(uJcicZ049);
					uJcicZ049.setOutJcicTxtDate(0);
					try {
						sJcicZ049Service.update(uJcicZ049, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ049Log iJcicZ049Log = sJcicZ049LogService.ukeyFirst(uJcicZ049.getUkey(), titaVo);
					JcicZ049 cJcicZ049 = sJcicZ049Service.ukeyFirst(uJcicZ049.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ049.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ049, uJcicZ049);
					iDataLog.exec("L8403取消報送", iJcicZ049Log.getUkey() + iJcicZ049Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "050":
			JcicZ050 uJcicZ050 = new JcicZ050();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iPayDate = Integer.valueOf(titaVo.getParam("PayDate"));
			JcicZ050Id jcicZ050Id = new JcicZ050Id();
			jcicZ050Id.setCustId(iCustId);
			jcicZ050Id.setRcDate(iRcDate);
			jcicZ050Id.setSubmitKey(iSubmitKey);
			jcicZ050Id.setPayDate(iPayDate);
			JcicZ050 sJcicZ050 = new JcicZ050();
			sJcicZ050 = sJcicZ050Service.findById(jcicZ050Id, titaVo);
			Slice<JcicZ050> JcicZ050S = sJcicZ050Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ050 oldJcicZ050 = new JcicZ050();
			List<JcicZ050> lJcicZ050 = JcicZ050S.getContent();
			this.info("iJcicZ050SIZE     =" + lJcicZ050.size());
			for (JcicZ050 iiJcicZ050 : lJcicZ050) {
				count++;
				if (iiJcicZ050.getOutJcicTxtDate() == iJcicDate && iiJcicZ050.getCustId().equals(sJcicZ050.getCustId())
						&& iiJcicZ050.getSubmitKey().equals(sJcicZ050.getSubmitKey())
						&& iiJcicZ050.getRcDate() == sJcicZ050.getRcDate()
						&& iiJcicZ050.getPayDate() == sJcicZ050.getPayDate() && iiJcicZ050.getActualFilingDate() == 0) {

					uJcicZ050 = sJcicZ050Service.holdById(iiJcicZ050.getJcicZ050Id(), titaVo);
					oldJcicZ050 = (JcicZ050) iDataLog.clone(uJcicZ050);
					uJcicZ050.setOutJcicTxtDate(0);
					try {
						sJcicZ050Service.update(uJcicZ050, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ050Log iJcicZ050Log = sJcicZ050LogService.ukeyFirst(uJcicZ050.getUkey(), titaVo);
					JcicZ050 cJcicZ050 = sJcicZ050Service.ukeyFirst(uJcicZ050.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ050.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ050, uJcicZ050);
					iDataLog.exec("L8403取消報送", iJcicZ050Log.getUkey() + iJcicZ050Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "051":
			JcicZ051 uJcicZ051 = new JcicZ051();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iDelayYM = Integer.valueOf(titaVo.getParam("DelayYM"));
			JcicZ051Id jcicZ051Id = new JcicZ051Id();
			jcicZ051Id.setCustId(iCustId);
			jcicZ051Id.setRcDate(iRcDate);
			jcicZ051Id.setSubmitKey(iSubmitKey);
			jcicZ051Id.setDelayYM(iDelayYM);
			JcicZ051 sJcicZ051 = new JcicZ051();
			sJcicZ051 = sJcicZ051Service.findById(jcicZ051Id, titaVo);
			Slice<JcicZ051> JcicZ051S = sJcicZ051Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ051 oldJcicZ051 = new JcicZ051();
			List<JcicZ051> lJcicZ051 = JcicZ051S.getContent();
			this.info("iJcicZ051SIZE     =" + lJcicZ051.size());
			for (JcicZ051 iiJcicZ051 : lJcicZ051) {
				count++;
				if (iiJcicZ051.getOutJcicTxtDate() == iJcicDate && iiJcicZ051.getCustId().equals(sJcicZ051.getCustId())
						&& iiJcicZ051.getSubmitKey().equals(sJcicZ051.getSubmitKey())
						&& iiJcicZ051.getRcDate() == sJcicZ051.getRcDate()
						&& iiJcicZ051.getDelayYM() == sJcicZ051.getDelayYM() && iiJcicZ051.getActualFilingDate() == 0) {

					uJcicZ051 = sJcicZ051Service.holdById(iiJcicZ051.getJcicZ051Id(), titaVo);
					oldJcicZ051 = (JcicZ051) iDataLog.clone(uJcicZ051);
					uJcicZ051.setOutJcicTxtDate(0);
					try {
						sJcicZ051Service.update(uJcicZ051, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ051Log iJcicZ051Log = sJcicZ051LogService.ukeyFirst(uJcicZ051.getUkey(), titaVo);
					JcicZ051 cJcicZ051 = sJcicZ051Service.ukeyFirst(uJcicZ051.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ051.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ051, uJcicZ051);
					iDataLog.exec("L8403取消報送", iJcicZ051Log.getUkey() + iJcicZ051Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "052":
			JcicZ052 uJcicZ052 = new JcicZ052();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			JcicZ052Id jcicZ052Id = new JcicZ052Id();
			jcicZ052Id.setCustId(iCustId);
			jcicZ052Id.setRcDate(iRcDate);
			jcicZ052Id.setSubmitKey(iSubmitKey);
			JcicZ052 sJcicZ052 = new JcicZ052();
			sJcicZ052 = sJcicZ052Service.findById(jcicZ052Id, titaVo);
			Slice<JcicZ052> JcicZ052S = sJcicZ052Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ052 oldJcicZ052 = new JcicZ052();
			List<JcicZ052> lJcicZ052 = JcicZ052S.getContent();
			this.info("iJcicZ052SIZE     =" + lJcicZ052.size());
			for (JcicZ052 iiJcicZ052 : lJcicZ052) {
				count++;
				if (iiJcicZ052.getOutJcicTxtDate() == iJcicDate && iiJcicZ052.getCustId().equals(sJcicZ052.getCustId())
						&& iiJcicZ052.getSubmitKey().equals(sJcicZ052.getSubmitKey())
						&& iiJcicZ052.getRcDate() == sJcicZ052.getRcDate() && iiJcicZ052.getActualFilingDate() == 0) {

					uJcicZ052 = sJcicZ052Service.holdById(iiJcicZ052.getJcicZ052Id(), titaVo);
					oldJcicZ052 = (JcicZ052) iDataLog.clone(uJcicZ052);
					uJcicZ052.setOutJcicTxtDate(0);
					try {
						sJcicZ052Service.update(uJcicZ052, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ052Log iJcicZ052Log = sJcicZ052LogService.ukeyFirst(uJcicZ052.getUkey(), titaVo);
					JcicZ052 cJcicZ052 = sJcicZ052Service.ukeyFirst(uJcicZ052.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ052.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ052, uJcicZ052);
					iDataLog.exec("L8403取消報送", iJcicZ052Log.getUkey() + iJcicZ052Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "053":
			JcicZ053 uJcicZ053 = new JcicZ053();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ053Id jcicZ053Id = new JcicZ053Id();
			jcicZ053Id.setCustId(iCustId);
			jcicZ053Id.setRcDate(iRcDate);
			jcicZ053Id.setSubmitKey(iSubmitKey);
			jcicZ053Id.setMaxMainCode(iMaxMainCode);
			JcicZ053 sJcicZ053 = new JcicZ053();
			sJcicZ053 = sJcicZ053Service.findById(jcicZ053Id, titaVo);
			Slice<JcicZ053> JcicZ053S = sJcicZ053Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ053 oldJcicZ053 = new JcicZ053();
			List<JcicZ053> lJcicZ053 = JcicZ053S.getContent();
			this.info("iJcicZ053SIZE     =" + lJcicZ053.size());
			for (JcicZ053 iiJcicZ053 : lJcicZ053) {
				count++;
				if (iiJcicZ053.getOutJcicTxtDate() == iJcicDate && iiJcicZ053.getCustId().equals(sJcicZ053.getCustId())
						&& iiJcicZ053.getSubmitKey().equals(sJcicZ053.getSubmitKey())
						&& iiJcicZ053.getRcDate() == sJcicZ053.getRcDate()
						&& iiJcicZ053.getMaxMainCode().equals(sJcicZ053.getMaxMainCode())
						&& iiJcicZ053.getActualFilingDate() == 0) {

					uJcicZ053 = sJcicZ053Service.holdById(iiJcicZ053.getJcicZ053Id(), titaVo);
					oldJcicZ053 = (JcicZ053) iDataLog.clone(uJcicZ053);
					uJcicZ053.setOutJcicTxtDate(0);
					try {
						sJcicZ053Service.update(uJcicZ053, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ053Log iJcicZ053Log = sJcicZ053LogService.ukeyFirst(uJcicZ053.getUkey(), titaVo);
					JcicZ053 cJcicZ053 = sJcicZ053Service.ukeyFirst(uJcicZ053.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ053.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ053, uJcicZ053);
					iDataLog.exec("L8403取消報送", iJcicZ053Log.getUkey() + iJcicZ053Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "054":
			JcicZ054 uJcicZ054 = new JcicZ054();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			iPayOffDate = Integer.valueOf(titaVo.get("PayOffDate"));
			JcicZ054Id jcicZ054Id = new JcicZ054Id();
			jcicZ054Id.setCustId(iCustId);
			jcicZ054Id.setRcDate(iRcDate);
			jcicZ054Id.setSubmitKey(iSubmitKey);
			jcicZ054Id.setMaxMainCode(iMaxMainCode);
			jcicZ054Id.setPayOffDate(iPayOffDate);
			JcicZ054 sJcicZ054 = new JcicZ054();
			sJcicZ054 = sJcicZ054Service.findById(jcicZ054Id, titaVo);
			Slice<JcicZ054> JcicZ054S = sJcicZ054Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ054 oldJcicZ054 = new JcicZ054();
			List<JcicZ054> lJcicZ054 = JcicZ054S.getContent();
			this.info("iJcicZ054SIZE     =" + lJcicZ054.size());
			for (JcicZ054 iiJcicZ054 : lJcicZ054) {
				count++;
				if (iiJcicZ054.getOutJcicTxtDate() == iJcicDate && iiJcicZ054.getCustId().equals(sJcicZ054.getCustId())
						&& iiJcicZ054.getSubmitKey().equals(sJcicZ054.getSubmitKey())
						&& iiJcicZ054.getRcDate() == sJcicZ054.getRcDate()
						&& iiJcicZ054.getMaxMainCode().equals(sJcicZ054.getMaxMainCode())
						&& iiJcicZ054.getActualFilingDate() == 0) {

					uJcicZ054 = sJcicZ054Service.holdById(iiJcicZ054.getJcicZ054Id(), titaVo);
					oldJcicZ054 = (JcicZ054) iDataLog.clone(uJcicZ054);
					uJcicZ054.setOutJcicTxtDate(0);
					try {
						sJcicZ054Service.update(uJcicZ054, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ054Log iJcicZ054Log = sJcicZ054LogService.ukeyFirst(uJcicZ054.getUkey(), titaVo);
					JcicZ054 cJcicZ054 = sJcicZ054Service.ukeyFirst(uJcicZ054.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ054.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ054, uJcicZ054);
					iDataLog.exec("L8403取消報送", iJcicZ054Log.getUkey() + iJcicZ054Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "055":
			JcicZ055 uJcicZ055 = new JcicZ055();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iCaseStatus = titaVo.get("CaseStatus");
			iClaimDate = Integer.valueOf(titaVo.get("ClaimDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ055Id jcicZ055Id = new JcicZ055Id();
			jcicZ055Id.setCustId(iCustId);
			jcicZ055Id.setSubmitKey(iSubmitKey);
			jcicZ055Id.setCaseStatus(iCaseStatus);
			jcicZ055Id.setClaimDate(iClaimDate);
			jcicZ055Id.setCourtCode(iCourtCode);
			JcicZ055 sJcicZ055 = new JcicZ055();
			sJcicZ055 = sJcicZ055Service.findById(jcicZ055Id, titaVo);
			Slice<JcicZ055> JcicZ055S = sJcicZ055Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ055 oldJcicZ055 = new JcicZ055();
			List<JcicZ055> lJcicZ055 = JcicZ055S.getContent();
			this.info("iJcicZ055SIZE     =" + lJcicZ055.size());
			for (JcicZ055 iiJcicZ055 : lJcicZ055) {
				count++;
				if (iiJcicZ055.getOutJcicTxtDate() == iJcicDate && iiJcicZ055.getCustId().equals(sJcicZ055.getCustId())
						&& iiJcicZ055.getSubmitKey().equals(sJcicZ055.getSubmitKey())
						&& iiJcicZ055.getClaimDate() == sJcicZ055.getClaimDate()
						&& iiJcicZ055.getCaseStatus().equals(sJcicZ055.getCaseStatus())
						&& iiJcicZ055.getCourtCode().equals(sJcicZ055.getCourtCode())
						&& iiJcicZ055.getActualFilingDate() == 0) {

					uJcicZ055 = sJcicZ055Service.holdById(iiJcicZ055.getJcicZ055Id(), titaVo);
					oldJcicZ055 = (JcicZ055) iDataLog.clone(uJcicZ055);
					uJcicZ055.setOutJcicTxtDate(0);
					try {
						sJcicZ055Service.update(uJcicZ055, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ055Log iJcicZ055Log = sJcicZ055LogService.ukeyFirst(uJcicZ055.getUkey(), titaVo);
					JcicZ055 cJcicZ055 = sJcicZ055Service.ukeyFirst(uJcicZ055.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ055.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ055, uJcicZ055);
					iDataLog.exec("L8403取消報送", iJcicZ055Log.getUkey() + iJcicZ055Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "056":
			JcicZ056 uJcicZ056 = new JcicZ056();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iCaseStatus = titaVo.get("CaseStatus");
			iClaimDate = Integer.valueOf(titaVo.get("ClaimDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ056Id jcicZ056Id = new JcicZ056Id();
			jcicZ056Id.setCustId(iCustId);
			jcicZ056Id.setSubmitKey(iSubmitKey);
			jcicZ056Id.setCaseStatus(iCaseStatus);
			jcicZ056Id.setClaimDate(iClaimDate);
			jcicZ056Id.setCourtCode(iCourtCode);
			JcicZ056 sJcicZ056 = new JcicZ056();
			sJcicZ056 = sJcicZ056Service.findById(jcicZ056Id, titaVo);
			Slice<JcicZ056> JcicZ056S = sJcicZ056Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ056 oldJcicZ056 = new JcicZ056();
			List<JcicZ056> lJcicZ056 = JcicZ056S.getContent();
			this.info("iJcicZ056SIZE     =" + lJcicZ056.size());
			for (JcicZ056 iiJcicZ056 : lJcicZ056) {
				count++;
				if (iiJcicZ056.getOutJcicTxtDate() == iJcicDate && iiJcicZ056.getCustId().equals(sJcicZ056.getCustId())
						&& iiJcicZ056.getSubmitKey().equals(sJcicZ056.getSubmitKey())
						&& iiJcicZ056.getClaimDate() == sJcicZ056.getClaimDate()
						&& iiJcicZ056.getCaseStatus().equals(sJcicZ056.getCaseStatus())
						&& iiJcicZ056.getCourtCode().equals(sJcicZ056.getCourtCode())
						&& iiJcicZ056.getActualFilingDate() == 0) {

					uJcicZ056 = sJcicZ056Service.holdById(iiJcicZ056.getJcicZ056Id(), titaVo);
					oldJcicZ056 = (JcicZ056) iDataLog.clone(uJcicZ056);
					uJcicZ056.setOutJcicTxtDate(0);
					try {
						sJcicZ056Service.update(uJcicZ056, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ056Log iJcicZ056Log = sJcicZ056LogService.ukeyFirst(uJcicZ056.getUkey(), titaVo);
					JcicZ056 cJcicZ056 = sJcicZ056Service.ukeyFirst(uJcicZ056.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ056.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ056, uJcicZ056);
					iDataLog.exec("L8403取消報送", iJcicZ056Log.getUkey() + iJcicZ056Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "060":
			JcicZ060 uJcicZ060 = new JcicZ060();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			JcicZ060Id jcicZ060Id = new JcicZ060Id();
			jcicZ060Id.setCustId(iCustId);
			jcicZ060Id.setSubmitKey(iSubmitKey);
			jcicZ060Id.setRcDate(iRcDate);
			jcicZ060Id.setChangePayDate(iChangePayDate);
			JcicZ060 sJcicZ060 = new JcicZ060();
			sJcicZ060 = sJcicZ060Service.findById(jcicZ060Id, titaVo);
			Slice<JcicZ060> JcicZ060S = sJcicZ060Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ060 oldJcicZ060 = new JcicZ060();
			List<JcicZ060> lJcicZ060 = JcicZ060S.getContent();
			this.info("iJcicZ060SIZE     =" + lJcicZ060.size());
			for (JcicZ060 iiJcicZ060 : lJcicZ060) {
				count++;
				if (iiJcicZ060.getOutJcicTxtDate() == iJcicDate && iiJcicZ060.getCustId().equals(sJcicZ060.getCustId())
						&& iiJcicZ060.getSubmitKey().equals(sJcicZ060.getSubmitKey())
						&& iiJcicZ060.getRcDate() == sJcicZ060.getRcDate()
						&& iiJcicZ060.getChangePayDate() == sJcicZ060.getChangePayDate()
						&& iiJcicZ060.getActualFilingDate() == 0) {

					uJcicZ060 = sJcicZ060Service.holdById(iiJcicZ060.getJcicZ060Id(), titaVo);
					oldJcicZ060 = (JcicZ060) iDataLog.clone(uJcicZ060);
					uJcicZ060.setOutJcicTxtDate(0);
					try {
						sJcicZ060Service.update(uJcicZ060, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ060Log iJcicZ060Log = sJcicZ060LogService.ukeyFirst(uJcicZ060.getUkey(), titaVo);
					JcicZ060 cJcicZ060 = sJcicZ060Service.ukeyFirst(uJcicZ060.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ060.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ060, uJcicZ060);
					iDataLog.exec("L8403取消報送", iJcicZ060Log.getUkey() + iJcicZ060Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "061":
			JcicZ061 uJcicZ061 = new JcicZ061();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			iMaxMainCode = titaVo.getParam("MaxMainCode");
			JcicZ061Id jcicZ061Id = new JcicZ061Id();
			jcicZ061Id.setCustId(iCustId);
			jcicZ061Id.setSubmitKey(iSubmitKey);
			jcicZ061Id.setRcDate(iRcDate);
			jcicZ061Id.setChangePayDate(iChangePayDate);
			jcicZ061Id.setMaxMainCode(iMaxMainCode);
			JcicZ061 sJcicZ061 = new JcicZ061();
			sJcicZ061 = sJcicZ061Service.findById(jcicZ061Id, titaVo);
			Slice<JcicZ061> JcicZ061S = sJcicZ061Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ061 oldJcicZ061 = new JcicZ061();
			List<JcicZ061> lJcicZ061 = JcicZ061S.getContent();
			this.info("iJcicZ061SIZE     =" + lJcicZ061.size());
			for (JcicZ061 iiJcicZ061 : lJcicZ061) {
				count++;
				if (iiJcicZ061.getOutJcicTxtDate() == iJcicDate && iiJcicZ061.getCustId().equals(sJcicZ061.getCustId())
						&& iiJcicZ061.getSubmitKey().equals(sJcicZ061.getSubmitKey())
						&& iiJcicZ061.getRcDate() == sJcicZ061.getRcDate()
						&& iiJcicZ061.getChangePayDate() == sJcicZ061.getChangePayDate()
						&& iiJcicZ061.getActualFilingDate() == 0) {

					uJcicZ061 = sJcicZ061Service.holdById(iiJcicZ061.getJcicZ061Id(), titaVo);
					oldJcicZ061 = (JcicZ061) iDataLog.clone(uJcicZ061);
					uJcicZ061.setOutJcicTxtDate(0);
					try {
						sJcicZ061Service.update(uJcicZ061, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ061Log iJcicZ061Log = sJcicZ061LogService.ukeyFirst(uJcicZ061.getUkey(), titaVo);
					JcicZ061 cJcicZ061 = sJcicZ061Service.ukeyFirst(uJcicZ061.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ061.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ061, uJcicZ061);
					iDataLog.exec("L8403取消報送", iJcicZ061Log.getUkey() + iJcicZ061Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "062":
			JcicZ062 uJcicZ062 = new JcicZ062();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			JcicZ062Id jcicZ062Id = new JcicZ062Id();
			jcicZ062Id.setCustId(iCustId);
			jcicZ062Id.setSubmitKey(iSubmitKey);
			jcicZ062Id.setRcDate(iRcDate);
			jcicZ062Id.setChangePayDate(iChangePayDate);
			JcicZ062 sJcicZ062 = new JcicZ062();
			sJcicZ062 = sJcicZ062Service.findById(jcicZ062Id, titaVo);
			Slice<JcicZ062> JcicZ062S = sJcicZ062Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ062 oldJcicZ062 = new JcicZ062();
			List<JcicZ062> lJcicZ062 = JcicZ062S.getContent();
			this.info("iJcicZ062SIZE     =" + lJcicZ062.size());
			for (JcicZ062 iiJcicZ062 : lJcicZ062) {
				count++;
				if (iiJcicZ062.getOutJcicTxtDate() == iJcicDate && iiJcicZ062.getCustId().equals(sJcicZ062.getCustId())
						&& iiJcicZ062.getSubmitKey().equals(sJcicZ062.getSubmitKey())
						&& iiJcicZ062.getRcDate() == sJcicZ062.getRcDate()
						&& iiJcicZ062.getChangePayDate() == sJcicZ062.getChangePayDate()
						&& iiJcicZ062.getActualFilingDate() == 0) {

					uJcicZ062 = sJcicZ062Service.holdById(iiJcicZ062.getJcicZ062Id(), titaVo);
					oldJcicZ062 = (JcicZ062) iDataLog.clone(uJcicZ062);
					uJcicZ062.setOutJcicTxtDate(0);
					try {
						sJcicZ062Service.update(uJcicZ062, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ062Log iJcicZ062Log = sJcicZ062LogService.ukeyFirst(uJcicZ062.getUkey(), titaVo);
					JcicZ062 cJcicZ062 = sJcicZ062Service.ukeyFirst(uJcicZ062.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ062.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ062, uJcicZ062);
					iDataLog.exec("L8403取消報送", iJcicZ062Log.getUkey() + iJcicZ062Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "063":
			JcicZ063 uJcicZ063 = new JcicZ063();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iRcDate = Integer.valueOf(titaVo.get("RcDate"));
			iChangePayDate = Integer.valueOf(titaVo.get("ChangePayDate"));
			JcicZ063Id jcicZ063Id = new JcicZ063Id();
			jcicZ063Id.setCustId(iCustId);
			jcicZ063Id.setSubmitKey(iSubmitKey);
			jcicZ063Id.setRcDate(iRcDate);
			jcicZ063Id.setChangePayDate(iChangePayDate);
			JcicZ063 sJcicZ063 = new JcicZ063();
			sJcicZ063 = sJcicZ063Service.findById(jcicZ063Id, titaVo);
			Slice<JcicZ063> JcicZ063S = sJcicZ063Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ063 oldJcicZ063 = new JcicZ063();
			List<JcicZ063> lJcicZ063 = JcicZ063S.getContent();
			this.info("iJcicZ063SIZE     =" + lJcicZ063.size());
			for (JcicZ063 iiJcicZ063 : lJcicZ063) {
				count++;
				if (iiJcicZ063.getOutJcicTxtDate() == iJcicDate && iiJcicZ063.getCustId().equals(sJcicZ063.getCustId())
						&& iiJcicZ063.getSubmitKey().equals(sJcicZ063.getSubmitKey())
						&& iiJcicZ063.getRcDate() == sJcicZ063.getRcDate()
						&& iiJcicZ063.getChangePayDate() == sJcicZ063.getChangePayDate()
						&& iiJcicZ063.getActualFilingDate() == 0) {

					uJcicZ063 = sJcicZ063Service.holdById(iiJcicZ063.getJcicZ063Id(), titaVo);
					oldJcicZ063 = (JcicZ063) iDataLog.clone(uJcicZ063);
					uJcicZ063.setOutJcicTxtDate(0);
					try {
						sJcicZ063Service.update(uJcicZ063, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ063Log iJcicZ063Log = sJcicZ063LogService.ukeyFirst(uJcicZ063.getUkey(), titaVo);
					JcicZ063 cJcicZ063 = sJcicZ063Service.ukeyFirst(uJcicZ063.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ063.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ063, uJcicZ063);
					iDataLog.exec("L8403取消報送", iJcicZ063Log.getUkey() + iJcicZ063Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "440":
			JcicZ440 uJcicZ440 = new JcicZ440();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ440Id jcicZ440Id = new JcicZ440Id();
			jcicZ440Id.setCustId(iCustId);
			jcicZ440Id.setSubmitKey(iSubmitKey);
			jcicZ440Id.setApplyDate(iApplyDate);
			jcicZ440Id.setCourtCode(iCourtCode);
			JcicZ440 sJcicZ440 = new JcicZ440();
			sJcicZ440 = sJcicZ440Service.findById(jcicZ440Id, titaVo);
			Slice<JcicZ440> JcicZ440S = sJcicZ440Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ440 oldJcicZ440 = new JcicZ440();
			List<JcicZ440> lJcicZ440 = JcicZ440S.getContent();
			this.info("iJcicZ440SIZE     =" + lJcicZ440.size());
			for (JcicZ440 iiJcicZ440 : lJcicZ440) {
				count++;
				if (iiJcicZ440.getOutJcicTxtDate() == iJcicDate && iiJcicZ440.getCustId().equals(sJcicZ440.getCustId())
						&& iiJcicZ440.getSubmitKey().equals(sJcicZ440.getSubmitKey())
						&& iiJcicZ440.getApplyDate() == sJcicZ440.getApplyDate()
						&& iiJcicZ440.getCourtCode().equals(sJcicZ440.getCourtCode())
						&& iiJcicZ440.getActualFilingDate() == 0) {

					uJcicZ440 = sJcicZ440Service.holdById(iiJcicZ440.getJcicZ440Id(), titaVo);
					oldJcicZ440 = (JcicZ440) iDataLog.clone(uJcicZ440);
					uJcicZ440.setOutJcicTxtDate(0);
					try {
						sJcicZ440Service.update(uJcicZ440, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ440Log iJcicZ440Log = sJcicZ440LogService.ukeyFirst(uJcicZ440.getUkey(), titaVo);
					JcicZ440 cJcicZ440 = sJcicZ440Service.ukeyFirst(uJcicZ440.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ440.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ440, uJcicZ440);
					iDataLog.exec("L8403取消報送", iJcicZ440Log.getUkey() + iJcicZ440Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "442":
			JcicZ442 uJcicZ442 = new JcicZ442();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			JcicZ442Id jcicZ442Id = new JcicZ442Id();
			jcicZ442Id.setCustId(iCustId);
			jcicZ442Id.setSubmitKey(iSubmitKey);
			jcicZ442Id.setApplyDate(iApplyDate);
			jcicZ442Id.setCourtCode(iCourtCode);
			jcicZ442Id.setMaxMainCode(iMaxMainCode);
			JcicZ442 sJcicZ442 = new JcicZ442();
			sJcicZ442 = sJcicZ442Service.findById(jcicZ442Id, titaVo);
			Slice<JcicZ442> JcicZ442S = sJcicZ442Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ442 oldJcicZ442 = new JcicZ442();
			List<JcicZ442> lJcicZ442 = JcicZ442S.getContent();
			this.info("iJcicZ442SIZE     =" + lJcicZ442.size());
			for (JcicZ442 iiJcicZ442 : lJcicZ442) {
				count++;
				if (iiJcicZ442.getOutJcicTxtDate() == iJcicDate && iiJcicZ442.getCustId().equals(sJcicZ442.getCustId())
						&& iiJcicZ442.getSubmitKey().equals(sJcicZ442.getSubmitKey())
						&& iiJcicZ442.getApplyDate() == sJcicZ442.getApplyDate()
						&& iiJcicZ442.getCourtCode().equals(sJcicZ442.getCourtCode())
						&& iiJcicZ442.getMaxMainCode().equals(sJcicZ442.getMaxMainCode())
						&& iiJcicZ442.getActualFilingDate() == 0) {

					uJcicZ442 = sJcicZ442Service.holdById(iiJcicZ442.getJcicZ442Id(), titaVo);
					oldJcicZ442 = (JcicZ442) iDataLog.clone(uJcicZ442);
					uJcicZ442.setOutJcicTxtDate(0);
					try {
						sJcicZ442Service.update(uJcicZ442, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ442Log iJcicZ442Log = sJcicZ442LogService.ukeyFirst(uJcicZ442.getUkey(), titaVo);
					JcicZ442 cJcicZ442 = sJcicZ442Service.ukeyFirst(uJcicZ442.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ442.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ442, uJcicZ442);
					iDataLog.exec("L8403取消報送", iJcicZ442Log.getUkey() + iJcicZ442Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "443":
			JcicZ443 uJcicZ443 = new JcicZ443();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			iAccount = titaVo.get("Account");
			JcicZ443Id jcicZ443Id = new JcicZ443Id();
			jcicZ443Id.setCustId(iCustId);
			jcicZ443Id.setSubmitKey(iSubmitKey);
			jcicZ443Id.setApplyDate(iApplyDate);
			jcicZ443Id.setCourtCode(iCourtCode);
			jcicZ443Id.setMaxMainCode(iMaxMainCode);
			jcicZ443Id.setAccount(iAccount);
			JcicZ443 sJcicZ443 = new JcicZ443();
			sJcicZ443 = sJcicZ443Service.findById(jcicZ443Id, titaVo);
			Slice<JcicZ443> JcicZ443S = sJcicZ443Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ443 oldJcicZ443 = new JcicZ443();
			List<JcicZ443> lJcicZ443 = JcicZ443S.getContent();
			this.info("iJcicZ443SIZE     =" + lJcicZ443.size());
			for (JcicZ443 iiJcicZ443 : lJcicZ443) {
				count++;
				if (iiJcicZ443.getOutJcicTxtDate() == iJcicDate && iiJcicZ443.getCustId().equals(sJcicZ443.getCustId())
						&& iiJcicZ443.getSubmitKey().equals(sJcicZ443.getSubmitKey())
						&& iiJcicZ443.getApplyDate() == sJcicZ443.getApplyDate()
						&& iiJcicZ443.getCourtCode().equals(sJcicZ443.getCourtCode())
						&& iiJcicZ443.getMaxMainCode().equals(sJcicZ443.getMaxMainCode())
						&& iiJcicZ443.getActualFilingDate() == 0) {

					uJcicZ443 = sJcicZ443Service.holdById(iiJcicZ443.getJcicZ443Id(), titaVo);
					oldJcicZ443 = (JcicZ443) iDataLog.clone(uJcicZ443);
					uJcicZ443.setOutJcicTxtDate(0);
					try {
						sJcicZ443Service.update(uJcicZ443, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ443Log iJcicZ443Log = sJcicZ443LogService.ukeyFirst(uJcicZ443.getUkey(), titaVo);
					JcicZ443 cJcicZ443 = sJcicZ443Service.ukeyFirst(uJcicZ443.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ443.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ443, uJcicZ443);
					iDataLog.exec("L8403取消報送", iJcicZ443Log.getUkey() + iJcicZ443Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "444":
			JcicZ444 uJcicZ444 = new JcicZ444();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ444Id jcicZ444Id = new JcicZ444Id();
			jcicZ444Id.setCustId(iCustId);
			jcicZ444Id.setSubmitKey(iSubmitKey);
			jcicZ444Id.setApplyDate(iApplyDate);
			jcicZ444Id.setCourtCode(iCourtCode);
			JcicZ444 sJcicZ444 = new JcicZ444();
			sJcicZ444 = sJcicZ444Service.findById(jcicZ444Id, titaVo);
			Slice<JcicZ444> JcicZ444S = sJcicZ444Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ444 oldJcicZ444 = new JcicZ444();
			List<JcicZ444> lJcicZ444 = JcicZ444S.getContent();
			this.info("iJcicZ444SIZE     =" + lJcicZ444.size());
			for (JcicZ444 iiJcicZ444 : lJcicZ444) {
				count++;
				if (iiJcicZ444.getOutJcicTxtDate() == iJcicDate && iiJcicZ444.getCustId().equals(sJcicZ444.getCustId())
						&& iiJcicZ444.getSubmitKey().equals(sJcicZ444.getSubmitKey())
						&& iiJcicZ444.getApplyDate() == sJcicZ444.getApplyDate()
						&& iiJcicZ444.getCourtCode().equals(sJcicZ444.getCourtCode())
						&& iiJcicZ444.getActualFilingDate() == 0) {

					uJcicZ444 = sJcicZ444Service.holdById(iiJcicZ444.getJcicZ444Id(), titaVo);
					oldJcicZ444 = (JcicZ444) iDataLog.clone(uJcicZ444);
					uJcicZ444.setOutJcicTxtDate(0);
					try {
						sJcicZ444Service.update(uJcicZ444, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ444Log iJcicZ444Log = sJcicZ444LogService.ukeyFirst(uJcicZ444.getUkey(), titaVo);
					JcicZ444 cJcicZ444 = sJcicZ444Service.ukeyFirst(uJcicZ444.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ444.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ444, uJcicZ444);
					iDataLog.exec("L8403取消報送", iJcicZ444Log.getUkey() + iJcicZ444Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "446":
			JcicZ446 uJcicZ446 = new JcicZ446();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ446Id jcicZ446Id = new JcicZ446Id();
			jcicZ446Id.setCustId(iCustId);
			jcicZ446Id.setSubmitKey(iSubmitKey);
			jcicZ446Id.setApplyDate(iApplyDate);
			jcicZ446Id.setCourtCode(iCourtCode);
			JcicZ446 sJcicZ446 = new JcicZ446();
			sJcicZ446 = sJcicZ446Service.findById(jcicZ446Id, titaVo);
			Slice<JcicZ446> JcicZ446S = sJcicZ446Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ446 oldJcicZ446 = new JcicZ446();
			List<JcicZ446> lJcicZ446 = JcicZ446S.getContent();
			this.info("iJcicZ446SIZE     =" + lJcicZ446.size());
			for (JcicZ446 iiJcicZ446 : lJcicZ446) {
				count++;
				if (iiJcicZ446.getOutJcicTxtDate() == iJcicDate && iiJcicZ446.getCustId().equals(sJcicZ446.getCustId())
						&& iiJcicZ446.getSubmitKey().equals(sJcicZ446.getSubmitKey())
						&& iiJcicZ446.getApplyDate() == sJcicZ446.getApplyDate()
						&& iiJcicZ446.getCourtCode().equals(sJcicZ446.getCourtCode())
						&& iiJcicZ446.getActualFilingDate() == 0) {

					uJcicZ446 = sJcicZ446Service.holdById(iiJcicZ446.getJcicZ446Id(), titaVo);
					oldJcicZ446 = (JcicZ446) iDataLog.clone(uJcicZ446);
					uJcicZ446.setOutJcicTxtDate(0);
					try {
						sJcicZ446Service.update(uJcicZ446, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ446Log iJcicZ446Log = sJcicZ446LogService.ukeyFirst(uJcicZ446.getUkey(), titaVo);
					JcicZ446 cJcicZ446 = sJcicZ446Service.ukeyFirst(uJcicZ446.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ446.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ446, uJcicZ446);
					iDataLog.exec("L8403取消報送", iJcicZ446Log.getUkey() + iJcicZ446Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "447":
			JcicZ447 uJcicZ447 = new JcicZ447();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			JcicZ447Id jcicZ447Id = new JcicZ447Id();
			jcicZ447Id.setCustId(iCustId);
			jcicZ447Id.setSubmitKey(iSubmitKey);
			jcicZ447Id.setApplyDate(iApplyDate);
			jcicZ447Id.setCourtCode(iCourtCode);
			JcicZ447 sJcicZ447 = new JcicZ447();
			sJcicZ447 = sJcicZ447Service.findById(jcicZ447Id, titaVo);
			Slice<JcicZ447> JcicZ447S = sJcicZ447Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ447 oldJcicZ447 = new JcicZ447();
			List<JcicZ447> lJcicZ447 = JcicZ447S.getContent();
			this.info("iJcicZ447SIZE     =" + lJcicZ447.size());
			for (JcicZ447 iiJcicZ447 : lJcicZ447) {
				count++;
				if (iiJcicZ447.getOutJcicTxtDate() == iJcicDate && iiJcicZ447.getCustId().equals(sJcicZ447.getCustId())
						&& iiJcicZ447.getSubmitKey().equals(sJcicZ447.getSubmitKey())
						&& iiJcicZ447.getApplyDate() == sJcicZ447.getApplyDate()
						&& iiJcicZ447.getCourtCode().equals(sJcicZ447.getCourtCode())
						&& iiJcicZ447.getActualFilingDate() == 0) {

					uJcicZ447 = sJcicZ447Service.holdById(iiJcicZ447.getJcicZ447Id(), titaVo);
					oldJcicZ447 = (JcicZ447) iDataLog.clone(uJcicZ447);
					uJcicZ447.setOutJcicTxtDate(0);
					try {
						sJcicZ447Service.update(uJcicZ447, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ447Log iJcicZ447Log = sJcicZ447LogService.ukeyFirst(uJcicZ447.getUkey(), titaVo);
					JcicZ447 cJcicZ447 = sJcicZ447Service.ukeyFirst(uJcicZ447.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ447.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ447, uJcicZ447);
					iDataLog.exec("L8403取消報送", iJcicZ447Log.getUkey() + iJcicZ447Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "448":
			JcicZ448 uJcicZ448 = new JcicZ448();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			JcicZ448Id jcicZ448Id = new JcicZ448Id();
			jcicZ448Id.setCustId(iCustId);
			jcicZ448Id.setSubmitKey(iSubmitKey);
			jcicZ448Id.setApplyDate(iApplyDate);
			jcicZ448Id.setCourtCode(iCourtCode);
			jcicZ448Id.setMaxMainCode(iMaxMainCode);
			JcicZ448 sJcicZ448 = new JcicZ448();
			sJcicZ448 = sJcicZ448Service.findById(jcicZ448Id, titaVo);
			Slice<JcicZ448> JcicZ448S = sJcicZ448Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ448 oldJcicZ448 = new JcicZ448();
			List<JcicZ448> lJcicZ448 = JcicZ448S.getContent();
			this.info("iJcicZ448SIZE     =" + lJcicZ448.size());
			for (JcicZ448 iiJcicZ448 : lJcicZ448) {
				count++;
				if (iiJcicZ448.getOutJcicTxtDate() == iJcicDate && iiJcicZ448.getCustId().equals(sJcicZ448.getCustId())
						&& iiJcicZ448.getSubmitKey().equals(sJcicZ448.getSubmitKey())
						&& iiJcicZ448.getApplyDate() == sJcicZ448.getApplyDate()
						&& iiJcicZ448.getCourtCode().equals(sJcicZ448.getCourtCode())
						&& iiJcicZ448.getMaxMainCode().equals(sJcicZ448.getMaxMainCode())
						&& iiJcicZ448.getActualFilingDate() == 0) {

					uJcicZ448 = sJcicZ448Service.holdById(iiJcicZ448.getJcicZ448Id(), titaVo);
					oldJcicZ448 = (JcicZ448) iDataLog.clone(uJcicZ448);
					uJcicZ448.setOutJcicTxtDate(0);
					try {
						sJcicZ448Service.update(uJcicZ448, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ448Log iJcicZ448Log = sJcicZ448LogService.ukeyFirst(uJcicZ448.getUkey(), titaVo);
					JcicZ448 cJcicZ448 = sJcicZ448Service.ukeyFirst(uJcicZ448.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ448.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ448, uJcicZ448);
					iDataLog.exec("L8403取消報送", iJcicZ448Log.getUkey() + iJcicZ448Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "450":
			JcicZ450 uJcicZ450 = new JcicZ450();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iPayDate = Integer.valueOf(titaVo.get("PayDate"));
			JcicZ450Id jcicZ450Id = new JcicZ450Id();
			jcicZ450Id.setCustId(iCustId);
			jcicZ450Id.setSubmitKey(iSubmitKey);
			jcicZ450Id.setApplyDate(iApplyDate);
			jcicZ450Id.setCourtCode(iCourtCode);
			jcicZ450Id.setPayDate(iPayDate);
			JcicZ450 sJcicZ450 = new JcicZ450();
			sJcicZ450 = sJcicZ450Service.findById(jcicZ450Id, titaVo);
			Slice<JcicZ450> JcicZ450S = sJcicZ450Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ450 oldJcicZ450 = new JcicZ450();
			List<JcicZ450> lJcicZ450 = JcicZ450S.getContent();
			this.info("iJcicZ450SIZE     =" + lJcicZ450.size());
			for (JcicZ450 iiJcicZ450 : lJcicZ450) {
				count++;
				if (iiJcicZ450.getOutJcicTxtDate() == iJcicDate && iiJcicZ450.getCustId().equals(sJcicZ450.getCustId())
						&& iiJcicZ450.getSubmitKey().equals(sJcicZ450.getSubmitKey())
						&& iiJcicZ450.getApplyDate() == sJcicZ450.getApplyDate()
						&& iiJcicZ450.getCourtCode().equals(sJcicZ450.getCourtCode())
						&& iiJcicZ450.getPayDate() == sJcicZ450.getPayDate() && iiJcicZ450.getActualFilingDate() == 0) {

					uJcicZ450 = sJcicZ450Service.holdById(iiJcicZ450.getJcicZ450Id(), titaVo);
					oldJcicZ450 = (JcicZ450) iDataLog.clone(uJcicZ450);
					uJcicZ450.setOutJcicTxtDate(0);
					try {
						sJcicZ450Service.update(uJcicZ450, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ450Log iJcicZ450Log = sJcicZ450LogService.ukeyFirst(uJcicZ450.getUkey(), titaVo);
					JcicZ450 cJcicZ450 = sJcicZ450Service.ukeyFirst(uJcicZ450.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ450.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ450, uJcicZ450);
					iDataLog.exec("L8403取消報送", iJcicZ450Log.getUkey() + iJcicZ450Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "451":
			JcicZ451 uJcicZ451 = new JcicZ451();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iDelayYM = Integer.valueOf(titaVo.get("DelayYM"));
			JcicZ451Id jcicZ451Id = new JcicZ451Id();
			jcicZ451Id.setCustId(iCustId);
			jcicZ451Id.setSubmitKey(iSubmitKey);
			jcicZ451Id.setApplyDate(iApplyDate);
			jcicZ451Id.setCourtCode(iCourtCode);
			jcicZ451Id.setDelayYM(iDelayYM);
			JcicZ451 sJcicZ451 = new JcicZ451();
			sJcicZ451 = sJcicZ451Service.findById(jcicZ451Id, titaVo);
			Slice<JcicZ451> JcicZ451S = sJcicZ451Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ451 oldJcicZ451 = new JcicZ451();
			List<JcicZ451> lJcicZ451 = JcicZ451S.getContent();
			this.info("iJcicZ451SIZE     =" + lJcicZ451.size());
			for (JcicZ451 iiJcicZ451 : lJcicZ451) {
				count++;
				if (iiJcicZ451.getOutJcicTxtDate() == iJcicDate && iiJcicZ451.getCustId().equals(sJcicZ451.getCustId())
						&& iiJcicZ451.getSubmitKey().equals(sJcicZ451.getSubmitKey())
						&& iiJcicZ451.getApplyDate() == sJcicZ451.getApplyDate()
						&& iiJcicZ451.getCourtCode().equals(sJcicZ451.getCourtCode())
						&& iiJcicZ451.getDelayYM() == sJcicZ451.getDelayYM() && iiJcicZ451.getActualFilingDate() == 0) {

					uJcicZ451 = sJcicZ451Service.holdById(iiJcicZ451.getJcicZ451Id(), titaVo);
					oldJcicZ451 = (JcicZ451) iDataLog.clone(uJcicZ451);
					uJcicZ451.setOutJcicTxtDate(0);
					try {
						sJcicZ451Service.update(uJcicZ451, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ451Log iJcicZ451Log = sJcicZ451LogService.ukeyFirst(uJcicZ451.getUkey(), titaVo);
					JcicZ451 cJcicZ451 = sJcicZ451Service.ukeyFirst(uJcicZ451.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ451.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ451, uJcicZ451);
					iDataLog.exec("L8403取消報送", iJcicZ451Log.getUkey() + iJcicZ451Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "454":
			JcicZ454 uJcicZ454 = new JcicZ454();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iCourtCode = titaVo.get("CourtCode");
			iMaxMainCode = titaVo.get("MaxMainCode");
			JcicZ454Id jcicZ454Id = new JcicZ454Id();
			jcicZ454Id.setCustId(iCustId);
			jcicZ454Id.setSubmitKey(iSubmitKey);
			jcicZ454Id.setApplyDate(iApplyDate);
			jcicZ454Id.setCourtCode(iCourtCode);
			jcicZ454Id.setMaxMainCode(iMaxMainCode);
			JcicZ454 sJcicZ454 = new JcicZ454();
			sJcicZ454 = sJcicZ454Service.findById(jcicZ454Id, titaVo);
			Slice<JcicZ454> JcicZ454S = sJcicZ454Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ454 oldJcicZ454 = new JcicZ454();
			List<JcicZ454> lJcicZ454 = JcicZ454S.getContent();
			this.info("iJcicZ454SIZE     =" + lJcicZ454.size());
			for (JcicZ454 iiJcicZ454 : lJcicZ454) {
				count++;
				if (iiJcicZ454.getOutJcicTxtDate() == iJcicDate && iiJcicZ454.getCustId().equals(sJcicZ454.getCustId())
						&& iiJcicZ454.getSubmitKey().equals(sJcicZ454.getSubmitKey())
						&& iiJcicZ454.getApplyDate() == sJcicZ454.getApplyDate()
						&& iiJcicZ454.getCourtCode().equals(sJcicZ454.getCourtCode())
						&& iiJcicZ454.getMaxMainCode().equals(sJcicZ454.getMaxMainCode())
						&& iiJcicZ454.getActualFilingDate() == 0) {

					uJcicZ454 = sJcicZ454Service.holdById(iiJcicZ454.getJcicZ454Id(), titaVo);
					oldJcicZ454 = (JcicZ454) iDataLog.clone(uJcicZ454);
					uJcicZ454.setOutJcicTxtDate(0);
					try {
						sJcicZ454Service.update(uJcicZ454, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ454Log iJcicZ454Log = sJcicZ454LogService.ukeyFirst(uJcicZ454.getUkey(), titaVo);
					JcicZ454 cJcicZ454 = sJcicZ454Service.ukeyFirst(uJcicZ454.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ454.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ454, uJcicZ454);
					iDataLog.exec("L8403取消報送", iJcicZ454Log.getUkey() + iJcicZ454Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "570":
			JcicZ570 uJcicZ570 = new JcicZ570();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			JcicZ570Id jcicZ570Id = new JcicZ570Id();
			jcicZ570Id.setCustId(iCustId);
			jcicZ570Id.setSubmitKey(iSubmitKey);
			jcicZ570Id.setApplyDate(iApplyDate);
			JcicZ570 sJcicZ570 = new JcicZ570();
			sJcicZ570 = sJcicZ570Service.findById(jcicZ570Id, titaVo);
			Slice<JcicZ570> JcicZ570S = sJcicZ570Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ570 oldJcicZ570 = new JcicZ570();
			List<JcicZ570> lJcicZ570 = JcicZ570S.getContent();
			this.info("iJcicZ570SIZE     =" + lJcicZ570.size());
			for (JcicZ570 iiJcicZ570 : lJcicZ570) {
				count++;
				if (iiJcicZ570.getOutJcicTxtDate() == iJcicDate && iiJcicZ570.getCustId().equals(sJcicZ570.getCustId())
						&& iiJcicZ570.getSubmitKey().equals(sJcicZ570.getSubmitKey())
						&& iiJcicZ570.getApplyDate() == sJcicZ570.getApplyDate()
						&& iiJcicZ570.getActualFilingDate() == 0) {

					uJcicZ570 = sJcicZ570Service.holdById(iiJcicZ570.getJcicZ570Id(), titaVo);
					oldJcicZ570 = (JcicZ570) iDataLog.clone(uJcicZ570);
					uJcicZ570.setOutJcicTxtDate(0);
					try {
						sJcicZ570Service.update(uJcicZ570, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ570Log iJcicZ570Log = sJcicZ570LogService.ukeyFirst(uJcicZ570.getUkey(), titaVo);
					JcicZ570 cJcicZ570 = sJcicZ570Service.ukeyFirst(uJcicZ570.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ570.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ570, uJcicZ570);
					iDataLog.exec("L8403取消報送", iJcicZ570Log.getUkey() + iJcicZ570Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "571":
			JcicZ571 uJcicZ571 = new JcicZ571();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iBankId = titaVo.getParam("BankId");
			JcicZ571Id jcicZ571Id = new JcicZ571Id();
			jcicZ571Id.setCustId(iCustId);
			jcicZ571Id.setSubmitKey(iSubmitKey);
			jcicZ571Id.setApplyDate(iApplyDate);
			jcicZ571Id.setBankId(iBankId);
			JcicZ571 sJcicZ571 = new JcicZ571();
			sJcicZ571 = sJcicZ571Service.findById(jcicZ571Id, titaVo);
			Slice<JcicZ571> JcicZ571S = sJcicZ571Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ571 oldJcicZ571 = new JcicZ571();
			List<JcicZ571> lJcicZ571 = JcicZ571S.getContent();
			this.info("iJcicZ571SIZE     =" + lJcicZ571.size());
			for (JcicZ571 iiJcicZ571 : lJcicZ571) {
				count++;
				if (iiJcicZ571.getOutJcicTxtDate() == iJcicDate && iiJcicZ571.getCustId().equals(sJcicZ571.getCustId())
						&& iiJcicZ571.getSubmitKey().equals(sJcicZ571.getSubmitKey())
						&& iiJcicZ571.getApplyDate() == sJcicZ571.getApplyDate()
						&& iiJcicZ571.getBankId().equals(sJcicZ571.getBankId())
						&& iiJcicZ571.getActualFilingDate() == 0) {

					uJcicZ571 = sJcicZ571Service.holdById(iiJcicZ571.getJcicZ571Id(), titaVo);
					oldJcicZ571 = (JcicZ571) iDataLog.clone(uJcicZ571);
					uJcicZ571.setOutJcicTxtDate(0);
					try {
						sJcicZ571Service.update(uJcicZ571, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ571Log iJcicZ571Log = sJcicZ571LogService.ukeyFirst(uJcicZ571.getUkey(), titaVo);
					JcicZ571 cJcicZ571 = sJcicZ571Service.ukeyFirst(uJcicZ571.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ571.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ571, uJcicZ571);
					iDataLog.exec("L8403取消報送", iJcicZ571Log.getUkey() + iJcicZ571Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "572":
			JcicZ572 uJcicZ572 = new JcicZ572();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iPayDate = Integer.valueOf(titaVo.get("PayDate"));
			iBankId = titaVo.getParam("BankId");
			JcicZ572Id jcicZ572Id = new JcicZ572Id();
			jcicZ572Id.setCustId(iCustId);
			jcicZ572Id.setSubmitKey(iSubmitKey);
			jcicZ572Id.setApplyDate(iApplyDate);
			jcicZ572Id.setPayDate(iPayDate);
			jcicZ572Id.setBankId(iBankId);
			JcicZ572 sJcicZ572 = new JcicZ572();
			sJcicZ572 = sJcicZ572Service.findById(jcicZ572Id, titaVo);
			Slice<JcicZ572> JcicZ572S = sJcicZ572Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ572 oldJcicZ572 = new JcicZ572();
			List<JcicZ572> lJcicZ572 = JcicZ572S.getContent();
			this.info("iJcicZ572SIZE     =" + lJcicZ572.size());
			for (JcicZ572 iiJcicZ572 : lJcicZ572) {
				count++;
				if (iiJcicZ572.getOutJcicTxtDate() == iJcicDate && iiJcicZ572.getCustId().equals(sJcicZ572.getCustId())
						&& iiJcicZ572.getSubmitKey().equals(sJcicZ572.getSubmitKey())
						&& iiJcicZ572.getApplyDate() == sJcicZ572.getApplyDate()
						&& iiJcicZ572.getPayDate() == sJcicZ572.getPayDate()
						&& iiJcicZ572.getBankId().equals(sJcicZ572.getBankId())
						&& iiJcicZ572.getActualFilingDate() == 0) {

					uJcicZ572 = sJcicZ572Service.holdById(iiJcicZ572.getJcicZ572Id(), titaVo);
					oldJcicZ572 = (JcicZ572) iDataLog.clone(uJcicZ572);
					uJcicZ572.setOutJcicTxtDate(0);
					try {
						sJcicZ572Service.update(uJcicZ572, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ572Log iJcicZ572Log = sJcicZ572LogService.ukeyFirst(uJcicZ572.getUkey(), titaVo);
					JcicZ572 cJcicZ572 = sJcicZ572Service.ukeyFirst(uJcicZ572.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ572.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ572, uJcicZ572);
					iDataLog.exec("L8403取消報送", iJcicZ572Log.getUkey() + iJcicZ572Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "573":
			JcicZ573 uJcicZ573 = new JcicZ573();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iPayDate = Integer.valueOf(titaVo.get("PayDate"));
			JcicZ573Id jcicZ573Id = new JcicZ573Id();
			jcicZ573Id.setCustId(iCustId);
			jcicZ573Id.setSubmitKey(iSubmitKey);
			jcicZ573Id.setApplyDate(iApplyDate);
			jcicZ573Id.setPayDate(iPayDate);
			JcicZ573 sJcicZ573 = new JcicZ573();
			sJcicZ573 = sJcicZ573Service.findById(jcicZ573Id, titaVo);
			Slice<JcicZ573> JcicZ573S = sJcicZ573Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ573 oldJcicZ573 = new JcicZ573();
			List<JcicZ573> lJcicZ573 = JcicZ573S.getContent();
			this.info("iJcicZ573SIZE     =" + lJcicZ573.size());
			for (JcicZ573 iiJcicZ573 : lJcicZ573) {
				count++;
				if (iiJcicZ573.getOutJcicTxtDate() == iJcicDate && iiJcicZ573.getCustId().equals(sJcicZ573.getCustId())
						&& iiJcicZ573.getSubmitKey().equals(sJcicZ573.getSubmitKey())
						&& iiJcicZ573.getApplyDate() == sJcicZ573.getApplyDate()
						&& iiJcicZ573.getPayDate() == sJcicZ573.getPayDate() && iiJcicZ573.getActualFilingDate() == 0) {

					uJcicZ573 = sJcicZ573Service.holdById(iiJcicZ573.getJcicZ573Id(), titaVo);
					oldJcicZ573 = (JcicZ573) iDataLog.clone(uJcicZ573);
					uJcicZ573.setOutJcicTxtDate(0);
					try {
						sJcicZ573Service.update(uJcicZ573, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ573Log iJcicZ573Log = sJcicZ573LogService.ukeyFirst(uJcicZ573.getUkey(), titaVo);
					JcicZ573 cJcicZ573 = sJcicZ573Service.ukeyFirst(uJcicZ573.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ573.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ573, uJcicZ573);
					iDataLog.exec("L8403取消報送", iJcicZ573Log.getUkey() + iJcicZ573Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "574":
			JcicZ574 uJcicZ574 = new JcicZ574();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			JcicZ574Id jcicZ574Id = new JcicZ574Id();
			jcicZ574Id.setCustId(iCustId);
			jcicZ574Id.setSubmitKey(iSubmitKey);
			jcicZ574Id.setApplyDate(iApplyDate);
			JcicZ574 sJcicZ574 = new JcicZ574();
			sJcicZ574 = sJcicZ574Service.findById(jcicZ574Id, titaVo);
			Slice<JcicZ574> JcicZ574S = sJcicZ574Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ574 oldJcicZ574 = new JcicZ574();
			List<JcicZ574> lJcicZ574 = JcicZ574S.getContent();
			this.info("iJcicZ574SIZE     =" + lJcicZ574.size());
			for (JcicZ574 iiJcicZ574 : lJcicZ574) {
				count++;
				if (iiJcicZ574.getOutJcicTxtDate() == iJcicDate && iiJcicZ574.getCustId().equals(sJcicZ574.getCustId())
						&& iiJcicZ574.getSubmitKey().equals(sJcicZ574.getSubmitKey())
						&& iiJcicZ574.getApplyDate() == sJcicZ574.getApplyDate()
						&& iiJcicZ574.getActualFilingDate() == 0) {

					uJcicZ574 = sJcicZ574Service.holdById(iiJcicZ574.getJcicZ574Id(), titaVo);
					oldJcicZ574 = (JcicZ574) iDataLog.clone(uJcicZ574);
					uJcicZ574.setOutJcicTxtDate(0);
					try {
						sJcicZ574Service.update(uJcicZ574, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ574Log iJcicZ574Log = sJcicZ574LogService.ukeyFirst(uJcicZ574.getUkey(), titaVo);
					JcicZ574 cJcicZ574 = sJcicZ574Service.ukeyFirst(uJcicZ574.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ574.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ574, uJcicZ574);
					iDataLog.exec("L8403取消報送", iJcicZ574Log.getUkey() + iJcicZ574Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		case "575":
			JcicZ575 uJcicZ575 = new JcicZ575();
			iCustId = titaVo.getParam("CustId");
			iSubmitKey = titaVo.get("SubmitKey");
			iApplyDate = Integer.valueOf(titaVo.get("ApplyDate"));
			iBankId = titaVo.getParam("BankId");
			JcicZ575Id jcicZ575Id = new JcicZ575Id();
			jcicZ575Id.setCustId(iCustId);
			jcicZ575Id.setSubmitKey(iSubmitKey);
			jcicZ575Id.setApplyDate(iApplyDate);
			jcicZ575Id.setBankId(iBankId);
			JcicZ575 sJcicZ575 = new JcicZ575();
			sJcicZ575 = sJcicZ575Service.findById(jcicZ575Id, titaVo);
			Slice<JcicZ575> JcicZ575S = sJcicZ575Service.findAll(0, Integer.MAX_VALUE, titaVo);
			JcicZ575 oldJcicZ575 = new JcicZ575();
			List<JcicZ575> lJcicZ575 = JcicZ575S.getContent();
			this.info("iJcicZ575SIZE     =" + lJcicZ575.size());
			for (JcicZ575 iiJcicZ575 : lJcicZ575) {
				count++;
				if (iiJcicZ575.getOutJcicTxtDate() == iJcicDate && iiJcicZ575.getCustId().equals(sJcicZ575.getCustId())
						&& iiJcicZ575.getSubmitKey().equals(sJcicZ575.getSubmitKey())
						&& iiJcicZ575.getApplyDate() == sJcicZ575.getApplyDate()
						&& iiJcicZ575.getBankId().equals(sJcicZ575.getBankId())
						&& iiJcicZ575.getActualFilingDate() == 0) {

					uJcicZ575 = sJcicZ575Service.holdById(iiJcicZ575.getJcicZ575Id(), titaVo);
					oldJcicZ575 = (JcicZ575) iDataLog.clone(uJcicZ575);
					uJcicZ575.setOutJcicTxtDate(0);
					try {
						sJcicZ575Service.update(uJcicZ575, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新報送JCIC日期時發生錯誤");
					}
					JcicZ575Log iJcicZ575Log = sJcicZ575LogService.ukeyFirst(uJcicZ575.getUkey(), titaVo);
					JcicZ575 cJcicZ575 = sJcicZ575Service.ukeyFirst(uJcicZ575.getUkey(), titaVo);
					CustMain tCustMain = sCustMainService.custIdFirst(cJcicZ575.getCustId(), titaVo);
					int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
					titaVo.putParam("CustNo", iCustNo);
					iDataLog.setEnv(titaVo, oldJcicZ575, uJcicZ575);
					iDataLog.exec("L8403取消報送", iJcicZ575Log.getUkey() + iJcicZ575Log.getTxSeq());
				}
			}
			if (count == 0) {
				throw new LogicException(titaVo, "E2003", "查無該轉出日期資料");
			}
			break;
		}
	}

	public String dealBankName(String BankId, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = new CdCode();
		tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", BankId, titaVo);
		String JcicBankName = "";// 80碼長度
		if (tCdCode != null) {
			JcicBankName = tCdCode.getItem();
		}
		return JcicBankName;
	}
}
