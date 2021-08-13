package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;


@Service("L5R34")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R34 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R34.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		this.info("active L5R34 ");
		this.totaVo.init(titaVo);
		
		String iFileName = titaVo.getParam("RimFileName");

		this.info("檔名="+iFileName);
	
		
		if(iFileName.equals("")) {
			totaVo.putParam("L5R34Message", "上傳內容為空，請選擇上傳檔案");
			totaVo.putParam("L5R34SuccessFlag", 0);
		}else {
			if(iFileName.contains(".xls")) {
				totaVo.putParam("L5R34Message", "檔案類型檢查成功，可送出交易");
				totaVo.putParam("L5R34SuccessFlag", 1);
			}else {
				totaVo.putParam("L5R34Message", "上傳檔案類型錯誤，請選擇 .xls之Excel檔案");
				totaVo.putParam("L5R34SuccessFlag", 0);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}