package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdRuleCode;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CdRuleCodeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3R32")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3R32 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CdRuleCodeService cdRuleCodeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R32 ");
		this.totaVo.init(titaVo);

		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		// 撥款日期
		int iDrawdownDate = parse.stringToInteger(titaVo.getParam("RimDrawdownDate"));

		FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain != null) {
			if (!tFacMain.getRuleCode().isEmpty()) {
				CdRuleCode tCdRuleCode = cdRuleCodeService.findById(tFacMain.getRuleCode(), titaVo);
				if (tCdRuleCode != null) {
					if (tCdRuleCode.getRuleStDate() > 0 && tCdRuleCode.getRuleStDate() > iDrawdownDate) {
						this.totaVo.setWarnMsg("撥款日早於該管制項目的生效日");
					}
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}