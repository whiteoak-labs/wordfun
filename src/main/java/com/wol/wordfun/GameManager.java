package com.wol.wordfun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wol.wordfun.dao.GameDao;
import com.wol.wordfun.dao.PlayerDao;
import com.wol.wordfun.model.Game;
import com.wol.wordfun.model.Letter;
import com.wol.wordfun.model.Move;
import com.wol.wordfun.model.Player;

@Component
public class GameManager
{
    private static Logger logger = LoggerFactory.getLogger(GameManager.class);

    @Autowired
    private GameDao gameDao;
    
    @Autowired
    private PlayerDao playerDao;
    
    public GameManager()
    {}

    public List<Game> getGames()
    {
        return gameDao.findAll();
    }

    public void removeGame(Game game)
    {
        gameDao.delete(game);
    }

    public Game findById(String id)
    {
        return gameDao.find(id);
    }

    public void updateGame(Game game)
    {
        gameDao.save(game);
    }

    public Game createGame(Player[] players)
    {
        Game game = new Game();
        game.setId(UUID.randomUUID().toString().replaceAll("-", ""));

        for (Player player : players)
        {
            List<Letter> letters = new ArrayList<Letter>();
            for(int x = 0; x < game.getPullSize(); x++)
            {
                letters.add(game.removeLetterFromBag());
            }
            
            player.setLetters(letters);
            playerDao.save(player);
        }
        
        game.getPlayers().addAll(Arrays.asList(players));
        gameDao.save(game);
        
        return game;
    }

    public Game playerMove(Move move)
    {
        final int x = move.getX();
        final int y = move.getY();
        
        Game game = gameDao.find(move.getGameId());
        
        if(null == game)
        {
            return null;
        }
        
        if (x < 0 || x > game.getBoardColumns() || y < 0 || y > game.getBoardRows())
        {
            logger.warn("Attempted to place letter outside board scope");
            return game;
        }

        int points = 0;
        Player player = getGamePlayer(move.getPlayerName());
        final Letter[] letters = move.getLetters();
        final Move.Direction direction = move.getDirection();
        final int currentScore = player.getScore();

        for (Letter letter : letters)
        {
            char character;
            
            if (letter.getCharacter() == '_')
            {
                Scanner inputScanner = new Scanner(System.in);
                char selectedCharacter = inputScanner.next().charAt(0);
                character = selectedCharacter;
                inputScanner.close();
            } 
            else
            {
                character = letter.getCharacter();
                Collections.replaceAll(player.getLetters(), letter, game.removeLetterFromBag());
            }
            
            game.getBoard()[x][y] = character;
            
            if (Move.Direction.LEFT_TO_RIGHT.equals(direction))
            {
                move.setX((x + 1));
                playerMove(move);
            } else if (Move.Direction.TOP_TO_BOTTOM.equals(direction))
            {
                move.setX((x - 1));
                playerMove(move);
            }

            points += letter.getValue();
        }

        player.setScore(points + currentScore);        
        game.getMoves().add(move);
        gameDao.save(game);
        
        return game;
    }

    public Player getGamePlayer(String name)
    {
        Player player = null;
        List<Game> games = gameDao.findAll();
        
        for(Game game : games)
        {
            for(Player p : game.getPlayers())
            {
                if(p.getName().equals(p.getName()))
                {
                    player = p;
                    break; //TODO can a player have more than 1 active game?
                }
            }
            
            if(null != player)
            {
                break;
            }
        }
        
        return player;
    }
    
    // TODO refactor into test
    public static void main(String[] args)
    {
        final int pullSize = 9;
        Game game = new Game();
        Player player = new Player("timothy");

        for (int x = 0; x < pullSize; x++)
        {
            player.getLetters().add(game.removeLetterFromBag());
        }

        game.setPullSize(pullSize);
        game.getPlayers().add(player);
        System.out.println(game);
    }
}
