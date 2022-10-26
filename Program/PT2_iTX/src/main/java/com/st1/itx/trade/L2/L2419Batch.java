package com.st1.itx.trade.L2;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.domain.ClBatch;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClBatchService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2419Batch")
@Scope("prototype")
/**
 * 擔保品整批匯入,寫入擔保品資料
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L2419Batch extends TradeBuffer {

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private WebClient webClient;

	@Autowired
	private MakeExcel makeExcel;

	@Autowired
	private ClImmService sClImmService;
	@Autowired
	private ClMainService sClMainService;
	@Autowired
	private CdCodeService sCdCodeService;
	@Autowired
	private CustMainService sCustMainService;
	@Autowired
	private CdCityService sCdCityService;
	@Autowired
	private CdAreaService sCdAreaService;
	@Autowired
	private ClBuildingService sClBuildingService;
	@Autowired
	private ClBuildingOwnerService sClBuildingOwnerService;
	@Autowired
	private ClLandService sClLandService;
	@Autowired
	private ClLandOwnerService sClLandOwnerService;
	@Autowired
	private ClFacService sClFacService;
	@Autowired
	private FacMainService sFacMainService;
	@Autowired
	private FacCaseApplService sFacCaseApplService;
	@Autowired
	private CdLandSectionService sCdLandSectionService;
	@Autowired
	private InsuOrignalService sInsuOrignalService;

	@Autowired
	private ClFacCom clFacCom;

	@Autowired
	private ClBatchService sClBatchService;

	private List<ClBatch> listClBatch;

	private int applNo = 0;
	private String evaCompany = null;
	private int evaDate = 0;
	private String groupNo = null;

	private int row = 1;

	TitaVo batchTitaVo = null;

	private List<Map<String, String>> lastOwnerList = null;

	private Map<String, String> zipItems = new HashMap<>();

	private int custNo = 0;

	private int facmNo = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2419Batch ");
		this.totaVo.init(titaVo);

		batchTitaVo = titaVo;

		zipItems = new HashMap<>();

		openFeedbackExcel(); // openExcel

		boolean isFirstLoop = true;

		for (ClBatch tClBatch : listClBatch) {
			// 只需讀第一筆資料
			if (isFirstLoop) {
				this.info("FirstLoop");
				applNo = tClBatch.getApplNo();
				evaCompany = tClBatch.getEvaCompany();
				evaDate = tClBatch.getEvaDate();
				groupNo = tClBatch.getGroupNo();
				this.info("applNo = " + applNo);
				this.info("evaCompany = " + evaCompany);
				this.info("evaDate = " + evaDate);
				this.info("groupNo = " + groupNo);

				getCustNoAndFacmNo();

				isFirstLoop = false;
			}
			int seq = tClBatch.getSeq();
			int clCode1 = tClBatch.getClCode1();
			int clCode2 = tClBatch.getClCode2();
			int clNo = tClBatch.getClNo();

			// seq + 2 = excel row number
			this.info("seq = " + seq);
			this.info("clCode1 = " + clCode1);
			this.info("clCode2 = " + clCode2);
			this.info("clNo = " + clNo);

			row = seq + 2;

			String seqInExcel = makeExcel.getValue(row, 1).toString(); // column A
			// column B
			String clCode1InExcel = getCodeAndCheck(makeExcel.getValue(row, 2).toString(), "B", "ClCode1", "擔保品代號1");
			// column C
			String clCode2InExcel = getCodeAndCheck(makeExcel.getValue(row, 3).toString(), "C",
					"ClCode2" + clCode1InExcel, "擔保品代號2");
			String clNoInExcel = makeExcel.getValue(row, 4).toString(); // column D

			this.info("seqInExcel = " + seqInExcel);
			this.info("clCode1InExcel = " + clCode1InExcel);
			this.info("clCode2InExcel = " + clCode2InExcel);
			this.info("clNoInExcel = " + clNoInExcel);

			if (parseInt(seqInExcel) != seq) {
				throwErrorMsg("E0015", "欄位:A ,行數:" + row + ",回饋檔的明細序號,與擔保品整批匯入檔資料不符,請使用L2419功能1,重新產生回饋檔.");
			}
			if (parseInt(clCode1InExcel) != clCode1) {
				throwErrorMsg("E0015", "欄位:B ,行數:" + row + ",回饋檔的擔保品代號1,與擔保品整批匯入檔資料不符,請使用L2419功能1,重新產生回饋檔.");
			}
			if (parseInt(clCode2InExcel) != clCode2) {
				throwErrorMsg("E0015", "欄位:C ,行數:" + row + ",回饋檔的擔保品代號2,與擔保品整批匯入檔資料不符,請使用L2419功能1,重新產生回饋檔.");
			}
			if (parseInt(clNoInExcel) != clNo) {
				throwErrorMsg("E0015", "欄位:D ,行數:" + row + ",回饋檔的擔保品號碼,與擔保品整批匯入檔資料不符,請使用L2419功能1,重新產生回饋檔.");
			}

			// column E 擔保品類別
			String clTypeCode = getCodeAndCheck(makeExcel.getValue(row, 5).toString(), "E", "ClTypeCode", "擔保品類別");

			// 房地才需要寫入建號
			// column F 建號-前5碼
			String buildNo1 = clCode1 == 1 ? leftPadZero(makeExcel.getValue(row, 6).toString(), 5) : "";
			// column G 建號-後3碼
			String buildNo2 = clCode1 == 1 ? leftPadZero(makeExcel.getValue(row, 7).toString(), 3) : "";

			// 房地跟土地都需要寫入地號
			// column H 地號-前4碼
			String landNo1 = leftPadZero(makeExcel.getValue(row, 8).toString(), 4);
			// column I 地號-後4碼
			String landNo2 = leftPadZero(makeExcel.getValue(row, 9).toString(), 4);

			// column J // 郵遞區號
			String zipCode = makeExcel.getValue(row, 10).toString();
			String cityCode = getZipItem("zipcity=" + zipCode);
			String areaCode = "";
			String cityItem = "";
			String areaItem = "";

			if (cityCode.isEmpty()) {

				CdArea cdArea = sCdAreaService.Zip3First(zipCode, titaVo);

				if (cdArea == null) {
					throwErrorMsg("E0015", "欄位:J ,行數:" + row + ",郵遞區號無法配對到鄉鎮市區代碼,郵遞區號=" + zipCode);
				}

				CdCity cdCity = sCdCityService.findById(cdArea.getCityCode(), titaVo);
				if (cdCity == null) {
					throwErrorMsg("E0015",
							"欄位:J ,行數:" + row + ",郵遞區號對應之縣市別代碼無法配對到正確的縣市代碼檔,縣市別代碼=" + cdArea.getCityCode());
				}

				zipItems.put("zipcity=" + zipCode, cdArea.getCityCode());
				zipItems.put("city=" + cdArea.getCityCode(), cdCity.getCityItem());
				zipItems.put("ziparea=" + zipCode, cdArea.getAreaCode());
				zipItems.put("area=" + cdArea.getCityCode() + "-" + cdArea.getAreaCode(), cdArea.getAreaItem());

				// 縣市
				cityCode = cdArea.getCityCode();
				// 鄉鎮市區
				areaCode = cdArea.getAreaCode();
				cityItem = cdCity.getCityItem();
				areaItem = cdArea.getAreaItem();
			} else {
				// 縣市
				// 鄉鎮市區
				areaCode = getZipItem("ziparea=" + zipCode);
				cityItem = getZipItem("city=" + cityCode);
				areaItem = getZipItem("area=" + cityCode + "-" + areaCode);
			}
			// column K 地段4碼
			String landSectionCode = leftPadZero(makeExcel.getValue(row, 11).toString(), 4);

			// 房地才需要輸入門牌
			// column L 門牌
			String address = clCode1 == 1 ? makeExcel.getValue(row, 12).toString() : "";

			// 房地才需要輸入用途
			// column M 用途
			String usage = clCode1 == 1
					? getCodeAndCheck(makeExcel.getValue(row, 13).toString(), "M", "BdMainUseCode", "用途")
					: "";

			// 房地才需要輸入建物類別
			// column N 類別
			String buildingTypeCode = clCode1 == 1
					? getCodeAndCheck(makeExcel.getValue(row, 14).toString(), "N", "BdTypeCode", "建物類別")
					: "";

			// 房地才需要輸入建材
			// column O 建材
			String buildingMtrlCode = clCode1 == 1
					? getCodeAndCheck(makeExcel.getValue(row, 15).toString(), "O", "BdMtrlCode", "建材")
					: "";

			// 房地才需要輸入樓層
			// column P 樓層
			String floorNo = clCode1 == 1 ? makeExcel.getValue(row, 16).toString() : "";

			// 房地才需要輸入總樓層
			// column Q 總樓層
			String totalFloor = clCode1 == 1 ? makeExcel.getValue(row, 17).toString() : "";

			// 房地才需要輸入建築完成日期
			// column R 建物完成日期
			String buildDate = clCode1 == 1 ? makeExcel.getValue(row, 18).toString() : "";
			if (clCode1 == 1 && !checkDate(buildDate)) {
				throwErrorMsg("E0015", "欄位:R ,行數:" + row + ",不是一個正確的日期,建築完成日期=" + buildDate);
			}
			if (clCode1 == 1 && !compareDate(buildDate, titaVo.getParam("CALDY"))) {
				throwErrorMsg("E0015", "欄位:R ,行數:" + row + ",建築完成日期(" + buildDate + "),不可大於日曆日");
			}

			// column S 設定日期
			String settingDate = makeExcel.getValue(row, 19).toString();
			if (!checkDate(settingDate)) {
				throwErrorMsg("E0015", "欄位:S ,行數:" + row + ",不是一個正確的日期,設定日期=" + settingDate);
			}
			if (!compareDate(settingDate, titaVo.getParam("CALDY"))) {
				throwErrorMsg("E0015", "欄位:S ,行數:" + row + ",設定日期(" + settingDate + "),不可大於日曆日");
			}

			// column T 面積
			String floorArea = makeExcel.getValue(row, 20).toString();
			BigDecimal floorAreaBigDecimal = new BigDecimal(floorArea);
			if (floorAreaBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				throwErrorMsg("E0015", "欄位:T ,行數:" + row + ",面積不得為0或空白,面積=" + floorArea);
			}

			// column U 鑑估單價
			String evaUnitPrice = makeExcel.getValue(row, 21).toString();
			BigDecimal evaUnitPriceBigDecimal = new BigDecimal(evaUnitPrice);
			if (evaUnitPriceBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				throwErrorMsg("E0015", "欄位:U ,行數:" + row + ",鑑估單價不得為0或空白,鑑估單價=" + evaUnitPrice);
			}

			// column V 鑑估總價
			String evaAmt = makeExcel.getValue(row, 22).toString();
			BigDecimal evaAmtBigDecimal = new BigDecimal(evaAmt);
			if (evaAmtBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				throwErrorMsg("E0015", "欄位:V ,行數:" + row + ",鑑估總價不得為0或空白,鑑估總價=" + evaAmt);
			}

			// column W 增值稅
			String tax = makeExcel.getValue(row, 23).toString();
			if (tax != null && !tax.isEmpty()) {
				try {
					new BigDecimal(tax);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:W ,行數:" + row + ",增值稅有輸入時需為數字,增值稅=" + tax);
				}
			}

			// column X 淨值
			String netValue = makeExcel.getValue(row, 24).toString();
			if (netValue != null && !netValue.isEmpty()) {
				try {
					new BigDecimal(netValue);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:X ,行數:" + row + ",淨值有輸入時需為數字,淨值=" + netValue);
				}
			}

			// column Y 押金
			String rentPrice = makeExcel.getValue(row, 25).toString();
			if (rentPrice != null && !rentPrice.isEmpty()) {
				try {
					new BigDecimal(rentPrice);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:Y ,行數:" + row + ",押金有輸入時需為數字,押金=" + rentPrice);
				}
			}

			// column Z 出租淨值
			String rentEvaValue = makeExcel.getValue(row, 26).toString();
			if (rentEvaValue != null && !rentEvaValue.isEmpty()) {
				try {
					new BigDecimal(rentEvaValue);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:Z ,行數:" + row + ",出租淨值有輸入時需為數字,出租淨值=" + rentEvaValue);
				}
			}

			// column AA 貸放成數(%)
			String loanToValue = makeExcel.getValue(row, 27).toString();
			BigDecimal loanToValueBigDecimal = null;
			try {
				loanToValueBigDecimal = new BigDecimal(loanToValue);
			} catch (Exception e) {
				throwErrorMsg(e, "E0015", "欄位:AA ,行數:" + row + ",貸放成數不得為0或空白,貸放成數=" + loanToValue);
			}
			if (loanToValueBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				throwErrorMsg("E0015", "欄位:AA ,行數:" + row + ",貸放成數不得為0或空白,貸放成數=" + loanToValue);
			}

			// column AB 借款金額(仟元)
			String loanAmt = makeExcel.getValue(row, 28).toString();
			if (loanAmt != null && !loanAmt.isEmpty()) {
				try {
					new BigDecimal(loanAmt);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:AB ,行數:" + row + ",借款金額有輸入時需為數字,借款金額=" + loanAmt);
				}
			}

			// column AC 設定金額(仟元)
			String settingAmt = makeExcel.getValue(row, 29).toString();
			BigDecimal settingAmtBigDecimal = null;
			try {
				settingAmtBigDecimal = new BigDecimal(settingAmt);
			} catch (Exception e) {
				throwErrorMsg(e, "E0015", "欄位:AC ,行數:" + row + ",設定金額不得為0或空白,設定金額=" + settingAmt);
			}
			if (settingAmtBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				throwErrorMsg("E0015", "欄位:AC ,行數:" + row + ",設定金額不得為0或空白,設定金額=" + settingAmt);
			}

			// column AD 還款金額(仟元)
			String repayAmt = makeExcel.getValue(row, 30).toString();
			if (repayAmt != null && !repayAmt.isEmpty()) {
				try {
					new BigDecimal(repayAmt);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:AD ,行數:" + row + ",借款金額有輸入時需為數字,借款金額=" + repayAmt);
				}
			}

			// column AE 保險單號碼
			String insuNo = makeExcel.getValue(row, 31).toString();
			if (insuNo == null || insuNo.isEmpty()) {
				throwErrorMsg("E0015", "欄位:AE ,行數:" + row + ",保險單號碼不得為空白.");
			}

			// column AF 保險公司
			String insuCompany = getCodeAndCheck(makeExcel.getValue(row, 32).toString(), "AF", "InsuCompany", "保險公司");

			// column AG 保險類別
			String insuTypeCode = getCodeAndCheck(makeExcel.getValue(row, 33).toString(), "AF", "InsuTypeCode", "保險類別");

			// column AH 火災險保險金額(仟元)
			String fireInsuAmt = makeExcel.getValue(row, 34).toString();
			BigDecimal fireInsuAmtBigDecimal = null;
			try {
				fireInsuAmtBigDecimal = new BigDecimal(fireInsuAmt);
			} catch (Exception e) {
				throwErrorMsg(e, "E0015", "欄位:AH ,行數:" + row + ",火災險保險金額不得為0或空白,火災險保險金額=" + fireInsuAmt);
			}
			if (fireInsuAmtBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				throwErrorMsg("E0015", "欄位:AH ,行數:" + row + ",火災險保險金額不得為0或空白,火災險保險金額=" + fireInsuAmt);
			}

			// column AI 火災險保費
			String fireInsuExpense = makeExcel.getValue(row, 35).toString();
			if (fireInsuExpense != null && !fireInsuExpense.isEmpty()) {
				try {
					new BigDecimal(fireInsuExpense);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:AI ,行數:" + row + ",火災險保費有輸入時需為數字,火災險保費=" + fireInsuExpense);
				}
			}

			// column AJ 地震險保險金額(仟元)
			String earthquakeInsuAmt = makeExcel.getValue(row, 36).toString();
			if (earthquakeInsuAmt != null && !earthquakeInsuAmt.isEmpty()) {
				try {
					new BigDecimal(earthquakeInsuAmt);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:AI ,行數:" + row + ",地震險保險金額有輸入時需為數字,地震險保險金額=" + earthquakeInsuAmt);
				}
			}

			// column AK 地震險保費
			String earthquakeInsuExpense = makeExcel.getValue(row, 37).toString();
			if (earthquakeInsuExpense != null && !earthquakeInsuExpense.isEmpty()) {
				try {
					new BigDecimal(earthquakeInsuExpense);
				} catch (Exception e) {
					throwErrorMsg(e, "E0015", "欄位:AI ,行數:" + row + ",地震險保費有輸入時需為數字,地震險保費=" + earthquakeInsuExpense);
				}
			}

			// column AL 保險起日
			String insuStartDate = makeExcel.getValue(row, 38).toString();
			if (!checkDate(insuStartDate)) {
				throwErrorMsg("E0015", "欄位:AL ,行數:" + row + ",不是一個正確的日期,保險起日=" + insuStartDate);
			}
			if (!compareDate(insuStartDate, titaVo.getParam("CALDY"))) {
				throwErrorMsg("E0015", "欄位:AL ,行數:" + row + ",保險起日(" + insuStartDate + "),不可大於日曆日");
			}

			// column AM 保險迄日
			String insuEndDate = makeExcel.getValue(row, 39).toString();
			if (!checkDate(insuEndDate)) {
				throwErrorMsg("E0015", "欄位:AM ,行數:" + row + ",不是一個正確的日期,checkDate=" + insuEndDate);
			}

			// 所有權人
			// 2022-10-18 Wei 修改 from 2022-10-17 會議
			// 所有權人1必須輸入，可以只輸入第一筆，留空白者複製前一筆所有權人資料
			// 需判斷擔保品代號1為1房地時,所有權人1需填建物所有權人
			// 當所有權人1的種類為空時,取前一筆資料的全部所有權人資料,若無前一筆資料的全部所有權人資料時,須給錯誤提示

			// column AN 所有權人1-所有權種類
			String ownerType1 = makeExcel.getValue(row, 40).toString();

			if (ownerType1 == null || ownerType1.isEmpty() || ownerType1.indexOf("_") < 0) {
				// CASE 1: 所有權人1-所有權種類為空白
				// 此情形需確認上一筆所有權人資料是否存在
				// 若不存在須提示錯誤
				if (lastOwnerList == null || lastOwnerList.isEmpty()) {
					throwErrorMsg("E0015", "欄位:AN ,行數:" + row + ",無前筆資料時,所有權人1不得為空白.");
				} else {
					// 沿用上一筆所有權人資料時,此筆不繼續往右讀取
					this.info("行數:" + row + ",沿用上一筆所有權人資料時,此筆不繼續往右讀取");
				}
			} else {
				ownerType1 = ownerType1.substring(0, ownerType1.indexOf("_"));

				// CASE 2: 所有權人-所有權種類不為空白
				// 此情形須清空lastOwnerList
				// 用此筆資料建立lastOwnerList
				lastOwnerList = new ArrayList<>();

				// 檢核 所有權種類 1:建物;2:土地
				if (clCode1 == 1 && !ownerType1.equals("1")) {
					throwErrorMsg("E0015", "欄位:AN ,行數:" + row + ",此筆為房地擔保品,所有權人1須填寫建物所有權人.");
				} else if (clCode1 == 2 && !ownerType1.equals("2")) {
					throwErrorMsg("E0015", "欄位:AN ,行數:" + row + ",此筆為土地擔保品,所有權人1須填寫土地所有權人.");
				} else if (!ownerType1.equals("1") && !ownerType1.equals("2")) {
					throwErrorMsg("E0015", "欄位:AN ,行數:" + row + ",所有權人1-所有權種類應為1_建物或2_土地,所有權種類=" + ownerType1);
				}

				// column AO 所有權人1-身分證/統編
				String ownerId1 = makeExcel.getValue(row, 41).toString().trim();
				if (ownerId1 == null || ownerId1.isEmpty()) {
					throwErrorMsg("E0015", "欄位:AO ,行數:" + row + ",有選擇所有權種類時,所有權人-身分證/統編不得為空白.");
				}

				// column AP 所有權人1-姓名
				String ownerName1 = makeExcel.getValue(row, 42).toString().trim();
				if (ownerName1 == null || ownerName1.isEmpty()) {
					throwErrorMsg("E0015", "欄位:AP ,行數:" + row + ",有選擇所有權種類時,所有權人-姓名不得為空白.");
				}

				// column AQ 所有權人1-與授信戶關係
				String ownerRel1 = makeExcel.getValue(row, 43).toString();
				if (ownerRel1 != null && !ownerRel1.isEmpty() && ownerRel1.indexOf("_") >= 0) {
					if (ownerRel1.substring(0, ownerRel1.indexOf("_")).equals("00")) {
						ownerRel1 = ownerRel1.substring(0, ownerRel1.indexOf("_"));
					} else {
						ownerRel1 = getCodeAndCheck(ownerRel1, "AQ", "GuaRelCode", "與授信戶關係");
					}
				} else {
					throwErrorMsg("E0015", "欄位:AQ ,行數:" + row + ",有選擇所有權種類時,所有權人-與授信戶關係不得為空白.");
				}

				// column AR 所有權人1-持份比率
				String part = makeExcel.getValue(row, 44).toString();
				if (part == null || part.isEmpty()) {
					throwErrorMsg("E0015", "欄位:AR ,行數:" + row + ",有選擇所有權種類時,所有權人-持份比率不得為空白.");
				}
				if (part.indexOf("分之") >= 0) {
					String ownerTotal = part.substring(0, part.indexOf("分之")); // 持份比率(分母)
					String ownerPart = part.substring(part.indexOf("分之") + 2); // 持份比率(分子)
					this.info("ownerPart = " + ownerPart);
					this.info("ownerTotal = " + ownerTotal);
				} else {
					throwErrorMsg("E0015", "欄位:AR ,行數:" + row + ",所有權人-持份比率格式為幾分之幾,可參考權狀上的權利範圍.");
				}

				// 所有權人1 檢核皆通過,紀錄到lastOwnerList
				Map<String, String> owner = new HashMap<>();
				owner.put("type", ownerType1);
				owner.put("id", ownerId1);
				owner.put("name", ownerName1);
				owner.put("rel", ownerRel1);
				owner.put("part", part);
				lastOwnerList.add(owner);

				checkElseOwner(row);
			}

			// 檢核完畢 寫入DB

			// Table 1 : ClMain
			boolean isExistInClMain = false;
			ClMainId clMainId = new ClMainId();
			clMainId.setClCode1(clCode1);
			clMainId.setClCode2(clCode2);
			clMainId.setClNo(clNo);

			ClMain clMain;
			clMain = sClMainService.holdById(clMainId, titaVo);

			if (clMain == null) {
				clMain = new ClMain();
				clMain.setClMainId(clMainId);
			} else {
				isExistInClMain = true;
			}

			// 擔保品類別代碼
			clMain.setClTypeCode(clTypeCode);
			// 地區別
			clMain.setCityCode(cityCode);
			// 鄉鎮區
			clMain.setAreaCode(areaCode);
			// 擔保品狀況碼-1:已抵押
			clMain.setClStatus("1");
			// 鑑估日期
			clMain.setEvaDate(evaDate);
			// 鑑估總值
			clMain.setEvaAmt(evaAmtBigDecimal);

			// 2022/7/29新增
			// 計算可分配金額
			BigDecimal shareTotal;
			BigDecimal shareCompAmt;
			BigDecimal wkEvaAmt = evaAmtBigDecimal; // 鑑估總值
			BigDecimal wkEvaNetWorth = parse.stringToBigDecimal(netValue); // 評估淨值
			// 評估淨值有值時擺評估淨值,否則擺鑑估總值.
			if (wkEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
				shareCompAmt = wkEvaNetWorth;
			} else {
				shareCompAmt = wkEvaAmt;
			}
			this.info("L2419 shareCompAmt = " + shareCompAmt);
			this.info("L2419 LoanToValue = " + loanToValue);
			this.info("L2419 SettingAmt = " + settingAmt);
//			"1.若""評估淨值""有值取""評估淨值""否則取""鑑估總值"")*貸放成數(四捨五入至個位數)
//			2.若設定金額低於可分配金額則為設定金額
//			3.擔保品塗銷/解除設定時(該筆擔保品的可分配金額設為零)"
//			若貸放成數為0則不乘貸放成數
			if (loanToValueBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
				shareTotal = shareCompAmt;
			} else {
				shareTotal = shareCompAmt.multiply(loanToValueBigDecimal).divide(new BigDecimal(100)).setScale(0,
						BigDecimal.ROUND_HALF_UP);
			}
			if (parse.stringToBigDecimal(settingAmt).compareTo(shareTotal) < 0) {
				shareTotal = parse.stringToBigDecimal(settingAmt);
			}
			clMain.setShareTotal(shareTotal);

			if (isExistInClMain) {
				try {
					sClMainService.update2(clMain, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0007", "擔保品主檔(ClMain)" + e.getErrorMsg());
				}
			} else {
				try {
					sClMainService.insert(clMain, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0005", "擔保品主檔(ClMain)" + e.getErrorMsg());
				}
			}

			// Table 2 : ClImm
			boolean isExistInClImm = false;
			ClImmId clImmId = new ClImmId();

			clImmId.setClCode1(clCode1);
			clImmId.setClCode2(clCode2);
			clImmId.setClNo(clNo);

			ClImm clImm;
			clImm = sClImmService.holdById(clImmId, titaVo);

			if (clImm == null) {
				clImm = new ClImm();
				clImm.setClImmId(clImmId);
			} else {
				isExistInClImm = true;
			}
			// 評估淨值
			clImm.setEvaNetWorth(parse.stringToBigDecimal(netValue));
			// 土地增值稅
			clImm.setLVITax(parse.stringToBigDecimal(tax));
			// 出租評估淨值
			clImm.setRentEvaValue(parse.stringToBigDecimal(rentEvaValue));
			// 押租金
			clImm.setRentPrice(parse.stringToBigDecimal(rentPrice));
			// 權利種類:1:抵押權
			clImm.setOwnershipCode("1");
			// 抵押權註記:0:最高限額抵押權
			clImm.setMtgCode("0");
			// 最高限額抵押權之擔保債權種類-票據
			clImm.setMtgCheck("Y");
			// 最高限額抵押權之擔保債權種類-借款
			clImm.setMtgLoan("Y");
			// 最高限額抵押權之擔保債權種類-保證債務
			clImm.setMtgPledge("Y");
			// 檢附同意書
			clImm.setAgreement("Y");
			// 鑑價公司
			clImm.setEvaCompanyCode(evaCompany);
			// 擔保註記:1:擔保
			clImm.setClCode("1");
			// 貸放成數(%)
			clImm.setLoanToValue(parse.stringToBigDecimal(loanToValue));
			// 設定狀態:1:設定
			clImm.setSettingStat("1");
			// 擔保品狀態:0:正常
			clImm.setClStat("0");
			// 設定日期
			clImm.setSettingDate(getDate(settingDate));
			// 設定金額
			clImm.setSettingAmt(parse.stringToBigDecimal(settingAmt));
			// 擔保債權確定日期:設定日期+30年
			clImm.setClaimDate(clImm.getSettingDate() + 300000);
			// 設定順位(1~9)
			clImm.setSettingSeq("1");

			if (isExistInClImm) {
				try {
					sClImmService.update2(clImm, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0007", "擔保品不動產檔(ClImm)" + e.getErrorMsg());
				}
			} else {
				try {
					sClImmService.insert(clImm, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0005", "擔保品不動產檔(ClImm)" + e.getErrorMsg());
				}
			}

			// Table 3 : ClBuilding
			if (clCode1 == 1) {
				boolean isExistInClBuilding = false;
				ClBuildingId clBuildingId = new ClBuildingId();
				clBuildingId.setClCode1(clCode1);
				clBuildingId.setClCode2(clCode2);
				clBuildingId.setClNo(clNo);

				ClBuilding clBuilding;
				clBuilding = sClBuildingService.holdById(clBuildingId, titaVo);
				if (clBuilding == null) {
					clBuilding = new ClBuilding();
					clBuilding.setClBuildingId(clBuildingId);
				} else {
					isExistInClBuilding = true;
				}

				// 縣市
				clBuilding.setCityCode(cityCode);
				// 鄉鎮市區
				clBuilding.setAreaCode(areaCode);
				// 段小段代碼
				clBuilding.setIrCode(landSectionCode);
				// 路名
				clBuilding.setRoad(address);
				// 建物門牌
				clBuilding.setBdLocation(cityItem + areaItem + address);
				// 建號
				clBuilding.setBdNo1(buildNo1);
				clBuilding.setBdNo2(buildNo2);
				// 建物主要用途
				clBuilding.setBdMainUseCode(usage);
				// 建物使用別:6:其他
				clBuilding.setBdUsageCode("6");
				// 建物主要建材
				clBuilding.setBdMtrlCode(buildingMtrlCode);
				// 建物類別
				clBuilding.setBdTypeCode(buildingTypeCode);
				// 總樓層
				clBuilding.setTotalFloor(parse.stringToInteger(totalFloor));
				// 擔保品所在樓層
				clBuilding.setFloorNo(floorNo);
				// 擔保品所在樓層面積
				clBuilding.setFloorArea(parse.stringToBigDecimal(floorArea));
				// 鑑價單價/坪
				clBuilding.setEvaUnitPrice(parse.stringToBigDecimal(evaUnitPrice));
				// 屋頂結構:07:其他 ???
				clBuilding.setRoofStructureCode("07");
				// 建築完成日期
				clBuilding.setBdDate(getDate(buildDate));
				// 房屋取得日期,預設為"建築完成日期"
				clBuilding.setHouseBuyDate(clBuilding.getBdDate());

				if (isExistInClBuilding) {
					try {
						sClBuildingService.update2(clBuilding, titaVo);
					} catch (DBException e) {
						throwErrorMsg(e, "E0007", "擔保品不動產建物檔主檔(ClBuilding)" + e.getErrorMsg());
					}
				} else {
					try {
						sClBuildingService.insert(clBuilding, titaVo);
					} catch (DBException e) {
						throwErrorMsg(e, "E0005", "擔保品不動產建物檔主檔(ClBuilding)" + e.getErrorMsg());
					}
				}
			}
			// Table 4 : ClLand
			boolean isExistInClLand = false;
			ClLandId clLandId = new ClLandId();
			clLandId.setClCode1(clCode1);
			clLandId.setClCode2(clCode2);
			clLandId.setClNo(clNo);

			ClLand clLand;
			clLand = sClLandService.holdById(clLandId, titaVo);
			if (clLand == null) {
				clLand = new ClLand();
				clLand.setClLandId(clLandId);
			} else {
				isExistInClLand = true;
			}

			// 土地序號(固定放000)
			clLand.setLandSeq(clCode1 == 1 ? 1 : 0);
			// 縣市
			clLand.setCityCode(cityCode);
			// 鄉鎮市區
			clLand.setAreaCode(areaCode);
			// 段小段代碼
			clLand.setIrCode(landSectionCode);
			// 地號
			clLand.setLandNo1(landNo1);
			clLand.setLandNo2(landNo2);
			// 土地座落
			String landLocation = cityItem + areaItem + getIrItem(cityCode, areaCode, landSectionCode);
			clLand.setLandLocation(landLocation);
			// 土地增值稅
			clLand.setLVITax(parse.stringToBigDecimal(tax));
			// 鑑價單價/坪
			clLand.setEvaUnitPrice(parse.stringToBigDecimal(evaUnitPrice));
			// 面積
			clLand.setArea(parse.stringToBigDecimal(floorArea));

			if (isExistInClLand) {
				try {
					sClLandService.update2(clLand, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0007", "擔保品不動產建土地主檔(ClLand)" + e.getErrorMsg());
				}
			} else {
				try {
					sClLandService.insert(clLand, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0005", "擔保品不動產建土地主檔(ClLand)" + e.getErrorMsg());
				}
			}

			// Table 4 : InsuComm
			boolean isExistInClFac = false;
			ClFacId clFacId = new ClFacId();
			clFacId.setClCode1(clCode1);
			clFacId.setClCode2(clCode2);
			clFacId.setClNo(clNo);
			clFacId.setApproveNo(applNo);

			ClFac clFac = sClFacService.holdById(clFacId, titaVo);
			if (clFac == null) {
				clFac = new ClFac();
				clFac.setClFacId(clFacId);
			} else {
				isExistInClFac = true;
			}

			clFac.setCustNo(custNo);
			clFac.setFacmNo(facmNo);
			if (isExistInClFac) {
				try {
					sClFacService.update2(clFac, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0007", "擔保品與額度關聯檔(ClFac)" + e.getErrorMsg());
				}
			} else {
				clFac.setOriSettingAmt(parse.stringToBigDecimal(settingAmt));
				try {
					sClFacService.insert(clFac, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0005", "擔保品與額度關聯檔(ClFac)" + e.getErrorMsg());
				}
			}

			// Table 6 : ClBuildingOwner
			// Table 7 : ClLandOwner
			insertOwners(clCode1, clCode2, clNo);

			// Table 8 : ClFac
			boolean isExistInInsuOrignal = false;
			InsuOrignalId insuOrignalId = new InsuOrignalId();
			insuOrignalId.setClCode1(clCode1);
			insuOrignalId.setClCode2(clCode2);
			insuOrignalId.setClNo(clNo);
			insuOrignalId.setOrigInsuNo(insuNo);

			InsuOrignal insuOrignal = sInsuOrignalService.holdById(insuOrignalId, titaVo);
			if (insuOrignal == null) {
				insuOrignal = new InsuOrignal();
				insuOrignal.setInsuOrignalId(insuOrignalId);
			} else {
				isExistInInsuOrignal = true;
			}

			insuOrignal.setInsuCompany(insuCompany);
			insuOrignal.setInsuTypeCode(insuTypeCode);
			insuOrignal.setFireInsuCovrg(parse.stringToBigDecimal(fireInsuAmt));
			insuOrignal.setEthqInsuCovrg(parse.stringToBigDecimal(earthquakeInsuAmt));
			insuOrignal.setFireInsuPrem(parse.stringToBigDecimal(fireInsuExpense));
			insuOrignal.setEthqInsuPrem(parse.stringToBigDecimal(earthquakeInsuExpense));
			insuOrignal.setInsuStartDate(getDate(insuStartDate));
			insuOrignal.setInsuEndDate(getDate(insuEndDate));
			insuOrignal.setRemark("擔保品整批匯入,批號:" + groupNo);

			if (isExistInInsuOrignal) {
				try {
					sInsuOrignalService.update2(insuOrignal, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0007", "火險初保檔(InsuOrignal)" + e.getErrorMsg());
				}
			} else {
				try {
					sInsuOrignalService.insert(insuOrignal, titaVo);
				} catch (DBException e) {
					throwErrorMsg(e, "E0005", "火險初保檔(InsuOrignal)" + e.getErrorMsg());
				}
			}

			// 更新ClBatch
			tClBatch = sClBatchService.holdById(tClBatch, titaVo);
			tClBatch.setInsertStatus(1);
			try {
				sClBatchService.update2(tClBatch, titaVo);
			} catch (DBException e) {
				throwErrorMsg(e, "E0007", "擔保品整批匯入檔(ClBatch)" + e.getErrorMsg());
			}
		}
		// 額度與擔保品關聯檔變動處理
		clFacCom.changeClFac(applNo, titaVo);

		// 連動L2038查詢擔保品寫入資料 用核准號碼
		String bufferL2038 = leftPadZero(applNo, 21);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L2038", bufferL2038, "", titaVo);

		this.batchTransaction.commit();
		return null;
	}

	private void throwErrorMsg(String errorCode, String errorMsg) throws LogicException {
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", batchTitaVo.getTlrNo(), "Y", "L2419", "",
				"[" + errorCode + "] " + errorMsg, batchTitaVo);
		throw new LogicException(errorCode, errorMsg);
	}

	private void throwErrorMsg(Exception e, String errorCode, String errorMsg) throws LogicException {
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", batchTitaVo.getTlrNo(), "Y", "L2419", "",
				"[" + errorCode + "] " + errorMsg, batchTitaVo);
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		this.error("L2419Batch error = " + errors.toString());
		throw new LogicException(errorCode, errorMsg);
	}

	private int parseInt(String input) {
		BigDecimal result = BigDecimal.ZERO;
		if (input == null || input.isEmpty()) {
			this.warn("getBigDecimal inputString is null or empty");
		} else {
			try {
				result = new BigDecimal(input);
			} catch (NumberFormatException e) {
				this.error("parstInt input : \"" + input + "\" parse to BigDecimal has NumberFormatException.");
				result = BigDecimal.ZERO;
			}
		}
		return result.intValue();
	}

	private void getCustNoAndFacmNo() throws LogicException {

		FacCaseAppl facCaseAppl = sFacCaseApplService.findById(applNo, batchTitaVo);
		if (facCaseAppl == null) {
			throwErrorMsg("E2019", "核准號碼 = " + applNo);
		}

		FacMain facMain = sFacMainService.facmApplNoFirst(applNo, batchTitaVo);
		if (facMain != null) {
			custNo = facMain.getCustNo();
			facmNo = facMain.getFacmNo();
		}
	}

	private void insertOwners(int clCode1, int clCode2, int clNo) throws LogicException {
		for (Map<String, String> owner : lastOwnerList) {
			String type = owner.get("type");
			String id = owner.get("id");
			String name = owner.get("name");
			String rel = owner.get("rel");
			String part = owner.get("part");

			String custUkey = ensureExistCustMain(id, name);

			if (type.equals("1")) {
				// 建物所有權人
				insertBuildingOwner(clCode1, clCode2, clNo, custUkey, rel, part);
			} else if (type.equals("2")) {
				// 土地所有權人
				insertLandOwner(clCode1, clCode2, clNo, custUkey, rel, part);
			}
		}
	}

	private void insertLandOwner(int clCode1, int clCode2, int clNo, String custUkey, String rel, String part)
			throws LogicException {
		boolean isExistInClLandOwner = false;
		ClLandOwnerId clLandOwnerId = new ClLandOwnerId();
		clLandOwnerId.setClCode1(clCode1);
		clLandOwnerId.setClCode2(clCode2);
		clLandOwnerId.setClNo(clNo);
		clLandOwnerId.setOwnerCustUKey(custUkey);

		ClLandOwner clLandOwner = sClLandOwnerService.holdById(clLandOwnerId, batchTitaVo);
		if (clLandOwner == null) {
			clLandOwner = new ClLandOwner();
			clLandOwner.setClLandOwnerId(clLandOwnerId);
		} else {
			isExistInClLandOwner = true;
		}

		if (clCode1 == 1) {
			clLandOwner.setLandSeq(1);
		} else {
			clLandOwner.setLandSeq(0);
		}

		// 與授信戶關係
		clLandOwner.setOwnerRelCode(rel);
		// 持份比率(分子)
		clLandOwner.setOwnerPart(parse.stringToBigDecimal(part.substring(0, part.indexOf("分之"))));
		// 持份比率(分母)
		clLandOwner.setOwnerTotal(parse.stringToBigDecimal(part.substring(part.indexOf("分之") + 2)));

		if (isExistInClLandOwner) {
			try {
				sClLandOwnerService.update2(clLandOwner, batchTitaVo);
			} catch (DBException e) {
				throwErrorMsg(e, "E0007", "擔保品-土地所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
			}
		} else {
			try {
				sClLandOwnerService.insert(clLandOwner, batchTitaVo);
			} catch (DBException e) {
				throwErrorMsg(e, "E0005", "擔保品-土地所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
			}
		}
	}

	private void insertBuildingOwner(int clCode1, int clCode2, int clNo, String custUkey, String rel, String part)
			throws LogicException {
		boolean isExistInClBuildingOwner = false;
		ClBuildingOwnerId clBuildingOwnerId = new ClBuildingOwnerId();
		clBuildingOwnerId.setClCode1(clCode1);
		clBuildingOwnerId.setClCode2(clCode2);
		clBuildingOwnerId.setClNo(clNo);
		clBuildingOwnerId.setOwnerCustUKey(custUkey);

		ClBuildingOwner clBuildingOwner = sClBuildingOwnerService.holdById(clBuildingOwnerId, batchTitaVo);
		if (clBuildingOwner == null) {
			clBuildingOwner = new ClBuildingOwner();
			clBuildingOwner.setClBuildingOwnerId(clBuildingOwnerId);
		} else {
			isExistInClBuildingOwner = true;
		}

		// 與授信戶關係
		clBuildingOwner.setOwnerRelCode(rel);
		// 持份比率(分子)
		clBuildingOwner.setOwnerPart(parse.stringToBigDecimal(part.substring(0, part.indexOf("分之"))));
		// 持份比率(分母)
		clBuildingOwner.setOwnerTotal(parse.stringToBigDecimal(part.substring(part.indexOf("分之") + 2)));

		if (isExistInClBuildingOwner) {
			try {
				sClBuildingOwnerService.update2(clBuildingOwner, batchTitaVo);
			} catch (DBException e) {
				throwErrorMsg(e, "E0007", "擔保品-建物所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
			}
		} else {
			try {
				sClBuildingOwnerService.insert(clBuildingOwner, batchTitaVo);
			} catch (DBException e) {
				throwErrorMsg(e, "E0005", "擔保品-建物所有權人檔(ClBuildingOwner)" + e.getErrorMsg());
			}
		}
	}

	private String ensureExistCustMain(String id, String name) throws LogicException {
		CustMain custMain = sCustMainService.custIdFirst(id, batchTitaVo);
		if (custMain == null) {
			String ukey = UUID.randomUUID().toString().toUpperCase().replace("-", "");
			custMain = new CustMain();
			custMain.setCustUKey(ukey);
			custMain.setCustId(id);
			custMain.setCustName(name);
			custMain.setDataStatus(1);
			custMain.setTypeCode(2);
			if (id.length() == 8) {
				custMain.setCuscCd("2");
			} else {
				custMain.setCuscCd("1");
			}
			try {
				sCustMainService.insert(custMain, batchTitaVo);
			} catch (DBException e) {
				throwErrorMsg(e, "E0005", "客戶資料主檔");
			}
		}
		return custMain.getCustUKey();
	}

	private String getCodeAndCheck(String inputString, String column, String defCode, String defCodeDesc)
			throws LogicException {
		if (inputString.indexOf("_") < 0) {
			return "";
		}
		String code = inputString.substring(0, inputString.indexOf("_")).trim();
		if (code != null && !code.isEmpty()) {
			getCdCode(code, defCode, column, defCodeDesc);
		}
		return code;
	}

	private String getCdCode(String code, String defCode, String column, String defCodeDesc) throws LogicException {
		CdCodeId cdCodeId = new CdCodeId();
		cdCodeId.setDefCode(defCode);
		cdCodeId.setCode(code);
		CdCode cdCode = sCdCodeService.findById(cdCodeId, batchTitaVo);
		if (cdCode == null) {
			throwErrorMsg("E0015",
					"欄位:" + column + " ,行數:" + row + " ,回饋檔的" + defCodeDesc + "(" + code + "),代碼錯誤,請依下拉選單選擇.");
		}
		return cdCode.getItem();
	}

	private void openFeedbackExcel() throws LogicException {
		this.info("openFeedbackExcel ... ");

		String fileItem = batchTitaVo.getParam("FileItem").trim();

		if (fileItem.isEmpty()) {
			throwErrorMsg("E0015", "請先設定擔保品明細表");
		}

		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + batchTitaVo.getTlrNo()
				+ File.separatorChar + fileItem;

		this.info("fileitem=" + fileItem);

		makeExcel.openExcel(fileName, "擔保品明細表");

		String groupNoOnExcelFileName = "";

		if (fileName.indexOf("_") >= 0) {
			groupNoOnExcelFileName = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
		} else {
			throwErrorMsg("E0015", "上傳的回饋檔檔名未含有系統產生的批次號碼,請使用L2419功能1所產生的回饋檔.");
		}

		this.info("groupNoOnExcelFileName=" + groupNoOnExcelFileName);

		Slice<ClBatch> sliceClBatch = sClBatchService.findGroupNo(groupNoOnExcelFileName, 0, Integer.MAX_VALUE,
				batchTitaVo);

		if (sliceClBatch == null || sliceClBatch.isEmpty()) {
			throwErrorMsg("E0015", "上傳回饋檔的批次號碼在擔保品整批匯入檔查無資料,請使用L2419功能1,重新產生回饋檔.");
		}

		listClBatch = new ArrayList<>(sliceClBatch.getContent());
	}

	private int getDate(String s) throws LogicException {
		int r = 0;
		s = s.replace("/", "");
		r = parse.stringToInteger(s);
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

	private boolean checkDate(String dt) throws LogicException {
		String dt2 = dt.replace("/", "");
		return dateUtil.checkDate(dt2);
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

	private void checkElseOwner(int row) throws LogicException {
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
				throwErrorMsg("E0015", "行數:" + row + ",所有權人" + ownerSeq + "-所有權種類應為1_建物或2_土地,實際輸入=" + columnAN);
			}

			// 所有權人-身分證/統編
			String columnAO = makeExcel.getValue(row, column + 1).toString();
			this.info("columnAO = " + columnAO);
			String ownerId1 = columnAO.trim();
			if (ownerId1 == null || ownerId1.isEmpty()) {
				throwErrorMsg("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-身分證/統編不得為空白.");
			}

			// 所有權人-姓名
			String columnAP = makeExcel.getValue(row, column + 2).toString();
			this.info("columnAP = " + columnAP);
			String ownerName1 = columnAP.trim();
			if (ownerName1 == null || ownerName1.isEmpty()) {
				throwErrorMsg("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-姓名不得為空白.");
			}

			// 所有權人-與授信戶關係
			String ownerRel = makeExcel.getValue(row, column + 3).toString();
			if (ownerRel != null && !ownerRel.isEmpty() && ownerRel.indexOf("_") >= 0) {
				if (ownerRel.substring(0, ownerRel.indexOf("_")).equals("00")) {
					ownerRel = ownerRel.substring(0, ownerRel.indexOf("_"));
				} else {
					ownerRel = getCodeAndCheck(ownerRel, "AQ", "GuaRelCode", "與授信戶關係");
				}
			} else {
				throwErrorMsg("E0015", "欄位:AQ ,行數:" + row + ",有選擇所有權種類時,所有權人-與授信戶關係不得為空白.");
			}
			this.info("ownerRel = " + ownerRel);

			// 所有權人-持份比率
			String columnAR = makeExcel.getValue(row, column + 4).toString();
			this.info("columnAR = " + columnAR);
			if (columnAR == null || columnAR.isEmpty()) {
				throwErrorMsg("E0015", "行數:" + row + ",有選擇所有權種類時,所有權人" + ownerSeq + "-持份比率不得為空白.");
			}
			if (columnAR.indexOf("分之") >= 0) {
				String ownerTotal = columnAR.substring(0, columnAR.indexOf("分之")); // 持份比率(分母)
				String ownerPart = columnAR.substring(columnAR.indexOf("分之") + 2); // 持份比率(分子)
				this.info("ownerPart = " + ownerPart);
				this.info("ownerTotal = " + ownerTotal);
			} else {
				throwErrorMsg("E0015", "行數:" + row + ",所有權人" + ownerSeq + "-持份比率格式為幾分之幾,可參考權狀上的權利範圍.");
			}

			// 所有權人 檢核皆通過,紀錄到lastOwnerList
			Map<String, String> owner = new HashMap<>();
			owner.put("type", ownerType1);
			owner.put("id", columnAO);
			owner.put("name", columnAP);
			owner.put("rel", ownerRel);
			owner.put("part", columnAR);
			lastOwnerList.add(owner);
		}
	}

	private String getZipItem(String k) {
		String rs = "";
		if (zipItems.size() > 0 && zipItems.containsKey(k) && zipItems.get(k) != null) {
			rs = zipItems.get(k);
		}
		return rs;
	}

	private String getIrItem(String cityCode, String areaCode, String irCode) {

		this.info("CdLandSection = " + cityCode + "-" + areaCode + "-" + irCode);
		CdLandSectionId cdLandSectionId = new CdLandSectionId();
		cdLandSectionId.setCityCode(cityCode);
		cdLandSectionId.setAreaCode(areaCode);
		cdLandSectionId.setIrCode(irCode);

		CdLandSection cdLandSection = sCdLandSectionService.findById(cdLandSectionId, batchTitaVo);

		if (cdLandSection != null) {
			return cdLandSection.getIrItem();
		}
		return "";
	}
}