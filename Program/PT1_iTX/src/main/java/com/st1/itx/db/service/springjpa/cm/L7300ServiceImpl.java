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
public class L7300ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int yearMonth, TitaVo titaVo) {
		this.info("L7300ServiceImpl findAll ");

		if (yearMonth <= 19110000) {
			yearMonth += 19110000;
		}
		
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
		sql += " SELECT ROW_NUMBER() ";
		sql += "        OVER ( ";
		sql += "          ORDER BY m.\"CustNo\" ";
		sql += "                 , m.\"FacmNo\" ";
		sql += "        )                          AS \"TranSeq\" "; // 傳輸順序
		sql += "      , f.\"MaturityDate\"         AS \"MaturityDate\" "; // 到期日
		sql += "      , CASE";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN '1' ";
		sql += "        ELSE '2' ";
		sql += "        END                        AS \"LoanType\" "; // 貸款類別
		sql += "      , CASE ";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN m.\"CustNo\" || '-' || m.\"FacmNo\" ";
		sql += "        ELSE TO_CHAR(c.\"CustName\") ";
		sql += "        END                        AS \"Counterparty\" "; // 交易對手
		sql += "      , m.\"StoreRate\"            AS \"LoanInt\""; // 放款利率
		sql += "      , CASE ";
		sql += "          WHEN NVL(CFSum.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(f.\"LineAmt\" / NVL(CFSum.\"EvaNetWorth\") * 100, 2)";
		sql += "        END                        AS \"LtvRatio\" "; // 貸款成數
		sql += "      , m.\"AcSubBookCode\"        AS \"SubCompanyCode\" "; // 區隔帳冊別(資金來源)
		sql += "      , m.\"PrinBalance\"          AS \"MrktValue\" "; // 市價
		sql += "      , m.\"PrinBalance\"          AS \"BookValue\" "; // 期初帳面金額
		sql += " FROM \"MonthlyFacBal\"  m ";
		sql += " LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" f ON f.\"CustNo\" = m.\"CustNo\" ";
		sql += "                        AND f.\"FacmNo\" = m.\"FacmNo\" ";
		sql += " LEFT JOIN CFSum ON CFSum.\"CustNo\" = f.\"CustNo\"";
		sql += "                AND CFSum.\"FacmNo\" = f.\"FacmNo\"";
		sql += " WHERE m.\"Status\" IN (0,2,4,6,7) ";
		sql += "   AND m.\"YearMonth\" = :yearMonth ";
		sql += " ORDER BY \"TranSeq\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findNow(TitaVo titaVo) {
		this.info("L7300ServiceImpl findNow ");

		String sql = "";
		sql += " WITH m AS ( ";
		sql += "   SELECT LBM.\"CustNo\" ";
		sql += "        , LBM.\"FacmNo\" ";
		sql += "        , MAX(AR.\"AcSubBookCode\") AS \"AcSubBookCode\" ";
		sql += "        , MIN(LBM.\"StoreRate\")    AS \"StoreRate\" ";
		sql += "        , SUM(LBM.\"LoanBal\")      AS \"LoanBal\" ";
		sql += "   FROM \"LoanBorMain\" LBM ";
		sql += "   LEFT JOIN \"FacMain\" f ON f.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                          AND f.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "   LEFT JOIN \"AcReceivable\" ar on ar.\"AcctCode\" = CASE ";
		sql += "                                                        WHEN LBM.\"Status\" IN (2,6,7) ";
		sql += "                                                        THEN '990' ";
		sql += "                                                      ELSE f.\"AcctCode\" END ";
		sql += "                                and ar.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                                and ar.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "                                and ar.\"RvNo\" = LPAD(LBM.\"BormNo\",3,'0') ";
		sql += "   WHERE LBM.\"Status\" IN (0,2,4,6,7) ";
		sql += "   GROUP BY LBM.\"CustNo\" ";
		sql += "          , LBM.\"FacmNo\" ";
		sql += " )";
		sql += " , CF AS ( ";
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
		sql += " SELECT ROW_NUMBER() ";
		sql += "        OVER ( ";
		sql += "          ORDER BY m.\"CustNo\" ";
		sql += "                 , m.\"FacmNo\" ";
		sql += "        )                          AS \"TranSeq\" "; // 傳輸筆數序號
		sql += "      , f.\"MaturityDate\"         AS \"MaturityDate\" "; // 到期日
		sql += "      , CASE";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN '1' ";
		sql += "        ELSE '2' ";
		sql += "        END                        AS \"LoanType\" "; // 貸款類別
		sql += "      , CASE ";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN m.\"CustNo\" || '-' || m.\"FacmNo\" ";
		sql += "        ELSE TO_CHAR(c.\"CustName\") ";
		sql += "        END                        AS \"Counterparty\" "; // 交易對手
		sql += "      , m.\"StoreRate\"            AS \"LoanInt\""; // 放款利率
		sql += "      , CASE ";
		sql += "          WHEN NVL(CFSum.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(f.\"LineAmt\" / NVL(CFSum.\"EvaNetWorth\",0) * 100, 2)";
		sql += "        END                        AS \"LtvRatio\" "; // 貸款成數
		sql += "      , m.\"AcSubBookCode\"        AS \"SubCompanyCode\" "; // 區隔帳冊別(資金來源)
		sql += "      , m.\"LoanBal\"              AS \"MrktValue\" "; // 市價
		sql += "      , m.\"LoanBal\"              AS \"BookValue\" "; // 期初帳面金額
		sql += " FROM m ";
		sql += " LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" f ON f.\"CustNo\" = m.\"CustNo\" ";
		sql += "                        AND f.\"FacmNo\" = m.\"FacmNo\" ";
		sql += " LEFT JOIN CFSum ON CFSum.\"CustNo\" = f.\"CustNo\"";
		sql += "                AND CFSum.\"FacmNo\" = f.\"FacmNo\"";
		sql += " ORDER BY \"TranSeq\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}