package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2064")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2064 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2064.class);

	@Autowired
	public CdSyndFeeService sCdSyndFeeService;
	@Autowired
	public AcReceivableService sAcReceivableService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2064 ");
		this.totaVo.init(titaVo);

		// tita
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 86 * 100 = 8600

		Slice<AcReceivable> slAcReceivable = null;
		CdSyndFee tCdSyndFee = new CdSyndFee();
		// 查詢銷帳檔 戶號 RvNo=SL開頭的資料
		slAcReceivable = sAcReceivableService.useL2064Eq(iCustNo, 0, 9, "SL%", this.index, this.limit, titaVo);
		if (slAcReceivable == null) {
			throw new LogicException(titaVo, "E2003", "銷帳檔"); // 查無資料
		}
		String wkRvNo = "";// 銷帳編號
		String tSyndFeeCode = "";// 費用代碼
		String wkCdSyndItem = "";// 費用代碼中文
		String wkAcctCode = ""; // 業務科目
		BigDecimal wkClsAmt = BigDecimal.ZERO; // 已銷金額
		BigDecimal wkSyndFeeAmt = BigDecimal.ZERO; // 費用金額
		BigDecimal wkEntryAmt = BigDecimal.ZERO;// 已入帳金額
		List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		int i = 1;
		for (AcReceivable t : lAcReceivable) {
			this.info("wkRvNo = " + wkRvNo);
			this.info("t.getRvNo = " + t.getRvNo().substring(0, 9));
//			不同筆時清值
			if (!wkRvNo.equals(t.getRvNo().substring(0, 9))) {
				wkRvNo = t.getRvNo().substring(0, 9);
				wkClsAmt = BigDecimal.ZERO;
				wkSyndFeeAmt = BigDecimal.ZERO;
				wkEntryAmt = BigDecimal.ZERO;
			}
			// RvNo = SL+-+費用代碼(2)+-+流水號(3)
			if (!("".equals(t.getRvNo())) && t.getRvNo().length() >= 9 && "SL".equals(t.getRvNo().substring(0, 2))) {
				// 取費用代碼
				tSyndFeeCode = t.getRvNo().substring(3, 5);
			}
			this.info("tSyndFeeCode = " + tSyndFeeCode);

			// 檢查費用設定檔
			// 取費用說明、該費用業務科目
			tCdSyndFee = sCdSyndFeeService.findById(tSyndFeeCode, titaVo);
			if (tCdSyndFee != null) {
				wkCdSyndItem = tCdSyndFee.getSyndFeeItem();
				wkAcctCode = tCdSyndFee.getAcctCode();
			}
			if (t.getRvAmt().compareTo(t.getRvBal()) >= 0) {

				wkClsAmt = wkClsAmt.add(t.getRvAmt().subtract(t.getRvBal()));
			}
			wkSyndFeeAmt = wkSyndFeeAmt.add(t.getRvAmt());
			// 已入帳金額邏輯
			if (t.getReceivableFlag() == 5) {
				wkEntryAmt = wkEntryAmt.add(t.getRvAmt());
			}
			if (t.getReceivableFlag() == 3) {
				wkEntryAmt = wkEntryAmt.add(t.getRvAmt().subtract(t.getRvBal()));
			}
			// 最後一筆 或 與下筆不同資料時塞入tota
			if (i == lAcReceivable.size() || !wkRvNo.equals(lAcReceivable.get(i).getRvNo().substring(0, 9))) {

				OccursList occursList = new OccursList();

				occursList.putParam("OOCustNo", t.getCustNo());// 戶號
				occursList.putParam("OOFacmNo", t.getFacmNo()); // 額度
				occursList.putParam("OOSyndFeeItem", tSyndFeeCode + "-" + wkCdSyndItem); // 聯貸費用代碼+說明
				occursList.putParam("OOSyndFee", wkSyndFeeAmt);// 費用金額
				occursList.putParam("OOClsAmt", wkClsAmt);// 已銷金額
				occursList.putParam("OORmk", t.getSlipNote());// 備註
				occursList.putParam("OORvNo", wkRvNo); // 銷帳編號
				occursList.putParam("OOAcctCode", t.getAcctCode()); // 業務科目
				occursList.putParam("OOEntryAmt", wkEntryAmt); // 已入帳金額
//				新增顯示已入帳 Y 時不可刪除
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			i++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}