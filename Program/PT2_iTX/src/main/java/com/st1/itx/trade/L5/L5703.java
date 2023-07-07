package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
/* DB容器 */
import com.st1.itx.db.domain.NegFinAcct;
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegFinShareId;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.service.NegAppr01Service;
/*DB服務*/
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.service.NegMainService;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
 * FinCode=9,3<br>
 * RemitBank=9,3<br>
 * RemitAcct=9,16<br>
 * DataSendSection=9,3<br>
 */

@Service("L5703")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5703 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegFinAcctService sNegFinAcctService;

	/* 日期工具 */
	@Autowired
	DateUtil dDateUtil;

	/* 轉型共用工具 */
	@Autowired
	Parse parse;

	@Autowired
	public DataLog dataLog;
	
	@Autowired
	public NegAppr01Service sNegAppr01Service;
	
	@Autowired
	public NegMainService sNegMainService;
	
	@Autowired
	public NegFinShareService sNegFinShareService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("Run L5703");
		this.info("active L5703 ");

		this.totaVo.init(titaVo);

		String FunCode = titaVo.getParam("FunCode").trim(); // 功能
		String FinCode = titaVo.getParam("FinCode").trim(); // 債權機構
		String FinCodeX = titaVo.getParam("FinCodeX").trim(); // 債權機構名稱
		String RemitBank = titaVo.getParam("RemitBank").trim(); // 匯款銀行
		String RemitAcct = titaVo.getParam("RemitAcct").trim(); // 債協匯款帳號
		String RemitAcct2 = titaVo.getParam("RemitAcct2").trim(); // 調解匯款帳號
		String RemitAcct3 = titaVo.getParam("RemitAcct3").trim(); // 更生匯款帳號
		String RemitAcct4 = titaVo.getParam("RemitAcct4").trim(); // 清算匯款帳號
		String DataSendSection = titaVo.getParam("DataSendSection").trim(); // 資料傳送單位
		String Enable = titaVo.getParam("Enable").trim(); // 啟用記號

		NegFinAcct NegFinAcctVO = new NegFinAcct();

		switch (FunCode) {

		case "1":
			NegFinAcctVO = sNegFinAcctService.findById(FinCode, titaVo);

			if (NegFinAcctVO != null) {
				throw new LogicException(titaVo, "E0002", "債務協商債權機構帳戶檔");
			}

			NegFinAcct tNegFinAcct = new NegFinAcct();
			tNegFinAcct.setFinCode(FinCode);
			tNegFinAcct.setFinItem(FinCodeX);
			tNegFinAcct.setRemitBank(RemitBank);
			tNegFinAcct.setRemitAcct(RemitAcct);
			tNegFinAcct.setRemitAcct2(RemitAcct2);
			tNegFinAcct.setRemitAcct3(RemitAcct3);
			tNegFinAcct.setRemitAcct4(RemitAcct4);
			tNegFinAcct.setDataSendSection(DataSendSection);
			tNegFinAcct.setEnable(Enable);
			tNegFinAcct.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tNegFinAcct.setCreateEmpNo(titaVo.get("TlrNo"));
			tNegFinAcct.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tNegFinAcct.setLastUpdateEmpNo(titaVo.get("TlrNo"));
			try {
				sNegFinAcctService.insert(tNegFinAcct, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				// E0005 新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}

			break;

		case "2":

			NegFinAcctVO = sNegFinAcctService.holdById(FinCode, titaVo);

			if (NegFinAcctVO == null) {
				throw new LogicException(titaVo, "E0003", "債務協商債權機構帳戶檔");
			}
			NegFinAcct beforeNegFinAcct = (NegFinAcct) dataLog.clone(NegFinAcctVO);
			NegFinAcctVO.setFinItem(FinCodeX);
			NegFinAcctVO.setRemitBank(RemitBank);
			NegFinAcctVO.setRemitAcct(RemitAcct);
			NegFinAcctVO.setRemitAcct2(RemitAcct2);
			NegFinAcctVO.setRemitAcct3(RemitAcct3);
			NegFinAcctVO.setRemitAcct4(RemitAcct4);
			NegFinAcctVO.setDataSendSection(DataSendSection);
			NegFinAcctVO.setEnable(Enable);
			//NegFinAcctVO.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			//NegFinAcctVO.setLastUpdateEmpNo(titaVo.get("TlrNo"));

			try {
				sNegFinAcctService.update(NegFinAcctVO, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				// E0007 更新資料時，發生錯誤
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			dataLog.setEnv(titaVo, beforeNegFinAcct,NegFinAcctVO);
			dataLog.exec("修改債務協商債權機構帳戶檔");
			updAppr01(NegFinAcctVO, beforeNegFinAcct, titaVo);// 同步維護未製檔資料-最大債權撥付檔
			
			break;

		case "4":

			NegFinAcctVO = sNegFinAcctService.holdById(FinCode, titaVo);

			if (NegFinAcctVO == null) {
				throw new LogicException(titaVo, "E0004", "債務協商債權機構帳戶檔");
			}
			// 檢查是否有戶況正常資料
			chkstatus(FinCode, titaVo);
			
			try {
				sNegFinAcctService.delete(NegFinAcctVO, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			dataLog.setEnv(titaVo, NegFinAcctVO,NegFinAcctVO);
			dataLog.exec("刪除債務協商債權機構帳戶檔");

			break;

		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void chkstatus(String FinCode,TitaVo titaVo) throws LogicException {
		// 刪除前檢查是否有債權機構的債協主檔戶況正常之資料
		Slice<NegMain> slNegMain = sNegMainService.statusEq("0", 0, Integer.MAX_VALUE, titaVo);
		List<NegMain> lNegMain = slNegMain == null ? null : slNegMain.getContent();

		if (lNegMain == null) {
			return;
		}
		for (NegMain tNegMain : lNegMain) {

			NegFinShareId NegFinShareId = new NegFinShareId();
			NegFinShare NegFinShare = new NegFinShare();
			NegFinShareId.setCaseSeq(tNegMain.getCaseSeq());
			NegFinShareId.setCustNo(tNegMain.getCustNo());
			NegFinShareId.setFinCode(FinCode);
			NegFinShare.setNegFinShareId(NegFinShareId);
			NegFinShare NegFinShareVO = sNegFinShareService.findById(NegFinShareId, titaVo);
			if (NegFinShareVO != null) {
				throw new LogicException(titaVo, "E5009", "尚有債協主檔戶況正常之資料,不可刪除");
			}
		}		
	}
	
	public void updAppr01(NegFinAcct NegFinAcctVO,NegFinAcct beforeNegFinAcct,TitaVo titaVo) throws LogicException {
		boolean changefg = false;
		// 無須異動匯款銀行與帳號
		if (NegFinAcctVO.getRemitBank() == beforeNegFinAcct.getRemitBank()
				&& NegFinAcctVO.getRemitAcct() == beforeNegFinAcct.getRemitAcct()
				&& NegFinAcctVO.getRemitAcct2() == beforeNegFinAcct.getRemitAcct2()
				&& NegFinAcctVO.getRemitAcct3() == beforeNegFinAcct.getRemitAcct3()
				&& NegFinAcctVO.getRemitAcct4() == beforeNegFinAcct.getRemitAcct4()) {
			return;
		}
		
		String tRemitAcct1 = NegFinAcctVO.getRemitAcct().trim();// 1:債協匯款帳號
		String tRemitAcct2 = NegFinAcctVO.getRemitAcct2().trim();// 2:調解匯款帳號
		String tRemitAcct3 = NegFinAcctVO.getRemitAcct3().trim();// 3:更生匯款帳號
		String tRemitAcct4 = NegFinAcctVO.getRemitAcct4().trim();// 4:清算匯款帳號
		String tRemitAcct = tRemitAcct1;// 匯款帳號預設為債協匯款帳號
		String tRemitBank = beforeNegFinAcct.getRemitBank();// 原始匯款銀行

		Slice<NegAppr01> slNegAppr01 = sNegAppr01Service.findExporFinCode(0, NegFinAcctVO.getFinCode(),0, Integer.MAX_VALUE, titaVo);// 找未製檔資料
		List<NegAppr01> lNegAppr01 = slNegAppr01 == null ? null : slNegAppr01.getContent();
		NegAppr01 tNegAppr01 = new NegAppr01();

		if (lNegAppr01 != null) {
			for (NegAppr01 cNegAppr01 : lNegAppr01) {
				if (!cNegAppr01.getFinCode().equals(NegFinAcctVO.getFinCode())) {
					continue;
				}
				// 判斷匯款帳號是否有變更
				String tCaseKindCode = cNegAppr01.getCaseKindCode();
				if (("2").equals(tCaseKindCode)) {// 調解
					if (tRemitAcct2 != null && tRemitAcct2.length() != 0 && !("0000000000000000").equals(tRemitAcct2)) {
						tRemitAcct = tRemitAcct2;
					}
				}
				if (("3").equals(tCaseKindCode) || ("4").equals(tCaseKindCode)) {// 更生與清算
					if (tRemitAcct3 != null && tRemitAcct3.length() != 0 && !("0000000000000000").equals(tRemitAcct3)) {
						tRemitAcct = tRemitAcct3;
					}
					if (("4").equals(tCaseKindCode)) {// 清算
						if (tRemitAcct4 != null && tRemitAcct4.length() != 0
								&& !("0000000000000000").equals(tRemitAcct4)) {
							tRemitAcct = tRemitAcct4;
						}
					}
				}
				if (!tRemitAcct.equals(cNegAppr01.getRemitAcct())) {
					changefg = true;
					if (tRemitAcct == null || tRemitAcct.length() == 0 || ("0000000000000000").equals(tRemitAcct)) {
						throw new LogicException(titaVo, "E5009",
								"匯款銀行" + NegFinAcctVO.getRemitBank() + "的匯款帳號有誤:" + tRemitAcct + ",尚有未製檔資料,匯款帳號不可為0");
					}
				}
				// 判斷匯款銀行是否有變更
				if (NegFinAcctVO.getRemitBank() != beforeNegFinAcct.getRemitBank()) {
					changefg = true;
					tRemitBank = NegFinAcctVO.getRemitBank();
				}
				// 同步變更匯款資料
				if (changefg) {
					tNegAppr01 = sNegAppr01Service.holdById(cNegAppr01.getNegAppr01Id(), titaVo);
					// NegAppr01 beforeNegAppr01 = (NegAppr01) dataLog.clone(tNegAppr01);
					tNegAppr01.setRemitBank(tRemitBank);
					tNegAppr01.setRemitAcct(tRemitAcct);
					try {
						tNegAppr01 = sNegAppr01Service.update(tNegAppr01, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "最大債權撥付資料檔");
					}
					// dataLog.setEnv(titaVo, beforeNegAppr01, tNegAppr01);
					// dataLog.exec("修改最大債權撥付資料檔-未製檔戶號" + cNegAppr01.getCustNo() + "債權機構" +
					// cNegAppr01.getFinCode()
					// + "會計日期" + cNegAppr01.getAcDate());
				}
			}
		}

	}
}