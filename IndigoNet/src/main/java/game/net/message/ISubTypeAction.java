package game.net.message;

import game.net.handler.IAction;


public interface ISubTypeAction<T extends AbstractMessage, P extends Object> extends IAction<T>
{
	public void processMessage(T message, P p);
}
