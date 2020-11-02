package com.ctaljaar.router.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ctaljaar.router.model.RouterGlobals;

public class MarketThreadUtil {

    protected static String errorOutputStream = null;
    protected static String IDOutputStream = null;

    // Very unlikely this works.
    // Function is attempting to find a Market in a Connection arraylist with a
    // market constructor.
    // public static void removeMarketFromOnlineList(MarketUtil thisMarket) {
    //     int index = RouterGlobals.onlineMarkets.indexOf(thisMarket);
    //     RouterGlobals.onlineMarkets.remove(index);
    //     System.out.println(RouterGlobals.onlineMarkets);
    // }
}
