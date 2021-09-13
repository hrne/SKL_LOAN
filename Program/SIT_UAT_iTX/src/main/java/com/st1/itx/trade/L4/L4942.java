package com.st1.itx.trade.L4;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PostAuthLogHistory;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4942 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		Slice<PostAuthLogHistory> slPostAuthLogHistory= postAuthLogHistoryService.facmNoEq(custNo, facmNo, index, limit, titaVo);

		if (slPostAuthLogHistory != null ) {
			for (PostAuthLogHistory tPostAuthLogHistory : slPostAuthLogHistory.getContent()) {

				String wkCreateFlag = tPostAuthLogHistory.getAuthApplCode();
				if (tPostAuthLogHistory.getDeleteDate() > 0) {
//					1申請 2終止 9暫停
					if ("00".equals(tPostAuthLogHistory.getAuthErrorCode())) {
						wkCreateFlag = "9";
					} else {
						wkCreateFlag = "2";
					}
				}
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
				occursList.putParam("OOUser", tPostAuthLogHistory.getLastUpdateEmpNo());
				String sDate = tPostAuthLogHistory.getLastUpdate().toString().substring(0, 10).trim();
				sDate = sDate.replace("-", "");
				int iDate = parse.stringToInteger(sDate);
				if (iDate > 19110000)
					iDate -= 19110000;
				occursList.putParam("OODate", iDate);

				totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}