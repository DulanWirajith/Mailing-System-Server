/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.fileaccess;

import edu.ijse.mailingsystem.dto.MailDTO;
import edu.ijse.mailingsystem.dto.UserDTO;
import edu.ijse.mailingsystem.util.AttachmentController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javax.imageio.ImageIO;

/**
 *
 * @author dulan
 */
public class MailFileAccess {

    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final File allUserFile = new File(".//src/edu//ijse//mailingsystem//file//AllUsers.txt");
    private static final File allMailFile = new File(".//src/edu//ijse//mailingsystem//file//AllMails.txt");

    String mailID;
    String senderName;
    String[] receiversNames;
    String mailType;

    public boolean sendMail(MailDTO mail) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        this.senderName = mail.getSenderName();
        System.out.println(senderName);
        String id = getMailID();
        System.out.println(id);
        String mailid = "M" + id;
        System.out.println(mailid);

        if (!allMailFile.exists()) {
            allMailFile.createNewFile();
            BufferedWriter bufferedWriter = null;
            try {
                rwLock.writeLock().lock();
                FileWriter fileWriter = new FileWriter(allMailFile, true);
                bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write("0000");
                bufferedWriter.newLine();
            } finally {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                rwLock.writeLock().unlock();
            }
        } else {
            BufferedWriter bufferedWriter = null;
            try {
                rwLock.readLock().lock();
                FileWriter fileWriter = new FileWriter(allMailFile, true);
                bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(id);
                bufferedWriter.newLine();
            } finally {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                rwLock.readLock().unlock();
            }
        }

        this.receiversNames = mail.getReceiversNames();
        this.mailType = "Sent Mail";
        String mailContent = mail.getMailContent();
        String mailTopic = mail.getMailTopic();
        String attachmentName = mail.getAttachmentName();
        System.out.println("0");
        String attachmentPath = mail.getAttachmentPath();
        System.out.println("1");
        System.out.println(attachmentPath);

        String newAttachmentPath;
        String attachmentSavingPath;
        String senderId = null;
        if ((attachmentName != null) && (attachmentPath != null)) {
            senderId = getUserID(senderName);
//            D:\IJSE\Project\Second Sem Project\AttachmentSavingFolder
            attachmentSavingPath = "D:/IJSE/Project/Second Sem Project/AttachmentSavingFolder/" + senderId + "@" + senderName;
            newAttachmentPath = attachmentSavingPath;
        } else {
            newAttachmentPath = attachmentPath;
            attachmentSavingPath = null;
        }

        boolean isWriteInReceiveFiles = writeInReceiversFiles(mail, newAttachmentPath, mailid);
        if (isWriteInReceiveFiles) {

            if ((!attachmentName.equals("")) && (!attachmentPath.equals(""))) {
//                boolean isSaved = saveAttachment(mailID, mailType, mail.getAttachmentInByteArray(), attachmentSavingPath);
                boolean isSaved = saveAttachment(mailid, mailType, mail.getAttachmentInByteArray(), attachmentSavingPath);
                if (isSaved) {
//                    boolean isFilesWrited = writeInSendersFiles(mail);
                    String mailSentDate = mail.getMailSentDate();
                    String mailSentTime = mail.getMailSentTime();

                    FileWriter fileWriter;
                    BufferedWriter bufferedWriter = null;
                    String receiverName = null;
                    String mainFileData;
                    String sentFileData;
                    String receivers = "";
                    for (String receiversName : receiversNames) {
                        receivers += receiversName + ",";
                    }
                    System.out.println(receivers);
                    String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + senderId + "@" + senderName;
//                  D:\IJSE\Project\Second Sem Project\FileSavingFolder\UI00001@freedom_fighter
                    String mainFilePath = filePath + "/AllDetails.txt";
                    File file = new File(mainFilePath);
                    String writingData = mailid + "#" + "Sent Mail" + "#" + receivers + "#" + mailSentDate;
                    try {
                        rwLock.readLock().lock();
                        fileWriter = new FileWriter(file, true);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(writingData);
                        bufferedWriter.newLine();
                    } finally {
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        rwLock.readLock().unlock();
                    }
                    String sendFilePath = filePath + "/" + senderId + "@" + senderName + "@Sent/" + senderId + "@" + senderName + "@SentFile.txt";
                    file = new File(sendFilePath);
                    writingData = mailid + "#" + senderName + "#" + receivers + "#" + mailContent + "#" + mailTopic + "#" + attachmentName + "#" + attachmentPath + "#" + mailSentDate + "#" + mailSentTime;

                    try {
                        rwLock.readLock().lock();
                        fileWriter = new FileWriter(file, true);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(writingData);
                        bufferedWriter.newLine();
                    } finally {
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        rwLock.readLock().unlock();
                    }
                    System.out.println("files writed successfully with attachment");
                    return true;
                } else {
                    System.out.println("files not writed successfully with attachment");
                    return false;
                }
            } else {
//                boolean isFilesWrited = writeInSendersFiles(mail);
                String mailSentDate = mail.getMailSentDate();
                String mailSentTime = mail.getMailSentTime();

                FileWriter fileWriter;
                BufferedWriter bufferedWriter = null;
                String receiverName = null;
                String mainFileData;
                String sentFileData;
                String receivers = null;
                for (String receiversName : receiversNames) {
                    receivers = receiversName + ",";
                }
                String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + senderId + "@" + senderName;
//                  D:\IJSE\Project\Second Sem Project\FileSavingFolder\UI00001@freedom_fighter
                String mainFilePath = filePath + "/AllDetails.txt";
                File file = new File(mainFilePath);
                String writingData = mailid + "#" + mailType + "#" + receivers + "#" + mailSentDate;
                try {
                    rwLock.readLock().lock();
                    fileWriter = new FileWriter(file, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(writingData);
                    bufferedWriter.newLine();
                } finally {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    rwLock.readLock().unlock();
                }
                String sendFilePath = filePath + "/" + senderId + "@" + senderName + "@Sent/" + senderId + "@" + senderName + "@SentFile.txt";
                file = new File(sendFilePath);
                writingData = mailid + "#" + senderName + "#" + receivers + "#" + mailContent + "#" + mailTopic + "#" + attachmentName + "#" + attachmentPath + "#" + mailSentDate + "#" + mailSentTime;

                try {
                    rwLock.readLock().lock();
                    fileWriter = new FileWriter(file, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(writingData);
                    bufferedWriter.newLine();
                } finally {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    rwLock.readLock().unlock();
                }
//                if (String line!=new BufferedReader) {
//                    
//                }
                System.out.println("files writed successfully without attachment");
                return true;

            }

        } else {
            System.out.println("error in writing receivers files");
            return false;
        }
    }

    public boolean writeInReceiversFiles(MailDTO mail, String newAttachmentPath, String mailid) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        this.mailID = mailid;
        this.senderName = mail.getSenderName();
        this.receiversNames = mail.getReceiversNames();
        this.mailType = "Received Mail";
        String mailContent = mail.getMailContent();
        String mailTopic = mail.getMailTopic();
        String attachmentName = mail.getAttachmentName();
        String attachmentPath = newAttachmentPath + "/" + mail.getMailID();//methanata daanna ona wenne attachment eka save wela thiyene thena.a kiyanne sender ge attachment eka save wela thiyena thena

        String mailSentDate = mail.getMailSentDate();
        String mailSentTime = mail.getMailSentTime();
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        String receiverName = null;
        String mainFileData;
        String receiveFileData;
        boolean checkWriting = false;
        for (String receiversName : receiversNames) {
            String receiverId = getUserID(receiversName);
            receiverName = receiversName;
            String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + receiverId + "@" + receiverName;

            String mainFilePath = filePath + "/AllDetails.txt";
            File mainFile = new File(mainFilePath);

            mainFileData = mailID + "#" + senderName + "#" + mailType + "#" + mail.getMailSentDate();

//write in AllDetails file
            try {
                rwLock.readLock().lock();
                fileWriter = new FileWriter(mainFile, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(mainFileData);
                bufferedWriter.newLine();
            } finally {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                rwLock.readLock().unlock();
            }
//            boolean mainFileWriteOrNot = mailWriteOrNot(mainFilePath, mailID, senderName);
//
//            if (mainFileWriteOrNot) {
//write In Receive file
            String receiveFilePath = filePath + "/" + receiverId + "@" + receiverName + "@Inbox/" + receiverId + "@" + receiverName + "@InboxFile.txt";
            File receiveFile = new File(receiveFilePath);
            receiveFileData = mailID + "#" + senderName + "#" + receiverName + "#" + mail.getMailContent() + "#" + mail.getMailTopic() + "#" + mail.getAttachmentName() + "#" + attachmentPath + "#" + mail.getMailSentDate() + "#" + mail.getMailSentTime() + "#NewMail";
            try {
                rwLock.readLock().lock();
                fileWriter = new FileWriter(receiveFile, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(receiveFileData);
                bufferedWriter.newLine();
            } finally {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                rwLock.readLock().lock();
            }
//                boolean receiveFileriteOrNot = mailWriteOrNot(receiveFilePath, mailID, senderName);
//                if (receiveFileriteOrNot) {
//                    checkWriting = true;
//                    System.out.println(receiverName + " Receive file is OK");
//                } else {
//                    System.out.println("Receive file eke write une nee bn...");
//                    return false;
//                }
//                System.out.println(receiverName + " AllDetailFile is OK");
//            } else {
//                System.out.println("AllDetail file eke write une nee bn...");
//                return false;
//            }

        }
        return true;
    }

    private String getUserID(String userName) throws FileNotFoundException, IOException {
        FileReader fileReader = new FileReader(allUserFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<UserDTO> allUsers = new ArrayList<>();
        String line = null;
        String receiverID = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] userAll = line.split("#");
            String userID = userAll[0];
            String userNamee = userAll[1];
            String firstName = userAll[2];
            String lastName = userAll[3];
            String userPassword = userAll[4];
            String userType = userAll[5];
            allUsers.add(new UserDTO(userID, userNamee, firstName, lastName, userPassword, userType));
        }
        for (UserDTO user : allUsers) {
            if (user.getUserName().equals(userName)) {
                receiverID = user.getUserID();
            }
        }
        return receiverID;
    }

    private boolean mailWriteOrNot(String filePath, String mailID, String senderName) throws FileNotFoundException, IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineDetails = line.split("#");
            if (lineDetails[0].equals(mailID)) {
                if (lineDetails[1].equals(senderName)) {
                    return true;
                }
            }
        }
        return false;
    }

//    private boolean saveAttachment(String mailID, String mailType, byte[] attachmentInByteArray, String attachmentSavingPath) throws IOException {
//        File fileSavingFolder = new File(attachmentSavingPath);
//        if (!fileSavingFolder.exists()) {
//            fileSavingFolder.mkdir();
//        }
//        File attachmentSavingFile = new File(attachmentSavingPath + "/" + mailID);
//        if (!attachmentSavingFile.exists()) {
//            attachmentSavingFile.createNewFile();
//        }
//
//        /*      attachment eka ena file eka apahu copy ekak ganna thmai me code eka gehuwe. lilantha sir qwa byte array ekakin file eka genna ganna kiyala
//        eka nisa me code tika wedak nee
//        
//        FileReader fileReader = new FileReader(attachmentFile);
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
////        String file=bufferedReader.read();
//        BufferedWriter bufferedWriter = null;
//        String line;
//        try {
//            FileWriter fileWriter = new FileWriter(attachmentSavingFile);
//            bufferedWriter = new BufferedWriter(fileWriter);
//
//            while ((line = bufferedReader.readLine()) != null) {
//                bufferedWriter.write(line);
//            }
//        } finally {
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//        }
//         */
//        FileInputStream fileInputStream = null;
//                fileInputStream.read(attachmentInByteArray);
////
//        File savingFile = new File(".//src//edu//ijse//mailingsystem//util//attachmentSavingFile");
//        if (savingFile.exists()) {
//            savingFile.delete();
//            savingFile.createNewFile();
//        } else {
//            savingFile.createNewFile();
//        }
//
//        FileOutputStream fileOutputStream = new FileOutputStream(savingFile);
//        fileOutputStream.write(fileToByteArray);
//        fileOutputStream.flush();
//        fileInputStream.close();
//        fileOutputStream.close();
//
////        to confirm file write or not
//        fileReader = new FileReader(attachmentSavingFile);
//        bufferedReader = new BufferedReader(fileReader);
//        if ((line = bufferedReader.readLine()) != null) {
//            System.out.println("attachment saved successfully...");
//            return true;
//        } else {
//            System.out.println("attachment eke aulak.. poddak balapan..");
//            return false;
//        }
////        Image image = null;
////        File f = new File("");
////        ImageIO.write(image, "jpeg", output)
////        
//    }
    private boolean saveAttachment(String mailID, String mailType, byte[] attachmentFile, String attachmentSavingPath) throws IOException {
        File fileSavingFolder = new File(attachmentSavingPath);
        if (!fileSavingFolder.exists()) {
            fileSavingFolder.mkdir();
        }
        File attachmentSavingFile = new File(attachmentSavingPath + "/" + mailID);
        if (!attachmentSavingFile.exists()) {
            attachmentSavingFile.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(attachmentSavingFile);
        fileOutputStream.write(attachmentFile);
        fileOutputStream.close();
//        return true;
//        FileReader fileReader = new FileReader(attachmentFile);
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
////        String file=bufferedReader.read();
//        BufferedWriter bufferedWriter = null;
//        String line;
//        try {
//            FileWriter fileWriter = new FileWriter(attachmentSavingFile);
//            bufferedWriter = new BufferedWriter(fileWriter);
//
//            while ((line = bufferedReader.readLine()) != null) {
//                bufferedWriter.write(line);
//            }
//        } finally {
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//        }
////        to confirm file write or not
        FileReader fileReader;
        fileReader = new FileReader(attachmentSavingFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        if ((line = bufferedReader.readLine()) != null) {
            System.out.println("attachment saved successfully...");
            return true;
        } else {
            System.out.println("attachment eke aulak.. poddak balapan..");
            return false;
        }
////        Image image = null;
////        File f = new File("");
////        ImageIO.write(image, "jpeg", output)
////        
    }

    private boolean writeInSendersFiles(MailDTO mail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean saveAsDraft(MailDTO mail) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        this.senderName = mail.getSenderName();
        System.out.println(senderName);
        this.mailID = "ASD";
        this.receiversNames = mail.getReceiversNames();
        this.mailType = "Draft Mail";

        String senderId = getUserID(senderName);
        String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + senderId + "@" + senderName;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        String receiverName = null;
        String mainFileData;
        String sentFileData;
        String receivers = "";
        for (String receiversName : receiversNames) {
            receivers += receiversName + ",";
        }
        System.out.println(receivers);

        String mainFilePath = filePath + "/AllDetails.txt";
        File file = new File(mainFilePath);
        String writingData = mailID + "#" + "Draft Mail" + "#" + receivers + "#" + mail.getMailSentDate();
        try {
            rwLock.readLock().lock();
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(writingData);
            bufferedWriter.newLine();
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            rwLock.readLock().unlock();
        }
        String draftFilePath = filePath + "/" + senderId + "@" + senderName + "@Draft/" + senderId + "@" + senderName + "@DraftFile.txt";
        file = new File(draftFilePath);
        writingData = mailID + "#" + senderName + "#" + receivers + "#" + mail.getMailContent() + "#" + mail.getMailTopic() + "#" + mail.getAttachmentName() + "#" + mail.getAttachmentPath() + "#" + mail.getMailSentDate() + "#" + mail.getMailSentTime();

        try {
            rwLock.readLock().lock();
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(writingData);
            bufferedWriter.newLine();
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            rwLock.readLock().unlock();
        }
        return true;
    }

    public ArrayList<MailDTO> viewAllMails(String userName, String folderName, String textFileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        String senderId = getUserID(userName);
        String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + senderId + "@" + userName;
        String mainFilePath = filePath + "/" + senderId + "@" + userName + "@" + folderName + "/" + senderId + "@" + userName + "@" + textFileName;
        System.out.println(mainFilePath);
        File file = new File(mainFilePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        ArrayList<MailDTO> allMailsToOneSide = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            String[] allDetail = line.split("#");
            System.out.println(allDetail.length);
            mailID = allDetail[0];
            System.out.println(mailID);
            senderName = allDetail[1];

            String receiverNames = allDetail[2];
            String mailContent = allDetail[3];
            String mailTopic = allDetail[4];
            String attachmentName = allDetail[5];
            String attachmentPath = allDetail[6];
            System.out.println(mailID);
            System.out.println(senderName);
            System.out.println(receiverNames);
            System.out.println(mailContent);
            System.out.println(mailTopic);
            System.out.println(attachmentName);
            System.out.println(attachmentPath);
            String mailSentDate = allDetail[7];
            String mailSentTime = allDetail[8];

            System.out.println(mailSentDate);
            allMailsToOneSide.add(new MailDTO(mailID, senderName, receiverNames, mailContent, mailTopic, attachmentName, attachmentPath, mailSentDate, mailSentTime));
        }
        return allMailsToOneSide;
    }

    public ArrayList<MailDTO> viewAllMailsToInbox(String userName, String folderName, String textFileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        String senderId = getUserID(userName);
        String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + senderId + "@" + userName;
        String mainFilePath = filePath + "/" + senderId + "@" + userName + "@" + folderName + "/" + senderId + "@" + userName + "@" + textFileName;
        System.out.println(mainFilePath);
        File file = new File(mainFilePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        ArrayList<MailDTO> allMailsToOneSide = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            String[] allDetail = line.split("#");
            System.out.println(allDetail.length);
            mailID = allDetail[0];
            senderName = allDetail[1];
            String receiverNames = allDetail[2];
            String mailContent = allDetail[3];
            String mailTopic = allDetail[4];
            String attachmentName = allDetail[5];
            String attachmentPath = allDetail[6];
            System.out.println(mailID);
            System.out.println(senderName);
            System.out.println(receiverNames);
            System.out.println(mailContent);
            System.out.println(mailTopic);
            System.out.println(attachmentName);
            System.out.println(attachmentPath);
            String mailSentDate = allDetail[7];
            String mailSentTime = allDetail[8];
            String ifNewMail = allDetail[9];

            System.out.println(mailSentDate);
            allMailsToOneSide.add(new MailDTO(mailID, senderName, receiverNames, mailContent, mailTopic, attachmentName, attachmentPath, mailSentDate, mailSentTime, ifNewMail));
        }
        return allMailsToOneSide;
    }

    public boolean saveReceivedAttachment(String fileSavingPath, String fileSavedPath, String fileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        byte[] fileToByteArray = AttachmentController.saveAttachment(fileSavedPath);

        File attachmentSavingFile = new File(fileSavingPath + "/" + fileName);
        if (!attachmentSavingFile.exists()) {
            attachmentSavingFile.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(attachmentSavingFile);
        fileOutputStream.write(fileToByteArray);
        fileOutputStream.close();

        FileReader fileReader;
        fileReader = new FileReader(attachmentSavingFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        if ((line = bufferedReader.readLine()) != null) {
            System.out.println("Received attachment saved successfully...");
            return true;
        } else {
            System.out.println("Received attachment eke aulak.. poddak balapan..");
            return false;
        }
    }

    public boolean viewCurrentMail(String userName, String mailId) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        System.out.println("in View Current Mail...");
        System.out.println(mailId);
        String userID = getUserID(userName);
        String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + userID + "@" + userName;
        String inboxreceiveFilePath = filePath + "/" + userID + "@" + userName + "@Inbox/" + userID + "@" + userName + "@InboxFile.txt";

        ArrayList<String> writer = new ArrayList<>();

        File file = new File(inboxreceiveFilePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        String writingData;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineToArray = line.split("#");
            System.out.println("lineToArra" + lineToArray[0]);
            if (lineToArray[0].equals(mailId)) {
                System.out.println("mailId" + mailId);
                System.out.println("lineToArra" + lineToArray[0]);
                writingData = lineToArray[0] + "#" + lineToArray[1] + "#" + lineToArray[2] + "#" + lineToArray[3] + "#" + lineToArray[4] + "#" + lineToArray[5] + "#" + lineToArray[6] + "#" + lineToArray[7] + "#" + lineToArray[8] + "#OldMail";
                System.out.println(writingData);
                writer.add(writingData);
            } else {
                writingData = lineToArray[0] + "#" + lineToArray[1] + "#" + lineToArray[2] + "#" + lineToArray[3] + "#" + lineToArray[4] + "#" + lineToArray[5] + "#" + lineToArray[6] + "#" + lineToArray[7] + "#" + lineToArray[8] + "#" + lineToArray[9];
                System.out.println(writingData);
                writer.add(writingData);
            }

        }
        if (file.exists()) {
            System.out.println("File is here");
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.close();

            for (String write : writer) {
                try {
                    fileWriter = new FileWriter(file, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(write);
                    System.out.println(write);
                    bufferedWriter.newLine();
                } finally {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteMail(String folderName, String userName, String mailId) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        System.out.println(mailId);
        String userID = getUserID(userName);
        String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + userID + "@" + userName;
        String deleteFileName = filePath + "/" + userID + "@" + userName + "@" + folderName + "/" + userID + "@" + userName + "@" + folderName + "File.txt";

        ArrayList<String> writer = new ArrayList<>();

        File file = new File(deleteFileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        String writingData;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter = null;

        if (folderName.equals("Inbox")) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineToArray = line.split("#");
                System.out.println("lineToArra" + lineToArray[0]);
                if (lineToArray[0].equals(mailId)) {

                } else {
                    writingData = lineToArray[0] + "#" + lineToArray[1] + "#" + lineToArray[2] + "#" + lineToArray[3] + "#" + lineToArray[4] + "#" + lineToArray[5] + "#" + lineToArray[6] + "#" + lineToArray[7] + "#" + lineToArray[8] + "#" + lineToArray[9];
                    System.out.println(writingData);
                    writer.add(writingData);
                }

            }
        } else {
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineToArray = line.split("#");
                System.out.println("lineToArra" + lineToArray[0]);
                if (lineToArray[0].equals(mailId)) {

                } else {
                    writingData = lineToArray[0] + "#" + lineToArray[1] + "#" + lineToArray[2] + "#" + lineToArray[3] + "#" + lineToArray[4] + "#" + lineToArray[5] + "#" + lineToArray[6] + "#" + lineToArray[7] + "#" + lineToArray[8];
                    System.out.println(writingData);
                    writer.add(writingData);
                }

            }
        }

        if (file.exists()) {
            System.out.println("File is here");
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.close();

            for (String write : writer) {
                try {
                    rwLock.readLock().lock();
                    fileWriter = new FileWriter(file, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(write);
                    System.out.println(write);
                    bufferedWriter.newLine();
                } finally {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    rwLock.readLock().lock();
                }
            }
            return true;
        }
        return false;
    }

    public String getMailID() {
        String mailID = "";
        try {
            FileReader fileReader = new FileReader(allMailFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                mailID = line;
            }
            System.out.println(line);
//            String[] lineToArray = line.split("#");

            int userid = Integer.parseInt(mailID);
            userid += 1;
            if (userid < 10) {
                mailID = "000" + userid;
                return mailID;
            } else if (userid < 100) {
                mailID = "00" + userid;
                return mailID;
            } else if (userid < 100) {
                mailID = "0" + userid;
                return mailID;
            } else {
                mailID = "" + userid;
                return mailID;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MailFileAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MailFileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mailID;
    }

    public int countMails(String userName, String fileName) throws RemoteException, MalformedURLException, IOException, NotBoundException {
        String userId = getUserID(userName);
        String filePath = "D:/IJSE/Project/Second Sem Project/FileSavingFolder/" + userId + "@" + userName;
        String textFilePath = filePath + "/" + userId + "@" + userName + "@" + fileName + "/" + userId + "@" + userName + "@" + fileName + "File.txt";
        
        String line;
        int count = 0;
        File file=new File(textFilePath);
        FileReader fileReader=new FileReader(file);
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        while ((line=bufferedReader.readLine())!= null) {            
            count++;
        }
        return count;
    }
}
