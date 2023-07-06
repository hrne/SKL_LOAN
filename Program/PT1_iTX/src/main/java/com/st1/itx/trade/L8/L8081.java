package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.service.TxAmlCreditService;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CustMainService;

@Service("L8081")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8081 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	CdBcmService cdBcmService;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	CustMainService sCustMainService;
	
	@Autowired
	public CdAreaService cdAreaService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	Parse parse;

	HashMap<String, String> unitItems = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8081 ");
		this.totaVo.init(titaVo);

		String iReviewType = titaVo.get("ReviewType").trim();
		String iProcessType = titaVo.get("ProcessType").trim();
		if ("9".equals(iProcessType)) {
			iProcessType = "%";
		}
		String iStatus = titaVo.get("Status").trim();
		int iAcDate1 = parse.stringToInteger(titaVo.get("AcDate1")) + 19110000;
		int iAcDate2 = parse.stringToInteger(titaVo.get("AcDate2")) + 19110000;

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxAmlCredit> slTxAmlCredit = null;

		List<String> reviewType = Arrays.asList("H");

		if ("9".equals(iStatus)) {
			slTxAmlCredit = txAmlCreditService.processAll(reviewType, iAcDate1, iAcDate2, iProcessType, this.index,
					this.limit);
		} else if ("1".equals(iStatus)) {
			slTxAmlCredit = txAmlCreditService.processYes(reviewType, iAcDate1, iAcDate2, iProcessType, 0, this.index,
					this.limit);
		} else {
			slTxAmlCredit = txAmlCreditService.processNo(reviewType, iAcDate1, iAcDate2, iProcessType, 0, this.index,
					this.limit);
		}

		List<TxAmlCredit> lTxAmlCredit = slTxAmlCredit == null ? null : slTxAmlCredit.getContent();

		if (lTxAmlCredit == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxAmlCredit txAmlCredit : lTxAmlCredit) {

				OccursList occursList = new OccursList();

				occursList.putParam("oDataDt", txAmlCredit.getDataDt());
				occursList.putParam("oCustKey", txAmlCredit.getCustKey());
				occursList.putParam("oRRSeq", txAmlCredit.getRRSeq());
				occursList.putParam("oReviewType", txAmlCredit.getReviewType());
				occursList.putParam("oUnit", txAmlCredit.getUnit());
				occursList.putParam("oUnitItem", getUnitItem(txAmlCredit.getUnit().trim(), titaVo));
				occursList.putParam("oIsStatus", txAmlCredit.getIsStatus());
//				this.info("txAmlCredit.ProcessType="+txAmlCredit.getProcessType());
				occursList.putParam("oProcessType", txAmlCredit.getProcessType());
				occursList.putParam("oProcessCount", txAmlCredit.getProcessCount());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAmlCredit != null && slTxAmlCredit.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		// 下載高風險郵寄名單及明細
		// 列數

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L8081";
		String fileItem = "下載高風險郵寄名單及明細";
		String fileName = "下載高風險郵寄名單及明細";
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		makeExcel.open(titaVo, reportVo, fileName);
		Slice<TxAmlCredit> slTxAmlCredit2 = null;

		List<String> reviewType2 = Arrays.asList("H");

		if ("9".equals(iStatus)) {
			slTxAmlCredit2 = txAmlCreditService.processAll(reviewType2, iAcDate1, iAcDate2, iProcessType, this.index,
					this.limit);
		} else if ("1".equals(iStatus)) {
			slTxAmlCredit2 = txAmlCreditService.processYes(reviewType2, iAcDate1, iAcDate2, iProcessType, 0, this.index,
					this.limit);
		} else {
			slTxAmlCredit2 = txAmlCreditService.processNo(reviewType2, iAcDate1, iAcDate2, iProcessType, 0, this.index,
					this.limit);
		}

		List<TxAmlCredit> lTxAmlCredit2 = slTxAmlCredit2 == null ? null : slTxAmlCredit2.getContent();

		if (lTxAmlCredit2 == null) {
			throw new LogicException("E0001", "");
		} else {
			makeExcel.setValue(1, 1, "郵遞區號");
			makeExcel.setValue(1, 2, "地址");
			makeExcel.setValue(1, 3, "姓名");
			int row = 1;
			for (TxAmlCredit txAmlCredit2 : lTxAmlCredit2) {
				row++;
				CustMain sCustMain2 = new CustMain();
				sCustMain2 = sCustMainService.custIdFirst(txAmlCredit2.getCustKey(), titaVo);

				if (sCustMain2 != null) {

					String iCurrZip3 = sCustMain2.getCurrZip3();
					if (iCurrZip3.isEmpty()) {
						iCurrZip3 = " ";
					}

					String iCurrZip2 = sCustMain2.getCurrZip2();
					if (iCurrZip2.isEmpty()) {
						iCurrZip2 = " ";
					}

					String iCurrCityCode = sCustMain2.getCurrCityCode();
					if (iCurrCityCode.isEmpty()) {
						iCurrCityCode = " ";
					}

					String iCurrAreaCode = sCustMain2.getCurrAreaCode();
					if (iCurrAreaCode.isEmpty()) {
						iCurrAreaCode = " ";
					}

					String iCurrRoad = sCustMain2.getCurrRoad();
					if (iCurrRoad.isEmpty()) {
						iCurrRoad = " ";
					}

					String iCurrSection = sCustMain2.getCurrSection();
					if (iCurrSection.isEmpty()) {
						iCurrSection = " ";
					}

					String iCurrAlley = sCustMain2.getCurrAlley();
					if (iCurrAlley.isEmpty()) {
						iCurrAlley = " ";
					}

					String iCurrLane = sCustMain2.getCurrLane();
					if (iCurrLane.isEmpty()) {
						iCurrLane = " ";
					}

					String iCurrNum = sCustMain2.getCurrNum();
					if (iCurrNum.isEmpty()) {
						iCurrNum = " ";
					}

					String iCurrNumDash = sCustMain2.getCurrNumDash();
					if (iCurrNumDash.isEmpty()) {
						iCurrNumDash = " ";
					}

					String iCurrFloor = sCustMain2.getCurrFloor();
					if (iCurrFloor.isEmpty()) {
						iCurrFloor = " ";
					}

					String iCurrFloorDash = sCustMain2.getCurrFloorDash();
					if (iCurrFloorDash.isEmpty()) {
						iCurrFloorDash = " ";
					}

					String iCustName = sCustMain2.getCustName();
					if (iCustName.isEmpty()) {
						iCustName = " ";
					}
					
					String currAddress = "";
					if (!"".equals(sCustMain2.getCurrCityCode())) {
						CdCity cdCity = cdCityService.findById(sCustMain2.getCurrCityCode(), titaVo);
						if (cdCity != null) {
							currAddress += cdCity.getCityItem();

							if (!"".equals(sCustMain2.getCurrAreaCode())) {
								CdAreaId cdAreaId = new CdAreaId();
								cdAreaId.setCityCode(sCustMain2.getCurrCityCode());
								cdAreaId.setAreaCode(sCustMain2.getCurrAreaCode());
								CdArea cdArea = cdAreaService.findById(cdAreaId, titaVo);
								if (cdArea != null) {
									currAddress += cdArea.getAreaItem();
								}
							}
						}
					}
					
					
					currAddress += sCustMain2.getCurrRoad();
					if (!"".equals(sCustMain2.getCurrSection())) {
						currAddress += sCustMain2.getCurrSection() + "段";
					}
					if (!"".equals(sCustMain2.getCurrAlley())) {
						currAddress += sCustMain2.getCurrAlley() + "巷";
					}
					if (!"".equals(sCustMain2.getCurrLane())) {
						currAddress += sCustMain2.getCurrLane() + "弄";
					}
					if (!"".equals(sCustMain2.getCurrNum())) {
						currAddress += sCustMain2.getCurrNum() + "號";
					}
					String numDash = "";
					if (!"".equals(sCustMain2.getCurrNumDash())) {
						currAddress += "-" + sCustMain2.getCurrNumDash();
						numDash = "，";
					}
					if (!"".equals(sCustMain2.getCurrFloor())) {
						currAddress += numDash + sCustMain2.getCurrFloor() + "樓";
					}
					if (!"".equals(sCustMain2.getCurrFloorDash())) {
						currAddress += "-" + sCustMain2.getCurrFloorDash();
					}
					makeExcel.setValue(row, 1, iCurrZip3 + "-" + iCurrZip2);
//					makeExcel.setValue(row, 2, iCurrCityCode + iCurrAreaCode + iCurrRoad + iCurrSection + iCurrAlley
//							+ iCurrLane + iCurrNum + iCurrNumDash + iCurrFloor + iCurrFloorDash);
					makeExcel.setValue(row, 2, currAddress);
					makeExcel.setValue(row, 3, iCustName);

				}
			}
		}

		makeExcel.close();
		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getUnitItem(String unitCode, TitaVo titaVo) {
		String unitItem = "";

		if ("".equals(unitCode)) {
			return unitItem;
		}

		if (unitItems.size() > 0) {
			if (unitItems.get(unitCode) != null) {
				unitItem = unitItems.get(unitCode).toString();
			}

		}

		if ("".equals(unitItem)) {
			CdBcm cdBcm = cdBcmService.findById(unitCode, titaVo);
			if (cdBcm == null) {
				unitItem = unitCode;
			} else {
				unitItem = cdBcm.getUnitItem();
				unitItems.put(unitCode, unitItem);
			}
		}
		return unitItem;
	}
}