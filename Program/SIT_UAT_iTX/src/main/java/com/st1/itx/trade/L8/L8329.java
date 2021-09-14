package com.st1.itx.trade.L8;

import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ447;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;
import com.st1.itx.db.domain.JcicZ450Log;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ450LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ450Service;

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

@Service("L8329")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8329 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ450Service sJcicZ450Service;
	@Autowired
	public JcicZ450LogService sJcicZ450LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8329 ");
		this.totaVo.init(titaVo);
				
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iCourtCode = titaVo.getParam("CourtCode");
		int iPayDate = Integer.valueOf(titaVo.getParam("PayDate"));
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		int iPayAmt = Integer.valueOf(titaVo.getParam("PayAmt"));
		int iSumRepayActualAmt = Integer.valueOf(titaVo.getParam("SumRepayActualAmt"));
		int iSumRepayShouldAmt = Integer.valueOf(titaVo.getParam("SumRepayShouldAmt"));
		String iPayStatus = titaVo.getParam("PayStatus");
		int ixApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"))+19110000;
		String iKey = "";
		//JcicZ450
		JcicZ450 iJcicZ450 = new JcicZ450();
		JcicZ450Id iJcicZ450Id = new JcicZ450Id();
		iJcicZ450Id.setSubmitKey(iSubmitKey);
		iJcicZ450Id.setCustId(iCustId);
		iJcicZ450Id.setApplyDate(iApplyDate);
		iJcicZ450Id.setCourtCode(iCourtCode);
		iJcicZ450Id.setPayDate(iPayDate);
		JcicZ450 chJcicZ450 = new JcicZ450();
		//檢核項目(D-58)
		//需檢核「IDN+報送單位代號+調解申請日+受理調解機構代號」是否存在「'447':金融機構無擔保債務協議資料」
		//二start
		Slice<JcicZ447> xJcicZ447 = sJcicZ447Service.otherEq(iSubmitKey,iCustId,ixApplyDate,iCourtCode, this.index, this.limit, titaVo);
		if (xJcicZ447 == null) {
			throw new LogicException(titaVo, "E0005", "「IDN+報送單位代號+調解申請日+受理調解機構代號+最大債權金融機構」是否存在「'447':金融機構無擔保債務協議資料」"); 
		}
		//二end
		//累計實際還款金額不等於該IDN所有已報送本檔案資料繳款金額之合計(含本次繳款金額)
		//now PayAmt  count SumRepayActualAmt
		//三start
		Slice<JcicZ450> xJcicZ450 = sJcicZ450Service.custIdEq(iCustId, this.index, this.limit, titaVo);
		if (xJcicZ450 == null) {
			throw new LogicException(titaVo, "E0005", "查無(450)前置調解債務人繳款資料"); 
		}
		for(JcicZ450 xoJcicZ450:xJcicZ450) {
		String ixCustId = iJcicZ450Id.getCustId();
		int ixPayAmt = xoJcicZ450.getPayAmt();
		this.info("ixPayAmt"+ixPayAmt);
		this.info("iPayAmt"+iPayAmt);
			if(ixCustId == iCustId) {
				if((ixPayAmt + iPayAmt) != iSumRepayActualAmt) {
					throw new LogicException(titaVo, "E0005", "累計繳款金額不等於該IND所有已報送之繳款金額(含今日)");
					}			
			}	
		}
		//三end
		//「繳款日期」不得大於資料報送日期
		//四start
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		int year = (cal.get(Calendar.YEAR)-1911)*10000;
//		int month = (cal.get(Calendar.MONTH) +1)*100;
//		int day = cal.get(Calendar.DAY_OF_MONTH);
//		int today = year+month+day;
		int today = Integer.valueOf(titaVo.get("ENTDY"))+19110000;
		if(iPayDate>today) {
			throw new LogicException(titaVo, "E0005", "「繳款日期」不得大於資料報送日期"); 
		}
		//四end
		//同一key值報送'446'檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料
		//五start
		Slice<JcicZ446> xJcicZ446 = sJcicZ446Service.custIdEq(iCustId, this.index, this.limit, titaVo);
		if (xJcicZ446 != null) {
			throw new LogicException(titaVo, "E0005", "同一key值報送'446'檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料"); 
		}
		//五end
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ450
			chJcicZ450 = sJcicZ450Service.findById(iJcicZ450Id, titaVo);
			if (chJcicZ450 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ450.setJcicZ450Id(iJcicZ450Id);
			iJcicZ450.setTranKey(iTranKey);
			iJcicZ450.setPayAmt(iPayAmt);
			iJcicZ450.setSumRepayActualAmt(iSumRepayActualAmt);
			iJcicZ450.setSumRepayShouldAmt(iSumRepayShouldAmt);
			iJcicZ450.setPayStatus(iPayStatus);
			iJcicZ450.setUkey(iKey);
			try {
				sJcicZ450Service.insert(iJcicZ450, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ450 = sJcicZ450Service.ukeyFirst(iKey, titaVo);
			JcicZ450 uJcicZ450 = new JcicZ450();
			uJcicZ450 = sJcicZ450Service.holdById(iJcicZ450.getJcicZ450Id(), titaVo);
			if (uJcicZ450 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ450.setPayAmt(iPayAmt);
			uJcicZ450.setSumRepayActualAmt(iSumRepayActualAmt);
			uJcicZ450.setSumRepayShouldAmt(iSumRepayShouldAmt);
			uJcicZ450.setPayStatus(iPayStatus);
			uJcicZ450.setTranKey(iTranKey);
			uJcicZ450.setOutJcicTxtDate(0);
			JcicZ450 oldJcicZ450 = (JcicZ450) iDataLog.clone(uJcicZ450);
			try {
				sJcicZ450Service.update(iJcicZ450, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ450, uJcicZ450);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ450 = sJcicZ450Service.findById(iJcicZ450Id);
			if (iJcicZ450 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ450Log> dJcicLogZ450 = null;
			dJcicLogZ450 = sJcicZ450LogService.ukeyEq(iJcicZ450.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ450 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ450Service.delete(iJcicZ450, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ450Log iJcicZ450Log = dJcicLogZ450.getContent().get(0);
				iJcicZ450.setPayAmt(iJcicZ450Log.getPayAmt());
				iJcicZ450.setSumRepayActualAmt(iJcicZ450Log.getSumRepayActualAmt());
				iJcicZ450.setSumRepayShouldAmt(iJcicZ450Log.getSumRepayShouldAmt());
				iJcicZ450.setPayStatus(iJcicZ450Log.getPayStatus());
				iJcicZ450.setTranKey(iJcicZ450Log.getTranKey());
				iJcicZ450.setOutJcicTxtDate(iJcicZ450Log.getOutJcicTxtDate());
				try {
					sJcicZ450Service.update(iJcicZ450, titaVo);
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