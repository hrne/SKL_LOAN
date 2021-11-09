package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ACCTDATEFM=9,7<br>
 * ACCTDATETO=9,7<br>
 * END=X,1<br>
 */

@Service("L4001")
@Scope("prototype")
public class L4001 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	DataLog datalog;

	@Autowired
	public BankRemitService bankRemitService;

	private BigDecimal oRemitAmt = new BigDecimal(0);
	private HashMap<L4001Vo, Integer> L4001VoCnt = new HashMap<>();

	private HashMap<L4001Vo, BigDecimal> L4001VoSum = new HashMap<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4001 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		int acDateFrom = parse.stringToInteger(titaVo.getParam("AcDateFrom")) + 19110000;
		int acDateTo = parse.stringToInteger(titaVo.getParam("AcDateTo")) + 19110000;
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode")); // 1.撥款 2.退款

		Slice<BankRemit> slBankRemit = bankRemitService.findL4001A(acDateFrom, acDateTo, this.index, this.limit);

		if (slBankRemit == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		// put
		for (BankRemit tBankRemit : slBankRemit.getContent()) {
			//作業項目為1.撥款時把退款篩選掉
			if (iItemCode == 1) {
				if (tBankRemit.getDrawdownCode() == 4 || tBankRemit.getDrawdownCode() == 5
						|| tBankRemit.getDrawdownCode() == 11) {
					continue;
				}
			}

			//作業項目為2.退款時把撥款篩選掉
			if (iItemCode == 2) {
				if (tBankRemit.getDrawdownCode() == 1 || tBankRemit.getDrawdownCode() == 2) {
					continue;
				}
			}

			if (tBankRemit.getActFg() == 1) {
				add(3, tBankRemit, titaVo);
			} else {
				add(tBankRemit.getStatusCode(), tBankRemit, titaVo);
			}
		}

		// get & sort
		Set<L4001Vo> tempSet = L4001VoCnt.keySet();
		List<L4001Vo> tempList = new ArrayList<>();
		for (Iterator<L4001Vo> it = tempSet.iterator(); it.hasNext();) {
			L4001Vo tempL4001Vo = it.next();
			tempList.add(tempL4001Vo);
		}

		tempList.sort((c1, c2) -> {
			return c1.compareTo(c2);
		});

		// output
		for (L4001Vo tempL4001Vo : tempList) {
			BigDecimal sum = L4001VoSum.get(tempL4001Vo);
			OccursList occursList = new OccursList();
			occursList.putParam("OOAcDate", tempL4001Vo.getoAcDate());
			occursList.putParam("OOBatchNo", tempL4001Vo.getoBatchNo().trim());
			occursList.putParam("OODrawdownCode", tempL4001Vo.getoDrawdownCode());
			occursList.putParam("OOStatusCode", tempL4001Vo.getoStatusCode());
			occursList.putParam("OOCount", L4001VoCnt.get(tempL4001Vo));
			occursList.putParam("OOCurName", titaVo.getCurName());
			occursList.putParam("OORemitAmt", sum);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void add(int statusCode, BankRemit tBankRemit, TitaVo titaVo) throws LogicException {
		oRemitAmt = tBankRemit.getRemitAmt();

		L4001Vo tmpL4001 = new L4001Vo(tBankRemit.getAcDate(), tBankRemit.getBatchNo(), tBankRemit.getDrawdownCode(),
				statusCode);

		if (L4001VoCnt.containsKey(tmpL4001)) {
			L4001VoCnt.put(tmpL4001, L4001VoCnt.get(tmpL4001) + 1);
		} else {
			L4001VoCnt.put(tmpL4001, 1);
		}

		if (L4001VoSum.containsKey(tmpL4001)) {
			oRemitAmt = oRemitAmt.add(L4001VoSum.get(tmpL4001));
		}

		L4001VoSum.put(tmpL4001, oRemitAmt);
	}
}

class L4001Vo implements Comparable<L4001Vo> {
	private int oAcDate = 0;
	private String oBatchNo = "";
	private int oDrawdownCode = 0;
	private int oStatusCode = 0;

	public L4001Vo(int oAcDate, String oBatchNo, int oDrawdownCode, int oStatusCode) {
		this.oAcDate = oAcDate;
		this.oBatchNo = oBatchNo;
		this.oDrawdownCode = oDrawdownCode;
		this.oStatusCode = oStatusCode;
	}

	public int getoAcDate() {
		return oAcDate;
	}

	public void setoAcDate(int oAcDate) {
		this.oAcDate = oAcDate;
	}

	public String getoBatchNo() {
		return oBatchNo;
	}

	public void setoBatchNo(String oBatchNo) {
		this.oBatchNo = oBatchNo;
	}

	public int getoDrawdownCode() {
		return oDrawdownCode;
	}

	public void setoDrawdownCode(int oDrawdownCode) {
		this.oDrawdownCode = oDrawdownCode;
	}

	public int getoStatusCode() {
		return oStatusCode;
	}

	public void setoStatusCode(int oStatusCode) {
		this.oStatusCode = oStatusCode;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		L4001Vo l4001Vo = (L4001Vo) obj;
		return oAcDate == l4001Vo.getoAcDate() && oBatchNo.equals(l4001Vo.getoBatchNo())
				&& oDrawdownCode == l4001Vo.getoDrawdownCode() && oStatusCode == l4001Vo.getoStatusCode();
	}

	public int hashCode() {
		return Objects.hash(oAcDate, oBatchNo, oDrawdownCode, oStatusCode);
	}

	@Override
	public int compareTo(L4001Vo other) {

		if (this.oAcDate - other.oAcDate != 0) {
			return this.oAcDate - other.oAcDate;
		} else if (this.oBatchNo.compareTo(other.oBatchNo) != 0) {
			return this.oBatchNo.compareTo(other.oBatchNo);
		} else if (this.oDrawdownCode - other.oDrawdownCode != 0) {
			return this.oDrawdownCode - other.oDrawdownCode;
		} else if (this.oStatusCode - other.oStatusCode != 0) {
			return this.oStatusCode - other.oStatusCode;
		} else {
			return 0;
		}
	}
}