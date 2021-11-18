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
			sql += " ,b.\"BormNo\"                   AS \"BormNo\"          ";
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
			sql += " ,b.\"BormNo\"                   AS \"BormNo\"          ";
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
			sql += " SELECT ";
			sql += "    \"CustNo\"";
			sql += "   ,\"EntryDate\"";
			sql += "   ,MAX( CASE  WHEN \"ReturnCode\" IN('00') THEN 0  ELSE 1  END ) AS ERRFG";
			sql += " FROM \"BankDeductDtl\" ";
			sql += " WHERE \"MediaCode\" = 'Y'                      ";
			sql += "   AND NVL(\"ReturnCode\", '  ') NOT IN ('  ' ) ";
			sql += "   AND \"RepayType\" = 1";
			sql += " GROUP BY \"CustNo\" ,\"EntryDate\"   ";
			sql += " ORDER BY \"CustNo\" ,\"EntryDate\"   ";
			sql += "     ";
			sql += " ), S1 AS ( ";
			sql += "  SELECT  ";
			sql += "    \"CustNo\" ";
			sql += "   ,\"EntryDate\" ";
			sql += "   ,ERRFG  ";
			sql += "   ,ROW_NUMBER() OVER (ORDER BY \"CustNo\",\"EntryDate\") AS \"Seq\"  ";
			sql += "  FROM S0   ";
			sql += "  )   ";
			sql += "  , S2 AS (  ";
			sql += "  SELECT     ";
			sql += "    \"CustNo\"   ";
			sql += "   ,\"EntryDate\"  ";
			sql += "   ,ERRFG  ";
			sql += "   ,\"Seq\"  ";
			sql += "   ,\"Seq\" - ROW_NUMBER() OVER (ORDER BY \"CustNo\",\"EntryDate\") AS \"GroupNum\"  ";
			sql += "  FROM S1  ";
			sql += "  WHERE \"ERRFG\" = 1  ";
			sql += "  )   ";
			sql += "  , \"MoreThan3\" AS (    ";
			sql += "     SELECT               ";
			sql += "      \"GroupNum\"        ";
			sql += "     ,COUNT(*)  AS  ERRCNT";
			sql += "     FROM S2 ";
			sql += "     GROUP BY \"GroupNum\" ";
			sql += "     HAVING COUNT(*) >= " + failTimes; // 連續失敗次數
			sql += " )                      ";
			sql += " select                                                 ";
			sql += "  b.\"EntryDate\" - 19110000     AS \"EntryDate\"       ";
			sql += " ,b.\"CustNo\"                   AS \"CustNo\"          ";
			sql += " ,b.\"FacmNo\"                   AS \"FacmNo\"          ";
			sql += " ,b.\"BormNo\"                   AS \"BormNo\"          ";
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
			sql += " ,f.ERRCNT                       AS \"ErrorCnt\"        ";
			sql += " ,r.\"FailNoticeDate\"           AS \"FailNoticeDate\"  ";
			sql += " ,ROW_NUMBER() OVER (Partition By b.\"CustNo\", b.\"FacmNo\", b.\"RepayType\" ORDER BY b.\"PayIntDate\") AS \"RowNumber\"  ";
			sql += " from          ";
			sql += " ( SELECT          ";
			sql += "    S2.\"CustNo\"  ";
			sql += "   ,MAX(S2.\"EntryDate\")        AS \"EntryDate\"       ";
			sql += "   ,MAX(ERRCNT)                  AS ERRCNT              ";
			sql += "   FROM \"MoreThan3\" M   ";
			sql += "   LEFT JOIN S2 ON S2.\"GroupNum\" = M.\"GroupNum\"   ";
			sql += "   GROUP by S2.\"CustNo\"                             ";
			sql += " ) f                                                  ";
			sql += "LEFT JOIN \"BankDeductDtl\" b                      ";
			sql += "       ON b.\"CustNo\" = f.\"CustNo\"              ";
			sql += "      AND b.\"EntryDate\" = f.\"EntryDate\"	       ";
			sql += "LEFT JOIN ( SELECT                                 ";
			sql += "             \"CustNo\"                            ";
			sql += "            ,\"FacmNo\"                            ";
			sql += "            ,MAX(to_number(to_char(\"CreateDate\", 'YYYYMMDD'))) AS  \"FailNoticeDate\" ";
			sql += "            FROM \"TxToDoDetailReserve\"           ";
			sql += "            GROUP BY \"CustNo\", \"FacmNo\"        ";
			sql += "      ) r ON r.\"CustNo\" = b.\"CustNo\"           ";
			sql += "         AND r.\"FacmNo\" = b.\"FacmNo\"	       ";
			sql += "left join \"CustMain\" c on c.\"CustNo\" = b.\"CustNo\"  ";
			sql += "where b.\"MediaCode\" = 'Y'                        ";
			sql += "   and NVL(b.\"ReturnCode\",'  ') not in ('  ','00') ";
			sql += "   and b.\"RepayType\" in (1)                         ";
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