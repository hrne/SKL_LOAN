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
public class L9110ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryBuilding(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryBuilding");

		// -- L9110 自然人/法人Query(建物)
		String sql = "";
		sql += " SELECT  * FROM  ";
		sql += " ( SELECT ROW_NUMBER() OVER (PARTITION BY CF.\"ClCode1\"";
		sql += "                                     , CF.\"ClCode2\"";
		sql += "                                     , CF.\"ClNo\"";
		sql += "                                     , LO.\"Owner\"";
		sql += "                          ORDER BY LO.\"OwnerPart\" DESC ";
		sql += "                         )              AS SEQ "; // -- 序號 F0
		sql += "      , LO.\"Owner\"                    AS Owner "; // -- 提供人 F1
		sql += "      , LPAD(NVL(L.\"BdNo1\",0),5,'0') ";
		sql += "        || '-' ";
		sql += "        || LPAD(NVL(L.\"BdNo2\",0),3,'0')      AS BdNo "; // -- 主建物建號 F2
		sql += "      , LPAD(NVL(CBP1.\"PublicBdNo1\",0),5,'0') ";
		sql += "        || '-' ";
		sql += "        || LPAD(NVL(CBP1.\"PublicBdNo2\",0),3,'0') ";
		sql += "                                        AS PublicBdNo ";// -- 公設建號 F3
		sql += "      , NVL(L.\"FloorArea\",0)          AS BdArea "; // -- 主建物 F4
		sql += "      , NVL(CBP2.\"PublicArea\",0)      AS PublicArea "; // -- 公設 F5
		sql += "      , CASE ";
		sql += "          WHEN CF.\"ClCode1\" = 1 AND CF.\"ClCode2\" = 5 "; // -- 獨立產權車位時，車位跟主建物面積顯示一樣
		sql += "          THEN NVL(L.\"FloorArea\",0) ";
		sql += "        ELSE 0 END                      AS ParkingArea "; // -- 車位 F6
		sql += "      , L.\"EvaUnitPrice\"              AS EvaUnitPrice "; // -- 鑑定單價 F7
		sql += "      , CLI.\"SettingAmt\"              AS SettingAmt "; // -- 設定 F8
		sql += "      , L.\"BdLocation\"                AS BdLocation "; // -- 門牌地址 F9
		sql += "      , NVL(SUBSTR(L.\"SellerName\",0,6),'無') ";
		sql += "                                        AS SellerName "; // -- 賣方姓名 F10
		sql += "      , NVL(L.\"SellerId\",'無')         AS SellerId "; // -- 賣方ID F11
		sql += "      , NVL(L.\"BdSubArea\",0)          AS BdSubArea "; // -- 附屬建物 F12
		sql += "      , LPAD(L.\"ClCode1\",1,'0')		AS ClCode1 "; // -- F13 擔保品代號1
		sql += "      , LPAD(L.\"ClCode2\",2,'0')		AS ClCode2 "; // -- F14 擔保品代號2
		sql += "      , LPAD(L.\"ClNo\",7,'0')			AS ClNo "; // -- F15 擔保品編號
		sql += "      , CDC1.\"Item\"				AS ClItem "; // -- F16 擔保品別
		sql += " FROM \"ClFac\" CF ";
		sql += " LEFT JOIN \"ClBuilding\" L ON L.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                           AND L.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                           AND L.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN (SELECT NVL(CM.\"CustId\",' ') || NVL(SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0),0,15),' ') AS \"Owner\"";
		sql += "                 , LO.\"ClCode1\"";
		sql += "                 , LO.\"ClCode2\"";
		sql += "                 , LO.\"ClNo\"";
		sql += "                 , LO.\"OwnerPart\"";
		sql += "            FROM \"ClBuildingOwner\" LO ";
		sql += "            LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = LO.\"OwnerCustUKey\"";
		sql += "           ) LO ON LO.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "               AND LO.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "               AND LO.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN \"ClImm\" CLI ON CLI.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                        AND CLI.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                        AND CLI.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                  , \"ClCode2\"";
		sql += "                  , \"ClNo\"";
		sql += "                  , \"PublicBdNo1\"";
		sql += "                  , \"PublicBdNo2\"";
		sql += "                  , ROW_NUMBER() OVER (PARTITION BY \"ClCode1\"";
		sql += "                                                  , \"ClCode2\"";
		sql += "                                                  , \"ClNo\"";
		sql += "                                       ORDER BY \"PublicBdNo1\"";
		sql += "                                      ) AS \"Seq\"";
		sql += "             FROM \"ClBuildingPublic\"";
		sql += "           ) CBP1 ON CBP1.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                 AND CBP1.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                 AND CBP1.\"ClNo\"    = CF.\"ClNo\"";
		sql += "                 AND CBP1.\"Seq\"     = 1 ";
		sql += " LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                  , \"ClCode2\"";
		sql += "                  , \"ClNo\"";
		sql += "                  , SUM(\"Area\") AS \"PublicArea\"";
		sql += "             FROM \"ClBuildingPublic\"";
		sql += "             GROUP BY \"ClCode1\"";
		sql += "                    , \"ClCode2\"";
		sql += "                    , \"ClNo\"";
		sql += "           ) CBP2 ON CBP2.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                 AND CBP2.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                 AND CBP2.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                  , \"ClCode2\"";
		sql += "                  , \"ClNo\"";
		sql += "                  , SUM(\"ParkingArea\") AS \"ParkingArea\"";
		sql += "             FROM \"ClParking\"";
		sql += "             GROUP BY \"ClCode1\"";
		sql += "                    , \"ClCode2\"";
		sql += "                    , \"ClNo\"";
		sql += "           ) CBPK ON CBPK.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                 AND CBPK.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                 AND CBPK.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'ClCode2' || CF.\"ClCode1\"";
		sql += "                        AND CDC1.\"Code\"    = LPAD(CF.\"ClCode2\",2,'0')";
		sql += " WHERE CF.\"ApproveNo\" = :applNo";
		sql += "   AND NVL(L.\"ClNo\",0) > 0 ";
		sql += " ) WHERE SEQ = 1   ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryBuildingLand(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryBuildingLand");

		// -- L9110 自然人/法人Query(建物土地)
		String sql = "";

		sql += " SELECT ";
		sql += "     ROW_NUMBER() OVER( ";
		sql += "                 PARTITION BY S1.\"ClCode1\", S1.\"ClCode2\", S1.\"ClNo\" ";
		sql += "                 ORDER BY ";
		sql += "                     S1.\"LandSeq\" ASC ";
		sql += "             ) AS \"F0Seq\"  ";
		sql += " ,S1.\"BdNo\" AS \"F1建號\"   ";
		sql += " ,S1.\"Owner\" AS \"F2擔保品提供人\"    ";
		sql += " ,S1.\"Location\" AS \"F3門牌坐落\"   ";
		sql += " ,S1.\"BdClItem\" AS \"F4擔保品別\"    ";
		sql += " ,S1.\"BdPublicDdNo\" AS \"F5公社建號\"    ";
		sql += " ,CASE WHEN ROW_NUMBER() OVER( ";
		sql += "                 PARTITION BY S1.\"ClCode1\", S1.\"ClCode2\", S1.\"ClNo\" ";
		sql += "                 ORDER BY ";
		sql += "                     S1.\"LandSeq\" ASC ";
		sql += "             ) = 1 THEN S1.\"BdArea\" ELSE 0 END AS \"F6主建物面積\"   ";
		sql += " ,CASE WHEN ROW_NUMBER() OVER( ";
		sql += "                 PARTITION BY S1.\"ClCode1\", S1.\"ClCode2\", S1.\"ClNo\" ";
		sql += "                 ORDER BY ";
		sql += "                     S1.\"LandSeq\" ASC ";
		sql += "             ) = 1 THEN S1.\"BdSubArea\" ELSE 0 END  AS \"F7附屬建物\"    ";
		sql += " ,CASE WHEN ROW_NUMBER() OVER( ";
		sql += "                 PARTITION BY S1.\"ClCode1\", S1.\"ClCode2\", S1.\"ClNo\" ";
		sql += "                 ORDER BY ";
		sql += "                     S1.\"LandSeq\" ASC ";
		sql += "             ) = 1 THEN S1.\"BdPublicArea\" ELSE 0 END  AS \"F8公設面積\"  ";
		sql += " ,CASE WHEN ROW_NUMBER() OVER( ";
		sql += "                 PARTITION BY S1.\"ClCode1\", S1.\"ClCode2\", S1.\"ClNo\" ";
		sql += "                 ORDER BY ";
		sql += "                     S1.\"LandSeq\" ASC ";
		sql += "             ) = 1 THEN S1.\"BdParkingArea\" ELSE 0 END  AS \"F9車位\"   ";
		sql += " ,CASE WHEN ROW_NUMBER() OVER( ";
		sql += "                 PARTITION BY S1.\"ClCode1\", S1.\"ClCode2\", S1.\"ClNo\" ";
		sql += "                 ORDER BY ";
		sql += "                     S1.\"LandSeq\" ASC ";
		sql += "             ) = 1 THEN S1.\"BdSettingAmt\" ELSE 0 END  AS \"F10設定金額\"   ";
		sql += " ,S1.\"LandArea\" AS \"F11土地面積\"   ";
		sql += " ,S1.\"LandSettingAmt\" AS \"F12土地設定\"  ";
		sql += " ,S1.\"ClCode1\" AS \"F13ClCode1\"    ";
		sql += " ,S1.\"ClCode2\" AS \"F14ClCode2\"    ";
		sql += " ,S1.\"ClNo\" AS \"F15ClNo\"    ";
		sql += " ,S1.\"BdEvaunitprice\" AS F16鑑定單價   ";
		sql += " ,S1.\"BdSellerName\" AS F17賣方姓名   ";
		sql += " ,S1.\"BdSellerId\" AS \"F18賣方ID\"    ";
		sql += " ,S1.\"LandSeq\" AS \"F19LandSeq\"   ";
		sql += " ,S1.\"LandCityItem\" AS F20土地縣市    ";
		sql += " ,S1.\"LandAreaItem\" AS F21土地鄉鎮區   ";
		sql += " ,S1.\"LandIrItem\" AS F22土地段小段    ";
		sql += " ,S1.\"LandLandNo\" AS F23土地地號   ";
		sql += " ,S1.\"LandTransFeredYear\" AS F24土地年度   ";
		sql += " ,S1.\"LandLastTransferedAmt\" AS F25土地前次移轉   ";
		sql += " ,S1.\"LandEvaunitprice\" AS F26土地鑑定單價  ";
		sql += " ,S1.\"LandOwnerPart\" AS F27土地持份比例    ";
		sql += " ,S1.\"LandOwnerTotal\" AS F28土地持份總比例    ";
		sql += " ,S1.\"LandClItem\" AS F29土地擔保品別  ";
		sql += " FROM ";
		sql += "     (( ";
		sql += "         SELECT ";
		sql += "             ";
		sql += "             lo.\"Owner\"         AS \"Owner\",   ";
		sql += "             l.\"BdLocation\"     AS \"Location\"   ";
		sql += "             , ";
		sql += "             lpad(nvl(l.\"BdNo1\", 0), 5, '0') ";
		sql += "             || '-' ";
		sql += "             || lpad(nvl(l.\"BdNo2\", 0), 3, '0') AS \"BdNo\",   ";
		sql += "             lpad(nvl(cbp1.\"PublicBdNo1\", 0), 5, '0') ";
		sql += "             || '-' ";
		sql += "     || lpad(nvl(cbp1.\"PublicBdNo2\", 0), 3, '0') AS \"BdPublicDdNo\" ,   ";
		sql += "             nvl(l.\"FloorArea\", 0) AS \"BdArea\",  ";
		sql += "             nvl(cbp2.\"PublicArea\", 0) AS \"BdPublicArea\",   ";
		sql += "             CASE ";
		sql += "                 WHEN cf.\"ClCode1\" = 1 ";
		sql += "              AND cf.\"ClCode2\" = 5           THEN ";
		sql += "             nvl(l.\"FloorArea\", 0) ";
		sql += "                 ELSE ";
		sql += "                     0 END AS \"BdParkingArea\",   ";
		sql += "             l.\"EvaUnitPrice\"   AS \"BdEvaunitprice\",   ";
		sql += "             cli.\"SettingAmt\"   AS \"BdSettingAmt\",   ";
		sql += "             TO_CHAR(nvl(substr(l.\"SellerName\", 0, 6), '無')) AS \"BdSellerName\",  ";
		sql += "             nvl(l.\"SellerId\", '無') AS \"BdSellerId\",   ";
		sql += "             nvl(l.\"BdSubArea\",0) AS \"BdSubArea\" ,   ";
		sql += "             lpad(l.\"ClCode1\", 1, '0') AS \"ClCode1\",   ";
		sql += "             lpad(l.\"ClCode2\", 2, '0') AS \"ClCode2\",   ";
		sql += "             lpad(l.\"ClNo\", 7, '0') AS \"ClNo\",   ";
		sql += "             0 AS \"LandSeq\",   ";
		sql += "             TO_CHAR(cdc1.\"Item\")        AS \"BdClItem\",   ";
		sql += "             ' ' AS \"LandCityItem\" ,  ";
		sql += "             ' ' AS \"LandAreaItem\",  ";
		sql += "             ' ' AS \"LandIrItem\" ,  ";
		sql += "             ' ' AS \"LandLandNo\",  ";
		sql += "             0 AS \"LandArea\",  ";
		sql += "             0 AS \"LandTransFeredYear\",  ";
		sql += "             0 AS \"LandLastTransferedAmt\",  ";
		sql += "             0 AS \"LandEvaunitprice\",  ";
		sql += "             0 AS \"LandSettingAmt\",  ";
		sql += "             0 AS \"LandOwnerPart\" ,  ";
		sql += "             0 AS \"LandOwnerTotal\",  ";
		sql += "             ' ' AS \"LandClItem\"   ";
		sql += "         FROM ";
		sql += "             \"ClFac\"        cf ";
		sql += "             LEFT JOIN \"ClBuilding\"   l ON l.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                                         AND l.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                                         AND l.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN ( ";
		sql += "                 SELECT ";
		sql += "                     nvl(cm.\"CustId\", ' ') ";
		sql += "                     || nvl(substr(\"Fn_ParseEOL\"(cm.\"CustName\", 0), 0, 15), ' ') AS \"Owner\", ";
		sql += "                     lo.\"ClCode1\", ";
		sql += "                     lo.\"ClCode2\", ";
		sql += "                     lo.\"ClNo\", ";
		sql += "                     lo.\"OwnerPart\" ";
		sql += "                 FROM ";
		sql += "                     \"ClBuildingOwner\"   lo ";
		sql += "                     LEFT JOIN \"CustMain\"          cm ON cm.\"CustUKey\" = lo.\"OwnerCustUKey\" ";
		sql += "             ) lo ON lo.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                     AND lo.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                     AND lo.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN \"ClImm\"        cli ON cli.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                                      AND cli.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                                      AND cli.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN ( ";
		sql += "                 SELECT ";
		sql += "                     \"ClCode1\", ";
		sql += "                     \"ClCode2\", ";
		sql += "                     \"ClNo\", ";
		sql += "                     \"PublicBdNo1\", ";
		sql += "                     \"PublicBdNo2\", ";
		sql += "                     ROW_NUMBER() OVER( ";
		sql += "                         PARTITION BY \"ClCode1\", \"ClCode2\", \"ClNo\" ";
		sql += "                         ORDER BY ";
		sql += "                             \"PublicBdNo1\" ";
		sql += "                     ) AS \"Seq\" ";
		sql += "                 FROM ";
		sql += "                     \"ClBuildingPublic\" ";
		sql += "             ) cbp1 ON cbp1.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                       AND cbp1.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                       AND cbp1.\"ClNo\" = cf.\"ClNo\" ";
		sql += "                       AND cbp1.\"Seq\" = 1 ";
		sql += "             LEFT JOIN ( ";
		sql += "                 SELECT ";
		sql += "                     \"ClCode1\", ";
		sql += "                     \"ClCode2\", ";
		sql += "                     \"ClNo\", ";
		sql += "                     SUM(\"Area\") AS \"PublicArea\" ";
		sql += "                 FROM ";
		sql += "                     \"ClBuildingPublic\" ";
		sql += "                 GROUP BY ";
		sql += "                     \"ClCode1\", ";
		sql += "                     \"ClCode2\", ";
		sql += "                     \"ClNo\" ";
		sql += "             ) cbp2 ON cbp2.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                       AND cbp2.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                       AND cbp2.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN ( ";
		sql += "                 SELECT ";
		sql += "                     \"ClCode1\", ";
		sql += "                     \"ClCode2\", ";
		sql += "                     \"ClNo\", ";
		sql += "                     SUM(\"ParkingArea\") AS \"ParkingArea\" ";
		sql += "                 FROM ";
		sql += "                     \"ClParking\" ";
		sql += "                 GROUP BY ";
		sql += "                     \"ClCode1\", ";
		sql += "                     \"ClCode2\", ";
		sql += "                     \"ClNo\" ";
		sql += "             ) cbpk ON cbpk.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                       AND cbpk.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                       AND cbpk.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN \"CdCode\"       cdc1 ON cdc1.\"DefCode\" = 'ClCode2' || cf.\"ClCode1\" ";
		sql += "                                        AND cdc1.\"Code\" = lpad(cf.\"ClCode2\", 2, '0') ";
		sql += "         WHERE ";
		sql += "             cf.\"ApproveNo\" = :applNo ";
		sql += "             AND nvl(l.\"ClNo\", 0) > 0 ";
		sql += "     ) ";
		sql += " UNION ALL ";
		sql += " ( SELECT ";
		sql += "     * ";
		sql += " FROM ";
		sql += "     ( ";
		sql += "         SELECT ";
		sql += "             ";
		sql += "             lo.\"Owner\"              AS \"Owner\",   ";
		sql += "             l.\"LandLocation\"        AS \"Location\" , ";
		sql += "             ' ' AS \"BdNo\",   ";
		sql += "             ' ' AS \"BdPublicBdno\",   ";
		sql += "             0 AS \"BdBdArea\",   ";
		sql += "             0 AS \"BdPublicArea\",   ";
		sql += "             0 AS \"BdParkingArea\",   ";
		sql += "             0 AS \"BdEvaunitprice\",   ";
		sql += "             0 AS \"BdSettingAmt\",   ";
		sql += "             TO_CHAR('') AS \"BdSellerName\",   ";
		sql += "             '' AS \"BdSellerId\",   ";
		sql += "             0 AS \"BdSubArea\" ,  ";
		sql += "             TO_CHAR(lpad(cf.\"ClCode1\", 1, '0')) AS \"ClCode1\",   ";
		sql += "             TO_CHAR(lpad(cf.\"ClCode2\", 2, '0')) AS \"ClCode2\",   ";
		sql += "             TO_CHAR(lpad(cf.\"ClNo\", 7, '0')) AS \"ClNo\",   ";
		sql += "             l.\"LandSeq\" AS \"LandSeq\",   ";
		sql += "             ' ' AS \"BdClItem\",   ";
		sql += "             TO_CHAR(city.\"CityItem\")         AS \"LandCityItem\",   ";
		sql += "             TO_CHAR(area.\"AreaItem\")         AS \"LandAreaItem\", ";
		sql += "             TO_CHAR(cdls.\"IrItem\")           AS \"LandIrItem\" , ";
		sql += "             TO_CHAR(lpad(l.\"LandNo1\", 4, '0') ";
		sql += "             || '-' ";
		sql += "             || lpad(l.\"LandNo2\", 4, '0')) AS \"LandLandNo\", ";
		sql += "             nvl(l.\"Area\", 0) AS \"LandArea\", ";
		sql += "             l.\"TransferedYear\"      AS \"LandTransferedYear\", ";
		sql += "             l.\"LastTransferedAmt\"   AS \"LandLastTransferedAmt\", ";
		sql += "             l.\"EvaUnitPrice\"        AS \"LandEvaunitprice\", ";
		sql += "             cli.\"SettingAmt\"        AS \"LandSettingAmt\", ";
		sql += "             nvl(lo.\"OwnerPart\", 0) AS \"LandOwnerPart\" , ";
		sql += "             nvl(lo.\"OwnerTotal\", 0) AS \"LandOwnerTotal\", ";
		sql += "             TO_CHAR(cdc1.\"Item\" )            AS \"LandClItem\"   ";
		sql += "         FROM ";
		sql += "             \"ClFac\"           cf ";
		sql += "             LEFT JOIN \"ClLand\"          l ON l.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                                     AND l.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                                     AND l.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN ( ";
		sql += "                 SELECT ";
		sql += "                     nvl(cm.\"CustId\", ' ') ";
		sql += "                     || nvl(substr(\"Fn_ParseEOL\"(cm.\"CustName\", 0), 0, 7), ' ') AS \"Owner\", ";
		sql += "                     lo.\"ClCode1\", ";
		sql += "                     lo.\"ClCode2\", ";
		sql += "                     lo.\"ClNo\", ";
		sql += "                     lo.\"LandSeq\", ";
		sql += "                     lo.\"OwnerPart\", ";
		sql += "                     lo.\"OwnerTotal\" ";
		sql += "                 FROM ";
		sql += "                     \"ClLandOwner\"   lo ";
		sql += "                     LEFT JOIN \"CustMain\"      cm ON cm.\"CustUKey\" = lo.\"OwnerCustUKey\" ";
		sql += "             ) lo ON lo.\"ClCode1\" = l.\"ClCode1\" ";
		sql += "                     AND lo.\"ClCode2\" = l.\"ClCode2\" ";
		sql += "                     AND lo.\"ClNo\" = l.\"ClNo\" ";
		sql += "                     AND lo.\"LandSeq\" = l.\"LandSeq\" ";
		sql += "             LEFT JOIN \"CdCity\"          city ON city.\"CityCode\" = l.\"CityCode\" ";
		sql += "             LEFT JOIN \"CdArea\"          area ON area.\"CityCode\" = l.\"CityCode\" ";
		sql += "                                        AND area.\"AreaCode\" = l.\"AreaCode\" ";
		sql += "             LEFT JOIN \"CdLandSection\"   cdls ON cdls.\"CityCode\" = l.\"CityCode\" ";
		sql += "                                               AND cdls.\"AreaCode\" = l.\"AreaCode\" ";
		sql += "                                               AND cdls.\"IrCode\" = l.\"IrCode\" ";
		sql += "             LEFT JOIN \"ClImm\"           cli ON cli.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                                      AND cli.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                                      AND cli.\"ClNo\" = cf.\"ClNo\" ";
		sql += "             LEFT JOIN \"CdCode\"          cdc1 ON cdc1.\"DefCode\" = 'ClCode2' || cf.\"ClCode1\" ";
		sql += "                                        AND cdc1.\"Code\" = lpad(cf.\"ClCode2\", 2, '0') ";
		sql += "         WHERE ";
		sql += "             cf.\"ApproveNo\" = :applNo ";
		sql += "             AND nvl(l.\"ClNo\", 0) > 0 ";
		sql += "     ) ";
		sql += " ) ";
		sql += " ) S1 ";
		sql += " order by S1.\"ClCode1\" ASC ,S1.\"ClCode2\" ASC , S1.\"ClNo\" ASC  ";


		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryCl(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryCl ");

		// -- L9110 Query(擔保品)
		String sql = " SELECT CASE";
		sql += "                WHEN CF.\"ClCode1\" IN ('1','2')";
		sql += "                THEN '不動產'";
		sql += "                WHEN CF.\"ClCode1\" IN ('3','4')";
		sql += "                THEN '股票'";
		sql += "                WHEN CF.\"ClCode1\" = '5'";
		sql += "                THEN '其他'";
		sql += "                WHEN CF.\"ClCode1\" = '9'";
		sql += "                THEN '動產'";
		sql += "              ELSE ' ' END             AS Collateral "; // -- F0 擔保品資料 *** 原\"押品資料\"
		sql += "            , LPAD(CF.\"ClCode1\",1,'0') ";
		sql += "              || '-' || LPAD(CF.\"ClCode2\",2,'0') ";
		sql += "              || '-' || LPAD(CF.\"ClNo\",7,'0') ";
		sql += "                                       AS ClNo "; // -- F1 擔保品號碼 原\"押品號碼\"
		sql += "            , CDC1.\"Item\"            AS ClItem "; // -- F2 擔保品別 原\"押品別\"
		sql += "            , CLM.\"EvaDate\"          AS EvaDate "; // -- F3 鑑價日期
		sql += "            , CLI2.\"ClaimDate\"        AS OtherDate "; // -- F4 他項存續期限 抓最新一筆
		sql += "            , CLI.\"SettingSeq\"       AS SettingSeq "; // -- F5 順位 只有不動產會有此欄位
		sql += "            , CLIRD.\"FirstAmt\"       AS FirstAmt "; // -- F6 前順位金額 只有不動產會有此欄位
		sql += "            , CITY.\"CityItem\"        AS CityItem "; // -- F7 地區別
		sql += "            , CDC2.\"Item\"            AS EvaCompany "; // -- F8 鑑定公司 只有不動產會有此欄位
		sql += "            , CLI.\"BdRmk\"            AS BdRmk "; // -- F9 建物標示備註 只有不動產會有此欄位
		sql += "            , CASE";
		sql += "                WHEN CF.\"ClCode1\" IN ('1','2')";
		sql += "                THEN CLI.\"SettingDate\" ";
		sql += "                WHEN CF.\"ClCode1\" IN ('3','4')";
		sql += "                THEN CLS.\"SettingDate\" ";
		sql += "                WHEN CF.\"ClCode1\" = '5'";
		sql += "                THEN CLO.\"SettingDate\" ";
		sql += "                WHEN CF.\"ClCode1\" = '9'";
		sql += "                THEN CLMOV.\"SettingDate\" ";
		sql += "              ELSE 0 END               AS SettingDate "; // -- F10 設定日期
		sql += "            , CF.\"ClCode1\"           AS ClCode1  "; // -- F11 擔保品代號1
		sql += "            , CLS.\"StockCode\"";
		sql += "              || ' '";
		sql += "              || CDS.\"StockItem\"     AS StockName "; // F12 股票代號及股票名稱
		sql += "            , NVL(CLS.\"PledgeNo\",'無')         AS PledgeNo "; // F13 質權設定書號
		sql += "            , CLS.\"ThreeMonthAvg\"    AS ThreeMonthAvg ";// F14 三個月平均價
		sql += "            , CLS.\"YdClosingPrice\"   AS YdClosingPrice "; // F15 前日收盤價
		sql += "            , CLS.\"EvaUnitPrice\"     AS EvaUnitPrice "; // F16 鑑定單價
		sql += "            , CLM.\"EvaAmt\"           AS EvaAmt ";// F17 鑑定總價
		sql += "            , CLS.\"LoanToValue\"      AS LTV "; // F18 貸放成數
		sql += "            , FAC.\"LineAmt\"          AS LineAmt "; // F19 核准額度
		sql += "            , NVL(CLS.\"CustodyNo\",'無')        AS CustodyNo ";// F20 保管條號碼
		sql += "            , CLI.\"EvaNetWorth\"      AS EvaNetWorth ";// F21 評估淨值
		sql += "            , CLI2.\"SettingAmt\"       AS SettingAmt ";// F22 設定金額
		sql += "       FROM \"ClFac\" CF";
		sql += "       LEFT JOIN \"FacMain\" FAC ON FAC.\"ApplNo\" = CF.\"ApproveNo\"";
		sql += "       LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'ClCode2' || CF.\"ClCode1\"";
		sql += "                                AND CDC1.\"Code\"    = LPAD(CF.\"ClCode2\",2,'0')";
		sql += "       LEFT JOIN \"ClMain\" CLM ON CLM.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                               AND CLM.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                               AND CLM.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"ClImm\" CLI ON CLI.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                              AND CLI.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                              AND CLI.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"ClImmRankDetail\" CLIRD ON CLIRD.\"ClCode1\"    = CLI.\"ClCode1\" ";
		sql += "                                          AND CLIRD.\"ClCode2\"    = CLI.\"ClCode2\" ";
		sql += "                                          AND CLIRD.\"ClNo\"       = CLI.\"ClNo\" ";
		sql += "                                          AND CLIRD.\"SettingSeq\" = CLI.\"SettingSeq\"";
		sql += "       LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = CLM.\"CityCode\"";
		sql += "       LEFT JOIN \"ClStock\" CLS ON CLS.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                AND CLS.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                AND CLS.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"CdStock\" CDS ON CDS.\"StockCode\" = CLS.\"StockCode\"";
		sql += "       LEFT JOIN \"ClOther\" CLO ON CLO.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                AND CLO.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                AND CLO.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"ClMovables\" CLMOV ON CLMOV.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                     AND CLMOV.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                     AND CLMOV.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"CdCode\" CDC2 ON CDC2.\"DefCode\" = 'EvaCompanyCode'";
		sql += "                                AND CDC2.\"Code\" = NVL(CLI.\"EvaCompanyCode\",' ')";
		sql += "       LEFT JOIN (SELECT MAX(cli.\"ClaimDate\") AS \"ClaimDate\" ,\"ApproveNo\" AS \"ApplNo\" , SUM(cli.\"SettingAmt\") AS \"SettingAmt\" FROM \"ClFac\" cf2"
				+ "		LEFT JOIN \"ClImm\" cli ON cli.\"ClCode1\" = cf2.\"ClCode1\" AND cli.\"ClCode2\" = cf2.\"ClCode2\" AND cli.\"ClNo\" = cf2.\"ClNo\"  "
				+ "		WHERE cf2.\"ApproveNo\" = :applNo"
				+ "		GROUP BY \"ApproveNo\") CLI2  ON CLI2.\"ApplNo\" =  CF.\"ApproveNo\" ";

		sql += "       WHERE CF.\"ApproveNo\" = :applNo";
//		sql += "         AND CF.\"MainFlag\" = 'Y' "; // -- 主要擔保品

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryCrossUse(TitaVo titaVo, String applNo) {

		this.info("L9110 queryCrossUse");

		String sql = "";
		sql += " SELECT \"Fn_GetCdCode\"('SubCompanyCode',CC.\"SubCompanyCode\") ";
		sql += "                          AS SubCompany "; // F0 子公司名稱
		sql += "      , CC.\"CrossUse\"   AS CrossUse "; // F1 同意使用
		sql += " FROM \"FacCaseAppl\" FCA ";
		sql += " LEFT JOIN \"CustCross\" CC ON CC.\"CustUKey\" = FCA.\"CustUKey\" ";
		sql += " WHERE CC.\"CrossUse\" = 'Y' "; // 同意使用
		sql += "   AND FCA.\"ApplNo\" = :applNo "; // 申請號碼
		sql += " ORDER BY CC.\"SubCompanyCode\" "; // 子公司代碼

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryCustTelNo(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110.findAll queryCustTelNo");

		String sql = "";
		sql += " SELECT \"Fn_GetCdCode\"('TelTypeCode',CTN.\"TelTypeCode\") ";
		sql += "                           AS TelType "; // -- F0 電話種類
		sql += "      , CTN.\"TelArea\"    AS TelArea "; // -- F1 電話區碼
		sql += "      , CTN.\"TelNo\"      AS TelNo "; // -- F2 電話號碼
		sql += "      , CTN.\"TelExt\"     AS TelExt "; // -- F3 分機號碼
		sql += "      , \"Fn_GetCdCode\"('RelationCode',CTN.\"RelationCode\") ";
		sql += "                           AS Relation "; // -- F4 與借款人關係
		sql += "      , CASE ";
		sql += "          WHEN CTN.\"RelationCode\" = '00' ";
		sql += "          THEN SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0),0,10) ";
		sql += "        ELSE SUBSTR(CTN.\"LiaisonName\",0,10) ";
		sql += "        END                AS LiaisonName "; // -- F5 聯絡人姓名
		sql += " FROM \"FacCaseAppl\" FCA ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = FCA.\"CustUKey\" ";
		sql += " LEFT JOIN \"CustTelNo\" CTN ON CTN.\"CustUKey\" = FCA.\"CustUKey\" ";
		sql += " WHERE CTN.\"Enable\" = 'Y' "; // -- 啟用
		sql += "   AND FCA.\"ApplNo\" = :applNo "; // -- 申請號碼
		sql += " ORDER BY CTN.\"TelTypeCode\" "; // -- 電話種類
		sql += "        , CTN.\"RelationCode\" "; // -- 與借款戶關係

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryGua(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110.findAll queryGua");

		String sql = "";
		sql += " SELECT GUACM.\"CustId\"     AS GuaId "; // -- F0 保證人統編
		sql += "      , SUBSTR(\"Fn_ParseEOL\"(GUACM.\"CustName\", 0),0,15)   AS GuaName "; // -- F1 保證人姓名
		sql += "      , CDG.\"GuaRelItem\"   AS GuaRelItem "; // -- F2 保證人關係
		sql += "      , GUA.\"GuaAmt\"       AS GuaAmt "; // -- F3 保證金額
		sql += "      , \"Fn_GetCustAddr\"(GUACM.\"CustUKey\",'1') ";
		sql += "                             AS GuaAddr "; // -- F4 保證人通訊地址
		sql += "      , NVL(GUACM.\"CurrZip3\",'') || NVL(GUACM.\"CurrZip2\",'') ";
		sql += "                             AS GuaZip  "; // -- F5 保證人郵遞區號(通訊地址)
		sql += "      , \"Fn_GetCdCode\"('GuaTypeCode',GUA.\"GuaTypeCode\") ";
		sql += "                             AS GuaType "; // -- F6 保證類別
		sql += "      , ROW_NUMBER() OVER (PARTITION BY GUA.\"ApproveNo\" ORDER BY GUA.\"GuaAmt\" DESC) ";
		sql += "                             AS Seq ";
		sql += " FROM \"Guarantor\" GUA ";
		// 客戶資料主檔(保證人)
		sql += " LEFT JOIN \"CustMain\" GUACM ON GUACM.\"CustUKey\" = GUA.\"GuaUKey\" ";
		// 保證人關係代碼檔
		sql += " LEFT JOIN \"CdGuarantor\" CDG ON CDG.\"GuaRelCode\" = GUA.\"GuaRelCode\" ";
		sql += " WHERE GUA.\"GuaStatCode\" = '1' "; // -- 設定
		// *** ApproveNo 將會統一改為 ApplNo
		sql += "   AND GUA.\"ApproveNo\" = :applNo "; // -- 申請號碼
		sql += " ORDER BY Seq "; // -- 申請號碼

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryInsu(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryInsu");

		// -- L9110 法人Query(火險)
		String sql = "";
		sql += " SELECT   ";
		sql += "        IR.\"NowInsuNo\"      AS \"NowInsuNo\" "; // -- 保單號碼
		sql += "      , IR.\"FireInsuAmt\"    AS \"FireInsuAmt\" "; // -- 火險金額
		sql += "      , IR.\"InsuStartDate\"  AS \"InsuStartDate\" "; // -- 保險起日
		sql += "      , IR.\"InsuEndDate\"    AS \"InsuEndDate\" "; // -- 保險迄日
		sql += "      , CDC1.\"Item\"    AS \"InsuCompany\" "; // -- 保險公司
		sql += "      , IR.\"EarthInsuAmt\"   AS \"EarthInsuAmt\" "; // -- 地震險金額
		sql += " FROM \"ClFac\" CF";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"ApplNo\" = :applNo";
		sql += " LEFT JOIN ( SELECT IO.\"ClCode1\" ";
		sql += "                  , IO.\"ClCode2\" ";
		sql += "                  , IO.\"ClNo\" ";
		sql += "                  , IO.\"OrigInsuNo\" AS \"NowInsuNo\" ";
		sql += "                  , IO.\"FireInsuCovrg\" AS \"FireInsuAmt\" ";
		sql += "                  , IO.\"EthqInsuCovrg\" AS \"EarthInsuAmt\" ";
		sql += "                  , IO.\"InsuStartDate\" ";
		sql += "                  , IO.\"InsuEndDate\" ";
		sql += "                  , IO.\"InsuCompany\"                             AS \"InsuCompany\" ";
		sql += "             FROM \"InsuOrignal\" IO ";
		sql += "             UNION ";
		sql += "             SELECT IR.\"ClCode1\" ";
		sql += "                  , IR.\"ClCode2\" ";
		sql += "                  , IR.\"ClNo\" ";
		sql += "                  , IR.\"NowInsuNo\" ";
		sql += "                  , IR.\"FireInsuCovrg\" AS \"FireInsuAmt\" ";
		sql += "                  , IR.\"EthqInsuCovrg\" AS \"EarthInsuAmt\" ";
		sql += "                  , IR.\"InsuStartDate\" ";
		sql += "                  , IR.\"InsuEndDate\" ";
		sql += "                  , IR.\"InsuCompany\"                             AS \"InsuCompany\" ";
		sql += "             FROM \"InsuRenew\" IR ";
		sql += "             WHERE NVL(IR.\"NowInsuNo\",' ') != ' ' ";
		sql += "           ) IR ON IR.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "               AND IR.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "               AND IR.\"ClNo\"    = CF.\"ClNo\" ";
		sql += "             LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'InsuCompany' "; // -- 共用代碼檔(保險公司)
		sql += "                                      AND CDC1.\"Code\"    = IR.\"InsuCompany\" ";
		sql += " WHERE CF.\"ApproveNo\" = :applNo "; // 日期大於保險迄日
		sql += "   AND NVL(IR.\"ClNo\",0) != 0 "; // 2022-04-25 智偉增加:有串到保險單資料才顯示
		sql += "   AND CASE WHEN FM.\"FirstDrawdownDate\" = 0 THEN 1 ";
		sql += "   	        WHEN IR.\"InsuEndDate\" > FM.\"FirstDrawdownDate\"  THEN 1 ";
		sql += "   	        ELSE 0  END  = 1 ";
		sql += "   ORDER BY \"NowInsuNo\"  ASC ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryLand(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryLand");

		String sql = ""; // -- L9110 Query(土地)
		sql += "SELECT * FROM ";
		sql += "(SELECT ROW_NUMBER() OVER (PARTITION BY CF.\"ClCode1\" ";
		sql += "                                     , CF.\"ClCode2\" ";
		sql += "                                     , CF.\"ClNo\"";
		sql += "                                     , LO.\"LandSeq\" ";
		sql += "                                     , LO.\"Owner\" ";
		sql += "                           ORDER BY    CF.\"ClCode1\" ";
		sql += "                                     , CF.\"ClCode2\" ";
		sql += "                                     , CF.\"ClNo\"";
		sql += "                          			 , LO.\"LandSeq\" ";
		sql += "                                     , LO.\"Owner\" ";
		sql += "                         )     AS SEQ "; // -- F0 序號
		sql += "     , LO.\"Owner\"							AS Owner "; // -- F1 提供人 ??? 房地只取一筆?
		sql += "     , CITY.\"CityItem\"					AS CityItem "; // -- F2 縣市
		sql += "     , AREA.\"AreaItem\"					AS AreaItem "; // -- F3 鄉鎮區
		sql += "     , CDLS.\"IrItem\"						AS IrItem "; // -- F4 段小段
		sql += "     , LPAD(L.\"LandNo1\",4,'0') ";
		sql += "       || '-' ";
		sql += "       || LPAD(L.\"LandNo2\",4,'0') ";
		sql += "                               AS LandNo "; // -- F5 地號
		sql += "     , NVL(L.\"Area\",0)					AS Area "; // -- F6 面積
		sql += "     , L.\"TransferedYear\"					AS TransferedYear "; // -- F7 年度
		sql += "     , L.\"LastTransferedAmt\"				AS LastTransferedAmt "; // -- F8 前次移轉
		sql += "     , L.\"EvaUnitPrice\"					AS EvaUnitPrice "; // -- F9 鑑定單價
		sql += "     , CLI.\"SettingAmt\"					AS SettingAmt "; // -- F10設定
		sql += "     , NVL(LO.\"OwnerPart\",0)				AS OwnerPart "; // -- F11 持份比例
		sql += "     , NVL(LO.\"OwnerTotal\",0)				AS OwnerTotal "; // -- F12 持份總比例
		sql += "     , LPAD(CF.\"ClCode1\",1,'0')			AS ClCode1 "; // -- F13 擔保品代號1
		sql += "     , LPAD(CF.\"ClCode2\",2,'0')			AS ClCode2 "; // -- F14 擔保品代號2
		sql += "     , LPAD(CF.\"ClNo\",7,'0')				AS ClNo "; // -- F15 擔保品編號
		sql += "     , L.\"LandLocation\"					AS LandLocation "; // -- F16 土地座落
		sql += "     , CDC1.\"Item\"						AS ClItem "; // -- F17 擔保品別

		sql += " FROM \"ClFac\" CF ";
		sql += " LEFT JOIN \"ClLand\" L ON L.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                    AND L.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                    AND L.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN (SELECT NVL(CM.\"CustId\",' ') || NVL(SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0),0,7),' ') AS \"Owner\"";
		sql += "                 , LO.\"ClCode1\"";
		sql += "                 , LO.\"ClCode2\"";
		sql += "                 , LO.\"ClNo\"";
		sql += "                 , LO.\"LandSeq\"";
		sql += "                 , LO.\"OwnerPart\"";
		sql += "                 , LO.\"OwnerTotal\"";
		sql += "            FROM \"ClLandOwner\" LO ";
		sql += "            LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = LO.\"OwnerCustUKey\"";
		sql += "           ) LO ON LO.\"ClCode1\" = L.\"ClCode1\"";
		sql += "               AND LO.\"ClCode2\" = L.\"ClCode2\"";
		sql += "               AND LO.\"ClNo\"    = L.\"ClNo\"";
		sql += "               AND LO.\"LandSeq\" = L.\"LandSeq\"";
		sql += " LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = L.\"CityCode\"";
		sql += " LEFT JOIN \"CdArea\" AREA ON AREA.\"CityCode\" = L.\"CityCode\"";
		sql += "                          AND AREA.\"AreaCode\" = L.\"AreaCode\"";
		sql += " LEFT JOIN \"CdLandSection\" CDLS ON CDLS.\"CityCode\" = L.\"CityCode\"";
		sql += "                                 AND CDLS.\"AreaCode\" = L.\"AreaCode\"";
		sql += "                                 AND CDLS.\"IrCode\"   = L.\"IrCode\"";
		sql += " LEFT JOIN \"ClImm\" CLI ON CLI.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                        AND CLI.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                        AND CLI.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'ClCode2' || CF.\"ClCode1\"";
		sql += "                        AND CDC1.\"Code\"    = LPAD(CF.\"ClCode2\",2,'0')";
		sql += " WHERE CF.\"ApproveNo\" = :applNo";
//		sql += "   AND CF.\"MainFlag\" = 'Y'"; // -- 主要擔保品
		sql += "   AND NVL(L.\"ClNo\",0) > 0  ";
		sql += " ) WHERE SEQ = 1  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryPerson(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110.findAll Person");

		// -- 自然人、法人的基本資料、核准資料
		String sql = " ";
		sql += " SELECT LPAD(NVL(CM.\"CustNo\",0),7,'0') ";
		sql += "        || '-' ";
		sql += "        || LPAD(NVL(FAC.\"FacmNo\",0),3,'0') ";
		sql += "                                       AS F0戶號 ";
		sql += "      , SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0),0,25)   AS F1戶名 ";
		sql += "      , CM.\"CustId\"                  AS F2統編 ";
		sql += "      , LPAD(FC.\"ApplNo\",7,'0')      AS F3核准號碼 ";
		sql += "      , \"Fn_GetCdCode\"('Sex',CM.\"Sex\") ";
		sql += "                                       AS F4性別 "; // 法人不出
		sql += "      , CM.\"SpouseName\"              AS F5負責人姓名 "; // 自然人-配偶姓名
		sql += "      , CM.\"SpouseId\"                AS F6負責人身分證 "; // 自然人-配偶統一編號
		sql += "      , CI.\"IndustryItem\"            AS F7行業別 "; // 自然人不出
		sql += "      , \"Fn_GetCdCode\"('CustTypeCode',CM.\"CustTypeCode\") ";
		sql += "                                       AS F8客戶別 ";
		sql += "      , CM.\"EmpNo\"                   AS F9員工代號 "; // 法人不出
		sql += "      , CASE";
		sql += "          WHEN CM.\"EmpNo\" IS NULL";
		sql += "          THEN '非員工'";
		sql += "          WHEN NVL(CDE.\"CommLineCode\",' ') = '35'";
		sql += "          THEN '十五日薪'";
		sql += "        ELSE '非十五日薪' END          AS F10十五日薪 "; // 法人不出
		sql += "      , CM.\"Birthday\"                AS F11設立日期 "; // 自然人出生年月日
		sql += "      , \"Fn_GetCustAddr\"(CM.\"CustUKey\",'0') ";
		sql += "                                       AS F12公司地址 "; // 自然人戶籍地址
		sql += "      , NVL(CM.\"RegZip3\",'') || NVL(CM.\"RegZip2\",'')";
		sql += "                                       AS F13公司郵遞區號 "; // 自然人戶籍郵遞區號
		sql += "      , \"Fn_GetCustAddr\"(CM.\"CustUKey\",'1') ";
		sql += "                                       AS F14通訊地址 ";
		sql += "      , NVL(CM.\"CurrZip3\",'') || NVL(CM.\"CurrZip2\",'')";
		sql += "                                       AS F15通訊郵遞區號 ";
		sql += "      , TO_CHAR(FAC.\"CreateDate\",'YYYYMMDD') ";
		sql += "                                       AS F16鍵檔日期 ";
		sql += "      , FAC.\"LineAmt\"                AS F17核准額度 ";
		sql += "      , \"Fn_GetCdCode\"('AcctCode',FAC.\"AcctCode\") || FAC.\"AcctCode\" ";
		sql += "                                       AS F18核准科目 ";
		sql += "      , FAC.\"LoanTermYy\"             AS F19貸款期間年 ";
		sql += "      , FAC.\"LoanTermMm\"             AS F20貸款期間月 ";
		sql += "      , FAC.\"LoanTermDd\"             AS F21貸款期間日 ";
		sql += "      , FAC.\"ProdNo\"                 AS F22商品代碼 ";
		sql += "      , FAC.\"ApproveRate\"            AS F23核准利率 ";
		sql += "      , FAC.\"RateAdjFreq\"            AS F24利率調整週期 ";
		sql += "      , \"Fn_GetCdCode\"('ExtraRepayCode',FAC.\"ExtraRepayCode\") ";
		sql += "                                       AS F25利率調整不變攤還額 ";
		sql += "      , FAC.\"CreditScore\"            AS F26信用評分 ";
		sql += "      , FAC.\"UtilDeadline\"           AS F27動支期限 ";
		sql += "      , FAC.\"RateIncr\"			   AS F28利率加減碼 ";
		sql += "      , \"Fn_GetCdCode\"('UsageCode',LPAD(FAC.\"UsageCode\", 2, 0)) ";
		sql += "                                       AS F29用途別 ";
		sql += "      ,FAC.\"RecycleDeadline\"        AS F30循環動用期限 ";
		sql += "      ,NVL(\"Fn_GetEmpName\"(FAC.\"Introducer\",1),'無') ";
		sql += "                                       AS F31介紹人姓名 ";
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"IncomeTaxFlag\" = 'Y' ";
		sql += "          THEN '代繳' ";
		sql += "        ELSE '不代繳' END              AS F32代繳所得稅 ";
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"CompensateFlag\" = 'Y' ";
		sql += "          THEN '代償件' ";
		sql += "        ELSE '非代償件' END            AS F33代償碼 ";
		sql += "      , \"Fn_GetCdCode\"('FacmAmortizedCode',FAC.\"AmortizedCode\") ";
		sql += "                                       AS F34攤還方式 ";
		sql += "      , FAC.\"GracePeriod\"            AS F35寬限總月數 ";
		sql += "      , FAC.\"FirstRateAdjFreq\"       AS F36首次調整週期 ";
		sql += "      , \"Fn_GetCdCode\"('RepayCode', LPAD(FAC.\"RepayCode\", 2, 0)) ";
		sql += "                                       AS F37繳款方式 ";
		sql += "      , NVL(\"Fn_GetCdCode\"('BankDeductCd',\"Fn_GetRepayAcct\"(FAC.\"CustNo\",FAC.\"FacmNo\",'0')),'無') ";
		sql += "                                       AS F38扣款銀行 ";
		sql += "      , NVL(\"Fn_GetRepayAcct\"(FAC.\"CustNo\",FAC.\"FacmNo\",'1'),'無') ";
		sql += "                                       AS F39扣款帳號 ";
		sql += "      , FAC.\"PayIntFreq\" ";
		sql += "        || CASE FAC.\"FreqBase\" ";
		sql += "             WHEN '1' THEN '日' ";
		sql += "             WHEN '2' THEN '月' ";
		sql += "             WHEN '3' THEN '週' ";
		sql += "           ELSE '' ";
		sql += "           END                         AS F40繳息週期 ";
		sql += "      , FAC.\"RateCode\" || \"Fn_GetCdCode\"('FacmRateCode',FAC.\"RateCode\") ";
		sql += "                                       AS F41利率區分 ";
		sql += "      , TO_NCHAR(\"Fn_GetCdCode\"('BreachCode',FAC.\"BreachCode\")) AS F42違約適用方式 ";
		sql += "      , NVL(\"Fn_ParseEOL\"(GROUPCM.\"CustName\", 0),'無')           AS F43團體戶名 "; // 法人不出
		sql += "      , FAC.\"PieceCode\" ";
		sql += "　　　　　　　　　　　　　　             AS F44計件代碼 ";
		sql += "      , NVL(\"Fn_GetEmpName\"(FAC.\"FireOfficer\",1),'無') ";
		sql += "                                       AS F45火險服務姓名 ";
		sql += "      , NVL(\"Fn_GetEmpName\"(NVL(FAC.\"LoanOfficer\",FC.\"LoanOfficer\"),1),'無') ";
		sql += "                                       AS F46放款專員 ";
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"ApprovedLevel\" = '9' ";
		sql += "          THEN n'董事會' "; // 核准層級9:董事會，不需填寫核決主管
		sql += "        ELSE \"Fn_GetEmpName\"(FAC.\"Supervisor\",1) ";
		sql += "        END                            AS F47核決主管 ";
		sql += "      , FAC.\"ProhibitMonth\"         AS F48限制清償期限 ";
		sql += "      , FAC.\"AcctFee\"                AS F49帳管費 ";
		sql += "      , NVL(\"Fn_GetEmpName\"(FAC.\"EstimateReview\",1),'無') ";
		sql += "                                       AS F50估價覆核姓名 ";
		sql += "      , \"Fn_GetCdCode\"('CustTypeCode',FAC.\"CustTypeCode\") ";
		sql += "                                       AS F51客戶別 ";
		sql += "      , NVL(\"Fn_GetEmpName\"(FAC.\"InvestigateOfficer\",1),'無') ";
		sql += "                                       AS F52徵信姓名 ";
		sql += "      , NVL(\"Fn_GetEmpName\"(FAC.\"CreditOfficer\",1),'無') ";
		sql += "                                       AS F53授信姓名 ";
		sql += "      , NVL(\"Fn_GetEmpName\"(FAC.\"Coorgnizer\",1),'無') ";
		sql += "                                       AS F54協辦姓名 ";
		sql += "      , NVL(LBM.\"LoanBal\",0)         AS F55本戶目前總額 ";
		sql += "      , \"Fn_GetCdCode\"('RuleCode',FAC.\"RuleCode\") ";
		sql += "                                       AS F56規定管制代碼 ";
		sql += "      , TO_CHAR(\"Fn_GetCdCode\"('BreachGetCode',FAC.\"BreachGetCode\"))  AS F57違約金收取方式 ";
		sql += "      , FAC.\"BreachCode\"     		   AS F58違約適用方式 ";
		sql += "      , PROD.\"ProdName\"       	   AS F59商品名稱 ";
		sql += "      , FAC.\"BaseRateCode\"       	   AS F60指標利率代碼 ";
		sql += "      , FAC.\"LastBormNo\"       	   AS F61最後撥款序號 ";
		sql += "      , FAC.\"BreachDecreaseMonth\"   AS F62違約金分段月數";
		sql += " FROM \"FacCaseAppl\" FC "; // 案件申請檔 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = FC.\"CustUKey\" "; // 客戶資料主檔
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"ApplNo\" = FC.\"ApplNo\" "; // 額度主檔
		sql += " LEFT JOIN \"FacProd\" PROD ON PROD.\"ProdNo\" = FAC.\"ProdNo\" ";
		sql += " LEFT JOIN \"CdIndustry\" CI ON CI.\"IndustryCode\" = CM.\"IndustryCode\" "; // 行業別代碼檔
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += "             FROM \"LoanBorMain\" ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "           ) LBM ON LBM.\"CustNo\" = CM.\"CustNo\" "; // 計算放款餘額加總
		sql += " LEFT JOIN \"CustMain\" GROUPCM ON GROUPCM.\"CustUKey\" = FC.\"GroupUKey\" "; // 團體戶
		sql += " LEFT JOIN \"CdEmp\" CDE ON CDE.\"EmployeeNo\" = CM.\"EmpNo\" "; // 十五日薪判斷相關
		sql += " WHERE FC.\"ApplNo\" = :applNo";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryStock(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryStock");

		// -- L9110 Query(股票)
		String sql = "";
		sql += " SELECT NVL(CM.\"CustId\",' ') || NVL(SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0),0,15),' ') AS \"Owner\" "; // F0
																															// 股票持有人
		sql += "      , CS.\"SettingBalance\" "; // F1 設質股數餘額
		sql += "      , CS.\"SettingBalance\" ";
		sql += "        * CS.\"ParValue\" AS \"TotalParValue\" "; // F2 面額合計
		sql += " FROM \"ClFac\" CF";
		sql += " LEFT JOIN \"ClStock\" CS ON CS.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                         AND CS.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                         AND CS.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = CS.\"OwnerCustUKey\" ";
		sql += " WHERE CF.\"ApproveNo\" = :applNo ";
		sql += "   AND CS.\"ClCode1\" = 3 "; // 串得到股票擔保品的資料才進表

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryCoborrower(TitaVo titaVo, String applNo) {

		this.info("L9110ServiceImpl.queryCoborrower");

		// -- L9110 Query(共同借款人)
		String sql = "";
		sql += " SELECT LPAD(S1.\"CustNo\",7,'0') ";
		sql += "        || '-' || LPAD(S1.\"FacmNo\",3,'0') ";
		sql += "         AS \"CustNo\" "; // -- F0 戶號
		sql += "      , SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0),0,11) AS \"CustName\" "; // -- F1 戶名
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"RecycleCode\" = '1' ";
		sql += "          THEN FAC.\"RecycleDeadline\" ";
		sql += "        ELSE FAC.\"UtilDeadline\" END AS \"UtilDeadline\" "; // -- F2 循環動用動支期限
		sql += "      , FAC.\"CurrencyCode\" "; // -- F3 幣別
		sql += "      , FAC.\"LineAmt\" "; // -- F4 核准額度
		sql += "      , FAC.\"UtilBal\" "; // -- F5 已動用額度餘額
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"RecycleCode\" = '1' ";
		sql += "          THEN 'Y' ";
		sql += "        ELSE 'N' END AS \"IsRecycle\" "; // -- F6 循環動用
		sql += "      , S1.\"JcicMergeFlag\" ";// -- F7 是否合併申報
		sql += " FROM (SELECT FSA.\"MainApplNo\" ";
		sql += "       FROM \"FacShareAppl\" FSA ";
		sql += "       WHERE FSA.\"ApplNo\" = :applNo ";
		sql += "       UNION ";
		sql += "       SELECT FSA.\"MainApplNo\" ";
		sql += "       FROM \"FacShareAppl\" FSA ";
		sql += "       WHERE FSA.\"MainApplNo\" = :applNo ";
		sql += "      ) S0 ";
		sql += " LEFT JOIN \"FacShareAppl\" S1 ON S1.\"MainApplNo\" = S0.\"MainApplNo\" ";
		sql += " LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = S1.\"ApplNo\" ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = S1.\"CustNo\" ";
		sql += "                          AND FAC.\"FacmNo\" = S1.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = FCA.\"CustUKey\" ";
		sql += " ORDER BY S1.\"MainApplNo\" ";
		sql += "        , S1.\"KeyinSeq\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryShareQuota(TitaVo titaVo, String applNo) {

		this.info("L9110ServiceImpl.queryShareQuota");

		// -- L9110 Query(合併額度控管)
		String sql = "";

		sql += " SELECT LPAD(S0.\"MainApplNo\",7,'0') AS \"MainApplNo\" "; // F0 主要核准號碼
		sql += "      , TTL.\"MainCCY\" "; // F1 主要核准號碼之幣別
		sql += "      , TTL.\"LineAmtTotal\" "; // F2 核准額度加總
		sql += "      , TTL.\"UtilBalTotal\" "; // F3 已動用額度餘額加總
		sql += "      , TTL.\"UtilAmtTotal\" "; // F4 貸出金額(放款餘額、目前餘額)加總
		sql += "      , LPAD(S1.\"CustNo\",7,'0') ";
		sql += "        || '-' ";
		sql += "        || LPAD(S1.\"FacmNo\",3,'0') AS \"CustNo\" "; // F5 戶號
		sql += "      , \"Fn_ParseEOL\"(CM.\"CustName\", 0) AS \"CustName\" "; // F6 戶名
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"RecycleCode\" = '1' ";
		sql += "          THEN FAC.\"RecycleDeadline\" ";
		sql += "        ELSE FAC.\"UtilDeadline\" END AS \"UtilDeadline\" "; // F7 循環動用動支期限
		sql += "      , FAC.\"CurrencyCode\" "; // F8 幣別
		sql += "      , FAC.\"LineAmt\" "; // F9 核准額度
		sql += "      , FAC.\"UtilBal\" "; // F10 已動用額度餘額
		sql += "      , FAC.\"UtilAmt\" "; // F11 貸出金額(放款餘額、目前餘額)
		sql += "      , CASE ";
		sql += "          WHEN FAC.\"RecycleCode\" = '1' ";
		sql += "          THEN 'Y' ";
		sql += "        ELSE 'N' END AS \"IsRecycle\" "; // F12 循環動用
		sql += " FROM (SELECT FSL.\"MainApplNo\" ";
		sql += "       FROM \"FacShareLimit\" FSL ";
		sql += "       WHERE FSL.\"ApplNo\" = :applNo ";
		sql += "       UNION ";
		sql += "       SELECT FSL.\"MainApplNo\" ";
		sql += "       FROM \"FacShareLimit\" FSL ";
		sql += "       WHERE FSL.\"MainApplNo\" = :applNo ";
		sql += "      ) S0 ";
		sql += " LEFT JOIN \"FacShareLimit\" S1 ON S1.\"MainApplNo\" = S0.\"MainApplNo\" ";
		sql += " LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = S1.\"ApplNo\" ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = S1.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = S1.\"FacmNo\" ";
		sql += " LEFT JOIN (SELECT FSL.\"MainApplNo\" ";
		sql += "                 , FAC.\"CurrencyCode\" AS \"MainCCY\" ";
		sql += "                 , MAX(FAC.\"LineAmt\") AS \"LineAmtTotal\" ";
		sql += "                 , SUM(FAC.\"UtilBal\") AS \"UtilBalTotal\" ";
		sql += "                 , SUM(FAC.\"UtilAmt\") AS \"UtilAmtTotal\" ";
		sql += "            FROM \"FacShareLimit\" FSL ";
		sql += "            LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = FSL.\"CustNo\" ";
		sql += "                                   AND FAC.\"FacmNo\" = FSL.\"FacmNo\" ";
		sql += "            GROUP BY FSL.\"MainApplNo\" ";
		sql += "                   , FAC.\"CurrencyCode\" ";
		sql += "           ) TTL ON TTL.\"MainApplNo\" = S0.\"MainApplNo\" ";
		sql += "                AND TTL.\"MainCCY\" = FAC.\"CurrencyCode\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = FCA.\"CustUKey\" ";
		sql += " ORDER BY TTL.\"MainApplNo\" ";
		sql += "        , TTL.\"MainCCY\" ";
		sql += "        , S1.\"KeyinSeq\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query);
	}
}