/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ijse.mailingsystem.reservation;

import java.util.HashMap;
import edu.ijse.mailingsystem.controller.UserController;

/**
 *
 * @author Dulan
 */
public class UserReservation {

    private HashMap<String, UserController> reserveData = new HashMap<>();

    public boolean reserveUser(String userName, UserController userController) {
        if (reserveData.containsKey(userName)) {
            return reserveData.get(userName) == userController;
        } else {
            reserveData.put(userName, userController);
            return true;
        }
    }

    public boolean releaseUser(String userName, UserController userController) {
        if (reserveData.get(userName) == userController) {
            reserveData.remove(userName);
            return true;
        } else {
            return false;
        }
    }
}
