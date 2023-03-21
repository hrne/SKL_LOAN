package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsFac;
import com.st1.itx.db.domain.ClOtherRightsFacId;
import com.st1.itx.db.domain.ClOtherRightsId;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.ClOtherRightsFacService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
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
	public ClOtherRightsFacService sClOtherRightsFacService;
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public ClFacService sClFacService;
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	DateUtil dDateUtil;

	// 功能
	private int iFunCd;
	// 擔保品代號1
	private int iClCode1;
	// 擔保品代號2
	private int iClCode2;
	// 擔保品編號
	private int iClNo;
	// 戶號
//	private int iCustNo = 0;
	// 他項權利序號
	private String iClSeq;

	private boolean isEloan = false;
	private int iClOtherRightsFacL = 10;// Detail有幾個
	private int lClOtherRightsFacS = 0;

	private int iClOtherRightsFacApplNo[] = new int[iClOtherRightsFacL];// 核准編號
	private int slClOtherRightsFacApplNo[] = new int[iClOtherRightsFacL];// 原核准編號

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
		String sClSeq = titaVo.getParam("ClSeq").trim();//
		iClSeq = titaVo.getParam("ClSeq");// 20230315改為文字,刪除戶號
		if (sClSeq.length() < 8 && iFunCd == 1) {
			throw new LogicException(titaVo, "E0015", "他項權利序號格式錯誤，須為9999-999，輸入值=" + sClSeq);
		}

		String iMrkey = titaVo.getParam("ClCode1") + titaVo.getParam("ClCode2") + titaVo.getParam("ClNo") + iClSeq;

//		iClSeq = parse.stringToInteger(titaVo.getParam("ClSeq"));
//		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		titaVo.putParam("MRKEY", iMrkey);// 20230315把戶號改為擔保品號碼+序號

//20230315點掉不維護		
//		ClMainId ClMainId = new ClMainId();
//		ClMain tClMain = new ClMain();
//		new ArrayList<ClOtherRights>();

//		ClMainId.setClCode1(iClCode1);
//		ClMainId.setClCode2(iClCode2);
//		ClMainId.setClNo(iClNo);

//		tClMain = sClMainService.findById(ClMainId, titaVo);
//		if (tClMain == null) {
//			throw new LogicException("E2003", "擔保品編號不存在擔保品主檔");
//		}
//		tClMain = sClMainService.holdById(tClMain, titaVo);
//		tClMain.setLastClOtherSeq(iClSeq);

//		try {
//			sClMainService.update(tClMain, titaVo);
//		} catch (DBException e) {
//			throw new LogicException("E0005", "擔保品主檔" + e.getErrorMsg());
//		}

		if (isEloan && iFunCd == 1 && tClOtherRights != null) {
			iFunCd = 2;
		}

		// 2023/3/19新增-檢查輸入之核准號碼
		checkApplNo(titaVo);

		tClOtherRightsId = new ClOtherRightsId();
		tClOtherRightsId.setClCode1(iClCode1);
		tClOtherRightsId.setClCode2(iClCode2);
		tClOtherRightsId.setClNo(iClNo);
		tClOtherRightsId.setSeq(iClSeq);

		tClOtherRights = sClOtherRightsService.holdById(tClOtherRightsId, titaVo);

		if (tClOtherRights == null) {
			if (iFunCd == 1) {
				// 輸入戶號下，檢查此擔保品是否與額度關聯

//				Boolean clfacFg = false;
//				if (iCustNo != 0) {
//					Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE,
//							titaVo);
//					if (slClFac == null) {
//						throw new LogicException(titaVo, "E0015", "此擔保品查無額度與擔保品關聯檔資料"); // 檢查錯誤
//					}
//
//					for (ClFac t : slClFac.getContent()) {
//						if (t.getCustNo() == iCustNo) {
//							clfacFg = true;
//							break;
//						}
//					}
//					if (!clfacFg) {
//						throw new LogicException(titaVo, "E0015", "此戶號與擔保品非關聯資料"); // 檢查錯誤
//					}
//				}

				tClOtherRights = new ClOtherRights();
				tClOtherRights.setClOtherRightsId(tClOtherRightsId);

				setClOtherRights(titaVo);

				try {
					sClOtherRightsService.insert(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品他項權利檔" + e.getErrorMsg());
				}
				updateClOtherRightsFac(titaVo);

			} else {
				throw new LogicException(titaVo, "E0001",
						"擔保品他項權利檔   擔保品編號" + iClCode1 + "-" + iClCode2 + "-" + iClNo + "  他項權利序號:" + iClSeq); // 查無資料
			}
		} else {
			if (iFunCd == 1) { // 新增
				throw new LogicException(titaVo, "E0002",
						"擔保品他項權利檔   擔保品編號" + iClCode1 + "-" + iClCode2 + "-" + iClNo + "  他項權利序號:" + iClSeq); // 新增資料已存在
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
				dataLog.exec("修改擔保品他項權利檔");
				updateClOtherRightsFac(titaVo);

			} else if (iFunCd == 4) { // 刪除
//				tClOtherRights = sClOtherRightsService.holdById(tClOtherRightsId, titaVo);
				try {
					dataLog.setEnv(titaVo, tClOtherRights, tClOtherRights);
					dataLog.exec("刪除擔保品他項權利資料" + iClCode1 + "-" + parse.IntegerToString(iClCode2, 2) + "-"
							+ parse.IntegerToString(iClNo, 7));
					sClOtherRightsService.delete(tClOtherRights, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品他項權利檔" + e.getErrorMsg());
				}
				updateClOtherRightsFac(titaVo);
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
		// tClOtherRights.setCustNo(iCustNo);
		tClOtherRights.setSecuredDate(parse.stringToInteger(titaVo.getParam("SecuredDate")));// 擔保債權確定日期
		tClOtherRights.setLocation(titaVo.getParam("Location"));// 建物坐落地號

	}

	/**
	 * 01.檢核 核准編號與擔保品關聯
	 */
	public void checkApplNo(TitaVo titaVo) throws LogicException {

		for (int i = 0; i < iClOtherRightsFacL; i++) {
			int Row = i + 1;
			iClOtherRightsFacApplNo[i] = parse.stringToInteger(titaVo.getParam("ApplNo" + Row + "")); // 核准編號
			if (iClOtherRightsFacApplNo[i] != 0) {
				int iApplNo = iClOtherRightsFacApplNo[i];
				ClFac tClFac = sClFacService.findById(new ClFacId(iClCode1, iClCode2, iClNo, iApplNo));
				if (tClFac == null) {
					throw new LogicException("E0015", "此核准編號與擔保品非關聯資料，核准編號:" + iApplNo); // 查無資料
				}
			}
		}
		// 資料庫原始值
		Slice<ClOtherRightsFac> slClOtherRightsFac = sClOtherRightsFacService.findClNoSeq(iClCode1, iClCode2, iClNo,
				iClSeq, this.index, this.limit, titaVo);
		List<ClOtherRightsFac> lClOtherRightsFac = slClOtherRightsFac == null ? null : slClOtherRightsFac.getContent();
		if (lClOtherRightsFac != null && lClOtherRightsFac.size() != 0) {
			lClOtherRightsFacS = lClOtherRightsFac.size();
			this.info("L2418 lClOtherRightsFacS=" + lClOtherRightsFacS);
			for (int i = 0; i < lClOtherRightsFacS; i++) {
				ClOtherRightsFac ClOtherRightsFaceVO = lClOtherRightsFac.get(i);
				slClOtherRightsFacApplNo[i] = ClOtherRightsFaceVO.getApproveNo();// 核准編號
			}
		}

	}

	public void updateClOtherRightsFac(TitaVo titaVo) throws LogicException {

		ClOtherRightsFac tClOtherRightsFac = new ClOtherRightsFac();
		ClOtherRightsFacId tClOtherRightsFacId = new ClOtherRightsFacId();
		int iCustNo = 0;
		int iFacmNo = 0;
		tClOtherRightsFac = new ClOtherRightsFac();
		tClOtherRightsFacId.setClCode1(iClCode1);
		tClOtherRightsFacId.setClCode2(iClCode2);
		tClOtherRightsFacId.setClNo(iClNo);
		tClOtherRightsFacId.setSeq(iClSeq);

		// ELOAN上送一律先刪除本日以前同KEY值的關聯檔ClOtherRightsFac資料
		if (isEloan) {
			for (int i = 0; i < lClOtherRightsFacS; i++) {// 原資料庫筆數
				int slApplNo = slClOtherRightsFacApplNo[i];// 原資料庫-核准號碼
				ClFac tClFac = sClFacService.findById(new ClFacId(iClCode1, iClCode2, iClNo, slApplNo), titaVo);
				if (tClFac != null) {
					iCustNo = tClFac.getCustNo();
					iFacmNo = tClFac.getFacmNo();
					tClOtherRightsFacId.setCustNo(iCustNo);
					tClOtherRightsFacId.setFacmNo(iFacmNo);
					tClOtherRightsFac.setClOtherRightsFacId(tClOtherRightsFacId);
					tClOtherRightsFac = sClOtherRightsFacService.findById(tClOtherRightsFacId, titaVo);
					if (tClOtherRightsFac != null && parse
							.stringToInteger(parse.timeStampToStringDate(tClOtherRightsFac.getLastUpdate())) < dDateUtil
									.getNowIntegerForBC()) {
						tClOtherRightsFac = sClOtherRightsFacService.holdById(tClOtherRightsFacId, titaVo);
						try {
							sClOtherRightsFacService.delete(tClOtherRightsFac, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0008", "擔保品他項權利額度關聯檔" + e.getErrorMsg());
						}
					}
				}
			}
		}
		// 上送資料-新增或刪除
		for (int i = 0; i < iClOtherRightsFacL; i++) {
			int iApplNo = iClOtherRightsFacApplNo[i];// tita上送核准編號(ELAON一次只有一筆)
			if (iApplNo > 0) {
				ClFac tClFac = sClFacService.findById(new ClFacId(iClCode1, iClCode2, iClNo, iApplNo), titaVo);
				if (tClFac != null) {
					iCustNo = tClFac.getCustNo();
					iFacmNo = tClFac.getFacmNo();
					tClOtherRightsFacId.setCustNo(iCustNo);
					tClOtherRightsFacId.setFacmNo(iFacmNo);
					tClOtherRightsFac.setClOtherRightsFacId(tClOtherRightsFacId);
					tClOtherRightsFac = sClOtherRightsFacService.findById(tClOtherRightsFacId, titaVo);
					if (tClOtherRightsFac == null) {// 無關聯檔一律新增
						tClOtherRightsFac = new ClOtherRightsFac();
						tClOtherRightsFac.setClOtherRightsFacId(tClOtherRightsFacId);
						tClOtherRightsFac.setApproveNo(iApplNo);
						try {
							sClOtherRightsFacService.insert(tClOtherRightsFac, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "擔保品他項權利額度關聯檔" + e.getErrorMsg());
						}
					}
					if (tClOtherRightsFac != null && iFunCd == 4) {
						tClOtherRightsFac = sClOtherRightsFacService.holdById(tClOtherRightsFacId, titaVo);
						ClOtherRightsFac beforeClOtherRightsFac = (ClOtherRightsFac) dataLog.clone(tClOtherRightsFac);
						try {
							sClOtherRightsFacService.delete(tClOtherRightsFac, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0008", "擔保品他項權利額度關聯檔" + e.getErrorMsg());
						}
						// 紀錄變更前變更後
						dataLog.setEnv(titaVo, beforeClOtherRightsFac, tClOtherRightsFac);
						dataLog.exec("刪除擔保品他項權利額度關聯檔-核准編號:" + iApplNo);
					}
				}
			}
		}

		if (iFunCd == 2 && !isEloan) {
			for (int i = 0; i < lClOtherRightsFacS; i++) {// 原資料庫筆數
				int slApplNo = slClOtherRightsFacApplNo[i];// 原資料庫值
				boolean findfg = false;
				for (int j = 0; j < iClOtherRightsFacL; j++) {// tita上送筆數
					int iApplNo = iClOtherRightsFacApplNo[j];
					if (slApplNo == iApplNo) {
						findfg = true;
						continue;
					}
				}
				if (!findfg) {// 比對原核准編號是否存在tita上送之核准編號,不存在則代表刪除
					ClFac tClFac = sClFacService.findById(new ClFacId(iClCode1, iClCode2, iClNo, slApplNo), titaVo);
					if (tClFac != null) {
						iCustNo = tClFac.getCustNo();
						iFacmNo = tClFac.getFacmNo();
						tClOtherRightsFacId.setCustNo(iCustNo);
						tClOtherRightsFacId.setFacmNo(iFacmNo);
						tClOtherRightsFac.setClOtherRightsFacId(tClOtherRightsFacId);
						tClOtherRightsFac = sClOtherRightsFacService.findById(tClOtherRightsFacId, titaVo);
						if (tClOtherRightsFac != null) {
							tClOtherRightsFac = sClOtherRightsFacService.holdById(tClOtherRightsFacId, titaVo);
							ClOtherRightsFac beforeClOtherRightsFac = (ClOtherRightsFac) dataLog
									.clone(tClOtherRightsFac);
							try {
								sClOtherRightsFacService.delete(tClOtherRightsFac, titaVo);
							} catch (DBException e) {
								throw new LogicException("E0008", "擔保品他項權利額度關聯檔" + e.getErrorMsg());
							}
							// 紀錄變更前變更後
							titaVo.putParam("FUNCIND", 4);// 變更記號以便寫TxDataLog,做完須改回原值
							dataLog.setEnv(titaVo, beforeClOtherRightsFac, tClOtherRightsFac);
							dataLog.exec("刪除擔保品他項權利額度關聯檔-核准編號:" + slApplNo);
							titaVo.putParam("FUNCIND", iFunCd);// TxDataLog:做完須改回原值
						}
					}
					continue;
				}
			}
		}
	}

}