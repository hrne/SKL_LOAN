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
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsFac;
import com.st1.itx.db.domain.ClOtherRightsId;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOtherRightsFacService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R46")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R46 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public ClOtherRightsService sClOtherRightsService;
	@Autowired
	public ClOtherRightsFacService sClOtherRightsFacService;

	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R46 ");
		this.totaVo.init(titaVo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部
		// tita
		// 功能
		int iFunCd = parse.stringToInteger(titaVo.getParam("FUNCIND"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		// 他項權利序號
//		int iClSeq = parse.stringToInteger(titaVo.getParam("RimClSeq"));
		String iClSeq = titaVo.getParam("RimClSeq");// 20230315

		// WK
		ClMain tClMain = new ClMain();
		ClMainId ClMainId = new ClMainId();
		new ArrayList<ClOtherRights>();
		new ClOtherRights();
		ClOtherRights tClOtherRights = new ClOtherRights();

		if (!(iFunCd == 1 || iFunCd == 2 || iFunCd == 4 || iFunCd == 5)) {
			throw new LogicException(titaVo, "E0010", "擔保品他項權利檔"); // 功能選擇錯誤
		}

		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		tClMain = sClMainService.findById(ClMainId, titaVo);
		if (tClMain == null) {
			throw new LogicException("E0001", "擔保品主檔");
		}

		ClOtherRightsId tClOtherRightsId = new ClOtherRightsId();
		tClOtherRightsId.setClCode1(iClCode1);
		tClOtherRightsId.setClCode2(iClCode2);
		tClOtherRightsId.setClNo(iClNo);
		tClOtherRightsId.setSeq(iClSeq);

		tClOtherRights = sClOtherRightsService.findById(tClOtherRightsId, titaVo);

		if (tClOtherRights == null) {
			if (iFunCd == 1) {
//				String seq = "0000001"; 
//				String seq = "";
				int cnt = 1;
				Slice<ClOtherRights> slClOtherRights = null;
				slClOtherRights = sClOtherRightsService.findClNo(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE,
						titaVo);
				// 取此擔保品編號最大序號續編+1,//20230319改為自行輸入:9999-999
				if (slClOtherRights != null) {
					List<ClOtherRights> lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();
//					seq = parse.IntegerToString(tClMain.getLastClOtherSeq() + 1, 7);
					cnt += lClOtherRights.size();
				}
				this.totaVo.putParam("L2r46Seq", iClSeq);
				this.totaVo.putParam("L2r46City", "");
				this.totaVo.putParam("L2r46OtherCity", "");
				this.totaVo.putParam("L2r46LandAdm", "");
				this.totaVo.putParam("L2r46OtherLandAdm", "");
				this.totaVo.putParam("L2r46RecYear", 0);
				this.totaVo.putParam("L2r46RecWord", "");
				this.totaVo.putParam("L2r46OtherRecWord", "");
				this.totaVo.putParam("L2r46RecNumber", "");
				this.totaVo.putParam("L2r46RightsNote", "");
				this.totaVo.putParam("L2r46SecuredTotal", 0);
				this.totaVo.putParam("L2r46Cnt", cnt);
				this.totaVo.putParam("L2r46SecuredDate", 0);// 20230315新增
				this.totaVo.putParam("L2r46Location", "");// 20230315新增

			} else {
				throw new LogicException(titaVo, "E0001",
						"擔保品他項權利檔   擔保品編號" + iClCode1 + "-" + iClCode2 + "-" + iClNo + "  他項權利序號:" + iClSeq); // 查無資料
			}
		} else {
			if (iFunCd == 1) {
				throw new LogicException(titaVo, "E0002",
						"擔保品他項權利檔   擔保品編號" + iClCode1 + "-" + iClCode2 + "-" + iClNo + "  他項權利序號:" + iClSeq); // 新增資料已存在
			} else {
				this.totaVo.putParam("L2r46Seq", tClOtherRights.getSeq());
				this.totaVo.putParam("L2r46City", tClOtherRights.getCity() == null ? "" : tClOtherRights.getCity());
				this.totaVo.putParam("L2r46OtherCity",
						tClOtherRights.getOtherCity() == null ? "" : tClOtherRights.getOtherCity());
				this.totaVo.putParam("L2r46LandAdm",
						tClOtherRights.getLandAdm() == null ? "" : tClOtherRights.getLandAdm());
				this.totaVo.putParam("L2r46OtherLandAdm",
						tClOtherRights.getOtherLandAdm() == null ? "" : tClOtherRights.getOtherLandAdm());
				this.totaVo.putParam("L2r46RecYear", tClOtherRights.getRecYear());
				this.totaVo.putParam("L2r46RecWord",
						tClOtherRights.getRecWord() == null ? "" : tClOtherRights.getRecWord());
				this.totaVo.putParam("L2r46OtherRecWord",
						tClOtherRights.getOtherRecWord() == null ? "" : tClOtherRights.getOtherRecWord());
				this.totaVo.putParam("L2r46RecNumber", tClOtherRights.getRecNumber());
				this.totaVo.putParam("L2r46RightsNote", tClOtherRights.getRightsNote());
				this.totaVo.putParam("L2r46SecuredTotal", tClOtherRights.getSecuredTotal());
				// this.totaVo.putParam("L2r46CustNo", tClOtherRights.getCustNo());//20230315
				this.totaVo.putParam("L2r46Cnt", 0);
				this.totaVo.putParam("L2r46SecuredDate", tClOtherRights.getSecuredDate());// 20230315新增
				this.totaVo.putParam("L2r46Location", tClOtherRights.getLocation());// 20230315新增
			}
		}

		Slice<ClOtherRightsFac> slClOtherRightsFac = sClOtherRightsFacService.findClNoSeq(iClCode1, iClCode2, iClNo,
				iClSeq, this.index, this.limit, titaVo);
//		Slice<ClOtherRightsFac> slClOtherRightsFac = sClOtherRightsFacService.findClNo(iClCode1, iClCode2, iClNo,
//				 this.index, this.limit, titaVo);
		List<ClOtherRightsFac> lClOtherRightsFac = slClOtherRightsFac == null ? null : slClOtherRightsFac.getContent();
		int lClOtherRightsFacS = 0;
//		int j = 0;
		if (lClOtherRightsFac != null && lClOtherRightsFac.size() != 0) {
			lClOtherRightsFacS = lClOtherRightsFac.size();
			this.info("L2R46 lClOtherRightsFacS=" + lClOtherRightsFacS);
			for (int i = 0; i < lClOtherRightsFacS; i++) {
				ClOtherRightsFac ClOtherRightsFaceVO = lClOtherRightsFac.get(i);
//				if (iClSeq.equals(ClOtherRightsFaceVO.getSeq())) {
				int Row = i + 1;
//					int Row = j + 1;
//					j=j+1;
				totaVo.putParam("L2r46ApplNo" + Row + "", ClOtherRightsFaceVO.getApproveNo());// 核准編號
//				}
			}
		}

		for (int i = lClOtherRightsFacS + 1; i <= 10; i++) {
//		for ( int i = j + 1; i <= 10; i++) {
			totaVo.putParam("L2r46ApplNo" + i + "", "");// 核准編號
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}