package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R13")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R13 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacProdService facProdService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R13 ");
		this.totaVo.init(titaVo);

		int adjDate = parse.stringToInteger(titaVo.getParam("RimAdjDate")) + 19110000;
		int txKind = parse.stringToInteger(titaVo.getParam("RimTxKind"));
		int custType1 = 0;
		int custType2 = 0;

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

		if (parse.stringToInteger(titaVo.getParam("RimCustType")) == 2) {
			custType1 = 1;
			custType2 = 2;
		}

		Slice<BatxRateChange> sBatxRateChange = null;

		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();
//		調出AdjCode = 5 者 上限100筆
		sBatxRateChange = batxRateChangeService.findL4931AEq(custType1, custType2, txKind, txKind, 4, 4, adjDate, adjDate, this.index, this.limit, titaVo);

		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange != null && lBatxRateChange.size() != 0) {

			this.info("Size : " + lBatxRateChange.size());

			for (int i = 0; i < lBatxRateChange.size(); i++) {
				BatxRateChange tBatxRateChange = new BatxRateChange();
				tBatxRateChange = lBatxRateChange.get(i);

				int j = i + 1;

				if (j == 101) {
					this.info("已超過100筆");
					break;
				}

				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(tBatxRateChange.getBatxRateChangeId().getCustNo(), tBatxRateChange.getBatxRateChangeId().getCustNo());

				FacProd tFacProd = facProdService.findById(tBatxRateChange.getProdNo());

				this.totaVo.putParam("L4r13CustNo" + j, tBatxRateChange.getBatxRateChangeId().getCustNo());
				this.totaVo.putParam("L4r13FacmNo" + j, tBatxRateChange.getBatxRateChangeId().getFacmNo());
				this.totaVo.putParam("L4r13BormNo" + j, tBatxRateChange.getBatxRateChangeId().getBormNo());
				this.totaVo.putParam("L4r13CustName" + j, tCustMain.getCustName());
				this.totaVo.putParam("L4r13ProdNo" + j, tBatxRateChange.getProdNo());
				this.totaVo.putParam("L4r13AdjFreq" + j, tBatxRateChange.getTxRateAdjFreq());
				this.totaVo.putParam("L4r13PresEffDate" + j, tBatxRateChange.getPresEffDate());
				this.totaVo.putParam("L4r13CurtEffDate" + j, tBatxRateChange.getCurtEffDate());
				this.totaVo.putParam("L4r13PrevIntDate" + j, tBatxRateChange.getPrevIntDate());
				this.totaVo.putParam("L4r13RateIncr" + j, tBatxRateChange.getRateIncr());
				this.totaVo.putParam("L4r13ContractRate" + j, tBatxRateChange.getContractRate());
				this.totaVo.putParam("L4r13PresentRate" + j, tBatxRateChange.getPresentRate());
				this.totaVo.putParam("L4r13ProposalRate" + j, tBatxRateChange.getProposalRate());
				this.totaVo.putParam("L4r13AdjustedRate" + j, tBatxRateChange.getAdjustedRate());
				this.totaVo.putParam("L4r13LowLimitRate" + j, tFacProd.getLowLimitRate());

			}
			this.info("totaVo 1 : " + this.totaVo.toString());

//			少於100補空
			for (int j = lBatxRateChange.size() + 1; j <= 100; j++) {
				this.totaVo.putParam("L4r13CustNo" + j, 0);
				this.totaVo.putParam("L4r13FacmNo" + j, 0);
				this.totaVo.putParam("L4r13BormNo" + j, 0);
				this.totaVo.putParam("L4r13CustName" + j, "");
				this.totaVo.putParam("L4r13ProdNo" + j, "");
				this.totaVo.putParam("L4r13AdjFreq" + j, 0);
				this.totaVo.putParam("L4r13PresEffDate" + j, 0);
				this.totaVo.putParam("L4r13CurtEffDate" + j, 0);
				this.totaVo.putParam("L4r13PrevIntDate" + j, 0);
				this.totaVo.putParam("L4r13RateIncr" + j, 0);
				this.totaVo.putParam("L4r13ContractRate" + j, 0);
				this.totaVo.putParam("L4r13PresentRate" + j, 0);
				this.totaVo.putParam("L4r13ProposalRate" + j, 0);
				this.totaVo.putParam("L4r13AdjustedRate" + j, 0);
				this.totaVo.putParam("L4r13LowLimitRate" + j, 0);
			}
			this.info("totaVo 2 : " + this.totaVo.toString());
		} else {
			throw new LogicException("E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}