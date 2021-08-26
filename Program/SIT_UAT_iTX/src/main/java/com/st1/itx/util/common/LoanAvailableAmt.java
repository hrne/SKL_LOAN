package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacShareLimit;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareLimitService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * 1.caculate 取得額度可用餘額<BR>
 * 2.caculateCl 取得擔保品可用餘額<BR>
 * 
 * @author st1
 *
 */
@Component("loanAvailableAmt")
@Scope("prototype")
public class LoanAvailableAmt extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public ClMainService clMainService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public FacShareLimitService facShareLimitService;

	BigDecimal availableAmt = BigDecimal.ZERO; // 可用額度
	BigDecimal availableFac = BigDecimal.ZERO; // 可用額度(額度)
	BigDecimal availableCl = BigDecimal.ZERO; // 可用額度(擔保品)
	BigDecimal availableShare = BigDecimal.ZERO; // 可用額度(合併額度控管)
	String clShareFlag = ""; // Y-擔保品可分配金額小於額度核准金額
	String facShareFlag = ""; // Y-有合併額度控管
	String limitFlag = ""; // 限額計算方式 F-額度, C-擔保品 S-合併額度控管
	int mainApplNo = 0;
	List<ClFac> lClFac = new ArrayList<ClFac>();

	private void init() {
		this.availableAmt = BigDecimal.ZERO;
		this.availableFac = BigDecimal.ZERO;
		this.availableCl = BigDecimal.ZERO;
		this.availableShare = BigDecimal.ZERO;
		this.clShareFlag = "";
		this.facShareFlag = "";
		this.limitFlag = "";
		this.mainApplNo = 0;
		lClFac = new ArrayList<ClFac>();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	/**
	 * 可用額度計算=Min<BR>
	 * 1.核准額度限額「核准額度限額 - 已動用額度」<BR>
	 * 2.合併額度控管限額「總額度 - 已動用額度總額」<BR>
	 * 3.擔保品可使用額度「擔保品可分配金額 - 撥款餘額」<BR>
	 * 
	 * @param tFacMain 額度檔
	 * @param titaVo   ..
	 * @return 可用額度
	 * @throws LogicException ..
	 */
	public BigDecimal caculate(FacMain tFacMain, TitaVo titaVo) throws LogicException {
		this.info("LoanAvailableAmt caculate ... ");
		init();
		// 核准額度限額
		this.limitFlag = "F"; // 限額計算方式 F-核准額度
		if (tFacMain.getLineAmt().compareTo(tFacMain.getUtilBal()) > 0) {
			this.availableFac = tFacMain.getLineAmt().subtract(tFacMain.getUtilBal());
			this.availableAmt = this.availableFac;
		}

		// 可用額度(合併額度控管)
		FacShareLimit tFacShareLimit = facShareLimitService.findById(tFacMain.getApplNo(), titaVo);
		if (tFacShareLimit != null) {
			this.facShareFlag = "Y";
			this.mainApplNo = tFacShareLimit.getMainApplNo();
			this.availableShare = compShareAvailable(tFacMain.getRecycleCode(), tFacShareLimit, titaVo);
			if (this.availableShare.compareTo(this.availableAmt) < 0) {
				this.limitFlag = "S"; // 限額計算方式 S-合併額度控管
				this.availableAmt = this.availableShare;
			}
		}

		// 可用額度(擔保品)
		this.availableCl = compClAvailable(tFacMain, titaVo);
		// 擔保品共用記號 1-擔保品可使用小於額度可使用金額, 2-無關聯擔保品
		if (this.availableCl.compareTo(this.availableAmt) < 0) {
			if (lClFac.size() == 0) {
				this.clShareFlag = "2"; 
			} else {
				this.clShareFlag = "1"; 
			}
			this.limitFlag = "C"; // C-擔保品
			this.availableAmt = this.availableCl;
		}

		this.info("caculate end ");
		this.info("核准額度 = " + tFacMain.getLineAmt());
		this.info("已動用額度 = " + tFacMain.getUtilBal());
		this.info("可用額度(額度)= " + this.availableFac);
		this.info("合併額度控管Y/N  = " + this.facShareFlag);
		this.info("可用額度(合併額度控管)= " + this.availableShare);
		this.info("擔保品共用記號 1-擔保品可使用小於額度可使用金額, 2-無關聯擔保品 = " + this.clShareFlag); 
		this.info("可用額度(擔保品)= " + this.availableCl);
		this.info("限額計算方式 F-核准額度, C-擔保品 S-合併額度控管 = " + this.limitFlag);
		this.info("可用額度 = " + this.availableAmt);

		return this.availableAmt;
	}

	// 取得擔保品限額的可使用額度
	private BigDecimal compClAvailable(FacMain tFacMain, TitaVo titaVo) throws LogicException {
		this.info("compClAvailable ... ");
		// 擔保品可使用額度 = 可分配金額 - 撥款餘額
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkUtilAmt = BigDecimal.ZERO;
		// 本額度的所有擔保品
		Slice<ClFac> slClFac = clFacService.approveNoEq(tFacMain.getApplNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slClFac != null) {
			for (ClFac t : slClFac.getContent()) {
				t.setFacShareFlag(0);
				t.setShareAmt(BigDecimal.ZERO);
				lClFac.add(t);
			}
		}

		// 同擔保品的其他額度
		for (ClFac t : new ArrayList<>(lClFac)) {
			getShareFac(t, titaVo);
		}

		// 擔保品的可分配金額
		for (ClFac t : lClFac) {
			if (t.getFacShareFlag() == 0) {
				ClMain tClMain = clMainService.findById(new ClMainId(t.getClCode1(), t.getClCode2(), t.getClNo()),
						titaVo);
				if (tClMain != null) {
					t.setShareAmt(tClMain.getShareTotal());
				}
				t.setFacShareFlag(1);
				for (ClFac c : lClFac) {
					if (c.getClCode1() == t.getClCode1() && c.getClCode2() == t.getClCode2()
							&& c.getClNo() == t.getClNo()) {
						c.setFacShareFlag(1);
					}
				}
			}
		}

		for (ClFac t : lClFac) {
			this.info("compCl 1 = " + t.toString());
		}

//	 可使用額度 = 可分配金額 - 撥款餘額

		// 計算其他額度、非本額度擔保品的占用
		for (ClFac t : lClFac) {
			if (t.getFacShareFlag() == 1 && t.getApproveNo() != tFacMain.getApplNo()) {
				FacMain t1FacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				wkUtilAmt = wkUtilAmt.add(t1FacMain.getUtilAmt());// 撥款餘額
				this.info("wkUtilAmt 1 = " + wkUtilAmt + ", " + t.toString());
				for (ClFac c : lClFac) {
					if (c.getApproveNo() == t1FacMain.getApplNo()) {
						c.setFacShareFlag(2);
					}
					if (c.getApproveNo() != tFacMain.getApplNo()) {
						if (wkUtilAmt.compareTo(t.getShareAmt()) > 0) {
							t.setShareAmt(BigDecimal.ZERO);
							wkUtilAmt = wkUtilAmt.subtract(t.getShareAmt());
						} else {
							t.setShareAmt(t.getShareAmt().subtract(wkUtilAmt));
							wkUtilAmt = BigDecimal.ZERO;

						}
					}
				}
			}
			this.info("wkUtilAmt 1 end = " + wkUtilAmt + ", " + t.toString());
		}
		// 計算本額度、本額度擔保品的占用
		for (ClFac t : lClFac) {
			if (t.getFacShareFlag() == 1 && t.getApproveNo() == tFacMain.getApplNo()) {
				FacMain t1FacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				wkUtilAmt = wkUtilAmt.add(t1FacMain.getUtilAmt());// 撥款餘額
				this.info("wkUtilAmt 2 = " + wkUtilAmt + ", " + t.toString());
				for (ClFac c : lClFac) {
					if (c.getApproveNo() == t1FacMain.getApplNo()) {
						c.setFacShareFlag(2);
					}
					if (c.getApproveNo() == tFacMain.getApplNo()) {
						if (wkUtilAmt.compareTo(t.getShareAmt()) > 0) {
							t.setShareAmt(BigDecimal.ZERO);
							wkUtilAmt = wkUtilAmt.subtract(t.getShareAmt());
						} else {
							t.setShareAmt(t.getShareAmt().subtract(wkUtilAmt));
							wkUtilAmt = BigDecimal.ZERO;
						}
					}
				}
			}
			this.info("wkUtilAmt 2 end = " + wkUtilAmt + ", " + t.toString());
		}

		wkAvailable = BigDecimal.ZERO.subtract(wkUtilAmt);

		// 計算本額度擔保品的可使用額度
		for (ClFac t : lClFac) {
			if (t.getApproveNo() == tFacMain.getApplNo()) {
				wkAvailable = wkAvailable.add(t.getShareAmt());
				this.info(" wkAvailable  " + wkAvailable + ", " + t.toString());
			}
		}
		return wkAvailable;
	}

	// 同擔保品的其他額度
	private void getShareFac(ClFac tClFac, TitaVo titaVo) throws LogicException {
		this.info("getShareFac = " + tClFac.toString());
		boolean newFac = true;
		List<ClFac> lClFacTmp = new ArrayList<ClFac>();
		Slice<ClFac> slClFac = clFacService.clNoEq(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo(), 0,
				Integer.MAX_VALUE, titaVo);
		if (slClFac != null) {
			for (ClFac t : slClFac.getContent()) {
				newFac = true;
				for (ClFac c : lClFac) {
					if (c.getApproveNo() == t.getApproveNo() && c.getClCode1() == t.getClCode1()
							&& c.getClCode2() == t.getClCode2() && c.getClNo() == t.getClNo()) {
						newFac = false;
					}
				}
				if (newFac) {
					t.setFacShareFlag(0);
					t.setShareAmt(BigDecimal.ZERO);
					lClFacTmp.add(t);
				}
			}
		}
		lClFac.addAll(lClFacTmp);

		// 其他額度的其他擔保擔保品
		for (ClFac t : lClFacTmp) {
			getShareCl(t, titaVo);
		}
	}

	// 其他額度的其他擔保擔保品
	private void getShareCl(ClFac tClFac, TitaVo titaVo) throws LogicException {
		this.info("getShareCl = " + tClFac.toString());
		boolean newCl = true;
		List<ClFac> lClFacTmp = new ArrayList<ClFac>();
		Slice<ClFac> slClFac = clFacService.approveNoEq(tClFac.getApproveNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slClFac != null) {
			for (ClFac t : slClFac.getContent()) {
				newCl = true;
				for (ClFac c : lClFac) {
					if (c.getApproveNo() == t.getApproveNo() && c.getClCode1() == t.getClCode1()
							&& c.getClCode2() == t.getClCode2() && c.getClNo() == t.getClNo()) {
						newCl = false;
					}
				}
				if (newCl) {
					t.setFacShareFlag(0);
					t.setShareAmt(BigDecimal.ZERO);
					lClFacTmp.add(t);
				}
			}
		}
		lClFac.addAll(lClFacTmp);
		for (ClFac t : lClFacTmp) {
			getShareFac(t, titaVo);
		}
	}

	// 取得合併額度控管的可使用額度
	private BigDecimal compShareAvailable(String iRecycleCode, FacShareLimit tFacShareLimit, TitaVo titaVo)
			throws LogicException {
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkUtilBal = BigDecimal.ZERO;
		Slice<FacShareLimit> slFacShareLimit = facShareLimitService.findMainApplNoEq(tFacShareLimit.getMainApplNo(), 0,
				Integer.MAX_VALUE, titaVo);
		List<FacShareLimit> lFacShareLimit = slFacShareLimit == null ? null : slFacShareLimit.getContent();
		if (lFacShareLimit != null) {
			for (FacShareLimit t : lFacShareLimit) {
				FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "額度主檔" + t.getCustNo() + "-" + t.getFacmNo()); // 查詢資料不存在
				}
				wkUtilBal = wkUtilBal.add(tFacMain.getUtilBal());
			}
		}

		if (tFacShareLimit.getLineAmt().compareTo(wkUtilBal) > 0) {
			wkAvailable = tFacShareLimit.getLineAmt().subtract(wkUtilBal);
		}
		this.info("總額度(合併額度控管)= " + tFacShareLimit.getLineAmt());
		this.info("已動用額度(合併額度控管)= " + wkUtilBal);
		this.info("可用額度(合併額度控管)= " + wkAvailable);
		return wkAvailable;

	}

	// 取得擔保品限額的可使用額度
	public BigDecimal checkClAvailable(int iClCode1, int iClCode2, int iClNo, BigDecimal iShareTotal, TitaVo titaVo)
			throws LogicException {
		this.info("checkClAvailable ... ");
		// 擔保品可使用額度 = 可分配金額 - 撥款餘額
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkUtilAmt = BigDecimal.ZERO;
		// 本擔保品下的全部額度
		Slice<ClFac> slClFac = clFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		if (slClFac != null) {
			for (ClFac t : slClFac.getContent()) {
				t.setFacShareFlag(0);
				t.setShareAmt(BigDecimal.ZERO);
				lClFac.add(t);
			}
		}

		// 所有額度下的其他擔保品
		for (ClFac t : new ArrayList<>(lClFac)) {
			getShareCl(t, titaVo);
		}

		// 擔保品的可分配金額
		for (ClFac t : lClFac) {
			if (t.getFacShareFlag() == 0) {
				if (iClCode1 == t.getClCode1() && iClCode2 == t.getClCode2() && iClNo == t.getClNo()) {
					t.setShareAmt(iShareTotal);
				} else {
					ClMain tClMain1 = clMainService.findById(new ClMainId(t.getClCode1(), t.getClCode2(), t.getClNo()),
							titaVo);
					t.setShareAmt(tClMain1.getShareTotal());
					t.setFacShareFlag(1);
				}
				for (ClFac c : lClFac) {
					if (c.getClCode1() == t.getClCode1() && c.getClCode2() == t.getClCode2()
							&& c.getClNo() == t.getClNo()) {
						c.setFacShareFlag(1);
					}
				}
			}
		}

		for (ClFac t : lClFac) {
			this.info("checkCl 1 = " + t.toString());
		}

//	 可使用額度 = 可分配金額 - 撥款餘額

		// 計算其他擔保品、非本擔保品額度的占用
		for (ClFac t : lClFac) {
			if (t.getFacShareFlag() == 1
					&& (t.getClCode1() != iClCode1 || t.getClCode2() != iClCode2 || t.getClNo() != iClNo)) {
				FacMain t1FacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				wkUtilAmt = wkUtilAmt.add(t1FacMain.getUtilAmt());// 撥款餘額
				this.info("wkUtilAmt 1 = " + wkUtilAmt + ", " + t.toString());
				for (ClFac c : lClFac) {
					if (c.getApproveNo() == t1FacMain.getApplNo()) {
						c.setFacShareFlag(2);
					}
					if (t.getClCode1() != iClCode1 || t.getClCode2() != iClCode2 || t.getClNo() != iClNo) {
						if (wkUtilAmt.compareTo(t.getShareAmt()) > 0) {
							t.setShareAmt(BigDecimal.ZERO);
							wkUtilAmt = wkUtilAmt.subtract(t.getShareAmt());
						} else {
							t.setShareAmt(t.getShareAmt().subtract(wkUtilAmt));
							wkUtilAmt = BigDecimal.ZERO;
						}
					}
				}
			}
			this.info("wkUtilAmt 1 end = " + wkUtilAmt + ", " + t.toString());
		}
		// 計算本擔保品、本擔保品額度的占用
		for (ClFac t : lClFac) {
			if (t.getFacShareFlag() == 1
					&& (t.getClCode1() == iClCode1 && t.getClCode2() == iClCode2 && t.getClNo() == iClNo)) {
				FacMain t1FacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				wkUtilAmt = wkUtilAmt.add(t1FacMain.getUtilAmt());// 撥款餘額
				this.info("wkUtilAmt 2 = " + wkUtilAmt + ", " + t.toString());
				for (ClFac c : lClFac) {
					if (c.getApproveNo() == t1FacMain.getApplNo()) {
						c.setFacShareFlag(2);
					}
					if (t.getClCode1() == iClCode1 && t.getClCode2() == iClCode2 && t.getClNo() == iClNo) {
						if (wkUtilAmt.compareTo(t.getShareAmt()) > 0) {
							wkUtilAmt = wkUtilAmt.subtract(t.getShareAmt());
							t.setShareAmt(BigDecimal.ZERO);
						} else {
							t.setShareAmt(t.getShareAmt().subtract(wkUtilAmt));
							wkUtilAmt = BigDecimal.ZERO;
						}
					}
				}
			}
			this.info("wkUtilAmt 2 end = " + wkUtilAmt + ", " + t.toString());
		}

		wkAvailable = BigDecimal.ZERO.subtract(wkUtilAmt);

		// 計算本額度擔保品的可使用額度
		for (ClFac t : lClFac) {
			if (t.getClCode1() == iClCode1 && t.getClCode2() == iClCode2 && t.getClNo() == iClNo) {
				wkAvailable = wkAvailable.add(t.getShareAmt());
				this.info(" wkAvailable  " + wkAvailable + ", " + t.toString());
			}
		}
		return wkAvailable;
	}

	/**
	 * 可用額度計算=Min(「核准額度限額 - 已動用額度」, 「合併額度控管總額度 - 已動用額度總額」,「擔保品可分配金額 - 撥款餘額」)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAvailableAmt() {
		return this.availableAmt;
	}

	/**
	 * 可用額度(額度)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAvailableFac() {
		return this.availableFac;
	}

	/**
	 * 可用額度(擔保品)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAvailableCl() {
		return availableCl;
	}

	/**
	 * 可用額度(合併額度控管)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAvailableShare() {
		return availableShare;
	}

	/**
	 * 擔保品共用記號 1-擔保品可使用小於額度可使用金額, 2-無關聯擔保品
	 * 
	 * @return Y/space
	 */
	public String getClShareFlag() {
		return this.clShareFlag;
	}

	/**
	 * Y-有合併額度控管
	 * 
	 * @return Y/space
	 */
	public String getFacShareFlag() {
		return this.facShareFlag;
	}

	/**
	 * 限額計算方式
	 * 
	 * @return F-核准額度, C-擔保品 S-合併額度控管
	 */
	public String getLimitFlag() {
		return this.limitFlag;
	}

}
