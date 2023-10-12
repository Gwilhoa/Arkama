package fr.mediapi.arkama.murder;

import fr.mediapi.arkama.util.SerializableVector;

import java.io.*;
import java.util.ArrayList;

public class Arena implements Serializable {

    private static final long serialVersionUID = -569565496456465125L;
    public String world;
    public ArrayList<SerializableVector> scrapSpawns;
    public ArrayList<SerializableVector> playerSpawns;
    public String name;

    public Arena(String name, String world) {
        this.name = name;
        this.world = world;
        scrapSpawns = new ArrayList<>();
        playerSpawns = new ArrayList<>();
    }

    public static Arena load(File file) {
        ObjectInputStream ois = null;
        Arena arena = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            arena = (Arena) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arena;
    }

    public void save() {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(name + "_arena.ser");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
