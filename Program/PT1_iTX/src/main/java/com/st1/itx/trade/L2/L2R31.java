package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R31")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R31 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustDataCtrlService sCustDataCtrlService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R31 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1設定 2解除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 客戶統編
		String iCustId = titaVo.getParam("RimCustId");

		// new table
		CustMain tCustMain = new CustMain();
		// 客戶統編找客戶主檔戶號
		tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

		CustDataCtrl tCustDataCtrl;
		int custNo = 0;

		if (tCustMain == null) {
			// 如果抓不到 CustMain, 搜尋控管檔, 如果還是沒有再throw exception

			Slice<CustDataCtrl> sCustDataCtrl = sCustDataCtrlService.findCustId(iCustId, 0, 1, titaVo);
			if (sCustDataCtrl == null)
				throw new LogicException(titaVo, "E2003", "該統編不存在客戶主檔或結清戶個資控管檔");

			List<CustDataCtrl> lCustDataCtrl = sCustDataCtrl.getContent();

			if (lCustDataCtrl.isEmpty())
				throw new LogicException(titaVo, "E2003", "該統編不存在客戶主檔或結清戶個資控管檔");

			tCustDataCtrl = lCustDataCtrl.get(0);
			tCustMain = sCustMainService.custNoFirst(tCustDataCtrl.getCustNo(), tCustDataCtrl.getCustNo(), titaVo);
			custNo = tCustMain.getCustNo();
		} else {
			// 於客戶主檔有
			custNo = tCustMain.getCustNo();
			tCustDataCtrl = sCustDataCtrlService.findById(custNo, titaVo);
		}

		if (iFunCd == 1) {
			if (tCustDataCtrl != null && tCustDataCtrl.getApplMark() != 3) {
				throw new LogicException(titaVo, "E0012", "此戶個資控管已設定");// 該筆資料已存在
			} else {
				tCustDataCtrl = new CustDataCtrl();// 此為新增
			}

			this.totaVo.putParam("L2r31CustNo", custNo);
			this.totaVo.putParam("L2r31CustName", tCustMain.getCustName());
			this.totaVo.putParam("L2r31Reason", tCustDataCtrl.getReason());
			this.totaVo.putParam("L2r31CuscCd", tCustMain.getCuscCd());
			this.totaVo.putParam("L2r31SetEmpNo", tCustDataCtrl.getSetEmpNo());
			this.totaVo.putParam("L2r31ReSetEmpNo", tCustDataCtrl.getReSetEmpNo());

			this.info("tlrno = " + titaVo.getTlrNo());

		} else {

			if (tCustDataCtrl == null) {
				throw new LogicException(titaVo, "E2003", "結清戶個資控管檔");
			}

			this.totaVo.putParam("L2r31CustNo", tCustDataCtrl.getCustNo());
			this.totaVo.putParam("L2r31CustName", tCustMain.getCustName());
			this.totaVo.putParam("L2r31Reason", tCustDataCtrl.getReason());
			this.totaVo.putParam("L2r31CuscCd", tCustMain.getCuscCd());
			this.totaVo.putParam("L2r31SetEmpNo", tCustDataCtrl.getSetEmpNo());
			this.totaVo.putParam("L2r31ReSetEmpNo", tCustDataCtrl.getReSetEmpNo());

		}

		this.totaVo.putParam("L2r31ApplMark", tCustDataCtrl.getApplMark());
		this.totaVo.putParam("L2r31CreateEmpNo", titaVo.getTlrNo());
		this.totaVo.putParam("L2r31CreateDate", dateUtil.getNowIntegerForBC() - 19110000);
		this.totaVo.putParam("L2r31CreateTime", dateUtil.getNowIntegerTime());

		this.totaVo.putParam("L2r31SetDate", parse.timeStampToString(tCustDataCtrl.getSetDate()));
		this.totaVo.putParam("L2r31ReSetDate", parse.timeStampToString(tCustDataCtrl.getReSetDate()));
		this.addList(this.totaVo);
		return this.sendList();
	}
}