/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.service;

import edu.ijse.mailingsystem.controller.UserController;
import edu.ijse.mailingsystem.dto.UserDTO;
import edu.ijse.mailingsystem.fileaccessfactory.FileAccessFactory;
import edu.ijse.mailingsystem.fileaccessfactoryimpl.FileAccessFactoryImpl;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author dulan
 */
public class UserService {

    FileAccessFactory fileAccessFactory;

    public UserService() throws RemoteException {
        this.fileAccessFactory = new FileAccessFactoryImpl();
    }

    public boolean addUser(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getUserFileAccess().addUser(user);
    }

    public boolean removeUser(String userID) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean modifyUser(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getUserFileAccess().modifyUser(user);
    }

    public UserDTO searchUser(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getUserFileAccess().searchUser(userName);
    }

    public ArrayList<UserDTO> getAllUsers() throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getUserFileAccess().getAllUsers();
    }

    public boolean signUp(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean SignIn(UserDTO user) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getUserFileAccess().SignIn(user);
    }

    public boolean userNameAvailability(String userName) throws IOException, RemoteException, MalformedURLException, NotBoundException {
        return fileAccessFactory.getUserFileAccess().userNameAvailability(userName);
    }

}
