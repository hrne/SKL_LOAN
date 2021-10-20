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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4925ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4925ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4925.findAll");
		
		
		
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iRepayCode = parse.stringToInteger(titaVo.get("RepayCode"));
		
		int iEntryDateFrom = parse.stringToInteger(titaVo.getParam("EntryDateFrom")) + 19110000;
		int iEntryDateTo = parse.stringToInteger(titaVo.getParam("EntryDateTo")) + 19110000;
		
		
		String iProcStsCode = titaVo.get("ProcStsCode");
		
		String sql = "";
		
			sql += "  SELECT                            ";  
			sql += "    bd.\"AcDate\",                  "; 
			sql += "    bd.\"BatchNo\",                 ";
			sql += "    bd.\"DetailSeq\",               ";
			sql += "    bd.\"RepayCode\",               "; 
			sql += "    bd.\"EntryDate\",               "; 
			sql += "    bd.\"CustNo\",                  ";
			sql += "    bd.\"FacmNo\",                  ";
			sql += "    bd.\"RepayType\",               ";
			sql += "    bd.\"ReconCode\",               "; 
			sql += "    bd.\"RepayAmt\",                ";
			sql += "    bd.\"AcctAmt\",                 ";
			sql += "    bd.\"DisacctAmt\",              ";
			sql += "    bd.\"ProcStsCode\",             ";
			sql += "    bd.\"ProcCode\",                ";
			sql += "    bd.\"TitaTlrNo\",               ";
			sql += "    bd.\"TitaTxtNo\",               ";
			sql += "    bd.\"ProcNote\"                 ";
			sql += "  FROM                              ";
			sql += "    \"BatxDetail\"              bd  ";
			sql += "    LEFT JOIN  \"BatxHead\"     bh  ";
			sql += "         ON bh.\"AcDate\" = bd.\"AcDate\"             ";
			sql += "         AND bh.\"BatchNo\" = bd.\"BatchNo\"          ";
			sql += "  WHERE                             ";
			
			if(iRepayCode == 0 && iCustNo == 0) {
				sql += "    bd.\"AcDate\" >= :EntryDateFrom       ";
				sql += "    AND bd.\"AcDate\" <= :EntryDateTo     "; 
				sql += "    AND bd.\"CustNo\" >= 0          ";
				sql += "    AND bd.\"CustNo\" <= 9999999    ";
				sql += "    AND bd.\"RepayCode\" >= 0       ";
				sql += "    AND bd.\"RepayCode\" <= 99      ";
			} else if (iRepayCode == 0) {
				sql += "    bd.\"AcDate\" >= :EntryDateFrom       ";
				sql += "    AND bd.\"AcDate\" <= :EntryDateTo     "; 
				sql += "    AND bd.\"CustNo\" >= :CustNo   ";
				sql += "    AND bd.\"CustNo\" <= :CustNo   ";
				sql += "    AND bd.\"RepayCode\" >= 0       ";
				sql += "    AND bd.\"RepayCode\" <= 99      ";
			} else if (iCustNo == 0) {
				sql += "    bd.\"AcDate\" >= :EntryDateFrom       ";
				sql += "    AND bd.\"AcDate\" <= :EntryDateTo     "; 
				sql += "    AND bd.\"CustNo\" >= 0          ";
				sql += "    AND bd.\"CustNo\" <= 9999999    ";
				sql += "    AND bd.\"RepayCode\" >= :RepayCode    ";
				sql += "    AND bd.\"RepayCode\" <= :RepayCode    ";
			} else {
				sql += "    bd.\"AcDate\" >= :EntryDateFrom       ";
				sql += "    AND bd.\"AcDate\" <= :EntryDateTo     "; 
				sql += "    AND bd.\"CustNo\" >= :CustNo   ";
				sql += "    AND bd.\"CustNo\" <= :CustNo   ";
				sql += "    AND bd.\"RepayCode\" >= :RepayCode    ";
				sql += "    AND bd.\"RepayCode\" <= :RepayCode    ";
			}
					
			
			switch (iProcStsCode) {
			case "A":
				sql += "   and (bd.\"ProcStsCode\" in ('0','2','3','4') ";
				sql += "   and bh.\"BatxExeCode\" <> 8 )";
				sql += "   or  bd.\"ProcStsCode\" in ('5','6','7') ";
				break;
			case "R":
				sql += "   and bd.\"ProcStsCode\" in ('0','2','3','4') ";
				sql += "   and bh.\"BatxExeCode\" <> 8 ";
				break;
			case "S":
				sql += "   and bd.\"ProcStsCode\" in ('5','6','7') ";
				break;
			default:
				sql += "   and bd.\"ProcStsCode\" = '" + iProcStsCode + "'";
				break;
			}
			

		sql += "ORDER BY                            ";
		sql += "    \"CustNo\" ASC,                 ";
		sql += "    \"FacmNo\" ASC,                 ";
		sql += "    \"RepayCode\" ASC,              ";
		sql += "    \"DetailSeq\" ASC               ";
		


		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		
		if(iRepayCode == 0 && iCustNo == 0) {
			query.setParameter("EntryDateFrom", iEntryDateFrom);
			query.setParameter("EntryDateTo", iEntryDateTo);			
		} else if (iRepayCode == 0) {
			query.setParameter("EntryDateFrom", iEntryDateFrom);
			query.setParameter("EntryDateTo", iEntryDateTo);			
			query.setParameter("CustNo", iCustNo);
		} else if (iCustNo == 0) {
			query.setParameter("EntryDateFrom", iEntryDateFrom);
			query.setParameter("EntryDateTo", iEntryDateTo);			
			query.setParameter("RepayCode", iRepayCode);
		} else {
			query.setParameter("EntryDateFrom", iEntryDateFrom);
			query.setParameter("EntryDateTo", iEntryDateTo);			
			query.setParameter("CustNo", iCustNo);
			query.setParameter("RepayCode", iRepayCode);
		}
		
		
		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}