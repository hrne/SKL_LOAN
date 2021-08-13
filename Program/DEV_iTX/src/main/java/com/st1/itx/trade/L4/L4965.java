package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * SearchFlag=9,1<br>
 * ClCode1=X,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * ApplNo=9,7<br>
 * NowInsuNo=X,16<br>
 * InsuCompany=X,2<br>
 * InsuTypeCode=X,2<br>
 * InsuEndDateFrom=9,7<br>
 * InsuEndDateTo=9,7<br>
 * END=X,1<br>
 */

@Service("L4965")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4965 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4965.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* DB服務注入 */
	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CustMainService custMainService;

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4965 ");
		this.totaVo.init(titaVo);

		int searchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int clCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int clCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int applNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		int insuEndDateFrom = parse.stringToInteger(titaVo.getParam("InsuEndDateFrom"));
		int insuEndDateTo = parse.stringToInteger(titaVo.getParam("InsuEndDateTo"));

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		FacMain tFacMain = new FacMain();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<InsuRenew> sInsuRenew = null;

		/*
		 * 1:擔保品編號; 2:戶號; 3:核准號螞; 4:保險單號碼; 5:保險公司; 6:保險類別
		 */
		switch (searchFlag) {
		case 1:
			sInsuRenew = insuRenewService.findNowInsuEq(clCode1, clCode2, clNo, this.index, this.limit);
			break;
		case 2:
			if (facmNo == 0) {
				sInsuRenew = insuRenewService.findCustEq(custNo, this.index, this.limit);
			} else {
				sInsuRenew = insuRenewService.findL4965A(custNo, facmNo, this.index, this.limit);
			}
			break;
		case 3:
			tFacMain = facMainService.facmApplNoFirst(applNo);
			if (tFacMain != null) {
				custNo = tFacMain.getCustNo();
				facmNo = tFacMain.getFacmNo();
			} else {
				custNo = 0;
				facmNo = 0;
			}
			sInsuRenew = insuRenewService.findL4965A(custNo, facmNo, this.index, this.limit);
			break;
		case 4:
			sInsuRenew = insuRenewService.findL4965B(titaVo.getParam("NowInsuNo"), this.index, this.limit);
			break;
		case 5:
			sInsuRenew = insuRenewService.findL4965C(titaVo.getParam("InsuCompany"), this.index, this.limit);
			break;
		case 6:
			sInsuRenew = insuRenewService.findL4965D(titaVo.getParam("InsuTypeCode"), this.index, this.limit);
			break;
		default:
			break;
		}

//		this.info("L4965 - b lInsuRenew : " + lInsuRenew);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			for (InsuRenew tInsuRenew : lInsuRenew) {

				int insuEndDate = tInsuRenew.getInsuEndDate();
				this.info("L4965 - A insuEndDate : " + insuEndDate);
				if (insuEndDateFrom <= insuEndDate && insuEndDateTo >= insuEndDate) {
					custNo = tInsuRenew.getCustNo();
					facmNo = tInsuRenew.getFacmNo();
					clCode1 = tInsuRenew.getClCode1();
					clCode2 = tInsuRenew.getClCode2();
					clNo = tInsuRenew.getClNo();

					CustMain tCustMain = new CustMain();
					tCustMain = custMainService.custNoFirst(custNo, custNo);
					String custName = tCustMain.getCustName();

					List<FacMain> lFacMain = new ArrayList<FacMain>();
					Slice<FacMain> sFacMain = null;

					sFacMain = facMainService.facmCustNoRange(custNo, custNo, facmNo, facmNo, this.index, this.limit);

					lFacMain = sFacMain == null ? null : sFacMain.getContent();

					if (lFacMain != null && lFacMain.size() != 0) {
						tFacMain = new FacMain();
						tFacMain = lFacMain.get(0);
						applNo = tFacMain.getApplNo();
					} else {
						applNo = 0;
					}

					OccursList occursList = new OccursList();

					occursList.putParam("OOCustNo", custNo);
					occursList.putParam("OOCustName", custName);
					occursList.putParam("OOFacmNo", facmNo);
					occursList.putParam("OOApplNo", applNo);
					occursList.putParam("OOClCode1", clCode1);
					occursList.putParam("OOClCode2", clCode2);
					occursList.putParam("OOClNo", clNo);
					occursList.putParam("OONowInsuNo", tInsuRenew.getNowInsuNo());
					occursList.putParam("OOInsuCompany", tInsuRenew.getInsuCompany());
					occursList.putParam("OOFireInsuCovrg", tInsuRenew.getFireInsuCovrg());
					occursList.putParam("OOFireInsuPrem", tInsuRenew.getFireInsuPrem());
					occursList.putParam("OOEthqInsuCovrg", tInsuRenew.getEthqInsuCovrg());
					occursList.putParam("OOEthqInsuPrem", tInsuRenew.getEthqInsuPrem());
					occursList.putParam("OOInsuStartDate", tInsuRenew.getInsuStartDate());
					occursList.putParam("OOInsuEndDate", tInsuRenew.getInsuEndDate());

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				}
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}