package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ055Id;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R27")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R27 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R27.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ055Service sJcicZ055Service;
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
		this.info("active L8R27 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
//		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
//		String RimMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();//最大債權金融機構代號

		String RimCaseStatus = titaVo.getParam("RimCaseStatus").trim();// 案件狀態
		String RimClaimDate = titaVo.getParam("RimClaimDate").trim();// 裁定日期
		String RimCourtCode = titaVo.getParam("RimCourtCode").trim();// 承審法院代碼

		int iRimClaimDate = Integer.parseInt(RimClaimDate) + 19110000;
		StringBuffer sbLogger = new StringBuffer();
		sbLogger.append("\r\n L8R27");
		sbLogger.append("iCustId=[" + iCustId + "]\r\n");
		// sbLogger.append("iRcDate=["+iRcDate+"]\r\n");
		sbLogger.append("RimSubmitKey=[" + RimSubmitKey + "]\r\n");
		// sbLogger.append("RimMaxMainCode=["+RimMaxMainCode+"]\r\n");
		sbLogger.append("RimCaseStatus=[" + RimCaseStatus + "]\r\n");
		sbLogger.append("RimClaimDate=[" + RimClaimDate + "]\r\n");
		this.info(sbLogger.toString());

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ055Id JcicZ055IdVO = new JcicZ055Id();
		JcicZ055IdVO.setCaseStatus(RimCaseStatus);
		JcicZ055IdVO.setClaimDate(iRimClaimDate);
		JcicZ055IdVO.setCourtCode(RimCourtCode);
		JcicZ055IdVO.setCustId(iCustId);
		JcicZ055IdVO.setSubmitKey(RimSubmitKey);
		JcicZ055 JcicZ055VO = sJcicZ055Service.findById(JcicZ055IdVO, titaVo);
		if (JcicZ055VO != null) {
			totaVo.putParam("L8r27TranKey", jcicCom.changeTranKey(JcicZ055VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r27CustId", JcicZ055VO.getCustId());// 身分證字號
//			totaVo.putParam("L8r27RcDate",JcicZ055VO.getRcDate());//協商申請日
			totaVo.putParam("L8r27SubmitKey", JcicZ055VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r27OutJcicTxtDate", JcicZ055VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

//			totaVo.putParam("L8r27MaxMainCode",JcicZ055VO.getMaxMainCode());//最大債權金融機構代號
			totaVo.putParam("L8r27CaseStatus", JcicZ055VO.getCaseStatus());// 案件狀態
			totaVo.putParam("L8r27ClaimDate", jcicCom.DcToRoc(String.valueOf(JcicZ055VO.getClaimDate()), 0));// 裁定日期
			totaVo.putParam("L8r27CourtCode", JcicZ055VO.getCourtCode());// 承審法院代碼
			
			totaVo.putParam("L8r27SubmitKeyX", jcicCom.FinCodeName(JcicZ055VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r27Year", jcicCom.DcToRoc(String.valueOf(JcicZ055VO.getYear()), 2));// 年度別
			totaVo.putParam("L8r27CourtDiv", JcicZ055VO.getCourtDiv());// 法院承審股別
			totaVo.putParam("L8r27CourtCaseNo", JcicZ055VO.getCourtCaseNo());// 法院案號
			totaVo.putParam("L8r27PayDate", JcicZ055VO.getPayDate());// 更生方案首期應繳款日
			totaVo.putParam("L8r27PayEndDate", JcicZ055VO.getPayEndDate());// 更生方案末期應繳款日
			totaVo.putParam("L8r27Period", JcicZ055VO.getPeriod());// 更生條件(期數)
			totaVo.putParam("L8r27Rate", JcicZ055VO.getRate());// 更生條件(利率)
			totaVo.putParam("L8r27OutstandAmt", JcicZ055VO.getOutstandAmt());// 原始債權金額
			totaVo.putParam("L8r27SubAmt", JcicZ055VO.getSubAmt());// 更生損失金額
			totaVo.putParam("L8r27ClaimStatus1", JcicZ055VO.getClaimStatus1());// 法院裁定保全處分
			totaVo.putParam("L8r27SaveDate", JcicZ055VO.getSaveDate());// 保全處分起始日
			totaVo.putParam("L8r27ClaimStatus2", JcicZ055VO.getClaimStatus2());// 法院裁定撤銷保全處分
			totaVo.putParam("L8r27SaveEndDate", JcicZ055VO.getSaveEndDate());// 保全處分撤銷日
			totaVo.putParam("L8r27IsImplement", JcicZ055VO.getIsImplement());// 是否依更生條件履行
			totaVo.putParam("L8r27InspectName", JcicZ055VO.getInspectName());// 監督人姓名
		} else {
			// 新增
			totaVo.putParam("L8r27TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r27CustId", iCustId);// 身分證字號
			// totaVo.putParam("L8r27RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r27SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r27OutJcicTxtDate", "");// 轉出JCIC文字檔日期
			totaVo.putParam("L8r27CaseStatus", "");// 案件狀態
			totaVo.putParam("L8r27ClaimDate", iRimClaimDate);// 裁定日期
			totaVo.putParam("L8r27CourtCode", "");// 承審法院代碼

			totaVo.putParam("L8r27SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r27Year", "");// 年度別
			totaVo.putParam("L8r27CourtDiv", "");// 法院承審股別
			totaVo.putParam("L8r27CourtCaseNo", "");// 法院案號
			totaVo.putParam("L8r27PayDate", "");// 更生方案首期應繳款日
			totaVo.putParam("L8r27PayEndDate", "");// 更生方案末期應繳款日
			totaVo.putParam("L8r27Period", "");// 更生條件(期數)
			totaVo.putParam("L8r27Rate", "");// 更生條件(利率)
			totaVo.putParam("L8r27OutstandAmt", "");// 原始債權金額
			totaVo.putParam("L8r27SubAmt", "");// 更生損失金額
			totaVo.putParam("L8r27ClaimStatus1", "");// 法院裁定保全處分
			totaVo.putParam("L8r27SaveDate", "");// 保全處分起始日
			totaVo.putParam("L8r27ClaimStatus2", "");// 法院裁定撤銷保全處分
			totaVo.putParam("L8r27SaveEndDate", "");// 保全處分撤銷日
			totaVo.putParam("L8r27IsImplement", "");// 是否依更生條件履行
			totaVo.putParam("L8r27InspectName", "");// 監督人姓名

			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r27");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}