package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;


import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;
import com.st1.itx.db.domain.JcicZ570Log;
import com.st1.itx.db.service.JcicZ570LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ570Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
* Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* ChangePayDate=9,7<br>
* ClosedDate=9,7<br>
* ClosedResult=9,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8332")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8332 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8332.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ570Service sJcicZ570Service;
    @Autowired
	public JcicZ570LogService sJcicZ570LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8332 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		int iAdjudicateDate = Integer.valueOf(titaVo.getParam("AdjudicateDate"));
		int iBankCount = Integer.valueOf(titaVo.getParam("BankCount"));
		String iBank1 = titaVo.getParam("Bank1");
		String iBank2 = titaVo.getParam("Bank2");
		String iBank3 = titaVo.getParam("Bank3");
		String iBank4 = titaVo.getParam("Bank4");
		String iBank5 = titaVo.getParam("Bank5");
		String iBank6 = titaVo.getParam("Bank6");
		String iBank7 = titaVo.getParam("Bank7");
		String iBank8 = titaVo.getParam("Bank8");
		String iBank9 = titaVo.getParam("Bank9");
		String iBank10 = titaVo.getParam("Bank10");
		String iBank11 = titaVo.getParam("Bank11");
		String iBank12 = titaVo.getParam("Bank12");
		String iBank13 = titaVo.getParam("Bank13");
		String iBank14 = titaVo.getParam("Bank14");
		String iBank15 = titaVo.getParam("Bank15");
		String iBank16 = titaVo.getParam("Bank16");
		String iBank17 = titaVo.getParam("Bank17");
		String iBank18 = titaVo.getParam("Bank18");
		String iBank19 = titaVo.getParam("Bank19");
		String iBank20 = titaVo.getParam("Bank20");
		String iBank21 = titaVo.getParam("Bank21");
		String iBank22 = titaVo.getParam("Bank22");
		String iBank23 = titaVo.getParam("Bank23");
		String iBank24 = titaVo.getParam("Bank24");
		String iBank25 = titaVo.getParam("Bank25");
		String iBank26 = titaVo.getParam("Bank26");
		String iBank27 = titaVo.getParam("Bank27");
		String iBank28 = titaVo.getParam("Bank28");
		String iBank29 = titaVo.getParam("Bank29");
		String iBank30 = titaVo.getParam("Bank30");
		String iKey = "";
		JcicZ570 iJcicZ570 = new JcicZ570();
		JcicZ570Id iJcicZ570Id = new JcicZ570Id();
        iJcicZ570Id.setApplyDate(iApplyDate);
		iJcicZ570Id.setCustId(iCustId);
		iJcicZ570Id.setSubmitKey(iSubmitKey);
		JcicZ570 chJcicZ570 = new JcicZ570();
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ570
			chJcicZ570 = sJcicZ570Service.findById(iJcicZ570Id, titaVo);
			this.info("findById========="+iJcicZ570Id);
			this.info("iKey========="+iKey);
			this.info("iApplyDate========="+iApplyDate);
			this.info("iTranKey========="+iJcicZ570Id);
			this.info("iSubmitKey========="+iSubmitKey);
			this.info("iCustId========="+iCustId);
			if (chJcicZ570!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ570.setJcicZ570Id(iJcicZ570Id);
			iJcicZ570.setUkey(iKey);
			iJcicZ570.setTranKey(iTranKey);
			iJcicZ570.setAdjudicateDate(iAdjudicateDate);
			iJcicZ570.setBankCount(iBankCount);
			iJcicZ570.setBank1(iBank1);
			iJcicZ570.setBank2(iBank2);
			iJcicZ570.setBank3(iBank3);
			iJcicZ570.setBank4(iBank4);
			iJcicZ570.setBank5(iBank5);
			iJcicZ570.setBank6(iBank6);
			iJcicZ570.setBank7(iBank7);
			iJcicZ570.setBank8(iBank8);
			iJcicZ570.setBank9(iBank9);
			iJcicZ570.setBank10(iBank10);
			iJcicZ570.setBank11(iBank11);
			iJcicZ570.setBank12(iBank12);
			iJcicZ570.setBank13(iBank13);
			iJcicZ570.setBank14(iBank14);
			iJcicZ570.setBank15(iBank15);
			iJcicZ570.setBank16(iBank16);
			iJcicZ570.setBank17(iBank17);
			iJcicZ570.setBank18(iBank18);
			iJcicZ570.setBank19(iBank19);
			iJcicZ570.setBank20(iBank20);
			iJcicZ570.setBank21(iBank21);
			iJcicZ570.setBank22(iBank22);
			iJcicZ570.setBank23(iBank23);
			iJcicZ570.setBank24(iBank24);
			iJcicZ570.setBank25(iBank25);
			iJcicZ570.setBank26(iBank26);
			iJcicZ570.setBank27(iBank27);
			iJcicZ570.setBank28(iBank28);
			iJcicZ570.setBank29(iBank29);
			iJcicZ570.setBank30(iBank30);
			try {
				sJcicZ570Service.insert(iJcicZ570, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
        iKey = titaVo.getParam("Ukey");
        iJcicZ570 = sJcicZ570Service.ukeyFirst(iKey, titaVo);
        JcicZ570 uJcicZ570 = new JcicZ570();
        uJcicZ570 = sJcicZ570Service.holdById(iJcicZ570.getJcicZ570Id(), titaVo);
        if (uJcicZ570 == null) {
            throw new LogicException("E0007", "無此更新資料");
        }
		uJcicZ570.setTranKey(iTranKey);
		uJcicZ570.setAdjudicateDate(iAdjudicateDate);
		uJcicZ570.setBankCount(iBankCount);
		uJcicZ570.setBank1(iBank1);
		uJcicZ570.setBank2(iBank2);
		uJcicZ570.setBank3(iBank3);
		uJcicZ570.setBank4(iBank4);
		uJcicZ570.setBank5(iBank5);
		uJcicZ570.setBank6(iBank6);
		uJcicZ570.setBank7(iBank7);
		uJcicZ570.setBank8(iBank8);
		uJcicZ570.setBank9(iBank9);
		uJcicZ570.setBank10(iBank10);
		uJcicZ570.setBank11(iBank11);
		uJcicZ570.setBank12(iBank12);
		uJcicZ570.setBank13(iBank13);
		uJcicZ570.setBank14(iBank14);
		uJcicZ570.setBank15(iBank15);
		uJcicZ570.setBank16(iBank16);
		uJcicZ570.setBank17(iBank17);
		uJcicZ570.setBank18(iBank18);
		uJcicZ570.setBank19(iBank19);
		uJcicZ570.setBank20(iBank20);
		uJcicZ570.setBank21(iBank21);
		uJcicZ570.setBank22(iBank22);
		uJcicZ570.setBank23(iBank23);
		uJcicZ570.setBank24(iBank24);
		uJcicZ570.setBank25(iBank25);
		uJcicZ570.setBank26(iBank26);
		uJcicZ570.setBank27(iBank27);
		uJcicZ570.setBank28(iBank28);
		uJcicZ570.setBank29(iBank29);
		uJcicZ570.setBank30(iBank30);
		uJcicZ570.setOutJcicTxtDate(0);
			JcicZ570 oldJcicZ570 = (JcicZ570) iDataLog.clone(uJcicZ570);
			try {
				sJcicZ570Service.update(uJcicZ570, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ570, uJcicZ570);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ570 = sJcicZ570Service.findById(iJcicZ570Id);
			if (iJcicZ570 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ570Log> dJcicLogZ570 = null;
			dJcicLogZ570 = sJcicZ570LogService.ukeyEq(iJcicZ570.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ570 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ570Service.delete(iJcicZ570, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ570Log iJcicZ570Log = dJcicLogZ570.getContent().get(0);
				iJcicZ570.setAdjudicateDate(iJcicZ570Log.getAdjudicateDate());
                iJcicZ570.setBankCount(iJcicZ570Log.getBankCount());
                iJcicZ570.setBank1(iJcicZ570Log.getBank1());
                iJcicZ570.setBank2(iJcicZ570Log.getBank2());
                iJcicZ570.setBank3(iJcicZ570Log.getBank3());
                iJcicZ570.setBank4(iJcicZ570Log.getBank4());
                iJcicZ570.setBank5(iJcicZ570Log.getBank5());
                iJcicZ570.setBank6(iJcicZ570Log.getBank6());
                iJcicZ570.setBank7(iJcicZ570Log.getBank7());
                iJcicZ570.setBank8(iJcicZ570Log.getBank8());
                iJcicZ570.setBank9(iJcicZ570Log.getBank9());
                iJcicZ570.setBank10(iJcicZ570Log.getBank10());
                iJcicZ570.setBank11(iJcicZ570Log.getBank11());
                iJcicZ570.setBank12(iJcicZ570Log.getBank12());
                iJcicZ570.setBank13(iJcicZ570Log.getBank13());
                iJcicZ570.setBank14(iJcicZ570Log.getBank14());
                iJcicZ570.setBank15(iJcicZ570Log.getBank15());
                iJcicZ570.setBank16(iJcicZ570Log.getBank16());
                iJcicZ570.setBank17(iJcicZ570Log.getBank17());
                iJcicZ570.setBank18(iJcicZ570Log.getBank18());
                iJcicZ570.setBank19(iJcicZ570Log.getBank19());
                iJcicZ570.setBank20(iJcicZ570Log.getBank20());
                iJcicZ570.setBank21(iJcicZ570Log.getBank21());
                iJcicZ570.setBank22(iJcicZ570Log.getBank22());
                iJcicZ570.setBank23(iJcicZ570Log.getBank23());
                iJcicZ570.setBank24(iJcicZ570Log.getBank24());
                iJcicZ570.setBank25(iJcicZ570Log.getBank25());
                iJcicZ570.setBank26(iJcicZ570Log.getBank26());
                iJcicZ570.setBank27(iJcicZ570Log.getBank27());
                iJcicZ570.setBank28(iJcicZ570Log.getBank28());
                iJcicZ570.setBank29(iJcicZ570Log.getBank29());
                iJcicZ570.setBank30(iJcicZ570Log.getBank30());
				iJcicZ570.setOutJcicTxtDate(iJcicZ570Log.getOutJcicTxtDate());
				try {
					sJcicZ570Service.update(iJcicZ570, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
