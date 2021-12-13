package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ045LogService;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.db.service.JcicZ046LogService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047LogService;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ048LogService;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ049LogService;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.db.service.JcicZ050LogService;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.db.service.JcicZ051LogService;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.db.service.JcicZ052LogService;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.service.JcicZ053LogService;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.db.service.JcicZ054LogService;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.service.JcicZ055LogService;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.db.service.JcicZ056LogService;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.db.service.JcicZ060LogService;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ061LogService;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.db.service.JcicZ062LogService;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.JcicZ063LogService;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.db.service.JcicZ440LogService;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446LogService;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448LogService;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ450LogService;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.db.service.JcicZ451LogService;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.db.service.JcicZ454LogService;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 * MEMTXCD=X,3<br>
 * CUSTID=X,10<br>
 * RCDATE=9,7<br>
 * END=X,1<br>
 */

@Service("L8030")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8030 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public JcicZ040Service iJcicZ040Service;
	@Autowired
	public JcicZ041Service iJcicZ041Service;
	@Autowired
	public JcicZ042Service iJcicZ042Service;
	@Autowired
	public JcicZ043Service iJcicZ043Service;
	@Autowired
	public JcicZ044Service iJcicZ044Service;
	@Autowired
	public JcicZ045Service iJcicZ045Service;
	@Autowired
	public JcicZ046Service iJcicZ046Service;
	@Autowired
	public JcicZ047Service iJcicZ047Service;
	@Autowired
	public JcicZ048Service iJcicZ048Service;
	@Autowired
	public JcicZ049Service iJcicZ049Service;
	@Autowired
	public JcicZ050Service iJcicZ050Service;
	@Autowired
	public JcicZ051Service iJcicZ051Service;
	@Autowired
	public JcicZ052Service iJcicZ052Service;
	@Autowired
	public JcicZ053Service iJcicZ053Service;
	@Autowired
	public JcicZ054Service iJcicZ054Service;
	@Autowired
	public JcicZ055Service iJcicZ055Service;
	@Autowired
	public JcicZ056Service iJcicZ056Service;
	@Autowired
	public JcicZ060Service iJcicZ060Service;
	@Autowired
	public JcicZ061Service iJcicZ061Service;
	@Autowired
	public JcicZ062Service iJcicZ062Service;
	@Autowired
	public JcicZ063Service iJcicZ063Service;
	@Autowired
	public JcicZ440Service iJcicZ440Service;
	@Autowired
	public JcicZ442Service iJcicZ442Service;
	@Autowired
	public JcicZ443Service iJcicZ443Service;
	@Autowired
	public JcicZ444Service iJcicZ444Service;
	@Autowired
	public JcicZ446Service iJcicZ446Service;
	@Autowired
	public JcicZ447Service iJcicZ447Service;
	@Autowired
	public JcicZ448Service iJcicZ448Service;
	@Autowired
	public JcicZ450Service iJcicZ450Service;
	@Autowired
	public JcicZ451Service iJcicZ451Service;
	@Autowired
	public JcicZ454Service iJcicZ454Service;
	@Autowired
	public JcicZ570Service iJcicZ570Service;
	@Autowired
	public JcicZ571Service iJcicZ571Service;
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	@Autowired
	public JcicZ573Service iJcicZ573Service;
	@Autowired
	public JcicZ574Service iJcicZ574Service;
	@Autowired
	public JcicZ575Service iJcicZ575Service;
	@Autowired
	public JcicZ040LogService iJcicZ040LogService;
	@Autowired
	public JcicZ041LogService iJcicZ041LogService;
	@Autowired
	public JcicZ042LogService iJcicZ042LogService;
	@Autowired
	public JcicZ043LogService iJcicZ043LogService;
	@Autowired
	public JcicZ044LogService iJcicZ044LogService;
	@Autowired
	public JcicZ045LogService iJcicZ045LogService;
	@Autowired
	public JcicZ046LogService iJcicZ046LogService;
	@Autowired
	public JcicZ047LogService iJcicZ047LogService;
	@Autowired
	public JcicZ048LogService iJcicZ048LogService;
	@Autowired
	public JcicZ049LogService iJcicZ049LogService;
	@Autowired
	public JcicZ050LogService iJcicZ050LogService;
	@Autowired
	public JcicZ051LogService iJcicZ051LogService;
	@Autowired
	public JcicZ052LogService iJcicZ052LogService;
	@Autowired
	public JcicZ053LogService iJcicZ053LogService;
	@Autowired
	public JcicZ054LogService iJcicZ054LogService;
	@Autowired
	public JcicZ055LogService iJcicZ055LogService;
	@Autowired
	public JcicZ056LogService iJcicZ056LogService;
	@Autowired
	public JcicZ060LogService iJcicZ060LogService;
	@Autowired
	public JcicZ061LogService iJcicZ061LogService;
	@Autowired
	public JcicZ062LogService iJcicZ062LogService;
	@Autowired
	public JcicZ063LogService iJcicZ063LogService;
	@Autowired
	public JcicZ440LogService iJcicZ440LogService;
	@Autowired
	public JcicZ442LogService iJcicZ442LogService;
	@Autowired
	public JcicZ443LogService iJcicZ443LogService;
	@Autowired
	public JcicZ444LogService iJcicZ444LogService;
	@Autowired
	public JcicZ446LogService iJcicZ446LogService;
	@Autowired
	public JcicZ447LogService iJcicZ447LogService;
	@Autowired
	public JcicZ448LogService iJcicZ448LogService;
	@Autowired
	public JcicZ450LogService iJcicZ450LogService;
	@Autowired
	public JcicZ451LogService iJcicZ451LogService;
	@Autowired
	public JcicZ454LogService iJcicZ454LogService;
	@Autowired
	public JcicZ570LogService iJcicZ570LogService;
	@Autowired
	public JcicZ571LogService iJcicZ571LogService;
	@Autowired
	public JcicZ572LogService iJcicZ572LogService;
	@Autowired
	public JcicZ573LogService iJcicZ573LogService;
	@Autowired
	public JcicZ574LogService iJcicZ574LogService;
	@Autowired
	public JcicZ575LogService iJcicZ575LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Autowired
	public CdCityService iCdCityService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8030 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40; // 232 * 40 = 9280
		/*
		 * 程式邏輯by Fegie occursListA =>查詢結果固定返回新增資料 occursListB =>可異動刪除資料 OOStatusFg =>
		 * 狀態參數 0:新增 1:異動 2:關閉按鈕 OOHistoryFg => log檔筆數不等於0筆時為1:開啟 否則為0:關閉 OODeleteFg =>
		 * 當該筆資料之轉jcic日期為空白時為1:開啟 否則為0:關閉
		 */
		String iTranCode = titaVo.getParam("TranCode");
		String iCustId = titaVo.getParam("CustId");
		switch (iTranCode) {
		case "040":
			dealZ040(iCustId, this.index, this.limit, titaVo);
			break;
		case "041":
			dealZ041(iCustId, this.index, this.limit, titaVo);
			break;
		case "042":
			dealZ042(iCustId, this.index, this.limit, titaVo);
			break;
		case "043":
			dealZ043(iCustId, this.index, this.limit, titaVo);
			break;
		case "044":
			dealZ044(iCustId, this.index, this.limit, titaVo);
			break;
		case "045":
			dealZ045(iCustId, this.index, this.limit, titaVo);
			break;
		case "046":
			dealZ046(iCustId, this.index, this.limit, titaVo);
			break;
		case "047":
			dealZ047(iCustId, this.index, this.limit, titaVo);
			break;
		case "048":
			dealZ048(iCustId, this.index, this.limit, titaVo);
			break;
		case "049":
			dealZ049(iCustId, this.index, this.limit, titaVo);
			break;
		case "050":
			dealZ050(iCustId, this.index, this.limit, titaVo);
			break;
		case "051":
			dealZ051(iCustId, this.index, this.limit, titaVo);
			break;
		case "052":
			dealZ052(iCustId, this.index, this.limit, titaVo);
			break;
		case "053":
			dealZ053(iCustId, this.index, this.limit, titaVo);
			break;
		case "054":
			dealZ054(iCustId, this.index, this.limit, titaVo);
			break;
		case "055":
			dealZ055(iCustId, this.index, this.limit, titaVo);
			break;
		case "056":
			dealZ056(iCustId, this.index, this.limit, titaVo);
			break;
		case "060":
			dealZ060(iCustId, this.index, this.limit, titaVo);
			break;
		case "061":
			dealZ061(iCustId, this.index, this.limit, titaVo);
			break;
		case "062":
			dealZ062(iCustId, this.index, this.limit, titaVo);
			break;
		case "063":
			dealZ063(iCustId, this.index, this.limit, titaVo);
			break;
		case "440":
			dealZ440(iCustId, this.index, this.limit, titaVo);
			break;
		case "442":
			dealZ442(iCustId, this.index, this.limit, titaVo);
			break;
		case "443":
			dealZ443(iCustId, this.index, this.limit, titaVo);
			break;
		case "444":
			dealZ444(iCustId, this.index, this.limit, titaVo);
			break;
		case "446":
			dealZ446(iCustId, this.index, this.limit, titaVo);
			break;
		case "447":
			dealZ447(iCustId, this.index, this.limit, titaVo);
			break;
		case "448":
			dealZ448(iCustId, this.index, this.limit, titaVo);
			break;
		case "450":
			dealZ450(iCustId, this.index, this.limit, titaVo);
			break;
		case "451":
			dealZ451(iCustId, this.index, this.limit, titaVo);
			break;
		case "454":
			dealZ454(iCustId, this.index, this.limit, titaVo);
			break;
		case "570":
			dealZ570(iCustId, this.index, this.limit, titaVo);
			break;
		case "571":
			dealZ571(iCustId, this.index, this.limit, titaVo);
			break;
		case "572":
			dealZ572(iCustId, this.index, this.limit, titaVo);
			break;
		case "573":
			dealZ573(iCustId, this.index, this.limit, titaVo);
			break;
		case "574":
			dealZ574(iCustId, this.index, this.limit, titaVo);
			break;
		case "575":
			dealZ575(iCustId, this.index, this.limit, titaVo);
			break;
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void dealZ040(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8301");
		occursListA.putParam("OOHistoryTxCd", "L8031");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ040> sJcicZ040 = null;
		sJcicZ040 = iJcicZ040Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ040 != null) {
			for (JcicZ040 xJcicZ040 : sJcicZ040) {
				OccursList occursListB = new OccursList();
				// 040 layout回傳
				occursListB.putParam("OOChainTxCd", "L8301");
				occursListB.putParam("OOHistoryTxCd", "L8031");
				occursListB.putParam("OOUkey", xJcicZ040.getUkey());
				occursListB.putParam("OOCustId", xJcicZ040.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ040.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ040.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ040.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ040.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ040.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ040 rJcicZ040 = new JcicZ040();
				JcicZ040Id rJcicZ040Id = new JcicZ040Id();
				rJcicZ040Id.setRcDate(xJcicZ040.getRcDate());
				rJcicZ040Id.setCustId(xJcicZ040.getCustId());
				rJcicZ040Id.setSubmitKey(xJcicZ040.getSubmitKey());
				rJcicZ040 = iJcicZ040Service.findById(rJcicZ040Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ040.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ040Log> iJcicZ040Log = null;
				iJcicZ040Log = iJcicZ040LogService.ukeyEq(rJcicZ040.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ040Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ041(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8302");
		occursListA.putParam("OOHistoryTxCd", "L8032");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ041> sJcicZ041 = null;
		sJcicZ041 = iJcicZ041Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ041 != null) {
			for (JcicZ041 xJcicZ041 : sJcicZ041) {
				OccursList occursListB = new OccursList();
				// 041 layout回傳
				occursListB.putParam("OOChainTxCd", "L8302");
				occursListB.putParam("OOHistoryTxCd", "L8032");
				occursListB.putParam("OOUkey", xJcicZ041.getUkey());
				occursListB.putParam("OOCustId", xJcicZ041.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ041.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ041.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ041.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ041.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ041.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ041 rJcicZ041 = new JcicZ041();
				JcicZ041Id rJcicZ041Id = new JcicZ041Id();
				rJcicZ041Id.setRcDate(xJcicZ041.getRcDate());
				rJcicZ041Id.setCustId(xJcicZ041.getCustId());
				rJcicZ041Id.setSubmitKey(xJcicZ041.getSubmitKey());
				rJcicZ041 = iJcicZ041Service.findById(rJcicZ041Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ041.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ041Log> iJcicZ041Log = null;
				iJcicZ041Log = iJcicZ041LogService.ukeyEq(rJcicZ041.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ041Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ042(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8303");
		occursListA.putParam("OOHistoryTxCd", "L8033");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ042> sJcicZ042 = null;
		sJcicZ042 = iJcicZ042Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ042 != null) {
			for (JcicZ042 xJcicZ042 : sJcicZ042) {
				OccursList occursListB = new OccursList();
				// 042 layout回傳
				occursListB.putParam("OOChainTxCd", "L8303");
				occursListB.putParam("OOHistoryTxCd", "L8033");
				occursListB.putParam("OOUkey", xJcicZ042.getUkey());
				occursListB.putParam("OOCustId", xJcicZ042.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ042.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ042.getSubmitKey(), titaVo));
				occursListB.putParam("OOMaxMainCode", xJcicZ042.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ042.getMaxMainCode(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ042.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ042.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ042.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ042 rJcicZ042 = new JcicZ042();
				JcicZ042Id rJcicZ042Id = new JcicZ042Id();
				rJcicZ042Id.setRcDate(xJcicZ042.getRcDate());
				rJcicZ042Id.setCustId(xJcicZ042.getCustId());
				rJcicZ042Id.setSubmitKey(xJcicZ042.getSubmitKey());
				rJcicZ042Id.setMaxMainCode(xJcicZ042.getMaxMainCode());
				rJcicZ042 = iJcicZ042Service.findById(rJcicZ042Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ042.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ042Log> iJcicZ042Log = null;
				iJcicZ042Log = iJcicZ042LogService.ukeyEq(rJcicZ042.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ042Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ043(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8304");
		occursListA.putParam("OOHistoryTxCd", "L8034");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ043> sJcicZ043 = null;
		sJcicZ043 = iJcicZ043Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ043 != null) {
			for (JcicZ043 xJcicZ043 : sJcicZ043) {
				OccursList occursListB = new OccursList();
				// 043 layout回傳
				occursListB.putParam("OOChainTxCd", "L8304");
				occursListB.putParam("OOHistoryTxCd", "L8034");
				occursListB.putParam("OOUkey", xJcicZ043.getUkey());
				occursListB.putParam("OOCustId", xJcicZ043.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ043.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ043.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ043.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ043.getTranKey());
				occursListB.putParam("OOMaxMainCode", xJcicZ043.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ043.getMaxMainCode(), titaVo));
				occursListB.putParam("OOAccount", xJcicZ043.getAccount());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ043.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ043 rJcicZ043 = new JcicZ043();
				JcicZ043Id rJcicZ043Id = new JcicZ043Id();
				rJcicZ043Id.setRcDate(xJcicZ043.getRcDate());
				rJcicZ043Id.setCustId(xJcicZ043.getCustId());
				rJcicZ043Id.setSubmitKey(xJcicZ043.getSubmitKey());
				rJcicZ043Id.setMaxMainCode(xJcicZ043.getMaxMainCode());
				rJcicZ043Id.setAccount(xJcicZ043.getAccount());
				rJcicZ043 = iJcicZ043Service.findById(rJcicZ043Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ043.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ043Log> iJcicZ043Log = null;
				iJcicZ043Log = iJcicZ043LogService.ukeyEq(rJcicZ043.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ043Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ044(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8305");
		occursListA.putParam("OOHistoryTxCd", "L8035");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ044> sJcicZ044 = null;
		sJcicZ044 = iJcicZ044Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ044 != null) {
			for (JcicZ044 xJcicZ044 : sJcicZ044) {
				OccursList occursListB = new OccursList();
				// 044 layout回傳
				occursListB.putParam("OOChainTxCd", "L8305");
				occursListB.putParam("OOHistoryTxCd", "L8035");
				occursListB.putParam("OOUkey", xJcicZ044.getUkey());
				occursListB.putParam("OOCustId", xJcicZ044.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ044.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ044.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ044.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ044.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ044.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ044 rJcicZ044 = new JcicZ044();
				JcicZ044Id rJcicZ044Id = new JcicZ044Id();
				rJcicZ044Id.setRcDate(xJcicZ044.getRcDate());
				rJcicZ044Id.setCustId(xJcicZ044.getCustId());
				rJcicZ044Id.setSubmitKey(xJcicZ044.getSubmitKey());
				rJcicZ044 = iJcicZ044Service.findById(rJcicZ044Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ044.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ044Log> iJcicZ044Log = null;
				iJcicZ044Log = iJcicZ044LogService.ukeyEq(rJcicZ044.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ044Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ045(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8306");
		occursListA.putParam("OOHistoryTxCd", "L8036");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOMaxMainCode", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ045> sJcicZ045 = null;
		sJcicZ045 = iJcicZ045Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ045 != null) {
			for (JcicZ045 xJcicZ045 : sJcicZ045) {
				OccursList occursListB = new OccursList();
				// 044 layout回傳
				occursListB.putParam("OOChainTxCd", "L8306");
				occursListB.putParam("OOHistoryTxCd", "L8036");
				occursListB.putParam("OOUkey", xJcicZ045.getUkey());
				occursListB.putParam("OOCustId", xJcicZ045.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ045.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ045.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ045.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ045.getTranKey());
				occursListB.putParam("OOMaxMainCode", xJcicZ045.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ045.getMaxMainCode(), titaVo));
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ045.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ045 rJcicZ045 = new JcicZ045();
				JcicZ045Id rJcicZ045Id = new JcicZ045Id();
				rJcicZ045Id.setRcDate(xJcicZ045.getRcDate());
				rJcicZ045Id.setCustId(xJcicZ045.getCustId());
				rJcicZ045Id.setSubmitKey(xJcicZ045.getSubmitKey());
				rJcicZ045Id.setMaxMainCode(xJcicZ045.getMaxMainCode());
				rJcicZ045 = iJcicZ045Service.findById(rJcicZ045Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ045.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ045Log> iJcicZ045Log = null;
				iJcicZ045Log = iJcicZ045LogService.ukeyEq(rJcicZ045.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ045Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ046(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8307");
		occursListA.putParam("OOHistoryTxCd", "L8037");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ046> sJcicZ046 = null;
		sJcicZ046 = iJcicZ046Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ046 != null) {
			for (JcicZ046 xJcicZ046 : sJcicZ046) {
				OccursList occursListB = new OccursList();
				// 046 layout回傳
				occursListB.putParam("OOChainTxCd", "L8307");
				occursListB.putParam("OOHistoryTxCd", "L8037");
				occursListB.putParam("OOUkey", xJcicZ046.getUkey());
				occursListB.putParam("OOCustId", xJcicZ046.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ046.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ046.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ046.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ046.getTranKey());
				occursListB.putParam("OOCloseDate", xJcicZ046.getCloseDate());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ046.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ046 rJcicZ046 = new JcicZ046();
				JcicZ046Id rJcicZ046Id = new JcicZ046Id();
				rJcicZ046Id.setRcDate(xJcicZ046.getRcDate());
				rJcicZ046Id.setCustId(xJcicZ046.getCustId());
				rJcicZ046Id.setSubmitKey(xJcicZ046.getSubmitKey());
				rJcicZ046Id.setCloseDate(xJcicZ046.getCloseDate());
				rJcicZ046 = iJcicZ046Service.findById(rJcicZ046Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ046.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ046Log> iJcicZ046Log = null;
				iJcicZ046Log = iJcicZ046LogService.ukeyEq(rJcicZ046.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ046Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ047(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8308");
		occursListA.putParam("OOHistoryTxCd", "L8038");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ047> sJcicZ047 = null;
		sJcicZ047 = iJcicZ047Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ047 != null) {
			for (JcicZ047 xJcicZ047 : sJcicZ047) {
				OccursList occursListB = new OccursList();
				occursListB.putParam("OOChainTxCd", "L8308");
				occursListB.putParam("OOHistoryTxCd", "L8038");
				occursListB.putParam("OOUkey", xJcicZ047.getUkey());
				occursListB.putParam("OOCustId", xJcicZ047.getCustId());
				occursListB.putParam("OORcDate", xJcicZ047.getRcDate());
				occursListB.putParam("OOSubmitKey", xJcicZ047.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ047.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ047.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ047.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ047 rJcicZ047 = new JcicZ047();
				JcicZ047Id rJcicZ047Id = new JcicZ047Id();
				rJcicZ047Id.setRcDate(xJcicZ047.getRcDate());
				rJcicZ047Id.setCustId(xJcicZ047.getCustId());
				rJcicZ047Id.setSubmitKey(xJcicZ047.getSubmitKey());
				rJcicZ047 = iJcicZ047Service.findById(rJcicZ047Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ047.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ047Log> iJcicZ047Log = null;
				iJcicZ047Log = iJcicZ047LogService.ukeyEq(xJcicZ047.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ047Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ048(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8309");
		occursListA.putParam("OOHistoryTxCd", "L8039");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ048> sJcicZ048 = null;
		sJcicZ048 = iJcicZ048Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ048 != null) {
			for (JcicZ048 xJcicZ048 : sJcicZ048) {
				OccursList occursListB = new OccursList();
				// 048 layout回傳
				occursListB.putParam("OOChainTxCd", "L8309");
				occursListB.putParam("OOHistoryTxCd", "L8039");
				occursListB.putParam("OOUkey", xJcicZ048.getUkey());
				occursListB.putParam("OOCustId", xJcicZ048.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ048.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ048.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ048.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ048.getTranKey());

				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ048.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ048 rJcicZ048 = new JcicZ048();
				JcicZ048Id rJcicZ048Id = new JcicZ048Id();
				rJcicZ048Id.setRcDate(xJcicZ048.getRcDate());
				rJcicZ048Id.setCustId(xJcicZ048.getCustId());
				rJcicZ048Id.setSubmitKey(xJcicZ048.getSubmitKey());
				rJcicZ048 = iJcicZ048Service.findById(rJcicZ048Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ048.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ048Log> iJcicZ048Log = null;
				iJcicZ048Log = iJcicZ048LogService.ukeyEq(rJcicZ048.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ048Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ049(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8310");
		occursListA.putParam("OOHistoryTxCd", "L8040");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ049> sJcicZ049 = null;
		sJcicZ049 = iJcicZ049Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ049 != null) {
			for (JcicZ049 xJcicZ049 : sJcicZ049) {
				OccursList occursListB = new OccursList();
				occursListB.putParam("OOChainTxCd", "L8310");
				occursListB.putParam("OOHistoryTxCd", "L8040");
				occursListB.putParam("OOUkey", xJcicZ049.getUkey());
				occursListB.putParam("OOCustId", xJcicZ049.getCustId());
				occursListB.putParam("OORcDate", xJcicZ049.getRcDate());
				occursListB.putParam("OOSubmitKey", xJcicZ049.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ049.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ049.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ049.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ049 rJcicZ049 = new JcicZ049();
				JcicZ049Id rJcicZ049Id = new JcicZ049Id();
				rJcicZ049Id.setRcDate(xJcicZ049.getRcDate());
				rJcicZ049Id.setCustId(xJcicZ049.getCustId());
				rJcicZ049Id.setSubmitKey(xJcicZ049.getSubmitKey());
				rJcicZ049 = iJcicZ049Service.findById(rJcicZ049Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ049.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ049Log> iJcicZ049Log = null;
				iJcicZ049Log = iJcicZ049LogService.ukeyEq(xJcicZ049.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ049Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ050(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8311");
		occursListA.putParam("OOHistoryTxCd", "L8041");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ050> sJcicZ050 = null;
		sJcicZ050 = iJcicZ050Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ050 != null) {
			for (JcicZ050 xJcicZ050 : sJcicZ050) {
				OccursList occursListB = new OccursList();
				// 050 layout回傳
				occursListB.putParam("OOChainTxCd", "L8311");
				occursListB.putParam("OOHistoryTxCd", "L8041");
				occursListB.putParam("OOUkey", xJcicZ050.getUkey());
				occursListB.putParam("OOCustId", xJcicZ050.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ050.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ050.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ050.getRcDate());
				occursListB.putParam("OOTranKey", xJcicZ050.getTranKey());
				occursListB.putParam("OOPayDate", xJcicZ050.getPayDate());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ050.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ050 rJcicZ050 = new JcicZ050();
				JcicZ050Id rJcicZ050Id = new JcicZ050Id();
				rJcicZ050Id.setRcDate(xJcicZ050.getRcDate());
				rJcicZ050Id.setCustId(xJcicZ050.getCustId());
				rJcicZ050Id.setSubmitKey(xJcicZ050.getSubmitKey());
				rJcicZ050Id.setPayDate(xJcicZ050.getPayDate());
				rJcicZ050 = iJcicZ050Service.findById(rJcicZ050Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ050.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ050Log> iJcicZ050Log = null;
				iJcicZ050Log = iJcicZ050LogService.ukeyEq(xJcicZ050.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ050Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ051(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8312");
		occursListA.putParam("OOHistoryTxCd", "L8042");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ051> sJcicZ051 = null;
		sJcicZ051 = iJcicZ051Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ051 != null) {
			for (JcicZ051 xJcicZ051 : sJcicZ051) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8312");
				occursListB.putParam("OOHistoryTxCd", "L8042");
				occursListB.putParam("OOUkey", xJcicZ051.getUkey());
				occursListB.putParam("OOCustId", xJcicZ051.getCustId());
				occursListB.putParam("OORcDate", xJcicZ051.getRcDate());
				occursListB.putParam("OOSubmitKey", xJcicZ051.getSubmitKey());
				occursListB.putParam("OODelayYM", xJcicZ051.getDelayYM());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ051.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ051.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ051.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ051 rJcicZ051 = new JcicZ051();
				JcicZ051Id rJcicZ051Id = new JcicZ051Id();
				rJcicZ051Id.setRcDate(xJcicZ051.getRcDate());
				rJcicZ051Id.setCustId(xJcicZ051.getCustId());
				rJcicZ051Id.setDelayYM(xJcicZ051.getDelayYM());
				rJcicZ051Id.setSubmitKey(xJcicZ051.getSubmitKey());
				rJcicZ051 = iJcicZ051Service.findById(rJcicZ051Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ051.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ051Log> iJcicZ051Log = null;
				iJcicZ051Log = iJcicZ051LogService.ukeyEq(xJcicZ051.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ051Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ052(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8313");
		occursListA.putParam("OOHistoryTxCd", "L8043");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ052> sJcicZ052 = null;
		sJcicZ052 = iJcicZ052Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ052 != null) {
			for (JcicZ052 xJcicZ052 : sJcicZ052) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8313");
				occursListB.putParam("OOHistoryTxCd", "L8043");
				occursListB.putParam("OOUkey", xJcicZ052.getUkey());
				occursListB.putParam("OOCustId", xJcicZ052.getCustId());
				occursListB.putParam("OORcDate", xJcicZ052.getRcDate());
				occursListB.putParam("OOSubmitKey", xJcicZ052.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ052.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ052.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ052.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ052 rJcicZ052 = new JcicZ052();
				JcicZ052Id rJcicZ052Id = new JcicZ052Id();
				rJcicZ052Id.setRcDate(xJcicZ052.getRcDate());
				rJcicZ052Id.setCustId(xJcicZ052.getCustId());
				rJcicZ052Id.setSubmitKey(xJcicZ052.getSubmitKey());
				rJcicZ052 = iJcicZ052Service.findById(rJcicZ052Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ052.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ052Log> iJcicZ052Log = null;
				iJcicZ052Log = iJcicZ052LogService.ukeyEq(xJcicZ052.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ052Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ053(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8314");
		occursListA.putParam("OOHistoryTxCd", "L8044");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ053> sJcicZ053 = null;
		sJcicZ053 = iJcicZ053Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ053 != null) {
			for (JcicZ053 xJcicZ053 : sJcicZ053) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8314");
				occursListB.putParam("OOHistoryTxCd", "L8044");
				occursListB.putParam("OOUkey", xJcicZ053.getUkey());
				occursListB.putParam("OOCustId", xJcicZ053.getCustId());
				occursListB.putParam("OORcDate", xJcicZ053.getRcDate());
				occursListB.putParam("OOMaxMainCode", xJcicZ053.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ053.getMaxMainCode(), titaVo));
				occursListB.putParam("OOSubmitKey", xJcicZ053.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ053.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ053.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ053.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ053 rJcicZ053 = new JcicZ053();
				JcicZ053Id rJcicZ053Id = new JcicZ053Id();
				rJcicZ053Id.setMaxMainCode(xJcicZ053.getMaxMainCode());
				rJcicZ053Id.setRcDate(xJcicZ053.getRcDate());
				rJcicZ053Id.setCustId(xJcicZ053.getCustId());
				rJcicZ053Id.setSubmitKey(xJcicZ053.getSubmitKey());
				rJcicZ053 = iJcicZ053Service.findById(rJcicZ053Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ053.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ053Log> iJcicZ053Log = null;
				iJcicZ053Log = iJcicZ053LogService.ukeyEq(xJcicZ053.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ053Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ054(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8315");
		occursListA.putParam("OOHistoryTxCd", "L8045");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOPayOffDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ054> sJcicZ054 = null;
		sJcicZ054 = iJcicZ054Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ054 != null) {
			for (JcicZ054 xJcicZ054 : sJcicZ054) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8315");
				occursListB.putParam("OOHistoryTxCd", "L8045");
				occursListB.putParam("OOUkey", xJcicZ054.getUkey());
				occursListB.putParam("OOCustId", xJcicZ054.getCustId());
				occursListB.putParam("OORcDate", xJcicZ054.getRcDate());
				occursListB.putParam("OOMaxMainCode", xJcicZ054.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ054.getMaxMainCode(), titaVo));
				occursListB.putParam("OOSubmitKey", xJcicZ054.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ054.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ054.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ054.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ054 rJcicZ054 = new JcicZ054();
				JcicZ054Id rJcicZ054Id = new JcicZ054Id();
				rJcicZ054Id.setMaxMainCode(xJcicZ054.getMaxMainCode());
				rJcicZ054Id.setRcDate(xJcicZ054.getRcDate());
				rJcicZ054Id.setCustId(xJcicZ054.getCustId());
				rJcicZ054Id.setSubmitKey(xJcicZ054.getSubmitKey());
				rJcicZ054Id.setPayOffDate(xJcicZ054.getPayOffDate());
				rJcicZ054 = iJcicZ054Service.findById(rJcicZ054Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ054.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ054Log> iJcicZ054Log = null;
				iJcicZ054Log = iJcicZ054LogService.ukeyEq(xJcicZ054.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ054Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ055(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8316");
		occursListA.putParam("OOHistoryTxCd", "L8046");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ055> sJcicZ055 = null;
		sJcicZ055 = iJcicZ055Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ055 != null) {
			for (JcicZ055 xJcicZ055 : sJcicZ055) {
				OccursList occursListB = new OccursList();
				// 055 layout回傳
				occursListB.putParam("OOChainTxCd", "L8316");
				occursListB.putParam("OOHistoryTxCd", "L8046");
				occursListB.putParam("OOUkey", xJcicZ055.getUkey());
				occursListB.putParam("OOCustId", xJcicZ055.getCustId());
				occursListB.putParam("OOCourtCode", xJcicZ055.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ055.getCourtCode(), titaVo));
				occursListB.putParam("OOClaimDate", xJcicZ055.getClaimDate());
				occursListB.putParam("OOCaseStatus", xJcicZ055.getCaseStatus());
				occursListB.putParam("OOSubmitKey", xJcicZ055.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ055.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ055.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ055.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ055 rJcicZ055 = new JcicZ055();
				JcicZ055Id rJcicZ055Id = new JcicZ055Id();
				rJcicZ055Id.setCourtCode(xJcicZ055.getCourtCode());
				rJcicZ055Id.setClaimDate(xJcicZ055.getClaimDate());
				rJcicZ055Id.setCaseStatus(xJcicZ055.getCaseStatus());
				rJcicZ055Id.setCustId(xJcicZ055.getCustId());
				rJcicZ055Id.setSubmitKey(xJcicZ055.getSubmitKey());
				rJcicZ055 = iJcicZ055Service.findById(rJcicZ055Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ055.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ055Log> iJcicZ055Log = null;
				iJcicZ055Log = iJcicZ055LogService.ukeyEq(xJcicZ055.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ055Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OORcDate", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ056(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8317");
		occursListA.putParam("OOHistoryTxCd", "L8047");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ056> sJcicZ056 = null;
		sJcicZ056 = iJcicZ056Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ056 != null) {
			for (JcicZ056 xJcicZ056 : sJcicZ056) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8317");
				occursListB.putParam("OOHistoryTxCd", "L8047");
				occursListB.putParam("OOUkey", xJcicZ056.getUkey());
				occursListB.putParam("OOCustId", xJcicZ056.getCustId());
				occursListB.putParam("OOCourtCode", xJcicZ056.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ056.getCourtCode(), titaVo));
				occursListB.putParam("OOClaimDate", xJcicZ056.getClaimDate());
				occursListB.putParam("OOCaseStatus", xJcicZ056.getCaseStatus());
				occursListB.putParam("OOSubmitKey", xJcicZ056.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ056.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ056.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ056.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ056 rJcicZ056 = new JcicZ056();
				JcicZ056Id rJcicZ056Id = new JcicZ056Id();
				rJcicZ056Id.setCourtCode(xJcicZ056.getCourtCode());
				rJcicZ056Id.setClaimDate(xJcicZ056.getClaimDate());
				rJcicZ056Id.setCaseStatus(xJcicZ056.getCaseStatus());
				rJcicZ056Id.setCustId(xJcicZ056.getCustId());
				rJcicZ056Id.setSubmitKey(xJcicZ056.getSubmitKey());
				rJcicZ056 = iJcicZ056Service.findById(rJcicZ056Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ056.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ056Log> iJcicZ056Log = null;
				iJcicZ056Log = iJcicZ056LogService.ukeyEq(xJcicZ056.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ056Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OORcDate", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ060(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8318");
		occursListA.putParam("OOHistoryTxCd", "L8048");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ060> sJcicZ060 = null;
		sJcicZ060 = iJcicZ060Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ060 != null) {
			for (JcicZ060 xJcicZ060 : sJcicZ060) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8318");
				occursListB.putParam("OOHistoryTxCd", "L8048");
				occursListB.putParam("OOUkey", xJcicZ060.getUkey());
				occursListB.putParam("OOCustId", xJcicZ060.getCustId());
				occursListB.putParam("OORcDate", xJcicZ060.getRcDate());
				occursListB.putParam("OOChangePayDate", xJcicZ060.getChangePayDate());
				occursListB.putParam("OOSubmitKey", xJcicZ060.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ060.getSubmitKey(), titaVo));

				occursListB.putParam("OOTranKey", xJcicZ060.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ060.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ060 rJcicZ060 = new JcicZ060();
				JcicZ060Id rJcicZ060Id = new JcicZ060Id();
				rJcicZ060Id.setCustId(xJcicZ060.getCustId());
				rJcicZ060Id.setRcDate(xJcicZ060.getRcDate());
				rJcicZ060Id.setChangePayDate(xJcicZ060.getChangePayDate());
				rJcicZ060Id.setSubmitKey(xJcicZ060.getSubmitKey());
				rJcicZ060 = iJcicZ060Service.findById(rJcicZ060Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ060.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ060Log> iJcicZ060Log = null;
				iJcicZ060Log = iJcicZ060LogService.ukeyEq(xJcicZ060.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ060Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ061(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8319");
		occursListA.putParam("OOHistoryTxCd", "L8049");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ061> sJcicZ061 = null;
		sJcicZ061 = iJcicZ061Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ061 != null) {
			for (JcicZ061 xJcicZ061 : sJcicZ061) {
				this.info("test===" + xJcicZ061.getCreateDate());
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8319");
				occursListB.putParam("OOHistoryTxCd", "L8049");
				occursListB.putParam("OOUkey", xJcicZ061.getUkey());
				occursListB.putParam("OOCustId", xJcicZ061.getCustId());
				occursListB.putParam("OORcDate", xJcicZ061.getRcDate());
				occursListB.putParam("OOChangePayDate", xJcicZ061.getChangePayDate());
				occursListB.putParam("OOSubmitKey", xJcicZ061.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ061.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ061.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ061.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ061 rJcicZ061 = new JcicZ061();
				JcicZ061Id rJcicZ061Id = new JcicZ061Id();
				rJcicZ061Id.setCustId(xJcicZ061.getCustId());
				rJcicZ061Id.setRcDate(xJcicZ061.getRcDate());
				rJcicZ061Id.setSubmitKey(xJcicZ061.getSubmitKey());
				rJcicZ061Id.setChangePayDate(xJcicZ061.getChangePayDate());
				rJcicZ061Id.setMaxMainCode(xJcicZ061.getMaxMainCode());
				rJcicZ061 = iJcicZ061Service.findById(rJcicZ061Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ061.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ061Log> iJcicZ061Log = null;
				iJcicZ061Log = iJcicZ061LogService.ukeyEq(xJcicZ061.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ061Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ062(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8320");
		occursListA.putParam("OOHistoryTxCd", "L8050");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ062> sJcicZ062 = null;
		sJcicZ062 = iJcicZ062Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ062 != null) {
			for (JcicZ062 xJcicZ062 : sJcicZ062) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8320");
				occursListB.putParam("OOHistoryTxCd", "L8050");
				occursListB.putParam("OOUkey", xJcicZ062.getUkey());
				occursListB.putParam("OOCustId", xJcicZ062.getCustId());
				occursListB.putParam("OORcDate", xJcicZ062.getRcDate());
				occursListB.putParam("OOChangePayDate", xJcicZ062.getChangePayDate());
				occursListB.putParam("OOSubmitKey", xJcicZ062.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ062.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ062.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ062.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ062 rJcicZ062 = new JcicZ062();
				JcicZ062Id rJcicZ062Id = new JcicZ062Id();
				rJcicZ062Id.setCustId(xJcicZ062.getCustId());
				rJcicZ062Id.setRcDate(xJcicZ062.getRcDate());
				rJcicZ062Id.setChangePayDate(xJcicZ062.getChangePayDate());
				rJcicZ062Id.setSubmitKey(xJcicZ062.getSubmitKey());
				rJcicZ062 = iJcicZ062Service.findById(rJcicZ062Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ062.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ062Log> iJcicZ062Log = null;
				iJcicZ062Log = iJcicZ062LogService.ukeyEq(xJcicZ062.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ062Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ063(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8321");
		occursListA.putParam("OOHistoryTxCd", "L8051");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		occursListA.putParam("OOCloseDate", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ063> sJcicZ063 = null;
		sJcicZ063 = iJcicZ063Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ063 != null) {
			for (JcicZ063 xJcicZ063 : sJcicZ063) {
				OccursList occursListB = new OccursList();
				// 063 layout回傳
				occursListB.putParam("OOChainTxCd", "L8321");
				occursListB.putParam("OOHistoryTxCd", "L8051");
				occursListB.putParam("OOUkey", xJcicZ063.getUkey());
				occursListB.putParam("OOCustId", xJcicZ063.getCustId());
				occursListB.putParam("OORcDate", xJcicZ063.getRcDate());
				occursListB.putParam("OOSubmitKey", xJcicZ063.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ063.getSubmitKey(), titaVo));
				occursListB.putParam("OOChangePayDate", xJcicZ063.getChangePayDate());
				occursListB.putParam("OOTranKey", xJcicZ063.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ063.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ063 rJcicZ063 = new JcicZ063();
				JcicZ063Id rJcicZ063Id = new JcicZ063Id();
				rJcicZ063Id.setRcDate(xJcicZ063.getRcDate());
				rJcicZ063Id.setCustId(xJcicZ063.getCustId());
				rJcicZ063Id.setSubmitKey(xJcicZ063.getSubmitKey());
				rJcicZ063Id.setChangePayDate(xJcicZ063.getChangePayDate());
				rJcicZ063 = iJcicZ063Service.findById(rJcicZ063Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ063.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ063Log> iJcicZ063Log = null;
				iJcicZ063Log = iJcicZ063LogService.ukeyEq(xJcicZ063.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ063Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				occursListB.putParam("OOCloseDate", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ440(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8322");
		occursListA.putParam("OOHistoryTxCd", "L8052");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ440> sJcicZ440 = null;
		sJcicZ440 = iJcicZ440Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ440 != null) {
			for (JcicZ440 xJcicZ440 : sJcicZ440) {
				OccursList occursListB = new OccursList();
				// 440 layout回傳
				occursListB.putParam("OOChainTxCd", "L8322");
				occursListB.putParam("OOHistoryTxCd", "L8052");
				occursListB.putParam("OOUkey", xJcicZ440.getUkey());
				occursListB.putParam("OOCustId", xJcicZ440.getCustId());
				occursListB.putParam("OORcDate", xJcicZ440.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ440.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ440.getSubmitKey(), titaVo));
				occursListB.putParam("OOCourtCode", xJcicZ440.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ440.getCourtCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ440.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ440.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ440 rJcicZ440 = new JcicZ440();
				JcicZ440Id rJcicZ440Id = new JcicZ440Id();
				rJcicZ440Id.setApplyDate(xJcicZ440.getApplyDate());
				rJcicZ440Id.setCustId(xJcicZ440.getCustId());
				rJcicZ440Id.setSubmitKey(xJcicZ440.getSubmitKey());
				rJcicZ440Id.setCourtCode(xJcicZ440.getCourtCode());
				rJcicZ440 = iJcicZ440Service.findById(rJcicZ440Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ440.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ440Log> iJcicZ440Log = null;
				iJcicZ440Log = iJcicZ440LogService.ukeyEq(xJcicZ440.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ440Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ442(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8323");
		occursListA.putParam("OOHistoryTxCd", "L8053");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOMaxMainCode", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ442> sJcicZ442 = null;
		sJcicZ442 = iJcicZ442Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ442 != null) {
			for (JcicZ442 xJcicZ442 : sJcicZ442) {
				OccursList occursListB = new OccursList();
				// 442 layout回傳
				occursListB.putParam("OOChainTxCd", "L8323");
				occursListB.putParam("OOHistoryTxCd", "L8053");
				occursListB.putParam("OOUkey", xJcicZ442.getUkey());
				occursListB.putParam("OOCustId", xJcicZ442.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ442.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ442.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ442.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ442.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ442.getCourtCode(), titaVo));
				occursListB.putParam("OOMaxMainCode", xJcicZ442.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ442.getMaxMainCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ442.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ442.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ442 rJcicZ442 = new JcicZ442();
				JcicZ442Id rJcicZ442Id = new JcicZ442Id();
				rJcicZ442Id.setCustId(xJcicZ442.getCustId());
				rJcicZ442Id.setSubmitKey(xJcicZ442.getSubmitKey());
				rJcicZ442Id.setApplyDate(xJcicZ442.getApplyDate());
				rJcicZ442Id.setCourtCode(xJcicZ442.getCourtCode());
				rJcicZ442Id.setMaxMainCode(xJcicZ442.getMaxMainCode());
				rJcicZ442 = iJcicZ442Service.findById(rJcicZ442Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ442.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ442Log> iJcicZ442Log = null;
				iJcicZ442Log = iJcicZ442LogService.ukeyEq(rJcicZ442.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ442Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ443(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8324");
		occursListA.putParam("OOHistoryTxCd", "L8054");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ443> sJcicZ443 = null;
		sJcicZ443 = iJcicZ443Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ443 != null) {
			for (JcicZ443 xJcicZ443 : sJcicZ443) {
				OccursList occursListB = new OccursList();
				// 443 layout回傳
				occursListB.putParam("OOChainTxCd", "L8324");
				occursListB.putParam("OOHistoryTxCd", "L8054");
				occursListB.putParam("OOUkey", xJcicZ443.getUkey());
				occursListB.putParam("OOCustId", xJcicZ443.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ443.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ443.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ443.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ443.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ443.getCourtCode(), titaVo));
				occursListB.putParam("OOMaxMainCode", xJcicZ443.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ443.getMaxMainCode(), titaVo));
				occursListB.putParam("OOAccount", xJcicZ443.getAccount());
				occursListB.putParam("OOTranKey", xJcicZ443.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ443.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ443 rJcicZ443 = new JcicZ443();
				JcicZ443Id rJcicZ443Id = new JcicZ443Id();
				rJcicZ443Id.setCustId(xJcicZ443.getCustId());
				rJcicZ443Id.setSubmitKey(xJcicZ443.getSubmitKey());
				rJcicZ443Id.setApplyDate(xJcicZ443.getApplyDate());
				rJcicZ443Id.setCourtCode(xJcicZ443.getCourtCode());
				rJcicZ443Id.setMaxMainCode(xJcicZ443.getMaxMainCode());
				rJcicZ443Id.setAccount(xJcicZ443.getAccount());
				rJcicZ443 = iJcicZ443Service.findById(rJcicZ443Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ443.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ443Log> iJcicZ443Log = null;
				iJcicZ443Log = iJcicZ443LogService.ukeyEq(rJcicZ443.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ443Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ444(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8325");
		occursListA.putParam("OOHistoryTxCd", "L8055");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCourtCode", "");
		// 固定回傳
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ444> sJcicZ444 = null;
		sJcicZ444 = iJcicZ444Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ444 != null) {
			for (JcicZ444 xJcicZ444 : sJcicZ444) {
				OccursList occursListB = new OccursList();
				// 444 layout回傳
				occursListB.putParam("OOChainTxCd", "L8325");
				occursListB.putParam("OOHistoryTxCd", "L8055");
				occursListB.putParam("OOUkey", xJcicZ444.getUkey());
				occursListB.putParam("OOCustId", xJcicZ444.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ444.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ444.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ444.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ444.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ444.getCourtCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ444.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ444.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ444 rJcicZ444 = new JcicZ444();
				JcicZ444Id rJcicZ444Id = new JcicZ444Id();
				rJcicZ444Id.setCustId(xJcicZ444.getCustId());
				rJcicZ444Id.setSubmitKey(xJcicZ444.getSubmitKey());
				rJcicZ444Id.setApplyDate(xJcicZ444.getApplyDate());
				rJcicZ444Id.setCourtCode(xJcicZ444.getCourtCode());
				rJcicZ444 = iJcicZ444Service.findById(rJcicZ444Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ444.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ444Log> iJcicZ444Log = null;
				iJcicZ444Log = iJcicZ444LogService.ukeyEq(rJcicZ444.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ444Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ446(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8326");
		occursListA.putParam("OOHistoryTxCd", "L8056");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		// 固定回傳
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ446> sJcicZ446 = null;
		sJcicZ446 = iJcicZ446Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ446 != null) {
			for (JcicZ446 xJcicZ446 : sJcicZ446) {
				OccursList occursListB = new OccursList();
				// 446 layout回傳
				occursListB.putParam("OOChainTxCd", "L8326");
				occursListB.putParam("OOHistoryTxCd", "L8056");
				occursListB.putParam("OOUkey", xJcicZ446.getUkey());
				occursListB.putParam("OOCustId", xJcicZ446.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ446.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ446.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ446.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ446.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ446.getCourtCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ446.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ446.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ446 rJcicZ446 = new JcicZ446();
				JcicZ446Id rJcicZ446Id = new JcicZ446Id();
				rJcicZ446Id.setCustId(xJcicZ446.getCustId());
				rJcicZ446Id.setSubmitKey(xJcicZ446.getSubmitKey());
				rJcicZ446Id.setApplyDate(xJcicZ446.getApplyDate());
				rJcicZ446Id.setCourtCode(xJcicZ446.getCourtCode());
				rJcicZ446 = iJcicZ446Service.findById(rJcicZ446Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ446.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ446Log> iJcicZ446Log = null;
				iJcicZ446Log = iJcicZ446LogService.ukeyEq(rJcicZ446.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ446Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ447(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8327");
		occursListA.putParam("OOHistoryTxCd", "L8057");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCourtCode", "");
		// 固定回傳
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ447> sJcicZ447 = null;
		sJcicZ447 = iJcicZ447Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ447 != null) {
			for (JcicZ447 xJcicZ447 : sJcicZ447) {
				OccursList occursListB = new OccursList();
				// 447 layout回傳
				occursListB.putParam("OOChainTxCd", "L8327");
				occursListB.putParam("OOHistoryTxCd", "L8057");
				occursListB.putParam("OOUkey", xJcicZ447.getUkey());
				occursListB.putParam("OOCustId", xJcicZ447.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ447.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ447.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ447.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ447.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ447.getCourtCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ447.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ447.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ447 rJcicZ447 = new JcicZ447();
				JcicZ447Id rJcicZ447Id = new JcicZ447Id();
				rJcicZ447Id.setCustId(xJcicZ447.getCustId());
				rJcicZ447Id.setSubmitKey(xJcicZ447.getSubmitKey());
				rJcicZ447Id.setApplyDate(xJcicZ447.getApplyDate());
				rJcicZ447Id.setCourtCode(xJcicZ447.getCourtCode());
				rJcicZ447 = iJcicZ447Service.findById(rJcicZ447Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ447.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ447Log> iJcicZ447Log = null;
				iJcicZ447Log = iJcicZ447LogService.ukeyEq(rJcicZ447.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ447Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ448(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8328");
		occursListA.putParam("OOHistoryTxCd", "L8058");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOMaxMainCode", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ448> sJcicZ448 = null;
		sJcicZ448 = iJcicZ448Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ448 != null) {
			for (JcicZ448 xJcicZ448 : sJcicZ448) {
				OccursList occursListB = new OccursList();
				// 448 layout回傳
				occursListB.putParam("OOChainTxCd", "L8328");
				occursListB.putParam("OOHistoryTxCd", "L8058");
				occursListB.putParam("OOUkey", xJcicZ448.getUkey());
				occursListB.putParam("OOCustId", xJcicZ448.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ448.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ448.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ448.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ448.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ448.getCourtCode(), titaVo));
				occursListB.putParam("OOMaxMainCode", xJcicZ448.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ448.getMaxMainCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ448.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ448.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ448 rJcicZ448 = new JcicZ448();
				JcicZ448Id rJcicZ448Id = new JcicZ448Id();
				rJcicZ448Id.setCustId(xJcicZ448.getCustId());
				rJcicZ448Id.setSubmitKey(xJcicZ448.getSubmitKey());
				rJcicZ448Id.setApplyDate(xJcicZ448.getApplyDate());
				rJcicZ448Id.setCourtCode(xJcicZ448.getCourtCode());
				rJcicZ448Id.setMaxMainCode(xJcicZ448.getMaxMainCode());
				rJcicZ448 = iJcicZ448Service.findById(rJcicZ448Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ448.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ448Log> iJcicZ448Log = null;
				iJcicZ448Log = iJcicZ448LogService.ukeyEq(rJcicZ448.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ448Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOPayDate", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ450(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8329");
		occursListA.putParam("OOHistoryTxCd", "L8059");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ450> sJcicZ450 = null;
		sJcicZ450 = iJcicZ450Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ450 != null) {
			for (JcicZ450 xJcicZ450 : sJcicZ450) {
				OccursList occursListB = new OccursList();
				// 450 layout回傳
				occursListB.putParam("OOChainTxCd", "L8329");
				occursListB.putParam("OOHistoryTxCd", "L8059");
				occursListB.putParam("OOUkey", xJcicZ450.getUkey());
				occursListB.putParam("OOCustId", xJcicZ450.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ450.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ450.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ450.getApplyDate());
				occursListB.putParam("OOPayDate", xJcicZ450.getPayDate());
				occursListB.putParam("OOCourtCode", xJcicZ450.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ450.getCourtCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ450.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ450.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ450 rJcicZ450 = new JcicZ450();
				JcicZ450Id rJcicZ450Id = new JcicZ450Id();
				rJcicZ450Id.setCustId(xJcicZ450.getCustId());
				rJcicZ450Id.setSubmitKey(xJcicZ450.getSubmitKey());
				rJcicZ450Id.setApplyDate(xJcicZ450.getApplyDate());
				rJcicZ450Id.setCourtCode(xJcicZ450.getCourtCode());
				rJcicZ450Id.setPayDate(xJcicZ450.getPayDate());
				rJcicZ450 = iJcicZ450Service.findById(rJcicZ450Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ450.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ450Log> iJcicZ450Log = null;
				iJcicZ450Log = iJcicZ450LogService.ukeyEq(rJcicZ450.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ450Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ451(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8330");
		occursListA.putParam("OOHistoryTxCd", "L8060");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOPayDate", "");
		// 固定回傳
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ451> sJcicZ451 = null;
		sJcicZ451 = iJcicZ451Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ451 != null) {
			for (JcicZ451 xJcicZ451 : sJcicZ451) {
				OccursList occursListB = new OccursList();
				// 451 layout回傳
				occursListB.putParam("OOChainTxCd", "L8330");
				occursListB.putParam("OOHistoryTxCd", "L8060");
				occursListB.putParam("OOUkey", xJcicZ451.getUkey());
				occursListB.putParam("OOCustId", xJcicZ451.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ451.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ451.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ451.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ451.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ451.getCourtCode(), titaVo));
				occursListB.putParam("OODelayYM", xJcicZ451.getDelayYM());
				occursListB.putParam("OOTranKey", xJcicZ451.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ451.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ451 rJcicZ451 = new JcicZ451();
				JcicZ451Id rJcicZ451Id = new JcicZ451Id();
				rJcicZ451Id.setCustId(xJcicZ451.getCustId());
				rJcicZ451Id.setSubmitKey(xJcicZ451.getSubmitKey());
				rJcicZ451Id.setApplyDate(xJcicZ451.getApplyDate());
				rJcicZ451Id.setCourtCode(xJcicZ451.getCourtCode());
				rJcicZ451Id.setDelayYM(xJcicZ451.getDelayYM());
				rJcicZ451 = iJcicZ451Service.findById(rJcicZ451Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ451.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ451Log> iJcicZ451Log = null;
				iJcicZ451Log = iJcicZ451LogService.ukeyEq(rJcicZ451.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ451Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳

				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ454(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8331");
		occursListA.putParam("OOHistoryTxCd", "L8061");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOTranKey", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ454> sJcicZ454 = null;
		sJcicZ454 = iJcicZ454Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ454 != null) {
			for (JcicZ454 xJcicZ454 : sJcicZ454) {
				OccursList occursListB = new OccursList();
				// 454 layout回傳
				occursListB.putParam("OOChainTxCd", "L8331");
				occursListB.putParam("OOHistoryTxCd", "L8061");
				occursListB.putParam("OOUkey", xJcicZ454.getUkey());
				occursListB.putParam("OOCustId", xJcicZ454.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ454.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ454.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ454.getApplyDate());
				occursListB.putParam("OOCourtCode", xJcicZ454.getCourtCode());
				occursListB.putParam("OOCourtCodeX", dealCourtName(xJcicZ454.getCourtCode(), titaVo));
				occursListB.putParam("OOMaxMainCode", xJcicZ454.getMaxMainCode());
				occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ454.getMaxMainCode(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ454.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ454.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ454 rJcicZ454 = new JcicZ454();
				JcicZ454Id rJcicZ454Id = new JcicZ454Id();
				rJcicZ454Id.setCustId(xJcicZ454.getCustId());
				rJcicZ454Id.setSubmitKey(xJcicZ454.getSubmitKey());
				rJcicZ454Id.setApplyDate(xJcicZ454.getApplyDate());
				rJcicZ454Id.setCourtCode(xJcicZ454.getCourtCode());
				rJcicZ454Id.setMaxMainCode(xJcicZ454.getMaxMainCode());
				rJcicZ454 = iJcicZ454Service.findById(rJcicZ454Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ454.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ454Log> iJcicZ454Log = null;
				iJcicZ454Log = iJcicZ454LogService.ukeyEq(rJcicZ454.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ454Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCaseStatus", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ570(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8332");
		occursListA.putParam("OOHistoryTxCd", "L8062");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");

		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ570> sJcicZ570 = null;
		sJcicZ570 = iJcicZ570Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ570 != null) {
			for (JcicZ570 xJcicZ570 : sJcicZ570) {
				OccursList occursListB = new OccursList();
				// 570 layout回傳
				occursListB.putParam("OOChainTxCd", "L8332");
				occursListB.putParam("OOHistoryTxCd", "L8062");
				occursListB.putParam("OOUkey", xJcicZ570.getUkey());
				occursListB.putParam("OOCustId", xJcicZ570.getCustId());
				occursListB.putParam("OOSubmitKey", xJcicZ570.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ570.getSubmitKey(), titaVo));
				occursListB.putParam("OORcDate", xJcicZ570.getApplyDate());
				occursListB.putParam("OOTranKey", xJcicZ570.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ570.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ570 rJcicZ570 = new JcicZ570();
				JcicZ570Id rJcicZ570Id = new JcicZ570Id();
				rJcicZ570Id.setApplyDate(xJcicZ570.getApplyDate());
				rJcicZ570Id.setCustId(xJcicZ570.getCustId());
				rJcicZ570Id.setSubmitKey(xJcicZ570.getSubmitKey());
				rJcicZ570 = iJcicZ570Service.findById(rJcicZ570Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ570.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ570Log> iJcicZ570Log = null;
				iJcicZ570Log = iJcicZ570LogService.ukeyEq(xJcicZ570.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ570Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ571(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8333");
		occursListA.putParam("OOHistoryTxCd", "L8063");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ571> sJcicZ571 = null;
		sJcicZ571 = iJcicZ571Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ571 != null) {
			for (JcicZ571 xJcicZ571 : sJcicZ571) {
				OccursList occursListB = new OccursList();
				// 571 layout回傳
				occursListB.putParam("OOChainTxCd", "L8333");
				occursListB.putParam("OOHistoryTxCd", "L8063");
				occursListB.putParam("OOUkey", xJcicZ571.getUkey());
				occursListB.putParam("OOCustId", xJcicZ571.getCustId());
				occursListB.putParam("OORcDate", xJcicZ571.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ571.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ571.getSubmitKey(), titaVo));
				occursListB.putParam("OOBankId", xJcicZ571.getBankId());
				occursListB.putParam("OOBankIdX", dealBankName(xJcicZ571.getBankId(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ571.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ571.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ571 rJcicZ571 = new JcicZ571();
				JcicZ571Id rJcicZ571Id = new JcicZ571Id();
				rJcicZ571Id.setApplyDate(xJcicZ571.getApplyDate());
				rJcicZ571Id.setCustId(xJcicZ571.getCustId());
				rJcicZ571Id.setBankId(xJcicZ571.getBankId());
				rJcicZ571Id.setSubmitKey(xJcicZ571.getSubmitKey());
				rJcicZ571 = iJcicZ571Service.findById(rJcicZ571Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ571.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ571Log> iJcicZ571Log = null;
				iJcicZ571Log = iJcicZ571LogService.ukeyEq(xJcicZ571.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ571Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ572(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8334");
		occursListA.putParam("OOHistoryTxCd", "L8064");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ572> sJcicZ572 = null;
		sJcicZ572 = iJcicZ572Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ572 != null) {
			for (JcicZ572 xJcicZ572 : sJcicZ572) {
				OccursList occursListB = new OccursList();
				// 572 layout回傳
				occursListB.putParam("OOChainTxCd", "L8334");
				occursListB.putParam("OOHistoryTxCd", "L8064");
				occursListB.putParam("OOUkey", xJcicZ572.getUkey());
				occursListB.putParam("OOCustId", xJcicZ572.getCustId());
				occursListB.putParam("OORcDate", xJcicZ572.getApplyDate());
				occursListB.putParam("OOPayDate", xJcicZ572.getPayDate());
				occursListB.putParam("OOSubmitKey", xJcicZ572.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ572.getSubmitKey(), titaVo));
				occursListB.putParam("OOBankId", xJcicZ572.getBankId());
				occursListB.putParam("OOBankIdX", dealBankName(xJcicZ572.getBankId(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ572.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ572.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ572 rJcicZ572 = new JcicZ572();
				JcicZ572Id rJcicZ572Id = new JcicZ572Id();
				rJcicZ572Id.setApplyDate(xJcicZ572.getApplyDate());
				rJcicZ572Id.setCustId(xJcicZ572.getCustId());
				rJcicZ572Id.setBankId(xJcicZ572.getBankId());
				rJcicZ572Id.setPayDate(xJcicZ572.getPayDate());
				rJcicZ572Id.setSubmitKey(xJcicZ572.getSubmitKey());
				rJcicZ572 = iJcicZ572Service.findById(rJcicZ572Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ572.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ572Log> iJcicZ572Log = null;
				iJcicZ572Log = iJcicZ572LogService.ukeyEq(xJcicZ572.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ572Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ573(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8335");
		occursListA.putParam("OOHistoryTxCd", "L8065");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ573> sJcicZ573 = null;
		sJcicZ573 = iJcicZ573Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ573 != null) {
			for (JcicZ573 xJcicZ573 : sJcicZ573) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8335");
				occursListB.putParam("OOHistoryTxCd", "L8065");
				occursListB.putParam("OOUkey", xJcicZ573.getUkey());
				occursListB.putParam("OOCustId", xJcicZ573.getCustId());
				occursListB.putParam("OORcDate", xJcicZ573.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ573.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ573.getSubmitKey(), titaVo));
				occursListB.putParam("OOPayDate", xJcicZ573.getPayDate());
				occursListB.putParam("OOTranKey", xJcicZ573.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ573.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ573 rJcicZ573 = new JcicZ573();
				JcicZ573Id rJcicZ573Id = new JcicZ573Id();
				rJcicZ573Id.setApplyDate(xJcicZ573.getApplyDate());
				rJcicZ573Id.setCustId(xJcicZ573.getCustId());
				rJcicZ573Id.setSubmitKey(xJcicZ573.getSubmitKey());
				rJcicZ573Id.setPayDate(xJcicZ573.getPayDate());
				rJcicZ573 = iJcicZ573Service.findById(rJcicZ573Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ573.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ573Log> iJcicZ573Log = null;
				iJcicZ573Log = iJcicZ573LogService.ukeyEq(xJcicZ573.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ573Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ574(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8336");
		occursListA.putParam("OOHistoryTxCd", "L8066");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ574> sJcicZ574 = null;
		sJcicZ574 = iJcicZ574Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ574 != null) {
			for (JcicZ574 xJcicZ574 : sJcicZ574) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8336");
				occursListB.putParam("OOHistoryTxCd", "L8066");
				occursListB.putParam("OOUkey", xJcicZ574.getUkey());
				occursListB.putParam("OOCustId", xJcicZ574.getCustId());
				occursListB.putParam("OORcDate", xJcicZ574.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ574.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ574.getSubmitKey(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ574.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ574.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ574 rJcicZ574 = new JcicZ574();
				JcicZ574Id rJcicZ574Id = new JcicZ574Id();
				rJcicZ574Id.setApplyDate(xJcicZ574.getApplyDate());
				rJcicZ574Id.setCustId(xJcicZ574.getCustId());
				rJcicZ574Id.setSubmitKey(xJcicZ574.getSubmitKey());
				rJcicZ574 = iJcicZ574Service.findById(rJcicZ574Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ574.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ574Log> iJcicZ574Log = null;
				iJcicZ574Log = iJcicZ574LogService.ukeyEq(xJcicZ574.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ574Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");

				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public void dealZ575(String custId, int index, int limit, TitaVo titaVo) throws LogicException {
		// 固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8337");
		occursListA.putParam("OOHistoryTxCd", "L8067");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		// 固定回傳
		occursListA.putParam("OODelayYM", "");
		occursListA.putParam("OOMaxMainCode", "");
		occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOCloseDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");
		occursListA.putParam("OOMaxMainCodeX", "");
		occursListA.putParam("OOCourtCodeX", "");

		this.totaVo.addOccursList(occursListA);
		// 固定內容結束

		// 若有資料則以下處理回傳
		Slice<JcicZ575> sJcicZ575 = null;
		sJcicZ575 = iJcicZ575Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ575 != null) {
			for (JcicZ575 xJcicZ575 : sJcicZ575) {
				OccursList occursListB = new OccursList();
				// 575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8337");
				occursListB.putParam("OOHistoryTxCd", "L8067");
				occursListB.putParam("OOUkey", xJcicZ575.getUkey());
				occursListB.putParam("OOCustId", xJcicZ575.getCustId());
				occursListB.putParam("OORcDate", xJcicZ575.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ575.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ575.getSubmitKey(), titaVo));
				occursListB.putParam("OOBankId", xJcicZ575.getBankId());
				occursListB.putParam("OOBankIdX", dealBankName(xJcicZ575.getBankId(), titaVo));
				occursListB.putParam("OOTranKey", xJcicZ575.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ575.getOutJcicTxtDate();
				if (iOutJcicTxtDate == 0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				} else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				// 按鈕控制
				JcicZ575 rJcicZ575 = new JcicZ575();
				JcicZ575Id rJcicZ575Id = new JcicZ575Id();
				rJcicZ575Id.setApplyDate(xJcicZ575.getApplyDate());
				rJcicZ575Id.setBankId(xJcicZ575.getBankId());
				rJcicZ575Id.setCustId(xJcicZ575.getCustId());
				rJcicZ575Id.setSubmitKey(xJcicZ575.getSubmitKey());
				rJcicZ575 = iJcicZ575Service.findById(rJcicZ575Id, titaVo);
				// 已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ575.getOutJcicTxtDate() == 0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				} else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}

				// 歷程按鈕控制
				Slice<JcicZ575Log> iJcicZ575Log = null;
				iJcicZ575Log = iJcicZ575LogService.ukeyEq(xJcicZ575.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ575Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				} else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				// 固定回傳
				occursListB.putParam("OODelayYM", "");
				occursListB.putParam("OOMaxMainCode", "");
				occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOCloseDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				occursListB.putParam("OOMaxMainCodeX", "");
				occursListB.putParam("OOCourtCodeX", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}

	public String dealBankName(String BankId, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = new CdCode();
		tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", BankId, titaVo);
		String JcicBankName = "";// 80碼長度
		if (tCdCode != null) {
			JcicBankName = tCdCode.getItem();
		}
		return JcicBankName;
	}

//	public String dealCourtName(String CourtId, TitaVo titaVo) throws LogicException {
//		CdCode tCdCode = new CdCode();
//		tCdCode = iCdCodeService.getItemFirst(8, "CourtCode", CourtId, titaVo);
//		String JcicCourtName = "";// 80碼長度
//		if (tCdCode != null) {
//			JcicCourtName = tCdCode.getItem();
//		}
//		return JcicCourtName;
//	}
	public String dealCourtName(String CourtId, TitaVo titaVo) throws LogicException {
		String JcicCourtName = "";// 80碼長度
		
		CdCodeId iCdCodeId = new CdCodeId();
		iCdCodeId.setDefCode("CourtCode");
		iCdCodeId.setCode(CourtId);
		CdCode iCdCode = iCdCodeService.findById(iCdCodeId, titaVo);
		if (iCdCode == null) {
			CdCity iCdCity = iCdCityService.findById(CourtId, titaVo);
			if (iCdCity !=null) {
				JcicCourtName = iCdCity.getCityItem();
			}
		}else {
			JcicCourtName = iCdCode.getItem();
		}
		return JcicCourtName;
	}
}