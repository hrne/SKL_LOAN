package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.RelsCompanyService;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * END=X,1<br>
 */

@Service("L2901")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2901 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2901.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public RelsMainService sRelsMainService;

	/* DB服務注入 */
	@Autowired
	public RelsFamilyService sRelsFamilyService;

	/* DB服務注入 */
	@Autowired
	public RelsCompanyService sRelsCompanyService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2901 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 343 * 100 = 34300

		// tita
		// 身分證字號 CustId
		String iCustId = titaVo.getParam("CustId");
		// 姓名 CustName
		String iCustName = titaVo.getParam("CustName");
		// 戶號 CustNo
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 選項 Option 1:是否為關係人;2:是否為親屬;3:是否為相關事業
		int iOption = parse.stringToInteger(titaVo.getParam("Option"));

		// new RelsMain TABLE
		RelsMain tRelsMain = new RelsMain();
		CustMain tCustMain = new CustMain();

		// new ArrayList
		List<RelsMain> lRelsMain = new ArrayList<RelsMain>();
		List<RelsFamily> lRelsFamily = new ArrayList<RelsFamily>();
		List<RelsCompany> lRelsCompany = new ArrayList<RelsCompany>();
		Slice<RelsFamily> slRelsFamily = null;
		Slice<RelsCompany> slRelsCompany = null;

		// 選項Option 1:是否為關係人;2:是否為親屬;3:是否為相關事業

		if (iOption == 1) {

//			// 邏輯處理三擇一輸入
			if (!iCustId.isEmpty()) {
				tRelsMain = sRelsMainService.RelsIdFirst(iCustId, titaVo);
				if (tRelsMain == null) {
					throw new LogicException(titaVo, "E2003", "該統編不存在(準)利害關係人主檔"); // 查無資料
				}
				lRelsMain.add(tRelsMain);
			} else if (!iCustName.isEmpty()) {
				Slice<RelsMain> slRelsMain = sRelsMainService.RelsNameEq(iCustName, this.index, this.limit, titaVo);
				lRelsMain = slRelsMain == null ? null : slRelsMain.getContent();
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (slRelsMain != null && slRelsMain.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}
			} else {
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				if (tCustMain == null) {
					throw new LogicException(titaVo, "E2003", "該戶號不存在客戶主檔"); // 查無資料
				}

				String custid = tCustMain.getCustId();
				tRelsMain = sRelsMainService.RelsIdFirst(custid, titaVo);
				if (tRelsMain == null) {
					throw new LogicException(titaVo, "E2003", "該戶號不存在(準)利害關係人主檔"); // 查無資料
				}
				lRelsMain.add(tRelsMain);
			}

			if (lRelsMain == null) {
				throw new LogicException(titaVo, "E2003", "無(準)利害關係人檔資料"); // 查無資料
			}
			for (RelsMain tmpRelsMain : lRelsMain) {

				OccursList occurslist = new OccursList();
				RelsCompany tRelsCompany = new RelsCompany();
				RelsFamily tRelsFamily = new RelsFamily();

				occurslist.putParam("OORelsId", tmpRelsMain.getRelsId());
				occurslist.putParam("OORelsName", tmpRelsMain.getRelsName());
				occurslist.putParam("OORelsCode", tmpRelsMain.getRelsCode());
				occurslist.putParam("OORelsType", tmpRelsMain.getRelsType());
				occurslist.putParam("OOFamilyCode", tRelsFamily.getFamilyCode());
				occurslist.putParam("OOFamilyCallCode", tRelsFamily.getFamilyCallCode());
				occurslist.putParam("OOFamilyId", tRelsFamily.getFamilyId());
				occurslist.putParam("OOFamilyName", tRelsFamily.getFamilyName());
				occurslist.putParam("OOCompanyId", tRelsCompany.getCompanyId());
				occurslist.putParam("OOCompanyName", tRelsCompany.getCompanyName());
				occurslist.putParam("OOHoldingRatio", tRelsCompany.getHoldingRatio());
				occurslist.putParam("OOJobTitle", tRelsCompany.getJobTitle());

				this.totaVo.addOccursList(occurslist);
			}
		} else if (iOption == 2) {

//			// 邏輯處理三擇一輸入
			if (!iCustId.isEmpty()) {
				slRelsFamily = sRelsFamilyService.findFamilyIdEq(iCustId, this.index, this.limit, titaVo);
				lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
			} else if (!iCustName.isEmpty()) {
				slRelsFamily = sRelsFamilyService.findFamilyNameEq(iCustName, this.index, this.limit, titaVo);
				lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();
			} else {
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				if (tCustMain == null) {
					throw new LogicException(titaVo, "E2003", "該戶號不存在客戶主檔"); // 查無資料
				}
				String custid = tCustMain.getCustId();
				slRelsFamily = sRelsFamilyService.findFamilyIdEq(custid, this.index, this.limit, titaVo);
				lRelsFamily = slRelsFamily == null ? null : slRelsFamily.getContent();

			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slRelsFamily != null && slRelsFamily.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 自動折返 */
				this.totaVo.setMsgEndToAuto();
			}
			RelsCompany tRelsCompany = new RelsCompany();
			RelsMain tmpRelsMain = new RelsMain();
			if (lRelsFamily == null) {
				throw new LogicException(titaVo, "E2003", "無(準)利害關係人親屬檔資料"); // 查無資料
			}
			this.info("tRelsFamily size L2901" + lRelsFamily.size());
			for (RelsFamily tRelsFamily : lRelsFamily) {
				this.info("tRelsFamily L2901" + tRelsFamily);
				String relsUKey = tRelsFamily.getRelsUKey();

				tRelsMain = new RelsMain();
				tRelsMain = sRelsMainService.findById(relsUKey);

				OccursList occurslist = new OccursList();

				occurslist.putParam("OORelsId", tRelsMain.getRelsId());
				occurslist.putParam("OORelsName", tRelsMain.getRelsName());
				occurslist.putParam("OORelsCode", tmpRelsMain.getRelsCode());
				occurslist.putParam("OORelsType", tmpRelsMain.getRelsType());
				occurslist.putParam("OOFamilyCode", tRelsFamily.getFamilyCode());
				occurslist.putParam("OOFamilyCallCode", tRelsFamily.getFamilyCallCode());
				occurslist.putParam("OOFamilyId", tRelsFamily.getFamilyId());
				occurslist.putParam("OOFamilyName", tRelsFamily.getFamilyName());
				occurslist.putParam("OOCompanyId", tRelsCompany.getCompanyId());
				occurslist.putParam("OOCompanyName", tRelsCompany.getCompanyName());
				occurslist.putParam("OOHoldingRatio", tRelsCompany.getHoldingRatio());
				occurslist.putParam("OOJobTitle", tRelsCompany.getJobTitle());

				this.totaVo.addOccursList(occurslist);

			}
		} else if (iOption == 3) {

//			// 邏輯處理三擇一輸入
			if (!iCustId.isEmpty()) {
				slRelsCompany = sRelsCompanyService.findCompanyIdEq(iCustId, this.index, this.limit, titaVo);
				lRelsCompany = slRelsCompany == null ? null : slRelsCompany.getContent();

			} else if (!iCustName.isEmpty()) {
				slRelsCompany = sRelsCompanyService.findCompanyNameEq(iCustName, this.index, this.limit, titaVo);
				lRelsCompany = slRelsCompany == null ? null : slRelsCompany.getContent();
			} else {
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				if (tCustMain == null) {
					throw new LogicException(titaVo, "E2003", "該戶號不存在客戶主檔"); // 查無資料
				}
				String custid = tCustMain.getCustId();
				slRelsCompany = sRelsCompanyService.findCompanyIdEq(custid, this.index, this.limit, titaVo);
				lRelsCompany = slRelsCompany == null ? null : slRelsCompany.getContent();

			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slRelsCompany != null && slRelsCompany.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 自動折返 */
				this.totaVo.setMsgEndToAuto();
			}
			RelsFamily tRelsFamily = new RelsFamily();
			RelsMain tmpRelsMain = new RelsMain();

			if (lRelsCompany == null) {
				throw new LogicException(titaVo, "E2003", "無(準)利害關係人相關事業檔資料"); // 查無資料
			}
			for (RelsCompany tRelsCompany : lRelsCompany) {
				this.info("lRelsCompany L2901" + lRelsCompany);

				OccursList occurslist = new OccursList();

				String relsUKey = tRelsCompany.getRelsUKey();

				tRelsMain = new RelsMain();
				tRelsMain = sRelsMainService.findById(relsUKey, titaVo);

				occurslist.putParam("OORelsId", tRelsMain.getRelsId());
				occurslist.putParam("OORelsName", tRelsMain.getRelsName());
				occurslist.putParam("OORelsCode", tmpRelsMain.getRelsCode());
				occurslist.putParam("OORelsType", tmpRelsMain.getRelsType());
				occurslist.putParam("OOFamilyCode", tRelsFamily.getFamilyCode());
				occurslist.putParam("OOFamilyCallCode", tRelsFamily.getFamilyCallCode());
				occurslist.putParam("OOFamilyId", tRelsFamily.getFamilyId());
				occurslist.putParam("OOFamilyName", tRelsFamily.getFamilyName());
				occurslist.putParam("OOCompanyId", tRelsCompany.getCompanyId());
				occurslist.putParam("OOCompanyName", tRelsCompany.getCompanyName());
				occurslist.putParam("OOHoldingRatio", tRelsCompany.getHoldingRatio());
				occurslist.putParam("OOJobTitle", tRelsCompany.getJobTitle());

				this.totaVo.addOccursList(occurslist);

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}