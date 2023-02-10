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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class LM083ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(String yearMonth, TitaVo titaVo) {
		this.info("LM083ServiceImpl findAll ");

		this.info("yearMonth = " + yearMonth);

		String sql = "SELECT CASE ";
		sql += "               WHEN c.\"EntCode\" = '0' ";
		sql += "               THEN N' ' ";
		sql += "             ELSE c.\"CustName\" ";
		sql += "             END                        AS F0 "; // 戶名
		sql += "           , m.\"CustNo\" ";
		sql += "             || '-' || m.\"FacmNo\"     AS F1 "; // 戶號-額度
		sql += "           , CASE ";
		sql += "               WHEN c.\"EntCode\" = '0' ";
		sql += "               THEN '房貸，住宅不動產抵押貸款' ";
		sql += "             ELSE '企金，商業及農業抵押貸款' ";
		sql += "             END                        AS F2 "; // 貸款類別
		sql += "           , CASE ";
		sql += "               WHEN NVL(cl.\"EvaNetWorth\", 0) = 0";
		sql += "               THEN 0 ";
		sql += "             ELSE TRUNC(f.\"LineAmt\" / cl.\"EvaNetWorth\" * 100, 2)";
		sql += "             END                        AS F3 "; // 貸款成數
		sql += "           , CASE ";
		sql += "               WHEN m.\"OvduTerm\" >= 1 ";
		sql += "               THEN '是' ";
		sql += "               WHEN m.\"AcctCode\" = '990' ";
		sql += "               THEN '是' ";
		sql += "             ELSE '否' ";
		sql += "             END                        AS F4 "; // 是否已遲付貸款及喪失贖回權貸款
		sql += "           , CASE ";
		sql += "               WHEN m.\"BadDebtBal\" >= 1 ";
		sql += "               THEN '是' ";
		sql += "             ELSE '否' ";
		sql += "             END                        AS F5"; // 是否已經在帳上提呆
		sql += "           , CASE ";
		sql += "               WHEN m.\"ClNo\" > 0 ";
		sql += "               THEN '是' ";
		sql += "             ELSE '否' ";
		sql += "             END                        AS F6"; // 是否有抵押品/擔保品
		sql += "           , nvl(cl.\"EvaNetWorth\", 0) AS F7"; // 抵押品/擔保品評估之市價金額
		sql += "           , m.\"PrinBalance\"          AS F8"; // 帳上剩餘金額
		sql += "           , f.\"FirstDrawdownDate\"    AS F9"; // 撥款日期
		sql += "           , f.\"MaturityDate\"         AS F10"; // 到期日
		sql += "           , \"Fn_GetCdCode\"('AcSubBookCode',m.\"AcSubBookCode\")          AS F11"; // 區隔帳冊
		sql += "      FROM \"MonthlyFacBal\"  m";
		sql += "      LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\"";
		sql += "      LEFT JOIN \"FacMain\"  f ON f.\"CustNo\" = m.\"CustNo\"";
		sql += "                              AND f.\"FacmNo\" = m.\"FacmNo\"";
		sql += "      LEFT JOIN \"ClImm\"   cl ON cl.\"ClCode1\" = m.\"ClCode1\"";
		sql += "                              AND cl.\"ClCode2\" = m.\"ClCode2\"";
		sql += "                              AND cl.\"ClNo\" = m.\"ClNo\"";
//		sql += "      WHERE m.\"Status\" IN (0,2,4,6,7) ";
		sql += "      WHERE m.\"PrinBalance\" != 0 ";
		sql += "        AND m.\"YearMonth\" = :yearMonth";
		sql += "        ORDER BY m.\"AcSubBookCode\",m.\"CustNo\",m.\"FacmNo\"  ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

}