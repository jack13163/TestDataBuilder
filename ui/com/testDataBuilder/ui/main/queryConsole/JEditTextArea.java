package com.testDataBuilder.ui.main.queryConsole;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.text.PlainDocument;

import org.syntax.jedit.DefaultInputHandler;
import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.SyntaxUtilities;
import org.syntax.jedit.TextAreaDefaults;

public class JEditTextArea extends org.syntax.jedit.JEditTextArea {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TAB_SIZE = 4;
    public JEditTextArea() {
        this(createTextAreaDefaults());  
        this.getDocument().putProperty(PlainDocument.tabSizeAttribute, TAB_SIZE);
        this.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
    }

    public JEditTextArea(TextAreaDefaults defaults) {
        super(defaults);
        this.getDocument().putProperty(PlainDocument.tabSizeAttribute, TAB_SIZE);
        this.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
    }

    private static TextAreaDefaults createTextAreaDefaults() {
        TextAreaDefaults retval = new TextAreaDefaults();
        retval.inputHandler = new DefaultInputHandler();
        retval.inputHandler.addDefaultKeyBindings();
        retval.document = new SyntaxDocument();
        retval.editable = true;

        retval.blockCaret = false;
        retval.caretVisible = true;
        retval.caretBlinks = true;
        retval.electricScroll = 0;

        retval.cols = 80;
        retval.rows = 25;
//        retval.cols = 0;
//        retval.rows = 0;
        retval.styles = SyntaxUtilities.getDefaultSyntaxStyles();
        retval.caretColor = Color.black; // Color.red;
        retval.selectionColor = new Color(0xccccff);
        retval.lineHighlightColor = new Color(0xe0e0e0);
        retval.lineHighlight = true;
        retval.bracketHighlightColor = Color.black;
        retval.bracketHighlight = true;
        retval.eolMarkerColor = new Color(0x009999);
        retval.eolMarkers = false; // true;
        retval.paintInvalid = false; //true;
        return retval;
    }
}
