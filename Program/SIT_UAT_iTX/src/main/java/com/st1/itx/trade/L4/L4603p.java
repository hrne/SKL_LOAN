package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

/**
 * L4603p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L4603p")
@Scope("prototype")
public class L4603p extends TradeBuffer {

	@Autowired
	public InsuRenewService insuRenewService;
	
	@Autowired
	public CustNoticeCom custNoticeCom;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	public LoanBorMainService loanBorMainService;
	
	@Autowired
	public CustMainService custMainService;
	
	@Autowired
	L4603Report l4603report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public Parse parse;
	
	@Value("${iTXOutFolder}")
	private String outFolder = "";
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4603p ");
		this.totaVo.init(titaVo);

		this.info("L4603p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		int iInsuEndMonth = 0;
		l4603report.setParentTranCode(parentTranCode);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		
			String subject = "火險及地震險保費-繳款通知單 ";
			for (InsuRenew t : slInsuRenew.getContent()) {
				
				TempVo tempVo = new TempVo();
				tempVo = custNoticeCom.getCustNotice("L4603", t.getCustNo(), t.getFacmNo(), titaVo);
				
				if ("Y".equals(tempVo.getParam("isEmail"))) {
					
					l4603report.exec(titaVo, t, this.getTxBuffer());
					
					String noticeEmail = tempVo.getParam("EmailAddress");		
					
//					mailService.setParams(tempVo.getParam("EmailAddress"), subject, bodyText);
					String bodyText = "親愛的客戶，繳款通知"+"\n"+"新光人壽關心您。";
					
					mailService.setParams("skcu31780001@skl.com.tw", subject, bodyText);
					mailService.setParams("", outFolder + "火險及地震險保費-繳款通知單.pdf");
					mailService.exec();
					
				}
			}

//		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L4603滯留客戶明細表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
	
}