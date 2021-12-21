package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClLand;
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

@Service("L2911")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2911 extends TradeBuffer {

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
		this.info("active L2911 ");
		this.totaVo.init(titaVo);

		Slice<ClLand> slClLand = null;
//		String custid = "";
//		String custname = "";

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

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
//		CustMain tCustMain = sCustMainService.findById(tClMain.getCustUKey(), titaVo);
//		if (tCustMain != null) {
//			custid = tCustMain.getCustId();
//			custname = tCustMain.getCustName();
//
//		}

		ClBuilding tClBuilding = sClBuildingService.findById(new ClBuildingId(iClCode1, iClCode2, iClNo), titaVo);

		slClLand = sClLandService.findClNo(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
		List<ClLand> lClLand = slClLand == null ? null : new ArrayList<ClLand>(slClLand.getContent());

//		// 查詢保險公司資料檔
//		Slice<CdInsurer> slCdInsurer;
//		slCdInsurer = sCdInsurerService.insurerTypeRange("1", "2", tClImm.getEvaCompanyCode(), tClImm.getEvaCompanyCode(), this.index, this.limit, titaVo);

//		List<CdInsurer> lCdInsurer = slCdInsurer == null ? null : slCdInsurer.getContent();

//		String OEvaCompanyX = "";
//		// 如有找到資料
//		if (lCdInsurer != null) {
//			for (CdInsurer tCdInsurer : lCdInsurer) {
//				OEvaCompanyX = tCdInsurer.getInsurerItem();
//			}
//		}

//		this.totaVo.putParam("OCustId", custid);
//		this.totaVo.putParam("OCustName", custname);
//		this.totaVo.putParam("OClCode1", tClImm.getClCode1());
//		this.totaVo.putParam("OClCode2", tClImm.getClCode2());
//		this.totaVo.putParam("OClNo", tClImm.getClNo());
//		this.totaVo.putParam("OClTypeCode", tClMain.getClTypeCode());
//		this.totaVo.putParam("OEvaDate", tClMain.getEvaDate());
//		this.totaVo.putParam("OEvaAmt", tClMain.getEvaAmt());
//		this.totaVo.putParam("OEvaNetWorth", tClImm.getEvaNetWorth());
//		this.totaVo.putParam("OLVITax", tClImm.getLVITax());
//		this.totaVo.putParam("ORentEvaValue", tClImm.getRentEvaValue());
//		this.totaVo.putParam("ORentPrice", tClImm.getRentPrice());
//		this.totaVo.putParam("OEvaCompany", tClImm.getEvaCompanyCode());
//		this.totaVo.putParam("OEvaCompanyX", OEvaCompanyX);
//
//		this.totaVo.putParam("OOwnershipCode", tClImm.getOwnershipCode());
//		this.totaVo.putParam("OMtgCode", tClImm.getMtgCode());
//		this.totaVo.putParam("OMtgCheck", tClImm.getMtgCheck());
//		this.totaVo.putParam("OMtgLoan", tClImm.getMtgLoan());
//		this.totaVo.putParam("OMtgPledge", tClImm.getMtgPledge());
//		this.totaVo.putParam("OClStatus", tClMain.getClStatus());
//		this.totaVo.putParam("OClStat", tClImm.getClStat());
//		this.totaVo.putParam("OAgreement", tClImm.getAgreement());
//		this.totaVo.putParam("OLimitCancelDate", tClImm.getLimitCancelDate());
//		this.totaVo.putParam("OClCode", tClImm.getClCode());
//		this.totaVo.putParam("OLoanToValue", tClImm.getLoanToValue());
//		this.totaVo.putParam("OOtherOwnerTotal", tClImm.getOtherOwnerTotal());
//		this.totaVo.putParam("OCompensationCopy", tClImm.getCompensationCopy());
//		this.totaVo.putParam("OBdRmk", tClImm.getBdRmk());
//		this.totaVo.putParam("OSynd", tClMain.getSynd());
//		this.totaVo.putParam("OSyndCode", tClMain.getSyndCode());
//		this.totaVo.putParam("ODispPrice", tClMain.getDispPrice());
//		this.totaVo.putParam("ODispDate", tClMain.getDispDate());
//		this.totaVo.putParam("OMtgReasonCode", tClImm.getMtgReasonCode());
//		this.totaVo.putParam("OReceivedDate", tClImm.getReceivedDate());
//		this.totaVo.putParam("OReceivedNo", tClImm.getReceivedNo());
//		this.totaVo.putParam("OCancelDate", tClImm.getCancelDate());
//		this.totaVo.putParam("OCancelNo", tClImm.getCancelNo());
//		this.totaVo.putParam("OSettingDate", tClImm.getSettingDate());
//		this.totaVo.putParam("OSettingStat", tClImm.getSettingStat());
//		this.totaVo.putParam("OSettingAmt", tClImm.getSettingAmt());
//		this.totaVo.putParam("OClaimDate", tClImm.getClaimDate());
//		this.totaVo.putParam("OSettingSeq", tClImm.getSettingSeq());
//		this.totaVo.putParam("OFirstAmt", tClImm.getFirstAmt());
//		this.totaVo.putParam("OFirstCreditor", tClImm.getFirstCreditor());
//		this.totaVo.putParam("OSecondAmt", tClImm.getSecondAmt());
//		this.totaVo.putParam("OSecondCreditor", tClImm.getSecondCreditor());
//		this.totaVo.putParam("OThirdAmt", tClImm.getThirdAmt());
//		this.totaVo.putParam("OThirdCreditor", tClImm.getThirdCreditor());

		if (tClBuilding != null) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOtradecode", "L2915");
			occursList.putParam("OOLandSeq", "");
			occursList.putParam("OOAddress", tClBuilding.getBdLocation() + "，建號" + tClBuilding.getBdNo1() + "-" + tClBuilding.getBdNo2());
			this.totaVo.addOccursList(occursList);
		}
		if (lClLand != null) {
			for (ClLand tClLand : lClLand) {
				OccursList occursList = new OccursList();

				occursList.putParam("OOtradecode", "L2916");
				occursList.putParam("OOLandSeq", tClLand.getLandSeq());
				occursList.putParam("OOAddress", tClLand.getLandLocation());
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}