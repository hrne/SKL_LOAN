package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * FuncCode=9,1 FormNo=x,10 FormName=x,80 Cycle=A,2 SendCode=A,1 Letter=A,1
 * Message=A,1 Email=A,1 UsageDesc=x,80 SignCode=A,1 Enable=X,1
 */

@Service("L6608")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6608 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdReportService sCdReportService;
	@Autowired
	private DateUtil dDateUtil;
	@Autowired
	private Parse parse;
	@Autowired
	private DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6608 ");
		this.totaVo.init(titaVo);
		titaVo.keepOrgDataBase();// 保留原本記號
		titaVo.setDataBaseOnLine();// 指定連線環境
		
		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iFormNo = titaVo.getParam("FormNo");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6608"); // 功能選擇錯誤
		}

		// 更新報表代號對照檔
		CdReport tCdReport = new CdReport();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdReport(tCdReport, iFuncCode, iFormNo, titaVo);
			try {
				this.info("1");
				sCdReportService.insert(tCdReport, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iFormNo); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdReport = sCdReportService.holdById(iFormNo);
			if (tCdReport == null) {
				throw new LogicException(titaVo, "E0003", iFormNo); // 修改資料不存在
			}
			CdReport tCdReport2 = (CdReport) dataLog.clone(tCdReport); ////
			try {
				moveCdReport(tCdReport, iFuncCode, iFormNo, titaVo);
				tCdReport = sCdReportService.update2(tCdReport, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdReport2, tCdReport); ////
			dataLog.exec("修改報表代號對照檔"); ////
			break;
		case 4: // 刪除
			tCdReport = sCdReportService.holdById(iFormNo);
			if (tCdReport != null) {
				try {
					sCdReportService.delete(tCdReport);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iFormNo); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tCdReport, tCdReport); ////
			dataLog.exec("刪除報表代號對照檔"); ////
			break;
		}
		this.info("3");

		titaVo.setDataBaseOnOrg();// 還原原本的環境
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdReport(CdReport mCdReport, int mFuncCode, String mFormNo, TitaVo titaVo) throws LogicException {

		mCdReport.setFormNo(titaVo.getParam("FormNo"));
		mCdReport.setFormName(titaVo.getParam("FormName"));
		mCdReport.setCycle(this.parse.stringToInteger((titaVo.getParam("Cycle"))));
		mCdReport.setSendCode(this.parse.stringToInteger((titaVo.getParam("SendCode"))));
		mCdReport.setLetter(this.parse.stringToInteger((titaVo.getParam("Letter"))));
		mCdReport.setMessage(this.parse.stringToInteger((titaVo.getParam("Message"))));
		mCdReport.setEmail(this.parse.stringToInteger((titaVo.getParam("Email"))));
		mCdReport.setUsageDesc(titaVo.getParam("UsageDesc"));
		mCdReport.setSignCode(this.parse.stringToInteger((titaVo.getParam("SignCode"))));
		mCdReport.setWatermarkFlag(this.parse.stringToInteger(titaVo.getParam("Watermark")));
		mCdReport.setEnable(titaVo.getParam("Enable"));
		mCdReport.setConfidentiality(titaVo.getParam("Confidentiality"));
		mCdReport.setApLogFlag(this.parse.stringToInteger(titaVo.getParam("ApLogFlag")));
		mCdReport.setGroupNo(titaVo.getParam("GroupNo"));

		mCdReport.setLetterFg(titaVo.getParam("LetterFg"));
		mCdReport.setMessageFg(titaVo.getParam("MessageFg"));
		mCdReport.setEmailFg(titaVo.getParam("EmailFg"));

		if (mFuncCode != 2) {
			mCdReport.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdReport.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdReport.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdReport.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
