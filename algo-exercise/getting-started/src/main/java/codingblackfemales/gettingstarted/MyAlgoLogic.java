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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {


    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // Maximum number of buy orders
    private static final int maxBuyOrders = 5;

    // Thresholds for buying and selling
    // Constant field that defines fixed value that doesn't change
    private static final long buyPriceThreshold = 110;
    


    @Override
    // evaluate - decision making
    // Action - create or cancel orders
    public Action evaluate(SimpleAlgoState state) {
       logger.info("[MYALGOLOGIC] In My Algo Logic...");

       var orderBookAsString = Util.orderBookToString(state);

       logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

       var totalOrderCount = state.getActiveChildOrders().size();

       long quantity = 100;

       // Prevent over-trading by limiting the number of active orders
       if (totalOrderCount > 15) {
        return NoAction;
       }

    //    // Retrieves list of active child orders
    //    final var activeOrders = state.getActiveChildOrders();
    //    // Cancel active orders
    //    if (activeOrders.size() > 0) {
    //     // Get first active order
    //     final var option = activeOrders.stream().findFirst();
    //     // Order found, cancel order and return CancelChildOrder action
    //     if (option.isPresent()) {
    //         var childOrder = option.get();
    //         logger.info("[MYALGOLOGIC] Cancelling order: " + childOrder);
    //         return new CancelChildOrder(childOrder);
    //     }
    
    // }

       // Cancel orders price above the buyPriceThreshold
       for (var childOrder : state.getActiveChildOrders()) {
        if (childOrder.getPrice() > buyPriceThreshold) {
            logger.info("[MYALGOLOGIC] Cancelling order at price: " + childOrder.getPrice() + ". Buy threshold is: " + buyPriceThreshold);
            return new CancelChildOrder(childOrder);
        }
       }

       // Calculate the average price of all bids and asks
       long averagePrice = calculateAveragePrice(state);

       // Log the average price
       logger.info("[MYALGOLOGIC] Average price is: " + averagePrice);

       // Place child order if average price is <= to buy threshold
       // And if max buy orders have not been reached
       if (averagePrice <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
        logger.info("[MYALGOLOGIC] Average is is below or equal to buy threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + averagePrice);
        logger.info("[MYALGOLOGIC] Total profit to be made: £" + (buyPriceThreshold - averagePrice));
        logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders completed, want " + maxBuyOrders);
        return new CreateChildOrder(Side.BUY, quantity, averagePrice);
       }

       logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders completed, wanted " + maxBuyOrders + ", no action required.");
       return NoAction;

    }

    // Method: calculate the average price of all bid and ask levels
    private long calculateAveragePrice(SimpleAlgoState state) {
        long priceSum = 0;
        int priceCount = 0;

        // Index for bid and ask levels
        int i = 0;

        // While loop to sum bid prices
        // Continue loop while i < total number of bid levels
        while (i < state.getBidLevels()) {
            BidLevel bid = state.getBidAt(i); // Get bid levels at index i
            priceSum += bid.price; // Add bid price to priceSum
            priceCount++; // Increment count of prices
            i++; // Increment index i for next iteration
        }

        // Reset index to 0 to iterate through ask levels
        i = 0;

        // While loop to sum ask prices
        // Continue loop while i < total number of ask levels
        while (i < state.getAskLevels()) {
            AskLevel ask = state.getAskAt(i); // Get ask level at index i
            priceSum += ask.price; // Add ask price to priceSum
            priceCount++; // Increment count of prices
            i++; // Increment index i for next iteration
        }

        // Return the average price
        // True: priceCount == 0 - 0 is returned (when no proces to average)
        // False: priceSum / priceCount != 0, expression returned
        return (priceCount == 0) ? 0 : priceSum / priceCount;
    }

}
