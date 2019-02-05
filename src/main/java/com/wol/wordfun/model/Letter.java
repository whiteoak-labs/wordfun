package com.wol.wordfun.model;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Letter implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public static final char[] alphabet = "_abcdefghijklmnopqrstuvwxyz".toCharArray();

    @JsonIgnore
    @NotNull
    private String uuid;
    
    @JsonProperty
    @NotNull
    private char character;
    
    @JsonProperty
    @NotNull
    private int value;

    public Letter(char character, int value)
    {
        this.uuid = UUID.randomUUID().toString().replaceAll("=", "");
        this.character = character;
        this.value = value;
    }

    public char getCharacter()
    {
        return character;
    }

    public void setCharacter(char character)
    {
        this.character = character;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public String toString()
    {
        return "Letter [character=" + character + ", value=" + value + "]";
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + character;
        result = prime * result + value;
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
        Letter other = (Letter) obj;
        if (character != other.character)
            return false;
        if (value != other.value)
            return false;
        return true;
    }
    
    //TODO refactor
    public static Letter fromString(String stringValue)
    {
        if(false == StringUtils.isEmpty(stringValue))
        {
            if(stringValue.indexOf('[') > 0)
            {
                String[] parts = (String[])stringValue.substring(stringValue.indexOf("["), stringValue.indexOf("]")).split(",");
                char character = parts[0].split("=")[1].charAt(0);
                int value = parts[1].split("=")[1].charAt(0);
                return new Letter(character, value);
            }
            
        }
        
        return null;
    }
}
