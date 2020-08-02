/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.fileaccess;

import edu.ijse.mailingsystem.dto.MailDTO;
import edu.ijse.mailingsystem.dto.UserDTO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author dulan
 */
public class UserFileAccess {

    private static final File allUsersFileInProject = new File(".//src/edu//ijse//mailingsystem//file//AllUsers.txt");
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public UserFileAccess() {
    }

    public boolean addUser(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        if (!allUsersFileInProject.exists()) {
            boolean createNewFile = allUsersFileInProject.createNewFile();
            System.out.println("File Created");
        }
        String id = getUserId();
        String userID = "UI" + id;

//        /media/dulan/New Volume/IJSE/Project/Second Sem Project/MailingSystemServer/src/edu/ijse/mailingsystem
        String writingData = userID + "#";
        writingData += user.getUserName() + "#";
        writingData += user.getFirstName() + "#";
        writingData += user.getLastName() + "#";
        writingData += user.getUserPassword() + "#";
        writingData += user.getUserType();

        boolean isLocationCreated = createUserLocation(userID, user.getUserName());
        if (isLocationCreated) {
            boolean isFoldersCreated = createSentInboxDraftTrashFolders(userID, user.getUserName());
            if (isFoldersCreated) {
                String directoryName = userID + "@" + user.getUserName();
                File allDetails = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/AllDetails.txt");

                if (!allDetails.exists()) {
                    allDetails.createNewFile();
                }

                boolean isFilesCreated = createSentInboxDraftTrashFiles(userID, user.getUserName());
                if (isFilesCreated || allDetails.exists()) {
                    BufferedWriter writeInFile = null;
                    try {
                        rwLock.writeLock().lock();
                        FileWriter fileWriter = new FileWriter(allUsersFileInProject, true);
                        writeInFile = new BufferedWriter(fileWriter);
                        writeInFile.write(writingData);
                        writeInFile.newLine();
                        System.out.println(writingData);
//                        JOptionPane.showMessageDialog(null, "Welcome to the Hell... ");
                        System.out.println("user added successfully...");
                        return true;
                    } finally {
                        if (writeInFile != null) {
                            writeInFile.close();
                        }
                        rwLock.writeLock().unlock();
                    }

                } else {
                    System.out.println("user not added...");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "user folders is not created yet...");
                System.out.println("user folders is not created yet...");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "user location is not created yet...");
            System.out.println("user location is not created yet...");
            return false;
        }

    }

    private boolean createUserLocation(String userID, String userName) {    //for addUser()
        String directoryName = userID + "@" + userName;
//E://e-Pal Database//inbox
//D:\IJSE\Project\Second Sem Project\FileSavingFolder
        File makeDirectory = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName);
        if (makeDirectory.exists()) {
            return false;
        } else {
            makeDirectory.mkdir();
            return true;
        }
    }

    private boolean createSentInboxDraftTrashFolders(String userID, String userName) {    //for addUser()
        String directoryName = userID + "@" + userName;

        String sentFolderName = userID + "@" + userName + "@Sent";
        String inboxFolderName = userID + "@" + userName + "@Inbox";
        String draftFolderName = userID + "@" + userName + "@Draft";
        String trashFolderName = userID + "@" + userName + "@Trash";

        File makeAttachment = new File("D:/IJSE/Project/Second Sem Project/AttachmentSavingFolder/" + directoryName);
        File makeSent = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + sentFolderName);
        File makeInbox = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + inboxFolderName);
        File makeDraft = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + draftFolderName);
        File makeTrash = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + trashFolderName);

        if (makeSent.exists() || makeInbox.exists() || makeDraft.exists() || makeTrash.exists() || makeAttachment.exists()) {
            return false;
        } else {
            makeAttachment.mkdir();
            makeSent.mkdir();
            makeInbox.mkdir();
            makeDraft.mkdir();
            makeTrash.mkdir();
            return true;
        }
    }

    private boolean createSentInboxDraftTrashFiles(String userID, String userName) throws IOException { //for addUser()
        String directoryName = userID + "@" + userName;

        String sentFolderName = userID + "@" + userName + "@Sent";
        String inboxFolderName = userID + "@" + userName + "@Inbox";
        String draftFolderName = userID + "@" + userName + "@Draft";
        String trashFolderName = userID + "@" + userName + "@Trash";

        String sentFileName = userID + "@" + userName + "@SentFile.txt";
        String inboxFileName = userID + "@" + userName + "@InboxFile.txt";
        String draftFileName = userID + "@" + userName + "@DraftFile.txt";
        String trashFileName = userID + "@" + userName + "@TrashFile.txt";

        File sentFile = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + sentFolderName + "/" + sentFileName);
        File inboxFile = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + inboxFolderName + "/" + inboxFileName);
        File draftFile = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + draftFolderName + "/" + draftFileName);
        File trashFile = new File("D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + directoryName + "/" + trashFolderName + "/" + trashFileName);

        if (sentFile.exists() || inboxFile.exists() || draftFile.exists() || trashFile.exists()) {
            return false;
        } else {
            sentFile.createNewFile();
            inboxFile.createNewFile();
            draftFile.createNewFile();
            trashFile.createNewFile();
            return true;
        }

    }

    public boolean userNameAvailability(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        if (!allUsersFileInProject.exists()) {
            boolean createNewFile = allUsersFileInProject.createNewFile();
            System.out.println("File Created");
        }

        if (allUsersFileInProject.exists()) {
            FileReader fileReader = new FileReader(allUsersFileInProject);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            ArrayList<String> allUserNames = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitedLine = line.split("#");
                String name = splitedLine[1];
                System.out.println(name);
                allUserNames.add(name);
            }

            boolean availability = true;
            if (allUserNames.contains(userName)) {
                availability = false;
            }

//        for (String userNameInFile : allUserNames) {
//            if (userNameInFile.equals(userName)) {
//                availability = false;
//            } 
//        }
            System.out.println(availability);
            return availability;

        } else {
            return true;
        }
    }

    public boolean SignIn(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        FileReader fileReader = new FileReader(allUsersFileInProject);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        ArrayList<UserDTO> allUsersInFile = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            String[] userAll = line.split("#");
            String userName = userAll[1];
            String userPassword = userAll[4];

            if ((userName.equals(user.getUserName())) && (userPassword.equals(user.getUserPassword()))) {
                allUsersInFile.add(new UserDTO(userName, userPassword));
                return true;
            }
        }

        if (!allUsersInFile.contains(user.getUserName())) {
//            JOptionPane.showMessageDialog(null, "Ehema user kenek nee bola... UserDTO Name eka apahu balapan...");
            return false;
        }
        return false;

//        return true;
    }

    public UserDTO searchUser(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        FileReader fileReader = new FileReader(allUsersFileInProject);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        String userID=
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineToArray = line.split("#");
            if (lineToArray[1].equals(userName)) {
                return new UserDTO(lineToArray[0], lineToArray[1], lineToArray[2], lineToArray[3], lineToArray[4], lineToArray[5]);
            }
        }
        return null;
    }

    public boolean modifyUser(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        System.out.println("0");
        FileReader fileReader = new FileReader(allUsersFileInProject);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int count = 0;
        String line;
        String dataa;
        ArrayList<String> writingData = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println("1");
            String[] lineToArray = line.split("#");
            if (lineToArray[1].equals(user.getUserName())) {
                System.out.println("2");
                dataa = user.getUserID() + "#" + user.getUserName() + "#" + user.getFirstName() + "#" + user.getLastName() + "#" + user.getUserPassword() + "#" + user.getUserType();
                System.out.println(dataa);
                writingData.add(dataa);
                count++;
            } else {
                System.out.println("3");
                dataa = lineToArray[0] + "#" + lineToArray[1] + "#" + lineToArray[2] + "#" + lineToArray[3] + "#" + lineToArray[4] + "#" + lineToArray[5];
                writingData.add(dataa);
                count++;
            }
        }

        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        if (allUsersFileInProject.exists()) {
            PrintWriter printWriter = new PrintWriter(allUsersFileInProject);
            printWriter.close();
//            try {
//                for (String writeData : writingData) {
//                    System.out.println(writeData);
//                    fileWriter = new FileWriter(allUsersFileInProject, true);
//                    bufferedWriter = new BufferedWriter(fileWriter);
//                    bufferedWriter.write(writeData);
//                    bufferedWriter.newLine();
//                }
//            } finally {
//                if (bufferedWriter != null) {
//                    bufferedWriter.close();
//                }
//            }
            for (String write : writingData) {
                try {
                    rwLock.writeLock().lock();
                    fileWriter = new FileWriter(allUsersFileInProject, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(write);
                    System.out.println(write);
                    bufferedWriter.newLine();
                } finally {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    rwLock.writeLock().unlock();
                }
            }

            return true;
        }
        return false;
    }

    public ArrayList<UserDTO> getAllUsers() throws IOException, RemoteException, MalformedURLException, NotBoundException {
        FileReader fileReader = new FileReader(allUsersFileInProject);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int count = 0;
        String line;

        ArrayList<UserDTO> allUsers = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            String[] lineToArray = line.split("#");
            allUsers.add(new UserDTO(lineToArray[1]));
        }
        return allUsers;
    }

    private String getUserId() {
        String userID = "";
        try {
            FileReader fileReader = new FileReader(allUsersFileInProject);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
            }

            count += 1;
            if (count < 10) {
                userID = "000" + count;
                return userID;
            } else if (count < 100) {
                userID = "00" + count;
                return userID;
            } else if (count < 100) {
                userID = "0" + count;
                return userID;
            } else {
                userID = "" + count;
                return userID;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserFileAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserFileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userID;
    }

}
