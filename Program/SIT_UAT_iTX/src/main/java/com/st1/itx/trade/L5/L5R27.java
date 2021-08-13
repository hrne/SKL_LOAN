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
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfItDetailService;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R27 ");
		this.totaVo.init(titaVo);
		this.info("L5R27 Run");
		Long iLogNo = Long.valueOf(titaVo.get("LogNo").trim());
		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("RimFacmNo"));
		int iBormNo = Integer.valueOf(titaVo.getParam("RimBormNo"));
		String PerfDate = titaVo.getParam("RimPerfDate");
		int IntPerfDate = 0;
		if (PerfDate != null && PerfDate.length() != 0) {
			IntPerfDate = Integer.parseInt(PerfDate);
			if (IntPerfDate != 0 && String.valueOf(IntPerfDate).length() <= 7) {
				IntPerfDate = IntPerfDate + 19110000;
			}
		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[業績日期]未填寫");
		}

		// PfItDetail
		PfItDetail PfItDetailVO = sPfItDetailService.findById(iLogNo, titaVo);
		if (PfItDetailVO != null) {
			String CustNm = "";
			CustMain CustMainVo = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			if (CustMainVo != null) {
				CustNm = CustMainVo.getCustName();
			}

			String BsOfficer = "";
			PfBsDetail tPfBsDetail = sPfBsDetailService.findBormNoLatestFirst(iCustNo, iFacmNo, iBormNo, titaVo);
			if (tPfBsDetail != null) {
				BsOfficer = tPfBsDetail.getBsOfficer();
			}
			String Introducer = PfItDetailVO.getIntroducer();
			String BsOfficerName = FindEmpName(BsOfficer, titaVo);
			String IntroducerName = FindEmpName(Introducer, titaVo);

			totaVo.putParam("L5r27CustNo", PfItDetailVO.getCustNo());
			totaVo.putParam("L5r27FacmNo", PfItDetailVO.getFacmNo());
			totaVo.putParam("L5r27BormNo", PfItDetailVO.getBormNo());
			totaVo.putParam("L5r27CustNm", CustNm);
			totaVo.putParam("L5r27BsOfficer", BsOfficer);
			totaVo.putParam("L5r27BsOfficerName", BsOfficerName);
			totaVo.putParam("L5r27PerfDate", PfItDetailVO.getPerfDate());
			totaVo.putParam("L5r27Introducer", Introducer);
			totaVo.putParam("L5r27IntroducerName", IntroducerName);
			totaVo.putParam("L5r27PerfCnt", PfItDetailVO.getPerfCnt());
			totaVo.putParam("L5r27PerfEqAmt", PfItDetailVO.getPerfEqAmt());
			totaVo.putParam("L5r27PerfReward", PfItDetailVO.getPerfReward());
			totaVo.putParam("L5r27PerfAmt", PfItDetailVO.getPerfAmt());
			totaVo.putParam("L5r27CntingCode", PfItDetailVO.getCntingCode());
		} else {
			// E2003 查無資料
			throw new LogicException(titaVo, "E2003", "");
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