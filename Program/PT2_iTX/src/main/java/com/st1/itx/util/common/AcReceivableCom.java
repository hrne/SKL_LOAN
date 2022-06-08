package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 會計銷帳檔處理<BR>
 * 1.run ： 入帳更新銷帳檔 call by AcEntetCom<BR>
 * 1.1 業務科目記號 = 1 資負明細科目（放款、催收款項) 且非L6801:放款戶帳冊別轉換<BR>
 * 1.2 銷帳科目記號 = 1－會計銷帳科目 2－業務銷帳科目 3-未收款, 4-短繳期金,5-另收欠款<BR>
 * 1.3 銷帳科目記號 = 8-核心銷帳碼科目(不寫銷帳檔)<BR>
 * 1.4 更新火險單續保檔 AcDate、更新法務費檔 CloseDate<BR>
 * 1.4.1 貸方入帳：TMI 暫收款－火險保費 F09 暫付款－火險保費 F25 催收款項－火險費用，收款不含L618B火險保費催收作業<BR>
 * 1.4.2 貸方入帳：F07 暫付法務費 F24 催收款項－法務費用，收款不含L618C 法務費轉列催收作業 <BR>
 * 1.5 合併銷帳檔及會計分錄的jsonFields<BR>
 * 1.6 編銷帳編號 AC+西元年後兩碼+流水號六碼（ 銷帳科目記號 = 1-會計銷帳科目 ＆＆ 起銷帳記號 = 0-起帳）<BR>
 *
 * 2.mnt：未出帳科目更新銷帳檔 call by LXXXX<BR>
 * 2.1 銷帳科目記號：2-核心出帳、3-未收費用、4-短繳期金、5-另收欠款 2.2 核心出帳更新總帳檔餘(call AcMain)
 * 
 * @author st1
 *
 */
@Component("acReceivableCom")
@Scope("prototype")
public class AcReceivableCom extends TradeBuffer {
	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public ForeclosureFeeService foreclosureFeeService;

	@Autowired
	public AcMainCom acMainCom;

	@Autowired
	public GSeqCom gSeqCom;

	@Autowired
	public DataLog dataLog;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	private TitaVo titaVo;
	private AcDetail ac = new AcDetail();
	private String debits[] = { "1", "5", "6", "9" }; // 資產、支出為借方科目，負債、收入為貸方科目
	private List<String> debitsList = Arrays.asList(debits);
	private int wkRvFg = 0; // 起銷帳記號 (0-起帳, 1-銷帳 2-訂正)
	private int wkOpenAcDate = 0; // 起帳日期
	private BigDecimal wkTxAmt = BigDecimal.ZERO; // 起銷帳金額
	private String wkRvNo = ""; // 銷帳編號
	private String wkAcctCode = ""; // 業務科目
	private AcReceivable tAcReceivable = new AcReceivable();
	private AcReceivableId tAcReceivableId = new AcReceivableId();
	private AcReceivable beforeAcReceivable;
	private CdAcCode tCdAcCode = new CdAcCode();
	private TempVo tTempVo = new TempVo();
	private List<AcReceivable> mntRvList = new ArrayList<AcReceivable>();

	/*----------- 會計分錄更新銷帳檔 -------------- */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcReceivableCom ... ");
		wkOpenAcDate = 0;
		int bizTbsdy = this.txBuffer.getTxBizDate().getTbsDy();
		this.titaVo = titaVo;
		int AcHCode = this.txBuffer.getTxCom().getBookAcHcode(); // 帳務訂正記號
		int idx = 0;

//		帳務訂正記號  AcHCode   0.正常     1.當日訂正     2.隔日訂正   ；   訂正反序          
		// 1.業務科目記號 = 1 資負明細科目（放款、催收款項) 且非L6801:放款戶帳冊別轉換
		// 2.銷帳科目記號 <> 0 0－非銷帳科目 1－會計銷帳科目 2－業務銷帳科目 3-未收款, 4-短繳期金,
		// 5-另收欠款,8-核心銷帳碼科目(不寫銷帳檔)
		for (int i = 0; i < this.txBuffer.getAcDetailList().size(); i++) {
			if (AcHCode == 0) {
				idx = i;
			} else {
				idx = this.txBuffer.getAcDetailList().size() - 1 - i;
			}

			wkRvFg = 0;
			wkOpenAcDate = 0;
			wkTxAmt = BigDecimal.ZERO;
			wkRvNo = "";
			wkAcctCode = "";
			ac = this.txBuffer.getAcDetailList().get(idx);
			tTempVo.clear();
			tTempVo = tTempVo.getVo(ac.getJsonFields());
			if ((ac.getAcctFlag() == 1 && !"L6801".equals(titaVo.getTxcd()))
					|| (ac.getReceivableFlag() > 0 && ac.getReceivableFlag() < 8)) {
				// 銷帳記號 0-起帳 1-銷帳
				// 1.ReceivableFlag >= 3 銷帳
				// 2.借方科目借方 ("1", "5","6","9") or 貸方科目貸方 -> 0-起帳, else 1-銷帳
				// 3.TRO借新還舊->相反(貸方科目，先借後貸)
				if (ac.getReceivableFlag() >= 3) {
					wkRvFg = 1;
					if (AcHCode == 2) {
						wkRvFg = 0;
					}
				} else if ((debitsList.contains(ac.getAcNoCode().substring(0, 1)) && ac.getDbCr().equals("D"))
						|| (!debitsList.contains(ac.getAcNoCode().substring(0, 1)) && ac.getDbCr().equals("C")))
					wkRvFg = 0;
				else
					wkRvFg = 1;
				if (ac.getAcctCode().equals("TRO")) {
					if (wkRvFg == 0)
						wkRvFg = 1;
					else
						wkRvFg = 0;
				}
				// L3250 暫收款退還沖正(轉換前交易)，一律為起帳
				if ("L3250".equals(titaVo.getTxcd())) {
					wkRvFg = 0;
				}

				// 銷帳業務科目
				wkAcctCode = ac.getAcctCode();
				if (tTempVo.getParam("RvAcctCode").length() == 3) {
					wkAcctCode = tTempVo.getParam("RvAcctCode");
				}
				if (tTempVo.getParam("OpenAcDate").length() > 0) {
					wkOpenAcDate = parse.stringToInteger(tTempVo.getParam("OpenAcDate"));
				}
				wkRvNo = ac.getRvNo().trim();
				// 短繳本金Zxx銷帳，除銷放款科目帳外，需另作短繳本金Zxx銷帳
				// 銷帳科目記號ReceivableFlag = 4-短繳期金
				if (ac.getReceivableFlag() == 4 && "3".equals(ac.getAcctCode().substring(0, 1))) {
					tAcReceivable = new AcReceivable();
					tAcReceivable.setAcctCode(wkAcctCode);
					tAcReceivable.setReceivableFlag(ac.getReceivableFlag());
					tAcReceivable.setRvAmt(ac.getTxAmt());
					tAcReceivable.setCustNo(ac.getCustNo());
					tAcReceivable.setFacmNo(ac.getFacmNo());
					tAcReceivable.setRvNo(parse.IntegerToString(ac.getBormNo(), 3));
					mntRvList.add(tAcReceivable);
					wkAcctCode = ac.getAcctCode();
				}
				// 暫收款－聯貸費攤提， 需轉換攤提銷帳科目記號 3:未收費用 -> 5.另收欠款
				if (wkRvFg == 0 && "TSL".equals(ac.getAcctCode())) {
					procSyndLoan(titaVo.getHCodeI());
					wkAcctCode = ac.getAcctCode();
					wkRvNo = "";
				}

				// 設定
				procSetting(AcHCode);
				this.info("AcReceivable RvFg=" + wkRvFg + ", RvAmt=" + wkTxAmt + ", RvNo=" + wkRvNo
						+ ", ReceivableFlag=" + ac.getReceivableFlag());

				// 銷帳編號
				this.txBuffer.getAcDetailList().get(idx).setRvNo(wkRvNo);

				// 更新
				procUpdate(AcHCode, bizTbsdy);

				// TMI 暫收款－火險保費 F09 暫付款－火險保費 F25 催收款項－火險費用
				// 收款不含L618B 火險保費催收作業
				if (!titaVo.getTxcd().equals("L618B") && ac.getDbCr().equals("C")) {
					if (ac.getAcctCode().equals("TMI") || ac.getAcctCode().equals("F09")
							|| ac.getAcctCode().equals("F25")) {
						updInsuRenew(AcHCode, bizTbsdy, ac);
					}
				}

				// 更新法務費檔 AcDate
				// F07 暫付法務費 F24 催收款項－法務費用
				// 收款不含L618C 法務費轉列催收作業
				if (!titaVo.getTxcd().equals("L618C") && ac.getDbCr().equals("C")) {
					if (ac.getAcctCode().equals("F07") || ac.getAcctCode().equals("F24")) {
						updForeclosureFee(AcHCode, bizTbsdy, ac);

					}
				}

				this.info("tAcReceivable=" + tAcReceivable);
			}
		}

		// 短繳本金，除銷放款科目帳外，需另作短繳本金Zxx銷帳
		if (mntRvList != null && mntRvList.size() > 0) {
			mnt(1, mntRvList, titaVo);
		}

		return null;
	}

	/**
	 * 未出帳科目更新銷帳檔(銷帳科目記號：2-核心出帳、3-未收費用、4-短繳期金、5-另收欠款)
	 * 
	 * @param RvFg   銷帳記號 0-起帳 1-銷帳 2-起帳刪除 3-起帳變更 4.已銷科目核心出帳
	 * @param rvList List of AcReceivable
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void mnt(int RvFg, List<AcReceivable> rvList, TitaVo titaVo) throws LogicException {
		this.info("AcReceivableCom mnt ... ");
		this.titaVo = titaVo;
		int bizTbsdy = this.txBuffer.getTxBizDate().getTbsDy();
		// 業務科目記號 <> 0 or 銷帳科目記號 <> 0
		for (AcReceivable rv : rvList) {
			this.info("AcReceivableCom mnt " + rv);

			if (rv.getReceivableFlag() < 2 || rv.getReceivableFlag() > 5) { // 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				throw new LogicException(titaVo, "E6003",
						"AcReceivable  ReceivableFlag must be 3,4,5 ->" + rv.getReceivableFlag());
			}

			// 核心出帳不可變更
			if (RvFg == 3 && rv.getReceivableFlag() == 2) {
				throw new LogicException(titaVo, "E6003",
						"AcReceivable  ReceivableFlag can not be 2 ->" + rv.getReceivableFlag());
			}
			// 帳冊別
			if (rv.getAcBookCode().isEmpty()) {
				rv.setAcBookCode(this.txBuffer.getSystemParas().getAcBookCode());

			}
			if (rv.getAcSubBookCode().isEmpty()) {
				rv.setAcSubBookCode(this.txBuffer.getSystemParas().getAcSubBookCode());
			}
			// 2-起帳刪除 3-起帳變更
			if (RvFg == 2 || RvFg == 3) {
				tAcReceivableId = new AcReceivableId();
				tAcReceivableId.setAcctCode(rv.getAcctCode());
				tAcReceivableId.setCustNo(rv.getCustNo());
				tAcReceivableId.setFacmNo(rv.getFacmNo());
				tAcReceivableId.setRvNo(rv.getRvNo());
				tAcReceivable = acReceivableService.holdById(tAcReceivableId, titaVo); // holdById
				if (tAcReceivable == null) {
					throw new LogicException(titaVo, "E6003", "AcReceivable.mnt notfound " + tAcReceivableId);
				}
				if (tAcReceivable.getRvBal().compareTo(tAcReceivable.getRvAmt()) != 0) {
					throw new LogicException(titaVo, "E6003", "已入帳資料不可修改、刪除" + tAcReceivableId);
				}
				if (RvFg == 2) {
					try {
						acReceivableService.delete(tAcReceivable, titaVo); // delete
					} catch (DBException e) {
						e.printStackTrace();
						throw new LogicException(titaVo, "E6003",
								"AcReceivable.mnt delete " + tAcReceivableId + e.getErrorMsg());
					}
				} else {
					beforeAcReceivable = (AcReceivable) dataLog.clone(tAcReceivable);
					mntAcDetail(RvFg, rv, titaVo);
					procSetting(titaVo.getHCodeI());
					newAcReceivable(bizTbsdy);
					updAcReceivable(0, bizTbsdy);
					try {
						acReceivableService.update2(tAcReceivable, titaVo); // update
					} catch (DBException e) {
						e.printStackTrace();
						throw new LogicException(titaVo, "E6003",
								"AcReceivable.mnt update2 " + tAcReceivableId + e.getErrorMsg());
					}
					// [契變手續費紀錄]變更前變更後
					if ("F29".equals(rv.getAcctCode())) {
						dataLog.setEnv(titaVo, beforeAcReceivable, tAcReceivable);
						dataLog.exec();
					}

				}
			}
			// AcReceivable -> AcDetail
			mntAcDetail(RvFg, rv, titaVo);

			// 0-起帳 1-銷帳
			if (RvFg == 0 || RvFg == 1) {
				/* 設定 */
				procSetting(titaVo.getHCodeI());
				/* 更新 */
				procUpdate(titaVo.getHCodeI(), bizTbsdy);
			}

			// 核心出帳更新總帳檔餘額
			if (rv.getReceivableFlag() == 2) { // 2-核心出帳
				acMainCom.setTxBuffer(this.txBuffer);
				acMainCom.core(titaVo.getHCodeI(), ac, titaVo);
			}
			this.info("AcReceivable mnt=" + ", RvBal=" + tAcReceivable.getRvBal());
		}

		return;

	}

	// AcReceivable -> AcDetail
	private AcDetail mntAcDetail(int RvFg, AcReceivable rv, TitaVo titaVo) throws LogicException {
		ac = new AcDetail();
		// RvFg 0-起帳 1-銷帳 2-起帳刪除 3-起帳變更 4.已銷科目核心出帳
		wkRvFg = 0; // 起銷帳記號 (0-起帳, 1-銷帳 2-訂正)
		if (RvFg == 0 || RvFg == 3) {
			wkRvFg = 0; // 起銷帳記號 (0-起帳)
		} else {
			wkRvFg = 1; // 起銷帳記號 (1-銷帳)
		}

		wkOpenAcDate = 0; // 起帳日期
		wkTxAmt = BigDecimal.ZERO; // 起銷帳金額
		wkRvNo = ""; // 銷帳編號
		/*----------- 必要參數 ----------*/
		// 科子細目、借貸別
		if (rv.getReceivableFlag() == 2) { // 2-核心出帳
			tCdAcCode = cdAcCodeService.acCodeAcctFirst(rv.getAcctCode(), titaVo);
			if (tCdAcCode == null) {
				throw new LogicException(titaVo, "E6003", "CdAcCode NotFound " + rv.getAcctCode());
			}
			ac.setAcNoCode(tCdAcCode.getAcNoCode()); // 科子細目
			ac.setAcSubCode(tCdAcCode.getAcSubCode());
			ac.setAcDtlCode(tCdAcCode.getAcDtlCode());
			if (debitsList.contains(ac.getAcNoCode().substring(0, 1))) {
				if (RvFg == 0)
					ac.setDbCr("D"); // 借方科目起帳
				else
					ac.setDbCr("C"); // 借方科目銷帳
			} else {
				if (RvFg == 0)
					ac.setDbCr("C"); // 貸方科目起帳
				else
					ac.setDbCr("D"); // 貸方科目銷帳
			}
		} else {
			ac.setAcNoCode(" ");
			ac.setAcSubCode(" ");
			ac.setAcDtlCode(" ");
			if (RvFg == 0)
				ac.setDbCr("D"); // 借
			else
				ac.setDbCr("C"); // 貸
		}
		ac.setTxAmt(rv.getRvAmt()); // 記帳金額
		ac.setAcctCode(rv.getAcctCode()); // 業務科目
		wkAcctCode = rv.getAcctCode();// 業務科目
		ac.setCustNo(rv.getCustNo());// 戶號+額度+撥款
		ac.setFacmNo(rv.getFacmNo()); //
		ac.setRvNo(rv.getRvNo()); // 銷帳編號
		wkRvNo = ac.getRvNo().trim();
		/*----------- 選擇參數 ----------*/
		ac.setSlipNote(rv.getSlipNote()); // 傳票摘要
		ac.setAcBookCode(rv.getAcBookCode()); // 帳冊別
		ac.setAcSubBookCode(rv.getAcSubBookCode()); // 區隔帳冊
		ac.setJsonFields(rv.getJsonFields());// jsonFields 欄
		/*----------- TITA值 -----------*/
		ac.setTitaTxCd(titaVo.getTxcd()); // 交易代號
		if (titaVo.getCurName().trim().isEmpty())
			ac.setCurrencyCode("TWD");
		else
			ac.setCurrencyCode(titaVo.getCurName()); // 幣別 */
		ac.setBranchNo(titaVo.getAcbrNo()); // 記帳單位別
		ac.setTitaKinbr(titaVo.getKinbr()); // 登錄單位別
		ac.setTitaTlrNo(this.txBuffer.getTxCom().getRelTlr()); // 登錄經辦
		ac.setTitaTxtNo(this.txBuffer.getTxCom().getRelTno()); // 登錄交易序號
		/*----------- 自動設定 -----------*/
		ac.setAcDate(titaVo.getEntDyI()); // 會計日期
		wkOpenAcDate = rv.getOpenAcDate(); // 起帳日期
		ac.setAcBookFlag(0); // 帳冊別記號
		ac.setAcctFlag(0); // 業務科目記號
		ac.setReceivableFlag(rv.getReceivableFlag()); // 銷帳科目記號 2－業務銷帳科目
		return ac;
	}

	/* 設定 */
	private void procSetting(int AcHcode) throws LogicException {

		// 起銷帳金額wkTxAmt
		// 起帳(+),銷帳(-)，當日訂正 +/- 相反 ※隔日訂正其借貸別已作相反處理
		if (wkRvFg == 0)
			wkTxAmt = ac.getTxAmt();
		else
			wkTxAmt = BigDecimal.ZERO.subtract(ac.getTxAmt());

		// 當日訂正 +/- 相反 ※隔日訂正其借貸別已作相反處理
		if (AcHcode == 1) {
			wkTxAmt = BigDecimal.ZERO.subtract(wkTxAmt);
		}

		// 起銷帳記號wkRvFg (0-起帳, 1-銷帳 2-訂正)
		if (AcHcode > 0) {
			wkRvFg = 2;
		}

		this.info("procSetting EntAc =" + ac.getEntAc() + ",ReceivableFlag = " + ac.getReceivableFlag() + ",AcHcode="
				+ AcHcode + ",wkRvFg=" + wkRvFg + ",wkTxAmt=" + wkTxAmt + ",AcctCode=" + ac.getAcctCode());
		this.info("debitsList.contains " + debitsList.contains(ac.getAcNoCode().substring(0, 1)) + "AcNoCode="
				+ ac.getAcNoCode() + ",DbCr=" + ac.getDbCr());

		// 銷帳編號wkRvNo primary key 不可有null, 放 " "
		if (wkRvNo.isEmpty()) {
			wkRvNo = " ";
			// 資負明細科目（放款、催收款項..) --> 撥款序號(擔保放款、催收款項)
			if (ac.getAcctFlag() == 1)
				wkRvNo = FormatUtil.pad9(String.valueOf(ac.getBormNo()), 3);
			// 銷帳科目記號 = 1-會計銷帳科目 && 起銷帳記號 = 0-起帳 -> 會計銷帳編號
			// 編號日期, 編號方式=1:年度編號, 業務類別, 交易種類
			else if (ac.getReceivableFlag() == 1 && wkRvFg == 0)
				// AC+西元年後兩碼+流水號六碼
				wkRvNo = "AC" + parse.IntegerToString(ac.getAcDate() + 19110000, 8).substring(2, 4)
						+ parse.IntegerToString(gSeqCom.getSeqNo(ac.getAcDate(), 1, "L6", "RvNo", 999999, titaVo), 6);
		}

	}

	/* 更新 */
	private void procSyndLoan(int AcHCode) throws LogicException {
		// --------------- hold該筆銷帳檔 --------------------
		tAcReceivableId = new AcReceivableId();
		tAcReceivableId.setAcctCode(wkAcctCode);
		tAcReceivableId.setCustNo(ac.getCustNo());
		tAcReceivableId.setFacmNo(ac.getFacmNo());
		tAcReceivableId.setRvNo(wkRvNo);
		tAcReceivable = acReceivableService.holdById(tAcReceivableId, titaVo); // holdById
		if (tAcReceivable == null) {
			throw new LogicException(titaVo, "E6003", "AcReceivable Notfound " + tAcReceivableId);
		}
		if (AcHCode == 0) {
			tAcReceivable.setReceivableFlag(5);
		} else {
			tAcReceivable.setReceivableFlag(3);
		}
		try {
			acReceivableService.update(tAcReceivable, titaVo); // update
		} catch (DBException e) {
			e.printStackTrace();
			throw new LogicException(titaVo, "E6003", "AcReceivable update " + tAcReceivableId + e.getErrorMsg());
		}
	}

	/* 更新 */
	private void procUpdate(int AcHCode, int bizTbsdy) throws LogicException {

		// --------------- hold該筆銷帳檔 --------------------
		tAcReceivableId = new AcReceivableId();
		tAcReceivableId.setAcctCode(wkAcctCode);
		tAcReceivableId.setCustNo(ac.getCustNo());
		tAcReceivableId.setFacmNo(ac.getFacmNo());
		tAcReceivableId.setRvNo(wkRvNo);
		tAcReceivable = acReceivableService.holdById(tAcReceivableId, titaVo); // holdById
		if (tAcReceivable == null) {
			// 0-起帳
			if (wkRvFg == 0) {
				tAcReceivable = new AcReceivable();
				tAcReceivable.setAcReceivableId(tAcReceivableId);
				newAcReceivable(bizTbsdy);
				updAcReceivable(AcHCode, bizTbsdy);
				try {
					acReceivableService.insert(tAcReceivable, titaVo); // insert
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E6003",
							"AcReceivable insert " + tAcReceivableId + e.getErrorMsg());
				}
			} else
				throw new LogicException(titaVo, "E6003", "AcReceivable Notfound " + tAcReceivableId);
		} else {
			updAcReceivable(AcHCode, bizTbsdy);
			// 同交易序號訂正後為已銷帳則刪除，否則更新(短繳期金因同時會有起帳及銷帳因此除外)
			if (AcHCode == 1 && tAcReceivable.getClsFlag() == 1 && tAcReceivable.getReceivableFlag() != 4
					&& tAcReceivable.getTitaTlrNo().equals(this.titaVo.getOrgTlr())
					&& tAcReceivable.getTitaTxtNo() == parse.stringToInteger(this.titaVo.getOrgTno())) {
				try {
					acReceivableService.delete(tAcReceivable, titaVo); // update
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E6003",
							"AcReceivable delete " + tAcReceivableId + e.getErrorMsg());
				}
			} else {
				if (wkOpenAcDate > 0 && wkRvFg == 0) {
					tAcReceivable.setRvAmt(tAcReceivable.getRvBal());
					tAcReceivable.setOpenAcDate(wkOpenAcDate);
					tAcReceivable.setOpenTxCd(ac.getTitaTxCd());
					tAcReceivable.setOpenKinBr(ac.getTitaKinbr());
					tAcReceivable.setOpenTlrNo(ac.getTitaTlrNo());
					tAcReceivable.setOpenTxtNo(ac.getTitaTxtNo());
				}
				tAcReceivable.setTitaTxCd(ac.getTitaTxCd());
				tAcReceivable.setTitaKinBr(ac.getTitaKinbr());
				tAcReceivable.setTitaTlrNo(ac.getTitaTlrNo());
				tAcReceivable.setTitaTxtNo(ac.getTitaTxtNo());
				try {
					acReceivableService.update(tAcReceivable, titaVo); // update
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E6003",
							"AcReceivable update " + tAcReceivableId + e.getErrorMsg());
				}
			}
		}
	}

	private void updAcReceivable(int AcHCode, int bizTbsdy) throws LogicException {

		// 帳冊別、科子細目
		if (tAcReceivable.getAcSubBookCode().trim().isEmpty()) {
			tAcReceivable.setAcBookCode(ac.getAcBookCode());
			tAcReceivable.setAcSubBookCode(ac.getAcSubBookCode());
		}
		if (tAcReceivable.getAcNoCode().trim().isEmpty()) {
			tAcReceivable.setAcNoCode(ac.getAcNoCode());
			tAcReceivable.setAcSubCode(ac.getAcSubCode());
			tAcReceivable.setAcDtlCode(ac.getAcDtlCode());
		}
		// jsonFields 欄
		settingjsonfields(ac.getJsonFields(), titaVo);

//                      最後作帳日   會計日餘額    最後交易日   未銷餘額    系統營業日    交易日          銷帳金額    新最後作帳日       新會計日餘額    新最後交易日   新未銷餘額
//                      LastAcDate    AcBal        LastTxDate   RvBal       MgDate.Tbsdy  Txcom.Tbsdy     wkTxAmt     LastAcDate         AcBal           LastTxDate     RvBal 
//                       3/16         300          3/17          250        3/18          3/19(次日)      -100         
//1.過次日                                                                                                                 3/17               250
//                                                                                                                                                    
//2.最後交易日                                                                                                                                               3/19
//                                                                                                                                                                                                      
//3.累加未銷餘額、會計日餘額                                                                                                                  250                           150   
//       

//1.過次日             
// 最後作帳日小於系統營業日，則會計日餘額為未銷餘額，最後作帳日為系統營業日 */
		if (tAcReceivable.getLastAcDate() < bizTbsdy) {
			tAcReceivable.setAcBal(tAcReceivable.getRvBal());
			tAcReceivable.setLastAcDate(bizTbsdy);
		}
//
//2.最後交易日
		tAcReceivable.setLastTxDate(ac.getAcDate());
//   
//3.累加未銷餘額、會計日餘額       
		tAcReceivable.setRvBal(wkTxAmt.add(tAcReceivable.getRvBal()));
		if (ac.getAcDate() == bizTbsdy)
			tAcReceivable.setAcBal(wkTxAmt.add(tAcReceivable.getAcBal())); // 次日交易不累加會計日餘額
		else
			this.info("AcReceivable procUpdate :" + ", bizTbsdy=" + bizTbsdy + ", AcBal=" + tAcReceivable.getAcBal());

//4.設定銷帳記號	 銷帳記號 0.未銷、1.已銷
		if (tAcReceivable.getRvBal().compareTo(BigDecimal.ZERO) == 0) {
			tAcReceivable.setClsFlag(1);
		} else {
			tAcReceivable.setClsFlag(0);
		}

//5.檢查銷帳金額		
		if (tAcReceivable.getRvBal().compareTo(BigDecimal.ZERO) < 0
				|| tAcReceivable.getAcBal().compareTo(BigDecimal.ZERO) < 0) {
			this.info("銷帳金額超過原入帳金額 :" + ", bizTbsdy=" + bizTbsdy + ", RvBal=" + tAcReceivable.getRvBal());
			String str = "科目=" + tAcReceivable.getAcctCode() + ", 戶號=" + tAcReceivable.getCustNo() + "-"
					+ parse.IntegerToString(tAcReceivable.getFacmNo(), 3) + " " + tAcReceivable.getRvNo();
			if (titaVo.isHcodeErase()) {
				throw new LogicException(titaVo, "E6003", "銷帳金額超過原入帳金額，請依序訂正 " + str);
			} else {
				throw new LogicException(titaVo, "E6003", "銷帳金額超過原入帳金額 " + str);
			}
		}

		this.info("AcReceivable update End :" + tAcReceivable.toString());
	}

	/* 設定 jsonFields 欄 */
	private void settingjsonfields(String JsonFields, TitaVo titaVo) throws LogicException {
		// 合併銷帳檔及會計分錄的jsonFields
		TempVo rTempVo = new TempVo();
		tTempVo = rTempVo.getVo(tAcReceivable.getJsonFields());
		rTempVo = tTempVo.getVo(JsonFields);
		tTempVo.putAll(rTempVo);
		tAcReceivable.setJsonFields(tTempVo.getJsonString());

	}

	private void newAcReceivable(int bizTbsdy) throws LogicException {
//       0. AcctCode 業務科目代號          VARCHAR2  3                AcDetail                
//	     1. CustNo     戶號                DECIMAL 7                  AcDetail
//	     2. FacmNo     額度編號            DECIMAL 3                  AcDetail
//	     3. BormNo     撥款序號            DECIMAL 3                  AcDetail
//	     4. RvNo       銷帳編號            VARCHAR2                   wkRvNo
//	        1.系統自動編(會計銷帳科目)
//	        2.支票帳號-支票號碼(暫收款－支票)
//	        3.原保險單號碼(暫付火險保費、催收款項－火險費用)
//	        4.FacmNo+額度編號(暫收款－借新還舊)
//	     5. AcNoCode        科目代號       VARCHAR2  8                AcDetail 
//	     6. AcSubCode       子目代號       VARCHAR2  5                AcDetail
//	     7. AcDtlCode       細目代號       VARCHAR2  2                AcDetail
//	     8. BranchNo        單位別         VARCHAR2  4                AcDetail
//	     9. CurrencyCode    幣別           VARCHAR2  3                AcDetail
//	    10. ClsFlag         銷帳記號       DECIMAL 1                
//	        0.未銷、1.已銷                                            IIF(RvBal=0,1,0)
//	    11. AcctFlag  業務科目記號         DECIMAL 1                  AcDetail
//	        0: 非業務科目
//	        1: 資負明細科目
//	    12. ReceivableFlag  銷帳科目記號   DECIMAL 1                  AcDetail
//	        1－會計銷帳科目
//	        2－業務銷帳科目
//	    13. RvAmt       起帳總額           DECIMAL 16  2              計算       
//	    14. RvBal       未銷餘額           DECIMAL 16  2              計算
//	    15. AcBal       會計日餘額         DECIMAL 16  2              計算
//	    16. SlipNote    傳票摘要           NVARCHAR2 80              AcDetail
//	    17. AcBookCode  帳冊別             VARCHAR2  3                AcDetail
//	    18. OpenAcDate  起帳日期           Decimald  8               AcDetail
//	    19. LastAcDate  最後作帳日         Decimald  8                計算
//	    20. LastTxDate  最後交易日         Decimald  8                計算
//	    21. TitaTxCd    交易代號           VARCHAR2  5              AcDetail
//	    22. TitaTlrNo   經辦               VARCHAR2  6               AcDetail
//	    23. TitaTxtNo   交易序號           DECIMAL 8                AcDetail

//	    25. CreateEmpNo 建檔人員           VARCHAR2  6                AcDetail
//	    26. CreateDate  建檔日期           DATE                       SYSTEM 
//	    27. LastUpdateEmpNo 最後維護人員   VARCHAR2  6              AcDetail
//	    28  LastUpdate  最後維護日期       DATE                       SYSTEM 

		tAcReceivable.setBranchNo(ac.getBranchNo());
		tAcReceivable.setCurrencyCode(ac.getCurrencyCode());
		tAcReceivable.setAcctFlag(ac.getAcctFlag());
		tAcReceivable.setReceivableFlag(ac.getReceivableFlag());
		tAcReceivable.setRvAmt(ac.getTxAmt());
		tAcReceivable.setRvBal(BigDecimal.ZERO);
		tAcReceivable.setAcBal(BigDecimal.ZERO);
		tAcReceivable.setSlipNote(ac.getSlipNote());
		tAcReceivable.setAcBookCode(ac.getAcBookCode());
		tAcReceivable.setAcSubBookCode(ac.getAcSubBookCode());
		if (wkOpenAcDate > 0) {
			tAcReceivable.setOpenAcDate(wkOpenAcDate);
		} else {
			tAcReceivable.setOpenAcDate(ac.getAcDate());
		}
		tAcReceivable.setTitaTxCd(ac.getTitaTxCd());
		tAcReceivable.setTitaKinBr(ac.getTitaKinbr());
		tAcReceivable.setTitaTlrNo(ac.getTitaTlrNo());
		tAcReceivable.setTitaTxtNo(ac.getTitaTxtNo());
		tAcReceivable.setOpenTxCd(ac.getTitaTxCd());
		tAcReceivable.setOpenKinBr(ac.getTitaKinbr());
		tAcReceivable.setOpenTlrNo(ac.getTitaTlrNo());
		tAcReceivable.setOpenTxtNo(ac.getTitaTxtNo());
	}

	/* 更新火險單續保檔 AcDate */
	private void updInsuRenew(int AcHCode, int bizTbsdy, AcDetail ac) throws LogicException {
// 客戶繳火險費
//  借: 收付欄  
//     貸: TMI 未收火險費  ReceivableFlag = 3
//         F09 暫付火險費   
//		   F25   催收款項－火險費用

		InsuRenew tInsuRenew = new InsuRenew();
		tInsuRenew = insuRenewService.prevInsuNoFirst(ac.getCustNo(), ac.getFacmNo(), wkRvNo, titaVo);

		if (tInsuRenew == null)
			throw new LogicException(titaVo, "E6003", "AcReceivableCom updInsuRenew notfound " + ac.getCustNo() + "-"
					+ ac.getFacmNo() + "," + ac.getRvNo());
		else {
			tInsuRenew = insuRenewService.holdById(tInsuRenew, titaVo);
			if (tInsuRenew.getTotInsuPrem().compareTo(ac.getTxAmt()) != 0) {
				throw new LogicException(titaVo, "E6003",
						"銷帳金額與總保費不符 " + ac.getTxAmt() + "/" + tInsuRenew.getTotInsuPrem());
			}
			if (AcHCode == 0) {
				tInsuRenew.setAcDate(bizTbsdy); // 1-已銷
				tInsuRenew.setTitaTlrNo(titaVo.getTlrNo());
				tInsuRenew.setTitaTxtNo(titaVo.getTxtNo());
			} else {
				tInsuRenew.setAcDate(0); // 0-未銷
				tInsuRenew.setTitaTlrNo("");
				tInsuRenew.setTitaTxtNo("");
			}
			try {
				insuRenewService.update(tInsuRenew, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003",
						"AcReceivableCom updInsuRenew " + tAcReceivableId + e.getErrorMsg());
			}
		}
	}

	/* 更新法務費檔 CloseDate */
	private void updForeclosureFee(int AcHCode, int bizTbsdy, AcDetail ac) throws LogicException {

		ForeclosureFee tForeclosureFee = new ForeclosureFee();
		tForeclosureFee = foreclosureFeeService.holdById(parse.stringToInteger(wkRvNo), titaVo);
		if (tForeclosureFee == null)
			throw new LogicException(titaVo, "E6003", " AcReceivableCom updForeclosureFee Notfound" + wkRvNo);
		else {
			if (tForeclosureFee.getFee().compareTo(ac.getTxAmt()) != 0) {
				throw new LogicException(titaVo, "E6003",
						"銷帳金額與法務費不符 " + ac.getTxAmt() + "/" + tForeclosureFee.getFee());
			}
			if (tAcReceivable.getClsFlag() == 1) {
				tForeclosureFee.setCloseDate(bizTbsdy); // 1-已銷
			} else {
				tForeclosureFee.setCloseDate(0); // 0-未銷
			}
			try {
				foreclosureFeeService.update(tForeclosureFee, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003",
						"AcReceivableCom updForeclosureFee " + tAcReceivableId + e.getErrorMsg());
			}
		}
	}
}
