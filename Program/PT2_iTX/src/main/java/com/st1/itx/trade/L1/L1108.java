package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
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
	public CustMainService sCustMainService;

	@Autowired
	public CustNoticeService sCustNoticeService;

	@Autowired
	public CustTelNoService sCustTelNoService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public CdReportService sCdReportService;

	/* 轉換工具 */
	@Autowired
	public Parse iParse;

	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1108 ");
		this.totaVo.init(titaVo);

		// 取tita功能 1:新增 2:修改
		String funcd = titaVo.getParam("FunCd").trim();

		// 取tita戶號先確定是否已在額度主檔測試
		int iCustNo = iParse.stringToInteger(titaVo.getParam("CustNo"));

		// 取tita額度號碼
		int iFacmNo = iParse.stringToInteger(titaVo.getParam("FacmNo"));

		CustMain custMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);

		if (custMain == null) {
			throw new LogicException("E0001", "客戶資料主檔");
		}

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
//				String VarPaper = "N";
//				String VarMsg = "N";
//				String VarEMail = "N";
				String VarPaper = "";
				String VarMsg = "";
				String VarEMail = "";
				String uKey = custMain.getCustUKey();

				Slice<CustTelNo> tCustTelNo = sCustTelNoService.findCustUKey(uKey, 0, Integer.MAX_VALUE, titaVo);
				// CustUkey 接電話跟客戶主檔 要去判斷有無對應的該編號(05) 然後去看是否有簡訊
				// 有UKEY 但是要從L1905的資料裡面抓出對應編號

				boolean ixy = titaVo.getParam("Msg" + i).trim().isEmpty();
				Slice<CustTelNo> tCustTelNo1 = sCustTelNoService.findCustUKey(uKey, 0, Integer.MAX_VALUE, titaVo);
				CdReport cdReport3 = sCdReportService.findById(titaVo.getParam("FormNo" + i), titaVo);
				String icdEFg = cdReport3.getEmailFg();
				String iMsgFg = cdReport3.getMessageFg();
				String iLetFg = cdReport3.getLetterFg();
				// CustUkey 接電話跟客戶主檔 要去判斷有無對應的該編號(05) 然後去看是否有簡訊
				// 有UKEY 但是要從L1905的資料裡面抓出對應編號
				// 看抓到的MSG以及其他的是否有符合
				// 05簡訊
				boolean ixy1 = titaVo.getParam("Msg" + i).trim().isEmpty();
				String ixy2 = "";
				if (iMsgFg.equals("Y")) {
					if (!ixy1) {
						ixy2 = "N";
					} else {
						ixy2 = "Y";
					}
				} else {
					ixy2 = "N";
				}
				if (tCustTelNo1 != null) {
					CustTelNo sCustTelNo = new CustTelNo();
					sCustTelNo = sCustTelNoService.custUKeyFirst(custMain.getCustUKey(), "05", titaVo);
					if (sCustTelNo == null && ixy2.equals("Y")) {
						throw new LogicException("E0005", "電話種類簡訊不存在，不發送簡訊限輸入'Y'");
					}
				}

				// CustMain Email
				// 頁面傳進來
				boolean ixyz = titaVo.getParam("EMail" + i).trim().isEmpty();
				String ixyz2 = "";
				if (icdEFg.equals("Y")) {
					if (!ixyz) {
//					ixyz2 = titaVo.getParam("EMail" + i);
						ixyz2 = "N";
					} else {
						ixyz2 = "Y";
					}
				} else {
					ixyz2 = "N";
				}

				// 找DB
				String sEmail = custMain.getEmail().trim();
				boolean isEmail2 = sEmail.trim().isEmpty();
				String iEmail2 = "";
				if (icdEFg.equals("Y")) {
					if (!isEmail2) {
						iEmail2 = "Y";
					} else {
						iEmail2 = "N";
					}
				} else {
					iEmail2 = "N";
				}

				if (iEmail2.equals("N") && ixyz2.equals("Y")) {
					throw new LogicException("E0005", "電子信箱不存在，不發送Email限輸入'Y'");
				}
				// FormNo報表代號
				iFormNo = titaVo.getParam("FormNo" + i);

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

					// 如果在頁面上輸入Y(不寄送)才需要修改DB的值為不寄送
					if ("Y".equals(titaVo.getParam("Paper" + i))) {
						VarPaper = "N";
					}
					if ("Y".equals(titaVo.getParam("Msg" + i))) {
						VarMsg = "N";
					}
					if ("Y".equals(titaVo.getParam("EMail" + i))) {
						VarEMail = "N";
					}

					// 如果頁面上已經是空白且CdReport是N不寄送 頁面上雖然是空白 但預設應該要為Y 2023/5/26 佳怡
					if (iLetFg.equals("N") && "Y".equals(titaVo.getParam("Paper" + i))) {
						VarPaper = "Y";
					}
					if (iMsgFg.equals("N") && "Y".equals(titaVo.getParam("Msg" + i))) {
						VarMsg = "Y";
					}
					if (icdEFg.equals("N") && "Y".equals(titaVo.getParam("EMail" + i))) {
						VarEMail = "Y";
					}
					
					int ApplyDt = iParse.stringToInteger(titaVo.getParam("ApplyDt"));

					CustNotice tCustNotice = new CustNotice();
					CustNoticeId tCustNoticePK = new CustNoticeId();

					tCustNoticePK.setCustNo(iCustNo);
					tCustNoticePK.setFacmNo(iFacmNo);
					tCustNoticePK.setFormNo(iFormNo);
					tCustNotice.setCustNoticeId(tCustNoticePK);
					tCustNotice.setCustNo(iCustNo);
					tCustNotice.setFacmNo(iFacmNo);
					tCustNotice.setFormNo(iFormNo);
					tCustNotice.setPaperNotice(VarPaper);
					tCustNotice.setMsgNotice(VarMsg);
					tCustNotice.setEmailNotice(VarEMail);
					tCustNotice.setApplyDate(ApplyDt);

					CdReport cdReport = sCdReportService.findById(tCustNotice.getFormNo(), titaVo);
					if (cdReport.getSendCode() == 1) {
//						if ("N".equals(VarPaper) && "N".equals(VarMsg) && "N".equals(VarEMail)) {
						this.info("Paper   ="   + titaVo.getParam("Paper" + i));
						this.info("Msg     ="  + titaVo.getParam("Msg" + i));
						this.info("EMail   ="  + titaVo.getParam("EMail" + i));

						if ( "Y".equals(titaVo.getParam("Paper" + i)) 
									&& "Y".equals(titaVo.getParam("Msg" + i)) 
										&& "Y".equals(titaVo.getParam("EMail" + i))) {
							throw new LogicException("E0007", tCustNotice.getFormNo() + "不可申請全部不寄送");
						}
					}
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

//				String VarPaper = "N";
//				String VarMsg = "N";
//				String VarEMail = "N";

				String VarPaper = "";
				String VarMsg = "";
				String VarEMail = "";
				String uKey = custMain.getCustUKey();

				Slice<CustTelNo> tCustTelNo = sCustTelNoService.findCustUKey(uKey, 0, Integer.MAX_VALUE, titaVo);
				CdReport cdReport3 = sCdReportService.findById(titaVo.getParam("FormNo" + i), titaVo);
				String icdEFg = cdReport3.getEmailFg();
				String iMsgFg = cdReport3.getMessageFg();
				String iLetFg = cdReport3.getLetterFg();
				// CustUkey 接電話跟客戶主檔 要去判斷有無對應的該編號(05) 然後去看是否有簡訊
				// 有UKEY 但是要從L1905的資料裡面抓出對應編號
				// 看抓到的MSG以及其他的是否有符合
				// 05簡訊
				boolean ixy = titaVo.getParam("Msg" + i).trim().isEmpty();
				String ixy2 = "";
				if (iMsgFg.equals("Y")) {
					if (!ixy) {
						ixy2 = "N";
					} else {
						ixy2 = "Y";
					}
				} else {
					ixy2 = "N";
				}
				if (tCustTelNo != null) {
					CustTelNo sCustTelNo = new CustTelNo();
					sCustTelNo = sCustTelNoService.custUKeyFirst(custMain.getCustUKey(), "05", titaVo);
					if (sCustTelNo == null && ixy2.equals("Y")) {
						throw new LogicException("E0005", "電話種類簡訊不存在，不發送簡訊限輸入'Y'");
					}
				}

				// CustMain Email
				// 頁面傳進來
				boolean ixyz = titaVo.getParam("EMail" + i).trim().isEmpty();
				String ixyz2 = "";
				if (icdEFg.equals("Y")) {
					if (!ixyz) {
//					ixyz2 = titaVo.getParam("EMail" + i);
						ixyz2 = "N";
					} else {
						ixyz2 = "Y";
					}
				} else {
					ixyz2 = "N";
				}

				// 找DB
				String sEmail = custMain.getEmail().trim();
				boolean isEmail2 = sEmail.trim().isEmpty();
				String iEmail2 = "";
				if (icdEFg.equals("Y")) {
					if (!isEmail2) {
						iEmail2 = "Y";
					} else {
						iEmail2 = "N";
					}
				} else {
					iEmail2 = "N";
				}

				if (iEmail2.equals("N") && ixyz2.equals("Y")) {
					throw new LogicException("E0005", "電子信箱不存在，不發送Email限輸入'Y'");
				}

				// FormNo報表代號
				iFormNo = titaVo.getParam("FormNo" + i);

				// 這邊的邏輯因為前端 VAR 是設定「不通知申請」，所以Paper{i}, Msg{i}, EMail{i}為 Y 時對應DB的 N
//				if (titaVo.getParam("Paper" + i).trim().isEmpty()) {
//					VarPaper = "Y";
//				}
//				if (titaVo.getParam("Msg" + i).trim().isEmpty()) {
//					VarMsg = "Y";
//				}
//				if (titaVo.getParam("EMail" + i).trim().isEmpty()) {
//					VarEMail = "Y";
//				}
				if ("".equals(titaVo.getParam("Paper" + i))) {
					VarPaper = "Y";
				}
				if ("".equals(titaVo.getParam("Msg" + i))) {
					VarMsg = "Y";
				}
				if ("".equals(titaVo.getParam("EMail" + i))) {
					VarEMail = "Y";
				}

				// 如果在頁面上輸入Y(不寄送)才需要修改DB的值為不寄送
				if ("Y".equals(titaVo.getParam("Paper" + i))) {
					VarPaper = "N";
				}
				if ("Y".equals(titaVo.getParam("Msg" + i))) {
					VarMsg = "N";
				}
				if ("Y".equals(titaVo.getParam("EMail" + i))) {
					VarEMail = "N";
				}

				// 如果頁面上已經是空白且CdReport是N不寄送 頁面上雖然是空白 但預設應該要為Y 2023/5/26 佳怡
				if (iLetFg.equals("N") && "Y".equals(titaVo.getParam("Paper" + i))) {
					VarPaper = "Y";
				}
				if (iMsgFg.equals("N") && "Y".equals(titaVo.getParam("Msg" + i))) {
					VarMsg = "Y";
				}
				if (icdEFg.equals("N") && "Y".equals(titaVo.getParam("EMail" + i))) {
					VarEMail = "Y";
				}

				int ApplyDt = iParse.stringToInteger(titaVo.getParam("ApplyDt"));

				CustNotice tCustNotice = new CustNotice();
				CustNoticeId tCustNoticePK = new CustNoticeId();
				tCustNoticePK.setCustNo(iCustNo);
				tCustNoticePK.setFacmNo(iFacmNo);
				tCustNoticePK.setFormNo(iFormNo);

				tCustNotice = sCustNoticeService.holdById(tCustNoticePK);

				boolean log = false;
				CustNotice oCustNotice = new CustNotice();

				// 維護時的新資料改為新增
				if (tCustNotice == null) {
					tCustNotice = new CustNotice();
					CustNoticeId xCustNoticePK = new CustNoticeId();

					xCustNoticePK.setCustNo(iCustNo);
					xCustNoticePK.setFacmNo(iFacmNo);
					xCustNoticePK.setFormNo(iFormNo);
					tCustNotice.setCustNoticeId(xCustNoticePK);
					tCustNotice.setPaperNotice(VarPaper);
					tCustNotice.setMsgNotice(VarMsg);
					tCustNotice.setEmailNotice(VarEMail);
					tCustNotice.setApplyDate(ApplyDt);
					try {
						sCustNoticeService.insert(tCustNotice, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "客戶通知設定檔");
					}

//					if ("N".equals(VarPaper) || "N".equals(VarMsg) || "N".equals(VarPaper)) {
//						log = true;
//						oCustNotice = (CustNotice) iDataLog.clone(tCustNotice);
//						oCustNotice.setPaperNotice("Y");
//						oCustNotice.setMsgNotice("Y");
//						oCustNotice.setEmailNotice("Y");
//					}
					oCustNotice = (CustNotice) iDataLog.clone(tCustNotice);
					oCustNotice.setPaperNotice(VarPaper);
					oCustNotice.setMsgNotice(VarMsg);
					oCustNotice.setEmailNotice(VarEMail);

				} else {
					// 變更前
					if (!VarPaper.equals(tCustNotice.getPaperNotice()) || !VarMsg.equals(tCustNotice.getMsgNotice())
							|| !VarEMail.equals(tCustNotice.getEmailNotice())) {

						// 只在有修改選項時，才實際更新
						log = true;
						oCustNotice = (CustNotice) iDataLog.clone(tCustNotice);

						tCustNotice.setPaperNotice(VarPaper);
						tCustNotice.setMsgNotice(VarMsg);
						tCustNotice.setEmailNotice(VarEMail);
						tCustNotice.setApplyDate(ApplyDt);

						try {
							tCustNotice = sCustNoticeService.update2(tCustNotice, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "客戶通知設定檔");
						}
					}

					if (log) {
						// 紀錄變更前變更後
						String formx = "";
						CdReport cdReport = sCdReportService.findById(tCustNotice.getFormNo(), titaVo);
						if (cdReport != null) {
							formx = cdReport.getFormName();
						}
						CdReport oCdReport = (CdReport) iDataLog.clone(cdReport);
						cdReport.setMessageFg(VarMsg);// 簡訊
						cdReport.setEmailFg(VarEMail);// Email
						cdReport.setLetterFg(VarPaper);// 書面'

						if (cdReport.getSendCode() == 1) {
//							if ("N".equals(tCustNotice.getPaperNotice()) && "N".equals(tCustNotice.getMsgNotice())
//									&& "N".equals(tCustNotice.getEmailNotice())) {
							if ( "Y".equals(titaVo.getParam("Paper" + i)) 
									&& "Y".equals(titaVo.getParam("Msg" + i)) 
										&& "Y".equals(titaVo.getParam("EMail" + i))) {
								throw new LogicException("E0007", tCustNotice.getFormNo() + "不可申請全部不寄送");
							}
						}
						oCustNotice = tranDesc(oCustNotice);
						CustNotice nCustNotice = tranDesc(tCustNotice);

						iDataLog.setEnv(titaVo, oCdReport, cdReport);
						iDataLog.exec("與報表設定比對,通知書:", cdReport.getFormNo() + " " + formx);

						iDataLog.setEnv(titaVo, oCustNotice, nCustNotice);
						iDataLog.exec("修改客戶通知設定,通知書:" + oCustNotice.getFormNo() + " " + formx);
					}
				}
				CdReport cdReport2 = sCdReportService.findById(tCustNotice.getFormNo(), titaVo);

				if (cdReport2 != null && tCustNotice != null) {
					if (cdReport2.getSendCode() == 1) {
//						if ("N".equals(VarPaper) && "N".equals(VarMsg) && "N".equals(VarEMail)) {
						if ( "Y".equals(titaVo.getParam("Paper" + i)) 
								&& "Y".equals(titaVo.getParam("Msg" + i)) 
									&& "Y".equals(titaVo.getParam("EMail" + i))) {
							throw new LogicException("E0007", tCustNotice.getFormNo() + "不可申請全部不寄送");
						}
					} else {
						if (cdReport2.getSendCode() == 1) {
//							if ("N".equals(VarPaper) && "N".equals(VarMsg) && "N".equals(VarEMail)) {
//							if ((iLetFg.equals("N") && "Y".equals(titaVo.getParam("Paper" + i))) 
//									&& (iMsgFg.equals("N") && "Y".equals(titaVo.getParam("Msg" + i))) 
//										&& ("Y".equals(titaVo.getParam("EMail" + i)))) {
							if ( "Y".equals(titaVo.getParam("Paper" + i)) 
									&& "Y".equals(titaVo.getParam("Msg" + i)) 
										&& "Y".equals(titaVo.getParam("EMail" + i))) {
								throw new LogicException("E0007", tCustNotice.getFormNo() + "不可申請全部不寄送");
							}
						}
					}
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private CustNotice tranDesc(CustNotice custNotice) {

		if ("N".equals(custNotice.getPaperNotice())) {
			custNotice.setPaperNotice("不寄送");
		} else {
			custNotice.setPaperNotice("寄送");
		}

		if ("N".equals(custNotice.getMsgNotice())) {
			custNotice.setMsgNotice("不發送");
		} else {
			custNotice.setMsgNotice("發送");
		}

		if ("N".equals(custNotice.getEmailNotice())) {
			custNotice.setEmailNotice("不發送");
		} else {
			custNotice.setEmailNotice("發送");
		}

		return custNotice;
	}
}
