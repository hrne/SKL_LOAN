package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.domain.PfRewardMediaId;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

@Service("L5503")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5503 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5503.class);

	@Autowired
	public PfRewardMediaService pfRewardMediaService;

	@Autowired
	public TxControlService txControlService;
	
	@Autowired
	public DataLog dataLog;

	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5503 ");
		this.totaVo.init(titaVo);

		int workmonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		
		String controlCode = "L5511." + workmonth + ".2";
		TxControl txControl = txControlService.findById(controlCode, titaVo);
		if (txControl != null) {
			throw new LogicException(titaVo, "E0010", "已產生媒體檔");
		}
		
		String iFunCode = titaVo.get("FunCode").trim();
		if ("1".equals(iFunCode)) {
			int custNo = Integer.valueOf(titaVo.getParam("CustNo"));
			int facmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
			int bormNo = Integer.valueOf(titaVo.getParam("BormNo"));
			int bonusType = Integer.valueOf(titaVo.getParam("BonusType"));
			PfRewardMedia pfRewardMedia = pfRewardMediaService.findDupFirst(custNo, facmNo, bormNo, bonusType,workmonth, titaVo);
			if (pfRewardMedia != null) {
				String s = "";
				if (bonusType == 1) {
					s = "介紹人";
				} else if (bonusType == 1) {
					s = "介紹人";
				} else {
					s = "介紹人加碼";
				}
				throw new LogicException("E0002", "獎金資料");
			}
		}

		// 交易需主管核可
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "異動獎金資料");
		}

		long iLogNo = Long.valueOf(titaVo.get("BonusNo").trim());

		PfRewardMedia pfRewardMedia = pfRewardMediaService.holdById(iLogNo, titaVo);

		if (pfRewardMedia == null) {
			if ("1".equals(iFunCode)) {

				pfRewardMedia = new PfRewardMedia();

				pfRewardMedia = setValue(titaVo, pfRewardMedia);

				BigDecimal iBonus = new BigDecimal(titaVo.get("Bonus").trim());
				pfRewardMedia.setBonus(iBonus);
				pfRewardMedia.setAdjustBonus(pfRewardMedia.getBonus());
				pfRewardMedia.setAdjustBonusDate(0);
				pfRewardMedia.setManualFg(1);
				pfRewardMedia.setMediaFg(0);
				pfRewardMedia.setMediaDate(0);

				try {
					pfRewardMediaService.insert(pfRewardMedia);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "");
				}

			} else {
				throw new LogicException("E0001", "");
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException("E0002", "");
			} else {
				if (pfRewardMedia.getMediaFg() == 1) {
					throw new LogicException("EC008", "已產製媒體檔，不可修改或刪除");
				}
				if ("2".equals(iFunCode)) {
					PfRewardMedia pfRewardMedia2 = (PfRewardMedia) dataLog.clone(pfRewardMedia);

					if (pfRewardMedia.getManualFg() == 1) {
//						BigDecimal iBonus=new BigDecimal(titaVo.get("Bonus").trim());

						pfRewardMedia = setValue(titaVo, pfRewardMedia);
					}
					BigDecimal iBonus = new BigDecimal(titaVo.get("Bonus").trim());
					BigDecimal iAdjustBonus = new BigDecimal(titaVo.get("AdjustBonus").trim());

					if (iBonus.compareTo(iAdjustBonus) == 0) {
						pfRewardMedia.setAdjustBonusDate(0);
						pfRewardMedia.setAdjustBonus(iAdjustBonus);

					} else {
						pfRewardMedia.setAdjustBonusDate(Integer.valueOf(titaVo.getParam("CALDY")));
						pfRewardMedia.setAdjustBonus(iAdjustBonus);
					}
					pfRewardMedia.setRemark(titaVo.get("Remark").trim());

					try {
						pfRewardMedia = pfRewardMediaService.update2(pfRewardMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "");
					}
					//
					dataLog.setEnv(titaVo, pfRewardMedia2, pfRewardMedia);
					dataLog.exec("修改獎金資料");

				} else if ("4".equals(iFunCode)) {
					if (pfRewardMedia.getManualFg() == 0) {
						throw new LogicException("EC008", "非人工新增資料，不可刪除");
					}
					try {
						pfRewardMediaService.delete(pfRewardMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "");
					}
					dataLog.setEnv(titaVo, pfRewardMedia, pfRewardMedia);
					dataLog.exec("刪除人工新增獎金");
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private PfRewardMedia setValue(TitaVo titaVo, PfRewardMedia pfRewardMedia) throws LogicException {

		int iBonusDate = Integer.valueOf(titaVo.get("BonusDate").trim());
		int iPerfDate = Integer.valueOf(titaVo.get("PerfDate").trim());
		int iCustNo = Integer.valueOf(titaVo.get("CustNo").trim());
		int iFacmNo = Integer.valueOf(titaVo.get("FacmNo").trim());
		int iBormNo = Integer.valueOf(titaVo.get("BormNo").trim());

		pfRewardMedia.setBonusDate(iBonusDate);
		pfRewardMedia.setBonusType(Integer.valueOf(titaVo.get("BonusType").trim()));
		pfRewardMedia.setPerfDate(iPerfDate);
		pfRewardMedia.setCustNo(iCustNo);
		pfRewardMedia.setFacmNo(iFacmNo);
		pfRewardMedia.setBormNo(iBormNo);
		pfRewardMedia.setEmployeeNo(titaVo.get("EmployeeNo").trim());
		pfRewardMedia.setProdCode(titaVo.get("ProdCode").trim());
		pfRewardMedia.setPieceCode(titaVo.get("PieceCode").trim());
//		BigDecimal iBonus=new BigDecimal(titaVo.get("Bonus").trim());
//		pfRewardMedia.setBonus(iBonus);
		int iWorkMonth = Integer.valueOf(titaVo.get("WorkMonth").trim());
		if (iWorkMonth > 0) {
			iWorkMonth += 191100;
		}
		pfRewardMedia.setWorkMonth(iWorkMonth);
		int iWorkSeason = Integer.valueOf(titaVo.get("WorkSeason").trim());
		if (iWorkSeason > 0) {
			iWorkSeason += 19110;
		}
		pfRewardMedia.setWorkSeason(iWorkSeason);
		pfRewardMedia.setRemark(titaVo.get("Remark").trim());

		return pfRewardMedia;
	}
}