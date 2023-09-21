package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5911ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5911")
@Scope("prototype")
/**
 * 撥款件貸款成數統計資料產生
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5911 extends TradeBuffer {
	@Autowired
	public L5911ServiceImpl iL5911ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5911 ");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		this.totaVo.init(titaVo);
		String iDateFm = titaVo.getParam("DateFm");
		String iDateTo = titaVo.getParam("DateTo");
		int xDateFm = Integer.valueOf(iDateFm) + 19110000;
		int xDateTo = Integer.valueOf(iDateTo) + 19110000;
		/*
		 * 需求欄位 案件編號 FacMain.CreditSysNo 核准額度 FacMain.LineAmt，同CreditSysNo相加 借款人戶號
		 * FacMain.CustNo 額度比數 同戶號+同CreditSysNo有多少不同FacmNo或ApplNo 首次撥款日**
		 * 要找首次撥款日落在查詢範圍內的資料 FacMain.FirstDrawdownDate 評估淨值 (猜測是同一額度相加) 成數 (有固定算法) 成數區間
		 * (excel有) 地區別名稱 地區區間 地區別 押租金 房貸專員 原編+姓名 FacMain.BusinessOfficer 核決主管 原編+姓名
		 * FacMain.Supervisor
		 */
		List<Map<String, String>> iL5911SqlReturn = new ArrayList<Map<String, String>>();
		try {
			iL5911SqlReturn = iL5911ServiceImpl.FindData(this.index, this.limit, xDateFm, xDateTo, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5911 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (iL5911SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001", iDateFm + "到" + iDateTo + "期間內查無資料");
		} else {
			if (iL5911SqlReturn != null && iL5911SqlReturn.size() >= this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				// this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			for (Map<String, String> r5911SqlReturn : iL5911SqlReturn) {
				OccursList occursList = new OccursList();
				BigDecimal iPercentage = new BigDecimal("0");
				BigDecimal iConstCount = new BigDecimal("0.95");
				BigDecimal bLineAmt = new BigDecimal("0"); // 核准額度轉bigdecimal
				BigDecimal bTotalEvaAmt = new BigDecimal("0"); // 評估淨值轉bigdecimal
				BigDecimal bTotalRentPrice = new BigDecimal("0"); // 租押金轉bigdeciaml
				if (!r5911SqlReturn.get("F1").equals("")) {
					bLineAmt = new BigDecimal(r5911SqlReturn.get("F1"));
				}
				if (!r5911SqlReturn.get("F10").equals("")) {
					bTotalEvaAmt = new BigDecimal(r5911SqlReturn.get("F10"));
				}
				if (!r5911SqlReturn.get("F11").equals("")) {
					bTotalRentPrice = new BigDecimal(r5911SqlReturn.get("F11"));
				}
				occursList.putParam("OOCreditSysNo", r5911SqlReturn.get("F0")); // 案件編號
				occursList.putParam("OOLineAmt", r5911SqlReturn.get("F1")); // 核准額度
				occursList.putParam("OOCustNo", r5911SqlReturn.get("F2")); // 借款人戶號
				occursList.putParam("OOFacCount", r5911SqlReturn.get("F3")); // 額度比數
				if (r5911SqlReturn.get("F4").equals("")) {
					occursList.putParam("OOFirstDrawdownDate", ""); // 首次撥款日
				} else {
					occursList.putParam("OOFirstDrawdownDate", Integer.valueOf(r5911SqlReturn.get("F4")) - 19110000); // 首次撥款日
				}
				occursList.putParam("OORegCityCode", r5911SqlReturn.get("F12")); // 地區別
				occursList.putParam("OORegCityItem", r5911SqlReturn.get("F15")); // 地區別名稱
				occursList.putParam("OOTotalEvaAmt", r5911SqlReturn.get("F10")); // 評估淨值
				occursList.putParam("OOTotalRentPrice", r5911SqlReturn.get("F11")); // 押租金
				occursList.putParam("OOBusinessOfficer", r5911SqlReturn.get("F5")); // 房貸專員 原編+姓名
				occursList.putParam("OOBusinessOfficerName", r5911SqlReturn.get("F13"));
				occursList.putParam("OOSupervisor", r5911SqlReturn.get("F6")); // 核決主管 原編+姓名
				occursList.putParam("OOSupervisorName", r5911SqlReturn.get("F14"));
				// 地區區間
				switch (r5911SqlReturn.get("F15")) {
				case "台北市":
					occursList.putParam("OORegCityArea", "1"); // 地區區間
					break;
				case "新北市":
					occursList.putParam("OORegCityArea", "2"); // 地區區間
					break;
				case "桃園縣":
					occursList.putParam("OORegCityArea", "3"); // 地區區間
					break;
				case "台中市":
					occursList.putParam("OORegCityArea", "4"); // 地區區間
					break;
				case "高雄市":
					occursList.putParam("OORegCityArea", "5"); // 地區區間
					break;
				default:
					occursList.putParam("OORegCityArea", "6"); // 地區區間
					break;
				}
				this.info("編號==" + r5911SqlReturn.get("F0") + "戶號==" + r5911SqlReturn.get("F2"));
				// 成數
				if (!r5911SqlReturn.get("F1").equals("") && !r5911SqlReturn.get("F10").equals("")) {
					if (r5911SqlReturn.get("F11").equals("0")) {
						// 無租押金
						if(bTotalEvaAmt.compareTo(BigDecimal.ZERO) > 0) {
							iPercentage = bLineAmt.divide(bTotalEvaAmt, 4, RoundingMode.HALF_UP);
						}else {
							iPercentage = BigDecimal.ZERO;
						}
					} else {
						BigDecimal a = new BigDecimal("0");
						BigDecimal b = new BigDecimal("0");
						a = bTotalEvaAmt.subtract(bTotalRentPrice);
						this.info("a==" + a);
						if(a.compareTo(BigDecimal.ZERO) > 0) {
							b = bLineAmt.divide(a, 4, RoundingMode.HALF_UP);
						}else {
							b=BigDecimal.ZERO;
						}
						this.info("b==" + b);
						iPercentage = b.divide(iConstCount, 4, RoundingMode.HALF_UP);
						this.info("成數==" + iPercentage);
					}
				}

				occursList.putParam("OOPerctenage", iPercentage); // 成數
				// 成數區間
				Double dPerctenageArea = iPercentage.doubleValue() * 100;
				if (dPerctenageArea >= 0 && dPerctenageArea < 49) {
					occursList.putParam("OOPerctenageArea", "1"); // 成數區間
				} else if (dPerctenageArea >= 50 && dPerctenageArea < 59) {
					occursList.putParam("OOPerctenageArea", "2"); // 成數區間
				} else if (dPerctenageArea >= 60 && dPerctenageArea < 64) {
					occursList.putParam("OOPerctenageArea", "3"); // 成數區間
				} else if (dPerctenageArea >= 65 && dPerctenageArea < 69) {
					occursList.putParam("OOPerctenageArea", "4"); // 成數區間
				} else if (dPerctenageArea >= 70 && dPerctenageArea < 74) {
					occursList.putParam("OOPerctenageArea", "5"); // 成數區間
				} else if (dPerctenageArea >= 75 && dPerctenageArea < 79) {
					occursList.putParam("OOPerctenageArea", "6"); // 成數區間
				} else {
					occursList.putParam("OOPerctenageArea", "0"); // 成數區間
				}

				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}