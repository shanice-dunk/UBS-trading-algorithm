package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.container.Actioner;
import codingblackfemales.container.AlgoContainer;
import codingblackfemales.container.RunTrigger;
import codingblackfemales.sequencer.DefaultSequencer;
import codingblackfemales.sequencer.Sequencer;
import codingblackfemales.sequencer.consumer.LoggingConsumer;
import codingblackfemales.sequencer.net.TestNetwork;
import codingblackfemales.service.MarketDataService;
import codingblackfemales.service.OrderService;
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

    protected AlgoContainer container;

    @Override
    public Sequencer getSequencer() {
        final TestNetwork network = new TestNetwork();
        final Sequencer sequencer = new DefaultSequencer(network);

        final RunTrigger runTrigger = new RunTrigger();
        final Actioner actioner = new Actioner(sequencer);

        container = new AlgoContainer(new MarketDataService(runTrigger), new OrderService(runTrigger), runTrigger, actioner);
        //set my algo logic
        container.setLogic(createAlgoLogic());

        network.addConsumer(new LoggingConsumer());
        network.addConsumer(container.getMarketDataService());
        network.addConsumer(container.getOrderService());
        network.addConsumer(container);

        return sequencer;
    }

    @Override
    public AlgoLogic createAlgoLogic() {
        //this adds your algo logic to the container classes
        return new MyAlgoLogic();
    }

    protected UnsafeBuffer createTick(){

        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();

        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        //write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);

        //set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);

        encoder.bidBookCount(5)
                .next().price(98L).size(100L)
                .next().price(95L).size(200L)
                .next().price(91L).size(300L)
                .next().price(90L).size(400L)
                .next().price(89L).size(500L);

        encoder.askBookCount(5)
                .next().price(100L).size(101L)
                .next().price(105L).size(200L)
                .next().price(107L).size(300L)
                .next().price(109L).size(500L)
                .next().price(110L).size(5000L);
            
        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);
        encoder.source(Source.STREAM);

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
                .next().price(100L).size(100L)
                .next().price(97L).size(200L)
                .next().price(95L).size(300L)
                .next().price(93L).size(400L)
                .next().price(91L).size(500L);

        encoder.askBookCount(5)
                .next().price(103L).size(101L)
                .next().price(104L).size(200L)
                .next().price(106L).size(300L)
                .next().price(108L).size(500L)
                .next().price(115L).size(5000L);

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
                .next().price(106L).size(101L)
                .next().price(108L).size(200L)
                .next().price(110L).size(300L)
                .next().price(111L).size(500L)
                .next().price(115L).size(5000L);

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
                .next().price(104L).size(100L)
                .next().price(102L).size(200L)
                .next().price(100L).size(300L)
                .next().price(98L).size(400L)
                .next().price(97L).size(500L);

        encoder.askBookCount(5)
                .next().price(108L).size(101L)
                .next().price(109L).size(200L)
                .next().price(110L).size(300L)
                .next().price(111L).size(500L)
                .next().price(115L).size(5000L);

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
                .next().price(106L).size(100L)
                .next().price(103L).size(200L)
                .next().price(101L).size(300L)
                .next().price(99L).size(400L)
                .next().price(96L).size(500L);

        encoder.askBookCount(5)
                .next().price(110L).size(101L)
                .next().price(112L).size(200L)
                .next().price(115L).size(300L)
                .next().price(117L).size(500L)
                .next().price(118L).size(5000L);

        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);

        return directBuffer;
    }


    @Test
    public void testDispatchThroughSequencer() throws Exception {

        //create a sample market data tick....
        send(createTick());
        send(createTick2());
        send(createTick3());
        send(createTick4());
        send(createTick5());

        //simple assert to check we had 3 orders created
        assertEquals(container.getState().getChildOrders().size(), 4); // 4 child orders as no child order created for first tick
    }

}
