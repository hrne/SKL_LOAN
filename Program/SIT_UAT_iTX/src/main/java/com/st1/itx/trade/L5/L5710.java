package com.st1.itx.trade.L5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/*DB服務*/

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegReportCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/* DB容器 */

/*DB服務*/
/**
 * Tita<br>
 * BringUpDate=9,7<br>
 * FilePath=X,20<br>
 */

@Service("L5710")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5710 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5710.class);
	/* DB服務注入 */
	@Autowired
	public NegReportCom sNegReportNegService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5710");
		this.info("active L5710 ");
		this.totaVo.init(titaVo);
		String BringUpDate = titaVo.getParam("BringUpDate").trim();// 提兌日
		// 路徑
		if("".equals(titaVo.getParam("FILENA").trim())) {
			throw new LogicException(titaVo, "E0015", "檔案不存在,請查驗路徑");
		}
		String FilePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();
		StringBuffer sbData = new StringBuffer();
		try {
			sbData = sNegReportNegService.BatchTx02(titaVo, FilePath, BringUpDate);
			
			String ChangeLine = "/n";
			if(sbData != null) {
			  String Data[] = sbData.toString().split(ChangeLine);
			  if (Data != null && Data.length != 0) {
		  	    for (String ThisLine : Data) {
		  		  if("2".equals(ThisLine.substring(0, 1))) {
		  			OccursList occursList = new OccursList();
					occursList.putParam("OOCol1", ThisLine.substring(0, 1));// 區別碼
					occursList.putParam("OOCol2", ThisLine.substring(1, 9));// 發件單位
					occursList.putParam("OOCol3", ThisLine.substring(9, 17));// 收件單位
					occursList.putParam("OOCol4", ThisLine.substring(17, 24));// 指定入扣帳日
					occursList.putParam("OOCol5", ThisLine.substring(24, 29));// 轉帳類別
					occursList.putParam("OOCol6", ThisLine.substring(29, 39));// 交易序號
					occursList.putParam("OOCol7", ThisLine.substring(39, 52));// 交易金額				
					occursList.putParam("OOCol8", ThisLine.substring(52, 59));// 轉帳行代碼
					occursList.putParam("OOCol9", ThisLine.substring(59, 75));// 轉帳帳號
					occursList.putParam("OOCol10", ThisLine.substring(75, 79));// 回應代碼
					occursList.putParam("OOCol11", ThisLine.substring(79, 86));// 銀行專用區-取前7碼

					this.totaVo.addOccursList(occursList);
		  		  }
			    }
			  }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//throw new LogicException(titaVo, "", "發生未預期的錯誤");        //點掉本行:會蓋掉原錯誤訊息
		}
		long sno = 0L;
		sno = sNegReportNegService.CreateTxt(titaVo, sbData, "BACHTX03");

		totaVo.put("TxtSnoF", "" + sno);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}