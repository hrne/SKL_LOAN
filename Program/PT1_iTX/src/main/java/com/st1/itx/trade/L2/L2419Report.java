package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdClBatch;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.ClBatch;
import com.st1.itx.db.domain.ClBatchId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClBatchService;
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
import com.st1.itx.db.service.springjpa.cm.L2419ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.ReportUtil;

@Service("L2419Report")
@Scope("prototype")
/**
 * 以系統擔保品資料產生回饋檔
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L2419Report extends TradeBuffer {

	@Autowired
	L2419ServiceImpl l2419ServiceImpl;

	@Autowired
	private ReportUtil rptUtil;

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
	private ClBatchService sClBatchService;

	@Autowired
	private CdClBatchService sCdClBatchService;

	private List<ClBatch> listClBatch;

	private int row = 1;

	private List<Map<String, String>> lastOwnerList = null;

	private Map<String, String> zipItems = new HashMap<>();

	private int custNo = 0;

	private int facmNo = 0;

	private static final String REPORT_CODE = "L2419";
	private static final String REPORT_ITEM = "擔保品整批匯入回饋檔";
	private static final String FILE_NAME = "擔保品整批匯入回饋檔";
	private static final String DEFAULT_EXCEL = "擔保品明細表_空白表_2022-11-08.xlsx";

	private TitaVo titaVo = null;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2419Report");
		this.totaVo.init(titaVo);

		this.titaVo = titaVo;

		int applNo = Integer.parseInt(titaVo.getParam("ApplNo"));

		String groupNo = this.getGroupNo(applNo, titaVo);

		zipItems = new HashMap<>();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			resultList = l2419ServiceImpl.doQuery(titaVo);
		} catch (Exception e) {
			this.error("L2419ServiceImpl doQuery " + e.getMessage());
			throw new LogicException("E0013", "L2419Report");
		}

		if (resultList == null || resultList.isEmpty()) {
			throw new LogicException("E0013", "L2419Report");
		}

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
				.setRptCode(REPORT_CODE).setRptItem(REPORT_ITEM).build();

		// open
		makeExcel.open(titaVo, reportVo, FILE_NAME + "_" + groupNo, DEFAULT_EXCEL, "擔保品明細表");

		int detailNo = 3;

		for (Map<String, String> result : resultList) {
			makeExcel.setShiftRow(detailNo + 1, 1);
			String clCode1 = result.get("ClCode1");
			String clCode2 = result.get("ClCode2");
			String clNo = result.get("ClNo");
			this.info("result " + detailNo + " = " + clCode1 + "-" + clCode2 + "-" + clNo);

			recordClBatch(applNo, clCode1, clCode2, clNo, groupNo, detailNo - 2);

			setDetailData(detailNo, result);

			detailNo++;
		}
		
		makeExcel.setLockColumn(3, detailNo - 1, L2419Column.NO.getIndex(), L2419Column.CL_NO.getIndex(), 142);
		
		makeExcel.setProtectSheet(groupNo);

		makeExcel.toExcel(makeExcel.close());

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", "", "L2419已完成", titaVo);

		return null;
	}

	private void recordClBatch(int applNo, String clCode1, String clCode2, String clNo, String groupNo, int no)
			throws LogicException {

		ClBatchId tClBatchId;
		ClBatch tClBatch;
		tClBatchId = new ClBatchId();
		tClBatchId.setGroupNo(groupNo);
		tClBatchId.setSeq(no);

		tClBatch = new ClBatch();
		tClBatch.setClBatchId(tClBatchId);
		tClBatch.setApplNo(applNo);
		tClBatch.setClCode1(Integer.parseInt(clCode1));
		tClBatch.setClCode2(Integer.parseInt(clCode2));
		tClBatch.setClNo(Integer.parseInt(clNo));
		tClBatch.setInsertStatus(1);

		try {
			sClBatchService.insert(tClBatch, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "擔保品整批匯入檔(ClBatch),上傳檔編號(" + no + ")");
		}
	}

	private void setDetailData(int detailNo, Map<String, String> result) throws LogicException {
		// 從query結果會拿到的資料,因為key需與query內容配合,這裡獨立寫一段
		String clCode1 = result.get("ClCode1");
		String clCode2 = result.get("ClCode2");
		String clNo = result.get("ClNo");
		String insertFlag = result.get("InsertFlag");
		String clTypeCode = result.get("ClTypeCode");
		String zip3 = result.get("Zip3");
		String evaAmt = result.get("EvaAmt");
		String evaNetWorth = result.get("EvaNetWorth");
		String lVITax = result.get("LVITax");
		String rentEvaValue = result.get("RentEvaValue");
		String rentPrice = result.get("RentPrice");
		String loanToValue = result.get("LoanToValue");
		String settingDate = result.get("SettingDate");
		String settingAmt = result.get("SettingAmt");
		String claimDate = result.get("ClaimDate");
		String irCode = result.get("IrCode");
		String road = result.get("Road");
		String bdNo1 = result.get("BdNo1");
		String bdNo2 = result.get("BdNo2");
		String bdMainUseCode = result.get("BdMainUseCode");
		String bdMtrlCode = result.get("BdMtrlCode");
		String bdTypeCode = result.get("BdTypeCode");
		String totalFloor = result.get("TotalFloor");
		String bdDate = result.get("BdDate");
		String floorNo = result.get("FloorNo");
		String area = result.get("Area");
		String evaUnitPrice = result.get("EvaUnitPrice");
		String landNo1 = result.get("LandNo1");
		String landNo2 = result.get("LandNo2");
		String origInsuNo = result.get("OrigInsuNo");
		String insuCompany = result.get("InsuCompany");
		String insuTypeCode = result.get("InsuTypeCode");
		String fireInsuCovrg = result.get("FireInsuCovrg");
		String ethqInsuCovrg = result.get("EthqInsuCovrg");
		String fireInsuPrem = result.get("FireInsuPrem");
		String ethqInsuPrem = result.get("EthqInsuPrem");
		String insuStartDate = result.get("InsuStartDate");
		String insuEndDate = result.get("InsuEndDate");

		// 不是從query結果拿到的資料處理
		String formula = "IF(ISNUMBER(A" + detailNo + "),A" + detailNo + "+1,1)";

		// 把值寫入Excel
		makeExcel.setFormula(detailNo, L2419Column.NO.getIndex(), new BigDecimal(detailNo - 2), formula, "0");
		makeExcel.setValue(detailNo, L2419Column.CL_CODE_1.getIndex(), getItem("ClCode1", clCode1));
		makeExcel.setValue(detailNo, L2419Column.CL_CODE_2.getIndex(),
				getItem("ClCode2" + clCode1, leftPadZero(clCode2, 2)));
		makeExcel.setValue(detailNo, L2419Column.CL_NO.getIndex(), clNo);
		makeExcel.setValue(detailNo, L2419Column.INSERT_FLAG.getIndex(), insertFlag);
		makeExcel.setValue(detailNo, L2419Column.CL_TYPE.getIndex(), getItem("ClTypeCode2" + clCode1, clTypeCode));
		makeExcel.setValue(detailNo, L2419Column.BUILD_NO_1.getIndex(), bdNo1);
		makeExcel.setValue(detailNo, L2419Column.BUILD_NO_2.getIndex(), bdNo2);
		makeExcel.setValue(detailNo, L2419Column.LAND_NO_1.getIndex(), landNo1);
		makeExcel.setValue(detailNo, L2419Column.LAND_NO_2.getIndex(), landNo2);
		makeExcel.setValue(detailNo, L2419Column.ZIP_3.getIndex(), zip3);
		makeExcel.setValue(detailNo, L2419Column.IR_CODE.getIndex(), irCode);
		makeExcel.setValue(detailNo, L2419Column.ROAD.getIndex(), road);
		makeExcel.setValue(detailNo, L2419Column.USE_CODE.getIndex(), getItem("BdMainUseCode", bdMainUseCode));
		makeExcel.setValue(detailNo, L2419Column.BUILD_TYPE.getIndex(), getItem("BdTypeCode", bdTypeCode));
		makeExcel.setValue(detailNo, L2419Column.MTRL.getIndex(), getItem("BdMtrlCode", bdMtrlCode));
		makeExcel.setValue(detailNo, L2419Column.FLOOR_NO.getIndex(), floorNo);
		makeExcel.setValue(detailNo, L2419Column.TOTAL_FLOOR.getIndex(), totalFloor);
		makeExcel.setValue(detailNo, L2419Column.BUILD_DATE.getIndex(), getFormattedRocDate(bdDate));
		makeExcel.setValue(detailNo, L2419Column.SETTING_DATE.getIndex(), getFormattedRocDate(settingDate));
		makeExcel.setValue(detailNo, L2419Column.CLAIM_DATE.getIndex(), getFormattedRocDate(claimDate));
		makeExcel.setValue(detailNo, L2419Column.FLOOR_AREA.getIndex(), rptUtil.getBigDecimal(area));
		makeExcel.setValue(detailNo, L2419Column.UNIT_PRICE.getIndex(), rptUtil.getBigDecimal(evaUnitPrice));
		makeExcel.setValue(detailNo, L2419Column.EVA_AMT.getIndex(), rptUtil.getBigDecimal(evaAmt));
		makeExcel.setValue(detailNo, L2419Column.TAX.getIndex(), rptUtil.getBigDecimal(lVITax));
		makeExcel.setValue(detailNo, L2419Column.NET_VALUE.getIndex(), rptUtil.getBigDecimal(evaNetWorth));
		makeExcel.setValue(detailNo, L2419Column.RENT_PRICE.getIndex(), rptUtil.getBigDecimal(rentPrice));
		makeExcel.setValue(detailNo, L2419Column.RENT_EVA_VALUE.getIndex(), rptUtil.getBigDecimal(rentEvaValue));
		makeExcel.setValue(detailNo, L2419Column.LOAN_TO_VALUE.getIndex(), rptUtil.getBigDecimal(loanToValue));
		makeExcel.setValue(detailNo, L2419Column.SETTING_AMT.getIndex(), rptUtil.getBigDecimal(settingAmt));
		makeExcel.setValue(detailNo, L2419Column.INSU_NO.getIndex(), origInsuNo);
		makeExcel.setValue(detailNo, L2419Column.INSU_COMPANY.getIndex(), getItem("InsuCompany", insuCompany));
		makeExcel.setValue(detailNo, L2419Column.INSU_TYPE.getIndex(), getItem("InsuTypeCode", insuTypeCode));
		makeExcel.setValue(detailNo, L2419Column.FIRE_INSU_AMT.getIndex(), rptUtil.getBigDecimal(fireInsuCovrg));
		makeExcel.setValue(detailNo, L2419Column.FIRE_INSU_EXPENSE.getIndex(), rptUtil.getBigDecimal(fireInsuPrem));
		makeExcel.setValue(detailNo, L2419Column.EARTHQUAKE_INSU_AMT.getIndex(), rptUtil.getBigDecimal(ethqInsuCovrg));
		makeExcel.setValue(detailNo, L2419Column.EARTHQUAKE_INSU_EXPENSE.getIndex(),
				rptUtil.getBigDecimal(ethqInsuPrem));
		makeExcel.setValue(detailNo, L2419Column.INSU_START.getIndex(), getFormattedRocDate(insuStartDate));
		makeExcel.setValue(detailNo, L2419Column.INSU_END.getIndex(), getFormattedRocDate(insuEndDate));
//		makeExcel.setValue(detailNo,L2419Column.OWNER_TYPE_1.getIndex(),);
//		makeExcel.setValue(detailNo,L2419Column.OWNER_ID_1.getIndex(),);
//		makeExcel.setValue(detailNo,L2419Column.OWNER_NAME_1.getIndex(),);
//		makeExcel.setValue(detailNo,L2419Column.OWNER_RELATION_1.getIndex(),);
//		makeExcel.setValue(detailNo,L2419Column.OWNER_PARTIAL_1.getIndex(),);
//		makeExcel.setValue(detailNo,L2419Column.OWNER_TYPE_2.getIndex(),);

		// 所有權人資料
		queryAndSetOwnerData(detailNo, clCode1, clCode2, clNo);

	}

	private void queryAndSetOwnerData(int detailNo, String clCode1, String clCode2, String clNo) throws LogicException {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			resultList = l2419ServiceImpl.doQueryOwner(clCode1, clCode2, clNo, titaVo);
		} catch (Exception e) {
			this.error("L2419ServiceImpl doQueryOwner " + e.getMessage());
			throw new LogicException("E0013", "L2419Report");
		}
		if (resultList == null || resultList.isEmpty()) {
			return;
		}
		int tempColumn = L2419Column.OWNER_TYPE_1.getIndex();
		for (Map<String, String> result : resultList) {
			String ownerType = result.get("OwnerType");
			String custId = result.get("CustId");
			String custName = result.get("CustName");
			String ownerRelCode = result.get("OwnerRelCode");
			String ownerPart = result.get("OwnerPart");
			String ownerTotal = result.get("OwnerTotal");

			String ownerTypeItem = ownerType.equals("1") ? "1_建物" : "2_土地";
			String ownerPartial = ownerTotal + "分之" + ownerPart;

			makeExcel.setValue(detailNo, tempColumn, ownerTypeItem);
			makeExcel.setValue(detailNo, tempColumn + 1, custId);
			makeExcel.setValue(detailNo, tempColumn + 2, custName);
			makeExcel.setValue(detailNo, tempColumn + 3,
					ownerRelCode == null || ownerRelCode.isEmpty() ? "00_本人" : getItem("GuaRelCode", ownerRelCode));
			makeExcel.setValue(detailNo, tempColumn + 4, ownerPartial);
			tempColumn += 5;
		}
	}

	private Object getItem(String defCode, String code) {
		String result = code + "_";
		CdCodeId cdCodeId = new CdCodeId();
		cdCodeId.setDefCode(defCode);
		cdCodeId.setCode(code);
		CdCode cdCode = sCdCodeService.findById(cdCodeId, this.titaVo);
		if (cdCode != null) {
			result += cdCode.getItem();
		}
		return result;
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

	private String getFormattedRocDate(String bcDate) {
		if (bcDate == null || bcDate.isEmpty()) {
			return "";
		}
		int tempDate = 0;
		try {
			tempDate = Integer.parseInt(bcDate);
		} catch (Exception e) {
			return "";
		}
		return rptUtil.showRocDate(tempDate, 1);
	}
}