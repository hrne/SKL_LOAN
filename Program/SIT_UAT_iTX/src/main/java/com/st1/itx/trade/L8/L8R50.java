package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryDetailId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimAcDate=9,7
 * RimFactor=9,2
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNo=9,3
 */
@Service("L8R50") // 尋找疑似洗錢交易合理性明細檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8R50 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R50.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryDetailService sMlaundryDetailService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L8R50 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		int iRimFactor = this.parse.stringToInteger(titaVo.getParam("RimFactor"));
		int iRimCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
//		int iRimFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
//		int iRimBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("RimAcDate"));
		int iFAcDate = iAcDate + 19110000;

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L8R50"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L8R50"); // 功能選擇錯誤
		}

		// 查詢客戶資料主檔
		CustMain tCustMain = new CustMain();
		tCustMain = sCustMainService.custNoFirst(iRimCustNo, iRimCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
		}

		// 初始值Tota
		moveTotaMlaundryDetail(new MlaundryDetail(), tCustMain.getCustName());

		// 查詢疑似洗錢交易合理性明細檔
		MlaundryDetail tMlaundryDetail = sMlaundryDetailService.findById(new MlaundryDetailId(iFAcDate, iRimFactor, iRimCustNo), titaVo);

		logger.info("tMlaundryDetail=" + tMlaundryDetail);
		/* 如有找到資料 */
		if (tMlaundryDetail != null) {
			if (iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimCustNo")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaMlaundryDetail(tMlaundryDetail, tCustMain.getCustName());
			}
		} else {
			if (iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "疑似洗錢交易合理性明細檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 疑似洗錢交易合理性明細檔
	private void moveTotaMlaundryDetail(MlaundryDetail mMlaundryDetail, String mCustName) throws LogicException {

		this.totaVo.putParam("L8R50AcDate", mMlaundryDetail.getEntryDate()); // 入帳日期
		logger.info("L8r50 AcDate=" + mMlaundryDetail.getEntryDate());
		this.totaVo.putParam("L8R50Factor", mMlaundryDetail.getFactor()); // 交易樣態
		this.totaVo.putParam("L8R50CustNo", mMlaundryDetail.getCustNo()); // 戶號
		this.totaVo.putParam("L8R50TotalCnt", mMlaundryDetail.getTotalCnt()); // 累積筆數
		this.totaVo.putParam("L8R50TotalAmt", mMlaundryDetail.getTotalAmt()); // 累積金額
		this.totaVo.putParam("L8R50Rational", mMlaundryDetail.getRational()); // 合理性記號
		this.totaVo.putParam("L8R50EmpNoDesc", mMlaundryDetail.getEmpNoDesc().replace("$n", "\n")); // 經辦合理性說明
		this.totaVo.putParam("L8R50ManagerDesc", mMlaundryDetail.getManagerDesc().replace("$n", "\n")); // 主管覆核說明
		this.totaVo.putParam("L8R50ManagerCheck", mMlaundryDetail.getManagerCheck()); // 主管覆核
		this.totaVo.putParam("L8R50ManagerDate", mMlaundryDetail.getManagerDate()); // 主管同意日期
		this.totaVo.putParam("L8R50ManagerCheckDate", mMlaundryDetail.getManagerCheckDate()); // 主管覆核日期
		logger.info("L8r50 ManagerDate=" + mMlaundryDetail.getManagerDate());
		this.totaVo.putParam("L8R50CustName", mCustName); // 戶名
		logger.info("L8r50 mCustName=" + mCustName);
	}

}