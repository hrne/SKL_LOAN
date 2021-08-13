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
import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsCompanyId;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsCompanyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R20")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R20 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R20.class);

	/* DB服務注入 */
	@Autowired
	public RelsCompanyService sRelsCompanyService;

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
		this.info("active L2R20 ");
		this.totaVo.init(titaVo);

		// tita 統編
		String iRelsId = titaVo.getParam("RimRelsId");
		// 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 相關事業統編
		String iCompanyId = titaVo.getParam("RimCompanyId");

		// 利害關係人主檔找ReltUKey
		RelsMain tRelsMain = new RelsMain();
		tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);
		String ReltUKey = tRelsMain.getRelsUKey();
		// new PK TABLE
		RelsCompanyId tRelsCompanyId = new RelsCompanyId();
		// new TABLE
		RelsCompany tRelsCompany = new RelsCompany();
		// 組RelsCompanyId PK (ReltUKey,iCompanyId)
		tRelsCompanyId.setRelsUKey(ReltUKey);
		tRelsCompanyId.setCompanyId(iCompanyId);
		tRelsCompany = sRelsCompanyService.findById(tRelsCompanyId, titaVo);
		// 處理邏輯
		if (iFunCd == 1 && tRelsCompany != null) {
			// 若為新增，但資料已存在，拋錯
			throw new LogicException("E0002", "L2R20(RelsCompany)");
		} else if (tRelsCompany == null) {
			switch (iFunCd) {
			case 1: {
				// 若為新增且資料不存在，存空值到totaVo
				tRelsCompany = new RelsCompany();
				break;
			}
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "L2R20(RelsCompany)");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "L2R20(RelsCompany)");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "L2R20(RelsCompany)");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "L2R20(RelsCompany)");
			}

		}
		// UKEY找利害關係人主檔RelsId
		tRelsMain = sRelsMainService.findById(ReltUKey, titaVo);
		/* 存入Tota */
		/* key 名稱需與L2R20.tom相同 */
		this.totaVo.putParam("L2r20RelsId", tRelsMain.getRelsId());
		this.totaVo.putParam("L2r20CompanyId", tRelsCompany.getCompanyId());
		this.totaVo.putParam("L2r20CompanyName", tRelsCompany.getCompanyName());
		this.totaVo.putParam("L2r20HoldingRatio", tRelsCompany.getHoldingRatio());
		this.totaVo.putParam("L2r20JobTitle", tRelsCompany.getJobTitle());

		this.addList(this.totaVo);
		return this.sendList();
	}
}