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

@Service("L5982ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5982ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
		
		this.index = titaVo.getReturnIndex();
		this.limit = 200; // 45 * 200 = 9000

		// 取得輸入資料
		int iYearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth"));
		if(iYearMonth!=0) {
			iYearMonth = iYearMonth+191100;
		}
		int iCustNo=this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iCondition = Integer.parseInt(titaVo.getParam("Condition"));
		int iUsageCode = Integer.parseInt(titaVo.getParam("UsageCode"));
		String sql = "";
		sql += " select                                   		 	  \n";
		sql += " Y.\"YearMonth\"     			as F0                 \n";
		sql += ",Y.\"CustNo\"       			as F1                 \n";
		sql += ",Y.\"FacmNo\" 					as F2				  \n";
		sql += ",Y.\"UsageCode\" 				as F3	              \n";
		sql += ",Y.\"AcctCode\"	    			as F4                 \n";
		sql += ",Y.\"RepayCode\"	    		as F5                 \n";
		sql += ",Y.\"LoanAmt\"	    			as F6                 \n";
		sql += ",Y.\"LoanBal\"	    			as F7                 \n";
		sql += ",Y.\"FirstDrawdownDate\"	    as F8                 \n";
		sql += ",Y.\"MaturityDate\"	   			as F9                 \n";
		sql += ",Y.\"YearlyInt\"	   			as F10                \n";
		sql += ",Y.\"HouseBuyDate\"	  			as F11                \n";
		sql += "from \"YearlyHouseLoanInt\" Y   					  \n";
		sql += "left join \"CustMain\" C							  \n";
		sql += "on C.\"CustNo\" = Y.\"CustNo\"					 	  \n";
		sql += "left join \"FacMain\" F							 	  \n";
		sql += "on F.\"CustNo\" = Y.\"CustNo\"					  	  \n";
		sql += "and F.\"FacmNo\" = Y.\"FacmNo\"					  	  \n";
		
		if(iYearMonth!=0 && iCustNo!=0) {
			sql += "where Y.\"YearMonth\" = :yearMonth and  Y.\"CustNo\" = :custNo";
		} else if(iYearMonth!=0) {
			sql += "where Y.\"YearMonth\" = :yearMonth";
		} else if(iCustNo!=0) {
			sql += "where Y.\"CustNo\" = :custNo";
		} else {
			sql += "where Y.\"YearMonth\" >0 and Y.\"YearMonth\" <999912";
		}
		if(iUsageCode>0) {
			sql += " and Y.\"UsageCode\" = '02' or Y.\"UsageCode\" = '2'    \n";
		}
		if(iCondition==1) {//借戶姓名空白
			sql += " and C.\"CustName\" = ''     				  \n";
		} else if(iCondition==2) {//統一編號空白 
			sql += " and C.\"CustId\" = ''     				  \n";
		} else if(iCondition==3) {//貸款帳號空白
			sql += " and Y.\"CustNo\" = 0 OR  Y.\"FacmNo\" = 0	  \n";
		} else if(iCondition==4) {//初貸金額為0
			sql += " and Y.\"LoanAmt\" = 0 		                  \n";
		} else if(iCondition==5) {//初貸金額>核准和度
			sql += " and Y.\"LoanAmt\" > F.\"LineAmt\"			  \n";
		} else if(iCondition==6) {//初貸款金額<放款餘額
			sql += " and Y.\"LoanAmt\" < Y.\"LoanBal\"			  \n";
		} else if(iCondition==7) {//貸款起日空白
			sql += " and Y.\"FirstDrawdownDate\" = 0			  \n";
		} else if(iCondition==8) {//貸款訖日空白
			sql += " and Y.\"MaturityDate\" = 0			  		  \n";
		} else if(iCondition==9) {//繳息所屬年月空白
			sql += " and Y.\"YearMonth\" = 0			  		  \n";
		} else if(iCondition==10) {//繳息金額為 0	
			sql += " and Y.\"YearlyInt\" = 0			  		  \n";
		} else if(iCondition==11) {//科子細目代號暨說明空白
			sql += " and Y.\"AcctCode\"= ' '			  		  \n";
		}
		sql += " order by Y.\"YearMonth\", Y.\"CustNo\",Y.\"FacmNo\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		if(iYearMonth!=0) {
			query.setParameter("yearMonth", iYearMonth);
		}
		if(iCustNo!=0) {
			query.setParameter("custNo", iCustNo);
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

		return this.convertToMap(query);
	}



	public int getSize() {
		return cnt;
	}

}