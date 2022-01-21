package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.StringCut;

@Service("L1R13")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R13 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public CustNoticeService iCustNoticeService;
	@Autowired
	public CustTelNoService iCustTelNoService;
	@Autowired
	public FacMainService iFacMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R13 ");
		this.totaVo.init(titaVo);
		int iRimFunc = Integer.valueOf(titaVo.getParam("RimFunc"));
		int iRimCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		int iRimFacmNo = Integer.valueOf(titaVo.getParam("RimFacmNo"));
		Slice<CustNotice> iCustNotice = null;
		CustMain iCustMain = new CustMain();
		Slice<CustTelNo> iCustTelNo = null;
		FacMainId iFacMainId = new FacMainId();
		iFacMainId.setCustNo(iRimCustNo);
		iFacMainId.setFacmNo(iRimFacmNo);
		FacMain iFacMain = new FacMain();
		iCustNotice = iCustNoticeService.facmNoEq(iRimCustNo, iRimFacmNo, iRimFacmNo, this.index, this.limit, titaVo);
		iCustMain = iCustMainService.custNoFirst(iRimCustNo, iRimCustNo, titaVo);
		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", "此戶號不存在。");
		}
		iCustTelNo = iCustTelNoService.findCustUKey(iCustMain.getCustUKey(), this.index, this.limit, titaVo);
		if (iRimFacmNo != 0) {
			iFacMain = iFacMainService.findById(iFacMainId, titaVo);
		}
		String iMsgFg = "0";
		String iEmailFg = "0";
		if (iRimFunc == 1 || iRimFunc == 3) {
			if (iCustNotice != null) {
				throw new LogicException(titaVo, "E0005", "該戶號額度已存在於客戶通知設定檔。");
			}
		} else {
			if (iCustNotice == null) {
				throw new LogicException(titaVo, "E0001", "該戶號額度不存在於客戶通知設定檔。");
			}
		}
		if (iFacMain == null && iRimFacmNo != 0) {
			throw new LogicException(titaVo, "E0005", "該額度不存在");
		}
		if (iCustMain != null) {
			if (!iCustMain.getEmail().equals("")) {
				iEmailFg = "1";
			}
		}
		if (iCustTelNo != null) {
			for (CustTelNo xCustTelNo : iCustTelNo) {
				if (xCustTelNo.getTelTypeCode().equals("05")) {
					iMsgFg = "1";
				}
			}
		}
		this.totaVo.putParam("L1R13CustName", StringCut.replaceLineUp(iCustMain.getCustName()));
		this.totaVo.putParam("L1R13MsgFg", iMsgFg);
		this.totaVo.putParam("L1R13EmailFg", iEmailFg);
		this.addList(this.totaVo);
		return this.sendList();
	}
}