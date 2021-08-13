package com.st1.itx.trade.LC;

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
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Service("LC009")
@Scope("prototype")
/**
 * 報表及製檔
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC009 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC009.class);

	/* DB服務注入 */
	@Autowired
	private TxFileService txFileService;

	@Autowired
	private DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
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
		this.limit = 200;

		Slice<TxFile> slTxFile;

		if (iCreateDateStart > 0) {
			slTxFile = txFileService.findByLC009WithCreateDate(iEntdyStart, iEntdyEnd, iBrNo, iTlrNo + "%", iCode + "%",
					"%" + iItem + "%", createDateStart, createDateEnd, this.index, this.limit);
		} else {
			slTxFile = txFileService.findByLC009(iEntdyStart, iEntdyEnd, iBrNo, iTlrNo + "%", iCode + "%",
					"%" + iItem + "%", this.index, this.limit);
		}
		List<TxFile> lTxFile = slTxFile == null ? null : slTxFile.getContent();

		if (lTxFile == null) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			for (TxFile tTxFile : lTxFile) {

				OccursList occursList = new OccursList();
				occursList.putParam("Ymd", tTxFile.getFileDate());
				occursList.putParam("Code", tTxFile.getFileCode().trim());
				occursList.putParam("Item", tTxFile.getFileItem().trim());
				occursList.putParam("TlrNo", tTxFile.getCreateEmpNo());
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