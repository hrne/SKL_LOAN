package com.st1.itx.trade.L8;

import java.util.ArrayList;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.TbJcicMu01Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8351")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8351 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8351.class);
	/* DB服務注入 */

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public TbJcicMu01Service iTbJcicMu01Service;

	@Autowired
	public L8351File iL8351File;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8351 ");
		this.totaVo.init(titaVo);

		doFile(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doFile(TitaVo titaVo) throws LogicException {

		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iTxtDate = titaVo.getParam("TxtDate");

		// 檔名
		String filename = iSubmitKey + iTxtDate.substring(3) + ".MU1";

		iL8351File.exec(titaVo);
		long fileNo = iL8351File.close();
		iL8351File.toFile(fileNo, filename);

	}
}