package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.domain.CdClId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimClCode1=9,1
 * RimClCode2=9,2
 */
@Service("L6R05") // 尋找擔保品代號資料檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R05 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R05.class);

	/* DB服務注入 */
	@Autowired
	public CdClService sCdClService;
	
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R05 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		int iRimClCode1 = this.parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iRimClCode2 = this.parse.stringToInteger(titaVo.getParam("RimClCode2"));

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R05"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R05"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdCl(new CdCl());
		String ClCode1Item="";
		//查共用代碼檔名稱
		CdCode tCdCode = sCdCodeService.findById(new CdCodeId("ClCode1","1"), titaVo);
		if(tCdCode!=null) {
			ClCode1Item = tCdCode.getItem();
		}
		// 查詢擔保品代號資料檔
		CdCl tCdCl = sCdClService.findById(new CdClId(iRimClCode1, iRimClCode2), titaVo);

		/* 如有找到資料 */
		if (tCdCl != null) {
			if (iRimTxCode.equals("L6603") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimClCode1")+"-"+titaVo.getParam("RimClCode2")+"/"+ClCode1Item+"-"+tCdCl.getClItem()); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdCl(tCdCl);
			}
		} else {
			if (iRimTxCode.equals("L6603") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "擔保品代號資料檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 擔保品代號資料檔
	private void moveTotaCdCl(CdCl mCdCl) throws LogicException {
		this.totaVo.putParam("L6R05ClCode1", mCdCl.getClCode1());
		this.totaVo.putParam("L6R05ClCode2", mCdCl.getClCode2());
		this.totaVo.putParam("L6R05ClItem", mCdCl.getClItem());
		this.totaVo.putParam("L6R05ClTypeJCIC", mCdCl.getClTypeJCIC());
	}

}