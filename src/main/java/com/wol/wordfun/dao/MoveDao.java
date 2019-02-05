package com.wol.wordfun.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wol.wordfun.model.Move;

@Repository
public class MoveDao implements BasicDao<Move>
{
    private static Logger logger = LoggerFactory.getLogger(MoveDao.class);

    @Autowired
    private EntityManager entityManager;
    
    public Move find(String id)
    {
        Query findQuery = entityManager.createQuery("FROM Move WHERE id=:id");
        findQuery.setParameter("id", id);
        Move move = null;

        try
        {
            move = (Move) findQuery.getSingleResult();
        }
        catch (NoResultException e)
        {
        }

        return move;
    }

    @SuppressWarnings("unchecked")
    public List<Move> findAll()
    {
        Query findQuery = entityManager.createQuery("FROM Move");
        return findQuery.getResultList();
    }

    @Transactional
    public Move save(Move move)
    {
        entityManager.persist(entityManager.contains(move) ? move : entityManager.merge(move));
        entityManager.flush();
        logger.debug("Successfully saved " + move);
        return move;
    }

    @Override
    @Transactional
    public Move delete(Move move)
    {
        entityManager.remove(entityManager.contains(move) ? move : entityManager.merge(move));
        entityManager.flush();
        logger.debug("Successfully deleted " + move);
        return move;
    }

    @Override
    @Transactional
    public int deleteAll()
    {
        Query deleteQuery = entityManager.createQuery("DELETE FROM Move");
        int count =  deleteQuery.executeUpdate();
        entityManager.flush();
        logger.debug("Successfully deleted '{}' moves", count);
        return count;
    }
}
