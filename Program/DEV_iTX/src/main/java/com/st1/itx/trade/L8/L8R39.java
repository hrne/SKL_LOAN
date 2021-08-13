package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R39")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R39 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R39.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service sJcicZ440Service;
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
		this.info("active L8r39 ");
		this.totaVo.init(titaVo);
		this.info("L8r39rimstart");
//		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
//		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
//		String iApplyDate = titaVo.getParam("RimApplyDate").trim();
//		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
//		int ApplyDate = Integer.parseInt(iApplyDate) + 19110000;
//
//		// 查資料的時候尚未改變 要自己轉民國年與西元年
//		JcicZ440Id lJcicZ440Id = new JcicZ440Id();
//		lJcicZ440Id.setApplyDate(ApplyDate);
//		lJcicZ440Id.setBankId(iBankId);
//		lJcicZ440Id.setCustId(iCustId);
//		lJcicZ440Id.setSubmitKey(RimSubmitKey);
//		JcicZ440 JcicZ440VO = sJcicZ440Service.findById(lJcicZ440Id, titaVo);
//		if (JcicZ440VO != null) {
//			totaVo.putParam("L8r39TranKey", jcicCom.changeTranKey(JcicZ440VO.getTranKey()));// 交易代碼
//			totaVo.putParam("L8r39CustId", JcicZ440VO.getCustId());// 債務人IDN
//			totaVo.putParam("L8r39SubmitKey", JcicZ440VO.getSubmitKey());// 報送單位代號
//			totaVo.putParam("L8r39SubmitKeyX", jcicCom.FinCodeName(JcicZ440VO.getSubmitKey(), 0, titaVo));// 報送單位代號
//			totaVo.putParam("L8r39ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ440VO.getApplyDate()), 0));// 款項統一收付申請日
//			totaVo.putParam("L8r39BankId", JcicZ440VO.getBankId());//受理調解機構代號
//			totaVo.putParam("L8r39BankIdX", jcicCom.Jcic440CourtCodeAndZipCode(JcicZ440VO.getApplyType(), JcicZ440VO.getBankId(),titaVo));//受理調解機構代號
//			totaVo.putParam("L8r39AgreeDate", JcicZ440VO.getAgreeDate());// 同意書取得日期
//			totaVo.putParam("L8r39StartDate", JcicZ440VO.getStartDate());// 首次調解日
//			totaVo.putParam("L8r39RemindDate", JcicZ440VO.getRemindDate());// 債權計算基準日
//			totaVo.putParam("L8r39ApplyType", JcicZ440VO.getApplyType());// 受理方式
//			totaVo.putParam("L8r39ReportYn", JcicZ440VO.getReportYn());// 協辦行是否需自行回報債權
//			
//			String strBankCode[]= {JcicZ440VO.getNotBankId1(),JcicZ440VO.getNotBankId2(),JcicZ440VO.getNotBankId3(),JcicZ440VO.getNotBankId4(),JcicZ440VO.getNotBankId5(),JcicZ440VO.getNotBankId6()};
//			int BankCodeL=strBankCode.length;
//			for(int i=0;i<BankCodeL;i++) {
//				totaVo.putParam("L8r39NotBankId"+(i+1), strBankCode[i]);// 債權金融機構代號
//			}
//			List<String> lJcicBank=new ArrayList<String>();
//			for(int i=0;i<BankCodeL;i++) {
//				String BankCodeKey=strBankCode[i];
//				if(BankCodeKey!=null && BankCodeKey.length()!=0) {
//					if(!lJcicBank.contains(BankCodeKey)) {
//						lJcicBank.add(BankCodeKey);
//					}
//				}
//			}
//			if(lJcicBank!=null && lJcicBank.size()!=0) {
//				Map<String,String>jcicBank=jcicCom.JcicBankCodeMapForRim(lJcicBank, titaVo);
//				for(int i=0;i<BankCodeL;i++) {
//					String thisRow=String.valueOf(i+1);
//					String BankCodeX=jcicBank.get(strBankCode[i]);
//					if(BankCodeX!=null) {
//						
//					}else {
//						BankCodeX="";
//					}
//					totaVo.putParam("L8r39NotBankIdX"+thisRow,BankCodeX);// 同意報送債權機構代號1
//				}
//			}else {
//				// 同意報送債權機構代號
//				for(int i=1;i<=BankCodeL;i++) {
//					totaVo.putParam("L8r39NotBankIdX"+i, "");
//				}
//			}
//			totaVo.putParam("L8r39OutJcicTxtDate", JcicZ440VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
//		} else {
//			// 新增
//			totaVo.putParam("L8r39TranKey", "A");// 交易代碼
//			totaVo.putParam("L8r39CustId", iCustId);// 債務人IDN
//			totaVo.putParam("L8r39SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
//			totaVo.putParam("L8r39SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
//			totaVo.putParam("L8r39ApplyDate", "");// 款項統一收付申請日
//			totaVo.putParam("L8r39BankId", "");// 異動債權金機構代號
//			totaVo.putParam("L8r39BankIdX", "");// 異動債權金機構代號
//			totaVo.putParam("L8r39AgreeDate", "");// 同意書取得日期
//			totaVo.putParam("L8r39StartDate", "");// 首次調解日
//			totaVo.putParam("L8r39RemindDate", "");// 債權計算基準日
//			totaVo.putParam("L8r39ApplyType", "");// 受理方式
//			totaVo.putParam("L8r39ReportYn", "");// 協辦行是否需自行回報債權
//			for(int i=1;i<=6;i++) {
//				totaVo.putParam("L8r39NotBankId"+i, "");// 債權金融機構代號1
//				totaVo.putParam("L8r39NotBankIdX"+i, "");// 債權金融機構代號1
//			}
//			totaVo.putParam("L8r39OutJcicTxtDate", "");// 轉JCIC文字檔日期
//		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}