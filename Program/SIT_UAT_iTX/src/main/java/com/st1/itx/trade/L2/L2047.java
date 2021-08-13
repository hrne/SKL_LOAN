package com.st1.itx.trade.L2;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * END=X,1<br>
 */

@Service("L2047")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2047 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2047.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public ClMovablesService sClMovablesService;

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService sFacCaseApplService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2047 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 28 * 500 = 14000

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		// new ArrayList
		List<ClMovables> lClMovables = new ArrayList<ClMovables>();
		Slice<ClMovables> slClMovables = null;
		// new TABLE
		ClMovables tClMovables = new ClMovables();

		// new PK
		ClMovablesId ClMovablesId = new ClMovablesId();
		ClMovablesId.setClCode1(iClCode1);
		ClMovablesId.setClCode2(iClCode2);
		ClMovablesId.setClNo(iClNo);

		// 擔保品代號2有輸入
		if (iClCode2 != 0) {
			// 擔保品編號有輸入
			if (iClNo > 0) {
				this.info("代號2有輸入.擔保品編號有輸入");
				tClMovables = sClMovablesService.findById(ClMovablesId, titaVo);
				// 輸入擔保品編號查無資料 拋錯
				if (tClMovables == null) {
					throw new LogicException(titaVo, "E2003", "L2047擔保品動產檔尚無資料,請至L2412新增"); // 查無資料
				}
				lClMovables.add(tClMovables);
				// 擔保品編號沒輸入
			} else {
				this.info("代號2有輸入.擔保品編號沒輸入");
				slClMovables = sClMovablesService.findClCode2(iClCode1, iClCode2, this.index, this.limit, titaVo);
				lClMovables = slClMovables == null ? null : slClMovables.getContent();
			}
			// 擔保品代號2沒輸入
		} else {
			// 擔保品編號沒輸入
			if (iClNo == 0) {
				this.info("代號2沒輸入.擔保品編號沒輸入 查全部");
				slClMovables = sClMovablesService.findAll(this.index, this.limit, titaVo);
				lClMovables = slClMovables == null ? null : slClMovables.getContent();
				// 擔保品編號有輸入
			} else {
				this.info("代號2沒輸入.擔保品編號有輸入");
				slClMovables = sClMovablesService.selectL2047(iClCode1, 1, 99, iClNo, this.index, this.limit, titaVo);
				lClMovables = slClMovables == null ? null : slClMovables.getContent();
			}
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClMovables != null && slClMovables.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		// 查無資料 拋錯
		if (lClMovables == null) {
			throw new LogicException(titaVo, "E2003", "L2047擔保品動產檔尚無資料,請至L2412新增"); // 查無資料
		}

		this.info("L2047 lClMovables " + lClMovables);
		for (ClMovables tClMovables2 : lClMovables) {

			// new occurs
			OccursList occurslist = new OccursList();
			// new ArrayList
			List<ClFac> lClFac = new ArrayList<ClFac>();
			// new table
			ClFac tClFac = new ClFac();
			FacMain tFacMain = new FacMain();

			// 取動產檔的擔保品代號1,擔保品代號2,擔保品編號 找擔保品與額度關聯檔(核准號碼,設定狀態),額度主檔(核准額度)
			int ClCode1 = tClMovables2.getClCode1();
			int ClCode2 = tClMovables2.getClCode2();
			int ClNo = tClMovables2.getClNo();
			// 找擔保品與額度關聯檔
			Slice<ClFac> slClFac = sClFacService.clNoEq(ClCode1, ClCode2, ClNo, 0, Integer.MAX_VALUE, titaVo);
			if (slClFac != null) {

			}

			BigDecimal wkLineAmt = BigDecimal.ZERO;

			lClFac = slClFac == null ? null : slClFac.getContent();


			if (lClFac != null) {
				for (ClFac tmpClFac : lClFac) {
					// 找額度主檔
					tFacMain = sFacMainService.facmApplNoFirst(tmpClFac.getApproveNo(), titaVo);
					if (tFacMain != null) {
						wkLineAmt = tFacMain.getLineAmt();
					}

					occurslist.putParam("OOLicenseNo", tClMovables2.getLicenseNo());
					occurslist.putParam("OOClCode1", tClMovables2.getClCode1());
					occurslist.putParam("OOClCode2", tClMovables2.getClCode2());
					occurslist.putParam("OOClNo", tClMovables2.getClNo());
					occurslist.putParam("OOLineAmt", wkLineAmt);
					occurslist.putParam("OOApproveNo", tmpClFac.getApproveNo());
					occurslist.putParam("OOSettingStat", tClMovables2.getSettingStat());
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occurslist);
				}
			} else {
				occurslist.putParam("OOLicenseNo", tClMovables2.getLicenseNo());
				occurslist.putParam("OOClCode1", tClMovables2.getClCode1());
				occurslist.putParam("OOClCode2", tClMovables2.getClCode2());
				occurslist.putParam("OOClNo", tClMovables2.getClNo());
				occurslist.putParam("OOLineAmt", wkLineAmt);
				occurslist.putParam("OOApproveNo", 0);
				occurslist.putParam("OOSettingStat", tClMovables2.getSettingStat());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}