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
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R45")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R45 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private Slice<FacClose> slFacClose;
	private FacClose tFacClose = new FacClose();
	// 作業項目
	private List<String> lfunCode = new ArrayList<String>();

	private int iCustNo;
	private int iFacmNo;
	private String iFunCode;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R45 ");
		this.totaVo.init(titaVo);

		// tita參數
		iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		iFunCode = titaVo.getParam("RimFunCode");

		if ("1".equals(iFunCode)) {
			lfunCode.add("0");
		} else {
			lfunCode.add("2");
			lfunCode.add("3");
		}

		tFacClose = sFacCloseService.findFacmNoFirst(iCustNo, iFacmNo, lfunCode, titaVo);

		if (tFacClose == null) {
			throw new LogicException(titaVo, "E2003", "查無清償作業檔資料"); // 查無資料
		}
		if (!("3".equals(iFunCode) || "2".equals(iFunCode))) {
			if (!(tFacClose.getCloseDate() > 0)) {
				throw new LogicException(titaVo, "E0010", "(需做結案登錄)"); // 查無資料
			}
		}

		this.totaVo.putParam("L2r45CustNo", tFacClose.getCustNo());
		this.totaVo.putParam("L2r45CloseNo", tFacClose.getCloseNo());
		this.totaVo.putParam("L2r45FacmNo", tFacClose.getFacmNo());
		this.totaVo.putParam("L2r45ActFlag", tFacClose.getActFlag());
		this.totaVo.putParam("L2r45FunCode", tFacClose.getFunCode());
		this.totaVo.putParam("L2r45CarLoan", tFacClose.getCarLoan());
		this.totaVo.putParam("L2r45ApplDate", tFacClose.getApplDate());
		this.totaVo.putParam("L2r45CloseDate", tFacClose.getCloseDate());
		this.totaVo.putParam("L2r45CloseReasonCode", tFacClose.getCloseReasonCode());
		this.totaVo.putParam("L2r45TwCloseAmt", tFacClose.getCloseAmt());
		this.totaVo.putParam("L2r45UsCloseAmt", tFacClose.getCloseAmt());
		this.totaVo.putParam("L2r45CollectFlag", tFacClose.getCollectFlag());
		this.totaVo.putParam("L2r45CollectWayCode", tFacClose.getCollectWayCode());
		this.totaVo.putParam("L2r45ReceiveDate", tFacClose.getReceiveDate());
		this.totaVo.putParam("L2r45TelNo1", tFacClose.getTelNo1());
		this.totaVo.putParam("L2r45TelNo2", tFacClose.getTelNo2());
		this.totaVo.putParam("L2r45TelNo3", tFacClose.getTelNo3());
		this.totaVo.putParam("L2r45EntryDate", tFacClose.getEntryDate());
		this.totaVo.putParam("L2r45AgreeNo", tFacClose.getAgreeNo());
		this.totaVo.putParam("L2r45DocNo", tFacClose.getDocNo());
		this.totaVo.putParam("L2r45ClsNo", tFacClose.getClsNo());
		this.totaVo.putParam("L2r45Rmk", tFacClose.getRmk());
		this.totaVo.putParam("L2r45ClCode1", tFacClose.getClCode1());
		this.totaVo.putParam("L2r45ClCode2", tFacClose.getClCode2());
		this.totaVo.putParam("L2r45ClNo", tFacClose.getClNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}