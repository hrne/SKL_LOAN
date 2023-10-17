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
import com.st1.itx.eum.ContentName;
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

	private int iFuncCode = 0;
	private String iFormNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6608 ");
		this.totaVo.init(titaVo);
		titaVo.keepOrgDataBase();// 保留原本記號

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iFormNo = titaVo.getParam("FormNo");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6608"); // 功能選擇錯誤
		}

		switch (iFuncCode) {
		case 1: // 新增

			// 連線環境(預設)
			insertData(titaVo);

			// 月報環境
			titaVo.setDataBaseOnMon();
			insertData(titaVo);

			// 日報環境
			titaVo.setDataBaseOnDay();
			insertData(titaVo);

			break;
		case 2: // 修改

			// 連線環境(預設)
			updateData(titaVo);

			// 月報環境
			titaVo.setDataBaseOnMon();
			updateData(titaVo);

			// 日報環境
			titaVo.setDataBaseOnDay();
			updateData(titaVo);

			break;
		case 4: // 刪除

			// 連線環境(預設)
			deleteData(titaVo);

			// 月報環境
			titaVo.setDataBaseOnMon();
			deleteData(titaVo);

			// 日報環境
			titaVo.setDataBaseOnDay();
			deleteData(titaVo);

			break;
		}

		titaVo.setDataBaseOnOrg();// 還原原本的環境
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdReport(CdReport mCdReport, TitaVo titaVo) throws LogicException {

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

		if (iFuncCode != 2) {
			mCdReport.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdReport.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdReport.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdReport.setLastUpdateEmpNo(titaVo.getTlrNo());

	}

	private void insertData(TitaVo titaVo) throws LogicException {

		CdReport tCdReport = new CdReport();
		// set value
		moveCdReport(tCdReport, titaVo);

		try {
			sCdReportService.insert(tCdReport, titaVo);
		} catch (DBException e) {
			if (e.getErrorId() == 2) {
				throw new LogicException(titaVo, "E0002", iFormNo); // 新增資料已存在
			} else {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	private void updateData(TitaVo titaVo) throws LogicException {

		CdReport tCdReport = sCdReportService.holdById(iFormNo);

		// set value
		moveCdReport(tCdReport, titaVo);

		if (tCdReport == null) {
			throw new LogicException(titaVo, "E0003", iFormNo); // 修改資料不存在
		}

		// datalog 寫在連線環境
		if (ContentName.onLine.equals(titaVo.getDataBase())) {
			CdReport tCdReport2 = (CdReport) dataLog.clone(tCdReport);
			dataLog.setEnv(titaVo, tCdReport2, tCdReport);
			dataLog.exec("修改報表代號對照檔");
		}

		try {
			tCdReport = sCdReportService.update2(tCdReport, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}
	}

	private void deleteData(TitaVo titaVo) throws LogicException {

		CdReport tCdReport = sCdReportService.findById(iFormNo, titaVo);

		if (tCdReport == null) {
			throw new LogicException(titaVo, "E0004", iFormNo); // 刪除資料不存在
		}

		// datalog 寫在連線環境
		if (ContentName.onLine.equals(titaVo.getDataBase())) {
			dataLog.setEnv(titaVo, tCdReport, tCdReport);
			dataLog.exec("刪除報表代號對照檔");
		}

		try {
			sCdReportService.delete(tCdReport, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
		}

	}

}
