package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.MlaundryRecordId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimRecordDate=9,7
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNo=9,3
 */
@Service("L8R51") // 尋找疑似洗錢交易訪談記錄檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8R51 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R51.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryRecordService sMlaundryRecordService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L8R51 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		int iRimCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iRimFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iRimBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		int iRecordDate = this.parse.stringToInteger(titaVo.getParam("RimRecordDate"));
		int iFRecordDate = iRecordDate + 19110000;

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L8R51"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L8R51"); // 功能選擇錯誤
		}

		// 查詢客戶資料主檔
		CustMain tCustMain = new CustMain();
		tCustMain = sCustMainService.custNoFirst(iRimCustNo, iRimCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
		}

		// 初始值Tota
		moveTotaMlaundryRecord(new MlaundryRecord(), tCustMain.getCustName());

		// 查詢疑似洗錢交易訪談記錄檔
		MlaundryRecord tMlaundryRecord = sMlaundryRecordService.findById(new MlaundryRecordId(iFRecordDate, iRimCustNo, iRimFacmNo, iRimBormNo), titaVo);

		/* 如有找到資料 */
		if (tMlaundryRecord != null) {
			if (iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimCustNo")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaMlaundryRecord(tMlaundryRecord, tCustMain.getCustName());
			}
		} else {
			if (iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "疑似洗錢交易訪談記錄檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 疑似洗錢交易訪談記錄檔
	private void moveTotaMlaundryRecord(MlaundryRecord mMlaundryRecord, String mCustName) throws LogicException {

		this.totaVo.putParam("L8R51RecordDate", mMlaundryRecord.getRecordDate()); // 訪談日期
		this.totaVo.putParam("L8R51CustNo", mMlaundryRecord.getCustNo()); // 戶號
		this.totaVo.putParam("L8R51FacmNo", mMlaundryRecord.getFacmNo()); // 額度編號
		this.totaVo.putParam("L8R51BormNo", mMlaundryRecord.getBormNo()); // 撥款序號
		this.totaVo.putParam("L8R51RepayDate", mMlaundryRecord.getRepayDate()); // 預定還款日期
		this.totaVo.putParam("L8R51RepayAmt", mMlaundryRecord.getRepayAmt()); // 預定還款金額
		this.totaVo.putParam("L8R51ActualRepayDate", mMlaundryRecord.getActualRepayDate()); // 實際還款日期
		this.totaVo.putParam("L8R51ActualRepayAmt", mMlaundryRecord.getActualRepayAmt()); // 實際還款金額
		this.totaVo.putParam("L8R51Career", mMlaundryRecord.getCareer()); // 職業別
		this.totaVo.putParam("L8R51Income", mMlaundryRecord.getIncome()); // 年收入(萬)
		this.totaVo.putParam("L8R51RepaySource", mMlaundryRecord.getRepaySource()); // 還款來源
		this.totaVo.putParam("L8R51RepayBank", mMlaundryRecord.getRepayBank()); // 代償銀行
		this.totaVo.putParam("L8R51Description", mMlaundryRecord.getDescription().replace("$n", "\n")); // 其他說明
		this.totaVo.putParam("L8R51CustName", mCustName); // 戶名

	}

}