/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roryunittest;


import RTools.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ReaperMan
 */
public class RoryUnitTest {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            TestCodeBox box = new TestCodeBox();
            //create a qualifiers array
            ArrayList<String> quals = new ArrayList();
            box.setdebugMode(true);

            quals.add("support");
            quals.add("english");
            quals.add("exchange");
            quals.add("sev1");
            quals.add("enterprise3");
            quals.add("abc");
            quals.add("");
            //box.checkForRoutes(quals);
            //System.out.println(box.buildRoutekey(quals));
            //System.out.println(box.buildAnyRoutekey(quals, 7));

            //box.allAnyCombination(quals);

        } catch (Exception e) {
            System.out.println("Exception in RoryUnitTest.java, error: " + e);
        }

    }

    @Test
    public void testCreateCombination() {
        TestCodeBox testBox = new TestCodeBox();
        testBox.setdebugMode(true);
        testBox.showDebugMessage("Testing execution of method...");
        ArrayList<String> qualifiers = new ArrayList<String>();

        qualifiers.add("support");
        qualifiers.add("english");
        //qualifiers.add("");
        //qualifiers.add("");
        qualifiers.add("exchange");
        qualifiers.add("sev1");
        /*qualifiers.add("enterprise3");
        qualifiers.add("abc");*/

        System.out.println(testBox.createPowersetPositions(qualifiers));
        testBox.buildAnyRoutekey(qualifiers, testBox.createPowersetPositions(qualifiers));
        /*ArrayList<ArrayList> list = new ArrayList<ArrayList>(testBox.allAnyCombination(qualifiers));
        for(ArrayList<ArrayList> theList: list){
            System.out.println(theList);
        }*/
        //System.out.println(testBox.createPowersetPositions(qualifiers));
        //System.out.println(testBox.createPowersetPositions(qualifiers).size());
        //testBox.makeAllPermutations(qualifiers, qualifiers.size());
        //System.out.println(testBox.routeKeyArray);


    }

}
