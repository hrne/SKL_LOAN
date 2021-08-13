package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4702ServiceImpl;
import com.st1.itx.trade.L4.L4702Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * CustNo=9,7<br>
 * RepayCode=9,1<br>
 * IntReportFlag=X,1<br>
 * DeprptFlag=X,1<br>
 * END=X,1<br>
 */

@Service("L4702")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4702 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4702.class);

	@Autowired
	public L4702ServiceImpl l4702ServiceImpl;

	@Autowired
	public L4702Report txReport;

	@Autowired
	public Parse parse;

	private int iACCTDATE_ST = 0;
	private int iACCTDATE_ED = 0;
	private int iCUSTNO = 0;
	private String iCONDITION1 = "";
	private int iCONDITION2 = 0;
	private int iID_TYPE = 0;
	private int iCORP_IND = 0;
	private int iAPNO = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4702 ");
//		TxBuffer txbuffer = this.getTxBuffer();
		this.totaVo.init(titaVo);
//		個人 tita輸入 1:個別列印;2:整批列印
		int funcCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

//		整批 BatxDetail s
//		  0.AcDate=Today
//		  1.RepayCode = 1(匯款轉帳)
//		  2.ProcStsCode = 7(完成) 

		/*
		 * ["#R1+@會計日期",#ACCTDATE_ST,"～",#ACCTDATE_ED,"止有變動者"], ["#R2+@戶號",#CUSTNO],
		 * ["#R3+@選擇條件一",#CONDITION1,#CONDITION1X],
		 * ["#R4+@選擇條件二",#CONDITION2,#CONDITION2X], ["#R5+@戶別",#ID_TYPE,#ID_TYPEX],
		 * ["#R6+@企金別",#CORP_IND,#CORP_INDX], ["#R7+@業務科目",#APNO,#APNOX],
		 */

//		繳息通知單

		switch (funcCode) {
		case 1:
//			List<Map<String, String>> L4702List = txReport.exec(titaVo);

//			今日已還款者 BatxDetail 
//			1.acDate = today 
//			2.procCode = 6
			this.info("L4702 Start ...");

			iACCTDATE_ST = parse.stringToInteger(titaVo.getParam("ACCTDATE_ST")) + 19110000;
			iACCTDATE_ED = parse.stringToInteger(titaVo.getParam("ACCTDATE_ED")) + 19110000;
			iCUSTNO = parse.stringToInteger(titaVo.getParam("CUSTNO"));
			iCONDITION1 = titaVo.getParam("CONDITION1");
			iCONDITION2 = parse.stringToInteger(titaVo.getParam("CONDITION2"));
			iID_TYPE = parse.stringToInteger(titaVo.getParam("ID_TYPE"));
			iCORP_IND = parse.stringToInteger(titaVo.getParam("CORP_IND"));
			iAPNO = parse.stringToInteger(titaVo.getParam("APNO"));

//			存入憑條

			break;
		case 2:
//			List<Map<String, String>> L4702List = txReport.exec(titaVo);

//			今日已還款者 BatxDetail 
//			1.acDate = today 
//			2.procCode = 6
			this.info("L4702 Start ...");

			iACCTDATE_ST = this.getTxBuffer().getTxCom().getTbsdyf();
			iACCTDATE_ED = this.getTxBuffer().getTxCom().getTbsdyf();
			iCUSTNO = 0;

//			A.匯款轉帳，已入金，有欠繳-------入帳完成後，於應處理事項清單執行L4702。
//			B.銀扣火險成功，期款失敗----------L4454.銀扣失敗通知(一扣)
			iCONDITION1 = "A";
			iCONDITION2 = 0; // 全部
			iID_TYPE = 0; // 全部
			iCORP_IND = 0; // 全部
			iAPNO = 0; // 全部

//			存入憑條

			break;
		default:
			break;
		}

		execL9705(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execL9705(TitaVo titaVo) throws LogicException {

		titaVo.putParam("ACCTDATE_ST", iACCTDATE_ST);
		titaVo.putParam("ACCTDATE_ED", iACCTDATE_ED);
		titaVo.putParam("CUSTNO", iCUSTNO);
		titaVo.putParam("CONDITION1", iCONDITION1);
		titaVo.putParam("CONDITION2", iCONDITION2);
		titaVo.putParam("ID_TYPE", iID_TYPE);
		titaVo.putParam("CORP_IND", iCORP_IND);
		titaVo.putParam("APNO", iAPNO);

		this.info("ACCTDATE_ST ..." + titaVo.getParam("ACCTDATE_ST"));
		this.info("ACCTDATE_ED ..." + titaVo.getParam("ACCTDATE_ED"));
		this.info("CUSTNO ..." + titaVo.getParam("CUSTNO"));
		this.info("CONDITION1 ..." + titaVo.getParam("CONDITION1"));
		this.info("CONDITION2 ..." + titaVo.getParam("CONDITION2"));
		this.info("ID_TYPE ..." + titaVo.getParam("ID_TYPE"));
		this.info("CORP_IND ..." + titaVo.getParam("CORP_IND"));
		this.info("APNO ..." + titaVo.getParam("APNO"));

		MySpring.newTask("L9705p", this.txBuffer, titaVo);
	}
}