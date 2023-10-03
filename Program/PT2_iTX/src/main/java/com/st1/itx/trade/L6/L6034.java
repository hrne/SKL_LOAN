package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6034") // 指標利率檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Mata
 * @version 1.0.0
 */

public class L6034 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdCommService sCdCommService;

	@Autowired
	private Parse parse;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6034 ");

		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();
		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 96 * 200 = 19,200

		int sYearMonth = parse.stringToInteger(titaVo.getParam("sYearMonth"));

		int sAcDate = 0;
		int eAcDate = 99999999;

		JSONObject jsonField;
		BigDecimal oLoanBal = BigDecimal.ZERO;
		BigDecimal nLoanBal = BigDecimal.ZERO;
		BigDecimal dLoanBal = BigDecimal.ZERO;
		String remark = "放款合計(催收款項為催收本金)";

		// 全部查詢
		if (sYearMonth == 0 && sYearMonth == 0) {

			Slice<CdComm> iCdComm = sCdCommService.findAll(this.index, Integer.MAX_VALUE, titaVo);

			if (iCdComm == null) {
				throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
			}

			List<CdComm> lCdComm = iCdComm.getContent();
			
			if(lCdComm.size() == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
			}
			
			for (CdComm sCdComm : lCdComm) {
				this.info("getEffectDate =" + sCdComm.getEffectDate());

				if (!"02".equals(sCdComm.getCdType())) {
					continue;
				}

				try {
					jsonField = new JSONObject(sCdComm.getJsonFields().toString());
					oLoanBal = new BigDecimal(jsonField.get("oLoanBal").toString());
					nLoanBal = new BigDecimal(jsonField.get("LoanBal").toString());
					dLoanBal = oLoanBal.subtract(nLoanBal);

				} catch (JSONException e) {

					e.printStackTrace();
				}

				OccursList occursList = new OccursList();

				occursList.putParam("OOYearMonth", sCdComm.getEffectDate() / 100);
				occursList.putParam("OORemark", remark);
				occursList.putParam("OOOLoanBal", df.format(oLoanBal));
				occursList.putParam("OONLoanBal", df.format(nLoanBal));
				occursList.putParam("OODLoanBal", df.format(dLoanBal));

				this.totaVo.addOccursList(occursList);
			}

		}

		/**
		 * 為什麼起迄sAcDate和eAcDate 日期都用1號： 因為此交易在CdComm在新增資料時只有年月，因欄位格式位數，所以日期都放1號，日期無作用。
		 */
		// 用年月查詢
		if (sYearMonth / 100 > 0 && sYearMonth % 100 > 0) {
			sYearMonth = sYearMonth + 191100;

			sAcDate = (sYearMonth / 100 * 10000) + (sYearMonth % 100 * 100) + 1;
			eAcDate = (sYearMonth / 100 * 10000) + (sYearMonth % 100 * 100) + 1;

			CdComm sCdComm = new CdComm();

			this.info("sAcDate =" + sAcDate);
			this.info("eAcDate =" + eAcDate);

			sCdComm = sCdCommService.CdTypeAscFirst("02", "02", sAcDate, eAcDate, titaVo);

			// 查無資料
			if (sCdComm == null) {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}

			try {
				jsonField = new JSONObject(sCdComm.getJsonFields().toString());
				oLoanBal = new BigDecimal(jsonField.get("oLoanBal").toString());
				nLoanBal = new BigDecimal(jsonField.get("LoanBal").toString());
				dLoanBal = oLoanBal.subtract(nLoanBal);
			} catch (JSONException e) {

				e.printStackTrace();
			}

			this.info("getEffectDate =" + sCdComm.getEffectDate());
			OccursList occursList = new OccursList();
			occursList.putParam("OOYearMonth", sCdComm.getEffectDate() / 100);
			occursList.putParam("OORemark", remark);
			occursList.putParam("OOOLoanBal", df.format(oLoanBal));
			occursList.putParam("OONLoanBal", df.format(nLoanBal));
			occursList.putParam("OODLoanBal", df.format(dLoanBal));
			this.totaVo.addOccursList(occursList);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}