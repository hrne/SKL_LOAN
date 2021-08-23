package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.CustMain;

/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.springjpa.cm.L5075ServiceImpl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.NegCom;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * IsMainFin=9,1<br>
 * WorkSubject=9,1<br>
 * NextPayDate=9,7<br>
 * CustId=X,10<br>
 */

@Service("L5075")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5075 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public NegCom sNegCom;

	@Autowired
	private L5075ServiceImpl l5075ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5075");
		this.info("active L5075 ");
		this.totaVo.init(titaVo);

		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		//this.limit=Integer.MAX_VALUE;//查全部
		this.limit=100;//查全部
		
//		String IsMainFin=titaVo.getParam("IsMainFin").trim(); //是否為最大債權 1:Y;2:N
//		String WorkSubject=titaVo.getParam("WorkSubject").trim(); //作業項目 1:滯繳(時間到未繳);2:應繳(通通抓出來);3即將到期(本金餘額<=三期期款)
		String NextPayDate = titaVo.getParam("NextPayDate").trim(); // 1:滯繳- 逾期基準日;2:應繳-下次應繳日
//		String CustId=titaVo.getParam("CustId").trim(); //員工編號

		int YYYYmmNextPayDate = Integer.parseInt(NextPayDate);

		List<NegMain> lNegMain = l5075ServiceImpl.findData(titaVo, this.index, this.limit);
		if (lNegMain != null && lNegMain.size() != 0) {
			// 有找到資料
			for (NegMain NegMainVO : lNegMain) {
				int ThisCustNo = NegMainVO.getCustNo();
				CustMain CustMainVO = sCustMainService.custNoFirst(ThisCustNo, ThisCustNo, titaVo);
				String ThisCustId = CustMainVO.getCustId();
				String ThisCustName = CustMainVO.getCustName();

				NegTrans NegTransVO = new NegTrans();

				Slice<NegTrans> slNegTransVO = sNegTransService.custNoEq(ThisCustNo, this.index, this.limit, titaVo);
				List<NegTrans> lNegTransVO = slNegTransVO == null ? null : slNegTransVO.getContent();
				String NegTransAcDate = "";
				String NegTransTitaTlrNo = "";
				String NegTransTitaTxtNo = "";
				if (lNegTransVO != null && lNegTransVO.size() != 0) {
					NegTransVO = lNegTransVO.get(0);
					NegTransAcDate = String.valueOf(NegTransVO.getAcDate());
					NegTransTitaTlrNo = String.valueOf(NegTransVO.getTitaTlrNo());
					NegTransTitaTxtNo = String.valueOf(NegTransVO.getTitaTxtNo());
				}

				Map<String, String> ShouldPayMap = sNegCom.NegMainSholdPay(NegMainVO, YYYYmmNextPayDate);
				String OOPayTerm = ShouldPayMap.get("OOPayTerm");// 應還期數
				String OOPayAmt = ShouldPayMap.get("OOPayAmt");// 應繳金額
				String OOOverDueAmt = ShouldPayMap.get("OOOverDueAmt");// 應催繳金額
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustId", ThisCustId);// 身份證字號
				occursList.putParam("OOCustNo", ThisCustNo);// 戶號
				occursList.putParam("OOCaseSeq", NegMainVO.getCaseSeq());// 案件序號
				occursList.putParam("OOCustName", ThisCustName);// 戶名
				occursList.putParam("OODueAmt", NegMainVO.getDueAmt());// 期款金額
				occursList.putParam("OOPayIntDate", NegMainVO.getPayIntDate());// 繳息迄日
				occursList.putParam("OONextPayDate", NegMainVO.getNextPayDate());// 應繳日期
				occursList.putParam("OOPayTerm", OOPayTerm);// 應還期數 由L5075-NegCom計算
				occursList.putParam("OOPayAmt", OOPayAmt);// 應繳金額 由L5075-NegCom計算
				occursList.putParam("OOAccuOverAmt", NegMainVO.getAccuOverAmt());// 累溢收
				occursList.putParam("OOOverDueAmt", OOOverDueAmt);// 應催繳金額 由L5075-NegCom計算
				occursList.putParam("OOAccuDueAmt", NegMainVO.getAccuDueAmt());// 已繳期金
				occursList.putParam("OODeferYMStart", NegMainVO.getDeferYMStart());// 延期年月(起)
				occursList.putParam("OODeferYMEnd", NegMainVO.getDeferYMEnd());// 延期年月(訖)
				occursList.putParam("OOAcDate", NegTransAcDate);// 會計日期
				occursList.putParam("OOTitaTlrNo", NegTransTitaTlrNo);// 經辦
				occursList.putParam("OOTitaTxtNo", NegTransTitaTxtNo);// 交易序號
				occursList.putParam("OOTwoStepCode", NegMainVO.getTwoStepCode());// 二階段註記
				occursList.putParam("OOLastDueDate", NegMainVO.getLastDueDate());// 還款結束日

				occursList.putParam("OOPrincipalBal", NegMainVO.getPrincipalBal());// 總本金餘額
				// 剩餘期數 = (總本金餘額 - 累溢收) / 期款金額
				BigDecimal Temptimes = new BigDecimal("0");
				this.info("NegMainVO.getPrincipalBal=" + NegMainVO.getPrincipalBal());
				this.info("NegMainVO.getAccuOverAmt=" + NegMainVO.getAccuOverAmt());
				this.info("NegMainVO.getDueAmt=" + NegMainVO.getDueAmt());
				Temptimes = NegMainVO.getPrincipalBal().subtract(NegMainVO.getAccuOverAmt());
				// 餘數無條件進位
				Temptimes = Temptimes.divide(NegMainVO.getDueAmt(), 2);
				Temptimes = Temptimes.setScale(0, BigDecimal.ROUND_UP);
				int times = Temptimes.intValue();
				occursList.putParam("OOTimes", times);// 剩餘期數
				
				this.totaVo.addOccursList(occursList);
			}
			
			if(lNegMain != null && lNegMain.size()>=this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			
			
		} else {
			// E2003 查無資料
			throw new LogicException("E2003", "債務協商案件主檔");
		}

		/*
		 * #OOCustId #OOCustNo #OOCaseSeq #OOCustName #OODueAmt #OOPayIntDate
		 * #OONextPayDate #OOPayTerm #OOPayAmt #OOAccuOverAmt #OOOverDueAmt
		 * #OOAccuDueAmt #DeferYMStart #DeferYMEnd #OOAcDate #OOTitaTlrNo #OOTitaTxtNo
		 * 
		 * 
		 */
		this.addList(this.totaVo);
		return this.sendList();
	}
}