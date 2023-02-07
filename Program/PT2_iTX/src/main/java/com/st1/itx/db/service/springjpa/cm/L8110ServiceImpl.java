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
		//must set to true
		return this.convertToMap(query,true);
	}

	public List<Map<String, String>> insertAll(int iDate, int iDate3Y, int iDate5Y, int iAmt, TitaVo titaVo) throws Exception {
		this.info("L8110ServiceImpl.insertAll start ");
		this.info("iDate=" + iDate);
		this.info("iDate3Y=" + iDate3Y);
		this.info("iDate5Y=" + iDate5Y);
		this.info("iAmt=" + iAmt);
		this.info("TlrNo=" + titaVo.getTlrNo());

		String sql = "　";
		sql += "INSERT INTO \"AmlCustList\" ";
		sql += "WITH txData AS ( ";
		sql += "    SELECT \"CustNo\" ";
		sql += "         , \"FacmNo\" ";
		sql += "         , SUM(CASE ";
		sql += "                 WHEN \"EntryDate\" >= :iDate3Y ";
		sql += "		         THEN \"TxAmt\" ";
		sql += "               ELSE 0 END ) AS repayAmtIn3Years ";
		sql += "         , SUM(CASE ";
		sql += "                 WHEN \"EntryDate\" >= :iDate5Y ";
		sql += "		         THEN \"TxAmt\" ";
		sql += "               ELSE 0 END ) AS repayAmtIn5Years ";
		sql += "    FROM \"LoanBorTx\" ";
		sql += "    WHERE \"TitaTxCd\" = 'L3200' ";
		sql += "      AND \"TitaHCode\" = '0' ";
		sql += "      AND \"ExtraRepay\" > 0 "; // 還本
		sql += "      AND \"RepayCode\" in  (1 , 11) "; // 1:匯款 11:大額匯款手工增入入帳
		sql += "    GROUP BY \"CustNo\" ";
		sql += "           , \"FacmNo\" ";
		sql += ") ";
		sql += "SELECT S1.\"CustNo\" AS \"CustNo\" "; // 戶號
		sql += "      , CASE ";
		sql += "         WHEN S1.repayAmtIn3Years >= :iAmt ";
		sql += "	     THEN 'Y' ";
		sql += " 	   ELSE 'N' ";
		sql += "	   END           AS \"Note1\" "; // 註記1 三年內還本的匯款金額>=2500萬則帶"Y"，不符合者則帶"N"
		sql += "     , CASE ";
		sql += "         WHEN S1.repayAmtIn5Years >= :iAmt ";
		sql += "		 THEN 'Y' ";
		sql += "	   ELSE 'N' ";
		sql += "	   END           AS \"Note2\" "; // 註記2 五年內還本的匯款金額>=2500萬則帶"Y"，不符合者則帶"N"
		sql += "     , CASE ";
		sql += "         WHEN S1.CNT > 0 ";
		sql += "	     THEN 'Y' ";
		sql += "	   ELSE 'N' ";
		sql += "	   END           AS \"Note3\" "; // 註記3
		sql += "     , SYSTIMESTAMP  AS \"CreateDate\" "; // 建檔日期時間
		sql += "     , :TlrNo        AS \"CreateEmpNo\" "; // 建檔人員
		sql += "     , SYSTIMESTAMP  AS \"LastUpdate\" "; // 最後更新日期時間
		sql += "     , :TlrNo        AS \"LastUpdateEmpNo\" "; // 最後更新人員
		sql += "FROM ( ";
		// 2022-10-18 Wei修改 FROM QC:2055 珮琪:此資料KEY值為LA$APLP.CASNUM3案件編號,當無案件編號時才帶戶號
		sql += "    SELECT CASE WHEN NVL(F.\"CreditSysNo\",0) = 0 THEN L.\"CustNo\" ELSE F.\"CreditSysNo\" END ";
		sql += "                                AS \"CustNo\" "; // 戶號
		sql += "		 , SUM(CASE "; 
		sql += "                 WHEN F.\"BreachFlag\" = 'N' "; // 同一戶號下且未約定「限制清償期間」的客戶才計算
		sql += "		         THEN T.repayAmtIn3Years ";
		sql += "		       ELSE 0 ";
		sql += "		       END)             AS repayAmtIn3Years "; // 三年內還本的匯款金額 
		sql += "		 , SUM(CASE ";
		sql += "                 WHEN F.\"BreachFlag\" = 'N' "; // 同一戶號下且未約定「限制清償期間」的客戶才計算
		sql += "		         THEN T.repayAmtIn5Years ";
		sql += "		       ELSE 0 ";
		sql += "		       END)             AS repayAmtIn5Years "; // 五年內還本的匯款金額 
		sql += "		 , SUM(CASE ";
		sql += "                 WHEN F.\"RecycleDeadline\" >= :iDate ";
		sql += "		         THEN 1 ";
		sql += "		       ELSE 0 ";
		sql += "		       END)             AS CNT "; // 循環動用期限>=資料產生日(筆數)
		sql += "    FROM ( ";
		sql += "        SELECT \"CustNo\" ";
		sql += "		     , \"FacmNo\" ";
		sql += "		FROM \"LoanBorMain\" ";
		sql += "		WHERE \"Status\" IN (0,2,4,7) "; // 00: 正常戶 02: 催收戶 04:逾期戶 07:部分轉呆戶
		sql += "		GROUP BY \"CustNo\",\"FacmNo\" ";
		sql += "    ) L ";
		sql += "    LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\" ";
		sql += "		                   AND F.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "    LEFT JOIN txData T ON T.\"CustNo\" = L.\"CustNo\" ";
		sql += "                      AND T.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "    GROUP BY CASE WHEN NVL(F.\"CreditSysNo\",0) = 0 THEN L.\"CustNo\" ELSE F.\"CreditSysNo\" END ";
		sql += ") S1 ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("TlrNo", titaVo.getTlrNo());
		query.setParameter("iAmt", iAmt);
		query.setParameter("iDate3Y", iDate3Y);
		query.setParameter("iDate5Y", iDate5Y);
		query.setParameter("iDate", iDate);
		return this.convertToMap(query, true);
	}
}