package io.silverspoon.bulldog.devices.lcd;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import org.apache.log4j.Logger;

import java.io.IOException;

public class I2CLcdDisplay implements Lcd {
    final static Logger log = Logger.getLogger(I2CLcdDisplay.class);

    // LCD Commands
    public final byte LCD_CLEAR = 0x01;
    public final byte LCD_HOME = 0x02;

    // I2C Bus Control Definition
    public final byte I2C_WRITE_CMD = 0x00;
    public final byte I2C_READ_CMD = 0x01;

    public final byte LCD_BL = 0b00001000;  //Backlight
    public final byte LCD_EN = 0b00000100;  //Strobe pin

    public final byte LCD_RS_DATA = 0b00000001;
    public final byte LCD_RS_COMMAND = 0b00000000;

    public final byte LCD_LINE_TWO = 0x40;    // LCD RAM address for the second line

    protected int rows;
    protected int columns;

    private HD44780Mode mode = HD44780Mode.EightBit;   // COMMUNICATION_MODE: 4 BITS or 8 BITS
    private boolean writeAsNibbles = false;

    private boolean backlight = true;
    private I2cConnection connection;


    public I2CLcdDisplay(int rows, int columns, I2cBus bus, int i2cAddress, HD44780Mode numBitsMode) throws Exception {
        this.rows = rows;
        this.columns = columns;
        this.connection = bus.createI2cConnection(i2cAddress);
        this.mode = numBitsMode;
        init();
    }

    private void init() {
        // Initialization commands for Hitachi HD44780U LCD Display
        // Wait for more than 15 ms after VCC rises to 4.5 V
        BulldogUtil.sleepMs(30); // Let LCD power up

        // Write Nibble 0011 three times (per HD44780U initialization spec)
        writeCommand((byte) 0b00110000);
        BulldogUtil.sleepMs(10);
        writeCommand((byte) 0b00110000);
        BulldogUtil.sleepMs(10);
        writeCommand((byte) 0b00110000);
        BulldogUtil.sleepMs(10);

        if(this.mode == HD44780Mode.FourBit) {
            // Function set: Set interface to be 4 bits long (only 1 cycle write).
            writeCommand((byte) (0x02 << 4)); // Write Nibble 0x02 once - Set interface to be 4 bits long
            BulldogUtil.sleepMs(10);
            setWriteAsNibbles(true);
        }

        // Function set: DL=0;Interface is 4 bits, N=1; 2 Lines, F=0; 5x8 dots font)
        writeCommand((byte) 0x28);
        BulldogUtil.sleepMs(10);
        // Display Off: D=0; Display off, C=0; Cursor Off, B=0; Blinking Off
        writeCommand((byte) 0x08);
        BulldogUtil.sleepMs(10);
        writeCommand(LCD_CLEAR); // Clear display
        BulldogUtil.sleepMs(10);
        // Entry Mode Set: I/D=1; Increment, S=0; No shift
        writeCommand((byte) 0x06); // Set cursor to increment
        BulldogUtil.sleepMs(10);
        //--------------------------------------------------------------------------
        // Display: D=1; Display on, C=1; Cursor On, B=0; Blinking Off
        writeCommand((byte) 0x0C); // Set cursor to increment
        BulldogUtil.sleepMs(30);
    }

    private void writeCommand(byte data) {
        log.debug("writeCommand(" + BulldogUtil.printBinaryValue((byte)data) + ")");
        writeByte(data, LCD_RS_COMMAND);
    }

    private void writeData(byte data) {
        log.debug("writeData(" + BulldogUtil.printBinaryValue((byte)data) + ")");
        writeByte(data, LCD_RS_DATA);
        BulldogUtil.sleepMs(10);
    }

    private void writeByte(byte data, byte registerType) {
        byte lcdValue;
        try {
            // Put the Upper 4 bits data
            lcdValue = (byte) ((data & 0xF0) | LCD_BL | registerType);
            log.debug("--writeByteAsNibbles(" + BulldogUtil.printBinaryValue((byte)(lcdValue | LCD_EN)) + ")");
            connection.writeByte(lcdValue | LCD_EN);
            BulldogUtil.sleepMs(10);

            // Write Enable Pulse E: Hi -> Lo
            log.debug("--writeByteAsNibbles(" + BulldogUtil.printBinaryValue((byte)(lcdValue & ~LCD_EN)) + ")");
            connection.writeByte(lcdValue & ~LCD_EN);
            BulldogUtil.sleepMs(10);

            if (getWriteAsNibbles()) {
                // LCD Data PCA8574:    P7, P6, P5, P4
                // LCD Control PCA8574: P3:Back Light, P2:E-Enable, P1:RW, P0:RS

                // Put the Lower 4 bits data
                lcdValue = (byte) (((data << 4) & 0xF0) | LCD_BL | registerType);
                log.debug("--writeByteAsNibbles(" + BulldogUtil.printBinaryValue((byte)(lcdValue | LCD_EN)) + ")");
                connection.writeByte(lcdValue | LCD_EN);
                BulldogUtil.sleepMs(10);

                // Write Enable Pulse E: Hi -> Lo
                log.debug("--writeByteAsNibbles(" + BulldogUtil.printBinaryValue((byte)(lcdValue & ~LCD_EN)) + ")");
                connection.writeByte(lcdValue & ~LCD_EN);
                BulldogUtil.sleepMs(10);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setBacklight(boolean backlight) {
        this.backlight = backlight;
    }

    public boolean isBacklight() {
        return backlight;
    }

    public boolean getWriteAsNibbles() {
        return writeAsNibbles;
    }

    public void setWriteAsNibbles(boolean writeAsNibbles) {
        this.writeAsNibbles = writeAsNibbles;
    }

    @Override
    public void setCursorPosition(int x, int y) {
        byte dataAddress;

        if (y != 0) {
            dataAddress = LCD_LINE_TWO;
        } else {
            dataAddress = 0;
        }
        dataAddress = (byte) (dataAddress + x);
        writeCommand((byte) (0x80 | dataAddress));
    }

    @Override
    public String readLine(int line) {
        return null;
    }

    @Override
    public String read(int length) {
        return null;
    }

    @Override
    public String read(int line, int column, int length) {
        return null;
    }

    @Override
    public void home() {
        writeCommand(LCD_HOME); // Move cursor to home position (0,0)
        BulldogUtil.sleepMs(2);
    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }

    @Override
    public void clear() {
        writeCommand(LCD_CLEAR); // Clear display
        BulldogUtil.sleepMs(2);
    }

    @Override
    public void blinkCursor(boolean blink) {

    }

    @Override
    public void showCursor(boolean show) {

    }

    @Override
    public void setMode(LcdMode mode, LcdFont font) {

    }

    @Override
    public void write(String string) {
        for (int i = 0; i < string.length(); i++) {
            writeData((byte) (string.charAt(i)));
        }
    }

    @Override
    public void writeAt(int row, int column, String text) {
        setCursorPosition(row, column);
        write(text);
    }

}