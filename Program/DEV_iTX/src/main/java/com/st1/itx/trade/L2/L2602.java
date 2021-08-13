package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2602")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2602 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2602.class);

	// 銷帳處理
	@Autowired
	public AcReceivableCom acReceivableCom;
	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2602 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 記錄號碼
		int iRecordNo = parse.stringToInteger(titaVo.getParam("RecordNo"));

		int iCloseDate = parse.stringToInteger(titaVo.getParam("CloseDate"));
		// new table
		ForeclosureFee tForeclosureFee = new ForeclosureFee();

		if (iFunCd == 2) {

			tForeclosureFee = sForeclosureFeeService.holdById(iRecordNo);
			if (tForeclosureFee == null) {
				throw new LogicException("E0003", "L2602(ForeclosureFee)");
			}
			// 變更前
			ForeclosureFee beforeForeclosureFee = (ForeclosureFee) dataLog.clone(tForeclosureFee);

			tForeclosureFee.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tForeclosureFee.setReceiveDate(parse.stringToInteger(titaVo.getParam("ReceiveDate")));
			tForeclosureFee.setDocDate(parse.stringToInteger(titaVo.getParam("DocDate")));
			tForeclosureFee.setFee(parse.stringToBigDecimal(titaVo.getParam("TimFee")));
			tForeclosureFee.setFeeCode(titaVo.getParam("FeeCode"));
			tForeclosureFee.setCaseNo(titaVo.getParam("CaseNo"));
			tForeclosureFee.setCloseDate(parse.stringToInteger(titaVo.getParam("CloseDate")));
			tForeclosureFee.setRmk(titaVo.getParam("Rmk"));
			tForeclosureFee.setCloseNo(parse.stringToInteger(titaVo.getParam("CloseNo")));
			tForeclosureFee.setOverdueDate(parse.stringToInteger(titaVo.getParam("OverdueDate")));

			try {
				// 修改
				tForeclosureFee = sForeclosureFeeService.update2(tForeclosureFee);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeForeclosureFee, tForeclosureFee);
			dataLog.exec();

			// 銷帳
			AcReceivable acReceivable = new AcReceivable();
			List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
			
			if(iCloseDate == 0) {
				acReceivable.setReceivableFlag(2); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				acReceivable.setAcctCode("F07"); // 業務科目
				// 法拍費用
				acReceivable.setRvAmt(tForeclosureFee.getFee()); // 記帳金額
				// 戶號 7
				acReceivable.setCustNo(tForeclosureFee.getCustNo());// 戶號+額度
				// 額度 3
				acReceivable.setFacmNo(tForeclosureFee.getFacmNo());
				// 紀錄號碼 7 int轉string
				acReceivable.setRvNo(parse.IntegerToString(tForeclosureFee.getRecordNo(), 7)); // 銷帳編號
				acReceivable.setOpenAcDate(tForeclosureFee.getDocDate()); // 單據日期
				acReceivableList.add(acReceivable);

				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
			}
			// 起帳
			acReceivable = new AcReceivable();
			acReceivableList = new ArrayList<AcReceivable>();
			acReceivable.setReceivableFlag(2); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			acReceivable.setAcctCode("F07"); // 業務科目
			// 法拍費用
			acReceivable.setRvAmt(tForeclosureFee.getFee()); // 記帳金額
			// 戶號 7
			acReceivable.setCustNo(tForeclosureFee.getCustNo());// 戶號+額度
			// 額度 3
			acReceivable.setFacmNo(tForeclosureFee.getFacmNo());
			// 紀錄號碼 7 int轉string
			acReceivable.setRvNo(parse.IntegerToString(tForeclosureFee.getRecordNo(), 7)); // 銷帳編號
			acReceivable.setOpenAcDate(tForeclosureFee.getDocDate()); // 單據日期
			acReceivableList.add(acReceivable);

			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳

		} else if (iFunCd == 4) {
			tForeclosureFee = sForeclosureFeeService.holdById(iRecordNo);
			if (tForeclosureFee == null) {
				throw new LogicException("E0004", "L2602(ForeclosureFee)");
			}
			try {

				ForeclosureFee tForeclosureFee4 = sForeclosureFeeService.holdById(iRecordNo);

				this.info(" L2602 deletetForeclosureFee4Log" + tForeclosureFee4);
				if (tForeclosureFee4 != null) {
					sForeclosureFeeService.delete(tForeclosureFee4);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			// 銷帳
			if(iCloseDate == 0) {
				AcReceivable acReceivable = new AcReceivable();
				List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
				acReceivable.setReceivableFlag(2); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				acReceivable.setAcctCode("F07"); // 業務科目
				// 法拍費用
				acReceivable.setRvAmt(tForeclosureFee.getFee()); // 記帳金額
				// 戶號 7
				acReceivable.setCustNo(tForeclosureFee.getCustNo());// 戶號+額度
				// 額度 3
				acReceivable.setFacmNo(tForeclosureFee.getFacmNo());
				// 紀錄號碼 7 int轉string
				acReceivable.setRvNo(parse.IntegerToString(tForeclosureFee.getRecordNo(), 7)); // 銷帳編號
				acReceivable.setOpenAcDate(tForeclosureFee.getDocDate()); // 單據日期
				acReceivableList.add(acReceivable);

				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
			}
		} else if (iFunCd == 5) {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}