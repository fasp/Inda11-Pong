package fasp.pong;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Line;

public class Brick {
	private Rectangle brick;
	private int durability;
	private Line rightSide;
	private Line leftSide;
	private Line bottomSide;
	private Line topSide;
	
public Brick(float positionX, float positionY, float width, float height, int durability)
{
	brick = new Rectangle(positionX, positionY, width, height);
	this.durability = durability;
	
	rightSide = new Line(positionX + width, positionY, positionX + width, positionY + height);
	leftSide = new Line(positionX, positionY, positionX, positionY + height);
	bottomSide = new Line(positionX, positionY + height, positionX + width, positionY + height);
	topSide = new Line(positionX, positionY, positionX + width, positionY);
}
public Rectangle getRectangle()
{
	return brick;
}
public Line getRightSide()
{
	return rightSide;
}
public Line getLeftSide()
{
	return leftSide;
}
public Line getBottomSide()
{
	return bottomSide;
}
public Line getTopSide()
{
	return topSide;
}
public int getDurability()
{
	return durability;
}
public void incrementDurabilty()
{
	durability++;
}
public void decrementDurability()
{
	durability--;
}
public void alterDurabilty(int amount)
{
	durability+=amount;
}
public float getWidth()
{
	return brick.getWidth();
}
public float getHeight()
{
	return brick.getHeight();
}
}
