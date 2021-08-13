package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R26")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R26 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R26.class);

	/* DB服務注入 */
	@Autowired
	public ClOtherService sClOtherService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R26 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// new table ClOther
		ClOther tClOther = new ClOther();
		ClMain tClMain = new ClMain();

		// new pk
		ClOtherId tClOtherId = new ClOtherId();
		ClMainId tClMainId = new ClMainId();

		// 塞pk
		tClOtherId.setClCode1(iClCode1);
		tClOtherId.setClCode2(iClCode2);
		tClOtherId.setClNo(iClNo);
		tClMainId.setClCode1(iClCode1);
		tClMainId.setClCode2(iClCode2);
		tClMainId.setClNo(iClNo);

		tClMain = sClMainService.findById(tClMainId, titaVo);

		tClOther = sClOtherService.findById(tClOtherId, titaVo);
		if (tClOther == null) {

			switch (iFunCd) {
			case 1: {
				// 若為新增且資料不存在，存空值到totaVo
				tClOther = new ClOther();
				break;
			}
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "擔保品其他檔");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "擔保品其他檔");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "擔保品其他檔");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "擔保品其他檔");
			}

		} else {
			if (iFunCd == 1) {
				tClOther = new ClOther();
			}

		}

		if (tClMain == null && iFunCd == 1) {
			tClMain = new ClMain();
		}

		this.totaVo.putParam("L2r26ClCode1", tClOther.getClCode1());
		this.totaVo.putParam("L2r26ClCode2", tClOther.getClCode2());
		this.totaVo.putParam("L2r26ClNo", tClOther.getClNo());
		this.totaVo.putParam("L2r26PledgeAmt", tClMain.getEvaAmt());
		this.totaVo.putParam("L2r26PledgeStartDate", tClOther.getPledgeStartDate());
		this.totaVo.putParam("L2r26PledgeEndDate", tClOther.getPledgeEndDate());
		this.totaVo.putParam("L2r26PledgeBankCode", tClOther.getPledgeBankCode());
		this.totaVo.putParam("L2r26PledgeNO", tClOther.getPledgeNO());
		this.totaVo.putParam("L2r26OwnerId", tClOther.getOwnerId());
		this.totaVo.putParam("L2r26OwnerName", tClOther.getOwnerName());
		this.totaVo.putParam("L2r26IssuingId", tClOther.getIssuingId());
		this.totaVo.putParam("L2r26IssuingCounty", tClOther.getIssuingCounty());
		this.totaVo.putParam("L2r26DocNo", tClOther.getDocNo());
		this.totaVo.putParam("L2r26LoanToValue", tClOther.getLoanToValue());
		
		this.totaVo.putParam("L2r26SecuritiesType",tClOther.getSecuritiesType());
		this.totaVo.putParam("L2r26Listed",tClOther.getListed());
		this.totaVo.putParam("L2r26OfferingDate",tClOther.getOfferingDate());
		this.totaVo.putParam("L2r26ExpirationDate",tClOther.getExpirationDate());
		this.totaVo.putParam("L2r26TargetIssuer",tClOther.getTargetIssuer());
		this.totaVo.putParam("L2r26SubTargetIssuer",tClOther.getSubTargetIssuer());
		this.totaVo.putParam("L2r26CreditDate",tClOther.getCreditDate());
		this.totaVo.putParam("L2r26Credit",tClOther.getCredit());
		this.totaVo.putParam("L2r26ExternalCredit",tClOther.getExternalCredit());
		this.totaVo.putParam("L2r26Index",tClOther.getIndex());
		this.totaVo.putParam("L2r26TradingMethod",tClOther.getTradingMethod());
		this.totaVo.putParam("L2r26Compensation",tClOther.getCompensation());
		this.totaVo.putParam("L2r26Investment",tClOther.getInvestment());
		this.totaVo.putParam("L2r26PublicValue",tClOther.getPublicValue());
		
		this.totaVo.putParam("L2r26SettingStat", tClOther.getSettingStat());
		this.totaVo.putParam("L2r26ClStat", tClOther.getClStat());
		this.totaVo.putParam("L2r26SettingDate", tClOther.getSettingDate());
		this.totaVo.putParam("L2r26SettingAmt", tClOther.getSettingAmt());

		this.addList(this.totaVo);
		return this.sendList();
	}
}