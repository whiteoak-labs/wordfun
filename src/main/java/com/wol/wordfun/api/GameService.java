package com.wol.wordfun.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wol.wordfun.GameManager;
import com.wol.wordfun.model.Move;
import com.wol.wordfun.model.Player;

@RestController
public class GameService
{
    private static Logger logger = LoggerFactory.getLogger(GameService.class);
    
    @Autowired
    private GameManager gameManager;

    @GetMapping(path = "/game/list", produces = { MediaType.APPLICATION_JSON })
    public List<com.wol.wordfun.model.Game> list(HttpServletRequest request, HttpServletResponse response)
    {
        logger.info("Received list games request from {}", request.getRemoteAddr());
        List<com.wol.wordfun.model.Game> games = gameManager.getGames();
        
        response.setStatus(HttpServletResponse.SC_OK);
        
        if(null != games && false == games.isEmpty())
        {
            response.setContentType(MediaType.APPLICATION_JSON);
            return games;
        }
        
        logger.info("No games currently available");
        return null;
    }

    @DeleteMapping(path = "/game/delete/{id}", produces = { MediaType.APPLICATION_JSON })
    public void delete(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response)
    {
        logger.info("Received delete game '{}' request from {}", id, request.getRemoteAddr());
        com.wol.wordfun.model.Game game = gameManager.findById(id);
        
        if(null != game)
        {
            gameManager.removeGame(game);
            response.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully deleted game '{}'", id);
        }
        else
        {
            logger.info("Unable to find game '{}'", id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping(path = "/game/{id}", produces = { MediaType.APPLICATION_JSON })
    public com.wol.wordfun.model.Game find(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response)
    {
        logger.info("Received get game '{}' request from {}", id,  request.getRemoteAddr());
        com.wol.wordfun.model.Game game = gameManager.findById(id);
        
        if(null != game)
        {
            logger.info("Successfully retrieved game '{}'", id);
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setContentType(MediaType.APPLICATION_JSON);
            return game;
        }
        else
        {
            logger.info("Unable to find game '{}'", id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @PutMapping(path = "/game", consumes = { MediaType.APPLICATION_JSON })
    public com.wol.wordfun.model.Game move(@RequestBody Move move, HttpServletRequest request,
            HttpServletResponse response)
    {
        logger.info("Received update game '{}' request from '{}'", move.getGameId(), request.getRemoteAddr());
        com.wol.wordfun.model.Game game = gameManager.playerMove(move);        
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        return game;
    }

    @PostMapping(path = "/game", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
    public com.wol.wordfun.model.Game create(@RequestBody Player[] players, HttpServletRequest request,
            HttpServletResponse response)
    {
        logger.info("Received create game request for players '{}' from '{}'", players, request.getRemoteAddr());        
        return gameManager.createGame(players);
    }
    
}
