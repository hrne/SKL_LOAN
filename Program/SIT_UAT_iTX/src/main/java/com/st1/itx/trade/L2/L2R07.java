package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimTxCode=X,5
 * RimCustId=X,10
 * RimCustNo=9,7
 */
/**
 * L2R07 尋找客戶資料主檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R07")
@Scope("prototype")
public class L2R07 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	Parse parse;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo = 0;
	private String iTxCode = "";
	private String wkCurrencyCode = "TWD";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R07 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		// 取得輸入資料
		iTxCode = titaVo.getParam("RimTxCode");
		String iCustId = titaVo.getParam("RimCustId").trim();
		iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));

		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", ""); // 交易代號不可為空白
		}

		if (iCustId.isEmpty() && iCustNo == 0) {
			throw new LogicException(titaVo, "E2051", ""); // 統一編號,戶號須擇一輸入
		}
		/*
		 * 設定第幾分頁 titaVo.getParamReturnIndex() 第一次會是0，如果需折返最後會塞值
		 * sCustMasterService.setPageIndex(titaVo.getParamReturnIndex());
		 */

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// sCustMasterService.setPageLimit(5);

		/* DB服務 */
		CustMain tCustMain = new CustMain();
		if (iCustId.isEmpty()) {
			tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		} else {
			tCustMain = custMainService.custIdFirst(iCustId, titaVo);
		}
		if (tCustMain != null) {
			this.totaVo.putParam("OCustId", tCustMain.getCustId());
			this.totaVo.putParam("OCustNo", tCustMain.getCustNo());
			this.totaVo.putParam("OCustName", tCustMain.getCustName());
			this.totaVo.putParam("OCustTypeCode", tCustMain.getCustTypeCode());
			this.totaVo.putParam("OEntCode", tCustMain.getEntCode());
			this.totaVo.putParam("OPrevPayIntDate", 0);
			this.totaVo.putParam("ONextPayIntDate", 0);
			this.totaVo.putParam("OSpecificDd", 0);
			this.totaVo.putParam("OSpecificDate", 0);
			this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);
			this.totaVo.putParam("OLoanBal", 0);
			this.totaVo.putParam("OFacmCount", 0);
			this.totaVo.putParam("OBormCount", 0);
			this.totaVo.putParam("OFacmNo", 0);
			this.totaVo.putParam("OBormNo", 0);
			// AML使用
			// 身分證長度8為法人
			// 否則為自然人
			if (tCustMain.getCustId().length() == 8) {
				this.totaVo.putParam("ORemitIdKind", 2);// 身份別 1:自然人 2:法人
			} else {
				this.totaVo.putParam("ORemitIdKind", 1);// 身份別 1:自然人 2:法人
			}

			this.totaVo.putParam("ORemitId", tCustMain.getCustId());// 身份證/居留證號碼
			this.totaVo.putParam("ORemitGender", tCustMain.getSex());// 性別
			this.totaVo.putParam("ORemitBirthday", tCustMain.getBirthday());// 出生日期

		} else {
			throw new LogicException(titaVo, "E0001", " 客戶資料主檔"); // 查詢資料不存在
		}
		switch (iTxCode) {
		case "L3711":
		case "L3712":
		case "L3921":
		case "L3925":
			PrevIntDateRoutine(tCustMain.getCustNo()); // 取得戶號之下的最近繳息日及放款餘額
			break;
		case "L3926":
			FacmCountRoutine1(tCustMain.getCustNo()); // 取得戶號之下的額度及放款筆數
			break;
		case "L3701": // 放款內容變更
			FacmCountRoutine2(tCustMain.getCustNo()); // 取得戶號之下的額度及放款筆數
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 取得戶號之下的最近繳息日及放款餘額
	private void PrevIntDateRoutine(int custNo) throws LogicException {
		int wkPrevPayIntDate = 0;
		int wkNextPayIntDate = 0;
		int wkSpecificDd = 0;
		int wkSpecificDate = 0;
		int wkDate = 0;
		int wkBormCount = 0;
		BigDecimal wkLoanBal = BigDecimal.ZERO;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		Slice<LoanBorMain> lLoanBorMain = loanBorMainService.bormCustNoEq(custNo, 1, 999, 1, 900, this.index, this.limit, titaVo);
		if (!(lLoanBorMain == null || lLoanBorMain.isEmpty())) {
			for (LoanBorMain ln : lLoanBorMain.getContent()) {
				if (ln.getStatus() == 0 || ln.getStatus() == 4) { // 0: 正常戶 4: 逾期戶
					wkDate = ln.getPrevPayIntDate() == 0 ? ln.getDrawdownDate() : ln.getPrevPayIntDate();
					if (wkDate > wkPrevPayIntDate) { // && t.getPrevPayIntDate() < wkTbsDy) {
						wkPrevPayIntDate = wkDate;
						wkNextPayIntDate = ln.getNextPayIntDate();
						wkSpecificDd = ln.getSpecificDd();
						wkSpecificDate = ln.getSpecificDate();
						wkCurrencyCode = ln.getCurrencyCode();
					}
					wkLoanBal = wkLoanBal.add(ln.getLoanBal());
					wkBormCount++;
				}
			}
			this.totaVo.putParam("OPrevPayIntDate", wkPrevPayIntDate);
			this.totaVo.putParam("ONextPayIntDate", wkNextPayIntDate);
			this.totaVo.putParam("OSpecificDd", wkSpecificDd);
			this.totaVo.putParam("OSpecificDate", wkSpecificDate);
			this.totaVo.putParam("OCurrencyCode", wkCurrencyCode);
			this.totaVo.putParam("OLoanBal", wkLoanBal);
		}
		if (wkBormCount == 0) {
			if (iTxCode.equals("L3711") || iTxCode.equals("L3712")) {
				throw new LogicException(titaVo, "E0001", " 查無該戶號的最近繳息日"); // 查詢資料不存在
			}
		}
	}

	// 取得戶號之下的額度及放款筆數(攤還方式為本息平均法或本金平均法)
	private void FacmCountRoutine1(int custNo) {
		int wkFacmCount = 0;
		int wkBormCount = 0;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		Slice<FacMain> lFacMain = facMainService.facmCustNoRange(custNo, custNo, 1, 999, this.index, this.limit, titaVo);
		if (!(lFacMain == null || lFacMain.isEmpty())) {
			for (FacMain t : lFacMain.getContent()) {
				Slice<LoanBorMain> lLoanBorMain = loanBorMainService.bormCustNoEq(custNo, t.getFacmNo(), t.getFacmNo(), 1, 900, this.index, this.limit, titaVo);
				if (!(lLoanBorMain == null || lLoanBorMain.isEmpty())) {
					for (LoanBorMain k : lLoanBorMain.getContent()) {
						if ((k.getStatus() == 0 || k.getStatus() == 4) && ("3".equals(k.getAmortizedCode()) || "4".equals(k.getAmortizedCode()))) {
							wkFacmCount++;
							this.totaVo.putParam("OFacmNo", k.getFacmNo());
							this.totaVo.putParam("OBormNo", k.getBormNo());
							break;
						}
					}
					for (LoanBorMain k : lLoanBorMain) {
						if ((k.getStatus() == 0 || k.getStatus() == 4) && ("3".equals(k.getAmortizedCode()) || "4".equals(k.getAmortizedCode()))) {
							wkBormCount++;
						}
					}
				}
			}
			this.totaVo.putParam("OFacmCount", wkFacmCount);
			this.totaVo.putParam("OBormCount", wkBormCount);
		}
	}

	// 取得戶號之下的額度及放款筆數
	private void FacmCountRoutine2(int custNo) throws LogicException {
		this.info("FacmCountRoutine2 ... ");

		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int FacmNoEnd = iFacmNo;
		if (iFacmNo == 0) {
			FacmNoEnd = 999;
		}
		int wkFacmCount = 0;
		int wkBormCount = 0;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		Slice<FacMain> lFacMain = facMainService.facmCustNoRange(custNo, custNo, iFacmNo, FacmNoEnd, this.index, this.limit, titaVo);
		if (lFacMain == null || lFacMain.isEmpty()) {
			throw new LogicException(titaVo, "E3085", " 額度主檔"); // 該戶號沒有額度編號，不可做內容變更
		}
		for (FacMain fac : lFacMain.getContent()) {
			wkFacmCount++;
			this.totaVo.putParam("OFacmNo", fac.getFacmNo());
		}
		for (FacMain fac : lFacMain.getContent()) {
			Slice<LoanBorMain> lLoanBorMain = loanBorMainService.bormCustNoEq(custNo, fac.getFacmNo(), fac.getFacmNo(), 1, 900, this.index, this.limit, titaVo);
			if (!(lLoanBorMain == null || lLoanBorMain.isEmpty())) {
				for (LoanBorMain ln : lLoanBorMain.getContent()) {
					if (ln.getStatus() == 0 || ln.getStatus() == 2 || ln.getStatus() == 4 || ln.getStatus() == 7) {
						wkBormCount++;
						if (wkBormCount == 1) {
							this.totaVo.putParam("OFacmNo", ln.getFacmNo());
							this.totaVo.putParam("OBormNo", ln.getBormNo());
						}
					}
				}
			}
		}
		this.totaVo.putParam("OFacmCount", wkFacmCount);
		this.totaVo.putParam("OBormCount", wkBormCount);
	}
}