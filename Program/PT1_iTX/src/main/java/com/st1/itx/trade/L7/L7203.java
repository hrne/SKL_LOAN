package com.st1.itx.trade.L7;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.Ias39IntMethodFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7203")
@Scope("prototype")
/**
 * 利息法帳面資料上傳作業
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L7203 extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public Ias39IntMethodFileVo Ias39IntMethodFileVo;
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7203 ");
		this.totaVo.init(titaVo);

		int iYearMonth = parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;
		this.info("L7203 YearMonth : " + iYearMonth);
//      吃檔                                            
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

//       編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "UTF-8");
		} catch (IOException e) {
			this.info("L5706(" + filename + ") : " + e.getMessage());
			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + filename;

			throw new LogicException("E0014", ErrorMsg);
		}

//       使用資料容器內定義的方法切資料
		Ias39IntMethodFileVo.setValueFromFile(dataLineList);

		ArrayList<OccursList> uploadFile = Ias39IntMethodFileVo.getOccursList();
		BigDecimal AmortizedAmt = BigDecimal.ZERO;
		if (uploadFile != null && uploadFile.size() != 0) {
			for (OccursList tempOccursList : uploadFile) {
				if (!(iYearMonth == parse.stringToInteger(tempOccursList.get("YearMonth")))) {
					throw new LogicException(titaVo, "E0015", "年月份錯誤 : " + tempOccursList.get("YearMonth"));
				}
				AmortizedAmt = AmortizedAmt.add(parse.stringToBigDecimal(tempOccursList.get("AccumDPAmortized")));
			}

			if (AmortizedAmt.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException(titaVo, "E0015", "本期累計應攤銷折溢價=0 ");
			}
		}
		// 整批處理：利息法帳面資料檔更新、寫入應處理清單 ACCL00-各項提存作業(折溢價攤銷)
		MySpring.newTask("BS720", this.txBuffer, titaVo);

		this.totaVo.putParam("Count", uploadFile.size());
		this.totaVo.putParam("Amount", AmortizedAmt.setScale(0, RoundingMode.HALF_UP));

		this.addList(this.totaVo);
		return this.sendList();

	}
}