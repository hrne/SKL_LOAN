package com.st1.itx.trade.XX;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.domain.TxAttachType;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.db.service.TxAttachTypeService;
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.service.CdGuarantorService;

@Service("XXR99")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class XXR99 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBranchService sCdBranchService;

	@Autowired
	public CdBranchGroupService sCdBranchGroupService;
	
	@Autowired
	public CdBcmService sCdBcmService;

	@Autowired
	public CdCodeService sCdCodeService;

	@Autowired
	public TxAuthGroupService sTxAuthGroupService;

	@Autowired
	public TxTellerService sTxTellerService;

	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;

	@Autowired
	public CdGuarantorService sCdGuarantorService;

	@Autowired
	public CdSyndFeeService sCdSyndFeeService;

	@Autowired
	public TxAttachTypeService txAttachTypeService;

	@Autowired
	public CdEmpService cdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active XXR99 ");
		this.totaVo.init(titaVo);

		int cnt = titaVo.getBodyCount();
//		int cnt = Integer.valueOf(titaVo.getParam("BodyFld1").trim());
		this.info("XXR99 BodyCount = " + cnt);

		String outs = "";
		for (int i = 1; i <= cnt; i++) {
//			String p = "HelpNo"+i;
			String p = "BodyFld" + i;
			String k = titaVo.getParam(p).trim();
			this.info("XXR99 [" + p + "] = " + k + ",len = " + k.length());

			String s = "";
			if ("CDBR".equals(k)) {
				s = getCdbr();

			} else if (k.length() > 4 && "GROP".equals(k.substring(0, 4))) {
				String BrNo = k.substring(4, 8);
				s = getGrop(BrNo,titaVo);

			} else if (k.length() > 7 && "CdCode.".equals(k.substring(0, 7))) {
				String DefCode = k.substring(7);

				s = getCdCode(DefCode);

			} else if ("CdBcm".equals(k)) {

				s = getCdBcm();

			} else if ("CdBcmDept".equals(k)) {

				s = getCdBcmDept();

			} else if ("CdBcmDist".equals(k)) {

				s = getCdBcmDist();

			} else if (k.length() > 12 && "TxAttachType".equals(k.substring(0, 12))) {
				s = getTxAttachType(k);
			} else if ("AUTHGROUP".equals(k)) {
				s = getAuthGroup();
			} else if (k.length() == 14 && "AUTHGROUP".equals(k.substring(0, 9))) {
//			} else if (k.length() == 14) {
				this.info("AUTHGROUP = " + k.substring(0, 9) + "/" + k.substring(9));
				s = getAuthGroup(k.substring(9, 13), k.substring(13, 14));
			} else if ("Teller".equals(k)) {
				s = getTeller(titaVo);
			} else if ("CdLoanNotYet".equals(k)) {
				s = getCdLoanNotYet();
			} else if ("CdLoanNotYet.YetDays".equals(k)) {
				s = getCdLoanNotYetYetDays();
			} else if ("CdGuarantor".equals(k)) {
				s = getCdGuarantor();
			} else if ("CdSyndFee".equals(k)) {
				s = getSyndFeeCode();
			} else {
				throw new LogicException(titaVo, "E0010", "HELP類別:" + k);
			}

			outs = outs + String.format("%04d", clength(s)) + s;

		}

		this.totaVo.putParam("HelpCount", cnt);
		this.totaVo.putParam("HelpDesc", outs);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getTxAttachType(String k) {
		String s = "";

		String tranNo = k.substring(12, 17);

		this.info("XXR99 getTxAttachType TranNo= " + tranNo);

		Slice<TxAttachType> slTxAttachType = txAttachTypeService.findByTranNo(tranNo, 0, Integer.MAX_VALUE);
		List<TxAttachType> lTxAttachType = slTxAttachType == null ? null : slTxAttachType.getContent();

		if (lTxAttachType != null && lTxAttachType.size() > 0) {
			for (TxAttachType txAttachType : lTxAttachType) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += txAttachType.getTypeItem() + ":";
			}
		}
		return s;
	}

	// getCdGuarantor
	private String getCdGuarantor() {
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		// 查詢保證人關係代碼檔
		Slice<CdGuarantor> slCdGuarantor;
		slCdGuarantor = sCdGuarantorService.findAll(this.index, this.limit);
		List<CdGuarantor> lCdGuarantor = slCdGuarantor == null ? null : slCdGuarantor.getContent();

		if (lCdGuarantor == null || lCdGuarantor.size() == 0) {
		} else {
			for (CdGuarantor tCdGuarantor : lCdGuarantor) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += tCdGuarantor.getGuaRelCode() + ":" + tCdGuarantor.getGuaRelItem();
			}
		}

		return s;
	}

	// getCdLoanNotYet
	private String getCdLoanNotYet() {
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdLoanNotYet> slCdLoanNotYet = cdLoanNotYetService.codeLike("%", this.index, this.limit);
		List<CdLoanNotYet> lCdLoanNotYet = slCdLoanNotYet == null ? null : slCdLoanNotYet.getContent();

		if (lCdLoanNotYet == null) {
		} else {
			for (CdLoanNotYet cdLoanNotYet : lCdLoanNotYet) {

				if (!"".equals(s)) {
					s += ";";
				}
				s += cdLoanNotYet.getNotYetCode().trim() + ":" + cdLoanNotYet.getNotYetItem().trim();
			}
		}

		return s;
	}

	// getCdLoanNotYet.YetDays
	private String getCdLoanNotYetYetDays() {
		String s = "";
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdLoanNotYet> slCdLoanNotYet = cdLoanNotYetService.codeLike("%", this.index, this.limit);
		List<CdLoanNotYet> lCdLoanNotYet = slCdLoanNotYet == null ? null : slCdLoanNotYet.getContent();

		if (lCdLoanNotYet == null) {
		} else {
			for (CdLoanNotYet cdLoanNotYet : lCdLoanNotYet) {

				if (!"".equals(s)) {
					s += ";";
				}
				s += cdLoanNotYet.getNotYetCode().trim() + ":" + String.format("%03d", cdLoanNotYet.getYetDays());
			}
		}

		return s;
	}

	// CdBcm help
	private String getCdBcm() {
		this.info("XXR99 getCdBcm");
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdBcm> slCdBcm = sCdBcmService.findAll(this.index, this.limit);
		List<CdBcm> lCdBcm = slCdBcm == null ? null : slCdBcm.getContent();

		if (lCdBcm != null) {
			for (CdBcm tCdBcm : lCdBcm) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += tCdBcm.getUnitCode().trim() + ":" + tCdBcm.getUnitItem().trim();
			}
		}

		return s;
	}

	// unique CdBcm.DeptCode
	private String getCdBcmDept() {
		this.info("XXR99 getCdBcmDept");
		String s = "";
		String DeptCode = "";
		String DeptItem = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdBcm> slCdBcm = sCdBcmService.findDeptCode("000000", "ZZZZZZ", this.index, this.limit);
		List<CdBcm> lCdBcm = slCdBcm == null ? null : slCdBcm.getContent();

		if (lCdBcm != null) {
			for (CdBcm tCdBcm : lCdBcm) {
				if (DeptCode.equals(tCdBcm.getDeptCode())) {
					continue;
				}
				if (!"".equals(s)) {
					s += ";";
				}
				DeptCode = tCdBcm.getDeptCode().trim();
				DeptItem = tCdBcm.getDeptItem().trim();
				s += DeptCode + ":" + DeptItem;
			}
		}

		return s;
	}

	// unique CdBcm.DistCode

	private String getCdBcmDist() {
		this.info("XXR99 getCdBcmDist");
		String s = "";
		String DistCode = "";
		String DistItem = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdBcm> slCdBcm = sCdBcmService.findDistCode("000000", "ZZZZZZ", this.index, this.limit);
		List<CdBcm> lCdBcm = slCdBcm == null ? null : slCdBcm.getContent();

		if (lCdBcm != null) {
			for (CdBcm tCdBcm : lCdBcm) {
				if (DistCode.equals(tCdBcm.getDistCode())) {
					continue;
				}
				if (!"".equals(s)) {
					s += ";";
				}
				DistCode = tCdBcm.getDistCode().trim();
				DistItem = tCdBcm.getDistItem().trim();
				s += DistCode + ":" + DistItem;
			}
		}

		return s;
	}

	private String getCdbr() {
		this.info("XXR99 getCdbr");
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdBranch> slCdBranch = sCdBranchService.findAll(this.index, this.limit);
		List<CdBranch> lCdBranch = slCdBranch == null ? null : slCdBranch.getContent();

		if (lCdBranch.size() > 0) {
			for (CdBranch tCdBranch : lCdBranch) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += tCdBranch.getBranchNo().trim() + ":" + tCdBranch.getBranchItem().trim();
			}
		}

		return s;
	}

	private String getGrop(String BrNo ,TitaVo titaVo) {
		this.info("XXR99 getGrop = " + BrNo);
		String s = "";

//		String BrNo = k.substring(4, 8);
		this.info("XXR99 GROP = " + BrNo);
		Slice<CdBranchGroup> tCdBranchGroup = sCdBranchGroupService.findByBranchNo(BrNo, 0, Integer.MAX_VALUE, titaVo);
		
		List<CdBranchGroup> lCdBranchGroup = tCdBranchGroup == null ? null : tCdBranchGroup.getContent();
		if (lCdBranchGroup != null) {
			for(CdBranchGroup Cdbg : lCdBranchGroup) {
				s +=Cdbg.getGroupNo().trim()+ ":" + Cdbg.getGroupItem().trim();
			}
		}

		return s;
	}

	private String getCdCode(String DefCode) {
		this.info("XXR99 getCdCode = " + DefCode);
		String s = "";

//		int DefNo = Integer.valueOf(k.substring(5, 9));

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdCode> slCdCode = sCdCodeService.defCodeEq(DefCode, "%", this.index, this.limit);
		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();

		if (lCdCode != null) {
			for (CdCode tCdCode : lCdCode) {
				if (!"".equals(s)) {
					s += ";";
				}

				String ss = ":Y";
				if ("N".equals(tCdCode.getEnable())) {
					ss = ":N";
				}
				s += tCdCode.getCode() + ":" + tCdCode.getItem().trim() + ss;
			}
		}

		return s;
	}

	private String getAuthGroup() {
		this.info("XXR99 getAuthGroup");
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<TxAuthGroup> slTxAuthGroup = sTxAuthGroupService.findAll(this.index, this.limit);
		List<TxAuthGroup> lTxAuthGroup = slTxAuthGroup == null ? null : slTxAuthGroup.getContent();

		if (lTxAuthGroup != null) {
			for (TxAuthGroup tTxAuthGroup : lTxAuthGroup) {
				if (!"".equals(s)) {
					s += ";";
				}
				String ss = ":N";
				if (tTxAuthGroup.getStatus() == 1) {
					ss = ":Y";
				}
				s += tTxAuthGroup.getAuthNo().trim() + ":" + tTxAuthGroup.getAuthItem().trim() + ss;
			}
		}

		return s;
	}

	private String getAuthGroup(String branchNo, String levelFg) {
		this.info("XXR99 getAuthGroup = " + branchNo + "/" + levelFg);
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<TxAuthGroup> slTxAuthGroup = sTxAuthGroupService.BranchAll(branchNo, Integer.valueOf(levelFg), this.index,
				this.limit);
		List<TxAuthGroup> lTxAuthGroup = slTxAuthGroup == null ? null : slTxAuthGroup.getContent();

		if (lTxAuthGroup != null) {
			for (TxAuthGroup tTxAuthGroup : lTxAuthGroup) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += tTxAuthGroup.getAuthNo().trim() + ":" + tTxAuthGroup.getAuthItem().trim();
			}
		}

		this.info("XXR99 getAuthGroup = " + branchNo + "/" + s);

		return s;
	}

	private String getTeller(TitaVo titaVo) {
		this.info("XXR99 getTeller");
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		String iTlrNo = titaVo.getTlrNo();
		String iBrno = titaVo.getBrno();

		Slice<TxTeller> slTxTeller = sTxTellerService.findAll(this.index, this.limit);
		List<TxTeller> lTxTeller = slTxTeller == null ? null : slTxTeller.getContent();

		String iGroupNo = "";
		TxTeller tTxTeller = sTxTellerService.findById(iTlrNo, titaVo);
		if (tTxTeller != null) {
			iGroupNo = tTxTeller.getGroupNo();
		}

		if (lTxTeller != null) {

			for (TxTeller txTeller : lTxTeller) {

				if (txTeller.getLevelFg() == 3) {// 經辦不同課組別跳過
					if (!(iGroupNo).equals(txTeller.getGroupNo())) {
						continue;
					}
				} else if (txTeller.getLevelFg() == 1) {// 主管不同單位跳過
					if (!(iBrno).equals(txTeller.getBrNo())) {
						continue;
					}
				}

				CdEmp cdEmp = cdEmpService.findById(txTeller.getTlrNo());

				if (cdEmp == null) {
					continue;
				}

				if (!"".equals(s)) {
					s += ";";
				}
				s += txTeller.getTlrNo().trim() + ":" + cdEmp.getFullname().trim();
			}
		}

		return s;
	}

//聯貸案費用help
	private String getSyndFeeCode() {
		this.info("XXR99 getSyndFeeCode");
		String s = "";

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = 0;

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;

		Slice<CdSyndFee> slCdSyndFee = sCdSyndFeeService.findAll(this.index, this.limit);
		List<CdSyndFee> lCdSyndFee = slCdSyndFee == null ? null : slCdSyndFee.getContent();

		if (lCdSyndFee != null) {
			for (CdSyndFee cdSyndFee : lCdSyndFee) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += cdSyndFee.getSyndFeeCode().trim() + ":" + cdSyndFee.getSyndFeeItem().trim();
			}
		}

		return s;
	}

	// 中文以2位計算長度
	private static int clength(String s) {
		int clen = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				clen += 2;
			} else {
				clen += 1;
			}

//			this.info("XXR99 clength [" + c + "] = " + clen);
		}

		return clen;
	}

}