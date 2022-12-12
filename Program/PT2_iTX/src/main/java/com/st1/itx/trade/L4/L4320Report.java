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
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4320RServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L4320Report")
@Scope("prototype")

public class L4320Report extends MakeReport {

	@Autowired
	public L4320RServiceImpl l4320RServiceImpl;

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
	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private long sno;
	String fileNm = "";

	public long exec(TitaVo titaVo) throws LogicException {
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		titaVo.putParam("AdjDate", titaVo.getEntDyI());

		this.info("L4320Report exec");

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
		int excelNo = 0;
		this.info("titaVo.getTxcd() = " + titaVo.getTxcd());
		for (int j = 1; j <= 7; j++) {
			switch (j) {
			case 1:
				fileNm = fileNm1 + "-批次自動調整";
				titaVo.putParam("AdjCode", 1);
				titaVo.putParam("RateKeyInCode", 1);
				if (this.iTxKind == 1) {
					excelNo = 1;
				} else {
					excelNo = 3;
				}
				break;
			case 2:
				fileNm = fileNm1 + "-按地區別調整";
				titaVo.putParam("AdjCode", 2);
				titaVo.putParam("RateKeyInCode", 1);
				if (this.iTxKind == 1) {
					excelNo = 2;
				} else {
					excelNo = 4;
				}
				break;
			case 3:
				fileNm = fileNm1 + "-人工調整_按合約(未調整)";
				titaVo.putParam("AdjCode", 3);
				titaVo.putParam("RateKeyInCode", 0);
				if (this.iTxKind == 1) {
					excelNo = 1;
				} else {
					excelNo = 3;
				}
				break;
			case 4:
				fileNm = fileNm1 + "-人工調整_按合約(檢核有誤)";
				titaVo.putParam("AdjCode", 3);
				titaVo.putParam("RateKeyInCode", 9);
				if (this.iTxKind == 1) {
					excelNo = 1;
				} else {
					excelNo = 3;
				}
				break;
			case 5:
				fileNm = fileNm1 + "-人工調整_按地區別(未調整)";
				titaVo.putParam("AdjCode", 4);
				titaVo.putParam("RateKeyInCode", 0);
				if (this.iTxKind == 1) {
					excelNo = 2;
				} else {
					excelNo = 4;
				}
				break;
			case 6:
				fileNm = fileNm1 + "-人工調整_按地區別(檢核有誤)";
				titaVo.putParam("AdjCode", 4);
				titaVo.putParam("RateKeyInCode", 9);
				if (this.iTxKind == 1) {
					excelNo = 2;
				} else {
					excelNo = 4;
				}
				break;
			case 7:
				fileNm = fileNm1 + "-檢核提醒件";
				titaVo.putParam("AdjCode", 9);
				titaVo.putParam("RateKeyInCode", 0);
				if (this.iTxKind == 1) {
					excelNo = 1;
				} else {
					excelNo = 3;
				}
				break;
			default:
				break;
			}
			fileNm += titaVo.getCalTm().substring(0, 4);

			try {
				fnAllList = l4320RServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L4320ServiceImpl.findAll error = " + errors.toString());
			}

			if (fnAllList.size() > 0) {
//				設定工作表名稱

				if (excelNo == 1) {
					makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
							"L4320_LNW171E底稿(10909調息檔)定期機動.xlsx", "正常件"); // 無地區
				} else if (excelNo == 2) {
					makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
							"L4320_LNW171E底稿(10909調息檔)定期機動-地區別調整.xlsx", "正常件");
				} else if (excelNo == 3) {
					makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
							"L4320_LNW171E底稿(10909調息檔)機動.xlsx", "正常件");
				} else {
					makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxcd(), fileNm, fileNm,
							"L4320_LNW171E底稿(10909調息檔)機動-地區別調整.xlsx", "正常件");
				}
				String fdnm = "";
//					從第幾列開始(表頭位置)
				int row = 1;

				for (Map<String, String> tLDVo : fnAllList) {
					this.info("tLDVo-------->" + tLDVo.toString());
					TempVo tempVo = new TempVo();
					tempVo = tempVo.getVo(tLDVo.get("F22"));
					row++;
					int ii = 0;

//						i = 欄數(Columns)
					for (int i = 0; i < tLDVo.size(); i++) {

//							預設每個欄位名稱為F1~Fn
						fdnm = "F" + String.valueOf(i);
//							設定每欄之Format
						switch (i) {

//0		  1		2	3	4	   5	6		7		 8		     9		    10		   11	 12		   13          14	     15	     16		    
//鄉鎮區 地區別	戶號	 額度 撥款  戶名	撥款金額	放款餘額	 目前生效日  	本次生效日	繳息迄日	商品代碼 商品名稱	  利率種類         目前利率   加碼值	 擬調利率 
//北投區 台北市	548040 2 1	        600,000 48,897 	1080319	    1090919	    1090810	 1E 退休滿五年員工 保單分紅利率  1.8600     0.35   1.7500

// 地區別
//  json       json         
//  地區別下限 地區別上限 
//  1.85        2.8      

// 定期機動
// 17       18         19           20           21                
// 撥款日  到期日  首次調整日期 利率調整週期 預定下次利率調整日 
// 1050319 1340919	 1050810	   6          1060210

// 機動
// 17       18                    
// 撥款日  到期日  
// 1050319 1340919	 

// 22
// 檢核訊息
						case 2:
						case 3:
						case 4:
							// 戶號(數字右靠)
							ii++;
							if (tLDVo.get(fdnm).equals("")) {
								makeExcel.setValue(row, ii, 0);
							} else {
								makeExcel.setValue(row, ii, Integer.valueOf(tLDVo.get(fdnm)));
							}
							break;
						case 6:
						case 7:
							// 金額
							ii++;
							makeExcel.setValue(row, ii, tLDVo.get(fdnm), "#,##0");
							break;
						case 14:
						case 15:
						case 16:
							// 利率
							ii++;
							makeExcel.setValue(row, ii, tLDVo.get(fdnm), "#0.####");
							if (i == 16) {
								if (excelNo == 2 || excelNo == 4) {
									ii++;
									makeExcel.setValue(row, ii, tempVo.getParam("CityRateFloor"), "#0.####");
									ii++;
									makeExcel.setValue(row, ii, tempVo.getParam("CityRateCeiling"), "#0.####");
								}
							}
							break;
						case 17: // F17 撥款日期
						case 18: // F18 到期日
							ii++;
							makeExcel.setValue(row, ii, tLDVo.get(fdnm));
							break;
						case 19:  // F19 首次調整日期
						case 20:  // F20 利率調整週期
						case 21:  // F21預定下次利率調整日
							if (this.iTxKind == 1) {
								ii++;
								makeExcel.setValue(row, ii, tLDVo.get(fdnm));
							}
							break;
						case 22:
							String procNote = "";
							if (tempVo.get("CheckMsg") != null) {
								procNote += tempVo.get("CheckMsg");
							}
							if (tempVo.get("WarnMsg") != null) {
								procNote += tempVo.get("WarnMsg");
							}
							ii++;
							makeExcel.setValue(row, ii, procNote);
							break;
						default:
							// 字串左靠
							ii++;
							makeExcel.setValue(row, ii, tLDVo.get(fdnm));
							break;
						}
					}
				}
				long sno = makeExcel.close();
				makeExcel.toExcel(sno);
			}
		}

		return sno;
	}

}
