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
import com.st1.itx.db.domain.CdCode;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
import com.st1.itx.db.domain.JcicZ440Log;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ440LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ446Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8322")
@Scope("prototype")
/**
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8322 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ440LogService sJcicZ440LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	@Autowired
	public CdCodeService iCdCodeService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8322 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		int iAgreeDate = Integer.valueOf(titaVo.getParam("AgreeDate"));
		int iStartDate = Integer.valueOf(titaVo.getParam("StartDate"));
		int iRemindDate = Integer.valueOf(titaVo.getParam("RemindDate"));
		String iApplyType = titaVo.getParam("ApplyType");
		String iReportYn = titaVo.getParam("ReportYn");
		String iNotBankId1 = titaVo.getParam("NotBankId1");
		String iNotBankId2 = titaVo.getParam("NotBankId2");
		String iNotBankId3 = titaVo.getParam("NotBankId3");
		String iNotBankId4 = titaVo.getParam("NotBankId4");
		String iNotBankId5 = titaVo.getParam("NotBankId5");
		String iNotBankId6 = titaVo.getParam("NotBankId6");
		int ixApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"))+19110000;
		String iKey = "";
		// JcicZ440
		JcicZ440 iJcicZ440 = new JcicZ440();
		JcicZ440Id iJcicZ440Id = new JcicZ440Id();
		iJcicZ440Id.setApplyDate(iApplyDate);
		iJcicZ440Id.setCourtCode(iCourtCode);
		iJcicZ440Id.setCustId(iCustId);
		iJcicZ440Id.setSubmitKey(iSubmitKey);
		JcicZ440 chJcicZ440 = new JcicZ440();
		//檢核項目(D-44)
		//「調解申請日」不得大於「資料報送日」
		//三start
		int iTest = titaVo.getEntDyI();
		
		iTest = 20210913;
		//先用固定的日期之後再轉為非固定日期
		String today2  = titaVo.getCalDy();
		this.info("today2 = "+ today2);
//		this.info("today = "+ iTest);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		int year = (cal.get(Calendar.YEAR)-1911)*10000;
//		int month = (cal.get(Calendar.MONTH) +1)*100;
//		int day = cal.get(Calendar.DAY_OF_MONTH);
//		int today = year+month+day;
		if(iTest<iApplyDate+19110000) {
			throw new LogicException(titaVo, "E0005","「調解申請日」不得大於「資料報送日」");
		}
		//三end
		//「同意書取得日期」不得大於「資料報送日」
		//四start
		if(iTest<iAgreeDate+19110000) {
			throw new LogicException(titaVo, "E0005","「同意書取得日期」不得大於「資料報送日」");
		}
		//四end
		//「首次調解日」小於等於「同意書取得日期」時，「協辦行是否需自行債權」需填報N
		//五start
		if(iStartDate+19110000<=iAgreeDate+19110000) {
			if(!iReportYn.equals("N")) {
				throw new LogicException(titaVo, "E0005","「首次調解日」小於等於「同意書取得日期」時，「協辦行是否需自行債權」需填報N");
			}
		}
		//五end
		//檢核報送單位、「未揭露債權機構代號」，若不屬於有效消債條例金融機構代號，則予剔退
		//六start
			String ixCourtCode;
	        String ixNotBankId1;
	        String ixNotBankId2;
	        String ixNotBankId3;
	        String ixNotBankId4;
	        String ixNotBankId5;
	        String ixNotBankId6;
	        	ixCourtCode=dealBankName(iCourtCode,titaVo);
	        	ixNotBankId1=dealBankName(iNotBankId1,titaVo);
	        	ixNotBankId2=dealBankName(iNotBankId2,titaVo);
	        	ixNotBankId3=dealBankName(iNotBankId3,titaVo);
	        	ixNotBankId4=dealBankName(iNotBankId4,titaVo);
	        	ixNotBankId5=dealBankName(iNotBankId5,titaVo);
	        	ixNotBankId6=dealBankName(iNotBankId6,titaVo);
	        	if(ixNotBankId2==null) {
	        		this.info("ixCourtCode 是 NULL");
	        	}
	        	if(ixNotBankId2.equals("")){
	        		this.info("ixCourtCode 是 空白 值");
	        	}
	        	
                if(!iCourtCode.equals("")&&ixCourtCode.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
                if(!iNotBankId1.equals("")&&ixNotBankId1.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
                if(!iNotBankId2.equals("")&&ixNotBankId2.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
                if(!iNotBankId3.equals("")&&ixNotBankId3.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
                if(!iNotBankId4.equals("")&&ixNotBankId4.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
                if(!iNotBankId5.equals("")&&ixNotBankId5.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
                if(!iNotBankId6.equals("")&&ixNotBankId6.equals("")){
                    throw new LogicException("E0005", "報送單位、「未揭露債權機構代號」，不屬於有效消債條例金融機構代號");
                    }
		//六end
		//同一key值報送'446'檔案，且該結案資料未刪除前，不得新增、異動本檔案資料
		//七start
		Slice<JcicZ446> xJcicZ446 = sJcicZ446Service.otherEq(iSubmitKey,iCustId,ixApplyDate,iCourtCode, this.index, this.limit, titaVo);
		if (xJcicZ446 != null) {
			throw new LogicException(titaVo, "E0005","同一key值報送'446'檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料");
		}
		//七end
		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ440
			chJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
			if (chJcicZ440 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ440.setJcicZ440Id(iJcicZ440Id);
			iJcicZ440.setTranKey(iTranKey);
			iJcicZ440.setAgreeDate(iAgreeDate);
			iJcicZ440.setStartDate(iStartDate);
			iJcicZ440.setRemindDate(iRemindDate);
			iJcicZ440.setApplyType(iApplyType);
			iJcicZ440.setReportYn(iReportYn);
			iJcicZ440.setNotBankId1(iNotBankId1);
			iJcicZ440.setNotBankId2(iNotBankId2);
			iJcicZ440.setNotBankId3(iNotBankId3);
			iJcicZ440.setNotBankId4(iNotBankId4);
			iJcicZ440.setNotBankId5(iNotBankId5);
			iJcicZ440.setNotBankId6(iNotBankId6);
			iJcicZ440.setUkey(iKey);
			try {
				sJcicZ440Service.insert(iJcicZ440, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ440 = sJcicZ440Service.ukeyFirst(iKey, titaVo);
			JcicZ440 uJcicZ440 = new JcicZ440();
			uJcicZ440 = sJcicZ440Service.holdById(iJcicZ440.getJcicZ440Id(), titaVo);
			if (uJcicZ440 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ440.setAgreeDate(iAgreeDate);
			uJcicZ440.setStartDate(iStartDate);
			uJcicZ440.setRemindDate(iRemindDate);
			uJcicZ440.setApplyType(iApplyType);
			uJcicZ440.setReportYn(iReportYn);
			uJcicZ440.setNotBankId1(iNotBankId1);
			uJcicZ440.setNotBankId2(iNotBankId2);
			uJcicZ440.setNotBankId3(iNotBankId3);
			uJcicZ440.setNotBankId4(iNotBankId4);
			uJcicZ440.setNotBankId5(iNotBankId5);
			iJcicZ440.setNotBankId6(iNotBankId6);
			uJcicZ440.setTranKey(iTranKey);
			uJcicZ440.setOutJcicTxtDate(0);
			JcicZ440 oldJcicZ440 = (JcicZ440) iDataLog.clone(uJcicZ440);
			try {
				sJcicZ440Service.update(uJcicZ440, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ440, uJcicZ440);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id);
			if (iJcicZ440 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ440Log> dJcicLogZ440 = null;
			dJcicLogZ440 = sJcicZ440LogService.ukeyEq(iJcicZ440.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ440 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ440Service.delete(iJcicZ440, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ440Log iJcicZ440Log = dJcicLogZ440.getContent().get(0);
				iJcicZ440.setAgreeDate(iJcicZ440Log.getAgreeDate());
				iJcicZ440.setStartDate(iJcicZ440Log.getStartDate());
				iJcicZ440.setRemindDate(iJcicZ440Log.getRemindDate());
				iJcicZ440.setApplyType(iJcicZ440Log.getApplyType());
				iJcicZ440.setReportYn(iJcicZ440Log.getReportYn());
				iJcicZ440.setNotBankId1(iJcicZ440Log.getNotBankId1());
				iJcicZ440.setNotBankId2(iJcicZ440Log.getNotBankId2());
				iJcicZ440.setNotBankId3(iJcicZ440Log.getNotBankId3());
				iJcicZ440.setNotBankId4(iJcicZ440Log.getNotBankId4());
				iJcicZ440.setNotBankId5(iJcicZ440Log.getNotBankId5());
				iJcicZ440.setNotBankId6(iJcicZ440Log.getNotBankId6());
				iJcicZ440.setTranKey(iJcicZ440Log.getTranKey());
				iJcicZ440.setOutJcicTxtDate(iJcicZ440Log.getOutJcicTxtDate());
				try {
					sJcicZ440Service.update(iJcicZ440, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	public String dealBankName(String iCourtCode,TitaVo titaVo) throws LogicException {
		CdCode tCdCode = new CdCode();
		tCdCode=iCdCodeService.getItemFirst(8, "JcicBankCode", iCourtCode,titaVo);
		String JcicBankName="";//80碼長度
		if(tCdCode!=null) {
			JcicBankName=tCdCode.getItem();
		}
		return JcicBankName;
	}
}