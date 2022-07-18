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
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Id;
import com.st1.itx.db.domain.JcicZ061Log;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ061LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ061Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8319")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8319 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ054Service sJcicZ054Service;
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ061LogService sJcicZ061LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8319 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		int iExpBalanceAmt = Integer.valueOf(titaVo.getParam("ExpBalanceAmt"));
		int iCashBalanceAmt = Integer.valueOf(titaVo.getParam("CashBalanceAmt"));
		int iCreditBalanceAmt = Integer.valueOf(titaVo.getParam("CreditBalanceAmt"));
		String iMaxMainNote = titaVo.getParam("MaxMainNote");
		String iIsGuarantor = titaVo.getParam("IsGuarantor");
		String iIsChangePayment = titaVo.getParam("IsChangePayment");
		String iKey = "";
		// JcicZ061, JcicZ042, JcicZ046, JcicZ052, JcicZ054, JcicZ060
		JcicZ061 iJcicZ061 = new JcicZ061();
		JcicZ061Id iJcicZ061Id = new JcicZ061Id();
		iJcicZ061Id.setSubmitKey(iSubmitKey);
		iJcicZ061Id.setCustId(iCustId);
		iJcicZ061Id.setRcDate(iRcDate);
		iJcicZ061Id.setChangePayDate(iChangePayDate);
		iJcicZ061Id.setMaxMainCode(iMaxMainCode);
		JcicZ061 chJcicZ061 = new JcicZ061();
		JcicZ060 iJcicZ060 = new JcicZ060();
		JcicZ060Id iJcicZ060Id = new JcicZ060Id();
		iJcicZ060Id.setSubmitKey(iSubmitKey);
		iJcicZ060Id.setCustId(iCustId);
		iJcicZ060Id.setRcDate(iRcDate);
		iJcicZ060Id.setChangePayDate(iChangePayDate);

		// 檢核項目(D-34)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2 本檔案除無擔保債權協辦行需報送外，主辦行無論有無無擔保債權變須報送本檔案資料.(99.11.18)***

			// 3 start
			// KEY值(IDN+報送單位代號+協商申請日+申請變更還款條件日)，若KEY值未曾報送過「'60':前置協商受理申請變更還款暨請求回報剩餘債權通知」，予以剔退處理.
			iJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id, titaVo);
			if (iJcicZ060 == null) {
				if ("A".equals(iTranKey)) {
					throw new LogicException("E0005", "KEY值(IDN+報送單位代號+原前置協商申請日+申請變更還款條件日)未曾報送過(60)前置協商受理申請變更還款暨請求回報剩餘債權通知資料.");
				} else {
					throw new LogicException("E0007", "KEY值(IDN+報送單位代號+原前置協商申請日+申請變更還款條件日)未曾報送過(60)前置協商受理申請變更還款暨請求回報剩餘債權通知資料.");
				}
			} // 3 end

			// 4 --->1014會議通知不需檢核
			// 除例外處理(報送過「'52':前置協商相關資料報送例外處理」且補報送檔案格式資料別為'61'),本檔案報送日不得超逾最大債權金融機構'60'資料報送日+3個營業日.

			// 5 start
			// 檢核報送單位代號+債務人IDN+協商申請日曾報送「'42':回報無擔保債權金額資料」，且未報送「'46':結案通知資料」及「'54':單獨全數受清償資料」.
			//@@@function 要改为：custRcSubEq
			Slice<JcicZ042> sJcicZ042 = sJcicZ042Service.custRcEq(iCustId, iRcDate + 19110000, 0, Integer.MAX_VALUE, titaVo);
			if (sJcicZ042 == null) {
				if ("A".equals(iTranKey)) {
					throw new LogicException("E0005", "「報送單位代號+債務人IDN+協商申請日」未曾報送過(42)回報無擔保債權金額資料.");
				} else {
					throw new LogicException("E0007", "「報送單位代號+債務人IDN+協商申請日」未曾報送過(42)回報無擔保債權金額資料.");
				}
			}
			if ("A".equals(iTranKey)) {
				Slice<JcicZ046> sJcicZ046 = sJcicZ046Service.hadZ046(iCustId, iRcDate + 19110000, iSubmitKey, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ046 != null) {
					int sTranKey = 0;
					for (JcicZ046 xJcicZ046 : sJcicZ046) {
						if (!"D".equals(xJcicZ046.getTranKey())) {
							sTranKey = 1;
						}
					}
					if (sTranKey == 1) {
						throw new LogicException("E0005", "「報送單位代號+債務人IDN+協商申請日」已報送過(46)結案通知資料.");
					}
				}
				//@@@function 要改为：custRcSubEq
				Slice<JcicZ054> sJcicZ054 = sJcicZ054Service.custRcEq(iCustId, iRcDate + 19110000, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ054 != null) {
					throw new LogicException("E0005", "「報送單位代號+債務人IDN+協商申請日」已報送過(54)單獨全數受清償資料.");
				}
			}
			// 5 end

			// 6
			// 第八欄「變更還款條件已履約期數」所填報值需大於或等於有效變更還款條件次數(「'62':金融機構無擔保債務變更還款條件協議資料」第17欄「簽約完成日」有值筆數)*24，否則予以剔退.***
			// 6~10非屬本表檢核條件***

			// 11
			// 第12欄「最大債權金融機構報送註記」報送'Y'時，檢核第8欄「最大債權金融機構代號」與檔頭資料之「報送單位代號」是否一致，不一致者予以剔退--->(前端檢核)

			// 12 start--->(前端檢核)
			// 第12欄「最大債權金融機構報送註記」報送'Y'時，第13欄「是否有保證人」需為空白且第14欄「是否同意債務人申請變更還款條件方案」需填報Y，否則剔退.
		}
		// 檢核項目 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ061 = sJcicZ061Service.findById(iJcicZ061Id, titaVo);
			if (chJcicZ061 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}

			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ061.setJcicZ061Id(iJcicZ061Id);
			iJcicZ061.setTranKey(iTranKey);
			iJcicZ061.setUkey(iKey);
			iJcicZ061.setExpBalanceAmt(iExpBalanceAmt);
			iJcicZ061.setCashBalanceAmt(iCashBalanceAmt);
			iJcicZ061.setCreditBalanceAmt(iCreditBalanceAmt);
			iJcicZ061.setMaxMainNote(iMaxMainNote);
			iJcicZ061.setIsGuarantor(iIsGuarantor);
			iJcicZ061.setIsChangePayment(iIsChangePayment);
			iJcicZ061.setUkey(iKey);
			try {
				sJcicZ061Service.insert(iJcicZ061, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ061 = sJcicZ061Service.ukeyFirst(iKey, titaVo);
			JcicZ061 uJcicZ061 = new JcicZ061();
			uJcicZ061 = sJcicZ061Service.holdById(iJcicZ061.getJcicZ061Id(), titaVo);
			if (uJcicZ061 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ061 oldJcicZ061 = (JcicZ061) iDataLog.clone(uJcicZ061);
			uJcicZ061.setTranKey(iTranKey);
			uJcicZ061.setExpBalanceAmt(iExpBalanceAmt);
			uJcicZ061.setCashBalanceAmt(iCashBalanceAmt);
			uJcicZ061.setCreditBalanceAmt(iCreditBalanceAmt);
			uJcicZ061.setMaxMainNote(iMaxMainNote);
			uJcicZ061.setIsGuarantor(iIsGuarantor);
			uJcicZ061.setIsChangePayment(iIsChangePayment);
			uJcicZ061.setOutJcicTxtDate(0);
			try {
				sJcicZ061Service.update(uJcicZ061, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ061, uJcicZ061);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ061 = sJcicZ061Service.ukeyFirst(iKey, titaVo);
			JcicZ061 uJcicZ0612 = new JcicZ061();
			uJcicZ0612 = sJcicZ061Service.holdById(iJcicZ061.getJcicZ061Id(), titaVo);
			iJcicZ061 = sJcicZ061Service.findById(iJcicZ061Id);
			if (iJcicZ061 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ061 oldJcicZ0612 = (JcicZ061) iDataLog.clone(uJcicZ0612);
			uJcicZ0612.setTranKey(iTranKey);
			uJcicZ0612.setExpBalanceAmt(iExpBalanceAmt);
			uJcicZ0612.setCashBalanceAmt(iCashBalanceAmt);
			uJcicZ0612.setCreditBalanceAmt(iCreditBalanceAmt);
			uJcicZ0612.setMaxMainNote(iMaxMainNote);
			uJcicZ0612.setIsGuarantor(iIsGuarantor);
			uJcicZ0612.setIsChangePayment(iIsChangePayment);
			uJcicZ0612.setOutJcicTxtDate(0);
			
			Slice<JcicZ061Log> dJcicLogZ061 = null;
			dJcicLogZ061 = sJcicZ061LogService.ukeyEq(iJcicZ061.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ061 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ061Service.delete(iJcicZ061, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ061Log iJcicZ061Log = dJcicLogZ061.getContent().get(0);
				iJcicZ061.setExpBalanceAmt(iJcicZ061Log.getExpBalanceAmt());
				iJcicZ061.setCashBalanceAmt(iJcicZ061Log.getCashBalanceAmt());
				iJcicZ061.setCreditBalanceAmt(iJcicZ061Log.getCreditBalanceAmt());
				iJcicZ061.setMaxMainNote(iJcicZ061Log.getMaxMainNote());
				iJcicZ061.setIsGuarantor(iJcicZ061Log.getIsGuarantor());
				iJcicZ061.setIsChangePayment(iJcicZ061Log.getIsChangePayment());
				iJcicZ061.setTranKey(iJcicZ061Log.getTranKey());
				iJcicZ061.setOutJcicTxtDate(iJcicZ061Log.getOutJcicTxtDate());
				try {
					sJcicZ061Service.update(iJcicZ061, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0612, uJcicZ0612);
			iDataLog.exec();
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}