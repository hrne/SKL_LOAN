package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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
//import com.st1.itx.db.domain.AcDetail;
//import com.st1.itx.db.domain.AcDetailId;
import com.st1.itx.db.domain.AcReceivable;
//import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * AcBookCode=X,3 BranchNo=9,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 CustNo=9,7 FacmNo=9,3 AcctCode=X,3 ClsFlag=9,1 END=X,1<br>
 */

@Service("L6907")
@Scope("prototype")
/**
 *
 *
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6907 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6907.class);

	/* DB服務注入 */
	@Autowired
	public AcReceivableService sAcReceivableService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String acctCode;
	// private String acctItem;
	private CdAcCode tCdAcCode = new CdAcCode();
	private CdAcCodeId CdAcCodeId = new CdAcCodeId();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6907 ");
		this.totaVo.init(titaVo);

		// tita
		// 帳冊別(必輸)
		String iAcBookCode = titaVo.getParam("AcBookCode");
		//區隔帳冊
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		
		// 單位別(自動顯示)
		String iBranchNo = titaVo.getParam("BranchNo");
		// 幣別(自動顯示)
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		// 科目代號
		String iAcNoCode = titaVo.getParam("AcNoCode");
		// 子目代號
		String iAcSubCode = titaVo.getParam("AcSubCode");
		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		// 細目代碼
		String iAcDtlCode = titaVo.getParam("AcDtlCode");
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}
		// 戶號
		int iCustNoStartAt = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iCustNoEndAt = 9999999;
		// 如戶號有輸入則查詢輸入值 沒輸入查0-9999999全部
		if (iCustNoStartAt > 0) {
			iCustNoEndAt = iCustNoStartAt;
		}
		// 額度編號
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 業務科目
		String iAcctCode = titaVo.getParam("AcctCode");
		int iClsFlag = parse.stringToInteger(titaVo.getParam("ClsFlag"));

		// new ArrayList
		List<AcReceivable> tmplAcReceivable = new ArrayList<AcReceivable>();
		Slice<AcReceivable> slAcReceivable = null;
		// 輸入科子細目
		if (!iAcNoCode.isEmpty()) {
				slAcReceivable = sAcReceivableService.acrvClsFlagSubBook(iClsFlag, iAcBookCode, iAcSubBookCode.trim()+"%", iBranchNo, iCurrencyCode, iAcNoCode, iAcSubCode, iAcDtlCode, iCustNoStartAt, iCustNoEndAt, this.index, Integer.MAX_VALUE,titaVo);
			
			// 業務科目有輸入
		} else if (!iAcctCode.isEmpty()) {
				slAcReceivable = sAcReceivableService.acctCodeSubBook(iClsFlag, iAcBookCode, iAcSubBookCode.trim()+"%", iAcctCode, iCustNoStartAt, iCustNoEndAt, this.index, Integer.MAX_VALUE, titaVo);
			
		} else {
				slAcReceivable = sAcReceivableService.acrvFacmNoSubBook(iClsFlag, iAcBookCode, iAcSubBookCode.trim()+"%", iCustNoStartAt, 0, iFacmNo, 999, this.index, Integer.MAX_VALUE, titaVo);
			}
			
		List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
// 查無資料 拋錯
		if (lAcReceivable == null) {
			throw new LogicException(titaVo, "E0001", "會計銷帳檔"); // 查無資料
		}
		// 如帳冊別輸入201 list取帳冊別值為201資料
//		int fg = 0;
//
//		for (AcReceivable tAcReceivable : lAcReceivable) {
//			this.info(";L6907 tAcReceivable =" + tAcReceivable);
//			fg = 0;
//
//			if (!(iAcBookCode.equals("000") || iAcBookCode.equals("10H")) && !tAcReceivable.getAcBookCode().equals(iAcBookCode))
//				fg = 1;
//			if (iFacmNo > 0 && tAcReceivable.getFacmNo() != iFacmNo)
//				fg = 1;
//
//			if (fg == 0) {
//
//				tmplAcReceivable.add(tAcReceivable);
//			}
//		}
		int custNo = 0;
		BigDecimal rvBal = BigDecimal.ZERO;
		for (AcReceivable tAcReceivable : lAcReceivable) {
			this.info("tAcReceivable = " + tAcReceivable);
			// new occurs
			OccursList occurslist = new OccursList();

			if (tAcReceivable.getCustNo() != custNo) {
				rvBal = tAcReceivable.getRvBal();
				custNo = tAcReceivable.getCustNo();
			} else {
				rvBal = rvBal.add(tAcReceivable.getRvBal());

			}

			occurslist.putParam("OOAcNoCode", tAcReceivable.getAcNoCode());
			occurslist.putParam("OOAcSubCode", tAcReceivable.getAcSubCode());
			occurslist.putParam("OOAcDtlCode", tAcReceivable.getAcDtlCode());

			occurslist.putParam("OOAcctItem", getAcctItem(tAcReceivable, titaVo));
			occurslist.putParam("OOCustNo", tAcReceivable.getCustNo());
			occurslist.putParam("OOFacmNo", tAcReceivable.getFacmNo());
			occurslist.putParam("OORvNo", tAcReceivable.getRvNo());
			occurslist.putParam("OOOpenAcDate", tAcReceivable.getOpenAcDate());
			occurslist.putParam("OORvAmt", tAcReceivable.getRvAmt());
			occurslist.putParam("OOLastTxDate", tAcReceivable.getLastTxDate());
			occurslist.putParam("OORvBal", tAcReceivable.getRvBal());
			occurslist.putParam("OO_SUM", rvBal);
			occurslist.putParam("OOAcBookCode", tAcReceivable.getAcBookCode());
			occurslist.putParam("OOAcSubBookCode", tAcReceivable.getAcSubBookCode());
			
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getAcctItem(AcReceivable rv, TitaVo titaVo) throws LogicException {
		acctCode = rv.getAcctCode();
		if (acctCode == null || acctCode.length() < 3)
			acctCode = "   ";
		String acctItem = "";
//	 	  銷帳科目記號ReceivableFlag = 4-短繳期金
//		   Z10	短期擔保放款	310	短期擔保放款
//		   Z20	中期擔保放款	320	中期擔保放款
//		   Z30	長期擔保放款	330	長期擔保放款
//		   Z40	三十年房貸	340	三十年房貸
//
//		   ZC1	短擔息	      IC1	短擔息
//		   ZC2	中擔息	      IC2	中擔息
//		   ZC3	長擔息	      IC3	長擔息
//		   ZC4	三十年房貸息	  IC4	三十年房貸息
//		   ZOV	逾期息              IOV	逾期息
//		   ZOP	違約金              IOP	違約金       
//
//	 	   銷帳科目記號ReceivableFlag = 5-另收欠款
//	       YOP 清償違約金         IOP	違約金              
		if ("Z".equals(acctCode.substring(0, 1))) {
			if ("ZC".equals(acctCode.substring(0, 2)))
				acctItem = "短繳利息";
			else if ("ZOV".equals(acctCode.substring(0, 2)))
				acctItem = "短繳逾期息";
			else if ("ZOP".equals(acctCode))
				acctItem = "短繳違約金";
			else
				acctItem = "短繳本金";
		}
		if ("YOP".equals(acctCode))
			acctItem = "清償違約金";
		if ("".equals(acctItem)) {
			if (acctCode.trim().length() > 0) {
				tCdAcCode = cdAcCodeService.acCodeAcctFirst(acctCode, titaVo);
			} else {
				CdAcCodeId.setAcNoCode(rv.getAcNoCode());
				CdAcCodeId.setAcSubCode(rv.getAcSubCode());
				CdAcCodeId.setAcDtlCode(rv.getAcDtlCode());
				tCdAcCode = cdAcCodeService.findById(CdAcCodeId, titaVo);
			}
			acctItem = tCdAcCode.getAcctItem();
		}

		return acctItem;
	}
}