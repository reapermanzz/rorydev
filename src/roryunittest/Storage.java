/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roryunittest;

import java.util.ArrayList;

/**
 *
 * @author ReaperMan
 */
public class Storage {

    public static String checkRouteReverse(ArrayList<String> qualifiers) {
        // assemble route key and try to get a route
        int qualifierSize = qualifiers.size();
        String routeKey = new String();
        while (qualifierSize > 0) {
            for (int i = 0; i < qualifierSize; i++) {
                // if this is the first qualifier
                if (i == 0) {
                    // if a qualifier ever returns a blank value set it to none instead
                    if (qualifiers.get(i).isEmpty()) {
                        routeKey = "none";
                    } else {
                        routeKey = qualifiers.get(i);
                    }
                } else // if a qualifier ever returns a blank value set it to none instead
                {
                    if (qualifiers.get(i).isEmpty()) {
                        routeKey = routeKey.concat(".none");
                    } else {
                        routeKey = routeKey.concat("." + qualifiers.get(i));
                    }
                }
            }
            try {
                // if this is not the first pass try to get a route using the wildcard first
                if (qualifiers.size() == qualifierSize) {
                    System.out.println(routeKey);
                } else {
                    System.out.println(routeKey + ".any");
                }
            } catch (Exception e) {
                // if this is not the first pass try to get a route without the wildcard
                if (qualifiers.size() == qualifierSize) {
                    // a route was not found continue on
                    ;
                } else {
                    try {
                        System.out.println(routeKey);
                    } catch (Exception e1) {
                        // a route was not found continue on
                        System.out.println("****Error: " + e);
                    }
                }
            }
            qualifierSize--;
        }
        return "Finished Running";
    }

}
