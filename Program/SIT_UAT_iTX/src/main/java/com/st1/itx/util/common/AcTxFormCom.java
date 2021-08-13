package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 產出交易分錄清單 <BR>
 * 1.run 產出分錄單 call by AcEntetCom、L6901 <BR>
 * 1.1 Form 格式 ：FM101 <BR>
 * 
 * @author st1
 * @version 1.0.0
 */
@Component("acTxFormCom")
@Scope("prototype")
public class AcTxFormCom extends TradeBuffer {

	private TitaVo titaVo;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcDetailService acDetailService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	public CdCodeService cdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.titaVo = titaVo;
		this.totaVo.init(titaVo);
		List<AcDetail> acDetailList = new ArrayList<AcDetail>();

		acDetailList = this.txBuffer.getAcDetailList();

		if (acDetailList != null && acDetailList.size() != 0) {
			AcDetail headAcDetail = new AcDetail();
			headAcDetail = acDetailList.get(0);

//			業務類別 01.撥款匯款 02.支票繳款 03.債協 09.放款
			CdCode tSN = cdCodeService.getItemFirst(6, "SecNo", FormatUtil.pad9(headAcDetail.getTitaSecNo(), 2), titaVo);

			String secNoX = "";

			if (tSN != null) {
				secNoX = tSN.getItem();
				this.info("secNoX = " + secNoX);
			}

			this.totaVo.putParam("MSGID", "FM101");

			TxTranCode txTranCode = txTranCodeService.findById(headAcDetail.getTitaTxCd());

//			帳冊別
			String acBookCode = "";

			if (headAcDetail.getAcBookCode() != null && !"".equals(headAcDetail.getAcBookCode().trim())) {
				acBookCode = FormatUtil.padX(headAcDetail.getAcBookCode(), 3);
			}

			String acBookCodeX = "";
//			帳冊別 000.全帳冊 10H.放款帳冊 201.利變年金 301.測試用帳冊
			if (!"".equals(acBookCode.trim())) {
				CdCode tABC = cdCodeService.getItemFirst(6, "AcBookCode", acBookCode, titaVo);
				if (tABC != null) {
					acBookCodeX = tABC.getItem();
					this.info("acBookCodeX = " + acBookCodeX);
				}
			}

//			會計日期 
			this.totaVo.putParam("FM101_AcDate", headAcDetail.getAcDate());
//			登放序號 
			this.totaVo.putParam("FM101_RelTxseq", headAcDetail.getAcDetailId().getRelTxseq());
//			交易代號 & 中文 
			this.totaVo.putParam("FM101_TitaTxCd", headAcDetail.getTitaTxCd());
			if (txTranCode == null) {
				this.totaVo.putParam("FM101_TitaTxCdX", " ");
			} else {
				this.totaVo.putParam("FM101_TitaTxCdX", txTranCode.getTranItem());
			}
//			業務類別  & 中文 
			this.totaVo.putParam("FM101_TitaSecNo", headAcDetail.getTitaSecNo());
			this.totaVo.putParam("FM101_TitaSecNoX", secNoX);
//			傳票摘要
			this.totaVo.putParam("FM101_SlipNote", headAcDetail.getSlipNote());
//			經辦
			this.totaVo.putParam("FM101_TitaTlrNo", headAcDetail.getTitaTlrNo());
//			主管 
			this.totaVo.putParam("FM101_TitaSupNo", headAcDetail.getTitaSupNo());
//			傳票批號 
			this.totaVo.putParam("FM101_SlipBatNo", FormatUtil.pad9("" + headAcDetail.getSlipBatNo(), 2));
//			總帳記號 0.未放行 1.已入帳 2.訂正  3.沖正  4-訂正(出分錄清單用，帳務明細刪除)
			this.totaVo.putParam("FM101_EntAc", headAcDetail.getEntAc());
//			帳冊別
			this.totaVo.putParam("FM101_AcBookCode", acBookCode);
			this.totaVo.putParam("FM101_AcBookCodeX", acBookCodeX);

			for (AcDetail tAcDetail : acDetailList) {
				String acNoCodeX = " ";

//				cdAcCodeService
				CdAcCode tCdAcCode = new CdAcCode();
				CdAcCodeId tCdAcCodeId = new CdAcCodeId();
				tCdAcCodeId.setAcNoCode(tAcDetail.getAcNoCode());
				tCdAcCodeId.setAcSubCode(tAcDetail.getAcSubCode());
				tCdAcCodeId.setAcDtlCode(tAcDetail.getAcDtlCode());

				tCdAcCode = cdAcCodeService.findById(tCdAcCodeId);

//				科子細目中文
				if (tCdAcCode != null) {
					acNoCodeX = tCdAcCode.getAcNoItem();
				}

//				區隔帳冊
				String acSubBookCode = "";
				if (tAcDetail.getAcSubBookCode() != null && !"".equals(tAcDetail.getAcSubBookCode().trim())) {
					acSubBookCode = FormatUtil.padX(tAcDetail.getAcSubBookCode(), 3);
				}
				String acSubBookCodeX = "";
//				00A	傳統帳冊	201	利變年金帳冊
				if (!"".equals(acSubBookCode.trim())) {
					CdCode tABC = cdCodeService.getItemFirst(6, "AcSubBookCode", acSubBookCode, titaVo);
					if (tABC != null) {
						acSubBookCodeX = tABC.getItem();
						this.info("acBookCodeX = " + acSubBookCodeX);
					}
				}

				OccursList occursList = new OccursList();
//				彙總別
				occursList.putParam("FM101_SumNo", tAcDetail.getSumNo());
//				戶號-額度-撥款
				occursList.putParam("FM101_CustNo", tAcDetail.getCustNo());
				occursList.putParam("FM101_FacmNo", tAcDetail.getFacmNo());
				occursList.putParam("FM101_BormNo", tAcDetail.getBormNo());
//				科子目
				occursList.putParam("FM101_AcNoCode", FormatUtil.padX(tAcDetail.getAcNoCode(), 11) + "-" + FormatUtil.padX(tAcDetail.getAcSubCode(), 5));
				occursList.putParam("FM101_AcDtlCode", tAcDetail.getAcDtlCode());
//				科子細目中文
				occursList.putParam("FM101_AcNoCodeX", acNoCodeX);
//				區隔帳冊
				occursList.putParam("FM101_AcSubBookCode", acSubBookCode);
				occursList.putParam("FM101_AcSubBookCodeX", acSubBookCodeX);
//				幣別
				occursList.putParam("FM101_CurrencyCode", tAcDetail.getCurrencyCode());
//				貸方金額(D) 借方金額(C)
				if (tAcDetail.getDbCr().equals("D")) {
					occursList.putParam("FM101_DbAmt", tAcDetail.getTxAmt());
					occursList.putParam("FM101_CrAmt", 0);
				} else if (tAcDetail.getDbCr().equals("C")) {
					occursList.putParam("FM101_DbAmt", 0);
					occursList.putParam("FM101_CrAmt", tAcDetail.getTxAmt());
				} else {
					throw new LogicException("E0001", "DbCr未定義");
				}

				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException("E0001", "查無資料");
		}
		this.info("totaVo : " + this.totaVo.toString());

		this.addList(this.totaVo);
		return this.sendList();
	}
}
