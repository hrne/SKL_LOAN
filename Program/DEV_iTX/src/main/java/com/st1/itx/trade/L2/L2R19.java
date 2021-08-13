package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsFamilyId;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R19")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R19 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R19.class);

	/* DB服務注入 */
	@Autowired
	public RelsFamilyService sRelsFamilyService;

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
		this.info("active L2R19 ");
		this.totaVo.init(titaVo);

		// tita 功能1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// tita 序號 RimRelsSeq
		int iRelsSeq = parse.stringToInteger(titaVo.getParam("RimRelsSeq"));

		// tita 統一編號 RimRelsId
		String iRelsId = titaVo.getParam("RimRelsId");

		if (iFunCd == 1) {

			// tita統編找關係人親屬檔序號排序最大第一筆
			RelsMain tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);
			// 測試iReltId統編是否存在利害關係人主檔
			if (tRelsMain == null) {
				throw new LogicException(titaVo, "E2003", "該統一編號" + iRelsId + "無關係人主檔資料"); // 查無資料
			}
			String reltukey = tRelsMain.getRelsUKey();
			RelsFamily tRelsFamily = sRelsFamilyService.maxRelsSeqFirst(reltukey, titaVo);
			if (tRelsFamily == null) {

				this.totaVo.putParam("L2r19RelsSeq", 0 + 1);
				this.totaVo.putParam("L2r19FamilyId", "");
				this.totaVo.putParam("L2r19FamilyName", "");
				this.totaVo.putParam("L2r19FamilyCode", "");
				this.totaVo.putParam("L2r19FamilyCallCode", "");
			} else {
				int relsSeq = tRelsFamily.getRelsSeq();
				// put回tota並排序+1
				this.totaVo.putParam("L2r19RelsSeq", relsSeq + 1);
				this.totaVo.putParam("L2r19FamilyId", "");
				this.totaVo.putParam("L2r19FamilyName", "");
				this.totaVo.putParam("L2r19FamilyCode", "");
				this.totaVo.putParam("L2r19FamilyCallCode", "");

			}
			// 排序最大一筆資料之序號欄位

		} else if (iFunCd == 2) {
			// new PKtable
			RelsFamilyId tRelsFamilyId = new RelsFamilyId();
			// new table
			RelsFamily tRelsFamily = new RelsFamily();

			// RelId找relukey找關係人親屬檔資料
			RelsMain tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);
			String relsukey = tRelsMain.getRelsUKey();
			tRelsFamilyId.setRelsUKey(relsukey);
			tRelsFamilyId.setRelsSeq(iRelsSeq);
			tRelsFamily = sRelsFamilyService.holdById(tRelsFamilyId, titaVo);
			if (tRelsFamily == null) {
				throw new LogicException(titaVo, "E2003", "L2R19（準）利害關係人親屬檔尚無資料,請選擇新增功能");
			}
			this.totaVo.putParam("L2r19RelsSeq", tRelsFamily.getRelsSeq());
			this.totaVo.putParam("L2r19FamilyId", tRelsFamily.getFamilyId());
			this.totaVo.putParam("L2r19FamilyName", tRelsFamily.getFamilyName());
			this.totaVo.putParam("L2r19FamilyCode", tRelsFamily.getFamilyCode());
			this.totaVo.putParam("L2r19FamilyCallCode", tRelsFamily.getFamilyCallCode());

		} else if (iFunCd == 4) {
			// new PKtable
			RelsFamilyId tRelsFamilyId = new RelsFamilyId();
			// new table
			RelsFamily tRelsFamily = new RelsFamily();

			// RelId找relukey找關係人親屬檔資料
			RelsMain tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);
			String relsukey = tRelsMain.getRelsUKey();
			tRelsFamilyId.setRelsUKey(relsukey);
			tRelsFamilyId.setRelsSeq(iRelsSeq);
			tRelsFamily = sRelsFamilyService.holdById(tRelsFamilyId, titaVo);
			if (tRelsFamily == null) {
				throw new LogicException(titaVo, "E2003", "L2R19（準）利害關係人親屬檔尚無資料");
			}
			this.totaVo.putParam("L2r19RelsSeq", tRelsFamily.getRelsSeq());
			this.totaVo.putParam("L2r19FamilyId", tRelsFamily.getFamilyId());
			this.totaVo.putParam("L2r19FamilyName", tRelsFamily.getFamilyName());
			this.totaVo.putParam("L2r19FamilyCode", tRelsFamily.getFamilyCode());
			this.totaVo.putParam("L2r19FamilyCallCode", tRelsFamily.getFamilyCallCode());
		} else if (iFunCd == 5) {
			// new PKtable
			RelsFamilyId tRelsFamilyId = new RelsFamilyId();
			// new table
			RelsFamily tRelsFamily = new RelsFamily();

			// RelId找relukey找關係人親屬檔資料
			RelsMain tRelsMain = sRelsMainService.RelsIdFirst(iRelsId, titaVo);
			String relsukey = tRelsMain.getRelsUKey();
			tRelsFamilyId.setRelsUKey(relsukey);
			tRelsFamilyId.setRelsSeq(iRelsSeq);
			tRelsFamily = sRelsFamilyService.holdById(tRelsFamilyId, titaVo);
			if (tRelsFamily == null) {
				throw new LogicException(titaVo, "E2003", "L2R19（準）利害關係人親屬檔尚無資料");
			}
			this.totaVo.putParam("L2r19RelsSeq", tRelsFamily.getRelsSeq());
			this.totaVo.putParam("L2r19FamilyId", tRelsFamily.getFamilyId());
			this.totaVo.putParam("L2r19FamilyName", tRelsFamily.getFamilyName());
			this.totaVo.putParam("L2r19FamilyCode", tRelsFamily.getFamilyCode());
			this.totaVo.putParam("L2r19FamilyCallCode", tRelsFamily.getFamilyCallCode());
		} else {

		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}