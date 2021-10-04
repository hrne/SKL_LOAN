package com.st1.itx.trade.L4;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PostAuthLogHistory;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PostAuthLogHistoryService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4942")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4942 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public PostAuthLogHistoryService postAuthLogHistoryService;
	@Autowired
	public CdEmpService cdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4942 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<PostAuthLogHistory> lPostAuthLogHistory = new ArrayList<PostAuthLogHistory>();

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		// wk
		String wkUser = "";

		Slice<PostAuthLogHistory> sPostAuthLogHistory = null;

		sPostAuthLogHistory = postAuthLogHistoryService.facmNoEq(custNo, facmNo, index, limit, titaVo);

		lPostAuthLogHistory = sPostAuthLogHistory == null ? null : sPostAuthLogHistory.getContent();

		if (lPostAuthLogHistory != null && lPostAuthLogHistory.size() != 0) {

			for (PostAuthLogHistory tPostAuthLogHistory : lPostAuthLogHistory) {

				String wkCreateFlag = tPostAuthLogHistory.getAuthApplCode();
//				if (tPostAuthLogHistory.getDeleteDate() > 0) {
////					1申請 2終止 9暫停
//					if ("00".equals(tPostAuthLogHistory.getAuthErrorCode())) {
//						wkCreateFlag = "9";
//					} else {
//						wkCreateFlag = "2";
//					}
//				}
				wkUser = tPostAuthLogHistory.getLastUpdateEmpNo();
				CdEmp tCdEmp = cdEmpService.findById(wkUser, titaVo);
				if (tCdEmp != null) {
					wkUser = wkUser + " " + tCdEmp.getFullname();
				}
				// 宣告
				String updateTime = "";
				this.info("LastUpdate = " + tPostAuthLogHistory.getLastUpdate());
				if (tPostAuthLogHistory.getLastUpdate() != null) {
					Timestamp ts = tPostAuthLogHistory.getLastUpdate();
					DateFormat sdftime = new SimpleDateFormat("HHmmss");

					updateTime = sdftime.format(ts);
					updateTime = updateTime.substring(0,2)+":"+updateTime.substring(2,4)+":"+updateTime.substring(4,6);

				}
				this.info("updateTime = " + updateTime);

				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", tPostAuthLogHistory.getCustNo());
				occursList.putParam("OOFacmNo", tPostAuthLogHistory.getFacmNo());
				occursList.putParam("OOAuthType", tPostAuthLogHistory.getAuthCode());
				occursList.putParam("OOPostDepCode", tPostAuthLogHistory.getPostDepCode());
				occursList.putParam("OORepayAcct", tPostAuthLogHistory.getRepayAcct());
				occursList.putParam("OOLimitAmt", 0);
				occursList.putParam("OOAcctSeq", tPostAuthLogHistory.getRepayAcctSeq());
				occursList.putParam("OOCustId", tPostAuthLogHistory.getCustId());
				occursList.putParam("OOCreateFlag", wkCreateFlag);
				occursList.putParam("OOAuthCreateDate", tPostAuthLogHistory.getAuthCreateDate());
				occursList.putParam("OOPropDate", tPostAuthLogHistory.getPropDate());
				occursList.putParam("OORetrDate", tPostAuthLogHistory.getRetrDate());
				occursList.putParam("OOStampCode", tPostAuthLogHistory.getStampCode());
				occursList.putParam("OOAuthErrorCode", tPostAuthLogHistory.getAuthErrorCode());
				occursList.putParam("OOPostMediaCode", tPostAuthLogHistory.getPostMediaCode());
				occursList.putParam("OOAmlRsp", tPostAuthLogHistory.getAmlRsp());
				occursList.putParam("OORepayAcctLog", tPostAuthLogHistory.getRepayAcct());
				occursList.putParam("OOStampFinishDate", tPostAuthLogHistory.getStampFinishDate());
				occursList.putParam("OODeleteDate", tPostAuthLogHistory.getDeleteDate());
				occursList.putParam("OOUser", wkUser);
				String sDate = tPostAuthLogHistory.getLastUpdate().toString().substring(0, 10).trim();
				sDate = sDate.replace("-", "");
				int iDate = parse.stringToInteger(sDate);
				if (iDate > 19110000) {
					iDate -= 19110000;
				}
				occursList.putParam("OODate", iDate);
				occursList.putParam("OOLastUpdateTime", updateTime);

				totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}