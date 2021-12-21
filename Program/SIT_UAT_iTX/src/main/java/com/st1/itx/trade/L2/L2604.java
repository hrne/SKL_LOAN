package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L2604")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2604 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2604.class);

	// 銷帳處理
	@Autowired
	public AcReceivableCom acReceivableCom;
	@Autowired
	public CdSyndFeeService sCdSyndFeeService;
	@Autowired
	public AcReceivableService sAcReceivableService;

	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public SendRsp sendRsp;

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iFunCd;
	private int iCustNo;
	private int iFacmNo;
	private int iOldCustNo;
	private int iOldFacmNo;
	private int iAllocationFreq;
	private int iAllocationTimes;
	private String iSyndFeeCode;
	private String iRmk;
	private String iOldRvNo;
	private String iOldAcctCode;
	private String iSyndFeeYearMonth;
	private String iIsAllocation;
	private String iClsFg;
	private BigDecimal iSyndFee;
	private int wkSeq;
	private String wkAcctCode;

	private CdSyndFee tCdSyndFee;
	private Slice<AcReceivable> slAcReceivable;
	private List<AcReceivable> lAcReceivableList;
	private AcReceivable tmpAcReceivable;
	private AcReceivable tAcReceivable;
	private List<AcReceivable> acReceivableList;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2604 ");
		this.totaVo.init(titaVo);
		acReceivableCom.setTxBuffer(txBuffer);
		this.titaVo = titaVo;

		// tita
		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd")); // 功能
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));// 戶號
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));// 額度
		iOldCustNo = parse.stringToInteger(titaVo.getParam("OldCustNo")); // 戶號 修改刪除使用
		iOldFacmNo = parse.stringToInteger(titaVo.getParam("OldFacmNo"));// 額度 修改刪除使用
		iSyndFeeCode = titaVo.getParam("SyndFeeCode");// 費用代號
		iIsAllocation = titaVo.getParam("IsAllocation");// 是否攤提
		iRmk = titaVo.getParam("Rmk");// 備註
		iOldRvNo = titaVo.getParam("OldRvNo");// 銷帳編號 修改刪除使用
		iOldAcctCode = titaVo.getParam("OldAcctCode");// 業務科目 修改刪除使用
		iSyndFeeYearMonth = titaVo.getParam("SyndFeeYearMonth");// 費用年月
		iSyndFee = parse.stringToBigDecimal(titaVo.getParam("TimSyndFee"));// 金額
		iAllocationFreq = parse.stringToInteger(titaVo.getParam("AllocationFreq")); // 攤提週期
		iAllocationTimes = parse.stringToInteger(titaVo.getParam("AllocationTimes"));// 攤提次數

		// wk
		tCdSyndFee = new CdSyndFee();
		slAcReceivable = null;
		lAcReceivableList = new ArrayList<AcReceivable>();
		tmpAcReceivable = new AcReceivable();
		acReceivableList = new ArrayList<AcReceivable>();

		// 檢查費用設定檔
		tCdSyndFee = sCdSyndFeeService.findById(iSyndFeeCode, titaVo);
		if (tCdSyndFee == null) {
			throw new LogicException(titaVo, "E2003", "費用參數檔"); // 查無資料
		}
		wkAcctCode = tCdSyndFee.getAcctCode();
		// 檢查業務科目戶號額度費用項目下是否有資料 有資料則流水號編號+1 若無資料流水號編號1
		slAcReceivable = sAcReceivableService.useL2062Eq(wkAcctCode, iCustNo, iFacmNo, iFacmNo, 0, 1, 0,
				Integer.MAX_VALUE, titaVo);
		if (slAcReceivable != null) {
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			for (AcReceivable t : lAcReceivable) {
				this.info("AcReceivable RvNo = " + t.getRvNo());
				// 檢查SL 是否為聯貸費用
				if (!("".equals(t.getRvNo())) && t.getRvNo().length() >= 9
						&& "SL".equals(t.getRvNo().substring(0, 2))) {
					String tSyndFeeCode = "";
					tSyndFeeCode = t.getRvNo().substring(3, 5);
					this.info(" RvNo  tSyndFeeCode = " + tSyndFeeCode);
					// 檢查是否同費用項目
					// 流水號取最大
					if (tSyndFeeCode.equals(iSyndFeeCode)
							&& parse.stringToInteger(t.getRvNo().substring(7, 9)) > wkSeq) {
						wkSeq = parse.stringToInteger(t.getRvNo().substring(7, 9));
						this.info("RvNo wkSeq = " + wkSeq);
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		}
		// 無資料流水編號1 有資料則編號+1
		if (wkSeq == 0) {
			wkSeq = 1;
		} else {
			wkSeq++;
		}
		if (iFunCd == 1) {

			// 設定攤提者
			if ("Y".equals(iIsAllocation)) {
				AllocationAcReceivableRoutine();
			}
			// 一般
			else {
				InsertAcReceivableRoutine();
			}

		}
		// 修改
		else if (iFunCd == 2) {

			// 設定攤提者
			if ("Y".equals(iIsAllocation)) {
				// 先刪除
				DelAcReceivableRoutine();

				// 再新增
				AllocationAcReceivableRoutine();
			}
			// 一般
			else {

				AcReceivable acReceivable = new AcReceivable();
				acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				acReceivable.setAcctCode(wkAcctCode); // 業務科目
				// 法拍費用
				acReceivable.setRvAmt(iSyndFee); // 記帳金額
				// 戶號 7
				acReceivable.setCustNo(iCustNo);
				// 額度 3
				acReceivable.setFacmNo(iFacmNo);
				// 銷帳編號 = SL+-+費用代碼(2)+-+流水號(3)+-+攤提年月(設定攤提需加攤提年月,一般不需加年月)
				acReceivable.setRvNo(iOldRvNo); // 銷帳編號
				acReceivable.setSlipNote(iRmk); // 備註
				acReceivable.setOpenAcDate(this.txBuffer.getTxCom().getTbsdy());

				acReceivableList.add(acReceivable);

				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(3, acReceivableList, titaVo); // 0-起帳 1-銷帳 3變更
			}

		}
		// 刪除
		else if (iFunCd == 4) {

			// 設定攤提者
			if ("Y".equals(iIsAllocation)) {
				// ReceivableFlag==3時整個刪除
				DelAcReceivableRoutine();
			}
			// 一般
			else {
				AcReceivable tAcReceivable = sAcReceivableService
						.holdById(new AcReceivableId(iOldAcctCode, iOldCustNo, iOldFacmNo, iOldRvNo), titaVo);
				if (tAcReceivable == null) {
					throw new LogicException(titaVo, "E2003", "此筆資料不存在銷帳檔"); // 查無資料
				}

				// 變更前
				AcReceivable beforeAcReceivable = (AcReceivable) dataLog.clone(tAcReceivable);

				// 刪除須刷主管卡
				if (titaVo.getEmpNos().trim().isEmpty()) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
				}

				lAcReceivableList.add(tAcReceivable);

				acReceivableCom.mnt(2, lAcReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 新增攤提 銷帳檔
	private void AllocationAcReceivableRoutine() throws LogicException {
		this.info("AllocationAcReceivableRoutine ...");

		for (int i = 1; i <= 10; i++) {
			if (parse.stringToInteger(titaVo.get("YearMonth" + i)) > 0) {
				if ("Y".equals(titaVo.getParam("CloseFg" + i))) {
					continue;
				}
				tAcReceivable = new AcReceivable();
				if (parse.stringToInteger(titaVo.getParam("ReceivableFg" + i)) == 5) {
					tAcReceivable.setReceivableFlag(5); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				} else {
					tAcReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				}

				tAcReceivable.setAcctCode(wkAcctCode); // 業務科目
				// 法拍費用
				tAcReceivable.setRvAmt(parse.stringToBigDecimal(titaVo.getParam("AllocationAmt" + i))); // 記帳金額
				// 戶號 7
				tAcReceivable.setCustNo(iCustNo);
				// 額度 3
				tAcReceivable.setFacmNo(iFacmNo);
				// 銷帳編號 = SL+費用代碼(2)+流水號(3)
				if ("".equals(iOldRvNo)) {

					tAcReceivable.setRvNo("SL" + "-" + iSyndFeeCode + "-" + parse.IntegerToString(wkSeq, 3) + "-"
							+ titaVo.getParam("YearMonth" + i)); // 銷帳編號
				} else {
					tAcReceivable.setRvNo(iOldRvNo + "-" + titaVo.getParam("YearMonth" + i));// 銷帳編號
				}
				tAcReceivable.setSlipNote(iRmk); // 備註
				tAcReceivable.setOpenAcDate(this.txBuffer.getTxCom().getTbsdy());

				acReceivableList.add(tAcReceivable);
			} else {
				break;
			}
		}

		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳

	}

	// 新增銷帳檔 一般
	private void InsertAcReceivableRoutine() throws LogicException {
		this.info("insertAcReceivableRoutine ...");

		this.info(" end wkSeq = " + wkSeq);
		tAcReceivable = new AcReceivable();
		tAcReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
		tAcReceivable.setAcctCode(wkAcctCode); // 業務科目
		// 法拍費用
		tAcReceivable.setRvAmt(iSyndFee); // 記帳金額
		// 戶號 7
		tAcReceivable.setCustNo(iCustNo);
		// 額度 3
		tAcReceivable.setFacmNo(iFacmNo);
		// 銷帳編號 = SL+費用代碼(2)+流水號(3)
		tAcReceivable.setRvNo("SL" + "-" + iSyndFeeCode + "-" + parse.IntegerToString(wkSeq, 3)); // 銷帳編號
		tAcReceivable.setSlipNote(iRmk); // 備註
		tAcReceivable.setOpenAcDate(this.txBuffer.getTxCom().getTbsdy());

		acReceivableList.add(tAcReceivable);

		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳

	}

	// 刪除銷帳檔 攤提
	private void DelAcReceivableRoutine() throws LogicException {
		this.info("DelAcReceivableRoutine ...");
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		slAcReceivable = sAcReceivableService.useL2r58Eq(iCustNo, iFacmNo, 0, 9, iOldRvNo + "%", 0, Integer.MAX_VALUE,
				titaVo);

		if (slAcReceivable != null) {
			lAcReceivable = slAcReceivable == null ? null : new ArrayList<>(slAcReceivable.getContent());
			this.info("lAcReceivable = " + lAcReceivable.size());
			for (AcReceivable t : new ArrayList<>(lAcReceivable)) {
				this.info("t = " + t);
				if (iFunCd == 4 && t.getReceivableFlag() != 3) {
					throw new LogicException(titaVo, "E0010", "此筆資料不可刪除"); // 查無資料
				}
				this.info("iFunCd ClsFlag = " + iFunCd + "  " + t.getClsFlag());
				if (iFunCd == 2 && t.getClsFlag() == 1) {

					lAcReceivable.remove(t);
				}
			}
		} else {
			throw new LogicException(titaVo, "E2003", "此筆資料不存在銷帳檔"); // 查無資料
		}
		// 刪除須刷主管卡
		if (iFunCd == 4 && titaVo.getEmpNos().trim().isEmpty()) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
		}

		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(2, lAcReceivable, titaVo); // 0-起帳 1-銷帳 2-起帳刪除

	}
}