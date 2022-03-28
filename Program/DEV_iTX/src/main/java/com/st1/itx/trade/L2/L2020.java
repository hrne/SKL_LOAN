package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2020")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2020 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public GuarantorService sGuarantorService;

	@Autowired
	public CdGuarantorService sCdGuarantorService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2020 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 155 * 350 = 54250

		// 取tita案號
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));
		// 取tita戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 取tita統一編號
		String iCustId = titaVo.getParam("CustId");
		// 取tita核准編號
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// new array list
		List<Guarantor> lGuarantor = new ArrayList<Guarantor>();
		List<FacMain> lFacMain = new ArrayList<FacMain>();

		// ArrayList of 核准號碼
		ArrayList<Integer> listApproveNo = new ArrayList<Integer>();
		// wk
		int wkApplNo = 0;
		// input四擇一輸入
		if (iCustNo > 0) {

			// 戶號不為0 查出屬於此戶號的所有額度號碼
			Slice<FacMain> slFacMain = sFacMainService.facmCustNoRange(iCustNo, iCustNo, 0, 999, this.index, this.limit, titaVo);
			lFacMain = slFacMain == null ? null : slFacMain.getContent();

			if (lFacMain == null || lFacMain.size() == 0) {
				throw new LogicException(titaVo, "E0001", "額度主檔"); // 此戶號查無資料
			}
			for (FacMain tFacMain : lFacMain) {

				if (wkApplNo != tFacMain.getApplNo()) {
					wkApplNo = tFacMain.getApplNo();
					listApproveNo.add(tFacMain.getApplNo());
				}
			}

		} else if (!iCustId.isEmpty()) {

			// 統一編號不為空白,查客戶主檔取戶號
			CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
			}

			int tmpCustNo = tCustMain.getCustNo();

			if (tmpCustNo == 0) {
				throw new LogicException(titaVo, "E2030", "客戶資料主檔"); // 戶號為0
			}

			// 查出屬於此戶號的所有額度號碼
			Slice<FacMain> slFacMain = sFacMainService.facmCustNoRange(tmpCustNo, tmpCustNo, 0, 999, this.index, this.limit, titaVo);
			lFacMain = slFacMain == null ? null : slFacMain.getContent();

			if (lFacMain == null || lFacMain.size() == 0) {
				throw new LogicException(titaVo, "E2029", "額度主檔"); // 此戶號查無資料
			}

			for (FacMain tFacMain : lFacMain) {

				if (wkApplNo != tFacMain.getApplNo()) {
					wkApplNo = tFacMain.getApplNo();
					listApproveNo.add(tFacMain.getApplNo());
				}
			}

		} else if (iApplNo > 0) {

			// 核准號碼不為0,查額度主檔確認核准號碼是否存在
			FacMain tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);

			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔"); // 此核准號碼在額度主檔無資料
			}

			listApproveNo.add(iApplNo);

		} else if (iCaseNo > 0) {

			// 案號不為0,查額度主檔
			Slice<FacMain> slFacMain = sFacMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, 0, 999, this.index, this.limit, titaVo);
			lFacMain = slFacMain == null ? null : slFacMain.getContent();

			// 案號查無資料拋錯
			if (lFacMain == null || lFacMain.size() == 0) {
				throw new LogicException(titaVo, "E2031", "額度主檔"); // 此案號查無資料
			}

			for (FacMain tFacMain : lFacMain) {

				if (wkApplNo != tFacMain.getApplNo()) {
					wkApplNo = tFacMain.getApplNo();
					listApproveNo.add(tFacMain.getApplNo());
				}
			}

		}

		// 用核准號碼清單查保證人檔
		for (int approveNo : listApproveNo) {

			Slice<Guarantor> stmpListGuarantor = sGuarantorService.approveNoEq(approveNo, this.index, this.limit, titaVo);
			List<Guarantor> tmpListGuarantor = stmpListGuarantor == null ? null : new ArrayList<Guarantor>(stmpListGuarantor.getContent());

			if (tmpListGuarantor != null && tmpListGuarantor.size() > 0) {

				// 將每一筆核准號碼查到的保證人清單裝在一起
				for (Guarantor tmpGuarantor : tmpListGuarantor) {
					lGuarantor.add(tmpGuarantor);
				}
			}
		}

		if (lGuarantor == null || lGuarantor.size() == 0) {
			throw new LogicException(titaVo, "E2003", "保證人檔"); // 查無資料
		}

		lGuarantor.sort((c1, c2) -> {
			if (c1.getApproveNo() != c2.getApproveNo()) {
				return c1.getApproveNo() - c2.getApproveNo();
			} else if (c1.getGuaDate() != c2.getGuaDate()) {
				return c1.getGuaDate() - c2.getGuaDate();
			}
			return 0;
		});

		int count = 0;
		// 將最終的保證人清單裝入tota
		for (Guarantor tGuarantor : lGuarantor) {
			this.info("L2020 tGuarantor = " + tGuarantor);

			// new occurs
			OccursList occursList = new OccursList();

			// table取ApplyNo進額度主檔找額度編號FacmNo塞進occurs
			int tmpApproveNo = tGuarantor.getApproveNo();

			FacMain tFacMain = sFacMainService.facmApplNoFirst(tmpApproveNo, titaVo);

			if (tFacMain == null) {
//				throw new LogicException(titaVo, "E2003", tmpApproveNo+"額度主檔"); // 查無資料
				continue;
			}

			// 用保證人鍵值GuaUKey查客戶主檔取統一編號及姓名
			String custukey = tGuarantor.getGuaUKey();
			CustMain tCustMain = sCustMainService.findById(custukey, titaVo);
			if (tCustMain == null) {
				count++;
				if (lGuarantor.size() == count) {
					throw new LogicException(titaVo, "E2003", custukey + "客戶主檔"); // 查無資料
				}
				continue;
			}

			if (iFacmNo != 0) { // 額度不查全部

				if (iFacmNo == tFacMain.getFacmNo()) { // 額度相同時

					occursList.putParam("OOCustNo", tFacMain.getCustNo());// 借款戶戶號
					occursList.putParam("OOFacmNo", tFacMain.getFacmNo()); // 借款戶額度號碼
					occursList.putParam("OOCustId", tCustMain.getCustId()); // 保證人統編
					occursList.putParam("OOGuarantor", tCustMain.getCustName()); // 保證人姓名
					occursList.putParam("OOGuaRel", tGuarantor.getGuaRelCode());
					occursList.putParam("OOGuaAmt", tGuarantor.getGuaAmt());
					occursList.putParam("OOGuaDt", tGuarantor.getGuaDate());
					occursList.putParam("OOApplNo", tGuarantor.getApproveNo()); // 借款戶核准號碼
					occursList.putParam("OOGuaType", tGuarantor.getGuaTypeCode());
					occursList.putParam("OOStatCd", tGuarantor.getGuaStatCode());
					occursList.putParam("OOCancelDate", tGuarantor.getCancelDate());

					int lastFacmNo = tCustMain.getLastFacmNo();

					if (lastFacmNo > 0) { // 借戶否
						occursList.putParam("OOBorrower", "Y");
					} else {
						occursList.putParam("OOBorrower", "N");
					}

					CdGuarantor iCdGuarantor = sCdGuarantorService.findById(tGuarantor.getGuaRelCode(), titaVo);
					if (iCdGuarantor == null) {
						occursList.putParam("OOGuaRelX", "");
					} else {
						occursList.putParam("OOGuaRelX", iCdGuarantor.getGuaRelItem());
					}

					this.info("occursList L2020" + occursList);

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				} // if

			} else {
				occursList.putParam("OOCustNo", tFacMain.getCustNo());// 借款戶戶號
				occursList.putParam("OOFacmNo", tFacMain.getFacmNo()); // 借款戶額度號碼
				occursList.putParam("OOCustId", tCustMain.getCustId()); // 保證人統編
				occursList.putParam("OOGuarantor", tCustMain.getCustName()); // 保證人姓名
				occursList.putParam("OOGuaRel", tGuarantor.getGuaRelCode());
				occursList.putParam("OOGuaAmt", tGuarantor.getGuaAmt());
				occursList.putParam("OOGuaDt", tGuarantor.getGuaDate());
				occursList.putParam("OOApplNo", tGuarantor.getApproveNo()); // 借款戶核准號碼
				occursList.putParam("OOGuaType", tGuarantor.getGuaTypeCode());
				occursList.putParam("OOStatCd", tGuarantor.getGuaStatCode());
				occursList.putParam("OOCancelDate", tGuarantor.getCancelDate());

				int lastFacmNo = tCustMain.getLastFacmNo();

				if (lastFacmNo > 0) { // 借戶否
					occursList.putParam("OOBorrower", "Y");
				} else {
					occursList.putParam("OOBorrower", "N");
				}

				CdGuarantor iCdGuarantor = sCdGuarantorService.findById(tGuarantor.getGuaRelCode(), titaVo);
				if (iCdGuarantor == null) {
					occursList.putParam("OOGuaRelX", "");
				} else {
					occursList.putParam("OOGuaRelX", iCdGuarantor.getGuaRelItem());
				}

				this.info("occursList L2020" + occursList);

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}

		if (this.totaVo.getOccursList() == null || this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E2003", "保證人檔"); // 查無資料
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}