package pacman;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

/**
 * Preloads bitmaps needed for game and provides easy access to them.
 */
public class BitmapServer {

	/**
	 * Enum listing all game bitmaps.
	 */
	public enum Bitmap {
		BACKDROP (0),
		PACMAN (1),
		PACMAN_RIGHT_1 (2),
		PACMAN_RIGHT_2 (3),
		PACMAN_RIGHT_3 (4),
		PACMAN_LEFT_1 (5),
		PACMAN_LEFT_2 (6),
		PACMAN_LEFT_3 (7),
		PACMAN_UP_1 (8),
		PACMAN_UP_2 (9),
		PACMAN_UP_3 (10),
		PACMAN_DOWN_1 (11),
		PACMAN_DOWN_2 (12),
		PACMAN_DOWN_3 (13),
		GHOST_RED_RIGHT_0 (14),
		GHOST_RED_RIGHT_1 (15),
		GHOST_RED_RIGHT_2 (16),
		GHOST_RED_RIGHT_3 (17),
		GHOST_RED_LEFT_0 (18),
		GHOST_RED_LEFT_1 (19),
		GHOST_RED_LEFT_2 (20),
		GHOST_RED_LEFT_3 (21),
		GHOST_RED_UP_0 (22),
		GHOST_RED_UP_1 (23),
		GHOST_RED_UP_2 (24),
		GHOST_RED_UP_3 (25),
		GHOST_RED_DOWN_0 (26),
		GHOST_RED_DOWN_1 (27),
		GHOST_RED_DOWN_2 (28),
		GHOST_RED_DOWN_3 (29),
		GHOST_BLUE_RIGHT_0 (30),
		GHOST_BLUE_RIGHT_1 (31),
		GHOST_BLUE_RIGHT_2 (32),
		GHOST_BLUE_RIGHT_3 (33),
		GHOST_BLUE_LEFT_0 (34),
		GHOST_BLUE_LEFT_1 (35),
		GHOST_BLUE_LEFT_2 (36),
		GHOST_BLUE_LEFT_3 (37),
		GHOST_BLUE_UP_0 (38),
		GHOST_BLUE_UP_1 (39),
		GHOST_BLUE_UP_2 (40),
		GHOST_BLUE_UP_3 (41),
		GHOST_BLUE_DOWN_0 (42),
		GHOST_BLUE_DOWN_1 (43),
		GHOST_BLUE_DOWN_2 (44),
		GHOST_BLUE_DOWN_3 (45),
		GHOST_PINK_RIGHT_0 (46),
		GHOST_PINK_RIGHT_1 (47),
		GHOST_PINK_RIGHT_2 (48),
		GHOST_PINK_RIGHT_3 (49),
		GHOST_PINK_LEFT_0 (50),
		GHOST_PINK_LEFT_1 (51),
		GHOST_PINK_LEFT_2 (52),
		GHOST_PINK_LEFT_3 (53),
		GHOST_PINK_UP_0 (54),
		GHOST_PINK_UP_1 (55),
		GHOST_PINK_UP_2 (56),
		GHOST_PINK_UP_3 (57),
		GHOST_PINK_DOWN_0 (58),
		GHOST_PINK_DOWN_1 (59),
		GHOST_PINK_DOWN_2 (60),
		GHOST_PINK_DOWN_3 (61),
		GHOST_ORANGE_RIGHT_0 (62),
		GHOST_ORANGE_RIGHT_1 (63),
		GHOST_ORANGE_RIGHT_2 (64),
		GHOST_ORANGE_RIGHT_3 (65),
		GHOST_ORANGE_LEFT_0 (66),
		GHOST_ORANGE_LEFT_1 (67),
		GHOST_ORANGE_LEFT_2 (68),
		GHOST_ORANGE_LEFT_3 (69),
		GHOST_ORANGE_UP_0 (70),
		GHOST_ORANGE_UP_1 (71),
		GHOST_ORANGE_UP_2 (72),
		GHOST_ORANGE_UP_3 (73),
		GHOST_ORANGE_DOWN_0 (74),
		GHOST_ORANGE_DOWN_1 (75),
		GHOST_ORANGE_DOWN_2 (76),
		GHOST_ORANGE_DOWN_3 (77),
		GHOST_EDIBLE_0 (78),
		GHOST_EDIBLE_1 (79),
		GHOST_EDIBLE_2 (80),
		GHOST_EDIBLE_3 (81),
		GHOST_EYES_U (82),
		GHOST_EYES_D (83),
		GHOST_EYES_L (84),
		GHOST_EYES_R (85),
		PACMAN_DEAD_0 (86),
		PACMAN_DEAD_1 (87),
		PACMAN_DEAD_2 (88),
		PACMAN_DEAD_3 (89),
		PACMAN_DEAD_4 (90),
		PACMAN_DEAD_5 (91),
		PACMAN_DEAD_6 (92),
		PACMAN_DEAD_7 (93),
		PACMAN_DEAD_8 (94),
		PACMAN_DEAD_9 (95),
		PACMAN_DEAD_10 (96),
		GHOST_EDIBLE_FLASH_0 (97),
		GHOST_EDIBLE_FLASH_1 (98),
		GHOST_EDIBLE_FLASH_2 (99),
		GHOST_EDIBLE_FLASH_3 (100);

		private int mValue;

		Bitmap(int value) {
			mValue = value;
		}

		public int value() { return mValue; }
	}

	// Constants
	final static private String PATH_NAME = "/pacman/resources/bitmaps";
	final static private String[] FILE_NAMES = {
		"backdrop.png",
		"pacman.png",
		"pacmanr1.png",
		"pacmanr2.png",
		"pacmanr3.png",
		"pacmanl1.png",
		"pacmanl2.png",
		"pacmanl3.png",
		"pacmanu1.png",
		"pacmanu2.png",
		"pacmanu3.png",
		"pacmand1.png",
		"pacmand2.png",
		"pacmand3.png",
		"ghost_red_r0.png",
		"ghost_red_r1.png",
		"ghost_red_r2.png",
		"ghost_red_r3.png",
		"ghost_red_l0.png",
		"ghost_red_l1.png",
		"ghost_red_l2.png",
		"ghost_red_l3.png",
		"ghost_red_u0.png",
		"ghost_red_u1.png",
		"ghost_red_u2.png",
		"ghost_red_u3.png",
		"ghost_red_d0.png",
		"ghost_red_d1.png",
		"ghost_red_d2.png",
		"ghost_red_d3.png",
		"ghost_blue_r0.png",
		"ghost_blue_r1.png",
		"ghost_blue_r2.png",
		"ghost_blue_r3.png",
		"ghost_blue_l0.png",
		"ghost_blue_l1.png",
		"ghost_blue_l2.png",
		"ghost_blue_l3.png",
		"ghost_blue_u0.png",
		"ghost_blue_u1.png",
		"ghost_blue_u2.png",
		"ghost_blue_u3.png",
		"ghost_blue_d0.png",
		"ghost_blue_d1.png",
		"ghost_blue_d2.png",
		"ghost_blue_d3.png",
		"ghost_pink_r0.png",
		"ghost_pink_r1.png",
		"ghost_pink_r2.png",
		"ghost_pink_r3.png",
		"ghost_pink_l0.png",
		"ghost_pink_l1.png",
		"ghost_pink_l2.png",
		"ghost_pink_l3.png",
		"ghost_pink_u0.png",
		"ghost_pink_u1.png",
		"ghost_pink_u2.png",
		"ghost_pink_u3.png",
		"ghost_pink_d0.png",
		"ghost_pink_d1.png",
		"ghost_pink_d2.png",
		"ghost_pink_d3.png",
		"ghost_orange_r0.png",
		"ghost_orange_r1.png",
		"ghost_orange_r2.png",
		"ghost_orange_r3.png",
		"ghost_orange_l0.png",
		"ghost_orange_l1.png",
		"ghost_orange_l2.png",
		"ghost_orange_l3.png",
		"ghost_orange_u0.png",
		"ghost_orange_u1.png",
		"ghost_orange_u2.png",
		"ghost_orange_u3.png",
		"ghost_orange_d0.png",
		"ghost_orange_d1.png",
		"ghost_orange_d2.png",
		"ghost_orange_d3.png",
		"ghost_edible_0.png",
		"ghost_edible_1.png",
		"ghost_edible_2.png",
		"ghost_edible_3.png",
		"ghost_eyes_u.png",
		"ghost_eyes_d.png",
		"ghost_eyes_l.png",
		"ghost_eyes_r.png",
		"pacman_dead_0.png",
		"pacman_dead_1.png",
		"pacman_dead_2.png",
		"pacman_dead_3.png",
		"pacman_dead_4.png",
		"pacman_dead_5.png",
		"pacman_dead_6.png",
		"pacman_dead_7.png",
		"pacman_dead_8.png",
		"pacman_dead_9.png",
		"pacman_dead_10.png",
		"ghost_edible_flash_0.png",
		"ghost_edible_flash_1.png",
		"ghost_edible_flash_2.png",
		"ghost_edible_flash_3.png"
	};

	// Members
	private static BufferedImage[] mImages;

	/**
	 * Initialise the bitmap server.
	 */
	public static void init() {
		mImages = new BufferedImage[Bitmap.values().length];

		try {
			for (int i = 0; i < FILE_NAMES.length; ++i) {
				mImages[i] = ImageIO.read(PacMan.class.getResourceAsStream(PATH_NAME + "/" + FILE_NAMES[i]));
			}
		} catch (IOException e) {
			util.Debug.print("Error loading images: " + e);
		}
	}

	/**
	 * Get the specified bitmap as a BufferedImage.
	 * @param bitmap The bitmap to retrieve.
	 * @return A BufferedImage representing the specified bitmap.
	 */
	public static BufferedImage getBitmap(Bitmap bitmap) {
		return mImages[bitmap.value()];
	}
}
