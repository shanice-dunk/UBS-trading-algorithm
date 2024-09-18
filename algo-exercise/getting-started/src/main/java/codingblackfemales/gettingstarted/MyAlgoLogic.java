package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
//import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;

import static codingblackfemales.action.NoAction.NoAction;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic<PriceTrend> implements AlgoLogic {
        
    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists
    private final List<Double> nearTouchBidPrices = new LinkedList<>();
    private final List<Double> nearTouchAskPrices = new LinkedList<>();

    // List hold the last 3 values of the bid and ask prices
    // Small window of price history for analysis
    private static final int maxPriceHistory = 5;

    @Override
    // evaluate - decision making
    // Action - create or cancel orders
    public Action evaluate(SimpleAlgoState state) {
        // Threshold logic
        // Loggers run outputs of the current state of the order book
        logger.info("[MYALGOLOGIC] In My Algo Logic....");

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

        // var totalOrderCount = state.getChildOrders().size();

        // Get highest bid price from order book
        final BidLevel nearTouchBid = state.getBidAt(0);
        long bidQuantity = 100;
        long bidPrice = nearTouchBid.price;

        // Get lowest ask price from order book
        final AskLevel nearTouchAsk = state.getAskAt(0);
        long askQuantity = 100;
        long askPrice = nearTouchAsk.price;

        // Update the near touch bid and ask prices in the lists
        updateNearTouch(nearTouchBidPrices, bidPrice);
        updateNearTouch(nearTouchAskPrices, askPrice);

        // Determine trend for both bid and ask prices 
        // Call getPrice method on updated price lists
        PriceTrend bidTrend = getPrice(nearTouchBidPrices);
        PriceTrend askTrend = getPrice(nearTouchAskPrices);

        // Logs the trends for the bid and ask prices
        logger.info("[MYALGOLOGIC] Bid price trend is: " + bidTrend);
        logger.info("[MYALGOLOGIC] Ask price trend is: " + askTrend);

        // Retrieve list of current active child orders
        final var activeOrders = state.getActiveChildOrders();

        // Cancel if there are 3 active child orders
        if (activeOrders.size() == 3) {
            // stream().findFirst - get first active order
            final var option = activeOrders.stream().findFirst();
            // option.isPresent() - order found, logs that it will cancel order and returns the CancelChildOrder action
            if (option.isPresent()) {
                var childOrder = option.get();
                logger.info("[MYALGOLOGIC] Cancelling order:" + childOrder);
                return new CancelChildOrder(childOrder);
            }
            // No action if no order is found
            else{
                return NoAction;
            }
        }

        // If less than 5 child orders, create new order based on trend
        if (state.getChildOrders().size() < 5) {
            // Decide to buy or sell based on trends
            // If bid price is in upward trend, create buy order
            if (bidTrend == PriceTrend.DOWN) {
                // Create buy order if price is trending up
                logger.info("[MYALGOLOGIC] Bid price in downward trend. Place buy order with: " + bidQuantity + " @ " + bidPrice);
                return new CreateChildOrder(Side.BUY, bidQuantity, bidPrice);
            // If ask price is in upward trend, create sell order
            } if (askTrend == PriceTrend.UP) {
                // Create sell order if ask price is trending up
                logger.info("[MYALGOLOGIC] Ask price in upward trend. Placing sell order with: " + askQuantity + " @ " + askPrice);
                return new CreateChildOrder(Side.SELL, askQuantity, askPrice);
            } else {
                // No action if there is no trend
                logger.info("[MYALGOLOGIC] No clear price trend, no action required.");
                return NoAction;
            }
        } else {
            // If there are 3 child orders, no action
            logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders, orders complete.");
            return NoAction;
        }

    }
    // Method maintains a list of most recent prices
    // If list exceeds maxPriceHistory, remove the oldest price and add new one
    private void updateNearTouch(List<Double> priceList, long price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price
        }
        priceList.add((double) price);
    }

    // Method to determine if prices are trending up or down
    private PriceTrend getPrice(List<Double> priceList) {
        if (priceList.size() < 2) {
            return PriceTrend.NONE; // Not enough data
        }

        Double lastPrice = priceList.get(priceList.size() - 1);
        Double previousPrice = priceList.get(priceList.size() - 2);

        if (lastPrice > previousPrice) {
            // Returns UP if latest price is higher
            return PriceTrend.UP;
        } else if (lastPrice < previousPrice) {
            // Returns DOWN of latest price is lower
            return PriceTrend.DOWN;
        } else {
            // Returns NONE if price is unchanged or not enough data
            return PriceTrend.NONE;
        }
    }

    // Define enum for trends UP, DOWN, NONE
    // enum = enumeration, represents fixed set of named constants 
    private enum PriceTrend {
        UP, DOWN, NONE;
    }

    }

     
//         // Best bid price for buying
//         final BidLevel nearTouch = state.getBidAt(0);
        
//         long quantity = 100;
//         long price = nearTouch.price;

//         // Checks total number of child orders that have been created
//         // If the number is greater than 20, no action taken
//         // This prevents over-trading by limiting number of active orders
//          if (totalOrderCount > 20) {
//             return NoAction;
//          }
//         // Retrieves list of active child orders
//         final var activeOrders = state.getActiveChildOrders();
            
//         // for loop to iterates over active orders and checks if prices are higher than order. If so, order is cancelled
//         for (var order : activeOrders) {
//             if (order.getPrice() > nearTouch.price) {
//                 logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + " (nearTouch is: " + nearTouch.price + ")");
//                 // If order is priced too high, algo cancels
//                 return new CancelChildOrder(order);
//             }

//         }
//         // LOGIC 1 = threshold
//         // Threshold logic to determine if the price is low
//         // Passive strategy when price is low
//         if (price <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
//             logger.info("[MYALGOLOGIC] Price is below or equal to threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + price);
//             logger.info("[MYALGOLOGIC] Profit made based on buy threshold: £" + (buyPriceThreshold - price));
//             return new CreateChildOrder(Side.BUY, quantity, price);     
//         } 
//         // No action if all child orders done
//         logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders done, wanted " + maxBuyOrders + ", no action needed.");
//             return NoAction;
        
//     }   

// }

    //     // Methods define the price thresholds for when to buy or sell
    //     // Return values subject to change 
    // protected double buyPriceThreshold() {
    //     return 104; // If current market price is less than 104, algo will buy
    // }

// Average price logic
//        logger.info("[MYALGOLOGIC] In My Algo Logic...");

//        var orderBookAsString = Util.orderBookToString(state);

//        logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//        var totalOrderCount = state.getActiveChildOrders().size();

//        long quantity = 100;

//        // Prevent over-trading by limiting the number of active orders
//        if (totalOrderCount > 15) {
//         return NoAction;
//        }

//     //    // Retrieves list of active child orders
//     //    final var activeOrders = state.getActiveChildOrders();
//     //    // Cancel active orders
//     //    if (activeOrders.size() > 0) {
//     //     // Get first active order
//     //     final var option = activeOrders.stream().findFirst();
//     //     // Order found, cancel order and return CancelChildOrder action
//     //     if (option.isPresent()) {
//     //         var childOrder = option.get();
//     //         logger.info("[MYALGOLOGIC] Cancelling order: " + childOrder);
//     //         return new CancelChildOrder(childOrder);
//     //     }
    
//     // }

//        // Cancel orders price above the buyPriceThreshold
//        for (var childOrder : state.getActiveChildOrders()) {
//         if (childOrder.getPrice() > buyPriceThreshold) {
//             logger.info("[MYALGOLOGIC] Cancelling order at price: " + childOrder.getPrice() + ". Buy threshold is: " + buyPriceThreshold);
//             return new CancelChildOrder(childOrder);
//         }
//        }

//        // Calculate the average price of all bids and asks
//        long averagePrice = calculateAveragePrice(state);

//        // Log the average price
//        logger.info("[MYALGOLOGIC] Average price is: " + averagePrice);

//        // Place child order if average price is <= to buy threshold
//        // And if max buy orders have not been reached
//        if (averagePrice <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
//         logger.info("[MYALGOLOGIC] Average is is below or equal to buy threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + averagePrice);
//         logger.info("[MYALGOLOGIC] Total profit to be made: £" + (buyPriceThreshold - averagePrice));
//         logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders completed, want " + maxBuyOrders);
//         return new CreateChildOrder(Side.BUY, quantity, averagePrice);
//        }

//        logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders completed, wanted " + maxBuyOrders + ", no action required.");
//        return NoAction;

//     }

//     // Method: calculate the average price of all bid and ask levels
//     private long calculateAveragePrice(SimpleAlgoState state) {
//         long priceSum = 0;
//         int priceCount = 0;

//         // Index for bid and ask levels
//         int i = 0;

//         // While loop to sum bid prices
//         // Continue loop while i < total number of bid levels
//         while (i < state.getBidLevels()) {
//             BidLevel bid = state.getBidAt(i); // Get bid levels at index i
//             priceSum += bid.price; // Add bid price to priceSum
//             priceCount++; // Increment count of prices
//             i++; // Increment index i for next iteration
//         }

//         // Reset index to 0 to iterate through ask levels
//         i = 0;

//         // While loop to sum ask prices
//         // Continue loop while i < total number of ask levels
//         while (i < state.getAskLevels()) {
//             AskLevel ask = state.getAskAt(i); // Get ask level at index i
//             priceSum += ask.price; // Add ask price to priceSum
//             priceCount++; // Increment count of prices
//             i++; // Increment index i for next iteration
//         }

//         // Return the average price
//         // True: priceCount == 0 - 0 is returned (when no proces to average)
//         // False: priceSum / priceCount != 0, expression returned
//         return (priceCount == 0) ? 0 : priceSum / priceCount;
//     }

// }
