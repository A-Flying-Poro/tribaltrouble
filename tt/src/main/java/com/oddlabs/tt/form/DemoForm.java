package com.oddlabs.tt.form;

import com.oddlabs.tt.gui.*;
import com.oddlabs.tt.util.Utils;

import java.util.ResourceBundle;

public strictfp class DemoForm extends Form {
	public DemoForm(GUIRoot gui_root, String header, GUIImage img, String text) {
		ResourceBundle bundle = ResourceBundle.getBundle("i18n." + DemoForm.class.getName());
		Label label_headline = new Label(header, Skin.getSkin().getHeadlineFont());
		addChild(label_headline);
		addChild(img);
		LabelBox text_box = new LabelBox(text, Skin.getSkin().getEditFont(), 512);
		addChild(text_box);

		HorizButton buy_button = new BuyButton(gui_root, Utils.getBundleString(bundle, "buy_now_caption"), 120);
		addChild(buy_button);
		HorizButton continue_button = new HorizButton(Utils.getBundleString(bundle, "continue"), 100);
		addChild(continue_button);
		continue_button.addMouseClickListener(new OKListener(this));

		// Place objects
		label_headline.place();
		img.place(label_headline, BOTTOM_LEFT);
		text_box.place(img, BOTTOM_LEFT);
		continue_button.place(text_box, BOTTOM_RIGHT);
		buy_button.place(continue_button, LEFT_MID);
		compileCanvas();
		
		centerPos();
	}

	protected final void keyRepeat(KeyboardEvent event) {
		super.keyPressed(event);
	}
}
