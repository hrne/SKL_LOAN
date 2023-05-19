package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.PfRewardMedia;
import com.st1.itx.db.domain.TxControl;
//import com.st1.itx.db.domain.PfRewardMediaId;
import com.st1.itx.db.service.PfRewardMediaService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.PfDetail;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.PfDetailService;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;

@Service("L5R37")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5R37 extends TradeBuffer {

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public PfRewardMediaService pfRewardMediaService;
	@Autowired
	public PfRewardService pfRewardService;

	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CdEmpService cdEmpService;

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	CdWorkMonthService cdWorkMonthService;

	@Autowired
	public TxControlService txControlService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R37 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		int iWorkMonth = parse.stringToInteger(titaVo.get("WorkMonth").trim());
		// 工作季(西曆)
		int workSeason = 0;
		if (iWorkMonth % 100 <= 3)
			workSeason = (iWorkMonth / 100)*10 + 1;
		else if (iWorkMonth % 100 <= 6)
			workSeason = (iWorkMonth / 100)*10 + 2;
		else if (iWorkMonth % 100 <= 9)
			workSeason = (iWorkMonth / 100)*10 + 3;
		else
			workSeason = (iWorkMonth / 100)*10 + 4;
		// int iBonusDate = Integer.valueOf(titaVo.get("BonusDate").trim());

//		PfRewardMediaId pfRewardMediaId = new PfRewardMediaId();

//		pfRewardMediaId.setBonusDate(iBonusDate);
//		pfRewardMediaId.setSeqNo(iSeqNo);

//		PfRewardMedia pfRewardMedia = pfRewardMediaService.findById(pfRewardMediaId, titaVo);

		if ("1".equals(iFunCode)) {
			int custNo = Integer.valueOf(titaVo.getParam("CustNo").trim());
			int facmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim());
			int bormNo = Integer.valueOf(titaVo.getParam("BormNo").trim());
			int bonusType = Integer.valueOf(titaVo.getParam("BonusType").trim());

			PfRewardMedia pfRewardMedia = pfRewardMediaService.findDupFirst(custNo, facmNo, bormNo, bonusType,
					iWorkMonth + 191100, titaVo);
			if (pfRewardMedia != null) {
				throw new LogicException("E0002", "獎金資料");
			}
			String prodCode = "";
			String pieceCode = "";
			String employeeNo = "";
			int perfDate = 0;
			Slice<PfReward> slPfReward = pfRewardService.findBormNoEq(custNo, facmNo, bormNo, 0, Integer.MAX_VALUE,
					titaVo);
			if (slPfReward != null) {
				for (PfReward tPfReward : slPfReward.getContent()) {
					if (tPfReward.getRepayType() == 0) {
						if ((bonusType == 1 || bonusType == 7)) {
							employeeNo = tPfReward.getIntroducer();
						}
						if (bonusType == 5) {
							employeeNo = tPfReward.getCoorgnizer();
						}
						prodCode = tPfReward.getProdCode();
						pieceCode = tPfReward.getPieceCode();
						perfDate = tPfReward.getPerfDate();
					}
				}
			}
			if (prodCode.isEmpty()) {
				LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(custNo, facmNo, bormNo),
						titaVo);
				if (tLoanBorMain == null) {
					throw new LogicException(titaVo, "E0001", "撥款資料");
				}
				FacMain tFacMain = facMainService.findById(new FacMainId(custNo, facmNo), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "額度資料");
				}
				if (bonusType == 1 || bonusType == 7) {
					employeeNo = tFacMain.getIntroducer();
				} else {
					employeeNo = tFacMain.getCoorgnizer();
				}
				prodCode = tFacMain.getProdNo();
				pieceCode = tLoanBorMain.getPieceCode();
				perfDate = tLoanBorMain.getDrawdownDate();
			}

			this.totaVo.putParam("PerfDate", perfDate);
			this.totaVo.putParam("BonusNo", 0);
			CdWorkMonth cdWorkMonth = cdWorkMonthService
					.findById(new CdWorkMonthId(iWorkMonth / 100 + 1911, iWorkMonth % 100), titaVo);
			if (cdWorkMonth == null) {
				throw new LogicException("E0001", "放款業績工作月對照檔");
			}
			this.totaVo.putParam("BonusDate", cdWorkMonth.getBonusDate());
			this.totaVo.putParam("BonusType", bonusType);
			this.totaVo.putParam("CustNo", custNo);

			CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
			if (custMain == null) {
				throw new LogicException("E0001", "客戶資料");
			}

			this.totaVo.putParam("CustName", custMain.getCustName());
			this.totaVo.putParam("FacmNo", facmNo);
			this.totaVo.putParam("BormNo", bormNo);
			this.totaVo.putParam("EmployeeNo", employeeNo);
			CdEmp cdEmp = cdEmpService.findById(employeeNo, titaVo);
			if (cdEmp == null) {
				this.totaVo.putParam("EmployeeName", employeeNo);
			} else {
				this.totaVo.putParam("EmployeeName", cdEmp.getFullname());
			}
			this.totaVo.putParam("Bonus", 0);
			this.totaVo.putParam("AdjustBonus", 0);
			this.totaVo.putParam("AdjustBonusDate", 0);
			this.totaVo.putParam("ProdCode", prodCode);

			FacProd facProd = facProdService.findById(prodCode, titaVo);
			if (facProd == null) {
				this.totaVo.putParam("ProdName", prodCode);
			} else {
				this.totaVo.putParam("ProdName", facProd.getProdName());
			}

			this.totaVo.putParam("PieceCode", pieceCode);
			this.totaVo.putParam("WorkMonth", iWorkMonth);
			this.totaVo.putParam("WorkSeason", workSeason);
			this.totaVo.putParam("Remark", "");
			this.totaVo.putParam("MediaFg", 0);
			this.totaVo.putParam("MediaDate", 0);
			this.totaVo.putParam("ManualFg", 1);
			this.totaVo.putParam("LastUpdate", "");
			this.totaVo.putParam("LastEmpName", "");
		} else {
			long iBonusNo = Long.valueOf(titaVo.get("BonusNo").trim());
			PfRewardMedia pfRewardMedia = pfRewardMediaService.findById(iBonusNo, titaVo);

			if (pfRewardMedia == null) {
				throw new LogicException("E0001", "");
			} else {
				if ("2".equals(iFunCode) || "4".equals(iFunCode)) {
					String controlCode = "";
					if (pfRewardMedia.getBonusType() == 7) {
						controlCode = "L5512." + pfRewardMedia.getWorkMonth() + ".2";
					} else {
						controlCode = "L5511." + pfRewardMedia.getWorkMonth() + ".2";
					}

					TxControl txControl = txControlService.findById(controlCode, titaVo);
					if (txControl != null) {
						throw new LogicException(titaVo, "E0010", "已產生媒體檔");
					}
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
					CustMain custMain = custMainService.custNoFirst(pfRewardMedia.getCustNo(),
							pfRewardMedia.getCustNo(), titaVo);
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
				this.totaVo.putParam("LastUpdate", parse.timeStampToString(pfRewardMedia.getLastUpdate()));

				cdEmp = cdEmpService.findById(pfRewardMedia.getLastUpdateEmpNo(), titaVo);
				if (cdEmp == null) {
					this.totaVo.putParam("LastEmpName", pfRewardMedia.getEmployeeNo());
				} else {
					this.totaVo.putParam("LastEmpName", pfRewardMedia.getEmployeeNo() + " " + cdEmp.getFullname());
				}

			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}