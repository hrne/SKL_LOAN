package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustDataCtrlService;
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

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public CustDataCtrlService custDataCtrlService;

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

		CustMain tCustMain = new CustMain();
		List<CustDataCtrl> lCustDataCtrl = new ArrayList<CustDataCtrl>();
		Slice<CustDataCtrl> slCustDataCtrl = null;
		int custNo = 0;
//		檢查客戶檔與結清戶個資控管檔的統編是否存在
		tCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
		slCustDataCtrl = custDataCtrlService.findCustId(iCustId, 0, Integer.MAX_VALUE, titaVo);
		lCustDataCtrl = slCustDataCtrl == null ? null : slCustDataCtrl.getContent();

		if (tCustMain != null) {
			custNo = tCustMain.getCustNo();
		}
		if (lCustDataCtrl != null) {
			custNo = lCustDataCtrl.get(0).getCustNo();
		}
		if (lCustDataCtrl == null && tCustMain == null) {

			throw new LogicException(titaVo, "E0001", "客戶資料主檔" ); // 查無資料錯誤
		}

		totaVo.putParam("L2R47CustNo", custNo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}