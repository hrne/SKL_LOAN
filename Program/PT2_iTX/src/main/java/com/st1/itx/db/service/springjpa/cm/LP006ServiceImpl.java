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

	public List<Map<String, String>> findChange(int effectiveDateS, int effectiveDateE, String updateDate,
			TitaVo titaVo) {

		this.info("LP006ServiceImpl findChange startdate(本季起日)=" + effectiveDateS + ", enddate(下季起日)=" + effectiveDateE
				+ ", updateDate=" + updateDate);

		String sql = "";
		sql += "with DATA AS (								";
		sql += "      select								";
		sql += "       \"EmpNo\"							";
		sql += "      ,\"AreaItem\"							";
		sql += "      ,\"DistItem\"							";
		sql += "		,\"DeptItem\"	AS \"DeptItem\" ";
		sql += "		,\"DeptSeq\"	AS \"DeptSeq\" ";
		sql += "      ,\"EffectiveDate\"					";
		sql += "      ,\"IneffectiveDate\"					";
		sql += "      ,\"EmpClass\"							";
		sql += "      ,\"ClassPass\"						";
		sql += "      ,\"FunctionCode\"						";
		sql += "      ,\"LogNo\"							";
		sql += "      from (								";
		sql += "            select							";
		sql += "             \"EmpNo\"						";
		sql += "            ,\"AreaItem\"					";
		sql += "            ,\"DistItem\"					";
		sql += " 		 ,CASE ";
		sql += "          WHEN \"DeptCode\" = 'A0B000' ";
		sql += "          THEN '營管部' ";
		sql += "          WHEN \"DeptCode\" = 'A0F000' ";
		sql += "          THEN '營推部' ";
		sql += "          WHEN \"DeptCode\" = 'A0E000' ";
		sql += "          THEN '業推部' ";
		sql += "          WHEN \"DeptCode\" = 'A0M000' ";
		sql += "          THEN '業開部' ";
		sql += "          WHEN \"DeptCode\" = 'A0X000' ";
		sql += "          THEN '專業行銷課' ";
		sql += "        ELSE \"DeptItem\" END            AS \"DeptItem\" ";
		sql += " 		 ,CASE ";
		sql += "          WHEN \"DeptCode\" = 'A0B000' ";
		sql += "          THEN 1 ";
		sql += "          WHEN \"DeptCode\" = 'A0F000' ";
		sql += "          THEN 2 ";
		sql += "          WHEN \"DeptCode\" = 'A0E000' ";
		sql += "          THEN 3 ";
		sql += "          WHEN \"DeptCode\" = 'A0M000' ";
		sql += "          THEN 4 ";
		sql += "          WHEN \"DeptCode\" = 'A0X000' ";
		sql += "          THEN 5 ";
		sql += "        ELSE 6 END            AS \"DeptSeq\" ";
		sql += "            ,\"EffectiveDate\"				";
		sql += "            ,\"IneffectiveDate\"			";
		sql += "            ,\"EmpClass\"					";
		sql += "            ,\"ClassPass\"					";
		sql += "            ,\"FunctionCode\"				";
		sql += "            ,\"LogNo\"						";
		sql += "            ,ROW_NUMBER() OVER (Partition By \"EmpNo\"				";
		sql += "                       	        ORDER BY \"EffectiveDate\" Desc		";
		sql += "                       	                ,\"LogNo\" Desc		        ";
		sql += "      	                       ) AS \"ROWNUMBER\"					";
		sql += "            from \"PfCoOfficerLog\"									";
		sql += "            where case when \"EffectiveDate\" > :startdate and \"EffectiveDate\" <= :enddate then 1   ";
		// 本季起日 < 生效日 <= 下季起日
		sql += "                     when \"EffectiveDate\" <= :startdate and \"UpdateDate\" > to_date(:updatedate,'yyyy-mm-dd hh24:mi:ss') then 2     ";
		// 生效日 <= 本季起日 && 更新日期時間 > 上季列印日期時間
		sql += "                     when \"IneffectiveDate\" between :startdate and :enddate then 3               ";
		// 本季起日 <= 停效日 <= 下季起日
		sql += "                     else 0                                                                 ";
		sql += "                  end > 0       and \"FunctionCode\" between 1 and 8    ";
		sql += "          )       ";
		sql += "       where ROWNUMBER = 1                                                                                                                                                 ";
		sql += ") ,LAST AS (                                                                        ";
		sql += "      select                                                                              ";
		sql += "       \"EmpNo\"                                                                            ";
		sql += "      ,\"AreaItem\"                                                                         ";
		sql += "      ,\"DistItem\"";
		sql += "      ,\"DeptItem\"";
		sql += "      ,\"EffectiveDate\"                                                                    ";
		sql += "      ,\"IneffectiveDate\"                                                                  ";
		sql += "      ,\"EmpClass\"                                                                         ";
		sql += "      ,\"ClassPass\" ";
		sql += "	   ,\"FunctionCode\"                     ";
		sql += "      ,\"LogNo\"                                                                          ";
		sql += "      from (                                                                              ";
		sql += "            select                                                                            ";
		sql += "             l.\"EmpNo\"                                                                          ";
		sql += "            ,l.\"AreaItem\"                                                                       ";
		sql += "            ,l.\"DistItem\"";
		sql += " 		 ,CASE ";
		sql += "          WHEN l.\"DeptCode\" = 'A0B000' ";
		sql += "          THEN '營管部' ";
		sql += "          WHEN l.\"DeptCode\" = 'A0F000' ";
		sql += "          THEN '營推部' ";
		sql += "          WHEN l.\"DeptCode\" = 'A0E000' ";
		sql += "          THEN '業推部' ";
		sql += "          WHEN l.\"DeptCode\" = 'A0M000' ";
		sql += "          THEN '業開部' ";
		sql += "          WHEN l.\"DeptCode\" = 'A0X000' ";
		sql += "          THEN '專業行銷課' ";
		sql += "        ELSE l.\"DeptItem\" END            AS \"DeptItem\" ";
		sql += " 		 ,CASE ";
		sql += "          WHEN l.\"DeptCode\" = 'A0B000' ";
		sql += "          THEN 1 ";
		sql += "          WHEN l.\"DeptCode\" = 'A0F000' ";
		sql += "          THEN 2 ";
		sql += "          WHEN l.\"DeptCode\" = 'A0E000' ";
		sql += "          THEN 3 ";
		sql += "          WHEN l.\"DeptCode\" = 'A0M000' ";
		sql += "          THEN 4 ";
		sql += "          WHEN l.\"DeptCode\" = 'A0X000' ";
		sql += "          THEN 5 ";
		sql += "        ELSE 6 END            AS \"DeptSeq\" ";
		sql += "            ,l.\"EffectiveDate\"                                                                  ";
		sql += "            ,l.\"IneffectiveDate\"                                                                ";
		sql += "            ,l.\"EmpClass\"                                                                       ";
		sql += "            ,l.\"ClassPass\"";
		sql += "            ,l.\"FunctionCode\"                     ";
		sql += "            ,l.\"LogNo\"                                                                          ";
		sql += "            ,ROW_NUMBER() OVER (Partition By l.\"EmpNo\"                                            ";
		sql += "                       	        ORDER BY l.\"EffectiveDate\" Desc		";
		sql += "                       	                ,l.\"LogNo\" Desc		        ";
		sql += "                       	    ORDER BY l.\"LogNo\" Desc                                 ";
		sql += "      	                       ) AS \"ROWNUMBER\"                                                  ";
		sql += "            from DATA d";
		sql += "            left join \"PfCoOfficerLog\" l on l.\"EmpNo\" = d.\"EmpNo\"";
		sql += "                                        and l.\"EffectiveDate\" <= d.\"effectiveDateS\"  ";
		sql += "                                        and l.\"LogNo\" < d.\"LogNo\"  ";
		sql += "                                        and l.\"FunctionCode\" between 1 and 8    ";
		sql += "           )            where ROWNUMBER = 1  )";
		sql += "select                                                                                ";
		sql += " d.\"EmpNo\"  ,NVL(e.\"Fullname\",' ')  AS \"Fullname\" ";
		sql += ",nvl(d.\"AreaItem\",' ')      as  \"AreaItem\"    ";
		sql += ",nvl(d.\"DistItem\",' ')      as  \"DistItem\"    ";
		sql += ",nvl(d.\"DeptItem\",' ')      as  \"DeptItem\"    ";
		sql += ",d.\"EffectiveDate\"                                                                    ";
		sql += ",d.\"IneffectiveDate\"                                                                  ";
		sql += ",nvl(d.\"EmpClass\",' ')      as  \"EmpClass\"  ";
		sql += ",nvl(cd1.\"Item\",' ')        as  \"EmpClassX\"	";
		sql += ",nvl(d.\"ClassPass\",' ')     as  \"ClassPass\" ";
		sql += ",d.\"FunctionCode\" ";
		sql += ",nvl(l.\"AreaItem\",' ')      as  \"LastAreaItem\"                                                                         ";
		sql += ",nvl(l.\"DistItem\",' ')      as  \"LastDistItem\"                                                                         ";
		sql += ",nvl(l.\"DeptItem\",' ')      as  \"LastDeptItem\"                                                                         ";
		sql += ",nvl(l.\"EffectiveDate\",0)   as  \"LastEffectiveDate\"                                                                    ";
		sql += ",nvl(l.\"IneffectiveDate\",0) as  \"LastIneffectiveDate\"                                                                  ";
		sql += ",nvl(l.\"EmpClass\",' ')      as  \"LastEmpClass\"	";
		sql += ",nvl(cd2.\"Item\",' ')        as  \"LastEmpClassX\"	";
		sql += ",nvl(l.\"ClassPass\",' ')     as  \"LastClassPass\"	";
		sql += ",nvl(l.\"FunctionCode\",0)    as  \"LastFunctionCode\"	";
		sql += "from DATA d                                                                          ";
		sql += "left join LAST l on l.\"EmpNo\" =  d.\"EmpNo\" ";
		sql += "left join \"CdEmp\" e on e.\"EmployeeNo\" = d.\"EmpNo\"  ";
		sql += "left join \"CdCode\" cd1 on cd1.\"DefCode\"= 'ClassType' and cd1.\"Code\" = d.\"EmpClass\"  ";
		sql += "left join \"CdCode\" cd2 on cd2.\"DefCode\"= 'ClassType' and cd2.\"Code\" = nvl(l.\"EmpClass\",' ')  ";
		sql += "order by d.\"DeptSeq\", d.\"DistItem\", d.\"AreaItem\", d.\"EmpNo\"   ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startdate", effectiveDateS);
		query.setParameter("enddate", effectiveDateE);
		query.setParameter("updatedate", updateDate);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int effectiveDateS, int effectiveDateE, TitaVo titaVo) {
		this.info("LP006ServiceImpl findAllChange startdate(本季起日)=" + effectiveDateS + ", enddate(下季起日)="
				+ effectiveDateE);

		String sql = "";
		sql += "WITH COOFFICER AS ( ";
		sql += "  SELECT \"EmpNo\"         ";
		sql += " 		 ,CASE ";
		sql += "          WHEN \"DeptCode\" = 'A0B000' ";
		sql += "          THEN '營管部' ";
		sql += "          WHEN \"DeptCode\" = 'A0F000' ";
		sql += "          THEN '營推部' ";
		sql += "          WHEN \"DeptCode\" = 'A0E000' ";
		sql += "          THEN '業推部' ";
		sql += "          WHEN \"DeptCode\" = 'A0M000' ";
		sql += "          THEN '業開部' ";
		sql += "          WHEN \"DeptCode\" = 'A0X000' ";
		sql += "          THEN '專業行銷課' ";
		sql += "        ELSE TO_CHAR(\"DeptItem\") END            AS \"DeptItem\" ";
		sql += " 		 ,CASE ";
		sql += "          WHEN \"DeptCode\" = 'A0B000' ";
		sql += "          THEN 1 ";
		sql += "          WHEN \"DeptCode\" = 'A0F000' ";
		sql += "          THEN 2 ";
		sql += "          WHEN \"DeptCode\" = 'A0E000' ";
		sql += "          THEN 3 ";
		sql += "          WHEN \"DeptCode\" = 'A0M000' ";
		sql += "          THEN 4 ";
		sql += "          WHEN \"DeptCode\" = 'A0X000' ";
		sql += "          THEN 5 ";
		sql += "        ELSE 6 END            AS \"DeptSeq\" ";
		sql += "       , \"DistItem\"      ";
		sql += "       , \"AreaItem\"      ";
		sql += "       , \"EmpClass\"      ";
		sql += "       , \"EffectiveDate\"      ";
		sql += "       , \"IneffectiveDate\"      ";
		sql += "       , ROW_NUMBER() OVER (Partition By \"EmpNo\"              ";
		sql += "    	                   	    ORDER BY \"EffectiveDate\" Desc      ";
		sql += "	                       ) AS ROWNUMBER                            ";
		sql += " FROM \"PfCoOfficer\" ";
		sql += " WHERE \"EffectiveDate\"  < :enddate ";
		sql += "   AND \"IneffectiveDate\" >= :startdate ";
		sql += " ) ";
		sql += ", COOFFICERLAST AS ( ";
		sql += "  SELECT \"EmpNo\"         ";
		sql += " 		 ,CASE ";
		sql += "          WHEN \"DeptCode\" = 'A0B000' ";
		sql += "          THEN '營管部' ";
		sql += "          WHEN \"DeptCode\" = 'A0F000' ";
		sql += "          THEN '營推部' ";
		sql += "          WHEN \"DeptCode\" = 'A0E000' ";
		sql += "          THEN '業推部' ";
		sql += "          WHEN \"DeptCode\" = 'A0M000' ";
		sql += "          THEN '業開部' ";
		sql += "          WHEN \"DeptCode\" = 'A0X000' ";
		sql += "          THEN '專業行銷課' ";
		sql += "        ELSE TO_CHAR(\"DeptItem\") END            AS \"DeptItem\" ";
		sql += "       , \"DistItem\"      ";
		sql += "       , \"AreaItem\"      ";
		sql += "       , \"EmpClass\"      ";
		sql += "       , \"EffectiveDate\"      ";
		sql += "       , \"IneffectiveDate\"    ";
		sql += "       , ROW_NUMBER() OVER (Partition By \"EmpNo\"              ";
		sql += "    	                   	    ORDER BY \"EffectiveDate\" Desc      ";
		sql += "	                       ) AS ROWNUMBER                            ";
		sql += " FROM \"PfCoOfficer\" ";
		sql += " WHERE \"EffectiveDate\"  < :startdate ";
		sql += " ) ";
		sql += " SELECT PCO.\"DeptItem\"         AS \"DeptItem\"    "; // -- 部室
		sql += "      , PCO.\"DistItem\"         AS \"DistItem\"    "; // -- 區部
		sql += "      , PCO.\"AreaItem\"         AS \"AreaItem\"    "; // -- 單位
		sql += "      , \"Fn_GetEmpName\"(PCO.\"EmpNo\",0) ";
		sql += "                                 AS \"EmpName\"     "; // -- 員工姓名
		sql += "      , PCO.\"EmpNo\"            AS \"Coorgnizer\"  "; // -- F3 員工代號
//		sql += "      , PCO.\"EmpClass\"         AS \"EmpClass\"    "; // -- F4 考核後職級
		sql += "	  , nvl(cd1.\"Item\",' ')     as  \"EmpClass\"	"; // -- F4 考核後職級
		sql += "      , PCO.\"EffectiveDate\"    AS \"EffectiveDate\" "; // --F15 生效日
		sql += "      , PCO.\"IneffectiveDate\"  AS \"IneffectiveDate\" "; // --F16 停效日
//		sql += "      , NVL(LCO.\"EmpClass\",' ') AS \"LastEmpClass\"  "; // -- 前季職級(考核前職級)
		sql += "	  , NVL(cd2.\"Item\",' ')        AS  \"LastEmpClass\"	"; // -- 前季職級(考核前職級)

		sql += " FROM COOFFICER PCO ";
		sql += " LEFT JOIN \"CdEmp\" EMP on EMP.\"EmployeeNo\" = PCO.\"EmpNo\" ";
		sql += " LEFT JOIN COOFFICERLAST LCO  ON LCO.\"EmpNo\" = PCO.\"EmpNo\" ";
		sql += "                             AND LCO.ROWNUMBER = 1 ";
		sql += " LEFT JOIN \"CdCode\" cd1 on cd1.\"DefCode\"= 'ClassType' and cd1.\"Code\" = PCO.\"EmpClass\"  ";
		sql += " LEFT JOIN \"CdCode\" cd2 on cd2.\"DefCode\"= 'ClassType' and cd2.\"Code\" = nvl(LCO.\"EmpClass\",' ')  ";
		sql += " WHERE PCO.ROWNUMBER = 1 ";
		sql += " ORDER BY \"DeptSeq\" ASC  ";
		sql += "        , \"DistItem\" ";
		sql += "        , \"AreaItem\" ";
		sql += "        , \"Coorgnizer\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startdate", effectiveDateS);
		query.setParameter("enddate", effectiveDateE);

		return this.convertToMap(query);
	}
}