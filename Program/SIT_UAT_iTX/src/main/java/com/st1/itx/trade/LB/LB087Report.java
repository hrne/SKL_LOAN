package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB087ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB087Report")
@Scope("prototype")

public class LB087Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LB087Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	public LB087ServiceImpl lB087ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// LB087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
//			List<HashMap<String, String>> LBList = lB087ServiceImpl.findAll(titaVo);
			List<HashMap<String, String>> LBList = null;
			if (LBList == null) {
				listCount = 0;
			} else {
				listCount = LBList.size();
			}
			this.info("--------LBList.size()=" + listCount);

			// txt
			genFile(titaVo, LBList);
			// excel
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB087ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB087 genFile : ");
		String txt = "";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".087";		// 458+月日+序號(1).087
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B087", "聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B087-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
					   + makeFile.fillStringR("02-23895858#7067", 16, ' ') + makeFile.fillStringR("審查單位聯絡人－許高政", 89, ' ');
			makeFile.put(strContent);

			// 欄位內容
			if (LBList == null) { // 無資料時，會出空檔

			} else {
				for (HashMap<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= tLBVo.size(); j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
								strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j)	{
			            	default:	strField = "";		break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + StringUtils.repeat(" ",3) + makeFile.fillStringL(String.valueOf(listCount), 6, '0') + StringUtils.repeat(" ",137);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB087ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB087 genExcel: ");
		this.info("LB087 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// B087 帳號轉換資料檔
		inf = "資料別(1~2);交易代碼(3~3);報送單位代號(4~6);註記對象統一編號(7~14);發生違約日(15~21);註記對象屬性(22~22);註記內容(23~120);空白(103~122);授信戶IDN/BAN(123~132);空白(133~150)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".087";		// 458+月日+序號(1).087
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B087", "聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔", strFileName);

			// 不設定CellStyle
			makeExcel.setNeedStyle(false);

			// 設定欄位寬度
			setAllWidth();

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (LBList == null) {	// 無資料時，會出空檔

			} else {
				for (HashMap<String, String> tLBVo : LBList) {
					for (int j = 1; j <= tLBVo.size(); j++) {
						if (tLBVo.get(txt1[j - 1]) == null) {
							makeExcel.setValue(i, j, "");
						} else {
							makeExcel.setValue(i, j, tLBVo.get(txt1[j - 1]));
						}
					}
					i++;
				}
			}

			long sno = makeExcel.close();
			// makeExcel.toExcel(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB087ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 6);
		makeExcel.setWidth(2, 5);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 12);
		makeExcel.setWidth(5, 11);
		makeExcel.setWidth(6, 5);
		makeExcel.setWidth(7, 84);
		makeExcel.setWidth(8, 24);
		makeExcel.setWidth(9, 14);
		makeExcel.setWidth(10,22);
	}

}
