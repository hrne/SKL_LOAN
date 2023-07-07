package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcNoCode=X,8 AcSubCode=X,5 AcDtlCode=X,2 END=X,1
 */

@Service("L6061") // 會計科子細目設定檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6061 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6061 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcNoCode = titaVo.getParam("AcNoCode");
		String iAcSubCode = titaVo.getParam("AcSubCode");
		String iAcDtlCode = titaVo.getParam("AcDtlCode");
		String iAcNoItem = titaVo.getParam("AcNoItem");

		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}
		if (iAcNoCode.isEmpty()) {
			iAcNoCode = " ";
		}

		this.info(" L6061 iAcNoCode : " + iAcNoCode + "-" + iAcSubCode + "-" + iAcDtlCode + ".");
		this.info(" L6061 iAcNoItem : " + iAcNoItem);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 145 * 200 = 29000

		// 查詢會計科子細目設定檔
		Slice<CdAcCode> slCdAcCode;

		if (iAcNoCode.isEmpty() && iAcNoItem.isEmpty()) {
			slCdAcCode = sCdAcCodeService.findAll(this.index, this.limit, titaVo);				
		} else if (iAcSubCode.equals("     ") && iAcDtlCode.equals("  ")&& iAcNoItem.isEmpty()) {
			slCdAcCode = sCdAcCodeService.findAcCode(iAcNoCode, iAcNoCode, "     ", "ZZZZZ", "  ", "ZZ", this.index, this.limit, titaVo);
			
		} else if (iAcDtlCode.equals("  ") && iAcNoItem.isEmpty()) {
			slCdAcCode = sCdAcCodeService.findAcCode(iAcNoCode, iAcNoCode, iAcSubCode, iAcSubCode, "  ", "ZZ", this.index, this.limit, titaVo);
			
		} else if (iAcSubCode.equals("     ") && iAcDtlCode.equals("  ") && iAcNoCode.equals(" ") && !iAcNoItem.isEmpty()) {
			slCdAcCode = sCdAcCodeService.likeAcItem("%"+iAcNoItem+"%" ,this.index, this.limit, titaVo);	
			
		}else if (iAcSubCode.equals("     ") && iAcDtlCode.equals("  ")&& !iAcNoItem.isEmpty()) {
			slCdAcCode = sCdAcCodeService.findAcItem(iAcNoCode, iAcNoCode, "     ", "ZZZZZ", "  ", "ZZ", "%"+iAcNoItem+"%" , this.index, this.limit, titaVo);
				
		} else if (iAcDtlCode.equals("  ") && !iAcNoItem.isEmpty()) {
			slCdAcCode = sCdAcCodeService.findAcItem(iAcNoCode, iAcNoCode, iAcSubCode, iAcSubCode, "  ", "ZZ", "%"+iAcNoItem+"%" ,this.index, this.limit, titaVo);
		
		} else {
			slCdAcCode = sCdAcCodeService.findAcCode(iAcNoCode, iAcNoCode, iAcSubCode, iAcSubCode, iAcDtlCode, iAcDtlCode, this.index, this.limit, titaVo);
		}
		List<CdAcCode> lCdAcCode = slCdAcCode == null ? null : slCdAcCode.getContent();

		if (lCdAcCode == null || lCdAcCode.size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
		}
		// 如有找到資料
		for (CdAcCode tCdAcCode : lCdAcCode) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOAcNoCode", tCdAcCode.getAcNoCode());
			occursList.putParam("OOAcSubCode", tCdAcCode.getAcSubCode());
			occursList.putParam("OOAcDtlCode", tCdAcCode.getAcDtlCode());
			occursList.putParam("OOAcNoItem", tCdAcCode.getAcNoItem());
			occursList.putParam("OOAcctCode", tCdAcCode.getAcctCode());
			occursList.putParam("OOAcctItem", tCdAcCode.getAcctItem());
			occursList.putParam("OOClassCode", tCdAcCode.getClassCode());
			occursList.putParam("OOAcBookFlag", tCdAcCode.getAcBookFlag());
			occursList.putParam("OODbCr", tCdAcCode.getDbCr());
			occursList.putParam("OOAcctFlag", tCdAcCode.getAcctFlag());
			occursList.putParam("OOReceivableFlag", tCdAcCode.getReceivableFlag());
			occursList.putParam("OOClsChkFlag", tCdAcCode.getClsChkFlag());
			occursList.putParam("OOInuseFlag", tCdAcCode.getInuseFlag());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdAcCode.getLastUpdate())+ " " +parse.timeStampToStringTime(tCdAcCode.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdAcCode.getLastUpdateEmpNo() + " " + empName(titaVo, tCdAcCode.getLastUpdateEmpNo()));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdAcCode != null && slCdAcCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}