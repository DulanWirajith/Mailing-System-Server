/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.service;

import edu.ijse.mailingsystem.controller.MailController;
import edu.ijse.mailingsystem.dto.MailDTO;
import edu.ijse.mailingsystem.fileaccessfactory.FileAccessFactory;
import edu.ijse.mailingsystem.fileaccessfactoryimpl.FileAccessFactoryImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Dulan
 */
public class MailService {

    FileAccessFactory fileAccessFactory;

    public MailService() throws RemoteException {
        fileAccessFactory = new FileAccessFactoryImpl();
    }

    public boolean sendMail(MailDTO mail) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().sendMail(mail);
    }

    public boolean saveAsDraft(MailDTO mail) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().saveAsDraft(mail);
    }

    public boolean viewCurrentMail(String userName, String mailID) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().viewCurrentMail(userName, mailID);
    }

    public ArrayList<MailDTO> viewAllMails(String userName, String folderName, String textFileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().viewAllMails(userName, folderName, textFileName);
    }

    public boolean deleteMail(String folderName, String userName, String mailId) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return  fileAccessFactory.getMailFileAccess().deleteMail(folderName, userName, mailId);
    }

    public boolean saveReceivedAttachment(String fileSavingPath, String fileSavedPath, String fileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().saveReceivedAttachment(fileSavingPath, fileSavedPath, fileName);
    }

    public ArrayList<MailDTO> viewAllMailsToInbox(String userName, String folderName, String textFileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().viewAllMailsToInbox(userName, folderName, textFileName);
    }
    
    public int countMails(String userName, String fileName) throws RemoteException, MalformedURLException, IOException, NotBoundException {
        return fileAccessFactory.getMailFileAccess().countMails(userName, fileName);
    }
}
