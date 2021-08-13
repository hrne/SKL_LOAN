package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R36")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R36 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R36.class);

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R36 ");
		this.totaVo.init(titaVo);

		// tita參數
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 清償序號
		int iCloseNo = parse.stringToInteger(titaVo.getParam("RimCloseNo"));

		// new PK
		FacCloseId FacCloseId = new FacCloseId();
		// new table
		FacClose tFacClose = new FacClose();

		FacCloseId.setCustNo(iCustNo);
		FacCloseId.setCloseNo(iCloseNo);

		tFacClose = sFacCloseService.findById(FacCloseId, titaVo);
		if (tFacClose == null) {
			throw new LogicException(titaVo, "E2003", "查無清償作業檔資料"); // 查無資料
		}
		// 建檔日期
		int CreateDate = parse.stringToInteger((String.valueOf(tFacClose.getCreateDate().toString().substring(0, 4)
				+ tFacClose.getCreateDate().toString().substring(5, 7)
				+ tFacClose.getCreateDate().toString().substring(8, 10)))) - 19110000;

		this.totaVo.putParam("L2r36FacmNo", tFacClose.getFacmNo());
		this.totaVo.putParam("L2r36CloseReasonCode", tFacClose.getCloseReasonCode());
		this.totaVo.putParam("L2r36CloseAmt", tFacClose.getCloseAmt());
		this.totaVo.putParam("L2r36CollectWayCode", tFacClose.getCollectWayCode());
		this.totaVo.putParam("L2r36ReceiveDate", tFacClose.getReceiveDate());
		this.totaVo.putParam("L2r36AgreeNo", tFacClose.getAgreeNo());
		this.totaVo.putParam("L2r36DocNo", tFacClose.getDocNo());
		this.totaVo.putParam("L2r36ClsNo", tFacClose.getClsNo());
		this.totaVo.putParam("L2r36CloseDate", tFacClose.getCloseDate());
		this.totaVo.putParam("L2r36TelNo1", tFacClose.getTelNo1());
		this.totaVo.putParam("L2r36TelNo2", tFacClose.getTelNo2());
		this.totaVo.putParam("L2r36CreateDate", CreateDate);
		this.totaVo.putParam("L2r36FunCd", tFacClose.getFunCode());
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}