/* STORED PREVIOUS LOGIC USED AND TESTED */


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

    // Original code
//         // Add and cancel logic
//         // Loggers run outputs of the current state of the order book
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var orderBookAsString = Util.orderBookToString(state);

//         logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//         var totalOrderCount = state.getActiveChildOrders().size();

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

//     //     // Methods define the price thresholds for when to buy or sell
//     //     // Return values subject to change 
//     // protected double buyPriceThreshold() {
//     //     return 104; // If current market price is less than 104, algo will buy
//     // }

    

// Track order book for 5 - 10 changes, find average price, use average to determine price is high or low

// Cancellation logic is first before the order creation logic
// Ensures that if there are any active orders, they are cancelled before checking the number of child orders

// // Notes and code from MyAlgoLogic
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


// // Trend logic    
// public class MyAlgoLogic<PriceTrend> implements AlgoLogic {
        
//     // Logs messages during execution of the algo
//     private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

//     // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists
//     private final List<Double> nearTouchBidPricesList = new LinkedList<>();
//     private final List<Double> nearTouchAskPricesList = new LinkedList<>();
//     // private final List<Double> farTouchBidPricesList = new LinkedList<>();
//     // private final List<Double> farTouchAskPricesList = new LinkedList<>();

//     // List hold the last 3 values of the bid and ask prices
//     // Small window of price history for analysis
//     private static final int maxPriceHistory = 10;

//     @Override
//     // evaluate - decision making
//     // Action - create or cancel orders
//     public Action evaluate(SimpleAlgoState state) {
//         // Threshold logic
//         // Loggers run outputs of the current state of the order book
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var orderBookAsString = Util.orderBookToString(state);

//         logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//         // var totalOrderCount = state.getChildOrders().size();

//         // Get highest bid price from order book
//         final BidLevel nearTouchBid = state.getBidAt(0);
//         long bidQuantity = 100;
//         long nearBidPrice = nearTouchBid.price;

//         // Get lowest ask price from order book
//         final AskLevel nearTouchAsk = state.getAskAt(0);
//         long askQuantity = 100;
//         long nearAskPrice = nearTouchAsk.price;

//         // // Get lowest bid in the order book
//         // final BidLevel farTouchBid = state.getBidAt(state.getBidLevels() - 1);
//         // long farBidPrice = farTouchBid.price;

//         // // Get highest ask in the order book
//         // final AskLevel farTouchAsk = state.getAskAt(state.getAskLevels() -1);
//         // long farAskPrice = farTouchAsk.price;

//         // Update the near touch bid and ask prices in the lists
//         updatePrices(nearTouchBidPricesList, nearBidPrice);
//         updatePrices(nearTouchAskPricesList, nearAskPrice);
//         // updatePrices(farTouchBidPricesList, farBidPrice);
//         // updatePrices(farTouchAskPricesList, farAskPrice);

//         // Determine trend for both bid and ask prices 
//         // Call getPrice method on updated price lists
//         PriceTrend nearTouchBidTrend = getPrice(nearTouchBidPricesList);
//         PriceTrend nearTouchAskTrend = getPrice(nearTouchAskPricesList);
//         // PriceTrend farTouchBidTrend = getPrice(farTouchBidPricesList);
//         // PriceTrend farTouchAskTrend = getPrice(farTouchAskPricesList);

//         // Logs the trends for the bid and ask prices
//         logger.info("[MYALGOLOGIC] Near touch bid price trend is: " + nearTouchBidTrend);
//         logger.info("[MYALGOLOGIC] Near touch ask price trend is: " + nearTouchAskTrend);
//         // logger.info("[MYALGOLOGIC] Far touch bid price trend is: " + farTouchBidTrend);
//         // logger.info("[MYALGOLOGIC] Far touch ask price trend is: " + farTouchAskTrend);

//         // Retrieve list of current active child orders
//         final var activeOrders = state.getActiveChildOrders();

//         // Cancel if there are 3 active child orders
//         if (activeOrders.size() > 0) {
//             // stream().findFirst - get first active order
//             final var option = activeOrders.stream().findFirst();
//             // option.isPresent() - order found, logs that it will cancel order and returns the CancelChildOrder action
//             if (option.isPresent()) {
//                 var childOrder = option.get();
//                 logger.info("[MYALGOLOGIC] Cancelling order:" + childOrder);
//                 return new CancelChildOrder(childOrder);
//             }
//             // No action if no order is found
//             else{
//                 return NoAction;
//             }
//         }

//         // If less than 5 child orders, create new order based on trend
//         if (state.getChildOrders().size() < 5) {
//             // Decide to buy or sell based on trends
//             // Buy when near touch bid is downward trend 
//             if (nearTouchBidTrend == PriceTrend.DownwardTrend) {
//                 // Create buy order if price is trending up
//                 logger.info("[MYALGOLOGIC] Bid price in downward trend. Place buy order with: " + bidQuantity + " @ " + nearBidPrice);
//                 return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
//             // Sell when the near or far touch ask price is in upward trend
//             } if (nearTouchAskTrend == PriceTrend.UpwardTrend) {
//                 // Create sell order if ask price is trending up
//                 logger.info("[MYALGOLOGIC] Ask price in upward trend. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
//                 return new CreateChildOrder(Side.SELL, askQuantity, nearAskPrice);
//             } else {
//                 // No action if there is no trend
//                 logger.info("[MYALGOLOGIC] No clear price trend, no action required.");
//                 return NoAction;
//             }
//         } else {
//             // If there are 3 child orders, no action
//             logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders, orders complete.");
//             return NoAction;
//         }

//     }
//     // Method maintains a list of most recent prices
//     // If list exceeds maxPriceHistory, remove the oldest price and add new one
//     private void updatePrices(List<Double> priceList, long price) {
//         if (priceList.size() == maxPriceHistory) {
//             priceList.remove(0); // Remove the oldest price
//         }
//         priceList.add((double) price);
//     }

//     // Method to determine if prices are trending up or down
//     private PriceTrend getPrice(List<Double> priceList) {
//         if (priceList.size() < 2) {
//             return PriceTrend.NoTrend; // Not enough data
//         }

//         // Retrieve the most recent prices from price list
//         // Index of the last element
//         Double lastPrice = priceList.get(priceList.size() - 1);
//         // Index od the second to last element 
//         Double previousPrice = priceList.get(priceList.size() - 2);

//         if (lastPrice > previousPrice) {
//             // Returns UpwardTrend if latest price is higher
//             return PriceTrend.UpwardTrend;
//         } else if (lastPrice < previousPrice) {
//             // Returns DownwardTrend of latest price is lower
//             return PriceTrend.DownwardTrend;
//         } else {
//             // Returns NoTrend if price is unchanged or not enough data
//             return PriceTrend.NoTrend;
//         }
//     }

//     // Define enum for trends UpwardTrend, DownwardTrend and NoTrend
//     // enum = enumeration, represents fixed set of named constants 
//     private enum PriceTrend {
//         UpwardTrend, DownwardTrend, NoTrend;
//     }

//     }

// // AVERAGE PRICE WITH THRESHOLD AND LIST LOGIC (tests seem to be working)
// public class MyAlgoLogic implements AlgoLogic {
        
//     // Logs messages during execution of the algo
//     private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

//     // History of average bid and ask prices
//     private final List<Double> avergaeBidPricesList = new LinkedList<>();
//     private final List<Double> averageAskPricesList = new LinkedList<>();
    
//     // List hold the last 10 values of the bid and ask prices
//     // Small window of price history for analysis (constant)
//     private static final int maxPriceHistory = 10;

//     // Thresholds for buy and sell
//     private static final double buyThreshold = 95;
//     private static final double sellThreshold = 98;

//     @Override
//     // evaluate - decision making
//     // Action - create or cancel orders
//     public Action evaluate(SimpleAlgoState state) {
//         // Threshold logic
//         // Loggers run outputs of the current state of the order book
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var orderBookAsString = Util.orderBookToString(state);

//         logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//         // Get highest bid price from order book
//         // Object
//         final BidLevel nearTouchBid = state.getBidAt(0);
//         // Primitive values
//         long bidQuantity = 80;
//         long nearBidPrice = nearTouchBid.price;

//         // Get lowest ask price from order book
//         // Object
//         final AskLevel nearTouchAsk = state.getAskAt(0);
//         // Primitive values
//         long askQuantity = 100;
//         long nearAskPrice = nearTouchAsk.price;


//         // Update the near touch bid and ask prices in the lists
//         // Methods
//         updatePrices(avergaeBidPricesList, nearTouchBid.price);
//         updatePrices(averageAskPricesList, nearTouchAsk.price);
    

//         // Method to calculate average prices
//         long averageBidPrice = (long) calculateAverage(avergaeBidPricesList);
//         long averageAskPrice = (long) calculateAverage(averageAskPricesList);

//         // Log the average prices
//         logger.info("[MYALGOLOGIC] Average BUY price: " + averageBidPrice);
//         logger.info("[MYALGOLOGIC] Average SELL price: " + averageAskPrice);

//         // Retrieve list of current active child orders
//         final var activeOrders = state.getActiveChildOrders();

//         // Cancel order if movedPrice is > 2% (method below)
//         for (var childOrder : activeOrders) {
//             if (movedPrice(state, childOrder) && activeOrders.size() > 5) {
//                 logger.info("[MYALGOLOGIC] Cancelling order due to a significant market price movement: " + childOrder);
//                 return new CancelChildOrder(childOrder);
//             }
//         }

//         // Create child order for buy or sell based on average price
//         if (activeOrders.size() < 5) {
//             if (averageBidPrice < buyThreshold) {
//                 logger.info("[MYALGOLOGIC] Average price is below buy threshold. Placing buy order with: " + bidQuantity + " @ " + nearBidPrice);
//                 return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
//             } else if (averageAskPrice > sellThreshold) {
//                 logger.info("[MYALGOLOGIC] Average price is above sell threshold. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
//                 return new CreateChildOrder(Side.SELL, askQuantity, nearAskPrice);
//             } else {
//                 // No action if there is no trend
//                 logger.info("[MYALGOLOGIC] No clear price trend, no action required.");
//                 return NoAction;
//                 }
//           } else {
//                 // If there are 5 child orders, no action
//                 logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders, orders complete.");
//                         return NoAction;
//                     }
//         }

//         // Method for market price moving significantly
//         private boolean movedPrice(SimpleAlgoState state, ChildOrder childOrder) {
//             double currentMarket = state.getBidAt(0).price;
//             double orderPrice = childOrder.getPrice();
//             double priceDifference = Math.abs(currentMarket - orderPrice) / orderPrice;

//             return priceDifference > 0.02; // Cancel if moved more than 2%
//         }

//         // Method to update price list
//         private void updatePrices(List<Double> priceList, long price) {

//             if (priceList.size() == maxPriceHistory) {
//                 priceList.remove(0); // Remove the oldest price
//             }
//             priceList.add((double) price);
//         }

//         // Method to calculate the average price in list
//         private double calculateAverage(List<Double> priceList) {
//             if (priceList.isEmpty()) {
//                 return 0; // No division by 0
//             }
//             double sum = 0;
//             for (Double price : priceList) {
//                 sum += price;
//             }
//             return sum / priceList.size();
//         }

// }

// // TREND ALGO LOGIC WITH TREND REVERSAL - (tests not working)
// public class MyAlgoLogic implements AlgoLogic {
        
//     // Logs messages during execution of the algo
//     private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

//     // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists (objects)
//     private final List<Double> nearTouchBidPricesList = new LinkedList<>();
//     private final List<Double> nearTouchAskPricesList = new LinkedList<>();
    
//     // List hold the last 10 values of the bid and ask prices
//     // Small window of price history for analysis (constant)
//     private static final int maxPriceHistory = 10;

//     // Moving average period for trend reversal
//     private static final int movingAverage = 5;

//     @Override
//     // evaluate - decision making
//     // Action - create or cancel orders
//     public Action evaluate(SimpleAlgoState state) {
//         // Threshold logic
//         // Loggers run outputs of the current state of the order book
//         logger.info("[MYALGOLOGIC] In My Algo Logic....");

//         var orderBookAsString = Util.orderBookToString(state);

//         logger.info("[MYALGOLOGIC] The state of the order book is:\n" + orderBookAsString);

//         // Get highest bid price from order book
//         // Object
//         final BidLevel nearTouchBid = state.getBidAt(0);
//         // Primitive values
//         long bidQuantity = 100;
//         long nearBidPrice = nearTouchBid.price;

//         // Get lowest ask price from order book
//         // Object
//         final AskLevel nearTouchAsk = state.getAskAt(0);
//         // Primitive values
//         long askQuantity = 100;
//         long nearAskPrice = nearTouchAsk.price;


//         // Update the near touch bid and ask prices in the lists
//         // Methods
//         updatePrices(nearTouchBidPricesList, nearBidPrice);
//         updatePrices(nearTouchAskPricesList, nearAskPrice);
        
//         // Determine trend for both bid and ask prices 
//         // Call getPrice method on updated price lists
//         // Methods
//         PriceTrend nearTouchBidTrend = getPrice(nearTouchBidPricesList);
//         PriceTrend nearTouchAskTrend = getPrice(nearTouchAskPricesList);
        

//         // Logs the trends for the bid and ask prices
//         logger.info("[MYALGOLOGIC] Near touch bid price trend is: " + nearTouchBidTrend);
//         logger.info("[MYALGOLOGIC] Near touch ask price trend is: " + nearTouchAskTrend);

//         // Retrieve list of current active child orders
//         final var activeOrders = state.getActiveChildOrders();

//         // Cancel active orders based on trend
//         for (var childOrder : activeOrders) {
//             // Cancel BUY order if market trend = UPWARD
//             if (childOrder.getSide() == Side.BUY && nearTouchBidTrend == PriceTrend.UpwardTrend) {
//                 logger.info("[MYALGOLOGIC] Market trend is UPWARD, cancelling BUY order: " + childOrder);
//                 return new CancelChildOrder(childOrder);
//                 // Cancel SELL order if market trend = DOWNWARD
//             } else if (childOrder.getSide() == Side.SELL && nearTouchAskTrend == PriceTrend.DownwardTrend) {
//                 logger.info("[MYALGOLOGIC] Market trend is DOWNWARD, cancelling SELL order: " + childOrder);
//                 return new CancelChildOrder(childOrder);
//             }
//         }

//         // If less than 5 child orders, create new order based on trend
//         if (state.getChildOrders().size() < 5) {
//             // Decide to buy or sell based on trends
//             // Buy when price is reversing from downward to upward 
//             if (nearTouchBidTrend == PriceTrend.DownwardTrend && buyReversal(nearTouchBidPricesList)) {
//                 // Create buy order if price is trending up
//                 logger.info("[MYALGOLOGIC] Market currently in DOWNWARD trend. Place buy order with: " + bidQuantity + " @ " + nearBidPrice);
//                 return new CreateChildOrder(Side.BUY, bidQuantity, nearBidPrice);
//             // Sell when the near touch ask price is in upward trend
//             } if (nearTouchAskTrend == PriceTrend.UpwardTrend && sellReversal(nearTouchAskPricesList)) {
//                 // Create sell order if ask price is trending up
//                 logger.info("[MYALGOLOGIC] Market currently in UPWARD trend. Placing sell order with: " + askQuantity + " @ " + nearAskPrice);
//                 return new CreateChildOrder(Side.SELL, askQuantity, nearAskPrice);
//             } else {
//                 // No action if there is no trend
//                 logger.info("[MYALGOLOGIC] No clear price trend, no action required.");
//                 return NoAction;
//             }
//         } else {
//             // If there are 5 child orders, no action
//             logger.info("[MYALGOLOGIC] Have: " + state.getChildOrders().size() + " child orders, orders complete.");
//             return NoAction;
//         }

//     }
//     // Method maintains a list of most recent prices
//     // If list exceeds maxPriceHistory, remove the oldest price and add new one
//     private void updatePrices(List<Double> priceList, double price) {
//         if (priceList.size() == maxPriceHistory) {
//             priceList.remove(0); // Remove the oldest price (FIFO)
//         }
//         priceList.add((double) price);
//     }

//     // Check if price list is null

//     // Method to determine if prices are trending up, down or no trend
//     // Add null check to make sure price list is being updated
//     private PriceTrend getPrice(List<Double> priceList) {
//         if (priceList == null) {
//             logger.error("[MYALGOLOGIC] Price list is null.");
//             return null;
//         }

//         if (priceList.size() < 4) {
//             logger.warn("[Not enough data in the price list.]");
//             return PriceTrend.NoTrend; // Not enough data
//         }

//         // Retrieve the most recent prices from price list
//         // Index of the last element
//         Double lastPrice = priceList.get(priceList.size() - 1);
//         // Index of the second to last element 
//         Double secondLastPrice = priceList.get(priceList.size() - 2);
//         // Index of third to last price
//         Double thirdLastPrice = priceList.get(priceList.size() - 3);

//         if (lastPrice > secondLastPrice && thirdLastPrice < secondLastPrice) {
//             // Returns UpwardTrend if latest price is higher
//             return PriceTrend.UpwardTrend;
//         } else if (lastPrice < secondLastPrice && thirdLastPrice > secondLastPrice) {
//             // Returns DownwardTrend of latest price is lower
//             return PriceTrend.DownwardTrend;
//         } else {
//             // Returns NoTrend if price is unchanged or not enough data
//             return PriceTrend.NoTrend;
//         }
//     }

//     // Method to determine if trend is reversing using moving average period
//      // Identify trend reversal for a BUY signal (downward to upward)
//      private boolean buyReversal(List<Double> priceList) {
//         if (priceList.size() < movingAverage + 1) {
//             return false; // Not enough data for moving average
//         }

//         double recentAverage = getMovingAverage(priceList, priceList.size() - movingAverage, priceList.size());
//         double previousAverage = getMovingAverage(priceList, priceList.size() - 2 * movingAverage, priceList.size() - movingAverage);

//         return recentAverage > previousAverage; // Confirm upward trend reversal
//     }

//     // Identify trend reversal for a SELL signal (upward to downward)
//     private boolean sellReversal(List<Double> priceList) {
//         if (priceList.size() < movingAverage + 1) {
//             return false; // Not enough data for moving average
//         }

//         double recentAverage = getMovingAverage(priceList, priceList.size() - movingAverage, priceList.size());
//         double previousAverage = getMovingAverage(priceList, priceList.size() - 2 * movingAverage, priceList.size() - movingAverage);

//         return recentAverage < previousAverage; // Confirm downward trend reversal
//     }

//     // Calculate moving average of the price list over a given range
//     private double getMovingAverage(List<Double> priceList, int start, int end) {
//         return priceList.subList(start, end).stream()
//                 .mapToDouble(Double::doubleValue)
//                 .average()
//                 .orElse(0);
//     }

//     // Define enum for trends UpwardTrend, DownwardTrend and NoTrend
//     // enum = enumeration, represents fixed set of named constants 
//     private enum PriceTrend {
//         UpwardTrend, DownwardTrend, NoTrend;
//     }

//     }

/* // TREND ALGO LOGIC (tests are working)
public class MyAlgoLogic implements AlgoLogic {
        
    // Logs messages during execution of the algo
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // History of highest bid prices and lowest ask price to analyse trends which are stored in two lists (objects)
    private final List<Double> nearTouchBidPricesList = new LinkedList<>();
    private final List<Double> nearTouchAskPricesList = new LinkedList<>();
    
    // List hold the last 10 values of the bid and ask prices
    // Small window of price history for analysis (constant)
    private static final int maxPriceHistory = 5;

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
        updateBidPrices(nearTouchBidPricesList, nearBidPrice);
        updateAskPrices(nearTouchAskPricesList, nearAskPrice);
        
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
    private void updateBidPrices(List<Double> priceList, double price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price (FIFO)
        }
        priceList.add((double) price);

        // Log current price in list
        logger.info("[MYALGOLOGIC] Current BUY price list: " + nearTouchBidPricesList.toString());
    }

    private void updateAskPrices(List<Double> priceList, double price) {
        if (priceList.size() == maxPriceHistory) {
            priceList.remove(0); // Remove the oldest price (FIFO)
        }
        priceList.add((double) price);

        // Log current price in list
        logger.info("[MYALGOLOGIC] Current SELL price list: " + nearTouchAskPricesList.toString());
    }

    // Method for percentage trend
    private PriceTrend getPrice(List<Double> priceList) {
        if (priceList == null) {
            logger.error("[MYALGOLOGIC] Price list is null.");
            return null;
        }

        // Ensure at least X prices to analyse trend
        if (priceList.size() < 3) {
            logger.warn("[MYALGOLOGIC] Not enough data to determine a price trend.");
            return PriceTrend.NoTrend;
        }

  

    // // Method to determine if prices are trending up, down or no trend
    // // Add null check to make sure price list is being updated
    // private PriceTrend getPrice(List<Double> priceList) {
    //     if (priceList == null) {
    //         logger.error("[MYALGOLOGIC] Price list is null.");
    //         return null;
    //     }
        
    //     // Ensure there are at least 4 prices to analyse the trend
    //     if (priceList.size() < 4) {
    //         logger.warn("[Not enough data in the price list.]");
    //         return PriceTrend.NoTrend; // Not enough data
    //     }

    //     // Retrieve the most recent prices from price list
    //     // Index of the last element
    //     Double lastPrice = priceList.get(priceList.size() - 1);
    //     // Index of the second to last element 
    //     Double secondLastPrice = priceList.get(priceList.size() - 2);
    //     // Index of third to last price
    //     Double thirdLastPrice = priceList.get(priceList.size() - 3);

    //     if (lastPrice > secondLastPrice && thirdLastPrice < secondLastPrice) {
    //         // Returns UpwardTrend if latest price is higher
    //         return PriceTrend.UpwardTrend;
    //     } else if (lastPrice < secondLastPrice && thirdLastPrice > secondLastPrice) {
    //         // Returns DownwardTrend of latest price is lower
    //         return PriceTrend.DownwardTrend;
    //     } else {
    //         // Returns NoTrend if price is unchanged or not enough data
    //         return PriceTrend.NoTrend;
    //     }
    // }

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

} */