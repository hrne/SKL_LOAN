package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 BranchNo=9,4 AcBranchNo=9,4 CRH=9,2 BranchShort=X,28
 * BranchItem=X,80 Zip3=9,3 Zip2=9,2 BranchAddress=X,120 Owner=X,28
 * BusinessID=9,8 BranchStatusCode=9,1 RSOCode=9,3 MediaUnitCode=9,4
 * #loop{times:10,i:1} Group=X,20 #end END=X,1
 */

@Service("L6702")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6702 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBranchService sCdBranchService;
	@Autowired
	public CdBranchGroupService sCdBranchGroupService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6702 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iBranchNo = titaVo.getParam("BranchNo");
		String iBranchAddress1;
		String iBranchAddress2;

		if (titaVo.getParam("BranchAddress").length() > 30) {
			iBranchAddress1 = titaVo.getParam("BranchAddress").substring(0, 30);
			iBranchAddress2 = titaVo.getParam("BranchAddress").substring(30, titaVo.getParam("BranchAddress").length());
		} else {
			iBranchAddress1 = titaVo.getParam("BranchAddress");
			iBranchAddress2 = "";
		}

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6702"); // 功能選擇錯誤
		}

		// 更新營業單位資料檔
		CdBranch tCdBranch = new CdBranch();

		switch (iFuncCode) {
		case 1: // 新增
			moveCdBranch(iFuncCode, iBranchAddress1, iBranchAddress2, tCdBranch, titaVo);
			try {
				this.info("1");
				sCdBranchService.insert(tCdBranch, titaVo);
				moveCdBranchGroup(iBranchNo, iFuncCode, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}

			break;

		case 2: // 修改
			tCdBranch = sCdBranchService.holdById(iBranchNo);
			if (tCdBranch == null) {
				throw new LogicException(titaVo, "E0003", iBranchNo); // 修改資料不存在
			}
			CdBranch tCdBranch2 = (CdBranch) dataLog.clone(tCdBranch); ////
			try {
				moveCdBranch(iFuncCode, iBranchAddress1, iBranchAddress2, tCdBranch, titaVo);
				tCdBranch = sCdBranchService.update2(tCdBranch, titaVo); ////
				moveCdBranchGroup(iBranchNo, iFuncCode, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdBranch2, tCdBranch); ////
			dataLog.exec("修改營業單位對照檔"); ////

			break;

		case 4: // 刪除
			tCdBranch = sCdBranchService.holdById(iBranchNo);
			if (tCdBranch != null) {
				try {
					sCdBranchService.delete(tCdBranch);
					moveCdBranchGroup(iBranchNo, iFuncCode, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iBranchNo); // 刪除資料不存在
			}

			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdBranch(int mFuncCode, String mBranchAddress1, String mBranchAddress2, CdBranch mCdBranch, TitaVo titaVo) throws LogicException {
		mCdBranch.setBranchNo(titaVo.getParam("BranchNo"));
//		mCdBranch.setAcBranchNo(titaVo.getParam("AcBranchNo"));
		mCdBranch.setCRH(titaVo.getParam("CRH"));
		mCdBranch.setBranchShort(titaVo.getParam("BranchShort"));
		mCdBranch.setBranchItem(titaVo.getParam("BranchItem"));
		mCdBranch.setZip3(titaVo.getParam("Zip3"));
		mCdBranch.setZip2(titaVo.getParam("Zip2"));
		mCdBranch.setBranchAddress1(mBranchAddress1);
		mCdBranch.setBranchAddress2(mBranchAddress2);
		mCdBranch.setOwner(titaVo.getParam("Owner"));
		mCdBranch.setBusinessID(titaVo.getParam("BusinessID"));
		mCdBranch.setBranchStatusCode(titaVo.getParam("BranchStatusCode"));
		mCdBranch.setRSOCode(titaVo.getParam("RSOCode"));
		mCdBranch.setMediaUnitCode(titaVo.getParam("MediaUnitCode"));
//		mCdBranch.setGroup1(titaVo.getParam("Group1"));
//		mCdBranch.setGroup2(titaVo.getParam("Group2"));
//		mCdBranch.setGroup3(titaVo.getParam("Group3"));
//		mCdBranch.setGroup4(titaVo.getParam("Group4"));
//		mCdBranch.setGroup5(titaVo.getParam("Group5"));
//		mCdBranch.setGroup6(titaVo.getParam("Group6"));
//		mCdBranch.setGroup7(titaVo.getParam("Group7"));
//		mCdBranch.setGroup8(titaVo.getParam("Group8"));
//		mCdBranch.setGroup9(titaVo.getParam("Group9"));
//		mCdBranch.setGroup10(titaVo.getParam("Group10"));
//		if (mFuncCode != 2) {
//			mCdBranch.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
//			mCdBranch.setCreateEmpNo(titaVo.getTlrNo());
//		}
//		mCdBranch.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
//		mCdBranch.setLastUpdateEmpNo(titaVo.getTlrNo());
	}

	private void moveCdBranchGroup(String mBranchNo, int mFuncCode, TitaVo titaVo) throws LogicException {
		this.info("into moveCdBranchGroup");

		if (mFuncCode == 2 || mFuncCode == 4) {

			Slice<CdBranchGroup> mCdBreanchGroup = sCdBranchGroupService.findByBranchNo(mBranchNo, this.index, this.limit, titaVo);
			List<CdBranchGroup> dCdBreanchGroup = mCdBreanchGroup == null ? null : mCdBreanchGroup.getContent();
			if (dCdBreanchGroup != null) {
				try {
					sCdBranchGroupService.deleteAll(dCdBreanchGroup);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			}
		}

		if (mFuncCode == 1 || mFuncCode == 2) {
			for (int i = 1; i <= 10; i++) {

				if (titaVo.getParam("Group" + i) != null && titaVo.getParam("Group" + i).length() > 0) {
					CdBranchGroup tCdBranchGroup = new CdBranchGroup();
					CdBranchGroupId tCdBranchGroupId = new CdBranchGroupId();

					if (i == 10) {
						tCdBranchGroupId.setBranchNo(mBranchNo);
						tCdBranchGroupId.setGroupNo("A");
						tCdBranchGroup.setCdBranchGroupId(tCdBranchGroupId);
						tCdBranchGroup.setGroupItem(titaVo.getParam("Group" + i));
					} else {
						tCdBranchGroupId.setBranchNo(mBranchNo);
						tCdBranchGroupId.setGroupNo(String.valueOf(i));
						tCdBranchGroup.setCdBranchGroupId(tCdBranchGroupId);
						tCdBranchGroup.setGroupItem(titaVo.getParam("Group" + i));
					}

					try {
						sCdBranchGroupService.insert(tCdBranchGroup, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());

					}
				}
			}
		}

	}
}