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
    private static final int maxPriceHistory = 10;

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
    private void updatePrices(List<Double> priceList, double price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price (FIFO)
        }
        priceList.add((double) price);
    }

    // Check if price list is null

    // Method to determine if prices are trending up, down or no trend
    // Add null check to make sure price list is being updated
    private PriceTrend getPrice(List<Double> priceList) {
        if (priceList == null) {
            logger.error("[MYALGOLOGIC] Price list is null.");
            return null;
        }

        if (priceList.size() < 4) {
            logger.warn("[Not enough data in the price list.]");
            return PriceTrend.NoTrend; // Not enough data
        }

        // Retrieve the most recent prices from price list
        // Index of the last element
        Double lastPrice = priceList.get(priceList.size() - 1);
        // Index of the second to last element 
        Double secondLastPrice = priceList.get(priceList.size() - 2);
        // Index of third to last price
        Double thirdLastPrice = priceList.get(priceList.size() - 3);

        if (lastPrice > secondLastPrice && thirdLastPrice < secondLastPrice) {
            // Returns UpwardTrend if latest price is higher
            return PriceTrend.UpwardTrend;
        } else if (lastPrice < secondLastPrice && thirdLastPrice > secondLastPrice) {
            // Returns DownwardTrend of latest price is lower
            return PriceTrend.DownwardTrend;
        } else {
            // Returns NoTrend if price is unchanged or not enough data
            return PriceTrend.NoTrend;
        }
    }

    // // Method to check if a profit was made
    // private void checkProfit() {
    //     if (lastBuyPrice > 0 && lastSellPrice > 0) {
    //         if (lastSellPrice > lastBuyPrice) {
    //             logger.info("[MYALGOLOGIC] Potential profit made. Bid placed at " + lastBuyPrice + " and ask placed at " + lastSellPrice);
    //         } else {
    //             logger.info("[MYALGOLOGIC] No profit has been made. Bid placed at " + lastBuyPrice + " and ask placed at " + lastSellPrice);
    //         }
    //     }
    // }

    // // Method to determine if trend is reversing 
    // private boolean trendReversing(List<Double> priceList, PriceTrend currentTrend) {
    //     if (priceList == null || priceList.size() < 3) {
    //         return false; // Not enough data
    //     }

    //     Double lastPrice = priceList.get(priceList.size() - 1);
    //     Double secondLastPrice = priceList.get(priceList.size() - 2);
    //     Double thirdLastPrice = priceList.get(priceList.size() - 3);

    //     // Check for upward to downward
    //     if (currentTrend == PriceTrend.UpwardTrend && secondLastPrice > thirdLastPrice && lastPrice < secondLastPrice) {
    //         return true; // Price is now failling
    //     }

    //     // Check for downward to upward
    //     if (currentTrend == PriceTrend.DownwardTrend && secondLastPrice < thirdLastPrice && lastPrice > secondLastPrice) {
    //         return true; // Price is now rising
    //     }
    //     return false;
    // }

    // Define enum for trends UpwardTrend, DownwardTrend and NoTrend
    // enum = enumeration, represents fixed set of named constants 
    private enum PriceTrend {
        UpwardTrend, DownwardTrend, NoTrend;
    }

    }
