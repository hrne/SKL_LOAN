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
import com.st1.itx.db.domain.TxApLogList;
import com.st1.itx.db.repository.online.TxApLogListRepository;
import com.st1.itx.db.repository.day.TxApLogListRepositoryDay;
import com.st1.itx.db.repository.mon.TxApLogListRepositoryMon;
import com.st1.itx.db.repository.hist.TxApLogListRepositoryHist;
import com.st1.itx.db.service.TxApLogListService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txApLogListService")
@Repository
public class TxApLogListServiceImpl extends ASpringJpaParm implements TxApLogListService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxApLogListRepository txApLogListRepos;

	@Autowired
	private TxApLogListRepositoryDay txApLogListReposDay;

	@Autowired
	private TxApLogListRepositoryMon txApLogListReposMon;

	@Autowired
	private TxApLogListRepositoryHist txApLogListReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txApLogListRepos);
		org.junit.Assert.assertNotNull(txApLogListReposDay);
		org.junit.Assert.assertNotNull(txApLogListReposMon);
		org.junit.Assert.assertNotNull(txApLogListReposHist);
	}

	@Override
	public TxApLogList findById(String txCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + txCode);
		Optional<TxApLogList> txApLogList = null;
		if (dbName.equals(ContentName.onDay))
			txApLogList = txApLogListReposDay.findById(txCode);
		else if (dbName.equals(ContentName.onMon))
			txApLogList = txApLogListReposMon.findById(txCode);
		else if (dbName.equals(ContentName.onHist))
			txApLogList = txApLogListReposHist.findById(txCode);
		else
			txApLogList = txApLogListRepos.findById(txCode);
		TxApLogList obj = txApLogList.isPresent() ? txApLogList.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxApLogList> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxApLogList> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TxCode"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txApLogListReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txApLogListReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txApLogListReposHist.findAll(pageable);
		else
			slice = txApLogListRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxApLogList holdById(String txCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txCode);
		Optional<TxApLogList> txApLogList = null;
		if (dbName.equals(ContentName.onDay))
			txApLogList = txApLogListReposDay.findByTxCode(txCode);
		else if (dbName.equals(ContentName.onMon))
			txApLogList = txApLogListReposMon.findByTxCode(txCode);
		else if (dbName.equals(ContentName.onHist))
			txApLogList = txApLogListReposHist.findByTxCode(txCode);
		else
			txApLogList = txApLogListRepos.findByTxCode(txCode);
		return txApLogList.isPresent() ? txApLogList.get() : null;
	}

	@Override
	public TxApLogList holdById(TxApLogList txApLogList, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txApLogList.getTxCode());
		Optional<TxApLogList> txApLogListT = null;
		if (dbName.equals(ContentName.onDay))
			txApLogListT = txApLogListReposDay.findByTxCode(txApLogList.getTxCode());
		else if (dbName.equals(ContentName.onMon))
			txApLogListT = txApLogListReposMon.findByTxCode(txApLogList.getTxCode());
		else if (dbName.equals(ContentName.onHist))
			txApLogListT = txApLogListReposHist.findByTxCode(txApLogList.getTxCode());
		else
			txApLogListT = txApLogListRepos.findByTxCode(txApLogList.getTxCode());
		return txApLogListT.isPresent() ? txApLogListT.get() : null;
	}

	@Override
	public TxApLogList insert(TxApLogList txApLogList, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + txApLogList.getTxCode());
		if (this.findById(txApLogList.getTxCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txApLogList.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txApLogListReposDay.saveAndFlush(txApLogList);
		else if (dbName.equals(ContentName.onMon))
			return txApLogListReposMon.saveAndFlush(txApLogList);
		else if (dbName.equals(ContentName.onHist))
			return txApLogListReposHist.saveAndFlush(txApLogList);
		else
			return txApLogListRepos.saveAndFlush(txApLogList);
	}

	@Override
	public TxApLogList update(TxApLogList txApLogList, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txApLogList.getTxCode());
		if (!empNot.isEmpty())
			txApLogList.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txApLogListReposDay.saveAndFlush(txApLogList);
		else if (dbName.equals(ContentName.onMon))
			return txApLogListReposMon.saveAndFlush(txApLogList);
		else if (dbName.equals(ContentName.onHist))
			return txApLogListReposHist.saveAndFlush(txApLogList);
		else
			return txApLogListRepos.saveAndFlush(txApLogList);
	}

	@Override
	public TxApLogList update2(TxApLogList txApLogList, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txApLogList.getTxCode());
		if (!empNot.isEmpty())
			txApLogList.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txApLogListReposDay.saveAndFlush(txApLogList);
		else if (dbName.equals(ContentName.onMon))
			txApLogListReposMon.saveAndFlush(txApLogList);
		else if (dbName.equals(ContentName.onHist))
			txApLogListReposHist.saveAndFlush(txApLogList);
		else
			txApLogListRepos.saveAndFlush(txApLogList);
		return this.findById(txApLogList.getTxCode());
	}

	@Override
	public void delete(TxApLogList txApLogList, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txApLogList.getTxCode());
		if (dbName.equals(ContentName.onDay)) {
			txApLogListReposDay.delete(txApLogList);
			txApLogListReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLogListReposMon.delete(txApLogList);
			txApLogListReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLogListReposHist.delete(txApLogList);
			txApLogListReposHist.flush();
		} else {
			txApLogListRepos.delete(txApLogList);
			txApLogListRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxApLogList> txApLogList, TitaVo... titaVo) throws DBException {
		if (txApLogList == null || txApLogList.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (TxApLogList t : txApLogList)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txApLogList = txApLogListReposDay.saveAll(txApLogList);
			txApLogListReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLogList = txApLogListReposMon.saveAll(txApLogList);
			txApLogListReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLogList = txApLogListReposHist.saveAll(txApLogList);
			txApLogListReposHist.flush();
		} else {
			txApLogList = txApLogListRepos.saveAll(txApLogList);
			txApLogListRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxApLogList> txApLogList, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txApLogList == null || txApLogList.size() == 0)
			throw new DBException(6);

		for (TxApLogList t : txApLogList)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txApLogList = txApLogListReposDay.saveAll(txApLogList);
			txApLogListReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLogList = txApLogListReposMon.saveAll(txApLogList);
			txApLogListReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLogList = txApLogListReposHist.saveAll(txApLogList);
			txApLogListReposHist.flush();
		} else {
			txApLogList = txApLogListRepos.saveAll(txApLogList);
			txApLogListRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxApLogList> txApLogList, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txApLogList == null || txApLogList.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txApLogListReposDay.deleteAll(txApLogList);
			txApLogListReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLogListReposMon.deleteAll(txApLogList);
			txApLogListReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLogListReposHist.deleteAll(txApLogList);
			txApLogListReposHist.flush();
		} else {
			txApLogListRepos.deleteAll(txApLogList);
			txApLogListRepos.flush();
		}
	}

}
