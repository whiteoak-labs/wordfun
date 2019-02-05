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

import com.wol.wordfun.model.Player;

@Repository
public class PlayerDao implements BasicDao<Player>
{
    private static Logger logger = LoggerFactory.getLogger(PlayerDao.class);

    @Autowired
    private EntityManager entityManager;

    public Player find(String id)
    {
        Query findQuery = entityManager.createQuery("FROM Player WHERE id=:id");
        findQuery.setParameter("id", id);
        Player player = null;

        try
        {
            player = (Player) findQuery.getSingleResult();
        }
        catch (NoResultException e)
        {
        }

        return player;
    }
    
    public Player findByName(String name)
    {
        Query findQuery = entityManager.createQuery("FROM Player WHERE name=:name");
        findQuery.setParameter("name", name);
        Player player = null;

        try
        {
            player = (Player) findQuery.getSingleResult();
        }
        catch (NoResultException e)
        {
        }

        return player;
    }
    
    @SuppressWarnings("unchecked")
    public List<Player> findAll()
    {
        Query findQuery = entityManager.createQuery("FROM Player");
        return findQuery.getResultList();
    }

    @Transactional
    public Player save(Player player)
    {
        entityManager.persist(entityManager.contains(player) ? player : entityManager.merge(player));
        entityManager.flush();
        logger.debug("Successfully saved " + player);
        return player;
    }

    @Transactional
    public Player delete(Player player)
    {
        entityManager.remove(entityManager.contains(player) ? player : entityManager.merge(player));
        entityManager.flush();
        logger.debug("Successfully deleted " + player);
        return player;
    }

    @Transactional
    public int deleteAll()
    {
        Query deleteQuery = entityManager.createQuery("DELETE FROM Player");
        int count =  deleteQuery.executeUpdate();
        entityManager.flush();
        logger.debug("Successfully deleted '{}' players", count);
        return count;
    }
}
