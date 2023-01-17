package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.AcMainCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6880")
@Scope("prototype")
/**
 * 更改連線日期
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L6880 extends TradeBuffer {
	@Autowired
	private DateUtil dDateUtil;

	@Autowired
	private Parse parse;

	@Autowired
	private TxRecordService txRecordService;

	@Autowired
	private TxTellerService txTellerService;

	@Autowired
	private TxBizDateService sTxBizDateService;

	@Autowired
	private SystemParasService systemParasService;

	@Autowired
	private AcMainService acMainService;

	@Autowired
	private AcCloseService sAcCloseService;

	@Autowired
	private AcMainCom acMainCom;
	String iEntday = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6880 ");
		this.totaVo.init(titaVo);
		boolean isChangeOnlineDate = true;
		iEntday = "";
		// L6880->正常換日 ， LC899->強制換日
		if ("L6880".equals(titaVo.getTxcd())) {
			// tita的批次日期
			AcClose tAcClose = new AcClose();
			AcCloseId tAcCloseId = new AcCloseId();
			tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			tAcCloseId.setBranchNo(titaVo.getAcbrNo());
			tAcCloseId.setSecNo("09"); // 業務類別: 09-放款
			tAcClose = sAcCloseService.findById(tAcCloseId);
			if (tAcClose == null) {
				throw new LogicException(titaVo, "E0003", "關帳狀態(09:放款)"); // 修改資料不存在
			}
			if (tAcClose.getClsFg() != 4) {
				throw new LogicException(titaVo, "E0015", "關帳狀態(09:放款)<>4-關帳" + tAcClose.getClsFg()); // 檢查錯誤
			}
			iEntday = titaVo.get("iEntday").trim();
			// 連線日期應為批次系統日期的下營業日
			TxBizDate batchTxBizDate = sTxBizDateService.holdById("BATCH", titaVo);
			if (parse.stringToInteger(iEntday) != batchTxBizDate.getNbsDy()) {
				throw new LogicException(titaVo, "E0015", "須執行L6870-夜間批次"); // E0010 功能選擇錯誤
			}
		} else {
			// Parm :1.系統換日異常，強制換至下營業日
			// Parm :2.測試時系統日期強制換至指定日期
			if ("1".equals(titaVo.getParam("Parm"))) {
				iEntday = "" + this.txBuffer.getTxBizDate().getTbsDy();
				this.totaVo.setWarnMsg("系統換日異常，強制換至下營業日");
			} else if ("2".equals(titaVo.getParam("Parm").substring(0, 1))) {
				isChangeOnlineDate = false;
				iEntday = titaVo.getParam("Parm").substring(1, 8);
				this.totaVo.setWarnMsg("測試時強制批次日期換至指定日期" + iEntday);
				// 2-連線日期 -> 批次日期
				acMainCom.setTxBuffer(this.txBuffer);
				proc(titaVo, "BATCH", iEntday);
				// 往前跳開批次日期需過總帳(測試時)，連線日期 -> 批次日期
				if (parse.stringToInteger(iEntday) > this.txBuffer.getTxBizDate().getTbsDy()) {
					Slice<AcMain> slAcMain = acMainService.acmainAcDateEq(this.txBuffer.getTxBizDate().getTbsDyf(),
							this.index, Integer.MAX_VALUE);
					List<AcMain> lAcMain = slAcMain == null ? null : slAcMain.getContent();
					if (lAcMain != null) {
						acMainCom.changeDate(this.txBuffer.getTxBizDate().getTbsDy(), parse.stringToInteger(iEntday),
								lAcMain, titaVo);
					}
				}
			} else {
				throw new LogicException(titaVo, "E0015", "參數：1/2 ，1-換至下營業日 2-換至指定日期"); // 檢查錯誤
			}
		}
		// 連線日期應為批次系統日期的下營業日
		TxBizDate batchTxBizDate = sTxBizDateService.holdById("BATCH", titaVo);
		if (parse.stringToInteger(iEntday) != batchTxBizDate.getNbsDy()) {
			throw new LogicException(titaVo, "E0015", "須執行L6870-夜間批次"); // E0010 功能選擇錯誤
		}

		// 更改連線日期
		if (isChangeOnlineDate) {
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

			this.chgTxnoTo0(titaVo);

			// insertAcClose
			this.insertAcClose(titaVo);

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

	public void insertAcClose(TitaVo titaVo) throws LogicException {
		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(parse.stringToInteger(iEntday));
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 09-放款
		tAcClose = sAcCloseService.findById(tAcCloseId);
		if (tAcClose == null) {
			tAcClose = new AcClose();
			tAcClose.setAcCloseId(tAcCloseId);
			tAcClose.setClsFg(0);
			tAcClose.setBatNo(1);
			tAcClose.setClsNo(0);
			try {
				sAcCloseService.insert(tAcClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "Acclose insert " + e.getErrorMsg());
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

	private void chgTxnoTo0(TitaVo titaVo) throws LogicException {
		Slice<TxTeller> txTellerSlice = txTellerService.findAll(0, Integer.MAX_VALUE);
		List<TxTeller> txTellerLi = txTellerSlice.hasContent() ? txTellerSlice.getContent() : null;

		if (Objects.isNull(txTellerLi))
			return;

		for (TxTeller te : txTellerLi)
			if (te.getTlrNo().trim().equals(titaVo.getTlrNo().trim())) {
				TxRecord txRecord = txRecordService.findEntdyFirst(this.getTxBuffer().getTxBizDate().getTbsDyf(),
						titaVo.getTlrNo(), "00");
				te.setTxtNo(Objects.isNull(txRecord) ? 1 : parse.stringToInteger(txRecord.getTxSeq()) + 1);
			} else {
				TxRecord txRecord = txRecordService.findEntdyFirst(this.getTxBuffer().getTxBizDate().getTbsDyf(),
						titaVo.getTlrNo(), "00");
				te.setTxtNo(Objects.isNull(txRecord) ? 0 : parse.stringToInteger(txRecord.getTxSeq()));
			}
		try {
			txTellerService.updateAll(txTellerLi, titaVo);
			titaVo.putParam(ContentName.txtno, FormatUtil.pad9("1", 8));
		} catch (DBException e) {
			this.error("TxTeller update all TxNo False!!!");
		}
	}

}