/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.fileaccessfactoryimpl;

import edu.ijse.mailingsystem.fileaccess.MailFileAccess;
import edu.ijse.mailingsystem.fileaccess.UserFileAccess;
import edu.ijse.mailingsystem.fileaccessfactory.FileAccessFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author dulan
 */
public class FileAccessFactoryImpl extends UnicastRemoteObject implements FileAccessFactory {

    public FileAccessFactoryImpl() throws RemoteException {

    }

    @Override
    public UserFileAccess getUserFileAccess() throws RemoteException, MalformedURLException, IOException, NotBoundException {
        return new UserFileAccess();
    }

    @Override
    public MailFileAccess getMailFileAccess() throws RemoteException, MalformedURLException, IOException, NotBoundException {
        return new MailFileAccess();
    }

}
