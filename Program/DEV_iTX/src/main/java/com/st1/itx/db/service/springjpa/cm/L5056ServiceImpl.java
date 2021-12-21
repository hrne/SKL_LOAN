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

@Service("l5056ServiceImpl")
@Repository
public class L5056ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L5056FindData = " + index + "/" + limit);

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim());
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim());
		int iBormNo = Integer.valueOf(titaVo.getParam("BormNo").trim());
		int iWorkYM = Integer.valueOf(titaVo.getParam("WorkYM").trim());
		if (iWorkYM > 0) {
			iWorkYM += 191100;
		}

		String sql = "SELECT A.\"LogNo\",A.\"CustNo\",A.\"FacmNo\",A.\"BormNo\",A.\"SumAmt\",A.\"SumCnt\""
				+ ",DECODE(A.\"WorkMonth\",0,0,A.\"WorkMonth\"-191100) AS YM,A.\"CreateDate\",A.\"CreateEmpNo\",A.\"LastUpdate\",A.\"LastUpdateEmpNo\""
				+ ",B.\"CustName\",C.\"Fullname\" AS \"CreateEmpName\",D.\"Fullname\" AS \"LastUpdateEmpName\" " + "FROM \"PfIntranetAdjust\" A "
				+ "LEFT JOIN \"CustMain\" B ON B.\"CustNo\" = A.\"CustNo\" " + "LEFT JOIN \"CdEmp\" C ON C.\"EmployeeNo\" = A.\"CreateEmpNo\" "
				+ "LEFT JOIN \"CdEmp\" D ON D.\"EmployeeNo\" = A.\"LastUpdateEmpNo\" ";

		// 1:業績日期
		sql += "WHERE \"LogNo\" > 0 ";

		if (iCustNo > 0) {
			sql += "AND A.\"CustNo\"= :CustNo ";
		}

		if (iFacmNo > 0) {
			sql += "AND A.\"FacmNo\"= :FacmNo ";
		}

		if (iBormNo > 0) {
			sql += "AND A.\"BormNo\"= :BormNo ";
		}

		if (iWorkYM > 0) {
			sql += "AND A.\"WorkMonth\"= :WorkMonth ";
		}

		sql += sqlRow;

		this.info("FindL5051 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
//			query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (iCustNo > 0) {
			query.setParameter("CustNo", iCustNo);
		}

		if (iFacmNo > 0) {
			query.setParameter("FacmNo", iFacmNo);
		}

		if (iBormNo > 0) {
			query.setParameter("BormNo", iBormNo);
		}

		if (iWorkYM > 0) {
			query.setParameter("WorkMonth", iWorkYM);
		}

		this.info("L5056Service FindData=" + query);

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