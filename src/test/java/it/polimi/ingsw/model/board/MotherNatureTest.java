package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.MotherNature;
import org.junit.jupiter.api.Test;

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
}