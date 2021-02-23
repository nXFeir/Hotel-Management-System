
package Functionality;

import HotelUI.ViewInvoice;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Invoice {
    private String invoiceID;
    private LocalDate invoiceDate;
    private String bookingID;
    private static File file = new File("Invoice.txt");
    private static ArrayList<Invoice> listInvoice = new ArrayList<>();
    
    public Invoice(String invoiceID, LocalDate invoiceDate, String bookingID) {
        this.invoiceID = invoiceID;
        this.invoiceDate = invoiceDate;
        this.bookingID = bookingID;
    }
    
    // Getter
    public String getInvoiceID() {
        return invoiceID;
    }
    
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }
    
    public String getBookingID() {
        return bookingID;
    }
    
    // return an ArrayList of all Invoice in file
    public static ArrayList<Invoice> getListInvoice() {
        listInvoice.clear();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    String list[] = line.split(", ");
                    Invoice i = new Invoice(list[0], LocalDate.parse(list[1]), list[2]);
                    listInvoice.add(i);
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Oops! File was not found", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
        
        return listInvoice;
    }
    
    // Add invoice into file
    public static void addInvoice(Invoice invoice) {
        getListInvoice();
        boolean exist = false;
        // Check existing Invoice
        for (int i =0; i < listInvoice.size(); i++) {
            if (invoice.invoiceID.equals(listInvoice.get(i).getInvoiceID())) {
                exist = true;
                break;
            }
        }
        // If no existing, write into file
       if (exist == false) {
            try {
                FileWriter fw = new FileWriter(file, true);
                fw.write(invoice.invoiceID + ", " + invoice.invoiceDate + ", " + invoice.bookingID + "\n");
                fw.close();
                System.out.println("Invoice added");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(ex);
            }
        }
    }
    
    // AutoIncrement Invoice ID
    public static String autoIncrement() {
        getListInvoice();
        int largest = 0;
        for (int i = 0; i < listInvoice.size(); i++) {
            int current = Integer.parseInt(listInvoice.get(i).getInvoiceID().substring(1));
            if (current > largest) {
                largest = current;
            }
        }
        int booking = largest + 1;
        String nextInvoiceID = "I" + String.format("%04d", (largest + 1));
        return nextInvoiceID;
    }
    
    // Search Invoice based on Invoice ID or Invoice date
    public static void searchInvoice(JTable table, String id) {
        getListInvoice();
        String column[] = {"Invoice ID", "Invoice Date", "Booking ID"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        for (int i = 0; i < listInvoice.size(); i++) {
            if ((listInvoice.get(i).getInvoiceID().startsWith(id)) || (listInvoice.get(i).getBookingID().startsWith(id))) {
                String data[] = {listInvoice.get(i).getInvoiceID(), listInvoice.get(i).getInvoiceDate().toString(), listInvoice.get(i).getBookingID()};
                model.addRow(data);
            }
        }    
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
    }
    
    public static void refreshInvoicePage(JTable table) {
        ArrayList<Invoice> invoice = Invoice.getListInvoice();
        String column[] = {"Invoice ID", "Invoice Date", "Booking ID"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        for (int i = 0; i < invoice.size(); i++) {
            String[] list = {invoice.get(i).getInvoiceID(), invoice.get(i).getInvoiceDate().toString(), invoice.get(i).getBookingID()};
            model.addRow(list);
        }
        
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
    }
    
    public static void rowClicked(JTable table) {
        int row = table.getSelectedRow();
        String id = table.getValueAt(row, 0).toString();
        ArrayList<Invoice> invoiceList = getListInvoice();
        ArrayList<Booking> booking = Booking.getListBooking();
        ArrayList<Guest> guest = Guest.getListGuest();
        ArrayList<Invoice> editInvoiceList = new ArrayList<>();
        ArrayList<Booking> editBookingList = new ArrayList<>();
        ArrayList<Guest> editGuestList = new ArrayList<>();
        for (int i = 0 ; i < invoiceList.size(); i++) {
            if (id.equals(invoiceList.get(i).getInvoiceID())) {
                editInvoiceList.add(invoiceList.get(i));
                break;
            }
        }
        String bookingID = editInvoiceList.get(0).getBookingID();
        for (int i = 0; i < booking.size(); i++) {
            if (bookingID.equals(booking.get(i).getBookingID())) {
                editBookingList.add(booking.get(i));
                break;
            }
        }
        String guestID = editBookingList.get(0).getGuestID();
        for (int i = 0; i < guest.size(); i++) {
            if (guestID.equals(guest.get(i).getGuestID())) {
                editGuestList.add(guest.get(i));
                break;
            }
        }
        
        String roomNumber = editBookingList.get(0).getRoomNumber();
        LocalDate dateIn = editBookingList.get(0).getDateIn();
        LocalDate dateOut = editBookingList.get(0).getDateOut();
        int dayOfStay = editBookingList.get(0).getDayOfStay();
        String invoiceID = editInvoiceList.get(0).getInvoiceID();
        LocalDate invoiceDate = editInvoiceList.get(0).getInvoiceDate();
        String name = editGuestList.get(0).getGuestName();
        double amount = editBookingList.get(0).getAmount();
        
        
        ViewInvoice invoice = new ViewInvoice(roomNumber, dateIn, dateOut, dayOfStay, invoiceID, invoiceDate, name, amount);
        invoice.setVisible(true);
        invoice.pack();
        invoice.setLocationRelativeTo(null);
    }
    
}
