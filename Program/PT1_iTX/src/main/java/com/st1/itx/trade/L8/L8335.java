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
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Id;
import com.st1.itx.db.domain.JcicZ573Log;
import com.st1.itx.db.service.JcicZ573LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ573Service;
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

@Service("L8335")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8335 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ573Service sJcicZ573Service;
	@Autowired
	public JcicZ573LogService sJcicZ573LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8335 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		int iPayDate = Integer.valueOf(titaVo.getParam("PayDate"));
		int iPayAmt = Integer.valueOf(titaVo.getParam("PayAmt"));
		int iTotalPayAmt = Integer.valueOf(titaVo.getParam("TotalPayAmt"));
		String iKey = "";
		// JcicZ573
		JcicZ573 iJcicZ573 = new JcicZ573();
		JcicZ573Id iJcicZ573Id = new JcicZ573Id();
		iJcicZ573Id.setApplyDate(iApplyDate);
		iJcicZ573Id.setPayDate(iPayDate);
		iJcicZ573Id.setCustId(iCustId);
		iJcicZ573Id.setSubmitKey(iSubmitKey);
		JcicZ573 chJcicZ573 = new JcicZ573();

		// 檢核項目(D-76)
		if (!"4".equals(iTranKey_Tmp)) {

			// 二 start key值為「債務人IDN+報送單位代號+申請日期+繳款日期」，不可重複，重複者予以剔退--->檢核在case "1"
			// 三 同一更生款項統一收付案件尚未報送572檔案資料且已報送574結案資料者，予以剔退處裡--->1014會議通知不需檢核

			// 四start 若累計繳款金額不等於該IDN所有已報送之繳款金額(含今日)，予以剔退處裡
			if (!"D".equals(iTranKey)) {
				int sPayAmt = 0;// 累計繳款金額
				Slice<JcicZ573> sJcicZ573 = sJcicZ573Service.custIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ573 == null) {
					if (iPayAmt != iTotalPayAmt) {
						if ("A".equals(iTranKey)) {
							throw new LogicException(titaVo, "E0005", "[累計繳款金額]不等於該IDN所有已報送之[本日繳款金額]合計(含今日)");
						} else {
							throw new LogicException(titaVo, "E0007", "[累計繳款金額]不等於該IDN所有已報送之[本日繳款金額]合計(含今日)");
						}
					}
				} else {
					for (JcicZ573 xJcicZ573 : sJcicZ573) {
						if (!"D".equals(xJcicZ573.getTranKey()) && !titaVo.getParam("Ukey").equals(xJcicZ573.getUkey())) {
							sPayAmt += xJcicZ573.getPayAmt();
						}
					}
				}
				if ((sPayAmt + iPayAmt) != iTotalPayAmt) {
					if ("A".equals(iTranKey)) {
						throw new LogicException(titaVo, "E0005", "[累計繳款金額]不等於該IDN所有已報送之[本日繳款金額]合計(含今日)");
					} else {
						throw new LogicException(titaVo, "E0007", "[累計繳款金額]不等於該IDN所有已報送之[本日繳款金額]合計(含今日)");
					}
				} // 四end
			}
		}

		// 檢核項目end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ573 = sJcicZ573Service.findById(iJcicZ573Id, titaVo);
			if (chJcicZ573 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ573.setJcicZ573Id(iJcicZ573Id);
			iJcicZ573.setTranKey(iTranKey);
			iJcicZ573.setPayAmt(iPayAmt);
			iJcicZ573.setTotalPayAmt(iTotalPayAmt);
			iJcicZ573.setUkey(iKey);
			try {
				sJcicZ573Service.insert(iJcicZ573, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債務人繳款資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ573 = sJcicZ573Service.ukeyFirst(iKey, titaVo);
			JcicZ573 uJcicZ573 = new JcicZ573();
			uJcicZ573 = sJcicZ573Service.holdById(iJcicZ573.getJcicZ573Id(), titaVo);
			if (uJcicZ573 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ573.setTranKey(iTranKey);
			uJcicZ573.setPayAmt(iPayAmt);
			uJcicZ573.setTotalPayAmt(iTotalPayAmt);
			uJcicZ573.setOutJcicTxtDate(0);
			JcicZ573 oldJcicZ573 = (JcicZ573) iDataLog.clone(uJcicZ573);
			try {
				sJcicZ573Service.update(uJcicZ573, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債務人繳款資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ573, uJcicZ573);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ573 = sJcicZ573Service.findById(iJcicZ573Id);
			if (iJcicZ573 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ573Log> dJcicLogZ573 = null;
			dJcicLogZ573 = sJcicZ573LogService.ukeyEq(iJcicZ573.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			// 最近一筆之資料
			if (dJcicLogZ573 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ573Service.delete(iJcicZ573, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ573Log iJcicZ573Log = dJcicLogZ573.getContent().get(0);
				iJcicZ573.setPayAmt(iJcicZ573Log.getPayAmt());
				iJcicZ573.setTotalPayAmt(iJcicZ573Log.getTotalPayAmt());
				iJcicZ573.setTranKey(iJcicZ573Log.getTranKey());
				iJcicZ573.setOutJcicTxtDate(iJcicZ573Log.getOutJcicTxtDate());
				try {
					sJcicZ573Service.update(iJcicZ573, titaVo);
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