package it.unibo.deathnote;

import it.unibo.deathnote.api.DeathNote;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static java.lang.Thread.sleep;

import org.junit.jupiter.api.BeforeEach;

class TestDeathNote {

    private static final int NEGATIVE_VALUE = -100;
    private static final String EMPTY_STRING = "";
    private static final String CAUSE_DEATH = "This is a cause of death";
    private static final String DEATH_BY_KARTING = "karting accident";
    private static final String LIGHT = "Light Yagami";
    private static final String L = "L";
    private static final long SLEEP_ERROR = 100;
    private static final long SLEEP_DETAILS = 6000 + SLEEP_ERROR;
    private DeathNoteImpl myDeathNote;

    @BeforeEach
    void setup() {
        myDeathNote = new DeathNoteImpl();
    }

    @Test
    void test1() {
        for (final var index: List.of(0, 1, NEGATIVE_VALUE)) {
            assertThrows(
                new IllegalArgumentThrower() {
                    @Override
                    public void run() {
                        myDeathNote.getRule(index);
                    }
                }
            );
        }

        /*try {
            myDeathNote.getRule(0);
            myDeathNote.getRule(NEGATIVE_VALUE);

            fail("There should be an exception");

        } catch(IllegalAccessError e) {
             assertNotNull(e.getMessage()); // Non-null message
             assertFalse(e.getMessage().isBlank()); // Not a blank or empty message
             assertTrue(e.getMessage().length() >= ACCEPTABLE_MESSAGE_LENGTH); // A message with a decent length
        }
        */
    }

    @Test
    void test2() {
        for (final String s : DeathNote.RULES) {
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }

    @Test
    void test3() {
        final String name = "Light Yagami";
        final String anotherName = "L";

        assertFalse(myDeathNote.isNameWritten(name));
        myDeathNote.writeName(name);
        assertTrue(myDeathNote.isNameWritten(name));
        assertFalse(myDeathNote.isNameWritten(anotherName));
        assertFalse(myDeathNote.isNameWritten(EMPTY_STRING));
    }

    @Test
    void test4() throws InterruptedException {
        assertThrows(
            new IllegalStateThrower() {
                @Override
                public void run() {
                    myDeathNote.writeDeathCause(CAUSE_DEATH);
                }
            }
        );

        myDeathNote.writeName(LIGHT);
        assertEquals("heart attack", myDeathNote.getDeathCause(LIGHT));
        myDeathNote.writeName(L);
        assertTrue(myDeathNote.writeDeathCause(DEATH_BY_KARTING));
        assertEquals(DEATH_BY_KARTING, myDeathNote.getDeathCause(L));
        sleep(SLEEP_ERROR);
        assertFalse(myDeathNote.writeDeathCause(CAUSE_DEATH));
        assertEquals(DEATH_BY_KARTING, myDeathNote.getDeathCause(L));
    }

    @Test
    void test5() throws InterruptedException {

        assertThrows(
            new IllegalStateThrower() {
                @Override
                public void run() {
                    myDeathNote.writeDeathCause(CAUSE_DEATH);
                }
            }
        );
        myDeathNote.writeName(LIGHT);
        assertEquals(EMPTY_STRING, myDeathNote.getDeathDetails(LIGHT));
        assertTrue(myDeathNote.writeDetails("ran for too long"));
        assertEquals("ran for too long", myDeathNote.getDeathDetails(LIGHT));
        myDeathNote.writeName(L);
        sleep(SLEEP_DETAILS);
        assertFalse(myDeathNote.writeDetails("i'm triyng to change"));
        assertEquals(EMPTY_STRING, myDeathNote.getDeathDetails(L));
    }

    static void assertThrows(final RuntimeExceptionThrower exceptionThrower) {
        try {
            exceptionThrower.run();
            fail("Exception was expected, but not thrown");
        } catch (IllegalStateException | IllegalArgumentException e) {
            assertTrue(
                exceptionThrower instanceof IllegalArgumentThrower && e instanceof IllegalArgumentException // NOPMD
                || exceptionThrower instanceof IllegalStateThrower && e instanceof IllegalStateException // NOPMD
            );
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().isBlank());
        }
    }

    @FunctionalInterface
    private interface RuntimeExceptionThrower {
        void run();
    }

    @FunctionalInterface
    private interface IllegalStateThrower extends RuntimeExceptionThrower { }

    @FunctionalInterface
    private interface IllegalArgumentThrower extends RuntimeExceptionThrower { }
}
