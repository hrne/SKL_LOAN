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
public class L9722ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(String yearMonth, TitaVo titaVo) throws Exception {
		this.info("l9722.findAll ");

		this.info("yearMonth = " + yearMonth);
		String sql = "";
		sql += " WITH CF AS ( ";
		sql += "   SELECT CF.\"CustNo\" AS \"CustNo\" ";
		sql += "        , CF.\"FacmNo\" AS \"FacmNo\" ";
		sql += "        , ROW_NUMBER() ";
		sql += "          OVER ( ";
		sql += "            PARTITION BY CF.\"CustNo\" ";
		sql += "                       , CF.\"FacmNo\" ";
		sql += "                       , CF.\"ClCode1\" ";
		sql += "                       , CF.\"ClCode2\" ";
		sql += "                       , CF.\"ClNo\" ";
		sql += "            ORDER BY NVL(CE_early.\"EvaDate\",0) DESC "; // 第1段. 最接近該額度核准日期，且擔保品鑑價日小於等於核准日期的那筆資料
		sql += "                   , NVL(CE_later.\"EvaDate\",0) "; // 第2段. 若第1段抓不到資料，才是改為抓鑑價日期最接近核准日期的那筆評估淨值
		sql += "          )                               AS \"Seq\" ";
		sql += "        , NVL(CE_early.\"EvaNetWorth\",NVL(CE_later.\"EvaNetWorth\",0)) ";
		sql += "                                          AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\" CF ";
		sql += "   LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                            AND FAC.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += "   LEFT JOIN \"FacCaseAppl\" CAS ON CAS.\"ApplNo\" = CF.\"ApproveNo\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_early ON CE_early.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_early.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_early.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_early.\"EvaDate\" <= CAS.\"ApproveDate\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_later ON CE_later.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_later.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_later.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_later.\"EvaDate\" > CAS.\"ApproveDate\" ";
		sql += "                               AND NVL(CE_early.\"EvaDate\",0) = 0 "; // 若第1段串不到,才串第2段
		sql += " ) ";
		sql += " , \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"EvaNetWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM CF ";
		sql += "   WHERE \"Seq\" = 1 "; // 每個擔保品只取一筆
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " )";
		sql += "      SELECT m.\"CustNo\"               AS F0"; // 戶號
		sql += "            ,m.\"FacmNo\"               AS F1"; // 額度
		sql += "            ,c.\"EntCode\"              AS F2"; // 企金別
		sql += "            ,m.\"DepartmentCode\"       AS F3"; // 通路
		sql += "     		, CASE ";
		sql += "                WHEN NVL(CS.\"EvaNetWorth\", 0) = 0";
		sql += "                THEN 0 ";
		sql += "                ELSE TRUNC(f.\"LineAmt\" / CS.\"EvaNetWorth\" * 100, 2)";
		sql += "             END                        AS F4"; // 貸款成數
		sql += "            ,m.\"OvduTerm\"             AS F5"; // 逾期期數
		sql += "            ,m.\"Status\"               AS F6"; // 戶況
		sql += "            ,m.\"ClCode1\" ";
		sql += "             || '-' ";
		sql += "             || LPAD(m.\"ClCode2\",2,'0') ";
		sql += "             || '-' ";
		sql += "             || LPAD(m.\"ClNo\",7,'0')  AS F7"; // 押品別
		sql += "            , NVL(CS.\"EvaNetWorth\", 0) AS F8"; // 評估淨值
		sql += "            ,m.\"PrinBalance\"          AS F9"; // 放款餘額
		sql += "            ,m.\"BadDebtBal\"           AS F10"; // 轉銷呆帳金額
		sql += "            ,f.\"FirstDrawdownDate\"    AS F11"; // 撥款日期
		sql += "            ,f.\"MaturityDate\"         AS F12"; // 到期日
		sql += "      FROM \"MonthlyFacBal\"  m";
		sql += "      LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\"";
		sql += "      LEFT JOIN \"FacMain\"  f ON f.\"CustNo\" = m.\"CustNo\"";
		sql += "                              AND f.\"FacmNo\" = m.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = m.\"CustNo\" ";
		sql += "                            AND CS.\"FacmNo\" = m.\"FacmNo\" ";
		sql += "      WHERE m.\"Status\" IN (0,2,4,6,7)";
		sql += "        AND m.\"YearMonth\" = :yearMonth";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

}