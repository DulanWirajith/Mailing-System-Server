/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.controllerimpl;

import edu.ijse.mailingsystem.controller.UserController;
import edu.ijse.mailingsystem.dto.UserDTO;
import edu.ijse.mailingsystem.observer.Observer;
import edu.ijse.mailingsystem.observer.Subject;
import edu.ijse.mailingsystem.reservation.UserReservation;
import edu.ijse.mailingsystem.servicefactory.ServiceFactory;
import edu.ijse.mailingsystem.servicefactoryimpl.ServiceFactoryImpl;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author dulan
 */
public class UserControllerImpl extends UnicastRemoteObject implements UserController, Subject {

    private static ArrayList<Observer> allobservers = new ArrayList<>();
    private ServiceFactory serviceFactory;
    
    private static final UserReservation userReservation = new UserReservation();

    public UserControllerImpl() throws RemoteException {
        serviceFactory = new ServiceFactoryImpl();
    }

    @Override
    public boolean addUser(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return serviceFactory.getUserService().addUser(user);
    }

    @Override
    public boolean modifyUser(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return serviceFactory.getUserService().modifyUser(user);
    }

    @Override
    public ArrayList<UserDTO> getAllUsers() throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return serviceFactory.getUserService().getAllUsers();
    }

    @Override
    public boolean SignIn(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return serviceFactory.getUserService().SignIn(user);
    }

    @Override
    public boolean userNameAvailability(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return serviceFactory.getUserService().userNameAvailability(userName);
    }

    @Override
    public UserDTO searchUser(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return serviceFactory.getUserService().searchUser(userName);
    }

    @Override
    public boolean reserveUser(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return userReservation.reserveUser(userName, this);
    }

    @Override
    public boolean releaseUser(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return userReservation.releaseUser(userName, this);
    }

    @Override
    public void registerObsever(Observer observer) throws RemoteException {
        allobservers.add(observer);
    }

    @Override
    public void unregisterObsever(Observer observer) throws RemoteException {
        allobservers.remove(observer);
    }

    @Override
    public void notifyObsever(String mailReceivers) throws RemoteException {
        for (Observer observer : allobservers) {
                observer.update(mailReceivers);
        }
    }

}
