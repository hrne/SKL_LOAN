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
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchAuthLogService;
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
				if ("1".equals(tPostAuthLog.getAuthApplCode()) && tPostAuthLog.getPropDate() == 0) {
					flag = 8;
				} else if ("1".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) {
					flag = 1;
				} else if ("9".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) {
					flag = 2;
				} else if ("2".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) {
					flag = 9;
				}

//				塞不塞值為var檢核
				result = flag;
				relationCode = tPostAuthLog.getRelationCode();
				relAcctName = tPostAuthLog.getRelAcctName();
				relationId = tPostAuthLog.getRelationId();
				relAcctBirthday = tPostAuthLog.getRelAcctBirthday();
				relAcctGender = tPostAuthLog.getRelAcctGender();
			} else {
				this.info("tPostAuthLog null ... ");
			}

		} else {

			AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(custNo, bankNo, acctNo, titaVo);

			if (tAchAuthLog != null) {
//				A:新增(恢復授權) D:取消 Z:暫停授權
//				非A或找不到 = 未授權

				this.info("tAchAuthLog ... " + tAchAuthLog.toString());

				int flag = 0;
				if ("A".equals(tAchAuthLog.getCreateFlag()) && tAchAuthLog.getPropDate() > 0) { // 已提出
					flag = 8;
				} else if ("A".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) { // 已提回授權成功
					flag = 1;
				} else if ("Z".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) { // 暫停授權
					flag = 2;
				} else if ("D".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) { // 取消授權
					flag = 9;
				}
//				塞不塞值為var檢核
				result = flag;
				relationCode = tAchAuthLog.getRelationCode();
				relAcctName = tAchAuthLog.getRelAcctName();
				relationId = tAchAuthLog.getRelationId();
				relAcctBirthday = tAchAuthLog.getRelAcctBirthday();
				relAcctGender = tAchAuthLog.getRelAcctGender();
				limitAmt = tAchAuthLog.getLimitAmt();
				wkNewCreateFlag = tAchAuthLog.getCreateFlag();
				if (funCd == 2) {
					if ("A".equals(tAchAuthLog.getCreateFlag())) {
						if ("0".equals(tAchAuthLog.getAuthStatus())) {

							// 刪除日期/暫停授權日期>0自動放A.新增授權 否則 Z.暫停授權
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