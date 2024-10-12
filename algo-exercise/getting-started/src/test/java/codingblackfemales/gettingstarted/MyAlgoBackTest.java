package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import messages.order.Side;

import static org.junit.Assert.assertEquals;
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
    public void testExampleBackTest() throws Exception {

        send(createTick());
        send(createTick2());
        
        // No orders created as not enough data after first 2 ticks
        assertEquals(container.getState().getChildOrders().size(), 0);

        // Market changes
        send(createTick3());
        send(createTick4());
        send(createTick5());
        send(createTick6());
        send(createTick7());
        send(createTick8());
        send(createTick9());
        // send(createTick10());

        // Number of child orders created depending on market trend
        assertEquals(container.getState().getChildOrders().size(), 4);

        //then: get the state
        var state = container.getState();

        // Check filled quantity for BUY
        long buyFilledQuantity = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.BUY).map(ChildOrder::getFilledQuantity).reduce(Long::sum).orElse(0l); // Handles when no buy orders exists

        // Check filled quantity for SELL
        long sellFilledQuantity = state.getChildOrders().stream().filter(childOrder -> childOrder.getSide() == Side.SELL).map(ChildOrder::getFilledQuantity).reduce(Long::sum).orElse(0l); // Handles when no sell orders exists
    
        // Print out filled quantities
        System.out.println("[MYALGOTEST] Filled quantity for BUY orders: " + buyFilledQuantity);
        System.out.println("[MYALGOTEST] Filled quantity for SELL orders: " + sellFilledQuantity);

        // Updated filled quantity
        assertEquals(0, buyFilledQuantity);
        assertEquals(0, sellFilledQuantity);

    
    // //Check things like filled quantity, cancelled order count etc....
    // long filledQuantity = state.getChildOrders().stream().map(ChildOrder::getFilledQuantity).reduce(Long::sum).get();
    
    // //and: check that our algo state was updated to reflect our fills when the market data
    // assertEquals(200, filledQuantity);
    }

}
