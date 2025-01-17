package com.oddlabs.tt.model.weapon;

import com.oddlabs.tt.audio.Audio;
import com.oddlabs.tt.model.Selectable;
import com.oddlabs.tt.model.Unit;
import com.oddlabs.tt.render.SpriteKey;

public final strictfp class IronAxeWeapon extends RotatingThrowingWeapon {
	private final float ROTS_PER_SECOND = 6;
	private final float ANGLE_DELTA = ROTS_PER_SECOND*360f;
	private final static float METERS_PER_SECOND = 25f; //multiplied by meters/second (in 2D)

	public IronAxeWeapon(boolean hit, Unit src, Selectable target, SpriteKey sprite_renderer, Audio throw_sound, Audio[] hit_sounds) {
		super(hit, src, target, sprite_renderer, throw_sound, hit_sounds);
	}

	protected final float getAngleVelocity() {
		return ANGLE_DELTA;
	}

	protected final float getMetersPerSecond() {
		return METERS_PER_SECOND;
	}

	protected final int getDamage() {
		return 2;
	}
}
