package com.st1.itx.trade.L6;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdOverdue;
import com.st1.itx.db.domain.CdOverdueId;
import com.st1.itx.db.service.CdOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimOverdueSign=X,1
 * RimOverdueCode=X,4
 */
@Service("L6R07") // 尋找逾期新增減少原因檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R07 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R07.class);

	/* DB服務注入 */
	@Autowired
	public CdOverdueService sCdOverdueService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R07 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimOverdueSign = titaVo.getParam("RimOverdueSign");
		String iRimOverdueCode = titaVo.getParam("RimOverdueCode");
		String dOverdueCode = "";
		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R07"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R07"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdOverdue(new CdOverdue());

		// 查詢逾期新增減少原因檔
		CdOverdue tCdOverdue = sCdOverdueService.findById(new CdOverdueId(iRimOverdueSign, iRimOverdueCode), titaVo);
		
	
		/* 如有找到資料 */
		if (tCdOverdue != null) {
			if (iRimTxCode.equals("L6605") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimOverdueSign")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdOverdue(tCdOverdue);
			}
		} else {
			//檢查是否先建立X000增減原因
			tCdOverdue = sCdOverdueService.findById(new CdOverdueId(iRimOverdueSign, iRimOverdueCode.substring(0,1)+"000"), titaVo);
			if(tCdOverdue==null) {
				if(!("000").equals(iRimOverdueCode.substring(1,4)))
				throw new LogicException(titaVo, "", "請先建立"+iRimOverdueCode.substring(0,1)+"000,增減原因");
			}
			
			if (iRimTxCode.equals("L6605") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "逾期新增減少原因檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 逾期新增減少原因檔
	private void moveTotaCdOverdue(CdOverdue mCdOverdue) throws LogicException {
		this.totaVo.putParam("L6R07OverdueSign", mCdOverdue.getOverdueSign());
		this.totaVo.putParam("L6R07OverdueCode", mCdOverdue.getOverdueCode());
		this.totaVo.putParam("L6R07OverdueItem", mCdOverdue.getOverdueItem());
	}

}