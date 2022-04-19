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
import com.st1.itx.db.domain.MlaundryChkDtl;
import com.st1.itx.db.domain.MlaundryChkDtlId;
import com.st1.itx.db.repository.online.MlaundryChkDtlRepository;
import com.st1.itx.db.repository.day.MlaundryChkDtlRepositoryDay;
import com.st1.itx.db.repository.mon.MlaundryChkDtlRepositoryMon;
import com.st1.itx.db.repository.hist.MlaundryChkDtlRepositoryHist;
import com.st1.itx.db.service.MlaundryChkDtlService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("mlaundryChkDtlService")
@Repository
public class MlaundryChkDtlServiceImpl extends ASpringJpaParm implements MlaundryChkDtlService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MlaundryChkDtlRepository mlaundryChkDtlRepos;

	@Autowired
	private MlaundryChkDtlRepositoryDay mlaundryChkDtlReposDay;

	@Autowired
	private MlaundryChkDtlRepositoryMon mlaundryChkDtlReposMon;

	@Autowired
	private MlaundryChkDtlRepositoryHist mlaundryChkDtlReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(mlaundryChkDtlRepos);
		org.junit.Assert.assertNotNull(mlaundryChkDtlReposDay);
		org.junit.Assert.assertNotNull(mlaundryChkDtlReposMon);
		org.junit.Assert.assertNotNull(mlaundryChkDtlReposHist);
	}

	@Override
	public MlaundryChkDtl findById(MlaundryChkDtlId mlaundryChkDtlId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + mlaundryChkDtlId);
		Optional<MlaundryChkDtl> mlaundryChkDtl = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryChkDtl = mlaundryChkDtlReposDay.findById(mlaundryChkDtlId);
		else if (dbName.equals(ContentName.onMon))
			mlaundryChkDtl = mlaundryChkDtlReposMon.findById(mlaundryChkDtlId);
		else if (dbName.equals(ContentName.onHist))
			mlaundryChkDtl = mlaundryChkDtlReposHist.findById(mlaundryChkDtlId);
		else
			mlaundryChkDtl = mlaundryChkDtlRepos.findById(mlaundryChkDtlId);
		MlaundryChkDtl obj = mlaundryChkDtl.isPresent() ? mlaundryChkDtl.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MlaundryChkDtl> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryChkDtl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EntryDate", "Factor", "CustNo", "DtlSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EntryDate", "Factor", "CustNo", "DtlSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryChkDtlReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryChkDtlReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryChkDtlReposHist.findAll(pageable);
		else
			slice = mlaundryChkDtlRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryChkDtl> findEntryDateRange(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryChkDtl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findEntryDateRange " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " + entryDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryChkDtlReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscFactorAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryChkDtlReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscFactorAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryChkDtlReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscFactorAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, pageable);
		else
			slice = mlaundryChkDtlRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscFactorAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryChkDtl> findFactor(int entryDate_0, int entryDate_1, int factor_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryChkDtl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFactor " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " + entryDate_1 + " factor_2 : " + factor_2);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryChkDtlReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsOrderByEntryDateAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, factor_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryChkDtlReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsOrderByEntryDateAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, factor_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryChkDtlReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsOrderByEntryDateAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, factor_2, pageable);
		else
			slice = mlaundryChkDtlRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsOrderByEntryDateAscCustNoAscDtlSeqAsc(entryDate_0, entryDate_1, factor_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MlaundryChkDtl holdById(MlaundryChkDtlId mlaundryChkDtlId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + mlaundryChkDtlId);
		Optional<MlaundryChkDtl> mlaundryChkDtl = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryChkDtl = mlaundryChkDtlReposDay.findByMlaundryChkDtlId(mlaundryChkDtlId);
		else if (dbName.equals(ContentName.onMon))
			mlaundryChkDtl = mlaundryChkDtlReposMon.findByMlaundryChkDtlId(mlaundryChkDtlId);
		else if (dbName.equals(ContentName.onHist))
			mlaundryChkDtl = mlaundryChkDtlReposHist.findByMlaundryChkDtlId(mlaundryChkDtlId);
		else
			mlaundryChkDtl = mlaundryChkDtlRepos.findByMlaundryChkDtlId(mlaundryChkDtlId);
		return mlaundryChkDtl.isPresent() ? mlaundryChkDtl.get() : null;
	}

	@Override
	public MlaundryChkDtl holdById(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + mlaundryChkDtl.getMlaundryChkDtlId());
		Optional<MlaundryChkDtl> mlaundryChkDtlT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryChkDtlT = mlaundryChkDtlReposDay.findByMlaundryChkDtlId(mlaundryChkDtl.getMlaundryChkDtlId());
		else if (dbName.equals(ContentName.onMon))
			mlaundryChkDtlT = mlaundryChkDtlReposMon.findByMlaundryChkDtlId(mlaundryChkDtl.getMlaundryChkDtlId());
		else if (dbName.equals(ContentName.onHist))
			mlaundryChkDtlT = mlaundryChkDtlReposHist.findByMlaundryChkDtlId(mlaundryChkDtl.getMlaundryChkDtlId());
		else
			mlaundryChkDtlT = mlaundryChkDtlRepos.findByMlaundryChkDtlId(mlaundryChkDtl.getMlaundryChkDtlId());
		return mlaundryChkDtlT.isPresent() ? mlaundryChkDtlT.get() : null;
	}

	@Override
	public MlaundryChkDtl insert(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + mlaundryChkDtl.getMlaundryChkDtlId());
		if (this.findById(mlaundryChkDtl.getMlaundryChkDtlId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			mlaundryChkDtl.setCreateEmpNo(empNot);

		if (mlaundryChkDtl.getLastUpdateEmpNo() == null || mlaundryChkDtl.getLastUpdateEmpNo().isEmpty())
			mlaundryChkDtl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryChkDtlReposDay.saveAndFlush(mlaundryChkDtl);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryChkDtlReposMon.saveAndFlush(mlaundryChkDtl);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryChkDtlReposHist.saveAndFlush(mlaundryChkDtl);
		else
			return mlaundryChkDtlRepos.saveAndFlush(mlaundryChkDtl);
	}

	@Override
	public MlaundryChkDtl update(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryChkDtl.getMlaundryChkDtlId());
		if (!empNot.isEmpty())
			mlaundryChkDtl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryChkDtlReposDay.saveAndFlush(mlaundryChkDtl);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryChkDtlReposMon.saveAndFlush(mlaundryChkDtl);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryChkDtlReposHist.saveAndFlush(mlaundryChkDtl);
		else
			return mlaundryChkDtlRepos.saveAndFlush(mlaundryChkDtl);
	}

	@Override
	public MlaundryChkDtl update2(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryChkDtl.getMlaundryChkDtlId());
		if (!empNot.isEmpty())
			mlaundryChkDtl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			mlaundryChkDtlReposDay.saveAndFlush(mlaundryChkDtl);
		else if (dbName.equals(ContentName.onMon))
			mlaundryChkDtlReposMon.saveAndFlush(mlaundryChkDtl);
		else if (dbName.equals(ContentName.onHist))
			mlaundryChkDtlReposHist.saveAndFlush(mlaundryChkDtl);
		else
			mlaundryChkDtlRepos.saveAndFlush(mlaundryChkDtl);
		return this.findById(mlaundryChkDtl.getMlaundryChkDtlId());
	}

	@Override
	public void delete(MlaundryChkDtl mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + mlaundryChkDtl.getMlaundryChkDtlId());
		if (dbName.equals(ContentName.onDay)) {
			mlaundryChkDtlReposDay.delete(mlaundryChkDtl);
			mlaundryChkDtlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryChkDtlReposMon.delete(mlaundryChkDtl);
			mlaundryChkDtlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryChkDtlReposHist.delete(mlaundryChkDtl);
			mlaundryChkDtlReposHist.flush();
		} else {
			mlaundryChkDtlRepos.delete(mlaundryChkDtl);
			mlaundryChkDtlRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MlaundryChkDtl> mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		if (mlaundryChkDtl == null || mlaundryChkDtl.size() == 0)
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
		for (MlaundryChkDtl t : mlaundryChkDtl) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			mlaundryChkDtl = mlaundryChkDtlReposDay.saveAll(mlaundryChkDtl);
			mlaundryChkDtlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryChkDtl = mlaundryChkDtlReposMon.saveAll(mlaundryChkDtl);
			mlaundryChkDtlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryChkDtl = mlaundryChkDtlReposHist.saveAll(mlaundryChkDtl);
			mlaundryChkDtlReposHist.flush();
		} else {
			mlaundryChkDtl = mlaundryChkDtlRepos.saveAll(mlaundryChkDtl);
			mlaundryChkDtlRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MlaundryChkDtl> mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (mlaundryChkDtl == null || mlaundryChkDtl.size() == 0)
			throw new DBException(6);

		for (MlaundryChkDtl t : mlaundryChkDtl)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			mlaundryChkDtl = mlaundryChkDtlReposDay.saveAll(mlaundryChkDtl);
			mlaundryChkDtlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryChkDtl = mlaundryChkDtlReposMon.saveAll(mlaundryChkDtl);
			mlaundryChkDtlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryChkDtl = mlaundryChkDtlReposHist.saveAll(mlaundryChkDtl);
			mlaundryChkDtlReposHist.flush();
		} else {
			mlaundryChkDtl = mlaundryChkDtlRepos.saveAll(mlaundryChkDtl);
			mlaundryChkDtlRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MlaundryChkDtl> mlaundryChkDtl, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (mlaundryChkDtl == null || mlaundryChkDtl.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			mlaundryChkDtlReposDay.deleteAll(mlaundryChkDtl);
			mlaundryChkDtlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryChkDtlReposMon.deleteAll(mlaundryChkDtl);
			mlaundryChkDtlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryChkDtlReposHist.deleteAll(mlaundryChkDtl);
			mlaundryChkDtlReposHist.flush();
		} else {
			mlaundryChkDtlRepos.deleteAll(mlaundryChkDtl);
			mlaundryChkDtlRepos.flush();
		}
	}

}
