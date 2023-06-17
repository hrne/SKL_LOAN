package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FacStatusCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUST_NO=9,7<br>
 * FACM_NO=9,3<br>
 * COL_IND1=9,1<br>
 * COL_IND2=9,2<br>
 * COL_NO=9,7<br>
 * SearchClsFg=X,1<br>
 * END=X,1<br>
 */

@Service("L2017")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2017 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public FacShareApplService sFacShareApplService;
	@Autowired
	public FacCaseApplService sFacCaseApplService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public InsuOrignalService sInsuOrignalService;
	@Autowired
	public CdCodeService sCdCodeService;

	@Autowired
	public FacStatusCom facStatusCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2017 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 240*200=48000

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// 額度號碼
		int iFacmNoStartAt = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iFacmNoEndAt = 999;

		// 有輸入額度號碼則用輸入值查詢
		// 無輸入範圍0-999查詢
		if (iFacmNoStartAt > 0) {
			iFacmNoEndAt = iFacmNoStartAt;
		}

		// 擔保品代號1
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));

		// 擔保品代號2
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));

		// 擔保品編號
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// 結案標記
		String iSearchClsFg = titaVo.getParam("SearchClsFg");
		this.info("結案標記 :" + iSearchClsFg);
//		若未輸入則查全部
		int ClCode1St = 0;
		int ClCode1Ed = 9;
		if (iClCode1 > 0) {
			ClCode1St = iClCode1;
			ClCode1Ed = iClCode1;
		}
		int ClCode2St = 0;
		int ClCode2Ed = 99;
		if (iClCode2 > 0) {
			ClCode2St = iClCode2;
			ClCode2Ed = iClCode2;
		}
		int ClNoSt = 0;
		int ClNoEd = 9999999;
		if (iClNo > 0) {
			ClNoSt = iClNo;
			ClNoEd = iClNo;
		}
//
//		// wk
//		int wkClCode1 = 0;
//		int wkClCode2 = 0;
//		int wkClNo = 0;

		// new ArrayList
		List<ClFac> lClFac = new ArrayList<ClFac>();

		// new table
		CustMain tCustMain = new CustMain();
		ClMain tClMain = new ClMain();
		FacMain tFacMain = new FacMain();
		FacCaseAppl tFacCaseAppl = new FacCaseAppl();
		InsuOrignal tInsuOrignal = new InsuOrignal();
		Slice<ClFac> slClFac = null;
		// 處理邏輯 二擇一輸入
		if (iCustNo != 0) {
			slClFac = sClFacService.selectForL2017CustNo(iCustNo, iFacmNoStartAt, iFacmNoEndAt, this.index, this.limit,
					titaVo);
		} else if (iClCode1 != 0 || iClCode2 != 0 || iClNo != 0) {
			slClFac = sClFacService.selectForL2049(ClCode1St, ClCode1Ed, ClCode2St, ClCode2Ed, ClNoSt, ClNoEd, 0,
					9999999, 0, 999, 0, 9999999, this.index, this.limit, titaVo);
		} else {
			slClFac = sClFacService.findAll(this.index, this.limit, titaVo);
		}

		lClFac = slClFac == null ? null : new ArrayList<ClFac>(slClFac.getContent());
		this.info("lClFac = " + lClFac);
		if (lClFac == null || lClFac.size() == 0) {
			// 查無資料
			throw new LogicException("E0001", "額度與擔保品關聯檔");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClFac != null && slClFac.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

//		lClFac
		// ClCode1 ASC,ClCode2 ASC,ClNo ASC,CustNo,ApproveNo ASC,FacmNo ASC
		// 依ClCode1 ClCode2 ClNo CustNo ApproveNo FacmNo排序
		lClFac.sort((c1, c2) -> {
			if (c1.getClCode1() - c2.getClCode1() != 0) {
				return c1.getClCode1() - c2.getClCode1();
			} else if (c1.getClCode2() - c2.getClCode2() != 0) {
				return c1.getClCode2() - c2.getClCode2();
			} else if (c1.getClNo() - c2.getClNo() != 0) {
				return c1.getClNo() - c2.getClNo();
			} else if (c1.getCustNo() - c2.getCustNo() != 0) {
				return c1.getCustNo() - c2.getCustNo();
			} else if (c1.getApproveNo() - c2.getApproveNo() != 0) {
				return c1.getApproveNo() - c2.getApproveNo();
			} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
				return c1.getFacmNo() - c2.getFacmNo();
			} else {
				return 0;
			}
		});
		for (ClFac tmpClFac : lClFac) {

			int thisCustNo = tmpClFac.getCustNo();
			int thisFacmNo = tmpClFac.getFacmNo();

			// 取戶名
			tCustMain = sCustMainService.custNoFirst(thisCustNo, thisCustNo, titaVo);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}

			// 取鑑價總值
			tClMain = sClMainService
					.findById(new ClMainId(tmpClFac.getClCode1(), tmpClFac.getClCode2(), tmpClFac.getClNo()), titaVo);
			if (tClMain == null) {
				tClMain = new ClMain();
			}
			// 取保險單號碼初保檔
			tInsuOrignal = sInsuOrignalService.clNoFirst(tmpClFac.getClCode1(), tmpClFac.getClCode2(),
					tmpClFac.getClNo(), titaVo);
			if (tInsuOrignal == null) {
				tInsuOrignal = new InsuOrignal();
			}

			// 取核准額度
			tFacMain = sFacMainService.findById(new FacMainId(thisCustNo, thisFacmNo), titaVo);
			if (tFacMain == null) {
				tFacMain = new FacMain();
			}
			// 取准駁日期
			tFacCaseAppl = sFacCaseApplService.findById(tFacMain.getApplNo(), titaVo);
			if (tFacCaseAppl == null) {
				tFacCaseAppl = new FacCaseAppl();
			}

			// 容器 : 計算額度下所有撥款序號之貸放金額加總
			BigDecimal loanAmt = new BigDecimal(0);
			// 容器 : 計算額度下所有撥款序號之貸放餘額加總
			BigDecimal loanBal = new BigDecimal(0);
			// 容器 : 此額度下最後一筆撥款序號之戶況
			int loanStatus = 99;
			String loanStatusX = "";

			Slice<LoanBorMain> slLoanBorMain = sLoanBorMainService.bormCustNoEq(thisCustNo, thisFacmNo, thisFacmNo, 0,
					900, 0, Integer.MAX_VALUE, titaVo);

			if (slLoanBorMain != null) {
				loanStatus = facStatusCom.settingStatus(slLoanBorMain.getContent(),
						this.txBuffer.getTxBizDate().getTbsDy());
				// 含結案為N時跳掉結案戶
				if (iSearchClsFg.equals("N")) {
					if (loanStatus == 1 || loanStatus == 3 || loanStatus == 8 || loanStatus == 9) {
						continue;
					}
				}
				for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
					// 計算額度下所有撥款序號之貸放金額加總
					loanAmt = loanAmt.add(tLoanBorMain.getDrawdownAmt());

					// 計算額度下所有撥款序號之貸放餘額加總
					loanBal = loanBal.add(tLoanBorMain.getLoanBal());
				}

			}

			// 取狀態
			if (loanStatus == 99) {
				loanStatusX = "未撥款";
			} else {
				CdCode tCdCode = sCdCodeService.findById(new CdCodeId("Status", parse.IntegerToString(loanStatus, 2)),
						titaVo);
				if (tCdCode != null) {
					loanStatusX = tCdCode.getItem();
				}
			}

			// 同一擔保品編號只顯示第一筆
			String wkOrigInsuNo = "";
			wkOrigInsuNo = tInsuOrignal.getOrigInsuNo();
//			if (wkClCode1 == tmpClFac.getClCode1() && wkClCode2 == tmpClFac.getClCode2() && wkClNo == tmpClFac.getClNo()) {
//				wkOrigInsuNo = "";
//			} else {
//			wkOrigInsuNo = tInsuOrignal.getOrigInsuNo();
//			}
//			wkClCode1 = tmpClFac.getClCode1();
//			wkClCode2 = tmpClFac.getClCode2();
//			wkClNo = tmpClFac.getClNo();

			// new occurs
			OccursList occurslist = new OccursList();

			String custNo = FormatUtil.pad9(String.valueOf(thisCustNo), 7) + "-"
					+ FormatUtil.pad9(String.valueOf(thisFacmNo), 3);

			occurslist.putParam("OOCustNo", custNo);
			occurslist.putParam("OCustNo", thisCustNo);
			occurslist.putParam("OFacmNo", thisFacmNo);

			occurslist.putParam("OOCustName", tCustMain.getCustName());
			occurslist.putParam("OOCreditSysNo", tFacCaseAppl.getCreditSysNo());
			String clNo = String.valueOf(tmpClFac.getClCode1()) + "-"
					+ FormatUtil.pad9(String.valueOf(tmpClFac.getClCode2()), 2) + "-"
					+ FormatUtil.pad9(String.valueOf(tmpClFac.getClNo()), 7);

			occurslist.putParam("OOClNo", clNo);
			occurslist.putParam("OClCode1", tmpClFac.getClCode1());
			occurslist.putParam("OClCode2", tmpClFac.getClCode2());
			occurslist.putParam("OClNo", tmpClFac.getClNo());
			occurslist.putParam("OOApplNo", tmpClFac.getApproveNo());
			occurslist.putParam("OOEvaNotWorth", tmpClFac.getOriEvaNotWorth());
			occurslist.putParam("OOEvaAmt", tClMain.getEvaAmt());
			occurslist.putParam("OOSettingAmt", tmpClFac.getOriSettingAmt());
			// 核准日期 案件申請檔 新增funtion 依核准號碼,處理情形找駁准日期
			occurslist.putParam("OOApproveDate", tFacCaseAppl.getApproveDate());
			occurslist.putParam("OOLineAmt", tFacMain.getLineAmt());
			// 貸放金額 (額度下所有撥款序號之貸放金額加總)
			occurslist.putParam("OOLoanAmt", loanAmt);
			// 貸放餘額 (額度下所有撥款序號之貸放餘額加總)
			occurslist.putParam("OOLoanBal", loanBal);
			// 狀態 (此額度下最後一筆撥款序號之戶況)
			occurslist.putParam("OOLoanStatus", loanStatus);
			occurslist.putParam("OOLoanStatusX", loanStatusX);
			// 保單號碼
			occurslist.putParam("OOInsuNo", wkOrigInsuNo);
			// 可分配金額
			occurslist.putParam("OOShareTotal", tClMain.getShareTotal());
			// 分配金額
			occurslist.putParam("OOShareAmt", tmpClFac.getShareAmt());
			// 共同借款人
			FacShareAppl tFacShareAppl = sFacShareApplService.findById(tmpClFac.getApproveNo(), titaVo);
			if (tFacShareAppl != null) {
				occurslist.putParam("OOShareFacmNoFg", "Y");
				occurslist.putParam("OOShareCustNo", tFacShareAppl.getCustNo());
				occurslist.putParam("OOShareFacmNo", tFacShareAppl.getFacmNo());
			} else {
				occurslist.putParam("OOShareFacmNoFg", "");
				occurslist.putParam("OOShareCustNo", "0000000");
				occurslist.putParam("OOShareFacmNo", "000");
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		List<LinkedHashMap<String, String>> chkOccursList = this.totaVo.getOccursList();

		if (chkOccursList == null || chkOccursList.size() == 0) {
			throw new LogicException("E2003", "額度與擔保品關聯檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}