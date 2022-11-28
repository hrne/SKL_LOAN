package com.st1.itx.trade.L5;

import java.util.ArrayList;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.domain.NegFinAcct;
/*DB服務*/
import com.st1.itx.db.service.NegFinAcctService;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
 * FinCode=9,3<br>
 * RemitBank=9,3<br>
 * RemitAcct=9,16<br>
 * DataSendSection=9,3<br>
 */

@Service("L5703")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5703 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegFinAcctService sNegFinAcctService;

	/* 日期工具 */
	@Autowired
	DateUtil dDateUtil;

	/* 轉型共用工具 */
	@Autowired
	Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("Run L5703");
		this.info("active L5703 ");

		this.totaVo.init(titaVo);

		String FunCode = titaVo.getParam("FunCode").trim(); // 功能
		String FinCode = titaVo.getParam("FinCode").trim(); // 債權機構
		String FinCodeX = titaVo.getParam("FinCodeX").trim(); // 債權機構名稱
		String RemitBank = titaVo.getParam("RemitBank").trim(); // 匯款銀行
		String RemitAcct = titaVo.getParam("RemitAcct").trim(); // 債協匯款帳號
		String RemitAcct2 = titaVo.getParam("RemitAcct2").trim(); // 調解匯款帳號
		String RemitAcct3 = titaVo.getParam("RemitAcct3").trim(); // 更生匯款帳號
		String RemitAcct4 = titaVo.getParam("RemitAcct4").trim(); // 清算匯款帳號
		String DataSendSection = titaVo.getParam("DataSendSection").trim(); // 資料傳送單位

		NegFinAcct NegFinAcctVO = new NegFinAcct();

		switch (FunCode) {

		case "1":
			NegFinAcctVO = sNegFinAcctService.findById(FinCode);

			if (NegFinAcctVO != null) {
				throw new LogicException(titaVo, "E0002", "債務協商債權機構帳戶檔");
			}

			NegFinAcct tNegFinAcct = new NegFinAcct();
			tNegFinAcct.setFinCode(FinCode);
			tNegFinAcct.setFinItem(FinCodeX);
			tNegFinAcct.setRemitBank(RemitBank);
			tNegFinAcct.setRemitAcct(RemitAcct);
			tNegFinAcct.setRemitAcct2(RemitAcct2);
			tNegFinAcct.setRemitAcct3(RemitAcct3);
			tNegFinAcct.setRemitAcct4(RemitAcct4);
			tNegFinAcct.setDataSendSection(DataSendSection);
			tNegFinAcct.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tNegFinAcct.setCreateEmpNo(titaVo.get("TlrNo"));
			tNegFinAcct.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tNegFinAcct.setLastUpdateEmpNo(titaVo.get("TlrNo"));
			try {
				sNegFinAcctService.insert(tNegFinAcct);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				// E0005 新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}

			break;

		case "2":

			NegFinAcctVO = sNegFinAcctService.holdById(FinCode);

			if (NegFinAcctVO == null) {
				throw new LogicException(titaVo, "E0003", "債務協商債權機構帳戶檔");
			}
			NegFinAcct beforeNegFinAcct = (NegFinAcct) dataLog.clone(NegFinAcctVO);
			NegFinAcctVO.setFinItem(FinCodeX);
			NegFinAcctVO.setRemitBank(RemitBank);
			NegFinAcctVO.setRemitAcct(RemitAcct);
			NegFinAcctVO.setRemitAcct2(RemitAcct2);
			NegFinAcctVO.setRemitAcct3(RemitAcct3);
			NegFinAcctVO.setRemitAcct4(RemitAcct4);
			NegFinAcctVO.setDataSendSection(DataSendSection);
			//NegFinAcctVO.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			//NegFinAcctVO.setLastUpdateEmpNo(titaVo.get("TlrNo"));

			try {
				sNegFinAcctService.update(NegFinAcctVO);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				// E0007 更新資料時，發生錯誤
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			dataLog.setEnv(titaVo, beforeNegFinAcct,NegFinAcctVO);
			dataLog.exec("修改債務協商債權機構帳戶檔");

			break;

		case "4":

			NegFinAcctVO = sNegFinAcctService.holdById(FinCode);

			if (NegFinAcctVO == null) {
				throw new LogicException(titaVo, "E0004", "債務協商債權機構帳戶檔");
			}

			try {
				sNegFinAcctService.delete(NegFinAcctVO);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			dataLog.setEnv(titaVo, NegFinAcctVO,NegFinAcctVO);
			dataLog.exec("刪除債務協商債權機構帳戶檔");

			break;

		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}