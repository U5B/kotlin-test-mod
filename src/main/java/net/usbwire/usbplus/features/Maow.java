package net.usbwire.usbplus.features;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import gg.essential.elementa.*;
import gg.essential.elementa.components.*;
import gg.essential.elementa.constraints.*;
import gg.essential.elementa.state.*;
import gg.essential.universal.*;
import net.minecraft.client.world.ClientWorld;
import net.usbwire.usbplus.config.Config;
import net.usbwire.usbplus.hud.CustomCenterConstraint;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;

public class Maow {
	public static final State<Float> xPos = new BasicState<>(Config.maowX);
	public static final State<Float> yPos = new BasicState<>(Config.maowY);
	public static final State<Float> width = new BasicState<>((float) Config.maowWidth);
	public static final State<Float> height = new BasicState<>((float) Config.maowHeight);
	public static final State<Float> imageWidth = new BasicState<>(1f);
	public static final State<Float> imageHeight = new BasicState<>(1f);
	public static final State<Color> color = new BasicState<>(new Color(255, 255, 255, Config.maowAlpha));
	public static final Window window = new Window(ElementaVersion.V7, 60);
	public static final UIContainer container = new UIContainer();
	static {
		container.setX(new CustomCenterConstraint(xPos));
		container.setY(new CustomCenterConstraint(yPos));
		container.setWidth(new ChildBasedMaxSizeConstraint());
		container.setHeight(new ChildBasedSizeConstraint());
		window.addChild(container);
	}

	public static boolean dirty = false;
	public static boolean downloading = false;
	public static long lastDownload = 0;

	static BufferedImage meownya; 
	
	public static void init() {
		if (!Config.maowEnabled || downloading) {
			return;
		}

		try {
			// fetch the image!
			downloading = true;
			CompletableFuture<BufferedImage> nya = CompletableFuture.supplyAsync(() -> {
				try {
					BufferedImage asset = UIImage.get(new URL(Config.maowUrl));
					imageWidth.set((float) asset.getWidth());
					imageHeight.set((float) asset.getHeight());
					meownya = asset;
					return asset;
				} catch (Exception e) {
					return null;
				} finally {
					lastDownload = System.currentTimeMillis();
					downloading = false;
				}
			});
			UIImage image = new UIImage(nya);
			image.setX(new PixelConstraint(0));
			image.setY(new PixelConstraint(0));
			if (width.get() > 0) {
				image.setWidth(new PixelConstraint(width));
				} else if (height.get() > 0) {
				image.setWidth(new ImageAspectConstraint());
			} else {
				image.setWidth(new PixelConstraint(imageWidth));
			}
			if (height.get() > 0) {
				image.setHeight(new PixelConstraint(height));
			} else if (width.get() > 0) {
				image.setHeight(new ImageAspectConstraint());
			} else {
				image.setHeight(new PixelConstraint(imageWidth));
			}
			image.setColor(new ConstantColorConstraint(color));
			container.clearChildren();
			container.addChild(image);
		} catch (Exception e) {
			e.printStackTrace();
			Config.maowEnabled = false;
		}
	}

	public static void clientTick() {
		long time = System.currentTimeMillis();
		if (Config.maowRefreshRate > 0 && time - lastDownload >= Config.maowRefreshRate * 1000) {
			init();
		}
	}

	public static void saveImage(){
		if (meownya == null) {
			return;
		}
		Date date = new Date();
		String filename = "catgirl" + date.toString().replace(" ", "_").replace(":", "_") + ".png";
		System.out.println(filename);
		File folder = new File(mc.gameDirectory, "maow");
		folder.mkdir();
		File outputfile = new File(folder, filename);

		try{
			outputfile.createNewFile();
			ImageIO.write(meownya, "png", outputfile);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void draw(UMatrixStack matrix) {
		if (!Config.maowEnabled)
			return;
		if (!container.getChildren().isEmpty())
			window.draw(matrix);
	}
}
