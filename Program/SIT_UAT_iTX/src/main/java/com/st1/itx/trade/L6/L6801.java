package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.parse.Parse;

@Service("L6801")
@Scope("prototype")
/**
 * 放款戶帳冊別轉換
 * 
 * @author w.y.Lai
 * @version 1.0.0
 */
public class L6801 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6801.class);

	@Autowired
	public Parse parse;

	@Autowired
	public AcDetailCom acDetailCom;

	@Autowired
	public AcReceivableService acReceivableService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6801 ");
		this.totaVo.init(titaVo);
		int iCustNo = parse.stringToInteger(titaVo.getParam("TimCustNo"));
		String iOldAcSubBookCode = titaVo.get("OldAcSubBookCode");
		String iNewAcSubBookCode = titaVo.get("NewAcSubBookCode");

		AcReceivable tAcReceivable = new AcReceivable();
		tAcReceivable = acReceivableService.acrvFacmNoFirst(iCustNo, 1, 0);
		if (tAcReceivable == null) {
			throw new LogicException(titaVo, "E0010", "銷帳檔無該放款戶資料"); // E0010 功能選擇錯誤
		}
		if (iNewAcSubBookCode.equals(tAcReceivable.getAcSubBookCode())) {
 			throw new LogicException(titaVo, "E0010", "新區隔帳冊別相同" + tAcReceivable.toString()); // 功能選擇錯誤
		}

		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		Slice<AcReceivable> slAcReceivable = null;
		// 已銷資負明細科目
		slAcReceivable = acReceivableService.acrvFacmNoRange(1, iCustNo, 1, 000, 999, 0, Integer.MAX_VALUE, titaVo);
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {
				if (titaVo.isHcodeNormal()) {
					rv.setAcSubBookCode(iNewAcSubBookCode);
				} else {
					rv.setAcSubBookCode(iOldAcSubBookCode);
				}
			}
			try {
				acReceivableService.updateAll(lAcReceivable, titaVo); // update All
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003", "AcReceivable update " + lAcReceivable + e.getErrorMsg());
			}
		}

		// 未銷資負明細科目
		slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 1, 000, 999, 0, Integer.MAX_VALUE, titaVo);
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {
				if (titaVo.isHcodeNormal()) {
					rv.setAcSubBookCode(iNewAcSubBookCode);
				} else {
					rv.setAcSubBookCode(iOldAcSubBookCode);
				}
			}
			try {
				acReceivableService.updateAll(lAcReceivable, titaVo); // update All
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003", "AcReceivable update " + lAcReceivable + e.getErrorMsg());
			}
		}
		List<AcDetail> acDetailList = new ArrayList<AcDetail>();
		AcDetail acDetail = new AcDetail();
		// 未銷資負明細科目
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {
				if (titaVo.isHcodeNormal()) {
					// 借:新帳冊別
					acDetail = new AcDetail();
					acDetail.setAcSubBookCode(iNewAcSubBookCode);
					acDetail.setDbCr("D");
					acDetail.setAcctCode(rv.getAcctCode());
					acDetail.setTxAmt(rv.getRvBal());
					acDetail.setCustNo(rv.getCustNo());
					acDetail.setFacmNo(rv.getFacmNo());
					acDetail.setBormNo(parse.stringToInteger(rv.getRvNo()));
					acDetailList.add(acDetail);
					// 貸:原帳冊別
					acDetail = new AcDetail();
					acDetail.setAcSubBookCode(iOldAcSubBookCode);
					acDetail.setDbCr("C");
					acDetail.setAcctCode(rv.getAcctCode());
					acDetail.setTxAmt(rv.getRvBal());
					acDetail.setCustNo(rv.getCustNo());
					acDetail.setFacmNo(rv.getFacmNo());
					acDetail.setBormNo(parse.stringToInteger(rv.getRvNo()));
					acDetailList.add(acDetail);
				}
			}
		}

		/* 產生會計分錄 */
		if (this.txBuffer.getTxCom().isBookAcYes() && acDetailList.size() > 0) {
			this.txBuffer.addAllAcDetailList(acDetailList);
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}