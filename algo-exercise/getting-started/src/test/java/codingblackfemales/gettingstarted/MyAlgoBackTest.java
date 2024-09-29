package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.container.Actioner;
import codingblackfemales.container.AlgoContainer;
import codingblackfemales.container.RunTrigger;
import codingblackfemales.orderbook.OrderBook;
import codingblackfemales.orderbook.channel.MarketDataChannel;
import codingblackfemales.orderbook.channel.OrderChannel;
import codingblackfemales.orderbook.consumer.OrderBookInboundOrderConsumer;
import codingblackfemales.sequencer.DefaultSequencer;
import codingblackfemales.sequencer.Sequencer;
import codingblackfemales.sequencer.consumer.LoggingConsumer;
import codingblackfemales.sequencer.net.TestNetwork;
import codingblackfemales.service.MarketDataService;
import codingblackfemales.service.OrderService;
import codingblackfemales.sotw.ChildOrder;
import messages.marketdata.BookUpdateEncoder;
import messages.marketdata.InstrumentStatus;
import messages.marketdata.MessageHeaderEncoder;
import messages.marketdata.Source;
import messages.marketdata.Venue;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.agrona.concurrent.UnsafeBuffer;
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
    
    // private final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
    // private final BookUpdateEncoder encoder = new BookUpdateEncoder();

    protected AlgoContainer container;

        
    @Override
    public Sequencer getSequencer() {
        final TestNetwork network = new TestNetwork();
        final Sequencer sequencer = new DefaultSequencer(network);

        final RunTrigger runTrigger = new RunTrigger();
        final Actioner actioner = new Actioner(sequencer);

        final MarketDataChannel marketDataChannel = new MarketDataChannel(sequencer);
        final OrderChannel orderChannel = new OrderChannel(sequencer);
        final OrderBook book = new OrderBook(marketDataChannel, orderChannel);

        final OrderBookInboundOrderConsumer orderConsumer = new OrderBookInboundOrderConsumer(book);

        container = new AlgoContainer(new MarketDataService(runTrigger), new OrderService(runTrigger), runTrigger, actioner);
        //set my algo logic
        container.setLogic(new MyAlgoLogic());

        network.addConsumer(new LoggingConsumer());
        network.addConsumer(book);
        network.addConsumer(container.getMarketDataService());
        network.addConsumer(container.getOrderService());
        network.addConsumer(orderConsumer);
        network.addConsumer(container);

        return sequencer;
    }

    
   
    @Override
    public AlgoLogic createAlgoLogic() {
        return new MyAlgoLogic();
    }

    protected UnsafeBuffer createTick(){
        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();


        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2000);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        //write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);

        //set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);
        encoder.source(Source.STREAM);

        encoder.bidBookCount(5)
                .next().price(98L).size(100L)
                .next().price(95L).size(200L)
                .next().price(91L).size(300L)
                .next().price(90L).size(400L)
                .next().price(89L).size(500L);

        encoder.askBookCount(5)
                .next().price(99L).size(101L)
                .next().price(100L).size(200L)
                .next().price(101L).size(300L)
                .next().price(103L).size(500L)
                .next().price(104L).size(5000L);
            
        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);

        return directBuffer;
    }

    protected UnsafeBuffer createTick2(){

        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();


        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2000);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        //write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);

        //set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);
        encoder.source(Source.STREAM);

        encoder.bidBookCount(5)
                .next().price(101L).size(100L)
                .next().price(99L).size(200L)
                .next().price(97L).size(300L)
                .next().price(96L).size(400L)
                .next().price(94L).size(500L);

        encoder.askBookCount(5)
                .next().price(101L).size(101L)
                .next().price(103L).size(200L)
                .next().price(104L).size(300L)
                .next().price(105L).size(500L)
                .next().price(110L).size(5000L);

        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);

        return directBuffer;
    }

    protected UnsafeBuffer createTick3(){

        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();


        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2000);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        //write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);

        //set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);
        encoder.source(Source.STREAM);

        encoder.bidBookCount(5)
                .next().price(103L).size(100L)
                .next().price(101L).size(200L)
                .next().price(98L).size(300L)
                .next().price(97L).size(400L)
                .next().price(94L).size(500L);

        encoder.askBookCount(5)
                .next().price(103L).size(101L)
                .next().price(105L).size(200L)
                .next().price(108L).size(300L)
                .next().price(110L).size(500L)
                .next().price(113L).size(5000L);

        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);

        return directBuffer;
    }

    protected UnsafeBuffer createTick4(){

        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();

        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2000);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        //write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);

        //set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);
        encoder.source(Source.STREAM);

        encoder.bidBookCount(5)
                .next().price(105L).size(100L)
                .next().price(103L).size(200L)
                .next().price(102L).size(300L)
                .next().price(100L).size(400L)
                .next().price(99L).size(500L);

        encoder.askBookCount(5)
                .next().price(103L).size(101L)
                .next().price(105L).size(200L)
                .next().price(108L).size(300L)
                .next().price(110L).size(500L)
                .next().price(113L).size(5000L);

        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);

        return directBuffer;
    }


    protected UnsafeBuffer createTick5(){

        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();

        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2000);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        //write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);

        //set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);
        encoder.source(Source.STREAM);

        encoder.bidBookCount(5)
                .next().price(103L).size(100L)
                .next().price(101L).size(200L)
                .next().price(98L).size(300L)
                .next().price(97L).size(400L)
                .next().price(96L).size(500L);

        encoder.askBookCount(5)
                .next().price(101L).size(101L)
                .next().price(103L).size(200L)
                .next().price(105L).size(300L)
                .next().price(107L).size(500L)
                .next().price(110L).size(5000L);

        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);

        return directBuffer;
    }

    @Test
    public void testExampleBackTest() throws Exception {
    // Create first market tick
    send(createTick());

    // Market data moves
    send(createTick2());
    send(createTick3());
    send(createTick4());
    send(createTick5());

    // Get state 
    var state = container.getState();
    // Assert that the algo updated its order based on market movements

    long filledQuantity = state.getChildOrders().stream().map(ChildOrder::getFilledQuantity).reduce(Long::sum).get();
    
    // Check if the algo is creating new orders 
    assertEquals(state.getChildOrders().size(), 5); 

    //and: check that our algo state was updated to reflect our fills when the market data
    assertEquals(0, filledQuantity); // One match
}

}
