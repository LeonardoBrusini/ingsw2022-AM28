package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.enumerations.CharacterCardInfo;
import it.polimi.ingsw.server.enumerations.Colour;
import it.polimi.ingsw.server.model.StudentGroup;
import it.polimi.ingsw.server.model.ProfessorGroup;
import it.polimi.ingsw.server.enumerations.Tower;
import it.polimi.ingsw.server.model.cards.CharacterCard;
import it.polimi.ingsw.server.model.players.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    private Island i;
    private final ArrayList<CharacterCard> cards = new ArrayList<>();

    /**
     * It tests that a student is correctly added to the island
     */
    @Test
    void addStudent() {
        i = new Island(3);
        for(Colour c : Colour.values())
            Assertions.assertEquals(0,i.getStudents().getQuantityColour(c));

        i.addStudent(Colour.YELLOW);

        for(Colour c : Colour.values()) {
            if (c != Colour.YELLOW)
                Assertions.assertEquals(0, i.getStudents().getQuantityColour(c));
            else
                Assertions.assertEquals(1, i.getStudents().getQuantityColour(c));
        }
    }

    /**
     * It tests if the method sets the given Tower on the Island
     */
    @Test
    void setTower() {
        i = new Island(3);
        assertNull(i.getTower());
        for(Tower t: Tower.values()) {
            i.setTower(t);
            assertEquals(t, i.getTower());
        }
    }

    /**
     * To test if is returned the right Player's Tower set on the Island
     */
    @Test
    void getTower() {
        i = new Island(3);
        Tower d = Tower.BLACK;
        i.setTower(d);
        assertEquals(d,i.getTower());
    }

    /**
     * It verifies if is returned the correct index of the Island
     */
    @Test
    void getIslandIndex() {
        i = new Island(3);
        assertEquals(3,i.getIslandIndex());
    }

    /**
     * It verifies that the method erases all the students on the Island
     */
    @Test
    void clearStudents() {
        i = new Island(3);
        for(Colour c : Colour.values())
            Assertions.assertEquals(0,i.getStudents().getQuantityColour(c));

        i.addStudent(Colour.YELLOW);

        for(Colour c : Colour.values()) {
            if (c != Colour.YELLOW)
                Assertions.assertEquals(0, i.getStudents().getQuantityColour(c));
            else
                Assertions.assertEquals(1, i.getStudents().getQuantityColour(c));
        }

        i.clearStudents();
        for(Colour c : Colour.values())
            Assertions.assertEquals(0,i.getStudents().getQuantityColour(c));

    }

    /**
     * The test checks if is returned the right influence associated with the given Player
     */
    @Test
    void playerInfluence(){
        i = new Island(3);
        Bag bag = new Bag();
        i.setStudents(new StudentGroup(2));
        ProfessorGroup pg = new ProfessorGroup();
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        pg.setProfessors(en);
        assertEquals(0,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        pg.setProfessors(en);
        assertEquals(2,i.playerInfluence(Tower.BLACK, pg, cards));
        i.setTower(Tower.BLACK);
        assertEquals(3,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        i.setTower(Tower.WHITE);
        assertEquals(2, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.WHITE);
        en.put(Colour.RED, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(2, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(3, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(2, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.BLACK);
        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.PINK, Tower.GRAY);
        en.put(Colour.BLUE, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(6, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(4, i.playerInfluence(Tower.GRAY, pg, cards));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg, cards));
    }

    /**
     * The test checks if is returned the right influence associated with the given Player, according to the special conditions set by CharacterCard CARD6
     */
    @Test
    public void playerInfluenceCARD6() {
        cards.add(new CharacterCard(CharacterCardInfo.CARD6));
        cards.get(0).getCardInfo().getEffect().resolveEffect(cards.get(0));
        i = new Island(3);
        Bag bag = new Bag();
        i.setStudents(new StudentGroup(2));
        ProfessorGroup pg = new ProfessorGroup();
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        pg.setProfessors(en);
        assertEquals(0,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        pg.setProfessors(en);
        assertEquals(2,i.playerInfluence(Tower.BLACK, pg, cards));
        i.setTower(Tower.BLACK);
        assertEquals(2,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        i.setTower(Tower.WHITE);
        assertEquals(2, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.WHITE);
        en.put(Colour.RED, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(2, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(2, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(2, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.BLACK);
        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.PINK, Tower.GRAY);
        en.put(Colour.BLUE, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(6, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(4, i.playerInfluence(Tower.GRAY, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.WHITE, pg, cards));
    }

    /**
     * The test checks if is returned the right influence associated with the given Player, according to the special conditions set by CharacterCard CARD6
     */
    @Test
    public void playerInfluenceCARD9() {
        cards.add(new CharacterCard(CharacterCardInfo.CARD9));
        cards.get(0).setSelectedColour(Colour.YELLOW);
        cards.get(0).getCardInfo().getEffect().resolveEffect(cards.get(0));
        i = new Island(3);
        Bag bag = new Bag();
        i.setStudents(new StudentGroup(2));
        ProfessorGroup pg = new ProfessorGroup();
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        pg.setProfessors(en);
        assertEquals(0,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        pg.setProfessors(en);
        assertEquals(0,i.playerInfluence(Tower.BLACK, pg, cards));
        i.setTower(Tower.BLACK);
        assertEquals(1,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        i.setTower(Tower.WHITE);
        assertEquals(0, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.WHITE);
        en.put(Colour.RED, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(0, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(3, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(2, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.BLACK);
        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.PINK, Tower.GRAY);
        en.put(Colour.BLUE, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(4, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(4, i.playerInfluence(Tower.GRAY, pg, cards));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg, cards));
    }

    /**
     * The test checks if is returned the right influence associated with the given Player, according to the special conditions set by CharacterCard CARD8
     */
    @Test
    public void playerInfluenceCARD8() {
        cards.add(new CharacterCard(CharacterCardInfo.CARD8));
        Player p = new Player(Tower.BLACK);
        cards.get(0).setPlayerThisTurn(p);
        cards.get(0).getCardInfo().getEffect().resolveEffect(cards.get(0));
        i = new Island(3);
        Bag bag = new Bag();
        i.setStudents(new StudentGroup(2));
        ProfessorGroup pg = new ProfessorGroup();
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        pg.setProfessors(en);
        assertEquals(2,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        pg.setProfessors(en);
        assertEquals(4,i.playerInfluence(Tower.BLACK, pg, cards));
        i.setTower(Tower.BLACK);
        assertEquals(5,i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(0,i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        i.setTower(Tower.WHITE);
        assertEquals(4, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(0, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.WHITE);
        en.put(Colour.RED, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(4, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(3, i.playerInfluence(Tower.WHITE, pg, cards));
        assertEquals(2, i.playerInfluence(Tower.GRAY, pg, cards));
        en.put(Colour.YELLOW, Tower.BLACK);
        en.put(Colour.GREEN, Tower.BLACK);
        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.PINK, Tower.GRAY);
        en.put(Colour.BLUE, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(8, i.playerInfluence(Tower.BLACK, pg, cards));
        assertEquals(4, i.playerInfluence(Tower.GRAY, pg, cards));
        assertEquals(1, i.playerInfluence(Tower.WHITE, pg, cards));
    }
}