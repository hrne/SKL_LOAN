package com.st1.itx.trade.L7;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;


@Service("L7903")
@Scope("prototype")
public class L7903 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L7903.class);

	/* DB服務注入 */
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public FacProdService iFacProdService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7903 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200; // 229 * 200 = 45800
		
		String iProdNo = titaVo.getParam("ProdNo");
		Slice<FacProd> sFacProd = null;
		FacProd pFacProd = new FacProd();
		if (iProdNo.equals("")) {
			sFacProd = iFacProdService.findAll(this.index, this.limit, titaVo);
			if (sFacProd == null) {
				throw new LogicException("E0001", "商品主檔"); // 查無資料
			}else {
				for (FacProd aFacProd:sFacProd) {
					OccursList occursList = new OccursList();
					occursList.putParam("OOProdNo", aFacProd.getProdNo());
					occursList.putParam("OOProdName", aFacProd.getProdName());
					occursList.putParam("OOStartDate", aFacProd.getStartDate());
					occursList.putParam("OOEndDate", aFacProd.getEndDate());
					occursList.putParam("OOIfrsStepProdCode", aFacProd.getIfrsStepProdCode());
					occursList.putParam("OOIfrsProdCode", aFacProd.getIfrsProdCode());
					this.totaVo.addOccursList(occursList);
				}
			}
		}else{
			pFacProd = iFacProdService.findById(iProdNo, titaVo);
			if (pFacProd == null) {
				throw new LogicException("E0001", "商品主檔"); // 查無資料
			}else {
						
				OccursList occursList = new OccursList();
				occursList.putParam("OOProdNo", pFacProd.getProdNo());
				occursList.putParam("OOProdName", pFacProd.getProdName());
				occursList.putParam("OOStartDate", pFacProd.getStartDate());
				occursList.putParam("OOEndDate", pFacProd.getEndDate());
				occursList.putParam("OOIfrsStepProdCode", pFacProd.getIfrsStepProdCode());
				occursList.putParam("OOIfrsProdCode", pFacProd.getIfrsProdCode());
				this.totaVo.addOccursList(occursList);
			}
		
		}
		
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sFacProd != null && sFacProd.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}