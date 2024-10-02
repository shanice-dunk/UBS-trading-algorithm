package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.orderbook.order.Order;
import codingblackfemales.sotw.ChildOrder;
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
        
    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists (objects)
    private final List<Double> nearTouchBidPricesList = new ArrayList<>();
    private final List<Double> nearTouchAskPricesList = new ArrayList<>();
    
    // List hold the last 10 values of the bid and ask prices
    // Small window of price history for analysis (constant)
    private static final int maxPriceHistory = 10;

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
        updatePrices(nearTouchBidPricesList, nearBidPrice);
        updatePrices(nearTouchAskPricesList, nearAskPrice);
        
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

        // if (activeOrders.size() > 4) {
        //     // Retrieve the last child order
        //     final var lastOrder = activeOrders.get(activeOrders.size() - 1);
        //     // Log last order will be cancelled
        //     logger.info("[MYALGOLOGIC] Cancelling order: " + lastOrder);
        //     return new CancelChildOrder(lastOrder);
        // } 

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
            // Decide to buy or sell based on trends
            // Buy when near touch bid is downward trend 
            if (nearTouchBidTrend == PriceTrend.DownwardTrend) {
                // Create buy order if price is trending up
                logger.info("[MYALGOLOGIC] Bid price in downward trend. Place buy order with: " + bidQuantity + " @ " + nearBidPrice);
                return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
            // Sell when the near touch ask price is in upward trend
            } if (nearTouchAskTrend == PriceTrend.UpwardTrend) {
                // Create sell order if ask price is trending up
                logger.info("[MYALGOLOGIC] Ask price in upward trend. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
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
    private void updatePrices(List<Double> priceList, double price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price (FIFO)
        }
        priceList.add((double) price);
    }

    // Check if price list is null

    // Method to determine if prices are trending up, down or no trend
    private PriceTrend getPrice(List<Double> priceList) {
        if (priceList.size() < 3) {
            logger.warn("[Price list is null or not enough data.]");
            return PriceTrend.NoTrend; // Not enough data
        }

        // Retrieve the most recent prices from price list
        // Index of the last element
        Double lastPrice = priceList.get(priceList.size() - 1);
        // Index of the second to last element 
        Double previousPrice = priceList.get(priceList.size() - 2);

        if (lastPrice > previousPrice) {
            // Returns UpwardTrend if latest price is higher
            return PriceTrend.UpwardTrend;
        } else if (lastPrice < previousPrice) {
            // Returns DownwardTrend of latest price is lower
            return PriceTrend.DownwardTrend;
        } else {
            // Returns NoTrend if price is unchanged or not enough data
            return PriceTrend.NoTrend;
        }
    }

    // Define enum for trends UpwardTrend, DownwardTrend and NoTrend
    // enum = enumeration, represents fixed set of named constants 
    private enum PriceTrend {
        UpwardTrend, DownwardTrend, NoTrend;
    }

    }
