package com.st1.itx.trade.L8;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.domain.JcicZ046Log;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.service.JcicZ046LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ051Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;

/**
 * Tita<br>
 * TranKey=X,1<br>
 * CustId=X,10<br>
 * SubmitKey=X,10<br>
 * CaseStatus=X,1<br>
 * ClaimDate=9,7<br>
 * CourtCode=X,3<br>
 * Year=9,3<br>
 * CourtDiv=X,8<br>
 * CourtCaseNo=X,80<br>
 * Approve=X,1<br>
 * OutstandAmt=9,9<br>
 * ClaimStatus1=X,1<br>
 * SaveDate=9,7<br>
 * ClaimStatus2=X,1<br>
 * SaveEndDate=9,7<br>
 * SubAmt=9,9<br>
 * AdminName=X,20<br>
 * OutJcicTxtDate=9,7<br>
 */

@Service("L8307")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8307 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ046LogService sJcicZ046LogService;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public DateUtil iDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8307 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate"));
		String iCloseCode = titaVo.getParam("CloseCode");
		String iBreakCode = titaVo.getParam("BreakCode");
		String iKey = "";

		// JcicZ046, JcicZ040
		JcicZ046 iJcicZ046 = new JcicZ046();
		JcicZ046Id iJcicZ046Id = new JcicZ046Id();
		iJcicZ046Id.setCustId(iCustId);// 債務人IDN
		iJcicZ046Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ046Id.setRcDate(iRcDate);
		iJcicZ046Id.setCloseDate(iCloseDate);
		JcicZ046 chJcicZ046 = new JcicZ046();
		JcicZ047 iJcicZ047 = new JcicZ047();
		JcicZ047Id iJcicZ047Id = new JcicZ047Id();
		iJcicZ047Id.setCustId(iCustId);// 債務人IDN
		iJcicZ047Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ047Id.setRcDate(iRcDate);

		// Date計算
		int txDate = Integer.valueOf(titaVo.getEntDy());// 會計日 民國年YYYMMDD (本檔案報送日期)

		// 檢核項目(D-15)
		if (!"4".equals(iTranKey_Tmp)) {
	
			// 1.3 結案日期不可早於協商申請日,也不可晚於報送本檔案日期。--->(前端檢核)				
			// 1.4.1 結案原因代號為協商不成立或毀諾案件者，不能再度申請前置協商---->1014會議通知不需檢核
			// 1.4.2 結案原因代號為視同未請求協商案件，且結案未滿180天，不能再度申請前置協商--->1014會議通知不需檢核

			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 1.5 start 報送'47':金融機構無擔保債務協議資料簽約完成後，本檔案結案原因代號僅能報送00，01，99
				String[] acceptCloseCode = { "00", "01", "99" }; // '47'無擔保債務協議資料簽約完成後，可報送的結案原因代號
				if (!Arrays.stream(acceptCloseCode).anyMatch(iCloseCode::equals)) {
					iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
					if (iJcicZ047 != null && !"D".equals(iJcicZ047.getTranKey())) {
						if (iJcicZ047.getSignDate() > 0 && iJcicZ047.getSignDate() <= txDate) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "金融機構無擔保債務協議資料已經簽約完成，本檔案結案原因代號僅能報送00，01或99.");
							} else {
								throw new LogicException("E0007", "金融機構無擔保債務協議資料已經簽約完成，本檔案結案原因代號僅能報送00，01或99.");
							}
						}
					}
				} // 1.5 end

				// 1.6 start 同一key值於'51':延期繳款(喘息期)期間不可報送'00'毀諾
				if ("00".equals(iCloseCode)) {
					Slice<JcicZ051> sJcicZ051 = sJcicZ051Service.SubCustRcEq(iCustId, iRcDate + 19110000, iSubmitKey, 0,
							Integer.MAX_VALUE, titaVo);
					if (sJcicZ051 != null) {
						int sDelayYM = 0;// 最晚「延期繳款年月」
						for (JcicZ051 xJcicZ051 : sJcicZ051) {
							if (!"D".equals(xJcicZ051.getTranKey()) && xJcicZ051.getDelayYM() > sDelayYM) {
								sDelayYM = xJcicZ051.getDelayYM();
							}
						}
						// 日期格式不一致， xJcicZ051.getDelayYM()是YYYMM，日期設31-->不合理，但不影響檢核
						int formateDelayYM = Integer.parseInt(sDelayYM + "31");
						if (txDate <= formateDelayYM) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "於(51)延期繳款(喘息期)期間(" + sDelayYM + "前)不可報送'00'毀諾.");
							} else {
								throw new LogicException("E0007", "於(51)延期繳款(喘息期)期間(" + sDelayYM + "前)不可報送'00'毀諾.");
							}
						}
					}
				}// 1.6 end
			} 

			// 1.7
			// 報送本結案檔後，同一KEY值不可再報送相關檔案之異動。若前述相關檔案之交易代碼為'X'補件者，及報送'49':債務清償方案法院認可資料則不在此限.***J

			// 2.若第2欄「交易代碼」無D刪除功能者，如有資料key值報送錯誤情形者，需以本檔案格式報送第8欄「結案原因代號」'97':資料key值報送錯誤.***J

			// 3 start 第8欄「結案原因代號」為11~19，21、49時，必須同時填報'44'及'48',否則則予以剔退***--->1014會議通知不需檢核

			// 4 start 'D'刪除僅限毀諾資料，且刪除需為結案日當月
			if ("D".equals(iTranKey)) {
				if (!"00".equals(iCloseCode)) {
					throw new LogicException("E0007", "'D'刪除功能僅限毀諾資料.");
				}
				if (GetRocYYYMM(iCloseDate) != GetRocYYYMM(txDate)) {
					throw new LogicException("E0007", "'D'刪除毀諾資料需在結案日當月.");
				}
			} // 4 end

			// 5 「結案原因代號」為'00':毀諾，則第6欄則「毀諾原因代號」不能為空，且必須為01~07--->(前端檢核)
			// 6 「結案原因代號」非'00':毀諾，則第6欄則「毀諾原因代號」必須為空白--->(前端檢核)
			// 檢核項目end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ046
			chJcicZ046 = sJcicZ046Service.findById(iJcicZ046Id, titaVo);
			if (chJcicZ046 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ046.setJcicZ046Id(iJcicZ046Id);
			iJcicZ046.setTranKey(iTranKey);
			iJcicZ046.setCloseCode(iCloseCode);
			iJcicZ046.setBreakCode(iBreakCode);
			iJcicZ046.setUkey(iKey);
			try {
				sJcicZ046Service.insert(iJcicZ046, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ046 = sJcicZ046Service.ukeyFirst(iKey, titaVo);
			JcicZ046 uJcicZ046 = new JcicZ046();
			uJcicZ046 = sJcicZ046Service.holdById(iJcicZ046.getJcicZ046Id(), titaVo);
			if (uJcicZ046 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ046.setTranKey(iTranKey);
			uJcicZ046.setCloseCode(iCloseCode);
			uJcicZ046.setBreakCode(iBreakCode);
			uJcicZ046.setOutJcicTxtDate(0);
			JcicZ046 oldJcicZ046 = (JcicZ046) iDataLog.clone(uJcicZ046);
			try {
				sJcicZ046Service.update(uJcicZ046, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ046, uJcicZ046);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ046 = sJcicZ046Service.findById(iJcicZ046Id);
			if (iJcicZ046 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ046Log> dJcicLogZ046 = null;
			dJcicLogZ046 = sJcicZ046LogService.ukeyEq(iJcicZ046.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ046 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ046Service.delete(iJcicZ046, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ046Log iJcicZ046Log = dJcicLogZ046.getContent().get(0);
				iJcicZ046.setCloseCode(iJcicZ046Log.getCloseCode());
				iJcicZ046.setBreakCode(iJcicZ046Log.getBreakCode());
				iJcicZ046.setTranKey(iJcicZ046Log.getTranKey());
				iJcicZ046.setOutJcicTxtDate(iJcicZ046Log.getOutJcicTxtDate());
				try {
					sJcicZ046Service.update(iJcicZ046, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private int GetRocYYYMM(int rocDate) {
		String strFormate = "000000";
		DecimalFormat df = new DecimalFormat(strFormate);
		String rocDateFormate = df.format(rocDate);
		String monFormate = rocDateFormate.substring(0, 5);
		return Integer.parseInt(monFormate);
	}
}
