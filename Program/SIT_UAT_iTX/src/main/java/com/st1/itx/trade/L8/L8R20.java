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
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Id;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R20")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R20 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R20.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ049Service sJcicZ049Service;
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
		this.info("active L8R20 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();//身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();//報送單位代號
		
		int iDcRcDate=Integer.parseInt(iRcDate)+19110000;
		this.info("L8R20 RimCustId=["+iCustId+"],iRcDate=["+iRcDate+"],iSubmitKey=["+RimSubmitKey+"]");
		//查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ049Id JcicZ049IdVO=new JcicZ049Id();
		JcicZ049IdVO.setCustId(iCustId);
		JcicZ049IdVO.setRcDate(iDcRcDate);
		JcicZ049IdVO.setSubmitKey(RimSubmitKey);
		JcicZ049 JcicZ049VO=sJcicZ049Service.findById(JcicZ049IdVO,titaVo);
		if(JcicZ049VO!=null) {
			totaVo.putParam("L8r20TranKey", jcicCom.changeTranKey(JcicZ049VO.getTranKey()));//[交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r20CustId",JcicZ049VO.getCustId());//身分證字號
			totaVo.putParam("L8r20RcDate",jcicCom.DcToRoc(String.valueOf(JcicZ049VO.getRcDate()), 0));//協商申請日
			totaVo.putParam("L8r20SubmitKey",JcicZ049VO.getSubmitKey());//報送單位代號
			totaVo.putParam("L8r20OutJcicTxtDate",JcicZ049VO.getOutJcicTxtDate());//轉出JCIC文字檔日期

			totaVo.putParam("L8r20SubmitKeyX", jcicCom.FinCodeName(JcicZ049VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			//以上為KEY VALUE
			totaVo.putParam("L8r20ClaimStatus",JcicZ049VO.getClaimStatus());//案件進度
			totaVo.putParam("L8r20ApplyDate",JcicZ049VO.getApplyDate());//遞狀日
			totaVo.putParam("L8r20CourtCode",JcicZ049VO.getCourtCode());//承審法院代碼
			totaVo.putParam("L8r20Year",jcicCom.DcToRoc(String.valueOf(JcicZ049VO.getYear()), 2));//年度別
			totaVo.putParam("L8r20CourtDiv",JcicZ049VO.getCourtDiv());//法院承審股別
			totaVo.putParam("L8r20CourtCaseNo",JcicZ049VO.getCourtCaseNo());//法院案號
			totaVo.putParam("L8r20Approve",JcicZ049VO.getApprove());//法院認可與否
			totaVo.putParam("L8r20ClaimDate",JcicZ049VO.getClaimDate());//法院裁定日期
		}else {
			//新增
			totaVo.putParam("L8r20TranKey", "A");//[交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r20CustId",iCustId);//身分證字號
			totaVo.putParam("L8r20RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r20SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r20OutJcicTxtDate","");//轉出JCIC文字檔日期
			
			totaVo.putParam("L8r20SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			//以上為KEY VALUE
			totaVo.putParam("L8r20ClaimStatus","");//案件進度
			totaVo.putParam("L8r20ApplyDate","");//遞狀日
			totaVo.putParam("L8r20CourtCode","");//承審法院代碼
			totaVo.putParam("L8r20Year","");//年度別
			totaVo.putParam("L8r20CourtDiv","");//法院承審股別
			totaVo.putParam("L8r20CourtCaseNo","");//法院案號
			totaVo.putParam("L8r20Approve","");//法院認可與否
			totaVo.putParam("L8r20ClaimDate","");//法院裁定日期
			//throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r20");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}