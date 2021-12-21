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
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.domain.ClImmRankDetail;
import com.st1.itx.db.service.ClImmRankDetailService;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R23")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R23 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;
	@Autowired
	public ClImmRankDetailService sClImmRankDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R23 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// new table ClMain
		ClImm tClImm = new ClImm();
		// new pk
		ClImmId ClImmId = new ClImmId();

		// 塞pk
		ClImmId.setClCode1(iClCode1);
		ClImmId.setClCode2(iClCode2);
		ClImmId.setClNo(iClNo);

		tClImm = sClImmService.findById(ClImmId, titaVo);
		if (tClImm == null) {

			switch (iFunCd) {
			case 1: {
				// 若為新增且資料不存在，存空值到totaVo
				tClImm = new ClImm();
				break;
			}
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "L2R23(ClImm)");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "L2R23(ClImm)");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "L2R23(ClImm)");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "L2R23(ClImm)");
			}

		} else {
			if (iFunCd == 1) {
				// 若為新增，但資料已存在，拋錯
				throw new LogicException("E0002", "L2R23(ClImm)");
			}

		}
		this.totaVo.putParam("L2r23ClCode1", tClImm.getClCode1());
		this.totaVo.putParam("L2r23ClCode2", tClImm.getClCode2());
		this.totaVo.putParam("L2r23ClNo", tClImm.getClNo());
		this.totaVo.putParam("L2r23EvaNetWorth", tClImm.getEvaNetWorth());
		this.totaVo.putParam("L2r23LVITax", tClImm.getLVITax());
		this.totaVo.putParam("L2r23RentEvaValue", tClImm.getRentEvaValue());
		this.totaVo.putParam("L2r23RentPrice", tClImm.getRentPrice());
		this.totaVo.putParam("L2r23OwnershipCode", tClImm.getOwnershipCode());
		this.totaVo.putParam("L2r23MtgCode", tClImm.getMtgCode());
		this.totaVo.putParam("L2r23MtgCheck", tClImm.getMtgCheck());
		this.totaVo.putParam("L2r23MtgLoan", tClImm.getMtgLoan());
		this.totaVo.putParam("L2r23MtgPledge", tClImm.getMtgPledge());
		this.totaVo.putParam("L2r23Agreement", tClImm.getAgreement());
		this.totaVo.putParam("L2r23EvaCompany", tClImm.getEvaCompanyCode());
		this.totaVo.putParam("L2r23LimitCancelDate", tClImm.getLimitCancelDate());
		this.totaVo.putParam("L2r23ClCode", tClImm.getClCode());
		this.totaVo.putParam("L2r23LoanToValue", tClImm.getLoanToValue());
		this.totaVo.putParam("L2r23OtherOwnerTotal", tClImm.getOtherOwnerTotal());
		this.totaVo.putParam("L2r23CompensationCopy", tClImm.getCompensationCopy());
		this.totaVo.putParam("L2r23BdRmk", tClImm.getBdRmk());
		this.totaVo.putParam("L2r23MtgReasonCode", tClImm.getMtgReasonCode());
		this.totaVo.putParam("L2r23ReceivedDate", tClImm.getReceivedDate());
		this.totaVo.putParam("L2r23ReceivedNo", tClImm.getReceivedNo());
		this.totaVo.putParam("L2r23CancelDate", tClImm.getCancelDate());
		this.totaVo.putParam("L2r23CancelNo", tClImm.getCancelNo());
		this.totaVo.putParam("L2r23SettingDate", tClImm.getSettingDate());
		this.totaVo.putParam("L2r23ClStat", tClImm.getClStat());
		this.totaVo.putParam("L2r23SettingStat", tClImm.getSettingStat());
		this.totaVo.putParam("L2r23SettingAmt", tClImm.getSettingAmt());
		this.totaVo.putParam("L2r23ClaimDate", tClImm.getClaimDate());
		this.totaVo.putParam("L2r23SettingSeq", tClImm.getSettingSeq());

		List<ClImmRankDetail> lClImmRankDetail = new ArrayList<ClImmRankDetail>();
		Slice<ClImmRankDetail> slClImmRankDetail = sClImmRankDetailService.clNoEq(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
		lClImmRankDetail = slClImmRankDetail == null ? null : slClImmRankDetail.getContent();

		for (int i = 0; i <= 9; i++) {
			this.totaVo.putParam("L2r23FirstCreditor" + i, "");
			this.totaVo.putParam("L2r23FirstAmt" + i, "");
		}

		if (lClImmRankDetail != null) {
			int k = 0;
			for (ClImmRankDetail tClImmRankDetail : lClImmRankDetail) {

				this.totaVo.putParam("L2r23FirstCreditor" + k, tClImmRankDetail.getFirstCreditor());
				this.totaVo.putParam("L2r23FirstAmt" + k, tClImmRankDetail.getFirstAmt());

				k++;
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}