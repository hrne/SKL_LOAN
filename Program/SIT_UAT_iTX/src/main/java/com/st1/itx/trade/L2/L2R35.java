package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.GuarantorId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R35")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R35 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R35.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public GuarantorService sGuarantorService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R35 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能
		String iFunCd = titaVo.getParam("RimFunCd");
		// 核准號碼
		int iApproveNo = parse.stringToInteger(titaVo.getParam("RIMApproveNo"));
		// 統編
		String iCustId = titaVo.getParam("RimCustId");

		// new table
		CustMain tCustMain = new CustMain();
		Guarantor tGuarantor = new Guarantor();
		// new pk
		GuarantorId GuarantorId = new GuarantorId();
		// 統編取客戶主檔custukey
		tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		String custUKey = tCustMain.getCustUKey();
		// 塞pk
		GuarantorId.setApproveNo(iApproveNo);
		GuarantorId.setGuaUKey(custUKey);

		tGuarantor = sGuarantorService.findById(GuarantorId, titaVo);

		if (iFunCd.equals("1")) {
			if (tGuarantor == null) {
				tGuarantor = new Guarantor();
			} else {
				// 若為新增，但資料已存在，拋錯
				throw new LogicException("E0002", "保證人檔");
			}
		} else {
			if (tGuarantor == null) {
				throw new LogicException(titaVo, "E2003", "查無保證人檔資料"); // 查無資料
			}
		}

		this.totaVo.putParam("L2r35RelInd", tGuarantor.getGuaRelCode());
		this.totaVo.putParam("L2r35GuaAmt", tGuarantor.getGuaAmt());
		this.totaVo.putParam("L2r35GuaType", tGuarantor.getGuaTypeCode());
		this.totaVo.putParam("L2r35GuaDt", tGuarantor.getGuaDate());
		this.totaVo.putParam("L2r35StatCd", tGuarantor.getGuaStatCode());
		this.totaVo.putParam("L2r35CancelDt", tGuarantor.getCancelDate());

		this.addList(this.totaVo);
		return this.sendList();
	}
}