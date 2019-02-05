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

import com.wol.wordfun.model.Game;

@Repository
public class GameDao implements BasicDao<Game>
{
    private static Logger logger = LoggerFactory.getLogger(GameDao.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public Game find(String id)
    {
        Query findQuery = entityManager.createQuery("FROM Game WHERE id=:id");
        findQuery.setParameter("id", id);
        Game game = null;

        try
        {
            game = (Game) findQuery.getSingleResult();
        }
        catch (NoResultException e)
        {}

        return game;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Game> findAll()
    {
        Query findQuery = entityManager.createQuery("FROM Game");
        return findQuery.getResultList();
    }

    @Transactional
    public Game save(Game game)
    {
        entityManager.persist(entityManager.contains(game) ? game : entityManager.merge(game));
        entityManager.flush();
        logger.debug("Successfully saved " + game);
        return game;
    }

    @Transactional
    public Game delete(Game game)
    {
        entityManager.remove(entityManager.contains(game) ? game : entityManager.merge(game));
        entityManager.flush();
        logger.debug("Successfully deleted " + game);
        return game;
    }

    @Transactional
    public int deleteAll()
    {
        Query deleteQuery = entityManager.createQuery("DELETE FROM Game");
        int count =  deleteQuery.executeUpdate();
        entityManager.flush();
        logger.debug("Successfully deleted '{}' games", count);
        return count;
    }
}
