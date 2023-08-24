package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcAcctCheck;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.AcMainId;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.AcAcctCheckService;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.DailyTavService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 轉換Patch AcMain 餘額 <BR>
 * 990催收款項－放款、 TAV暫收款-可抵繳、TCK暫收款－支票、T11債協暫收款－抵繳款、 T12前調暫收款－抵繳款、
 * T13更生暫收款－抵繳款、TMI暫收款－火險保費
 * 
 * @author st1
 *
 */
@Service("BST04")
@Scope("prototype")
public class BST04 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public AcAcctCheckService acAcctCheckService;

	@Autowired
	public AcMainService acMainService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Override
	/* 產生債協入帳明細 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BST04 run  ");
		String[] strAr = titaVo.getParam("Parm").split(",");
		int iAcDate = this.parse.stringToInteger(strAr[0]);
		this.info("iAcDate=" + iAcDate);
		Slice<AcAcctCheck> slAcAcctCheck = acAcctCheckService.findAcDate(iAcDate + 19110000, 0, Integer.MAX_VALUE,
				titaVo);

		for (AcAcctCheck t : slAcAcctCheck.getContent()) {
			// 990催收款項－放款 TAV暫收款-可抵繳 TCK暫收款－支票 T11債協暫收款－分配款T12前調暫收款－分配款 T13更生暫收款－分配款更新
			if (t.getAcctCode().equals("990") || t.getAcctCode().equals("TAV") || t.getAcctCode().equals("TCK")
					|| t.getAcctCode().equals("T11") || t.getAcctCode().equals("T12")
					|| t.getAcctCode().equals("T13") || t.getAcctCode().equals("TMI")) {
				updateAcMain(t, titaVo);
			}
		}

		return null;

	}

	private void updateAcMain(AcAcctCheck t, TitaVo titaVo) throws LogicException {
		CdAcCode tCdAcCode = cdAcCodeService.acCodeAcctFirst(t.getAcctCode(), titaVo);
		AcMainId tAcMainId = new AcMainId();
		tAcMainId.setAcBookCode("000");
		tAcMainId.setAcSubBookCode(t.getAcSubBookCode());
		tAcMainId.setBranchNo(t.getBranchNo());
		tAcMainId.setCurrencyCode(t.getCurrencyCode());
		tAcMainId.setAcNoCode(tCdAcCode.getAcNoCode());
		tAcMainId.setAcSubCode(tCdAcCode.getAcSubCode());
		tAcMainId.setAcDtlCode(tCdAcCode.getAcDtlCode());
		tAcMainId.setAcDate(t.getAcDate());
		AcMain tAcMain = acMainService.holdById(tAcMainId, titaVo); // holdById
		boolean isInsert = false;
		if (tAcMain == null) {
			tAcMain = new AcMain();
			isInsert = true;
			tAcMain.setAcMainId(tAcMainId);
		}
		tAcMain.setTdBal(t.getReceivableBal());
		tAcMain.setAcctCode(t.getAcctCode());

		// MonthEndYm // 平常日-> 0, 月底日資料 -> yyyymm,ex.202005
		if (tAcMainId.getAcDate() == txBuffer.getTxCom().getMfbsdy()) {
			tAcMain.setMonthEndYm(txBuffer.getTxCom().getMfbsdyf() / 100);
		} else {
			tAcMain.setMonthEndYm(0);
		}
		if (isInsert) {
			try {
				acMainService.insert(tAcMain, titaVo); // insert
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003", "AcMain insert " + tAcMainId + e.getErrorMsg());
			}
		} else {
			try {
				acMainService.update(tAcMain, titaVo); // insert
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003", "AcMaine update " + tAcMainId + e.getErrorMsg());
			}
		}

	}

}
