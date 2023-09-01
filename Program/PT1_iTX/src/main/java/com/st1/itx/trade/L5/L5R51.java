package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.domain.CustMain;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

@Service("L5R51")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R51 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public NegCom sNegCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// 查詢 債協交易專用
		this.info("active L5R51 ");
		this.totaVo.init(titaVo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		String custId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String custNo = titaVo.getParam("RimCustNo").trim();// 戶號
		String caseSeq = titaVo.getParam("RimCaseSeq").trim();// 案件序號
		String custName = "";
		String errorFg = titaVo.getParam("RimErrorFg").trim();// 是否踢錯誤訊息
		String icustId = "";
		int iCustNo = 0;
		if (custNo != null && custNo.length() != 0) {
			iCustNo = parse.stringToInteger(custNo);
		}

		int iCaseSeq = 0;
		if (caseSeq != null && caseSeq.length() != 0) {
			iCaseSeq = parse.stringToInteger(caseSeq);
		}
//		this.info("***L5R51**key="+custId+","+custNo+","+caseSeq+","+errorFg);
//		this.info("***iCustNo="+iCustNo+",iCaseSeq="+iCaseSeq);
		// 先檢查債協主檔
		NegMain negMainVO = new NegMain();
		CustMain custMainVO = new CustMain();
		NegMainId NegMainIdVO = new NegMainId();

		if (iCustNo > 0) {
			if (iCaseSeq == 0) {
				if (iCustNo > 9990000) {// 保證人或保貸戶
					negMainVO = sNegMainService.custNoFirst(iCustNo, titaVo);
					if (negMainVO != null) {
						icustId = negMainVO.getNegCustId();
						iCaseSeq = negMainVO.getCaseSeq();
						custName = negMainVO.getNegCustName();
					}else if(errorFg.length() != 0){//非新增踢錯誤訊息
						throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
					}
					
				} else {// 非保證人或保貸戶
					custMainVO = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
					if (custMainVO != null) {
						custName = custMainVO.getCustName();
						icustId = custMainVO.getCustId();
						negMainVO = sNegMainService.custNoFirst(iCustNo, titaVo);
						if (negMainVO != null) {
							iCaseSeq = negMainVO.getCaseSeq();
						}else if(errorFg.length() != 0){//非新增踢錯誤訊息
							throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
						}
					}else {
						throw new LogicException(titaVo, "E0001", "客戶主檔");
					}
				}
			} else {
				NegMainIdVO.setCustNo(iCustNo);
				NegMainIdVO.setCaseSeq(iCaseSeq);
				negMainVO = sNegMainService.findById(NegMainIdVO, titaVo);
				if (negMainVO != null) {
					if (!"1".equals(negMainVO.getCustLoanKind())) {// 保證人或保貸戶
						icustId = negMainVO.getNegCustId();
						custName = negMainVO.getNegCustName();
					} else {
						custMainVO = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
						if (custMainVO != null) {
							icustId = custMainVO.getCustId();
							custName = custMainVO.getCustName();
						}
					}
				}else{
					throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
				}
			}
			if(!"".equals(icustId)) {
				custId = icustId;
			}
		}

//		this.info("***custId="+custId+",length="+custId.length()+",iCustNo="+iCustNo);
		if (custId != null && custId.length() != 0 && iCustNo == 0) {
			if (iCaseSeq == 0) {
				custMainVO = sCustMainService.custIdFirst(custId, titaVo);
				if (custMainVO != null) {// 先找客戶檔
					iCustNo = custMainVO.getCustNo();
					custName = custMainVO.getCustName();
					negMainVO = sNegMainService.custNoFirst(iCustNo, titaVo);
					if (negMainVO != null) {
						iCaseSeq = negMainVO.getCaseSeq();
					}
					if(iCustNo==0) {
						negMainVO = sNegMainService.negCustIdFirst(custId, titaVo);
						if (negMainVO != null) {// 保證人或保貸戶
							iCustNo = negMainVO.getCustNo();
							iCaseSeq = negMainVO.getCaseSeq();
							custName = negMainVO.getNegCustName();
						}else if(errorFg.length() != 0){//非新增踢錯誤訊息
							throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
						}
					}
				} else {// 再找債協主檔
					negMainVO = sNegMainService.negCustIdFirst(custId, titaVo);
					if (negMainVO != null) {// 保證人或保貸戶
						iCustNo = negMainVO.getCustNo();
						iCaseSeq = negMainVO.getCaseSeq();
						custName = negMainVO.getNegCustName();
					}else if(errorFg.length() != 0){//非新增踢錯誤訊息
						throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
					}
				}
			} else {
				custMainVO = sCustMainService.custIdFirst(custId, titaVo);
				if (custMainVO != null) {// 先找客戶檔
					iCustNo = custMainVO.getCustNo();
					custName = custMainVO.getCustName();
					if(iCustNo==0) {
						negMainVO = sNegMainService.negCustIdFirst(custId, titaVo);
						if (negMainVO != null ) {
							iCustNo = negMainVO.getCustNo();
							custName = negMainVO.getNegCustName();
						}
					}
				} else {// 債協主檔
					negMainVO = sNegMainService.negCustIdFirst(custId, titaVo);
					if (negMainVO != null) {
						iCustNo = negMainVO.getCustNo();
						custName = negMainVO.getNegCustName();
					}
				}
				if(iCustNo == 0 ) {//有輸入序號則應找到資料
					throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
				}
				NegMainIdVO.setCustNo(iCustNo);
				NegMainIdVO.setCaseSeq(iCaseSeq);
				negMainVO = sNegMainService.findById(NegMainIdVO, titaVo);
				if(negMainVO == null) {//該序號找不到資料
					throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
				}
			}

		} else if (iCustNo == 0) {
			// E0001 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "[身分證字號]或[戶號]須擇一填寫");
		}

//		NegMainIdVO.setCustNo(iCustNo);
//		NegMainIdVO.setCaseSeq(iCaseSeq);
//		negMainVO = sNegMainService.findById(NegMainIdVO, titaVo);
//		if (negMainVO != null) {
//			totaVo.putParam("L5r01CustId", custId);//
//			totaVo.putParam("L5r01CustNo", NegMainVO.getCustNo());//
//			totaVo.putParam("L5r01CaseSeq", NegMainVO.getCaseSeq());//
//			totaVo.putParam("L5r01CaseKindCode", NegMainVO.getCaseKindCode());//
//			totaVo.putParam("L5r01CustLoanKind", NegMainVO.getCustLoanKind());//
//			totaVo.putParam("L5r01Status", NegMainVO.getStatus());//
//			if (NegMainVO.getDeferYMStart() == 0) {
//				totaVo.putParam("L5r01DeferYMStart", NegMainVO.getDeferYMStart());//
//			} else {
//				totaVo.putParam("L5r01DeferYMStart", NegMainVO.getDeferYMStart() - 191100);//
//			}

//			if (NegMainVO.getDeferYMStart() == 0) {
//				totaVo.putParam("L5r01DeferYMEnd", NegMainVO.getDeferYMEnd());//
//			} else {
//				totaVo.putParam("L5r01DeferYMEnd", NegMainVO.getDeferYMEnd() - 191100);//
//			}

//			totaVo.putParam("L5r01ApplDate", NegMainVO.getApplDate());//
//			totaVo.putParam("L5r01DueAmt", NegMainVO.getDueAmt());//
//			totaVo.putParam("L5r01TotalPeriod", NegMainVO.getTotalPeriod());//
//			totaVo.putParam("L5r01IntRate", NegMainVO.getIntRate());//
//			totaVo.putParam("L5r01FirstDueDate", NegMainVO.getFirstDueDate());//
//			totaVo.putParam("L5r01LastDueDate", NegMainVO.getLastDueDate());//
//			totaVo.putParam("L5r01NextPayDate", NegMainVO.getNextPayDate());//
//			totaVo.putParam("L5r01IsMainFin", NegMainVO.getIsMainFin());//
//			totaVo.putParam("L5r01TotalContrAmt", NegMainVO.getTotalContrAmt());//
//			totaVo.putParam("L5r01MainFinCode", NegMainVO.getMainFinCode());//
//			totaVo.putParam("L5r01PrincipalBal", NegMainVO.getPrincipalBal());//
//			totaVo.putParam("L5r01TwoStepCode", NegMainVO.getTwoStepCode());//
//			totaVo.putParam("L5r01PayerCustNo", NegMainVO.getPayerCustNo());//
//			totaVo.putParam("L5r01ChgCondDate", NegMainVO.getChgCondDate());//
//			totaVo.putParam("L5r01CourtCode", NegMainVO.getCourtCode());//
//		} else {
			// E0001 查詢資料不存在
//			throw new LogicException(titaVo, "E0001", "查無資料");
//		}

		totaVo.putParam("L5r51CustNo", iCustNo);
		totaVo.putParam("L5r51CustId", custId);
		totaVo.putParam("L5r51CustName", custName);
		totaVo.putParam("L5r51CaseSeq", iCaseSeq);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}