package com.st1.itx.util.common;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdConvertCode;
import com.st1.itx.db.domain.CdConvertCodeId;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdConvertCodeService;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("CustCom")
@Scope("prototype")
public class CustCom extends TradeBuffer {

//	private TitaVo titaVo;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CustDataCtrlService custDataCtrlService;
	
	@Autowired
	public CdConvertCodeService cdConvertCodeService;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	public String eLoanCustTypeCode(TitaVo titaVo, String custTypeCode) throws LogicException {

		CdConvertCodeId cdConvertCodeId = new CdConvertCodeId();
		cdConvertCodeId.setCodeType("CustTypeCode");
		cdConvertCodeId.setOrgCode(custTypeCode);
		CdConvertCode cdConvertCode = cdConvertCodeService.findById(cdConvertCodeId, titaVo);
		
		if (cdConvertCode == null) {
			throw new LogicException(titaVo, "E0001", "eLoan客戶代碼:" + custTypeCode);
		}
		
		return cdConvertCode.getCodeType();
	}

	/**
	 * 依客戶戶號找客戶主檔CustMain
	 * 
	 * @param titaVo titaVo
	 * @param custNo 戶號
	 * @return CustMain
	 * @throws LogicException
	 */
	public CustMain getCustMain(TitaVo titaVo, int custNo) throws LogicException {
		return this.getCustMain(titaVo, custNo, false);
	}

	/**
	 * 依客戶戶號找客戶主檔CustMain
	 * 
	 * @param titaVo    titaVo
	 * @param custNo    戶號
	 * @param skipError 找不到時不顯示錯誤
	 * @return CustMain
	 * @throws LogicException
	 */
	public CustMain getCustMain(TitaVo titaVo, int custNo, boolean skipError) throws LogicException {
		this.info("CustCom.getCustMain/CustNo = " + custNo);

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
		if (custMain == null) {
			if (skipError) {
				custMain = new CustMain();
				custMain.setCustNo(custNo);

			} else {
				throw new LogicException(titaVo, "E0001", "客戶資料,戶號=" + custNo);
			}
		} else {
			if (custMain.getCustNo() > 0) {
				custMain = chkCustDataCtrl(titaVo, custMain);
			}
		}

		return custMain;
	}

	/**
	 * 依客戶ID找客戶主檔CustMain
	 * 
	 * @param titaVo titaVo
	 * @param custId titaVo
	 * @return CustMain
	 * @throws LogicException
	 */
	public CustMain getCustMain(TitaVo titaVo, String custId) throws LogicException {
		return this.getCustMain(titaVo, custId, false);
	}

	/**
	 * 依客戶ID找客戶主檔CustMain
	 * 
	 * @param titaVo    titaVo
	 * @param custId    客戶ID
	 * @param skipError 找不到時不顯示錯誤
	 * @return CustMain
	 * @throws LogicException
	 */
	public CustMain getCustMain(TitaVo titaVo, String custId, boolean skipError) throws LogicException {
		this.info("CustCom.getCustMain/CustId = " + custId);

		CustMain custMain = custMainService.custIdFirst(custId, titaVo);
		if (custMain == null) {
			if (skipError) {
				custMain = new CustMain();
			} else {
				throw new LogicException(titaVo, "E0001", "客戶資料,ID=" + custId);
			}
		} else {
			if (custMain.getCustNo() > 0) {
				custMain = chkCustDataCtrl(titaVo, custMain);
			}
		}

		return custMain;
	}

	private CustMain chkCustDataCtrl(TitaVo titaVo, CustMain custMain) throws LogicException {

		CustDataCtrl custDataCtrl = custDataCtrlService.findById(custMain.getCustNo(), titaVo);

		if (custDataCtrl != null) {
			custMain.setCustId("");
			custMain.setCustName("");
		}
		return custMain;
	}
}
