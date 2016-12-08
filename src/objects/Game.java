package objects;

import java.io.File;
import java.util.*;

/**
 * Created by Florian on 08.12.2016.
 */
public class Game {
    //Attrbute
    private String name;
    private String pfad;

    //Methoden
    public Game (String name, String pfad){
        this.name=name;
        this.pfad=pfad;
    }

    public String getName(){
        return this.name;
    }

    public String getPfad(){
        return this.pfad;
    }

    public static List<Game> readGamesFolder(){
        List<Game> gamesInFolder=new ArrayList<>();
        File f=new File("C:\\Users\\Florian\\Desktop\\Games"); //nur als Test, muss noch angepasst werden
        File[] fileArray = f.listFiles();
        for(File gelesen : fileArray){
            if(gelesen.getName().endsWith(".jar")){
                String nameAktuell=gelesen.getName().substring(0, (gelesen.getName().length()-4));
                String pfadAktuell=gelesen.getAbsolutePath();
                Game aktuellesSpiel=new Game(nameAktuell,pfadAktuell);
                gamesInFolder.add(aktuellesSpiel);
            }
        }
        return gamesInFolder;
    }

}
