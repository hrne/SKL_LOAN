package com.st1.itx.trade.LW;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LW003ServiceImpl;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LW003Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LW003Report.class);

	@Autowired
	LW003ServiceImpl LW003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LW003Report exec");

//getEntDyI()帳務日 getKinbr()單位
		/**
		 * 開啟excel製檔<br>
		 * 
		 * @param titaVo     titaVo
		 * @param date       日期
		 * @param brno       單位
		 * @param filecode   檔案編號
		 * @param fileitem   檔案說明
		 * @param filename   輸出檔案名稱(不含副檔名,預設為.xlsx)
		 * @param sheetnanme 新建Sheet名稱
		 * @throws LogicException LogicException
		 */
//		open(TitaVo titaVo, int date, String brno, String filecode, String fileitem, String filename, String sheetnanme)
//		 open(TitaVo titaVo, int date, String brno, String filecode, String fileitem, String filename, String defaultExcel, Object defaultSheet) 
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LW003", "房貸獎勵費用率統計表", "LW003房貸獎勵費用率", "房貸獎勵費用率.xls", "獎勵費用率");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LW003", "房貸獎勵費用率統計表", "LW003房貸獎勵費用率", "獎勵費用率");
		List<Map<String, String>> wkSsnList = new ArrayList<>();

		try {

			wkSsnList = LW003ServiceImpl.wkSsn(titaVo);

			this.info("0." + wkSsnList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LW003ServiceImpl.WorkSeason error = " + errors.toString());
		}

		if (wkSsnList.size() > 0) {
//			執行excel_init
			this.info("0." + titaVo);
			this.info("0.." + wkSsnList.get(0));
//			陣列中第一個位置的數組丟進下面兩個method做處理
			excel_init(titaVo, wkSsnList.get(0));
			findAll(titaVo, wkSsnList.get(0));
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void excel_init(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("1." + wkSsnVo.get("F0"));
//		西元年轉民國 get組別中的name=F0 
		String ROCYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);
		this.info("2." + ROCYear);
//		月份
		this.info("3." + wkSsnVo.get("F1"));
		int mm = Integer.valueOf(wkSsnVo.get("F1"));
		this.info("4." + mm);
//		一樣取titaVo數組中的 name=ENTDY
		this.info(titaVo.get("ENTDY") + "-----" + titaVo.get("ENTDY").substring(6, 8) + "-----" + titaVo.get("ENTDY").substring(6, 8).replaceFirst("^0*", ""));
//		標題串連
		String tmp = ROCYear + "年放款部第1~" + wkSsnVo.get("F1") + "工作月房貸獎勵費用率統計表\n" + "[獎勵費 /個金總業績 ] ～ " + titaVo.get("ENTDY").substring(4, 6).replaceFirst("^0*", "") + "/"
				+ titaVo.get("ENTDY").substring(6, 8).replaceFirst("^0*", "") + "止";
//		串接結果
		this.info(tmp);
//		標題位置 第1列 第1欄
		makeExcel.setValue(1, 1, tmp);
//		makeExcel.setRange(1, 5, 1, 10);

//		makeExcel.setValue(2, 1, tmp);
		for (int i = 1; i <= 13; i++) {
//			設置第2列 第x欄 放入民國年.月
			makeExcel.setValue(2, i + 3, ROCYear + "." + String.format("%02d", i));

//			makeExcel.setValue(19, i + 3, 0);
		}
	}

	private void findAll(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {

		this.info("===========in wrExcel");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = LW003ServiceImpl.findAll(titaVo, wkSsnVo);
			this.info("5." + fnAllList);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LW003ServiceImpl.findAll error = " + errors.toString());
		}

		int mm = 0;

		if (fnAllList.size() > 0) {
			for (Map<String, String> tLDVo : fnAllList) {
//				{F0=202002, F1=150}, {F0=202003, F1=600}, {F0=202001, F1=246}
				mm = Integer.valueOf(tLDVo.get("F0")) % 100;
				this.info("6." + mm);
				makeExcel.setValue(19, mm + 3, Float.valueOf(tLDVo.get("F1")), "#,##0");
				this.info("6." + tLDVo.get("F1"));
			}
		} else {
			makeExcel.setValue(3, 4, "本日無資料");
		}
	}
}
