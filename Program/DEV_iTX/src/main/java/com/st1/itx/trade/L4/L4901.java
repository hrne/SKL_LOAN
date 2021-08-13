package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * SearchFlag=9,1<br>
 * CustNo=9,7<br>
 * AcDate=9,7<br>
 * BatchNo=X,6<br>
 * DrawdownCode=9,1<br>
 * StatusCode=9,1<br>
 * END=X,1<br>
 */

@Service("L4901")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4901 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4901.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BankRemitService bankRemitService;

	@Autowired
	public CdBankService cdBankService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4901 ");
		this.totaVo.init(titaVo);

		int searchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String batchNo = titaVo.getParam("BatchNo");
		int drawdownCode = parse.stringToInteger(titaVo.getParam("DrawdownCode"));
		int statusCode = parse.stringToInteger(titaVo.getParam("StatusCode"));

		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<BankRemit> sBankRemit = null;

		if (searchFlag == 1) {
			sBankRemit = bankRemitService.findL4901A(custNo, this.index, this.limit);
		} else if (searchFlag == 2) {
			if("".equals(batchNo.trim())) {
				if (drawdownCode == 99 && statusCode == 9) {
					sBankRemit = bankRemitService.findL4901C(acDate, 0, 99, 0, 9, this.index, this.limit, titaVo);
				} else if (drawdownCode == 99) {
					sBankRemit = bankRemitService.findL4901C(acDate, 0, 99, statusCode, statusCode, this.index,
							this.limit, titaVo);
				} else if (statusCode == 9) {
					sBankRemit = bankRemitService.findL4901C(acDate, drawdownCode, drawdownCode, 0, 9, this.index,
							this.limit, titaVo);
				} else {
					sBankRemit = bankRemitService.findL4901C(acDate, drawdownCode, drawdownCode, statusCode,
							statusCode, this.index, this.limit, titaVo);
				}	
			} else {
				if (drawdownCode == 99 && statusCode == 9) {
					sBankRemit = bankRemitService.findL4901B(acDate, batchNo, 0, 99, 0, 9, this.index, this.limit, titaVo);
				} else if (drawdownCode == 99) {
					sBankRemit = bankRemitService.findL4901B(acDate, batchNo, 0, 99, statusCode, statusCode, this.index,
							this.limit, titaVo);
				} else if (statusCode == 9) {
					sBankRemit = bankRemitService.findL4901B(acDate, batchNo, drawdownCode, drawdownCode, 0, 9, this.index,
							this.limit, titaVo);
				} else {
					sBankRemit = bankRemitService.findL4901B(acDate, batchNo, drawdownCode, drawdownCode, statusCode,
							statusCode, this.index, this.limit, titaVo);
				}	
			}
		}
		this.info("L4901 list : " + lBankRemit);

		lBankRemit = sBankRemit == null ? null : sBankRemit.getContent();

		if (lBankRemit != null && lBankRemit.size() != 0) {
			for (BankRemit tBankRemit : lBankRemit) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOAcDate", tBankRemit.getAcDate());
				occursList.putParam("OOBatchNo", tBankRemit.getBatchNo());
				occursList.putParam("OODrawdownCode", tBankRemit.getDrawdownCode());
				occursList.putParam("OOStatusCode", tBankRemit.getStatusCode());
				occursList.putParam("OORemitBank", tBankRemit.getRemitBank());
				occursList.putParam("OORemitBranch", tBankRemit.getRemitBranch());

				CdBankId tCdBankId = new CdBankId();
				tCdBankId.setBankCode(tBankRemit.getRemitBank());
				tCdBankId.setBranchCode(tBankRemit.getRemitBranch());

				CdBank tCdBank = new CdBank();
				tCdBank = cdBankService.findById(tCdBankId, titaVo);

				String ckItem = "";
				String brItem = "";

				if (tCdBank != null) {
					ckItem = tCdBank.getBankItem();
					brItem = tCdBank.getBranchItem();
				}

				occursList.putParam("OORemitBankX", ckItem);
				occursList.putParam("OORemitBranchX", brItem);
				occursList.putParam("RemitAcctNo", tBankRemit.getRemitAcctNo());
				occursList.putParam("OOCustNo", tBankRemit.getCustNo());
				occursList.putParam("OOFacmNo", tBankRemit.getFacmNo());
				occursList.putParam("OOBormNo", tBankRemit.getBormNo());
				occursList.putParam("OOCustName", tBankRemit.getCustName());
				occursList.putParam("OORemaker", tBankRemit.getRemark());
				occursList.putParam("OOCurrencyCode", tBankRemit.getCurrencyCode());
				occursList.putParam("OORemitAmt", tBankRemit.getRemitAmt());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}