package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
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
	private CustNoticeService sCustNoticeService;

	/* DB服務注入 */
	@Autowired
	private FacMainService facMainService;

	@Autowired
	private CustMainService iCustMainService;

	@Autowired
	private CdReportService iCdReportService;

	@Autowired
	private CdEmpService iCdEmpService;

	@Autowired
	private L1908ServiceImpl l1908ServiceImpl;

	/* 轉換工具 */
	@Autowired
	private Parse iParse;

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
			Slice<FacMain> slFacMain = facMainService.facmCustNoRange(custNo, custNo, 1, 999, 0, Integer.MAX_VALUE,
					titaVo);
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
		Slice<CustNotice> slCustNotice = sCustNoticeService.facmNoEq(custNo, facmNo, facmNo, 0, Integer.MAX_VALUE,
				titaVo);
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
					this.info("更新日期1      = " + iParse.stringToStringDateTime(dVo.get("LastUpdate")));
					this.info("更新日期2      = " + dVo.get("LastUpdate"));

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

}