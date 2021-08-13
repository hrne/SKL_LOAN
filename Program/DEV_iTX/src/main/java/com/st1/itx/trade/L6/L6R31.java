package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R31FormNo=X,1
 */
@Service("L6R31") // 尋找報表代號對照檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R31 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R31.class);

	/* DB服務注入 */
	@Autowired
	public CdReportService sCdReportService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R31 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimFormNo = titaVo.getParam("RimL6R31FormNo");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R31"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R31"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdReport(new CdReport());

		// 查詢報表代號對照檔
		CdReport tCdReport = sCdReportService.findById(iRimFormNo, titaVo);

		/* 如有找到資料 */
		if (tCdReport != null) {
			if (iRimTxCode.equals("L6608") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimFormNo); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdReport(tCdReport);
			}
		} else {
			if (iRimTxCode.equals("L6608") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "報表代號對照檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 報表代號對照檔
	private void moveTotaCdReport(CdReport mCdReport) throws LogicException {

		this.totaVo.putParam("L6R31FormNo", mCdReport.getFormNo());
		this.totaVo.putParam("L6R31FormName", mCdReport.getFormName());
		this.totaVo.putParam("L6R31Cycle", mCdReport.getCycle());
		this.totaVo.putParam("L6R31SendCode", mCdReport.getSendCode());
		this.totaVo.putParam("L6R31Letter", mCdReport.getLetter());
		this.totaVo.putParam("L6R31Message", mCdReport.getMessage());
		this.totaVo.putParam("L6R31Email", mCdReport.getEmail());
		this.totaVo.putParam("L6R31SignCode", mCdReport.getSignCode());
		this.totaVo.putParam("L6R31UsageDesc", mCdReport.getUsageDesc());
		this.totaVo.putParam("L6R31Enable", mCdReport.getEnable());

	}

}