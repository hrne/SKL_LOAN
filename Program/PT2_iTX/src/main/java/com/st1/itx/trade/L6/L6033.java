package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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

@Service("L6033") // 指標利率檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Mata
 * @version 1.0.0
 */

public class L6033 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdCommService sCdCommService;

	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6033 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iEffectDate = parse.stringToInteger(titaVo.getParam("EffectDate")) + 19110000;
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 96 * 200 = 19,200
		CdComm sCdComm;
		sCdComm = sCdCommService.CdTypeAscFirst("01", "01", iEffectDate, iEffectDate, titaVo);
		if (iEffectDate != 19110000) {
			if (sCdComm == null) {
				throw new LogicException(titaVo, "E0001", "政府優惠房屋貸款-補貼利率"); // 查無資料
			}
			String iRemark;
			String iEnable;
			
			OccursList occursList = new OccursList();
			iRemark = sCdComm.getRemark();
			iEnable = sCdComm.getEnable();
			
			occursList.putParam("OOEffectDate", sCdComm.getEffectDate());
			occursList.putParam("OORemark", iRemark);
			occursList.putParam("OOEnable", iEnable);
			
			this.totaVo.addOccursList(occursList);
			
		}else {
			this.info("EffectDate  = 19110000");
			
			Slice<CdComm> iCdComm;
			iCdComm = sCdCommService.findAll(this.index, this.limit, titaVo);
			if (iCdComm == null) {
				throw new LogicException(titaVo, "E0001", "政府優惠房屋貸款-補貼利率"); // 查無資料
			}
			List<CdComm> lCdComm = iCdComm == null ? new ArrayList<CdComm>() : iCdComm.getContent();
			this.info("getContent   = " +  iCdComm.getContent());
			for(CdComm siCdComm : lCdComm) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOEffectDate", siCdComm.getEffectDate());
				occursList.putParam("OORemark", siCdComm.getRemark());
				occursList.putParam("OOEnable", siCdComm.getEnable());
				this.totaVo.addOccursList(occursList);
			}
		}




		this.addList(this.totaVo);
		return this.sendList();
	}
}