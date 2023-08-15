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
import com.st1.itx.db.transaction.BaseEntityManager;;

@Service("l5055ServiceImpl")
@Repository
public class L5055ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L5055FindData");

		int WorkMonth = Integer.valueOf(titaVo.getParam("WorkYM").trim());
		if (WorkMonth > 0) {
			WorkMonth += 191100;
		}

		String sql = "SELECT ";
		sql += "A.\"CustNo\"    				AS \"CustNo\" ,";
		sql += "A.\"FacmNo\"    				AS \"FacmNo\"   ,";
		sql += "A.\"BormNo\"    				AS \"BormNo\" ,";
		sql += "C.\"CustName\"    				AS \"CustName\" ,";
		sql += "A.\"DrawdownDate\" 			 	AS \"DrawdownDate\",";
		sql += "A.\"ProdCode\"    				AS \"ProdCode\" ,";
		sql += "A.\"PieceCode\"    				AS \"PieceCode\" ,";
		sql += "A.\"CntingCode\"    			AS \"CntingCode\" ,";
		sql += "A.\"DrawdownAmt\"    			AS \"DrawdownAmt\" ,";
		sql += "A.\"DeptCode\"    				AS \"DeptCode\" ,";
		sql += "A.\"DistCode\"    				AS \"DistCode\" ,";
		sql += "A.\"UnitCode\"   				AS \"UnitCode\" ,";
		sql += "E2.\"UnitItem\"     			AS \"ItDeptName\",";
		sql += "E3.\"UnitItem\"     			AS \"ItDistName\",";
		sql += "E4.\"UnitItem\"      			AS \"ItUnitName\",";
		sql += "A.\"Introducer\"    			AS \"Introducer\" ,";
		sql += "F2.\"Fullname\"     			AS \"IntroducerName\",";
		sql += "A.\"PerfEqAmt\"    				AS \"PerfEqAmt\" ,";
		sql += "A.\"PerfReward\"    			AS \"PerfReward\" ,";
		sql += "A.\"MediaDate\"    				AS \"MediaDate\" ,";
		sql += "A.\"AdjRange\"    				AS \"AdjRange\"  ";
		sql += "FROM \"PfItDetail\" A ";
		sql += "LEFT JOIN \"PfBsDetail\" B ON B.\"CustNo\"=A.\"CustNo\" AND B.\"FacmNo\"=A.\"FacmNo\" AND B.\"BormNo\"=A.\"BormNo\" AND B.\"PerfDate\"=A.\"PerfDate\" AND B.\"RepayType\"=A.\"RepayType\" AND B.\"PieceCode\"=A.\"PieceCode\" AND B.\"DrawdownAmt\">0 ";
		sql += "LEFT JOIN \"CustMain\" C ON C.\"CustNo\"=A.\"CustNo\" ";
		sql += "LEFT JOIN \"PfItDetailAdjust\" D ON D.\"CustNo\"=A.\"CustNo\" AND D.\"FacmNo\"=A.\"FacmNo\" AND D.\"BormNo\"=A.\"BormNo\" AND A.\"RepayType\"=0 ";
		sql += "LEFT JOIN \"CdBcm\" E2 ON E2.\"UnitCode\"=A.\"DeptCode\" ";
		sql += "LEFT JOIN \"CdBcm\" E3 ON E3.\"UnitCode\"=A.\"DistCode\" ";
		sql += "LEFT JOIN \"CdBcm\" E4 ON E4.\"UnitCode\"=A.\"UnitCode\" ";
		sql += "LEFT JOIN \"CdEmp\" F2 ON F2.\"EmployeeNo\"=A.\"Introducer\" ";
		sql += "LEFT JOIN \"TxControl\" G ON G.\"Code\"= CONCAT(CONCAT('L5510.',A.\"WorkMonth\"),'.2') ";
		sql += "WHERE A.\"WorkMonth\" = :workMonth ";

		sql += "ORDER BY A.\"Introducer\",A.\"CustNo\",A.\"FacmNo\",A.\"BormNo\" ";

		this.info("FindL5055 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		this.info("WorkMonth = " + WorkMonth);

		query.setParameter("workMonth", WorkMonth);

		this.info("L5055Service FindData=" + query);

		return switchback(query);
	}

}