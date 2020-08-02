/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.controllerimpl;

import edu.ijse.mailingsystem.controller.MailController;
import edu.ijse.mailingsystem.dto.MailDTO;
import edu.ijse.mailingsystem.observer.Observer;
import edu.ijse.mailingsystem.observer.Subject;
import edu.ijse.mailingsystem.servicefactory.ServiceFactory;
import edu.ijse.mailingsystem.servicefactoryimpl.ServiceFactoryImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dulan
 */
public class MailControllerImpl extends UnicastRemoteObject implements MailController, Subject {

    private ServiceFactory serviceFactory;
    private static ArrayList<Observer> observers = new ArrayList<>();

    public MailControllerImpl() throws RemoteException {
        serviceFactory = new ServiceFactoryImpl();
    }

    @Override
    public boolean sendMail(MailDTO mail) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        boolean sendMail = serviceFactory.getMailService().sendMail(mail);
        String receivers = "";
        for (String receiver : mail.getReceiversNames()) {
            receivers = receiver + ",";
        }
        final String receiversFinal = receivers;
        
        new Thread() {
            public void run() {
                try {
                    notifyObsever(receiversFinal);
                } catch (RemoteException ex) {
                    Logger.getLogger(MailControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }.start();

        return sendMail;

    }

    @Override
    public boolean saveAsDraft(MailDTO mail) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return serviceFactory.getMailService().saveAsDraft(mail);
    }

    @Override
    public boolean viewCurrentMail(String userName, String mailID) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return serviceFactory.getMailService().viewCurrentMail(userName, mailID);
    }

    @Override
    public ArrayList<MailDTO> viewAllMails(String userName, String folderName, String textFileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return serviceFactory.getMailService().viewAllMails(userName, folderName, textFileName);
    }

    @Override
    public boolean deleteMail(String folderName, String userName, String mailId) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return serviceFactory.getMailService().deleteMail(folderName, userName, mailId);
    }

    @Override
    public boolean saveReceivedAttachment(String fileSavingPath, String fileSavedPath, String fileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return serviceFactory.getMailService().saveReceivedAttachment(fileSavingPath, fileSavedPath, fileName);
    }

    @Override
    public ArrayList<MailDTO> viewAllMailsToInbox(String userName, String folderName, String textFileName) throws RemoteException, IOException, MalformedURLException, NotBoundException {
        return serviceFactory.getMailService().viewAllMailsToInbox(userName, folderName, textFileName);
    }

    @Override
    public int countMails(String userName, String fileName) throws RemoteException, MalformedURLException, IOException, NotBoundException {
        return serviceFactory.getMailService().countMails(userName, fileName);
    }

    @Override
    public void registerObsever(Observer observer) throws RemoteException {
        System.out.println("register");
        observers.add(observer);
    }

    @Override
    public void unregisterObsever(Observer observer) throws RemoteException {
        observers.remove(observer);
    }

    @Override
    public void notifyObsever(String mailReceivers) throws RemoteException {
        for (Observer observer : observers) {
            System.out.println("notifyObserver");
            System.out.println(mailReceivers);
            observer.update(mailReceivers);
        }
    }

}
