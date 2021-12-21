package com.st1.itx.trade.L6;

import java.io.File;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.CdIndustryFileVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L660A")
@Scope("prototype")
/**
 *
 *
 * @author Chih Cheng
 * @version 1.0.0
 */
public class L660A extends TradeBuffer {

	@Autowired
	public CdIndustryService CdIndustryService;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public MakeExcel makeExcel;
	@Autowired
	public CdIndustryFileVo CdIndustryFileVo;
	@Autowired
	public DataLog dataLog;
	@Value("${iTXInFolder}")
	private String inFolder = "";

	private int updcount = 0;
	private int inscount = 0;

	String mType;// 中類
	String tType; // 細類
	String iTem = "";// 行業名稱

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L660A ");
		this.totaVo.init(titaVo);

//      吃檔                                                   
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();
		this.info("filename==" + filename);

		makeExcel.openExcel(filename, 1);

		for (int row = 6; row >= 6; row++) {

			if (!makeExcel.getValue(row, 1).toString().isEmpty()) {
				if (("※").equals(makeExcel.getValue(row, 1).toString().substring(0, 1))) {
					this.info("結束記號:" + makeExcel.getValue(row, 1).toString().substring(0, 1));
					break;
				}
			}

			int d = 0;
			if (makeExcel.getValue(row, 4).toString().isEmpty()) {
				// 細類無值則跳過
				continue;
			} else {
				d = (int) ((double) makeExcel.getValue(row, 4));
				tType = String.valueOf(d);

				if (tType.length() < 4) {
					for (int i = tType.length(); i < 4; i++) {
						tType = "0" + tType; // 細類
					}
				}

			}

			mType = tType.substring(0, 2);// 中類取細類前兩碼
			iTem = (String) makeExcel.getValue(row, 5);// 行業名稱

			doIndustry(titaVo);

		}

		this.totaVo.putParam("InsCount", inscount);
		this.totaVo.putParam("UpdCount", updcount);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void doIndustry(TitaVo titaVo) throws LogicException {

		String iMainType = ""; // 主計處大類

		if (("01").equals(mType) || ("02").equals(mType) || ("03").equals(mType)) {// 農、林、漁、牧業
			iMainType = "A";
		} else if (("05").equals(mType) || ("06").equals(mType)) { // 礦業及土石採取業
			iMainType = "B";
		} else if (("35").equals(mType)) {// 電力及燃氣供應業
			iMainType = "D";
		} else if (("36").equals(mType) || ("37").equals(mType) || ("38").equals(mType) || ("39").equals(mType)) { // 用水供應及污染整治業
			iMainType = "E";
		} else if (("41").equals(mType) || ("42").equals(mType) || ("43").equals(mType)) { // 營建工程業
			iMainType = "F";
		} else if (("45").equals(mType) || ("46").equals(mType) || ("47").equals(mType) || ("48").equals(mType)) { // 批發及零售業
			iMainType = "G";
		} else if (("49").equals(mType) || ("50").equals(mType) || ("51").equals(mType) || ("52").equals(mType) || ("53").equals(mType) || ("54").equals(mType)) { // 運輸及倉儲業
			iMainType = "H";
		} else if (("55").equals(mType) || ("56").equals(mType)) { // 住宿及餐飲業
			iMainType = "I";
		} else if (("58").equals(mType) || ("59").equals(mType) || ("60").equals(mType) || ("61").equals(mType) || ("62").equals(mType) || ("63").equals(mType)) { // 出版影音及資通訊業
			iMainType = "J";
		} else if (("64").equals(mType) || ("65").equals(mType) || ("66").equals(mType)) { // 金融及保險業
			iMainType = "K";
		} else if (("67").equals(mType) || ("68").equals(mType)) { // 不動產業
			iMainType = "L";
		} else if (("69").equals(mType) || ("70").equals(mType) || ("71").equals(mType) || ("72").equals(mType) || ("73").equals(mType) || ("74").equals(mType) || ("75").equals(mType)
				|| ("76").equals(mType)) { // 專業、科學及技術服務業
			iMainType = "M";
		} else if (("77").equals(mType) || ("78").equals(mType) || ("79").equals(mType) || ("80").equals(mType) || ("81").equals(mType) || ("82").equals(mType)) { // 支援服務業
			iMainType = "N";
		} else if (("83").equals(mType) || ("84").equals(mType)) { // 公共行政及國防；強制性社會安全
			iMainType = "O";
		} else if (("85").equals(mType)) {// 教育業
			iMainType = "P";
		} else if (("86").equals(mType) || ("87").equals(mType) || ("88").equals(mType)) {// 醫療保健及社會工作服務業
			iMainType = "Q";
		} else if (("90").equals(mType) || ("91").equals(mType) || ("92").equals(mType) || ("93").equals(mType)) {// 藝術、娛樂及休閒服務業
			iMainType = "R";
		} else if (("94").equals(mType) || ("95").equals(mType) || ("96").equals(mType)) {// 其他服務業
			iMainType = "S";
		} else {
			iMainType = "C"; // 製造業 中類 08~34
		}

		CdIndustry tCdIndustry = new CdIndustry();
//		
		// 行業代號 = 01民營+ 細類
		String iType1 = "01" + tType;
		// 行業代號 = 02公營+ 細類
		String iType2 = "02" + tType;
		this.info("民營細類=" + iType1 + ",公營細類=" + iType2 + ",行業名稱=" + iTem + ",大類=" + mType + "," + iMainType);

		// 民營資料
		tCdIndustry = CdIndustryService.holdById(iType1, titaVo);
		if (tCdIndustry != null) {

			tCdIndustry.setIndustryCode(iType1);
			tCdIndustry.setIndustryItem(iTem);
			tCdIndustry.setEnable("Y");
			tCdIndustry.setMainType(iMainType);
			updcount = updcount + 1;

			try {
				CdIndustryService.update(tCdIndustry, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException("E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		} else {

			CdIndustry iCdIndustry = new CdIndustry();
			iCdIndustry.setIndustryCode(iType1);
			iCdIndustry.setIndustryItem(iTem);
			iCdIndustry.setEnable("Y");
			iCdIndustry.setMainType(iMainType);

			try {
				CdIndustryService.insert(iCdIndustry, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0002", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			inscount = inscount + 1;
		}

		// 公營資料
		tCdIndustry = CdIndustryService.holdById(iType2, titaVo);

		if (tCdIndustry != null) {

			tCdIndustry.setIndustryCode(iType2);
			tCdIndustry.setIndustryItem(iTem);
			tCdIndustry.setEnable("Y");
			tCdIndustry.setMainType(iMainType);
			updcount = updcount + 1;

			try {
				tCdIndustry = CdIndustryService.update(tCdIndustry, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException("E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}

		} else {

			CdIndustry iCdIndustry = new CdIndustry();
			iCdIndustry.setIndustryCode(iType2);
			iCdIndustry.setIndustryItem(iTem);
			iCdIndustry.setEnable("Y");
			iCdIndustry.setMainType(iMainType);

			try {
				CdIndustryService.insert(iCdIndustry, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0002", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			inscount = inscount + 1;
		}
	}

}