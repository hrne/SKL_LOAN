package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryChkDtl;
import com.st1.itx.db.domain.MlaundryChkDtlId;
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryDetailId;
import com.st1.itx.db.service.MlaundryChkDtlService;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.service.springjpa.cm.BS820ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS820")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class BS820 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS820.class);

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public MlaundryDetailService mlaundryDetailService;
	@Autowired
	public MlaundryChkDtlService mlaundryChkDtlService;
	@Autowired
	public WebClient webClient;
	@Autowired
	public BS820ServiceImpl bS820ServiceImpl;

	private String sendMsg = "";
	private int processCnt = 0;
	private int custNo;
	private Boolean isError = false;
	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private List<MlaundryDetail> lDetail = new ArrayList<MlaundryDetail>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS820 ");
		this.totaVo.init(titaVo);
		titaVo.putParam("Factor1TotLimit", titaVo.getParam("Factor1TotLimit")); // 洗錢樣態一金額合計超過
		titaVo.putParam("Factor2Count", titaVo.getParam("Factor2Count")); // 洗錢樣態二次數
		titaVo.putParam("Factor2AmtStart", titaVo.getParam("Factor2AmtStart")); // 洗錢樣態二單筆起始金額
		titaVo.putParam("Factor2AmtEnd", titaVo.getParam("Factor2AmtEnd")); // 洗錢樣態二單筆迄止金額
		titaVo.putParam("Factor3TotLimit", titaVo.getParam("Factor3TotLimit")); // 洗錢樣態三金額合計超過
		titaVo.putParam("FactorDays", titaVo.getParam("FactorDays")); // 統計期間天數
		titaVo.putParam("EntryDateS", titaVo.getParam("AcDateStart")); // 入帳日期起日
		titaVo.putParam("EntryDateE", titaVo.getParam("AcDateEnd")); // 入帳日期迄日
		// 刪除未寫疑似洗錢樣態檢核明細檔的疑似洗錢交易合理性明細檔
		try {
			deleteDetail(titaVo);
		} catch (LogicException e) {
			sendMsg = e.getErrorMsg();
			isError = true;
		}
		this.batchTransaction.commit();

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

		// 新增 MlaundryDetail 疑似洗錢交易合理性明細檔
		if (!isError) {
			try {
				insertDetail(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				isError = true;
			}
		}
		this.batchTransaction.commit();

		// 新增 MlaundryChkDtl 疑似洗錢樣態檢核明細檔
		if (!isError) {
			try {
				insertChkDtl(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				isError = true;
			}
		}

		// 更新 MlaundryDetail 疑似洗錢交易合理性明細檔，
		if (!isError) {
			try {
				updateDetail(titaVo);
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
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L8921",
						titaVo.getParam("EntryDateS") + titaVo.getParam("EntryDateE"),
						"疑似洗錢樣態資料已產生，筆數：" + this.processCnt, titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", titaVo.getTlrNo(),
						"無疑似洗錢樣態資料", titaVo);
			}
		}
		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void findAll(TitaVo titaVo) throws LogicException {
		this.info("BS820 findAll");

		try {
			fnAllList = bS820ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage());
		}
	}

	private void insertChkDtl(TitaVo titaVo) throws LogicException {
		this.info("BS820 insertChkDtl");
		fnAllList = new ArrayList<>();
		try {
			fnAllList = bS820ServiceImpl.findChkDtl(titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage());
		}
		List<MlaundryChkDtl> lChkDtl = new ArrayList<MlaundryChkDtl>();
		MlaundryChkDtl c = new MlaundryChkDtl();
		MlaundryChkDtlId cId = new MlaundryChkDtlId();

//		運算全戶餘額及其他輸出欄位
		if (fnAllList != null && fnAllList.size() != 0) {
			for (Map<String, String> s : fnAllList) {
				this.info("fnAllList=" + s);
				// F0 入帳日期
				// F1 交易樣態
				// F2 戶號
				// F3 明細序號
				// F4 明細入帳日期
				// F5 來源
				// F6 摘要代碼
				// F7 交易金額
				// F8 累積筆數
				// F9 累積金額
				// F10統計期間起日
				c = new MlaundryChkDtl();
				cId = new MlaundryChkDtlId();
				cId.setEntryDate(parse.stringToInteger(s.get("F0")));
				cId.setFactor(parse.stringToInteger(s.get("F1")));
				cId.setCustNo(parse.stringToInteger(s.get("F2")));
				cId.setDtlSeq(parse.stringToInteger(s.get("F3")));
				c.setMlaundryChkDtlId(cId);
				c.setDtlEntryDate(parse.stringToInteger(s.get("F4")));
				c.setRepayItem(s.get("F5"));
				c.setDscptCode(s.get("F6"));
				c.setTxAmt(parse.stringToBigDecimal(s.get("F7")));
				c.setTotalCnt(parse.stringToInteger(s.get("F8")));
				c.setTotalAmt(parse.stringToBigDecimal(s.get("F9")));
				c.setStartEntryDate(parse.stringToInteger(s.get("F10")));
				c.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				lChkDtl.add(c);
			}
			try {
				mlaundryChkDtlService.insertAll(lChkDtl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", ", MlaundryChkDtl insert error : " + e.getErrorMsg());
			}
		}
	}

	private void insertDetail(TitaVo titaVo) throws LogicException {
		this.info("BS820 insertDetail");

		MlaundryDetail d = new MlaundryDetail();
		MlaundryDetailId dId = new MlaundryDetailId();

//		運算全戶餘額及其他輸出欄位
		if (fnAllList != null && fnAllList.size() != 0) {
			for (Map<String, String> s : fnAllList) {
				this.info("fnAllList=" + s);
				// F0 戶號
				// F1 入帳日期
				// F2 洗錢樣態一累計筆數
				// F3 洗錢樣態一累計金額
				// F4 洗錢樣態一資料重複 (1.是)
				// F5 洗錢樣態二累計筆數
				// F6 洗錢樣態二累計金額
				// F7 洗錢樣態二資料重複 (1.是)
				// F8 洗錢樣態三累計筆數
				// F9 洗錢樣態三累計金額
				// F10 洗錢樣態三資料重複 (1.是)
				custNo = parse.stringToInteger(s.get("F4"));
				// 專戶不寫
				if (custNo == this.txBuffer.getSystemParas().getLoanDeptCustNo()
						|| custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
					continue;
				}
				if (parse.stringToInteger(s.get("F2")) > 0 && parse.stringToInteger(s.get("F4")) == 0) {
					d = new MlaundryDetail();
					dId = new MlaundryDetailId();
					dId.setEntryDate(parse.stringToInteger(s.get("F1")));
					dId.setFactor(1);
					dId.setCustNo(parse.stringToInteger(s.get("F0")));
					d.setMlaundryDetailId(dId);
					d.setTotalCnt(parse.stringToInteger(s.get("F2")));
					d.setTotalAmt(parse.stringToBigDecimal(s.get("F3")));
					d.setRational("?"); // 未寫檢核明細檔
					lDetail.add(d);
				}
				if (parse.stringToInteger(s.get("F5")) > 0 && parse.stringToInteger(s.get("F7")) == 0) {
					d = new MlaundryDetail();
					dId = new MlaundryDetailId();
					dId.setEntryDate(parse.stringToInteger(s.get("F1")));
					dId.setFactor(2);
					dId.setCustNo(parse.stringToInteger(s.get("F0")));
					d.setMlaundryDetailId(dId);
					d.setTotalCnt(parse.stringToInteger(s.get("F5")));
					d.setTotalAmt(parse.stringToBigDecimal(s.get("F6")));
					d.setRational("?"); // 未寫檢核明細檔
					lDetail.add(d);
				}
				if (parse.stringToInteger(s.get("F8")) > 0 && parse.stringToInteger(s.get("F10")) == 0) {
					d = new MlaundryDetail();
					dId = new MlaundryDetailId();
					dId.setEntryDate(parse.stringToInteger(s.get("F1")));
					dId.setFactor(3);
					dId.setCustNo(parse.stringToInteger(s.get("F0")));
					d.setMlaundryDetailId(dId);
					d.setTotalCnt(parse.stringToInteger(s.get("F8")));
					d.setTotalAmt(parse.stringToBigDecimal(s.get("F9")));
					d.setRational("?"); // 未寫檢核明細檔
					lDetail.add(d);
				}
			}
			this.info("BS820 insertDetail size=" + lDetail.size());
			for (MlaundryDetail dtl : lDetail) {
				this.info("insert=" + dtl.toString());
			}
			processCnt = lDetail.size();
			if (lDetail.size() > 0) {
				try {
					mlaundryDetailService.insertAll(lDetail, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", ", MlaundryDetail insert error : " + e.getErrorMsg());
				}
			}
		}
	}

	private void deleteDetail(TitaVo titaVo) throws LogicException {
		this.info("BS820 deleteDetail");
		Slice<MlaundryDetail> slMlaundryDetail = mlaundryDetailService.findEntryDateRange(0, 99991231,
				Arrays.asList(new String[] { "?" }), 0, Integer.MAX_VALUE, titaVo);
		List<MlaundryDetail> lMlaundryDetail = slMlaundryDetail == null ? null
				: new ArrayList<MlaundryDetail>(slMlaundryDetail.getContent());

		if (lMlaundryDetail != null) {
			try {
				mlaundryDetailService.deleteAll(lMlaundryDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", ", MlaundryDetail deleteAll error : " + e.getErrorMsg());
			}
		}
	}

	private void updateDetail(TitaVo titaVo) throws LogicException {
		this.info("BS820 updateDetail");
		if (lDetail.size() > 0) {
			for (MlaundryDetail d : lDetail) {
				d.setRational(" "); // 已寫檢核明細檔
			}
			try {
				mlaundryDetailService.updateAll(lDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", ", MlaundryDetail insert error : " + e.getErrorMsg());
			}
		}
	}

}