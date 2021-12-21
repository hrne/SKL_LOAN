package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R47")
@Scope("prototype")
/**
 * 
 * 
 * @author chih cheng
 * @version 1.0.0
 */
public class L2R47 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R47.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R47 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("Rim2R47CustId");

		CustMain iCustMain = new CustMain();

		iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);

		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", "查無此編號"); // 查無資料錯誤
		} else {
			totaVo.putParam("L2R47CustNo", iCustMain.getCustNo());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}