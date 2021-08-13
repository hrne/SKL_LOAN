package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L5951ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L5951")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5951 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5951.class);
	@Autowired
	public PfItDetailService sPfItDetailService;
	@Autowired
	public PfBsDetailService sPfBsDetailService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public PfRewardService sPfRewardService;
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public L5051ServiceImpl l5051ServiceImpl;

	@Autowired
	public L5951ServiceImpl l5951ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5951 ");
		this.totaVo.init(titaVo);

		String MaxDataRow = titaVo.getParam("MaxDataRow").trim();// 最大資料筆數
		String WorkMonthFm = titaVo.getParam("WorkMonthFm").trim();// 工作月(起)YYYYmm
		String WorkMonthTo = titaVo.getParam("WorkMonthTo").trim();// 工作月(訖)YYYYmm
		String SumByFacm = titaVo.getParam("SumByFacm").trim();// 額度加總 Y N
		String DeptCodeNotIn = titaVo.getParam("DeptCodeNotIn").trim();// 排除[團體意外險部]
		String PieceCodeNotIn = titaVo.getParam("PieceCodeNotIn").trim();// 計件代碼於L5951查詢條件須排除
		// List<PfItDetail> lPfItDetailVO=new ArrayList<PfItDetail>();

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */

		this.limit = 40;
		if (MaxDataRow != null && MaxDataRow.length() != 0) {
			int iMaxDataRow = parse.stringToInteger(MaxDataRow);
			if (iMaxDataRow != 0) {
				this.limit = iMaxDataRow;
			}
		}
//		if(SumByFacm.equals("Y")) {
//			this.limit=Integer.MAX_VALUE;
//		}
		String lPieceCodeNotIn[] = null;
		if (PieceCodeNotIn != null && PieceCodeNotIn.trim().length() != 0) {
			lPieceCodeNotIn = PieceCodeNotIn.split("，");
		}

		String lDeptCodeNotIn[] = null;
		if (DeptCodeNotIn != null && DeptCodeNotIn.trim().length() != 0) {
			lDeptCodeNotIn = DeptCodeNotIn.split("，");
		}

		// String sqlSumL5951="SELECT FROM\"PfBsDetail\" ";
		String sqlL5951 = l5951ServiceImpl.getSqlL5951(SumByFacm, lPieceCodeNotIn, lDeptCodeNotIn);

		// 介紹人業績,只有有跟沒有,不會有負的業績.
		// sqlL5951+="AND I.\"PerfEqAmt\">=0 AND I.\"PerfAmt\">=0 AND
		// I.\"RerfReward\">=0 ";

		Map<String, String> queryKey = new HashMap<String, String>();
		// 工作月
		int IntWorkMonthFm = l5051ServiceImpl.ChangeYM(WorkMonthFm);
		int IntWorkMonthTo = l5051ServiceImpl.ChangeYM(WorkMonthTo);
		queryKey.put("WorkMonthFm", String.valueOf(IntWorkMonthFm));
		queryKey.put("WorkMonthTo", String.valueOf(IntWorkMonthTo));
		// 工作月
		if (lPieceCodeNotIn != null && lPieceCodeNotIn.length != 0) {
			for (int i = 0; i < lPieceCodeNotIn.length; i++) {
				queryKey.put("PieceCodeNotIn" + i, lPieceCodeNotIn[i]);
			}
		}

		if (lDeptCodeNotIn != null && lDeptCodeNotIn.length != 0) {
			for (int i = 0; i < lDeptCodeNotIn.length; i++) {
				queryKey.put("DeptCodeNotIn" + i, lDeptCodeNotIn[i]);
			}
		}

		this.info("L5951 sqlL5951 sql=[" + sqlL5951 + "]");
		List<String[]> dataL5951 = l5951ServiceImpl.FindData(this.index, this.limit, sqlL5951, queryKey, titaVo);

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (dataL5951 != null && dataL5951.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToAuto();// 自動折返
//			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		String introducer = "";
		String custNo = "";
		String facmNo = "";
		int perfAmt = 0;
		int drawdownAmt = 0;
		int perfEqAmt = 0;
		int perfReward = 0;
		int subCnt = 0;

		int cnt = 0;
		if (dataL5951 != null && dataL5951.size() != 0) {
			for (String[] lData : dataL5951) {
				this.info("L5951 count = " + ++cnt);

				String OOBsDeptX = lData[0];// 部室別
				String OOBsOfficerName = lData[1];// 房貸專員姓名
				String OOBsOfficer = lData[2];// 房貸專員原編
				String OOCustNm = lData[3];// 戶名
				String OOCustNo = lData[4];// 戶號
				String OOFacmNo = lData[5];// 額度
				String OOBormNo = lData[6];// 撥款
				String OODrawdownDate = lData[7];// 撥款日
				String OOProdCode = lData[8];// 利率代碼
				String OOPieceCode = lData[9];// 計件代碼
				String OOCntingCode = lData[10];// 是否計件
				String OODrawdownAmt = lData[11];// 撥款金額
				if (OODrawdownAmt == null || "".equals(OODrawdownAmt)) {
					OODrawdownAmt = "0";
				}
				String OODeptCode = lData[12];// 介紹人部市代號
				String OODistCode = lData[13];// 介紹人區部代號
				String OOUnitCode = lData[14];// 介紹人單位代號
				String OODeptCodeX = lData[15];// 介紹人部市名稱
				String OODistCodeX = lData[16];// 介紹人區部名稱
				String OOUnitCodeX = lData[17];// 介紹人單位名稱
				String OOIntroducerName = lData[18];// 介紹人姓名
				String OOIntroducer = lData[19];// 介紹人原編
				String OODeptManager = lData[20];// 部經理代號
				String OODeptManagerName = lData[21];// 部經理名稱
				String OODistManager = lData[22];// 區經理
				String OODistManagerName = lData[23];// 區經理名稱
				String OOUnitManager = lData[24];// 處經理
				String OOUnitManagerName = lData[25];// 處經理名稱
				String OOPerfEqAmt = lData[26];// 三階換算業績
				if (OOPerfEqAmt == null || "".equals(OOPerfEqAmt)) {
					OOPerfEqAmt = "0";
				}
				String OOPerfReward = lData[27];// 三階業務報酬
				if (OOPerfReward == null || "".equals(OOPerfReward)) {
					OOPerfReward = "0";
				}
				String OOPerfAmt = lData[28];// 已用額度
				String OOWorkMonth = lData[29];// 工作月
				String OOPerfDate = lData[30];// 業績日期

				// 顯示合計
				if (("Y").equals(SumByFacm)) {
					if ((!"".equals(introducer) && !introducer.equals(OOIntroducer)) || (!"".equals(custNo) && !custNo.equals(OOCustNo)) || (!"".equals(facmNo) && !facmNo.equals(OOFacmNo))) {
						if (subCnt>1) {
							subTotal(introducer, custNo, facmNo, perfAmt, drawdownAmt, perfEqAmt, perfReward);							
						}

						perfAmt = 0;
						drawdownAmt = 0;
						perfEqAmt = 0;
						perfReward = 0;
						subCnt = 0;
					}
				}

				OccursList occursList1 = new OccursList();
				occursList1.putParam("OOBsDeptX", OOBsDeptX);// 部室別
				occursList1.putParam("OOBsOfficerName", OOBsOfficerName);// 房貸專員姓名
				occursList1.putParam("OOBsOfficer", OOBsOfficer);// 房貸專員原編
				occursList1.putParam("OOCustNm", OOCustNm);// 戶名
				occursList1.putParam("OOCustNo", OOCustNo);// 戶號
				occursList1.putParam("OOFacmNo", OOFacmNo);// 額度
				occursList1.putParam("OOBormNo", OOBormNo);// 撥款
				occursList1.putParam("OODrawdownDate", toRoc(OODrawdownDate));// 撥款日
				occursList1.putParam("OOProdCode", OOProdCode);// 利率代碼
				occursList1.putParam("OOPieceCode", OOPieceCode);// 計件代碼
				occursList1.putParam("OOCntingCode", OOCntingCode);// 是否計件
				occursList1.putParam("OODrawdownAmt", numIsNull(OODrawdownAmt));// 撥款金額
				occursList1.putParam("OODeptCode", OODeptCode);// 介紹人部市代號
				occursList1.putParam("OODistCode", OODistCode);// 介紹人區部代號
				occursList1.putParam("OOUnitCode", OOUnitCode);// 介紹人單位代號
				occursList1.putParam("OODeptCodeX", OODeptCodeX);// 介紹人部市名稱
				occursList1.putParam("OODistCodeX", OODistCodeX);// 介紹人區部名稱
				occursList1.putParam("OOUnitCodeX", OOUnitCodeX);// 介紹人單位名稱
				occursList1.putParam("OOIntroducerName", OOIntroducerName);// 介紹人姓名
				occursList1.putParam("OOIntroducer", OOIntroducer);// 介紹人原編
				occursList1.putParam("OODeptManager", OODeptManager);// 部經理代號
				occursList1.putParam("OODeptManagerName", OODeptManagerName);// 部經理名稱
				occursList1.putParam("OODistManager", OODistManager);// 區經理
				occursList1.putParam("OODistManagerName", OODistManagerName);// 區經理名稱
				occursList1.putParam("OOUnitManager", OOUnitManager);// 處經理
				occursList1.putParam("OOUnitManagerName", OOUnitManagerName);// 處經理名稱
				occursList1.putParam("OOPerfEqAmt", numIsNull(OOPerfEqAmt));// 三階換算業績
				occursList1.putParam("OOPerfReward", numIsNull(OOPerfReward));// 三階業務報酬
				occursList1.putParam("OOPerfAmt", numIsNull(OOPerfAmt));// 已用額度
				occursList1.putParam("OOWorkMonth", toRoc(OOWorkMonth));// 工作月
				occursList1.putParam("OOPerfDate", toRoc(OOPerfDate));// 業績日期
				this.totaVo.addOccursList(occursList1);

				subCnt++;
				introducer = OOIntroducer;
				custNo = OOCustNo;
				facmNo = OOFacmNo;
				perfAmt += Integer.valueOf(OOPerfAmt);
				drawdownAmt += Integer.valueOf(OODrawdownAmt);
				perfEqAmt += Integer.valueOf(OOPerfEqAmt);
				perfReward += Integer.valueOf(OOPerfReward);
			}
			if (!"".equals(introducer) && ("Y").equals(SumByFacm)&&subCnt>1) {
				subTotal(introducer, custNo, facmNo, perfAmt, drawdownAmt, perfEqAmt, perfReward);
			}
		} else {
			if (this.index != 0) {
				// 代表有多筆查詢,然後筆數剛好可以被整除

			} else {
				throw new LogicException(titaVo, "E0001", "");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void subTotal(String introducer, String custNo, String facmNo, int perfAmt, int drawdownAmt, int perfEqAmt, int perfReward) {

		OccursList occursList1 = new OccursList();
		occursList1.putParam("OOBsDeptX", "");// 部室別
		occursList1.putParam("OOBsOfficerName", "");// 房貸專員姓名
		occursList1.putParam("OOBsOfficer", "");// 房貸專員原編
		occursList1.putParam("OOCustNm", String.format("%07d-%03d", Integer.valueOf(custNo),Integer.valueOf(facmNo)) + "合計");// 戶名
		occursList1.putParam("OOCustNo", 0);// 戶號
		occursList1.putParam("OOFacmNo", 0);// 額度
		occursList1.putParam("OOBormNo", 0);// 撥款
		occursList1.putParam("OODrawdownDate", 0);// 撥款日
		occursList1.putParam("OOProdCode", "");// 利率代碼
		occursList1.putParam("OOPieceCode", "");// 計件代碼
		occursList1.putParam("OOCntingCode", "");// 是否計件
		occursList1.putParam("OODrawdownAmt", drawdownAmt);// 撥款金額
		occursList1.putParam("OODeptCode", "");// 介紹人部市代號
		occursList1.putParam("OODistCode", "");// 介紹人區部代號
		occursList1.putParam("OOUnitCode", "");// 介紹人單位代號
		occursList1.putParam("OODeptCodeX", "");// 介紹人部市名稱
		occursList1.putParam("OODistCodeX", "");// 介紹人區部名稱
		occursList1.putParam("OOUnitCodeX", "");// 介紹人單位名稱
		occursList1.putParam("OOIntroducerName", "");// 介紹人姓名
		occursList1.putParam("OOIntroducer", introducer);// 介紹人原編
		occursList1.putParam("OODeptManager", "");// 部經理代號
		occursList1.putParam("OODeptManagerName", "");// 部經理名稱
		occursList1.putParam("OODistManager", "");// 區經理
		occursList1.putParam("OODistManagerName", "");// 區經理名稱
		occursList1.putParam("OOUnitManager", "");// 處經理
		occursList1.putParam("OOUnitManagerName", "");// 處經理名稱
		occursList1.putParam("OOPerfEqAmt", perfEqAmt);// 三階換算業績
		occursList1.putParam("OOPerfReward", perfReward);// 三階業務報酬
		occursList1.putParam("OOPerfAmt", perfAmt);// 已用額度
		occursList1.putParam("OOWorkMonth", 0);// 工作月
		occursList1.putParam("OOPerfDate", 0);// 業績日期
		this.totaVo.addOccursList(occursList1);
	}

	public String numIsNull(String num) {
		if (num != null && num.length() != 0) {

		} else {
			num = "0";
		}
		return num;
	}

	public Integer toRoc(String iDate) {
		int date = 0;
		if ("".equals(iDate) || iDate == null) {
			date = 0;

		} else {
			if (iDate.length() >= 7) {
				date = Integer.valueOf(iDate);
				if (date > 19110000) {
					date -= 19110000;
				}
			} else if (iDate.length() >= 5) {
				// 年月
				date = Integer.valueOf(iDate);
				if (date > 191100) {
					date -= 191100;
				}
			}
		}
		return date;
	}

//	public void addOccurs(TitaVo titaVo,PfItDetail PfItDetailVO,PfBsDetail PfBsDetailVo,PfReward PfRewardVO) {
//		CustMain CustMainVO=sCustMainService.custNoFirst(PfItDetailVO.getCustNo(), PfItDetailVO.getCustNo());
//		String OOBsOfficer="";//戶名房貸專員-PfBsDetail
//		String OOCustNm="";//戶名
//		String OODeptCodeX=l5051ServiceImpl.FindCdBcm(titaVo,PfItDetailVO.getDeptCode());//部室
//		String OODistCodeX=l5051ServiceImpl.FindCdBcm(titaVo,PfItDetailVO.getDistCode());//區部
//		String OOUnitCodeX=l5051ServiceImpl.FindCdBcm(titaVo,PfItDetailVO.getUnitCode());//單位
//		String OOIntroducerBonus="";//介紹人介紹獎金
//		String OOCoorgnize="";//協辦人-PfReward
//		String OOIntroducerAddBonus="";//介紹人加碼獎勵津貼-PfReward
//		String OOCoorgnizerBonus="";//協辦人員協辦獎金-PfReward
//		if(PfBsDetailVo!=null) {
//			OOBsOfficer=PfBsDetailVo.getBsOfficer();//戶名房貸專員
//		}
//		if(CustMainVO!=null) {
//			OOCustNm=CustMainVO.getCustName();//戶名
//		}
//		if(OODeptCodeX==null) {
//			OODeptCodeX="";
//		}
//		if(OODistCodeX==null) {
//			OODistCodeX="";
//		}
//		if(OOUnitCodeX==null) {
//			OOUnitCodeX="";
//		}
//		if(PfRewardVO!=null) {
//			OOIntroducerBonus=String.valueOf(PfRewardVO.getIntroducerBonus());//介紹人介紹獎金
//			OOCoorgnize=PfRewardVO.getCoorgnizer();//協辦人-PfReward
//			OOIntroducerAddBonus=String.valueOf(PfRewardVO.getIntroducerAddBonus());//介紹人加碼獎勵津貼-PfReward
//			OOCoorgnizerBonus=String.valueOf(PfRewardVO.getCoorgnizerBonus());//協辦人員協辦獎金-PfReward
//		}
//		OccursList occursList = new OccursList();
//		occursList.putParam("OOLastUpdate",PfItDetailVO.getLastUpdateEmpNo());//經辦
//		occursList.putParam("OOBsOfficer",OOBsOfficer);//房貸專員-PfBsDetail
//		occursList.putParam("OOCustNm",OOCustNm);//戶名
//		occursList.putParam("OOCustNo",PfItDetailVO.getCustNo());//戶號
//		occursList.putParam("OOFacmNo",PfItDetailVO.getFacmNo());//額度
//		occursList.putParam("OOBormNo",PfItDetailVO.getBormNo());//撥款
//		occursList.putParam("OODrawdownDate",PfItDetailVO.getDrawdownDate());//撥款日
//		occursList.putParam("OOProdCode",PfItDetailVO.getProdCode());//商品代碼
//		occursList.putParam("OOPieceCode",PfItDetailVO.getPieceCode());//計件代碼
//		occursList.putParam("OOCntingCode",PfItDetailVO.getCntingCode());//是否計件
//		occursList.putParam("OODrawdownAmt",PfItDetailVO.getDrawdownAmt());//撥款金額 
//		occursList.putParam("OODeptCode",PfItDetailVO.getDeptCode());//部室代號
//		occursList.putParam("OODistCode",PfItDetailVO.getDistCode());//區部代號
//		occursList.putParam("OOUnitCode",PfItDetailVO.getUnitCode());//單位代號
//		occursList.putParam("OODeptCodeX",OODeptCodeX);//部室
//		occursList.putParam("OODistCodeX",OODistCodeX);//區部
//		occursList.putParam("OOUnitCodeX",OOUnitCodeX);//單位
//		occursList.putParam("OOIntroducer",PfItDetailVO.getIntroducer());//介紹人
//		occursList.putParam("OOUnitManager",PfItDetailVO.getUnitManager());//處經理
//		occursList.putParam("OODistManager",PfItDetailVO.getDistManager());//區經理
//		occursList.putParam("OOPerfCnt",PfItDetailVO.getPerfCnt());//件數
//		occursList.putParam("OOPerfEqAmt",PfItDetailVO.getPerfEqAmt());//換算業績
//		occursList.putParam("OOPerfReward",PfItDetailVO.getPerfReward());//業務報酬 
//		occursList.putParam("OOPerfAmt",PfItDetailVO.getPerfAmt());//業績金額 
//		occursList.putParam("OOIntroducerBonus",OOIntroducerBonus);//介紹人介紹獎金
//		occursList.putParam("OOCoorgnize",OOCoorgnize);//協辦人-PfReward
//		occursList.putParam("OOIntroducerAddBonus",OOIntroducerAddBonus);//介紹人加碼獎勵津貼-PfReward
//		occursList.putParam("OOCoorgnizerBonus",OOCoorgnizerBonus);//協辦人員協辦獎金-PfReward
//		occursList.putParam("OOPerfDate",PfItDetailVO.getPerfDate());//業績日期 
//		this.totaVo.addOccursList(occursList);
//	}

}