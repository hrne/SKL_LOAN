package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

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
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.domain.InsuRenewMediaTempId;
import com.st1.itx.db.repository.online.InsuRenewMediaTempRepository;
import com.st1.itx.db.repository.day.InsuRenewMediaTempRepositoryDay;
import com.st1.itx.db.repository.mon.InsuRenewMediaTempRepositoryMon;
import com.st1.itx.db.repository.hist.InsuRenewMediaTempRepositoryHist;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("insuRenewMediaTempService")
@Repository
public class InsuRenewMediaTempServiceImpl extends ASpringJpaParm implements InsuRenewMediaTempService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private InsuRenewMediaTempRepository insuRenewMediaTempRepos;

	@Autowired
	private InsuRenewMediaTempRepositoryDay insuRenewMediaTempReposDay;

	@Autowired
	private InsuRenewMediaTempRepositoryMon insuRenewMediaTempReposMon;

	@Autowired
	private InsuRenewMediaTempRepositoryHist insuRenewMediaTempReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(insuRenewMediaTempRepos);
		org.junit.Assert.assertNotNull(insuRenewMediaTempReposDay);
		org.junit.Assert.assertNotNull(insuRenewMediaTempReposMon);
		org.junit.Assert.assertNotNull(insuRenewMediaTempReposHist);
	}

	@Override
	public InsuRenewMediaTemp findById(InsuRenewMediaTempId insuRenewMediaTempId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + insuRenewMediaTempId);
		Optional<InsuRenewMediaTemp> insuRenewMediaTemp = null;
		if (dbName.equals(ContentName.onDay))
			insuRenewMediaTemp = insuRenewMediaTempReposDay.findById(insuRenewMediaTempId);
		else if (dbName.equals(ContentName.onMon))
			insuRenewMediaTemp = insuRenewMediaTempReposMon.findById(insuRenewMediaTempId);
		else if (dbName.equals(ContentName.onHist))
			insuRenewMediaTemp = insuRenewMediaTempReposHist.findById(insuRenewMediaTempId);
		else
			insuRenewMediaTemp = insuRenewMediaTempRepos.findById(insuRenewMediaTempId);
		InsuRenewMediaTemp obj = insuRenewMediaTemp.isPresent() ? insuRenewMediaTemp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<InsuRenewMediaTemp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InsuRenewMediaTemp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FireInsuMonth", "InsuNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = insuRenewMediaTempReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = insuRenewMediaTempReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = insuRenewMediaTempReposHist.findAll(pageable);
		else
			slice = insuRenewMediaTempRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InsuRenewMediaTemp> fireInsuMonthRg(String fireInsuMonth_0, String fireInsuMonth_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InsuRenewMediaTemp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("fireInsuMonthRg " + dbName + " : " + "fireInsuMonth_0 : " + fireInsuMonth_0 + " fireInsuMonth_1 : " + fireInsuMonth_1);
		if (dbName.equals(ContentName.onDay))
			slice = insuRenewMediaTempReposDay.findAllByFireInsuMonthGreaterThanEqualAndFireInsuMonthLessThanEqual(fireInsuMonth_0, fireInsuMonth_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = insuRenewMediaTempReposMon.findAllByFireInsuMonthGreaterThanEqualAndFireInsuMonthLessThanEqual(fireInsuMonth_0, fireInsuMonth_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = insuRenewMediaTempReposHist.findAllByFireInsuMonthGreaterThanEqualAndFireInsuMonthLessThanEqual(fireInsuMonth_0, fireInsuMonth_1, pageable);
		else
			slice = insuRenewMediaTempRepos.findAllByFireInsuMonthGreaterThanEqualAndFireInsuMonthLessThanEqual(fireInsuMonth_0, fireInsuMonth_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public InsuRenewMediaTemp holdById(InsuRenewMediaTempId insuRenewMediaTempId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + insuRenewMediaTempId);
		Optional<InsuRenewMediaTemp> insuRenewMediaTemp = null;
		if (dbName.equals(ContentName.onDay))
			insuRenewMediaTemp = insuRenewMediaTempReposDay.findByInsuRenewMediaTempId(insuRenewMediaTempId);
		else if (dbName.equals(ContentName.onMon))
			insuRenewMediaTemp = insuRenewMediaTempReposMon.findByInsuRenewMediaTempId(insuRenewMediaTempId);
		else if (dbName.equals(ContentName.onHist))
			insuRenewMediaTemp = insuRenewMediaTempReposHist.findByInsuRenewMediaTempId(insuRenewMediaTempId);
		else
			insuRenewMediaTemp = insuRenewMediaTempRepos.findByInsuRenewMediaTempId(insuRenewMediaTempId);
		return insuRenewMediaTemp.isPresent() ? insuRenewMediaTemp.get() : null;
	}

	@Override
	public InsuRenewMediaTemp holdById(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + insuRenewMediaTemp.getInsuRenewMediaTempId());
		Optional<InsuRenewMediaTemp> insuRenewMediaTempT = null;
		if (dbName.equals(ContentName.onDay))
			insuRenewMediaTempT = insuRenewMediaTempReposDay.findByInsuRenewMediaTempId(insuRenewMediaTemp.getInsuRenewMediaTempId());
		else if (dbName.equals(ContentName.onMon))
			insuRenewMediaTempT = insuRenewMediaTempReposMon.findByInsuRenewMediaTempId(insuRenewMediaTemp.getInsuRenewMediaTempId());
		else if (dbName.equals(ContentName.onHist))
			insuRenewMediaTempT = insuRenewMediaTempReposHist.findByInsuRenewMediaTempId(insuRenewMediaTemp.getInsuRenewMediaTempId());
		else
			insuRenewMediaTempT = insuRenewMediaTempRepos.findByInsuRenewMediaTempId(insuRenewMediaTemp.getInsuRenewMediaTempId());
		return insuRenewMediaTempT.isPresent() ? insuRenewMediaTempT.get() : null;
	}

	@Override
	public InsuRenewMediaTemp insert(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + insuRenewMediaTemp.getInsuRenewMediaTempId());
		if (this.findById(insuRenewMediaTemp.getInsuRenewMediaTempId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			insuRenewMediaTemp.setCreateEmpNo(empNot);

		if (insuRenewMediaTemp.getLastUpdateEmpNo() == null || insuRenewMediaTemp.getLastUpdateEmpNo().isEmpty())
			insuRenewMediaTemp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return insuRenewMediaTempReposDay.saveAndFlush(insuRenewMediaTemp);
		else if (dbName.equals(ContentName.onMon))
			return insuRenewMediaTempReposMon.saveAndFlush(insuRenewMediaTemp);
		else if (dbName.equals(ContentName.onHist))
			return insuRenewMediaTempReposHist.saveAndFlush(insuRenewMediaTemp);
		else
			return insuRenewMediaTempRepos.saveAndFlush(insuRenewMediaTemp);
	}

	@Override
	public InsuRenewMediaTemp update(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + insuRenewMediaTemp.getInsuRenewMediaTempId());
		if (!empNot.isEmpty())
			insuRenewMediaTemp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return insuRenewMediaTempReposDay.saveAndFlush(insuRenewMediaTemp);
		else if (dbName.equals(ContentName.onMon))
			return insuRenewMediaTempReposMon.saveAndFlush(insuRenewMediaTemp);
		else if (dbName.equals(ContentName.onHist))
			return insuRenewMediaTempReposHist.saveAndFlush(insuRenewMediaTemp);
		else
			return insuRenewMediaTempRepos.saveAndFlush(insuRenewMediaTemp);
	}

	@Override
	public InsuRenewMediaTemp update2(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + insuRenewMediaTemp.getInsuRenewMediaTempId());
		if (!empNot.isEmpty())
			insuRenewMediaTemp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			insuRenewMediaTempReposDay.saveAndFlush(insuRenewMediaTemp);
		else if (dbName.equals(ContentName.onMon))
			insuRenewMediaTempReposMon.saveAndFlush(insuRenewMediaTemp);
		else if (dbName.equals(ContentName.onHist))
			insuRenewMediaTempReposHist.saveAndFlush(insuRenewMediaTemp);
		else
			insuRenewMediaTempRepos.saveAndFlush(insuRenewMediaTemp);
		return this.findById(insuRenewMediaTemp.getInsuRenewMediaTempId());
	}

	@Override
	public void delete(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + insuRenewMediaTemp.getInsuRenewMediaTempId());
		if (dbName.equals(ContentName.onDay)) {
			insuRenewMediaTempReposDay.delete(insuRenewMediaTemp);
			insuRenewMediaTempReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuRenewMediaTempReposMon.delete(insuRenewMediaTemp);
			insuRenewMediaTempReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuRenewMediaTempReposHist.delete(insuRenewMediaTemp);
			insuRenewMediaTempReposHist.flush();
		} else {
			insuRenewMediaTempRepos.delete(insuRenewMediaTemp);
			insuRenewMediaTempRepos.flush();
		}
	}

	@Override
	public void insertAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		if (insuRenewMediaTemp == null || insuRenewMediaTemp.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (InsuRenewMediaTemp t : insuRenewMediaTemp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			insuRenewMediaTemp = insuRenewMediaTempReposDay.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuRenewMediaTemp = insuRenewMediaTempReposMon.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuRenewMediaTemp = insuRenewMediaTempReposHist.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempReposHist.flush();
		} else {
			insuRenewMediaTemp = insuRenewMediaTempRepos.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempRepos.flush();
		}
	}

	@Override
	public void updateAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (insuRenewMediaTemp == null || insuRenewMediaTemp.size() == 0)
			throw new DBException(6);

		for (InsuRenewMediaTemp t : insuRenewMediaTemp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			insuRenewMediaTemp = insuRenewMediaTempReposDay.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuRenewMediaTemp = insuRenewMediaTempReposMon.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuRenewMediaTemp = insuRenewMediaTempReposHist.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempReposHist.flush();
		} else {
			insuRenewMediaTemp = insuRenewMediaTempRepos.saveAll(insuRenewMediaTemp);
			insuRenewMediaTempRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (insuRenewMediaTemp == null || insuRenewMediaTemp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			insuRenewMediaTempReposDay.deleteAll(insuRenewMediaTemp);
			insuRenewMediaTempReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuRenewMediaTempReposMon.deleteAll(insuRenewMediaTemp);
			insuRenewMediaTempReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuRenewMediaTempReposHist.deleteAll(insuRenewMediaTemp);
			insuRenewMediaTempReposHist.flush();
		} else {
			insuRenewMediaTempRepos.deleteAll(insuRenewMediaTemp);
			insuRenewMediaTempRepos.flush();
		}
	}

}
