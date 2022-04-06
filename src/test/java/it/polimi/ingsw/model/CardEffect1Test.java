package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardEffect1Test {
    private EffectStrategy card;
    @Test
    void resolveEffect(CharacterCard c) {
      this.card = new CardEffect1();
      StudentGroup students = new StudentGroup(3);
      assertEquals("CardEffect1",c.getFileName());
      assertEquals(card,c.getEffect());
      assertEquals(1, c.getPrice());
      c.setStudentsOnCard(students);
      Island i = new Island(4);
      i.setStudents(new StudentGroup(4));
      c.setSelectedIsland(i);
        int counter = 0;
        for(Colour colour: Colour.values()){
            counter += students.getQuantityColour(colour);
        }
      for(Colour color: Colour.values()){
          int countIsland = i.getStudents().getQuantityColour(color);
          int countCard = students.getQuantityColour(color);
          c.setSelectedColour(color);
          //resolveEffect(c);
          assertEquals(countIsland + countCard,i.getStudents().getQuantityColour(color));
          assertEquals(countCard, counter);
      }
    }
}