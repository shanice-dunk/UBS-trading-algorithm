package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This test is designed to check your algo behavior in isolation of the order book.
 *
 * You can tick in market data messages by creating new versions of createTick() (ex. createTick2, createTickMore etc..)
 *
 * You should then add behaviour to your algo to respond to that market data by creating or cancelling child orders.
 *
 * When you are comfortable you algo does what you expect, then you can move on to creating the MyAlgoBackTest.
 *
 */
public class MyAlgoTest extends AbstractAlgoTest {

    @Override
    public AlgoLogic createAlgoLogic() {
            return new MyAlgoLogic();
    }



    @Test
    public void testDispatchThroughSequencer() throws Exception {

        //create a sample market data tick....
        send(createTick());

        // No orders created as not enough data after first tick
        assertEquals(container.getState().getChildOrders().size(), 0);

        // Market changes
        send(createTick2());
        send(createTick3());
        send(createTick4());
        send(createTick5());
        send(createTick6());
        send(createTick7());
        send(createTick8());
        send(createTick9());
        send(createTick10());

        // Number of child orders created depending on market trend
        assertEquals(container.getState().getChildOrders().size(), 2);


        
    }

}
