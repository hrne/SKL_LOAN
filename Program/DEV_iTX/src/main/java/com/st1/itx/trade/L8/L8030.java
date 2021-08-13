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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.domain.JcicZ040Log;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Id;
import com.st1.itx.db.domain.JcicZ041Log;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;
import com.st1.itx.db.domain.JcicZ042Log;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;
import com.st1.itx.db.domain.JcicZ043Log;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
import com.st1.itx.db.domain.JcicZ044Log;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ045LogService;
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
import com.st1.itx.db.domain.JcicZ045Log;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.db.service.JcicZ046LogService;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.domain.JcicZ046Log;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047LogService;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ048LogService;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.domain.JcicZ048Log;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ049LogService;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.db.service.JcicZ050LogService;
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;
import com.st1.itx.db.domain.JcicZ050Log;
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
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;
import com.st1.itx.db.domain.JcicZ570Log;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Id;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Id;
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Id;
import com.st1.itx.db.domain.JcicZ573Log;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Id;
import com.st1.itx.db.domain.JcicZ574Log;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Id;
import com.st1.itx.db.domain.JcicZ575Log;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446LogService;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.domain.JcicZ446Log;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448LogService;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ450LogService;
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;
import com.st1.itx.db.domain.JcicZ450Log;
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
	// private static final Logger logger = LoggerFactory.getLogger(L8030.class);
	
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
/*		程式邏輯by Fegie
 * 		occursListA =>查詢結果固定返回新增資料
 * 		occursListB =>可異動刪除資料
 *      OOStatusFg => 狀態參數 0:新增 1:異動 2:關閉按鈕
 *      OOHistoryFg => log檔筆數不等於0筆時為1:開啟 否則為0:關閉
 *      OODeleteFg => 當該筆資料之轉jcic日期為空白時為1:開啟 否則為0:關閉
*/
		String iTranCode = titaVo.getParam("TranCode");
		String iCustId = titaVo.getParam("CustId");
		this.info("TranCode==========="+iTranCode);
		switch(iTranCode) {
		case"040":
			dealZ040(iCustId,this.index,this.limit,titaVo);
			break;
		case"041":
			dealZ041(iCustId,this.index,this.limit,titaVo);
			break;
		case"042":
			dealZ042(iCustId,this.index,this.limit,titaVo);
			break;
		case"043":
			dealZ043(iCustId,this.index,this.limit,titaVo);
			break;
		case"046":
			dealZ046(iCustId,this.index,this.limit,titaVo);
			break;
		case"048":
			dealZ048(iCustId,this.index,this.limit,titaVo);
			break;
		case"050":
			dealZ050(iCustId,this.index,this.limit,titaVo);
			break;
//		case"063":
//			dealZ063(iCustId,this.index,this.limit,titaVo);
//			break;
		case"446":
			dealZ446(iCustId,this.index,this.limit,titaVo);
			break;
		case"450":
			dealZ450(iCustId,this.index,this.limit,titaVo);
			break;
		case"570":
			dealZ570(iCustId,this.index,this.limit,titaVo);
			break;
		case"571":
			dealZ571(iCustId,this.index,this.limit,titaVo);
			break;
		case"572":
			dealZ572(iCustId,this.index,this.limit,titaVo);
			break;
		case"573":
			dealZ573(iCustId,this.index,this.limit,titaVo);
			break;
		case"574":
			dealZ574(iCustId,this.index,this.limit,titaVo);
			break;
		case"575":
			dealZ575(iCustId,this.index,this.limit,titaVo);
			break;
		default:
			break;
		}
		
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	public void dealZ040(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8301");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ040> sJcicZ040 = null;
	    sJcicZ040 = iJcicZ040Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ040 != null) {
	        for (JcicZ040 xJcicZ040:sJcicZ040) {
	            OccursList occursListB = new OccursList();
	            //040 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8301");
	            occursListB.putParam("OOUkey", xJcicZ040.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ040.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ040.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ040.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ040.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ040.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ040.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ040 rJcicZ040 = new JcicZ040();
	            JcicZ040Id rJcicZ040Id = new JcicZ040Id();
	            rJcicZ040Id.setRcDate(xJcicZ040.getRcDate());
	            rJcicZ040Id.setCustId(xJcicZ040.getCustId());
	            rJcicZ040Id.setSubmitKey(xJcicZ040.getSubmitKey());	
	            rJcicZ040 = iJcicZ040Service.findById(rJcicZ040Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ040.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ040Log> iJcicZ040Log = null;
	            iJcicZ040Log = iJcicZ040LogService.ukeyEq(rJcicZ040.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ040Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ041(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8302");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ041> sJcicZ041 = null;
	    sJcicZ041 = iJcicZ041Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ041 != null) {
	        for (JcicZ041 xJcicZ041:sJcicZ041) {
	            OccursList occursListB = new OccursList();
	            //041 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8302");
	            occursListB.putParam("OOUkey", xJcicZ041.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ041.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ041.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ041.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ041.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ041.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ041.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ041 rJcicZ041 = new JcicZ041();
	            JcicZ041Id rJcicZ041Id = new JcicZ041Id();
	            rJcicZ041Id.setRcDate(xJcicZ041.getRcDate());
	            rJcicZ041Id.setCustId(xJcicZ041.getCustId());
	            rJcicZ041Id.setSubmitKey(xJcicZ041.getSubmitKey());	
	            rJcicZ041 = iJcicZ041Service.findById(rJcicZ041Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ041.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ041Log> iJcicZ041Log = null;
	            iJcicZ041Log = iJcicZ041LogService.ukeyEq(rJcicZ041.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ041Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ042(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8303");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ042> sJcicZ042 = null;
	    sJcicZ042 = iJcicZ042Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ042 != null) {
	        for (JcicZ042 xJcicZ042:sJcicZ042) {
	            OccursList occursListB = new OccursList();
	            //042 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8303");
	            occursListB.putParam("OOUkey", xJcicZ042.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ042.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ042.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ042.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ042.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ042.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ042.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ042 rJcicZ042 = new JcicZ042();
	            JcicZ042Id rJcicZ042Id = new JcicZ042Id();
	            rJcicZ042Id.setRcDate(xJcicZ042.getRcDate());
	            rJcicZ042Id.setCustId(xJcicZ042.getCustId());
	            rJcicZ042Id.setSubmitKey(xJcicZ042.getSubmitKey());	
	            rJcicZ042 = iJcicZ042Service.findById(rJcicZ042Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ042.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ042Log> iJcicZ042Log = null;
	            iJcicZ042Log = iJcicZ042LogService.ukeyEq(rJcicZ042.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ042Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ043(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8304");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    //固定回傳
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ043> sJcicZ043 = null;
	    sJcicZ043 = iJcicZ043Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ043 != null) {
	        for (JcicZ043 xJcicZ043:sJcicZ043) {
	            OccursList occursListB = new OccursList();
	            //043 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8304");
	            occursListB.putParam("OOUkey", xJcicZ043.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ043.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ043.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ043.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ043.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ043.getTranKey());
	            occursListB.putParam("OOMaxMainCode", xJcicZ043.getMaxMainCode());
	    	    occursListB.putParam("OOAccount", xJcicZ043.getAccount());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ043.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ043 rJcicZ043 = new JcicZ043();
	            JcicZ043Id rJcicZ043Id = new JcicZ043Id();
	            rJcicZ043Id.setRcDate(xJcicZ043.getRcDate());
	            rJcicZ043Id.setCustId(xJcicZ043.getCustId());
	            rJcicZ043Id.setSubmitKey(xJcicZ043.getSubmitKey());	
	            rJcicZ043Id.setMaxMainCode(xJcicZ043.getMaxMainCode());
	            rJcicZ043Id.setAccount(xJcicZ043.getAccount());
	            rJcicZ043 = iJcicZ043Service.findById(rJcicZ043Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ043.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ043Log> iJcicZ043Log = null;
	            iJcicZ043Log = iJcicZ043LogService.ukeyEq(rJcicZ043.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ043Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ044(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8305");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ044> sJcicZ044 = null;
	    sJcicZ044 = iJcicZ044Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ044 != null) {
	        for (JcicZ044 xJcicZ044:sJcicZ044) {
	            OccursList occursListB = new OccursList();
	            //044 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8305");
	            occursListB.putParam("OOUkey", xJcicZ044.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ044.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ044.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ044.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ044.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ044.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ044.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ044 rJcicZ044 = new JcicZ044();
	            JcicZ044Id rJcicZ044Id = new JcicZ044Id();
	            rJcicZ044Id.setRcDate(xJcicZ044.getRcDate());
	            rJcicZ044Id.setCustId(xJcicZ044.getCustId());
	            rJcicZ044Id.setSubmitKey(xJcicZ044.getSubmitKey());	
	            rJcicZ044 = iJcicZ044Service.findById(rJcicZ044Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ044.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ044Log> iJcicZ044Log = null;
	            iJcicZ044Log = iJcicZ044LogService.ukeyEq(rJcicZ044.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ044Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	    	    occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ045(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8306");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    occursListA.putParam("OOMaxMainCode", "");
	    //固定回傳
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ045> sJcicZ045 = null;
	    sJcicZ045 = iJcicZ045Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ045 != null) {
	        for (JcicZ045 xJcicZ045:sJcicZ045) {
	            OccursList occursListB = new OccursList();
	            //044 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8306");
	            occursListB.putParam("OOUkey", xJcicZ045.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ045.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ045.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ045.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ045.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ045.getTranKey());
	    	    occursListB.putParam("OOMaxMainCode", xJcicZ045.getMaxMainCode());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ045.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ045 rJcicZ045 = new JcicZ045();
	            JcicZ045Id rJcicZ045Id = new JcicZ045Id();
	            rJcicZ045Id.setRcDate(xJcicZ045.getRcDate());
	            rJcicZ045Id.setCustId(xJcicZ045.getCustId());
	            rJcicZ045Id.setSubmitKey(xJcicZ045.getSubmitKey());	
	            rJcicZ045Id.setMaxMainCode(xJcicZ045.getMaxMainCode());
	            rJcicZ045 = iJcicZ045Service.findById(rJcicZ045Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ045.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ045Log> iJcicZ045Log = null;
	            iJcicZ045Log = iJcicZ045LogService.ukeyEq(rJcicZ045.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ045Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ046(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8307");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    occursListA.putParam("OOClosedDate", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ046> sJcicZ046 = null;
	    sJcicZ046 = iJcicZ046Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ046 != null) {
	        for (JcicZ046 xJcicZ046:sJcicZ046) {
	            OccursList occursListB = new OccursList();
	            //046 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8307");
	            occursListB.putParam("OOUkey", xJcicZ046.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ046.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ046.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ046.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ046.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ046.getTranKey());
	    	    occursListB.putParam("OOClosedDate", xJcicZ046.getCloseDate());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ046.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ046 rJcicZ046 = new JcicZ046();
	            JcicZ046Id rJcicZ046Id = new JcicZ046Id();
	            rJcicZ046Id.setRcDate(xJcicZ046.getRcDate());
	            rJcicZ046Id.setCustId(xJcicZ046.getCustId());
	            rJcicZ046Id.setSubmitKey(xJcicZ046.getSubmitKey());	
	            rJcicZ046Id.setCloseDate(xJcicZ046.getCloseDate());
	            rJcicZ046 = iJcicZ046Service.findById(rJcicZ046Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ046.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ046Log> iJcicZ046Log = null;
	            iJcicZ046Log = iJcicZ046LogService.ukeyEq(rJcicZ046.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ046Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ048(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8309");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");
	    occursListA.putParam("OOClosedDate", "");
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ048> sJcicZ048 = null;
	    sJcicZ048 = iJcicZ048Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ048 != null) {
	        for (JcicZ048 xJcicZ048:sJcicZ048) {
	            OccursList occursListB = new OccursList();
	            //048 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8309");
	            occursListB.putParam("OOUkey", xJcicZ048.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ048.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ048.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ048.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ048.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ048.getTranKey());

	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ048.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ048 rJcicZ048 = new JcicZ048();
	            JcicZ048Id rJcicZ048Id = new JcicZ048Id();
	            rJcicZ048Id.setRcDate(xJcicZ048.getRcDate());
	            rJcicZ048Id.setCustId(xJcicZ048.getCustId());
	            rJcicZ048Id.setSubmitKey(xJcicZ048.getSubmitKey());	
	            rJcicZ048 = iJcicZ048Service.findById(rJcicZ048Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ048.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ048Log> iJcicZ048Log = null;
	            iJcicZ048Log = iJcicZ048LogService.ukeyEq(rJcicZ048.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ048Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ050(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8311");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    occursListA.putParam("OOPayDate", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ050> sJcicZ050 = null;
	    sJcicZ050 = iJcicZ050Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ050 != null) {
	        for (JcicZ050 xJcicZ050:sJcicZ050) {
	            OccursList occursListB = new OccursList();
	            //050 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8311");
	            occursListB.putParam("OOUkey", xJcicZ050.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ050.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ050.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ050.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ050.getRcDate());
	            occursListB.putParam("OOTranKey", xJcicZ050.getTranKey());
	    	    occursListB.putParam("OOPayDate", xJcicZ050.getPayDate());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ050.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ050 rJcicZ050 = new JcicZ050();
	            JcicZ050Id rJcicZ050Id = new JcicZ050Id();
	            rJcicZ050Id.setRcDate(xJcicZ050.getRcDate());
	            rJcicZ050Id.setCustId(xJcicZ050.getCustId());
	            rJcicZ050Id.setSubmitKey(xJcicZ050.getSubmitKey());				
	            rJcicZ050 = iJcicZ050Service.findById(rJcicZ050Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ050.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ050Log> iJcicZ050Log = null;
	            iJcicZ050Log = iJcicZ050LogService.ukeyEq(xJcicZ050.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ050Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
//	public void dealZ063(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
//        //固定內容起始
//		OccursList occursListA = new OccursList();
//		occursListA.putParam("OOStatusFg", "0");
//		occursListA.putParam("OOHistoryFg", "0");
//		occursListA.putParam("OODeleteFg", "0");
//		occursListA.putParam("OOChainTxCd", "L8321");
//		occursListA.putParam("OOUkey", "");
//		occursListA.putParam("OOCustId", custId);
//		occursListA.putParam("OORcDate", "");
//		occursListA.putParam("OOSubmitKey", "");
//		occursListA.putParam("OOSubmitKeyX", "");
//		occursListA.putParam("OOChangePayDate", "");
//		occursListA.putParam("OOClosedDate", "");
//		occursListA.putParam("OOOutJcicTxtDate", "");
//		occursListA.putParam("OOTranKey", "");
//		//固定回傳
//		occursListA.putParam("OOPayDate", "");
//		occursListA.putParam("OOBankId", "");
//		occursListA.putParam("OOBankIdX", "");
//		occursListA.putParam("OOClaimDate", "");
//		occursListA.putParam("OOCourtCode", "");
//		occursListA.putParam("OOCaseStatus", "");	
//		this.totaVo.addOccursList(occursListA);
//		//固定內容結束
//		
//		//若有資料則以下處理回傳
//		Slice<JcicZ063> sJcicZ063 = null;
//		sJcicZ063 = iJcicZ063Service.CustIdEq(custId, this.index, this.limit, titaVo);
//		if (sJcicZ063 != null) {
//			for (JcicZ063 xJcicZ063:sJcicZ063) {
//				OccursList occursListB = new OccursList();
//				//063 layout回傳
//				occursListB.putParam("OOChainTxCd", "L8321");
//				occursListB.putParam("OOUkey", xJcicZ063.getUkey());
//				occursListB.putParam("OOCustId", xJcicZ063.getCustId());
//				occursListB.putParam("OORcDate", xJcicZ063.getRcDate());
//				occursListB.putParam("OOSubmitKey", xJcicZ063.getSubmitKey());
//				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ063.getSubmitKey(),titaVo));
//				occursListB.putParam("OOChangePayDate", xJcicZ063.getChangePayDate());
//				occursListB.putParam("OOClosedDate", xJcicZ063.getClosedDate());
//				occursListB.putParam("OOTranKey", xJcicZ063.getTranKey());
//				int iOutJcicTxtDate = 0;
//				iOutJcicTxtDate = xJcicZ063.getOutJcicTxtDate();
//				if (iOutJcicTxtDate ==0) {
//					occursListB.putParam("OOOutJcicTxtDate", "");
//				}else {
//					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
//				}
//				//按鈕控制
//				JcicZ063 rJcicZ063 = new JcicZ063();
//				JcicZ063Id rJcicZ063Id = new JcicZ063Id();
//				rJcicZ063Id.setRcDate(xJcicZ063.getRcDate());
//				rJcicZ063Id.setCustId(xJcicZ063.getCustId());
//				rJcicZ063Id.setSubmitKey(xJcicZ063.getSubmitKey());
//				rJcicZ063Id.setChangePayDate(xJcicZ063.getChangePayDate());
//				rJcicZ063 = iJcicZ063Service.findById(rJcicZ063Id, titaVo);
//				//已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
//				if (rJcicZ063.getOutJcicTxtDate()==0) {
//					occursListB.putParam("OODeleteFg", "1");
//					occursListB.putParam("OOStatusFg", "2");
//				}else {
//					occursListB.putParam("OODeleteFg", "0");
//					occursListB.putParam("OOStatusFg", "1");
//				}
//				
//				//歷程按鈕控制
//				Slice<JcicZ063Log> iJcicZ063Log = null;
//				iJcicZ063Log = iJcicZ063LogService.ukeyEq(xJcicZ063.getUkey(), 0, Integer.MAX_VALUE, titaVo);
//				if (iJcicZ063Log == null) {
//					occursListB.putParam("OOHistoryFg", "0");
//				}else {
//					occursListB.putParam("OOHistoryFg", "1");
//				}
//				//固定回傳
//				occursListB.putParam("OOBankId","" );
//				occursListB.putParam("OOBankIdX", "");
//				occursListB.putParam("OOPayDate", "");
//				occursListB.putParam("OOClaimDate", "");
//				occursListB.putParam("OOCourtCode", "");
//				occursListB.putParam("OOCaseStatus", "");
//				
//				this.totaVo.addOccursList(occursListB);
//			}
//		}
//	}
	
	public void dealZ446(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8326");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ446> sJcicZ446 = null;
	    sJcicZ446 = iJcicZ446Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ446 != null) {
	        for (JcicZ446 xJcicZ446:sJcicZ446) {
	            OccursList occursListB = new OccursList();
	            //446 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8326");
	            occursListB.putParam("OOUkey", xJcicZ446.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ446.getCustId());
	            occursListB.putParam("OORcDate", xJcicZ446.getApplyDate());
	            occursListB.putParam("OOSubmitKey", xJcicZ446.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ446.getSubmitKey(),titaVo));
	            occursListB.putParam("OOTranKey", xJcicZ446.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ446.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ446 rJcicZ446 = new JcicZ446();
	            JcicZ446Id rJcicZ446Id = new JcicZ446Id();
	            rJcicZ446Id.setApplyDate(xJcicZ446.getApplyDate());
	            rJcicZ446Id.setCustId(xJcicZ446.getCustId());
	            rJcicZ446Id.setCourtCode(xJcicZ446.getCourtCode());
	            rJcicZ446Id.setSubmitKey(xJcicZ446.getSubmitKey());				
	            rJcicZ446 = iJcicZ446Service.findById(rJcicZ446Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ446.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ446Log> iJcicZ446Log = null;
	            iJcicZ446Log = iJcicZ446LogService.ukeyEq(rJcicZ446.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ446Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	            occursListB.putParam("OOBankId", "");
	            occursListB.putParam("OOBankIdX", "");	
	            occursListB.putParam("OOClosedDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ450(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8329");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOTranKey", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOPayDate", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ450> sJcicZ450 = null;
	    sJcicZ450 = iJcicZ450Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ450 != null) {
	        for (JcicZ450 xJcicZ450:sJcicZ450) {
	            OccursList occursListB = new OccursList();
	            //450 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8329");
	            occursListB.putParam("OOUkey", xJcicZ450.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ450.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ450.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ450.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ450.getApplyDate());
	            occursListB.putParam("OOTranKey", xJcicZ450.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ450.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ450 rJcicZ450 = new JcicZ450();
	            JcicZ450Id rJcicZ450Id = new JcicZ450Id();
	            rJcicZ450Id.setCustId(xJcicZ450.getCustId());
	            rJcicZ450Id.setSubmitKey(xJcicZ450.getSubmitKey());	
	            rJcicZ450Id.setApplyDate(xJcicZ450.getApplyDate());
	            rJcicZ450Id.setCourtCode(xJcicZ450.getCourtCode());
	            rJcicZ450Id.setPayDate(xJcicZ450.getPayDate());
	            rJcicZ450 = iJcicZ450Service.findById(rJcicZ450Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ450.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ450Log> iJcicZ450Log = null;
	            iJcicZ450Log = iJcicZ450LogService.ukeyEq(rJcicZ450.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ450Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	    	    occursListB.putParam("OOPayDate", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	
	public void dealZ570(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8332");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OOTranKey", "");
	     
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ570> sJcicZ570 = null;
	    sJcicZ570 = iJcicZ570Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ570 != null) {
	        for (JcicZ570 xJcicZ570:sJcicZ570) {
	            OccursList occursListB = new OccursList();
	            //570 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8332");
	            occursListB.putParam("OOUkey", xJcicZ570.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ570.getCustId());
	            occursListB.putParam("OOSubmitKey", xJcicZ570.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ570.getSubmitKey(),titaVo));
	            occursListB.putParam("OORcDate", xJcicZ570.getApplyDate());
	            occursListB.putParam("OOTranKey", xJcicZ570.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ570.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ570 rJcicZ570 = new JcicZ570();
	            JcicZ570Id rJcicZ570Id = new JcicZ570Id();
	            rJcicZ570Id.setApplyDate(xJcicZ570.getApplyDate());
	            rJcicZ570Id.setCustId(xJcicZ570.getCustId());
	            rJcicZ570Id.setSubmitKey(xJcicZ570.getSubmitKey());				
	            rJcicZ570 = iJcicZ570Service.findById(rJcicZ570Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ570.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ570Log> iJcicZ570Log = null;
	            iJcicZ570Log = iJcicZ570LogService.ukeyEq(xJcicZ570.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ570Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	    	    occursListB.putParam("OOPayDate", "");
	    	    occursListB.putParam("OOBankId", "");
	    	    occursListB.putParam("OOBankIdX", "");
	    	    occursListB.putParam("OORcDate", "");
	    	    occursListB.putParam("OOChangePayDate", "");
	    	    occursListB.putParam("OOClosedDate", "");
	    	    occursListB.putParam("OOClaimDate", "");
	    	    occursListB.putParam("OOCourtCode", "");
	    	    occursListB.putParam("OOCaseStatus", "");	
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	public void dealZ571(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
	    //固定內容起始
	    OccursList occursListA = new OccursList();
	    occursListA.putParam("OOStatusFg", "0");
	    occursListA.putParam("OOHistoryFg", "0");
	    occursListA.putParam("OODeleteFg", "0");
	    occursListA.putParam("OOChainTxCd", "L8333");
	    occursListA.putParam("OOUkey", "");
	    occursListA.putParam("OOCustId", custId);
	    occursListA.putParam("OORcDate", "");
	    occursListA.putParam("OOSubmitKey", "");
	    occursListA.putParam("OOSubmitKeyX", "");
	    occursListA.putParam("OOBankId", "");
	    occursListA.putParam("OOBankIdX", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OOTranKey", "");
	    //固定回傳
	    occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
	    occursListA.putParam("OOPayDate", "");
	    occursListA.putParam("OOChangePayDate", "");
	    occursListA.putParam("OOClosedDate", "");
	    occursListA.putParam("OOClaimDate", "");
	    occursListA.putParam("OOCourtCode", "");
	    occursListA.putParam("OOCaseStatus", "");	
	    this.totaVo.addOccursList(occursListA);
	    //固定內容結束
	    
	    //若有資料則以下處理回傳
	    Slice<JcicZ571> sJcicZ571 = null;
	    sJcicZ571 = iJcicZ571Service.CustIdEq(custId, this.index, this.limit, titaVo);
	    if (sJcicZ571 != null) {
	        for (JcicZ571 xJcicZ571:sJcicZ571) {
	            OccursList occursListB = new OccursList();
	            //571 layout回傳
	            occursListB.putParam("OOChainTxCd", "L8333");
	            occursListB.putParam("OOUkey", xJcicZ571.getUkey());
	            occursListB.putParam("OOCustId", xJcicZ571.getCustId());
	            occursListB.putParam("OORcDate", xJcicZ571.getApplyDate());
	            occursListB.putParam("OOSubmitKey", xJcicZ571.getSubmitKey());
	            occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ571.getSubmitKey(),titaVo));
	            occursListB.putParam("OOBankId", xJcicZ571.getBankId());
	            occursListB.putParam("OOBankIdX", dealBankName(xJcicZ571.getBankId(),titaVo));	
	            occursListB.putParam("OOTranKey", xJcicZ571.getTranKey());
	            int iOutJcicTxtDate = 0;
	            iOutJcicTxtDate = xJcicZ571.getOutJcicTxtDate();
	            if (iOutJcicTxtDate ==0) {
	                occursListB.putParam("OOOutJcicTxtDate", "");
	            }else {
	                occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
	            }
	            //按鈕控制
	            JcicZ571 rJcicZ571 = new JcicZ571();
	            JcicZ571Id rJcicZ571Id = new JcicZ571Id();
	            rJcicZ571Id.setApplyDate(xJcicZ571.getApplyDate());
	            rJcicZ571Id.setCustId(xJcicZ571.getCustId());
	            rJcicZ571Id.setBankId(xJcicZ571.getBankId());
	            rJcicZ571Id.setSubmitKey(xJcicZ571.getSubmitKey());				
	            rJcicZ571 = iJcicZ571Service.findById(rJcicZ571Id, titaVo);
	            //已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
	            if (rJcicZ571.getOutJcicTxtDate()==0) {
	                occursListB.putParam("OODeleteFg", "1");
	                occursListB.putParam("OOStatusFg", "2");
	            }else {
	                occursListB.putParam("OODeleteFg", "0");
	                occursListB.putParam("OOStatusFg", "1");
	            }
	            
	            //歷程按鈕控制
	            Slice<JcicZ571Log> iJcicZ571Log = null;
	            iJcicZ571Log = iJcicZ571LogService.ukeyEq(xJcicZ571.getUkey(), 0, Integer.MAX_VALUE, titaVo);
	            if (iJcicZ571Log == null) {
	                occursListB.putParam("OOHistoryFg", "0");
	            }else {
	                occursListB.putParam("OOHistoryFg", "1");
	            }
	            //固定回傳
	            occursListB.putParam("OOMaxMainCode", "");
	    	    occursListB.putParam("OOAccount", "");
	            occursListB.putParam("OOClosedDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
	            this.totaVo.addOccursList(occursListB);
	        }
	    }
	}
	public void dealZ572(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
        //固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8334");
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
		//固定回傳
		occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClosedDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");	
		this.totaVo.addOccursList(occursListA);
		//固定內容結束
		
		//若有資料則以下處理回傳
		Slice<JcicZ572> sJcicZ572 = null;
		sJcicZ572 = iJcicZ572Service.CustIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ572 != null) {
			for (JcicZ572 xJcicZ572:sJcicZ572) {
				OccursList occursListB = new OccursList();
				//572 layout回傳
				occursListB.putParam("OOChainTxCd", "L8334");
				occursListB.putParam("OOUkey", xJcicZ572.getUkey());
				occursListB.putParam("OOCustId", xJcicZ572.getCustId());
				occursListB.putParam("OORcDate", xJcicZ572.getApplyDate());
				occursListB.putParam("OOPayDate", xJcicZ572.getPayDate());
				occursListB.putParam("OOSubmitKey", xJcicZ572.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ572.getSubmitKey(),titaVo));
				occursListB.putParam("OOBankId", xJcicZ572.getBankId());
				occursListB.putParam("OOBankIdX", dealBankName(xJcicZ572.getBankId(),titaVo));	
				occursListB.putParam("OOTranKey", xJcicZ572.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ572.getOutJcicTxtDate();
				if (iOutJcicTxtDate ==0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				}else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				//按鈕控制
				JcicZ572 rJcicZ572 = new JcicZ572();
				JcicZ572Id rJcicZ572Id = new JcicZ572Id();
				rJcicZ572Id.setApplyDate(xJcicZ572.getApplyDate());
				rJcicZ572Id.setCustId(xJcicZ572.getCustId());
				rJcicZ572Id.setBankId(xJcicZ572.getBankId());
				rJcicZ572Id.setPayDate(xJcicZ572.getPayDate());
				rJcicZ572Id.setSubmitKey(xJcicZ572.getSubmitKey());				
				rJcicZ572 = iJcicZ572Service.findById(rJcicZ572Id, titaVo);
				//已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ572.getOutJcicTxtDate()==0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				}else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}
				
				//歷程按鈕控制
				Slice<JcicZ572Log> iJcicZ572Log = null;
				iJcicZ572Log = iJcicZ572LogService.ukeyEq(xJcicZ572.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ572Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				}else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				//固定回傳
				occursListB.putParam("OOMaxMainCode", "");
			    occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOClosedDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				this.totaVo.addOccursList(occursListB);
			}
		}
	}
	public void dealZ573(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
        //固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8335");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		//固定回傳
		occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOClosedDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");	
		this.totaVo.addOccursList(occursListA);
		//固定內容結束
		
		//若有資料則以下處理回傳
		Slice<JcicZ573> sJcicZ573 = null;
		sJcicZ573 = iJcicZ573Service.CustIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ573 != null) {
			for (JcicZ573 xJcicZ573:sJcicZ573) {
				OccursList occursListB = new OccursList();
				//575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8335");
				occursListB.putParam("OOUkey", xJcicZ573.getUkey());
				occursListB.putParam("OOCustId", xJcicZ573.getCustId());
				occursListB.putParam("OORcDate", xJcicZ573.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ573.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ573.getSubmitKey(),titaVo));
				occursListB.putParam("OOPayDate", xJcicZ573.getPayDate());
				occursListB.putParam("OOTranKey", xJcicZ573.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ573.getOutJcicTxtDate();
				if (iOutJcicTxtDate ==0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				}else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				//按鈕控制
				JcicZ573 rJcicZ573 = new JcicZ573();
				JcicZ573Id rJcicZ573Id = new JcicZ573Id();
				rJcicZ573Id.setApplyDate(xJcicZ573.getApplyDate());
				rJcicZ573Id.setCustId(xJcicZ573.getCustId());
				rJcicZ573Id.setSubmitKey(xJcicZ573.getSubmitKey());
				rJcicZ573Id.setPayDate(xJcicZ573.getPayDate());
				rJcicZ573 = iJcicZ573Service.findById(rJcicZ573Id, titaVo);
				//已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ573.getOutJcicTxtDate()==0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				}else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}
				
				//歷程按鈕控制
				Slice<JcicZ573Log> iJcicZ573Log = null;
				iJcicZ573Log = iJcicZ573LogService.ukeyEq(xJcicZ573.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ573Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				}else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				//固定回傳
				occursListB.putParam("OOMaxMainCode", "");
			    occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOClosedDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				
				this.totaVo.addOccursList(occursListB);
			}
		}
	}
	public void dealZ574(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
        //固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8336");
		occursListA.putParam("OOUkey", "");
		occursListA.putParam("OOCustId", custId);
		occursListA.putParam("OORcDate", "");
		occursListA.putParam("OOSubmitKey", "");
		occursListA.putParam("OOSubmitKeyX", "");
		occursListA.putParam("OOOutJcicTxtDate", "");
		occursListA.putParam("OOTranKey", "");
		//固定回傳
		occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOBankId", "");
		occursListA.putParam("OOBankIdX", "");
		occursListA.putParam("OOClosedDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");	
		this.totaVo.addOccursList(occursListA);
		//固定內容結束
		
		//若有資料則以下處理回傳
		Slice<JcicZ574> sJcicZ574 = null;
		sJcicZ574 = iJcicZ574Service.CustIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ574 != null) {
			for (JcicZ574 xJcicZ574:sJcicZ574) {
				OccursList occursListB = new OccursList();
				//575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8336");
				occursListB.putParam("OOUkey", xJcicZ574.getUkey());
				occursListB.putParam("OOCustId", xJcicZ574.getCustId());
				occursListB.putParam("OORcDate", xJcicZ574.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ574.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ574.getSubmitKey(),titaVo));
				occursListB.putParam("OOTranKey", xJcicZ574.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ574.getOutJcicTxtDate();
				if (iOutJcicTxtDate ==0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				}else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				//按鈕控制
				JcicZ574 rJcicZ574 = new JcicZ574();
				JcicZ574Id rJcicZ574Id = new JcicZ574Id();
				rJcicZ574Id.setApplyDate(xJcicZ574.getApplyDate());
				rJcicZ574Id.setCustId(xJcicZ574.getCustId());
				rJcicZ574Id.setSubmitKey(xJcicZ574.getSubmitKey());
				rJcicZ574 = iJcicZ574Service.findById(rJcicZ574Id, titaVo);
				//已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ574.getOutJcicTxtDate()==0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				}else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}
				
				//歷程按鈕控制
				Slice<JcicZ574Log> iJcicZ574Log = null;
				iJcicZ574Log = iJcicZ574LogService.ukeyEq(xJcicZ574.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ574Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				}else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				//固定回傳
				occursListB.putParam("OOMaxMainCode", "");
			    occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOBankId", "");
				occursListB.putParam("OOBankIdX", "");
				occursListB.putParam("OOClosedDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				
				this.totaVo.addOccursList(occursListB);
			}
		}
	}
	public void dealZ575(String custId ,int index,int limit,TitaVo titaVo) throws LogicException {
        //固定內容起始
		OccursList occursListA = new OccursList();
		occursListA.putParam("OOStatusFg", "0");
		occursListA.putParam("OOHistoryFg", "0");
		occursListA.putParam("OODeleteFg", "0");
		occursListA.putParam("OOChainTxCd", "L8337");
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
		//固定回傳
		occursListA.putParam("OOMaxMainCode", "");
	    occursListA.putParam("OOAccount", "");
		occursListA.putParam("OOPayDate", "");
		occursListA.putParam("OOChangePayDate", "");
		occursListA.putParam("OOClosedDate", "");
		occursListA.putParam("OOClaimDate", "");
		occursListA.putParam("OOCourtCode", "");
		occursListA.putParam("OOCaseStatus", "");	
		this.totaVo.addOccursList(occursListA);
		//固定內容結束
		
		//若有資料則以下處理回傳
		Slice<JcicZ575> sJcicZ575 = null;
		sJcicZ575 = iJcicZ575Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ575 != null) {
			for (JcicZ575 xJcicZ575:sJcicZ575) {
				OccursList occursListB = new OccursList();
				//575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8337");
				occursListB.putParam("OOUkey", xJcicZ575.getUkey());
				occursListB.putParam("OOCustId", xJcicZ575.getCustId());
				occursListB.putParam("OORcDate", xJcicZ575.getApplyDate());
				occursListB.putParam("OOSubmitKey", xJcicZ575.getSubmitKey());
				occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ575.getSubmitKey(),titaVo));
				occursListB.putParam("OOBankId", xJcicZ575.getBankId());
				occursListB.putParam("OOBankIdX", dealBankName(xJcicZ575.getBankId(),titaVo));
				occursListB.putParam("OOTranKey", xJcicZ575.getTranKey());
				int iOutJcicTxtDate = 0;
				iOutJcicTxtDate = xJcicZ575.getOutJcicTxtDate();
				if (iOutJcicTxtDate ==0) {
					occursListB.putParam("OOOutJcicTxtDate", "");
				}else {
					occursListB.putParam("OOOutJcicTxtDate", iOutJcicTxtDate);
				}
				//按鈕控制
				JcicZ575 rJcicZ575 = new JcicZ575();
				JcicZ575Id rJcicZ575Id = new JcicZ575Id();
				rJcicZ575Id.setApplyDate(xJcicZ575.getApplyDate());
				rJcicZ575Id.setBankId(xJcicZ575.getBankId());
				rJcicZ575Id.setCustId(xJcicZ575.getCustId());
				rJcicZ575Id.setSubmitKey(xJcicZ575.getSubmitKey());
				rJcicZ575 = iJcicZ575Service.findById(rJcicZ575Id, titaVo);
				//已報送檔案(OutJcicDate!=0)才可做異動，否則只能刪除
				if (rJcicZ575.getOutJcicTxtDate()==0) {
					occursListB.putParam("OODeleteFg", "1");
					occursListB.putParam("OOStatusFg", "2");
				}else {
					occursListB.putParam("OODeleteFg", "0");
					occursListB.putParam("OOStatusFg", "1");
				}
				
				//歷程按鈕控制
				Slice<JcicZ575Log> iJcicZ575Log = null;
				iJcicZ575Log = iJcicZ575LogService.ukeyEq(xJcicZ575.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ575Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				}else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				//固定回傳
				occursListB.putParam("OOMaxMainCode", "");
			    occursListB.putParam("OOAccount", "");
				occursListB.putParam("OOClosedDate", "");
				occursListB.putParam("OOChangePayDate", "");
				occursListB.putParam("OOPayDate", "");
				occursListB.putParam("OOClaimDate", "");
				occursListB.putParam("OOCourtCode", "");
				occursListB.putParam("OOCaseStatus", "");
				
				this.totaVo.addOccursList(occursListB);
			}
		}
	}
	
	public String dealBankName(String BankId,TitaVo titaVo) throws LogicException {
		CdCode tCdCode = new CdCode();
		tCdCode=iCdCodeService.getItemFirst(8, "JcicBankCode", BankId,titaVo);
		String JcicBankName="";//80碼長度
		if(tCdCode!=null) {
			JcicBankName=tCdCode.getItem();
		}
		return JcicBankName;
	}
}