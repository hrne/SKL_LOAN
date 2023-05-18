package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R51 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;
		this.info("titaVo  = " + titaVo);
		// 取得輸入資料
		CdCommId tCdCommId = new CdCommId();
		int iEffectDate = Integer.valueOf(titaVo.getParam("RimEffectDate").trim()) + 19110000;
		int iTranKey_Tmp = Integer.valueOf(titaVo.getParam("TranKey_Tmp").trim());
		Slice<CdCode> slCdCode = null;
		slCdCode = cdCodeService.getCodeList(2, "GovOfferCode", 0, Integer.MAX_VALUE, titaVo);

		if (slCdCode == null) {

		} else {

			String i1 = "01";

			String iCdType = i1;
			String iCdItem = i1;

			tCdCommId.setCdType(iCdType);
			tCdCommId.setCdItem(iCdItem);
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
				totaVo.putParam("L6r51Enable",tCdComm.getEnable());
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

		this.addList(this.totaVo);
		return this.sendList();
	}

}