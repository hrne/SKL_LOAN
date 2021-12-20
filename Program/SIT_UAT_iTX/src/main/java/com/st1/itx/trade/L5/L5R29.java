package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R29 extends TradeBuffer {
	@Autowired
	public PfBsDetailService pfBsDetailService;

	@Autowired
	public PfItDetailService pfItDetailService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public PfBsDetailAdjustService pfBsDetailAdjustService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R29 ");
		this.totaVo.init(titaVo);
		// L5052調RIM
		this.info("L5R29");
		long logNo = Long.valueOf(titaVo.getParam("LogNo"));

		PfBsDetail pfBsDetail = pfBsDetailService.findById(logNo, titaVo);

		if (pfBsDetail == null) {
			throw new LogicException(titaVo, "E0001", "房貸專員業績資料");
		}

		totaVo.putParam("L5r29CustNo", pfBsDetail.getCustNo());
		totaVo.putParam("L5r29FacmNo", pfBsDetail.getFacmNo());
		totaVo.putParam("L5r29BormNo", pfBsDetail.getBormNo());

		CustMain custMain = sCustMainService.custNoFirst(pfBsDetail.getCustNo(), pfBsDetail.getCustNo(), titaVo);
		if (custMain != null) {
			totaVo.putParam("L5r29CustNm", custMain.getCustName());
		} else {
			totaVo.putParam("L5r29CustNm", "");
		}

		totaVo.putParam("L5r29WorkMonth", pfBsDetail.getWorkMonth() - 191100);
		totaVo.putParam("L5r29BsOfficer", pfBsDetail.getBsOfficer());
		totaVo.putParam("L5r29BsOfficerName", FindEmpName(pfBsDetail.getBsOfficer(), titaVo));

		PfItDetail pfItDetail = pfItDetailService.findByTxFirst(pfBsDetail.getCustNo(), pfBsDetail.getFacmNo(),
				pfBsDetail.getBormNo(), pfBsDetail.getPerfDate() + 19110000, pfBsDetail.getRepayType(),
				pfBsDetail.getPieceCode(), titaVo);
		if (pfItDetail != null) {
			totaVo.putParam("L5r29Introducer", pfItDetail.getIntroducer());
			totaVo.putParam("L5r29IntroducerName", FindEmpName(pfItDetail.getIntroducer(), titaVo));
		} else {
			totaVo.putParam("L5r29Introducer", "");
			totaVo.putParam("L5r29IntroducerName", "");
		}

		totaVo.putParam("L5r29PerfCnt", pfBsDetail.getPerfCnt());
		totaVo.putParam("L5r29PerfAmt", pfBsDetail.getPerfAmt());

		PfBsDetailAdjust pfBsDetailAdjust = pfBsDetailAdjustService.findCustBormFirst(pfBsDetail.getCustNo(),
				pfBsDetail.getFacmNo(), pfBsDetail.getBormNo(), titaVo);

		if (pfBsDetailAdjust == null) {
			totaVo.putParam("L5r29AdjLogNo", 0);
		} else {
			if (pfBsDetailAdjust.getWorkMonth() > 0) {
				this.totaVo.putParam("L5r29PerfCnt", pfBsDetailAdjust.getAdjPerfCnt());
				this.totaVo.putParam("L5r29PerfAmt", pfBsDetailAdjust.getAdjPerfAmt());
			}
			totaVo.putParam("L5r29AdjLogNo", pfBsDetailAdjust.getLogNo());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public String FindEmpName(String employeeNo, TitaVo titaVo) {
		String EmpName = "";
		if (employeeNo != null && employeeNo.length() != 0) {
			CdEmp CdEmpVo = sCdEmpService.findById(employeeNo, titaVo);
			if (CdEmpVo != null) {
				EmpName = CdEmpVo.getFullname();
			}
		}
		return EmpName;
	}
}