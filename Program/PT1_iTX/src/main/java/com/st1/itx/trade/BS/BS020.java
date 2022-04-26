package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.springjpa.cm.BS020ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

@Service("BS020")
@Scope("prototype")
/**
 * 新增整批入帳明細－暫收抵繳<br>
 * 執行時機：日始作業，系統換日後(BS001執行後)自動執行，L4450-產出銀行扣帳檔<br>
 * 1.保留成功的整批入帳明細，其餘刪除(程式可重複執行)<br>
 * 2.找正常戶、應繳日<=本日，且額度下有暫收可抵繳之戶號、額度<br>
 * 3.進行還款試算(戶號、額度)，若暫收可抵繳>= 期金 或 費用，則寫入整批入帳檔<br>
 * 
 * @author w.y.Lai
 * @version 1.0.0
 */
public class BS020 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BS020ServiceImpl bS020ServiceImpl;

	@Autowired
	public BaTxCom baTxCom;

	private int tbsdyf;
	private int tbsdy;
	private String batchNo;
	private int detailSeq;
	private String txCode;
	private BatxHead tBatxHead;
	private BatxHeadId tBatxHeadId;
	private BatxDetail tBatxDetail;
	private List<BatxDetail> lBatxDetail;
	private BatxDetailId tBatxDetailId;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS020 ");

		exec(titaVo, this.txBuffer);

		this.batchTransaction.commit();

		return null;
	}

	/* 設定暫收抵繳款批號 */
	public BatxHead exec(TitaVo titaVo, TxBuffer txBuffer) throws LogicException {
		txCode = "L4450".equals(titaVo.getTxcd()) ? "L4450" : "BS020";
		baTxCom.setTxBuffer(txBuffer);

		this.tbsdyf = txBuffer.getMgBizDate().getTbsDyf();
		tbsdy = txBuffer.getMgBizDate().getTbsDy();

		// step 1. get BatchNo
		this.batchNo = null;
		this.detailSeq = 0;
		settingBatchNo(titaVo);

		// step 2. find BatxDetail list
		this.lBatxDetail = new ArrayList<BatxDetail>();
		findList(titaVo);

		// step 3.insert BatxDetail
		if (this.lBatxDetail.size() > 0) {
			try {
				batxDetailService.insertAll(lBatxDetail);
			} catch (DBException e) {
				throw new LogicException("E0005", "BS020 insertAll : " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}

		// step 4.insert/update BatxHead
		updateBatxHead(titaVo);
		return tBatxHead;

	}

	/* 設定暫收抵繳款批號 */
	private void settingBatchNo(TitaVo titaVo) throws LogicException {
		// call by BS020-日始作業, or L4450-產出銀行扣帳檔
		tBatxHead = batxHeadService.titaTxCdFirst(tbsdyf, txCode, "8"); // <> 8-已刪除
		// 保留成功的整批入帳明細，其餘刪除(程式可重複執行)
		// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.轉暫收
		if (tBatxHead != null) {
			this.batchNo = tBatxHead.getBatchNo();
			List<String> dStatusCode = new ArrayList<String>();
			dStatusCode.add("0");
			dStatusCode.add("1");
			dStatusCode.add("2");
			dStatusCode.add("3");
			dStatusCode.add("4");
			Slice<BatxDetail> slBatxDetail = batxDetailService.findL4930BAEq(this.tbsdyf, this.batchNo, dStatusCode,
					this.index, Integer.MAX_VALUE);
			lBatxDetail = slBatxDetail == null ? null : slBatxDetail.getContent();
			if (lBatxDetail != null) {
				detailSeq = lBatxDetail.size();
				this.info("settingBatchNo delete size =" + lBatxDetail.size());
				try {
					batxDetailService.deleteAll(lBatxDetail);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "BatxDetail deleteAll " + e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			}
		}
		// 批號為"BATX" + NN(01起，續編)
		if (this.batchNo == null) {
			tBatxHead = batxHeadService.batchNoFirst(tbsdyf);
			if (tBatxHead == null)
				this.batchNo = "BATX01";
			else
				this.batchNo = "BATX"
						+ parse.IntegerToString(parse.stringToInteger(tBatxHead.getBatchNo().substring(4)) + 1, 2);
		}
	}

	private void findList(TitaVo titaVo) throws LogicException {
		// 找正常戶、應繳日<=本日，且額度下有暫收可抵繳之戶號、額度

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = bS020ServiceImpl.findAll(titaVo, tbsdy + 19110000);
		} catch (Exception e) {
			this.error("bS020ServiceImpl " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		if (resultList == null || resultList.size() == 0) {
			return;
		}
//		 F0   戶號
//		 F1   暫收可抵繳金額
//		 F2   還款類別 1.期款 2.費用
//		 F3   還款來源 02.銀行扣款 00.其他

		// 進行還款試算(戶號、額度)，若暫收可抵繳>= 期金(含費用)，則寫入整批入帳檔
		for (Map<String, String> result : resultList) {
			// 期款款試算
			ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();
			int custNo = parse.stringToInteger(result.get("F0"));
			int repayType = parse.stringToInteger(result.get("F2"));
			int repayCode = parse.stringToInteger(result.get("F3"));
			// 銀扣檔產生只處理還款來源 02.銀行扣款
			if ("L4450".equals(txCode) && repayCode == 0) {
				continue;
			}
			TempVo tTempVo = new TempVo();
			tTempVo.putParam("Note", repayType == 01 ? "暫收抵繳期款" : "暫收抵繳費用");
			try {
				listBaTxVo = baTxCom.settleUnPaid(tbsdy, 0, custNo, 0, 0, repayCode, repayType, BigDecimal.ZERO,
						tTempVo, titaVo);
			} catch (LogicException e) {
				this.info("baTxCom.settingUnPaid" + e.getMessage());
				continue;
			}
			boolean isTermPay = false;
			boolean isRecvPay = false;
			// dataKind = 0; // 資料類型
			// 1.應收費用+未收費用+短繳期金
			// 2.本金利息
			// 3.暫收抵繳
			// 4.溢(C)短(D)繳
			// 5.其他額度暫收可抵繳
			// 暫收抵繳
			if (listBaTxVo != null && listBaTxVo.size() != 0) {
				for (BaTxVo ba : listBaTxVo) {
					// 累溢收 > 費用
					if (ba.getRepayType() >= 4 && baTxCom.getExcessive().compareTo(ba.getUnPaidAmt()) >= 0) {
						isRecvPay = true;
					}
					// 期款
					if (ba.getDataKind() == 2 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
						isTermPay = true;
					}
					// 短繳
					if (ba.getDataKind() == 4) {
						if ("D".equals(ba.getDbCr()) && ba.getUnPaidAmt().compareTo(BigDecimal.ZERO) > 0)
							isTermPay = false;
					}
				}
			}
			if (isTermPay) {
				addDetail(custNo, 1, tTempVo, titaVo);
			} else {
				if (isRecvPay) {
					addDetail(custNo, 9, tTempVo, titaVo); // 其他
				}
			}
		}
	}

	private void updateBatxHead(TitaVo titaVo) throws LogicException {
		boolean insertfg = false;
		tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(this.tbsdyf);
		tBatxHeadId.setBatchNo(this.batchNo);
		tBatxHead = batxHeadService.holdById(tBatxHeadId);
		if (tBatxHead == null) {
			insertfg = true;
			tBatxHead = new BatxHead();
			tBatxHead.setBatxHeadId(tBatxHeadId);
			tBatxHead.setAcDate(this.tbsdyf);
			tBatxHead.setBatchNo(this.batchNo);
		}
		tBatxHead.setBatxTotAmt(BigDecimal.ZERO);
		tBatxHead.setBatxTotCnt(this.lBatxDetail.size());
		tBatxHead.setBatxExeCode(this.lBatxDetail.size() > 0 ? "0" : "4");
		tBatxHead.setBatxStsCode("0");
		tBatxHead.setTitaTlrNo(titaVo.getTlrNo());
		tBatxHead.setTitaTxCd(txCode);
		if (insertfg)
			try {
				batxHeadService.insert(tBatxHead);
			} catch (DBException e) {
				throw new LogicException("E0005", "L4210 BatxHead insert : " + e.getErrorMsg()); // E0005 新增資料時，發生錯誤
			}
		else
			try {
				batxHeadService.update(tBatxHead);
			} catch (DBException e) {
				throw new LogicException("E0007", "L4210 BatxHead Update : " + e.getErrorMsg()); // E0007 更新資料時，發生錯誤

			}

	}

	private void addDetail(int custNo, int repayType,TempVo tTempVo, TitaVo titaVo) throws LogicException {
		tBatxDetail = new BatxDetail();
		tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(this.tbsdy);
		tBatxDetailId.setBatchNo(this.batchNo);
		this.detailSeq++;
		tBatxDetailId.setDetailSeq(detailSeq);
		this.info("tBatxDetailId " + tBatxDetailId);

		tBatxDetail.setBatxDetailId(tBatxDetailId);
		tBatxDetail.setRepayCode(90); // 暫收抵繳
		tBatxDetail.setCustNo(custNo);
		tBatxDetail.setFacmNo(0);
		tBatxDetail.setEntryDate(this.tbsdy);
		tBatxDetail.setFileName(txCode);
		tBatxDetail.setReconCode("   ");
		tBatxDetail.setRepayType(repayType);
		tBatxDetail.setRepayAmt(BigDecimal.ZERO);
		tBatxDetail.setProcStsCode("0");
		tBatxDetail.setProcCode("00000");
		tBatxDetail.setTitaTlrNo("");
		tBatxDetail.setTitaTxtNo("");

		tBatxDetail.setProcNote(tTempVo.getJsonString());

		lBatxDetail.add(tBatxDetail);
	}

}