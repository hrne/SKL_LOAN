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
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.domain.TbJcicMu01Id;
import com.st1.itx.db.repository.online.TbJcicMu01Repository;
import com.st1.itx.db.repository.day.TbJcicMu01RepositoryDay;
import com.st1.itx.db.repository.mon.TbJcicMu01RepositoryMon;
import com.st1.itx.db.repository.hist.TbJcicMu01RepositoryHist;
import com.st1.itx.db.service.TbJcicMu01Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("tbJcicMu01Service")
@Repository
public class TbJcicMu01ServiceImpl extends ASpringJpaParm implements TbJcicMu01Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TbJcicMu01Repository tbJcicMu01Repos;

	@Autowired
	private TbJcicMu01RepositoryDay tbJcicMu01ReposDay;

	@Autowired
	private TbJcicMu01RepositoryMon tbJcicMu01ReposMon;

	@Autowired
	private TbJcicMu01RepositoryHist tbJcicMu01ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(tbJcicMu01Repos);
		org.junit.Assert.assertNotNull(tbJcicMu01ReposDay);
		org.junit.Assert.assertNotNull(tbJcicMu01ReposMon);
		org.junit.Assert.assertNotNull(tbJcicMu01ReposHist);
	}

	@Override
	public TbJcicMu01 findById(TbJcicMu01Id tbJcicMu01Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + tbJcicMu01Id);
		Optional<TbJcicMu01> tbJcicMu01 = null;
		if (dbName.equals(ContentName.onDay))
			tbJcicMu01 = tbJcicMu01ReposDay.findById(tbJcicMu01Id);
		else if (dbName.equals(ContentName.onMon))
			tbJcicMu01 = tbJcicMu01ReposMon.findById(tbJcicMu01Id);
		else if (dbName.equals(ContentName.onHist))
			tbJcicMu01 = tbJcicMu01ReposHist.findById(tbJcicMu01Id);
		else
			tbJcicMu01 = tbJcicMu01Repos.findById(tbJcicMu01Id);
		TbJcicMu01 obj = tbJcicMu01.isPresent() ? tbJcicMu01.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TbJcicMu01> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TbJcicMu01> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "HeadOfficeCode", "BranchCode", "DataDate", "EmpId"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "HeadOfficeCode", "BranchCode", "DataDate", "EmpId"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = tbJcicMu01ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = tbJcicMu01ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = tbJcicMu01ReposHist.findAll(pageable);
		else
			slice = tbJcicMu01Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TbJcicMu01> empIdEq(String empId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TbJcicMu01> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("empIdEq " + dbName + " : " + "empId_0 : " + empId_0);
		if (dbName.equals(ContentName.onDay))
			slice = tbJcicMu01ReposDay.findAllByEmpIdIs(empId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = tbJcicMu01ReposMon.findAllByEmpIdIs(empId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = tbJcicMu01ReposHist.findAllByEmpIdIs(empId_0, pageable);
		else
			slice = tbJcicMu01Repos.findAllByEmpIdIs(empId_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TbJcicMu01> dataDateEq(int dataDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TbJcicMu01> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("dataDateEq " + dbName + " : " + "dataDate_0 : " + dataDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = tbJcicMu01ReposDay.findAllByDataDateIs(dataDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = tbJcicMu01ReposMon.findAllByDataDateIs(dataDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = tbJcicMu01ReposHist.findAllByDataDateIs(dataDate_0, pageable);
		else
			slice = tbJcicMu01Repos.findAllByDataDateIs(dataDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TbJcicMu01> empIdRcEq(String empId_0, int dataDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TbJcicMu01> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("empIdRcEq " + dbName + " : " + "empId_0 : " + empId_0 + " dataDate_1 : " + dataDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = tbJcicMu01ReposDay.findAllByEmpIdIsAndDataDateIs(empId_0, dataDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = tbJcicMu01ReposMon.findAllByEmpIdIsAndDataDateIs(empId_0, dataDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = tbJcicMu01ReposHist.findAllByEmpIdIsAndDataDateIs(empId_0, dataDate_1, pageable);
		else
			slice = tbJcicMu01Repos.findAllByEmpIdIsAndDataDateIs(empId_0, dataDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TbJcicMu01> findByKey(String headOfficeCode_0, String branchCode_1, int dataDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TbJcicMu01> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByKey " + dbName + " : " + "headOfficeCode_0 : " + headOfficeCode_0 + " branchCode_1 : " + branchCode_1 + " dataDate_2 : " + dataDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = tbJcicMu01ReposDay.findAllByHeadOfficeCodeIsAndBranchCodeIsAndDataDateIs(headOfficeCode_0, branchCode_1, dataDate_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = tbJcicMu01ReposMon.findAllByHeadOfficeCodeIsAndBranchCodeIsAndDataDateIs(headOfficeCode_0, branchCode_1, dataDate_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = tbJcicMu01ReposHist.findAllByHeadOfficeCodeIsAndBranchCodeIsAndDataDateIs(headOfficeCode_0, branchCode_1, dataDate_2, pageable);
		else
			slice = tbJcicMu01Repos.findAllByHeadOfficeCodeIsAndBranchCodeIsAndDataDateIs(headOfficeCode_0, branchCode_1, dataDate_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TbJcicMu01 holdById(TbJcicMu01Id tbJcicMu01Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + tbJcicMu01Id);
		Optional<TbJcicMu01> tbJcicMu01 = null;
		if (dbName.equals(ContentName.onDay))
			tbJcicMu01 = tbJcicMu01ReposDay.findByTbJcicMu01Id(tbJcicMu01Id);
		else if (dbName.equals(ContentName.onMon))
			tbJcicMu01 = tbJcicMu01ReposMon.findByTbJcicMu01Id(tbJcicMu01Id);
		else if (dbName.equals(ContentName.onHist))
			tbJcicMu01 = tbJcicMu01ReposHist.findByTbJcicMu01Id(tbJcicMu01Id);
		else
			tbJcicMu01 = tbJcicMu01Repos.findByTbJcicMu01Id(tbJcicMu01Id);
		return tbJcicMu01.isPresent() ? tbJcicMu01.get() : null;
	}

	@Override
	public TbJcicMu01 holdById(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + tbJcicMu01.getTbJcicMu01Id());
		Optional<TbJcicMu01> tbJcicMu01T = null;
		if (dbName.equals(ContentName.onDay))
			tbJcicMu01T = tbJcicMu01ReposDay.findByTbJcicMu01Id(tbJcicMu01.getTbJcicMu01Id());
		else if (dbName.equals(ContentName.onMon))
			tbJcicMu01T = tbJcicMu01ReposMon.findByTbJcicMu01Id(tbJcicMu01.getTbJcicMu01Id());
		else if (dbName.equals(ContentName.onHist))
			tbJcicMu01T = tbJcicMu01ReposHist.findByTbJcicMu01Id(tbJcicMu01.getTbJcicMu01Id());
		else
			tbJcicMu01T = tbJcicMu01Repos.findByTbJcicMu01Id(tbJcicMu01.getTbJcicMu01Id());
		return tbJcicMu01T.isPresent() ? tbJcicMu01T.get() : null;
	}

	@Override
	public TbJcicMu01 insert(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + tbJcicMu01.getTbJcicMu01Id());
		if (this.findById(tbJcicMu01.getTbJcicMu01Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			tbJcicMu01.setCreateEmpNo(empNot);

		if (tbJcicMu01.getLastUpdateEmpNo() == null || tbJcicMu01.getLastUpdateEmpNo().isEmpty())
			tbJcicMu01.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return tbJcicMu01ReposDay.saveAndFlush(tbJcicMu01);
		else if (dbName.equals(ContentName.onMon))
			return tbJcicMu01ReposMon.saveAndFlush(tbJcicMu01);
		else if (dbName.equals(ContentName.onHist))
			return tbJcicMu01ReposHist.saveAndFlush(tbJcicMu01);
		else
			return tbJcicMu01Repos.saveAndFlush(tbJcicMu01);
	}

	@Override
	public TbJcicMu01 update(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + tbJcicMu01.getTbJcicMu01Id());
		if (!empNot.isEmpty())
			tbJcicMu01.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return tbJcicMu01ReposDay.saveAndFlush(tbJcicMu01);
		else if (dbName.equals(ContentName.onMon))
			return tbJcicMu01ReposMon.saveAndFlush(tbJcicMu01);
		else if (dbName.equals(ContentName.onHist))
			return tbJcicMu01ReposHist.saveAndFlush(tbJcicMu01);
		else
			return tbJcicMu01Repos.saveAndFlush(tbJcicMu01);
	}

	@Override
	public TbJcicMu01 update2(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + tbJcicMu01.getTbJcicMu01Id());
		if (!empNot.isEmpty())
			tbJcicMu01.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			tbJcicMu01ReposDay.saveAndFlush(tbJcicMu01);
		else if (dbName.equals(ContentName.onMon))
			tbJcicMu01ReposMon.saveAndFlush(tbJcicMu01);
		else if (dbName.equals(ContentName.onHist))
			tbJcicMu01ReposHist.saveAndFlush(tbJcicMu01);
		else
			tbJcicMu01Repos.saveAndFlush(tbJcicMu01);
		return this.findById(tbJcicMu01.getTbJcicMu01Id());
	}

	@Override
	public void delete(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + tbJcicMu01.getTbJcicMu01Id());
		if (dbName.equals(ContentName.onDay)) {
			tbJcicMu01ReposDay.delete(tbJcicMu01);
			tbJcicMu01ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			tbJcicMu01ReposMon.delete(tbJcicMu01);
			tbJcicMu01ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			tbJcicMu01ReposHist.delete(tbJcicMu01);
			tbJcicMu01ReposHist.flush();
		} else {
			tbJcicMu01Repos.delete(tbJcicMu01);
			tbJcicMu01Repos.flush();
		}
	}

	@Override
	public void insertAll(List<TbJcicMu01> tbJcicMu01, TitaVo... titaVo) throws DBException {
		if (tbJcicMu01 == null || tbJcicMu01.size() == 0)
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
		for (TbJcicMu01 t : tbJcicMu01) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			tbJcicMu01 = tbJcicMu01ReposDay.saveAll(tbJcicMu01);
			tbJcicMu01ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			tbJcicMu01 = tbJcicMu01ReposMon.saveAll(tbJcicMu01);
			tbJcicMu01ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			tbJcicMu01 = tbJcicMu01ReposHist.saveAll(tbJcicMu01);
			tbJcicMu01ReposHist.flush();
		} else {
			tbJcicMu01 = tbJcicMu01Repos.saveAll(tbJcicMu01);
			tbJcicMu01Repos.flush();
		}
	}

	@Override
	public void updateAll(List<TbJcicMu01> tbJcicMu01, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (tbJcicMu01 == null || tbJcicMu01.size() == 0)
			throw new DBException(6);

		for (TbJcicMu01 t : tbJcicMu01)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			tbJcicMu01 = tbJcicMu01ReposDay.saveAll(tbJcicMu01);
			tbJcicMu01ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			tbJcicMu01 = tbJcicMu01ReposMon.saveAll(tbJcicMu01);
			tbJcicMu01ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			tbJcicMu01 = tbJcicMu01ReposHist.saveAll(tbJcicMu01);
			tbJcicMu01ReposHist.flush();
		} else {
			tbJcicMu01 = tbJcicMu01Repos.saveAll(tbJcicMu01);
			tbJcicMu01Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<TbJcicMu01> tbJcicMu01, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (tbJcicMu01 == null || tbJcicMu01.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			tbJcicMu01ReposDay.deleteAll(tbJcicMu01);
			tbJcicMu01ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			tbJcicMu01ReposMon.deleteAll(tbJcicMu01);
			tbJcicMu01ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			tbJcicMu01ReposHist.deleteAll(tbJcicMu01);
			tbJcicMu01ReposHist.flush();
		} else {
			tbJcicMu01Repos.deleteAll(tbJcicMu01);
			tbJcicMu01Repos.flush();
		}
	}

}
