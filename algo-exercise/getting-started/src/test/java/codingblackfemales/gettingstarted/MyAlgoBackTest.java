package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.OrderState;
import messages.order.Side;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This test plugs together all of the infrastructure, including the order book (which you can trade against)
 * and the market data feed.
 *
 * If your algo adds orders to the book, they will reflect in your market data coming back from the order book.
 *
 * If you cross the srpead (i.e. you BUY an order with a price which is == or > askPrice()) you will match, and receive
 * a fill back into your order from the order book (visible from the algo in the childOrders of the state object.
 *
 * If you cancel the order your child order will show the order status as cancelled in the childOrders of the state object.
 *
 */
public class MyAlgoBackTest extends AbstractAlgoBackTest {  
    
   
@Override
    public AlgoLogic createAlgoLogic() {
            return new MyAlgoLogic();
    }

    @Test
    public void uptrendDowntrendTest() throws Exception {

        send(createTick());
        send(createTick2());
        send(createTick3());
        send(createTick4());
        send(createTick5());
        send(createTick6());
        send(createTick7());

        //then: get the state
        var state = container.getState();

        // Check filled quantity for BUY
        long buyFilledQuantity = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.BUY).map(ChildOrder::getFilledQuantity).reduce(Long::sum).orElse(0l); // Handles when no buy orders exists

        // Check filled quantity for SELL
        long sellFilledQuantity = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.SELL).map(ChildOrder::getFilledQuantity).reduce(Long::sum).orElse(0l); // Handles when no sell orders exists

        // Check how many buy orders cancelled
        long cancelledBuyOrders = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.BUY).filter(childOrder -> childOrder.getState() == OrderState.CANCELLED).count();

        // Check how many sell orders cancelled
        long cancelledSellOrders = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.SELL).filter(childOrder -> childOrder.getState() == OrderState.CANCELLED).count();

    
        // Print out filled quantities
        System.out.println("[MYALGOTEST] Filled quantity for BUY orders: " + buyFilledQuantity);
        System.out.println("[MYALGOTEST] Filled quantity for SELL orders: " + sellFilledQuantity);
        // Print out cancelled orders
        System.out.println("[MYALGOTEST] Total number of CANCELLED BUY orders: " + cancelledBuyOrders);
        System.out.println("[MYALGOTEST] Total number of CANCELLED SELL orders: " + cancelledSellOrders);

        // Updated filled quantity
        assertEquals(202, buyFilledQuantity);
        assertEquals(0, sellFilledQuantity);
        // // Updated cancelled orders
        assertTrue(cancelledBuyOrders == 0);
        assertTrue(cancelledSellOrders > 0);

    }

    @Test
    public void downtrendUptrendTest() throws Exception {

        send(createTick8());
        send(createTick9());
        send(createTick10());
        send(createTick11());
        send(createTick12());
        send(createTick13());
        send(createTick14());

        //then: get the state
        var state = container.getState();

        // Check filled quantity for BUY
        long buyFilledQuantity = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.BUY).map(ChildOrder::getFilledQuantity).reduce(Long::sum).orElse(0l); // Handles when no buy orders exists

        // Check filled quantity for SELL
        long sellFilledQuantity = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.SELL).map(ChildOrder::getFilledQuantity).reduce(Long::sum).orElse(0l); // Handles when no sell orders exists

        // Check how many buy orders cancelled
        long cancelledBuyOrders = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.BUY).filter(childOrder -> childOrder.getState() == OrderState.CANCELLED).count();

        // Check how many sell orders cancelled
        long cancelledSellOrders = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.SELL).filter(childOrder -> childOrder.getState() == OrderState.CANCELLED).count();

    
        // Print out filled quantities
        System.out.println("[MYALGOTEST] Filled quantity for BUY orders: " + buyFilledQuantity);
        System.out.println("[MYALGOTEST] Filled quantity for SELL orders: " + sellFilledQuantity);
        // Print out cancelled orders
        System.out.println("[MYALGOTEST] Total number of CANCELLED BUY orders: " + cancelledBuyOrders);
        System.out.println("[MYALGOTEST] Total number of CANCELLED SELL orders: " + cancelledSellOrders);

        // Updated filled quantity
        assertEquals(101, buyFilledQuantity);
        assertEquals(0, sellFilledQuantity);
        // // Updated cancelled orders
        assertTrue(cancelledBuyOrders > 0);
        assertTrue(cancelledSellOrders == 0);

    }



}
