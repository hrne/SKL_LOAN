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

/**
 * Tita<br>
 * RpFg=9,1<br>
 * RpCode1=9,2<br>
 * RpAmt1=9,14.2<br>
 * RpAcctCode1=9,3<br>
 * RpAcCode1=9,15<br>
 * RpCustNo1=9,7<br>
 * RpFacmNo1=9,3<br>
 * RpBormNo1=9,3<br>
 * RpRemitBank1=9,3<br>
 * RpRemitBranch1=9,4<br>
 * RpRemitAcctNo1=9,14<br>
 * RpCustName1=X,100<br>
 * RpRemark1=X,100<br>
 * RpRvno1=X,30<br>
 * RpDscpt1=X,4<br>
 * RpNote1=X,80<br>
 * RpRemitNm1=X,100<br>
 * RpType1=9,2<br>
 * RpCode2=9,2<br>
 * RpAmt2=9,14.2<br>
 * RpAcctCode2=9,3<br>
 * RpAcCode2=9,15<br>
 * RpCustNo2=9,7<br>
 * RpFacmNo2=9,3<br>
 * RpBormNo2=9,3<br>
 * RpRemitBank2=9,3<br>
 * RpRemitBranch2=9,4<br>
 * RpRemitAcctNo2=9,14<br>
 * RpCustName2=X,100<br>
 * RpRemark2=X,100<br>
 * RpRvno2=X,30<br>
 * RpDscpt2=X,4<br>
 * RpNote2=X,80<br>
 * RpRemitNm2=X,100<br>
 * RpType2=9,2<br>
 * RpCode3=9,2<br>
 * RpAmt3=9,14.2<br>
 * RpAcctCode3=9,3<br>
 * RpAcCode3=9,15<br>
 * RpCustNo3=9,7<br>
 * RpFacmNo3=9,3<br>
 * RpBormNo3=9,3<br>
 * RpRemitBank3=9,3<br>
 * RpRemitBranch3=9,4<br>
 * RpRemitAcctNo3=9,14<br>
 * RpCustName3=X,100<br>
 * RpRemark3=X,100<br>
 * RpRvno3=X,30<br>
 * RpDscpt3=X,4<br>
 * RpNote3=X,80<br>
 * RpRemitNm3=X,100<br>
 * RpType3=9,2<br>
 * RpCode4=9,2<br>
 * RpAmt4=9,14.2<br>
 * RpAcctCode4=9,3<br>
 * RpAcCode4=9,15<br>
 * RpCustNo4=9,7<br>
 * RpFacmNo4=9,3<br>
 * RpBormNo4=9,3<br>
 * RpRemitBank4=9,3<br>
 * RpRemitBranch4=9,4<br>
 * RpRemitAcctNo4=9,14<br>
 * RpCustName4=X,100<br>
 * RpRemark4=X,100<br>
 * RpRvno4=X,30<br>
 * RpDscpt4=X,4<br>
 * RpNote4=X,80<br>
 * RpRemitNm4=X,100<br>
 * RpType4=9,2<br>
 * RpCode5=9,2<br>
 * RpAmt5=9,14.2<br>
 * RpAcctCode5=9,3<br>
 * RpAcCode5=9,15<br>
 * RpCustNo5=9,7<br>
 * RpFacmNo5=9,3<br>
 * RpBormNo5=9,3<br>
 * RpRemitBank5=9,3<br>
 * RpRemitBranch5=9,4<br>
 * RpRemitAcctNo5=9,14<br>
 * RpCustName5=X,100<br>
 * RpRemark5=X,100<br>
 * RpRvno5=X,30<br>
 * RpDscpt5=X,4<br>
 * RpNote5=X,80<br>
 * RpRemitNm5=X,100<br>
 * RpType5=9,2<br>
 * OverRpFg=9,1<br>
 * OverRpAmt=9,14.2<br>
 * OverRpFacmNo=9,3<br>
 */

@Service("L41XX")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L41XX extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L41XX.class);

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

//		ArrayList<TotaVo> result = acTxFormCom.formBuild(acPaymentCom.getTxBuffer().getAcDetailList(), titaVo);
//		acTxFormCom.setTxBuffer(this.getTxBuffer());

//		this.info("1.AcctCode : " + titaVo.getParam("RpAcctCode1"));

//		this.addAllList(acTxFormCom.run(titaVo));

		this.addList(this.totaVo);
		return this.sendList();
	}
}