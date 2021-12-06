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
import com.st1.itx.util.parse.Parse;
import com.st1.itx.eum.ContentName;

@Service
@Repository
/* 放款交易內容檔二年內繳款明細查詢 */
public class L560AServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	public Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	public List<Map<String, String>> findLoanBorTx(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L560AServiceImpl.findLoanBorTx ");

		// *** 折返控制相關 ***
		this.limit = limit;

		String sql = " ";

		sql += " SELECT \"BormNo\"                    "; // -- F0 撥款序號
		sql += "      , \"EntryDate\"                 "; // -- F1 入帳日期
		sql += "      , \"LoanBal\"                   "; // -- F2放款餘額
		sql += "      , \"IntStartDate\"              "; // -- F3 計息起日
		sql += "      , \"IntEndDate\"                "; // -- F4 計息迄日
		sql += "      , \"Rate\"                      "; // -- F5 利率
		sql += "      , \"Desc\"                      "; // -- F6 摘要
		sql += "      , \"Principal\" + \"Interest\" + \"DelayInt\" + \"BreachAmt\" + \"CloseBreachAmt\" + \"TempAmt\"  "
				+ " AS \"TxAmt\"  ";// --F7 交易金額
		sql += "      , \"Principal\" + \"Interest\" + \"DelayInt\" + \"BreachAmt\" + \"CloseBreachAmt\"  "
				+ " AS \"TxAmt2\"  ";// --F8 作帳金額
		sql += "      , \"Principal\"                 "; // -- F9 實收本金
		sql += "      , \"Interest\"                  "; // -- F10 實收利息
		sql += "      , \"DelayInt\" + \"BreachAmt\" + \"CloseBreachAmt\"    " + " AS \"BreachAmt\"  ";// --F11 違約金
		sql += "      , CASE WHEN \"TempAmt\"  < 0  THEN ABS(\"TempAmt\") ELSE 0  END  AS  TempAmt1 "; // -- F12 暫收借
		sql += "      , CASE WHEN \"TempAmt\"  > 0  THEN \"TempAmt\" ELSE 0  END  AS  TempAmt2 "; // -- F13 暫收貸
		sql += "      , \"UnpaidInterest\" + \"UnpaidPrincipal\"     " + " AS \"UnpaidAmt\"  "; // --F14 短繳

		sql += " FROM \"LoanBorTx\" ";
		sql += " WHERE \"CustNo\" = :CustNo ";
		sql += " AND \"FacmNo\" = :FacmNo";
		sql += "  AND \"TitaHCode\" = 0";
		sql += "  AND \"TitaTxCd\" = 'L3200' ";
		sql += " AND \"EntryDate\" BETWEEN :startDate AND :endDate ";
		sql += "   ORDER BY  \"EntryDate\" DESC , \"AcDate\" DESC ";
		sql += sqlRow;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("CustNo", parse.stringToInteger(titaVo.get("OOCustNo").trim()) );
		query.setParameter("FacmNo", parse.stringToInteger(titaVo.get("OOFacmNo").trim()));
		query.setParameter("startDate", parse.stringToInteger(titaVo.getCalDy()) + 19090000  );
		query.setParameter("endDate", parse.stringToInteger(titaVo.getCalDy()) + 19110000);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}


}