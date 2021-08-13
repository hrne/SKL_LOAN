package com.st1.itx.trade.L1;

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
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1908")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1908 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1908.class);

	/* DB服務注入 */
	@Autowired
	public CustNoticeService sCustNoticeService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;
	
	@Autowired
	public CustMainService iCustMainService;
	
	@Autowired
	public CdReportService iCdReportService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1908 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; // 25 * 500 = 12500

		// 取tita戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// FacmNo額度編號
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		
		String iCustId = titaVo.getParam("CustId");

		// 宣告new list
		List<CustNotice> lCustNotice = new ArrayList<CustNotice>();
		Slice<CustNotice> slCustNotice = null;
		// 若戶號為空(int == 0 )
		if (iCustNo == 0 && iFacmNo == 0 && iCustId.equals("")) {

			// 查所有客戶通知設定檔
			slCustNotice = sCustNoticeService.findAll(this.index, this.limit, titaVo);
			lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();

		} else if (iCustNo != 0 && iFacmNo != 0) {

			slCustNotice = sCustNoticeService.facmNoEq(iCustNo, iFacmNo, iFacmNo, this.index, this.limit, titaVo);
			lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();

		}  else {
			// 使用者輸入統編查詢，將iCustNo替換為查詢結果
			if (!iCustId.equals("")){
				CustMain iCustMain = new CustMain();
				iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
				if(iCustMain == null ) {
					throw new LogicException("E0001", "客戶檔查無此統一編號:"+iCustId); // 查無資料
				}
				iCustNo = iCustMain.getCustNo();
			}
			slCustNotice = sCustNoticeService.findCustNo(iCustNo, this.index, this.limit, titaVo);
			lCustNotice = slCustNotice == null ? null : slCustNotice.getContent();
		}

		if (lCustNotice == null || lCustNotice.size() == 0) {
			throw new LogicException("E0001", "客戶通知設定檔"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCustNotice != null && slCustNotice.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (CustNotice tCustNotice : lCustNotice) {

			// new occurs
			OccursList occursList = new OccursList();
			String iFormNo = tCustNotice.getFormNo();
			CdReport iCdReport = new CdReport();
			iCdReport = iCdReportService.findById(iFormNo, titaVo);
//			if(iCdReport.getSendCode()!=0) {
//				//CdReport 報送記號不為0才報送
//				occursList.putParam("OOCustNo", tCustNotice.getCustNo());
//				occursList.putParam("OOFacmNo", tCustNotice.getFacmNo());
//				occursList.putParam("OOPaper", tCustNotice.getPaperNotice());
//				occursList.putParam("OOMsg", tCustNotice.getMsgNotice());
//				occursList.putParam("OOEMail", tCustNotice.getEmailNotice());
//				occursList.putParam("OOFormNo", iFormNo);
//				occursList.putParam("OOFormName", iCdReport.getFormName());
//				occursList.putParam("OOApplyDt", tCustNotice.getApplyDate());
//			}else {
//				continue;
//			}		
			occursList.putParam("OOCustNo", tCustNotice.getCustNo());
			occursList.putParam("OOFacmNo", tCustNotice.getFacmNo());
			occursList.putParam("OOPaper", tCustNotice.getPaperNotice());
			occursList.putParam("OOMsg", tCustNotice.getMsgNotice());
			occursList.putParam("OOEMail", tCustNotice.getEmailNotice());
			occursList.putParam("OOFormNo", iFormNo);
			if(iCdReport == null) {
				occursList.putParam("OOFormName", "");
			}else {
				occursList.putParam("OOFormName", iCdReport.getFormName());
			}
			occursList.putParam("OOApplyDt", tCustNotice.getApplyDate());
			
			
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}