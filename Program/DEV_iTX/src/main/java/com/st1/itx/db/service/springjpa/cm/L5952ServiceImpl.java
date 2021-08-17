package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5952ServiceImpl")
@Repository
public class L5952ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<String[]> findData(int workMonthFm, int workMonthTo, String sumByFacm, int index, int limit,
			TitaVo titaVo) throws LogicException {
		String sqlL5952 = "";
		sqlL5952 += "SELECT  ";
		sqlL5952 += "BSOF.\"DepItem\" AS \"部室別名稱\" ";
		sqlL5952 += ",BSOF.\"Fullname\" AS \"房貸專員名稱\" ";
		sqlL5952 += ",BSD.\"PerfCnt\" AS \"房貸專員件數\" ";
		sqlL5952 += ",BSD.\"PerfAmt\" AS \"房貸專員業績金額\" ";
		sqlL5952 += ",BSD.\"BsOfficer\" AS \"房貸專員\" ";
		sqlL5952 += ",CM.\"CustName\" AS \"戶名\" ";
		sqlL5952 += ",BSD.\"CustNo\" AS \"戶號\" ";
		sqlL5952 += ",BSD.\"FacmNo\" AS \"額度\" ";
		sqlL5952 += ",BSD.\"BormNo\" AS \"撥款序號\" ";
		sqlL5952 += ",BSD.\"DrawdownDate\" AS \"撥款日\" ";
		sqlL5952 += ",BSD.\"ProdCode\" AS \"商品代碼\" ";
		sqlL5952 += ",BSD.\"PieceCode\" AS \"計件代碼\" ";
		sqlL5952 += ",BSD.\"DrawdownAmt\" AS \"撥款金額\" ";
		sqlL5952 += ",BSD.\"WorkMonth\" AS \"工作月\" ";
		sqlL5952 += ",ICBDept.\"UnitItem\" AS \"介紹人區部名稱\" ";
		sqlL5952 += ",ICBDist.\"UnitItem\" AS \"介紹人部室別名稱\" ";
		sqlL5952 += ",ICBUnit.\"UnitItem\" AS \"介紹人區域中心名稱\" ";
		sqlL5952 += ",IEMP.\"Fullname\" AS \"介紹人姓名\" ";
		sqlL5952 += ",ITD.\"Introducer\" AS \"介紹人員編\" ";
		sqlL5952 += "FROM ( SELECT ";
		sqlL5952 += " SUM(\"PerfCnt\") AS \"PerfCnt\" ";
		sqlL5952 += ",SUM(\"PerfAmt\") AS \"PerfAmt\" ";
		sqlL5952 += ",\"BsOfficer\" ";
		sqlL5952 += ",\"CustNo\" ";
		sqlL5952 += ",\"FacmNo\" ";
		sqlL5952 += ",MIN(\"BormNo\") AS \"BormNo\" ";
		sqlL5952 += ",MIN(\"DrawdownDate\") AS \"DrawdownDate\" ";
		sqlL5952 += ",MIN (\"ProdCode\") AS \"ProdCode\" ";
		sqlL5952 += ",\"PieceCode\" ";
		sqlL5952 += ",SUM(\"DrawdownAmt\") AS \"DrawdownAmt\" ";
		sqlL5952 += ",\"WorkMonth\" ";
		sqlL5952 += "FROM \"PfBsDetail\" ";
		sqlL5952 += "WHERE \"WorkMonth\" BETWEEN " + workMonthFm + " AND " + workMonthTo + " ";
		sqlL5952 += "AND \"RepayType\" in (0,4)  "; // 0.撥款(計件代碼變更) 2.部分償還 3.提前結案4.人工增減業績
		sqlL5952 += "GROUP BY ";
		sqlL5952 += " \"CustNo\" ";
		sqlL5952 += ",\"FacmNo\" ";
		sqlL5952 += ",\"PieceCode\" ";
		sqlL5952 += ",\"BsOfficer\" ";
		sqlL5952 += ",\"WorkMonth\" ";
		if (!"Y".equals(sumByFacm)) {
			sqlL5952 += ",\"BormNo\" ";
		}
		sqlL5952 += " ) BSD ";
		sqlL5952 += "LEFT JOIN ( SELECT  ";
		sqlL5952 += " \"CustNo\" ";
		sqlL5952 += ",\"FacmNo\" ";
		sqlL5952 += ",\"Introducer\" ";
		sqlL5952 += ",\"WorkMonth\" ";
 	    sqlL5952 += ",MIN (\"UnitCode\") AS  \"UnitCode\" ";
		sqlL5952 += ",MIN (\"DistCode\") AS  \"DistCode\" ";
		sqlL5952 += ",MIN (\"DeptCode\") AS  \"DeptCode\" ";
		sqlL5952 += "FROM  \"PfItDetail\" ";
		sqlL5952 += "WHERE \"WorkMonth\" BETWEEN " + workMonthFm + " AND " + workMonthTo + " ";
		sqlL5952 += "GROUP BY ";
		sqlL5952 += " \"CustNo\" ";
		sqlL5952 += ",\"FacmNo\" ";
		sqlL5952 += ",\"Introducer\" ";
		sqlL5952 += ",\"WorkMonth\" ";
		sqlL5952 += " ) ITD ";
		sqlL5952 += "ON ITD.\"CustNo\" = BSD.\"CustNo\" ";
		sqlL5952 += "AND ITD.\"FacmNo\" = BSD.\"FacmNo\" ";
		sqlL5952 += "AND ITD.\"WorkMonth\" = BSD.\"WorkMonth\" ";
		sqlL5952 += "LEFT JOIN \"PfBsOfficer\" BSOF ";
		sqlL5952 += "ON BSOF.\"WorkMonth\"=BSD.\"WorkMonth\" AND BSOF.\"EmpNo\"=BSD.\"BsOfficer\" ";
	    sqlL5952 += "LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\"= BSD.\"CustNo\" ";
		sqlL5952 += "LEFT JOIN \"CdBcm\" ICBUnit ON ICBUnit.\"UnitCode\"=ITD.\"UnitCode\" ";
		sqlL5952 += "LEFT JOIN \"CdBcm\" ICBDist ON ICBDist.\"UnitCode\"=ITD.\"DistCode\" ";
		sqlL5952 += "LEFT JOIN \"CdBcm\" ICBDept ON ICBDept.\"UnitCode\"=ITD.\"DeptCode\" ";
		sqlL5952 += "LEFT JOIN \"CdEmp\" IEMP ON IEMP.\"EmployeeNo\"=ITD.\"Introducer\" ";

		sqlL5952 += "ORDER BY BSD.\"BsOfficer\",BSD.\"CustNo\",BSD.\"FacmNo\",BSD.\"BormNo\" ";

		sqlL5952 += "OFFSET " + index + " * " + limit + " ROWS FETCH NEXT " + limit + " ROW ONLY ";

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sqlL5952);

		this.info("L5959.sqlL5952=" + sqlL5952);

		@SuppressWarnings("unchecked")
		List<Object> lObject = this.convertToMap(query.getResultList());

		return findItem(lObject);

	}

	@SuppressWarnings("unchecked")
	public List<String[]> findItem(List<Object> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = ((Map<String, String>) lObject.get(0)).keySet().size();
			for (Object obj : lObject) {
				Map<String, String> MapObj = (Map<String, String>) obj;
				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + String.valueOf(i));
					if (row[i] != null && row[i].length() != 0) {

					} else {
						row[i] = "";
					}
				}
				data.add(row);
			}
		}
		return data;
	}
}