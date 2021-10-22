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
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;
import com.st1.itx.db.domain.JcicZ060Log;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ060LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.JcicZ063Service;
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

@Service("L8318")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8318 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ060LogService sJcicZ060LogService;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8318 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		int iYM = Integer.valueOf(titaVo.getParam("YM"));
		String iKey = "";
		// JcicZ060, JcicZ046, JcicZ047, JcicZ062, JcicZ063
		JcicZ060 iJcicZ060 = new JcicZ060();
		JcicZ060Id iJcicZ060Id = new JcicZ060Id();
		iJcicZ060Id.setSubmitKey(iSubmitKey);
		iJcicZ060Id.setCustId(iCustId);
		iJcicZ060Id.setRcDate(iRcDate);
		iJcicZ060Id.setChangePayDate(iChangePayDate);
		JcicZ060 chJcicZ060 = new JcicZ060();
//		JcicZ062 iJcicZ062 = new JcicZ062();
//		JcicZ062Id iJcicZ062Id = new JcicZ062Id();
//		iJcicZ062Id.setSubmitKey(iSubmitKey);
//		iJcicZ062Id.setCustId(iCustId);
//		iJcicZ062Id.setRcDate(iRcDate);
//		iJcicZ062Id.setChangePayDate(iChangePayDate);
//		JcicZ063 iJcicZ063 = new JcicZ063();
//		JcicZ063Id iJcicZ063Id = new JcicZ063Id();
//		iJcicZ063Id.setSubmitKey(iSubmitKey);
//		iJcicZ063Id.setCustId(iCustId);
//		iJcicZ063Id.setRcDate(iRcDate);
//		iJcicZ063Id.setChangePayDate(iChangePayDate);

		// 檢核項目(D-32)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2 start
			// KEY值(IDN+報送單位代號+原前置協商申請日+申請變更還款條件日)，不能重複，若有重複，且無'63'結案資料，則剔退處理.--->1014會議通知不需檢核
			// 3 本檔案報送日應為每月16-20日，否則予以剔退***1014會議通知不需檢核

			// 5 需檢核最大債權金融機構是否有報送「'47':金融機構無擔保債務協議資料」，且未曾報送「'46':結案通知資料」.--->1014會議通知不需檢核

			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 8 start--->1014會議要再確認(清和)
				// 除已報送'62'資料且「簽約完成日」有值之同一KEY值本檔案資料外，於本次報送本檔案('60')前皆須報送'63'結案(結案原因為A或B)，否則予以剔退.
//				iJcicZ063 = sJcicZ063Service.findById(iJcicZ063Id, titaVo);
//				if (iJcicZ063 == null
//						|| (!"A".equals(iJcicZ063.getClosedResult()) && !"B".equals(iJcicZ063.getClosedResult()))) {
//					iJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id, titaVo);
//					if (iJcicZ062 == null) {
//				if ("A".equals(iTranKey)) {
//						throw new LogicException("E0005", "須先報送(63)變更還款方案結案通知資料，且結案原因為A或B.");
//				}else {
//					throw new LogicException("E0007", "須先報送(63)變更還款方案結案通知資料，且結案原因為A或B.");
//				}
//					} else if (iJcicZ062.getChaRepayEndDate() <= 0) {
//				if ("A".equals(iTranKey)) {
//						throw new LogicException("E0005", "(62)金融機構無擔保債務變更還款條件協議資料之「簽約完成日」不能為空，或者亦可先報送(63)變更還款方案結案通知資料.");
//					}else{
//					throw new LogicException("E0007", "(62)金融機構無擔保債務變更還款條件協議資料之「簽約完成日」不能為空，或者亦可先報送(63)變更還款方案結案通知資料.");
//				}
//				} // 8 end
			}

			// 4 start若交易代碼報送C異動，於進檔時檢查並無此筆資料，視為新增A，不予剔退
			if ("C".equals(iTranKey)) {
				JcicZ060 jJcicZ060 = sJcicZ060Service.ukeyFirst(titaVo.getParam("Ukey"), titaVo);
				if (jJcicZ060 == null) {
					iTranKey_Tmp = "1";
				}
			}
			// 4 end

			// 第7欄「申請變更還款條件日」不得大於當月10日.--->(前端檢核)

			// 7 第8欄「已清分足月期付金年月」需小於等於第7欄「申請變更還款條件日」，否則予以剔退.--->(前端檢核)

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id, titaVo);
			if (chJcicZ060 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}

			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ060 = new JcicZ060();
			iJcicZ060.setJcicZ060Id(iJcicZ060Id);
			iJcicZ060.setUkey(iKey);
			iJcicZ060.setRcDate(iRcDate);
			iJcicZ060.setTranKey(iTranKey);
			iJcicZ060.setSubmitKey(iSubmitKey);
			iJcicZ060.setCustId(iCustId);
			iJcicZ060.setChangePayDate(iChangePayDate);
			iJcicZ060.setYM(iYM);
			try {
				sJcicZ060Service.insert(iJcicZ060, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ060 = sJcicZ060Service.ukeyFirst(iKey, titaVo);
			JcicZ060 uJcicZ060 = new JcicZ060();
			uJcicZ060 = sJcicZ060Service.holdById(iJcicZ060.getJcicZ060Id(), titaVo);
			if (uJcicZ060 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ060.setTranKey(iTranKey);
			uJcicZ060.setYM(iYM);
			uJcicZ060.setOutJcicTxtDate(0);
			JcicZ060 oldJcicZ060 = (JcicZ060) iDataLog.clone(uJcicZ060);
			try {
				sJcicZ060Service.update(uJcicZ060, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ060, uJcicZ060);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id);
			if (iJcicZ060 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ060Log> dJcicLogZ060 = null;
			dJcicLogZ060 = sJcicZ060LogService.ukeyEq(iJcicZ060.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ060 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ060Service.delete(iJcicZ060, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ060Log iJcicZ060Log = dJcicLogZ060.getContent().get(0);
				iJcicZ060.setYM(iJcicZ060Log.getYM());
				iJcicZ060.setTranKey(iJcicZ060Log.getTranKey());
				iJcicZ060.setOutJcicTxtDate(iJcicZ060Log.getOutJcicTxtDate());
				try {
					sJcicZ060Service.update(iJcicZ060, titaVo);
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

}