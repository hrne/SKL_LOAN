package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R55")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R55 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public GuarantorService guarantorService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	String Help = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R55 ");
		this.totaVo.init(titaVo);

		// TITA
		int iSyndNo = parse.stringToInteger(titaVo.getParam("RimSyndNo"));

		Slice<FacCaseAppl> slFacCaseAppl = null;
		List<FacCaseAppl> lFacCaseAppl = new ArrayList<FacCaseAppl>();
		Slice<Guarantor> slGuarantor = null;
		List<Guarantor> lGuarantor = new ArrayList<Guarantor>();
		FacMain tFacMain = new FacMain();

		BigDecimal wkBaseRate = BigDecimal.ZERO;

		slFacCaseAppl = facCaseApplService.syndNoEq(iSyndNo, 0, Integer.MAX_VALUE, titaVo);
		lFacCaseAppl = slFacCaseAppl == null ? null : new ArrayList<FacCaseAppl>(slFacCaseAppl.getContent());
		int j = 1;

		if (lFacCaseAppl != null) {
			for (FacCaseAppl tFacCaseAppl : lFacCaseAppl) {
				this.info("tFacCaseAppl = " + tFacCaseAppl);

				if (!"1".equals(tFacCaseAppl.getProcessCode())) {
					continue;
				}
				if (j > 50) {
					break;
				}
				OccursList occursList = new OccursList();

				// 戶號
				tFacMain = facMainService.facmApplNoFirst(tFacCaseAppl.getApplNo(), titaVo);
				if (tFacMain != null) {
					this.info("tFacMain = " + tFacMain);
					occursList.putParam("L2r55CustNo", tFacMain.getCustNo());// 戶號
					occursList.putParam("L2r55FacmNo", tFacMain.getFacmNo());// 額度
					wkBaseRate = tFacMain.getApproveRate().subtract(tFacMain.getRateIncr());
					occursList.putParam("L2r55BaseRate", wkBaseRate); // 利率基準=核准利率-加碼利率
					occursList.putParam("L2r55RateIncr", tFacMain.getRateIncr()); // 加碼利率
					// 動支期限
					if (tFacMain.getRecycleCode().equals("1")) {

						occursList.putParam("L2r55Deadline", tFacMain.getRecycleDeadline());
					} else {
						occursList.putParam("L2r55Deadline", tFacMain.getUtilDeadline());
					}
					occursList.putParam("L2r55MaturityDate", tFacMain.getMaturityDate());// 到期日
					// 是否存在保證人記號
					slGuarantor = guarantorService.approveNoEq(tFacCaseAppl.getApplNo(), 0, 1, titaVo);
					if (slGuarantor == null) {
						occursList.putParam("L2r55GuaFg", "");
					} else {
						occursList.putParam("L2r55GuaFg", "Y");
					}

				}

				this.totaVo.addOccursList(occursList);
				j++;
			}

		}
		this.info("totaVo = " + totaVo);
		this.addList(this.totaVo);
		return this.sendList();

	}

}