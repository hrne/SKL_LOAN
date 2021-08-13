package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(L8110ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> deleteAll(TitaVo titaVo) throws Exception {

		String sql = "　";
		sql += "	DELETE                                                    \r\n";
		sql += "	FROM \"AmlCustList\"                                      \r\n";
		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query.getResultList());
	}

	public List<Map<String, String>> insertAll(int iDate, int iDate3Y, int iDate5Y, int iAmt, TitaVo titaVo)
			throws Exception {

		String sql = "　";
		sql += "INSERT INTO \"AmlCustList\"                               \r\n";
		sql += "SELECT                                                    \r\n";
		sql += " \"CustNo\"                                               \r\n"; // 戶號
		sql += ",CASE WHEN AMT1 >" + iAmt + "                            \r\n";
		sql += "	          THEN 'Y'                                       \r\n";
		sql += " 	 ELSE 'N'                                            \r\n";
		sql += "	END                                                      \r\n"; // 註記1
		sql += ",CASE WHEN AMT2 >" + iAmt + "                            \r\n";
		sql += "		      THEN 'Y'                                       \r\n";
		sql += "		 ELSE 'N'                                            \r\n";
		sql += "	END                                                      \r\n"; // 註記2
		sql += ",CASE WHEN CNT3 > 0                                       \r\n";
		sql += "	          THEN 'Y'                                       \r\n";
		sql += "	     ELSE 'N'                                            \r\n";
		sql += "	END                                                      \r\n"; // 註記3
		sql += ",TIMESTAMP                                                \r\n"; // 建檔日期時間
		sql += "," + titaVo.getTlrNo() + "                                \r\n"; // 建檔人員
		sql += ",TIMESTAMP                                                \r\n"; // 最後更新日期時間
		sql += "," + titaVo.getTlrNo() + "                                \r\n"; // 最後更新人員
		sql += "	FROM                                                     \r\n";
		sql += "	 (SELECT                                                  \r\n";
		sql += "		 L.\"CustNo\"         AS  \"CustNo\"                  \r\n"; // 戶號
		sql += "		,SUM(CASE WHEN T.\"EntryDate\" <= " + iDate3Y + "     \r\n";
		sql += "		                THEN T.\"TxAmt\"                      \r\n";
		sql += "		           ELSE 0                                     \r\n";
		sql += "		     END)             AS  AMT1                        \r\n"; // 三年內還本的匯款金額
		sql += "		,SUM(CASE WHEN T.\"EntryDate\" <= " + iDate5Y + "    \r\n";
		sql += "		                THEN T.\"TxAmt\"                      \r\n";
		sql += "		           ELSE 0                                     \r\n";
		sql += "		     END)             AS  AMT2                        \r\n"; // 五年內還本的匯款金額
		sql += "		,SUM(CASE WHEN F.\"RecycleDeadline\" <= " + iDate + " \r\n";
		sql += "		                THEN  1                               \r\n";
		sql += "		           ELSE 0                                     \r\n";
		sql += "		     END)             AS  CNT3                        \r\n"; // 循環動用期限>=資料產生日(筆數)
		sql += "		FROM ( SELECT                                         \r\n";
		sql += "		        \"CustNo\"                                    \r\n";
		sql += "		       ,\"FacmNo\"                                    \r\n";
		sql += "		       FROM \"LoanBorMain\"                           \r\n";
		sql += "		       WHERE \"Status\" IN (0,2,4,7)                  \r\n"; // 00: 正常戶 02: 催收戶 03: 結案戶 04:逾期戶
																						// 05:催收結案戶 06:呆帳戶 07:部分轉呆戶
																						// 08:債權轉讓戶 09:呆帳結案戶
		sql += "		       GROUP BY \"CustNo\",\"FacmNo\"                 \r\n";
		sql += "		      ) L                                             \r\n";
		sql += "	  LEFT JOIN \"FacMain\" F                                 \r\n";
		sql += "		       ON F.\"CustNo\" = L.\"CustNo\"                 \r\n";
		sql += "		      AND F.\"FacmNo\" = L.\"FacmNo\"                 \r\n";
		sql += "	  LEFT JOIN \"FacProd\" P                                 \r\n";
		sql += "		       ON P.\"ProdNo\" = F.\"ProdNo\"                 \r\n";
		sql += "      LEFT JOIN \"LoanBorTx\" T                               \r\n";
		sql += "		       ON T.\"CustNo\" = L.\"CustNo\"                 \r\n";
		sql += "		      AND T.\"FacmNo\" = L.\"FacmNo\"                 \r\n";
		sql += "		      AND T.\"TitaTxCd\" = 'L3200'                    \r\n";
		sql += "		      AND T.\"ExtraRepay\" > 0                        \r\n";
		sql += "		      AND JSON_VALUE(T.\"OtherFields\",'$.RepayCode') = '1'  \r\n";
		sql += "		      AND P.\"BreachFlag\" = 'N'                      \r\n";
		sql += "       GROUP BY L.\"CustNo\"                                  \r\n";
		sql += "   )                                                          \r\n";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query.getResultList());
	}
}