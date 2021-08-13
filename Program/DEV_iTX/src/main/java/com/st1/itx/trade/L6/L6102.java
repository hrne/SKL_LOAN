package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;

import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;

import com.st1.itx.trade.L9.L9130;
import com.st1.itx.trade.L9.L9131;
import com.st1.itx.trade.L9.L9132;
import com.st1.itx.trade.L9.L9133;
import com.st1.itx.tradeService.TradeBuffer;
//import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita SecNo=9,2 ClsFg=9,1 BatNo=9,2 ClsNo=9,2 CoreSeqNo=9,3 END=X,1
 */

@Service("L6102")
@Scope("prototype")
/**
 * 核心傳票相關單獨作業
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6102 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6102.class);

	/* DB服務注入 */
	@Autowired
	public AcCloseService sAcCloseService;
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public L9130 tranL9130;
	@Autowired
	public L9131 tranL9131;
	@Autowired
	public L9132 tranL9132;
	@Autowired
	public L9133 tranL9133;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6102 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")); // 會計日期
		int iBatchNo = this.parse.stringToInteger(titaVo.getParam("BatchNo")); // 傳票批號
		if (iBatchNo < 90) {
			throw new LogicException(titaVo, "E0010", "須執行業務關帳作業"); // 功能選擇錯誤
		}
		// 查AcDetail by 傳票批號

		Slice<AcDetail> slAcDetail = sAcDetailService.findL9RptData(iAcDate + 19110000, iBatchNo, 0, Integer.MAX_VALUE,
				titaVo);
		List<AcDetail> lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

		if (lAcDetail == null || lAcDetail.size() == 0) {
			throw new LogicException(titaVo, "E2003", "無該傳票批號帳務"); // 查無資料
		}

		// 更新上傳核心序號(09:放款)
		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();

		tAcCloseId.setAcDate(iAcDate);
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 09-放款

		tAcClose = sAcCloseService.holdById(tAcCloseId);

		if (tAcClose == null) {
			tAcClose = new AcClose();
			tAcClose.setAcCloseId(tAcCloseId);
			tAcClose.setClsFg(0);
			tAcClose.setBatNo(1);
			tAcClose.setCoreSeqNo(1);
			try {
				sAcCloseService.insert(tAcClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "Acclose insert " + e.getErrorMsg());
			}
		} else {
			tAcClose.setCoreSeqNo(tAcClose.getCoreSeqNo() + 1);
			try {
				sAcCloseService.update(tAcClose);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "更新上傳核心序號(09:放款)"); // 更新資料時，發生錯誤
			}
		}

		// 啟動 L9130核心傳票媒體檔產生作業 ; L9131核心日結單代傳票列印 ; L9132傳票媒體明細表(核心) ; L9133會計與主檔餘額檢核表
		// 下行電文
		this.totaVo.putParam("OOCoreSeqNo", tAcClose.getCoreSeqNo());

		titaVo.putParam("MediaSeq", tAcClose.getCoreSeqNo()); // 核心傳票

		tranL9130.run(titaVo);
//		MySpring.newTask("L9130", this.txBuffer, titaVo);

		tranL9131.run(titaVo);
//		MySpring.newTask("L9131", this.txBuffer, titaVo);

		titaVo.putParam("MediaType", tAcClose.getCoreSeqNo()); // 核心傳票

		tranL9132.run(titaVo);
//		MySpring.newTask("L9132", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}
