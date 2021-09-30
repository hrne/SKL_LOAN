package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l5022ServiceImpl")
public class L5022ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	/*
	 * public List<Map<String, String>> officerNoBlank(int index, int limit,TitaVo
	 * titaVo) throws Exception{ Query query; EntityManager em =
	 * this.baseEntityManager.getCurrentEntityManager(titaVo); String sql =
	 * "select * from (" + " select a.\"EmpNo\" ," + " a.\"EffectiveDate\"," +
	 * " a.\"EmpClass\"," + " a.\"ClassPass\" ," + " b.\"Fullname\"," +
	 * " b.\"CenterCodeName\"," + " b.\"CenterCode\"," + " c.\"DistCode\"," +
	 * " c.\"DistItem\"," + " c.\"DeptCode\"," + " c.\"DeptItem\"," +
	 * " a.\"IneffectiveDate\"," + " row_number() over (partition by a.\"EmpNo\"" +
	 * " order by a.\"EffectiveDate\" desc) as \"sort\"" + " from \"PfCoOfficer\" a"
	 * + " left join \"CdEmp\" b on b.\"EmployeeNo\" = a.\"EmpNo\"" +
	 * " left join \"CdBcm\" c on c.\"UnitCode\" = b.\"CenterCode\" " +
	 * ") x where x.\"sort\" = 1 "; sql += sqlRow; this.info("sql = " + sql); // ***
	 * 折返控制相關 *** this.index = index; // *** 折返控制相關 *** this.limit = limit; query =
	 * em.createNativeQuery(sql); this.info("L5022Service officerNoBlank=" +
	 * query.toString());
	 * 
	 * query.setParameter("ThisIndex", index); query.setParameter("ThisLimit",
	 * limit);
	 * 
	 * query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
	 * 
	 * // *** 折返控制相關 *** // 設定每次撈幾筆,需在createNativeQuery後設定
	 * query.setMaxResults(this.limit);
	 * 
	 * return this.convertToMap(query); }
	 */

	/**
	 * 
	 * @param cDate  ...
	 * @param index  ...
	 * @param limit  ...
	 * @param titaVo titaVo
	 * @return data ...
	 * @throws Exception ...
	 */
	// 當輸入空白找尋最近的日期資料
	public List<Map<String, String>> findByStatus(int cDate,String empNo, int index, int limit, TitaVo titaVo) throws Exception {

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select * from (select a.* , "
							 + "case when a.\"EffectiveDate\" > '"+cDate+"' then '3'"
							 + " when a.\"IneffectiveDate\" < '"+cDate+"' and a.\"IneffectiveDate\" !='0' then '2'"
							 + " when (a.\"EffectiveDate\" <= '"+cDate+"' and a.\"IneffectiveDate\" = '0') or (a.\"IneffectiveDate\" > '"+cDate+"' and a.\"EffectiveDate\" <= '"+cDate+"')then '1' "
							 + " else '9' end \"StatusFg\","
							 + " b.\"Fullname\" from \"PfCoOfficer\" a left join \"CdEmp\" b on b.\"EmployeeNo\" = a.\"EmpNo\" ";
							 if (!empNo.trim().isEmpty()) {
								 sql += " where a.\"EmpNo\" = '"+empNo+"' ";
							 }
				sql  += "order by a.\"EmpNo\" ASC , \"EffectiveDate\" DESC) ";
					
		sql += sqlRow;
		this.info("sql = " + sql);
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		query = em.createNativeQuery(sql);
		this.info("L5022Service officerNoBlank=" + query.toString());

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}
}
