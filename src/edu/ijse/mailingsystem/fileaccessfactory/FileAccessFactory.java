/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.fileaccessfactory;

import edu.ijse.mailingsystem.fileaccess.MailFileAccess;
import edu.ijse.mailingsystem.fileaccess.UserFileAccess;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author dulan
 */
public interface FileAccessFactory extends Remote {

    public UserFileAccess getUserFileAccess() throws RemoteException, MalformedURLException, IOException, NotBoundException;
    public MailFileAccess getMailFileAccess() throws RemoteException, MalformedURLException, IOException, NotBoundException;
}
