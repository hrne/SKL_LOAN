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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita Year=9,3 Month=9,2 END=X,1
 */

@Service("L6082") // 放款業績工作月對照檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6082 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdWorkMonthService sCdWorkMonthService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6082 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iYear = this.parse.stringToInteger(titaVo.getParam("Year"));
		this.info("L6082 iYear : " + iYear);
		int iFYear = iYear + 1911;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500; // 19 * 500 = 9500

		// 查詢放款業績工作月對照檔
		Slice<CdWorkMonth> slCdWorkMonth;
		if (iYear == 0) {
			slCdWorkMonth = sCdWorkMonthService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdWorkMonth = sCdWorkMonthService.findYearMonth(iFYear, iFYear, 00, 99, this.index, this.limit, titaVo);
		}
		List<CdWorkMonth> lCdWorkMonth = slCdWorkMonth == null ? null : slCdWorkMonth.getContent();

		if (lCdWorkMonth == null || lCdWorkMonth.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款業績工作月對照檔"); // 查無資料
		}
		
		int distinctYear = 1911;
		Timestamp latestUpdateTime = null;
		String lastestUpdateEmp = "";
		for (CdWorkMonth tCdWorkMonth : lCdWorkMonth) {
			// 只抓取民國1年開始以後的資料
			if(tCdWorkMonth.getYear() >= 1912) {
				// 抓到的資料的 西元年
				int yearAD = tCdWorkMonth.getYear();
				// 抓到的資料的 lastUpdateTimeStamp
				Timestamp ts = tCdWorkMonth.getLastUpdate();
				// 抓到的資料的 lastUpdateEmpNo
				String empNo = tCdWorkMonth.getLastUpdateEmpNo();
				// 如果年度重複
				if(distinctYear == yearAD) {
					// 做最後修改時間的比較
					if(latestUpdateTime.compareTo(ts) < 0) {
						// 設定成最近更新的資料：時間、人員
						latestUpdateTime = ts;
						lastestUpdateEmp = getLastUpdateEmp(empNo, titaVo);
					}
					continue;
				}
				// 第一個年度開始時，不做上一個年度（null）的資料儲存
				if(distinctYear > 1911) {
					// 每次遇到新的年份開始，放入上一輪年的結果
					putDistinctYearData(distinctYear, latestUpdateTime, lastestUpdateEmp);
				}
				
				// 新的一個年度的開始：資料的設定
				distinctYear = yearAD;
				latestUpdateTime = ts;
				lastestUpdateEmp = getLastUpdateEmp(empNo, titaVo);
			}
		}
		// 放入最後一個年度的結果
		putDistinctYearData(distinctYear, latestUpdateTime, lastestUpdateEmp);

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdWorkMonth != null && slCdWorkMonth.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
	
	private String getLastUpdateEmp(String empNo, TitaVo titaVo) throws LogicException {
		
		String empName = null;
		
		CdEmp tCdEmp = sCdEmpService.findById(empNo, titaVo);
		if(tCdEmp != null)
			empName = tCdEmp.getFullname();
		
		empName = empName.equals("") ? empNo : empName;
		
		return empNo + " " + empName;
	}
	
	private void putDistinctYearData(int distinctYear, Timestamp latestUpdateTime, String lastestUpdateEmp) {

		OccursList occursList = new OccursList();
		// put LastestUpdate: ROC year
		occursList.putParam("OOYear", distinctYear - 1911);
		// put LastestUpdateDateTime: Date & Time
		String lastUpdate = parse.timeStampToStringDate(latestUpdateTime) + " " + parse.timeStampToStringTime(latestUpdateTime);
		occursList.putParam("OOLastUpdate", lastUpdate);
		// put LatestUpdateEmployee: EmpNo + EmpName
		occursList.putParam("OOLastEmp", lastestUpdateEmp);
		// 加入 occursList
		this.totaVo.addOccursList(occursList);
	}
}