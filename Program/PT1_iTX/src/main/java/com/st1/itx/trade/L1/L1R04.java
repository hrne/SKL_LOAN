package com.st1.itx.trade.L1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.springjpa.cm.L1R04ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1R04")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R04 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	/* DB服務注入 */
	@Autowired
	public CustNoticeService sCustNoticeService;
	/* DB服務注入 */
	@Autowired
	public CdReportService sCdReportService;
	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	L1R04ServiceImpl l1R04ServiceImpl;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R04 ");
		this.totaVo.init(titaVo);

		try {

			List<Map<String, String>> dList = l1R04ServiceImpl.FindData(titaVo);

			int applyDt = 0;

			if (dList != null && dList.size() != 0) {
				int i = 0;
				for (Map<String, String> dVo : dList) {

					if (applyDt == 0) {
						int applyDt2 = parse.stringToInteger(dVo.get("ApplyDate"));
						if (applyDt2 > 19110000) {
							applyDt2 -= 19110000;
							applyDt = applyDt2;
						}
					}
					
					i++;
					this.totaVo.putParam("L1r04FormNo" + i, dVo.get("FormNo"));
					this.totaVo.putParam("L1r04FormName" + i, dVo.get("FormName"));
					this.totaVo.putParam("L1r04SendCode" + i, dVo.get("SendCode"));
					this.totaVo.putParam("L1r04PaperFg" + i, dVo.get("LetterFg"));
					this.totaVo.putParam("L1r04MsgFg" + i, dVo.get("MessageFg"));
					this.totaVo.putParam("L1r04EMailFg" + i, dVo.get("EmailFg"));
					if (!"N".equals(dVo.get("PaperNotice").trim())) {
						this.totaVo.putParam("L1r04Paper" + i, " ");
					} else {
						this.totaVo.putParam("L1r04Paper" + i, "Y");
					}
					if (!"N".equals(dVo.get("MsgNotice").trim())) {
						this.totaVo.putParam("L1r04Msg" + i, " ");
					} else {
						this.totaVo.putParam("L1r04Msg" + i, "Y");
					}
					if (!"N".equals(dVo.get("EmailNotice").trim())) {
						this.totaVo.putParam("L1r04EMail" + i, " ");
					} else {
						this.totaVo.putParam("L1r04EMail" + i, "Y");
					}
				}
			}
			this.totaVo.putParam("L1r04ApplyDt", applyDt);
		} catch (LogicException e) {
			throw e;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException(titaVo, "E0000", errors.toString());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public ArrayList<TotaVo> run2(TitaVo titaVo) throws LogicException {
		this.info("active L1R04 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("RimFacmNo"));
		Slice<CustNotice> iCustNotice = null;

		iCustNotice = sCustNoticeService.facmNoEq(iCustNo, iFacmNo, iFacmNo, this.index, this.limit, titaVo);

		Slice<CdReport> slCdReport1;
		Slice<CdReport> slCdReport2;
		ArrayList<CdReport> lCdReport1 = new ArrayList<CdReport>();
		ArrayList<CdReport> lCdReport2 = new ArrayList<CdReport>();
		slCdReport1 = sCdReportService.formNoLike("L9" + "%", 0, Integer.MAX_VALUE, titaVo);
		lCdReport1 = slCdReport1 == null ? null : new ArrayList<CdReport>(slCdReport1.getContent());
		slCdReport2 = sCdReportService.formNoLike("L4" + "%", 0, Integer.MAX_VALUE, titaVo);
		lCdReport2 = slCdReport2 == null ? null : new ArrayList<CdReport>(slCdReport2.getContent());
		ArrayList<String> cdFormNo = new ArrayList<>();
		if (lCdReport1 != null) {
			for (CdReport tmpCdReport1 : lCdReport1) {
				lCdReport2.add(tmpCdReport1);
			}
		}
		for (CdReport xCdReport : lCdReport2) {
			if (xCdReport.getSendCode() != 0) {
				cdFormNo.add(xCdReport.getFormNo());
			}
		}
		this.info("全部的報表代號=" + cdFormNo.toString());
		int j = 1;
		this.totaVo.putParam("L1r04ApplyDt", iCustNotice.getContent().get(0).getApplyDate());
		// 回傳舊有資料
		for (CustNotice sCustNotice : iCustNotice) {
			if (j == 41) {
				break;
			}
			CdReport iCdReport = new CdReport();
			String iFormNo = sCustNotice.getFormNo();
			iCdReport = sCdReportService.FormNoFirst(iFormNo, titaVo);
			if (iCdReport == null) {
				continue;
			}
			if (iCdReport.getSendCode() == 0) {
				continue;
			}
			if (cdFormNo.contains(iFormNo)) {
				cdFormNo.remove(iFormNo);
			}

			this.totaVo.putParam("L1r04FormNo" + j, iFormNo);
			this.totaVo.putParam("L1r04FormName" + j, iCdReport.getFormName());
			this.totaVo.putParam("L1r04SendCode" + j, iCdReport.getSendCode());
			this.totaVo.putParam("L1r04Paper" + j, "Y");
			this.totaVo.putParam("L1r04Msg" + j, "Y");
			this.totaVo.putParam("L1r04EMail" + j, "Y");
			if (!sCustNotice.getPaperNotice().equals("N")) {
				this.totaVo.putParam("L1r04Paper" + j, " ");
			}
			if (!sCustNotice.getMsgNotice().equals("N")) {
				this.totaVo.putParam("L1r04Msg" + j, " ");
			}
			if (!sCustNotice.getEmailNotice().equals("N")) {
				this.totaVo.putParam("L1r04EMail" + j, " ");
			}
			j++;
		}
		// 回傳新符合報表
		if (cdFormNo != null) {
			this.info("新符合報表=" + cdFormNo.toString());
			for (String rFormNo : cdFormNo) {
				if (j == 41) {
					break;
				}
				CdReport cCdReport = new CdReport();
				cCdReport = sCdReportService.findById(rFormNo, titaVo);
				this.totaVo.putParam("L1r04FormNo" + j, rFormNo);
				this.totaVo.putParam("L1r04FormName" + j, cCdReport.getFormName());
				this.totaVo.putParam("L1r04SendCode" + j, cCdReport.getSendCode());
				this.totaVo.putParam("L1r04Paper" + j, " ");
				this.totaVo.putParam("L1r04Msg" + j, " ");
				this.totaVo.putParam("L1r04EMail" + j, " ");
				j++;
			}
		}
		// 回傳報表
		while (j <= 40) {
			this.totaVo.putParam("L1r04FormNo" + j, " ");
			this.totaVo.putParam("L1r04FormName" + j, " ");
			this.totaVo.putParam("L1r04SendCode" + j, " ");
			this.totaVo.putParam("L1r04Paper" + j, " ");
			this.totaVo.putParam("L1r04Msg" + j, " ");
			this.totaVo.putParam("L1r04EMail" + j, " ");
			j++;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}