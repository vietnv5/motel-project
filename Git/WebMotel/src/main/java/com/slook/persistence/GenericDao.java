/*
 * Created on Aug 15, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.persistence;

import com.slook.exception.AppException;
import com.slook.exception.SysException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ??nh ngh?a c�c h�m chung thao t�c v?i database.
 *
 * @param <T>
 *            Model class
 * @param <PK>
 *            Ki?u d? li?u c?a kh�a ch�nh
 * @author Nguy?n H?i H� (hanh45@viettel.com.vn)
 * @since Aug 15, 2013
 * @version 1.0.0
 */
public interface GenericDao<T, PK extends Serializable> {

	/**
	 * L?y th�ng tin ??i t??ng theo kh�a ch�nh.
	 * <p>
	 * C� 3 ??nh d?ng PK ?� ???c h? tr? nh? sau:
	 * <ul>
	 * <li>Integer</li>
	 * <li>String</li>
	 * <li>Composite</li>
	 * </ul>
	 *
	 * @param id
	 *            (PK) kh�a ch�nh c?a b?ng.
	 * @return
	 * <li>??i t??ng l?y ???c theo id truy?n v�o.</li>
	 * <li>Gi� tr? <b>null</b> n?u kh�ng t?n t?i.</li>
	 * @throws AppException
	 * @throws SysException
	 */
	public T findById(PK id) throws AppException, SysException;

	/**
	 * X�a th�ng tin m?t ??i t??ng.
	 *
	 * @param object
	 *            ??i t??ng mu?n x�a.
	 * @throws AppException
	 * @throws SysException
	 */
	public void delete(T object) throws AppException, SysException;

	/**
	 * X�a th�ng tin m?t danh s�ch c�c ??i t??ng.
	 *
	 * @param objects
	 *            danh s�ch c�c ??i t??ng mu?n x�a.
	 * @throws AppException
	 * @throws SysException
	 */
	public void delete(List<T> objects) throws AppException, SysException;

	/**
	 * Th�m m?i m?t ??i t??ng.
	 *
	 * @param object
	 *            ??i t??ng c?n th�m m?i.
	 *
	 * @return kh�a ch�nh.
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public PK save(T object) throws AppException, SysException;

	/**
	 * Th�m m?i ho?c c?p nh?t th�ng tin m?t ??i t??ng.
	 *
	 * @param object
	 *            ??i t??ng c?n th�m m?i ho?c c?p nh?t th�ng tin.
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public void saveOrUpdate(T object) throws AppException, SysException;

	/**
	 * Th�m m?i ho?c c?p nh?t th�ng tin m?t danh s�ch c�c ??i t??ng.
	 *
	 * @param objects
	 *            danh s�ch c�c ??i t??ng.
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public void saveOrUpdate(List<T> objects) throws AppException, SysException;

	/**
	 * L?y danh s�ch t?t c? c�c ??i t??ng.
	 *
	 * @return danh s�ch t?t c? c�c ??i t??ng.
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public List<T> findList() throws AppException, SysException;

	/**
	 * <p>
	 * L?y th�ng tin t?t c? c�c ??i t??ng.
	 * <p>
	 * H? tr? <b>Filter</b> theo tr??ng mong mu?n.
	 * <p>
	 * H? tr? <b>Order</b> theo tr??ng mong mu?n.
	 *
	 * @param filters
	 *            the filters
	 * @param orders
	 *            the orders
	 *
	 * @return Danh s�ch c�c ??i t??ng.
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public List<T> findList(Map<String, Object> filters,
			Map<String, String> orders) throws AppException, SysException;

	/**
	 * L?y danh s�ch c�c ??i t??ng c� ph�n trang.
	 *
	 * @param first
	 *            S? th? t? c?a ph?n t? ??u ti�n c?n l?y.
	 * @param pageSize
	 *            S? ph?n t? mu?n l?y.
	 * @param filters
	 *            Danh s�ch c�c tr??ng c?n filter.
	 * @return Danh s�ch t?t c? c�c ??i t??ng trong database theo trang
	 *         (paging).
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public List<T> findList(int first, int pageSize, Map<String, Object> filters)
			throws AppException, SysException;

	/**
	 * <p>
	 * L?y danh s�ch c�c ??i t??ng c� ph�n trang.
	 * <p>
	 * H? tr? filter, order.
	 *
	 * @param first
	 *            S? th? t? c?a ph?n t? ??u ti�n c?n l?y.
	 * @param pageSize
	 *            S? ph?n t? mu?n l?y.
	 * @param filters
	 *            Danh s�ch c�c tr??ng c?n filter.
	 * @param orders
	 *            Danh s�ch c�c tr??ng c?n order.
	 * @return Danh s�ch t?t c? c�c ??i t??ng trong database theo trang
	 *         (paging).
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public List<T> findList(int first, int pageSize,
			Map<String, Object> filters, Map<String, String> orders)
			throws AppException, SysException;

	/**
	 * L?y t?ng s? c�c ph?n t? theo filter.
	 *
	 * @param filters
	 *            Danh s�ch c�c tr??ng c?n filter.
	 *
	 * @return T?ng s? ph�n t?.
	 *
	 * @throws AppException
	 * @throws SysException
	 */
	public int count(Map<String, Object> filters) throws AppException,
			SysException;

	/**
	 * L?y t?ng s? ph?n t?.
	 *
	 * @return T?ng s? ph?n t?
	 * 
	 * @throws AppException
	 * @throws SysException
	 */
	public int count() throws AppException, SysException;
	
	/**
	 * Persist object
	 * 
	 * @param object
	 * @throws AppException
	 * @throws SysException
	 */
	public void persist(T object) throws AppException, SysException;
	
	/**
	 * Merge object
	 * 
	 * @param object
	 * @throws AppException
	 * @throws SysException
	 */
	public void merge(T object) throws AppException, SysException;
	
	/**
	 * Get object by id
	 * 
	 * @param id
	 * @throws AppException
	 * @throws SysException
	 */
	public T get(PK id) throws AppException, SysException;
}
