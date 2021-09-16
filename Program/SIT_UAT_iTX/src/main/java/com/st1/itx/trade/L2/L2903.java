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
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUSTID=X,10<br>
 * END=X,1<br>
 */

@Service("L2903")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2903 extends TradeBuffer {


	/* DB服務注入 */
	@Autowired
	public ReltMainService sReltMainService;
	
	/* DB服務注入 */
	@Autowired
	public LoanBorMainService sLoanBorMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;


	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2903 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 155 * 300 = 46500

		String iCustId = titaVo.getParam("CustId");
		String iCustName = titaVo.getParam("CustName");
		// new table
		
		FacMain tFacMain = new FacMain();
		
		CustMain tCustMain = new CustMain();
		
		ReltMain tReltMain = new ReltMain();
		
		// new ArrayList
		List<LoanBorMain> tmplLoanBorMain = new ArrayList<LoanBorMain>();
		
		// 必須輸入,不得為空白且須存在於顧客檔,不存在,拋錯
		Boolean inflag = false;
		if ("".equals(iCustId)) {

			tReltMain = sReltMainService.reltnameFirst(iCustName, titaVo);
			
			if (tReltMain == null) {
				throw new LogicException("E0001", "借款戶關係人/關係企業主檔");
			}
		} else {
			tReltMain = sReltMainService.reltIdFirst(iCustId, titaVo);
			if (tReltMain == null) {
				throw new LogicException("E0001", "借款戶關係人/關係企業主檔");
			}
		}

		String RelId = tReltMain.getReltId();
		CustMain tmpCustMain = sCustMainService.custIdFirst(RelId, titaVo);

		// 多筆關聯人統編是否存在於客戶資料主檔
		if (tmpCustMain == null) {
			throw new LogicException("E0001", "客戶資料主檔");
		} // if
		
		// 存在檢查放款主檔
		int custNo = tmpCustMain.getCustNo();

		if (custNo > 0) {
			// 取放款主檔所有該戶號資料
			Slice<LoanBorMain> stmplLoanBorMain = sLoanBorMainService.bormCustNoEq(custNo, 0, 999, 0, 900, 0, Integer.MAX_VALUE, titaVo);
			tmplLoanBorMain = stmplLoanBorMain == null ? null : stmplLoanBorMain.getContent();
		}
		
		// 查無資料拋錯
		if (tmplLoanBorMain == null || custNo == 0) {
			throw new LogicException("E0001", "放款主檔");
		}
		
		this.info("custNo = " + custNo);
		this.info("tmplLoanBorMain SIZE  = " + tmplLoanBorMain.size());
		inflag = true;
		for (LoanBorMain tmpLoanBorMain : tmplLoanBorMain) {
			// new TABLE
			tCustMain = new CustMain();

			// 取額度主檔資料
			tFacMain = sFacMainService.findById(new FacMainId(tmpLoanBorMain.getCustNo(), tmpLoanBorMain.getFacmNo()), titaVo);
			if (tFacMain == null) {
				tFacMain = new FacMain();
			}
			// 取關聯戶統編 姓名
			tCustMain = sCustMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}

			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOCustId", tmpCustMain.getCustId());
			occurslist.putParam("OOCustName", tmpCustMain.getCustName());
			occurslist.putParam("OOApplNo", tFacMain.getApplNo());
			occurslist.putParam("OOCustNo", tFacMain.getCustNo());
			occurslist.putParam("OOFacmNo", tFacMain.getFacmNo());
			occurslist.putParam("OOBormNo", tmpLoanBorMain.getBormNo());
			occurslist.putParam("OOLineAmt", tFacMain.getLineAmt());
			occurslist.putParam("OORate", tmpLoanBorMain.getApproveRate());
			occurslist.putParam("OOLoanBal", tmpLoanBorMain.getLoanBal());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		} // for
		

//
//		for (CustRelDetail tmpCustRelDetail : lCustRelDetail) {
//			// 啟用記號= Y:啟用 N:停用
//
//			if (tmpCustRelDetail.getStatus().equals("N")) {
//				continue;
//			}
//
//			String RelId = tmpCustRelDetail.getRelId();
//
//			CustMain tmpCustMain = sCustMainService.custIdFirst(RelId, titaVo);
//
//			// 多筆關聯人統編是否存在於客戶資料主檔
//			if (tmpCustMain == null) {
//				continue;
//			} // id
//
//			// 存在檢查放款主檔
//			int custNo = tmpCustMain.getCustNo();
//
//			if (custNo > 0) {
//				// 取放款主檔所有該戶號資料
//				Slice<LoanBorMain> stmplLoanBorMain = sLoanBorMainService.bormCustNoEq(custNo, 0, 999, 0, 900, 0, Integer.MAX_VALUE, titaVo);
//				tmplLoanBorMain = stmplLoanBorMain == null ? null : stmplLoanBorMain.getContent();
//			}
//
//			// 查無資料拋錯
//			if (tmplLoanBorMain == null || custNo == 0) {
//				continue;
//			}
//
//			this.info("custNo = " + custNo);
//			this.info("tmplLoanBorMain SIZE  = " + tmplLoanBorMain.size());
//			inflag = true;
//			for (LoanBorMain tmpLoanBorMain : tmplLoanBorMain) {
//				// new TABLE
//				tCustMain = new CustMain();
//
//				// 取額度主檔資料
//				tFacMain = sFacMainService.findById(new FacMainId(tmpLoanBorMain.getCustNo(), tmpLoanBorMain.getFacmNo()), titaVo);
//				if (tFacMain == null) {
//					tFacMain = new FacMain();
//				}
//				// 取關聯戶統編 姓名
//				tCustMain = sCustMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
//				if (tCustMain == null) {
//					tCustMain = new CustMain();
//				}
//
//				// new occurs
//				OccursList occurslist = new OccursList();
//
//				occurslist.putParam("OOCustId", tmpCustMain.getCustId());
//				occurslist.putParam("OOCustName", tmpCustMain.getCustName());
//				occurslist.putParam("OOApplNo", tFacMain.getApplNo());
//				occurslist.putParam("OOCustNo", tFacMain.getCustNo());
//				occurslist.putParam("OOFacmNo", tFacMain.getFacmNo());
//				occurslist.putParam("OOBormNo", tmpLoanBorMain.getBormNo());
//				occurslist.putParam("OOLineAmt", tFacMain.getLineAmt());
//				occurslist.putParam("OORate", tmpLoanBorMain.getApproveRate());
//				occurslist.putParam("OOLoanBal", tmpLoanBorMain.getLoanBal());
//
//				/* 將每筆資料放入Tota的OcList */
//				this.totaVo.addOccursList(occurslist);
//			} // for
//
//		} // for
//
//		if (!inflag) {
//			throw new LogicException("E0001", "放款主檔");
//		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}