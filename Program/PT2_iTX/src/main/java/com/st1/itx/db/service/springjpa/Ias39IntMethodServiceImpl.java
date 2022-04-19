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
import com.st1.itx.db.domain.Ias39IntMethod;
import com.st1.itx.db.domain.Ias39IntMethodId;
import com.st1.itx.db.repository.online.Ias39IntMethodRepository;
import com.st1.itx.db.repository.day.Ias39IntMethodRepositoryDay;
import com.st1.itx.db.repository.mon.Ias39IntMethodRepositoryMon;
import com.st1.itx.db.repository.hist.Ias39IntMethodRepositoryHist;
import com.st1.itx.db.service.Ias39IntMethodService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias39IntMethodService")
@Repository
public class Ias39IntMethodServiceImpl extends ASpringJpaParm implements Ias39IntMethodService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Ias39IntMethodRepository ias39IntMethodRepos;

	@Autowired
	private Ias39IntMethodRepositoryDay ias39IntMethodReposDay;

	@Autowired
	private Ias39IntMethodRepositoryMon ias39IntMethodReposMon;

	@Autowired
	private Ias39IntMethodRepositoryHist ias39IntMethodReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(ias39IntMethodRepos);
		org.junit.Assert.assertNotNull(ias39IntMethodReposDay);
		org.junit.Assert.assertNotNull(ias39IntMethodReposMon);
		org.junit.Assert.assertNotNull(ias39IntMethodReposHist);
	}

	@Override
	public Ias39IntMethod findById(Ias39IntMethodId ias39IntMethodId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + ias39IntMethodId);
		Optional<Ias39IntMethod> ias39IntMethod = null;
		if (dbName.equals(ContentName.onDay))
			ias39IntMethod = ias39IntMethodReposDay.findById(ias39IntMethodId);
		else if (dbName.equals(ContentName.onMon))
			ias39IntMethod = ias39IntMethodReposMon.findById(ias39IntMethodId);
		else if (dbName.equals(ContentName.onHist))
			ias39IntMethod = ias39IntMethodReposHist.findById(ias39IntMethodId);
		else
			ias39IntMethod = ias39IntMethodRepos.findById(ias39IntMethodId);
		Ias39IntMethod obj = ias39IntMethod.isPresent() ? ias39IntMethod.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<Ias39IntMethod> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39IntMethod> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = ias39IntMethodReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39IntMethodReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39IntMethodReposHist.findAll(pageable);
		else
			slice = ias39IntMethodRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<Ias39IntMethod> findYearMonthEq(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39IntMethod> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findYearMonthEq " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
		if (dbName.equals(ContentName.onDay))
			slice = ias39IntMethodReposDay.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39IntMethodReposMon.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39IntMethodReposHist.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);
		else
			slice = ias39IntMethodRepos.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Ias39IntMethod holdById(Ias39IntMethodId ias39IntMethodId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39IntMethodId);
		Optional<Ias39IntMethod> ias39IntMethod = null;
		if (dbName.equals(ContentName.onDay))
			ias39IntMethod = ias39IntMethodReposDay.findByIas39IntMethodId(ias39IntMethodId);
		else if (dbName.equals(ContentName.onMon))
			ias39IntMethod = ias39IntMethodReposMon.findByIas39IntMethodId(ias39IntMethodId);
		else if (dbName.equals(ContentName.onHist))
			ias39IntMethod = ias39IntMethodReposHist.findByIas39IntMethodId(ias39IntMethodId);
		else
			ias39IntMethod = ias39IntMethodRepos.findByIas39IntMethodId(ias39IntMethodId);
		return ias39IntMethod.isPresent() ? ias39IntMethod.get() : null;
	}

	@Override
	public Ias39IntMethod holdById(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39IntMethod.getIas39IntMethodId());
		Optional<Ias39IntMethod> ias39IntMethodT = null;
		if (dbName.equals(ContentName.onDay))
			ias39IntMethodT = ias39IntMethodReposDay.findByIas39IntMethodId(ias39IntMethod.getIas39IntMethodId());
		else if (dbName.equals(ContentName.onMon))
			ias39IntMethodT = ias39IntMethodReposMon.findByIas39IntMethodId(ias39IntMethod.getIas39IntMethodId());
		else if (dbName.equals(ContentName.onHist))
			ias39IntMethodT = ias39IntMethodReposHist.findByIas39IntMethodId(ias39IntMethod.getIas39IntMethodId());
		else
			ias39IntMethodT = ias39IntMethodRepos.findByIas39IntMethodId(ias39IntMethod.getIas39IntMethodId());
		return ias39IntMethodT.isPresent() ? ias39IntMethodT.get() : null;
	}

	@Override
	public Ias39IntMethod insert(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + ias39IntMethod.getIas39IntMethodId());
		if (this.findById(ias39IntMethod.getIas39IntMethodId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			ias39IntMethod.setCreateEmpNo(empNot);

		if (ias39IntMethod.getLastUpdateEmpNo() == null || ias39IntMethod.getLastUpdateEmpNo().isEmpty())
			ias39IntMethod.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39IntMethodReposDay.saveAndFlush(ias39IntMethod);
		else if (dbName.equals(ContentName.onMon))
			return ias39IntMethodReposMon.saveAndFlush(ias39IntMethod);
		else if (dbName.equals(ContentName.onHist))
			return ias39IntMethodReposHist.saveAndFlush(ias39IntMethod);
		else
			return ias39IntMethodRepos.saveAndFlush(ias39IntMethod);
	}

	@Override
	public Ias39IntMethod update(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39IntMethod.getIas39IntMethodId());
		if (!empNot.isEmpty())
			ias39IntMethod.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39IntMethodReposDay.saveAndFlush(ias39IntMethod);
		else if (dbName.equals(ContentName.onMon))
			return ias39IntMethodReposMon.saveAndFlush(ias39IntMethod);
		else if (dbName.equals(ContentName.onHist))
			return ias39IntMethodReposHist.saveAndFlush(ias39IntMethod);
		else
			return ias39IntMethodRepos.saveAndFlush(ias39IntMethod);
	}

	@Override
	public Ias39IntMethod update2(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39IntMethod.getIas39IntMethodId());
		if (!empNot.isEmpty())
			ias39IntMethod.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			ias39IntMethodReposDay.saveAndFlush(ias39IntMethod);
		else if (dbName.equals(ContentName.onMon))
			ias39IntMethodReposMon.saveAndFlush(ias39IntMethod);
		else if (dbName.equals(ContentName.onHist))
			ias39IntMethodReposHist.saveAndFlush(ias39IntMethod);
		else
			ias39IntMethodRepos.saveAndFlush(ias39IntMethod);
		return this.findById(ias39IntMethod.getIas39IntMethodId());
	}

	@Override
	public void delete(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + ias39IntMethod.getIas39IntMethodId());
		if (dbName.equals(ContentName.onDay)) {
			ias39IntMethodReposDay.delete(ias39IntMethod);
			ias39IntMethodReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39IntMethodReposMon.delete(ias39IntMethod);
			ias39IntMethodReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39IntMethodReposHist.delete(ias39IntMethod);
			ias39IntMethodReposHist.flush();
		} else {
			ias39IntMethodRepos.delete(ias39IntMethod);
			ias39IntMethodRepos.flush();
		}
	}

	@Override
	public void insertAll(List<Ias39IntMethod> ias39IntMethod, TitaVo... titaVo) throws DBException {
		if (ias39IntMethod == null || ias39IntMethod.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (Ias39IntMethod t : ias39IntMethod) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			ias39IntMethod = ias39IntMethodReposDay.saveAll(ias39IntMethod);
			ias39IntMethodReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39IntMethod = ias39IntMethodReposMon.saveAll(ias39IntMethod);
			ias39IntMethodReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39IntMethod = ias39IntMethodReposHist.saveAll(ias39IntMethod);
			ias39IntMethodReposHist.flush();
		} else {
			ias39IntMethod = ias39IntMethodRepos.saveAll(ias39IntMethod);
			ias39IntMethodRepos.flush();
		}
	}

	@Override
	public void updateAll(List<Ias39IntMethod> ias39IntMethod, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (ias39IntMethod == null || ias39IntMethod.size() == 0)
			throw new DBException(6);

		for (Ias39IntMethod t : ias39IntMethod)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			ias39IntMethod = ias39IntMethodReposDay.saveAll(ias39IntMethod);
			ias39IntMethodReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39IntMethod = ias39IntMethodReposMon.saveAll(ias39IntMethod);
			ias39IntMethodReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39IntMethod = ias39IntMethodReposHist.saveAll(ias39IntMethod);
			ias39IntMethodReposHist.flush();
		} else {
			ias39IntMethod = ias39IntMethodRepos.saveAll(ias39IntMethod);
			ias39IntMethodRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<Ias39IntMethod> ias39IntMethod, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (ias39IntMethod == null || ias39IntMethod.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			ias39IntMethodReposDay.deleteAll(ias39IntMethod);
			ias39IntMethodReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39IntMethodReposMon.deleteAll(ias39IntMethod);
			ias39IntMethodReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39IntMethodReposHist.deleteAll(ias39IntMethod);
			ias39IntMethodReposHist.flush();
		} else {
			ias39IntMethodRepos.deleteAll(ias39IntMethod);
			ias39IntMethodRepos.flush();
		}
	}

}
