package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsId;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L2418")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2418 extends TradeBuffer {

	@Autowired
	public ClOtherRightsService sClOtherRightsService;
	@Autowired
	public ClMainService sClMainService;
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;

	// 功能
	private int iFunCd;
	// 擔保品代號1
	private int iClCode1;
	// 擔保品代號2
	private int iClCode2;
	// 擔保品編號
	private int iClNo;
	// 他項權利序號
	private String iClSeq;

	private boolean isEloan = false;

	private ClOtherRights tClOtherRights = new ClOtherRights();
	private ClOtherRightsId tClOtherRightsId = new ClOtherRightsId();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2418 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// 功能
		iFunCd = parse.stringToInteger(titaVo.getParam("FUNCIND"));
		// 擔保品代號1
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		// 擔保品代號2
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		// 擔保品編號
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		// 他項權利序號
		iClSeq = titaVo.getParam("ClSeq");

		ClMainId ClMainId = new ClMainId();
		ClMain tClMain = new ClMain();
		new ArrayList<ClOtherRights>();

		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		tClMain = sClMainService.findById(ClMainId, titaVo);
		if (tClMain == null) {
			throw new LogicException("E2003", "擔保品編號不存在擔保品主檔");
		}

		tClOtherRightsId = new ClOtherRightsId();
		tClOtherRightsId.setClCode1(iClCode1);
		tClOtherRightsId.setClCode2(iClCode2);
		tClOtherRightsId.setClNo(iClNo);
		tClOtherRightsId.setSeq(iClSeq);

		tClOtherRights = sClOtherRightsService.holdById(tClOtherRightsId, titaVo);

		if (isEloan && iFunCd == 1 && tClOtherRights != null) {
			iFunCd = 2;
		}

		if (tClOtherRights == null) {
			if (iFunCd == 1) {
				tClOtherRights = new ClOtherRights();
				tClOtherRights.setClOtherRightsId(tClOtherRightsId);


				setClOtherRights(titaVo);

				try {
					sClOtherRightsService.insert(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品他項權利檔" + e.getErrorMsg());
				}
			} else {
				throw new LogicException(titaVo, "E0001", "擔保品他項權利檔   擔保品編號" + iClCode1 + "-" + iClCode2 + "-" + iClNo + "  他項權利序號:" + iClSeq); // 查無資料
			}

		} else {
			if (iFunCd == 1) { // 新增
				throw new LogicException(titaVo, "E0002", "擔保品他項權利檔   擔保品編號" + iClCode1 + "-" + iClCode2 + "-" + iClNo + "  他項權利序號:" + iClSeq); // 新增資料已存在
			} else if (iFunCd == 2) { // 修改


				// 變更前
				ClOtherRights beforeClOtherRights = (ClOtherRights) dataLog.clone(tClOtherRights);


				setClOtherRights(titaVo);

				try {
					tClOtherRights = sClOtherRightsService.update2(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品他項權利檔" + e.getErrorMsg());
				}

				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClOtherRights, tClOtherRights);
				dataLog.exec();
			} else if (iFunCd == 4) { // 刪除
//				tClOtherRights = sClOtherRightsService.holdById(tClOtherRightsId, titaVo);
				try {
					dataLog.setEnv(titaVo, tClOtherRights, tClOtherRights);
					dataLog.exec("刪除擔保品他項權利資料");
					sClOtherRightsService.delete(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品他項權利檔" + e.getErrorMsg());
				}
			} else if (iFunCd == 5) {

			} else {
				throw new LogicException(titaVo, "E0010", "擔保品他項權利檔"); // 功能選擇錯誤

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setClOtherRights(TitaVo titaVo) throws LogicException {
		tClOtherRights.setCity(titaVo.getParam("City"));
		tClOtherRights.setOtherCity(titaVo.getParam("InCityX"));
		tClOtherRights.setLandAdm(titaVo.getParam("LandAdm"));
		tClOtherRights.setOtherLandAdm(titaVo.getParam("InLandAdmX"));
		tClOtherRights.setRecYear(parse.stringToInteger(titaVo.getParam("RecYear")));
		tClOtherRights.setRecWord(titaVo.getParam("RecWord"));
		tClOtherRights.setOtherRecWord(titaVo.getParam("InRecWordX"));
		tClOtherRights.setRecNumber(titaVo.getParam("RecNumber"));
		tClOtherRights.setRightsNote(titaVo.getParam("RightsNote"));
		tClOtherRights.setSecuredTotal(parse.stringToBigDecimal(titaVo.getParam("TimSecuredTotal")));

	}
}