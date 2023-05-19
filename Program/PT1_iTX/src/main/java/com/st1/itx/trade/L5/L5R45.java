package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.domain.PfIntranetAdjust;
import com.st1.itx.db.service.PfIntranetAdjustService;

@Service("L5R45")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5R45 extends TradeBuffer {
	private static final Object iWorkSeason = null;

	@Autowired
	CdWorkMonthService cdWorkMonthService;

	@Autowired
	public PfItDetailService pfItDetailService;

	@Autowired
	public PfBsDetailService pfBsDetailService;

	@Autowired
	public CdBcmService cdBcmService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public PfIntranetAdjustService pfIntranetAdjustService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R45 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode").trim();

		boolean found = false;

		if ("1".equals(iFunCode)) {
			int iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim());
			int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim());
			int iBormNo = Integer.valueOf(titaVo.getParam("BormNo").trim());

			CdWorkMonth tCdWorkMonth = cdWorkMonthService.findDateFirst(titaVo.getEntDyI() + 19110000,
					titaVo.getEntDyI() + 19110000, titaVo);
			if (tCdWorkMonth == null) {
				throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月檔，業績日期=" + titaVo.getEntDyI()); // 查詢資料不存在
			}

			int iWorkMonthF = tCdWorkMonth.getYear() * 100 + tCdWorkMonth.getMonth();
			int iWorkMonth = iWorkMonthF - 191100;
			PfIntranetAdjust pfIntranetAdjust = pfIntranetAdjustService.findByBormFirst(iCustNo, iFacmNo, iBormNo,
					iWorkMonthF, titaVo);

			if (pfIntranetAdjust != null) {
				throw new LogicException("E0002", "業績資料");
			}
			// 工作季(西曆)
			int iWorkSeason = 0;
			if (iWorkMonth % 100 <= 3)
				iWorkSeason = (iWorkMonth / 100) + 1;
			else if (iWorkMonth % 100 <= 6)
				iWorkSeason = (iWorkMonth / 100) + 2;
			else if (iWorkMonth % 100 <= 9)
				iWorkSeason = (iWorkMonth / 100) + 3;
			else
				iWorkSeason = (iWorkMonth / 100) + 4;

			Slice<PfItDetail> slPfItDetail = pfItDetailService.findBormNoEq(iCustNo, iFacmNo, iBormNo, 0,
					Integer.MAX_VALUE, titaVo);
			List<PfItDetail> lPfItDetail = slPfItDetail == null ? null : slPfItDetail.getContent();
			if (lPfItDetail != null && lPfItDetail.size() > 0) {
				for (PfItDetail PfItDetail : lPfItDetail) {
					// 撥款件
					if (PfItDetail.getRepayType() == 0) {
						found = true;

						this.totaVo.putParam("CustNo", iCustNo);
						this.totaVo.putParam("FacmNo", iFacmNo);
						this.totaVo.putParam("BormNo", iBormNo);

						CustMain custMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);

						if (custMain == null) {
							throw new LogicException("E0001", "客戶主檔");
						}

						this.totaVo.putParam("CustName", custMain.getCustName());

						this.totaVo.putParam("LogNo", PfItDetail.getLogNo());

						this.totaVo.putParam("Introducer", PfItDetail.getIntroducer());

						if (!"".equals(PfItDetail.getIntroducer().trim())) {
							CdEmp cdEmp = cdEmpService.findById(PfItDetail.getIntroducer(), titaVo);

							if (cdEmp == null) {
								this.totaVo.putParam("IntroducerName", "");
							} else {
								this.totaVo.putParam("IntroducerName", cdEmp.getFullname());
							}
						} else {
							this.totaVo.putParam("IntroducerName", "");
						}

						this.totaVo.putParam("BsOfficer", "");
						this.totaVo.putParam("BsOfficerName", "");
						PfBsDetail tPfBsDetail = pfBsDetailService.findBormNoLatestFirst(iCustNo, iFacmNo, iBormNo,
								titaVo);
						if (tPfBsDetail != null && !"".equals(tPfBsDetail.getBsOfficer().trim())) {
							this.totaVo.putParam("BsOfficer", tPfBsDetail.getBsOfficer());
							CdEmp cdEmp = cdEmpService.findById(tPfBsDetail.getBsOfficer(), titaVo);
							if (cdEmp != null) {
								this.totaVo.putParam("BsOfficerName", cdEmp.getFullname());
							}
						}
						this.totaVo.putParam("PerfAmt", PfItDetail.getPerfAmt());

						this.totaVo.putParam("PerfCnt", PfItDetail.getPerfCnt());

						this.totaVo.putParam("UnitCode", PfItDetail.getUnitCode());

						CdBcm cdBcm = cdBcmService.findById(PfItDetail.getUnitCode().trim(), titaVo);

						if (cdBcm != null) {
							this.totaVo.putParam("UnitItem", cdBcm.getUnitItem());
						} else {
							this.totaVo.putParam("UnitItem", "");
						}

						this.totaVo.putParam("DistCode", PfItDetail.getDistCode());

						cdBcm = cdBcmService.distCodeFirst(PfItDetail.getDistCode(), titaVo);

						if (cdBcm != null) {
							this.totaVo.putParam("DistItem", cdBcm.getDistItem());
						} else {
							this.totaVo.putParam("DistItem", "");
						}

						this.totaVo.putParam("DeptCode", PfItDetail.getDeptCode());

						cdBcm = cdBcmService.deptCodeFirst(PfItDetail.getDeptCode(), titaVo);

						if (cdBcm != null) {
							this.totaVo.putParam("DeptItem", cdBcm.getDeptItem());
						} else {
							this.totaVo.putParam("DeptItem", "");
						}

						this.totaVo.putParam("PerfDate", PfItDetail.getPerfDate());
						this.totaVo.putParam("WorkMonth", iWorkMonth);
						this.totaVo.putParam("WorkSeason", iWorkSeason);

						this.totaVo.putParam("UnitType", 0);
						this.totaVo.putParam("SumAmtSign", " ");
						this.totaVo.putParam("SumAmt", 0);
						this.totaVo.putParam("SumCntSign", " ");
						this.totaVo.putParam("SumCnt", 0);
					}
				}
			}

			if (!found) {
				throw new LogicException("E0001", "業績資料");
			}

		} else {
			long iLogNo = Long.valueOf(titaVo.getParam("LogNo").trim());

			PfIntranetAdjust pfIntranetAdjust = pfIntranetAdjustService.findById(iLogNo, titaVo);

			if (pfIntranetAdjust == null) {
				throw new LogicException(titaVo, "E0001", "");
			}

			this.totaVo.putParam("CustNo", pfIntranetAdjust.getCustNo());
			this.totaVo.putParam("FacmNo", pfIntranetAdjust.getFacmNo());
			this.totaVo.putParam("BormNo", pfIntranetAdjust.getBormNo());

			CustMain custMain = custMainService.custNoFirst(pfIntranetAdjust.getCustNo(), pfIntranetAdjust.getCustNo(),
					titaVo);

			if (custMain == null) {
				throw new LogicException("E0001", "客戶主檔");
			}
			this.totaVo.putParam("CustName", custMain.getCustName());

			this.totaVo.putParam("LogNo", pfIntranetAdjust.getLogNo());

			this.totaVo.putParam("Introducer", pfIntranetAdjust.getIntroducer());

			if (!"".equals(pfIntranetAdjust.getIntroducer().trim())) {
				CdEmp cdEmp = cdEmpService.findById(pfIntranetAdjust.getIntroducer(), titaVo);

				if (cdEmp == null) {
					this.totaVo.putParam("IntroducerName", "");
				} else {
					this.totaVo.putParam("IntroducerName", cdEmp.getFullname());
				}
			} else {
				this.totaVo.putParam("IntroducerName", "");
			}

			this.totaVo.putParam("BsOfficer", pfIntranetAdjust.getBsOfficer());

			if (!"".equals(pfIntranetAdjust.getBsOfficer().trim())) {
				CdEmp cdEmp = cdEmpService.findById(pfIntranetAdjust.getBsOfficer(), titaVo);

				if (cdEmp == null) {
					this.totaVo.putParam("BsOfficerName", "");
				} else {
					this.totaVo.putParam("BsOfficerName", cdEmp.getFullname());
				}
			} else {
				this.totaVo.putParam("BsOfficerName", "");
			}

			this.totaVo.putParam("PerfAmt", pfIntranetAdjust.getPerfAmt());

			this.totaVo.putParam("PerfCnt", pfIntranetAdjust.getPerfCnt());

			this.totaVo.putParam("UnitCode", pfIntranetAdjust.getUnitCode());

			CdBcm cdBcm = cdBcmService.findById(pfIntranetAdjust.getUnitCode().trim(), titaVo);

			if (cdBcm != null) {
				this.totaVo.putParam("UnitItem", cdBcm.getUnitItem());
			} else {
				this.totaVo.putParam("UnitItem", "");
			}

			this.totaVo.putParam("DistCode", pfIntranetAdjust.getDistCode());

			cdBcm = cdBcmService.distCodeFirst(pfIntranetAdjust.getDistCode(), titaVo);

			if (cdBcm != null) {
				this.totaVo.putParam("DistItem", cdBcm.getDistItem());
			} else {
				this.totaVo.putParam("DistItem", "");
			}

			this.totaVo.putParam("DeptCode", pfIntranetAdjust.getDeptCode());

			cdBcm = cdBcmService.deptCodeFirst(pfIntranetAdjust.getDeptCode(), titaVo);

			if (cdBcm != null) {
				this.totaVo.putParam("DeptItem", cdBcm.getDeptItem());
			} else {
				this.totaVo.putParam("DeptItem", "");
			}

			this.totaVo.putParam("PerfDate", pfIntranetAdjust.getPerfDate());
			this.totaVo.putParam("WorkMonth", pfIntranetAdjust.getWorkMonth() - 191100);
			this.totaVo.putParam("WorkSeason", pfIntranetAdjust.getWorkSeason() - 1911);

			this.totaVo.putParam("UnitType", pfIntranetAdjust.getUnitType());

			BigDecimal bigZero = new BigDecimal("0");
			BigDecimal bigNeg = new BigDecimal("-1");

			BigDecimal sumAmt = pfIntranetAdjust.getSumAmt();
			String signAmt = "";
			if (pfIntranetAdjust.getSumAmt().compareTo(bigZero) > 0) {
				signAmt = "+";
			} else if (pfIntranetAdjust.getSumAmt().compareTo(bigZero) < 0) {
				signAmt = "-";
				sumAmt = sumAmt.multiply(bigNeg);
			}
			this.totaVo.putParam("SumAmtSign", signAmt);
			this.totaVo.putParam("SumAmt", sumAmt);

			BigDecimal SumCnt = pfIntranetAdjust.getSumCnt();
			String signCnt = "";
			if (pfIntranetAdjust.getSumCnt().compareTo(bigZero) > 0) {
				signCnt = "+";
			} else if (pfIntranetAdjust.getSumCnt().compareTo(bigZero) < 0) {
				signCnt = "-";
				SumCnt = SumCnt.multiply(bigNeg);
			}
			this.totaVo.putParam("SumCntSign", signCnt);
			this.totaVo.putParam("SumCnt", SumCnt);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}