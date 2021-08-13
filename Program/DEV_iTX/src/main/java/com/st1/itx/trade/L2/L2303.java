package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsCompanyId;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsCompanyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RelsId=X,10<br>
 * FunCd=9,1<br>
 * CompanyId=9,8<br>
 * CompanyName=X,80<br>
 * HoldingRatio=9,3.2<br>
 * JobTitle=9,2<br>
 */

@Service("L2303")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2303 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2303.class);

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

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2303 ");
		this.totaVo.init(titaVo);

		// tita 功能1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// tita 統一編號CustId
		String iRelsId = titaVo.getParam("RelsId");
		// tita 相關事業統一編號
		String iCompanyId = titaVo.getParam("CompanyId");
		// new RelsCompany TABLE
		RelsCompany tRelsCompany = new RelsCompany();
		// new tRelsCompanyId PK TABLE
		RelsCompanyId tRelsCompanyId = new RelsCompanyId();

		// tita統編找RelsMain 取ReltUKey
		RelsMain tRelsMain = sRelsMainService.RelsIdFirst(iRelsId);
		if (tRelsMain == null) {

			throw new LogicException(titaVo, "E2003", "該統一編號" + iRelsId + "無關係人主檔資料"); // 查無資料

		}
		String relsukey = tRelsMain.getRelsUKey();
		// relsukey,CompanyId組PK
		tRelsCompanyId.setRelsUKey(relsukey);
		tRelsCompanyId.setCompanyId(iCompanyId);

		if (iFunCd == 1) {

			tRelsCompany = sRelsCompanyService.findById(tRelsCompanyId);
			if (tRelsCompany != null) {
				throw new LogicException(titaVo, "E0002",
						"L2303 該統編" + iRelsId + " 相關事業統編" + iCompanyId + "  已存在於利害關係人相關事業檔。"); // 新增資料已存在

			}
			tRelsCompany = new RelsCompany();
			// tita值,塞table
			tRelsCompany.setRelsCompanyId(tRelsCompanyId);
			tRelsCompany.setRelsUKey(relsukey);
			tRelsCompany.setCompanyId(iCompanyId);
			tRelsCompany.setCompanyName(titaVo.getParam("CompanyName"));
			tRelsCompany.setHoldingRatio(parse.stringToBigDecimal(titaVo.getParam("HoldingRatio")));
			tRelsCompany.setJobTitle(parse.stringToInteger(titaVo.getParam("JobTitle")));

			try {
				sRelsCompanyService.insert(tRelsCompany);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}

			// 修改
		} else if (iFunCd == 2) {
			// PK HOLD資料
			tRelsCompany = sRelsCompanyService.holdById(tRelsCompanyId);

			// 變更前
			RelsCompany beforeRelsCompany = (RelsCompany) dataLog.clone(tRelsCompany);

			tRelsCompany.setRelsCompanyId(tRelsCompanyId);
			tRelsCompany.setRelsUKey(relsukey);
			tRelsCompany.setCompanyId(iCompanyId);
			tRelsCompany.setCompanyName(titaVo.getParam("CompanyName"));
			tRelsCompany.setHoldingRatio(parse.stringToBigDecimal(titaVo.getParam("HoldingRatio")));
			tRelsCompany.setJobTitle(parse.stringToInteger(titaVo.getParam("JobTitle")));

			try {
				// 修改
				tRelsCompany = sRelsCompanyService.update2(tRelsCompany);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeRelsCompany, tRelsCompany);
			dataLog.exec();
		} else if (iFunCd == 4) {

			try {
				// PK找關係人主檔HOLD資料

				RelsCompany tRelsCompany4 = sRelsCompanyService.holdById(tRelsCompanyId);
				this.info(" L2303 tRelsCompany4" + tRelsCompany4);

				if (tRelsCompany4 != null) {
					sRelsCompanyService.delete(tRelsCompany4);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		} else if (iFunCd == 5) {

		} else {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}