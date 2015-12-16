package com.wurmonline.client.renderer.gui;

import org.lwjgl.opengl.GL11;

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
	protected void renderComponent(final float alpha) {
		if (!this.hoverMode || this.hovered) {

			GL11.glEnable(3553);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);

			float r2 = this.r;
			float g2 = this.g;
			float b2 = this.b;

			if ((this.isCloseHovered || this.isDown) && this.isEnabled()) {

				r2 /= 2.0f;
				g2 /= 2.0f;
				b2 /= 2.0f;
			}
			GL11.glColor4f(r2, g2, b2, 1.0f);

			if (this.texture != null) {

				this.texture.switchTo();
				GL11.glBegin(7);

				GL11.glTexCoord2f(0.0f, 0.0f);
				GL11.glVertex2f((float) (this.x + 0), (float) (this.y + 0));

				GL11.glTexCoord2f(0.0f, 1.0f);
				GL11.glVertex2f((float) (this.x + 0), (float) (this.y + this.height));

				GL11.glTexCoord2f(1.0f, 1.0f);
				GL11.glVertex2f((float) (this.x + this.width), (float) (this.y + this.height));

				GL11.glTexCoord2f(1.0f, 0.0f);
				GL11.glVertex2f((float) (this.x + this.width), (float) (this.y + 0));

				GL11.glEnd();
			}
			GL11.glDisable(3553);
			GL11.glDisable(3042);

		}
		final int yo2 = ((this.isCloseHovered || this.isDown) && this.isEnabled()) ? 1 : 0;
		final float c = ((this.isCloseHovered || this.isDown) && this.isEnabled()) ? 0.8f : 1.0f;
		GL11.glColor4f(this.rText * c, this.gText * c, this.bText * c, 1.0f);
		this.text.moveTo(this.x + 4 + 0, this.y + this.text.getHeight() + yo2 + 0);
		this.text.paint(this.label);
		if (c == 0.8f || this.rText != 1.0f || this.gText != 1.0f || this.bText != 1.0f) {

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

}
