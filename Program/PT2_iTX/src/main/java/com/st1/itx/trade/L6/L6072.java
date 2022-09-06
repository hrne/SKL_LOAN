package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * BranchNo=X,4<br>
 * END=X,1<br>
 */

@Service("L6072") // 營業單位對照檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6072 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBranchService sCdBranchService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public TxDataLogService txDataLogService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6072 ");
		this.totaVo.init(titaVo);

		String iBranchNo = titaVo.getParam("BranchNo");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 113 * 200 = 22600

		// 查詢營業單位對照檔檔
		Slice<CdBranch> slCdBranch = sCdBranchService.BranchNoLike(iBranchNo.trim() + "%", this.index, this.limit, titaVo);
		List<CdBranch> lCdBranch = slCdBranch == null ? null : slCdBranch.getContent();

		if (lCdBranch == null || lCdBranch.size() == 0) {
			throw new LogicException(titaVo, "E0001", "營業單位對照檔檔"); // 查無資料
		}
		// 如有找到資料
		for (CdBranch tCdBranch : lCdBranch) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOBranchNo", tCdBranch.getBranchNo());
			occursList.putParam("OOBranchShort", tCdBranch.getBranchShort());
			occursList.putParam("OOBranchItem", tCdBranch.getBranchItem());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdBranch.getLastUpdate()) + " " + parse.timeStampToStringTime(tCdBranch.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdBranch.getLastUpdateEmpNo() + " " + empName(titaVo, tCdBranch.getLastUpdateEmpNo()));

			// 新增功能：歷程按鈕，如果查無資料就不顯示該按鈕
			Slice<TxDataLog> slTxDataLog = null;
			try {
				slTxDataLog = txDataLogService.findByTranNo("L6702", "CODE:" + tCdBranch.getBranchNo(), 0, 1);
			} catch (Exception e) {
				this.error("L6702 Exeception When txDataLogService: " + e.getMessage());
				throw new LogicException("E0013", "txDataLogService");
			}
			List<TxDataLog> lTxDataLog = slTxDataLog == null ? null : slTxDataLog.getContent();
			if (lTxDataLog == null || lTxDataLog.isEmpty())
				occursList.putParam("OOHasL6933", "N");
			else
				occursList.putParam("OOHasL6933", "Y");

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdBranch != null && slCdBranch.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}