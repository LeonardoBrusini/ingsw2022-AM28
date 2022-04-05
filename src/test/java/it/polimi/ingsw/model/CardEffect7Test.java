package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardEffect7Test {

    @Test
    void resolveEffect(CharacterCard c) {
        Dashboard d = c.getPlayerThisTurn().getDashboard();
        StudentGroup sp = d.getEntrance();
        StudentGroup sc = c.getSelectedStudentsFrom();
        //resolveEffect(c);
        for(Colour cc: Colour.values()){
            assertEquals(sp.getQuantityColour(cc), c.getSelectedStudentsTo().getQuantityColour(cc));
            assertEquals(sc.getQuantityColour(cc), d.getEntrance().getQuantityColour(cc));
        }
    }
}