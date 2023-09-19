package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegFinAcct;
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.parse.Parse;;

@Service("L5983")
@Scope("prototype")
/**
 * L5983 結清試算
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L5983 extends TradeBuffer {

	@Autowired
	public NegMainService negMainService;
	@Autowired
	public NegFinShareService negFinShareService;
	@Autowired
	public NegAppr01Service negAppr01Service;
	@Autowired
	public NegFinAcctService negFinAcctService;
	@Autowired
	public NegCom negCom;

	@Autowired
	public Parse parse;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5983 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("CustId");
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iCaseSeq = parse.stringToInteger(titaVo.getParam("CaseSeq"));
		int iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate"));

		NegMain tNegMain = negMainService.findById(new NegMainId(iCustNo, iCaseSeq), titaVo);
		if (tNegMain == null) {
			throw new LogicException("E0001", "債務協商案件主檔");// 查無資料
		}
		BigDecimal tempAmt = BigDecimal.ZERO; // 暫收抵繳
		int diffdd = 0;
		int lastPayIntDate = negCom.getRepayDate(tNegMain.getNextPayDate(), -1, titaVo);// 上次繳息止日
		if (iEntryDate >= lastPayIntDate) {
			diffdd = negCom.diffday(lastPayIntDate, iEntryDate);// 計息區間:上次繳息止日~本次繳款日
		} else {
			diffdd = negCom.diffday(iEntryDate, lastPayIntDate);// 利息倒扣區間:本次繳款日~上次繳息止日
		}
		BigDecimal intAmt = tNegMain.getPrincipalBal().multiply(tNegMain.getIntRate())
				.divide(parse.stringToBigDecimal("100")).multiply(new BigDecimal(diffdd)).divide(new BigDecimal(365));
		tempAmt = tNegMain.getPrincipalBal().add(intAmt).subtract(tNegMain.getAccuOverAmt());
		BigDecimal revivAmt = tNegMain.getPrincipalBal().add(intAmt);
		String revivAmtX = df.format(revivAmt) + "(本金：" + df.format(tNegMain.getPrincipalBal()) + "利息："
				+ df.format(intAmt) + ")";
		this.totaVo.putParam("OCustId", iCustId);
		this.totaVo.putParam("OCustNo", tNegMain.getCustNo());
		this.totaVo.putParam("OCaseSeq", tNegMain.getCaseSeq());
		this.totaVo.putParam("OPrincipalBal", tNegMain.getPrincipalBal());
		this.totaVo.putParam("OAccuOverAmt", tNegMain.getAccuOverAmt());
		this.totaVo.putParam("ODueAmt", tNegMain.getDueAmt());
		this.totaVo.putParam("OIntRate", tNegMain.getIntRate());
		this.totaVo.putParam("OEntryDate", iEntryDate);
		this.totaVo.putParam("OIsMainFin", tNegMain.getIsMainFin());
		this.totaVo.putParam("OTempAmt", tempAmt);
		this.totaVo.putParam("OAccuOverAmt", tNegMain.getAccuOverAmt());
		this.totaVo.putParam("ORevivAmtX", revivAmtX.trim());

		Slice<NegFinShare> slNegFinShare = negFinShareService.findFinCodeAll(tNegMain.getCustNo(),
				tNegMain.getCaseSeq(), 0, Integer.MAX_VALUE, titaVo);

		if (slNegFinShare != null && slNegFinShare.getContent().size() > 0) {
			for (NegFinShare tNegFinShare : slNegFinShare.getContent()) {
				OccursList occursList = new OccursList();
				String FinItem = "";
				BigDecimal accuApprAmt = BigDecimal.ZERO; // 暫收抵繳
				NegFinAcct tNegFinAcct = negFinAcctService.findById(tNegFinShare.getFinCode(), titaVo);
				if (tNegFinAcct != null) {
					FinItem = tNegFinAcct.getFinItem();
				}
				// 抓該戶該機構最後一筆累計撥付金額
				NegAppr01 tNegAppr01 = negAppr01Service.findCustNoFinCodeFirst(tNegFinShare.getCustNo(),
						tNegFinShare.getCaseSeq(), tNegFinShare.getFinCode(), titaVo);
				if (tNegAppr01 != null) {
					accuApprAmt = tNegAppr01.getAccuApprAmt();
				}
				BigDecimal apprAmt = tempAmt.multiply(tNegFinShare.getAmtRatio());
				occursList.putParam("OOFinCode", tNegFinShare.getFinCode());// 債權機構
				occursList.putParam("OOFinItem", FinItem); // 機構名稱
				occursList.putParam("OOApprAmt", apprAmt);// 撥付金額
				occursList.putParam("OOAccuApprAmt", accuApprAmt);// 累計撥付金額
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}