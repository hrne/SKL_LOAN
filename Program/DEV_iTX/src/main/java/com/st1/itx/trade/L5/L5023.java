package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;

@Component("L5023")
@Scope("prototype")

/**
 * 晤談人員明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5023 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5023.class);
	/* 轉型共用工具 */
	@Autowired
	public PfRewardService iPfRewardService;
	@Autowired
	public FacMainService iFacMainService;
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iCreditSysNo = Integer.valueOf(titaVo.getParam("CreditSysNo"));
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		if (iCustNo!=0) {
			Slice<PfReward> iPfReward = iPfRewardService.findByCustNo(iCustNo, this.index, this.limit, titaVo);
			ArrayList<String> iRepeatList = new ArrayList<>(); 
			if (iPfReward != null) {			
				if (iPfReward != null && iPfReward.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}
				
				for (PfReward rePfReward : iPfReward) {
					OccursList occurslist = new OccursList();
					String iCombine = StringUtils.leftPad(String.valueOf(rePfReward.getCustNo()), 7,"0")+StringUtils.leftPad(String.valueOf(rePfReward.getFacmNo()), 3,"0");
					this.info("戶號+額度="+iCombine);
					if(iRepeatList.contains(iCombine)) {
						continue;
					}else {
						iRepeatList.add(iCombine);
						occurslist.putParam("OOCustNo", rePfReward.getCustNo());
						occurslist.putParam("OOFacmNo", rePfReward.getFacmNo());
						occurslist.putParam("OOInterviewerA", rePfReward.getInterviewerA());
						occurslist.putParam("OOInterviewerB", rePfReward.getInterviewerB());
						occurslist.putParam("OOKeyiner", rePfReward.getCreateEmpNo());
						occurslist.putParam("OOCoorgnizer", rePfReward.getCoorgnizer());
						occurslist.putParam("OOInterviewerAX", getEmpName(rePfReward.getInterviewerA(),titaVo));
						occurslist.putParam("OOInterviewerBX", getEmpName(rePfReward.getInterviewerB(),titaVo));
						occurslist.putParam("OOKeyinerX", getEmpName(rePfReward.getCreateEmpNo(),titaVo));
						occurslist.putParam("OOCoorgnizerX", getEmpName(rePfReward.getCoorgnizer(),titaVo));
						this.totaVo.addOccursList(occurslist);
					}
				}
			} else {
				throw new LogicException(titaVo, "E0001", "此戶號查無資料:"+titaVo.getParam("CustNo")); // 查無資料時
			}
		}
		if (iCreditSysNo!=0) {
			Slice<FacMain> iFacMain = null;
			ArrayList<String> aRepeatList = new ArrayList<>(); 
			iFacMain = iFacMainService.facmCreditSysNoRange(iCreditSysNo, iCreditSysNo, 0, 999, this.index, this.limit, titaVo);
			if (iFacMain == null) {
				throw new LogicException(titaVo, "E0001", "此案件編號查無資料:"+titaVo.getParam("CreditSysNo")); // 查無資料時
			}
			int iCount = 0;
			for (FacMain rFacMain:iFacMain) {
				Slice<PfReward> iPfReward = null;
				iPfReward = iPfRewardService.findByCustNo(rFacMain.getCustNo(), this.index, this.limit, titaVo);
				if (iPfReward != null) {
					iCount++;
				}else {
					continue;
				}
				if (iCount == 0) {
					throw new LogicException(titaVo, "E0001", "此案件編號查無資料"); // 查無資料時
				}
				for (PfReward rPfReward:iPfReward) {
					OccursList occurslist = new OccursList();
					String iCombine = StringUtils.leftPad(String.valueOf(rPfReward.getCustNo()), 7,"0")+StringUtils.leftPad(String.valueOf(rPfReward.getFacmNo()), 3,"0");
					this.info("戶號+額度="+iCombine);
					if(aRepeatList.contains(iCombine)) {
						continue;
					}else {
						aRepeatList.add(iCombine);
						occurslist.putParam("OOCustNo", rPfReward.getCustNo());
						occurslist.putParam("OOFacmNo", rPfReward.getFacmNo());
						occurslist.putParam("OOInterviewerA", rPfReward.getInterviewerA());
						occurslist.putParam("OOInterviewerB", rPfReward.getInterviewerB());
						occurslist.putParam("OOKeyiner", rPfReward.getCreateEmpNo());
						occurslist.putParam("OOCoorgnizer", rPfReward.getCoorgnizer());
						occurslist.putParam("OOInterviewerAX", getEmpName(rPfReward.getInterviewerA(),titaVo));
						occurslist.putParam("OOInterviewerBX", getEmpName(rPfReward.getInterviewerB(),titaVo));
						occurslist.putParam("OOKeyinerX", getEmpName(rPfReward.getCreateEmpNo(),titaVo));
						occurslist.putParam("OOCoorgnizerX", getEmpName(rPfReward.getCoorgnizer(),titaVo));
						this.totaVo.addOccursList(occurslist);
					}
				}
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public String getEmpName(String iEmpNo,TitaVo titaVo) {
		String iEmpName ="";
		
		CdEmp iCdEmp = new CdEmp();
		iCdEmp = iCdEmpService.findById(iEmpNo, titaVo);
		
		if(iCdEmp!=null) {
			iEmpName = iCdEmp.getFullname();
		}
		
		return iEmpName;
	}
}
