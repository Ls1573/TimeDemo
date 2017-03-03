package com.time.memory.gui.swipback.swipeback.util;

/**
 * Little helper class that provides some useful mathematical methods
 * 
 * @author Hannes Dorfmann
 * 
 */
public class MathUtils {

	/**
	 * This method maps a number x, which is in the range [sourceStart,
	 * sourceEnd], to a new range [targetStart, targetEnd]
	 * 
	 * <p>
	 * sourceStart <= x <= sourceEnd <br/>
	 * targetStart <= returnValue <= targetEnd
	 * </p>
	 * 
	 * @param x
	 *            The value that should be mapped
	 * @param sourceStart
	 *            The source range start (inclusive)
	 * @param sourceEnd
	 *            The source range end (inclusive)
	 * @param targetStart
	 *            The target range start (inclusive)
	 * @param targetEnd
	 *            The target range end (inclusive)
	 * @return The corresponding value of x in the target range
	 */
	public static float mapPoint(float x, float sourceStart, float sourceEnd,
			float targetStart, float targetEnd) {

		if (x <= sourceStart) {
			return targetStart;
		}

		if (x >= sourceEnd) {
			return targetEnd;
		}

		return (x - sourceStart) / (sourceEnd - sourceStart)
				* (targetEnd - targetStart) + targetStart;
	}

	/**
	 * <b>This is the same as
	 * {@link #mapPoint(float, float, float, float, float)}but without rounding
	 * the integer up. Use {@link #mapPointRound(float, float, float, int, int)}
	 * if you want rounded results</b>
	 * <p>
	 * This method maps a number x, which is in the range [sourceStart,
	 * sourceEnd], to a new range [targetStart, targetEnd]
	 * </p>
	 * <p>
	 * sourceStart <= x <= sourceEnd <br/>
	 * targetStart <= returnValue <= targetEnd
	 * </p>
	 * 
	 * @param x
	 *            The value that should be mapped
	 * @param sourceStart
	 *            The source range start (inclusive)
	 * @param sourceEnd
	 *            The source range end (inclusive)
	 * @param targetStart
	 *            The target range start (inclusive)
	 * @param targetEnd
	 *            The target range end (inclusive)
	 * @return The corresponding value of x in the target range
	 */
	public static int mapPoint(float x, float sourceStart, float sourceEnd,
			int targetStart, int targetEnd) {

		if (x <= sourceStart) {
			return targetStart;
		}

		if (x >= sourceEnd) {
			return targetEnd;
		}

		float fRes = (x - sourceStart) / (sourceEnd - sourceStart)
				* (targetEnd - targetStart) + targetStart;

		return (int) fRes;
	}

	/**
	 * <b>This is the same as
	 * {@link #mapPoint(float, float, float, float, float)}but rounds to
	 * integer.</b>
	 * <p>
	 * This method maps a number x, which is in the range [sourceStart,
	 * sourceEnd], to a new range [targetStart, targetEnd]
	 * </p>
	 * <p>
	 * sourceStart <= x <= sourceEnd <br/>
	 * targetStart <= returnValue <= targetEnd
	 * </p>
	 * 
	 * @param x
	 *            The value that should be mapped
	 * @param sourceStart
	 *            The source range start (inclusive)
	 * @param sourceEnd
	 *            The source range end (inclusive)
	 * @param targetStart
	 *            The target range start (inclusive)
	 * @param targetEnd
	 *            The target range end (inclusive)
	 * @return The corresponding value of x in the target range
	 */
	public static int mapPointRound(float x, float sourceStart,
			float sourceEnd, int targetStart, int targetEnd) {

		if (x <= sourceStart) {
			return targetStart;
		}

		if (x >= sourceEnd) {
			return targetEnd;
		}

		float fRes = (x - sourceStart) / (sourceEnd - sourceStart)
				* (targetEnd - targetStart) + targetStart;

		return (int) (fRes + 0.5f);
	}

	/**
	 * Checks if a value is between up and down (inclusive up and down)
	 * 
	 * @param x
	 * @param down
	 * @param up
	 * @return
	 */
	public static boolean isBetween(float x, float down, float up) {
		return x >= down && x <= up;
	}
}
