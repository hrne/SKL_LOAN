package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.CdCommId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimOverdueSign=X,1
 * RimOverdueCode=X,4
 */
@Service("L6R51") // 尋找逾期新增減少原因檔資料
@Scope("prototype")
/**
 *
 *
 * @author Mata
 * @version 1.0.0
 */
public class L6R51 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdCommService cdCommService;

	/* DB服務注入 */
	@Autowired
	private CdCodeService cdCodeService;

	@Autowired
	Parse parse;

	private int iTranKey_Tmp = 0;
	private int iEffectDate = 0;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R51 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();

//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").equals("") ? 0
				: parse.stringToInteger(titaVo.getParam("TranKey_Tmp").trim());
		iEffectDate = Integer.valueOf(titaVo.getParam("RimEffectDate").trim()) + 19110000;

		String txcd = titaVo.getTxcd();

		this.info("L6R51 getTxcd= " + txcd);
		this.info("L6R51 iTranKey_Tmp= " + iTranKey_Tmp);
		this.info("L6R51 iEffectDate= " + iEffectDate);

		String cdCommCdType = "";
		String cdCommCdItem = "";

		if ("L6303".equals(txcd)) {
			cdCommCdType = "01";
			cdCommCdItem = "01";
			dataFromL6303(cdCommCdType, cdCommCdItem, titaVo);
		}

		if ("L6304".equals(txcd)) {
			cdCommCdType = "02";
			cdCommCdItem = "02";

			dataFromL6403(cdCommCdType, cdCommCdItem, titaVo);
		}

		if ("L6911".equals(txcd)) {
			cdCommCdType = "03";
			cdCommCdItem = "01";
			dataFromL6911(cdCommCdType, cdCommCdItem, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void dataFromL6303(String cdType, String cdItem, TitaVo titaVo) throws LogicException {
		// 取得輸入資料
		CdCommId tCdCommId = new CdCommId();
		Slice<CdCode> slCdCode = null;

		slCdCode = cdCodeService.getCodeList(2, "GovOfferCode", 0, Integer.MAX_VALUE, titaVo);

		if (slCdCode != null) {

			tCdCommId.setCdType(cdType);
			tCdCommId.setCdItem(cdItem);
			tCdCommId.setEffectDate(iEffectDate);

			CdComm tCdComm = cdCommService.findById(tCdCommId, titaVo);

			TempVo tTempVo = new TempVo();

			if (tCdComm != null) {

				if (iTranKey_Tmp == 1) {
					throw new LogicException(titaVo, "E0001", "新增日期已存在");

				}
				tTempVo = tTempVo.getVo(tCdComm.getJsonFields());

				totaVo.putParam("L6r51EffectDate", tCdComm.getEffectDate());
				totaVo.putParam("L6r51Remark", tCdComm.getRemark());
				totaVo.putParam("L6r51Enable", tCdComm.getEnable());
			}

			for (CdCode tCdCode : slCdCode.getContent()) {
				if ("Y".equals(tCdCode.getCode()) || "N".equals(tCdCode.getCode())) {
					continue;
				}

				BigDecimal rate = BigDecimal.ZERO;
				rate = parse.stringToBigDecimal(tTempVo.getParam("SubsidyRate" + tCdCode.getCode()));
				this.info("rate   = " + rate);
				OccursList occursList = new OccursList();
				occursList.putParam("L6r51CodeTypeNo", tCdCode.getCode());
				occursList.putParam("L6r51CdItemNo", tCdCode.getItem());
				occursList.putParam("L6r51JsonFieldsNo", rate);

				this.totaVo.addOccursList(occursList);
			}
		}
	}

	@SuppressWarnings("unused")
	private void dataFromL6403(String cdType, String cdItem, TitaVo titaVo) throws LogicException {
		// 取得輸入資料
		CdCommId tCdCommId = new CdCommId();
		Slice<CdCode> slCdCode = null;

		tCdCommId.setCdType(cdType);
		tCdCommId.setCdItem(cdItem);
		tCdCommId.setEffectDate(iEffectDate);

		CdComm tCdComm = cdCommService.findById(tCdCommId, titaVo);

		// 固定參數
		slCdCode = cdCodeService.getCodeList(3, "GovOfferCode", 0, Integer.MAX_VALUE, titaVo);

		if (slCdCode != null) {

			String prodName = "";
			String colName = "";
			String remark = "";
			int cnt = 0;

			JSONObject jsonField = null;
			try {
				jsonField = new JSONObject(tCdComm.getJsonFields().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (iTranKey_Tmp == 1) {

				if (tCdComm != null) {
					throw new LogicException(titaVo, "E0002", "資料年月已存在");
				}

				for (CdCode tCdCode : slCdCode.getContent()) {

					// 八八風災不列入
					if ("88LoanBal".equals(tCdCode.getCode())) {
						continue;
					}

					cnt++;

					BigDecimal oLoanBal = BigDecimal.ZERO;
					BigDecimal loanBal = BigDecimal.ZERO;
					prodName = tCdCode.getItem();
					colName = tCdCode.getCode();

					if ("340".equals(prodName.substring(0, 3))) {
						prodName = prodName.substring(4);
					}
					this.totaVo.putParam("L6r51ColName" + cnt, colName);
					this.totaVo.putParam("L6r51ProdName" + cnt, prodName);
					this.totaVo.putParam("oL6r51LoanBal" + cnt, df.format(oLoanBal));
					this.totaVo.putParam("L6r51LoanBal" + cnt, df.format(loanBal));
					try {

						String prodNo = titaVo.getParam("L6r51ColName" + cnt).replace("LoanBal", "").trim();
						remark = jsonField.get(prodNo + "Remark").toString();
						this.info("remark=" + remark);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					this.totaVo.putParam("L6r51Remark" + cnt, remark);

				}
			}
			if (iTranKey_Tmp != 1) {

				TempVo tTempVo = new TempVo();

				tTempVo = tTempVo.getVo(tCdComm.getJsonFields());

				for (CdCode tCdCode : slCdCode.getContent()) {

					// 八八風災不列入
					if ("88LoanBal".equals(tCdCode.getCode())) {
						continue;
					}

					cnt++;
					BigDecimal oLoanBal = BigDecimal.ZERO;
					BigDecimal loanBal = BigDecimal.ZERO;
					loanBal = parse.stringToBigDecimal(tTempVo.getParam(tCdCode.getCode()));
					oLoanBal = parse.stringToBigDecimal(tTempVo.getParam("o" + tCdCode.getCode()));
					prodName = tCdCode.getItem();
					colName = tCdCode.getCode();
					if ("340".equals(prodName.substring(0, 3))) {
						prodName = prodName.substring(4);
					}
					this.totaVo.putParam("L6r51ColName" + cnt, colName);
					this.totaVo.putParam("L6r51ProdName" + cnt, prodName);
					this.totaVo.putParam("oL6r51LoanBal" + cnt, df.format(oLoanBal));
					this.totaVo.putParam("L6r51LoanBal" + cnt, df.format(loanBal));
					try {
						remark = jsonField.get("Remark" + cnt).toString();
						this.info("remark=" + remark);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					this.totaVo.putParam("L6r51Remark" + cnt, remark);
				}
			}

		} // if

	}

	@SuppressWarnings("unused")
	private void dataFromL6911(String cdType, String cdItem, TitaVo titaVo) throws LogicException {
		// 取得輸入資料
		CdCommId tCdCommId = new CdCommId();
		Slice<CdCode> slCdCode = null;

		tCdCommId.setCdType(cdType);
		tCdCommId.setCdItem(cdItem);
		tCdCommId.setEffectDate(iEffectDate);

		CdComm tCdComm = cdCommService.findById(tCdCommId, titaVo);

		// 固定參數

		String prodName = "";
		String colName = "";
		String remark = "";
		int cnt = 0;
		if (iTranKey_Tmp == 1) {
			this.totaVo.putParam("ooLimitRate11", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan11", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate12", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan12", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate13", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan13", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate21", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan21", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate22", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan22", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate23", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan23", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate31", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan31", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate32", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan32", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate33", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan33", BigDecimal.ZERO);

			this.totaVo.putParam("ooLimitRate43", BigDecimal.ZERO);
			this.totaVo.putParam("ooLimitLoan43", BigDecimal.ZERO);
		} else {

			JSONObject jsv = null;

			try {
				jsv = new JSONObject(tCdComm.getJsonFields().toString());
//			this.info("jsv = " + jsv.toString());
				this.totaVo.putParam("ooLimitRate11", jsv.get("LimitRate11").toString());
				this.totaVo.putParam("ooLimitLoan11", jsv.get("LimitLoan11").toString());

				this.totaVo.putParam("ooLimitRate12", jsv.get("LimitRate12").toString());
				this.totaVo.putParam("ooLimitLoan12", jsv.get("LimitLoan12").toString());

				this.totaVo.putParam("ooLimitRate13", jsv.get("LimitRate13").toString());
				this.totaVo.putParam("ooLimitLoan13", jsv.get("LimitLoan13").toString());

				this.totaVo.putParam("ooLimitRate21", jsv.get("LimitRate21").toString());
				this.totaVo.putParam("ooLimitLoan21", jsv.get("LimitLoan21").toString());

				this.totaVo.putParam("ooLimitRate22", jsv.get("LimitRate22").toString());
				this.totaVo.putParam("ooLimitLoan22", jsv.get("LimitLoan22").toString());

				this.totaVo.putParam("ooLimitRate23", jsv.get("LimitRate23").toString());
				this.totaVo.putParam("ooLimitLoan23", jsv.get("LimitLoan23").toString());

				this.totaVo.putParam("ooLimitRate31", jsv.get("LimitRate31").toString());
				this.totaVo.putParam("ooLimitLoan31", jsv.get("LimitLoan31").toString());

				this.totaVo.putParam("ooLimitRate32", jsv.get("LimitRate32").toString());
				this.totaVo.putParam("ooLimitLoan32", jsv.get("LimitLoan32").toString());

				this.totaVo.putParam("ooLimitRate33", jsv.get("LimitRate33").toString());
				this.totaVo.putParam("ooLimitLoan33", jsv.get("LimitLoan33").toString());

				this.totaVo.putParam("ooLimitRate43", jsv.get("LimitRate43").toString());
				this.totaVo.putParam("ooLimitLoan43", jsv.get("LimitLoan43").toString());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.info(e.getMessage());
			}
		}
	}
}