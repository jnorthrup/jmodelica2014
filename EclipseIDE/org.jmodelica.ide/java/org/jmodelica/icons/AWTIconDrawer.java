package org.jmodelica.icons;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.jmodelica.icons.exceptions.CreateShapeFailedException;
import org.jmodelica.icons.exceptions.FailedConstructionException;
import org.jmodelica.icons.mls.Component;
import org.jmodelica.icons.mls.CoordinateSystem;
import org.jmodelica.icons.mls.Icon;
import org.jmodelica.icons.mls.Placement;
import org.jmodelica.icons.mls.Types;
import org.jmodelica.icons.mls.Types.FillPattern;
import org.jmodelica.icons.mls.Types.LinePattern;
import org.jmodelica.icons.mls.Types.TextAlignment;
import org.jmodelica.icons.mls.primitives.Bitmap;
import org.jmodelica.icons.mls.primitives.Color;
import org.jmodelica.icons.mls.primitives.Ellipse;
import org.jmodelica.icons.mls.primitives.Extent;
import org.jmodelica.icons.mls.primitives.FilledRectShape;
import org.jmodelica.icons.mls.primitives.FilledShape;
import org.jmodelica.icons.mls.primitives.Line;
import org.jmodelica.icons.mls.primitives.Point;
import org.jmodelica.icons.mls.primitives.Polygon;
import org.jmodelica.icons.mls.primitives.Rectangle;
import org.jmodelica.icons.mls.primitives.Text;
import org.jmodelica.ide.ui.ImageLoader;

public class AWTIconDrawer implements GraphicsInterface {
	
	public static final double MINIMUM_FONT_SIZE = 9.0;
	
	public static final BasicStroke DEFAULT_LINE_STROKE = 
		new BasicStroke((float)(Line.DEFAULT_THICKNESS/IconConstants.PIXLES_PER_MM),
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	
	public static final BasicStroke DEFAULT_SHAPE_STROKE = 
		new BasicStroke((float)(FilledShape.DEFAULT_LINE_THICKNESS/IconConstants.PIXLES_PER_MM), 
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
	

	private static final int DEFAULT_FONT_STYLE = Font.PLAIN;
	private static final int DEFAULT_FONT_SIZE = 12;
	
	private boolean outline;
	private BufferedImage image;
	
	private Graphics2D g;
	
	private Stack<AffineTransform> savedTransformations;
	
	private Extent iconExtent;
	
	
	public AWTIconDrawer() {
		savedTransformations = new Stack<AffineTransform>();
		outline = true;
	}
	
	public AWTIconDrawer(Icon icon) {
		savedTransformations = new Stack<AffineTransform>();
		outline = true;
		//createBufferedImage(icon, 0, 0);
		createBufferedImage(icon);
	}
	public AWTIconDrawer(Icon icon, boolean outline) {
		savedTransformations = new Stack<AffineTransform>();
		this.outline = outline;
		createBufferedImage(icon);
	}
	/** Creates an object for making BufferedImages from a ClassIcon.
	 * @param  icon ClassIcon to make a BufferedImage of. 
	 * @param iconView setting iconView to true generates an icon representation of the component. 
	 * Setting iconView to false generates a diagram representation of the component.   
	 * @param outlineIcon setting outlineIcon to true scale the bufferImage object to fit IDE outline size.
	 */
/*
	public void createBufferedImage(Icon icon, int preferredWidth, int preferredHeight) {
		
		
	    float finalImageWidth;
	    float finalImageHeight;
//		skulle kunna kolla h�r om icon.getExtent() returnerar NO_EXTENT och i s� fall inte skapa n�gon bild.	    
	    float imageWidth;
	    float imageHeight;
	    
	    Extent iconBounds = icon.getBounds(icon.getExtent());
	    if(iconBounds.equals(Extent.NO_EXTENT)) {
	    	return;
	    } else {
	    
	    	imageWidth = (float)iconBounds.getWidth() + 10;
	    	imageHeight = (float)iconBounds.getHeight() + 20;
	    }
		if(outline)
	    {	
	    	finalImageWidth=IconConstants.OUTLINE_IMAGE_SIZE-1;
	    		finalImageHeight=IconConstants.OUTLINE_IMAGE_SIZE-1;
		} else {
			
			finalImageWidth = preferredWidth-1;
			finalImageHeight = preferredHeight-1;	
		}
	    float scaleWidth = (float)(1.0f * finalImageWidth/imageWidth);
		float scaleHeight = (float)(1.0f * finalImageHeight/imageHeight);
		
		
	    image = new BufferedImage(
        		(int)finalImageWidth, 
        		(int)finalImageHeight, 
        		BufferedImage.TYPE_INT_RGB
        );
         
        // Create a graphics context on the buffered image
        g = image.createGraphics();
        
        setBackgroundColor(Color.WHITE);
        
        // Clear the image.
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        
        AffineTransform transform = new AffineTransform(); 
        
        transform.translate(
        		image.getWidth()/2,		
        		image.getHeight()/2		
        );
        
        transform.scale(scaleWidth, scaleHeight);
        
        g.transform(transform);
    }
*/    
	public void createBufferedImage(Icon icon) {
		
		float imageWidth = IconConstants.IMAGE_SIZE-1;
		float imageHeight = IconConstants.IMAGE_SIZE-1;
		float iconWidth;
		float iconHeight;
		
		iconExtent = icon.getExtent();
		if (iconExtent.equals(Extent.NO_EXTENT)) {
/* 
 * 	G�r return h�r och returnera en dummybild i SWTImage om aman vill ha en dummybild
  	ist�llet f�r en tom ikon f�r ikoner som inte har n�got layer. 			
 */
			//return;
			iconWidth = IconConstants.DEFAULT_ICON_WIDTH;
			iconHeight = IconConstants.DEFAULT_ICON_HEIGHT;
		} else {
			iconExtent = icon.getBounds(iconExtent); 
			iconWidth = (float)iconExtent.getWidth() + 10;
    		iconHeight = (float)iconExtent.getHeight() + 10;
    		
		}
		if(outline) {
    		imageWidth=IconConstants.OUTLINE_IMAGE_SIZE-1;
    		imageHeight=IconConstants.OUTLINE_IMAGE_SIZE-1;	
    	}
		float scaleWidth = (float)(1.0f * imageWidth/iconWidth);
		float scaleHeight = (float)(1.0f * imageHeight/iconHeight);
	    image = new BufferedImage(
        		(int)imageWidth, 
        		(int)imageHeight, 
        		BufferedImage.TYPE_INT_RGB
        );
         
        // Create a graphics context on the buffered image
        g = image.createGraphics();
        
        setBackgroundColor(Color.WHITE);
        
        // Clear the image.
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        
        AffineTransform transform = new AffineTransform(); 
        
        transform.translate(
        		image.getWidth()/2,
        		image.getHeight()/2		
        );
        transform.scale(
        		0.97*(imageWidth/(iconWidth+1)), 
        		0.97*(imageHeight/(iconHeight+1))
        );
//        transform.scale(scaleWidth, scaleHeight);
        g.transform(transform);
        
        icon.draw(this);
    }

	/**	
	 * Creates a BufferedImage representation of this object's ClassIcon.
	 */

	public BufferedImage getBufferedImage() {
		return image;
	}
	
	private double getCurrentScaleFactor() {
		return Math.abs(Math.min(
				g.getTransform().getScaleX(), 
				g.getTransform().getScaleY()
		));
	}
	
	/**
	 * Draws the specified Text primitive in this object's Graphics2D
	 * context.
	 * @param t
	 */
	public void drawText(Text t, Icon icon) {
		
		boolean debugText = false;
	
		Extent extent = t.getExtent();
		int x = (int)(extent.getP1().getX());
		int y = -(int)(extent.getP2().getY());		
		int extentHeight = (int)t.getExtent().getHeight();
		int extentWidth = (int)t.getExtent().getWidth();
		
		g.setFont(this.setFont(t));
		String text = t.getTextString();
			
		// if fontsize is not set the text is scaled to fit its extent
		FontMetrics metrics;
		if(t.getFontSize() == 0)
		{
			// scale text to fit extext horizontally.
			int fontsize = g.getFont().getSize();
			int newSize = fontsize-(fontsize-extentHeight);
			g.setFont(g.getFont().deriveFont((float)newSize));
			
			// scale text to fit extent vertically.
			Font font = g.getFont();
			metrics = g.getFontMetrics(); 
			while (metrics.stringWidth(text) > extentWidth) {
				newSize = g.getFont().getSize()-1;
				font = g.getFont().deriveFont((float)newSize);
				g.setFont(font);
				metrics = g.getFontMetrics();		
			}
		}
		// place text in center 
		metrics = g.getFontMetrics();
		y += (int)((extentHeight+g.getFont().getSize())/2.0);
		if (t.getHorizontalAlignment() == TextAlignment.CENTER) {
			x += (int)((extentWidth-metrics.stringWidth(text))/2.0);
		} else if (t.getHorizontalAlignment() == TextAlignment.RIGHT) {
			x += extentWidth-metrics.stringWidth(text);
		}
		
		// Check if the text is large enough to be drawn.
		double currentScaleFactor = getCurrentScaleFactor();
		double actualFontSize = currentScaleFactor*(double)(g.getFont().getSize());
		boolean largeEnough = actualFontSize >= MINIMUM_FONT_SIZE;
		
		// Check if the text consists of only one character.
		boolean oneChar = text.length() == 1;
		
		// Abort if the text should not be drawn.
		if (!largeEnough  && !oneChar) {
			return;
		}
		
		// If the text consists of only one character, enlarge it. 
		if (oneChar) {
			double oldActualFontSize = actualFontSize;
			double oldActualTextWidth = 
				g.getFontMetrics().stringWidth(text)*currentScaleFactor;
			double fontSize = g.getFont().getSize2D(); 
			while (actualFontSize < MINIMUM_FONT_SIZE) {
				fontSize++;
				actualFontSize = currentScaleFactor*fontSize;
			}
			g.setFont(g.getFont().deriveFont((float)fontSize));
			double actualFontSizeDiff = actualFontSize-oldActualFontSize;
			double newActualTextWidth = 
				g.getFontMetrics().stringWidth(text)*currentScaleFactor;
			double actualTextWidthDiff = newActualTextWidth-oldActualTextWidth;
			double dx = (int)Math.round((actualTextWidthDiff/2.0)/currentScaleFactor);
			double dy = (int)Math.round((actualFontSizeDiff/2.0)/currentScaleFactor);
			x -= dx;
			y += dy;
		}
		
		// Draw the text string.
		setColor(t.getLineColor());
		if (debugText) {
			try {
				g.draw(createShape(t));
			} catch(Exception e){}
		}
//		if (t.getFillPattern() != FillPattern.NONE) {
//			drawShape(t);
//		}
		g.drawString(text, x, y);
	}
		
	public void drawBitmap(Bitmap b) {
		BufferedImage bitmapImage = null;
//		System.out.println("drawing bitmap. filename = " + b.getFileName());
		if (b.getFileName() != null) {
			try {
				bitmapImage = ImageIO.read(new File(b.getFileName()));
			} 
			catch (MalformedURLException e) {
//				System.out.println(e.getClass().getName() + ", " + e.getMessage());
			} 
			catch (IOException e) {
//				System.out.println(e.getClass().getName() + ", " + e.getMessage());
			}
		} else if (b.getImageSource() != null) {
			try {
				byte decodedBytes[] = 
					new sun.misc.BASE64Decoder().decodeBuffer(b.getImageSource());
				InputStream in = new ByteArrayInputStream(decodedBytes);  
				bitmapImage = ImageIO.read(in);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		Extent extent = b.getExtent().fix();
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		);
		g.drawImage(
				bitmapImage, 
				(int)extent.getP1().getX(), 
				(int)extent.getP1().getY(), 
				(int)extent.getP2().getX(), 
				(int)extent.getP2().getY(),
				0,
				0,
				bitmapImage.getWidth(),
				bitmapImage.getHeight(),
				null
		);
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF
		);
	}
	
	/**
	 * Draws the specified MSLLine primitive in this object's Graphics2D
	 * context.
	 * @param t
	 */
	public void drawLine(Line l) {
		ArrayList<Point> points = l.getPoints();
		if(points.size() > 2 && l.getSmooth().equals(Types.Smooth.BEZIER)) {
			this.drawBezier(l);
		} else {	
			setColor(l.getColor());
			Stroke newStroke = this.getLineStroke(l.getLinePattern(), l.getThickness());
			g.setStroke(newStroke);
			g.drawPolyline(GraphicsUtil.getXLinePoints(points),
					GraphicsUtil.getYLinePoints(points), points.size());
			Polygon[] arrows = l.getArrowPolygons();
			for (int i = 0; i < arrows.length; i++) {
				if (arrows[i] != null) {
					drawShape(arrows[i]);
				}
			}
		}
	}

	/**
	 * Draws the specified shape in this object's Graphics2D context.
	 * @param t
	 */
	public void drawShape(FilledShape s) {
		Shape shape;
		try {
			shape = createShape(s);
		} catch (CreateShapeFailedException e) {
//			System.out.println(e.getMessage());
			return;
		}
		
		Types.FillPattern fillPattern = s.getFillPattern();
		if (!fillPattern.equals(FillPattern.NONE)) {
			Paint oldPaint = g.getPaint();
			if (fillPattern.equals(FillPattern.SOLID)) {
				setColor(s.getFillColor());
			} else {
				try {
					Paint p = getFillPaint(s);
					g.setPaint(p);
				} catch(IllegalArgumentException e) {
//					System.out.println(e);
				}
			}
			g.fill(shape);
			g.setPaint(oldPaint);
		}

		boolean gradient = (s.getFillPattern() == FillPattern.HORIZONTALCYLINDER ||
							s.getFillPattern() == FillPattern.VERTICALCYLINDER ||
							s.getFillPattern() == FillPattern.SPHERE);
		if (s.getLinePattern() != LinePattern.NONE && !gradient) {
			Stroke newStroke = getLineStroke(s.getLinePattern(), s.getLineThickness()); 
			g.setStroke(newStroke);
			setColor(s.getLineColor());
			g.draw(shape);
		}
		
		//TODO testa stroke till borderPattern
		// + skapa en ny rektangel utanf�r orginalrektangeln
		if(s instanceof Rectangle)
		{
			Rectangle r = (Rectangle) s;
			Types.BorderPattern borderPattern = r.getBorderPattern(); 
			if(borderPattern != Types.BorderPattern.NONE)
			{
				ArrayList<Point> points; 
				try
				{
					points = GraphicsUtil.getBorderPatternPoints(r, borderPattern);
				}catch(FailedConstructionException e)
				{
//					System.out.println("BorderPattern " + borderPattern + " is not implemented yet" );
					return;
				}
				setColor(Color.BLACK);
				//returnerar default linestroke
				Stroke newStroke = getLineStroke(Types.LinePattern.SOLID, IconConstants.BORDER_PATTERN_THICKNESS); 
				g.setStroke(newStroke);
				g.drawPolyline(
						GraphicsUtil.getXLinePoints(points),
						GraphicsUtil.getYLinePoints(points), 
						points.size()
				);
			}
		}
	}
	
	private void drawBezier(Line l) 
	{	
		ArrayList<Point> linePoints = l.getPoints();
		for(Point lp : linePoints)
		{
			/* invert y to compensate java */
			lp.setY(-(lp.getY()));
		}
		ArrayList<Point> bezierPoints = new ArrayList<Point>();
		for(int i = 0; i < linePoints.size()-1; i++)
		{
			double x = (linePoints.get(i).getX() + linePoints.get(i+1).getX()) / 2;
			double y = (linePoints.get(i).getY() + linePoints.get(i+1).getY()) / 2;
			bezierPoints.add(new Point(x, y));
		}
		GeneralPath gp = new GeneralPath();
		gp.moveTo(linePoints.get(0).getX(), linePoints.get(0).getY());
		gp.lineTo(bezierPoints.get(0).getX(), bezierPoints.get(0).getY());
		for(int i = 1; i < bezierPoints.size(); i++)
		{
			gp.quadTo(linePoints.get(i).getX(), linePoints.get(i).getY(), 
					bezierPoints.get(i).getX(), bezierPoints.get(i).getY());
		}
		gp.lineTo(linePoints.get(linePoints.size()-1).getX(), 
				linePoints.get(linePoints.size()-1).getY());
		
		setColor(l.getColor());
		Stroke newStroke = getLineStroke(l.getLinePattern(), l.getThickness()); 
		if (newStroke != null) {
			g.setStroke(newStroke);
		}
		g.draw(gp);
	}
	/**
	 * Applies the transformation of the specified component's icon layer 
	 * specification.
	 * @param component
	 */
	public void setTransformation(Component comp)
	{	
		Placement placement = comp.getPlacement();
		Icon icon = comp.getIcon();
		AffineTransform transform = g.getTransform();
		
		Extent iconTransExtent = placement.getTransformation().getExtent();
		CoordinateSystem coordinateSystem = icon.getLayer().getCoordinateSystem();
		if (coordinateSystem == null)
			return;
		
		Extent iconCordExtent = coordinateSystem.getExtent(); // TODO f�r Nullpointer h�r f�r instanstr�det.
		
		
		//System.out.println("S�tter transformation f�r " + icon.getClassName());
		//System.out.println("iconTransExtent = " + iconTransExtent);
		//System.out.println("iconCordExtent = " + iconCordExtent);
		
		double iconTransExtHeight = iconTransExtent.getHeight();
		double iconTransExtWidth = iconTransExtent.getWidth();
			
		double iconCordExtHeight = iconCordExtent.getHeight();	
		double iconCordExtWidth = iconCordExtent.getWidth();
	
		//System.out.println("Skalar bredd med faktor = " + iconTransExtWidth/iconCordExtWidth);
		//System.out.println("Skalar h�jd med faktor = " + iconTransExtHeight/iconCordExtHeight);
		
		double originX = placement.getTransformation().getOrigin().getX() + iconTransExtent.getMiddle().getX(); 
		double originY = placement.getTransformation().getOrigin().getY() + iconTransExtent.getMiddle().getY();	
		

		transform.translate(originX, -originY);
		// transform flipping
		//horizontally
		if(iconTransExtent.getP2().getX() < iconTransExtent.getP1().getX())
		{
			
			transform.scale(-1.0, 1.0);
			
		}
		//vertically
		if(iconTransExtent.getP2().getY() < iconTransExtent.getP1().getY())
		{
			
			transform.scale(1.0, -1.0);
		}
		
		transform.scale(iconTransExtWidth/iconCordExtWidth, iconTransExtHeight/iconCordExtHeight);
		// invert rotation to compensate java 
		transform.rotate(-placement.getTransformation().getRotation() * Math.PI/180);
		g.setTransform(transform);
	}
	public void saveTransformation() {
		savedTransformations.push(g.getTransform());
	}
	
	public void resetTransformation() {
		g.setTransform(savedTransformations.pop());
	}
	
	/**
	 * Creates a Paint object that matches the shape's colors and fill pattern.
	 * 
	 * @param shape
	 * @return
	 */
	private Paint getFillPaint(FilledShape shape) {
		if (shape.getFillPattern() == FillPattern.HORIZONTAL
				|| shape.getFillPattern() == FillPattern.VERTICAL
				|| shape.getFillPattern() == FillPattern.FORWARD
				|| shape.getFillPattern() == FillPattern.BACKWARD
				|| shape.getFillPattern() == FillPattern.CROSS
				|| shape.getFillPattern() == FillPattern.CROSSDIAG) {
			return getTextureFillPaint(shape);
		}
		Paint paint;
		Extent extent; 		
		if (shape instanceof FilledRectShape) {
			extent = ((FilledRectShape) shape).getExtent();
		} else if (shape instanceof Polygon) {
			extent = ((Polygon) shape).getBounds();
		} else {
			throw new IllegalArgumentException("getFillPaint");
		}
		double width = extent.getWidth();
		double height = extent.getHeight();
		if (shape.getFillPattern() == FillPattern.VERTICALCYLINDER
				|| shape.getFillPattern() == FillPattern.HORIZONTALCYLINDER) {
			Point2D p1 = new Point2D.Double(extent.getP1().getX(), extent.getP1()
					.getY());
			Point2D p2 = new Point2D.Double(extent.getP2().getX(), extent.getP2()
					.getY());
			if (shape.getFillPattern() == FillPattern.VERTICALCYLINDER) {
				p1.setLocation(p1.getX(), p1.getY() + 0.5 * height);
				p2.setLocation(p2.getX() - 0.5 * width, p2.getY() - 0.5
						* height);
			} else if (shape.getFillPattern() == FillPattern.HORIZONTALCYLINDER) {
				p1.setLocation(p1.getX() + 0.5 * width, p1.getY());
				p2.setLocation(p2.getX() - 0.5 * width, p2.getY() - 0.5
						* height);
			}
			// Flip the y-axis of the points.
			p1.setLocation(p1.getX(), -p1.getY());
			p2.setLocation(p2.getX(), -p2.getY());
			paint = new GradientPaint(
					p1, 
					translateColor(shape.getLineColor()), 
					p2,
					translateColor(shape.getFillColor()), 
					true
			);
		} else if (shape.getFillPattern() == FillPattern.SPHERE) {
			float[] fractions = { 0.0f, 1.0f };
			java.awt.Color[] colors = {
					translateColor(shape.getFillColor()), 
					translateColor(shape.getLineColor()) 
			};
			double ymin = iconExtent.getP1().getY();
			double ymax = iconExtent.getP2().getY();
			double x = extent.getP1().getX();
			double y2 = extent.getP2().getY();
			double y = ymin+Math.abs(ymax-y2);
			paint = new RadialGradientPaint(
					new Rectangle2D.Double(
							x-width/2.0, 
							y-height/2.0, 
							2*width, 
							2*height
					),
					fractions, 
					colors, 
					CycleMethod.NO_CYCLE
			);
		} else {
			throw new IllegalArgumentException("getFillPaint");
		}
		return paint;
	}
	/**
	 * Creates a TexturePaint object that matches the shape's colors and fill
	 * pattern.
	 * 
	 * @param s
	 * @return
	 */
	private Paint getTextureFillPaint(FilledShape s) {
        
        double currentScaleX = g.getTransform().getScaleX();
        double currentScaleY = g.getTransform().getScaleY();
        double minScale = Math.min(currentScaleX, currentScaleY);
		int baseTextureSize = 7;
		int textureSize = (int)(1.0*baseTextureSize/minScale);
        int anchorWidth = (int)(1.0*baseTextureSize/currentScaleX);
        int anchorHeight = (int)(1.0*baseTextureSize/currentScaleY);
		
		BufferedImage img = new BufferedImage(
				textureSize, 
				textureSize, 
				BufferedImage.TYPE_INT_RGB
		);

		Graphics2D graphics = img.createGraphics();
		
		graphics.setColor(translateColor(s.getFillColor()));
		graphics.fillRect(0, 0, textureSize, textureSize);
		
		graphics.setColor(translateColor(s.getLineColor()));
		
		graphics.setStroke(
				new BasicStroke(
						(float)(2.0f/(currentScaleX+currentScaleY)),
						BasicStroke.CAP_BUTT, 
						BasicStroke.JOIN_MITER, 
						10.0f, 
						null, 
						0.0f
				)
		);
		if (s.getFillPattern().equals(FillPattern.BACKWARD)) {
			graphics.drawLine(0, 0, textureSize, textureSize);
			graphics.drawLine(0, textureSize-1, 1, textureSize);
			graphics.drawLine(textureSize-1, 0, textureSize, 1);
		} else if (s.getFillPattern().equals(FillPattern.FORWARD)) {
			graphics.drawLine(textureSize, 0, 0, textureSize);
			graphics.drawLine(1, 0, 0, 1);
			graphics.drawLine(textureSize-1, textureSize, textureSize, textureSize-1);
		} else if (s.getFillPattern().equals(FillPattern.CROSSDIAG)) {
			graphics.drawLine(0, 0, textureSize, textureSize);
			graphics.drawLine(textureSize, 0, 0, textureSize);
			graphics.drawLine(0, textureSize-1, 1, textureSize);
			graphics.drawLine(textureSize-1, 0, textureSize, 1);
			graphics.drawLine(textureSize, 0, 0, textureSize);
			graphics.drawLine(1, 0, 0, 1);			
		} else if (s.getFillPattern().equals(FillPattern.HORIZONTAL)) {
			graphics.drawLine(0, textureSize/2, textureSize, textureSize/2);
		
		} else if (s.getFillPattern().equals(FillPattern.VERTICAL)) {
			graphics.drawLine(textureSize/2, 0, textureSize/2, textureSize);
		
		} else if (s.getFillPattern().equals(FillPattern.CROSS)) {
			graphics.drawLine(0, textureSize/2, textureSize, textureSize/2);
			graphics.drawLine(textureSize/2, 0, textureSize/2, textureSize);
		}
		
		return new TexturePaint(
				img, 
				new java.awt.Rectangle(
						0, 
						0,
						anchorWidth, 
						anchorHeight
				)
		);
	}
	
	public Image getImage() /*throws FailedConstructionException*/ {

	    ImageData imagedata = null;
		if(image == null)
		{
			return ImageLoader.getImage("dummy.png");
			//throw new FailedConstructionException("SWTImage");
		}
		
//		try { 
//	    	ImageIO.write(
//	    			image, 
//	    			"png", 
//	    			new File(
//	    					"C:/tmp/images/" + this.hashCode() + ".png"
//	    			)
//	    	);
//		} catch (IOException e) {
//			System.out.println(e);
//		}
		
	    if (image.getColorModel() instanceof DirectColorModel) {
	        DirectColorModel colorModel
	                = (DirectColorModel) image.getColorModel();
	        PaletteData palette = new PaletteData(colorModel.getRedMask(),
	                colorModel.getGreenMask(), colorModel.getBlueMask());
	        
	        	        
	        imagedata = new ImageData(IconConstants.OUTLINE_IMAGE_SIZE,
	        		IconConstants.OUTLINE_IMAGE_SIZE, colorModel.getPixelSize(),
	                palette);
	   
	    	
	        
	        
	        for (int y = 0; y < imagedata.height; y++) {
	            for (int x = 0; x < imagedata.width; x++) {
	                int pixel = palette.getPixel(new RGB(255, 255, 255));
	                //l�mna tom kant nere och till v�nster
	                imagedata.setPixel(x, y, pixel);
	            }
	        }
	      
	        WritableRaster raster = image.getRaster();
	        int[] pixelArray = new int[3];
	        for (int y = 0; y < image.getHeight(); y++) {
	            for (int x = 0; x < image.getWidth(); x++) {
	                raster.getPixel(x, y, pixelArray);
	                int pixel = palette.getPixel(new RGB(pixelArray[0],
	                        pixelArray[1], pixelArray[2]));
	                //l�mna tom kant nere och till v�nster
	                imagedata.setPixel(x+1, y, pixel);
	            }
	        }
	    }
	    else
	    {	
	    	//throw new FailedConstructionException("SWTImage");
	    	
	    }
		ImageDescriptor desc = ImageDescriptor.createFromImageData(imagedata);
		return desc.createImage(); 
	}	
	public Font setFont(Text text) {
		
		Font font = new Font(text.getFontName(), DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE);
		double textFontSize = text.getFontSize();
		if (textFontSize != 0) {
		font = font.deriveFont((float) textFontSize);
		}
		Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		boolean bold = false;
		boolean italic = false;
		if (text.getTextStyle() != null) {
			for (Types.TextStyle ts : text.getTextStyle())
			{
				if (ts == Types.TextStyle.UNDERLINE) {
					map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				}
				if (ts == Types.TextStyle.BOLD) {
					bold = true;
				}
				if (ts == Types.TextStyle.ITALIC) {
					italic = true;
				}
			}
			if (bold && italic) {
				font = font.deriveFont(Font.ITALIC | Font.BOLD);
			} else if (bold) {
				font = font.deriveFont(Font.BOLD);
			} else if (italic) {
				font = font.deriveFont(Font.ITALIC);
			}
		}
		font = font.deriveFont(map);
		return font;
	}
	
	private Shape createShape(Text t) throws CreateShapeFailedException {
		Extent extent = t.getExtent();
		if (extent.equals(Extent.NO_EXTENT)) {
			throw new CreateShapeFailedException("MLSText");
		}
		return new java.awt.Rectangle(
			(int)(extent.getP1().getX()),
			-(int)(extent.getP2().getY()),
			(int)(extent.getP2().getX()-extent.getP1().getX()),
			(int)(extent.getP2().getY()-extent.getP1().getY())
		);
	}

	private Shape createShape(Rectangle r) throws CreateShapeFailedException {
		Extent extent = r.getExtent();
		if (extent.equals(Extent.NO_EXTENT)) {
			throw new CreateShapeFailedException("MLSRectangle");
		}
		return new java.awt.Rectangle(
			(int)(extent.getP1().getX()),
			-(int)(extent.getP2().getY()),
			(int)(extent.getP2().getX()-extent.getP1().getX()),
			(int)(extent.getP2().getY()-extent.getP1().getY())
		);
	}
	
	private Shape createShape(Polygon p) throws CreateShapeFailedException {
		ArrayList<Point> points = p.getPoints();
		if (points.isEmpty()) {
			throw new CreateShapeFailedException("MLSPolygon");
		}
		int npoints = points.size();
		int [] xpoints = new int [npoints];
		int [] ypoints = new int [npoints];
		for(int i = 0; i < npoints; i++)
		{
			xpoints[i] = (int)points.get(i).getX();
		}
		for(int i = 0; i < npoints; i++)
		{
			ypoints[i] = -((int)points.get(i).getY());
		}
		return new java.awt.Polygon(xpoints, ypoints, npoints);
	}
	
	private Shape createShape(Ellipse e) throws CreateShapeFailedException {
		Extent extent = e.getExtent();
		if (extent.equals(Extent.NO_EXTENT)) {
			throw new CreateShapeFailedException("MLSEllipse");
		}
		return new Ellipse2D.Double(
				extent.getP1().getX(),
				-(extent.getP2().getY()),
				extent.getP2().getX()-extent.getP1().getX(),
				extent.getP2().getY()-extent.getP1().getY()
		);
	}
	
	private Shape createShape(FilledShape s) throws CreateShapeFailedException {
		if (s instanceof Ellipse) {
			return createShape((Ellipse)s);
		} else if (s instanceof Rectangle) {
			return createShape((Rectangle)s);
		} else if (s instanceof Polygon) {
			return createShape((Polygon)s);
		} else {
			return createShape((Text)s);
		}
	}
	public Stroke getLineStroke(LinePattern linepattern, double thicknessInMM) {
		double compensatedThicknessInMM = thicknessInMM/getCurrentScaleFactor();
		float thicknessInPixles = (float)(compensatedThicknessInMM*IconConstants.PIXLES_PER_MM);
		return new BasicStroke(thicknessInPixles, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, linepattern.getDash(), 0.0f);
	}
	
	public void setColor(Color color) {
		g.setColor(translateColor(color));
	}
	
	public void setBackgroundColor(Color color) {
		g.setBackground(translateColor(color));
	}	
	
	private java.awt.Color translateColor(Color color) {
		return new java.awt.Color(color.getR(), color.getG(), color.getB());
	}
}