package com.st1.itx.trade.L2;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.StaticTool;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.parse.ZLibUtils;

@Service("L2419")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2419 extends TradeBuffer {
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Autowired
	public FileCom fileCom;

	@Autowired
	public DateUtil dateUtil;

	/* ?????????????????? */
	@Autowired
	public Parse parse;

	@Autowired
	ZLibUtils zLibUtils;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	/* ???????????? */
	@Autowired
	GSeqCom gSeqCom;

	@Autowired
	public ClImmService sClImmService;
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CdClService sCdClService;
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	public ClBuildingService sClBuildingService;
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;
	@Autowired
	public ClLandService sClLandService;
	@Autowired
	public ClLandOwnerService sClLandOwnerService;
	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public FacMainService sFacMainService;
	/* DB???????????? */
	@Autowired
	TxFileService sTxFileService;
	@Autowired
	CdLandSectionService sCdLandSectionService;
	@Autowired
	public InsuOrignalService insuOrignalService;

	private HashMap<String, String> ownerids = new HashMap<String, String>();
	private HashMap<String, String> items = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2419 ");
		this.totaVo.init(titaVo);

		String Step = titaVo.getParam("Step");

		if ("1".equals(Step)) {
			step1(titaVo);
		} else {
			step2(titaVo);
		}
//		MySpring.newTask("L2419Batch", this.txBuffer, titaVo);
//		this.totaVo.setWarnMsg("???????????????,???????????????????????????");

		this.addList(this.totaVo);
		return this.sendList();
	}

	// ???????????????
	private void step1(TitaVo titaVo) throws LogicException {
		this.info("active Step1 ");

		this.index = titaVo.getReturnIndex();

		String custId = titaVo.getParam("CustId");

		String fileItem = titaVo.getParam("FileItem").trim();

		if (fileItem.isEmpty()) {
			throw new LogicException("E0015", "??????????????????????????????");
		}

		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + fileItem;

		this.info("fileitem=" + fileItem);
		makeExcel.openExcel(fileName, "??????????????????");

		CustMain custMain = sCustMainService.custIdFirst(custId, titaVo);
		if (custMain == null) {
			throw new LogicException("E0001", "???????????? = " + custId);
		}

		int cnt = 0;

		for (int i = index; i < 10000; i++) {

			int ii = i + 1;

			int row = 3 + i;

			int no = (int) toNumeric(makeExcel.getValue(row, 1).toString());

			if (no == 0) {
				break;
			}

			if (cnt >= 100) {
				titaVo.setReturnIndex(i);
				this.totaVo.setMsgEndToAuto();// ????????????
				return;
			}

			OccursList occursList = new OccursList();

			// ??????
			occursList.putParam("Idx", i + 1);

			// ??????
			occursList.putParam("No", no);

//				this.info(i + "-" + j);
//				s += makeExcel.getValue(4 + i, j).toString() + "/";

			// ???????????????1
			int clCode1 = (int) toNumeric(makeExcel.getValue(row, 2).toString());
			if (clCode1 != 1 && clCode1 != 2) {
				throwError("B" + row, "???????????????1", makeExcel.getValue(row, 2).toString());
			}
			occursList.putParam("ClCode1", clCode1);

			// ???????????????2
			String clCode2 = toString(makeExcel.getValue(row, 3).toString(), 2);
			if (clCode1 == 1) {
				String clCode2X = checkCode(titaVo, "ClCode21", "" + clCode2, "C" + row, "???????????????2",
						makeExcel.getValue(row, 3).toString());
			} else {
				String clCode2X = checkCode(titaVo, "ClCode22", "" + clCode2, "C" + row, "???????????????2",
						makeExcel.getValue(row, 3).toString());
			}
			occursList.putParam("ClCode2", clCode2);

			// ???????????????
			int clNo = (int) toNumeric(makeExcel.getValue(row, 4).toString());
			occursList.putParam("ClNo", clNo);

			if (clNo > 0) {
				ClMainId clMainId = new ClMainId();

				clMainId.setClCode1(clCode1);
				clMainId.setClCode2(parse.stringToInteger(clCode2));
				clMainId.setClNo(clNo);

				ClMain clMain = sClMainService.findById(clMainId, titaVo);
				if (clMain == null) {
					String clNoX = String.format("%01d-%02d-%07d", clCode1, parse.stringToInteger(clCode2), clNo);
					throw new LogicException("E0001", "??????:D" + row + ",???????????????(ClMain)=" + clNoX);
				}

			}

			// ???????????????
			occursList.putParam("ClCode", String.format("%01d-%s-%07d", clCode1, clCode2, clNo));

			// ?????????????????????
			String typeCode = toString(makeExcel.getValue(row, 5).toString(), 3);
			String TypeCodeX = "";
			if (clCode1 == 1) {
				TypeCodeX = checkCode(titaVo, "ClTypeCode21", typeCode, "E" + row, "???????????????",
						makeExcel.getValue(row, 5).toString());
			} else {
				TypeCodeX = checkCode(titaVo, "ClTypeCode22", typeCode, "E" + row, "???????????????",
						makeExcel.getValue(row, 5).toString());
			}
			occursList.putParam("TypeCode", typeCode);

			if (clCode1 == 1) {
				// ??????
				int bdno1 = (int) toNumeric(makeExcel.getValue(row, 6).toString());
				if (bdno1 == 0) {
					throw new LogicException("E0015", "??????:F" + row + ",?????? = " + makeExcel.getValue(row, 6).toString());
				}

				occursList.putParam("BdNo1", toString(makeExcel.getValue(row, 6).toString(), 5));
				occursList.putParam("BdNo2", toString(makeExcel.getValue(row, 7).toString(), 3));
				// ??????
				occursList.putParam("LdNo1", "");
				occursList.putParam("LdNo2", "");
			} else {
				// ??????
				occursList.putParam("BdNo1", "");
				occursList.putParam("BdNo2", "");
				// ??????
				int ldno1 = (int) toNumeric(makeExcel.getValue(row, 8).toString());
				if (ldno1 == 0) {
					throw new LogicException("E0015", "??????:H" + row + ",?????? = " + makeExcel.getValue(row, 8).toString());
				}
				occursList.putParam("LdNo1", toString(makeExcel.getValue(row, 8).toString(), 4));
				occursList.putParam("LdNo2", toString(makeExcel.getValue(row, 9).toString(), 4));
			}

			// ????????????
			String zip3 = toString(makeExcel.getValue(row, 10).toString(), 3);
			String cityCode = getItem("zipcity=" + zip3);
			String areaCode = "";

			if (cityCode.isEmpty()) {

				CdArea cdArea = sCdAreaService.Zip3First(zip3, titaVo);

				if (cdArea == null) {
					throw new LogicException("E0015",
							"??????:J" + row + ",???????????? = " + makeExcel.getValue(row, 10).toString());
				}

				CdCity cdCity = sCdCityService.findById(cdArea.getCityCode(), titaVo);
				if (cdCity == null) {
					throw new LogicException("E0015", "??????:J" + row + ",??????????????? = " + cdArea.getCityCode());
				}

				items.put("zipcity=" + zip3, cdArea.getCityCode());
				items.put("city=" + cdArea.getCityCode(), cdCity.getCityItem());
				items.put("ziparea=" + zip3, cdArea.getAreaCode());
				items.put("area=" + cdArea.getCityCode() + "-" + cdArea.getAreaCode(), cdArea.getAreaItem());

				// ??????
				occursList.putParam("CityCode", cdArea.getCityCode());
				occursList.putParam("CityCodeX", cdCity.getCityItem());
				// ????????????
				occursList.putParam("AreaCode", cdArea.getAreaCode());
				occursList.putParam("AreaCodeX", cdArea.getAreaItem());

				cityCode = cdArea.getCityCode();
				areaCode = cdArea.getAreaCode();
			} else {
				// ??????
				occursList.putParam("CityCode", cityCode);
				occursList.putParam("CityCodeX", getItem("city=" + cityCode));
				// ????????????
				areaCode = getItem("ziparea=" + zip3);
				occursList.putParam("AreaCode", areaCode);
				occursList.putParam("AreaCodeX", getItem("area=" + cityCode + "-" + areaCode));

			}

			String irCode = toString(makeExcel.getValue(row, 11).toString(), 4);
			// ???????????????
			occursList.putParam("IrCode", irCode);

			this.info("CdLandSection = " + cityCode + "-" + areaCode + "-" + irCode);
			CdLandSectionId cdLandSectionId = new CdLandSectionId();
			cdLandSectionId.setCityCode(cityCode);
			cdLandSectionId.setAreaCode(areaCode);
			cdLandSectionId.setIrCode(irCode);

			CdLandSection CdLandSection = sCdLandSectionService.findById(cdLandSectionId, titaVo);
			if (CdLandSection == null) {
				throw new LogicException("E0015", "??????:K" + row + ",?????? = " + makeExcel.getValue(row, 11).toString());
			}
			occursList.putParam("IrCodeX", CdLandSection.getIrItem());

			if (clCode1 == 1) {
				// ??????
				String road = makeExcel.getValue(row, 12).toString().trim();
				occursList.putParam("Road", road);

				if (road.isEmpty()) {
					throw new LogicException("E0015", "??????:L" + row + ",??????????????????");
				}
				// ??????
				String useCode = toString(makeExcel.getValue(row, 13).toString(), 2);
				checkCode(titaVo, "BdMainUseCode", useCode, "M" + row, "??????", makeExcel.getValue(row, 11).toString());
				occursList.putParam("UseCode", useCode);
				// ????????????
				String buTypeCode = toString(makeExcel.getValue(row, 14).toString(), 2);
				checkCode(titaVo, "BdTypeCode", buTypeCode, "N" + row, "????????????", makeExcel.getValue(row, 12).toString());
				occursList.putParam("BuTypeCode", buTypeCode);
				// ??????
				String mtrlCode = toString(makeExcel.getValue(row, 15).toString(), 2);
				checkCode(titaVo, "BdMtrlCode", mtrlCode, "O" + row, "??????", makeExcel.getValue(row, 13).toString());
				occursList.putParam("MtrlCode", mtrlCode);
				// ??????
				String floorNo = makeExcel.getValue(row, 16).toString().trim();
				occursList.putParam("FloorNo", floorNo);
				if (floorNo.isEmpty()) {
					throw new LogicException("E0015", "??????:P" + row + ",??????????????????");
				}
				// ?????????
				int totalFloor = (int) toNumeric(makeExcel.getValue(row, 17).toString());
				occursList.putParam("TotalFloor", totalFloor);
				if (totalFloor == 0) {
					throw new LogicException("E0015", "??????:Q" + row + ",?????????=" + makeExcel.getValue(row, 17).toString());
				}
				// ??????????????????
				String BdDate = makeExcel.getValue(row, 18).toString();
				if (BdDate.length() != 9) {
					throw new LogicException("E0015", "??????:R" + row + ",??????????????????(YYY/MM/DD) = " + BdDate);
				}
				if (!checkDate(BdDate)) {
					throw new LogicException("E0015", "??????:R" + row + ",??????????????????(YYY/MM/DD) = " + BdDate);
				}
				if (!compareDate(BdDate, titaVo.getParam("CALDY"))) {
					throw new LogicException("E0015", "??????:R" + row + ",??????????????????(YYY/MM/DD) = " + BdDate + " ,?????????????????????");
				}
				occursList.putParam("BdDate", BdDate);
			} else {
				occursList.putParam("Road", "");
				occursList.putParam("UseCode", "");
				occursList.putParam("BuTypeCode", "");
				occursList.putParam("MtrlCode", "");
				occursList.putParam("FloorNo", "");
				occursList.putParam("TotalFloor", "");
				occursList.putParam("BdDate", "");
			}
			// ????????????
			String SettingDate = makeExcel.getValue(row, 19).toString();
			if (SettingDate.length() != 9) {
				throw new LogicException("E0015", "??????:S" + row + ",????????????(YYY/MM/DD) = " + SettingDate);
			}
			if (!checkDate(SettingDate)) {
				throw new LogicException("E0015", "??????:S" + row + ",????????????(YYY/MM/DD) = " + SettingDate);
			}
			if (!compareDate(SettingDate, titaVo.getParam("CALDY"))) {
				throw new LogicException("E0015", "??????:S" + row + ",????????????(YYY/MM/DD) = " + SettingDate + " ,?????????????????????");
			}
			occursList.putParam("SettingDate", SettingDate);

//			this.info("Floor=" + makeExcel.getValue(row, 15).toString() + "/"
//					+ toNumeric(makeExcel.getValue(row, 15).toString()) + "/"
//					+ Math.round(toNumeric(makeExcel.getValue(row, 15).toString()) * 100) + "/"
//					+ (float) (Math.round(toNumeric(makeExcel.getValue(row, 15).toString()) * 100) / 100));
			// ??????
			double floorArea = Math.round(toNumeric(makeExcel.getValue(row, 20).toString()) * 100);
			if (floorArea == 0) {
				throw new LogicException("E0015", "??????:T" + row + ",?????? = " + makeExcel.getValue(row, 20).toString());
			}
			occursList.putParam("FloorArea", floorArea / 100);
			// ????????????
			double unitPrice = Math.round(toNumeric(makeExcel.getValue(row, 21).toString()));
			if (unitPrice == 0) {
				throw new LogicException("E0015", "??????:U" + row + ",???????????? = " + makeExcel.getValue(row, 21).toString());
			}
			occursList.putParam("UnitPrice", unitPrice);
			// ????????????
			double evaAmt = Math.round(toNumeric(makeExcel.getValue(row, 22).toString()));
			if (evaAmt == 0) {
				throw new LogicException("E0015", "??????:V" + row + ",???????????? = " + makeExcel.getValue(row, 22).toString());
			}
			occursList.putParam("EvaAmt", evaAmt);
			// ?????????
			occursList.putParam("Tax", Math.round(toNumeric(makeExcel.getValue(row, 23).toString())));
			// ??????
			occursList.putParam("NetWorth", Math.round(toNumeric(makeExcel.getValue(row, 24).toString())));
			// ????????????
			double c = Math.round(toNumeric(makeExcel.getValue(row, 27).toString()) * 10000);
			if (c == 0) {
				throw new LogicException("E0015", "??????:AA" + row + ",???????????? = " + makeExcel.getValue(row, 27).toString());
			}
			occursList.putParam("LoanToValue", c / 100);
			// ????????????
			double seetingAmt = toNumeric(makeExcel.getValue(row, 30).toString()) * 1000;
			if (seetingAmt == 0) {
				throw new LogicException("E0015", "??????:AD" + row + ",???????????? = " + makeExcel.getValue(row, 30).toString());
			}
			occursList.putParam("SettingAmt", seetingAmt);

			// ????????????
			String ownerString = makeExcel.getValue(row, 32).toString().trim();
			occursList.putParam("Owner", ownerString);
			double rate = 0;
			if (ownerString.isEmpty()) {
			} else {
				String[] owners = ownerString.split("/");
				for (int j = 0; j < owners.length; j++) {
					String[] s = owners[j].split(",");
					if (custId.equals(s[0])) {
						if (owners.length == 1) {
							rate = 1;
						} else {
							if (s.length != 5) {
								throw new LogicException("E0015",
										"??????:AF" + row + ",???" + (j + 1) + "????????????????????????????????? = " + ownerString);
							}
							double part = toNumeric(s[3]);
							double total = toNumeric(s[4]);
							if (part == 0 || total == 0 || part > total) {
								throw new LogicException("E0015",
										"??????:AF" + row + ",???" + (j + 1) + "????????????????????????????????? = " + ownerString);
							}
							rate += part / total;
						}
					} else {
						if (s.length < 3) {
							throw new LogicException("E0015",
									"??????:AF" + row + ",???" + (j + 1) + "????????????????????????????????? = " + ownerString);
						}
						if (!StaticTool.checkID(s[0])) {
							throw new LogicException("E0015", "??????:AF" + row + ",???" + (j + 1) + "???????????????ID???????????? = " + s[0]);
						}
						String relaCode = toString(s[2], 2);
						String relaCodeX = checkCode(titaVo, "GuaRelCode", relaCode, "AF" + row, "??????????????????", s[2]);
						if (owners.length == 1) {
							rate = 1;
						} else {
							if (s.length != 5) {
								throw new LogicException("E0015",
										"??????:AF" + row + ",???" + (j + 1) + "????????????????????????????????? = " + ownerString);
							}
							double part = toNumeric(s[3]);
							double total = toNumeric(s[4]);
							if (part == 0 || total == 0 || part > total) {
								throw new LogicException("E0015",
										"??????:AF" + row + ",???" + (j + 1) + "????????????????????????????????? = " + ownerString);
							}
							rate += part / total;
						}
					}
				}
//				rate = Math.round(rate);
//				this.info("row " + row + " / rate =" + rate);
				if (rate != 1) {
					throw new LogicException("E0015", "??????:AF" + row + "????????????????????????????????????100%");

				}
			}

			// ??????
			// ???????????????
			String InsuNo = makeExcel.getValue(row, 33).toString();
			occursList.putParam("InsuNo", InsuNo);

			if (InsuNo.isEmpty()) {
				occursList.putParam("InsuCompany", "");
				occursList.putParam("InsuCompanyX", "");
				occursList.putParam("InsuTypeCode", "");
				occursList.putParam("InsuTypeCodeX", "");
				occursList.putParam("FireInsuCovrg", 0);
				occursList.putParam("FireInsuPrem", 0);
				occursList.putParam("EthqInsuCovrg", 0);
				occursList.putParam("EthqInsuPrem", 0);
				occursList.putParam("InsuStartDate", 0);
				occursList.putParam("InsuEndDate", 0);

			} else {
				// ????????????
				String InsuCompany = toString(makeExcel.getValue(row, 34).toString(), 2);
				String InsuCompanyX = checkCode(titaVo, "InsuCompany", InsuCompany, "AH" + row, "????????????",
						makeExcel.getValue(row, 34).toString());

				occursList.putParam("InsuCompany", InsuCompany);
				occursList.putParam("InsuCompanyX", InsuCompanyX);
				// ????????????
				String InsuTypeCode = toString(makeExcel.getValue(row, 35).toString(), 2);
				String InsuTypeCodeX = checkCode(titaVo, "InsuTypeCode", InsuTypeCode, "AI" + row, "????????????",
						makeExcel.getValue(row, 35).toString());
				occursList.putParam("InsuTypeCode", InsuTypeCode);
				occursList.putParam("InsuTypeCodeX", InsuTypeCodeX);

				// ?????????????????????(??????)
				double FireInsuCovrg = toNumeric(makeExcel.getValue(row, 36).toString()) * 1000;
				if (FireInsuCovrg <= 0) {
					throw new LogicException("E0015",
							"??????:AJ" + row + ",?????????????????????(??????) = " + makeExcel.getValue(row, 36).toString());
				}
				occursList.putParam("FireInsuCovrg", FireInsuCovrg);

				// ???????????????
				double FireInsuPrem = toNumeric(makeExcel.getValue(row, 37).toString());
				if (FireInsuPrem <= 0) {
					throw new LogicException("E0015",
							"??????:AK" + row + ",??????????????? = " + makeExcel.getValue(row, 37).toString());
				}
				occursList.putParam("FireInsuPrem", FireInsuPrem);

				// ?????????????????????(??????)
				double EthqInsuCovrg = toNumeric(makeExcel.getValue(row, 38).toString()) * 1000;
				if (EthqInsuCovrg < 0) {
					throw new LogicException("E0015",
							"??????:AL" + row + ",?????????????????????(??????) = " + makeExcel.getValue(row, 38).toString());
				}
				occursList.putParam("EthqInsuCovrg", EthqInsuCovrg);

				// ???????????????
				double EthqInsuPrem = toNumeric(makeExcel.getValue(row, 39).toString());
				if (EthqInsuCovrg > 0 && EthqInsuPrem <= 0) {
					throw new LogicException("E0015",
							"??????:AM" + row + ",??????????????? = " + makeExcel.getValue(row, 39).toString());
				}
				occursList.putParam("EthqInsuPrem", EthqInsuPrem);

				// ????????????
				String InsuStartDate = makeExcel.getValue(row, 40).toString();
				if (InsuStartDate.length() != 9) {
					throw new LogicException("E0015", "??????:AN" + row + ",????????????(YYY/MM/DD) = " + InsuStartDate);
				}
				if (!checkDate(InsuStartDate)) {
					throw new LogicException("E0015", "??????:AN" + row + ",????????????(YYY/MM/DD) = " + InsuStartDate);
				}
				occursList.putParam("InsuStartDate", InsuStartDate);

				// ????????????
				String InsuEndDate = makeExcel.getValue(row, 41).toString();
				if (InsuEndDate.length() != 9) {
					throw new LogicException("E0015", "??????:AO" + row + ",????????????(YYY/MM/DD) = " + InsuEndDate);
				}
				if (!checkDate(InsuEndDate)) {
					throw new LogicException("E0015", "??????:AO" + row + ",????????????(YYY/MM/DD) = " + InsuEndDate);
				}
				if (!compareDate(InsuStartDate, InsuEndDate)) {
					throw new LogicException("E0015", "??????:AO" + row + ",????????????>????????????");
				}
				occursList.putParam("InsuEndDate", InsuEndDate);
			}

			this.totaVo.addOccursList(occursList);
			cnt++;
		}

		this.info("Total = " + cnt);

	}

	// ????????????
	private void step2(TitaVo titaVo) throws LogicException {
		int Idx = parse.stringToInteger(titaVo.getParam("Idx"));

		this.info("active Step2 = " + Idx);

		int applNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		FacMain facMain = sFacMainService.facmApplNoFirst(applNo, titaVo);
		if (facMain == null) {
			throw new LogicException("E2019", "???????????? = " + applNo);
		}

		CustMain custMain = sCustMainService.custNoFirst(facMain.getCustNo(), facMain.getCustNo(), titaVo);
		if (custMain == null) {
			throw new LogicException("E1004", "?????? = " + facMain.getCustNo());
		}

		String fileItem = titaVo.getParam("FileItem");
		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + fileItem;

		this.info("fileitem=" + fileItem);
		makeExcel.openExcel(fileName, "??????????????????");

		int clCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int clCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		if (clCode1 == 0 || clCode2 == 0) {
			throw new LogicException("E0019", "?????????????????????");
		}

		ClMain clMain = new ClMain();
		ClMainId clMainId = new ClMainId();

		clMainId.setClCode1(clCode1);
		clMainId.setClCode2(clCode2);

		boolean newfg = false; // ??????
		if (clNo == 0) {

			newfg = true; // ??????

			// ??????
			String clCode = StringUtils.leftPad(String.valueOf(clCode1), 2, "0")
					+ StringUtils.leftPad(String.valueOf(clCode2), 2, "0");

			clNo = gSeqCom.getSeqNo(0, 0, "L2", clCode, 9999999, titaVo);

			makeExcel.setValueInt(Idx + 2, 4, clNo);

			clMainId.setClNo(clNo);
			clMain.setClMainId(clMainId);

		} else {
			clMainId.setClNo(clNo);

			clMain = sClMainService.findById(clMainId, titaVo);
			if (clMain == null) {
				String clNoX = String.format("%01d-%02d-%07d", clCode1, clCode2, clNo);
				throw new LogicException("E0001", "???????????????(ClMain)=" + clNoX);
			}
		}

		String clNoX = String.format("%01d-%02d-%07d", clCode1, clCode2, clNo);

		this.info("L2419 ClCode = " + clNoX);

		// ?????????????????????
		clMain.setClTypeCode(titaVo.getParam("TypeCode"));
		// ?????????
		clMain.setCityCode(titaVo.getParam("CityCode"));
		// ?????????
		clMain.setAreaCode(titaVo.getParam("AreaCode"));
		// ??????????????????-1:?????????
		clMain.setClStatus("1");
		// ????????????
		int evaDate = parse.stringToInteger(titaVo.getParam("EvaDate"));
		clMain.setEvaDate(evaDate);
		// ????????????
		clMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));

		if (newfg) {
			try {
				sClMainService.insert(clMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "???????????????(ClMain)" + e.getErrorMsg());
			}
		} else {
			try {
				clMain = sClMainService.update2(clMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "???????????????(ClMain)" + e.getErrorMsg());
			}

		}

		ClImmId clImmId = new ClImmId();

		clImmId.setClCode1(clCode1);
		clImmId.setClCode2(clCode2);
		clImmId.setClNo(clNo);

		ClImm clImm = sClImmService.findById(clImmId, titaVo);
		if (clImm == null) {
			newfg = true;

			clImm = new ClImm();
			clImm.setClImmId(clImmId);
		} else {
			newfg = false;
		}

		// ????????????
		clImm.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("NetWorth")));
		// ???????????????
		clImm.setLVITax(parse.stringToBigDecimal(titaVo.getParam("Tax")));
		// ??????????????????
		clImm.setRentEvaValue(BigDecimal.ZERO);
		// ?????????
		clImm.setRentPrice(parse.stringToBigDecimal(titaVo.getParam("RentPrice")));
		// ????????????:1:?????????
		clImm.setOwnershipCode("1");
		// ???????????????:0:?????????????????????
		clImm.setMtgCode("0");
		// ??????????????????????????????????????????-??????
		clImm.setMtgCheck("Y");
		// ??????????????????????????????????????????-??????
		clImm.setMtgLoan("Y");
		// ??????????????????????????????????????????-????????????
		clImm.setMtgPledge("Y");
		// ???????????????
		clImm.setAgreement("Y");
		// ????????????
		clImm.setEvaCompanyCode(titaVo.getParam("EvaCompany"));
		// ????????????:1:??????
		clImm.setClCode("1");
		// ????????????(%)
		clImm.setLoanToValue(parse.stringToBigDecimal(titaVo.getParam("LoanToValue")));
		// ????????????:1:??????
		clImm.setSettingStat("1");
		// ???????????????:0:??????
		clImm.setClStat("0");
		// ????????????
		clImm.setSettingDate(getDate(titaVo.getParam("SettingDate")));
		// ????????????
		clImm.setSettingAmt(parse.stringToBigDecimal(titaVo.getParam("SettingAmt")));
		// ????????????????????????:????????????+30???
		clImm.setClaimDate(clImm.getSettingDate() + 300000);
		// ????????????(1~9)
		clImm.setSettingSeq("1");

		if (newfg) {
			try {
				sClImmService.insert(clImm, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "?????????????????????(ClImm)" + e.getErrorMsg());
			}
		} else {
			try {
				clImm = sClImmService.update2(clImm, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "?????????????????????(ClImm)" + e.getErrorMsg());
			}
		}

		if (clCode1 == 1) {

			ClBuildingId clBuildingId = new ClBuildingId();

			clBuildingId.setClCode1(clCode1);
			clBuildingId.setClCode2(clCode2);
			clBuildingId.setClNo(clNo);

			ClBuilding clBuilding = sClBuildingService.findById(clBuildingId, titaVo);
			if (clBuilding == null) {
				newfg = true;
				clBuilding = new ClBuilding();
				clBuilding.setClBuildingId(clBuildingId);
			} else {
				newfg = false;
			}

			// ??????
			clBuilding.setCityCode(clMain.getCityCode());
			// ????????????
			clBuilding.setAreaCode(clMain.getAreaCode());
			// ???????????????
			clBuilding.setIrCode(titaVo.getParam("IrCode"));
			// ??????
			clBuilding.setRoad(titaVo.getParam("Road"));
			// ????????????
			clBuilding.setBdLocation(
					titaVo.getParam("CityCodeX") + titaVo.getParam("AreaCodeX") + titaVo.getParam("Road"));
			// ??????
			clBuilding.setBdNo1(titaVo.getParam("BdNo1"));
			clBuilding.setBdNo2(titaVo.getParam("BdNo2"));
			// ??????????????????
			clBuilding.setBdMainUseCode(titaVo.getParam("UseCode"));
			// ???????????????:6:??????
			clBuilding.setBdUsageCode("6");
			// ??????????????????
			clBuilding.setBdMtrlCode(titaVo.getParam("MtrlCode"));
			// ????????????
			clBuilding.setBdTypeCode(titaVo.getParam("BuTypeCode"));
			// ?????????
			clBuilding.setTotalFloor(parse.stringToInteger(titaVo.getParam("TotalFloor")));
			// ?????????????????????
			clBuilding.setFloorNo(titaVo.getParam("FloorNo"));
			// ???????????????????????????
			clBuilding.setFloorArea(parse.stringToBigDecimal(titaVo.getParam("FloorArea")));
			// ????????????/???
			clBuilding.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("UnitPrice")));
			// ????????????:07:?????? ???
			clBuilding.setRoofStructureCode("07");
			// ??????????????????
			clBuilding.setBdDate(getDate(titaVo.getParam("BdDate")));
			// ??????????????????,?????????"??????????????????"
			clBuilding.setHouseBuyDate(clBuilding.getBdDate());

			if (newfg) {
				try {
					clBuilding = sClBuildingService.insert(clBuilding, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "?????????????????????????????????(ClBuilding)" + e.getErrorMsg());
				}
			} else {
				try {
					clBuilding = sClBuildingService.update2(clBuilding, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "?????????????????????????????????(ClBuilding)" + e.getErrorMsg());
				}

			}

		} else {
			ClLandId clLandId = new ClLandId();

			clLandId.setClCode1(clCode1);
			clLandId.setClCode2(clCode2);
			clLandId.setClNo(clNo);

			ClLand clLand = sClLandService.findById(clLandId, titaVo);
			if (clLand == null) {
				newfg = true;
				clLand = new ClLand();
				clLand.setClLandId(clLandId);
			} else {
				newfg = false;
			}

			// ????????????(?????????000)
			clLand.setLandSeq(0);
			// ??????
			clLand.setCityCode(clMain.getCityCode());
			// ????????????
			clLand.setAreaCode(clMain.getAreaCode());
			// ???????????????
			clLand.setIrCode(titaVo.getParam("IrCode"));
			// ??????
			clLand.setLandNo1(titaVo.getParam("LdNo1"));
			clLand.setLandNo2(titaVo.getParam("LdNo2"));
			// ????????????
			String landLocation = titaVo.getParam("CityCodeX") + titaVo.getParam("AreaCodeX")
					+ titaVo.getParam("IrCodeX") + "?????????" + titaVo.getParam("LdNo1") + "-" + titaVo.getParam("LdNo2");
			clLand.setLandLocation(landLocation);
			// ???????????????
			clLand.setLVITax(parse.stringToBigDecimal(titaVo.getParam("Tax")));
			// ????????????/???
			clLand.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("UnitPrice")));
			// ??????
			clLand.setArea(parse.stringToBigDecimal(titaVo.getParam("FloorArea")));

			if (newfg) {
				try {
					clLand = sClLandService.insert(clLand, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "?????????????????????????????????(ClLand)" + e.getErrorMsg());
				}
			} else {
				try {
					clLand = sClLandService.update2(clLand, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "?????????????????????????????????(ClLand)" + e.getErrorMsg());
				}

			}
		}

		ownerids.clear();

		String ownerString = titaVo.getParam("Owner").toString().trim();

		this.info("OwnerString = " + ownerString);

		if (ownerString.isEmpty()) {
			buildOwner(titaVo, clCode1, clCode2, clNo, custMain.getCustId(), custMain.getCustName(), "00",
					new BigDecimal("1"), new BigDecimal("1"));
		} else {
			String[] owners = ownerString.split("/");
			for (int j = 0; j < owners.length; j++) {
				String[] s = owners[j].split(",");
				if (custMain.equals(s[0])) {
					if (owners.length == 1) {
						buildOwner(titaVo, clCode1, clCode2, clNo, custMain.getCustId(), custMain.getCustName(), "00",
								new BigDecimal("1"), new BigDecimal("1"));
					} else {
						buildOwner(titaVo, clCode1, clCode2, clNo, custMain.getCustId(), custMain.getCustName(), "00",
								new BigDecimal(s[3]), new BigDecimal(s[4]));
					}
				} else {
					String relaCode = toString(s[2], 2);
					if (owners.length == 1) {
						buildOwner(titaVo, clCode1, clCode2, clNo, s[0], s[1], relaCode, new BigDecimal("1"),
								new BigDecimal("1"));
					} else {
						buildOwner(titaVo, clCode1, clCode2, clNo, s[0], s[1], relaCode, new BigDecimal(s[3]),
								new BigDecimal(s[4]));
					}
				}

			}
		}

		// ?????????????????? owner

		if (clCode1 == 1) {
			Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(clCode1, clCode2, clNo, 0,
					Integer.MAX_VALUE, titaVo);
			List<ClBuildingOwner> lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
			if (lClBuildingOwner != null) {
				for (ClBuildingOwner clBuildingOwner : lClBuildingOwner) {
					ClBuildingOwnerId clBuildingOwnerId = clBuildingOwner.getClBuildingOwnerId();
					this.info("ClBuildingOwner=" + clBuildingOwnerId.getClCode1() + "-" + clBuildingOwnerId.getClCode2()
							+ "-" + clBuildingOwnerId.getClNo() + "/" + clBuildingOwnerId.getOwnerCustUKey());
					if (ownerids.get(clBuildingOwnerId.getOwnerCustUKey()) == null) {
//						this.info("[" + clBuildingOwner.getOwnerCustUKey() + "] is null / " + ownerids.size());
						try {
							sClBuildingOwnerService.delete(clBuildingOwner, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0008", "?????????-?????????????????????(ClBuildingOwner)" + e.getErrorMsg());
						}
					}
				}
			}
		} else {
			Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.clNoEq(clCode1, clCode2, clNo, 0, Integer.MAX_VALUE,
					titaVo);
			List<ClLandOwner> lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();
			if (lClLandOwner != null) {
				for (ClLandOwner clLandOwner : lClLandOwner) {
					ClLandOwnerId clLandOwnerId = clLandOwner.getClLandOwnerId();
					this.info("clLandOwner=" + clLandOwnerId.getClCode1() + "-" + clLandOwnerId.getClCode2() + "-"
							+ clLandOwnerId.getClNo() + "/" + clLandOwnerId.getOwnerCustUKey());
					if (ownerids.get(clLandOwnerId.getOwnerCustUKey()) == null) {
//						this.info("[" + clBuildingOwner.getOwnerCustUKey() + "] is null / " + ownerids.size());
						try {
							sClLandOwnerService.delete(clLandOwner, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0008", "?????????-?????????????????????(ClBuildingOwner)" + e.getErrorMsg());
						}
					}
				}
			}
		}

		ClFacId clFacId = new ClFacId();
		clFacId.setClCode1(clCode1);
		clFacId.setClCode2(clCode2);
		clFacId.setClNo(clNo);
		clFacId.setApproveNo(applNo);

		ClFac clFac = sClFacService.findById(clFacId, titaVo);
		if (clFac == null) {
			newfg = true;
			clFac = new ClFac();
			clFac.setClFacId(clFacId);
		} else {
			newfg = false;
		}

		clFac.setCustNo(facMain.getCustNo());
		clFac.setFacmNo(facMain.getFacmNo());

		if (newfg) {
			try {
				sClFacService.insert(clFac, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "???????????????????????????(ClFac)" + e.getErrorMsg());
			}
		} else {
			try {
				clFac = sClFacService.update2(clFac, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "???????????????????????????(ClFac)" + e.getErrorMsg());
			}
		}

		this.totaVo.putParam("ClCode1", clCode1);
		this.totaVo.putParam("ClCode2", clCode2);
		this.totaVo.putParam("ClNo", clNo);
		this.totaVo.putParam("ClCode", clNoX);

		String InsuNo = titaVo.getParam("InsuNo").toString().trim();
		if (!InsuNo.isEmpty()) {
			InsuOrignalId insuOrignalId = new InsuOrignalId();
			insuOrignalId.setClCode1(clCode1);
			insuOrignalId.setClCode2(clCode2);
			insuOrignalId.setClNo(clNo);
			insuOrignalId.setOrigInsuNo(InsuNo);
			insuOrignalId.setEndoInsuNo(" ");
			InsuOrignal insuOrignal = insuOrignalService.findById(insuOrignalId, titaVo);
			if (insuOrignal == null) {
				insuOrignal = new InsuOrignal();
				insuOrignal.setInsuOrignalId(insuOrignalId);
				insuOrignal = toInsuOrignal(titaVo, insuOrignal);

				try {
					insuOrignalService.insert(insuOrignal, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "???????????????" + e.getErrorMsg());
				}
			} else {
				insuOrignal = toInsuOrignal(titaVo, insuOrignal);
				try {
					insuOrignal = insuOrignalService.update2(insuOrignal, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "???????????????" + e.getErrorMsg());
				}
			}
		}

		makeExcel.saveExcel(fileName);

		if (titaVo.getSelectTotal() == titaVo.getSelectIndex()) {
			long fileno = toTxFile(titaVo, fileName);

			String msg = "???????????????????????????????????????" + titaVo.getSelectTotal() + "?????????????????????????????????????????????????????????????????????";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					String.format("%-8s", titaVo.getTlrNo().trim()) + "L2419", msg, titaVo);
		}

	}

	private void buildOwner(TitaVo titaVo, int clCode1, int clCode2, int clNo, String ownerId, String ownerName,
			String relCode, BigDecimal part, BigDecimal total) throws LogicException {

		CustMain custMain = sCustMainService.custIdFirst(ownerId, titaVo);

		if (custMain == null) {
			String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			custMain = new CustMain();
			custMain.setCustUKey(Ukey);
			custMain.setCustId(ownerId);
			custMain.setCustName(ownerName);
			custMain.setDataStatus(1);
			custMain.setTypeCode(2);
			try {
				sCustMainService.insert(custMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "??????????????????");
			}

		}

		this.info("owner =" + clCode1 + "-" + clCode2 + "-" + clNo + "/" + custMain.getCustId());

		if (clCode1 == 1) {
			ClBuildingOwnerId clBuildingOwnerId = new ClBuildingOwnerId();
			clBuildingOwnerId.setClCode1(clCode1);
			clBuildingOwnerId.setClCode2(clCode2);
			clBuildingOwnerId.setClNo(clNo);
			clBuildingOwnerId.setOwnerCustUKey(custMain.getCustUKey());

			ClBuildingOwner clBuildingOwner = sClBuildingOwnerService.findById(clBuildingOwnerId, titaVo);

			boolean newfg = false;
			if (clBuildingOwner == null) {
				newfg = true;

				clBuildingOwner = new ClBuildingOwner();
				clBuildingOwner.setClBuildingOwnerId(clBuildingOwnerId);
			}

			// ??????????????????
			clBuildingOwner.setOwnerRelCode(relCode);
			// ????????????(??????)
			clBuildingOwner.setOwnerPart(part);
			// ????????????(??????)
			clBuildingOwner.setOwnerTotal(total);

			if (newfg) {
				try {
					sClBuildingOwnerService.insert(clBuildingOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "?????????-?????????????????????(ClBuildingOwner)" + e.getErrorMsg());
				}
			} else {
				try {
					clBuildingOwner = sClBuildingOwnerService.update2(clBuildingOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "?????????-?????????????????????(ClBuildingOwner)" + e.getErrorMsg());
				}
			}
		} else {
			ClLandOwnerId clLanOwnerId = new ClLandOwnerId();
			clLanOwnerId.setClCode1(clCode1);
			clLanOwnerId.setClCode2(clCode2);
			clLanOwnerId.setClNo(clNo);
			clLanOwnerId.setOwnerCustUKey(custMain.getCustUKey());

			ClLandOwner clLandOwner = sClLandOwnerService.findById(clLanOwnerId, titaVo);

			boolean newfg = false;
			if (clLandOwner == null) {
				newfg = true;

				clLandOwner = new ClLandOwner();
				clLandOwner.setClLandOwnerId(clLanOwnerId);
			}

			// ??????????????????
			clLandOwner.setOwnerRelCode(relCode);
			// ????????????(??????)
			clLandOwner.setOwnerPart(part);
			// ????????????(??????)
			clLandOwner.setOwnerTotal(total);

			if (newfg) {
				try {
					sClLandOwnerService.insert(clLandOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "?????????-?????????????????????(ClBuildingOwner)" + e.getErrorMsg());
				}
			} else {
				try {
					clLandOwner = sClLandOwnerService.update2(clLandOwner, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "?????????-?????????????????????(ClBuildingOwner)" + e.getErrorMsg());
				}
			}

		}

		this.info("ownerids put = " + custMain.getCustUKey());
		ownerids.put(custMain.getCustUKey(), custMain.getCustUKey());
	}

	private InsuOrignal toInsuOrignal(TitaVo titaVo, InsuOrignal insuOrignal) throws LogicException {

		// ????????????
		insuOrignal.setInsuCompany(titaVo.getParam("InsuCompany"));
		// ????????????
		insuOrignal.setInsuTypeCode(titaVo.getParam("InsuTypeCode"));
		// ?????????????????????
		insuOrignal.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("FireInsuCovrg")));
		// ?????????????????????
		insuOrignal.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("EthqInsuCovrg")));
		// ???????????????
		insuOrignal.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("FireInsuPrem")));
		// ???????????????
		insuOrignal.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("EthqInsuPrem")));
		// ????????????
		insuOrignal.setInsuStartDate(getDate(titaVo.getParam("InsuStartDate")));
		// ????????????
		insuOrignal.setInsuEndDate(getDate(titaVo.getParam("InsuEndDate")));

		return insuOrignal;
	}

	private long toTxFile(TitaVo titaVo, String filename) throws LogicException {
		TxFile txFile = new TxFile();

		txFile.setFileDate(titaVo.getEntDyI());
		txFile.setFileCode(titaVo.getTxCode());
		txFile.setFileItem(titaVo.getParam("FileItem"));
		txFile.setFileType(7);
		txFile.setFileOutput(titaVo.getParam("FileItem"));
		txFile.setFileZip(zLibUtils.compress(new File(filename)));
		txFile.setBrNo(titaVo.getKinbr());

		try {
			txFile = sTxFileService.insert(txFile, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "?????????(TxFile):" + e.getErrorMsg());
		}

		return txFile.getFileNo();
	}

	private int getDate(String s) throws LogicException {
		int r = 0;

		s = s.replace("/", "");

		r = parse.stringToInteger(s);

		return r;

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

		if (items.size() > 0) {
			if (items.get(k) != null) {
				rs = items.get(k).toString();
			}
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
		throw new LogicException("E0015", "??????:" + column + ",???" + desc + "????????? = " + code);
	}

	private double toNumeric(String s) {
		float r = 0;
		try {
			r = parse.stringToFloat(s);
		} catch (Exception e) {
			r = 0;
		}

		return r;
	}

	private String toString(String s, int len) {
		float r = 0;
		try {
			r = parse.stringToFloat(s);
		} catch (Exception e) {
			r = 0;
		}

		return String.format("%0" + len + "d", Math.round(r));
	}
}