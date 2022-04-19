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
import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.domain.CdAcBookId;
import com.st1.itx.db.repository.online.CdAcBookRepository;
import com.st1.itx.db.repository.day.CdAcBookRepositoryDay;
import com.st1.itx.db.repository.mon.CdAcBookRepositoryMon;
import com.st1.itx.db.repository.hist.CdAcBookRepositoryHist;
import com.st1.itx.db.service.CdAcBookService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdAcBookService")
@Repository
public class CdAcBookServiceImpl extends ASpringJpaParm implements CdAcBookService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdAcBookRepository cdAcBookRepos;

	@Autowired
	private CdAcBookRepositoryDay cdAcBookReposDay;

	@Autowired
	private CdAcBookRepositoryMon cdAcBookReposMon;

	@Autowired
	private CdAcBookRepositoryHist cdAcBookReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdAcBookRepos);
		org.junit.Assert.assertNotNull(cdAcBookReposDay);
		org.junit.Assert.assertNotNull(cdAcBookReposMon);
		org.junit.Assert.assertNotNull(cdAcBookReposHist);
	}

	@Override
	public CdAcBook findById(CdAcBookId cdAcBookId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdAcBookId);
		Optional<CdAcBook> cdAcBook = null;
		if (dbName.equals(ContentName.onDay))
			cdAcBook = cdAcBookReposDay.findById(cdAcBookId);
		else if (dbName.equals(ContentName.onMon))
			cdAcBook = cdAcBookReposMon.findById(cdAcBookId);
		else if (dbName.equals(ContentName.onHist))
			cdAcBook = cdAcBookReposHist.findById(cdAcBookId);
		else
			cdAcBook = cdAcBookRepos.findById(cdAcBookId);
		CdAcBook obj = cdAcBook.isPresent() ? cdAcBook.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdAcBook> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdAcBook> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcBookCode", "AcSubBookCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcBookCode", "AcSubBookCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdAcBookReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdAcBookReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdAcBookReposHist.findAll(pageable);
		else
			slice = cdAcBookRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdAcBook> acBookAssignSeqGeq(String acBookCode_0, int assignSeq_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdAcBook> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("acBookAssignSeqGeq " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " assignSeq_1 : " + assignSeq_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdAcBookReposDay.findAllByAcBookCodeIsAndAssignSeqGreaterThanEqualOrderByAssignSeqAsc(acBookCode_0, assignSeq_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdAcBookReposMon.findAllByAcBookCodeIsAndAssignSeqGreaterThanEqualOrderByAssignSeqAsc(acBookCode_0, assignSeq_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdAcBookReposHist.findAllByAcBookCodeIsAndAssignSeqGreaterThanEqualOrderByAssignSeqAsc(acBookCode_0, assignSeq_1, pageable);
		else
			slice = cdAcBookRepos.findAllByAcBookCodeIsAndAssignSeqGreaterThanEqualOrderByAssignSeqAsc(acBookCode_0, assignSeq_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdAcBook> findAcBookCode(String acBookCode_0, String acSubBookCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdAcBook> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findAcBookCode " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " + acSubBookCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdAcBookReposDay.findAllByAcBookCodeIsAndAcSubBookCodeIsOrderByAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdAcBookReposMon.findAllByAcBookCodeIsAndAcSubBookCodeIsOrderByAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdAcBookReposHist.findAllByAcBookCodeIsAndAcSubBookCodeIsOrderByAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, pageable);
		else
			slice = cdAcBookRepos.findAllByAcBookCodeIsAndAcSubBookCodeIsOrderByAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdAcBook holdById(CdAcBookId cdAcBookId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdAcBookId);
		Optional<CdAcBook> cdAcBook = null;
		if (dbName.equals(ContentName.onDay))
			cdAcBook = cdAcBookReposDay.findByCdAcBookId(cdAcBookId);
		else if (dbName.equals(ContentName.onMon))
			cdAcBook = cdAcBookReposMon.findByCdAcBookId(cdAcBookId);
		else if (dbName.equals(ContentName.onHist))
			cdAcBook = cdAcBookReposHist.findByCdAcBookId(cdAcBookId);
		else
			cdAcBook = cdAcBookRepos.findByCdAcBookId(cdAcBookId);
		return cdAcBook.isPresent() ? cdAcBook.get() : null;
	}

	@Override
	public CdAcBook holdById(CdAcBook cdAcBook, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdAcBook.getCdAcBookId());
		Optional<CdAcBook> cdAcBookT = null;
		if (dbName.equals(ContentName.onDay))
			cdAcBookT = cdAcBookReposDay.findByCdAcBookId(cdAcBook.getCdAcBookId());
		else if (dbName.equals(ContentName.onMon))
			cdAcBookT = cdAcBookReposMon.findByCdAcBookId(cdAcBook.getCdAcBookId());
		else if (dbName.equals(ContentName.onHist))
			cdAcBookT = cdAcBookReposHist.findByCdAcBookId(cdAcBook.getCdAcBookId());
		else
			cdAcBookT = cdAcBookRepos.findByCdAcBookId(cdAcBook.getCdAcBookId());
		return cdAcBookT.isPresent() ? cdAcBookT.get() : null;
	}

	@Override
	public CdAcBook insert(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdAcBook.getCdAcBookId());
		if (this.findById(cdAcBook.getCdAcBookId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdAcBook.setCreateEmpNo(empNot);

		if (cdAcBook.getLastUpdateEmpNo() == null || cdAcBook.getLastUpdateEmpNo().isEmpty())
			cdAcBook.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdAcBookReposDay.saveAndFlush(cdAcBook);
		else if (dbName.equals(ContentName.onMon))
			return cdAcBookReposMon.saveAndFlush(cdAcBook);
		else if (dbName.equals(ContentName.onHist))
			return cdAcBookReposHist.saveAndFlush(cdAcBook);
		else
			return cdAcBookRepos.saveAndFlush(cdAcBook);
	}

	@Override
	public CdAcBook update(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdAcBook.getCdAcBookId());
		if (!empNot.isEmpty())
			cdAcBook.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdAcBookReposDay.saveAndFlush(cdAcBook);
		else if (dbName.equals(ContentName.onMon))
			return cdAcBookReposMon.saveAndFlush(cdAcBook);
		else if (dbName.equals(ContentName.onHist))
			return cdAcBookReposHist.saveAndFlush(cdAcBook);
		else
			return cdAcBookRepos.saveAndFlush(cdAcBook);
	}

	@Override
	public CdAcBook update2(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdAcBook.getCdAcBookId());
		if (!empNot.isEmpty())
			cdAcBook.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdAcBookReposDay.saveAndFlush(cdAcBook);
		else if (dbName.equals(ContentName.onMon))
			cdAcBookReposMon.saveAndFlush(cdAcBook);
		else if (dbName.equals(ContentName.onHist))
			cdAcBookReposHist.saveAndFlush(cdAcBook);
		else
			cdAcBookRepos.saveAndFlush(cdAcBook);
		return this.findById(cdAcBook.getCdAcBookId());
	}

	@Override
	public void delete(CdAcBook cdAcBook, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdAcBook.getCdAcBookId());
		if (dbName.equals(ContentName.onDay)) {
			cdAcBookReposDay.delete(cdAcBook);
			cdAcBookReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAcBookReposMon.delete(cdAcBook);
			cdAcBookReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAcBookReposHist.delete(cdAcBook);
			cdAcBookReposHist.flush();
		} else {
			cdAcBookRepos.delete(cdAcBook);
			cdAcBookRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdAcBook> cdAcBook, TitaVo... titaVo) throws DBException {
		if (cdAcBook == null || cdAcBook.size() == 0)
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
		for (CdAcBook t : cdAcBook) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdAcBook = cdAcBookReposDay.saveAll(cdAcBook);
			cdAcBookReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAcBook = cdAcBookReposMon.saveAll(cdAcBook);
			cdAcBookReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAcBook = cdAcBookReposHist.saveAll(cdAcBook);
			cdAcBookReposHist.flush();
		} else {
			cdAcBook = cdAcBookRepos.saveAll(cdAcBook);
			cdAcBookRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdAcBook> cdAcBook, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdAcBook == null || cdAcBook.size() == 0)
			throw new DBException(6);

		for (CdAcBook t : cdAcBook)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdAcBook = cdAcBookReposDay.saveAll(cdAcBook);
			cdAcBookReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAcBook = cdAcBookReposMon.saveAll(cdAcBook);
			cdAcBookReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAcBook = cdAcBookReposHist.saveAll(cdAcBook);
			cdAcBookReposHist.flush();
		} else {
			cdAcBook = cdAcBookRepos.saveAll(cdAcBook);
			cdAcBookRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdAcBook> cdAcBook, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdAcBook == null || cdAcBook.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdAcBookReposDay.deleteAll(cdAcBook);
			cdAcBookReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAcBookReposMon.deleteAll(cdAcBook);
			cdAcBookReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAcBookReposHist.deleteAll(cdAcBook);
			cdAcBookReposHist.flush();
		} else {
			cdAcBookRepos.deleteAll(cdAcBook);
			cdAcBookRepos.flush();
		}
	}

}
