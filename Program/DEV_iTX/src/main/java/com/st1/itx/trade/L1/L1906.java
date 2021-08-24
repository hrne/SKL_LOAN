package com.st1.itx.trade.L1;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustRelDetail;
import com.st1.itx.db.domain.CustRelMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustRelDetailService;
import com.st1.itx.db.service.CustRelMainService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 * CustId=X,10<br>
 * RETURN=9,1<br>
 * END=X,1<br>
 * RTNTXT=X,9<br>
 */

@Service("L1906")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L1906 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustRelMainService iCustRelMainService;

	@Autowired
	public CustRelDetailService iCustRelDetailService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1906 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; // 68 * 500 = 34000

		String iCustId = titaVo.getParam("CustId");
		String iRelMainUKey = "";
		CustRelMain iCustRelMain = new CustRelMain();
		Slice<CustRelDetail> iCustRelDetail = null; // 客戶為主
		Slice<CustRelDetail> rCustRelDetail = null; // 客戶為關聯戶
		iCustRelMain = iCustRelMainService.custRelIdFirst(iCustId, titaVo);
		if (iCustRelMain == null) {
			throw new LogicException("E0001", "客戶關係主檔查無資料:" + iCustId); // 查無資料
		}
		iRelMainUKey = iCustRelMain.getUkey();
		iCustRelDetail = iCustRelDetailService.custRelMainUKeyEq(iRelMainUKey, this.index, this.limit, titaVo);

		if (iCustRelDetail == null) {
			throw new LogicException("E0001", "客戶關係明細檔查無資料:" + iCustId); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (iCustRelDetail != null && iCustRelDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		// 客戶關聯檔寫入tota
		for (CustRelDetail tCustRelDetail : iCustRelDetail) {
			CdEmp iCdEmp = new CdEmp();
			iCdEmp = iCdEmpService.findById(tCustRelDetail.getLastUpdateEmpNo(), titaVo);

			// new occurs
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustRelMainUKey", tCustRelDetail.getCustRelMainUKey());
			occursList.putParam("OOUkey", tCustRelDetail.getUkey());
			occursList.putParam("OOCustRelId", iCustRelMain.getCustRelId());
			occursList.putParam("OOCustRelName", iCustRelMain.getCustRelName());
			occursList.putParam("OORelTypeCode", tCustRelDetail.getRelTypeCode());
			occursList.putParam("OORelId", tCustRelDetail.getRelId());
			occursList.putParam("OORelName", tCustRelDetail.getRelName());
			occursList.putParam("OORelationCode", tCustRelDetail.getRelationCode());
			occursList.putParam("OORemark", tCustRelDetail.getRemark());
			occursList.putParam("OOStatus", tCustRelDetail.getStatus());
			occursList.putParam("OONote", tCustRelDetail.getNote().replace("$n", "\n"));
			String tC = tCustRelDetail.getCreateDate().toString();
			String tU = tCustRelDetail.getLastUpdate().toString();
			String cDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(tC.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			String uDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(tU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			cDate = cDate.substring(0, 3) + "/" + cDate.substring(3, 5) + "/" + cDate.substring(5);
			uDate = uDate.substring(0, 3) + "/" + uDate.substring(3, 5) + "/" + uDate.substring(5);
			String cTime = tC.substring(11, 19);
			String uTime = tU.substring(11, 19);

			occursList.putParam("OOCreateDate", cDate + " " + cTime);
			occursList.putParam("OOLastUpdate", uDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", tCustRelDetail.getLastUpdateEmpNo());
			if (iCdEmp == null) {
				occursList.putParam("OOLastUpdateEmpNoX", "");
			} else {
				this.info("名稱===" + iCdEmp.getFullname());
				occursList.putParam("OOLastUpdateEmpNoX", iCdEmp.getFullname());
			}
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);

		}
		rCustRelDetail = iCustRelDetailService.relIdEq(iCustId, this.index, this.limit, titaVo);
		if (rCustRelDetail != null) {
			for (CustRelDetail aCustRelDetail : rCustRelDetail) {
				CdEmp iCdEmp = new CdEmp();
				iCdEmp = iCdEmpService.findById(aCustRelDetail.getLastUpdateEmpNo(), titaVo);
				CustRelMain rCustRelMain = new CustRelMain();
				rCustRelMain = iCustRelMainService.findById(aCustRelDetail.getCustRelMainUKey(), titaVo);
				// new occurs
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustRelMainUKey", aCustRelDetail.getCustRelMainUKey());
				occursList.putParam("OOUkey", aCustRelDetail.getUkey());
				occursList.putParam("OOCustRelId", rCustRelMain.getCustRelId());
				occursList.putParam("OOCustRelName", rCustRelMain.getCustRelName());
				occursList.putParam("OORelTypeCode", aCustRelDetail.getRelTypeCode());
				occursList.putParam("OORelId", aCustRelDetail.getRelId());
				occursList.putParam("OORelName", aCustRelDetail.getRelName());
				occursList.putParam("OORelationCode", aCustRelDetail.getRelationCode());
				occursList.putParam("OORemark", aCustRelDetail.getRemark());
				occursList.putParam("OOStatus", aCustRelDetail.getStatus());
				occursList.putParam("OONote", aCustRelDetail.getNote().replace("$n", "\n"));
				String taC = aCustRelDetail.getCreateDate().toString();
				String taU = aCustRelDetail.getLastUpdate().toString();
				String caDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taC.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
				caDate = caDate.substring(0, 3) + "/" + caDate.substring(3, 5) + "/" + caDate.substring(5);
				uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
				String cTime = taC.substring(11, 19);
				String uTime = taU.substring(11, 19);

				occursList.putParam("OOCreateDate", caDate + " " + cTime);
				occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
				occursList.putParam("OOLastUpdateEmpNo", aCustRelDetail.getLastUpdateEmpNo());
				if (iCdEmp == null) {
					occursList.putParam("OOLastUpdateEmpNoX", "");
				} else {
					occursList.putParam("OOLastUpdateEmpNoX", iCdEmp.getFullname());
				}
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}