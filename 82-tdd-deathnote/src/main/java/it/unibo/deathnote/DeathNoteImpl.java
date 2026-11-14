package it.unibo.deathnote;

import it.unibo.deathnote.api.DeathNote;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class is an implementation of the interface: {@link it.unibo.deathnote.api.DeathNote}.
 */
public class DeathNoteImpl implements DeathNote {

    private final Map<String, Death> deathNote;
    private String lastName;

    /**
     * A new Deathnote.
     */
    public DeathNoteImpl() {
        deathNote = new HashMap<>();
    }

    private Death getDeath(final String name) {
        final Death death = deathNote.get(name);
        if (death == null) {
            throw new IllegalArgumentException("there should be a valid name");
        }
        return death;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        return getDeath(name).cause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
         return getDeath(name).details;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber <= 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("the rule number" + ruleNumber + "doesn't exist");
        }
        return RULES.get(ruleNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
       return deathNote.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(final String cause) {
       if (!updatePossible(cause)) {
         return false;
       }
        final Death oldDeath = getDeath(lastName);
        final Death newDeath = getDeath(lastName).writeCause(cause);
        if (newDeath.equals(oldDeath)) {
            return false;
        }
        deathNote.put(lastName, newDeath);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(final String details) {
       if (!updatePossible(details)) {
         return false;
       }
       final Death oldDeath = getDeath(lastName);
       final Death newDeath = getDeath(lastName).writeDetails(details);
       if (newDeath.equals(oldDeath)) {
            return false;
       }
       deathNote.put(lastName, newDeath);
       return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);
        deathNote.put(name, new Death());
        this.lastName = name;
    }

    private boolean updatePossible(final String update) {
        if (update == null) {
            throw new IllegalArgumentException("the argument can't be null");
        }
        if (lastName == null) {
            throw new IllegalStateException("there is noone written in the deathnote");
        }
        return true;
    }

    private class Death {
        private static final String DEFAULT_DEATH = "heart attack";
        private static final long DEFAULT_TIME_CAUSE = 40;
        private static final long DEFAULT_TIME_DETAILS = 6000 + DEFAULT_TIME_CAUSE;
        private String cause;
        private String details;
        private long timeOfDeath;
        private int hash;

        Death(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            this.timeOfDeath = System.currentTimeMillis();
        }

        Death() {
            this(DEFAULT_DEATH, "");
        }

        Death writeCause(final String causeToWrite) {
            return System.currentTimeMillis() < (timeOfDeath + DEFAULT_TIME_CAUSE)
                   ? new Death(causeToWrite, this.details)
                   : this;
        }

        Death writeDetails(final String detailsToWrite) {
            return System.currentTimeMillis() < (timeOfDeath + DEFAULT_TIME_DETAILS)
                   ? new Death(this.cause, detailsToWrite)
                   : this;
        }

        String getCause() {
            return this.cause;
        }

        String getDetails() {
            return this.details;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Death other)) {
                return false;
            }
            return Objects.equals(cause, other.cause)
                && Objects.equals(details, other.details)
                && timeOfDeath == other.timeOfDeath;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = Objects.hash(cause, details, timeOfDeath);
            }
            return hash;
        }
    }
}
