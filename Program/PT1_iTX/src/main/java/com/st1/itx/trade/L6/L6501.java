package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

@Service("L6501")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6501 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public SystemParasService sSystemParasService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6501 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iBusinessType = titaVo.getParam("BusinessType");
		this.info("L6501 iBusinessType : " + iBusinessType);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6501"); // 功能選擇錯誤
		}

		// 交易需主管核可
		if (!(iFuncCode == 5)) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
		}

		// 更新系統參數設定檔
		SystemParas tSystemParas = new SystemParas();
		switch (iFuncCode) {
		case 1: // 新增
			moveSystemParas(tSystemParas, iFuncCode, titaVo);
			try {
				sSystemParasService.insert(tSystemParas, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tSystemParas = sSystemParasService.holdById(iBusinessType);
			if (tSystemParas == null) {
				throw new LogicException(titaVo, "E0003", iBusinessType); // 修改資料不存在
			}
			SystemParas tSystemParas2 = (SystemParas) dataLog.clone(tSystemParas); ////
			try {
				moveSystemParas(tSystemParas, iFuncCode, titaVo);
				tSystemParas = sSystemParasService.update2(tSystemParas, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tSystemParas2, tSystemParas); ////
			dataLog.exec("修改系統變數級參數設定值"); ////
			break;

		case 4: // 刪除
			tSystemParas = sSystemParasService.holdById(iBusinessType);
			if (tSystemParas != null) {
				try {
					sSystemParasService.delete(tSystemParas);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iBusinessType); // 刪除資料不存在
			}
			break;
		case 5: // inq
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveSystemParas(SystemParas mSystemParas, int mFuncCode, TitaVo titaVo) throws LogicException {

		mSystemParas.setBusinessType(titaVo.getParam("BusinessType"));
		mSystemParas.setGraceDays(this.parse.stringToInteger(titaVo.getParam("GraceDays")));
		mSystemParas.setAchAuthOneTime(titaVo.getParam("AchAuthOneTime"));

		mSystemParas.setAchDeductFlag(this.parse.stringToInteger(titaVo.getParam("AchDeductFlag")));
		mSystemParas.setAchDeductDD1(this.parse.stringToInteger(titaVo.getParam("AchDeductDD1")));
		mSystemParas.setAchDeductDD2(this.parse.stringToInteger(titaVo.getParam("AchDeductDD2")));
		mSystemParas.setAchDeductDD3(this.parse.stringToInteger(titaVo.getParam("AchDeductDD3")));
		mSystemParas.setAchDeductDD4(this.parse.stringToInteger(titaVo.getParam("AchDeductDD4")));
		mSystemParas.setAchDeductDD5(this.parse.stringToInteger(titaVo.getParam("AchDeductDD5")));
		mSystemParas.setAchSecondDeductDays(this.parse.stringToInteger(titaVo.getParam("AchSecondDeductDays")));
		mSystemParas.setAchDeductMethod(this.parse.stringToInteger(titaVo.getParam("AchDeductMethod")));

		mSystemParas.setPostDeductFlag(this.parse.stringToInteger(titaVo.getParam("PostDeductFlag")));
		mSystemParas.setPostDeductDD1(this.parse.stringToInteger(titaVo.getParam("PostDeductDD1")));
		mSystemParas.setPostDeductDD2(this.parse.stringToInteger(titaVo.getParam("PostDeductDD2")));
		mSystemParas.setPostDeductDD3(this.parse.stringToInteger(titaVo.getParam("PostDeductDD3")));
		mSystemParas.setPostDeductDD4(this.parse.stringToInteger(titaVo.getParam("PostDeductDD4")));
		mSystemParas.setPostDeductDD5(this.parse.stringToInteger(titaVo.getParam("PostDeductDD5")));
		mSystemParas.setPostSecondDeductDays(this.parse.stringToInteger(titaVo.getParam("PostSecondDeductDays")));
		mSystemParas.setPostDeductMethod(this.parse.stringToInteger(titaVo.getParam("PostDeductMethod")));

		mSystemParas.setLoanDeptCustNo(this.parse.stringToInteger(titaVo.getParam("LoanDeptCustNo")));
		mSystemParas.setNegDeptCustNo(this.parse.stringToInteger(titaVo.getParam("NegDeptCustNo")));
		mSystemParas.setPerfBackRepayAmt(this.parse.stringToBigDecimal(titaVo.getParam("PerfBackRepayAmt")));
		mSystemParas.setPerfBackPeriodS(this.parse.stringToInteger(titaVo.getParam("PerfBackPeriodS")));
		mSystemParas.setPerfBackPeriodE(this.parse.stringToInteger(titaVo.getParam("PerfBackPeriodE")));
		mSystemParas.setEmpNoList(titaVo.getParam("EmpNoList"));
		mSystemParas.setAcctCode310A(this.parse.stringToInteger(titaVo.getParam("AcctCode310A")));
		mSystemParas.setAcctCode310B(this.parse.stringToInteger(titaVo.getParam("AcctCode310B")));
		mSystemParas.setAcctCode320A(this.parse.stringToInteger(titaVo.getParam("AcctCode320A")));
		mSystemParas.setAcctCode320B(this.parse.stringToInteger(titaVo.getParam("AcctCode320B")));
		mSystemParas.setAcctCode330A(this.parse.stringToInteger(titaVo.getParam("AcctCode330A")));
		mSystemParas.setAcctCode330B(this.parse.stringToInteger(titaVo.getParam("AcctCode330B")));
		mSystemParas.setReduceAmtLimit1(this.parse.stringToInteger(titaVo.getParam("ReduceAmtLimit1")));
		mSystemParas.setReduceAmtLimit2(this.parse.stringToInteger(titaVo.getParam("ReduceAmtLimit2")));
		mSystemParas.setReduceAmtLimit3(this.parse.stringToInteger(titaVo.getParam("ReduceAmtLimit3")));
		mSystemParas.setCoreRemitLimit(this.parse.stringToBigDecimal(titaVo.getParam("CoreRemitLimit")));
		mSystemParas.setPreRepayTerms(this.parse.stringToInteger(titaVo.getParam("PreRepayTerms")));
		mSystemParas.setPreRepayTermsBatch(this.parse.stringToInteger(titaVo.getParam("PreRepayTermsBatch")));
		mSystemParas.setShortPrinPercent(this.parse.stringToInteger(titaVo.getParam("ShortPrinPercent")));
		mSystemParas.setShortPrinLimit(this.parse.stringToInteger(titaVo.getParam("ShortPrinLimit")));
		mSystemParas.setShortIntPercent(this.parse.stringToInteger(titaVo.getParam("ShortIntPercent")));
		mSystemParas.setBatchFireFeeFg(titaVo.getParam("BatchFireFeeFg"));
		mSystemParas.setAmlFg(this.parse.stringToInteger(titaVo.getParam("AmlFg")));
		mSystemParas.setAmlUrl(titaVo.getParam("AmlUrl"));

		mSystemParas.setPerfDate(this.parse.stringToInteger(titaVo.getParam("PerfDate")));
		mSystemParas.setAcBookAdjDate(this.parse.stringToInteger(titaVo.getParam("AcBookAdjDate")));

		mSystemParas.setLoanMediaFtpUrl(titaVo.getParam("FtpUrl"));
		mSystemParas.setSmsFtpUrl(titaVo.getParam("SmsFtpUrl"));
		mSystemParas.setEbsUrl(titaVo.getParam("EbsUrl"));
		mSystemParas.setIcsUrl(titaVo.getParam("IcsUrl"));

		if (mFuncCode != 2) {
			mSystemParas.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mSystemParas.setCreateEmpNo(titaVo.getTlrNo());
		}
		mSystemParas
				.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mSystemParas.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
