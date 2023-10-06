package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FinHoldRel;
import com.st1.itx.db.domain.FinHoldRelId;
import com.st1.itx.db.domain.LifeRelEmp;
import com.st1.itx.db.domain.LifeRelEmpId;
import com.st1.itx.db.domain.LifeRelHead;
import com.st1.itx.db.domain.LifeRelHeadId;
import com.st1.itx.db.domain.StakeholdersStaff;
import com.st1.itx.db.service.FinHoldRelService;
import com.st1.itx.db.service.LifeRelEmpService;
import com.st1.itx.db.service.LifeRelHeadService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.service.StakeholdersStaffService;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7206")
@Scope("prototype")
/**
 * 人壽利關人上傳作業
 *
 * @author Ted
 * @version 1.0.0
 */
public class L7206 extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public StakeholdersStaffService tStakeholdersStaffService;
	@Autowired
	public LifeRelHeadService tLifeRelHeadService;
	@Autowired
	public LifeRelEmpService tLifeRelEmpService;
	@Autowired
	public FinHoldRelService tFinHoldRelService;
	@Autowired
	public MonthlyFacBalService tMonthlyFacBalService;
	@Autowired
	MakeExcel makeExcel;
	@Autowired
	public WebClient webClient;
	@Autowired
	DateUtil dDateUtil;

	StringCut sStringCut;

	private int inputAcDate = 0;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7206 ");
		this.totaVo.init(titaVo);

		inputAcDate = Integer.valueOf(titaVo.getParam("inputAcDate")) + 19110000;
		if (inputAcDate == 19110000) {
			throw new LogicException(titaVo, "E0014", "請輸入會計日期");
		}

		// 上傳檔案
		// 0:人壽利關人職員名單[LA$RLTP] ;csv xlsx xls
		// 1:人壽利關人負責人名單[T07];xlsx xls
		// 2:人壽負利關人職員名單[T07_2];xlsx xls
		// 3:金控利關人名單[T044];xlsx xls
		// 4:以 FTP 檔案更新資料庫
		int iFunctionCode = Integer.valueOf(titaVo.getParam("FunctionCode"));

		if (iFunctionCode != 4) {
			uploadFile(iFunctionCode, titaVo);
		} else {
			// 2023-08-21 Wei 新增 from SKL-IT 葛經理
			MySpring.newTask("L7206p", this.txBuffer, titaVo);
		}

		isLatestData(titaVo);

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void uploadFile(int iFunctionCode, TitaVo titaVo) throws LogicException {

		if (titaVo.getParam("FILENA").trim().length() == 0) {
			throw new LogicException(titaVo, "E0014", "沒有選擇檔案");
		}

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename=" + filename);

		ArrayList<String> dataLineList = new ArrayList<>();

		titaVo.keepOrgDataBase();// 保留原本記號

		String iFunctionName = titaVo.getParam("FunctionCodeX").toString();

		this.info("iFunctionCode=" + iFunctionCode);

		// 路徑
		String[] extension = null;
		if (filename.contains("\\/")) {
			extension = filename.split("\\/");
		} else if (filename.contains("\\\\")) {
			extension = filename.split("\\\\");
		} else if (filename.contains("/")) {
			extension = filename.split("/");
		}

		String tmpNameG[] = extension[extension.length - 1].split("\\.");
		// 檔案名稱
		String tmpName = tmpNameG[tmpNameG.length - 2];
		// 附檔名
		String fileExt = tmpNameG[tmpNameG.length - 1].toLowerCase();
		this.info("file fileName=" + tmpName);
		this.info("file fileExt=" + fileExt);

		// 月底營業日
		// 20220101
		// 10 17
//		int acDate = parse.stringToInteger(tmpName.substring(tmpName.length() - 8, tmpName.length()));
		int acDate = inputAcDate;

		this.info("acDate=" + acDate);
		// 判斷選擇上傳的項目與實際上傳的檔案名稱要相符(避免上傳錯檔案，欄位不符會報錯)
		if ("T07_2".equals(tmpName.substring(0, 5))
				&& tmpName.contains("T07_2_") && "T07_2".equals(iFunctionName
						.substring(iFunctionName.length() - (1 + "T07_2".length()), iFunctionName.length() - 1))
				&& iFunctionCode == 2) {
			// 人壽職員名單檔(純職員無主管)
		} else if ("T07".equals(tmpName.substring(0, 3))
				&& !tmpName.contains("T07_2_") && "T07".equals(iFunctionName
						.substring(iFunctionName.length() - (1 + "T07".length()), iFunctionName.length() - 1))
				&& iFunctionCode == 1) {
			// 人壽負責人名單檔
		} else if ("T044".equals(tmpName.substring(0, 4)) && iFunctionName.contains("T044") && iFunctionCode == 3) {
			// 金控利關人名單檔
		} else if ((tmpName.contains("LA$RLTP") && iFunctionName.contains("LA$RLTP")) || iFunctionCode == 0) {
			// 人壽利關人職員名單檔 主要給LM013 金檢報表使用
		} else {
			String ErrorMsg = "請確認上傳選項與上傳檔案不符合";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		if ("xlsx".equals(fileExt) || "xls".equals(fileExt)) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);

			// 切資料
			setValueFromFileExcel(titaVo, iFunctionCode);

		} else if ("csv".equals(fileExt)) {

			// 編碼參數，設定為UTF-8 || big5
			try {

				dataLineList = fileCom.intputTxt(filename, "UTF-8");
//				this.info("dataLineList = " + dataLineList);

			} catch (IOException e) {
				this.info("L7206(" + filename + ") : " + e.getMessage());
				String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + filename;

				throw new LogicException(titaVo, "E0014", ErrorMsg);
			}

			setValueFromFile(dataLineList, iFunctionCode);

		} else {
			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		int CountAll = occursList.size();
		int CountS = 0;

		this.info("iFunctionName.occursList = " + occursList.toString());

		// 0:人壽利關人職員名單[LA$RLTP] ;csv xlsx xls
		if (iFunctionCode == 0) {

			List<StakeholdersStaff> instakeholdersStaff = new ArrayList<StakeholdersStaff>();

			List<StakeholdersStaff> delStakeholdersStaff = new ArrayList<StakeholdersStaff>();

			Slice<StakeholdersStaff> tStakeholdersStaff = tStakeholdersStaffService.findAll(0, Integer.MAX_VALUE,
					titaVo);

			delStakeholdersStaff = tStakeholdersStaff == null ? null : tStakeholdersStaff.getContent();

			this.info("delStakeholdersStaff = " + delStakeholdersStaff);

			for (OccursList tempOccursList : occursList) {

				String id = tempOccursList.get("StaffId").toString();
				String name = tempOccursList.get("StaffName").toString();
				BigDecimal loanAmount = new BigDecimal(tempOccursList.get("LoanAmount"));

				StakeholdersStaff sStakeholdersStaff = new StakeholdersStaff();

				sStakeholdersStaff.setStaffId(id);
				sStakeholdersStaff.setStaffName(StringCut.stringMask(name));
				sStakeholdersStaff.setLoanAmount(loanAmount);
				instakeholdersStaff.add(sStakeholdersStaff);

				CountS++; // 成功筆數+1

			} // for

			try {

				if (delStakeholdersStaff != null) {
					this.info("1.delete old data");
					tStakeholdersStaffService.deleteAll(delStakeholdersStaff, titaVo);
				}
				this.info("2.insert new data");
				tStakeholdersStaffService.insertAll(instakeholdersStaff, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			changeDBEnv(titaVo);

			try {

				if (delStakeholdersStaff != null) {
					this.info("1.delete old data");
					tStakeholdersStaffService.deleteAll(delStakeholdersStaff, titaVo);
				}
				this.info("2.insert new data");
				tStakeholdersStaffService.insertAll(instakeholdersStaff, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 1:人壽利關人負責人名單[T07];xlsx xls
		} else if (iFunctionCode == 1) {

			List<LifeRelHead> inLifeRelHead = new ArrayList<LifeRelHead>();

			List<LifeRelHead> delLifeRelHead = new ArrayList<LifeRelHead>();

			Slice<LifeRelHead> tLifeRelHead = tLifeRelHeadService.findAcDate(acDate, 0, Integer.MAX_VALUE, titaVo);

			delLifeRelHead = tLifeRelHead == null ? null : tLifeRelHead.getContent();
			this.info("delLifeRelHead = " + delLifeRelHead);
			int cntTrans = 0;

			int cntInsert = 0;

			// 因筆數大量 所以逐批update
			for (OccursList tempOccursList : occursList) {

				String iRelWithCompany = tempOccursList.get("RelWithCompany").toString();
				String iHeadId = converntScientificNotation(tempOccursList.get("HeadId").toString());
				String iHeadName = tempOccursList.get("HeadName").toString();
				String iHeadTitle = tempOccursList.get("HeadTitle").toString();
				String iRelId = converntScientificNotation(tempOccursList.get("RelId").toString());
				String iRelName = tempOccursList.get("RelName").toString();
				String iRelKinShip = tempOccursList.get("RelKinShip").toString();
				String iRelTitle = tempOccursList.get("RelTitle").toString();
				String iBusId = converntScientificNotation(tempOccursList.get("BusId").toString());
				String iBusName = tempOccursList.get("BusName").toString();
				BigDecimal iShareHoldingRatio = new BigDecimal(tempOccursList.get("ShareHoldingRatio"));
				String iBusTitle = tempOccursList.get("BusTitle").toString();
				BigDecimal iLineAmt = new BigDecimal(tempOccursList.get("LineAmt"));
				BigDecimal iLoanBalance = new BigDecimal(tempOccursList.get("LoanBalance"));

				LifeRelHeadId sLifeRelHeadId = new LifeRelHeadId();
				sLifeRelHeadId.setHeadId(iHeadId);
				sLifeRelHeadId.setRelId(iRelId);
				sLifeRelHeadId.setBusId(iBusId);
				sLifeRelHeadId.setAcDate(acDate);

				LifeRelHead sLifeRelHead = new LifeRelHead();
				sLifeRelHead.setLifeRelHeadId(sLifeRelHeadId);
				sLifeRelHead.setRelWithCompany(iRelWithCompany);
				sLifeRelHead.setHeadName(StringCut.stringMask(iHeadName));
				sLifeRelHead.setHeadTitle(iHeadTitle);
				sLifeRelHead.setRelName(StringCut.stringMask(iRelName));
				sLifeRelHead.setRelKinShip(iRelKinShip);
				sLifeRelHead.setRelTitle(iRelTitle);
				sLifeRelHead.setBusName(StringCut.stringMask(iBusName));
				sLifeRelHead.setShareHoldingRatio(iShareHoldingRatio.intValue());
				sLifeRelHead.setBusTitle(iBusTitle);
				sLifeRelHead.setLineAmt(iLineAmt);
				sLifeRelHead.setLoanBalance(iLoanBalance);

				inLifeRelHead.add(sLifeRelHead);

				CountS++; // 成功筆數+1
				cntTrans++;

				// 每1000筆，新增一次
				if (cntTrans > 1000) {

					changeDBEnv(titaVo);

					try {

						if (delLifeRelHead != null && cntInsert == 0) {

							this.info("1.delete old data");
							tLifeRelHeadService.deleteAll(delLifeRelHead, titaVo);
						}

						this.info("2.insert new data");
						tLifeRelHeadService.insertAll(inLifeRelHead, titaVo);

					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}

					changeDBEnv(titaVo);

					try {

						if (delLifeRelHead != null && cntInsert == 0) {
							this.info("1.delete old data");
							tLifeRelHeadService.deleteAll(delLifeRelHead, titaVo);
						}

						this.info("2.insert new data");
						tLifeRelHeadService.insertAll(inLifeRelHead, titaVo);

					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}

					cntTrans = 0;
					cntInsert++;
					inLifeRelHead = new ArrayList<LifeRelHead>();

				}
			} // for

			changeDBEnv(titaVo);

			try {

				if (delLifeRelHead != null && cntInsert == 0) {
					this.info("1.delete old data");
					tLifeRelHeadService.deleteAll(delLifeRelHead, titaVo);
				}

				this.info("2.insert new data");
				tLifeRelHeadService.insertAll(inLifeRelHead, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			changeDBEnv(titaVo);

			try {

				if (delLifeRelHead != null && cntInsert == 0) {
					this.info("1.delete old data");
					tLifeRelHeadService.deleteAll(delLifeRelHead, titaVo);
				}
				this.info("2.insert new data");
				tLifeRelHeadService.insertAll(inLifeRelHead, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 2:人壽利關人職員名單[T07_2];xlsx xls
		} else if (iFunctionCode == 2) {

			List<LifeRelEmp> inLifeRelEmp = new ArrayList<LifeRelEmp>();

			List<LifeRelEmp> delLifeRelEmp = new ArrayList<LifeRelEmp>();

			Slice<LifeRelEmp> tLifeRelEmp = tLifeRelEmpService.findAcDate(acDate, 0, Integer.MAX_VALUE, titaVo);

			delLifeRelEmp = tLifeRelEmp == null ? null : tLifeRelEmp.getContent();
			this.info("delLifeRelEmp = " + delLifeRelEmp);

			for (OccursList tempOccursList : occursList) {

				String iEmpId = converntScientificNotation(tempOccursList.get("EmpId").toString());
				String iEmpName = tempOccursList.get("EmpName").toString();
				BigDecimal iLoanBalance = new BigDecimal(tempOccursList.get("LoanBalance"));

				LifeRelEmpId sLifeRelEmpId = new LifeRelEmpId();
				sLifeRelEmpId.setAcDate(acDate);
				sLifeRelEmpId.setEmpId(iEmpId);

				LifeRelEmp sLifeRelEmp = new LifeRelEmp();
				sLifeRelEmp.setLifeRelEmpId(sLifeRelEmpId);
				sLifeRelEmp.setEmpName(StringCut.stringMask(iEmpName));
				sLifeRelEmp.setLoanBalance(iLoanBalance);

				inLifeRelEmp.add(sLifeRelEmp);

				CountS++; // 成功筆數+1

			} // for

			this.info("inLifeRelEmp =" + inLifeRelEmp.toString());
			try {

				if (delLifeRelEmp != null) {
					this.info("1.delete old data");
					tLifeRelEmpService.deleteAll(delLifeRelEmp, titaVo);
				}
				this.info("2.insert new data");
				tLifeRelEmpService.insertAll(inLifeRelEmp, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			changeDBEnv(titaVo);

			try {

				if (delLifeRelEmp != null) {
					this.info("1.delete old data");
					tLifeRelEmpService.deleteAll(delLifeRelEmp, titaVo);
				}
				this.info("2.insert new data");
				tLifeRelEmpService.insertAll(inLifeRelEmp, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 3:金控利關人名單[T044];xlsx xls
		} else if (iFunctionCode == 3) {

			List<FinHoldRel> inFinHoldRel = new ArrayList<FinHoldRel>();

			List<FinHoldRel> delFinHoldRel = new ArrayList<FinHoldRel>();

			Slice<FinHoldRel> tFinHoldRel = tFinHoldRelService.findAcDate(acDate, 0, Integer.MAX_VALUE, titaVo);

			delFinHoldRel = tFinHoldRel == null ? null : tFinHoldRel.getContent();
			this.info("delFinHoldRel = " + delFinHoldRel);

			for (OccursList tempOccursList : occursList) {

				String iCompanyName = tempOccursList.get("CompanyName").toString();
				String iId = converntScientificNotation(tempOccursList.get("Id").toString());
				String iName = tempOccursList.get("Name").toString();
				String iBusTitle = tempOccursList.get("BusTitle").toString();
				BigDecimal iLineAmt = new BigDecimal(tempOccursList.get("LineAmt"));
				BigDecimal iLoanBalance = new BigDecimal(tempOccursList.get("LoanBalance"));

				FinHoldRelId sFinHoldRelId = new FinHoldRelId();
				sFinHoldRelId.setId(iId);
				sFinHoldRelId.setAcDate(acDate);
				sFinHoldRelId.setCompanyName(StringCut.stringMask(iCompanyName));

				FinHoldRel sFinHoldRel = new FinHoldRel();
				sFinHoldRel.setFinHoldRelId(sFinHoldRelId);
				sFinHoldRel.setName(StringCut.stringMask(iName));
				sFinHoldRel.setBusTitle(iBusTitle);
				sFinHoldRel.setLineAmt(iLineAmt);
				sFinHoldRel.setLoanBalance(iLoanBalance);

				inFinHoldRel.add(sFinHoldRel);

				CountS++; // 成功筆數+1

			} // for

			this.info("inFinHoldRel =" + inFinHoldRel.toString());

			try {

				if (delFinHoldRel != null) {
					this.info("1.delete old data");
					tFinHoldRelService.deleteAll(delFinHoldRel, titaVo);
				}
				this.info("2.insert new data");
				tFinHoldRelService.insertAll(inFinHoldRel, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			changeDBEnv(titaVo);
			try {

				if (delFinHoldRel != null) {
					this.info("1.delete old data");
					tFinHoldRelService.deleteAll(delFinHoldRel, titaVo);
				}

				this.info("2.insert new data");
				tFinHoldRelService.insertAll(inFinHoldRel, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

		}

		this.totaVo.putParam("CountAll", CountAll);
		this.totaVo.putParam("Count", CountS);
	}

	/**
	 * 處理CSV檔案資料
	 * 
	 * @param lineList 資料串
	 * @param repoNo   報表代號<br>
	 *                 0:人壽利關人職員名單[LA$RLTP]<br>
	 *                 1:人壽利關人負責人名單[T07]<br>
	 *                 2:人壽負利關人職員名單[T07_2]<br>
	 *                 3:金控利關人名單[T044]<br>
	 */
	public void setValueFromFile(ArrayList<String> lineList, int repoNo) {
		int row = 0;
		// repno=2 判斷欄位用
		int s = 0;
		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			row++;

			if (row == 1) {
				continue;
			}

			this.info(repoNo + "," + row + ".thisLine = " + thisLine);
			// 明細
			OccursList occursList = new OccursList();

			String[] thisColumn = thisLine.split(",");

			if (thisColumn.length >= 1 && thisColumn != null) {
				// 0:人壽利關人職員名單[LA$RLTP]
				if (repoNo == 0) {

					occursList.putParam("StaffId", thisColumn[0]);
					occursList.putParam("StaffName", thisColumn[1]);
					String tmpLoanAmount = thisColumn[2].length() == 0 ? "0" : thisColumn[2];
					occursList.putParam("LoanAmount", tmpLoanAmount);

					// 1:人壽利關人負責人名單[T07]
				} else if (repoNo == 1) {
					int col = 1;
					occursList.putParam("RelWithCompany", thisColumn[col + 0]);
					occursList.putParam("HeadId", thisColumn[col + 1]);
					occursList.putParam("HeadName", thisColumn[col + 2]);
					occursList.putParam("HeadTitle", thisColumn[col + 3]);
					occursList.putParam("RelId", thisColumn[col + 4]);
					occursList.putParam("RelName", thisColumn[col + 5]);
					occursList.putParam("RelKinShip", thisColumn[col + 6]);
					occursList.putParam("RelTitle", thisColumn[col + 7]);
					occursList.putParam("BusId", thisColumn[col + 8]);
					occursList.putParam("BusName", thisColumn[col + 9]);
					String tmpShareHoldingRatio = thisColumn[col + 10].length() == 0 ? "0" : thisColumn[col + 10];
					occursList.putParam("ShareHoldingRatio", tmpShareHoldingRatio);
					occursList.putParam("BusTitle", thisColumn[col + 11]);
					occursList.putParam("LineAmt", thisColumn[col + 12]);
					String tmpLoanBalance = thisColumn[col + 13].length() == 0 ? "0" : thisColumn[col + 13];
					occursList.putParam("LoanBalance", tmpLoanBalance);

					// 2:人壽負利關人職員名單[T07_2]
				} else if (repoNo == 2) {

					s = 2;
//					for (int i = 0; i < thisColumn.length; i++) {
//						if ((thisColumn[i].length() == 10 || thisColumn[i].length() == 8) && row == 2) {
//							s = i;
//							this.info("start col =" + s);
//							break;
//						}
//					}

					occursList.putParam("EmpId", thisColumn[s + 0]);
					occursList.putParam("EmpName", thisColumn[s + 1]);
					String tmpLoanBalance = thisColumn[s + 12].length() == 0 ? "0" : thisColumn[s + 12];
					occursList.putParam("LoanBalance", tmpLoanBalance);

					// 3:金控利關人名單[T044]
				} else if (repoNo == 3) {

					occursList.putParam("CompanyName", thisColumn[0]);
					occursList.putParam("Id", thisColumn[1]);
					occursList.putParam("Name", thisColumn[2]);
					occursList.putParam("BusTitle", thisColumn[3]);
					occursList.putParam("LineAmt", thisColumn[4]);
					String tmpLoanBalance = thisColumn[5].length() == 0 ? "0" : thisColumn[5];
					occursList.putParam("LoanBalance", tmpLoanBalance);

				}

			}
			this.occursList.add(occursList);

		}

	}

	/**
	 * 處理xlsx,xls檔案資料
	 * 
	 * @param titaVo
	 * @param repoNo 報表代號<br>
	 *               0:人壽利關人職員名單[LA$RLTP]<br>
	 *               1:人壽利關人負責人名單[T07]<br>
	 *               2:人壽負利關人職員名單[T07_2]<br>
	 *               3:金控利關人名單[T044]<br>
	 * @throws LogicException
	 */
	public void setValueFromFileExcel(TitaVo titaVo, int repoNo) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		// 0:人壽利關人職員名單[LA$RLTP]
		if (repoNo == 0) {
			String id = "";
			String name = "";
			BigDecimal loanAmt = BigDecimal.ZERO;

			for (int i = 2; i <= lastRowNum; i++) {

				OccursList occursList = new OccursList();

				// 0:人壽利關人職員名單[LA$RLTP]

				// 連續資料串，遇到空值強行結束
				if (makeExcel.getValue(i, 1) == null || makeExcel.getValue(i, 2) == null
						|| makeExcel.getValue(i, 3) == null) {
					break;
				}

				try {

					id = makeExcel.getValue(i, 1).toString().trim();
					Object xid = makeExcel.getValue(i, 1).toString().trim().length() > 10
							? new BigDecimal(makeExcel.getValue(i, 1).toString().trim()).toPlainString()
							: id;

					id = xid.toString();
					name = makeExcel.getValue(i, 2).toString().trim();
					loanAmt = new BigDecimal(makeExcel.getValue(i, 3).toString());
					this.info("id =" + id.length() + ":" + id);

				} catch (Exception e) {

					String ErrorMsg = "L7206(Excel欄位應為關係人代號在A欄、關係人名稱在B欄、放款金額為C欄)，請確認";
					throw new LogicException(titaVo, "E0015", ErrorMsg);

				}

				occursList.putParam("StaffId", id);
				occursList.putParam("StaffName", name);
				occursList.putParam("LoanAmount", loanAmt.intValue());

				this.occursList.add(occursList);

			}

			// 1:人壽利關人負責人名單[T07]
		} else if (repoNo == 1) {
			/*
			 * RelWithCompany HeadId HeadName HeadTitle RelId RelName RelKinShip RelTitle
			 * BusId BusName ShareHoldingRatio BusTitle LineAmt LoanBalance
			 */
			//
			String iRelWithCompany = "";
			String iHeadId = "";
			String iHeadName = "";
			String iHeadTitle = "";
			String iRelId = "";
			String iRelName = "";
			String iRelKinShip = "";
			String iRelTitle = "";
			String iBusId = "";
			String iBusName = "";
			BigDecimal iShareHoldingRatio = BigDecimal.ZERO;
			String iBusTitle = "";
			BigDecimal iLineAmt = BigDecimal.ZERO;
			BigDecimal iLoanBalance = BigDecimal.ZERO;

			for (int i = 2; i <= lastRowNum; i++) {

				OccursList occursList = new OccursList();

				iRelWithCompany = makeExcel.getValue(i, 2).toString().trim();
				iHeadId = makeExcel.getValue(i, 3).toString().trim();
				iHeadName = makeExcel.getValue(i, 4).toString().trim();
				iHeadTitle = makeExcel.getValue(i, 5).toString().trim();
				iRelId = makeExcel.getValue(i, 6).toString().trim();
				iRelName = makeExcel.getValue(i, 7).toString().trim();
				iRelKinShip = makeExcel.getValue(i, 8).toString().trim();
				iRelTitle = makeExcel.getValue(i, 9).toString().trim();
				iBusId = makeExcel.getValue(i, 10).toString().trim();
				iBusName = makeExcel.getValue(i, 11).toString().trim();
				iShareHoldingRatio = new BigDecimal(makeExcel.getValue(i, 12).toString());
				iBusTitle = makeExcel.getValue(i, 13).toString().trim();
				iLineAmt = new BigDecimal(makeExcel.getValue(i, 14).toString());
				iLoanBalance = new BigDecimal(makeExcel.getValue(i, 15).toString());

				occursList.putParam("RelWithCompany", iRelWithCompany);
				occursList.putParam("HeadId", iHeadId);
				occursList.putParam("HeadName", iHeadName);
				occursList.putParam("HeadTitle", iHeadTitle);
				occursList.putParam("RelId", iRelId);
				occursList.putParam("RelName", iRelName);
				occursList.putParam("RelKinShip", iRelKinShip);
				occursList.putParam("RelTitle", iRelTitle);
				occursList.putParam("BusId", iBusId);
				occursList.putParam("BusName", iBusName);
				occursList.putParam("ShareHoldingRatio", iShareHoldingRatio);
				occursList.putParam("BusTitle", iBusTitle);
				occursList.putParam("LineAmt", iLineAmt);
				occursList.putParam("LoanBalance", iLoanBalance);

				this.occursList.add(occursList);
			}

			// 2:人壽負利關人職員名單[T07_2]
		} else if (repoNo == 2) {
			/*
			 * EmpId EmpName LoanBalance
			 * 
			 */
			String iEmpId = "";
			String iEmpName = "";
			BigDecimal iLoanBalance = BigDecimal.ZERO;

			for (int i = 2; i <= lastRowNum; i++) {

				OccursList occursList = new OccursList();

//				// 連續資料串，遇到空值強行結束
//				if (makeExcel.getValue(i, 1) == null || makeExcel.getValue(i, 2) == null
//						|| makeExcel.getValue(i, 3) == null) {
//					break;
//				}

				try {

					iEmpId = makeExcel.getValue(i, 3).toString().trim();
					iEmpName = makeExcel.getValue(i, 4).toString().trim();
					iLoanBalance = new BigDecimal(makeExcel.getValue(i, 15).toString());

				} catch (Exception e) {

					String ErrorMsg = "L7206(Excel欄位讀取有誤，請確認";
					throw new LogicException(titaVo, "E0015", ErrorMsg);

				}
				occursList.putParam("EmpId", iEmpId);
				occursList.putParam("EmpName", iEmpName);
				occursList.putParam("LoanBalance", iLoanBalance);

				this.occursList.add(occursList);

			}

			// 3:金控利關人名單[T044]
		} else if (repoNo == 3) {
			/*
			 * CompanyName Id Name BusTitle LineAmt LoanBalance
			 * 
			 */

			String iCompanyName = "";
			String iId = "";
			String iName = "";
			String iBusTitle = "";
			BigDecimal iLineAmt = BigDecimal.ZERO;
			BigDecimal iLoanBalance = BigDecimal.ZERO;

			for (int i = 2; i <= lastRowNum; i++) {

				OccursList occursList = new OccursList();

//				// 連續資料串，遇到空值強行結束
//				if (makeExcel.getValue(i, 1) == null || makeExcel.getValue(i, 2) == null
//						|| makeExcel.getValue(i, 3) == null) {
//					break;
//				}

				try {

					iCompanyName = makeExcel.getValue(i, 1).toString().trim();
					iId = makeExcel.getValue(i, 2).toString().trim();
					iName = makeExcel.getValue(i, 3).toString().trim();
					iBusTitle = makeExcel.getValue(i, 4).toString().trim();
					iLineAmt = new BigDecimal(makeExcel.getValue(i, 5).toString());
					iLoanBalance = new BigDecimal(makeExcel.getValue(i, 6).toString());

				} catch (Exception e) {

					String ErrorMsg = "L7206(Excel欄位讀取有誤，請確認";
					throw new LogicException(titaVo, "E0015", ErrorMsg);

				}

				occursList.putParam("CompanyName", iCompanyName);
				occursList.putParam("Id", iId);
				occursList.putParam("Name", iName);
				occursList.putParam("BusTitle", iBusTitle);
				occursList.putParam("LineAmt", iLineAmt);
				occursList.putParam("LoanBalance", iLoanBalance);

				this.occursList.add(occursList);

			}
		}

	}

	private String converntScientificNotation(String text) {
		String resText = "";
		if (text.contains("E") && text.length() != 10) {
			BigDecimal decimalFormat = new BigDecimal(text);
			resText = decimalFormat.toPlainString();
		} else if (text.contains("-") || text.length() == 0) {
			resText = "-";
		} else {
			resText = text;
		}

		return resText;
	}

	/**
	 * 更新MonthlyFacBal.BankRelationFlag 利害關係人欄位
	 */
	private void updL7UploadData(TitaVo titaVo) throws LogicException {
		this.info("upd updL7UploadData start.");

		// 2023-09-12 更新MonthlyFacBal BankRelationFlag

		int iYYMM = inputAcDate / 100;
		String tlrno = titaVo.getTlrNo();
		String txcd = titaVo.getTxcd();
		this.info("txcd =" + txcd);
		changeDBEnv(titaVo);

		tMonthlyFacBalService.Usp_L7_UploadToMonthlyFacBal_Upd(iYYMM, txcd, tlrno, "", titaVo);

		changeDBEnv(titaVo);

		tMonthlyFacBalService.Usp_L7_UploadToMonthlyFacBal_Upd(iYYMM, txcd, tlrno, "", titaVo);
		this.info("upd updL7UploadData end.");
	}

	/**
	 * 切換環境
	 */
	private void changeDBEnv(TitaVo titaVo) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ("onLine".equals(titaVo.getDataBase())) {
			titaVo.setDataBaseOnMon();// 指定月報環境
		} else {
			titaVo.setDataBaseOnLine();// 指定連線環境
		}

	}

	/**
	 * 檢查LifeRelHead和LifeRelEmp 是否在同一天有資料，有的話才同步更新MonthlyFacBal
	 * 
	 * @throws LogicException
	 */
	private void isLatestData(TitaVo titaVo) throws LogicException {

		// 判斷LifeRelHead和LifeRelEmp 是否在同一天有資料，有的話才更新
		Slice<LifeRelHead> head = tLifeRelHeadService.findAcDate(inputAcDate, 0, Integer.MAX_VALUE, titaVo);
		int lifeRelHeadSize = head == null ? 0 : head.getContent().size();

		if (lifeRelHeadSize == 0) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "",
					titaVo.getParam("TLRNO"), "請同步上傳利關人負責人檔[T07]，以利更新每月額度檔", titaVo);
		}

		Slice<LifeRelEmp> emp = tLifeRelEmpService.findAcDate(inputAcDate, 0, Integer.MAX_VALUE, titaVo);
		int lifeRelEmpSize = emp == null ? 0 : emp.getContent().size();
		if (lifeRelEmpSize == 0) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "",
					titaVo.getParam("TLRNO"), "請同步上傳利關人職員檔[T07_2]，以利更新每月額度檔", titaVo);
		}

		if (lifeRelEmpSize > 0 && lifeRelHeadSize > 0) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "",
					titaVo.getParam("TLRNO"), "每月額度檔同步更新中", titaVo);
			titaVo.keepOrgDataBase();
			updL7UploadData(titaVo);
			titaVo.setDataBaseOnOrg();
		}

	}
}