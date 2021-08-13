package com.st1.itx.trade.L2;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunCd=9,1<br>
 * CustId=X,10<br>
 * CustNo=9,7<br>
 * CustName=X,100<br>
 * CreateEmpNo=X,6<br>
 * CreateDate=9,7<br>
 */

@Service("L2703")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2703 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2703.class);

	/* DB服務注入 */
	@Autowired
	public CustDataCtrlService sCustDataCtrlService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2703 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 客戶統編
		String iCustId = titaVo.getParam("CustId");
		// new table
		CustMain tCustMain = new CustMain();
		CustDataCtrl tCustDataCtrl = new CustDataCtrl();

		// 客戶統編找客戶主檔戶號
		tCustMain = sCustMainService.custIdFirst(iCustId);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "客戶主檔");
		}
		int custNo = tCustMain.getCustNo();
		// 如無戶號則拋錯
		if (custNo == 0) {
			throw new LogicException(titaVo, "E0002", "無戶號");
		}
		String custUKet = tCustMain.getCustUKey();

		if (iFunCd == 1) {

			tCustDataCtrl = new CustDataCtrl();
			// 新增時,測試該戶號是否存在結清戶個資控管檔 如存在 拋錯
			tCustDataCtrl = sCustDataCtrlService.findById(custNo);
			if (tCustDataCtrl != null) {
				throw new LogicException(titaVo, "E0002", "L2703 該戶號" + custNo + "已存在於結清戶個資控管檔。");
			}
			// 檢查是否結清
			Slice<LoanBorMain> slLoanBorMain = sLoanBorMainService.bormCustNoEq(custNo, 0, 999, 0, 999, 0,
					Integer.MAX_VALUE, titaVo);
			List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
			if (lLoanBorMain != null) {
				for (LoanBorMain tLoanBorMain : lLoanBorMain) {
					if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 4
							|| tLoanBorMain.getStatus() == 7 || tLoanBorMain.getStatus() == 99) {
						throw new LogicException(titaVo, "E0002", "該戶號非結清戶");
					}
				}
			}
			

			tCustDataCtrl = new CustDataCtrl();
			tCustDataCtrl.setCustNo(custNo);
			tCustDataCtrl.setCustUKey(custUKet);
			tCustDataCtrl.setEnable("Y");
			tCustDataCtrl.setCreateEmpNo(titaVo.getParam("CreateEmpNo"));
			tCustDataCtrl.setLastUpdateEmpNo(titaVo.getParam("CreateEmpNo"));

			/* 存入DB */
			try {
				this.info("insert");
				tCustDataCtrl = sCustDataCtrlService.insert(tCustDataCtrl);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// 刪除
		} else if (iFunCd == 4) {

			tCustDataCtrl = sCustDataCtrlService.holdById(custNo);
			if (tCustDataCtrl == null) {
				throw new LogicException(titaVo, "E0004", "L2703 該戶號" + custNo + "不存在於結清戶個資控管檔。");
			}

			try {

				this.info(" L2703 deletetCustDataCtrlLog : " + tCustDataCtrl);

				if (tCustDataCtrl != null) {
					sCustDataCtrlService.delete(tCustDataCtrl);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}

		} else if (iFunCd == 5) {

			tCustDataCtrl = sCustDataCtrlService.findById(custNo);
		}

		// 宣告
		Timestamp ts = tCustDataCtrl.getCreateDate();
		this.info("ts = " + ts);
		String createDate = "";
		String createTime = "";
		DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
		DateFormat sdftime = new SimpleDateFormat("HHmmss");

		createDate = sdfdate.format(ts);
		createTime = sdftime.format(ts);
		this.info("createDate = " + createDate);
		this.info("createTime = " + createTime);

		this.totaVo.putParam("OCreateDate", parse.stringToInteger(createDate) - 19110000);
		this.totaVo.putParam("OCreateTime", createTime);

		this.addList(this.totaVo);
		return this.sendList();
	}
}