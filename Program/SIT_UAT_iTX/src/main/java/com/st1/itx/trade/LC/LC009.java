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
		this.info("active LC009 " + this.getTxBuffer().getTxCom().getTlrLevel() + "/"
				+ this.getTxBuffer().getTxCom().getTlrDept());
		
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
		
		if (dList == null || dList.size() == 0) {
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
		
		this.addList(this.totaVo);
		return this.sendList();
	}

	public ArrayList<TotaVo> run2(TitaVo titaVo) throws LogicException {
		this.info("active LC009 " + this.getTxBuffer().getTxCom().getTlrLevel() + "/"
				+ this.getTxBuffer().getTxCom().getTlrDept());
		this.totaVo.init(titaVo);

		// 2021-04-05 Wei 修改: 日期欄位修改為範圍
		// iEntdyStart 會計日期起日
		// iEntdyEnd 會計日期迄日
		int iEntdyStart = Integer.valueOf(titaVo.get("iEntdyStart").trim()) + 19110000;
		int iEntdyEnd = Integer.valueOf(titaVo.get("iEntdyEnd").trim()) + 19110000;

		String iBrNo = titaVo.get("iBrNo").trim();
		String iTlrNo = titaVo.get("iTlrNo").trim();
		String iCode = titaVo.get("iCode").trim();
		String iItem = titaVo.get("iItem").trim();

		// 2021-01-26 Wei新增
		// 2021-04-05 Wei 修改: 日期欄位修改為範圍
		// iCreateDateStart 製作日期起日(空白時查全部)
		// iCreateDateEnd 製作日期迄日(空白時查全部)
		String xCreateDateStart = titaVo.get("iCreateDateStart").trim();
		String xCreateDateEnd = titaVo.get("iCreateDateEnd").trim();

		int iCreateDateStart = 0;
		int iCreateDateEnd = 0;

		Timestamp createDateStart = null;

		Timestamp createDateEnd = null;

		if (xCreateDateStart != null && !xCreateDateStart.isEmpty()) {

			iCreateDateStart = Integer.parseInt(xCreateDateStart);

			iCreateDateStart += 19110000;

			xCreateDateStart = String.valueOf(iCreateDateStart);

			// 轉java.sql.Timestamp格式
			// yyyy-[m]m-[d]d hh:mm:ss[.f...]
			String timestampFormat = xCreateDateStart.substring(0, 4) + "-" + xCreateDateStart.substring(4, 6) + "-"
					+ xCreateDateStart.substring(6, 8) + " 00:00:00.000000";

			this.info("timestampFormat = " + timestampFormat);

			createDateStart = Timestamp.valueOf(timestampFormat);

			iCreateDateEnd = Integer.parseInt(xCreateDateEnd);

			iCreateDateEnd += 19110000;

			// 因為是TimeStamp
			// 所以要取下一天的0點0分
			dDateUtil.setDate_1(iCreateDateEnd);

			dDateUtil.setDays(1);

			iCreateDateEnd = dDateUtil.getCalenderDay();

			xCreateDateEnd = String.valueOf(iCreateDateEnd);

			// 轉java.sql.Timestamp格式
			// yyyy-[m]m-[d]d hh:mm:ss[.f...]
			String timestampFormat2 = xCreateDateEnd.substring(0, 4) + "-" + xCreateDateEnd.substring(4, 6) + "-"
					+ xCreateDateEnd.substring(6, 8) + " 00:00:00.000000";

			this.info("timestampFormat2 = " + timestampFormat2);

			createDateEnd = Timestamp.valueOf(timestampFormat2);

		}

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		Slice<TxFile> slTxFile;

		if (iCreateDateStart > 0) {
			slTxFile = txFileService.findByLC009WithCreateDate(iEntdyStart, iEntdyEnd, iBrNo, iTlrNo + "%", iCode + "%",
					"%" + iItem + "%", createDateStart, createDateEnd, this.index, this.limit);
		} else {
			slTxFile = txFileService.findByLC009(iEntdyStart, iEntdyEnd, iBrNo, iTlrNo + "%", iCode + "%",
					"%" + iItem + "%", this.index, this.limit);
		}
		List<TxFile> lTxFile = slTxFile == null ? null : new ArrayList<>(slTxFile.getContent());

		// 按分配順序排序
		Collections.sort(lTxFile, new Comparator<TxFile>() {
			public int compare(TxFile c1, TxFile c2) {
				String c1OrderKey = new SimpleDateFormat("yyyyMMdd-HH:mm").format(c1.getCreateDate()) + c1.getFileCode()
						+ new SimpleDateFormat("yyyyMMdd-HH:mm:ss").format(c1.getCreateDate());
				String c2OrderKey = new SimpleDateFormat("yyyyMMdd-HH:mm").format(c2.getCreateDate()) + c2.getFileCode()
						+ new SimpleDateFormat("yyyyMMdd-HH:mm:ss").format(c2.getCreateDate());
				return 0 - c1OrderKey.compareTo(c2OrderKey);
			}
		});

		if (lTxFile == null) {
			throw new LogicException(titaVo, "E0001", "報表及檔案");
		}

		for (TxFile tTxFile : lTxFile) {

			OccursList occursList = new OccursList();
			occursList.putParam("Ymd", tTxFile.getFileDate());
			occursList.putParam("Code", tTxFile.getFileCode().trim());
			occursList.putParam("Item", tTxFile.getFileItem().trim());

			String cretlrno = tTxFile.getCreateEmpNo();
			CdEmp cdEmp = cdEmpService.findById(tTxFile.getCreateEmpNo(), titaVo);
			if (cdEmp != null) {
				cretlrno += " " + cdEmp.getFullname();
			}
			occursList.putParam("CreTlrNo", cretlrno);

			String tlrno = tTxFile.getTlrNo();
			if (!tTxFile.getTlrNo().isEmpty()) {
				cdEmp = cdEmpService.findById(tTxFile.getTlrNo(), titaVo);
				if (cdEmp != null) {
					tlrno += " " + cdEmp.getFullname();
				}
			}
			occursList.putParam("TlrNo", tlrno);

			String supno = tTxFile.getSupNo();
			if (!tTxFile.getSupNo().isEmpty()) {
				cdEmp = cdEmpService.findById(tTxFile.getSupNo(), titaVo);
				if (cdEmp != null) {
					supno += " " + cdEmp.getFullname();
				}
			}
			occursList.putParam("SupNo", supno);

			occursList.putParam("Type", tTxFile.getFileType());
			if (tTxFile.getCreateDate() == null) {
				occursList.putParam("CalDate", "");
				occursList.putParam("CalTime", "");
			} else {
				occursList.putParam("CalDate", dbDateToRocDate(tTxFile.getCreateDate().toString()));
				occursList.putParam("CalTime", dbDateToRocTime(tTxFile.getCreateDate().toString()));
			}
			occursList.putParam("Sno", tTxFile.getFileNo());

			occursList.putParam("SignCode", "0");
			if (tTxFile.getFileType() == 1 && "1".equals(tTxFile.getSignCode())) {

				if (this.txBuffer.getTxCom().getTlrLevel() == 3 && "".equals(tTxFile.getTlrNo())) {
					occursList.putParam("SignCode", "1");
				} else if (this.txBuffer.getTxCom().getTlrLevel() < 3 && !"".equals(tTxFile.getTlrNo())
						&& this.txBuffer.getTxCom().getTlrDept().equals(tTxFile.getGroupNo())) {
					occursList.putParam("SignCode", "1");
				}
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxFile != null && slTxFile.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String dbDateToRocDate(String dbDate) {

		String sysDateY = dbDate.substring(0, 4);
		int rocDate9 = Integer.valueOf(sysDateY) - 1911;

		return String.valueOf(rocDate9) + "/" + dbDate.substring(5, 7) + "/" + dbDate.substring(8, 10);
	}

	private String dbDateToRocTime(String dbDate) {

		return dbDate.substring(11, 19);
	}

}