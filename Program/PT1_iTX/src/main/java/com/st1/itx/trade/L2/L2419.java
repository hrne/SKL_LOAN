package com.st1.itx.trade.L2;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdClBatch;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.domain.ClBatch;
import com.st1.itx.db.domain.ClBatchId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClBatchService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClBatchService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.parse.ZLibUtils;

@Service("L2419")
@Scope("prototype")
/**
 * 不動產擔保品資料整批匯入 2022-10-21 Wei 修改: 實際寫入擔保品資料時,放到背景執行
 * 
 * @author Eric
 * @version 1.0.0
 */
public class L2419 extends TradeBuffer {
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Autowired
	private DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	private Parse parse;

	@Autowired
	private ZLibUtils zLibUtils;

	@Autowired
	private MakeExcel makeExcel;

	@Autowired
	private WebClient webClient;

	/* 自動取號 */
	@Autowired
	private GSeqCom gSeqCom;

	@Autowired
	private CdCodeService sCdCodeService;
	@Autowired
	private CustMainService sCustMainService;
	@Autowired
	private CdCityService sCdCityService;
	@Autowired
	private CdAreaService sCdAreaService;
	@Autowired
	private TxFileService sTxFileService;
	@Autowired
	private CdLandSectionService sCdLandSectionService;
	@Autowired
	private CdClBatchService sCdClBatchService;
	@Autowired
	private ClBatchService sClBatchService;

	private HashMap<String, String> items = new HashMap<String, String>();

	private List<Map<String, String>> lastOwnerList = null;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2419 ");
		this.totaVo.init(titaVo);

		String function = titaVo.getParam("Function");

		switch (function) {
		case "1":
			check(titaVo); // 全部檢核通過之前，不取號，故不產生回饋檔
			feedback(titaVo); // 全部檢核通過，才取號並產生回饋檔
			break;
		case "2":
			modifyClData(titaVo); // 寫入或修改整批匯入的擔保品資料
			break;
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 產生回饋檔
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException LogicException
	 */
	private void feedback(TitaVo titaVo) throws LogicException {
		// 核准號碼
		int applNo = Integer.parseInt(titaVo.getParam("ApplNo"));
		// 鑑價公司代碼
		String evaCompany = titaVo.getParam("EvaCompany");
		// 鑑價日期
		int evaDate = Integer.parseInt(titaVo.getParam("EvaDate")) + 19110000;
		// 批號
		String groupNo = getGroupNo(applNo, titaVo);

		Slice<ClBatch> slClBatch = sClBatchService.findGroupNo(groupNo, 0, Integer.MAX_VALUE, titaVo);
		if (slClBatch != null && !slClBatch.isEmpty()) {
			// 此批號已存在,通知錯誤
			throw new LogicException("E0005", "批號已存在,擔保品整批匯入檔(ClBatch),批號(" + groupNo + ")");
		}

		int lastRowNum = makeExcel.getSheetLastRowNum();

		this.info("lastRowNum=" + lastRowNum);

		ClBatch tClBatch;
		ClBatchId tClBatchId;

		int row = 3;
		for (; row <= lastRowNum + 1; row++) {
			this.info("row = " + row);

			// 編號
			String columnA = makeExcel.getValue(row, 1).toString(); // NO
			int no = (int) toNumeric(columnA);

			if (no == 0) {
				break;
			}

			// 擔保品代號1
			String columnB = makeExcel.getValue(row, 2).toString();
			columnB = columnB.substring(0, columnB.indexOf("_"));
			this.info("columnB = " + columnB);
			int clCode1 = Integer.parseInt(columnB);

			// 擔保品代號2
			String columnC = makeExcel.getValue(row, 3).toString();
			columnC = columnC.substring(0, columnC.indexOf("_"));
			this.info("columnC = " + columnC);
			int clCode2 = Integer.parseInt(columnC);

			// 擔保品序號
			int clNo = this.getClNo(clCode1, clCode2, titaVo);

			makeExcel.setValueInt(row, 4, clNo);

			tClBatchId = new ClBatchId();
			tClBatchId.setGroupNo(groupNo);
			tClBatchId.setSeq(no);

			tClBatch = new ClBatch();
			tClBatch.setClBatchId(tClBatchId);
			tClBatch.setApplNo(applNo);
			tClBatch.setEvaCompany(evaCompany);
			tClBatch.setEvaDate(evaDate);
			tClBatch.setClCode1(clCode1);
			tClBatch.setClCode2(clCode2);
			tClBatch.setClNo(clNo);
			tClBatch.setInsertStatus(0);

			try {
				sClBatchService.insert(tClBatch, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品整批匯入檔(ClBatch),上傳檔編號(" + no + ")");
			}
		}

		makeExcel.lockColumn(3, row - 1, 1, 4, 140);

		makeExcel.protectSheet(groupNo);

		String newFileItem = "擔保品明細表回饋檔_" + groupNo + ".xlsx";
		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + newFileItem;
		makeExcel.saveExcel(fileName);
		toTxFile(titaVo, fileName, newFileItem);
		String msg = "擔保品整批匯入，資料已檢核通過，請至【報表及製檔】下傳回饋檔";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				String.format("%-8s", titaVo.getTlrNo().trim()) + "L2419", msg, titaVo);
	}

	/**
	 * 檢核上傳檔
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException LogicException
	 */
	private void check(TitaVo titaVo) throws LogicException {
		this.info("checkAndFeedback ");

		String custId = titaVo.getParam("CustId");

		String fileItem = titaVo.getParam("FileItem").trim();

		if (fileItem.isEmpty()) {
			throw new LogicException("E0015", "請先設定擔保品明細表");
		}

		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + fileItem;

		this.info("fileitem=" + fileItem);
		makeExcel.openExcel(fileName, "擔保品明細表");

		CustMain custMain = sCustMainService.custIdFirst(custId, titaVo);
		if (custMain == null) {
			throw new LogicException("E0001", "客戶統編 = " + custId);
		}

		int lastRowNum = makeExcel.getSheetLastRowNum();

		this.info("lastRowNum=" + lastRowNum);

		lastOwnerList = new ArrayList<>();

		for (int i = 0; i <= lastRowNum; i++) {

			int row = 3 + i;

			this.info("row = " + row);

			// 編號
			String columnA = makeExcel.getValue(row, 1).toString(); // NO
			int no = (int) toNumeric(columnA);
			this.info("columnA = " + columnA);

			if (no == 0) {
				break;
			}

			// 擔保品代號1
			String columnB = makeExcel.getValue(row, 2).toString();
			columnB = columnB.substring(0, columnB.indexOf("_"));
			this.info("columnB = " + columnB);
			int clCode1 = (int) toNumeric(columnB);
			if (clCode1 != 1 && clCode1 != 2) {
				throwError("B ,行數:" + row, "擔保品代號1應為1或2", columnB);
			}

			// 擔保品代號2
			String columnC = makeExcel.getValue(row, 3).toString();
			columnC = columnC.substring(0, columnC.indexOf("_"));
			this.info("columnC = " + columnC);
			String clCode2 = columnC;
			if (clCode1 == 1) {
				checkCode(titaVo, "ClCode21", "" + clCode2, "C ,行數:" + row, "擔保品代號2", columnC);
			} else if (clCode1 == 2) {
				checkCode(titaVo, "ClCode22", "" + clCode2, "C ,行數:" + row, "擔保品代號2", columnC);
			} else {
				// unexpected ClCode1
			}

			// 擔保品編號
			String columnD = makeExcel.getValue(row, 4).toString();
			this.info("columnD = " + columnD);
			int clNo = (int) toNumeric(columnD);

			if (!columnD.isEmpty() && clNo != 0) {
				throw new LogicException("E0015", "欄位:D ,行數:" + row + ",擔保品號碼不可輸入");
			}

			// 擔保品類別代碼
			String columnE = makeExcel.getValue(row, 5).toString();
			columnE = columnE.substring(0, columnE.indexOf("_"));
			this.info("columnE = " + columnE);
			String typeCode = columnE;
			if (clCode1 == 1) {
				checkCode(titaVo, "ClTypeCode21", typeCode, "E ,行數:" + row, "擔保品類別", columnE);
			} else {
				checkCode(titaVo, "ClTypeCode22", typeCode, "E ,行數:" + row, "擔保品類別", columnE);
			}

			if (clCode1 == 1) {
				// 建號-前5碼
				String columnF = makeExcel.getValue(row, 6).toString();
				this.info("columnF = " + columnF);
				int bdno1 = (int) toNumeric(columnF);
				if (bdno1 == 0) {
					throw new LogicException("E0015", "欄位:F ,行數:" + row + ",房地擔保品必須輸入建號前5碼，且建號前5碼不得為0或空白 = " + columnF);
				}
//				String buildNo1 = leftPadZero(columnF, 5);

				// 建號-後3碼
				String columnG = makeExcel.getValue(row, 7).toString();
				this.info("columnG = " + columnG);
//				String buildNo2 = leftPadZero(columnG, 3);
			}

			// 地號-前4碼
			String columnH = makeExcel.getValue(row, 8).toString();
			this.info("columnH = " + columnH);
			// 地號-後4碼
			int ldno1 = (int) toNumeric(columnH);
			if (ldno1 == 0) {
				throw new LogicException("E0015", "欄位:H ,行數:" + row + ",必須輸入地號前4碼且不得為0 = " + columnH);
			}
//			String landNo1 = leftPadZero(columnH, 4);

			String columnI = makeExcel.getValue(row, 9).toString();
			this.info("columnI = " + columnI);
//			String landNo2 = leftPadZero(columnI, 4);

			// 郵遞區號
			String columnJ = makeExcel.getValue(row, 10).toString();
			this.info("columnJ = " + columnJ);
			String zip3 = columnJ;
			String cityCode = getItem("zipcity=" + zip3);
			String areaCode = "";

			if (cityCode.isEmpty()) {

				CdArea cdArea = sCdAreaService.Zip3First(zip3, titaVo);

				if (cdArea == null) {
					throw new LogicException("E0015", "欄位:J ,行數:" + row + ",郵遞區號無法配對到鄉鎮市區代碼,郵遞區號=" + columnJ);
				}

				CdCity cdCity = sCdCityService.findById(cdArea.getCityCode(), titaVo);
				if (cdCity == null) {
					throw new LogicException("E0015",
							"欄位:J ,行數:" + row + ",郵遞區號對應之縣市別代碼無法配對到正確的縣市代碼檔,縣市別代碼=" + cdArea.getCityCode());
				}

				items.put("zipcity=" + zip3, cdArea.getCityCode());
				items.put("city=" + cdArea.getCityCode(), cdCity.getCityItem());
				items.put("ziparea=" + zip3, cdArea.getAreaCode());
				items.put("area=" + cdArea.getCityCode() + "-" + cdArea.getAreaCode(), cdArea.getAreaItem());

				// 縣市
				cityCode = cdArea.getCityCode();
				// 鄉鎮市區
				areaCode = cdArea.getAreaCode();
			} else {
				// 縣市
				// 鄉鎮市區
				areaCode = getItem("ziparea=" + zip3);
			}

			// 段小段代碼
			String columnK = makeExcel.getValue(row, 11).toString();
			this.info("columnK = " + columnK);
			String irCode = leftPadZero(columnK, 4);

			this.info("CdLandSection = " + cityCode + "-" + areaCode + "-" + irCode);
			CdLandSectionId cdLandSectionId = new CdLandSectionId();
			cdLandSectionId.setCityCode(cityCode);
			cdLandSectionId.setAreaCode(areaCode);
			cdLandSectionId.setIrCode(irCode);

			CdLandSection cdLandSection = sCdLandSectionService.findById(cdLandSectionId, titaVo);
			if (cdLandSection == null) {
				throw new LogicException("E0015", "欄位:K ,行數:" + row + ",此地段4碼在地段代碼檔查無資料,地段4碼= " + columnK);
			}

			if (clCode1 == 1) {
				// 門牌
				String columnL = makeExcel.getValue(row, 12).toString();
				this.info("columnL = " + columnL);
				String road = columnL.trim();

				if (road.isEmpty()) {
					throw new LogicException("E0015", "欄位:L ,行數:" + row + ",門牌不可空白");
				}

				// 用途
				String columnM = makeExcel.getValue(row, 13).toString();
				columnM = columnM.substring(0, columnM.indexOf("_"));
				this.info("columnM = " + columnM);
				String useCode = columnM;
				checkCode(titaVo, "BdMainUseCode", useCode, "M ,行數:" + row, "用途", columnM);

				// 建物類別
				String columnN = makeExcel.getValue(row, 14).toString();
				columnN = columnN.substring(0, columnN.indexOf("_"));
				this.info("columnN = " + columnN);
				String buTypeCode = columnN;
				checkCode(titaVo, "BdTypeCode", buTypeCode, "N ,行數:" + row, "建物類別", columnN);

				// 建材
				String columnO = makeExcel.getValue(row, 15).toString();
				columnO = columnO.substring(0, columnO.indexOf("_"));
				this.info("columnO = " + columnO);
				String mtrlCode = columnO;
				checkCode(titaVo, "BdMtrlCode", mtrlCode, "O ,行數:" + row, "建材", columnO);

				// 樓層
				String columnP = makeExcel.getValue(row, 16).toString();
				this.info("columnP = " + columnP);
				String floorNo = columnP.trim();
				if (floorNo.isEmpty()) {
					throw new LogicException("E0015", "欄位:P ,行數:" + row + ",樓層不可空白");
				}

				// 總樓層
				String columnQ = makeExcel.getValue(row, 17).toString();
				this.info("columnQ = " + columnQ);
				int totalFloor = (int) toNumeric(columnQ);
				if (totalFloor == 0) {
					throw new LogicException("E0015", "欄位:Q ,行數:" + row + ",總樓層不得為0或空白,總樓層=" + columnQ);
				}

				// 建築完成日期
				String columnR = makeExcel.getValue(row, 18).toString();
				this.info("columnR = " + columnR);
				String bdDate = columnR;
				if (!checkDate(bdDate)) {
					throw new LogicException("E0015", "欄位:R ,行數:" + row + ",不是一個正確的日期,建築完成日期=" + bdDate);
				}
				if (!compareDate(bdDate, titaVo.getParam("CALDY"))) {
					throw new LogicException("E0015", "欄位:R ,行數:" + row + ",建築完成日期(" + bdDate + "),不可大於日曆日");
				}
			}

			// 設定日期
			String columnS = makeExcel.getValue(row, 19).toString();
			this.info("columnS = " + columnS);
			String settingDate = columnS;
			if (!checkDate(settingDate)) {
				throw new LogicException("E0015", "欄位:S ,行數:" + row + ",不是一個正確的日期,設定日期=" + settingDate);
			}
			if (!compareDate(settingDate, titaVo.getParam("CALDY"))) {
				throw new LogicException("E0015", "欄位:S ,行數:" + row + ",設定日期(" + settingDate + "),不可大於日曆日");
			}

			// 面積(坪)
			String columnT = makeExcel.getValue(row, 20).toString();
			this.info("columnT = " + columnT);
			String value20 = columnT;
			BigDecimal floorArea = new BigDecimal(value20);
			if (floorArea.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException("E0015", "欄位:T ,行數:" + row + ",面積不得為0或空白,面積=" + columnT);
			}

			// 鑑估單價
			String columnU = makeExcel.getValue(row, 21).toString();
			this.info("columnU = " + columnU);
			String value21 = columnU;
			BigDecimal unitPrice = new BigDecimal(value21);
			unitPrice = unitPrice.setScale(0, RoundingMode.HALF_UP);
			if (unitPrice.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException("E0015", "欄位:U ,行數:" + row + ",鑑估單價不得為0或空白,鑑估單價=" + columnU);
			}

			// 鑑估總價
			String columnV = makeExcel.getValue(row, 22).toString();
			this.info("columnV = " + columnV);
			String value22 = columnV;
			BigDecimal evaAmt = new BigDecimal(value22);
			evaAmt = evaAmt.setScale(0, RoundingMode.HALF_UP);
			if (evaAmt.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException("E0015", "欄位:V ,行數:" + row + ",鑑估總價不得為0或空白,鑑估總價=" + columnV);
			}

			// 增值稅
			String columnW = makeExcel.getValue(row, 23).toString();
			this.info("columnW = " + columnW);
			String tax = columnW;
			if (tax != null && !tax.isEmpty()) {
				try {
					new BigDecimal(tax);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:W ,行數:" + row + ",增值稅有輸入時需為數字,增值稅=" + tax);
				}
			}

			// 淨值
			String columnX = makeExcel.getValue(row, 24).toString();
			this.info("columnX = " + columnX);
			String netValue = columnX;
			if (netValue != null && !netValue.isEmpty()) {
				try {
					new BigDecimal(netValue);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:X ,行數:" + row + ",淨值有輸入時需為數字,淨值=" + netValue);
				}
			}

			// 押金
			String columnY = makeExcel.getValue(row, 25).toString();
			this.info("columnY = " + columnY);
			String rentPrice = columnY;
			if (rentPrice != null && !rentPrice.isEmpty()) {
				try {
					new BigDecimal(rentPrice);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:Y ,行數:" + row + ",押金有輸入時需為數字,押金=" + rentPrice);
				}
			}

			// 出租淨值
			String columnZ = makeExcel.getValue(row, 26).toString();
			this.info("columnZ = " + columnZ);
			String rentEvaValue = columnZ;
			if (rentEvaValue != null && !rentEvaValue.isEmpty()) {
				try {
					new BigDecimal(rentEvaValue);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:Z ,行數:" + row + ",出租淨值有輸入時需為數字,出租淨值=" + rentEvaValue);
				}
			}

			// 貸放成數
			String columnAA = makeExcel.getValue(row, 27).toString();
			this.info("columnAA = " + columnAA);
			String value27 = columnAA;
			BigDecimal loanToValue = new BigDecimal(value27);
			if (loanToValue.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException("E0015", "欄位:AA ,行數:" + row + ",貸放成數不得為0或空白,貸放成數=" + columnAA);
			}

			// 借款金額
			String columnAB = makeExcel.getValue(row, 28).toString();
			this.info("columnAB = " + columnAB);
			String loanAmt = columnAB;
			if (loanAmt != null && !loanAmt.isEmpty()) {
				try {
					new BigDecimal(loanAmt);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:AB ,行數:" + row + ",借款金額有輸入時需為數字,借款金額=" + loanAmt);
				}
			}

			// 設定金額
			String columnAC = makeExcel.getValue(row, 29).toString();
			this.info("columnAC = " + columnAC);
			double seetingAmt = toNumeric(columnAC) * 1000;
			if (seetingAmt == 0) {
				throw new LogicException("E0015", "欄位:AC ,行數:" + row + ",設定金額不得為0或空白,設定金額 = " + columnAC);
			}

			// 還款金額
			String columnAD = makeExcel.getValue(row, 30).toString();
			this.info("columnAD = " + columnAD);
			String repayAmt = columnAD;
			if (repayAmt != null && !repayAmt.isEmpty()) {
				try {
					new BigDecimal(repayAmt);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:AD ,行數:" + row + ",借款金額有輸入時需為數字,借款金額=" + repayAmt);
				}
			}

			// 保險單號碼
			String columnAE = makeExcel.getValue(row, 31).toString();
			this.info("columnAE = " + columnAE);
			if (columnAE.trim().isEmpty()) {
				throw new LogicException("E0015", "欄位:AE ,行數:" + row + ",保險單號碼不得為空白.");
			}

			// 保險公司
			String columnAF = makeExcel.getValue(row, 32).toString();
			this.info("columnAF = " + columnAF);
			String insuCompany = columnAF.substring(0, columnAF.indexOf("_"));
//			String insuCompanyX = columnAF.substring(columnAF.indexOf("_") + 1);
			checkCode(titaVo, "InsuCompany", insuCompany, "AF ,行數:" + row, "保險公司", insuCompany);

			// 保險類別
			String columnAG = makeExcel.getValue(row, 33).toString();
			this.info("columnAG = " + columnAG);
			String insuTypeCode = columnAG.substring(0, columnAG.indexOf("_"));
//			String insuTypeCodeX = columnAG.substring(columnAG.indexOf("_") + 1);
			checkCode(titaVo, "InsuTypeCode", insuTypeCode, "AG ,行數:" + row, "保險類別", insuTypeCode);

			// 火災險保險金額
			String columnAH = makeExcel.getValue(row, 34).toString();
			this.info("columnAH = " + columnAH);
			BigDecimal fireInsuAmt = new BigDecimal(columnAH);
			if (fireInsuAmt.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException("E0015", "欄位:AH ,行數:" + row + ",火災險保險金額不得為0或空白.");
			}

			// 火災險保費
			String columnAI = makeExcel.getValue(row, 35).toString();
			this.info("columnAI = " + columnAI);
			String fireInsuExpense = columnAI;
			if (fireInsuExpense != null && !fireInsuExpense.isEmpty()) {
				try {
					new BigDecimal(fireInsuExpense);
				} catch (Exception e) {
					throw new LogicException("E0015", "欄位:AI ,行數:" + row + ",火災險保費有輸入時需為數字,火災險保費=" + fireInsuExpense);
				}
			}

			// 地震險保險金額
			String columnAJ = makeExcel.getValue(row, 36).toString();
			this.info("columnAJ = " + columnAJ);
			String earthquakeInsuAmt = columnAJ;
			if (earthquakeInsuAmt != null && !earthquakeInsuAmt.isEmpty()) {
				try {
					new BigDecimal(earthquakeInsuAmt);
				} catch (Exception e) {
					throw new LogicException("E0015",
							"欄位:AI ,行數:" + row + ",地震險保險金額有輸入時需為數字,地震險保險金額=" + earthquakeInsuAmt);
				}
			}

			// 地震險保費
			String columnAK = makeExcel.getValue(row, 37).toString();
			this.info("columnAK = " + columnAK);
			String earthquakeInsuExpense = columnAK;
			if (earthquakeInsuExpense != null && !earthquakeInsuExpense.isEmpty()) {
				try {
					new BigDecimal(earthquakeInsuExpense);
				} catch (Exception e) {
					throw new LogicException("E0015",
							"欄位:AI ,行數:" + row + ",地震險保費有輸入時需為數字,地震險保費=" + earthquakeInsuExpense);
				}
			}

			// 保險起日
			String columnAL = makeExcel.getValue(row, 38).toString();
			this.info("columnAL = " + columnAL);
			if (!checkDate(columnAL)) {
				throw new LogicException("E0015", "欄位:AL ,行數:" + row + ",不是一個正確的日期,保險起日=" + columnAL);
			}

			// 保險迄日
			String columnAM = makeExcel.getValue(row, 39).toString();
			this.info("columnAM = " + columnAM);
			if (!checkDate(columnAM)) {
				throw new LogicException("E0015", "欄位:AM ,行數:" + row + ",不是一個正確的日期,保險迄日=" + columnAM);
			}

			// 所有權人

			// 所有權人1-所有權種類
			String columnAN = makeExcel.getValue(row, 40).toString();
			String ownerType1 = columnAN;
			if (columnAN != null && !columnAN.isEmpty() && columnAN.indexOf("_") >= 0) {
				ownerType1 = columnAN.substring(0, columnAN.indexOf("_"));
				ownerType1 = ownerType1.trim();
			}
			this.info("columnAN = " + columnAN);
			// 2022-10-18 Wei 修改 from 2022-10-17 會議
			// 所有權人1必須輸入，可以只輸入第一筆，留空白者複製前一筆所有權人資料
			// 需判斷擔保品代號1為1房地時,所有權人1需填建物所有權人
			// 當所有權人1的種類為空時,取前一筆資料的全部所有權人資料,若無前一筆資料的全部所有權人資料時,須給錯誤提示

			if (ownerType1 == null || ownerType1.isEmpty()) {
				// CASE 1: 所有權人1-所有權種類為空白
				// 此情形需確認上一筆所有權人資料是否存在
				// 若不存在須提示錯誤
				if (lastOwnerList == null || lastOwnerList.isEmpty()) {
					throw new LogicException("E0015", "欄位:AN ,行數:" + row + ",無前筆資料時,所有權人1不得為空白.");
				} else {
					// 沿用上一筆所有權人資料時,此筆不繼續往右讀取
					this.info("行數:" + row + ",沿用上一筆所有權人資料時,此筆不繼續往右讀取");
					continue;
				}
			}

			// CASE 2: 所有權人-所有權種類不為空白
			// 此情形須清空lastOwnerList
			// 用此筆資料建立lastOwnerList
			lastOwnerList = new ArrayList<>();

			// 檢核 所有權種類 1:建物;2:土地
			if (clCode1 == 1 && !ownerType1.equals("1")) {
				throw new LogicException("E0015", "欄位:AN ,行數:" + row + ",此筆為房地擔保品,所有權人1須填寫建物所有權人.");
			} else if (clCode1 == 2 && !ownerType1.equals("2")) {
				throw new LogicException("E0015", "欄位:AN ,行數:" + row + ",此筆為土地擔保品,所有權人1須填寫土地所有權人.");
			} else if (!ownerType1.equals("1") && !ownerType1.equals("2")) {
				throw new LogicException("E0015", "欄位:AN ,行數:" + row + ",所有權人1-所有權種類應為1_建物或2_土地,實際輸入=" + columnAN);
			}

			// 所有權人1-身分證/統編
			String columnAO = makeExcel.getValue(row, 41).toString();
			this.info("columnAO = " + columnAO);
			String ownerId1 = columnAO.trim();
			if (ownerId1 == null || ownerId1.isEmpty()) {
				throw new LogicException("E0015", "欄位:AO ,行數:" + row + ",有選擇所有權種類時,所有權人-身分證/統編不得為空白.");
			}

			// 所有權人1-姓名
			String columnAP = makeExcel.getValue(row, 42).toString();
			this.info("columnAP = " + columnAP);
			String ownerName1 = columnAP.trim();
			if (ownerName1 == null || ownerName1.isEmpty()) {
				throw new LogicException("E0015", "欄位:AP ,行數:" + row + ",有選擇所有權種類時,所有權人-姓名不得為空白.");
			}

			// 所有權人1-與授信戶關係
			String columnAQ = makeExcel.getValue(row, 43).toString();
			String ownerRel1 = columnAQ;
			if (ownerRel1 == null || ownerRel1.isEmpty()) {
				throw new LogicException("E0015", "欄位:AQ ,行數:" + row + ",有選擇所有權種類時,所有權人-與授信戶關係不得為空白.");
			}
			if (ownerRel1.indexOf("_") >= 0) {
				ownerRel1 = ownerRel1.substring(0, ownerRel1.indexOf("_"));
				if (!ownerRel1.equals("00")) {
					checkCode(titaVo, "GuaRelCode", ownerRel1, "AQ ,行數:" + row, "與授信戶關係", ownerRel1);
				}
			}
			this.info("columnAQ = " + columnAQ);

			// 所有權人1-持份比率
			String columnAR = makeExcel.getValue(row, 44).toString();
			this.info("columnAR = " + columnAR);
			if (columnAR == null || columnAR.isEmpty()) {
				throw new LogicException("E0015", "欄位:AR ,行數:" + row + ",有選擇所有權種類時,所有權人-持份比率不得為空白.");
			}
			if (columnAR.indexOf("分之") >= 0) {
				String ownerTotal = columnAR.substring(0, columnAR.indexOf("分之")); // 持份比率(分母)
				String ownerPart = columnAR.substring(columnAR.indexOf("分之") + 2); // 持份比率(分子)
				this.info("ownerPart = " + ownerPart);
				this.info("ownerTotal = " + ownerTotal);
			} else {
				throw new LogicException("E0015", "欄位:AR ,行數:" + row + ",所有權人-持份比率格式為幾分之幾,可參考權狀上的權利範圍.");
			}

			// 所有權人1 檢核皆通過,紀錄到lastOwnerList
			Map<String, String> owner = new HashMap<>();
			owner.put("type", columnAN);
			owner.put("id", columnAO);
			owner.put("name", columnAP);
			owner.put("rel", columnAQ);
			owner.put("part", columnAR);
			lastOwnerList.add(owner);

			checkElseOwner(row, titaVo);
		}
	}

	private void checkElseOwner(int row, TitaVo titaVo) throws LogicException {
		this.info("checkElseOwner row = " + row);
		for (int ownerSeq = 2; ownerSeq <= 20; ownerSeq++) {

			// 所有權人2 第1欄為45
			// 所有權人3 第1欄為50
			// 依此類推...
			int column = 45 + ((ownerSeq - 2) * 5);

			this.info("ownerSeq=" + ownerSeq);
			this.info("column=" + column);

			// 所有權人-所有權種類
			String columnAN = makeExcel.getValue(row, column).toString();
			String ownerType1 = columnAN;
			if (columnAN != null && !columnAN.isEmpty() && columnAN.indexOf("_") >= 0) {
				ownerType1 = columnAN.substring(0, columnAN.indexOf("_"));
				ownerType1 = ownerType1.trim();
			}
			this.info("columnAN = " + columnAN);

			// 若所有權種類為空白,中止迴圈
			if (columnAN == null || columnAN.trim().isEmpty()) {
				this.info("行數:" + row + ",所有權人" + ownerSeq + "所有權種類為空白,不繼續往右讀取");
				break;
			}

			// 檢核 所有權種類 1:建物;2:土地
			if (!ownerType1.equals("1") && !ownerType1.equals("2")) {
				throw new LogicException("E0015",
						"行數:" + row + ",所有權人" + ownerSeq + "-所有權種類應為1_建物或2_土地,實際輸入=" + columnAN);
			}

			// 所有權人-身分證/統編
			String columnAO = makeExcel.getValue(row, column + 1).toString();
			this.info("columnAO = " + columnAO);
			String ownerId1 = columnAO.trim();
			if (ownerId1 == null || ownerId1.isEmpty()) {
				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-身分證/統編不得為空白.");
			}

			// 所有權人-姓名
			String columnAP = makeExcel.getValue(row, column + 2).toString();
			this.info("columnAP = " + columnAP);
			String ownerName1 = columnAP.trim();
			if (ownerName1 == null || ownerName1.isEmpty()) {
				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-姓名不得為空白.");
			}

			// 所有權人-與授信戶關係
			String columnAQ = makeExcel.getValue(row, column + 3).toString();
			String ownerRel1 = columnAQ;
			if (ownerRel1 == null || ownerRel1.isEmpty()) {
				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-與授信戶關係不得為空白.");
			}
			if (ownerRel1.indexOf("_") >= 0) {
				ownerRel1 = ownerRel1.substring(0, ownerRel1.indexOf("_"));
				if (!ownerRel1.equals("00")) {
					checkCode(titaVo, "GuaRelCode", ownerRel1, "行數:" + row, "所有權人" + ownerSeq + "與授信戶關係", ownerRel1);
				}
			}
			this.info("columnAQ = " + columnAQ);

			// 所有權人-持份比率
			String columnAR = makeExcel.getValue(row, column + 4).toString();
			this.info("columnAR = " + columnAR);
			if (columnAR == null || columnAR.isEmpty()) {
				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-持份比率不得為空白.");
			}
			if (columnAR.indexOf("分之") >= 0) {
				String ownerTotal = columnAR.substring(0, columnAR.indexOf("分之")); // 持份比率(分母)
				String ownerPart = columnAR.substring(columnAR.indexOf("分之") + 2); // 持份比率(分子)
				this.info("ownerPart = " + ownerPart);
				this.info("ownerTotal = " + ownerTotal);
			} else {
				throw new LogicException("E0015", "行數:" + row + ",所有權人" + ownerSeq + "-持份比率格式為幾分之幾,可參考權狀上的權利範圍.");
			}

			// 所有權人 檢核皆通過,紀錄到lastOwnerList
			Map<String, String> owner = new HashMap<>();
			owner.put("type", columnAN);
			owner.put("id", columnAO);
			owner.put("name", columnAP);
			owner.put("rel", columnAQ);
			owner.put("part", columnAR);
			lastOwnerList.add(owner);
		}
	}

	private String getGroupNo(int applNo, TitaVo titaVo) throws LogicException {
		int groupNo = 1;
		String newGroupNo = leftPadZero(applNo, 7) + leftPadZero(groupNo, 3);
		CdClBatch tCdClBatch = sCdClBatchService.holdById(applNo, titaVo);
		if (tCdClBatch == null) {
			// insert
			tCdClBatch = new CdClBatch();
			tCdClBatch.setApplNo(applNo);
			tCdClBatch.setGroupNo(newGroupNo);
			try {
				sCdClBatchService.insert(tCdClBatch, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品整批匯入批號紀錄檔(CdClBatch)" + e.getErrorMsg());
			}
		} else {
			// update
			String oldGroupNo = tCdClBatch.getGroupNo();
			groupNo = (Integer.parseInt(oldGroupNo) % 1000) + 1;
			newGroupNo = leftPadZero(applNo, 7) + leftPadZero(groupNo, 3);
			tCdClBatch.setGroupNo(newGroupNo);
			try {
				sCdClBatchService.update(tCdClBatch, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品整批匯入批號紀錄檔(CdClBatch)" + e.getErrorMsg());
			}
		}
		return newGroupNo;
	}

	private int getClNo(int clCode1, int clCode2, TitaVo titaVo) throws LogicException {
		String clCode = StringUtils.leftPad(String.valueOf(clCode1), 2, "0")
				+ StringUtils.leftPad(String.valueOf(clCode2), 2, "0");
		return gSeqCom.getSeqNo(0, 0, "L2", clCode, 9999999, titaVo);
	}

	// 寫入或修改整批匯入的擔保品資料
	private void modifyClData(TitaVo titaVo) throws LogicException {
		// 執行背景交易
		MySpring.newTask("L2419Batch", this.txBuffer, titaVo);
	}

	private long toTxFile(TitaVo titaVo, String filename, String newFileItem) throws LogicException {
		TxFile txFile = new TxFile();

		txFile.setFileDate(titaVo.getEntDyI());
		txFile.setFileCode(titaVo.getTxCode());
		txFile.setFileItem(newFileItem);
		txFile.setFileType(7);
		txFile.setFileOutput(newFileItem);
		txFile.setFileZip(zLibUtils.compress(new File(filename)));
		txFile.setBrNo(titaVo.getKinbr());

		try {
			txFile = sTxFileService.insert(txFile, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "輸出檔(TxFile):" + e.getErrorMsg());
		}

		return txFile.getFileNo();
	}

	private boolean compareDate(String dtA, String dtB) throws LogicException {
		boolean r = true;
		String dtA2 = dtA.replace("/", "");
		String dtB2 = dtB.replace("/", "");
		int dt1 = parse.stringToInteger(dtA2);
		int dt2 = parse.stringToInteger(dtB2);
		if (dt1 > dt2) {
			r = false;
		}
		return r;
	}

	private boolean checkDate(String dt) throws LogicException {
		String dt2 = dt.replace("/", "");
		return dateUtil.checkDate(dt2);
	}

	private String checkCode(TitaVo titaVo, String defCode, String code, String column, String desc, String eCode)
			throws LogicException {
		String rs = getItem(defCode + "=" + code);
		if (rs.isEmpty()) {
			rs = getCdCode(titaVo, defCode, code, column, desc, eCode);
			items.put(defCode + "=" + code, rs);
		}
		return rs;
	}

	private String getItem(String k) {
		String rs = "";
		if (items.size() > 0 && items.containsKey(k) && items.get(k) != null) {
			rs = items.get(k);
		}
		return rs;
	}

	private String getCdCode(TitaVo titaVo, String defCode, String code, String column, String desc, String eCode)
			throws LogicException {
		CdCodeId cdCodeId = new CdCodeId();
		cdCodeId.setDefCode(defCode);
		cdCodeId.setCode(code);
		CdCode cdCode = sCdCodeService.findById(cdCodeId, titaVo);
		if (cdCode == null) {
			throwError(column, desc, eCode);
		}
		return cdCode.getItem();
	}

	private void throwError(String column, String desc, String code) throws LogicException {
		throw new LogicException("E0015", "欄位:" + column + ",「" + desc + "」代碼 = " + code);
	}

	private double toNumeric(Object o) {
		String s = o == null ? "" : o.toString();
		double r = 0;
		try {
			r = parse.stringToDouble(s);
		} catch (Exception e) {
			r = 0;
		}
		return r;
	}

	/**
	 * 左補零 若傳入值長度大於目標總長度,由右至左取目標總長度的值後,多餘的值截斷 若傳入值長度小於目標總長度,從左邊補零至目標總長度
	 * 
	 * @param inputObject  傳入值
	 * @param targetLength 目標總長度
	 * @return 處理後的字串
	 */
	private String leftPadZero(Object inputObject, int targetLength) {
		String result = inputObject == null ? "" : inputObject.toString();
		result = result == null ? "" : result;
		int len = result.length();
		if (len > targetLength) {
			result = result.substring(len - targetLength);
		} else if (len < targetLength) {
			for (int i = 1; i <= targetLength - len; i++) {
				result = "0" + result;
			}
		}
		return result;
	}
}