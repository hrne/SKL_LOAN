package com.st1.itx.trade.L8;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;
import com.st1.itx.db.domain.JcicZ043Log;
import com.st1.itx.db.service.CustMainService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ043Service;

import com.st1.itx.db.service.JcicZ043LogService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;


@Service("L8304")
@Scope("prototype")
/**
 * @author Mata
 * @version 1.0.0
 */
public class L8304 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ043LogService sJcicZ043LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8304 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		String iAccount = titaVo.getParam("Account").trim();
		String iCollateralType = titaVo.getParam("CollateralType").trim();
		BigDecimal iOriginLoanAmt = new BigDecimal(titaVo.getParam("OriginLoanAmt").trim());
		BigDecimal iCreditBalance = new BigDecimal(titaVo.getParam("CreditBalance").trim());
		BigDecimal iPerPeriordAmt = new BigDecimal(titaVo.getParam("PerPeriordAmt").trim());
		BigDecimal iLastPayAmt = new BigDecimal(titaVo.getParam("LastPayAmt").trim());
		int iLastPayDate = Integer.valueOf(titaVo.getParam("LastPayDate").trim());
		BigDecimal iOutstandAmt = new BigDecimal(titaVo.getParam("OutstandAmt").trim());
		int iRepayPerMonDay = Integer.valueOf(titaVo.getParam("RepayPerMonDay").trim());
		int iContractStartYM = Integer.valueOf(titaVo.getParam("ContractStartYM").trim());
		int iContractEndYM = Integer.valueOf(titaVo.getParam("ContractEndYM").trim());
		String iKey = "";
		
		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);
		
		// JcicZ043, JcicZ040, JcicZ053
		JcicZ043 iJcicZ043 = new JcicZ043();
		JcicZ043Id iJcicZ043Id = new JcicZ043Id();
		iJcicZ043Id.setCustId(iCustId);// 債務人IDN
		iJcicZ043Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ043Id.setRcDate(iRcDate);
		iJcicZ043Id.setMaxMainCode(iMaxMainCode);
		iJcicZ043Id.setAccount(iAccount);
		JcicZ043 chJcicZ043 = new JcicZ043();
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);// 債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);
		String[] irCollateralType = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09" };// 不合規的「擔保品類別」代號集合

		// 檢核項目(D-9)
		if (!"4".equals(iTranKey_Tmp)) {

			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 2 start 完整key值未曾報送過'40':前置協商受理申請暨請求回報債權通知則予以剔退
				iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
				if (iJcicZ040 == null) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
					} else {
						throw new LogicException("E0007", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
					}
				} // 2 end

				// 3 金融機構報送日大於協商申請日+25則予以剔退***J

				// 4 start 第9欄「擔保品類別」代號不可為'00'~'09'.
				if (Arrays.stream(irCollateralType).anyMatch(iCollateralType::equals)) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "「擔保品類別」代號不可為'00'~'09'.");
					} else {
						throw new LogicException("E0007", "「擔保品類別」代號不可為'00'~'09'.");
					}
				} // 4 end

				// 檢核項目 end

			}
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ043
			chJcicZ043 = sJcicZ043Service.findById(iJcicZ043Id, titaVo);
			if (chJcicZ043 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ043.setJcicZ043Id(iJcicZ043Id);
			iJcicZ043.setTranKey(iTranKey);
			iJcicZ043.setCollateralType(iCollateralType);
			iJcicZ043.setOriginLoanAmt(iOriginLoanAmt);
			iJcicZ043.setCreditBalance(iCreditBalance);
			iJcicZ043.setPerPeriordAmt(iPerPeriordAmt);
			iJcicZ043.setLastPayAmt(iLastPayAmt);
			iJcicZ043.setLastPayDate(iLastPayDate);
			iJcicZ043.setOutstandAmt(iOutstandAmt);
			iJcicZ043.setRepayPerMonDay(iRepayPerMonDay);
			iJcicZ043.setContractStartYM(iContractStartYM);
			iJcicZ043.setContractEndYM(iContractEndYM);
			iJcicZ043.setUkey(iKey);
			try {
				sJcicZ043Service.insert(iJcicZ043, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ043 = sJcicZ043Service.ukeyFirst(iKey, titaVo);
			JcicZ043 uJcicZ043 = new JcicZ043();
			uJcicZ043 = sJcicZ043Service.holdById(iJcicZ043.getJcicZ043Id(), titaVo);
			if (uJcicZ043 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ043 oldJcicZ043 = (JcicZ043) iDataLog.clone(uJcicZ043);
			uJcicZ043.setTranKey(iTranKey);
			uJcicZ043.setCollateralType(iCollateralType);
			uJcicZ043.setOriginLoanAmt(iOriginLoanAmt);
			uJcicZ043.setCreditBalance(iCreditBalance);
			uJcicZ043.setPerPeriordAmt(iPerPeriordAmt);
			uJcicZ043.setLastPayAmt(iLastPayAmt);
			uJcicZ043.setLastPayDate(iLastPayDate);
			uJcicZ043.setOutstandAmt(iOutstandAmt);
			uJcicZ043.setRepayPerMonDay(iRepayPerMonDay);
			uJcicZ043.setContractStartYM(iContractStartYM);
			uJcicZ043.setContractEndYM(iContractEndYM);
			uJcicZ043.setOutJcicTxtDate(0);
			try {
				sJcicZ043Service.update(uJcicZ043, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			this.info("進入6932 ================ L8301");
			this.info("UKey    ===== " + uJcicZ043.getUkey());

			iDataLog.setEnv(titaVo, oldJcicZ043, uJcicZ043);
			iDataLog.exec("L8304異動", uJcicZ043.getSubmitKey()+uJcicZ043.getCustId()+uJcicZ043.getRcDate()+uJcicZ043.getMaxMainCode()+uJcicZ043.getAccount());
			break;
			// 2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ043 = sJcicZ043Service.ukeyFirst(iKey, titaVo);
			JcicZ043 uJcicZ0432 = new JcicZ043();
			uJcicZ0432 = sJcicZ043Service.holdById(iJcicZ043.getJcicZ043Id(), titaVo);
			iJcicZ043 = sJcicZ043Service.findById(iJcicZ043Id);
			if (iJcicZ043 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ043 oldJcicZ0432 = (JcicZ043) iDataLog.clone(uJcicZ0432);
			uJcicZ0432.setTranKey(iTranKey);
			uJcicZ0432.setCollateralType(iCollateralType);
			uJcicZ0432.setOriginLoanAmt(iOriginLoanAmt);
			uJcicZ0432.setCreditBalance(iCreditBalance);
			uJcicZ0432.setPerPeriordAmt(iPerPeriordAmt);
			uJcicZ0432.setLastPayAmt(iLastPayAmt);
			uJcicZ0432.setLastPayDate(iLastPayDate);
			uJcicZ0432.setOutstandAmt(iOutstandAmt);
			uJcicZ0432.setRepayPerMonDay(iRepayPerMonDay);
			uJcicZ0432.setContractStartYM(iContractStartYM);
			uJcicZ0432.setContractEndYM(iContractEndYM);
			uJcicZ0432.setOutJcicTxtDate(0);
			
			Slice<JcicZ043Log> dJcicLogZ043 = null;
			dJcicLogZ043 = sJcicZ043LogService.ukeyEq(iJcicZ043.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ043 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ043Service.delete(iJcicZ043, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ043Log iJcicZ043Log = dJcicLogZ043.getContent().get(0);
				iJcicZ043.setCollateralType(iJcicZ043Log.getCollateralType());
				iJcicZ043.setOriginLoanAmt(iJcicZ043Log.getOriginLoanAmt());
				iJcicZ043.setCreditBalance(iJcicZ043Log.getCreditBalance());
				iJcicZ043.setPerPeriordAmt(iJcicZ043Log.getPerPeriordAmt());
				iJcicZ043.setLastPayAmt(iJcicZ043Log.getLastPayAmt());
				iJcicZ043.setLastPayDate(iJcicZ043Log.getLastPayDate());
				iJcicZ043.setOutstandAmt(iJcicZ043Log.getOutstandAmt());
				iJcicZ043.setRepayPerMonDay(iJcicZ043Log.getRepayPerMonDay());
				iJcicZ043.setContractStartYM(iJcicZ043Log.getContractStartYM());
				iJcicZ043.setContractEndYM(iJcicZ043Log.getContractEndYM());
				iJcicZ043.setTranKey(iJcicZ043Log.getTranKey());
				iJcicZ043.setOutJcicTxtDate(iJcicZ043Log.getOutJcicTxtDate());
				try {
					sJcicZ043Service.update(iJcicZ043, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0432, uJcicZ0432);
			iDataLog.exec("L8304刪除", uJcicZ0432.getSubmitKey()+uJcicZ0432.getCustId()+uJcicZ0432.getRcDate()+uJcicZ0432.getMaxMainCode()+uJcicZ0432.getAccount());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
