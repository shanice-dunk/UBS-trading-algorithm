package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
// import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
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

    // Thresholds for buying and selling
    // Constant field that defines fixed value that doesn't change
    private static final long buyPriceThreshold = 100;
    


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
        
        long quantity = 100;
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
            }

        }
        // LOGIC 1 = threshold
        // Threshold logic to determine if the price is low
        // Passive strategy when price is low
        if (price <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
            logger.info("[MYALGOLOGIC] Price is below or equal to threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + price);
            logger.info("[MYALGOLOGIC] Profit made based on buy threshold: " + (buyPriceThreshold - price));
            return new CreateChildOrder(Side.BUY, quantity, price);     
        } 
        // No action if all child orders done
        logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders done, wanted" + maxBuyOrders + ", no action needed.");
            return NoAction;
        
    }   

}

    //     // Methods define the price thresholds for when to buy or sell
    //     // Return values subject to change 
    // protected double buyPriceThreshold() {
    //     return 104; // If current market price is less than 104, algo will buy
    // }

    

// Track order book for 5 - 10 changes, find average price, use average to determine price is high or low

// Cancellation logic is first before the order creation logic
// Ensures that if there are any active orders, they are cancelled before checking the number of child orders
