package com.st1.itx.trade.L2;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R31")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R31 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R31.class);

	/* DB服務注入 */
	@Autowired
	public CustDataCtrlService sCustDataCtrlService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R31 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 客戶統編
		String iCustId = titaVo.getParam("RimCustId");
		// new table
		CustMain tCustMain = new CustMain();
		CustDataCtrl tCustDataCtrl = new CustDataCtrl();

		// 客戶統編找客戶主檔戶號
		tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "該統編不存在客戶主檔");
		}
		int custNo = tCustMain.getCustNo();
		String custUKet = tCustMain.getCustUKey();

		/* 取得日曆日日期及現在時間 */
		Timestamp date = parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime());

		if (iFunCd == 1) {

			this.totaVo.putParam("L2r31CustNo", custNo);
			this.totaVo.putParam("L2r31CustName", tCustMain.getCustName());
			this.totaVo.putParam("L2r31CreateEmpNo", titaVo.getTlrNo());
			this.totaVo.putParam("L2r31CreateDate", dateUtil.getNowIntegerForBC() - 19110000);
			this.totaVo.putParam("L2r31CreateTime", dateUtil.getNowIntegerTime());
			this.info("tlrno = " + titaVo.getTlrNo());

		} else {

			tCustDataCtrl = sCustDataCtrlService.findById(custNo, titaVo);
			if (tCustDataCtrl == null) {
				throw new LogicException(titaVo, "E2003", "結清戶個資控管檔");
			}
			String txDate4 = "";
			String txTime2 = "";
			if (tCustDataCtrl.getCreateDate() != null) {
				String txDate = tCustDataCtrl.getCreateDate().toString();
				this.info("txDate L2073 " + txDate);
				String txDate2 = txDate.substring(0, 4) + txDate.substring(5, 7) + txDate.substring(8, 10);
				int txDate3 = parse.stringToInteger(txDate2) - 19110000;
				txDate4 = String.valueOf(txDate3);
				txTime2 = txDate.substring(11, 13) + ":" + txDate.substring(14, 16) + ":" + txDate.substring(17, 19);
			}

			this.totaVo.putParam("L2r31CustNo", tCustDataCtrl.getCustNo());
			this.totaVo.putParam("L2r31CustName", tCustMain.getCustName());
			this.totaVo.putParam("L2r31CreateEmpNo", tCustDataCtrl.getLastUpdateEmpNo());
			this.totaVo.putParam("L2r31CreateDate", txDate4);
			this.totaVo.putParam("L2r31CreateTime", txTime2);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}