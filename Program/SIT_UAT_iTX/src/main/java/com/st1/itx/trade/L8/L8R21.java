package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R21")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R21 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R21.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicCom jcicCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R21 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();//身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();//報送單位代號

		int iDcRcDate=Integer.parseInt(iRcDate)+19110000;
		this.info("L8R21 RimCustId=["+iCustId+"],iRcDate=["+iDcRcDate+"],iSubmitKey=["+RimSubmitKey+"]");
		//查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ047Id JcicZ047IdVO =new JcicZ047Id();
		JcicZ047IdVO.setCustId(iCustId);
		JcicZ047IdVO.setRcDate(iDcRcDate);
		JcicZ047IdVO.setSubmitKey(RimSubmitKey);
		JcicZ047 JcicZ047VO=sJcicZ047Service.findById(JcicZ047IdVO,titaVo);
		
		String L8r21CheckZ047="0";//0未找到 1 找到
		int L8r21Z047SignDate=0;
		if(JcicZ047VO!=null) {
			L8r21CheckZ047="1";
			L8r21Z047SignDate=JcicZ047VO.getSignDate();
		}
		totaVo.putParam("L8r21CheckZ047", L8r21CheckZ047);
		totaVo.putParam("L8r21Z047SignDate", L8r21Z047SignDate);
		this.addList(this.totaVo);
		return this.sendList();
	}
}