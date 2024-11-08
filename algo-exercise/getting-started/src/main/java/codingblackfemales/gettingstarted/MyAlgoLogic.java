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


public class MyAlgoLogic implements AlgoLogic {
        
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // Best bid and ask prices to analyse trends which are stored in two lists (objects)
    // LinkedList used due to fast adding and deleting at the beginning and end of lists = more efficient
    private final List<Double> nearTouchBidPricesList = new LinkedList<>();
    private final List<Double> nearTouchAskPricesList = new LinkedList<>();
    
    // List hold the last 5 values of the bid and ask prices
    // Small window of price history for analysis (constant)
    private static final int maxPriceHistory = 5;

    // Profit threshold
    private static final double profitThreshold = 1.5;

    // Array lists for executed buy and sell orders
    private final List<Double> buyPrices = new ArrayList<>();
    private final List<Double> sellPrices = new ArrayList<>();
    

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

        // If less than 3 child orders, create new order based on trend
        if (state.getActiveChildOrders().size() < 3) {
            if (nearTouchBidTrend == PriceTrend.DownTrend) {
                // Create buy order if PriceTrend = DownTrend
                logger.info("[MYALGOLOGIC] Market currently in DOWNTREND. Place buy order with: " + bidQuantity + " @ " + nearBidPrice);
                buyPrices.add((double) nearBidPrice);
                return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
                // Create sell order if PriceTrend = UpTrend
            } if (nearTouchAskTrend == PriceTrend.UpTrend) {
                // Create sell order if ask price is trending up
                logger.info("[MYALGOLOGIC] Market currently in UPTREND. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
                sellPrices.add((double) nearAskPrice);
                return new CreateChildOrder(Side.SELL, askQuantity, nearAskPrice);
            } else {
                // No action if there is no trend
                logger.info("[MYALGOLOGIC] No clear price trend, no action required.");
                return NoAction;
            }
        } else {
            logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders, orders complete.");
        }

    // Call calculateProfit() to determine profit % after trade
    if (sellPrices.size() > 0 && buyPrices.size() > 0) {
        boolean isProfit = calculateProfit();
        if (isProfit) {
            logger.info("[MYALGOLOGIC] Profit threshold has been met.");
        } else {
            logger.info("[MYALGOLOGIC] Profit threshold has not been met.");
        }
    }
    return NoAction;
}

    // Method to calculate profit based on the average buy and sell prices
    // stream() method called to convert list into stream (abstraction that allows to process elements in the list in functional style)
    // .mapToDouble - method to convert stream of Double objects into stream of primitive double values
    // Double::doubleValue - returns the double value that Double object represents
    // .avergae - calculates average of all double values in stream
    // orElse(0.0) - provides default value if stream is empty
    private boolean calculateProfit() {
        double averageBuyPrice = buyPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double averageSellPrice = sellPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        double profitPercent = ((averageSellPrice - averageBuyPrice) / averageBuyPrice) * 100;
        logger.info("[MYALGOLOGIC] Current profit percentage: " + String.format("%.2f", profitPercent) + "%");

        return profitPercent >= profitThreshold;
    }

    // Method for removing duplicates from list
    private void removeDuplicates(List<Double> pricList) {
        // Set to track unique values
        // Set only allows distinct elements
        Set<Double> uniquePrices = new HashSet<>();

        // Iterator to traverse list and remove duplicates
        // Safely remove elements from the list while traversing it
        // More efficient and avoids ConcurrentModificationException (modify the list directly while iterating)
        Iterator<Double> iterator = pricList.iterator();

        while (iterator.hasNext()) {
            Double price = iterator.next();
            // If the price is already in the set, remove from list
            if (!uniquePrices.add(price)) {
                iterator.remove();
            }
        }

    }

    // Method maintains a list of most recent prices
    private void updateBidPrices(List<Double> priceList, double price) {
            if (priceList.size() == maxPriceHistory) {
                priceList.remove(0); // Remove the oldest price (FIFO)
            }
                priceList.add((double) price);

                // Price list BEFORE removing duplicates
                logger.info("[MYALGOLOGIC] Current BUY price list BEFORE removing duplicates: " + nearTouchBidPricesList.toString());

                // Remove duplicates after adding new price
                removeDuplicates(priceList);
    
                // Price list AFTER removing duplicates
                logger.info("[MYALGOLOGIC] Current BUY price list AFTER removing duplicates: " + nearTouchBidPricesList.toString());
        }

    private void updateAskPrices(List<Double> priceList, double price) {
            if (priceList.size() == maxPriceHistory) {
                priceList.remove(0); // Remove the oldest price (FIFO)
            }

                priceList.add((double) price);

                // Price list BEFORE removing duplicates
                logger.info("[MYALGOLOGIC] Current SELL price list BEFORE removing duplicates: " + nearTouchAskPricesList.toString());

                // Remove duplicates after adding new price
                removeDuplicates(priceList);
    
                // Log current price in list
                logger.info("[MYALGOLOGIC] Current SELL price list AFTER removing duplicates: " + nearTouchAskPricesList.toString());
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

        // Get last and second to last price
        Double previousClosingPrice = priceList.get(priceList.size() - 2);
        Double currentClosingPrice = priceList.get(priceList.size() - 1);

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