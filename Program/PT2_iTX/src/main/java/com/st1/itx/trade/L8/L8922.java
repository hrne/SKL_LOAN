package com.st1.itx.trade.L8;

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
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.springjpa.cm.L8923ServiceImpl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcDateStart=9,7 AcDateEnd=9,7 Factor=9,2 Type=9,1 END=X,1
 */

@Service("L8922") // 疑似洗錢交易合理性明細檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L8922 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryDetailService sMlaundryDetailService;
	@Autowired
	MlaundryRecordService sMlaundryRecordService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	L8923ServiceImpl l8923ServiceImpl;
	@Autowired
	TxDataLogService sTxDataLogService;
	@Autowired
	Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8922 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";
		int iFactor = this.parse.stringToInteger(titaVo.getParam("Factor"));
		int iType = this.parse.stringToInteger(titaVo.getParam("Type"));
		int iLevel = this.txBuffer.getTxCom().getTlrLevel();
		this.info("L8922 iLevel : " + iLevel);
		this.info("L8922 iType : " + iType);
		this.info("L8922 iFactor : " + iFactor);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 461 * 100 = 46,100

		// 查詢疑似洗錢交易合理性明細檔檔
		Slice<MlaundryDetail> slMlaundryDetail = null;
		if (iType == 1) {
			int iAcDateStart = this.parse.stringToInteger(titaVo.getParam("AcDateStart"));
			int iAcDateEnd = this.parse.stringToInteger(titaVo.getParam("AcDateEnd"));
			int iFAcDateStart = iAcDateStart + 19110000;
			int iFAcDateEnd = iAcDateEnd + 19110000;
			this.info("L8922 iFAcDate : " + iFAcDateStart + "~" + iFAcDateEnd);
			slMlaundryDetail = sMlaundryDetailService.findbyDate(iFAcDateStart, iFAcDateEnd, this.index, this.limit, titaVo);
		} else if (iType == 2) {
			slMlaundryDetail = sMlaundryDetailService.findAll(0, Integer.MAX_VALUE, titaVo);
		}

		List<MlaundryDetail> lMlaundryDetail = slMlaundryDetail == null ? null : slMlaundryDetail.getContent();

		if (lMlaundryDetail == null || lMlaundryDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "疑似洗錢交易合理性明細檔"); // 查無資料
		}

		boolean hasOutput = false;
		// 如有找到資料
		for (MlaundryDetail tMlaundryDetail : lMlaundryDetail) {

			if (iType == 2) {
				// 未完成時只接受 ManagerCheck 不為 Y 者
				if ("Y".equals(tMlaundryDetail.getManagerCheck()))
					continue;
			}

			OccursList occursList = new OccursList();

			// 0:全部;1:樣態1;2:樣態2;3:樣態3 -> 不等時找下一筆
			if (!(iFactor == 0) && !(iFactor == tMlaundryDetail.getFactor())) {
				continue;
			}

			switch (iLevel) {

			case 1:
				// 主管
				// 經辦未輸入合理性,主管查不出來
				this.info("L8922 Rational" + tMlaundryDetail.getRational());
				if (tMlaundryDetail.getRational().trim().isEmpty()) {
					this.info("Rational 空白");
					continue;
				}
				break;

			case 3:
				// 經辦
				break;
			}

			// 查詢客戶資料主檔
			CustMain tCustMain = new CustMain();
			tCustMain = sCustMainService.custNoFirst(tMlaundryDetail.getCustNo(), tMlaundryDetail.getCustNo(), titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
			}

			occursList.putParam("OOAcDate", tMlaundryDetail.getEntryDate()); // 入帳日期
			occursList.putParam("OOFactor", tMlaundryDetail.getFactor()); // 交易樣態
			occursList.putParam("OOCustNo", tMlaundryDetail.getCustNo()); // 戶號
			occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名
			occursList.putParam("OOTotalCnt", tMlaundryDetail.getTotalCnt()); // 累積筆數
			occursList.putParam("OOTotalAmt", tMlaundryDetail.getTotalAmt()); // 累積金額
			occursList.putParam("OORational", tMlaundryDetail.getRational()); // 合理性記號
			occursList.putParam("OOEmpNoDesc", tMlaundryDetail.getEmpNoDesc().replace("$n", "\n")); // 經辦合理性說明
			occursList.putParam("OOManagerDesc", tMlaundryDetail.getManagerDesc().replace("$n", "\n")); // 主管覆核說明
			occursList.putParam("OOEmpNo", tMlaundryDetail.getLastUpdateEmpNo()); // 經辦
			occursList.putParam("OOManagerCheck", tMlaundryDetail.getManagerCheck()); // 主管覆核
			if (tMlaundryDetail.getManagerDate() == 0) {
				occursList.putParam("OOManagerDate", ""); // 主管同意日期
			} else {
				occursList.putParam("OOManagerDate", tMlaundryDetail.getManagerDate()); // 主管同意日期
			}

			// 先檢查是否有訪談, Y/N
			titaVo.putParam("CustNo", tMlaundryDetail.getCustNo());
			titaVo.putParam("ActualRepayDateStart", tMlaundryDetail.getEntryDate());
			titaVo.putParam("ActualRepayDateEnd", tMlaundryDetail.getEntryDate());
			titaVo.putParam("RecordDateStart", 0);
			titaVo.putParam("RecordDateEnd", 0);

			// 訪談按鈕顯示邏輯：該戶號有Record訪談日期大於Detail入帳日期的資料時，就有訪談資料
			MlaundryRecord MlaundryRecord = sMlaundryRecordService.findCustNoAndRecordDateFirst(tMlaundryDetail.getCustNo(), tMlaundryDetail.getEntryDate() + 19110000, 99991231, titaVo);

			occursList.putParam("OOHasL8923", MlaundryRecord != null ? "Y" : "N");

			DateTime = this.parse.timeStampToString(tMlaundryDetail.getLastUpdate()); // 異動日期
			this.info("L8922 DateTime : " + DateTime);
			Date = FormatUtil.left(DateTime, 9);
			occursList.putParam("OOUpdate", Date);

			// 歷程按鈕顯示或隱藏
			Slice<TxDataLog> slTxDataLog = sTxDataLogService.findByCustNo1(tMlaundryDetail.getEntryDate() + 19110000, parse.stringToInteger(titaVo.getCalDy()) + 19110000, "L8203",
					tMlaundryDetail.getCustNo(), 0, Integer.MAX_VALUE, titaVo);
			List<TxDataLog> lTxDataLog = slTxDataLog == null ? new ArrayList<TxDataLog>() : slTxDataLog.getContent();

			boolean hasResult = false;

			for (TxDataLog txDataLog : lTxDataLog) {
				if (txDataLog.getMrKey()
						.equals((tMlaundryDetail.getEntryDate()) + "-" + parse.IntegerToString(tMlaundryDetail.getFactor(), 2) + "-" + parse.IntegerToString(tMlaundryDetail.getCustNo(), 7))) {
					hasResult = true;
					break;
				}
			}

			occursList.putParam("OOHasHistory", hasResult ? "Y" : "N");

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
			hasOutput = true;
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slMlaundryDetail != null && slMlaundryDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		if (!hasOutput) {
			this.error("Tried to output, but hasOutput == false...");
			throw new LogicException(titaVo, "E0001", "疑似洗錢交易合理性明細檔"); // 實際上沒有任何輸出, 視為查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}