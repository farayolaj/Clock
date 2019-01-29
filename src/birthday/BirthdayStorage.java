/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package birthday;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FarayolaJ
 */
public class BirthdayStorage {

    private static final File FILE = new File("birthday.txt");

    public static LocalDate put(String key, LocalDate value) {
        HashMap<String, LocalDate> mp = open();
        LocalDate date = mp.put(key, value);
        save(mp);
        return date;
    }

    public static LocalDate get(String key) {
        return open().get(key);
    }

    public static LocalDate remove(String key) {
        HashMap<String, LocalDate> mp = open();
        LocalDate date = mp.remove(key);
        save(mp);
        return date;
    }

    public static Set<String> keySet() {
        return open().keySet();
    }

    private static HashMap<String, LocalDate> save(HashMap<String, LocalDate> cs) {
        ObjectOutputStream objOut = null;
        try {
            FileOutputStream out = new FileOutputStream(FILE);
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(cs);
            return cs;
        } catch (IOException ex) {
            Logger.getLogger(BirthdayStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                objOut.close();
            } catch (IOException ex) {
                Logger.getLogger(BirthdayStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private static HashMap<String, LocalDate> open() {
        ObjectInputStream objIn = null;
        try {
            FILE.createNewFile();
            FileInputStream in = new FileInputStream(FILE);
            try {
                objIn = new ObjectInputStream(in);
                return (HashMap<String, LocalDate>) objIn.readObject();
            } catch (EOFException ex) {
                return save(new HashMap<>());
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(BirthdayStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (objIn != null) {
                    objIn.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(BirthdayStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
