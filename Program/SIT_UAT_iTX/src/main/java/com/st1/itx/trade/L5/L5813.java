package com.st1.itx.trade.L5;

import java.util.ArrayList;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L5813")
@Scope("prototype")
/**
 * 國稅局申報媒體檔
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5813 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L5813File l5813File;




	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5813 ");
		this.totaVo.init(titaVo);
		String iYear = titaVo.getParam("Year");
		int tYear = Integer.parseInt(iYear)+1;
		
		String fileName = "每年房屋擔保借款繳息媒體檔"+tYear+"年度";
		l5813File.exec(titaVo);
		long fileNo = l5813File.close();
		l5813File.toFile(fileNo, fileName);
		


		this.addList(this.totaVo);
		return this.sendList();
	}




}