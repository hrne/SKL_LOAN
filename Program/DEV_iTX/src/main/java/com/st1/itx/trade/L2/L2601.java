package com.st1.itx.trade.L2;

import java.math.BigDecimal;
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
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * ReceiveDate=9,7<br>
 * DocDate=9,7<br>
 * Fee=9,14.2<br>
 * FeeCode=9,2<br>
 * LegalStaff=X,6<br>
 * CloseNo=9,7<br>
 * Rmk=X,60<br>
 */

@Service("L2601")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2601 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2601.class);

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
	GSeqCom gGSeqCom;
	@Autowired
	BaTxCom baTxCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2601 ");
		this.totaVo.init(titaVo);
		baTxCom.setTxBuffer(txBuffer);

		// new TABLE
		ForeclosureFee tForeclosureFee = new ForeclosureFee();

		// 自動取號程式
		int wkRecordNo = gGSeqCom.getSeqNo(0, 0, "L2", "2601", 9999999, titaVo);

		this.info("記錄號碼 : " + wkRecordNo);
		tForeclosureFee.setRecordNo(wkRecordNo);
		tForeclosureFee.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tForeclosureFee.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tForeclosureFee.setReceiveDate(parse.stringToInteger(titaVo.getParam("ReceiveDate")));
		// 起帳日期 新增的會計日期
		tForeclosureFee.setOpenAcDate(parse.stringToInteger(titaVo.getParam("AcDate")));
//		tForeclosureFee.setOpenAcDate(dateUtil.getNowIntegerForBC());
		this.info("起帳日期" + tForeclosureFee.getOpenAcDate());
		tForeclosureFee.setDocDate(parse.stringToInteger(titaVo.getParam("DocDate")));
		tForeclosureFee.setFee(parse.stringToBigDecimal(titaVo.getParam("TimFee")));
		tForeclosureFee.setRemitBranch("0");
		tForeclosureFee.setCaseNo("0");
		tForeclosureFee.setFeeCode(titaVo.getParam("FeeCode"));
		tForeclosureFee.setLegalStaff(titaVo.getParam("LegalStaff"));
		tForeclosureFee.setCloseNo(wkRecordNo);
		tForeclosureFee.setRmk(titaVo.getParam("Rmk"));

		try {
			sForeclosureFeeService.insert(tForeclosureFee);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", e.getErrorMsg());
		}

		// 查詢各項費用
		baTxCom.settingUnPaid(this.txBuffer.getTxCom().getTbsdy(), tForeclosureFee.getCustNo(), 000, 000, 99,
				BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)
		this.info("累溢收 = " + baTxCom.getExcessive());
		this.info("法拍費 = " + tForeclosureFee.getFee());
		if (baTxCom.getExcessive().compareTo(tForeclosureFee.getFee()) >= 0) {
			this.info("可抵繳的未銷餘額足夠 = ");
			this.totaVo.putParam("OWarningMsg", "此戶目前暫收款可抵繳餘額超過法拍費用");
		}

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
		// 紀錄號碼 7 int轉string左補0
		acReceivable.setRvNo(parse.IntegerToString(tForeclosureFee.getRecordNo(), 7)); // 銷帳編號
		acReceivable.setOpenAcDate(tForeclosureFee.getDocDate());

		acReceivableList.add(acReceivable);

		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳

		this.totaVo.putParam("OCloseNo", wkRecordNo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}