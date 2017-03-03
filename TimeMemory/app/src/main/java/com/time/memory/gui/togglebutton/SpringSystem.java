package com.time.memory.gui.togglebutton;

/**
 * This is a wrapper for BaseSpringSystem that provides the convenience of
 * automatically providing the AndroidSpringLooper dependency in
 * {@link SpringSystem#create}. <br/>
 * 类似“弹簧”动画效果的第三方工具包
 * 
 */
public class SpringSystem extends BaseSpringSystem {

	/**
	 * Create a new SpringSystem providing the appropriate constructor
	 * parameters to work properly in an Android environment.
	 * 
	 * @return the SpringSystem
	 */
	public static SpringSystem create() {
		return new SpringSystem(AndroidSpringLooperFactory.createSpringLooper());
	}

	private SpringSystem(SpringLooper springLooper) {
		super(springLooper);
	}

}