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

@Service("l5951ServiceImpl")
@Repository
public class L5951ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<String[]> findData(int workMonthFm, int workMonthTo, String sumByFacm, int index, int limit,
			TitaVo titaVo) throws LogicException {
		String sqlL5951 = "";
		sqlL5951 += "SELECT BSOF.\"DepItem\" AS \"部室別\" ";
		sqlL5951 += ",EBS.\"Fullname\" AS \"房貸專員姓名\" ";
		sqlL5951 += ",B.\"BsOfficer\" AS \"房貸專員員編\" ";
		sqlL5951 += ",C.\"CustName\" AS \"戶名\" ";
		sqlL5951 += ",I.\"CustNo\" AS \"戶號\" ";
		sqlL5951 += ",I.\"FacmNo\" AS \"額度\" ";
		sqlL5951 += ",I.\"BormNo\" AS \"撥款序號\" ";
		sqlL5951 += ",I.\"DrawdownDate\" AS \"撥款日\" ";
		sqlL5951 += ",I.\"ProdCode\" AS \"利率代碼\" ";
		sqlL5951 += ",I.\"PieceCode\" AS \"計件代碼\" ";
		sqlL5951 += ",I.\"CntingCode\" AS \"是否計件\" ";
		sqlL5951 += ",I.\"DrawdownAmt\" AS \"撥款金額\" ";
		sqlL5951 += ",I.\"DeptCode\" AS \"介紹人部市代號\" ";
		sqlL5951 += ",I.\"DistCode\" AS \"介紹人區部代號\" ";
		sqlL5951 += ",I.\"UnitCode\" AS \"介紹人區域代號\" ";
		sqlL5951 += ",DeptCB.\"UnitItem\" AS \"介紹人部市名稱\" ";
		sqlL5951 += ",DistCB.\"UnitItem\" AS \"介紹人區部名稱\" ";
		sqlL5951 += ",UnitCB.\"UnitItem\" AS \"介紹人區域名稱\" ";
		sqlL5951 += ",EI.\"Fullname\" AS \"介紹人姓名\" ";
		sqlL5951 += ",I.\"Introducer\" AS \"介紹人員編\" ";
		sqlL5951 += ",I.\"DeptManager\" AS \"部經理代號\" ";
		sqlL5951 += ",DeptE.\"Fullname\" AS \"部經理名稱\" ";
		sqlL5951 += ",I.\"DistManager\" AS \"區經理\" ";
		sqlL5951 += ",DistE.\"Fullname\" AS \"區經理名稱\" ";
		sqlL5951 += ",I.\"UnitManager\" AS \"處經理\" ";
		sqlL5951 += ",UnitE.\"Fullname\" AS \"處經理名稱\" ";
		sqlL5951 += ",I.\"PerfEqAmt\" AS \"三階換算業績\" ";
		sqlL5951 += ",I.\"PerfReward\" AS \"三階業務報酬\" ";
		sqlL5951 += ",I.\"PerfAmt\" AS \"業績金額\" ";
		sqlL5951 += ",I.\"WorkMonth\" AS \"工作月\" ";
		sqlL5951 += ",I.\"PerfDate\" AS \"業績日期\" ";
		sqlL5951 += "FROM ( SELECT ";
		sqlL5951 += " \"CustNo\" ";
		sqlL5951 += ",\"FacmNo\" ";
		sqlL5951 += ",MIN (\"BormNo\") AS  \"BormNo\" ";
		sqlL5951 += ",MIN (\"DrawdownDate\") AS \"DrawdownDate\" ";
		sqlL5951 += ",MIN (\"ProdCode\") AS \"ProdCode\" ";
		sqlL5951 += ",\"PieceCode\" ";
		sqlL5951 += ",\"CntingCode\" ";
		sqlL5951 += ",SUM(\"DrawdownAmt\") AS \"DrawdownAmt\" ";
		sqlL5951 += ",\"DeptCode\" ";
		sqlL5951 += ",\"DistCode\" ";
		sqlL5951 += ",\"UnitCode\" ";
		sqlL5951 += ",\"Introducer\" ";
		sqlL5951 += ",\"DeptManager\" ";
		sqlL5951 += ",\"DistManager\" ";
		sqlL5951 += ",\"UnitManager\" ";
		sqlL5951 += ",SUM(\"PerfEqAmt\") AS \"PerfEqAmt\" ";
		sqlL5951 += ",SUM(\"PerfReward\") AS \"PerfReward\" ";
		sqlL5951 += ",SUM(\"PerfAmt\") AS \"PerfAmt\" ";
		sqlL5951 += ",\"WorkMonth\" ";
		sqlL5951 += ",MIN(\"PerfDate\") AS \"PerfDate\" ";
		sqlL5951 += "FROM  \"PfItDetail\" ";
		sqlL5951 += "WHERE \"WorkMonth\" BETWEEN " + workMonthFm + " AND " + workMonthTo + " ";
		sqlL5951 += "AND \"Introducer\" IS NOT NULL ";
		sqlL5951 += "AND \"RepayType\" in (0,4)  "; // 0.撥款(計件代碼變更) 2.部分償還 3.提前結案 4.人工維護 5.保費檢核追回
		sqlL5951 += "AND  \"MediaFg\" in (0,1)  "; // 0:尚未產生媒體檔
													// 1:已產生發放媒體-(不可刪除與異動)2:人工維護後之原資料(不出媒體)3.保費檢核結果為Y時已追回撥款，還款不用追回
		sqlL5951 += "GROUP BY ";
		sqlL5951 += " \"CustNo\" ";
		sqlL5951 += ",\"FacmNo\" ";
		sqlL5951 += ",\"PieceCode\" ";
		sqlL5951 += ",\"CntingCode\" ";
		sqlL5951 += ",\"DeptCode\" ";
		sqlL5951 += ",\"DistCode\" ";
		sqlL5951 += ",\"UnitCode\" ";
		sqlL5951 += ",\"Introducer\" ";
		sqlL5951 += ",\"DeptManager\" ";
		sqlL5951 += ",\"DistManager\" ";
		sqlL5951 += ",\"UnitManager\" ";
		sqlL5951 += ",\"WorkMonth\" ";
		if (!"Y".equals(sumByFacm)) {
			sqlL5951 += ",\"BormNo\" ";
		}
		sqlL5951 += " ) I ";

		sqlL5951 += "LEFT JOIN ( SELECT ";
		sqlL5951 += " \"CustNo\" ";
		sqlL5951 += ",\"FacmNo\" ";
		sqlL5951 += ",\"WorkMonth\" ";
		sqlL5951 += ",MIN (\"BsOfficer\") AS  \"BsOfficer\" ";
		sqlL5951 += ",MIN (\"DeptCode\") AS  \"DeptCode\" ";
		sqlL5951 += "FROM  \"PfBsDetail\" ";
		sqlL5951 += "WHERE \"WorkMonth\" BETWEEN " + workMonthFm + " AND " + workMonthTo + " ";
		sqlL5951 += "GROUP BY ";
		sqlL5951 += " \"CustNo\" ";
		sqlL5951 += ",\"FacmNo\" ";
		sqlL5951 += ",\"WorkMonth\" ";
		sqlL5951 += " ) B ";
		sqlL5951 += "ON I.\"CustNo\" = B.\"CustNo\" ";
		sqlL5951 += "AND I.\"FacmNo\" = B.\"FacmNo\" ";
		sqlL5951 += "AND I.\"WorkMonth\" = B.\"WorkMonth\" ";
		sqlL5951 += "LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\" ";
		sqlL5951 += "LEFT JOIN \"CdEmp\" EBS ON EBS.\"EmployeeNo\" = B.\"BsOfficer\" ";

		sqlL5951 += "LEFT JOIN \"PfBsOfficer\" BSOF ";
		sqlL5951 += "ON BSOF.\"WorkMonth\"=B.\"WorkMonth\" AND BSOF.\"EmpNo\"=B.\"BsOfficer\" ";
		sqlL5951 += "LEFT JOIN \"CdEmp\" EI ON EI.\"EmployeeNo\" = I.\"Introducer\" ";
		sqlL5951 += "LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = B.\"DeptCode\" ";
		sqlL5951 += "LEFT JOIN \"CdBcm\" DeptCB ON DeptCB.\"UnitCode\" = I.\"DeptCode\" ";
		sqlL5951 += "LEFT JOIN \"CdBcm\" DistCB ON DistCB.\"UnitCode\" = I.\"DistCode\" ";
		sqlL5951 += "LEFT JOIN \"CdBcm\" UnitCB ON UnitCB.\"UnitCode\" = I.\"UnitCode\" ";
		sqlL5951 += "LEFT JOIN \"CdEmp\" DeptE ON DeptE.\"EmployeeNo\" = I.\"DeptManager\" ";
		sqlL5951 += "LEFT JOIN \"CdEmp\" DistE ON DistE.\"EmployeeNo\" = I.\"DistManager\" ";
		sqlL5951 += "LEFT JOIN \"CdEmp\" UnitE ON UnitE.\"EmployeeNo\" = I.\"UnitManager\" ";

		sqlL5951 += "ORDER BY I.\"Introducer\",I.\"CustNo\",I.\"FacmNo\",I.\"BormNo\" ";

		sqlL5951 += "OFFSET " + index + " * " + limit + " ROWS FETCH NEXT " + limit + " ROW ONLY ";

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sqlL5951);

		this.info("L5959.sqlL5951=" + sqlL5951);

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