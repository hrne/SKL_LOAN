package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("L4943ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4943ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int flag, TitaVo titaVo) throws Exception {

		this.info("L4943.findAll");

//		會計日期    #AcDate
//		整批批號    #BatchNo
//		作業狀態    #StatusCode
//		還款來源    #RepayCode
//		檔名            #FileName
//		處理狀態    #ProcStsCode
//		戶號           #CustNo

		int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo").trim());
		String repayBank = titaVo.getParam("BankCode");
		int opItem = parse.stringToInteger(titaVo.getParam("OpItem"));
		int repayType = parse.stringToInteger(titaVo.getParam("RepayType").trim());
		int entryDateFm = parse.stringToInteger(titaVo.getParam("EntryDateFm").trim()) + 19110000;
		int entryDateTo = parse.stringToInteger(titaVo.getParam("EntryDateTo").trim()) + 19110000;
		BigDecimal postLimitAmt = parse.stringToBigDecimal(titaVo.getParam("PostLimitAmt"));
		BigDecimal singleLimitAmt = parse.stringToBigDecimal(titaVo.getParam("SingleLimit"));
		BigDecimal lowLimitAmt = parse.stringToBigDecimal(titaVo.getParam("LowLimit"));

		this.info("functionCode = " + functionCode);
		this.info("custNo = " + custNo);
		this.info("repayBank = " + repayBank);
		this.info("opItem = " + opItem);
		this.info("repayType = " + repayType);
		this.info("entryDateFm = " + entryDateFm);
		this.info("entryDateTo = " + entryDateTo);
		this.info("postLimitAmt = " + postLimitAmt);
		this.info("singleLimitAmt = " + singleLimitAmt);

		String sql = "";
//		flag 0.Part 1.All
		if (flag == 0) {
			sql += " select                                                   ";
			sql += "  BDD.\"EntryDate\"   AS F0                               ";
			sql += " ,BDD.\"CustNo\"      AS F1                               ";
			sql += " ,BDD.\"FacmNo\"      AS F2                               ";
			sql += " ,BDD.\"BormNo\"      AS F3                               ";
			sql += " ,BDD.\"PrevIntDate\" AS F4                               ";
			sql += " ,BDD.\"PayIntDate\"  AS F5                               ";
			sql += " ,BDD.\"RepayType\"   AS F6                               ";
			sql += " ,BDD.\"UnpaidAmt\"   AS F7                               ";
			sql += " ,BDD.\"TempAmt\"     AS F8                               ";
			sql += " ,BDD.\"RepayAmt\"    AS F9                               ";
			sql += " ,BDD.\"MediaCode\"   AS F10                              ";
			sql += " ,BDD.\"AcDate\"      AS F11                              ";
			sql += " ,BDD.\"JsonFields\"  AS F12                              ";
			sql += " ,BDD.\"ReturnCode\"  AS F13                              ";
			sql += " ,BDD.\"MediaKind\"   AS F14                              ";
			sql += " from \"BankDeductDtl\" BDD                               ";
		} else {
			sql += " select                                                   ";
			sql += "  SUM(BDD.\"UnpaidAmt\")   AS F0                          ";
			sql += " ,SUM(BDD.\"TempAmt\")     AS F1                          ";
			sql += " ,SUM(BDD.\"RepayAmt\")    AS F2                          ";
			sql += " from \"BankDeductDtl\" BDD                               ";
		}
		if (functionCode == 2) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"CustNo\"                                          ";
			sql += "     ,SUM(\"RepayAmt\") as \"RepayAmt\"                   ";
			sql += "     from \"BankDeductDtl\"                               ";
			sql += "     where \"RepayBank\" = 700                            ";
			sql += "       and \"EntryDate\" >= " + entryDateFm;
			sql += "       and \"EntryDate\" <= " + entryDateTo;
			sql += "     group by \"CustNo\"                                  ";
			sql += "     ) postLimit on postLimit.\"CustNo\" = BDD.\"CustNo\" ";

		}
		sql += " where BDD.\"EntryDate\" >= " + entryDateFm;
		sql += "   and BDD.\"EntryDate\" <= " + entryDateTo;
		switch (repayBank) {
		case "": // none
			break;
		case "999": // all
			break;
		case "998": // ach
			sql += "   and BDD.\"RepayBank\" <> 700 ";
			break;
		default:
			sql += "   and BDD.\"RepayBank\" = " + repayBank;
			break;
		}

		switch (opItem) {
		case 1: // ach
			sql += "   and BDD.\"RepayBank\" <> 700 ";
			break;
		case 2: // post
			sql += "   and BDD.\"RepayBank\" = 700 ";
			break;
		default:
			break;
		}
		switch (repayType) {
		case 0:
			break;
		case 99:
			break;
		default:
			sql += "   and BDD.\"RepayType\" = " + repayType;
			break;
		}
		switch (functionCode) {
		case 1: // 戶號
			sql += "   and BDD.\"CustNo\" = " + custNo;
			break;
		case 2: // 上限金額
			sql += "   and (postLimit.\"RepayAmt\" >= " + postLimitAmt;
			sql += "        or BDD.\"RepayAmt\" >= " + singleLimitAmt + " )      ";
			break;
		case 3: // 下限金額-
			sql += "   and BDD.\"RepayAmt\" <= " + lowLimitAmt;
			break;
		case 4: // 檢核不正常
			sql += "   and BDD.\"JsonFields\" is not null                     ";
			sql += "   and BDD.\"AcDate\" = 0                     ";
			break;
		case 5: // 扣款金額為0
			sql += "   and BDD.\"RepayAmt\" = 0                     ";
			break;
		case 6: // 媒體檔總金額
			sql += "   and BDD.\"MediaCode\" = 'Y' ";
			break;
		case 9: // 整批
			break;
		}

//		flag 0.Part 1.All
		if (flag == 1) {
			if (functionCode == 1) {
				sql += "   GROUP BY BDD.\"CustNo\" ";
			} else {
				sql += "   GROUP BY BDD.\"EntryDate\" ";
			}
		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		if (flag == 0) {
			// *** 折返控制相關 ***
			// 設定從第幾筆開始抓,需在createNativeQuery後設定
			query.setFirstResult(this.index * this.limit);

			// *** 折返控制相關 ***
			// 設定每次撈幾筆,需在createNativeQuery後設定
			query.setMaxResults(this.limit);
		}

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int flag, int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(flag, titaVo);
	}

	public int getSize() {
		return cnt;
	}
}