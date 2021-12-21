package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R19")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R19 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R19.class);
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CustTelNoService sCustTelNoService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdLandSectionService sCdLandSectionService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L8R19");
		this.info("active L8R19 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		this.info("L8R19 iCustId=[" + iCustId + "]");
		CustMain CustMainVo = sCustMainService.custIdFirst(iCustId, titaVo);

		String L8r19CustRegAddr = "";// 債務人戶籍之郵遞區號及地址
		String L8r19CustComAddr = "";// 債務人通訊地之郵遞區號及地址
		String L8r19CustRegTelNo = "";// 債務人戶籍電話
		String L8r19CustComTelNo = "";// 債務人通訊電話
		String L8r19CustMobilNo = "";// 債務人行動電話
		if (CustMainVo != null) {
			String CustUKey = CustMainVo.getCustUKey();
			// 地址
			Map<String, String> Data = Address(CustMainVo);
			L8r19CustRegAddr = Data.get("RegAddr");
			L8r19CustComAddr = Data.get("ComAddr");

			// 連絡電話
			/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
			this.index = 0;
			/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
			this.limit = Integer.MAX_VALUE;// 查全部

			Slice<CustTelNo> slCustTelNo = sCustTelNoService.findCustUKey(CustUKey, this.index, this.limit, titaVo);
			List<CustTelNo> lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();
			this.info("L8R19 CustTelNo CustUKey=[" + CustUKey + "]");
			if (lCustTelNo != null) {
				for (CustTelNo CustTelNoVo : lCustTelNo) {
					String TelTypeCode = CustTelNoVo.getTelTypeCode();// 電話種類 01:公司,02:住家,03:手機,04:傳真,05:簡訊,06:催收聯絡,09:其他
					String Enable = CustTelNoVo.getEnable();// 啟用記號 Y:啟用,N:不啟用
					String RelationCode = CustTelNoVo.getRelationCode();// 與借款人關係 00:本人,01:夫,02:妻,03:父,04:母,05:子,06:女,07:兄,08:弟,09:姊,10:妹,11:姪子,99:其他
					this.info("L8R19 CustTelNo TelTypeCode=[" + TelTypeCode + "],Enable=[" + Enable + "],RelationCode=[" + RelationCode + "]");
					if (("Y").equals(Enable)) {
						String TelArea = CustTelNoVo.getTelArea();// 電話區碼
						String TelNo = CustTelNoVo.getTelNo();// 電話號碼
						// String TelExt=CustTelNoVo.getTelExt();//分機號碼
						// String Mobile = CustTelNoVo.getTelNo();// 手機號碼
						if (("01").equals(TelTypeCode)) {
							String OTelNo = "";
							if (TelArea != null && TelArea.trim().length() != 0) {
								if (TelNo != null && TelNo.trim().length() != 0) {
									OTelNo = TelArea + "-" + TelNo;
								}
							} else {
								if (TelNo != null && TelNo.trim().length() != 0) {
									OTelNo = TelNo;
								}
							}
							L8r19CustComTelNo = OTelNo;// 債務人通訊電話
						} else if (("02").equals(TelTypeCode)) {
							// 02:住家
							if (L8r19CustComTelNo != null && L8r19CustComTelNo.trim().length() != 0) {
								continue;
							}
							String OTelNo = "";
							if (TelArea != null && TelArea.trim().length() != 0) {
								if (TelNo != null && TelNo.trim().length() != 0) {
									OTelNo = TelArea + "-" + TelNo;
								}
							} else {
								if (TelNo != null && TelNo.trim().length() != 0) {
									OTelNo = TelNo;
								}
							}
							L8r19CustRegTelNo = OTelNo;// 債務人戶籍電話
						} else if (("03").equals(TelTypeCode)) {
							// 03:手機
							if (L8r19CustMobilNo != null && L8r19CustMobilNo.trim().length() != 0) {
								continue;
							}
							L8r19CustMobilNo = TelNo;// 債務人行動電話
						}
					}

				}
			}
		} else {
			// throw new LogicException(titaVo, "E8002", "L8R19");//400無此債務人聯絡資料-改在前端處理
		}

		// 該長度已L8309(JcicZ048)為主
		totaVo.putParam("L8r19CustRegAddr", CutLength(L8r19CustRegAddr, 38));// 債務人戶籍之郵遞區號及地址
		totaVo.putParam("L8r19CustComAddr", CutLength(L8r19CustComAddr, 38));// 債務人通訊地之郵遞區號及地址
		totaVo.putParam("L8r19CustRegTelNo", CutLength(L8r19CustRegTelNo, 16));// 債務人戶籍電話
		totaVo.putParam("L8r19CustComTelNo", CutLength(L8r19CustComTelNo, 16));// 債務人通訊電話
		totaVo.putParam("L8r19CustMobilNo", CutLength(L8r19CustMobilNo, 16));// 債務人行動電話
		this.addList(this.totaVo);
		return this.sendList();
	}

	public String CutLength(String str, int maxLength) {
		if (str != null && str.length() != 0) {
			int strL = str.length();
			if (strL > maxLength) {
				str = str.substring(0, maxLength);
			}
		} else {
			str = "";
		}
		return str;
	}

	public Map<String, String> Address(CustMain CustMainVo) {
		String RegAddr = "";// 戶籍之郵遞區號及地址
		String ComAddr = "";// 訊地之郵遞區號及地址

		String RegZip3 = CustMainVo.getRegZip3().trim();// 戶籍-郵遞區號前三碼
		String RegZip2 = CustMainVo.getRegZip2().trim();// 戶籍-郵遞區號後兩碼
		String RegCityCode = CustMainVo.getRegCityCode().trim();// 戶籍-縣市代碼 地區別與鄉鎮區對照檔CdArea
		String RegAreaCode = CustMainVo.getRegAreaCode().trim();// 戶籍-鄉鎮市區代碼 地區別與鄉鎮區對照檔CdArea
		// String RegIrCode=CustMainVo.getRegIrCode();//戶籍-段/小段代碼 地段代碼檔CdLandSection
		String RegIrCode = "";// 戶籍-段/小段代碼 地段代碼檔CdLandSection
		String RegRoad = CustMainVo.getRegRoad().trim();// 戶籍-路名
		String RegSection = CustMainVo.getRegSection().trim();// 戶籍-段
		String RegAlley = CustMainVo.getRegAlley().trim();// 戶籍-巷
		String RegLane = CustMainVo.getRegLane().trim();// 戶籍-弄
		String RegNum = CustMainVo.getRegNum().trim();// 戶籍-號
		String RegNumDash = CustMainVo.getRegNumDash().trim();// 戶籍-號之
		String RegFloor = CustMainVo.getRegFloor().trim();// 戶籍-樓
		String RegFloorDash = CustMainVo.getRegFloorDash().trim();// 戶籍-樓之

		String CurrZip3 = CustMainVo.getCurrZip3().trim();// 通訊-郵遞區號前三碼
		String CurrZip2 = CustMainVo.getCurrZip2().trim();// 通訊-郵遞區號後兩碼
		String CurrCityCode = CustMainVo.getCurrCityCode().trim();// 通訊-縣市代碼 地區別與鄉鎮區對照檔CdArea
		String CurrAreaCode = CustMainVo.getCurrAreaCode().trim();// 通訊-鄉鎮市區代碼 地區別與鄉鎮區對照檔CdArea
		// String CurrIrCode=CustMainVo.getCurrIrCode();//通訊-段/小段代碼 地段代碼檔CdLandSection
		String CurrIrCode = "";// 通訊-段/小段代碼 地段代碼檔CdLandSection
		String CurrRoad = CustMainVo.getCurrRoad().trim();// 通訊-路名
		String CurrSection = CustMainVo.getCurrSection().trim();// 通訊-段
		String CurrAlley = CustMainVo.getCurrAlley().trim();// 通訊-巷
		String CurrLane = CustMainVo.getCurrLane().trim();// 通訊-弄
		String CurrNum = CustMainVo.getCurrNum().trim();// 通訊-號
		String CurrNumDash = CustMainVo.getCurrNumDash().trim();// 通訊-號之
		String CurrFloor = CustMainVo.getCurrFloor().trim();// 通訊-樓
		String CurrFloorDash = CustMainVo.getCurrFloorDash().trim();// 通訊-樓之
		// (0-1)2縣/市CdArea 3鄉鎮市區CdArea 4段CdLandSection 5路名 6段 7巷 8弄 9號 10號之 11樓 12樓之
		String RegAddrData[] = { RegZip3, RegZip2, RegCityCode, RegAreaCode, RegIrCode, RegRoad, RegSection, RegAlley, RegLane, RegNum, RegNumDash, RegFloor, RegFloorDash };
		String CurrData[] = { CurrZip3, CurrZip2, CurrCityCode, CurrAreaCode, CurrIrCode, CurrRoad, CurrSection, CurrAlley, CurrLane, CurrNum, CurrNumDash, CurrFloor, CurrFloorDash };

		String AssignWorld[] = { "路", "段", "巷", "弄", "號", "之", "樓", "之" };
		for (int i = 0; i <= 1; i++) {
			String UseData[] = new String[0];
			StringBuffer sbAddr = new StringBuffer();
			// 使用資料
			if (i == 0) {
				UseData = RegAddrData;
			} else if (i == 1) {
				UseData = CurrData;
			}
			for (int x = 0; x < UseData.length; x++) {
				String ThisValue = UseData[x];
				if (ThisValue != null && ThisValue.trim().length() != 0) {

				} else {
					ThisValue = "";
				}
				// 有值
				if (x == 0) {
					// 郵遞區號前三碼
					String PostalCode3 = ThisValue;
					String PostalCode2 = UseData[x + 1];
					sbAddr.append("(");
					if (PostalCode3 != null && PostalCode3.length() != 0) {
						sbAddr.append(PostalCode3);
					} else {
						sbAddr.append("000");
					}
					// 郵遞區號後兩碼

					x = x + 1;
					sbAddr.append("-");
					if (PostalCode2 != null && PostalCode2.length() != 0) {
						sbAddr.append(PostalCode2);
					} else {
						sbAddr.append("00");
					}
					sbAddr.append(")");
				} else if (x == 2) {
					// 縣市代碼 地區別代碼檔CdCity
					// 鄉鎮市區代碼 地區別與鄉鎮區對照檔CdArea

					// 段/小段代碼 地段代碼檔CdLandSection
					String CityCode = ThisValue;
					String AreaCode = UseData[x + 1];
					String IrCode = UseData[x + 2];
					String Road = UseData[x + 3];
					x = x + 3;

					if (CityCode != null && CityCode.length() != 0) {
						CdCity tCdCityVo = new CdCity();
						tCdCityVo = sCdCityService.findById(CityCode);
						if (tCdCityVo != null) {
							String CityItem = tCdCityVo.getCityItem();
							if (CityItem != null && CityItem.trim().length() != 0) {
								sbAddr.append(CityItem);
							}
						}

						CdArea tCdArea = new CdArea();
						CdAreaId tCdAreaId = new CdAreaId();

						tCdAreaId.setAreaCode(AreaCode);
						tCdAreaId.setCityCode(CityCode);

						tCdArea = sCdAreaService.findById(tCdAreaId);
						if (tCdArea != null) {
							String AreaItem = tCdArea.getAreaItem();// 鄉鎮區名稱
							if (AreaItem != null && AreaItem.trim().length() != 0) {
								sbAddr.append(AreaItem);
							}
//							String CityShort=tCdArea.getCityShort();//縣市簡稱
//							String AreaShort=tCdArea.getAreaShort();//鄉鎮簡稱
//							if(CityShort!=null && CityShort.trim().length()!=0) {
//								sbAddr.append(CityShort);
//							}
//							if(AreaShort!=null && AreaShort.trim().length()!=0) {
//								sbAddr.append(AreaShort);
//							}
						}

						// 區段名
						if (IrCode != null && IrCode.length() != 0) {
							CdLandSection tCdLandSection = new CdLandSection();
							CdLandSectionId tCdLandSectionId = new CdLandSectionId();
							tCdLandSectionId.setAreaCode(AreaCode);
							tCdLandSectionId.setCityCode(CityCode);
							tCdLandSectionId.setIrCode(IrCode);

							tCdLandSection = sCdLandSectionService.findById(tCdLandSectionId);
							if (tCdLandSection != null) {
								String IrItem = tCdLandSection.getIrItem();
								if (IrItem != null && IrItem.trim().length() != 0) {
									sbAddr.append(IrItem);
								}
							}
						}
						// 路名
						if (Road != null && Road.length() != 0) {
							sbAddr.append(Road);
							// sbAddr.append("（路／街／村）");
						}
					}

				} else if (x >= 5 && x <= 12) {
					// 5 路名
					// 6 段
					// 7 巷
					// 8 弄
					// 9 號
					// 10 號之
					// 11 樓
					// 12 樓之
					/*
					 * String assignWorld=""; switch(x) { case 5: assignWorld="路"; break; case 6:
					 * assignWorld="段"; break; case 7: assignWorld="巷"; break; case 8:
					 * assignWorld="弄"; break; case 9: assignWorld="號"; break; case 10:
					 * assignWorld="之"; break; case 11: assignWorld="樓"; break; case 12:
					 * assignWorld="之"; break; }
					 */
					int AssignWorldUse = x - 5;
					String assignWorld = AssignWorld[AssignWorldUse];
					int ThisValueL = ThisValue.length();
					if (AssignWorldUse == 6) {
						if (ThisValue != null && ThisValue.length() != 0) {
							sbAddr.append("，");
						}
					}
					if (ThisValueL > 0) {
						String LastWorld = ThisValue.substring(ThisValueL - 1, ThisValueL);

						if (!(assignWorld).equals(LastWorld)) {
							if (!("之").equals(assignWorld)) {
								sbAddr.append(ThisValue);
								sbAddr.append(assignWorld);
							} else {
								sbAddr.append(assignWorld);
								sbAddr.append(ThisValue);
							}
						}
					}

				}

			}

			// 塞值
			if (i == 0) {
				RegAddr = sbAddr.toString();
			} else if (i == 1) {
				ComAddr = sbAddr.toString();
			}
		}

		Map<String, String> Value = new HashMap<String, String>();
		Value.put("RegAddr", RegAddr);// 戶籍之郵遞區號及地址
		Value.put("ComAddr", ComAddr);
		return Value;
	}
}