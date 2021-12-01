package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	private HashMap<tmpBatx, Integer> sumCnt = new HashMap<>();// 要處理筆數
	private HashMap<tmpBatx, Integer> ignCnt = new HashMap<>();// 待處理筆數
	private HashMap<tmpBatx, Integer> totCnt = new HashMap<>();// 總筆數
	private HashMap<tmpBatx, Integer> conCnt = new HashMap<>();// 已確認筆數
	private HashMap<tmpBatx, Integer> keyinCnt = new HashMap<>();// 已處理筆數

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
//				Key = 調整日 + 戶別(自然人;企金戶) + 作業項目 + 調整記號 + 輸入記號 + 排序
//				#OOLableA 作業項目
//				#OOLableB 調整記號
//				#OOLableC 輸入記號
//				#OORank 排序

				tmpBatx grp1 = new tmpBatx(tBatxRateChange.getTxKind(), 0, 0, 1);
// 				grp1 (Group by 作業項目 )
				setCount(tBatxRateChange, grp1, 1);

				tmpBatx grp2 = new tmpBatx(tBatxRateChange.getTxKind(), tBatxRateChange.getAdjCode(),
						tBatxRateChange.getRateKeyInCode(), 2);
// 				grp2 (Group by 作業項目，調整記號, 輸入記號 )
				setCount(tBatxRateChange, grp2, 2);

//				該作業項目狀態 0.未確認 1.確認未放行 2.已確認放行
				if (tBatxRateChange.getRateKeyInCode() != 9 && !status.containsKey(grp1)) {
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
				if (totCnt.get(tempL4031Vo) == 0 && sumCnt.get(tempL4031Vo) == 0 && ignCnt.get(tempL4031Vo) == 0) {
					continue;
				}
				OccursList occursList = new OccursList();
				this.info("totCnt=" + totCnt.get(tempL4031Vo) + ", sumCnt = " + sumCnt.get(tempL4031Vo) + ",ignCnt="
						+ ignCnt.get(tempL4031Vo) + ", conCnt=" + conCnt.get(tempL4031Vo));
				checkFlag = 9;
				if (tempL4031Vo.getRank() == 1) {
					if (sumCnt.get(tempL4031Vo) == conCnt.get(tempL4031Vo)) {
						checkFlag = 1; // 1-已確認報表
					} else if (keyinCnt.get(tempL4031Vo) == totCnt.get(tempL4031Vo)) {
						checkFlag = 0; // 0-確認
					}
				}
				int adjCode = tempL4031Vo.getLableB() / 10;
				int keyinCode = tempL4031Vo.getLableB() % 10;
				// 1.批次自動調整 RPTFG = 5(只有目前&調後，無取消調整)
				// 2.按地區別調整 RPTFG = 6(只有目前&調後&上下限，無取消調整)
				// 3.人工調整(未調整) RPTFG = 3(全部欄位，可選擇4種調整方式) 若為機動利率調整則為7
				// 4.人工調整 (待輸入)RPTFG = 4(全部欄位，可取消調整)
				// 5.人工調整(已調整) RPTFG = 2(全部欄位，無取消調整)
				// A.全部 RPTFG = 4(全部欄位，可取消調整)

// TxKind           adjcode            KeyinCode          RptFg                                    
//                  0.全部                                2(無利率欄)

// 1.定期機動調整
// 3.機動利率調整                                                                                           
//                  1.批次自動調整                        5(目前&調後，無取消調整)   
//                  2.按地區別調整                                   
//                  3.人工調整                                   
//                                      0.未調整          7(有地區別，可選擇3種調整方式)  
//                                      1.已調整          4(無地區別，可取消調整)         
//                                      2.待輸入          4(全部欄位，無取消調整)

// 2.指數型利率調整
// 4.員工利率調整 
// 5.按商品別調整 7 機動利率調整 
//				    1.批次自動調整                                              5(只有目前&調後，無取消調整)   
//				             
//				    3.人工調整                                   
//				                        0.未調整                     3(無地區別，可選擇3種調整方式)  
//				                        1.已調整                     4(無地區別，可取消調整)         
//				                        2.待輸入                     4(全部欄位，無取消調整)

				int rptFg = 0;
				String lableBX = "";
				switch (adjCode) {
				case 0:
					rptFg = 2;
					break;
				case 1:
					lableBX = "批次自動調整";
					rptFg = 5;
					break;
				case 2:
					lableBX = "按地區別調整";
					break;
				case 3:
					lableBX = "人工調整";
					break;
				}
				if (adjCode == 2 || adjCode == 3) {
					switch (keyinCode) {
					case 0:
						lableBX += "(未調整)";
						if (tempL4031Vo.getLableA() == 1 || tempL4031Vo.getLableA() == 3) {
							rptFg = 3;
						} else {
							rptFg = 7;
						}
						break;
					case 1:
						lableBX += "(已調整)";
						rptFg = 4;
						break;
					case 2:
						lableBX += "(待輸入)";
						checkFlag = 2; // 2-輸入利率
						rptFg = 4;
						break;
					}
				}

				occursList.putParam("OORank", tempL4031Vo.getRank()); // 排序
				occursList.putParam("OOLableA", tempL4031Vo.getLableA()); // 作業項目
				occursList.putParam("OOLableB", tempL4031Vo.getLableB()); // 輸入記號
				occursList.putParam("OOLableBX", lableBX); // 註記
				occursList.putParam("OOSumCnt", sumCnt.get(tempL4031Vo));
				occursList.putParam("OOIgnCnt", ignCnt.get(tempL4031Vo));
				occursList.putParam("OOTotCnt", totCnt.get(tempL4031Vo));
				occursList.putParam("OOCheckFlag", checkFlag); // 檢核記號 0-確認 1-已確認報表 2-輸入利率
				occursList.putParam("OOStatus", status.get(tempL4031Vo)); // 作業項目狀態 0.未確認 1.確認未放行 2.已確認放行
				occursList.putParam("OORptFg", rptFg); // 作業項目狀態 0.未確認 1.確認未放行 2.已確認放行

				this.info("L4031 occursList : " + occursList.toString());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else

		{
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
			if (rank != other.rank)
				return false;
			return true;
		}

		private int lableA = 0;
		private int lableB = 0;
		private int rank = 0;

		public tmpBatx(int txKind, int AdjCode, int RateKeyInCode, int rank) {
			lableA = txKind;
			lableB = AdjCode * 10 + RateKeyInCode;
			this.setLableA(lableA);
			this.setLableB(lableB);
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
			} else if (this.rank - other.rank != 0) {
				return this.rank - other.rank;
			} else {
				return 0;
			}
		}

		@Override
		public String toString() {
			return "tmpBatx [lableA=" + lableA + ", lableB=" + lableB + ", rank=" + rank + "]";
		}

		private L4031 getEnclosingInstance() {
			return L4031.this;
		}

	}

	private void setCount(BatxRateChange tBatxRateChange, tmpBatx grp, int rank) {
		this.info("grp : " + grp.toString());
		if (!totCnt.containsKey(grp)) {
			totCnt.put(grp, 0);
		}
		if (rank == 1) {
			totCnt.put(grp, totCnt.get(grp) + 1);
		}

		if (!sumCnt.containsKey(grp)) {
			sumCnt.put(grp, 0);
		}

		if (tBatxRateChange.getRateKeyInCode() != 9) {
			sumCnt.put(grp, sumCnt.get(grp) + 1);
		}

		if (!ignCnt.containsKey(grp)) {
			ignCnt.put(grp, 0);
		}

		if (rank == 1) {
			if (tBatxRateChange.getRateKeyInCode() == 9) {
				ignCnt.put(grp, ignCnt.get(grp) + 1);
			}
		}

		if (!conCnt.containsKey(grp)) {
			conCnt.put(grp, 0);
		}
		if (rank == 1) {
			if (tBatxRateChange.getConfirmFlag() >= 1) {
				conCnt.put(grp, conCnt.get(grp) + 1);
			}
		}

		if (!keyinCnt.containsKey(grp)) {
			keyinCnt.put(grp, 0);
		}
		if (rank == 1) {
			if (tBatxRateChange.getRateKeyInCode() == 1) {
				keyinCnt.put(grp, keyinCnt.get(grp) + 1);
			}
		}

	}
}