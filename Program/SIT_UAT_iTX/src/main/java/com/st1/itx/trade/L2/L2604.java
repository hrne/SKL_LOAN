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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2604 ");
		this.totaVo.init(titaVo);
		acReceivableCom.setTxBuffer(txBuffer);

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		String iSyndFeeCode = titaVo.getParam("SyndFeeCode");
		String iRmk = titaVo.getParam("Rmk");
		BigDecimal iSyndFee = parse.stringToBigDecimal(titaVo.getParam("TimSyndFee"));

		int iOldCustNo = parse.stringToInteger(titaVo.getParam("OldCustNo"));
		int iOldFacmNo = parse.stringToInteger(titaVo.getParam("OldFacmNo"));
		String iOldRvNo = titaVo.getParam("OldRvNo");
		String iOldAcctCode = titaVo.getParam("OldAcctCode");

		// wk
		String wkAcctCode = "";
		int wkSeq = 0;
		CdSyndFee tCdSyndFee = new CdSyndFee();
		Slice<AcReceivable> slAcReceivable = null;
		List<AcReceivable> lAcReceivableList = new ArrayList<AcReceivable>();
		AcReceivable tmpAcReceivable = new AcReceivable();

		// 檢查費用設定檔
		tCdSyndFee = sCdSyndFeeService.findById(iSyndFeeCode, titaVo);
		if (tCdSyndFee == null) {
			throw new LogicException(titaVo, "E2003", "費用參數檔"); // 查無資料
		}
		wkAcctCode = tCdSyndFee.getAcctCode();
		if (iFunCd == 1) {

			// 檢查業務科目戶號額度費用項目下是否有資料 有資料則流水號編號+1 若無資料流水號編號1
			slAcReceivable = sAcReceivableService.useL2062Eq(wkAcctCode, iCustNo, iFacmNo, iFacmNo, 0, 1, 0,
					Integer.MAX_VALUE, titaVo);
			if (slAcReceivable != null) {
				List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
				for (AcReceivable t : lAcReceivable) {
					this.info("AcReceivable RvNo = " + t.getRvNo());
					// 檢查SL 是否為聯貸費用
					if (!("".equals(t.getRvNo())) && t.getRvNo().length() >= 7
							&& "SL".equals(t.getRvNo().substring(0, 2))) {
						String tSyndFeeCode = "";
						tSyndFeeCode = t.getRvNo().substring(2, 4);
						this.info(" RvNo  tSyndFeeCode = " + tSyndFeeCode);
						// 檢查是否同費用項目
						// 流水號取最大
						if (tSyndFeeCode.equals(iSyndFeeCode)
								&& parse.stringToInteger(t.getRvNo().substring(5, 7)) > wkSeq) {
							wkSeq = parse.stringToInteger(t.getRvNo().substring(5, 7));
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
			this.info(" end wkSeq = " + wkSeq);
			AcReceivable acReceivable = new AcReceivable();
			List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
			acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			acReceivable.setAcctCode(wkAcctCode); // 業務科目
			// 法拍費用
			acReceivable.setRvAmt(iSyndFee); // 記帳金額
			// 戶號 7
			acReceivable.setCustNo(iCustNo);
			// 額度 3
			acReceivable.setFacmNo(iFacmNo);
			// 銷帳編號 = SL+費用代碼(2)+流水號(3)
			acReceivable.setRvNo("SL" + iSyndFeeCode + parse.IntegerToString(wkSeq, 3)); // 銷帳編號
			acReceivable.setSlipNote(iRmk); //備註
			acReceivable.setOpenAcDate(this.txBuffer.getTxCom().getTbsdy());

			acReceivableList.add(acReceivable);

			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳
		}
		// 修改
		else if (iFunCd == 2) {

			AcReceivable acReceivable = new AcReceivable();
			List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
			acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			acReceivable.setAcctCode(wkAcctCode); // 業務科目
			// 法拍費用
			acReceivable.setRvAmt(iSyndFee); // 記帳金額
			// 戶號 7
			acReceivable.setCustNo(iCustNo);
			// 額度 3
			acReceivable.setFacmNo(iFacmNo);
			// 銷帳編號 = SL+費用代碼(2)+流水號(3)
			acReceivable.setRvNo(iOldRvNo); // 銷帳編號
			acReceivable.setSlipNote(iRmk); //備註
			acReceivable.setOpenAcDate(this.txBuffer.getTxCom().getTbsdy());

			acReceivableList.add(acReceivable);

			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(3, acReceivableList, titaVo); // 0-起帳 1-銷帳 3變更

		}
		// 刪除
		else if (iFunCd == 4) {
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

		this.addList(this.totaVo);
		return this.sendList();
	}
}