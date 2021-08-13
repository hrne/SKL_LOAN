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
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Id;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R28")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R28 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R28.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ056Service sJcicZ056Service;
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
		this.info("active L8R28 ");
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
		sbLogger.append("\r\n L8R28");
		sbLogger.append("iCustId=[" + iCustId + "]\r\n");
		// sbLogger.append("iRcDate=["+iRcDate+"]\r\n");
		sbLogger.append("RimSubmitKey=[" + RimSubmitKey + "]\r\n");
		// sbLogger.append("RimMaxMainCode=["+RimMaxMainCode+"]\r\n");
		sbLogger.append("RimCaseStatus=[" + RimCaseStatus + "]\r\n");
		sbLogger.append("RimClaimDate=[" + RimClaimDate + "]\r\n");
		this.info(sbLogger.toString());

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ056Id JcicZ056IdVO = new JcicZ056Id();
		JcicZ056IdVO.setCaseStatus(RimCaseStatus);
		JcicZ056IdVO.setClaimDate(iRimClaimDate);
		JcicZ056IdVO.setCourtCode(RimCourtCode);
		JcicZ056IdVO.setCustId(iCustId);
		JcicZ056IdVO.setSubmitKey(RimSubmitKey);
		JcicZ056 JcicZ056VO = sJcicZ056Service.findById(JcicZ056IdVO, titaVo);
		if (JcicZ056VO != null) {
			totaVo.putParam("L8r28TranKey", jcicCom.changeTranKey(JcicZ056VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r28CustId", JcicZ056VO.getCustId());// 身分證字號
//			totaVo.putParam("L8r28RcDate",JcicZ056VO.getRcDate());//協商申請日
			totaVo.putParam("L8r28SubmitKey", JcicZ056VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r28SubmitKeyX", jcicCom.FinCodeName(JcicZ056VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r28ClaimDate", jcicCom.DcToRoc(String.valueOf(JcicZ056VO.getClaimDate()), 0));// 裁定日期
			totaVo.putParam("L8r28OutJcicTxtDate", JcicZ056VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

//			totaVo.putParam("L8r28MaxMainCode",JcicZ056VO.getMaxMainCode());//最大債權金融機構代號
			totaVo.putParam("L8r28CaseStatus", JcicZ056VO.getCaseStatus());// 案件狀態
			totaVo.putParam("L8r28CourtCode", JcicZ056VO.getCourtCode());// 承審法院代碼
			// 以上為KEY VALUE
			totaVo.putParam("L8r28Year", jcicCom.DcToRoc(String.valueOf(JcicZ056VO.getYear()), 2));// 年度別
			totaVo.putParam("L8r28CourtDiv", JcicZ056VO.getCourtDiv());// 法院承審股別
			totaVo.putParam("L8r28CourtCaseNo", JcicZ056VO.getCourtCaseNo());// 法院案號
			totaVo.putParam("L8r28Approve", JcicZ056VO.getApprove());// 法院裁定免責確定
			totaVo.putParam("L8r28OutstandAmt", JcicZ056VO.getOutstandAmt());// 原始債權金額
			totaVo.putParam("L8r28ClaimStatus1", JcicZ056VO.getClaimStatus1());// 法院裁定保全處分
			totaVo.putParam("L8r28SaveDate", JcicZ056VO.getSaveDate());// 保全處分起始日
			totaVo.putParam("L8r28ClaimStatus2", JcicZ056VO.getClaimStatus2());// 法院裁定撤銷保全處分
			totaVo.putParam("L8r28SaveEndDate", JcicZ056VO.getSaveEndDate());// 保全處分撤銷日
			totaVo.putParam("L8r28SubAmt", JcicZ056VO.getSubAmt());// 清算損失金額
			totaVo.putParam("L8r28AdminName", JcicZ056VO.getAdminName());// 管理人姓名
		} else {
			// 新增
			totaVo.putParam("L8r28TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r28CustId", iCustId);// 身分證字號
			// totaVo.putParam("L8r28RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r28SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r28SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r28OutJcicTxtDate", "");// 轉出JCIC文字檔日期
			totaVo.putParam("L8r28CaseStatus", "");// 案件狀態
			totaVo.putParam("L8r28ClaimDate", iRimClaimDate);// 裁定日期
			totaVo.putParam("L8r28CourtCode", "");// 承審法院代碼

			// 以上為KEY VALUE
			totaVo.putParam("L8r28Year", "");// 年度別
			totaVo.putParam("L8r28CourtDiv", "");// 法院承審股別
			totaVo.putParam("L8r28CourtCaseNo", "");// 法院案號
			totaVo.putParam("L8r28Approve", "");// 法院裁定免責確定
			totaVo.putParam("L8r28OutstandAmt", "");// 原始債權金額
			totaVo.putParam("L8r28ClaimStatus1", "");// 法院裁定保全處分
			totaVo.putParam("L8r28SaveDate", "");// 保全處分起始日
			totaVo.putParam("L8r28ClaimStatus2", "");// 法院裁定撤銷保全處分
			totaVo.putParam("L8r28SaveEndDate", "");// 保全處分撤銷日
			totaVo.putParam("L8r28SubAmt", "");// 清算損失金額
			totaVo.putParam("L8r28AdminName", "");// 管理人姓名
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r28");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}