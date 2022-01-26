package com.st1.itx.trade.L8;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TbJcicMu01Service;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;

@Component("L8351File")
@Scope("prototype")

public class L8351File extends MakeFile {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public TbJcicMu01Service iTbJcicMu01Service;

	public void exec(TitaVo titaVo) throws LogicException {

		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iTxtDate = titaVo.getParam("TxtDate");
		String iTxtCount = String.valueOf(Integer.valueOf(titaVo.getParam("TxtCount")));
		int date = Integer.valueOf(titaVo.getEntDy());
		String brno = titaVo.getBrno();
		String filecode = "L8351";
		String fileitem = "暫定每月產檔";
		// 檔名
		String filename = iSubmitKey + iTxtDate.substring(3) + iTxtCount + ".MU1";

		this.open(titaVo, date, brno, filecode, fileitem, filename,2);
		// 用String.format()
		Slice<TbJcicMu01> iTbJcicMu01 = iTbJcicMu01Service.findAll(0,Integer.MAX_VALUE, titaVo);

		// 第一行
		String iContactX =  FormatUtil.padX("放款部聯絡人-程慧娟", 80);
		String iFirstLine = String.format("JCIC-DAT-MU01-V%s-%s     %s%s          02-23895858#7076"+iContactX, iTxtCount, iSubmitKey, iTxtDate, iTxtCount);
		this.put(iFirstLine);

		if (iTbJcicMu01 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 資料新建錯誤
		} else {
			int iTotalCount = 0;
			// 組資料，條件getOutJcictxtDate=0，組完後回填今天日期
			for (TbJcicMu01 aTbJcicMu01 : iTbJcicMu01) {
				if (aTbJcicMu01.getOutJcictxtDate() == 0) {
					iTotalCount += 1;

					// 產檔內容
					
					int iDataDate = Integer.valueOf(aTbJcicMu01.getDataDate());
					int iAuthStartDay = Integer.valueOf(aTbJcicMu01.getAuthStartDay());
					int iAuthEndDay = Integer.valueOf(aTbJcicMu01.getAuthEndDay());
					
					String sDataDate = "";
					String sAuthStartDay = "";
					String sAuthEndDay = "";
					
					String iEmpId = aTbJcicMu01.getEmpId();
					String iAuthMgrIdS = aTbJcicMu01.getAuthMgrIdS();
					String iAuthMgrIdE = aTbJcicMu01.getAuthMgrIdE();
					String iEmpIdX = "";
					String iAuthMgrIdSX = "";
					String iAuthMgrIdEX = "";
					String iQuery = "";
					String iReview = "";
					String iOther = "";
					CdEmp dCdEmp = sCdEmpService.findById(iEmpId, titaVo);
					CdEmp sCdEmp = sCdEmpService.findById(iAuthMgrIdS, titaVo);
					CdEmp eCdEmp = sCdEmpService.findById(iAuthMgrIdE, titaVo);
					if (iDataDate > 0) {
						sDataDate = String.valueOf(iDataDate);
					}
					if (iAuthStartDay > 0) {
						sAuthStartDay = String.valueOf(iAuthStartDay);
					}
					if (iAuthEndDay > 0) {
						sAuthEndDay = String.valueOf(iAuthEndDay);
					}
					
					if (dCdEmp != null) {
						iEmpIdX = dCdEmp.getFullname();
					}
					if (sCdEmp != null) {
						iAuthMgrIdSX = dCdEmp.getFullname();
					}
					if (eCdEmp != null) {
						iAuthMgrIdEX = dCdEmp.getFullname();
					}
					
					if (!aTbJcicMu01.getAuthItemQuery().trim().isEmpty()) {
						iQuery = "A";
					}
					if (!aTbJcicMu01.getAuthItemReview().trim().isEmpty()) {
						iReview = "B";
					}
					if (!aTbJcicMu01.getAuthItemOther().trim().isEmpty()) {
						iOther = "C";
					}
					
					//補空白
					String iHeadOfficeCode = FormatUtil.padX(aTbJcicMu01.getHeadOfficeCode(),3);
					String iBranchCode = FormatUtil.padX(aTbJcicMu01.getBranchCode(),4);
					iEmpIdX = FormatUtil.padX(iEmpIdX,40);
					iEmpId = FormatUtil.padX(iEmpId,8);
					String iTitle = FormatUtil.padX(aTbJcicMu01.getTitle(),50);
					String iAuthQryType = FormatUtil.padX(aTbJcicMu01.getAuthQryType(),1);
					String iQryUserId = FormatUtil.padX(aTbJcicMu01.getQryUserId(),8);
					iQuery = FormatUtil.padX(iQuery,1);
					iReview = FormatUtil.padX(iReview,1);
					iOther = FormatUtil.padX(iOther,1);
					iAuthMgrIdSX = FormatUtil.padX(iAuthMgrIdSX,40);
					iAuthMgrIdS = FormatUtil.padX(iAuthMgrIdS,8);
					iAuthMgrIdEX = FormatUtil.padX(iAuthMgrIdEX,40);
					iAuthMgrIdE = FormatUtil.padX(iAuthMgrIdE,8);
					String iEmailAccount = FormatUtil.padX(aTbJcicMu01.getEmailAccount(),50);
					String iContent = " "
					        + iHeadOfficeCode + iBranchCode + StringUtils.rightPad(sDataDate, 7, " ")
							+ iEmpIdX+iEmpId+iTitle+iAuthQryType+iQryUserId
							+ "          "
							+ iQuery + iReview + iOther 
							+ StringUtils.rightPad(sAuthStartDay, 7, " ")
							+ iAuthMgrIdSX+iAuthMgrIdS
							+ StringUtils.rightPad(sAuthEndDay, 7, " ")
							+ iAuthMgrIdEX+iAuthMgrIdE
							+ iEmailAccount
//							+ StringUtils.rightPad(aTbJcicMu01.getModifyUserId(), 25, " ");
							+ "                         ";
					this.put(iContent);
					// 修改Jcic日期為今天日期
					TbJcicMu01 bTbJcicMu01 = iTbJcicMu01Service.holdById(aTbJcicMu01.getTbJcicMu01Id());
					bTbJcicMu01.setOutJcictxtDate(Integer.valueOf(titaVo.getCalDy()) + 19110000);
					try {
						iTbJcicMu01Service.update(bTbJcicMu01);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料修改錯誤
					}
				}
			}
		if (iTotalCount == 0) {
			throw new LogicException(titaVo, "E0001", "無可轉出資料");
		}
		// 最後一行
		String sCount = String.valueOf(iTotalCount);// update by Hedy (2021/11/3)
		String footText = "TRLR" + StringUtils.leftPad(sCount, 8, '0') + StringUtils.rightPad("", 129);
		this.put(footText);
		}
	}
}
