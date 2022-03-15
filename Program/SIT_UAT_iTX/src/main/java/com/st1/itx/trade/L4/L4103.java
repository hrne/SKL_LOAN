package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4103")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L4103 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4103.class);

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BankRemitService bankRemitService;
	@Autowired
	AcDetailService acDetailService;

	int acDate = 0;
	String batchNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4103 ");
		this.totaVo.init(titaVo);

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = titaVo.getParam("BatchNo");
		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

		Slice<AcDetail> slAcDetail = acDetailService.acdtlTitaBatchNo(titaVo.getAcbrNo(), titaVo.getCurName(), acDate,
				batchNo, 0, Integer.MAX_VALUE, titaVo);
		lAcDetail = slAcDetail == null ? null : new ArrayList<AcDetail>(slAcDetail.getContent());
		


		if (lAcDetail != null && lAcDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		// 執行交易
		titaVo.setBatchNo(batchNo);
		this.info("batchNo = " + batchNo);
		this.info("titaVo = " + titaVo.toString());
		MySpring.newTask("L4103Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}