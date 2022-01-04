package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.service.CdBranchGroupService;
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

	/* DB服務注入 */
	@Autowired
	public CdBranchService sCdBranchService;
	@Autowired
	public CdBranchGroupService sCdBranchGroupService;
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

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R02"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R02"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdBranch(new CdBranch());

		for (int i = 1; i <= 20; i++) {
			this.totaVo.putParam("L6R02GroupNo" + i, "");
			this.totaVo.putParam("L6R02Group" + i, "");
		}

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

		// 查詢營業單位客組別檔
		moveTotaCdBranchGroup(iRimBranchNo, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 營業單位對照檔
	private void moveTotaCdBranch(CdBranch mCdBranch) throws LogicException {
		this.totaVo.putParam("L6R02BranchNo", mCdBranch.getBranchNo());
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

	}

	private void moveTotaCdBranchGroup(String mRimBranchNo, TitaVo titaVo) throws LogicException {

		int i = 1;
		Slice<CdBranchGroup> tCdBranchGroup = sCdBranchGroupService.findByBranchNo(mRimBranchNo, this.index, this.limit, titaVo);
		List<CdBranchGroup> lCdBranchGroup = tCdBranchGroup == null ? null : tCdBranchGroup.getContent();

		if (lCdBranchGroup != null) {
			for (CdBranchGroup mlCdBranchGroup : lCdBranchGroup) {
				this.totaVo.putParam("L6R02GroupNo" + i, mlCdBranchGroup.getGroupNo());
				this.totaVo.putParam("L6R02Group" + i, mlCdBranchGroup.getGroupItem());
				i++;
			}

		}

	}

}