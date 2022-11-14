package com.st1.itx.db.service.springjpa.cm;

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

@Service
@Repository
public class L6088ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws LogicException {

		this.info("L6088ServiceImpl findAll active");

		String iCenterCode = titaVo.getParam("CenterCode").trim(); // 單位代號
		String iEmployeeNo = titaVo.getParam("EmployeeNo").trim(); // 員工編號
		String iEmpId = titaVo.getParam("EmpId").trim(); // 員工統編
		String iEmployeeNoX = titaVo.getParam("EmployeeNoX").trim(); // 員工姓名
		String iAgStatusCode = titaVo.getParam("AgStatusCode").trim(); // 是否在職

		this.info("CenterCode: [" + iCenterCode + "]");
		this.info("EmployeeNo: [" + iEmployeeNo + "]");
		this.info("EmpId: [" + iEmpId + "]");
		this.info("EmployeeNoX: [" + iEmployeeNoX + "]");
		this.info("AgStatusCode: [" + iAgStatusCode + "]");

		boolean useCenterCode = !iCenterCode.isEmpty();
		boolean useEmployeeNo = !iEmployeeNo.isEmpty();
		boolean useEmpId = !iEmpId.isEmpty();
		boolean useEmployeeNoX = !iEmployeeNoX.isEmpty();
		boolean useAgStatusCode = !iAgStatusCode.isEmpty();

		String sql = " ";
		sql += " SELECT CE.\"EmployeeNo\" "; // 員工編號
		sql += "      , CE.\"AgentId\"    	AS \"AgentId\""; // 身分證字號
		sql += "      , CE.\"Fullname\"   	AS \"Fullname\""; // 員工姓名
		sql += "      , NVL(CE.\"SeniorityYY\",0)   	AS \"SeniorityYY\""; // 年資_年
		sql += "      , NVL(CE.\"SeniorityMM\",0)   	AS \"SeniorityMM\""; // 年資_月
		sql += "      , CE.\"AgLevel\"    	AS \"EmployeeAgLevel\" "; // 上一級主管職等
		sql += "      , S1.\"AgLevel\"    	AS \"FirstSuperiorAgLevel\" "; // 上一級主管職等
		sql += "      , S1.\"EmployeeNo\"  	AS \"FirstSuperiorEmpNo\" "; // 上一級主管編號
		sql += "      , S1.\"Fullname\"   	AS \"FirstSuperiorName\" "; // 上一級主管姓名
		sql += "      , S2.\"AgLevel\"    	AS \"SecondSuperiorAgLevel\" "; // 上兩級主管職等
		sql += "      , S2.\"EmployeeNo\"  	AS \"SecondSuperiorEmpNo\" "; // 上兩級主管編號
		sql += "      , S2.\"Fullname\"   	AS \"SecondSuperiorName\" "; // 上兩級主管姓名
		sql += "      , S3.\"AgLevel\"    	AS \"ThirdSuperiorAgLevel\" "; // 上三級主管職等
		sql += "      , S3.\"EmployeeNo\"  	AS \"ThirdSuperiorEmpNo\" "; // 上三級主管編號
		sql += "      , S3.\"Fullname\"   	AS \"ThirdSuperiorName\" "; // 上三級主管姓名
		sql += "      , CE.\"CenterCode\" 	AS \"UnitCode\" "; // 單位代號
		sql += "      , CBUnit.\"UnitItem\" AS \"UnitItem\" "; // 單位名稱
		sql += "      , CE.\"CenterCode1\"  AS \"DistCode\" "; // 區部代號
		sql += "      , CBDist.\"UnitItem\" AS \"DistItem\" "; // 區部名稱
		sql += "      , CE.\"CenterCode2\"  AS \"DeptCode\" "; // 部室代號
		sql += "      , CBDept.\"UnitItem\" AS \"DeptItem\" "; // 部室名稱
		sql += "      , CD.\"Item\"  		AS \"AgStatus\" "; // 身分別
		sql += "      , CE.\"AgCurInd\" 	AS \"AgCurInd\""; // 在職
		sql += "      , CE.\"LastUpdate\" 	AS \"LastUpdate\""; // 最後修改日期
		// 如果像這種 SELECT 欄位很長的, 要取 alias 否則 Java 端接 Query 結果會報錯
		sql += " FROM \"CdEmp\" CE ";
		sql += " LEFT JOIN \"CdEmp\" S1 ON CE.\"DirectorId\" = S1.\"AgentCode\" ";
		sql += " LEFT JOIN \"CdEmp\" S2 ON S1.\"DirectorId\" = S2.\"AgentCode\" ";
		sql += " LEFT JOIN \"CdEmp\" S3 ON S2.\"DirectorId\" = S3.\"AgentCode\" ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'EmpIdentity' ";
		sql += "                        AND CD.\"Code\"    = CE.\"AgStatusCode\" ";
		sql += " LEFT JOIN \"CdBcm\" CBUnit ON CBUnit.\"UnitCode\" = CE.\"CenterCode\" "; // CdBcm 結構: 所有單位/部室/區部 都寫一筆
																							// UnitCode = 它 的資料
		sql += " LEFT JOIN \"CdBcm\" CBDist ON CBDist.\"UnitCode\" = CE.\"CenterCode1\" "; // 所以這邊都用 UnitCode
		sql += " LEFT JOIN \"CdBcm\" CBDept ON CBDept.\"UnitCode\" = CE.\"CenterCode2\" ";
		sql += " WHERE 1 = 1 "; // dummy
		if (useCenterCode)
			sql += " AND CE.\"CenterCode\" = :CenterCode ";
		if (useEmployeeNo)
			sql += " AND CE.\"EmployeeNo\" = :EmployeeNo ";
		if (useEmpId)
			sql += " AND CE.\"AgentId\" = :EmpId ";
		if (useEmployeeNoX)
			sql += " AND CE.\"Fullname\" = :EmployeeNoX ";
		if (useAgStatusCode)
			sql += " AND CE.\"AgCurInd\" = :AgStatusCode "; // 這裡 AgStatusCode 是 Y/N，條件是在職/非在職，應該是對應 AgCurInd (Y/N) 而非
															// AgStatusCode (null/1)
		sql += " ORDER BY CE.\"EmployeeNo\" ";
		sql += " OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

		this.info("L6088ServiceImpl SqlQuery = " + sql);

		Query query;
		EntityManager em = baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (useCenterCode)
			query.setParameter("CenterCode", iCenterCode);
		if (useEmployeeNo)
			query.setParameter("EmployeeNo", iEmployeeNo);
		if (useEmpId)
			query.setParameter("EmpId", iEmpId);
		if (useEmployeeNoX)
			query.setParameter("EmployeeNoX", iEmployeeNoX);
		if (useAgStatusCode)
			query.setParameter("AgStatusCode", iAgStatusCode);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		query.setParameter("ThisIndex", this.index);
		query.setParameter("ThisLimit", this.limit);

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
