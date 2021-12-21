package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l5R24ServiceImpl")
public class L5R24ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5R24ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(String AddressCode, String CustUKey, String ClNo, String ClCode1, String ClCode2, TitaVo titaVo) throws Exception {
		String sql = "";
		switch (AddressCode) {
		case "1":
			sql = "select a.\"RegZip3\"," + "a.\"RegZip2\"," + "b.\"CityItem\"," + "c.\"AreaItem\"," + "a.\"RegRoad\"," + "a.\"RegSection\"," + "a.\"RegAlley\"," + "a.\"RegLane\"," + "a.\"RegNum\","
					+ "a.\"RegNumDash\"," + "a.\"RegFloor\"," + "a.\"RegFloorDash\"" + " from \"CustMain\" a " + "left join \"CdCity\" b on b.\"CityCode\" = a.\"RegCityCode\" "
					+ "left join \"CdArea\" c on c.\"CityCode\" = a.\"RegCityCode\" and c.\"AreaCode\" = a.\"RegAreaCode\" " + "where a.\"CustUKey\" = '" + CustUKey + "'";
			break;
		case "2":
			sql = "select a.\"CurrZip3\"," + "a.\"CurrZip2\"," + "b.\"CityItem\"," + "c.\"AreaItem\"," + "a.\"CurrRoad\"," + "a.\"CurrSection\"," + "a.\"CurrAlley\"," + "a.\"CurrLane\","
					+ "a.\"CurrNum\"," + "a.\"CurrNumDash\"," + "a.\"CurrFloor\"," + "a.\"CurrFloorDash\"" + " from \"CustMain\" a " + "left join \"CdCity\" b on b.\"CityCode\" = a.\"CurrCityCode\" "
					+ "left join \"CdArea\" c on c.\"CityCode\" = a.\"CurrCityCode\" and c.\"AreaCode\" = a.\"CurrAreaCode\"" + "where a.\"CustUKey\" = '" + CustUKey + "'";
			break;
		case "3":
			sql = "select b.\"CityItem\"," + "c.\"AreaItem\"," + "d.\"IrItem\"," + "a.\"Road\"," + "a.\"Section\"," + "a.\"Alley\"," + "a.\"Lane\"," + "a.\"Num\"," + "a.\"NumDash\"," + "a.\"Floor\","
					+ "a.\"FloorDash\"" + " from \"ClBuilding\" a " + "left join \"CdCity\" b on b.\"CityCode\" = a.\"CityCode\" "
					+ "left join \"CdArea\" c on c.\"CityCode\" = a.\"CityCode\" and c.\"AreaCode\" = a.\"AreaCode\" "
					+ "left join \"CdLandSection\" d on d.\"CityCode\" = a.\"CityCode\" and d.\"AreaCode\" = a.\"AreaCode\" and d.\"IrCode\" = a.\"IrCode\" " + "where a.\"ClNo\" = '" + ClNo + "'"
					+ " and a.\"ClCode1\" = '" + ClCode1 + "'" + " and a.\"ClCode2\" = '" + ClCode2 + "'";
			break;
		}

		logger.info("sql = " + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		logger.info("L5R24Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
