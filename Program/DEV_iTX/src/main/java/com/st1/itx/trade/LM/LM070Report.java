package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM070ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LM070Report")
@Scope("prototype")

public class LM070Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM070Report.class);

	@Autowired
	LM070ServiceImpl lm070ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM070Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM070", "介紹人加碼獎勵津貼明細", "LM070介紹人加碼獎勵津貼明細",
				"介紹人加碼獎勵津貼明細.xlsx", "Q2573_13");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = lm070ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM070ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			String fdnm = "";
			int row = 1;

			for (Map<String, String> tLDVo : fnAllList) {
				this.info("tLDVo-------->" + tLDVo.toString());
				row++;
				for (int i = 0; i < tLDVo.size(); i++) {
					fdnm = "F" + String.valueOf(i);
					switch (i) {
					case 3:
					case 4:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 15:
					case 18:
					case 19:
						// 字串左靠
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					default:
						// 戶號(數字右靠)
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, new BigDecimal(tLDVo.get(fdnm)));
						}
						break;
					}
				}
			}
		
		}else {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
//	public void exec(TitaVo titaVo) throws LogicException {
//
//		this.info("LM070Report exec");
//
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM070", "介紹人加碼獎勵津貼明細", "LM070介紹人加碼獎勵津貼明細",
//				"介紹人加碼獎勵津貼明細.xlsx", "Q2573_13");
//
//		List<Map<String, String>> wkSsnList = new ArrayList<>();
//
//		try {
//			wkSsnList = lm070ServiceImpl.wkSsn(titaVo);
//
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("LM070ServiceImpl.WorkSeason error = " + errors.toString());
//		}
//
//		if (wkSsnList.size() > 0) {
//			findAll(titaVo, wkSsnList.get(0));
//		}
//
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}
//
//	private void findAll(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
//
//		this.info("===========in wrExcel");
//
//		List<Map<String, String>> fnAllList = new ArrayList<>();
//
//		try {
//			fnAllList = lm070ServiceImpl.findAll(titaVo, wkSsnVo);
//
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("LM070ServiceImpl.findAll error = " + errors.toString());
//		}
//
//		if (fnAllList.size() > 0) {
//			String fdnm = "";
//			int row = 1;
//
//			for (Map<String, String> tLDVo : fnAllList) {
//				this.info("tLDVo-------->" + tLDVo.toString());
//				row++;
//				for (int i = 0; i < tLDVo.size(); i++) {
//					fdnm = "F" + String.valueOf(i);
//					switch (i) {
//					case 3:
//					case 4:
//					case 6:
//					case 7:
//					case 8:
//					case 9:
//					case 10:
//					case 11:
//					case 12:
//					case 13:
//					case 15:
//					case 18:
//					case 19:
//						// 字串左靠
//						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
//						break;
//					default:
//						// 戶號(數字右靠)
//						if (tLDVo.get(fdnm).equals("")) {
//							makeExcel.setValue(row, i + 1, 0);
//						} else {
//							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
//						}
//						break;
//					}
//				}
//			}
//		}
//	}

}
