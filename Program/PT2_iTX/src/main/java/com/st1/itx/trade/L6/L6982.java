package com.st1.itx.trade.L6;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * SelectCode=9,1<br>
 * END=X,1<br>
 */

@Service("L6982")
@Scope("prototype")
/**
 * 火險費轉列催收作業
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L6982 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService sAcReceivableService;

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public InsuRenewService insuRenewService;
	
	@Autowired
	public CdEmpService sCdEmpService;

	private int selectCode = 0;
	private int custNo = 0;
//	private int trasCollDate = 0;
	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6982 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 400; // 147 * 400 = 58800

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));
		custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
//		trasCollDate = parse.stringToInteger(titaVo.getParam("TransCollDate"));

		// 0.未處理
		// 1.已保留
		// 2.已處理
		// 3.已刪除

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
		Slice<InsuRenew> slInsuRenew = null;
		Slice<TxToDoDetail> slTxToDoDetail = null;
//		TRIS00 火險轉列催收

//		! 1:昨日留存 
//		! 2:本日新增 
//		! 3:全部     
//		! 4:本日處理 
//		! 5:本日刪除 
//		! 6:保留    
//		! 7:未處理
//		! 9:未處理 (按鈕處理)
		if (custNo > 0) {
			slInsuRenew = insuRenewService.findCustEq(custNo, this.index, this.limit, titaVo);
			lInsuRenew = slInsuRenew == null ? null : slInsuRenew.getContent();
			this.info("custno>0");

			if (lInsuRenew == null) {
				throw new LogicException(titaVo, "E2003", "火險單續保檔");

			}
			for (InsuRenew tmpInsuRenew : lInsuRenew) {
				if (tmpInsuRenew.getAcDate() == 0 && tmpInsuRenew.getStatusCode() == 2) {
					OccursList occursList = new OccursList();

					occursList.putParam("OOProcStatus", 0);
					occursList.putParam("OOFireInsuMonth", tmpInsuRenew.getInsuYearMonth() - 191100);
					occursList.putParam("OOCustNo", tmpInsuRenew.getCustNo());
					occursList.putParam("OOFacmNo", tmpInsuRenew.getFacmNo());
					occursList.putParam("OOColNo",
							tmpInsuRenew.getClCode1() + "-" + tmpInsuRenew.getClCode2() + "-" + tmpInsuRenew.getClNo());
					occursList.putParam("OOInsuNo", tmpInsuRenew.getPrevInsuNo());
					occursList.putParam("OOInsuFireFee", tmpInsuRenew.getFireInsuPrem());
					occursList.putParam("OOEthqInsuFee", tmpInsuRenew.getEthqInsuPrem());
					occursList.putParam("OOInsuStartDate", tmpInsuRenew.getInsuStartDate());
					occursList.putParam("OOInsuEndDate", tmpInsuRenew.getInsuEndDate());
					occursList.putParam("OOTransCollAmt", tmpInsuRenew.getTotInsuPrem());
					occursList.putParam("OORelNo", "");
					occursList.putParam("OOItemCode", "TRIS00");
					occursList.putParam("OOBormNo", "");
					occursList.putParam("OODtlValue", tmpInsuRenew.getPrevInsuNo());
					// 最後修改時間
					Timestamp lastUpdateTime = tmpInsuRenew.getLastUpdate();
					occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(lastUpdateTime) + " " + parse.timeStampToStringTime(lastUpdateTime));
					// 最後修改人員
					String lastUpdateEmpNo = tmpInsuRenew.getLastUpdateEmpNo();
					occursList.putParam("OOLastEmp", getLastUpdateEmp(lastUpdateEmpNo, titaVo));
					cnt++;
					this.totaVo.addOccursList(occursList);
				}

			}
		} else {
			this.info("未輸入");
			switch (selectCode) {
			case 1:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 0, 3, this.index, this.limit, titaVo);

				break;
			case 2:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 0, 3, this.index, this.limit, titaVo);
				break;
			case 3:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 0, 3, this.index, this.limit, titaVo);
				break;
			case 4:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 2, 2, this.index, this.limit, titaVo);
				break;
			case 5:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 3, 3, this.index, this.limit, titaVo);
				break;
			case 6:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 1, 1, this.index, this.limit, titaVo);
				break;
			case 7:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 0, 0, this.index, this.limit, titaVo);
				break;
			case 9:
				slTxToDoDetail = txToDoDetailService.detailStatusRange("TRIS00", 0, 0, this.index, this.limit, titaVo);
				break;
			default:
				break;
			}
			lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
		}

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				OccursList occursList = new OccursList();

				if (selectCodeIsNotQualify(tTxToDoDetail)) {
					continue;
				}

				tTxToDoDetail.getCustNo();
				tTxToDoDetail.getFacmNo();
				tTxToDoDetail.getDtlValue().trim();
				InsuRenew tIsuRenew = new InsuRenew();
				tIsuRenew = insuRenewService.prevInsuNoFirst(tTxToDoDetail.getCustNo(), tTxToDoDetail.getFacmNo(),
						tTxToDoDetail.getDtlValue().trim(), titaVo);

				occursList.putParam("OOProcStatus", tTxToDoDetail.getStatus());
				if (tIsuRenew != null) {
					occursList.putParam("OOFireInsuMonth", tIsuRenew.getInsuYearMonth() - 191100);
					occursList.putParam("OOColNo",
							tIsuRenew.getClCode1() + "-" + tIsuRenew.getClCode2() + "-" + tIsuRenew.getClNo());
					occursList.putParam("OOInsuNo", tIsuRenew.getPrevInsuNo());
					occursList.putParam("OOInsuFireFee", tIsuRenew.getFireInsuPrem());
					occursList.putParam("OOEthqInsuFee", tIsuRenew.getEthqInsuPrem());
					occursList.putParam("OOInsuStartDate", tIsuRenew.getInsuStartDate());
					occursList.putParam("OOInsuEndDate", tIsuRenew.getInsuEndDate());
					occursList.putParam("OOTransCollAmt", tIsuRenew.getTotInsuPrem());
				} else {
					occursList.putParam("OOFireInsuMonth", 0);
					occursList.putParam("OOColNo", "");
					occursList.putParam("OOInsuNo", "");
					occursList.putParam("OOInsuFireFee", 0);
					occursList.putParam("OOEthqInsuFee", 0);
					occursList.putParam("OOInsuStartDate", 0);
					occursList.putParam("OOInsuEndDate", 0);
					occursList.putParam("OOTransCollAmt", 0);
					occursList.putParam("OOLastUpdate", "");
					occursList.putParam("OOLastEmp", "");
				}
				occursList.putParam("OOCustNo", tTxToDoDetail.getCustNo());
				occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
				occursList.putParam("OORelNo", tTxToDoDetail.getTitaEntdy() + tTxToDoDetail.getTitaKinbr()
						+ tTxToDoDetail.getTitaTlrNo() + parse.IntegerToString(tTxToDoDetail.getTitaTxtNo(), 8));
				occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
				occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
				occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());
				// 最後修改時間
				Timestamp lastUpdateTime = tTxToDoDetail.getLastUpdate();
				occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(lastUpdateTime) + " " + parse.timeStampToStringTime(lastUpdateTime));
				// 最後修改人員
				String lastUpdateEmpNo = tTxToDoDetail.getLastUpdateEmpNo();
				occursList.putParam("OOLastEmp", getLastUpdateEmp(lastUpdateEmpNo, titaVo));
				cnt++;
				this.totaVo.addOccursList(occursList);
			}
		}
		if (cnt == 0) {
			throw new LogicException(titaVo, "E0001", "火險單續保檔"); // 查詢資料不存在
		}

		if (custNo > 0) {
			// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
			if (slInsuRenew != null && slInsuRenew.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
			if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean selectCodeIsNotQualify(TxToDoDetail tTxToDoDetail) throws LogicException {
		Boolean result = false;
		int today = this.getTxBuffer().getTxCom().getTbsdy();
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

		default:
			break;
		}

		if (custNo > 0) {
			if (tTxToDoDetail.getCustNo() != custNo) {
				result = true;
			}
		}

		return result;
	}

	private String getLastUpdateEmp(String empNo, TitaVo titaVo) throws LogicException {
		
		String empName = null;
		
		CdEmp tCdEmp = sCdEmpService.findById(empNo, titaVo);
		if(tCdEmp != null)
			empName = tCdEmp.getFullname();
		
		empName = empName.equals("") ? empNo : empName;
		
		return empNo + " " + empName;
	}
}