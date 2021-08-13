package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.repository.online.TxToDoMainRepository;
import com.st1.itx.db.repository.day.TxToDoMainRepositoryDay;
import com.st1.itx.db.repository.mon.TxToDoMainRepositoryMon;
import com.st1.itx.db.repository.hist.TxToDoMainRepositoryHist;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txToDoMainService")
@Repository
public class TxToDoMainServiceImpl implements TxToDoMainService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TxToDoMainServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxToDoMainRepository txToDoMainRepos;

	@Autowired
	private TxToDoMainRepositoryDay txToDoMainReposDay;

	@Autowired
	private TxToDoMainRepositoryMon txToDoMainReposMon;

	@Autowired
	private TxToDoMainRepositoryHist txToDoMainReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txToDoMainRepos);
		org.junit.Assert.assertNotNull(txToDoMainReposDay);
		org.junit.Assert.assertNotNull(txToDoMainReposMon);
		org.junit.Assert.assertNotNull(txToDoMainReposHist);
	}

	@Override
	public TxToDoMain findById(String itemCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + itemCode);
		Optional<TxToDoMain> txToDoMain = null;
		if (dbName.equals(ContentName.onDay))
			txToDoMain = txToDoMainReposDay.findById(itemCode);
		else if (dbName.equals(ContentName.onMon))
			txToDoMain = txToDoMainReposMon.findById(itemCode);
		else if (dbName.equals(ContentName.onHist))
			txToDoMain = txToDoMainReposHist.findById(itemCode);
		else
			txToDoMain = txToDoMainRepos.findById(itemCode);
		TxToDoMain obj = txToDoMain.isPresent() ? txToDoMain.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxToDoMain> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxToDoMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ItemCode"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txToDoMainReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txToDoMainReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txToDoMainReposHist.findAll(pageable);
		else
			slice = txToDoMainRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxToDoMain excuteTxcdFirst(String autoFg_0, String excuteTxcd_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("excuteTxcdFirst " + dbName + " : " + "autoFg_0 : " + autoFg_0 + " excuteTxcd_1 : " + excuteTxcd_1);
		Optional<TxToDoMain> txToDoMainT = null;
		if (dbName.equals(ContentName.onDay))
			txToDoMainT = txToDoMainReposDay.findTopByAutoFgIsAndExcuteTxcdIs(autoFg_0, excuteTxcd_1);
		else if (dbName.equals(ContentName.onMon))
			txToDoMainT = txToDoMainReposMon.findTopByAutoFgIsAndExcuteTxcdIs(autoFg_0, excuteTxcd_1);
		else if (dbName.equals(ContentName.onHist))
			txToDoMainT = txToDoMainReposHist.findTopByAutoFgIsAndExcuteTxcdIs(autoFg_0, excuteTxcd_1);
		else
			txToDoMainT = txToDoMainRepos.findTopByAutoFgIsAndExcuteTxcdIs(autoFg_0, excuteTxcd_1);
		return txToDoMainT.isPresent() ? txToDoMainT.get() : null;
	}

	@Override
	public TxToDoMain holdById(String itemCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + itemCode);
		Optional<TxToDoMain> txToDoMain = null;
		if (dbName.equals(ContentName.onDay))
			txToDoMain = txToDoMainReposDay.findByItemCode(itemCode);
		else if (dbName.equals(ContentName.onMon))
			txToDoMain = txToDoMainReposMon.findByItemCode(itemCode);
		else if (dbName.equals(ContentName.onHist))
			txToDoMain = txToDoMainReposHist.findByItemCode(itemCode);
		else
			txToDoMain = txToDoMainRepos.findByItemCode(itemCode);
		return txToDoMain.isPresent() ? txToDoMain.get() : null;
	}

	@Override
	public TxToDoMain holdById(TxToDoMain txToDoMain, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + txToDoMain.getItemCode());
		Optional<TxToDoMain> txToDoMainT = null;
		if (dbName.equals(ContentName.onDay))
			txToDoMainT = txToDoMainReposDay.findByItemCode(txToDoMain.getItemCode());
		else if (dbName.equals(ContentName.onMon))
			txToDoMainT = txToDoMainReposMon.findByItemCode(txToDoMain.getItemCode());
		else if (dbName.equals(ContentName.onHist))
			txToDoMainT = txToDoMainReposHist.findByItemCode(txToDoMain.getItemCode());
		else
			txToDoMainT = txToDoMainRepos.findByItemCode(txToDoMain.getItemCode());
		return txToDoMainT.isPresent() ? txToDoMainT.get() : null;
	}

	@Override
	public TxToDoMain insert(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + txToDoMain.getItemCode());
		if (this.findById(txToDoMain.getItemCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txToDoMain.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txToDoMainReposDay.saveAndFlush(txToDoMain);
		else if (dbName.equals(ContentName.onMon))
			return txToDoMainReposMon.saveAndFlush(txToDoMain);
		else if (dbName.equals(ContentName.onHist))
			return txToDoMainReposHist.saveAndFlush(txToDoMain);
		else
			return txToDoMainRepos.saveAndFlush(txToDoMain);
	}

	@Override
	public TxToDoMain update(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txToDoMain.getItemCode());
		if (!empNot.isEmpty())
			txToDoMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txToDoMainReposDay.saveAndFlush(txToDoMain);
		else if (dbName.equals(ContentName.onMon))
			return txToDoMainReposMon.saveAndFlush(txToDoMain);
		else if (dbName.equals(ContentName.onHist))
			return txToDoMainReposHist.saveAndFlush(txToDoMain);
		else
			return txToDoMainRepos.saveAndFlush(txToDoMain);
	}

	@Override
	public TxToDoMain update2(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txToDoMain.getItemCode());
		if (!empNot.isEmpty())
			txToDoMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txToDoMainReposDay.saveAndFlush(txToDoMain);
		else if (dbName.equals(ContentName.onMon))
			txToDoMainReposMon.saveAndFlush(txToDoMain);
		else if (dbName.equals(ContentName.onHist))
			txToDoMainReposHist.saveAndFlush(txToDoMain);
		else
			txToDoMainRepos.saveAndFlush(txToDoMain);
		return this.findById(txToDoMain.getItemCode());
	}

	@Override
	public void delete(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + txToDoMain.getItemCode());
		if (dbName.equals(ContentName.onDay)) {
			txToDoMainReposDay.delete(txToDoMain);
			txToDoMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txToDoMainReposMon.delete(txToDoMain);
			txToDoMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txToDoMainReposHist.delete(txToDoMain);
			txToDoMainReposHist.flush();
		} else {
			txToDoMainRepos.delete(txToDoMain);
			txToDoMainRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxToDoMain> txToDoMain, TitaVo... titaVo) throws DBException {
		if (txToDoMain == null || txToDoMain.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (TxToDoMain t : txToDoMain)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txToDoMain = txToDoMainReposDay.saveAll(txToDoMain);
			txToDoMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txToDoMain = txToDoMainReposMon.saveAll(txToDoMain);
			txToDoMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txToDoMain = txToDoMainReposHist.saveAll(txToDoMain);
			txToDoMainReposHist.flush();
		} else {
			txToDoMain = txToDoMainRepos.saveAll(txToDoMain);
			txToDoMainRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxToDoMain> txToDoMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (txToDoMain == null || txToDoMain.size() == 0)
			throw new DBException(6);

		for (TxToDoMain t : txToDoMain)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txToDoMain = txToDoMainReposDay.saveAll(txToDoMain);
			txToDoMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txToDoMain = txToDoMainReposMon.saveAll(txToDoMain);
			txToDoMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txToDoMain = txToDoMainReposHist.saveAll(txToDoMain);
			txToDoMainReposHist.flush();
		} else {
			txToDoMain = txToDoMainRepos.saveAll(txToDoMain);
			txToDoMainRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxToDoMain> txToDoMain, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txToDoMain == null || txToDoMain.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txToDoMainReposDay.deleteAll(txToDoMain);
			txToDoMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txToDoMainReposMon.deleteAll(txToDoMain);
			txToDoMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txToDoMainReposHist.deleteAll(txToDoMain);
			txToDoMainReposHist.flush();
		} else {
			txToDoMainRepos.deleteAll(txToDoMain);
			txToDoMainRepos.flush();
		}
	}

}
