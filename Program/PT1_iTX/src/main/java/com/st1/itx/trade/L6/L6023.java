package com.st1.itx.trade.L6;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L6023ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6023")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6023 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L6023ServiceImpl l6023ServiceImpl;
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6023 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iLandOfficeCode = titaVo.getParam("LandOfficeCode");
		String iRecWord = titaVo.getParam("RecWord");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 64 * 200 = 12,800

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l6023ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		int wkCnt = 0;
		this.info("resultList = " + resultList);
		if (resultList != null && resultList.size() != 0) {

			this.info("Size =" + resultList.size());

			for (Map<String, String> result : resultList) {

				OccursList occurslist = new OccursList();

				Timestamp lastUpdateTime = parse.StringToSqlDateO(result.get("LastUpdate"), result.get("LastUpdate"));

				occurslist.putParam("OOLandOfficeCode", result.get("LandOfficeCode"));
				occurslist.putParam("OORecWord", result.get("RecWord"));
				occurslist.putParam("OORecWordItem", result.get("RecWordItem"));
				occurslist.putParam("OOLastUpdate", parse.timeStampToStringDate(lastUpdateTime)+ " " +parse.timeStampToStringTime(lastUpdateTime));
				occurslist.putParam("OOLastEmp",
						result.get("LastUpdateEmpNo") + " " + empName(titaVo, result.get("LastUpdateEmpNo")));
				/* 將每筆資料放入Tota的OcList */
				wkCnt++;
				this.totaVo.addOccursList(occurslist);

			}
		}

		if (wkCnt == 0) {
			throw new LogicException(titaVo, "E0001", "地政收件字檔"); // 查無資料
		}

		if (resultList != null && resultList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = sCdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}