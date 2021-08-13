package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.InnReCheckId;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.db.service.springjpa.cm.L5106ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L5106Batch")
@Scope("prototype")
/**
 * 產生覆審案件資料明細(L5106啟動)
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L5106Batch extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5106Batch.class);

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public InnReCheckService InnReCheckService;
	@Autowired
	public WebClient webClient;
	@Autowired
	public L5106ServiceImpl L5106ServiceImpl;

	private String sendMsg = "";
	private int processCnt = 0;

	private Boolean isError = false;
	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private List<InnReCheck> lDetail = new ArrayList<InnReCheck>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5106Batch ");
		this.totaVo.init(titaVo);
//		titaVo.putParam("YearMonth", "10904"); // 覆審年月
		// 抓取資料
		if (!isError) {
			try {
				findAll(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				isError = true;
			}
		}
		this.batchTransaction.commit();

		// 新增 InnReCheck 疑似洗錢交易合理性明細檔
		if (!isError) {
			try {
				insertDetail(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				isError = true;
			}
		}
		this.batchTransaction.commit();

		// Broadcast
		if (isError) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", titaVo.getTlrNo(),
					"程式處理錯誤" + sendMsg, titaVo);
		} else {
			if (this.processCnt > 0) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5905", "",
						"覆審案件明細資料已產生，筆數：" + this.processCnt, titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", titaVo.getTlrNo(),
						"覆審案件明細資料", titaVo);
			}
		}
		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void findAll(TitaVo titaVo) throws LogicException {
		this.info("BS510 findAll");

		try {
			fnAllList = L5106ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage());
		}
	}

	private void insertDetail(TitaVo titaVo) throws LogicException {
		this.info("BS510 insertDetail");

		InnReCheck d = new InnReCheck();
		InnReCheckId dId = new InnReCheckId();

//		運算全戶餘額及其他輸出欄位
		if (fnAllList != null && fnAllList.size() != 0) {
			for (Map<String, String> s : fnAllList) {
				this.info("fnAllList=" + s);
				// ConditionCode\" AS F0 --條件代碼
				// CustNo\" AS F1 --借款人戶號
				// FacmNo\" AS F2 --額度號碼
				// ReCheckCode\" AS F3 --覆審記號
				// FollowMark\" AS F4 --是否追蹤
				// ReChkYearMonth\" AS F5 --覆審年月
				// DrawdownDate\" AS F6 --撥款日期
				// LoanBal\" AS F7 --貸放餘額
				// Evaluation\" AS F8 --評等
				// CustTypeItem\" AS F9 --客戶別
				// UsageItem\" AS F10 --用途別
				// CityItem\" AS F11 --地區別
				// ReChkUnit\" AS F12 --應覆審單位
				// Remark\" AS F13 --備註
				d = new InnReCheck();
				dId = new InnReCheckId();
				// F0 YearMonth --資料年月
				// F1 ConditionCode --條件代碼
				// F2 CustNo --借款人戶號
				// F3 FacmNo --額度號碼
				d = new InnReCheck();
				dId = new InnReCheckId();
				dId.setYearMonth(parse.stringToInteger(s.get("F0")));
				dId.setConditionCode(parse.stringToInteger(s.get("F1")));
				dId.setCustNo(parse.stringToInteger(s.get("F2")));
				dId.setFacmNo(parse.stringToInteger(s.get("F3")));
				d.setInnReCheckId(dId);
				d.setReCheckCode(s.get("F4"));
				d.setFollowMark(s.get("F5"));
				d.setReChkYearMonth(parse.stringToInteger(s.get("F6")));
				d.setDrawdownDate(parse.stringToInteger(s.get("F7")));
				d.setLoanBal(parse.stringToBigDecimal(s.get("F8")));
				d.setEvaluation(parse.stringToInteger(s.get("F9")));
				d.setCustTypeItem(s.get("F10"));
				d.setUsageItem(s.get("F11"));
				d.setCityItem(s.get("F12"));
				d.setReChkUnit(s.get("F13"));
				d.setRemark(s.get("F14"));
				lDetail.add(d);
			}
		}
		this.info("BS510 insertDetail size=" + lDetail.size());
		for (InnReCheck dtl : lDetail) {
			this.info("insert=" + dtl.toString());
		}
		processCnt = lDetail.size();
		if (lDetail.size() > 0) {
			try {
				InnReCheckService.insertAll(lDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", ", InnReCheck insert error : " + e.getErrorMsg());
			}
		}
	}
}