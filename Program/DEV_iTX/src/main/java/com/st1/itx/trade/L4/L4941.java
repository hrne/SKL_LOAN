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
import com.st1.itx.db.domain.AchAuthLogHistory;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.AchAuthLogHistoryService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4941")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4941 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AchAuthLogHistoryService achAuthLogHistoryService;
	@Autowired
	public CdEmpService cdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4941 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<AchAuthLogHistory> lAchAuthLogHistory = new ArrayList<AchAuthLogHistory>();

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		// wk
		String wkUser = "";

		Slice<AchAuthLogHistory> sAchAuthLogHistory = null;

		sAchAuthLogHistory = achAuthLogHistoryService.facmNoEq(custNo, facmNo, index, limit, titaVo);

		lAchAuthLogHistory = sAchAuthLogHistory == null ? null : sAchAuthLogHistory.getContent();

		if (lAchAuthLogHistory != null && lAchAuthLogHistory.size() != 0) {
			for (AchAuthLogHistory tAchAuthLogHistory : lAchAuthLogHistory) {
				OccursList occursList = new OccursList();
				String wkCreateFlag = tAchAuthLogHistory.getCreateFlag();
				if (tAchAuthLogHistory.getDeleteDate() > 0) {
					if ("0".equals(tAchAuthLogHistory.getAuthStatus())) {
						wkCreateFlag = "Z";
					} else {
						wkCreateFlag = "Y";
					}
				}
				if (tAchAuthLogHistory.getLastUpdateEmpNo() != null) {
					wkUser = tAchAuthLogHistory.getLastUpdateEmpNo();
					CdEmp tCdEmp = cdEmpService.findById(wkUser, titaVo);
					if (tCdEmp != null) {
						wkUser = wkUser + " " + tCdEmp.getFullname();
					}
				}

				// 宣告
				String updateTime = "";
				this.info("LastUpdate = " + tAchAuthLogHistory.getLastUpdate());
				if (tAchAuthLogHistory.getLastUpdate() != null) {
					Timestamp ts = tAchAuthLogHistory.getLastUpdate();
					DateFormat sdftime = new SimpleDateFormat("HHmmss");

					updateTime = sdftime.format(ts);
					updateTime = updateTime.substring(0, 2) + ":" + updateTime.substring(2, 4) + ":"
							+ updateTime.substring(4, 6);

				}
				this.info("updateTime = " + updateTime);

				occursList.putParam("OOCustNo", tAchAuthLogHistory.getCustNo());
				occursList.putParam("OOFacmNo", tAchAuthLogHistory.getFacmNo());
				occursList.putParam("OORepayBank", tAchAuthLogHistory.getRepayBank());
				occursList.putParam("OORepayAcct", tAchAuthLogHistory.getRepayAcct());
				occursList.putParam("OOLimitAmt", tAchAuthLogHistory.getLimitAmt());
				occursList.putParam("OOCreateFlag", wkCreateFlag);
				occursList.putParam("OOAuthCreateDate", tAchAuthLogHistory.getAuthCreateDate());
				occursList.putParam("OOPropDate", tAchAuthLogHistory.getPropDate());
				occursList.putParam("OORetrDate", tAchAuthLogHistory.getRetrDate());
				occursList.putParam("OOAuthStatus", tAchAuthLogHistory.getAuthStatus());
				occursList.putParam("OOMediaCode", tAchAuthLogHistory.getMediaCode());
				occursList.putParam("OOAmlRsp", tAchAuthLogHistory.getAmlRsp());
				occursList.putParam("OOStampFinishDate", tAchAuthLogHistory.getStampFinishDate());
				occursList.putParam("OODeleteDate", tAchAuthLogHistory.getDeleteDate());
				occursList.putParam("OOUser", wkUser);
				String sDate = tAchAuthLogHistory.getLastUpdate().toString().substring(0, 10).trim();
				sDate = sDate.replace("-", "");
				int iDate = parse.stringToInteger(sDate);
				if (iDate > 19110000)
					iDate -= 19110000;
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