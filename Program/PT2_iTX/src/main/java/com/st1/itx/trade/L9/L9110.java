package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L9110 首次撥款審核資料表
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9110")
@Scope("prototype")
public class L9110 extends TradeBuffer {
	@Autowired
	FacMainService facMainService;
	@Autowired
	CdBaseRateService cdBaseRateService;

	/* 報表服務注入 */
	@Autowired
	L9110Report l9110Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9110 ");
		this.totaVo.init(titaVo);
		String applNo = "";
		String errorApplNo = "";
		for (int currentApplNoItem = 1; currentApplNoItem <= 50; currentApplNoItem++) {
			applNo = titaVo.getParam("APPLNO" + currentApplNoItem);
			if (applNo != null && !(applNo.isEmpty()) && Integer.parseInt(applNo) > 0) {
				this.info("applNo = " + applNo);
				FacMain tFacMain = facMainService.facmApplNoFirst(Integer.parseInt(applNo), titaVo);
				if (tFacMain == null) {
					errorApplNo += " " + applNo;
				}
			}
		}
		if (!errorApplNo.isEmpty()) {
			throw new LogicException(titaVo, "E0001", "核准號碼 = " + errorApplNo); // 查詢資料不存在
		}
		for (int currentApplNoItem = 1; currentApplNoItem <= 50; currentApplNoItem++) {
			applNo = titaVo.getParam("APPLNO" + currentApplNoItem);
			if (applNo != null && !(applNo.isEmpty()) && Integer.parseInt(applNo) > 0) {
				FacMain tFacMain = facMainService.facmApplNoFirst(Integer.parseInt(applNo), titaVo);
				tFacMain = facMainService.holdById(tFacMain, titaVo);
				tFacMain.setL9110Flag("Y");
				if (tFacMain.getLastBormNo() == 0 && !"99".equals(tFacMain.getBaseRateCode())) {
					BigDecimal approveRate = getBaseRate(tFacMain.getBaseRateCode(), titaVo)
							.add(tFacMain.getRateIncr());
					this.info("applNo = " + applNo + "ApproveRate=" + tFacMain.getApproveRate() + "/" + approveRate);
					if (approveRate.compareTo(tFacMain.getApproveRate()) != 0) {
						tFacMain.setApproveRate(approveRate);
					}
				}
				try {
					facMainService.update(tFacMain, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "額度主檔" + applNo);
				}
			}
		}

		this.info("L9110 titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9110Report.setParentTranCode(parentTranCode);

		l9110Report.exec(titaVo, this.totaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private BigDecimal getBaseRate(String baseRateCode, TitaVo titaVo) {
		BigDecimal baseRate = BigDecimal.ZERO;
		CdBaseRate tCdBaseRate = new CdBaseRate();
		tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst("TWD", baseRateCode, 19110101,
				titaVo.getEntDyI() + 19110000, titaVo);
		this.info(" cdBaseRate date =" + titaVo.getEntDyI());
		if (tCdBaseRate != null) {
			baseRate = tCdBaseRate.getBaseRate();
		}
		return baseRate;
	}

}