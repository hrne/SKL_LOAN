package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcTxFormCom;
import com.st1.itx.util.parse.Parse;

@Service("L41XX")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L41XX extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AcPaymentCom acPaymentCom;

	@Autowired
	public AcTxFormCom acTxFormCom;

	@Autowired
	public AcDetailCom acDetailCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L41XX ");
		this.totaVo.init(titaVo);

		this.info(" BookAc : " + this.txBuffer.getTxCom().getBookAc());

		this.info("1.AcctCode : " + titaVo.getParam("RpAcctCode1"));

		if (this.txBuffer.getTxCom().isBookAcYes()) {
			List<AcDetail> acDetailList = new ArrayList<AcDetail>();
			AcDetail tacDetail = new AcDetail();
			if (titaVo.getCrdb().equals("1")) {
				tacDetail.setDbCr("D");
				tacDetail.setAcctCode("310");
				tacDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getTxAmt()));
				tacDetail.setCustNo(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
				tacDetail.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
				tacDetail.setBormNo(parse.stringToInteger(titaVo.getMrKey().substring(12, 15)));

				acDetailList.add(tacDetail);
				this.txBuffer.addAllAcDetailList(acDetailList);

				acPaymentCom.setTxBuffer(this.txBuffer);
				/* 貸：收付欄 */
				acPaymentCom.run(titaVo);
//				this.txBuffer.addAllAcDetailList(acPaymentCom.getTxBuffer().getAcDetailList());

			} else {

				acPaymentCom.setTxBuffer(this.txBuffer);

				/* 借：收付欄 */
				acPaymentCom.run(titaVo);
//				this.txBuffer.addAllAcDetailList(acPaymentCom.getTxBuffer().getAcDetailList());
				this.info("this.txBuffer ... size" + this.txBuffer.getAcDetailList().size());

				tacDetail.setDbCr("C");
				tacDetail.setAcctCode("310");
				tacDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getTxAmt()));
				tacDetail.setCustNo(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
				tacDetail.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
				tacDetail.setBormNo(parse.stringToInteger(titaVo.getMrKey().substring(12, 15)));

				acDetailList.add(tacDetail);
				this.txBuffer.addAllAcDetailList(acDetailList);

			}

			/* 產生會計分錄 */
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);

			this.info("2.AcctCode : " + titaVo.getParam("RpAcctCode1"));

		}

		/* 維護撥款匯款檔 */
		if (titaVo.isActfgEntry() && titaVo.getSecNo().equals("01")) {
			/* 經辦登帳 */
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			this.info("this.getRelTno 1 :" + this.txBuffer.getTxCom().getRelTlr());
			this.info("this.getRelTno 2 :" + this.getTxBuffer().getTxCom().getRelTlr());

			acPaymentCom.remit(titaVo);
		}
		this.info("L41XX-A AcDetailList : " + acPaymentCom.getTxBuffer().getAcDetailList());

		this.addList(this.totaVo);
		return this.sendList();
	}
}