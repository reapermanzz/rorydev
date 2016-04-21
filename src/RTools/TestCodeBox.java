package RTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class TestCodeBox {

    private JSONObject routeList = null;
    public static TestCodeBox testM = new TestCodeBox();
    private Boolean debugMode = false;
    public static ArrayList<String> routeKeyArray = new ArrayList<String>();

    public String checkForRoutes(ArrayList<String> qualifiers) {
        // assemble route key and try to get a route
        String routeKey = new String(buildRoutekey(qualifiers));
        ArrayList qualifierHolder = qualifiers;
        int qualifierSize = qualifiers.size();
        int firstTimeCounter = qualifiers.size();
        ArrayList<String> stripOrder = new ArrayList<String>();
        stripOrder.add("any.");
        stripOrder.add(routeKey);
        for (int j = 0; j < 2; j++) { //this loop enables qualifiers to be stripped off off in forward and reverse directions to account for *any* wildcard being placed anywhere.
            qualifiers = new ArrayList(qualifierHolder);//re-init the qualifier list
            qualifierSize = qualifiers.size();//re-init the counter
            while (qualifierSize > 0) {
                if (j == 0) {//update the display order array with the newly built routeKey
                    stripOrder.set(1, buildRoutekey(qualifiers));
                } else {
                    stripOrder.set(0, buildRoutekey(qualifiers));
                }
                try {
                    // if this is not the first pass try to get a route using the wildcard first
                    if (firstTimeCounter == qualifierSize) {
                        // callForRoute(routeKey);
                        //showDebugMessage("Calling for ANY route first time....");
                        //callForRoute(buildAnyRoutekey(qualifierHolder, qualifiers.size()));
                    } else {
                        //showDebugMessage("Regular...");
                        //callForRoute(stripOrder.get(0) + stripOrder.get(1));
                        //showDebugMessage("Calling for ANY route....");
                        //callForRoute(buildAnyRoutekey(qualifierHolder, qualifiers.size()));
                    }
                } catch (Exception e) {
                    // if this is not the first pass try to get a route without the wildcard
                    if (qualifiers.size() == qualifierSize) {
                        // a route was not found continue on
                        callForRoute(routeKey);
                    } else {
                        try {
                            callForRoute(routeKey);
                        } catch (Exception e1) {
                            // a route was not found continue on
                            showDebugMessage("****Error: " + e);
                        }
                    }
                }
                if (stripOrder.get(0) == "any.") {//strip off the first or last, depending on order of array.
                    qualifiers.remove(0);//strip off the qualifiers
                } else {
                    qualifiers.remove(qualifierSize - 1);//strip off the qualifiers
                }

                qualifierSize--; //loop countdown
            }
            stripOrder.set(0, routeKey);//reverse the order and re-run the loop
            stripOrder.set(1, ".any");
        }

        return "Finished Running";
    }

    /**
     * The purpose of this method is to simply intake an array of Qualifiers, and put them together into a routeKey string.
     * This is a vanilla routeKey method - it just builds a basic string, unlike buildAnyRouteKey which is
     * designed to replace a range of strings.  This doesn't replace anything, simply puts together a route Key.
     *
     * @param qualifiers - the list of qualifiers to build into a routeKey string.
     * @return - The routeKey built from the qualifiers.
     */
    public String buildRoutekey(ArrayList<String> qualifiers) {
        String routeKey = new String();
        int qualifierSize = qualifiers.size();
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
                if (qualifiers.get(i).isEmpty()) {
                    routeKey = routeKey.concat(".none");
                } else {
                    routeKey = routeKey.concat("." + qualifiers.get(i));
                }
        }
        return routeKey;
    }

    /**The purpose of this method is to build all permutations of a route key based on an instruction set given.
     * its designed to iterate through the instruction sets and adhere to them in terms of replacing indexes with the any wildcard.
     * this enables us to build sets of routeKey permutations dynamically based on qualifier size and custom instructions.
     * @param qualifiers - the original qualifiers by which to base all permutations off of.
     * @param replacePositions - An ArrayList<ArrayList<String>> containing positions that you want to replace, as index strings. 0, 1, 2, etc... Each set of
     *                         index positions is a permutation for a routeKey.
     * @return - an ArrayList<String> of routeKeys to query for.
     */
    public ArrayList<String> buildAnyRoutekey(ArrayList<String> qualifiers, ArrayList<ArrayList> replacePositions) {
        String routeKey = new String();
        String routingRule = qualifiers.get(0);
        qualifiers.remove(0);
        ArrayList<String> protectOriginalQualifier = new ArrayList<String>(qualifiers);
        int qualifierSize = qualifiers.size();
        int currentIndex = qualifierSize - 1; //sets the array position offset.
        ArrayList<String> routeKeys = new ArrayList<String>();

        //start a loop for re-writing the qualifiers based on how many needed, NEVER replacing the first
        for (ArrayList<String> currentReplace : replacePositions) {
            protectOriginalQualifier = new ArrayList<String>(qualifiers);
            this.showDebugMessage("current Replace Set: " + currentReplace);
            if (currentReplace.isEmpty()) {
                this.showDebugMessage("CurrentReplace is empty");
                routeKey = buildRoutekey(protectOriginalQualifier);
            } else {
                for (int i = 0; i < qualifiers.size(); i++) {
                    // if this is the first qualifier
                    if (i == 0) {
                        // if a qualifier ever returns a blank value set it to none instead
                        if (qualifiers.get(i).isEmpty()) {
                            if (currentReplace.contains(Integer.toString((i)))) {
                                routeKey = "any";
                            } else {
                                routeKey = "none";
                            }

                        } else {
                            if (currentReplace.contains(Integer.toString((i)))) {
                                routeKey = "any";
                            } else {
                                routeKey = qualifiers.get(i);
                            }
                        }
                    } else //if a qualifier ever returns a  blank value set it to none instead
                    {
                        if (qualifiers.get(i).isEmpty()) {
                            if (currentReplace.contains(Integer.toString((i)))) {//if this happens to be the one thats supposed to be set to any, then set that.
                                routeKey = routeKey + ".any";
                            } else {
                                routeKey = routeKey.concat(".none"); //if its not the one being requested to change.
                            }
                        } else {
                            if (currentReplace.contains(Integer.toString((i)))) {
                                protectOriginalQualifier.set(i, "any");
                            }
                            if (compareTwoNumbers(i, qualifiers.size())) {
                                routeKey = routeKey + "." + protectOriginalQualifier.get(i) + "."; //if its not the last
                            } else {
                                routeKey = routeKey + "." + protectOriginalQualifier.get(i);
                            }
                        }
                    }
                }
            }
            routeKey = routingRule + "." + routeKey;
            routeKeys.add(routeKey);
        }
        this.showDebugMessage("the routeKeys Array: " + routeKeys);
        this.showDebugMessage("routeKeys Array Size: " + routeKeys.size());
        return routeKeys;
    }

    public void callForRoute(String routeKey) {
        showDebugMessage(routeKey);
    }

    /**
     * The purpose of this function is to simply return true or false based on an integer comparison.
     * If the two inputs are equal, it returns true.  Otherwise False.
     *
     * @param num1 - First integer to compare
     * @param num2 - Second integer to compare
     * @return
     */
    public Boolean compareTwoNumbers(int num1, int num2) {
        if (num1 > num2) {
            return false;
        } else if (num1 < num2) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList allAnyCombination(ArrayList<String> qualifiers) {
        String routingRule = qualifiers.get(0); //extract the qualifier before calculating ANY permutations.
        int offSet = qualifiers.size() - 1;
        ArrayList<String> quals = new ArrayList<String>(qualifiers); //initialize a copy of the incoming object to change
        ArrayList<ArrayList> combinations = new ArrayList<ArrayList>(); //ArrayList of arraylists to house the various combinations returned.
        int indexShift = 1;
        int ignoreForShift = 0;
        while (ignoreForShift < qualifiers.size()) {
            //this.showDebugMessage("Third loop: " + ignoreForShift);
            while (indexShift < qualifiers.size()) {
                for (int i = 0; i < qualifiers.size(); i++) {
                    if (!compareTwoNumbers(i, ignoreForShift)) {
                        quals.set(i, "any");
                    }
                }
                combinations.add(quals);
                quals = new ArrayList<>(qualifiers); //re-initialize the working array to insert the next wildcard combination.
                indexShift++;
                //this.showDebugMessage("2nd loop");
            }
            indexShift = 1; //re-init the 2nd inner loop
            quals = new ArrayList<>(qualifiers); //re-initialize the working array to insert the next wildcard combination.
            ignoreForShift++;
        }
        return combinations;
    }

    /**
     * The purpose of this is to create a powerset of array positions that represent all possible combinations of qualifier
     * array to replace as *any*.  It automatically generates, based on the size of the qualifier set, what positions need to change to
     * the any wildcard, to cover all possibilities of the wildcard in any position of the route Key.
     *
     * @param qualifiers - this is the input qualifier set
     * @return - an ArrayList of qualifier position changes.  Each set within the array is an iteration of wildcards to replace with Any in the qualifier array.
     */
    public ArrayList<ArrayList> createPowersetPositions(ArrayList<String> qualifiers) {
        Set<String> qualifierSet = new LinkedHashSet<>(); //initialize a set to hold the qualifiers.
        //Based on the size of the qualifiers array, create a set to insert the positions that represent the qualifiers.
        ArrayList<String> protectOriginalQualifiers = new ArrayList<String>(qualifiers);
        protectOriginalQualifiers.remove(0);
        for (int i = 0; i < protectOriginalQualifiers.size(); i++) {
            qualifierSet.add(Integer.toString(i));
        }
        ArrayList<String> powerSetHolder = new ArrayList<String>(); //a temporary holder for the positions generated from powerset.
        ArrayList<ArrayList> entirePowerset = new ArrayList<ArrayList>(); //the returning array  initialization
        for (Set<String> s : Sets.powerSet(qualifierSet)) {
            for (Iterator<String> it = s.iterator(); it.hasNext(); ) {
                powerSetHolder.add(it.next());
            }
            entirePowerset.add(powerSetHolder); //add the position set to the return arrayList.
            powerSetHolder = new ArrayList<String>(); //re-init the holder fresh.
        }
        return entirePowerset;
    }

    /**
     * The purpose of this method is to produce all routeKey string possibilities using the Any wildcard.
     *
     * @param qualifiers   - the original qualifier array to compose routeKeys from
     * @param currentIndex - the array index to shift for the coverage pattern of replacing *any* wildcard on the qualifiers.
     */
    public void makeAllPermutations(ArrayList<String> qualifiers, int currentIndex) {
        ArrayList<String> currentArray = new ArrayList<String>();
        currentIndex = currentIndex - 1;
        if (routeKeyArray.isEmpty()) {
            currentArray.add(qualifiers.get(currentIndex));
            currentArray.add("any");
        } else {
            for (int i = 0; i < routeKeyArray.size(); i++) {
                currentArray.add(qualifiers.get(currentIndex) + "." + routeKeyArray.get(i));
                currentArray.add("any." + routeKeyArray.get(i));
            }
        }
        routeKeyArray = currentArray;
        if (currentIndex > 0) {
            makeAllPermutations(qualifiers, currentIndex);
        }

    }


    /**
     * The purpose of this method is to check whether or not debugging is enabled, if so, print the debug message.
     * if not, ignore.
     *
     * @param message - the string you want to print to the console
     */
    public void showDebugMessage(String message) {
        if (debugMode) {
            System.out.println(message);
        }
    }

    public void setdebugMode(Boolean status) {
        this.debugMode = status;
    }
}
