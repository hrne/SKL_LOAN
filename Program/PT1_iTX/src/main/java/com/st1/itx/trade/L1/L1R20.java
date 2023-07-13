package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1R20")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L1R20 extends TradeBuffer {
	@Autowired
	public CustNoticeService custNoticeService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CdReportService cdReportService;

	@Autowired
	Parse parse;

	// 是否為不列印
	private boolean notPrintLetter = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L1R20 ");
		this.totaVo.init(titaVo);

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		String formNo = titaVo.getParam("FormNo").trim();
		int flag = parse.stringToInteger(titaVo.getParam("RimFlag"));

		String s = "";

		s = chkForm(titaVo, custNo, facmNo, formNo, flag);

		if (!s.isEmpty()) {
			if (flag == 1) {
				// this.totaVo.init(titaVo);

				this.totaVo.setHtmlContent(s);

				// this.addList(totaVo);
			} else {
				TotaVo msgTotaVo = new TotaVo();
				msgTotaVo.setWarnMsg("請詳閱「注意事項」");
				this.addList(msgTotaVo);
				this.info("L1R20 notPrintLetter =" + (notPrintLetter ? "Y" : "N"));
				this.totaVo.putParam("notPrintLetter", notPrintLetter ? "Y" : "N");
				this.totaVo.putParam("L1R20Msg", s);

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String chkForm(TitaVo titaVo, int custNo, int facmNo, String formNo, int flag) {
		String rs = "";

		if (custNo == 0) {
			return rs;
		}

		if (formNo.isEmpty()) {
			return rs;
		}

		if (facmNo > 0) {
			CustNoticeId custNoticeid = new CustNoticeId();
			custNoticeid.setCustNo(custNo);
			custNoticeid.setFacmNo(facmNo);
			custNoticeid.setFormNo(formNo);

			CustNotice custNotice = custNoticeService.findById(custNoticeid, titaVo);

			rs = chkNotice(custNotice, flag);
		} else {
			// 排除一戶多額度中，有一額度沒有申請列印(表示需列印)，會被列為皆不列印之情況
			Slice<FacMain> sFacMainService = facMainService.facmCustNoRange(custNo, custNo, 0, 999, 0,
					Integer.MAX_VALUE, titaVo);

			if (sFacMainService != null) {
				for (FacMain fm : sFacMainService.getContent()) {

					CustNoticeId custNoticeid = new CustNoticeId();
					custNoticeid.setCustNo(fm.getCustNo());
					custNoticeid.setFacmNo(fm.getFacmNo());
					custNoticeid.setFormNo(formNo);
					CustNotice custNotice = custNoticeService.findById(custNoticeid, titaVo);
					rs += chkNotice(custNotice, flag);
				}
			}
		}

		if (!rs.isEmpty()) {
			if (flag == 1) {
				rs = "{red-s}{b-s}顧客申請通知警訊：{b-e}{red-e}<br><br>" + rs;
			} else {
				rs = "顧客申請通知警訊：\n" + rs;
			}
		}

		return rs;
	}

	private String chkNotice(CustNotice custNotice, int flag) {
		String rs = "";

		String s1 = "";
		String s2 = "";

		// 沒申請者 或 要書面通知(表示要列印)資料庫上為N者 不列印記號為Y
		if (custNotice == null || "Y".equals(custNotice.getPaperNotice())) {
			notPrintLetter = false;
		}

		if (custNotice != null) {

			if ("N".equals(custNotice.getPaperNotice())) {
				s1 += s2 + "申請書面不通知";
				s2 = ",";
			}
			if ("N".equals(custNotice.getMsgNotice())) {
				s1 += s2 + "申請簡訊不通知";
				s2 = ",";
			}
			if ("N".equals(custNotice.getEmailNotice())) {
				s1 += s2 + "申請Email不通知";
				s2 = ",";
			}
			if (!s1.isEmpty()) {
				rs = "戶號:" + String.format("%07d", custNotice.getCustNo()) + "-"
						+ String.format("%03d", custNotice.getFacmNo()) + ",已" + s1;
				if (flag == 1) {
					rs += "<br>";
				} else {
					rs += "\n";
				}
			}
		}

		return rs;

	}
}