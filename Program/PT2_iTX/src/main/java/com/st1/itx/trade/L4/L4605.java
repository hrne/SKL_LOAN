package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Service("L4605")
@Scope("prototype")
public class L4605 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public CollListService collListService;

	private int iInsuEndMonth = 0;
	private int cnt = 0;

	

	@Value("${iTXInFolder}")
	private String inFolder = "";

	HashMap<tmpInsu, String> alrdyInsu = new HashMap<>();
	HashMap<tmpInsu, Integer> error = new HashMap<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4605 ");
		this.totaVo.init(titaVo);
		HashMap<tmpInsu, Integer> error = new HashMap<>();
		HashMap<tmpInsu, String> alrdyInsu = new HashMap<>();
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

//		吃檔
//		String filePath1 = "D:\\temp\\test\\火險\\Test\\Return\\201912_final.txt";
		String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

//		 編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filePath1, "big5");
		} catch (IOException e) {
			throw new LogicException("E0014", "L4605(" + filePath1 + ") " + e.getMessage());
		}

//		 使用資料容器內定義的方法切資料
		insuRenewFileVo.setValueFromFile(dataLineList);

		ArrayList<OccursList> uploadFile = insuRenewFileVo.getOccursList();

		if (uploadFile != null && uploadFile.size() != 0) {
			for (OccursList tempOccursList : uploadFile) {
				String insuNo = tempOccursList.get("InsuNo");
//				file檔為NewInusNo
				String newInsuNo = tempOccursList.get("NewInusNo");

				this.info("insuNo =" + insuNo);
				this.info("newInsuNo =" + newInsuNo);

				if (insuNo != null && !"".equals(insuNo)) {
					insuNo = insuNo.trim();
				}
				if (newInsuNo != null && !"".equals(newInsuNo)) {
					newInsuNo = newInsuNo.trim();
				}
				
				tmpInsu tmp = new tmpInsu(parse.stringToInteger(tempOccursList.get("CustNo")),
						parse.stringToInteger(tempOccursList.get("FacmNo")), insuNo, newInsuNo,tempOccursList.get("InsuCustId"));
//				Initial
				error.put(tmp, 0);

				if (iInsuEndMonth != parse.stringToInteger(tempOccursList.get("FireInsuMonth"))) {
					error.put(tmp, 1);
					continue;
				}

				InsuRenew tInsuRenew = insuRenewService.prevInsuNoFirst(
						parse.stringToInteger(tempOccursList.get("CustNo").trim()),
						parse.stringToInteger(tempOccursList.get("FacmNo").trim()), insuNo, titaVo);

				if (tInsuRenew == null) {
					error.put(tmp, 3);
					continue;
				}

				tInsuRenew = insuRenewService.holdById(tInsuRenew, titaVo);

				this.info("tInsuRenew : " + tInsuRenew.toString());
				if (!"".equals(tInsuRenew.getNowInsuNo().trim()) && !tInsuRenew.getNowInsuNo().equals(newInsuNo)) {
					error.put(tmp, 4);
					alrdyInsu.put(tmp, tInsuRenew.getNowInsuNo());
					continue;
				}
				if (tInsuRenew.getRenewCode() == 1) {
					error.put(tmp, 5);
					continue;
				}
				if (tInsuRenew.getTotInsuPrem().compareTo(BigDecimal.ZERO) == 0) {
					error.put(tmp, 6);
					continue;
				}
				BigDecimal totFee = tInsuRenew.getEthqInsuPrem().add(tInsuRenew.getFireInsuPrem());

				if (tInsuRenew.getTotInsuPrem().compareTo(totFee) != 0) {
					this.info("EthqInsuPrem : " + tInsuRenew.getEthqInsuPrem());
					this.info("FireInsuPrem : " + tInsuRenew.getFireInsuPrem());
					this.info("totFee : " + totFee);
					this.info("NewTotalFee : " + parse.stringToBigDecimal(tempOccursList.get("NewTotalFee")));
					error.put(tmp, 7);
					continue;
				}

				if (tInsuRenew.getInsuEndDate() + 19110000 != parse
						.stringToInteger(tempOccursList.get("NewInsuEndDate").replace("/", ""))) {
					this.info("InsuEndDate : " + tInsuRenew.getEthqInsuPrem() + 19110000);
					this.info("NewInsuEndDate : "
							+ parse.stringToInteger(tempOccursList.get("NewInsuEndDate").replace("/", "")));
					error.put(tmp, 10);
					continue;
				}
				if (tInsuRenew.getFireInsuCovrg()
						.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewFireInsuAmt"))) != 0
						|| tInsuRenew.getFireInsuPrem()
								.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewFireInsuFee"))) != 0) {
					error.put(tmp, 11);
					continue;
				}
				if (tInsuRenew.getEthqInsuCovrg()
						.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewEqInsuAmt"))) != 0
						|| tInsuRenew.getEthqInsuPrem()
								.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewEqInsuFee"))) != 0) {
					error.put(tmp, 12);
					continue;
				}
				if (tInsuRenew.getTotInsuPrem()
						.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewTotalFee"))) != 0) {
					error.put(tmp, 13);
					continue;
				}
				if (!"".equals(tInsuRenew.getNowInsuNo().trim()) && tInsuRenew.getNowInsuNo().equals(newInsuNo)) {
					error.put(tmp, 14);
					alrdyInsu.put(tmp, newInsuNo);
					continue;
				}

				tInsuRenew.setNowInsuNo(newInsuNo);

				try {
					insuRenewService.update(tInsuRenew);
				} catch (DBException e) {
					throw new LogicException("E0007", e.getErrorMsg());
				}
			}
			Set<tmpInsu> tempSet = error.keySet();

			List<tmpInsu> tempList = new ArrayList<>();

			for (Iterator<tmpInsu> it = tempSet.iterator(); it.hasNext();) {
				tmpInsu tmpInsuVo = it.next();
				tempList.add(tmpInsuVo);
			}

			this.info("L4605 tempList size = " + tempList.size());

			this.alrdyInsu = alrdyInsu;
			this.error = error;
			exportExcel(titaVo, tempList);// 產excel檔

			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), "L4605-火險最終保單上傳作業", titaVo);
		}

//		傳回var控制錯誤清單顯示與否
//		this.totaVo.putParam("OCnt", cnt);

		this.addList(this.totaVo);
		return this.sendList();
	}

//	暫時紀錄戶號額度
	private class tmpInsu {

		private int custNo = 0;
		private int facmNo = 0;
		private String pInsuNo = "";
//		private String aInsuNo = "";
		private String nInsuNo = "";
		private String insuCustId = "";
		public tmpInsu(int custNo, int facmNo, String pInsuNo, String nInsuNo,String insuCustId) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setpInsuNo(pInsuNo);
			this.setnInsuNo(nInsuNo);
			this.setInsuCustId(insuCustId);
		}

		public String getInsuCustId() {
			return insuCustId;
		}

		public void setInsuCustId(String insuCustId) {
			this.insuCustId = insuCustId;
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public int getFacmNo() {
			return facmNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		public String getpInsuNo() {
			return pInsuNo;
		}

		public void setpInsuNo(String pInsuNo) {
			this.pInsuNo = pInsuNo;
		}

		public String getnInsuNo() {
			return nInsuNo;
		}

		public void setnInsuNo(String nInsuNo) {
			this.nInsuNo = nInsuNo;
		}
	}

	private void exportExcel(TitaVo titaVo, List<tmpInsu> tempList) throws LogicException {
		this.info("L4605 exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4605";
		String fileItem = "火險最終保單";
		String fileName = "L4605-火險最終保單上傳作業";
		int row = 1; // 列數:記錄印到第幾列
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);

		// 調整欄寬
		makeExcel.setWidth(1, 10);
		makeExcel.setWidth(2, 10);
		makeExcel.setWidth(3, 10);
		makeExcel.setWidth(4, 28);
		makeExcel.setWidth(5, 12);
		makeExcel.setWidth(6, 12);
		makeExcel.setWidth(7, 24);
		makeExcel.setWidth(8, 24);

		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
		headerStyleVo.setBold(true);
		// 表頭

		makeExcel.setValue(row, 1, "戶號", "C", headerStyleVo);
		makeExcel.setValue(row, 2, "額度", "C", headerStyleVo);
		makeExcel.setValue(row, 3, "保單號碼", "C", headerStyleVo);
		makeExcel.setValue(row, 4, "戶名", "C", headerStyleVo);
		makeExcel.setValue(row, 5, "已有新保單號碼", "C", headerStyleVo);
		makeExcel.setValue(row, 6, "上傳新保單號碼", "C", headerStyleVo);
		makeExcel.setValue(row, 7, "戶況", "C", headerStyleVo);
		makeExcel.setValue(row, 8, "原因狀況", "C", headerStyleVo);

		row++;

		// 明細
		if (tempList == null || tempList.isEmpty()) {
			makeExcel.setValue(3, 1, "本日無資料", "L");
		} else {
			for (tmpInsu tempL4605Vo : tempList) {
//				OccursList occursList = new OccursList();

				this.info("L4605 errorCode = " + error.get(tempL4605Vo));

				if (error.get(tempL4605Vo) != 0) {

					cnt = cnt + 1;
					CustMain tCustMain = new CustMain();
					tCustMain = custMainService.custIdFirst(tempL4605Vo.getInsuCustId(), titaVo);
					int colStatus = 99;
					CollList tCollList = collListService
							.findById(new CollListId(tempL4605Vo.getCustNo(), tempL4605Vo.getFacmNo()), titaVo);
					if (tCollList != null) {
						colStatus = tCollList.getStatus();
					}
					if (colStatus == 4) {
						colStatus = 0;
					}

					CdCode tCdCode = cdCodeService
							.findById(new CdCodeId("ColStatus", parse.IntegerToString(colStatus, 2)), titaVo);
					String colStatusX = "";
					if (tCdCode != null) {
						colStatusX = tCdCode.getItem();
					}



					makeExcel.setValue(row, 1, tempL4605Vo.getCustNo());// 戶號
					makeExcel.setValue(row, 2, tempL4605Vo.getFacmNo());// 額度
					makeExcel.setValue(row, 3, tempL4605Vo.getpInsuNo());// 保單號碼
					makeExcel.setValue(row, 4, tCustMain == null ? "" : tCustMain.getCustName());// 戶名CustName

					if (error.get(tempL4605Vo) == 4 || error.get(tempL4605Vo) == 14) {
						makeExcel.setValue(row, 5, alrdyInsu.get(tempL4605Vo));
					} else {
						makeExcel.setValue(row, 5, "");
					}



					makeExcel.setValue(row, 6, tempL4605Vo.getnInsuNo());
					makeExcel.setValue(row, 7, colStatusX);// 戶況



					switch (error.get(tempL4605Vo)) {
					case 1:
						makeExcel.setValue(row, 8, "檔案與畫面年月不符");
						break;
					case 2:
						makeExcel.setValue(row, 8, "資料有誤，請人工檢核調整");
						break;
					case 3:
						makeExcel.setValue(row, 8, "不存在火險明細檔");
						break;
					case 4:
						makeExcel.setValue(row, 8, "與之前上的新保單號碼不同");
						break;
					case 5:
						makeExcel.setValue(row, 8, "此為不處理之保單資料");
						break;
					case 6:
						makeExcel.setValue(row, 8, "總保費=0");
						break;
					case 7:
						makeExcel.setValue(row, 8, "總保費不等於火險保費+地震險保費");
						break;
					case 8:
						makeExcel.setValue(row, 8, "火險保額與保費不符");
						break;
					case 9:
						makeExcel.setValue(row, 8, "地震險保額與保費不符");
						break;
					case 10:
						makeExcel.setValue(row, 8, "上傳之保險起迄日與檔案資料不符");
						break;
					case 11:
						makeExcel.setValue(row, 8, "上傳之火險保額或保費與檔案資料不符");
						break;
					case 12:
						makeExcel.setValue(row, 8, "上傳地震險保額或保費與檔案資料不符");
						break;
					case 13:
						makeExcel.setValue(row, 8, "上傳總保費與檔案資料不符");
						break;
					case 14:
						makeExcel.setValue(row, 8, "保單資料中已有新保單之保單號碼");
						break;
					default:
						makeExcel.setValue(row, 8, "ErrorMsg");
						break;
					}

//					this.totaVo.addOccursList(occursList);
					row++;

				} else {
					continue;
				}
			}
		}

		makeExcel.close();

	}
}