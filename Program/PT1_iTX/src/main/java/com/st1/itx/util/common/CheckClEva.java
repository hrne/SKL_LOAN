package com.st1.itx.util.common;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.domain.CdInsurerId;
import com.st1.itx.db.domain.ClEva;
import com.st1.itx.db.domain.ClEvaId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.CdInsurerService;
import com.st1.itx.db.service.ClEvaService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("checkClEva")
@Scope("prototype")
public class CheckClEva extends CommBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	@Autowired
	public ClImmService sClImmService;

	@Autowired
	public ClFacService sClFacService;

	@Autowired
	public ClEvaService sClEvaService;

	@Autowired
	public LoanAvailableAmt loanAvailableAmt;

	@Autowired
	public CdInsurerService sCdInsurerService;

	@Autowired
	public FacCaseApplService facCaseApplService;

	@Autowired
	public DataLog dataLog;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	BigDecimal shareTotal = BigDecimal.ZERO;

	String wkWarningMsg = "";

	// EvaReason待新增eloan mapping
	// 01:新貸件 02:展期件 03:增貸件 04:動支件
	public void setClEva(TitaVo titaVo, int iClNo) throws LogicException {

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		String creditCode = titaVo.get("CreditCode");
		if (!creditCode.isEmpty()) {
			creditCode = FormatUtil.pad9(creditCode, 2);
		}

		int EvaReason = 0;
		String EvaReasonX = "";
		switch (creditCode) {
		case "01":
		case "02":
		case "03":
			EvaReason = 1;
			EvaReasonX = "新貸件";
			break;
		case "06":
			EvaReason = 2;
			EvaReasonX = "展期件";
			break;
		case "05":
			EvaReason = 3;
			EvaReasonX = "增貸件";
			break;
		case "04":
			EvaReason = 4;
			EvaReasonX = "動支件";
			break;
		case "12":
			EvaReason = 12;
			EvaReasonX = "貸前授變";
			break;
		default:
			break;
		}
		ClEva tClEva = new ClEva();
		ClEvaId tClEvaId = new ClEvaId();
		tClEva = sClEvaService.ClNoFirst(iClCode1, iClCode2, iClNo);

		int newEvaNo = 1;

		if (tClEva != null) { // 新增時，若存在則抓最新的序號+1
			newEvaNo += tClEva.getEvaNo();
		} else { // 新增一筆00原始的資料
			setOriginalClEva(titaVo, iClNo);
		}

		tClEva = sClEvaService.ClNoFirst(iClCode1, iClCode2, iClNo); // 新增完00的資料再查一次

		ClImm tClImm = new ClImm();

		tClImm = sClImmService.holdById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);

		if (tClEva.getEvaDate() != parse.stringToInteger(titaVo.getParam("EvaDate"))) { // 鑑估日期跟上次重評日期不同

			tClEvaId = new ClEvaId();
			tClEvaId.setClCode1(iClCode1);
			tClEvaId.setClCode2(iClCode2);
			tClEvaId.setClNo(iClNo);
			tClEvaId.setEvaNo(newEvaNo);

			tClEva = new ClEva();
			tClEva.setClEvaId(tClEvaId);
			tClEva.setClCode1(iClCode1);
			tClEva.setClCode2(iClCode2);
			tClEva.setClNo(iClNo);
			tClEva.setEvaNo(newEvaNo);
			tClEva.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			tClEva.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
			tClEva.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
			tClEva.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));
			tClEva.setEvaCompanyId(titaVo.getParam("EvaCompany"));

			// 查詢保險公司資料檔
			CdInsurer tCdInsurer = sCdInsurerService.findById(new CdInsurerId("2", tClImm.getEvaCompanyCode()), titaVo);

			tClEva.setEvaCompanyName("");
			if (tCdInsurer != null) {
				tClEva.setEvaCompanyName(tCdInsurer.getInsurerItem());
			}

			tClEva.setEvaEmpno("");
			tClEva.setEvaReason(EvaReason);
			tClEva.setOtherReason(EvaReasonX);

			checkAmt(titaVo, iClNo);

			try {
				sClEvaService.insert(tClEva, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品重評資料檔");
			}
		} // if

		ClMain tClMain = new ClMain();

		ClMainId ClMainId = new ClMainId();
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		tClMain = sClMainService.holdById(ClMainId, titaVo);

		// 變更前
		ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);

		tClMain.setShareTotal(shareTotal);
		tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
		tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));

		ClImm t = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
		if (t == null) {
			throw new LogicException("E0001", "該擔保品編號不存在擔保品不動產檔 =" + iClCode1 + -+iClCode2 + -+iClNo);
		}
		if ("1".equals(t.getClStat()) || "2".equals(t.getSettingStat())) {
			tClMain.setShareTotal(BigDecimal.ZERO);
		} else {
			tClMain.setShareTotal(shareTotal);
		}
		try {
			tClMain = sClMainService.update2(tClMain, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "擔保品主檔");
		}

		// 紀錄變更前變更後
		dataLog.setEnv(titaVo, beforeClMain, tClMain);
		dataLog.exec();

		// 變更前
		ClImm beforeClImm = (ClImm) dataLog.clone(tClImm);

		tClImm.setEvaCompanyCode(titaVo.getParam("EvaCompany"));
		// 評估淨值
		tClImm.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
		// 出租評估淨值
		tClImm.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));

		try {
			tClImm = sClImmService.update(tClImm, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "擔保品不動產檔");
		}

		// 紀錄變更前變更後
		dataLog.setEnv(titaVo, beforeClImm, tClImm);
		dataLog.exec();

	}

	public void setOriginalClEva(TitaVo titaVo, int iClNo) throws LogicException {

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));

		ClImm tClImm = new ClImm();

		tClImm = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);

		ClMain tClMain = new ClMain();

		ClMainId ClMainId = new ClMainId();
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		// 檢查該擔保品編號是否存在擔保品主檔
		tClMain = sClMainService.findById(ClMainId, titaVo);

		// 該擔保品編號不存在 拋錯
		if (tClMain == null) {
			throw new LogicException("E0001", "L2480該擔保品編號不存在擔保品主檔(ClMain)");
		}

		// 組ClEva PK
		ClEva tClEva = new ClEva();
		ClEvaId tClEvaId = new ClEvaId();

		tClEvaId = new ClEvaId();
		tClEvaId.setClCode1(iClCode1);
		tClEvaId.setClCode2(iClCode2);
		tClEvaId.setClNo(iClNo);
		tClEvaId.setEvaNo(0);

		tClEva = new ClEva();
		tClEva.setClEvaId(tClEvaId);
		tClEva.setClCode1(iClCode1);
		tClEva.setClCode2(iClCode2);
		tClEva.setClNo(iClNo);
		tClEva.setEvaNo(0);
		tClEva.setEvaDate(tClMain.getEvaDate());
		tClEva.setEvaAmt(tClMain.getEvaAmt());
		tClEva.setEvaNetWorth(tClImm.getEvaNetWorth());
		tClEva.setRentEvaValue(tClImm.getRentEvaValue());
		tClEva.setEvaCompanyId(tClImm.getEvaCompanyCode());

		// 查詢保險公司資料檔
		CdInsurer tCdInsurer = sCdInsurerService.findById(new CdInsurerId("2", tClImm.getEvaCompanyCode()), titaVo);

		tClEva.setEvaCompanyName("");
		if (tCdInsurer != null) {
			tClEva.setEvaCompanyName(tCdInsurer.getInsurerItem());
		}
		tClEva.setEvaEmpno("");
		tClEva.setEvaReason(0);
		tClEva.setOtherReason("");

		checkAmt(titaVo, iClNo);

		try {
			sClEvaService.insert(tClEva, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "擔保品重評資料檔");
		}
	}

	public BigDecimal checkAmt(TitaVo titaVo, int iClNo) throws LogicException {

		/*
		 * loanToValue 貸放成數 shareTotal 可分配金額 settingAmt 設定金額 evaAmt 鑑估總價 shareAmtSum
		 * 計算同擔保品分配金額加總
		 */

		BigDecimal loanToValue = BigDecimal.ZERO;

		shareTotal = BigDecimal.ZERO;

		BigDecimal settingAmt = BigDecimal.ZERO;

		parse.stringToBigDecimal(titaVo.getParam("EvaAmt"));

		BigDecimal shareAmtSum = new BigDecimal(0);

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));

		// 取貸放成數,設定金額
		ClImm tClImm = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
		loanToValue = tClImm.getLoanToValue();
		settingAmt = tClImm.getSettingAmt();
		BigDecimal shareCompAmt = BigDecimal.ZERO;
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkEvaAmt = parse.stringToBigDecimal(titaVo.getParam("EvaAmt"));
		BigDecimal wkEvaNetWorth = parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth"));

		// 評估淨值有值時擺評估淨值,否則擺鑑估總值.
		if (wkEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
			shareCompAmt = wkEvaNetWorth;
		} else {
			shareCompAmt = wkEvaAmt;
		}

		shareTotal = shareCompAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0,
				BigDecimal.ROUND_HALF_UP);

		// 分配金額和設定金額比較 較低的為可分配金額
		this.info("分配金額和設定金額比較 = " + shareTotal + "," + settingAmt);
		if (settingAmt.compareTo(shareTotal) < 0) {
			shareTotal = settingAmt;
		}

		ClImm t = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
		if (t == null) {
			throw new LogicException("E0001", "該擔保品編號不存在擔保品不動產檔 =" + iClCode1 + -+iClCode2 + -+iClNo);
		}
		if ("1".equals(t.getClStat()) || "2".equals(t.getSettingStat())) {
			shareTotal = BigDecimal.ZERO;
		}

		wkAvailable = loanAvailableAmt.checkClAvailable(iClCode1, iClCode2, iClNo, shareTotal, titaVo); // 可用額度

		return wkAvailable;

	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}

}
