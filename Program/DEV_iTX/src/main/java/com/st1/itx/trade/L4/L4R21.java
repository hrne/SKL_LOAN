package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R21")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R21 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R21.class);

	@Autowired
	public Parse parse;

	@Autowired
	public FacMainService facMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R21 ");
		this.totaVo.init(titaVo);

//		RimFindCode
//		1.查CustMain
//		2.查FacMain
		int custNo = 0;
		int facmNo = 0;
		int code = parse.stringToInteger(titaVo.get("RimFindCode"));
		int result = 0;

//		return
//		0.查無資料
//		1.有值

		switch (code) {
		case 1:
			custNo = parse.stringToInteger(titaVo.get("RimCustNo"));
			CustMain tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
			if (tCustMain != null) {
				result = 1;
			}

			break;
		case 2:
			custNo = parse.stringToInteger(titaVo.get("RimCustNo"));
			facmNo = parse.stringToInteger(titaVo.get("RimFacmNo"));

			FacMainId tFacMainId = new FacMainId();
			tFacMainId.setCustNo(custNo);
			tFacMainId.setFacmNo(facmNo);

			FacMain tFacMain = facMainService.findById(tFacMainId, titaVo);
			if (tFacMain != null) {
				result = 1;
			}
			break;
		}

		totaVo.putParam("L4r21CheckCode", result);

		this.addList(this.totaVo);
		return this.sendList();
	}
}