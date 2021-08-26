package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R55")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R55 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ051Service iJcicZ051Service;
	@Autowired
	public JcicZ052Service iJcicZ052Service;
	@Autowired
	public JcicZ053Service iJcicZ053Service;
	@Autowired
	public JcicZ054Service iJcicZ054Service;
	@Autowired
	public JcicZ055Service iJcicZ055Service;
	@Autowired
	public JcicZ056Service iJcicZ056Service;
	@Autowired
	public JcicZ060Service iJcicZ060Service;
	@Autowired
	public JcicZ061Service iJcicZ061Service;
	@Autowired
	public JcicZ062Service iJcicZ062Service;
	@Autowired
	public JcicZ063Service iJcicZ063Service;
	@Autowired
	public JcicZ440Service iJcicZ440Service;
	@Autowired
	public JcicZ442Service iJcicZ442Service;
	@Autowired
	public JcicZ443Service iJcicZ443Service;
	@Autowired
	public JcicZ444Service iJcicZ444Service;
	@Autowired
	public JcicZ447Service iJcicZ447Service;
	@Autowired
	public JcicZ448Service iJcicZ448Service;
	@Autowired
	public JcicZ451Service iJcicZ451Service;
	@Autowired
	public JcicZ454Service iJcicZ454Service;
	@Autowired
	public JcicZ570Service iJcicZ570Service;
	@Autowired
	public JcicZ571Service iJcicZ571Service;
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	@Autowired
	public JcicZ573Service iJcicZ573Service;
	@Autowired
	public JcicZ574Service iJcicZ574Service;
	@Autowired
	public JcicZ575Service iJcicZ575Service;
	/* 日期工具 */
	@Autowired
	public DateUtil iDateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse iParse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r55 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		String iChainCd = titaVo.getParam("RimChainCd");
		switch(iChainCd) {
		case "L8312":
			JcicZ051 iJcicZ051 = new JcicZ051();
			iJcicZ051 = iJcicZ051Service.ukeyFirst(iUkey, titaVo);
			if (iJcicZ051 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55SubmitKey", iJcicZ051.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ051.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode","");
				totaVo.putParam("L8r55DelayYM", iJcicZ051.getDelayYM());
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8313":
			JcicZ052 iJcicZ052 = new JcicZ052();
			iJcicZ052 = iJcicZ052Service.ukeyFirst(iUkey, titaVo);
			if (iJcicZ052 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55SubmitKey", iJcicZ052.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ052.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode","");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8314":
			JcicZ053 iJcicZ053 = new JcicZ053();
			iJcicZ053 = iJcicZ053Service.ukeyFirst(iUkey, titaVo);
			if (iJcicZ053 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ053.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ053.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ053.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", iJcicZ053.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8315":
			JcicZ054 iJcicZ054 = new JcicZ054();
			iJcicZ054 = iJcicZ054Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ054 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ054.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ054.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ054.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", iJcicZ054.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", iJcicZ054.getPayOffDate());
			}
		break;
		case "L8316":
			JcicZ055 iJcicZ055 = new JcicZ055();
			iJcicZ055 = iJcicZ055Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ055 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ055.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ055.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", "");
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", iJcicZ055.getCaseStatus());
				totaVo.putParam("L8r55ClaimDate", iJcicZ055.getClaimDate());
				totaVo.putParam("L8r55CourtCode", iJcicZ055.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8317":
			JcicZ056 iJcicZ056 = new JcicZ056();
			iJcicZ056 = iJcicZ056Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ056 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ056.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ056.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", "");
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", iJcicZ056.getCaseStatus());
				totaVo.putParam("L8r55ClaimDate", iJcicZ056.getClaimDate());
				totaVo.putParam("L8r55CourtCode", iJcicZ056.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8318":
			JcicZ060 iJcicZ060 = new JcicZ060();
			iJcicZ060 = iJcicZ060Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ060 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ060.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ060.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ060.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", iJcicZ060.getChangePayDate());
				totaVo.putParam("L8r55CaseStatus","");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8319":
			JcicZ061 iJcicZ061 = new JcicZ061();
			iJcicZ061 = iJcicZ061Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ061 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ061.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ061.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ061.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", iJcicZ061.getChangePayDate());
				totaVo.putParam("L8r55CaseStatus","");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", iJcicZ061.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8320":
			JcicZ062 iJcicZ062 = new JcicZ062();
			iJcicZ062 = iJcicZ062Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ062 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ062.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ062.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ062.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", iJcicZ062.getChangePayDate());
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8321":
			JcicZ063 iJcicZ063 = new JcicZ063();
			iJcicZ063 = iJcicZ063Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ063 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ063.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ063.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ063.getRcDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", iJcicZ063.getChangePayDate());
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8322":
			JcicZ440 iJcicZ440 = new JcicZ440();
			iJcicZ440 = iJcicZ440Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ440 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ440.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ440.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ440.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ440.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8323":
			JcicZ442 iJcicZ442 = new JcicZ442();
			iJcicZ442 = iJcicZ442Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ442 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ442.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ442.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ442.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ442.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", iJcicZ442.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8324":
			JcicZ443 iJcicZ443 = new JcicZ443();
			iJcicZ443 = iJcicZ443Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ443 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ443.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ443.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ443.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ443.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", iJcicZ443.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", iJcicZ443.getAccount());
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8325":
			JcicZ444 iJcicZ444 = new JcicZ444();
			iJcicZ444 = iJcicZ444Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ444 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ444.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ444.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ444.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ444.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8327":
			JcicZ447 iJcicZ447 = new JcicZ447();
			iJcicZ447 = iJcicZ447Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ447 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ447.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ447.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ447.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ447.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8328":
			JcicZ448 iJcicZ448 = new JcicZ448();
			iJcicZ448 = iJcicZ448Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ448 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ448.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ448.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ448.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ448.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", iJcicZ448.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;	
		case "L8330":
			JcicZ451 iJcicZ451 = new JcicZ451();
			iJcicZ451 = iJcicZ451Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ451 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ451.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ451.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ451.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ451.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", iJcicZ451.getDelayYM());
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8331":
			JcicZ454 iJcicZ454 = new JcicZ454();
			iJcicZ454 = iJcicZ454Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ454 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ454.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ454.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ454.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", iJcicZ454.getCourtCode());
				totaVo.putParam("L8r55MaxMainCode", iJcicZ454.getMaxMainCode());
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8332":
			JcicZ570 iJcicZ570 = new JcicZ570();
			iJcicZ570 = iJcicZ570Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ570 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ570.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ570.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ570.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8333":
			JcicZ571 iJcicZ571 = new JcicZ571();
			iJcicZ571 = iJcicZ571Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ571 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ571.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ571.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ571.getApplyDate());
				totaVo.putParam("L8r55BankId", iJcicZ571.getBankId());
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8334":
			JcicZ572 iJcicZ572 = new JcicZ572();
			iJcicZ572 = iJcicZ572Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ572 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ572.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ572.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ572.getApplyDate());
				totaVo.putParam("L8r55BankId", iJcicZ572.getBankId());
				totaVo.putParam("L8r55PayDate", iJcicZ572.getPayDate());
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8335":
			JcicZ573 iJcicZ573 = new JcicZ573();
			iJcicZ573 = iJcicZ573Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ573 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ573.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ573.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ573.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", iJcicZ573.getPayDate());
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8336":
			JcicZ574 iJcicZ574 = new JcicZ574();
			iJcicZ574 = iJcicZ574Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ574 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ574.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ574.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ574.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		case "L8337":
			JcicZ575 iJcicZ575 = new JcicZ575();
			iJcicZ575 = iJcicZ575Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ575 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ575.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ575.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ575.getApplyDate());
				totaVo.putParam("L8r55BankId", iJcicZ575.getBankId());
				totaVo.putParam("L8r55PayDate", "");
				totaVo.putParam("L8r55ChangePayDate", "");
				totaVo.putParam("L8r55CaseStatus", "");
				totaVo.putParam("L8r55ClaimDate", "");
				totaVo.putParam("L8r55CourtCode", "");
				totaVo.putParam("L8r55MaxMainCode", "");
				totaVo.putParam("L8r55DelayYM", "");
				totaVo.putParam("L8r55Account", "");
				totaVo.putParam("L8r55PayOffDate", "");
			}
		break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}