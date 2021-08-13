package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CollLaw;
import com.st1.itx.db.domain.CollLawId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollLawService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R21")
@Scope("prototype")
/**
 * 法催調rim使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R21 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CollListService iCollListService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CollLawService iCollLawService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R21 ");
		this.info("L5R21 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iRimCaseCode = titaVo.getParam("RimCaseCode");
		String iRimCustNo = titaVo.getParam("RimCustNo");
		String iRimFacmNo = titaVo.getParam("RimFacmNo");
		int iRimAcDate = Integer.valueOf(titaVo.getParam("RimAcDate")) + 19110000;
		String iRimTitaTlrNo = titaVo.getParam("RimTitaTlrNo");
		String iRimTitaTxtNo = titaVo.getParam("RimTitaTxtNo");
		CollLawId iCollLawId = new CollLawId();
		CollLaw iCollLaw = new CollLaw();

		iCollLawId.setAcDate(iRimAcDate);
		iCollLawId.setCaseCode(iRimCaseCode);
		iCollLawId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollLawId.setFacmNo(Integer.valueOf(iRimFacmNo));
		iCollLawId.setTitaTlrNo(iRimTitaTlrNo);
		iCollLawId.setTitaTxtNo(iRimTitaTxtNo);

		iCollLaw = iCollLawService.findById(iCollLawId, titaVo);
		if (iCollLaw != null) {
			totaVo.putParam("L5R21RecordDate", iCollLaw.getRecordDate());
			totaVo.putParam("L5R21LegalProg", iCollLaw.getLegalProg());
			totaVo.putParam("L5R21Amount", iCollLaw.getAmount());
			totaVo.putParam("L5R21Memo", iCollLaw.getMemo());
			totaVo.putParam("L5R21ReMark", iCollLaw.getRemark());
			totaVo.putParam("L5R21ClCode1", iCollLaw.getClCode1());
			totaVo.putParam("L5R21ClCode2", iCollLaw.getClCode2());
			totaVo.putParam("L5R21ClNo", iCollLaw.getClNo());
		} else {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}