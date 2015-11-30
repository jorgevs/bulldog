package io.silverspoon.bulldog.devices.lcd;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import io.silverspoon.bulldog.core.util.easing.StringUtil;
import org.apache.log4j.Logger;

/**
 * This class represents a HD44780 compatible lcd which is driven
 * in 4 BIT mode behind an I2C Port Expander.
 *
 **/

public class I2CLcdDisplay implements Lcd {
    final static Logger log = Logger.getLogger(I2CLcdDisplay.class);

    // LCD Commands
    public final byte LCD_CLEAR = 0b00000001;
    public final byte LCD_HOME  = 0b00000010;

    // Control bits
    private final byte LCD_BACKLIGHT_ON   = 0b00001000;
    private final byte LCD_BACKLIGHT_OFF  = 0b00000000;
    private final byte LCD_EN             = 0b00000100;     // Strobe pin (Will change from HI to LOW
                                                            // so that the Lcd accepts data)
    private final byte LCD_RW_READ        = 0b00000010;
    private final byte LCD_RW_WRITE       = 0b00000000;
    private final byte LCD_RS_DATA        = 0b00000001;
    private final byte LCD_RS_COMMAND     = 0b00000000;

    // Masks for commands
    private final byte LCD_DISPLAY_ON       = 0b00001100;
    private final byte LCD_DISPLAY_OFF      = 0b00001000;
    private final byte LCD_SHOW_CURSOR_ON   = 0b00001110;
    private final byte LCD_SHOW_CURSOR_OFF  = 0b00001100;
    private final byte LCD_BLINK_CURSOR_ON  = 0b00001111;
    private final byte LCD_BLINK_CURSOR_OFF = 0b00001110;

    private boolean twoCycleTrans = false;  // Send the info/cmd byte in a four bits fashion
                                            // (Upper 4 bits first and later the Lower 4 bits)

    public final byte LCD_LINE_TWO = 0x40;    // LCD RAM address for the second line

    private LcdMode lcdMode;    // Indicates the number of rows, columns, offset.
    private LcdFont lcdFont;    // Indicates the dots used to print a character

    private boolean isOn = true;   // Display is turned on/off
    private boolean showCursor = false; // Cursor is on/off
    private boolean blinkCursor = false;    // Blink the character at the cursor position

    private I2cConnection connection;

    public I2CLcdDisplay(LcdMode lcdMode, LcdFont lcdFont, I2cBus bus, int i2cAddress) {
        this.lcdMode = lcdMode;
        this.lcdFont = lcdFont;
        this.connection = bus.createI2cConnection(i2cAddress);
        init();
    }

    private void init() {
        // Initialization commands
        // Wait for more than 15 ms after VCC rises to 4.5 V
        BulldogUtil.sleepMs(20); // Let LCD power up

        // Interface starts as 8 bits long (1 cycle write).
        twoCycleTrans = false;
        // Write Nibble 0011 three times (HD44780 initialization spec)
        writeCommand((byte) 0b00110000);
        writeCommand((byte) 0b00110000);
        writeCommand((byte) 0b00110000);

        // Set Interface to be 4 bits long (2 cycle write).
        writeCommand((byte) 0b00100000);
        // Beyond this point, all bytes are sent in a Four bits fashion
        twoCycleTrans = true;

        // Function Set (Number of display lines and character font)
        functionSet(lcdMode.getColumns(), LcdFont.Font_5x8);  //writeCommand((byte) 0b00101000);
        // Display Off
        writeCommand((byte) 0b00001000);
        // Clear display
        writeCommand(LCD_CLEAR);
        // Entry Mode Set (Cursor move direction: Increment, accompanies display shift)
        writeCommand((byte) 0b00000110);

        //--------------------------------------------------------------------------
        // Adding this extra command, to set: Display On, showCursor Off, blinkCursor Off
        writeCommand((byte) 0b00001100);
    }

    private void functionSet(int lines, LcdFont font) {
        byte command = 0b00101000;
        /*if (mode == HD44780Mode.FourBit) {
            command = BitMagic.setBit(command, 4, 0);
        } else {
            command = BitMagic.setBit(command, 4, 1);
        }

        if (lines > 1) {
            command = BitMagic.setBit(command, 3, 1);
        } else {
            command = BitMagic.setBit(command, 3, 0);
        }

        if (font == LcdFont.Font_8x10) {
            command = BitMagic.setBit(command, 2, 1);
        } else {
            command = BitMagic.setBit(command, 2, 0);
        }*/

        writeCommand(command);
    }

    public int getColumnsCount() {
        return this.lcdMode.getColumns();
    }


    public int getRowsCount() {
        return this.lcdMode.getRows();
    }

    private void writeCommand(byte data) {
        log.debug("writeCommand(" + BulldogUtil.printBinaryValue(data) + ")");
        writeByte(data, LCD_RS_COMMAND);
    }

    private void writeData(byte data) {
        log.debug("writeData(" + BulldogUtil.printBinaryValue(data) + ")");
        writeByte(data, LCD_RS_DATA);
    }

    private void writeByte(byte data, byte registerType) {
        byte lcdValue;

        try {
            // Get the UPPER 4bits and mask them with the control bits
            lcdValue = (byte) ((data & 0xF0) | LCD_BACKLIGHT_ON | LCD_RW_WRITE | registerType);

            // Enable Pulse E: HI
            log.debug("  writeByte(" + printCtrlBits(BulldogUtil.printBinaryValue((byte)(lcdValue | LCD_EN))) + ") - twoCycleTrans: " + twoCycleTrans);
            connection.writeByte((byte)(lcdValue | LCD_EN));
            BulldogUtil.sleepMs(2);

            // Disable Pulse E: LOW
            log.debug("  writeByte(" + printCtrlBits(BulldogUtil.printBinaryValue((byte)(lcdValue & ~LCD_EN))) + ") - twoCycleTrans: " + twoCycleTrans);
            connection.writeByte((byte)(lcdValue & ~LCD_EN));
            BulldogUtil.sleepMs(2);

            if (twoCycleTrans) {
                // Get the LOWER 4bits and mask them with the control bits
                lcdValue = (byte) (((data << 4) & 0xF0) | LCD_BACKLIGHT_ON | LCD_RW_WRITE | registerType);

                // Enable Pulse E: HI
                log.debug("  writeByte(" + printCtrlBits(BulldogUtil.printBinaryValue((byte)(lcdValue | LCD_EN))) + ") - twoCycleTrans: " + twoCycleTrans);
                connection.writeByte((byte)(lcdValue | LCD_EN));
                BulldogUtil.sleepMs(2);

                // Disable Pulse E: LOW
                log.debug("  writeByte(" + printCtrlBits(BulldogUtil.printBinaryValue((byte)(lcdValue & ~LCD_EN))) + ") - twoCycleTrans: " + twoCycleTrans);
                connection.writeByte((byte)(lcdValue & ~LCD_EN));
                BulldogUtil.sleepMs(2);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String printCtrlBits(String strByte) {
        return "[FourBites: " + strByte.substring(0, 4) + "] [BACKLIGHT: " + strByte.charAt(4) + " E: " + strByte.charAt(5) + " RW: " + strByte.charAt(6) + " RS: " + strByte.charAt(7) + "]";
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
    public void clear() {
        writeCommand(LCD_CLEAR); // Clear display
    }

    @Override
    public void home() {
        writeCommand(LCD_HOME); // Move cursor to home position (0,0)
    }

    @Override
    public void on() {
        this.isOn = true;
        writeCommand(LCD_DISPLAY_ON);
    }

    @Override
    public void off() {
        this.isOn = false;
        writeCommand(LCD_DISPLAY_OFF);
    }

    @Override
    public void showCursor(boolean show) {
        this.showCursor = show;
        if(show)writeCommand(LCD_SHOW_CURSOR_ON);
        else writeCommand(LCD_SHOW_CURSOR_OFF);
    }

    @Override
    public void blinkCursor(boolean blink) {
        this.blinkCursor = blink;
        if(this.blinkCursor)writeCommand(LCD_BLINK_CURSOR_ON);
        else writeCommand(LCD_BLINK_CURSOR_OFF);
    }

    @Override
    public void setMode(LcdMode mode, LcdFont font) {
        this.lcdMode = mode;
        this.lcdFont = font;
        init();
        clear();
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

    //*****************************************************************************
    public void write(int row, int column, String data){
        validateCoordinates(row, column);
        setCursorPosition(row, column);
        write(data);
    }

    public void write(int row, String data) {
        write(row, 0, data);
    }

    protected void validateCoordinates(int row, int column) {
        validateRowIndex(row);
        validateColumnIndex(column);
    }

    protected void validateRowIndex(int row) {
        if(row >= getRowsCount() || row < 0)
            throw new RuntimeException("Invalid row index.");
    }

    protected void validateColumnIndex(int column) {
        if(column >= getColumnsCount() || column < 0)
            throw new RuntimeException("Invalid column index.");
    }

    public void writeln(int row, String data) {
        writeln(row, data, LCDTextAlignment.ALIGN_LEFT);
    }

    public void writeln(int row, String data, LCDTextAlignment alignment) {
        String result = data;
        if(data.length() < this.getColumnsCount()){
            if(alignment == LCDTextAlignment.ALIGN_LEFT)
                result = StringUtil.padRight(data, (getColumnsCount() - data.length()));
            else if(alignment == LCDTextAlignment.ALIGN_RIGHT)
                result = StringUtil.padLeft(data, (getColumnsCount() - data.length()));
            else if(alignment == LCDTextAlignment.ALIGN_CENTER)
                result = StringUtil.padCenter(data, getColumnsCount());
        }
        write(row, 0, result);
    }

    public void write(int row, String data, LCDTextAlignment alignment) {
        int columnIndex = 0;
        if(alignment != LCDTextAlignment.ALIGN_LEFT && data.length() < getColumnsCount()){
            int remaining = getColumnsCount() - data.length();
            if(alignment == LCDTextAlignment.ALIGN_RIGHT) {
                columnIndex = remaining;
            }
            else if(alignment == LCDTextAlignment.ALIGN_CENTER) {
                columnIndex = (remaining/2);
            }
        }
        write(row, columnIndex, data);
    }
}