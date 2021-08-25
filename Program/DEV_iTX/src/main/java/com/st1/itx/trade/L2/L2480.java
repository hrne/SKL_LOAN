package com.st1.itx.trade.L2;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.ClEva;
import com.st1.itx.db.domain.ClEvaId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.ClEvaService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunCd=9,1<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * EvaDate=9,7<br>
 * EvaAmt=9,14.2<br>
 * EvaCompanyId=X,24<br>
 * EvaCompanyName=X,50<br>
 * EvaEmpno=X,6<br>
 * EvaReason=9,2<br>
 */

@Service("L2480")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2480 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClEvaService sClEvaService;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	@Autowired
	LoanAvailableAmt loanAvailableAmt;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;
	BigDecimal shareTotal = BigDecimal.ZERO;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2480 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int iEvaNo = parse.stringToInteger(titaVo.getParam("EvaNo"));

		ClMain tClMain = new ClMain();

		ClMainId ClMainId = new ClMainId();
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		// 檢查該擔保品編號是否存在擔保品主檔
		tClMain = sClMainService.findById(ClMainId, titaVo);

		// 該擔保品編號不存在 拋錯
		if (tClMain == null) {
			throw new LogicException("E0001", "L2480該擔保品編號不存在擔保品主檔(ClMain)");
		}

		// 組ClEva PK
		ClEva tClEva = new ClEva();
		ClEvaId tClEvaId = new ClEvaId();
		tClEvaId.setClCode1(iClCode1);
		tClEvaId.setClCode2(iClCode2);
		tClEvaId.setClNo(iClNo);
		tClEvaId.setEvaNo(iEvaNo);

		int newEvaNo = 1;
		int oEvaNo = iEvaNo;
		// 抓當前序號 EvaNo desc
		tClEva = sClEvaService.ClNoFirst(iClCode1, iClCode2, iClNo);

		// FunCd = 1 新增 3複製
		if (iFunCd == 1 || iFunCd == 3) {

			if (tClEva != null) { // 新增時，若存在則抓最新的序號+1
				newEvaNo += tClEva.getEvaNo();
				oEvaNo = newEvaNo;
			}

			tClEvaId = new ClEvaId();
			tClEvaId.setClCode1(iClCode1);
			tClEvaId.setClCode2(iClCode2);
			tClEvaId.setClNo(iClNo);
			tClEvaId.setEvaNo(newEvaNo);

			tClEva = new ClEva();
			tClEva.setClEvaId(tClEvaId);
			tClEva.setClCode1(iClCode1);
			tClEva.setClCode2(iClCode2);
			tClEva.setClNo(iClNo);
			tClEva.setEvaNo(newEvaNo);
			tClEva.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			tClEva.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
			tClEva.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
			tClEva.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));
			tClEva.setEvaCompanyId(titaVo.getParam("EvaCompanyId"));
			tClEva.setEvaCompanyName(titaVo.getParam("EvaCompanyName"));
			tClEva.setEvaEmpno(titaVo.getParam("EvaEmpno"));
			tClEva.setEvaReason(parse.stringToInteger(titaVo.getParam("EvaReason")));
			tClEva.setOtherReason(titaVo.getParam("EvaReasonX").trim());

			CheckAmt(titaVo);

			try {
				sClEvaService.insert(tClEva);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品重評資料檔");
			}

			// 變更前
			ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);

			tClMain.setShareTotal(shareTotal);
			tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));

			ClImm t = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
			if (t == null) {
				throw new LogicException("E0001", "該擔保品編號不存在擔保品不動產檔 =" + iClCode1 + -+iClCode2 + -+iClNo);
			}
			if ("1".equals(t.getClStat()) || "2".equals(t.getSettingStat())) {
				tClMain.setShareTotal(BigDecimal.ZERO);
			} else {
				tClMain.setShareTotal(shareTotal);
			}
			try {
				tClMain = sClMainService.update2(tClMain);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品主檔");
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeClMain, tClMain);
			dataLog.exec();

			// ClImm
			ClImm tClImm = new ClImm();

			tClImm = sClImmService.holdById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
			// 變更前
			ClImm beforeClImm = (ClImm) dataLog.clone(tClImm);

			tClImm.setEvaCompanyCode(titaVo.getParam("EvaCompanyId"));
			// 評估淨值
			tClImm.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
			// 出租評估淨值
			tClImm.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));
			;

			try {
				tClImm = sClImmService.update(tClImm);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品不動產檔");
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeClImm, tClImm);
			dataLog.exec();

			// FunCd = 2 修改
		} else if (iFunCd == 2) {

			tClEva = sClEvaService.holdById(tClEvaId, titaVo);
			if (tClEva == null) {
				throw new LogicException("E0003", "L2480該擔保品編號不存在擔保品重評資料檔(ClEva)");
			}

			// 變更前
			ClEva beforeClEva = (ClEva) dataLog.clone(tClEva);

			tClEva.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
			tClEva.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
			tClEva.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
			tClEva.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));
			tClEva.setEvaCompanyId(titaVo.getParam("EvaCompanyId"));
			tClEva.setEvaCompanyName(titaVo.getParam("EvaCompanyName"));
			tClEva.setEvaEmpno(titaVo.getParam("EvaEmpno"));
			tClEva.setEvaReason(parse.stringToInteger(titaVo.getParam("EvaReason")));
			tClEva.setOtherReason(titaVo.getParam("EvaReasonX").trim());

			try {
				tClEva = sClEvaService.update2(tClEva);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品重評資料檔");
			}
			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeClEva, tClEva);
			dataLog.exec();

			// 如果為最後一筆序號才更新擔保品主檔與擔保品不動產檔
			// 最新的序號 == 目前序號
			if (tClEva.getEvaNo() == iEvaNo) {

				CheckAmt(titaVo);

				tClMain = sClMainService.holdById(ClMainId, titaVo);
				// 變更前
				ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);
				tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
				tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
				tClMain.setShareTotal(shareTotal);
				ClImm t = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
				if (t == null) {
					throw new LogicException("E0001", "該擔保品編號不存在擔保品不動產檔 =" + iClCode1 + -+iClCode2 + -+iClNo);
				}
				if ("1".equals(t.getClStat()) || "2".equals(t.getSettingStat())) {
					tClMain.setShareTotal(BigDecimal.ZERO);
				} else {
					tClMain.setShareTotal(shareTotal);
				}

				try {
					tClMain = sClMainService.update2(tClMain);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品主檔");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClMain, tClMain);
				dataLog.exec();

				// 組ClImm PK
				ClImm tClImm = new ClImm();

				tClImm = sClImmService.holdById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
				// 變更前
				ClImm beforeClImm = (ClImm) dataLog.clone(tClImm);

				tClImm.setEvaCompanyCode(titaVo.getParam("EvaCompanyId"));

				// 評估淨值
				tClImm.setEvaNetWorth(parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth")));
				// 出租評估淨值
				tClImm.setRentEvaValue(parse.stringToBigDecimal(titaVo.getParam("RentEvaValue")));
				;
				try {
					tClImm = sClImmService.update(tClImm);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產檔");
				}

				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClImm, tClImm);
				dataLog.exec();
			}
			// FunCd = 4 刪除
		} else if (iFunCd == 4) {
			tClEva = sClEvaService.holdById(tClEvaId);
			try {
				sClEvaService.delete(tClEva);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品重評資料檔");
			}

		}

		this.totaVo.putParam("OEvaNo", oEvaNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void CheckAmt(TitaVo titaVo) throws LogicException {

		/*
		 * loanToValue 貸放成數 shareTotal 可分配金額 settingAmt 設定金額 evaAmt 鑑估總價 shareAmtSum
		 * 計算同擔保品分配金額加總
		 */

		BigDecimal loanToValue = BigDecimal.ZERO;

		shareTotal = BigDecimal.ZERO;

		BigDecimal settingAmt = BigDecimal.ZERO;

		BigDecimal evaAmt = parse.stringToBigDecimal(titaVo.getParam("EvaAmt"));

		BigDecimal shareAmtSum = new BigDecimal(0);

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// 取貸放成數,設定金額
		ClImm tClImm = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
		loanToValue = tClImm.getLoanToValue();
		settingAmt = tClImm.getSettingAmt();
		BigDecimal shareCompAmt = BigDecimal.ZERO;
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkEvaAmt = parse.stringToBigDecimal(titaVo.getParam("EvaAmt"));
		BigDecimal wkEvaNetWorth = parse.stringToBigDecimal(titaVo.getParam("EvaNetWorth"));

		// 評估淨值有值時擺評估淨值,否則擺鑑估總值.
		if (wkEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
			shareCompAmt = wkEvaNetWorth;
		} else {
			shareCompAmt = wkEvaAmt;
		}

		shareTotal = shareCompAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0,
				BigDecimal.ROUND_HALF_UP);

		// 分配金額和設定金額比較 較低的為可分配金額
		this.info("分配金額和設定金額比較 = " + shareTotal + "," + settingAmt);
		if (settingAmt.compareTo(shareTotal) < 0) {
			shareTotal = settingAmt;
		}

		ClImm t = sClImmService.findById(new ClImmId(iClCode1, iClCode2, iClNo), titaVo);
		if (t == null) {
			throw new LogicException("E0001", "該擔保品編號不存在擔保品不動產檔 =" + iClCode1 + -+iClCode2 + -+iClNo);
		}
		if ("1".equals(t.getClStat()) || "2".equals(t.getSettingStat())) {
			shareTotal = BigDecimal.ZERO;
		}

		wkAvailable = loanAvailableAmt.checkClAvailable(iClCode1, iClCode2, iClNo, shareTotal, titaVo); // 可用額度

		if (wkAvailable.compareTo(BigDecimal.ZERO) < 0) {
			throw new LogicException("E3071", "可分配金額不足 ： = " + wkAvailable);
		}



		Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();

		// 加總
		if (lClFac != null && lClFac.size() > 0) {
			for (ClFac tmpClFac : lClFac) {
				shareAmtSum = shareAmtSum.add(tmpClFac.getShareAmt());
			}
		}
		this.info("擔保品關聯檔分配金額加總 = " + shareAmtSum);
		if (shareTotal.subtract(shareAmtSum).compareTo(BigDecimal.ZERO) < 0) {
			throw new LogicException("E2033", "評估總價*貸放成數 = " + shareTotal + "," + "擔保品與額度關聯檔的分配金額加總=" + shareAmtSum);
		}

	}
}