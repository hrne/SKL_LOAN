package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.DailyLoanBalService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L8701")
@Scope("prototype")
/**
 * 產製公務人員報送資料<BR>
 * 
 * 
 * @author Lai
 * @version 1.0.0
 */

public class L8701 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	@Autowired
	public CustMainService custMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public DailyLoanBalService dailyLoanBalService;
	@Autowired
	public AcDetailService acDetailService;

	@Autowired
	public MakeExcel makeExcel;
	@Autowired
	public MakeFile makeFile;
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8701 ");
		this.totaVo.init(titaVo);

		// 執行背景交易
		MySpring.newTask("L8701Batch", this.txBuffer, titaVo);

		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

		this.addList(this.totaVo);
		return this.sendList();
	}

}