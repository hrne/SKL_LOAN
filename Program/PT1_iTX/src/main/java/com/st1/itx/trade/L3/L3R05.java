package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.data.BaTxVo;
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

	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private BigDecimal oExcessive = BigDecimal.ZERO;
	private BigDecimal oExcessiveAll = BigDecimal.ZERO;

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
		// 是否額度可抵繳
		// 按指定額度：00-全費用類別
		// 1.iFacmNo >0 該額度為指定額度則只有該額度可抵繳,如該額度為非指定額度則全部非指定額度可抵繳
		// 2.iFacmNo =0 全部非指定額度可抵繳
		// 96 : 單一額度轉帳
		// 暫收可抵繳
		baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, iTempItemCode == 6 ? 96 : 0,
				BigDecimal.ZERO, titaVo);

		if (iTempReasonCode == 1 && iCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {

			wkTmpFacmNoX = baTxCom.getTmpFacmNoX();
			wkTempAmt = baTxCom.getExcessive();
			oExcessiveAll = baTxCom.getExcessive().add(baTxCom.getExcessiveOther());
		} else {

			if (this.baTxList != null) {
				// 重新計算作帳金額
				for (BaTxVo ba : this.baTxList) {
					ba.setAcctAmt(BigDecimal.ZERO);
				}
				for (BaTxVo ba : this.baTxList) {
					if (iTempReasonCode == 1 && ba.getAcctCode().equals("TLD")
							|| (iTempReasonCode == 2 && ba.getAcctCode().substring(0, 2).equals("T1"))
							|| (iTempReasonCode == 3 && ba.getAcctCode().substring(0, 2).equals("T2"))
							|| (iTempReasonCode == 4 && ba.getAcctCode().equals("TAM"))) {
						wkTempAmt = wkTempAmt.add(ba.getUnPaidAmt());
					}
				}
			}
			oExcessiveAll = wkTempAmt;
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

		this.totaVo.putParam("L3r05ExcessiveAll", oExcessiveAll);

		this.addList(this.totaVo);
		return this.sendList();
	}
}