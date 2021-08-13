package com.st1.itx.trade.BS;

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
import com.st1.itx.util.common.data.BS020Vo;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

@Service("BS020")
@Scope("prototype")
/**
 * 新增整批入帳明細－暫收抵繳期款<br>
 * 執行時機：日始作業，系統換日後(BS001執行後)自動執行<br>
 * 1.保留成功的整批入帳明細，其餘刪除(程式可重複執行)<br>
 * 2.找正常戶、應繳日<=本日，且額度下有暫收可抵繳之戶號、額度<br>
 * 3.進行還款試算(戶號、額度)，若暫收可抵繳>= 期金(含費用)，則寫入整批入帳檔<br>
 * 
 * @author w.y.Lai
 * @version 1.0.0
 */
public class BS020 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS020.class);

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
	private BatxHead tBatxHead;
	private BatxHeadId tBatxHeadId;
	private BatxDetail tBatxDetail;
	private List<BatxDetail> lBatxDetail;
	private BatxDetailId tBatxDetailId;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS020 ");

		baTxCom.setTxBuffer(this.getTxBuffer());

		this.tbsdyf = this.getTxBuffer().getMgBizDate().getTbsDyf();
		tbsdy = this.getTxBuffer().getMgBizDate().getTbsDy();

		// step 1. get BatchNo
		this.batchNo = null;
		this.detailSeq = 0;
		settingBatchNo(titaVo);

		// step 2. find BatxDetail list
		this.lBatxDetail = new ArrayList<BatxDetail>();
		findList(titaVo);

		// step 3.insert BatxDetail
		if (this.lBatxDetail != null && this.lBatxDetail.size() > 0) {
			try {
				batxDetailService.insertAll(lBatxDetail);
			} catch (DBException e) {
				throw new LogicException("E0005", "L4210 BatxDetail insertAll : " + e.getErrorMsg()); // E0005
																										// 新增資料時，發生錯誤
			}
		}

		// step 4.insert/update BatxHead

		if (this.lBatxDetail != null && this.lBatxDetail.size() > 0)
			updateBatxHead(titaVo);

		// end
		this.batchTransaction.commit();
		return null;
	}

	/* 設定暫收抵繳款批號 */
	private void settingBatchNo(TitaVo titaVo) throws LogicException {

		tBatxHead = batxHeadService.titaTxCdFirst(tbsdyf, "BS020", "8"); // <> 8-已刪除

		// 保留成功的整批入帳明細，其餘刪除(程式可重複執行)
		// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.虛擬轉暫收
		if (tBatxHead != null) {
			this.batchNo = tBatxHead.getBatchNo();
			List<String> dStatusCode = new ArrayList<String>();
			dStatusCode.add("0");
			dStatusCode.add("1");
			dStatusCode.add("2");
			dStatusCode.add("3");
			dStatusCode.add("4");
			Slice<BatxDetail> slBatxDetail = batxDetailService.findL4920HEq(this.tbsdyf, this.batchNo, dStatusCode, this.index, Integer.MAX_VALUE);
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
				this.batchNo = "BATX" + parse.IntegerToString(parse.stringToInteger(tBatxHead.getBatchNo().substring(4)) + 1, 2);
		}
	}

	private void findList(TitaVo titaVo) throws LogicException {
		// 找正常戶、應繳日<=本日，且額度下有暫收可抵繳之戶號、額度
		List<BS020Vo> BS020VoList = null;
		try {
//			BS020VoList = bS020ServiceImpl.find(0, this.tbsdyf, 01); // 下次應繳日起日、下次應繳日止日、繳款方式 :01.匯款轉帳
			BS020VoList = bS020ServiceImpl.find(0, this.tbsdyf, 00); // 下次應繳日起日、下次應繳日止日、繳款方式 :00.all
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("BS020List=" + BS020VoList.toString());

		int custNo = 0;
		int facmNo = 0;
		// 進行還款試算(戶號、額度)，若暫收可抵繳>= 期金(含費用)，則寫入整批入帳檔
		if (BS020VoList != null && BS020VoList.size() > 0) {
			for (BS020Vo bs : BS020VoList) {
				// by 額度處理
				if (bs.getCustNo() != custNo || bs.getFacmNo() != facmNo) {
//EntryDate 入帳日, CustNo, FacmNo , BormNo, RepayType 還款類別, TxAmt 回收金額
					custNo = bs.getCustNo();
					facmNo = bs.getFacmNo();
					ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();
					listBaTxVo = baTxCom.settingUnPaid(tbsdy, custNo, facmNo, 0, 1, BigDecimal.ZERO, titaVo);
					boolean isTermPay = false;
					boolean isShortAmt = false;

//dataKind = 0; // 資料類型
//1.應收費用+未收費用+短繳期金
//2.本金利息	
//3.暫收抵繳
//4.溢(C)短(D)繳
//5.其他額度暫收可抵繳
					if (listBaTxVo != null && listBaTxVo.size() != 0) {
						for (BaTxVo ba : listBaTxVo) {
							if (ba.getDataKind() == 2)
								isTermPay = true;
							if (ba.getDataKind() == 4) {
								if ("D".equals(ba.getDbCr()) && ba.getUnPaidAmt().compareTo(BigDecimal.ZERO) > 0)
									isShortAmt = true;
							}
						}
					}
					if (isTermPay && !isShortAmt)
						addDetail(custNo, facmNo, titaVo);
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
		}
		tBatxHead.setBatxTotAmt(BigDecimal.ZERO);
		tBatxHead.setBatxTotCnt(this.lBatxDetail.size());
		tBatxHead.setBatxExeCode("0");
		tBatxHead.setBatxStsCode("0");
		tBatxHead.setTitaTlrNo(titaVo.getTlrNo());
		tBatxHead.setTitaTxCd("BS020");
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

	private void addDetail(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
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
		tBatxDetail.setFacmNo(facmNo);
		tBatxDetail.setEntryDate(this.tbsdy);
		tBatxDetail.setFileName("BS020");
		tBatxDetail.setReconCode("   ");
		tBatxDetail.setRepayType(01); // 01.期款
		tBatxDetail.setRepayAmt(BigDecimal.ZERO);
		tBatxDetail.setProcStsCode("0");
		tBatxDetail.setProcCode("00000");
		TempVo tTempVo = new TempVo();
		tTempVo.putParam("Note", "暫收抵繳期款");
		tBatxDetail.setProcNote(tTempVo.getJsonString());

		lBatxDetail.add(tBatxDetail);
	}

}