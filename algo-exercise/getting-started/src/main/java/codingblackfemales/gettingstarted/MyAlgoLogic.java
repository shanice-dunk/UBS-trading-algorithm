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



// TREND ALGO LOGIC WITH % (tests are working)
public class MyAlgoLogic implements AlgoLogic {
        
    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists (objects)
    private final List<Double> nearTouchBidPricesList = new LinkedList<>();
    private final List<Double> nearTouchAskPricesList = new LinkedList<>();
    
    // List hold the last 5 values of the bid and ask prices
    // Small window of price history for analysis (constant)
    private static final int maxPriceHistory = 5;

    @Override
    // evaluate - decision making
    // Action - create or cancel orders
    public Action evaluate(SimpleAlgoState state) {
        // Loggers run outputs of the current state of the order book
        logger.info("[MYALGOLOGIC] In My Algo Logic....");

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

        // Get best bid price from order book
        // Object
        final BidLevel nearTouchBid = state.getBidAt(0);
        // Primitive values
        long bidQuantity = 100;
        long nearBidPrice = nearTouchBid.price;

        // Get best ask price from order book
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
            if (childOrder.getSide() == Side.BUY && nearTouchBidTrend == PriceTrend.UpTrend) {
                logger.info("[MYALGOLOGIC] Market trend is UP, cancelling BUY order: " + childOrder);
                return new CancelChildOrder(childOrder);
                // Cancel SELL order if market trend = DOWNWARD
            } else if (childOrder.getSide() == Side.SELL && nearTouchAskTrend == PriceTrend.DownTrend) {
                logger.info("[MYALGOLOGIC] Market trend is DOWN, cancelling SELL order: " + childOrder);
                return new CancelChildOrder(childOrder);
            }
        }

        // If less than 5 child orders, create new order based on trend
        if (state.getChildOrders().size() < 5) {
            // Decide to buy or sell based on trends
            if (nearTouchBidTrend == PriceTrend.DownTrend) {
                // Create buy order if PriceTrend = DownTrend
                logger.info("[MYALGOLOGIC] Market currently in DOWNTREND. Place buy order with: " + bidQuantity + " @ " + nearBidPrice);
                return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
                // Create sell order if PriceTrend = UpTrend
            } if (nearTouchAskTrend == PriceTrend.UpTrend) {
                // Create sell order if ask price is trending up
                logger.info("[MYALGOLOGIC] Market currently in UPTREND. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
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
    // If list exceeds maxPriceHistory, remove the oldest price and add new one (FIFO)
    private void updateBidPrices(List<Double> priceList, double price) {
            if (priceList.size() == maxPriceHistory) {
                priceList.remove(0); // Remove the oldest price (FIFO)
            }

            // // Check for duplicates before adding the new prices
            // if (!priceList.contains(price)) {
                priceList.add((double) price);
            // } else {
            //     logger.info("[MYALGOLOGIC] Duplicate BUY price found: " + price + ". This will not be added to the list.");
            // }
    
            // Log current price in list
            logger.info("[MYALGOLOGIC] Current BUY price list: " + nearTouchBidPricesList.toString());
        }

    private void updateAskPrices(List<Double> priceList, double price) {
            if (priceList.size() == maxPriceHistory) {
                priceList.remove(0); // Remove the oldest price (FIFO)
            }
            // // Check for duplicates before adding the new prices
            // if (!priceList.contains(price)) {
                priceList.add((double) price);
            // } else {
            //     logger.info("[MYALGOLOGIC] Duplicate SELL price found: " + price + ". This will not be added to the list.");
            // }
    
            // Log current price in list
            logger.info("[MYALGOLOGIC] Current SELL price list: " + nearTouchAskPricesList.toString());
        }

    // Method for percentage trend
    // Null check 
    private PriceTrend getPrice(List<Double> priceList) {
        if (priceList == null) {
            logger.error("[MYALGOLOGIC] Price list is null.");
            return null;
        }

        // Ensure at least 3 prices to analyse trend
        if (priceList.size() < 3) {
            logger.warn("[MYALGOLOGIC] Not enough data to determine a price trend.");
            return PriceTrend.NoTrend;
        }

        // Threshold for percentage change
        final double rateOfChangeThreshold = 2.0;

        // Get 3rd and 5th price
        Double previousClosingPrice = priceList.get(priceList.size() - 3); // 3rd to last price in list
        Double currentClosingPrice = priceList.get(priceList.size() - 1); // last price in list

        // % change calculation
        double rateOfChange = ((currentClosingPrice - previousClosingPrice) / previousClosingPrice * 100);
        // Log % change at 2 decimal places
        logger.info("[MYALGOLOGIC] Percentage change from " + previousClosingPrice + " to " + currentClosingPrice + " is: " + String.format("%.2f", rateOfChange) + "%");

        // Check if % change results in upward trend
        if (rateOfChange > rateOfChangeThreshold) {
            return PriceTrend.UpTrend;
        } // Check if % change results in downward trend
        // Negative % for downward trend
        else if (rateOfChange < -rateOfChangeThreshold) {
            return PriceTrend.DownTrend;
        } else {
            return PriceTrend.NoTrend; // No significant trend
        }
    }

    // Define enum for trends UpwardTrend, DownwardTrend and NoTrend
    // enum = enumeration, represents fixed set of named constants 
    private enum PriceTrend {
        UpTrend, DownTrend, NoTrend;
    }

}