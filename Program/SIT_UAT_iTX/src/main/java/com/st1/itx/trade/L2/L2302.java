package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsFamilyId;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2302")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2302 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2302.class);

	/* DB服務注入 */
	@Autowired
	public RelsMainService sRelsMainService;

	/* DB服務注入 */
	@Autowired
	public RelsFamilyService sRelsFamilyService;

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
		this.info("active L2302 ");
		this.totaVo.init(titaVo);

		// tita 功能 1新增 2修改 4刪除 5 查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// tita 統編
		String iRelsId = titaVo.getParam("RelsId");
		// tita 序號
		int iRelsSeq = parse.stringToInteger(titaVo.getParam("RelsSeq"));
		// new RelsMain TABLE
		RelsMain tRelsMain = new RelsMain();
		// new RelsFamily TABLE
		RelsFamily tRelsFamily = new RelsFamily();
		// new PK TABLE
		RelsFamilyId tRelsFamilyId = new RelsFamilyId();

		// 統編找利害關係人主檔取UKEY
		tRelsMain = sRelsMainService.RelsIdFirst(iRelsId);

		if (iFunCd == 1) {
			// 組PK TABLE
			String RelsUKey = tRelsMain.getRelsUKey();
			tRelsFamilyId.setRelsUKey(RelsUKey);
			tRelsFamilyId.setRelsSeq(iRelsSeq);
			// 測試是否存在利害關係人親屬檔
			tRelsFamily = sRelsFamilyService.findById(tRelsFamilyId);
			this.info("tRelsFamilyId L2302" + tRelsFamilyId);
			if (tRelsFamily != null) {
				throw new LogicException(titaVo, "E0002", "L2302 該統編" + iRelsId + "已存在於關係人主檔。");
			}

			// new tRelsFamily
			tRelsFamily = new RelsFamily();
			// pk 塞table
			tRelsFamily.setRelsFamilyId(tRelsFamilyId);
			// tita值塞進table
			tRelsFamily.setRelsUKey(RelsUKey);
			tRelsFamily.setRelsSeq(iRelsSeq);
			tRelsFamily.setFamilyCode(parse.stringToInteger(titaVo.getParam("FamilyCode")));
			tRelsFamily.setFamilyCallCode(titaVo.getParam("FamilyCallCode"));
			tRelsFamily.setFamilyId(titaVo.getParam("FamilyId"));
			tRelsFamily.setFamilyName(titaVo.getParam("FamilyName"));

			/* 存入DB */

			try {
				sRelsFamilyService.insert(tRelsFamily);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// 修改
		} else if (iFunCd == 2) {

			// new PK table塞PK
			tRelsFamilyId = new RelsFamilyId();
			// new tRelsFamily
			tRelsFamily = new RelsFamily();
			// 組PK TABLE
			String RelsUKey = tRelsMain.getRelsUKey();

			// 塞值到TablePK
			tRelsFamilyId.setRelsUKey(RelsUKey);
			tRelsFamilyId.setRelsSeq(iRelsSeq);
			// PK找關係人主檔HOLD資料
			tRelsFamily = sRelsFamilyService.holdById(tRelsFamilyId);
			// 變更前
			RelsFamily beforeRelsFamily = (RelsFamily) dataLog.clone(tRelsFamily);

			// tita值塞進table
			tRelsFamily.setRelsUKey(RelsUKey);
			tRelsFamily.setRelsSeq(iRelsSeq);
			tRelsFamily.setFamilyCode(parse.stringToInteger(titaVo.getParam("FamilyCode")));
			tRelsFamily.setFamilyCallCode(titaVo.getParam("FamilyCallCode"));
			tRelsFamily.setFamilyId(titaVo.getParam("FamilyId"));
			tRelsFamily.setFamilyName(titaVo.getParam("FamilyName"));

			try {
				// 修改
				tRelsFamily = sRelsFamilyService.update2(tRelsFamily);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeRelsFamily, tRelsFamily);
			dataLog.exec();
			// 刪除
		} else if (iFunCd == 4) {

			// 組PK TABLE
			String RelsUKey = tRelsMain.getRelsUKey();
			// new PK table塞PK
			tRelsFamilyId = new RelsFamilyId();
			// 塞值到TablePK
			tRelsFamilyId.setRelsUKey(RelsUKey);
			tRelsFamilyId.setRelsSeq(iRelsSeq);

			try {
				// PK找關係人主檔HOLD資料
				tRelsFamily = sRelsFamilyService.holdById(tRelsFamilyId);
				RelsFamilyId RelsUKeyId = tRelsFamily.getRelsFamilyId();

				RelsFamily deleteRelsFamilyLog = sRelsFamilyService.holdById(RelsUKeyId);
				this.info(" L2302 deleteRelsFamilyLog" + deleteRelsFamilyLog);

				if (deleteRelsFamilyLog != null) {
					sRelsFamilyService.delete(deleteRelsFamilyLog);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}

			// 查詢
		} else if (iFunCd == 5) {

		} else {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}