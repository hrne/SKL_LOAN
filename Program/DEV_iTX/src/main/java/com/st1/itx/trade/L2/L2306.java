package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.domain.ReltMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2306")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2306 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2306.class);

	/* DB服務注入 */
	@Autowired
	public ReltMainService sReltMainService;

	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2306 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// tita功能1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));

		// tita借戶戶號CustId
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		// tita案件編號CaseNo
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));

		// tita統一編號CustId
		String iRelId = titaVo.getParam("RelId");

		ReltMain tReltMain = new ReltMain();
//		ReltMain tReltMain1 = new ReltMain();

//		tReltMain = sReltMainService.ReltIdFirst(iRelId, titaVo);
//		tReltMain = sReltMainService.CaseNoFirst(iCaseNo, titaVo);
		
		//eloan resend
		if (isEloan && iFunCd == 1) {
			ReltMainId ReltMainIdVo = new ReltMainId();
			ReltMainIdVo.setCaseNo(iCaseNo);
			ReltMainIdVo.setCustNo(iCustNo);
			ReltMainIdVo.setReltId(iRelId);
			tReltMain.setReltMainId(ReltMainIdVo);
			tReltMain = sReltMainService.findById(ReltMainIdVo);
			if (tReltMain !=null) {
				iFunCd = 2;
			}
		}
		
		// 新增
		if (iFunCd == 1) {

//			if (tReltMain != null) {
//				throw new LogicException(titaVo, "E0002", "L2306 該案件編號" + iCaseNo + "已存在於關係人主檔。");
//			}
//			
//			if (tReltMain != null) {
//				throw new LogicException(titaVo, "E0002", "L2306 該統編" + iCustId + "已存在於關係人主檔。");
//			}

			tReltMain = new ReltMain();

			ReltMainId ReltMainIdVo = new ReltMainId();
			ReltMainIdVo.setCaseNo(iCaseNo);
			ReltMainIdVo.setCustNo(iCustNo);
			ReltMainIdVo.setReltId(iRelId);
			tReltMain.setReltMainId(ReltMainIdVo);

			// tita值塞進table
			tReltMain.setReltId(titaVo.getParam("RelId"));
			tReltMain.setReltName(titaVo.getParam("RelName"));
			tReltMain.setReltCode(titaVo.getParam("PosInd"));
			tReltMain.setRemarkType(titaVo.getParam("RemarkType"));
			tReltMain.setReltmark(titaVo.getParam("Remark"));

			/* 存入DB */

			try {
				sReltMainService.insert(tReltMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// 修改
		} else if (iFunCd == 2) {
			// 找ReltUKey
//			tReltMain = sReltMainService.CaseNoFirst(iCaseNo, titaVo);

			ReltMainId ReltMainIdVo = new ReltMainId();
			ReltMainIdVo.setCaseNo(iCaseNo);
			ReltMainIdVo.setCustNo(iCustNo);
			ReltMainIdVo.setReltId(iRelId);
			tReltMain.setReltMainId(ReltMainIdVo);
			tReltMain = sReltMainService.holdById(ReltMainIdVo);
			
			if (tReltMain == null) {
				throw new LogicException(titaVo, "E0003", "關係人主檔");
			}

			// 變更前
			ReltMain beforeReltMain = (ReltMain) dataLog.clone(tReltMain);

			tReltMain.setReltId(titaVo.getParam("RelId"));
			tReltMain.setReltName(titaVo.getParam("RelName"));
			tReltMain.setReltCode(titaVo.getParam("PosInd"));
			tReltMain.setRemarkType(titaVo.getParam("RemarkType"));
			tReltMain.setReltmark(titaVo.getParam("Remark"));

			try {
				// 修改
				tReltMain = sReltMainService.update2(tReltMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeReltMain, tReltMain);
			dataLog.exec();

			// 刪除
		} else if (iFunCd == 4) {

			// 找ReltUKey
//			tReltMain = sReltMainService.CaseNoFirst(iCaseNo, titaVo);

			ReltMainId ReltMainIdVo = new ReltMainId();
			ReltMainIdVo.setCaseNo(iCaseNo);
			ReltMainIdVo.setCustNo(iCustNo);
			ReltMainIdVo.setReltId(iRelId);
			tReltMain.setReltMainId(ReltMainIdVo);
			tReltMain = sReltMainService.holdById(ReltMainIdVo);

			if (tReltMain == null) {
				throw new LogicException(titaVo, "E0004", "關係人主檔");
			}

			try {
				sReltMainService.delete(tReltMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			// FunCd==5,查詢 無處理
		} else if (iFunCd == 5) {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}