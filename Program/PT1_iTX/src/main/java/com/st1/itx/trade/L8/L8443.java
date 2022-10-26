package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JcicReFileService;
import com.st1.itx.db.service.springjpa.cm.L8443ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8443")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8443 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public JcicReFileService sJcicReFileService;
	
	@Autowired
	public L8443ServiceImpl iL8443;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8443 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();
		// new OccursList
		OccursList OccursList = new OccursList();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		int intArray[] = { 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 60, 61, 62, 63, 440, 442,
				443, 444, 446, 447, 448, 450, 451, 454, 570, 571, 572, 573, 574, 575 };

		List<Map<String, String>> sL8443 = new ArrayList<Map<String, String>>();
		
		for (int x : intArray) {
			
			String f = Integer.toString(x);
			this.info("f   = " + f);
			String txDate ="";
			try {
				sL8443 = iL8443.Find(titaVo, f);
				for(Map<String, String> isL8443 : sL8443) {
					for(int j = 0; j<isL8443.size(); j++) {
						txDate =  isL8443.get("F0");
						if(j>1) {
						txDate += ","+txDate;
						}
					}
				}
			} catch (Exception e) {
				this.info("L8440 have JcicDate ===");
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			
			
			String a = "JcicZ";
			String b = "JcicZ0";
			String c = Integer.toString(x);
			if (x < 100) {
				c = b + x;
			} else {
				c = a + x;
			}
			String minCode = c.substring(5,8);
			this.info("c   = " + c);
			try {
				sL8443 = iL8443.FindData(titaVo, c , txDate);
			} catch (Exception e) {
				this.info("L8440 have JcicDate ===");
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			
			for(Map<String, String> isL8443 : sL8443) {
				String iNotReportCount = isL8443.get("F0");
				String iReportDate = isL8443.get("F1");
				String iReportId = isL8443.get("F2");
				int iiReportDate = Integer.valueOf(iReportDate);
				if( iiReportDate > 19110000 ) {
					iiReportDate = iiReportDate-19110000;
				}
				
				OccursList = new OccursList();	
				
	
				switch (minCode) {
				case "040":
					OccursList.putParam("OOSubmitkey", "JcicZ040-前置協商受理申請暨請求回報債權通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);
					OccursList.putParam("OOReportId", iReportId);
					
					break;
				case "041":
					OccursList.putParam("OOSubmitkey", "JcicZ041-協商開始暨停催通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "042":
					OccursList.putParam("OOSubmitkey", "JcicZ042-回報無擔保債權金額資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "043":
					OccursList.putParam("OOSubmitkey", "JcicZ043-回報有擔保債權金額資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "044":
					OccursList.putParam("OOSubmitkey", "JcicZ044-請求同意債務清償方案通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "045":
					OccursList.putParam("OOSubmitkey", "JcicZ045-回報是否同意債務清償方案資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "046":
					OccursList.putParam("OOSubmitkey", "JcicZ046-結案通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "047":
					OccursList.putParam("OOSubmitkey", "JcicZ047-金融機構無擔保債務協議資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "048":
					OccursList.putParam("OOSubmitkey", "JcicZ048-債務人基本資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "049":
					OccursList.putParam("OOSubmitkey", "JcicZ049-債務清償方案法院認可資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "050":
					OccursList.putParam("OOSubmitkey", "JcicZ050-債務人繳款資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "051":
					OccursList.putParam("OOSubmitkey", "JcicZ051-延期繳款（喘息期）資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "052":
					OccursList.putParam("OOSubmitkey", "JcicZ052-前置協商相關資料報送例外處理");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "053":
					OccursList.putParam("OOSubmitkey", "JcicZ053-同意報送例外處裡檔案");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "054":
					OccursList.putParam("OOSubmitkey", "JcicZ054-單獨全數受清償資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "055":
					OccursList.putParam("OOSubmitkey", "JcicZ055-更生案件通報資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "056":
					OccursList.putParam("OOSubmitkey", "JcicZ056-清算案件通報資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "060":
					OccursList.putParam("OOSubmitkey", "JcicZ060-前置協商受理變更還款條件申請暨請求回報剩餘債權通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "061":
					OccursList.putParam("OOSubmitkey", "JcicZ061-回報協商剩餘債權金額資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "062":
					OccursList.putParam("OOSubmitkey", "JcicZ062-金融機構無擔保債務變更還款條件協議資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "063":
					OccursList.putParam("OOSubmitkey", "JcicZ063-變更還款方案結案通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "440":
					OccursList.putParam("OOSubmitkey", "JcicZ440-前置調解受理申請暨請求回報債權通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "442":
					OccursList.putParam("OOSubmitkey", "JcicZ442-前置調解回報無擔保債權金額資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "443":
					OccursList.putParam("OOSubmitkey", "JcicZ443-前置調解回報有擔保債權金額資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "444":
					OccursList.putParam("OOSubmitkey", "JcicZ444-前置調解債務人基本資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "446":
					OccursList.putParam("OOSubmitkey", "JcicZ446-前置調解結案通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "447":
					OccursList.putParam("OOSubmitkey", "JcicZ447-前置調解金融機構無擔保債務協議資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "448":
					OccursList.putParam("OOSubmitkey", "JcicZ448-前置調解無擔保債務分配資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "450":
					OccursList.putParam("OOSubmitkey", "JcicZ450-前置調解債務人繳款資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "451":
					OccursList.putParam("OOSubmitkey", "JcicZ451-前置調解延期繳款資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "454":
					OccursList.putParam("OOSubmitkey", "JcicZ454-前置調解單獨全數受清償資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "570":
					OccursList.putParam("OOSubmitkey", "JcicZ570-受理更生款項統一收付通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "571":
					OccursList.putParam("OOSubmitkey", "JcicZ571-更生款項統一收付回報債權資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "572":
					OccursList.putParam("OOSubmitkey", "JcicZ572-更生款項統一收款及撥付款項分配表資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "573":
					OccursList.putParam("OOSubmitkey", "JcicZ573-更生債務人繳款資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "574":
					OccursList.putParam("OOSubmitkey", "JcicZ574-更生款項統一收付結案通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				case "575":
					OccursList.putParam("OOSubmitkey", "JcicZ575-債權金額異動通知資料");
					OccursList.putParam("OOReportDate", iiReportDate);
					OccursList.putParam("OONotReportCount", iNotReportCount);

					break;
				}
					
				this.totaVo.addOccursList(OccursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}