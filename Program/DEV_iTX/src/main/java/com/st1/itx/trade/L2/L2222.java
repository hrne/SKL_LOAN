package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.domain.FacShareRelation;
import com.st1.itx.db.domain.FacShareRelationId;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.FacShareRelationService;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2222")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2222 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2222.class);

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;
	
	@Autowired
	public FacShareApplService sFacShareApplService;
	
	@Autowired
	public CustMainService sCustMainService;
	
	@Autowired
	public FacMainService sFacMainService;
	
	@Autowired
	public FacShareRelationService sFacShareRelationService;
	
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2222 ");
		this.totaVo.init(titaVo);
		
//		String iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		String creditSysNo = titaVo.getParam("CreditSysNo").toString().trim();
		int iCreditSysNo = 0;
		if (!"".equals(creditSysNo)) {
			iCreditSysNo = parse.stringToInteger(creditSysNo);
		}
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		String relUKey = titaVo.getParam("RelUKey");
		String relCode = titaVo.getParam("RelCode");
		String type = titaVo.getParam("Type").trim();
		
		if("共同借款人".equals(type)) {
			
			FacMainId facMainId = new FacMainId();
			facMainId.setCustNo(iCustNo);
			facMainId.setFacmNo(iFacmNo);
			FacMain tFacMain = sFacMainService.findById(facMainId, titaVo);
			
			if( tFacMain == null) {
				throw new LogicException("E0001", "額度主檔");
			}
			
			
			
			FacShareAppl tFacShareAppl = sFacShareApplService.findById(tFacMain.getApplNo(), titaVo);
			
			if (tFacShareAppl != null) {
				int MainApplNo = tFacShareAppl.getMainApplNo();
				int ApplNo = tFacShareAppl.getApplNo();
				int tempApplNo = 0 ;
				
				
				CustMain tCustMain = sCustMainService.findById(relUKey, titaVo);
				int iCustNo2 = tCustMain.getCustNo();
				
				Slice<FacShareAppl> slFacShareAppl2 = sFacShareApplService.findMainApplNo(MainApplNo, index, limit, titaVo);
				List<FacShareAppl> lFacShareAppl = slFacShareAppl2 == null ? null : slFacShareAppl2.getContent();
				
				for (FacShareAppl tFacShareAppl2 : lFacShareAppl) {
				  if (tFacShareAppl2.getCustNo() == iCustNo2) {
					tempApplNo = tFacShareAppl2.getApplNo();
				  }
				}
				
				FacShareRelationId tFacShareRelationId = new FacShareRelationId();
				tFacShareRelationId.setApplNo(ApplNo);
				tFacShareRelationId.setRelApplNo(tempApplNo);
				FacShareRelation tFacShareRelation = sFacShareRelationService.findById(tFacShareRelationId, titaVo);
				
				if (tFacShareRelation == null) {
					tFacShareRelation = new FacShareRelation();
					tFacShareRelation.setFacShareRelationId(tFacShareRelationId);
					tFacShareRelation.setRelCode(relCode);
					try {
						sFacShareRelationService.insert(tFacShareRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "共同借款人闗係檔" + e.getErrorMsg());
					}
				  } else {
					  tFacShareRelation.setRelCode(relCode);
					try {
						sFacShareRelationService.update(tFacShareRelation, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "共同借款人闗係檔" + e.getErrorMsg());
					}
				  }		
			} else {
				throw new LogicException("E0001", "共同借款人資料檔");
			}
		  
		  
		} else {
			
		  ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
		  clOwnerRelationId.setCreditSysNo(iCreditSysNo);
		  clOwnerRelationId.setCustNo(iCustNo);
		  clOwnerRelationId.setOwnerCustUKey(relUKey);
		
		  ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId, titaVo);

		  if (clOwnerRelation == null) {
			clOwnerRelation = new ClOwnerRelation();
			clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
			clOwnerRelation.setOwnerRelCode(relCode);
			try {
				sClOwnerRelationService.insert(clOwnerRelation, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
			}
		  } else {
			clOwnerRelation.setOwnerRelCode(relCode);
			try {
				sClOwnerRelationService.update(clOwnerRelation, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
			}
		  }		
		
	    } // else 

		this.addList(this.totaVo);
		return this.sendList();
	}
}