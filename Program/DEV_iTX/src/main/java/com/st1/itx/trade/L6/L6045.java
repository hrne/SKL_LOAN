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
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxRecordService;
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
	TxRecordService sTxRecordService;
	@Autowired
	public TxTranCodeService txTranCodeService;
	
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
		
		String iBrno = titaVo.getBrno();
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));
		Slice<TxRecord> sTxRecord = null;
		
		if(iCustNo == 0) {
			sTxRecord = sTxRecordService.findByCalDate(iTxDAteS, iTxDAteE, iBrno, "1", this.index, this.limit, titaVo);
		} else {
			sTxRecord = sTxRecordService.findByCustNo(iTxDAteS, iTxDAteE, iBrno, iCustNo, "1", this.index, this.limit, titaVo);
		}
	

		List<TxRecord> iTxRecord = sTxRecord== null ? null : sTxRecord.getContent();

		TempVo tTempVo = new TempVo();
		TxTranCode tTxTranCode = null;
		if (iTxRecord != null) {
			
			for (TxRecord tTxRecord : iTxRecord) {
				OccursList occursList = new OccursList();
				String tranItem = "";
				tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tTxRecord.getTranData());

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
				occursList.putParam("OOTlrItem", tTempVo.get("EMPNM"));
				
				String date = tTempVo.get("CALDY");
				date = date.substring(0, 3) + "/" + date.substring(3, 5) + "/" + date.substring(5, 7);
				
				String time = tTempVo.get("CALTM");
				time = time.substring(0, 2)+":"+time.substring(2, 4)+":"+time.substring(4, 6);
				
				occursList.putParam("OOCaldy", date+" "+time);
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			
		} else {
			throw new LogicException("E0001", "");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sTxRecord != null && sTxRecord.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}