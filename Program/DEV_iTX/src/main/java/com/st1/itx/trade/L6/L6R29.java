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
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * L6R29ClCode1=9,1
 */
@Service("L6R29")
@Scope("prototype")
/**
 * 查詢擔保品代號檔 ; 組擔保品名稱清單
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R29 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R29.class);

	/* DB服務注入 */
	@Autowired
	public CdClService sCdClService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R29 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 60000;

		// 取得輸入資料
		int iRimClCode1 = Integer.valueOf(titaVo.getParam("L6R29ClCode1"));

		// 查擔保品代號檔
		List<CdCl> listCdCl = new ArrayList<CdCl>();
		Slice<CdCl> slCdCl = sCdClService.clCode1Eq(iRimClCode1, iRimClCode1, this.index, this.limit, titaVo);
		listCdCl = slCdCl == null ? null : slCdCl.getContent();

		if (listCdCl == null || listCdCl.size() == 0) {
			// throw new LogicException("E0001", "擔保品代號檔"); // 查無資料
			this.totaVo.putParam("L6r29clCodeHelpList", "");
			this.addList(this.totaVo);
			return this.sendList();
		}

		ArrayList<String> clCodeHelpList = new ArrayList<String>();

		this.info("L6R29 listCdCl.size() = " + listCdCl.size());

		for (CdCl tCdCl : listCdCl) {
			String ClCode2 = String.valueOf(tCdCl.getClCode2());
			if(ClCode2.length()==1) {
				ClCode2=0+ClCode2;
			}
			
			String ClItem = tCdCl.getClItem().trim();
			String codeHelp = ClCode2 + ": " + ClItem;
			this.info("L6R29 add codeHelp = " + codeHelp);
			clCodeHelpList.add(codeHelp);
		}

		String codeHelp = "";

		int listSize = clCodeHelpList.size();
		this.info("L6R29 clCodeHelpList size = " + listSize);

		int i = 1;

		for (String tmpCodeHelp : clCodeHelpList) {
			if (i == 1) {
				codeHelp = tmpCodeHelp;
			} else {
				codeHelp += tmpCodeHelp;
			}
			if (i < listSize) {
				codeHelp += ";";
			}
			i++;
		}
		this.info("L6R29 final codeHelp = " + codeHelp);
		this.info("L6R29 codeHelp.length() = " + codeHelp.length());

		this.totaVo.putParam("L6r29clCodeHelpList", codeHelp);

		this.addList(this.totaVo);
		return this.sendList();
	}

}