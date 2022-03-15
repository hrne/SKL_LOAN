package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * L2010 申請案件明細資料查詢
 * a.此功能提供申請案件資料作准/駁處理或查詢
 * b.若申請案件資料為准時,同時輸入額度資料
 */
/*
 * Tita
 * ApplNo=9,7
 * CustId=X,10
 * ProcessCode=X,1
 */
/**
 * L2010 申請案件明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2010")
@Scope("prototype")
public class L2010 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2010.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2010 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		String iCustId = titaVo.getParam("CustId").trim();
		String iGroupId = titaVo.getParam("GroupId").trim();
		String iProcessCode = titaVo.getParam("ProcessCode");
		String iCustUkey = "";
		String iGroupUKey = "";
		if (!iCustId.isEmpty()) {
			CustMain tCustMain = custMainService.custIdFirst(iCustId, titaVo);
			if (tCustMain != null) {
				iCustUkey = tCustMain.getCustUKey();
			} else {
				throw new LogicException(titaVo, "E2003", "客戶資料主檔"); // 查無資料
			}
		}

		if (!iGroupId.isEmpty()) {
			CustMain tCustMain = custMainService.custIdFirst(iGroupId, titaVo);
			if (tCustMain != null) {
				iGroupUKey = tCustMain.getCustUKey();
			} else {
				throw new LogicException(titaVo, "E2003", "客戶資料主檔"); // 查無資料
			}
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 47 * 1000 = 47000

		// 查詢申請案件檔
		Slice<FacCaseAppl> lFacCaseAppl;
		if (iApplNo > 0) { // 以申請號碼查詢
			if (iProcessCode.equals("9")) {
				lFacCaseAppl = facCaseApplService.caseApplNoRange(iApplNo, 9999999, "0", "2", this.index, this.limit, titaVo);
			} else {
				lFacCaseAppl = facCaseApplService.caseApplNoRange(iApplNo, 9999999, iProcessCode, iProcessCode, this.index, this.limit, titaVo);
			}
		} else if (!iCustId.isEmpty()) { // 以統一編號查詢查詢
			if (iProcessCode.equals("9")) {
				lFacCaseAppl = facCaseApplService.caseApplCustUKeyEq(iCustUkey, "0", "2", this.index, this.limit, titaVo);
			} else {
				lFacCaseAppl = facCaseApplService.caseApplCustUKeyEq(iCustUkey, iProcessCode, iProcessCode, this.index, this.limit, titaVo);
			}
		} else { // 以團體戶統編查詢
			if (iProcessCode.equals("9")) {
				lFacCaseAppl = facCaseApplService.caseApplGroupUKeyEq(iGroupUKey, "0", "2", this.index, this.limit, titaVo);
			} else {
				lFacCaseAppl = facCaseApplService.caseApplGroupUKeyEq(iGroupUKey, iProcessCode, iProcessCode, this.index, this.limit, titaVo);
			}
		}

		if (lFacCaseAppl == null || lFacCaseAppl.isEmpty()) {
			throw new LogicException(titaVo, "E2003", "案件申請檔"); // 查無資料
		}
		// 如有有找到資料
		for (FacCaseAppl tFacCaseAppl : lFacCaseAppl.getContent()) {
			OccursList occursList = new OccursList();
			FacMain tFacMain = new FacMain();

			occursList.putParam("OOApplNo", tFacCaseAppl.getApplNo());
			CustMain tCustMain = custMainService.findById(tFacCaseAppl.getCustUKey(), titaVo);
			if (tCustMain != null) {
				occursList.putParam("OOCustId", tCustMain.getCustId());
			} else {
				occursList.putParam("OOCustId", "");
//				throw new LogicException(titaVo, "E2003", "客戶資料主檔"); // 查無資料
			}
			occursList.putParam("OOApplDate", tFacCaseAppl.getApplDate());
			occursList.putParam("OOCurrencyCode", tFacCaseAppl.getCurrencyCode());
			occursList.putParam("OOApplAmt", tFacCaseAppl.getApplAmt());
			occursList.putParam("OOProcessCode", tFacCaseAppl.getProcessCode());

			iGroupUKey = tFacCaseAppl.getGroupUKey();
			if (!"".equals(iGroupUKey)) {
				tCustMain = custMainService.findById(iGroupUKey, titaVo);
				if (tCustMain != null) {
					occursList.putParam("OOGroupId", tCustMain.getCustId());
				} else {
					throw new LogicException(titaVo, "E2003", "客戶資料主檔"); // 查無資料
				}
			} else {
				occursList.putParam("OOGroupId", "");
			}

			tFacMain = facMainService.facmApplNoFirst(tFacCaseAppl.getApplNo(), titaVo);
			if (tFacMain == null) {
				occursList.putParam("OOAcctCode", "");
			} else {
				occursList.putParam("OOAcctCode", tFacMain.getAcctCode());
			}
			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (lFacCaseAppl != null && lFacCaseAppl.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}