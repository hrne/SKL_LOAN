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
import com.st1.itx.db.domain.RepayActChangeLog;
import com.st1.itx.db.repository.online.RepayActChangeLogRepository;
import com.st1.itx.db.repository.day.RepayActChangeLogRepositoryDay;
import com.st1.itx.db.repository.mon.RepayActChangeLogRepositoryMon;
import com.st1.itx.db.repository.hist.RepayActChangeLogRepositoryHist;
import com.st1.itx.db.service.RepayActChangeLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("repayActChangeLogService")
@Repository
public class RepayActChangeLogServiceImpl extends ASpringJpaParm implements RepayActChangeLogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private RepayActChangeLogRepository repayActChangeLogRepos;

	@Autowired
	private RepayActChangeLogRepositoryDay repayActChangeLogReposDay;

	@Autowired
	private RepayActChangeLogRepositoryMon repayActChangeLogReposMon;

	@Autowired
	private RepayActChangeLogRepositoryHist repayActChangeLogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(repayActChangeLogRepos);
		org.junit.Assert.assertNotNull(repayActChangeLogReposDay);
		org.junit.Assert.assertNotNull(repayActChangeLogReposMon);
		org.junit.Assert.assertNotNull(repayActChangeLogReposHist);
	}

	@Override
	public RepayActChangeLog findById(Long logNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + logNo);
		Optional<RepayActChangeLog> repayActChangeLog = null;
		if (dbName.equals(ContentName.onDay))
			repayActChangeLog = repayActChangeLogReposDay.findById(logNo);
		else if (dbName.equals(ContentName.onMon))
			repayActChangeLog = repayActChangeLogReposMon.findById(logNo);
		else if (dbName.equals(ContentName.onHist))
			repayActChangeLog = repayActChangeLogReposHist.findById(logNo);
		else
			repayActChangeLog = repayActChangeLogRepos.findById(logNo);
		RepayActChangeLog obj = repayActChangeLog.isPresent() ? repayActChangeLog.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<RepayActChangeLog> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RepayActChangeLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = repayActChangeLogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = repayActChangeLogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = repayActChangeLogReposHist.findAll(pageable);
		else
			slice = repayActChangeLogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RepayActChangeLog> findFacmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RepayActChangeLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFacmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = repayActChangeLogReposDay.findAllByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = repayActChangeLogReposMon.findAllByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = repayActChangeLogReposHist.findAllByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1, pageable);
		else
			slice = repayActChangeLogRepos.findAllByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RepayActChangeLog findRelTxseqFirst(int relDy_0, String relTxseq_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findRelTxseqFirst " + dbName + " : " + "relDy_0 : " + relDy_0 + " relTxseq_1 : " + relTxseq_1);
		Optional<RepayActChangeLog> repayActChangeLogT = null;
		if (dbName.equals(ContentName.onDay))
			repayActChangeLogT = repayActChangeLogReposDay.findTopByRelDyIsAndRelTxseqIsOrderByLogNoDesc(relDy_0, relTxseq_1);
		else if (dbName.equals(ContentName.onMon))
			repayActChangeLogT = repayActChangeLogReposMon.findTopByRelDyIsAndRelTxseqIsOrderByLogNoDesc(relDy_0, relTxseq_1);
		else if (dbName.equals(ContentName.onHist))
			repayActChangeLogT = repayActChangeLogReposHist.findTopByRelDyIsAndRelTxseqIsOrderByLogNoDesc(relDy_0, relTxseq_1);
		else
			repayActChangeLogT = repayActChangeLogRepos.findTopByRelDyIsAndRelTxseqIsOrderByLogNoDesc(relDy_0, relTxseq_1);

		return repayActChangeLogT.isPresent() ? repayActChangeLogT.get() : null;
	}

	@Override
	public RepayActChangeLog findFacmNoFirst(int custNo_0, int facmNo_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findFacmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		Optional<RepayActChangeLog> repayActChangeLogT = null;
		if (dbName.equals(ContentName.onDay))
			repayActChangeLogT = repayActChangeLogReposDay.findTopByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1);
		else if (dbName.equals(ContentName.onMon))
			repayActChangeLogT = repayActChangeLogReposMon.findTopByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1);
		else if (dbName.equals(ContentName.onHist))
			repayActChangeLogT = repayActChangeLogReposHist.findTopByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1);
		else
			repayActChangeLogT = repayActChangeLogRepos.findTopByCustNoIsAndFacmNoIsOrderByLogNoDesc(custNo_0, facmNo_1);

		return repayActChangeLogT.isPresent() ? repayActChangeLogT.get() : null;
	}

	@Override
	public RepayActChangeLog holdById(Long logNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + logNo);
		Optional<RepayActChangeLog> repayActChangeLog = null;
		if (dbName.equals(ContentName.onDay))
			repayActChangeLog = repayActChangeLogReposDay.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onMon))
			repayActChangeLog = repayActChangeLogReposMon.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onHist))
			repayActChangeLog = repayActChangeLogReposHist.findByLogNo(logNo);
		else
			repayActChangeLog = repayActChangeLogRepos.findByLogNo(logNo);
		return repayActChangeLog.isPresent() ? repayActChangeLog.get() : null;
	}

	@Override
	public RepayActChangeLog holdById(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + repayActChangeLog.getLogNo());
		Optional<RepayActChangeLog> repayActChangeLogT = null;
		if (dbName.equals(ContentName.onDay))
			repayActChangeLogT = repayActChangeLogReposDay.findByLogNo(repayActChangeLog.getLogNo());
		else if (dbName.equals(ContentName.onMon))
			repayActChangeLogT = repayActChangeLogReposMon.findByLogNo(repayActChangeLog.getLogNo());
		else if (dbName.equals(ContentName.onHist))
			repayActChangeLogT = repayActChangeLogReposHist.findByLogNo(repayActChangeLog.getLogNo());
		else
			repayActChangeLogT = repayActChangeLogRepos.findByLogNo(repayActChangeLog.getLogNo());
		return repayActChangeLogT.isPresent() ? repayActChangeLogT.get() : null;
	}

	@Override
	public RepayActChangeLog insert(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + repayActChangeLog.getLogNo());
		if (this.findById(repayActChangeLog.getLogNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			repayActChangeLog.setCreateEmpNo(empNot);

		if (repayActChangeLog.getLastUpdateEmpNo() == null || repayActChangeLog.getLastUpdateEmpNo().isEmpty())
			repayActChangeLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return repayActChangeLogReposDay.saveAndFlush(repayActChangeLog);
		else if (dbName.equals(ContentName.onMon))
			return repayActChangeLogReposMon.saveAndFlush(repayActChangeLog);
		else if (dbName.equals(ContentName.onHist))
			return repayActChangeLogReposHist.saveAndFlush(repayActChangeLog);
		else
			return repayActChangeLogRepos.saveAndFlush(repayActChangeLog);
	}

	@Override
	public RepayActChangeLog update(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + repayActChangeLog.getLogNo());
		if (!empNot.isEmpty())
			repayActChangeLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return repayActChangeLogReposDay.saveAndFlush(repayActChangeLog);
		else if (dbName.equals(ContentName.onMon))
			return repayActChangeLogReposMon.saveAndFlush(repayActChangeLog);
		else if (dbName.equals(ContentName.onHist))
			return repayActChangeLogReposHist.saveAndFlush(repayActChangeLog);
		else
			return repayActChangeLogRepos.saveAndFlush(repayActChangeLog);
	}

	@Override
	public RepayActChangeLog update2(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + repayActChangeLog.getLogNo());
		if (!empNot.isEmpty())
			repayActChangeLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			repayActChangeLogReposDay.saveAndFlush(repayActChangeLog);
		else if (dbName.equals(ContentName.onMon))
			repayActChangeLogReposMon.saveAndFlush(repayActChangeLog);
		else if (dbName.equals(ContentName.onHist))
			repayActChangeLogReposHist.saveAndFlush(repayActChangeLog);
		else
			repayActChangeLogRepos.saveAndFlush(repayActChangeLog);
		return this.findById(repayActChangeLog.getLogNo());
	}

	@Override
	public void delete(RepayActChangeLog repayActChangeLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + repayActChangeLog.getLogNo());
		if (dbName.equals(ContentName.onDay)) {
			repayActChangeLogReposDay.delete(repayActChangeLog);
			repayActChangeLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			repayActChangeLogReposMon.delete(repayActChangeLog);
			repayActChangeLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			repayActChangeLogReposHist.delete(repayActChangeLog);
			repayActChangeLogReposHist.flush();
		} else {
			repayActChangeLogRepos.delete(repayActChangeLog);
			repayActChangeLogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<RepayActChangeLog> repayActChangeLog, TitaVo... titaVo) throws DBException {
		if (repayActChangeLog == null || repayActChangeLog.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (RepayActChangeLog t : repayActChangeLog) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			repayActChangeLog = repayActChangeLogReposDay.saveAll(repayActChangeLog);
			repayActChangeLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			repayActChangeLog = repayActChangeLogReposMon.saveAll(repayActChangeLog);
			repayActChangeLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			repayActChangeLog = repayActChangeLogReposHist.saveAll(repayActChangeLog);
			repayActChangeLogReposHist.flush();
		} else {
			repayActChangeLog = repayActChangeLogRepos.saveAll(repayActChangeLog);
			repayActChangeLogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<RepayActChangeLog> repayActChangeLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (repayActChangeLog == null || repayActChangeLog.size() == 0)
			throw new DBException(6);

		for (RepayActChangeLog t : repayActChangeLog)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			repayActChangeLog = repayActChangeLogReposDay.saveAll(repayActChangeLog);
			repayActChangeLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			repayActChangeLog = repayActChangeLogReposMon.saveAll(repayActChangeLog);
			repayActChangeLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			repayActChangeLog = repayActChangeLogReposHist.saveAll(repayActChangeLog);
			repayActChangeLogReposHist.flush();
		} else {
			repayActChangeLog = repayActChangeLogRepos.saveAll(repayActChangeLog);
			repayActChangeLogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<RepayActChangeLog> repayActChangeLog, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (repayActChangeLog == null || repayActChangeLog.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			repayActChangeLogReposDay.deleteAll(repayActChangeLog);
			repayActChangeLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			repayActChangeLogReposMon.deleteAll(repayActChangeLog);
			repayActChangeLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			repayActChangeLogReposHist.deleteAll(repayActChangeLog);
			repayActChangeLogReposHist.flush();
		} else {
			repayActChangeLogRepos.deleteAll(repayActChangeLog);
			repayActChangeLogRepos.flush();
		}
	}

}
