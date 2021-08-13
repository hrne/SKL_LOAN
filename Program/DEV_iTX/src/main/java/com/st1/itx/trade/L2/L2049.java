package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * END=X,1<br>
 */

@Service("L2049")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2049 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2049.class);

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;
	/* DB服務注入 */
	@Autowired
	public ClMovablesService sClMovablesService;
	/* DB服務注入 */
	@Autowired
	public ClStockService sClStockService;
	/* DB服務注入 */
	@Autowired
	public ClOtherService sClOtherService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2049 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 97 * 500 = 48500

		// tita
		// 擔保品代號1
		int iClCode1StatAt = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode1EndAt = 9;
		// 若iClCode1有輸入
		if (iClCode1StatAt > 0) {
			iClCode1EndAt = iClCode1StatAt;
		}

		// 擔保品代號2
		int iClCode2StatAt = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClCode2EndAt = 99;
		// 若iClCode2有輸入
		if (iClCode2StatAt > 0) {
			iClCode2EndAt = iClCode2StatAt;
		}

		// 擔保品編號
		int clNoStartAt = parse.stringToInteger(titaVo.getParam("ClNo"));
		int clNoEndAt = 9999999;
		// 若ClNo有輸入
		if (clNoStartAt > 0) {
			clNoEndAt = clNoStartAt;
		}

		// 統編
		String iCustId = titaVo.getParam("CustId");

		// 借款人戶號
		int custNoStartAt = parse.stringToInteger(titaVo.getParam("CustNo"));
		int custNoEndAt = 9999999;

		// 若CustNo有輸入
		if (custNoStartAt > 0) {
			custNoEndAt = custNoStartAt;
		}

		// 擔保品大類
		if (iClCode1StatAt == 0) {

			int iClMainType = parse.stringToInteger(titaVo.getParam("ClMainType"));
			iClCode1StatAt = 0;
			iClCode1EndAt = 9;
			switch (iClMainType) {
			case 1:
				// iClMainType為1時,擔保品代號為1,2 (不動產)
				iClCode1StatAt = 1;
				iClCode1EndAt = 2;
				break;
			case 2:
				// iClMainType為2時,擔保品代號為9 (動產)
				iClCode1StatAt = 9;
				iClCode1EndAt = 9;
				break;
			case 3:
				// iClMainType為3時,擔保品代號為3,4 (股票)
				iClCode1StatAt = 3;
				iClCode1EndAt = 4;
				break;
			case 4:
				// iClMainType為4時,擔保品代號為5 (銀行保證)
				iClCode1StatAt = 5;
				iClCode1EndAt = 5;
				break;
			default:
				iClCode1StatAt = 0;
				iClCode1EndAt = 9;
				break;
			}
		}

		// 擔保品類別代號
		String clTypeCode = titaVo.getParam("ClTypeCode");

		this.info("擔保品類別代號  :" + clTypeCode);

		// 額度號碼
		int facmNoStartAt = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int facmNoEndAt = 999;
		// 若FacmNo有輸入
		if (facmNoStartAt > 0) {
			facmNoEndAt = facmNoStartAt;
		}

		// 核准號碼
		int approveNoStartAt = parse.stringToInteger(titaVo.getParam("ApplNo"));
		int approveNoEndAt = 9999999;
		// 若ApproveNo有輸入
		if (approveNoStartAt > 0) {
			approveNoEndAt = approveNoStartAt;
		}

		// 設定狀態
		final String iSettingStat = titaVo.getParam("SettingStat");

		// 擔保品狀態
		final String iClStat = titaVo.getParam("ClStat");

		// new ArrayList
		List<ClFacId> lClFacId = new ArrayList<ClFacId>();
		List<ClFac> lClFac = new ArrayList<ClFac>();
		List<ClMain> lClMain = new ArrayList<ClMain>();

		// new table
		CustMain tCustMain = new CustMain();

		// 統編有輸入
		if (!iCustId.isEmpty()) {
			tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
			// 統編有輸入,戶號有輸入 測試是否資料有誤
			if (custNoStartAt > 0) {
				// 該統編戶號與輸入戶號不符合,拋錯
				if (tCustMain.getCustNo() != custNoStartAt) {
					throw new LogicException(titaVo, "E2003", "統編戶號與輸入戶號不符合,查無資料"); // 查無資料
				}
			}
			custNoStartAt = tCustMain.getCustNo();
			custNoEndAt = custNoStartAt;
		}

		// tita Range查找
		Slice<ClFac> slClFac = sClFacService.selectForL2049(iClCode1StatAt, iClCode1EndAt, iClCode2StatAt,
				iClCode2EndAt, clNoStartAt, clNoEndAt, custNoStartAt, custNoEndAt, facmNoStartAt, facmNoEndAt,
				approveNoStartAt, approveNoEndAt, this.index, this.limit, titaVo);
		lClFac = slClFac == null ? null : slClFac.getContent();
		if (lClFac == null) {
			throw new LogicException("E2003", "額度與擔保品關聯檔"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClFac != null && slClFac.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (ClFac tmpClFac : lClFac) {
			ClFacId tmpClFacId = tmpClFac.getClFacId();
			if (!lClFacId.contains(tmpClFacId)) {
				lClFacId.add(tmpClFacId);
				this.info("tmpClFacId  = " + tmpClFacId);
			}
		}
		this.info("lClFacId size = " + lClFacId.size());

		// 擔保品類別有輸入
		if (!clTypeCode.isEmpty()) {
			for (ClFacId tmpClFacId : new ArrayList<ClFacId>(lClFacId)) {

				int tmpClCode1 = tmpClFacId.getClCode1();
				int tmpClCode2 = tmpClFacId.getClCode2();
				int tmpClNo = tmpClFacId.getClNo();

				ClMain tClMain = sClMainService.findById(new ClMainId(tmpClCode1, tmpClCode2, tmpClNo), titaVo);

				// 查詢出來的擔保品類別不符合輸入值時remove掉
				if (tClMain != null && !tClMain.getClTypeCode().equals(clTypeCode)) {
					this.info("l2049 tmpClFacId 不符合remove = " + tmpClFacId);
					lClFacId.remove(tmpClFacId);
				}

			}

		}

		if (lClFacId == null || lClFacId.size() == 0) {
			throw new LogicException("E2003", "額度與擔保品關聯檔"); // 查無資料
		}

		this.info("lClFacId : " + lClFacId);
		this.info("lClFacId size : " + lClFacId.size());

		for (ClFacId tmpClFacId : lClFacId) {
			ClFac tmpClFac = new ClFac();
			tmpClFac = sClFacService.findById(tmpClFacId);

			// new occurs
			OccursList occurslist = new OccursList();

			// new table
			tCustMain = new CustMain();
			FacMain tFacMain = new FacMain();
			ClMain tClMain = new ClMain();

			// 取統編,戶名
			tCustMain = sCustMainService.custNoFirst(tmpClFac.getCustNo(), tmpClFac.getCustNo(), titaVo);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}

			// 取核准額度
			tFacMain = sFacMainService.findById(new FacMainId(tmpClFac.getCustNo(), tmpClFac.getFacmNo()), titaVo);
			if (tFacMain == null) {
				tFacMain = new FacMain();
			}

			// 取擔保品類別代碼
			tClMain = sClMainService
					.findById(new ClMainId(tmpClFac.getClCode1(), tmpClFac.getClCode2(), tmpClFac.getClNo()), titaVo);
			if (tClMain == null) {
				tClMain = new ClMain();
			}
			int tmpClCode1 = tmpClFac.getClCode1();
			int tmpClCode2 = tmpClFac.getClCode2();
			int tmpClNo = tmpClFac.getClNo();

			String settingSeq = "";
			String settingStat = "";
			String clStat = "";
			String settingDate = "";
			String settingAmt = "";
			String claimDate = "";

			// 依據擔保品代號1查不同Table
			switch (tmpClCode1) {
			case 1:
			case 2:
				ClImmId tClImmId = new ClImmId();
				tClImmId.setClCode1(tmpClCode1);
				tClImmId.setClCode2(tmpClCode2);
				tClImmId.setClNo(tmpClNo);
				ClImm tClImm = sClImmService.findById(tClImmId, titaVo);
				if(tClImm==null) {
					tClImm = new ClImm();
				}
				settingSeq = tClImm.getSettingSeq();
				settingStat = tClImm.getSettingStat();
				clStat = tClImm.getClStat();
				settingDate = String.valueOf(tClImm.getSettingDate());
				settingAmt = tClImm.getSettingAmt().toString();
				claimDate = String.valueOf(tClImm.getClaimDate());
				break;
			case 3:
			case 4:
				ClStockId tClStockId = new ClStockId();
				tClStockId.setClCode1(tmpClCode1);
				tClStockId.setClCode2(tmpClCode2);
				tClStockId.setClNo(tmpClNo);
				ClStock tClStock = sClStockService.findById(tClStockId, titaVo);
				if(tClStock==null) {
					tClStock = new ClStock();
				}
				settingStat = tClStock.getSettingStat();
				clStat = tClStock.getClStat();
				settingDate = String.valueOf(tClStock.getSettingDate());
				settingAmt = tClStock.getSettingBalance().toString();
				claimDate = String.valueOf(tClStock.getMtgDate());
				break;
			case 5:
				ClOtherId tClOtherId = new ClOtherId();
				tClOtherId.setClCode1(tmpClCode1);
				tClOtherId.setClCode2(tmpClCode2);
				tClOtherId.setClNo(tmpClNo);
				ClOther tClOther = sClOtherService.findById(tClOtherId, titaVo);
				if(tClOther==null) {
					tClOther = new ClOther();
				}
				settingStat = tClOther.getSettingStat();
				clStat = tClOther.getClStat();
				settingDate = String.valueOf(tClOther.getSettingDate());
				settingAmt = tClOther.getSettingAmt().toString();

				break;
			case 9:
				ClMovablesId tClMovablesId = new ClMovablesId();
				tClMovablesId.setClCode1(tmpClCode1);
				tClMovablesId.setClCode2(tmpClCode2);
				tClMovablesId.setClNo(tmpClNo);
				ClMovables tClMovables = sClMovablesService.findById(tClMovablesId, titaVo);
				if(tClMovables==null) {
					tClMovables = new ClMovables();
				}
				settingStat = tClMovables.getSettingStat();
				clStat = tClMovables.getClStat();
				settingDate = String.valueOf(tClMovables.getMortgageIssueStartDate());
				settingAmt = tClMovables.getSettingAmt().toString();
				claimDate = String.valueOf(tClMovables.getMortgageIssueEndDate());
				break;
			}

			// 若有篩選設定狀態
			if (!iSettingStat.isEmpty()) {
				// 篩選資料
				if (!settingStat.equals(iSettingStat)) {
					continue;
				}
			}
			// 若有篩選擔保品狀態
			if (!iClStat.isEmpty()) {
				// 篩選資料
				if (!clStat.equals(iClStat)) {
					continue;
				}
			}

			occurslist.putParam("OOCustNo", tmpClFac.getCustNo());
			occurslist.putParam("OOCustId", tCustMain.getCustId());
			occurslist.putParam("OOCustName", tCustMain.getCustName());
			occurslist.putParam("OOFacmNo", tmpClFac.getFacmNo());
			occurslist.putParam("OOApproveNo", tmpClFac.getApproveNo());
			occurslist.putParam("OOLineAmt", tFacMain.getLineAmt());
			occurslist.putParam("OOClCode1", tmpClFac.getClCode1());
			occurslist.putParam("OOClCode2", tmpClFac.getClCode2());
			occurslist.putParam("OOClNo", tmpClFac.getClNo());
			occurslist.putParam("OOClTypeCode", tClMain.getClTypeCode());
			occurslist.putParam("OOSettingSeq", settingSeq);
			occurslist.putParam("OOSettingDate", settingDate);
			occurslist.putParam("OOSettingAmt", settingAmt);
			occurslist.putParam("OOSettingStat", settingStat);
			occurslist.putParam("OOClaimDate", claimDate);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		List<LinkedHashMap<String, String>> chkOccursList = this.totaVo.getOccursList();

		if (chkOccursList == null || chkOccursList.size() == 0) {
			throw new LogicException("E2003", "額度與擔保品關聯檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}