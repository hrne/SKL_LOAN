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

import com.st1.itx.db.domain.PfDetail;
import com.st1.itx.db.service.PfDetailService;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
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

	@Autowired
	public PfDetailService pfDetailService;

	@Autowired
	public CdBcmService CdBcmService;

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

			Slice<PfDetail> slPfDetail = pfDetailService.FindByBormNo(iCustNo, iFacmNo, iBormNo, 0, Integer.MAX_VALUE, titaVo);
			List<PfDetail> lPfDetail = slPfDetail == null ? null : slPfDetail.getContent();
			if (lPfDetail != null && lPfDetail.size() > 0) {
				for (PfDetail pfDetail : lPfDetail) {
					// 撥款件
					if (pfDetail.getRepayType() == 0) {
						found = true;

						this.totaVo.putParam("CustNo", pfDetail.getCustNo());
						this.totaVo.putParam("FacmNo", pfDetail.getFacmNo());
						this.totaVo.putParam("BormNo", pfDetail.getBormNo());

						CustMain custMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);

						if (custMain == null) {
							throw new LogicException("E0001", "客戶主檔");
						}

						this.totaVo.putParam("CustName", custMain.getCustName());

						this.totaVo.putParam("LogNo", pfDetail.getLogNo());

						this.totaVo.putParam("Introducer", pfDetail.getIntroducer());

						if (!"".equals(pfDetail.getIntroducer().trim())) {
							CdEmp cdEmp = cdEmpService.findById(pfDetail.getIntroducer(), titaVo);

							if (cdEmp == null) {
								this.totaVo.putParam("IntroducerName", "");
							} else {
								this.totaVo.putParam("IntroducerName", cdEmp.getFullname());
							}
						} else {
							this.totaVo.putParam("IntroducerName", "");
						}

						this.totaVo.putParam("BsOfficer", pfDetail.getBsOfficer());

						if (!"".equals(pfDetail.getBsOfficer().trim())) {
							CdEmp cdEmp = cdEmpService.findById(pfDetail.getBsOfficer(), titaVo);

							if (cdEmp == null) {
								this.totaVo.putParam("BsOfficerName", "");
							} else {
								this.totaVo.putParam("BsOfficerName", cdEmp.getFullname());
							}
						} else {
							this.totaVo.putParam("BsOfficerName", "");
						}

						this.totaVo.putParam("PerfAmt", pfDetail.getItPerfAmt());

						this.totaVo.putParam("PerfCnt", pfDetail.getItPerfCnt());

						this.totaVo.putParam("UnitCode", pfDetail.getUnitCode());

						CdBcm cdBcm = CdBcmService.findById(pfDetail.getUnitCode().trim(), titaVo);

						if (cdBcm != null) {
							this.totaVo.putParam("UnitItem", cdBcm.getUnitItem());
						} else {
							this.totaVo.putParam("UnitItem", "");
						}

						this.totaVo.putParam("DistCode", pfDetail.getDistCode());

						cdBcm = CdBcmService.distCodeFirst(pfDetail.getDistCode(), titaVo);

						if (cdBcm != null) {
							this.totaVo.putParam("DistItem", cdBcm.getDistItem());
						} else {
							this.totaVo.putParam("DistItem", "");
						}

						this.totaVo.putParam("DeptCode", pfDetail.getDeptCode());

						cdBcm = CdBcmService.deptCodeFirst(pfDetail.getDeptCode(), titaVo);

						if (cdBcm != null) {
							this.totaVo.putParam("DeptItem", cdBcm.getDeptItem());
						} else {
							this.totaVo.putParam("DeptItem", "");
						}

						this.totaVo.putParam("PerfDate", pfDetail.getPerfDate());
						this.totaVo.putParam("WorkMonth", pfDetail.getWorkMonth());
						this.totaVo.putParam("WorkSeason", pfDetail.getWorkSeason());

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

			PfIntranetAdjust pfIntranetAdjust = pfIntranetAdjustService.findByBormFirst(iCustNo, iFacmNo, iBormNo, titaVo);

			if (pfIntranetAdjust != null) {
				throw new LogicException("E0002", "業績資料");
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

			CustMain custMain = custMainService.custNoFirst(pfIntranetAdjust.getCustNo(), pfIntranetAdjust.getCustNo(), titaVo);

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

			CdBcm cdBcm = CdBcmService.findById(pfIntranetAdjust.getUnitCode().trim(), titaVo);

			if (cdBcm != null) {
				this.totaVo.putParam("UnitItem", cdBcm.getUnitItem());
			} else {
				this.totaVo.putParam("UnitItem", "");
			}

			this.totaVo.putParam("DistCode", pfIntranetAdjust.getDistCode());

			cdBcm = CdBcmService.distCodeFirst(pfIntranetAdjust.getDistCode(), titaVo);

			if (cdBcm != null) {
				this.totaVo.putParam("DistItem", cdBcm.getDistItem());
			} else {
				this.totaVo.putParam("DistItem", "");
			}

			this.totaVo.putParam("DeptCode", pfIntranetAdjust.getDeptCode());

			cdBcm = CdBcmService.deptCodeFirst(pfIntranetAdjust.getDeptCode(), titaVo);

			if (cdBcm != null) {
				this.totaVo.putParam("DeptItem", cdBcm.getDeptItem());
			} else {
				this.totaVo.putParam("DeptItem", "");
			}

			this.totaVo.putParam("PerfDate", pfIntranetAdjust.getPerfDate());
			this.totaVo.putParam("WorkMonth", pfIntranetAdjust.getWorkMonth());
			this.totaVo.putParam("WorkSeason", pfIntranetAdjust.getWorkSeason());

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