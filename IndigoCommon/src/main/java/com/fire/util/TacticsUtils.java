package com.fire.util;

import java.util.Random;

public class TacticsUtils
{
	/**
	 * 90度转弧度
	 */
	public final static double HALFPI = Math.PI * 0.5;
	/**
	 * 角度转弧
	 */
	public final static double DEG2RAD = Math.PI / 180;

	/**
	 * 弧度转角度
	 */
	public final static double RAD2DEG = 180 / Math.PI;

	/**
	 * 弧度规格化范围
	 */
	public final static double DIRECTION_REGULAR_SCOPE = Math.PI * 2;

	/**
	 * 角度规格化范围
	 */
	public final static double DIRECTION_REGULAR_ANGLE_SCOPE = 360;

	/**
	 * 弧度规格化步长
	 */
	public final static double DIRECTION_CHECK_STEP = Math.PI;

	/**
	 * 角度规格化步长
	 */
	public final static double DIRECTION_CHECK_ANGLE_STEP = 180;

	/**
	 * 弧度规格化范围
	 */
	public final static double AXIS_SECTION_FACTOR = 4.0 / Math.PI;
	
	/**
	 * 规格化单位弧度
	 */
	public final static double AXIS_SECTION_DIRECTION = Math.PI/4.0;

	/**
	 * 有效偏移判断
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @param scopex
	 * @param scopey
	 * @return
	 */
	public static boolean inScope(int p1x, int p1y, int p2x, int p2y, int scopex, int scopey)
	{
		return (Math.abs(p1x - p2x) < scopex && Math.abs(p1y - p2y) < scopey);
	}

	/**
	 * 两点之间快速距离计算
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @return
	 */
	public static double fastApproxDistance(int p1x, int p1y, int p2x, int p2y)
	{
//		p1x = (p1x > p2x) ? (p1x - p2x) : (p2x - p1x);
//		p1y = (p1y > p2y) ? (p1y - p2y) : (p2y - p1y);
//		return ((p1x > p1y) ? (p1x + 0.43 * p1y) : (p1y + 0.43 * p1x));
		
		return fastApproxDeltaDistance(p1x - p2x, p1y - p2y);
	}

	/**
	 * 两点之间快速距离计算
	 * 
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @return
	 */
//	public static int fastApproxDeltaDistance(int deltax, int deltay)
//	{
//		deltax = (deltax > 0) ? deltax : -deltax;
//		deltay = (deltay > 0) ? deltay : -deltay;
//		return (int) ((deltax > deltay) ? (deltax + 0.43 * deltay) : (deltay + 0.43 * deltax));
//	}
	
	public static double fastApproxDeltaDistance(double deltax, double deltay)
	{
//		deltax = (deltax > 0) ? deltax : -deltax;
//		deltay = (deltay > 0) ? deltay : -deltay;
//		return ((deltax > deltay) ? (deltax + 0.43 * deltay) : (deltay + 0.43 * deltax));
		
		return Math.sqrt(deltax * deltax + deltay * deltay);
	}

	/**
	 * 计算atan2角度
	 * 
	 * @param deltax
	 * @param deltay
	 * @return
	 */
	public static double atan2(int deltax, int deltay)
	{
		if (Math.abs(deltax) > 1)
			return Math.atan2(deltay, deltax);
		else
		{
			if (deltay > 0)
				return HALFPI;
			else
				return -HALFPI;
		}
	}

	/**
	 * 规格化弧度方向
	 * 
	 * @param direction
	 * @return
	 */
	public static double regularDirection(double direction)
	{
		if (direction >= DIRECTION_REGULAR_SCOPE)
		{
			while (direction >= DIRECTION_REGULAR_SCOPE)
			{
				direction -= DIRECTION_REGULAR_SCOPE;
			}
		}
		else if (direction < 0)
		{
			while (direction < 0)
			{
				direction += DIRECTION_REGULAR_SCOPE;
			}
		}
		return direction;
	}

	/**
	 * 规格化弧度方向差
	 * 
	 * @param direction1
	 * @param direction2
	 * @return
	 */
	public static double regularDiffer(double direction1, double direction2)
	{
		direction1 = regularDirection(direction1);
		direction2 = regularDirection(direction2);
		if (Math.abs(direction1 - direction2) < DIRECTION_CHECK_STEP)
			return direction2 - direction1;
		else if (direction1 >= direction2)
			return direction2 + DIRECTION_REGULAR_SCOPE - direction1;
		else
			return direction2 - DIRECTION_REGULAR_SCOPE - direction1;
	}

	public final static double PHYSCIAL_TO_WORLD_ANGLE_OFFSET = -Math.PI * 0.5;

	public static double moveDirectionToFaceDirection(double moveAngle)
	{
		return regularDirection(PHYSCIAL_TO_WORLD_ANGLE_OFFSET - moveAngle);
	}

	public static double faceDirectionToMoveDirection(double worldAngle)
	{
		return regularDirection(PHYSCIAL_TO_WORLD_ANGLE_OFFSET - worldAngle);
	}

	/**
	 * 随机
	 */
	private static Random random = new Random(System.currentTimeMillis());

	public static int randomInt(int scope)
	{
		if (scope == 0)
			return 0;
		return random.nextInt(scope);
	}

	private final static int DAY_TIME = 24 * 3600 * 1000;

	public static int currentTime()
	{
		return (int) (System.currentTimeMillis() % DAY_TIME);
	}
}
