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
import com.st1.itx.db.domain.FinReportCashFlow;
import com.st1.itx.db.domain.FinReportCashFlowId;
import com.st1.itx.db.repository.online.FinReportCashFlowRepository;
import com.st1.itx.db.repository.day.FinReportCashFlowRepositoryDay;
import com.st1.itx.db.repository.mon.FinReportCashFlowRepositoryMon;
import com.st1.itx.db.repository.hist.FinReportCashFlowRepositoryHist;
import com.st1.itx.db.service.FinReportCashFlowService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finReportCashFlowService")
@Repository
public class FinReportCashFlowServiceImpl extends ASpringJpaParm implements FinReportCashFlowService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FinReportCashFlowRepository finReportCashFlowRepos;

	@Autowired
	private FinReportCashFlowRepositoryDay finReportCashFlowReposDay;

	@Autowired
	private FinReportCashFlowRepositoryMon finReportCashFlowReposMon;

	@Autowired
	private FinReportCashFlowRepositoryHist finReportCashFlowReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(finReportCashFlowRepos);
		org.junit.Assert.assertNotNull(finReportCashFlowReposDay);
		org.junit.Assert.assertNotNull(finReportCashFlowReposMon);
		org.junit.Assert.assertNotNull(finReportCashFlowReposHist);
	}

	@Override
	public FinReportCashFlow findById(FinReportCashFlowId finReportCashFlowId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + finReportCashFlowId);
		Optional<FinReportCashFlow> finReportCashFlow = null;
		if (dbName.equals(ContentName.onDay))
			finReportCashFlow = finReportCashFlowReposDay.findById(finReportCashFlowId);
		else if (dbName.equals(ContentName.onMon))
			finReportCashFlow = finReportCashFlowReposMon.findById(finReportCashFlowId);
		else if (dbName.equals(ContentName.onHist))
			finReportCashFlow = finReportCashFlowReposHist.findById(finReportCashFlowId);
		else
			finReportCashFlow = finReportCashFlowRepos.findById(finReportCashFlowId);
		FinReportCashFlow obj = finReportCashFlow.isPresent() ? finReportCashFlow.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FinReportCashFlow> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FinReportCashFlow> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = finReportCashFlowReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = finReportCashFlowReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = finReportCashFlowReposHist.findAll(pageable);
		else
			slice = finReportCashFlowRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FinReportCashFlow holdById(FinReportCashFlowId finReportCashFlowId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + finReportCashFlowId);
		Optional<FinReportCashFlow> finReportCashFlow = null;
		if (dbName.equals(ContentName.onDay))
			finReportCashFlow = finReportCashFlowReposDay.findByFinReportCashFlowId(finReportCashFlowId);
		else if (dbName.equals(ContentName.onMon))
			finReportCashFlow = finReportCashFlowReposMon.findByFinReportCashFlowId(finReportCashFlowId);
		else if (dbName.equals(ContentName.onHist))
			finReportCashFlow = finReportCashFlowReposHist.findByFinReportCashFlowId(finReportCashFlowId);
		else
			finReportCashFlow = finReportCashFlowRepos.findByFinReportCashFlowId(finReportCashFlowId);
		return finReportCashFlow.isPresent() ? finReportCashFlow.get() : null;
	}

	@Override
	public FinReportCashFlow holdById(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + finReportCashFlow.getFinReportCashFlowId());
		Optional<FinReportCashFlow> finReportCashFlowT = null;
		if (dbName.equals(ContentName.onDay))
			finReportCashFlowT = finReportCashFlowReposDay.findByFinReportCashFlowId(finReportCashFlow.getFinReportCashFlowId());
		else if (dbName.equals(ContentName.onMon))
			finReportCashFlowT = finReportCashFlowReposMon.findByFinReportCashFlowId(finReportCashFlow.getFinReportCashFlowId());
		else if (dbName.equals(ContentName.onHist))
			finReportCashFlowT = finReportCashFlowReposHist.findByFinReportCashFlowId(finReportCashFlow.getFinReportCashFlowId());
		else
			finReportCashFlowT = finReportCashFlowRepos.findByFinReportCashFlowId(finReportCashFlow.getFinReportCashFlowId());
		return finReportCashFlowT.isPresent() ? finReportCashFlowT.get() : null;
	}

	@Override
	public FinReportCashFlow insert(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + finReportCashFlow.getFinReportCashFlowId());
		if (this.findById(finReportCashFlow.getFinReportCashFlowId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			finReportCashFlow.setCreateEmpNo(empNot);

		if (finReportCashFlow.getLastUpdateEmpNo() == null || finReportCashFlow.getLastUpdateEmpNo().isEmpty())
			finReportCashFlow.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return finReportCashFlowReposDay.saveAndFlush(finReportCashFlow);
		else if (dbName.equals(ContentName.onMon))
			return finReportCashFlowReposMon.saveAndFlush(finReportCashFlow);
		else if (dbName.equals(ContentName.onHist))
			return finReportCashFlowReposHist.saveAndFlush(finReportCashFlow);
		else
			return finReportCashFlowRepos.saveAndFlush(finReportCashFlow);
	}

	@Override
	public FinReportCashFlow update(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + finReportCashFlow.getFinReportCashFlowId());
		if (!empNot.isEmpty())
			finReportCashFlow.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return finReportCashFlowReposDay.saveAndFlush(finReportCashFlow);
		else if (dbName.equals(ContentName.onMon))
			return finReportCashFlowReposMon.saveAndFlush(finReportCashFlow);
		else if (dbName.equals(ContentName.onHist))
			return finReportCashFlowReposHist.saveAndFlush(finReportCashFlow);
		else
			return finReportCashFlowRepos.saveAndFlush(finReportCashFlow);
	}

	@Override
	public FinReportCashFlow update2(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + finReportCashFlow.getFinReportCashFlowId());
		if (!empNot.isEmpty())
			finReportCashFlow.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			finReportCashFlowReposDay.saveAndFlush(finReportCashFlow);
		else if (dbName.equals(ContentName.onMon))
			finReportCashFlowReposMon.saveAndFlush(finReportCashFlow);
		else if (dbName.equals(ContentName.onHist))
			finReportCashFlowReposHist.saveAndFlush(finReportCashFlow);
		else
			finReportCashFlowRepos.saveAndFlush(finReportCashFlow);
		return this.findById(finReportCashFlow.getFinReportCashFlowId());
	}

	@Override
	public void delete(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + finReportCashFlow.getFinReportCashFlowId());
		if (dbName.equals(ContentName.onDay)) {
			finReportCashFlowReposDay.delete(finReportCashFlow);
			finReportCashFlowReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			finReportCashFlowReposMon.delete(finReportCashFlow);
			finReportCashFlowReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			finReportCashFlowReposHist.delete(finReportCashFlow);
			finReportCashFlowReposHist.flush();
		} else {
			finReportCashFlowRepos.delete(finReportCashFlow);
			finReportCashFlowRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FinReportCashFlow> finReportCashFlow, TitaVo... titaVo) throws DBException {
		if (finReportCashFlow == null || finReportCashFlow.size() == 0)
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
		for (FinReportCashFlow t : finReportCashFlow) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			finReportCashFlow = finReportCashFlowReposDay.saveAll(finReportCashFlow);
			finReportCashFlowReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			finReportCashFlow = finReportCashFlowReposMon.saveAll(finReportCashFlow);
			finReportCashFlowReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			finReportCashFlow = finReportCashFlowReposHist.saveAll(finReportCashFlow);
			finReportCashFlowReposHist.flush();
		} else {
			finReportCashFlow = finReportCashFlowRepos.saveAll(finReportCashFlow);
			finReportCashFlowRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FinReportCashFlow> finReportCashFlow, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (finReportCashFlow == null || finReportCashFlow.size() == 0)
			throw new DBException(6);

		for (FinReportCashFlow t : finReportCashFlow)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			finReportCashFlow = finReportCashFlowReposDay.saveAll(finReportCashFlow);
			finReportCashFlowReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			finReportCashFlow = finReportCashFlowReposMon.saveAll(finReportCashFlow);
			finReportCashFlowReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			finReportCashFlow = finReportCashFlowReposHist.saveAll(finReportCashFlow);
			finReportCashFlowReposHist.flush();
		} else {
			finReportCashFlow = finReportCashFlowRepos.saveAll(finReportCashFlow);
			finReportCashFlowRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FinReportCashFlow> finReportCashFlow, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (finReportCashFlow == null || finReportCashFlow.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			finReportCashFlowReposDay.deleteAll(finReportCashFlow);
			finReportCashFlowReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			finReportCashFlowReposMon.deleteAll(finReportCashFlow);
			finReportCashFlowReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			finReportCashFlowReposHist.deleteAll(finReportCashFlow);
			finReportCashFlowReposHist.flush();
		} else {
			finReportCashFlowRepos.deleteAll(finReportCashFlow);
			finReportCashFlowRepos.flush();
		}
	}

}
