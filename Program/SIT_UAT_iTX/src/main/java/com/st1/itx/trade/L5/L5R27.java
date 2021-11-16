package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
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

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;

		BigDecimal UtilBal = new BigDecimal("0");
		FacMainId facMainId = new FacMainId();
		facMainId.setCustNo(iCustNo);
		facMainId.setFacmNo(iFacmNo);
		FacMain facMain = sFacMainService.findById(facMainId, titaVo);
		if (facMain == null) {
			throw new LogicException(titaVo, "E0001", "額度資料");
		}
		UtilBal = facMain.getUtilBal();

		String CustNm = "";
		CustMain CustMainVo = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (CustMainVo == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料");
		}
		CustNm = CustMainVo.getCustName();

		totaVo.putParam("L5r27CustNo", facMain.getCustNo());
		totaVo.putParam("L5r27FacmNo", facMain.getFacmNo());
		totaVo.putParam("L5r27CustNm", CustNm);
		totaVo.putParam("L5r27BsOfficer", facMain.getBusinessOfficer());

		String BsOfficerName = FindEmpName(facMain.getBusinessOfficer(), titaVo);

		totaVo.putParam("L5r27BsOfficerName", BsOfficerName);

		totaVo.putParam("L5r27Introducer", facMain.getIntroducer());

		String IntroducerName = FindEmpName(facMain.getIntroducer(), titaVo);

		totaVo.putParam("L5r27IntroducerName", IntroducerName);
		totaVo.putParam("L5r27UtilBal", UtilBal);
//		totaVo.putParam("L5r27PerfDate", pfItDetail.getPerfDate());
//		totaVo.putParam("L5r27PerfCnt", pfItDetail.getPerfCnt());
//		totaVo.putParam("L5r27PerfEqAmt", pfItDetail.getPerfEqAmt());
//		totaVo.putParam("L5r27PerfReward", pfItDetail.getPerfReward());
//		totaVo.putParam("L5r27PerfAmt", pfItDetail.getPerfAmt());
//		totaVo.putParam("L5r27CntingCode", pfItDetail.getCntingCode());

		PfItDetailAdjust pfItDetailAdjust = pfItDetailAdjustService.findCustFacmFirst(iCustNo, iFacmNo, iWorkMonth, titaVo);

		if (pfItDetailAdjust == null) {
			totaVo.putParam("L5r27AdjPerfEqAmt", new BigDecimal("0"));
			totaVo.putParam("L5r27AdjPerfReward", new BigDecimal("0"));
			totaVo.putParam("L5r27AdjPerfAmt", new BigDecimal("0"));
			totaVo.putParam("L5r27AdjCntingCode", "");
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
//		} else {
//			// E2003 查無資料
//			throw new LogicException(titaVo, "E2003", "");
//		}

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