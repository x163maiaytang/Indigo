package com.fire.core.bhns;

import com.fire.task.Task;

public class BOServiceTaskImpl implements Task {

	@Override
	public void run()  {
		ServiceManager.update();
	}

}
