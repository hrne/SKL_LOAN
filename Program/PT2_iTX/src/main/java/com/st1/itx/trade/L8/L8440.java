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
import com.st1.itx.db.service.springjpa.cm.L8440ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8440")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8440 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L8440ServiceImpl iL8440;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8440 ");
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
		String a = "JcicZ";
		String b = "JcicZ0";
		String c = "";
		List<Map<String, String>> sL8440 = new ArrayList<Map<String, String>>();
		for (int x : intArray) {
			if (x < 100) {
				c = b + x;
			} else {
				c = a + x;
			}
			this.info("ciArray     = " + c);

			try {
				// 今日轉出有JCIC日期
				sL8440 = iL8440.FindData(titaVo, c);
			} catch (Exception e) {
				this.info("L8440 have JcicDate ===");
				throw new LogicException(titaVo, "E2003", "查無可轉出資料");
			}
			for(Map<String, String> isL8440 : sL8440) {
				String toDay = isL8440.get("F0");
				String notToDay = isL8440.get("F1");

				OccursList = new OccursList();	
				OccursList.putParam("OOTranCode", c);
				String minCode = c.substring(5,8);
//				OccursList.putParam("OOChainTxCd", "L8404");
//				OccursList.putParam("OOCtxCd", "041");
				switch (minCode) {
				case "040":
					OccursList.putParam("OOTranCode", "JcicZ040-前置協商受理申請暨請求回報債權通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8403");
					break;
				case "041":
					OccursList.putParam("OOTranCode", "JcicZ041-協商開始暨停催通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8404");
					break;
				case "042":
					OccursList.putParam("OOTranCode", "JcicZ042-回報無擔保債權金額資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8405");
					break;
				case "043":
					OccursList.putParam("OOTranCode", "JcicZ043-回報有擔保債權金額資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8406");
					break;
				case "044":
					OccursList.putParam("OOTranCode", "JcicZ044-請求同意債務清償方案通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8407");
					break;
				case "045":
					OccursList.putParam("OOTranCode", "JcicZ045-回報是否同意債務清償方案資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8408");
					break;
				case "046":
					OccursList.putParam("OOTranCode", "JcicZ046-結案通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8409");
					break;
				case "047":
					OccursList.putParam("OOTranCode", "JcicZ047-金融機構無擔保債務協議資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8410");
					break;
				case "048":
					OccursList.putParam("OOTranCode", "JcicZ048-債務人基本資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8411");
					break;
				case "049":
					OccursList.putParam("OOTranCode", "JcicZ049-債務清償方案法院認可資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8412");
					break;
				case "050":
					OccursList.putParam("OOTranCode", "JcicZ050-債務人繳款資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8413");
					break;
				case "051":
					OccursList.putParam("OOTranCode", "JcicZ051-延期繳款（喘息期）資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8414");
					break;
				case "052":
					OccursList.putParam("OOTranCode", "JcicZ052-前置協商相關資料報送例外處理");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8415");
					break;
				case "053":
					OccursList.putParam("OOTranCode", "JcicZ053-同意報送例外處裡檔案");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8416");
					break;
				case "054":
					OccursList.putParam("OOTranCode", "JcicZ054-單獨全數受清償資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8417");
					break;
				case "055":
					OccursList.putParam("OOTranCode", "JcicZ055-更生案件通報資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8418");
					break;
				case "056":
					OccursList.putParam("OOTranCode", "JcicZ056-清算案件通報資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8419");
					break;
				case "060":
					OccursList.putParam("OOTranCode", "JcicZ060-前置協商受理變更還款條件申請暨請求回報剩餘債權通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8420");
					break;
				case "061":
					OccursList.putParam("OOTranCode", "JcicZ061-回報協商剩餘債權金額資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8421");
					break;
				case "062":
					OccursList.putParam("OOTranCode", "JcicZ062-金融機構無擔保債務變更還款條件協議資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8422");
					break;
				case "063":
					OccursList.putParam("OOTranCode", "JcicZ063-變更還款方案結案通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8423");
					break;
				case "440":
					OccursList.putParam("OOTranCode", "JcicZ440-前置調解受理申請暨請求回報債權通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8424");
					break;
				case "442":
					OccursList.putParam("OOTranCode", "JcicZ442-前置調解回報無擔保債權金額資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8425");
					break;
				case "443":
					OccursList.putParam("OOTranCode", "JcicZ443-前置調解回報有擔保債權金額資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8426");
					break;
				case "444":
					OccursList.putParam("OOTranCode", "JcicZ444-前置調解債務人基本資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8427");
					break;
				case "446":
					OccursList.putParam("OOTranCode", "JcicZ446-前置調解結案通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8428");
					break;
				case "447":
					OccursList.putParam("OOTranCode", "JcicZ447-前置調解金融機構無擔保債務協議資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8429");
					break;
				case "448":
					OccursList.putParam("OOTranCode", "JcicZ448-前置調解無擔保債務分配資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8430");
					break;
				case "450":
					OccursList.putParam("OOTranCode", "JcicZ450-前置調解債務人繳款資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8431");
					break;
				case "451":
					OccursList.putParam("OOTranCode", "JcicZ451-前置調解延期繳款資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8432");
					break;
				case "454":
					OccursList.putParam("OOTranCode", "JcicZ454-前置調解單獨全數受清償資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8433");
					break;
				case "570":
					OccursList.putParam("OOTranCode", "JcicZ570-受理更生款項統一收付通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8434");
					break;
				case "571":
					OccursList.putParam("OOTranCode", "JcicZ571-更生款項統一收付回報債權資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8435");
					break;
				case "572":
					OccursList.putParam("OOTranCode", "JcicZ572-更生款項統一收款及撥付款項分配表資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8436");
					break;
				case "573":
					OccursList.putParam("OOTranCode", "JcicZ573-更生債務人繳款資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8437");
					break;
				case "574":
					OccursList.putParam("OOTranCode", "JcicZ574-更生款項統一收付結案通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8438");
					break;
				case "575":
					OccursList.putParam("OOTranCode", "JcicZ575-債權金額異動通知資料");
					OccursList.putParam("OOCtxCd", minCode);
					OccursList.putParam("OOChainTxCd", "L8439");
					break;
				}
//				if(!"0".equals(isL8440.get("F0"))) {
					OccursList.putParam("OOToDateReport",toDay);
//				}else {
					OccursList.putParam("OONotRepoet",notToDay);
//				}
//				OccursList.putParam("OOminCode",minCode);
				
				
				this.totaVo.addOccursList(OccursList);
			}

			
		}
		this.addList(this.totaVo);
		return this.sendList();

	}

}