package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.Tiles.Tile;

@RunWith(Parameterized.class)
public class CaveColorsTest {
	
	@Parameters(name = "{0}")
	public static List<Tiles.Tile> getTiles() {
		return Arrays.stream(Tile.values()).filter(Tile::isCave).collect(Collectors.toList());
	}
	
	
	@Parameter
	public Tile tile;

	@Test
	public void test() {
		Color color = CaveColors.getColorFor(tile);
		if (tile.isOreCave() || tile == Tile.TILE_CAVE_WALL_ROCKSALT || tile == Tile.TILE_CAVE_WALL_LAVA) {
			Assertions.assertThat(color).isNotEqualTo(Color.PINK);
		} else if (tile.isSolidCave()) {
			Assertions.assertThat(color).isEqualTo(Color.DARK_GRAY);
		} else if (tile.isReinforcedCave()) {
			Assertions.assertThat(color).isEqualTo(Color.DARK_GRAY);
		} else {
			Assertions.assertThat(color).isEqualTo(Color.PINK);
			Assertions.assertThat(CaveColors.getMappings()).containsKey(tile);
		}
	}

}
