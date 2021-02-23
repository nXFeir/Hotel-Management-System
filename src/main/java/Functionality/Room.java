
package Functionality;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JOptionPane;


public class Room {
    private String roomNumber;
    private double price;
    private static File file = new File("Rooms.txt");
    private static ArrayList<Room> listRoom = new ArrayList<>();
    
    public Room(String roomNumber, double price) {
        this.roomNumber = roomNumber;
        this.price = price;
    }   
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public double getPrice() {
        return price;
    }
    
    // return an ArrayList of all Room in file
    public static ArrayList<Room> getListRoom() {
        listRoom.clear();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                String list[] = line.split(", ");
                Room r = new Room(list[0], Double.parseDouble(list[1]));
                listRoom.add(r);
                }
            }
            sc.close();
        } catch (FileNotFoundException ex) { 
            JOptionPane.showMessageDialog(null, "Oops! File was not found", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
          return listRoom;
    }
}
