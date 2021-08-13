package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5410")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5410 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5410.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public PfBsDetailService iPfBsDetailService;
	

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5410 ");
		this.totaVo.init(titaVo);
		
		int iDateFm = Integer.valueOf(titaVo.getParam("DateFm"))+19110000;
		int iDateTo = Integer.valueOf(titaVo.getParam("DateTo"))+19110000;
		
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		Slice<PfBsDetail> iPfBsDetail = iPfBsDetailService.findByPerfDate(iDateFm,iDateTo,this.index,this.limit,titaVo);
		if (iPfBsDetail!=null) {
			for (PfBsDetail aPfBsDetail:iPfBsDetail) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOPerfDate", aPfBsDetail.getPerfDate());
				occursList.putParam("OOCustNo", aPfBsDetail.getCustNo());
				occursList.putParam("OOFacmNo", aPfBsDetail.getFacmNo());
				occursList.putParam("OOBormNo", aPfBsDetail.getBormNo());
				occursList.putParam("OOBsOfficer", aPfBsDetail.getBsOfficer());
				this.totaVo.addOccursList(occursList);
			}
		}else {
			throw new LogicException(titaVo, "E0001", "");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}