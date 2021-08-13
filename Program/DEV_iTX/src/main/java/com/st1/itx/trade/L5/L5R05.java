package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.CustMain;

import com.st1.itx.db.domain.NegFinShare;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegFinShareService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

@Service("L5R05")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R05 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R05.class);
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public NegFinShareService sNegFinShareService;

	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public NegCom sNegCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5R05");
		this.info("active L5R05 ");
		this.totaVo.init(titaVo);

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		String CustId = titaVo.getParam("RimCustId").trim();// 身分證字號

//		L5r05CustId=X,10
//				L5r05CustNo=X,7
//				L5r05CaseSeq=X,3
//				L5r05IsMainFin=X,1
		if (CustId != null && CustId.length() != 0) {
			int CustNo = 0;
			CustMain CustMainVO = sCustMainService.custIdFirst(CustId);
			if (CustMainVO != null) {
				CustNo = CustMainVO.getCustNo();
				int CaseSeq = 0;
				NegMain NegMainVO = sNegMainService.CustNoFirst(CustNo);
				if (NegMainVO != null) {
					CaseSeq = NegMainVO.getCaseSeq();
					totaVo.putParam("L5r05CustId", CustId);
					totaVo.putParam("L5r05CustNo", CustNo);
					totaVo.putParam("L5r05CaseSeq", CaseSeq);
					totaVo.putParam("L5r05IsMainFin", NegMainVO.getIsMainFin());

					Slice<NegFinShare> slNegFinShare = sNegFinShareService.FindAllFinCode(CustNo, CaseSeq, this.index, this.limit);
					List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();

//					List<NegFinShare> lNegFinShare=sNegFinShareService.findAll();
					this.info("L5r05 NegFinShare CustNo=[" + CustNo + "],CaseSeq=[" + CaseSeq + "]");
					this.info("L5r05 lNegFinShare=[" + lNegFinShare + "]");
					if (lNegFinShare != null && lNegFinShare.size() != 0) {
						int lNegFinShareS = 0;
						lNegFinShareS = lNegFinShare.size();
						this.info("L5R05 lNegFinShareS=" + lNegFinShareS);
						for (int i = 0; i < lNegFinShareS; i++) {
							NegFinShare NegFinShareVO = lNegFinShare.get(i);
							int Row = i + 1;
							totaVo.putParam("L5r05NegFinShareFinCode" + Row + "", NegFinShareVO.getFinCode());// 債權機構
							totaVo.putParam("L5r05NegFinShareFinName" + Row + "", sNegCom.FindCdBank(NegFinShareVO.getFinCode(), titaVo)[0]);// 債權機構名稱
							totaVo.putParam("L5r05NegFinShareContractAmt" + Row + "", NegFinShareVO.getContractAmt());// 簽約金額
							totaVo.putParam("L5r05NegFinShareAmtRatio" + Row + "", NegFinShareVO.getAmtRatio());// 債權比例%
							totaVo.putParam("L5r05NegFinShareDueAmt" + Row + "", NegFinShareVO.getDueAmt());// 期款
							totaVo.putParam("L5r05NegFinShareCancelDate" + Row + "", NegFinShareVO.getCancelDate());// 註銷日期
							totaVo.putParam("L5r05NegFinShareCancelAmt" + Row + "", NegFinShareVO.getCancelAmt());// 註銷本金
						}
						for (int i = lNegFinShareS; i < 30; i++) {
							int Row = i + 1;
							totaVo.putParam("L5r05NegFinShareFinCode" + Row + "", "");// 債權機構
							totaVo.putParam("L5r05NegFinShareFinName" + Row + "", "");// 債權機構名稱
							totaVo.putParam("L5r05NegFinShareContractAmt" + Row + "", "");// 簽約金額
							totaVo.putParam("L5r05NegFinShareAmtRatio" + Row + "", "");// 債權比例%
							totaVo.putParam("L5r05NegFinShareDueAmt" + Row + "", "");// 期款
							totaVo.putParam("L5r05NegFinShareCancelDate" + Row + "", "");// 註銷日期
							totaVo.putParam("L5r05NegFinShareCancelAmt" + Row + "", "");// 註銷本金
						}
					} else {
						// E0001 查詢資料不存在
						throw new LogicException(titaVo, "E0001", "查無資料 NegFinShare");
					}
				} else {
					// E0001 查詢資料不存在
					throw new LogicException(titaVo, "E0001", "查無資料 NegMain");
				}
			} else {
				// E0001 查詢資料不存在
				throw new LogicException(titaVo, "E0001", "查無資料 CustMain");
			}
		} else {
			// E0001 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "CustId 為空值");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}