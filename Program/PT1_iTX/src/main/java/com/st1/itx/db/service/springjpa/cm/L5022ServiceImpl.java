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
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	// 協辦人員明細資料查詢
	public List<Map<String, String>> findByStatus(int iAcDate, int iEffectiveDateS, int iEffectiveDateE, String iEmpNo,
			int iStatus, int index, int limit, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "";
		sql += "select *              ";
		sql += "from (                ";
		sql += "      select          ";
		sql += "        case when nvl(c.\"logcount\",0)> 0 then 1 else 0 end as \"LogCount\" ";
		sql += "      , a.*           ";
		sql += "      , case when a.\"EffectiveDate\" < :cDate  then 3                                           "; // 待生效
		sql += "             when a.\"IneffectiveDate\" < :cDate and a.\"IneffectiveDate\" !='0' then 2           "; // 已停效
		sql += "             when (a.\"EffectiveDate\" <= :cDate and a.\"IneffectiveDate\" = 0)                   ";
		sql += "               or (a.\"IneffectiveDate\" > :cDate and a.\"EffectiveDate\" <= :cDate) then 1       "; // 已生效
		sql += "             else 9                                                                               "; // 全部
		sql += "        end as \"StatusFg\"        ";
		sql += "      , NVL(b.\"Fullname\",''      ";
		sql += "      , NVL(b.\"QuitDate\",0)      "; // 離職/停約日
		sql += "      , NVL(b.\"AgPostChgDate\",0) "; // 職務異動日
		sql += "      , NVL(b.\"CenterCode\",' ')  "; // 單位代號
		sql += "      , NVL(d.\"EffectiveDate\",0) as \"EvalueChgDate\" "; // 考核職級異動
		sql += "      , NVL(d.\"EmpClass\",' ')    as \"EvalueChgClass\""; // 考核職級
		sql += "      from \"PfCoOfficer\" a  ";
		sql += "      where a.\"EffectiveDate\" > 0 ";
		if (!iEmpNo.trim().isEmpty()) {
			sql += "   and a.\"EmpNo\" = '" + iEmpNo + "' ";
		}
		if (iEffectiveDateS > 0) {
			sql += "   and a.\"EffectiveDate\" >=  + iEffectiveDateS  ";
		}
		if (iEffectiveDateE > 0) {
			sql += "   and a.\"EffectiveDate\" <=  + iEffectiveDateE  ";
		}
		switch (iEffectiveDateS) {
		case 1: // 已生效
			sql += "   and  (    (a.\"EffectiveDate\" <= :cDate and a.\"IneffectiveDate\" = 0)                   ";
			sql += "          or (a.\"IneffectiveDate\" > :cDate and a.\"EffectiveDate\" <= :cDate))             "; // 已生效
			break;
		case 2:
			sql += "   and a.\"IneffectiveDate\" < :cDate and a.\"IneffectiveDate\" !='0' then 2           "; // 已停效
			break;
		case 3:
			sql += "   and a.\"IneffectiveDate\" < :cDate and a.\"IneffectiveDate\" !='0' then 2           "; // 已停效
			break;
		}
		if (iEffectiveDateE > 0) {
			sql += "   and a.\"EffectiveDate\" < :cDate                                                    "; // 待生效
		}
		sql += "      left join \"CdEmp\" b on b.\"EmployeeNo\" = a.\"EmpNo\" ";
		sql += "      Left join (select \"EmpNo\" ,sum(1) as \"logcount\" ";
		sql += "                 from \"PfCoOfficerLog\"                       ";
		sql += "                 group by \"EmpNo\" ) ";
		sql += "                ) c on c.\"EmpNo\" = a.\"EmpNo\" ";
		sql += "      Left join (select \"EmpNo\"              ";   
		sql += "                       ,\"EffectiveDate\"      ";  // 生效日期
		sql += "                       ,\"EmpClass\"           ";  // 協辦等級
		sql += "                       ,ROW_NUMBER() OVER (Partition By \"EmpNo\"              ";
		sql += "    	                   	   ORDER BY l.\"EffectiveDate\" Desc 							               ";
		sql += "	                    ) AS \"ROWNUMBER\"                              ";
		sql += "                 from \"PfCoOfficerLog\"                       ";
		sql += "                 where \"FunctionCode\"  = 7 "; // 7.考核核算底稿 
		sql += "                ) d on d.\"EmpNo\" = a.\"EmpNo\" and  \"ROWNUMBER\" = 1 ";
		sql += "     )  ";
		sql += "order by \"EmpNo\" ASC , \"EffectiveDate\" DESC) ";
		sql += sqlRow;
		this.info("sql = " + sql);
		// *** 折返控制相關 ***
		this.limit = limit;
		query = em.createNativeQuery(sql);
		this.info("L5022Service officerNoBlank=" + query.toString());

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		query.setParameter("cDate", iAcDate);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}
}
