package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Service("L4321")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4321 extends TradeBuffer {

	@Autowired
	public MakeReport makeReport;
	@Autowired
	public Parse parse;

	private int iTxKind = 0;
	private int iCustType = 0;
	private int iAdjCode = 0;
	private String fileNm = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4321 ");
		this.totaVo.init(titaVo);
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		this.iAdjCode = parse.stringToInteger(titaVo.get("AdjCode"));

		this.info("L4321Report exec");

//		titaVo titaVo
//		date 日期
//		brno 單位
//		filecode 檔案編號
//		fileitem 檔案說明
//		filename 輸出檔案名稱(不含副檔名,預設為.xlsx)
//		defaultExcel 預設excel底稿檔
//		defaultSheet 預設sheet,可指定 sheet index or sheet name
		String fileNm1 = "";
		if (this.iCustType == 1) {
			fileNm1 = "個金";
		} else {
			fileNm1 = "企金";
		}
		switch (this.iTxKind) {
		case 1:
			fileNm1 += "定期機動利率";
			break;
		case 2:
			fileNm1 += "機動指數利率";
			break;
		case 3:
			fileNm1 += "機動非指數利率";
			break;
		case 4:
			fileNm1 += "員工利率變動";
			break;
		case 5:
			fileNm1 += "按商品別利率";
			break;
		default:
			break;
		}
		this.info("titaVo.getTxcd() = " + titaVo.getTxcd());
		switch (this.iAdjCode) {
		case 1:
			fileNm += fileNm1 + "-批次自動調整";
			break;
		case 2:
			fileNm += fileNm1 + "-按地區別調整";
			break;
		case 3:
			fileNm += fileNm1 + "-人工調整";
			break;
		default:
			break;
		}
		fileNm += makeReport.showTime(titaVo.getCalTm());

		titaVo.putParam("FileNm", fileNm);
		titaVo.putParam("iCode", "L4321");
		titaVo.putParam("iItem", fileNm);

		// 執行交易
		MySpring.newTask("L4321Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}