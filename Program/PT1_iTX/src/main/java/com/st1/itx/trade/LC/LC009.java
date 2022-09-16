package com.st1.itx.trade.LC;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.springjpa.cm.LC009ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("LC009")
@Scope("prototype")
/**
 * 報表及製檔
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC009 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private TxFileService txFileService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	LC009ServiceImpl lc009ServiceImpl;

	@Autowired
	private DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC009 " + this.getTxBuffer().getTxCom().getTlrLevel() + "/" + this.getTxBuffer().getTxCom().getTlrDept());
		titaVo.setDataBaseOnLine();
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		List<Map<String, String>> dList = null;

		try {
			dList = lc009ServiceImpl.findAll(titaVo, index, limit);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", e.toString());
		}

		if (this.index == 0 && (dList == null || dList.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "報表及檔案");
		}

		for (Map<String, String> dVo : dList) {
			OccursList occursList = new OccursList();
			occursList.putParam("Sno", dVo.get("FileNo"));
			occursList.putParam("Type", dVo.get("FileType"));
			occursList.putParam("Ymd", dVo.get("FileDate"));
			occursList.putParam("Code", dVo.get("FileCode"));
			occursList.putParam("Item", dVo.get("FileItem"));
			occursList.putParam("CreTlrNo", dVo.get("CreateEmpNo") + ' ' + dVo.get("EmpName"));
			occursList.putParam("TlrNo", dVo.get("TlrNo") + ' ' + dVo.get("TlrName"));
			occursList.putParam("SupNo", dVo.get("SupNo") + ' ' + dVo.get("SupName"));
			occursList.putParam("SignCode", dVo.get("SignCode"));
			occursList.putParam("CalDate", parse.stringToStringDate(dVo.get("CreateDate")));
			occursList.putParam("CalTime", parse.stringToStringTime(dVo.get("CreateDate")));
			occursList.putParam("ServerIp", dVo.get("ServerIp"));
			occursList.putParam("Printer", dVo.get("Printer"));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (dList != null && dList.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		titaVo.setDataBaseOnOrg();

		this.addList(this.totaVo);
		return this.sendList();
	}

}