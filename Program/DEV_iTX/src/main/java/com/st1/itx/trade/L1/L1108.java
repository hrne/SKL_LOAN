package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1108")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1108 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustNoticeService sCustNoticeService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1108 ");
		this.totaVo.init(titaVo);

		// 取tita功能 1:新增 2:修改
		String funcd = titaVo.getParam("FunCd").trim();

		// 取tita戶號先確定是否已在額度主檔測試
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// 取tita額度號碼
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		FacMain tFacMain = new FacMain();
		String iFormNo;
		// 若額度號碼欄位輸入0時,寫一組額度為0的資料,作為此戶號的主要通知設定.
		// 額度號碼欄位輸入值不為0時,需檢查是否存在於額度主檔
		if (iFacmNo != 0) {
			FacMainId facMainId = new FacMainId();

			facMainId.setCustNo(iCustNo);
			facMainId.setFacmNo(iFacmNo);
			this.info("L1108 有輸入額度號碼,檢查此額度號碼是否存在 = " + facMainId.toString());

			tFacMain = sFacMainService.findById(facMainId);

			if (tFacMain == null) {
				throw new LogicException("E0001", "額度主檔");
			}
		}

		if (funcd.equals("1") || funcd.equals("3")) {
			for (int i = 1; i <= 40; i++) {
				if (titaVo.getParam("FormNo" + i).equals("")) {
					continue;
				}
				String VarPaper = "N";
				String VarMsg = "N";
				String VarEMail = "N";

				// FormNo報表代號
				iFormNo = titaVo.getParam("FormNo" + i);
				this.info("iFormNo" + i + " = " + iFormNo);
				if (!iFormNo.equals("")) {
					if ("".equals(titaVo.getParam("Paper" + i))) {
						VarPaper = "Y";
					}
					if ("".equals(titaVo.getParam("Msg" + i))) {
						VarMsg = "Y";
					}
					if ("".equals(titaVo.getParam("EMail" + i))) {
						VarEMail = "Y";
					}

					int ApplyDt = parse.stringToInteger(titaVo.getParam("ApplyDt"));

					CustNotice tCustNotice = new CustNotice();
					CustNoticeId tCustNoticePK = new CustNoticeId();

					tCustNoticePK.setCustNo(iCustNo);
					tCustNoticePK.setFacmNo(iFacmNo);
					tCustNoticePK.setFormNo(iFormNo);
					this.info("i = " + i);
					this.info("tCustNoticePK = " + tCustNoticePK);

					tCustNotice.setCustNoticeId(tCustNoticePK);

					tCustNotice.setCustNo(iCustNo);
					tCustNotice.setFacmNo(iFacmNo);
					tCustNotice.setFormNo(iFormNo);

					tCustNotice.setPaperNotice(VarPaper);
					tCustNotice.setMsgNotice(VarMsg);
					tCustNotice.setEmailNotice(VarEMail);
					tCustNotice.setApplyDate(ApplyDt);
					this.info("tCustNotice = " + tCustNotice);

					try {
						sCustNoticeService.insert(tCustNotice, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "客戶通知設定檔");
					}
				}
			}
		} else if (funcd.equals("2")) {
			for (int i = 1; i <= 40; i++) {
				if (titaVo.getParam("FormNo" + i).equals("")) {
					continue;
				}
				String VarPaper = "N";
				String VarMsg = "N";
				String VarEMail = "N";

				// FormNo報表代號
				iFormNo = titaVo.getParam("FormNo" + i);

				if ("".equals(titaVo.getParam("Paper" + i))) {
					VarPaper = "Y";
				}
				if ("".equals(titaVo.getParam("Msg" + i))) {
					VarMsg = "Y";
				}
				if ("".equals(titaVo.getParam("EMail" + i))) {
					VarEMail = "Y";
				}

				int ApplyDt = parse.stringToInteger(titaVo.getParam("ApplyDt"));

				CustNotice tCustNotice = new CustNotice();
				CustNoticeId tCustNoticePK = new CustNoticeId();

				tCustNoticePK.setCustNo(iCustNo);
				tCustNoticePK.setFacmNo(iFacmNo);
				tCustNoticePK.setFormNo(iFormNo);

				tCustNotice = sCustNoticeService.holdById(tCustNoticePK);
				// 維護時的新資料改為新增
				if (tCustNotice == null) {
					CustNotice xCustNotice = new CustNotice();
					CustNoticeId xCustNoticePK = new CustNoticeId();

					xCustNoticePK.setCustNo(iCustNo);
					xCustNoticePK.setFacmNo(iFacmNo);
					xCustNoticePK.setFormNo(iFormNo);
					this.info("i = " + i);
					this.info("tCustNoticePK = " + tCustNoticePK);
					xCustNotice.setCustNoticeId(xCustNoticePK);
					xCustNotice.setPaperNotice(VarPaper);
					xCustNotice.setMsgNotice(VarMsg);
					xCustNotice.setEmailNotice(VarEMail);
					xCustNotice.setApplyDate(ApplyDt);

					try {
						sCustNoticeService.insert(xCustNotice, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "客戶通知設定檔");
					}
				} else {
					// 變更前
					CustNotice beforeCustNotice = (CustNotice) dataLog.clone(tCustNotice);
					tCustNotice.setPaperNotice(VarPaper);
					tCustNotice.setMsgNotice(VarMsg);
					tCustNotice.setEmailNotice(VarEMail);
					tCustNotice.setApplyDate(ApplyDt);

					try {
						tCustNotice = sCustNoticeService.update2(tCustNotice, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "客戶通知設定檔");
					}

					// 紀錄變更前變更後
					dataLog.setEnv(titaVo, beforeCustNotice, tCustNotice);
					dataLog.exec();
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
