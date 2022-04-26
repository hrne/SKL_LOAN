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
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * END=X,1<br>
 */

@Service("L4964")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4964 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* DB服務注入 */
	@Autowired
	public InsuRenewService insuRenewService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public InsuOrignalService insuOrignalService;

//	自保續保按鈕控制，僅最新一筆能按
	private HashMap<tmpInsu, Integer> btnShowFlag = new HashMap<>();
	private HashMap<tmpInsu, String> btnInsuNo = new HashMap<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4964 ");
		this.totaVo.init(titaVo);

		List<InsuRenew> l0InsuRenew = new ArrayList<InsuRenew>();
		List<InsuOrignal> lInsuOrignal = new ArrayList<InsuOrignal>();

		int clCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int clCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int State = parse.stringToInteger(titaVo.getParam("State"));

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<InsuRenew> slInsuRenew = null;
		Slice<InsuOrignal> sInsuOrignal = null;

		slInsuRenew = insuRenewService.findNowInsuEq(clCode1, clCode2, clNo, this.index, this.limit);
		sInsuOrignal = insuOrignalService.clNoEqual(clCode1, clCode2, clNo, this.index, this.limit);

		l0InsuRenew = slInsuRenew == null ? null : slInsuRenew.getContent();
		lInsuOrignal = sInsuOrignal == null ? null : sInsuOrignal.getContent();

		String bdLocation;
		ClBuilding tClBuilding = new ClBuilding();
		ClBuildingId tClBuildingId = new ClBuildingId();
		tClBuildingId.setClCode1(clCode1);
		tClBuildingId.setClCode2(clCode2);
		tClBuildingId.setClNo(clNo);

		tClBuilding = clBuildingService.findById(tClBuildingId);
		if (tClBuilding == null) {
			bdLocation = "";
		} else {
			bdLocation = tClBuilding.getBdLocation();
		}

//		0.新保 1.自保 2.續保 
		int selfInsuCode = 0;

		int threeMonthsB4Date = find3MonthsB4();

		this.info("threeMonthsB4Date : " + threeMonthsB4Date);

//		查詢條件
//		1.原始保單及續保檔(查詢3個月前、且AcDate有)
//		2.尚未繳款(Renew無AcDate)

		Boolean occursflg = true;

		if (l0InsuRenew != null && l0InsuRenew.size() != 0) {
			
			ArrayList<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

			lInsuRenew = new ArrayList<InsuRenew>(l0InsuRenew);

//			ClCode1 > ClCode2 > ClNo > InsuMonth DESC
			lInsuRenew.sort((c1, c2) -> {
				int result = 0;
				if (c1.getClCode1() - c2.getClCode1() != 0) {
					result = c1.getClCode1() - c2.getClCode1();
				} else if (c1.getClCode2() - c2.getClCode2() != 0) {
					result = c1.getClCode2() - c2.getClCode2();
				} else if (c1.getClNo() - c2.getClNo() != 0) {
					result = c1.getClNo() - c2.getClNo();
				} else if (c1.getInsuYearMonth() - c2.getInsuYearMonth() != 0) {
					result = c2.getInsuYearMonth() - c1.getInsuYearMonth();
				} else {
					result = 0;
				}
				return result;
			});
			
			
			for (InsuRenew tInsuRenew : lInsuRenew) {

				tmpInsu tmp = new tmpInsu(tInsuRenew.getOrigInsuNo(), tInsuRenew.getInsuCompany());

				this.info("getPrevInsuNo : " + tInsuRenew.getPrevInsuNo());
				this.info("getRenewCode : " + tInsuRenew.getRenewCode());
				this.info("getInsuEndDate : " + tInsuRenew.getInsuEndDate());
				selfInsuCode = tInsuRenew.getRenewCode();
//				僅第一筆有按鈕，或該筆相同的保單號碼且有批單號碼的
				if (btnShowFlag.containsKey(tmp)) {
					btnShowFlag.put(tmp, 0);
					if (btnInsuNo.get(tmp).equals(tInsuRenew.getPrevInsuNo())
							&& !"".equals(tInsuRenew.getEndoInsuNo().trim())) {
						this.info("tInsuRenew.getEndoInsuNo() ... " + tInsuRenew.getEndoInsuNo());
						btnShowFlag.put(tmp, 1);
					}
				} else {
					btnShowFlag.put(tmp, 1);
					btnInsuNo.put(tmp, tInsuRenew.getPrevInsuNo());
				}

				this.info("btnShowFlag ... " + btnShowFlag.get(tmp));

				int InsuYearMonth = tInsuRenew.getInsuYearMonth();
				if (InsuYearMonth > 191201) {
					InsuYearMonth = InsuYearMonth - 191100;
				}

				// 自保、修改、刪除
				this.info("tInsuRenew.getInsuEndDate : " + tInsuRenew.getInsuEndDate());

				if (State == 9) {
					OccursList occursList = new OccursList();
					occursList.putParam("OOPrevInsuNo", tInsuRenew.getPrevInsuNo());
					occursList.putParam("OOEndoInsuNo", tInsuRenew.getEndoInsuNo());
					occursList.putParam("OONowInsuNo", tInsuRenew.getNowInsuNo());
					occursList.putParam("OOSelfInsuCode", selfInsuCode);
					occursList.putParam("OOBdLocation", bdLocation);
					occursList.putParam("OOInsuCompany", tInsuRenew.getInsuCompany());
					occursList.putParam("OOInsuStartDate", tInsuRenew.getInsuStartDate());
					occursList.putParam("OOInsuEndDate", tInsuRenew.getInsuEndDate());
					occursList.putParam("OOInsuTypeCode", tInsuRenew.getInsuTypeCode());
					occursList.putParam("OOFireInsuCovrg", tInsuRenew.getFireInsuCovrg());
					occursList.putParam("OOEthqInsuCovrg", tInsuRenew.getEthqInsuCovrg());
					occursList.putParam("OOFireInsuPrem", tInsuRenew.getFireInsuPrem());
					occursList.putParam("OOEthqInsuPrem", tInsuRenew.getEthqInsuPrem());
					occursList.putParam("OOInsuYearMonth", InsuYearMonth);
					occursList.putParam("OOBtnFlag", btnShowFlag.get(tmp));

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
					occursflg = false;
				} else { // 近三個月、未結案
					if ((tInsuRenew.getInsuEndDate() >= threeMonthsB4Date) || (tInsuRenew.getRenewCode() == 2
							&& tInsuRenew.getAcDate() == 0 && tInsuRenew.getStatusCode() != 4)) {
						OccursList occursList = new OccursList();

						occursList.putParam("OOPrevInsuNo", tInsuRenew.getPrevInsuNo());
						occursList.putParam("OOEndoInsuNo", tInsuRenew.getEndoInsuNo());
						occursList.putParam("OONowInsuNo", tInsuRenew.getNowInsuNo());
						occursList.putParam("OOSelfInsuCode", selfInsuCode);
						occursList.putParam("OOBdLocation", bdLocation);
						occursList.putParam("OOInsuCompany", tInsuRenew.getInsuCompany());
						occursList.putParam("OOInsuStartDate", tInsuRenew.getInsuStartDate());
						occursList.putParam("OOInsuEndDate", tInsuRenew.getInsuEndDate());
						occursList.putParam("OOInsuTypeCode", tInsuRenew.getInsuTypeCode());
						occursList.putParam("OOFireInsuCovrg", tInsuRenew.getFireInsuCovrg());
						occursList.putParam("OOEthqInsuCovrg", tInsuRenew.getEthqInsuCovrg());
						occursList.putParam("OOFireInsuPrem", tInsuRenew.getFireInsuPrem());
						occursList.putParam("OOEthqInsuPrem", tInsuRenew.getEthqInsuPrem());
						occursList.putParam("OOInsuYearMonth", InsuYearMonth);
						occursList.putParam("OOBtnFlag", btnShowFlag.get(tmp));
						/* 將每筆資料放入Tota的OcList */
						this.totaVo.addOccursList(occursList);
						occursflg = false;
					}
				} // else

			}
		}
		if (lInsuOrignal != null && lInsuOrignal.size() != 0) {
			for (InsuOrignal tInsuOrignal : lInsuOrignal) {

				tmpInsu tmp = new tmpInsu(tInsuOrignal.getOrigInsuNo(), tInsuOrignal.getInsuCompany());

				this.info("tInsuOrignal.getOrigInsuNo ... " + tInsuOrignal.getOrigInsuNo());
				this.info("tInsuOrignal.getInsuStartDate ... " + tInsuOrignal.getInsuStartDate());
				this.info("tInsuOrignal.getInsuEndDate ... " + tInsuOrignal.getInsuEndDate());

				if (State == 9) {

					OccursList occursList = new OccursList();

//					0.新保
					selfInsuCode = 0;

//					僅第一筆有按鈕，或該筆相同的保單號碼且有批單號碼的
					if (btnShowFlag.containsKey(tmp)) {
						btnShowFlag.put(tmp, 0);
						if (btnInsuNo.get(tmp).equals(tInsuOrignal.getOrigInsuNo())
								&& !"".equals(tInsuOrignal.getEndoInsuNo().trim())) {
							this.info("tInsuRenew.getEndoInsuNo() ... " + tInsuOrignal.getEndoInsuNo());
							btnShowFlag.put(tmp, 1);
						}
					} else {
						btnShowFlag.put(tmp, 1);
						btnInsuNo.put(tmp, tInsuOrignal.getOrigInsuNo());
					}

					this.info("btnShowFlag ... " + btnShowFlag.get(tmp));

					occursList.putParam("OOPrevInsuNo", tInsuOrignal.getOrigInsuNo());
					occursList.putParam("OOEndoInsuNo", tInsuOrignal.getEndoInsuNo());
					occursList.putParam("OONowInsuNo", tInsuOrignal.getOrigInsuNo());
					occursList.putParam("OOSelfInsuCode", selfInsuCode);
					occursList.putParam("OOBdLocation", bdLocation);
					occursList.putParam("OOInsuCompany", tInsuOrignal.getInsuCompany());
					occursList.putParam("OOInsuStartDate", tInsuOrignal.getInsuStartDate());
					occursList.putParam("OOInsuEndDate", tInsuOrignal.getInsuEndDate());
					occursList.putParam("OOInsuTypeCode", tInsuOrignal.getInsuTypeCode());
					occursList.putParam("OOFireInsuCovrg", tInsuOrignal.getFireInsuCovrg());
					occursList.putParam("OOEthqInsuCovrg", tInsuOrignal.getEthqInsuCovrg());
					occursList.putParam("OOFireInsuPrem", tInsuOrignal.getFireInsuPrem());
					occursList.putParam("OOEthqInsuPrem", tInsuOrignal.getEthqInsuPrem());
					occursList.putParam("OOInsuYearMonth", 0);
					occursList.putParam("OOBtnFlag", btnShowFlag.get(tmp));
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
					occursflg = false;
				} else {
					if (tInsuOrignal.getInsuEndDate() >= threeMonthsB4Date) {
						OccursList occursList = new OccursList();

//						0.新保
						selfInsuCode = 0;

//						僅第一筆有按鈕，或該筆相同的保單號碼且有批單號碼的
						if (btnShowFlag.containsKey(tmp)) {
							btnShowFlag.put(tmp, 0);
							if (btnInsuNo.get(tmp).equals(tInsuOrignal.getOrigInsuNo())
									&& !"".equals(tInsuOrignal.getEndoInsuNo().trim())) {
								this.info("tInsuRenew.getEndoInsuNo() ... " + tInsuOrignal.getEndoInsuNo());
								btnShowFlag.put(tmp, 1);
							}
						} else {
							btnShowFlag.put(tmp, 1);
							btnInsuNo.put(tmp, tInsuOrignal.getOrigInsuNo());
						}

						this.info("btnShowFlag ... " + btnShowFlag.get(tmp));

						occursList.putParam("OOPrevInsuNo", tInsuOrignal.getOrigInsuNo());
						occursList.putParam("OOEndoInsuNo", tInsuOrignal.getEndoInsuNo());
						occursList.putParam("OONowInsuNo", tInsuOrignal.getOrigInsuNo());
						occursList.putParam("OOSelfInsuCode", selfInsuCode);
						occursList.putParam("OOBdLocation", bdLocation);
						occursList.putParam("OOInsuCompany", tInsuOrignal.getInsuCompany());
						occursList.putParam("OOInsuStartDate", tInsuOrignal.getInsuStartDate());
						occursList.putParam("OOInsuEndDate", tInsuOrignal.getInsuEndDate());
						occursList.putParam("OOInsuTypeCode", tInsuOrignal.getInsuTypeCode());
						occursList.putParam("OOFireInsuCovrg", tInsuOrignal.getFireInsuCovrg());
						occursList.putParam("OOEthqInsuCovrg", tInsuOrignal.getEthqInsuCovrg());
						occursList.putParam("OOFireInsuPrem", tInsuOrignal.getFireInsuPrem());
						occursList.putParam("OOEthqInsuPrem", tInsuOrignal.getEthqInsuPrem());
						occursList.putParam("OOInsuYearMonth", 0);
						occursList.putParam("OOBtnFlag", btnShowFlag.get(tmp));
						/* 將每筆資料放入Tota的OcList */
						this.totaVo.addOccursList(occursList);
						occursflg = false;
					}

				} // else
			} // for
		} 
//		else {
//			throw new LogicException(titaVo, "E0001", "L4964  查無資料");
//		}

		if (occursflg) {
			throw new LogicException(titaVo, "E0001", "L4964  查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

//	三個月前(民國年) ，比較GET值為民國年
	private int find3MonthsB4() throws LogicException {
		int returnDate = 0;
		dateUtil.init();
		dateUtil.setDate_1(this.getTxBuffer().getTxCom().getTbsdy());
		dateUtil.setMons(-3);

		String month = ("" + dateUtil.getCalenderDay()).substring(0, 5);

		returnDate = parse.stringToInteger(month + "01");

		return returnDate;
	}

	private class tmpInsu {
		@Override
		public String toString() {
			return "tmpInsu [orignalInsuNo=" + orignalInsuNo + ", insuCompany=" + insuCompany + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((insuCompany == null) ? 0 : insuCompany.hashCode());
			result = prime * result + ((orignalInsuNo == null) ? 0 : orignalInsuNo.hashCode());
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
			if (insuCompany == null) {
				if (other.insuCompany != null)
					return false;
			} else if (!insuCompany.equals(other.insuCompany))
				return false;
			if (orignalInsuNo == null) {
				if (other.orignalInsuNo != null)
					return false;
			} else if (!orignalInsuNo.equals(other.orignalInsuNo))
				return false;
			return true;
		}

		private String orignalInsuNo = "";
		private String insuCompany = "";

		private tmpInsu(String orignalInsuNo, String insuCompany) {
			this.setOrignalInsuNo(orignalInsuNo);
			this.setInsuCompany(insuCompany);
		}

		private void setOrignalInsuNo(String orignalInsuNo) {
			this.orignalInsuNo = orignalInsuNo;
		}

		private void setInsuCompany(String insuCompany) {
			this.insuCompany = insuCompany;
		}

		private L4964 getEnclosingInstance() {
			return L4964.this;
		}

	}
}