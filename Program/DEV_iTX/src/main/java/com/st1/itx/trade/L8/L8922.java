package com.st1.itx.trade.L8;

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
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcDateStart=9,7 AcDateEnd=9,7 Factor=9,2 Type=9,1 END=X,1
 */

@Service("L8922") // 疑似洗錢交易合理性明細檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L8922 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8922.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryDetailService sMlaundryDetailService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;
	
	@Autowired
	public DateUtil dateUtil;
	
	
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8922 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String DateTime; // YYY/MM/DD hh:mm:ss
		String Date = "";
		int iFactor = this.parse.stringToInteger(titaVo.getParam("Factor"));
		int iType = this.parse.stringToInteger(titaVo.getParam("Type"));
		int iAcDateStart = this.parse.stringToInteger(titaVo.getParam("AcDateStart"));
		int iAcDateEnd = this.parse.stringToInteger(titaVo.getParam("AcDateEnd"));
		int iFAcDateStart = iAcDateStart + 19110000;
		int iFAcDateEnd = iAcDateEnd + 19110000;
		int iLevel = this.txBuffer.getTxCom().getTlrLevel();
		this.info("L8922 iFAcDate : " + iFAcDateStart + "~" + iFAcDateEnd);
		this.info("L8922 iLevel : " + iLevel);
		
		int retxdate = 0;
		
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 461 * 100 = 46,100

		// 查詢疑似洗錢交易合理性明細檔檔
		Slice<MlaundryDetail> slMlaundryDetail;
		slMlaundryDetail = sMlaundryDetailService.findbyDate(iFAcDateStart, iFAcDateEnd, this.index, this.limit, titaVo);
		List<MlaundryDetail> lMlaundryDetail = slMlaundryDetail == null ? null : slMlaundryDetail.getContent();

		if (lMlaundryDetail == null || lMlaundryDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "疑似洗錢交易合理性明細檔"); // 查無資料
		}
		// 如有找到資料
		for (MlaundryDetail tMlaundryDetail : lMlaundryDetail) {
			OccursList occursList = new OccursList();
			
			// 0:全部;1:樣態1;2:樣態2;3:樣態3 -> 不等時找下一筆
			if (!(iFactor == 0 ) && !(iFactor == tMlaundryDetail.getFactor())) {
				continue;
				}
			
			
			switch(iType) {
			
			case 1:
				// 查詢種類 1:合理性;
				break;
				
			case 2:
				//2:延遲交易確認
				
				dateUtil.init();
				
				if(tMlaundryDetail.getManagerDate()!=0) {
					retxdate = dateUtil.getbussDate(tMlaundryDetail.getManagerDate(), -4);
					this.info("延期交易日="+retxdate);
					//延遲交易確認=依據[主管同意日期] >=入帳日＋4營業日
					if(!(retxdate>= tMlaundryDetail.getEntryDate())) {
						continue;
					}	
				}
				break; 
			}		
			
			
			switch(iLevel) {
			
				case 1:
					//主管
					//經辦未輸入合理性,主管查不出來
					this.info("L8922 Rational"+tMlaundryDetail.getRational());
					if((" ").equals(tMlaundryDetail.getRational())) {
						this.info("Rational 空白");
						continue;
					}
					break;
				
				case 3:
					//經辦
					break; 
			}


			// 查詢客戶資料主檔
			CustMain tCustMain = new CustMain();
			tCustMain = sCustMainService.custNoFirst(tMlaundryDetail.getCustNo(), tMlaundryDetail.getCustNo(), titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查無資料
			}
			

			occursList.putParam("OOAcDate", tMlaundryDetail.getEntryDate()); // 入帳日期
			occursList.putParam("OOFactor", tMlaundryDetail.getFactor()); // 交易樣態
			occursList.putParam("OOCustNo", tMlaundryDetail.getCustNo()); // 戶號
			occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名
//			occursList.putParam("OOFacmNo", tMlaundryDetail.getFacmNo()); // 額度編號
//			occursList.putParam("OOBormNo", tMlaundryDetail.getBormNo()); // 撥款序號
			occursList.putParam("OOTotalCnt", tMlaundryDetail.getTotalCnt()); // 累積筆數
			occursList.putParam("OOTotalAmt", tMlaundryDetail.getTotalAmt()); // 累積金額
//			occursList.putParam("OOMemoSeq", tMlaundryDetail.getMemoSeq()); // 備忘錄序號
			occursList.putParam("OORational", tMlaundryDetail.getRational()); // 合理性記號
			occursList.putParam("OOEmpNoDesc", tMlaundryDetail.getEmpNoDesc().replace("$n", "")); // 經辦合理性說明
			occursList.putParam("OOManagerDesc", tMlaundryDetail.getManagerDesc().replace("$n", "")); // 主管覆核說明
			occursList.putParam("OOEmpNo", tMlaundryDetail.getLastUpdateEmpNo()); // 經辦
			occursList.putParam("OOManagerCheck", tMlaundryDetail.getManagerCheck()); // 主管覆核
			if(tMlaundryDetail.getManagerDate()==0) {
				occursList.putParam("OOManagerDate", ""); // 主管同意日期
			} else {
				occursList.putParam("OOManagerDate", tMlaundryDetail.getManagerDate()); // 主管同意日期
			}
			
			DateTime = this.parse.timeStampToString(tMlaundryDetail.getLastUpdate()); // 異動日期
			this.info("L8922 DateTime : " + DateTime);
			Date = FormatUtil.left(DateTime, 9);
			occursList.putParam("OOUpdate", Date);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slMlaundryDetail != null && slMlaundryDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}