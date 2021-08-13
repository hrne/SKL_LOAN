package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4031")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4031 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4031.class);

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

// 	總筆數
	private HashMap<tmpBatx, Integer> totCnt = new HashMap<>();
// 	已確認筆數
	private HashMap<tmpBatx, Integer> conCnt = new HashMap<>();
// 	合計筆數筆數
	private HashMap<tmpBatx, Integer> sumCnt = new HashMap<>();
// 	不處理筆數筆數
	private HashMap<tmpBatx, Integer> ignCnt = new HashMap<>();
// 	未處理筆數筆數
	private HashMap<tmpBatx, Integer> undCnt = new HashMap<>();
// 	狀態
	private HashMap<tmpBatx, Integer> status = new HashMap<>();

//	確認是否同作業項目皆已確認:0.尚有未確認 1.全皆已確認
	private int checkFlag = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4031 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		int date = parse.stringToInteger(titaVo.getParam("RateChangeDate")) + 19110000;
		int custType = parse.stringToInteger(titaVo.getParam("CustType"));

		int custCode1 = 0;
		int custCode2 = 0;
//		1:個金;2:企金（含企金自然人）
		if (custType == 2) {
			custCode1 = 1;
			custCode2 = 2;
		}

		Slice<BatxRateChange> sBatxRateChange = null;

//		0:個金 1:企金 2:企金自然人
		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();
		sBatxRateChange = batxRateChangeService.custCodeEq(date, date, custCode1, custCode2, this.index, this.limit);

		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange != null && lBatxRateChange.size() != 0) {
			for (BatxRateChange tBatxRateChange : lBatxRateChange) {
//				Key = 調整日 + 戶別(自然人;企金戶) + 指標利率代碼 + 調整記號 + 是否輸入利率 + 排序
//				#OOLableA 指標利率代碼 
//				#OOLableB 調整記號
//				#OOLableC 是否輸入
//				#OORank 排序

				tmpBatx grp1 = new tmpBatx(tBatxRateChange.getTxKind(), 0, 0, 1);
// 				grp1 (Group by 指標利率代碼)
				setCount(tBatxRateChange, grp1);

				tmpBatx grp2 = new tmpBatx(tBatxRateChange.getTxKind(), tBatxRateChange.getAdjCode(), 0, 2);
// 				grp2 (Group by 指標利率代碼，調整記號)
				setCount(tBatxRateChange, grp2);

//				該作業項目狀態 0.未確認 1.確認未放行 2.已確認放行
				if (tBatxRateChange.getRateKeyInCode() != 2 && !status.containsKey(grp1)) {
					status.put(grp1, tBatxRateChange.getConfirmFlag());
				}
			}

			Set<tmpBatx> tempSet = totCnt.keySet();

			List<tmpBatx> tempList = new ArrayList<>();

			for (Iterator<tmpBatx> it = tempSet.iterator(); it.hasNext();) {
				tmpBatx tmpBatxVo = it.next();
				tempList.add(tmpBatxVo);
			}

			tempList.sort((c1, c2) -> {
				return c1.compareTo(c2);
			});

			for (tmpBatx tempL4031Vo : tempList) {
				OccursList occursList = new OccursList();

				if (undCnt.get(tempL4031Vo) >= 1) {
					checkFlag = 9;
				} else {
					if (sumCnt.get(tempL4031Vo) == conCnt.get(tempL4031Vo)) {
						checkFlag = 1;
					} else {
						checkFlag = 0;
					}
				}

				occursList.putParam("OORank", tempL4031Vo.getRank());
				occursList.putParam("OOLableA", tempL4031Vo.getLableA());
				occursList.putParam("OOLableB", tempL4031Vo.getLableB());
//				occursList.putParam("OOLableC", tempL4031Vo.getLableC());
				occursList.putParam("OOSumCnt", sumCnt.get(tempL4031Vo));
				occursList.putParam("OOIgnCnt", ignCnt.get(tempL4031Vo));
				occursList.putParam("OOCheckFlag", checkFlag);
				occursList.putParam("OOStatus", status.get(tempL4031Vo));

				this.info("L4031 occursList : " + occursList.toString());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

//	暫時紀錄戶號額度
	private class tmpBatx implements Comparable<tmpBatx> {

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + lableA;
			result = prime * result + lableB;
			result = prime * result + lableC;
			result = prime * result + rank;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpBatx other = (tmpBatx) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (lableA != other.lableA)
				return false;
			if (lableB != other.lableB)
				return false;
			if (lableC != other.lableC)
				return false;
			if (rank != other.rank)
				return false;
			return true;
		}

		private int lableA = 0;
		private int lableB = 0;
		private int lableC = 0;
		private int rank = 0;

		public tmpBatx(int lableA, int lableB, int lableC, int rank) {
			this.setLableA(lableA);
			this.setLableB(lableB);
			this.setLableC(lableC);
			this.setRank(rank);
		}

		public int getLableA() {
			return lableA;
		}

		public void setLableA(int lableA) {
			this.lableA = lableA;
		}

		public int getLableB() {
			return lableB;
		}

		public void setLableB(int lableB) {
			this.lableB = lableB;
		}

		public int getLableC() {
			return lableC;
		}

		public void setLableC(int lableC) {
			this.lableC = lableC;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		@Override
		public int compareTo(tmpBatx other) {
			if (this.lableA - other.lableA != 0) {
				return this.lableA - other.lableA;
			} else if (this.lableB - other.lableB != 0) {
				return this.lableB - other.lableB;
			} else if (this.lableC - other.lableC != 0) {
				return this.lableC - other.lableC;
			} else if (this.rank - other.rank != 0) {
				return this.rank - other.rank;
			} else {
				return 0;
			}
		}

		@Override
		public String toString() {
			return "tmpBatx [lableA=" + lableA + ", lableB=" + lableB + ", lableC=" + lableC + ", rank=" + rank + "]";
		}

		private L4031 getEnclosingInstance() {
			return L4031.this;
		}
	}

	private void setCount(BatxRateChange tBatxRateChange, tmpBatx grp) {
		this.info("grp : " + grp.toString());
		if (totCnt.containsKey(grp)) {
			totCnt.put(grp, totCnt.get(grp) + 1);
		} else {
			totCnt.put(grp, 1);
		}

		if (!conCnt.containsKey(grp)) {
			conCnt.put(grp, 0);
		}

		if (tBatxRateChange.getConfirmFlag() >= 1) {
			if (conCnt.containsKey(grp)) {
				conCnt.put(grp, conCnt.get(grp) + 1);
			}
		}

		if (!undCnt.containsKey(grp)) {
			undCnt.put(grp, 0);
		}

		if (tBatxRateChange.getAdjCode() == 3 || tBatxRateChange.getAdjCode() == 4) {
			if (undCnt.containsKey(grp)) {
				undCnt.put(grp, undCnt.get(grp) + 1);
			}
		}

		if (!ignCnt.containsKey(grp)) {
			ignCnt.put(grp, 0);
		}

		if (!sumCnt.containsKey(grp)) {
			sumCnt.put(grp, 0);
		}

		if (tBatxRateChange.getRateKeyInCode() == 2) {
			if (ignCnt.containsKey(grp)) {
				ignCnt.put(grp, ignCnt.get(grp) + 1);
			}
		} else {
			if (sumCnt.containsKey(grp)) {
				sumCnt.put(grp, sumCnt.get(grp) + 1);
			}
		}
	}
}