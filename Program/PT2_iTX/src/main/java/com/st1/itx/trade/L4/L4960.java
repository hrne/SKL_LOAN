package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * SearchFlag=9,1<br>
 * CustNo=9,7<br>
 * CustId=X,10<br>
 * END=X,1<br>
 */

@Service("L4960")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4960 extends TradeBuffer {

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

//	自保續保按鈕控制，僅最新一筆能按
	private HashMap<tmpInsu, Integer> btnShowFlag = new HashMap<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4960 ");
		this.totaVo.init(titaVo);

//		查詢選項  1:戶號;2:統一編號
//		借款人戶號
//		統一編號

		int searchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int custNo = 0;
		String custId = titaVo.getParam("CustId");

		// Input searchFlag: 1.CustNo, 2.CustId
		if (searchFlag == 1) {
			custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		} else if (searchFlag == 2) {
			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custIdFirst(custId);
			custNo = tCustMain.getCustNo();
		}

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<InsuRenew> sInsuRenew = null;

		List<InsuRenew> l0InsuRenew = new ArrayList<InsuRenew>();

		sInsuRenew = insuRenewService.findCustEq(custNo, this.index, this.limit, titaVo);

		l0InsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

//		0.新保 1.自保 2.續保 
		int selfInsuCode = 0;

		if (l0InsuRenew != null && l0InsuRenew.size() != 0) {
			ArrayList<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

			lInsuRenew = new ArrayList<InsuRenew>(l0InsuRenew);

//			InsuYearMonth > InsuStartDate > InsuEndDate  DESC
			lInsuRenew.sort((c1, c2) -> {
				int result = 0;
				if (c1.getInsuYearMonth() - c2.getInsuYearMonth() != 0) {
					result = c2.getInsuYearMonth() - c1.getInsuYearMonth();
				} else if (c1.getInsuStartDate() - c2.getInsuStartDate() != 0) {
					result = c2.getInsuStartDate() - c1.getInsuStartDate();
				} else if (c1.getInsuEndDate() - c2.getInsuEndDate() != 0) {
					result = c2.getInsuEndDate() - c1.getInsuEndDate();
				} else {
					result = 0;
				}
				return result;
			});

			for (InsuRenew tInsuRenew : lInsuRenew) {

				tmpInsu tmp = new tmpInsu(tInsuRenew.getClCode1(), tInsuRenew.getClCode2(), tInsuRenew.getClNo());

				this.info(" tmp ... " + tmp);

//				僅第一筆有按鈕 order by 火險年月
				if (btnShowFlag.containsKey(tmp)) {
					btnShowFlag.put(tmp, 0);
				} else {
					btnShowFlag.put(tmp, 1);
				}

				String entryFlag = "N";
				String transFlag = "N";

				if (tInsuRenew.getAcDate() > 0) {
					entryFlag = "Y";
				}
				if (tInsuRenew.getOvduDate() > 0) {
					transFlag = "Y";
				}
				if (tInsuRenew.getPrevInsuNo() == null || "".equals(tInsuRenew.getPrevInsuNo())) {
					selfInsuCode = 0;
				} else {
					if (tInsuRenew.getRenewCode() == 1) {
						selfInsuCode = 1;
					} else if (tInsuRenew.getRenewCode() == 2) {
						selfInsuCode = 2;
					}
				}

				this.info("EndoInsuNo ... '" + tInsuRenew.getInsuRenewId().getEndoInsuNo() + "'");

				OccursList occursList = new OccursList();

				occursList.putParam("OOInsuYearMonth", tInsuRenew.getInsuYearMonth() - 191100);
				occursList.putParam("OOFacmNo", tInsuRenew.getFacmNo());
				occursList.putParam("OOFireInsuCovrg", tInsuRenew.getFireInsuCovrg());
				occursList.putParam("OOFireInsuPrem", tInsuRenew.getFireInsuPrem());
				occursList.putParam("OOInsuStartDate", tInsuRenew.getInsuStartDate());
				occursList.putParam("OOInsuEndDate", tInsuRenew.getInsuEndDate());
				occursList.putParam("OOEntryFlag", entryFlag);
				occursList.putParam("OOTransFlag", transFlag);
				occursList.putParam("OOClCode1", tInsuRenew.getInsuRenewId().getClCode1());
				occursList.putParam("OOClCode2", tInsuRenew.getInsuRenewId().getClCode2());
				occursList.putParam("OOClNo", tInsuRenew.getInsuRenewId().getClNo());
				occursList.putParam("OOPrevInsuNo", tInsuRenew.getInsuRenewId().getPrevInsuNo());
				occursList.putParam("OOEndoInsuNo", tInsuRenew.getInsuRenewId().getEndoInsuNo());
				occursList.putParam("OOSelfInsuCode", selfInsuCode);
				occursList.putParam("OOStatusCode", tInsuRenew.getStatusCode());
				occursList.putParam("OOAcDate", tInsuRenew.getAcDate());
				occursList.putParam("OOBtnFlag", btnShowFlag.get(tmp));
				occursList.putParam("OOEthqInsuCovrg", tInsuRenew.getEthqInsuCovrg());	//地震險保險金額
				occursList.putParam("OOEthqInsuPrem", tInsuRenew.getEthqInsuPrem());		//地震險保費
				occursList.putParam("OORemark", tInsuRenew.getRemark());		//備註
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private class tmpInsu {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + clCode1;
			result = prime * result + clCode2;
			result = prime * result + clNo;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpInsu other = (tmpInsu) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (clCode1 != other.clCode1)
				return false;
			if (clCode2 != other.clCode2)
				return false;
			if (clNo != other.clNo)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "tmpInsu [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + "]";
		}

		private int clCode1 = 0;
		private int clCode2 = 0;
		private int clNo = 0;

		private tmpInsu(int clCode1, int clCode2, int clNo) {
			this.setClCode1(clCode1);
			this.setClCode2(clCode2);
			this.setClNo(clNo);
		}

		public void setClCode1(int clCode1) {
			this.clCode1 = clCode1;
		}

		public void setClCode2(int clCode2) {
			this.clCode2 = clCode2;
		}

		public void setClNo(int clNo) {
			this.clNo = clNo;
		}

		private L4960 getEnclosingInstance() {
			return L4960.this;
		}
	}
}