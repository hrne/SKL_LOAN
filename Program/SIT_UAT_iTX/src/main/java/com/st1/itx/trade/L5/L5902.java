package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.InnLoanMeeting;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.InnLoanMeetingService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * MeetingDateFrom=9,7<br>
 * MeetingDateTo=9,7<br>
 * END=X,1<br>
 */

@Service("L5902")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5902 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnLoanMeetingService innLoanMeetingService;
	
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5902 ");
		this.totaVo.init(titaVo);

		int meetingDateFrom = parse.stringToInteger(titaVo.getParam("MeetingDateFrom")) + 19110000;
		int meetingDateTo = parse.stringToInteger(titaVo.getParam("MeetingDateTo")) + 19110000;

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 80;

		Slice<InnLoanMeeting> sInnLoanMeeting = null;

		List<InnLoanMeeting> lInnLoanMeeting = new ArrayList<InnLoanMeeting>();

		sInnLoanMeeting = innLoanMeetingService.meetingDateRg(meetingDateFrom, meetingDateTo, this.index, this.limit);

		lInnLoanMeeting = sInnLoanMeeting == null ? null : sInnLoanMeeting.getContent();

		if (lInnLoanMeeting != null && lInnLoanMeeting.size() != 0) {
			int refMeetDate = 0;
			int CustCnt = 0;
			BigDecimal DaySumAmt = new BigDecimal("0");
			for (InnLoanMeeting tInnLoanMeeting : lInnLoanMeeting) {
				String iEmpNo = "";
				CdEmp iCdEmp = new CdEmp();
				OccursList occursList = new OccursList();
				occursList.putParam("OOMeetingDate", tInnLoanMeeting.getMeetingDate());
				occursList.putParam("OOCustCode", tInnLoanMeeting.getCustCode());
				occursList.putParam("OOAmount", tInnLoanMeeting.getAmount());
				occursList.putParam("OOMeetNo", tInnLoanMeeting.getMeetNo());
				occursList.putParam("OOIssue", tInnLoanMeeting.getIssue());
				occursList.putParam("OORemark", tInnLoanMeeting.getRemark());
				// 計算件數和總金額
				if (refMeetDate == 0 || refMeetDate != Integer.valueOf(tInnLoanMeeting.getMeetingDate())) { // 第一筆資料或是月分與上一次不符合時重製計算資料
					refMeetDate = Integer.valueOf(tInnLoanMeeting.getMeetingDate());
					CustCnt = 1;
					DaySumAmt = new BigDecimal("0");
					DaySumAmt = DaySumAmt.add(tInnLoanMeeting.getAmount());
					occursList.putParam("OOCustCnt", CustCnt);
					occursList.putParam("OODaySumAmt", DaySumAmt);
				} else {
					CustCnt += 1;
					DaySumAmt = DaySumAmt.add(tInnLoanMeeting.getAmount());
					occursList.putParam("OOCustCnt", CustCnt);
					occursList.putParam("OODaySumAmt", DaySumAmt);
				}
				if (tInnLoanMeeting.getLastUpdate() == null || tInnLoanMeeting.getLastUpdate().toString().equals("")) {
					occursList.putParam("OOLastUpdate", "");
				} else {
					String taU = tInnLoanMeeting.getLastUpdate().toString();
					String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
					String uTime = taU.substring(11, 19);
					uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
					occursList.putParam("OOLastUpdate", uaDate+ " " + uTime);
				}
				if (tInnLoanMeeting.getLastUpdateEmpNo().trim().isEmpty()) {
					iEmpNo = tInnLoanMeeting.getCreateEmpNo();
				}else {
					iEmpNo = tInnLoanMeeting.getLastUpdateEmpNo();
				}
				
				occursList.putParam("OOLastUpdateEmpNo", iEmpNo);
				if (iEmpNo == null || iEmpNo.trim().isEmpty()) {
					occursList.putParam("OOLastUpdateEmpNoX", "");
				} else {
					iCdEmp = iCdEmpService.findById(iEmpNo, titaVo);
					if (iCdEmp==null) {
						occursList.putParam("OOLastUpdateEmpNoX", "");
					}else {
						occursList.putParam("OOLastUpdateEmpNoX", iCdEmp.getFullname());
					}
					
				}
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}