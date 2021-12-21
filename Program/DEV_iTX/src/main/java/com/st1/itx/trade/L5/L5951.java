package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
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
	public L5951ServiceImpl l5951ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5951 ");
		this.totaVo.init(titaVo);

		int iMaxDataRow = parse.stringToInteger(titaVo.getParam("MaxDataRow"));// 最大資料筆數
		int iWorkMonthFm = parse.stringToInteger(titaVo.getParam("WorkMonthFm")) + 191100;// 工作月(起)YYYYmm
		int iWorkMonthTo = parse.stringToInteger(titaVo.getParam("WorkMonthTo")) + 191100;// 工作月(訖))YYYYmm
		String iSumByFacm = titaVo.getParam("SumByFacm");// 額度加總 Y N

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */

		this.limit = iMaxDataRow;

		List<String[]> dataL5951 = l5951ServiceImpl.findData(iWorkMonthFm, iWorkMonthTo, iSumByFacm, this.index, this.limit, titaVo);

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (dataL5951 != null && dataL5951.size() == this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToAuto();// 自動折返
//			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		if (this.index == 0 && (dataL5951 == null || dataL5951.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "");
		}

		int cnt = 0;
		for (String[] lData : dataL5951) {
			this.info("L5951 count = " + ++cnt);

			String OOBsDeptX = lData[0];// 部室別
			String OOBsOfficerName = lData[1];// 房貸專員姓名
			String OOBsOfficer = lData[2];// 房貸專員原編
			String OOCustNm = lData[3];// 戶名
			String OOCustNo = lData[4];// 戶號
			String OOFacmNo = lData[5];// 額度
			String OOBormNo = lData[6];// 撥款
			if ("Y".equals(iSumByFacm)) {
				OOBormNo = "";
			}
			int OODrawdownDate = parse.stringToInteger(lData[7]) - 19110000;// 撥款日
			String OOProdCode = lData[8];// 利率代碼
			String OOPieceCode = lData[9];// 計件代碼
			String OOCntingCode = lData[10];// 是否計件
			String OODrawdownAmt = lData[11];// 撥款金額
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
			String OOPerfReward = lData[27];// 三階業務報酬
			String OOPerfAmt = lData[28];// 業績金額
			int OOWorkMonth = parse.stringToInteger(lData[29]) - 191100;// 工作月
			int OOPerfDate = parse.stringToInteger(lData[30]) - 19110000;// 業績日期
			String renewFlag = lData[31];// 展期/借新還舊;

			OccursList occursList1 = new OccursList();
			occursList1.putParam("OOBsDeptX", OOBsDeptX);// 部室別
			occursList1.putParam("OOBsOfficerName", OOBsOfficerName);// 房貸專員姓名
			occursList1.putParam("OOBsOfficer", OOBsOfficer);// 房貸專員原編
			occursList1.putParam("OOCustNm", OOCustNm);// 戶名
			occursList1.putParam("OOCustNo", OOCustNo);// 戶號
			occursList1.putParam("OOFacmNo", OOFacmNo);// 額度
			occursList1.putParam("OOBormNo", OOBormNo);// 撥款
			occursList1.putParam("OODrawdownDate", OODrawdownDate);// 撥款日
			occursList1.putParam("OOProdCode", OOProdCode);// 利率代碼
			occursList1.putParam("OOPieceCode", OOPieceCode);// 計件代碼
			occursList1.putParam("OOCntingCode", OOCntingCode);// 是否計件
			occursList1.putParam("OODrawdownAmt", OODrawdownAmt);// 撥款金額
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
			occursList1.putParam("OOPerfEqAmt", OOPerfEqAmt);// 三階換算業績
			occursList1.putParam("OOPerfReward", OOPerfReward);// 三階業務報酬
			occursList1.putParam("OOPerfAmt", OOPerfAmt);// 已用額度
			occursList1.putParam("OOWorkMonth", OOWorkMonth);// 工作月
			occursList1.putParam("OOPerfDate", OOPerfDate);// 業績日期
			String OOReMark = "1".equals(renewFlag) ? "展期" : ("2".equals(renewFlag) ? "借新還舊" : "");
			;
			occursList1.putParam("OOReMark", OOReMark);// 備註

			this.totaVo.addOccursList(occursList1);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}