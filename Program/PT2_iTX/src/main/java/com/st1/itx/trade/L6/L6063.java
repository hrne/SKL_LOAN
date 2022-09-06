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
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * END=X,1<br>
 */

@Service("L6063") // 擔保品代號資料檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6063 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdClService sCdClService;
	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6063 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iClCode1 = this.parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = this.parse.stringToInteger(titaVo.getParam("ClCode2"));

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 45 * 200 = 9000

		// 查詢擔保品代號資料檔
		Slice<CdCl> slCdCl;
//    lCdCl = sCdClService.findAll();
		if (iClCode1 != 0) {
			if (iClCode2 != 0) {
				slCdCl = sCdClService.clCode1Range(iClCode1, iClCode1, iClCode2, iClCode2, this.index, this.limit, titaVo);
			} else {
				slCdCl = sCdClService.clCode1Range(iClCode1, iClCode1, 00, 99, this.index, this.limit, titaVo);
			}
		} else {
			slCdCl = sCdClService.findAll(this.index, this.limit, titaVo);
		}
		List<CdCl> lCdCl = slCdCl == null ? null : slCdCl.getContent();

		if (lCdCl == null || lCdCl.size() == 0) {
			throw new LogicException(titaVo, "E0001", "擔保品代號資料檔檔"); // 查無資料
		}
		// 如有找到資料
		for (CdCl tCdCl : lCdCl) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOClCode1", tCdCl.getClCode1());
			occursList.putParam("OOClCode2", tCdCl.getClCode2());
			occursList.putParam("OOClItem", tCdCl.getClItem());
			occursList.putParam("OOClTypeJCIC", tCdCl.getClTypeJCIC());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdCl.getLastUpdate()) + " " + parse.timeStampToStringTime(tCdCl.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdCl.getLastUpdateEmpNo() + " " + empName(titaVo, tCdCl.getLastUpdateEmpNo()));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdCl != null && slCdCl.hasNext()) {
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