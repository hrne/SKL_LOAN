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

@Service("l8110ServiceImpl")
@Repository
/* AML每日有效客戶名單 */
public class L8110ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> deleteAll(TitaVo titaVo) throws Exception {

		String sql = "　";
		sql += "DELETE FROM \"AmlCustList\"";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		// must set to true
		return this.convertToMap(query, true);
	}

	public List<Map<String, String>> insertAll(int iDate, int iDate3Y, int iDate5Y, int iAmt, TitaVo titaVo) throws Exception {

		String sql = "　";
		sql += "INSERT INTO \"AmlCustList\"                                ";
		sql += "SELECT                                                     ";
		sql += " \"CustNo\"                                                "; // 戶號
		sql += ",CASE WHEN AMT1 >" + iAmt + "                             ";
		sql += "	          THEN 'Y'                                        ";
		sql += " 	 ELSE 'N'                                             ";
		sql += "	END                                                       "; // 註記1
		sql += ",CASE WHEN AMT2 >" + iAmt + "                             ";
		sql += "		      THEN 'Y'                                        ";
		sql += "		 ELSE 'N'                                             ";
		sql += "	END                                                       "; // 註記2
		sql += ",CASE WHEN CNT3 > 0                                        ";
		sql += "	          THEN 'Y'                                        ";
		sql += "	     ELSE 'N'                                             ";
		sql += "	END                                                       "; // 註記3
		sql += ",SYSTIMESTAMP                                                 "; // 建檔日期時間
		sql += ",:TlrNo                                 "; // 建檔人員
		sql += ",SYSTIMESTAMP                                                "; // 最後更新日期時間
		sql += ",:TlrNo                                 "; // 最後更新人員
		sql += "	FROM                                                      ";
		sql += "	 (SELECT                                                   ";
		sql += "		 L.\"CustNo\"         AS  \"CustNo\"                   "; // 戶號
		sql += "		,SUM(CASE WHEN T.\"EntryDate\" <= " + iDate3Y + "      ";
		sql += "		                THEN T.\"TxAmt\"                       ";
		sql += "		           ELSE 0                                      ";
		sql += "		     END)             AS  AMT1                         "; // 三年內還本的匯款金額
		sql += "		,SUM(CASE WHEN T.\"EntryDate\" <= " + iDate5Y + "     ";
		sql += "		                THEN T.\"TxAmt\"                       ";
		sql += "		           ELSE 0                                      ";
		sql += "		     END)             AS  AMT2                         "; // 五年內還本的匯款金額
		sql += "		,SUM(CASE WHEN F.\"RecycleDeadline\" <= " + iDate + "  ";
		sql += "		                THEN  1                                ";
		sql += "		           ELSE 0                                      ";
		sql += "		     END)             AS  CNT3                         "; // 循環動用期限>=資料產生日(筆數)
		sql += "		FROM ( SELECT                                          ";
		sql += "		        \"CustNo\"                                     ";
		sql += "		       ,\"FacmNo\"                                     ";
		sql += "		       FROM \"LoanBorMain\"                            ";
		sql += "		       WHERE \"Status\" IN (0,2,4,7)                   "; // 00: 正常戶 02: 催收戶 03: 結案戶 04:逾期戶
																					// 05:催收結案戶 06:呆帳戶 07:部分轉呆戶
																					// 08:債權轉讓戶 09:呆帳結案戶
		sql += "		       GROUP BY \"CustNo\",\"FacmNo\"                  ";
		sql += "		      ) L                                              ";
		sql += "	  LEFT JOIN \"FacMain\" F                                  ";
		sql += "		       ON F.\"CustNo\" = L.\"CustNo\"                  ";
		sql += "		      AND F.\"FacmNo\" = L.\"FacmNo\"                  ";
		sql += "	  LEFT JOIN \"FacProd\" P                                  ";
		sql += "		       ON P.\"ProdNo\" = F.\"ProdNo\"                  ";
		sql += "      LEFT JOIN \"LoanBorTx\" T                                ";
		sql += "		       ON T.\"CustNo\" = L.\"CustNo\"                  ";
		sql += "		      AND T.\"FacmNo\" = L.\"FacmNo\"                  ";
		sql += "		      AND T.\"TitaTxCd\" = 'L3200'                     ";
		sql += "		      AND T.\"ExtraRepay\" > 0                         ";
		sql += "		      AND JSON_VALUE(T.\"OtherFields\",'$.RepayCode') = '1'   ";
		sql += "		      AND P.\"BreachFlag\" = 'N'                       ";
		sql += "       GROUP BY L.\"CustNo\"                                   ";
		sql += "   )                                                           ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("TlrNo", titaVo.getTlrNo());
		return this.convertToMap(query, true);
	}
}