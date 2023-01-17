package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimBusinessType=X,2
 */
@Service("L6R28") // 尋找系統參數設定檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R28 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public SystemParasService sSystemParasService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R28 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimBusinessType = titaVo.getParam("RimBusinessType");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R28"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R28"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaSystemParas(new SystemParas());

		// 查詢系統參數設定檔
		SystemParas tSystemParas = sSystemParasService.findById(iRimBusinessType, titaVo);

		/* 如有找到資料 */
		if (tSystemParas != null) {
			if (iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimBusinessType")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaSystemParas(tSystemParas);
			}
		} else {
			if (iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "系統參數設定檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 系統參數設定檔
	private void moveTotaSystemParas(SystemParas mSystemParas) throws LogicException {

		this.totaVo.putParam("L6R28GraceDays", mSystemParas.getGraceDays());
		this.totaVo.putParam("L6R28AchAuthOneTime", mSystemParas.getAchAuthOneTime());
		this.totaVo.putParam("L6R28AchDeductFlag", mSystemParas.getAchDeductFlag());
		this.totaVo.putParam("L6R28AchDeductDD1", mSystemParas.getAchDeductDD1());
		this.totaVo.putParam("L6R28AchDeductDD2", mSystemParas.getAchDeductDD2());
		this.totaVo.putParam("L6R28AchDeductDD3", mSystemParas.getAchDeductDD3());
		this.totaVo.putParam("L6R28AchDeductDD4", mSystemParas.getAchDeductDD4());
		this.totaVo.putParam("L6R28AchDeductDD5", mSystemParas.getAchDeductDD5());
		this.totaVo.putParam("L6R28AchSecondDeductDays", mSystemParas.getAchSecondDeductDays());
		this.totaVo.putParam("L6R28AchDeductMethod", mSystemParas.getAchDeductMethod());
		this.totaVo.putParam("L6R28PostDeductFlag", mSystemParas.getPostDeductFlag());
		this.totaVo.putParam("L6R28PostDeductDD1", mSystemParas.getPostDeductDD1());
		this.totaVo.putParam("L6R28PostDeductDD2", mSystemParas.getPostDeductDD2());
		this.totaVo.putParam("L6R28PostDeductDD3", mSystemParas.getPostDeductDD3());
		this.totaVo.putParam("L6R28PostDeductDD4", mSystemParas.getPostDeductDD4());
		this.totaVo.putParam("L6R28PostDeductDD5", mSystemParas.getPostDeductDD5());
		this.totaVo.putParam("L6R28PostSecondDeductDays", mSystemParas.getPostSecondDeductDays());
		this.totaVo.putParam("L6R28PostDeductMethod", mSystemParas.getPostDeductMethod());
		this.totaVo.putParam("L6R28LoanDeptCustNo", mSystemParas.getLoanDeptCustNo());
		this.totaVo.putParam("L6R28NegDeptCustNo", mSystemParas.getNegDeptCustNo());
		this.totaVo.putParam("L6R28PerfBackRepayAmt", mSystemParas.getPerfBackRepayAmt());
		this.totaVo.putParam("L6R28PerfBackPeriodS", mSystemParas.getPerfBackPeriodS());
		this.totaVo.putParam("L6R28PerfBackPeriodE", mSystemParas.getPerfBackPeriodE());
		this.totaVo.putParam("L6R28EmpNoList", mSystemParas.getEmpNoList());
		this.totaVo.putParam("L6R28AcctCode310A", mSystemParas.getAcctCode310A());
		this.totaVo.putParam("L6R28AcctCode310B", mSystemParas.getAcctCode310B());
		this.totaVo.putParam("L6R28AcctCode320A", mSystemParas.getAcctCode320A());
		this.totaVo.putParam("L6R28AcctCode320B", mSystemParas.getAcctCode320B());
		this.totaVo.putParam("L6R28AcctCode330A", mSystemParas.getAcctCode330A());
		this.totaVo.putParam("L6R28AcctCode330B", mSystemParas.getAcctCode330B());
		this.totaVo.putParam("L6R28ReduceAmtLimit", mSystemParas.getReduceAmtLimit());
		this.totaVo.putParam("L6R28PreRepayTerms", mSystemParas.getPreRepayTerms());
		this.totaVo.putParam("L6R28PreRepayTermsBatch", mSystemParas.getPreRepayTermsBatch());
		this.totaVo.putParam("L6R28ShortPrinPercent", mSystemParas.getShortPrinPercent());
		this.totaVo.putParam("L6R28ShortPrinLimit", mSystemParas.getShortPrinLimit());
		this.totaVo.putParam("L6R28ShortIntPercent", mSystemParas.getShortIntPercent());
		this.totaVo.putParam("L6R28AmlFg", mSystemParas.getAmlFg());
		this.totaVo.putParam("L6R28AmlUrl", mSystemParas.getAmlUrl());
		this.totaVo.putParam("L6R28FtpUrl", mSystemParas.getLoanMediaFtpUrl());
		this.totaVo.putParam("L6R28SmsFtpUrl", mSystemParas.getSmsFtpUrl());
		this.totaVo.putParam("L6R28PerfDate", mSystemParas.getPerfDate());
		this.totaVo.putParam("L6R28AcBookAdjDate", mSystemParas.getAcBookAdjDate());
		this.totaVo.putParam("L6R28JcicEmpName", mSystemParas.getJcicEmpName());
		this.totaVo.putParam("L6R28JcicEmpTel", mSystemParas.getJcicEmpTel());
		this.totaVo.putParam("L6R28EbsUrl", mSystemParas.getEbsUrl());
		this.totaVo.putParam("L6R28IcsUrl", mSystemParas.getIcsUrl());
		this.totaVo.putParam("L6R28BatchFireFeeFg", mSystemParas.getBatchFireFeeFg());
		this.totaVo.putParam("L6R28InsuSettleDate", mSystemParas.getInsuSettleDate());
		this.totaVo.putParam("L6R28JcicZDep", mSystemParas.getJcicZDep());
		this.totaVo.putParam("L6R28JcicZName", mSystemParas.getJcicZName());
		this.totaVo.putParam("L6R28JcicZTel", mSystemParas.getJcicZTel());
		this.totaVo.putParam("L6R28JcicMU1Dep", mSystemParas.getJcicMU1Dep());
		this.totaVo.putParam("L6R28JcicMU1Name", mSystemParas.getJcicMU1Name());
		this.totaVo.putParam("L6R28JcicMU1Tel", mSystemParas.getJcicMU1Tel());

	}

}