package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacRelationService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.FacShareRelationService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.springjpa.cm.L2022ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2022")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2022 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public FacShareApplService sFacShareApplService;

	@Autowired
	public GuarantorService sGuarantorService;

	@Autowired
	public ClFacService sClFacService;

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

	@Autowired
	public FacRelationService sFacRelationService;

	@Autowired
	public FacShareRelationService sFacShareRelationService;

	@Autowired
	public CdGuarantorService sCdGuarantorService;

	@Autowired
	public CdCodeService sCdCodeService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	/* DB服務注入 */
	@Autowired
	public L2022ServiceImpl sL2022ServiceImpl;

	HashMap<String, String> owners = new HashMap<String, String>();

	String mainRel = "";
	String mainCustId = "";
	int mainCustNo = 0;
	String mainCustName = "";
	int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2022 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 50; // 9 * 15 * 376 = 50760 1次最多9筆occurs

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = sL2022ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2022ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0001", "L2022");

		}

		List<LinkedHashMap<String, String>> chkOccursList = null;
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				if (parse.stringToInteger(result.get("CreditSysNo")) == 0) {
					occursList.putParam("oCreditSysNo", "");
				} else {
					occursList.putParam("oCreditSysNo", result.get("CreditSysNo"));
				}
				occursList.putParam("oApplNo", result.get("ApplNo"));
				occursList.putParam("oCustNo", result.get("CustNo"));
				occursList.putParam("oFacmNo", result.get("FacmNo"));
				occursList.putParam("oCustName", result.get("CustName"));
				occursList.putParam("oUKey", result.get("UKey"));
				occursList.putParam("oId", result.get("Id"));
				occursList.putParam("oName", result.get("Name"));
				occursList.putParam("oType", result.get("Type"));
				occursList.putParam("oRelation", result.get("Relation"));
				occursList.putParam("oClCode1", result.get("ClCode1"));
				occursList.putParam("oClCode2", result.get("ClCode2"));
				occursList.putParam("oClNo", result.get("ClNo"));
				occursList.putParam("oModify", result.get("Modify"));

				this.totaVo.addOccursList(occursList);

			}

			chkOccursList = this.totaVo.getOccursList();

			if (resultList.size() >= this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}

		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException("E0001", ""); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}