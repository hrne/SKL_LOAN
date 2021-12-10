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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.PfBsDetailAdjust;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsDetailAdjustService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R29")
@Scope("prototype")
/**
 * L5R29
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R29 extends TradeBuffer {
	@Autowired
	PfBsDetailService pfBsDetailService;

	@Autowired
	PfItDetailService sPfItDetailService;

	@Autowired
	CustMainService sCustMainService;

	@Autowired
	CdEmpService sCdEmpService;

	@Autowired
	PfBsDetailAdjustService pfBsDetailAdjustService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R29 ");
		this.totaVo.init(titaVo);
		// L5052調RIM
		this.info("L5R29");
//		Long iLogNo = Long.valueOf(titaVo.get("LogNo").trim());
		int custNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int facmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		int workMonth = Integer.valueOf(titaVo.getParam("WorkMonth").trim()) + 191100;

		Slice<PfBsDetail> slPfBsDetail = pfBsDetailService.findByCustNoAndFacmNo(custNo, facmNo, 0, Integer.MAX_VALUE,
				titaVo);

		List<PfBsDetail> lPfBsDetail = slPfBsDetail == null ? null : slPfBsDetail.getContent();

		BigDecimal bigZero = new BigDecimal("0");
		BigDecimal bigNeg = new BigDecimal("-1");

		boolean found = false;
		boolean first = true;
		BigDecimal perfAmt = new BigDecimal("0");
		
		if (lPfBsDetail != null && lPfBsDetail.size() > 0) {
			for (PfBsDetail pfBsDetail : lPfBsDetail) {
				if (pfBsDetail.getDrawdownAmt().compareTo(bigZero) <= 0) {
					continue;
				}
				if (pfBsDetail.getWorkMonth() != workMonth) {
					continue;
				}
				if (pfBsDetail.getRepayType() != 0) {
					continue;
				}
				found = true;
				perfAmt = perfAmt.add(pfBsDetail.getPerfAmt());
				if (first) {
					first = false;
					totaVo.putParam("L5r29CustNo", pfBsDetail.getCustNo());
					totaVo.putParam("L5r29FacmNo", pfBsDetail.getFacmNo());
					totaVo.putParam("L5r29BormNo", pfBsDetail.getBormNo());

					CustMain custMain = sCustMainService.custNoFirst(pfBsDetail.getCustNo(), pfBsDetail.getCustNo(),
							titaVo);
					if (custMain != null) {
						totaVo.putParam("L5r29CustNm", custMain.getCustName());
					} else {
						totaVo.putParam("L5r29CustNm", "");
					}

					totaVo.putParam("L5r29WorkMonth", pfBsDetail.getWorkMonth() - 191100);
					totaVo.putParam("L5r29BsOfficer", pfBsDetail.getBsOfficer());
					totaVo.putParam("L5r29BsOfficerName", findEmpName(pfBsDetail.getBsOfficer(), titaVo));

					PfItDetail pfItDetail = sPfItDetailService.findByTxFirst(pfBsDetail.getCustNo(),
							pfBsDetail.getFacmNo(), pfBsDetail.getBormNo(), pfBsDetail.getPerfDate() + 19110000,
							pfBsDetail.getRepayType(), pfBsDetail.getPieceCode(), titaVo);
					if (pfItDetail != null) {
						totaVo.putParam("L5r29Introducer", pfItDetail.getIntroducer());
						totaVo.putParam("L5r29IntroducerName", findEmpName(pfItDetail.getIntroducer(), titaVo));
					} else {
						totaVo.putParam("L5r29Introducer", "");
						totaVo.putParam("L5r29IntroducerName", "");
					}
				}
			}
		}

		if (!found) {
			throw new LogicException(titaVo, "E0001", "業績資料");
		}
		
		totaVo.putParam("L5r29PerfAmt", perfAmt);

		PfBsDetailAdjust pfBsDetailAdjust = pfBsDetailAdjustService.findCustFacmFirst(custNo, facmNo, workMonth,
				titaVo);

		if (pfBsDetailAdjust == null) {
			totaVo.putParam("L5r29AdjPerfAmtSign", "");
			totaVo.putParam("L5r29AdjPerfAmt", 0);
			totaVo.putParam("L5r29AdjPerfCntSign", "");
			totaVo.putParam("L5r29AdjPerfCnt", 0);
			totaVo.putParam("L5r29AdjLogNo", 0);
		} else {
			BigDecimal amt = pfBsDetailAdjust.getAdjPerfAmt();
			String sign = "";
			if (pfBsDetailAdjust.getAdjPerfAmt().compareTo(bigZero) > 0) {
				sign = "+";
			} else if (pfBsDetailAdjust.getAdjPerfAmt().compareTo(bigZero) < 0) {
				sign = "-";
				amt = amt.multiply(bigNeg);
			}
			this.totaVo.putParam("L5r29AdjPerfAmtSign", sign);
			this.totaVo.putParam("L5r29AdjPerfAmt", amt);

			BigDecimal cnt = pfBsDetailAdjust.getAdjPerfCnt();

			sign = "";

			if (pfBsDetailAdjust.getAdjPerfCnt().compareTo(bigZero) > 0) {
				sign = "+";
			} else if (pfBsDetailAdjust.getAdjPerfCnt().compareTo(bigZero) < 0) {
				sign = "-";
				cnt = cnt.multiply(bigNeg);
			}
			this.totaVo.putParam("L5r29AdjPerfCntSign", sign);
			this.totaVo.putParam("L5r29AdjPerfCnt", cnt);
			totaVo.putParam("L5r29AdjLogNo", pfBsDetailAdjust.getLogNo());
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}

	public String findEmpName(String employeeNo, TitaVo titaVo) {
		String empName = "";
		if (employeeNo != null && employeeNo.length() != 0) {
			CdEmp CdEmpVo = sCdEmpService.findById(employeeNo, titaVo);
			if (CdEmpVo != null) {
				empName = CdEmpVo.getFullname();
			}
		}
		return empName;
	}
}