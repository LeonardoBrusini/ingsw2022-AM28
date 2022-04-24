package it.polimi.ingsw.model.board;

import it.polimi.ingsw.enumerations.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureTest {
    private MotherNature m;
    private final ArrayList<CharacterCard> cards = new ArrayList<>();

    /**
     * The test checks that the methods returns the index of the Island on witch Mother Nature effectively is
     */
    @Test
    void getIslandIndex() {
        m = new MotherNature();
        int res = m.getIslandIndex();
        m.setIsland(res);
        int con = m.getIslandIndex();
        assertEquals(res, con);
    }

    /**
     * It verifies that the methods sets the right Island's index on witch Mother Nature effectively is
     */
    @Test
    void setIsland() {
        m = new MotherNature();
        m.setIsland(12);

        assertEquals(12, m.getIslandIndex());

    }

    /**
     *It tests if the methods returns the Player with more influence on the Island on witch Mother Nature is
     */
    @Test
    void playerWithMostInfluence() {
        MotherNature mn = new MotherNature();
        mn.setIsland(3);
        IslandManager im = new IslandManager(3);
        ArrayList<Player> players = new ArrayList<>();
        StudentGroup st = new StudentGroup(2);
        st.addStudent(Colour.BLUE);
        im.getIslandByIndex(3).setStudents(st);
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        ProfessorGroup pg = new ProfessorGroup();
        Player p1 = new Player("g1", Tower.BLACK);
        Player p2 = new Player("g2", Tower.WHITE);
        players.add(p1);
        players.add(p2);
        assertNull(mn.playerWithMostInfluence(players,im,pg,cards));

        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.BLUE, Tower.BLACK);
        en.put(Colour.YELLOW, Tower.WHITE);
        pg.setProfessors(en);
        assertEquals(p1, mn.playerWithMostInfluence(players, im, pg,cards));

        im.getArchipelagos().get(2).merge(im.getArchipelagoByIslandIndex(3));
        assertEquals(players.get(0), mn.playerWithMostInfluence(players, im, pg,cards));

        Player p3 = new Player("g3", Tower.GRAY);
        players.add(p3);
        en.put(Colour.BLUE, Tower.GRAY);
        pg.setProfessors(en);
        assertEquals(p3, mn.playerWithMostInfluence(players, im, pg,cards));

    }
}