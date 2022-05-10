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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

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
		Long iLogNo = Long.parseLong(titaVo.get("RimLogNo"));
		
		MlaundryRecord mMlaundryRecord = sMlaundryRecordService.findById(iLogNo, titaVo);
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
		
		CustMain tCustMain = sCustMainService.custNoFirst(mMlaundryRecord.getCustNo(), mMlaundryRecord.getCustNo(), titaVo);
		
		if (tCustMain != null)
		{
			this.totaVo.putParam("L8R51CustName", tCustMain.getCustName()); // 戶名
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}