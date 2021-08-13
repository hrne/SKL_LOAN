package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.repository.online.CdLandOfficeRepository;
import com.st1.itx.db.repository.day.CdLandOfficeRepositoryDay;
import com.st1.itx.db.repository.mon.CdLandOfficeRepositoryMon;
import com.st1.itx.db.repository.hist.CdLandOfficeRepositoryHist;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdLandOfficeService")
@Repository
public class CdLandOfficeServiceImpl implements CdLandOfficeService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(CdLandOfficeServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdLandOfficeRepository cdLandOfficeRepos;

	@Autowired
	private CdLandOfficeRepositoryDay cdLandOfficeReposDay;

	@Autowired
	private CdLandOfficeRepositoryMon cdLandOfficeReposMon;

	@Autowired
	private CdLandOfficeRepositoryHist cdLandOfficeReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdLandOfficeRepos);
		org.junit.Assert.assertNotNull(cdLandOfficeReposDay);
		org.junit.Assert.assertNotNull(cdLandOfficeReposMon);
		org.junit.Assert.assertNotNull(cdLandOfficeReposHist);
	}

	@Override
	public CdLandOffice findById(String landOfficeCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + landOfficeCode);
		Optional<CdLandOffice> cdLandOffice = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOffice = cdLandOfficeReposDay.findById(landOfficeCode);
		else if (dbName.equals(ContentName.onMon))
			cdLandOffice = cdLandOfficeReposMon.findById(landOfficeCode);
		else if (dbName.equals(ContentName.onHist))
			cdLandOffice = cdLandOfficeReposHist.findById(landOfficeCode);
		else
			cdLandOffice = cdLandOfficeRepos.findById(landOfficeCode);
		CdLandOffice obj = cdLandOffice.isPresent() ? cdLandOffice.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdLandOffice> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LandOfficeCode"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAll(pageable);
		else
			slice = cdLandOfficeRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdLandOffice> findCity(String city_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findCity " + dbName + " : " + "city_0 : " + city_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAllByCityIsOrderByLandOfficeCodeAsc(city_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAllByCityIsOrderByLandOfficeCodeAsc(city_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAllByCityIsOrderByLandOfficeCodeAsc(city_0, pageable);
		else
			slice = cdLandOfficeRepos.findAllByCityIsOrderByLandOfficeCodeAsc(city_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdLandOffice> findTown(String town_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findTown " + dbName + " : " + "town_0 : " + town_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAllByTownIsOrderByLandOfficeCodeAsc(town_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAllByTownIsOrderByLandOfficeCodeAsc(town_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAllByTownIsOrderByLandOfficeCodeAsc(town_0, pageable);
		else
			slice = cdLandOfficeRepos.findAllByTownIsOrderByLandOfficeCodeAsc(town_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdLandOffice> cityCodeEq(String cityCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("cityCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAllByCityCodeIsOrderByLandOfficeCodeAsc(cityCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAllByCityCodeIsOrderByLandOfficeCodeAsc(cityCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAllByCityCodeIsOrderByLandOfficeCodeAsc(cityCode_0, pageable);
		else
			slice = cdLandOfficeRepos.findAllByCityCodeIsOrderByLandOfficeCodeAsc(cityCode_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdLandOffice> AreaCodeEq(String cityCode_0, String areaCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("AreaCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " areaCode_1 : " + areaCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAllByCityCodeIsAndAreaCodeIsOrderByLandOfficeCodeAsc(cityCode_0, areaCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAllByCityCodeIsAndAreaCodeIsOrderByLandOfficeCodeAsc(cityCode_0, areaCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAllByCityCodeIsAndAreaCodeIsOrderByLandOfficeCodeAsc(cityCode_0, areaCode_1, pageable);
		else
			slice = cdLandOfficeRepos.findAllByCityCodeIsAndAreaCodeIsOrderByLandOfficeCodeAsc(cityCode_0, areaCode_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdLandOffice holdById(String landOfficeCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + landOfficeCode);
		Optional<CdLandOffice> cdLandOffice = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOffice = cdLandOfficeReposDay.findByLandOfficeCode(landOfficeCode);
		else if (dbName.equals(ContentName.onMon))
			cdLandOffice = cdLandOfficeReposMon.findByLandOfficeCode(landOfficeCode);
		else if (dbName.equals(ContentName.onHist))
			cdLandOffice = cdLandOfficeReposHist.findByLandOfficeCode(landOfficeCode);
		else
			cdLandOffice = cdLandOfficeRepos.findByLandOfficeCode(landOfficeCode);
		return cdLandOffice.isPresent() ? cdLandOffice.get() : null;
	}

	@Override
	public CdLandOffice holdById(CdLandOffice cdLandOffice, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + cdLandOffice.getLandOfficeCode());
		Optional<CdLandOffice> cdLandOfficeT = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOfficeT = cdLandOfficeReposDay.findByLandOfficeCode(cdLandOffice.getLandOfficeCode());
		else if (dbName.equals(ContentName.onMon))
			cdLandOfficeT = cdLandOfficeReposMon.findByLandOfficeCode(cdLandOffice.getLandOfficeCode());
		else if (dbName.equals(ContentName.onHist))
			cdLandOfficeT = cdLandOfficeReposHist.findByLandOfficeCode(cdLandOffice.getLandOfficeCode());
		else
			cdLandOfficeT = cdLandOfficeRepos.findByLandOfficeCode(cdLandOffice.getLandOfficeCode());
		return cdLandOfficeT.isPresent() ? cdLandOfficeT.get() : null;
	}

	@Override
	public CdLandOffice insert(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + cdLandOffice.getLandOfficeCode());
		if (this.findById(cdLandOffice.getLandOfficeCode()) != null)
			throw new DBException(2);

		cdLandOffice.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdLandOfficeReposDay.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onMon))
			return cdLandOfficeReposMon.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onHist))
			return cdLandOfficeReposHist.saveAndFlush(cdLandOffice);
		else
			return cdLandOfficeRepos.saveAndFlush(cdLandOffice);
	}

	@Override
	public CdLandOffice update(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + cdLandOffice.getLandOfficeCode());
		cdLandOffice.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdLandOfficeReposDay.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onMon))
			return cdLandOfficeReposMon.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onHist))
			return cdLandOfficeReposHist.saveAndFlush(cdLandOffice);
		else
			return cdLandOfficeRepos.saveAndFlush(cdLandOffice);
	}

	@Override
	public CdLandOffice update2(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + cdLandOffice.getLandOfficeCode());
		cdLandOffice.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdLandOfficeReposDay.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onMon))
			cdLandOfficeReposMon.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onHist))
			cdLandOfficeReposHist.saveAndFlush(cdLandOffice);
		else
			cdLandOfficeRepos.saveAndFlush(cdLandOffice);
		return this.findById(cdLandOffice.getLandOfficeCode());
	}

	@Override
	public void delete(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + cdLandOffice.getLandOfficeCode());
		if (dbName.equals(ContentName.onDay)) {
			cdLandOfficeReposDay.delete(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOfficeReposMon.delete(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOfficeReposHist.delete(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOfficeRepos.delete(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException {
		if (cdLandOffice == null || cdLandOffice.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (CdLandOffice t : cdLandOffice)
			t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdLandOffice = cdLandOfficeReposDay.saveAll(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOffice = cdLandOfficeReposMon.saveAll(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOffice = cdLandOfficeReposHist.saveAll(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOffice = cdLandOfficeRepos.saveAll(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (cdLandOffice == null || cdLandOffice.size() == 0)
			throw new DBException(6);

		for (CdLandOffice t : cdLandOffice)
			t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdLandOffice = cdLandOfficeReposDay.saveAll(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOffice = cdLandOfficeReposMon.saveAll(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOffice = cdLandOfficeReposHist.saveAll(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOffice = cdLandOfficeRepos.saveAll(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdLandOffice == null || cdLandOffice.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdLandOfficeReposDay.deleteAll(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOfficeReposMon.deleteAll(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOfficeReposHist.deleteAll(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOfficeRepos.deleteAll(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

}
