// package codingblackfemales.gettingstarted;

// import codingblackfemales.action.Action;
// import codingblackfemales.action.CancelChildOrder;
// import codingblackfemales.action.CreateChildOrder;
// import codingblackfemales.algo.AlgoLogic;
// import codingblackfemales.sotw.SimpleAlgoState;
// import codingblackfemales.sotw.marketdata.BidLevel;
// import codingblackfemales.sotw.marketdata.AskLevel;
// import codingblackfemales.util.Util;
// import messages.order.Side;

// import static codingblackfemales.action.NoAction.NoAction;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// // public class BuyLogic implements AlgoLogic {
// // Average price logic
//     private static final Logger logger = LoggerFactory.getLogger(BuyLogic.class);

//     // Maximum number of buy orders
//     private static final int maxBuyOrders = 3;

//     // Threshold for buying
//     private static final long buyPriceThreshold = 95;

//     @Override
//     public Action evaluate(SimpleAlgoState state) {
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var totalOrderCount = state.getActiveChildOrders().size();
//         long quantity = 100;

//         // Prevent over-trading by limiting the number of active orders
//         if (totalOrderCount > 20) {
//             return NoAction;
//         }

//         // Cancel any orders priced above the buyPriceThreshold
//         for (var order : state.getActiveChildOrders()) {
//             if (order.getPrice() > buyPriceThreshold) {
//                 logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + ". Threshold is: " + buyPriceThreshold);
//                 return new CancelChildOrder(order);
//             }
//         }

//         // Calculate the average price of all bids and asks
//         long averagePrice = calculateAveragePrice(state);

//         // Log the average price
//         logger.info("[MYALGOLOGIC] Average price calculated: " + averagePrice);

//         // Place order if average price is <= to buy threshold
//         // And if we haven't reached the maximum number of buy orders
//         if (averagePrice <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
//             logger.info("[MYALGOLOGIC] Average price is below or equal to threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + averagePrice);
//             return new CreateChildOrder(Side.BUY, quantity, averagePrice);
//         }

//         logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders done, wanted " + maxBuyOrders + ", no action needed.");
//         return NoAction;
//     }

//     /**
//      * Calculate the average price of all bid and ask levels
//      */
//     private long calculateAveragePrice(SimpleAlgoState state) {
//         long totalPriceSum = 0;
//         int totalPriceCount = 0;

//         // Sum bid prices
//         for (int i = 0; i < state.getBidLevels().size(); i++) {
//             totalPriceSum += state.getBidAt(i).price;
//             totalPriceCount++;
//         }

//         // Sum ask prices
        // for (int i = 0; i < state.getAskLevels().size(); i++) {
        //     totalPriceSum += state.getAskAt(i).price;
        //     totalPriceCount++;
        // }

//         // Avoid division by zero in case no bids or asks are present
//         if (totalPriceCount == 0) {
//             logger.info("[MYALGOLOGIC] No valid bids or asks, no action taken.");
//             return 0;
//         }

//         // Return the average price
//         return totalPriceSum / totalPriceCount;
//     }
// }

// Mid point logic
// public class BuyLogic implements AlgoLogic {
//     private static final Logger logger = LoggerFactory.getLogger(BuyLogic.class);

//     // Maximum number of buy orders
//     private static final int maxBuyOrders = 3;

//     // Thresholds for buying and selling
//     // Constant field that defines fixed value that doesn't change
//     private static final long buyPriceThreshold = 95;

// //     @Override
// //     public Action evaluate(SimpleAlgoState state) {
// //         // Add and cancel logic
//         // Loggers run outputs of the current state of the order book
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var orderBookAsString = Util.orderBookToString(state);

//         logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//         var totalOrderCount = state.getActiveChildOrders().size();

//         // Fixed quantity
//         long quantity = 100;

//         // Checks total number of child orders that have been created
//         // If the number is greater than 20, no action taken
//         // This prevents over-trading by limiting number of active orders
//          if (totalOrderCount > 20) {
//             return NoAction;
//          }


//         // Retrieves list of active child orders
//         final var activeOrders = state.getActiveChildOrders();

//         for (var order : activeOrders) {
//             if (order.getPrice() > buyPriceThreshold) {
//                 logger.info("[MYALGOLOGIC] Cancelling order at price: " + order.getPrice() + ". Threshold is: " + buyPriceThreshold);
//                 return new CancelChildOrder(order);
//             }
//         }

//         // Best bid ans ask price from order book
//         final BidLevel bestBid = state.getBidAt(0);
//         final AskLevel bestAsk = state.getAskAt(0);

//         // Midpoint price between best bid and best ask
//         long midPrice = (bestBid.price + bestAsk.price) / 2;

//       // If there are no bids or asks in order book, no action
//       if (bestBid == null || bestAsk == null) {
//         logger.info("[MYALGOLOGIC] Best bid or best ask is null, no action needed.");
//         return NoAction;
//       }

//       // Log midpoint
//       logger.info("[MYALGOLOGIC] Best bid: " + bestBid.price + ", Best ask: " + bestAsk.price + ". Midpoint price: " + midPrice);

//       // Place order if midprice is <= to buy threshold
//       // And not reached max buy orders

//         if (midPrice <= buyPriceThreshold && state.getChildOrders().size() < maxBuyOrders) {
//             logger.info("[MYALGOLOGIC] Midpoint price is below or equal to threshold: " + buyPriceThreshold + ". Buying " + quantity + " @ " + midPrice);
//             return new CreateChildOrder(Side.BUY, quantity, midPrice);
//         }


//         logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders done, wanted " + maxBuyOrders + ", no action needed.");
//         return NoAction;

//     }