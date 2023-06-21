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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClBatchService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClBatchService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
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
	@Autowired
	private FacMainService sFacMainService;

	private HashMap<String, String> items = new HashMap<String, String>();

	private List<Map<String, String>> lastOwnerList = null;

	private boolean noError = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2419 ");
		this.totaVo.init(titaVo);

		String function = titaVo.getParam("Function");

		noError = true;

		switch (function) {
		case "1":
			check(1, titaVo); // 檢核
			feedback(titaVo); // 取號並產生回饋檔
			break;
		case "2":
			check(2, titaVo); // 檢核
			modifyClData(titaVo); // 寫入或修改整批匯入的擔保品資料
			break;
		case "3":
			report(titaVo); // 以系統擔保品資料產生回饋檔
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

		String msg = "";

		if (noError) {
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
				String columnA = makeExcel.getValue(row, L2419Column.NO.getIndex()).toString(); // NO
				int no = (int) toNumeric(columnA);

				if (no == 0) {
					break;
				}

				// 擔保品代號1
				String columnB = makeExcel.getValue(row, L2419Column.CL_CODE_1.getIndex()).toString();
				if (columnB == null || columnB.isEmpty()) {
					continue;
				}
				columnB = columnB.substring(0, columnB.indexOf("_"));
				this.info("columnB = " + columnB);
				int clCode1 = Integer.parseInt(columnB);
				if (clCode1 != 1 && clCode1 != 2) {
					continue;
				}

				// 擔保品代號2
				String columnC = makeExcel.getValue(row, L2419Column.CL_CODE_2.getIndex()).toString();
				if (columnC == null || columnC.isEmpty()) {
					continue;
				}
				columnC = columnC.substring(0, columnC.indexOf("_"));
				this.info("columnC = " + columnC);
				int clCode2 = Integer.parseInt(columnC);

				// 擔保品序號
				int clNo = this.getClNo(clCode1, clCode2, titaVo);

				makeExcel.setValueInt(row, L2419Column.CL_NO.getIndex(), clNo);

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

			makeExcel.lockColumn(3, row - 1, L2419Column.NO.getIndex(), L2419Column.CL_NO.getIndex(), 142); // 142:最後一個欄位Index

			makeExcel.protectSheet(groupNo);
			String newFileItem = "擔保品明細表回饋檔_" + groupNo + ".xlsx";
			String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + newFileItem;
			makeExcel.saveExcel(fileName);
			toTxFile(titaVo, fileName, newFileItem);
			msg = "擔保品整批匯入，資料已檢核通過，請至【報表及製檔】下傳回饋檔";
		} else {
			String newFileItem = "擔保品明細表回饋檔_error.xlsx";
			String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + newFileItem;
			makeExcel.saveExcel(fileName);
			toTxFile(titaVo, fileName, newFileItem);
			msg = "擔保品整批匯入，資料檢核不通過，請至【報表及製檔】下傳回饋檔，修正紅底欄位資料";
		}

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				String.format("%-8s", titaVo.getTlrNo().trim()) + "L2419", msg, titaVo);
	}

	/**
	 * 檢核上傳檔
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException LogicException
	 */
	private void check(int functionCode, TitaVo titaVo) throws LogicException {
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

		if (functionCode == 1) {
			CustMain custMain = sCustMainService.custIdFirst(custId, titaVo);
			if (custMain == null) {
				throw new LogicException("E0001", "客戶統編 = " + custId);
			}
		}

		int lastRowNum = makeExcel.getSheetLastRowNum();

		this.info("lastRowNum=" + lastRowNum);

		lastOwnerList = new ArrayList<>();

		int lastNo = 1;

		for (int i = 0; i <= lastRowNum; i++) {

			int row = 3 + i;

			this.info("row = " + row);

			makeExcel.setNoBackgroundByRow(row);

			// 編號
			String columnA = makeExcel.getValue(row, L2419Column.NO.getIndex()).toString(); // NO
			int no;
			this.info("columnA = " + columnA);

			// 是否寫入資料庫
			String insertFlag = makeExcel.getValue(row, L2419Column.INSERT_FLAG.getIndex()).toString();

			// 2023-06-21 Wei 增加判斷: User在這欄可能留空白,即使這欄空白若其他欄位有資料也需要做檢核
			boolean isEntered = chectIsEnter(row);

			if (isEntered) {
				no = lastNo;
				makeExcel.setValueInt(row, L2419Column.NO.getIndex(), no);
				lastNo++;
			} else {
				break;
			}

			// 擔保品代號1
			String clCode1String = makeExcel.getValue(row, L2419Column.CL_CODE_1.getIndex()).toString();
			int clCode1 = 0;
			if (clCode1String != null && !clCode1String.isEmpty()) {
				clCode1String = clCode1String.substring(0, clCode1String.indexOf("_"));
				this.info("clCode1String = " + clCode1String);
				clCode1 = (int) toNumeric(clCode1String);
				if (clCode1 != 1 && clCode1 != 2) {
//				throwError("B ,行數:" + row, "擔保品代號1應為1或2", columnB);
					setError(row, L2419Column.CL_CODE_1.getIndex());
				}
			} else {
				// 此為必輸欄位
				setError(row, L2419Column.CL_CODE_1.getIndex());
			}

			// 擔保品代號2
			String columnC = makeExcel.getValue(row, L2419Column.CL_CODE_2.getIndex()).toString();
			if (columnC != null && !columnC.isEmpty()) {
				columnC = columnC.substring(0, columnC.indexOf("_"));
				this.info("columnC = " + columnC);
				String clCode2 = columnC;
				if (clCode1 == 1) {
					checkCode(titaVo, "ClCode21", "" + clCode2, "擔保品代號2", columnC, row,
							L2419Column.CL_CODE_2.getIndex());
				} else if (clCode1 == 2) {
					checkCode(titaVo, "ClCode22", "" + clCode2, "擔保品代號2", columnC, row,
							L2419Column.CL_CODE_2.getIndex());
				}
			} else {
				// 此為必輸欄位
				setError(row, L2419Column.CL_CODE_2.getIndex());
			}

			// 擔保品編號
			String clNoString = makeExcel.getValue(row, L2419Column.CL_NO.getIndex()).toString();
			this.info("clNoString = " + clNoString);
			int clNo = (int) toNumeric(clNoString);

			if (functionCode == 1 && !clNoString.isEmpty() && clNo != 0) {
				// 功能1初次上傳時，擔保品號碼不可輸入
				setError(row, L2419Column.CL_NO.getIndex());
			}

			// 是否寫入資料庫
			if ((insertFlag == null || insertFlag.isEmpty()) || !(insertFlag.equals("Y") || insertFlag.equals("N"))) {
				setError(row, L2419Column.INSERT_FLAG.getIndex());
			}

			if (insertFlag.equals("N")) {
				int applNo = Integer.parseInt(titaVo.getParam("ApplNo"));

				FacMain facMain = sFacMainService.facmApplNoFirst(applNo, titaVo);

				String drawdownStatus = "N";

				if (facMain != null && facMain.getLastBormNo() > 0) {
					drawdownStatus = "Y";
				}

				if (drawdownStatus.equals("Y")) {
					// 已經撥款 insertFlag不能改為N
					setError(row, L2419Column.INSERT_FLAG.getIndex());
				}
			}

			// 擔保品類別代碼
			String typeCode = makeExcel.getValue(row, L2419Column.CL_TYPE.getIndex()).toString();
			if (typeCode != null && !typeCode.isEmpty()) {
				typeCode = typeCode.substring(0, typeCode.indexOf("_"));
				this.info("typeCode = " + typeCode);
				if (clCode1 == 1) {
					checkCode(titaVo, "ClTypeCode21", typeCode, "擔保品類別", typeCode, row, L2419Column.CL_TYPE.getIndex());
				} else if (clCode1 == 2) {
					checkCode(titaVo, "ClTypeCode22", typeCode, "擔保品類別", typeCode, row, L2419Column.CL_TYPE.getIndex());
				}
			} else {
				// 此為必輸欄位
				setError(row, L2419Column.CL_TYPE.getIndex());
			}

			if (clCode1 == 1) {
				// 建號-前5碼
				String buildNo1 = makeExcel.getValue(row, L2419Column.BUILD_NO_1.getIndex()).toString();
				this.info("buildNo1 = " + buildNo1);
				int bdno1 = (int) toNumeric(buildNo1);
				if (bdno1 == 0) {
					// 若為房地擔保品，不得為0
					setError(row, L2419Column.BUILD_NO_1.getIndex());
				}

				// 建號-後3碼
				String buildNo2 = makeExcel.getValue(row, L2419Column.BUILD_NO_2.getIndex()).toString();
				this.info("buildNo2 = " + buildNo2);
			}

			// 地號-前4碼
			String landNo1 = makeExcel.getValue(row, L2419Column.LAND_NO_1.getIndex()).toString();
			this.info("landNo1 = " + landNo1);
			int ldno1 = (int) toNumeric(landNo1);
			if (ldno1 == 0) {
				// 地號不得為0
				setError(row, L2419Column.LAND_NO_1.getIndex());
			}

			// 地號-後4碼
			String landNo2 = makeExcel.getValue(row, L2419Column.LAND_NO_2.getIndex()).toString();
			this.info("landNo2 = " + landNo2);

			// 郵遞區號
			String zip3 = makeExcel.getValue(row, L2419Column.ZIP_3.getIndex()).toString();
			String cityCode = "";
			String areaCode = "";
			if (zip3 != null && !zip3.isEmpty()) {
				this.info("zip3 = " + zip3);
				cityCode = getItem("zipcity=" + zip3);
				areaCode = "";

				if (cityCode.isEmpty()) {
					CdArea cdArea = sCdAreaService.Zip3First(zip3, titaVo);
					if (cdArea == null) {
						// 郵遞區號無法找到鄉鎮市區代碼
						setError(row, L2419Column.ZIP_3.getIndex());
					} else {
						CdCity cdCity = sCdCityService.findById(cdArea.getCityCode(), titaVo);
						if (cdCity == null) {
							// 郵遞區號無法找到縣市別代碼
							setError(row, L2419Column.ZIP_3.getIndex());
						}

						items.put("zipcity=" + zip3, cdArea.getCityCode());
						items.put("city=" + cdArea.getCityCode(), cdCity.getCityItem());
						items.put("ziparea=" + zip3, cdArea.getAreaCode());
						items.put("area=" + cdArea.getCityCode() + "-" + cdArea.getAreaCode(), cdArea.getAreaItem());

						// 縣市
						cityCode = cdArea.getCityCode();
						// 鄉鎮市區
						areaCode = cdArea.getAreaCode();
					}
				} else {
					// 縣市
					// 鄉鎮市區
					areaCode = getItem("ziparea=" + zip3);
				}
			} else {
				// 此為必輸欄位
				setError(row, L2419Column.ZIP_3.getIndex());
			}

			// 段小段代碼
			String irCode = makeExcel.getValue(row, L2419Column.IR_CODE.getIndex()).toString();
			if (irCode != null && !irCode.isEmpty()) {
				this.info("irCode = " + irCode);
				irCode = leftPadZero(irCode, 4);

				this.info("CdLandSection = " + cityCode + "-" + areaCode + "-" + irCode);
				CdLandSectionId cdLandSectionId = new CdLandSectionId();
				cdLandSectionId.setCityCode(cityCode);
				cdLandSectionId.setAreaCode(areaCode);
				cdLandSectionId.setIrCode(irCode);

				CdLandSection cdLandSection = sCdLandSectionService.findById(cdLandSectionId, titaVo);
				if (cdLandSection == null) {
					// 此地段4碼在地段代碼檔查無資料
					setError(row, L2419Column.IR_CODE.getIndex());
				}
			} else {
				// 此為必輸欄位
				setError(row, L2419Column.IR_CODE.getIndex());
			}

			if (clCode1 == 1) {
				// 門牌
				String road = makeExcel.getValue(row, L2419Column.ROAD.getIndex()).toString().trim();
				this.info("road = " + road);
				if (road.isEmpty()) {
					// 門牌不可空白
					setError(row, L2419Column.ROAD.getIndex());
				}

				// 用途
				String useCode = makeExcel.getValue(row, L2419Column.USE_CODE.getIndex()).toString().trim();
				this.info("useCode = " + useCode);
				if (!useCode.isEmpty()) {
					useCode = useCode.substring(0, useCode.indexOf("_"));
					checkCode(titaVo, "BdMainUseCode", useCode, "用途", useCode, row, 14);
				} else {
					setError(row, L2419Column.USE_CODE.getIndex());
				}

				// 建物類別
				String buTypeCode = makeExcel.getValue(row, L2419Column.BUILD_TYPE.getIndex()).toString();
				this.info("buTypeCode = " + buTypeCode);
				if (!buTypeCode.isEmpty()) {
					buTypeCode = buTypeCode.substring(0, buTypeCode.indexOf("_"));
					checkCode(titaVo, "BdTypeCode", buTypeCode, "建物類別", buTypeCode, row,
							L2419Column.BUILD_TYPE.getIndex());
				} else {
					setError(row, L2419Column.BUILD_TYPE.getIndex());
				}

				// 建材
				String mtrlCode = makeExcel.getValue(row, L2419Column.MTRL.getIndex()).toString();
				this.info("mtrlCode = " + mtrlCode);
				if (!mtrlCode.isEmpty()) {
					mtrlCode = mtrlCode.substring(0, mtrlCode.indexOf("_"));
					checkCode(titaVo, "BdMtrlCode", mtrlCode, "建材", mtrlCode, row, L2419Column.MTRL.getIndex());
				} else {
					setError(row, L2419Column.MTRL.getIndex());
				}

				// 樓層
				String floorNo = makeExcel.getValue(row, L2419Column.FLOOR_NO.getIndex()).toString().trim();
				this.info("floorNo = " + floorNo);
				if (floorNo.isEmpty()) {
					// 樓層不可空白
					setError(row, L2419Column.FLOOR_NO.getIndex());
				}

				// 總樓層
				String totalFloorString = makeExcel.getValue(row, L2419Column.TOTAL_FLOOR.getIndex()).toString();
				this.info("totalFloorString = " + totalFloorString);
				int totalFloor = (int) toNumeric(totalFloorString);
				if (totalFloor == 0) {
					// 總樓層不得為0或空白
					setError(row, L2419Column.TOTAL_FLOOR.getIndex());
				}

				// 建築完成日期
				String bdDate = makeExcel.getValue(row, L2419Column.BUILD_DATE.getIndex()).toString();
				this.info("bdDate = " + bdDate);
				if (!checkDate(bdDate)) {
					// 不是一個正確的日期
					setError(row, L2419Column.BUILD_DATE.getIndex());
				}
				if (!compareDate(bdDate, titaVo.getParam("CALDY"))) {
					// 建築完成日期不可大於日曆日
					setError(row, L2419Column.BUILD_DATE.getIndex());
				}
			}

			// 設定日期
			String settingDate = makeExcel.getValue(row, L2419Column.SETTING_DATE.getIndex()).toString();
			this.info("settingDate = " + settingDate);
			if (!checkDate(settingDate)) {
				// 不是一個正確的日期
				setError(row, L2419Column.SETTING_DATE.getIndex());
			}
			if (!compareDate(settingDate, titaVo.getParam("CALDY"))) {
				// 設定日期,不可大於日曆日
				setError(row, L2419Column.SETTING_DATE.getIndex());
			}

			// 擔保債權確定日期
			String claimDate = makeExcel.getValue(row, L2419Column.CLAIM_DATE.getIndex()).toString();
			this.info("claimDate = " + claimDate);
			if ((!claimDate.isEmpty()) && !checkDate(claimDate)) {
				setError(row, L2419Column.CLAIM_DATE.getIndex());
			}

			// 面積(坪)
			String floorAreaString = makeExcel.getValue(row, L2419Column.FLOOR_AREA.getIndex()).toString();
			this.info("floorAreaString = " + floorAreaString);
			BigDecimal floorArea = BigDecimal.ZERO;
			try {
				floorArea = new BigDecimal(floorAreaString);
			} catch (Exception e) {
				setError(row, L2419Column.FLOOR_AREA.getIndex());
			}
			if (floorArea.compareTo(BigDecimal.ZERO) == 0) {
				// 面積不得為0或空白
				setError(row, L2419Column.FLOOR_AREA.getIndex());
			}

			// 鑑估單價
			String unitPriceString = makeExcel.getValue(row, L2419Column.UNIT_PRICE.getIndex()).toString();
			this.info("unitPriceString = " + unitPriceString);
			BigDecimal unitPrice = BigDecimal.ZERO;
			try {
				unitPrice = new BigDecimal(unitPriceString);
				unitPrice = unitPrice.setScale(0, RoundingMode.HALF_UP);
			} catch (Exception e) {
				setError(row, L2419Column.UNIT_PRICE.getIndex());
			}
			if (unitPrice.compareTo(BigDecimal.ZERO) == 0) {
				setError(row, L2419Column.UNIT_PRICE.getIndex());
			}

			// 鑑估總價
			String evaAmtString = makeExcel.getValue(row, L2419Column.EVA_AMT.getIndex()).toString();
			this.info("evaAmtString = " + evaAmtString);
			BigDecimal evaAmt = BigDecimal.ZERO;
			try {
				evaAmt = new BigDecimal(evaAmtString);
				evaAmt = evaAmt.setScale(0, RoundingMode.HALF_UP);
			} catch (Exception e) {
				setError(row, L2419Column.EVA_AMT.getIndex());
			}
			if (evaAmt.compareTo(BigDecimal.ZERO) == 0) {
				// 鑑估總價不得為0或空白
				setError(row, L2419Column.EVA_AMT.getIndex());
			}

			// 增值稅
			String tax = makeExcel.getValue(row, L2419Column.TAX.getIndex()).toString();
			this.info("tax = " + tax);
			if (tax != null && !tax.isEmpty()) {
				try {
					new BigDecimal(tax);
				} catch (Exception e) {
					// 增值稅有輸入時需為數字
					setError(row, L2419Column.TAX.getIndex());
				}
			}

			// 淨值
			String netValue = makeExcel.getValue(row, L2419Column.NET_VALUE.getIndex()).toString();
			this.info("netValue = " + netValue);
			if (netValue != null && !netValue.isEmpty()) {
				try {
					new BigDecimal(netValue);
				} catch (Exception e) {
					// 淨值有輸入時需為數字
					setError(row, L2419Column.NET_VALUE.getIndex());
				}
			}

			// 押金
			String rentPrice = makeExcel.getValue(row, L2419Column.RENT_PRICE.getIndex()).toString();
			this.info("rentPrice = " + rentPrice);
			if (rentPrice != null && !rentPrice.isEmpty()) {
				try {
					new BigDecimal(rentPrice);
				} catch (Exception e) {
					// 押金有輸入時需為數字
					setError(row, L2419Column.RENT_PRICE.getIndex());
				}
			}

			// 出租淨值
			String rentEvaValue = makeExcel.getValue(row, L2419Column.RENT_EVA_VALUE.getIndex()).toString();
			this.info("rentEvaValue = " + rentEvaValue);
			if (rentEvaValue != null && !rentEvaValue.isEmpty()) {
				try {
					new BigDecimal(rentEvaValue);
				} catch (Exception e) {
					// 出租淨值有輸入時需為數字
					setError(row, L2419Column.RENT_EVA_VALUE.getIndex());
				}
			}

			// 貸放成數
			String loanToValueString = makeExcel.getValue(row, L2419Column.LOAN_TO_VALUE.getIndex()).toString();
			this.info("loanToValueString = " + loanToValueString);
			BigDecimal loanToValue = BigDecimal.ZERO;
			try {
				loanToValue = new BigDecimal(loanToValueString);
			} catch (Exception e) {
				setError(row, L2419Column.LOAN_TO_VALUE.getIndex());
			}
			if (loanToValue.compareTo(BigDecimal.ZERO) == 0) {
				// 貸放成數不得為0或空白
				setError(row, L2419Column.LOAN_TO_VALUE.getIndex());
			}

			// 借款金額
			String loanAmt = makeExcel.getValue(row, L2419Column.LOAN_AMT.getIndex()).toString();
			this.info("loanAmt = " + loanAmt);

			// 設定金額
			String seetingAmtString = makeExcel.getValue(row, L2419Column.SETTING_AMT.getIndex()).toString();
			this.info("seetingAmtString = " + seetingAmtString);
			double seetingAmt = toNumeric(seetingAmtString) * 1000;
			if (seetingAmt == 0) {
				// 設定金額不得為0或空白
				setError(row, L2419Column.SETTING_AMT.getIndex());
			}

			// 還款金額
			String repayAmt = makeExcel.getValue(row, L2419Column.REPAY_AMT.getIndex()).toString();
			this.info("repayAmt = " + repayAmt);

			// 保險單號碼
			String insuNo = makeExcel.getValue(row, L2419Column.INSU_NO.getIndex()).toString();
			this.info("insuNo = " + insuNo);
			if (insuNo.trim().isEmpty()) {
				// 保險單號碼不得為空白
				setError(row, L2419Column.INSU_NO.getIndex());
			}

			// 保險公司
			String insuCompany = makeExcel.getValue(row, L2419Column.INSU_COMPANY.getIndex()).toString();
			this.info("insuCompany = " + insuCompany);
			if (!insuCompany.isEmpty()) {
				insuCompany = insuCompany.substring(0, insuCompany.indexOf("_"));
				checkCode(titaVo, "InsuCompany", insuCompany, "保險公司", insuCompany, row, 34);
			} else {
				setError(row, L2419Column.INSU_COMPANY.getIndex());
			}

			// 保險類別
			String insuTypeCode = makeExcel.getValue(row, L2419Column.INSU_TYPE.getIndex()).toString();
			this.info("insuTypeCode = " + insuTypeCode);
			if (!insuTypeCode.isEmpty()) {
				insuTypeCode = insuTypeCode.substring(0, insuTypeCode.indexOf("_"));
				checkCode(titaVo, "InsuTypeCode", insuTypeCode, "保險類別", insuTypeCode, row,
						L2419Column.INSU_TYPE.getIndex());
			} else {
				setError(row, L2419Column.INSU_TYPE.getIndex());
			}

			// 火災險保險金額
			String fireInsuAmtString = makeExcel.getValue(row, L2419Column.FIRE_INSU_AMT.getIndex()).toString();
			this.info("fireInsuAmtString = " + fireInsuAmtString);
			BigDecimal fireInsuAmt = BigDecimal.ZERO;
			try {
				fireInsuAmt = new BigDecimal(fireInsuAmtString);
			} catch (Exception e) {
				setError(row, L2419Column.FIRE_INSU_AMT.getIndex());
			}
			if (fireInsuAmt.compareTo(BigDecimal.ZERO) == 0) {
				// 火災險保險金額不得為0或空白
				setError(row, L2419Column.FIRE_INSU_AMT.getIndex());
			}

			// 火災險保費
			String fireInsuExpense = makeExcel.getValue(row, L2419Column.FIRE_INSU_EXPENSE.getIndex()).toString();
			this.info("fireInsuExpense = " + fireInsuExpense);
			if (fireInsuExpense != null && !fireInsuExpense.isEmpty()) {
				try {
					new BigDecimal(fireInsuExpense);
				} catch (Exception e) {
					// 火災險保費有輸入時需為數字
					setError(row, L2419Column.FIRE_INSU_EXPENSE.getIndex());
				}
			}

			// 地震險保險金額
			String earthquakeInsuAmt = makeExcel.getValue(row, L2419Column.EARTHQUAKE_INSU_AMT.getIndex()).toString();
			this.info("earthquakeInsuAmt = " + earthquakeInsuAmt);
			if (earthquakeInsuAmt != null && !earthquakeInsuAmt.isEmpty()) {
				try {
					new BigDecimal(earthquakeInsuAmt);
				} catch (Exception e) {
					// 地震險保險金額有輸入時需為數字
					setError(row, L2419Column.EARTHQUAKE_INSU_AMT.getIndex());
				}
			}

			// 地震險保費
			String earthquakeInsuExpense = makeExcel.getValue(row, L2419Column.EARTHQUAKE_INSU_EXPENSE.getIndex())
					.toString();
			this.info("earthquakeInsuExpense = " + earthquakeInsuExpense);
			if (earthquakeInsuExpense != null && !earthquakeInsuExpense.isEmpty()) {
				try {
					new BigDecimal(earthquakeInsuExpense);
				} catch (Exception e) {
					// 地震險保費有輸入時需為數字
					setError(row, L2419Column.EARTHQUAKE_INSU_EXPENSE.getIndex());
				}
			}

			// 保險起日
			String insuStart = makeExcel.getValue(row, L2419Column.INSU_START.getIndex()).toString();
			this.info("insuStart = " + insuStart);
			if (!checkDate(insuStart)) {
				// 不是一個正確的日期
				setError(row, L2419Column.INSU_START.getIndex());
			}

			// 保險迄日
			String insuEnd = makeExcel.getValue(row, L2419Column.INSU_END.getIndex()).toString();
			this.info("insuEnd = " + insuEnd);
			if (!checkDate(insuEnd)) {
				// 不是一個正確的日期
				setError(row, L2419Column.INSU_END.getIndex());
			}

			// 所有權人

			// 所有權人1-所有權種類
			String ownerType1 = makeExcel.getValue(row, L2419Column.OWNER_TYPE_1.getIndex()).toString();
			if (ownerType1 != null && !ownerType1.isEmpty() && ownerType1.indexOf("_") >= 0) {
				ownerType1 = ownerType1.substring(0, ownerType1.indexOf("_"));
				ownerType1 = ownerType1.trim();
			}
			this.info("ownerType1 = " + ownerType1);
			// 2022-10-18 Wei 修改 from 2022-10-17 會議
			// 所有權人1必須輸入，可以只輸入第一筆，留空白者複製前一筆所有權人資料
			// 需判斷擔保品代號1為1房地時,所有權人1需填建物所有權人
			// 當所有權人1的種類為空時,取前一筆資料的全部所有權人資料,若無前一筆資料的全部所有權人資料時,須給錯誤提示

			if (ownerType1 == null || ownerType1.isEmpty()) {
				// CASE 1: 所有權人1-所有權種類為空白
				// 此情形需確認上一筆所有權人資料是否存在
				// 若不存在須提示錯誤
				if (lastOwnerList == null || lastOwnerList.isEmpty()) {
					// 無前筆資料時,所有權人1不得為空白
					setError(row, L2419Column.OWNER_TYPE_1.getIndex());
				} else {
					// 沿用上一筆所有權人資料時,此筆不繼續往右讀取
					this.info("行數:" + row + ",沿用上一筆所有權人資料時,此筆不繼續往右讀取");
				}
				continue;
			}

			// CASE 2: 所有權人-所有權種類不為空白
			// 此情形須清空lastOwnerList
			// 用此筆資料建立lastOwnerList
			lastOwnerList = new ArrayList<>();

			// 檢核 所有權種類 1:建物;2:土地
			if (clCode1 == 1 && !ownerType1.equals("1")) {
				// 此筆為房地擔保品,所有權人1須填寫建物所有權人
				setError(row, L2419Column.OWNER_TYPE_1.getIndex());
			}
			if (clCode1 == 2 && !ownerType1.equals("2")) {
				// 此筆為土地擔保品,所有權人1須填寫土地所有權人
				setError(row, L2419Column.OWNER_TYPE_1.getIndex());
			}
			if (!ownerType1.equals("1") && !ownerType1.equals("2")) {
				// 所有權人1-所有權種類應為1_建物或2_土地
				setError(row, L2419Column.OWNER_TYPE_1.getIndex());
			}

			// 所有權人1-身分證/統編
			String ownerId1 = makeExcel.getValue(row, L2419Column.OWNER_ID_1.getIndex()).toString().trim();
			this.info("ownerId1 = " + ownerId1);
			if (ownerId1 == null || ownerId1.isEmpty()) {
				// 有選擇所有權種類時,所有權人-身分證/統編不得為空白
				setError(row, L2419Column.OWNER_ID_1.getIndex());
			}

			// 所有權人1-姓名
			String ownerName1 = makeExcel.getValue(row, L2419Column.OWNER_NAME_1.getIndex()).toString().trim();
			this.info("ownerName1 = " + ownerName1);
			if (ownerName1 == null || ownerName1.isEmpty()) {
				// 有選擇所有權種類時,所有權人-姓名不得為空白
				setError(row, L2419Column.OWNER_NAME_1.getIndex());
			}

			// 所有權人1-與授信戶關係
			String ownerRel1 = makeExcel.getValue(row, L2419Column.OWNER_RELATION_1.getIndex()).toString();
			if (ownerRel1 == null || ownerRel1.isEmpty()) {
				// 有選擇所有權種類時,所有權人-與授信戶關係不得為空白
				setError(row, L2419Column.OWNER_RELATION_1.getIndex());
			}
			if (ownerRel1.indexOf("_") >= 0) {
				ownerRel1 = ownerRel1.substring(0, ownerRel1.indexOf("_"));
				if (!ownerRel1.equals("00")) {
					checkCode(titaVo, "GuaRelCode", ownerRel1, "與授信戶關係", ownerRel1, row,
							L2419Column.OWNER_RELATION_1.getIndex());
				}
			}
			this.info("ownerRel1 = " + ownerRel1);

			// 所有權人1-持份比率
			String ownerPartial = makeExcel.getValue(row, L2419Column.OWNER_PARTIAL_1.getIndex()).toString();
			this.info("ownerPartial = " + ownerPartial);
			if (ownerPartial == null || ownerPartial.isEmpty()) {
				// 有選擇所有權種類時,所有權人-持份比率不得為空白
				setError(row, L2419Column.OWNER_PARTIAL_1.getIndex());
			}
			if (ownerPartial.indexOf("分之") >= 0) {
				String ownerTotal = ownerPartial.substring(0, ownerPartial.indexOf("分之")); // 持份比率(分母)
				String ownerPart = ownerPartial.substring(ownerPartial.indexOf("分之") + 2); // 持份比率(分子)
				this.info("ownerPart = " + ownerPart);
				this.info("ownerTotal = " + ownerTotal);
			} else {
				// 所有權人-持份比率格式為幾分之幾,可參考權狀上的權利範圍
				setError(row, L2419Column.OWNER_PARTIAL_1.getIndex());
			}

			// 所有權人1 檢核皆通過,紀錄到lastOwnerList
			Map<String, String> owner = new HashMap<>();
			owner.put("type", ownerType1);
			owner.put("id", ownerId1);
			owner.put("name", ownerName1);
			owner.put("rel", ownerRel1);
			owner.put("part", ownerPartial);
			lastOwnerList.add(owner);

			checkElseOwner(row, titaVo);
		}
	}

	private boolean chectIsEnter(int row) throws LogicException {
		// check 第1~41 若任一欄位有值,回true
		for (int i = 1; i <= 41; i++) {
			String value = (String) makeExcel.getValue(row, i);
			if (value != null && !value.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private void setError(int row, int column) throws LogicException {
		this.info("setError : " + row + " , " + column);
		this.noError = false;
		makeExcel.setErrorColumn(row, column);
	}

	private void checkElseOwner(int row, TitaVo titaVo) throws LogicException {
		this.info("checkElseOwner row = " + row);
		for (int ownerSeq = 2; ownerSeq <= 20; ownerSeq++) {

			// 所有權人2 第1欄為47
			// 所有權人3 第1欄為52
			// 依此類推...
			int column = L2419Column.OWNER_TYPE_2.getIndex() + ((ownerSeq - 2) * 5);

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
//				throw new LogicException("E0015",
//						"行數:" + row + ",所有權人" + ownerSeq + "-所有權種類應為1_建物或2_土地,實際輸入=" + columnAN);
				setError(row, column);
			}

			// 所有權人-身分證/統編
			String columnAO = makeExcel.getValue(row, column + 1).toString();
			this.info("columnAO = " + columnAO);
			String ownerId1 = columnAO.trim();
			if (ownerId1 == null || ownerId1.isEmpty()) {
//				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-身分證/統編不得為空白.");
				setError(row, column + 1);
			}

			// 所有權人-姓名
			String columnAP = makeExcel.getValue(row, column + 2).toString();
			this.info("columnAP = " + columnAP);
			String ownerName1 = columnAP.trim();
			if (ownerName1 == null || ownerName1.isEmpty()) {
//				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-姓名不得為空白.");
				setError(row, column + 2);
			}

			// 所有權人-與授信戶關係
			String columnAQ = makeExcel.getValue(row, column + 3).toString();
			String ownerRel1 = columnAQ;
			if (ownerRel1 == null || ownerRel1.isEmpty()) {
//				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-與授信戶關係不得為空白.");
				setError(row, column + 3);
			}
			if (ownerRel1.indexOf("_") >= 0) {
				ownerRel1 = ownerRel1.substring(0, ownerRel1.indexOf("_"));
				if (!ownerRel1.equals("00")) {
					checkCode(titaVo, "GuaRelCode", ownerRel1, "所有權人" + ownerSeq + "與授信戶關係", ownerRel1, row,
							column + 3);
				}
			}
			this.info("columnAQ = " + columnAQ);

			// 所有權人-持份比率
			String columnAR = makeExcel.getValue(row, column + 4).toString();
			this.info("columnAR = " + columnAR);
			if (columnAR == null || columnAR.isEmpty()) {
//				throw new LogicException("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-持份比率不得為空白.");
				setError(row, column + 4);
			}
			if (columnAR.indexOf("分之") >= 0) {
				String ownerTotal = columnAR.substring(0, columnAR.indexOf("分之")); // 持份比率(分母)
				String ownerPart = columnAR.substring(columnAR.indexOf("分之") + 2); // 持份比率(分子)
				this.info("ownerPart = " + ownerPart);
				this.info("ownerTotal = " + ownerTotal);
			} else {
//				throw new LogicException("E0015", "行數:" + row + ",所有權人" + ownerSeq + "-持份比率格式為幾分之幾,可參考權狀上的權利範圍.");
				setError(row, column + 4);
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
		if (noError) {
			// 執行背景交易
			MySpring.newTask("L2419Batch", this.txBuffer, titaVo);
		} else {

			String fileItem = titaVo.getParam("FileItem").trim();

			String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + fileItem;

			this.info("fileitem=" + fileItem);

			makeExcel.openExcel(fileName, "擔保品明細表");

			String groupNoOnExcelFileName = "";

			if (fileName.indexOf("_") >= 0) {
				groupNoOnExcelFileName = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
			} else {
				throw new LogicException("E0015", "上傳的回饋檔檔名未含有系統產生的批次號碼,請使用L2419功能1所產生的回饋檔.");
			}

			int lastRowNum = makeExcel.getSheetLastRowNum();

			this.info("lastRowNum=" + lastRowNum);

			makeExcel.lockColumn(3, lastRowNum - 1, L2419Column.NO.getIndex(), L2419Column.CL_NO.getIndex(), 142); // 142:最後一個欄位Index

			makeExcel.protectSheet(groupNoOnExcelFileName);

			String newFileItem = "擔保品明細表回饋檔_" + groupNoOnExcelFileName + ".xlsx";
			String newFileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + newFileItem;
			makeExcel.saveExcel(newFileName);

			toTxFile(titaVo, newFileName, newFileItem);

			String msg = "擔保品整批匯入，資料檢核不通過，請至【報表及製檔】下傳回饋檔，修正紅底欄位資料";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					String.format("%-8s", titaVo.getTlrNo().trim()) + "L2419", msg, titaVo);
		}
	}

	private void report(TitaVo titaVo) throws LogicException {
		// 執行背景交易
		MySpring.newTask("L2419Report", this.txBuffer, titaVo);
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
		return (dt2 == null || dt2.isEmpty()) ? false : dateUtil.checkDate(dt2);
	}

	private String checkCode(TitaVo titaVo, String defCode, String code, String desc, String eCode, int r, int c)
			throws LogicException {
		String rs = getItem(defCode + "=" + code);
		if (rs.isEmpty()) {
			rs = getCdCode(titaVo, defCode, code, desc, eCode, r, c);
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

	private String getCdCode(TitaVo titaVo, String defCode, String code, String desc, String eCode, int r, int c)
			throws LogicException {
		CdCodeId cdCodeId = new CdCodeId();
		cdCodeId.setDefCode(defCode);
		cdCodeId.setCode(code);
		CdCode cdCode = sCdCodeService.findById(cdCodeId, titaVo);
		if (cdCode == null) {
			setError(r, c);
		}
		return cdCode.getItem();
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