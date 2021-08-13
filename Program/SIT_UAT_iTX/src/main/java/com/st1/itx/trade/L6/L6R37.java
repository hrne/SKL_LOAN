package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L6R37")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6R37 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R37.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public FacProdService iFacProdService;
	
	@Autowired
	public CdBcmService iCdBcmSerivce;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		this.info("active L6R37 ");
		this.totaVo.init(titaVo);
		
		String iFlag = titaVo.getParam("RimFlag"); //1:商品 2:部室
		String iProdCode = titaVo.getParam("RimProdCode");
		String iDeptCode = titaVo.getParam("RimDeptCode");
		this.info("FFFFlag="+iFlag);
		this.info("Prodddd="+iProdCode);
		this.info("deptdddd="+iDeptCode);
		CdBcm iCdBcm = new CdBcm();
		FacProd iFacProd = new FacProd();
		
		switch(iFlag) {
		case "1":
			iFacProd = iFacProdService.findById(iProdCode, titaVo);
			if (iFacProd == null) {
				throw new LogicException(titaVo, "E0001","");
			}else {
				totaVo.putParam("L6R37ProdName",iFacProd.getProdName());
				totaVo.putParam("L6R37DeptName","");
			}
			break;
		case "2":
			iCdBcm = iCdBcmSerivce.deptCodeFirst(iDeptCode, titaVo);
			this.info("Test==="+iCdBcm);
			if (iCdBcm == null) {
				throw new LogicException(titaVo, "E0001","");
			}else {
				totaVo.putParam("L6R37ProdName","");
				totaVo.putParam("L6R37DeptName",iCdBcm.getDeptItem());
			}
			break;
		}
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}