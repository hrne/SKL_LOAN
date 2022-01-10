package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L5060ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Component("L5060")
@Scope("prototype")

/**
 * 新增應處理明細－放款轉列催收
 * 
 * @author Lai
 * @version 1.0.0
 */

public class L5060 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public L5060ServiceImpl l5060ServiceImpl;

	@Autowired
	public CdEmpService iCdEmpService;

	@Autowired
	public CustMainService sCustMainService;


	@Override
	/* 應處理清單 放款轉列催收 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("L5060 ......");
		this.totaVo.init(titaVo);
		String iOprionCd = titaVo.getParam("OprionCd");
		int iIdentity = Integer.valueOf(titaVo.getParam("Identity"));
		int iCustNo = 0;
		String iCustName = "";
		String iCustId = "";
		String iAccCollPsn = "";
		String iLegalPsn = "";
		String iTxCode = titaVo.getParam("TxCode");
		String iCityCode = titaVo.getParam("CityCode");
		this.info("身分別=" + iIdentity + ".");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;
		List<Map<String, String>> lL5060Vo = new ArrayList<Map<String, String>>();

		switch (iIdentity) {
		case 0:
			this.info("查全部");
			break;
		case 1:
			this.info("查戶號");
			iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
			break;
		case 2:
			this.info("查戶名");
			iCustName = titaVo.getParam("CustName");
			break;
		case 3:
			this.info("查身分證字號");
			iCustId = titaVo.getParam("CustId");
			break;
		case 4:
			this.info("查催收");
			iAccCollPsn = titaVo.getParam("AccCollPsn");
			break;
		case 5:
			this.info("查法務");
			iLegalPsn = titaVo.getParam("LegalPsn");
			break;
		}
		try {
//			l5060ServiceImpl.getEntityManager(titaVo);
			if (iOprionCd.equals("1")) {
				lL5060Vo = l5060ServiceImpl.load(this.index, this.limit, titaVo.getParam("CaseCode"), Integer.valueOf(titaVo.getParam("Ovdtrmfm")), Integer.valueOf(titaVo.getParam("Ovdtrmto")),
						titaVo.getParam("Ovdamtfm"), titaVo.getParam("Ovdamtto"), Integer.valueOf(titaVo.getParam("Status")), this.getTxBuffer().getTxBizDate().getTbsDyf(), iIdentity, iCustNo,
						iCustName, iCustId, iAccCollPsn, iLegalPsn, iTxCode, iCityCode , titaVo);
			} else {
				lL5060Vo = l5060ServiceImpl.load(this.index, this.limit, titaVo.getParam("CaseCode"), Integer.valueOf(titaVo.getParam("Ovddayfm")), Integer.valueOf(titaVo.getParam("Ovddayto")),
						titaVo.getParam("Ovdamtfm"), titaVo.getParam("Ovdamtto"), Integer.valueOf(titaVo.getParam("Status")), this.getTxBuffer().getTxBizDate().getTbsDyf(), iIdentity, iCustNo,
						iCustName, iCustId, iAccCollPsn, iLegalPsn, iTxCode, iCityCode , titaVo);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.warn(errors.toString());
			throw new LogicException(titaVo, "E0006", "loadCollListServiceImpl Exception= " + e); // 讀資料時，發生錯誤
		}

		if (lL5060Vo != null && lL5060Vo.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.info("L5060List=" + lL5060Vo.toString());
		if (lL5060Vo.size() == 0 || lL5060Vo == null) {
			throw new LogicException(titaVo, "E0001", "法催作業清單檔查無資料");
		}
		ArrayList<String> clCustList = new ArrayList<String>();
//		ArrayList<Integer> clFacmNoList = new ArrayList<Integer>();

//		for (Map<String, String> thisL5060Vo:lL5060Vo) {
//			boolean iSwitch = false;
//			String iTxCode = titaVo.getParam("TxCode");
//			if (iTxCode.equals("9")) {
//				iSwitch = true;
//			}else {
//				if(iTxCode.equals(thisL5060Vo.getTxCode())) {
//					iSwitch = true;
//				}
//			}
//			if (iSwitch) {
//				OccursList occursList = new OccursList();
//				if(clCustList.contains(StringUtils.leftPad(String.valueOf(thisL5060Vo.getClCustNo()), 3)+StringUtils.leftPad(String.valueOf(thisL5060Vo.getClFacmNo()), 3))) {
//					//有同擔保品情況
//					occursList.putParam("OOClFlag", 0);
//					occursList.putParam("OOClFlagA", 1);
//				}else {
//					//無同擔保品情況或第一次
//					clCustList.add(StringUtils.leftPad(String.valueOf(thisL5060Vo.getClCustNo()), 3)+StringUtils.leftPad(String.valueOf(thisL5060Vo.getClFacmNo()), 3));
//					occursList.putParam("OOClFlag", 1);
//					occursList.putParam("OOClFlagA", 0);
//				}
//				int previntdate = thisL5060Vo.getPrevIntDate();
//				int txdate = thisL5060Vo.getTxDate();
//				if(txdate==0) {
//					occursList.putParam("OOTxDate", txdate);
//				}else {
//					occursList.putParam("OOTxDate", txdate-19110000);
//				}
//				occursList.putParam("OOTxCode", thisL5060Vo.getTxCode()); //上次作業項目
//				occursList.putParam("OOCustNo", thisL5060Vo.getCustNo());
//				occursList.putParam("OOFacmNo", thisL5060Vo.getFacmNo());
//				if (previntdate==0) {
//					occursList.putParam("OOPrevIntDate", previntdate);
//				}else {
//					occursList.putParam("OOPrevIntDate", previntdate-19110000);
//				}
//				occursList.putParam("OOOverDueterm", thisL5060Vo.getOvduTerm());
//				occursList.putParam("OOOvduDays", thisL5060Vo.getOvduDays());
//				occursList.putParam("OOCurrencyCode", thisL5060Vo.getCurrencyCode());
//				occursList.putParam("OOPrinBalance", thisL5060Vo.getPrinBalance());
//				occursList.putParam("OOAccCollPsn", thisL5060Vo.getAccCollPsn());
//				occursList.putParam("OOLegalPsn", thisL5060Vo.getLegalPsn());
//				occursList.putParam("OOAccCollPsnX", thisL5060Vo.getAccCollPsnName());//催收人員姓名
//				occursList.putParam("OOLegalPsnX", thisL5060Vo.getLegalPsnName());//法務人員姓名
//				occursList.putParam("OOIsSpecify", thisL5060Vo.getIsSpecify()); //是否指定
//				occursList.putParam("OOStatus", thisL5060Vo.getStatus()); //戶況
//				if(thisL5060Vo.getRemindDate()==0) {
//					occursList.putParam("OOAlertDate", 0);
//				}else {
//					occursList.putParam("OOAlertDate", thisL5060Vo.getRemindDate()-19110000);
//				}
//				if(Integer.valueOf(thisL5060Vo.getRemindDate())<this.getTxBuffer().getTxBizDate().getTbsDyf()) {
//					occursList.putParam("OOClFlagB", 1);//提醒日按鈕
//				}else {
//					occursList.putParam("OOClFlagB", 1);//測試用設為1:開
//				}
//				occursList.putParam("OOClCustNo", thisL5060Vo.getClCustNo());		
//				occursList.putParam("OOClFacmNo", thisL5060Vo.getClFacmNo());
//			
		for (Map<String, String> thisL5060Vo : lL5060Vo) {
			OccursList occursList = new OccursList();
			if (clCustList.contains(StringUtils.leftPad(thisL5060Vo.get("F15"), 3) + StringUtils.leftPad(thisL5060Vo.get("F16"), 3))) {
				// 有同擔保品情況
				occursList.putParam("OOClFlag", 0);
				occursList.putParam("OOClFlagA", 1);
			} else {
				// 無同擔保品情況或第一次
				clCustList.add(StringUtils.leftPad(thisL5060Vo.get("F15"), 3) + StringUtils.leftPad(thisL5060Vo.get("F16"), 3));
				occursList.putParam("OOClFlag", 1);
				occursList.putParam("OOClFlagA", 0);
			}
			int previntdate = Integer.valueOf(thisL5060Vo.get("F5"));
			int txdate = Integer.valueOf(thisL5060Vo.get("F2"));
			if (txdate == 0) {
				occursList.putParam("OOTxDate", txdate);
			} else {
				occursList.putParam("OOTxDate", txdate - 19110000);
			}
			occursList.putParam("OOTxCode", thisL5060Vo.get("F3")); // 上次作業項目
			occursList.putParam("OOCustNo", thisL5060Vo.get("F0"));
			CustMain tCustMain = new CustMain();
			String CustName = "";
			tCustMain = sCustMainService.custNoFirst(Integer.valueOf(thisL5060Vo.get("F0")),Integer.valueOf(thisL5060Vo.get("F0")), titaVo);
			if (tCustMain != null) {
				CustName = StringCut.replaceLineUp(tCustMain.getCustName());
			}
			occursList.putParam("OOCustName", CustName);
			occursList.putParam("OOFacmNo", thisL5060Vo.get("F1"));
			if (previntdate == 0) {
				occursList.putParam("OOPrevIntDate", previntdate);
			} else {
				occursList.putParam("OOPrevIntDate", previntdate - 19110000);
			}
			occursList.putParam("OOOverDueterm", thisL5060Vo.get("F6"));
			occursList.putParam("OOOvduDays", thisL5060Vo.get("F7"));
			occursList.putParam("OOCurrencyCode", thisL5060Vo.get("F8"));
			occursList.putParam("OOPrinBalance", thisL5060Vo.get("F9"));
			occursList.putParam("OOAccCollPsn", thisL5060Vo.get("F10"));
			occursList.putParam("OOLegalPsn", thisL5060Vo.get("F11"));
			occursList.putParam("OOAccCollPsnX", thisL5060Vo.get("F12"));// 催收人員姓名
			occursList.putParam("OOLegalPsnX", thisL5060Vo.get("F13"));// 法務人員姓名
			occursList.putParam("OOIsSpecify", thisL5060Vo.get("F18")); // 是否指定
			occursList.putParam("OOStatus", thisL5060Vo.get("F14")); // 戶況
			if (Integer.valueOf(thisL5060Vo.get("F4")) == 0) {
				occursList.putParam("OOAlertDate", 0);
			} else {
				occursList.putParam("OOAlertDate", Integer.valueOf(thisL5060Vo.get("F4")) - 19110000);
			}
			if (Integer.valueOf(thisL5060Vo.get("F4")) < this.getTxBuffer().getTxBizDate().getTbsDyf()) {
				occursList.putParam("OOClFlagB", 1);// 提醒日按鈕
			} else {
				occursList.putParam("OOClFlagB", 1);// 測試用設為1:開
			}
			occursList.putParam("OOClCustNo", thisL5060Vo.get("F15"));
			occursList.putParam("OOClFacmNo", thisL5060Vo.get("F16"));
			occursList.putParam("OOCityCode", thisL5060Vo.get("F19"));// 擔保品地區別

			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}