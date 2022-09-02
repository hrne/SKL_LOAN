package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimAcDate=9,7
 * RimCustNo=9,7
 */
@Service("L8R60") // 尋找疑似洗錢交易訪談記錄檔資料-戶號+實際償還日期-最新的一筆
@Scope("prototype")
/**
 *
 *
 * @author Linda
 * @version 1.0.0
 */
public class L8R60 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryRecordService sMlaundryRecordService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R60 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("RimAcDate"));
		int iFAcDate = iAcDate + 19110000;
		int iRimCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));

		// 檢查輸入資料
		if ( !(iRimCustNo > 0) ) {
			throw new LogicException(titaVo, "E0009", "L8R60"); // 戶號應有值
		}
		if ( !(iAcDate > 0) ) {
			throw new LogicException(titaVo, "E0009", "L8R60"); // 實際償還日期應有值
		}

		// 查詢疑似洗錢交易訪談記錄檔-for交易L8203登錄[經辦合理性說明]預設值
		MlaundryRecord tMlaundryRecord = sMlaundryRecordService.findCustNoAndActualRepayDateFirst(iRimCustNo,iFAcDate, iFAcDate,  titaVo);

		/* 如有找到資料 */
		if (tMlaundryRecord != null) {
			this.totaVo.putParam("L8R60RepaySource", tMlaundryRecord.getRepaySource()); // 還款來源
			this.totaVo.putParam("L8R60RepayBank", tMlaundryRecord.getRepayBank()); // 代償銀行
			this.totaVo.putParam("L8R60Description", tMlaundryRecord.getDescription().replace("$n", "\n")); // 其他說明
		} 

		this.addList(this.totaVo);
		return this.sendList();
	}

}