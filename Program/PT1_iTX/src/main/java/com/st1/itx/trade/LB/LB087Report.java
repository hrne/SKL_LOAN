package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB087ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB087Report")
@Scope("prototype")

public class LB087Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB087ServiceImpl lB087ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public SystemParasService sSystemParasService;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LB087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔
		this.info("-----strToday=" + strToday);
		this.info("-----strTodayMM=" + strTodayMM);
		this.info("-----strTodaydd=" + strTodaydd);

		// 系統營業日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底營業日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();
		// 上月月底日
		int lmndyf = this.txBuffer.getTxCom().getLmndyf();

		int dataMonth = 0;

		if (tbsdyf == mfbsdyf) {
			// 今日為月底營業日:產本月報表
			dataMonth = tbsdyf / 100;
		} else {
			// 今日非月底營業日:產上月報表
			dataMonth = lmndyf / 100;
		}

		this.info("dataMonth= " + dataMonth);

		List<Map<String, String>> LBList = null;
		try {
//			LBList = lB087ServiceImpl.findAll(dataMonth,titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB087Report LB087ServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		if (LBList == null) {
			listCount = 0;
		}

		this.info("--------LBList.size()=" + listCount);

		try {
			// txt
			genFile(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB087Report.genFile error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LB087Report.genExcel error = " + errors.toString());
			return false;
		}

		return true;

	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB087 genFile : ");

		int fileday = Integer.parseInt(titaVo.getParam("FileDay")); // 民國年月日 -畫面輸入報送日期
		if (fileday > 0) {
			strToday = String.valueOf(fileday); // 7位 民國年
			strTodayMM = strToday.substring(3, 5); // 月
			strTodaydd = strToday.substring(5, 7); // 日
		}
		int ifileNo = Integer.parseInt(titaVo.getParam("FileNo"));// 檔案序號
		String sfileNo1 = String.valueOf(ifileNo);
		String sfileNo2 = titaVo.getParam("FileNo");
		if (ifileNo == 0) {
			sfileNo1 = "1";
			sfileNo2 = "01";
		}

		// 查詢系統參數設定檔-JCIC放款報送人員資料
		String iRimBusinessType = "LN";
		String jcicEmpName = "";
		String jcicEmpTel = "";
		SystemParas tSystemParas = sSystemParasService.findById(iRimBusinessType, titaVo);
		/* 如有找到資料 */
		if (tSystemParas != null) {
			jcicEmpName = tSystemParas.getJcicEmpName();
			jcicEmpTel = tSystemParas.getJcicEmpTel();
			if (jcicEmpName == null || jcicEmpTel == null) {
				throw new LogicException(titaVo, "E0015", "請執行L8501設定JCIC放款報送人員資料");
			}
		} else {
			throw new LogicException(titaVo, "E0001", "系統參數設定檔"); // 查無資料
		}

		String txt = "";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".087"; // 458+月日+序號.087
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B087", "聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B087-V01-458" + StringUtils.repeat(" ", 5) + strToday + sfileNo2 + StringUtils.repeat(" ", 10) + makeFile.fillStringR(jcicEmpTel, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" + jcicEmpName, 89, ' ');
			makeFile.put(strContent);

			// 欄位內容
			if (LBList == null) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 10; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + StringUtils.repeat(" ", 3) + makeFile.fillStringL(String.valueOf(listCount), 6, '0') + StringUtils.repeat(" ", 137);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB087ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB087 genExcel: ");
		this.info("LB087 genExcel TitaVo=" + titaVo);

		int fileday = Integer.parseInt(titaVo.getParam("FileDay")); // 民國年月日 -畫面輸入報送日期
		if (fileday > 0) {
			strToday = String.valueOf(fileday); // 7位 民國年
			strTodayMM = strToday.substring(3, 5); // 月
			strTodaydd = strToday.substring(5, 7); // 日
		}
		int ifileNo = Integer.parseInt(titaVo.getParam("FileNo"));// 檔案序號
		String sfileNo1 = String.valueOf(ifileNo);
		if (ifileNo == 0) {
			sfileNo1 = "1";
		}
		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B087 帳號轉換資料檔
		inf = "資料別(1~2),交易代碼(3),報送單位代號(4~6),註記對象統一編號(7~14),發生違約日(15~21),註記對象屬性(22),註記內容(23~120)," + "空白(103~122),授信戶IDN/BAN(123~132),空白(133~150)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9";

		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			String strFileName = "458" + strTodayMM + strTodaydd + sfileNo1 + ".087.CSV"; // 458+月日+序號+.087.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B087", "聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList == null) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 10; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 10) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB087ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
