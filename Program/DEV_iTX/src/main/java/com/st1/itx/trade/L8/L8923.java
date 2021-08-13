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
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita RecordDateStart=9,7 RecordDateEnd=9,7 ActualRepayDateStart=9,7
 * ActualRepayDateEnd=9,7 END=X,1
 */

@Service("L8923") // 疑似洗錢交易訪談記錄檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L8923 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8923.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryRecordService sMlaundryRecordService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8923 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";

		int iRecordDateStart = this.parse.stringToInteger(titaVo.getParam("RecordDateStart"));
		int iRecordDateEnd = this.parse.stringToInteger(titaVo.getParam("RecordDateEnd"));
		int iFRecordDateStart = iRecordDateStart + 19110000;
		int iFRecordDateEnd = iRecordDateEnd + 19110000;
		this.info("L8923 iFRecordDate : " + iFRecordDateStart + "~" + iFRecordDateEnd);

		int iActualRepayDateStart = this.parse.stringToInteger(titaVo.getParam("ActualRepayDateStart"));
		int iActualRepayDateEnd = this.parse.stringToInteger(titaVo.getParam("ActualRepayDateEnd"));
		int iFActualRepayDateStart = iActualRepayDateStart + 19110000;
		int iFActualRepayDateEnd = iActualRepayDateEnd + 19110000;
		this.info("L8923 iFRepayDate : " + iFActualRepayDateStart + "~" + iFActualRepayDateEnd);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 292 * 100 = 29,200

		// 查詢疑似洗錢交易訪談記錄檔檔
		Slice<MlaundryRecord> slMlaundryRecord;
		if(iRecordDateStart==0 && iRecordDateEnd==0) {
			slMlaundryRecord = sMlaundryRecordService.findRepayD(iFActualRepayDateStart, iFActualRepayDateEnd, this.index, this.limit, titaVo);
		} else {
			slMlaundryRecord = sMlaundryRecordService.findRecordD(iFRecordDateStart, iFRecordDateEnd, this.index, this.limit, titaVo);
		}
		
		
		
		List<MlaundryRecord> lMlaundryRecord = slMlaundryRecord == null ? null : slMlaundryRecord.getContent();

		if (lMlaundryRecord == null || lMlaundryRecord.size() == 0) {
			throw new LogicException(titaVo, "E0001", "疑似洗錢交易訪談記錄檔"); // 查無資料
		}
		// 如有找到資料
		for (MlaundryRecord tMlaundryRecord : lMlaundryRecord) {
			OccursList occursList = new OccursList();

			// 查詢客戶資料主檔
			CustMain tCustMain = new CustMain();
			tCustMain = sCustMainService.custNoFirst(tMlaundryRecord.getCustNo(), tMlaundryRecord.getCustNo(), titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
			}
			occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名

			occursList.putParam("OORecordDate", tMlaundryRecord.getRecordDate()); // 訪談日期
			occursList.putParam("OOCustNo", tMlaundryRecord.getCustNo()); // 戶號
			occursList.putParam("OOFacmNo", tMlaundryRecord.getFacmNo()); // 額度編號
			occursList.putParam("OOBormNo", tMlaundryRecord.getBormNo()); // 撥款序號
			occursList.putParam("OORepayDate", tMlaundryRecord.getRepayDate()); // 預定還款日期
			occursList.putParam("OOActualRepayDate", tMlaundryRecord.getActualRepayDate()); // 實際還款日期
			occursList.putParam("OORepayAmt", tMlaundryRecord.getRepayAmt()); // 還款金額
			occursList.putParam("OOCareer", tMlaundryRecord.getCareer()); // 職業別
			occursList.putParam("OOIncome", tMlaundryRecord.getIncome()); // 年收入(萬)
			occursList.putParam("OORepaySource", tMlaundryRecord.getRepaySource()); // 還款來源
			occursList.putParam("OORepayBank", tMlaundryRecord.getRepayBank()); // 代償銀行
			occursList.putParam("OODescription", tMlaundryRecord.getDescription().replace("$n", "")); // 其他說明
			occursList.putParam("OOEmpNo", tMlaundryRecord.getLastUpdateEmpNo()); // 經辦

			DateTime = this.parse.timeStampToString(tMlaundryRecord.getLastUpdate()); // 異動日期
			this.info("L8923 DateTime : " + DateTime);
			Date = FormatUtil.left(DateTime, 9);
			occursList.putParam("OOUpdate", Date);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slMlaundryRecord != null && slMlaundryRecord.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
