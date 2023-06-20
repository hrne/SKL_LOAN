package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Service("L4605")
@Scope("prototype")
public class L4605 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4605 ");
		this.totaVo.init(titaVo);

//		吃檔
//		String filePath1 = "D:\\temp\\test\\火險\\Test\\Return\\201912_final.txt";
		String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

//		 編碼參數，設定為UTF-8 || big5
		try {
			fileCom.intputTxt(filePath1, "big5");
		} catch (IOException e) {
			throw new LogicException("E0014", "L4605(" + filePath1 + ") " + e.getMessage());
		}
		// 執行交易
		MySpring.newTask("L4605Batch", this.txBuffer, titaVo);
		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

		this.addList(this.totaVo);
		return this.sendList();
	}
}