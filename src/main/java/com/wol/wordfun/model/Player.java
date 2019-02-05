package com.wol.wordfun.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "wrd_player")
public class Player implements Serializable
{
    private static final long serialVersionUID = 1L;

    @JsonProperty
    @NotNull
    @Id
    @Column(name = "player_id", unique = true, nullable = false, updatable = false, length = 80)
    private String id;
    
    @JsonProperty
    @NotNull
    @Column(name = "name", nullable = false, updatable = true, unique = true, length = 25)
    private String name;
    
    @JsonProperty
    @Transient
    private List<Letter> letters;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Move> moves;
    
    @JsonProperty
    @Column
    private int score = 0;
    
    //This field is used to persist the player's letters without the
    //letter modeling needing to be a persistable entity.
    @JsonIgnore
    @Column
    private String characters;
    
    public Player()
    {
        this.letters = new ArrayList<Letter>();
        this.moves = new ArrayList<Move>();
    }
    
    public Player(String name)
    {
        this();
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Letter> getLetters()
    {
        return letters;
    }

    public void setLetters(List<Letter> letters)
    {
        this.letters = letters;
        setCharacters(this.letters.toString());
    }

    public List<Move> getMoves()
    {
        return moves;
    }

    public void setMoves(List<Move> moves)
    {
        this.moves = moves;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public String getCharacters()
    {
        return this.characters;
    }

    public void setCharacters(String characters)
    {
        this.characters = characters;
    }

    public String toString()
    {
        return "Player [name=" + name + ", letters=" + letters + ", score=" + score + "]";
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
