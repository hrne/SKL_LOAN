package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ReceiveDateStart=9,7<br>
 * ReceiveDateEnd=9,7<br>
 * CustNoStart=9,7<br>
 * CustNoEnd=9,7<br>
 * END=X,1<br>
 */

@Service("L2078")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */

public class L2078 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2078.class);

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2078 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 143 * 400 = 57200

		// tita
		// 收件日
		int iReceiveDateStart = parse.stringToInteger(titaVo.getParam("ReceiveDateStart")) + 19110000;
		int iReceiveDateEnd = parse.stringToInteger(titaVo.getParam("ReceiveDateEnd")) + 19110000;
		// 戶號
		int iCustNoStart = parse.stringToInteger(titaVo.getParam("CustNoStart"));
		int CustNoEnd = parse.stringToInteger(titaVo.getParam("CustNoEnd"));

		// 轉催註記
		String overdueCode = "";
		// new ArrayList
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();

		// 依tita值查詢
		Slice<ForeclosureFee> slForeclosureFee = sForeclosureFeeService.selectForL2078(iReceiveDateStart, iReceiveDateEnd, iCustNoStart, CustNoEnd, this.index, this.limit, titaVo);

		lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
		// 查無資料 拋錯
		if (lForeclosureFee == null) {
			throw new LogicException(titaVo, "E2003", "查無資料,請至L2601新增"); // 查無資料
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slForeclosureFee != null && slForeclosureFee.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		for (ForeclosureFee tForeclosureFee : lForeclosureFee) {

			// new occurs
			OccursList occursList = new OccursList();

			// 催收註記
			if (tForeclosureFee.getOverdueDate() != 0) {
				overdueCode = "Y";
			} else {
				overdueCode = "";
			}

			occursList.putParam("OORecordNo", tForeclosureFee.getRecordNo());
			occursList.putParam("OOReceiveDate", tForeclosureFee.getReceiveDate());
			occursList.putParam("OOCustNo", tForeclosureFee.getCustNo());
			occursList.putParam("OOFacmNo", tForeclosureFee.getFacmNo());
			occursList.putParam("OOFee", tForeclosureFee.getFee());
			occursList.putParam("OOFeeCode", tForeclosureFee.getFeeCode());
			occursList.putParam("OOCaseCode", tForeclosureFee.getCaseCode());
			// 單位
			occursList.putParam("OOUnit", tForeclosureFee.getRemitBranch());
			occursList.putParam("OOCaseNo", tForeclosureFee.getCaseNo());
			// 入帳日期
			occursList.putParam("OOEntryDate", tForeclosureFee.getDocDate());
			occursList.putParam("OOCloseDate", tForeclosureFee.getCloseDate());
			// 催收記號
			occursList.putParam("OOOverdue", overdueCode);
			occursList.putParam("OOCloseNo", tForeclosureFee.getCloseNo());
			occursList.putParam("OODocDate", tForeclosureFee.getDocDate()); // 單據日期
			occursList.putParam("OOLegalStaff", tForeclosureFee.getLegalStaff()); // 法務人員
			occursList.putParam("OORmk", tForeclosureFee.getRmk()); // 備註

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}