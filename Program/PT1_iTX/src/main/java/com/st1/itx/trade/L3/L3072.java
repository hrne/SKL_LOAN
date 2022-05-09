package com.st1.itx.trade.L3;

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
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3072")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3072 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public LoanCustRmkService loanCustRmkService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3072 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; // 29 * 500 = 14500

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
		int iBorxNo = parse.stringToInteger(titaVo.getParam("BorxNo"));

		Timestamp ts;
		Timestamp uts;
		String createDate = "";
		String updateDate = "";
		// new ArrayList
		List<LoanCustRmk> loanCustRmk = new ArrayList<LoanCustRmk>();
		Slice<LoanCustRmk> sloanCustRmk = null;

		if (iBorxNo > 0) {
			sloanCustRmk = loanCustRmkService.BorxNoAll(iCustNo, iFacmNo, iBormNo, iBorxNo, this.index, this.limit, titaVo);
		}
		else if (iCustNo > 0) {
			sloanCustRmk = loanCustRmkService.findCustNo(iCustNo, this.index, this.limit, titaVo);
		} else {
			sloanCustRmk = loanCustRmkService.findAll(this.index, this.limit, titaVo);
		}

		loanCustRmk = sloanCustRmk == null ? null : sloanCustRmk.getContent();
		
		if (loanCustRmk == null || loanCustRmk.isEmpty()) {
			throw new LogicException(titaVo, "E2003", "該戶號" + iCustNo + "不存在帳務備忘錄明細檔。"); // 查無資料
		}
		
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sloanCustRmk != null && sloanCustRmk.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		CdEmp tCdEmp = new CdEmp();

		for (LoanCustRmk t : loanCustRmk) {
			this.info("tCustRmk---->" + t);
			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOCustNo", t.getCustNo());
			occurslist.putParam("OOFacmNo", t.getFacmNo());
			occurslist.putParam("OOBormNo", t.getBormNo());
			occurslist.putParam("OOBorxNo", t.getBorxNo());
			occurslist.putParam("OOAcDate", t.getAcDate());
			occurslist.putParam("OORmkNo", t.getRmkNo());
//			occurslist.putParam("OORmkCode", t.getRmkCode());
			occurslist.putParam("OORmkDesc", t.getRmkDesc());

			String tempEmpNo = t.getCreateEmpNo().isEmpty() ? t.getLastUpdateEmpNo() : t.getCreateEmpNo();
			String updateEmpNo = t.getLastUpdateEmpNo().isEmpty() ? t.getCreateEmpNo() : t.getLastUpdateEmpNo();

			occurslist.putParam("OOEmpNo", tempEmpNo);

			occurslist.putParam("OOEmpName", ""); // 建檔人員姓名
			tCdEmp = cdEmpService.findById(tempEmpNo, titaVo);

			if (tCdEmp != null) {
				occurslist.putParam("OOEmpName", tCdEmp.getFullname()); // 建檔人員姓名
			}

			occurslist.putParam("OOUpdateEmpNo", updateEmpNo);

			occurslist.putParam("OOUpdateEmpName", ""); // 更新人員姓名
			tCdEmp = cdEmpService.findById(updateEmpNo, titaVo);

			if (tCdEmp != null) {
				occurslist.putParam("OOUpdateEmpName", tCdEmp.getFullname()); // 更新人員姓名
			}

			// 宣告
			ts = t.getCreateDate();
			uts = t.getLastUpdate();
			this.info("ts = " + ts);
			this.info("uts = " + uts);
			DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");

			createDate = sdfdate.format(ts);
			updateDate = sdfdate.format(uts);

			createDate = parse.IntegerToString(parse.stringToInteger(createDate) - 19110000, 7);
			updateDate = parse.IntegerToString(parse.stringToInteger(updateDate) - 19110000, 7);
			this.info("createDate = " + createDate);
			this.info("updateDate = " + updateDate);

			occurslist.putParam("OOCreateDate", createDate);
			occurslist.putParam("OOLastUpdate", updateDate);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}