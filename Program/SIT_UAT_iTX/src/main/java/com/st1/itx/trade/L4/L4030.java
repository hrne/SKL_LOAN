package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * SearchRange=X,1<br>
 * END=X,1<br>
 */

@Service("L4030")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4030 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4030.class);

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
	public CdEmpService cdEmpService;

	private HashMap<tmpFacm, BigDecimal> loanBal = new HashMap<>();
	private HashMap<tmpFacm, Integer> maturityDateM = new HashMap<>();

	int selectCode = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4030 ");
		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();

		this.limit = 500;

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
//		FunctionCode = 
//		2:處理  
//		3:刪除 
//		6:取消刪除
//		9:查詢

//		此交易為勾選
		Slice<TxToDoDetail> sTxToDoDetail = null;

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		switch (selectCode) {
		case 1:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 0, 6, this.index, this.limit);
			break;
		case 2:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 0, 6, this.index, this.limit);
			break;
		case 3:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 0, 6, this.index, this.limit);
			break;
		case 4:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 2, 2, this.index, this.limit);
			break;
		case 5:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 3, 3, this.index, this.limit);
			break;
		case 6:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 1, 1, this.index, this.limit);
			break;
		case 7:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 0, 0, this.index, this.limit);
			break;
		case 9:
			sTxToDoDetail = txToDoDetailService.detailStatusRange("EMRT00", 0, 0, this.index, this.limit);
			break;
		default:
			break;
		}

		lTxToDoDetail = sTxToDoDetail == null ? null : sTxToDoDetail.getContent();

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (sTxToDoDetail != null && sTxToDoDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
//				if (selectCodeIsNotQualify(tTxToDoDetail)) {
//					continue;
//				}

				tmpFacm tmp = new tmpFacm(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo());

				if (!loanBal.containsKey(tmp)) {
					Slice<LoanBorMain> sLoanBorMain = null;

					List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

					sLoanBorMain = loanBorMainService.bormCustNoEq(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo(),
							tTxToDoDetail.getFacmNo(), 0, 999, this.index, this.limit);

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
				tmpFacm tmp = new tmpFacm(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo());

				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(tTxToDoDetail.getCustNo(), tTxToDoDetail.getCustNo());

				CdEmp tCdEmp = new CdEmp();
				if (tCustMain != null && tCustMain.getEmpNo() != null) {
					tCdEmp = cdEmpService.findById(tCustMain.getEmpNo());
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OOStatus", tTxToDoDetail.getStatus());
				occursList.putParam("OOProcessDate", tTxToDoDetail.getDataDate());
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
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
				occursList.putParam("OOOriRateKind", " ");
				occursList.putParam("OONewAgType2X", " ");
				occursList.putParam("OONewRateKind", " ");

				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean selectCodeIsNotQualify(TxToDoDetail tTxToDoDetail) throws LogicException {
		Boolean result = false;
		int today = this.getTxBuffer().getTxCom().getTbsdyf();

		switch (selectCode) {
		case 1:
			if (tTxToDoDetail.getDataDate() >= today) {
				result = true;
			}
			break;
		case 2:
			if (tTxToDoDetail.getDataDate() != today) {
				result = true;
			}
			break;
		case 3:
			break;
		case 4:
			if (tTxToDoDetail.getDataDate() != today) {
				result = true;
			}
			break;
		case 5:
			if (tTxToDoDetail.getDataDate() != today) {
				result = true;
			}
			break;
		case 6:
			break;
		case 7:
			break;
		default:
			break;
		}
		return result;
	}

//	暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int facmNo = 0;

		public tmpFacm(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public int getFacmNo() {
			return facmNo;
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