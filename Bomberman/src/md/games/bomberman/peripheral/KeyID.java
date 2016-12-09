/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.awt.event.KeyEvent;
import nt.ntjg.Key;

/**
 *
 * @author Marc
 */
public final class KeyID extends PeripheralID
{
    private final int keycode;
    private final char _char;

    KeyID(int keycode)
    {
        super();
        this.keycode = keycode;
        this._char = KeyID.INVALID_ASSOCIATED_CHAR;
    }

    @Override
    final int getCode0()
    {
        return PeripheralReference.KEYBOARD + ((keycode & 0xFFFFFF) << 2);
    }
    
    @Override
    public final int getIDType()
    {
        return PeripheralReference.KEYBOARD;
    }
    
    public static final PeripheralID getID(int keycode)
    {
        return new KeyID(keycode);
    }
    
    static final KeyID decode0(int code)
    {
        return new KeyID((code >>> 2) & 0xFFFFFF);
    }
    
    static final int getCode0(int keycode)
    {
        return PeripheralReference.KEYBOARD + ((keycode & 0xFFFFFF) << 2);
    }
    
    public static final int encode(int keycode) { return getCode0(keycode); }
    
    @Override
    final String toString0()
    {
        return "keycode = " + keycode + " (" + getKeyName(keycode) + ")";
    }
    
    @Override
    public final String getName()
    {
        return getKeyName(keycode);
    }
    
    public static final int getKeycode(PeripheralID id)
    {
        if(id instanceof KeyID == false) return -1;
        return ((KeyID)id).keycode;
    }
    
    public static final int getKeycode(int code)
    {
        if((code & 0x3) != PeripheralReference.KEYBOARD) return -1;
        return (code >>> 2) & 0xFFFFFF;
    }
    
    
    
    
    
    
    
    
    /**
     * The first number in the range of ids used for key events.
     */
    public static final int KEY_FIRST = KeyEvent.KEY_FIRST;

    /**
     * The last number in the range of ids used for key events.
     */
    public static final int KEY_LAST  = KeyEvent.KEY_LAST;

    /**
     * The "key typed" event.  This event is generated when a character is
     * entered.  In the simplest case, it is produced by a single key press.
     * Often, however, characters are produced by series of key presses, and
     * the mapping from key pressed events to key typed events may be
     * many-to-one or many-to-many.
     */
    public static final int KEY_TYPED = KEY_FIRST;

    /**
     * The "key pressed" event. This event is generated when a key
     * is pushed down.
     */
    public static final int KEY_PRESSED = 1 + KEY_FIRST; //Event.KEY_PRESS

    /**
     * The "key released" event. This event is generated when a key
     * is let up.
     */
    public static final int KEY_RELEASED = 2 + KEY_FIRST; //Event.KEY_RELEASE

    /* Virtual key codes. */

    public static final int VK_ENTER          = '\n';
    public static final int VK_BACK_SPACE     = '\b';
    public static final int VK_TAB            = '\t';
    public static final int VK_CANCEL         = 0x03;
    public static final int VK_CLEAR          = 0x0C;
    public static final int VK_SHIFT          = 0x10;
    public static final int VK_CONTROL        = 0x11;
    public static final int VK_ALT            = 0x12;
    public static final int VK_PAUSE          = 0x13;
    public static final int VK_CAPS_LOCK      = 0x14;
    public static final int VK_ESCAPE         = 0x1B;
    public static final int VK_SPACE          = 0x20;
    public static final int VK_PAGE_UP        = 0x21;
    public static final int VK_PAGE_DOWN      = 0x22;
    public static final int VK_END            = 0x23;
    public static final int VK_HOME           = 0x24;

    /**
     * Constant for the non-numpad <b>left</b> arrow key.
     * @see #VK_KP_LEFT
     */
    public static final int VK_LEFT           = 0x25;

    /**
     * Constant for the non-numpad <b>up</b> arrow key.
     * @see #VK_KP_UP
     */
    public static final int VK_UP             = 0x26;

    /**
     * Constant for the non-numpad <b>right</b> arrow key.
     * @see #VK_KP_RIGHT
     */
    public static final int VK_RIGHT          = 0x27;

    /**
     * Constant for the non-numpad <b>down</b> arrow key.
     * @see #VK_KP_DOWN
     */
    public static final int VK_DOWN           = 0x28;

    /**
     * Constant for the comma key, ","
     */
    public static final int VK_COMMA          = 0x2C;

    /**
     * Constant for the minus key, "-"
     * @since 1.2
     */
    public static final int VK_MINUS          = 0x2D;

    /**
     * Constant for the period key, "."
     */
    public static final int VK_PERIOD         = 0x2E;

    /**
     * Constant for the forward slash key, "/"
     */
    public static final int VK_SLASH          = 0x2F;

    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
    public static final int VK_0              = 0x30;
    public static final int VK_1              = 0x31;
    public static final int VK_2              = 0x32;
    public static final int VK_3              = 0x33;
    public static final int VK_4              = 0x34;
    public static final int VK_5              = 0x35;
    public static final int VK_6              = 0x36;
    public static final int VK_7              = 0x37;
    public static final int VK_8              = 0x38;
    public static final int VK_9              = 0x39;

    /**
     * Constant for the semicolon key, ";"
     */
    public static final int VK_SEMICOLON      = 0x3B;

    /**
     * Constant for the equals key, "="
     */
    public static final int VK_EQUALS         = 0x3D;

    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */
    public static final int VK_A              = 0x41;
    public static final int VK_B              = 0x42;
    public static final int VK_C              = 0x43;
    public static final int VK_D              = 0x44;
    public static final int VK_E              = 0x45;
    public static final int VK_F              = 0x46;
    public static final int VK_G              = 0x47;
    public static final int VK_H              = 0x48;
    public static final int VK_I              = 0x49;
    public static final int VK_J              = 0x4A;
    public static final int VK_K              = 0x4B;
    public static final int VK_L              = 0x4C;
    public static final int VK_M              = 0x4D;
    public static final int VK_N              = 0x4E;
    public static final int VK_O              = 0x4F;
    public static final int VK_P              = 0x50;
    public static final int VK_Q              = 0x51;
    public static final int VK_R              = 0x52;
    public static final int VK_S              = 0x53;
    public static final int VK_T              = 0x54;
    public static final int VK_U              = 0x55;
    public static final int VK_V              = 0x56;
    public static final int VK_W              = 0x57;
    public static final int VK_X              = 0x58;
    public static final int VK_Y              = 0x59;
    public static final int VK_Z              = 0x5A;

    /**
     * Constant for the open bracket key, "["
     */
    public static final int VK_OPEN_BRACKET   = 0x5B;

    /**
     * Constant for the back slash key, "\"
     */
    public static final int VK_BACK_SLASH     = 0x5C;

    /**
     * Constant for the close bracket key, "]"
     */
    public static final int VK_CLOSE_BRACKET  = 0x5D;

    public static final int VK_NUMPAD0        = 0x60;
    public static final int VK_NUMPAD1        = 0x61;
    public static final int VK_NUMPAD2        = 0x62;
    public static final int VK_NUMPAD3        = 0x63;
    public static final int VK_NUMPAD4        = 0x64;
    public static final int VK_NUMPAD5        = 0x65;
    public static final int VK_NUMPAD6        = 0x66;
    public static final int VK_NUMPAD7        = 0x67;
    public static final int VK_NUMPAD8        = 0x68;
    public static final int VK_NUMPAD9        = 0x69;
    public static final int VK_MULTIPLY       = 0x6A;
    public static final int VK_ADD            = 0x6B;

    /**
     * This constant is obsolete, and is included only for backwards
     * compatibility.
     * @see #VK_SEPARATOR
     */
    public static final int VK_SEPARATER      = 0x6C;

    /**
     * Constant for the Numpad Separator key.
     * @since 1.4
     */
    public static final int VK_SEPARATOR      = VK_SEPARATER;

    public static final int VK_SUBTRACT       = 0x6D;
    public static final int VK_DECIMAL        = 0x6E;
    public static final int VK_DIVIDE         = 0x6F;
    public static final int VK_DELETE         = 0x7F; /* ASCII DEL */
    public static final int VK_NUM_LOCK       = 0x90;
    public static final int VK_SCROLL_LOCK    = 0x91;

    /** Constant for the F1 function key. */
    public static final int VK_F1             = 0x70;

    /** Constant for the F2 function key. */
    public static final int VK_F2             = 0x71;

    /** Constant for the F3 function key. */
    public static final int VK_F3             = 0x72;

    /** Constant for the F4 function key. */
    public static final int VK_F4             = 0x73;

    /** Constant for the F5 function key. */
    public static final int VK_F5             = 0x74;

    /** Constant for the F6 function key. */
    public static final int VK_F6             = 0x75;

    /** Constant for the F7 function key. */
    public static final int VK_F7             = 0x76;

    /** Constant for the F8 function key. */
    public static final int VK_F8             = 0x77;

    /** Constant for the F9 function key. */
    public static final int VK_F9             = 0x78;

    /** Constant for the F10 function key. */
    public static final int VK_F10            = 0x79;

    /** Constant for the F11 function key. */
    public static final int VK_F11            = 0x7A;

    /** Constant for the F12 function key. */
    public static final int VK_F12            = 0x7B;

    /**
     * Constant for the F13 function key.
     * @since 1.2
     */
    /* F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F13            = 0xF000;

    /**
     * Constant for the F14 function key.
     * @since 1.2
     */
    public static final int VK_F14            = 0xF001;

    /**
     * Constant for the F15 function key.
     * @since 1.2
     */
    public static final int VK_F15            = 0xF002;

    /**
     * Constant for the F16 function key.
     * @since 1.2
     */
    public static final int VK_F16            = 0xF003;

    /**
     * Constant for the F17 function key.
     * @since 1.2
     */
    public static final int VK_F17            = 0xF004;

    /**
     * Constant for the F18 function key.
     * @since 1.2
     */
    public static final int VK_F18            = 0xF005;

    /**
     * Constant for the F19 function key.
     * @since 1.2
     */
    public static final int VK_F19            = 0xF006;

    /**
     * Constant for the F20 function key.
     * @since 1.2
     */
    public static final int VK_F20            = 0xF007;

    /**
     * Constant for the F21 function key.
     * @since 1.2
     */
    public static final int VK_F21            = 0xF008;

    /**
     * Constant for the F22 function key.
     * @since 1.2
     */
    public static final int VK_F22            = 0xF009;

    /**
     * Constant for the F23 function key.
     * @since 1.2
     */
    public static final int VK_F23            = 0xF00A;

    /**
     * Constant for the F24 function key.
     * @since 1.2
     */
    public static final int VK_F24            = 0xF00B;

    public static final int VK_PRINTSCREEN    = 0x9A;
    public static final int VK_INSERT         = 0x9B;
    public static final int VK_HELP           = 0x9C;
    public static final int VK_META           = 0x9D;

    public static final int VK_BACK_QUOTE     = 0xC0;
    public static final int VK_QUOTE          = 0xDE;

    /**
     * Constant for the numeric keypad <b>up</b> arrow key.
     * @see #VK_UP
     * @since 1.2
     */
    public static final int VK_KP_UP          = 0xE0;

    /**
     * Constant for the numeric keypad <b>down</b> arrow key.
     * @see #VK_DOWN
     * @since 1.2
     */
    public static final int VK_KP_DOWN        = 0xE1;

    /**
     * Constant for the numeric keypad <b>left</b> arrow key.
     * @see #VK_LEFT
     * @since 1.2
     */
    public static final int VK_KP_LEFT        = 0xE2;

    /**
     * Constant for the numeric keypad <b>right</b> arrow key.
     * @see #VK_RIGHT
     * @since 1.2
     */
    public static final int VK_KP_RIGHT       = 0xE3;

    /* For European keyboards */
    /** @since 1.2 */
    public static final int VK_DEAD_GRAVE               = 0x80;
    /** @since 1.2 */
    public static final int VK_DEAD_ACUTE               = 0x81;
    /** @since 1.2 */
    public static final int VK_DEAD_CIRCUMFLEX          = 0x82;
    /** @since 1.2 */
    public static final int VK_DEAD_TILDE               = 0x83;
    /** @since 1.2 */
    public static final int VK_DEAD_MACRON              = 0x84;
    /** @since 1.2 */
    public static final int VK_DEAD_BREVE               = 0x85;
    /** @since 1.2 */
    public static final int VK_DEAD_ABOVEDOT            = 0x86;
    /** @since 1.2 */
    public static final int VK_DEAD_DIAERESIS           = 0x87;
    /** @since 1.2 */
    public static final int VK_DEAD_ABOVERING           = 0x88;
    /** @since 1.2 */
    public static final int VK_DEAD_DOUBLEACUTE         = 0x89;
    /** @since 1.2 */
    public static final int VK_DEAD_CARON               = 0x8a;
    /** @since 1.2 */
    public static final int VK_DEAD_CEDILLA             = 0x8b;
    /** @since 1.2 */
    public static final int VK_DEAD_OGONEK              = 0x8c;
    /** @since 1.2 */
    public static final int VK_DEAD_IOTA                = 0x8d;
    /** @since 1.2 */
    public static final int VK_DEAD_VOICED_SOUND        = 0x8e;
    /** @since 1.2 */
    public static final int VK_DEAD_SEMIVOICED_SOUND    = 0x8f;

    /** @since 1.2 */
    public static final int VK_AMPERSAND                = 0x96;
    /** @since 1.2 */
    public static final int VK_ASTERISK                 = 0x97;
    /** @since 1.2 */
    public static final int VK_QUOTEDBL                 = 0x98;
    /** @since 1.2 */
    public static final int VK_LESS                     = 0x99;

    /** @since 1.2 */
    public static final int VK_GREATER                  = 0xa0;
    /** @since 1.2 */
    public static final int VK_BRACELEFT                = 0xa1;
    /** @since 1.2 */
    public static final int VK_BRACERIGHT               = 0xa2;

    /**
     * Constant for the "@" key.
     * @since 1.2
     */
    public static final int VK_AT                       = 0x0200;

    /**
     * Constant for the ":" key.
     * @since 1.2
     */
    public static final int VK_COLON                    = 0x0201;

    /**
     * Constant for the "^" key.
     * @since 1.2
     */
    public static final int VK_CIRCUMFLEX               = 0x0202;

    /**
     * Constant for the "$" key.
     * @since 1.2
     */
    public static final int VK_DOLLAR                   = 0x0203;

    /**
     * Constant for the Euro currency sign key.
     * @since 1.2
     */
    public static final int VK_EURO_SIGN                = 0x0204;

    /**
     * Constant for the "!" key.
     * @since 1.2
     */
    public static final int VK_EXCLAMATION_MARK         = 0x0205;

    /**
     * Constant for the inverted exclamation mark key.
     * @since 1.2
     */
    public static final int VK_INVERTED_EXCLAMATION_MARK = 0x0206;

    /**
     * Constant for the "(" key.
     * @since 1.2
     */
    public static final int VK_LEFT_PARENTHESIS         = 0x0207;

    /**
     * Constant for the "#" key.
     * @since 1.2
     */
    public static final int VK_NUMBER_SIGN              = 0x0208;

    /**
     * Constant for the "+" key.
     * @since 1.2
     */
    public static final int VK_PLUS                     = 0x0209;

    /**
     * Constant for the ")" key.
     * @since 1.2
     */
    public static final int VK_RIGHT_PARENTHESIS        = 0x020A;

    /**
     * Constant for the "_" key.
     * @since 1.2
     */
    public static final int VK_UNDERSCORE               = 0x020B;

    /**
     * Constant for the Microsoft Windows "Windows" key.
     * It is used for both the left and right version of the key.
     * @see #getKeyLocation()
     * @since 1.5
     */
    public static final int VK_WINDOWS                  = 0x020C;

    /**
     * Constant for the Microsoft Windows Context Menu key.
     * @since 1.5
     */
    public static final int VK_CONTEXT_MENU             = 0x020D;

    /* for input method support on Asian Keyboards */

    /* not clear what this means - listed in Microsoft Windows API */
    public static final int VK_FINAL                    = 0x0018;

    /** Constant for the Convert function key. */
    /* Japanese PC 106 keyboard, Japanese Solaris keyboard: henkan */
    public static final int VK_CONVERT                  = 0x001C;

    /** Constant for the Don't Convert function key. */
    /* Japanese PC 106 keyboard: muhenkan */
    public static final int VK_NONCONVERT               = 0x001D;

    /** Constant for the Accept or Commit function key. */
    /* Japanese Solaris keyboard: kakutei */
    public static final int VK_ACCEPT                   = 0x001E;

    /* not clear what this means - listed in Microsoft Windows API */
    public static final int VK_MODECHANGE               = 0x001F;

    /* replaced by VK_KANA_LOCK for Microsoft Windows and Solaris;
       might still be used on other platforms */
    public static final int VK_KANA                     = 0x0015;

    /* replaced by VK_INPUT_METHOD_ON_OFF for Microsoft Windows and Solaris;
       might still be used for other platforms */
    public static final int VK_KANJI                    = 0x0019;

    /**
     * Constant for the Alphanumeric function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: eisuu */
    public static final int VK_ALPHANUMERIC             = 0x00F0;

    /**
     * Constant for the Katakana function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: katakana */
    public static final int VK_KATAKANA                 = 0x00F1;

    /**
     * Constant for the Hiragana function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: hiragana */
    public static final int VK_HIRAGANA                 = 0x00F2;

    /**
     * Constant for the Full-Width Characters function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: zenkaku */
    public static final int VK_FULL_WIDTH               = 0x00F3;

    /**
     * Constant for the Half-Width Characters function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: hankaku */
    public static final int VK_HALF_WIDTH               = 0x00F4;

    /**
     * Constant for the Roman Characters function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: roumaji */
    public static final int VK_ROMAN_CHARACTERS         = 0x00F5;

    /**
     * Constant for the All Candidates function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - VK_CONVERT + ALT: zenkouho */
    public static final int VK_ALL_CANDIDATES           = 0x0100;

    /**
     * Constant for the Previous Candidate function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - VK_CONVERT + SHIFT: maekouho */
    public static final int VK_PREVIOUS_CANDIDATE       = 0x0101;

    /**
     * Constant for the Code Input function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - VK_ALPHANUMERIC + ALT: kanji bangou */
    public static final int VK_CODE_INPUT               = 0x0102;

    /**
     * Constant for the Japanese-Katakana function key.
     * This key switches to a Japanese input method and selects its Katakana input mode.
     * @since 1.2
     */
    /* Japanese Macintosh keyboard - VK_JAPANESE_HIRAGANA + SHIFT */
    public static final int VK_JAPANESE_KATAKANA        = 0x0103;

    /**
     * Constant for the Japanese-Hiragana function key.
     * This key switches to a Japanese input method and selects its Hiragana input mode.
     * @since 1.2
     */
    /* Japanese Macintosh keyboard */
    public static final int VK_JAPANESE_HIRAGANA        = 0x0104;

    /**
     * Constant for the Japanese-Roman function key.
     * This key switches to a Japanese input method and selects its Roman-Direct input mode.
     * @since 1.2
     */
    /* Japanese Macintosh keyboard */
    public static final int VK_JAPANESE_ROMAN           = 0x0105;

    /**
     * Constant for the locking Kana function key.
     * This key locks the keyboard into a Kana layout.
     * @since 1.3
     */
    /* Japanese PC 106 keyboard with special Windows driver - eisuu + Control; Japanese Solaris keyboard: kana */
    public static final int VK_KANA_LOCK                = 0x0106;

    /**
     * Constant for the input method on/off key.
     * @since 1.3
     */
    /* Japanese PC 106 keyboard: kanji. Japanese Solaris keyboard: nihongo */
    public static final int VK_INPUT_METHOD_ON_OFF      = 0x0107;

    /* for Sun keyboards */
    /** @since 1.2 */
    public static final int VK_CUT                      = 0xFFD1;
    /** @since 1.2 */
    public static final int VK_COPY                     = 0xFFCD;
    /** @since 1.2 */
    public static final int VK_PASTE                    = 0xFFCF;
    /** @since 1.2 */
    public static final int VK_UNDO                     = 0xFFCB;
    /** @since 1.2 */
    public static final int VK_AGAIN                    = 0xFFC9;
    /** @since 1.2 */
    public static final int VK_FIND                     = 0xFFD0;
    /** @since 1.2 */
    public static final int VK_PROPS                    = 0xFFCA;
    /** @since 1.2 */
    public static final int VK_STOP                     = 0xFFC8;

    /**
     * Constant for the Compose function key.
     * @since 1.2
     */
    public static final int VK_COMPOSE                  = 0xFF20;

    /**
     * Constant for the AltGraph function key.
     * @since 1.2
     */
    public static final int VK_ALT_GRAPH                = 0xFF7E;

    /**
     * Constant for the Begin key.
     * @since 1.5
     */
    public static final int VK_BEGIN                    = 0xFF58;

    /**
     * This value is used to indicate that the keyCode is unknown.
     * KEY_TYPED events do not have a keyCode value; this value
     * is used instead.
     */
    public static final int VK_UNDEFINED      = 0x0;
    
    public static final int VK_SHIFT_LEFT          = Key.VK_SHIFT_LEFT;
    public static final int VK_SHIFT_RIGHT          = Key.VK_SHIFT_RIGHT;
    public static final int VK_CONTROL_LEFT        = Key.VK_CONTROL_LEFT;
    public static final int VK_CONTROL_RIGHT        = Key.VK_CONTROL_RIGHT;

    /**
     * KEY_PRESSED and KEY_RELEASED events which do not map to a
     * valid Unicode character use this for the keyChar value.
     */
    public static final char CHAR_UNDEFINED   = 0xFFFF;

    /**
     * A constant indicating that the keyLocation is indeterminate
     * or not relevant.
     * <code>KEY_TYPED</code> events do not have a keyLocation; this value
     * is used instead.
     * @since 1.4
     */
    public static final int KEY_LOCATION_UNKNOWN  = 0;

    /**
     * A constant indicating that the key pressed or released
     * is not distinguished as the left or right version of a key,
     * and did not originate on the numeric keypad (or did not
     * originate with a virtual key corresponding to the numeric
     * keypad).
     * @since 1.4
     */
    public static final int KEY_LOCATION_STANDARD = 1;

    /**
     * A constant indicating that the key pressed or released is in
     * the left key location (there is more than one possible location
     * for this key).  Example: the left shift key.
     * @since 1.4
     */
    public static final int KEY_LOCATION_LEFT     = 2;

    /**
     * A constant indicating that the key pressed or released is in
     * the right key location (there is more than one possible location
     * for this key).  Example: the right shift key.
     * @since 1.4
     */
    public static final int KEY_LOCATION_RIGHT    = 3;

    /**
     * A constant indicating that the key event originated on the
     * numeric keypad or with a virtual key corresponding to the
     * numeric keypad.
     * @since 1.4
     */
    public static final int KEY_LOCATION_NUMPAD   = 4;
    
    public static final String getKeyName(PeripheralID id)
    {
        if(id instanceof KeyID == false) return null;
        return getKeyName(((KeyID)id).keycode);
    }
    public static final String getKeyName(int keycode)
    {
        switch(keycode)
        {
            case KeyID.VK_ENTER: return "ENTER";
            case KeyID.VK_BACK_SPACE: return "BACK_SPACE";
            case KeyID.VK_TAB: return "TAB";
            case KeyID.VK_CANCEL: return "CANCEL";
            case KeyID.VK_CLEAR: return "CLEAR";
            case KeyID.VK_SHIFT: return "SHIFT";
            case KeyID.VK_CONTROL: return "CONTROL";
            case KeyID.VK_ALT: return "ALT";
            case KeyID.VK_PAUSE: return "PAUSE";
            case KeyID.VK_CAPS_LOCK: return "CAPS_LOCK";
            case KeyID.VK_ESCAPE: return "ESCAPE";
            case KeyID.VK_SPACE: return "SPACE";
            case KeyID.VK_PAGE_UP: return "PAGE_UP";
            case KeyID.VK_PAGE_DOWN: return "PAGE_DOWN";
            case KeyID.VK_END: return "END";
            case KeyID.VK_HOME: return "HOME";
            case KeyID.VK_LEFT: return "LEFT";
            case KeyID.VK_UP: return "UP";
            case KeyID.VK_RIGHT: return "RIGHT";
            case KeyID.VK_DOWN: return "DOWN";
            case KeyID.VK_COMMA: return "COMMA";
            case KeyID.VK_MINUS: return "MINUS";
            case KeyID.VK_PERIOD: return "PERIOD";
            case KeyID.VK_SLASH: return "SLASH";
            case KeyID.VK_0: return "0";
            case KeyID.VK_1: return "1";
            case KeyID.VK_2: return "2";
            case KeyID.VK_3: return "3";
            case KeyID.VK_4: return "4";
            case KeyID.VK_5: return "5";
            case KeyID.VK_6: return "6";
            case KeyID.VK_7: return "7";
            case KeyID.VK_8: return "8";
            case KeyID.VK_9: return "9";
            case KeyID.VK_SEMICOLON: return "SEMICOLON";
            case KeyID.VK_EQUALS: return "EQUALS";
            case KeyID.VK_A: return "A";
            case KeyID.VK_B: return "B";
            case KeyID.VK_C: return "C";
            case KeyID.VK_D: return "D";
            case KeyID.VK_E: return "E";
            case KeyID.VK_F: return "F";
            case KeyID.VK_G: return "G";
            case KeyID.VK_H: return "H";
            case KeyID.VK_I: return "I";
            case KeyID.VK_J: return "J";
            case KeyID.VK_K: return "K";
            case KeyID.VK_L: return "L";
            case KeyID.VK_M: return "M";
            case KeyID.VK_N: return "N";
            case KeyID.VK_O: return "O";
            case KeyID.VK_P: return "P";
            case KeyID.VK_Q: return "Q";
            case KeyID.VK_R: return "R";
            case KeyID.VK_S: return "S";
            case KeyID.VK_T: return "T";
            case KeyID.VK_U: return "U";
            case KeyID.VK_V: return "V";
            case KeyID.VK_W: return "W";
            case KeyID.VK_X: return "X";
            case KeyID.VK_Y: return "Y";
            case KeyID.VK_Z: return "Z";
            case KeyID.VK_OPEN_BRACKET: return "OPEN_BRACKET";
            case KeyID.VK_BACK_SLASH: return "BACK_SLASH";
            case KeyID.VK_CLOSE_BRACKET: return "CLOSE_BRACKET";
            case KeyID.VK_NUMPAD0: return "NUMPAD0";
            case KeyID.VK_NUMPAD1: return "NUMPAD1";
            case KeyID.VK_NUMPAD2: return "NUMPAD2";
            case KeyID.VK_NUMPAD3: return "NUMPAD3";
            case KeyID.VK_NUMPAD4: return "NUMPAD4";
            case KeyID.VK_NUMPAD5: return "NUMPAD5";
            case KeyID.VK_NUMPAD6: return "NUMPAD6";
            case KeyID.VK_NUMPAD7: return "NUMPAD7";
            case KeyID.VK_NUMPAD8: return "NUMPAD8";
            case KeyID.VK_NUMPAD9: return "NUMPAD9";
            case KeyID.VK_MULTIPLY: return "MULTIPLY";
            case KeyID.VK_ADD: return "ADD";
            case KeyID.VK_SEPARATOR: return "SEPARATOR";
            case KeyID.VK_SUBTRACT: return "SUBTRACT";
            case KeyID.VK_DECIMAL: return "DECIMAL";
            case KeyID.VK_DIVIDE: return "DIVIDE";
            case KeyID.VK_NUM_LOCK: return "NUM_LOCK";
            case KeyID.VK_SCROLL_LOCK: return "SCROLL_LOCK";
            case KeyID.VK_F1: return "F1";
            case KeyID.VK_F2: return "F2";
            case KeyID.VK_F3: return "F3";
            case KeyID.VK_F4: return "F4";
            case KeyID.VK_F5: return "F5";
            case KeyID.VK_F6: return "F6";
            case KeyID.VK_F7: return "F7";
            case KeyID.VK_F8: return "F8";
            case KeyID.VK_F9: return "F9";
            case KeyID.VK_F10: return "F10";
            case KeyID.VK_F11: return "F11";
            case KeyID.VK_F12: return "F12";
            case KeyID.VK_F13: return "F13";
            case KeyID.VK_F14: return "F14";
            case KeyID.VK_F15: return "F15";
            case KeyID.VK_F16: return "F16";
            case KeyID.VK_F17: return "F17";
            case KeyID.VK_F18: return "F18";
            case KeyID.VK_F19: return "F19";
            case KeyID.VK_F20: return "F20";
            case KeyID.VK_F21: return "F21";
            case KeyID.VK_F22: return "F22";
            case KeyID.VK_F23: return "F23";
            case KeyID.VK_F24: return "F24";
            case KeyID.VK_PRINTSCREEN: return "PRINTSCREEN";
            case KeyID.VK_INSERT: return "INSERT";
            case KeyID.VK_HELP: return "HELP";
            case KeyID.VK_META: return "META";
            case KeyID.VK_BACK_QUOTE: return "BACK_QUOTE";
            case KeyID.VK_QUOTE: return "QUOTE";
            case KeyID.VK_KP_UP: return "KP_UP";
            case KeyID.VK_KP_DOWN: return "KP_DOWN";
            case KeyID.VK_KP_LEFT: return "KP_LEFT";
            case KeyID.VK_KP_RIGHT: return "KP_RIGHT";
            case KeyID.VK_DEAD_GRAVE: return "DEAD_GRAVE";
            case KeyID.VK_DEAD_ACUTE: return "DEAD_ACUTE";
            case KeyID.VK_DEAD_CIRCUMFLEX: return "DEAD_CIRCUMFLEX";
            case KeyID.VK_DEAD_TILDE: return "DEAD_TILDE";
            case KeyID.VK_DEAD_MACRON: return "DEAD_MACRON";
            case KeyID.VK_DEAD_BREVE: return "DEAD_BREVE";
            case KeyID.VK_DEAD_ABOVEDOT: return "DEAD_ABOVEDOT";
            case KeyID.VK_DEAD_DIAERESIS: return "DEAD_DIAERESIS";
            case KeyID.VK_DEAD_ABOVERING: return "DEAD_ABOVERING";
            case KeyID.VK_DEAD_DOUBLEACUTE: return "DEAD_DOUBLEACUTE";
            case KeyID.VK_DEAD_CARON: return "DEAD_CARON";
            case KeyID.VK_DEAD_CEDILLA: return "DEAD_CEDILLA";
            case KeyID.VK_DEAD_OGONEK: return "DEAD_OGONEK";
            case KeyID.VK_DEAD_IOTA: return "DEAD_IOTA";
            case KeyID.VK_DEAD_VOICED_SOUND: return "DEAD_VOICED_SOUND";
            case KeyID.VK_DEAD_SEMIVOICED_SOUND: return "DEAD_SEMIVOICED_SOUND";
            case KeyID.VK_AMPERSAND: return "AMPERSAND";
            case KeyID.VK_ASTERISK: return "ASTERISK";
            case KeyID.VK_QUOTEDBL: return "QUOTEDBL";
            case KeyID.VK_LESS: return "LESS";
            case KeyID.VK_GREATER: return "GREATER";
            case KeyID.VK_BRACELEFT: return "BRACELEFT";
            case KeyID.VK_BRACERIGHT: return "BRACERIGHT";
            case KeyID.VK_AT: return "AT";
            case KeyID.VK_COLON: return "COLON";
            case KeyID.VK_CIRCUMFLEX: return "CIRCUMFLEX";
            case KeyID.VK_DOLLAR: return "DOLLAR";
            case KeyID.VK_EURO_SIGN: return "EURO_SIGN";
            case KeyID.VK_EXCLAMATION_MARK: return "EXCLAMATION_MARK";
            case KeyID.VK_INVERTED_EXCLAMATION_MARK: return "INVERTED_EXCLAMATION_MARK";
            case KeyID.VK_LEFT_PARENTHESIS: return "LEFT_PARENTHESIS";
            case KeyID.VK_NUMBER_SIGN: return "NUMBER_SIGN";
            case KeyID.VK_PLUS: return "PLUS";
            case KeyID.VK_RIGHT_PARENTHESIS: return "RIGHT_PARENTHESIS";
            case KeyID.VK_UNDERSCORE: return "UNDERSCORE";
            case KeyID.VK_WINDOWS: return "WINDOWS";
            case KeyID.VK_CONTEXT_MENU: return "CONTEXT_MENU";
            case KeyID.VK_FINAL: return "FINAL";
            case KeyID.VK_CONVERT: return "CONVERT";
            case KeyID.VK_NONCONVERT: return "NONCONVERT";
            case KeyID.VK_ACCEPT: return "ACCEPT";
            case KeyID.VK_MODECHANGE: return "MODECHANGE";
            case KeyID.VK_KANA: return "KANA";
            case KeyID.VK_KANJI: return "KANJI";
            case KeyID.VK_ALPHANUMERIC: return "ALPHANUMERIC";
            case KeyID.VK_KATAKANA: return "KATAKANA";
            case KeyID.VK_HIRAGANA: return "HIRAGANA";
            case KeyID.VK_FULL_WIDTH: return "FULL_WIDTH";
            case KeyID.VK_HALF_WIDTH: return "HALF_WIDTH";
            case KeyID.VK_ROMAN_CHARACTERS: return "ROMAN_CHARACTERS";
            case KeyID.VK_ALL_CANDIDATES: return "ALL_CANDIDATES";
            case KeyID.VK_PREVIOUS_CANDIDATE: return "PREVIOUS_CANDIDATE";
            case KeyID.VK_CODE_INPUT: return "CODE_INPUT";
            case KeyID.VK_JAPANESE_KATAKANA: return "JAPANESE_KATAKANA";
            case KeyID.VK_JAPANESE_HIRAGANA: return "JAPANESE_HIRAGANA";
            case KeyID.VK_JAPANESE_ROMAN: return "JAPANESE_ROMAN";
            case KeyID.VK_KANA_LOCK: return "KANA_LOCK";
            case KeyID.VK_INPUT_METHOD_ON_OFF: return "INPUT_METHOD_ON_OFF";
            case KeyID.VK_CUT: return "CUT";
            case KeyID.VK_COPY: return "COPY";
            case KeyID.VK_PASTE: return "PASTE";
            case KeyID.VK_UNDO: return "UNDO";
            case KeyID.VK_AGAIN: return "AGAIN";
            case KeyID.VK_FIND: return "FIND";
            case KeyID.VK_PROPS: return "PROPS";
            case KeyID.VK_STOP: return "STOP";
            case KeyID.VK_COMPOSE: return "COMPOSE";
            case KeyID.VK_ALT_GRAPH: return "ALT_GRAPH";
            case KeyID.VK_BEGIN: return "BEGIN";
            case KeyID.VK_UNDEFINED: return "UNDEFINED";
            case KeyID.VK_SHIFT_LEFT: return "SHIFT_LEFT";
            case KeyID.VK_SHIFT_RIGHT: return "SHIFT_RIGHT";
            case KeyID.VK_CONTROL_LEFT: return "CONTROL_LEFT";
            case KeyID.VK_CONTROL_RIGHT: return "CONTROL_RIGHT";
            default: return null;
        }
    }
    
    public static final char INVALID_ASSOCIATED_CHAR = '\u0000';
}
