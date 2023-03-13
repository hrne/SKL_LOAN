package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;

import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component("L9135Report2")
@Scope("prototype")

public class L9135Report2 extends MakeFile {

	@Autowired
	private Parse parse;

	String iAcDate = "0";

	public List<Map<String, String>> exec(TitaVo titaVo, List<Map<String, String>> l9135Result, int iAcDate)
			throws LogicException {
		this.info("L9135Report2");
		
		BigDecimal amtDb;
		BigDecimal amtCr;
		BigDecimal amt;

		this.iAcDate = String.valueOf(iAcDate);
		List<Map<String, String>> l9135List = l9135Result;

		if (l9135List.size() > 0) {

			
			int reportDate = titaVo.getEntDyI() + 19110000;
			String brno = titaVo.getBrno();
			String txcd = "L9135";
			String fileItem = "銀行存款媒體明細表";
			String fileName = "L9135-銀行存款媒體明細表";
//			String exportFile = "L9135-銀行存款媒體明細表（總帳）.txt";


			ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
					.setRptItem(fileItem).build();

			
			
			
			this.open(titaVo, reportVo,fileName, 2);

			for (Map<String, String> r : l9135List) {

				amtDb =  parse.stringToBigDecimal(String.format(r.get("DbTxAmt")));
				amtCr =  parse.stringToBigDecimal(String.format(r.get("CrTxAmt")));
				DecimalFormat df = new DecimalFormat("0.00#");
				BigDecimal iamtDb = new BigDecimal(df.format(amtDb));
				BigDecimal iamtCr =new BigDecimal(df.format(amtCr));
				amt =  iamtCr.add(iamtDb);
				BigDecimal iamt =new BigDecimal(df.format(amt));
				String iDate = parse.IntegerToString(iAcDate, 7);
				this.info("");
				String iAcctItem = r.get("AcctItem");
				if (iAcctItem == null) {
					iAcctItem = "          ";
				}
				String iDbCr = r.get("DbCr");
				if (iDbCr.equals("C")) {
					iDbCr = "3";
				}
				if (iDbCr.equals("D")) {
					iDbCr = "4";
				}
				
				int i=amt.compareTo(BigDecimal.ZERO);
				
				if(i==0) {
					iamt = BigDecimal.ZERO;
				}else {
					iamt = amt;					
				}
				
//				日期(7)
//				帳號(10)
//				借貸別(1)
//				金額(13)
//				傳票號碼(5)
				String iSlipNo = r.get("SlipNo");
				String sti = iamt.toString();
				int stsi = sti.length();
				int std = 13-stsi;
				String ssti ="";
				if(sti.length()<13) {
					for(int ix = std; ix!=0; ix--) {
						  ssti += "0" ;
						  this.info("ssti  = " + ssti);
					}
				}

				String text = iDate  + iAcctItem + iDbCr + ssti+sti + iSlipNo;
				this.info("text   = " + text);
				this.put(text);
			}
		}
		this.close();
		return l9135List;

	}

}
