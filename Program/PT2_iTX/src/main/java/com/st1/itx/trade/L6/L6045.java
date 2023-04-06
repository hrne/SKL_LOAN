package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxInquiry;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxInquiryService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.TxTranCodeService;

@Service("L6045")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L6045 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	TxInquiryService sTxInquiryService;
	@Autowired
	public TxTranCodeService txTranCodeService;
	@Autowired
	public CdEmpService cdEmpService;
	
	@Autowired
	CustMainService sCustMainService;
	
	@Autowired
	public TxTellerService txTellerService;
	
	@Autowired
	SendRsp iSendRsp;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6045 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 64 * 200 = 12,800

		int iTxDAteS = Integer.parseInt(titaVo.getParam("TxDateS"))+19110000;
		int iTxDAteE = Integer.parseInt(titaVo.getParam("TxDateE"))+19110000;

		String ixxCustNo = titaVo.getTlrNo();
		TxTeller itxTell = null;
		itxTell = txTellerService.findById(ixxCustNo, titaVo);
		
		this.info("itxTell   =" + itxTell.getAllowFg());
		
		int iCustNoSt = Integer.parseInt(titaVo.getParam("CustNo"));
		int iCustNoEd = 0;
		if(iCustNoSt>0) {
			iCustNoEd = iCustNoSt;
		} else {
			iCustNoEd = 9999999;
		}
		Slice<TxInquiry> sTxInquiry = null;
		
		sTxInquiry = sTxInquiryService.findImportFg(iTxDAteS, iTxDAteE, "1",iCustNoSt,iCustNoEd, this.index, this.limit, titaVo);

		List<TxInquiry> iTxInquiry = sTxInquiry== null ? null : sTxInquiry.getContent();

		TempVo tTempVo = new TempVo();
		TxTranCode tTxTranCode = null;
		
		// 主管授權
		if (!titaVo.getHsupCode().equals("1")) {
			iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
		}
		
		if (iTxInquiry != null) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOAllowFg",itxTell.getAllowFg());
			for (TxInquiry tTxInquiry : iTxInquiry) {
				
				String tranItem = "";
				tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tTxInquiry.getTranData());

				if(tTempVo.get("TxReason")==null || tTempVo.get("TxReason").isEmpty()) {
					continue;
				}
				
				occursList.putParam("OOTranNo", tTempVo.get("TXCD"));
				tTxTranCode = txTranCodeService.findById(tTempVo.get("TXCD"), titaVo);
				if(tTxTranCode!=null) {
					tranItem = tTxTranCode.getTranItem();
				}
				occursList.putParam("OOTranItem", tranItem);
				
				occursList.putParam("OOReason", tTempVo.get("TxReason"));
				occursList.putParam("OOTlrNo", tTempVo.get("TLRNO"));	
				
				CdEmp tCdEmp = cdEmpService.findById(tTempVo.get("TLRNO"), titaVo);
				
				if(tCdEmp!=null) {
					occursList.putParam("OOTlrItem", tCdEmp.getFullname());
				} else {
					occursList.putParam("OOTlrItem", "");
				}
				
				
				String date = tTempVo.get("CALDY");
				date = date.substring(0, 3) + "/" + date.substring(3, 5) + "/" + date.substring(5, 7);
				
				String time = tTempVo.get("CALTM");
				time = time.substring(0, 2)+":"+time.substring(2, 4)+":"+time.substring(4, 6);
				
				occursList.putParam("OOCaldy", date+" "+time);
				
				// 放入戶號與戶名
				int custNo = tTxInquiry.getCustNo();
				occursList.putParam("OOCustNo", custNo);
				
				CustMain tCustMain = sCustMainService.custNoFirst(custNo, custNo, titaVo);
				
				if (tCustMain != null)
				{
					occursList.putParam("OOCustName", tCustMain.getCustName());
				} else 
				{
					this.warn("CustNo " + custNo + " is not found in CustMain !?");
				}
		
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			
		} else {
			throw new LogicException("E0001", "");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sTxInquiry != null && sTxInquiry.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}