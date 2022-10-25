package com.st1.itx.trade.L8;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicReFile;
import com.st1.itx.db.domain.JcicReFileId;
import com.st1.itx.db.service.JcicReFileService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.http.WebClient;

@Service("L8441")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8441 extends TradeBuffer {

//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active L8441 ");
//		this.totaVo.init(titaVo);
//
//		MySpring.newTask("L8441Batch", this.txBuffer, titaVo);
//		
//		this.addList(this.totaVo);
//		return this.sendList();
//	}

	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public WebClient webClient;
	
	@Autowired
	public JcicReFileService sJcicReFileService;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8441Batch");
		this.totaVo.init(titaVo);
		this.info("titaVo   = " + titaVo);
		String iSubmitkey = "";
		int sTotal = 0;
		int sCorrect = 0;
		int sMistakeCount = 0;
		int sJcicDate = 0;	
		
//		吃檔
//		String filePath1 = "D:\\temp\\test\\火險\\Test\\Return\\1)R-10904LNM01P.txt";
		String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();
		ArrayList<String> dataLineList1 = new ArrayList<>();

//		if (filePath1 != null) {
//			String checkMsg = "聯真回寫筆數已上傳。";
//			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L8442",
//					titaVo.getTlrNo(), checkMsg, titaVo);
//		}
		
		try {
			dataLineList1 = fileCom.intputTxt(filePath1, "big5");
		} catch (IOException e) {
			this.info("L8441Batch(" + filePath1 + ") : " + e.getMessage());
		}
		this.info("dataLineList1    = " + dataLineList1);
		for (String filename : dataLineList1) {
			if (filename.contains("畫面編號:")) {
			int Xx =	filename.length();
			this.info("XX    = "  + Xx); // 050 28
			}
			if (filename.contains("畫面編號:")) {
				iSubmitkey = filename.substring(7, 10).trim();
			}
			if (filename.contains("本次檢核共檢視過")) {
//				int iTotal = parse.stringToInteger(filename.substring(8, 17).trim());
				String ixTotal = filename.substring(8, 18).trim();
				String ixTotal2 ="";
				for(int i=0; i < ixTotal.length(); i++) {
					if(ixTotal.charAt(i)>=48 && ixTotal.charAt(i)<=57) {
						ixTotal2 += ixTotal.charAt(i);
					}
				}	
				sTotal = parse.stringToInteger(ixTotal2);
				
//				int iCorrect = parse.stringToInteger(filename.substring(25, 35).trim());
				String ixCorrect = filename.substring(26, 36).trim();
				String ixCorrect2 ="";
				for(int i=0; i < ixCorrect.length(); i++) {
					if(ixCorrect.charAt(i)>=48 && ixCorrect.charAt(i)<=57) {
						ixCorrect2 += ixCorrect.charAt(i);
					}
				}	
				sCorrect = parse.stringToInteger(ixCorrect2);
				
//				int iMistakeCount = parse.stringToInteger(filename.substring(39, 49).trim());
				String ixMistakeCount = filename.substring(40, 50).trim();
				String ixMistakeCount2 ="";
				for(int i=0; i < ixMistakeCount.length(); i++) {
					if(ixMistakeCount.charAt(i)>=48 && ixMistakeCount.charAt(i)<=57) {
						ixMistakeCount2 += ixMistakeCount.charAt(i);
					}
				}	
				sMistakeCount = parse.stringToInteger(ixMistakeCount2);;
				
				this.info("sMistakeCount    =  " + sMistakeCount);
				
			} 
			if (filename.contains("報送日期:")) {
				int iJcicDate = parse.stringToInteger(filename.substring(6, 13).trim());
				sJcicDate = iJcicDate;
			}
		}
		this.info("iSubmitkey   = " + iSubmitkey);
		this.info("sJcicDate    = " + sJcicDate);
		this.info("sTotal       = " + sTotal);
		this.info("sCorrect     = " + sCorrect);
		this.info("sMistakeCount  = " + sMistakeCount);
		
		JcicReFileId iJcicReFileid = new JcicReFileId();
		JcicReFile iJcicReFile = new JcicReFile();
		JcicReFile chJcicReFile = new JcicReFile();
		iJcicReFileid.setSubmitKey(iSubmitkey);
		iJcicReFileid.setJcicDate(sJcicDate);
		chJcicReFile = sJcicReFileService.findById(iJcicReFileid, titaVo);
		if (chJcicReFile != null) {
			throw new LogicException("E0002", "已有相同資料");
		}
		iJcicReFile.setJcicReFileId(iJcicReFileid);
		iJcicReFile.setSubmitKey(iSubmitkey);
		iJcicReFile.setJcicDate(sJcicDate);
		iJcicReFile.setReportTotal(sTotal);
		iJcicReFile.setCorrectCount(sCorrect);
		iJcicReFile.setMistakeCount(sMistakeCount);
		iJcicReFile.setNoBackFileCount(0);
		iJcicReFile.setNoBackFileDate(0);
		try {
			sJcicReFileService.insert(iJcicReFile, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "聯徵上傳檔案");
		}
		
		this.totaVo.putParam("Submitkey", iSubmitkey);
		this.totaVo.putParam("JcicDate", sJcicDate);
		this.totaVo.putParam("ReportTotal", sTotal);
		this.totaVo.putParam("CorrectCount", sCorrect);
		this.totaVo.putParam("MistakeCount", sMistakeCount);
		this.info("sMistakeCount    =  " + sMistakeCount);
		
		String checkMsg = "聯真回寫筆數已上傳。";
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L8442",
				titaVo.getTlrNo(), checkMsg, titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}