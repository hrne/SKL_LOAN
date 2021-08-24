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
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.db.service.springjpa.cm.L5952ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L5952")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5952 extends TradeBuffer {
	@Autowired
	public PfBsDetailService sPfBsDetailService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	public PfBsOfficerService sPfBsOfficerService;
	@Autowired
	public CdAoDeptService sCdAoDeptService;
	@Autowired
	public Parse parse;

	@Autowired
	public L5952ServiceImpl l5952ServiceImpl;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

//	@Autowired
//	public CdEmpService sCdEmpService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5952 ");
		this.totaVo.init(titaVo);
		int iWorkMonthFm = parse.stringToInteger(titaVo.getParam("WorkMonthFm")) + 191100;// 工作月(起)YYYYmm
		int iWorkMonthTo = parse.stringToInteger(titaVo.getParam("WorkMonthTo")) + 191100;// 工作月(訖))YYYYmm
		String iSumByFacm = titaVo.getParam("SumByFacm").trim();// 額度加總 Y N

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;// 查全部
		List<String[]> dataL5952 = l5952ServiceImpl.findData(iWorkMonthFm, iWorkMonthTo, iSumByFacm, this.index,
				this.limit, titaVo);

		if (dataL5952 != null && dataL5952.size() == this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToAuto();// 自動折返
			// this.totaVo.setMsgEndToEnter();// 手動折返
		}
		if (this.index == 0 && (dataL5952 == null || dataL5952.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (dataL5952 != null && dataL5952.size() != 0) {
			for (String[] lData : dataL5952) {
				String OOBsDeptX = lData[0];// 部室名
				String OOBsOfficerName = lData[1];// 房貸專員名稱
				String OOPerfCnt = lData[2];// 件數
				String OOPerfAmt = lData[3];// 業績金額
				String OOBsOfficer = lData[4];// 房貸專員
				String OOCustNm = lData[5];// 戶名
				String OOCustNo = lData[6];// 戶號
				String OOFacmNo = lData[7];// 額度
				String OOBormNo = lData[8];// 撥款序號
				if ("Y".equals(iSumByFacm)) {
					OOBormNo = "";
				}
				int OODrawdownDate = parse.stringToInteger(lData[9]) - 19110000;// 撥款日
				String OOProdCode = lData[10];// 商品代碼
				String OOPieceCode = lData[11];// 計件代碼
				String OODrawdownAmt = lData[12];// 撥款金額
				int OOWorkMonth = parse.stringToInteger(lData[13]) - 191100;// 工作月
				String OOIntDept = lData[14];// 區部名稱
				String OODistDept = lData[15];// 部室別名稱
				String OOUnitDept = lData[16];// 區域中心名稱
				String OOIntroducerName = lData[17];// 姓名
				String OOIntroducer = lData[18];// 員編
				String renewFlag = lData[19];// 展期/借新還舊;		
				OccursList occursList1 = new OccursList();
				occursList1.putParam("OOBsDeptX", OOBsDeptX);// 部室名
				occursList1.putParam("OOBsOfficerName", OOBsOfficerName);// 房貸專員名稱
				occursList1.putParam("OOPerfCnt", OOPerfCnt);// 件數
				occursList1.putParam("OOPerfAmt", OOPerfAmt);// 業績金額
				occursList1.putParam("OOBsOfficer", OOBsOfficer);// 房貸專員
				occursList1.putParam("OOCustNm", OOCustNm);// 戶名
				occursList1.putParam("OOCustNo", OOCustNo);// 戶號
				occursList1.putParam("OOFacmNo", OOFacmNo);// 額度
				occursList1.putParam("OOBormNo", OOBormNo);// 撥款序號
				occursList1.putParam("OODrawdownDate", OODrawdownDate);// 撥款日
				occursList1.putParam("OOProdCode", OOProdCode);// 商品代碼
				occursList1.putParam("OOPieceCode", OOPieceCode);// 計件代碼
				occursList1.putParam("OODrawdownAmt",OODrawdownAmt);// 撥款金額
				occursList1.putParam("OOWorkMonth", OOWorkMonth);// 工作月
				occursList1.putParam("OOIntDept", OOIntDept);// 區部名稱
				occursList1.putParam("OODistDept", OODistDept);// 部室別名稱
				occursList1.putParam("OOUnitDept", OOUnitDept);// 區域中心名稱
				occursList1.putParam("OOIntroducerName", OOIntroducerName);// 姓名
				occursList1.putParam("OOIntroducer", OOIntroducer);// 員編
				String OOReMark =  "1".equals(renewFlag) ? "展期" : ("2".equals(renewFlag) ? "借新還舊" : "");;
				occursList1.putParam("OOReMark", OOReMark);// 備註
				this.totaVo.addOccursList(occursList1);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}