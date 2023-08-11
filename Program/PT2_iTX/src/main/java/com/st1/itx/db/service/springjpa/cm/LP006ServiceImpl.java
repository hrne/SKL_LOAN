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

@Service
@Repository
public class LP006ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(int effectiveDateS, int effectiveDateE, String updateDate, TitaVo titaVo) {

		this.info("LP006ServiceImpl findAll startdate=" + effectiveDateS + ", enddate=" + effectiveDateE + ", updatedate=" + updateDate);

		String sql = "";
        sql += "with DATA AS (                                                                        \r\n" + 
        		"      select                                                                         \r\n" + 
        		"       \"EmpNo\"                                                                     \r\n" + 
        		"      ,\"AreaItem\"    \r\n" + 
        		"      ,\"DistItem\"\r\n" + 
        		"      ,\"DeptItem\"\r\n" + 
        		"      ,\"EffectiveDate\"                                                                    \r\n" + 
        		"      ,\"IneffectiveDate\"                                                                  \r\n" + 
        		"      ,\"EmpClass\"                                                                         \r\n" + 
        		"      ,\"ClassPass\"                     \r\n" + 
        		"      ,\"FunctionCode\"                     \r\n" + 
        		"      ,\"LogNo\"                                                                          \r\n" + 
        		"      from (                                                                              \r\n" + 
        		"            select                                                                            \r\n" + 
        		"             \"EmpNo\"                                                                          \r\n" + 
        		"            ,\"AreaItem\"    \r\n" + 
        		"            ,\"DistItem\"\r\n" + 
        		"            ,\"DeptItem\"\r\n" + 
        		"            ,\"EffectiveDate\"                                                                  \r\n" + 
        		"            ,\"IneffectiveDate\"                                                                \r\n" + 
        		"            ,\"EmpClass\"   \r\n" + 
        		"            ,\"ClassPass\"\r\n" + 
        		"            ,\"FunctionCode\"                     \r\n" + 
        		"            ,\"LogNo\"                                                                          \r\n" + 
        		"            ,ROW_NUMBER() OVER (Partition By \"EmpNo\"                                            \r\n" + 
        		"                       	        ORDER BY \"EffectiveDate\" Desc                                 \r\n" + 
        		"      	                       ) AS \"ROWNUMBER\"                                                  \r\n" + 
        		"            from \"PfCoOfficerLog\"                                                             \r\n" + 
        		"            where case when \"EffectiveDate\" > :startdate and \"EffectiveDate\" <= :enddate then 1   \r\n" +  // 本季起日 < 生效日 <= 下季起日
         		"                       when \"EffectiveDate\" <= :startdate and \"UpdateDate\" > to_date(:updatedate,'yyyy-mm-dd hh24:mi:ss') then 2     \r\n" +  //  生效日 <= 本季起日  && 更新日期時間 > 上季列印日期時間 
        	   	"                       when \"IneffectiveDate\" between :startdate and :enddate then 3               \r\n" +   // 本季起日 <= 停效日 <= 下季起日
        		"                       else 0                                                                 \r\n" + 
        		"                  end > 0     \r\n" + 
        		"             and \"FunctionCode\" between 1 and 8    \r\n" + 
        		"          )       \r\n" + 
        		"       where ROWNUMBER = 1                                                                                                                                                 \r\n" + 
        		") ,LAST AS (                                                                        \r\n" + 
        		"      select                                                                              \r\n" + 
        		"       \"EmpNo\"                                                                            \r\n" + 
        		"      ,\"AreaItem\"                                                                         \r\n" + 
        		"      ,\"DistItem\"\r\n" + 
        		"      ,\"DeptItem\"\r\n" + 
        		"      ,\"EffectiveDate\"                                                                    \r\n" + 
        		"      ,\"IneffectiveDate\"                                                                  \r\n" + 
        		"      ,\"EmpClass\"                                                                         \r\n" + 
        		"      ,\"ClassPass\"\r\n" + 
        		"      ,\"FunctionCode\"                     \r\n" + 
        		"      ,\"LogNo\"                                                                          \r\n" + 
        		"      from (                                                                              \r\n" + 
        		"            select                                                                            \r\n" + 
        		"             l.\"EmpNo\"                                                                          \r\n" + 
        		"            ,l.\"AreaItem\"                                                                       \r\n" + 
        		"            ,l.\"DistItem\"\r\n" + 
        		"            ,l.\"DeptItem\"\r\n" + 
        		"            ,l.\"EffectiveDate\"                                                                  \r\n" + 
        		"            ,l.\"IneffectiveDate\"                                                                \r\n" + 
        		"            ,l.\"EmpClass\"                                                                       \r\n" + 
        		"            ,l.\"ClassPass\"\r\n" + 
        		"            ,l.\"FunctionCode\"                     \r\n" + 
        		"            ,l.\"LogNo\"                                                                          \r\n" + 
        		"            ,ROW_NUMBER() OVER (Partition By l.\"EmpNo\"                                            \r\n" + 
        		"                       	    ORDER BY l.\"LogNo\" Desc                                 \r\n" + 
        		"      	                       ) AS \"ROWNUMBER\"                                                  \r\n" + 
        		"            from DATA d\r\n" + 
        		"            left join \"PfCoOfficerLog\" l on l.\"EmpNo\" = d.\"EmpNo\"\r\n" + 
        		"                                        and l.\"LogNo\" < d.\"LogNo\"  \r\n" + 
        		"                                        and l.\"FunctionCode\" between 1 and 8    \r\n" + 
        		"           )      \r\n" + 
        		"       where ROWNUMBER = 1   \r\n" + 
        		")\r\n" + 
        		"select                                                                                \r\n" + 
        		" d.\"EmpNo\" \r\n" + 
        		",NVL(e.\"Fullname\",' ')  AS \"Fullname\" \r\n" + 
        		",d.\"AreaItem\"                                                                         \r\n" + 
        		",d.\"DistItem\"\r\n" + 
        		",d.\"DeptItem\"\r\n" + 
        		",d.\"EffectiveDate\"                                                                    \r\n" + 
        		",d.\"IneffectiveDate\"                                                                  \r\n" + 
        		",d.\"EmpClass\"  \r\n" + 
        		",nvl(cd1.\"Item\",' ')        as  \"EmpClassX\"                                                                                                                                                               \r\n" + 
       	    	",d.\"ClassPass\"   \r\n" + 
        		",d.\"FunctionCode\" \r\n" + 
        		",nvl(l.\"AreaItem\",' ')      as  \"LastAreaItem\"                                                                         \r\n" + 
        		",nvl(l.\"DistItem\",' ')      as  \"LastDistItem\"                                                                         \r\n" + 
        		",nvl(l.\"DeptItem\",' ')      as  \"LastDeptItem\"                                                                         \r\n" + 
        		",nvl(l.\"EffectiveDate\",0)   as  \"LastEffectiveDate\"                                                                    \r\n" + 
        		",nvl(l.\"IneffectiveDate\",0) as  \"LastIneffectiveDate\"                                                                  \r\n" + 
        		",nvl(l.\"EmpClass\",' ')      as  \"LastEmpClass\"                                                                                                                                                               \r\n" + 
        		",nvl(cd2.\"Item\",' ')        as  \"LastEmpClassX\"                                                                                                                                                               \r\n" + 
        		",nvl(l.\"ClassPass\",' ')     as  \"LastClassPass\"                                                                                                                                                               \r\n" + 
        		",nvl(l.\"FunctionCode\",0)    as  \"LastFunctionCode\"                                                                                                                                                               \r\n" + 
        		"from DATA d                                                                          \r\n" + 
        		"left join LAST l on l.\"EmpNo\" =  d.\"EmpNo\" \r\n" + 
        		"left join \"CdEmp\" e on e.\"EmployeeNo\" = d.\"EmpNo\"  \r\n" + 
        		"left join \"CdCode\" cd1 on cd1.\"DefCode\"= 'ClassType' and cd1.\"Code\" = d.\"EmpClass\"  \r\n" + 
        		"left join \"CdCode\" cd2 on cd2.\"DefCode\"= 'ClassType' and cd2.\"Code\" = nvl(l.\"EmpClass\",' ')  \r\n" + 
        		"order by d.\"DeptItem\", d.\"DistItem\", d.\"AreaItem\", d.\"EmpNo\"   \r\n"; 
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startdate", effectiveDateS);
		query.setParameter("enddate", effectiveDateE);
		query.setParameter("updatedate", updateDate);

		return this.convertToMap(query);
	}

}