package com.st1.itx.trade.L8;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.domain.TbJcicMu01Id;
/*DB服務*/
import com.st1.itx.db.service.TbJcicMu01Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8350")
@Scope("prototype")
/**
 * 查詢人員名冊報送
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8350 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8350.class);
	/* DB服務注入 */
	@Autowired
	public TbJcicMu01Service sTbJcicMu01Service;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8350 ");
		this.totaVo.init(titaVo);

		String iHeadOfficeCode = titaVo.getParam("HeadOfficeCode");
		String iBranchCode = titaVo.getParam("BranchCode");
		int iDataDate = Integer.valueOf(titaVo.getParam("DataDate"));
		String iEmpName = titaVo.getParam("EmpName");
		String iEmpId = titaVo.getParam("EmpId");
		String iEmailAccount = titaVo.getParam("EmailAccount");
		String iTitle = titaVo.getParam("Title");
		String iQryUserId = titaVo.getParam("QryUserId");
		String iAuthQryType = titaVo.getParam("AuthQryType");
		String iAuthItemQuery = titaVo.getParam("AuthItemQuery");
		int iAuthStartDay = Integer.valueOf(titaVo.getParam("AuthStartDay"));
		int iAuthEndDay = Integer.valueOf(titaVo.getParam("AuthEndDay"));
		String iAuthMgrNameS = titaVo.getParam("AuthMgrNameS");
		String iAuthMgrNameE = titaVo.getParam("AuthMgrNameE");
		String iAuthMgrIdS = titaVo.getParam("AuthMgrIdS");
		String iAuthMgrIdE = titaVo.getParam("AuthMgrIdE");
		int iOutJcicTxtDate = Integer.valueOf(titaVo.getParam("OutJcicTxtDate"));

		TbJcicMu01 iTbJcicMu01 = new TbJcicMu01();
		TbJcicMu01Id iTbJcicMu01Id = new TbJcicMu01Id();

		iTbJcicMu01Id.setHeadOfficeCode(iHeadOfficeCode);
		iTbJcicMu01Id.setBranchCode(iBranchCode);
		iTbJcicMu01Id.setDataDate(iDataDate);
		iTbJcicMu01Id.setEmpId(iEmpId);
		iTbJcicMu01.setTbJcicMu01Id(iTbJcicMu01Id);
		iTbJcicMu01.setEmpName(iEmpName);
		iTbJcicMu01.setEmailAccount(iEmailAccount);
		iTbJcicMu01.setTitle(iTitle);
		iTbJcicMu01.setQryUserId(iQryUserId);
		iTbJcicMu01.setAuthQryType(iAuthQryType);
		iTbJcicMu01.setAuthItemQuery(iAuthItemQuery);
		iTbJcicMu01.setAuthStartDay(iAuthStartDay);
		iTbJcicMu01.setAuthEndDay(iAuthEndDay);
		iTbJcicMu01.setAuthMgrNameS(iAuthMgrNameS);
		iTbJcicMu01.setAuthMgrNameE(iAuthMgrNameE);
		iTbJcicMu01.setAuthMgrIdS(iAuthMgrIdS);
		iTbJcicMu01.setAuthMgrIdE(iAuthMgrIdE);
		iTbJcicMu01.setOutJcictxtDate(iOutJcicTxtDate);
		TbJcicMu01 aTbJcicMu01 = sTbJcicMu01Service.findById(iTbJcicMu01Id, titaVo);
		this.info("前置=" + aTbJcicMu01);
		if (aTbJcicMu01 == null) {
			try {
				sTbJcicMu01Service.insert(iTbJcicMu01,titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}