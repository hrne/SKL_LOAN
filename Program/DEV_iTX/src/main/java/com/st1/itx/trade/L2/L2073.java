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
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2073")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2073 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustDataCtrlService sCustDataCtrlService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public CdEmpService sCdEmpService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	MakeReport makeReport;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2073 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 136 * 400 = 54400

		// tita
		// 統編
		String iCustId = titaVo.getParam("CustId");
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// 宣告
		Timestamp ts;

		// new table
		CustMain tCustMain = new CustMain();
		// new ArrayList
		List<CustDataCtrl> lCustDateCtrl = new ArrayList<CustDataCtrl>();
		Slice<CustDataCtrl> slCustDateCtrl = null;
		// 統編有輸入
		if (!iCustId.isEmpty()) {
			tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
			iCustNo = tCustMain.getCustNo();
			slCustDateCtrl = sCustDataCtrlService.findCustNo(iCustNo, this.index, this.limit, titaVo);
			lCustDateCtrl = slCustDateCtrl == null ? null : slCustDateCtrl.getContent();
			// 統編沒輸入
		} else if (iCustNo > 0) {
			slCustDateCtrl = sCustDataCtrlService.findCustNo(iCustNo, this.index, this.limit, titaVo);

			lCustDateCtrl = slCustDateCtrl == null ? null : slCustDateCtrl.getContent();
		} else {
			slCustDateCtrl = sCustDataCtrlService.findAll(this.index, this.limit, titaVo);

			lCustDateCtrl = slCustDateCtrl == null ? null : slCustDateCtrl.getContent();
		}
		// 查無資料 拋錯
		if (lCustDateCtrl == null) {
			throw new LogicException(titaVo, "E2003", "L2073" + "不存在於結清戶個資控管檔。");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCustDateCtrl != null && slCustDateCtrl.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (CustDataCtrl tCustDateCtrl : lCustDateCtrl) {
			
			String lastUpdate = "";

			// new occurs
			OccursList occurslist = new OccursList();
			// new table
			tCustMain = new CustMain();
			String custUKey = tCustDateCtrl.getCustUKey();
			tCustMain = sCustMainService.findById(custUKey, titaVo);
			if (tCustMain == null) {
				continue;
			}

			if (tCustDateCtrl.getLastUpdate() != null) {

				// 宣告
				ts = tCustDateCtrl.getLastUpdate();
				this.info("ts = " + ts);
				DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
				DateFormat sdftime = new SimpleDateFormat("HH:mm:ss");

				String lastUpdateDate = makeReport.showRocDate(sdfdate.format(ts), 1);
				String lastUpdateTime = sdftime.format(ts);
				this.info("createDate = " + lastUpdateDate);
				this.info("createTime = " + lastUpdateTime);
				lastUpdate = lastUpdateDate + " " + lastUpdateTime;

			}
			String TlrNo = "";
			String EmpName = "";
			CdEmp tCdEmp = new CdEmp();

			if (tCustDateCtrl.getLastUpdateEmpNo() != null) {
				TlrNo = tCustDateCtrl.getLastUpdateEmpNo();
				tCdEmp = sCdEmpService.findById(TlrNo, titaVo);
				if (tCdEmp != null) {
					EmpName = tCdEmp.getFullname();
				}
			}

			occurslist.putParam("OOCustId", tCustMain.getCustId());
			occurslist.putParam("OOCustNo", tCustDateCtrl.getCustNo());
			occurslist.putParam("OOCustName", tCustMain.getCustName());
			occurslist.putParam("OOTlrNo", TlrNo);
			occurslist.putParam("OOEmpName", EmpName);
			occurslist.putParam("OOLastUpdate", lastUpdate);
			occurslist.putParam("OOReason", tCustDateCtrl.getReason());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}