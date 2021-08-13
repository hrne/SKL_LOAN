package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegQueryCust;
import com.st1.itx.db.domain.NegQueryCustId;
import com.st1.itx.db.service.NegQueryCustService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Service("L575A")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L575A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L575A.class);
	@Autowired
	public NegQueryCustService sNegQueryCustService;
	@Autowired
	DateUtil dateUtil;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5705File ");
		this.totaVo.init(titaVo);
		String strToday=titaVo.getCalDy();//日曆日
		
		int Today=0;
		String CustId = titaVo.getParam("TxCustId").trim();//身份證字號
		String btnIndex=titaVo.getBtnIndex();//0~N第幾個按鈕
		switch (btnIndex) {
		case "0":
			//產生請求檔
			this.info("L5705File Today=["+Today+"],CustId=["+CustId+"]");
			if(String.valueOf(Integer.parseInt(strToday)).length()!=8) {
				Today=Integer.parseInt(strToday)+19110000;
			}
			NegQueryCustId NegQueryCustIdVo=new NegQueryCustId();
			NegQueryCustIdVo.setAcDate(Today);
			NegQueryCustIdVo.setCustId(CustId);
			
			NegQueryCust NegQueryCustVo=sNegQueryCustService.findById(NegQueryCustIdVo, titaVo);
			if(NegQueryCustVo!=null) {
				if(("Y").equals(NegQueryCustVo.getFileYN())) {
					NegQueryCustVo.setFileYN("N");
					NegQueryCustVo.setSeqNo(NegQueryCustVo.getSeqNo()+1);
					try {
						sNegQueryCustService.update(NegQueryCustVo, titaVo);//資料異動
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "");
					}
				}
			}else {
				NegQueryCustVo=new NegQueryCust();
				NegQueryCustVo.setNegQueryCustId(NegQueryCustIdVo);
				NegQueryCustVo.setFileYN("N");
				NegQueryCustVo.setSeqNo(1);
				try {
					sNegQueryCustService.insert(NegQueryCustVo, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
//	public int NegQuerySeq(int Today,TitaVo titaVo) throws LogicException {
//		int seq=1;
//		Slice<NegQueryCust> slNegQueryCust=sNegQueryCustService.FirstAcDate(Today, "N", 0, Integer.MAX_VALUE, titaVo);
//		List<NegQueryCust> lNegQueryCust = slNegQueryCust == null ? null : slNegQueryCust.getContent();
//		if(lNegQueryCust!=null ) {
//			seq=lNegQueryCust.get(0).getSeqNo();
//		}else {
//			
//		}
//		return seq;
//	}
}