package it.unibo.deathnote;

import it.unibo.deathnote.api.DeathNote;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class TestDeathNote {

    private static final int ACCEPTABLE_MESSAGE_LENGTH = 10;
    private static final int NEGATIVE_VALUE = -100;
    private static final String EMPTY_STRING = "";
    private DeathNoteImpl myDeathNote;
    @BeforeEach 
    void setup(){
        myDeathNote = new DeathNoteImpl(); 
    }

    @Test 
    public void test1(){
        try{
            myDeathNote.getRule(0);
            myDeathNote.getRule(NEGATIVE_VALUE);

            fail("There should be an exception");

        }catch(IllegalAccessError e){
             assertNotNull(e.getMessage()); // Non-null message
             assertFalse(e.getMessage().isBlank()); // Not a blank or empty message
             assertTrue(e.getMessage().length() >= ACCEPTABLE_MESSAGE_LENGTH); // A message with a decent length
        }
    }

    public void test2(){
        for(String s : DeathNote.RULES){
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }

    public void test3(){
        String name = "Light Yagami";
        String anotherName = "L";

        assertFalse(myDeathNote.isNameWritten(name));
        myDeathNote.writeName(name);
        assertTrue(myDeathNote.isNameWritten(name));
        assertFalse(myDeathNote.isNameWritten(anotherName));
        assertFalse(myDeathNote.isNameWritten(EMPTY_STRING));
    }

    public void test4(){
        
    }
}