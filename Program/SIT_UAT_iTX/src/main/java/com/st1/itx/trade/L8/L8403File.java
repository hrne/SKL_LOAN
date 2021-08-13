package com.st1.itx.trade.L8;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Log;
import com.st1.itx.db.domain.JcicZ570LogId;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.domain.JcicZ571LogId;
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.domain.JcicZ572LogId;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Log;
import com.st1.itx.db.domain.JcicZ573LogId;
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Log;
import com.st1.itx.db.domain.JcicZ574LogId;
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Log;
import com.st1.itx.db.domain.JcicZ575LogId;
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ454;
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
import com.st1.itx.db.service.JcicZ063Service;
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
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;


@Component("L8403File")
@Scope("prototype")

public class L8403File extends MakeFile {
	private static final Logger logger = LoggerFactory.getLogger(L8403File.class);

	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ041Service sJcicZ041Service;
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ045Service sJcicZ045Service;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ049Service sJcicZ049Service;
	@Autowired
	public JcicZ050Service sJcicZ050Service;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ053Service sJcicZ053Service;
	@Autowired
	public JcicZ054Service sJcicZ054Service;
	@Autowired
	public JcicZ055Service sJcicZ055Service;
	@Autowired
	public JcicZ056Service sJcicZ056Service;
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ442Service sJcicZ442Service;
	@Autowired
	public JcicZ443Service sJcicZ443Service; 
	@Autowired
	public JcicZ444Service sJcicZ444Service; 
	@Autowired
	public JcicZ446Service sJcicZ446Service; 
	@Autowired
	public JcicZ447Service sJcicZ447Service; 
	@Autowired
	public JcicZ448Service sJcicZ448Service; 
	@Autowired
	public JcicZ450Service sJcicZ450Service; 
	@Autowired
	public JcicZ451Service sJcicZ451Service; 
	@Autowired
	public JcicZ454Service sJcicZ454Service; 
	@Autowired
	public JcicZ570Service sJcicZ570Service;
	@Autowired
	public JcicZ570LogService sJcicZ570LogService;
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ571LogService sJcicZ571LogService;
	@Autowired
	public JcicZ572Service sJcicZ572Service;
	@Autowired
	public JcicZ572LogService sJcicZ572LogService;
	@Autowired
	public JcicZ573Service sJcicZ573Service;
	@Autowired
	public JcicZ573LogService sJcicZ573LogService;
	@Autowired
	public JcicZ574Service sJcicZ574Service;
	@Autowired
	public JcicZ574LogService sJcicZ574LogService;
	@Autowired
	public JcicZ575Service sJcicZ575Service;
	@Autowired
	public JcicZ575LogService sJcicZ575LogService;

	@Autowired
	DateUtil dDateUtil;
	


	public void exec(TitaVo titaVo) throws LogicException {
		
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iReportDate = titaVo.getParam("ReportDate");
		String iTranCode = StringUtils.leftPad(titaVo.getParam("TranCode"), 3, '0');
		//檔名
		//BBBMMDDS.XXX 金融機構總行代號+月份+日期+次數.檔案類別
		String fileName = iSubmitKey+iReportDate.substring(3)+"."+iTranCode;
		
		int date = Integer.valueOf(titaVo.getEntDy());
		String brno = titaVo.getBrno();
		String fileCode = "L8403";
		String fileItem = "暫定每月產檔";
		
		switch(iTranCode){
		case "040":
			fileCode = "L8403-040";
			fileItem = "前置協商受理申請暨請求回報債權通知資料檔案";
		break;
		case "041":
			fileCode = "L8403-041";
			fileItem = "協商開始暨停催通知資料";
		break;
		case "570":
			fileCode = "L8403-570";
			fileItem = "受理更生款項統一收付通知資料";
		break;
		case "571":
			fileCode = "L8403-571";
			fileItem = "前置調解金融機構無擔保債務協議資料";
		break;
		case "572":
			fileCode = "L8403-572";
			fileItem = "前置調解無擔保債務分配表資料";
		break;
		case "573":
			fileCode = "L8403-573";
			fileItem = "更生債務人繳款資料";
		break;
		case "574":
			fileCode = "L8403-574";
			fileItem = "更生款項統一收付結案通知資料";
		break;
		case "575":
			fileCode = "L8403-575";
			fileItem = "更生債權金額異動通知資料";
		break;
		}
		
		this.open(titaVo, date, brno, fileCode, fileItem, fileName);
		// 用String.format()
			
		//頭筆資料
		//JCIC-DAT-Z040-V01
		String headText = "JCIC-DAT-Z"+iTranCode+"-V01-458     "+iReportDate+"01          02-28225252#1688資訊單位連絡人-王大中，審查單位聯絡人-林小華";
		this.info("第一行="+headText);
		this.put(headText);
		
		int iCount = 0; //總筆數
		
		switch(iTranCode){
			case "040":
				iCount = totalCount("040");
				doZ040File(titaVo);
			break;
			case "041":
				iCount = totalCount("041");
				doZ041File(titaVo);
			break;
			case "042":
				iCount = totalCount("042");
				doZ042File(titaVo);
			break;
			case "043":
				iCount = totalCount("043");
				doZ043File(titaVo);
			break;
			case "044":
				iCount = totalCount("044");
				doZ044File(titaVo);
			break;
			case "045":
				iCount = totalCount("045");
				doZ045File(titaVo);
			break;
			case "046":
				iCount = totalCount("046");
				doZ046File(titaVo);
			break;
			case "047":
				iCount = totalCount("047");
				doZ047File(titaVo);
			break;
			case "048":
				iCount = totalCount("048");
				doZ048File(titaVo);
			break;
			case "049":
				iCount = totalCount("049");
				doZ049File(titaVo);
			break;
			case "050":
				iCount = totalCount("050");
				doZ050File(titaVo);
			break;
			case "051":
				iCount = totalCount("051");
				doZ051File(titaVo);
			break;
			case "052":
				iCount = totalCount("052");
				doZ052File(titaVo);
			break;
			case "053":
				iCount = totalCount("053");
				doZ053File(titaVo);
			break;
			case "054":
				iCount = totalCount("054");
				doZ054File(titaVo);
			break;
			case "055":
				iCount = totalCount("055");
				doZ055File(titaVo);
			break;
			case "056":
				iCount = totalCount("056");
				doZ056File(titaVo);
			break;
			case "060":
				iCount = totalCount("060");
				doZ060File(titaVo);
			break;
			case "061":
				iCount = totalCount("061");
				doZ061File(titaVo);
			break;
			case "062":
				iCount = totalCount("062");
				doZ062File(titaVo);
			break;
			case "063":
				iCount = totalCount("063");
				doZ063File(titaVo);
			break;
			case "440":
				iCount = totalCount("440");
				doZ440File(titaVo);
			break;
			case "442":
				iCount = totalCount("442");
				doZ442File(titaVo);
			break;
			case "443":
				iCount = totalCount("443");
				doZ443File(titaVo);
			break;
			case "444":
				iCount = totalCount("444");
				doZ444File(titaVo);
			break;
			case "446":
				iCount = totalCount("446");
				doZ446File(titaVo);
			break;
			case "447":
				iCount = totalCount("447");
				doZ447File(titaVo);
			break;
			case "448":
				iCount = totalCount("448");
				doZ448File(titaVo);
			break;
			case "450":
				iCount = totalCount("450");
				doZ450File(titaVo);
			break;
			case "451":
				iCount = totalCount("451");
				doZ451File(titaVo);
			break;
			case "454":
				iCount = totalCount("454");
				doZ454File(titaVo);
			break;
			case "570":
				iCount = totalCount("570");
				doZ570File(titaVo);
			break;
			case "571":
				iCount = totalCount("571");
				doZ571File(titaVo);
			break;
			case "572":
				iCount = totalCount("572");
				doZ572File(titaVo);
			break;
			case "573":
				iCount = totalCount("573");
				doZ573File(titaVo);
			break;
			case "574":
				iCount = totalCount("574");
				doZ574File(titaVo);
			break;
			case "575":
				iCount = totalCount("575");
				doZ575File(titaVo);
			break;
		}
		//末筆資料
		String sCount = String.valueOf(iCount);
		String footText = "TRLR"+StringUtils.leftPad(sCount, 8, '0')+StringUtils.leftPad("", 129);
		this.info("最後一行="+footText);
		this.put(footText);
		}
	private int totalCount(String trancode) throws LogicException {
		int iTotalCount = 0;
		switch(trancode) {
			case "040":
				Slice<JcicZ040> xJcicZ040 = sJcicZ040Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ040 iJcicZ040:xJcicZ040) {
					if(iJcicZ040.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "041":
				Slice<JcicZ041> xJcicZ041 = sJcicZ041Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ041 iJcicZ041:xJcicZ041) {
					if(iJcicZ041.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "042":
				Slice<JcicZ042> xJcicZ042 = sJcicZ042Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ042 iJcicZ042:xJcicZ042) {
					if(iJcicZ042.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "043":
				Slice<JcicZ043> xJcicZ043 = sJcicZ043Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ043 iJcicZ043:xJcicZ043) {
					if(iJcicZ043.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "044":
				Slice<JcicZ044> xJcicZ044 = sJcicZ044Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ044 iJcicZ044:xJcicZ044) {
					if(iJcicZ044.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "045":
				Slice<JcicZ045> xJcicZ045 = sJcicZ045Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ045 iJcicZ045:xJcicZ045) {
					if(iJcicZ045.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "046":
				Slice<JcicZ046> xJcicZ046 = sJcicZ046Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ046 iJcicZ046:xJcicZ046) {
					if(iJcicZ046.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "047":
				Slice<JcicZ047> xJcicZ047 = sJcicZ047Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ047 iJcicZ047:xJcicZ047) {
					if(iJcicZ047.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "048":
				Slice<JcicZ048> xJcicZ048 = sJcicZ048Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ048 iJcicZ048:xJcicZ048) {
					if(iJcicZ048.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "049":
				Slice<JcicZ049> xJcicZ049 = sJcicZ049Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ049 iJcicZ049:xJcicZ049) {
					if(iJcicZ049.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "050":
				Slice<JcicZ050> xJcicZ050 = sJcicZ050Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ050 iJcicZ050:xJcicZ050) {
					if(iJcicZ050.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "051":
				Slice<JcicZ051> xJcicZ051 = sJcicZ051Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ051 iJcicZ051:xJcicZ051) {
					if(iJcicZ051.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "052":
				Slice<JcicZ052> xJcicZ052 = sJcicZ052Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ052 iJcicZ052:xJcicZ052) {
					if(iJcicZ052.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "053":
				Slice<JcicZ053> xJcicZ053 = sJcicZ053Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ053 iJcicZ053:xJcicZ053) {
					if(iJcicZ053.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "054":
				Slice<JcicZ054> xJcicZ054 = sJcicZ054Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ054 iJcicZ054:xJcicZ054) {
					if(iJcicZ054.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "055":
				Slice<JcicZ055> xJcicZ055 = sJcicZ055Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ055 iJcicZ055:xJcicZ055) {
					if(iJcicZ055.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "056":
				Slice<JcicZ056> xJcicZ056 = sJcicZ056Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ056 iJcicZ056:xJcicZ056) {
					if(iJcicZ056.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "060":
				Slice<JcicZ060> xJcicZ060 = sJcicZ060Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ060 iJcicZ060:xJcicZ060) {
					if(iJcicZ060.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "061":
				Slice<JcicZ061> xJcicZ061 = sJcicZ061Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ061 iJcicZ061:xJcicZ061) {
					if(iJcicZ061.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "062":
				Slice<JcicZ062> xJcicZ062 = sJcicZ062Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ062 iJcicZ062:xJcicZ062) {
					if(iJcicZ062.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "063":
				Slice<JcicZ063> xJcicZ063 = sJcicZ063Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ063 iJcicZ063:xJcicZ063) {
					if(iJcicZ063.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "440":
				Slice<JcicZ440> xJcicZ440 = sJcicZ440Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ440 iJcicZ440:xJcicZ440) {
					if(iJcicZ440.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "442":
				Slice<JcicZ442> xJcicZ442 = sJcicZ442Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ442 iJcicZ442:xJcicZ442) {
					if(iJcicZ442.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "443":
				Slice<JcicZ443> xJcicZ443 = sJcicZ443Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ443 iJcicZ443:xJcicZ443) {
					if(iJcicZ443.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "444":
				Slice<JcicZ444> xJcicZ444 = sJcicZ444Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ444 iJcicZ444:xJcicZ444) {
					if(iJcicZ444.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "446":
				Slice<JcicZ446> xJcicZ446 = sJcicZ446Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ446 iJcicZ446:xJcicZ446) {
					if(iJcicZ446.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "447":
				Slice<JcicZ447> xJcicZ447 = sJcicZ447Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ447 iJcicZ447:xJcicZ447) {
					if(iJcicZ447.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "448":
				Slice<JcicZ448> xJcicZ448 = sJcicZ448Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ448 iJcicZ448:xJcicZ448) {
					if(iJcicZ448.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "450":
				Slice<JcicZ450> xJcicZ450 = sJcicZ450Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ450 iJcicZ450:xJcicZ450) {
					if(iJcicZ450.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "451":
				Slice<JcicZ451> xJcicZ451 = sJcicZ451Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ451 iJcicZ451:xJcicZ451) {
					if(iJcicZ451.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "454":
				Slice<JcicZ454> xJcicZ454 = sJcicZ454Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ454 iJcicZ454:xJcicZ454) {
					if(iJcicZ454.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "570":
				Slice<JcicZ570> xJcicZ570 = sJcicZ570Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ570 iJcicZ571:xJcicZ570) {
					if(iJcicZ571.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "571":
				Slice<JcicZ571> xJcicZ571 = sJcicZ571Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ571 iJcicZ571:xJcicZ571) {
					if(iJcicZ571.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "572":
				Slice<JcicZ572> xJcicZ572 = sJcicZ572Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ572 iJcicZ572:xJcicZ572) {
					if(iJcicZ572.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "573":
				Slice<JcicZ573> xJcicZ573 = sJcicZ573Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ573 iJcicZ573:xJcicZ573) {
					if(iJcicZ573.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "574":
				Slice<JcicZ574> xJcicZ574 = sJcicZ574Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ574 iJcicZ574:xJcicZ574) {
					if(iJcicZ574.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
			case "575":
				Slice<JcicZ575> xJcicZ575 = sJcicZ575Service.findAll(this.index,this.limit, titaVo);
				for(JcicZ575 iJcicZ575:xJcicZ575) {
					if(iJcicZ575.getOutJcicTxtDate()==0) {
						iTotalCount += 1;
					}
				}
				if (iTotalCount == 0) {
					throw new LogicException(titaVo, "E2003", "查無可轉出資料");
				}
			break;
		}
		this.info("iTotalCount="+iTotalCount);
		return iTotalCount;
	}
	public void doZ040File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ040> rJcicZ040 = null;
		rJcicZ040 = sJcicZ040Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ040 ==null) {
			throw new LogicException(titaVo, "E0001", ""); //資料新建錯誤
		}else {
			for (JcicZ040 sJcicZ040:rJcicZ040) {
				if(sJcicZ040.getOutJcicTxtDate()==0) {
					String iTranKey = sJcicZ040.getTranKey();
					String iSubmitKey = sJcicZ040.getSubmitKey();
					String iCustId = sJcicZ040.getCustId();
					String iRcDate = String.valueOf(sJcicZ040.getRcDate());
					String iRbDate = String.valueOf(sJcicZ040.getRbDate());
					String iApplyType = sJcicZ040.getApplyType();
					String iRefBankId = sJcicZ040.getRefBankId();
					String iNotBankId1 = sJcicZ040.getNotBankId1();
					String iNotBankId2 = sJcicZ040.getNotBankId2();
					String iNotBankId3 = sJcicZ040.getNotBankId3();		
					int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
					String text = "40"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")+StringUtils.leftPad(iRcDate, 7,'0')
									+StringUtils.rightPad("", 5)+StringUtils.leftPad(iRbDate, 7,'0')+StringUtils.rightPad(iApplyType, 1,"")
									+StringUtils.rightPad(iRefBankId, 3,"")+StringUtils.rightPad(iNotBankId1, 3,"0")
									+StringUtils.rightPad(iNotBankId2, 3,"0")+StringUtils.rightPad(iNotBankId3, 3,"0")+StringUtils.rightPad("", 32);
					this.put(text);
					
					//回填JcicDate
					sJcicZ040.setOutJcicTxtDate(iDate);
					try {
						sJcicZ040Service.update(sJcicZ040, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
					}
				}
			}
			
		}
	}
	public void doZ041File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ041> rJcicZ041 = null;
		rJcicZ041 = sJcicZ041Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ041==null) {
			throw new LogicException(titaVo, "E0001", ""); //資料新建錯誤
		}else {
			for (JcicZ041 sJcicZ041:rJcicZ041) {
				if(sJcicZ041.getOutJcicTxtDate()==0) {
					String iTranKey = sJcicZ041.getTranKey();
					String iSubmitKey = sJcicZ041.getSubmitKey();
					String iCustId = sJcicZ041.getCustId();
					String iRcDate = String.valueOf(sJcicZ041.getRcDate());
					String iScDate = String.valueOf(sJcicZ041.getScDate());
					String iNegoStartDate = String.valueOf(sJcicZ041.getNegoStartDate());
					String iNonFinClaimAmt = String.valueOf(sJcicZ041.getNonFinClaimAmt());
					int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
					String text ="41"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
									 +StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)+StringUtils.leftPad(iNegoStartDate,7,'0')
									 +StringUtils.leftPad(iNonFinClaimAmt, 9,'0')+StringUtils.leftPad(iScDate, 7,'0')+StringUtils.rightPad("", 29);
					this.info("text="+text);
					this.put(text);
					
					//回填JcicDate
					sJcicZ041.setOutJcicTxtDate(iDate);
					try {
						sJcicZ041Service.update(sJcicZ041, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
					}
				}
			}
		}
	}
	public void doZ042File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ042> rJcicZ042 = null;
		rJcicZ042 = sJcicZ042Service.findAll(this.index,this.limit,titaVo);
		if(rJcicZ042==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ042 sJcicZ042:rJcicZ042) {
				String iTranKey = sJcicZ042.getTranKey();
				String iSubmitKey = sJcicZ042.getSubmitKey();
				String iCustId = sJcicZ042.getCustId();
				String iRcDate = String.valueOf(sJcicZ042.getRcDate());
				String iMaxMainCode = sJcicZ042.getMaxMainCode();
				String iIsClaims = sJcicZ042.getIsClaims();
				String iGuarLoanCnt = String.valueOf(sJcicZ042.getGuarLoanCnt());
				String iExpLoadAmt = String.valueOf(sJcicZ042.getExpLoanAmt());
				String iCivil323ExpAmt = String.valueOf(sJcicZ042.getCivil323ExpAmt());
				String iReceExpAmt = String.valueOf(sJcicZ042.getReceExpAmt());
				String iCashCardAmt = String.valueOf(sJcicZ042.getCashCardAmt());
				String iCivil323CashAmt = String.valueOf(sJcicZ042.getCivil323CashAmt());
				String iReceCashAmt = String.valueOf(sJcicZ042.getReceCashAmt());
				String iCreditCardAmt = String.valueOf(sJcicZ042.getCreditCardAmt());
				String iCivil323CreditAmt = String.valueOf(sJcicZ042.getCivil323CreditAmt());		
				String iReceCreditAmt = String.valueOf(sJcicZ042.getReceCreditAmt());
				String iReceExpPrin = String.valueOf(sJcicZ042.getReceExpPrin());
				String iReceExpInte = String.valueOf(sJcicZ042.getReceExpInte());
				String iReceExpPena = String.valueOf(sJcicZ042.getReceExpPena());
				String iReceExpOther = String.valueOf(sJcicZ042.getReceExpOther());
				String iCashCardPrin = String.valueOf(sJcicZ042.getCashCardPrin());
				String iCashCardInte = String.valueOf(sJcicZ042.getCashCardInte());
				String iCashCardPena = String.valueOf(sJcicZ042.getCashCardPena());
				String iCashCardOther = String.valueOf(sJcicZ042.getCashCardOther());
				String iCreditCardPrin = String.valueOf(sJcicZ042.getCreditCardPrin());
				String iCreditCardInte = String.valueOf(sJcicZ042.getCreditCardInte());
				String iCreditCardPena = String.valueOf(sJcicZ042.getCreditCardPena());
				String iCreditCardOther = String.valueOf(sJcicZ042.getCreditCardOther());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "42"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							 +StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)+StringUtils.rightPad(iMaxMainCode, 3,"")
							 +StringUtils.rightPad(iIsClaims, 1,"")+StringUtils.leftPad(iGuarLoanCnt, 2,'0')
							 +StringUtils.leftPad(iExpLoadAmt, 9,'0')+StringUtils.leftPad(iCivil323ExpAmt, 9,'0')
							 +StringUtils.leftPad(iReceExpAmt, 9,'0')+StringUtils.leftPad(iCashCardAmt, 9,'0')
							 +StringUtils.leftPad(iCivil323CashAmt, 9,'0')+StringUtils.leftPad(iReceCashAmt, 9,'0')
							 +StringUtils.leftPad(iCreditCardAmt, 9,'0')+StringUtils.leftPad(iCivil323CreditAmt, 9,'0')
							 +StringUtils.leftPad(iReceCreditAmt, 9,'0')+StringUtils.rightPad("", 5)
							 +StringUtils.leftPad(iReceExpPrin, 9,'0')+StringUtils.leftPad(iReceExpInte, 9,'0')+StringUtils.leftPad(iReceExpPena, 9,'0')
							 +StringUtils.leftPad(iReceExpOther, 9,'0')+StringUtils.leftPad(iCashCardPrin, 9,'0')+StringUtils.leftPad(iCashCardInte, 9,'0')
							 +StringUtils.leftPad(iCashCardPena, 9,'0')+StringUtils.leftPad(iCashCardOther, 9,'0')+StringUtils.leftPad(iCreditCardPrin, 9,'0')
							 +StringUtils.leftPad(iCreditCardInte, 9,'0')+StringUtils.leftPad(iCreditCardPena, 9,'0')+StringUtils.leftPad(iCreditCardOther, 9,'0')
							 +StringUtils.rightPad("",72);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ042.setOutJcicTxtDate(iDate);
				try {
					sJcicZ042Service.update(sJcicZ042, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ043File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ043> rJcicZ043 = null;
		rJcicZ043 = sJcicZ043Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ043==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for (JcicZ043 sJcicZ043:rJcicZ043) {
				String iTranKey = sJcicZ043.getTranKey();
				String iSubmitKey = sJcicZ043.getSubmitKey();
				String iCustId = sJcicZ043.getCustId();
				String iRcDate = String.valueOf(sJcicZ043.getRcDate());
				String iMaxMainCode = sJcicZ043.getMaxMainCode();
				String iAccount = sJcicZ043.getAccount();
				String iCollateralType = sJcicZ043.getCollateralType(); //擔保品類別
				String iOriginLoanAmt = String.valueOf(sJcicZ043.getOriginLoanAmt()); //原借款金額
				String iCreditBalance = String.valueOf(sJcicZ043.getCreditBalance()); //授信餘額
				String iPerPeriordAmt = String.valueOf(sJcicZ043.getPerPeriordAmt()); //每期應付金額
				String iLastPayDate = String.valueOf(sJcicZ043.getLastPayDate()); //最後繳息日
				String iLastPayAmt = String.valueOf(sJcicZ043.getLastPayAmt()); //最後繳款金額
				String iOutstandAmt = String.valueOf(sJcicZ043.getOutstandAmt()); //已到期尚未償還金額
				String iRepayPerMonday = String.valueOf(sJcicZ043.getRepayPerMonDay()); //每月應還款日
				String iContractStartYM = String.valueOf(sJcicZ043.getContractStartYM()); //契約起始年月
				String iContractEndYM = String.valueOf(sJcicZ043.getContractEndYM()); //契約截止年月
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "43"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iMaxMainCode,3,"")+StringUtils.rightPad(iAccount, 50,"")
							+StringUtils.rightPad(iCollateralType, 2,"")+StringUtils.leftPad(iOriginLoanAmt, 12,'0')
							+StringUtils.leftPad(iCreditBalance, 12,'0')+StringUtils.leftPad(iPerPeriordAmt, 10,'0')
							+StringUtils.leftPad(iLastPayAmt, 10,'0')+StringUtils.leftPad(iLastPayDate, 7,'0')
							+StringUtils.leftPad(iOutstandAmt, 10,'0')+StringUtils.leftPad(iRepayPerMonday, 2,'0')
							+StringUtils.leftPad(iContractStartYM, 5,'0')+StringUtils.leftPad(iContractEndYM, 5,'0');
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ043.setOutJcicTxtDate(iDate);
				try {
					sJcicZ043Service.update(sJcicZ043, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ044File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ044> rJcicZ044 = null;
		rJcicZ044 = sJcicZ044Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ044==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for (JcicZ044 sJcicZ044:rJcicZ044) {
				String iTranKey = sJcicZ044.getTranKey();
				String iSubmitKey = sJcicZ044.getSubmitKey();
				String iCustId = sJcicZ044.getCustId();
				String iRcDate = String.valueOf(sJcicZ044.getRcDate());
				String iDebtCode = String.valueOf(sJcicZ044.getDebtCode());
				String iNonGageAmt = String.valueOf(sJcicZ044.getNonGageAmt());
				String iPeriod = String.valueOf(sJcicZ044.getPeriod());
				String iRate = String.valueOf(sJcicZ044.getRate());
				String iMonthPayAmt = String.valueOf(sJcicZ044.getMonthPayAmt());
				String iReceYearIncome = String.valueOf(sJcicZ044.getReceYearIncome());
				String iReceYear = String.valueOf(sJcicZ044.getReceYear());
				String iReceYear2Income = String.valueOf(sJcicZ044.getReceYear2Income());
				String iReceYear2 = String.valueOf(sJcicZ044.getReceYear2());
				String iCurrentMonthIncome = String.valueOf(sJcicZ044.getCurrentMonthIncome());
				String iLivingCost = String.valueOf(sJcicZ044.getLivingCost());
				String iCompName = sJcicZ044.getCompName();
				String iCompId = sJcicZ044.getCompId();
				String iCarCnt = String.valueOf(sJcicZ044.getCarCnt());
				String iHouseCnt = String.valueOf(sJcicZ044.getHouseCnt());
				String iLandCnt = String.valueOf(sJcicZ044.getLandCnt());
				String iChildCnt = String.valueOf(sJcicZ044.getChildCnt());
				String iChildRate = String.valueOf(sJcicZ044.getChildRate());
				String iParentCnt = String.valueOf(sJcicZ044.getParentCnt());
				String iParentRate = String.valueOf(sJcicZ044.getParentRate());
				String iMouthCnt = String.valueOf(sJcicZ044.getMouthCnt());
				String iMouthRate = String.valueOf(sJcicZ044.getMouthRate());
				String iGradeType = sJcicZ044.getGradeType();
				String iPayLastAmt = String.valueOf(sJcicZ044.getPayLastAmt());
				String iPeriod2 = String.valueOf(sJcicZ044.getPeriod2());
				String iRate2 = String.valueOf(sJcicZ044.getRate2());
				String iMonthPayAmt2 = String.valueOf(sJcicZ044.getMonthPayAmt2());
				String iPayLastAmt2 = String.valueOf(sJcicZ044.getPayLastAmt2());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "44"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							 +StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
				             +StringUtils.rightPad(iDebtCode, 2,"")+StringUtils.leftPad(iNonGageAmt, 9,'0')
				             +StringUtils.leftPad(iPeriod, 3,'0')+StringUtils.leftPad(iRate, 5,'0')+StringUtils.leftPad(iMonthPayAmt, 9,'0')
				             +StringUtils.rightPad("", 10)+StringUtils.leftPad(iReceYearIncome, 9,'0')+StringUtils.leftPad(iReceYear, 3,'0')
				             +StringUtils.leftPad(iReceYear2Income, 9,'0')+StringUtils.leftPad(iReceYear2, 3,'0')
				             +StringUtils.leftPad(iCurrentMonthIncome, 9,'0')+StringUtils.leftPad(iLivingCost, 9,'0')
				             +StringUtils.rightPad(iCompName, 40,"")+StringUtils.rightPad(iCompId, 8,"")+StringUtils.rightPad("", 10)
				             +StringUtils.leftPad(iCarCnt, 2,'0')+StringUtils.leftPad(iHouseCnt, 2,'0')+StringUtils.leftPad(iLandCnt, 2,'0')
				             +StringUtils.leftPad(iChildCnt, 2,'0')+StringUtils.leftPad(iChildRate, 5,'0')
				             +StringUtils.leftPad(iParentCnt, 2,'0')+StringUtils.leftPad(iParentRate, 5,'0')
				             +StringUtils.leftPad(iMouthCnt, 2,'0')+StringUtils.leftPad(iMouthRate, 5,'0')+StringUtils.rightPad(iGradeType, 1,"")
				             +StringUtils.leftPad(iPayLastAmt, 9,'0')+StringUtils.rightPad(iPeriod2, 3,"")
				             +StringUtils.leftPad(iRate2, 5,"")+StringUtils.leftPad(iMonthPayAmt2, 9,'0')+StringUtils.leftPad(iPayLastAmt2, 9,'0');
				this.info("text="+text);
				this.put(text);		
				
				//回填JcicDate
				sJcicZ044.setOutJcicTxtDate(iDate);
				try {
					sJcicZ044Service.update(sJcicZ044, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
			
	}
	public void doZ045File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ045> rJcicZ045 = null;
		rJcicZ045 = sJcicZ045Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ045==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for (JcicZ045 sJcicZ045:rJcicZ045) {
				String iTranKey = sJcicZ045.getTranKey();
				String iSubmitKey = sJcicZ045.getSubmitKey();
				String iCustId = sJcicZ045.getCustId();
				String iRcDate = String.valueOf(sJcicZ045.getRcDate());
				String iMaxMainCode = sJcicZ045.getMaxMainCode();
				String iAgreeCode = sJcicZ045.getAgreeCode();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "45"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iMaxMainCode, 3,"")+StringUtils.rightPad(iAgreeCode, 1,"");
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ045.setOutJcicTxtDate(iDate);
				try {
					sJcicZ045Service.update(sJcicZ045, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ046File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ046> rJcicZ046 = null;
		rJcicZ046 = sJcicZ046Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ046==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for (JcicZ046 sJcicZ046:rJcicZ046) {
				String iTranKey = sJcicZ046.getTranKey();
				String iSubmitKey = sJcicZ046.getSubmitKey();
				String iCustId = sJcicZ046.getCustId();
				String iRcDate = String.valueOf(sJcicZ046.getRcDate());
				String iCloseCode = sJcicZ046.getCloseCode();
				String iBreakCode = sJcicZ046.getBreakCode();
				String iCloseDate = String.valueOf(sJcicZ046.getCloseDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "46"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
								+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
								+StringUtils.rightPad(iBreakCode, 2,"")+StringUtils.rightPad("", 3)
								+StringUtils.leftPad(iCloseCode, 2,'0')+StringUtils.leftPad(iCloseDate, 7,'0')+StringUtils.rightPad("", 43);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ046.setOutJcicTxtDate(iDate);
				try {
					sJcicZ046Service.update(sJcicZ046, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ047File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ047> rJcicZ047 = null;
		rJcicZ047 = sJcicZ047Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ047==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ047 sJcicZ047:rJcicZ047) {
				String iTranKey = sJcicZ047.getTranKey();
				String iSubmitKey = sJcicZ047.getSubmitKey();
				String iCustId = sJcicZ047.getCustId();
				String iRcDate = String.valueOf(sJcicZ047.getRcDate());
				String iPeriod = String.valueOf(sJcicZ047.getPeriod());
				String iRate = String.valueOf(sJcicZ047.getRate());
				String iCivil323ExpAmt = String.valueOf(sJcicZ047.getCivil323ExpAmt());
				String iExpLoadAmt = String.valueOf(sJcicZ047.getExpLoanAmt());
				String iCivil323CashAmt = String.valueOf(sJcicZ047.getCivil323CashAmt());
				String iCashCardAmt = String.valueOf(sJcicZ047.getCashCardAmt());
				String iCivil323CreditAmt = String.valueOf(sJcicZ047.getCivil323CreditAmt());
				String iCreditCardAmt = String.valueOf(sJcicZ047.getCreditCardAmt());
				String iCivil323Amt = String.valueOf(sJcicZ047.getCivil323Amt());
				String iTotalAmt = String.valueOf(sJcicZ047.getTotalAmt());
				String iPassDate = String.valueOf(sJcicZ047.getPassDate());
				String iInterviewDate = String.valueOf(sJcicZ047.getInterviewDate());
				String iSignDate = String.valueOf(sJcicZ047.getSignDate());
				String iLimitDate = String.valueOf(sJcicZ047.getLimitDate());
				String iFirstPayDate = String.valueOf(sJcicZ047.getFirstPayDate());
				String iMonthPayAmt = String.valueOf(sJcicZ047.getMonthPayAmt());
				String iPayAccount = String.valueOf(sJcicZ047.getPayAccount());
				String iPostAddr = String.valueOf(sJcicZ047.getPostAddr());
				String iGradeType = String.valueOf(sJcicZ047.getGradeType());
				String iPayLastAmt = String.valueOf(sJcicZ047.getPayLastAmt());
				String iPeriod2 = String.valueOf(sJcicZ047.getPeriod2());
				String iRate2 = String.valueOf(sJcicZ047.getRate2());
				String iMonthPayAmt2 = String.valueOf(sJcicZ047.getMonthPayAmt2());
				String iPayLastAmt2 = String.valueOf(sJcicZ047.getPayLastAmt2());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "47"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iPeriod, 3,'0')+StringUtils.leftPad(iRate, 5,'0')
							+StringUtils.leftPad(iCivil323ExpAmt, 9,'0')+StringUtils.leftPad(iExpLoadAmt, 9,'0')
							+StringUtils.leftPad(iCivil323CashAmt, 9, '0')+StringUtils.leftPad(iCashCardAmt, 9,'0')
							+StringUtils.leftPad(iCivil323CreditAmt, 9,'0')+StringUtils.leftPad(iCreditCardAmt, 9,'0')
							+StringUtils.leftPad(iCivil323Amt,10,'0')+StringUtils.leftPad(iTotalAmt, 10,'0')
							+StringUtils.leftPad(iPassDate, 7,'0')+StringUtils.leftPad(iInterviewDate, 7,"")
							+StringUtils.leftPad(iSignDate, 7,"")+StringUtils.leftPad(iLimitDate, 7,'0')
							+StringUtils.leftPad(iFirstPayDate, 7,'0')+StringUtils.leftPad(iMonthPayAmt, 9,'0')
							+StringUtils.rightPad(iPayAccount,20,"")+StringUtils.rightPad(iPostAddr, 76,"")
							+StringUtils.rightPad(iGradeType, 1,"")+StringUtils.leftPad(iPayLastAmt, 9,'0')
							+StringUtils.rightPad(iPeriod2, 3,"")+StringUtils.leftPad(iRate2, 5,"")
							+StringUtils.leftPad(iMonthPayAmt2,9,'0')+StringUtils.leftPad(iPayLastAmt2, 9,'0')
							+StringUtils.rightPad("", 14);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ047.setOutJcicTxtDate(iDate);
				try {
					sJcicZ047Service.update(sJcicZ047, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
				
	}
	public void doZ048File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ048> rJcicZ048 = null;
		rJcicZ048 = sJcicZ048Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ048==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for (JcicZ048 sJcicZ048:rJcicZ048) {
				String iTranKey = sJcicZ048.getTranKey();
				String iSubmitKey = sJcicZ048.getSubmitKey();
				String iCustId = sJcicZ048.getCustId();
				String iRcDate = String.valueOf(sJcicZ048.getRcDate());
				String iCustRegAddr = sJcicZ048.getCustRegAddr();
				String iCustComAddr = sJcicZ048.getCustComAddr();
				String iCustRegTelNo = sJcicZ048.getCustRegTelNo();
				String iCustComTelNo = sJcicZ048.getCustComTelNo();
				String iCustMobilNo = sJcicZ048.getCustMobilNo();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "48"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iCustRegAddr, 76,"")+StringUtils.rightPad(iCustComAddr, 76,"")
							+StringUtils.rightPad(iCustRegTelNo, 16,"")+StringUtils.rightPad(iCustComTelNo, 16,"")
							+StringUtils.rightPad(iCustMobilNo, 16,"")+StringUtils.rightPad("", 32);
							
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ048.setOutJcicTxtDate(iDate);
				try {
					sJcicZ048Service.update(sJcicZ048, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ049File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ049> rJcicZ049 = null;
		rJcicZ049 = sJcicZ049Service.findAll(this.index, this.limit, titaVo);
		if(rJcicZ049==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for (JcicZ049 sJcicZ049:rJcicZ049) {
				String iTranKey = sJcicZ049.getTranKey();
				String iSubmitKey = sJcicZ049.getSubmitKey();
				String iCustId = sJcicZ049.getCustId();
				String iRcDate = String.valueOf(sJcicZ049.getRcDate());
				String iClaimStatus = String.valueOf(sJcicZ049.getClaimStatus());
				String iApplyDate = String.valueOf(sJcicZ049.getApplyDate());
				String iCourtCode = sJcicZ049.getCourtCode();
				String iYear = String.valueOf(sJcicZ049.getYear());
				String iCourtDiv = sJcicZ049.getCourtDiv();
				String iCourtCaseNo = sJcicZ049.getCourtCaseNo();
				String iApprove = sJcicZ049.getApprove();
				String iClaimDate = String.valueOf(sJcicZ049.getClaimDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "49"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							 +StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							 +StringUtils.rightPad(iClaimStatus, 1,"")+StringUtils.leftPad(iApplyDate, 7,'0')
							 +StringUtils.rightPad(iCourtCode, 3,"")+StringUtils.leftPad(iYear, 3,"")
							 +StringUtils.rightPad(iCourtDiv, 4,"")+StringUtils.rightPad(iCourtCaseNo, 20,"")
							 +StringUtils.rightPad(iApprove, 1,"")+StringUtils.leftPad(iClaimDate, 7,'0')+StringUtils.rightPad("", 46);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ049.setOutJcicTxtDate(iDate);
				try {
					sJcicZ049Service.update(sJcicZ049, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ050File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ050> rJcicZ050 = null;
		rJcicZ050 = sJcicZ050Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ050==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ050 sJcicZ050:rJcicZ050) {
				String iTranKey = sJcicZ050.getTranKey();
				String iSubmitKey = sJcicZ050.getSubmitKey();
				String iCustId = sJcicZ050.getCustId();
				String iRcDate = String.valueOf(sJcicZ050.getRcDate());
				String iPayDate = String.valueOf(sJcicZ050.getPayDate());
				String iPayAmt = String.valueOf(sJcicZ050.getPayAmt());
				String iSumRepayActualAmt = String.valueOf(sJcicZ050.getSumRepayActualAmt());
				String iSumRepayShouldAmt = String.valueOf(sJcicZ050.getSumRepayShouldAmt());
				String iStatus = sJcicZ050.getStatus();
				String iSecondRepayYM = String.valueOf(sJcicZ050.getSecondRepayYM());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "50"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iPayDate, 7,'0')+StringUtils.leftPad(iPayAmt, 9,'0')
							+StringUtils.leftPad(iSumRepayActualAmt, 9,'0')+StringUtils.leftPad(iSumRepayShouldAmt, 9,'0')
							+StringUtils.rightPad(iStatus, 1,"")+StringUtils.leftPad(iSecondRepayYM, 5,'0')
							+StringUtils.rightPad("", 32);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ050.setOutJcicTxtDate(iDate);
				try {
					sJcicZ050Service.update(sJcicZ050, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ051File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ051> rJcicZ051 = null;
		rJcicZ051 = sJcicZ051Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ051==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ051 sJcicZ051:rJcicZ051) {
				String iTranKey = sJcicZ051.getTranKey();
				String iSubmitKey = sJcicZ051.getSubmitKey();
				String iCustId = sJcicZ051.getCustId();
				String iRcDate = String.valueOf(sJcicZ051.getRcDate());
				String iDelayCode = sJcicZ051.getDelayCode();
				String iDelayYM = String.valueOf(sJcicZ051.getDelayYM());
				String iDelayDesc = sJcicZ051.getDelayDesc();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "51"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							  +StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
				  			  +StringUtils.rightPad(iDelayCode, 1,"")+StringUtils.leftPad(iDelayYM, 5,'0')
				  			  +StringUtils.rightPad(iDelayDesc, 40,"")+StringUtils.rightPad("", 46);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ051.setOutJcicTxtDate(iDate);
				try {
					sJcicZ051Service.update(sJcicZ051, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ052File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ052> rJcicZ052 = null;
		rJcicZ052 = sJcicZ052Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ052==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ052 sJcicZ052:rJcicZ052) {
				String iTranKey = sJcicZ052.getTranKey();
				String iSubmitKey = sJcicZ052.getSubmitKey();
				String iCustId = sJcicZ052.getCustId();
				String iRcDate = String.valueOf(sJcicZ052.getRcDate());
				String iBankCode1 = sJcicZ052.getBankCode1();
				String iDataCode1 = sJcicZ052.getDataCode1();
				String iBankCode2 = sJcicZ052.getBankCode2();
				String iDataCode2 = sJcicZ052.getDataCode2();
				String iBankCode3 = sJcicZ052.getBankCode3();
				String iDataCode3 = sJcicZ052.getDataCode3();
				String iBankCode4 = sJcicZ052.getBankCode4();
				String iDataCode4 = sJcicZ052.getDataCode4();
				String iBankCode5 = sJcicZ052.getBankCode5();
				String iDataCode5 = sJcicZ052.getDataCode5();
				String iChangePayDate= String.valueOf(sJcicZ052.getChangePayDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "52"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iBankCode1, 3,"")+StringUtils.rightPad(iDataCode1, 2,"")
							+StringUtils.rightPad(iBankCode2, 3,"")+StringUtils.rightPad(iDataCode2, 2,"")
							+StringUtils.rightPad(iBankCode3, 3,"")+StringUtils.rightPad(iDataCode3, 2,"")
							+StringUtils.rightPad(iBankCode4, 3,"")+StringUtils.rightPad(iDataCode4, 2,"")
							+StringUtils.rightPad(iBankCode5, 3,"")+StringUtils.rightPad(iDataCode5, 2,"")
							+StringUtils.leftPad(iChangePayDate, 7,'0')+StringUtils.rightPad("", 20);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ052.setOutJcicTxtDate(iDate);
				try {
					sJcicZ052Service.update(sJcicZ052, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ053File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ053> rJcicZ053 = null;
		rJcicZ053 = sJcicZ053Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ053==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ053 sJcicZ053:rJcicZ053) {
				String iTranKey = sJcicZ053.getTranKey();
				String iSubmitKey = sJcicZ053.getSubmitKey();
				String iCustId = sJcicZ053.getCustId();
				String iRcDate = String.valueOf(sJcicZ053.getRcDate());
				String iMaxMainCode = sJcicZ053.getMaxMainCode();
				String iAgreeSend = sJcicZ053.getAgreeSend();
				String iAgreeSendData1 = sJcicZ053.getAgreeSendData1();
				String iAgreeSendData2 = sJcicZ053.getAgreeSendData2();
				String iChangePayDate = String.valueOf(sJcicZ053.getChangePayDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "53"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iMaxMainCode, 3,"")+StringUtils.rightPad(iAgreeSend, 1,"")
							+StringUtils.rightPad(iAgreeSendData1, 2,"")+StringUtils.rightPad(iAgreeSendData2, 2,"")
							+StringUtils.leftPad(iChangePayDate, 7,'0')+StringUtils.rightPad("", 37);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ053.setOutJcicTxtDate(iDate);
				try {
					sJcicZ053Service.update(sJcicZ053, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ054File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ054> rJcicZ054 = null;
		rJcicZ054 = sJcicZ054Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ054==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ054 sJcicZ054:rJcicZ054) {
				String iTranKey = sJcicZ054.getTranKey();
				String iSubmitKey = sJcicZ054.getSubmitKey();
				String iCustId = sJcicZ054.getCustId();
				String iRcDate = String.valueOf(sJcicZ054.getRcDate());
				String iMaxMainCode = sJcicZ054.getMaxMainCode();
				String iPayOffResult = sJcicZ054.getPayOffResult();
				String iPayOffDate = String.valueOf(sJcicZ054.getPayOffDate());		
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "54"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iMaxMainCode, 3,"")+StringUtils.rightPad(iPayOffResult, 1,"")
							+StringUtils.leftPad(iPayOffDate, 7,'0')+StringUtils.rightPad("", 41);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ054.setOutJcicTxtDate(iDate);
				try {
					sJcicZ054Service.update(sJcicZ054, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ055File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ055> rJcicZ055 = null;
		rJcicZ055 = sJcicZ055Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ055==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ055 sJcicZ055:rJcicZ055) {
				String iTranKey = sJcicZ055.getTranKey();
				String iSubmitKey = sJcicZ055.getSubmitKey();
				String iCustId = sJcicZ055.getCustId();
				String iCaseStatus = sJcicZ055.getCaseStatus();
				String iClaimDate = String.valueOf(sJcicZ055.getClaimDate());
				String iCourtCode = sJcicZ055.getCourtCode();
				int iYear = sJcicZ055.getYear();
				String iCourtDiv = sJcicZ055.getCourtDiv();
				String iCourtCaseNo = sJcicZ055.getCourtCaseNo();
				String iPayDate = String.valueOf(sJcicZ055.getPayDate());
				String iPayEndDate = String.valueOf(sJcicZ055.getPayEndDate());
				String iPeriod = String.valueOf(sJcicZ055.getPeriod());
				String iRate = String.valueOf(sJcicZ055.getRate());
				String iOutstandAmt = String.valueOf(sJcicZ055.getOutstandAmt());
				String iSubAmt = String.valueOf(sJcicZ055.getSubAmt());
				String iClaimStatus1 = sJcicZ055.getClaimStatus1();
				String iSaveDate = String.valueOf(sJcicZ055.getSaveDate());
				String iClaimStatus2 = sJcicZ055.getClaimStatus2();
				String iSaveEndDate = String.valueOf(sJcicZ055.getSaveEndDate());
				String iIsImplement = sJcicZ055.getIsImplement();
				String iInspectName = sJcicZ055.getInspectName();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "55"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.rightPad(iCaseStatus, 1,"")+StringUtils.leftPad(iClaimDate, 7,'0')
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iCourtCode, 3,"")
							+StringUtils.leftPad(String.valueOf(iYear), 3,'0')+StringUtils.rightPad(iCourtDiv, 4,"")
							+StringUtils.rightPad(iCourtCaseNo, 40,"")+StringUtils.leftPad(iPayDate, 7,"")
							+StringUtils.leftPad(iPayEndDate, 7,"")+StringUtils.leftPad(iPeriod, 3,'0')
							+StringUtils.leftPad(iRate, 5,'0')+StringUtils.leftPad(iOutstandAmt, 9,'0')
							+StringUtils.leftPad(iSubAmt, 9,'0')+StringUtils.rightPad(iClaimStatus1, 1,"")
							+StringUtils.leftPad(iSaveDate, 7,"")+StringUtils.rightPad(iClaimStatus2, 1,"")
							+StringUtils.leftPad(iSaveEndDate, 7,"")+StringUtils.rightPad(iIsImplement, 1,"")
							+StringUtils.rightPad(iInspectName, 10,"")+StringUtils.rightPad("", 54);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ055.setOutJcicTxtDate(iDate);
				try {
					sJcicZ055Service.update(sJcicZ055, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ056File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ056> rJcicZ056 = null;
		rJcicZ056 = sJcicZ056Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ056==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ056 sJcicZ056:rJcicZ056) {
				String iTranKey = sJcicZ056.getTranKey();
				String iSubmitKey = sJcicZ056.getSubmitKey();
				String iCustId = sJcicZ056.getCustId();
				String iCaseStatus = sJcicZ056.getCaseStatus();
				String iClaimDate = String.valueOf(sJcicZ056.getClaimDate());
				String iCourtCode = sJcicZ056.getCourtCode();
				String iYear = String.valueOf(sJcicZ056.getYear());
				String iCourtDiv = sJcicZ056.getCourtDiv();
				String iCourtCaseNo = sJcicZ056.getCourtCaseNo();
				String iApprove = sJcicZ056.getApprove();
				String iOutStandAmt = String.valueOf(sJcicZ056.getOutstandAmt());
				String iClaimStatus1 = sJcicZ056.getClaimStatus1();
				String iSaveDate = String.valueOf(sJcicZ056.getSaveDate());
				String iClaimStatus2 = sJcicZ056.getClaimStatus2();
				String iSaveEndDate = String.valueOf(sJcicZ056.getSaveEndDate());
				String iSubAmt = String.valueOf(sJcicZ056.getSubAmt());
				String iAdminName = sJcicZ056.getAdminName();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "55"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.rightPad(iCaseStatus, 1,"")+StringUtils.leftPad(iClaimDate, 7,'0')
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iCourtCode, 3,"")
							+StringUtils.leftPad(iYear, 3,'0')+StringUtils.rightPad(iCourtDiv, 4,"")
							+StringUtils.rightPad(iCourtCaseNo, 40,"")+StringUtils.rightPad(iApprove, 1,"")
							+StringUtils.leftPad(iOutStandAmt, 9,'0')+StringUtils.leftPad(iSubAmt, 9,'0')
							+StringUtils.rightPad(iClaimStatus1, 1,"")+StringUtils.leftPad(iSaveDate, 7,"")
							+StringUtils.rightPad(iClaimStatus2, 1,"")+StringUtils.leftPad(iSaveEndDate, 7,"")
							+StringUtils.rightPad(iAdminName, 10,"")+StringUtils.rightPad("", 76);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ056.setOutJcicTxtDate(iDate);
				try {
					sJcicZ056Service.update(sJcicZ056, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ060File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ060> rJcicZ060 = null;
		rJcicZ060 = sJcicZ060Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ060==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ060 sJcicZ060:rJcicZ060) {
				String iTranKey = sJcicZ060.getTranKey();
				String iSubmitKey = sJcicZ060.getSubmitKey();
				String iCustId = sJcicZ060.getCustId();
				String iRcDate = String.valueOf(sJcicZ060.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ060.getChangePayDate());
				int iYM = sJcicZ060.getYM();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "60"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iChangePayDate, 7,'0')+StringUtils.leftPad(String.valueOf(iYM), 5,'0')
							+StringUtils.rightPad("", 30);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ060.setOutJcicTxtDate(iDate);
				try {
					sJcicZ060Service.update(sJcicZ060, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ061File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ061> rJcicZ061 = null;
		rJcicZ061 = sJcicZ061Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ061==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ061 sJcicZ061:rJcicZ061) {
				String iTranKey = sJcicZ061.getTranKey();
				String iSubmitKey = sJcicZ061.getSubmitKey();
				String iCustId = sJcicZ061.getCustId();
				String iRcDate = String.valueOf(sJcicZ061.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ061.getChangePayDate());
				String iMaxMainCode = sJcicZ061.getMaxMainCode();
				String iExpBalanceAmt = String.valueOf(sJcicZ061.getExpBalanceAmt());
				String iCashBalanceAmt = String.valueOf(sJcicZ061.getCashBalanceAmt());
				String iCreditBalanceAmt = String.valueOf(sJcicZ061.getCreditBalanceAmt());
				String iMaxMainNote = sJcicZ061.getMaxMainCode();
				String iIsGuarantor = sJcicZ061.getIsGuarantor();
				String iIsChangePayment = sJcicZ061.getIsChangePayment();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "61"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iChangePayDate, 7,'0')+StringUtils.rightPad(iMaxMainCode, 3,"")
							+StringUtils.leftPad(iExpBalanceAmt, 9,'0')+StringUtils.leftPad(iCashBalanceAmt, 9,'0')
							+StringUtils.leftPad(iCreditBalanceAmt, 9,'0')+StringUtils.rightPad(iMaxMainNote, 1,"")
							+StringUtils.rightPad(iIsGuarantor, 1,"")+StringUtils.rightPad(iIsChangePayment, 1,"")
							+StringUtils.rightPad("", 42);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ061.setOutJcicTxtDate(iDate);
				try {
					sJcicZ061Service.update(sJcicZ061, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ062File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ062> rJcicZ062 = null;
		rJcicZ062 = sJcicZ062Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ062==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ062 sJcicZ062:rJcicZ062) {
				String iTranKey = sJcicZ062.getTranKey();
				String iSubmitKey = sJcicZ062.getSubmitKey();
				String iCustId = sJcicZ062.getCustId();
				String iRcDate = String.valueOf(sJcicZ062.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ062.getChangePayDate());
				String iCompletePeriod = String.valueOf(sJcicZ062.getCompletePeriod());
				String iPeriod = String.valueOf(sJcicZ062.getPeriod());
				String iRate = String.valueOf(sJcicZ062.getRate());
				String iExpBalanceAmt =String.valueOf(sJcicZ062.getExpBalanceAmt());
				String iCashBalanceAmt = String.valueOf(sJcicZ062.getCashBalanceAmt());
				String iCreditBalanceAmt = String.valueOf(sJcicZ062.getCreditBalanceAmt());
				String iChaRepayAmt = String.valueOf(sJcicZ062.getChaRepayAmt());
				String iChaRepayAgreeDate = String.valueOf(sJcicZ062.getChaRepayAgreeDate());
				String iChaRepayViewDate = String.valueOf(sJcicZ062.getChaRepayViewDate());
				String iChaRepayEndDate = String.valueOf(sJcicZ062.getChaRepayEndDate());
				String iChaRepayFirstDate = String.valueOf(sJcicZ062.getChaRepayFirstDate());
				String iPayAccount = String.valueOf(sJcicZ062.getPayAccount());
				String iPostAddr = String.valueOf(sJcicZ062.getPostAddr());
				String iMonthPayAmt = String.valueOf(sJcicZ062.getMonthPayAmt());
				String iGradeType = String.valueOf(sJcicZ062.getGradeType());
				String iPeriod2 = String.valueOf(sJcicZ062.getPeriod());
				String iRate2 = String.valueOf(sJcicZ062.getRate2());
				String iMonthPayAmt2 = String.valueOf(sJcicZ062.getMonthPayAmt2());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "62"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.rightPad(iChangePayDate, 7,'0')+StringUtils.leftPad(iCompletePeriod, 3,'0')
							+StringUtils.leftPad(iPeriod, 3,'0')+StringUtils.leftPad(iRate, 5,'0')
							+StringUtils.leftPad(iExpBalanceAmt, 9,'0')+StringUtils.leftPad(iCashBalanceAmt, 9,'0')
							+StringUtils.leftPad(iCreditBalanceAmt, 9,'0')+StringUtils.leftPad(iChaRepayAmt, 10,'0')
							+StringUtils.leftPad(iChaRepayAgreeDate, 7,'0')+StringUtils.leftPad(iChaRepayViewDate, 7,"")
							+StringUtils.leftPad(iChaRepayEndDate, 7,"")+StringUtils.leftPad(iChaRepayFirstDate, 7,'0')
							+StringUtils.rightPad(iPayAccount, 20,"")+StringUtils.rightPad(iPostAddr,76,"")
							+StringUtils.leftPad(iMonthPayAmt, 9,'0')+StringUtils.rightPad(iGradeType,1,"")
							+StringUtils.leftPad(iPeriod2,3,'0')+StringUtils.leftPad(iRate2, 5,'0')
							+StringUtils.leftPad(iMonthPayAmt2, 9,'0')+StringUtils.leftPad("",572);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ062.setOutJcicTxtDate(iDate);
				try {
					sJcicZ062Service.update(sJcicZ062, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}
		}
	}
	public void doZ063File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ063> rJcicZ063 = null;
		rJcicZ063 = sJcicZ063Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ063==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ063 sJcicZ063:rJcicZ063) {
				String iTranKey = sJcicZ063.getTranKey();
				String iSubmitKey = sJcicZ063.getSubmitKey();
				String iCustId = sJcicZ063.getCustId();
				String iRcDate = String.valueOf(sJcicZ063.getRcDate());
				String iChangePayDate = String.valueOf(sJcicZ063.getChangePayDate());
				String iCloseDate = String.valueOf(sJcicZ063.getClosedDate());
				String iClosedResult = sJcicZ063.getClosedResult();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "63"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iRcDate, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iChangePayDate, 7,'0')+StringUtils.leftPad(iCloseDate, 7,'0')
							+StringUtils.rightPad(iClosedResult, 1,"")+StringUtils.rightPad("",42);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ063.setOutJcicTxtDate(iDate);
				try {
					sJcicZ063Service.update(sJcicZ063, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ440File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ440> rJcicZ440 = null;
		rJcicZ440 = sJcicZ440Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ440==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ440 sJcicZ440:rJcicZ440) {
				String iTranKey = sJcicZ440.getTranKey();
				String iSubmitKey = sJcicZ440.getSubmitKey();
				String iCustId = sJcicZ440.getCustId();
				String iApplyDate = String.valueOf(sJcicZ440.getApplyDate());
				String iBankId = sJcicZ440.getBankId();
				String iAgreeDate =  String.valueOf(sJcicZ440.getAgreeDate());
				String iStartDate = String.valueOf(sJcicZ440.getStartDate());
				String iRemindDate = String.valueOf(sJcicZ440.getRemindDate());
				String iApplyType = sJcicZ440.getApplyType();
				String iReportYn = sJcicZ440.getReportYn();
				String iNotBank1 = sJcicZ440.getNotBankId1();
				String iNotBank2 = sJcicZ440.getNotBankId2();
				String iNotBank3 = sJcicZ440.getNotBankId3();
				String iNotBank4 = sJcicZ440.getNotBankId4();
				String iNotBank5 = sJcicZ440.getNotBankId5();
				String iNotBank6 = sJcicZ440.getNotBankId6();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "440"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.leftPad(iAgreeDate, 7,'0')
							+StringUtils.leftPad(iStartDate, 7,'0')+StringUtils.leftPad(iRemindDate, 7,'0')
							+StringUtils.rightPad(iApplyType, 1,"")+StringUtils.rightPad(iReportYn, 1,"")
							+StringUtils.rightPad(iNotBank1, 3,"")+StringUtils.rightPad(iNotBank2, 3,"")
							+StringUtils.rightPad(iNotBank3, 3,"")+StringUtils.rightPad(iNotBank4, 3,"")
							+StringUtils.rightPad(iNotBank5, 3,"")+StringUtils.rightPad(iNotBank6, 3,"")
							+StringUtils.rightPad("", 17);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ440.setOutJcicTxtDate(iDate);
				try {
					sJcicZ440Service.update(sJcicZ440, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ442File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ442> rJcicZ442 = null;
		rJcicZ442 = sJcicZ442Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ442==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ442 sJcicZ442:rJcicZ442) {
				String iTranKey = sJcicZ442.getTranKey();
				String iSubmitKey = sJcicZ442.getSubmitKey();
				String iCustId = sJcicZ442.getCustId();
				String iApplyDate = String.valueOf(sJcicZ442.getApplyDate());
		//		String iBankId = sJcicZ442.getBankId();
				String iMaxMainCode = sJcicZ442.getMaxMainCode();
				String iIsMaxMain = sJcicZ442.getIsMaxMain();
				String iIsClaims = sJcicZ442.getIsClaims();
				String iGuarLoanCnt = String.valueOf(sJcicZ442.getGuarLoanCnt());
				String iCivil323ExpAmt = String.valueOf(sJcicZ442.getCivil323ExpAmt());
				String iCivil323CashAmt =  String.valueOf(sJcicZ442.getCivil323CashAmt());
				String iCivil323CreditAmt =  String.valueOf(sJcicZ442.getCivil323CreditAmt());
				String iCivil323GuarAmt =  String.valueOf(sJcicZ442.getCivil323GuarAmt());
				String iReceExpPrin =  String.valueOf(sJcicZ442.getReceExpPrin());
				String iReceExpInte =  String.valueOf(sJcicZ442.getReceExpInte());
				String iReceExpPena =  String.valueOf(sJcicZ442.getReceExpPena());
				String iReceExpOther =  String.valueOf(sJcicZ442.getReceExpOther());
				String iCashCardPrin =  String.valueOf(sJcicZ442.getCashCardPrin());
				String iCashCardInte =  String.valueOf(sJcicZ442.getCashCardInte());
				String iCashCardPena =  String.valueOf(sJcicZ442.getCashCardPena());
				String iCashCardOther =  String.valueOf(sJcicZ442.getCashCardOther());
				String iCreditCardPrin =  String.valueOf(sJcicZ442.getCreditCardPrin());
				String iCreditCardInte =  String.valueOf(sJcicZ442.getCreditCardInte());
				String iCreditCardPena =  String.valueOf(sJcicZ442.getCreditCardPena());
				String iCreditCardOther =  String.valueOf(sJcicZ442.getCreditCardOther());
				String iGuarObliPrin =  String.valueOf(sJcicZ442.getGuarObliPrin());
				String iGuarObliInte =  String.valueOf(sJcicZ442.getGuarObliInte());
				String iGuarObliPena =  String.valueOf(sJcicZ442.getGuarObliPena());
				String iGuarObliOther =  String.valueOf(sJcicZ442.getGuarObliOther());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "442"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
			//				+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iMaxMainCode, 3,"")
							+StringUtils.rightPad(iIsMaxMain, 1,"")+StringUtils.rightPad(iIsClaims, 1,"")
							+StringUtils.leftPad(iGuarLoanCnt, 2,'0')+StringUtils.leftPad(iCivil323ExpAmt, 9,'0')
							+StringUtils.leftPad(iCivil323CashAmt, 7,'0')+StringUtils.leftPad(iCivil323CreditAmt, 7,'0')
							+StringUtils.leftPad(iCivil323GuarAmt, 7,'0')+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iReceExpPrin, 9,'0')+StringUtils.leftPad(iReceExpInte, 9,'0')
							+StringUtils.leftPad(iReceExpPena, 9,'0')+StringUtils.leftPad(iReceExpOther, 9,'0')
							+StringUtils.leftPad(iCashCardPrin, 9,'0')+StringUtils.leftPad(iCashCardInte, 9,'0')
							+StringUtils.leftPad(iCashCardPena, 9,'0')+StringUtils.leftPad(iCashCardOther, 9,'0')
							+StringUtils.leftPad(iCreditCardPrin, 9,'0')+StringUtils.leftPad(iCreditCardInte, 9,'0')
							+StringUtils.leftPad(iCreditCardPena, 9,'0')+StringUtils.leftPad(iCreditCardOther, 9,'0')
							+StringUtils.leftPad(iGuarObliPrin, 9,'0')+StringUtils.leftPad(iGuarObliInte, 9,'0')
							+StringUtils.leftPad(iGuarObliPena, 9,'0')+StringUtils.leftPad(iGuarObliOther, 9,'0')
							+StringUtils.rightPad("", 56);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ442.setOutJcicTxtDate(iDate);
				try {
					sJcicZ442Service.update(sJcicZ442, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ443File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ443> rJcicZ443 = null;
		rJcicZ443 = sJcicZ443Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ443==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ443 sJcicZ443:rJcicZ443) {
				String iTranKey = sJcicZ443.getTranKey();
				String iSubmitKey = sJcicZ443.getSubmitKey();
				String iCustId = sJcicZ443.getCustId();
				String iApplyDate = String.valueOf(sJcicZ443.getApplyDate());
			//	String iBankId = sJcicZ443.getBankId();
				String iMaxMainCode = sJcicZ443.getMaxMainCode();
				String iIsMaxMain = sJcicZ443.getIsMaxMain();
				String iAccount = sJcicZ443.getAccount();
				String iGuarentType = sJcicZ443.getGuarantyType();
				String iLoanAmt = String.valueOf(sJcicZ443.getLoanAmt());
				String iCreditAmt = String.valueOf(sJcicZ443.getCreditAmt());
				String iPrincipal = String.valueOf(sJcicZ443.getPrincipal());
				String iInterest = String.valueOf(sJcicZ443.getInterest());
				String iPenalty = String.valueOf(sJcicZ443.getPenalty());
				String iOther = String.valueOf(sJcicZ443.getOther());
				String iTerminalPayAmt = String.valueOf(sJcicZ443.getTerminalPayAmt());
				String iLatestPayAmt = String.valueOf(sJcicZ443.getLatestPayAmt());
				String iFinalPayDay = String.valueOf(sJcicZ443.getFinalPayDay());
				String iNotyetaQuit  = String.valueOf(sJcicZ443.getNotyetacQuit());
				String iMothPayDay = String.valueOf(sJcicZ443.getMothPayDay());
				String iBeginDate = String.valueOf(sJcicZ443.getBeginDate());
				String iEndDate = String.valueOf(sJcicZ443.getEndDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "443"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
			//				+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iMaxMainCode, 3,"")
							+StringUtils.rightPad(iIsMaxMain, 1,"")+StringUtils.rightPad(iAccount,50,"")
							+StringUtils.leftPad(iGuarentType, 2,"")+StringUtils.leftPad(iLoanAmt, 12,'0')
							+StringUtils.leftPad(iCreditAmt, 12,'0')+StringUtils.leftPad(iPrincipal, 10,'0')
							+StringUtils.leftPad(iInterest, 10,'0')+StringUtils.leftPad(iPenalty, 10,'0')
							+StringUtils.leftPad(iOther, 10,'0')+StringUtils.leftPad(iTerminalPayAmt, 10,'0')
							+StringUtils.leftPad(iLatestPayAmt, 10,'0')+StringUtils.rightPad(iFinalPayDay,7,"")
							+StringUtils.leftPad(iNotyetaQuit, 10,'0')+StringUtils.rightPad(iMothPayDay, 2,"")
							+StringUtils.rightPad(iBeginDate, 5,"")+StringUtils.rightPad(iEndDate, 5,"")
							+StringUtils.rightPad("", 49);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ443.setOutJcicTxtDate(iDate);
				try {
					sJcicZ443Service.update(sJcicZ443, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ444File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ444> rJcicZ444 = null;
		rJcicZ444 = sJcicZ444Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ444==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ444 sJcicZ444:rJcicZ444) {
				String iTranKey = sJcicZ444.getTranKey();
				String iSubmitKey = sJcicZ444.getSubmitKey();
				String iCustId = sJcicZ444.getCustId();
				String iApplyDate = String.valueOf(sJcicZ444.getApplyDate());
			//	String iBankId = sJcicZ444.getBankId();
				String iCustRegAddr = sJcicZ444.getCustRegAddr();
				String iCustComAddr = sJcicZ444.getCustComAddr();
				String iCustRegTelNo = sJcicZ444.getCustRegTelNo();
				String iCustComTelNo = sJcicZ444.getCustComTelNo();
				String iCustMobilNo = sJcicZ444.getCustMobilNo();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "444"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
			//				+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iCustRegAddr, 76,"")
							+StringUtils.rightPad(iCustComAddr, 76,"")+StringUtils.rightPad(iCustRegTelNo, 16,"")
							+StringUtils.rightPad(iCustComTelNo,16,"")+StringUtils.rightPad(iCustMobilNo, 16,"")
							+StringUtils.rightPad("", 18);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ444.setOutJcicTxtDate(iDate);
				try {
					sJcicZ444Service.update(sJcicZ444, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ446File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ446> rJcicZ446 = null;
		rJcicZ446 = sJcicZ446Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ446==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ446 sJcicZ446:rJcicZ446) {
				String iTranKey = sJcicZ446.getTranKey();
				String iSubmitKey = sJcicZ446.getSubmitKey();
				String iCustId = sJcicZ446.getCustId();
				String iApplyDate = String.valueOf(sJcicZ446.getApplyDate());
				String iBankId = sJcicZ446.getBankId();
				String iCloseCode = sJcicZ446.getCloseCode();
				String iCloseDate = String.valueOf(sJcicZ446.getCloseDate());	
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "446"+iTranKey+iSubmitKey
							+StringUtils.rightPad(iCustId, 10,"")+StringUtils.leftPad(iApplyDate, 7,'0')
							+StringUtils.rightPad(iBankId, 3,"")+StringUtils.rightPad("", 5)
							+StringUtils.leftPad(iCloseCode,2,'0')+StringUtils.leftPad(iCloseDate,7,'0')
							+StringUtils.rightPad("", 39);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ446.setOutJcicTxtDate(iDate);
				try {
					sJcicZ446Service.update(sJcicZ446, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ447File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ447> rJcicZ447 = null;
		rJcicZ447 = sJcicZ447Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ447==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ447 sJcicZ447:rJcicZ447) {
				String iTranKey = sJcicZ447.getTranKey();
				String iSubmitKey = sJcicZ447.getSubmitKey();
				String iCustId = sJcicZ447.getCustId();
				String iApplyDate = String.valueOf(sJcicZ447.getApplyDate());
			//	String iBankId = sJcicZ447.getBankId();
				String iCivil323Amt = String.valueOf(sJcicZ447.getCivil323Amt());
				String iTotalAmt = String.valueOf(sJcicZ447.getTotalAmt());
				String iSignDate = String.valueOf(sJcicZ447.getSignDate());
				String iFirstPayDate = String.valueOf(sJcicZ447.getFirstPayDate());
				String iPeriod = String.valueOf(sJcicZ447.getPeriod());
				String iRate = String.valueOf(sJcicZ447.getRate());
				String iMonthPayAmt = String.valueOf(sJcicZ447.getMonthPayAmt());
				String iPayAccount = sJcicZ447.getPayAccount();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "447"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
			//				+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.leftPad(iCivil323Amt, 10,'0')
							+StringUtils.leftPad(iTotalAmt, 10,'0')+StringUtils.leftPad(iSignDate, 7,"")
							+StringUtils.leftPad(iFirstPayDate, 7,'0')+StringUtils.leftPad(iPeriod, 3,'0')
							+StringUtils.leftPad(iRate, 5,'0')+StringUtils.leftPad(iMonthPayAmt, 9,'0')
							+StringUtils.rightPad(iPayAccount, 20,"")+StringUtils.rightPad("",27);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ447.setOutJcicTxtDate(iDate);
				try {
					sJcicZ447Service.update(sJcicZ447, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ448File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ448> rJcicZ448 = null;
		rJcicZ448 = sJcicZ448Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ448==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ448 sJcicZ448:rJcicZ448) {
				String iTranKey = sJcicZ448.getTranKey();
				String iSubmitKey = sJcicZ448.getSubmitKey();
				String iCustId = sJcicZ448.getCustId();
				String iApplyDate = String.valueOf(sJcicZ448.getApplyDate());
				String iBankId = sJcicZ448.getBankId();
				String iMaxMainCode = sJcicZ448.getMaxMainCode();
				String iSignPrin = String.valueOf(sJcicZ448.getSignPrin());
				String iSignOther = String.valueOf(sJcicZ448.getSignOther());
				String iOwnPercentage = String.valueOf(sJcicZ448.getOwnPercentage());
				String iAcQuitAmt = String.valueOf(sJcicZ448.getAcQuitAmt());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "448"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iMaxMainCode, 3,"")
							+StringUtils.leftPad(iSignPrin,9,'0')+StringUtils.leftPad(iSignOther,9,'0')
							+StringUtils.leftPad(iOwnPercentage,6,"")+StringUtils.leftPad(iAcQuitAmt,9,'0')
							+StringUtils.rightPad("",22);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ448.setOutJcicTxtDate(iDate);
				try {
					sJcicZ448Service.update(sJcicZ448, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ450File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ450> rJcicZ450 = null;
		rJcicZ450 = sJcicZ450Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ450==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ450 sJcicZ450:rJcicZ450) {
				String iTranKey = sJcicZ450.getTranKey();
				String iSubmitKey = sJcicZ450.getSubmitKey();
				String iCustId = sJcicZ450.getCustId();
				String iApplyDate = String.valueOf(sJcicZ450.getApplyDate());
		//		String iBankId = sJcicZ450.getBankId();
				String iPayDate = String.valueOf(sJcicZ450.getPayDate());
				String iPayAmt = String.valueOf(sJcicZ450.getPayAmt());
				String iSumRepayActualAmt = String.valueOf(sJcicZ450.getSumRepayActualAmt());
				String iSumRepayShouldAmt = String.valueOf(sJcicZ450.getSumRepayShouldAmt());
				String iPayStatus = sJcicZ450.getPayStatus();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "450"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
		//					+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iPayDate, 7,"")
							+StringUtils.leftPad(iPayAmt, 9,'0')+StringUtils.leftPad(iSumRepayActualAmt, 9,'0')
							+StringUtils.leftPad(iSumRepayShouldAmt, 9,'0')+StringUtils.rightPad(iPayStatus, 1,"")
							+StringUtils.rightPad("", 23);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ450.setOutJcicTxtDate(iDate);
				try {
					sJcicZ450Service.update(sJcicZ450, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ451File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ451> rJcicZ451 = null;
		rJcicZ451 = sJcicZ451Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ451==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ451 sJcicZ451:rJcicZ451) {
				String iTranKey = sJcicZ451.getTranKey();
				String iSubmitKey = sJcicZ451.getSubmitKey();
				String iCustId = sJcicZ451.getCustId();
				String iApplyDate = String.valueOf(sJcicZ451.getApplyDate());
		//		String iBankId = sJcicZ451.getBankId();
				String iDelayYM = String.valueOf(sJcicZ451.getDelayYM());
				String iDelayCode = sJcicZ451.getDelayCode();
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "451"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
		//					+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iDelayCode, 1,"")
							+StringUtils.leftPad(iDelayYM, 5,'0')+StringUtils.rightPad("",12);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ451.setOutJcicTxtDate(iDate);
				try {
					sJcicZ451Service.update(sJcicZ451, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ454File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ454> rJcicZ454 = null;
		rJcicZ454 = sJcicZ454Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ454==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ454 sJcicZ454:rJcicZ454) {
				String iTranKey = sJcicZ454.getTranKey();
				String iSubmitKey = sJcicZ454.getSubmitKey();
				String iCustId = sJcicZ454.getCustId();
				String iApplyDate = String.valueOf(sJcicZ454.getApplyDate());
				String iBankId = sJcicZ454.getBankId();
				String iMaxMainCode = sJcicZ454.getMaxMainCode();
				String iPayOffResult = sJcicZ454.getPayOffResult();
				String iPayOffDate = String.valueOf(sJcicZ454.getPayOffDate());
				int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
				String text = "454"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
							+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad(iBankId, 3,"")
							+StringUtils.rightPad("", 5)+StringUtils.rightPad(iMaxMainCode, 3,"")
							+StringUtils.rightPad(iPayOffResult, 1,"")+StringUtils.leftPad(iPayOffDate, 7,'0')
							+StringUtils.rightPad("",37);
				this.info("text="+text);
				this.put(text);
				
				//回填JcicDate
				sJcicZ454.setOutJcicTxtDate(iDate);
				try {
					sJcicZ454Service.update(sJcicZ454, titaVo);
				}catch (Exception e){
					throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
				}
			}	
		}
	}
	public void doZ570File(TitaVo titaVo) throws LogicException{
	    Slice<JcicZ570> rJcicZ570 = null;
	    rJcicZ570 = sJcicZ570Service.findAll(this.index, this.limit,titaVo);
	    if(rJcicZ570==null) {
	        throw new LogicException(titaVo,"E0001","");
	    }else {
	        for(JcicZ570 sJcicZ570:rJcicZ570) {
	            if(sJcicZ570.getOutJcicTxtDate()==0) {
	                String iTranKey = sJcicZ570.getTranKey();
	                String iSubmitKey = sJcicZ570.getSubmitKey();
	                String iCustId = sJcicZ570.getCustId();
	                String iApplyDate = String.valueOf(sJcicZ570.getApplyDate());
	                String iAdjudicateDate = String.valueOf(sJcicZ570.getAdjudicateDate());
	                String iBankCount = String.valueOf(sJcicZ570.getBankCount());
	                int ixAdjudicateDate = Integer.valueOf(sJcicZ570.getAdjudicateDate());
	                int ixBankCount = Integer.valueOf(sJcicZ570.getBankCount());
	                String iBank1 = sJcicZ570.getBank1();
	                String iBank2 = sJcicZ570.getBank2();
	                String iBank3 = sJcicZ570.getBank3();
	                String iBank4 = sJcicZ570.getBank4();
	                String iBank5 = sJcicZ570.getBank5();
	                String iBank6 = sJcicZ570.getBank6();
	                String iBank7 = sJcicZ570.getBank7();
	                String iBank8 = sJcicZ570.getBank8();
	                String iBank9 = sJcicZ570.getBank9();
	                String iBank10 = sJcicZ570.getBank10();
	                String iBank11 = sJcicZ570.getBank11();
	                String iBank12 = sJcicZ570.getBank12();
	                String iBank13 = sJcicZ570.getBank13();
	                String iBank14 = sJcicZ570.getBank14();
	                String iBank15 = sJcicZ570.getBank15();
	                String iBank16 = sJcicZ570.getBank16();
	                String iBank17 = sJcicZ570.getBank17();
	                String iBank18 = sJcicZ570.getBank18();
	                String iBank19 = sJcicZ570.getBank19();
	                String iBank20 = sJcicZ570.getBank20();
	                String iBank21 = sJcicZ570.getBank21();
	                String iBank22 = sJcicZ570.getBank22();
	                String iBank23 = sJcicZ570.getBank23();
	                String iBank24 = sJcicZ570.getBank24();
	                String iBank25 = sJcicZ570.getBank25();
	                String iBank26 = sJcicZ570.getBank26();
	                String iBank27 = sJcicZ570.getBank27();
	                String iBank28 = sJcicZ570.getBank28();
	                String iBank29 = sJcicZ570.getBank29();
	                String iBank30 = sJcicZ570.getBank30();
	                int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
	                String iUkey = sJcicZ570.getUkey();
	                String text = "570"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
	                            +StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad("", 5)
	                            +StringUtils.leftPad(iAdjudicateDate, 7,'0')+StringUtils.leftPad(iBankCount, 2,'0')
	                            +StringUtils.rightPad(iBank1, 3,"")+StringUtils.rightPad(iBank2, 3,"")
	                            +StringUtils.rightPad(iBank3, 3,"")+StringUtils.rightPad(iBank4, 3,"")
	                            +StringUtils.rightPad(iBank5, 3,"")+StringUtils.rightPad(iBank6, 3,"")
	                            +StringUtils.rightPad(iBank7, 3,"")+StringUtils.rightPad(iBank8, 3,"")
	                            +StringUtils.rightPad(iBank9, 3,"")+StringUtils.rightPad(iBank10, 3,"")
	                            +StringUtils.rightPad(iBank11, 3,"")+StringUtils.rightPad(iBank12, 3,"")
	                            +StringUtils.rightPad(iBank13, 3,"")+StringUtils.rightPad(iBank14, 3,"")
	                            +StringUtils.rightPad(iBank15, 3,"")+StringUtils.rightPad(iBank16, 3,"")
	                            +StringUtils.rightPad(iBank17, 3,"")+StringUtils.rightPad(iBank18, 3,"")
	                            +StringUtils.rightPad(iBank19, 3,"")+StringUtils.rightPad(iBank20, 3,"")
	                            +StringUtils.rightPad(iBank21, 3,"")+StringUtils.rightPad(iBank22, 3,"")
	                            +StringUtils.rightPad(iBank23, 3,"")+StringUtils.rightPad(iBank24, 3,"")
	                            +StringUtils.rightPad(iBank25, 3,"")+StringUtils.rightPad(iBank26, 3,"")
	                            +StringUtils.rightPad(iBank27, 3,"")+StringUtils.rightPad(iBank28, 3,"")
	                            +StringUtils.rightPad(iBank29, 3,"")+StringUtils.rightPad(iBank30, 3,"");
	                this.info("text="+text);
	                this.put(text);
	            
	            //檔案產生後，回填JcicDate
	            sJcicZ570.setOutJcicTxtDate(iDate);
	            try {
	                sJcicZ570Service.update(sJcicZ570, titaVo);
	            }catch (Exception e){
	                throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
	            }
	            
	            //回填JcicDate後寫入Log檔
	            JcicZ570Log iJcicZ570Log = new JcicZ570Log();
	            JcicZ570LogId iJcicZ570LogId = new JcicZ570LogId();
	            this.info("TxSeq==="+titaVo.getTxSeq());
	            iJcicZ570LogId.setTxSeq(titaVo.getTxSeq());
	            iJcicZ570LogId.setUkey(iUkey);
	            iJcicZ570Log.setJcicZ570LogId(iJcicZ570LogId);
	            iJcicZ570Log.setAdjudicateDate(ixAdjudicateDate);
	            iJcicZ570Log.setBankCount(ixBankCount);
	            iJcicZ570Log.setBank1(iBank1);
	            iJcicZ570Log.setBank2(iBank2);
	            iJcicZ570Log.setBank3(iBank3);
	            iJcicZ570Log.setBank4(iBank4);
	            iJcicZ570Log.setBank5(iBank5);
	            iJcicZ570Log.setBank6(iBank6);
	            iJcicZ570Log.setBank7(iBank7);
	            iJcicZ570Log.setBank8(iBank8);
	            iJcicZ570Log.setBank9(iBank9);
	            iJcicZ570Log.setBank10(iBank10);
	            iJcicZ570Log.setBank11(iBank11);
	            iJcicZ570Log.setBank12(iBank12);
	            iJcicZ570Log.setBank13(iBank13);
	            iJcicZ570Log.setBank14(iBank14);
	            iJcicZ570Log.setBank15(iBank15);
	            iJcicZ570Log.setBank16(iBank16);
	            iJcicZ570Log.setBank17(iBank17);
	            iJcicZ570Log.setBank18(iBank18);
	            iJcicZ570Log.setBank19(iBank19);
	            iJcicZ570Log.setBank20(iBank20);
	            iJcicZ570Log.setBank21(iBank21);
	            iJcicZ570Log.setBank22(iBank22);
	            iJcicZ570Log.setBank23(iBank23);
	            iJcicZ570Log.setBank24(iBank24);
	            iJcicZ570Log.setBank25(iBank25);
	            iJcicZ570Log.setBank26(iBank26);
	            iJcicZ570Log.setBank27(iBank27);
	            iJcicZ570Log.setBank28(iBank28);
	            iJcicZ570Log.setBank29(iBank29);
	            iJcicZ570Log.setBank30(iBank30);
	            iJcicZ570Log.setTranKey(iTranKey);
	            iJcicZ570Log.setOutJcicTxtDate(iDate);
	            try {
	                sJcicZ570LogService.insert(iJcicZ570Log, titaVo);
	            }catch (Exception e){
	                throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z570)");
	            }
	        }
	    }	
	}
	}
	public void doZ571File(TitaVo titaVo) throws LogicException{
	    Slice<JcicZ571> rJcicZ571 = null;
	    rJcicZ571 = sJcicZ571Service.findAll(this.index, this.limit,titaVo);
	    if(rJcicZ571==null) {
	        throw new LogicException(titaVo,"E0001","");
	    }else {
	        for(JcicZ571 sJcicZ571:rJcicZ571) {
	            if(sJcicZ571.getOutJcicTxtDate()==0) {
	                String iTranKey = sJcicZ571.getTranKey();
	                String iSubmitKey = sJcicZ571.getSubmitKey();
	                String iCustId = sJcicZ571.getCustId();
	                sJcicZ571.getBankId();
	                String iApplyDate = String.valueOf(sJcicZ571.getApplyDate());
	                String iXOwnerAmt = String.valueOf(sJcicZ571.getOwnerAmt());
	                String iXAllotAmt = String.valueOf(sJcicZ571.getAllotAmt());
	                String iXUnallotAmt = String.valueOf(sJcicZ571.getUnallotAmt());
	                int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
	        		String iOwnerYn = String.valueOf(sJcicZ571.getOwnerYn());
	        		String iPayYn = String.valueOf(sJcicZ571.getPayYn());
	        		int iOwnerAmt = Integer.valueOf(sJcicZ571.getOwnerAmt());
	        		int iAllotAmt = Integer.valueOf(sJcicZ571.getAllotAmt());
	        		int iUnallotAmt = Integer.valueOf(sJcicZ571.getUnallotAmt());
	        		String iUkey = sJcicZ571.getUkey();
	                //組成檔案內容，並產生檔案
	                String text = "571"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
	                             +StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad("", 5)
	                             +StringUtils.rightPad(iOwnerYn, 1,"")+StringUtils.rightPad(iPayYn, 1,"")
	                             +StringUtils.leftPad(iXOwnerAmt, 9,'0')+StringUtils.leftPad(iXAllotAmt, 9,'0')
	                             +StringUtils.leftPad(iXUnallotAmt, 9,'0')+StringUtils.rightPad("", 44);
	                this.info("text="+text);
	                this.put(text);
	                
	                //檔案產生後，回填JcicDate
	                sJcicZ571.setOutJcicTxtDate(iDate);
	                try {
	                    sJcicZ571Service.update(sJcicZ571, titaVo);
	                }catch (Exception e){
	                    throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
	                }
	                
	                //回填JcicDate後寫入Log檔
	                JcicZ571Log iJcicZ571Log = new JcicZ571Log();
	                JcicZ571LogId iJcicZ571LogId = new JcicZ571LogId();
	                this.info("TxSeq==="+titaVo.getTxSeq());
	                iJcicZ571LogId.setTxSeq(titaVo.getTxSeq());
	                iJcicZ571LogId.setUkey(iUkey);
	                iJcicZ571Log.setJcicZ571LogId(iJcicZ571LogId);
	                iJcicZ571Log.setOwnerYn(iOwnerYn);
	                iJcicZ571Log.setPayYn(iPayYn);
	                iJcicZ571Log.setOwnerAmt(iOwnerAmt);
	                iJcicZ571Log.setAllotAmt(iAllotAmt);
	                iJcicZ571Log.setUnallotAmt(iUnallotAmt);
	                iJcicZ571Log.setTranKey(iTranKey);
	                iJcicZ571Log.setOutJcicTxtDate(iDate);
	                try {
	                    sJcicZ571LogService.insert(iJcicZ571Log, titaVo);
	                }catch (Exception e){
	                    throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z571)");
	                }
	            }
	        }	
	    }
	}
	public void doZ572File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ572> rJcicZ572 = null;
		rJcicZ572 = sJcicZ572Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ572==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ572 sJcicZ572:rJcicZ572) {
				if(sJcicZ572.getOutJcicTxtDate()==0) {
					String iTranKey = sJcicZ572.getTranKey();
					String iSubmitKey = sJcicZ572.getSubmitKey();
					String iCustId = sJcicZ572.getCustId();
					String iApplyDate = String.valueOf(sJcicZ572.getApplyDate());
					String iStartDate = String.valueOf(sJcicZ572.getStartDate());
					int ixStartDate = Integer.valueOf(sJcicZ572.getStartDate());
					String iPayDate = String.valueOf(sJcicZ572.getPayDate());
					String iBankId = sJcicZ572.getBankId();
					String iAllotAmt = String.valueOf(sJcicZ572.getAllotAmt());
					int ixAllotAmt = Integer.valueOf(sJcicZ572.getAllotAmt());
					String iOwnPercentage = String.valueOf(sJcicZ572.getOwnPercentage());
					BigDecimal ixOwnPercentage = sJcicZ572.getOwnPercentage();	 
					String iUkey = sJcicZ572.getUkey();
					int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
					//組成檔案內容，並產生檔案
					String text = "572"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
								+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad("", 5)
								+StringUtils.leftPad(iStartDate, 7,'0')+StringUtils.leftPad(iPayDate, 7,'0')
								+StringUtils.rightPad(iBankId,3,"")+StringUtils.leftPad(iAllotAmt, 9,'0')
							+	StringUtils.leftPad(iOwnPercentage, 6,'0')+StringUtils.rightPad("", 44);
					this.info("text="+text);
					this.put(text);
					
					//檔案產生後，回填JcicDate
					sJcicZ572.setOutJcicTxtDate(iDate);
					try {
						sJcicZ572Service.update(sJcicZ572, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
					}
					
					//回填JcicDate後寫入Log檔
					JcicZ572Log iJcicZ572Log = new JcicZ572Log();
					JcicZ572LogId iJcicZ572LogId = new JcicZ572LogId();
					this.info("TxSeq==="+titaVo.getTxSeq());
					iJcicZ572LogId.setTxSeq(titaVo.getTxSeq());
					iJcicZ572LogId.setUkey(iUkey);
					iJcicZ572Log.setJcicZ572LogId(iJcicZ572LogId);
					iJcicZ572Log.setStartDate(ixStartDate);
					iJcicZ572Log.setAllotAmt(ixAllotAmt);
					iJcicZ572Log.setOwnPercentage(ixOwnPercentage);
					iJcicZ572Log.setTranKey(iTranKey);
					iJcicZ572Log.setOutJcicTxtDate(iDate);
					
					try {
						sJcicZ572LogService.insert(iJcicZ572Log, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z572)");
					}
				}
			}	
		}
	}
	public void doZ573File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ573> rJcicZ573 = null;
		rJcicZ573 = sJcicZ573Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ573==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ573 sJcicZ573:rJcicZ573) {
				if(sJcicZ573.getOutJcicTxtDate()==0) {
					String iTranKey = sJcicZ573.getTranKey();
					String iSubmitKey = sJcicZ573.getSubmitKey();
					String iCustId = sJcicZ573.getCustId();
					String iApplyDate = String.valueOf(sJcicZ573.getApplyDate());
					String iPayDate = String.valueOf(sJcicZ573.getPayDate());
					String iPayAmt = String.valueOf(sJcicZ573.getPayAmt());
					int ixPayAmt = Integer.valueOf(sJcicZ573.getPayAmt());
					String iTotalPayAmt = String.valueOf(sJcicZ573.getTotalPayAmt());
					int ixTotalPayAmt = Integer.valueOf(sJcicZ573.getTotalPayAmt());
					String iUkey = sJcicZ573.getUkey();
					int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
					//組成檔案內容，並產生檔案
					String text = "573"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
								+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad("", 5)
								+StringUtils.leftPad(iPayDate, 7,'0')+StringUtils.leftPad(iPayAmt, 9,'0')
								+StringUtils.leftPad(iTotalPayAmt, 9,'0')+StringUtils.rightPad("", 51);
					this.info("text="+text);
					this.put(text);
					
					//檔案產生後，回填JcicDate
					sJcicZ573.setOutJcicTxtDate(iDate);
					try {
						sJcicZ573Service.update(sJcicZ573, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
					}
					
					//回填JcicDate後寫入Log檔
					JcicZ573Log iJcicZ573Log = new JcicZ573Log();
					JcicZ573LogId iJcicZ573LogId = new JcicZ573LogId();
					this.info("TxSeq==="+titaVo.getTxSeq());
					iJcicZ573LogId.setTxSeq(titaVo.getTxSeq());
					iJcicZ573LogId.setUkey(iUkey);
					iJcicZ573Log.setJcicZ573LogId(iJcicZ573LogId);
					iJcicZ573Log.setPayAmt(ixPayAmt);
					iJcicZ573Log.setTotalPayAmt(ixTotalPayAmt);
					iJcicZ573Log.setTranKey(iTranKey);
					iJcicZ573Log.setOutJcicTxtDate(iDate);
					
					try {
						sJcicZ573LogService.insert(iJcicZ573Log, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z573)");
					}
				}
			}	
		}
	}
	public void doZ574File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ574> rJcicZ574 = null;
		rJcicZ574 = sJcicZ574Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ574==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ574 sJcicZ574:rJcicZ574) {
				if(sJcicZ574.getOutJcicTxtDate()==0) {
					String iTranKey = sJcicZ574.getTranKey();
					String iSubmitKey = sJcicZ574.getSubmitKey();
					String iCustId = sJcicZ574.getCustId();
					String iApplyDate = String.valueOf(sJcicZ574.getApplyDate());
					String iCloseDate = String.valueOf(sJcicZ574.getCloseDate());
					int ixCloseDate = Integer.valueOf(sJcicZ574.getCloseDate());
					String iCloseMark = sJcicZ574.getCloseMark();
					String iPhoneNo = sJcicZ574.getPhoneNo();
					String iUkey = sJcicZ574.getUkey();
					int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
					//組成檔案內容，並產生檔案
					String text = "574"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
								+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.leftPad(iCloseDate, 7,'0')
								+StringUtils.rightPad(iCloseMark, 2,"")+StringUtils.rightPad(iPhoneNo, 16,"")
								+StringUtils.rightPad("", 31);
					this.info("text="+text);
					this.put(text);
					
					//檔案產生後，回填JcicDate
					sJcicZ574.setOutJcicTxtDate(iDate);
					try {
						sJcicZ574Service.update(sJcicZ574, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
					}
					
					//回填JcicDate後寫入Log檔
					JcicZ574Log iJcicZ574Log = new JcicZ574Log();
					JcicZ574LogId iJcicZ574LogId = new JcicZ574LogId();
					this.info("TxSeq==="+titaVo.getTxSeq());
					iJcicZ574LogId.setTxSeq(titaVo.getTxSeq());
					iJcicZ574LogId.setUkey(iUkey);
					iJcicZ574Log.setJcicZ574LogId(iJcicZ574LogId);
					iJcicZ574Log.setCloseDate(ixCloseDate);
					iJcicZ574Log.setCloseMark(iCloseMark);
					iJcicZ574Log.setPhoneNo(iPhoneNo);
					iJcicZ574Log.setTranKey(iTranKey);
					iJcicZ574Log.setOutJcicTxtDate(iDate);
					
					try {
						sJcicZ574LogService.insert(iJcicZ574Log, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z574)");
					}
				}
			}	
		}
	}
	public void doZ575File(TitaVo titaVo) throws LogicException{
		Slice<JcicZ575> rJcicZ575 = null;
		rJcicZ575 = sJcicZ575Service.findAll(this.index, this.limit,titaVo);
		if(rJcicZ575==null) {
			throw new LogicException(titaVo,"E0001","");
		}else {
			for(JcicZ575 sJcicZ575:rJcicZ575) {
				if(sJcicZ575.getOutJcicTxtDate()==0) {
					String iTranKey = sJcicZ575.getTranKey();
					String iSubmitKey = sJcicZ575.getSubmitKey();
					String iCustId = sJcicZ575.getCustId();
					String iApplyDate = String.valueOf(sJcicZ575.getApplyDate());
					String iBankId = sJcicZ575.getBankId();
					String iModifyType = sJcicZ575.getModifyType();
//					String iUkey = sJcicZ575.getUkey();
					int iDate = Integer.valueOf(titaVo.getParam("ReportDate"));
					//組成檔案內容，並產生檔案
					String text = "575"+iTranKey+iSubmitKey+StringUtils.rightPad(iCustId, 10,"")
								+StringUtils.leftPad(iApplyDate, 7,'0')+StringUtils.rightPad("", 5)
							    +StringUtils.rightPad(iModifyType, 1,"")+StringUtils.rightPad(iBankId, 3,"")
							    +StringUtils.rightPad("", 52);
					this.info("text="+text);
					this.put(text);
					
					//檔案產生後，回填JcicDate
					sJcicZ575.setOutJcicTxtDate(iDate);
					try {
						sJcicZ575Service.update(sJcicZ575, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0007", "回填Jcic報送日期時發生錯誤");
					}
					
					//回填JcicDate後寫入Log檔
					JcicZ575Log iJcicZ575Log = new JcicZ575Log();
					JcicZ575LogId iJcicZ575LogId = new JcicZ575LogId();
					this.info("TxSeq==="+titaVo.getTxSeq());
					iJcicZ575LogId.setTxSeq(titaVo.getTxSeq());
//					iJcicZ575LogId.setUkey(iUkey);
					iJcicZ575Log.setJcicZ575LogId(iJcicZ575LogId);
					iJcicZ575Log.setModifyType(iModifyType);
					iJcicZ575Log.setTranKey(iTranKey);
					iJcicZ575Log.setOutJcicTxtDate(iDate);

					try {
						sJcicZ575LogService.insert(iJcicZ575Log, titaVo);
					}catch (Exception e){
						throw new LogicException(titaVo, "E0005", "寫入記錄檔時發生錯誤(Z575)");
					}
				}
			}	
		}
	}
}
