package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Class to test that CPTCode and CPTCodeForm are created from each other
 * properly.
 *
 * @author Parikshit Patel
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CPTCodeTest {

    @Test
    @Transactional
    public void testCPTCodes () {

        final CPTCodeForm form1 = new CPTCodeForm();
        form1.setCode( "99292" );
        form1.setDescription( "Kidney Surgery to buy iPhone" );
        form1.setId( 1L );
        form1.setCost( 100000.00 );
        form1.setTimeFrame( "15-29 minutes" );

        final CPTCode base = new CPTCode();
        base.setCode( "99292" );
        base.setDescription( "Kidney Surgery to buy iPhone" );
        base.setId( 1L );
        base.setCost( 100000.00 );
        base.setTimeFrame( "15-29 minutes" );

        final CPTCode code = new CPTCode( form1 );
        assertEquals( code, base );

        final CPTCodeForm form2 = new CPTCodeForm( base );
        assertEquals( form1, form2 );

        assertEquals( "99292", code.getCode() );
        assertTrue( code.getId().equals( 1L ) );
        assertEquals( "Kidney Surgery to buy iPhone", code.getDescription() );
        assertEquals( (Double) 100000.00, code.getCost() );
        assertEquals( "15-29 minutes", code.getTimeFrame() );

    }

    @Test
    @Transactional
    public void testSetTimeFrame () {
        // Testing setTimeFrame of CPTCodeForm
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "99215" );
        form.setDescription( "Another Kidney Surgery to buy another iPhone" );
        form.setId( 2L );
        form.setCost( 400000.00 );
        form.setTimeFrame( "50-74 minutes" );

        assertEquals( "50-74 minutes", form.getTimeFrame() );

        form.setTimeFrame( "60-74 minutes" );
        assertEquals( "60-74 minutes", form.getTimeFrame() );

        try {
            form.setTimeFrame( "30-40 units" );
            form.setTimeFrame( "-30--40 units" );
            form.setTimeFrame( "-30-40 units" );
            form.setTimeFrame( "30--40 units" );
            form.setTimeFrame( "40-39 units" );
            fail( "Allowing invalid format for time frame" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Time format should be in format [int-int minutes] OR [int-int hours]", e.getMessage() );
        }

        try {
            form.setTimeFrame( "30-$ units" );
            fail( "Allowing invalid format for time frame" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Time range must contain integer for time", e.getMessage() );
        }

        // Testing setTimeFrame of CPTCode
        final CPTCode code = new CPTCode();
        code.setCode( "99215" );
        code.setDescription( "Another Kidney Surgery to buy another iPhone" );
        code.setId( 2L );
        code.setCost( 400000.00 );
        code.setTimeFrame( "50-74 minutes" );

        assertEquals( "50-74 minutes", code.getTimeFrame() );

        code.setTimeFrame( "60-74 minutes" );
        assertEquals( "60-74 minutes", code.getTimeFrame() );

        try {
            code.setTimeFrame( "30-40 units" );
            code.setTimeFrame( "-30--40 units" );
            code.setTimeFrame( "-30-40 units" );
            code.setTimeFrame( "30--40 units" );
            code.setTimeFrame( "40-39 units" );
            fail( "Allowing invalid format for time frame" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Time format should be in format [int-int minutes] OR [int-int hours]", e.getMessage() );
        }

        try {
            code.setTimeFrame( "30-$ units" );
            fail( "Allowing invalid format for time frame" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Time range must contain integer for time", e.getMessage() );
        }
    }

    @Test
    @Transactional
    public void testSetCode () {
        // Testing setCode of CPTCodeForm
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "12345" );
        form.setDescription( "Selling my brain to buy Apple" );
        form.setId( 3L );
        form.setCost( 10000000.00 );
        form.setTimeFrame( "496-3432 hours" );

        assertEquals( "12345", form.getCode() );

        form.setCode( "98215" );
        assertEquals( "98215", form.getCode() );

        try {
            form.setCode( "123456" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must be at least five characters and all integers: 123456", e.getMessage() );
        }
        try {
            form.setCode( "1234" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must be at least five characters and all integers: 1234", e.getMessage() );
        }
        try {
            form.setCode( "1234$" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must contain all integers and must be of length 5: 1234$", e.getMessage() );
        }
        try {
            form.setCode( "12L34" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must contain all integers and must be of length 5: 12L34", e.getMessage() );
        }

        // Testing setCode of CPTCode
        final CPTCode code = new CPTCode();
        code.setCode( "12345" );
        code.setDescription( "Selling my brain to buy Apple" );
        code.setId( 3L );
        code.setCost( 10000000.00 );
        code.setTimeFrame( "496-3432 hours" );

        assertEquals( "12345", code.getCode() );

        code.setCode( "98215" );
        assertEquals( "98215", code.getCode() );

        try {
            code.setCode( "123456" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must be at least five characters and all integers: 123456", e.getMessage() );
        }
        try {
            code.setCode( "1234" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must be at least five characters and all integers: 1234", e.getMessage() );
        }
        try {
            code.setCode( "1234$" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must contain all integers and must be of length 5: 1234$", e.getMessage() );
        }
        try {
            code.setCode( "12L34" );
            fail( "Allowing invalid code" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code must contain all integers and must be of length 5: 12L34", e.getMessage() );
        }
    }

    @Test
    @Transactional
    public void testSetDescription () {
        // Testing setDescription of CPTCodeForm
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "99832" );
        form.setDescription( "Selling my liver to buy iPad" );
        form.setId( 4L );
        form.setCost( 10000.00 );
        form.setTimeFrame( "28-40 minutes" );

        assertEquals( "Selling my liver to buy iPad", form.getDescription() );

        form.setDescription( "Selling my bladder to buy another iPad" );
        assertEquals( "Selling my bladder to buy another iPad", form.getDescription() );

        final String des = "Counting characters until its more than 250 Counting characters until its more than 250 Counting characters until its more than 250 Counting characters until its more than 250 Counting characters until its more than 250 Counting characters until its m";

        try {
            form.setDescription( des );
            fail( "Allowing invalid description" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Description too long (250 characters max)", e.getMessage() );
        }

        // Testing setDescription of CPTCode
        final CPTCode code = new CPTCode();
        code.setCode( "99832" );
        code.setDescription( "Selling my liver to buy iPad" );
        code.setId( 4L );
        code.setCost( 10000.00 );
        code.setTimeFrame( "28-40 minutes" );

        assertEquals( "Selling my liver to buy iPad", code.getDescription() );

        code.setDescription( "Selling my bladder to buy another iPad" );
        assertEquals( "Selling my bladder to buy another iPad", code.getDescription() );

        try {
            code.setDescription( des );
            fail( "Allowing invalid description" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Description too long (250 characters max)", e.getMessage() );
        }
    }

    @Test
    @Transactional
    public void testSetCost () {
        // Testing setCost of CPTCodeForm
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "99269" );
        form.setDescription( "Selling my Heart to buy macbook" );
        form.setId( 5L );
        form.setCost( 1000000.00 );
        form.setTimeFrame( "69-80 hours" );

        assertEquals( (Double) 1000000.00, form.getCost() );

        form.setCost( 1000000.69 );
        assertEquals( (Double) 1000000.69, form.getCost() );

        form.setCost( 0.00 );
        assertEquals( (Double) 0.00, form.getCost() );

        try {
            form.setCost( -1321321.02 );
            fail( "Allowing invalid cost" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "CPT Code Cost cannot be negative", e.getMessage() );
        }

        // Testing setCost of CPTCode
        final CPTCode code = new CPTCode();
        code.setCode( "99269" );
        code.setDescription( "Selling my Heart to buy macbook" );
        code.setId( 5L );
        code.setCost( 1000000.00 );
        code.setTimeFrame( "69-80 hours" );

        assertEquals( (Double) 1000000.00, code.getCost() );

        code.setCost( 1000000.69 );
        assertEquals( (Double) 1000000.69, code.getCost() );

        code.setCost( 0.00 );
        assertEquals( (Double) 0.00, code.getCost() );

        try {
            code.setCost( -1321321.02 );
            fail( "Allowing invalid cost" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "CPT Code Cost cannot be negative", e.getMessage() );
        }
    }

}
