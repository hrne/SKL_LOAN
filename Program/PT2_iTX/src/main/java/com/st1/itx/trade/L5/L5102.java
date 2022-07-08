package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnLoanMeeting;
import com.st1.itx.db.service.InnLoanMeetingService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * MeetingDate=9,7<br>
 * CustCnt=9,4<br>
 * END=X,1<br>
 */

@Service("L5102")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5102 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnLoanMeetingService innLoanMeetingService;

	/* 自動取號 */
	@Autowired
	GSeqCom gGSeqCom;
	
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5102 ");
		this.totaVo.init(titaVo);

		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		int iMeetingDate = parse.stringToInteger(titaVo.getParam("MeetingDate").trim());

		int iMeetNo = 0; // 放審會流水號(2020/12/2新增)
		if (iFunctionCode == 1) {
			// 新增時取號
			iMeetNo = gGSeqCom.getSeqNo(0, 0, "L5", "0001", 9999999, titaVo);
		} else {
			iMeetNo = parse.stringToInteger(titaVo.getParam("MeetNo").trim());
		}
		this.info("取號=" + iMeetNo);
		InnLoanMeeting iInnLoanMeeting = innLoanMeetingService.findById(iMeetNo, titaVo);
		InnLoanMeeting tInnLoanMeeting = new InnLoanMeeting();
		switch (iFunctionCode) {
		case 1:
			if (iInnLoanMeeting != null) {
				throw new LogicException(titaVo, "E0005", "");
			} else {
				tInnLoanMeeting.setMeetNo(iMeetNo);
				tInnLoanMeeting.setMeetingDate(iMeetingDate);
				tInnLoanMeeting.setCustCode(titaVo.getParam("CustCode").trim());
				tInnLoanMeeting.setAmount(parse.stringToBigDecimal(titaVo.getParam("Amount").trim()));
				tInnLoanMeeting.setIssue(titaVo.getParam("Issue"));
				tInnLoanMeeting.setRemark(titaVo.getParam("Remark"));
				try {
					innLoanMeetingService.insert(tInnLoanMeeting, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L5102 InnLoanMeeting insert " + e.getErrorMsg());
				}
			}
			break;
		case 2:
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			if (iInnLoanMeeting == null) {
				throw new LogicException(titaVo, "E0006", "");
			} else {
				tInnLoanMeeting = innLoanMeetingService.holdById(iMeetNo, titaVo);
				tInnLoanMeeting.setMeetingDate(iMeetingDate);
				tInnLoanMeeting.setCustCode(titaVo.getParam("CustCode").trim());
				tInnLoanMeeting.setAmount(parse.stringToBigDecimal(titaVo.getParam("Amount").trim()));
				tInnLoanMeeting.setIssue(titaVo.getParam("Issue"));
				tInnLoanMeeting.setRemark(titaVo.getParam("Remark"));
				try {
					innLoanMeetingService.update(tInnLoanMeeting, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L5102 InnLoanMeeting update " + e.getErrorMsg());
				}
			}
			break;
		case 4:
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			if (iInnLoanMeeting == null) {
				throw new LogicException(titaVo, "E0006", "");
			} else {
				tInnLoanMeeting = innLoanMeetingService.holdById(iMeetNo, titaVo);
				try {
					innLoanMeetingService.delete(tInnLoanMeeting, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5102 InnLoanMeeting delete " + e.getErrorMsg());
				}
			}
			break;
		case 5:
			break;
		}
		
		this.totaVo.putParam("MeetNo", tInnLoanMeeting.getMeetNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}