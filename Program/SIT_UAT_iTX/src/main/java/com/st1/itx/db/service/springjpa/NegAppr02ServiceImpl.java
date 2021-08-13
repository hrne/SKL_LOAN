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
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.repository.online.NegAppr02Repository;
import com.st1.itx.db.repository.day.NegAppr02RepositoryDay;
import com.st1.itx.db.repository.mon.NegAppr02RepositoryMon;
import com.st1.itx.db.repository.hist.NegAppr02RepositoryHist;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negAppr02Service")
@Repository
public class NegAppr02ServiceImpl implements NegAppr02Service, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(NegAppr02ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private NegAppr02Repository negAppr02Repos;

	@Autowired
	private NegAppr02RepositoryDay negAppr02ReposDay;

	@Autowired
	private NegAppr02RepositoryMon negAppr02ReposMon;

	@Autowired
	private NegAppr02RepositoryHist negAppr02ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(negAppr02Repos);
		org.junit.Assert.assertNotNull(negAppr02ReposDay);
		org.junit.Assert.assertNotNull(negAppr02ReposMon);
		org.junit.Assert.assertNotNull(negAppr02ReposHist);
	}

	@Override
	public NegAppr02 findById(NegAppr02Id negAppr02Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + negAppr02Id);
		Optional<NegAppr02> negAppr02 = null;
		if (dbName.equals(ContentName.onDay))
			negAppr02 = negAppr02ReposDay.findById(negAppr02Id);
		else if (dbName.equals(ContentName.onMon))
			negAppr02 = negAppr02ReposMon.findById(negAppr02Id);
		else if (dbName.equals(ContentName.onHist))
			negAppr02 = negAppr02ReposHist.findById(negAppr02Id);
		else
			negAppr02 = negAppr02Repos.findById(negAppr02Id);
		NegAppr02 obj = negAppr02.isPresent() ? negAppr02.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<NegAppr02> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<NegAppr02> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BringUpDate", "FinCode", "TxSeq"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = negAppr02ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = negAppr02ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = negAppr02ReposHist.findAll(pageable);
		else
			slice = negAppr02Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<NegAppr02> acDateBetween(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<NegAppr02> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("acDateBetween " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " + acDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = negAppr02ReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustIdAscAcDateAsc(acDate_0, acDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = negAppr02ReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustIdAscAcDateAsc(acDate_0, acDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = negAppr02ReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustIdAscAcDateAsc(acDate_0, acDate_1, pageable);
		else
			slice = negAppr02Repos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustIdAscAcDateAsc(acDate_0, acDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<NegAppr02> acDateEq(int acDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<NegAppr02> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("acDateEq " + dbName + " : " + "acDate_0 : " + acDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = negAppr02ReposDay.findAllByAcDateIsOrderByCustIdAsc(acDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = negAppr02ReposMon.findAllByAcDateIsOrderByCustIdAsc(acDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = negAppr02ReposHist.findAllByAcDateIsOrderByCustIdAsc(acDate_0, pageable);
		else
			slice = negAppr02Repos.findAllByAcDateIsOrderByCustIdAsc(acDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<NegAppr02> bringUpDateEq(int bringUpDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<NegAppr02> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("bringUpDateEq " + dbName + " : " + "bringUpDate_0 : " + bringUpDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = negAppr02ReposDay.findAllByBringUpDateIsOrderByCustIdAsc(bringUpDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = negAppr02ReposMon.findAllByBringUpDateIsOrderByCustIdAsc(bringUpDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = negAppr02ReposHist.findAllByBringUpDateIsOrderByCustIdAsc(bringUpDate_0, pageable);
		else
			slice = negAppr02Repos.findAllByBringUpDateIsOrderByCustIdAsc(bringUpDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public NegAppr02 holdById(NegAppr02Id negAppr02Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + negAppr02Id);
		Optional<NegAppr02> negAppr02 = null;
		if (dbName.equals(ContentName.onDay))
			negAppr02 = negAppr02ReposDay.findByNegAppr02Id(negAppr02Id);
		else if (dbName.equals(ContentName.onMon))
			negAppr02 = negAppr02ReposMon.findByNegAppr02Id(negAppr02Id);
		else if (dbName.equals(ContentName.onHist))
			negAppr02 = negAppr02ReposHist.findByNegAppr02Id(negAppr02Id);
		else
			negAppr02 = negAppr02Repos.findByNegAppr02Id(negAppr02Id);
		return negAppr02.isPresent() ? negAppr02.get() : null;
	}

	@Override
	public NegAppr02 holdById(NegAppr02 negAppr02, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + negAppr02.getNegAppr02Id());
		Optional<NegAppr02> negAppr02T = null;
		if (dbName.equals(ContentName.onDay))
			negAppr02T = negAppr02ReposDay.findByNegAppr02Id(negAppr02.getNegAppr02Id());
		else if (dbName.equals(ContentName.onMon))
			negAppr02T = negAppr02ReposMon.findByNegAppr02Id(negAppr02.getNegAppr02Id());
		else if (dbName.equals(ContentName.onHist))
			negAppr02T = negAppr02ReposHist.findByNegAppr02Id(negAppr02.getNegAppr02Id());
		else
			negAppr02T = negAppr02Repos.findByNegAppr02Id(negAppr02.getNegAppr02Id());
		return negAppr02T.isPresent() ? negAppr02T.get() : null;
	}

	@Override
	public NegAppr02 insert(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + negAppr02.getNegAppr02Id());
		if (this.findById(negAppr02.getNegAppr02Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			negAppr02.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return negAppr02ReposDay.saveAndFlush(negAppr02);
		else if (dbName.equals(ContentName.onMon))
			return negAppr02ReposMon.saveAndFlush(negAppr02);
		else if (dbName.equals(ContentName.onHist))
			return negAppr02ReposHist.saveAndFlush(negAppr02);
		else
			return negAppr02Repos.saveAndFlush(negAppr02);
	}

	@Override
	public NegAppr02 update(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + negAppr02.getNegAppr02Id());
		if (!empNot.isEmpty())
			negAppr02.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return negAppr02ReposDay.saveAndFlush(negAppr02);
		else if (dbName.equals(ContentName.onMon))
			return negAppr02ReposMon.saveAndFlush(negAppr02);
		else if (dbName.equals(ContentName.onHist))
			return negAppr02ReposHist.saveAndFlush(negAppr02);
		else
			return negAppr02Repos.saveAndFlush(negAppr02);
	}

	@Override
	public NegAppr02 update2(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + negAppr02.getNegAppr02Id());
		if (!empNot.isEmpty())
			negAppr02.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			negAppr02ReposDay.saveAndFlush(negAppr02);
		else if (dbName.equals(ContentName.onMon))
			negAppr02ReposMon.saveAndFlush(negAppr02);
		else if (dbName.equals(ContentName.onHist))
			negAppr02ReposHist.saveAndFlush(negAppr02);
		else
			negAppr02Repos.saveAndFlush(negAppr02);
		return this.findById(negAppr02.getNegAppr02Id());
	}

	@Override
	public void delete(NegAppr02 negAppr02, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + negAppr02.getNegAppr02Id());
		if (dbName.equals(ContentName.onDay)) {
			negAppr02ReposDay.delete(negAppr02);
			negAppr02ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			negAppr02ReposMon.delete(negAppr02);
			negAppr02ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			negAppr02ReposHist.delete(negAppr02);
			negAppr02ReposHist.flush();
		} else {
			negAppr02Repos.delete(negAppr02);
			negAppr02Repos.flush();
		}
	}

	@Override
	public void insertAll(List<NegAppr02> negAppr02, TitaVo... titaVo) throws DBException {
		if (negAppr02 == null || negAppr02.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (NegAppr02 t : negAppr02)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			negAppr02 = negAppr02ReposDay.saveAll(negAppr02);
			negAppr02ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			negAppr02 = negAppr02ReposMon.saveAll(negAppr02);
			negAppr02ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			negAppr02 = negAppr02ReposHist.saveAll(negAppr02);
			negAppr02ReposHist.flush();
		} else {
			negAppr02 = negAppr02Repos.saveAll(negAppr02);
			negAppr02Repos.flush();
		}
	}

	@Override
	public void updateAll(List<NegAppr02> negAppr02, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (negAppr02 == null || negAppr02.size() == 0)
			throw new DBException(6);

		for (NegAppr02 t : negAppr02)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			negAppr02 = negAppr02ReposDay.saveAll(negAppr02);
			negAppr02ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			negAppr02 = negAppr02ReposMon.saveAll(negAppr02);
			negAppr02ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			negAppr02 = negAppr02ReposHist.saveAll(negAppr02);
			negAppr02ReposHist.flush();
		} else {
			negAppr02 = negAppr02Repos.saveAll(negAppr02);
			negAppr02Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<NegAppr02> negAppr02, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (negAppr02 == null || negAppr02.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			negAppr02ReposDay.deleteAll(negAppr02);
			negAppr02ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			negAppr02ReposMon.deleteAll(negAppr02);
			negAppr02ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			negAppr02ReposHist.deleteAll(negAppr02);
			negAppr02ReposHist.flush();
		} else {
			negAppr02Repos.deleteAll(negAppr02);
			negAppr02Repos.flush();
		}
	}

}
