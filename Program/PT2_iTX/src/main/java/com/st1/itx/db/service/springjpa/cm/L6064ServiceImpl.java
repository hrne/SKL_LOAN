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

@Service("L6064ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L6064ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// *** 折返控制相關 ***
	private int limit;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}
	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
	
	public List<Map<String, String>> findAll(String iDefType, String iDefCode,String iCode,String iCodeItem,int index, int limit, TitaVo titaVo) throws Exception {
		this.info("iDefType = " + iDefType);
		this.info("iDefCode = " + iDefCode);
		this.info("iCode    = " + iCode);
		this.info("iCodeItem = "+ iCodeItem);
		String sql = "	SELECT ";
		sql += "    C.\"Item\" as CI , ";
		sql += "	A.\"DefCode\" , ";
		sql += "	A.\"Code\" , ";
		sql += "	A.\"Item\" , ";
		sql += "	A.\"DefType\" , ";
		sql += "	A.\"Enable\" , ";
		sql += "	A.\"LastUpdate\" , ";
		sql += "    A.\"LastUpdateEmpNo\" ,";
		sql += "    B.\"Fullname\" , ";
		sql += "    A.\"IsNumeric\" ";
		sql += "	from \"CdCode\" A ";
		sql += "    left join \"CdEmp\" B on A.\"LastUpdateEmpNo\" = B.\"EmployeeNo\" ";
		sql += "    left join (select * from \"CdCode\" where \"DefCode\" = 'CodeType') C ";
		sql += "              on A.\"DefCode\" = C.\"Code\" and C.\"DefCode\" = 'CodeType' ";
		
		
		sql += "	WHERE ";
		if(!"".equals(iDefType)) {
			sql += " A.\"DefType\" = :iDefType ";
		}
		if("".equals(iDefType) && !"".equals(iDefCode)) {
//			sql += " A.\"DefCode\" = :iDefCode ";
			sql += " A.\"DefCode\" Like :iDefCode ";
		}
		if(!"".equals(iDefType) && !"".equals(iDefCode)){
			sql += "AND";
//			sql += " A.\"DefCode\" = :iDefCode ";
			sql += " A.\"DefCode\" Like :iDefCode ";
		}
		
		if("".equals(iDefType) && "".equals(iDefCode) && !"".equals(iCode)) {
//			sql += " A.\"Code\" = :iCode ";
			sql += " A.\"Code\" Like :iCode ";
		}
		if((!"".equals(iDefType) || !"".equals(iDefCode)) && !"".equals(iCode)) {
			sql += "AND";
//			sql += " A.\"Code\" = :iCode ";
			sql += " A.\"Code\" Like :iCode ";
		}
		if("".equals(iDefType) && "".equals(iDefCode) && "".equals(iCode) && !"".equals(iCodeItem)) {
//			sql += " A.\"Item\" = :iCodeItem ";
			sql += " A.\"Item\" Like :iCodeItem ";
		}
		if((!"".equals(iDefType) || !"".equals(iDefCode) || !"".equals(iCode)) && !"".equals(iCodeItem)) {
			sql += "AND";
//			sql += " A.\"CodeItem\" = :iCodeItem ";
			sql += " A.\"Item\" Like :iCodeItem ";
		}
		sql += " ORDER BY  CASE WHEN A.\"DefCode\" = 'CodeType' THEN A.\"Code\" ELSE A.\"DefCode\" END ";
		sql += "         , CASE WHEN A.\"DefCode\" = 'CodeType' THEN ' ' ELSE  A.\"Code\" END ";

		sql += sqlRow;

		this.info("sql=" + sql);
	
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		
		if (!"".equals(iDefType)) {
		query.setParameter("iDefType" , iDefType);
		}
		if (!"".equals(iDefCode)) {
		query.setParameter("iDefCode" , "%" + iDefCode + "%");
		}
		if (!"".equals(iCode)) {
		query.setParameter("iCode" , "%" + iCode+ "%");
		}
		if (!"".equals(iCodeItem)) {
		query.setParameter("iCodeItem" , "%" + iCodeItem+ "%");
		}
		this.info("L6064Service FindData=" + query);
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}

}