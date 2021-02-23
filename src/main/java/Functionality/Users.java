
package Functionality;

import HotelUI.*;
import java.io.*;
import java.util.*;

import javax.swing.*;


public class Users {
    private String Name;
    private String Password;
    private String ID ;
    private static ArrayList<Users> listUsers = new ArrayList<>();
    private static File file = new File("Users.txt");

    public Users(String ID, String Password, String Name) {
        this.Name = Name;
        this.Password = Password;
        this.ID = ID;
    }
    
    // Getter
    public String getName() {
        return Name;
    }
    
    public String getPassword() {
        return Password;
    }
    
    public String getID() {
        return ID;
    }
    
   // return ArrayList of all users in file
    public static ArrayList<Users> getListUsers() {
        listUsers.clear();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                String list[] = line.split(", ");
                Users u = new Users(list[0], list[1], list[2]);
                listUsers.add(u);
                }
            } 
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Oops! File was not found", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex);
        }
        return listUsers;
    }
    
    // Add users to file
    public void addUsers(Users users) {
        getListUsers();
        boolean exist = false;
        // Check existing user
        for (int i =0 ; i < listUsers.size(); i++) {
            if (ID.equals(listUsers.get(i).getID())) {
                exist = true;
                break;
            }
        }
        // if exist, error
        if (exist == true) {
            JOptionPane.showMessageDialog(null, "User existed", "Error", JOptionPane.ERROR_MESSAGE);
            MainPage.register.getTxtStaffId().setText("");
            MainPage.register.getTxtStaffName().setText("");
            MainPage.register.getPswPsw().setText("");
            MainPage.register.getTxtStaffId().requestFocus();
            } 
        // if not exist, write into file
        else if (exist == false) {
            try {
                FileWriter fw = new FileWriter(file, true);
                fw.write(ID + ", " + Password + ", " + Name + "\n");
                fw.close();
                System.out.println("Users added");
                JOptionPane.showMessageDialog(null, "Register Successfully", "Register", JOptionPane.INFORMATION_MESSAGE);
                MainPage.register.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Oops! Something wrong with your file", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(ex);
        }

    }
}
    
    public void login() {
        getListUsers();
        boolean exist = false;
        // Check existing user
        for (int i = 0; i < listUsers.size(); i ++) {
            if (ID.equals(listUsers.get(i).getID()) && Password.equals(listUsers.get(i).getPassword())) {
                Name = listUsers.get(i).getName();
                exist = true;
                break;
            }
        }
        // if not exist, error
        if (exist == false) {
            JOptionPane.showMessageDialog(null, "Invalid Staff ID or Password!");
            Home.home.getTextField().setText("");
            Home.home.getPasswordField().setText("");
            Home.home.getTextField().requestFocus();

        }
        // if exist, login
        else if (exist == true) {
            Home.home.setVisible(false);
            MainPage mainpage = new MainPage(ID, Name);
            mainpage.setVisible(true);
            mainpage.pack();
            mainpage.setLocationRelativeTo(null);
        }
    }
}
