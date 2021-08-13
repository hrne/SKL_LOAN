package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.parse.Parse;

@Service("L2R44")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R44 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R44.class);

	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	Parse parse;

	private TempVo tempVo = new TempVo();

	// work area
	private int wkCustNo = 0;
	private int wkFacmNo = 0;
	private int wkFunCd = 0;

	private String wkRepayBank = "";
	private String wkRepayAcctNo = "";
	private String wkPostCode = "";
	private String wkAchAuthCode = "";
	private String wkRelationCode = "";
	private String wkRelationName = "";
	private String wkRelationId = "";
	private int wkRelationBirthday = 0;
	private String wkRelationGender = "";
	private String wkAuthStatus ="";
	private String wkMainStatus ="";
	

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R44 ");
		this.totaVo.init(titaVo);

		wkCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		wkFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		wkFunCd = this.parse.stringToInteger(titaVo.getParam("RimFunCode"));

		tempVo = authLogCom.exec(wkCustNo, wkFacmNo, titaVo);

		if (tempVo == null || tempVo.size() == 0) {
//			if (!(wkFunCd == 5) || !(wkFunCd == 4)) {
//				throw new LogicException(titaVo, "E0001", "查無銀扣資料,不得更改(L2R44)"); // 查詢資料不存在
//			}

		} else {
			wkRepayBank = tempVo.getParam("RepayBank");
			wkRepayAcctNo = tempVo.getParam("RepayAcctNo");
			wkPostCode = tempVo.getParam("PostCode");
			wkAchAuthCode = tempVo.getParam("AchAuthCode");
			wkRelationCode = tempVo.getParam("RelationCode");
			wkRelationName = tempVo.getParam("RelationName");
			wkRelationId = tempVo.getParam("RelationId");
			wkRelationBirthday = parse.stringToInteger(tempVo.getParam("RelationBirthday"));
			wkRelationGender = tempVo.getParam("RelationGender");
			wkAuthStatus =  tempVo.getParam("AuthStatus");
			wkMainStatus =  tempVo.getParam("MainStatus");
		}
		this.totaVo.putParam("ORepayBank", wkRepayBank);
		this.totaVo.putParam("ORepayAcctNo", wkRepayAcctNo);
		this.totaVo.putParam("OPostCode", wkPostCode);
		this.totaVo.putParam("OAchAuthCode", wkAchAuthCode);
		this.totaVo.putParam("ORelationCode", wkRelationCode);
		this.totaVo.putParam("ORelationName", wkRelationName);
		this.totaVo.putParam("ORelationId", wkRelationId);
		this.totaVo.putParam("ORelationBirthday", wkRelationBirthday);
		this.totaVo.putParam("ORelationGender", wkRelationGender);
		this.totaVo.putParam("OAuthStatus", wkAuthStatus);
		this.totaVo.putParam("OMainStatus", wkMainStatus);
		this.info("totaVo" + totaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}
}