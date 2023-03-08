package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

import net.sf.ehcache.pool.impl.BalancedAccessEvictor;

@Component
@Scope("prototype")

public class LQ007Report extends MakeReport {

	@Autowired
	public LQ007ServiceImpl lQ007ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	BigDecimal totleIntSum = BigDecimal.ZERO;
	BigDecimal balTotal = BigDecimal.ZERO;
	BigDecimal intTotal = BigDecimal.ZERO;
	BigDecimal intAATotal = BigDecimal.ZERO;
	BigDecimal intIITotal = BigDecimal.ZERO;
	BigDecimal intAATotal3=BigDecimal.ZERO;
	BigDecimal intIITotal3=BigDecimal.ZERO;
	BigDecimal intAATotal6=BigDecimal.ZERO;
	BigDecimal intIITotal6=BigDecimal.ZERO;
	BigDecimal intAATotal9=BigDecimal.ZERO;
	BigDecimal intIITotal9=BigDecimal.ZERO;
	BigDecimal intAATotal12=BigDecimal.ZERO;
	BigDecimal intIITotal12=BigDecimal.ZERO;

	public void exec(TitaVo titaVo) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LQ007";
		String fileItem = "專案放款餘額及利息收入";
		String fileName = "LQ007-專案放款餘額及利息收入";
		String defaultExcel = "LQ007_底稿_專案放款餘額及利息收入.xlsx";
		String defaultSheet = "YYYMM";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		List<Map<String, String>> LQ007List = null;
		try {
			LQ007List = lQ007ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lQ007ServiceImpl.findAll error = " + errors.toString());
		}

		int YearMonth = reportDate / 10000;
		int col = 2;
		int row1 = 0;
		makeExcel.setSheet("YYYMM", String.valueOf(titaVo.getEntDyI() / 100));

		if (LQ007List != null && !LQ007List.isEmpty()) {

			for (Map<String, String> LQ007Vo : LQ007List) {

				// only works if sql query is ordered by YearMonth
				int visibleMonth = Integer.valueOf(LQ007Vo.get("F1"));

				if (YearMonth > visibleMonth / 100) {

					if (LQ007Vo.get("F0").equals("AA")) {
						BigDecimal intAAS = getBigDecimal(LQ007Vo.get("F3"));
						intAATotal = intAATotal.add(intAAS);
					} else {
						BigDecimal intIIS = getBigDecimal(LQ007Vo.get("F3"));
						intIITotal = intIITotal.add(intIIS);
					}

					if (visibleMonth % 100 == 12) {
						if ((visibleMonth - 191100) == YearMonth - 3) {
							makeExcel.setValue(3, 2, (visibleMonth - 191100) / 100 + '年');
							if (LQ007Vo.get("F0").equals("AA")) {
								makeExcel.setValue(5, 2, LQ007Vo.get("F2"), "C");
								BigDecimal intS = getBigDecimal(LQ007Vo.get("F3"));
								intTotal = intTotal.add(intS);
								makeExcel.setValue(5, 3, formatAmt(intTotal.add(intAATotal), 3, 8), "C");
								intAATotal = BigDecimal.ZERO;
							} else {
								row1=6;
								makeExcel.setValue(row1, 2, LQ007Vo.get("F2"), "C");
								row1++;
								for (int row = 6; row <= 12; row++) {
									BigDecimal intSum = getBigDecimal(LQ007Vo.get("F3"));
									totleIntSum = totleIntSum.add(intSum);
									makeExcel.setValue(6, 3, formatAmt(totleIntSum.add(intIITotal), 3, 8), "C");
									intIITotal = BigDecimal.ZERO;
								}

							}
							totalAll(13, 2, LQ007Vo.get("F2"), LQ007Vo.get("F3"));
						} else if ((visibleMonth - 191100) == YearMonth - 2) {
							makeExcel.setValue(3, 4, (visibleMonth - 191100) / 100 + '年');
							if (LQ007Vo.get("F0").equals("AA")) {
								makeExcel.setValue(5, 4, LQ007Vo.get("F2"), "C");
								BigDecimal intS = getBigDecimal(LQ007Vo.get("F3"));
								intTotal = intTotal.add(intS);
								makeExcel.setValue(5, 5, formatAmt(intTotal.add(intAATotal), 3, 8), "C");
								intAATotal = BigDecimal.ZERO;
							} else {
								row1 = 6;
								makeExcel.setValue(row1, 4, LQ007Vo.get("F2"), "C");
								row1++;
								for (int row = 6; row <= 12; row++) {
									BigDecimal intSum = getBigDecimal(LQ007Vo.get("F3"));
									totleIntSum = totleIntSum.add(intSum);
									makeExcel.setValue(6, 5, formatAmt(totleIntSum.add(intIITotal), 3, 8), "C");
									intIITotal = BigDecimal.ZERO;
								}

							}
							totalAll(13, 4, LQ007Vo.get("F2"), LQ007Vo.get("F3"));

						} else if ((visibleMonth - 191100) == YearMonth - 1) {
							makeExcel.setValue(3, 6, (visibleMonth - 191100) / 100 + '年');
							if (LQ007Vo.get("F0").equals("AA")) {
								makeExcel.setValue(5, 6, LQ007Vo.get("F2"), "C");
								BigDecimal intS = getBigDecimal(LQ007Vo.get("F3"));
								intTotal = intTotal.add(intS);
								makeExcel.setValue(5, 7, formatAmt(intTotal.add(intAATotal), 3, 8), "C");
								intAATotal = BigDecimal.ZERO;
							} else {
								row1 = 6;
								makeExcel.setValue(row1, 6, LQ007Vo.get("F2"), "C");
								row1++;
								for (int row = 6; row <= 12; row++) {
									BigDecimal intSum = getBigDecimal(LQ007Vo.get("F3"));
									totleIntSum = totleIntSum.add(intSum);
									makeExcel.setValue(6, 7, formatAmt(totleIntSum.add(intIITotal), 3, 8), "C");
									intIITotal = BigDecimal.ZERO;
								}

							}
							totalAll(13, 6, LQ007Vo.get("F2"), LQ007Vo.get("F3"));
						}

					}

				} else {
					if(visibleMonth%100<=3) {
						if (LQ007Vo.get("F0").equals("AA")) {
							BigDecimal intAAS = getBigDecimal(LQ007Vo.get("F3"));
							
							intAATotal3 = intAATotal.add(intAAS);
						} else {
							BigDecimal intIIS = getBigDecimal(LQ007Vo.get("F3"));
							
							intIITotal3 = intIITotal.add(intIIS);
						}
					}else if(visibleMonth%100<=6) {
						if (LQ007Vo.get("F0").equals("AA")) {
							BigDecimal intAAS = getBigDecimal(LQ007Vo.get("F3"));
							
							intAATotal6 = intAATotal3.add(intAAS);
						} else {
							BigDecimal intIIS = getBigDecimal(LQ007Vo.get("F3"));
							
							intIITotal6 = intIITotal3.add(intIIS);
						}
					}else if(visibleMonth%100<=9) {
						if (LQ007Vo.get("F0").equals("AA")) {
							BigDecimal intAAS = getBigDecimal(LQ007Vo.get("F3"));
							
							intAATotal9 = intAATotal6.add(intAAS);
						} else {
							BigDecimal intIIS = getBigDecimal(LQ007Vo.get("F3"));
							
							intIITotal9 = intIITotal6.add(intIIS);
						}
					}else if(visibleMonth%100<=9) {
						if (LQ007Vo.get("F0").equals("AA")) {
							BigDecimal intAAS = getBigDecimal(LQ007Vo.get("F3"));
							
							intAATotal12 = intAATotal9.add(intAAS);
						} else {
							BigDecimal intIIS = getBigDecimal(LQ007Vo.get("F3"));
							
							intIITotal12 = intIITotal9.add(intIIS);
						}
					}
					switch (visibleMonth%100) {
					default:
						for(int row=5;row<=13;row++) {
							for(int col1=8;col1<=15;col1++) {
								makeExcel.setValue(row, col1, 0 , "C");
							}
						}
					
					case 3:						
						makeExcel.setValue(3, 8,(visibleMonth - 191100) / 100 + "年3月");
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, 8, LQ007Vo.get("F2") , "C");
							makeExcel.setValue(5, 9,  formatAmt(intAATotal3, 3,8), "C");
							
						} else {
							row1 = 6;
							makeExcel.setValue(row1, 8, LQ007Vo.get("F2") , "C");
							row1++;
							makeExcel.setValue(6, 9, formatAmt(intIITotal3, 3, 8) ,"C");
							

						}
						totalAll(13, 8, LQ007Vo.get("F2"), LQ007Vo.get("F3"));

					case 6:
						makeExcel.setValue(3, 10,(visibleMonth - 191100) / 100 + "年6月");
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, 10, LQ007Vo.get("F2") , "C");
							makeExcel.setValue(5, 11,  formatAmt(intAATotal6, 3, 8), "C");
							
						} else {
							row1 = 6;
							makeExcel.setValue(row1, 10, LQ007Vo.get("F2") , "C");
							row1++;
							makeExcel.setValue(6, 11, formatAmt(intIITotal6, 3, 8) ,"C");
						}
						totalAll(13, 10, LQ007Vo.get("F2"), LQ007Vo.get("F3"));
				
					case 9:
						makeExcel.setValue(3, 12,(visibleMonth - 191100) / 100 + "年9月");
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, 12, LQ007Vo.get("F2") , "C");
							makeExcel.setValue(5, 13,  formatAmt(intAATotal9, 3,8), "C");
							
						} else {
							row1 = 6;
							makeExcel.setValue(row1, 12, LQ007Vo.get("F2") , "C");
							row1++;
							makeExcel.setValue(6, 13, formatAmt(intIITotal9, 3, 8) ,"C");
						}
						totalAll(13, 12, LQ007Vo.get("F2"), LQ007Vo.get("F3"));

					case 12:
						makeExcel.setValue(3, 14,(visibleMonth - 191100) / 100 + "年12月");
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, 14, LQ007Vo.get("F2") , "C");
							makeExcel.setValue(5, 15,  formatAmt(intAATotal12, 3,8), "C");
							
						} else {
							row1 = 6;
							makeExcel.setValue(row1, 14, LQ007Vo.get("F2") , "C");
							row1++;
							makeExcel.setValue(6, 15, formatAmt(intIITotal12, 3, 8) ,"C");
						}
						totalAll(13, 14, LQ007Vo.get("F2"), LQ007Vo.get("F3"));

					}
				}
				// 第二個表格
				if (visibleMonth == reportDate / 100) {

					if (LQ007Vo.get("F0").equals("AA")) {
						makeExcel.setValue(5, 17, LQ007Vo.get("F2"), "C");
						makeExcel.setValue(5, 18,formatAmt(intAATotal12, 3, 8) , "C");
					} else {
						row1 = 6;
						makeExcel.setValue(row1, 17, LQ007Vo.get("F2"), "C");
						row1++;
						makeExcel.setValue(6, 18, formatAmt(intIITotal12, 3, 8), "C");
						
					}
					totalAll(13, 17, LQ007Vo.get("F2"), LQ007Vo.get("F3"));

				}

			}

		} else {
			makeExcel.setValue(5, 2, "本日無資料");

		}

		makeExcel.close();
		// this.toPdf(sno);

//		if (LQ007List != null && !LQ007List.isEmpty()) {
//
//			for (Map<String, String> LQ007Vo : LQ007List) {
//
//				// only works if sql query is ordered by YearMonth
//				int visibleMonth = Integer.valueOf(LQ007Vo.get("F1"));
//
//				if (YearMonth > visibleMonth / 100) {
//
//					if (LQ007Vo.get("F0").equals("AA")) {
//						BigDecimal intAAS = getBigDecimal(LQ007Vo.get("F3"));
//						intAATotal = intAATotal.add(intAAS);
//					} else {
//						BigDecimal intIIS = getBigDecimal(LQ007Vo.get("F3"));
//						intIITotal = intIITotal.add(intIIS);
//					}
//					if (visibleMonth % 100 == 12) {
//
//						makeExcel.setValue(3, col, (visibleMonth - 191100) / 100 + '年');
//						if (LQ007Vo.get("F0").equals("AA")) {
//							makeExcel.setValue(5, col, LQ007Vo.get("F2"), "C");
//							BigDecimal intS = getBigDecimal(LQ007Vo.get("F3"));
//							intTotal = intTotal.add(intS);
//							makeExcel.setValue(5, col + 1, formatAmt(intTotal.add(intAATotal), 3, 8), "C");
//							intAATotal = BigDecimal.ZERO;
//						} else {
//							int row1 = 6;
//							makeExcel.setValue(row1, col, LQ007Vo.get("F2"), "C");
//							for (int row = 6; row <= 12; row++) {
//								BigDecimal intSum = getBigDecimal(LQ007Vo.get("F3"));
//								totleIntSum = totleIntSum.add(intSum);
//								makeExcel.setValue(6, col + 1, formatAmt(totleIntSum.add(intIITotal), 3, 8), "C");
//								intIITotal = BigDecimal.ZERO;
//							}
//
//							row1++;
//
//						}
//						totalAll(13, col, LQ007Vo.get("F2"), LQ007Vo.get("F3"));
////						BigDecimal bal = getBigDecimal(LQ007Vo.get("F2"));
////						balTotal = balTotal.add(bal);
////						BigDecimal intS = getBigDecimal(LQ007Vo.get("F3"));
////						intTotal = intTotal.add(intS);
////						makeExcel.setValue(13, col, formatAmt(balTotal, 3, 8), "C");
////
////						makeExcel.setValue(13, col + 1, formatAmt(intTotal, 3, 8), "C");
//						col += 2;
//					}
//
//				} else {
//					int mon = ((titaVo.getEntDyI() / 100) % 100) % 3;
//					int tmpCol = visibleMonth % 100 / 3 - 1;
//
//					makeExcel.setValue(3, col + (2 * tmpCol),
//							(visibleMonth - 191100) / 100 + "年" + (visibleMonth - 191100) % 100 + '月');
//					if (LQ007Vo.get("F0").equals("AA")) {
//						makeExcel.setValue(5, col + (2 * tmpCol), (mon == 0) ? LQ007Vo.get("F2") : 0, "C");
//						makeExcel.setValue(5, col + (2 * tmpCol) + 1, (mon == 0) ? LQ007Vo.get("F3") : 0, "C");
//					} else {
//						int row1 = 6;
//						makeExcel.setValue(row1, col + (2 * tmpCol), (mon == 0) ? LQ007Vo.get("F2") : 0, "C");
//						for (int row = 6; row <= 12; row++) {
//							BigDecimal IntSum = getBigDecimal(LQ007Vo.get("F3"));
//							totleIntSum = totleIntSum.add(IntSum);
//							makeExcel.setValue(6, col + (2 * tmpCol) + 1, (mon == 0) ? formatAmt(totleIntSum, 3, 8) : 0,
//									"C");
//						}
//						row1++;
//
//					}
//					totalAll(13, col + (2 * tmpCol), LQ007Vo.get("F2"), LQ007Vo.get("F3"));
////					BigDecimal bal = getBigDecimal(LQ007Vo.get("F2"));
////					balTotal = balTotal.add(bal);
////					BigDecimal intS = getBigDecimal(LQ007Vo.get("F2"));
////					intTotal = intTotal.add(intS);
////					makeExcel.setValue(13, col + (2 * tmpCol), (mon==0)?formatAmt(balTotal, 3, 8):0, "C");
////
////					makeExcel.setValue(13, col + (2 * tmpCol) + 1, (mon==0)?formatAmt(intTotal, 3, 8):0, "C");
//
//				}
//				// 第二個表格
//				if (visibleMonth == reportDate / 100) {
//
//					if (LQ007Vo.get("F0").equals("AA")) {
//						makeExcel.setValue(5, 17, LQ007Vo.get("F2"), "C");
//						makeExcel.setValue(5, 18, LQ007Vo.get("F3"), "C");
//					} else {
//						int row1 = 6;
//						makeExcel.setValue(row1, 17, LQ007Vo.get("F2"), "C");
//						for (int row = 6; row <= 12; row++) {
//
//							BigDecimal IntSum = getBigDecimal(LQ007Vo.get("F3"));
//							totleIntSum = totleIntSum.add(IntSum);
//							makeExcel.setValue(6, 18, formatAmt(totleIntSum, 3, 8), "C");
//						}
//						row1++;
//					}
//					totalAll(13, 17, LQ007Vo.get("F2"), LQ007Vo.get("F3"));
////					BigDecimal bal = getBigDecimal(LQ007Vo.get("F2"));
////					balTotal = balTotal.add(bal);
////					BigDecimal intS = getBigDecimal(LQ007Vo.get("F2"));
////					intTotal = intTotal.add(intS);
////					makeExcel.setValue(13, col + 8, formatAmt(balTotal, 3, 8), "C");
////
////					makeExcel.setValue(13, col + 9, formatAmt(intTotal, 3, 8), "C");
//
//				}
//
//			}
//
//		} else {
//			makeExcel.setValue(5, 2, "本日無資料");
//
//		}
//
//		makeExcel.close();
//		// this.toPdf(sno);

	}

	// 合計
	public void totalAll(int row, int col, String balSum, String intSum) throws LogicException {
		BigDecimal bal = getBigDecimal(balSum);
		balTotal = balTotal.add(bal);
		BigDecimal intS = getBigDecimal(intSum);
		intTotal = intTotal.add(intS);
		makeExcel.setValue(row, col, formatAmt(balTotal, 3, 8), "C");

		makeExcel.setValue(row, col + 1, formatAmt(intTotal, 3, 8), "C");
	}

}
