package com.oddlabs.tt.delegate;

import com.oddlabs.net.NetworkSelector;
import com.oddlabs.tt.camera.Camera;
import com.oddlabs.tt.form.*;
import com.oddlabs.tt.global.Settings;
import com.oddlabs.tt.gui.GUIRoot;
import com.oddlabs.tt.gui.ImageBuyButton;
import com.oddlabs.tt.gui.LocalInput;
import com.oddlabs.tt.gui.MenuButton;
import com.oddlabs.tt.guievent.MouseClickListener;
import com.oddlabs.tt.net.Network;
import com.oddlabs.tt.render.Renderer;
import com.oddlabs.tt.util.Utils;

public final strictfp class MainMenu extends Menu {
	public MainMenu(NetworkSelector network, GUIRoot gui_root, Camera camera) {
		super(network, gui_root, camera);
		reload();
	}

	private void addGameTypeButtons() {
		MenuButton tutorial = new MenuButton(Utils.getBundleString(bundle, "tutorial"), COLOR_NORMAL, COLOR_ACTIVE);
		addChild(tutorial);
		tutorial.addMouseClickListener(new TutorialListener());
		MenuButton campaign_menu = new MenuButton(Utils.getBundleString(bundle, "campaign"), COLOR_NORMAL, COLOR_ACTIVE);
		addChild(campaign_menu);
		campaign_menu.addMouseClickListener(new CampaignListener());
		MenuButton single_player = new MenuButton(Utils.getBundleString(bundle, "skirmish"), COLOR_NORMAL, COLOR_ACTIVE);
		addChild(single_player);
		single_player.addMouseClickListener(new SinglePlayerListener());
		if (!Settings.getSettings().hide_multiplayer) {
			MenuButton multi_player = new MenuButton(Utils.getBundleString(bundle, "multiplayer"), COLOR_NORMAL, COLOR_ACTIVE);
			addChild(multi_player);
			multi_player.addMouseClickListener(new MultiPlayerListener());
		}
	}

	private void addRegisterButton() {
		if (!Renderer.isRegistered() && !Settings.getSettings().hide_register) {
			MenuButton register_game = new MenuButton(Utils.getBundleString(bundle, "register"), COLOR_NORMAL, COLOR_ACTIVE);
			addChild(register_game);
			register_game.addMouseClickListener(new RegisterListener());
		}
	}

	private void addBuyButton() {
		if (!Renderer.isRegistered() && Settings.getSettings().online) {
			ImageBuyButton img_buy = new ImageBuyButton(getGUIRoot());
			addChild(img_buy);
		}
	}

	private void addUpdateButton() {
		//noinspection ConstantValue,PointlessBooleanExpression
		if (/*LocalInput.getUpdateInfo() != null*/ false /* Updater removed */ && !Settings.getSettings().hide_update) {
			MenuButton update_game = new MenuButton(Utils.getBundleString(bundle, "update"), COLOR_NORMAL, COLOR_ACTIVE);
			addChild(update_game);
//			update_game.addMouseClickListener(new UpdateGameListener());
		}
	}

	protected void addButtons() {
		addGameTypeButtons();
		
		addRegisterButton();
		
		addDefaultOptionsButton();

		addUpdateButton();

		addExitButton();

		addBuyButton();

		if (Network.getMatchmakingClient().isConnected()) {
			new SelectGameMenu(getNetwork(), getGUIRoot(), this);
		}
	}

	private final strictfp class MultiPlayerListener implements MouseClickListener {
		public final void mouseClicked(int button, int x, int y, int clicks) {
			if (Network.getMatchmakingClient().isConnected()) {
				new SelectGameMenu(getNetwork(), getGUIRoot(), MainMenu.this);
			} else {
				Network.getMatchmakingClient().close();
				new LoginForm(getNetwork(), getGUIRoot(), MainMenu.this);
			}
		}
	}

	private final strictfp class RegisterListener implements MouseClickListener {
		public final void mouseClicked(int button, int x, int y, int clicks) {
			setMenuCentered(new RegistrationForm(getGUIRoot(), true, MainMenu.this));
		}
	}

	private final strictfp class TutorialListener implements MouseClickListener {
		public final void mouseClicked(int button, int x, int y, int clicks) {
			setMenu(new TutorialForm(getNetwork(), getGUIRoot()));
		}
	}

	private final strictfp class CampaignListener implements MouseClickListener {
		public final void mouseClicked(int button, int x, int y, int clicks) {
			setMenu(new CampaignForm(getNetwork(), getGUIRoot(), MainMenu.this));
		}
	}

	private final strictfp class SinglePlayerListener implements MouseClickListener {
		public final void mouseClicked(int button, int x, int y, int clicks) {
			setMenu(new TerrainMenuForm(getNetwork(), getGUIRoot(), MainMenu.this));
		}
	}
}
