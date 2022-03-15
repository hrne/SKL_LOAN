package com.st1.itx.trade.BS;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("BS990")
@Scope("prototype")
/**
 * 提存測試程式<br>
 * 
 * 1.在upload下建立西元營業日資料夾<br>
 * 2.在西元營業日資料夾下建立櫃員編號資料夾<br>
 * 3.在櫃員編號資料夾下建立BS990TestData.txt<br>
 * 4.每行為一筆輸入，格式為CustNo,FacmNo,BormNo
 * 
 * @author xiangwei
 * @version 1.0.0
 */
public class BS990 extends TradeBuffer {

	@Autowired
	FileCom fileCom;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	// itxWrite/Upload/
	@Value("${iTXInFolder}")
	private String inFolder = "";
	private String pathToFile = "";

	private ArrayList<String> getKeys(TitaVo titaVo) {
		// 營業日\櫃員編號\BS990TestFile.txt
		pathToFile = parse.IntegerToString(this.txBuffer.getTxBizDate().getTbsDyf(), 8) + File.separatorChar
				+ titaVo.getTlrNo() + File.separatorChar + "BS990TestData.txt";

		try {
			// 輸入例 (txt):
			// 39,1,1\n
			// 2,2,1\n
			// 1,1,1\n
			// \n

			// 輸出:
			// {"39,1,1","2,2,1","1,1,1","\n"}
			this.info("getKeys getting file:" + pathToFile);
			return fileCom.intputTxt(inFolder + File.separatorChar + pathToFile, "UTF-8");
		} catch (Exception e) {
			this.error("getKeys error:");
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	private void doAcLoanInt(ArrayList<String> keys, TitaVo titaVo) {
		// 輸入例:
		// {"39,1,1","2,2,1","1,1,1","\n"}
		try {
			// baTxCom 需要這個
			baTxCom.setTxBuffer(this.getTxBuffer());

			// 取 parameters 用的日期
			dateUtil.init();
			dateUtil.setDate_1(this.getTxBuffer().getMgBizDate().getTbsDy());
			dateUtil.setMons(1);

			int intDate = (dateUtil.getCalenderDay() / 100) * 100 + 01; // 提存日期 =>下個月一號 copied from BS900
			int iEntryDate = this.getTxBuffer().getMgBizDate().getTmnDy(); // 月底日曆日 copied from BS900

			for (String s : keys) {
				String[] key = s.split(",");

				if (key.length == 3) // 只處理正確格式資料
				{
					this.info("doAcLoanInt: " + key[0] + "," + key[1] + "," + key[2]);

					baTxCom.acLoanInt(iEntryDate, intDate, parse.stringToInteger(key[0]), parse.stringToInteger(key[1]),
							parse.stringToInteger(key[2]), titaVo);
				}
			}
		} catch (Exception e) {
			this.error("BS990.doAcLoanInt error:" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS990 initiated...");
		this.totaVo.init(titaVo);

		// 1. 取測資
		ArrayList<String> keysFromFile = getKeys(titaVo);
		// 2. Loop and do the function call
		doAcLoanInt(keysFromFile, titaVo);

		return this.sendList();
	}
}