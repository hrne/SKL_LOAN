package com.st1.itx.trade.L2;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * END=X,1<br>
 */

@Service("L2072")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2072 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2072.class);

	
	@Autowired
	public CdEmpService sCdEmpService;
	
	/* DB服務注入 */
	@Autowired
	public CustRmkService sCustRmkService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2072 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 29 * 500 = 14500

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		Timestamp ts;
		Timestamp uts;
		String createDate = "";
		String updateDate = "";
		// new ArrayList
		List<CustRmk> lCustRmk = new ArrayList<CustRmk>();
		Slice<CustRmk> slCustRmk = null;
		// 調所有經辦號碼跟名稱
		List<CdEmp> lCdEmp = new ArrayList<CdEmp>();
		Slice<CdEmp> slCdEmp = null;
		
		slCdEmp = sCdEmpService.findAll(this.index, this.limit, titaVo);
		lCdEmp = slCdEmp == null ? null : slCdEmp.getContent();
		
		if (lCdEmp == null) {
			throw new LogicException(titaVo, "", "不存在使用者資料。");
		}
		
		
		// PK
//		CustRmkId CustRmkId = new CustRmkId();
		// 測試該戶號是否有資料存在顧客控管警訊檔
		if (iCustNo > 0) {
			slCustRmk = sCustRmkService.findCustNo(iCustNo, this.index, this.limit, titaVo);
			lCustRmk = slCustRmk == null ? null : slCustRmk.getContent();
		} else {
			slCustRmk = sCustRmkService.findAll(this.index, this.limit, titaVo);
			lCustRmk = slCustRmk == null ? null : slCustRmk.getContent();
		}

		if (lCustRmk == null) {
			throw new LogicException(titaVo, "E2003", "L2072 該戶號" + iCustNo + "不存在顧客控管警訊檔。");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCustRmk != null && slCustRmk.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		
		Boolean checkno = false;
		for (CustRmk tCustRmk : lCustRmk) {
			this.info("tCustRmk---->" + tCustRmk);
			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOCustNo", tCustRmk.getCustNo());
			occurslist.putParam("OORmkNo", tCustRmk.getRmkNo());
			occurslist.putParam("OORmkCode", tCustRmk.getRmkCode());
			occurslist.putParam("OORmkDesc", tCustRmk.getRmkDesc());
			
			String tempEmpNo = tCustRmk.getCreateEmpNo() == "" ? tCustRmk.getLastUpdateEmpNo() : tCustRmk.getCreateEmpNo();
			String updateEmpNo = tCustRmk.getLastUpdateEmpNo() == "" ? tCustRmk.getCreateEmpNo() : tCustRmk.getLastUpdateEmpNo();
			

			occurslist.putParam("OOEmpNo",tempEmpNo);
			this.info("OOEmpNo---->" + tempEmpNo);
			for(CdEmp tCdEmp: lCdEmp) {
				if(tCdEmp.getEmployeeNo().equals(tempEmpNo)) {
					occurslist.putParam("OOEmpName",tCdEmp.getFullname());
					this.info("OOEmpName---->" + tCdEmp.getFullname());
					checkno = true;
				}
			} // for
			
			if(checkno) {
				checkno = false;
			} else {
				occurslist.putParam("OOEmpName","          ");				
			}
			
			
			occurslist.putParam("OOUpdateEmpNo",updateEmpNo);
			this.info("OOUpdateEmpNo---->" + updateEmpNo);
			for(CdEmp tCdEmp: lCdEmp) {
				if(tCdEmp.getEmployeeNo().equals(updateEmpNo)) {
					occurslist.putParam("OOUpdateEmpName",tCdEmp.getFullname());
					this.info("OOUpdateEmpName---->" + tCdEmp.getFullname());
					checkno = true;
				}
			}

			if(checkno) {
				checkno = false;
			} else {
				occurslist.putParam("OOUpdateEmpName","          ");				
			}
			
			// 宣告
			ts = tCustRmk.getCreateDate();
			uts = tCustRmk.getLastUpdate();
			this.info("ts = " + ts);
			this.info("uts = " + uts);
			DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");

			createDate = sdfdate.format(ts);
			updateDate = sdfdate.format(uts);
			
			createDate = parse.IntegerToString(parse.stringToInteger(createDate) - 19110000, 8);
			updateDate = parse.IntegerToString(parse.stringToInteger(updateDate) - 19110000, 8);
			this.info("createDate = " + createDate);
			this.info("updateDate = " + updateDate);
			
			occurslist.putParam("OOCreateDate",createDate);
			occurslist.putParam("OOLastUpdate",updateDate);
			
			
//			occurslist.putParam("OOEmpName", tCustRmk.get
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}