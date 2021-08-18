package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.service.ClOwnerRelationService;

@Service("L2R52")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2R52 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R52.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public ClFacService sClFacService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public ClMainService sClMainService;

	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;

	@Autowired
	public ClLandOwnerService sClLandOwnerService;

	@Autowired
	public ClMovablesService sClMovablesService;

	@Autowired
	public ClStockService sClStockService;

	@Autowired
	public ClOtherService sClOtherService;

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R52 ");
		this.totaVo.init(titaVo);
		String iFunCd = titaVo.getParam("FunCd");

		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApproveNo"));

		if ("1".equals(iFunCd)) {
			ClMainId clMainId = new ClMainId();
			clMainId.setClCode1(iClCode1);
			clMainId.setClCode2(iClCode2);
			clMainId.setClNo(iClNo);

			ClMain clMain = sClMainService.findById(clMainId, titaVo);
			if (clMain == null) {
				throw new LogicException(titaVo, "E0001", "擔保品編號:" + iClCode1 + "-" + iClCode2 + "-" + iClNo);
			}

			ClFacId clFacId = new ClFacId();
			clFacId.setClCode1(iClCode1);
			clFacId.setClCode2(iClCode2);
			clFacId.setClNo(iClNo);
			clFacId.setApproveNo(iApplNo);
			ClFac clFac = sClFacService.findById(clFacId, titaVo);
			if (clFac != null) {
				throw new LogicException(titaVo, "E0002", "擔保品與額度關聯");
			}
		} else {
			ClFacId clFacId = new ClFacId();
			clFacId.setClCode1(iClCode1);
			clFacId.setClCode2(iClCode2);
			clFacId.setClNo(iClNo);
			clFacId.setApproveNo(iApplNo);
			ClFac clFac = sClFacService.findById(clFacId, titaVo);
			if (clFac == null) {
				throw new LogicException(titaVo, "E0001", "擔保品與額度關聯");
			}

		}

		FacMain facMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
		if (facMain == null) {
			throw new LogicException(titaVo, "E0001", "核准號碼:" + iApplNo);
		}

		this.info("L2R52 CustNo = " + facMain.getCustNo());
		CustMain custMain = sCustMainService.custNoFirst(facMain.getCustNo(), facMain.getCustNo(), titaVo);
		
		if (custMain == null) {
			throw new LogicException(titaVo, "E0001", "戶號:" + facMain.getCustNo());
		}
		
		this.totaVo.putParam("L2r52CustName", custMain.getCustName());

		HashMap<String, String> owners = new HashMap<String, String>();

		// 擔保品擔供人資料
		if (iClCode1 == 1) {
			Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
			List<ClBuildingOwner> lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
			if (lClBuildingOwner != null && lClBuildingOwner.size() > 0) {
				for (ClBuildingOwner clBuildingOwner : lClBuildingOwner) {
					owners.put(clBuildingOwner.getOwnerCustUKey(), clBuildingOwner.getOwnerCustUKey());
				}
			}
		}
		if (iClCode1 == 1 || iClCode1 == 2) {
			Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
			List<ClLandOwner> lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();
			if (lClLandOwner != null && lClLandOwner.size() > 0) {
				for (ClLandOwner clLandOwner : lClLandOwner) {
					owners.put(clLandOwner.getOwnerCustUKey(), clLandOwner.getOwnerCustUKey());
				}
			}
		}

		if (iClCode1 == 9) {
			ClMovablesId clMovablesId = new ClMovablesId();
			clMovablesId.setClCode1(iClCode1);
			clMovablesId.setClCode2(iClCode2);
			clMovablesId.setClNo(iClNo);
			ClMovables clMovables = sClMovablesService.findById(clMovablesId, titaVo);
			if (clMovables != null) {
				owners.put(clMovables.getOwnerCustUKey(), clMovables.getOwnerCustUKey());
			}
		}

		if (iClCode1 == 3 || iClCode1 == 4) {
			ClStockId clStockId = new ClStockId();
			clStockId.setClCode1(iClCode1);
			clStockId.setClCode2(iClCode2);
			clStockId.setClNo(iClNo);
			ClStock clStock = sClStockService.findById(clStockId, titaVo);
			if (clStock != null) {
				owners.put(clStock.getOwnerCustUKey(), clStock.getOwnerCustUKey());
			}
		}

		if (iClCode1 == 5) {
			ClOtherId clOtherId = new ClOtherId();
			clOtherId.setClCode1(iClCode1);
			clOtherId.setClCode2(iClCode2);
			clOtherId.setClNo(iClNo);
			ClOther clOther = sClOtherService.findById(clOtherId, titaVo);
			if (clOther != null) {
				owners.put(clOther.getOwnerCustUKey(), clOther.getOwnerCustUKey());
			}
		}

		for (HashMap.Entry<String, String> entry : owners.entrySet()) {
			this.info("L2R52 key:" + entry.getKey() + ",value:" + entry.getValue());

			String custUKey = entry.getKey();
			CustMain custMain2 = sCustMainService.findById(custUKey, titaVo);
			if (custMain2 != null) {
				OccursList occursList = new OccursList();

				occursList.putParam("L2r52OwnerCustUKey", custUKey);
				occursList.putParam("L2r52OwnerId", custMain2.getCustId());
				occursList.putParam("L2r52OwnerName", custMain2.getCustName());

				ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
				clOwnerRelationId.setClCode1(iClCode1);
				clOwnerRelationId.setClCode2(iClCode2);
				clOwnerRelationId.setClNo(iClNo);
				clOwnerRelationId.setApplNo(iApplNo);
				clOwnerRelationId.setOwnerCustUKey(custUKey);

				ClOwnerRelation clOwnerRelation = sClOwnerRelationService.findById(clOwnerRelationId, titaVo);
				if (clOwnerRelation != null) {
					occursList.putParam("L2r52OwnerRelCode", clOwnerRelation.getOwnerRelCode());
				} else {
					occursList.putParam("L2r52OwnerRelCode", "");
				}

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}