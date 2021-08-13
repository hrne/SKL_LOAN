package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdPfSpecParms;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdPfSpecParmsService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L6R38")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R38 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R38.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public CdPfSpecParmsService iCdPfSpecParmsService;
	
	@Autowired
	public CdBcmService iCdBcmSerivce;
	
	@Autowired
	public FacProdService iFacProdService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		this.info("active L6R38 ");
		this.totaVo.init(titaVo);
		
		Slice<CdPfSpecParms> xCdPfSpecParms = null;
		xCdPfSpecParms = iCdPfSpecParmsService.findAll(this.index, this.limit, titaVo);
		
		if (xCdPfSpecParms == null) {
			totaVo.putParam("L6R38Flag", "0");
			for (int i = 1 ; i<=80;i++) {
				totaVo.putParam("L6R38ProdCode"+i," ");
				totaVo.putParam("L6R38ProdName"+i," ");
			}
			for (int a = 1 ; a<=20;a++) {
				totaVo.putParam("L6R38DeptCode"+a," ");
				totaVo.putParam("L6R38DeptName"+a," ");
			}
		}else {
			totaVo.putParam("L6R38Flag", "1");
			Slice<CdPfSpecParms> pCdPfSpecParms = null;
			Slice<CdPfSpecParms> dCdPfSpecParms = null;
			pCdPfSpecParms = iCdPfSpecParmsService.conditionCodeEq("1", this.index, this.limit, titaVo);
			dCdPfSpecParms = iCdPfSpecParmsService.conditionCodeEq("2", this.index, this.limit, titaVo);
			int pCount = 1;
			int dCount = 1;
			if (pCdPfSpecParms == null) {
				while(pCount <= 80) {
					totaVo.putParam("L6R38ProdCode"+pCount," ");
					totaVo.putParam("L6R38ProdName"+pCount," ");
					pCount ++;
				}
			}else {
				for(CdPfSpecParms ppCdPfSpecParms :pCdPfSpecParms) {
					FacProd iFacProd = new FacProd();
					totaVo.putParam("L6R38ProdCode"+pCount,ppCdPfSpecParms.getCondition());
					iFacProd = iFacProdService.findById(ppCdPfSpecParms.getCondition(), titaVo);
					if (iFacProd == null) {
						totaVo.putParam("L6R38ProdName"+pCount," ");
					}else {
						totaVo.putParam("L6R38ProdName"+pCount,iFacProd.getProdName());
					}
					pCount ++;
				}
				while(pCount <= 80) {
					totaVo.putParam("L6R38ProdCode"+pCount," ");
					totaVo.putParam("L6R38ProdName"+pCount," ");
					pCount ++;
				}
			}
			if (dCdPfSpecParms == null) {
				while(dCount<= 20) {
					totaVo.putParam("L6R38DeptCode"+dCount, " ");
					totaVo.putParam("L6R38DeptName"+dCount, " ");
					dCount ++ ;
				}
			}else {
				for(CdPfSpecParms ddCdPfSpecParms :dCdPfSpecParms) {
					CdBcm iCdBcm = new CdBcm();
					totaVo.putParam("L6R38DeptCode"+dCount, ddCdPfSpecParms.getCondition());
					iCdBcm = iCdBcmSerivce.deptCodeFirst(ddCdPfSpecParms.getCondition(), titaVo);
					if(iCdBcm == null) {
						totaVo.putParam("L6R38DeptName"+dCount, " ");
					}else {
						totaVo.putParam("L6R38DeptName"+dCount, iCdBcm.getDeptItem());
					}
					dCount ++ ;
				}
				while(dCount<= 20) {
					totaVo.putParam("L6R38DeptCode"+dCount, " ");
					totaVo.putParam("L6R38DeptName"+dCount, " ");
					dCount ++ ;
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}