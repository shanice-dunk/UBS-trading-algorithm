package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;

import static codingblackfemales.action.NoAction.NoAction;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



// TREND ALGO LOGIC (tests are working)
public class MyAlgoLogic implements AlgoLogic {
        
    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists (objects)
    private final List<Double> nearTouchBidPricesList = new LinkedList<>();
    private final List<Double> nearTouchAskPricesList = new LinkedList<>();
    
    // List hold the last 10 values of the bid and ask prices
    // Small window of price history for analysis (constant)
    private static final int maxPriceHistory = 5;

    // // Variable to store last buy and sell prices
    // // Initialise both at -1 to indiact no buy or sell has been made
    // private double lastBuyPrice = -1;
    // private double lastSellPrice = -1;

    @Override
    // evaluate - decision making
    // Action - create or cancel orders
    public Action evaluate(SimpleAlgoState state) {
        // Threshold logic
        // Loggers run outputs of the current state of the order book
        logger.info("[MYALGOLOGIC] In My Algo Logic....");

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

        // Get highest bid price from order book
        // Object
        final BidLevel nearTouchBid = state.getBidAt(0);
        // Primitive values
        long bidQuantity = 100;
        long nearBidPrice = nearTouchBid.price;

        // Get lowest ask price from order book
        // Object
        final AskLevel nearTouchAsk = state.getAskAt(0);
        // Primitive values
        long askQuantity = 100;
        long nearAskPrice = nearTouchAsk.price;


        // Update the near touch bid and ask prices in the lists
        // Methods
        updateBidPrices(nearTouchBidPricesList, nearBidPrice);
        updateAskPrices(nearTouchAskPricesList, nearAskPrice);
        
        // Determine trend for both bid and ask prices 
        // Call getPrice method on updated price lists
        // Methods
        PriceTrend nearTouchBidTrend = getPrice(nearTouchBidPricesList);
        PriceTrend nearTouchAskTrend = getPrice(nearTouchAskPricesList);
        

        // Logs the trends for the bid and ask prices
        logger.info("[MYALGOLOGIC] Near touch bid price trend is: " + nearTouchBidTrend);
        logger.info("[MYALGOLOGIC] Near touch ask price trend is: " + nearTouchAskTrend);

        // Retrieve list of current active child orders
        final var activeOrders = state.getActiveChildOrders();

        // Cancel active orders based on trend
        for (var childOrder : activeOrders) {
            // Cancel BUY order if market trend = UPWARD
            if (childOrder.getSide() == Side.BUY && nearTouchBidTrend == PriceTrend.UpwardTrend) {
                logger.info("[MYALGOLOGIC] Market trend is UPWARD, cancelling BUY order: " + childOrder);
                return new CancelChildOrder(childOrder);
                // Cancel SELL order if market trend = DOWNWARD
            } else if (childOrder.getSide() == Side.SELL && nearTouchAskTrend == PriceTrend.DownwardTrend) {
                logger.info("[MYALGOLOGIC] Market trend is DOWNWARD, cancelling SELL order: " + childOrder);
                return new CancelChildOrder(childOrder);
            }
        }

        // If less than 5 child orders, create new order based on trend
        if (state.getChildOrders().size() < 5) {
            // // Decide to buy or sell based on trends
            if (nearTouchBidTrend == PriceTrend.DownwardTrend) {
                // Create buy order if price is trending up
                logger.info("[MYALGOLOGIC] Market currently in DOWNWARD trend. Place buy order with: " + bidQuantity + " @ " + nearBidPrice);
                // // Update last buy price
                // lastBuyPrice = nearBidPrice;
                // Create child order
                return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
                // Sell when the near touch ask price is in upward trend
            } if (nearTouchAskTrend == PriceTrend.UpwardTrend) {
                // Create sell order if ask price is trending up
                logger.info("[MYALGOLOGIC] Market currently in UPWARD trend. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
                // // Update last sell price
                // lastSellPrice = nearAskPrice;
                // // Check if there has been a profit
                // checkProfit();
                // // Create child order
                return new CreateChildOrder(Side.SELL, askQuantity, nearAskPrice);
            } else {
                // No action if there is no trend
                logger.info("[MYALGOLOGIC] No clear price trend, no action required.");
                return NoAction;
            }
        } else {
            // If there are 5 child orders, no action
            logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders, orders complete.");
            return NoAction;
        }

    }
    // Method maintains a list of most recent prices
    // If list exceeds maxPriceHistory, remove the oldest price and add new one
    private void updateBidPrices(List<Double> priceList, double price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price (FIFO)
        }
        priceList.add((double) price);

        // Log current price in list
        logger.info("[MYALGOLOGIC] Current BUY price list: " + nearTouchBidPricesList.toString());
    }

    private void updateAskPrices(List<Double> priceList, double price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price (FIFO)
        }
        priceList.add((double) price);

        // Log current price in list
        logger.info("[MYALGOLOGIC] Current SELL price list: " + nearTouchAskPricesList.toString());
    }

    // Method for percentage trend
    private PriceTrend getPrice(List<Double> priceList) {
        if (priceList == null) {
            logger.error("[MYALGOLOGIC] Price list is null.");
            return null;
        }

        // Ensure at least X prices to analyse trend
        if (priceList.size() < 3) {
            logger.warn("[MYALGOLOGIC] Not enough data to determine a price trend.");
            return PriceTrend.NoTrend;
        }

        // Threshold for percentage change
        final double percentageThreshold = 2.0;

        // Get oldest price and most recent price
        Double firstPrice = priceList.get(priceList.size() - 2); // Oldest
        Double lastPrice = priceList.get(priceList.size() - 1); // most recent

        // % change calculation
        double percentageChange = ((lastPrice - firstPrice) / firstPrice * 100);
        // Log % change at 2 decimal places
        logger.info("[MYALGOLOGIC] Percentage change from " + firstPrice + " to " + lastPrice + " is: " + String.format("%.2f", percentageChange) + "%");

        // Check if % change results in upward trend
        if (percentageChange >= percentageThreshold) {
            return PriceTrend.UpwardTrend;
        } // Check if % change results in downward trend
        // Negative % for downward trend
        else if (percentageChange <= -percentageThreshold) {
            return PriceTrend.DownwardTrend;
        } else {
            return PriceTrend.NoTrend; // No significant trend
        }
    }

    // Define enum for trends UpwardTrend, DownwardTrend and NoTrend
    // enum = enumeration, represents fixed set of named constants 
    private enum PriceTrend {
        UpwardTrend, DownwardTrend, NoTrend;
    }

}