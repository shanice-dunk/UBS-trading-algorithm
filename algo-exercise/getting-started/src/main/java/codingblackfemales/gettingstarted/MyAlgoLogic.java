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

        final BidLevel nearTouch = state.getBidAt(0);
        long quantity = 75;
        long price = nearTouch.price;
        

        // Checks total number of child orders that have been created
        // If the number is greater than 20, no action taken
         if (totalOrderCount > 20) {
            return NoAction;
         }
        // Retrieves list of active child orders
        final var activeOrders = state.getActiveChildOrders();
            
        // Cancel any order that is not at the nearTouch price
        for (var order : activeOrders) {
            if (order.getPrice() != nearTouch.price) {
                logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + " (nearTouch is: " + nearTouch.price + ")");
                return new CancelChildOrder(order);
            }
        }
            // Incorporate passive logic to buy when close to the price
             if (state.getChildOrders().size() < 3) {
            logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " children, want 3, joining passive side of book with: " + quantity + " @ " + price);
            return new CreateChildOrder(Side.BUY, quantity, price);
        }  else {
            logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " children, want 3 done.");
            return NoAction; 
        }  
    }
}

// Track order book for 5 - 10 changes, find average price, use average to determine price is high or low

// Cancellation logic is first before the order creation logic
// Ensures that if there are any active orders, they are cancelled before checking the number of child orders
