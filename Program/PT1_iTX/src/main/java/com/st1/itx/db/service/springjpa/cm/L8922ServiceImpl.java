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

@Service
@Repository
public class L8922ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Autowired
	Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

//	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";


	public List<Map<String, String>> queryresult(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L8922ServiceImpl.queryresult");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		int iFactor = this.parse.stringToInteger(titaVo.getParam("Factor"));
		String iEntryDateStart = titaVo.getParam("AcDateStart");
		String iEntryDateEnd = titaVo.getParam("AcDateEnd");
		int afEntryDateStart = Integer.parseInt(iEntryDateStart) + 19110000;
		int afEntryDateEnd = Integer.parseInt(iEntryDateEnd) + 19110000;

		this.info("iFactor=" + iFactor );

		String sql = "";
		sql += " SELECT                               			 	   					\n";
		sql += " M.\"Factor\" 				    		AS \"Factor\"					\n"; // 樣態
		sql += ",M.\"EntryDate\" 						AS \"EntryDate\"				\n"; // 入帳日
		sql += ",M.\"CustNo\"		            		AS \"CustNo\"				 	\n"; // 戶號
		sql += ",C.\"CustName\" 						AS \"CustName\"					\n"; // 戶名
		sql += ",M.\"TotalAmt\" 						AS \"TotalAmt\"					\n"; // 累積金額
		sql += ",M.\"TotalCnt\" 						AS \"TotalCnt\"					\n"; // 次數
		sql += ",E.\"Fullname\" 		        		AS \"Fullname\"					\n"; // 經辦
		sql += ",M.\"Rational\" 						AS \"Rational\"					\n"; // 合理性
		sql += ",to_char(M.\"LastUpdate\", 'yyyymmdd') 	AS \"LastUpdate\"				\n"; // 異動時間
		sql += ",M.\"EmpNoDesc\" 						AS \"EmpNoDesc\"				\n"; // 經辦說明
		sql += ",M.\"ManagerCheck\" 					AS \"ManagerCheck\"  			\n"; // 主管覆核
		sql += ",M.\"ManagerDate\" 			    		AS \"ManagerDate\"  		    \n"; // 主管同意日期
		sql += ",M.\"ManagerDesc\" 						AS \"ManagerDesc\"				\n"; // 主管說明

		sql += "FROM \"MlaundryDetail\" M										\n";
		sql += "LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"		\n";
		sql += "LEFT JOIN \"CdEmp\" E ON M.\"CreateEmpNo\" = E.\"EmployeeNo\"	\n";
		sql += " WHERE  1=1     \n";
		sql += " AND  M.\"EntryDate\" >= :entryStart AND M.\"EntryDate\" <= :entrydEnd   \n";
		sql += " AND  CASE WHEN NVL(M.\"ManagerCheck\", 'N') != 'Y' THEN 1     \n     ";
		sql += "           WHEN \"Fn_CountBusinessDays\"(M.\"EntryDate\",M.\"ManagerDate\") > 4   THEN 1     \n     ";
		sql += "           ELSE 0 END=1  \n";
		if (iFactor > 0) {
			sql += " AND  M.\"Factor\" = :iFactor       \n";
		}

		sql += "ORDER BY M.\"EntryDate\" DESC , M.\"CustNo\" , M.\"Factor\" ";

//		sql += sqlRow;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);
		query.setParameter("entryStart", afEntryDateStart);
		query.setParameter("entrydEnd", afEntryDateEnd);

		if (iFactor > 0) {
			query.setParameter("iFactor", iFactor);
		}


		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(0);
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