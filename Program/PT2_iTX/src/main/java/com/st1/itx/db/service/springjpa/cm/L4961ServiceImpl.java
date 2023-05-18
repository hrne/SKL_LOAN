package com.st1.itx.db.service.springjpa.cm;

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

@Service("L4961ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4961ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4961.findAll");
		int intSearchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int intInsuYearMonth = parse.stringToInteger(titaVo.getParam("InsuYearMonth")) + 191100;
		int intInsuYearMonthEnd = parse.stringToInteger(titaVo.getParam("InsuYearMonthEnd")) + 191100;
		int intReportYearMonth = parse.stringToInteger(titaVo.getParam("ReportYearMonth"));
		int intSearchOption = parse.stringToInteger(titaVo.getParam("SearchOption"));
		int intRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode"));

		this.info("intSearchFlag = " + intSearchFlag);
		this.info("intInsuYearMonth = " + intInsuYearMonth);
		this.info("intReportYearMonth = " + intReportYearMonth);
		this.info("intSearchOption = " + intSearchOption);
		this.info("intRepayCode = " + intRepayCode);

		String sql = " ";
		sql += " select                                                    ";
		sql += "  i.\"InsuYearMonth\"   as F0                              ";
		sql += " ,i.\"PrevInsuNo\"      as F1                              ";
		sql += " ,i.\"NowInsuNo\"       as F2                              ";
		sql += " ,i.\"CustNo\"          as F3                              ";
		sql += " ,i.\"FacmNo\"          as F4                              ";
		sql += " ,c.\"CustName\"        as F5                              ";
		sql += " ,i.\"TotInsuPrem\"     as F6                              ";
		sql += " ,i.\"RepayCode\"       as F7                              ";
		sql += " ,i.\"StatusCode\"      as F8                              ";
		sql += " ,i.\"AcDate\"          as F9                              ";
		sql += " ,i.\"FireInsuCovrg\"   as F10                             ";
		sql += " ,i.\"FireInsuPrem\"    as F11                             ";
		sql += " ,i.\"EthqInsuCovrg\"   as F12                             ";
		sql += " ,i.\"EthqInsuPrem\"    as F13                             ";
		sql += " ,i.\"InsuReceiptDate\" as F14                             ";
		sql += " from \"InsuRenew\" i                                      ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"   ";
		// 查詢方式: 1.火險到期年月 2.報表年月 3.未銷全部
		if (intSearchFlag == 1) {
			sql += " where i.\"InsuYearMonth\" >= " + intInsuYearMonth;
			sql += "   and i.\"InsuYearMonth\" <= " + intInsuYearMonthEnd;
		} else if (intSearchFlag == 2) {
			sql += "   where i.\"InsuYearMonth\" >= " + intInsuYearMonth;
			sql += "   and i.\"InsuYearMonth\" <= " + intInsuYearMonthEnd;
		} else if (intSearchFlag == 3) {
			sql += " where i.\"AcDate\" = 0                                ";
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" in (0, 1, 2)                   ";
		}
		// SearchOption 0:正常未繳 1:正常已繳 2:借支 3:轉催 4:催收未繳 5:結案 7:續保 8:自保 9:全部
		// status 0:正常 1:借支 2:催收 4:結案
		switch (intSearchOption) {
		case 0: // 0:正常未繳 0:正常
			if (intSearchFlag == 2) {
				sql += " and (substr(LPAD(i.\"AcDate\" - 19110000, 7, 0), 1, 5) > " + intReportYearMonth
						+ " or i.\"AcDate\" = 0) ";
			}
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" = 0                            ";
			if (intSearchFlag != 2) {
				sql += "   and i.\"AcDate\" = 0                              ";
			}
			break;
		case 1: // 1:正常已繳 0:正常
			if (intSearchFlag == 2) {
				sql += " and substr(LPAD(i.\"AcDate\" - 19110000, 7, 0), 1, 5) <= " + intReportYearMonth
						+ "and i.\"AcDate\" > 0";
			}
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" = 0                            ";
			if (intSearchFlag != 2) {
				sql += "   and i.\"AcDate\" > 0                                ";
			}
			break;
		case 2: // 2:借支 1:借支
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" = 1                            ";
			break;
		case 3: // 3:轉催 2:催收
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" = 2                            ";
			break;
		case 4: // 4:催收未繳 2:催收 未入帳
			if (intSearchFlag == 2) {
				sql += " and substr(LPAD(i.\"AcDate\" - 19110000, 7, 0), 1, 5) <= " + intReportYearMonth
						+ "and i.\"AcDate\" = 0";
			}
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" = 2                            ";
			if (intSearchFlag != 2) {
				sql += "   and i.\"AcDate\" = 0                                ";
			}
			break;
		case 5: // 5:結案 4:結案
			sql += "   and i.\"RenewCode\" = 2                             ";
			sql += "   and i.\"StatusCode\" = 4                            ";
			break;
		case 7: // 7:續保
			sql += "   and i.\"RenewCode\" = 2                             ";
			break;
		case 8: // 8:自保
			sql += "   and i.\"RenewCode\" = 1                             ";
			break;
		}
		// 繳款方式
		if (intRepayCode != 99) {
			sql += "   and i.\"RepayCode\" = " + intRepayCode;
		}
		sql += "  order by i.\"InsuYearMonth\", i.\"CustNo\", i.\"FacmNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}