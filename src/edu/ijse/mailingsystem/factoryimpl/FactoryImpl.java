/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.factoryimpl;

import edu.ijse.mailingsystem.controller.MailController;
import edu.ijse.mailingsystem.controller.SuperController;
import edu.ijse.mailingsystem.controller.UserController;
import edu.ijse.mailingsystem.controllerimpl.MailControllerImpl;
import edu.ijse.mailingsystem.controllerimpl.UserControllerImpl;
import edu.ijse.mailingsystem.factory.MailingSystemFactory;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author dulan
 */
public class FactoryImpl extends UnicastRemoteObject implements MailingSystemFactory {

    private static FactoryImpl factoryImpl;

    public FactoryImpl() throws RemoteException {
    }

    public static FactoryImpl getFactoryImpl() throws RemoteException {
        if (factoryImpl == null) {
            factoryImpl = new FactoryImpl();
        }
        return factoryImpl;
    }

    @Override
    public SuperController getController(ControllerType controllerType) throws RemoteException {
        switch (controllerType) {
            case USER:
                return new UserControllerImpl();
            case MAIL:
                return new MailControllerImpl();
            default:
                return null;
        }
    }

}
