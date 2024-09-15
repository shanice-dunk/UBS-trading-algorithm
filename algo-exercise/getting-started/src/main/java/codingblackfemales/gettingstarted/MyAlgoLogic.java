package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.util.Util;
import messages.order.Side;

import static codingblackfemales.action.NoAction.NoAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // Maximum number of buy orders
    private static final int maxBuyOrders = 3;

    // Maximum number of sell
    private static final int maxSellOrders = 3;

    // Thresholds for buying and selling
    // Constant field that defines fixed value that doesn't change
    private static final long buyPriceThreshold = 104;
    private static final long sellPriceThreshold = 110;
    


    @Override
    // evaluate - decision making
    // Action - create or cancel orders
    public Action evaluate(SimpleAlgoState state) {

        // Add and cancel logic
        // Loggers run outputs of the current state of the order book
        logger.info("[MYALGOLOGIC] In My Algo Logic....");

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

        var totalOrderCount = state.getActiveChildOrders().size();

        // Best bid price for buying
        final BidLevel nearTouch = state.getBidAt(0);
        // Best price for selling
        final AskLevel bestAsk = state.getAskAt(0);
        
        long quantity = 50;
        long price = nearTouch.price;
    

        // Checks total number of child orders that have been created
        // If the number is greater than 20, no action taken
        // This prevents over-trading by limiting number of active orders
         if (totalOrderCount > 20) {
            return NoAction;
         }
        // Retrieves list of active child orders
        final var activeOrders = state.getActiveChildOrders();
            
        // for loop to iterates over active orders and checks if prices are higher than order. If so, order is cancelled
        for (var order : activeOrders) {
            if (order.getPrice() > nearTouch.price) {
                logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + " (nearTouch is: " + nearTouch.price + ")");
                // If order is priced too high, algo cancels
                return new CancelChildOrder(order);
                // Ensures algo stays competitive and doesn't have overpriced orders
            }

        }
        // LOGIC 1 = threshold
        // Threshold logic to determine if the price is low
        // Passive strategy when price is low
        if (price <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
            logger.info("[MYALGOLOGIC] Price is below or equal to threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + price);
            return new CreateChildOrder(Side.BUY, quantity, price);     
        } 
        // Sniper strategy to sell when price is high
        if (bestAsk.price >= sellPriceThreshold && state.getChildOrders().size() < maxSellOrders) {
            logger.info("[MYALGOLOGIC] Selling at price: " + bestAsk.price + ", quantity: " + quantity);
            return new CreateChildOrder(Side.SELL, quantity, bestAsk.price);
        }
        
        logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " children done, wanted " + maxBuyOrders + ", order completed.");
            return NoAction;
        
    }   

    //     // Methods define the price thresholds for when to buy or sell
    //     // Return values subject to change 
    // protected double buyPriceThreshold() {
    //     return 104; // If current market price is less than 104, algo will buy
    // }

    // protected double sellPriceThreshold() {
    //     return 110; // If current market price is more than 110, algo will sell
    // }
        // // Method to define price threshold for low price
        // protected double lowPriceThreshold() {
        // return 104.50; // Buy if the price is below 104.50 (based on mid point in the order book)
    }


// // LOGIC 2 = price levels
// // Place multiple buy orders at different price levels
// if (state.getChildOrders().size() < maxBuyOrders) {
//     logger.info("[MYALGOLOGIC] Placing multiple buy orders.");

//     // Defining multiple price levels for buy orders in array
//     // Buy at current price and two at lower price
//     long[] priceLevels = {price, price - 3, price - 4};

//     // for loop to iterate through the price levels and place buy orders
//     for (long levelPrice : priceLevels) {
//         // Ensure number of buy orders stays within limit
//         if (state.getChildOrders().size() < maxBuyOrders) {
//             logger.info("[MYALGOLOGIC] Placing buy order for " + quantity + " @ " + levelPrice);
//             return new CreateChildOrder(Side.BUY, quantity, levelPrice);
//         }
//     }
// } 
// logger.info("[MYALGOLOGIC] No action taken.");
// return NoAction;

// }

// }


// Track order book for 5 - 10 changes, find average price, use average to determine price is high or low

// Cancellation logic is first before the order creation logic
// Ensures that if there are any active orders, they are cancelled before checking the number of child orders
