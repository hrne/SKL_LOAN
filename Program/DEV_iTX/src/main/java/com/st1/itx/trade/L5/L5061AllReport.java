package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L5061ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L5061AllReport")
@Scope("prototype")

public class L5061AllReport extends MakeReport {

	@Autowired
	public L5061ServiceImpl iL5061ServiceImpl;

	@Autowired
	public CdEmpService iCdEmpService;

	@Autowired
	public CdCodeService iCdCodeService;

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

	public long exec(TitaVo titaVo) throws LogicException {

		this.info("L5061Report start success");

		Integer.valueOf(titaVo.getParam("OptionCode"));
		int dateS = Integer.valueOf(titaVo.getParam("DateS"));
		int dateE = Integer.valueOf(titaVo.getParam("DateE"));
		String sDateS = String.valueOf(dateS);
		String sdateE = String.valueOf(dateE);
		int iCustNoS = Integer.valueOf(titaVo.getParam("CustS"));
		int iCustNoE = Integer.valueOf(titaVo.getParam("CustE"));
		String sCustNoS = String.valueOf(iCustNoS);
		String sCustNoE = String.valueOf(iCustNoE);

		List<String> telHeader = new ArrayList<>();
		telHeader.addAll(Arrays.asList("戶號", "額度", "戶名", "戶況", "放款餘額", "繳息迄日", "逾期數", "電催日期", "電催時間", "通話結果", "催收人員", "法務人員"));
		List<String> meetHeader = new ArrayList<>();
		meetHeader.addAll(Arrays.asList("戶號", "額度", "戶名", "戶況", "放款餘額", "繳息迄日", "逾期數", "面催日期", "面催時間", "面催記錄", "催收人員", "法務人員"));
		List<String> letterHeader = new ArrayList<>();
		letterHeader.addAll(Arrays.asList("戶號", "額度", "戶名", "戶況", "放款餘額", "繳息迄日", "逾期數", "函催日期", "發函種類", "催收人員", "法務人員"));

		List<Map<String, String>> t5061SqlReturn = new ArrayList<Map<String, String>>();
		List<Map<String, String>> m5061SqlReturn = new ArrayList<Map<String, String>>();
		List<Map<String, String>> l5061SqlReturn = new ArrayList<Map<String, String>>();
		String fileName = " ";
		if (dateS > 0) {
			fileName = "催收催繳明細表_" + sDateS + "_" + sdateE + "(逾期件全部查詢)";
		} else {
			fileName = "催收催繳明細表_" + sCustNoS + "_" + sCustNoE + "(逾期件全部查詢)";
		}

		int iFlag = 0;

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5061", fileName, fileName);

		// 電催清單處理開始
		try {
			t5061SqlReturn = iL5061ServiceImpl.FindData(sDateS, sdateE, iCustNoS, iCustNoE, 1, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		// 列數
		int row = 1;

		// 表頭列數
		int hcol = 0;

		if (t5061SqlReturn.isEmpty()) {
//			throw new LogicException(titaVo, "E0001","查無資料");
		} else {
			makeExcel.setSheet("L5061", "電催資料");
			// 表頭
			for (String content : telHeader) {
				makeExcel.setValue(row, hcol + 1, content);
				hcol++;
			}
			for (Map<String, String> i5061SqlReturn : t5061SqlReturn) {
				row++;
				for (int col = 0; col < i5061SqlReturn.size(); col++) {
					String colName = "F" + String.valueOf(col);
					switch (col) {
					// 處理左右靠
					case 0:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 1:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 2: // 戶名
						makeExcel.setValue(row, col + 1, StringCut.replaceLineUp(i5061SqlReturn.get(colName)));
						break;
					case 3:
						int status = Integer.valueOf(i5061SqlReturn.get(colName));
						this.info("戶況====" + status);
						Slice<CdCode> iCdCode = null;
						iCdCode = iCdCodeService.getCodeList(3, "Status", this.index, this.limit, titaVo);
						for (CdCode xCdCode : iCdCode) {
							this.info("戶況代碼====" + xCdCode.toString());
							if (Integer.valueOf(xCdCode.getCode()) == status) {
								makeExcel.setValue(row, col + 1, xCdCode.getItem());
								break;
							}
						}
						break;
					case 4:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 5:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 6:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 9:
						int resultCode = Integer.valueOf(i5061SqlReturn.get(colName));
						Slice<CdCode> sCdCode = null;
						sCdCode = iCdCodeService.getCodeList(5, "ResultCode", this.index, this.limit, titaVo);
						for (CdCode tCdCode : sCdCode) {
							if (Integer.valueOf(tCdCode.getCode()) == resultCode) {
								makeExcel.setValue(row, col + 1, tCdCode.getItem());
								break;
							}
						}
						break;
					case 10:
						CdEmp iCdEmp = new CdEmp();
						String employeeName = "";
						employeeName = i5061SqlReturn.get(colName);
						if (employeeName.equals("")) {
							makeExcel.setValue(row, col + 1, "");
						}
						iCdEmp = iCdEmpService.findById(employeeName, titaVo);
						if (iCdEmp == null) {
							makeExcel.setValue(row, col + 1, "");
						} else {
							makeExcel.setValue(row, col + 1, iCdEmp.getFullname());
						}
						break;
					case 11:
						CdEmp pCdEmp = new CdEmp();
						String iemployeeName = "";
						iemployeeName = i5061SqlReturn.get(colName);
						if (iemployeeName.equals("")) {
							makeExcel.setValue(row, col + 1, "");
						}
						pCdEmp = iCdEmpService.findById(iemployeeName, titaVo);
						if (pCdEmp == null) {
							makeExcel.setValue(row, col + 1, "");
						} else {
							makeExcel.setValue(row, col + 1, pCdEmp.getFullname());
						}
						break;
					default:
						makeExcel.setValue(row, col + 1, i5061SqlReturn.get(colName));
						break;
					}
					iFlag++;
				}
			}
		}
		// 面催清單處理開始
		try {
			m5061SqlReturn = iL5061ServiceImpl.FindData(sDateS, sdateE, iCustNoS, iCustNoE, 2, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		row = 0;
		if (m5061SqlReturn.isEmpty()) {
//			throw new LogicException(titaVo, "E0001","查無資料");
		} else {
			makeExcel.newSheet("面催資料");
			hcol = 0;
			row++;
			for (String content : meetHeader) {
				makeExcel.setValue(row, hcol + 1, content);
				hcol++;
			}

			for (Map<String, String> i5061SqlReturn : m5061SqlReturn) {
				row++;
				for (int col = 0; col < i5061SqlReturn.size(); col++) {
					String colName = "F" + String.valueOf(col);
					switch (col) {
					// 處理左右靠
					case 0:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 1:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 2: // 戶名
						makeExcel.setValue(row, col + 1, StringCut.replaceLineUp(i5061SqlReturn.get(colName)));
						break;
					case 3:
						int status = Integer.valueOf(i5061SqlReturn.get(colName));
						this.info("戶況====" + status);
						Slice<CdCode> iCdCode = null;
						iCdCode = iCdCodeService.getCodeList(3, "Status", this.index, this.limit, titaVo);
						for (CdCode xCdCode : iCdCode) {
							this.info("戶況代碼====" + xCdCode.toString());
							if (Integer.valueOf(xCdCode.getCode()) == status) {
								makeExcel.setValue(row, col + 1, xCdCode.getItem());
								break;
							}
						}
						break;
					case 4:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 5:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 6:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 9:
						String remark = i5061SqlReturn.get(colName);
						remark = remark.replace("$n", "\n");
						makeExcel.setValue(row, col + 1, remark);
						break;
					case 10:
						CdEmp iCdEmp = new CdEmp();
						String employeeName = "";
						employeeName = i5061SqlReturn.get(colName);
						if (employeeName.equals("")) {
							makeExcel.setValue(row, col + 1, "");
						}
						iCdEmp = iCdEmpService.findById(employeeName, titaVo);
						if (iCdEmp == null) {
							makeExcel.setValue(row, col + 1, "");
						} else {
							makeExcel.setValue(row, col + 1, iCdEmp.getFullname());
						}
						break;
					case 11:
						CdEmp pCdEmp = new CdEmp();
						String iemployeeName = "";
						iemployeeName = i5061SqlReturn.get(colName);
						if (iemployeeName.equals("")) {
							makeExcel.setValue(row, col + 1, "");
						}
						pCdEmp = iCdEmpService.findById(iemployeeName, titaVo);
						if (pCdEmp == null) {
							makeExcel.setValue(row, col + 1, "");
						} else {
							makeExcel.setValue(row, col + 1, pCdEmp.getFullname());
						}
						break;
					default:
						makeExcel.setValue(row, col + 1, i5061SqlReturn.get(colName));
						break;

					}
					iFlag++;
				}
			}
		}

		// 函催清單處理開始

		try {
			l5061SqlReturn = iL5061ServiceImpl.FindData(sDateS, sdateE, iCustNoS, iCustNoE, 3, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		row = 0;
		if (l5061SqlReturn.isEmpty()) {
//			throw new LogicException(titaVo, "E0001","查無資料");
		} else {
			makeExcel.newSheet("函催資料");
			hcol = 0;
			row++;
			for (String content : letterHeader) {
				makeExcel.setValue(row, hcol + 1, content);
				hcol++;
			}
			for (Map<String, String> i5061SqlReturn : l5061SqlReturn) {
				row++;
				for (int col = 0; col < i5061SqlReturn.size(); col++) {
					String colName = "F" + String.valueOf(col);
					switch (col) {
					// 處理左右靠
					case 0:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 1:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 2: // 戶名
						makeExcel.setValue(row, col + 1, StringCut.replaceLineUp(i5061SqlReturn.get(colName)));
						break;
					case 3:
						int status = Integer.valueOf(i5061SqlReturn.get(colName));
						this.info("戶況====" + status);
						Slice<CdCode> iCdCode = null;
						iCdCode = iCdCodeService.getCodeList(3, "Status", this.index, this.limit, titaVo);
						for (CdCode xCdCode : iCdCode) {
							this.info("戶況代碼====" + xCdCode.toString());
							if (Integer.valueOf(xCdCode.getCode()) == status) {
								makeExcel.setValue(row, col + 1, xCdCode.getItem());
								break;
							}
						}
						break;
					case 4:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 5:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 6:
						makeExcel.setValue(row, col + 1, Integer.valueOf(i5061SqlReturn.get(colName)));
						break;
					case 8:
						int mailTypeCode = Integer.valueOf(i5061SqlReturn.get(colName));
						Slice<CdCode> sCdCode = null;
						sCdCode = iCdCodeService.getCodeList(5, "MailTypeCode", this.index, this.limit, titaVo);
						for (CdCode tCdCode : sCdCode) {
							if (Integer.valueOf(tCdCode.getCode()) == mailTypeCode) {
								makeExcel.setValue(row, col + 1, tCdCode.getItem());
								break;
							}
						}
						break;
					case 9:
						CdEmp iCdEmp = new CdEmp();
						String employeeName = "";
						employeeName = i5061SqlReturn.get(colName);
						if (employeeName.equals("")) {
							makeExcel.setValue(row, col + 1, "");
						}
						iCdEmp = iCdEmpService.findById(employeeName, titaVo);
						if (iCdEmp == null) {
							makeExcel.setValue(row, col + 1, "");
						} else {
							makeExcel.setValue(row, col + 1, iCdEmp.getFullname());
						}
						break;
					case 10:
						CdEmp pCdEmp = new CdEmp();
						String iemployeeName = "";
						iemployeeName = i5061SqlReturn.get(colName);
						if (iemployeeName.equals("")) {
							makeExcel.setValue(row, col + 1, "");
						}
						pCdEmp = iCdEmpService.findById(iemployeeName, titaVo);
						if (pCdEmp == null) {
							makeExcel.setValue(row, col + 1, "");
						} else {
							makeExcel.setValue(row, col + 1, pCdEmp.getFullname());
						}
						break;
					default:
						makeExcel.setValue(row, col + 1, i5061SqlReturn.get(colName));
						break;

					}
					iFlag++;
				}
			}
		}

		if (iFlag == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

		return sno;
	}

}
