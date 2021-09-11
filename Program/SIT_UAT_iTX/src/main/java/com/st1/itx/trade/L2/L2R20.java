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
import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.CdInsurerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R20")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R20 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingService sClBuildingService;

	/* DB服務注入 */
	@Autowired
	public ClLandService sClLandService;

	@Autowired
	public CdInsurerService sCdInsurerService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R20 ");
		this.totaVo.init(titaVo);


		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));

		ClMainId ClMainId = new ClMainId();
		ClImmId ClImmId = new ClImmId();

		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		ClImmId.setClCode1(iClCode1);
		ClImmId.setClCode2(iClCode2);
		ClImmId.setClNo(iClNo);

		this.index = titaVo.getReturnIndex();

		// *** 折返控制相關 ***
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 129 * 400 = 51600

		ClImm tClImm = sClImmService.findById(ClImmId, titaVo);
		ClMain tClMain = sClMainService.findById(ClMainId, titaVo);
		if (tClImm == null) {
			throw new LogicException(titaVo, "E2003", "擔保品不動產檔"); // 查無資料
		}
		if (tClMain == null) {
			throw new LogicException(titaVo, "E2003", "擔保品主檔"); // 查無資料
		}


		// 查詢保險公司資料檔
		Slice<CdInsurer> slCdInsurer;
		slCdInsurer = sCdInsurerService.insurerTypeRange("1", "2", tClImm.getEvaCompanyCode(), tClImm.getEvaCompanyCode(), this.index, this.limit, titaVo);

		List<CdInsurer> lCdInsurer = slCdInsurer == null ? null : slCdInsurer.getContent();

		String OEvaCompanyX = "";
		// 如有找到資料
		if (lCdInsurer != null) {
			for (CdInsurer tCdInsurer : lCdInsurer) {
				OEvaCompanyX = tCdInsurer.getInsurerItem();
			}
		}

		this.totaVo.putParam("L2r20ClTypeCode", tClMain.getClTypeCode());
		this.totaVo.putParam("L2r20EvaDate", tClMain.getEvaDate());
		this.totaVo.putParam("L2r20EvaAmt", tClMain.getEvaAmt());
		this.totaVo.putParam("L2r20EvaNetWorth", tClImm.getEvaNetWorth());
		this.totaVo.putParam("L2r20LVITax", tClImm.getLVITax());
		this.totaVo.putParam("L2r20RentEvaValue", tClImm.getRentEvaValue());
		this.totaVo.putParam("L2r20RentPrice", tClImm.getRentPrice());
		this.totaVo.putParam("L2r20EvaCompany", tClImm.getEvaCompanyCode());
		this.totaVo.putParam("L2r20EvaCompanyX", OEvaCompanyX);

		this.totaVo.putParam("L2r20OwnershipCode", tClImm.getOwnershipCode());
		this.totaVo.putParam("L2r20MtgCode", tClImm.getMtgCode());
		this.totaVo.putParam("L2r20MtgCheck", tClImm.getMtgCheck());
		this.totaVo.putParam("L2r20MtgLoan", tClImm.getMtgLoan());
		this.totaVo.putParam("L2r20MtgPledge", tClImm.getMtgPledge());
		this.totaVo.putParam("L2r20ClStatus", tClMain.getClStatus());
		this.totaVo.putParam("L2r20ClStat", tClImm.getClStat());
		this.totaVo.putParam("L2r20Agreement", tClImm.getAgreement());
		this.totaVo.putParam("L2r20LimitCancelDate", tClImm.getLimitCancelDate());
		this.totaVo.putParam("L2r20ClCode", tClImm.getClCode());
		this.totaVo.putParam("L2r20LoanToValue", tClImm.getLoanToValue());
		this.totaVo.putParam("L2r20OtherOwnerTotal", tClImm.getOtherOwnerTotal());
		this.totaVo.putParam("L2r20CompensationCopy", tClImm.getCompensationCopy());
		this.totaVo.putParam("L2r20BdRmk", tClImm.getBdRmk());
		this.totaVo.putParam("L2r20DispPrice", tClMain.getDispPrice());
		this.totaVo.putParam("L2r20DispDate", tClMain.getDispDate());
		this.totaVo.putParam("L2r20MtgReasonCode", tClImm.getMtgReasonCode());
		this.totaVo.putParam("L2r20ReceivedDate", tClImm.getReceivedDate());
		this.totaVo.putParam("L2r20ReceivedNo", tClImm.getReceivedNo());
		this.totaVo.putParam("L2r20CancelDate", tClImm.getCancelDate());
		this.totaVo.putParam("L2r20CancelNo", tClImm.getCancelNo());
		this.totaVo.putParam("L2r20SettingDate", tClImm.getSettingDate());
		this.totaVo.putParam("L2r20SettingStat", tClImm.getSettingStat());
		this.totaVo.putParam("L2r20SettingAmt", tClImm.getSettingAmt());
		this.totaVo.putParam("L2r20ClaimDate", tClImm.getClaimDate());
		this.totaVo.putParam("L2r20SettingSeq", tClImm.getSettingSeq());
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}