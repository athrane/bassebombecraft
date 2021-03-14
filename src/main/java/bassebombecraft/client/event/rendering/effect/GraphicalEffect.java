package bassebombecraft.client.event.rendering.effect;

import bassebombecraft.operator.Ports;

/**
 * Graphical effect.
 */
public interface GraphicalEffect {

	/**
	 * Render effect using ports.
	 */
	public void render(Ports ports);

	/**
	 * Return ID used to identify instance in repositories.
	 * 
	 * @return ID used to identify instance in repositories
	 */
	public String getId();
}
