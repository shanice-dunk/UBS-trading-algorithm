package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;
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
        logger.info("[MYALGOADDCANCEL] In My Algo Logic....");

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

        var totalOrderCount = state.getActiveChildOrders().size();

        // Checks total number of child orders that have been created
        // If the number is greater than 20, no action taken
        if (totalOrderCount > 20) {
            return NoAction.NoAction;
        }

        // Retrieves list of active child orders
        final var activeOrders = state.getActiveChildOrders();

        // If there are active orders, cancel 
        if (activeOrders.size() > 0) {
            // stream().findFirst - get first active order
            final var option = activeOrders.stream().findFirst();
            // option.isPresent() - order found, logs that it will cancel order and returns the CancelChildOrder action
            if (option.isPresent()) {
                var childOrder = option.get();
                logger.info("[MYALGOADDCANCEL] Cancelling order:" + childOrder);
                return new CancelChildOrder(childOrder);
            }
            // No action if no order is found
            else {
                return NoAction.NoAction;
            }
        } else {
            // If there are no active orders, create new order
            // Fetches best bid (highest price buyer is willing to pay)
            BidLevel level = state.getBidAt(0);
            // Extracts price and quantity from bid level
            final long price = level.price;
            final long quantity = level.quantity;
            // Logs details of the order and returns CreateChildOrder action to buy specified quantity at specified price
            logger.info("[MYALGOADDCANCEL] Adding order for" + quantity + "@" + price);
            return new CreateChildOrder(Side.BUY, quantity, price);
        }

        // Bid and Ask levels
        // BidLevel bestBid = state.getBidAt(0);
        // AskLevel bestAsk = state.getAskAt(0);

        // Buy low logic 
        // ********* 

        // Sell high logic
        // ********** 

        // No action if no conditions met
        // logger.info("[MYALGOADDCANCEL] No action taken.")
        // return NoAction.NoAction;
    }
}
