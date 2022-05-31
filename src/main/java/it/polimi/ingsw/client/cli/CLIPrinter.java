package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.network.ArchipelagoStatus;
import it.polimi.ingsw.network.CharacterCardStatus;
import it.polimi.ingsw.network.CloudStatus;
import it.polimi.ingsw.network.IslandStatus;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;

public class CLIPrinter {
    public static String[] colourList = {"🟢","🔴","🟡","🟣","🔵"};
    public static void printDashboard(int[] entrance, int[] hall, int numTowers) {
        /*System.out.println("Entrance: "+colourList()+", Hall: "+colourList()+", "+numTowers+" towers.");
        System.out.print("          ");
        System.out.print(vectToString(entrance)+",       ");
        System.out.println(vectToString(hall));*/
        System.out.println("Entrance: "+vectToString(entrance)+", Hall: "+vectToString(hall)+", "+numTowers+" towers.");
    }

    public static void printArchipelago(ArchipelagoStatus a, int mnIndex) {
        boolean atLeastOneTower = false;
        String s = "Archipelago "+(a.getIndex()+1);
        if((a.getIndex()+1)<10) s+=" ";
        s+=": ";
        /*for (IslandStatus is:a.getIslands()) {
            s += "Island "+is.getIslandIndex()+"\t";
            s+=colourList()+" ";
            if(mnIndex==is.getIslandIndex()) s+="\uD83E\uDDDA\t\t";
            else s+="\t\t";
        }
        System.out.println(s);
        s="\t\t\t";
        for (IslandStatus is:a.getIslands()) {
            s += "\t\t\t\t";
            s+=vectToString(is.getStudents());
            if(is.getTowerColour()!=null) s+=" "+is.getTowerColour()+" ";
            else s+="   ";
        }
        System.out.println(s);*/
        for (IslandStatus is:a.getIslands()) {
            s += "Island "+is.getIslandIndex()+" ";
            if(is.getIslandIndex()<10) s+=" ";
            s+=vectToString(is.getStudents())+" ";
            if(is.getTowerColour()!=null) s+=is.getTowerColour()+" ";
            if(mnIndex==is.getIslandIndex()) s+="\uD83E\uDDDA ";
        }
        System.out.println(s);
    }

    public static void printProfessors(int[] professors) {
        /*String s = "Professors: "+colourList();
        System.out.println(s);
        s = "\t\t\t[";
        for (int i=0;i<professors.length;i++) {
            if(professors[i]==-1) s+="\u274C";
            else if (professors[i]==0) s+="WHITE";
            else if (professors[i]==1) s+="BLACK";
            else s+="GREY";
            if(i<professors.length-1) s+=",";
            else s+="]";
        }
        System.out.println(s);*/
        String s = "Professors: [";
        for (int i=0;i<professors.length;i++) {
            s+=colourList[i];
            if(professors[i]==-1) s+="\u274C";
            else if (professors[i]==0) s+="WHITE";
            else if (professors[i]==1) s+="BLACK";
            else s+="GREY";
            if(i<professors.length-1) s+=",";
            else s+="]";
        }
        System.out.println(s);
    }

    public static void printClouds(ArrayList<CloudStatus> clouds) {
        /*String s ="";
        for(int i=0;i<clouds.size();i++) {
            s+="Cloud "+(clouds.get(i).getIndex()+1)+": "+colourList()+"\t";
        }
        System.out.println(s);
        s="";
        for(int i=0;i<clouds.size();i++) {
            s+="\t\t "+vectToString(clouds.get(i).getStudents())+"\t";
        }
        System.out.println(s);*/
        String s ="";
        for(int i=0;i<clouds.size();i++) {
            s+="Cloud "+(clouds.get(i).getIndex()+1)+": "+vectToString(clouds.get(i).getStudents())+"\t";
        }
        System.out.println(s);
    }

    public static void printCCards(CharacterCardStatus card, String description) {
        /*boolean optionalComponents = false;
        boolean students = false;
        System.out.println("Card "+(card.getIndex()+1)+": "+description);
        String s = "";
        if(!vectEmpty(card.getStudents())) {
            optionalComponents = true;
            students = true;
            s+="Students: "+colourList()+"\t";
        }
        if(card.getNoEntryTiles()!=0) {
            optionalComponents = true;
            s+=card.getNoEntryTiles()+" NoEntryTiles";
        }
        if(optionalComponents) System.out.println(s);
        if(students) System.out.println("\t\t  "+vectToString(card.getStudents()));*/
        System.out.println("Card "+(card.getIndex()+1)+": "+description);
        String s = "";
        boolean otherComponents = false;
        if(!vectEmpty(card.getStudents())) {
            otherComponents = true;
            s+="Students: "+vectToString(card.getStudents())+"\t";
        }
        if(card.getNoEntryTiles()!=0) {
            otherComponents = true;
            s+=card.getNoEntryTiles()+" NoEntryTiles";
        }
        if(otherComponents) System.out.println(s);
    }

    private static boolean vectEmpty(int[] students) {
        for(int i: students) if(i!=0) return false;
        return true;
    }

    private static String colourList() {
        return "[\uD83D\uDFE1,\uD83D\uDFE2,\uD83D\uDD35,\uD83D\uDFE3,\uD83D\uDD34]";
    }
    private static String vectToString(int[] v) {
        String s = "[";
        for(int i=0;i<v.length-1;i++) {
            s+=colourList[i]+v[i]+" ,";
        }
        s+=colourList[v.length-1]+v[v.length-1]+"]";
        return s;
    }
}


