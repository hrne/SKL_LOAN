package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;


@Service("L7210")
@Scope("prototype")
public class L7210 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7210.class);

	/* DB服務注入 */
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public FacProdService iFacProdService;
	
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7210 ");
		this.totaVo.init(titaVo);

		String iProdNo = titaVo.getParam("ProdNo");
		String iIfrsStepProdCode = titaVo.getParam("IfrsStepProdCode");
		String iIfrsProdCode = titaVo.getParam("IfrsProdCode");
		
		FacProd iFacProd = new FacProd();
		
		iFacProd = iFacProdService.holdById(iProdNo, titaVo);
		
		if (iFacProd == null) {
			throw new LogicException("E0007", "商品主檔查無資料"); // 查無資料
		}
		
		// 變更前
		FacProd beforeFacProd = (FacProd) dataLog.clone(iFacProd);
		iFacProd.setIfrsProdCode(iIfrsProdCode);
		iFacProd.setIfrsStepProdCode(iIfrsStepProdCode);
		// 搬值
		try {
			iFacProd = iFacProdService.update2(iFacProd,titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "商品主檔" + e.getErrorMsg()); // 變更資料時，發生錯誤
		}
		// 紀錄變更前變更後
		dataLog.setEnv(titaVo, beforeFacProd, iFacProd);
		dataLog.exec();
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}