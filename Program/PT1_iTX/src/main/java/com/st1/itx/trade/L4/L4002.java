package com.st1.itx.trade.L4;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/* Tita
ACCTDATE=9,7
TELLERID=X,6
END=X,1
*/

@Service("L4002")
@Scope("prototype")
public class L4002 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	private BigDecimal ooRpAmt = new BigDecimal("0");
	private int totalCnt = 0;
	private String batxStatus = "";
	int acDate;

//	private BigDecimal bgZero = new BigDecimal("0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4002 ");
		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();

		this.limit = Integer.MAX_VALUE;

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String procExeCode = titaVo.getParam("ProcExeCode");
//		計算總筆數，若0最後提示ERROR

//		2020/11/17 增加search條件 by BatxHead檔狀態
		Slice<BatxHead> sBatxHead = null;
		String tlrNo = titaVo.getParam("TlrNo");
		List<BatxHead> lBatxHead = new ArrayList<BatxHead>();

		sBatxHead = batxHeadService.acDateRange(acDate, acDate, this.index, this.limit, titaVo);

		lBatxHead = sBatxHead == null ? null : sBatxHead.getContent();

		// 未輸入經辦 or 經辦相同 or BS020
		if (lBatxHead != null && lBatxHead.size() != 0) {
			for (BatxHead tBatxHead : lBatxHead) {
				if ("".equals(tlrNo) || tBatxHead.getTitaTlrNo().equals(tlrNo)
						|| "BS020".equals(tBatxHead.getTitaTxCd())) {
				} else {
					continue;
				}
//				若整批處理中畫面顯示處理中，反之顯示該批作業狀態
				if ("1".equals(tBatxHead.getBatxStsCode())) {
					batxStatus = "9";
				} else {
					batxStatus = tBatxHead.getBatxExeCode();
				}

//				0.待處理 ->0123
//				9.已完成 ->ALL
				if ("0".equals(procExeCode)) {
					if ("0".equals(tBatxHead.getBatxExeCode()) || "1".equals(tBatxHead.getBatxExeCode())
							|| "2".equals(tBatxHead.getBatxExeCode()) || "3".equals(tBatxHead.getBatxExeCode())) {

						setL4002Tota(tBatxHead, titaVo);
					} else {
						this.info("continue... ，procExeCode = " + procExeCode + "， BatxHead狀態 != 0~3 ，狀態 = "
								+ tBatxHead.getBatxExeCode());
						continue;
					}
				} else {
					setL4002Tota(tBatxHead, titaVo);
				}
			}
			if (totalCnt == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		} else

		{
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setL4002Tota(BatxHead tBatxHead, TitaVo titaVo) throws LogicException {
		String batchNo = tBatxHead.getBatchNo();
		int labelRankFlag = 1;
		Slice<BatxDetail> sBatxDetail = batxDetailService.findL4002AEq(acDate, batchNo, this.index, this.limit, titaVo);

		List<BatxDetail> lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();
		if (lBatxDetail == null) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOBatchNo", batchNo);
			occursList.putParam("OORankFlag", 1);
			occursList.putParam("OOStatusCode", batxStatus);
			occursList.putParam("OORepayCode", 90);
			occursList.putParam("OOReconCode", "   ");
			occursList.putParam("OOFileName", tBatxHead.getTitaTxCd());
			occursList.putParam("OOFileCnt", 0);
			occursList.putParam("OODntCnt", 0);
			occursList.putParam("OOAlrCnt", 0);
			occursList.putParam("OOWatCnt", 0);
			occursList.putParam("OOVirCnt", 0);
			occursList.putParam("OOTotalRepayAmt", 0);
			occursList.putParam("OOToDoRepayAmt", 0);
			occursList.putParam("OOUnDoRepayAmt", 0);
			occursList.putParam("OOLabelFgA", "");
			occursList.putParam("OOLabelFgB", "");
			occursList.putParam("OOLabelFgC", "");

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		} else {
			totalCnt++;
			// 會計日期 整批批號 還款來源 對帳類別 檔名
			// AcDate BatchNo RepayCode ReconCode fileName
			// 先將一筆寫3筆
			// (Group1 : 批次合計-- 來源& 類別、檔案空白)
			// (Group2 : 類別合計-- 檔案空白 )
			// (Group3 : 檔案合計 )
			// 放入list

			// 總筆數合計
			HashMap<tmpBatx, Integer> totalCnt = new HashMap<>();
			// 總金額合計
			HashMap<tmpBatx, BigDecimal> totalAmtSum = new HashMap<>();
			// 需處理金額合計
			HashMap<tmpBatx, BigDecimal> toDoAmtSum = new HashMap<>();
			// 不處理金額合計
			HashMap<tmpBatx, BigDecimal> unDoAmtSum = new HashMap<>();
			// 資料筆數
			HashMap<tmpBatx, Integer> fileCnt = new HashMap<>();
			// 不處理筆數
			HashMap<tmpBatx, Integer> dntCnt = new HashMap<>();
			// 已處理筆數
			HashMap<tmpBatx, Integer> alrCnt = new HashMap<>();
			// 待處理筆數
			HashMap<tmpBatx, Integer> watCnt = new HashMap<>();
			// 轉暫收筆數
			HashMap<tmpBatx, Integer> virCnt = new HashMap<>();
			// 需檢核筆數
			HashMap<tmpBatx, Integer> canCheckCnt = new HashMap<>();
			// 可入帳筆數
			HashMap<tmpBatx, Integer> canEnterCnt = new HashMap<>();
			// 可暫收筆數
			HashMap<tmpBatx, Integer> canTempCnt = new HashMap<>();
			// 可訂正筆數
			HashMap<tmpBatx, Integer> canEraseCnt = new HashMap<>();

			for (BatxDetail tBatxDetail : lBatxDetail) {

				tmpBatx grp1 = new tmpBatx();

				tmpBatx grp2 = new tmpBatx();

				tmpBatx grp3 = new tmpBatx();

				switch (tBatxDetail.getRepayCode()) {
				case 1:
					grp1.setAcDate(tBatxDetail.getAcDate());
					grp1.setBatchNo(tBatxDetail.getBatchNo());
					grp1.setRepayCode(0);
					grp1.setReconCode(" ");
					grp1.setFileName(" ");
					grp1.setRankFlag(1);

					grp2.setAcDate(tBatxDetail.getAcDate());
					grp2.setBatchNo(tBatxDetail.getBatchNo());
					grp2.setRepayCode(tBatxDetail.getRepayCode());
					grp2.setReconCode(" ");
					grp2.setFileName(tBatxDetail.getFileName());
					grp2.setRankFlag(2);

					grp3.setAcDate(tBatxDetail.getAcDate());
					grp3.setBatchNo(tBatxDetail.getBatchNo());
					grp3.setRepayCode(tBatxDetail.getRepayCode());
					grp3.setReconCode(tBatxDetail.getReconCode());
					grp3.setFileName(" ");
					grp3.setRankFlag(3);
					labelRankFlag = 3;
					break;

				case 2:
				case 3:
					grp1.setAcDate(tBatxDetail.getAcDate());
					grp1.setBatchNo(tBatxDetail.getBatchNo());
					grp1.setRepayCode(0);
					grp1.setReconCode(" ");
					grp1.setFileName(" ");
					grp1.setRankFlag(1);

					grp2.setAcDate(tBatxDetail.getAcDate());
					grp2.setBatchNo(tBatxDetail.getBatchNo());
					grp2.setRepayCode(tBatxDetail.getRepayCode());
					grp2.setReconCode(tBatxDetail.getReconCode());
					grp2.setFileName(" ");
					grp2.setRankFlag(2);

					grp3.setAcDate(tBatxDetail.getAcDate());
					grp3.setBatchNo(tBatxDetail.getBatchNo());
					grp3.setRepayCode(tBatxDetail.getRepayCode());
					grp3.setReconCode(tBatxDetail.getReconCode());
					grp3.setFileName(tBatxDetail.getFileName());
					grp3.setRankFlag(3);
					break;

				case 4:
					grp1.setAcDate(tBatxDetail.getAcDate());
					grp1.setBatchNo(tBatxDetail.getBatchNo());
					grp1.setRepayCode(0);
					grp1.setReconCode(" ");
					grp1.setFileName(" ");
					grp1.setRankFlag(1);

					grp2.setAcDate(tBatxDetail.getAcDate());
					grp2.setBatchNo(tBatxDetail.getBatchNo());
					grp2.setRepayCode(tBatxDetail.getRepayCode());
					grp2.setReconCode(tBatxDetail.getReconCode());
					grp2.setFileName(tBatxDetail.getFileName());
					grp2.setRankFlag(2);
					break;
				default:
					grp1.setAcDate(tBatxDetail.getAcDate());
					grp1.setBatchNo(tBatxDetail.getBatchNo());
					grp1.setRepayCode(0);
					grp1.setReconCode(" ");
					grp1.setFileName(" ");
					grp1.setRankFlag(1);
					grp2.setAcDate(tBatxDetail.getAcDate());
					grp2.setBatchNo(tBatxDetail.getBatchNo());
					grp2.setRepayCode(tBatxDetail.getRepayCode());
					grp2.setReconCode(tBatxDetail.getReconCode());
					grp2.setFileName(tBatxDetail.getFileName());
					grp2.setRankFlag(2);
					break;
				}
				ooRpAmt = tBatxDetail.getRepayAmt();
				this.info("L4002  - ooRpAmt : " + ooRpAmt);

				this.info("L4002  - grp1 : " + grp1.toString());
				this.info("L4002  - grp2 : " + grp2.toString());
				this.info("L4002  - grp3 : " + grp3.toString());

// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.虛擬轉暫收
				// grp1 總筆數合計
				if (totalCnt.containsKey(grp1)) {
					totalCnt.put(grp1, totalCnt.get(grp1) + 1);
				} else {
					totalCnt.put(grp1, 1);
				}
				// grp2 總筆數合計
				if (totalCnt.containsKey(grp2)) {
					totalCnt.put(grp2, totalCnt.get(grp2) + 1);
				} else {
					totalCnt.put(grp2, 1);
				}
				// grp3總筆數合計
				if (grp3.getBatchNo() != null && !grp3.getBatchNo().isEmpty()) {
					if (totalCnt.containsKey(grp3)) {
						totalCnt.put(grp3, totalCnt.get(grp3) + 1);
					} else {
						totalCnt.put(grp3, 1);
					}
				}
				// grp2 總筆數計算
				if (fileCnt.containsKey(grp2)) {
					fileCnt.put(grp2, fileCnt.get(grp2) + 1);
				} else {
					fileCnt.put(grp2, 1);
				}
				// grp2不處理
				if (tBatxDetail.getProcStsCode().equals("1")) {
					if (dntCnt.containsKey(grp2)) {
						dntCnt.put(grp2, dntCnt.get(grp2) + 1);
					} else {
						dntCnt.put(grp2, 1);
					}
				}
				// grp2已處理
				if (tBatxDetail.getProcStsCode().equals("5") || tBatxDetail.getProcStsCode().equals("6")) {
					if (alrCnt.containsKey(grp2)) {
						alrCnt.put(grp2, alrCnt.get(grp2) + 1);
					} else {
						alrCnt.put(grp2, 1);
					}
				}
				// grp2待處理
				if (tBatxDetail.getProcStsCode().equals("0") || tBatxDetail.getProcStsCode().equals("2")
						|| tBatxDetail.getProcStsCode().equals("3") || tBatxDetail.getProcStsCode().equals("4")) {
					if (watCnt.containsKey(grp2)) {
						watCnt.put(grp2, watCnt.get(grp2) + 1);
					} else {
						watCnt.put(grp2, 1);
					}
				}
				// grp2轉暫收
				if (tBatxDetail.getProcStsCode().equals("7")) {
					if (virCnt.containsKey(grp2)) {
						virCnt.put(grp2, virCnt.get(grp2) + 1);
					} else {
						virCnt.put(grp2, 1);
					}
				}

				// grp3 總筆數計算
				if (grp3.getBatchNo() != null && !grp3.getBatchNo().isEmpty()) {
					if (fileCnt.containsKey(grp3)) {
						fileCnt.put(grp3, fileCnt.get(grp3) + 1);
					} else {
						fileCnt.put(grp3, 1);
					}
					// grp3不處理
					if (tBatxDetail.getProcStsCode().equals("1")) {
						if (dntCnt.containsKey(grp3)) {
							dntCnt.put(grp3, dntCnt.get(grp3) + 1);
						} else {
							dntCnt.put(grp3, 1);
						}
					}
					// grp3已處理
					if (tBatxDetail.getProcStsCode().equals("5") || tBatxDetail.getProcStsCode().equals("6")) {
						if (alrCnt.containsKey(grp3)) {
							alrCnt.put(grp3, alrCnt.get(grp3) + 1);
						} else {
							alrCnt.put(grp3, 1);
						}
					}
					// grp3待處理
					if (tBatxDetail.getProcStsCode().equals("0") || tBatxDetail.getProcStsCode().equals("2")
							|| tBatxDetail.getProcStsCode().equals("3") || tBatxDetail.getProcStsCode().equals("4")) {
						if (watCnt.containsKey(grp3)) {
							watCnt.put(grp3, watCnt.get(grp3) + 1);
						} else {
							watCnt.put(grp3, 1);
						}
					}
					// grp3轉暫收
					if (tBatxDetail.getProcStsCode().equals("7")) {
						if (virCnt.containsKey(grp3)) {
							virCnt.put(grp3, virCnt.get(grp3) + 1);
						} else {
							virCnt.put(grp3, 1);
						}
					}
				}
				// grp2 總金額計算
				if (totalAmtSum.containsKey(grp2)) {
					totalAmtSum.put(grp2, ooRpAmt.add(totalAmtSum.get(grp2)));
				} else {
					totalAmtSum.put(grp2, ooRpAmt);
				}

				// grp3 總金額計算
				if (grp3.getBatchNo() != null && !grp3.getBatchNo().isEmpty()) {
					if (totalAmtSum.containsKey(grp3)) {
						totalAmtSum.put(grp3, ooRpAmt.add(totalAmtSum.get(grp3)));
					} else {
						totalAmtSum.put(grp3, ooRpAmt);
					}
				}
				// 需處理金額計算
				if (!"1".equals(tBatxDetail.getProcStsCode())) {
					// grp2 需處理金額計算
					if (toDoAmtSum.containsKey(grp2)) {
						toDoAmtSum.put(grp2, ooRpAmt.add(toDoAmtSum.get(grp2)));
					} else {
						toDoAmtSum.put(grp2, ooRpAmt);
					}

					// grp3 需處理金額計算
					if (grp3.getBatchNo() != null && !grp3.getBatchNo().isEmpty()) {
						if (toDoAmtSum.containsKey(grp3)) {
							toDoAmtSum.put(grp3, ooRpAmt.add(toDoAmtSum.get(grp3)));
						} else {
							toDoAmtSum.put(grp3, ooRpAmt);
						}
					}
				}

				// 不處理金額計算
				if ("1".equals(tBatxDetail.getProcStsCode())) {
					// grp2 不處理金額計算
					if (unDoAmtSum.containsKey(grp2)) {
						unDoAmtSum.put(grp2, ooRpAmt.add(unDoAmtSum.get(grp2)));
					} else {
						unDoAmtSum.put(grp2, ooRpAmt);
					}

					// grp3 不處理金額計算
					if (grp3.getBatchNo() != null && !grp3.getBatchNo().isEmpty()) {
						if (unDoAmtSum.containsKey(grp3)) {
							unDoAmtSum.put(grp3, ooRpAmt.add(unDoAmtSum.get(grp3)));
						} else {
							unDoAmtSum.put(grp3, ooRpAmt);
						}
					}
				}

				// 可訂正筆數
				if (tBatxDetail.getProcStsCode().equals("5") || tBatxDetail.getProcStsCode().equals("6")
						|| tBatxDetail.getProcStsCode().equals("7")) {
					if (canEraseCnt.containsKey(grp1)) {
						canEraseCnt.put(grp1, canEraseCnt.get(grp1) + 1);
					} else {
						canEraseCnt.put(grp1, 1);
					}
				}

				if (labelRankFlag == 1) {
					// 可檢核筆數
					if (tBatxDetail.getProcStsCode().equals("0") || tBatxDetail.getProcStsCode().equals("3")) {
						if (canCheckCnt.containsKey(grp1)) {
							canCheckCnt.put(grp1, canCheckCnt.get(grp1) + 1);
						} else {
							canCheckCnt.put(grp1, 1);
						}
					}
					// 可入帳筆數
					if (tBatxDetail.getProcStsCode().equals("2") || tBatxDetail.getProcStsCode().equals("4")) {
						if (canEnterCnt.containsKey(grp1)) {
							canEnterCnt.put(grp1, canEnterCnt.get(grp1) + 1);
						} else {
							canEnterCnt.put(grp1, 1);
						}
						// 可暫收筆數
						if (tBatxDetail.getProcStsCode().equals("2") || tBatxDetail.getProcStsCode().equals("3")
								|| tBatxDetail.getProcStsCode().equals("4")) {
							if (canTempCnt.containsKey(grp3)) {
								canTempCnt.put(grp3, canTempCnt.get(grp3) + 1);
							} else {
								canTempCnt.put(grp3, 1);
							}
						}
					}
				} else {
					// 可訂正筆數
					if (tBatxDetail.getProcStsCode().equals("5") || tBatxDetail.getProcStsCode().equals("6")
							|| tBatxDetail.getProcStsCode().equals("7")) {
						if (canEraseCnt.containsKey(grp3)) {
							canEraseCnt.put(grp3, canEraseCnt.get(grp3) + 1);
						} else {
							canEraseCnt.put(grp3, 1);
						}
					}
					// 可檢核筆數
					if (tBatxDetail.getProcStsCode().equals("0") || tBatxDetail.getProcStsCode().equals("3")) {
						if (canCheckCnt.containsKey(grp3)) {
							canCheckCnt.put(grp3, canCheckCnt.get(grp3) + 1);
						} else {
							canCheckCnt.put(grp3, 1);
						}
					}
					// 可入帳筆數
					if (tBatxDetail.getProcStsCode().equals("2") || tBatxDetail.getProcStsCode().equals("4")) {
						if (canEnterCnt.containsKey(grp3)) {
							canEnterCnt.put(grp3, canEnterCnt.get(grp3) + 1);
						} else {
							canEnterCnt.put(grp3, 1);
						}
					}
					// 可暫收筆數
					if (tBatxDetail.getProcStsCode().equals("2") || tBatxDetail.getProcStsCode().equals("3")
							|| tBatxDetail.getProcStsCode().equals("4")) {
						if (canTempCnt.containsKey(grp3)) {
							canTempCnt.put(grp3, canTempCnt.get(grp3) + 1);
						} else {
							canTempCnt.put(grp3, 1);
						}
					}
				}

			}

			Set<tmpBatx> tempSet = totalCnt.keySet();

			List<tmpBatx> tempList = new ArrayList<>();

			for (Iterator<tmpBatx> it = tempSet.iterator(); it.hasNext();) {
				tmpBatx tmpBatxVo = it.next();
				tempList.add(tmpBatxVo);
			}

			tempList.sort((c1, c2) -> {
				return c1.compareTo(c2);
			});
			this.info("L4002 tempList size = " + tempList.size());

			for (tmpBatx tempL4002Vo : tempList) {

				this.info("!!!! L4002 OOFileName :" + tempL4002Vo.getFileName());

				this.info("!!!! L4002 tempL4002Vo :" + tempL4002Vo.toString());

				OccursList occursList = new OccursList();
				occursList.putParam("OOBatchNo", tempL4002Vo.getBatchNo());
				occursList.putParam("OORankFlag", tempL4002Vo.getRankFlag());
				occursList.putParam("OOStatusCode", batxStatus);
				occursList.putParam("OORepayCode", tempL4002Vo.getRepayCode());
				occursList.putParam("OOReconCode", tempL4002Vo.getReconCode());
				occursList.putParam("OOFileName", tempL4002Vo.getFileName());
				occursList.putParam("OOFileCnt", fileCnt.get(tempL4002Vo));
				occursList.putParam("OODntCnt", dntCnt.get(tempL4002Vo)); // 失敗
				occursList.putParam("OOAlrCnt", alrCnt.get(tempL4002Vo)); // 已入帳
				occursList.putParam("OOWatCnt", watCnt.get(tempL4002Vo)); // 待處理
				occursList.putParam("OOVirCnt", virCnt.get(tempL4002Vo)); // 轉暫收
				occursList.putParam("OOTotalRepayAmt", totalAmtSum.get(tempL4002Vo));
				occursList.putParam("OOToDoRepayAmt", toDoAmtSum.get(tempL4002Vo));
				occursList.putParam("OOUnDoRepayAmt", unDoAmtSum.get(tempL4002Vo));
// LabelFgA 整批刪除(D)、刪除回復(R)、整批訂正(H)
// LabelFgB             整批檢核(C)、整批入帳(E)
// LabelFgC 轉暫收(T) 待處理筆數 - 需檢核筆數 > 0

				String labelFgA = "";
				String labelFgB = "";
				String labelFgC = "";
				if (tempL4002Vo.getRankFlag() == 1) {
					if ("8".equals(batxStatus)) {
						labelFgA = "R";
					} else {
						if (canEraseCnt.get(tempL4002Vo) == null || canEraseCnt.get(tempL4002Vo) == 0) {
							labelFgA = "D";
						}
					}
				}
				if (!"8".equals(batxStatus) && labelRankFlag == tempL4002Vo.getRankFlag()) {
					if (canEraseCnt.get(tempL4002Vo) != null && canEraseCnt.get(tempL4002Vo) > 0) {
						labelFgA = "H";
					}
				}
				if (!"8".equals(batxStatus) && labelRankFlag == tempL4002Vo.getRankFlag()) {
					if (canCheckCnt.get(tempL4002Vo) != null && canCheckCnt.get(tempL4002Vo) > 0) {
						labelFgB = "C";
					} else {
						if (canEnterCnt.get(tempL4002Vo) != null && canEnterCnt.get(tempL4002Vo) > 0) {
							labelFgB = "E";
						}
					}
				}
				if (!"8".equals(batxStatus) && labelRankFlag == tempL4002Vo.getRankFlag()) {
					if (canTempCnt.get(tempL4002Vo) != null && canTempCnt.get(tempL4002Vo) > 0) {
						labelFgC = "T";
					}
				}
				if (acDate != titaVo.getEntDyI() + 19110000) {
					occursList.putParam("OOLabelFgA", "");
					occursList.putParam("OOLabelFgB", "");
					occursList.putParam("OOLabelFgC", "");
				} else {
					occursList.putParam("OOLabelFgA", labelFgA);
					occursList.putParam("OOLabelFgB", labelFgB);
					occursList.putParam("OOLabelFgC", labelFgC);
				}
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}
	}
}

class tmpBatx implements Comparable<tmpBatx> {

	private int acDate = 0;
	private String batchNo = "";
	private int repayCode = 0;
	private String reconCode = "";
	private String fileName = "";
	private int rankFlag = 0;

//	public tmpBatx(int acDate, String batchNo, int repayCode, String reconCode, String fileName, int rankFlag) {
//		this.acDate = acDate;
//		this.batchNo = batchNo;
//		this.repayCode = repayCode;
//		this.reconCode = reconCode;
//		this.fileName = fileName;
//		this.rankFlag = rankFlag;
//	}

	@Override
	public String toString() {
		return "tmpBatx [acDate=" + acDate + ", batchNo=" + batchNo + ", repayCode=" + repayCode + ", reconCode="
				+ reconCode + ", fileName=" + fileName + ", rankFlag=" + rankFlag + "]";
	}

	@Override
	public int compareTo(tmpBatx other) {
		if (this.acDate - other.acDate != 0) {
			return this.acDate - other.acDate;
		} else if (this.batchNo.compareTo(other.batchNo) != 0) {
			return this.batchNo.compareTo(other.batchNo);
		} else if (this.repayCode - other.repayCode != 0) {
			return this.repayCode - other.repayCode;
		} else if (this.reconCode.compareTo(other.reconCode) != 0) {
			return this.reconCode.compareTo(other.reconCode);
		} else if (this.fileName.compareTo(other.fileName) != 0) {
			return this.fileName.compareTo(other.fileName);
		} else {
			return 0;
		}
	}

	public int getAcDate() {
		return acDate;
	}

	public void setAcDate(int acDate) {
		this.acDate = acDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int getRepayCode() {
		return repayCode;
	}

	public void setRepayCode(int repayCode) {
		this.repayCode = repayCode;
	}

	public String getReconCode() {
		return reconCode;
	}

	public void setReconCode(String reconCode) {
		this.reconCode = reconCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getRankFlag() {
		return rankFlag;
	}

	public void setRankFlag(int rankFlag) {
		this.rankFlag = rankFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acDate;
		result = prime * result + ((batchNo == null) ? 0 : batchNo.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + rankFlag;
		result = prime * result + ((reconCode == null) ? 0 : reconCode.hashCode());
		result = prime * result + repayCode;
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
		if (acDate != other.acDate)
			return false;
		if (batchNo == null) {
			if (other.batchNo != null)
				return false;
		} else if (!batchNo.equals(other.batchNo))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (rankFlag != other.rankFlag)
			return false;
		if (reconCode == null) {
			if (other.reconCode != null)
				return false;
		} else if (!reconCode.equals(other.reconCode))
			return false;
		if (repayCode != other.repayCode)
			return false;
		return true;
	}
}