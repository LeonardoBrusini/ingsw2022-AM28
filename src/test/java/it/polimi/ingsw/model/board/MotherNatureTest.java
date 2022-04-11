package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.ProfessorGroup;
import it.polimi.ingsw.model.StudentGroup;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.board.MotherNature;
import it.polimi.ingsw.model.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureTest {
    private MotherNature m;

    @Test
    void getIslandIndex() {
        m = new MotherNature();
        int res = m.getIslandIndex();
        m.setIsland(res);
        int con = m.getIslandIndex();
        assertEquals(res,con);
    }

    @Test
    void setIsland() {
        m = new MotherNature();
        m.setIsland(12);

        assertEquals(12,m.getIslandIndex());

    }
    /*
    @Test
    void playerWithMostInfluence(){
        IslandManager im = new IslandManager(3);
        //im.getArchipelagos().get(2).merge(im.getArchipelagoByIslandIndex(3));
        EnumMap<Colour, Tower> en = new EnumMap<>(Colour.class);
        ProfessorGroup pg = new ProfessorGroup();
        en.put(Colour.RED, Tower.BLACK);
        en.put(Colour.YELLOW, Tower.WHITE);
        pg.setProfessors(en);
        im.getIsland(3).setStudents(new StudentGroup(new Bag().removeStudentGroup(5)));
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("g1"));
        players.add(new Player("g2"));

    }*/
}