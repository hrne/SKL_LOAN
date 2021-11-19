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

@Service("L4454ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L4454A.findAll");

		int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		int entryDate = parse.stringToInteger(titaVo.getParam("EntryDate").trim()) + 19110000;
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo").trim());
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo").trim());
		String repayBank = titaVo.getParam("RepayBank"); // 銀行別
		int entryDateS = parse.stringToInteger(titaVo.getParam("EntryDateS").trim()) + 19110000;
		int failTimes = parse.stringToInteger(titaVo.getParam("FailTimes")); // 連續失敗次數
		int sendDateS = parse.stringToInteger(titaVo.getParam("SendDateS")); // 上次寄發日

		this.info("FunctionCode = " + functionCode);
		this.info("EntryDate = " + entryDate);
		this.info("CustNo = " + custNo);
		this.info("FacmNo = " + facmNo);
		this.info("RepayBank = " + repayBank);
		this.info("EntryDateS = " + entryDateS);
		this.info("FailTimes = " + failTimes);
		this.info("SendDateS = " + sendDateS);

		String sql = "";
		switch (functionCode) {
		case 1: // 個別列印
			sql += " select                                                 ";
			sql += "  b.\"EntryDate\" - 19110000     AS \"EntryDate\"       ";
			sql += " ,b.\"CustNo\"                   AS \"CustNo\"          ";
			sql += " ,b.\"FacmNo\"                   AS \"FacmNo\"          ";
			sql += " ,b.\"RepayType\"                AS \"RepayType\"       ";
			sql += " ,b.\"UnpaidAmt\"                AS \"UnpaidAmt\"       ";
			sql += " ,b.\"TempAmt\"                  AS \"TempAmt\"         ";
			sql += " ,b.\"RepayAmt\"                 AS \"RepayAmt\"        ";
			sql += " ,b.\"PrevIntDate\" - 19110000   AS \"PrevIntDate\"     ";
			sql += " ,b.\"PayIntDate\"  - 19110000   AS \"PayIntDate\"      ";
			sql += " ,b.\"IntStartDate\" - 19110000  AS \"IntStartDate\"    ";
			sql += " ,b.\"IntEndDate\"  - 19110000   AS \"IntEndDate\"      ";
			sql += " ,b.\"RepayBank\"                AS \"RepayBank\"       ";
			sql += " ,b.\"RepayAcctNo\"              AS \"RepayAcctNo\"     ";
			sql += " ,b.\"JsonFields\"               AS \"JsonFields\"      ";
			sql += " ,b.\"ReturnCode\"               AS \"ReturnCode\"      ";
			sql += " ,c.\"CustId\"                   AS \"CustId\"          ";
			sql += " ,c.\"CustName\"                 AS \"CustName\"        ";
			sql += " ,2                              AS \"RepayCode\" ";
			sql += " ,NVL(d.\"RepayType\",0)         AS \"FireFeeSuccess\"  "; // 5-火險費成功、期款失敗
			sql += " ,ROW_NUMBER() OVER (Partition By b.\"CustNo\", b.\"FacmNo\", b.\"RepayType\" ORDER BY b.\"PayIntDate\") AS \"RowNumber\"  ";
			sql += " from \"BankDeductDtl\" b                               ";
			sql += " left join \"CustMain\" c on c.\"CustNo\" = b.\"CustNo\"  ";
			sql += " left join \"BankDeductDtl\" d                          ";
			sql += "       on d.\"CustNo\" = b.\"CustNo\"                   ";
			sql += "      and d.\"FacmNo\" = b.\"FacmNo\"	                ";
			sql += "      and d.\"EntryDate\" = b.\"EntryDate\"	            ";
			sql += "      and b.\"RepayType\" =  1                          ";
			sql += "      and d.\"RepayType\" = 5	                        ";
			sql += "      and NVL(b.\"ReturnCode\",'  ') in ('00')          ";
			sql += " where b.\"MediaCode\" = 'Y'                            ";
			sql += "   and NVL(b.\"ReturnCode\",'  ') not in ('  ','00')    ";
			sql += "   and b.\"RepayType\" in (1,5)                         ";
			sql += "   and b.\"EntryDate\" = " + entryDate;
			if (custNo > 0) {
				sql += "   and b.\"CustNo\" = " + custNo;
			}
			if (facmNo > 0) {
				sql += "   and b.\"FacmNo\" = " + facmNo;
			}
			break;
		case 2: // 整批列印
			sql += " select                                                 ";
			sql += "  b.\"EntryDate\" - 19110000     AS \"EntryDate\"       ";
			sql += " ,b.\"CustNo\"                   AS \"CustNo\"          ";
			sql += " ,b.\"FacmNo\"                   AS \"FacmNo\"          ";
			sql += " ,b.\"RepayType\"                AS \"RepayType\"       ";
			sql += " ,b.\"UnpaidAmt\"                AS \"UnpaidAmt\"       ";
			sql += " ,b.\"TempAmt\"                  AS \"TempAmt\"         ";
			sql += " ,b.\"RepayAmt\"                 AS \"RepayAmt\"        ";
			sql += " ,b.\"PrevIntDate\" - 19110000   AS \"PrevIntDate\"     ";
			sql += " ,b.\"PayIntDate\"  - 19110000   AS \"PayIntDate\"      ";
			sql += " ,b.\"IntStartDate\" - 19110000  AS \"IntStartDate\"    ";
			sql += " ,b.\"IntEndDate\"  - 19110000   AS \"IntEndDate\"      ";
			sql += " ,b.\"RepayBank\"                AS \"RepayBank\"       ";
			sql += " ,b.\"RepayAcctNo\"              AS \"RepayAcctNo\"     ";
			sql += " ,b.\"JsonFields\"               AS \"JsonFields\"      ";
			sql += " ,b.\"ReturnCode\"               AS \"ReturnCode\"      ";
			sql += " ,c.\"CustId\"                   AS \"CustId\"          ";
			sql += " ,c.\"CustName\"                 AS \"CustName\"        ";
			sql += " ,2                              AS \"RepayCode\" ";
			sql += " ,NVL(d.\"RepayType\",0)         AS \"FireFeeSuccess\"  "; // 5-火險費成功、期款失敗
			sql += " ,ROW_NUMBER() OVER (Partition By b.\"CustNo\", b.\"FacmNo\", b.\"RepayType\" ORDER BY b.\"PayIntDate\") AS \"RowNumber\"  ";
			sql += " from \"BankDeductDtl\" b                               ";
			sql += " left join \"CustMain\" c on c.\"CustNo\" = b.\"CustNo\"";
			sql += " left join \"BankDeductDtl\" d                          ";
			sql += "       on d.\"CustNo\" = b.\"CustNo\"                   ";
			sql += "      and d.\"FacmNo\" = b.\"FacmNo\"	                ";
			sql += "      and d.\"EntryDate\" = b.\"EntryDate\"	            ";
			sql += "      and b.\"RepayType\" =  1                          ";
			sql += "      and d.\"RepayType\" = 5	                        ";
			sql += "      and NVL(b.\"ReturnCode\",'  ') in ('00')          ";
			sql += " where b.\"MediaCode\" = 'Y'                            ";
			sql += "   and NVL(b.\"ReturnCode\",'  ') not in ('  ','00')    ";
			sql += "   and b.\"EntryDate\" = " + entryDate;
			sql += "   and b.\"RepayType\" in (1,5)                         ";
			switch (repayBank) {
			case "": // none
				break;
			case "999": // all
				break;
			case "998": // ach
				sql += "   and b.\"RepayBank\" <> 700 ";
				break;
			default:
				sql += "   and b.\"RepayBank\" = " + repayBank;
				break;
			}
			break;
		case 3: // 連續扣款失敗明細＆通知
			sql += " WITH S0 AS (";
			sql += " SELECT                                              ";
			sql += "    l.\"CustNo\"                                     ";
			sql += "   ,l.\"FacmNo\"                                     ";
			sql += "   ,l.\"BormNo\"                                     ";
			sql += "   ,MAX(b.\"EntryDate\")          AS \"EntryDate\"   ";
			sql += "   ,MAX(b.\"PrevIntDate\")        AS \"PrevIntDate\" ";
			sql += "   ,COUNT(*)                      AS \"FailCnt\"     ";
			sql += " FROM \"LoanBorMain\" l                              ";
			sql += " LEFT JOIN \"BankDeductDtl\" b                       ";
			sql += "       ON b.\"CustNo\" = l.\"CustNo\"                ";
			sql += "      AND b.\"FacmNo\" = l.\"FacmNo\"                ";
			sql += "      AND b.\"PrevIntDate\" = decode(l.\"PrevPayIntDate\",0,l.\"DrawdownDate\",l.\"PrevPayIntDate\")";
			sql += "      AND b.\"RepayType\" = 1                        ";
			sql += "      AND b.\"MediaCode\" = 'Y'                      ";
			sql += "      AND NVL(b.\"ReturnCode\", '  ') NOT IN ('  ' ) ";
			sql += " WHERE l.\"Status\" = 0                              ";
			sql += "   AND NVL(b.\"CustNo\", 0) > 0                      ";
			sql += " GROUP BY l.\"CustNo\" ,l.\"FacmNo\", l.\"BormNo\"   ";
			sql += "     HAVING COUNT(*) >= " + failTimes; // 連續失敗次數
			sql += " )                                                   ";
			sql += " SELECT                                                 ";
			sql += "  b.\"EntryDate\" - 19110000     AS \"EntryDate\"       ";
			sql += " ,b.\"CustNo\"                   AS \"CustNo\"          ";
			sql += " ,b.\"FacmNo\"                   AS \"FacmNo\"          ";
			sql += " ,b.\"RepayType\"                AS \"RepayType\"       ";
			sql += " ,b.\"UnpaidAmt\"                AS \"UnpaidAmt\"       ";
			sql += " ,b.\"TempAmt\"                  AS \"TempAmt\"         ";
			sql += " ,b.\"RepayAmt\"                 AS \"RepayAmt\"        ";
			sql += " ,b.\"PrevIntDate\" - 19110000   AS \"PrevIntDate\"     ";
			sql += " ,b.\"PayIntDate\"  - 19110000   AS \"PayIntDate\"      ";
			sql += " ,b.\"IntStartDate\" - 19110000  AS \"IntStartDate\"    ";
			sql += " ,b.\"IntEndDate\"  - 19110000   AS \"IntEndDate\"      ";
			sql += " ,b.\"RepayBank\"                AS \"RepayBank\"       ";
			sql += " ,b.\"RepayAcctNo\"              AS \"RepayAcctNo\"     ";
			sql += " ,b.\"JsonFields\"               AS \"JsonFields\"      ";
			sql += " ,b.\"ReturnCode\"               AS \"ReturnCode\"      ";
			sql += " ,c.\"CustId\"                   AS \"CustId\"          ";
			sql += " ,c.\"CustName\"                 AS \"CustName\"        ";
			sql += " ,2                              AS \"RepayCode\"       ";
			sql += " ,f.\"FailCnt\"                  AS \"FailCnt\"         ";
			sql += " ,r.\"FailNoticeDate\"           AS \"FailNoticeDate\"  ";
			sql += " ,ROW_NUMBER() OVER (Partition By b.\"CustNo\", b.\"FacmNo\", b.\"RepayType\" ORDER BY b.\"PayIntDate\") AS \"RowNumber\"  ";
			sql += " FROM                                                   ";
			sql += " ( SELECT                                               ";
			sql += "    \"CustNo\"                                          ";
			sql += "   ,\"FacmNo\"                                          ";
			sql += "   ,MAX(\"EntryDate\")           AS \"EntryDate\"       ";
			sql += "   ,MAX(\"PrevIntDate\")         AS \"PrevIntDate\"     ";
			sql += "   ,MAX(\"FailCnt\")             AS \"FailCnt\"         ";
			sql += "   FROM S0                                              ";
			sql += " GROUP BY \"CustNo\" ,\"FacmNo\"                        ";
			sql += " ) f                                                    ";
			sql += " LEFT JOIN \"BankDeductDtl\" b                          ";
			sql += "        ON b.\"CustNo\" = f.\"CustNo\"                  ";
			sql += "       AND b.\"FacmNo\" = f.\"FacmNo\"                  ";
			sql += "       AND b.\"EntryDate\" =  f.\"EntryDate\"           ";
			sql += "       AND b.\"PrevIntDate\" = f.\"PrevIntDate\"	    ";
			sql += "       AND b.\"RepayType\" in (1)                       ";
			sql += " LEFT JOIN ( SELECT                                     ";
			sql += "             \"CustNo\"                                 ";
			sql += "            ,\"FacmNo\"                                 ";
			sql += "            ,MAX(to_number(to_char(\"CreateDate\", 'YYYYMMDD'))) AS  \"FailNoticeDate\" ";
			sql += "            FROM \"TxToDoDetailReserve\"                ";
			sql += "            GROUP BY \"CustNo\", \"FacmNo\"             ";
			sql += "      ) r ON r.\"CustNo\" = b.\"CustNo\"                ";
			sql += "         AND r.\"FacmNo\" = b.\"FacmNo\"	            ";
			sql += " LEFT JOIN \"CustMain\" c on c.\"CustNo\" = f.\"CustNo\"";
			sql += " WHERE b.\"MediaCode\" = 'Y'                            ";
			sql += "  AND NVL(b.\"ReturnCode\",'  ') not in ('  ','00')     ";
			sql += "  AND b.\"RepayType\" = 1                               ";
			if (sendDateS > 0) {
				sql += "   and r.\"FailNoticeDate\" < " + sendDateS + 19110000; // 上次寄發日
			}
			break;
		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
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