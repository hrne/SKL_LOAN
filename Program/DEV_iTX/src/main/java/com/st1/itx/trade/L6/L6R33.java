package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6R33")
@Scope("prototype")
/**
 * 
 * 
 * @author chih cheng
 * @version 1.0.0
 */
public class L6R33 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R33.class);

	/* DB服務注入 */
	@Autowired
	public CdCodeService iCdCodeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R33 ");
		this.totaVo.init(titaVo);
		
		int iDefType = Integer.parseInt(titaVo.getParam("Rim6R33DefType"));
		String iDefCode = titaVo.getParam("Rim6R33DefCode");
		this.info("iDefType=="+iDefType+"iDefCode=="+iDefCode);
		
		Slice<CdCode> iCdCode = null;
		
		iCdCode = iCdCodeService.getCodeList(iDefType,iDefCode,this.index,this.limit, titaVo);
		
		this.info("iCdCode="+iCdCode);
		
		if (iCdCode==null) {
			totaVo.putParam("L6R33Flag", "0");//沒資料回傳0
			this.info("L6R33Flag0");
		}else {
			totaVo.putParam("L6R33Flag", "1");//有資料回傳1
			this.info("L6R33Flag1");
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}