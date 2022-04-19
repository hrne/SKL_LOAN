package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L618D")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L618D extends TradeBuffer {

	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcDetailCom acDetailCom;

	@Autowired
	public WebClient webClient;
	@Autowired
	public TxTempService txTempService;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private List<TxTemp> lTxTemp;
	private String iItemCode;
	private String iTxDtlValue;
	private int iAcdate;
	private int iSlipBatNo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L618D ");
		this.totaVo.init(titaVo);
		txToDoCom.setTxBuffer(this.getTxBuffer());

		// 提存項目
		iItemCode = titaVo.getParam("TxItemCode");
		iTxDtlValue = titaVo.getParam("TxDtlValue");
		// 會計日期
		iAcdate = parse.stringToInteger(titaVo.getParam("TxAcDate"));
		iSlipBatNo = parse.stringToInteger(titaVo.getParam("TxSlipBatNo"));
//		boolean isSendMsg = false;
		List<AcDetail> acDetailList = new ArrayList<AcDetail>();
		AcDetail acDetail = new AcDetail();
		TempVo tTempVo = new TempVo();
		TempVo t2TempVo = new TempVo();

		if (titaVo.isHcodeNormal()) {
			String txtNo = "";
			if (parse.stringToInteger(titaVo.get("selectIndex")) == 1) {
				setTxTemp(txtNo, titaVo);
			} else {
				if (titaVo.getSelectReturnOK() == 0) {
					setTxTemp(txtNo, titaVo);
				} else {
					TxTemp t2TxTemp = txTempService.txtNoLastFirst(titaVo.getTlrNo(), titaVo);
					if (t2TxTemp != null) {
						txtNo = t2TxTemp.getTxtNo();
					}
					setTxTemp(txtNo, titaVo);
				}
			}
		}

		TxToDoDetail tTxToDoDetail = txToDoDetailService.findById(new TxToDoDetailId(iItemCode, 0, 0, 0, iTxDtlValue),
				titaVo);
		if (tTxToDoDetail == null) {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料
		}
		// update應處理清單
		txToDoCom.updDetailStatus(2, tTxToDoDetail.getTxToDoDetailId(), titaVo);

//		if (titaVo.isHcodeNormal()) {
//			Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange(iItemCode, 0, 0, this.index,
//					Integer.MAX_VALUE, titaVo);
//			if (slTxToDoDetail == null) {
//				isSendMsg = true;
//			}
//		}

		if (titaVo.isHcodeNormal()) {
			tTempVo = tTempVo.getVo(tTxToDoDetail.getProcessNote());
			// 相同會計日
			if (tTempVo.getParam("AcDate").equals(titaVo.getParam("TxAcDate"))) {
				// update應處理清單
				txToDoCom.updDetailStatus(2, tTxToDoDetail.getTxToDoDetailId(), titaVo);
				/* 借 */
				for (int i = 1; i <= 5; i++) {
					if (tTempVo.get("DbAcctCode" + i) != null) {
						this.info("DbAcctCode = " + tTempVo.get("DbAcctCode" + i));
						acDetail = new AcDetail();
						acDetail.setAcDate(iAcdate); // 會計日期
						acDetail.setSlipBatNo(parse.stringToInteger(tTempVo.getParam("SlipBatNo"))); // 傳票批號
						acDetail.setDbCr("D"); // 借貸別
						acDetail.setAcctCode(tTempVo.get("DbAcctCode" + i)); // 業務科目
						acDetail.setTxAmt(parse.stringToBigDecimal(tTempVo.getParam("DbTxAmt" + i))); // 記帳金額
						acDetail.setRvNo(tTempVo.getParam("DbRvNo" + i)); // 銷帳編號
						acDetail.setSlipNote(tTempVo.getParam("SlipNote")); // 傳票摘要
						acDetail.setAcSubBookCode(tTempVo.get("AcSubBookCode")); // 區隔帳冊
						acDetailList.add(acDetail);
					}
				}

				/* 貸 */
				for (int i = 1; i <= 5; i++) {
					if (tTempVo.get("CrAcctCode" + i) != null) {
						this.info("CrAcctCode = " + tTempVo.get("CrAcctCode" + i));
						acDetail = new AcDetail();
						acDetail.setAcDate(iAcdate); // 會計日期
						acDetail.setSlipBatNo(parse.stringToInteger(tTempVo.getParam("SlipBatNo"))); // 傳票批號
						acDetail.setDbCr("C"); // 借貸別
						acDetail.setAcctCode(tTempVo.get("CrAcctCode" + i)); // 業務科目
						acDetail.setTxAmt(parse.stringToBigDecimal(tTempVo.getParam("CrTxAmt" + i))); // 記帳金額
						acDetail.setRvNo(tTempVo.getParam("CrRvNo" + i)); // 銷帳編號
						acDetail.setSlipNote(tTempVo.getParam("SlipNote")); // 傳票摘要
						acDetail.setAcSubBookCode(tTempVo.get("AcSubBookCode")); // 區隔帳冊
						acDetailList.add(acDetail);
					}
				}
			}
		}

//		帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			this.txBuffer.addAllAcDetailList(acDetailList);
			/* 產生會計分錄 */
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}

		if (titaVo.isHcodeNormal()) {

			if (titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {
				String t3txtNo = "";
				TxTemp t3TxTemp = txTempService.txtNoLastFirst(titaVo.getTlrNo(), titaVo);
				if (t3TxTemp != null) {
					t3txtNo = t3TxTemp.getTxtNo();
				}
				Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getEntDyI() + 19110000, titaVo.getKinbr(),
						titaVo.getTlrNo(), t3txtNo, this.index, Integer.MAX_VALUE, titaVo);
				lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
				if (lTxTemp == null || lTxTemp.size() == 0) {
					throw new LogicException(titaVo, "E0001",
							"交易暫存檔 會計日期 = " + parse.stringToInteger(titaVo.getEntDy()) + 19110000 + "分行別 = "
									+ titaVo.getKinbr() + " 交易員代號 = " + titaVo.getTlrNo() + " 交易序號 = " + t3txtNo); // 查詢資料不存在
				}
				int txSlipBatNo = 0;
				for (TxTemp tx : lTxTemp) {
					boolean isSendMsg = false;
					String txItemCode = "";
					String txAcDate = "";
					t2TempVo = new TempVo();
					t2TempVo = t2TempVo.getVo(tx.getText());
					txItemCode = t2TempVo.getParam("TxItemCode");
					txAcDate = t2TempVo.getParam("TxAcDate");

					if (txSlipBatNo != this.parse.stringToInteger(t2TempVo.getParam("TxSlipBatNo"))) {
						txSlipBatNo = this.parse.stringToInteger(t2TempVo.getParam("TxSlipBatNo"));
						Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange(txItemCode, 0, 0,
								this.index, Integer.MAX_VALUE, titaVo);
						if (slTxToDoDetail == null) {
							isSendMsg = true;
						}
						if (isSendMsg) {
							this.info("txAcDate =" + txAcDate);
							this.info("txSlipBatNo =" + txSlipBatNo);
							webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6102",
									txAcDate + txSlipBatNo, "請執行L6102-核心傳票相關單獨作業，傳票批號: " + txSlipBatNo, titaVo);
						}
						this.info("isSendMsg 2 =" + isSendMsg);
					}
				}
			}
		}

// BroadCast L6102
//		if (isSendMsg) {
//			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6102",
//					titaVo.getParam("TxAcDate") + titaVo.getParam("TxSlipBatNo"),
//					"請執行L6102-核心傳票相關單獨作業，傳票批號: " + iSlipBatNo, titaVo);
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void setTxTemp(String txtNo, TitaVo titaVo) throws LogicException {
		this.info("setTxTemp ... ");
		this.info("   titaVo.getEntDy() = " + parse.stringToInteger(titaVo.getEntDy()) + 19110000);
		this.info("   titaVo.getKinbr()  = " + titaVo.getKinbr());
		this.info("   titaVo.getTlrNo()  = " + titaVo.getTlrNo());
		this.info("   titaVo.getTxtNo()  = " + titaVo.getTxtNo());
		tTxTempId = new TxTempId();
		tTxTemp = new TxTemp();
		TempVo tTempVo = new TempVo();
		String wkSeqNo = titaVo.getTxtNo();
		tTxTempId.setEntdy(parse.stringToInteger(titaVo.getEntDy()) + 19110000);
		tTxTempId.setKinbr(titaVo.getKinbr());
		tTxTempId.setTlrNo(titaVo.getTlrNo());
		tTxTempId.setTxtNo(txtNo.isEmpty() ? titaVo.getTxtNo() : txtNo);
		tTxTempId.setSeqNo(wkSeqNo);
		tTxTemp.setEntdy(parse.stringToInteger(titaVo.getEntDy()) + 19110000);
		tTxTemp.setKinbr(titaVo.getKinbr());
		tTxTemp.setTlrNo(titaVo.getTlrNo());
		tTxTemp.setTxtNo(txtNo.isEmpty() ? titaVo.getTxtNo() : txtNo);
		tTxTemp.setSeqNo(wkSeqNo);
		tTxTemp.setTxTempId(tTxTempId);

		tTempVo.clear();
		tTempVo.putParam("TxItemCode", iItemCode);
		tTempVo.putParam("TxSlipBatNo", iSlipBatNo);
		tTempVo.putParam("TxAcDate", iAcdate);
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
	}
}