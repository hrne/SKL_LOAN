package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R22")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R22 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public PostAuthLogService postAuthLogService;
	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R22 ");
		this.totaVo.init(titaVo);
		int funCd = parse.stringToInteger(titaVo.get("RimFuncCode"));
		int custNo = parse.stringToInteger(titaVo.get("RimCustNo"));
		String bankNo = FormatUtil.pad9(titaVo.getParam("RimBankNo"), 3);
		String acctNo = FormatUtil.pad9(titaVo.getParam("RimAcctNo"), 14);
		int result = 0;

		String relationCode = "";
		String relAcctName = "";
		String relationId = "";
		int relAcctBirthday = 0;
		String relAcctGender = "";
		String wkNewCreateFlag = "";
		BigDecimal limitAmt = BigDecimal.ZERO;

		if ("700".equals(bankNo)) {
			String postDepCode = titaVo.getParam("RimPostDepCode");

			PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(custNo, postDepCode, acctNo, "1", titaVo);

			if (tPostAuthLog != null) {
				this.info("tPostAuthLog ... " + tPostAuthLog.toString());

				int flag = 0;
				if ("1".equals(tPostAuthLog.getAuthApplCode()) && tPostAuthLog.getPropDate() == 0) { // ????????????????????? 8
					flag = 8;
				} else if ("1".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) { // 1.???????????????????????????
					flag = 1;
				} else if ("9".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) { // 2.???????????????????????????
					flag = 2;
				} else if ("2".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) { // 9.???????????????????????????
					flag = 9;
				} else if ("1".equals(tPostAuthLog.getAuthApplCode()) && tPostAuthLog.getPropDate() > 0
						&& tPostAuthLog.getRetrDate() == 0) { // 7.??????????????????????????????
					flag = 7;
				}

//				???????????????var??????
				result = flag;
				relationCode = tPostAuthLog.getRelationCode();
				relAcctName = tPostAuthLog.getRelAcctName();
				relationId = tPostAuthLog.getRelationId();
				CustMain tCustMain = custMainService.custIdFirst(relationId, titaVo);
				relAcctBirthday = 0;
				relAcctGender = "";

				if (tPostAuthLog.getRelAcctBirthday() != 0) {
					relAcctBirthday = tPostAuthLog.getRelAcctBirthday();
				} else {
					if (tCustMain != null) {
						relAcctBirthday = tCustMain.getBirthday();
					}
				}
				if (!tPostAuthLog.getRelAcctGender().isEmpty()) {
					relAcctGender = tPostAuthLog.getRelAcctGender();
				} else {
					if (tCustMain != null) {
						relAcctGender = tCustMain.getSex();
					}
				}
				limitAmt = tPostAuthLog.getLimitAmt();
			} else {
				this.info("tPostAuthLog null ... ");
			}

		} else {

			AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(custNo, bankNo, acctNo, titaVo);

			if (tAchAuthLog != null) {
//				A:??????(????????????) D:?????? Z:????????????
//				???A???????????? = ?????????

				this.info("tAchAuthLog ... " + tAchAuthLog.toString());

				int flag = 0;
				if ("A".equals(tAchAuthLog.getCreateFlag()) && tAchAuthLog.getPropDate() == 0) { // ?????????????????????
					flag = 8;
				} else if ("A".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) { // ?????????????????????
					flag = 1;
				} else if ("Z".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) { // ????????????
					flag = 2;
				} else if ("D".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) { // ????????????
					flag = 9;
				} else if ("A".equals(tAchAuthLog.getCreateFlag()) && tAchAuthLog.getPropDate() > 0
						&& tAchAuthLog.getRetrDate() == 0) { // 7.??????????????????????????????
					flag = 7;
				}
//				???????????????var??????
				result = flag;
				relationCode = tAchAuthLog.getRelationCode();
				relAcctName = tAchAuthLog.getRelAcctName();
				relationId = tAchAuthLog.getRelationId();

				CustMain tCustMain = custMainService.custIdFirst(relationId, titaVo);
				relAcctBirthday = 0;
				relAcctGender = "";
				if (tAchAuthLog.getRelAcctBirthday() != 0) {
					relAcctBirthday = tAchAuthLog.getRelAcctBirthday();
				} else {
					if (tCustMain != null) {
						relAcctBirthday = tCustMain.getBirthday();
					}
				}
				if (!tAchAuthLog.getRelAcctGender().isEmpty()) {
					relAcctGender = tAchAuthLog.getRelAcctGender();
				} else {
					if (tCustMain != null) {
						relAcctGender = tCustMain.getSex();
					}
				}
				limitAmt = tAchAuthLog.getLimitAmt();
				wkNewCreateFlag = tAchAuthLog.getCreateFlag();
				if (funCd == 2) {
					if ("A".equals(tAchAuthLog.getCreateFlag())) {
						if ("0".equals(tAchAuthLog.getAuthStatus())) {

							// ????????????/??????????????????>0?????????A.???????????? ?????? Z.????????????
							if (tAchAuthLog.getDeleteDate() > 0) {
								wkNewCreateFlag = "A";
							} else {
								wkNewCreateFlag = "Z";
							}
						}
					}
				}

			} else {
				this.info("tAchAuthLog null... ");
			}
		}

		totaVo.putParam("L4r22CheckCode", result);
		totaVo.putParam("L4r22RelationCode", relationCode);
		totaVo.putParam("L4r22RelAcctName", relAcctName);
		totaVo.putParam("L4r22RelationId", relationId);
		totaVo.putParam("L4r22RelAcctBirthday", relAcctBirthday);
		totaVo.putParam("L4r22RelAcctGender", relAcctGender);
		totaVo.putParam("L4r22LimitAmt", limitAmt);
		totaVo.putParam("L4r22NewCreateFlag", wkNewCreateFlag);

		this.addList(this.totaVo);
		return this.sendList();
	}
}