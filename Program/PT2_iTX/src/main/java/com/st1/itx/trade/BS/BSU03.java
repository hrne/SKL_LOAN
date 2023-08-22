package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.springjpa.cm.BSU03ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 重新轉換業績資料，更新業績明細檔<BR>
 * 
 * @author Lai
 * @version 1.0.0
 */
@Component("BSU03")
@Scope("prototype")
public class BSU03 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	@Autowired
	PfItDetailService pfItDetailService;

	@Autowired
	public CdWorkMonthService cdWorkMonthService;

	@Autowired
	public BSU03ServiceImpl bsu03ServiceImpl;

	@Autowired
	public WebClient webClient;

	private List<Map<String, String>> adjustList = null;
	private int iStartDate = 0;
	private int iCustNoS = 0;
	private int iCustNoE = 0;
	private ArrayList<PfItDetail> lPfItDetailAdjust = new ArrayList<PfItDetail>();
	private ArrayList<PfItDetail> lPfItDetail = new ArrayList<PfItDetail>();
	private ArrayList<PfItDetail> lPfItDetailUpdate = new ArrayList<PfItDetail>();
	private BigDecimal drawdownAmt = BigDecimal.ZERO;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BSU03 ......");
		String[] strAr = titaVo.getParam("Parm").split(",");
		List<String> strList = Arrays.asList(strAr);
		if (strList.size() < 3) {
			throw new LogicException(titaVo, "E0000", "參數：EX.1101229,1,9999999( 業績起日, 起戶號, 止戶號)");
		}
		iStartDate = this.parse.stringToInteger(strAr[0]); // 業績起日
		iCustNoS = this.parse.stringToInteger(strAr[1]); // 起戶號
		iCustNoE = this.parse.stringToInteger(strAr[2]); // 止戶號
		loadPfItDetail(titaVo);
		updatePfItDetail(titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", "已完成更新業績明細檔", titaVo);

		this.batchTransaction.commit();

		this.info(" BSU03 END");

		// end
		return null;
	}

	private void loadPfItDetail(TitaVo titaVo) throws LogicException {
		try {
			adjustList = bsu03ServiceImpl.findAdjustList(iStartDate + 19110000, titaVo);
		} catch (Exception e) {
			this.info("noToDoList" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (adjustList == null) {
			return;
		}
		for (Map<String, String> d : adjustList) {
			int custNo = parse.stringToInteger(d.get("CustNo"));
			if (custNo >= iCustNoS && custNo <= iCustNoE) {
				PfItDetail t = new PfItDetail();
				t.setCustNo(parse.stringToInteger(d.get("CustNo")));
				t.setFacmNo(parse.stringToInteger(d.get("FacmNo")));
				t.setCntingCode(d.get("CntingCode"));
				t.setDrawdownAmt(parse.stringToBigDecimal(d.get("DrawdownAmt")));
				t.setPerfEqAmt(parse.stringToBigDecimal(d.get("PerfEqAmt")));
				t.setPerfReward(parse.stringToBigDecimal(d.get("PerfReward")));
				t.setPerfAmt(parse.stringToBigDecimal(d.get("PerfAmt")));
				lPfItDetailAdjust.add(t);
			}
		}
		// 清除介紹人業績資料(除計件代碼已調整或已調整外)
		Slice<PfItDetail> slItDetail = pfItDetailService.findByPerfDate(iStartDate + 19110000, 99991231, 0,
				Integer.MAX_VALUE, titaVo);
		if (slItDetail != null) {
			for (PfItDetail it : slItDetail.getContent()) {
				if (it.getCustNo() >= iCustNoS && it.getCustNo() <= iCustNoE && it.getRepayType() == 0) {
					it.setCntingCode(""); // 是否計件
					it.setPerfEqAmt(BigDecimal.ZERO); // 換算業績
					it.setPerfReward(BigDecimal.ZERO); // 業務報酬
					it.setPerfAmt(BigDecimal.ZERO); // 業績金額
					lPfItDetail.add(it);
				}
			}
		}
	}

	private void updatePfItDetail(TitaVo titaVo) throws LogicException {

		int custNo = 0;
		int facmNo = 0;
		for (PfItDetail it : lPfItDetail) {
			if (custNo == 0) {
				custNo = it.getCustNo();
				facmNo = it.getFacmNo();
			}
			if (it.getCustNo() != custNo || it.getFacmNo() != facmNo) {
				if (lPfItDetailUpdate.size() > 0) {
					this.info("not update size=" + lPfItDetailUpdate.size());
					lPfItDetailUpdate = new ArrayList<PfItDetail>();
					this.drawdownAmt = BigDecimal.ZERO;
				}
			}
			if (checkPf(it, titaVo)) {
				this.info("update" + it.toString());
			} else {
				this.info("add" + it.toString());
			}
			custNo = it.getCustNo();
			facmNo = it.getFacmNo();
		}

		if (lPfItDetail.size() > 0) {
			try {
				pfItDetailService.updateAll(lPfItDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "PfItDetail update " + e.getErrorMsg());
			}
		}

		for (PfItDetail it : lPfItDetail) {
			if (it.getCntingCode().isEmpty()) {
				this.info("not update" + it.toString());
			}
		}

	}

	private boolean checkPf(PfItDetail it, TitaVo titaVo) throws LogicException {
		boolean isUpdate = false;
		lPfItDetailUpdate.add(it);
		this.drawdownAmt = this.drawdownAmt.add(it.getDrawdownAmt());
		for (PfItDetail t : lPfItDetailAdjust) {
			if (t.getCustNo() == it.getCustNo() && t.getFacmNo() == it.getFacmNo()) {
				if (this.drawdownAmt.compareTo(t.getDrawdownAmt()) == 0) {
					isUpdate = true;
					computePf(t, lPfItDetailUpdate, titaVo);
					lPfItDetailUpdate = new ArrayList<PfItDetail>();
					this.drawdownAmt = BigDecimal.ZERO;
				}
			}
		}

		return isUpdate;
	}

	private void computePf(PfItDetail t, ArrayList<PfItDetail> lPfItDetailUpate, TitaVo titaVo) throws LogicException {
		this.info("computePf ... this.drawdownAmt=" + this.drawdownAmt + ", t=" + t.toString());
		for (PfItDetail it : lPfItDetailUpate) {
			it.setCntingCode(t.getCntingCode()); // 是否計件
			if (it.getDrawdownAmt().compareTo(t.getDrawdownAmt()) == 0) {
				it.setPerfEqAmt(t.getPerfEqAmt()); // 換算業績
				it.setPerfReward(t.getPerfReward()); // 業務報酬
				it.setPerfAmt(t.getPerfAmt()); // 業績金額
				t.setDrawdownAmt(BigDecimal.ZERO); // 撥款金額
				t.setPerfEqAmt(BigDecimal.ZERO); // 換算業績
				t.setPerfReward(BigDecimal.ZERO); // 業務報酬
				t.setPerfAmt(BigDecimal.ZERO); // 業績金額
			} else {
				BigDecimal rate = it.getDrawdownAmt().divide(this.drawdownAmt, 5, RoundingMode.HALF_UP);
				it.setPerfEqAmt(t.getPerfEqAmt().multiply(rate).setScale(0, RoundingMode.HALF_UP));
				it.setPerfReward(t.getPerfReward().multiply(rate).setScale(0, RoundingMode.HALF_UP));
				it.setPerfAmt(t.getPerfAmt().multiply(rate).setScale(0, RoundingMode.HALF_UP));
				t.setDrawdownAmt(t.getDrawdownAmt().subtract(it.getDrawdownAmt()));
				t.setPerfEqAmt(t.getPerfEqAmt().subtract(it.getPerfEqAmt()));
				t.setPerfReward(t.getPerfReward().subtract(it.getPerfReward()));
				t.setPerfAmt(t.getPerfAmt().subtract(it.getPerfAmt()));
			}
			this.info("computePf it=" + it.toString() + ", t=" + t.toString());
		}
	}
}