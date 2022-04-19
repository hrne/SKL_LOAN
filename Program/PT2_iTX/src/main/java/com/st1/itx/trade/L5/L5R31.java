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
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R31")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R31 extends TradeBuffer {
	@Autowired
	public PfRewardService sPfRewardService;

	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public PfBsDetailService sPfBsDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R31 ");
		this.totaVo.init(titaVo);
		// L5053調RIM
		this.info("L5R30");
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
		PfReward tPfReward = sPfRewardService.findById(iLogNo, titaVo);
		if (tPfReward != null) {
			String CustNm = "";
			CustMain CustMainVo = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			if (CustMainVo != null) {
				CustNm = CustMainVo.getCustName();
			}
			PfBsDetail tPfBsDetail = sPfBsDetailService.findBormNoLatestFirst(iCustNo, iFacmNo, iBormNo, titaVo);
			String BsOfficer = "";
			String PieceCode = "";
			if (tPfBsDetail != null) {
				BsOfficer = tPfBsDetail.getBsOfficer();
				PieceCode = tPfBsDetail.getPieceCode();
			}
			String Introducer = tPfReward.getIntroducer();
			String BsOfficerName = FindEmpName(BsOfficer, titaVo);
			String IntroducerName = FindEmpName(Introducer, titaVo);
			String CoorgnizerName = FindEmpName(tPfReward.getCoorgnizer(), titaVo);

			totaVo.putParam("L5r31PerfDate", tPfReward.getPerfDate());
			totaVo.putParam("L5r31CustNo", tPfReward.getCustNo());
			totaVo.putParam("L5r31FacmNo", tPfReward.getFacmNo());
			totaVo.putParam("L5r31BormNo", tPfReward.getBormNo());
			totaVo.putParam("L5r31CustNm", CustNm);
			totaVo.putParam("L5r31PieceCode", PieceCode);
			totaVo.putParam("L5r31BsOfficer", BsOfficer);
			totaVo.putParam("L5r31BsOfficerName", BsOfficerName);
			totaVo.putParam("L5r31Introducer", Introducer);
			totaVo.putParam("L5r31IntroducerName", IntroducerName);

			totaVo.putParam("L5r31InterviewerA", tPfReward.getInterviewerA());
			totaVo.putParam("L5r31InterviewerAName", FindEmpName(tPfReward.getInterviewerA(), titaVo));
			totaVo.putParam("L5r31InterviewerB", tPfReward.getInterviewerB());
			totaVo.putParam("L5r31InterviewerBName", FindEmpName(tPfReward.getInterviewerB(), titaVo));

			totaVo.putParam("L5r31Coorgnizer", tPfReward.getCoorgnizer());
			totaVo.putParam("L5r31CoorgnizerBonus", tPfReward.getCoorgnizerBonus());
			totaVo.putParam("L5r31CoorgnizerName", CoorgnizerName);

			totaVo.putParam("L5r31IntroducerAddBonus", tPfReward.getIntroducerAddBonus());
		} else {
			// E2003 查無資料
			throw new LogicException(titaVo, "E2003", "PfReward");
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