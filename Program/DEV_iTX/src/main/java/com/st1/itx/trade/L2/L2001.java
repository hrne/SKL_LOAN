package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

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

/*
 * Tita
 * ProdNo=X,5
 */
/**
 * L2001 商品參數明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2001")
@Scope("prototype")
public class L2001 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	Parse parse;

	private List<String> lStatusCode = new ArrayList<String>();
	private List<String> lEnterpriseFg = new ArrayList<String>();

	private boolean inflag = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2001 ");
		this.totaVo.init(titaVo);

		String iProdNo = titaVo.getParam("ProdNo");
		String iStatusCode = titaVo.getParam("ProdStatus");
		String iEnterpriseFg = titaVo.getParam("EnterpriseFg");
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 80 * 500 = 400000
		Slice<FacProd> slFacProd = null;
		// 0 全部 1生效 2未生效 3已截止 4停用
		if ("0".equals(iStatusCode)) {
			lStatusCode.add("0");
			lStatusCode.add("1");
		} else if ("1".equals(iStatusCode)) {
			lStatusCode.add("0");
		} else if ("2".equals(iStatusCode)) {
			lStatusCode.add("0");
		} else if ("3".equals(iStatusCode)) {
			lStatusCode.add("0");
		} else {
			lStatusCode.add("1");
		}
		// 空白為全部 Y企金可使用
		if ("Y".equals(iEnterpriseFg)) {
			lEnterpriseFg.add("Y");
			slFacProd = facProdService.fildProdNo(iProdNo.trim() + "%", lStatusCode, lEnterpriseFg, this.index,
					this.limit, titaVo);
		} else {
			slFacProd = facProdService.fildStatus(iProdNo.trim() + "%", lStatusCode, this.index, this.limit, titaVo);
		}

		// 查詢商品參數檔
		List<FacProd> lFacProd = slFacProd == null ? null : slFacProd.getContent();
		if (lFacProd == null || lFacProd.size() == 0) {
			throw new LogicException(titaVo, "E2003", "商品參數檔"); // 查無資料
		}
		// 如有有找到資料
		for (FacProd tFacProd : lFacProd) {
			// 當商品狀態輸入1生效時,生效日>日曆日(未生效) 或 截止日>0時,截止日<日曆日的跳過(
			if ("1".equals(iStatusCode)
					&& (parse.stringToInteger(titaVo.getCalDy()) < tFacProd.getStartDate() || (tFacProd.getEndDate() > 0
							&& tFacProd.getEndDate() <= parse.stringToInteger(titaVo.getCalDy())))) {
				this.info("非生效");
				continue;
			}
			if ("2".equals(iStatusCode) && (parse.stringToInteger(titaVo.getCalDy()) >= tFacProd.getStartDate()
					|| (tFacProd.getEndDate() > 0
							&& tFacProd.getEndDate() <= parse.stringToInteger(titaVo.getCalDy())))) {
				this.info("非未生效");
				continue;
			}
			if ("3".equals(iStatusCode) && !((tFacProd.getEndDate() > 0)
					&& (tFacProd.getEndDate() < parse.stringToInteger(titaVo.getCalDy())))) {
				this.info("非已截止");
				continue;
			}
			this.inflag = true;

			OccursList occursList = new OccursList();
			occursList.putParam("OOProdNo", tFacProd.getProdNo());
			occursList.putParam("OOStartDate", tFacProd.getStartDate());
			occursList.putParam("OOEndDate", tFacProd.getEndDate());
			occursList.putParam("OOStatusCode", tFacProd.getStatusCode());
			occursList.putParam("OOProdName", tFacProd.getProdName());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slFacProd != null && slFacProd.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		if (!this.inflag) {
			throw new LogicException(titaVo, "E2003", "商品參數檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}