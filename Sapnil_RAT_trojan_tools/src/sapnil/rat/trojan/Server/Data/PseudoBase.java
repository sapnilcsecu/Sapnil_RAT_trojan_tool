package sapnil.rat.trojan.Server.Data;


import sapnil.rat.trojan.Logger.Level;
import sapnil.rat.trojan.Logger.Logger;
import sapnil.rat.trojan.Maus.ClientBuilder;
import sapnil.rat.trojan.Server.ClientObject;
import sapnil.rat.trojan.Server.SapnilSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import sapnil.rat.trojan.Server.Server;
import sapnil.rat.trojan.Server.SapnilSettings;

import java.io.*;

public class PseudoBase implements Repository {
    /* Collection of client connections by representation of ClientObjects*/
    private static ObservableMap<String, ClientObject> mausData = FXCollections.observableHashMap();

    public synchronized static ObservableMap<String, ClientObject> getMausData() {
        return mausData;
    }

    /* Serializes client objects  & writes server settings */
    public static void writeMausData() throws IOException {
        for (ClientObject o : mausData.values()) {
            if (o != null) {
                o.serialize();
            }
        }
        File data = new File(System.getProperty("user.home") + "/Maus/.serverSettings");
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(data))) {
            writer.write(SapnilSettings.CONNECTION_IP + " ");
            writer.write(SapnilSettings.SHOW_NOTIFICATIONS + " ");
            writer.write(SapnilSettings.BACKGROUND_PERSISTENT + " ");
            writer.write(SapnilSettings.MAX_CONNECTIONS + " ");
            writer.write(SapnilSettings.PORT + " ");
            writer.write(SapnilSettings.SOUND + " ");
        } catch (IOException i) {
            Logger.log(Level.ERROR, i.toString());
        }

        File mauscs = new File(System.getProperty("user.home") + "/Maus/.mauscs");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mauscs))) {
            writer.write(sapnil.rat.trojan.Server.SapnilSettings.CONNECTION_IP + "\n" + " ");
            writer.write("" + SapnilSettings.PORT + "\n" + " ");
            writer.write("" + ClientBuilder.isPersistent + "\n" + " ");
            writer.write("" + ClientBuilder.autoSpread);
        } catch (IOException i) {
            Logger.log(Level.ERROR, i.toString());
        }
    }

    /* Creates necessary files for Maus to run. Including the directories for client + server setting data. */
    public void createMausData() throws IOException {
        final File parent = new File(System.getProperty("user.home") + "/Maus/clients");
        final File parent2 = new File(System.getProperty("user.home") + "/Maus/Client");
        if (!parent.mkdirs() && !parent2.mkdirs()) {
            Logger.log(Level.WARNING, "Unable to make necessary directories, may already exist.");
        }
        File settings = new File(System.getProperty("user.home") + "/Maus/.serverSettings");
        if (!settings.exists()) {
            settings.createNewFile();
        }
    }

    /* Loads .SapnilSettings file to Maus server settings*/
    private void loadServerSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Maus/.serverSettings"))
        ) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String[] settings = stringBuilder.toString().split(" ");
            if (settings.length == 6) {
                SapnilSettings.CONNECTION_IP = (settings[0].trim());
                SapnilSettings.SHOW_NOTIFICATIONS = (Boolean.valueOf(settings[1].trim()));
                SapnilSettings.BACKGROUND_PERSISTENT = (Boolean.valueOf(settings[2].trim()));
                SapnilSettings.MAX_CONNECTIONS = (Integer.parseInt(settings[3].trim()));
                SapnilSettings.PORT = (Integer.parseInt(settings[4].trim()));
                SapnilSettings.SOUND = (Boolean.valueOf(settings[5].trim()));
            }
            Logger.log(Level.INFO,"Maus server settings loaded.");
        } catch (IOException e) {
            Logger.log(Level.ERROR, e.toString());
        }
    }

    /* Loads data for server settings and from .client objects (serialized) and adds them to MausData & CONNECTIONS */
    public void loadData() throws IOException, ClassNotFoundException {
        loadServerSettings();
        File folder = new File(System.getProperty("user.home") + "/Maus/clients/");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String file = listOfFile.toString();
                if (file.contains(".client")) {
                    ClientObject o;
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    try {
                        o = (ClientObject) in.readObject();
                        mausData.put(o.getIP(), o);
                        in.close();
                    } catch (InvalidClassException e){
                        deleteMausData(System.getProperty("user.home") + "/Maus/clients/");
                    }
                    listOfFile.delete();
                }
            } else {
                break;
            }
        }
    }

    /* Deletes serialized client objects in the event that they become corrupted / out dated (mostly used for development)*/
    private void deleteMausData(String directory){
       File folder = new File(directory);
       File[] listOfFiles = folder.listFiles();
       assert listOfFiles != null;
       for (File listOfFile : listOfFiles) {
           if (listOfFile.isFile()) {
               String file = listOfFile.toString();
               if (file.contains(".client")) {
                   listOfFile.delete();
               }
           } else {
               break;
           }
       }
    }
}
