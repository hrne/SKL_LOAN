package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * AcDate=9,7<br>
 * ResrvStndrd=9,14.2<br>
 * PosbleBorPsn=9,3.2<br>
 * PosbleBorAmt=9,14.2<br>
 * AlrdyBorAmt=9,14.2<br>
 * END=X,1<br>
 */

@Service("L5101")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5101 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnFundAplService innFundAplService;

	@Autowired
	public AcMainService acMainService;

	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5101 ");
		this.totaVo.init(titaVo);

		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		int acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		this.info("iFunctionCode : " + iFunctionCode);
		this.info("acDate : " + acDate);
		InnFundApl iInnFundApl = new InnFundApl();
		InnFundApl xInnFundApl = innFundAplService.findById(acDate, titaVo);

		titaVo.keepOrgDataBase();// 保留原本記號

		switch (iFunctionCode) {
		case 1:
			if (xInnFundApl != null) {
				throw new LogicException(titaVo, "E0005", "日期:" + titaVo.getParam("AcDate") + "已有相同資料");
			} else {
				iInnFundApl.setAcDate(acDate);
				iInnFundApl.setAlrdyBorAmt(parse.stringToBigDecimal(titaVo.getParam("AlrdyBorAmt")));
				iInnFundApl.setResrvStndrd(parse.stringToBigDecimal(titaVo.getParam("ResrvStndrd")));
				iInnFundApl.setPosbleBorPsn(parse.stringToBigDecimal(titaVo.getParam("PosbleBorPsn")));
				iInnFundApl.setPosbleBorAmt(parse.stringToBigDecimal(titaVo.getParam("PosbleBorAmt")));
				iInnFundApl.setStockHoldersEqt(parse.stringToBigDecimal(titaVo.getParam("StockHoldersEqt")));
				iInnFundApl.setAvailableFunds(parse.stringToBigDecimal(titaVo.getParam("AvailableFunds")));

				try {
					innFundAplService.insert(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L5101 InnFundApl insert " + e.getErrorMsg());
				}

				// 指定月報環境
				updateInnFundAplonMonth(iFunctionCode, iInnFundApl, titaVo);
			}
			break;
		case 2:
			if (xInnFundApl == null) {
				throw new LogicException(titaVo, "E0006", "日期" + titaVo.getParam("AcDate") + "資料不存在");
			} else {
				iInnFundApl = innFundAplService.holdById(acDate, titaVo);

				if (iInnFundApl == null) {
					throw new LogicException("EC001", "查無資料");
				}

				// 變更前
				InnFundApl BefInnFundApl = (InnFundApl) iDataLog.clone(iInnFundApl);

				iInnFundApl.setAlrdyBorAmt(parse.stringToBigDecimal(titaVo.getParam("AlrdyBorAmt")));
				iInnFundApl.setResrvStndrd(parse.stringToBigDecimal(titaVo.getParam("ResrvStndrd")));
				iInnFundApl.setPosbleBorPsn(parse.stringToBigDecimal(titaVo.getParam("PosbleBorPsn")));
				iInnFundApl.setPosbleBorAmt(parse.stringToBigDecimal(titaVo.getParam("PosbleBorAmt")));
				iInnFundApl.setStockHoldersEqt(parse.stringToBigDecimal(titaVo.getParam("StockHoldersEqt")));
				iInnFundApl.setAvailableFunds(parse.stringToBigDecimal(titaVo.getParam("AvailableFunds")));

				try {
					innFundAplService.update(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5101 InnFundApl update " + e.getErrorMsg());
				}

				// 紀錄變更前變更後
				iDataLog.setEnv(titaVo, BefInnFundApl, iInnFundApl);
				iDataLog.exec("修改資金運用概況");

				// 指定月報環境
				updateInnFundAplonMonth(iFunctionCode, iInnFundApl, titaVo);
			}
			break;
		case 4:
			if (xInnFundApl == null) {
				throw new LogicException(titaVo, "E0006", "日期" + titaVo.getParam("AcDate") + "資料不存在");
			} else {
				iInnFundApl = innFundAplService.holdById(acDate, titaVo);

				if (iInnFundApl == null) {
					throw new LogicException("EC001", "查無資料");
				}

				// 變更前
				InnFundApl BefInnFundApl = (InnFundApl) iDataLog.clone(iInnFundApl);

				try {
					innFundAplService.delete(xInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "L5101 InnFundApl delete " + e.getErrorMsg());
				}

				// 紀錄變更前變更後
				iDataLog.setEnv(titaVo, BefInnFundApl, iInnFundApl);
				iDataLog.exec("刪除資金運用概況");

				// 指定月報環境
				updateInnFundAplonMonth(iFunctionCode, iInnFundApl, titaVo);

			}
			break;
		case 5:
			break;
		}

		titaVo.setDataBaseOnOrg();// 還原原本的環境

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void updateInnFundAplonMonth(int iFunctionCode, InnFundApl iInnFundApl, TitaVo titaVo)
			throws LogicException {
		this.info("L5101 updateInnFundAplonMonth  ... ");
		titaVo.setDataBaseOnMon();// 指定月報環境
		InnFundApl tInnFundApl = innFundAplService.findById(iInnFundApl.getAcDate() + 19110000, titaVo);
		if (iFunctionCode == 4) {
			if (tInnFundApl != null) {
				try {
					innFundAplService.delete(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "InnFundApl " + e.getErrorMsg());
				}
			}
		} else {
			if (tInnFundApl == null) {
				try {
					innFundAplService.insert(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "InnFundApl " + e.getErrorMsg());
				}
			} else {
				try {
					innFundAplService.update(iInnFundApl, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "InnFundApl " + e.getErrorMsg());
				}
			}
		}
	}

	/**
	 * 更新資金運用概況檔已放款金額 call by L9130
	 * 
	 * @param AcDate 會計日期
	 * @param titaVo TitaVo
	 * @throws LogicException ....
	 */
	public void updateInnFundAplLoanBal(int acDate, TitaVo titaVo) throws LogicException {
		this.info("L5101 updateInnFundAplLoanBal  ... ");

		Slice<AcMain> slAcMain = acMainService.acmainAcDateEq(acDate + 19110000, this.index, Integer.MAX_VALUE);
		BigDecimal loanBal = BigDecimal.ZERO;
		if (slAcMain != null) {
			for (AcMain t : slAcMain.getContent()) {

				if ("310".equals(t.getAcctCode()) || "320".equals(t.getAcctCode()) || "330".equals(t.getAcctCode())
						|| "340".equals(t.getAcctCode()) || "990".equals(t.getAcctCode())) {
					loanBal = loanBal.add(t.getTdBal());

				}
			}
		}
		InnFundApl tInnFundApl = innFundAplService.findById(acDate + 19110000, titaVo);
		boolean isInsert = false;
		if (tInnFundApl == null) {
			isInsert = true;
			tInnFundApl = new InnFundApl();
			tInnFundApl.setAcDate(acDate);
		}
		tInnFundApl.setAlrdyBorAmt(loanBal);

		if (isInsert) {
			try {
				innFundAplService.insert(tInnFundApl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "InnFundApl " + e.getErrorMsg());
			}
		} else {
			try {
				innFundAplService.update(tInnFundApl, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "InnFundApl " + e.getErrorMsg());
			}

		}
	}

}