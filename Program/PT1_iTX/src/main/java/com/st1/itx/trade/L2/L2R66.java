package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.tradeService.TradeBuffer;

/* Tita
 * RimEmployeeNo=X,6
 */
/**
 * L2R66 尋找分公司資料
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R66")
@Scope("prototype")
public class L2R66 extends TradeBuffer {

	/* DB服務注入 */
	
	@Autowired
	public CdBcmService sCdBcmService;

	
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R66 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		
		String iUnitCode = titaVo.getParam("RimUnitCode").trim();
		String iErrorSkip = titaVo.get("RimErrorSkip1");

		if (iErrorSkip == null)
			iErrorSkip = "N";
		// 檢查輸入資料
		
		if (iUnitCode.isEmpty()) {
			throw new LogicException(titaVo, "E2012", "請輸入單位代號"); // 查詢資料不可為空白
		}

		// 查詢員工資料檔
		
		CdBcm tCdBcm = sCdBcmService.findById(iUnitCode, titaVo);
		
		//查詢分公司資料檔
		if (tCdBcm == null) {
			// X add by eric for L5503
			if (iErrorSkip.equals("Y") || iErrorSkip.equals("Z")) {
				this.totaVo.putParam("L2r66UnitItem", "");
				
			} else {
				throw new LogicException(titaVo, "E0001", " 分公司資料檔  單位代號=" + iUnitCode); // 查無資料
			}
		} else {
			if (!tCdBcm.getEnable().equals("Y")) {
				if ("X".equals(iErrorSkip)) {
				} else if (!iErrorSkip.equals("Y")) {
					throw new LogicException(titaVo, "E2081", " 單位代號=" + iUnitCode); // 該單位非啟用
				}
			}
			this.totaVo.putParam("L2r66UnitItem", tCdBcm.getUnitItem());
			
		}

		
		this.addList(this.totaVo);
		return this.sendList();
	}
}