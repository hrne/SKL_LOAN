package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R54")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R54 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R54.class);

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	String Help = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R54 ");
		this.totaVo.init(titaVo);

		// TITA
		int iSyndNo = parse.stringToInteger(titaVo.getParam("RimSyndNo"));

		Slice<FacCaseAppl> slFacCaseAppl = null;
		List<FacCaseAppl> lFacCaseAppl = new ArrayList<FacCaseAppl>();
		FacMain tFacMain = new FacMain();
		CustMain tCustMain = new CustMain();

		slFacCaseAppl = facCaseApplService.syndNoEq(iSyndNo, 0, Integer.MAX_VALUE, titaVo);
		lFacCaseAppl = slFacCaseAppl == null ? null : new ArrayList<FacCaseAppl>(slFacCaseAppl.getContent());
		int j = 1;
		if (lFacCaseAppl != null) {
			this.info("lFacCaseAppl = " + lFacCaseAppl);
			for (FacCaseAppl tFacCaseAppl : lFacCaseAppl) {
				if (!"1".equals(tFacCaseAppl.getProcessCode())) {
					continue;
				}
				this.info("tFacCaseAppl = " + tFacCaseAppl);
				if (j > 50) {
					break;
				}
				OccursList occursList = new OccursList();
				// 核准號碼
				occursList.putParam("L2r54ApplNo", tFacCaseAppl.getApplNo());
				// 案件編號
				occursList.putParam("L2r54CreditSysNo", tFacCaseAppl.getCreditSysNo());
				// 統編
				// 戶名
				tCustMain = custMainService.findById(tFacCaseAppl.getCustUKey(), titaVo);
				if (tCustMain != null) {
					occursList.putParam("L2r54CustId", tCustMain.getCustId());
					occursList.putParam("L2r54CustName", tCustMain.getCustName());
				} else {
					occursList.putParam("L2r54CustId", "");
					occursList.putParam("L2r54CustName", "");
				}
				// 戶號
				tFacMain = facMainService.facmApplNoFirst(tFacCaseAppl.getApplNo(), titaVo);
				if (tFacMain != null) {
					occursList.putParam("L2r54CustNo", tFacMain.getCustNo());
				} else {
					occursList.putParam("L2r54CustNo", "");
				}

				this.info("occursList = " + occursList);
				this.totaVo.addOccursList(occursList);
				j++;
			}

		}
		this.info("totaVo = " + totaVo);
		this.addList(this.totaVo);
		return this.sendList();

	}

}