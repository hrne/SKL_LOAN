package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.parse.Parse;

/**
 * L3R05 尋找暫收款餘額
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R05")
@Scope("prototype")
public class L3R05 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R05 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iTxCode = titaVo.getParam("RimTxCode");
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("RimTempReasonCode"));
		int iTempItemCode = this.parse.stringToInteger(titaVo.get("RimTempItemCode"));

		// work area
		BigDecimal wkTempAmt = new BigDecimal(0);
		String wkTmpFacmNoX = "";
		// 暫收可抵繳
		if (iTempReasonCode == 1 && iCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {

// 是否額度可抵繳
// 按指定額度：00-全費用類別
//  1.iFacmNo >0 該額度為指定額度則只有該額度可抵繳,如該額度為非指定額度則全部非指定額度可抵繳
//  2.iFacmNo =0 全部非指定額度可抵繳
//	96 : 單一額度轉帳
			baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, iTempItemCode == 6 ? 96 : 0, BigDecimal.ZERO,
					titaVo);
			wkTempAmt = baTxCom.getExcessive();
			wkTmpFacmNoX = baTxCom.getTmpFacmNoX();
		} else {
			// 查詢會計銷帳檔
			Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, 0, 999, 0,
					Integer.MAX_VALUE, titaVo);
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			if (lAcReceivable != null && lAcReceivable.size() > 0) {
				for (AcReceivable tAcReceivable : lAcReceivable) {

					switch (iTempReasonCode) {
					case 1: // 放款暫收款
						// 放款專戶
						if (tAcReceivable.getAcctCode().equals("TLD")) {
							wkTempAmt = tAcReceivable.getRvBal().add(wkTempAmt);
						}
						break;
					case 2: // 債協暫收款
						if (tAcReceivable.getAcctCode().substring(0, 2).equals("T1")) {
							wkTempAmt = tAcReceivable.getRvBal().add(wkTempAmt);
						}
						break;
					case 3: // 債協退還款
						if (tAcReceivable.getAcctCode().substring(0, 2).equals("T2")) {
							wkTempAmt = tAcReceivable.getRvBal().add(wkTempAmt);
						}
						break;
					case 4: // AML暫收款
						if (tAcReceivable.getAcctCode().equals("TAM")) {
							wkTempAmt = tAcReceivable.getRvBal().add(wkTempAmt);
						}
						break;
					case 5: // 聯貸費攤提暫收款
						if (tAcReceivable.getAcctCode().equals("TSL")) {
							wkTempAmt = tAcReceivable.getRvBal().add(wkTempAmt);
						}
						break;
					}

				}
			}
		}

		if ("L3220".equals(iTxCode) && (iTempItemCode == 4 || iTempItemCode == 5 || iTempItemCode == 11)
				&& wkTempAmt.compareTo(BigDecimal.ZERO) == 0) {
			throw new LogicException(titaVo, "E3093", " 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查無暫收款(暫收款金額為零)
		}
		if ("L3230".equals(iTxCode) && wkTempAmt.compareTo(BigDecimal.ZERO) == 0) {
			throw new LogicException(titaVo, "E3093", " 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查無暫收款(暫收款金額為零)
		}

		this.totaVo.putParam("L3r05TempAmt", wkTempAmt);
		this.totaVo.putParam("L3r05TmpFacmNoX", wkTmpFacmNoX);

		this.addList(this.totaVo);
		return this.sendList();
	}
}