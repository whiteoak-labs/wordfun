package com.wol.wordfun.dao;

import java.util.List;

/**
 * Data access object used for dealing with persistables
 * 
 * @see T
 */
public interface BasicDao<T>
{
	/**
	 * Attempts to locate an entity in persistent storage
	 * 
	 * @param id
	 * @return T with matching primary key, null if not found
	 * @see T
	 */
	public T find(String id);

	/**
	 * Retrieve all entitys in persistent storage
	 * 
	 * @return Set of entitys in persistent storage
	 * @see Set
	 * @see T
	 */
	public List<T> findAll();

	/**
	 * Upsert an entity into persistent storage.
	 * 
	 * @param entity
	 *            to persist
	 * @return T which was persisted
	 * @see T
	 */
	public T save(T entity);

	/**
	 * Delete a given entity from persistent storage.
	 * 
	 * @param entity
	 *            to delete
	 * @return T which was deleted
	 * @see T
	 */
	public T delete(T entity);

	/**
	 * Deletes all entitys from persistent storage
	 * 
	 * @return count of all entitys deleted
	 * @see T
	 */
	public int deleteAll();

}
