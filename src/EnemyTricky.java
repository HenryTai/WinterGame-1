import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class EnemyTricky extends GameObject{
	public EnemyTricky(int x, int y, GameObjectID id) {
		super(x, y, id);
		
		Random r = new Random();
		veloX = r.nextInt(5) - 2;
		veloY = 3 - Math.abs(veloX);
		dmg = -10;
		health = 5;
	}

	@Override
	public void updateLogic() {
		if(y < 0 || y > Main.HEIGHT - 15 - 28) veloY *= -1;
		if(x < 0 || x > Main.WIDTH - 15 - 28) veloX *= -1;
		x += veloX;
		y += veloY;
	}

	@Override
	public void updateGraphic(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(x, y, 15, 15);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 15, 15);
	}
}
