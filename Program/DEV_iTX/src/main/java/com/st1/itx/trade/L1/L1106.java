package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustRelDetail;
import com.st1.itx.db.domain.CustRelDetailId;
import com.st1.itx.db.domain.CustRelMain;
import com.st1.itx.db.service.CustRelDetailService;
import com.st1.itx.db.service.CustRelMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1106")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L1106 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1106.class);

	/* DB服務注入 */
	@Autowired
	public CustRelMainService iCustRelMainService;

	/* DB服務注入 */
	@Autowired
	public CustRelDetailService iCustRelDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1106 ");	
		this.totaVo.init(titaVo);
		int iFunCd = Integer.valueOf(titaVo.getParam("FunCd"));
		String iCustRelId = titaVo.getParam("CustRelId");
		String iCustMainUKey = "";
		String iCustRelName = titaVo.getParam("CustRelName");
		String iIsForeigner = titaVo.getParam("IsForeigner");
		String iRelTypeCode = titaVo.getParam("RelTypeCode");
		String iRelId = titaVo.getParam("RelId");
		String iCustMainRelUKey = "";
		String iCustDetailRelUKey = "";
		String iRelName = titaVo.getParam("RelName");
		String iRelIsForeigner = titaVo.getParam("RelIsForeigner");
		String iRelationCode = titaVo.getParam("RelationCode");
		String iRemarkTypeCode = titaVo.getParam("RemarkTypeCode");
		String iRemark = titaVo.getParam("Remark");
		String iNote = titaVo.getParam("Note");
		String iStatus = titaVo.getParam("Status");
		
		switch(iFunCd) {
		case 1:
			 //檢查上送客戶與關聯戶是否已存在於CustRelMain
			 CustRelMain iCustRelMain = new CustRelMain();
			 CustRelMain sCustRelMain = new CustRelMain();
			 iCustRelMain = iCustRelMainService.custRelIdFirst(iCustRelId, titaVo);
			 sCustRelMain = iCustRelMainService.custRelIdFirst(iRelId, titaVo);
			 //客戶與關聯戶不存在則取新流水號，並新增資料
			 if (iCustRelMain == null) {
				 iCustMainUKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				 iCustRelMain = new CustRelMain();
				 iCustRelMain.setUkey(iCustMainUKey);
				 iCustRelMain.setCustRelId(iCustRelId);
				 iCustRelMain.setCustRelName(iCustRelName);
				 iCustRelMain.setIsForeigner(iIsForeigner);
				 try {
					 iCustRelMainService.insert(iCustRelMain, titaVo);
				 }catch (DBException e) {
						throw new LogicException("E0005", "新增客戶關係主檔時發生錯誤:"+iCustRelId);
				 }
			 }else {
				 //若客戶已存在於關係主檔，則取流水號
				 iCustMainUKey = iCustRelMain.getUkey();
			 }
			 if (sCustRelMain == null) {
				 iCustMainRelUKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				 sCustRelMain = new CustRelMain();
				 sCustRelMain.setUkey(iCustMainRelUKey);
				 sCustRelMain.setCustRelId(iRelId);
				 sCustRelMain.setCustRelName(iRelName);
				 sCustRelMain.setIsForeigner(iRelIsForeigner);
				 try {
					 iCustRelMainService.insert(sCustRelMain, titaVo);
				 }catch (DBException e) {
						throw new LogicException("E0005", "新增客戶關係主檔時發生錯誤:"+iRelId);
				 }
			 }
			 
			 //新增明細檔
			 CustRelDetail iCustRelDetail = new CustRelDetail();
			 CustRelDetailId iCustRelDetailId = new CustRelDetailId();
			 iCustRelDetail = iCustRelDetailService.relIdFirst(iRelId, titaVo) ;
			 //若該關聯戶已存在於明細檔，找尋其他資料共用UKey，否則取新流水號
			 if (iCustRelDetail == null) {
				 iCustDetailRelUKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			 }else {
				 iCustDetailRelUKey = iCustRelDetail.getUkey();
			 }
			 iCustRelDetail = new CustRelDetail();
			 iCustRelDetailId.setCustRelMainUKey(iCustMainUKey);
			 iCustRelDetailId.setUkey(iCustDetailRelUKey);
			 iCustRelDetail = iCustRelDetailService.findById(iCustRelDetailId, titaVo);
			 if (iCustRelDetail == null) {
				 iCustRelDetail = new CustRelDetail();
				 iCustRelDetail.setCustRelDetailId(iCustRelDetailId);
				 iCustRelDetail.setRelTypeCode(iRelTypeCode);
				 iCustRelDetail.setRelId(iRelId);
				 iCustRelDetail.setRelName(iRelName);
				 iCustRelDetail.setRelationCode(iRelationCode);
				 iCustRelDetail.setRemarkTypeCode(iRemarkTypeCode);
				 iCustRelDetail.setRemark(iRemark);
				 iCustRelDetail.setNote(iNote);
				 iCustRelDetail.setStatus(iStatus);
				 try {
					 iCustRelDetailService.insert(iCustRelDetail, titaVo);
				 }catch (DBException e) {
						throw new LogicException("E0005", "新增客戶關係明細時發生錯誤:"+iRelId);
				 }
			 }else {
				 throw new LogicException("E0005", "客戶:"+iCustRelId+"已有關係戶:"+iRelId);
			 }
			 break;
		case 2:
			String uCustRelMainUKey = titaVo.getParam("CustRelMainUKey");
			String uUKey = titaVo.getParam("Ukey");
			String uRelId = titaVo.getParam("RelId");
			String uCustRelName = titaVo.getParam("CustRelName");
			String uRelName = titaVo.getParam("RelName");
					
			//檢查姓名欄位是否有變動，若有則更新，不需異動IsForeigner
			CustRelMain uCustRelMain = new CustRelMain();
			uCustRelMain = iCustRelMainService.holdById(uCustRelMainUKey, titaVo);
			if (uCustRelMain == null) {
				throw new LogicException("E0005", "查詢關聯戶主檔時發生錯誤");
			}
			//客戶姓名
			if (!uCustRelName.equals(uCustRelMain.getCustRelName())) {
				CustRelMain beforeCustRelMain = (CustRelMain) dataLog.clone(uCustRelMain);
				uCustRelMain.setCustRelName(uCustRelName);
				try {
					iCustRelMainService.update(uCustRelMain, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0007", "更新關聯戶主檔姓名時發生錯誤");
				}
				dataLog.setEnv(titaVo, beforeCustRelMain, uCustRelMain);
				dataLog.exec();
			}
			//關聯戶姓名
			uCustRelMain = new CustRelMain();
			uCustRelMain = iCustRelMainService.custRelIdFirst(uRelId, titaVo);
			if (uCustRelMain == null) {
				throw new LogicException("E0005", "查詢關聯戶主檔時發生錯誤");
			}
			String uCustRelMainUkey = uCustRelMain.getUkey();
			uCustRelMain = iCustRelMainService.holdById(uCustRelMainUkey, titaVo);
			if (!uRelName.equals(uCustRelMain.getCustRelName())) {
				CustRelMain beforeCustRelMain_1 = (CustRelMain) dataLog.clone(uCustRelMain);
				uCustRelMain.setCustRelName(uRelName);
				try {
					iCustRelMainService.update(uCustRelMain, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0007", "更新關聯戶主檔姓名時發生錯誤");
				}
				dataLog.setEnv(titaVo, beforeCustRelMain_1, uCustRelMain);
				dataLog.exec();
			}
			
			//更新明細檔
			CustRelDetailId uCustRelDetailId = new CustRelDetailId();
			CustRelDetail uCustRelDetail = new CustRelDetail();
			uCustRelDetailId.setCustRelMainUKey(uCustRelMainUKey);
			uCustRelDetailId.setUkey(uUKey);
			uCustRelDetail = iCustRelDetailService.holdById(uCustRelDetailId, titaVo);
			if (uCustRelDetail == null) {
				throw new LogicException("E0005", "關聯戶明細檔查無資料");
			}
			CustRelDetail beforeCustDetail = (CustRelDetail) dataLog.clone(uCustRelDetail);
			uCustRelDetail.setRelTypeCode(iRelTypeCode);
			uCustRelDetail.setRelName(iRelName);
			uCustRelDetail.setRelationCode(iRelationCode);
			uCustRelDetail.setRemarkTypeCode(iRemarkTypeCode);
			uCustRelDetail.setRemark(iRemark);
			uCustRelDetail.setNote(iNote);
			uCustRelDetail.setStatus(iStatus);
			try {
				iCustRelDetailService.update(uCustRelDetail, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0007", "更新關聯戶明細檔時發生錯誤");
			}
			dataLog.setEnv(titaVo, beforeCustDetail, uCustRelDetail);
			dataLog.exec();
			break;
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}