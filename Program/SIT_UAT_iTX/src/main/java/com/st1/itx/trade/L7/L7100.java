package com.st1.itx.trade.L7;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L7100")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L7100 extends TradeBuffer {

	private final String[] tranCode = { "L2153", "L2411", "L2416", "L2415", "L2101" };

	private final String[][] occursName = { { "L2153StepOccurs" }, { "L2411Occurs" },
			{ "L2416OwnerOccurs", "L2416ReasonOccurs" },
			{ "L2415PublicOccurs", "L2415ParkingOccurs", "L2415ReasonOccurs" },
			{ "L2101StepOccurs", "L2101PremiumOccurs", "L2101AcctFeeOccurs" } };

//	private static final String[][] occursfld = { { "StepMonths", "StepMonthE", "StepRateCode", "StepRateIncr", "BreachbMmB", "BreachbPercent" },
//	{ "OwnerId_", "OwnerName_", "OwnerRelCode_", "OwnerPart_", "OwnerTotal_" },
//	{ "OwnerId_", "OwnerName_", "OwnerRelCode_", "OwnerPart_", "OwnerTotal_", "Reason_", "OtherReason_", "CreateEmpNo_", "CreateDate_" },
//	{ "PublicBdNoA_", "PublicBdNoB_", "Area_", "PublicBdOwnerId_", "PublicBdOwnerName_", "ParkingBdNoA_", "ParkingBdNoB_", "ParkingArea_", "ParkingAmt_", "OwnerId_", "OwnerName_",
//			"OwnerRelCode_", "OwnerPart_", "OwnerTotal_", "Reason_", "OtherReason_", "CreateEmpNo_", "CreateDate_" },
//	{ "StepMonths", "StepMonthE", "StepRateCode", "StepRateIncr", "TimPremium", "PremiumIncr", "TimLoanAmt", "TimAcctFee" },
//	{ "CustTelSeq", "TelTypeCode", "TelArea", "TelNo", "TelExt", "Mobile", "TelChgRsnCode", "RelationCode", "LiaisonName", "Rmk", "Enable", "StopReason", "TelNoUKey" } };

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7100...");
		this.totaVo.init(titaVo);

		Map<String, String> tranCode = new LinkedHashMap<String, String>();
		for (int i = 0; i < this.tranCode.length; i++)
			tranCode.put(this.tranCode[i], i + "");

		int count = 0;
		for (Map<String, Object> m : titaVo.geteBody()) {
			String num = tranCode.get(m.get("TranCode"));
			TitaVo tempVo = (TitaVo) titaVo.clone();
			tempVo.seteBody(null);

			for (Entry<String, Object> fld : m.entrySet())
				if (fld.getValue() instanceof String)
					tempVo.put(fld.getKey(), (String) fld.getValue());

			TradeBuffer x = (TradeBuffer) MySpring.getBean((String) m.get("TranCode"));
			x.setLoggerFg("1",
					"com.st1.itx.trade." + ((String) m.get("TranCode")).substring(0, 2) + "." + m.get("TranCode"));
			x.setTxBuffer(this.txBuffer);
			tempVo.put(ContentName.txcd, (String) m.get("TranCode"));
			tempVo.put(ContentName.txCode, (String) m.get("TranCode"));
			try {
				if (num != null) {
					for (String occursName : this.occursName[Integer.parseInt(num)]) {
						this.info("occursName=" + occursName);
						List<Map<String, Object>> list = (List<Map<String, Object>>) m.get(occursName);
						int i = 1;
						for (Map<String, Object> m2 : list) {
							for (Entry<String, Object> fld : m2.entrySet()) {
								tempVo.put(fld.getKey() + i, fld.getValue() + "");

								for (int n = i + 1; n < 11; n++)
									tempVo.put(fld.getKey() + n, "");
							}
							i++;
						}
					}

				}

				ArrayList<TotaVo> ls = x.run(tempVo);
				this.addAllList(ls);
//					for (TotaVo l : ls)
//						this.totaVo.putAll(l);

				count++;
			} catch (LogicException e) {
				throw new LogicException(e.getErrorMsgId(), "Body coun : " + count + "TranCode : " + m.get("TranCode")
						+ " " + e.getErrorMsgId() + " " + e.getErrorMsg());
			}
		}
		this.totaVo.putParam("statuts", "S");
		this.totaVo.putParam("ErrMsg", "");
		this.addList(this.totaVo);
		return this.sendList();
	}

}