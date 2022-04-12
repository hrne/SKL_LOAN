package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4901")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4901 extends TradeBuffer {

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
	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	DataLog datalog;

	private List<BankRemit> lBankRemit = new ArrayList<BankRemit>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4901 ");
		this.totaVo.init(titaVo);

		int iSearchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String batchNo = FormatUtil.padX(titaVo.getParam("BatchNo"), 6);
		if (batchNo.isEmpty()) {
			batchNo = " ";
		}
		int iDrawdownCode = parse.stringToInteger(titaVo.getParam("DrawdownCode"));
		int iStatusCode = parse.stringToInteger(titaVo.getParam("StatusCode"));
		int drawdownCodeS = iDrawdownCode;
		int drawdownCodeE = iDrawdownCode;
		if (iDrawdownCode == 99) {
			drawdownCodeS = 0;
			drawdownCodeE = 99;
		}

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<BankRemit> slBankRemit = null;
		switch (iSearchFlag) {
		case 1: // 戶號
			slBankRemit = bankRemitService.findL4901A(iCustNo, this.index, this.limit);
			break;
		case 2: // 批號
			slBankRemit = bankRemitService.findL4901B(iAcDate, batchNo, drawdownCodeS, drawdownCodeE, 0, 9, this.index,
					this.limit, titaVo);
			break;
		case 3: // 全部批號
			slBankRemit = bankRemitService.findL4901C(iAcDate, drawdownCodeS, drawdownCodeE, 0, 9, this.index,
					this.limit, titaVo);
			break;
		}

		if (slBankRemit != null) {
			for (BankRemit t : slBankRemit.getContent()) {
				this.info(t.toString());

				switch (iStatusCode) {
				// 正常
				case 0:
					if (t.getActFg() == 0 || t.getActFg() == 2) {
						lBankRemit.add(t);
					}
					break;
				// 未放行
				case 1:
					if (t.getActFg() == 1) {
						lBankRemit.add(t);
					}
					break;
				// 產檔後訂正
				case 2:
					if (t.getStatusCode() == 2) {
						lBankRemit.add(t);
					}
					break;
				// 產檔後修正
				case 3:
					if (t.getStatusCode() == 3) {
						addDetail(t);
					}
					break;
				// 產檔後改單筆匯款
				case 4:
					if (t.getStatusCode() == 4) {
						addDetail(t);
					}
					break;
				case 9:
					lBankRemit.add(t);
					break;
				}
			}
		}
		if (lBankRemit.size() == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		for (BankRemit tBankRemit : lBankRemit) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOAcDate", tBankRemit.getAcDate());
			occursList.putParam("OOBatchNo", tBankRemit.getBatchNo());
			occursList.putParam("OODrawdownCode", tBankRemit.getDrawdownCode());
			occursList.putParam("OOStatusCode", tBankRemit.getStatusCode());
			occursList.putParam("OORemitBank", tBankRemit.getRemitBank());
			occursList.putParam("OORemitBranch", tBankRemit.getRemitBranch());

			CdBank tCdBank = new CdBank();
			if (!tBankRemit.getRemitBranch().isEmpty()) {
				tCdBank = cdBankService.findById(new CdBankId(tBankRemit.getRemitBank(), tBankRemit.getRemitBranch()),
						titaVo);
			}
			String ckItem = "";
			String brItem = "";

			if (tCdBank != null) {
				ckItem = tCdBank.getBankItem();
				brItem = tCdBank.getBranchItem();
			}
			String payCodeItem = "";
			CdCode tCdCode = cdCodeService.findById(new CdCodeId("PayCode", tBankRemit.getPayCode()), titaVo);
			if (tCdCode != null) {
				payCodeItem = tCdCode.getItem();
			}

			String relNo = titaVo.getKinbr() + tBankRemit.getTitaTlrNo() + tBankRemit.getTitaTxtNo();
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
			occursList.putParam("OOTellerNo", tBankRemit.getTitaTlrNo());
			occursList.putParam("OOTxtNo", tBankRemit.getTitaTxtNo());
			occursList.putParam("OOPayCode", tBankRemit.getPayCode());
			occursList.putParam("OOPayCodeX", payCodeItem);
			occursList.putParam("OORelNo", relNo);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void addDetail(BankRemit t) throws LogicException {

		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(t.getModifyContent());
		BankRemit t2 = new BankRemit();
		t2 = (BankRemit) datalog.clone(t);

		t.setStatusCode(5);
		lBankRemit.add(t);

		if (tTempVo.get("DrawdownCode") != null) {
			t2.setDrawdownCode(parse.stringToInteger(tTempVo.getParam("DrawdownCode")));
		}
		if (tTempVo.get("RemitAmt") != null) {
			t2.setRemitAmt(parse.stringToBigDecimal(tTempVo.getParam("RemitAmt")));
		}

		if (tTempVo.get("RemitBank") != null) {
			t2.setRemitBank(tTempVo.getParam("RemitBank"));
			t2.setRemitBranch(t.getRemitBranch());
		}
		if (tTempVo.get("RemitBranch") != null) {
			t2.setRemitBranch(tTempVo.getParam("RemitBranch"));
		}
		if (tTempVo.get("CustName") != null) {
			t2.setCustName(tTempVo.getParam("CustName"));
		}
		if (tTempVo.get("RemitAcctNo") != null) {
			t2.setRemitAcctNo(tTempVo.getParam("RemitAcctNo"));
		}
		if (tTempVo.get("Remark") != null) {
			t2.setRemark(tTempVo.getParam("Remark"));
		}
		lBankRemit.add(t2);

	}

}