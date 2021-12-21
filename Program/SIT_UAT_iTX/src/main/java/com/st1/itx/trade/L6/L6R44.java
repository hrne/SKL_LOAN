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
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * L6R44ClCode1=9,1
 */
@Service("L6R44")
@Scope("prototype")
/**
 * 查詢課組別檔 ; 組課組別清單
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R44 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdClService sCdClService;
	@Autowired
	public CdBranchGroupService sCdBranchGroupService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R44 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 60000;

		// 取得輸入資料
		String iBranchNo = titaVo.getParam("L6R44BranchNo");

		// 查擔保品代號檔
		Slice<CdBranchGroup> slCdBranchGroup = sCdBranchGroupService.findByBranchNo(iBranchNo, this.index, this.limit, titaVo);
		List<CdBranchGroup> listCdBranchGroup = slCdBranchGroup == null ? null : slCdBranchGroup.getContent();

		if (listCdBranchGroup == null || listCdBranchGroup.size() == 0) {
			this.totaVo.putParam("L6R44clCodeHelpList", "");
			this.addList(this.totaVo);
			return this.sendList();
		}

		ArrayList<String> groupHelpList = new ArrayList<String>();

		this.info("L6R44 listCdBranchGroup.size() = " + listCdBranchGroup.size());

		for (CdBranchGroup tCdBranchGroup : listCdBranchGroup) {
			String GroupItem = String.valueOf(tCdBranchGroup.getGroupItem());
			String GroupNo = String.valueOf(tCdBranchGroup.getGroupNo());

			GroupItem = GroupNo + ": " + GroupItem;
			groupHelpList.add(GroupItem);
		}

		String groupHelp = "";

		int listSize = groupHelpList.size();
		this.info("L6R44 clCodeHelpList size = " + listSize);

		int i = 1;

		for (String tmpGroupHelp : groupHelpList) {
			if (i == 1) {
				groupHelp = tmpGroupHelp;
			} else {
				groupHelp += tmpGroupHelp;
			}
			if (i < listSize) {
				groupHelp += ";";
			}
			i++;
		}
		this.info("L6R44 final groupHelp = " + groupHelp);
		this.info("L6R44 groupHelp.length() = " + groupHelp.length());

		this.totaVo.putParam("L6r44GroupHelpList", groupHelp);

		this.addList(this.totaVo);
		return this.sendList();
	}

}