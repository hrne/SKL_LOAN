package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * MediaDate=9,7<br>
 */

@Service("L4951")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4951 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4951 ");
		this.totaVo.init(titaVo);

//		1.查詢 入帳日期

		int entryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		int mediaType = parse.stringToInteger(titaVo.getParam("MediaType"));

		List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();
		List<EmpDeductDtl> lEmpDeductDtlold = new ArrayList<EmpDeductDtl>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<EmpDeductDtl> sEmpDeductDtl = null;

		List<String> type = new ArrayList<String>();

		if (mediaType == 1) {
			type.add("4");
			type.add("5");
			sEmpDeductDtl = empDeductDtlService.entryDateRng(entryDate, entryDate, type, this.index, this.limit,
					titaVo);
		} else if (mediaType == 2) {
			type.add("1");
			type.add("2");
			type.add("3");
			type.add("6");
			type.add("7");
			type.add("8");
			type.add("9");
			sEmpDeductDtl = empDeductDtlService.entryDateRng(entryDate, entryDate, type, this.index, this.limit,
					titaVo);
		}
		lEmpDeductDtlold = sEmpDeductDtl == null ? null : sEmpDeductDtl.getContent();
		if (lEmpDeductDtlold != null && lEmpDeductDtlold.size() != 0) {

			int lastMediaSeq = 0;
			int lastMediaDate = 0;
			String lastMediaKind = "";
			EmpDeductDtl t = new EmpDeductDtl();
			for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtlold) {
				OccursList occursList = new OccursList();
				CustMain tCustMain = new CustMain();
				if (tEmpDeductDtl.getMediaSeq() == 0) {
					lEmpDeductDtl.add(tEmpDeductDtl);
					continue;
				}

				this.info("t = " + tEmpDeductDtl.toString());
				if (tEmpDeductDtl.getMediaSeq() != lastMediaSeq || tEmpDeductDtl.getMediaDate() != lastMediaDate
						|| !lastMediaKind.equals(tEmpDeductDtl.getMediaKind())) {
					lEmpDeductDtl.add(tEmpDeductDtl);
					t = tEmpDeductDtl;
					lastMediaSeq = tEmpDeductDtl.getMediaSeq();
					lastMediaDate = tEmpDeductDtl.getMediaDate();
					lastMediaKind = tEmpDeductDtl.getMediaKind();
					this.info("t1 = " + t.toString());
				} else {
					if (tEmpDeductDtl.getFacmNo() != t.getFacmNo()) {
						t.setFacmNo(0);
						t.setBormNo(0);
					}
					if (tEmpDeductDtl.getBormNo() != t.getBormNo()) {
						t.setBormNo(0);
					}
					t.setTxAmt(t.getTxAmt().add(tEmpDeductDtl.getTxAmt()));// 實扣金額
					t.setRepayAmt(t.getRepayAmt().add(tEmpDeductDtl.getRepayAmt()));// 應扣金額
					t.setPrincipal(t.getPrincipal().add(tEmpDeductDtl.getPrincipal()));// 本金
					t.setInterest(t.getInterest().add(tEmpDeductDtl.getInterest()));// 利息
					t.setCurrPrinAmt(t.getCurrPrinAmt().add(tEmpDeductDtl.getCurrPrinAmt()));// 當期本金
					t.setCurrIntAmt(t.getCurrIntAmt().add(tEmpDeductDtl.getCurrIntAmt()));// 當期利息
					t.setSumOvpayAmt(t.getSumOvpayAmt().add(tEmpDeductDtl.getSumOvpayAmt()));// 累溢收
					if (tEmpDeductDtl.getIntStartDate() < t.getIntStartDate()) {
						t.setIntStartDate(tEmpDeductDtl.getIntStartDate());
					}
					if (tEmpDeductDtl.getIntEndDate() > t.getIntEndDate()) {
						t.setIntEndDate(tEmpDeductDtl.getIntEndDate());
					}
					this.info("t2 = " + t.toString());
				}
			}

			for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
				OccursList occursList = new OccursList();
				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(tEmpDeductDtl.getCustNo(), tEmpDeductDtl.getCustNo());

				occursList.putParam("OOEntryDate", tEmpDeductDtl.getEntryDate());
				occursList.putParam("OOPerfMonth", tEmpDeductDtl.getPerfMonth() - 191100);
				occursList.putParam("OOProcCode", tEmpDeductDtl.getProcCode());
				occursList.putParam("OOAcctCode", tEmpDeductDtl.getAcctCode());
				occursList.putParam("OORepayCode", tEmpDeductDtl.getRepayCode());
				occursList.putParam("OOAchRepayCode", tEmpDeductDtl.getAchRepayCode());
				occursList.putParam("OOCustNo", tEmpDeductDtl.getCustNo());
				occursList.putParam("OOFacmNo", tEmpDeductDtl.getFacmNo());
				occursList.putParam("OOBormNo", tEmpDeductDtl.getBormNo());
				occursList.putParam("OOTellerNo", tCustMain.getEmpNo());
				occursList.putParam("OOTellerId", tEmpDeductDtl.getCustId());
				occursList.putParam("OOTellerName", tCustMain.getCustName());
				occursList.putParam("OORepayAmt", tEmpDeductDtl.getRepayAmt());
				occursList.putParam("OOProcCode", tEmpDeductDtl.getProcCode());
				occursList.putParam("OOMediaDate", tEmpDeductDtl.getMediaDate());
				occursList.putParam("OOMediaKind", tEmpDeductDtl.getMediaKind());
				occursList.putParam("OOMediaSeq", tEmpDeductDtl.getMediaSeq());
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException("E0001", "L4951 查無資料");
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (sEmpDeductDtl != null && sEmpDeductDtl.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}