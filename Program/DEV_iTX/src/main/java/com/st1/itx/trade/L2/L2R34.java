package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R34")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R34 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R34.class);

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R34 ");
		this.totaVo.init(titaVo);

		// tita
		// 記錄號碼
		int iRecordNo = parse.stringToInteger(titaVo.getParam("RimRecordNo"));
		// new table
		ForeclosureFee tForeclosureFee = new ForeclosureFee();

		tForeclosureFee = sForeclosureFeeService.findById(iRecordNo, titaVo);
		//查無資料拋錯
		if (tForeclosureFee == null) {
			throw new LogicException(titaVo, "E0001", "法拍費主檔"); // 查詢資料不存在
		}

		this.totaVo.putParam("L2r34CustNo", tForeclosureFee.getCustNo());
		this.totaVo.putParam("L2r34FacmNo", tForeclosureFee.getFacmNo());
		this.totaVo.putParam("L2r34ReceiveDate", tForeclosureFee.getReceiveDate());
		this.totaVo.putParam("L2r34DocDate", tForeclosureFee.getDocDate());
		this.totaVo.putParam("L2r34OpenAcDate", tForeclosureFee.getOpenAcDate());
		this.totaVo.putParam("L2r34CloseDate", tForeclosureFee.getCloseDate());
		this.totaVo.putParam("L2r34Fee", tForeclosureFee.getFee());
		this.totaVo.putParam("L2r34FeeCode", tForeclosureFee.getFeeCode());
		this.totaVo.putParam("L2r34LegalStaff", tForeclosureFee.getLegalStaff());
		this.totaVo.putParam("L2r34CloseNo", tForeclosureFee.getCloseNo());
		this.totaVo.putParam("L2r34Rmk", tForeclosureFee.getRmk());
		this.totaVo.putParam("L2r34CaseCode", tForeclosureFee.getCaseCode());
		this.totaVo.putParam("L2r34RemitBranch", tForeclosureFee.getRemitBranch());
		this.totaVo.putParam("L2r34Remitter", tForeclosureFee.getRemitter());
		this.totaVo.putParam("L2r34CaseNo", tForeclosureFee.getCaseNo());
		this.totaVo.putParam("L2r34OverdueDate", tForeclosureFee.getOverdueDate());

		this.addList(this.totaVo);
		return this.sendList();
	}
}