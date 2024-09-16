// package codingblackfemales.gettingstarted;

// import static codingblackfemales.action.NoAction.NoAction;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import codingblackfemales.action.Action;
// import codingblackfemales.action.CancelChildOrder;
// import codingblackfemales.action.CreateChildOrder;
// import codingblackfemales.sotw.SimpleAlgoState;
// import codingblackfemales.sotw.marketdata.AskLevel;
// import codingblackfemales.sotw.marketdata.BidLevel;
// import codingblackfemales.util.Util;
// import messages.order.Side;

// public class TestLogic {
//     // Logs messages during execution of the algo
//     private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);


//     @Override
//     // evaluate - decision making
//     // Action - create or cancel orders
//     public Action evaluate(SimpleAlgoState state) {

//         // Add and cancel logic
//         // Loggers run outputs of the current state of the order book
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var orderBookAsString = Util.orderBookToString(state);

//         logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//         var totalOrderCount = state.getActiveChildOrders().size();

//         // Highest buyer price
//         final BidLevel nearTouchBid = state.getBidAt(0);
//         // Lowest seller price
//         final AskLevel nearTouchAsk = state.getAskAt(0);
        
//         long quantity = 75;
//         long bidPrice = nearTouchBid.price;
//         long askPrice = nearTouchAsk.price;

//         // Log the bid and ask price
//         logger.info("[MYALGOLOGIC] Best bid price: " + bidPrice);
//         logger.info("[MYALGOLOGIC] Best ask price: " + askPrice);

//         // Buy if price is low
//         if (totalOrderCount < 5 && askPrice < lowPriceThreshold()) {
//             logger.info("[MYALGOLOGIC] Buying: " + quantity + " @ " + askPrice + " Ask price is low");
//             return new CreateChildOrder(Side.BUY, quantity, askPrice); // Buy at lowest available ask price
//         }

//         // Sell if the price has increased
//         for (var order : state.getActiveChildOrders()) {
//             if (order.getSide() == Side.BUY && bidPrice > order.getPrice() + profitMargin()) {
//                 logger.info("[MYALGOLOGIC] Selling order bought at price: " + order.getPrice() + "for a higher bid of: " + bidPrice);
//                 return new CreateChildOrder(Side.SELL, order.getQuantity(), bidPrice); // Sell at highest available bid price
//             }
//         }

//         // Cancel orders if total order count is more than 20
//         if (totalOrderCount > 20) {
//             for (var order : state.getActiveChildOrders()) {
//                 if (order.getPrice() > nearTouchBid.price) {
//                     logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + " nearTouch is: " + nearTouchBid);
//                     return new CancelChildOrder(order);
//                 }
//             }
//         }
        
//         // No action
//         logger.info("[MYALGOLOGIC] No action taken.");
//         return NoAction;
//     }

//     // Define threshold for buy
//     private long lowPriceThreshold() {
//         return 100; // Buy if price is below 100
//     }

//     // Define profit margin for selling
//     private long profitMargin() {
//         return 10; // Sell if price is 10 units higher than buying
//     }

// }

// // Maximum number of sell
// private static final int maxSellOrders = 3;

// private static final long sellPriceThreshold = 110;

//  // Best price for selling
//  final AskLevel bestAsk = state.getAskAt(0);

// // Sniper strategy to sell when price is high
// if (bestAsk.price >= sellPriceThreshold && state.getChildOrders().size() < maxSellOrders) {
//     logger.info("[MYALGOLOGIC] Selling at price: " + bestAsk.price + ", quantity: " + quantity);
//     return new CreateChildOrder(Side.SELL, quantity, bestAsk.price);
// }


// protected double sellPriceThreshold() {
    //     return 110; // If current market price is more than 110, algo will sell
    // }
        // // Method to define price threshold for low price
        // protected double lowPriceThreshold() {
        // return 104.50; // Buy if the price is below 104.50 (based on mid point in the order book)
    


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
    
