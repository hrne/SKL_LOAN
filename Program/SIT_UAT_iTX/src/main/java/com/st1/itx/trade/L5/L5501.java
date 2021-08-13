package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.util.common.SendRsp;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * BormNo=9,3<br>
 * Introducer=X,8<br>
 * BsOfficer=X,8<br>
 * PerfDate=9,7<br>
 * AdjPerfCnt=9,1.1<br>
 * AdjPerfEqAmt=9,14.2<br>
 * AdjPerfReward=9,14.2<br>
 * AdjPerfAmt=9,14.2<br>
 */

@Service("L5501")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5501 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public DataLog dataLog;

	@Autowired
	public SendRsp sendRsp;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public PfItDetailService sPfItDetailService;

	@Autowired
	public CdEmpService sCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5501");
		this.info("active L5501 ");
		this.totaVo.init(titaVo);

		Long iLogNo = Long.valueOf(titaVo.get("LogNo").trim());
		String CustNo = titaVo.getParam("CustNo").trim();// 借款人戶號
		String FacmNo = titaVo.getParam("FacmNo").trim();// 額度編號
		String BormNo = titaVo.getParam("BormNo").trim();// 撥款序號
		titaVo.getParam("Introducer").trim();
		titaVo.getParam("BsOfficer").trim();
		String PerfDate = titaVo.getParam("PerfDate").trim();// 業績日期
		String AdjIntroducer = titaVo.getParam("AdjIntroducer").trim();// 調整後戶名
//		String AdjPerfCnt = titaVo.getParam("AdjPerfCnt").trim();// 調整後件數
		String AdjCntingCode = titaVo.getParam("AdjCntingCode").trim();// 調整後是否計件
		String AdjPerfEqAmt = titaVo.getParam("AdjPerfEqAmt").trim();// 調整後換算業績
		String AdjPerfReward = titaVo.getParam("AdjPerfReward").trim();// 調整後業務報酬
		String AdjPerfAmt = titaVo.getParam("AdjPerfAmt").trim();// 調整後業績金額

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

		// 交易需主管核可
//		if (!titaVo.getHsupCode().equals("1")) {
//			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "修改介紹人業績");
//		}

		// PfItDetail
		
		// 以下段落再修改
		PfItDetail tPfItDetail = sPfItDetailService.holdById(iLogNo, titaVo);
		PfItDetail oPfItDetail = (PfItDetail) dataLog.clone(tPfItDetail);// 資料異動前
		if (tPfItDetail != null) {
			// "CdEmp" E0 ON E0."EmployeeNo" = B."BsOfficer" --AdjIntroducer
			// 查驗介紹人是否在 員工資料檔案(CdEmp)內 --不確定 額度主黨(FacMain)
			if (AdjIntroducer != null || !"".equals(AdjIntroducer)) {
				CdEmp CdEmpVO = sCdEmpService.findById(AdjIntroducer, titaVo);
				if (CdEmpVO == null) {
					throw new LogicException(titaVo, "E0006", "查無介紹人" + AdjIntroducer + "員工資料");
				}
			}
			// 放行 1:登 2:放
			int ActFg = titaVo.getActFgI();
			if (ActFg == 2) {

				tPfItDetail.setIntroducer(AdjIntroducer);// 介紹人
//			tPfItDetail.setPerfCnt(new BigDecimal(AdjPerfCnt));// 是否計件
				tPfItDetail.setCntingCode(AdjCntingCode);// 調整後是否計件
				tPfItDetail.setPerfAmt(new BigDecimal(AdjPerfAmt));// 業績金額
				tPfItDetail.setPerfReward(new BigDecimal(AdjPerfReward));// 業務報酬
				tPfItDetail.setPerfEqAmt(new BigDecimal(AdjPerfEqAmt));// 換算業績

				try {

					tPfItDetail = sPfItDetailService.update2(tPfItDetail, titaVo);
					dataLog.setEnv(titaVo, oPfItDetail, tPfItDetail);// 資料異動後-2
					dataLog.exec();// 資料異動後-3
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "");
				}
			}

		} else {
			// E0006 鎖定資料時，發生錯誤
			throw new LogicException(titaVo, "E0003", "");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}