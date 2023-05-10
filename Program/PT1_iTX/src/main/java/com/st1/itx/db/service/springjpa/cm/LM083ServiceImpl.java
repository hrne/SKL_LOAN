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

		String sql = "";
		sql += " WITH CF AS ( ";
		sql += "   SELECT CF.\"CustNo\" AS \"CustNo\" ";
		sql += "        , CF.\"FacmNo\" AS \"FacmNo\" ";
		// 2023-05-04 Wei修改 from SKL-佳怡 email : SKL-會議記錄-擔保品轉換相關議題-20230503
		// 第1段. 最接近該額度核准日期，且擔保品鑑價日小於等於核准日期的那筆資料
		// 第2段. 若第1段抓不到資料，才是改為抓鑑價日期最接近核准日期的那筆評估淨值
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
		sql += " , CFSum AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"EvaNetWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM CF ";
		sql += "   WHERE \"Seq\" = 1 "; // 每個擔保品只取一筆
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " )";
		sql += " SELECT CASE ";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN N' ' ";
		sql += "        ELSE c.\"CustName\" ";
		sql += "        END                        AS F0 "; // 戶名
		sql += "      , m.\"CustNo\" ";
		sql += "        || '-' || m.\"FacmNo\"     AS F1 "; // 戶號-額度
		sql += "      , CASE ";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN '房貸，住宅不動產抵押貸款' ";
		sql += "        ELSE '企金，商業及農業抵押貸款' ";
		sql += "        END                        AS F2 "; // 貸款類別
		sql += "      , CASE ";
		sql += "          WHEN NVL(CFSum.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE TRUNC(f.\"LineAmt\" / CFSum.\"EvaNetWorth\" * 100, 2)";
		sql += "        END                        AS F3 "; // 貸款成數
		sql += "      , CASE ";
		sql += "          WHEN m.\"OvduTerm\" >= 1 ";
		sql += "          THEN '是' ";
		sql += "          WHEN m.\"AcctCode\" = '990' ";
		sql += "          THEN '是' ";
		sql += "        ELSE '否' ";
		sql += "        END                        AS F4 "; // 是否已遲付貸款及喪失贖回權貸款
		sql += "      , CASE ";
		sql += "          WHEN m.\"BadDebtBal\" >= 1 ";
		sql += "          THEN '是' ";
		sql += "        ELSE '否' ";
		sql += "        END                        AS F5"; // 是否已經在帳上提呆
		sql += "      , CASE ";
		sql += "          WHEN m.\"ClNo\" > 0 ";
		sql += "          THEN '是' ";
		sql += "        ELSE '否' ";
		sql += "        END                        AS F6"; // 是否有抵押品/擔保品
		sql += "      , nvl(CFSum.\"EvaNetWorth\", 0) AS F7"; // 抵押品/擔保品評估之市價金額
		sql += "      , m.\"PrinBalance\"          AS F8"; // 帳上剩餘金額
		sql += "      , f.\"FirstDrawdownDate\"    AS F9"; // 撥款日期
		sql += "      , f.\"MaturityDate\"         AS F10"; // 到期日
		sql += "      , \"Fn_GetCdCode\"('AcSubBookCode',m.\"AcSubBookCode\")          AS F11"; // 區隔帳冊
		sql += " FROM \"MonthlyFacBal\"  m";
		sql += " LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\"";
		sql += " LEFT JOIN \"FacMain\"  f ON f.\"CustNo\" = m.\"CustNo\"";
		sql += "                         AND f.\"FacmNo\" = m.\"FacmNo\"";
		sql += " LEFT JOIN CFSum ON CFSum.\"CustNo\" = f.\"CustNo\"";
		sql += "                AND CFSum.\"FacmNo\" = f.\"FacmNo\"";
		sql += " WHERE m.\"YearMonth\" = :yearMonth ";
		sql += "   AND CASE ";
		sql += "         WHEN m.\"Status\" IN (6,7) ";
		sql += "         THEN 1 "; // case 1:呆帳戶、部呆戶
		sql += "         WHEN m.\"PrinBalance\" != 0 ";
		sql += "         THEN 2 "; // case 2:餘額不為0
		sql += "       ELSE 0 END != 0 ";
		sql += " ORDER BY m.\"AcSubBookCode\",m.\"CustNo\",m.\"FacmNo\"  ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

}