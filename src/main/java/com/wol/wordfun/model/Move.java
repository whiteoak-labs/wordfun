package com.wol.wordfun.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "wrd_player_move")
public class Move implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static enum Direction
    {
        LEFT_TO_RIGHT,
        
        TOP_TO_BOTTOM;
    }
    
    @JsonProperty
    @NotNull
    @Id
    @Column(name = "move_id", unique = true, nullable = false, updatable = false, length = 80)
    private String id;
    
    @JsonProperty
    @NotNull
    @Column(name = "game_id", nullable = false, length = 80, updatable = false)
    private String gameId;
    
    @JsonProperty
    @NotNull
    @Column(name = "player_name", nullable = false, length = 25, updatable = false)
    private String playerName;
    
    @JsonProperty
    @NotNull
    @Transient
    private Letter[] letters;
    
    @JsonProperty
    @NotNull
    @Column
    private int x;
    
    @JsonProperty
    @NotNull
    @Column
    private int y;

    @JsonProperty
    @Column
    private int value;
    
    @JsonProperty
    @Column
    private Direction direction;
    
    public Move()
    {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public Move(String playerName, Letter[] letters, 
            int x, int y, Direction direction, int value)
    {
        this();
        this.playerName = playerName;
        this.letters = letters;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getGameId()
    {
        return gameId;
    }

    public void setGameId(String gameId)
    {
        this.gameId = gameId;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public Letter[] getLetters()
    {
        return letters;
    }

    public void setLetters(Letter[] letters)
    {
        this.letters = letters;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public String toString()
    {
        return "Move [player=" + playerName + ", word=" + letters 
                + ", x=" + x + ", y=" + y + "]";
    }
    
}
