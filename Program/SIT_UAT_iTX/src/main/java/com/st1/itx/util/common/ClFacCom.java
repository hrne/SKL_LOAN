package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;

import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

/**
 * 額度與擔保品關聯檔處理 <BR>
 * 
 * @author Lai
 * @version 1.0.0
 */
@Component("clFacCom")
@Scope("prototype")
public class ClFacCom extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;
	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public FacShareApplService facShareApplService;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 額度與擔保品關聯檔變動處理<BR>
	 * 1.更新額度與擔保品關聯檔的戶號、額度 <BR>
	 * 2.更新額度與擔保品關聯檔的主要擔保品記號 <BR>
	 * 3.更新額度主檔設定擔保品記號 4.自動建立共同借款人之額度與擔保品關聯檔
	 * 
	 * @param iApproveNo iApproveNo
	 * @param titaVo titaVo
	 * @throws LogicException ...
	 */
	public void changeClFac(int iApproveNo, TitaVo titaVo) throws LogicException {
		this.info("ClFacCom change ....." + iApproveNo);
		int custNo = 0;
		int facmNo = 0;
		int mainFlagIdx = 0;
		String colSetFlag = "N";
		FacMain tFacMain = sFacMainService.facmApplNoFirst(iApproveNo, titaVo);
		if (tFacMain != null) {
			custNo = tFacMain.getCustNo();
			facmNo = tFacMain.getFacmNo();
		}
		// 共同借款人自動建立額度與擔保品關聯檔
		facShareAppl(iApproveNo, titaVo);

		// 以相同核准號碼中,可分配金額最高者為主要擔保品
		Slice<ClFac> slClFac = sClFacService.approveNoEq(iApproveNo, this.index, this.limit);
		List<ClFac> lClFac = slClFac == null ? null : new ArrayList<ClFac>(slClFac.getContent());
		BigDecimal wkShareTotal = BigDecimal.ZERO;
		// 有資料才需檢查
		if (lClFac != null) {
			for (int i = 0; i < lClFac.size(); i++) {
				ClMain tClMain = sClMainService.findById(
						new ClMainId(lClFac.get(i).getClCode1(), lClFac.get(i).getClCode2(), lClFac.get(i).getClNo()),
						titaVo);
				if (tClMain.getShareTotal().compareTo(wkShareTotal) > 0) {
					mainFlagIdx = i;
				}
			}
			for (int i = 0; i < lClFac.size(); i++) {
				if (i == mainFlagIdx) {
					lClFac.get(i).setMainFlag("Y");
				} else {
					lClFac.get(i).setMainFlag("N");
				}
				lClFac.get(i).setCustNo(custNo);
				lClFac.get(i).setFacmNo(facmNo);
			}
			colSetFlag = "Y";
			try {
				sClFacService.updateAll(lClFac);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品與額度關聯檔");// 更新資料時，發生錯誤
			}
		}

		// 2.更新額度主檔設定擔保品記號
		if (tFacMain != null) {
			if (!tFacMain.getColSetFlag().equals(colSetFlag)) {
				tFacMain = sFacMainService.holdById(tFacMain, titaVo);
				tFacMain.setColSetFlag(colSetFlag);
				try {
					sFacMainService.update(tFacMain);
				} catch (DBException e) {
					throw new LogicException("E0007", "核准號碼 = " + iApproveNo);// 更新資料時，發生錯誤
				}
			}
		}

	}

	// 共同借款人自動建立額度與擔保品關聯檔
	private void facShareAppl(int iApproveNo, TitaVo titaVo) throws LogicException {
		FacShareAppl tFacShareAppl = facShareApplService.findById(iApproveNo, titaVo);
		if (tFacShareAppl == null) {
			return;
		}
		// 共同借款人全部
		Slice<FacShareAppl> slFacShareAppl = facShareApplService.findMainApplNo(tFacShareAppl.getMainApplNo(), 0,
				Integer.MAX_VALUE, titaVo);
		// 第一筆登錄的核准號碼，刪除自動的建立額度與擔保品關聯檔
		if (iApproveNo == tFacShareAppl.getMainApplNo()) {
			for (FacShareAppl facShareAppl : slFacShareAppl.getContent()) {
				if (facShareAppl.getApplNo() != facShareAppl.getMainApplNo()) {
					this.info("ClFac 1 deleteAll " + facShareAppl.getApplNo());
					Slice<ClFac> slClFac = sClFacService.approveNoEq(facShareAppl.getApplNo(), 0, Integer.MAX_VALUE);
					if (slClFac != null) {
						try {
							sClFacService.deleteAll(slClFac.getContent(), titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔");// 新增資料時，發生錯誤
						}
					}
				}
			}
		} else {
			// 刪除該核准號碼下額度與擔保品關聯檔
			this.info("ClFac 2  deleteAll " + iApproveNo);
			Slice<ClFac> slClFac = sClFacService.approveNoEq(iApproveNo, this.index, this.limit);
			if (slClFac != null) {
				try {
					sClFacService.deleteAll(slClFac.getContent(), titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔");// 新增資料時，發生錯誤
				}
			}
		}

		// 以第一筆登錄的核准號碼建立該核准號碼下額度與擔保品關聯檔
		Slice<ClFac> slClFacMain = sClFacService.approveNoEq(tFacShareAppl.getMainApplNo(), 0, Integer.MAX_VALUE);
		if (slClFacMain != null) {
			for (FacShareAppl facShareAppl : slFacShareAppl.getContent()) {
				for (ClFac clFac : slClFacMain.getContent()) {
					if (facShareAppl.getApplNo() != facShareAppl.getMainApplNo()) {
						shareApplClFac(clFac, facShareAppl, titaVo);
					}
				}
			}
		}
	}

	// 共同借款人自動建立
	private void shareApplClFac(ClFac clFac, FacShareAppl facShareAppl, TitaVo titaVo) throws LogicException {
		this.info("ClFac insert " + facShareAppl.toString() + clFac.toString());
		ClFacId tClFacId = new ClFacId();
		ClFac tClFac = (ClFac) dataLog.clone(clFac);
		tClFacId.setApproveNo(facShareAppl.getApplNo());
		tClFacId.setClCode1(clFac.getClCode1());
		tClFacId.setClCode2(clFac.getClCode2());
		tClFacId.setClNo(clFac.getClNo());
		tClFac.setClFacId(tClFacId);
		tClFac.setCustNo(facShareAppl.getCustNo());
		tClFac.setFacmNo(facShareAppl.getFacmNo());
		try {
			sClFacService.insert(tClFac, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "擔保品與額度關聯檔" + tClFacId.toString());// 新增資料時，發生錯誤
		}
	}

}