package com.st1.itx.trade.L6;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxInquiry;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxInquiryService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L6101Excel")
@Scope("prototype")
public class L6101Excel extends MakeReport {

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	TxInquiryService sTxInquiryService;
	
	@Autowired
	TxTellerService sTxTellerService;
	
	@Autowired
	TxTranCodeService sTxTranCodeService;
	
	
	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L6101", "結清戶滿五年查詢清單", "L6101" + "_" + "結清戶滿五年查詢清單");
		printExcelHeader();
		
		int Caldy = Integer.parseInt(titaVo.getCalDy())+19110000;
		
		Slice<TxInquiry> sTxInquiry = sTxInquiryService.findImportFg(Caldy,Caldy, "1",0,9999999, 0,Integer.MAX_VALUE, titaVo);

		List<TxInquiry> iTxInquiry = sTxInquiry== null ? null : sTxInquiry.getContent();

		TempVo tTempVo = new TempVo();
		int rowCursor = 2;
		
		if (iTxInquiry != null) {
			
			for ( TxInquiry tTxInquiry : iTxInquiry) {
				
				String reason = "";
				
				tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tTxInquiry.getTranData());
				
				if(tTempVo.get("TxReason") ==null || tTempVo.get("TxReason").isEmpty()) {
					continue;
				}
				
				String tranItem =findTranItem(tTempVo.get("TXCD"),titaVo);
				
				//交易代號
				makeExcel.setValue(rowCursor, 1, tTempVo.get("TXCD")+" "+tranItem);
				
				//查詢理由
				reason = tTempVo.get("TxReason");
				makeExcel.setValue(rowCursor, 2, reason);
				
				//經辦
				makeExcel.setValue(rowCursor, 3, tTempVo.get("TLRNO")+" "+tTempVo.get("EMPNM"));
					
				//交易日期
				String date = tTempVo.get("CALDY");
				if(date.length()>0 && date!=null && !date.isEmpty()) {
					date = showDate(date,1);
				}
				makeExcel.setValue(rowCursor, 4, date);	
	
				//交易時間
				String time = tTempVo.get("CALTM");
				time = time.substring(0, 2)+":"+time.substring(2, 4)+":"+time.substring(4, 6);
				makeExcel.setValue(rowCursor, 5, time);
					
					
				rowCursor++;
			}
	
		}
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "結清戶滿五年查詢清單完成", titaVo);
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}
	

	
//	private String padStart(String temp, int len, String tran) {
//		if (temp.length() < len) {
//			for (int i = temp.length(); i < len; i++) {
//				temp = tran + temp;
//			}
//		}
//		return temp;
//	}
	private String showDate(String date, int iType) {
//		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
			return " ";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
//		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);

		if (rocdatex.length() == 7) {
			return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
		} else {
			return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);

		}

	}
	
	private String findTranItem(String iTranCode,TitaVo titaVo) {
		String tranitem = "";
		
		TxTranCode sTxTranCode = sTxTranCodeService.findById(iTranCode, titaVo);
		
		if(sTxTranCode!=null) {
			tranitem = sTxTranCode.getTranItem();
		}
		
		return tranitem;
	}
	private void printExcelHeader() throws LogicException {
		makeExcel.setValue(1, 1, "交易代號");
		makeExcel.setWidth(1, 40);
		
		makeExcel.setValue(1, 2, "查詢理由");
		makeExcel.setWidth(2, 40);
		
		makeExcel.setValue(1, 3, "經辦");
		makeExcel.setWidth(3, 20);
		
		makeExcel.setValue(1, 4, "交易日期");
		makeExcel.setWidth(4, 18);
		
		
		makeExcel.setValue(1, 5, "交易時間");
		makeExcel.setWidth(5, 18);
		
		
	

		
		
	}

}
