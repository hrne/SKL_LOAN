package com.st1.itx.db.repository.online;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxFlowRepository extends JpaRepository<TxFlow, TxFlowId> {

  // Entdy = ,AND FlowBrNo =
  public Slice<TxFlow> findAllByEntdyIsAndFlowBrNoIsOrderByFlowNoAsc(int entdy_0, String flowBrNo_1, Pageable pageable);

  // Entdy = ,AND FlowBrNo = ,AND FlowMode = ,AND TranNo % ,AND FlowGroupNo ^i
  public Slice<TxFlow> findAllByEntdyIsAndFlowBrNoIsAndFlowModeIsAndTranNoLikeAndFlowGroupNoInOrderByFlowNoAsc(int entdy_0, String flowBrNo_1, int flowMode_2, String tranNo_3, List<String> flowGroupNo_4, Pageable pageable);

  // Entdy = ,AND SecNo =
  public Slice<TxFlow> findAllByEntdyIsAndSecNoIs(int entdy_0, String secNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxFlow> findByTxFlowId(TxFlowId txFlowId);

}

