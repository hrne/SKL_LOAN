package com.st1.itx.trade.L5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/*DB服務*/

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
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
	/* DB服務注入 */
	@Autowired
	public NegReportCom sNegReportNegService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public MakeExcel makeExcel;
	

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5710");
		this.info("active L5710 ");
		this.totaVo.init(titaVo);
		String BringUpDate = titaVo.getParam("BringUpDate").trim();// 提兌日
		// 路徑
		if ("".equals(titaVo.getParam("FILENA").trim())) {
			throw new LogicException(titaVo, "E0015", "檔案不存在,請查驗路徑");
		}
		String FilePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();
		StringBuffer sbData = new StringBuffer();
		try {
			sbData = sNegReportNegService.BatchTx02(titaVo, FilePath, BringUpDate);

			String ChangeLine = "/n";
			
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5710", "一般債權撥付資料檢核", "一般債權撥付資料檢核");
			makeExcel.setValue(1, 1, "區別碼");
			makeExcel.setValue(1, 2, "發件單位");
			makeExcel.setValue(1, 3, "收件單位");
			makeExcel.setValue(1, 4, "指定入扣帳日");
			makeExcel.setValue(1, 5, "轉帳類別");
			makeExcel.setValue(1, 6, "交易序號");
			makeExcel.setValue(1, 7, "交易金額");							
			makeExcel.setValue(1, 8, "轉帳行代碼");
			makeExcel.setValue(1, 9, "轉帳帳號");
			makeExcel.setValue(1, 10, "回應代碼");
			makeExcel.setValue(1, 11, "銀行專用區");
			
			int i =1;
			String Col11="";//回應代碼
			
			if (sbData != null) {
				String Data[] = sbData.toString().split(ChangeLine);
				if (Data != null && Data.length != 0) {
					for (String ThisLine : Data) {
						if ("2".equals(ThisLine.substring(0, 1))) {
							i++;
							makeExcel.setValue(i, 1, ThisLine.substring(0, 1));// 區別碼
							makeExcel.setValue(i, 2, ThisLine.substring(1, 9));// 發件單位
							makeExcel.setValue(i, 3, ThisLine.substring(9, 17));// 收件單位
							makeExcel.setValue(i, 4, ThisLine.substring(17, 24));// 指定入扣帳日
							makeExcel.setValue(i, 5, ThisLine.substring(24, 29));// 轉帳類別
							makeExcel.setValue(i, 6, ThisLine.substring(29, 39));// 交易序號
							makeExcel.setValue(i, 7, Integer.parseInt(ThisLine.substring(39, 52))/100, "#.##00");// 交易金額
							makeExcel.setValue(i, 8, ThisLine.substring(52, 59));// 轉帳行代碼
							makeExcel.setValue(i, 9, ThisLine.substring(59, 75));// 轉帳帳號
							if(("4001").equals(ThisLine.substring(75, 79))) {
								Col11 = "4001:入/扣帳成功";
							} else if(("4808").equals(ThisLine.substring(75, 79))) {
								Col11 = "4808:無此帳戶或問題帳戶";
							} else {
								Col11 = ThisLine.substring(75, 79);
							}
							
							makeExcel.setValue(i, 10, Col11);// 回應代碼
							makeExcel.setValue(i, 11, ThisLine.substring(79, 86));// 銀行專用區-取前7碼
							
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// throw new LogicException(titaVo, "", "發生未預期的錯誤"); //點掉本行:會蓋掉原錯誤訊息
		}
		if (sbData != null) {
			makeExcel.setWidth(2, 15);
			makeExcel.setWidth(3, 15);
			makeExcel.setWidth(4, 15);
			makeExcel.setWidth(5, 15);
			makeExcel.setWidth(6, 15);
			makeExcel.setWidth(7, 30);
			makeExcel.setWidth(8, 15);
			makeExcel.setWidth(9, 30);
			makeExcel.setWidth(10, 40);
			makeExcel.setWidth(11, 15);
			}
		long sno1 = makeExcel.close();
		makeExcel.toExcel(sno1);
		totaVo.put("ExcelSnoM", "" + sno1);
		
		long sno2 = 0L;
		sno2 = sNegReportNegService.CreateTxt(titaVo, sbData, "BACHTX03");

		totaVo.put("TxtSnoF", "" + sno2);

		this.addList(this.totaVo);
		return this.sendList();
	}
}