package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.PfRewardMedia;
//import com.st1.itx.db.domain.PfRewardMediaId;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;

@Service("L5R37")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5R37 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R37.class);

	@Autowired
	public PfRewardMediaService pfRewardMediaService;

	@Autowired
	public FacProdService facProdService;
	
	@Autowired
	public CdEmpService cdEmpService;
	
	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R37 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
//		int iBonusDate = Integer.valueOf(titaVo.get("BonusDate").trim());
		long iBonusNo = Long.valueOf(titaVo.get("BonusNo").trim());

//		PfRewardMediaId pfRewardMediaId = new PfRewardMediaId();

//		pfRewardMediaId.setBonusDate(iBonusDate);
//		pfRewardMediaId.setSeqNo(iSeqNo);


//		PfRewardMedia pfRewardMedia = pfRewardMediaService.findById(pfRewardMediaId, titaVo);
		PfRewardMedia pfRewardMedia = pfRewardMediaService.findById(iBonusNo, titaVo);

		if (pfRewardMedia == null) {
			if ("1".equals(iFunCode)) {
				this.totaVo.putParam("BonusNo", 0);
				this.totaVo.putParam("BonusDate", 0);
				this.totaVo.putParam("BonusType", 0);
				this.totaVo.putParam("PerfDate", 0);
				this.totaVo.putParam("CustNo", 0);
				this.totaVo.putParam("CustName", 0);
				this.totaVo.putParam("FacmNo", 0);
				this.totaVo.putParam("BormNo", 0);
				this.totaVo.putParam("EmployeeNo", "");
				this.totaVo.putParam("EmployeeName", "");
				this.totaVo.putParam("Bonus", 0);
				this.totaVo.putParam("AdjustBonus", 0);
				this.totaVo.putParam("AdjustBonusDate", 0);
				this.totaVo.putParam("ProdCode", "");
				this.totaVo.putParam("ProdName", "");
				this.totaVo.putParam("PieceCode", "");
				this.totaVo.putParam("WorkMonth", 0);
				this.totaVo.putParam("WorkSeason", 0);
				this.totaVo.putParam("Remark", "");
				this.totaVo.putParam("MediaFg", 0);
				this.totaVo.putParam("MediaDate", 0);
				this.totaVo.putParam("ManualFg", 1);				
			} else {
				throw new LogicException("E0001", "");
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException("E0002", "");
			} else {
				if (pfRewardMedia.getMediaFg()==1&&("2".equals(iFunCode)||"4".equals(iFunCode))) {
					throw new LogicException("EC008", "已產製媒體檔，不可修改或刪除");
				}
				this.totaVo.putParam("BonusNo", pfRewardMedia.getBonusNo());
				this.totaVo.putParam("BonusDate", pfRewardMedia.getBonusDate());
				this.totaVo.putParam("BonusType", pfRewardMedia.getBonusType());
				this.totaVo.putParam("PerfDate", pfRewardMedia.getPerfDate());
				this.totaVo.putParam("CustNo", pfRewardMedia.getCustNo());
				this.totaVo.putParam("FacmNo", pfRewardMedia.getFacmNo());
				this.totaVo.putParam("BormNo", pfRewardMedia.getBormNo());
				this.totaVo.putParam("EmployeeNo", pfRewardMedia.getEmployeeNo());
				
				CdEmp cdEmp = cdEmpService.findById(pfRewardMedia.getEmployeeNo(), titaVo);
				if (cdEmp == null) {
					this.totaVo.putParam("EmployeeName", pfRewardMedia.getEmployeeNo());
				} else {
					this.totaVo.putParam("EmployeeName", cdEmp.getFullname());
				}
				
				this.totaVo.putParam("Bonus", pfRewardMedia.getBonus());
				this.totaVo.putParam("AdjustBonus", pfRewardMedia.getAdjustBonus());
				this.totaVo.putParam("AdjustBonusDate", pfRewardMedia.getAdjustBonusDate());
				this.totaVo.putParam("ProdCode", pfRewardMedia.getProdCode());
				
				FacProd facProd = facProdService.findById(pfRewardMedia.getProdCode(), titaVo);
				if (facProd == null) {
					this.totaVo.putParam("ProdName", pfRewardMedia.getProdCode());
				} else {
					this.totaVo.putParam("ProdName", facProd.getProdName());
				}
				
				this.totaVo.putParam("PieceCode", pfRewardMedia.getPieceCode());
				
				if (pfRewardMedia.getWorkMonth() > 0) {
					this.totaVo.putParam("WorkMonth", pfRewardMedia.getWorkMonth() - 191100);
				} else {
					this.totaVo.putParam("WorkMonth", pfRewardMedia.getWorkMonth());
				}
				if (pfRewardMedia.getWorkSeason() > 0) {
					this.totaVo.putParam("WorkSeason", pfRewardMedia.getWorkSeason() - 19110);
				} else {
					this.totaVo.putParam("WorkSeason", pfRewardMedia.getWorkSeason());
				}

				if (pfRewardMedia.getBonusType() != 6) {
					CustMain custMain = custMainService.custNoFirst(pfRewardMedia.getCustNo(), pfRewardMedia.getCustNo(), titaVo);
					if (custMain == null) {
						throw new LogicException("E0001", "客戶資料");
					}
					
					this.totaVo.putParam("CustName", custMain.getCustName());
				} else {
					this.totaVo.putParam("CustName", "");
				}
					
				this.totaVo.putParam("Remark", pfRewardMedia.getRemark());
				this.totaVo.putParam("MediaFg", pfRewardMedia.getMediaFg());
				this.totaVo.putParam("MediaDate", pfRewardMedia.getMediaDate());
				this.totaVo.putParam("ManualFg", pfRewardMedia.getManualFg());				
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}