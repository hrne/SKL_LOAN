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

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@SuppressWarnings("unused")
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R51 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();

//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		int iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").equals("") ? 0
				: parse.stringToInteger(titaVo.getParam("TranKey_Tmp").trim());
		int iEffectDate = Integer.valueOf(titaVo.getParam("RimEffectDate").trim()) + 19110000;

		String txcd = titaVo.getTxcd();

		this.info("L6R51 getTxcd= " + titaVo.getTxcd());
		this.info("L6R51 iTranKey_Tmp= " + iTranKey_Tmp);
		this.info("L6R51 iEffectDate= " + iEffectDate);

		// 取得輸入資料
		CdCommId tCdCommId = new CdCommId();
		Slice<CdCode> slCdCode = null;

		if (txcd.equals("L6303")) {

			slCdCode = cdCodeService.getCodeList(2, "GovOfferCode", 0, Integer.MAX_VALUE, titaVo);

			if (slCdCode != null) {

				tCdCommId.setCdType("01");
				tCdCommId.setCdItem("01");
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
		} else if (txcd.equals("L6304")) {

			tCdCommId.setCdType("02");
			tCdCommId.setCdItem("02");
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
		} // if
		this.addList(this.totaVo);
		return this.sendList();
	}

}