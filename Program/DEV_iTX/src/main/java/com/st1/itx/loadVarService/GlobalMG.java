package com.st1.itx.loadVarService;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.MGBuffer;
import com.st1.itx.dataVO.MgCurr;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxCurr;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.TxCurrService;
import com.st1.itx.util.date.DateUtil;

/**
 * GlobalMG
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("globalMG")
@Scope("prototype")
public class GlobalMG {
	@Autowired
	TxBizDateService txBizDateService;

	@Autowired
	TxCurrService txCurrService;

	@Autowired
	MGBuffer mgBuffer;

	@Autowired
	DateUtil dateUtil;

	@PostConstruct
	public void init() throws IOException, LogicException {
		this.findAndSetMGBuffer();
	}

	/**
	 * set MgBuffer
	 * 
	 * @throws LogicException LogicException
	 */
	private void findAndSetMGBuffer() throws LogicException {
		TxBizDate txBizDate = txBizDateService.findById("ONLINE");

		TxBizDate ntxBizDate = new TxBizDate();

		if (txBizDate != null) {
			// 本日
			mgBuffer.setTxBizDate(txBizDate);

			// 次日
			ntxBizDate.setDateCode("ONLINE");
			ntxBizDate.setLbsDyf(txBizDate.getTbsDyf());
			ntxBizDate.setTbsDyf(txBizDate.getNbsDyf());
			ntxBizDate.setDayOfWeek(dateUtil.getDayOfWeek(ntxBizDate.getTbsDyf()));

			int date = txBizDate.getTbsDyf();
			while (true) {
				dateUtil.setDate_1(date);
				dateUtil.setDays(1);
				date = dateUtil.getCalenderDay();
				if (!dateUtil.isHoliDay()) {
					ntxBizDate.setNbsDyf(date);
					while (true) {
						dateUtil.setDate_1(date);
						dateUtil.setDays(1);
						date = dateUtil.getCalenderDay();
						if (!dateUtil.isHoliDay()) {
							ntxBizDate.setNnbsDyf(date);
							break;
						}
					}
					break;
				}
			}

			dateUtil.init();
			dateUtil.setDate_1(ntxBizDate.getTbsDyf());
			date = (ntxBizDate.getTbsDyf() / 100 * 100) + dateUtil.getMonLimit();
			ntxBizDate.setTmnDyf(date);
			dateUtil.setMons(-1);
			date = dateUtil.getCalenderDay();
			dateUtil.setDate_1(date);
			date = (date / 100 * 100) + dateUtil.getMonLimit();
			ntxBizDate.setLmnDyf(date);

			dateUtil.init();
			dateUtil.setDate_1(ntxBizDate.getTbsDyf());
			date = (ntxBizDate.getTbsDyf() / 100 * 100) + dateUtil.getMonLimit();
			dateUtil.setDate_2(date);

			while (true) {
				if (!dateUtil.isHoliDay()) {
					ntxBizDate.setMfbsDyf(date);
					break;
				}
				dateUtil.setDate_1(date);
				dateUtil.setDays(-1);
				date = dateUtil.getCalenderDay();
			}

			ntxBizDate.setTbsDy(ntxBizDate.getTbsDyf() - 19110000);
			ntxBizDate.setNbsDy(ntxBizDate.getNbsDyf() - 19110000);
			ntxBizDate.setNnbsDy(ntxBizDate.getNnbsDyf() - 19110000);
			ntxBizDate.setLbsDy(ntxBizDate.getLbsDyf() - 19110000);
			ntxBizDate.setLmnDy(ntxBizDate.getLmnDyf() - 19110000);
			ntxBizDate.setTmnDy(ntxBizDate.getTmnDyf() - 19110000);
			ntxBizDate.setMfbsDy(ntxBizDate.getMfbsDyf() - 19110000);

			mgBuffer.setnTxBizDate(ntxBizDate);
		}

		// 幣別
		MgCurr mgCurr = new MgCurr();
		Slice<TxCurr> stxCurr = this.txCurrService.findAll(0, Integer.MAX_VALUE);
		List<TxCurr> txCurr = stxCurr == null ? null : stxCurr.getContent();
		if (txCurr != null && txCurr.size() > 0)
			for (TxCurr c : txCurr) {
				mgCurr.putParam(String.valueOf(c.getCurCd() < 10 ? "0" + c.getCurCd() : c.getCurCd()), c.getCurNm());
				if (c.getCurCd() < 10)
					mgCurr.putParam(String.valueOf(c.getCurCd()), c.getCurNm());
				mgCurr.putParam(c.getCurNm(), String.valueOf(c.getCurCd() < 10 ? "0" + c.getCurCd() : c.getCurCd()));
			}
		mgBuffer.setMgCurr(mgCurr);

		txBizDate = null;
		mgCurr = null;
		txCurr = null;
	}
}
