package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L6907ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6907")
@Scope("prototype")
/**
 *
 * @author YuJiaXing
 * @author Mata 2022/5/13
 * @version 1.0.0
 */
public class L6907 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService sAcReceivableService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public L6907ServiceImpl l6907ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String acctCode;
	CdAcCode tCdAcCode = new CdAcCode();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6907 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 45 * 200 = 9000

		List<Map<String, String>> L6907List = null;

		try {
			L6907List = l6907ServiceImpl.FindAll(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		if (L6907List == null || L6907List.size() == 0) {
			// 查無資料 拋錯
			throw new LogicException(titaVo, "E0001", "會計銷帳檔");
		}

//		BigDecimal rvBal = BigDecimal.ZERO;
//		BigDecimal iRvBal0 = BigDecimal.ZERO;
//		BigDecimal iRvBal = BigDecimal.ZERO;
		for (Map<String, String> tAcReceivable : L6907List) {
			OccursList occursList = new OccursList();
//			rvBal = BigDecimal.ZERO;
//			iRvBal0 = BigDecimal.ZERO;
//			iRvBal = BigDecimal.ZERO;
//			// 同戶號、業務代號金額累加全部(同戶號、業務代號戶號合計需相同)
//			
//			for(Map<String, String> tAcReceivable2 : L6907List) {
//				if(tAcReceivable2.get("CustNo").equals(tAcReceivable.get("CustNo"))
//						&&tAcReceivable2.get("AcctCode").equals(tAcReceivable.get("AcctCode"))){
//					if(tAcReceivable2.get("rvBal") == null) {
//						 iRvBal0 = new BigDecimal(0);
//					}else {						
//						 iRvBal = new BigDecimal(tAcReceivable2.get("rvBal")); 
//					}
//					rvBal = rvBal.add(iRvBal);
//				}
//			}

			// new occurs
			// 科子項目
			occursList.putParam("OOAcNoCode", tAcReceivable.get("AcNoCode"));
			occursList.putParam("OOAcSubCode", tAcReceivable.get("AcSubCode"));
			occursList.putParam("OOAcDtlCode", tAcReceivable.get("AcDtlCode"));
			occursList.putParam("OOAcBookCode", tAcReceivable.get("AcBookCode"));

			// Y-顯示[明細]按鈕
			String l6908Flag = "Y";
			// 未收費用未變動不顯示按鈕
			if (parse.stringToInteger(tAcReceivable.get("ReceivableFlag")) >= 3
					&& tAcReceivable.get("RvAmt").compareTo(tAcReceivable.get("RvBal")) == 0) {
				l6908Flag = "";
			}
			// 交易序號 = 0不顯示按鈕 
			occursList.putParam("L6908Flag", l6908Flag);
            if ("0".equals(tAcReceivable.get("TitaTxtNo"))) {
				l6908Flag = "";         	
            }
			// 戶號 OOCustNoX
			occursList.putParam("OOCustNoX", tAcReceivable.get("CustNo") + '-' + tAcReceivable.get("FacmNo"));
			occursList.putParam("OOCustNo", tAcReceivable.get("CustNo"));
			occursList.putParam("OOFacmNo", tAcReceivable.get("FacmNo"));

			// 業務科目 OOAcctCodeX
			occursList.putParam("OOAcctCode", tAcReceivable.get("AcctCode"));
			occursList.putParam("OOAcctItem", getAcctItem(tAcReceivable, titaVo)); // 用不到
			// 銷帳編號 OORvNo
			occursList.putParam("OORvNo", tAcReceivable.get("RvNo"));
			// 起帳日期 OOOpenAcDate
			occursList.putParam("OOOpenAcDate", tAcReceivable.get("OpenAcDate"));
			// 起帳金額 #OORvAmt
			occursList.putParam("OORvAmt", tAcReceivable.get("RvAmt"));
			// 最後交易日 #OOLastTxDate
			occursList.putParam("OOLastTxDate", tAcReceivable.get("LastTxDate"));
			// 未銷餘額 OORvBal
			occursList.putParam("OORvBal", tAcReceivable.get("RvBal"));
			// 業務科目合計 OO_SUM1
			occursList.putParam("OO_SUM", tAcReceivable.get("SumRvBal"));
			// 區隔帳冊 OOAcSubBookCode
			occursList.putParam("OOAcSubBookCode", tAcReceivable.get("AcSubBookCode"));
			// 最後修改日期 OOLastUpdate
			occursList.putParam("OOLastUpdate", parse.stringToStringDateTime(tAcReceivable.get("LastUpdate")));
			// 最後修改人員 OOLastEmp
			occursList.putParam("OOLastEmp", tAcReceivable.get("LastUpdateEmpNo"));

			this.totaVo.addOccursList(occursList);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getAcctItem(Map<String, String> rv, TitaVo titaVo) throws LogicException {

		acctCode = rv.get("AcctCode");
		if (acctCode == null || acctCode.length() < 3)
			acctCode = "   ";
		String acctItem = "";

		CdAcCode tCdAcCode = new CdAcCode();
		CdAcCodeId CdAcCodeId = new CdAcCodeId();
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
				CdAcCodeId.setAcNoCode(rv.get("AcNoCode"));
				CdAcCodeId.setAcSubCode(rv.get("AcSubCode"));
				CdAcCodeId.setAcDtlCode(rv.get("AcDtlCode"));
				tCdAcCode = cdAcCodeService.findById(CdAcCodeId, titaVo);
			}
			acctItem = tCdAcCode.getAcctItem();
		}

		return acctItem;
	}

}