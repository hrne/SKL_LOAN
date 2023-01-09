package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R34")
@Scope("prototype")

public class L4R34 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public InsuRenewService insuRenewService;


	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R34 ");
		this.totaVo.init(titaVo);

		int iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;


// 與新光產險對帳後，列應付費用
// 一、正常繳款(放款系統出帳)
//  　借:暫收及待結轉帳項-火險保費
//　　   貸:應付費用-待匯
// 二、墊付火險費(核心系統出帳)
// 　 借 :暫付及待結轉帳項-火險保費
//  　　貸: 應付費用-待匯

		// 已繳金額
		BigDecimal txAmt1 = BigDecimal.ZERO;
		// 未繳金額
		BigDecimal txAmt2 = BigDecimal.ZERO;

		// 已繳
		Slice<InsuRenew> sInsuRenew = insuRenewService.findL4604A(iInsuEndMonth, 2, 1, 99999999, this.index,
				this.limit);
		if (sInsuRenew != null) {
			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {
				if (tInsuRenew.getStatusCode() == 0) {
					txAmt1 = txAmt1.add(tInsuRenew.getTotInsuPrem());
				}
			}
		}
		// 未繳
		sInsuRenew = insuRenewService.findL4604A(iInsuEndMonth, 2, 0, 0, this.index, this.limit);
		if (sInsuRenew != null) {
			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {
				if (tInsuRenew.getStatusCode() == 0) {
					txAmt2 = txAmt2.add(tInsuRenew.getTotInsuPrem());
				}
			}
		}

		totaVo.putParam("L4r34TxAmt1", txAmt1);
		totaVo.putParam("L4r34TxAmt2", txAmt2);


		this.addList(this.totaVo);
		return this.sendList();
	}
}