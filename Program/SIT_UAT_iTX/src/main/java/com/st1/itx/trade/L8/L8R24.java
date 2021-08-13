package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R24")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R24 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R24.class);

	/* DB服務注入 */
	@Autowired
	public JcicZ052Service sJcicZ052Service;
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
		this.info("active L8R24 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R24 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ052Id JcicZ052IdVO = new JcicZ052Id();
		JcicZ052IdVO.setCustId(iCustId);
		JcicZ052IdVO.setRcDate(iDcRcDate);
		JcicZ052IdVO.setSubmitKey(RimSubmitKey);
		JcicZ052 JcicZ052VO = sJcicZ052Service.findById(JcicZ052IdVO, titaVo);
		if (JcicZ052VO != null) {
			totaVo.putParam("L8r24TranKey", jcicCom.changeTranKey(JcicZ052VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r24CustId", JcicZ052VO.getCustId());// 身分證字號
			totaVo.putParam("L8r24RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ052VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r24SubmitKey", JcicZ052VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r24OutJcicTxtDate", JcicZ052VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期
			totaVo.putParam("L8r24SubmitKeyX", jcicCom.FinCodeName(JcicZ052VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			String L8r24BankCode1=JcicZ052VO.getBankCode1();
			String L8r24BankCode2=JcicZ052VO.getBankCode2();
			String L8r24BankCode3=JcicZ052VO.getBankCode3();
			String L8r24BankCode4=JcicZ052VO.getBankCode4();
			String L8r24BankCode5=JcicZ052VO.getBankCode5();
			totaVo.putParam("L8r24BankCode1", L8r24BankCode1);// 同意報送債權機構代號1
			totaVo.putParam("L8r24DataCode1", JcicZ052VO.getDataCode1());// 同意報送檔案格式資料別1
			totaVo.putParam("L8r24BankCode2", L8r24BankCode2);// 同意報送債權機構代號2
			totaVo.putParam("L8r24DataCode2", JcicZ052VO.getDataCode2());// 同意報送檔案格式資料別2
			totaVo.putParam("L8r24BankCode3", L8r24BankCode3);// 同意報送債權機構代號3
			totaVo.putParam("L8r24DataCode3", JcicZ052VO.getDataCode3());// 同意報送檔案格式資料別3
			totaVo.putParam("L8r24BankCode4", L8r24BankCode4);// 同意報送債權機構代號4
			totaVo.putParam("L8r24DataCode4", JcicZ052VO.getDataCode4());// 同意報送檔案格式資料別4
			totaVo.putParam("L8r24BankCode5", L8r24BankCode5);// 同意報送債權機構代號5
			totaVo.putParam("L8r24DataCode5", JcicZ052VO.getDataCode5());// 同意報送檔案格式資料別5
			totaVo.putParam("L8r24ChangePayDate", JcicZ052VO.getChangePayDate());// 申請變更還款條件日
			
			String strBankCode[]= {L8r24BankCode1,L8r24BankCode2,L8r24BankCode3,L8r24BankCode4,L8r24BankCode5};
			int BankCodeL=strBankCode.length;
			List<String> lJcicBank=new ArrayList<String>();
			for(int i=0;i<BankCodeL;i++) {
				String BankCodeKey=strBankCode[i];
				if(BankCodeKey!=null && BankCodeKey.length()!=0) {
					if(!lJcicBank.contains(BankCodeKey)) {
						lJcicBank.add(BankCodeKey);
					}
				}
			}
			if(lJcicBank!=null && lJcicBank.size()!=0) {
				Map<String,String>jcicBank=jcicCom.JcicBankCodeMapForRim(lJcicBank, titaVo);
				for(int i=0;i<BankCodeL;i++) {
					String thisRow=String.valueOf(i+1);
					String BankCodeX=jcicBank.get(strBankCode[i]);
					if(BankCodeX!=null) {
						
					}else {
						BankCodeX="";
					}
					totaVo.putParam("L8r24BankCodeX"+thisRow,BankCodeX);// 同意報送債權機構代號1
				}
			}else {
				totaVo.putParam("L8r24BankCodeX1", "");// 同意報送債權機構代號1
				totaVo.putParam("L8r24BankCodeX2", "");// 同意報送債權機構代號1
				totaVo.putParam("L8r24BankCodeX3", "");// 同意報送債權機構代號1
				totaVo.putParam("L8r24BankCodeX4", "");// 同意報送債權機構代號1
				totaVo.putParam("L8r24BankCodeX5", "");// 同意報送債權機構代號1
			}
			
		} else {
			// 新增
			totaVo.putParam("L8r24TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r24CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r24RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r24SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r24OutJcicTxtDate", "");// 轉出JCIC文字檔日期
			totaVo.putParam("L8r24SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r24BankCode1", "");// 同意報送債權機構代號1
			totaVo.putParam("L8r24DataCode1", "");// 同意報送檔案格式資料別1
			totaVo.putParam("L8r24BankCode2", "");// 同意報送債權機構代號2
			totaVo.putParam("L8r24DataCode2", "");// 同意報送檔案格式資料別2
			totaVo.putParam("L8r24BankCode3", "");// 同意報送債權機構代號3
			totaVo.putParam("L8r24DataCode3", "");// 同意報送檔案格式資料別3
			totaVo.putParam("L8r24BankCode4", "");// 同意報送債權機構代號4
			totaVo.putParam("L8r24DataCode4", "");// 同意報送檔案格式資料別4
			totaVo.putParam("L8r24BankCode5", "");// 同意報送債權機構代號5
			totaVo.putParam("L8r24DataCode5", "");// 同意報送檔案格式資料別5
			totaVo.putParam("L8r24ChangePayDate", "");// 申請變更還款條件日
			
			totaVo.putParam("L8r24BankCodeX1", "");// 同意報送債權機構代號1
			totaVo.putParam("L8r24BankCodeX2", "");// 同意報送債權機構代號1
			totaVo.putParam("L8r24BankCodeX3", "");// 同意報送債權機構代號1
			totaVo.putParam("L8r24BankCodeX4", "");// 同意報送債權機構代號1
			totaVo.putParam("L8r24BankCodeX5", "");// 同意報送債權機構代號1
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r24");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}