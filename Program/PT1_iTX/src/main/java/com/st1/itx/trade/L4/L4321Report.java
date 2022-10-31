package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4321ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L4321Report")
@Scope("prototype")

public class L4321Report extends MakeReport {

	@Autowired
	public L4321ServiceImpl L4321ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Override
	public void printTitle() {

	}

	private int iTxKind = 0;
	private int iCustType = 0;
	private int iAdjCode = 0;
	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private long sno;
	private String fileNm = "";

	public long exec(TitaVo titaVo) throws LogicException {
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		this.iAdjCode = parse.stringToInteger(titaVo.get("AdjCode"));

		this.fileNm = titaVo.getParam("FileNm");
		this.info("L4321Report exec");

//		titaVo titaVo
//		date 日期
//		brno 單位
//		filecode 檔案編號
//		fileitem 檔案說明
//		filename 輸出檔案名稱(不含副檔名,預設為.xlsx)
//		defaultExcel 預設excel底稿檔
//		defaultSheet 預設sheet,可指定 sheet index or sheet name
		this.info("titaVo.getTxcd() = " + titaVo.getTxcd());
		switch (this.iAdjCode) {
		case 1:
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
					"L4321_LNW171E底稿(10909調息檔)機動.xlsx", "正常件");
			break;
		case 2:
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
					"L4321_LNW171E底稿(10909調息檔)機動-地區別調整.xlsx", "正常件");
			break;
		case 3:
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
					"L4321_LNW171E底稿(10909調息檔)機動.xlsx", "正常件");
			break;
		default:
			break;
		}

		try {
			fnAllList = L4321ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4321ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			String fdnm = "";
//				從第幾列開始(表頭位置)
			int row = 1;

			for (Map<String, String> tLDVo : fnAllList) {
				this.info("tLDVo-------->" + tLDVo.toString());
				row++;

//					i = 欄數(Columns)
				for (int i = 0; i < tLDVo.size(); i++) {

//						預設每個欄位名稱為F1~Fn
					fdnm = "F" + String.valueOf(i);

//						設定每欄之Format
					switch (i) {

//		0		1		2	3	4	     5  	6		7		8		  9		     10		   11	  12	  13	       14	      15
//		 鄉鎮區	地區別	戶號	 額度 撥款	戶名	撥款金額	放款餘額	 目前生效日  	本次生效日	繳息迄日	商品代碼  商品名稱 利率種類	  目前利率 	調整後利率
//		 北投區	台北市	548040	2	1	     600,000 48,897  1080319	1090919  	1090810  1E	退休滿五年員工 保單分紅利率 1.8600	     1.75	
					case 2:
					case 3:
					case 4:
						// 戶號(數字右靠)
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					case 6:
					case 7:
						// 金額
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "#,##0");
						break;
					case 14:
					case 15:
						// 利率
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "#0.####");
						break;
					default:
						// 字串左靠
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					}
				}
			}
		}

		sno = makeExcel.close();
		makeExcel.toExcel(sno);

		return sno;
	}

}
