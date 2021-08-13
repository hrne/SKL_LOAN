package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2301")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2301 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2301.class);

	/* DB服務注入 */
	@Autowired
	public RelsMainService sRelsMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2301 ");
		this.totaVo.init(titaVo);

		// tita 功能 1新增 2修改 4刪除 5 查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// tita 統編
		String iRelsId = titaVo.getParam("RelsId");

		RelsMain tRelsMain = new RelsMain();

		// 測試 統編是否存在利害關係人主檔
		tRelsMain = sRelsMainService.RelsIdFirst(iRelsId);

		if (iFunCd == 1) {
			if (tRelsMain != null) {
				throw new LogicException(titaVo, "E0002", "L2301 該統編" + iRelsId + "已存在於關係人主檔。");
			}
			tRelsMain = new RelsMain();
			// 產生一組新的識別碼
			tRelsMain.setRelsUKey(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
			tRelsMain.setRelsId(iRelsId);
			// tita值塞進table
			tRelsMain.setRelsName(titaVo.getParam("RelsName"));
			tRelsMain.setRelsCode(titaVo.getParam("RelsCode"));
			tRelsMain.setRelsType(parse.stringToInteger(titaVo.getParam("RelsType")));

			/* 存入DB */

			try {
				sRelsMainService.insert(tRelsMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		} else if (iFunCd == 2) {
			if (tRelsMain == null) {
				throw new LogicException(titaVo, "E0003", "L2301 該統編" + iRelsId + "不存在於關係人主檔。");
			}
			String RelsUKey = tRelsMain.getRelsUKey();

			// new table
			tRelsMain = new RelsMain();

			// RelsUKey找一筆資料 鎖定
			tRelsMain = sRelsMainService.holdById(RelsUKey);

			// 變更前
			RelsMain beforeRelsMain = (RelsMain) dataLog.clone(tRelsMain);

			tRelsMain.setRelsUKey(RelsUKey);
			tRelsMain.setRelsId(iRelsId);
			tRelsMain.setRelsName(titaVo.getParam("RelsName"));
			tRelsMain.setRelsCode(titaVo.getParam("RelsCode"));
			tRelsMain.setRelsType(parse.stringToInteger(titaVo.getParam("RelsType")));

			try {
				// 修改
				tRelsMain = sRelsMainService.update2(tRelsMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeRelsMain, tRelsMain);
			dataLog.exec();

		} else if (iFunCd == 4) {

			String RelsUKey = tRelsMain.getRelsUKey();
			// RelsUKey找一筆資料
			tRelsMain = sRelsMainService.holdById(RelsUKey);
			try {

				RelsMain deleteReltFamilyLog = sRelsMainService.holdById(RelsUKey);
				this.info(" L2301 deleteReltFamilyLog" + deleteReltFamilyLog);

				if (deleteReltFamilyLog != null) {
					sRelsMainService.delete(deleteReltFamilyLog);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}