package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Service("L6870")
@Scope("prototype")
/**
 * 發動每日夜間批次
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L6870 extends TradeBuffer {
	@Autowired
	private AcCloseService sAcCloseService;
	@Autowired
	private TxToDoMainService sTxToDoMainService;
	@Autowired
	private TxBizDateService sTxBizDateService;
	@Autowired
	private DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6870 ");
		this.totaVo.init(titaVo);
		String iEntday = "";
		// tita的批次日期
		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();

		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 09-放款
		tAcClose = sAcCloseService.holdById(tAcCloseId);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0003", "關帳狀態(09:放款)"); // 修改資料不存在
		}
		if ("L6870".equals(titaVo.getTxcd())) {
			iEntday = titaVo.get("iEntday").trim();
			switch (tAcClose.getClsFg()) {
			case 0: // 0:開帳
				throw new LogicException(titaVo, "E0015", "放款業務未關帳"); // 檢查錯誤
			case 1: // 1:關帳
				break;
			case 2: // 2:關帳取消
				throw new LogicException(titaVo, "E0015", "放款業務未關帳"); // 檢查錯誤
			case 3: // 3:夜間批次執行中
				throw new LogicException(titaVo, "E0015", "夜間批次執行中"); // 檢查錯誤
			case 4: // 4.夜間批次執行完畢
				throw new LogicException(titaVo, "E0015", "夜間批次已執行完畢"); // 檢查錯誤
			}
			TxToDoMain tTxToDoMain = sTxToDoMainService.findById("L7400", titaVo);
			if (tTxToDoMain != null && tTxToDoMain.getUnProcessCnt() > 0) {
				throw new LogicException(titaVo, "E0015", "L7400-總帳傳票資料傳輸，須執行完畢 ，才可進行夜間批次作業"); // 檢查錯誤
			}
			tAcClose.setClsFg(3); // 3:夜間批次執行中
		} else {
			iEntday = "" + this.txBuffer.getTxBizDate().getTbsDy();
			// Parm :1 設定為關帳
			// Parm :3 設定為夜間批次開始
			// Parm :4.設定為夜間批次結束
			if ("1".equals(titaVo.getParam("Parm"))) {
				this.totaVo.setWarnMsg("設定為關帳");
				tAcClose.setClsFg(1); // 1:關帳
			} else if ("3".equals(titaVo.getParam("Parm"))) {
				this.totaVo.setWarnMsg("設定為夜間批次開始");
				tAcClose.setClsFg(3); // 3:夜間批次執行中
			} else if ("4".equals(titaVo.getParam("Parm"))) {
				this.totaVo.setWarnMsg("設定為夜間批次結束");
				tAcClose.setClsFg(4); // 4:夜間批次執行完畢
			} else {
				throw new LogicException(titaVo, "E0015", "參數：1/2 ，1-強制啟動 2-強制結束"); // 檢查錯誤
			}
		}

		// update
		try {
			sAcCloseService.update(tAcClose);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "更新關帳狀態(09:放款)"); // 更新資料時，發生錯誤
		}

		this.info("L6870 iEntday = " + iEntday);
		// 更新批次日期檔
		proc(titaVo, "BATCH", iEntday);

		this.info("L6870 每日夜間批次 eodFlow");

		if (tAcClose.getClsFg() == 3) {
			// 每日夜間批次程式，參考文件.\itxConfig\spring\batch\eodFlow.xml
			// EOD : End Of Day
			titaVo.setBatchJobId("eodFlow");
		}
		this.info("L6870 end.");

		this.addList(this.totaVo);
		return this.sendList();
	}

	private TxBizDate proc(TitaVo titaVo, String datecode, String date) throws LogicException {
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