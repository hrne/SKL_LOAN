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
		String iTxtCount = titaVo.getParam("TxtCount");
		int date = Integer.valueOf(titaVo.getEntDy());
		String brno = titaVo.getBrno();
		String filecode = "L8351";
		String fileitem = "暫定每月產檔";
		// 檔名
		String filename = iSubmitKey + iTxtDate.substring(3) + ".MU1";

		this.open(titaVo, date, brno, filecode, fileitem, filename,2);
		// 用String.format()
		Slice<TbJcicMu01> iTbJcicMu01 = iTbJcicMu01Service.findAll(this.index, this.limit, titaVo);

		// 第一行
		String iFirstLine = String.format("JCIC-DAT-MU01-V%s-%s     %s%s          02-23895858#7064放款部聯絡人-陳仕賢", iTxtCount, iSubmitKey, iTxtDate, iTxtCount);
		this.put(iFirstLine);

		if (iTbJcicMu01 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 資料新建錯誤
		} else {
			// 組資料，條件getOutJcictxtDate=0，組完後回填今天日期
			for (TbJcicMu01 aTbJcicMu01 : iTbJcicMu01) {
				if (aTbJcicMu01.getOutJcictxtDate() == 0) {
					String iEmpNo = aTbJcicMu01.getEmpId();
					CdEmp iCdEmp = new CdEmp();
					iCdEmp = sCdEmpService.findById(iEmpNo, titaVo);
					String iEmpName = "";
					if (iCdEmp != null) {
						iEmpName = iCdEmp.getFullname();
					}

					// 產檔內容
					int iDataDate = Integer.valueOf(aTbJcicMu01.getDataDate()) - 19110000;
					int iAuthStartDay = Integer.valueOf(aTbJcicMu01.getAuthStartDay()) - 19110000;
					String iContent = " " + StringUtils.rightPad(aTbJcicMu01.getHeadOfficeCode(), 3, " ") + StringUtils.rightPad(aTbJcicMu01.getBranchCode(), 4, " ")
							+ StringUtils.leftPad(String.valueOf(iDataDate), 7, '0') + StringUtils.rightPad(iEmpName, 38, " ") + StringUtils.rightPad(aTbJcicMu01.getEmpId(), 8, " ")
							+ StringUtils.rightPad(aTbJcicMu01.getTitle(), 25, "　") + StringUtils.rightPad(aTbJcicMu01.getAuthQryType(), 1, " ")
							+ StringUtils.rightPad(aTbJcicMu01.getQryUserId(), 18, " ") + "A  " + StringUtils.rightPad(String.valueOf(iAuthStartDay), 90, " ")
							+ StringUtils.rightPad(aTbJcicMu01.getEmailAccount(), 4, " ");
					this.put(iContent);
					// 修改Jcic日期為今天日期
					TbJcicMu01 bTbJcicMu01 = iTbJcicMu01Service.holdById(aTbJcicMu01.getTbJcicMu01Id());
					bTbJcicMu01.setOutJcictxtDate(Integer.valueOf(titaVo.getOrgEntdyI()) + 19110000);
					try {
						iTbJcicMu01Service.update(bTbJcicMu01);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料修改錯誤
					}
				}
			}
		}
		// 最後一行
		String iLastLine = "TRLR00000026";
		this.put(iLastLine);

	}
}
