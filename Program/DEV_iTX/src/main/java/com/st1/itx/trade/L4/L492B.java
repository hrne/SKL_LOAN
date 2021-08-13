package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * END=X,1<br>
 */

@Service("L492B")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L492B extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L492B.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcReceivableService acReceivableService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L492B ");
		this.totaVo.init(titaVo);

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		BigDecimal sumBal = new BigDecimal("0");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<AcReceivable> sAcReceivable = null;

		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

		sAcReceivable = acReceivableService.acrvFacmNoRange(iCustNo, iCustNo, 0, iFacmNo, iFacmNo, this.index, this.limit);

		lAcReceivable = sAcReceivable == null ? null : sAcReceivable.getContent();

		if (lAcReceivable != null && lAcReceivable.size() != 0) {
			for (AcReceivable tAcReceivable : lAcReceivable) {

				/* 未銷餘額大於零者且非支票者 */
				if (tAcReceivable.getRvBal().compareTo(new BigDecimal("0")) == 1 && !tAcReceivable.getAcctCode().equals("TCK")) {

					OccursList occursList = new OccursList();
					occursList.putParam("OOFacmNo", tAcReceivable.getFacmNo());
					occursList.putParam("OOAcctCode", tAcReceivable.getAcctCode());
					occursList.putParam("OORvNo", tAcReceivable.getRvNo());
					occursList.putParam("OOAcNoCode", tAcReceivable.getAcNoCode());
					occursList.putParam("OOAcSubCode", tAcReceivable.getAcSubCode());
					occursList.putParam("OOAcDtlCode", tAcReceivable.getAcDtlCode());
					occursList.putParam("OOOpenAcDate", tAcReceivable.getOpenAcDate());
					occursList.putParam("OOLastTxDate", tAcReceivable.getLastTxDate());
					occursList.putParam("OORvAmt", tAcReceivable.getRvAmt());
					occursList.putParam("OORvBal", tAcReceivable.getRvBal());
					sumBal = sumBal.add(tAcReceivable.getRvBal());

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				} else {
					continue;
				}
			}
			this.totaVo.putParam("OSumBal", sumBal);
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}