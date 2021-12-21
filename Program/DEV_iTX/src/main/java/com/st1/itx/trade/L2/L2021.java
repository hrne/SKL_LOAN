package com.st1.itx.trade.L2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacRelation;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacRelationService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CaseNo=9,7<br>
 * CustNo=9,7<br>
 * CustId=X,10<br>
 * ApplNo=9,7<br>
 * END=X,1<br>
 */

@Service("L2021")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2021 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public FacRelationService sFacRelationService;

	@Autowired
	public CdEmpService sCdEmpService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2021 ");
		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();
		this.limit = 100;

		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));

		String iCustId = titaVo.getParam("CustId");

		List<FacRelation> lFacRelation = new ArrayList<FacRelation>();
		CustMain lCustMain = new CustMain();
		CdEmp tCdEmp = new CdEmp();

		Slice<FacRelation> slFacRelation = null;

		if (iCaseNo > 0) {

			slFacRelation = sFacRelationService.CreditSysNoAll(iCaseNo, this.index, this.limit, titaVo);

			lFacRelation = slFacRelation == null ? null : slFacRelation.getContent();

			if (lFacRelation == null || lFacRelation.size() == 0) {
				throw new LogicException(titaVo, "E0001", "交易關係人檔");
			}

//			String Ukey = lFacRelation.get(0).getCustUKey(); // 統編 案件編號pk
//			
//			lCustMain  = sCustMainService.findById(Ukey, titaVo);
//			
//	    	if( lCustMain == null ) {
//	    		throw new LogicException(titaVo, "E0001", "客戶主檔"); 
//	    	}

		} else {

			lCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

			if (lCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶主檔");
			}

			String Ukey = lCustMain.getCustUKey();

			slFacRelation = sFacRelationService.CustUKeyAll(Ukey, this.index, this.limit, titaVo);

			lFacRelation = slFacRelation == null ? null : slFacRelation.getContent();

			if (lFacRelation == null || lFacRelation.size() == 0) {
				throw new LogicException(titaVo, "E0001", "交易關係人檔");
			}

		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slFacRelation != null && slFacRelation.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		int Date = 0;
		for (FacRelation tFacRelation : lFacRelation) {

			OccursList occursList = new OccursList();

			occursList.putParam("OOCreditSysNo", tFacRelation.getCreditSysNo()); // 案件編號

			String Ukey = tFacRelation.getCustUKey();
			lCustMain = sCustMainService.findById(Ukey, titaVo);

			if (lCustMain != null) {
				occursList.putParam("OOCustId", lCustMain.getCustId()); // 統一編號
			} else {
				occursList.putParam("OOCustId", ""); // 統一編號
			}

			occursList.putParam("OOCustName", lCustMain.getCustName()); // 交易關係人姓名

			occursList.putParam("OOFacRelationCode", tFacRelation.getFacRelationCode()); // 掃描類別

			Date = parse.stringToInteger(df.format(tFacRelation.getCreateDate())) - 19110000;

			occursList.putParam("OOCreateDate", Date); // 建檔日期時間
			occursList.putParam("OOCreateEmpNo", tFacRelation.getCreateEmpNo()); // 建檔人員

			occursList.putParam("OOCreateEmpName", ""); // 建檔人員姓名
			tCdEmp = sCdEmpService.findById(tFacRelation.getCreateEmpNo(), titaVo);

			if (tCdEmp != null) {
				occursList.putParam("OOCreateEmpName", tCdEmp.getFullname()); // 建檔人員姓名
			}

			Date = parse.stringToInteger(df.format(tFacRelation.getLastUpdate())) - 19110000;

			occursList.putParam("OOLastUpdate", Date); // 最後更新日期時間
			occursList.putParam("OOLastUpdateEmpNo", tFacRelation.getLastUpdateEmpNo()); // 最後更新人員

			tCdEmp = sCdEmpService.findById(tFacRelation.getLastUpdateEmpNo(), titaVo);

			occursList.putParam("OOLastUpdateEmpName", ""); // 建檔人員姓名
			if (tCdEmp != null) {
				occursList.putParam("OOLastUpdateEmpName", tCdEmp.getFullname()); // 最後更新人員姓名
			}

			this.info("occursList L2021" + occursList);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}