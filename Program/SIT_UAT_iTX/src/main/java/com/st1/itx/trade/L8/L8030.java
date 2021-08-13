package com.st1.itx.trade.L8;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.db.service.JcicZ062Service;
//import com.st1.itx.db.domain.JcicZ063Log;
import com.st1.itx.db.service.JcicZ063Service;
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
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.db.service.JcicZ451Service;
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
	private static final Logger logger = LoggerFactory.getLogger(L8030.class);
	
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
//	@Autowired
//	public JcicZ063LogService iJcicZ063LogService;
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
	public JcicZ570LogService iJcicZ570LogService;
	@Autowired
	public JcicZ571Service iJcicZ571Service;
	@Autowired
	public JcicZ571LogService iJcicZ571LogService;
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	@Autowired
	public JcicZ572LogService iJcicZ572LogService;
	@Autowired
	public JcicZ573Service iJcicZ573Service;
	@Autowired
	public JcicZ573LogService iJcicZ573LogService;
	@Autowired
	public JcicZ574Service iJcicZ574Service;
	@Autowired
	public JcicZ574LogService iJcicZ574LogService;
	@Autowired
	public JcicZ575Service iJcicZ575Service;
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
//		case"063":
//			dealZ063(iCustId,this.index,this.limit,titaVo);
//			break;
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
	    occursListA.putParam("OOApplyDate", "");
	    occursListA.putParam("OOOutJcicTxtDate", "");
	    occursListA.putParam("OOTranKey", "");
	     
	    //固定回傳
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
//		sJcicZ575 = iJcicZ575Service.custIdEq(custId, this.index, this.limit, titaVo);
		if (sJcicZ575 != null) {
			for (JcicZ575 xJcicZ575:sJcicZ575) {
				OccursList occursListB = new OccursList();
				//575 layout回傳
				occursListB.putParam("OOChainTxCd", "L8337");
//				occursListB.putParam("OOUkey", xJcicZ575.getUkey());
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
//				iJcicZ575Log = iJcicZ575LogService.ukeyEq(xJcicZ575.getUkey(), 0, Integer.MAX_VALUE, titaVo);
				if (iJcicZ575Log == null) {
					occursListB.putParam("OOHistoryFg", "0");
				}else {
					occursListB.putParam("OOHistoryFg", "1");
				}
				//固定回傳
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