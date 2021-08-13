package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;

@Service("L8R54")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R54 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R54.class);
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ444Service sJcicZ444Service;
	@Autowired
	public JcicCom jcicCom;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R54 ");
		this.totaVo.init(titaVo);
		String CustId = titaVo.getParam("RimCustId").trim();// 身分證字號
//		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
//		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
//		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;
		this.info("L8R54={CustId=["+CustId+"] }" );
		//自動由JCICZ048,JCICZ444等資料代入 [通訊電話]-手機,家庭電話 為優先順
		
		String Connect[]= {"",""};
		if(CustId!=null && CustId.length()!=0) {
			Connect=SerchJcicZ048(CustId,titaVo);
			if(Connect!=null && (Connect[0]!=null && Connect[0].length()!=0)) {
				
			}else{
				Connect=SerchJcicZ444(CustId,titaVo);
			}
		}else {
			
		}

		totaVo.putParam("L8r54PhoneNo", Connect[0]);// 通訊電話
		totaVo.putParam("L8r54PhoneNoRemark", Connect[1]);//備註
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public String[] SerchJcicZ048(String CustId,TitaVo titaVo) throws LogicException {
		String PhoneNo="";
		String PhoneNoRemark="";
		Slice<JcicZ048> slJcicZ048 = sJcicZ048Service.CustIdEq(CustId, 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ048> lJcicZ048 = slJcicZ048 == null ? null : slJcicZ048.getContent();
		if(lJcicZ048!=null && lJcicZ048.size()!=0) {
			for(JcicZ048 JcicZ048Vo:lJcicZ048) {
				String testConnect[][]= {{JcicZ048Vo.getCustMobilNo().trim(),"行動電話"},{JcicZ048Vo.getCustComTelNo().trim(),"通訊電話"},{JcicZ048Vo.getCustRegTelNo().trim(),"戶籍電話"}};
				for(int i=0;i<testConnect.length;i++) {
					String connect=testConnect[i][0];
					String connectRemark=testConnect[i][1];
					if(connect!=null && connect.length()!=0) {
						PhoneNo=connect;
						if(PhoneNo!=null && PhoneNo.length()!=0) {
							PhoneNoRemark=""+connectRemark+" JcicZ048 協商申請日:"+jcicCom.DcToRoc(String.valueOf(JcicZ048Vo.getJcicZ048Id().getRcDate()),0)+" 報送單位代號:"+JcicZ048Vo.getJcicZ048Id().getSubmitKey()+"";
							break;
						}
					}
				}

			}
		}
		String ConnectJcicZ048[]= {PhoneNo,PhoneNoRemark};
		return ConnectJcicZ048;
	}
	
	public String[] SerchJcicZ444(String CustId,TitaVo titaVo) throws LogicException {
		String PhoneNo="";
		String PhoneNoRemark="";
		Slice<JcicZ444> slJcicZ444 = sJcicZ444Service.CustIdEq(CustId, 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ444> lJcicZ444 = slJcicZ444 == null ? null : slJcicZ444.getContent();
		if(lJcicZ444!=null && lJcicZ444.size()!=0) {
			for(JcicZ444 JcicZ444Vo:lJcicZ444) {
				String testConnect[][]= {{JcicZ444Vo.getCustMobilNo().trim(),"行動電話"},{JcicZ444Vo.getCustComTelNo().trim(),"通訊電話"},{JcicZ444Vo.getCustRegTelNo().trim(),"戶籍電話"}};
				for(int i=0;i<testConnect.length;i++) {
					String connect=testConnect[i][0];
					String connectRemark=testConnect[i][1];
					if(connect!=null && connect.length()!=0) {
						PhoneNo=connect;
						if(PhoneNo!=null && PhoneNo.length()!=0) {
							PhoneNoRemark=""+connectRemark+" JcicZ444 統一款項收付申請日:"+jcicCom.DcToRoc(String.valueOf(JcicZ444Vo.getJcicZ444Id().getApplyDate()),0)+" 報送單位代號:"+JcicZ444Vo.getJcicZ444Id().getSubmitKey()+"";
							break;
						}
					}
				}

			}
		}
		

		String ConnectJcicZ444[]= {PhoneNo,PhoneNoRemark};
		return ConnectJcicZ444;
		}
}