package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CustCross;
import com.st1.itx.db.domain.CustCrossId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustCrossService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L1109")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1109 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CustCrossService iCustCrossService;

	@Autowired
	public CdCodeService iCdCodeService;

	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1109 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		String iCustId = titaVo.getParam("CustId");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));

		CustMain iCustMain = new CustMain();
		String iCustUKey = "";
		if (!iCustId.equals("")) {
			iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
		} else if (iCustNo != 0) {
			iCustMain = iCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		} else {
			throw new LogicException(titaVo, "E0001", "統一編號與戶號須擇一輸入"); // 輸入錯誤
		}
		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶主檔查無資料"); // 輸入錯誤
		}

		titaVo.putParam("CustNo", iCustMain.getCustNo());

		iCustUKey = iCustMain.getCustUKey();

		String iSubCompanyFg = titaVo.getParam("SubCompanyFg");

//		List<HashMap<String, Object>> logMap = new ArrayList<HashMap<String, Object>>();

		int cnt = 0;

		if ("N".equals(iSubCompanyFg)) {
			Slice<CustCross> slCustCross = iCustCrossService.custUKeyEq(iCustUKey, 0, Integer.MAX_VALUE, titaVo);
			List<CustCross> lCustCross = slCustCross == null ? null : slCustCross.getContent();
			if (lCustCross != null && lCustCross.size() > 0) {
				for (CustCross fCustCross : lCustCross) {
					if ("Y".equals(fCustCross.getCrossUse())) {
						// update
						fCustCross.setCrossUse("N");
						try {
							fCustCross = iCustCrossService.update2(fCustCross, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "更新時發生錯誤");
						}

						CdCodeId cdCodeId = new CdCodeId();

						cdCodeId.setDefCode("SubCompanyCode");
						cdCodeId.setCode(fCustCross.getSubCompanyCode());

						CdCode cdCode = iCdCodeService.findById(cdCodeId, titaVo);
						String k = "";
						if (cdCode != null) {
							k = fCustCross.getSubCompanyCode() + " " + cdCode.getItem() + " 交互運用";
						} else {
							k = fCustCross.getSubCompanyCode() + " 交互運用";
						}

						iDataLog.setLog(k, "Y", "N");
						cnt++;

					}
				}
			}

		} else {
			int i = 1;
			while (true) {
				CustCross fCustCross = new CustCross();
				CustCrossId fCustCrossId = new CustCrossId(); // findbyId
				if (i == 21) {
					break;
				}
				String iSubCompanyCode = titaVo.get("SubCompanyCode" + i).trim();

				if (iSubCompanyCode.isEmpty()) {
					break;
				}

				String iCrossUse = titaVo.get("CrossUse" + i);
				if ("".equals(iCrossUse)) {
					iCrossUse = "N";
				}

				fCustCrossId.setCustUKey(iCustUKey);
				fCustCrossId.setSubCompanyCode(iSubCompanyCode);
				fCustCross = iCustCrossService.holdById(fCustCrossId, titaVo);

				CdCodeId cdCodeId = new CdCodeId();

				cdCodeId.setDefCode("SubCompanyCode");
				cdCodeId.setCode(iSubCompanyCode);

				CdCode cdCode = iCdCodeService.findById(cdCodeId, titaVo);

				String k = "";
				if (cdCode != null) {
					k = iSubCompanyCode + " " + cdCode.getItem() + " 交互運用";
				} else {
					k = iSubCompanyCode + " 交互運用";
				}

				if (fCustCross == null) {
					// insert
					fCustCross = new CustCross(); // init
					fCustCross.setCustCrossId(fCustCrossId);
					fCustCross.setCrossUse(iCrossUse);
					try {
						iCustCrossService.insert(fCustCross, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "新增時發生錯誤");
					}
					if ("Y".equals(iCrossUse)) {
						iDataLog.setLog(k, "N", iCrossUse);
						cnt++;
					}
				} else {
					// update
					if (!iCrossUse.equals(fCustCross.getCrossUse())) {
						iDataLog.setLog(k, fCustCross.getCrossUse(), iCrossUse);
						cnt++;
					}

					fCustCross.setCrossUse(iCrossUse);
					try {
						fCustCross = iCustCrossService.update2(fCustCross, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新時發生錯誤");
					}

				}

				i++;
			}
		}

		this.info("L1109 cnt = " + cnt);
		if (cnt > 0) {
			iDataLog.setEnv(titaVo, iCustMain, iCustMain);
			iDataLog.exec("修改顧客 " + iCustMain.getCustId() + " 交互運用資料", "CustUKey:" + iCustMain.getCustUKey());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
