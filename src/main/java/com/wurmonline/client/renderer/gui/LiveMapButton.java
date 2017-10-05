package com.wurmonline.client.renderer.gui;

import com.wurmonline.client.renderer.backend.Queue;
import com.wurmonline.client.resources.textures.Texture;

public class LiveMapButton extends WButton {

	private final Texture texture;

	public LiveMapButton(final String label, final String hoverString, int width, int height, Texture texture, final ButtonListener buttonListener) {
		super(label, buttonListener);
		if (!hoverString.isEmpty()) {
			this.setHoverString(hoverString);
		}
		
		this.sizeFlags = 0;
		setInitialSize(width, height, false);
		this.sizeFlags = FlexComponent.FIXED_WIDTH | FlexComponent.FIXED_HEIGHT;
		
		this.texture = texture;
	}

	@Override
	protected void renderComponent(Queue queue, final float alpha) {
		if (!this.hoverMode || this.hovered) {

			float r2 = this.r;
			float g2 = this.g;
			float b2 = this.b;

			if ((this.isCloseHovered || this.isDown) && this.isEnabled()) {

				r2 /= 2.0f;
				g2 /= 2.0f;
				b2 /= 2.0f;
			}

			if (this.texture != null) {
				this.drawTexture(queue, (Texture)this.texture, r2, g2, b2, 1.0f, this.x, this.y, this.width, this.height, 0, 0, 256, 256);
			}
		}
		
		final int yo2 = ((this.isCloseHovered || this.isDown) && this.isEnabled()) ? 1 : 0;
		final float c = ((this.isCloseHovered || this.isDown) && this.isEnabled()) ? 0.8f : 1.0f;
		this.text.moveTo(this.x + 4 + 0, this.y + this.text.getHeight() + yo2 + 0);
		this.text.paint(queue, this.label, this.rText * c, this.gText *c, this.bText * c, 1.0f);
	}

}
