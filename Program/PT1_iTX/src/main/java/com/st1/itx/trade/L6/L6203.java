package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6203")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6203 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6203.class);

	/* DB服務注入 */
	@Autowired
	public CdLandOfficeService cdLandOfficeService;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public SendRsp sendRsp;

	public String iCityCode;
	public String iLandOffice;
	public String iRecWord;
	public String iRecWordItem;
	public String wkRecWord;
	private CdLandOffice tCdLandOffice;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6203 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCd = this.parse.stringToInteger(titaVo.getParam("FunCd"));
		iCityCode = titaVo.getParam("CityCode");
		iLandOffice = titaVo.getParam("LandOfficeCode");
		iRecWord = titaVo.getParam("RecWord");
		iRecWordItem = titaVo.getParam("RecWordItem");
		wkRecWord = titaVo.getParam("RecWord");

		// 檢查輸入資料
		if (!(iFunCd >= 1 && iFunCd <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6607"); // 功能選擇錯誤
		}

		CdLandOfficeId cdLandOfficeId = new CdLandOfficeId();
		cdLandOfficeId.setCityCode(iCityCode);
		cdLandOfficeId.setLandOfficeCode(iLandOffice);
		cdLandOfficeId.setRecWord(iRecWord);
		tCdLandOffice = cdLandOfficeService.holdById(cdLandOfficeId);
		switch (iFunCd) {
		case 1: // 新增
			tCdLandOffice = new CdLandOffice();
			moveCdLandOffice(iFunCd, titaVo);
			this.info("tCdLandOffice =  " + tCdLandOffice);
			try {
				cdLandOfficeService.insert(tCdLandOffice, titaVo);

			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			if (tCdLandOffice == null) {
				throw new LogicException(titaVo, "E0003", "地政所 = " + iLandOffice + " 收件字代號 = " + iRecWord); // 修改資料不存在
			}
			CdLandOffice tCdLandOffice2 = (CdLandOffice) dataLog.clone(tCdLandOffice);
			try {
				moveCdLandOffice(iFunCd, titaVo);
				this.info("tCdLandOffice =  " + tCdLandOffice);
				tCdLandOffice = cdLandOfficeService.update2(tCdLandOffice, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdLandOffice2, tCdLandOffice);
			dataLog.exec("修改收件字代碼說明");
			break;

		case 4: // 刪除
			if (tCdLandOffice != null) {
				// 刪除須刷主管卡
				if (titaVo.getEmpNos().trim().isEmpty()) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
				}
				try {
					dataLog.setEnv(titaVo, tCdLandOffice, tCdLandOffice);
					dataLog.exec("刪除收件字代碼");
					cdLandOfficeService.delete(tCdLandOffice, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", "地政所 = " + iLandOffice + " 收件字代號 = " + iRecWord); // 刪除資料不存在
			}
			break;
		}

		this.totaVo.putParam("ORecWord", wkRecWord); // 收件字代號 流水號

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdLandOffice(int mFuncCode, TitaVo titaVo) throws LogicException {
		CdLandOfficeId cdLandOfficeId = new CdLandOfficeId();
		// 新增收件字代號 流水編號
		// 維護時從收件字最後一號開始續編
		if (mFuncCode == 1) {
			CdLandOffice tLastCdLandOffice = cdLandOfficeService.findRecWordFirst(iCityCode, iLandOffice, titaVo);
			this.info("tLastCdLandOffice = " + tLastCdLandOffice);
			cdLandOfficeId.setCityCode(iCityCode);
			cdLandOfficeId.setLandOfficeCode(iLandOffice);
			if (tLastCdLandOffice == null) {
				wkRecWord = "001";
				cdLandOfficeId.setRecWord(wkRecWord);
			} else {
				int newRecWord = parse.stringToInteger(tLastCdLandOffice.getRecWord()) + 1;
				wkRecWord = FormatUtil.pad9("" + newRecWord, 3);
				cdLandOfficeId.setRecWord(wkRecWord);
				this.info("wkRecWord = " + wkRecWord);
			}

			this.info("cdLandOfficeId = " + cdLandOfficeId);
			tCdLandOffice.setCdLandOfficeId(cdLandOfficeId);
			tCdLandOffice.setLandOfficeCode(iLandOffice);
			tCdLandOffice.setRecWord(wkRecWord);
		}
		tCdLandOffice.setRecWordItem(iRecWordItem);

	}
}