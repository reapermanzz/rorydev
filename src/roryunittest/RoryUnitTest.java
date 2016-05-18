/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roryunittest;


import RTools.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import org.apache.http.HttpEntity.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.xml.ws.http.HTTPException;

/**
 * @author ReaperMan
 */
public class RoryUnitTest {

    /**
     * This method creates a basic set of qualifiers, with blank values, and ensures that the buildRoutekey method
     * puts together an appropriate string dealing with nulls.
     */
    @Test
    public void testBuildRoutekey(){
        TestCodeBox testBox = new TestCodeBox();
        //testBox.setdebugMode(true);
        System.out.println("Running test testBuildRoutekey...");
        ArrayList<String> qualifiers = new ArrayList<String>();
        qualifiers.add("support");
        qualifiers.add("english");
        qualifiers.add("exchange");
        qualifiers.add("sev1");
        qualifiers.add("");
        qualifiers.add("enterprise3");
        qualifiers.add("abc");
        qualifiers.add("");
        int qualSize = qualifiers.size()-1; //take off one slot for the routingRule
        String routeKey = testBox.buildRoutekey(qualifiers);
        //calculate the possible number of combos
        int permutations = 1;
        for(int i = 0; i<qualSize; i++) {//times it by 2 for each slot that is replaced.
            permutations = permutations * 2;
        }
    }

    /**
     * This method calls the buildAnyRoutekey method, ensuring that it creates the correct amount of permutations for *any*
     * wildcard replacement, without stripping off qualifiers.
     */
    @Test
    public void testBuildAnyRoutekey() {
        TestCodeBox testBox = new TestCodeBox();
        //testBox.setdebugMode(true);
        System.out.println("Running test testBuildAnyRoutekey...");
        ArrayList<String> qualifiers = new ArrayList<String>();
        qualifiers.add("support");
        qualifiers.add("english");
        qualifiers.add("exchange");
        qualifiers.add("sev1");
        qualifiers.add("enterprise3");
        qualifiers.add("abc");
        qualifiers.add("");
        int qualSize = qualifiers.size()-1; //take off one slot for the routingRule
        ArrayList<String> allRouteKeys = testBox.buildAnyRoutekey(qualifiers, testBox.createPowersetPositions(qualifiers));
        //calculate the possible number of combos
        int permutations = 1;
        for(int i = 0; i<qualSize; i++) {//times it by 2 for each slot that is replaced.
            permutations = permutations * 2;
        }
        assertEquals(allRouteKeys.size(), permutations);
        System.out.println("testBuildAnyRoutekey successful");
    }

    /**
     * Creates a simulated set of qualifiers, and validates that we receive the correct number of
     * routekey permutations based on the strip-off method, right to left.
     */
    @Test
    public void testBuildRoutekeysStripLeft(){
        TestCodeBox testBox = new TestCodeBox();
        //testBox.setdebugMode(true);
        System.out.println("Running test buildRoutekeysStripLeft...");
        //simulate a set of qualifiers.  The first one is always the routing Rule (support in case below).
        ArrayList<String> qualifiers = new ArrayList<String>();
        qualifiers.add("support");
        qualifiers.add("english");
        qualifiers.add("exchange");
        qualifiers.add("sev1");
        qualifiers.add("enterprise3");
        qualifiers.add("abc");
        qualifiers.add("");

        //initialize a list of routeKeys and call the method to return them.
        ArrayList<String> listOfRouteKeys = testBox.buildRoutekeyStripLeft(qualifiers);
        int qualifierSize = qualifiers.size();
        /*size times 2(one try with qualifier, one stripped off with any), minus 2... one for the original qualifier,
        because i ignore that for duplicate coverage, and one for removing a position to ignore the routingRule,
        which is always the first item in the qualifiers.
         */
        int permutations = ((qualifierSize-1)*2)-1;
        assertSame(listOfRouteKeys.size(), permutations);
        System.out.println("testBuildRoutekeysStripLeft successful");
    }

    @Test
    public void testGreaterThanTLS1HTTPS(){
        ArrayList <NameValuePair> parameters = null;
        String URL = "https://tls1test.salesforce.com/s/";
        String URL2 = "https://www.yahoo.com";

        TestCodeBox tb = new TestCodeBox();
        tb.setdebugMode(true);
        tb.attemptHTTPSPostConnection(URL, parameters, new String[]{"TLSv1.1"});
    }
}
