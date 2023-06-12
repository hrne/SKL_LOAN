package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4030")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4030 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public FacProdService facProdService;

	@Autowired
	public CdEmpService cdEmpService;

	private HashMap<tmpFacm, BigDecimal> loanBal = new HashMap<>();
	private HashMap<tmpFacm, Integer> maturityDateM = new HashMap<>();

	int selectCode = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4030 ");
		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();

		this.limit = 100;
		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
		Slice<TxToDoDetail> slTxToDoDetail = null;

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		// 上一營業日
		int lbsDy = this.txBuffer.getTxCom().getLbsdy() + 19110000;
		// 本營業日
		int tbsDy = this.txBuffer.getTxCom().getTbsdy() + 19110000;
		String itemCode = "EMRT00"; // 員工利率調整
//		! 1:昨日留存  0-上一營業日
//		! 2:本日新增  本營業日-本營業日
//		! 3:全部     0-99991231
//		! 4:本日處理   0-99991231
//		! 5:本日刪除   0-99991231
//		! 6:保留     0-99991231
//		! 7:未處理 0-99991231
//		! 9:未處理 (按鈕處理) 0-99991231
//   0.未處理
//   1.已保留
//   2.已處理
//   3.已刪除

		switch (selectCode) {
		case 1:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0, lbsDy, this.index, this.limit,
					titaVo);
			break;
		case 2:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, tbsDy, tbsDy, this.index, this.limit,
					titaVo);
			break;
		case 3:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 4:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 2, 2, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 5:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 3, 3, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 6:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 1, 1, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 7:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		case 9:
			slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0, 99991231, this.index, this.limit,
					titaVo);
			break;
		default:
			break;
		}

		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				tmpFacm tmp = new tmpFacm(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo());

				if (!loanBal.containsKey(tmp)) {
					Slice<LoanBorMain> sLoanBorMain = null;

					List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

					sLoanBorMain = loanBorMainService.bormCustNoEq(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo(), tTxToDoDetail.getFacmNo(), 0, 999, this.index, this.limit);

					lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

					if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
						for (LoanBorMain tLoanBorMain : lLoanBorMain) {
							int maturityDate = tLoanBorMain.getMaturityDate();
							if (loanBal.containsKey(tmp)) {
								loanBal.put(tmp, loanBal.get(tmp).add(tLoanBorMain.getLoanBal()));
								if (maturityDateM.get(tmp) > maturityDate) {
									maturityDateM.put(tmp, maturityDate);
								}
							} else {
								loanBal.put(tmp, tLoanBorMain.getLoanBal());
								maturityDateM.put(tmp, tLoanBorMain.getMaturityDate());
							}
						}
					}
				}
			}

			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				int custno = tTxToDoDetail.getCustNo();
				int facmno = tTxToDoDetail.getFacmNo();

				tmpFacm tmp = new tmpFacm(custno, facmno);

				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(custno, custno);
				String CustTypeCode = "";

				if (tCustMain != null && tCustMain.getCustTypeCode() != null) {
					CustTypeCode = tCustMain.getCustTypeCode();
				}

				CdEmp tCdEmp = new CdEmp();
				if (tCustMain != null && tCustMain.getEmpNo() != null) {
					tCdEmp = cdEmpService.findById(tCustMain.getEmpNo());
				}

				FacMainId tFacMainId = new FacMainId();
				tFacMainId.setCustNo(custno);
				tFacMainId.setFacmNo(facmno);

				FacMain tFacMain = facMainService.findById(tFacMainId, titaVo);

				String prodno = "";
				String prodname = "";
				if (tFacMain != null && tFacMain.getProdNo() != null) {
					prodno = tFacMain.getProdNo();
					FacProd tFacProd = facProdService.findById(prodno, titaVo);
					if (tFacProd != null && tFacProd.getProdName() != null) {
						prodname = tFacProd.getProdName();
					}
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OOStatus", tTxToDoDetail.getStatus());
				occursList.putParam("OOProcessDate", tTxToDoDetail.getDataDate());
				occursList.putParam("OOCustNo", custno);
				occursList.putParam("OOFacmNo", facmno);
				occursList.putParam("OOLoanBalance", loanBal.get(tmp));
				occursList.putParam("OOMaturityDate", maturityDateM.get(tmp));

				if (tCdEmp != null) {
					occursList.putParam("OORegisterDate", tCdEmp.getRegisterDate());
					occursList.putParam("OOQuitDate", tCdEmp.getQuitDate());
					occursList.putParam("OOOriAgType2X", tCdEmp.getLevelNameChs());
				} else {
					occursList.putParam("OORegisterDate", " ");
					occursList.putParam("OOQuitDate", " ");
					occursList.putParam("OOOriAgType2X", " ");
				}

				occursList.putParam("OOOriRateKind", prodno + "  " + prodname);
				occursList.putParam("OONewAgType2", CustTypeCode);
				occursList.putParam("OONewRateKind", " ");

				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int facmNo = 0;

		public tmpFacm(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
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
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			return true;
		}

		private L4030 getEnclosingInstance() {
			return L4030.this;
		}
	}
}