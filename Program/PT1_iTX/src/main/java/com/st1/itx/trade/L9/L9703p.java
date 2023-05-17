package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L9703p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9703p")
@Scope("prototype")
public class L9703p extends TradeBuffer {

	@Autowired
	L9703Report l9703report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	CdReportService sCdReportService;

	@Autowired
	CustNoticeService sCustNoticeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9703p ");
		this.totaVo.init(titaVo);

		this.info("L9703p titaVo.getTxcd() = " + titaVo.getTxcd());
		String formNo = "L9703";
		CdReport tCdReport = new CdReport();
		Slice<CustNotice> fFormNo = null;
		tCdReport = sCdReportService.findById(formNo, titaVo);

		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));

		String Fg = tCdReport.getLetterFg();
		if ("Y".equals(Fg)) {
			String iFg = "";
			fFormNo = sCustNoticeService.findCustNoFormNo(iCustNo, formNo, 0, Integer.MAX_VALUE, titaVo);
			int iFormcount = fFormNo.getSize();
			this.info("iFormcount   = " + iFormcount);
			if (iFormcount == 1) {
				for (CustNotice custNotice : fFormNo) {
					iFg = custNotice.getPaperNotice();
				}
			}
			this.info("iFg     = " + iFg);
			if (iFormcount != 1 || (iFormcount == 1 && iFg.equals("Y"))) {
				Slice<CustNotice> tCustNotice = null;
				Slice<CustNotice> iCustNotice = null;
				if (iFacmNo == 0) {
					tCustNotice = sCustNoticeService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
				} else {
					iCustNotice = sCustNoticeService.facmNoEq(iCustNo, iFacmNo, iFacmNo, 0, Integer.MAX_VALUE, titaVo);
				}
				this.info("tCustNotice   = " + tCustNotice);
				this.info("iCustNotice   = " + iCustNotice);

				// 額度0
				List<CustNotice> lCustNotice = tCustNotice == null ? null : tCustNotice.getContent();
				if (lCustNotice != null && lCustNotice.size() > 0) {
					String tran = titaVo.getTxCode().isEmpty() ? "L9703" : titaVo.getTxCode();
					String parentTranCode = titaVo.getTxcd();
					l9703report.setParentTranCode(parentTranCode);
					l9703report.exec(titaVo, this.getTxBuffer());
					webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
							titaVo.getParam("TLRNO") + tran, tran + " 滯繳客戶明細表 已完成", titaVo);
				}

				// 有額度判別是否要產出
				List<CustNotice> oCustNotice = iCustNotice == null ? null : iCustNotice.getContent();
				String pFg = "";
				for (CustNotice t : oCustNotice) {
					pFg = t.getPaperNotice();
				}
				if (iFacmNo != 0 && oCustNotice != null && pFg.equals("Y")) {
					String tran = titaVo.getTxCode().isEmpty() ? "L9703" : titaVo.getTxCode();
					String parentTranCode = titaVo.getTxcd();
					l9703report.setParentTranCode(parentTranCode);
					l9703report.exec(titaVo, this.getTxBuffer());
					webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
							titaVo.getParam("TLRNO") + tran, tran + " 滯繳客戶明細表 已完成", titaVo);
				}
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}