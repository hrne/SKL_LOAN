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
		BigDecimal i340LoanBal = BigDecimal.ZERO;
		BigDecimal IALoanBal = BigDecimal.ZERO;
		BigDecimal IBLoanBal = BigDecimal.ZERO;
		BigDecimal ICLoanBal = BigDecimal.ZERO;
		BigDecimal IDLoanBal = BigDecimal.ZERO;
		BigDecimal IELoanBal = BigDecimal.ZERO;
		BigDecimal IFLoanBal = BigDecimal.ZERO;
		BigDecimal IGLoanBal = BigDecimal.ZERO;
		BigDecimal IHLoanBal = BigDecimal.ZERO;
		BigDecimal IILoanBal = BigDecimal.ZERO;
		BigDecimal i921LoanBal = BigDecimal.ZERO;
		BigDecimal i990LoanBal = BigDecimal.ZERO;

		// 全部查詢
		if (sYearMonth == 0 && sYearMonth == 0) {

			Slice<CdComm> iCdComm = sCdCommService.findAll(this.index, Integer.MAX_VALUE, titaVo);

			if (iCdComm == null) {
				throw new LogicException(titaVo, "E0001", "查無資料"); // 查無資料
			}

			List<CdComm> lCdComm = iCdComm.getContent();

			for (CdComm sCdComm : lCdComm) {
				this.info("getEffectDate =" + sCdComm.getEffectDate());

				if (!"02".equals(sCdComm.getCdType())) {
					continue;
				}

				try {
					jsonField = new JSONObject(sCdComm.getJsonFields().toString());

					i340LoanBal = new BigDecimal(jsonField.get("340LoanBal").toString());
					IALoanBal = new BigDecimal(jsonField.get("IALoanBal").toString());
					IBLoanBal = new BigDecimal(jsonField.get("IBLoanBal").toString());
					ICLoanBal = new BigDecimal(jsonField.get("ICLoanBal").toString());
					IDLoanBal = new BigDecimal(jsonField.get("IDLoanBal").toString());
					IELoanBal = new BigDecimal(jsonField.get("IELoanBal").toString());
					IFLoanBal = new BigDecimal(jsonField.get("IFLoanBal").toString());
					IGLoanBal = new BigDecimal(jsonField.get("IGLoanBal").toString());
					IHLoanBal = new BigDecimal(jsonField.get("IHLoanBal").toString());
					IILoanBal = new BigDecimal(jsonField.get("IILoanBal").toString());
					i921LoanBal = new BigDecimal(jsonField.get("921LoanBal").toString());
					i990LoanBal = new BigDecimal(jsonField.get("990LoanBal").toString());

				} catch (JSONException e) {

					e.printStackTrace();
				}

				OccursList occursList = new OccursList();

				occursList.putParam("OOYearMonth", sCdComm.getEffectDate() / 100);
				occursList.putParam("OORemark", sCdComm.getRemark());
				occursList.putParam("OO340LoanBal", df.format(i340LoanBal));
				occursList.putParam("OOIALoanBal", df.format(IALoanBal));
				occursList.putParam("OOIBLoanBal", df.format(IBLoanBal));
				occursList.putParam("OOICLoanBal", df.format(ICLoanBal));
				occursList.putParam("OOIDLoanBal", df.format(IDLoanBal));
				occursList.putParam("OOIELoanBal", df.format(IELoanBal));
				occursList.putParam("OOIFLoanBal", df.format(IFLoanBal));
				occursList.putParam("OOIGLoanBal", df.format(IGLoanBal));
				occursList.putParam("OOIHLoanBal", df.format(IHLoanBal));
				occursList.putParam("OOIILoanBal", df.format(IILoanBal));
				occursList.putParam("OO921LoanBal", df.format(i921LoanBal));
				occursList.putParam("OO990LoanBal", df.format(i990LoanBal));

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

			sCdComm = sCdCommService.CdTypeAscFirst("02", "01", sAcDate, eAcDate, titaVo);

			// 查無資料
			if (sCdComm == null) {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}

			try {
				jsonField = new JSONObject(sCdComm.getJsonFields().toString());

				i340LoanBal = new BigDecimal(jsonField.get("340LoanBal").toString());
				IALoanBal = new BigDecimal(jsonField.get("IALoanBal").toString());
				IBLoanBal = new BigDecimal(jsonField.get("IBLoanBal").toString());
				ICLoanBal = new BigDecimal(jsonField.get("ICLoanBal").toString());
				IDLoanBal = new BigDecimal(jsonField.get("IDLoanBal").toString());
				IELoanBal = new BigDecimal(jsonField.get("IELoanBal").toString());
				IFLoanBal = new BigDecimal(jsonField.get("IFLoanBal").toString());
				IGLoanBal = new BigDecimal(jsonField.get("IGLoanBal").toString());
				IHLoanBal = new BigDecimal(jsonField.get("IHLoanBal").toString());
				IILoanBal = new BigDecimal(jsonField.get("IILoanBal").toString());
				i921LoanBal = new BigDecimal(jsonField.get("921LoanBal").toString());
				i990LoanBal = new BigDecimal(jsonField.get("990LoanBal").toString());

			} catch (JSONException e) {

				e.printStackTrace();
			}

			this.info("getEffectDate =" + sCdComm.getEffectDate());
			OccursList occursList = new OccursList();
			occursList.putParam("OOYearMonth", sCdComm.getEffectDate() / 100);
			occursList.putParam("OORemark", sCdComm.getRemark());
			occursList.putParam("OO340LoanBal", df.format(i340LoanBal));
			occursList.putParam("OOIALoanBal", df.format(IALoanBal));
			occursList.putParam("OOIBLoanBal", df.format(IBLoanBal));
			occursList.putParam("OOICLoanBal", df.format(ICLoanBal));
			occursList.putParam("OOIDLoanBal", df.format(IDLoanBal));
			occursList.putParam("OOIELoanBal", df.format(IELoanBal));
			occursList.putParam("OOIFLoanBal", df.format(IFLoanBal));
			occursList.putParam("OOIGLoanBal", df.format(IGLoanBal));
			occursList.putParam("OOIHLoanBal", df.format(IHLoanBal));
			occursList.putParam("OOIILoanBal", df.format(IILoanBal));
			occursList.putParam("OO921LoanBal", df.format(i921LoanBal));
			occursList.putParam("OO990LoanBal", df.format(i990LoanBal));
			this.totaVo.addOccursList(occursList);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}