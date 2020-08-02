/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.servicefactory;

import edu.ijse.mailingsystem.service.MailService;
import edu.ijse.mailingsystem.service.UserService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author dulan
 */
public interface ServiceFactory extends Remote {

    public UserService getUserService() throws RemoteException, MalformedURLException, IOException, NotBoundException;

    public MailService getMailService() throws RemoteException, MalformedURLException, IOException, NotBoundException;
}
