package com.st1.itx.trade.L3;

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
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
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
	private static final Logger logger = LoggerFactory.getLogger(L3R05.class);

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L3R05 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iTxCode = titaVo.getParam("RimTxCode");
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("RimTempReasonCode"));
		int iTempItemCode = this.parse.stringToInteger(titaVo.get("RimTempItemCode"));

		// work area
		BigDecimal wkTempAmt = new BigDecimal(0);

//		放款部專戶 
		if (iCustNo == this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
			wkTempAmt = new BigDecimal("99999999999999");
		} else {

			// 查詢會計銷帳檔
			Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, 0, 999, 0,
					Integer.MAX_VALUE, titaVo);
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			if (lAcReceivable != null && lAcReceivable.size() > 0) {
				for (AcReceivable tAcReceivable : lAcReceivable) {
					if ((iTempItemCode == 06)) {
						if (iFacmNo != tAcReceivable.getFacmNo()) {
							continue;
						}
					} else {
						if (iFacmNo > 0 && iFacmNo != tAcReceivable.getFacmNo()) {
							continue;
						}
					}
					switch (iTempReasonCode) {
					case 1: // 放款暫收款
						if (tAcReceivable.getAcctCode().equals("TAV")) {
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

		if (iTxCode.equals("L3230") && wkTempAmt.compareTo(BigDecimal.ZERO) == 0) {
			throw new LogicException(titaVo, "E3093", " 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查無暫收款(暫收款金額為零)
		}

		this.totaVo.putParam("L3r05TempAmt", wkTempAmt);

		this.addList(this.totaVo);
		return this.sendList();
	}
}