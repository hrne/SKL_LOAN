package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CollLaw;
import com.st1.itx.db.domain.CollLawId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CollLawService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R21")
@Scope("prototype")
/**
 * 法催調rim使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R21 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CollListService iCollListService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CollLawService iCollLawService;
	
	@Autowired
	public ClBuildingService iClBuildingService;

	@Autowired
	public ClLandService iClLandService;

	@Autowired
	public ClMainService iClMainService;

	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R21 ");
		this.info("L5R21 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iRimCaseCode = titaVo.getParam("RimCaseCode");
		String iRimCustNo = titaVo.getParam("RimCustNo");
		String iRimFacmNo = titaVo.getParam("RimFacmNo");
		int iRimAcDate = Integer.valueOf(titaVo.getParam("RimAcDate")) + 19110000;
		String iRimTitaTlrNo = titaVo.getParam("RimTitaTlrNo");
		String iRimTitaTxtNo = titaVo.getParam("RimTitaTxtNo");
		CollLawId iCollLawId = new CollLawId();
		CollLaw iCollLaw = new CollLaw();

		iCollLawId.setAcDate(iRimAcDate);
		iCollLawId.setCaseCode(iRimCaseCode);
		iCollLawId.setCustNo(Integer.valueOf(iRimCustNo));
		iCollLawId.setFacmNo(Integer.valueOf(iRimFacmNo));
		iCollLawId.setTitaTlrNo(iRimTitaTlrNo);
		iCollLawId.setTitaTxtNo(iRimTitaTxtNo);

		iCollLaw = iCollLawService.findById(iCollLawId, titaVo);
		if (iCollLaw != null) {
			totaVo.putParam("L5R21RecordDate", iCollLaw.getRecordDate());
			totaVo.putParam("L5R21LegalProg", iCollLaw.getLegalProg());
			totaVo.putParam("L5R21Amount", iCollLaw.getAmount());
			totaVo.putParam("L5R21Memo", iCollLaw.getMemo());
			totaVo.putParam("L5R21ReMark", iCollLaw.getRemark());
			totaVo.putParam("L5R21ClCode1", iCollLaw.getClCode1());
			totaVo.putParam("L5R21ClCode2", iCollLaw.getClCode2());
			totaVo.putParam("L5R21ClNo", iCollLaw.getClNo());
			totaVo.putParam("L5R21EditEmpNo", iCollLaw.getLastUpdateEmpNo());
			String tU = iCollLaw.getLastUpdate().toString();
			String uDate = StringUtils
					.leftPad(String.valueOf(Integer.valueOf(tU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			String uTime = tU.substring(11, 13) + tU.substring(14, 16);
			totaVo.putParam("L5R21EditDate", uDate);
			totaVo.putParam("L5R21EditTime", uTime);
			String tOther = "";
			if (iCollLaw.getClCode1() == 1) {// 房地-建物門牌
				ClBuildingId iClBuildingId = new ClBuildingId();
				ClBuilding iClBuilding = new ClBuilding();
				iClBuildingId.setClCode1(iCollLaw.getClCode1());
				iClBuildingId.setClCode2(iCollLaw.getClCode2());
				iClBuildingId.setClNo(iCollLaw.getClNo());
				iClBuilding = iClBuildingService.findById(iClBuildingId, titaVo);
				if (iClBuilding != null) {
					tOther = iClBuilding.getBdLocation() + "，建號" + iClBuilding.getBdNo1() + "-"
							+ iClBuilding.getBdNo2();
				}
			} else if (iCollLaw.getClCode1() == 2) {// 土地-土地座落
				ClLandId clLandId = new ClLandId();
				clLandId.setClCode1(iCollLaw.getClCode1());
				clLandId.setClCode2(iCollLaw.getClCode2());
				clLandId.setClNo(iCollLaw.getClNo());
				clLandId.setLandSeq(0);
				ClLand tClLand = new ClLand();
				tClLand = iClLandService.findById(clLandId, titaVo);
				if (tClLand != null) {
					tOther = tClLand.getLandLocation();
				}
			} else {// 擔保品類別代碼
				ClMainId iClMainId = new ClMainId();
				ClMain iClMain = new ClMain();
				iClMainId.setClCode1(iCollLaw.getClCode1());
				iClMainId.setClCode2(iCollLaw.getClCode2());
				iClMainId.setClNo(iCollLaw.getClNo());
				iClMain = iClMainService.findById(iClMainId, titaVo);
				if (iClMain != null) {
					String tClTypeCode = iClMain.getClTypeCode();
					if (!tClTypeCode.equals("")) {
						CdCodeId iCdCodeId = new CdCodeId();
						CdCode iCdCode = new CdCode();
						iCdCodeId.setDefCode("ClTypeCode");
						iCdCodeId.setCode(tClTypeCode);
						iCdCode = iCdCodeService.findById(iCdCodeId, titaVo);
						if (iCdCode != null) {
							tOther = iCdCode.getItem();
						}
					}
				}
			}
			totaVo.putParam("L5R21Other", tOther);
		} else {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料錯誤
		}
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		this.addList(this.totaVo);
		return this.sendList();
	}
}