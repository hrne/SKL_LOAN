package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2042")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2042 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingService sClBuildingService;

	/* DB服務注入 */
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;

	@Autowired
	public CustMainService custMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2042 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 192 * 300 = 57600

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// new ArrayList ClBuilding
		List<ClBuilding> lClBuilding = new ArrayList<ClBuilding>();
		Slice<ClBuilding> slClBuilding = null;

		// new ClImm PK
		ClImmId ClImmId = new ClImmId();

		// new table
		ClImm tClImm = new ClImm();
		ClBuildingOwner tClBuildingOwner = new ClBuildingOwner();

		// iClCode2 iClNo 值為0時 用ClCode1查不動產建物檔
		if (iClNo == 0 && iClCode2 == 0) {
			slClBuilding = sClBuildingService.findClCode1(iClCode1, this.index, this.limit, titaVo);

			lClBuilding = slClBuilding == null ? null : slClBuilding.getContent();
			// iClNo 值為0時 用ClCode1,iClCode2查不動產建物檔
		} else if (iClNo == 0) {
			slClBuilding = sClBuildingService.findClCode2(iClCode1, iClCode2, this.index, this.limit, titaVo);

			lClBuilding = slClBuilding == null ? null : slClBuilding.getContent();
			// 用ClCode1,iClCode2,iClNo查不動產建物檔
		} else {
			slClBuilding = sClBuildingService.findClNo(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);

			lClBuilding = slClBuilding == null ? null : slClBuilding.getContent();
		}
		// 查無不動產建物檔資料 拋錯
		if (lClBuilding == null) {
			throw new LogicException("E0001", "擔保品不動產建物檔");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slClBuilding != null && slClBuilding.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		this.info("lClBuilding L2042" + lClBuilding);
		for (ClBuilding tClBuilding : lClBuilding) {

			// new occurs
			OccursList occurslist = new OccursList();
			// new table
			tClImm = new ClImm();
			tClBuildingOwner = new ClBuildingOwner();
			// new ClImm PK
			ClImmId = new ClImmId();

			// 依擔保品編號查Owner統編
			int clcode1 = tClBuilding.getClCode1();
			int clcode2 = tClBuilding.getClCode2();
			int clno = tClBuilding.getClNo();
			Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(clcode1, clcode2, clno, this.index, this.limit, titaVo);

			List<ClBuildingOwner> lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
			if (lClBuildingOwner != null) {
				tClBuildingOwner = lClBuildingOwner.get(0);
				CustMain custMain = custMainService.findById(tClBuildingOwner.getOwnerCustUKey(), titaVo);
				if (custMain != null) {
					occurslist.putParam("OOCustId", custMain.getCustId());
				} else {
					occurslist.putParam("OOCustId", "");
				}
			} else {
				occurslist.putParam("OOCustId", "");
			}

			// 拿擔保品編號取ClImm設定金額
			ClImmId.setClCode1(clcode1);
			ClImmId.setClCode2(clcode2);
			ClImmId.setClNo(clno);
			tClImm = sClImmService.findById(ClImmId);
			if (tClImm == null) {
				tClImm = new ClImm();
			}

			occurslist.putParam("OOClCode1", tClBuilding.getClCode1());
			occurslist.putParam("OOClCode2", tClBuilding.getClCode2());
			occurslist.putParam("OOClNo", tClBuilding.getClNo());
			occurslist.putParam("OOBdNo1", tClBuilding.getBdNo1());
			occurslist.putParam("OOBdNo2", tClBuilding.getBdNo2());
			occurslist.putParam("OOSettingAmt", tClImm.getSettingAmt());
			occurslist.putParam("OOBdLocation", tClBuilding.getBdLocation().trim());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}