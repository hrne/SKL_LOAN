package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchDeductMedia;
import com.st1.itx.db.domain.AchDeductMediaId;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.domain.PostDeductMediaId;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.trade.L4.L4452Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.AchDeductFileVo;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.common.data.PostDeductFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4452Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4452Batch extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BankDeductDtlService bankDeductDtlService;
	@Autowired
	public AchDeductMediaService achDeductMediaService;
	@Autowired
	public PostDeductMediaService postDeductMediaService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public PostDeductFileVo postDeductFileVo;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	SendRsp sendRsp;

	@Autowired
	public TxAmlCom txAmlCom;

	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public PostAuthLogService postAuthLogService;

	@Autowired
	public L4452Report l4452Report;
	@Autowired
	public WebClient webClient;
	private int iFunctionCode = 0;
	private int iEntryDate = 0;
	private int iOpItem = 0;
	private int mediaDate = 0;
	private int OccRepayType = 0;

	private BigDecimal amt11 = BigDecimal.ZERO;
	private BigDecimal amt12 = BigDecimal.ZERO;

	private HashMap<tmpFacm, BigDecimal> rpAmtMap = new HashMap<>();
	private HashMap<tmpFacm, Integer> flagMap = new HashMap<>();

	private int cnt11 = 0;
	private int cnt12 = 0;

	private Boolean checkFlag = true;
	private String sendMsg = "";
	private int doCnt = 0;
//	寄送筆數
	private int commitCnt = 500;

	ArrayList<OccursList> unDoList = new ArrayList<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4452Batch ");
		this.totaVo.init(titaVo);

		iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		mediaDate = this.getTxBuffer().getTxCom().getTbsdyf();
		iOpItem = parse.stringToInteger(titaVo.getParam("OpItem"));

		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		// 產出媒體檔
		if (iFunctionCode == 1) {
			try {
				normalRoutine(titaVo);
			} catch (LogicException e) {
				checkFlag = false;
				sendMsg = sendMsg + e.getErrorMsg();
			}
			if (checkFlag) {
				sendMsg = "產製媒體檔完成，媒體檔產出筆數=" + doCnt + ", 未產出筆數=" + unDoList.size();
			}
		}

		// 重製媒體碼
		if (iFunctionCode == 2) {
			try {
				eraseRoutine(titaVo);
			} catch (LogicException e) {
				checkFlag = false;
				sendMsg = sendMsg + e.getErrorMsg();
			}
			if (checkFlag) {
				sendMsg = "重製媒體碼完成。";
				checkFlag = false;
			}
		}

		if (checkFlag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void normalRoutine(TitaVo titaVo) throws LogicException {
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.info("iEntryDate : " + iEntryDate);
		this.info("mediaDate : " + mediaDate);
//		銀行扣款媒體製作

//		200707 update 改為不同撥款相加, group by facmNo
//		2.建完media，回頭update media date seq，bankDeductdtl

//		this.totaVo.putParam("OFormFlag", 1);

		Slice<BankDeductDtl> slBankDeductDtl = null;
		switch (iOpItem) {
		case 1: // ach
			slBankDeductDtl = bankDeductDtlService.repayBankNotEq("700", iEntryDate, iEntryDate, this.index, this.limit,
					titaVo);
			break;
		case 2: // post
			slBankDeductDtl = bankDeductDtlService.repayBankEq("700", iEntryDate, iEntryDate, this.index, this.limit,
					titaVo);
			break;
		default: // all
			slBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate, iEntryDate, this.index, this.limit, titaVo);
			break;
		}

		if (slBankDeductDtl == null) {
			throw new LogicException("E0001", "銀行扣款資料不存在"); // 查詢資料不存在
		}

		ArrayList<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();
		lBankDeductDtl = new ArrayList<BankDeductDtl>(slBankDeductDtl.getContent());
		for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
			if (!"".equals(tBankDeductDtl.getMediaCode())) {
				throw new LogicException("E0015", "已產製媒體，需重製媒體碼後再產製"); // 檢查錯誤
			}
		}

//			產出媒體檢核AML表單
		amlCheckForm(lBankDeductDtl, titaVo);
//			將金額加總
		setRepayAmtMap(lBankDeductDtl, titaVo);

//			寫入媒體檔
		switch (iOpItem) {
		case 1: // ach
			setAchDeductMedia(lBankDeductDtl, titaVo);
			produceAchDeductMedia(titaVo);
			break;
		case 2: // post
			setPostDeductMedia(lBankDeductDtl, titaVo);
			producePostDeductMedia(titaVo);
			break;
		default:// all
			setAchDeductMedia(lBankDeductDtl, titaVo);
			produceAchDeductMedia(titaVo);
			setPostDeductMedia(lBankDeductDtl, titaVo);
			producePostDeductMedia(titaVo);
			break;
		}

		l4452Report.doReport(unDoList, titaVo);
	}

	private void eraseRoutine(TitaVo titaVo) throws LogicException {
//		刪除今日於媒體檔之資料
		Slice<BankDeductDtl> slBankDeductDtl = null;
		switch (iOpItem) {
		case 1:
			deleteOldAchMedia(mediaDate, "1", titaVo);
			deleteOldAchMedia(mediaDate, "2", titaVo);
			slBankDeductDtl = bankDeductDtlService.repayBankNotEq("700", iEntryDate, iEntryDate, this.index, this.limit,
					titaVo);
			break;
		case 2:
			deleteOldPostMedia(mediaDate, titaVo);
			slBankDeductDtl = bankDeductDtlService.repayBankEq("700", iEntryDate, iEntryDate, this.index, this.limit,
					titaVo);
			break;
		default:
			deleteOldAchMedia(mediaDate, "1", titaVo);
			deleteOldAchMedia(mediaDate, "2", titaVo);
			deleteOldPostMedia(mediaDate, titaVo);
			slBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate, iEntryDate, this.index, this.limit, titaVo);
			break;
		}

		if (slBankDeductDtl != null) {
			for (BankDeductDtl tBankDeductDtl : slBankDeductDtl.getContent()) {
				updateBankDeductDtl(tBankDeductDtl, 0, "", 0, "", titaVo);
			}
		}

		this.batchTransaction.commit();
	}

	private void setRepayAmtMap(List<BankDeductDtl> lBankDeductDtl, TitaVo titaVo) throws LogicException {
		this.batchTransaction.commit();

		int n = 0;
//		金額合計 by 額度 -ACH & POST
		for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
			n++;
			if (n % commitCnt == 0) {
				this.batchTransaction.commit();
			}

			tmpFacm tmp = new tmpFacm();
			tmp.setCustNo(tBankDeductDtl.getCustNo());
			tmp.setFacmNo(tBankDeductDtl.getFacmNo());
			tmp.setRepayType(tBankDeductDtl.getRepayType());
			tmp.setPayIntDate(tBankDeductDtl.getPayIntDate());
			TempVo tempVo = new TempVo();
			tempVo = tempVo.getVo(tBankDeductDtl.getJsonFields());

			this.info("JsonFields : " + tBankDeductDtl.getJsonFields());
//				欄位不夠長，順序為帳號、AML、扣帳金額為零
			if (tempVo.get("Auth") != null && tempVo.get("Auth").length() > 0) {
				this.info("帳號授權檢核");
				continue;
			}
			if ("1".equals(tBankDeductDtl.getAmlRsp()) || "2".equals(tBankDeductDtl.getAmlRsp())) {
				this.info("Aml檢核");
				continue;
			}
			if (tempVo.get("Deduct") != null && tempVo.get("Deduct").length() > 0) {
				this.info("扣款檢核");
				continue;
			}

			if (rpAmtMap.containsKey(tmp)) {
				rpAmtMap.put(tmp, rpAmtMap.get(tmp).add(tBankDeductDtl.getRepayAmt()));
			} else {
				rpAmtMap.put(tmp, tBankDeductDtl.getRepayAmt());
			}
		}

	}

	private void setPostDeductMedia(ArrayList<BankDeductDtl> lBankDeductDtl, TitaVo titaVo) throws LogicException {
		this.info("setPostDeductMedia Start");

		List<PostDeductMedia> lPostDeductMedia = new ArrayList<PostDeductMedia>();

//		產出媒體之排序(寫入時產生MediaSeq，產出檔根據此seq寫入媒體)
		lBankDeductDtl = postMediaSorting(lBankDeductDtl);

		int mediaSeq = 0;
		for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
			if (mediaSeq % commitCnt == 0) {
				this.batchTransaction.commit();
			}

			PostDeductMedia tPostDeductMedia = new PostDeductMedia();
			PostDeductMediaId tPostDeductMediaId = new PostDeductMediaId();

			if ("700".equals(tBankDeductDtl.getRepayBank())) {
				tmpFacm tmp = new tmpFacm();
				tmp.setCustNo(tBankDeductDtl.getCustNo());
				tmp.setFacmNo(tBankDeductDtl.getFacmNo());
				tmp.setRepayType(tBankDeductDtl.getRepayType());
				tmp.setPayIntDate(tBankDeductDtl.getPayIntDate());
				String mediaCode = "Y";
				String procNote = "";
				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(tBankDeductDtl.getJsonFields());
				this.info("JsonFields : " + tBankDeductDtl.getJsonFields());
//					欄位不夠長，順序為帳號、AML、扣帳金額為零
				if (tempVo.get("Auth") != null && tempVo.get("Auth").length() > 0) {
					procNote = "帳號授權檢核:" + authX(tempVo.get("Auth"), titaVo) + "。";
					mediaCode = "N";
				} else if ("1".equals(tBankDeductDtl.getAmlRsp()) || "2".equals(tBankDeductDtl.getAmlRsp())) {
					procNote = "Aml檢核:" + amlRspX(tBankDeductDtl.getAmlRsp(), titaVo) + "。";
					mediaCode = "N";
				} else if (tempVo.get("Deduct") != null && tempVo.get("Deduct").length() > 0) {
					procNote = "扣款檢核：" + tempVo.get("Deduct") + "。";
					mediaCode = "N";
				}

				updateBankDeductDtl(tBankDeductDtl, 0, "3", 0, mediaCode, titaVo);

				if ("N".equals(mediaCode)) {
					doReport(procNote, "", tBankDeductDtl, titaVo);
					this.info("不產出，僅寫入報表...");
					continue;
				}

//					重複的撥款seq不+1
				if (!flagMap.containsKey(tmp)) {
					mediaSeq = mediaSeq + 1;
				}

				updateBankDeductDtl(tBankDeductDtl, mediaDate, "3", mediaSeq, mediaCode, titaVo);

//					重複的撥款跳過
				if (flagMap.containsKey(tmp)) {
					this.info("flagMap continue ..." + tmp);
					continue;
				} else {
					flagMap.put(tmp, 1);
				}

				tPostDeductMediaId.setMediaDate(mediaDate);
				tPostDeductMediaId.setMediaSeq(mediaSeq);
				tPostDeductMedia.setPostDeductMediaId(tPostDeductMediaId);
				tPostDeductMedia.setMediaDate(tPostDeductMediaId.getMediaDate());
				tPostDeductMedia.setMediaSeq(tPostDeductMediaId.getMediaSeq());

				tPostDeductMedia.setCustNo(tBankDeductDtl.getCustNo());
				tPostDeductMedia.setFacmNo(tBankDeductDtl.getFacmNo());
				tPostDeductMedia.setRepayType(tBankDeductDtl.getRepayType());
				tPostDeductMedia.setRepayAmt(rpAmtMap.get(tmp));
				tPostDeductMedia.setProcNoteCode(" ");
				tPostDeductMedia.setPostDepCode(tBankDeductDtl.getPostCode());

//					還款類別1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費
//					入帳扣款別(MBKTRX)】=1火險、2期款、3帳管、4契變手續費
				if (tBankDeductDtl.getRepayType() == 1 || tBankDeductDtl.getRepayType() == 3) {
					tPostDeductMedia.setOutsrcCode("846");
					tPostDeductMedia.setDistCode("0002");
					OccRepayType = 2;
				} else if (tBankDeductDtl.getRepayType() == 4) {
					tPostDeductMedia.setOutsrcCode("846");
					tPostDeductMedia.setDistCode("0001");
					OccRepayType = 3;
				} else if (tBankDeductDtl.getRepayType() == 5) {
					tPostDeductMedia.setOutsrcCode("53N");
					tPostDeductMedia.setDistCode(" ");
					OccRepayType = 1;
				} else if (tBankDeductDtl.getRepayType() == 6) {
					tPostDeductMedia.setOutsrcCode("846");
					tPostDeductMedia.setDistCode("0001");
					OccRepayType = 4;
				}
				tPostDeductMedia.setTransDate(iEntryDate);
				tPostDeductMedia.setRepayAcctNo(tBankDeductDtl.getRepayAcctNo());

//				扣款人ID+郵局存款別(POSCDE)+戶號) +2位帳號碼
				// 右靠左補空白
				String custId = "";
				PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(tBankDeductDtl.getCustNo(),
						tBankDeductDtl.getPostCode(), tBankDeductDtl.getRepayAcctNo(), "1", titaVo);
				if (tPostAuthLog != null) {
					custId = tPostAuthLog.getCustId();
				} else {
					throw new LogicException("E0005", "無授權檔資料");
				}

				tPostDeductMedia.setPostUserNo(
						FormatUtil.padX(tBankDeductDtl.getRepayAcctSeq(), 2) + FormatUtil.padX(custId, 10)
								+ tBankDeductDtl.getPostCode() + FormatUtil.pad9("" + tBankDeductDtl.getCustNo(), 7));

// 				計息迄日+額度編號+入帳扣款別
				int entryDate = 0;
				if (tBankDeductDtl.getIntEndDate() != 0) {
					entryDate = tBankDeductDtl.getIntEndDate() + 19110000;
				}
				tPostDeductMedia.setOutsrcRemark(FormatUtil.padX(FormatUtil.pad9("" + entryDate, 8)
						+ FormatUtil.pad9("" + tBankDeductDtl.getFacmNo(), 3) + OccRepayType, 20));

				lPostDeductMedia.add(tPostDeductMedia);
			}
		}
		if (mediaSeq > 1) {
			try {
				postDeductMediaService.insertAll(lPostDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "L4452 資料重複，請先訂正。");
			}
		}
	}

	private void setAchDeductMedia(ArrayList<BankDeductDtl> lBankDeductDtl, TitaVo titaVo) throws LogicException {
		this.info("setAchDeductMedia Start");

		List<AchDeductMedia> lAchDeductMedia = new ArrayList<AchDeductMedia>();

		this.batchTransaction.commit();

//		產出媒體之排序(寫入時產生MediaSeq，產出檔根據此seq寫入媒體)
		lBankDeductDtl = achMediaSorting(lBankDeductDtl);

		int n = 0, mediaSeq1 = 0, mediaSeq2 = 0;
		for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
			AchDeductMediaId tAchDeductMediaId = new AchDeductMediaId();
			AchDeductMedia tAchDeductMedia = new AchDeductMedia();

			n++;

			if (n % commitCnt == 0) {
				this.batchTransaction.commit();
			}

			if (!"700".equals(tBankDeductDtl.getRepayBank())) {
				String mediaCode = "Y";
				String procNote = "";
				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(tBankDeductDtl.getJsonFields());
				this.info("JsonFields : " + tBankDeductDtl.getJsonFields());
//					欄位不夠長，順序為帳號、AML、扣帳金額為零
				if (tempVo.get("Auth") != null && tempVo.get("Auth").length() > 0) {
					procNote = "帳號授權檢核:" + authX(tempVo.get("Auth"), titaVo) + "。";
					mediaCode = "N";
				} else if ("1".equals(tBankDeductDtl.getAmlRsp()) || "2".equals(tBankDeductDtl.getAmlRsp())) {
					procNote = "Aml檢核:" + amlRspX(tBankDeductDtl.getAmlRsp(), titaVo) + "。";
					mediaCode = "N";
				} else if (tempVo.get("Deduct") != null && tempVo.get("Deduct").length() > 0) {
					procNote = "扣款檢核：" + tempVo.get("Deduct") + "。";
					mediaCode = "N";
				}

				if ("103".equals(tBankDeductDtl.getRepayBank())) {
					updateBankDeductDtl(tBankDeductDtl, 0, "1", 0, mediaCode, titaVo);
				} else {
					updateBankDeductDtl(tBankDeductDtl, 0, "2", 0, mediaCode, titaVo);
				}
				if ("N".equals(mediaCode)) {
					doReport(procNote, "", tBankDeductDtl, titaVo);
					this.info("不產出，僅寫入報表...");
					continue;
				}
				tmpFacm tmp = new tmpFacm();
				tmp.setCustNo(tBankDeductDtl.getCustNo());
				tmp.setFacmNo(tBankDeductDtl.getFacmNo());
				tmp.setRepayType(tBankDeductDtl.getRepayType());
				tmp.setPayIntDate(tBankDeductDtl.getPayIntDate());

//					重複的撥款seq不+1
				if (!flagMap.containsKey(tmp)) {
					if ("103".equals(tBankDeductDtl.getRepayBank())) {
						mediaSeq1 = mediaSeq1 + 1;
					} else {
						mediaSeq2 = mediaSeq2 + 1;
					}
				}

				this.info("mediaSeq1 ..." + mediaSeq1);
				this.info("mediaSeq2 ..." + mediaSeq2);

				tAchDeductMediaId.setMediaDate(mediaDate);
				if ("103".equals(tBankDeductDtl.getRepayBank())) {
					updateBankDeductDtl(tBankDeductDtl, mediaDate, "1", mediaSeq1, mediaCode, titaVo);
				} else {
					updateBankDeductDtl(tBankDeductDtl, mediaDate, "2", mediaSeq2, mediaCode, titaVo);
				}

//					重複的跳過
				if (flagMap.containsKey(tmp)) {
					this.info("flagMap continue ..." + tmp);
					continue;
				} else {
					flagMap.put(tmp, 1);
				}

				tAchDeductMediaId.setMediaDate(mediaDate);
				if ("103".equals(tBankDeductDtl.getRepayBank())) {
					tAchDeductMediaId.setMediaKind("1");
					tAchDeductMediaId.setMediaSeq(mediaSeq1);
				} else {
					tAchDeductMediaId.setMediaKind("2");
					tAchDeductMediaId.setMediaSeq(mediaSeq2);
				}

				tAchDeductMedia.setAchDeductMediaId(tAchDeductMediaId);
				tAchDeductMedia.setMediaDate(tAchDeductMediaId.getMediaDate());
				tAchDeductMedia.setMediaKind(tAchDeductMediaId.getMediaKind());
				tAchDeductMedia.setMediaSeq(tAchDeductMediaId.getMediaSeq());

				tAchDeductMedia.setCustNo(tBankDeductDtl.getCustNo());
				tAchDeductMedia.setFacmNo(tBankDeductDtl.getFacmNo());
				tAchDeductMedia.setRepayType(tBankDeductDtl.getRepayType());
				tAchDeductMedia.setRepayAmt(rpAmtMap.get(tmp));
				tAchDeductMedia.setReturnCode("  ");
				tAchDeductMedia.setEntryDate(tBankDeductDtl.getEntryDate());
				tAchDeductMedia.setPrevIntDate(tBankDeductDtl.getPrevIntDate());
				tAchDeductMedia.setRepayBank(tBankDeductDtl.getRepayBank());
				tAchDeductMedia.setRepayAcctNo(tBankDeductDtl.getRepayAcctNo());
//					共用代碼檔
//					1.期款
//					2.部分償還
//					3.結案
//					4.帳管費
//					5.火險費
//					6.契變手續費
//					7.法務費
//					9.其他

//					銀扣用
//					1.火險費 
//					2.帳管費、貸後契變手續費
//					3.期款 
				if (tBankDeductDtl.getRepayType() == 1 || tBankDeductDtl.getRepayType() == 3) {
					tAchDeductMedia.setAchRepayCode("3");
				} else if (tBankDeductDtl.getRepayType() == 4) {
					tAchDeductMedia.setAchRepayCode("2");
				} else if (tBankDeductDtl.getRepayType() == 5) {
					tAchDeductMedia.setAchRepayCode("1");
				} else if (tBankDeductDtl.getRepayType() == 6) {
					tAchDeductMedia.setAchRepayCode("2");
				}
				tAchDeductMedia.setAcctCode(tBankDeductDtl.getAcctCode());
				tAchDeductMedia.setIntStartDate(tBankDeductDtl.getIntStartDate());
				tAchDeductMedia.setIntEndDate(tBankDeductDtl.getIntEndDate());
//					tAchDeductMedia.setDepCode(depCode);
				tAchDeductMedia.setRelationCode(tBankDeductDtl.getRelationCode());
				tAchDeductMedia.setRelCustName(tBankDeductDtl.getRelCustName());
				tAchDeductMedia.setRelCustId(tBankDeductDtl.getRelCustId());

				lAchDeductMedia.add(tAchDeductMedia);
				this.info("lAchDeductMedia" + tAchDeductMedia.toString());

			}
		}
		if (mediaSeq1 >= 1 || mediaSeq2 >= 1) {
			try {
				achDeductMediaService.insertAll(lAchDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "L4452 資料重複，請先重製媒體碼。");
			}
		}
	}

	private void producePostDeductMedia(TitaVo titaVo) throws LogicException {
		ArrayList<PostDeductMedia> lPostDeductMedia = new ArrayList<PostDeductMedia>();

		ArrayList<OccursList> tmp53N = new ArrayList<>();
		ArrayList<OccursList> tmp8460001 = new ArrayList<>();
		ArrayList<OccursList> tmp8460002 = new ArrayList<>();

		int cnt53N = 0;
		int cnt8460001 = 0;
		int cnt8460002 = 0;

		BigDecimal amt53N = BigDecimal.ZERO;
		BigDecimal amt8460001 = BigDecimal.ZERO;
		BigDecimal amt8460002 = BigDecimal.ZERO;

		int transDate = 0;

		this.batchTransaction.commit();

//		header
//		none
		Slice<PostDeductMedia> sPostDeductMedia = null;

		sPostDeductMedia = postDeductMediaService.mediaDateEq(mediaDate, this.index, this.limit, titaVo);

		lPostDeductMedia = sPostDeductMedia == null ? null
				: new ArrayList<PostDeductMedia>(sPostDeductMedia.getContent());

//		postMediaSorting()

		if (lPostDeductMedia != null && lPostDeductMedia.size() != 0) {
//			依儲金帳號排序後，先依區處代號排序(0001.0002)，再依計息迄日由小到大，最後才依扣款金額由大到小排序
//			RepayAcctNo > RepayType(5>4>1) DESC > PrevIntDate > RepayAmt DESC
			lPostDeductMedia.sort((c1, c2) -> {
				int result = 0;
				if (c1.getRepayAcctNo().compareTo(c2.getRepayAcctNo()) != 0) {
					result = c1.getRepayAcctNo().compareTo(c2.getRepayAcctNo());
				} else if (c1.getRepayType() - c2.getRepayType() != 0) {
					if (c1.getRepayType() == 3) {
						result = 1;
					} else if (c2.getRepayType() == 3) {
						result = -1;
					} else {
						result = c2.getRepayType() - c1.getRepayType();
					}
				} else if (c1.getOutsrcRemark().substring(0, 8).compareTo(c2.getOutsrcRemark().substring(0, 8)) != 0) {
					result = c1.getOutsrcRemark().substring(0, 8).compareTo(c2.getOutsrcRemark().substring(0, 8));
				} else if (c2.getRepayAmt().compareTo(c1.getRepayAmt()) != 0) {
					result = c2.getRepayAmt().compareTo(c1.getRepayAmt());
				} else {
					result = 0;
				}
				return result;
			});

			int n = 0;
			for (PostDeductMedia tPostDeductMedia : lPostDeductMedia) {
				int iRepayMonth = 0;
				iRepayMonth = parse.stringToInteger(("" + tPostDeductMedia.getTransDate()).substring(0, 5));
				transDate = tPostDeductMedia.getTransDate();

				n++;
				if (n % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				if ("53N".equals(tPostDeductMedia.getOutsrcCode())) {
					cnt53N = cnt53N + 1;
					amt53N = amt53N.add(tPostDeductMedia.getRepayAmt());
				} else if ("846".equals(tPostDeductMedia.getOutsrcCode())) {
					if ("0001".equals(tPostDeductMedia.getDistCode())) {
						cnt8460001 = cnt8460001 + 1;
						amt8460001 = amt8460001.add(tPostDeductMedia.getRepayAmt());
					} else if ("0002".equals(tPostDeductMedia.getDistCode())) {
						cnt8460002 = cnt8460002 + 1;
						amt8460002 = amt8460002.add(tPostDeductMedia.getRepayAmt());
					}
				}

//		occurs
				OccursList occursList = new OccursList();

				occursList.putParam("OccIndex", "1");
				occursList.putParam("OccDepCode", tPostDeductMedia.getPostDepCode());
				occursList.putParam("OccOrgCode", tPostDeductMedia.getOutsrcCode());
				occursList.putParam("OccDistCode", FormatUtil.padX(tPostDeductMedia.getDistCode(), 4));
				occursList.putParam("OccTxDate", tPostDeductMedia.getTransDate());
				occursList.putParam("OccStampInd", "S");
				occursList.putParam("OccPostNote1", FormatUtil.padX("", 2));
				occursList.putParam("OccRepayAcctNo", FormatUtil.pad9("" + tPostDeductMedia.getRepayAcctNo(), 14));
				occursList.putParam("OccKeepColA", FormatUtil.padX("", 10));

//				含兩位小數位
				occursList.putParam("OccRepayAmt", FormatUtil.pad9(tPostDeductMedia.getRepayAmt() + "00", 11));

				occursList.putParam("OccCustMemo", FormatUtil.padLeft(tPostDeductMedia.getPostUserNo(), 20));
				occursList.putParam("OccPrtCustNo", FormatUtil.padX("1", 1));
				;
				occursList.putParam("OccPostNote2", FormatUtil.padX("", 1));
				occursList.putParam("OccMaskFlag", FormatUtil.padX("", 1));
				occursList.putParam("OccChgFlag", FormatUtil.padX("", 1));
				occursList.putParam("OccReturnCode", FormatUtil.padX("", 2));
				occursList.putParam("OccRepayMonth", iRepayMonth);
				occursList.putParam("OccPostNote3", FormatUtil.padX("", 5));
				occursList.putParam("OccRemark", FormatUtil.padX(tPostDeductMedia.getOutsrcRemark(), 20));
				occursList.putParam("OccKeepColB", FormatUtil.padX("", 10));

				if ("53N".equals(tPostDeductMedia.getOutsrcCode())) {
					tmp53N.add(occursList);
				} else if ("846".equals(tPostDeductMedia.getOutsrcCode())) {
					if ("0001".equals(tPostDeductMedia.getDistCode())) {
						tmp8460001.add(occursList);
					} else if ("0002".equals(tPostDeductMedia.getDistCode())) {
						tmp8460002.add(occursList);
					}
				}
			}
		} else {
			this.info("lPostDeductMedia is null");
		}

		if (cnt53N >= 1) {
//		footer
			this.batchTransaction.commit();

			PostDeductFileVo tmp53NPostDeductFileVo = new PostDeductFileVo();

			tmp53NPostDeductFileVo.put("FootIndex", "2");
			tmp53NPostDeductFileVo.put("FootDepCode", FormatUtil.padX("", 1));
			tmp53NPostDeductFileVo.put("FootOrgCode", "53N");
			tmp53NPostDeductFileVo.put("FootDistCode", "");
			tmp53NPostDeductFileVo.put("FootTxDate", transDate);
			tmp53NPostDeductFileVo.put("FootKeepColA", FormatUtil.padX("", 3));
			tmp53NPostDeductFileVo.put("FootTotCnt", FormatUtil.pad9("" + cnt53N, 7));
			tmp53NPostDeductFileVo.put("FootTotAmt", FormatUtil.pad9(amt53N + "00", 13));
			tmp53NPostDeductFileVo.put("FootKeepColB", FormatUtil.padX("", 16));
			tmp53NPostDeductFileVo.put("FootSucsCnt", FormatUtil.pad9("", 7));
			tmp53NPostDeductFileVo.put("FootSucsAmt", FormatUtil.pad9("", 13));
			tmp53NPostDeductFileVo.put("FootKeepColC", FormatUtil.padX("", 45));

			// 把明細資料容器裝到檔案資料容器內
			tmp53NPostDeductFileVo.setOccursList(tmp53N);
			// 轉換資料格式
			ArrayList<String> file53N = tmp53NPostDeductFileVo.toFile();

//			String outputFilePath53N = outFolder + "PWBCP4CS_53N扣出.txt";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
					titaVo.getTxCode() + "-郵局扣款提出媒體檔_53N", "PWBCP4CS_53N扣出.txt", 2);

			for (String line : file53N) {
				makeFile.put(line);
			}

			long sno = makeFile.close();

			this.info("sno : " + sno);

			makeFile.toFile(sno);
		} else {
			this.info("53N no data !!!!");
		}

		if (cnt8460001 + cnt8460002 >= 1) {
			ArrayList<OccursList> occursList = new ArrayList<OccursList>();
			postDeductFileVo.setOccursList(occursList);
			this.batchTransaction.commit();

			if (cnt8460001 >= 1) {
				PostDeductFileVo tmp8460001PostDeductFileVo = new PostDeductFileVo();

				tmp8460001PostDeductFileVo.put("FootIndex", "2");
				tmp8460001PostDeductFileVo.put("FootDepCode", FormatUtil.padX("", 1));
				tmp8460001PostDeductFileVo.put("FootOrgCode", "846");
				tmp8460001PostDeductFileVo.put("FootDistCode", "0001");
				tmp8460001PostDeductFileVo.put("FootTxDate", transDate);
				tmp8460001PostDeductFileVo.put("FootKeepColA", FormatUtil.padX("", 3));
				tmp8460001PostDeductFileVo.put("FootTotCnt", FormatUtil.pad9("" + cnt8460001, 7));
				tmp8460001PostDeductFileVo.put("FootTotAmt", FormatUtil.pad9(amt8460001 + "00", 13));
				tmp8460001PostDeductFileVo.put("FootKeepColB", FormatUtil.padX("", 16));
				tmp8460001PostDeductFileVo.put("FootSucsCnt", FormatUtil.pad9("", 7));
				tmp8460001PostDeductFileVo.put("FootSucsAmt", FormatUtil.pad9("", 13));
				tmp8460001PostDeductFileVo.put("FootKeepColC", FormatUtil.padX("", 45));

				// 把明細資料容器裝到檔案資料容器內
				tmp8460001PostDeductFileVo.setOccursList(tmp8460001);
				// 轉換資料格式
				ArrayList<String> file8460001 = tmp8460001PostDeductFileVo.toFile();

//				String outputFilePath846 = outFolder + "PWBCP4CS_846扣出.txt";

				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-郵局扣款提出媒體檔_846", "PWBCP4CS_846扣出.txt", 2);

				for (String line : file8460001) {
					makeFile.put(line);
				}

			} else {
				this.info("8460001 no data !!!!");
			}
			this.batchTransaction.commit();

			if (cnt8460002 >= 1) {
				PostDeductFileVo tmp8460002PostDeductFileVo = new PostDeductFileVo();

				tmp8460002PostDeductFileVo.put("FootIndex", "2");
				tmp8460002PostDeductFileVo.put("FootDepCode", FormatUtil.padX("", 1));
				tmp8460002PostDeductFileVo.put("FootOrgCode", "846");
				tmp8460002PostDeductFileVo.put("FootDistCode", "0002");
				tmp8460002PostDeductFileVo.put("FootTxDate", transDate);
				tmp8460002PostDeductFileVo.put("FootKeepColA", FormatUtil.padX("", 3));
				tmp8460002PostDeductFileVo.put("FootTotCnt", FormatUtil.pad9("" + cnt8460002, 7));
				tmp8460002PostDeductFileVo.put("FootTotAmt", FormatUtil.pad9(amt8460002 + "00", 13));
				tmp8460002PostDeductFileVo.put("FootKeepColB", FormatUtil.padX("", 16));
				tmp8460002PostDeductFileVo.put("FootSucsCnt", FormatUtil.pad9("", 7));
				tmp8460002PostDeductFileVo.put("FootSucsAmt", FormatUtil.pad9("", 13));
				tmp8460002PostDeductFileVo.put("FootKeepColC", FormatUtil.padX("", 45));

				// 把明細資料容器裝到檔案資料容器內
				tmp8460002PostDeductFileVo.setOccursList(tmp8460002);
				// 轉換資料格式
				ArrayList<String> file8460002 = tmp8460002PostDeductFileVo.toFile();

				if (cnt8460001 == 0) {
					makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
							titaVo.getTxCode() + "-郵局扣款提出媒體檔_846", "PWBCP4CS_846扣出.txt", 2);
				}

				for (String line : file8460002) {
					makeFile.put(line);
				}

			} else {
				this.info("8460002 no data !!!!");
			}
			long sno = makeFile.close();

			this.info("sno : " + sno);

			makeFile.toFile(sno);
		} else {
			this.info("846 no data !!!!");
		}
		doCnt += cnt8460001 + cnt8460002 + cnt53N;
	}

	private void produceAchDeductMedia(TitaVo titaVo) throws LogicException {
		this.info("produceAchDeductMedia ...");
//		AHP11P_扣出.txt 新光
//		AHP12P_扣出.txt 他行  "‪D:\\temp\\AHP11P_扣出.txt";

		ArrayList<OccursList> tmp11 = new ArrayList<>();
		ArrayList<OccursList> tmp12 = new ArrayList<>();
		this.batchTransaction.commit();

		int iProcessDate = dateUtil.getNowIntegerRoc();
		int iProcessTime = dateUtil.getNowIntegerTime();

		AchDeductFileVo tmp11AchDeductFileVo = new AchDeductFileVo();
		List<AchDeductMedia> lAchDeductMedia1 = new ArrayList<AchDeductMedia>();

		Slice<AchDeductMedia> sAchDeductMedia = null;

		sAchDeductMedia = achDeductMediaService.mediaDateEq(mediaDate, "1", this.index, this.limit, titaVo);

		lAchDeductMedia1 = sAchDeductMedia == null ? null : sAchDeductMedia.getContent();

		if (lAchDeductMedia1 != null && lAchDeductMedia1.size() != 0) {
			this.info("lAchDeductMedia1.size()= " + lAchDeductMedia1.size());
//		header
			tmp11AchDeductFileVo.put("HeadIndex", "BOF");
			tmp11AchDeductFileVo.put("HeadDataCode", "ACHP01");
			tmp11AchDeductFileVo.put("HeadProcessDate", FormatUtil.pad9("" + iProcessDate, 8));
			tmp11AchDeductFileVo.put("HeadProcessTime", FormatUtil.pad9("" + iProcessTime, 6));
			tmp11AchDeductFileVo.put("HeadSubmitUnit", "1030000");
			tmp11AchDeductFileVo.put("HeadReceiveUnit", "9990250");
			tmp11AchDeductFileVo.put("HeadNote", FormatUtil.padX("", 123));

//		occurs
			tmp11 = setAchOccurs(lAchDeductMedia1, titaVo);

//		footer
			tmp11AchDeductFileVo.put("FootIndex", "EOF");
			tmp11AchDeductFileVo.put("FootDataCode", "ACHP01");
			tmp11AchDeductFileVo.put("FootProgressDate", FormatUtil.pad9("" + iProcessDate, 8));
			tmp11AchDeductFileVo.put("FootSenderUnit", "1030000");
			tmp11AchDeductFileVo.put("FootReceiveUnit", "9990250");
			tmp11AchDeductFileVo.put("FootTotCnt", FormatUtil.pad9("" + cnt11, 8));
			tmp11AchDeductFileVo.put("FootTotAmt", FormatUtil.pad9("" + amt11, 16));
			tmp11AchDeductFileVo.put("FootNote", FormatUtil.padX("", 105));

			// 把明細資料容器裝到檔案資料容器內
			tmp11AchDeductFileVo.setOccursList(tmp11);
			// 轉換資料格式
			ArrayList<String> file11 = tmp11AchDeductFileVo.toFile();

//			String outputFilePath11 = outFolder + "AHP11P_扣出.txt";

			if (cnt11 >= 1) {
				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-銀行扣款提出媒體檔_新光", "AHP11P_扣出.txt", 2);
				this.batchTransaction.commit();

				for (String line : file11) {
					makeFile.put(line);
				}

				long sno = makeFile.close();

				this.info("sno : " + sno);

				makeFile.toFile(sno);
			} else {
				this.info("cnt11 == 0 ...");
			}

		} else {
			this.info("lAchDeductMedia1 is null");
		}

		AchDeductFileVo tmp12AchDeductFileVo = new AchDeductFileVo();
		List<AchDeductMedia> lAchDeductMedia2 = new ArrayList<AchDeductMedia>();

		Slice<AchDeductMedia> s2AchDeductMedia = null;

		s2AchDeductMedia = achDeductMediaService.mediaDateEq(mediaDate, "2", this.index, this.limit, titaVo);

		lAchDeductMedia2 = s2AchDeductMedia == null ? null : s2AchDeductMedia.getContent();

		if (lAchDeductMedia2 != null && lAchDeductMedia2.size() != 0) {

//		header
			tmp12AchDeductFileVo.put("HeadIndex", "BOF");
			tmp12AchDeductFileVo.put("HeadDataCode", "ACHP01");
			tmp12AchDeductFileVo.put("HeadProcessDate", FormatUtil.pad9("" + iProcessDate, 8));
			tmp12AchDeductFileVo.put("HeadProcessTime", iProcessTime);
			tmp12AchDeductFileVo.put("HeadSubmitUnit", "1030000");
			tmp12AchDeductFileVo.put("HeadReceiveUnit", "9990250");
			tmp12AchDeductFileVo.put("HeadNote", FormatUtil.padX("", 123));

//		occurs
			tmp12 = setAchOccurs(lAchDeductMedia2, titaVo);

//		footer
			tmp12AchDeductFileVo.put("FootIndex", "EOF");
			tmp12AchDeductFileVo.put("FootDataCode", "ACHP01");
			tmp12AchDeductFileVo.put("FootProgressDate", FormatUtil.pad9("" + iProcessDate, 8));
			tmp12AchDeductFileVo.put("FootSenderUnit", "1030000");
			tmp12AchDeductFileVo.put("FootReceiveUnit", "9990250");
			tmp12AchDeductFileVo.put("FootTotCnt", FormatUtil.pad9("" + cnt12, 8));
			tmp12AchDeductFileVo.put("FootTotAmt", FormatUtil.pad9("" + amt12, 16));
			tmp12AchDeductFileVo.put("FootNote", FormatUtil.padX("", 105));

			// 把明細資料容器裝到檔案資料容器內
			tmp12AchDeductFileVo.setOccursList(tmp12);
			// 轉換資料格式
			ArrayList<String> file12 = tmp12AchDeductFileVo.toFile();

//			String outputFilePath12 = outFolder + "AHP12P_扣出.txt";

			if (cnt12 >= 1) {
				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-銀行扣款提出媒體檔_他行", "AHP12P_扣出.txt", 2);
				this.batchTransaction.commit();

				for (String line : file12) {
					makeFile.put(line);
				}

				long sno = makeFile.close();

				this.info("sno : " + sno);

				makeFile.toFile(sno);
			} else {
				this.info("cnt12 == 0 ...");
			}
		} else {
			this.info("lAchDeductMedia2 is null");
		}
		doCnt += cnt11 + cnt12;

	}

//	table資料寫入File明細
	private ArrayList<OccursList> setAchOccurs(List<AchDeductMedia> lAchDeductMedia, TitaVo titaVo)
			throws LogicException {
		this.info("setAchOccurs ...");
		ArrayList<OccursList> tmp = new ArrayList<>();
		if (lAchDeductMedia != null && lAchDeductMedia.size() != 0) {
			int n = 0;
			for (AchDeductMedia tAchDeductMedia : lAchDeductMedia) {

//				if (tAchDeductMedia.getRepayAmt().compareTo(BigDecimal.ZERO) <= 0) {
//					this.info("RepayAmt : " + tAchDeductMedia.getRepayAmt() + " <= 0 continue... ");
//					doReport("AMT", "", tAchDeductMedia, titaVo);
//					continue;
//				}

				n++;
				if (n % commitCnt == 0) {
					this.batchTransaction.commit();
				}
				this.info("tAchDeductMedia.getMediaKind()=" + tAchDeductMedia.getMediaKind());

				if ("1".equals(tAchDeductMedia.getMediaKind())) {
					cnt11 = cnt11 + 1;
					amt11 = amt11.add(tAchDeductMedia.getRepayAmt());
				} else if ("2".equals(tAchDeductMedia.getMediaKind())) {
					cnt12 = cnt12 + 1;
					amt12 = amt12.add(tAchDeductMedia.getRepayAmt());
				}

				OccursList occursList = new OccursList();

				occursList.putParam("OccTransType", "N");
				occursList.putParam("OccTransCate", "SD");
				occursList.putParam("OccTransCode", "801");
				occursList.putParam("OccTransSeq",
						FormatUtil.pad9("" + tAchDeductMedia.getAchDeductMediaId().getMediaSeq(), 6));
				occursList.putParam("OccSenderNo", "1030116");
				occursList.putParam("OccSenderAcct", "00116101001006");
				if ("812".equals(tAchDeductMedia.getRepayBank())) {
					occursList.putParam("OccWdBankNo", "8120012");
				} else if ("006".equals(tAchDeductMedia.getRepayBank())) {
					occursList.putParam("OccWdBankNo", "0060567");
				} else if ("103".equals(tAchDeductMedia.getRepayBank())) {
					occursList.putParam("OccWdBankNo", "1030019");
				} else {
					occursList.putParam("OccWdBankNo", FormatUtil.padX("", 7));
				}
				occursList.putParam("OccCustAcctNo", FormatUtil.pad9(tAchDeductMedia.getRepayAcctNo(), 14));
				occursList.putParam("OccRepayAmt", FormatUtil.pad9("" + tAchDeductMedia.getRepayAmt(), 10));
				occursList.putParam("OccReturnCode", FormatUtil.pad9("", 2));
				occursList.putParam("OccIndicator", "B");
				occursList.putParam("OccSenderId", FormatUtil.padX("03458902", 10));
				String custId = "";
				if ("00".equals(tAchDeductMedia.getRelationCode())) {
					CustMain tCustMain = custMainService.custNoFirst(tAchDeductMedia.getCustNo(),
							tAchDeductMedia.getCustNo(), titaVo);
					custId = tCustMain.getCustId();
				} else {
					custId = tAchDeductMedia.getRelCustId();
				}

				occursList.putParam("OccCustId", FormatUtil.padX(custId, 10));
				occursList.putParam("OccCompanyCode", FormatUtil.padX("2888", 6));
				occursList.putParam("OccOTransDate", FormatUtil.pad9("", 8));
				occursList.putParam("OccOTransSeq", FormatUtil.pad9("", 6));
				occursList.putParam("OccOTransOrder", FormatUtil.padX("", 1));
				occursList.putParam("OccCustSeq",
						FormatUtil.padX("" + FormatUtil.pad9("" + tAchDeductMedia.getCustNo(), 7), 20));

				int prevIntDate = 0;

				if (tAchDeductMedia.getPrevIntDate() >= 10101) {
					prevIntDate = tAchDeductMedia.getPrevIntDate() + 19110000;
				}

//				戶號7+額度3+入帳扣款別1+繳息迄日8 左靠右補空白
				occursList.putParam("OccSenderRemarker",
						FormatUtil.padX(
								FormatUtil.pad9("" + tAchDeductMedia.getCustNo(), 7)
										+ FormatUtil.pad9("" + tAchDeductMedia.getFacmNo(), 3)
										+ tAchDeductMedia.getAchRepayCode() + FormatUtil.pad9("" + prevIntDate, 8),
								20));
				// 存摺摘要 1.左靠右補空白 2.非新光 : 放空白 3.新光 : 火險放705, 其他放801
				if ("103".equals(tAchDeductMedia.getRepayBank())) {
					if (tAchDeductMedia.getRepayType() == 5) {
						occursList.putParam("OccAbstract", FormatUtil.padX("705", 10));
					} else {
						occursList.putParam("OccAbstract", FormatUtil.padX("801", 10));
					}
				} else {
					occursList.putParam("OccAbstract", FormatUtil.padX(" ", 10));
				}
				
				occursList.putParam("OccNote", FormatUtil.padX("", 2));

				tmp.add(occursList);
			}
		}

		return tmp;
	}

	private void deleteOldPostMedia(int today, TitaVo titaVo) throws LogicException {
		List<PostDeductMedia> lPostDeductMedia = new ArrayList<PostDeductMedia>();

		Slice<PostDeductMedia> sPostDeductMedia = null;

		sPostDeductMedia = postDeductMediaService.mediaDateEq(today, this.index, this.limit, titaVo);

		lPostDeductMedia = sPostDeductMedia == null ? null : sPostDeductMedia.getContent();

		if (lPostDeductMedia != null && lPostDeductMedia.size() != 0) {
			int n = 0;
			for (PostDeductMedia tPostDeductMedia : lPostDeductMedia) {

				n++;
				if (n % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				postDeductMediaService.holdById(tPostDeductMedia, titaVo);
				try {
					postDeductMediaService.delete(tPostDeductMedia, titaVo);
					this.info("L4452 PostDeductMedia delete Success");
				} catch (DBException e) {
					if (e.getErrorId() == 2)
						throw new LogicException("E0008", "L4452 PostDeductMedia delete " + e.getErrorMsg());
				}
			}
		}
	}

	private void deleteOldAchMedia(int today, String mediaKind, TitaVo titaVo) throws LogicException {
		List<AchDeductMedia> lAchDeductMedia = new ArrayList<AchDeductMedia>();

		Slice<AchDeductMedia> sAchDeductMedia = null;

		sAchDeductMedia = achDeductMediaService.mediaDateEq(today, mediaKind, this.index, this.limit, titaVo);

		lAchDeductMedia = sAchDeductMedia == null ? null : sAchDeductMedia.getContent();

		if (lAchDeductMedia != null && lAchDeductMedia.size() != 0) {
			int n = 0;
			for (AchDeductMedia tAchDeductMedia : lAchDeductMedia) {

				n++;
				if (n % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				achDeductMediaService.holdById(tAchDeductMedia, titaVo);
				try {
					achDeductMediaService.delete(tAchDeductMedia, titaVo);
					this.info("L4452 AchDeductMedia delete Success");
				} catch (DBException e) {
					if (e.getErrorId() == 2)
						throw new LogicException("E0008", "L4452 AchDeductMedia delete " + e.getErrorMsg());
				}
			}
		}
	}

	private void updateBankDeductDtl(BankDeductDtl t2BankDeductDtl, int mediaDate, String mediaKind, int mediaSeq,
			String mediaCode, TitaVo titaVo) throws LogicException {
		this.info("updateBankDeductDtl Start...");

		BankDeductDtl tBankDeductDtl = new BankDeductDtl();

		tBankDeductDtl = bankDeductDtlService.holdById(t2BankDeductDtl.getBankDeductDtlId(), titaVo);

		if (tBankDeductDtl.getAcDate() > 0) {
			throw new LogicException("E0005", "L4452 該筆已入帳");
		}

		tBankDeductDtl.setMediaCode(mediaCode);
		tBankDeductDtl.setMediaDate(mediaDate);
		tBankDeductDtl.setMediaKind(mediaKind);
		tBankDeductDtl.setMediaSeq(mediaSeq);

		this.info("Seq = " + mediaSeq);

		try {
			bankDeductDtlService.update(tBankDeductDtl, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "L4452 BankDeductDtl update " + e.getErrorMsg());
		}
	}

	private void amlCheckForm(List<BankDeductDtl> lBankDeductDtl, TitaVo titaVo) throws LogicException {
		txAmlCom.setTxBuffer(this.txBuffer);
		this.batchTransaction.commit();
		this.info("amlCheckForm Start");

		int n = 0;
		for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
			if (!"0".equals(tBankDeductDtl.getAmlRsp())) {
				n++;
				if (n % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				CheckAmlVo tCheckAmlVo = txAmlCom.deduct(tBankDeductDtl, titaVo);
				String amlRsp = tCheckAmlVo.getConfirmStatus();
				tBankDeductDtl.setAmlRsp(amlRsp);
				try {
					bankDeductDtlService.update(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "L4452 BankDeductDtl update " + e.getErrorMsg());
				}
			}
		}

		this.info("amlCheckForm End");
	}

	private void doReport(String reason, String confirmCode, BankDeductDtl tBankDeductDtl, TitaVo titaVo) {
		this.info("doReport...");
		this.info("ConfirmCode : " + confirmCode);
		this.info("RepayType : " + tBankDeductDtl.getRepayType());

		OccursList occursList = new OccursList();
		String repayType = "";
		String note = "";
		int repayTypeInt = 0;

		int custno = tBankDeductDtl.getCustNo();
		String custname = "";
		CustMain tCustMain = custMainService.custNoFirst(custno, custno, titaVo);

		if (tCustMain != null) {
			custname = tCustMain.getCustName();
		}

		if (tBankDeductDtl.getRepayType() == 3) {
			repayTypeInt = 1;
		} else {
			repayTypeInt = tBankDeductDtl.getRepayType();
		}

		CdCode t2CdCode = cdCodeService.getItemFirst(4, "RepayTypeSearch", FormatUtil.pad9("" + repayTypeInt, 2),
				titaVo);

		if (t2CdCode != null) {
			repayType = t2CdCode.getItem();
		}

		note = reason;
		occursList.putParam("OORepayBank", tBankDeductDtl.getRepayBank());
		occursList.putParam("OOCustNo", custno);
		occursList.putParam("OOFacmNo", tBankDeductDtl.getFacmNo());
		occursList.putParam("OOCustName", custname);
		occursList.putParam("OORepayType", repayType);
		occursList.putParam("OORepayAmt", tBankDeductDtl.getRepayAmt());
		occursList.putParam("OONote", note);
		unDoList.add(occursList);
	}

	private ArrayList<BankDeductDtl> postMediaSorting(ArrayList<BankDeductDtl> lBankDeductDtl) {
		this.info("setPostDeductMedia Start...");
//		依儲金帳號排序後，先依區處代號排序(0001.0002)，再依計息迄日由小到大，最後才依扣款金額由大到小排序
//		RepayAcctNo > RepayType(5>4>1) DESC > PrevIntDate > RepayAmt DESC
//		                                       排序為-1>0>1 3最後>由大到小
		lBankDeductDtl.sort((c1, c2) -> {
			int result = 0;
			if (c1.getRepayAcctNo().compareTo(c2.getRepayAcctNo()) != 0) {
				result = c1.getRepayAcctNo().compareTo(c2.getRepayAcctNo());
			} else if (c1.getRepayType() - c2.getRepayType() != 0) {
				if (c1.getRepayType() == 3) {
					result = 1;
				} else if (c2.getRepayType() == 3) {
					result = -1;
				} else {
					result = c2.getRepayType() - c1.getRepayType();
				}
			} else if (c1.getPrevIntDate() - c2.getPrevIntDate() != 0) {
				result = c1.getPrevIntDate() - c2.getPrevIntDate();
			} else if ((c1.getRepayAmt().subtract(c2.getRepayAmt())).compareTo(BigDecimal.ZERO) != 0) {
				result = c2.getRepayAmt().compareTo(c1.getRepayAmt());
			} else {
				result = 0;
			}
			return result;
		});

		return lBankDeductDtl;
	}

	private ArrayList<BankDeductDtl> achMediaSorting(ArrayList<BankDeductDtl> lBankDeductDtl) {
		this.info("achMediaSorting Start");
//		CustNo, FacmNo, RepayType, PayIntDate 
		lBankDeductDtl.sort((c1, c2) -> {
			int result = 0;
			if (c1.getCustNo() - c2.getCustNo() != 0) {
				result = c1.getCustNo() - c2.getCustNo();
			} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
				result = c1.getFacmNo() - c2.getFacmNo();
			} else if (c1.getRepayType() - c2.getRepayType() != 0) {
				if (c1.getRepayType() == 3) {
					result = 1;
				} else if (c2.getRepayType() == 3) {
					result = -1;
				} else {
					result = c2.getRepayType() - c1.getRepayType();
				}
			} else if (c1.getPayIntDate() - c2.getPayIntDate() != 0) {
				result = c1.getPayIntDate() - c2.getPayIntDate();
			} else {
				result = 0;
			}
			return result;
		});
		return lBankDeductDtl;
	}

	private String authX(String auth, TitaVo titaVo) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "AuthStatusCode", auth, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

	private String amlRspX(String amlRsp, TitaVo titaVo) {
		String result = "";

		String srt = FormatUtil.pad9(amlRsp.trim(), 1);

		CdCode cdCode = cdCodeService.getItemFirst(4, "AmlCheckItem", srt, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

//	暫時紀錄戶號額度
	private class tmpFacm {
		private int custNo = 0;
		private int facmNo = 0;
		private int repayType = 0;
		private int payIntDate = 0;

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + ", repayType=" + repayType + ", payIntDate="
					+ payIntDate + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
			result = prime * result + payIntDate;
			result = prime * result + repayType;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			if (payIntDate != other.payIntDate)
				return false;
			if (repayType != other.repayType)
				return false;
			return true;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private void setRepayType(int repayType) {
			this.repayType = repayType;
		}

		private void setPayIntDate(int payIntDate) {
			this.payIntDate = payIntDate;
		}

		private L4452Batch getEnclosingInstance() {
			return L4452Batch.this;
		}
	}
}