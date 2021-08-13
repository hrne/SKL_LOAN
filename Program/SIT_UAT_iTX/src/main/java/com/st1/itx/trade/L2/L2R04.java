package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdAcctFee;
import com.st1.itx.db.service.FacProdAcctFeeService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimTxCode=X,5
 * RimProdNo=X,5
 * RimLoanAmt=9,14.2
 */
/**
 * L2R04 尋找商品參數檔帳管費資料
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R04")
@Scope("prototype")
public class L2R04 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R04.class);

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdAcctFeeService facProdAcctFeeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R04 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimProdNo = titaVo.getParam("RimProdNo");
		BigDecimal iRimLoanAmt = this.parse.stringToBigDecimal(titaVo.getParam("RimLoanAmt"));

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L2R04"); // 交易代號不可為空白
		}

		// 查詢商品參數檔
		FacProd tFacProd = facProdService.findById(iRimProdNo, titaVo);
		/* 如有有找到資料 */
		if (tFacProd != null) {
			// 檢查商品參數檔

			if (!(tFacProd.getStartDate() <= parse.stringToInteger(titaVo.getCalDy()))) {
				throw new LogicException(titaVo, "E2053", "L2R04"); // 此商品尚未生效
			}
			if (tFacProd.getEndDate() > 0) {
				if (tFacProd.getEndDate() < this.txBuffer.getTxCom().getTbsdy()) {
					throw new LogicException(titaVo, "E2054", "L2R04" + iRimProdNo + tFacProd.getEndDate()); // 此商品已截止
				}
			}
			if (!tFacProd.getStatusCode().equals("0")) {
				throw new LogicException(titaVo, "E2054", "L2R04"); // 此商品已停用
			}
		} else {
			throw new LogicException(titaVo, "E0001", "L2R04 商品參數檔"); // 查無資料
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		// 查詢帳管費檔
		this.totaVo.putParam("OAcctFee", 0);
		Slice<FacProdAcctFee> lFacProdAcctFee = facProdAcctFeeService.acctFeeProdNoEq(iRimProdNo, new BigDecimal(0.00),
				new BigDecimal(99999999999999.00), this.index, this.limit, titaVo);
		if (!(lFacProdAcctFee == null || lFacProdAcctFee.isEmpty())) {
			for (FacProdAcctFee tFacProdAcctFee : lFacProdAcctFee.getContent()) {
				if (tFacProdAcctFee.getLoanLow().compareTo(iRimLoanAmt) != 1
						&& tFacProdAcctFee.getLoanHigh().compareTo(iRimLoanAmt) != -1) {
					this.totaVo.putParam("OAcctFee", tFacProdAcctFee.getAcctFee());
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}