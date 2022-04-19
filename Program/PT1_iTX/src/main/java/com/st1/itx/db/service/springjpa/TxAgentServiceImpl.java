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
import com.st1.itx.db.domain.TxAgent;
import com.st1.itx.db.domain.TxAgentId;
import com.st1.itx.db.repository.online.TxAgentRepository;
import com.st1.itx.db.repository.day.TxAgentRepositoryDay;
import com.st1.itx.db.repository.mon.TxAgentRepositoryMon;
import com.st1.itx.db.repository.hist.TxAgentRepositoryHist;
import com.st1.itx.db.service.TxAgentService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAgentService")
@Repository
public class TxAgentServiceImpl extends ASpringJpaParm implements TxAgentService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxAgentRepository txAgentRepos;

	@Autowired
	private TxAgentRepositoryDay txAgentReposDay;

	@Autowired
	private TxAgentRepositoryMon txAgentReposMon;

	@Autowired
	private TxAgentRepositoryHist txAgentReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txAgentRepos);
		org.junit.Assert.assertNotNull(txAgentReposDay);
		org.junit.Assert.assertNotNull(txAgentReposMon);
		org.junit.Assert.assertNotNull(txAgentReposHist);
	}

	@Override
	public TxAgent findById(TxAgentId txAgentId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + txAgentId);
		Optional<TxAgent> txAgent = null;
		if (dbName.equals(ContentName.onDay))
			txAgent = txAgentReposDay.findById(txAgentId);
		else if (dbName.equals(ContentName.onMon))
			txAgent = txAgentReposMon.findById(txAgentId);
		else if (dbName.equals(ContentName.onHist))
			txAgent = txAgentReposHist.findById(txAgentId);
		else
			txAgent = txAgentRepos.findById(txAgentId);
		TxAgent obj = txAgent.isPresent() ? txAgent.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxAgent> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAgent> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TlrNo", "AgentTlrNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txAgentReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAgentReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAgentReposHist.findAll(pageable);
		else
			slice = txAgentRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAgent> findByAgentTlrNo(String agentTlrNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAgent> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByAgentTlrNo " + dbName + " : " + "agentTlrNo_0 : " + agentTlrNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txAgentReposDay.findAllByAgentTlrNoIsOrderByTlrNoAsc(agentTlrNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAgentReposMon.findAllByAgentTlrNoIsOrderByTlrNoAsc(agentTlrNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAgentReposHist.findAllByAgentTlrNoIsOrderByTlrNoAsc(agentTlrNo_0, pageable);
		else
			slice = txAgentRepos.findAllByAgentTlrNoIsOrderByTlrNoAsc(agentTlrNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAgent> findByTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAgent> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByTlrNo " + dbName + " : " + "tlrNo_0 : " + tlrNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txAgentReposDay.findAllByTlrNoIsOrderByAgentTlrNoAsc(tlrNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAgentReposMon.findAllByTlrNoIsOrderByAgentTlrNoAsc(tlrNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAgentReposHist.findAllByTlrNoIsOrderByAgentTlrNoAsc(tlrNo_0, pageable);
		else
			slice = txAgentRepos.findAllByTlrNoIsOrderByAgentTlrNoAsc(tlrNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxAgent holdById(TxAgentId txAgentId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txAgentId);
		Optional<TxAgent> txAgent = null;
		if (dbName.equals(ContentName.onDay))
			txAgent = txAgentReposDay.findByTxAgentId(txAgentId);
		else if (dbName.equals(ContentName.onMon))
			txAgent = txAgentReposMon.findByTxAgentId(txAgentId);
		else if (dbName.equals(ContentName.onHist))
			txAgent = txAgentReposHist.findByTxAgentId(txAgentId);
		else
			txAgent = txAgentRepos.findByTxAgentId(txAgentId);
		return txAgent.isPresent() ? txAgent.get() : null;
	}

	@Override
	public TxAgent holdById(TxAgent txAgent, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txAgent.getTxAgentId());
		Optional<TxAgent> txAgentT = null;
		if (dbName.equals(ContentName.onDay))
			txAgentT = txAgentReposDay.findByTxAgentId(txAgent.getTxAgentId());
		else if (dbName.equals(ContentName.onMon))
			txAgentT = txAgentReposMon.findByTxAgentId(txAgent.getTxAgentId());
		else if (dbName.equals(ContentName.onHist))
			txAgentT = txAgentReposHist.findByTxAgentId(txAgent.getTxAgentId());
		else
			txAgentT = txAgentRepos.findByTxAgentId(txAgent.getTxAgentId());
		return txAgentT.isPresent() ? txAgentT.get() : null;
	}

	@Override
	public TxAgent insert(TxAgent txAgent, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + txAgent.getTxAgentId());
		if (this.findById(txAgent.getTxAgentId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txAgent.setCreateEmpNo(empNot);

		if (txAgent.getLastUpdateEmpNo() == null || txAgent.getLastUpdateEmpNo().isEmpty())
			txAgent.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAgentReposDay.saveAndFlush(txAgent);
		else if (dbName.equals(ContentName.onMon))
			return txAgentReposMon.saveAndFlush(txAgent);
		else if (dbName.equals(ContentName.onHist))
			return txAgentReposHist.saveAndFlush(txAgent);
		else
			return txAgentRepos.saveAndFlush(txAgent);
	}

	@Override
	public TxAgent update(TxAgent txAgent, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txAgent.getTxAgentId());
		if (!empNot.isEmpty())
			txAgent.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAgentReposDay.saveAndFlush(txAgent);
		else if (dbName.equals(ContentName.onMon))
			return txAgentReposMon.saveAndFlush(txAgent);
		else if (dbName.equals(ContentName.onHist))
			return txAgentReposHist.saveAndFlush(txAgent);
		else
			return txAgentRepos.saveAndFlush(txAgent);
	}

	@Override
	public TxAgent update2(TxAgent txAgent, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txAgent.getTxAgentId());
		if (!empNot.isEmpty())
			txAgent.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txAgentReposDay.saveAndFlush(txAgent);
		else if (dbName.equals(ContentName.onMon))
			txAgentReposMon.saveAndFlush(txAgent);
		else if (dbName.equals(ContentName.onHist))
			txAgentReposHist.saveAndFlush(txAgent);
		else
			txAgentRepos.saveAndFlush(txAgent);
		return this.findById(txAgent.getTxAgentId());
	}

	@Override
	public void delete(TxAgent txAgent, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txAgent.getTxAgentId());
		if (dbName.equals(ContentName.onDay)) {
			txAgentReposDay.delete(txAgent);
			txAgentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAgentReposMon.delete(txAgent);
			txAgentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAgentReposHist.delete(txAgent);
			txAgentReposHist.flush();
		} else {
			txAgentRepos.delete(txAgent);
			txAgentRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxAgent> txAgent, TitaVo... titaVo) throws DBException {
		if (txAgent == null || txAgent.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (TxAgent t : txAgent) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txAgent = txAgentReposDay.saveAll(txAgent);
			txAgentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAgent = txAgentReposMon.saveAll(txAgent);
			txAgentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAgent = txAgentReposHist.saveAll(txAgent);
			txAgentReposHist.flush();
		} else {
			txAgent = txAgentRepos.saveAll(txAgent);
			txAgentRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxAgent> txAgent, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txAgent == null || txAgent.size() == 0)
			throw new DBException(6);

		for (TxAgent t : txAgent)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txAgent = txAgentReposDay.saveAll(txAgent);
			txAgentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAgent = txAgentReposMon.saveAll(txAgent);
			txAgentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAgent = txAgentReposHist.saveAll(txAgent);
			txAgentReposHist.flush();
		} else {
			txAgent = txAgentRepos.saveAll(txAgent);
			txAgentRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxAgent> txAgent, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txAgent == null || txAgent.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txAgentReposDay.deleteAll(txAgent);
			txAgentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAgentReposMon.deleteAll(txAgent);
			txAgentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAgentReposHist.deleteAll(txAgent);
			txAgentReposHist.flush();
		} else {
			txAgentRepos.deleteAll(txAgent);
			txAgentRepos.flush();
		}
	}

}
