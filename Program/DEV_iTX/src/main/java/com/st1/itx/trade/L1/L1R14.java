package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustRelMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustRelDetailService;
import com.st1.itx.db.service.CustRelMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R14")
@Scope("prototype")
/**
 * 客戶關係調Rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R14 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1R14.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public CustRelMainService iCustRelMainService;
	@Autowired
	public CustRelDetailService iCustRelDetailService;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R14 ");
		this.totaVo.init(titaVo);
		String iRimCustId = titaVo.getParam("RimCustId");
		CustMain iCustMain = new CustMain();
		CustRelMain iCustRelMain = new CustRelMain();
		//關係人主檔
		iCustMain = iCustMainService.custIdFirst(iRimCustId, titaVo);
		iCustRelMain = iCustRelMainService.custRelIdFirst(iRimCustId, titaVo);
		if (iCustRelMain != null) {
			totaVo.putParam("L1R14Name", iCustRelMain.getCustRelName());
			totaVo.putParam("L1R14IsForeigner", iCustRelMain.getIsForeigner());
		}else {
			if (iCustMain == null) {
				totaVo.putParam("L1R14Name", "");
				totaVo.putParam("L1R14IsForeigner", "");
			}else {
				totaVo.putParam("L1R14Name", iCustMain.getCustName());
				totaVo.putParam("L1R14IsForeigner", "");
			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}