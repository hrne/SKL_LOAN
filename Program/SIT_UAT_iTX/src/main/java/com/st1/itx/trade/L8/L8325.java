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
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ444Id;
import com.st1.itx.db.domain.JcicZ444Log;
import com.st1.itx.db.service.JcicZ444LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ444Service;

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

@Service("L8325")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8325 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ444Service sJcicZ444Service;
	@Autowired
	public JcicZ444LogService sJcicZ444LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8325 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		String iCustRegAddr = titaVo.getParam("CustRegAddr");
		String iCustComAddr = titaVo.getParam("CustComAddr");
		String iCustRegTelNo = titaVo.getParam("CustRegTelNo");
		String iCustComTelNo = titaVo.getParam("CustComTelNo");
		String iCustMobilNo = titaVo.getParam("CustMobilNo");
		String iKey = "";
		//JcicZ444
		JcicZ444 iJcicZ444 = new JcicZ444();
		JcicZ444Id iJcicZ444Id = new JcicZ444Id();
		iJcicZ444Id.setSubmitKey(iSubmitKey);
		iJcicZ444Id.setCustId(iCustId);		
		iJcicZ444Id.setApplyDate(iApplyDate);
		iJcicZ444Id.setCourtCode(iCourtCode);
		JcicZ444 chJcicZ444 = new JcicZ444();

		switch(iTranKey_Tmp) {
		case "1":
		    //檢核是否重複
		    chJcicZ444 = sJcicZ444Service.findById(iJcicZ444Id, titaVo);
		    this.info("TEST==="+chJcicZ444);
		    if (chJcicZ444!=null) {
		        throw new LogicException("E0005", "已有相同資料");
		    }
		    
		    iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		    iJcicZ444.setJcicZ444Id(iJcicZ444Id);
		    iJcicZ444.setTranKey(iTranKey);
		    iJcicZ444.setUkey(iKey);
		    iJcicZ444.setCustRegAddr(iCustRegAddr);
		    iJcicZ444.setCustComAddr(iCustComAddr);
		    iJcicZ444.setCustRegTelNo(iCustRegTelNo);
		    iJcicZ444.setCustComTelNo(iCustComTelNo);
		    iJcicZ444.setCustMobilNo(iCustMobilNo);
		    try {
		        sJcicZ444Service.insert(iJcicZ444, titaVo);
		    }catch (DBException e) {
		        throw new LogicException("E0005", "更生債權金額異動通知資料");
		    }
		    break;
		case "2":
		    iKey = titaVo.getParam("Ukey");
		    iJcicZ444 = sJcicZ444Service.ukeyFirst(iKey, titaVo);
		    JcicZ444 uJcicZ444 = new JcicZ444();
		    uJcicZ444 = sJcicZ444Service.holdById(iJcicZ444.getJcicZ444Id(), titaVo);
		    if (uJcicZ444 == null) {
		        throw new LogicException("E0007", "無此更新資料");
		    }
		    uJcicZ444.setTranKey(iTranKey);
		    uJcicZ444.setCustRegAddr(iCustRegAddr);
		    uJcicZ444.setCustComAddr(iCustComAddr);
		    uJcicZ444.setCustRegTelNo(iCustRegTelNo);
		    uJcicZ444.setCustComTelNo(iCustComTelNo);
		    uJcicZ444.setCustMobilNo(iCustMobilNo);
		    uJcicZ444.setOutJcicTxtDate(0);
		    JcicZ444 oldJcicZ444 = (JcicZ444) iDataLog.clone(uJcicZ444);
		    try {
		        sJcicZ444Service.update(uJcicZ444, titaVo);
		    }catch (DBException e) {
		        throw new LogicException("E0005", "更生債權金額異動通知資料");
		    }
		    iDataLog.setEnv(titaVo, oldJcicZ444, uJcicZ444);
		    iDataLog.exec();
		    break;
		case "4": //需刷主管卡
		    iJcicZ444 = sJcicZ444Service.findById(iJcicZ444Id);
		    if (iJcicZ444 == null) {
		        throw new LogicException("E0008", "");
		    }
		    if (!titaVo.getHsupCode().equals("1")) {
		        iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
		    }
		    Slice<JcicZ444Log> dJcicLogZ444 = null;
		    dJcicLogZ444 = sJcicZ444LogService.ukeyEq(iJcicZ444.getUkey(), 0, Integer.MAX_VALUE, titaVo);
		    if (dJcicLogZ444 == null) {
		        //尚未開始寫入log檔之資料，主檔資料可刪除
		        try {
		            sJcicZ444Service.delete(iJcicZ444, titaVo);
		        }catch (DBException e) {
		            throw new LogicException("E0008", "更生債權金額異動通知資料");
		        }
		    }else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
		        //最近一筆之資料
		        JcicZ444Log iJcicZ444Log = dJcicLogZ444.getContent().get(0);				
			    iJcicZ444.setCustRegAddr(iJcicZ444Log.getCustRegAddr());
			    iJcicZ444.setCustComAddr(iJcicZ444Log.getCustComAddr());
			    iJcicZ444.setCustRegTelNo(iJcicZ444Log.getCustRegTelNo());
			    iJcicZ444.setCustComTelNo(iJcicZ444Log.getCustComTelNo());
			    iJcicZ444.setCustMobilNo(iJcicZ444Log.getCustMobilNo());
		        iJcicZ444.setOutJcicTxtDate(iJcicZ444Log.getOutJcicTxtDate());
		        try {
		            sJcicZ444Service.update(iJcicZ444, titaVo);
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

