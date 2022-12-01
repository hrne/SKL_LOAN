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

@Service("L2419ServiceImpl")
@Repository
public class L2419ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> doQuery(TitaVo titaVo) throws Exception {
		this.info("L2419ServiceImpl doQuery");

		int applNo = Integer.parseInt(titaVo.getParam("ApplNo"));

		this.info("L2419ServiceImpl applNo = " + applNo);

		String sql = "";
		sql += " SELECT ";
		sql += "   CF.\"ClCode1\" ";
		sql += " , CF.\"ClCode2\" ";
		sql += " , CF.\"ClNo\" ";
		sql += " , 'Y' AS \"InsertFlag\" ";
		sql += " , CM.\"ClTypeCode\" ";
		sql += " , CA.\"Zip3\" ";
		sql += " , CM.\"EvaAmt\" ";
		sql += " , CI.\"EvaNetWorth\" ";
		sql += " , CI.\"LVITax\" ";
		sql += " , CI.\"RentEvaValue\" ";
		sql += " , CI.\"RentPrice\" ";
		sql += " , CI.\"LoanToValue\" ";
		sql += " , CI.\"SettingDate\" ";
		sql += " , CI.\"SettingAmt\" ";
		sql += " , CI.\"ClaimDate\" ";
		sql += " , CASE ";
		sql += "     WHEN CF.\"ClCode1\" = 1 ";
		sql += "     THEN CB.\"IrCode\" ";
		sql += "   ELSE CL.\"IrCode\" END AS \"IrCode\" ";
		sql += " , CB.\"Road\" ";
		sql += " , CB.\"BdNo1\" ";
		sql += " , CB.\"BdNo2\" ";
		sql += " , CB.\"BdMainUseCode\" ";
		sql += " , CB.\"BdMtrlCode\" ";
		sql += " , CB.\"BdTypeCode\" ";
		sql += " , CB.\"BdDate\" ";
		sql += " , CB.\"TotalFloor\" ";
		sql += " , CB.\"FloorNo\" ";
		sql += " , CASE ";
		sql += "     WHEN CF.\"ClCode1\" = 1 ";
		sql += "     THEN CB.\"FloorArea\" ";
		sql += "   ELSE CL.\"Area\" END AS \"Area\" ";
		sql += " , CASE ";
		sql += "     WHEN CF.\"ClCode1\" = 1 ";
		sql += "     THEN CB.\"EvaUnitPrice\" ";
		sql += "   ELSE CL.\"EvaUnitPrice\" END AS \"EvaUnitPrice\" ";
		sql += " , CL.\"LandNo1\" ";
		sql += " , CL.\"LandNo2\" ";
		sql += " , IO.\"OrigInsuNo\" ";
		sql += " , IO.\"InsuCompany\" ";
		sql += " , IO.\"InsuTypeCode\" ";
		sql += " , IO.\"FireInsuCovrg\" ";
		sql += " , IO.\"EthqInsuCovrg\" ";
		sql += " , IO.\"FireInsuPrem\" ";
		sql += " , IO.\"EthqInsuPrem\" ";
		sql += " , IO.\"InsuStartDate\" ";
		sql += " , IO.\"InsuEndDate\" ";
		sql += " FROM \"ClFac\" CF ";
		sql += " LEFT JOIN \"ClMain\" CM  ";
		sql += "     ON CM.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "     AND CM.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "     AND CM.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"ClImm\" CI  ";
		sql += "     ON CI.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "     AND CI.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "     AND CI.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"ClBuilding\" CB  ";
		sql += "     ON CB.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "     AND CB.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "     AND CB.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"ClLand\" CL ";
		sql += "     ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "     AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "     AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"InsuOrignal\" IO ";
		sql += "     ON IO.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "     AND IO.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "     AND IO.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"CdArea\" CA ";
		sql += "     ON CA.\"CityCode\" = CM.\"CityCode\" ";
		sql += "     AND CA.\"AreaCode\" = CM.\"AreaCode\" ";
		sql += " WHERE CF.\"ApproveNo\" = :applNo ";
		sql += "   AND CF.\"ClCode1\" IN (1,2) ";
		sql += " ORDER BY \"ClCode1\" ";
		sql += "        , \"ClCode2\" ";
		sql += "        , \"ClNo\" ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);

		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doQueryOwner(String clCode1, String clCode2, String clNo, TitaVo titaVo)
			throws Exception {
		this.info("L2419ServiceImpl doQueryOwner");
		this.info("L2419ServiceImpl clCode1 = " + clCode1);
		this.info("L2419ServiceImpl clCode2 = " + clCode2);
		this.info("L2419ServiceImpl clNo = " + clNo);

		String sql = "";
		sql += " SELECT ";
		sql += "   '1'                 AS \"OwnerType\" ";
		sql += " , CM.\"CustId\"       AS \"CustId\" ";
		sql += " , CM.\"CustName\"     AS \"CustName\" ";
		sql += " , BO.\"OwnerRelCode\" AS \"OwnerRelCode\" ";
		sql += " , BO.\"OwnerPart\"    AS \"OwnerPart\" ";
		sql += " , BO.\"OwnerTotal\"   AS \"OwnerTotal\" ";
		sql += " FROM \"ClBuildingOwner\" BO ";
		sql += " LEFT JOIN \"CustMain\" CM ";
		sql += "     ON CM.\"CustUKey\" = BO.\"OwnerCustUKey\" ";
		sql += " WHERE BO.\"ClCode1\" = :clCode1 ";
		sql += "   AND BO.\"ClCode2\" = :clCode2 ";
		sql += "   AND BO.\"ClNo\" = :clNo ";
		sql += " UNION ALL ";
		sql += " SELECT ";
		sql += "   '2'                 AS \"OwnerType\" ";
		sql += " , CM.\"CustId\"       AS \"CustId\" ";
		sql += " , CM.\"CustName\"     AS \"CustName\" ";
		sql += " , LO.\"OwnerRelCode\" AS \"OwnerRelCode\" ";
		sql += " , LO.\"OwnerPart\"    AS \"OwnerPart\" ";
		sql += " , LO.\"OwnerTotal\"   AS \"OwnerTotal\" ";
		sql += " FROM \"ClLandOwner\" LO ";
		sql += " LEFT JOIN \"CustMain\" CM ";
		sql += "     ON CM.\"CustUKey\" = LO.\"OwnerCustUKey\" ";
		sql += " WHERE LO.\"ClCode1\" = :clCode1 ";
		sql += "   AND LO.\"ClCode2\" = :clCode2 ";
		sql += "   AND LO.\"ClNo\" = :clNo ";
		sql += " ORDER BY \"OwnerType\" ";
		sql += "        , \"OwnerRelCode\" ";
		sql += "        , \"CustId\" ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);

		query.setParameter("clCode1", clCode1);
		query.setParameter("clCode2", clCode2);
		query.setParameter("clNo", clNo);

		return this.convertToMap(query);
	}

}