
package Functionality;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Guest {
    private String guestID;
    private String guestName;
    private String IC;
    private String nationality;
    private String email;
    private String contactNumber;
    private static final File file = new File("Guests.txt");
    private static ArrayList<Guest> listGuest = new ArrayList<>();
    private static ArrayList<Guest> searchGuest = new ArrayList<>();
    
    public Guest(String guestID, String guestName, String IC, String nationality, String email, String contactNumber) {
        this.guestID = guestID;
        this.guestName = guestName;
        this.IC = IC;
        this.nationality = nationality;
        this.email = email;
        this.contactNumber = contactNumber;
    }
    

    // Getter
    public String getGuestID() {
        return guestID;
    }
    
    public String getGuestName() {
        return guestName;
    }
    
    public String getIC() {
        return IC;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    // return an ArrayList of all Guest in file
    public static ArrayList<Guest> getListGuest() {
        listGuest.clear();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                String list[] = line.split(", ");
                Guest g = new Guest(list[0], list[1], list[2], list[3], list[4], list[5]);
                listGuest.add(g);
                }
            }
            sc.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Oops! File was not found", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
        return listGuest;
    }
    
    // Add Guest into file
    public static void addGuest(Guest guest) {  
        getListGuest();
        boolean exist = false;
        // Check existing Guest
        for (int i =0 ; i < listGuest.size(); i++) {
            if ((guest.getIC().equals(listGuest.get(i).getIC()))) {
                exist = true;
                break;
            }
        }
        // if no exist, write into file
        if (exist == false) {
            try {
                FileWriter fw = new FileWriter(file, true);
                fw.write(guest.guestID + ", " + guest.guestName + ", " + guest.IC + ", " + guest.nationality + ", " + guest.email + ", " + guest.contactNumber + "\n");
                fw.close();
                System.out.println("Guest added");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(ex);
                }
        } else System.out.println("Guest existed");
    }
     
    // Edit Guest Detail
    public static void editGuest(String guestID, String name, String IC, String nationality, String email, String contactNumber) {
        try {
            Scanner sc = new Scanner(file);
            File tempFile = new File("tempGuests.txt");
            File realFile = new File("Guests.txt");
            boolean changed = true;
            if (tempFile.createNewFile()) {
                FileWriter fw = new FileWriter(tempFile);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (!line.isEmpty()) {
                        String list[] = line.split(", ");
                        if (guestID.equals(list[0])) {
                            fw.write(guestID + ", " + name + ", " + IC + ", " + nationality + ", " + email + ", " + contactNumber + "\n");
                            changed = true;
                            } else {
                                fw.write(line + "\n");
                            }
                        }
                    }
                fw.close();
            }
            sc.close();
            if (changed = true) {
                if (realFile.delete()) {
                    tempFile.renameTo(realFile);
                    JOptionPane.showMessageDialog(null, "Edited Successfully", "Guest", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
    }
    
    // search Guest
    public static ArrayList<Guest> searchGuest(String nameIC) {
        getListGuest();
        searchGuest.clear();
        for (int i = 0; i < listGuest.size(); i++) {
            if (listGuest.get(i).getGuestName().startsWith(nameIC) || listGuest.get(i).getIC().startsWith(nameIC))
            searchGuest.add(listGuest.get(i));
        }
        
        return searchGuest;
    }
    
    public static String autoIncrementID() {
        getListGuest();
        int largest = 0;
        for (int i = 0; i < listGuest.size(); i++) {
            int current = Integer.parseInt(listGuest.get(i).getGuestID().substring(1));
            if (current > largest) {
                largest = current;
            }
        }
        int guest = largest + 1;
        String nextGuestID = "G" + String.format("%04d", (largest + 1));
        return nextGuestID;
    }
    
    public static void existingGuest(JTable table) {
        ArrayList<Guest> g = getListGuest();
        String column[] = {"Guest ID", "Name", "IC/Passport", "Contact Number"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        for (int i = 0; i < g.size(); i++) {
            String data[] = {g.get(i).getGuestID(), g.get(i).getGuestName(), g.get(i).getIC(), g.get(i).getContactNumber()};
            model.addRow(data);
        }
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
    }
    
    public static ArrayList<Guest> existingGuestClicked(JTable table) {
        
        ArrayList<Guest> g = getListGuest();
        ArrayList<Guest> selectedGuest = new ArrayList<>();
        int row = table.getSelectedRow();
        String id = table.getValueAt(row, 0).toString();
        
        for (int i = 0; i < g.size(); i++) {
            if (id.equals(g.get(i).getGuestID())) {
                selectedGuest.add(g.get(i));
            }
        }
        
        
        return selectedGuest;
    }
}
