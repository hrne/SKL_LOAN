package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.springjpa.cm.L1908ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1908")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1908 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustNoticeService sCustNoticeService;

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CdReportService iCdReportService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Autowired
	L1908ServiceImpl l1908ServiceImpl;

	/* 轉換工具 */
	@Autowired
	public Parse iParse;

	boolean found = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1908 ");
		this.totaVo.init(titaVo);

		// 取tita戶號
		int custNo = iParse.stringToInteger(titaVo.getParam("CustNo"));

		// FacmNo額度編號
		int facmNo = iParse.stringToInteger(titaVo.getParam("FacmNo"));

		String custId = titaVo.getParam("CustId");

		if (custNo == 0) {
			CustMain custMain = iCustMainService.custIdFirst(custId, titaVo);
			if (custMain == null) {
				throw new LogicException("E0001", "客戶檔查無此統一編號:" + custId); // 查無資料
			}
			custNo = custMain.getCustNo();
		}

		if (facmNo > 0) {
			findData(titaVo, custNo, facmNo);
		} else {
			Slice<FacMain> slFacMain = facMainService.facmCustNoRange(custNo, custNo, 1, 999, 0, Integer.MAX_VALUE, titaVo);
			List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
			if (lFacMain != null) {
				for (FacMain tFacMain : lFacMain) {
					findData(titaVo, custNo, tFacMain.getFacmNo());
				}
			}
		}

		if (!found) {
			throw new LogicException(titaVo, "E0001", "客戶無申請不列印通知書");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void findData(TitaVo titaVo, int custNo, int facmNo) throws LogicException {
		Slice<CustNotice> slCustNotice = sCustNoticeService.facmNoEq(custNo, facmNo, facmNo, 0, Integer.MAX_VALUE, titaVo);
		List<CustNotice> lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();
		if (lCustNotice == null || lCustNotice.size() == 0) {
			return;
		}
		try {

			List<Map<String, String>> dList = l1908ServiceImpl.FindData(titaVo, custNo, facmNo);

			if (dList != null && dList.size() != 0) {
				for (Map<String, String> dVo : dList) {
					OccursList occursList = new OccursList();
					occursList.putParam("OOCustNo", custNo);
					occursList.putParam("OOFacmNo", facmNo);
					occursList.putParam("OOPaper", dVo.get("PaperNotice"));
					occursList.putParam("OOMsg", dVo.get("MsgNotice"));
					occursList.putParam("OOEMail", dVo.get("EmailNotice"));
					occursList.putParam("OOFormNo", dVo.get("FormNo"));
					occursList.putParam("OOFormName", dVo.get("FormName"));
					occursList.putParam("OOApplyDt", dVo.get("ApplyDate"));
					occursList.putParam("OOLastUpdateEmpNo", dVo.get("LastUpdateEmpNo"));
					occursList.putParam("OOLastUpdateEmpNoName", dVo.get("Fullname"));
					occursList.putParam("OOLastUpdate", iParse.stringToStringDateTime(dVo.get("LastUpdate")));

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);

					found = true;
				}
			}
		} catch (LogicException e) {
			throw e;
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "查詢語法錯誤"); // 查無資料
		}

	}

	public ArrayList<TotaVo> run2(TitaVo titaVo) throws LogicException {
		this.info("active L1908 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 300; // 25 * 500 = 12500

		// 取tita戶號
		int iCustNo = iParse.stringToInteger(titaVo.getParam("CustNo"));

		// FacmNo額度編號
		int iFacmNo = iParse.stringToInteger(titaVo.getParam("FacmNo"));

		String iCustId = titaVo.getParam("CustId");

		// 宣告new list
		List<CustNotice> lCustNotice = new ArrayList<CustNotice>();
		Slice<CustNotice> slCustNotice = null;
		// 若戶號為空(int == 0 )
		if (iCustNo == 0 && iFacmNo == 0 && iCustId.equals("")) {

			// 查所有客戶通知設定檔
			slCustNotice = sCustNoticeService.findAll(this.index, this.limit, titaVo);
			lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();

		} else if (iCustNo != 0 && iFacmNo != 0) {

			slCustNotice = sCustNoticeService.facmNoEq(iCustNo, iFacmNo, iFacmNo, this.index, this.limit, titaVo);
			lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();

		} else {
			// 使用者輸入統編查詢，將iCustNo替換為查詢結果
			if (!iCustId.equals("")) {
				CustMain iCustMain = new CustMain();
				iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
				if (iCustMain == null) {
					throw new LogicException("E0001", "客戶檔查無此統一編號:" + iCustId); // 查無資料
				}
				iCustNo = iCustMain.getCustNo();
			}
			slCustNotice = sCustNoticeService.findCustNo(iCustNo, this.index, this.limit, titaVo);
			lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();
		}

		if (lCustNotice == null || lCustNotice.size() == 0) {
			throw new LogicException("E0001", "客戶通知設定檔"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCustNotice != null && slCustNotice.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (CustNotice tCustNotice : lCustNotice) {

			// new occurs
			OccursList occursList = new OccursList();
			String iFormNo = tCustNotice.getFormNo();
			CdReport iCdReport = new CdReport();
			CdEmp iCdEmp = new CdEmp();
			iCdReport = iCdReportService.findById(iFormNo, titaVo);
//			if(iCdReport.getSendCode()!=0) {
//				//CdReport 報送記號不為0才報送
//				occursList.putParam("OOCustNo", tCustNotice.getCustNo());
//				occursList.putParam("OOFacmNo", tCustNotice.getFacmNo());
//				occursList.putParam("OOPaper", tCustNotice.getPaperNotice());
//				occursList.putParam("OOMsg", tCustNotice.getMsgNotice());
//				occursList.putParam("OOEMail", tCustNotice.getEmailNotice());
//				occursList.putParam("OOFormNo", iFormNo);
//				occursList.putParam("OOFormName", iCdReport.getFormName());
//				occursList.putParam("OOApplyDt", tCustNotice.getApplyDate());
//			}else {
//				continue;
//			}		
			occursList.putParam("OOCustNo", tCustNotice.getCustNo());
			occursList.putParam("OOFacmNo", tCustNotice.getFacmNo());
			occursList.putParam("OOPaper", tCustNotice.getPaperNotice());
			occursList.putParam("OOMsg", tCustNotice.getMsgNotice());
			occursList.putParam("OOEMail", tCustNotice.getEmailNotice());
			occursList.putParam("OOFormNo", iFormNo);
			if (iCdReport == null) {
				occursList.putParam("OOFormName", "");
			} else {
				occursList.putParam("OOFormName", iCdReport.getFormName());
			}
			occursList.putParam("OOApplyDt", tCustNotice.getApplyDate());
			occursList.putParam("OOLastUpdateEmpNo", tCustNotice.getLastUpdateEmpNo());
			iCdEmp = iCdEmpService.findById(tCustNotice.getLastUpdateEmpNo(), titaVo);
			if (tCustNotice.getLastUpdateEmpNo().equals("")) {
				occursList.putParam("OOLastUpdateEmpNoName", "");
			} else {
				if (iCdEmp == null) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				} else {
					occursList.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
				}
			}
			String taU = tCustNotice.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			occursList.putParam("OOLastUpdate", uaDate);
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}