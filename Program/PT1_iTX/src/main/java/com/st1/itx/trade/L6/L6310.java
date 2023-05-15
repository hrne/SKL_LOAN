package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.domain.TxHolidayId;
import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.trade.LC.LC800;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

/**
 * Tita FuncCode=9,1 Country=X,2 #loop{times:100,i:1} Holiday=9,7 TypeCode=9,1
 * #end END=X,1
 */

@Service("L6310")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6310 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxHolidayService sTxHolidayService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Autowired
	SendRsp sendRsp;

	@Autowired
	LC800 lC800;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6310 ");
		this.totaVo.init(titaVo);

		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String funcd = titaVo.getParam("FuncCode").trim();
		String iCountry = titaVo.getParam("Country").trim();
		int iHoliday;

		// 交易需主管核可
		if (!(funcd.equals("5"))) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
		}

		// 新增
		if (funcd.equals("1")) {

			for (int i = 1; i <= 150; i++) {

				this.info("L6310 Holiday 1 : " + titaVo.getParam("Holiday" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("Holiday" + i).equals("0000000") || titaVo.getParam("Holiday" + i) == null || titaVo.getParam("Holiday" + i).trim().isEmpty()) {
					break;
				}

				TxHoliday tTxHoliday = new TxHoliday();
				TxHolidayId tTxHolidayId = new TxHolidayId();

				tTxHolidayId.setCountry(titaVo.getParam("Country"));
				tTxHolidayId.setHoliday(this.parse.stringToInteger(titaVo.getParam("Holiday" + i)));
				tTxHoliday.setTxHolidayId(tTxHolidayId);

				tTxHoliday.setCountry(titaVo.getParam("Country"));
				tTxHoliday.setHoliday(this.parse.stringToInteger(titaVo.getParam("Holiday" + i)));
				tTxHoliday.setTypeCode(this.parse.stringToInteger(titaVo.getParam("TypeCode" + i)));
				tTxHoliday.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tTxHoliday.setCreateEmpNo(titaVo.getTlrNo());
				tTxHoliday.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tTxHoliday.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					sTxHolidayService.insert(tTxHoliday, titaVo);
				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", titaVo.getParam("Holiday" + i)); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		} else if (funcd.equals("2")) {

			for (int i = 1; i <= 150; i++) {

				iHoliday = this.parse.stringToInteger(titaVo.getParam("Holiday" + i));
				iHoliday = iHoliday + 19110000;

				this.info("L6310 Holiday 2 : " + titaVo.getParam("Holiday" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("Holiday" + i).equals("0000000") || titaVo.getParam("Holiday" + i) == null || titaVo.getParam("Holiday" + i).trim().isEmpty()) {
					break;
				}

				TxHoliday tTxHoliday = new TxHoliday();
				tTxHoliday = sTxHolidayService.holdById(new TxHolidayId(iCountry, iHoliday));
				if (tTxHoliday == null) {
					throw new LogicException(titaVo, "E0003", titaVo.getParam("Holiday" + i)); // 修改資料不存在
				}

				TxHoliday tTxHoliday2 = (TxHoliday) dataLog.clone(tTxHoliday); ////
				try {
					tTxHoliday.setCountry(titaVo.getParam("Country"));
					tTxHoliday.setHoliday(this.parse.stringToInteger(titaVo.getParam("Holiday" + i)));
					tTxHoliday.setTypeCode(this.parse.stringToInteger(titaVo.getParam("TypeCode" + i)));
					tTxHoliday.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
					tTxHoliday.setLastUpdateEmpNo(titaVo.getTlrNo());

					tTxHoliday = sTxHolidayService.update2(tTxHoliday, titaVo); ////
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tTxHoliday2, tTxHoliday); ////
				dataLog.exec("修改特殊/例假日"); ////

			}

		} else if (funcd.equals("4")) {

			for (int i = 1; i <= 150; i++) {

				iHoliday = this.parse.stringToInteger(titaVo.getParam("Holiday" + i));
				iHoliday = iHoliday + 19110000;

				this.info("L6310 Holiday 4 : " + titaVo.getParam("Holiday" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("Holiday" + i).equals("0000000") || titaVo.getParam("Holiday" + i) == null || titaVo.getParam("Holiday" + i).trim().isEmpty()) {
					break;
				}

				TxHoliday tTxHoliday = new TxHoliday();
				tTxHoliday = sTxHolidayService.holdById(new TxHolidayId(iCountry, iHoliday));

				if (tTxHoliday != null) {
					try {
						sTxHolidayService.delete(tTxHoliday);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", titaVo.getParam("Holiday" + i)); // 刪除資料不存在
				}
				dataLog.setEnv(titaVo, tTxHoliday, tTxHoliday); ////
				dataLog.exec("刪除特殊/例假日"); ////
			}

		} else if (!(funcd.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L6310"); // 功能選擇錯誤
		}

		// Adam 更新完假日檔需更新營業日
		this.lC800.proc(titaVo, "N2ONLINE", this.lC800.proc(titaVo, "NONLINE", this.lC800.proc(titaVo, "ONLINE", titaVo.getEntDy()).getNbsDy() + "").getNbsDy() + "");
		this.addList(this.totaVo);
		return this.sendList();
	}
}