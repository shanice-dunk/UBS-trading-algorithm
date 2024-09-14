package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;

import static codingblackfemales.action.NoAction.NoAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

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

        // Highest buyer price
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
            
        // Cancel any order that are higher than nearTouch price
        // Iterate over active orders and checks if prices are higher than current best bid
        for (var order : activeOrders) {
            if (order.getPrice() != nearTouch.price) {
                logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + " (nearTouch is: " + nearTouch.price + ")");
                // If order is priced too high, algo cancels
                return new CancelChildOrder(order);
                // Ensures algo stays competitive and doesn't have overpriced orders
            }

        }

        // Threshold logic to determine if the price is low
        if (price <= lowPriceThreshold() && state.getChildOrders().size() < 3) {
            logger.info("[MYALGOLOGIC] Price is below or equal to threshold: " + lowPriceThreshold() + ". Buying " + quantity + " @ " + price);
            return new CreateChildOrder(Side.BUY, quantity, price); 
            
        } if (price > lowPriceThreshold()) {
            logger.info("[MYALGOLOGIC] Price is above threshold: " + price + ". Threshold price is: " + lowPriceThreshold());
        }
        
        return NoAction;
        
    }   
        // Method to define price threshold for low price
        protected long lowPriceThreshold() {
        return 100; // Buy if the price is below 100
}

}

// Incorporate passive logic to buy when close to the price
            // If less than 3 buy orders, creates new buy order for quantity outlined at the best bid price
            // Passive = waiting for seller to sell to me
            // Maintain up to 3 child oders
        //      if (state.getChildOrders().size() < 3) {
        //     logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " children, want 3, joining passive side of book with: " + quantity + " @ " + price);
        //     return new CreateChildOrder(Side.BUY, quantity, price);
        // }  else {
        //     logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " children, want 3 done.");
        //     return NoAction; 
        // }  


// Track order book for 5 - 10 changes, find average price, use average to determine price is high or low

// Cancellation logic is first before the order creation logic
// Ensures that if there are any active orders, they are cancelled before checking the number of child orders
