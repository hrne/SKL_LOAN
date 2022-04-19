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
import com.st1.itx.db.domain.Ias39Loss;
import com.st1.itx.db.domain.Ias39LossId;
import com.st1.itx.db.repository.online.Ias39LossRepository;
import com.st1.itx.db.repository.day.Ias39LossRepositoryDay;
import com.st1.itx.db.repository.mon.Ias39LossRepositoryMon;
import com.st1.itx.db.repository.hist.Ias39LossRepositoryHist;
import com.st1.itx.db.service.Ias39LossService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias39LossService")
@Repository
public class Ias39LossServiceImpl extends ASpringJpaParm implements Ias39LossService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Ias39LossRepository ias39LossRepos;

	@Autowired
	private Ias39LossRepositoryDay ias39LossReposDay;

	@Autowired
	private Ias39LossRepositoryMon ias39LossReposMon;

	@Autowired
	private Ias39LossRepositoryHist ias39LossReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(ias39LossRepos);
		org.junit.Assert.assertNotNull(ias39LossReposDay);
		org.junit.Assert.assertNotNull(ias39LossReposMon);
		org.junit.Assert.assertNotNull(ias39LossReposHist);
	}

	@Override
	public Ias39Loss findById(Ias39LossId ias39LossId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + ias39LossId);
		Optional<Ias39Loss> ias39Loss = null;
		if (dbName.equals(ContentName.onDay))
			ias39Loss = ias39LossReposDay.findById(ias39LossId);
		else if (dbName.equals(ContentName.onMon))
			ias39Loss = ias39LossReposMon.findById(ias39LossId);
		else if (dbName.equals(ContentName.onHist))
			ias39Loss = ias39LossReposHist.findById(ias39LossId);
		else
			ias39Loss = ias39LossRepos.findById(ias39LossId);
		Ias39Loss obj = ias39Loss.isPresent() ? ias39Loss.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<Ias39Loss> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39Loss> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "MarkDate"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "MarkDate"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = ias39LossReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39LossReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39LossReposHist.findAll(pageable);
		else
			slice = ias39LossRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<Ias39Loss> findFacmNo(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39Loss> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = ias39LossReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscMarkDateAsc(custNo_0, facmNo_1, facmNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39LossReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscMarkDateAsc(custNo_0, facmNo_1, facmNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39LossReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscMarkDateAsc(custNo_0, facmNo_1, facmNo_2, pageable);
		else
			slice = ias39LossRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscMarkDateAsc(custNo_0, facmNo_1, facmNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<Ias39Loss> findCustNo(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39Loss> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " + custNo_1 + " facmNo_2 : " + facmNo_2 + " facmNo_3 : " + facmNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = ias39LossReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAscMarkDateAsc(custNo_0, custNo_1,
					facmNo_2, facmNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39LossReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAscMarkDateAsc(custNo_0, custNo_1,
					facmNo_2, facmNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39LossReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAscMarkDateAsc(custNo_0, custNo_1,
					facmNo_2, facmNo_3, pageable);
		else
			slice = ias39LossRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAscMarkDateAsc(custNo_0, custNo_1,
					facmNo_2, facmNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Ias39Loss holdById(Ias39LossId ias39LossId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39LossId);
		Optional<Ias39Loss> ias39Loss = null;
		if (dbName.equals(ContentName.onDay))
			ias39Loss = ias39LossReposDay.findByIas39LossId(ias39LossId);
		else if (dbName.equals(ContentName.onMon))
			ias39Loss = ias39LossReposMon.findByIas39LossId(ias39LossId);
		else if (dbName.equals(ContentName.onHist))
			ias39Loss = ias39LossReposHist.findByIas39LossId(ias39LossId);
		else
			ias39Loss = ias39LossRepos.findByIas39LossId(ias39LossId);
		return ias39Loss.isPresent() ? ias39Loss.get() : null;
	}

	@Override
	public Ias39Loss holdById(Ias39Loss ias39Loss, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39Loss.getIas39LossId());
		Optional<Ias39Loss> ias39LossT = null;
		if (dbName.equals(ContentName.onDay))
			ias39LossT = ias39LossReposDay.findByIas39LossId(ias39Loss.getIas39LossId());
		else if (dbName.equals(ContentName.onMon))
			ias39LossT = ias39LossReposMon.findByIas39LossId(ias39Loss.getIas39LossId());
		else if (dbName.equals(ContentName.onHist))
			ias39LossT = ias39LossReposHist.findByIas39LossId(ias39Loss.getIas39LossId());
		else
			ias39LossT = ias39LossRepos.findByIas39LossId(ias39Loss.getIas39LossId());
		return ias39LossT.isPresent() ? ias39LossT.get() : null;
	}

	@Override
	public Ias39Loss insert(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + ias39Loss.getIas39LossId());
		if (this.findById(ias39Loss.getIas39LossId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			ias39Loss.setCreateEmpNo(empNot);

		if (ias39Loss.getLastUpdateEmpNo() == null || ias39Loss.getLastUpdateEmpNo().isEmpty())
			ias39Loss.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39LossReposDay.saveAndFlush(ias39Loss);
		else if (dbName.equals(ContentName.onMon))
			return ias39LossReposMon.saveAndFlush(ias39Loss);
		else if (dbName.equals(ContentName.onHist))
			return ias39LossReposHist.saveAndFlush(ias39Loss);
		else
			return ias39LossRepos.saveAndFlush(ias39Loss);
	}

	@Override
	public Ias39Loss update(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39Loss.getIas39LossId());
		if (!empNot.isEmpty())
			ias39Loss.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39LossReposDay.saveAndFlush(ias39Loss);
		else if (dbName.equals(ContentName.onMon))
			return ias39LossReposMon.saveAndFlush(ias39Loss);
		else if (dbName.equals(ContentName.onHist))
			return ias39LossReposHist.saveAndFlush(ias39Loss);
		else
			return ias39LossRepos.saveAndFlush(ias39Loss);
	}

	@Override
	public Ias39Loss update2(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39Loss.getIas39LossId());
		if (!empNot.isEmpty())
			ias39Loss.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			ias39LossReposDay.saveAndFlush(ias39Loss);
		else if (dbName.equals(ContentName.onMon))
			ias39LossReposMon.saveAndFlush(ias39Loss);
		else if (dbName.equals(ContentName.onHist))
			ias39LossReposHist.saveAndFlush(ias39Loss);
		else
			ias39LossRepos.saveAndFlush(ias39Loss);
		return this.findById(ias39Loss.getIas39LossId());
	}

	@Override
	public void delete(Ias39Loss ias39Loss, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + ias39Loss.getIas39LossId());
		if (dbName.equals(ContentName.onDay)) {
			ias39LossReposDay.delete(ias39Loss);
			ias39LossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39LossReposMon.delete(ias39Loss);
			ias39LossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39LossReposHist.delete(ias39Loss);
			ias39LossReposHist.flush();
		} else {
			ias39LossRepos.delete(ias39Loss);
			ias39LossRepos.flush();
		}
	}

	@Override
	public void insertAll(List<Ias39Loss> ias39Loss, TitaVo... titaVo) throws DBException {
		if (ias39Loss == null || ias39Loss.size() == 0)
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
		for (Ias39Loss t : ias39Loss) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			ias39Loss = ias39LossReposDay.saveAll(ias39Loss);
			ias39LossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39Loss = ias39LossReposMon.saveAll(ias39Loss);
			ias39LossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39Loss = ias39LossReposHist.saveAll(ias39Loss);
			ias39LossReposHist.flush();
		} else {
			ias39Loss = ias39LossRepos.saveAll(ias39Loss);
			ias39LossRepos.flush();
		}
	}

	@Override
	public void updateAll(List<Ias39Loss> ias39Loss, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (ias39Loss == null || ias39Loss.size() == 0)
			throw new DBException(6);

		for (Ias39Loss t : ias39Loss)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			ias39Loss = ias39LossReposDay.saveAll(ias39Loss);
			ias39LossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39Loss = ias39LossReposMon.saveAll(ias39Loss);
			ias39LossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39Loss = ias39LossReposHist.saveAll(ias39Loss);
			ias39LossReposHist.flush();
		} else {
			ias39Loss = ias39LossRepos.saveAll(ias39Loss);
			ias39LossRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<Ias39Loss> ias39Loss, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (ias39Loss == null || ias39Loss.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			ias39LossReposDay.deleteAll(ias39Loss);
			ias39LossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39LossReposMon.deleteAll(ias39Loss);
			ias39LossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39LossReposHist.deleteAll(ias39Loss);
			ias39LossReposHist.flush();
		} else {
			ias39LossRepos.deleteAll(ias39Loss);
			ias39LossRepos.flush();
		}
	}

}
