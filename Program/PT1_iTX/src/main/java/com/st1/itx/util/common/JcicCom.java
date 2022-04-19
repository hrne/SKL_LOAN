package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
//import org.springframework.stereotype.Service;
//import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
//import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.JcicZ040;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Id;
/* DB服務 */
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.NegFinAcctService;

/* 交易共用組件 */
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * 使用到的地方L8,Jcic報送
 * 
 * @author Jacky Lu
 */
@Component("jcicCom")
@Scope("prototype")
public class JcicCom extends CommBuffer {
	/* DB服務注入 */
	@Autowired
	public NegFinAcctService sNegFinAcctService;
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ045Service sJcicZ045Service;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ050Service sJcicZ050Service;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ448Service sJcicZ448Service;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	public CdCityService sCdCityService;
	/* 轉型共用工具 */
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
	private int index = 0;
	/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
	private Integer limit = Integer.MAX_VALUE;// 查全部

	public JcicCom() {

	}

	public String getPreSubmitKey() {
		return "458";
	}

	public String getDeleteFunctionCode() {
		// String FunctionCd=titaVo.getParam("FunctionCd").trim();
		// //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		return "04";
	}

	public boolean JcicOutDateCanUpdByUser(TitaVo titaVo) throws LogicException {
		boolean TF = false;
		// L6064->L6604
		CdCode tCdCode = sCdCodeService.getItemFirst(8, "UpdOutJcictxtDate", "Upd", titaVo);
		if (tCdCode != null) {
			String Item = tCdCode.getItem();
			if (Item != null && Item.length() != 0) {
				String Item1 = Item.substring(0, 1);
				if (("1").equals(Item1)) {
					TF = true;
				} else if (("0").equals(Item1)) {
					TF = false;
				} else {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "業務類別:8,代碼檔代號:UpdOutJcictxtDate,代碼:Upd,說明:第一碼不為0或1");
				}
			} else {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "業務類別:8,代碼檔代號:UpdOutJcictxtDate,代碼:Upd,說明:[未填寫] 格式應為 [1,1:可由USER修改;0:不可由USER修改]");
			}

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "查無CdCode資料,請至L6064新增[8,UpdOutJcictxtDate,Upd]");
		}
		return TF;
	}

	public boolean DeleteLogic(TitaVo titaVo, Object obj, int OutJcicTxtDate) throws LogicException {
		boolean TF = false;
		if (obj != null) {
			// String objStr=obj.toString();
			if (OutJcicTxtDate != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "[Jcic報送日期]存在不可刪除");
			} else {
				TF = true;
			}
		} else {
			// E0008 刪除資料時，發生錯誤
			throw new LogicException(titaVo, "E0008", "資料不存在");
		}
		return TF;
	}

	public String DateAdjust(String YYYYmmdd, int AdjYYYY, int AdjMM, int AdjDD) throws LogicException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dt = null;
		try {
			dt = sdf.parse(YYYYmmdd);
		} catch (ParseException e) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "JcicCom DateAdjust 轉換資料發生問題 (YYYYmmdd=[" + YYYYmmdd + "])");
		}
		if (dt != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.YEAR, AdjYYYY);// 日期減1年
			rightNow.add(Calendar.MONTH, AdjMM);// 日期加3個月
			rightNow.add(Calendar.DAY_OF_YEAR, AdjDD);// 日期加10天
			Date dt1 = rightNow.getTime();
			String newYYYYmmdd = sdf.format(dt1);
			return newYYYYmmdd;
		} else {
			return "";
		}
	}

	public String DcToRoc(String Dc, int type) throws LogicException {
		String Roc = "0";
		if (Dc != null) {
			int DcL = String.valueOf(Dc).length();
			this.info("JcicCom DcToRoc Dc=[" + Dc + "] type=[" + type + "] DcL=[" + DcL + "] Roc=[" + Roc + "]");
			if (DcL != 0) {
				Roc = Dc;
				switch (type) {
				case 0:
					// YYYYMMDD
					if (DcL == 8) {
						Roc = String.valueOf(Integer.parseInt(Dc) - 19110000);
					}
					break;
				case 1:
					// YYYYMM
					if (DcL == 6) {
						Roc = String.valueOf(Integer.parseInt(Dc) - 191100);
					}
					break;
				case 2:
					if (DcL == 4) {
						Roc = String.valueOf(Integer.parseInt(Dc) - 1911);
					}
					break;
				default:
					Roc = Dc;
				}
			} else {
				return Roc;
			}
		}
		return Roc;
	}

	public String RocTurnDc(String Roc, int type) throws LogicException {
		String Dc = "0";
		if (Roc != null) {
			int RocL = Roc.length();
			if (RocL != 0) {
				if (Integer.parseInt(Roc) != 0) {
					Dc = Roc;
					switch (type) {
					case 0:
						// YYYYMMDD
						if (RocL == 7 || RocL == 6) {
							Dc = String.valueOf(Integer.parseInt(Roc) + 19110000);
						}
						break;
					case 1:
						// YYYYMM
						if (RocL == 5 || RocL == 4) {
							Dc = String.valueOf(Integer.parseInt(Roc) + 191100);
						}
						break;
					case 2:
						if (RocL == 3 || RocL == 2) {
							Dc = String.valueOf(Integer.parseInt(Roc) + 1911);
						}
						break;
					default:
						Dc = Roc;
					}
				} else {
					return Dc;
				}
			}
		}
		return Dc;
	}

	public String JcicCourtCodeAndZipCode(String JcicCourtZipCode, TitaVo titaVo) throws LogicException {
		String ApplyType = "1";// 1:法院調解 2:鄉鎮市區調解委員會
		if (JcicCourtZipCode != null && JcicCourtZipCode.length() != 0) {
			int JcicCourtZipCodeL = JcicCourtZipCode.length();
			String num = "0123456789";
			boolean isNum = true;
			for (int i = 0; i < JcicCourtZipCodeL; i++) {
				if (num.indexOf(JcicCourtZipCode.charAt(i)) == -1) {
					isNum = false;
					break;
				}
			}
			if (isNum) {
				// 由數字構成
				ApplyType = "2";
			} else {
				ApplyType = "1";
			}
			this.info("L8R03 ApplyType=[" + ApplyType + "]");
		}
		String JcicCourtZipName = Jcic440CourtCodeAndZipCode(ApplyType, JcicCourtZipCode, titaVo);// 80碼長度
		return JcicCourtZipName;
	}

	public String Jcic440CourtCodeAndZipCode(String ApplyType, String JcicCourtZipCode, TitaVo titaVo) throws LogicException {
		String CourtCodeAndZipCodeName = "";
		switch (ApplyType) {
		case "1":
			// 1:法院調解
			CourtCodeAndZipCodeName = checkJcicCourtCode(JcicCourtZipCode, titaVo);
			break;
		case "2":
			// 2:鄉鎮市區調解委員會
			CourtCodeAndZipCodeName = ZipName(JcicCourtZipCode, titaVo);
			break;
		default:
			this.info("L8R02 RimApplyType not found");
		}
		return CourtCodeAndZipCodeName;
	}

	public String ZipName(String ZipCode, TitaVo titaVo) throws LogicException {
		String ZipName = "";
		CdArea tCdArea = sCdAreaService.Zip3First(ZipCode, titaVo);
		if (tCdArea != null) {
			String cityCode = tCdArea.getCityCode();
			String areaItem = tCdArea.getAreaItem();
			String cityItem = "";
			if (cityCode != null && cityCode.length() != 0) {
				CdCity tCdCity = sCdCityService.findById(cityCode, titaVo);
				if (tCdCity != null) {
					cityItem = tCdCity.getCityItem();
				}
			}
			ZipName = cityItem + areaItem;
		}
		return ZipName;
	}

	public Map<String, String> JcicBankCodeMapForRim(List<String> BankCode, TitaVo titaVo) throws LogicException {
		Map<String, String> lBankCode = new HashMap<String, String>();
		Slice<CdCode> slCdCode = sCdCodeService.getCodeList2(8, "JcicBankCode", BankCode, 0, Integer.MAX_VALUE, titaVo);
		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();
		if (lCdCode != null && lCdCode.size() != 0) {
			for (CdCode tCdCode : lCdCode) {
				lBankCode.put(tCdCode.getCode(), tCdCode.getItem());
			}
		} else {
			// 查無資料
		}
		return lBankCode;
	}

	// FinCode JCIC法院代碼 ,ThrowError 1踢出錯誤訊息0不踢出
	public String checkJcicCourtCode(String CourtCode, TitaVo titaVo) throws LogicException {
		String CourtName = "";
		if (CourtCode != null && CourtCode.length() != 0) {
			CdCode CdCode = sCdCodeService.getItemFirst(8, "CourtCode", CourtCode, titaVo);
			if (CdCode != null) {
				CourtName = CdCode.getItem();
			} else {

			}
		}

		return CourtName;
	}

	// FinCode JCIC報送銀行代碼 ,ThrowError 1踢出錯誤訊息0不踢出
	public CdCode checkJcicBankCode(String FinCode, int ThrowError, TitaVo titaVo) throws LogicException {
		this.info("checkNegFinAcct");
		if (FinCode != null && FinCode.length() != 0) {
			// 檢核是否為消債條例金融機構代號
			this.info("L8R01 FinCode=[" + FinCode + "]");
			CdCode tCdCode = sCdCodeService.getItemFirst(8, "JcicBankCode", FinCode, titaVo);
			if (tCdCode != null) {
				return tCdCode;
			} else {
				if (ThrowError == 1) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "未找到銀行代碼[" + FinCode + "]請至L6064新增代碼 [業務類別:08,代碼檔代號:JcicBankCode]");
				}
			}
		}
		return null;
	}

	public String FinCodeName(String FinCode, int ThrowError, TitaVo titaVo) throws LogicException {
		String strFinCodeName = "";
		CdCode tCdCode = checkJcicBankCode(FinCode, ThrowError, titaVo);
		if (tCdCode != null) {
			strFinCodeName = tCdCode.getItem();
		}
		return strFinCodeName;
	}

	public String changeTranKey(String TranKey) {
		if (TranKey.equals("A")) {
			TranKey = "C";
		} else {

		}
		return TranKey;
	}

//	public void checkValueJcicZ042(JcicZ047 JcicZ047Vo,TitaVo titaVo)throws LogicException {
//		//檢核第9~14欄(依民法第323條計算之信用貸款債務總金額、信用貸款債務簽約總金額、依民法323條計算之現金卡債務總金額、現金卡簽約總金額、依民法323條計算之信用卡債務總金額、信用卡債務簽約總金額)若與所有原債權金融機構回報‘42’:回報無擔保債權金額資料債權金額不同時，則予以剔退。
//		JcicZ042Id JcicZ042IdVo=new JcicZ042Id();
//		JcicZ042IdVo.setCustId(JcicZ047Vo.getJcicZ047Id().getCustId());
//		JcicZ042IdVo.setRcDate(JcicZ047Vo.getJcicZ047Id().getRcDate());
//		JcicZ042IdVo.setSubmitKey(JcicZ047Vo.getJcicZ047Id().getSubmitKey());
//		JcicZ042 JcicZ042Vo=sJcicZ042Service.findById(JcicZ042IdVo, titaVo);
//		if(JcicZ042Vo!=null) {
//			JcicZ042Vo.getCivil323CashAmt();//依民法第323條計算之現金卡放款本息餘額
//			JcicZ042Vo.getCivil323CreditAmt();//依民法第323條計算之信用卡本息餘額
//			JcicZ042Vo.getCivil323ExpAmt();//依民法第323條計算之信用貸款本息餘額
//			
//			JcicZ047Vo.getExpLoanAmt();//信用貸款債務簽約總金額
//			JcicZ047Vo.getCivil323ExpAmt();//依民法第323條計算之信用貸款債務總金額
//			JcicZ047Vo.getCashCardAmt();//現金卡債務簽約總金額
//			JcicZ047Vo.getCivil323CashAmt();//依民法第323條計算之現金卡債務總金額
//			JcicZ047Vo.getCreditCardAmt();//信用卡債務簽約總金額
//			JcicZ047Vo.getCivil323CreditAmt();//依民法第323條計算之信用卡債務總金額
//			Vector vErrorMsg=new Vector();
//			
//		}else {
//			//E5009	資料檢核錯誤
//			throw new LogicException(titaVo, "E5009","未報送過JcicZ042!");
//		}
//	}

	public void checkJcicZ051(JcicZ051 jcicZ051Vo, TitaVo titaVo) throws LogicException {
		// 若延期繳款原因為D者，延期繳款年月不能連續兩期。
		// 延期繳款累計期數(月)不得超過6期，超過者以剔退處理(100.10)。
		String CustId = jcicZ051Vo.getJcicZ051Id().getCustId();
		int RcDate = Integer.parseInt(RocTurnDc(String.valueOf(jcicZ051Vo.getJcicZ051Id().getRcDate()), 0));
		String SubmitKey = jcicZ051Vo.getJcicZ051Id().getSubmitKey();
		Slice<JcicZ051> slJcicZ051 = sJcicZ051Service.SubCustRcEq(CustId, RcDate, SubmitKey, 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ051> lJcicZ051 = slJcicZ051 == null ? null : slJcicZ051.getContent();
		if (lJcicZ051 != null) {
			// int lJcicZ051S=lJcicZ051.size();
			int bandValue = 6;
			int countZ051 = 0;
			int testUpdOrIns = 0;// 0 Insert,1:Update
			for (JcicZ051 JcicZ051VO : lJcicZ051) {
				if (JcicZ051VO != null) {
					if (JcicZ051VO.getJcicZ051Id().getCustId().equals(jcicZ051Vo.getJcicZ051Id().getCustId()) && JcicZ051VO.getJcicZ051Id().getRcDate() == jcicZ051Vo.getJcicZ051Id().getRcDate()
							&& JcicZ051VO.getJcicZ051Id().getDelayYM() == jcicZ051Vo.getJcicZ051Id().getDelayYM()
							&& JcicZ051VO.getJcicZ051Id().getSubmitKey().equals(jcicZ051Vo.getJcicZ051Id().getSubmitKey())) {
						// 修改
						testUpdOrIns = 1;
						continue;
					}
				}
				if (("D").equals(JcicZ051VO.getTranKey())) {
					continue;
				}
				countZ051++;
			}
			if (testUpdOrIns == 1) {
				// 已存在資料
				// 此刻為修改
				if (countZ051 > bandValue) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "延期繳款累計期數(月)不得超過6期，超過者以剔退處理。");
				}
			} else {
				// 此刻為新增
				if (countZ051 >= bandValue) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "延期繳款累計期數(月)不得超過6期，超過者以剔退處理。");
				}
			}

			if (("D").equals(jcicZ051Vo.getDelayCode())) {
				// 因為根據延期繳款年月的上一筆資料
				for (JcicZ051 JcicZ051VO : lJcicZ051) {
					if (JcicZ051VO != null) {
						if (JcicZ051VO.getJcicZ051Id().getCustId().equals(jcicZ051Vo.getJcicZ051Id().getCustId()) && JcicZ051VO.getJcicZ051Id().getRcDate() == jcicZ051Vo.getJcicZ051Id().getRcDate()
								&& JcicZ051VO.getJcicZ051Id().getDelayYM() == jcicZ051Vo.getJcicZ051Id().getDelayYM()
								&& JcicZ051VO.getJcicZ051Id().getSubmitKey().equals(jcicZ051Vo.getJcicZ051Id().getSubmitKey())) {
							continue;
						}
						if (("D").equals(JcicZ051VO.getTranKey())) {
							// 第一筆要不等於刪除,刪除就繼續往下找,然後KEY值也不可相同

							continue;
						}
						if (JcicZ051VO.getDelayCode() != null) {
							if (!JcicZ051VO.getDelayCode().equals(jcicZ051Vo.getDelayCode())) {
								break;
							} else {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "若延期繳款原因為[D:繳稅]者，延期繳款年月不能連續兩期。");
							}
						}

					}
				}

			}
		}

	}

	public int countJcicZ043(String CustId, int RcDate, String SubmitKey, String MaxMainCode, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		Slice<JcicZ043> sJcicZ043 = sJcicZ043Service.coutCollaterals(CustId, RcDate, SubmitKey, MaxMainCode, index, limit, titaVo);
		int count = 0;
		if (sJcicZ043 != null) {
			count = sJcicZ043.getSize();
		}
		return count;
	}

	public JcicZ044 checkJcicZ044(String CustId, int RcDate, String SubmitKey, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		JcicZ044Id JcicZ044IdVo = new JcicZ044Id();
		JcicZ044IdVo.setCustId(CustId);
		JcicZ044IdVo.setRcDate(RcDate);
		JcicZ044IdVo.setSubmitKey(SubmitKey);
		JcicZ044 JcicZ044Vo = sJcicZ044Service.findById(JcicZ044IdVo, titaVo);
		if (JcicZ044Vo != null) {
			return JcicZ044Vo;
		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "未報送過JcicZ044!");
		}
	}

	public void checkJcicZ044RateAnedPeriod(JcicZ044 JcicZ044Vo, JcicZ047 JcicZ047Vo) throws LogicException {
		if (JcicZ044Vo != null) {
			String ErrorMsg = "";
			if (JcicZ047Vo.getPeriod() != JcicZ044Vo.getPeriod()) {
				if (ErrorMsg.length() != 0) {
					ErrorMsg += ";";
				}
				ErrorMsg += "期數不相同,本次期數(" + JcicZ047Vo.getPeriod() + ")與JcicZ044期數(" + JcicZ044Vo.getPeriod() + ")";
			}
			if (JcicZ047Vo.getRate().compareTo(JcicZ044Vo.getRate()) != 0) {
				if (ErrorMsg.length() != 0) {
					ErrorMsg += ";";
				}
				ErrorMsg += "利率不相同,本次利率(" + JcicZ047Vo.getRate() + ")與JcicZ044利率(" + JcicZ044Vo.getRate() + ")";
			}
			if (ErrorMsg.length() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", ErrorMsg);
			}
		}
	}

	public void checkJcicZ045(String CustId, int RcDate, String SubmitKey, String MaxMainCode, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		JcicZ045Id JcicZ045IdVo = new JcicZ045Id();
		JcicZ045IdVo.setCustId(CustId);
		JcicZ045IdVo.setMaxMainCode(MaxMainCode);
		JcicZ045IdVo.setRcDate(RcDate);
		JcicZ045IdVo.setSubmitKey(SubmitKey);
		JcicZ045 JcicZ045Vo = sJcicZ045Service.findById(JcicZ045IdVo, titaVo);
		if (JcicZ045Vo != null) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "未報送過JcicZ045!");
		}
	}

	public void checkJcicZ440(String CustId, int ApplyDate, String SubmitKey, String BankId, TitaVo titaVo) throws LogicException {
		ApplyDate = Integer.parseInt(RocTurnDc(String.valueOf(ApplyDate), 0));
		// 需先報送'440'方可報送'444'
		JcicZ440Id tJcicZ440Id = new JcicZ440Id();
		tJcicZ440Id.setCustId(CustId);
		tJcicZ440Id.setSubmitKey(SubmitKey);
		tJcicZ440Id.setApplyDate(ApplyDate);
//		tJcicZ440Id.setBankId(BankId);
		JcicZ440 tJcicZ440VO = sJcicZ440Service.findById(tJcicZ440Id, titaVo);
		if (tJcicZ440VO != null) {

		} else {
			throw new LogicException(titaVo, "E8107", "");
		}
	}

	public void checkJcicZ447(String CustId, int ApplyDate, String SubmitKey, String BankId, TitaVo titaVo) throws LogicException {
		ApplyDate = Integer.parseInt(RocTurnDc(String.valueOf(ApplyDate), 0));
		// 需先報送'447'方可報送'450'
		JcicZ447Id tJcicZ447Id = new JcicZ447Id();
		tJcicZ447Id.setCustId(CustId);
		tJcicZ447Id.setSubmitKey(SubmitKey);
		tJcicZ447Id.setApplyDate(ApplyDate);
//		tJcicZ447Id.setBankId(BankId);
		JcicZ447 tJcicZ447VO = sJcicZ447Service.findById(tJcicZ447Id, titaVo);
		if (tJcicZ447VO != null) {

		} else {
			throw new LogicException(titaVo, "E8102", "");
		}
	}

	public void checkJcicZ448(String CustId, int ApplyDate, String SubmitKey, String BankId, String MaxMainCode, TitaVo titaVo) throws LogicException {
		ApplyDate = Integer.parseInt(RocTurnDc(String.valueOf(ApplyDate), 0));
		// 需先報送'448'方可報送'454'
		JcicZ448Id tJcicZ448Id = new JcicZ448Id();
		tJcicZ448Id.setCustId(CustId);
		tJcicZ448Id.setSubmitKey(SubmitKey);
		tJcicZ448Id.setApplyDate(ApplyDate);
//		tJcicZ448Id.setBankId(BankId);
		tJcicZ448Id.setMaxMainCode(MaxMainCode);
		JcicZ448 tJcicZ448VO = sJcicZ448Service.findById(tJcicZ448Id, titaVo);
		if (tJcicZ448VO != null) {

		} else {
			throw new LogicException(titaVo, "E8101", "");
		}
	}

	public void checkHadJcicZ446(String CustId, int ApplyDate, String SubmitKey, String BankId, TitaVo titaVo) throws LogicException {
		ApplyDate = Integer.parseInt(RocTurnDc(String.valueOf(ApplyDate), 0));
		JcicZ446Id tJcicZ446Id = new JcicZ446Id();
		tJcicZ446Id.setCustId(CustId);
		tJcicZ446Id.setSubmitKey(SubmitKey);
		tJcicZ446Id.setApplyDate(ApplyDate);
//		tJcicZ446Id.setBankId(BankId);
		JcicZ446 tJcicZ446VO = sJcicZ446Service.findById(tJcicZ446Id, titaVo);
		if (tJcicZ446VO != null) {
			throw new LogicException(titaVo, "E8117", "");
		} else {

		}
	}

	public void checkHadJcicZ046(String CustId, int RcDate, String SubmitKey, TitaVo titaVo) throws LogicException {
		// 相同KEY值報送'446'結案後，不得新增、異動、刪除或補件本檔案。
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		Slice<JcicZ046> slJcicZ046 = null;
		slJcicZ046 = sJcicZ046Service.hadZ046(CustId, RcDate, SubmitKey, 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ046> lJcicZ046 = slJcicZ046 == null ? null : slJcicZ046.getContent();
		if (lJcicZ046 != null) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "已報送過JcicZ046!不可異動或新增資料!");
		} else {

		}
	}

	public void checkJcicZ047(String CustId, int RcDate, String SubmitKey, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		JcicZ047Id JcicZ047IdVo = new JcicZ047Id();
		JcicZ047IdVo.setCustId(CustId);
		JcicZ047IdVo.setRcDate(RcDate);
		JcicZ047IdVo.setSubmitKey(SubmitKey);
		JcicZ047 JcicZ047Vo = sJcicZ047Service.findById(JcicZ047IdVo, titaVo);
		if (JcicZ047Vo != null) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "未報送過JcicZ047!");
		}
	}

	public void l8307checkJcicZ047(String CustId, int RcDate, String SubmitKey, String CloseCode, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		JcicZ047Id JcicZ047IdVo = new JcicZ047Id();
		JcicZ047IdVo.setCustId(CustId);
		JcicZ047IdVo.setRcDate(RcDate);
		JcicZ047IdVo.setSubmitKey(SubmitKey);
		JcicZ047 JcicZ047Vo = sJcicZ047Service.findById(JcicZ047IdVo, titaVo);
		if (JcicZ047Vo != null) {
			// 報送過簽約完成日
			if (JcicZ047Vo.getSignDate() != 0) {
				List<String> lCloseCode = new ArrayList<String>();
				lCloseCode.add("00");// 00：毀諾
				lCloseCode.add("01");// 01：協商終止
				lCloseCode.add("99");// 99：依債務清償方案履行完畢
				if (!lCloseCode.contains(CloseCode)) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "已報送過JcicZ047,且簽約完成日已填寫!結案代碼必須為[00：毀諾,01：協商終止,99：依債務清償方案履行完畢]");
				}
			}
		} else {
//			//E5009	資料檢核錯誤
//			throw new LogicException(titaVo, "E5009","未報送過JcicZ047!");
		}
	}

	public void checkJcicZ048(String CustId, int RcDate, String SubmitKey, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		JcicZ048Id JcicZ048IdVo = new JcicZ048Id();
		JcicZ048IdVo.setCustId(CustId);
		JcicZ048IdVo.setRcDate(RcDate);
		JcicZ048IdVo.setSubmitKey(SubmitKey);
		JcicZ048 JcicZ048Vo = sJcicZ048Service.findById(JcicZ048IdVo, titaVo);
		if (JcicZ048Vo != null) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "未報送過JcicZ048!");
		}
	}

	public void checkJcicZ040(String CustId, int RcDate, String SubmitKey, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		// KEY值為IDN+報送單位代號+協商申請日，若KEY值未曾報送過"40"：前置協商受理申請暨請求債權通知，予以剔退處理。
		JcicZ040Id JcicZ040IdVo = new JcicZ040Id();
		JcicZ040IdVo.setCustId(CustId);
		JcicZ040IdVo.setRcDate(RcDate);
		JcicZ040IdVo.setSubmitKey(SubmitKey);
		JcicZ040 JcicZ040Vo = sJcicZ040Service.findById(JcicZ040IdVo, titaVo);
		if (JcicZ040Vo != null) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "未報送過JcicZ040!");
		}
	}

	public void CheckSumJcicZ050PayAmt(JcicZ050 ThisJcicZ050, TitaVo titaVo) throws LogicException {

		if (ThisJcicZ050 != null) {
			String JcicZ050CustId = ThisJcicZ050.getJcicZ050Id().getCustId();
			int JcicZ050RcDate = Integer.parseInt(RocTurnDc(String.valueOf(ThisJcicZ050.getJcicZ050Id().getRcDate()), 0));
			String JcicZ050SubmitKey = ThisJcicZ050.getJcicZ050Id().getSubmitKey();

			// 若第11欄[債權結案註記]填報‘Y’:債務全數清償者，必須隨同報送‘46’:結案通知資料，且第7欄結案原因代號必須填報‘99’:依債務清償方案履行完畢，否則予以剔退。
			if (("Y").equals(ThisJcicZ050.getStatus())) {
				Slice<JcicZ046> slJcicZ046 = null;
				slJcicZ046 = sJcicZ046Service.custRcEq(JcicZ050CustId, JcicZ050RcDate, this.index, this.limit, titaVo);
				List<JcicZ046> lJcicZ046 = slJcicZ046 == null ? null : slJcicZ046.getContent();
				JcicZ046 JcicZ046Vo = null;
				if (lJcicZ046 != null) {
					for (JcicZ046 ThisJcicZ046Vo : lJcicZ046) {
						if (JcicZ050SubmitKey.equals(ThisJcicZ046Vo.getSubmitKey())) {
							JcicZ046Vo = ThisJcicZ046Vo;
							break;
						}
					}
				} else {

				}
				String ErrorMsg = "JcicZ050[債權結案註記]填報‘Y’:債務全數清償者";
				if (JcicZ046Vo != null) {
					if (!("99").equals(JcicZ046Vo.getCloseCode())) {
						// E5009 資料檢核錯誤
						throw new LogicException(titaVo, "E5009", ErrorMsg + ",JcicZ046[結案原因代號]不為'99' ");
					}
				} else {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", ErrorMsg + ",查無JcicZ046結案通知資料");
				}
			}

			// 若第9欄[累計實際還款金額]不等於該IDN所有已報送本檔案資料之第8欄[繳款金額]之合計(含本次繳金額)，則以剔退處理。
			Slice<JcicZ050> slJcicZ050 = sJcicZ050Service.custRcEq(JcicZ050CustId, JcicZ050RcDate, 0, Integer.MAX_VALUE, titaVo);
			List<JcicZ050> lJcicZ050 = slJcicZ050 == null ? null : slJcicZ050.getContent();
			BigDecimal SumPayAmt = BigDecimal.ZERO;
			if (lJcicZ050 != null) {
				for (JcicZ050 JcicZ050VO : lJcicZ050) {
					if (!JcicZ050VO.getJcicZ050Id().equals(ThisJcicZ050.getJcicZ050Id())) {
						BigDecimal PayAmt = new BigDecimal(JcicZ050VO.getPayAmt());
						SumPayAmt = SumPayAmt.add(PayAmt);
					} else {
						continue;
					}
				}
			} else {
				// 同一協商案件首次報送本檔案時，若尚未報送‘47’:金融機構無擔保債務協議資料第19欄‘簽約完成日期’，則予以剔退。
				JcicZ047Id JcicZ047IdVo = new JcicZ047Id();
				JcicZ047IdVo.setCustId(JcicZ050CustId);
				JcicZ047IdVo.setRcDate(JcicZ050RcDate);
				JcicZ047IdVo.setSubmitKey(JcicZ050SubmitKey);
				JcicZ047 JcicZ047Vo = sJcicZ047Service.findById(JcicZ047IdVo, titaVo);

				String ErrorMessage = "本筆資料為首次報送JcicZ050";
				if (JcicZ047Vo != null) {
					Integer SignDate = JcicZ047Vo.getSignDate();
					if (SignDate != null && SignDate != 0) {

					} else {
						// E5009 資料檢核錯誤
						throw new LogicException(titaVo, "E5009", ErrorMessage + ",JcicZ047[簽約完成日]未填寫");
					}
				} else {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", ErrorMessage + ",查無JcicZ047的資料");
				}
			}

			SumPayAmt = SumPayAmt.add(new BigDecimal(ThisJcicZ050.getPayAmt()));
			if (SumPayAmt.compareTo(new BigDecimal(ThisJcicZ050.getSumRepayActualAmt())) == 0) {

			} else {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "[累計實際還款金額(" + SumPayAmt + ")]不等於JcicZ050所有以報送本檔案[繳款金額(" + ThisJcicZ050.getSumRepayActualAmt() + ")]之合計(含本次繳金額)");
			}

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "JcicZ050不存在");
		}
	}

	public void checkJcicZ052(String CustId, int RcDate, String SubmitKey, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		JcicZ052Id JcicZ052IdVo = new JcicZ052Id();
		JcicZ052IdVo.setCustId(CustId);
		JcicZ052IdVo.setRcDate(RcDate);
		JcicZ052IdVo.setSubmitKey(SubmitKey);
		JcicZ052 JcicZ052Vo = sJcicZ052Service.findById(JcicZ052IdVo, titaVo);
		if (JcicZ052Vo != null) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "JcicZ052資料不存在");
		}
		return;
	}

	public void checkJcicZ063(String custId, int RcDate, String submitKey, int changePayDate, TitaVo titaVo) throws LogicException {
		RcDate = Integer.parseInt(RocTurnDc(String.valueOf(RcDate), 0));
		changePayDate = Integer.parseInt(RocTurnDc(String.valueOf(changePayDate), 0));
		JcicZ063Id JcicZ063IdVo = new JcicZ063Id();
		JcicZ063IdVo.setCustId(custId);
		JcicZ063IdVo.setRcDate(RcDate);
		JcicZ063IdVo.setSubmitKey(submitKey);
		JcicZ063IdVo.setChangePayDate(changePayDate);
		JcicZ063 JcicZ063Vo = sJcicZ063Service.findById(JcicZ063IdVo, titaVo);
		if (JcicZ063Vo != null) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "JcicZ063資料不存在");
		}
	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}
}
