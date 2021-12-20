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
import com.st1.itx.db.domain.PfItDetailAdjust;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.PfItDetailAdjustService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R27")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R27 extends TradeBuffer {

	@Autowired
	public PfItDetailService sPfItDetailService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public PfBsDetailService sPfBsDetailService;

	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public PfItDetailAdjustService pfItDetailAdjustService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R27 ");
		this.totaVo.init(titaVo);
		this.info("L5R27 Run");

		long logNo = Long.valueOf(titaVo.getParam("LogNo"));

		PfItDetail pfItDetail = sPfItDetailService.findById(logNo, titaVo);

		if (pfItDetail == null) {
			throw new LogicException(titaVo, "E0001", "介紹人業績資料");
		}
		// UtilBal = facMain.getUtilBal();

		String CustNm = "";
		CustMain CustMainVo = sCustMainService.custNoFirst(pfItDetail.getCustNo(), pfItDetail.getCustNo(), titaVo);
		if (CustMainVo == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料");
		}
		CustNm = CustMainVo.getCustName();

		totaVo.putParam("L5r27CustNo", pfItDetail.getCustNo());
		totaVo.putParam("L5r27FacmNo", pfItDetail.getFacmNo());
		totaVo.putParam("L5r27BormNo", pfItDetail.getBormNo());
		totaVo.putParam("L5r27WorkMonth", pfItDetail.getWorkMonth() - 191100);

		totaVo.putParam("L5r27CustNm", CustNm);

		PfBsDetail pfBsItDetail = sPfBsDetailService.findByTxFirst(pfItDetail.getCustNo(), pfItDetail.getFacmNo(),
				pfItDetail.getBormNo(), pfItDetail.getPerfDate(), pfItDetail.getRepayType(), pfItDetail.getPieceCode(),
				titaVo);

		if (pfBsItDetail == null) {
			totaVo.putParam("L5r27BsOfficer", "");
			totaVo.putParam("L5r27BsOfficerName", "");
		} else {
			totaVo.putParam("L5r27BsOfficer", pfBsItDetail.getBsOfficer());
			String BsOfficerName = FindEmpName(pfBsItDetail.getBsOfficer(), titaVo);
			totaVo.putParam("L5r27BsOfficerName", BsOfficerName);

		}

		totaVo.putParam("L5r27Introducer", pfItDetail.getIntroducer());

		String IntroducerName = FindEmpName(pfItDetail.getIntroducer(), titaVo);

		totaVo.putParam("L5r27IntroducerName", IntroducerName);
		totaVo.putParam("L5r27UtilBal", pfItDetail.getDrawdownAmt());

		PfItDetailAdjust pfItDetailAdjust = pfItDetailAdjustService.findCustFacmBormFirst(pfItDetail.getCustNo(),
				pfItDetail.getFacmNo(), pfItDetail.getBormNo(), titaVo);

		if (pfItDetailAdjust == null || pfItDetailAdjust.getAdjRange() == 0) {
			totaVo.putParam("L5r27AdjPerfEqAmt", pfItDetail.getPerfEqAmt());
			totaVo.putParam("L5r27AdjPerfReward", pfItDetail.getPerfReward());
			totaVo.putParam("L5r27AdjPerfAmt", pfItDetail.getPerfAmt());
			totaVo.putParam("L5r27AdjCntingCode", pfItDetail.getCntingCode());
			totaVo.putParam("L5r27AdjRange", 0);
			totaVo.putParam("L5r27AdjLogNo", 0);
		} else {
			totaVo.putParam("L5r27AdjPerfEqAmt", pfItDetailAdjust.getAdjPerfEqAmt());
			totaVo.putParam("L5r27AdjPerfReward", pfItDetailAdjust.getAdjPerfReward());
			totaVo.putParam("L5r27AdjPerfAmt", pfItDetailAdjust.getAdjPerfAmt());
			totaVo.putParam("L5r27AdjCntingCode", pfItDetailAdjust.getAdjCntingCode());
			totaVo.putParam("L5r27AdjRange", pfItDetailAdjust.getAdjRange());
			totaVo.putParam("L5r27AdjLogNo", pfItDetailAdjust.getLogNo());
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