package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R18")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R18 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R18.class);

	/* DB服務注入 */
	@Autowired
	public RelsMainService sRelsMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R18 ");
		this.totaVo.init(titaVo);

		// tita值 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// tita值 統編
		String iRelsId = titaVo.getParam("RimRelsId");

		// new table
		RelsMain tRelsMain = new RelsMain();
		// 測試該統編是否有資料
		tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);

		// 處理邏輯
		if (iFunCd == 1 && tRelsMain != null) {
			// 若為新增，但資料已存在，拋錯
			throw new LogicException("E0002", "L2R18(RelsMain)");
		} else if (tRelsMain == null) {
			switch (iFunCd) {
			case 1: {
				// 若為新增且資料不存在，存空值到totaVo
				tRelsMain = new RelsMain();
				break;
			}
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "L2R18(RelsMain)");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "L2R18(RelsMain)");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "L2R18(RelsMain)");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "L2R18(RelsMain)");
			}

		}

		/* 存入Tota */
		/* key 名稱需與L2R18.tom相同 */
		this.totaVo.putParam("2r18RelsId", tRelsMain.getRelsId());
		this.totaVo.putParam("2r18RelsName", tRelsMain.getRelsName());
		this.totaVo.putParam("2r18RelsCode", tRelsMain.getRelsCode());
		this.totaVo.putParam("2r18RelsType", tRelsMain.getRelsType());

		this.addList(this.totaVo);
		return this.sendList();
	}
}