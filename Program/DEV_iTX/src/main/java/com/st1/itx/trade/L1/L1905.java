package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUSTID=X,10<br>
 * CUSTNO=X,7<br>
 * RETURN=9,1<br>
 * END=X,1<br>
 * RTNTXT=X,9<br>
 */

@Service("L1905")
@Scope("prototype")
public class L1905 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1905.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustTelNoService sCustTelNoService;
	
	@Autowired
	public CdEmpService iCdEmpService;
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1905 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200; // 229 * 200 = 45800

		String custId = titaVo.getParam("CustId").trim();
		int icustNo = parse.stringToInteger(titaVo.getParam("CustNo").trim());

		CustMain tCustMain = new CustMain();

		if (!custId.isEmpty()) {

			tCustMain = sCustMainService.custIdFirst(custId, titaVo);

		} else if (icustNo > 0) {
			tCustMain = sCustMainService.custNoFirst(icustNo, icustNo, titaVo);
		} else {
			throw new LogicException(titaVo, "E1001", "L1905");
		}

		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔");
		}

		String custUKey = tCustMain.getCustUKey().trim();

		List<CustTelNo> lCustTelNo = new ArrayList<CustTelNo>();

		Slice<CustTelNo> slCustTelNo = sCustTelNoService.findCustUKey(custUKey, this.index, this.limit, titaVo);
		lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCustTelNo != null && slCustTelNo.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		/* 如有有找到資料 */
		if (lCustTelNo != null && lCustTelNo.size() > 0) {

			this.info("L1905 listCustTelNo size = " + lCustTelNo.size());

			for (CustTelNo tCustTelNo : lCustTelNo) {
				OccursList occursList = new OccursList();
				/* key 名稱需與L1001.tom相同 檔案位於.6的iTX/L1/L1001.tom */
				String iLastTelNo = tCustTelNo.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				iCdEmp = iCdEmpService.findById(iLastTelNo, titaVo);
				
				occursList.putParam("OOTelTypeCode", tCustTelNo.getTelTypeCode());
				occursList.putParam("OOTelArea", tCustTelNo.getTelArea());
				occursList.putParam("OOTelNo", tCustTelNo.getTelNo());
				occursList.putParam("OOTelExt", tCustTelNo.getTelExt());
//				occursList.putParam("OOMobile", tCustTelNo.getMobile()); 因改電話格式廢除
				occursList.putParam("OORelationCode", tCustTelNo.getRelationCode());
				occursList.putParam("OOLiaisonName", tCustTelNo.getLiaisonName());	
				if(iCdEmp == null) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				}else {
					occursList.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
				}
				String tU = tCustTelNo.getLastUpdate().toString();
				String uDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(tU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uDate = uDate.substring(0,3)+"/"+uDate.substring(3, 5)+"/"+uDate.substring(5);
				String uTime = tU.substring(11,19);
				occursList.putParam("OOLastUpdate",uDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastTelNo);
				occursList.putParam("OOEnable", tCustTelNo.getEnable());
				occursList.putParam("OOTelChgRsnCode", tCustTelNo.getTelChgRsnCode());
				occursList.putParam("OORmk", tCustTelNo.getRmk());
				occursList.putParam("OOStopReason", tCustTelNo.getStopReason());
				occursList.putParam("OOTelNoUKey", tCustTelNo.getTelNoUKey());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException("E0001", "客戶連絡電話檔");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}