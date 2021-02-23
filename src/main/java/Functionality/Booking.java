
package Functionality;

import HotelUI.EditBooking;
import java.io.*;
import java.time.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;


public class Booking {
    private String bookingID;
    private String roomNumber;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private int dayOfStay;
    private String guestID;
    private double amount;
    private static ArrayList<Booking> listBooking = new ArrayList<>();
    private static ArrayList<String> availableRoom = new ArrayList<>();
    private static File file = new File("Bookings.txt");
    
    // Constructor to add booking
    public Booking(String bookingID, String roomNumber, LocalDate dateIn, LocalDate dateOut, int dayOfStay, String guestID, double amount) {
        this.bookingID = bookingID;
        this.roomNumber = roomNumber;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.dayOfStay = dayOfStay;
        this.guestID = guestID;
        this.amount = amount;
    }

    // Getter for each properties
    public String getBookingID() {
        return bookingID;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public LocalDate getDateIn() {
        return dateIn;
    }
    
    public LocalDate getDateOut() {
        return dateOut;
    }
    
    public int getDayOfStay() {
        return dayOfStay;
    }
    
    public String getGuestID() {
        return guestID;
    }
    
    public double getAmount() {
        return amount;
    }
    
    // return an ArrayList of all Booking in file
    public static ArrayList<Booking> getListBooking() {
        listBooking.clear();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                String booking[] = line.split(", ");
                Booking b = new Booking(booking[0], booking[1], LocalDate.parse(booking[2]), LocalDate.parse(booking[3]), Integer.parseInt(booking[4]), booking[5], Double.parseDouble(booking[6])); 
                listBooking.add(b);
                }
            }
            sc.close();
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Oops! File is not found", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        } 
        return listBooking;
    }
    
    // Add booking
    public static void addBooking(Booking booking) {
        getListBooking();
        boolean exist = false;
        for (int i = 0; i < listBooking.size(); i++) {
            if (booking.bookingID.equals(listBooking.get(i).getBookingID())) {
                exist = true;
            }
        }
        if (exist == false) {
            try {
                FileWriter fw = new FileWriter(file, true);
                fw.write(booking.bookingID + ", " + booking.roomNumber + ", " + booking.dateIn + ", " + booking.dateOut + ", " + booking.dayOfStay + ", " + booking.guestID + ", " + booking.amount + "\n");
                fw.close();
                System.out.println("Booking added");
                JOptionPane.showMessageDialog(null, "Booking Successfully", "Booking", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Booking record existed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // return ArrayList of available room based on dateIn dateOut
    // Loop through all Booking file to compare date
    // Get a Arraylist of unavailable room
    // Remove unavailable from available
    public static ArrayList<String> searchRoom(LocalDate dateIn, LocalDate dateOut) {
        ArrayList<String> unavailableRoom = new ArrayList<>();
        getListBooking();
        availableRoom.clear();
        for (int i = 0; i < listBooking.size(); i++) {
            if (((!dateOut.isBefore(listBooking.get(i).getDateIn())) 
                    && (!dateOut.isAfter(listBooking.get(i).getDateOut()))) 
                        || ((!dateIn.isBefore(listBooking.get(i).getDateIn())) 
                        && (!dateIn.isAfter(listBooking.get(i).getDateOut()))) 
                            || ((!dateIn.isAfter(listBooking.get(i).getDateIn())) 
                            && (!dateOut.isBefore(listBooking.get(i).getDateOut())))) {
                unavailableRoom.add(listBooking.get(i).getRoomNumber());
            } 
        }
        for (int i = 0; i < Room.getListRoom().size(); i++) {
            availableRoom.add(Room.getListRoom().get(i).getRoomNumber());
        } 
        
        availableRoom.removeAll(unavailableRoom);
        
        return availableRoom;
    }
    
    public static void searchBooking(JTable table, String id) {
        ArrayList<Booking> booking = getListBooking();
        ArrayList<Guest> g = Guest.getListGuest();
        String column[] = {"Booking ID", "Room Number", "Date In", "Date Out", "Day of Stay", "Guest Name", "Total Amount"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        for (int i = 0; i < booking.size(); i++) {
            String guestID = booking.get(i).getGuestID();
            String guestName = "" ;
            for (int count = 0; count < g.size(); count++) {
                if (guestID.equals(g.get(count).getGuestID())) {
                    guestName = g.get(count).getGuestName();
                    break;
                } 
            }
            if (booking.get(i).getBookingID().startsWith(id) || guestName.startsWith(id)) {
                String[] data = {booking.get(i).getBookingID(), booking.get(i).getRoomNumber(), booking.get(i).getDateIn().toString(), booking.get(i).getDateOut().toString(), String.valueOf(booking.get(i).getDayOfStay()), guestName, String.valueOf(booking.get(i).getAmount())};
                model.addRow(data);
            }
        }
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
    }
    
    // Auto Increment booking ID 
    public static String autoIncrement() {
        getListBooking();
        int largest = 0;
        for (int i = 0; i < listBooking.size(); i++) {
            int current = Integer.parseInt(listBooking.get(i).getBookingID().substring(1));
            if (current > largest) {
                largest = current;
            }
        }
        int booking = largest + 1;
        String nextBookingID = "B" + String.format("%04d", (largest + 1));
        return nextBookingID;
    }
    
    // Delete MAYBE
   public static void deleteBooking(String bookingID) {
       try {
            Scanner sc = new Scanner(file);
            File tempFile = new File("tempBooking.txt");
            File realFile = new File("Bookings.txt");
            if (tempFile.createNewFile()) {
                FileWriter fw = new FileWriter(tempFile);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (!line.isEmpty()) {
                        String list[] = line.split(", ");
                        if (!bookingID.equals(list[0])) {
                            fw.write(line + "\n");
                        } 
                    }
                }
                fw.close();
            }
            sc.close();
            if (realFile.delete()) {
                tempFile.renameTo(realFile);
                JOptionPane.showMessageDialog(null, "Record deleted successfully", "Booking", JOptionPane.INFORMATION_MESSAGE);
            }
 
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
   }
   
   
   // Edit Booking detail (basically change the whole booking)
   public static void editBooking(String bookingID, String roomNumber, LocalDate dateIn, LocalDate dateOut, int dayOfStay, String guestID, double amount ) {
        try {
            Scanner sc = new Scanner(file);
            File tempFile = new File("tempBooking.txt");
            File realFile = new File("Bookings.txt");
            boolean changed = false;
            if (tempFile.createNewFile()) {
                FileWriter fw = new FileWriter(tempFile);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (!line.isEmpty()) {
                        String list[] = line.split(", ");
                        if (bookingID.equals(list[0])) {
                            fw.write(bookingID + ", " + roomNumber + ", " + dateIn + ", " + dateOut + ", " + dayOfStay + ", " + guestID + ", " + amount + "\n");
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
                    JOptionPane.showMessageDialog(null, "Edited Successfully", "Booking", JOptionPane.INFORMATION_MESSAGE);
                }
            } 
 
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
       
   }
   public static void refreshManageBookingPage(JTable table) {
        ArrayList<Booking> booking = Booking.getListBooking();
        ArrayList<Guest> g = Guest.getListGuest();
        String column[] = {"Booking ID", "Room No", "Date In", "Date Out", "Day of Stay", "Guest Name", "Total Amount"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        for (int i = 0; i < booking.size(); i++) {
            String guestID = booking.get(i).getGuestID();
            String guestName = "";
            for (int count = 0; count < g.size(); count++) {
                if (guestID.equals(g.get(count).getGuestID()) ) {
                    guestName = g.get(count).getGuestName();
                    break;
                } 
            }
            String[] list = {booking.get(i).getBookingID(), booking.get(i).getRoomNumber(), booking.get(i).getDateIn().toString(), booking.get(i).getDateOut().toString(), String.valueOf(booking.get(i).getDayOfStay()), guestName, String.valueOf(booking.get(i).getAmount())};
            model.addRow(list);
        }
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
    }
   
   public static void refreshOverviewPage(JTable table) {
        ArrayList<Booking> booking = Booking.getListBooking();
        ArrayList<Guest> g = Guest.getListGuest();
        String column[] = {"Date In", "Date Out", "Guest Name", "Room Number"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        for (int i =0; i < booking.size(); i++) {
            
            if (!booking.get(i).getDateIn().isBefore(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                String guestID = booking.get(i).getGuestID();
                String guestName = "";
                for (int count = 0; count < g.size(); count++) {
                    if (guestID.equals(g.get(count).getGuestID()) ) {
                        guestName = g.get(count).getGuestName();
                    } 
                }
                String[] list = {booking.get(i).getDateIn().toString(), booking.get(i).getDateOut().toString(), guestName, booking.get(i).getRoomNumber()};
                model.addRow(list);
                }   
        }
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
   }
   
   public static void rowClicked(JTable table) {
       int row = table.getSelectedRow();
       String id = table.getValueAt(row, 0).toString();
       String guestID = "";
       ArrayList<Booking> booking = Booking.getListBooking();
       ArrayList<Guest> guest = Guest.getListGuest();
       ArrayList<Booking> selectedEdit = new ArrayList<>();
       ArrayList<Guest> selectedEditGuest = new ArrayList<>();
       for (int i = 0; i < booking.size(); i++) {
           if (id.equals(booking.get(i).getBookingID())) {
               selectedEdit.add(booking.get(i));
               guestID = booking.get(i).getGuestID();
               break;
           }
       }
       System.out.println(id + " " + guestID);
       for (int i = 0; i < guest.size(); i++) {
           if (guestID.equals(guest.get(i).getGuestID())) {
               selectedEditGuest.add(guest.get(i));
               break;
           }
       }
       
       String editbookingID = selectedEdit.get(0).getBookingID();
       String editroomNumber = selectedEdit.get(0).getRoomNumber();
       int editdayOfStay = selectedEdit.get(0).getDayOfStay();
       String editguestID = selectedEdit.get(0).getGuestID();
       String editguestName = selectedEditGuest.get(0).getGuestName();
       String editguestIC = selectedEditGuest.get(0).getIC();
       String editnationality = selectedEditGuest.get(0).getNationality();
       String editemail = selectedEditGuest.get(0).getEmail();
       String editcontactNumber = selectedEditGuest.get(0).getContactNumber();
       LocalDate dateIn = selectedEdit.get(0).getDateIn();
       LocalDate dateOut = selectedEdit.get(0).getDateOut();
       double totalAmount = selectedEdit.get(0).getAmount();
       
       EditBooking editBooking = new EditBooking(editbookingID, editroomNumber, editdayOfStay, editguestID, editguestName, editguestIC, editnationality, editemail, editcontactNumber, dateIn, dateOut, totalAmount);
       editBooking.setVisible(true);
       editBooking.pack();
       editBooking.setLocationRelativeTo(null);
       }
   
   public static void delete(JTable table) {
       int row = table.getSelectedRow();
       String id = table.getValueAt(row, 0).toString();
       deleteBooking(id);
   }
}
       

