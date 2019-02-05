package com.wol.wordfun.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "wrd_game")
public class Game implements Serializable
{
    public static final int DEFAULT_WIDTH = 15;
    public static final int DEFAULT_HEIGHT = 15;
    public static final int DEFAULT_PULL_SIZE = 7;
    public static final int BAG_SIZE = 100;
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty
    @NotNull
    @Id
    @Column(name = "game_id", unique = true, nullable = false, updatable = false, length = 80)
    private String id;
    
    @JsonProperty
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable
    private List<Player> players;
    
    @JsonProperty
    @Column
    private Date created;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Move> moves;

    @JsonIgnore
    @Transient
    private List<Letter> letterBag;
    
    @JsonIgnore
    @Column
    private int boardColumns = DEFAULT_WIDTH;
    
    @JsonIgnore
    @Column
    private int boardRows = DEFAULT_HEIGHT;
    
    @JsonIgnore
    @Column
    private int pullSize = DEFAULT_PULL_SIZE;
    
    @JsonIgnore
    @Transient
    private char[][] board = new char[boardColumns][boardRows]; 
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Move lastMove;
    
    public Game()
    {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.players = new ArrayList<Player>();
        this.moves = new ArrayList<Move>();
        this.created = new Date();
        this.letterBag = createLetterBag();
    }
    
    public Game(int columns, int rows, int pullSize)
    {
        this();
        this.boardColumns = columns;
        this.boardRows = rows;
        this.pullSize = pullSize;
        this.board = new char[boardColumns][boardRows];
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Player> players)
    {
        this.players = players;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public List<Move> getMoves()
    {
        return moves;
    }

    public void setMoves(List<Move> moves)
    {
        this.moves = moves;
    }

    public int getPullSize()
    {
        return pullSize;
    }

    public void setPullSize(int pullSize)
    {
        this.pullSize = pullSize;
    }

    public Letter removeLetterFromBag()
    {
        final int size = this.letterBag.size();
        final int itemIndex = new Random().nextInt(size);        
        Letter letter = this.letterBag.get(itemIndex);
        
        this.letterBag.remove(itemIndex);
        return letter;
    }

    public int getBoardColumns()
    {
        return boardColumns;
    }

    public void setBoardColumns(int boardColumns)
    {
        this.boardColumns = boardColumns;
    }

    public int getBoardRows()
    {
        return boardRows;
    }

    public void setBoardRows(int boardRows)
    {
        this.boardRows = boardRows;
    }

    public char[][] getBoard()
    {
        return board;
    }

    public Move getLastMove()
    {
        return lastMove;
    }

    public void setLastMove(Move lastMove)
    {
        this.lastMove = lastMove;
    }

    public String toString()
    {
        return "Game [id=" + id + ", players=" + players + ", created=" + created + "]";
    }

    //TODO this method is heavy, refactor
    private List<Letter> createLetterBag()
    {
        List<Letter> bag = new ArrayList<Letter>();
        final int alphabetSize = Letter.alphabet.length;
        
        for(int x = 0; x < alphabetSize; x++)
        {
            char character = Letter.alphabet[new Random().nextInt(alphabetSize - x)];        
            int multiplier = 0;
            int value = 0;
            
            if(character == '_')
            {
                multiplier = 2;
                value = 0;
            }
            else if(character == 'a' || character == 'e' || character == 'i' || character == 'o' 
                    || character == 'u' || character == 'n' || character == 'r' || character == 't' 
                    || character == 'l' || character == 's')
            {
                value = 1;
                
                switch(character)
                {
                    case 'a':
                    case 'i':
                        multiplier = 9;
                        break;
                    case 'e':
                        multiplier = 12;
                        break;
                    case 'o':
                        multiplier = 8;
                        break;
                    case 'n':
                    case 'r':
                    case 't':
                        multiplier = 6;
                        break;
                    case 'l':
                    case 's':
                    case 'u':
                        multiplier = 4;
                        break;
                }
            }
            else if (character == 'd' || character == 'g')
            {
                value = 2;
                
                switch(character)
                {
                    case 'd':
                        multiplier = 4;
                        break;
                    case 'g':
                        multiplier = 3;
                        break;
                }
            }
            else if (character == 'b' || character == 'c' || character == 'm' || character == 'p')
            {
                multiplier = 2;
                value = 3;
            }
            else if(character == 'f' || character == 'h' || character == 'v' || character == 'w' || character == 'y')
            {
                multiplier = 2;
                value = 2;
            }
            else if(character == 'k' || character == 'j' || character == 'x' || character == 'q' || character == 'z')
            {
                multiplier = 1;
                
                switch(character)
                {
                    case 'k':
                        value = 5;
                        break;
                    case 'j':
                    case 'x':
                        value = 1;
                        break;
                    case 'q':
                    case 'z':
                        value = 10;
                        break;
                }
                
                value = 5;
            }           
            
            for(int y = 0; y < multiplier; y++)
            {
                bag.add(new Letter(Character.toUpperCase(character), value));
            }
            
        }
        
        return bag;
    }
    
}
