package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=X,4 CurrencyCode=X,3 AcDate=9,7 SlipNote=X,160
 * #loop{times:6,i:1} DbAcNoCode=X,8 DbAcSubCode=X,5 DbAcDtlCode=X,2 DbRvNo=X,30
 * DbTxAmt=9,14.2 CrAcNoCode=X,8 CrAcSubCode=X,5 CrAcDtlCode=X,2 CrRvNo=X,30
 * CrTxAmt=9,14.2 #end END=X,1
 */

@Service("L6201")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6201 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6201.class);

	/* DB服務注入 */
	@Autowired
	public AcDetailCom acDetailCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6201 ");
		this.totaVo.init(titaVo);

		BigDecimal dbTxAmt;
		BigDecimal crTxAmt;
		this.info("L6201 BookAcYes : " + this.txBuffer.getTxCom().getBookAc());

		dbTxAmt = this.parse.stringToBigDecimal(titaVo.getParam("DbTxAmt1"));
		crTxAmt = this.parse.stringToBigDecimal(titaVo.getParam("CrTxAmt1"));
		if (dbTxAmt.compareTo(BigDecimal.ZERO) == 0 || crTxAmt.compareTo(BigDecimal.ZERO) == 0) {
			throw new LogicException(titaVo, "E6008", titaVo.getParam("DbTxAmt1") + "-" + titaVo.getParam("CrTxAmt1")); // 金額錯誤
		}

		/* 帳務 (借方入帳) */
		if (this.txBuffer.getTxCom().isBookAcYes()) { // 經辦登帳非訂正交易

			AcDetail acDetail;
			List<AcDetail> acDetailList = new ArrayList<AcDetail>();

			/* 借 */
			for (int i = 1; i <= 6; i++) {

				dbTxAmt = this.parse.stringToBigDecimal(titaVo.getParam("DbTxAmt" + i));
				this.info("L6201 DbAcNoCode : " + titaVo.getParam("DbAcNoCode" + i) + "-" + dbTxAmt);

				// if (titaVo.getParam("DbAcNoCode" + i).trim().isEmpty() ) {
				if (dbTxAmt.compareTo(BigDecimal.ZERO) == 0) {
					break;
				}

				/* 借 */
				acDetail = new AcDetail();
				acDetail.setDbCr("D"); // 借貸別
				acDetail.setAcctCode(titaVo.get("DbAcctCode" + i)); // 業務科目
				acDetail.setAcNoCode(titaVo.get("DbAcNoCode" + i)); // 科目代號
				acDetail.setAcSubCode(titaVo.get("DbAcSubCode" + i)); // 子目代號
				acDetail.setAcDtlCode(titaVo.get("DbAcDtlCode" + i)); // 細目代號
				acDetail.setTxAmt(this.parse.stringToBigDecimal(titaVo.getParam("DbTxAmt" + i))); // 記帳金額
				acDetail.setRvNo(titaVo.getParam("DbRvNo" + i)); // 銷帳編號
				acDetail.setSlipNote(titaVo.getParam("SlipNote")); // 傳票摘要
				acDetail.setAcBookCode(titaVo.get("AcBookCode")); // 帳冊別
				acDetail.setAcSubBookCode(titaVo.get("AcSubBookCode")); // 區隔帳冊
				acDetail.setAcBookFlag(3); // 帳冊別記號 (3: 指定帳冊)

				acDetailList.add(acDetail);

			}

			/* 貸 */
			for (int i = 1; i <= 6; i++) {

				crTxAmt = this.parse.stringToBigDecimal(titaVo.getParam("CrTxAmt" + i));
				this.info("L6201 CrAcNoCode : " + titaVo.getParam("CrAcNoCode" + i) + "-" + crTxAmt);

				// if (titaVo.getParam("CrAcNoCode" + i).trim().isEmpty()) {
				if (crTxAmt.compareTo(BigDecimal.ZERO) == 0) {
					break;
				}
				/* 貸 */
				acDetail = new AcDetail();
				acDetail.setDbCr("C"); // 借貸別
				acDetail.setAcctCode(titaVo.get("CrAcctCode" + i)); // 業務科目
				acDetail.setAcNoCode(titaVo.get("CrAcNoCode" + i)); // 科目代號
				acDetail.setAcSubCode(titaVo.get("CrAcSubCode" + i)); // 子目代號
				acDetail.setAcDtlCode(titaVo.get("CrAcDtlCode" + i)); // 細目代號
				acDetail.setTxAmt(this.parse.stringToBigDecimal(titaVo.getParam("CrTxAmt" + i))); // 記帳金額
				acDetail.setRvNo(titaVo.getParam("CrRvNo" + i)); // 銷帳編號
				acDetail.setSlipNote(titaVo.getParam("SlipNote")); // 傳票摘要
				acDetail.setAcBookCode(titaVo.get("AcBookCode")); // 帳冊別
				acDetail.setAcSubBookCode(titaVo.get("AcSubBookCode")); // 區隔帳冊
				acDetail.setAcBookFlag(3); // 帳冊別記號 (3: 指定帳冊)

				acDetailList.add(acDetail);

			}

			this.info("acDetailCom start ...");
			this.txBuffer.addAllAcDetailList(acDetailList);
			/* 產生會計分錄 */
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}