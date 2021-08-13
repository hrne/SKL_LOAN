package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimBranchNo=X,4
 */
@Service("L6R02") // 尋找營業單位對照檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R02 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R02.class);

	/* DB服務注入 */
	@Autowired
	public CdBranchService sCdBranchService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R02 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimBranchNo = titaVo.getParam("RimBranchNo");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R02"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R02"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdBranch(new CdBranch());

		// 查詢營業單位對照檔
		CdBranch tCdBranch = sCdBranchService.findById(iRimBranchNo, titaVo);
		/* 如有找到資料 */
		if (tCdBranch != null) {
			if (iRimTxCode.equals("L6702") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimBranchNo); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdBranch(tCdBranch);
			}
		} else {
			if (iRimTxCode.equals("L6702") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "營業單位對照檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 營業單位對照檔
	private void moveTotaCdBranch(CdBranch mCdBranch) throws LogicException {
		this.totaVo.putParam("L6R02BranchNo", mCdBranch.getBranchNo());
//		this.totaVo.putParam("L6R02AcBranchNo", mCdBranch.getAcBranchNo());
		this.totaVo.putParam("L6R02CRH", mCdBranch.getCRH());
		this.totaVo.putParam("L6R02BranchShort", mCdBranch.getBranchShort());
		this.totaVo.putParam("L6R02BranchItem", mCdBranch.getBranchItem());
		this.totaVo.putParam("L6R02Zip3", mCdBranch.getZip3());
		this.totaVo.putParam("L6R02Zip2", mCdBranch.getZip2());
		this.totaVo.putParam("L6R02BranchAddress1", mCdBranch.getBranchAddress1());
		this.totaVo.putParam("L6R02BranchAddress2", mCdBranch.getBranchAddress2());
		this.totaVo.putParam("L6R02Owner", mCdBranch.getOwner());
		this.totaVo.putParam("L6R02BusinessID", mCdBranch.getBusinessID());
		this.totaVo.putParam("L6R02BranchStatusCode", mCdBranch.getBranchStatusCode());
		this.totaVo.putParam("L6R02RSOCode", mCdBranch.getRSOCode());
		this.totaVo.putParam("L6R02MediaUnitCode", mCdBranch.getMediaUnitCode());
		this.totaVo.putParam("L6R02Group1", mCdBranch.getGroup1());
		this.totaVo.putParam("L6R02Group2", mCdBranch.getGroup2());
		this.totaVo.putParam("L6R02Group3", mCdBranch.getGroup3());
		this.totaVo.putParam("L6R02Group4", mCdBranch.getGroup4());
		this.totaVo.putParam("L6R02Group5", mCdBranch.getGroup5());
		this.totaVo.putParam("L6R02Group6", mCdBranch.getGroup6());
		this.totaVo.putParam("L6R02Group7", mCdBranch.getGroup7());
		this.totaVo.putParam("L6R02Group8", mCdBranch.getGroup8());
		this.totaVo.putParam("L6R02Group9", mCdBranch.getGroup9());
		this.totaVo.putParam("L6R02Group10", mCdBranch.getGroup10());

	}

}