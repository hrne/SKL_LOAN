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
import com.st1.itx.util.parse.Parse;

@Service("L3R16")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3R16 extends TradeBuffer {

	@Autowired
	Parse parse;
	@Autowired
	public AcReceivableService acReceivableService;

	private Slice<AcReceivable> slAcReceivable;
	private List<AcReceivable> lAcReceivableList;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R16 ");
		this.totaVo.init(titaVo);

		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		BigDecimal iAcctFee = parse.stringToBigDecimal(titaVo.getParam("RimAcctFee"));
		String wkAcctCode = titaVo.getParam("RimAcctCode");

		String messageTxt = "(新建未收費用)";
		// 檢查業務科目戶號額度 金額相同者
		// 手續費帳管費:(戶號額度金額相等)1.已建企金費2.已入帳 3.新建未收費用
		slAcReceivable = acReceivableService.useL2062Eq(wkAcctCode, iCustNo, iFacmNo, iFacmNo, 0, 1, 0,
				Integer.MAX_VALUE, titaVo);
		if (slAcReceivable != null) {
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			for (AcReceivable t : lAcReceivable) {
				// 檢查金額是否相符
				if (t.getRvAmt().compareTo(iAcctFee) == 0) {
					// 相符金額是否已銷
					if (t.getClsFlag() == 0) {
						messageTxt = "(已建企金費)";
					} else {
						messageTxt = "(已入帳)";
					}
					break;
				}
			}
		}

		this.totaVo.putParam("L3r16MessageTxt", messageTxt); // 訊息

		this.addList(this.totaVo);
		return this.sendList();
	}
}