package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuildingParking;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClBuildingParkingId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingParkingService {

  /**
   * findByPrimaryKey
   *
   * @param clBuildingParkingId PK
   * @param titaVo Variable-Length Argument
   * @return ClBuildingParking ClBuildingParking
   */
  public ClBuildingParking findById(ClBuildingParkingId clBuildingParkingId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingParking ClBuildingParking of List
   */
  public Slice<ClBuildingParking> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingParking ClBuildingParking of List
   */
  public Slice<ClBuildingParking> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * ParkingBdNo1 = 
   *
   * @param parkingBdNo1_0 parkingBdNo1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingParking ClBuildingParking of List
   */
  public Slice<ClBuildingParking> parkingBdNo1Eq(int parkingBdNo1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ParkingBdNo1 = ,AND ParkingBdNo2 = 
   *
   * @param parkingBdNo1_0 parkingBdNo1_0
   * @param parkingBdNo2_1 parkingBdNo2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingParking ClBuildingParking of List
   */
  public Slice<ClBuildingParking> parkingBdNo2Eq(int parkingBdNo1_0, int parkingBdNo2_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClBuildingParking
   * 
   * @param clBuildingParkingId key
   * @param titaVo Variable-Length Argument
   * @return ClBuildingParking ClBuildingParking
   */
  public ClBuildingParking holdById(ClBuildingParkingId clBuildingParkingId, TitaVo... titaVo);

  /**
   * hold By ClBuildingParking
   * 
   * @param clBuildingParking key
   * @param titaVo Variable-Length Argument
   * @return ClBuildingParking ClBuildingParking
   */
  public ClBuildingParking holdById(ClBuildingParking clBuildingParking, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clBuildingParking Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingParking Entity
   * @throws DBException exception
   */
  public ClBuildingParking insert(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clBuildingParking Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingParking Entity
   * @throws DBException exception
   */
  public ClBuildingParking update(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clBuildingParking Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingParking Entity
   * @throws DBException exception
   */
  public ClBuildingParking update2(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clBuildingParking Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clBuildingParking Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClBuildingParking> clBuildingParking, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clBuildingParking Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClBuildingParking> clBuildingParking, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clBuildingParking Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClBuildingParking> clBuildingParking, TitaVo... titaVo) throws DBException;

}
