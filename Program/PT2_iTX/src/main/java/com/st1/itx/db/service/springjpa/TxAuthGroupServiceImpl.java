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
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.repository.online.TxAuthGroupRepository;
import com.st1.itx.db.repository.day.TxAuthGroupRepositoryDay;
import com.st1.itx.db.repository.mon.TxAuthGroupRepositoryMon;
import com.st1.itx.db.repository.hist.TxAuthGroupRepositoryHist;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAuthGroupService")
@Repository
public class TxAuthGroupServiceImpl extends ASpringJpaParm implements TxAuthGroupService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxAuthGroupRepository txAuthGroupRepos;

	@Autowired
	private TxAuthGroupRepositoryDay txAuthGroupReposDay;

	@Autowired
	private TxAuthGroupRepositoryMon txAuthGroupReposMon;

	@Autowired
	private TxAuthGroupRepositoryHist txAuthGroupReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txAuthGroupRepos);
		org.junit.Assert.assertNotNull(txAuthGroupReposDay);
		org.junit.Assert.assertNotNull(txAuthGroupReposMon);
		org.junit.Assert.assertNotNull(txAuthGroupReposHist);
	}

	@Override
	public TxAuthGroup findById(String authNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + authNo);
		Optional<TxAuthGroup> txAuthGroup = null;
		if (dbName.equals(ContentName.onDay))
			txAuthGroup = txAuthGroupReposDay.findById(authNo);
		else if (dbName.equals(ContentName.onMon))
			txAuthGroup = txAuthGroupReposMon.findById(authNo);
		else if (dbName.equals(ContentName.onHist))
			txAuthGroup = txAuthGroupReposHist.findById(authNo);
		else
			txAuthGroup = txAuthGroupRepos.findById(authNo);
		TxAuthGroup obj = txAuthGroup.isPresent() ? txAuthGroup.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxAuthGroup> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAuthGroup> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AuthNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AuthNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txAuthGroupReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAuthGroupReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAuthGroupReposHist.findAll(pageable);
		else
			slice = txAuthGroupRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAuthGroup> AuthNoLike(String authNo_0, int status_1, int status_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAuthGroup> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("AuthNoLike " + dbName + " : " + "authNo_0 : " + authNo_0 + " status_1 : " + status_1 + " status_2 : " + status_2);
		if (dbName.equals(ContentName.onDay))
			slice = txAuthGroupReposDay.findAllByAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(authNo_0, status_1, status_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAuthGroupReposMon.findAllByAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(authNo_0, status_1, status_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAuthGroupReposHist.findAllByAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(authNo_0, status_1, status_2, pageable);
		else
			slice = txAuthGroupRepos.findAllByAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(authNo_0, status_1, status_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAuthGroup> BranchAll(String branchNo_0, int levelFg_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAuthGroup> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("BranchAll " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " levelFg_1 : " + levelFg_1);
		if (dbName.equals(ContentName.onDay))
			slice = txAuthGroupReposDay.findAllByBranchNoIsAndLevelFgIsOrderByAuthNoAsc(branchNo_0, levelFg_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAuthGroupReposMon.findAllByBranchNoIsAndLevelFgIsOrderByAuthNoAsc(branchNo_0, levelFg_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAuthGroupReposHist.findAllByBranchNoIsAndLevelFgIsOrderByAuthNoAsc(branchNo_0, levelFg_1, pageable);
		else
			slice = txAuthGroupRepos.findAllByBranchNoIsAndLevelFgIsOrderByAuthNoAsc(branchNo_0, levelFg_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAuthGroup> BranchAuthNo(String branchNo_0, String authNo_1, int status_2, int status_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAuthGroup> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("BranchAuthNo " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " authNo_1 : " + authNo_1 + " status_2 : " + status_2 + " status_3 : " + status_3);
		if (dbName.equals(ContentName.onDay))
			slice = txAuthGroupReposDay.findAllByBranchNoIsAndAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(branchNo_0, authNo_1, status_2, status_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAuthGroupReposMon.findAllByBranchNoIsAndAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(branchNo_0, authNo_1, status_2, status_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAuthGroupReposHist.findAllByBranchNoIsAndAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(branchNo_0, authNo_1, status_2, status_3, pageable);
		else
			slice = txAuthGroupRepos.findAllByBranchNoIsAndAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(branchNo_0, authNo_1, status_2, status_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxAuthGroup holdById(String authNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + authNo);
		Optional<TxAuthGroup> txAuthGroup = null;
		if (dbName.equals(ContentName.onDay))
			txAuthGroup = txAuthGroupReposDay.findByAuthNo(authNo);
		else if (dbName.equals(ContentName.onMon))
			txAuthGroup = txAuthGroupReposMon.findByAuthNo(authNo);
		else if (dbName.equals(ContentName.onHist))
			txAuthGroup = txAuthGroupReposHist.findByAuthNo(authNo);
		else
			txAuthGroup = txAuthGroupRepos.findByAuthNo(authNo);
		return txAuthGroup.isPresent() ? txAuthGroup.get() : null;
	}

	@Override
	public TxAuthGroup holdById(TxAuthGroup txAuthGroup, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txAuthGroup.getAuthNo());
		Optional<TxAuthGroup> txAuthGroupT = null;
		if (dbName.equals(ContentName.onDay))
			txAuthGroupT = txAuthGroupReposDay.findByAuthNo(txAuthGroup.getAuthNo());
		else if (dbName.equals(ContentName.onMon))
			txAuthGroupT = txAuthGroupReposMon.findByAuthNo(txAuthGroup.getAuthNo());
		else if (dbName.equals(ContentName.onHist))
			txAuthGroupT = txAuthGroupReposHist.findByAuthNo(txAuthGroup.getAuthNo());
		else
			txAuthGroupT = txAuthGroupRepos.findByAuthNo(txAuthGroup.getAuthNo());
		return txAuthGroupT.isPresent() ? txAuthGroupT.get() : null;
	}

	@Override
	public TxAuthGroup insert(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + txAuthGroup.getAuthNo());
		if (this.findById(txAuthGroup.getAuthNo(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txAuthGroup.setCreateEmpNo(empNot);

		if (txAuthGroup.getLastUpdateEmpNo() == null || txAuthGroup.getLastUpdateEmpNo().isEmpty())
			txAuthGroup.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAuthGroupReposDay.saveAndFlush(txAuthGroup);
		else if (dbName.equals(ContentName.onMon))
			return txAuthGroupReposMon.saveAndFlush(txAuthGroup);
		else if (dbName.equals(ContentName.onHist))
			return txAuthGroupReposHist.saveAndFlush(txAuthGroup);
		else
			return txAuthGroupRepos.saveAndFlush(txAuthGroup);
	}

	@Override
	public TxAuthGroup update(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txAuthGroup.getAuthNo());
		if (!empNot.isEmpty())
			txAuthGroup.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAuthGroupReposDay.saveAndFlush(txAuthGroup);
		else if (dbName.equals(ContentName.onMon))
			return txAuthGroupReposMon.saveAndFlush(txAuthGroup);
		else if (dbName.equals(ContentName.onHist))
			return txAuthGroupReposHist.saveAndFlush(txAuthGroup);
		else
			return txAuthGroupRepos.saveAndFlush(txAuthGroup);
	}

	@Override
	public TxAuthGroup update2(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txAuthGroup.getAuthNo());
		if (!empNot.isEmpty())
			txAuthGroup.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txAuthGroupReposDay.saveAndFlush(txAuthGroup);
		else if (dbName.equals(ContentName.onMon))
			txAuthGroupReposMon.saveAndFlush(txAuthGroup);
		else if (dbName.equals(ContentName.onHist))
			txAuthGroupReposHist.saveAndFlush(txAuthGroup);
		else
			txAuthGroupRepos.saveAndFlush(txAuthGroup);
		return this.findById(txAuthGroup.getAuthNo());
	}

	@Override
	public void delete(TxAuthGroup txAuthGroup, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txAuthGroup.getAuthNo());
		if (dbName.equals(ContentName.onDay)) {
			txAuthGroupReposDay.delete(txAuthGroup);
			txAuthGroupReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAuthGroupReposMon.delete(txAuthGroup);
			txAuthGroupReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAuthGroupReposHist.delete(txAuthGroup);
			txAuthGroupReposHist.flush();
		} else {
			txAuthGroupRepos.delete(txAuthGroup);
			txAuthGroupRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxAuthGroup> txAuthGroup, TitaVo... titaVo) throws DBException {
		if (txAuthGroup == null || txAuthGroup.size() == 0)
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
		for (TxAuthGroup t : txAuthGroup) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txAuthGroup = txAuthGroupReposDay.saveAll(txAuthGroup);
			txAuthGroupReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAuthGroup = txAuthGroupReposMon.saveAll(txAuthGroup);
			txAuthGroupReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAuthGroup = txAuthGroupReposHist.saveAll(txAuthGroup);
			txAuthGroupReposHist.flush();
		} else {
			txAuthGroup = txAuthGroupRepos.saveAll(txAuthGroup);
			txAuthGroupRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxAuthGroup> txAuthGroup, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (txAuthGroup == null || txAuthGroup.size() == 0)
			throw new DBException(6);

		for (TxAuthGroup t : txAuthGroup)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txAuthGroup = txAuthGroupReposDay.saveAll(txAuthGroup);
			txAuthGroupReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAuthGroup = txAuthGroupReposMon.saveAll(txAuthGroup);
			txAuthGroupReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAuthGroup = txAuthGroupReposHist.saveAll(txAuthGroup);
			txAuthGroupReposHist.flush();
		} else {
			txAuthGroup = txAuthGroupRepos.saveAll(txAuthGroup);
			txAuthGroupRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxAuthGroup> txAuthGroup, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txAuthGroup == null || txAuthGroup.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txAuthGroupReposDay.deleteAll(txAuthGroup);
			txAuthGroupReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAuthGroupReposMon.deleteAll(txAuthGroup);
			txAuthGroupReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAuthGroupReposHist.deleteAll(txAuthGroup);
			txAuthGroupReposHist.flush();
		} else {
			txAuthGroupRepos.deleteAll(txAuthGroup);
			txAuthGroupRepos.flush();
		}
	}

}
