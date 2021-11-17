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

@Service("L2921RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2921ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

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
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 統編
		String iCustId = titaVo.getParam("CustId");
		// 核准編號
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));
		// 經辦
		String iTELLER = titaVo.getParam("Teller");
		// 額度編號
		int iCloseCode = parse.stringToInteger(titaVo.getParam("CloseCode"));
		// 銷號狀態 0:全部 1:已銷 2:未銷
		int YetDate1 = parse.stringToInteger(titaVo.getParam("YetDate1"));
		int YetDate2 = parse.stringToInteger(titaVo.getParam("YetDate2"));
		int CloseDate1 = parse.stringToInteger(titaVo.getParam("CloseDate1"));
		int CloseDate2 = parse.stringToInteger(titaVo.getParam("CloseDate2"));

		if (YetDate1 == 0) {
			YetDate2 = 99999999;
		} else {
			YetDate1 = YetDate1 + 19110000;
			YetDate2 = YetDate2 + 19110000;
		}

		if (CloseDate1 == 0) {
			CloseDate2 = 99999999;
		} else {
			CloseDate1 = CloseDate1 + 19110000;
			CloseDate2 = CloseDate2 + 19110000;
		}		
				
		String sql = " SELECT                 " ; 
			sql +=  "      fm.\"CreditSysNo\"," ; 
			sql +=  "      cm.\"CustNo\"," ; 
			sql +=  "      cm.\"CustName\"," ; 
			sql +=  "      cm.\"CustId\"," ; 
			sql +=  "      fm.\"ApplNo\"," ; 
			sql +=  "      lo.\"FacmNo\"," ; 
			sql +=  "      fm.\"FirstDrawdownDate\"," ; 
			sql +=  "      fm.\"BusinessOfficer\"," ; 
			sql +=  "      ce.\"Fullname\"," ; 
			sql +=  "      lo.\"NotYetCode\"," ; 
			sql +=  "      lo.\"NotYetItem\"," ; 
			sql +=  "      lo.\"YetDate\"," ; 
			sql +=  "      lo.\"CloseDate\"," ; 
			sql +=  "      lo.\"ReMark\"" ; 
			sql +=  "  FROM" ; 
			sql +=  "      \"LoanNotYet\"   lo" ; 
			sql +=  "      LEFT JOIN \"FacMain\"      fm ON fm.\"CustNo\" = lo.\"CustNo\"" ; 
			sql +=  "                                AND fm.\"FacmNo\" = lo.\"FacmNo\"" ; 
			sql +=  "      LEFT JOIN \"CustMain\"     cm ON cm.\"CustNo\" = fm.\"CustNo\"" ; 
			sql +=  "      LEFT JOIN \"CdEmp\"        ce ON ce.\"EmployeeNo\" = fm.\"BusinessOfficer\"";
			
			if(iCaseNo > 0) {
				sql +=  "      WHERE fm.\"CreditSysNo\" = :CaseNo";
			} else if(iCustNo > 0){
				sql +=  "      WHERE fm.\"CustNo\" = :CustNo";
			} else if(!iCustId.isEmpty()) {
				sql +=  "      WHERE cm.\"CustId\" = :CustId";
			} else if(iApplNo > 0) {
				sql +=  "      WHERE fm.\"ApplNo\" = :ApplNo";
			} else if(!iTELLER.isEmpty()) {
				sql +=  "      WHERE fm.\"BusinessOfficer\" = :TELLER";
			} 
			
			
			if(iCloseCode == 1) {
				sql +=  "      AND lo.\"CloseDate\" > 0 ";
			} else if(iCloseCode == 2) {
				sql +=  "      AND lo.\"CloseDate\" = 0 ";
			}
			
			if(YetDate1 != 0) {		
				sql +=  "      AND lo.\"YetDate\" >= :YetDate1";
				sql +=  "      AND lo.\"YetDate\" <= :YetDate2";
			}
			
			if(CloseDate1 != 0) {
				sql +=  "      AND lo.\"CloseDate\" >= :CloseDate1 ";
				sql +=  "      AND lo.\"CloseDate\" <= :CloseDate2 ";
			}
			
			sql +=  "      ORDER BY lo.\"FacmNo\" , lo.\"NotYetCode\" , cm.\"CustNo\"";
			
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		
		if(iCaseNo > 0) {
			query.setParameter("CaseNo", iCaseNo);
		} else if(iCustNo > 0){
			query.setParameter("CustNo", iCustNo);
		} else if(!iCustId.isEmpty()) {
			query.setParameter("CustId", iCustId);
		} else if(iApplNo > 0) {
			query.setParameter("ApplNo", iApplNo);
		} else if(!iTELLER.isEmpty()) {
			query.setParameter("TELLER", iTELLER);
		} 
		if(YetDate1 != 0) {	
		  query.setParameter("YetDate1", YetDate1);
		  query.setParameter("YetDate2", YetDate2);
		}
		
		if(CloseDate1 != 0) {
			query.setParameter("CloseDate1", CloseDate1);
			query.setParameter("CloseDate2", CloseDate2);
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
	
	public int getSize() {
		return cnt;
	}
	
	
}