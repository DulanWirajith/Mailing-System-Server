/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.servicefactoryimpl;

import edu.ijse.mailingsystem.service.MailService;
import edu.ijse.mailingsystem.service.UserService;
import edu.ijse.mailingsystem.servicefactory.ServiceFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author dulan
 */
public class ServiceFactoryImpl extends UnicastRemoteObject implements ServiceFactory {

    public ServiceFactoryImpl() throws RemoteException {

    }

    @Override
    public UserService getUserService() throws RemoteException, MalformedURLException, IOException, NotBoundException {
        return new UserService();
    }

    @Override
    public MailService getMailService() throws RemoteException, MalformedURLException, IOException, NotBoundException {
        return new MailService();
    }

}
