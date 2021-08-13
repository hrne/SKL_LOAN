package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R32")
@Scope("prototype")
/**
 * 清償作業RIM
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R32 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R32.class);

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public ClBuildingService sClBuildingService;
	@Autowired
	public ClImmService sClImmService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public LoanBorTxService sLoanBorTxService;
	@Autowired
	public LoanBorMainService sloanBorMainService;
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public AcReceivableService acReceivableService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;

	private ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
	int iCustNo;
	int iCaseCloseCode;
	int iFacmNo;
	int iTranDate;
	int iFunCode;
	String  iCollectFlag;
	private BigDecimal oTotal = new BigDecimal(0);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R32 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// 取得輸入資料
		iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		iTranDate = this.parse.stringToInteger(titaVo.getParam("TranDate"));
		iFunCode = this.parse.stringToInteger(titaVo.getParam("FunCode"));
		
		// 2: 補領
		if (iFunCode == 2) {
			getCloseBreachAmt(titaVo);
		}

		this.totaVo.putParam("L2r32Total", oTotal); // 總計

		//
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void getCloseBreachAmt(TitaVo titaVo) throws LogicException {
		oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iCustNo, iFacmNo, 0, null, titaVo);
		// 輸出清償違約金
		if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
			for (LoanCloseBreachVo v : oListCloseBreach) {
				if (v.getCloseBreachAmt().compareTo(BigDecimal.ZERO) > 0) {
					oTotal = oTotal.add(v.getCloseBreachAmt());
				}
			}
		}
	}
}