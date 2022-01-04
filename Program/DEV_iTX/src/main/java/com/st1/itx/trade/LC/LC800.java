package com.st1.itx.trade.LC;

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
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.AcMainCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("LC800")
@Scope("prototype")
/**
 * 更改連線日期
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC800 extends TradeBuffer {

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	@Autowired
	public TxBizDateService sTxBizDateService;
	@Autowired
	public SystemParasService systemParasService;
	@Autowired
	public AcMainService acMainService;
	@Autowired
	public AcMainCom acMainCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC800 ");
		this.totaVo.init(titaVo);

//		改getForTxBizDate
		String iType = titaVo.get("iType").trim();
		String iEntday = titaVo.get("iEntday").trim();
		acMainCom.setTxBuffer(this.txBuffer);

		if ("2".equals(iType)) {
			proc(titaVo, "BATCH", iEntday);

			// 往前跳開批次日期需過總帳(測試時)，連線日期 -> 批次日期
			if (parse.stringToInteger(iEntday) > this.txBuffer.getTxBizDate().getTbsDy()) {
				Slice<AcMain> slAcMain = acMainService.acmainAcDateEq(this.txBuffer.getTxBizDate().getTbsDyf(), this.index, Integer.MAX_VALUE);
				List<AcMain> lAcMain = slAcMain == null ? null : slAcMain.getContent();
				if (lAcMain != null) {
					acMainCom.changeDate(this.txBuffer.getTxBizDate().getTbsDy(), parse.stringToInteger(iEntday), lAcMain, titaVo);
				}
			}
		} else {
			// 連線日期應為批次系統日期的下營業日
			TxBizDate batchTxBizDate = sTxBizDateService.holdById("BATCH", titaVo);
			if (parse.stringToInteger(iEntday) != batchTxBizDate.getNbsDy()) {
				throw new LogicException(titaVo, "E0010", "須執行LC700-夜間批次"); // E0010 功能選擇錯誤
			}

			// 更改連線日期
			TxBizDate tTxBizDate = proc(titaVo, "ONLINE", iEntday);
			proc(titaVo, "NONLINE", String.valueOf(tTxBizDate.getNbsDy()));
			proc(titaVo, "N2ONLINE", String.valueOf(tTxBizDate.getNnbsDy()));

			// 更改業績日期
			perfDate(titaVo, parse.stringToInteger(iEntday));

			// 更新txBuffer
			titaVo.putParam(ContentName.entdy, iEntday);
			this.txBuffer.init(titaVo);

			// 自動執行：日始作業(BS001)
			// 直接非同步 ( 較簡易 )
			this.info("LC800 Call BS001 ...");
			MySpring.newTask("BS001", this.txBuffer, titaVo);
			this.info("LC800 Call BS001 Finished.");

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 更改業績日期 SystemParas.PerfDate
	private void perfDate(TitaVo titaVo, int perfDate) throws LogicException {
		// 業績日期 SystemParas.PerfDate
		SystemParas tSystemParas = systemParasService.holdById("LN");
		if (tSystemParas.getPerfDate() != perfDate) {
			tSystemParas.setPerfDate(perfDate);
			try {
				systemParasService.update(tSystemParas);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "BS050 update SystemParas " + tSystemParas + e.getErrorMsg());
			}
		}

	}

	public TxBizDate proc(TitaVo titaVo, String datecode, String date) throws LogicException {
		this.info("LC800 proc ... ");

		TxBizDate tTxBizDate = sTxBizDateService.holdById(datecode, titaVo);

		dDateUtil.init();

		boolean newfg = false;
		if (tTxBizDate == null) {
			newfg = true;
			tTxBizDate = new TxBizDate();
		}
		this.info("LC800 newfg = " + newfg);

		dDateUtil.setDate_1(date);

		TxBizDate tTxBizDate2 = dDateUtil.getForTxBizDate();

		this.info("TxBizDate = " + tTxBizDate2.toString());

		tTxBizDate.setDateCode(datecode);
		tTxBizDate.setDayOfWeek(tTxBizDate2.getDayOfWeek());
		tTxBizDate.setTbsDy(tTxBizDate2.getTbsDy());
		tTxBizDate.setNbsDy(tTxBizDate2.getNbsDy());
		tTxBizDate.setNnbsDy(tTxBizDate2.getNnbsDy());
		tTxBizDate.setLbsDy(tTxBizDate2.getLbsDy());
		tTxBizDate.setLmnDy(tTxBizDate2.getLmnDy());
		tTxBizDate.setTmnDy(tTxBizDate2.getTmnDy());
		tTxBizDate.setMfbsDy(tTxBizDate2.getMfbsDy());
		tTxBizDate.setTbsDyf(tTxBizDate2.getTbsDyf());
		tTxBizDate.setNbsDyf(tTxBizDate2.getNbsDyf());
		tTxBizDate.setNnbsDyf(tTxBizDate2.getNnbsDyf());
		tTxBizDate.setLbsDyf(tTxBizDate2.getLbsDyf());
		tTxBizDate.setLmnDyf(tTxBizDate2.getLmnDyf());
		tTxBizDate.setTmnDyf(tTxBizDate2.getTmnDyf());
		tTxBizDate.setMfbsDyf(tTxBizDate2.getMfbsDyf());

		try {
			if (newfg) {
				this.info("LC800 insert this = " + tTxBizDate.toString());
				sTxBizDateService.insert(tTxBizDate, titaVo);
			} else {
				this.info("LC800 update this = " + tTxBizDate.toString());
				sTxBizDateService.update(tTxBizDate, titaVo);
			}
		} catch (DBException e) {
			if (newfg) {
				throw new LogicException(titaVo, "EC002", "系統日期檔(TxBizDate)=" + datecode + "/" + e.getMessage());
			} else {
				throw new LogicException(titaVo, "EC003", "系統日期檔(TxBizDate)=" + datecode + "/" + e.getMessage());
			}
		}

		this.info("LC800 proc finished.");
		return tTxBizDate;
	}

}